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
 * <p>Título: ayuTra</p>
 * <p>Descripcion: Ventana de Ayuda para buscar Transportistas por nombre. </p>
 * <p>Copyright: Copyright (c) 2005-2011
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los términos de la Licencia Publica General de GNU según es publicada por
 *  la Free Software Foundation, bien de la versión 2 de dicha Licencia
 *  o bien (según su elección) de cualquier versión posterior.
 *  Este programa se distribuye con la esperanza de que sea útil,
 *  pero SIN NINGUNA GARANTÍA, incluso sin la garantía MERCANTIL implícita
 *  o sin garantizar la CONVENIENCIA PARA UN PROPÓSITO PARTICULAR.
 *  Véase la Licencia Pública General de GNU para más detalles.
 *  Debería haber recibido una copia de la Licencia Pública General junto con este programa.
 *  Si no ha sido así, escriba a la Free Software Foundation, Inc.,
 *  en 675 Mass Ave, Cambridge, MA 02139, EEUU.
 * </p>
 * <p>Empresa: MISL</p>
 * @author chuchiP
 * @version 1.0
 */

public class ayuTra extends ventana // implements  Runnable
{
  Vector datos=new Vector();
  BorderLayout bLay1 = new BorderLayout();
  String strSql="";
  public String tra_codiT="";
  public String tra_nombT="";
  public boolean consulta=false;
  CPanel Pprinc = new CPanel();
  Cgrid jt = new Cgrid(3);
  CPanel Pcons = new CPanel();
  CLabel tra_nombL = new CLabel();
  CTextField tra_nombE = new CTextField();
  CButton Belegir = new CButton(Iconos.getImageIcon("rotarup"));
  CButton Baceptar = new CButton(Iconos.getImageIcon("buscar"));
  CLabel CLabel1 = new CLabel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  CTextField tra_poblE = new CTextField();

  public ayuTra(EntornoUsuario e,JLayeredPane fr)
  {
    this(e,fr,null);
  }


 public ayuTra(EntornoUsuario e, JLayeredPane fr, DatosTabla dt)
 {
   setTitulo("Ayuda Transportistas");
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
    this.setResizable(true);
    this.setIconifiable(false);
    this.setSize(new Dimension(471,383));

    statusBar = new StatusBar(this);
    if (dtCon1==null)
      conecta();


    Pcons.setMaximumSize(new Dimension(455, 41));
    Pcons.setMinimumSize(new Dimension(455, 41));
    Pcons.setPreferredSize(new Dimension(455, 41));
    Pcons.setDefButton(Baceptar);
    tra_nombL.setMaximumSize(new Dimension(47, 16));
    tra_nombL.setMinimumSize(new Dimension(47, 16));
    tra_nombL.setPreferredSize(new Dimension(47, 16));
    tra_nombL.setText("Nombre");

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
    cabecera.addElement("Poblacion"); // 2 -- Poblacion
    jt.setCabecera(cabecera);
    jt.setAnchoColumna(new int[]{56,283,253});
    jt.setFormatoColumna(0,"###9");
    jt.setAlinearColumna(new int[]{2,0,0});
    jt.ajustar(true);
    jt.setNumRegCargar(100);
    jt.setAjustarGrid(true);
    jt.setMaximumSize(new Dimension(455, 265));
    jt.setMinimumSize(new Dimension(455, 265));
    jt.setPreferredSize(new Dimension(455, 265));

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
      public void keyPressed(KeyEvent e)
      {
        if (e.getKeyCode() == KeyEvent.VK_INSERT || e.getKeyCode() == KeyEvent.VK_ENTER)
          Belegir_actionPerformed();
      }
    });
    Belegir.setToolTipText("Elegir el Registro Activo");

    tra_nombE.setMaximumSize(new Dimension(234, 15));
    tra_nombE.setMinimumSize(new Dimension(234, 15));
    tra_nombE.setPreferredSize(new Dimension(234, 15));

    CLabel1.setOpaque(false);
    CLabel1.setText("Poblacion");
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
    Pprinc.add(jt,  new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 1, 0, 5), 0, 0));
    Pprinc.add(Pcons,     new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 1, 5, 5), 0, 0));
    Pcons.add(tra_nombE,   new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(3, 0, 0, 5), 0, 0));
    Pcons.add(tra_nombL,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 1, 0, 0), 0, 0));
    Pcons.add(Baceptar,   new GridBagConstraints(2, 0, 1, 2, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(3, 0, 10, 10), 0, 0));
    Pcons.add(CLabel1,     new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 1, 5), 0, 0));
    Pcons.add(tra_poblE,  new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    Belegir.setBounds(200,0,25,22);
    jt.panelBuscar.add(Belegir,null);

  }
  public void iniciarVentana() throws Exception
  {
     Pcons.setDefButton(Baceptar);
     Pcons.setButton(KeyEvent.VK_F4, Baceptar);

     tra_nombE.setColumnaAlias("tra_nomb");
     tra_poblE.setColumnaAlias("tra_nomb");
     tra_nombE.setQuery(true);
     tra_nombE.requestFocus();
  }
  void Baceptar_actionPerformed()
  {

    String s="";
    Vector v=new Vector();
    v.add(tra_nombE.getStrQuery());
    v.add(tra_poblE.getStrQuery());
    strSql="select tra_codi,tra_nomb,tra_pobl FROM v_transport ";
    creaWhere(strSql,v,true);
    strSql+=" order by tra_codi";

    rgSelect();
  }

  public boolean rgSelect()
  {
    mensaje("Buscando Registros ....");
    Belegir.setEnabled(false);
    try
    {
      if (dtCon1.select(strSql, false) == false)
      {
        mensaje(" -- NO ENCONTRADOS Transportistas con esas Condiones -- ");
        return true;
      }
    }
    catch (Throwable k)
    {
      fatalError("Error al Buscar Transportistas: ", dtCon1.SqlException);
      return false;
    }
    //	Llenar el Grid con la Base de Datos.
    Belegir.setEnabled(true);

    jt.setEnabled(false);
    jt.setDatos(dtCon1);
    jt.setEnabled(true);
    jt.requestFocusInicio();
    mensaje("Transportistas Seleccionados ...");

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
    tra_nombT=jt.getValString(1);
    tra_codiT=jt.getValString(0);
    consulta=true;
    matar();
  }

  } // Final de Clase


