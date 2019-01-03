package gnu.chu.anjelica.compras;
/**
 * <p>Titulo:   PDFACCOM </p>
 * <p>Descripción: Mantenimiento FACTURAS DE COMPRAS
 * <p>Copyright: (c) 2005-2018
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
*  los términos de la Licencia Pública General de GNU según es publicada por
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
 * @version 1.2 (Incluido soporte para control de pagos)
 */
import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.listados.Listados;
import gnu.chu.anjelica.pad.pdejerci;
import gnu.chu.anjelica.sql.Albacoc;
import gnu.chu.camposdb.empPanel;
import gnu.chu.camposdb.proPanel;
import gnu.chu.camposdb.prvPanel;
import gnu.chu.controles.*;
import gnu.chu.interfaces.PAD;
import gnu.chu.sql.DatosTabla;
import gnu.chu.sql.conexion;
import gnu.chu.utilidades.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;
import net.sf.jasperreports.engine.*;


public class pdfaccom extends ventanaPad   implements PAD,JRDataSource
{
  private String fccFacprvOLD="";
  private boolean swConsulta=false;
  String s;
  int numDec=2;
  int nLin=0;
  boolean modPrecio=false;
  boolean admin=false;
  CButton  Bimpri   = new CButton(Iconos.getImageIcon("print"));
  CPanel Pinicial = new CPanel();
  CPanel Pcabe = new CPanel();
  CLabel cLabel1 = new CLabel();
  prvPanel prv_codiE = new prvPanel();
  CLabel cLabel2 = new CLabel();
  CComboBox lvt_tippagE=new CComboBox();
  CTextField eje_numeE = new CTextField(Types.DECIMAL,"###9");
  CTextField fcc_numeE = new CTextField(Types.DECIMAL,"#####9");
  CLabel cLabel3 = new CLabel();
  CLabel cLabel4 = new CLabel();
  CTextField fcc_fecfraE = new CTextField(Types.DATE,"dd-MM-yy");
  CCheckBox fcc_traspE = new CCheckBox("0","-1");
  CPanel cPanel3 = new CPanel();
  CLabel cLabel5 = new CLabel();
  TitledBorder titledBorder2=new TitledBorder("");
  CTextField fcc_facprvE = new CTextField(Types.CHAR,"X",12);
  CLabel cLabel6 = new CLabel();
  CTextField fcc_fefapvE = new CTextField(Types.DATE,"dd-MM-yy");
  CLabel div_codiL = new CLabel();
  CLinkBox div_codiE = new CLinkBox();
  CPanel Ppie = new CPanel();
  CLabel fcc_kilfraL = new CLabel("Kilos Fra");
  CTextField fcc_kilfraE = new CTextField(Types.DECIMAL,"---,--9.99");
  CLabel fcc_implinL = new CLabel();
  CTextField fcc_implinE = new CTextField(Types.DECIMAL,"----,--9.99");

  CLabel fcc_imptotL = new CLabel();
  CTextField fcc_imptotE = new CTextField(Types.DECIMAL,"----,--9.99");
  CLabel fcc_dtoppL = new CLabel();
  CTextField fcc_dtoppE = new CTextField(Types.DECIMAL,"#9.99");
  CLabel cLabel7 = new CLabel();
  CTextField fcc_dtocomE = new CTextField(Types.DECIMAL,"#9.99");
  CLabel cLabel8 = new CLabel();
  CTextField fcc_piva1E = new CTextField(Types.DECIMAL,"#9.99");
  CLabel cLabel9 = new CLabel();
  CTextField fcc_impiv1E = new CTextField(Types.DECIMAL,"----,--9.99");
  CLabel cLabel10 = new CLabel();
  CTextField fcc_prec1E = new CTextField(Types.DECIMAL,"#9.99");
  CTextField fcc_impre1E = new CTextField(Types.DECIMAL,"----,--9.99");
  CLabel cLabel11 = new CLabel();
  CLabel cLabel12 = new CLabel();
  CTextField fcc_kglinE = new CTextField(Types.DECIMAL,"----,--9.99");
  CLabel cLabel13 = new CLabel();
  CTextField fcc_numuniE = new CTextField(Types.DECIMAL,"###9");
  CLabel cLabel14 = new CLabel();
  CTextField fcc_numlinE = new CTextField(Types.DECIMAL,"###9");
//  CButton Baceptar = new CButton("Aceptar",Iconos.getImageIcon("check"));
//  CButton Bcancelar = new CButton("Cancelar",Iconos.getImageIcon("cancel"));
  CGridEditable jtFra = new CGridEditable(15)
  {
        @Override
    public void cambiaColumna(int col,int colNueva, int row)
    {
      try
      {
         if (col == 0 && jtFra.isEnabled())
         {
           String nombArt;
           nombArt = pro_codiE.getNombArt(pro_codiE.getText(), EU.em_cod);
           jtFra.setValor(nombArt, row, 1);
//           pro_nombE.setText(nombArt);
         }
      } catch (Exception k)
      {
        Error("Error en Cambia columna de JtFra",k);
      }
    }
        @Override
    public boolean deleteLinea(int row, int col)
    {
      try {
        insLinAlb(row);
      } catch (Exception k)
      {
        Error("Error al Insertar Linea Fra. en Alb",k);
      }
      return true;
    }

        @Override
    public void afterDeleteLinea()
    {
      try
      {
        actAcumFra();
      }
      catch (Exception k)
      {
        Error("Error al Actualizar Acumulado Factura", k);
      }
    }

        @Override
    public int cambiaLinea(int row, int col)
    {
      return cambiaLinFra();
    }
        @Override
    public void afterCambiaLinea()
    {
      try
      {
        boolean edit=true;
        if (acl_nulinE.getValorInt()==0)
            edit=true;
        acc_numeE.setEditable(edit);
        acc_serieE.setEditable(edit);
        acc_anoE.setEditable(edit);
        actAcumFra();
      } catch (Exception k)
      {
        Error("Error al Actualizar Acumulado de Factura",k);
      }
    }
  };
  JTabbedPane Tprinci = new JTabbedPane();
  CPanel PFactura = new CPanel();
  CPanel Palbar = new CPanel();
  Cgrid jtAlb = new Cgrid(12)
  {
        @Override
    protected void Binser_actionPerformed(boolean b)
    {
      try {
        insLinFra(jtAlb.getSelectedRow());
      } catch (Exception k)
      {
        Error("Error al Insertar Linea de Albaran",k);
      }
    }
  };
  BorderLayout borderLayout2 = new BorderLayout();
  CLabel cLabel15 = new CLabel();
  empPanel emp_codiE = new empPanel();
  CLabel cLabel16 = new CLabel();
  CLinkBox fpa_codiE = new CLinkBox();
  CPanel Pvtos = new CPanel();
  CPanel cPanel1 = new CPanel();
  pLibVto jtVto;
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CTextField lvt_fechaE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CTextField lvt_imporE = new CTextField(Types.DECIMAL,"---,--9.99");
  CButton BirGrid = new CButton();
  proPanel pro_codiE = new proPanel();
  CTextField pro_nombE = new CTextField();
  CTextField fcl_cantiE = new CTextField(Types.DECIMAL,"---,--9.99");
  CTextField fcl_prcomE = new CTextField(Types.DECIMAL,"--,--9.999");
  CTextField fcl_tipdesE = new CTextField(Types.CHAR,"X");
  CTextField fcl_dtoE = new CTextField(Types.DECIMAL,"##9.99");
  CTextField fcl_implinE = new CTextField(Types.DECIMAL,"----,--9.99");
  CTextField acl_cantiE = new CTextField(Types.DECIMAL,"---,--9.99");
  CTextField acl_prcomE = new CTextField(Types.DECIMAL,"--,--9.999");
  CTextField acc_serieE = new CTextField(Types.CHAR,"X",1);
  CTextField acc_anoE = new CTextField(Types.DECIMAL,"###9");
  CTextField acc_numeE = new CTextField(Types.DECIMAL,"####9");
  CTextField acc_fecalbE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CTextField acl_nulinE = new CTextField(Types.DECIMAL,"##9");
  CTextField acl_numcajE = new CTextField(Types.DECIMAL,"##9");
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  CLabel cLabel17 = new CLabel();
  CButton BinsAlb = new CButton(Iconos.getImageIcon("insertar"));
  CComboBox alc_numinsE = new CComboBox();
  CLabel fcc_implinL1 = new CLabel();
  CTextField fcc_basim1E = new CTextField(Types.DECIMAL,"----,--9.99");
  conexion ctCom;
  CLabel cLabel18 = new CLabel();
  CComboBox fcc_conpagE = new CComboBox();
  CLabel cLabel19 = new CLabel();
  CTextField fcc_comentE = new CTextField(Types.CHAR,"X",150);
  CCheckBox fcc_pagadoE = new CCheckBox();
  CPanel Palb = new CPanel();
  CLabel cLabel20 = new CLabel();
  CComboBox verAlbaE = new CComboBox();
  GridBagLayout gridBagLayout3 = new GridBagLayout();

  public pdfaccom(EntornoUsuario eu, Principal p)
 {
   this(eu,p,null);
 }
 public pdfaccom(EntornoUsuario eu, Principal p,Hashtable ht)
 {
   EU = eu;
   vl = p.panel1;
   jf = p;
   eje = true;

   try
   {
     if (ht != null)
     {

       if (ht.get("modPrecio") != null)
         modPrecio = Boolean.valueOf(ht.get("modPrecio").toString()).
             booleanValue();
       if (ht.get("admin") != null)
         admin = Boolean.valueOf(ht.get("admin").toString()).
             booleanValue();
        if (ht.get("consulta") != null)
         swConsulta = Boolean.valueOf(ht.get("consulta").toString()).
             booleanValue();
       if (admin)
         modPrecio=true;
     }
     setTitulo("Mantenimiento Facturas Compras");
     if (jf.gestor.apuntar(this))
       jbInit();
     else
       setErrorInit(true);
   }
   catch (Exception e)
   {
     ErrorInit(e);
   }
 }

 public pdfaccom(gnu.chu.anjelica.menu p, EntornoUsuario eu)
 {
   this(p,eu,null);
 }
 public pdfaccom(gnu.chu.anjelica.menu p, EntornoUsuario eu,Hashtable ht)
 {
   EU = eu;
   vl = p.getLayeredPane();
   eje = false;
   setTitulo("Mantenimiento Facturas Compras");
   try
   {
     if (ht != null)
     {

       if (ht.get("modPrecio") != null)
         modPrecio = Boolean.valueOf(ht.get("modPrecio").toString()).
             booleanValue();
       if (ht.get("admin") != null)
         admin = Boolean.valueOf(ht.get("admin").toString()).
             booleanValue();
       if (ht.get("consulta") != null)
         swConsulta = Boolean.valueOf(ht.get("consulta").toString()).
             booleanValue();
       if (admin)
         modPrecio=true;
     }
     jbInit();
   }
   catch (Exception e)
   {
    ErrorInit(e);
   }
 }

