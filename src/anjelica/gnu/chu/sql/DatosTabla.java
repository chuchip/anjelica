package gnu.chu.sql;

import java.io.*;
import java.sql.*;
import java.util.*;
import gnu.chu.utilidades.*;
import gnu.chu.interfaces.*;
import java.text.ParseException;

/**
 *
 * <p>Título: DatosTabla </p>
 * <p>Descripción: Clase generica para realizar cualquier select,insert
 * en la base de dats </p>
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
 * @see conexion
 * @version  2.1 2007-09-22
 *
 */

public class DatosTabla   implements Serializable
{
  private boolean camposAddNew; // Indica si en una insert se pondran los nombres de los campos.
  int fetchSize=0;
  int timeSelect=100;   // Tiempo que se tiene para ejectuar una select con UPDATE (Por Defecto: 10 Seg.)
  Vector vecEdit=new Vector();
  String condEdit; // Condiciones para un edit en modo update.
  int maxRows=0;
  public final static int NORMAL = 0;
  public final static int EDIT = 1;
  public final static int ADDNEW = 2;
  Vector nullCampo = new Vector();
  private int modSel = NORMAL;
  Vector vecAddNew = new Vector();
  boolean escProc = false;
  boolean BOF = false; // Principio de la Tabla.
  boolean EOF = false; // Final de la Tabla.
  boolean NOREG = false; // No encontrados registros en Select;
  boolean cerrarCursor = true;
  conexion dtb_Con; // Conexion a la Database.
  String sql; // Instrucion SQL. de Select.
  String sqlUpdate; // Instrucion SQL de Insert, Delete ...
  Statement stmt1;// = new Statement[3]; // Variables Internas.
  boolean stmOpen = false; // Indica si se ha abierto el stmt1.
  ResultSet rs;// = new ResultSet[2]; // Variable Interna
  boolean rsOpen;// = new boolean; // Indica cuando las ResulSet estan iniciadas.
  boolean sqlOpen = false; // Indica si hay abierto Un cursor.
  private String MsgError = "";
  private boolean Error = false;
//  long pos = 0; // Posicion Absoluta.
//  long mpos = 0; // Maxima Posicion Alcanzada.
//  long Lmpos = 1; // No columnas a Guardar.
//  long Fmpos = 1; // Minimo No. Columnas guardadas antes de Ir para atras
//  long Rmpos = 1; // Rango No Columnas entre Lecturas.
//  Vector datos;//[] = new Vector[2]; // Vector con Los datos.
  boolean tt_Error = false; // Indica si el Hijo ha tenido algun Error.
  boolean tt_Hijo = false; // Indica si se esta lanzado el hijo.
  String tt_MsgError = "";
  boolean sw_sqlUpdate = false;
  String nomTabla; // Nombre de Tabla de la que se realiza la Select FOR UPDATE

  boolean lanzaDBCambio = true; // Dice si debe lanzar los Eventos tipo DBCambio.
  String condWhere = "";
  String schema = ".";
  String comodin = "\uFFFD";
  public SQLException SqlException; //= new SQLException("xx");

  private int maxEsperaBloq = 5;
  private boolean swReintentaBloq = true;
  private CEditable CEditable = null;
  /**
   * Constructor Final.
   * @param Conexion y Select a Ejecutar.
   */
  public DatosTabla(conexion conexion) throws SQLException
  {
    rsOpen= false;
    setConexion(conexion);
  }
  /**
   * Constructor Final.
   * @param Conexion y Select a Ejecutar.
   */
  public DatosTabla(Connection conesion) throws SQLException
  {
    rsOpen= false;
    setConexion(new conexion(conesion));
  }

  /**
  * Constructor generico. Sin Parametros (JAVABEAN)
  */
    public DatosTabla()
  {
//     datos=new Vector();
     rsOpen=false;

  }

 


    /**
   * @return true -> esta em el Ultimo Registro de la Select.
   */
     public boolean isLast()
     {
         return EOF;
     }

     /**
      * @return true -> esta en el Primer registro de la Select
      */
    public boolean isFirst()
    {
        return BOF;
    }

    public boolean isBeforeFirst() throws SQLException
    {
      return rs.isBeforeFirst();
    }

  /**
  * @return true -> La Ultima select no ha devuelto Registros.
  */
    public boolean getNOREG()
    {
        return NOREG;
    }
  /**
  * @param establece la Variable que indica si la \uFFFDltima Select
  * ha devuelto algun Registro
  * <p>
  *  Solo tiene efectos de cara a la rutina getNOREG.
  */
    public void setNOREG(boolean nr)
  {
    NOREG=nr;
  }
  /**
  * Por defecto, cada vez que se ejecuta una Select, se cierra y abre el Stament.
  * si se envia un false, no se cerraran.
  * <p>
  * Esto puede dar problemas con algunas select, por un problema del JDBC
  *
  */
  public void setCerrarCursor(boolean b)
  {
    cerrarCursor=b;
  }
  public boolean getCerrarCursor()
  {
    return cerrarCursor;
  }
  public void setEscapeProcesa(boolean escProc)
  {
    this.escProc=escProc;
  }

  public boolean getEscapeProcesa()
  {
    return escProc;
  }

  /**
  * Cancela la Select Activa
  */
 public void cancel() throws SQLException
 {
   sqlOpen = false;
   stmt1.cancel();
 }









 /**
  * * Cierra el cursor establecido con la Ultima select.
  * <p>
  * Cada vez que se llama a select esta cierra el Ultimo cursor.
  * @exception Lanza una SQLException en caso de cualquier
  * tipo de error.
  * @see getMsgError()
  * @throws SQLException en caso de error en la DB
  */
 public void close() throws SQLException
 {
   this.cerrar();
 }

 public String getSQLDate( String campo) throws SQLException //, ParseException
 {
     return getObject(campo)==null?null:"{d '"+getFecha(campo,"yyyy-MM-dd")+"'}";
  }
 /**
  * * Cierra el cursor establecido con la Ultima select.
  * <p>
  * Cada vez que se llama a select esta cierra el Ultimo cursor.
  * @exception Lanza una SQLException en caso de cualquier
  * tipo de error.
  * @see getMsgError()
  * @throws SQLException en caso de error en la DB
  */
 public void cerrar() throws SQLException
 {
   modSel = NORMAL;
   try
   {

     if (rsOpen)
       rs.close();
     if (stmt1 != null)
       stmt1.close();
   }
   catch (SQLException k)
   {
     MsgError = "Error en Cerrar" + k.getMessage();
     SqlException = k;
     throw k;
   }
 }
    /*
  * @return true -> Hay algun cursor abierto, es decir se ha ejecutado
  * una SELECT con exito.
  */
    public boolean getSqlOpen()
    {
        return sqlOpen;
    }
    void setSqlOpen(boolean b)
    {
        sqlOpen=b;
    }

    void setstmOpen(boolean b)
    {
        stmOpen=b;
    }
  /**
  *  @return  true si el Statement ha sido abierto con exito.
  * <p>
  * false si el Statement no ha sido abierto o ha habido un error
  * y se ha cerrado
  */
  public boolean getStatementOpen()
  {
    return stmOpen;
  }

    void setTtError(boolean b)
    {
        tt_Error=b;
    }

    void setTtMsgError(String b)
    {
        tt_MsgError=b;
    }

    void setTtHijo(boolean b)
    {
        tt_Hijo = b;
    }


  /**
  * @return Numero de Columnas de la Ultima sentencia Select ejecutada.
  * @deprecated  usar getColumnCount
  */
  public int getNumCol() throws SQLException
  {
    return getColumnCount();
  }
  
  public int getColumnCount() throws SQLException
  {
    return rs.getMetaData().getColumnCount();
  }
  /*
  * @return Devuelve el Ultimo Mensaje de Error.
  */
  public String getMsgError()
  {
    return MsgError;
  }
  /*
  * @return Devuelve si ha habido un Error en la ultima rutina Ejecutada.
  */
  public boolean getError()
  {
    return Error;
  }
  /*
  * @param pone la variable interna error al valor mandado.
  * <p>
  * !! Esta funci\uFFFDn Tecnicamente no se deberia utilizar. !!
  *
  */
  public void setError(boolean error)
  {
    Error=error;
  }

  /**
  * Actualizar datos de la posicion Actual en El vector Interno.
  * @param String - Nombre de Columna a actualizar
  * @param Object - Dato a establecer.
  */
  public void setDato(String columna, Object dato) throws SQLException
  {
    setDato(getNomCol(columna),(Object) dato);
  }

  public void setDato(int columna,long dato) throws SQLException
  {
    setDato(columna,""+dato);
  }
  public void setDato(int columna,int dato) throws SQLException
  {
    setDato(columna,""+dato);
  }
  public void setDato(int columna,double dato) throws SQLException
  {
    setDato(columna,""+dato);
  }

  public void setDato(int columna,String fecha,String formFecha) throws SQLException
  {
    try {
      setDato(columna, Formatear.formatearFecha(fecha, formFecha, "yyyy-MM-dd"));
    } catch (java.text.ParseException k)
    {
      throw new SQLException("Error al formatear fecha","Syntax error",-1);
    }
  }

  public void setDato(String columna,long dato) throws SQLException
  {
    setDato(columna,""+dato);
  }
  public void setDato(String columna,int dato) throws SQLException
  {
    setDato(columna,""+dato);
  }
  public void setDato(String columna,double dato) throws SQLException
  {
    setDato(columna,""+dato);
  }
  public void setDato(String columna, java.util.Date fecha) throws SQLException
  {
    if (fecha==null)
    {
        setDato(columna,(String) null);
        return;
    }
    setDato(columna, Formatear.getFecha(fecha, "dd-MM-yyyy"),
            "dd-MM-yyyy");
  }
  public void setDato(String columna,Timestamp tiempo) throws SQLException
  {
    if (tiempo==null)
    {
        setDato(columna,(String) null);
        return;
    }
    setDato(columna,(Object) tiempo);
  }
  
