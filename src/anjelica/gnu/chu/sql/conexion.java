package gnu.chu.sql;

/**
 *
 * <p>Título: Conexion </p>
 * <p>Descripción: Clase generica para realizar una conexion a la base de datos</p>
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
 */
import gnu.chu.utilidades.EntornoUsuario;
import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class conexion  implements Connection
{
  private String nomColRowid="rowid";
  private int driverType=0;
  String Usuario="";
  String PalabraPaso="";
  String Clase="";
  String ConURL="";
  private Connection ct;
//  boolean sw_conectado=false;
  String MsgError="";
  int nStam=-1;
  public SQLException sqlException;
  public final static int INFORMIX=1;
  public final static int ORACLE=2;
  public final static int OTRO=3;
  public final static int POSTGRES=4;
  public final static int MSQL=5;
  public final static int MYSQL = 6;
  public final static int HSQL = 7;

  public conexion()
  {

  }
  public conexion(EntornoUsuario eu) throws SQLException, ClassNotFoundException,IllegalAccessException,InstantiationException
  {
    this(eu.usuario,eu.password,eu.driverDB,
                      eu.addressDB);
  }
  public conexion(String usu,String palpas,String clase,String url) throws SQLException, ClassNotFoundException,IllegalAccessException,InstantiationException
  {
     setUsuario(usu);
     setPalabraPaso(palpas);
     setClase(clase);
     setConURL(url);
     Conecta();
  }
  public conexion(Connection ct)
    {
      this.ct=ct;
  }
  public boolean isConectado()
  {
    if (ct!=null)
    {
      try {
        return (! ct.isClosed());
      } catch (SQLException k)
      {
        sqlException=k;
        MsgError="Error al Testear si la ct esta Cerrada\n"+k.getMessage();
        return false;
      }
    } else
      return false;
  }

  public String getMsgError()
  {
          return MsgError;
  }

  // Funcion para llamar al createStament de La conexion Abierta.
  public Statement crearEstamento()  throws SQLException
  {
    nStam++;
    try
    {
      return ct.createStatement();
    } catch (SQLException k)
    {
      throw new SQLException("ConexDB. Error al Crear Estamento. Estamentos: "+nStam+"  \n"+k.getMessage());
    }
  }

  public void close() throws SQLException {
    if (! ct.isClosed())
    {
//      if (driverType == HSQL)
//        ct.createStatement().execute("SHUTDOWN");

      ct.rollback();
      ct.close();
    }

  }
  public void Conecta() throws SQLException, ClassNotFoundException,IllegalAccessException,InstantiationException
  {
    if (Clase.indexOf("informix")>=0)
      conectaInformix();
    else if (Clase.indexOf("sun.jdbc.odbc.JdbcOdbcDriver") >= 0)
      conectaOtros();
    else if (Clase.indexOf("postgresql")>=0)
      conectaPostgres();
    else if (Clase.indexOf("sqlserver")>=0)
      conectaMsql();
    else if (Clase.indexOf("mysql")>=0)
      conectaMysql();
    else if (Clase.indexOf("hsql")>=0)
      conectaHsql();
    else
      throw new SQLException("NO se que clase usar para conectarme. CLASE: "+Clase);
  }

  public void conectaPostgres() throws SQLException, ClassNotFoundException,IllegalAccessException,InstantiationException
  {
    Properties p = new Properties();
    p.setProperty("user",Usuario);
    p.setProperty("password",PalabraPaso);
//    ConURL=ConURL+";user="+Usuario+";password="+PalabraPaso;
    Class.forName(Clase);
    ct= DriverManager.getConnection(ConURL,p);
    ct.setTransactionIsolation(ct.TRANSACTION_READ_COMMITTED);
    ct.setAutoCommit(false);
    nomColRowid="oid";
    driverType=POSTGRES;
  }
  public void conectaMysql() throws SQLException, ClassNotFoundException,IllegalAccessException,InstantiationException
   {
     Properties p = new Properties();
     p.setProperty("user",Usuario);
     p.setProperty("password",PalabraPaso);
//    ConURL=ConURL+";user="+Usuario+";password="+PalabraPaso;
     Class.forName(Clase);
     ct= DriverManager.getConnection(ConURL,p);
     nomColRowid=null;
     ct.setTransactionIsolation(ct.TRANSACTION_REPEATABLE_READ);
     ct.setAutoCommit(true);
     driverType=MYSQL;
   }

  public void conectaOtros() throws SQLException, ClassNotFoundException,IllegalAccessException,InstantiationException
  {
    conectaOracle();
    driverType=OTRO;
  }
  public void conectaMsql() throws SQLException, ClassNotFoundException,IllegalAccessException,InstantiationException
  {
    Properties p = new Properties();
//    p.setProperty("SelectedMethod", "cursor");
    Driver driver;
//    ConURL=ConURL+",user="+Usuario+";password="+PalabraPaso;

    driver = (Driver)Class.forName(Clase).newInstance();
    p.put("user", Usuario);
    p.put("password", PalabraPaso);
    driver = (Driver)Class.forName(Clase).newInstance();
    ct  = driver.connect(ConURL, p);
//    ct=DriverManager.getConnection(ConURL,Usuario,PalabraPaso);
    setAutoCommit(true);
    ct.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
    nomColRowid=null;
    driverType=MSQL;
  }

  public void conectaOracle() throws SQLException, ClassNotFoundException,IllegalAccessException,InstantiationException
  {
    Properties p = new Properties();
    Driver driver;

    p.put("user", Usuario);
    p.put("password", PalabraPaso);
    driver = (Driver)Class.forName(Clase).newInstance();
    ct  = driver.connect(ConURL, p);
    setAutoCommit(false);
    driverType=ORACLE;
  }

  public void conectaInformix() throws SQLException, ClassNotFoundException,IllegalAccessException,InstantiationException
  {
    Properties p = new Properties();

    ConURL=ConURL+";user="+Usuario+";password="+PalabraPaso;
    Class.forName(Clase);
    ct= DriverManager.getConnection(ConURL,p);
    ct.setTransactionIsolation(ct.TRANSACTION_READ_UNCOMMITTED);
    setAutoCommit(false);
    driverType=INFORMIX;
  }
  public void conectaHsql() throws SQLException, ClassNotFoundException,IllegalAccessException,InstantiationException
   {
     Class.forName(Clase);
     ct = DriverManager.getConnection(ConURL,"sa","");//Usuario,PalabraPaso);
   //  ct.setTransactionIsolation(ct.TRANSACTION_NONE);
     driverType=HSQL;

   }

  public void setAutoCommit(boolean autocommit) throws SQLException
  {
     ct.setAutoCommit(autocommit);
  }

  public boolean getAutoCommit() throws SQLException
  {
     return ct.getAutoCommit();
  }
  public Connection getConnection()
  {
     return ct;
  }

  public void setConURL(String u)
  {
          ConURL=u;
  }
  public String getConURL()
  {
          return ConURL;
  }

  public void setPalabraPaso(String u)
  {
          PalabraPaso=u;
  }

  public String getPalabraPaso()
  {
          return PalabraPaso;
  }

  public void setUsuario(String u)
  {
          Usuario=u;
  }

  public String getUsuario()
  {
          return Usuario;
  }

  public void setClase(String c)
  {
          Clase=c;
  }

  public String getClase()
  {
          return Clase;
  }
  public int getDriverType()
  {
    return driverType;
  }
  public String getNomColRowid()
  {
    return nomColRowid;
  }
  public void setNomColRowid(String nomCol)
  {
    nomColRowid=nomCol;
  }
  public Statement createStatement() throws SQLException {
    return ct.createStatement();
  }
  public PreparedStatement prepareStatement(String sql) throws SQLException {
    return ct.prepareStatement(sql);
  }
  public CallableStatement prepareCall(String sql) throws SQLException {
    return ct.prepareCall(sql);
  }
  public String nativeSQL(String sql) throws SQLException {
    return ct.nativeSQL(sql);
  }
  public void commit() throws SQLException {
    if (!ct.getAutoCommit())
      ct.commit();
  }
  public void rollback() throws SQLException {
    if (!ct.getAutoCommit())
      ct.rollback();
  }
  public boolean isClosed() throws SQLException {
    return ct.isClosed();
  }
  public DatabaseMetaData getMetaData() throws SQLException {
    return ct.getMetaData();
  }
  public void setReadOnly(boolean readOnly) throws SQLException {
    ct.setReadOnly(readOnly);
  }
  public boolean isReadOnly() throws SQLException {
    return ct.isReadOnly();
  }
  public void setCatalog(String catalog) throws SQLException {
    if (catalog==null)
      return;
    ct.setCatalog(catalog);
  }
  public String getCatalog() throws SQLException {
    return ct.getCatalog();
  }
  public void setTransactionIsolation(int level) throws SQLException {
    ct.setTransactionIsolation(level);
  }
  public int getTransactionIsolation() throws SQLException {
    return ct.getTransactionIsolation();
  }
  public SQLWarning getWarnings() throws SQLException {
    return ct.getWarnings();
  }
  public void clearWarnings() throws SQLException {
    ct.clearWarnings();
  }
  public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
    return ct.createStatement(resultSetType,resultSetConcurrency);
  }
  public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
    return ct.prepareStatement(sql,resultSetType,resultSetConcurrency);
  }
  public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
    return ct.prepareCall(sql,resultSetType,resultSetConcurrency);
  }
  public Map getTypeMap() throws SQLException {
    return ct.getTypeMap();
  }
  public void setTypeMap(Map map) throws SQLException {
    ct.setTypeMap(map);
  }
  public void setHoldability(int holdability) throws SQLException {
    ct.setHoldability(holdability);
  }
  public int getHoldability() throws SQLException {
    return ct.getHoldability();
  }
  public Savepoint setSavepoint() throws SQLException {
    return ct.setSavepoint();
  }
  public Savepoint setSavepoint(String name) throws SQLException {
    return ct.setSavepoint(name);
  }
  public void rollback(Savepoint savepoint) throws SQLException {
    ct.rollback(savepoint);
  }
  public void releaseSavepoint(Savepoint savepoint) throws SQLException {
    ct.releaseSavepoint(savepoint);
  }
  public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
    return ct.createStatement(resultSetType,resultSetConcurrency,resultSetConcurrency);
  }
  public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
    return ct.prepareStatement(sql,resultSetType,resultSetConcurrency,resultSetHoldability);
  }
  public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
    return prepareCall(sql,resultSetType,resultSetConcurrency,resultSetHoldability);
  }
  public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
    return prepareStatement(sql,autoGeneratedKeys);
  }
  public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
    return ct.prepareStatement(sql,columnIndexes);
  }
  public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
    return ct.prepareStatement(sql,columnNames);
  }

    public Clob createClob() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Blob createBlob() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public NClob createNClob() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public SQLXML createSQLXML() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isValid(int timeout) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getClientInfo(String name) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Properties getClientInfo() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setSchema(String schema) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getSchema() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void abort(Executor executor) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getNetworkTimeout() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