 private void jbInit() throws Exception
 {

   iniciarFrame();
   this.setSize(new Dimension(764, 531));
   this.setVersion("2018-05-08 "+(modPrecio?"-Modificar Precios-":"")+
         (admin?"-ADMINISTRADOR-":"")+ (swConsulta?"-Solo Consulta-":""));
   strSql = "SELECT emp_codi,eje_nume,fcc_nume " +
       " FROM v_facaco WHERE emp_codi = " + EU.em_cod +
       " ORDER BY eje_nume,fcc_nume ";


   statusBar = new StatusBar(this);
   nav = new navegador(this, dtCons, false,  navegador.NORMAL);
   if (swConsulta)
   {
       nav.removeBoton(navegador.EDIT);
       nav.removeBoton(navegador.DELETE);
       nav.removeBoton(navegador.ADDNEW);
   }
   jtVto=new pLibVto(statusBar);
   conecta();
   iniciar(this);
   Bimpri.setPreferredSize(new Dimension(24, 24));
   Bimpri.setMaximumSize(new Dimension(24, 24));
   Bimpri.setMinimumSize(new Dimension(24, 24));
   Bimpri.setToolTipText("Imprimir Factura Compra");
   alc_numinsE.setMinimumSize(new Dimension(32, 24));
    alc_numinsE.setBounds(new Rectangle(523, 27, 155, 18));
    cLabel18.setText("Conf. Pago");
    cLabel18.setBounds(new Rectangle(4, 49, 67, 18));
    fcc_conpagE.setBounds(new Rectangle(74, 49, 56, 18));
    cLabel19.setText("Coment");
    cLabel19.setBounds(new Rectangle(135, 49, 50, 18));
    fcc_comentE.setText("");
    fcc_comentE.setBounds(new Rectangle(187, 49, 397, 18));
    fcc_kilfraL.setBounds(new Rectangle(588, 49, 80, 18));
    fcc_kilfraE.setBounds(new Rectangle(647, 49, 60, 18));
    BirGrid.setBounds(new Rectangle(405, 34, 5, 5));
    BinsAlb.setBounds(new Rectangle(685, 27, 24, 20));
    cLabel17.setBounds(new Rectangle(424, 27, 95, 18));
    prv_codiE.setBounds(new Rectangle(72, 27, 327, 18));
    cLabel1.setBounds(new Rectangle(3, 27, 65, 18));
    cPanel3.setBounds(new Rectangle(403, 3, 305, 22));
    eje_numeE.setBounds(new Rectangle(133, 2, 36, 18));
    emp_codiE.setBounds(new Rectangle(52, 3, 47, 18));
    fcc_numeE.setBounds(new Rectangle(213, 3, 57, 18));
    cLabel3.setBounds(new Rectangle(170, 3, 49, 18));
    cLabel2.setBounds(new Rectangle(100, 2, 35, 18));
    cLabel15.setBounds(new Rectangle(3, 3, 50, 18));
    fcc_fecfraE.setBounds(new Rectangle(334, 3, 65, 18));
    cLabel4.setBounds(new Rectangle(271, 3, 59, 18));
    fcc_pagadoE.setText("Pagado");
    fcc_pagadoE.setBounds(new Rectangle(631, 23, 81, 18));
    fcc_pagadoE.setEnabled(false);
    Palb.setMaximumSize(new Dimension(350, 20));
    Palb.setMinimumSize(new Dimension(350, 20));
    Palb.setPreferredSize(new Dimension(350, 20));
    Palb.setLayout(null);
    cLabel20.setMaximumSize(new Dimension(350, 15));
    cLabel20.setMinimumSize(new Dimension(350, 15));
    cLabel20.setPreferredSize(new Dimension(350, 15));
    cLabel20.setText("Ver Lineas de Albaran");
    cLabel20.setBounds(new Rectangle(1, 1, 126, 17));

    verAlbaE.setBounds(new Rectangle(137, 1, 169, 17));
    fcc_fefapvE.setText("01-01-08   ");
    statusBar.add(Bimpri, new GridBagConstraints(9, 0, 1, 2, 0.0, 0.0
                                              , GridBagConstraints.EAST,
                                              GridBagConstraints.VERTICAL,
                                              new Insets(0, 5, 0, 0), 0, 0));

   fpa_codiE.setAceptaNulo(false);
//   emp_codiE.setEnabled(false);


   pro_nombE.setEnabled(false);
   fcl_tipdesE.setEnabled(false);
   fcl_tipdesE.setText("%");
   fcl_implinE.setEnabled(false);
   acl_cantiE.setEnabled(false);
   acl_prcomE.setEnabled(false);
   acc_serieE.setMayusc(true);

//   acc_serieE.setEnabled(false);
//   acc_anoE.setEnabled(false);
//   acc_numeE.setEnabled(false);
   acc_fecalbE.setEnabled(false);
   acl_nulinE.setEnabled(false);
   acc_serieE.setText("A");
   acc_anoE.setValorInt(EU.ejercicio);
   ArrayList v = new ArrayList();
   v.add("Prod."); // 0
   v.add("Descr. Prod."); // 1
   v.add("Cantidad"); // 2
   v.add("Precio"); // 3
   v.add("T.Dto"); // 4
   v.add("Dto"); // 5
   v.add("Imp.Linea"); // 6
   v.add("Kg.Alb"); // 7
   v.add("Pr.Alb."); // 8
   v.add("SA"); // 9
   v.add("Eje."); // 10
   v.add("Alb."); // 11
   v.add("N.Uni"); // 12
   v.add("Fec.Alb"); // 13
   v.add("L.Alb"); // 14
   jtFra.setCabecera(v);
   jtFra.setAlinearColumna(new int[]{2,0,2,2,1,2,2,2,2,1,2,2,2,1,2});
   jtFra.setAnchoColumna(new int[]{50,120,60,60,40,60,80,70,70,30,40,80,50,80,50} );
   jtFra.setFormatoColumna(2,"---,--9.99");
   jtFra.setFormatoColumna(3,"--,--9.999");
   jtFra.setFormatoColumna(5,"##9.99");
   jtFra.setFormatoColumna(6,"----,--9.99");
   jtFra.setFormatoColumna(7,"---,--9.99");
   jtFra.setFormatoColumna(8,"--,--9.999");
   jtFra.setFormatoColumna(12,"##9");
   jtFra.setFormatoColumna(14,"##9");
   jtFra.setPonValoresInFocus(true);
   acl_numcajE.setEnabled(false);

   jtFra.setAjustarGrid(true);
   jtFra.ajustar(false);
   jtAlb.setBotonInsert();
   jtAlb.Binser.setToolTipText("Insertar Linea Albaran en Factura (F7)");
   ArrayList vc=new ArrayList();

   vc.add(pro_codiE.getTextField()); //0
   vc.add(pro_nombE); // 1
   vc.add(fcl_cantiE); // 2
   vc.add(fcl_prcomE); // 3
   vc.add(fcl_tipdesE); // 4
   vc.add(fcl_dtoE); // 5
   vc.add(fcl_implinE); // 6
   vc.add(acl_cantiE); // 7
   vc.add(acl_prcomE);// 8
   vc.add(acc_serieE);// 9
   vc.add(acc_anoE);// 10
   vc.add(acc_numeE);// 11
   vc.add(acl_numcajE);// 12
   vc.add(acc_fecalbE); // 13
   vc.add(acl_nulinE);// 14
   jtFra.setCampos(vc);

   ArrayList v1 = new ArrayList();
   v1.add("Prod."); // 0
   v1.add("Descr. Prod."); // 1
   v1.add("Un."); // 2
   v1.add("Cantidad"); // 3
   v1.add("Precio"); // 4
   v1.add("Imp.Linea"); // 5
   v1.add("Serie"); // 6
   v1.add("Eje."); // 7
   v1.add("Alb."); // 8
   v1.add("Fec.Alb"); // 9
   v1.add("L.Alb"); // 10 Linea Albaran
   v1.add("TF"); // 11 Totalmente Fact.
   jtAlb.setCabecera(v1);

   jtAlb.setAlinearColumna(new int[]{2,0,2,2,2,2,1,2,2,1,2,1});
   jtAlb.setAnchoColumna(new int[]
                         {50, 120, 40, 60, 50,  70, 30, 40, 60, 80,
                         50,30});
   jtAlb.setFormatoColumna(2, "---9");
   jtAlb.setFormatoColumna(3, "---,--9.99");
   jtAlb.setFormatoColumna(4, "--,--9.999");
   jtAlb.setFormatoColumna(5, "---,--9.99");
   jtAlb.setFormatoColumna(11, "BSN");
   jtAlb.setAjustarGrid(true);
   ArrayList vv=new ArrayList();
   vv.add("Fecha");
   vv.add("T.Pago");
   vv.add("Importe");

   fcc_traspE.setEnabled(false);
    fcc_traspE.setBounds(new Rectangle(631, 3, 108, 18));
   fcc_numuniE.setEnabled(false);
    fcc_numuniE.setBounds(new Rectangle(508, 23, 31, 16));
   fcc_numlinE.setEnabled(false);
    fcc_numlinE.setBounds(new Rectangle(597, 23, 34, 16));

   Pcabe.setBorder(BorderFactory.createRaisedBevelBorder());
    Pcabe.setMaximumSize(new Dimension(722, 70));
    Pcabe.setMinimumSize(new Dimension(722, 70));
    Pcabe.setPreferredSize(new Dimension(722, 70));
    Pcabe.setLayout(null);
    Pinicial.setLayout(gridBagLayout2);
    cLabel1.setText("Proveedor");
    prv_codiE.setAncTexto(50);
//    prv_codiE.setVisibleBotonAlta(false);
//    prv_codiE.setVisibleBotonCons(false);
    cLabel2.setText("Ejerc.");
    cLabel3.setText("Factura");
    cLabel4.setText("Fecha Fra");
    fcc_traspE.setText("Traspasado");
    cPanel3.setBorder(BorderFactory.createLineBorder(Color.black));
    cPanel3.setText("Proveedor");
    cPanel3.setLayout(null);
    cLabel5.setText("Factura Prv");
    cLabel5.setBounds(new Rectangle(3, 1, 72, 17));
    titledBorder2.setTitleFont(new java.awt.Font("Lucida Console", 2, 12));
    titledBorder2.setTitle("Proveedor");
    cLabel6.setRequestFocusEnabled(true);
    cLabel6.setText("Fec.Fra Prv");
    cLabel6.setBounds(new Rectangle(165, 2, 67, 17));
    div_codiL.setText("Divisa");
    div_codiL.setBounds(new Rectangle(211, 41, 37, 19));
    div_codiE.setAncTexto(30);
    div_codiE.setBounds(new Rectangle(256, 41, 166, 19));
    Ppie.setBorder(BorderFactory.createLoweredBevelBorder());
    Ppie.setMaximumSize(new Dimension(743, 75));
    Ppie.setMinimumSize(new Dimension(743, 75));
    Ppie.setPreferredSize(new Dimension(743, 75));
    Ppie.setLayout(null);
    fcc_implinL.setText("Imp. Lineas");
    fcc_implinL.setBounds(new Rectangle(4, 3, 70, 18));
    fcc_imptotL.setText("Total Fra");
    fcc_imptotL.setBounds(new Rectangle(465, 3, 57, 18));
    fcc_dtoppL.setText("% Dto PP");
    fcc_dtoppL.setBounds(new Rectangle(153, 3, 51, 18));
    cLabel7.setText("% Dto Com");
    cLabel7.setBounds(new Rectangle(153, 24, 57, 18));
    cLabel8.setText("% IVA");
    cLabel8.setBounds(new Rectangle(260, 3, 39, 18));
    cLabel9.setText("Imp. Iva");
    cLabel9.setBounds(new Rectangle(338, 3, 50, 18));
    cLabel10.setText("% RE");
    cLabel10.setBounds(new Rectangle(257, 24, 39, 18));
    cLabel11.setText("Imp.RE");
    cLabel11.setBounds(new Rectangle(335, 23, 50, 16));
    cLabel12.setText("Kg. Lineas");
    cLabel12.setBounds(new Rectangle(8, 24, 63, 18));
    cLabel13.setText("N.Unid.");
    cLabel13.setBounds(new Rectangle(465, 23, 46, 16));
    cLabel14.setText("N.Lineas");
    cLabel14.setBounds(new Rectangle(548, 23, 51, 16));
    PFactura.setLayout(borderLayout2);
    Palbar.setLayout(gridBagLayout3);
    cLabel15.setText("Empresa");
    cLabel16.setText("Forma Pago");
    cLabel16.setBounds(new Rectangle(10, 5, 66, 19));

    fpa_codiE.setAncTexto(30);
    fpa_codiE.setBounds(new Rectangle(81, 5, 247, 19));
    Pvtos.setLayout(gridBagLayout1);
    cPanel1.setBorder(BorderFactory.createLoweredBevelBorder());
    cPanel1.setMaximumSize(new Dimension(416, 27));
    cPanel1.setMinimumSize(new Dimension(416, 27));
    cPanel1.setPreferredSize(new Dimension(416, 27));
    cPanel1.setLayout(null);


    Tprinci.setMaximumSize(new Dimension(758, 337));
    Tprinci.setMinimumSize(new Dimension(758, 337));
    Tprinci.setPreferredSize(new Dimension(758, 337));
    Bcancelar.setBounds(new Rectangle(612, 44, 117, 25));
    Baceptar.setBounds(new Rectangle(453, 44, 127, 25));
    fcc_dtocomE.setBounds(new Rectangle(214, 24, 38, 18));
    fcc_impre1E.setBounds(new Rectangle(381, 23, 73, 16));
    fcc_prec1E.setBounds(new Rectangle(291, 23, 38, 16));
    fcc_kglinE.setBounds(new Rectangle(78, 24, 71, 18));
    fcc_dtoppE.setBounds(new Rectangle(214, 3, 38, 18));
    fcc_imptotE.setBounds(new Rectangle(534, 3, 73, 18));
    fcc_impiv1E.setBounds(new Rectangle(384, 3, 73, 18));
    fcc_piva1E.setBounds(new Rectangle(294, 3, 38, 18));
    fcc_implinE.setBounds(new Rectangle(75, 3, 73, 18));
    fcc_fefapvE.setBounds(new Rectangle(231, 2, 68, 17));
    fcc_facprvE.setBounds(new Rectangle(66, 1, 94, 17));
    cLabel17.setText("Insertar Albaran");
    BinsAlb.setToolTipText("Insertar Albaran");
    BinsAlb.setMargin(new Insets(0, 0, 0, 0));


    fcc_implinL1.setBounds(new Rectangle(4, 44, 70, 18));
    fcc_implinL1.setText("Base Impon.");
    fcc_basim1E.setBounds(new Rectangle(75, 44, 73, 18));
    this.getContentPane().add(Pinicial, BorderLayout.CENTER);
    Pinicial.add(Pcabe,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 0, 0, 0), 0, 0));
    jtVto.setMaximumSize(new Dimension(375, 267));
    jtVto.setMinimumSize(new Dimension(375, 267));
    jtVto.setPreferredSize(new Dimension(375, 267));

    cPanel3.add(cLabel5, null);
    cPanel3.add(fcc_facprvE, null);
    cPanel3.add(cLabel6, null);
    cPanel3.add(fcc_fefapvE, null);
    Pcabe.add(fcc_fecfraE, null);
    Pcabe.add(cLabel4, null);
    Pcabe.add(fcc_numeE, null);
    Pcabe.add(cLabel3, null);
    Pcabe.add(eje_numeE, null);
    Pcabe.add(cLabel2, null);
    Pcabe.add(BirGrid, null);
    Pcabe.add(alc_numinsE, null);
    Pcabe.add(cLabel17, null);
    Pcabe.add(prv_codiE, null);
    Pcabe.add(cLabel1, null);
    Pcabe.add(BinsAlb, null);
    Pcabe.add(cLabel18, null);
    Pcabe.add(fcc_conpagE, null);
    Pcabe.add(cLabel19, null);
    Pcabe.add(fcc_comentE, null);
    Pcabe.add(fcc_kilfraE, null);
    Pcabe.add(fcc_kilfraL, null);
    Pcabe.add(cLabel15, null);
    Pcabe.add(emp_codiE, null);
    Pcabe.add(cPanel3, null);
    Ppie.add(fcc_implinE, null);
    Ppie.add(fcc_dtoppE, null);
    Ppie.add(fcc_dtocomE, null);
    Ppie.add(cLabel7, null);
    Ppie.add(fcc_dtoppL, null);
    Ppie.add(cLabel8, null);
    Ppie.add(fcc_piva1E, null);
    Ppie.add(cLabel9, null);
    Ppie.add(fcc_impiv1E, null);
    Ppie.add(fcc_impre1E, null);
    Ppie.add(cLabel10, null);
    Ppie.add(fcc_prec1E, null);
    Ppie.add(cLabel11, null);
    Ppie.add(fcc_imptotE, null);
    Ppie.add(fcc_imptotL, null);
    Ppie.add(fcc_implinL, null);
    Ppie.add(cLabel13, null);
    Ppie.add(fcc_kglinE, null);
    Ppie.add(cLabel12, null);
    Ppie.add(fcc_traspE, null);
    Ppie.add(Bcancelar, null);
    Ppie.add(Baceptar, null);
    Ppie.add(div_codiE, null);
    Ppie.add(div_codiL, null);
    Ppie.add(fcc_basim1E, null);
    Ppie.add(fcc_implinL1, null);
    Ppie.add(fcc_numuniE, null);
    Ppie.add(cLabel14, null);
    Ppie.add(fcc_numlinE, null);
    Ppie.add(fcc_pagadoE, null);
    Pinicial.add(Tprinci,   new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 1, 0, 1), 0, 0));
    Pinicial.add(Ppie,   new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 1, 0, 1), 0, 0));
    Tprinci.add(PFactura,  "Factura");
    PFactura.add(jtFra, BorderLayout.CENTER);
    Tprinci.add(Palbar,   "Albaran");
    Palbar.add(jtAlb,     new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    Palbar.add(Palb,     new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    Palb.add(cLabel20, null);
    Palb.add(verAlbaE, null);
    Tprinci.add(Pvtos,   "Vencimientos");
    Pvtos.add(cPanel1,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(3, 0, 0, 0), -76, 0));
    cPanel1.add(fpa_codiE, null);
    cPanel1.add(cLabel16, null);
    Pvtos.add(jtVto,             new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(7, 0, 2, 0), 309, 0));
    Baceptar.setText("F4 Aceptar");
 }

    @Override
 public void iniciarVentana() throws Exception
 {
   Pcabe.setButton(KeyEvent.VK_F2, BirGrid);
   if (ctUp.getAutoCommit())
   { // No quiero que el cursor de Update Sea Autocommit
     ctCom = new conexion(EU.usuario, EU.password,
                          EU.driverDB,
                          EU.addressDB);
     ctCom.setAutoCommit(false);
     ctCom.setCatalog(EU.catalog);

     stUp = ctCom.createStatement();
   }
   else
     ctCom = ctUp;

   emp_codiE.iniciar(dtStat, this, vl, EU);
   Pcabe.setButton(KeyEvent.VK_F4, Baceptar);
   jtAlb.setButton(KeyEvent.VK_F4, Baceptar);

   lvt_tippagE.addItem("Giro", "G");
   lvt_tippagE.addItem("Talon", "T");
   lvt_tippagE.addItem("Efectivo", "E");

   eje_numeE.setColumnaAlias("eje_nume");
   fcc_numeE.setColumnaAlias("fcc_nume");
   fcc_fecfraE.setColumnaAlias("fcc_fecfra");
   fcc_facprvE.setColumnaAlias("fcc_facprv");
   fcc_fefapvE.setColumnaAlias("fcc_fefapv");
   prv_codiE.setColumnaAlias("prv_codi");
   fpa_codiE.setColumnaAlias("fpa_codi");
   fcc_comentE.setColumnaAlias("fcc_coment");
   fcc_conpagE.setColumnaAlias("fcc_conpag");
   fcc_kilfraE.setColumnaAlias("fcc_kilfra");
   activar(false);
   jtFra.setButton(KeyEvent.VK_F4, Baceptar);
   jtVto.jtVto.setButton(KeyEvent.VK_F4, Baceptar);
   Ppie.setDefButton(Baceptar);
   verDatos(dtCons);
   activarEventos();
 }
 private void activarEventos()
 {
   jtVto.getGridGrupo().addMouseListener(new MouseAdapter()
   {
            @Override
     public void mouseClicked(MouseEvent e)
     {
       if (jtVto.getGridGrupo().isEnabled() || jtVto.getGridGrupo().isVacio() )
         return;
       if (e.getClickCount() < 2 )
        return;
       if (jtVto.getGridGrupo().getSelectedColumn() == 0 || jtVto.getGridGrupo().getSelectedColumn() == 1 )
       {
                    try {
                        int row=jtVto.getGridGrupo().getSelectedRowDisab();
                        strSql = "SELECT emp_codi,eje_nume,fcc_nume " + 
                                " FROM v_facaco WHERE emp_codi = " + emp_codiE.getValorInt() +
                                " and fcc_nume = " + jtVto.getGridGrupo().getValorInt(row,1) +
                                " and eje_nume = " + jtVto.getGridGrupo().getValorInt(row,0);
                        if (!dtCons.select(strSql)) {
                            msgBox("No encontrada factura agrupada: " + 
                                    jtVto.getGridGrupo().getValorInt(row,0) + " - " +
                                    jtVto.getGridGrupo().getValorInt(row,1));
                            return;
                        }
                        verDatos(dtCons);
                        mensajeErr("Selecionada factura de grupo: "+ eje_numeE.getValorInt() + " - " +
                                    fcc_numeE.getValorInt());
                        return;
                    } catch (SQLException ex) {
                        Error("Error al buscar factura agrupada ",ex);
                    }
       }

     }
   });
//   jtAlb.addMouseListener(new MouseAdapter()
//   {
//     public void mouseClicked(MouseEvent e)
//     {
//       if (! verAlbaE.isEnabled()  || jtAlb.isVacio() || jtAlb.getSelectedColumn()==11)
//         return;
//       jtAlb.setValor(new Boolean(! jtAlb.getValBoolean(11)),11);
//       ponLinAlbFact( jtAlb.getValBoolean(11));
//     }
//   });

   verAlbaE.addActionListener(new ActionListener()
   {
     @Override
     public void actionPerformed(ActionEvent e)
     {
       try {
         if (verAlbaE.isEnabled())
           llenaGridAlb();
       } catch (Exception k)
       {
         Error("Error al recargar grid de Albaranes",k);
       }
     }
   });
   Bimpri.addActionListener(new ActionListener()
   {
     @Override
     public void actionPerformed(ActionEvent e)
     {
       Bimpri_actionPerformed();
     }
   });

   jtAlb.addMouseListener(new MouseAdapter()
   {
            @Override
     public void mouseClicked(MouseEvent e)
     {
       if (!jtAlb.isEnabled() || jtAlb.isVacio() )
         return;

       if (jtAlb.getSelectedColumn() == 11)
       {
         jtAlb.setValor(!jtAlb.getValBoolean(11), 11);
         ponLinAlbFact(jtAlb.getValBoolean(11));
         return;
       }
      if (e.getClickCount() >= 2)
        jtAlb.Binser.doClick();
     }
   });
   jtFra.addMouseListener(new MouseAdapter()
    {
            @Override
      public void mouseClicked(MouseEvent e)
      {
        if (jtFra.isEnabled()==false && (nav.pulsado==navegador.ADDNEW || nav.pulsado==navegador.EDIT))
          irGridFra();
      }
    });

   BinsAlb.addActionListener(new ActionListener()
   {
     @Override
     public void actionPerformed(ActionEvent e)
     {
       BinsAlb_actionPerformed();
     }
   });
   prv_codiE.addFocusListener(new FocusAdapter()
   {
            @Override
     public void focusLost(FocusEvent e)
     {
       if (prv_codiE.getQuery())
         return;
       try
       {
         cambioPrv();
       }
       catch (Exception k)
     {
       Error("Error al cambiar de proveedor",k);
     }
     }
   });
   BirGrid.addFocusListener(new FocusAdapter()
   {
            @Override
     public void focusGained(FocusEvent e)
     {
       irGridFra();
     }
   });
   fpa_codiE.addCambioListener(new gnu.chu.eventos.CambioListener()
   {
    	public void cambio(gnu.chu.eventos.CambioEvent event)
            {
               llenaGridVtos();
            }
/*     public void focusLost(FocusEvent e)
     {
       llenaGridVtos();
     }*/
   });
 }

 private void llenaGridVtos()
 {
   try {
   if (! fpa_codiE.hasCambio())
     return;
   fpa_codiE.resetCambio();
   jtVto.ponValoresDef(fcc_imptotE.getValorDec(),fpa_codiE.getValorInt(),fcc_fecfraE.getFecha("dd-MM-yyyy"));
   } catch (Exception k)
   {
     Error("Error al poner vtos por defecto",k);
   }
 }
 private void irGridFra()
 {
   if (! checkCabe())
     return;

   try
   {
     cambioPrv();
   }
   catch (Exception k)
   {
     Error("Error al ir al grid de Factura", k);
     return;
   }
   jtFra.setEnabled(true);
   jtAlb.setEnabled(true);
   alc_numinsE.setEnabled(true);
   BinsAlb.setEnabled(true);
   jtFra.requestFocusInicio();
 }

 boolean checkCabe()
 {
   if (fcc_fecfraE.getError() || fcc_fecfraE.isNull())
   {
     mensajeErr("Fecha Factura NO es Valida");
     fcc_fecfraE.requestFocus();
     return false;
   }

   if (fcc_fefapvE.getError() || fcc_fefapvE.isNull())
   {
     mensajeErr("Fecha Factura de Proveedor NO es Valida");
     fcc_fefapvE.requestFocus();
     return false;
   }
   try
   {
    


     if (! emp_codiE.controla())
     {
       mensajeErr("Empresa NO valida");
       return false;
     }
     if (!prv_codiE.controla(true))
     {
       mensajeErr(prv_codiE.getMsgError());
       return false;
     }
     if (prv_codiE.isInterno())
     {
       mensajeErr("No se puede crear una factura sobre un Proveedor Interno");
       return false;
     }
     s = pdejerci.checkFecha(dtStat, eje_numeE.getValorInt(), emp_codiE.getValorInt(),
                             fcc_fecfraE.getFecha("dd-MM-yyyy"));
     if (s != null)
     {
       mensajeErr(s);
       fcc_fecfraE.requestFocus();
       return false;
     }
     if (fcc_kilfraE.isNull())
     {
         int ret=mensajes.mensajeYesNo("Deberia Introduzca los kilos de factura. ¿Continuar?");
         if (ret!=mensajes.YES)
         {
            fcc_kilfraE.requestFocus();
            return false;
         }
     }
   }
   catch (Exception k)
   {
     Error("Error al chequear Proveedor", k);
     return false;
   }
   if (fcc_facprvE.isNull())
   {
     mensajeErr("Introduzca Codigo Factura de Proveedor");
     fcc_facprvE.requestFocus();
     return false;
   }
   if (! fpa_codiE.controla())
   {
     mensajeErr("Forma de Pago NO VALIDA");
     return false;
   }
   return true;
 }

 void llenaGridAlb() throws Exception
 {
   verAlbaE.setEnabled(true);
   alc_numinsE.setEnabled(false);
   alc_numinsE.removeAllItems();
   jtAlb.removeAllDatos();
   s="SELECT c.acc_impokg,c.acc_imcokg, c.acc_fecrec, l.*,p.pro_nomb "+
       " FROM v_albacoc as c, v_albacol as l,v_articulo as p "+
      "  WHERE c.emp_codi = "+emp_codiE.getValorInt()+
       " AND l.emp_codi = "+emp_codiE.getValorInt()+
       " AND c.acc_copvfa = "+prv_codiE.getValorInt()+
       " and c.acc_ano = l.acc_ano "+
       " and c.acc_serie= l.acc_serie "+
       " and c.acc_nume = l.acc_nume "+
       " and p.pro_codi = l.pro_codi "+
//       " and p.emp_codi = "+EU.em_cod+
       " and c.acc_cerra != 0 "+  // Albaran cerrado
       " AND C.acc_totfra = 0 "+ // NO totalmente facturado
       (verAlbaE.getValor().equals("T")?"":" and l.acl_totfra = "+verAlbaE.getValor())+
       " and l.acl_canti - l.acl_canfac NOT BETWEEN  -0.1 AND 0.1"+
       " AND l.acl_prcom != 0"+
       " order by l.acc_ano desc,l.acc_serie,l.acc_nume desc,l.acl_nulin";
   if (! dtCon1.select(s))
   {
     mensajeErr("Sin albaranes pendientes para este proveedor");
     return;
   }
   String alb="",alb1;

   do
   {
     alb1=dtCon1.getString("acc_ano")+dtCon1.getString("acc_serie")+dtCon1.getString("acc_nume");
     if (! alb.equals(alb1))
     {
       alc_numinsE.addItem(dtCon1.getString("acc_ano")+"/"+dtCon1.getString("acc_nume") + " (" +
                           dtCon1.getFecha("acc_fecrec") + ")",
                           dtCon1.getString("acc_ano")+"/"+dtCon1.getString("acc_nume"));
       alb=alb1;
     }
     insLiAlb0();
   } while (dtCon1.next());
   jtAlb.requestFocusInicio();
 }