  public void setDato(String columna, java.sql.Date fecha) throws SQLException
  {
    if (fecha==null)
    {
        setDato(columna,(String) null);
        return;
    }
    setDato(columna, Formatear.getFecha(fecha, "dd-MM-yyyy"),
            "dd-MM-yyyy");
  }
  public void setDato(String columna,String fecha,String formFecha) throws SQLException
  {
    try {
      if (fecha==null)
      {
        setDato(columna, (String)null);
        return;
      }
      if (fecha.trim().equals(""))
      {
        setDato(columna, (String)null);
        return;
      }
      if (getConexion().getDriverType() == conexion.MSQL)
        setDato(columna, Formatear.formatearFecha(fecha, formFecha, "yyyyMMdd"));
      else
        setDato(columna, Formatear.formatearFecha(fecha, formFecha, "yyyy-MM-dd"));
    } catch (java.text.ParseException k)
    {
      throw new SQLException("Error al formatear fecha","Syntax error",-1);
    }
  }


  public void setDato(int columna, Object valor) throws SQLException
  {
    if (!sqlOpen)
    {
      MsgError = "setDatos: No Hay Ningun Cursor abierto";
      SqlException = new SQLException(MsgError);
      Error = true;
      throw SqlException;
    }
    if (NOREG && modSel != ADDNEW)
    {
      MsgError = "setDatos: No Hay Registros Activo";
      SqlException = new SQLException(MsgError);
      Error = true;
      throw SqlException;
    }

    if (valor!=null & valor instanceof String)
      valor = ( (String) valor).replaceAll("\\\\", "\\\\\\\\");

    if (modSel != ADDNEW && modSel!=EDIT)
      rs.updateObject(columna, valor);
    columna--;
//    if (modSel == ADDNEW)
//    {
//      vecAddNew.setElementAt(valor, columna);
//    else
//    {
      if (vecEdit.indexOf("" + (columna)) == -1)
        vecEdit.addElement("" + (columna));

      vecAddNew.setElementAt(valor, columna);
//    }
  }

  public void setFetchSize(int size) throws SQLException
  {
    fetchSize=size;
    stmt1.setFetchSize(size);
    if (size!=0)
      setCerrarCursor(false);
  }
  public int getFetchSize() throws SQLException
  {
    return stmt1.getFetchSize();
  }

    /**
    * Actualiza los datos del Vector Interno.
    * @param String con las condiciones del Update.
  * <p>
    * El formato debe ser similar a la funcion Update de SQL(No acepta los DATE)
    * Ej: cli_nume = 20, cli_nomb='pepe' ....
  * @return false si hay alg\uFFFDn tipo de Error.
    */
  public  boolean setAllDatos(String strUpdate) throws SQLException
  {
    String campo="";
    String valor="";
    int pos=0;
    int r;

    if ((strUpdate=sqlOracleToInformix(strUpdate,true))==null)
      return false;

    do {
        // Busca Campo.
            // Ignoro los espacios en blanco.
            for (;pos<strUpdate.length();pos++)
            {
                if (strUpdate.charAt(pos)!=' ')
                    break;
            }

        r=Formatear.buscaletra(strUpdate,'=',pos);
            if (r<0)
                return true;
            campo= strUpdate.substring(pos,r);
            pos=r+1;
            // Busca Valor a asignar.
            // Se salta los campos en blanco
            for (;pos<strUpdate.length();pos++)
            {
                if (strUpdate.charAt(pos)!=' ')
                    break;
            }

            if (strUpdate.charAt(pos)=='\'')
            {

                // Entre comillas .. Busco donde se cierran.
                pos++;
                r=Formatear.buscaletra(strUpdate,'\'',pos);
                if (r<0)
                {
                    MsgError="COMILLAS SIN TERMINAR";
                    return false;
                }
                valor=strUpdate.substring(pos,r);
                pos=r;
            }
            else
            {
                // Se supone que es un numero. Busco donde termina.
                for (r=pos;r<strUpdate.length();r++)
                {
                    if ( Character.isDigit(strUpdate.charAt(r))==false &&  strUpdate.charAt(r)!= '.' &&  strUpdate.charAt(r)!= 'E')
                        break;
                }
                valor=strUpdate.substring(pos,r);
                pos=r;
            }

           setDato(campo,valor);

            // Busco la coma.

            r=Formatear.buscaletra(strUpdate,',',pos);
            if (r<0)
                return true;
            pos=r+1;
        } while (true);

  }

  /**
  * Establece la conexion y pone la varible dtb_con igual
  * al parametro mandado.
  * <p>
  * Si la conexion es a una Base de Datos Informix establece el eschema a "",
  * en caso contrario a ".barpimo"
  * @return true si se ha establecido la conexi\uFFFDn con exito.<p>
  * false en caso de error.
  * @see conexion
  */
 public void setConexion(conexion conexion) throws SQLException
 {
   modSel = NORMAL;

   dtb_Con = conexion;
   if (dtb_Con == null)
   {
     MsgError = "1.SetConexion: CONEXION NO VALIDA";
     Error = true;
     throw new SQLException(MsgError);
   }

   stmt1 = crearEstamento();

   stmOpen = true;
   Error = false;
   sqlOpen = false;
   if (conexion.getDriverType() != conexion.ORACLE)
   {
//     setSchema("anjelica");
     this.executeUpdate("SET search_path TO anjelica,public");
     this.commit();
   }
 }

  /**
  * @return Devuelve la conexion usada por esta Class
  */
  public conexion getConexion()
  {
    return dtb_Con;
  }
  public Statement createStatement() throws SQLException
  {
    return dtb_Con.createStatement();
  }
  public Statement getStatement() throws SQLException
  {
    if (stmOpen)
      return stmt1;
    else
      return null;
  }
  /***********************************************************
  * Crear Stamento para preparar la select.
  ***********************************************************/
 Statement crearEstamento() throws SQLException
 {
   if (dtb_Con == null)
   {
     MsgError = "1.crearEstamento: CONEXION NO VALIDA";
     Error = true;
     throw new SQLException(MsgError);
   }

   Statement st;
   if (fetchSize!=0)
     st=dtb_Con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                ResultSet.CONCUR_READ_ONLY);
   else
     st = dtb_Con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                                ResultSet.CONCUR_READ_ONLY);
   if (maxRows > 0)
     st.setMaxRows(maxRows);
   // Driver Postgresql No soporta setQueryTimeOut
//   if (getConexion().getDriverType() == conexion.POSTGRES)
//     st.setQueryTimeout(10); // segundos de espera al bloqueo
   if (fetchSize!=0)
     st.setFetchSize(fetchSize);
   Error = false;
   return st;
 }
