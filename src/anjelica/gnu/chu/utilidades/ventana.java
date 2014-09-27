package gnu.chu.utilidades;

import com.jgoodies.looks.plastic.PlasticInternalFrameUI;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;
import java.util.Vector;
import javax.swing.event.*;
import java.sql.*;
import gnu.chu.controles.*;
import gnu.chu.interfaces.*;
import gnu.chu.sql.*;
import gnu.chu.Menu.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import org.apache.log4j.Logger;



/**
 *
 * <p>Título: ventana</p>
 * <p>Descripción: InternalFrame de las q derivaran las mayorias de los programas
 * (incluido ventanaPad). Incluye una serie de funciones generales de ayuda. Es
 * la clase base que instanciara el menu
 *  </p>
 * <p>Copyright: Copyright (c) 2005-2012
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los terminos de la Licencia Pública General de GNU seg�n es publicada por
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
 *
 * @author chuchi P.
 * @version 2.0
 */
public class ventana extends CInternalFrame implements ejecutable
{
  private PopOcupado popOcupado;
  boolean trabajando=false;
  ArrayList vActList;
  private PopEspere popEspere;;
  private String tablaLock=null;
  private String registroLock=null;
  public static Logger logger = Logger.getRootLogger();
  String versionID="1.0";
  String titProg="";
  public String msgBloqueo="";
  public Principal jf;
  public conexion ct;
  public conexion ctUp; // Conexion para Updates.
  public java.sql.Statement stUp; // Statement para updates.
  public JLayeredPane vl;
  public DatosTabla dtStat=null,dtCon1=null;
  public EntornoUsuario EU;
  boolean reintentarBloqueo=false;
  public boolean muerto=false;
//  public String setTitulo(";
  public boolean eje;
  public int pid = 0;
  public StatusBar statusBar; // = new StatusBar(this);

  public BorderLayout borderLayout1 = new BorderLayout();
  public boolean errorInit=false; // Indica si hubo un error en el Constructor.
  public escribe systemOut;


  protected String gestion = "";

  private Dimension dOriginal = null;
  private JPopupMenu popMenu = new JPopupMenu();