private void insLiAlb0() throws IllegalArgumentException, ParseException,
    SQLException
{
  ArrayList v=new ArrayList();
  v.add(dtCon1.getString("pro_codi"));
  if (dtCon1.getString("pro_nomart").equals(""))
    v.add(dtCon1.getString("pro_nomb"));
  else
    v.add(dtCon1.getString("pro_nomart"));
   v.add(dtCon1.getString("acl_numcaj"));
   v.add(dtCon1.getDouble("acl_canti",true)-dtCon1.getDouble("acl_canfac",true));
   double precio= MantAlbCom.getPrecioFra(dtCon1.getDouble("acl_dtopp",true),
       dtCon1.getDouble("acl_prcom",true),
       dtCon1.getInt("acl_porpag")==0?dtCon1.getDouble("acc_impokg",true):0,       
       dtCon1.getDouble("acc_imcokg"));
   v.add(precio);   
   v.add((dtCon1.getDouble("acl_canti")-dtCon1.getDouble("acl_canfac"))*precio);
   v.add(dtCon1.getString("acc_serie"));
   v.add(dtCon1.getString("acc_ano"));
   v.add(dtCon1.getString("acc_nume"));
   v.add(dtCon1.getFecha("acc_fecrec"));
   v.add(dtCon1.getString("acl_nulin"));
   v.add(dtCon1.getInt("acl_totfra")!=0);
   jtAlb.addLinea(v);
}

 private void cambioPrv() throws Exception
 {
   if (!prv_codiE.hasCambio())
     return;
   if (!prv_codiE.controlar())
   {
     mensajeErr(prv_codiE.getMsgError());
     return;
   }
   prv_codiE.resetCambio();
   fpa_codiE.setValorInt(prv_codiE.getFpaCodi());
   jtVto.setProveedor(prv_codiE.getValorInt());
   llenaGridAlb();
 }

  @Override
 public void PADPrimero() {verDatos(dtCons);}
