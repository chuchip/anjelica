package gnu.chu.winayu;


import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import gnu.chu.sql.*;
import javax.swing.*;
/**
 *
 * <p>Título: ayuFpa</p>
 * <p>Descripción: Pantalla de Ayuda de Formas de Pago</p>
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
 * <p>Empresa: </p>
 * @author Chuchi P
 * @version 1.0
 */
public class ayuFpa extends ventana implements  Runnable
{
  Vector datos=new Vector();
  BorderLayout bLay1 = new BorderLayout();
  String strSql="";
  public String fpa_codiT="";
  public String fpa_nombT="";
  public boolean consulta=false;
  CPanel Pprinc = new CPanel();
  Cgrid jt = new Cgrid(6);
  CPanel Pcons = new CPanel();
  CLabel prv_nombL = new CLabel();
  CTextField fpa_nombE = new CTextField();
  CButton Belegir = new CButton(Iconos.getImageIcon("rotarup"));
  CButton Baceptar = new CButton(Iconos.getImageIcon("buscar"));
  CLabel CLabel1 = new CLabel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  GridBagLayout gridBagLayout2 = new GridBagLayout();

  public ayuFpa(EntornoUsuario e,JLayeredPane fr)
  {
    this(e,fr,null);
  }

  /**
  *  Recibe el Entorno Usuario, el Principal
  */
 public ayuFpa(EntornoUsuario e, JLayeredPane fr, DatosTabla dt)
 {
   setTitulo("Ayuda Formas de Pago");
   eje = true;

   EU = e;
//    jf=fr;
   if (dt != null)
     dtCon1 = dt;

   try
   {
     jbInit();
   }
   catch (Exception k)
   {
     setErrorInit(true);
     return;
   }
 }

