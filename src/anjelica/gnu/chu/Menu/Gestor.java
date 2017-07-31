package gnu.chu.Menu;


import gnu.chu.controles.CButton;
import gnu.chu.controles.CInternalFrame;
import gnu.chu.controles.CPanel;
import gnu.chu.controles.Cgrid;
import gnu.chu.interfaces.ejecutable;
import gnu.chu.sql.DatosTabla;
import gnu.chu.sql.conexion;
import gnu.chu.utilidades.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import org.apache.log4j.Logger;
/**
* Gestor de programas. El gestor es el encargado de mantener una estructura de datos con
* los hilos y ventanas lanzados para poder así realizar acciones sobre todos los elementos
* de la aplicación.<p>
* El gestor es también el encargado de lanzar los programas que se han
* seleccionado en el menu. Para ejecutar cada uno de los programas se dispone de 3
* modos de funcionamiento:
* <LI> Directa. A traves de un thread dentro de la VM.
* <LI> Background. Con una nueva VM.
* <LI> Batch. Ejecucion encolada en un fichero de tareas.<p>
* En la ejecución directa el gestor genera un nuevo thread y carga dinámicamente la
* clase desde el fichero seleccionado, añadiendo el subproceso a una tabla de threads
* en ejecucion. La estructura de la tabla es la siguiente:<p>
* | PID | InfoProgram |<p>
* Un programa ejecutado en un thread se identifica mediante un PID único que
* hace posible acabar con su ejecución a través del propio gestor. Para las terminaciones
* anormales o asincronas existe un thread controlador que accede a la tabla periodicamente
* para mantener su consistencia.
* Los programas lanzados directamente a través de un thread sólo son lanzados si el usuario
* dispone de los recursos necesarios (lo que se determina calculando el peso total de los
* procesos en ejecución) y si no ha alcanzado el número máximo de copias simultáneas.
* <p>Para las ventanas e hilos lanzados por el usuario se limita a mantener la coherencia
* de la tabla en todo momento.
* @author Diego Cuesta
* @author chuchi P
* @version 2.01
*/

public class Gestor extends Thread implements Serializable
{
  private int nBotonStatusBar=0;
  CInternalFrame frameProcesos =null;
  public escribe systemOut;

  /////////////////////////////
  // DEFINICI�N DE VARIABLES //
  /////////////////////////////

  /**
  * El hilo actual
  */
  private Thread t;

  /**
  * Grupo de threads lanzados
  */
  public ThreadGroup grupo = new ThreadGroup("Gestor");

  /**
  * Máximo número de entradas en la tabla de procesos.
  */
  private final int MAX_PROCESOS = 100;

  /**
  * Identificador de proceso
  */
  private int PID = -1;

  /**
  * Peso actual de los programas ejecutados por el usuario
  */
  private int pesoActual = 0;

  /**
  * Peso máximo de los programas ejecutados por el usuario
  */
  private final int pesoMaximo = 200;

  /**
  * Tabla de procesos
  */
  public TablaProcesos tabla = new TablaProcesos();

  JLayeredPane vl;
  Principal principal;
  ///////////////////////////
  // FUNCIONES DE UTILIDAD //
  ///////////////////////////

  /**
  * Genera un nuevo gestor e inicializa el PID a 1.
  */
  public Gestor() {
    t = Thread.currentThread();
    PID = 1;
    iniciar();
  }
  public Gestor(JLayeredPane panelApollo, Principal p) {
    this();
    setJLayeredPanel(panelApollo);
    setPrincipal(p);
    iniciar();
  }
  void iniciar()
  {
    systemOut = new escribe(System.out);
    printError pError = new printError(System.out, false);
  }
  public void setJLayeredPanel(JLayeredPane panelApollo) {
    vl = panelApollo;
  }
  public void setPrincipal(Principal p) {
    principal = p;
  }

