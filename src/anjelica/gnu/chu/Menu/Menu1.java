package gnu.chu.Menu;

import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.jgoodies.looks.plastic.theme.ExperienceBlue;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.net.*;
import gnu.chu.utilidades.*;
import gnu.chu.controles.*;
import gnu.chu.sql.*;
/**
 *
 * <p>Título: Menu1</p>
 * <p>Descripción: Clase llamada directamente por LoginDB una vez introducido
 *  el usuario y contraseña valido.
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

public class Menu1 extends Principal
{
  BorderLayout borderLayout1 = new BorderLayout();
  public CPanel panelNotas = new CPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
  public CPanel panelProg = new CPanel(new FlowLayout(FlowLayout.LEFT, 2, 2)) {
         public void remove(Component c) {
                super.remove(c);
                if (getComponentCount() == 0) {
                   setPreferredSize(new Dimension(0, 0));
                }
                invalidate();
                revalidate();
                repaint();
                panelSouth.invalidate();
                panelSouth.revalidate();
                panelSouth.repaint();
         }
  };
  public CPanel panelSouth = new CPanel(new BorderLayout());
	/**
	* Es el menu principal
	*/
  MenuPrincipal menu;
  JMenuBar menubar = new JMenuBar();
//  JMenu utilidades = new JMenu("Utilidades");
//  JMenuItem utiSalir = new JMenuItem("Salir del Menu");
  JMenu opciones = new JMenu("Preferencias");
  // JMenu opcPantalla = new JMenu("Entorno");
