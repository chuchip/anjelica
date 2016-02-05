package gnu.chu.isql;

import gnu.chu.anjelica.pad.pdusua;
import java.io.*;
import java.util.*;
import gnu.chu.sql.*;
import gnu.chu.utilidades.SystemOut;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>Titulo: utilSql </p>
 * <p>Descripción: Utiliadades varias para trabajar con SQL.<br>
 *  Esto incluye rutinas para exportar/importar tablas </p>
 * <p>Copyright: Copyright (c) 2005-2010
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los terminos de la Licencia Pública General de GNU según es publicada por
 *  la Free Software Foundation, bien de la versión 2 de dicha Licencia
 *  o bien (según su elección) de cualquier versión posterior.
 *  Este programa se distribuye con la esperanza de que sea útil,ed
 *  pero SIN NINGUNA GARANTIA, incluso sin la garantía MERCANTIL implícita
 *  o sin garantizar la CONVENIENCIA PARA UN PROPOSITO PARTICULAR.
 *  Véase la Licencia Pública General de GNU para más detalles.
 *  Debería haber recibido una copia de la Licencia Pública General junto con este programa.
 *  Si no ha sido así, escriba a la Free Software Foundation, Inc.,
 *  en 675 Mass Ave, Cambridge, MA 02139, EEUU.
 * </p>
 * <p>Empresa: MISL</p>
 * @author chuchiP
 * @version 1.0
 */

public class utilSql
{
  public String delimitador = "\t";
  public int nLin;
  DatosTabla dt;
  String tabla;
  String fecfor;
  Statement stUp;
  public boolean muerto = false;
  public Exception excep = null;
  public int nMsg=50;
  boolean swTrim=true;
  boolean append=false;

  public utilSql()
  {
  }
  
  public ArrayList load(String fichero, String delimit) throws IOException
  {
    String s;
    FileReader fr=new FileReader(fichero);
    BufferedReader bfr =new BufferedReader(fr);
    nLin=0;
    ArrayList datos=new ArrayList();
    ArrayList v;
    delimitador=delimit;
    while ((s=bfr.readLine())!=null)
    {
        nLin++;
        if (s.trim().equals(""))
          continue;
        v=insertaLinea(s);
        
        if (muerto)
        {
          bfr.close();
          return null;
        }
        datos.add(v);
    }
    bfr.close(); 
    return datos;
  }
  public void load(String fichero) throws Throwable
  {
    load(fichero, null,null, null,"dd-MM-yyyy");
  }

  public void load(String fichero,String tabla,DatosTabla datTab,Statement stat,String fecfor) throws Throwable 
  {
    String s;
    dt=datTab;
    stUp=stat;
    excep=null;
    muerto=false;
    this.tabla=tabla;
    this.fecfor=fecfor;
    nLin=0;
    dt.addNew(tabla);
    FileReader fr=new FileReader(fichero);
    BufferedReader bfr =new BufferedReader(fr);
    ArrayList v;
    nLin=0;
    while ((s=bfr.readLine())!=null)
    {
        nLin++;
        if (s.trim().equals(""))
          continue;
        v=insertaLinea(s);
        if (muerto)
          return;
        procesaLinea(v);
    }
    bfr.close();
  }

  public ArrayList insertaLinea(String linea) 
  {
    ArrayList v= new ArrayList();
    int po=0;
      int pf=0;
      String dato;
      while (pf!=-1)
      {
        pf=linea.indexOf(delimitador,po);
        if (pf==-1)
            dato=linea.substring(po);
        else
        {
          dato=linea.substring(po,pf);
          po=pf+1;
        }
        dato=dato.replaceAll("\\\\n","\n");
        if (delimitador.charAt(0)=='\t')
          dato=dato.replaceAll("\\\\t","\t");
        dato=dato.replaceAll("\\\\'","'");
        v.add(dato);
      }
      
      return v;
  }

  protected void procesaLinea(ArrayList v) throws SQLException
  {
    if (muerto)
      return;
    try
    {
      procesLin(v);
    } catch (SQLException k)
    {
      System.out.println("Linea: "+nLin);
       SystemOut.print(k);
      muerto=true;
      excep=k;
      throw k;
    }
  }
  public static int getTipo(int tc)
  {
    switch (tc) {
      case (Types.DATE):
      case (Types.TIMESTAMP):
        tc = Types.DATE;
        break;
      case (Types.TIME):
        tc = Types.TIME;
        break;
      case (Types.DECIMAL):
      case (Types.DOUBLE):
      case (Types.FLOAT):
      case (Types.INTEGER):
      case (Types.NUMERIC):
      case (Types.REAL):
      case (Types.SMALLINT):
        tc = Types.DECIMAL;
        break;
      default:
        tc = Types.CHAR;
        break;
    }
    return tc;
  }

  void procesLin(ArrayList v) throws SQLException
  {
    String s,valor;
    s = "INSERT INTO " + tabla + " VALUES (";

      for (int n = 0; n < v.size(); n++)
      {
        valor = v.get(n).toString();
        if (utilSql.getTipo(dt.getTipCampo(n+1)) == Types.CHAR)
          s += "'" + valor + "',";
        else if (utilSql.getTipo(dt.getTipCampo(n+1)) == Types.DECIMAL)
        {
          valor = valor.replace(',', '.');
          try
          {
            Double.parseDouble(valor);
          }
          catch (NumberFormatException k)
          {
            valor = "null";
          }
          s += valor + ",";
        }
        else if (utilSql.getTipo(dt.getTipCampo(n+1)) == Types.TIME)
        {
          s += "null,";
        } else
        {
          if (valor.trim().equals(""))
            s += "null,";
          else
          {
            if (valor.length() > 10)
              valor = valor.substring(0, 10);
            s += "TO_DATE('" + valor + "','" + fecfor + "'),"; // Date
          }
        }
      }
      s = s.substring(0, s.length() - 1) + ")";
      dt.setSqlUpdate(s);

      stUp.executeUpdate(dt.getSqlUpdate());

      if (nLin % nMsg == 0)
        incMsg();
    
    }