  /**
  * Ejecuta la clase pasada como parámetro sin hacer ningún tipo de chequeo
  * @param nombreClase nombre de la clase que contiene el ByteCode
  * @param parametros los parámetros necesarios para la ejecución
  */
  public synchronized void ejecutar(String nombreClase, Object[] parametros, JLabel estado)
  {
    try
    {
      // Busca si lleva algun parametro
      StringTokenizer sTo = new StringTokenizer(nombreClase);
      ArrayList<String> param = new ArrayList();
      // Guarda el Nombre de la Class
      nombreClase = sTo.nextToken();

      // Guarda un Array con todos los parametros
      while (sTo.hasMoreTokens())
            param.add(sTo.nextToken());

      if (param.size() > 0) {
         Hashtable ht = new Hashtable();
         for (int i=0;i<param.size();i++) {
             String p = param.get(i);
             int x = p.indexOf('=');
             if (x > 0)
                ht.put(p.substring(0, x), p.substring(x+1));
             else
               ht.put(""+i, p);
         }

         Object[] paramTmp = new Object[parametros.length + 1];
         int pos=0;
         for (Object parametro : parametros)
         {
              paramTmp[pos++] = parametro;
         }
         paramTmp[pos] = ht;
         parametros = paramTmp;
      }
      Class clase = Class.forName(nombreClase);

      // Salta el Proceso si Termina en 1
      String sPID = ""+PID;
      if (sPID.substring(sPID.length()-1).equals("1"))
         PID++;
      if (estado.getText().contains("*DIRECTO*"))
          ejecutaThread(clase,estado,parametros); 
      else  
          javax.swing.SwingUtilities.invokeLater(new Hilo(this, clase, grupo, ""+PID, parametros, estado));
//      Hilo hiloEjecucion =  new Hilo(this, clase, grupo, ""+PID, parametros, estado);
//      hiloEjecucion.start();
      PID++;
    } catch(Exception e) {
      mensajes.mensajeAviso("Error en Programa: " + nombreClase + "\nNo Existe");

      new miThread("Activando Menu ...") {
                @Override
          public void run() {
                 try {
                     sleep(500);
                 } catch (Throwable j) {}
                 activaMenu();
          }
      };

      Container c = estado.getParent();
      c.remove(estado);
      c.validate();
      c.repaint();
      c = c.getParent();
      if (c != null) {
         c.validate();
         c.repaint();
      }      
    } catch(Throwable e)
    {
      mensajes.mensajeAviso("Error en Programa: " + nombreClase + "\nPosiblemente el nombre este mal escrito");
      SystemOut.print(e);
      new miThread("Activando Menu ...") {
                @Override
          public void run() {
                 try {
                     sleep(500);
                 } catch (Throwable j) {}
                 activaMenu();
          }
      };

      Container c = estado.getParent();
      c.remove(estado);
      c.validate();
      c.repaint();
      c = c.getParent();
      if (c != null) {
         c.validate();
         c.repaint();
      }      
    }
  }





  /**
  * Apunta el objeto en la tabla de procesos del sistema, en el caso de que no se
  * pueda ejecutar el proceso la función devolverà falso. Si se puede ejecutar
  * añadira un eventlistener a las ventanas para detectar su finalización.
  * @param objeto el objeto que se pretende ejecutar
  * @return true si el objeto puede ser ejecutado
  */
  public synchronized boolean apuntar(ejecutable miObjeto) {
    return apuntar(miObjeto, true);
  }



  /**
  * Apunta el objeto en la tabla de procesos del sistema, en el caso de que no se
  * pueda ejecutar el proceso la función devolvera falso. Si se puede ejecutar
  * añadira un eventlistener a las ventanas para detectar su finalización.
  * @param miObjeto el objeto que se pretende ejecutar
  * @param chequeo booleano para indicar que se realice chequeo de copias
  * @return true si el objeto puede ser ejecutado
  */
  public synchronized boolean apuntar(ejecutable miObjeto, boolean chequeo) {
    if (principal.dt1.getConexion() == null)
       return true;
     miObjeto.setDuplicado(false);
//    String descripcion = miObjeto.getNombre();
    int result = JOptionPane.YES_OPTION;
    String nombre = miObjeto.getClass().getName();
   // boolean matable = miObjeto.isMatable();
    int peso = miObjeto.getPeso();
    int maxCopias = miObjeto.getMaxCopias();
    int lePID = tabla.nProc;
    Enumeration<InfoProgram> valores = tabla.getProgramInfo();
    InfoProgram elem;
    int cuenta = 1;
    int pidAntiguo=0;
    boolean esLanzable;

    while(valores.hasMoreElements()) {
      elem = valores.nextElement();
      if(elem.getNombre().compareTo(nombre)==0)
      {
        pidAntiguo=elem.getPID();
        cuenta++;
      }
    }

    if (cuenta > maxCopias)
      esLanzable = false;
    else
      esLanzable = ((peso+pesoActual) <= pesoMaximo);

    if(esLanzable)
    {
      if ( (cuenta > 1) && chequeo)
        result = JOptionPane.showConfirmDialog(null, "¿ Quiere una nueva copia?",
                                               "Programa duplicado en ejecución ",
                                               JOptionPane.YES_NO_OPTION,
                                               JOptionPane.QUESTION_MESSAGE);


      if(result == JOptionPane.YES_OPTION)
      {
        pesoActual += peso;
        if(cuenta > 1) // hay mas de una copia en circulación          
          miObjeto.setNombre(miObjeto.getNombre()+" ("+cuenta+")");
        tabla.anadir(new StringBuffer().append(miObjeto.getNombre()), miObjeto,
            new StringBuffer().append(lePID), new StringBuffer().append(nombre));
      }
      else
      {      
        miObjeto.setDuplicado(true);
        tabla.ir(""+pidAntiguo);        
        return false;
      }
   }
   return esLanzable;
  }

