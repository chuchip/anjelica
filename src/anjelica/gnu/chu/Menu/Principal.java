package gnu.chu.Menu;

import gnu.chu.anjelica.almacen.actStkPart;
import gnu.chu.anjelica.pad.pdusua;
import gnu.chu.mail.sendMail;
import gnu.chu.sql.*;
import gnu.chu.utilidades.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.rmi.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import javax.swing.*;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

/**
 *
 * <p>Título: Principal.java</p>
 * <p>Descripción: Clase  de la que extiende Menu1.
 *  </p>
 * <p>Copyright: Copyright (c) 2005-2011
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los términos de la Licencia Pública General de GNU segun es publicada por
 *  la Free Software Foundation, bien de la versión 2 de dicha Licencia
 *  o bien (según su elección) de cualquier versión posterior.
 *  Este programa se distribuye con la esperanza de que sea útil,
 *  pero SIN NINGUNA GARANTIA, incluso sin la garantía MERCANTIL implícita
 *  o sin garantizar la CONVENIENCIA PARA UN PROPOSITO PARTICULAR.
 *  Véase la Licencia Pública General de GNU para más detalles.
 *  Debería haber recibido una copia de la Licencia Pública General junto con este programa.
 *  Si no ha sido así, escriba a la Free Software Foundation, Inc.,
 *  en 675 Mass Ave, Cambridge, MA 02139, EEUU.
 * </p>
 * <p>Empresa: miSL</p>
 * @author chuchi P
 * @version 1.1
 */
public class Principal extends JFrame
{
  vlike lkUsu=new vlike();
  sendMail sm;
  public HashMap ht=new HashMap();
  EntornoUsuario EU;
  BarraEstado statuBar;
  protected Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
  /**
  * Panel donde se tienen que ejecutar todas las aplicaciones
  */
  public JDesktopPane panel1 = new JDesktopPane();
    /**
	*        	*  *  	*	El Gestor de programas
    */
  public Gestor gestor = new Gestor(panel1, this);

  protected  conexion MyDb;
  public DatosTabla dt1;
  public DatosTabla dt2;
  protected  conexion dbMenu;
  protected  DatosTabla dtMenu;

  public EntornoUsuario Usuario=null;
  protected  String usuario = "";
  protected  String password = "";

//  protected pdempresas pdEmpresas;

  protected static Remote rmiImpresion;
  protected static Remote rmiVisualizador;
  protected static Remote rmiEjecutarBG;
  protected static boolean RMISecurCreado=false;
  protected static boolean RMIImpreCreado=false;
  protected static boolean RMIVisuaCreado=false;
  protected static boolean RMIExecCreado=false;

  protected String bd;

  protected static ResourceBundle param;
  protected static ResourceBundle listDB;

  // Timer que se conecta con el Servidor de Notificaciones
  protected  javax.swing.Timer timerNotifi;
//  protected  NotifiClie notifiClie = null;
  protected JPanel panelDeNotas = null;

  public JPopupMenu popMenu = new JPopupMenu();
  protected  boolean swMenuCargado = false;

  protected boolean swActiva = false;

