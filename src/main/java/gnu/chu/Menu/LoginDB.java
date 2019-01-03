package gnu.chu.Menu;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import gnu.chu.controles.*;
import gnu.chu.sql.*;
import gnu.chu.utilidades.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * <p>Título: LoginDB</p>
 * <p>Descripción: Muestra la pantalla de login para entrar a Anjelica
 * Si la contraseña y usuario es valido lanza la clase gnu.chu.Menu.Menu1
 *  </p>
 * <p>Copyright: Copyright (c) 2005-2018
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
public class LoginDB extends JFrame
{
  String gestion;
  static String dirMailAviso;
  static String dirMailError;
  static String mailHost;
  DatosTabla dt;
  /**
   * Pantalla de Introducion
   */
  CLabel icon = new CLabel();
  /**
   * Pantalla de Dialogo
   */
//  public JDialog frame;
  BorderLayout borderLayout1 = new BorderLayout();
  CPanel beVelPanel1 = new CPanel();
  CLabel label1 = new CLabel();
  public CTextField Usuario = new CTextField();
  CLabel label2 = new CLabel();
  public CPasswordField Password = new CPasswordField();
  CButton Baceptar = new CButton("Aceptar", Iconos.getImageIcon("check"));
  CButton Bsalir = new CButton(Iconos.getImageIcon("salir"));

  conexion MyDb;
//  private ResourceBundle param;
  int repetiones=0;

  Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
  CLabel vLabel1 = new CLabel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  EntornoUsuario EU;
  CLabel cLabel1 = new CLabel();
  CLabel versionL = new CLabel();
  CLabel dbL = new CLabel();