  void controlaProg(ejecutable miObjeto)
  {
    try
    {
      if (Class.forName("gnu.chu.controles.CInternalFrame").isAssignableFrom
          (miObjeto.getClass()))
      {
        final CInternalFrame miInternalFrame = (CInternalFrame) miObjeto;
        miInternalFrame.addInternalFrameListener(new SListener(this,
            miInternalFrame, miObjeto));
        miInternalFrame.addInternalFrameListener(new InternalFrameAdapter()
        {
          @Override
          public void internalFrameDeiconified(InternalFrameEvent e)
          {
            miInternalFrame.requestFocus();
            miInternalFrame.transferFocus();
          }
        });
        JButton bprog = new JButton(miObjeto.getAcronimo());
        
        bprog.setHorizontalAlignment(SwingConstants.CENTER);
        
        bprog.setToolTipText(miObjeto.getNombre());
        bprog.addActionListener(new ActionListener()
        {
          @Override
          public void actionPerformed(ActionEvent e)
          {
            ir(miInternalFrame);
          }
        });
        bprog.setMargin(new Insets(0, 0, 0, 0));
        bprog.setMaximumSize(new Dimension(200,20));
        bprog.setMinimumSize(new Dimension(200,20));
        bprog.setPreferredSize(new Dimension(200,20));
        nBotonStatusBar++;
        principal.statuBar.panel2.add(bprog, new GridBagConstraints(nBotonStatusBar, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 2, 0, 0), 0, 0));
      }
    }
    catch (Exception e)
    {
      JOptionPane.showMessageDialog(null, "Error en EventListener", "Error",
                                    JOptionPane.ERROR_MESSAGE);
    }
}
  void setVisible(CInternalFrame prog)
  {
    try {
    Component c[]=vl.getComponents();
    for (int n=0;n<c.length;n++)
    {
      if(c[n]==prog)
      {
        prog.setSelected(!prog.isSelected());
//        prog.requestFocus();
        return;
      }
    }
    prog.setVisible(true);
      prog.setIcon(false);
    } catch (Exception k){SystemOut.print(k);}
  }

  private boolean conectar(SQLException j)
  {
          if (mensajes.mensajePreguntar("ERROR DE BASE DE DATOS\n" + j.getMessage() + "\nDESEA INTENTAR LA CONEXION?", principal) == mensajes.YES) {
             conexion c = principal.dt1.getConexion();
            try{
             c.Conecta();
                principal.dt1 = new DatosTabla(c);
                principal.dt2 = new DatosTabla(c);
                return true;
             } catch (Exception k)
             {
               mensajes.mensajeAviso("Error al Conectar con la Base de Datos\n" +
                                     "Cod. Error: " + j.getErrorCode() +
                                     "  " + j.getMessage(), principal);
             }
          }
          return false;
  }
  /**
  * Borra el objeto de la tabla interna junto con todos sus hijos
  * @param objeto el objeto que hay que borrar
  */
  public void borra(Object objeto){
    ejecutable miObjeto = (ejecutable)objeto;
    pesoActual -= tabla.eliminar(new StringBuffer().append(miObjeto.getPID()));

    activaMenu();
  }

  /**
   * Comprueba cuantos programas quedan activos
   * si no quedan ninguno visualiza la pantalla principal
   */
  public void activaMenu() {
    if (principal != null) {
       Vector v = getProcesosActivos();
       if ( v.isEmpty()) {
          principal.activaMenu();
       }
    }
  }
 public void ventanaEntorno(JLayeredPane p) {
      final CInternalFrame frame = new CInternalFrame("Variables de Entorno");
      frame.setClosable(true);
      Cgrid jt=new Cgrid(2);
      Vector v=new Vector();
      v.add("Variable");
      v.add("Valor");
      jt.setCabecera(v);
      jt.setAnchoColumna(new int[]{150,250});
      jt.setAjustarGrid(true);
      frame.setSize(350,400);
      CPanel panel = new CPanel();
      panel.setLayout(new GridBagLayout());
      frame.getContentPane().add(panel);
      panel.add(jt,new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 0, 0), 0, 0));

      Enumeration en =System.getProperties().propertyNames();
     while (en.hasMoreElements())
     {
        String var=en.nextElement().toString();
         Vector v1=new Vector();
         v1.addElement(var);
         v1.addElement(System.getProperty(var));
         jt.addLinea(v1);
     }

     posicionaComponent(frame, frame.getTitle());
     p.add(frame, 1);
     frame.setResizable(true);
     frame.setVisible(true);
     jt.requestFocusInicio();
 }
  /**
  * Genera una ventana que indica la memoria disponible del sistema
  * @param p el panel sobre el que se va a insertar
  */
  public void ventanaMemoria(JLayeredPane p) {
    final CInternalFrame frame = new CInternalFrame("Memoria del Sistema");
    JProgressBar indicador = new JProgressBar();
    final JLabel label = new JLabel("0");
    final HiloMemoria hilo = new HiloMemoria(grupo, indicador, label);
    frame.setSize(250, 90);

    CPanel panel = new CPanel();
    panel.setLayout(null);
    CButton boton = new CButton("Cerrar");
    boton.setMargin(new Insets(1,1,1,1));
    boton.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
         guardaPosicion(frame, frame.getTitle());

         hilo.matar();
         frame.dispose();
       }
   });

    CButton boton2 = new CButton("GC");
    boton2.setMargin(new Insets(1,1,1,1));
    boton2.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
         System.gc();
       }
   });
   indicador.setBounds(15,10,170,15);
   boton.setBounds(65, 30, 50, 18);
   boton2.setBounds(135, 30, 50, 18);
   label.setBounds(187, 5, 55, 25);
   panel.add(indicador,null);
   panel.add(boton,null);
   panel.add(boton2,null);
   panel.add(label,null);



   hilo.start();
   frame.getContentPane().add(panel);

   posicionaComponent(frame, frame.getTitle());
   p.add(frame, 1);
   frame.setResizable(true);
   frame.setVisible(true);


}
  /**
   * Devuelve un vector con todos los elementos activos
   */
  public Vector getProcesosActivos() {
    Vector v = new Vector();
    Enumeration listaInfo = tabla.getProgramInfo();

    while(listaInfo.hasMoreElements()) {
      InfoProgram s = (InfoProgram)listaInfo.nextElement();
      v.addElement(s);
    }
    return v;
  }
  
  public InfoProgram getInfoPrograma(String clase)
  {
    Enumeration listaInfo = tabla.getProgramInfo();
    while(listaInfo.hasMoreElements()) {
      InfoProgram s = (InfoProgram)listaInfo.nextElement();
      if (s.getNombre().equals(clase))
          return s;
    }
    return null;
  }
  public ejecutable getProceso(String clase)
  {
    ejecutable eje=tabla.getProceso(clase);
    if (eje!=null)
        return eje; // Ya esta el proceso ejecutandose
    try {
        String claseEjec=principal.canExecuteClase(clase,principal.Usuario.usuario);
        if (claseEjec!=null)
        {
            principal.ejecutar(claseEjec, "*DIRECTO*");
        }
        return tabla.getProceso(clase);
    } catch (SQLException k)
    {
        Logger.getRootLogger().error("Error al Buscar Proceso", k);
    }
    return null;
  }
  public void ir(CInternalFrame intFrame)
  {
      tabla.ir(intFrame);
  }
  /**
  * Genera una ventana que contiene la lista de los procesos del sistema
  * junto con dos botones para eliminarlos
  * @param p el panel sobre el que se va a insertar
  */
  public void ventanaProcesos(JLayeredPane p) {
   if (frameProcesos==null)
     frameProcesos = new CInternalFrame("Programas Activos");
   
    
    if (frameProcesos.isVisible())
    {
        frameProcesos.toFront();
        frameProcesos.show();
        return;
    }
    CPanel panel = new CPanel(new BorderLayout());
    CPanel panelBotones = new CPanel(new GridLayout());
    CButton botonMata = new CButton("Kill", Iconos.getImageIcon("bomb"));
    final CButton botonCancela = new CButton("Cancel", Iconos.getImageIcon("cancel"));
    Enumeration listaInfo = tabla.getProgramInfo();

    frameProcesos.setSize(250, 350);
    frameProcesos.setResizable(true);
    botonMata.setToolTipText("Elimina un Proceso");
    botonMata.setMargin(new Insets(0, 0, 0, 0));
    botonCancela.setMargin(new Insets(0, 0, 0, 0));

    InfoProgram elem;
    Vector datos = new Vector();

    // pasamos la información de una lista a un vector
    while(listaInfo.hasMoreElements()) {
      InfoProgram s = (InfoProgram)listaInfo.nextElement();
      StringBuffer s3 = new StringBuffer().append(s.getPID()).append(": ").append(s.getDescripcion());
      datos.addElement(s3);
    }
    final JList lista = new JList(datos);

    botonMata.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
         if (!lista.isSelectionEmpty()) {
           StringBuffer s = (StringBuffer)lista.getSelectedValue();
           String cad = s.toString();
           String prog=cad.substring(cad.indexOf(":")+1);
           cad = cad.substring(0, cad.indexOf(':'));

           tabla.matando(new StringBuffer(cad));

           activaMenu();
//           guardaPosicion(frameProcesos, prog);
           principal.ht.clear();
           principal.ht.put("%u",principal.Usuario.usuario);
           principal.ht.put("%p","*"+prog+"*");
           principal.guardaMens("MP",principal.ht);
           botonCancela.doClick();
         } else
           mensajes.mensajeUrgente("Seleccione un proceso", principal);
       }
    });

    botonCancela.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
              guardaPosicion(frameProcesos, frameProcesos.getTitle());
              frameProcesos.dispose();
              frameProcesos=null;
       }
    });
    lista.addMouseListener(new MouseAdapter() {
            @Override
      public void mouseClicked(MouseEvent e) {
        if (lista.isSelectionEmpty())
            return;
        if (e.getClickCount()>1)
        {
           StringBuffer s = (StringBuffer)lista.getSelectedValue();
           String cad = s.toString();
           String prog=cad.substring(cad.indexOf(":")+1);
           cad = cad.substring(0, cad.indexOf(':'));
           tabla.ir(cad);
        }
      }
    });
    JScrollPane scrollPane = new JScrollPane(lista);

    frameProcesos.getContentPane().add(panel);
    panel.add(scrollPane, BorderLayout.CENTER);
    panel.add(panelBotones, BorderLayout.SOUTH);
    panelBotones.add(botonMata);
    panelBotones.add(botonCancela);

    posicionaComponent(frameProcesos, frameProcesos.getTitle());

    p.add(frameProcesos, 1);
    frameProcesos.setVisible(true);
  }


  /**
  * Genera una ventana con informaci�n acerca de los threads
  * activos del sistema.
  * @param p el panel sobre el que se va a insertar
  */

  public void ventanaThreads (JLayeredPane p) {
    final CInternalFrame frame = new CInternalFrame("Thread Activos");
    CPanel panel = new CPanel(new BorderLayout());
    CButton botonCancela = new CButton("Salir", Iconos.getImageIcon("back"));
    final JTextArea lista = new JTextArea(getListaThread());
    final Thread t = new miThread("Localizando Threads ....") {
            @Override
        public void run() {
               while (true) {
                     try {
                         Thread.sleep(2000);
                     } catch (Throwable j) {}
                     String info = getListaThread();
                     lista.setText(info);
               }
        }
    };


    botonCancela.addActionListener(new ActionListener() {
       @Override
       public void actionPerformed(ActionEvent e) {
              t.stop();
//              t = null;

              guardaPosicion(frame, frame.getTitle());
              frame.dispose();
       }
    });

    frame.setSize(250, 350);
    frame.setResizable(true);
    botonCancela.setMargin(new Insets(0, 0, 0, 0));

    panel.add(new JScrollPane(lista), BorderLayout.CENTER);
    panel.add(botonCancela, BorderLayout.SOUTH);
    frame.getContentPane().add(panel);

    posicionaComponent(frame, frame.getTitle());

    p.add(frame, 1);
    frame.setVisible(true);
  }
  public void ventanaPrevi (JLayeredPane p,EntornoUsuario EU) {
    frPrevisual frame = new frPrevisual(EU,this.principal);
    posicionaComponent(frame, frame.getTitle());
    p.add(frame, 1);
    frame.setVisible(true);

  }
  /**
   * Retorna un String con todos los Thread
   */
  private String getListaThread() {
    try {
        Thread[] listaHilos = new Thread[100];
        int cuenta = Thread.enumerate(listaHilos);
        String info = "";

        String[] s = new String[cuenta];
        for(int i = 0; i < cuenta; i++) {
                s[i] = Formatear.ajusIzq(listaHilos[i].getName(), 50) +
                       " Pri.: " + Formatear.ajusDer(""+listaHilos[i].getPriority(), 2) +
                       " Group: " + listaHilos[i].getThreadGroup().getName();
        }
        Arrays.sort(s);
        for(int i = 0; i < cuenta; i++) {
          info = info + s[i] + '\n';
        }
        return info;
    } catch (Throwable j) {
      return "";
    }
  }
  private void anadirATablaInfo(int pid, InfoProgram info, Thread t) {
    StringBuffer s = new StringBuffer().append(info.getDescripcion());
    StringBuffer s3 = new StringBuffer().append(info.getNombre());
    StringBuffer s2 = new StringBuffer().append(pid);
    tabla.anadir(s, (ejecutable)t, s2, s3 );
  }