  protected String DriverDB = null;
  protected String AddressDB = null;
    /**
   * Contructor
   */
  public Principal() {
    try
    {
      jbInit();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      System.exit(1);
    }
  }
    /**
   * Contructor
   */
  public Principal(String usuario, String password, conexion conex) {
    try {
      this.usuario = usuario;
      this.password = password;
      this.MyDb = conex;
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
        System.exit(1);
    }
  }
  protected void conectado() throws Exception {}
  //Component initialization
  private void jbInit() throws Exception{

    if (!Conectar())
    {
      System.exit(0);
      return;
    }
    conectado();

    this.setDefaultCloseOperation(this.DO_NOTHING_ON_CLOSE);

    this.setSize(new Dimension(screenSize.width, screenSize.height-20));
    //Center the window
    Dimension frameSize = getSize();
    if (frameSize.height > screenSize.height)
       frameSize.height = screenSize.height;
    if (frameSize.width > screenSize.width)
       frameSize.width = screenSize.width;
    setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

    bd = Usuario.addressDB;
//    String iconEmp = System.getProperty("empresa");
//    if (iconEmp == null)
    String iconEmp = "anjelica";

    ImageIcon a = Iconos.getImageIcon(iconEmp.toLowerCase());
    this.setIconImage(a.getImage());

    bd = bd.toUpperCase();
    EU = LoginDB.cargaEntornoUsu();
    EU.dialogoPrint=lkUsu.getString("usu_diapri").equals("S");
    EU.previsual=lkUsu.getString("usu_previ").equals("S");
    Usuario.dialogoPrint=EU.dialogoPrint;
    Usuario.previsual=EU.previsual;
    Usuario.pathReport=EU.pathReport;
    Usuario.setPathReportAlt(EU.getPathReportAlt());
    Usuario.debug=EU.debug;
    Usuario.pathCom=EU.pathCom;
    Usuario.dirTmp=EU.dirTmp;
    Usuario.comPrint=EU.comPrint;
    Usuario.puertoAlb=EU.puertoAlb;
    Usuario.pathCom=EU.pathCom;
    Usuario.catalog=EU.catalog;
    Usuario.setImpresoras(EU.getImpresoras());
    if (EU.getLog4J() == null)
      BasicConfigurator.configure();
    else
    {
      if (EU.getLog4J().indexOf("xml") >= 0)
      {
        DOMConfigurator.configure(EU.getLog4J());
      }
      else
        PropertyConfigurator.configureAndWatch(EU.getLog4J());
    }
//    System.out.println("Usuario.dirTmp:"+ Usuario.dirTmp);
    org.apache.log4j.Appender fl=null;
    Enumeration enum1= org.apache.log4j.Logger.getRootLogger().getAllAppenders();
    while (enum1.hasMoreElements())
    {
       fl=( org.apache.log4j.Appender) enum1.nextElement();
       if (fl instanceof org.apache.log4j.FileAppender && fl != null)
       {
         String fich=((org.apache.log4j.FileAppender) fl).getFile();
         if (fich != null)
         {
           int lastBarra = fich.lastIndexOf("/");
           if (lastBarra != -1)
             fich = fich.substring(lastBarra+1);
         }
         ( (org.apache.log4j.FileAppender) fl).setFile(Usuario.dirTmp + "/" +fich);
         ( (org.apache.log4j.FileAppender) fl).activateOptions();
       }
     }
//     Logger.getLogger(this.getName()).debug("aa");
    this.setTitle("Anjelica (" + EU.empresa + ")");

    gestor.posicionaComponent(this, "Menu " + EU.empresa);

    activarEventos();
    gnu.chu.print.util.limpiaTmp(EU);

    recargaMenu();

  /*  notifiClie = new NotifiClie() {
               public String confirmaCierreProceso(int Id) {
                      try {
                          Vector procesos = gestor.getProcesosActivos();

                          for (int i=0;i<procesos.size();i++) {
                              int PID = ((InfoProgram)procesos.elementAt(i)).getPID();
                              if (PID == Id) {
                                 if (!((InfoProgram)procesos.elementAt(i)).isMatable())
                                    return "ERROR. Programa no Matable";
                                 gestor.tabla.matando(new StringBuffer("" + PID));
                                 return "OK";
                              }
                          }
                          return "ERROR. PROCESO NO ENCONTRADO";
                      } catch (Throwable j) {
                        return "ERROR: " + j.getMessage();
                      }
               }
               public Vector getProcesos() {
                      Vector v = new Vector();
                      try {
                          Vector procesos = gestor.getProcesosActivos();

                          for (int i=0;i<procesos.size();i++) {
                              String[] s = new String[2];
                              s[0] = ""+((InfoProgram)procesos.elementAt(i)).getPID();
                              s[1] = ((InfoProgram)procesos.elementAt(i)).getDescripcion();
                              v.addElement(s);
                          }

                      } catch (Throwable j) {
                        return new Vector();
                      }

                      return v;
               }
               public String confirmaCierre() {
                      try {
                          salir(false);
                          return "OK";
                      } catch (Throwable j) {
                        return "Error al Cerrar Menu\n" + j.getMessage();
                      }
               }
               public void msgClie(String usuEnv, String fecha, String msg) throws Throwable {
                 final String usuEnvT = usuEnv;
                 final String fechaT = fecha;
                 final String msgT = msg;
                 new miThread("") {
                     public void run() {
                      if (getState() == Frame.ICONIFIED)
                         aviso(usuEnvT, fechaT, msgT);
                      else if (!swActiva)
                          aviso(usuEnvT, fechaT, msgT);
                      else if (panelDeNotas == null)
                          aviso(usuEnvT, fechaT, msgT);
                      if (panelDeNotas != null) {
                         final Nota n = new Nota(Principal.this, usuEnvT, fechaT, msgT, panelDeNotas);
                         panelDeNotas.add(n);
                         validate();
                         repaint();
                      }
                     }
                 };
               }
    };*/
    panel1.setBackground(new Color(0,128,128));
    }

