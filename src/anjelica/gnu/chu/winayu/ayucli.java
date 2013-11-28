package gnu.chu.winayu;
/**
 *
 * Author:       Chuchi P.
 *
 * Description:  Pantalla de Ayuda de Clientes,    permite Consultar clientes
 * por nombre, NIF o Razon Social.
 * Siempre sera Llamado desde otro programa.
 * <p>Copyright: Copyright (c) 2005
 *   Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
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
 * @author chuchiP
 * @version 1.0
 */

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.sql.*;
import gnu.chu.sql.*;
import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import gnu.chu.sql.*;
import javax.swing.*;
import javax.swing.event.*;

public class ayucli extends ventana
{
  String zona=null;
  DatosTabla dtCon1;
  CPanel vPanel1 = new CPanel();
  CLabel cli_nombL = new CLabel();
  public CTextField cli_nombE = new CTextField(Types.CHAR,"X",35);
  public CTextField cli_nombcE = new CTextField(Types.CHAR,"X",35);
  CLabel cli_nombcL = new CLabel();
  public CTextField cli_poblE = new CTextField(Types.CHAR,"X",35);
  CLabel cli_poblL = new CLabel();
  CComboBox cli_acreE = new CComboBox();
//  Shape VShape1 = new VShape();

  Cgrid jt = new Cgrid(4);
  CButton Bbuscar = new CButton(Iconos.getImageIcon("binocular"));
//  Vector datos=new Vector();
  Vector cabecera = new Vector();

  String strSql="";
  public boolean Error=false;
  public String cli_codiE="";
  public String cli_cuenE="";
  public boolean consulta=false;
  CLabel CLabel1 = new CLabel();
  BorderLayout borderLayout1 = new BorderLayout();
  CPanel cPanel1 = new CPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();


  public ayucli(EntornoUsuario e,JLayeredPane fr)
  {
    this(e,fr,null);
  }
  public ayucli(EntornoUsuario e,JLayeredPane fr,DatosTabla dt)
  {
    setTitulo("Ayuda Clientes");
    EU = e;
    eje = false;
    if (dt!=null)
      dtCon1=dt;
    try
    {
      jbInit();
    }
    catch (Exception k)
    {
      fatalError("constructor: ", k);
      setErrorInit(true);
      return;
    }
  }

  private void jbInit() throws Exception {
    this.setSize(new Dimension(580, 451));
    this.setIconifiable(false);
    this.setClosable(false);
    this.setResizable(true);

    statusBar = new StatusBar(this);

    if (dtCon1==null)
      conecta();

    Bbuscar.setText("(F4) Buscar");
    Bbuscar.setMargin(new Insets(0, 0, 0, 0));
    Bbuscar.setToolTipText("Buscar Clientes con estos Conceptos");
    Bbuscar.setBounds(new Rectangle(445, 24, 109, 20));
    Bbuscar.setFont(new Font("Dialog", 1, 11));
    Bbuscar.setMnemonic('B');
    CLabel1.setRequestFocusEnabled(true);
    CLabel1.setText("Situac. Cliente ");
    CLabel1.setBounds(new Rectangle(300, 26, 78, 17));

    vPanel1.setDefButton(Bbuscar);
    jt.setBackground(Color.gray);
    jt.setMinimumSize(new Dimension(566, 311));
    jt.setPreferredSize(new Dimension(566, 311));
    jt.setPuntoDeScroll(50);
    cPanel1.setBorder(BorderFactory.createLoweredBevelBorder());
    cPanel1.setMaximumSize(new Dimension(562, 47));
    cPanel1.setMinimumSize(new Dimension(562, 47));
    cPanel1.setPreferredSize(new Dimension(562, 47));
    cPanel1.setLayout(null);
    cli_acreE.setBounds(new Rectangle(381, 24, 61, 20));
    cli_nombL.setBounds(new Rectangle(0, 26, 78, 17));
    cli_nombcE.setBounds(new Rectangle(79, 23, 209, 17));
    cli_poblE.setBounds(new Rectangle(358, 6, 196, 17));
    cli_nombcL.setBounds(new Rectangle(-1, 5, 74, 18));
    cli_nombE.setBounds(new Rectangle(80, 4, 208, 17));
    cli_poblL.setBounds(new Rectangle(294, 3, 60, 17));
    cli_nombE.setMayusc(true);
    cli_nombcE.setMayusc(true);
    cli_poblE.setMayusc(true);
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);

    vPanel1.setLayout(gridBagLayout1);
    this.setTitle(getTitulo());
    cli_nombL.setText("Nombre Fiscal");
    cli_nombL.setHorizontalAlignment(4);
    cli_poblL.setText("Poblacion");
    cli_poblL.setHorizontalAlignment(4);

    cli_nombcL.setText("Nomb. Social");
    cli_nombcL.setHorizontalAlignment(4);
    // Configurando el Grid.
    cabecera.addElement("Codigo"); // 0
    cabecera.addElement("Nombre Fiscal"); //1
    cabecera.addElement("Nombre Social"); // 2
    cabecera.addElement("Poblaccion"); // 3
   jt.setCabecera(cabecera);
    int i []= {50,300,300,300};
    jt.setAnchoColumna(i);