/*  public void run() {
    Thread t = Thread.currentThread();
    t.setName("Gestor de Programas");
    t.setPriority(MIN_PRIORITY);
    while(true) {
      try {
        t.sleep(5000);
      }
      catch(Exception e) {
        e.printStackTrace();
      }
    }
  }
 */
  /**
   * Selecciona de la Base de Datos el Programa Indicado
   * y los posiciona respecto a los valores que tenga guardados
     * @param c
     * @param programa
   */
  public void posicionaComponent(Component c, String programa)
  {
  
    int x = 0, y = 0;
    int width = 0, height = 0;
    if (c instanceof ventana)
    {      
        ( (ventana) c).guardaTamanoOriginal();
    }
      
    String s = "select * from xypantalla where emp_codi = " + principal.Usuario.em_cod +
        " and usu_nomb = '" + principal.Usuario.usuario + "' and xyp_prog = '" +
        programa + "'";

    while (true)
    {
      try
      {
        principal.dt1.select(s);
        if (!principal.dt1.getNOREG())
        {
          x = principal.dt1.getInt("xyp_posx");
          y = principal.dt1.getInt("xyp_posy");
          if (principal.dt1.getObject("xyp_with") != null)
          {
            width = principal.dt1.getInt("xyp_with");
            height = principal.dt1.getInt("xyp_heig");
          }
        }
        break;
      }
      catch (SQLException j)
      {
        if (j.getErrorCode()==-1)
         java.util.logging.Logger.getLogger(Gestor.class.getName()).log(Level.SEVERE, "Error al posicionar ventana");
        else
        {
          if (! conectar(j))
            break;
        }
      }
    }
    if (x>principal.getWidth())
        x=0;
    if (y>principal.getHeight())
        y=0;
    if (x+width+30>principal.getWidth())
    {
        x=5;
        if (x+width+30>principal.getWidth())
            width=principal.getWidth()-25;
    }
    if (y+height+105>principal.getHeight())
    {
        y=5;
        if (y+height+105>principal.getHeight())
            height=principal.getHeight()-105;
    }

    if (width != 0)
    {
      c.setBounds(new Rectangle(x, y, width, height));
    }
    else
      c.setLocation(x, y);
  }
  /**
   * Guarda la Posioncion y el tama�o actual del Componente
     * @param c
     * @param programa
   */
  public void guardaPosicion(Component c, String programa)
  {

        int x=c.getLocation().x, y=c.getLocation().y;
        int height=c.getSize().height, width=c.getSize().width;

        String s = "Select * from xypantalla where emp_codi = " + principal.Usuario.em_cod +
                     " and usu_nomb = '" + principal.Usuario.usuario + "' and xyp_prog = '" +
                     programa + "'";
          while (true) {
                try {
                    principal.dt1.select(s);
                    if (principal.dt1.getNOREG())
                       s = "Insert into xypantalla values(" + principal.Usuario.em_cod + ", '" +
                                                               principal.Usuario.usuario + "', '" +
                                                               programa + "', " + x + ", " + y + ", " +
                                                               width + ", " + height + ")";
                                                         else
                        s = "Update xypantalla set xyp_posx = " + x + ", xyp_posy = " + y +
                            ", xyp_with = " + width + ", xyp_heig = " + height +
                            " where emp_codi = " + principal.Usuario.em_cod +
                            " and usu_nomb = '" + principal.Usuario.usuario +
                            "' and xyp_prog = '" + programa + "'";
                    principal.dt1.executeUpdate(s);
                    principal.dt1.commit();
                    break;
                } catch (SQLException j) {
                  if (! conectar(j))
                      break;
                }

          }

  }
  public boolean puedeEjecutarProg(String miClass) {
      // comprueba si la class se debe pedir palabra de paso
      /*
      String s = "Select * from segulanza where emp_codi = " + principal.Usuario.em_cod +
                 " and seg_prog = '" + miClass + "' and (seg_pwd is not null or seg_pwd <> '')";
      try {
          if (principal.dt1.select(s)) {
             LoginDB login = new LoginDB() {
                     protected void cancelar() {
                               Password.setText("");
                               frameProcesos.setVisible(false);
                     }
                     protected void aceptar() {
                       lblAceptar: {
                           try {
                               principal.dt1.first();
                               do {
                                  if (!principal.dt1.getString("usu_nomb").equals("") &&
                                      !principal.dt1.getString("usu_nomb").equals(Usuario.getText().trim()))
                                        continue;

                                  if (principal.dt1.getString("seg_pwd").equals(Password.getText().trim()))
                                     break lblAceptar;

                               } while (principal.dt1.next());

                               mensajes.mensajeAviso("ERROR. NO PUEDE EJECUTAR EL PROGRAMA", principal);
                               cancelar();
                           } catch (Throwable j) {
                             cancelar();
                           }
                       }
                       frameProcesos.setVisible(false);
                     }
             };
             login.Usuario.setText(principal.Usuario.usuario);
             login.Usuario.setEnabled(false);
             login.setVisible(true);
             if (login.Password.getPassword().equals("")) {
                login.dispose();
                return false;
             }
             login.dispose();
          }
      } catch (Exception j) {
              return false;
      }*/
      return true;
  }
 
  void ejecutaThread(Class miClase,JLabel estado,Object[] misParametros)
  {
    if (!puedeEjecutarProg(miClase.getName())) {
       Container c = estado.getParent();
       c.remove(estado);
       c.validate();
       c.repaint();
       c = c.getParent();
       if (c != null) {
          c.validate();
          c.repaint();
       }
       return;
    }

    Class[] tipos = new Class[misParametros.length];
    try {
      for(int i = 0; i < misParametros.length; i++) {
              if (misParametros[i] instanceof Principal)
                 tipos[i] = misParametros[i].getClass().getSuperclass();
              else
                 tipos[i] = misParametros[i].getClass();
      }
    }
    catch(Exception e) {
      JOptionPane.showMessageDialog(vl, "--ERROR NO RECONOCIDO--", "Atención", JOptionPane.ERROR_MESSAGE);
    }
    try {
      Constructor elConstructor = miClase.getConstructor(tipos);
      ejecutable bicho = (ejecutable)elConstructor.newInstance(misParametros);
      if (vl != null) {
        if (!bicho.getErrorInit())
        {
          posicionaComponent(((Component)bicho), miClase.getName());
          ((ejecutable)bicho).setLabelEstado(estado);
          vl.add((Component)bicho);
          ((CInternalFrame)bicho).toFront();
          ((ejecutable)bicho).iniciarVentana();
          if (System.getProperty("maximizada") != null)
          {
            ((CInternalFrame)bicho).setMaximizable(true);
           try
           {
               ((CInternalFrame) bicho).setMaximum(true);               
           } catch (PropertyVetoException ex)
           {
               java.util.logging.Logger.getLogger(Gestor.class.getName()).log(Level.SEVERE, null, ex);
           }
          }
          ((CInternalFrame)bicho).validate();
          try {
            ((CInternalFrame)bicho).setVisible(true);
          } catch (ClassCastException cc)
          { // Ignoro este error.
              Logger.getRootLogger().error("Error al poner visible la clase: "+miClase);
          }
          ((CInternalFrame)bicho).repaint();
          bicho.setAcronimo(principal.getAcronimo());
          controlaProg(bicho);
          // Añadir boton a la barra de estado
//          miGestor.apuntar(bicho);
//          miGestor.ponerBoton(bicho, ((CInternalFrame)bicho).getTitle());
        }
        else
        {
          if (! bicho.isDuplicado())
          {
              mensajes.mensajeAviso("Error al Iniciar Programa");
              borra(bicho);
          }
        }
      }
    }
    catch(IllegalAccessException e) {
      fatalError("No se pudo cargar la clase",e);
    }
    catch(InstantiationException  e) {
      fatalError("Error de instanciación",e);
    }
    catch( NoSuchMethodException e) {
      fatalError("No existe constructor con esos parametros",e);
    }
    catch( SecurityException e) {
      fatalError("Imposible acceder a la información",e);
      java.util.logging.Logger.getLogger(Gestor.class.getName()).log(Level.SEVERE, null, e);
      JOptionPane.showMessageDialog(vl, "Imposible acceder a la información", "Atención", JOptionPane.ERROR_MESSAGE);
    }
    catch( IllegalArgumentException e) {
      fatalError("Error en el número de parametros",e);
    }
    catch( InvocationTargetException e) {
      fatalError("Excepción en el constructor",e);
    }
    catch(Exception e) {
      java.util.logging.Logger.getLogger(Gestor.class.getName()).log(Level.SEVERE, null, e);
      fatalError("Error desconocido",e);
    } finally {
      Container c = estado.getParent();
      c.remove(estado);
      c.validate();
      c.repaint();
      c = c.getParent();
      if (c != null) {
         c.validate();
         c.repaint();
      }
    }
}
  
