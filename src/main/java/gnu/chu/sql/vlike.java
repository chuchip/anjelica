package gnu.chu.sql;

/**
*  Clase que emula el Like de Informix.
 *
 * <p>Título: pddespie </p>
 * <p>Descripción: Mantenimiento de Despieces </p>
 * <p>Copyright: Copyright (c) 2005-2011
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
* @author chuchiP 11/10/98
* @version 1.1
*/

import java.util.*;
import java.sql.*;
import java.util.zip.*;
import java.io.*;
import gnu.chu.utilidades.Fecha;
import gnu.chu.utilidades.Formatear;

public class vlike implements Serializable {
  String msgError;
  boolean error;
  Vector datos = new Vector();
        Vector nomCampo= new Vector(); // Nombres de Los Campos.
        Vector tipCampo= new Vector(); // Tipos de Los campos.
        Vector preCampo= new Vector(); // Precision del Campo.
        Vector decCampo= new Vector(); // Escala del Campo. (Numero de Decimales).
        Vector lonCampo= new Vector(); // Longitud del Campo. TIPO CHAR.
        int numcol=0;

  public vlike()
  {
  }

  public Object getDatos(int col) throws SQLException
  {
    error=false;
    if (numcol==0)
    {//
      msgError="getDatos: NO Hay Datos Activos";
      error=true;
      return null;
    }

    if (col < 0 || col >= numcol)
    {
      msgError = "getDatos: Columna NO Valida";
      error = true;
      return null;
    }

    try
    {
      return datos.elementAt(col);
    }
    catch (Exception k)
    {
      msgError = "(getDatos) Error al Recoger datos de Columna: " + col;
      error = true;
      throw new SQLException(msgError);
    }
  }


  public Object getDatos(String col) throws SQLException
  {
     return getDatos(getNumColumna(col));
  }

  public String getString(String col)  throws SQLException
  {
    return getString(col, true);
  }

  public String getString(String col, boolean trim)  throws SQLException
  {
    return getString(getNumColumna(col), trim);

  }

   /**
    * @deprecated Usar getString
    * @param col String Nombre de Columna
    * @return String Valor
    * @throws SQLException
    */
   public String getDatoStr(String col)  throws SQLException
  {
    return getString(col,true);
  }
  /**
   * @deprecated Usar getString
   * @param col String Nombre de Col.
   * @param trim boolean Hacer trim()
   * @return String Valor de la columna
   * @throws SQLException
   */
  public String getDatoStr(String col,boolean trim)  throws SQLException
  {
    return getString(col,trim);

  }

  public String getString(int col)  throws SQLException
  {
    return getString(col, true);
  }

  /**
   * @deprecated Usar getString
   * @param col int N� de Columna
   * @return String Valor de la columna
   * @throws SQLException
   */
  public String getDatoStr(int col)  throws SQLException
  {
    return getString(col,true);
  }
  /**
   * @deprecated usar getString
   * @param col int N� columna
   * @param trim boolean Realizar trim?
   * @return String Valor de la columna
   * @throws SQLException
   */
  public String getDatoStr(int col,boolean trim)  throws SQLException
  {
    return getString(col,trim);
  }
  public boolean isNull(int col)   throws SQLException
  {
     return getDatos(col)==null;
  }
  public boolean isNull(String col) throws SQLException
  {
    return getDatos(getNumColumna(col))==null;
  }
  public String getString(int col,boolean trim)  throws SQLException
  {
    Object o= getDatos(col);
    if (o==null)
    {
      if (error)
        return null;
      else
        if (trim)
          return "";
        else
          return null;
    }
    String s;
    if (trim)
      s= o.toString().trim();
    else
      s=o.toString();
      s=s.replace('\'',' ');
      return s;
  }
  /**
   * @deprecated usar getDouble
   * @param col String Nombre col.
   * @throws DataFormatException Si no se puede trasnf. a numero
   * @return double Valor de col.
   * @throws SQLException
   */
  public double getDatoNumero(String col) throws SQLException
  {
    return getDouble(col);
  }
 public double getDouble(String col) throws SQLException
 {
    return getDouble(getNumColumna(col));
  }
  private int getNumColumna(String col) throws SQLException
  {
    int nc=getNomCol(col);
    if (nc==-10)
    {
      error=true;
      throw new SQLException(msgError);
    }
    return nc;
  }
  public int getInt(String col) throws SQLException {
    return (int)getDouble(col);
  }
  /**
   * @deprecated user getInt(String col)
   * @param col String
   * @throws SQLException
   * @return int
   */
  public int getDatoInt(String col) throws SQLException {
    return getInt(col);
  }