//  JMenuItem panWindows = new JMenuItem("Windows");
//  JMenuItem panWindowsXP = new JMenuItem("WindowsXP");
//  JMenuItem panMotif = new JMenuItem("Motif");
//  JMenuItem panMetal = new JMenuItem("Metal");
  JMenu herramientas = new JMenu("Herramientas");
  JMenuItem herRecar = new JMenuItem("Recargar Menu");
  CButton visualProgM = new CButton("Programas");
  JMenuItem herLogs = new JMenuItem("Ver Logs");
  JMenuItem herMemoria = new JMenuItem("Memoria Sistema");
  JMenuItem herThread = new JMenuItem("Thread");
  JMenuItem herPrevi = new JMenuItem("Listados");
  JMenuItem herBloq = new JMenuItem("Adm. Bloqueos");
  JMenuItem herEntorno = new JMenuItem("Entorno");

  public Menu1(String usuario, String password, conexion conex) {
    super(usuario, password, conex);
    try {
        jbInit();
    } catch (Throwable j) {
      SystemOut.print(j);
      System.exit(1);
    }
  }

  private void jbInit() throws Throwable
  {
    this.getContentPane().setLayout(borderLayout1);
   
//    utilidades.setMnemonic('U');
//    utiSalir.setMnemonic('S');
//    opcPantalla.setMnemonic('O');
//    panWindows.setMnemonic('W');
//    panMetal.setMnemonic('M');
//    panMotif.setMnemonic('F');
//    panWindowsXP.setMnemonic('X');
    herramientas.setMnemonic('H');
    herRecar.setMnemonic('R');
    visualProgM.setMnemonic('P');
    herLogs.setMnemonic('L');
    herMemoria.setMnemonic('M');
    herThread.setMnemonic('T');
    herPrevi.setMnemonic('P');
    herBloq.setMnemonic('B');
//    utilidades.add(utiSalir);
//    opcPantalla.add(panWindows);
//    opcPantalla.add(panMetal);
//    opcPantalla.add(panMotif);
//    opcPantalla.add(panWindowsXP);
//    opciones.add(opcPantalla);
    herramientas.add(visualProgM);
    herramientas.add(herLogs);
    herramientas.add(herBloq);
    herramientas.addSeparator();
    herramientas.add(herMemoria);
    herramientas.add(herThread);
    herramientas.add(herEntorno);
    herramientas.addSeparator();
    herramientas.add(herRecar);
    opciones.add(herPrevi);
//    menubar.add(utilidades);
    menubar.add(herramientas);
    menubar.add(opciones);
    menubar.add(visualProgM);

    this.getContentPane().add("Center", panel1);
    this.getContentPane().add(panelSouth, BorderLayout.SOUTH);
    panelProg.setOpaque(false);
    panelProg.setMinimumSize(new Dimension(0, 0));
    panelProg.setPreferredSize(new Dimension(0, 0));
    panel1.add(panelNotas);
    panelNotas.setOpaque(false);
    this.getContentPane().add(menubar, BorderLayout.NORTH);
    panelSouth.add(statuBar, BorderLayout.SOUTH);
    panelSouth.add(panelProg, BorderLayout.NORTH);

    panelNotas.setBackground(panel1.getBackground());
    panelSouth.setBackground(panel1.getBackground());

    activarEventos();
    panelDeNotas = panelNotas;

    try
    {                  
        SystemOut sout=new SystemOut(System.out);
        System.setOut(sout);        
        SystemOut serr=new SystemOut(System.err);
        serr.setSalidaError(true);
        System.setErr(serr);
    }
    catch (Throwable k)
    {
      SystemOut.print(k);
      System.out.println("No puedo redirigir la salida ..."+k.getMessage());
    }
    ventana.logger.debug("Iniciado Menu con usuario: "+Usuario.usuario);   
    ht.clear();
    ht.put("%u", Usuario.usuario + " (" + InetAddress.getLocalHost().getHostName() + ")");
    guardaMens("EM", ht);
    afterConecta();
    SwingUtilities.invokeLater(new Thread()
    {
        public void run()
        {
            Plastic3DLookAndFeel.setPlasticTheme(new ExperienceBlue());
            setCambioFormato("com.jgoodies.looks.plastic.PlasticLookAndFeel");
        }
    });
  }

  void afterConecta() throws Exception
  {
    checkEjercicio();

  }

  void checkEjercicio() throws Exception
  {
    String s;
    s="select now() as ahora from v_config ";
    dt1.select(s);
    //int ejerFecha=Integer.parseInt(Formatear.getFechaAct("yyyy"));
    int ejerFecha=Integer.parseInt(Formatear.getFecha(dt1.getDate("ahora"),"yyyy"));
//    ejerFecha=2008;
    if (ejerFecha<=Usuario.ejercicio)
      return;
    s="SELECT * FROM v_config WHERE emp_codi = "+Usuario.em_cod;
    if (! dt1.select(s))
      return;
    if (dt1.getString("cfg_caejau").equals("N"))
      return;
    s="SELECT * FROM usuarios WHERE emp_codi = "+Usuario.em_cod+
        " and eje_nume = "+ejerFecha;
    if (dt1.select(s))
      return; // Hay al menos un usuario con el ejercicio actual. No hago nada
    Usuario.ejercicio=ejerFecha;
    s = "UPDATE usuarios set eje_nume = " + ejerFecha + " where emp_codi = " + Usuario.em_cod;
    dt1.executeUpdate(s);
    dt1.commit();

    s="SELECT emp_codi FROM v_empresa order by emp_codi ";
    if (! dt1.select(s))
      return;
    do
    {
      s = "SELECT * FROM ejercicio where emp_codi = " + dt1.getInt("emp_codi") +
          " and eje_nume = " + ejerFecha;
      if (!dt2.select(s))
      { // Creo el registro de ejercicioE
        dt2.addNew("ejercicio");
        dt2.setDato("eje_nume", ejerFecha);
        dt2.setDato("emp_codi", dt1.getInt("emp_codi"));
        dt2.setDato("eje_fecini", "01-01-" + ejerFecha, "dd-MM-yyyy");
        dt2.setDato("eje_fecfin", "31-12-" + ejerFecha, "dd-MM-yyyy");
        dt2.setDato("eje_cerrad", 0);
        dt2.update();
      }
      s="SELECT * FROM v_numerac WHERE emp_codi = " + dt1.getInt("emp_codi") +
         " and eje_nume = "+ejerFecha;
      if (!dt2.select(s))
      {
        dt2.addNew();
        dt2.addNew("v_numerac");
        dt2.setDato("eje_nume", ejerFecha);
        dt2.setDato("emp_codi", dt1.getInt("emp_codi"));
        dt2.setDato("num_seriea",0);
        dt2.setDato("num_serieb", 0);
        dt2.setDato("num_seriec", 0);
        dt2.setDato("num_seried", 0);
        dt2.setDato("num_seriex", 0);

        dt2.setDato("num_factur", 0);

        dt2.setDato("num_secoma", 0);
        dt2.setDato("num_secomb", 0);
        dt2.setDato("num_secomc", 0);
        dt2.setDato("num_secomd", 0);

        dt2.setDato("num_faccom", 0);
        dt2.setDato("num_pedcom", 0);

        dt2.setDato("num_despi", 0);
        dt2.setDato("num_remesa", 0);
        dt2.update();
      }
    } while (dt1.next());
    dt2.commit();

  }

  protected void conectado() throws Exception
  {
    menu = new MenuPrincipal(MyDb, Usuario, this);
    statuBar = new BarraEstado(menu)
    {
      public void showMenu(Component c)
      {
        Menu1.this.showMenu(c);
      }
    };
  }

  protected void cargaMenu() throws Throwable
  {
    try
    {
      herRecar.setEnabled(false);
      popMenu.removeAll();
      swMenuCargado = false;

      llenaMenu("MENU", popMenu);
      popMenu.updateUI();
      swMenuCargado = true;

      herRecar.setEnabled(true);
    }
    catch (Throwable j)
    {
      if (herRecar != null)
        herRecar.setEnabled(true);
      throw j;
    }
  }
  /**
   * lee el Padre y añade al popmenu
   */
  private void llenaMenu(String padre, JComponent menu) throws Throwable
  {
    // Lee el los Hijos del Padre
    String s = "select * from menus where mnu_usua = '" + Usuario.usuario + "'" +
        " and mnu_padr = '" + padre + "'" +
        " ORDER BY mnu_usua, mnu_nuli";
    if (dtMenu.select(s))
    {
      // ArrayList con todos los Elementos que son Padres
      ArrayList<HashMap> padres = new ArrayList();
      do
      {
        JComponent c = null;
        // El Menu es un padre lo Añade al ArrayList
        // para luego leerlo
        if (dtMenu.getString("mnu_tipo").equals("P"))
        {
          // Crea un Menu
          c = new JMenu(dtMenu.getString("mnu_defi"));
          ( (JMenu) c).setIcon(Iconos.getImageIcon(dtMenu.getString("mnu_icon")));
          Font f = c.getFont().deriveFont(Font.BOLD);
          c.setFont(f);
          
          HashMap ht = new HashMap();
          ht.put("Comp", c);
          ht.put("Acro", dtMenu.getString("mnu_acro"));
          padres.add(ht);
        }
        else
        {
          // Crea un MenuItem
          final String mnu_defi = dtMenu.getString("mnu_defi");
          final String mnu_prog = dtMenu.getString("mnu_prog");
          c = new JMenuItem(mnu_defi);
          ( (JMenuItem) c).setIcon(Iconos.getImageIcon(dtMenu.getString("mnu_icon")));

          // A�ade Orejas para ejecutar el Programa
          ( (JMenuItem) c).addActionListener(new ActionListener()
          {
            public void actionPerformed(ActionEvent e)
            {
              Menu1.this.menu.LanzarPrograma(mnu_defi, mnu_prog);
            }
          });
        }

        if (menu instanceof JPopupMenu)
        {
          ( (JPopupMenu) menu).add(c);
        }
        else if (menu instanceof JMenu)
        {
          ( (JMenu) menu).add(c);
        }
      }  while (dtMenu.next());

      for (int i = 0; i < padres.size(); i++)
      {
        HashMap ht =  padres.get(i);
        llenaMenu(ht.get("Acro").toString(), (JComponent) ht.get("Comp"));
      }
    }
  }
  void generarEventoSalir()
  {
    dispatchEvent(new WindowEvent(Menu1.this, WindowEvent.WINDOW_CLOSING));
  }
  private void activarEventos() {
    /*utiSalir.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        dispatchEvent(new WindowEvent(Menu1.this, WindowEvent.WINDOW_CLOSING));
      }
    });*/
	  this.addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
             panelNotas.setSize(panel1.getSize());
             validate();
             repaint();
      }
      public void componentShown(ComponentEvent e) {
             panelNotas.setSize(panel1.getSize());
             validate();
             repaint();
      }
    });