    jt.setConfigurar("gnu.chu.winayu.ayucli",EU,dtCon1);
    Vector v1= new Vector();

    this.getContentPane().add(vPanel1,  BorderLayout.CENTER);
    vPanel1.add(jt,   new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 4), 0, 0));
    vPanel1.add(cPanel1,    new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    cPanel1.add(cli_poblE, null);
    cPanel1.add(cli_nombcL, null);
    cPanel1.add(cli_nombE, null);
    cPanel1.add(cli_poblL, null);
    cPanel1.add(cli_nombL, null);
    cPanel1.add(cli_nombcE, null);
    cPanel1.add(CLabel1, null);
    cPanel1.add(cli_acreE, null);
    cPanel1.add(Bbuscar, null);
    cPanel1.setButton(KeyEvent.VK_F4,Bbuscar);
    cPanel1.setDefButton(Bbuscar);
    cli_acreE.addItem("Todos","T");
    cli_acreE.addItem("Activo","S");
    cli_acreE.addItem("Retirado","N");
    activarEventos();
  }
  public void setZona(String zona)
  {
    this.zona=zona;
  }
    @Override
  public void iniciarVentana() throws Exception
  {
    jt.removeAllDatos();
    cPanel1.resetTexto();
    cli_acreE.setValor("S");
    SwingUtilities.invokeLater(new Runnable(){
        public void run()
        {
            cli_nombE.requestFocus();
        }
    });
    
  }

  void activarEventos()
  {
    Bbuscar.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Bbuscar_actionPerformed();
      }
    });

    jt.tableView.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent m)
      {
        if (m.getClickCount() > 1 && jt.isVacio()==false)
        {
          Baceptar_actionPerformed();
        }
      }

    });

    jt.tableView.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyPressed(KeyEvent e)
      {
        if ((e.getKeyCode() == KeyEvent.VK_INSERT || e.getKeyCode() == KeyEvent.VK_ENTER) && jt.isVacio()==false)
          Baceptar_actionPerformed();
      }

    });
  }
  public void Bbuscar_actionPerformed() {
    String s="";

    if (cli_nombE.getText().length()<= 3 && ! cli_nombE.isNull())
    {
      mensaje("Introduzca al Menos 3 Caracteres");
      cli_nombE.requestFocus();
      return;
    }
    if (cli_nombcE.getText().length()<= 3 && ! cli_nombcE.isNull())
    {
      mensaje("Introduzca al Menos 3 Caracteres");
      cli_nombcE.requestFocus();
      return;
    }
    if (cli_poblE.getText().length()<= 3 && ! cli_poblE.isNull())
    {
      mensaje("Introduzca al Menos 3 Caracteres");
      cli_poblE.requestFocus();
      return;
    }
    s="1=1 ";
    if (! cli_nombE.isNull())
      s=s+(s.equals("")?"":"and ")+" UPPER(cli_nomb) LIKE '%"+cli_nombE.getText()+"%'";

    if (! cli_nombcE.isNull())
      s=s+(s.equals("")?"":"and ")+" UPPER(cli_nomco) LIKE '%"+cli_nombcE.getText()+"%'";

    if (! cli_poblE.isNull())
      s=s+(s.equals("")?"":"and ")+" UPPER(cli_pobl) LIKE '%"+cli_poblE.getText()+"%'";


    if (cli_acreE.getValor().compareTo("T")!=0)
      s=s+(s.equals("")?"":"and ")+" cli_activ= '"+cli_acreE.getValor()+"'";

    try
    {
        strSql = "select cli_codi,cli_nomb,cli_nomco,cli_pobl " +
            " FROM clientes " +
            " WHERE " + s +
            (zona==null?"":" AND cli_zonrep LIKE '"+zona+"'")+
            " order by cli_codi";
//        debug("AyuCli: "+strSql);
        if (! dtCon1.select(strSql))
        {
          mensajeErr("NO encontrados CLIENTES con estos criterios");
          cli_nombE.requestFocus();
          return;
        }

        jt.setDatos(dtCon1);
      } catch (Exception k)
      {
        Error("Error al buscar clientes",k);
        return;
      }
      jt.requestFocusInicio();
  }

  void Baceptar_actionPerformed()
  {
    if (jt.tableView.getSelectedRow() < 0)
    {
      mensaje("SELECCIONE UNA COLUMNA EN EL GRID");
      jt.requestFocus();
      return;
    }

    // Editar Columna Activa.
    cli_codiE = jt.getValString(0);

    cli_nombE.setText(jt.getValString(1));
    cli_nombcE.setText(jt.getValString(2));
    cli_poblE.setText(jt.getValString(3));
    consulta = true;
    matar();
  }

  void Bcancelar_actionPerformed(ActionEvent e) {
    matar();
  }

}