  /**
   * Recarga el Menu
   */
  protected void recargaMenu() {
    new miThread("Cargando Menu ....", gestor.grupo)
    {
        public void run() {
               try {
                 this.sleep(100);
                 if (Usuario!=null)
                 {
                   dbMenu = new conexion(Usuario.usuario, Usuario.password,
                                         Usuario.driverDB, Usuario.addressDB);
                   if (!dbMenu.isConectado())
                     throw new Exception(
                         "No se ha podido conectar con la Base de Datos\n" +
                         dbMenu.getMsgError());
                   dbMenu.setCatalog(Usuario.catalog);
                   dtMenu = new DatosTabla(dbMenu);

                   Thread.currentThread().setPriority(3);

                   cargaMenu();
                 }
               } catch (Throwable j) {
                 j.printStackTrace();
//                 mensajes.mensajeUrgente("Error al Cargar Menu\n" + j.getMessage());
               }
               try {
                   dtMenu.cerrar();
               } catch (Throwable j) {}
               try {
                   dbMenu.close();
               } catch (Throwable j) {}
             }
    };
  }
  protected void cargaMenu() throws Throwable {}
  protected void windowOpened() {}
  private void activarEventos() {
        /**
     * Cierra la Aplicacion
     * Posiciona el MenuPrincipal Centrado
     */
    this.addWindowListener(new WindowAdapter()
    {
      public void windowOpened(WindowEvent e)
      {
        Principal.this.windowOpened();
      }

      public void windowClosing(WindowEvent e)
      {
        salir();
      }

      public void windowActivated(WindowEvent e)
      {
        swActiva = true;
      }

      public void windowDeactivated(WindowEvent e)
      {
        swActiva = false;
      }
    });

  }

  private void reiniciar() {
            if (System.getProperty("os.name").toUpperCase().indexOf("WIN") != -1 && !bd.equals("RDOSPO")) {
               try {
                   Runtime.getRuntime().exec("menu.exe " + System.getProperty("Param") + " -DUsuario=" + Usuario.usuario + " -DPass=" + Usuario.password);
               } catch (Throwable j) {
                 ejecutarVM("bin/");
               }
            } else {
              ejecutarVM("bin/");
            }
  }
  private void ejecutarVM(String pathVM) {
          String jvm = pathVM + "java";
          if (System.getProperty("os.name").toUpperCase().indexOf("WIN") != -1)
             jvm = pathVM + "javaw.exe";
          jvm += " -cp \"" + System.getProperty("java.class.path") +
                                           "\" -Djava.security.policy=" + System.getProperty("java.security.policy") +
                                           " -DParam=" + System.getProperty("Param") + " -DUsuario=" + Usuario.usuario + " -DPass=" + Usuario.password +
                                           " VirtualCom.Menu.Menu";
          try {
              Runtime.getRuntime().exec(jvm);
          } catch (Throwable j) {
            if (pathVM.equals(""))
               mensajes.mensajeAviso("ERROR AL REINICIAR MENU\n" + j.getMessage());
            else
                ejecutarVM("");
          }
  }
  protected void salir() {
          salir (true);
  }
  protected void salir(boolean preguntar) {
            salir(preguntar, false);
  }
  protected void salir(boolean preguntar, final boolean reiniciar) {
    Vector procesos = gestor.getProcesosActivos();

    boolean salir=true;
    if (procesos.size()==0) {
       if (preguntar) {
          if (mensajes.mensajeYesNo("Esta seguro que desea Salir?",this) == mensajes.NO)
             return;
       }
    } else {
      for (int i=0;i<procesos.size();i++) {
        if (!((InfoProgram)procesos.elementAt(i)).isMatable()) {
          salir = false;
          break;
        }
      }
      if (!preguntar)
         salir = true;
      if (!salir) {
        mensajes.mensajeAviso("Tienes Programas que no se pueden Cerrar\nCierralos y vuelve a intentarlo", this);
        return;
      } else {
        if (preguntar) {
           if (mensajes.mensajeYesNo("Tienes Programas Abiertos\nDesea que los Cierre?", this)==mensajes.NO)
              return;
        }
        for (int i=0;i<procesos.size();i++)
          gestor.tabla.matando(new StringBuffer(((InfoProgram)procesos.elementAt(i)).getPID()));

        procesos = gestor.getProcesosActivos();
        if (procesos.size()!=0) {
           if (preguntar) {
              if (mensajes.mensajeYesNo("No se pudo cerrar todos los programas\nDesea salir de todas formas?", this)==mensajes.NO)
                 return;
           }
        }
      }
    }

    if (preguntar)
       gestor.guardaPosicion(this, "Menu " + bd);
     try {
       if (dt1.getConexion().getDriverType() == conexion.HSQL)
         dt1.executeUpdate("SHUTDOWN");
     } catch (Exception k)
     {
       k.printStackTrace();
     }
    if (preguntar)
    {
       if (reiniciar)
          reiniciar();
        ht.clear();
        try {
          ht.put("%u",
                 Usuario.usuario + " (" + InetAddress.getLocalHost().getHostName() +
                 ")");
          guardaMens("MS",ht);
          System.out.close();
          System.err.close();
          enviaSalEstandard();
          enviaSalError();
        } catch (Throwable k){}

       System.exit(0);
    }
    else
    {
         new miThread("Cerrando Menu", gestor.grupo)
         {
             public void run() {
                    try {
                        Thread.currentThread().sleep(1000);
                    if (reiniciar)
                       reiniciar();
                     System.out.close();
                     System.err.close();
                    enviaSalEstandard();
                    enviaSalError();
                   } catch (Throwable j) {}
                    System.exit(0);
             }
         };
    }
  }