    protected void incMsg()
    {

    }

    public boolean unload(String fichero, DatosTabla dt, String sql) throws Throwable
    {
      String fecfor = "dd-MM-yyyy";
      return unload(fichero, dt, sql, fecfor);
    }
    /**
   * Realiza una Unload de una setencia sql.
   * Devuelve false si no ENCONTRO ningun Registro.
   */
  public boolean unload(String fichero,DatosTabla dt,String sql,String fecfor) throws Throwable
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
      for (int n=1;n<=nc;n++)
      {
        campo="";
        if (dt.getDatos(n)!=null)
        {
          if (dt.getTipCampo(n)==Types.DATE || dt.getTipCampo(n)==Types.TIMESTAMP)
            campo=dt.getFecha(n,fecfor);
          else
            campo=dt.getString(n,swTrim);
        }
        campo=campo.replaceAll("\\\\","\\\\\\\\'");
        campo=campo.replaceAll("\n","\\\\n");
        if (delimitador.charAt(0)=='\t')
          campo=campo.replaceAll(delimitador,"\\\\t"); // A piñon Fijo si el delimitador es otro no funcionara
        campo=campo.replaceAll("'","\\'");

        if (n!=nc)
          campo+=delimitador;
        fr.write(campo);
      }
      fr.write("\n");
      nLin++;
      if (nLin % nMsg == 0)
        incMsg();

    } while (dt.next());
    fr.close();
    return true;
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
      this.fecfor=formFecha;
  }
  public String getFormFecha()
  {
      return fecfor;
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
 

  public static Vector dtToVLike(DatosTabla dt) throws Throwable
  {
    Vector v = new Vector();

    dt.first();

    if (!dt.getNOREG())
    {
      do
      {
        vlike lk = new vlike();
        lk.setDatosTabla(dt);

        v.addElement(lk);
      }
      while (dt.next());
    }
    return v;
  }
 
  /**
   * Esta función solo se debe utilizar para crear la base de datos en modo pruebas.
   *
   * @throws Exception
   * @throws <any>
   */
  public static void executeScript(String fichero,Statement st,boolean debug) throws Exception
  {
    FileReader fr = new FileReader(fichero);
    BufferedReader bfr = new BufferedReader(fr);
    String sql="";
    int nLin = 0;
    try  {
      String linea;
      while ( (linea = bfr.readLine()) != null)
      {
        nLin++;
        if (linea.trim().equals(""))
          continue;
        if (linea.startsWith("--")) // Linea de Comentario
          continue;
        if (linea.indexOf("--")!=-1)
        {
          linea=linea.substring(0,linea.indexOf("--"));
        }
        sql+=linea;
        if (linea.indexOf(");") != -1)
        {
          if (!sql.trim().equals(""))
          {
            if (debug)
              System.out.println("* " +sql+" *");
            st.executeUpdate(sql);
            Thread.sleep(50);
//            st.getConnection().commit();
          }
          sql="";
        }
      }
    } catch (SQLException k)
    {
      bfr.close();
       SystemOut.print(k);
      throw new SQLException("Error al parsear sentencia: "+sql+" en linea: "+nLin);
    }
    bfr.close();
  }
  
  public static void regenerarPermisosAll(Connection ct)
    {
      try
      {
          DatosTabla dtCon1=new DatosTabla(ct);
          DatosTabla dt=new DatosTabla(ct);
          
          String s="select usu_nomb from usuarios where usu_activ='S'";
          if (!dtCon1.select(s))
              return;
          do
          {
              darPermisos(dtCon1.getString("usu_nomb"),dt.getStatement(),dt);
          } while (dtCon1.next());
      } catch (SQLException ex)
      {
          Logger.getLogger(pdusua.class.getName()).log(Level.SEVERE, null, ex);
      }
        
    }
    public static void darPermisos(String usuNomb,Statement stUp,DatosTabla dtCon1) throws SQLException {
        String dbName=stUp.getConnection().getCatalog();
        stUp.executeUpdate("grant connect ON DATABASE  "+dbName +" TO " +usuNomb);
        stUp.executeUpdate("grant all on schema anjelica to  " + usuNomb);
        stUp.executeUpdate("grant all on all tables in schema anjelica to " + usuNomb);
        // Dando permisos en secuencias 
        if (dtCon1.select("SELECT c.relname FROM pg_class c WHERE c.relkind = 'S'"))
        {
            do
            {
                stUp.executeUpdate("grant all on "+dtCon1.getString("relname")+
                    " to " + usuNomb);
            } while (dtCon1.next());
        }
        // Dando permisos a las vistas.
        if (dtCon1.select("select viewname from pg_views where schemaname in ('anjelica')"))
        {
            do
            {
                stUp.executeUpdate("grant all on "+dtCon1.getString("viewname")+
                    " to " +  usuNomb);
            } while (dtCon1.next());
        }
    }
}
