package gnu.chu.Menu;

import gnu.chu.anjelica.despiece.ValDespi;
import gnu.chu.anjelica.pad.pdusua;
import java.util.*;
import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import gnu.chu.utilidades.*;
import gnu.chu.controles.*;
import gnu.chu.sql.*;
import java.beans.PropertyVetoException;
import java.util.logging.Level;
import java.util.logging.Logger;
/* 
 * <p>Copyright: Copyright (c) 2005-2009
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los terminos de la Licencia P�blica General de GNU seg�n es publicada por
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
*/
public class MenuPrincipal extends CInternalFrame
{
  CPanel panel1 = new CPanel();
  CPanel panel2 = new CPanel();
  CPanel panel3 = new CPanel();
  CPanel panel4 = new CPanel();
  BevelBorder borde1 = new BevelBorder(BevelBorder.RAISED);
  BevelBorder borde2 = new BevelBorder(BevelBorder.LOWERED);

  CLabel lblEmpresa = new CLabel(" Empresa: ");
  CLabel lblUsuario = new CLabel(" Usuario: ");
  CLabel lblEleccion = new CLabel(" Elección: ");
  CLabel lblEjercicio = new CLabel(" Ejercicio: ");
  public CLabel Empresa = new CLabel("");
  public CComboBox ejercicioE = new CComboBox();
  public CLabel Usuario = new CLabel("");
  CTextField Eleccion = new CTextField();
  CButton Anterior = new CButton("Menu Anterior", Iconos.getImageIcon("previous")) {
    public boolean isFocusTraversable() { return false; }
  };
  CButton Inicial = new CButton("Menu Inicial", Iconos.getImageIcon("up")) {
    public boolean isFocusTraversable() { return false; }
  };
  CButton Ocultar = new CButton("Ocultar Menu", Iconos.getImageIcon("fileclose")) {
    public boolean isFocusTraversable() { return false; }
  };

  static final int nLineas = 24;
  int wi = 0;
  int wt = 0;

  CLabel[] mnu_nuli = new CLabel[nLineas];
  CButton[] mnu_bton = new CButton[nLineas];
  CLabel[] mnu_acro = new CLabel[nLineas];
  CLabel[] mnu_defi = new CLabel[nLineas];

  conexion BaseDatos;
  DatosTabla Query;
  EntornoUsuario EntornoUsu;
  Menu1 frmPrincipal;

  String comandOld = "MENU";
    /**
	*
        * Indica el SubMenu actual
    */
  String SubMenu = "MENU";
    /**
	* Indica el Menu anterior
    */
  String MenuAnterior = "MENU";
    /**
	*  Contiene la lineas del Menu actual
    */
  Object LineaMenu[][]= new Object[nLineas][12];

  Font font;

  int btnActivo=0;

  private String menuUsua = "";
  private boolean swLst = false;
  /**
  *	Constructor
  * @param a > Conexion con la base de datos
  * @param b > Parametros del ususrio
  * @param c > VFrame donde se tiene que ejecutar
  */
  public MenuPrincipal(conexion a, EntornoUsuario b, Menu1 c) {
    try
    {
      BaseDatos = a;
      Query = new DatosTabla(BaseDatos);
      EntornoUsu = b;
      frmPrincipal = c;

      jbInit();
      frmPrincipal.panel1.add(this, new Integer(10));
    }
    catch (Exception e)
    {
      mensajes.mensajeUrgente(e.getMessage(), this);
      return;
    }
  }

  private boolean conectar(SQLException j) {
          SystemOut.print(j);
          if (mensajes.mensajePreguntar("ERROR DE BASE DE DATOS\n" + j.getMessage() + "\nDESEA INTENTAR LA CONEXION?", frmPrincipal) == mensajes.YES) {
             conexion c = frmPrincipal.dt1.getConexion();
             try {
                 c.close();
             } catch (Throwable k){}
             try {
             c.Conecta();
             frmPrincipal.dt1 = new DatosTabla(c);
                frmPrincipal.dt2 = new DatosTabla(c);
                Query = new DatosTabla(c);
                return true;
             } catch (Exception k)
             {
             }
             mensajes.mensajeAviso("Error al Conectar con la Base de Datos\n" +
                                   "Cod. Error: " +j.getErrorCode() + "  " +j.getMessage(), frmPrincipal);
          }
          return false;
  }

  //Component initialization

