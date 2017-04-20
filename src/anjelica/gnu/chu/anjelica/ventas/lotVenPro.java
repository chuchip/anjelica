package gnu.chu.anjelica.ventas;

import gnu.chu.utilidades.*;
import gnu.chu.sql.*;
import gnu.chu.controles.*;
import java.awt.*;
import javax.swing.BorderFactory;
import java.util.*;
import javax.swing.SwingConstants;
import java.sql.Types;
import javax.swing.event.*;
import gnu.chu.anjelica.despiece.utildesp;
import gnu.chu.anjelica.compras.PDetComp;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
/**
 *
 * <p>Título: lotVenPro</p>
 * <p>Descripción: Muestra de donde proviene un individuo vendido en un albaran.
 * Muestra el proveedor, Fecha de Recepcion y Precio de Compra.</p>
 *
* <p>Copyright: Copyright (c) 2005-2011
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
 * <p>Empresa: miSL</p>
 * @author chuchi P
 * @version 1.0
 */

public class lotVenPro extends ventana
{
  boolean swSelected=false;
  utildesp utDesp;
  String s;
  CPanel Pprinc = new CPanel();
  CPanel Pcabec = new CPanel();
  CLabel cLabel1 = new CLabel();
  CTextField pro_codiE = new CTextField(java.sql.Types.DECIMAL,"#####9");
  CLabel pro_nombL = new CLabel();
  Cgrid jt = new Cgrid(7);
  DatosTabla dt = new DatosTabla();
  DatosTabla dt1 = new DatosTabla();
  CLabel cLabel2 = new CLabel();
  CLabel avc_anoL = new CLabel();
  CLabel cLabel3 = new CLabel();
  CLabel avl_numlinL = new CLabel();
  CLabel emp_codiL = new CLabel();
  CLabel avc_serieL = new CLabel();
  CLabel avc_numeL = new CLabel();
  CLabel cLabel4 = new CLabel();
  CTextField prv_codiE = new CTextField(Types.DECIMAL,"#####9");
  CLabel prv_nombE = new CLabel();
  CLabel cLabel5 = new CLabel();
  CTextField acc_numeE = new CTextField(Types.DECIMAL,"#####9");
  CLabel cLabel6 = new CLabel();
  CLabel acc_fecrecE = new CLabel();
  CLabel acc_serieE = new CLabel();
  CLabel cLabel7 = new CLabel();
  CTextField acl_prcomE = new CTextField(Types.DECIMAL,"---,--9.999");
  gnu.chu.anjelica.compras.PDetComp pdetCom;// = new CPanel();
  CLabel cLabel8 = new CLabel();
  CLabel acc_feccadE = new CLabel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CLabel cLabel9 = new CLabel();
  CTextField deo_codiE = new CTextField(Types.DECIMAL,"#####9");
  ventana papa;
  boolean iniciado=false;
  CCheckBox swCompraInt = new CCheckBox();
  private CButton Bescape=new CButton();
  
