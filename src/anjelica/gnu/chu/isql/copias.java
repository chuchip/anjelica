package gnu.chu.isql;

import gnu.chu.Menu.*;
import gnu.chu.sql.*;
import gnu.chu.utilidades.*;
import java.io.*;
import java.sql.*;

public class copias
{
  boolean swTrim=true;
  boolean append=false;
  int nLin;
  String dirTrabajo;
  EntornoUsuario EU;
  String delimitador="|";
  String formFecha="dd-MM-yyyy";

  public copias()
  {
  }
  public copias(EntornoUsuario eu,String dirTrabajo,String delim)
  {
      iniciar(eu,dirTrabajo,delim);
  }
  public void iniciar(EntornoUsuario eu,String dirTrabajo,String delim)
  {
      EU=eu;
      if (dirTrabajo != null)
        this.dirTrabajo=dirTrabajo;
      if (delim!=null)
        this.delimitador=delim;
  }
  public int getNumLineas()
  {
      return nLin;
  }
  public void setDelimitador(String delim)
  {
      delimitador=delim;
  }
  public String getDelimitador()
  {
      return delimitador;
  }
  public void setFormFecha(String formFecha)
  {
      this.formFecha=formFecha;
  }
  public String getFormFecha()
  {
      return formFecha;
  }
  /**
   * Carga en En una tabla los datos cogidos de un fichero. Utiliza dt para realizar la
   * insert NO realiza COMMIT
   */

  public void load(String fichero,DatosTabla dt,String tabla) throws Throwable
  {
    String s;
    dt.select("select * from "+tabla);
    FileReader fr=new FileReader(fichero);
    BufferedReader bfr =new BufferedReader(fr);
    nLin=0;
    while ((s=bfr.readLine())!=null)
    {
        nLin++;
        if (s.trim().equals(""))
          continue;
        insertaLinea(s,dt,tabla);
//          selectE.setText(selectE.getText()+s);
    }
    bfr.close();
  }

  public void insertaLinea(String linea,DatosTabla dt,String tabla) throws Throwable
  {
      dt.addNew(tabla);
//      System.out.println(linea);
      int nc=dt.getNumCol();
      int po=0;
      int pf;
      String dato="";
//      System.out.println("N� Columnas: "+nc);
      for (int n=0;n<nc;n++)
      {
        pf=linea.indexOf(delimitador,po);
        if (pf==-1)
          if (n+1!=nc)
            throw new Exception("N. Campos NO coinciden con los de la tabla en Linea: "+nLin);
          else
          {
            dato=linea.substring(po);
            nc=0;
          }
        else
        {
          dato=linea.substring(po,pf);
          po=pf+1;
        }
//        System.out.println("Campo: "+n+" Dato: "+dato);
        if (dt.getTipCampo(n)==Types.DATE)
          dt.setDato(n,dato,formFecha);
        else
        {
          if (dato.equals(""))
          {
//            System.out.println("Campo: "+n+" null");
            dt.setDato(n,null);
          }
          else
            dt.setDato(n,dato);
        }
      }
      dt.update(dt.getStatement());
  }
  public void setAnadir(boolean append)
  {
      this.append=append;
  }
  public boolean getAnadir()
  {
      return this.append;
  }
  public void setTrim(boolean trim)
  {
      swTrim=trim;
  }
  public boolean getTrim()
  {
      return swTrim;
  }

  /**
   * Realiza una Unload de una setencia sql.
   * Devuelve false si no ENCONTRO ningun Registro.
   */
  public boolean unload(String fichero,DatosTabla dt,String sql) throws Throwable
  {
    nLin=0;
    FileWriter fr=new FileWriter(fichero,append);
    BufferedWriter bfr =new BufferedWriter(fr);
    if (! dt.select(sql))
      return false;
    int nc=dt.getNumCol();
    String campo;
    do
    {
      for (int n=0;n<nc;n++)
      {
        campo="";
        if (dt.getDatos(n)!=null)
        {
          if (dt.getTipCampo(n)==Types.DATE)
            campo=dt.getFecha(n,formFecha);
          else
            campo=dt.getString(n,swTrim);
        }
        if (n+1!=nc)
          campo+=delimitador;
        fr.write(campo);
      }
      fr.write("\n");
      nLin++;
    } while (dt.next());
    fr.close();
    return true;
  }
}