  private void jbInit() throws Exception{
    menuUsua = EntornoUsu.usuario;
    frmPrincipal.gestor.start();

    this.setSize(new Dimension(640, 450));

    this.getContentPane().setLayout(null);
    this.setTitle("Menu Inicial");
    this.setClosable(false);
    this.setMaximizable(false);
    this.setIconifiable(true);
    this.setResizable(false);

    panel2.setEscButton(Ocultar);
    panel2.setBounds(new Rectangle(0, 377, 634, 50));

    if (System.getProperty("os.name").toUpperCase().indexOf("WIN") != -1)
      font = new Font("", font.BOLD, 11);
    else
      font = new Font("", 1, 9);

    lblEmpresa.setForeground(Color.blue);
    lblEmpresa.setBounds(new Rectangle(9, 2, 60, 20));
    lblEjercicio.setForeground(Color.blue);
    lblEjercicio.setBounds(new Rectangle(325, 2, 60, 20));
    lblUsuario.setForeground(Color.blue);
    lblUsuario.setBounds(new Rectangle(507, 2, 55, 20));
    Empresa.setForeground(Color.blue);
    Query.select("select eje_nume from ejercicio where eje_cerrad=0 and emp_codi = "+EntornoUsu.em_cod+" ORDER BY eje_nume");
    ejercicioE.setDatos(Query);
  //  Usuario.setHorizontalAlignment(4);
    Usuario.setForeground(Color.blue);

    Anterior.setBounds(new Rectangle(12, 22, 150, 25));
    Anterior.setMnemonic('A');
    Inicial.setBounds(new Rectangle(244, 22, 150, 25));
    Inicial.setMnemonic('I');
    Ocultar.setBounds(new Rectangle(476, 22, 150, 25));
    Ocultar.setMnemonic('O');

    panel1.setLayout(null);
    panel2.setLayout(null);
    panel3.setLayout(null);
    panel4.setLayout(null );

    Eleccion.setMayusc(false);
    Eleccion.setBounds(new Rectangle(77, 3, 550, 17));

// PONE QUE el ENTER actue igul que el TABULADOR
    HashSet hs = new HashSet();
    panel4.setBounds(new Rectangle(317, 26, 317, 350));
    panel3.setBounds(new Rectangle(0, 26, 317, 350));
    panel1.setBounds(new Rectangle(0, 1, 634, 25));
    lblEleccion.setToolTipText("");
    lblEleccion.setText("Elección: ");
    lblEleccion.setBounds(new Rectangle(12, 3, 60, 17));
    hs.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_TAB, 0));
    this.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, hs);
    Anterior.setMargin(new Insets(0,0,0,0));
    Inicial.setMargin(new Insets(0,0,0,0));
    Ocultar.setMargin(new Insets(0,0,0,0));
    Usuario.setBounds(new Rectangle(554, 2, 66, 20));
    ejercicioE.setBounds(new Rectangle(378, 2, 76, 20));
    Empresa.setBounds(new Rectangle(68, 2, 236, 20));
    panel1.add(lblEmpresa, null);
    panel1.add(Empresa, null);
    panel1.add(lblEjercicio, null);
    panel1.add(ejercicioE, null);
    panel1.add(lblUsuario, null);
    panel1.add(Usuario, null);
    panel2.add(lblEleccion, null);
    panel2.add(Eleccion, null);
    panel2.add(Anterior, null);
    panel2.add(Inicial, null);
    panel2.add(Ocultar, null);

    panel1.setBorder(borde1);
    panel2.setBorder(borde1);
    panel3.setBorder(borde1);
    panel4.setBorder(borde1);

    CPanel panel;
    wt = 0;
    if (System.getProperty("os.name").toUpperCase().indexOf("WIN") != -1)
      font = new Font("", font.BOLD, 11);
    else
      font = new Font("", 0, 11);

    for (wi = 0; wi < nLineas; wi++)
    {
      mnu_nuli[wi] = new CLabel();
      mnu_bton[wi] = new CButton();
      mnu_acro[wi] = new CLabel();
      mnu_defi[wi] = new CLabel();

      mnu_nuli[wi].setVisible(false);
      mnu_bton[wi].setVisible(false);
      mnu_acro[wi].setVisible(false);
      mnu_defi[wi].setVisible(false);

      mnu_acro[wi].setBorder(borde2);
      if (wi == 12)
        wt = 0;
      if (wi < 12)
        panel = panel3;
      else
        panel = panel4;
      mnu_nuli[wi].setBounds(5, (28 * wt) + 8, 20, 20);
      mnu_bton[wi].setBounds(20, (28 * wt) + 4, 28, 28);
      mnu_acro[wi].setBounds(52, (28 * wt) + 8, 70, 20);
      mnu_defi[wi].setBounds(124, (28 * wt) + 8, 186, 20);

      panel.add(mnu_nuli[wi], null);
      panel.add(mnu_bton[wi], null);
      panel.add(mnu_acro[wi], null);
      panel.add(mnu_defi[wi], null);

      mnu_bton[wi].addActionListener(new botonListener(this, wi));
      mnu_nuli[wi].addMouseListener(new labelListener(this, wi));
      mnu_acro[wi].addMouseListener(new labelListener(this, wi));
      mnu_defi[wi].addMouseListener(new labelListener(this, wi));
      mnu_bton[wi].addFocusListener(new focoListener(this, wi));
      mnu_bton[wi].addKeyListener(new teclaListener(this, wi));
      wt++;
    }

    this.getContentPane().add(panel1, null);
    this.getContentPane().add(panel2, null);
    this.getContentPane().add(panel3, null);
    this.getContentPane().add(panel4, null);

    CargarMenu();
    ponerUsuarioEmp();
    activarEventos();
    SwingUtilities.invokeLater(new Thread(){
      public void run()
          {
            try
            {
              MenuPrincipal.this.setSelected(true);
              SwingUtilities.invokeLater(new Thread(){
                public void run()
                {
                  Eleccion.requestFocus();
                }
              });
            }
            catch (PropertyVetoException k)
            {
              SystemOut.print(k);
            }
      }
    });
   
  }

  private void activarEventos() {
/*    this.addAncestorListener(new AncestorListener() {
      public void ancestorAdded(AncestorEvent event) {
        cambiaColor(0, btnActivo);
        Eleccion.requestFocus();
      }
      public void ancestorRemoved(AncestorEvent event){}
      public void ancestorMoved(AncestorEvent event) {}
    });
 */
    this.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
         public void internalFrameIconified(InternalFrameEvent e) {
                try{
                setIcon(false);
                } catch (Exception j){}
                setVisible(false);
         }
    });
    Anterior.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        SubMenu = MenuAnterior;
        CargarMenu();
      }
    });
    Inicial.addActionListener(new ActionListener() {
            @Override
      public void actionPerformed(ActionEvent e) {
        SubMenu = "MENU";
        CargarMenu();
      }
    });
    Ocultar.addActionListener(new ActionListener() {
            @Override
      public void actionPerformed(ActionEvent e) {
          setVisible(false);
        }
    });
    Eleccion.addActionListener(new ActionListener() {
            @Override
      public void actionPerformed(ActionEvent e) {
        if (Eleccion.getText().trim().compareTo("") == 0) {
          mnu_bton[btnActivo].doClick();
          return;
        }

        int btn=0;
        try {
                if (Formatear.StrToInt(Eleccion.getText()) >= 1 &&
                      Formatear.StrToInt(Eleccion.getText()) <= 24) {
                    btn = Formatear.StrToInt(Eleccion.getText()) - 1;
                    if (LineaMenu[btn][0] != null)
                        Ejecutar(btn);
                }
            } catch (Exception j) {
          String prog = Eleccion.getText().trim();
          String param = "";

          if (frmPrincipal.puedeEjecutarParam()) {
             StringTokenizer sTo = new StringTokenizer(prog);
             prog = sTo.nextToken();
             while (sTo.hasMoreTokens()) {
                param += sTo.nextToken() + " ";
             }
             param = param.trim();
          }


          if (!prog.trim().startsWith("$") && !prog.startsWith("#") && prog.length() <= 8)
             EjecutarAcro(prog, param);
          else if (frmPrincipal.puedeEjecutarParam()) {
             comandOld = Eleccion.getText().trim();
             LanzarPrograma(comandOld, comandOld);
          }

            } finally {
                Eleccion.setText("");
            }
        }
    });
    Eleccion.addKeyListener(new KeyAdapter() {
            @Override
      public void keyPressed(KeyEvent e) {
        teclas(e);
      }
    });
  }
  void teclas(KeyEvent e) {
    int b = btnActivo;
    cambiaColor(1, btnActivo);
    switch (e.getKeyCode()) {
      case KeyEvent.VK_DOWN:
        do {
          b++;
          if (b == nLineas)
            b=0;
        } while (!mnu_bton[b].isVisible());
        break;
      case KeyEvent.VK_UP:
        do {
          b--;
          if (b == -1)
            b=nLineas-1;
        } while (!mnu_bton[b].isVisible());
        break;
      case KeyEvent.VK_LEFT:
      case KeyEvent.VK_RIGHT:
        if (!Eleccion.getText().equals(""))
           break;
        int s = nLineas/2;
        if (b >= s)
          if (mnu_bton[b-s].isVisible())
            b=b-s;
          else
            b=b;
        else
          if (mnu_bton[b+s].isVisible())
            b=b+s;
          else
            b=b;
        break;
      case KeyEvent.VK_BACK_SPACE:
        if (Eleccion.getText().compareTo("") == 0)
          Anterior.doClick();
        Eleccion.requestFocus();
        break;
      case KeyEvent.VK_HOME:
        if (Eleccion.getText().compareTo("") == 0)
          Inicial.doClick();
        Eleccion.requestFocus();
        break;
      case KeyEvent.VK_F8:
           Eleccion.setText(comandOld);
           break;
//      case KeyEvent.VK_F10:
//           if (e.getModifiers() == KeyEvent.ALT_MASK)
//              frmPrincipal.utiSalir.doClick();
//           break;
      case KeyEvent.VK_F12:
           if (e.getModifiers() != KeyEvent.ALT_MASK + KeyEvent.CTRL_MASK)
              return;
           if (!frmPrincipal.puedeEjecutarParam())
              return;
           swLst = false;
           cambiaMenu();
           break;
    }
//    mnu_bton[b].requestFocus();
    btnActivo = b;
    cambiaColor(0, btnActivo);
  }

  private void cambiaMenu() {
         String s = "Select usu_nomb from usuarios ORDER BY usu_nomb";
          String[] lst = null;
          try {
              if (swLst) {
                  if (frmPrincipal.dt1.select(s)) {
                     Vector v = gnu.chu.isql.utilSql.dtToVLike(frmPrincipal.dt1);
                     lst = new String[v.size()];
                     for (int i=0;i<v.size();i++) {
                         vlike lk = (vlike)v.elementAt(i);
                         lst[i] = lk.getString("usu_nomb");
                     }
                  }
              }
          } catch (Throwable j) {
            lst = null;
          }
          cambiaMenu(mensajes.mensajeGetTexto2("Usuario: ", "Introduzca el Usuario que desea Visualizar", this, EntornoUsu.usuario, lst));

  }
  private void cambiaMenu(String usu) {
          menuUsua = usu;
          if (menuUsua == null || menuUsua.equals(""))
              menuUsua = EntornoUsu.usuario;
          Inicial.doClick();
  }
  /**
  *	Funcion que visualiza y carga en memoria el submenu actual
  */
 public void CargarMenu()
 {
   Thread.currentThread().setPriority(8);
   ImageIcon[] mnu_icon = new ImageIcon[nLineas];
   ImageIcon icon = new ImageIcon("");

   // Limpia el SubMenu Actual
   for (wi = 0; wi < nLineas; wi++)
   {
     for (wt = 0; wt < 12; wt++)
     {
       LineaMenu[wi][wt] = null;
     }
     mnu_nuli[wi].setVisible(false);
     mnu_bton[wi].setVisible(false);
     mnu_acro[wi].setVisible(false);
     mnu_defi[wi].setVisible(false);

     mnu_bton[wi].setIcon(icon);
     mnu_acro[wi].setForeground(lblEleccion.getForeground());
   }

   // Recoge el Menu Anterior
   String Selec = "select * from menus where mnu_usua = '" + menuUsua
       + "' and mnu_acro = '" + SubMenu + "'";
   while (true)
   {
     try
     {
       if (Query.select(Selec, false))
       {
         MenuAnterior = Query.getString("mnu_padr");
         this.setTitle(Query.getString("mnu_defi",true) + "     (" +
                       SubMenu.trim() + ")");
       }
       else
       {
         MenuAnterior = "MENU";
         this.setTitle("Menu Inicial");
       }
       break;
     }
     catch (SQLException j)
     {
       if (conectar(j))
         continue;
       else
         break;
     }


   }

   // Carga  el SubMenu
   Selec = "select * from menus where mnu_usua = '" + menuUsua
       + "' and mnu_padr = '" + SubMenu + "' order by mnu_nuli";
   while (true)
   {
     try
     {
       if (!Query.select(Selec, false))
         return;
       do
       {
         for (wi = 1; wi <= Query.getNumCol(); wi++)
         {
           LineaMenu[Formatear.StrToInt(Query.getString("mnu_nuli")) - 1]
               [wi-1] = Query.getString(wi);
         }
       }  while (Query.next());
       break;
     }
     catch (SQLException j)
     {
       if (conectar(j))
         continue;
       else
         break;
     }

   }

   // Visualiza el SubMenu
   for (wi = 0; wi < nLineas; wi++)
   {
     if (LineaMenu[wi][0] != null)
     {
       mnu_nuli[wi].setVisible(true);
       mnu_bton[wi].setVisible(true);
       mnu_acro[wi].setVisible(true);
       mnu_defi[wi].setVisible(true);

       if (LineaMenu[wi][7] != null ||
           !LineaMenu[wi][7].toString().trim().equals(""))
       {
         mnu_icon[wi] = Iconos.getImageIcon(LineaMenu[wi][7].toString().trim());
         mnu_bton[wi].setIcon(mnu_icon[wi]);
       }

       mnu_nuli[wi].setText(LineaMenu[wi][3].toString().trim());
       mnu_acro[wi].setText(LineaMenu[wi][1].toString().trim());
       mnu_defi[wi].setText(LineaMenu[wi][4].toString().trim());

       if (LineaMenu[wi][5].toString().compareTo("P") == 0)
       {
         mnu_acro[wi].setForeground(Color.blue);
       }
     }
   }
   this.setVisible(false);
   this.setVisible(true);
   Eleccion.requestFocus();

   cambiaBoton(btnActivo);
 }

    /**
	*                                                                                                    	*  *  	*	Ejecuta un programa
    * @param pulsado > boton pulsado
    */
  public void Ejecutar(int pulsado) {
    if (LineaMenu[pulsado][0] != null) {
        if (LineaMenu[pulsado][5].toString().compareTo("P") == 0) {
            MenuAnterior = SubMenu;
            SubMenu = LineaMenu[pulsado][1].toString().trim();

            CargarMenu();
        } else {
            if (LineaMenu[pulsado][5].toString().compareTo("H") == 0) {
                String desc = LineaMenu[pulsado][4].toString();
                String clas = LineaMenu[pulsado][6].toString();
                comandOld = LineaMenu[pulsado][1].toString().trim();
                LanzarPrograma(desc, clas);
            } else {
                    return;
            }
        }
      }
  }

    /**
	*                                                                                                    	*  *  	*	Ejecuta el programa que se envia como parametro
    * @param acro > Acronimo del programa
    */
  public void EjecutarAcro(String acro, String param) {
    if (acro.toUpperCase().compareTo("MENU") == 0) {
      comandOld = acro.trim();
        MenuAnterior = "MENU";
        SubMenu = "MENU";

        CargarMenu();
        return;
    }

    String Selec = "select * from menus where mnu_usua = '" + menuUsua
             + "' and mnu_acro = '" + acro.toUpperCase().trim() + "'";
    while (true) {
          try {
                if (!Query.select(Selec, false)) {
                 Selec = "select * from menus where mnu_usua = '" + menuUsua
                           + "' and mnu_acro = '" + acro.trim() + "'";
                   if (!Query.select(Selec))
                    return;
                }
                comandOld = Query.getString("mnu_acro");
                if (Query.getString("mnu_tipo",true).equals("P"))
                {
                    MenuAnterior = Query.getString("mnu_padr",true);
                    SubMenu = Query.getString("mnu_acro",true);

                    CargarMenu();
                } else {
                    if (Query.getDatos("mnu_tipo").toString().compareTo("H") == 0) {
                        String desc = Query.getDatos("mnu_defi").toString();
                        String clas = Query.getDatos("mnu_prog").toString();
                        LanzarPrograma(desc, clas + " " + param);//, ncop, peso, teje, kill);
                    } else {
                            return;
                    }
                }
                break;
          } catch (SQLException j) {
            if (conectar(j))
               continue;
            else
                break;
          }


    }
  }
  
  /**
  * Llama al Gestor para ejecutar un programa
  * @param desc  Descripción del programa
  * @param clas Nombre de clase a ejecutar
  */
  public void LanzarPrograma(String desc, final String clas)
  {
    try {
      // ejecuta una aplicacion externa
      if (clas.trim().startsWith("$")) 
      {
        if (! pdusua.canEjecutarProgExt(EntornoUsu.usuario,frmPrincipal.dt1))
          return;
        swLst = (clas.trim().equals("$"));
        String usu = clas.replace('$', ' ');
        if (!usu.trim().equals(""))
           cambiaMenu(usu.trim());
        else
           cambiaMenu();
        return;
      }
      else   if (clas.startsWith("#"))
      {
         new miThread("Ejecutando " + clas.substring(1) + " ...") {
             @Override
             public void run() {
                    try {
                        Runtime.getRuntime().exec(clas.substring(1));
                    } catch (Throwable j) {
                      mensajes.mensajeAviso("Error al Ejecutar programa " + clas.substring(1) + "\n" + j.getMessage());
                    }
                    Eleccion.setText("");
             }
         };
         return;
      }

      Object[] a = {EntornoUsu, frmPrincipal};

      CLabel estado = new CLabel("  CARGANDO: " + desc.trim() + ". ESPERE ...");
      estado.setOpaque(true);
      estado.setBackground(Color.yellow);
      estado.setForeground(Color.blue);
      frmPrincipal.panelProg.add(estado);
      frmPrincipal.panelProg.setPreferredSize(new Dimension(17, 17));
      frmPrincipal.panelProg.invalidate();
      frmPrincipal.panelProg.revalidate();
      frmPrincipal.panelProg.repaint();
      frmPrincipal.gestor.ejecutar(clas.trim(), a, estado); // Directa
//	  	frmPrincipal.gestor.ejecutarEnVM(clas.trim()); // En Virtual Machine
//	  	frmPrincipal.gestor.ejecutarEnCola(clas.trim(), "Nombre de Fichero"); // En Cola de Diego
        this.setVisible(false);
      }
      catch(Exception e){ 
          Logger.getLogger(MenuPrincipal.class.getName()).log(Level.SEVERE, null, e);
      }
  }
  
 
  public void ponerUsuarioEmp() {

    Empresa.setText(EntornoUsu.em_cod + " - " + EntornoUsu.empresa);
    try {
      if (EntornoUsu.catalog != null)
        Empresa.setText(Empresa.getText() + " (" + BaseDatos.getCatalog() + ")");
    } catch (SQLException k)
    {
      SystemOut.print(k);
    }
    ejercicioE.setValor(EntornoUsu.ejercicio);
    ejercicioE.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        EntornoUsu.ejercicio = ejercicioE.getValorInt();
      }
    });
    Usuario.setText(EntornoUsu.usuario);
  }
  /**
   * Tipo = 0 gana el foco
   */
  void cambiaColor(int tipo, int btn) {
    Color forIni = lblEleccion.getForeground();;
    Color bckIni = mnu_bton[btn].getBackground();;
    Color bckFoc1 = Color.orange;
    Color bckFoc2 = Color.blue;

    if (tipo==0) {  // focusGained
      mnu_nuli[btn].setForeground(bckFoc2);
      mnu_acro[btn].setBackground(bckFoc1);
      mnu_defi[btn].setForeground(bckFoc2);
    } else {
      mnu_nuli[btn].setForeground(forIni);
      mnu_acro[btn].setBackground(bckIni);
      mnu_defi[btn].setForeground(forIni);
    }
    repaint();
    validate();
  }
  void cambiaBoton(int btnNuevo) {
    int repeticiones=0;
    cambiaColor(1, btnActivo);
    btnActivo = btnNuevo;
    while (!mnu_bton[btnActivo].isVisible()) {
      btnActivo++;
      if (btnActivo == nLineas) {
        btnActivo=0;
        repeticiones++;
      }
      if (repeticiones == 2)
        return;
    }
    cambiaColor(0, btnActivo);
  }
}

class botonListener implements ActionListener {
  MenuPrincipal m;
  int btn=0;

  public botonListener(MenuPrincipal mp, int boton) {
    m = mp;
    btn = boton;
  }
  public void actionPerformed(ActionEvent e) {
    m.Ejecutar(btn);
  }
}
class labelListener extends MouseAdapter {
  MenuPrincipal m;
  int btn=0;

  public labelListener(MenuPrincipal mp, int boton) {
    m = mp;
    btn = boton;
  }
  public void mouseClicked(MouseEvent e) {
    m.mnu_bton[btn].doClick();
  }
}
class focoListener extends FocusAdapter {
  MenuPrincipal m;
  int btn=0;

  public focoListener(MenuPrincipal mp, int boton) {
    m = mp;
    btn = boton;
  }
  public void focusGained(FocusEvent e) {
    if (!e.isTemporary())
      m.cambiaBoton(btn);
  }
}
class teclaListener extends KeyAdapter {
  MenuPrincipal m;
  int btn=0;

  public teclaListener(MenuPrincipal mp, int boton) {
    m = mp;
    btn = boton;
  }
  public void keyPressed(KeyEvent e) {
    m.teclas(e);
  }
}