  public String getDatoFecha(String col,String frSali) throws SQLException
  {
    return getDatoFecha(getNumColumna(col),frSali);
  }

  public String getDatoFecha(int col, String frSali) throws SQLException
  {
    Object o = getDatos(col);
    if (error)
      throw new SQLException (msgError);

    if (o==null)
      return "";

    if (frSali.compareTo("")==0)
      frSali="dd-MM-yyyy";

    Fecha fec= new Fecha(o.toString(),"yyyy-MM-dd",frSali);
    try {
      return ""+fec.getFecha();
     } catch (Exception k)
     {
      throw new SQLException("Campo NO se pudo transformar a Fecha");
     }
  }
  /**
   * @deprecated usar getDouble(int x)
   * @param col int N� Col.
   * @throws DataFormatException Error al transf. a numero
   * @return double Valor de la col.
   * @throws SQLException
   */
  public double getDatoNumero(int col) throws SQLException
  {
    return getDouble(col);
  }
  public  double getDouble(int col) throws SQLException
  {
    error=false;
    Object o= getDatos(col);
    if (error)
      throw new SQLException(msgError);

    if (o==null)
      throw new SQLException("Valor del Campo es NULL");

    Double i;
    try {
      i= new Double(o.toString());
     } catch (Exception k)
     {
      error=true;
      msgError="Campo NO se pudo transformar a Numero";
      throw new SQLException(msgError);
     }
     return i.doubleValue();
  }

  public int getDatoInt(int col) throws SQLException
  {
     return (int)getDouble(col);
  }






  /**
   *
   * @param s String Nombre de la col.
   * @return int Numero de Col.
   */
  public int getNomCol(String s)
  {
    if (s == null)
    {
      msgError = "getNomCol: Nombre de Columna es NULL";
      error = true;
      return -10;
    }
    if (numcol == 0)
    {
      msgError = "getNomCol: NO Hay Datos Activos";
      error = true;
      return -10;
    }

    s = s.trim().toUpperCase();
    String tt;
    for (int n = 0; n < numcol; n++)
    {
      tt = getNomCampo(n).toUpperCase();
      if (s.trim().compareTo(tt) == 0)
        return n;
    }
    msgError = "getNomCol: Nombre de Columna (" + s + ") NO encontrada";
    return -10;
  }
  /*
  * Asigna los datos provenientes de un datosTabla.
  * @return true si todo va bien.
  *         false si hay un error.
  */
  public boolean setDatos(DatosTabla dt) throws SQLException
  {
    numcol=0;
    datos.removeAllElements();
          nomCampo.removeAllElements();
          tipCampo.removeAllElements();
          preCampo.removeAllElements();
          decCampo.removeAllElements();
          lonCampo.removeAllElements();
    int n;
    for (n=0;n<dt.getColumnCount();n++)
    {
      Object o= dt.getObject(n);
      datos.addElement(o);
//      nomCampo.addElement(dt.getNomCampo(n));
//      tipCampo.addElement(""+dt.getTipCampo(n));
//      preCampo.addElement(""+dt.getPreCampo(n));
//      decCampo.addElement(""+dt.getDecCampo(n));
//      lonCampo.addElement(""+dt.getLonCampo(n));
    }
    numcol= dt.getColumnCount();
    return true;
  }