  public ventana() {

    systemOut = new escribe(System.out);
    printError pError = new printError(System.out, false);
    pError.setEjecutable(this);

    try {
      setClosable(isMatable());
    }
    catch (Exception j) {
      setClosable(false);
    }
    setDefaultCloseOperation(CInternalFrame.DO_NOTHING_ON_CLOSE);
    addInternalFrameListener(new InternalFrameAdapter() {
            @Override
      public void internalFrameClosing(InternalFrameEvent e) {
        if (isCloseable()) {
          if (statusBar!=null)
            statusBar.setEnabled(false);
          matar();
        }
      }
    });
    try
    {
      jbInit();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    //    System.setErr(pError);
  }

  public void setTitulo(String titulo)
  {
    this.setTitle(titulo);
    titProg=titulo;
  }
  public String getTitulo()
  {
    return titProg;
  }

  public int fatalError(String s)
  {
    return fatalError(s, new Exception());
  }

  public int fatalError(String s, SQLException k)
  {
    k.printStackTrace(systemOut);

    PopError pe= new PopError("Error de Base de Datos\n"+s+"\n"+systemOut.getMessage());
    pe.setEjecutable(this);
    pe.Bcancelar.requestFocus();
    pe.setVisible(true);
    try  {
        logger.error("Usuario: "+EU.usu_nomb+" en host: "+Inet4Address.getLocalHost().getHostAddress()+"\n"+
                 s+"\n"+systemOut.getMessage() );
    } catch (UnknownHostException k1)
    {
        logger.error("Usuario: "+EU.usu_nomb+"\nHost: DESCONOCIDO \n"+
                 s+"\n"+systemOut.getMessage() );
    }

//    enviaMailError(((pe.getResultado() == PopError.REINTENTAR)?"ERROR REINTENTADO\n":"ERROR CANCELADO\n") + s);

    return pe.getResultado();
  }
  public static String getCurrentStackTrace()
  {
    String s="";
    StackTraceElement[] st=Thread.currentThread().getStackTrace();
    for (int n=2;n<st.length;n++)
        s+=st[n].toString()+"\n";
    return s;
  }
  public int fatalError(String s, Exception k)
      {
    return fatalError(s,(Throwable) k);
  }
  public int fatalError(String s, Throwable k)
  {

    k.printStackTrace( systemOut);
    try {
       logger.error("Usuario: "+EU.usu_nomb+"\nHost: "+Inet4Address.getLocalHost().getHostAddress()+"\n"+
                 s+"\n"+systemOut.getMessage() );
    } catch (UnknownHostException k1)
    {
        logger.error("Usuario: "+EU.usu_nomb+"\nHost: DESCONOCIDO \n"+
                 s+"\n"+systemOut.getMessage() );
    }
//    enviaMailError(s);

    PopError pe= new PopError(s+"\n"+systemOut.getMessage());
    pe.setEjecutable(this);
    pe.Bcancelar.requestFocus();
    pe.setVisible(true);
    return -1;
  }
  /**
   * Rutina de Error Comprueba de que tipo de error se trata
   * y llama al fatalError que controla ese error
   * @param s String Cabecera del error
   * @param t Throwable Excepcion
   * @return boolean si se debe reintentar
   */
  public boolean Error(String s, Throwable t) {
         int resul = 0;
         if (t instanceof SQLException)
            resul = fatalError("[Anjelica] ERROR SQL "+s, (SQLException)t);
         else if (t instanceof Exception)
            resul = fatalError("[Anjelica] ERROR EN PROGRAMA\n" + s, (Exception)t);
         else
             resul = fatalError("ERROR DESCONOCIDO\n" + s, t);
         return ((resul == PopError.REINTENTAR)?true:false);
  }
  /**
   * Rutina de Error en el JbInit envia un mensaje de Error
   */
  protected void ErrorInit(Throwable j)
  {
    j.printStackTrace(systemOut);
    try  {
        logger.error("Usuario: "+EU.usu_nomb+" en host: "+Inet4Address.getLocalHost().getHostAddress()+"\n"+
                 "\n Error en el constructor\n"+systemOut.getMessage() );
    } catch (UnknownHostException k1)
    {
        logger.error("Usuario: "+EU.usu_nomb+"\nHost: DESCONOCIDO \n"+
                 "\n Error en el constructor\n"+systemOut.getMessage() );
    }

//    enviaMailError("ERROR EN EL COSTRUCTOR");

    setErrorInit(true);
  }

  /**
   * Envia un mensaje de Error al Administrador
   */
  public void enviaMailError(String s) {
    try  {
        logger.error("Usuario: "+EU.usu_nomb+" en host: "+Inet4Address.getLocalHost().getHostAddress()+"\n"+
                s );
        if (jf != null)
        {
          jf.ht.clear();
          jf.ht.put("%s",s);
          jf.guardaMens("ME", jf.ht);
        }
    } catch (UnknownHostException k1)
    {
        logger.error("Usuario: "+EU.usu_nomb+"\nHost: DESCONOCIDO \n"+
                 s );
    }

//    sendMail.enviaMailError("[AnJelica] Error en Programa: "+titProg+"\n"+s,systemOut,EU);
  }

  /**
   * Rutinas genericas del interface ejecutable.
   *
   * Normalmente se sustituiran en cada programa.
   */
  public void iniciarVentana() throws Exception
  {
  }

  public void iniciar(int ancho, int alto) throws Exception
  {
    iniciarFrame();
    this.setSize(new Dimension(ancho, alto));
  }

  public void iniciarFrame() 
  {
    this.setTitle(getTitulo());
    this.setResizable(true);
    this.setMaximizable(true);
    this.setIconifiable(true);
  }
  public void matar(boolean cerrarConexion)
  {
    if (muerto)
      return;
    if (statusBar!=null)
    {
      if (!statusBar.salido)
      {
        this.setEnabled(true);
        statusBar.setEnabled(true);
        statusBar.Bsalir.setEnabled(true);
        statusBar.Bsalir.doClick();
        return;
      }
    }
    muerto = true;
    try
    {
      if (popEspere!=null)
        popEspere.setClosed(true);
      if (cerrarConexion)
      {
        if (ct != null)
          ct.close();
        if (ctUp!=null)
          ctUp.close();
      }
    }
    catch (Exception k)
    {
      debug("Error al cerrar la conexion ... pasando del tema");
    }
    try {
      setClosed(true);
    }
    catch (Exception k) {}
    processEvent(new InternalFrameEvent(this,
                                        InternalFrameEvent.
                                        INTERNAL_FRAME_CLOSING));
    return;
  }
  /**
   * Establece si la ventana debe tener una cabecera
   * La cabecera es la parte arriba de la ventana donde se pone el boton minimizar, maximizar, etc.
   * Por defecto esta puesto
   * @param cabecera 
   */
  public void setCabeceraVentana(boolean cabecera)
  {
   this.putClientProperty(
             PlasticInternalFrameUI.IS_PALETTE,
             !cabecera);
  }
  public void matar()
  {
    matar(true);
  }

  public String getNombre()
  {
    return titProg;
  }
  public int getPID()
  {
    return pid;
  }

  public void setPID(int nuevoPID)
    {
        pid=nuevoPID;
    }

    public void setNombre(String nuevoNombre)
    {
      titProg = nuevoNombre;
    }


    /**
    * Escribe un nuevo mensaje.
    */
    public void mensaje(String s)
    {
        if (s.compareTo("")==0)
            mensaje(s,false);
        else
            mensaje(s,true);
    }


    /**
    * Escribe un mensaje en la barra de estado.
    */
    public void mensaje(String  s,boolean t)
    {
    statusBar.setText(s,t);
    }
  
    /**
     * Escribe un mensaje de error en rojo en la barra de estado
     * @param s Mensaje
     * @param t true con sonido
     */
    public void mensajeErr(String  s,boolean t)
    {
      statusBar.setTextErr(s,t);
    }
    public String getMensajeErr()
    {
      return statusBar.getTextErr();
    }
    /**
     * Pone un mensaje en el statusBar que dura 1 segundo y medio
     * NO ejecuta sonido.
     * @param s String Mensaje a poner.
     */
    public void mensajeRapido(String s)
    {
      statusBar.setMsgRapido(s);
    }

    public void mensajeErr(String s)
    {
        if (s==null)
            s="";
        if (s.compareTo("")==0)
            mensajeErr(s,false);
        else
            mensajeErr(s,true);
    }
    public void salirEnabled(boolean b)
    {
        statusBar.setEnabled(b);
    }
  public int getPeso()
  {
    return 10;
  }
  public int getMaxCopias()
  {
    return 10;
  }


  public boolean isMatable()
  {
    return true;
  }

  public boolean getErrorInit()
  {
    return errorInit;
  }

  public void setErrorInit(boolean errInit)
  {
    errorInit=errInit;
  }



  /**
   * Esta funcion es llamada desde la Funcion UtilWindows#salirPantalla
   */
  public void salirHijo() {}
  /**
   * Esta funcion es llamada desde la Funcion UtilWindows#salirPantalla
   * Envia la referencia del hijo
   */
  public void salirHijo(ventana hijo) {}


  public boolean isCloseable() { return (statusBar == null?true:statusBar.Bsalir.isEnabled()); }
    @Override
  public void setClosed(boolean b) throws PropertyVetoException {
    if (isCloseable())
      super.setClosed(b);
  }

  public void msgBox(String msg)
  {
    mensajes.mensajeAviso(msg, this);
  }
  public void msgExplica(String title,String msgAviso )
  {
      mensajes.mensajeExplica(title==null?"AVISO":title,msgAviso);
  }
  /**
   * @deprecated usar creaWhere(String, ArrayList)
   * @param sel
   * @param v
   * @return 
   */
  public  static String creaWhere(String sel, Vector v)
  {
    return creaWhere(sel, new ArrayList(v));
  }
  /**
   * @deprecated usar creaWhere(String, ArrayList,boolean)
   * @param sel
   * @param v
   * @param incWhere Incluir palabra 'where'
   * @return 
   */
  public  static String creaWhere(String sel, Vector v,boolean incWhere)
  {
    return creaWhere(sel, new ArrayList(v), incWhere);
  }
  public  static String creaWhere(String sel, ArrayList<String> v)
  {
    return creaWhere(sel, v, v.size());
  }
  /**
   * Crea condiciones WHERE para una consulta tipo QUERY BY EXAMPLE
   * @param sel String Primera parte de la select
   * Ejemplo: SELECT * FROM usuarios
   * @param v ArrayList  con los diferentes campos de where
   * @param incWhere boolean Si se debe incluir la clausula WHERE en el
   * string devuelto
   * @return String Select ya montada
   */
  public static String creaWhere(String sel, ArrayList<String> v, boolean incWhere)
  {
    return creaWhere(sel, v,v.size(), incWhere);
  }

  public static String creaWhere(String sel, ArrayList<String> v, int nc)
  {
    return creaWhere(sel, v, nc, true);
  }

  /** 
   * Monta las condiciones where de una Select.
   *
   * @param sel String con el principio de la select (SELECT * FROM TABLA)
   * @param v Condiciones a añadir a la clausula WHERE
   * @param nc Numero de Elementos a añadir.
   * @param incWhere Incluir palabra WHERE
   * @return 
   */
  public static String creaWhere(String sel, ArrayList<String> v, int nc, boolean incWhere)
  {
    int n;
    String s = "";
    for (n = 0; n < nc; n++)
    {
      if (v.get(n) == null) // Añadido por si acaso
        continue;
      if (v.get(n).toString().equals(""))
        continue;
      if (s.compareTo("") == 0)
        s = v.get(n).toString();
      else
        s = s + " AND " + v.get(n);

    }
    if (! s.equals("") )
      s = sel + (incWhere?" WHERE ":" AND ") + s;
    else
        s = sel;
    return s;
  }

  public void conecta()  throws SQLException,ClassNotFoundException,IllegalAccessException, InstantiationException,java.text.ParseException
  {
    conecta(EU);
  }

  public void conecta(EntornoUsuario eu) throws SQLException, ClassNotFoundException,
      IllegalAccessException, InstantiationException,java.text.ParseException
  {
//    debug("Iniciando Conexion a Base Datos ");
    ct = new conexion(eu.usuario, eu.password,
                      eu.driverDB,
                      eu.addressDB);
//    if (ct.getDriverType()==conexion.MSQL)
//      ctUp=new conexion(eu.usuario, eu.password,
//                        eu.driverDB,
//                        eu.addressDB);
//    else
//      ctUp=ct;

    ctUp=ct;

    if (System.getProperty("autocommit")!=null)
    {
      if (System.getProperty("autocommit").toUpperCase().equals("TRUE"))
      {
        ctUp.setAutoCommit(true);
      }
      else
      {
        ctUp.setAutoCommit(false);
      }
    }
    else
      ctUp.setAutoCommit(false); // Por defecto es no AUTOCOMMIT

    if (eu.catalog!=null)
    {
      ct.setCatalog(eu.catalog);
      ctUp.setCatalog(eu.catalog);
    }
    dtStat=new DatosTabla(ct);
    dtCon1=new DatosTabla(ct);
    stUp=creaStamento();
  }
  public java.sql.Statement creaStamento() throws SQLException
  {
    return creaStamento(null);
  }
  public java.sql.Statement creaStamento(java.sql.Statement st) throws SQLException
  {
    if (st!=null)
    {
      try { // Intentar Cerrar la conexion
        st.close();
      } catch (Exception k){}
    }
    return ctUp.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                              java.sql.ResultSet.CONCUR_READ_ONLY);
  }