public void fatalError(String s, Throwable k)
{
//    String msgStack = "";
    SystemOut.print(k);
//    msgStack = systemOut.getMessage();

    PopError pe = new PopError("ERROR AL EJECUTAR UN PROGRAMA\n" + s + "\n" +
                               systemOut.getMessage());
    pe.Bcancelar.requestFocus();
    pe.setVisible(true);
    Logger.getRootLogger().warn(s+"\n"+systemOut.getMessage());
  }
}
///////////////////////
// CLASES DE THREADS //
///////////////////////


/**
* Clase que define cada uno de los hilos que se crean y carga la clase con
* el programa que se necesite en cada momento.
*/
class Hilo extends Thread {
 
  Class miClase;
  Object[] misParametros;
  String sPID;
  int PID;
  Integer aux;
  Gestor miGestor;
  JLabel estado;

  public Hilo(Gestor miGestor, Class clase, ThreadGroup grupo, String identificador, 
      Object[] parametros, JLabel estado) {
    this.miGestor= miGestor;
    this.estado = estado;
    miClase = clase;
    misParametros = parametros;
  }


 @Override
  public void run() 
  {
    miGestor.ejecutaThread(miClase,estado,misParametros);
  }

}
  ////////////////////////////
 //     SALIR LISTENER     //