  public void setCampos(Vector nombCampo)
  {
    numcol=0;
    datos.removeAllElements();
          nomCampo.removeAllElements();
    int n;
    for (n=0;n<nombCampo.size();n++)
    {
      nomCampo.addElement(nombCampo.elementAt(n).toString());
      datos.addElement("");
    }
    numcol=nombCampo.size();
  }
  /**
   * Devuelve el Nombre de un campo
   * @param n int N� Columna
   * @return String Nombre de campo
   */
  public String getNomCampo(int n)
  {
                if (numcol==0)
                {
                        msgError="getNomCampo: No Hay Ningun Cursor abierto";
                        error=true;
                        return null;
                }

                if (n>numcol)
    {
             msgError="Solo hay: "+numcol+" campos Disponibles";
      error=true;
      return null;
    }

          try
     {
             return (String) nomCampo.elementAt(n);
     } catch (Exception k)
     {
             msgError="NOMBRE COLUMNA: "+k.getMessage();
      error=true;
      return null;
     }
  }
  public void setDatos(String nomCol, Object dato) throws Exception
  {
          int nc=getNomCol(nomCol);
    if (nc==-10)
    {
      error=true;
      throw new Exception(msgError);
    }
    setDatos(nc,dato);
  }

  public void setDatos(int numCol,Object dato) throws Exception
  {
    error=false;
    if (numcol==0)
    {
      msgError="setDatos: NO Hay Datos Activos";
      error=true;
      throw new Exception(msgError);
    }

                if (numCol<0 || numCol>= numcol)
    {
                         msgError="setDatos: Columna NO Valida";
                        error=true;
                        throw new Exception(msgError);
    }

     try
     {
                                datos.setElementAt(dato, numCol);
     } catch (Exception k)
     {
                                msgError="setDatos: ERROR AL PONER DATO: "+dato+" EN Columna: "+numCol+"\n"+k.getMessage();
              error=true;
                          throw new Exception(msgError);
     }
     return;
  }
  public boolean getError()
  {
    return error;
  }
  public String getMsgError()
  {
    return msgError;
  }
  public Object clone() {
         vlike lk = new vlike();
         lk.msgError = msgError;
         lk.error = error;
         lk.datos = (Vector)datos.clone();
               lk.nomCampo = (Vector)nomCampo.clone();
               lk.tipCampo = (Vector)tipCampo.clone();
               lk.preCampo = (Vector)preCampo.clone();
               lk.decCampo = (Vector)decCampo.clone();
               lk.lonCampo = (Vector)lonCampo.clone();
               lk.numcol = numcol;

         return lk;
  }
  /**
   *
   * @return int No de Col. por cada linea
   */
  public int getColumnCount() { return numcol; }
  public void setColumnCount(int nCol) { numcol=nCol; }
  public boolean isNull() { return numcol==0; }
  /**
   * Establece el Datostabla de donde coger los valores
   * @param dt DatosTabla De donde leer
   * @throws SQLException Error al leer de la BD
   */
  public void setDatosTabla(DatosTabla dt) throws SQLException
  {
      numcol=0;
      datos.removeAllElements();
            nomCampo.removeAllElements();
            tipCampo.removeAllElements();
            preCampo.removeAllElements();
            decCampo.removeAllElements();
            lonCampo.removeAllElements();
      int n;
      for (n=1;n<=dt.getNumCol();n++)
      {
        Object o= dt.getObject(n);

        datos.addElement(o);
        nomCampo.addElement(dt.getNomCampo(n));
        tipCampo.addElement(""+dt.getTipCampo(n));
        preCampo.addElement(""+dt.getPreCampo(n));
        decCampo.addElement(""+dt.getDecCampo(n));
        lonCampo.addElement(""+dt.getLonCampo(n));
      }
      numcol= dt.getNumCol();

    }
  
}