  public LoginDB() {
    
      try
      {
          jbInit();
          UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);        
      } catch (Exception ex)
      {
          Logger.getLogger(LoginDB.class.getName()).log(Level.SEVERE, null, ex);
      }
  }


  private void jbInit() throws Exception
  {
    
    this.setSize(new Dimension(239, 250));
    this.setResizable(false);

    //Center the window
    Dimension frameSize = this.getSize();
    if (frameSize.height > screenSize.height)
      frameSize.height = screenSize.height;
    if (frameSize.width > screenSize.width)
      frameSize.width = screenSize.width;
    this.setLocation( (screenSize.width - frameSize.width) / 2,
                     (screenSize.height - frameSize.height) / 2);

   this.setTitle("Anjelica - Login");
    EU= cargaEntornoUsu();
    gestion=EU.empresa;


    this.getContentPane().setLayout(new BorderLayout());
    this.validate();
    versionL.setText("Versión :"+EU.getVersion());
    dbL.setText("DB: "+gestion.substring(0, 1).toUpperCase() +  gestion.substring(1).toLowerCase());

    ImageIcon a = Iconos.getImageIcon(EU.empresa.toLowerCase());
    this.setIconImage(a.getImage());

    vLabel1.setBackground(Color.blue);
    vLabel1.setFont(new java.awt.Font("SansSerif", 1, 11));
    vLabel1.setForeground(Color.white);
    vLabel1.setMaximumSize(new Dimension(173, 17));
    vLabel1.setMinimumSize(new Dimension(173, 17));
    vLabel1.setOpaque(true);
    vLabel1.setPreferredSize(new Dimension(173, 17));
    vLabel1.setText("  Cargando Menu ... Espere.");

    label1.setText("Usuario");
    label2.setText("Contraseña");
    Baceptar.setMnemonic('A');
    Baceptar.setMargin(new Insets(0,0,0,0));
    Bsalir.setMnemonic('S');
    Bsalir.setMargin(new Insets(0,0,0,0));
    Bsalir.setText("Salir");
    beVelPanel1.setLayout(null);
    cLabel1.setText("Contraseña");
    cLabel1.setBounds(new Rectangle(18, 71, 76, 24));
    versionL.setBackground(Color.blue);
    versionL.setForeground(Color.yellow);
    versionL.setOpaque(true);
    versionL.setHorizontalAlignment(SwingConstants.CENTER);
    versionL.setHorizontalTextPosition(SwingConstants.CENTER);

    versionL.setBounds(new Rectangle(32, 152, 182, 17));
    dbL.setBackground(Color.yellow);
    dbL.setForeground(Color.blue);
    dbL.setAlignmentY((float) 0.5);
    dbL.setOpaque(true);
    dbL.setHorizontalAlignment(SwingConstants.CENTER);

    dbL.setBounds(new Rectangle(7, 5, 226, 18));
    this.getContentPane().add(beVelPanel1, BorderLayout.CENTER);
    Usuario.setMayusc(false);
    beVelPanel1.setEscButton(Bsalir);
    beVelPanel1.setDefButton(Baceptar);
    Usuario.setBounds(new Rectangle(106, 34, 107, 22));
    label1.setBounds(new Rectangle(22, 34, 58, 22));
    Password.setBounds(105, 61, 107, 22);
    Password.setBounds(new Rectangle(106, 72, 105, 22));
    beVelPanel1.add(label2, null);
    Baceptar.setBounds(9, 112, 97, 29);
    Bsalir.setBounds(125, 111, 97, 29);
    beVelPanel1.add(Baceptar,null);
    beVelPanel1.add(Bsalir, null);
    beVelPanel1.add(versionL, null);
    beVelPanel1.add(Usuario, null);
    beVelPanel1.add(label1, null);
    beVelPanel1.add(Password, null);
    beVelPanel1.add(cLabel1, null);
    beVelPanel1.add(dbL, null);

    activarEventos();

  }

  void activarEventos()
  {
    Baceptar.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        if (Usuario.getText().equals(""))
        {
          Usuario.requestFocus();
          return;
        }
        if (Password.getPassword().equals(""))
        {
          Password.requestFocus();
          return;
        }

        Baceptar_actionPerformed();
      }
    });

    Bsalir.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        cancelar();
      }
    });
    this.addWindowListener(new WindowAdapter()
    {
      @Override
      public void windowOpened(WindowEvent e)
      {
        if (Usuario.getText().equals(""))
          Usuario.requestFocus();
        else
          Password.requestFocus();
      }

      @Override
      public void windowClosing(WindowEvent e)
      {
        Bsalir.doClick();
      }
    });

  }
  protected void cancelar() {
    System.exit(0);
  }
  @Override
  public void dispose() {
    try {
        this.dispose();
    } catch (Exception j) {}
  }

  public void iniciar()
  {

    if (Usuario.getText().equals(""))
       Usuario.setText(System.getProperty("user.name"));

    if (System.getProperty("Usuario") != null)
       Usuario.setText(System.getProperty("Usuario"));
    else
    {
        try {
          System.out.println("Home: " + System.getProperty("user.home"));
          File fo = new File(System.getProperty("user.home") + "/.anjelica.user");
          if (fo.isFile() && fo.canRead())
          {
            FileInputStream fis = new FileInputStream(fo);
            byte cadena[] = new byte[100];
            int lgCad=fis.read(cadena, 0, 100);
            System.out.println("usuario: " + new String(cadena,0,lgCad));
            Usuario.setText(new String(cadena,0,lgCad));
            fis.close();
          }
        } catch (Throwable k)
        {
          k.printStackTrace();
        }
    }


    if (System.getProperty("Pass") != null)
       Password.setText(System.getProperty("Pass"));

    if (!Usuario.getText().equals("") && !Password.getText().equals(""))
       Baceptar.doClick();
  }

   protected void Baceptar_actionPerformed()
    {
      Thread t = new Thread(new Runnable()
      {
        @Override
        public void run()
        {
          beVelPanel1.setEnabled(false);
          try
          {
            MyDb = new conexion(Usuario.getText(), Password.getText(),
                                EU.driverDB, EU.addressDB);
            MyDb.setCatalog(EU.catalog);
          }
          catch (Exception k)
          {
            if (repetiones == 0)
              mensajes.mensajeUrgente(k.getMessage() + "\nIntentelo de Nuevo.",
                                      Usuario);
            else if (repetiones == 1)
              mensajes.mensajeUrgente(k.getMessage() +
                  "\nIntentelo de Nuevo. Cuidado es la Ultima oportunidad",
                                      Usuario);
            else
            {
              mensajes.mensajeUrgente(k.getMessage() + "\nAdios.", Usuario);
              System.exit(1);
            }
            beVelPanel1.setEnabled(true);

            Usuario.requestFocus();
            repetiones++;
            return;
          }

          lanzaPrincipal();
        }
      });
      t.setName("Ejecutando Menu ....");
      t.start();
  }