  public void debug(String msg)
  {
    logger.debug(titProg +": "+msg);
  }

  public void guardaTamanoOriginal()
  {
    Dimension d = (Dimension) getSize().clone();
    if (dOriginal == null)
    {
      JMenuItem m = new JMenuItem("Restablecer tamaño original de la pantalla");
      JMenuItem m1 = new JMenuItem("Mostrar Versión");
      m.setIcon(Iconos.getImageIcon("properties"));
      m1.setIcon(Iconos.getImageIcon("status"));
      m.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          setSize(dOriginal);
          validate();
          repaint();
        }
      });

      m1.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JOptionPane.showMessageDialog(ventana.this, getVersion(), "Version del Programa",
                                  JOptionPane.INFORMATION_MESSAGE);
        }
      });

      popMenu.add(m);
      popMenu.add(m1);
      addMouseListener(new MouseAdapter()
      {
                @Override
        public void mouseClicked(MouseEvent e)
        {
          if (e.getModifiers() == MouseEvent.BUTTON1_MASK)
            return;
          popMenu.show(ventana.this, e.getX(), e.getY());
        }
      });
      dOriginal = d;
    }
  }
  public String getVersion()
  {
    return versionID;
  }
  public void setVersion(String version)
  {
    versionID=version;
  }
  public boolean isBloqueado(DatosTabla dt, String tabla, String registro) throws SQLException
  {
    return isBloqueado(dt,tabla,registro,false);
  }
  /**
   * Comprueba si un registro esta bloqueado en una tabla
   * @param dt DatosTabla Para realizar la conexion a la DB
   * @param tabla String Nombre de la TABLA
   * @param registro String Definici�n del registro
   * @param porMi boolean TRUE Devuelve false (no bloqueado) si esta bloqueado por mi mismo.
   * @throws SQLException Error en la DB
   * @return boolean true (bloqueado) false (No bloqueado)
   */
  public boolean isBloqueado(DatosTabla dt, String tabla, String registro,boolean porMi) throws SQLException
  {
    tabla = tabla.toLowerCase();
    if (porMi && tabla.equals(tablaLock) && registro.equals(registroLock))
      return false; // Esta bloqueado por Mi.
    String s = "SELECT * FROM bloqueos WHERE blo_tabla = '" + tabla + "'" +
        " AND blo_regis = '" + registro.trim() + "'";
    if (dt.select(s))
    {
      msgBloqueo = "REGISTRO ESTA SIENDO EDITADO POR: " +
          dt.getString("usu_nomb") +
          "\n El registro se edito el: " +
          dt.getFecha("blo_fecha", "dd-MM-yyyy") + " " +
          dt.getDouble("blo_hora");
      return true;
    }
    else
    {
      msgBloqueo = "Registro NO esta bloqueado";
      return false;
    }
  }

  public boolean setBloqueo(DatosTabla dt, String tabla, String registro) throws SQLException,
      java.net.UnknownHostException
  {
    return setBloqueo(dt, tabla, registro, true);
  }

  public boolean setBloqueo(DatosTabla dt, String tabla, String registro, boolean commit) throws SQLException
  {
    tabla = tabla.toLowerCase();
    if (isBloqueado(dt, tabla, registro))
    {
      msgBloqueo = "REGISTRO ESTA SIENDO EDITADO POR: " + dt.getString("usu_nomb") +
          "\n El registro se edito el: " + dt.getFecha("blo_fecha", "dd-MM-yyyy") + " " + dt.getDouble("blo_hora");
      return false;
    }
    dt.addNew("bloqueos");
    dt.setDato("usu_nomb", EU.usuario);
    try {
      dt.setDato("blo_tty", java.net.InetAddress.getLocalHost().getHostName());
    } catch ( java.net.UnknownHostException k1)
    {
      throw new SQLException("No encontrado Nombre de Localhost "+k1.getMessage());
    }
    dt.setDato("blo_fecha", Fecha.getFechaSys("dd-MM-yyyy"), "dd-MM-yyyy");
    dt.setDato("blo_hora", Fecha.getFechaSys("hh.mm"));
    dt.setDato("blo_tabla", tabla);
    dt.setDato("blo_regis", registro);
    dt.update(dt.getStatement());
    if (commit)
      dt.getStatement().getConnection().commit();
    tablaLock = tabla;
    registroLock = registro;
    return true;
  }

  public boolean resetBloqueo(DatosTabla dt) throws SQLException,
      java.text.ParseException
  {
    if (tablaLock == null || registroLock == null)
    {
      msgBloqueo = "No se bloqueo NINGUN registro";
      return false;
    }
    boolean res = resetBloqueo(dt, tablaLock, registroLock, true);
    tablaLock = null;
    registroLock = null;
    return res;
  }

  public boolean resetBloqueo(DatosTabla dt, String tabla, String registro) throws SQLException,
      java.text.ParseException
  {
    return resetBloqueo(dt, tabla, registro, true);
  }

  public boolean resetBloqueo(DatosTabla dt, String tabla, String registro, boolean commit) throws SQLException
  {
    tabla=tabla.toLowerCase();
    if (!isBloqueado(dt, tabla, registro))
    {
      msgBloqueo = "Registro NO esta bloqueado";
      return false;
    }
    String s = "delete FROM bloqueos WHERE blo_tabla = '" + tabla + "'" +
        " AND blo_regis = '" + registro.trim() + "'";
    java.sql.Statement st = dt.getStatement();
    st.executeUpdate(s);
    if (commit)
      st.getConnection().commit();
    return true;
  }

  private void jbInit() throws Exception
  {
  }
  public Logger getLogger()
  {
    return logger;
  }
  public void aviso(String aviso)
  {
      try  {
        logger.error("Usuario: "+EU.usu_nomb+"en host: "+Inet4Address.getLocalHost().getHostAddress()+"\n"+
                aviso );
    } catch (UnknownHostException k1)
    {
        logger.error("Usuario: "+EU.usu_nomb+"\nHost: DESCONOCIDO \n"+
                 aviso );
    }
  }

  public void aviso(String aviso, Throwable t1)
  {
   try  {
        logger.error("Usuario: "+EU.usu_nomb+"en host: "+Inet4Address.getLocalHost().getHostAddress()+"\n"+
                aviso,t1 );
    } catch (UnknownHostException k1)
    {
        logger.error("Usuario: "+EU.usu_nomb+"\nHost: DESCONOCIDO \n"+
                 aviso,t1 );
    }
  }
  /**
   * Establece que se esta realizando una petición (trabajando)
   * Si es true, se pondrá el cursor como ocupado, la ventana sera puesta a inactiva (disabled)
   * y se mostrara la ventana PopOcupado encima de la actual.
   * Cuando se mande 'false' se pondrá la ventana actual como enabled, restaurando el cursor 
   * a su valor por defecto y poniendo la CInternalFrame PopOcupado invisible.
   * 
   * @param inWork 
   */
  public void setTrabajando(boolean inWork)
  {      
      trabajando = inWork;  
      if (SwingUtilities.isEventDispatchThread())
      {
          setTrabajando_priv();
          return;
      }
      try
      {
          SwingUtilities.invokeAndWait(new Thread()
          {
              @Override
              public void run() {
                  setTrabajando_priv();                  
              }
          });
      } catch (InterruptedException ex)
      {
          java.util.logging.Logger.getLogger(ventana.class.getName()).log(Level.SEVERE, null, ex);
      } catch (InvocationTargetException ex)
      {
          java.util.logging.Logger.getLogger(ventana.class.getName()).log(Level.SEVERE, null, ex);
      }
  }
  
  private void setTrabajando_priv()
  {
     
      Component root;
      
      root=this.getTopLevelAncestor();
      //root=this.getParent();
      this.setEnabled(!trabajando);
      root.setCursor(Cursor.getPredefinedCursor(trabajando?Cursor.WAIT_CURSOR:Cursor.DEFAULT_CURSOR));
      if (trabajando)
      {
          if (popOcupado==null)
            popOcupado=new PopOcupado(this);
          popOcupado.mostrar();
      }
      else
      {
          if (popOcupado!=null)
             popOcupado.oculta();
      }
  }
  
  public boolean getTrabajando()
  {
      return trabajando;
  }
  
  /**
   * Crea una ventana con el mensaje que hayamos puesto.
   * Esta ventana pone al padre disabled y muestra una barra de progreso
   * Se usa cuando se esta lanzando algo en background y se quiere que el usuario
   * vea que se esta trabajando.
   * 
   * @see actualizaMsg
   * @seee resetMsgEspere (Quita la ventana de mensajeEspere)
   * @see popEspere_BCancelaraddActionListener
   * @param msg mensaje
   */
  public void msgEspere(final String msg)
  {     
    if (popEspere == null) {
        creaPopEspere();
    }
    popEspere.setMensaje(msg);
    popEspere.mostrar();
  }

  public void creaPopEspere()
  {
    popEspere = new PopEspere("", this);
    if (vActList!=null)
    {
      for (int n=0;n< vActList.size();n++)
        popEspere.BCancelar_addActionListener((ActionListener) vActList.get(n));
    }
  }
  public void popEspere_BCancelarSetEnabled(final boolean  enabl)
  {
      popEspere.BCancelar.setEnabled(enabl);
  }
  /**
   * Para que llame a alguna función cuando se pulse el boton cancelar.
   * @param actList ActionListener
   */
  public void popEspere_BCancelaraddActionListener(ActionListener actList)
  {
    if (vActList==null)
      vActList=new ArrayList();
    vActList.add(actList);
  }
  
  public PopEspere getPopEspere()
  {
    return popEspere;
  }