@Override
 public void PADAnterior() {verDatos(dtCons);}
@Override
 public void PADSiguiente() {verDatos(dtCons);}
@Override
 public void PADUltimo() {verDatos(dtCons);}

    @Override
 public void PADQuery()
 {
   mensaje("Estableciendo condiciones de Filtro");
   Pcabe.setQuery(true);
   Pcabe.resetTexto();
   activar(navegador.QUERY,true);
   emp_codiE.setValorDec(EU.em_cod);
   if (Integer.parseInt(Formatear.getFechaAct("MM"))>2)
     eje_numeE.setValorDec(EU.ejercicio);
   fpa_codiE.setQuery(true);
   fpa_codiE.resetTexto();
   fcc_numeE.requestFocus();
 }

 public void ej_query1()
 {
   Component c=Pcabe.getErrorConf();
   if (c!=null)
   {
     mensajeErr("Criterios NO validos");
     c.requestFocus();
     return;
   }
   ArrayList v=new ArrayList();
   v.add(eje_numeE.getStrQuery());
   v.add(fcc_numeE.getStrQuery());
   v.add(fcc_fecfraE.getStrQuery());
   v.add(fcc_facprvE.getStrQuery());
   v.add(fcc_fefapvE.getStrQuery());
   v.add(prv_codiE.getStrQuery());
   v.add(fpa_codiE.getStrQuery());

   s = "SELECT emp_codi,eje_nume,fcc_nume " +
         " FROM v_facaco WHERE emp_codi = " + EU.em_cod ;
   s= creaWhere(s, v, false);
   s+=" ORDER BY eje_nume,fcc_nume ";
   Pcabe.setQuery(false);
   fpa_codiE.setQuery(false);
   this.setEnabled(false);
   try
   {
   mensaje("Buscando Datos ... Espere, por favor");
   if (!dtCon1.select(s))
   {
     msgBox("No encontradas Facturas con estos criterios");
     verDatos(dtCons);
     activaTodo();
     this.setEnabled(true);
     return;
   }
   strSql = s;
   rgSelect();
   verDatos(dtCons);
   this.setEnabled(true);
   mensaje("");
   mensajeErr("Nuevas Condiciones ... Establecidas");
 }
 catch (Exception k)
 {
   Error("Error al buscar datos", k);
 }
 activaTodo();
 nav.pulsado=navegador.NINGUNO;
 }

 public void canc_query()
 {
   Pcabe.setQuery(false);
   fpa_codiE.setQuery(false);
   activaTodo();
   verDatos(dtCons);
   mensaje("");
   mensajeErr("Consulta ... Cancelada");
   nav.pulsado = navegador.NINGUNO;

 }

    @Override
 public void PADEdit()
 {
   try {
     s = "SELECT * FROM v_facaco WHERE emp_codi = " + emp_codiE.getValorInt() +
         " and eje_nume = " + eje_numeE.getValorInt() +
         " and fcc_nume = " + fcc_numeE.getValorInt();
     if (!dtStat.select(s))
     {
       mensajeErr("Factura NO encontrada .. SEGURAMENTE SE BORRO");
       activaTodo();
       return;
     }
     fccFacprvOLD=dtStat.getString("fcc_facprv");
     if (pdejerci.isCerrado(dtStat, eje_numeE.getValorInt(), emp_codiE.getValorInt()))
     {
       if (!admin)
       {
         msgBox("Factura es de un ejercicio YA cerrado ... IMPOSIBLE BORRAR");
         nav.pulsado = navegador.NINGUNO;
         activaTodo();
         return;
       }
       else
         msgBox("ATENCION!!! Factura es de un ejercicio YA cerrado");
     }

     if (!setBloqueo(dtAdd, "v_facaco",
                     eje_numeE.getValorInt() + "|" + emp_codiE.getValorInt() +
                     "|" + fcc_numeE.getValorInt()))
     {
       msgBox(msgBloqueo);
       activaTodo();
       return;
     }
     mensaje("Modificando Factura ....");
     activar(navegador.ADDNEW,true);
     verAlbaE.resetTexto();
     prv_codiE.resetCambio();
     fpa_codiE.resetCambio();
     Tprinci.setSelectedComponent(PFactura);
//     jtVto.requestFocusInicio();
     jtVto.setProveedor(prv_codiE.getValorInt());
     jtVto.jtGru.ponValores(0);
     jtVto.setDatosFra("C",emp_codiE.getValorInt(),eje_numeE.getValorInt(),fcc_numeE.getValorInt());
     irGridFra();
     llenaGridAlb();
   } catch (Exception k)
   {
     Error("Error al editar registro",k);
   }
 }

    @Override
 public void ej_edit1()
 {
   mensaje("Espere, por favor .. Actualizando Factura");

   this.setEnabled(false);
   try {
//    actAcumFra();
//    incNumFra();
    borrarFra();
    insCabec();
    insLineas();
    if (jf != null)
    {
      jf.ht.clear();
      jf.ht.put("%f", eje_numeE.getValorInt() + "|" + emp_codiE.getValorInt() +
                         "|" + fcc_numeE.getValorInt());
      jf.guardaMens("C3", jf.ht); // Mod. Fra de Compras
    }

    jtVto.actDatos(dtAdd,stUp, 'C',emp_codiE.getValorInt(),eje_numeE.getValorInt(),
                   fcc_numeE.getValorInt(),fcc_fecfraE.getDate(),prv_codiE.getValorInt(),
                   prv_codiE.getTextNomb());
    ctCom.commit();
    resetBloqueo(dtAdd, "v_facaco",
                         eje_numeE.getValorInt() + "|" + emp_codiE.getValorInt() +
                         "|" + fcc_numeE.getValorInt());
//    jtVto.llenaGrid(dtCon1,'C',emp_codiE.getValorInt(),eje_numeE.getValorInt(),fcc_numeE.getValorInt());
    mensajeErr("Factura ... MODIFICADA");
    mensaje("");
  } catch (Exception k)
  {
    Error("Error al Modificar Factura",k);
    return;
  }
  activaTodo();
  nav.pulsado=navegador.NINGUNO;
 }

 void borrarFra() throws Exception
 {
   s = "DELETE FROM v_facaco WHERE emp_codi = " + emp_codiE.getValorInt() +
       " and eje_nume = " + eje_numeE.getValorInt() +
       " and fcc_nume = " + fcc_numeE.getValorInt();
   stUp.executeUpdate(dtAdd.parseaSql(s));
   s = "SELECT * FROM v_falico WHERE emp_codi = " + emp_codiE.getValorInt() +
       " and eje_nume = " + eje_numeE.getValorInt() +
       " and fcc_nume = " + fcc_numeE.getValorInt();
   if (! dtCon1.select(s))
     return;
   do
   {
     if ( dtCon1.getInt("acl_nulin",true)!=0)
     { // Act. Lineas Albaran
       s = "SELECT acl_canfac FROM v_albacol "+
           " WHERE emp_codi = " + emp_codiE.getValorInt() +
           " and acc_ano = " + dtCon1.getInt("acc_ano",true) +
           " and acc_nume = " + dtCon1.getInt("acc_nume",true) +
           " and acc_serie = '" + dtCon1.getString("acc_serie") + "'" +
           " and acl_nulin = " + dtCon1.getInt("acl_nulin",true);
       if (!dtAdd.select(s, true))
           msgBox("NO encontrado registro Linea Albaran.\n"+s);
       else
       {
           dtAdd.edit(dtAdd.getCondWhere());
           dtAdd.setDato("acl_canfac",
                         dtAdd.getDouble("acl_canfac",true) -
                         dtCon1.getDouble("fcl_canti",true));
           dtAdd.update(stUp);
       }
     }
   } while (dtCon1.next());
   s = "DELETE FROM v_falico WHERE emp_codi = " + emp_codiE.getValorInt() +
       " and eje_nume = " + eje_numeE.getValorInt() +
       " and fcc_nume = " + fcc_numeE.getValorInt();
   stUp.executeUpdate(dtAdd.parseaSql(s));
 }

    @Override
 public void canc_edit()
 {
   mensajeErr("Modificacion Cancelada");
   mensaje("");
   try
   {
     resetBloqueo(dtAdd, "v_facaco",
                      eje_numeE.getValorInt() + "|" + emp_codiE.getValorInt() +
                      "|" + fcc_numeE.getValorInt());
   } catch (Exception k)
   {
      mensajes.mensajeAviso("Error al Quitar el bloqueo\n"+k.getMessage());
   }
   activaTodo();
   verDatos(dtCons);
   nav.pulsado=navegador.NINGUNO;
 }

    @Override
 public void PADAddNew()
 {
   jtAlb.removeAllDatos();
   jtFra.removeAllDatos();
   jtVto.removeAllDatos();
   Pcabe.resetTexto();
   Ppie.resetTexto();
   prv_codiE.resetCambio();
   activar(navegador.ADDNEW, true);
   emp_codiE.setValorDec(EU.em_cod);
   eje_numeE.setValorDec(EU.ejercicio);
   fcc_fecfraE.setText(Formatear.getFechaAct("dd-MM-yy"));
   fcc_numeE.setEnabled(false);
   div_codiE.setEnabled(true);
   fpa_codiE.resetTexto();
   fpa_codiE.resetCambio();
   jtVto.setProveedor(0);
   verAlbaE.resetTexto();
   mensaje("Insertando nueva factura ...");
   Tprinci.setSelectedComponent(PFactura);
   jtVto.requestFocusInicio();
  
   jtVto.setDatosFra("C",emp_codiE.getValorInt(),0,0);
   fcc_facprvE.requestFocus();
 }
 @Override
 public boolean checkEdit()
 {
   return checkAddNew();
 }
 @Override
 public boolean checkAddNew()
 {
   try
   {
      s="SELECT * FROM v_facaco WHERE emp_codi = " + emp_codiE.getValorInt() +
         " and fcc_facprv = '"+fcc_facprvE.getText()+"'"+
         " and prv_codi = "+prv_codiE.getValorInt()+
         (nav.getPulsado()==navegador.EDIT?" and fcc_facprv != '"+fccFacprvOLD+"'":"");
     if (dtStat.select(s) )
     {
        int ret=mensajes.mensajePreguntar("Ya se cargo una factura con este numero. Continuar ?");
        if (ret!=mensajes.YES)
            return false;
     }
//     if (!prv_codiE.controlar())
//     {
//       mensajeErr(prv_codiE.getMsgError());
//       return false;
//     }
     if (! checkCabe())
       return false;
     if (Integer.parseInt(fcc_fefapvE.getFecha("yyyy"))!=eje_numeE.getValorInt())
     {
       if (mensajes.mensajePreguntar("Fecha Fra. de Proveedor NO es del Ejercicio introducido\n¿Continuar?")!=mensajes.YES)
       {
         eje_numeE.setText(fcc_fefapvE.getFecha("yyyy"));
         eje_numeE.requestFocus();
         return false;
       }
     }
     jtFra.procesaAllFoco();
     if (cambiaLinFra()>=0)
       return false;
     if (fcc_numlinE.getValorInt() ==0)
     {
       mensajeErr("Introduzca alguna Linea de Fra");
       jtFra.requestFocusInicio();
       return false;
     }
     jtVto.jtVto.procesaAllFoco();
     if (jtVto.cambiaLineaVto()>=0)
     {
       jtVto.requestFocusVto();
       Tprinci.setSelectedComponent(Pvtos);
       return false;
     }
     if (jtVto.getLinActivas()==0)
     {
       mensajeErr("Introduzca Algun Importe en los Vtos.");
       jtVto.requestFocusInicio();
       Tprinci.setSelectedComponent(Pvtos);
       return false;
     }
     if (jtVto.getImpVtos()!=fcc_imptotE.getValorDec())
     {
       mensajeErr("Importe de Factura NO coincide con la suma de Vtos");
       jtVto.requestFocusInicio();
       Tprinci.setSelectedComponent(Pvtos);
       return false;
     }
     jtVto.jtGru.procesaAllFoco();
     actAcumFra();
     if (jtVto.cambiaLineaFra() >= 0)
     {
       mensajeErr("Error al comprobar lineas de Vto de la factura");
       jtVto.requestFocusGru();
       Tprinci.setSelectedComponent(Pvtos);
       return false;
     }
     if (! jtVto.checkPrv(prv_codiE.getValorInt()))
     {
       jtVto.requestFocusGru();
       Tprinci.setSelectedComponent(Pvtos);
       mensajeErr("Proveedor en Factura de grupo no es el mismo que el de esta factura");
       return false;
     }
   }
   catch (Exception k)
   {
     Error("Error al Chequear Alta de Factura", k);
   }
   return true;
 }

 public void ej_addnew1()
 {
   mensaje("Espere, por favor .. Insertando Factura");

   this.setEnabled(false);
   try {
     incNumFra();
     insCabec();
     insLineas();
     jtVto.actDatos(dtAdd,stUp, 'C',emp_codiE.getValorInt(),eje_numeE.getValorInt(),
                   fcc_numeE.getValorInt(),fcc_fecfraE.getDate(),prv_codiE.getValorInt(),
                   prv_codiE.getTextNomb());
     ctCom.commit();
     mensajeErr("Factura ... INSERTADA");
     mensaje("");
     activaTodo();
     Tprinci.setSelectedComponent(PFactura);
     nav.pulsado=navegador.NINGUNO;
     if (dtCons.getNOREG())
     {
       rgSelect();
       navActivarAll();
     }
   } catch (Exception k)
   {
     Error("Error al Insertar Factura",k);
   }
 }

 void incNumFra()  throws Exception
 {
   s = "SELECT num_faccom FROM v_numerac WHERE emp_codi = " + emp_codiE.getValorInt() +
       " AND eje_nume = " + eje_numeE.getValorInt();
   if (!dtAdd.select(s, true))
     throw new Exception("No encontrado Registro Guia de Numeraciones");
   fcc_numeE.setValorDec(dtAdd.getInt("num_faccom") + 1);
   dtAdd.edit(dtAdd.getCondWhere());
   dtAdd.setDato("num_faccom", dtAdd.getInt("num_faccom") + 1);
   dtAdd.update(stUp);
 }

 void insCabec() throws Exception
 {
   dtAdd.addNew("v_facaco");
   dtAdd.setDato("eje_nume",eje_numeE.getValorInt());
   dtAdd.setDato("emp_codi",emp_codiE.getValorInt());
   dtAdd.setDato("fcc_nume",fcc_numeE.getValorInt());
   dtAdd.setDato("prv_codi",prv_codiE.getValorInt());
   dtAdd.setDato("fcc_fecfra",fcc_fecfraE.getText(),"dd-MM-yy");
   dtAdd.setDato("fcc_trasp",-1); // No Traspasado
   dtAdd.setDato("fcc_facimp",0); // Impreso
   dtAdd.setDato("fcc_facprv",fcc_facprvE.getText());
   dtAdd.setDato("fcc_fefapv",fcc_fefapvE.getText(),"dd-MM-yy");
   dtAdd.setDato("div_codi",div_codiE.getText());
   dtAdd.setDato("fpa_codi",fpa_codiE.getText());
   dtAdd.setDato("fcc_apltas",0); // Aplicar Tasas
   dtAdd.setDato("fcc_tainpr",0); // Incluir Tasas en Precio
   dtAdd.setDato("fcc_sumlin",fcc_implinE.getValorDec());
   dtAdd.setDato("fcc_sumtot",fcc_imptotE.getValorDec());
   dtAdd.setDato("fcc_impiv1",fcc_impiv1E.getValorDec());
   dtAdd.setDato("fcc_impre1",fcc_impre1E.getValorDec());
   dtAdd.setDato("fcc_basim1",fcc_basim1E.getValorDec());
   dtAdd.setDato("fcc_dtopp",fcc_dtoppE.getValorDec());
   dtAdd.setDato("fcc_dtocom",fcc_dtocomE.getValorDec());
   dtAdd.setDato("fcc_piva1",fcc_piva1E.getValorDec());
   dtAdd.setDato("fcc_prec1",fcc_prec1E.getValorDec());
   dtAdd.setDato("fcc_conpag",fcc_conpagE.getValor());
   dtAdd.setDato("fcc_coment",fcc_comentE.getText());
   dtAdd.setDato("fcc_kilfra",fcc_kilfraE.getValorDec());
   dtAdd.update(stUp);
 }

 void insLineas() throws Exception
 {
   
   int nRow=jtFra.getRowCount();
   int nLin1=1;
   ArrayList<Albacoc> v=new ArrayList();
   for (int n=0;n<nRow;n++)
   {
     if (jtFra.getValorInt(n,0)==0)
       continue;
     dtAdd.addNew("v_falico");
     dtAdd.setDato("eje_nume", eje_numeE.getValorInt());
     dtAdd.setDato("emp_codi", emp_codiE.getValorInt());
     dtAdd.setDato("fcc_nume", fcc_numeE.getValorInt());
     dtAdd.setDato("fcl_numlin", nLin1);
     dtAdd.setDato("fcl_tipdes",jtFra.getValString(n,4));
     dtAdd.setDato("pro_codi",jtFra.getValorInt(n,0));
     dtAdd.setDato("acc_nume", jtFra.getValString(n,11 ));
     dtAdd.setDato("acc_ano", jtFra.getValString(n,10 ));
     dtAdd.setDato("acc_serie", jtFra.getValString(n, 9));
     if (! jtFra.getValString(n,9,true).equals(""))
     {
       dtAdd.setDato("acc_fecalb", jtFra.getValString(n,13 ),"dd-MM-yyyy");
       dtAdd.setDato("acl_nulin", jtFra.getValString(n,14 ));
     }
     dtAdd.setDato("fcl_canti",jtFra.getValorDec(n,2));
     dtAdd.setDato("fcl_prcom",jtFra.getValorDec(n,3));
     dtAdd.setDato("fcl_dto",jtFra.getValorDec(n,5));
     dtAdd.update(stUp);
     if (! jtFra.getValString(n,9,true).equals("") && jtFra.getValorInt(n,14)>0)
     { // Act. Lineas Albaran
        s = "SELECT  acc_impokg FROM v_albacoc "+
          " WHERE emp_codi = " + emp_codiE.getValorInt() +
           " and acc_ano = " + jtFra.getValorInt(n, 10) +
           " and acc_nume = " + jtFra.getValorInt(n, 11) +
           " and acc_serie = '" + jtFra.getValString(n, 9) + "'";
       if (!dtStat.select(s))
         throw new Exception("NO encontrado registro Cabecera Albaran.\n" + s);
       s="SELECT acl_canfac,acl_prcom,acl_dtopp,acl_porpag  "+
              " FROM v_albacol WHERE emp_codi = "+emp_codiE.getValorInt()+
              " and acc_ano = "+jtFra.getValorInt(n,10)+
              " and acc_nume = "+jtFra.getValorInt(n,11)+
              " and acc_serie = '"+jtFra.getValString(n,9)+"'"+
              " and acl_nulin = "+jtFra.getValorInt(n,14);
        if (! dtAdd.select(s,true))
          throw new Exception("NO encontrado registro Linea Albaran.\n"+s);
//       double prCompra=dtAdd.getDouble("acl_prcom")- 
//           (dtAdd.getInt("acl_porpag")==0?dtStat.getDouble("acc_impokg"):0)-
//           dtStat.getDouble("acc_imcokg");
           
//       prCompra=prCompra / (1 - (dtAdd.getDouble("acl_dtopp")/100)); // Dejo costo de linea 'limpio'
//       prCompra-=prCompra*fcc_dtoppE.getValorDec()/100; // Le quito el nuevo dto pp
//       prCompra+=dtAdd.getInt("acl_porpag")==0?dtStat.getDouble("acc_impokg"):0; // Le sumo los portes.
       dtAdd.edit(dtAdd.getCondWhere());
       dtAdd.setDato("acl_canfac",dtAdd.getDouble("acl_canfac")+jtFra.getValorDec(n,2));
       dtAdd.setDato("acl_dtopp",fcc_dtoppE.getValorDec());
//       dtAdd.setDato("acl_prcom",prCompra);
       dtAdd.update(stUp);
       Albacoc fc=new Albacoc(jtFra.getValorInt(n,11),jtFra.getValorInt(n,10),jtFra.getValString(n,9));
       if (v.indexOf(fc)==-1)
           v.add(fc);
     }
     
     nLin1++;
   }
   nRow=v.size();
//   double impTot;
   for (int n=0;n<nRow;n++)
   {
      s = "SELECT fcc_ano,fcc_nume,acc_totfra FROM v_albacoc "+
          " WHERE emp_codi = " + emp_codiE.getValorInt() +
           " and acc_ano = " + v.get(n).getAccAno() +
           " and acc_nume = " + v.get(n).getAccNume() +
           " and acc_serie = '" + v.get(n).getAccSerie() + "'";
       if (!dtAdd.select(s,true))
         throw new Exception("NO encontrado registro Cabecera Albaran.\n" + s);
//       impTot=pdalbco2.getImporteAlb(dtCon1,emp_codiE.getValorInt(),v.get(n).getAccAno(),
//           v.get(n).getAccSerie(),v.get(n).getAccNume());
       dtAdd.edit();
       dtAdd.setDato("fcc_ano",eje_numeE.getValorInt());
       dtAdd.setDato("fcc_nume",fcc_numeE.getValorInt());
//       dtAdd.setDato("acc_totfra",impTot);
       dtAdd.update(stUp);
   }
 }

    @Override
 public void canc_addnew()
 {
   mensajeErr("Insercion Cancelada");
   mensaje("");
   activaTodo();
   verDatos(dtCons);
   nav.pulsado=navegador.NINGUNO;
   return;
 }

    @Override
 public void PADDelete()
 {
   try
   {
     s = "SELECT * FROM v_facaco WHERE emp_codi = " + emp_codiE.getValorInt() +
         " and eje_nume = " + eje_numeE.getValorInt() +
         " and fcc_nume = " + fcc_numeE.getValorInt();
     if (!dtStat.select(s))
     {
       mensajeErr("Factura NO encontrada .. SEGURAMENTE SE BORRO");
       activaTodo();
       return;
     }
     if (jtVto.hasPagos(dtAdd, 'C', emp_codiE.getValorInt(), eje_numeE.getValorInt(),
                        fcc_numeE.getValorInt()))
     {
       mensajeErr("Ya se han realizado pagos sobre esta Factura ... IMPOSIBLE BORRAR");
       activaTodo();
       return;
     }
     if (pdejerci.isCerrado(dtStat, eje_numeE.getValorInt(), emp_codiE.getValorInt()))
     {
       if (!admin)
       {
         msgBox("Factura es de un ejercicio YA cerrado ... IMPOSIBLE MODIFICAR");
         nav.pulsado = navegador.NINGUNO;
         activaTodo();
         return;
       }
       else
         msgBox("ATENCION!!! Factura es de un ejercicio YA cerrado");
     }

     if (!setBloqueo(dtAdd, "v_facaco",
                     eje_numeE.getValorInt() + "|" + emp_codiE.getValorInt() +
                     "|" + fcc_numeE.getValorInt()))
     {
       msgBox(msgBloqueo);
       activaTodo();
       return;
     }
     Bimpri.setEnabled(false);
     Baceptar.setEnabled(true);
     Bcancelar.setEnabled(true);
     Bcancelar.requestFocus();
     Tprinci.setSelectedComponent(PFactura);
     mensaje("Borrando Factura ....");

   }
   catch (Exception k)
   {
     Error("Error al editar registro", k);
   }

 }
    @Override
 public void ej_delete1()
 {
   mensaje("Espere, por favor .. Borrando Factura");

   this.setEnabled(false);
   try
   {
     jtVto.delDatos(dtAdd,stUp, 'C',emp_codiE.getValorInt(),eje_numeE.getValorInt(),
                   fcc_numeE.getValorInt());
     borrarFra();
     if (jf != null)
    {
      jf.ht.clear();
      jf.ht.put("%a", eje_numeE.getValorInt() + "|" + emp_codiE.getValorInt() +
                         "|" + fcc_numeE.getValorInt());
      jf.guardaMens("C5", jf.ht); // BORRADO  Fra de Compras
    }

     ctCom.commit();
     resetBloqueo(dtAdd, "v_facaco",
                  eje_numeE.getValorInt() + "|" + emp_codiE.getValorInt() +
                  "|" + fcc_numeE.getValorInt());

     mensajeErr("Factura ... BORRADA");
     mensaje("");
     activaTodo();
     rgSelect();
   }
   catch (Exception k)
   {
     Error("Error al Borrar Factura", k);
   }
 }

 public void canc_delete()
 {
   mensajeErr("Borrado .... Cancelado");
   mensaje("");
   try
   {
     resetBloqueo(dtAdd, "v_facaco",
                  eje_numeE.getValorInt() + "|" + emp_codiE.getValorInt() +
                  "|" + fcc_numeE.getValorInt());
   }
   catch (Exception k)
   {
     mensajes.mensajeAviso("Error al Quitar el bloqueo\n" + k.getMessage());
   }
   nav.pulsado=navegador.NINGUNO;
   activaTodo();
 }

 public void activar(boolean b)
 {
   activar(navegador.TODOS, b);
 }
 public void activar(int modo,boolean b)
 {
   verAlbaE.setEnabled(false);
   Bimpri.setEnabled(!b);
   BirGrid.setEnabled(b);
   eje_numeE.setEnabled(b);
   emp_codiE.setEnabled(b);
   fcc_numeE.setEnabled(b);
   fcc_fecfraE.setEnabled(b);
   fcc_facprvE.setEnabled(b);
   fcc_fefapvE.setEnabled(b);
   prv_codiE.setEnabled(b);
   Baceptar.setEnabled(b);
   Bcancelar.setEnabled(b);
   fpa_codiE.setEnabled(b);
   fcc_comentE.setEnabled(b);
   fcc_kilfraE.setEnabled(b);
   fcc_conpagE.setEnabled(b);
   if (modo==navegador.QUERY)
     return;

   div_codiE.setEnabled(b);
   fcc_implinE.setEnabled(b);
   fcc_kglinE.setEnabled(b);
   fcc_dtoppE.setEnabled(b);
   fcc_dtocomE.setEnabled(b);
   fcc_piva1E.setEnabled(b);
   fcc_prec1E.setEnabled(b);
   fcc_impiv1E.setEnabled(b);
   fcc_impre1E.setEnabled(b);
   fcc_imptotE.setEnabled(b);
   fcc_basim1E.setEnabled(b);
   jtVto.setEnabled(b);
   if (modo == navegador.ADDNEW)
     return;
   jtFra.setEnabled(b);
   jtAlb.setEnabled(b);
   alc_numinsE.setEnabled(b);
   BinsAlb.setEnabled(b);

 }

    @Override
 public void afterConecta() throws SQLException, java.text.ParseException
 {
   verAlbaE.addItem("No Facturadas","0");
   verAlbaE.addItem("Facturadas","-1");
   verAlbaE.addItem("TODAS","T");
   jtVto.iniciar(this,dtStat);
   prv_codiE.iniciar(dtStat,this,vl,EU);
   s = "SELECT div_codi,div_codedi FROM v_divisa order by div_codedi";
   if (!dtStat.select(s))
     throw new SQLException("NO HAY NINGUNA DIVISA DEFINIDA");
   div_codiE.addDatos(dtStat);
   div_codiE.setFormato(Types.DECIMAL,"##9",3);
   fpa_codiE.setFormato(Types.DECIMAL,"##9",3);
   s = "SELECT fpa_codi,fpa_nomb FROM v_forpago order by fpa_codi";
   dtStat.select(s);
   fpa_codiE.addDatos(dtStat);
   pro_codiE.iniciar(dtStat,this,vl,EU);
   pro_codiE.setProNomb(null);
   fcc_conpagE.addItem("Si","S");
   fcc_conpagE.addItem("No","N");
 }

 void verDatos(DatosTabla dt)
 {
   try
   {
     if (dt.getNOREG())
       return;

     Pcabe.resetTexto();
     jtFra.removeAllDatos();
     emp_codiE.setValorDec(dt.getInt("emp_codi"));
     eje_numeE.setValorDec(dt.getInt("eje_nume"));
     fcc_numeE.setValorDec(dt.getInt("fcc_nume"));
     s="SELECT * FROM v_facaco WHERE emp_codi = " + emp_codiE.getValorInt()+
         " and eje_nume = "+eje_numeE.getValorInt()+
         " and fcc_nume = "+fcc_numeE.getValorInt();
     if (! dtCon1.select(s))
     {
       msgBox("NO ENCONTRADOS DATOS DE FACTURA");
       return;
     }
     fcc_fecfraE.setText(dtCon1.getFecha("fcc_fecfra","dd-MM-yy"));
     prv_codiE.setText(dtCon1.getString("prv_codi"));
     fcc_facprvE.setText(dtCon1.getString("fcc_facprv"));
     fcc_fefapvE.setText(dtCon1.getFecha("fcc_fefapv","dd-MM-yy"));
     div_codiE.setText(dtCon1.getString("div_codi"));
     fcc_implinE.setValorDec(dtCon1.getDouble("fcc_sumlin"));
     fcc_basim1E.setValorDec(dtCon1.getDouble("fcc_basim1"));
     fcc_dtoppE.setValorDec(dtCon1.getDouble("fcc_dtopp"));
     fcc_piva1E.setValorDec(dtCon1.getDouble("fcc_piva1"));
     fcc_impiv1E.setValorDec(dtCon1.getDouble("fcc_impiv1"));
     fcc_dtocomE.setValorDec(dtCon1.getDouble("fcc_dtocom"));
     fcc_prec1E.setValorDec(dtCon1.getDouble("fcc_prec1"));
     fcc_impre1E.setValorDec(dtCon1.getDouble("fcc_impre1"));
     fcc_traspE.setSelecion(dtCon1.getString("fcc_trasp"));
     fcc_imptotE.setValorDec(dtCon1.getDouble("fcc_sumtot"));
     fpa_codiE.setValorInt(dtCon1.getInt("fpa_codi",true));
     fcc_conpagE.setValor(dtCon1.getString("fcc_conpag"));
     fcc_comentE.setText(dtCon1.getString("fcc_coment"));
     fcc_kilfraE.setText(dtCon1.getString("fcc_kilfra",true));
     jtVto.setDatosFra("C",emp_codiE.getValorInt(),eje_numeE.getValorInt(),fcc_numeE.getValorInt());
     jtVto.verGrupoFra();
     s="SELECT l.*,p.pro_nomb FROM v_falico as l,v_articulo as p "+
         " WHERE l.emp_codi = " + emp_codiE.getValorInt()+
         " and l.eje_nume = "+eje_numeE.getValorInt()+
         " and l.fcc_nume = "+fcc_numeE.getValorInt()+
         " and l.pro_codi = p.pro_codi "+
         " ORDER BY fcl_numlin";
     if (! dtCon1.select(s))
     {
       msgBox("NO ENCONTRADAS LINEAS DE FACTURA");
       return;
     }
     double kilos=0,numUnid=0;
     int nLin1=0;
     boolean conAlb;
     double impLin;
     do
     {
       kilos+=dtCon1.getDouble("fcl_canti");
       nLin1++;
       ArrayList v=new ArrayList();
       v.add(dtCon1.getString("pro_codi"));
       v.add(dtCon1.getString("pro_nomb"));
       v.add(dtCon1.getString("fcl_canti"));
       v.add(dtCon1.getString("fcl_prcom"));
       v.add(dtCon1.getString("fcl_tipdes")); // Tipo Descuento (%)
       v.add(dtCon1.getString("fcl_dto")); // Descuento
       impLin=Formatear.redondea(dtCon1.getDouble("fcl_canti")*dtCon1.getDouble("fcl_prcom"),numDec);
       if (dtCon1.getString("fcl_tipdes").equals("%") && dtCon1.getDouble("fcl_dto")>0)
         impLin-= impLin*dtCon1.getDouble("fcl_dto")/100;
        else
          impLin-= dtCon1.getDouble("fcl_dto");
        impLin=Formatear.redondea(impLin,numDec);
        v.add(""+impLin);
         conAlb=false;
        if (dtCon1.getInt("acc_ano",true)!=0 && dtCon1.getInt("acc_nume",true)!=0)
        {
          s="SELECT l.*,c.acc_impokg FROM v_albacol as l, v_albacoc as c WHERE l.emp_codi = "+dtCon1.getInt("emp_codi")+
              " and l.acc_ano = "+dtCon1.getInt("acc_ano",true)+
              " and l.acc_nume = "+dtCon1.getInt("acc_nume",true)+
              " and l.acc_serie = '"+dtCon1.getString("acc_serie")+"'"+
              " and l.acl_nulin = "+dtCon1.getInt("acl_nulin")+
              " and c.emp_codi = "+dtCon1.getInt("emp_codi")+
              " and c.acc_ano = "+dtCon1.getInt("acc_ano",true)+
              " and c.acc_nume = "+dtCon1.getInt("acc_nume",true)+
              " and c.acc_serie = '"+dtCon1.getString("acc_serie")+"'";

          if (dtStat.select(s))
          {
            v.add(dtStat.getString("acl_canti"));
            v.add(""+(dtStat.getDouble("acl_prcom")-dtStat.getDouble("acc_impokg",true)));
            numUnid+=dtStat.getInt("acl_numcaj");
            conAlb=true;
          }
        }
        if (!conAlb)
        {
          v.add("");
          v.add("");
        }
        v.add(dtCon1.getString("acc_serie"));
        v.add(dtCon1.getString("acc_ano"));
        v.add(dtCon1.getString("acc_nume"));
        if (conAlb)
          v.add(dtStat.getString("acl_numcaj"));
        else
          v.add("");
        v.add(dtCon1.getFecha("acc_fecalb","dd-MM-yyyy"));
        v.add(dtCon1.getString("acl_nulin"));
        jtFra.addLinea(v);
     } while (dtCon1.next());
     jtFra.requestFocusInicio();
     fcc_kglinE.setValorDec(kilos);
     fcc_numlinE.setValorDec(nLin1);
     fcc_numuniE.setValorDec(numUnid);
     jtVto.llenaGrid(dtCon1,'C',emp_codiE.getValorInt(),eje_numeE.getValorInt(),fcc_numeE.getValorInt());
     fcc_pagadoE.setSelected(jtVto.isTotalPagado());
   } catch (Exception k)
   {
     Error("Error al ver datos",k);
   }
 }
    @Override
 public void rgSelect() throws SQLException
{
  super.rgSelect();
  if (!dtCons.getNOREG())
  {
    dtCons.last();
    nav.setEnabled(navegador.ULTIMO, false);
    nav.setEnabled(navegador.SIGUIENTE, false);
  }
 // verDatos(dtCons);
}
/**
 * Usada para insertar una linea en alb. cuando se borra de la fra.
 * @param row int N. Linea de la Fra.,
 * @throws Exception error base datos
 */