/*
  private void LeerEstructura() throws SQLException
  {
    try
    {
      numcol = rs.getMetaData().getColumnCount(); // N� columnas de la SELECT.
    }
    catch (SQLException k)
    {
      numcol = 0;
      MsgError = "LEER N\uFFFD COLUMNAS .. " + k.getMessage();
      sqlOpen = false;
      stmOpen = false;
      Error = true;
      throw new SQLException("LeerEstructura: " + k.getLocalizedMessage(),
                             k.getSQLState(), k.getErrorCode());
    }

    nomCampo.removeAllElements();
    tipCampo.removeAllElements();
    preCampo.removeAllElements();
    decCampo.removeAllElements();
    lonCampo.removeAllElements();
    nullCampo.removeAllElements();
    tipNCampo.removeAllElements();
    esWrite.removeAllElements();
    try
    {
      for (int n = 0; n < numcol; n++)
      {
        nomCampo.addElement(rs.getMetaData().getColumnName(n + 1));
        tipNCampo.addElement(rs.getMetaData().getColumnTypeName(n + 1));
        tipCampo.addElement(new Integer(rs.getMetaData().getColumnType(n + 1)));
        preCampo.addElement(new Integer(rs.getMetaData().getPrecision(n + 1)));
        decCampo.addElement(new Integer(rs.getMetaData().getScale(n + 1)));
        lonCampo.addElement(new Integer(rs.getMetaData().getColumnDisplaySize(n +
            1)));
        nullCampo.addElement(new Integer(rs.getMetaData().isNullable(n + 1)));
        esWrite.addElement(new Boolean(rs.getMetaData().isWritable(n + 1)));
      }
        } catch (SQLException k)
    {
//      dtb_Con.setConectado(false);
        sqlOpen=false;
        stmOpen=false;
        MsgError="Recoger Datos de Columna: "+k.getMessage();
      Error= true;
      throw new SQLException("LeerEstructura: "+k.getLocalizedMessage(),k.getSQLState(),k.getErrorCode());
    }
        Error=false;
    return;
  }
*/
    /******************************************************
  * Devuelve la Setencia SQL (select) a ejectar o ejecutada.
  *******************************************************/
  /*
  * @despreciado Usar en su lugar getStrSelect
  */
    public String getSql()
  {
    return getStrSelect();
  }
    /**
  * Devuelve la Setencia select a ejectar o ya ejecutada.
  */
 public String getStrSelect()
  {
    return sql;
  }

  /**
  * Establece la sentencia SELECT a ejectutar cuando se llame
  * a la funci\uFFFDn select() sin el String.
  *
  * @param String a establecer como sentencia SELECT.
  * @return true -> si la Instruccion SQL es Valida.
  *         false -> si tiene un error al tratar instruccion SQL.
  */
 public void setStrSelect(String strSelect)  throws SQLException
  {
     sql=parseaSql(strSelect);
  }
  /**
   * Utilizada para formatear los TO_DATE y alguna cosilla mas
   *
   * @param sql String Sentencia SQL a Parsear
   * @throws ParseException si la sentencia NO es valida
   * @return String Select Parseada
   */
  public String getStrSelect(String sql) throws SQLException
  {
    return parseaSql(sql);
  }

  public String parseaSql(String sql) throws SQLException
  {
    if (dtb_Con.getDriverType() == conexion.ORACLE)
      return sql;

    return sqlOracleToInformix(sql, true);

  }
  /**
  * Transforma una setencia SQL to de Oracle a Informix.
  * @return String nueva sentencia SQL.
  *         Null si hay un error al Tratarla.
  */

 private String sqlOracleToInformix(String strSelect, boolean swFec) throws
     SQLException
 {
   try {
     String s = "";
     String sqlLocal;

     String pp, valor;
     sqlLocal = strSelect.toUpperCase();
     int pos = 0;
     int r = 0;
     String formFecha;
     // Base de datos es Informix ... Quitar los TO_DATE
     do
     {
       r = sqlLocal.indexOf("TO_DATE", pos);
       if (r < 0)
       {
         s = s + strSelect.substring(pos);
         break;
       }
       s = s + strSelect.substring(pos, r);
       r = sqlLocal.indexOf('\'', r + 1); //Busco Primeras Comillas.
       if (r < 0)
       {
         MsgError = "No encontrado principio Comillas despues de TO_DATE";
         Error = true;
         return null;
       }
       pos = r + 1;
       r = sqlLocal.indexOf('\'', pos); // Busco Cerrar segundas Comillas.
       if (r < 0)
       {
         MsgError = "setStrSelect: COMILLAS SIN TERMINAR";
         Error = true;
         return null;
       }
       valor = strSelect.substring(pos, r);

       r = sqlLocal.indexOf('\'', r + 1); //Busco Segundas Comillas.
       if (r < 0)
       {
         MsgError =
             "No encontrado principio Comillas despues del Formato del TO_DATE";
         Error = true;
         return null;
       }
       pos = r + 1;
       r = sqlLocal.indexOf('\'', pos); // Busco segundas Comillas.
       if (r < 0)
       {
         MsgError = "setStrSelect: Segundas COMILLAS SIN TERMINAR";
         Error = true;
         return null;
       }
       if (valor.trim().compareTo("") != 0)
       {
         if (!swFec)
           formFecha = "yyyy-MM-dd";
         else
           formFecha = strSelect.substring(pos, r);
         if (getConexion().getDriverType() == conexion.MSQL)
           valor = Formatear.formatearFecha(valor, formFecha, "yyyyMMdd");
         else
           valor = Formatear.formatearFecha(valor, formFecha, "yyyy-MM-dd");
       }
       else
         valor = "";
       s = s + "'" + valor + "'";
       pos = r + 1;
       r = sqlLocal.indexOf(')', r + 1); //Busco Cerrar Parentesis
       if (r < 0)
       {
         MsgError = "No encontrado Cerrar Parentesis despues de TO_DATE";
         Error = true;
         return null;
       }
       pos = r + 1;
       pos = r + 1;
     }
     while (true);

     return s;
   } catch (java.text.ParseException k)
   {
     throw new java.sql.SQLException("Error al Parsear SQL\n"+k.getMessage(),"Error de Parseo",-1);
   }
    }

  /**
    * @despreciado Usar en su Lugar setStrSelect
  */
    public void setSql(String strSelect)  throws SQLException
  {
        setStrSelect(strSelect);
    }


  /********************************************************
  * Modifica TODOS los registros del Cursor a partir del Actual
  * Recibe un String con el SIGUIENTE FORMATO:
  *   aa=xx,bb=yy
  * Devuelve N\uFFFD DE Registros Modificados.
  *          -1 si hay un error.
  * NO PONER CLAUSULAS WHERE, las coge del CURSOR.
    ************************************************************/
    public   int UpdateAll(String mod) throws SQLException
  {
        if (! sqlOpen)
        {
            MsgError="UpdateAll. No Hay Ningun Cursor abierto";
            Error=true;
            return -1;
        }
        if ( NOREG )
        {
            MsgError="UpdateAll: No Hay Registros Activo";
            Error=true;
            return -1;
        }
        if (! sw_sqlUpdate)
        {
        MsgError="UpdateAll - Cursor no es FOR UPDATE";
      Error=true;
            return -1;
     }

    String s= "UPDATE "+nomTabla+" SET "+mod+ " WHERE "+condWhere;
    return executeUpdate(s);
  }
  /**
   * Borra TODOS los registros selecionados en la ultima select.
   *
   * @return int Numero de Registros borrados
   * @throws SQLException Error en DB
   * @throws ParseException ERROR en DB
   */
  public int delete() throws SQLException
  {
    return delete(stmt1,true);
  }

  public int delete(Statement st) throws SQLException
  {
    return delete(st,true);
  }
  /**
   * Borra TODOS los registros selecionados en la ultima SELECT
   * @param st Statement Statement donde ejecutar el update.
   * @param ceroisError boolean Lanzar exception en caso de que no se borre ningun registro
   * @throws SQLException Error en DB
   * @throws ParseException Error en DB
   * @return int Numero de registros borrados
   */
  public int delete(Statement st,boolean ceroisError) throws SQLException
 {

   if (!sqlOpen)
   {
     MsgError = "Delete: No Hay Ningun Cursor abierto";
     SqlException = new SQLException(MsgError);
     Error = true;
     throw SqlException;
   }
   if (NOREG)
   {
     MsgError = "Delete: No Hay Registros Activo";
     SqlException = new SQLException(MsgError);
     Error = true;
     throw SqlException;
   }
   if (!sw_sqlUpdate)
   {
     MsgError = "Delete - Cursor no es FOR UPDATE";
     SqlException = new SQLException(MsgError);
     Error = true;
     throw SqlException;
   }

   int n;
   String s;

   s = "DELETE FROM " + nomTabla + " WHERE " + getCondWhere();
   try
   {
     n = executeUpdate(s);
     if (n == 0 && ceroisError)
     {
       MsgError = "NO Borrado Ningun Registro";
       SqlException = new SQLException(MsgError);
       Error = true;
       throw SqlException;
     }
     return n;
   }
   catch (SQLException k)
   {
     Error = true;
     MsgError = "Delete: " + s + "\nError No: " + k.getErrorCode() + " " +
         k.getMessage();
     if (dtb_Con.getDriverType() == conexion.ORACLE)
     {
       stmOpen = false;
     }
     SqlException = k;
     throw SqlException;
   }
 }


  public  boolean select() throws SQLException
  {
    return select(sql,false);
  }

  public  boolean select(String strSelect) throws SQLException
  {
     setStrSelect(strSelect);
     return select(false);
  }


  public  boolean selectInto(String strSelect,vlike like)  throws SQLException
  {
    if (! select(strSelect))
      return false;
    like.setDatosTabla(this);

    return true;
  }

    public  boolean select(String strSelect,boolean forUpdate)     throws SQLException
  {
        setStrSelect(strSelect);
        return select(forUpdate);
  }
  /**
  * @despreciado Usar select en su lugar
  */
    public  boolean ejecSql(String q,boolean u) throws SQLException
  {
    return select(q,u);
  }


  public synchronized boolean select(boolean forUpdate) throws SQLException {
         return select(forUpdate, false);
  }


 public synchronized boolean select(boolean forUpdate, boolean add) throws
     SQLException
 {
   modSel = NORMAL;
   Error = false;
   if (!stmOpen)
   {
     MsgError = "Statement no esta iniciado. Ejecute Primero: setDtbCon(conexion) ";
     Error = true;
     throw new SQLException("ejecSql: " + MsgError, "", -1);
   }

   if (cerrarCursor)
   {
     cerrar();
     setConexion(dtb_Con);
     if (add)
       stmt1.setMaxRows(1);
   }

   String s = parseaSelect(forUpdate);
   sqlOpen = false;
   boolean sw = true;
   String msgEdi = ( (CEditable != null) ? CEditable.getText() : "");
   try
   {
     int pun = 0;
     lblWhile:while (sw)
     {
       try
       {
         rs = stmt1.executeQuery(s);
         rsOpen = true;
         sw = false;
       }
       catch (SQLException j)
       {
         j.printStackTrace();
         Error = true;
         MsgError = "Select: " + s + "\nError: " + j.getMessage();
         if (dtb_Con.getDriverType() == conexion.ORACLE)
           stmOpen = false;

         SqlException = new SQLException(j.getMessage() + "\n SQL: " + s,
                                         j.getSQLState(), j.getErrorCode());

         throw new SQLException(MsgError, j.getSQLState(), j.getErrorCode());

       }
     }
   }
   catch (SQLException k)
   {
     if (CEditable != null)
       CEditable.setText(msgEdi);
     throw k;
   }
   catch (Exception k)
   {
     k.printStackTrace();
     if (CEditable != null)
       CEditable.setText(msgEdi);
     Error = true;
     MsgError = "Select: " + s + "\n Select: " + k.getMessage();
     stmOpen = false;
     throw new SQLException(MsgError);
   }
   if (CEditable != null)
     CEditable.setText(msgEdi);

   EOF = false;
   BOF = false;
   NOREG = false;


   sqlOpen = true;
   if (!add)
   {
     boolean rsNext=next();

     Error = false;
     if (! rsNext)
     {
       MsgError = "NO ENCONTRADO NINGUN REGISTRO";
       NOREG = true;
       return false; // No recogido Ningun Dato. SELECT fallo.
     }
   }

   EOF = false;
   BOF = false;

   return true;
  }