////////////////////////////
class SListener extends InternalFrameAdapter
{

  Gestor miGestor;
  Component miVentana;
  ejecutable miClase;

  public SListener(Gestor g, Component frame, ejecutable programa) {
    miGestor = g;
    miVentana = frame;
    miClase = programa;
  }
    @Override
  public void internalFrameClosing(InternalFrameEvent e) {

    miGestor.guardaPosicion(miVentana,  miClase.getClass().getName());

    miGestor.borra(miVentana);
    ((ventana)miVentana).dispose();
    ((ventana)miVentana).removeInternalFrameListener(this);
    Component c[]=miGestor.principal.statuBar.panel2.getComponents();
    try
    {
      for (int n = 0; n < c.length; n++)
      {
        if (Class.forName("javax.swing.JButton").isAssignableFrom(c[n].getClass()))
        {
          if (((JButton) c[n]).getText().equals(miClase.getNombre()) || ((JButton) c[n]).getText().equals(miClase.getAcronimo())  )
          {
            miGestor.principal.statuBar.panel2.remove(c[n]);
            miGestor.principal.statuBar.validate();
            miGestor.principal.statuBar.repaint();
            return;
          }
        }
      }
    } catch (Exception k)
    {
      SystemOut.print(k);
    }
  }
    @Override
  public void internalFrameDeiconified(InternalFrameEvent e) {
    try
    {
      ( (JInternalFrame) miClase).requestFocus();
      ( (JInternalFrame) miClase).setSelected(true);
    }
    catch (Exception k)
    {
     SystemOut.print(k);
    }

  }
    @Override
  public void internalFrameIconified(InternalFrameEvent e) {
      try
      {
        ( (JInternalFrame) miClase).setVisible(false);
      }
      catch (Exception k)
      {
       SystemOut.print(k);
      }
  }
}
///////////////////////
// THREAD DE MEMORIA //
///////////////////////