/**
 * Actualizar mensaje de msgEspere
 * @param msg Mensaje a poner. Lo añade al que ya hubiera.
 */
  public void actualizaMsg(String msg)
  {
    actualizaMsg(msg, true);
  }
  /**
   * Actualizar mensaje de msgEspere
   *
   * @param msg
   * @param anadir true, añade al mensaje anterior. (no pone salto de linea ni nada)
   */
  public void actualizaMsg(String msg, boolean anadir)
  {
    if (popEspere==null)
      return;
    if (SwingUtilities.isEventDispatchThread())
    {
     popEspere.setMensaje((anadir ? popEspere.getMensaje() : "") + " " + msg);
     return;
    }
    SwingUtilities.invokeLater(new actualizaMsg(popEspere,(anadir ? popEspere.getMensaje() : "") + " " + msg));
  }
  class actualizaMsg  extends Thread
  {
      PopEspere pe;
      String msg;
      public actualizaMsg(PopEspere pe,String msg)
      {
         this.pe=pe;
         this.msg=msg;
      }
        @Override
      public void run() {
        popEspere.setMensaje(msg);
      }
  }
/**
 * Funcion a llamar cuando el proceso que empezo con msgEspere haya terminado
 * Cierra la ventana
 * @see msgEspere
 */
  public void resetMsgEspere()
  {
    if (popEspere==null)
      return;
    popEspere.oculta();
  }
  public PopEspere getMsgEspere()
  {
    return popEspere;
  }
}

 