private String parseaSelect(boolean forUpdate) throws SQLException
{
  sql = sql.trim(); // Quita Espacios a la Sentencia SELECT

  sql = Formatear.reemplazar(sql, comodin, schema);
  StringTokenizer sto = new StringTokenizer(sql);
  String pp = sto.nextToken().toUpperCase();
  // Buscar Palabra Clave Select
  if (pp.compareTo("SELECT") != 0)
  {
    MsgError = "Instrucion NO es una SELECT valida";
    Error = true;
    throw new SQLException("ejecSql: " + MsgError, "", -1);
  }

  String s = sql;
  sw_sqlUpdate = forUpdate;

  if (forUpdate)
  {
    if (stmt1.getResultSetConcurrency()==ResultSet.CONCUR_UPDATABLE)
      s = "SELECT "+sql.substring(7) + " FOR UPDATE";
  }
  // Buscamos la palabra clave FROM, lo que haya despues
  // se asume es el nombre de la tabla.
  boolean sw_enc = false;
  while (sto.hasMoreTokens())
  {
    if (sto.nextToken().toUpperCase().compareTo("FROM") == 0)
    {
      sw_enc = true;
      break;
    }
  }
  if (!sw_enc)
  {
    MsgError = "No encontrado Nombre de Tabla en sentencia SELECT";
    Error = true;
    throw new SQLException("ejecSql: " + MsgError, "", -1);
  }

  nomTabla = sto.nextToken().toUpperCase();
  int pw = 0;
  int pun;
  condWhere = "";

  // Buscamos Condiciones WHERE.
  if ( (pw = sql.toUpperCase().indexOf("WHERE")) != -1)
  {
    if (( pun=sql.toUpperCase().indexOf("ORDER BY")) ==-1)
      condWhere = sql.substring(pw + 5);
    else
      condWhere=sql.substring(pw+5,pun);
  }
  return s;
}

    /*
    * Establece si se deben (true) 	o no deben (false) lanzar eventos
    * tipo DBCambio, cuando se produce un cambio en SQL.
    */
    public void setLanzaDBCambio(boolean b)
    {
        lanzaDBCambio=b;
        return;
    }

    /*
    * Devuelve si se deben (true) 	o no deben (false) lanzar eventos
    * tipo DBCambio, cuando se produce un cambio en SQL.
    */
    public boolean getLanzaDBCambio(boolean b)
    {
        return lanzaDBCambio;
    }

    /*****************************************************************
  * Posiciona el Cursor en el Primer Registro.
  ******************************************************************/
 public boolean first() throws SQLException
 {
   Error = false;
   modSel = NORMAL;
   if (!sqlOpen)
   {
     MsgError = "Primero: No Hay Ningun Cursor abierto";
     Error = true;
     return false;
   }

   if (NOREG)
   {
     MsgError = "Primero: No Hay Registros Activo";
     Error = true;
     return false;
   }
   if (sw_sqlUpdate == true)
   {
     MsgError = "SELECT es Update No se puede ir al Primero";
     Error = true;
     return false;
   }

   EOF = false;
   if (BOF)
   {
     MsgError = "Primero: YA esta en el Primer registro";
//			Error=true;
     return true;
   }

   BOF = true;

   try
   {
     rs.first();
   }
   catch (SQLException j)
   {
//      dtb_Con.setConectado(false);
     Error = true;
     MsgError = "Primero: Ejec.Query Error: " + j.getMessage();
     sqlOpen = false;
     stmOpen = false;
     throw new SQLException(MsgError);
   }
   // Instrucion SQL ejecturada correctamente.

   // Inicializar Variables de Posicion.

//   datos.removeAllElements(); // Quitar datos del Vector.
//   ponDatos();


   MsgError = "";
   Error = false;
   return true;
  }

  /*****************************************************************
   * Posiciona el Cursor en el Ultimo Registro.
   *****************************************************************/
  public boolean last() throws SQLException
  {
    modSel = NORMAL;
    BOF = false;
    if (!sqlOpen)
    {
      MsgError = "Ultimo: No Hay Ningun Cursor abierto";
      Error = true;
      return false;
    }
    if (NOREG)
    {
      MsgError = "Ultimo: No Hay Registros Activo";
      Error = true;
      return false;
    }
    if (EOF)
    {
      MsgError = "Ultimo: Ya esta en el Ultimo Registro";
      Error = true;
      return false;
    }
    EOF = true;
    rs.last();
//    ponDatos();
    EOF = true;
    return true;


  }

  /**
   *
   * Manda una posicion atras el Cursor
   * @return false en caso de que este en el primer registro
   * @throws SQLException en ERROR
   */
  public boolean previous() throws SQLException
  {
    return previous(1);
  }
  /**
   * Va para atras <b>b</b> posiciones
   * @param p  Numero de Posiciones a ir para atras
   * @return si ha podido ir para atras las posiciones solicitadas
   * @throws SQLException En caso de error
   */
  public boolean previous(int p) throws SQLException
  {
    modSel = NORMAL;
    if (!sqlOpen)
    {
      MsgError = "Atras: No Hay Ningun Cursor abierto";
      Error = true;
      return false;
    }
    if (NOREG)
    {
      MsgError = "Atras: No Hay Registros Activos";
      Error = true;
      return false;
    }


    if (BOF && modSel==NORMAL)
    {
      MsgError = "Estas en la PRIMERA POSICION";
      Error = true;
      return false;
    }
    EOF = false;
    if (rs.isFirst())
    {
      BOF=true;
      MsgError = "Estas en la PRIMERA POSICION";
      Error = true;
      return false;
    }
    boolean rt=true;
    if (p == 1)
      rt=rs.previous();
    else
      rt = rs.relative(p * -1);
    if (!rt)
    {
      rs.first();
      BOF = true;
    }
    return rt;
  }

  /**
   * Devuelve el nombre de un campo, mandando el numero de columna
   * La primera columna es 1, la segunda 2 ... 
   * @param n
   * @return
   * @throws SQLException 
   * @deprecated usar getColumnName
   */
   public String getNomCampo(int n) throws SQLException
   {
     return rs.getMetaData().getColumnName(n);
   }
   /**
   * Devuelve el nombre de un campo, mandando el numero de columna
   * La primera columna es 1, la segunda 2 ... 
   * @param numCol
   * @return Nombre del campo
   * @throws SQLException 
   */
   public String getColumnName(int numCol) throws SQLException
   {
     return rs.getMetaData().getColumnName(numCol);  
   }
   /**
    * Especifica cuantos registros se deben leer como maximo en una
    * select. 0 significa sin limite
    * @see Statement.setMaxRows(int);
    * @param nr int Numero de registros maximos
    */
   public void setMaxRows(int nr)
  {
    maxRows=nr;
  }
  /**
   * Retorna el Numero maximo de registros que se deben leen en una select.
   * 0 significa sin limite
   * @return int Numero Max. de Registros
   */
  public int getMaxRows()
  {
    return maxRows;
  }
  /**
   * Devuelve el valor del campo como un string.
   * Si el campo es null devuelve una cadena vacia ("")
   * @param col Nombre de campo
   * @return Valor del campo
   * @throws SQLException
   */
  public String getString(String col) throws SQLException
  {
    return getString(col,true);
  }