class HiloMemoria extends Thread 
{

  Runtime r = Runtime.getRuntime();
  JProgressBar miBar;
  JLabel lab;

  boolean swVivo = true;

  public HiloMemoria(ThreadGroup grupo, JProgressBar bar, JLabel label) {
    super(grupo, "Memoria del Sistema");
    miBar = bar;
    lab = label;
  }

  public void matar() {
         swVivo = false;
  }

    @Override
  public void run() {
    miBar.setMaximum(100);
    while (swVivo) {
      long ocupado = r.totalMemory() - r.freeMemory();
      double memoria = ((double)r.totalMemory())/1000000;
      double d = (double)ocupado / r.totalMemory();
      miBar.setValue( (int)(d * 100));

      lab.setText(Formatear.format(memoria, "#,###,##9.99").trim() + " Mb.");
      try{
        HiloMemoria.sleep(2000);
      } catch(Exception e) {}
    }
  }
 }
/////////////////////////
// THREAD DE EJECUCION //
/////////////////////////

class HiloEjecucion extends Thread {

 String param;

  public HiloEjecucion(ThreadGroup grupo, String s) {
    super(grupo, "Nueva VM");
    param = s;
  }

    @Override
  public void run() {
   try {
      Runtime r = Runtime.getRuntime();
      r.exec("java "+param);
    }
    catch(Exception e) {
      JOptionPane.showMessageDialog(null, "Error de Runtime", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }
}