  /**
   * Conecta con la base de datos
   * @return Devuelve true si la conexion es correcta
   */
  private boolean Conectar()
  {

    DriverDB = MyDb.getClase(); //.getString("DriverDB" + i);
    AddressDB = MyDb.getConURL(); //.getString("AddressDB" + i);


    try
    {
      dt1 = new DatosTabla(MyDb);
      dt2 = new DatosTabla(MyDb);
     
      String s = "Select * from usuarios where usu_nomb = '" + usuario + "'";

      if (!dt1.selectInto(s,lkUsu))
      {
        mensajes.mensajeUrgente("Error al Leer Usuario\n" + dt1.getMsgError(), this);
        return false;
      }
      if (!dt1.getString("usu_activ").equals("S"))
      {
        mensajes.mensajeUrgente("USUARIO NO ESTA ACTIVO", this);
        return false;
      }

      s = "select * from v_empresa where emp_codi = " + dt1.getDatos("emp_codi");
      vlike lk = new vlike();
      if (!dt2.selectInto(s, lk))
      {
        mensajes.mensajeUrgente("Error al Leer Empresas\n" + dt2.getMsgError(), this);
        return false;
      }
      // Recoge los datos del usuario
      Usuario = new EntornoUsuario(Formatear.StrToInt(dt1.getDatos("emp_codi").
          toString()), lk.getDatos("emp_nomb").toString().trim(),
          Formatear.StrToInt(dt1.getDatos("eje_nume").toString()), usuario,
          password, DriverDB, AddressDB);
      Usuario.usu_nomb = dt1.getString("usu_nomco");
      Usuario.email = dt1.getString("usu_email");
      Usuario.lkEmpresa = lk;
      Usuario.setUsuReser1(dt1.getString("usu_rese1"));
   
      Usuario.iniciarParametros(dt1);
    }
    catch (Exception j)
    {
      j.printStackTrace();
      mensajes.mensajeUrgente("Error al Leer Usuario/Empresas\n" + j, this);
      return false;
    }

    return true;
  }

    /**
    * Cambia Entorno de trabajo
    * @param ent Entorno
    */
   protected void setCambioFormato(String ent)
   {
     try
     {
       UIManager.setLookAndFeel(ent);
       this.validate();
       this.repaint();
     }
     catch (Exception f)
     {
       mensajes.mensajeUrgente("Error al Cambiar Entorno de Trabajo\n" + f, this);
       return;
     }
     SwingUtilities.updateComponentTreeUI(this);
   }
  public static void setSecurityManager() {
    if (!RMISecurCreado) {
      System.setSecurityManager(new RMISecurityManager());
      RMISecurCreado = true;
    }
  }