void insLinAlb(int row) throws Exception
{
  if (jtFra.getValString(row,9,true).equals(""))
    return; // Sin Numero de Linea
  // Busco los datos del Albaran.
  s="SELECT c.acc_impokg,acc_imcokg, c.acc_fecrec, l.*,p.pro_nomb "+
       " FROM v_albacoc as c, v_albacol as l,v_articulo as p "+
      " WHERE l.emp_codi = " + EU.em_cod +
      " AND C.prv_codi = "+prv_codiE.getValorInt()+
      " and l.acc_ano = " + jtFra.getValorInt(row, 10) +
      " and l.acc_nume = " + jtFra.getValorInt(row, 11) +
      " and l.acc_serie = '" + jtFra.getValString(row, 9) + "'" +
      " and l.acl_nulin = " + jtFra.getValorInt(row, 14) +
      " and c.emp_codi = " + EU.em_cod +
      " and c.acc_ano = " + jtFra.getValorInt(row, 10) +
      " and c.acc_nume = " + jtFra.getValorInt(row, 11) +
      " and c.acc_serie = '" + jtFra.getValString(row, 9) + "'"+
      " and p.pro_codi = l.pro_codi "+
//      " and p.emp_codi = " + EU.em_cod +
      " and l.acl_canti - l.acl_canfac > 0" +
      " AND l.acl_prcom != 0" ;

  if (! dtCon1.select(s))
    return; // No existe Linea para ese proveedor
  insLiAlb0();
}
int cambiaLinFra()
{
  if (pro_codiE.isNull())
    return -1; // No tiene codigo de Producto
  try {
    if (!pro_codiE.controla(false))
    {
      mensajeErr(pro_codiE.getMsgError());
      return 0;
    }
    if (acc_anoE.getValorInt()==0 || acc_numeE.getValorInt()==0 || acc_serieE.isNull())
    {
        mensajeErr("Introduzca Albarán de compra");
        return 10;
    }
    s= "SELECT * FROM v_albacoc "+
           " WHERE emp_codi = " + emp_codiE.getValorInt() +
           " and acc_ano = " + acc_anoE.getValorInt() +
           " and acc_nume = " + acc_numeE.getValorInt() +
           " and acc_serie = '" + acc_serieE.getText() + "'" ;
    if (! dtStat.select(s))
    {
        mensajeErr("Albaran no encontrado");
        return 10;
    }
    if (dtStat.getInt("prv_codi")!=prv_codiE.getValorInt())
        msgBox("Atención ese albaran es de proveedor: "+dtStat.getInt("prv_codi"));
  } catch (Exception k)
  {
    Error("Error al Cambiar de Linea",k);
  }
  return -1;

}
  void insLinFra(int row) throws Exception
  {
    if (jtAlb.isVacio())
      return;
    int nCol;
    nCol=cambiaLinFra();
    if (nCol>=0)
    {
      jtFra.requestFocus(nCol,jtFra.getSelectedColumn());
      return;
    }
    ArrayList v= getLinAlb(row);
    jtFra.setEnabled(false);
    jtFra.addLinea(v);
    jtFra.ponValores(jtFra.getSelectedRowDisab());
    jtFra.setEnabled(true);
    jtFra.requestFocusFinal();
    jtAlb.removeLinea(row);
    actAcumFra();
  }

  ArrayList getLinAlb(int row)
  {
    ArrayList v = new ArrayList();
    v.add(jtAlb.getValString(row, 0));
    v.add(jtAlb.getValString(row, 1));
    v.add(jtAlb.getValString(row, 3));
    v.add(jtAlb.getValString(row, 4));
    v.add("%");
    v.add("0");
    v.add("0"); // Imp. Linea
    v.add(jtAlb.getValString(row, 3)); // Kg. Alb.
    v.add(jtAlb.getValString(row, 4)); // Pr. Alb
    v.add(jtAlb.getValString(row, 6)); // Serie
    v.add(jtAlb.getValString(row, 7)); // Ejer.
    v.add(jtAlb.getValString(row, 8)); // Alb.
    v.add(jtAlb.getValString(row, 2)); // N. Unid
    v.add(jtAlb.getValString(row, 9)); // Fec,Alb
    v.add(jtAlb.getValString(row, 10)); // N. Lin.Alb
    return v;
  }
  /**
   * Actualiza el acumulado de la Fra. (importes del pie)
   * @throws Exception en caso error en DB
   */
  void actAcumFra() throws Exception
  {
    int n=0;
    int nRow=jtFra.getRowCount();
    double kilos = 0, numUnid = 0;
    int nLin1 = 0;
//    boolean conAlb = false;
    double impLin,impLiT=0;

    for (n=0;n<nRow;n++)
    {
      if (jtFra.getValorInt(n,0)==0)
        continue;

      kilos+=jtFra.getValorDec(n,2);
      nLin1++;
      impLin= Formatear.redondea(jtFra.getValorDec(n,2)*jtFra.getValorDec(n,3),numDec);
      if (jtFra.getValString(n, 4).equals("%") && jtFra.getValorDec(n, 5) > 0)
        impLin -= impLin * jtFra.getValorDec(n, 5) / 100;
      else
        impLin -= jtFra.getValorDec(n, 5);
      impLin=Formatear.redondea(impLin,numDec);
      impLiT+=impLin;
      jtFra.setValor(""+impLin,n,6);
      numUnid+=jtFra.getValorDec(n,12);
    }
    fcc_kglinE.setValorDec(kilos);
    fcc_numlinE.setValorDec(nLin1);
    fcc_numuniE.setValorDec(numUnid);
    double impLi0=impLiT;
    fcc_implinE.setValorDec(impLiT);
    fcc_implinE.getValorDec();
    if (fcc_dtoppE.getValorDec() != 0)
      impLiT -= impLi0 * fcc_dtoppE.getValorDec() / 100;
    if (fcc_dtocomE.getValorDec() != 0)
      impLiT -= impLi0 * fcc_dtocomE.getValorDec() / 100;
    impLiT=Formatear.Redondea(impLiT,numDec);
    fcc_basim1E.setValorDec(impLiT);
    fcc_impiv1E.setValorDec(Formatear.Redondea(impLiT * fcc_piva1E.getValorDec() / 100,numDec));
    fcc_impre1E.setValorDec(Formatear.Redondea(impLiT * fcc_prec1E.getValorDec() / 100,numDec));
    double impAnt=fcc_imptotE.getValorDec();
    double imptot=fcc_imptotE.getValorDec();
    fcc_imptotE.setValorDec(impLiT + fcc_impre1E.getValorDec() +
                            fcc_impiv1E.getValorDec());
    if (fcc_imptotE.getValorDec()!=imptot && (nav.pulsado==navegador.EDIT || nav.pulsado==navegador.ADDNEW))
      jtVto.ponValoresDef(fcc_imptotE.getValorDec(),fpa_codiE.getValorInt(),fcc_fecfraE.getText());
    imptot=fcc_imptotE.getValorDec();
    if (jtVto.getLinActivas()==0)
    {
      jtVto.jtVto.setValor(fcc_imptotE.getText(), 0, 2);
      jtVto.lbv_impvtoE.setValorDec(fcc_imptotE.getValorDec());
    }
//    if (jtVto.getLinActivas()==1 && jtVto.jtVto.getValorDec(0,2)==impAnt)
    if (jtVto.getLinActivas()==1 && jtVto.jtVto.getValorDec(0,2)==impAnt && impAnt != imptot)
    {
      jtVto.jtVto.setValor(fcc_imptotE.getText(), 0, 2);
      jtVto.lbv_impvtoE.setValorDec(fcc_imptotE.getValorDec());
    }
  }

  void BinsAlb_actionPerformed()
  {
    try {
      if (alc_numinsE.isVacio())
      {
        mensajeErr("NO hay albaranes Disponibles");
        return;
      }
      String numAlb;
      
      jtFra.setEnabled(false);
      jtFra.salirGrid();      
      //jtFra.requestFocusFinal();
//    int nRow=jtAlb.getRowCount();
      for (int n = 0; n < jtAlb.getRowCount(); )
      {
        numAlb = jtAlb.getValString(n, 7) + "/" + jtAlb.getValString(n, 8);
        if (numAlb.equals(alc_numinsE.getValor()))
        {
          ArrayList v = getLinAlb(n);
          jtFra.addLinea(v);          
          jtAlb.removeLinea(n);
          continue;
        }
        n++;
      }
      jtFra.setEnabled(true);
      jtFra.requestFocusFinal();      
      jtFra.ponValores(jtFra.getSelectedRow());

//      jtFra.requestFocusFinal();
      actAcumFra();
    } catch (Exception k)
    {
        Error("Error al insertar linea de albaran",k);
    }
  }

    @Override
  public void matar(boolean cerrarConexion)
  {
    super.matar(cerrarConexion);
    try
    {
      if (cerrarConexion)
      {
        if (ctCom.isConectado())
        {
          ctCom.rollback();
          if (ctCom != null)
            ctCom.close();
        }
      }
    }
    catch (Exception k)
    {
      Logger.getLogger(pdfaccom.class.getName()).log(Level.SEVERE, null, k);
    }

  }
  void Bimpri_actionPerformed()
   {
     if (dtCons.getNOREG())
       return;
     try {
       java.util.HashMap mp = Listados.getHashMapDefault();
       JasperReport jr;
     nLin=-1;

     jr = Listados.getJasperReport(EU,"frascom");
     JasperPrint jp = JasperFillManager.fillReport(jr, mp, this);
     gnu.chu.print.util.printJasper(jp, EU);

    }  catch (Exception k)
    {
      Error("Error al imprimir Factura", k);
    }
   }
   
    @Override
   public boolean next() throws JRException
   {
     try {
       nLin++;
       if (nLin >= jtFra.getRowCount() || jtFra.isVacio())
         return false;
     } catch (Exception k)
     {
       Logger.getLogger(pdfaccom.class.getName()).log(Level.SEVERE, null, k);
     }
     return true;
   }
    
    @Override
   public Object getFieldValue(JRField f) throws JRException
   {
     try {
       String campo = f.getName().toLowerCase();
//       debug(campo+ " Tipo: "+f.getValueClassName());
       if (campo.equals("emp_codi"))
         return new Integer(emp_codiE.getValorInt());
       if (campo.equals("eje_nume"))
         return new Integer(eje_numeE.getValorInt());
       if (campo.equals("fcc_nume"))
         return new Integer(fcc_numeE.getValorInt());
       if (campo.equals("fcc_fecfra"))
         return fcc_fecfraE.getDate();
       if (campo.equals("fcc_facprv"))
         return fcc_facprvE.getText();
       if (campo.equals("fcc_fefapv"))
         return fcc_fefapvE.getDate();
       if (campo.equals("prv_codi"))
         return new Integer(prv_codiE.getValorInt());
       if (campo.equals("prv_nomb"))
         return prv_codiE.getTextNomb();
       if (campo.equals("pro_codi"))
         return new Integer(jtFra.getValorInt(nLin,0));
       if (campo.equals("pro_nomb"))
         return jtFra.getValString(nLin,1);
       if (campo.equals("fcl_canti"))
         return new Double(jtFra.getValorDec(nLin,2));
       if (campo.equals("fcl_prcom"))
         return new Double(jtFra.getValorDec(nLin,3));
       if (campo.equals("fcl_impor"))
         return new Double(jtFra.getValorDec(nLin,6));

       if (campo.equals("acc_ano"))
         return jtFra.getValorInt(nLin,10)==0?null: new Integer(jtFra.getValInt(nLin,10));
       if (campo.equals("acc_nume"))
         return jtFra.getValorInt(nLin,10)==0?null: new Integer(jtFra.getValInt(nLin,11));
       if (campo.equals("acl_nulin"))
         return jtFra.getValorInt(nLin,10)==0?null: new Integer(jtFra.getValInt(nLin,14));
       if (campo.equals("acc_fecalb"))
         return Formatear.getDate(jtFra.getValString(nLin,13),"dd-MM-yyyy");
       if (campo.equals("acl_canti"))
         return jtFra.getValorInt(nLin,10)==0?null: new Double(jtFra.getValorDec(nLin,7));
       if (campo.equals("acl_prcom"))
         return jtFra.getValorInt(nLin,10)==0?null: new Double(jtFra.getValorDec(nLin,8));

       if (campo.equals("fcc_kglin"))
         return new Double(fcc_kglinE.getValorDec());
       if (campo.equals("fcc_sumlin"))
         return new Double(fcc_implinE.getValorDec());
       if (campo.equals("fcc_sumtot"))
         return new Double(fcc_imptotE.getValorDec());

     } catch (Exception k)
     {
       Logger.getLogger(pdfaccom.class.getName()).log(Level.SEVERE, null, k);
       throw new JRException(k);
     }
     return null;
   }
   void ponLinAlbFact(boolean swFact)
   {
     try {
       s = "UPDATE v_albacol set acl_totfra="+(swFact?-1:0)+
           " WHERE emp_codi = " + emp_codiE.getValorInt() +
           " and acc_ano = " + jtAlb.getValorInt(7) +
           " and acc_nume = " + jtAlb.getValorInt(8) +
           " and acc_serie = '" + jtAlb.getValString(6) + "'" +
           " and acl_nulin = " + jtAlb.getValorInt(10);
       dtAdd.executeUpdate(s);
       dtAdd.commit();
     } catch (SQLException k)
     {
       Error("Error al Actualizar datos de Linea Albaran",k);
     }
   }
}