/**
 * Devuelve como string el valor del campo
 * @param col Nombre del campo
 * @param trim Realiza un trim. Si es false y el valor devuelto es null, devuelve null, si es true
 * y el valor es null devuelve una cadena vacia ("")
 * @return Valor del campo, como string
 * @throws SQLException
 */
  public String getString(String campo,boolean trim) throws SQLException
  {
    return getString(findColumn(campo,true),trim);
  }

  public String getString(int col) throws SQLException
  {
    return getString(col,true);
  }
  /**
   * Devuelve el valor del campo como string
   *
   * @param col int  Numero de campo
   * @param trim boolean true si hay que hacerle un trim. Si el campo es nulo
   * y trim es true se devolvera un string vacio "", en caso contrario se devolvera
   * null.
   * @return String Valor del campo
   */
  public String getString(int col,boolean trim) throws SQLException
  {
    String s;
    Object o= getObject(col);
    if (o==null)
    {
      if (Error)
        return null;
      else
        if (trim)
          return "";
        else
          return null;
    }
    if (trim)
      s=o.toString().trim();
    else
      s=o.toString();
      s=s.replace('\'',' ');
    return s;
  }

  public long getDatoLong(int col) throws SQLException
  {
     return (long)getDatoNumero(col,false);
  }
  public long getDatoLong(String col) throws SQLException {
    return (long)getDouble(col,false);
  }
  public long getDatoLong(String col,boolean nullisCero) throws SQLException
  {
    return (long)getDouble(col,nullisCero);
  }
  public long getDatoLong(int col,boolean nullisCero) throws SQLException
  {
    return (long)getDatoNumero(col,nullisCero);
  }
  /**
   * @deprecated usar getInt
   * @param col
   * @return
   * @throws java.sql.SQLException
   */
  public int getDatoInt(int col) throws SQLException
  {
     return (int)getDatoNumero(col,false);
  }

  public int getInt(String col) throws SQLException {
    return (int)getDouble(col,false);
  }
  
  public short getShort(String col) throws SQLException
  {
    return getShort(findColumn(col));  
  }
  public short getShort(String col, boolean nullIsCero) throws SQLException
  {
    return getShort(findColumn(col),nullIsCero);  
  }
  /**
   * Devuelve el valor tipo short de la columna mandada. 
   * Si es NULL o la columna no se puede transformar a short lanzara una excepcion
   * @param col Numero de Columna (1 es la primera)
   * @return Valor de la columna
   * @throws SQLException 
   */
  public short getShort(int col) throws SQLException
  {
      return getShort(col,false);
  }
  /**
   * Devuelve el valor tipo short de la columna mandada. 
   * Si la columna no se puede transformar a short lanzara una excepcion
   * @param col Numero de Columna (1 es la primera)
   * @param nullIsCero si es true, en caso de que el valor sea NULL, devolvera 0
   * @return Valor de la columna
   * @throws SQLException 
   */
  public short getShort(int col,boolean nullIsCero) throws SQLException
  {
    Error=false;
    Object o= getObject(col);

    if (o==null)
    {
      if (! nullIsCero)
        throw new SQLException("Valor del Campo "+col+"("+ getNomCampo(col)+") es NULL");
      else
        return 0;
    }
    short i;
    try {
        if (modSel == ADDNEW || modSel==EDIT)
            i=Short.valueOf(vecAddNew.elementAt(col-1).toString());
        else
            i = rs.getShort(col);
     } catch (Exception k)
     {
      Error=true;
      MsgError="Campo NO se pudo transformar a Numero";
      throw new SQLException(MsgError);
     }
     return i;
  }
  
  public long getLong(String col) throws SQLException
  {
    return getLong(findColumn(col));  
  }
  public long getLong(String col, boolean nullIsCero) throws SQLException
  {
    return getLong(findColumn(col),nullIsCero);  
  }
  /**
   * Devuelve el valor tipo Long de la columna mandada. 
   * Si es NULL o la columna no se puede transformar a short lanzara una excepcion
   * @param col Numero de Columna (1 es la primera)
   * @return Valor de la columna
   * @throws SQLException 
   */
  public long getLong(int col) throws SQLException
  {
      return getLong(col,false);
  }
  /**
   * Devuelve el valor tipo long de la columna mandada. 
   * Si la columna no se puede transformar a short lanzara una excepcion
   * @param col Numero de Columna (1 es la primera)
   * @param nullIsCero si es true, en caso de que el valor sea NULL, devolvera 0
   * @return Valor de la columna
   * @throws SQLException 
   */
  public Long getLong(int col,boolean nullIsCero) throws SQLException
  {
    Error=false;
    Object o= getObject(col);

    if (o==null)
    {
      if (! nullIsCero)
        throw new SQLException("Valor del Campo "+col+"("+ getNomCampo(col)+") es NULL");
      else
        return (long)0;
    }
    Long i;
    try {
        if (modSel == ADDNEW || modSel==EDIT)
            i=Long.valueOf(vecAddNew.elementAt(col-1).toString());
        else
            i = rs.getLong(col);
     } catch (Exception k)
     {
      Error=true;
      MsgError="Campo NO se pudo transformar a Numero";
      throw new SQLException(MsgError);
     }
     return i;
  }
  public int getInt(String col,boolean nullisCero) throws SQLException
  {
    return (int)getDouble(col,nullisCero);
  }
  public int getInt(int col) throws SQLException
  {
    return rs.getInt(col);
  }
  public int getInt(int col,boolean nullisCero) throws SQLException
  {
    return (int)getDatoNumero(col,nullisCero);
  }

  public double getDouble(String col) throws SQLException
  {
    return getDouble(col,false);
  }
  public double getDouble(String col,boolean nullisCero) throws SQLException
  {
    return getDatoNumero(getNomCol(col),nullisCero);
  }
  public double getDouble(int col,boolean nullisCero) throws SQLException
  {
    return getDatoNumero(col,nullisCero);
  }


  public String getFecha(int col) throws SQLException
  {
    return getFecha(col,"dd-MM-yyyy");
  }
  /**
   * Devuelve el valor de ese campo como un string
   * @param col columna (campo)
   * @return valor del campo como un string, siendo ese campo de tipo fecha.
   * El valor devuelto es en formato "dd-MM-yyyy"
   * @throws SQLException
   */
  public String getFecha(String col) throws SQLException
  {
    return getFecha(col,"dd-MM-yyyy");
  }
  public String getFecha(String col,String frSali) throws SQLException
  {
    return getFecha(getNomCol(col),frSali);
  }
  public Timestamp getTimeStamp(String col) throws SQLException
  {
      return getTimeStamp(findColumn(col));
  }
  /**
   * Devuelve el valor de la columna de tipo TimeStamp
   * @param col COlumna
   * @return Si no es convertible a timestamp. Devuelve NULL.
   * @throws SQLException 
   */
  public Timestamp getTimeStamp(int col) throws SQLException
  {
    if (!sqlOpen)
    {
      MsgError = "getDate: No Hay Ningun Cursor abierto";
      Error = true;
      throw new SQLException(MsgError);
    }

    if (NOREG && modSel != ADDNEW)
    {
      MsgError = "getDate: No Hay Registros Activos";
      Error = true;
      throw new SQLException(MsgError);
    }
    
    if (modSel == ADDNEW || modSel==EDIT)
       if ((vecAddNew.elementAt(col-1)) instanceof Timestamp)
         return (Timestamp) vecAddNew.elementAt(col-1);
       else
       {
         try {
            return new Timestamp(Long.parseLong(vecAddNew.elementAt(col-1).toString()));
         } catch (Exception k)
         {
             return null; // No es un timestamp.
         }
       }
    else
        return rs.getTimestamp(col); 
    
  }
  
  public java.sql.Date getDate(String col) throws SQLException
  {
    if (!sqlOpen)
    {
      MsgError = "getDate: No Hay Ningun Cursor abierto";
      Error = true;
      throw new SQLException(MsgError);
    }

    if (NOREG && modSel != ADDNEW)
    {
      MsgError = "getDate: No Hay Registros Activos";
      Error = true;
      throw new SQLException(MsgError);
    }
    return rs.getDate(col);
  }
  
  public java.sql.Date getDate(int col) throws SQLException
  {
    Error = false;
    if (!sqlOpen)
    {
      MsgError = "getDate: No Hay Ningun Cursor abierto";
      Error = true;
      throw new SQLException(MsgError);
    }

    if (NOREG && modSel != ADDNEW)
    {
      MsgError = "getDate: No Hay Registros Activos";
      Error = true;
      throw new SQLException(MsgError);
    }
    if (col < 0)
    {
      MsgError = "getDatos: Columna ("+col+") NO Valida";
      Error = true;
      throw new SQLException(MsgError);
    }

    if ( (sw_sqlUpdate ) && modSel != ADDNEW)
      col++;
    return rs.getDate(col);
  }
  public String getFecha(int col, String frSali) throws SQLException
  {
    try
    {
      String f;
      Object o = getObject(col);

      if (o == null)
        return "";

      if (frSali.compareTo("") == 0)
        frSali = "dd-MM-yyyy";
      f = o.toString().trim();
      if (f.length()==0)
          return "";
      if (! Character.isDigit(f.charAt(0))) // PRE-formateada o funcion especial
        return f;
      if (f.indexOf("-") == -1)
        return Formatear.formatearFecha(f, "yyyyMMdd", frSali);
      else
        return Formatear.formatearFecha(f,
                                        (f.indexOf(":") == -1 ? "yyyy-MM-dd" : "yyyy-MM-dd hh:mm:ss"),
                                        frSali);
    } catch (java.text.ParseException k)
    {
      throw new SQLException("Error al formatear fecha","Syntax error",-1);
    }
  }

  public  double getDatoNumero(int col) throws SQLException
  {
    return getDatoNumero(col,false);
  }
  public  double getDatoNumero(int col,boolean nullisCero) throws SQLException
  {
    Error=false;
    Object o= getObject(col);

    if (o==null)
    {
      if (! nullisCero)
        throw new SQLException("Valor del Campo "+col+"("+ getNomCampo(col)+") es NULL");
      else
        return 0;
    }
    Double i;
    try {
      i= new Double(o.toString());
     } catch (Exception k)
     {
      Error=true;
      MsgError="Campo NO se pudo transformar a Numero";
      throw new SQLException(MsgError);
     }
     return i.doubleValue();
  }

  private String getDatoNumer1(int col) throws SQLException
  {
    Error=false;
    Object o= getObject(col);
    if (Error)
      throw new SQLException(MsgError);

    if (o==null)
     return "0";

    if (o.toString().equals(""))
      return "0";

    Double i;
    try {
      i= new Double(o.toString());
     } catch (Exception k)
     {
      Error=true;
      try {
        MsgError="Campo: "+col+" ("+ getColumnName(col) +") NO se pudo transformar a Numero '" + o.toString() + "'";
      } catch (SQLException k1)
      {
        MsgError="Campo: "+col+" (COLUMNA DESCONOCIDA) NO se pudo transformar a Numero '" + o.toString() + "'";
 
      }
      throw new SQLException(MsgError);
     }

     return ""+i.doubleValue();
  }




  private String getDatMemo(InputStream inStr,int ncar) throws SQLException
  {
    if (inStr==null)
      return null;

    byte bit[] = new byte[ncar];
    int lg;
    try {
      lg=inStr.read(bit);
    } catch (Exception k)
    {
      MsgError="Error al Leer memo: "+k.getMessage();
      Error=true;
      SqlException=(SQLException) k;
//      throw new SQLException(MsgError);
      throw  SqlException;
    }
    if (lg==-1)
      return "";
    else {
      String s = "";
      String s1 = new String(bit,0,lg);
      for (int i = 0; i < s1.length(); i++) {
          if (((byte)s1.charAt(i)) != 0)
             s += s1.charAt(i);
      }
      return s;
    }
  }
  public Object getObject(int col) throws SQLException
  {
    if (modSel == ADDNEW || modSel==EDIT)
      return vecAddNew.elementAt(col-1);
    else
      return rs.getObject(col); 
  }
  /**
   * @deprecated Usar getObject(int col)
   * @param col int Nº de Columna (1 es la primera)
   * @throws SQLException Error al recoger datos
   * @return Object Leido (puedo ser NULL)
   */

  public Object getDatos(int col) throws SQLException
  {
    return getObject(col);
  }

  /**
   * @deprecated Usar getObject(int col)
   * @param col String Nombre de Columna
   * @throws SQLException Error al recoger datos
   * @return Object Leido (puedo ser NULL)
   */
  public  Object getDatos(String col) throws SQLException
   {
      return getObject(getNomCol(col));
   }

  public  Object getObject(String col) throws SQLException
  {
     return getObject(getNomCol(col));
  }
  /*
   * Recoge todos los Datos de La Linea Activa
   */
  public Vector getDatosRow() throws SQLException
  {
    Vector v = new Vector();

    if (!sqlOpen)
    {
      MsgError = "getDatosRow: No Hay Ningun Cursor abierto";
      Error = true;
      return null;
    }

    if (NOREG)
    {
      MsgError = "getDatosRow: No Hay Registros Activo";
      Error = true;
      return null;
    }
    int n;
    for (n = 0; n < getNumCol(); n++)
    {
      v.addElement(rs.getObject(n));
//          v.addElement((Object)((Vector) datos.elementAt((int)pos)).elementAt(n));
    }
    return v;
  }
   public int findColumn(String campo,boolean throwException) throws SQLException
   {
       int numCol= findColumn(campo);
       if (numCol==0)
           throw new SQLException("Campo "+campo+ " no encontrado en query activa\n"+sql);
       return numCol;
   }
  /**
   * Devuelve el numero de columna,
   * @param campo
   * @return numero campo usado (1, es el primer campo, 2 el segundo...)
   * Si no encuentra el campo, devuelve 0.
   * @throws SQLException  Error al buscar el numero de campo. NO lanza exception,
   * si no lo encuentra.
   */
  public int findColumn(String campo) throws SQLException
  {
      int nCol;
      if (campo == null)
      {
        MsgError = "getNomCol: Nombre de Columna es NULL";
        SqlException = new SQLException(MsgError);
        Error = true;
        throw SqlException;
      }
      if (!sqlOpen)
      {
        MsgError = "getNomCol: No Hay Ningun Cursor abierto";
        SqlException = new SQLException(MsgError);
        Error = true;
        throw SqlException;
      }

      campo = campo.trim();
      try {
        nCol = rs.findColumn(campo);
      } catch (SQLException k)
      {
        if (! k.getMessage().contains("not found"))
        {
        Error = true;
        MsgError = "getNomCol: Error al buscar nombre de Columna (" + campo+ ") NO encontrada";
        SqlException = k;
        throw SqlException;
        }
        else
            nCol=0;
      }
   
      return nCol;   
  }
    /**
     * Devuelve el Numero de una columna a traves de su nombre
     * 
     * @deprecated usar findColumn
     */
    public int getNomCol(String s) throws SQLException
    {
      return findColumn(s,true);
    }


  public String getSqlUpdate()  throws SQLException
  {
        return sqlUpdate;
  }
  public boolean setSqlUpdate(String s)  throws SQLException
  {
    if (dtb_Con.getDriverType()==conexion.ORACLE)
    {
      sqlUpdate=s;
      return true;
    }

    if ((sqlUpdate=sqlOracleToInformix(s,true))==null)
      return false;
    return true;
  }


  public void commit() throws SQLException
  {
    modSel = NORMAL;
    dtb_Con.commit();
    Error = false;
  }

  public void rollback() throws SQLException
  {
    modSel = NORMAL;
    dtb_Con.rollback();
    Error = false;
  }


  public int executeUpdate(String s) throws  SQLException
  {
    return executeUpdate(s,stmt1);
  }
    public int executeUpdate(String s,Statement st) throws  SQLException
  {
    if (! setSqlUpdate(s))
      throw new SQLException(MsgError+"\n"+s);
    return executeUpdate(st);
  }
  /**
   * @deprecated usar executeUpdate()
   * @param st Statement Statement sobre el que ejecutar el update
   * @throws SQLException si hay algun error
   * @return int Numero  de Lineas Afectadas
   */
  public int ejecUpdate(Statement st) throws SQLException
  {
    return executeUpdate(st);
  }
  /**
   * Ejecuta sentencias tipo INSERT / Update / Delete etc.
   * @param st Statement Statement sobre el que ejecutar la sentencia
   * @throws SQLException Error de la base de datos
   * @return int Numero de Columnas Afectadas
   */
  public int executeUpdate(Statement st) throws SQLException
  {

    String s = sqlUpdate;
    s = Formatear.reemplazar(s, comodin, schema);

    int n = 0;
    double seg = 0;
    boolean sw = true;
    String msgEdi = ( (CEditable != null) ? CEditable.getText() : "");
    lblWhile:while (sw)
    {
      try
      {
        n = st.executeUpdate(s);
        sw = false;
      }
      catch (SQLException j)
      {
        Error = true;
        MsgError = "ejecUpdate: " + s + "\nError: " + j.getMessage();
        if (dtb_Con.getDriverType() == conexion.ORACLE)
          stmOpen = false;
        SqlException = j;
        if (CEditable != null)
          CEditable.setText(msgEdi);
        throw new SQLException(MsgError, j.getSQLState(), j.getErrorCode());

      }
    }
    if (CEditable != null)
      CEditable.setText(msgEdi);
    Error = false;
    return n;
  }


  public boolean next() throws SQLException
  {
    return next(1);
  }



 public boolean next(int i) throws SQLException
 {
   modSel = NORMAL;

   if (!sqlOpen)
   {
     MsgError = "next: No Hay Ningun Cursor abierto";
     Error = true;
     throw new SQLException(MsgError);
   }
   if (NOREG)
   {
     MsgError = "next: No Hay Registros Selecionados";
     Error = true;
     return false;
   }
   try
   {
     if (EOF && modSel == NORMAL)
     {
       MsgError = "next: Estas en el Ultimo Registro";
       return false;
     }
     if (rs.isLast() && modSel == NORMAL)
     {
       EOF = true;
       MsgError = "next: Estas en el Ultimo Registro";
       return false;
     }
   }  catch (SQLException k)
   {
      // Pos mira que no soporta el ISLAST.
   }

   Error = false;
   BOF = false;
   EOF = false;
   boolean swRes;
   if (i>1)
     swRes=rs.relative(i);
   else
     swRes=rs.next();
   if (! swRes)
   {
     MsgError = "next: Estas en el Ultimo Registro";
     EOF = true;
     if (!isForUpdate() && modSel != ADDNEW && (rs.getType()==ResultSet.TYPE_SCROLL_INSENSITIVE ||
                                                rs.getType()==ResultSet.TYPE_SCROLL_SENSITIVE ))
       rs.previous();
     Error = true;
     return false;
   }
   try
   {
     if (rs.getStatement().getConnection().getAutoCommit())
     {
       if (rs.isLast())
         EOF = true;
     }
   } catch (SQLException k)
   {
      // Pos mira que no soporta el ISLAST.
   }
   return true;
 }

    public  int getTipCampo(int n) throws SQLException
    {
      return rs.getMetaData().getColumnType(n);
    }

    public ResultSetMetaData getMetaData() throws SQLException
    {
      return rs.getMetaData();
    }



  public  boolean isWritable(int n) throws SQLException
  {
    return rs.getMetaData().isWritable(n);
}
  public  boolean isAutoIncrement(int n) throws SQLException
   {
     return rs.getMetaData().isAutoIncrement(n);
 }

 public boolean isAutoIncrement(String campo) throws SQLException
 {
   return rs.getMetaData().isAutoIncrement(getNomCol(campo));
 }


  public  int getTipCampo(String campo) throws SQLException
  {
    return getTipCampo(getNomCol(campo));
  }
  /***********************************************************************
    * Devuelve la Precision del campo.
    ***************************************************************************/
    public int getPreCampo(int n) throws SQLException
  {
    return rs.getMetaData().getPrecision(n);

  }

  public int getPreCampo(String campo) throws SQLException
  {
    return getPreCampo(getNomCol(campo));
  }
  public int getDecCampo(int n) throws SQLException
  {
    return rs.getMetaData().getScale(n);
  }
  public int getNullCampo(String c) throws SQLException
  {
    return getNullCampo(getNomCol(c));
  }

  public int getNullCampo(int n) throws SQLException
  {
    return rs.getMetaData().isNullable(n);
  }
  public String getTipNomCampo(String campo) throws SQLException
  {
    return getTipNomCampo(getNomCol(campo));
  }

    /**********************************************************************
    * Devuelve los Decimales del Campo.
    ***************************************************************************/
    public  String getTipNomCampo(int n) throws SQLException
  {
    return rs.getMetaData().getColumnTypeName(n);
  }
  public int getDecCampo(String campo) throws SQLException
  {
        return getDecCampo(getNomCol(campo));
  }
  public  int getLonCampo(int n) throws SQLException
  {
    return rs.getMetaData().getColumnDisplaySize(n);
  }
  public int getLonCampo(String campo) throws SQLException
  {
        return getLonCampo(getNomCol(campo));
  }

  /**
   * Devuelve el nombre de la tabla sobre la q se esta realizando select/UPDATE/ADD_NEW
   * @return 
   */
  public String getNomTabla()
  {
    if (nomTabla==null)
      return "";
    else
      return nomTabla;
  }
  /**
  * @return devuelve el string establecido como Schema
  */
  public String getSchema()
  {
    return schema;
  }

  /**
  * Establece el String utilizado como Schema
  * <p>
  * por defecto es ".barpimo"
  */
  public void setSchema(String esquema)
  {
    schema=esquema;
  }


  /**
  * Establece el Comodin que se sustituira en las sentencias SQL
  * por lo establecido en setSchema.
  * <p>
  * Por defecto es '\uFFFD' Caracter 158
  *
  */
  public void setComodin(String comodin)
  {
    this.comodin=comodin;
  }

  /**
  * Devuelve el String que se utilizara como comodin para sustituirlo
  * por la cadena establecido en setSchema()
  */
  public String getComodin()
  {
    return comodin;
  }
  /**
   * Copia los datos de un DatosTabla a otro.
   * Este datos tabla ha tenido que ser puesto en modo addnew o edit.
   *
   * @param dt DatosTabla
   * @throws SQLException
   */
  public void setDato(DatosTabla dt) throws SQLException
  {
    String f = "";
    int n;
    int tc;
    for (n = 1; n <= getNumCol(); n++)
    {
      if (!isWritable(n))
        continue;
      tc = getTipCampo(n);
      switch (tc)
      {
        case (Types.DATE):
        case (Types.TIME):
        case (Types.TIMESTAMP):
          setDato(n,dt.getDate(n));
          break;
        case (Types.DECIMAL):
        case (Types.DOUBLE):
        case (Types.FLOAT):
        case (Types.INTEGER):
          if (getObject(n) == null)
            setDato(n,null);
          else
            setDato(n,dt.getDouble(n,true));
          break;
        case (Types.NUMERIC):
        case (Types.SMALLINT):
        case (Types.REAL):
          if (getObject(n) == null)
            setDato(n, null);
          else
            setDato(n, dt.getInt(n));
          break;
        case Types.OTHER: // Puede ser bit
          if (getString(n).equals("False"))
            setDato(n,0);
          else
            setDato(n,1);
          break;
        default:
            setDato(n,dt.getString(n,false));
          break;
      }
    }

  }
  /**
  * Devuelve el String que se puede Mandar a Una Insert para insertar todos los Datos
  * de esa tabla.
  * @Throw Exception en Caso de Error al leer Datos.
  */
 public String getStrInsert() throws SQLException
 {
   String s = "";
   String f = "";
   int n;
   int tc;
   for (n = 1; n <= getNumCol(); n++)
   {
     if (camposAddNew && vecEdit.indexOf(""+(n-1))==-1)
       continue; // Campo NO modificado
     if (!isWritable(n))
       continue;
     tc = getTipCampo(n);
     switch (tc)
     {
       case (Types.DATE):
       case (Types.TIME):
         if (getObject(n)==null)
           s+="null";
         else
         {
           f = getFecha(n, "dd-MM-yyyy");
           if (f.compareTo("") == 0)
             s+= null;
           else
           {
             if ( ! Character.isDigit(f.trim().charAt(0)) )
               s = s + f; // Pre-FORMATEADA o funcion
             else
               s = s + "TO_DATE('" + f + "','dd-MM-yyyy')";
           }
         }
         break;
       case (Types.TIMESTAMP):
          if (getObject(n)==null)
           s+="null";
         else
          {
            if (getTimeStamp(n)==null)
              s = s + getFecha(n); // Pre-FORMATEADA o funcion
            else
                s = s + "{ts '"+Formatear.getFecha(getTimeStamp(n), "yyyy-MM-dd HH:mm:ss")+"'}";
          }
         break;
       case (Types.DECIMAL):
       case (Types.DOUBLE):
       case (Types.FLOAT):
       case (Types.INTEGER):
       case (Types.NUMERIC):
       case (Types.REAL):
       case (Types.SMALLINT):
         if (getObject(n) == null)
           s += "null";
         else
           s = s + getDatoNumer1(n);
         break;
       case Types.OTHER: // Puede ser bit
         if (getString(n).equals("False"))
           s += "'0'";
         else
           s += "'1'";
         break;
       default:
         if (getObject(n) == null)
           s += "null";
         else
           s = s + "'" + getString(n) + "'";
         break;
     }
//     if (n <= getNumCol() - 1)
      s = s + ", ";
   }
   
    // Quitar la  coma final
   if (s.indexOf(",",s.length()-2)>0)
           s=s.substring(0,s.length()-2);
       
   
    return s;
  }

  /**
  * Devuelve el String que se puede Mandar a Una Update
  *  para Modificar todos los Datos
  * de esa tabla.
  * @Throw Exception en Caso de Error al leer Datos.
  */
 public String getStrUpdate() throws SQLException
 {
   String s = "";
   String f = "";
   int n;
   int tc;
   for (n = 1; n <= getNumCol(); n++)
   {
     if (!isWritable(n))
       continue;
     if (vecEdit.indexOf(""+(n-1))==-1)
       continue; // Campo NO modificado
     s += getColumnName(n) + " = ";
     tc = getTipCampo(n);

     switch (tc)
     {
       case (Types.DATE):
       case (Types.TIME):
         if (getObject(n)==null)
           s+="null";
         else
         {
           f = getFecha(n, "dd-MM-yyyy");
           if (f.compareTo("") == 0)
             s = s + "''";
           else
               if ( ! Character.isDigit(f.charAt(0)))
                 s = s + f; // Pre-FORMATEADA o funcion especial de la DB
               else
                 s = s + "TO_DATE('" + f + "','dd-MM-yyyy')";
         }
         break;
       case (Types.TIMESTAMP):
         if (getObject(n)==null)
           s+="null";
         else
         {
            if (getTimeStamp(n)==null)
              s = s + getFecha(n); // Pre-FORMATEADA o funcion
            else
                s = s + "{ts '"+Formatear.getFecha(getTimeStamp(n), "yyyy-MM-dd HH:mm:ss")+"'}";
          }
         break;
       case (Types.DECIMAL):
       case (Types.DOUBLE):
       case (Types.FLOAT):
       case (Types.INTEGER):
       case (Types.NUMERIC):
       case (Types.REAL):
       case (Types.SMALLINT):
         if (getObject(n) == null)
           s += "null";
         else
           s = s + getDatoNumer1(n);
         break;
       default:
         if (getObject(n) == null)
           s += "null";
         else
           s = s + "'" + getString(n) + "'";
         break;
     }
     s+=", ";
   }
   if (s.length()>0)
     s=s.substring(0,s.length()-2);

   return s;
 }
  /**
  * Rutina Especifica para recoger un PreparedStatement
  * @param String con el la Instrucion a Preparar ..
  *      Esta String sera convertida igual que cualquier otra en esta clase,
  *      soportando el caracter '\uFFFD' y la conversion del TO_DATE, ETC.
  *
  */
  public PreparedStatement getPreparedStatement(String sql) throws SQLException
  {
    String sq;
    sql=sql.trim(); // Quita Espacios a la Sentencia SELECT
    /* Reemplaza el caracter \uFFFD (Asc: 158) por el String establecido
       en setSchema
    */
    sql=Formatear.reemplazar(sql,comodin,schema);

    if (dtb_Con.getDriverType()==conexion.ORACLE)
        sq=sql;
    else
    {
      if ((sq=sqlOracleToInformix(sql,true))==null)
        throw new SQLException(MsgError);
    }
    return getConexion().getConnection().prepareStatement(sq);
  }

  public int getTipoUpdate()
  {
    return modSel;
  }
  /**
   * Pone en modo edicion el registro activo.
   * Modificara linea selecionada por las condiciones de la ultima select
   * lanzada con el parametro forUpate activado
   * @throws SQLException
   */
  public void edit() throws SQLException
  {
    edit(getCondWhere());
  }
  /**
  * Prepara los datos para poder realizar una Modificacion de los registros
  * a traves del SetDatos.
  * @throws SQLException si el cursor no es forUpdate o no hay nada selecionado
  */
  public void edit(String condEdit) throws SQLException
  {
    if (!sqlOpen)
    {
      MsgError = "edit: No Hay Ningun Cursor abierto";
      SqlException = new SQLException(MsgError);
      Error = true;
      throw SqlException;
    }

     this.condEdit=condEdit;
     vecEdit.removeAllElements();
     vecAddNew.removeAllElements();
     int n;
     if (modSel==ADDNEW || NOREG)
     {
       for (n=0;n<getNumCol();n++)
         vecAddNew.addElement("");
     }
     else
     {
       modSel = NORMAL;
       for (n = 0; n < getNumCol(); n++)
         vecAddNew.addElement(getObject(n + 1));
     }
     modSel=EDIT;
  }
  /**
   * Añadir nuevo registro en la tabla mandada como parametro
   * @param tabla
   * @throws SQLException
   */
   public void addNew(String tabla) throws SQLException
   {
    addNew(tabla,true);
   }
  /**
  * Prepara los datos para poder realizar una Insercion de los registros
  * en SedDatos.
  * @param tabla
  * @param allCampos Indica si se debera realizar el insert sobre todos los campos.
  * @throws Exception si el cursor no es forUpdate o no hay nada selecionado
  */
 public void addNew(String tabla,boolean allCampos) throws SQLException
 {
   // Monta la select
   String s = "Select * from " + tabla+" WHERE 1=2";
   setStrSelect(s);

   // Lee la estructura de la tabla
   select(false, true);

   // prepara el cursor para a�adir
   addNew(allCampos);
  }
  public void addNew()  throws SQLException
  {
    addNew(true);
  }
  /**
  * Prepara los datos para poder realizar una Insercion de los registros
  * en SedDatos.
  * Incluir todos los campos por defecto en el insert
  * @throws Exception si el cursor no es forUpdate o no hay nada selecionado
  */
  public void addNew(boolean allCampos) throws SQLException
  {
    if (!sqlOpen)
    {
      MsgError = "edit: No Hay Ningun Cursor abierto";
      SqlException = new SQLException(MsgError);
      Error = true;
      throw SqlException;
    }

    modSel=ADDNEW;
    this.camposAddNew=! allCampos;
    if (! allCampos)
        vecEdit.removeAllElements();
    vecAddNew.removeAllElements();
    for (int n=0;n<getNumCol();n++)
      vecAddNew.addElement("");
  }
   /**
   * Copia TODOS los registros activo de la select de esta tabla  a otra en un datostabla mandado.
   * El datosTabla final no es necesario que  tenga hecho el addnew(tabla) inicial 
   * No se realizara un commit al final. 
   */
  public void copy(DatosTabla dt) throws SQLException
  {
     copy(dt,null,null,0);
  }
  /**
   * Copia TODOS los registros activo de la select de esta tabla  a otra en un datostabla alternativo.
   * El datosTabla final no es necesario que  tenga hecho el addnew(tabla) inicial 
   * No se realizara un commit al final. 
   * Si se detecta que tiene los campos:
   *  his_usunom varchar(15) not null, -- Usuario que realiza el Cambio
   *  his_fecha date not null, -- Fecha de Cambio (Coge la actual del sistema)
   *  his_comen varchar(100) -- Comentario sobre el Cambio
   *  his_rowid int, -- Rowid para unir diferentes modificaciones
   * se rellenaran
   * @param dtFin DatosTabla donde copiar
   * @param usuario
   * @param coment 
   * @param rowid 
   * @throws SQLException 
   */
    public void copy(DatosTabla dtFin,String usuario, String coment,int rowid) throws SQLException
    {
      if (dtFin.modSel!=ADDNEW)
           dtFin.addNew(getNomTabla());
      do
      {
          copiaRegistro(dtFin,usuario,coment,rowid);
          dtFin.addNew(dtFin.getNomTabla());
      } while (next());
      dtFin.modSel=NORMAL;
    }
    /**
     * Copia el registro activo del datostabla activo al datosTabla mandado
     * @param dtFin DatosTabla donde copiar. Debe tener una select ya realizada, y puesto en modo EDIT O ADDNEW
     * @param usuario
     * @param coment
     * @param rowid
     * @throws SQLException 
     */
    public void copiaRegistro(DatosTabla dtFin,String usuario, String coment,int rowid) throws SQLException
    {
      int n;
      for (n=1;n<= getColumnCount();n++)
      {
        if (dtFin.findColumn(getColumnName(n))==0)
            continue;
        if (getTipCampo(n)==Types.TIMESTAMP)
        {
            dtFin.setDato(getColumnName(n) ,getTimeStamp(n));
            continue;
        }
        Object o= getObject(n); 
        if (o==null)
            continue;
        dtFin.setDato(getColumnName(n) ,o);
      }
      if (dtFin.findColumn("his_usunom")>0 && usuario != null)
          dtFin.setDato("his_usunom",usuario);
      if (dtFin.findColumn("his_fecha")>0 )
          dtFin.setDato("his_fecha","CURRENT_TIMESTAMP(2)");
       if (dtFin.findColumn("his_coment")>0 && coment != null)
          dtFin.setDato("his_coment",coment);
       if (dtFin.findColumn("his_rowid")>0 && rowid != 0)
          dtFin.setDato("his_rowid",rowid);
      dtFin.update();
    }
  /**
   * Ejecuta un update sobre una sentencia ADDNEW o EDIT
   * anteriormente lanzada. Usa el statement que tenga
   * definido por defecto este datostabla.
   *
   * @throws SQLException
   */
  public int update() throws SQLException
  {
    return update(getStatement());
  }
   /**
   * Ejecuta un update sobre una sentencia ADDNEW o EDIT
   * anteriormente lanzada.
   *
   * @throws SQLException
   */
  public int update(Statement st) throws SQLException
  {
    int nLinAf = 0;
    if (modSel == NORMAL)
    {
      MsgError = "edit: NO Se ha realizdo un edit o addnew";
      SqlException = new SQLException(MsgError);
      Error = true;
      throw SqlException;
    }
    String s;
    if (modSel == EDIT)
    {
      s = getStrUpdate();
      setSqlUpdate(s);
      s=getSqlUpdate();
      s+= " WHERE "+condEdit;
      s="UPDATE "+nomTabla+ " set "+s;
//      System.out.println("s: "+s);
      try
      {
        nLinAf = st.executeUpdate(s);
      }
      catch (SQLException k)
      {
        throw new SQLException("Error al Realizar el UPDATE (" + s + ") \n" +
                               k.getMessage(), k.getSQLState(), k.getErrorCode());
      }
    }
    else
    {
      s = "INSERT INTO " + nomTabla;
      if (camposAddNew)
        s+= "("+getCamposInsert()+") ";
      s+=" VALUES(" + getStrInsert() + ")";
      setSqlUpdate(s);
      s=getSqlUpdate();
      try {
        nLinAf = st.executeUpdate(s);
      } catch (SQLException k)
      {
        throw new SQLException("Error al Realizar el Insert ("+s+")+\n"+k.getMessage(),k.getSQLState(),k.getErrorCode());
      }
    }
    EOF = false;
    NOREG = false;
    modSel = NORMAL;
    return nLinAf;
  }
  /**
   * Devuelve un string con los campos a insertar en un UPDATE, despues de 
   * un addnew
   * @return String
   * @throws SQLException
   */
  String getCamposInsert() throws SQLException
  {
     int nRow=getNumCol();
     String s="";
      for (int n = 1; n <= nRow; n++) {
          if (vecEdit.indexOf("" + (n - 1)) == -1) 
              continue; // Campo NO modificado       
          if (!isWritable(n))
              continue;
          
          s+=s.equals("")?"":",";
          s += getNomCampo(n);
      }
     return s;
  }
  /*
  public void update(DatosTabla dt) throws SQLException,  ParseException
  {
     if (modSel==NORMAL)
     {
        MsgError="edit: NO Se ha realizdo un edit o addnew";
      SqlException= new SQLException(MsgError);
            Error=true;
      throw SqlException;
     }
     String s;
     if (modSel==EDIT)
     {
       s=getStrUpdate();

       update(s);
       if (dt!=null)
        dt.setAllDatos(s);
     }
     else
     {
       s="INSERT INTO "+nomTabla+" VALUES("+getStrInsert()+")";
       executeUpdate(s);
       if (dt!=null)
       {
        if (! dt.addDatos(getStrUpdate()))
        {
            MsgError=dt.getMsgError();
          SqlException= new SQLException(MsgError);
                Error=true;
          throw SqlException;
        }
       }
       EOF=false;
       NOREG=false;
     }
     modSel=NORMAL;
  }

*/
  public String getNomColRowid()
  {
    return dtb_Con.getNomColRowid();
  }
  /**
   * Retorna si el curso es for update
   */
  public boolean isForUpdate() { return sw_sqlUpdate; }
  /**
   * Indica el tiempo maximo que espera cuando un registro esta bloqueado
   */
  public void setMaxEsperaBloq(int seg) { maxEsperaBloq = seg; }
  /**
   * Retorna el tiempo maximo que espera cuando un registro esta bloqueado
   */
  public int getMaxEsperaBloq() { return maxEsperaBloq; }
  /**
   * Indica si se debe reintentar la select/update si el registro esta bloqueado
   */
  public void setReintentarBloq(boolean sino) { swReintentaBloq = sino; }
  /**
   * Retorna si se debe reintentar la select/update si el registro esta bloqueado
   */
  public boolean getReintentarBloq() { return swReintentaBloq; }
  /**
   * Asigna el CEditable donde debe escribir el mensaje de Bloqueo
   */
  public void setCEditable(CEditable e) { CEditable = e; }

  /**
   * Devuelve la condicion Where (sin la palabra WHERE) de la ultima select ejecutada.
   *
   * @return String con condWhere
   */
  public String getCondWhere()
  {
    return condWhere;
  }
  /**
   * Obliga a utilizar un statement ya creado anteriormente en vez del propio que
   * crea la clase
   * @param st Statement a utilizar
   */
  public void setStatement(Statement st)
  {
    if (st==null)
      cerrarCursor=true;
    else
    {
      stmt1 = st;
      cerrarCursor = false;
    }
  }
  /**
   * Devuelve el resultSet utilizado actualmente
   * @return ResultSet
   */
  public ResultSet getResultSet()
  {
    return rs;
  }
  /**
   * Manda un resultset para tratarlo. Lo usaran las funciones para sacar los datos.
   * 
   * @param rs
   */
  public void setResultSet(ResultSet rs)
  {
    EOF = false;
    BOF = false;
    NOREG = false;
    sqlOpen = true;

    this.rs = rs;
  }

  /**
   * Especifica el tiempo que se esperara para realizar una select
   * antes de realizar un  cancel.
   *
   * @param tiempoSelect int tiempo en decimas de segundo (10 es un segundo)
   */
  public void setTimeSelect(int tiempoSelect)
  {
    timeSelect=tiempoSelect;
  }

  public int getTimeSelect() {
    return timeSelect;
  }

}

/*
class ThreadDT extends Thread
{
  DatosTabla dt;
  int timeSelect;

  public ThreadDT(DatosTabla dt)
  {
    this.dt=dt;
    timeSelect=dt.timeSelect;
    this.start();
  }

  public void run() {
    try {
      while (timeSelect > 0) {
        this.sleep(100);
        if (dt.ejecSelect)
          return;
        timeSelect--;

        if (timeSelect == 0) {
          dt.cancel();
          return;
        }
      }
      ;
    }
    catch (Exception k) {
      k.printStackTrace();
    }
  }
}

*/