  /**
   * Visualiza el Menu de Persianas
   */
  public void showMenu(Component c) {
         if (!swMenuCargado)
            return;
         popMenu.setVisible(true);
         popMenu.setLocation(c.getLocation().x, panel1.getSize().height + c.getLocation().y - popMenu.getSize().height);
         popMenu.show(panel1, c.getLocation().x, panel1.getSize().height + c.getLocation().y - popMenu.getSize().height);
  }
  /**
   * Visualiza la pantalla de Menu
   */
  public void activaMenu() {}
  /**
   * Puede Ejecutar parametros
   */
  public boolean puedeEjecutarParam() {

    return true;
  }
  protected void ejecutar(String clas, String desc) {}
  /**
   * Guarda mensajes en la tabla histmens
   * @param codMens Codigo Mensaje
   * @param ht Parametros diversos para poner en los mensajes
   */
  public void guardaMens(String codMens,HashMap ht)
  {
    guardaMens(dt1,codMens,ht,null,Usuario.usuario);
  }
    /**
   * Guarda mensajes en la tabla histmens
   * @param codMens Codigo Mensaje
   * @param ht Parametros diversos para poner en los mensajes
    *@param explicac Explicacion por la que se creo el mensaje dada por el usuario
    *@param usuario que genera el evento. (Login)
    * interactivamente.
   */
  public static void guardaMens(DatosTabla dt,String codMens,HashMap ht,String explicac,String usuario)
  {
    String s;
    String index;
    java.sql.Statement st;
    try
    {
      st=dt.getConexion().createStatement();
      s="SELECT * FROM mensajes WHERE men_codi= '"+codMens+"'";
      if (! dt.select(s))
      {
        s="SELECT * FROM mensajes WHERE men_codi= 'ER'";
        if (dt.select(s))
        {
          ht.clear();
          ht.put("%c", codMens);
          guardaMens(dt,"ER", ht,null,usuario);
        }
        return;
      }
      s=dt.getString("men_nomb");
      Iterator it= ht.keySet().iterator();
      while (it.hasNext())
      {
        index=it.next().toString();
        s=s.replaceAll(index,ht.get(index).toString());
      }
      if (s.length()>255)
        s=s.substring(0,254);
      dt.addNew("histmens",false);
      dt.setDato("usu_nomb",usuario);
      dt.setDato("him_fecha",Fecha.getFechaSys("dd-MM-yyyy"),"dd-MM-yyyy");
      dt.setDato("him_hora",Fecha.getFechaSys("HH.mm"));
      dt.setDato("men_codi",codMens);
      dt.setDato("men_nomb",s);
      dt.update(st);
      if (explicac!=null)
      { // Guardar explicacion
           s="SELECT  currval(pg_get_serial_sequence('histmens', 'him_codi')) ";
           ResultSet rs=dt.getStatement().executeQuery(s);
           rs.next();
           int himCodi=rs.getInt(1);
           dt.addNew("razonmens");
           dt.setDato("him_codi",himCodi);
           dt.setDato("rme_descr",explicac);
           dt.update(st);
      }
      dt.getConexion().commit();
      st.close();
    } catch (Exception k)
    {
        Logger.getLogger(Principal.class.getName()).error(ventana.getCurrentStackTrace());
    }
  }
   /**
   * Guarda mensajes en la tabla histmens
   * @param codMens Codigo Mensaje
   * @param ht Parametros diversos para poner en los mensajes
    *@param explicac Explicacion por la que se creo el mensaje dada por el usuario
    * interactivamente.
   */
  public void guardaMens(String codMens,HashMap ht,String explicac)
  {
    guardaMens(dt1,codMens,ht,explicac,Usuario.usuario);
   
  }
  void enviaSalEstandard() throws Throwable
   {
     if (gnu.chu.Menu.LoginDB.getDirMailAviso()==null)
       return;
     if (sm==null)
       sm =new sendMail(false,Usuario);
     File f = new File(Usuario.dirTmp + "salida.out");
     if (! f.exists())
       return;

     if (f.length()==0)
     {
       f.delete();
       return;
     }
     FileInputStream fr = new FileInputStream(f);
     sm.addFile(f);
     sm.send(gnu.chu.Menu.LoginDB.getDirMailAviso(), "<ANJELICA> Salida Standard");
     fr.close();
     f.delete();
   }

   void enviaSalError() throws Throwable
   {
     if (gnu.chu.Menu.LoginDB.getDirMailAviso()==null)
       return;
     if (sm==null)
       sm =new sendMail(false,Usuario);

     File f = new File(Usuario.dirTmp + "salida.err");
     if (! f.exists())
       return;
     if (f.length()==0)
     {
       f.delete();
       return;
     }
     FileInputStream fr = new FileInputStream(f);
     sm.addFile(f);

     sm.send(gnu.chu.Menu.LoginDB.getDirMailAviso(), "<ANJELICA> Salida Errores");
     fr.close();
     f.delete();
   }
  public boolean canExecuteClase(String clase, String usuario) throws SQLException
  {
    if (pdusua.canEjecutarProgExt(usuario,dt1) )
        return true;
    return dt1.select("select * from menus where mnu_prog like '%"+clase+"%' and mnu_usua='"+usuario+"'");
  }
}