//	  /**
//	  * Cambia a entorno Windows
//	  */
//	  panWindows.addActionListener(new ActionListener() {
//	  	public void actionPerformed(ActionEvent e) {
//  			setCambioFormato("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//	  	}
//	  });
//      
//	  /**
//	  * Cambia a entorno Motif
//	  */
//	  panMotif.addActionListener(new ActionListener() {
//	  	public void actionPerformed(ActionEvent e) {
//		    setCambioFormato("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
//		  }
//	  });
//	  /**
//	  * Cambia a entorno Metal
//	  */
//	  panMetal.addActionListener(new ActionListener() {
//	  	public void actionPerformed(ActionEvent e) {
//		    setCambioFormato("javax.swing.plaf.metal.MetalLookAndFeel");
//		  }
//	  });
      
       

	  /**
	  * Recarga el Menu
	  */
	  herRecar.addActionListener(new ActionListener() {
	  	public void actionPerformed(ActionEvent e) {
             menu.CargarMenu();

             recargaMenu();
		  }
	  });
	  /**
	  * Visualiza las tareas activas del Gestor
	  * Permite Terminar una tareas
	  */
	  visualProgM.addActionListener(new ActionListener() {
	  	public void actionPerformed(ActionEvent e) {
	  		gestor.ventanaProcesos(panel1);
		  }
	  });
          herLogs.addActionListener(new ActionListener()
          {
            public void actionPerformed(ActionEvent e)
            {
              ejecutar("gnu.chu.logs.MantLogs","Ver Logs");
            }
          });
          herBloq.addActionListener(new ActionListener()
                  {
                    public void actionPerformed(ActionEvent e)
                    {
                      ejecutar("gnu.chu.sql.admBloqueos","Administrar Bloqueos");
                    }
                  });

	  /**
	  * Visualiza la Memoria del Sistema
	  */
	  herMemoria.addActionListener(new ActionListener() {
	  	public void actionPerformed(ActionEvent e) {
	  		gestor.ventanaMemoria(panel1);
		  }
	  });
           herEntorno.addActionListener(new ActionListener() {
	  	public void actionPerformed(ActionEvent e) {
	  		gestor.ventanaEntorno(panel1);
		  }
	  });
	  /**
	  * Visualiza los Threads Lanzados
	  */
	  herThread.addActionListener(new ActionListener() {
	  	public void actionPerformed(ActionEvent e) {
	  		gestor.ventanaThreads(panel1);
		  }
	  });
          herPrevi.addActionListener(new ActionListener() {
                  public void actionPerformed(ActionEvent e) {
                          gestor.ventanaPrevi(panel1,Usuario);
                  }
          });

  }
  protected void windowOpened() {
		// Centra la pantalla del Menu
    Dimension PrincipalSize = panel1.getSize();
    Dimension MenuSize = menu.getSize();
    if (MenuSize.height > PrincipalSize.height)
      MenuSize.height = PrincipalSize.height;
    if (MenuSize.width > PrincipalSize.width)
      MenuSize.width = PrincipalSize.width;
    menu.setLocation((PrincipalSize.width - MenuSize.width) / 2, (PrincipalSize.height - MenuSize.height) / 2);
  }

  /**
   * Visualiza la pantalla de Menu
   */
    @Override
  public void activaMenu() {
        if (menu == null)
          return;
        menu.ponerUsuarioEmp();
	  	  menu.setVisible(true);
		    menu.Eleccion.requestFocus();
        new ThreadSelected(menu);
        menu.toFront();
  }
  protected void ejecutar(String clas, String desc) {
            menu.LanzarPrograma(desc, clas);
  }

}

