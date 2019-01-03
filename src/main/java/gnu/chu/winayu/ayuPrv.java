package gnu.chu.winayu;


import java.awt.*;
import java.awt.event.*;
import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import gnu.chu.sql.*;
import java.util.ArrayList;
import javax.swing.*;
/**
 *
 * <p>Título: ayuPrv</p>
 * <p>Descripcion: Ventana de Ayuda para buscar proveedores por nombre. </p>
 * <p>Copyright: Copyright (c) 2005-2017
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los terminos de la Licencia Pública General de GNU según es publicada por
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
 * <p>Empresa: MISL</p>
 * @author chuchiP
 * @version 1.0
 */

public class ayuPrv extends ventana // implements  Runnable
{
    ArrayList datos=new ArrayList();
  BorderLayout bLay1 = new BorderLayout();
  String strSql="";
  public String prv_codiT="";
  public String prv_nombT="";
  public boolean consulta=false;
  CPanel Pprinc = new CPanel();
  Cgrid jt = new Cgrid(3);
  CPanel Pcons = new CPanel();
  CLabel prv_nombL = new CLabel();
  CTextField prv_nombE = new CTextField();
  CButton Belegir = new CButton(Iconos.getImageIcon("rotarup"));
  CButton Baceptar = new CButton(Iconos.getImageIcon("buscar"));
  CLabel prv_activL = new CLabel("Activo");
  CComboBox prv_activE = new CComboBox();
  GridBagLayout gridBagLayout2 = new GridBagLayout();

  public ayuPrv(EntornoUsuario e,JLayeredPane fr)
  {
    this(e,fr,null);
  }

  /**
  *  Recibe el Entorno Usuario, el Principal
     * @param e
     * @param fr
     * @param dt
  */
 public ayuPrv(EntornoUsuario e, JLayeredPane fr, DatosTabla dt)
 {
   setTitulo("Ayuda Proveedores");
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
    prv_nombL.setBounds(2,2,47,17);
    prv_nombE.setBounds(52,2,200,17);
    prv_nombL.setText("Nombre");
    prv_activL.setBounds(2,20,47,17);
    prv_activE.addItem("Si","S");
    prv_activE.addItem("No","N");
    prv_activE.addItem("*","*");
    prv_activE.setBounds(52,20,47,17);
    Baceptar.setText("F4 Buscar");
    Baceptar.setBounds(300,4,133,28);
    Baceptar.setMargin(new Insets(0, 0, 0, 0));
    Baceptar.setMnemonic('B');

    Pcons.setLayout(null);
    Pprinc.setLayout(gridBagLayout2);

    // Configurando el Grid.
    ArrayList cabecera = new ArrayList();
    cabecera.add("Codigo"); // 0 -- Codigo
    cabecera.add("Nombre"); //1 -- Nombre
    cabecera.add("Nomb. Comercial"); // 2 -- Familia
    jt.setCabecera(cabecera);
    jt.setAnchoColumna(new int[]{56,283,283});
    jt.setFormatoColumna(0,"#####9");
    jt.setAlinearColumna(new int[]{2,0,0});
    jt.setAjustarGrid(true);
    jt.setNumRegCargar(100);
    jt.setAjustarGrid(true);
//    jt.setIgnoraColumna(0,true);
    jt.setMaximumSize(new Dimension(455, 265));
    jt.setMinimumSize(new Dimension(455, 265));
    jt.setPreferredSize(new Dimension(455, 265));

    Belegir.setEnabled(false);
    Pprinc.setDefButton(Belegir);
    Belegir.setMargin(new Insets(0, 0, 0, 0));
    Belegir.setToolTipText("Elegir el Registro Activo");

        // Poniendo Orejas.
    Belegir.addActionListener(new java.awt.event.ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Belegir_actionPerformed();
      }
    });
    Baceptar.addActionListener(new java.awt.event.ActionListener() {
         @Override
      public void actionPerformed(ActionEvent e) {
        Baceptar_actionPerformed();
      }
    });

    jt.tableView.addMouseListener(new MouseAdapter()
    {
         @Override
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
           if (e.getKeyCode() == KeyEvent.VK_INSERT || e.getKeyCode() == KeyEvent.VK_ENTER)
             Belegir_actionPerformed();
         }
    });
    Belegir.setToolTipText("Elegir el Registro Activo");

  
    prv_nombE.setMayusc(true);
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
    Pprinc.add(jt,  new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 1, 0, 5), 0, 0));
    Pprinc.add(Pcons,     new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 1, 5, 5), 0, 0));
    Pcons.add(prv_nombE, null);
    Pcons.add(prv_nombL, null);
    Pcons.add(Baceptar,  null);
    Pcons.add(prv_activL, null);
    Pcons.add(prv_activE, null);
    Belegir.setBounds(200,0,25,22);
    jt.panelBuscar.add(Belegir,null);//, new XYConstraints(200,0, 25, 22));

  }
    @Override
  public void iniciarVentana() throws Exception
  {
     Pcons.setDefButton(Baceptar);
     Pcons.setButton(KeyEvent.VK_F4, Baceptar);
     statusBar.setEnabled(true);
     prv_nombE.setColumnaAlias("prv_nomb");
     prv_nombE.setQuery(true);
     prv_nombE.requestFocusLater();
  }
  void Baceptar_actionPerformed()
  {
    if (prv_nombE.isNull())
    {
      mensajeErr("Introduzca algún Nombre de Proveedor");
      prv_nombE.requestFocus();
      return;
    }

    strSql = "select prv_codi,prv_nomb,prv_nomco FROM v_proveedo " +
        " where upper(prv_nomb) like '%" + prv_nombE.getText() + "%'" +
        (prv_activE.getValor().equals("*")?"":" and prv_activ = '"+prv_activE.getValor()+"'")+
        " order by prv_codi";

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
        mensaje(" -- NO ENCONTRADOS Proveedores con esas Condiciones -- ");
        return true;
      }
    }
    catch (Throwable k)
    {
      fatalError("Error al Buscar Proveedores: ", dtCon1.SqlException);
      return false;
    }
    //	Llenar el Grid con la Base de Datos.
    Belegir.setEnabled(true);

    jt.setEnabled(false);
    jt.setDatos(dtCon1);
    jt.setEnabled(true);
    jt.requestFocusInicio();
    mensaje("Proveedores Seleccionados ...");

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
    prv_nombT=jt.getValString(1);
    prv_codiT=jt.getValString(0);
    consulta=true;
    matar();
  }

  } // Final de Clase


