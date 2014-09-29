package gnu.chu.anjelica;

import java.sql.*;
import gnu.chu.sql.*;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.SystemOut;


public class instalar
{
  Statement stUp;
  conexion ct;
  EntornoUsuario EU= new EntornoUsuario();
  String driver,urlDB,pathInst;

  public instalar()
  {
    this(null,null,null);
  }
  public instalar(String driver,String urlDB,String pathInst)
  {
    try {
      this.driver=driver;
      this.urlDB=urlDB;
      this.pathInst=pathInst;
      jbInit();
    } catch (Throwable k)
    {
      SystemOut.print(k);
      System.exit(1);
    }
  }

  private void jbInit() throws Exception
  {
    System.out.println("Instalando BASE de datos");
    EU = gnu.chu.Menu.LoginDB.cargaEntornoUsu();
    EU.usu_nomb = "Usuario Administrador";
    EU.usuario = "sa";
    EU.password = "";
    EU.email = "cpuente@misl.es";
//    if (System.getProperty("os.name").toUpperCase().indexOf("WIN")!=-1)
//      EU.addressDB=EU.addressDB.replace('/',System.getProperty("file.separator").charAt(0));
//    EU.catalog="qc_pruebas";
    EU.ejercicio = 2005;
    if (driver!=null)
    {
      EU.driverDB = driver; //"org.hsqldb.jdbcDriver";
      EU.addressDB = urlDB; //"jdbc:hsqldb:/home/cpuente/anjelica/db";
    }
    if (pathInst != null)
      EU.pathInstall = pathInst;
//    EU.catalog="pruebas";
    EU.previsual = true;
    EU.setSimulaPrint(false);
     ct= new conexion(EU);
     stUp=ct.createStatement();
    // System.out.println("EU.pathInstall: "+EU.pathInstall+" driver: "+EU.driverDB+" URL: "+EU.addressDB);
     gnu.chu.isql.utilSql.executeScript(EU.pathInstall+System.getProperty("file.separator")+"sql"+System.getProperty("file.separator")+"hsql.sql",stUp,true);
     ct.commit();
     gnu.chu.isql.utilSql.executeScript(EU.pathInstall+System.getProperty("file.separator")+"sql"+System.getProperty("file.separator")+"datpruebas.sql",stUp,true);
     ct.commit();
     stUp.execute("SHUTDOWN");
     ct.close();
     System.out.println("");
     System.out.println("");
     System.out.println("Base de datos ... INICIALIZADA CORRECTAMENTE");
  }

  public static void main(String[] args) {
     new instalar();
//    new instalar(args[0],args[1],args[2]);
  }

}