/**
 * Se ha validado correctamente el usuario. Lanzo la clase 'principal'
 */
  private void lanzaPrincipal()
  {
    try
    {
      dt=new DatosTabla(MyDb);
      
      String fecLic=EU.getValorParam("fl", "", dt);
      if (!fecLic.equals(""))
      {
          if (Formatear.comparaFechas(Formatear.getDateAct(), Formatear.getDate(fecLic, "dd-MM-yyyy"))>=0 )
          {
              if (!pedirLic())
                  System.exit(1);
          }
      }
      EU.usuario = Usuario.getText();
      EU.password = Password.getText();
      File fo = new File(System.getProperty("user.home") + "/.anjelica.user");
      FileWriter fw=new FileWriter(fo,false);
      fw.write(EU.usuario);
      fw.close();
    }
    catch (Exception k)
    {
        k.printStackTrace();
    }
    
    SwingUtilities.invokeLater(new Thread()
    {
            @Override
      public void run()
      {
        Principal frame = null;
        try
        {
          DatosTabla dt = new DatosTabla(MyDb);
          String menuEje = "gnu.chu.Menu.Menu1";
          // Ejecuta el Menu Seleccionado
          Class miClase = Class.forName(menuEje);
          Object[] misParam =
          {
            Usuario.getText(), Password.getText(), MyDb};
            Class[] tipos = new Class[misParam.length];
            for (int i = 0; i < misParam.length; i++)
            {
              tipos[i] = misParam[i].getClass();
            }
            Constructor elConstructor = miClase.getConstructor(tipos);
            frame = (gnu.chu.Menu.Principal) elConstructor.newInstance(misParam);
            frame.validate();
            frame.setVisible(true);
            LoginDB.this.setVisible(false);//dispose();
        }
        catch (Throwable j)
        {
          j.printStackTrace();
        }
      }
    }
    );
  }
  
  public boolean pedirLic() throws SQLException
  {
    String pass= EU.getValorParam("flp", "", dt);
    int r=Integer.parseInt( EU.getValorParam("fle", "0", dt));
    if (r>5)
        return false;
    do
    {
        String cta=mensajes.mensajeGetTexto("Introduzca la contraseña para validar licencia.\n Intento ("+
            (r+1)+" de 5)","Contraseña Licencia" );
        if (cta==null)
        {
            dt.commit();
            return false;
        }
        if ( cta.equals(pass))
        {
            pass= EU.getValorParam("fld", "", dt);
            EU.setValorParam("fl", Formatear.sumaDias(Formatear.getDateAct(), Integer.parseInt(pass))
                 , "", "*", dt);
            EU.setValorParam("fle", "0", "", "*", dt);
            dt.commit();
            return true;
        }     
        EU.setValorParam("fle", ""+(r+1), "", "*", dt);
        r++;
    } while (r<5);
  
    String s="delete from menus";
    dt.executeUpdate(s);
    s="delete from parametros";
    dt.executeUpdate(s);
    s="delete from configuracion";
    dt.executeUpdate(s);
    s="delete from usuarios";
    dt.executeUpdate(s);
  

    dt.commit();
    return false;
  }
  public static EntornoUsuario cargaEntornoUsu() throws Exception
  {
    String db = System.getProperty("db");
    if (db == null)
      db = "post";
    String raiz=System.getProperty("raiz");
    if (raiz==null)
      raiz="gnu.chu.anjelica.";
    EntornoUsuario eu = new EntornoUsuario();
    eu.empresa = "anjelica";
    if (System.getProperty("empresa") != null)
      eu.empresa = System.getProperty("empresa");
    return cargaEntornoUsu(raiz,db,eu);
  }

  public static void iniciarLKEmpresa(EntornoUsuario EU,DatosTabla dt) throws java.sql.SQLException
  {
    if (EU.lkEmpresa==null || EU.lkEmpresa.isNull())
    {
      String s = "SELECT * FROM v_empresa where emp_codi = " + EU.em_cod;
      dt.selectInto(s, EU.lkEmpresa);
    }
  }

  public static EntornoUsuario cargaEntornoUsu(String raiz,String db,EntornoUsuario eu) throws Exception
  {
   ResourceBundle param = ResourceBundle.getBundle(raiz+".db_"+db);
   Enumeration en = param.getKeys();
   String el;
   String val;

   boolean asigPATHREPORT = false;
   boolean asigDIRTMP = false;
   boolean asigCOMPRINT = false;
   boolean asigPUERTOALB = false;
   boolean asigPATHCOM = false;

   while (en.hasMoreElements())
   {
     el = en.nextElement().toString();
     val = param.getString(el);
     if (el.equals("USER"))
       eu.usuario = val;
     if (el.equals("PASS"))
       eu.password = val;
     if (el.equals("DRIVER"))
       eu.driverDB = val;
     if (el.equals("URL"))
       eu.addressDB = val;
     if (el.equals("CATALOG"))
       eu.catalog = val.trim().equals("")?null:val;
   }
   param = ResourceBundle.getBundle(raiz+".config");
   eu.setParametrosConfiguracion(param);
   en = param.getKeys();
   dirMailAviso=null;
   dirMailError=null;
   mailHost = "127.0.0.1";
   eu.dirTmp=System.getProperty("java.io.tmpdir"); // Valor por def. del sistema

   while (en.hasMoreElements())
   {
     el = en.nextElement().toString();
     val = param.getString(el);
     if (el.startsWith("PRINT_"))
       eu.setImpresora(el.substring(6),val.toUpperCase());
     if (el.equals("VERSION"))
       eu.setVersion(val.trim().equals("")?null:val);
     if (el.equals("LOG4J"))
       eu.setLog4J(val.trim().equals("")?null:val);
     if (el.equals("DEBUG"))
       eu.debug = Boolean.parseBoolean(val);
     if (el.equals("DIRMAILAVISO"))
       dirMailAviso=val.trim().equals("")?null:val;
     if (el.equals("DIRMAILERROR"))
       dirMailError=val.trim().equals("")?null:val;
     if (el.equals("MAILHOST"))
       mailHost=val;
     if (el.equals("PATHREPORTALT"))
       eu.setPathReportAlt(val);
     if (el.equals("EJERC"))
       eu.ejercicio = Integer.parseInt(val);
     if (el.equals("EMPCODI"))
       eu.em_cod = Integer.parseInt(val);
     if (el.equals("PATHINSTALL"))
       eu.pathInstall =val;

     if (System.getProperty("os.name").toUpperCase().indexOf("WIN") != -1)
     {
       if (el.equals("PATHREPORTWIN"))
       {
         eu.pathReport = val;
         System.out.println("Path Report: "+val);
         if (val!=null && !val.equals(""))
         {
//           System.out.println("Asignado Path report: ");
           asigPATHREPORT = true;
         }
       }
       if (el.equals("DIRTMPWIN"))
       {
         eu.dirTmp = val;
         if (val!=null && !val.equals(""))
           asigDIRTMP=true;
       }
       if (el.equals("COMPRINTWIN"))
       {
         eu.comPrint = val;
         if (val!=null && !val.equals(""))
           asigCOMPRINT=true;
       }
       if (el.equals("PUERTOALBWIN"))
       {
         eu.puertoAlb = val;
         if (val!=null && !val.equals(""))
           asigPUERTOALB=true;
       }
       if (el.equals("PATHCOMWIN"))
       {
         eu.pathCom = val;
         if (val!=null && !val.equals(""))
           asigPATHCOM=true;
       }
     }
     if (el.equals("PATHREPORT") && !asigPATHREPORT )
         eu.pathReport = val;
     if (el.equals("DIRTMP") && !asigDIRTMP)
       eu.dirTmp = val;
     if (el.equals("COMPRINT") && !asigCOMPRINT)
       eu.comPrint = val;
     if (el.equals("PUERTOALB") && !asigPUERTOALB)
       eu.puertoAlb = val;
     if (el.equals("PATHCOM") && !asigPATHCOM)
       eu.pathCom = val;
   }
//   System.out.println(" Systema: "+System.getProperty("os.name")+" PATHREPORT: "+eu.pathReport);
//   System.out.println("PathReport: " + eu.pathReport + "\nDIRTMP: " + eu.dirTmp +
//                    "\nCOMPRINT: " + eu.comPrint +
//                    "\nPUERTOALB: " + eu.puertoAlb + "\nPATHCOM: " +
//                    eu.pathCom);
   return eu;
  }
  public static String getDirMailAviso()
  {
    return dirMailAviso;
  }
  public static String getDirMailError()
  {
    return dirMailError;
  }
  public static String getMailHost()
  {
    return mailHost;
  }
}