  private void jbInit() throws Exception
  {
    this.setTitle(getTitulo());
    this.setIconifiable(false);
    this.setResizable(true);


    this.setSize(new Dimension(471,383));

    statusBar = new StatusBar(this);
    if (dtCon1==null)
      conecta();

//    Pcons.setBorder(BBraise);
    Pcons.setMaximumSize(new Dimension(455, 41));
    Pcons.setMinimumSize(new Dimension(455, 41));
    Pcons.setPreferredSize(new Dimension(455, 41));
    Pcons.setDefButton(Baceptar);
    prv_nombL.setMaximumSize(new Dimension(47, 16));
    prv_nombL.setMinimumSize(new Dimension(47, 16));
    prv_nombL.setPreferredSize(new Dimension(47, 16));
    prv_nombL.setText("Nombre");

    Baceptar.setText("F4 Buscar");
    Baceptar.setMaximumSize(new Dimension(133, 28));
    Baceptar.setMinimumSize(new Dimension(133, 28));
    Baceptar.setPreferredSize(new Dimension(133, 28));
    Baceptar.setMargin(new Insets(0, 0, 0, 0));
    Baceptar.setMnemonic('B');

    Pcons.setLayout(gridBagLayout1);
    Pprinc.setLayout(gridBagLayout2);

    // Configurando el Grid.
    Vector cabecera = new Vector();
    cabecera.addElement("Codigo"); // 0 -- Codigo
    cabecera.addElement("Nombre"); //1 -- Nombre
    cabecera.addElement("Dia 1"); // 2 --
    cabecera.addElement("Dia 2"); // 3 --
    cabecera.addElement("Dia 3"); // 4 --
    cabecera.addElement("Giro"); // 5 --


    jt.setCabecera(cabecera);
    int i []= {46,283,50,50,50,40};
    jt.setAnchoColumna(i);
    int a[]= {1,0,2,2,2,1};
    jt.setAlinearColumna(a);
    jt.ajustar(true);
    jt.setNumRegCargar(100);

    jt.setAjustarGrid(true);
//    jt.setIgnoraColumna(0,true);
    jt.setConfigurar("gnu.chu.winayu.ayuFpa",EU,dtCon1);
    jt.setBackground(Color.orange);
    jt.setMaximumSize(new Dimension(455, 265));
    jt.setMinimumSize(new Dimension(455, 265));
    jt.setOpaque(true);
    jt.setPreferredSize(new Dimension(455, 265));
    jt.setNumRegCargar(25);

    Belegir.setEnabled(false);
    Pprinc.setDefButton(Belegir);
    Belegir.setMargin(new Insets(0, 0, 0, 0));
    Belegir.setToolTipText("Elegir el Registro Activo");

        // Poniendo Orejas.
    Belegir.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Belegir_actionPerformed();
      }
    });
    Baceptar.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Baceptar_actionPerformed();
      }
    });

    jt.tableView.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent m)
      {
        if (m.getClickCount() > 1)
        {
          Belegir_actionPerformed();
        }
      }
    });
    jt.tableView.addKeyListener(new KeyAdapter()
    {
            @Override
     public void keyPressed(KeyEvent e) {
       if ((e.getKeyCode() == KeyEvent.VK_INSERT || e.getKeyCode() == KeyEvent.VK_ENTER))
         Belegir_actionPerformed();
     }
    });
    Belegir.setToolTipText("Elegir el Registro Activo");

    fpa_nombE.setMaximumSize(new Dimension(234, 15));
    fpa_nombE.setMinimumSize(new Dimension(234, 15));
    fpa_nombE.setPreferredSize(new Dimension(234, 15));

    this.getContentPane().add(statusBar, BorderLayout.SOUTH);
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
    Pprinc.add(jt,  new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 1, 0, 5), 0, 0));
    Pprinc.add(Pcons,     new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 1, 5, 5), 0, 0));
    Pcons.add(fpa_nombE,   new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(3, 0, 0, 5), 0, 0));
    Pcons.add(prv_nombL,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 1, 0, 0), 0, 0));
    Pcons.add(Baceptar,   new GridBagConstraints(2, 0, 1, 2, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(3, 0, 10, 10), 0, 0));
    Pcons.add(CLabel1,  new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 25, 1, 168), 87, 17));
    Belegir.setBounds(200,0,25,22);
    jt.panelBuscar.add(Belegir,null);//, new XYConstraints(200,0, 25, 22));

  }
  public void iniciarVentana() throws Exception
  {
     Pcons.setDefButton(Baceptar);
     Pcons.setButton(KeyEvent.VK_F4, Baceptar);

     fpa_nombE.setColumnaAlias("fpa_nomb");
     fpa_nombE.setQuery(true);
     fpa_nombE.requestFocus();
  }
  void Baceptar_actionPerformed()
  {

    String s="";
    s=fpa_nombE.getStrQuery();


    strSql="select fpa_codi,fpa_nomb,fpa_dia1,fpa_dia2,fpa_dia3,fpa_esgir FROM v_forpago ";

    if (! s.trim().equals(""))
      strSql+=" where "+s;
    strSql+=" order by fpa_codi";


    Thread th= new Thread(this);
    th.setPriority(Thread.MAX_PRIORITY);
    th.start();
  }
    public boolean rgSelect()
    {
    mensaje("Buscando Registros ....");
    Belegir.setEnabled(false);
        try{
        if (dtCon1.select(strSql,false)==false)
        {
            mensaje(" -- NO ENCONTRADOS FORMAS DE PAGO con esas Condiciones -- ");
            return true;
        }
    } catch (Throwable k)
    {
         fatalError("Error al Buscar FORMAS DE PAGO: ",dtCon1.SqlException);
         return false;
    }
        //	Llenar el Grid con la Base de Datos.
    Belegir.setEnabled(true);

    jt.setEnabled(false);
    jt.setEnabled(true);

    mensaje("Formas de Pago Seleccionados ...");

        return true;
    }

  void Belegir_actionPerformed()
  {
    if (jt.isVacio())
    {
      mensaje("No Encontrados Registros para Selecionar");
      jt.requestFocus();
      return;
    }

        // Editar Columna Activa.
    fpa_nombT=jt.getValString(1);
    fpa_codiT=jt.getValString(0);
    consulta=true;
    matar();
  }

  public void run()
  {
    setEnabled(false);
    rgSelect();
    jt.setDatos(dtCon1);
    validate();
    repaint();
    setEnabled(true);
    jt.setEnabled(true);
    jt.requestFocusInicio();
  }
} // Final de Clase