  public lotVenPro(ventana padre)
  {
    try
    {
      papa=padre;
      jbInit();
    }
    catch (Exception e)
    {
      ErrorInit(e);
    }
  }
  private void jbInit() throws Exception
  {

    this.setSize(new Dimension(447, 315));
    this.setIconifiable(false);
    this.setResizable(false);
    this.setMaximizable(false);
//      this.setBorder(null);
//    ((BasicInternalFrameUI)this.getUI()).setNorthPane(null); // Para quitar el titulo a la ventana

    setIniciado(false);
    statusBar=new StatusBar(this);
    this.add(statusBar,BorderLayout.SOUTH);
//    this.addPropertyChangeListener(new PropertyChangeListener()
//    {
//      public void propertyChange(PropertyChangeEvent evt)
//      {
//                cambiadaPropiedad();
//      }
//    });
    Pcabec.setBorder(BorderFactory.createRaisedBevelBorder());
    Pcabec.setPreferredSize(new Dimension(100, 45));
    Pcabec.setQuery(false);
    pdetCom = new PDetComp(papa);
    Vector v= new Vector();
    pro_codiE.setText("");
   cLabel2.setText("Albaran");
    cLabel2.setBounds(new Rectangle(3, 24, 58, 15));
    avc_anoL.setBounds(new Rectangle(64, 24, 45, 15));
    avc_anoL.setPreferredSize(new Dimension(42, 15));
    avc_anoL.setOpaque(true);

    avc_anoL.setForeground(Color.white);
    avc_anoL.setBackground(Color.orange);
    avc_anoL.setText("");
    avc_anoL.setHorizontalTextPosition(SwingConstants.LEADING);
    avc_anoL.setHorizontalAlignment(SwingConstants.RIGHT);
    cLabel3.setText("No. Linea");
    cLabel3.setBounds(new Rectangle(324, 24, 51, 15));
    avl_numlinL.setHorizontalAlignment(SwingConstants.RIGHT);
    avl_numlinL.setHorizontalTextPosition(SwingConstants.LEADING);
    avl_numlinL.setText("");
    avl_numlinL.setBackground(Color.orange);
    avl_numlinL.setForeground(Color.white);
    avl_numlinL.setOpaque(true);
    avl_numlinL.setPreferredSize(new Dimension(42, 15));
    avl_numlinL.setBounds(new Rectangle(376, 24, 39, 15));
    emp_codiL.setHorizontalAlignment(SwingConstants.RIGHT);
    emp_codiL.setHorizontalTextPosition(SwingConstants.LEADING);
    emp_codiL.setText("");
    emp_codiL.setBackground(Color.orange);
    emp_codiL.setForeground(Color.white);
    emp_codiL.setOpaque(true);
    emp_codiL.setPreferredSize(new Dimension(42, 15));
    emp_codiL.setBounds(new Rectangle(116, 24, 33, 15));
    avc_serieL.setHorizontalAlignment(SwingConstants.RIGHT);
    avc_serieL.setHorizontalTextPosition(SwingConstants.LEADING);
    avc_serieL.setText("");
    avc_serieL.setBackground(Color.orange);
    avc_serieL.setForeground(Color.white);
    avc_serieL.setOpaque(true);
    avc_serieL.setPreferredSize(new Dimension(42, 15));
    avc_serieL.setBounds(new Rectangle(154, 24, 17, 15));
    avc_numeL.setHorizontalAlignment(SwingConstants.RIGHT);
    avc_numeL.setHorizontalTextPosition(SwingConstants.LEADING);
    avc_numeL.setText("");
    avc_numeL.setBackground(Color.orange);
    avc_numeL.setForeground(Color.white);
    avc_numeL.setOpaque(true);
    avc_numeL.setPreferredSize(new Dimension(42, 15));
    avc_numeL.setBounds(new Rectangle(182, 24, 45, 15));
    cLabel4.setRequestFocusEnabled(true);
    cLabel4.setText("Proveedor");
    cLabel4.setBounds(new Rectangle(3, 4, 62, 16));
    prv_codiE.setBackground(Color.orange);
    prv_codiE.setOpaque(true);
    prv_codiE.setBounds(new Rectangle(64, 4, 62, 14));
    prv_nombE.setBounds(new Rectangle(138, 4, 287, 14));
    prv_nombE.setOpaque(true);
    prv_nombE.setText("");
    prv_nombE.setBackground(Color.orange);
    cLabel5.setText("Albaran");
    cLabel5.setBounds(new Rectangle(4, 20, 47, 14));
    acc_numeE.setBounds(new Rectangle(70, 20, 62, 14));
    acc_numeE.setHorizontalAlignment(SwingConstants.RIGHT);
    acc_numeE.setOpaque(true);
    acc_numeE.setBackground(Color.orange);
    cLabel6.setText("Fecha Alb.");
    cLabel6.setBounds(new Rectangle(148, 20, 61, 15));
    acc_fecrecE.setBounds(new Rectangle(210, 20, 79, 14));
    acc_fecrecE.setHorizontalAlignment(SwingConstants.CENTER);
    acc_fecrecE.setOpaque(true);
    acc_fecrecE.setBackground(Color.orange);
    acc_serieE.setBackground(Color.orange);
    acc_serieE.setOpaque(true);
    acc_serieE.setBounds(new Rectangle(50, 20, 17, 14));
    cLabel7.setText("Precio");
    cLabel7.setBounds(new Rectangle(302, 22, 37, 15));
    acl_prcomE.setBackground(Color.orange);
    acl_prcomE.setOpaque(true);
    acl_prcomE.setHorizontalAlignment(SwingConstants.RIGHT);
    acl_prcomE.setBounds(new Rectangle(344, 20, 79, 14));
    pdetCom.setBorder(BorderFactory.createLoweredBevelBorder());
    pdetCom.setMaximumSize(new Dimension(429, 58));
    pdetCom.setMinimumSize(new Dimension(429, 58));
    pdetCom.setPreferredSize(new Dimension(429, 58));

    cLabel8.setText("Fecha Caducidad");
    cLabel8.setBounds(new Rectangle(3, 37, 103, 14));
    acc_feccadE.setBackground(Color.orange);
    acc_feccadE.setOpaque(true);
    acc_feccadE.setHorizontalAlignment(SwingConstants.CENTER);
    acc_feccadE.setText("");
    acc_feccadE.setBounds(new Rectangle(100, 37, 79, 14));
    jt.setMaximumSize(new Dimension(426, 142));
    jt.setMinimumSize(new Dimension(426, 142));
    jt.setPreferredSize(new Dimension(426, 142));
    cLabel9.setBounds(new Rectangle(198, 37, 61, 14));
    cLabel9.setText("Despiece");
    deo_codiE.setBackground(Color.orange);
    deo_codiE.setOpaque(true);
    deo_codiE.setHorizontalAlignment(SwingConstants.RIGHT);
    deo_codiE.setBounds(new Rectangle(253, 37, 62, 14));
    swCompraInt.setBackground(Color.orange);
    swCompraInt.setText("Compra Int");
    swCompraInt.setEnabled(false);
    swCompraInt.setBounds(new Rectangle(327, 37, 99, 14));
    v.add("Ejerc"); // 0
    v.add("Empre"); // 1
    v.add("Serie"); // 2
    v.add("Lote"); // 3
    v.add("Indi"); // 4
    v.add("Unid"); //5
    v.add("Cantidad"); //6
    jt.setCabecera(v);
    jt.setFormatoColumna(5, "#99");
    jt.setFormatoColumna(6, "---,--9.99");
    jt.setAlinearColumna(new int[] {2, 2, 1, 2, 2, 2,2});
    jt.setAnchoColumna(new int[]{40,40,42,40,54,50,87});
   
    Bescape.addActionListener(new ActionListener() {
     public void actionPerformed(ActionEvent e) {
        matar();
     }
   });
    jt.setButton(KeyEvent.VK_ESCAPE, Bescape);
    Pprinc.setButton(KeyEvent.VK_ESCAPE, Bescape);
    Pcabec.setLayout(null);
    cLabel1.setText("Producto");
    cLabel1.setBounds(new Rectangle(4, 7, 55, 17));
    pro_codiE.setBackground(Color.orange);
    pro_codiE.setForeground(Color.white);
    pro_codiE.setOpaque(true);
    pro_codiE.setPreferredSize(new Dimension(42, 15));
    pro_codiE.setBounds(new Rectangle(64, 5, 66, 16));
    pro_nombL.setBounds(new Rectangle(135, 5, 281, 16));
    pro_nombL.setPreferredSize(new Dimension(42, 15));
    pro_nombL.setText("");
    pro_nombL.setOpaque(true);
    pro_nombL.setForeground(Color.white);
    pro_nombL.setBackground(Color.orange);
    Pprinc.setLayout(gridBagLayout1);

    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
    Pprinc.add(jt,   new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 0, 0, 0), 0, 0));
    Pprinc.add(pdetCom,    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

    this.getContentPane().add(Pcabec, BorderLayout.NORTH);

    Pcabec.add(cLabel1, null);
    Pcabec.add(pro_codiE, null);
    Pcabec.add(pro_nombL, null);
    Pcabec.add(avc_serieL, null);
    Pcabec.add(avc_numeL, null);
    Pcabec.add(cLabel3, null);
    Pcabec.add(avl_numlinL, null);
    Pcabec.add(avc_anoL, null);
    Pcabec.add(cLabel2, null);
    Pcabec.add(emp_codiL, null);

    utDesp=new utildesp();
    utDesp.setSaltaDespInt(true);
    Pcabec.setEnabled(false);
    pdetCom.setEnabled(false);
    jt.tableView.getSelectionModel().addListSelectionListener(new
        ListSelectionListener()
    {
      @Override
      public void valueChanged(ListSelectionEvent e)
      {
        try {
          verDatCom();
        } catch (Exception k)
        {
          papa.Error("Error al ver datos de Compra",k);
        }
      }
    });
    this.setVisible(false);
  }
  public void setIniciado(boolean inic)
  {
    iniciado=inic;
    swSelected=false;
  }
  public void cargaDatos(conexion ct,String proCodi,String proNomb,int ejeNume,int empCodi,
                         String avcSerie,int numalb,int numlin,double precio) throws Exception
  {
    jt.setEnabled(false);
    dt.setConexion(ct);
    dt1.setConexion(ct);
    pro_codiE.setText(Formatear.format(proCodi,"####9"));
    pro_nombL.setText(proNomb);
    avc_anoL.setText(""+ejeNume);
    emp_codiL.setText(""+empCodi);
    avc_serieL.setText(avcSerie);
    avc_numeL.setText(""+numalb);
    avl_numlinL.setText(""+numlin);

    s = "select A.* from v_albvenpar a,v_albavel l  where " +
        " A.avc_ano = " + ejeNume +
        " and A.emp_codi = " + empCodi +
        " and A.avc_serie = '" + avcSerie + "'" +
        " and A.avc_nume = " + numalb+
        " and l.emp_codi = a.emp_codi "+
        " and l.avc_ano = a.avc_ano "+
        " and l.avc_serie = a.avc_serie "+
        " and l.avc_nume = a.avc_nume "+
        " and l.avl_numlin = a.avl_numlin " ;
    if (numlin >= 0)
      s += " and a.avl_numlin = " + numlin;
    else
      s+=" and l.pro_codi = " + proCodi +
          " and l.avl_prven = "+precio;
    s+=" order by l.avl_numlin,avp_numlin";
//    System.out.println("s: "+s);
    jt.removeAllDatos();
    if (! dt.select(s))
    {
      mensajeErr("No encontrado partida de este albaran");
      prv_codiE.setText("");
      prv_nombE.setText("");
      acc_fecrecE.setText("");
      acc_serieE.setText("");
      acc_numeE.setText("");
      acl_prcomE.setText("");

      this.setEnabled(true);
//      statusBar.setEnabled(true);
      return;
    }
    do
    {
      ArrayList v=new ArrayList();

      v.add(dt.getString("avp_ejelot")); // 0
      v.add(dt.getString("avp_emplot")); // 1
      v.add(dt.getString("avp_serlot")); // 2
      v.add(dt.getString("avp_numpar")); // 3
      v.add(dt.getString("avp_numind"));
      v.add(dt.getString("avp_numuni"));
      v.add(dt.getString("avp_canti"));
      jt.addLinea(v);
    } while (dt.next());
    this.setSelected(true);
    jt.requestFocusInicio();
    this.setEnabled(true);
    jt.setEnabled(true);
    verDatCom();
    
  }

  void verDatCom() throws Exception
  {
   if (jt.isVacio())
     return;
//   debug("verdatCom: "+jt.isEnabled());
   if (!jt.isEnabled())
     return;
    utDesp.busDatInd(jt.getValString(2),pro_codiE.getValorInt(),jt.getValorInt(1),jt.getValorInt(0),
                    jt.getValorInt(3),jt.getValorInt(4),dt,dt1,papa.EU);


    if (utDesp.prvCodi==0)
    {
      mensajeErr("No encontrados datos de Compra");
      pdetCom.resetTexto();
      return;
    }
    pdetCom.setUtilDesp(utDesp,dt);

    mensajeErr("Actualizados datos de Compra",false);
  }
 
  


}
