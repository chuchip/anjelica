package gnu.chu.anjelica.compras;

import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.sql.Albacoc;
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
import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 * <p>Titulo:   PDFACTRA </p>
 * <p>Descripción: Mantenimiento FACTURAS DE Transportistas
 * <p>Copyright: Copyright (c) 2005-2012
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

public class pdfactra extends ventanaPad   implements PAD
{
  int nLiFra=0;
  boolean swLlenaAlb=false; // Indica si ya se ha llenado una vez el grid de Albaranes.
  CTextField prv_codiE = new CTextField();
  double impTra;
  String s;
  int numDec=2;
  boolean modPrecio=false;
  boolean admin=false;
  CPanel Pinicial = new CPanel();
  CPanel Pcabe = new CPanel();
  CLabel cLabel1 = new CLabel();
  CLinkBox tra_codiE = new CLinkBox();
  CLabel cLabel2 = new CLabel();
  CTextField eje_numeE = new CTextField(Types.DECIMAL,"###9");
  CTextField frt_numeE = new CTextField(Types.DECIMAL,"#####9");
  CLabel cLabel3 = new CLabel();
  CLabel cLabel4 = new CLabel();
  CTextField frt_fechaE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CCheckBox frt_traspE = new CCheckBox("0","-1");
  CPanel cPanel3 = new CPanel();
  CLabel cLabel5 = new CLabel();
  TitledBorder titledBorder2=new TitledBorder("");
  CTextField frt_nufrtrE = new CTextField();
  CLabel cLabel6 = new CLabel();
  CTextField frt_fefrtrE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CPanel Ppie = new CPanel();
  CLabel frt_implinL = new CLabel();
  CTextField frt_implinE = new CTextField(Types.DECIMAL,"----,--9.99");
  CTextField frt_imptotE = new CTextField(Types.DECIMAL,"----,--9.99");
  CLabel frt_imptotL = new CLabel();
  CTextField tap_imporE = new CTextField(Types.DECIMAL,"----,--9.99");
  CLabel frt_dtoppL = new CLabel();
  CTextField frt_dtoppE = new CTextField(Types.DECIMAL,"#9.99");
  CLabel cLabel7 = new CLabel();
  CTextField frt_dtocomE = new CTextField(Types.DECIMAL,"#9.99");
  CLabel cLabel8 = new CLabel();
  CTextField frt_pivaE = new CTextField(Types.DECIMAL,"#9.99");
  CLabel cLabel9 = new CLabel();
  CTextField frt_impiva = new CTextField(Types.DECIMAL,"----,--9.99");
  CLabel cLabel10 = new CLabel();
  CTextField frt_preequE = new CTextField(Types.DECIMAL,"#9.99");
  CTextField frt_impreeE = new CTextField(Types.DECIMAL,"----,--9.99");
  CLabel cLabel11 = new CLabel();
  CLabel cLabel12 = new CLabel();
  CTextField frt_kglinE = new CTextField(Types.DECIMAL,"----,--9.99");
  CLabel cLabel14 = new CLabel();
  CTextField frt_numlinE=new CTextField(Types.DECIMAL,"##9");
//  CButton Baceptar = new CButton("Aceptar",Iconos.getImageIcon("check"));
//  CButton Bcancelar = new CButton("Cancelar",Iconos.getImageIcon("cancel"));
  CTextField frt_alccamE = new CTextField(Types.CHAR, "?", 1);
  CTextField dcc_ejerE = new CTextField(Types.DECIMAL, "###9");
  CTextField acc_serieE = new CTextField(Types.CHAR, "X");
  CTextField frt_numalbE = new CTextField(Types.DECIMAL, "####9");
  CTextField dcl_numliE = new CTextField(Types.DECIMAL, "##9");
  CTextField acc_fecalbE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CTextField frl_cantiE = new CTextField(Types.DECIMAL, "---,--9.99");
  CTextField frl_canalbE = new CTextField(Types.DECIMAL, "---,--9.99");
  CTextField tap_codiE = new CTextField(Types.DECIMAL, "##9");
  CTextField frt_numliE = new CTextField(Types.DECIMAL, "##9");

  CGridEditable jtFra = new CGridEditable(12)
  {

    public void cambiaColumna(int col,int colNueva, int row)
    {
      try
      {
      } catch (Exception k)
      {
        Error("Error en Cambia columna de JtFra",k);
      }
    }
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

    public int cambiaLinea(int row, int col)
    {
      return cambiaLinAlb(row);
    }
    public void afterCambiaLinea()
    {
      try
      {
        actAcumFra();
      } catch (Exception k)
      {
        Error("Error al Actualizar Acumulado de Factura",k);
        return;
      }
    }
  };
  JTabbedPane Tprinci = new JTabbedPane();
  CPanel PFactura = new CPanel();
  CPanel Palbcom = new CPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  Cgrid jtAlb = new Cgrid(7)
  {
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
  CTextField emp_codiE = new CTextField(Types.DECIMAL,"#9");
  CLabel cLabel16 = new CLabel();
  CLinkBox fpa_codiE = new CLinkBox();
  CPanel Pvtos = new CPanel();
  CPanel cPanel1 = new CPanel();
  pLibVto jtVto;
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CButton BirGrid = new CButton();
  CTextField fcl_cantiE = new CTextField(Types.DECIMAL,"---,--9.99");
  CTextField fcl_prcomE = new CTextField(Types.DECIMAL,"--,--9.999");
  CTextField fcl_tipdesE = new CTextField(Types.CHAR,"X");
  CTextField fcl_dtoE = new CTextField(Types.DECIMAL,"##9.99");
  CTextField fcl_implinE = new CTextField(Types.DECIMAL,"----,--9.99");
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  CLabel frt_implinL1 = new CLabel();
  CTextField frt_basimpE = new CTextField(Types.DECIMAL,"----,--9.99");
  conexion ctCom;
  CPanel cPanel2 = new CPanel();
  CLabel cLabel18 = new CLabel();
  CTextField feinalE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel cLabel19 = new CLabel();
  CTextField fefialE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CButton BinsAlb = new CButton(Iconos.getImageIcon("insertar"));
  CPanel PdescCam = new CPanel();
  CCheckBox frt_caltotE = new CCheckBox("S","N");
  CTextField tpc_codiE = new CTextField(Types.DECIMAL,"###9");
  CLabel cLabel13 = new CLabel();
  CTextField frt_prekilE = new CTextField(Types.DECIMAL,"---9.999");
  CLabel frt_imptotL1 = new CLabel();
  CLabel tap_nombL = new CLabel();
  CLinkBox div_codiE = new CLinkBox();
  CLabel div_codiL = new CLabel();
  CTextField frt_kgalbaE = new CTextField(Types.DECIMAL,"----,--9.99");
  CLabel cLabel17 = new CLabel();
  double frtPrekil_Ant=0;
  CCheckBox opSobAlb = new CCheckBox("S","N");
  public pdfactra(EntornoUsuario eu, Principal p)
 {
   this(eu,p,null);
 }
 public pdfactra(EntornoUsuario eu, Principal p,Hashtable ht)
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
       if (admin)
         modPrecio=true;
     }
     setTitulo( "Mant. Fras. Transportistas ");

     if (jf.gestor.apuntar(this))
       jbInit();
     else
       setErrorInit(true);
   }
   catch (Exception e)
   {
     e.printStackTrace();
     setErrorInit(true);
   }
 }

 public pdfactra(gnu.chu.anjelica.menu p, EntornoUsuario eu)
 {
   this(p,eu,null);
 }
 public pdfactra(gnu.chu.anjelica.menu p, EntornoUsuario eu,Hashtable ht)
 {
   EU = eu;
   vl = p.getLayeredPane();
   eje = false;

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
       if (admin)
         modPrecio=true;
     }
    setTitulo("Mant. Fras. Transportistas");

  jbInit();
   }
   catch (Exception e)
   {
     e.printStackTrace();
     setErrorInit(true);
   }
 }

 private void jbInit() throws Exception
 {
   iniciarFrame();
   this.setSize(new Dimension(775, 530));
   this.setVersion("20120304  "+(modPrecio?"-Modificar Precios-":"")+
         (admin?"-ADMINISTRADOR-":"")+")");

   strSql = "SELECT emp_codi,eje_nume,frt_nume " +
       " FROM fratraca WHERE emp_codi = " + EU.em_cod +
       " ORDER BY eje_nume,frt_nume ";

   confGrid();
   statusBar = new StatusBar(this);
   nav = new navegador(this, dtCons, false,  navegador.NORMAL);
   jtVto=new pLibVto(statusBar);
   conecta();
   iniciar(this);
   fpa_codiE.setAceptaNulo(false);
   emp_codiE.setEnabled(false);
   emp_codiE.setBounds(new Rectangle(52, 5, 27, 18));

   frt_traspE.setEnabled(false);
   frt_traspE.setBounds(new Rectangle(622, 2, 110, 18));
   frt_numlinE.setEnabled(false);
   frt_numlinE.setBounds(new Rectangle(511, 20, 41, 18));

   Pcabe.setBorder(BorderFactory.createRaisedBevelBorder());
   Pcabe.setMaximumSize(new Dimension(722, 55));
   Pcabe.setMinimumSize(new Dimension(722, 55));
   Pcabe.setPreferredSize(new Dimension(722, 55));
   Pcabe.setLayout(null);
   Pinicial.setLayout(gridBagLayout2);
   cLabel1.setText("Transport.");
   cLabel1.setBounds(new Rectangle(6, 26, 65, 18));
   tra_codiE.setAncTexto(40);
   tra_codiE.setAceptaNulo(false);
   tra_codiE.setCeroIsNull(true);
   tra_codiE.setBounds(new Rectangle(68, 26, 296, 18));
    cLabel2.setText("Ejerc.");
    cLabel2.setBounds(new Rectangle(81, 5, 35, 19));
    cLabel3.setText("Factura");
    cLabel3.setBounds(new Rectangle(152, 5, 49, 16));
    cLabel4.setText("Fecha Fra");
    cLabel4.setBounds(new Rectangle(260, 5, 59, 16));
    frt_traspE.setText("Traspasado");
    cPanel3.setBorder(BorderFactory.createLineBorder(Color.black));
    cPanel3.setText("Proveedor");
    cPanel3.setBounds(new Rectangle(398, 3, 315, 22));
    cPanel3.setLayout(null);
    cLabel5.setText("Fra. Transp.");
    cLabel5.setBounds(new Rectangle(3, 1, 72, 17));
    titledBorder2.setTitleFont(new java.awt.Font("Lucida Console", 2, 12));
    titledBorder2.setTitle("Proveedor");
    cLabel6.setRequestFocusEnabled(true);
    cLabel6.setText("Fec.Fra Tra");
    cLabel6.setBounds(new Rectangle(174, 2, 67, 17));
    Ppie.setBorder(BorderFactory.createLoweredBevelBorder());
    Ppie.setMaximumSize(new Dimension(743, 68));
    Ppie.setMinimumSize(new Dimension(743, 68));
    Ppie.setPreferredSize(new Dimension(743, 68));
    Ppie.setLayout(null);
    frt_implinL.setText("Imp. Lineas");
    frt_implinL.setBounds(new Rectangle(4, 2, 70, 18));
    frt_imptotL.setText("Total Fra");
    frt_imptotL.setBounds(new Rectangle(465, 3, 57, 18));
    frt_dtoppL.setText("% Dto PP");
    frt_dtoppL.setBounds(new Rectangle(153, 2, 51, 18));
    cLabel7.setText("% Dto Com");
    cLabel7.setBounds(new Rectangle(153, 21, 57, 18));
    cLabel8.setText("% IVA");
    cLabel8.setBounds(new Rectangle(260, 3, 39, 18));
    cLabel9.setText("Imp. Iva");
    cLabel9.setBounds(new Rectangle(338, 2, 50, 18));
    cLabel10.setText("% RE");
    cLabel10.setBounds(new Rectangle(257, 21, 39, 18));
    cLabel11.setText("Imp.RE");
    cLabel11.setBounds(new Rectangle(335, 20, 50, 18));
    cLabel12.setText("Kg. Lineas");
    cLabel12.setBounds(new Rectangle(4, 39, 63, 18));
    cLabel14.setText("N.Lineas");
    cLabel14.setBounds(new Rectangle(459, 20, 54, 17));
    PFactura.setLayout(borderLayout2);
    Palbcom.setLayout(borderLayout1);
    cLabel15.setText("Empresa");
    cLabel15.setBounds(new Rectangle(3, 5, 50, 15));
    cLabel16.setText("Forma Pago");
    cLabel16.setBounds(new Rectangle(9, 4, 66, 19));

    fpa_codiE.setAncTexto(30);
    fpa_codiE.setBounds(new Rectangle(80, 4, 277, 19));
    Pvtos.setLayout(gridBagLayout1);
    cPanel1.setBorder(BorderFactory.createLoweredBevelBorder());
    cPanel1.setMaximumSize(new Dimension(416, 27));
    cPanel1.setMinimumSize(new Dimension(416, 27));
    cPanel1.setPreferredSize(new Dimension(416, 27));
    cPanel1.setLayout(null);


    BirGrid.setBounds(new Rectangle(367, 47, 10, 10));
    eje_numeE.setBounds(new Rectangle(114, 5, 36, 18));
    frt_numeE.setBounds(new Rectangle(195, 5, 57, 18));
    frt_fechaE.setBounds(new Rectangle(316, 5, 79, 19));
    Tprinci.setMaximumSize(new Dimension(758, 337));
    Tprinci.setMinimumSize(new Dimension(758, 337));
    Tprinci.setPreferredSize(new Dimension(758, 337));
    Bcancelar.setBounds(new Rectangle(615, 40, 117, 25));
    Baceptar.setBounds(new Rectangle(482, 40, 127, 25));
    frt_dtocomE.setBounds(new Rectangle(214, 21, 38, 18));
    frt_impreeE.setBounds(new Rectangle(384, 20, 73, 18));
    frt_preequE.setBounds(new Rectangle(291, 20, 38, 18));
    frt_kglinE.setBounds(new Rectangle(77, 39, 71, 18));
    frt_dtoppE.setBounds(new Rectangle(214, 3, 38, 18));
    frt_imptotE.setBounds(new Rectangle(534, 2, 73, 18));
    frt_impiva.setBounds(new Rectangle(384, 2, 73, 18));
    frt_pivaE.setBounds(new Rectangle(294, 2, 38, 18));
    frt_implinE.setBounds(new Rectangle(75, 2, 73, 18));
    frt_fefrtrE.setBounds(new Rectangle(240, 2, 72, 17));
    frt_nufrtrE.setBounds(new Rectangle(71, 1, 94, 17));


    frt_implinL1.setBounds(new Rectangle(4, 20, 70, 18));
    frt_implinL1.setText("Base Impon.");
    frt_basimpE.setBounds(new Rectangle(75, 20, 73, 18));
    cPanel2.setBorder(BorderFactory.createLineBorder(Color.black));
    cPanel2.setMaximumSize(new Dimension(32767, 32767));
    cPanel2.setBounds(new Rectangle(402, 45, 308, 23));
    cPanel2.setLayout(null);
    cLabel18.setText("De Fecha");
    cLabel18.setBounds(new Rectangle(3, 3, 57, 17));
    cLabel19.setText("A Fecha");
    cLabel19.setBounds(new Rectangle(139, 3, 57, 17));
    BinsAlb.setBounds(new Rectangle(272, 2, 32, 21));
    BinsAlb.setMargin(new Insets(0, 0, 0, 0));
    BinsAlb.setToolTipText("Insertar Alb. de compras");
    feinalE.setBounds(new Rectangle(197, 3, 72, 17));
    fefialE.setBounds(new Rectangle(61, 3, 72, 17));

    frt_caltotE.setText("Calc. Sobre Total");
    frt_caltotE.setBounds(new Rectangle(554, 20, 126, 19));
    tpc_codiE.setBounds(new Rectangle(68, 45, 38, 18));
    tpc_codiE.setCeroIsNull(true);
    cLabel13.setBounds(new Rectangle(18, 45, 42, 18));
    cLabel13.setText("Tarifa");
    frt_prekilE.setBounds(new Rectangle(398, 39, 59, 18));
    frt_imptotL1.setBounds(new Rectangle(340, 39, 57, 18));
    frt_imptotL1.setText("Precio Kg");

    tap_nombL.setBackground(Color.orange);
    tap_nombL.setOpaque(true);
    tap_nombL.setBounds(new Rectangle(109, 45, 252, 18));
    div_codiE.setBounds(new Rectangle(490, 26, 223, 18));
    div_codiE.setAncTexto(30);
    div_codiL.setBounds(new Rectangle(451, 25, 37, 19));
    div_codiL.setText("Divisa");
    frt_kgalbaE.setEnabled(false);
    frt_kgalbaE.setBounds(new Rectangle(246, 39, 71, 18));
    cLabel17.setBounds(new Rectangle(173, 39, 70, 18));
    cLabel17.setText("Kg. Albar.");
    if (! admin)
      opSobAlb.setEnabled(false);
    opSobAlb.setBounds(new Rectangle(682, 20, 82, 19));
    opSobAlb.setToolTipText("Sobreescribir portes de Albaran");
    opSobAlb.setText("Sobr.Alb.");
    this.getContentPane().add(Pinicial, BorderLayout.CENTER);
    cPanel3.add(cLabel5, null);
    cPanel3.add(frt_nufrtrE, null);
    cPanel3.add(frt_fefrtrE, null);
    cPanel3.add(cLabel6, null);
    Pcabe.add(BirGrid, null);
    Pcabe.add(div_codiL, null);
    Pcabe.add(div_codiE, null);
    Pcabe.add(cPanel2, null);
    Pcabe.add(cLabel1, null);
    Pcabe.add(tra_codiE, null);
    Pcabe.add(tpc_codiE, null);
    Pcabe.add(cLabel13, null);
    Pcabe.add(emp_codiE, null);

    Pcabe.add(frt_numeE, null);
    Pcabe.add(eje_numeE, null);


    cPanel2.add(cLabel18, null);
    cPanel2.add(fefialE, null);
    cPanel2.add(feinalE, null);
    cPanel2.add(cLabel19, null);
    cPanel2.add(BinsAlb, null);
    Pcabe.add(tap_nombL, null);
    Pcabe.add(frt_fechaE, null);
    Pcabe.add(cLabel4, null);
    Pcabe.add(cLabel3, null);
    Pcabe.add(cLabel15, null);
    Pcabe.add(cLabel2, null);
    Pcabe.add(cPanel3, null);
    Ppie.add(frt_imptotL, null);
    Ppie.add(frt_implinL, null);
    Ppie.add(frt_traspE, null);
    Ppie.add(frt_implinE, null);
    Ppie.add(frt_dtoppE, null);
    Ppie.add(frt_dtoppL, null);
    Ppie.add(cLabel8, null);
    Ppie.add(frt_pivaE, null);
    Ppie.add(cLabel9, null);
    Ppie.add(frt_impiva, null);
    Ppie.add(frt_imptotE, null);
    Ppie.add(frt_preequE, null);
    Ppie.add(cLabel11, null);
    Ppie.add(frt_basimpE, null);
    Ppie.add(frt_implinL1, null);
    Ppie.add(frt_dtocomE, null);
    Ppie.add(cLabel7, null);
    Ppie.add(frt_impreeE, null);
    Ppie.add(cLabel10, null);
    Ppie.add(cLabel17, null);
    Ppie.add(frt_kgalbaE, null);
    Ppie.add(frt_imptotL1, null);
    Ppie.add(frt_prekilE, null);
    Ppie.add(cLabel12, null);
    Ppie.add(frt_kglinE, null);
    Ppie.add(Bcancelar, null);
    Ppie.add(Baceptar, null);
    Ppie.add(cLabel14, null);
    Ppie.add(frt_caltotE, null);
    Ppie.add(opSobAlb, null);
    Ppie.add(frt_numlinE, null);
    jtVto.setMaximumSize(new Dimension(375, 267));
    jtVto.setMinimumSize(new Dimension(375, 267));
    jtVto.setPreferredSize(new Dimension(375, 267));

    Pinicial.add(Pcabe,     new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 0, 0, 0), 7, 14));
    Pinicial.add(Tprinci,   new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 1, 0, 1), 0, 0));
    Pinicial.add(Ppie,    new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 1, 0, 1), 0, 0));
    Tprinci.add(PFactura,  "Factura");
    PFactura.add(jtFra, BorderLayout.CENTER);
    Tprinci.add(Palbcom,    "Alb.Compras");
    Tprinci.add(PdescCam,   "Desc.Camion");
    Tprinci.add(Pvtos,   "Vencimientos");
    Palbcom.add(jtAlb, BorderLayout.CENTER);
    Pvtos.add(cPanel1,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(3, 0, 0, 0), -45, 0));
    cPanel1.add(fpa_codiE, null);
    cPanel1.add(cLabel16, null);
    Pvtos.add(jtVto,        new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(7, 0, 2, 0), 0, 0));

    Baceptar.setText("F4 Aceptar");
 }
 public void iniciarVentana() throws Exception
 {
   Pcabe.setButton(KeyEvent.VK_F2,BirGrid);
   jtFra.setButton(KeyEvent.VK_F4,Baceptar);
   jtVto.setButton(KeyEvent.VK_F4,Baceptar);

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
      ctCom=ctUp;

   Pcabe.setButton(KeyEvent.VK_F4,Baceptar);
   jtAlb.setButton(KeyEvent.VK_F4,Baceptar);


   eje_numeE.setColumnaAlias("eje_nume");
   frt_numeE.setColumnaAlias("frt_nume");
   frt_fechaE.setColumnaAlias("frt_fecha");
   frt_nufrtrE.setColumnaAlias("frt_nufrtr");
   frt_fefrtrE.setColumnaAlias("frt_fefrtr");
   tpc_codiE.setColumnaAlias("tap_codi");
   fpa_codiE.setColumnaAlias("fpa_codi");
   activar(false);
   activarEventos();
 }
 private void activarEventos()
 {
   BinsAlb.addActionListener(new ActionListener()
   {
     public void actionPerformed(ActionEvent e)
     {
       BinsAlb_actionPerformed();
     }
   });
   jtFra.addMouseListener(new MouseAdapter()
   {
     public void mouseClicked(MouseEvent e)
     {
       if (jtFra.isEnabled()==false && (nav.pulsado==navegador.ADDNEW || nav.pulsado==navegador.EDIT))
         irGridFra();
     }
   });
   tpc_codiE.addFocusListener(new FocusAdapter()
   {
     public void focusLost(FocusEvent e)
     {
       if (!tpc_codiE.isQuery())
       {
         tap_nombL.setText(getNombTap(tra_codiE.getValorInt(),
                                      tpc_codiE.getValorInt()));
       }
     }
   });

   jtAlb.addMouseListener(new MouseAdapter()
   {
     public void mouseClicked(MouseEvent e)
     {
       if (! jtAlb.isEnabled() || jtAlb.isVacio() || e.getClickCount()<2)
         return;
       jtAlb.Binser.doClick();
     }
   });
   BirGrid.addFocusListener(new FocusAdapter()
   {
     public void focusGained(FocusEvent e)
     {
       irGridFra();
     }
   });
   frt_caltotE.addActionListener(new ActionListener()
   {
     public void actionPerformed(ActionEvent e)
     {
       if (nav.pulsado==navegador.ADDNEW || nav.pulsado==navegador.EDIT)
         actAcumFra();
     }
   });
   fpa_codiE.addCambioListener(new gnu.chu.eventos.CambioListener()
   {
     public void cambio(gnu.chu.eventos.CambioEvent event)
     {
       llenaGridVtos();
     }
   });

 }

 String getNombTap(int traCodi, int tapCodi)
 {
   try
   {
     s = "SELECT tap_nomb FROM taripor where tra_codi = " + traCodi +
         " and tap_codi = " + tapCodi;
     if (dtStat.select(s))
       return dtStat.getString("tap_nomb");
   }
   catch (Exception k)
   {
     Error("Error al recoger Nombre de Tarifa", k);
   }
   return "";
 }

 private void llenaGridVtos()
 {
   try
   {
     if (!fpa_codiE.hasCambio())
       return;
     fpa_codiE.resetCambio();
     jtVto.ponValoresDef(frt_imptotE.getValorDec(), fpa_codiE.getValorInt(), frt_fefrtrE.getText());
   }
   catch (Exception k)
   {
     Error("Error al poner vtos por defecto", k);
   }
 }
 boolean  irGridFra()
 {
   if (! checkCabe())
     return false;


   try
   {
     llenaGridAlb();
   }
   catch (Exception k)
   {
     Error("Error al ir al grid de Albaranes Pend.", k);
     return false;
   }
   jtFra.setEnabled(true);
   jtAlb.setEnabled(true);

   jtFra.requestFocusInicio();
   return true;
 }

 boolean checkCabe()
 {
   if (frt_fechaE.getError() || frt_fechaE.isNull())
   {
     mensajeErr("Fecha Factura NO es Valida");
     frt_fechaE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
     frt_fechaE.requestFocus();
     return false;
   }

   if (frt_fefrtrE.getError() || frt_fefrtrE.isNull())
   {
     mensajeErr("Fecha Factura de Proveedor NO es Valida");
     frt_fefrtrE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
     frt_fefrtrE.requestFocus();
     return false;
   }

   if (frt_nufrtrE.isNull())
   {
     mensajeErr("Introduzca Codigo Factura de Proveedor");
     frt_nufrtrE.setText("0");
     frt_nufrtrE.requestFocus();
     return false;
   }
   if (! tra_codiE.controla())
   {
     mensajeErr("Transportista NO Valido");
     return false;
   }
   return true;
 }

 void llenaGridAlb() throws Exception
 {
   if (swLlenaAlb)
     return;
   swLlenaAlb=true;
   jtAlb.removeAllDatos();
   s="SELECT c.acc_ano,c.acc_serie,c.acc_nume,c.acc_fecrec,c.prv_codi,pv.prv_nomb, "+
       " sum(acl_canti) as acl_canti "+
       " FROM v_albacol as l, v_albacoc as c,v_proveedo pv "+
        " WHERE c.emp_codi = "+EU.em_cod+
        " and l.emp_codi = "+EU.em_cod+
        " and pv.emp_codi = "+EU.em_cod+
        " and l.acl_porpag = 0 "+
        " and pv.prv_codi = c.prv_codi "+
        " and c.acc_ano = l.acc_ano "+
        " and c.acc_nume = l.acc_nume "+
        " and c.acc_serie = l.acc_serie "+
       " AND c.frt_nume = 0  "+
       " and c.acc_portes = 'D' "+
       " group by c.acc_ano,c.acc_serie,c.acc_nume,c.acc_fecrec,c.prv_codi,pv.prv_nomb "+
       " order by c.acc_ano desc,c.acc_serie,c.acc_nume desc ";

   if (! dtCon1.select(s))
   {
     mensajeErr("Sin albaranes pendientes");
     return;
   }
//   String alb="",alb1="";
//   alb="";
   do
   {
     insLiAlb0();
   } while (dtCon1.next());

   jtAlb.requestFocusInicio();
 }

private void insLiAlb0() throws IllegalArgumentException, ParseException,
    SQLException
{
  ArrayList v=new ArrayList();
  v.add(dtCon1.getString("acc_ano"));
  v.add(dtCon1.getString("acc_serie"));
  v.add(dtCon1.getString("acc_nume"));
  v.add(dtCon1.getFecha("acc_fecrec"));
  v.add(dtCon1.getString("acl_canti"));
  v.add(dtCon1.getString("prv_codi"));
  v.add(dtCon1.getString("prv_nomb"));
  jtAlb.addLinea(v);
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
   emp_codiE.setEnabled(false);
   if (Integer.parseInt(Formatear.getFechaAct("MM"))>2)
     eje_numeE.setValorDec(EU.ejercicio);
   fpa_codiE.setQuery(true);
   fpa_codiE.resetTexto();
   frt_numeE.requestFocus();
 }

    @Override
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
   v.add(frt_numeE.getStrQuery());
   v.add(frt_fechaE.getStrQuery());
   v.add(frt_nufrtrE.getStrQuery());
   v.add(frt_fefrtrE.getStrQuery());
   v.add(tra_codiE.getStrQuery());
   v.add(tpc_codiE.getStrQuery());
   v.add(fpa_codiE.getStrQuery());
   s = "SELECT emp_codi,eje_nume,frt_nume " +
         " FROM fratraca WHERE emp_codi = " + EU.em_cod ;
   s= creaWhere(s, v, false);
   s+=" ORDER BY eje_nume,frt_nume ";
   Pcabe.setQuery(false);
   fpa_codiE.setQuery(false);
   this.setEnabled(false);
   try
   {
   mensaje("Buscando Datos ... Espere, por favor");
   if (!dtCon1.select(s))
   {
     msgBox("No encontrados Albaranes con estos criterios");
     verDatos(dtCons);
     activaTodo();
     this.setEnabled(true);
     return;
   }
   strSql = s;
   rgSelect();
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

    @Override
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
     s = "SELECT * FROM fratraca WHERE emp_codi = " + emp_codiE.getValorInt() +
         " and eje_nume = " + eje_numeE.getValorInt() +
         " and frt_nume = " + frt_numeE.getValorInt();
     if (!dtStat.select(s))
     {
       mensajeErr("Factura NO encontrada .. SEGURAMENTE SE BORRO");
       activaTodo();
       return;
     }
     if (!setBloqueo(dtAdd, "fratraca",
                     eje_numeE.getValorInt() + "|" + emp_codiE.getValorInt() +
                     "|" + frt_numeE.getValorInt()))
     {
       msgBox(msgBloqueo);
       activaTodo();
       return;
     }
     mensaje("Modificando Factura ....");
     activar(navegador.ADDNEW,true);
     tra_codiE.resetCambio();
//     jtVto.requestFocusInicio();
     jtVto.setProveedor(tra_codiE.getValorInt());
     jtVto.setDatosFra("T",emp_codiE.getValorInt(),eje_numeE.getValorInt(),frt_numeE.getValorInt());

     irGridFra();
     actAcumFra();
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
   jtFra.salirFoco();
   jtFra.procesaAllFoco();

   this.setEnabled(false);
   try
   {
//     actAcumFra();

//    incNumFra();
     borrarFra();
     insCabec();
     insLineas();
     jtVto.actDatos(dtAdd,stUp, 'T',emp_codiE.getValorInt(),eje_numeE.getValorInt(),
                   frt_numeE.getValorInt(),frt_fechaE.getDate(),tra_codiE.getValorInt(),
                   tra_codiE.getTextCombo());

     if (jf != null)
     {
       jf.ht.clear();
       jf.ht.put("%f", eje_numeE.getValorInt() + "|" + emp_codiE.getValorInt() +
                 "|" + frt_numeE.getValorInt());
       jf.guardaMens("C7", jf.ht); // Mod. Fra de Compras
     }
     ctCom.commit();
     resetBloqueo(dtAdd, "fratraca",
                  eje_numeE.getValorInt() + "|" + emp_codiE.getValorInt() +
                  "|" + frt_numeE.getValorInt());

     mensajeErr("Factura ... MODIFICADA");
     jtVto.llenaGrid(dtCon1, 'T', emp_codiE.getValorInt(),
                     eje_numeE.getValorInt(), frt_numeE.getValorInt());
     mensaje("");
     nav.pulsado = navegador.NINGUNO;
     frtPrekil_Ant=frt_prekilE.getValorDec();
   }
   catch (Exception k)
   {
     Error("Error al Insertar Factura", k);
   }
   activaTodo();
 }

 void borrarFra() throws Exception
 {
   s = "DELETE FROM fratraca WHERE emp_codi = " + emp_codiE.getValorInt() +
       " and eje_nume = " + eje_numeE.getValorInt() +
       " and frt_nume = " + frt_numeE.getValorInt();
   stUp.executeUpdate(dtAdd.parseaSql(s));
   s = "SELECT * FROM fratrali WHERE emp_codi = " + emp_codiE.getValorInt() +
       " and eje_nume = " + eje_numeE.getValorInt() +
       " and frt_nume = " + frt_numeE.getValorInt();
   if (! dtCon1.select(s))
     return;
  
   ArrayList<Albacoc> v=new ArrayList();
   do
   {
     if (dtCon1.getString("frt_alccam", true).equals("A"))
     { // Act. Cabecera y Lineas  Albaran
       s = "SELECT frt_ejerc,frt_nume,acc_impokg FROM v_albacoc " +
           "  WHERE emp_codi = " + emp_codiE.getValorInt() +
           " and acc_ano = " + dtCon1.getInt("dcc_ejerc") +
           " and acc_nume = " + dtCon1.getInt("frt_numalb") +
           " and acc_serie = '" + dtCon1.getString("acc_serie") + "'";
        if (! dtBloq.select(s))
          throw new Exception("NO encontrado registro Albaran.\n"+s);
 
       s = "UPDATE v_albacol set acl_prcom = acl_prcom -"+ frtPrekil_Ant +
                " WHERE emp_codi = " + emp_codiE.getValorInt() +
                " and acc_ano = " + dtCon1.getInt("dcc_ejerc") +
                " and acc_nume = " + dtCon1.getInt("frt_numalb") +
                " and acl_porpag = 0 "+
                " and acc_serie = '" + dtCon1.getString("acc_serie") + "'";
       dtAdd.executeUpdate(s,stUp);
      
       Albacoc fc=new Albacoc(dtCon1.getInt("frt_numalb"),dtCon1.getInt("dcc_ejerc"),dtCon1.getString("acc_serie"));
       if (v.indexOf(fc)==-1)
           v.add(fc);
     }
   } while (dtCon1.next());
   int nRow=v.size();
   double impTot;
   for (int n=0;n<nRow;n++)
   {
      s = "SELECT frt_ejerc,frt_nume,acc_impokg,acc_totfra FROM v_albacoc "+
          " WHERE emp_codi = " + emp_codiE.getValorInt() +
           " and acc_ano = " + v.get(n).getAccAno() +
           " and acc_nume = " + v.get(n).getAccNume() +
           " and acc_serie = '" + v.get(n).getAccSerie() + "'";
       if (!dtAdd.select(s,true))
         throw new Exception("NO encontrado registro Cabecera Albaran.\n" + s);
       if (opSobAlb.isSelected()) // Sobreescribir Albaran ?
          frtPrekil_Ant=dtBloq.getDouble("acc_impokg");
//       impTot=pdalbco2.getImporteAlb(dtCon1,emp_codiE.getValorInt(),v.get(n).getAccAno(),
//           v.get(n).getAccSerie(),v.get(n).getAccNume());
       dtAdd.edit();
       dtAdd.setDato("frt_ejerc",0); 
       dtAdd.setDato("frt_nume",0);
       dtAdd.setDato("acc_impokg", opSobAlb.isSelected()?0:dtAdd.getDouble("acc_impokg") - frtPrekil_Ant);
//       dtAdd.setDato("acc_totfra",impTot);
       dtAdd.update();
   }
   
   s = "DELETE FROM fratrali WHERE emp_codi = " + emp_codiE.getValorInt() +
       " and eje_nume = " + eje_numeE.getValorInt() +
       " and frt_nume = " + frt_numeE.getValorInt();
   stUp.executeUpdate(dtAdd.parseaSql(s));
 }

    @Override
 public void canc_edit()
 {
   mensajeErr("Modificacion Cancelada");
   mensaje("");
   try
   {
     resetBloqueo(dtAdd, "fratraca",
                      eje_numeE.getValorInt() + "|" + emp_codiE.getValorInt() +
                      "|" + frt_numeE.getValorInt());
     verDatos(dtCons);
   } catch (Exception k)
   {
      mensajes.mensajeAviso("Error al Quitar el bloqueo\n"+k.getMessage());
   }
   activaTodo();
   nav.pulsado=nav.NINGUNO;
 }

 public void PADAddNew()
 {
   jtAlb.removeAllDatos();
   jtFra.removeAllDatos();
   jtVto.removeAllDatos();
   Pcabe.resetTexto();
   swLlenaAlb=false;
   Ppie.resetTexto();
   tra_codiE.resetCambio();
   activar(navegador.ADDNEW, true);
   emp_codiE.setValorDec(EU.em_cod);
   eje_numeE.setValorDec(EU.ejercicio);
   frt_fechaE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
   frt_numeE.setEnabled(false);
   div_codiE.setEnabled(true);
   tpc_codiE.setText("1");
   fpa_codiE.resetTexto();
   frtPrekil_Ant=0;
   mensaje("Insertando nueva factura ...");
   jtVto.setDatosFra("T",emp_codiE.getValorInt(),0,0);
   Tprinci.setSelectedComponent(PFactura);
   jtVto.requestFocusInicio();
   frt_nufrtrE.requestFocus();
 }
 public boolean checkEdit()
 {
   return checkAddNew();
 }

 public boolean checkDelete()
 {
   try
   {

     nLiFra = 0;
     for (int n = 0; n < jtFra.getRowCount(); n++)
     {
       if (jtFra.getValorDec(n, 7) == 0)
         continue;
       nLiFra++;
       if (isBloqueado(dtAdd, "v_albacoc", jtFra.getValorInt(n, 1) +
                       "|" + emp_codiE.getValorInt() +
                       "|" + jtFra.getValString(n, 2) + "|" +
                       jtFra.getValString(n, 3)))
       {
         mensajes.mensajeUrgente("Albaran " + jtFra.getValorInt(n, 1) +
                    "|" + emp_codiE.getValorInt() +
                    "|" + jtFra.getValString(n, 2) + "|" +
                    jtFra.getValString(n, 3) + " BLOQUEADO");
         jtFra.requestFocus(n, 6);
         return false;
       }
     }
   }
   catch (Exception k)
   {
     Error("Error al Chequear Alta de Factura", k);
   }
   return true;
 }

 public boolean checkAddNew()
 {
   try
   {
     if (! checkCabe())
       return false;
     if (Integer.parseInt(frt_fefrtrE.getFecha("yyyy"))!=eje_numeE.getValorInt())
     {
       if (mensajes.mensajePreguntar("Fecha Fra. de Proveedor NO es del Ejercicio introducido\n�Continuar?")!=mensajes.YES)
       {
         eje_numeE.setText(frt_fefrtrE.getFecha("yyyy"));
         eje_numeE.requestFocus();
         return false;
       }
     }
     jtFra.salirFoco();
     jtFra.procesaAllFoco();

     if (cambiaLinAlb()>=0)
       return false;
     if (frt_numlinE.getValorInt() == 0)
     {
       mensajeErr("Introduzca alguna Linea de Fra");
       jtFra.requestFocusInicio();
       return false;
     }
     if (! fpa_codiE.controla()){
       mensajeErr("Forma de Pago NO valida");
       return false;
     }
     jtVto.jtVto.procesaAllFoco();

     actAcumFra();
     if (jtVto.cambiaLineaVto()>=0)
     {
       jtVto.requestFocus();
       Tprinci.setSelectedComponent(Pvtos);
       return false;
     }
     if (! checkDelete()) return false;
     if (nLiFra==0)
     {
       mensajeErr("Introduzca Alguna linea de Factura");
       jtFra.requestFocusInicio();
       return false;
     }


     if (jtVto.getLinActivas()==0)
     {
       mensajeErr("Introduzca Algun Importe en los Vto.");
       jtVto.requestFocusInicio();
       Tprinci.setSelectedComponent(Pvtos);
       return false;
     }
     if (jtVto.getImpVtos()!=frt_imptotE.getValorDec())
     {
       mensajeErr("Importe de Factura NO coincide con la suma de Vtos");
       jtVto.requestFocusInicio();
       Tprinci.setSelectedComponent(Pvtos);
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
     jtVto.actDatos(dtAdd,stUp, 'T',emp_codiE.getValorInt(),eje_numeE.getValorInt(),
                   frt_numeE.getValorInt(),frt_fechaE.getDate(),tra_codiE.getValorInt(),
                   tra_codiE.getTextCombo());
     ctCom.commit();
     mensajeErr("Factura ... INSERTADA");
     mensaje("");
     if (dtCons.getNOREG())
       rgSelect();

     activaTodo();
     nav.pulsado=nav.NINGUNO;
   } catch (Exception k)
   {
     Error("Error al Insertar Factura",k);
   }

 }

 void incNumFra()  throws Exception
 {
   s = "SELECT max(frt_nume) as frt_nume FROM fratraca "+
       " WHERE emp_codi = " + emp_codiE.getValorInt() +
       " AND eje_nume = " + eje_numeE.getValorInt();
   dtAdd.select(s);
   frt_numeE.setValorDec(dtAdd.getInt("frt_nume",true) + 1);
 }

 void insCabec() throws Exception
 {
   dtAdd.addNew("fratraca");
   dtAdd.setDato("eje_nume", eje_numeE.getValorInt());
   dtAdd.setDato("emp_codi", emp_codiE.getValorInt());
   dtAdd.setDato("frt_nume", frt_numeE.getValorInt());
   dtAdd.setDato("frt_fecha", frt_fechaE.getText(), "dd-MM-yyyy");
   dtAdd.setDato("frt_nufrtr", frt_nufrtrE.getText());
   dtAdd.setDato("frt_fefrtr", frt_fefrtrE.getText(), "dd-MM-yyyy");
   dtAdd.setDato("tra_codi", tra_codiE.getValorInt());
   dtAdd.setDato("frt_trasp", -1); // No Traspasado
   dtAdd.setDato("div_codi", div_codiE.getText());
   dtAdd.setDato("fpa_codi", fpa_codiE.getText());

   dtAdd.setDato("frt_implin", frt_implinE.getValorDec());
   dtAdd.setDato("frt_dtopp", frt_dtoppE.getValorDec());
   dtAdd.setDato("frt_dtocom", frt_dtocomE.getValorDec());
   dtAdd.setDato("frt_basimp", frt_basimpE.getValorDec());
   dtAdd.setDato("frt_piva", frt_pivaE.getValorDec());
   dtAdd.setDato("frt_impiva", frt_impiva.getValorDec());
   dtAdd.setDato("frt_preequ", frt_preequE.getValorDec());
   dtAdd.setDato("frt_impree", frt_impreeE.getValorDec());
   dtAdd.setDato("frt_caltot", frt_caltotE.getSelecion());
   dtAdd.setDato("tap_codi", tpc_codiE.getValorInt());
   dtAdd.setDato("frt_imptot", frt_imptotE.getValorDec());
   dtAdd.setDato("frt_prekil", frt_prekilE.getValorDec());
   dtAdd.update(stUp);
 }
 /**
  * Inserta todas las lineas.
  * @throws Exception 
  */
 void insLineas() throws Exception
 {
   int nRow=jtFra.getRowCount();
   int nLin=1;
   String alccam;
   ArrayList<Albacoc> v=new ArrayList();
   for (int n=0;n<nRow;n++)
   {
     if (jtFra.getValorDec(n,7)==0)
       continue;
     alccam=jtFra.getValString(n,0);
     if (alccam.trim().equals(""))
       alccam=" ";
     else
       alccam=alccam.substring(0,1);
     dtAdd.addNew("fratrali");
     dtAdd.setDato("eje_nume", eje_numeE.getValorInt());
     dtAdd.setDato("emp_codi", emp_codiE.getValorInt());
     dtAdd.setDato("frt_nume", frt_numeE.getValorInt());
     dtAdd.setDato("frt_numlin", nLin);
     dtAdd.setDato("frt_alccam",alccam);
     dtAdd.setDato("dcc_ejerc", jtFra.getValorInt(n, 1));
     dtAdd.setDato("acc_serie", jtFra.getValString(n, 2));
     dtAdd.setDato("frt_numalb", jtFra.getValString(n, 3));
     dtAdd.setDato("dcl_numli", jtFra.getValString(n,4 ));
//     dtAdd.setDato("frl_canalb",jtFra.getValorDec(n,6));
     dtAdd.setDato("frl_canti",jtFra.getValorDec(n,7));
     dtAdd.setDato("tra_codi",tra_codiE.getValorInt());
     dtAdd.setDato("tap_codi",jtFra.getValorInt(n,8));
     dtAdd.setDato("tap_impor",jtFra.getValorDec(n,9));
     dtAdd.update(stUp);
//     double impokg=0;
     if (alccam.equals("A"))
     { // Act. Cabecera Albaran
       s="SELECT frt_ejerc,frt_nume,acc_impokg FROM v_albacoc "+
          "  WHERE emp_codi = "+emp_codiE.getValorInt()+
              " and acc_ano = "+jtFra.getValorInt(n,1)+
              " and acc_nume = "+jtFra.getValorInt(n,3)+
              " and acc_serie = '"+jtFra.getValString(n,2)+"'";
        if (! dtAdd.select(s))
          throw new Exception("NO encontrado registro Albaran.\n"+s);

       if (! opSobAlb.isSelected() && dtAdd.getDouble("acc_impokg")!=0)
          aviso("pdfratra: IMPORTE de portes != 0 ("+
                                               dtAdd.getDouble("acc_impokg")+") sobre albaran: "+s+
                                               "\n en fra: "+frt_numeE.getValorInt());
       s = "UPDATE v_albacol set acl_prcom = acl_prcom + "+( frt_prekilE.getValorDec())+
           " WHERE emp_codi = " + emp_codiE.getValorInt() +
           " and acc_ano = " + jtFra.getValorInt(n, 1) +
           " and acc_nume = " + jtFra.getValorInt(n, 3) +
           " and acl_porpag = 0 "+
           " and acc_serie = '" + jtFra.getValString(n, 2) + "'";
       dtAdd.executeUpdate(s, stUp);
       Albacoc fc=new Albacoc(jtFra.getValorInt(n, 3),jtFra.getValorInt(n, 1),jtFra.getValString(n, 2) );
       if (v.indexOf(fc)==-1)
           v.add(fc);
     }
     nLin++;
   }
   nRow=v.size();
   double impTot;
   for (int n=0;n<nRow;n++)
   {
      s = "SELECT frt_ejerc,frt_nume,acc_impokg,acc_totfra  FROM v_albacoc "+
          " WHERE emp_codi = " + emp_codiE.getValorInt() +
           " and acc_ano = " + v.get(n).getAccAno() +
           " and acc_nume = " + v.get(n).getAccNume() +
           " and acc_serie = '" + v.get(n).getAccSerie() + "'";
       if (!dtAdd.select(s,true))
         throw new Exception("NO encontrado registro Cabecera Albaran.\n" + s);
      
//       impTot=pdalbco2.getImporteAlb(dtCon1,emp_codiE.getValorInt(),v.get(n).getAccAno(),
//           v.get(n).getAccSerie(),v.get(n).getAccNume());
       dtAdd.edit();
       dtAdd.setDato("frt_ejerc",eje_numeE.getValorInt());
       dtAdd.setDato("frt_nume",frt_numeE.getValorInt());
       dtAdd.setDato("acc_impokg",opSobAlb.isSelected()? frt_prekilE.getValorDec():dtAdd.getDouble("acc_impokg")+frt_prekilE.getValorDec());
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
 }

    @Override
 public void PADDelete()
 {
   try
   {
     s = "SELECT * FROM fratraca WHERE emp_codi = " + emp_codiE.getValorInt() +
         " and eje_nume = " + eje_numeE.getValorInt() +
         " and frt_nume = " + frt_numeE.getValorInt();
     if (!dtStat.select(s))
     {
       mensajeErr("Factura NO encontrada .. SEGURAMENTE SE BORRO");
       activaTodo();
       return;
     }
     if (jtVto.hasPagos(dtAdd, 'T', emp_codiE.getValorInt(), eje_numeE.getValorInt(),
                        frt_numeE.getValorInt()))
     {
       mensajeErr("Ya se han realizado pagos sobre esta Factura ... IMPOSIBLE BORRAR");
       activaTodo();
       return;
     }

     if (!setBloqueo(dtAdd, "fratraca",
                     eje_numeE.getValorInt() + "|" + emp_codiE.getValorInt() +
                     "|" + frt_numeE.getValorInt()))
     {
       msgBox(msgBloqueo);
       activaTodo();
       return;
     }
     actAcumFra();
     Baceptar.setEnabled(true);
     Bcancelar.setEnabled(true);
     Bcancelar.requestFocus();
     mensaje("Borrando Factura ....");
   }
   catch (Exception k)
   {
     Error("Error al editar registro", k);
   }

 }


 public void ej_delete1()
 {
   mensaje("Espere, por favor .. Borrando Factura");
   this.setEnabled(false);
   try
   {
     borrarFra();
     jtVto.delDatos(dtAdd, stUp, 'T', emp_codiE.getValorInt(),
                    eje_numeE.getValorInt(),
                    frt_numeE.getValorInt());
     if (jf != null)
     {
       jf.ht.clear();
       jf.ht.put("%f", eje_numeE.getValorInt() + "|" + emp_codiE.getValorInt() +
                 "|" + frt_numeE.getValorInt());
       jf.guardaMens("C6", jf.ht); // BORRADO  Fra de Transportistas
     }

     ctCom.commit();
     resetBloqueo(dtAdd, "fratraca",
                  eje_numeE.getValorInt() + "|" + emp_codiE.getValorInt() +
                  "|" + frt_numeE.getValorInt());

     mensajeErr("Factura ... BORRADA");
     mensaje("");
     rgSelect();
     activaTodo();

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
     resetBloqueo(dtAdd, "fratraca",
                  eje_numeE.getValorInt() + "|" + emp_codiE.getValorInt() +
                  "|" + frt_numeE.getValorInt());
     verDatos(dtCons);
   }
   catch (Exception k)
   {
     mensajes.mensajeAviso("Error al Quitar el bloqueo\n" + k.getMessage());
   }
   activaTodo();
 }

 public void activar(boolean b)
 {
   activar(navegador.TODOS, b);
 }
 public void activar(int modo,boolean b)
 {
   BirGrid.setEnabled(b);

   eje_numeE.setEnabled(b);
   frt_numeE.setEnabled(b);
   frt_fechaE.setEnabled(b);
   frt_nufrtrE.setEnabled(b);
   frt_fefrtrE.setEnabled(b);
   tra_codiE.setEnabled(b);
   Baceptar.setEnabled(b);
   Bcancelar.setEnabled(b);
   frt_caltotE.setEnabled(b);
   if (admin)
     opSobAlb.setEnabled(b);
   tpc_codiE.setEnabled(b);
   fpa_codiE.setEnabled(b);
   if (modo==navegador.QUERY)
     return;

   feinalE.setEnabled(b);
   fefialE.setEnabled(b);
   BinsAlb.setEnabled(b);


   div_codiE.setEnabled(b);
   frt_implinE.setEnabled(b);
   frt_kglinE.setEnabled(b);
   frt_dtoppE.setEnabled(b);
   frt_dtocomE.setEnabled(b);
   frt_pivaE.setEnabled(b);
   frt_preequE.setEnabled(b);
   frt_impiva.setEnabled(b);
   frt_impreeE.setEnabled(b);
   frt_imptotE.setEnabled(b);
   frt_basimpE.setEnabled(b);
   frt_prekilE.setEnabled(b);
   jtVto.setEnabled(b);
   if (modo == navegador.ADDNEW)
     return;
   jtFra.setEnabled(b);
   jtAlb.setEnabled(b);

 }

 public void afterConecta() throws SQLException, java.text.ParseException
 {
    s = "SELECT tra_codi,tra_nomb FROM v_transport ORDER BY tra_nomb";
    dtStat.select(s);
    tra_codiE.addDatos(dtStat);
    tra_codiE.setFormato(Types.DECIMAL, "#9");
    tra_codiE.setColumnaAlias("tra_codi");
    div_codiE.setFormato(Types.DECIMAL,"##9",3);
   s = "SELECT div_codi,div_codedi FROM v_divisa order by div_codedi";
   if (!dtStat.select(s))
     throw new SQLException("NO HAY NINGUNA DIVISA DEFINIDA");
   div_codiE.addDatos(dtStat);
   fpa_codiE.setFormato(Types.DECIMAL,"##9",3);
   s = "SELECT fpa_codi,fpa_nomb FROM v_forpago order by fpa_codi";
   dtStat.select(s);
   fpa_codiE.addDatos(dtStat);
   jtVto.iniciar(this,dtStat);
 }

 void verDatos(DatosTabla dt)
 {
   String prvCodi;
   String fecalb;
   try
   {
     if (dt.getNOREG())
       return;

     Pcabe.resetTexto();
     boolean grAct=jtFra.isEnabled();
     jtFra.setEnabled(false);
     jtFra.removeAllDatos();
     emp_codiE.setValorDec(dt.getInt("emp_codi"));
     eje_numeE.setValorDec(dt.getInt("eje_nume"));
     frt_numeE.setValorDec(dt.getInt("frt_nume"));
     s="SELECT * FROM fratraca WHERE emp_codi = " + emp_codiE.getValorInt()+
         " and eje_nume = "+eje_numeE.getValorInt()+
         " and frt_nume = "+frt_numeE.getValorInt();
     if (! dtCon1.select(s))
     {
       msgBox("NO ENCONTRADOS DATOS DE FACTURA");
       jtFra.setEnabled(grAct);
       return;
     }
     frt_fechaE.setText(dtCon1.getFecha("frt_fecha","dd-MM-yyyy"));
     tra_codiE.setText(dtCon1.getString("tra_codi"));
     frt_nufrtrE.setText(dtCon1.getString("frt_nufrtr"));
     frt_fefrtrE.setText(dtCon1.getFecha("frt_fefrtr","dd-MM-yyyy"));
     div_codiE.setText(dtCon1.getString("div_codi"));
     frt_implinE.setValorDec(dtCon1.getDouble("frt_implin"));
     frt_basimpE.setValorDec(dtCon1.getDouble("frt_basimp"));
     frt_dtoppE.setValorDec(dtCon1.getDouble("frt_dtopp"));
     frt_pivaE.setValorDec(dtCon1.getDouble("frt_piva"));
     frt_impiva.setValorDec(dtCon1.getDouble("frt_impiva"));
     frt_dtocomE.setValorDec(dtCon1.getDouble("frt_dtocom"));
     frt_preequE.setValorDec(dtCon1.getDouble("frt_preequ"));
     frt_impreeE.setValorDec(dtCon1.getDouble("frt_impree"));
     frt_traspE.setSelecion(dtCon1.getString("frt_trasp"));
     frt_imptotE.setValorDec(dtCon1.getDouble("frt_imptot"));
     frt_prekilE.setValorDec(dtCon1.getDouble("frt_prekil"));
     frt_caltotE.setSelecion(dtCon1.getString("frt_caltot"));
     tpc_codiE.setValorDec(dtCon1.getInt("tap_codi"));
     fpa_codiE.setValorInt(dtCon1.getInt("fpa_codi",true));
     jtVto.setDatosFra("T",emp_codiE.getValorInt(),eje_numeE.getValorInt(),frt_numeE.getValorInt());
     jtVto.verGrupoFra();
     tap_nombL.setText(getNombTap(tra_codiE.getValorInt(),tpc_codiE.getValorInt()));
     s="SELECT * FROM fratrali "+
         " WHERE emp_codi = " + emp_codiE.getValorInt()+
         " and eje_nume = "+eje_numeE.getValorInt()+
         " and frt_nume = "+frt_numeE.getValorInt()+
         " ORDER BY frt_numlin";
     if (! dtCon1.select(s))
     {
       msgBox("NO ENCONTRADAS LINEAS DE FACTURA");
       return;
     }
     double kilos=0,kgAlb=0,k;
     int nLin=0;
     double frtPrekil=0;
     String frtAlccam;
     do
     {
       kilos += dtCon1.getDouble("frl_canti");
       nLin++;
       ArrayList v = new ArrayList();
       fecalb="";
       prvCodi="";
       k=0;
       frtAlccam=dtCon1.getString("frt_alccam");
       if (frtAlccam.equals("A"))
       {
         s = "SELECT c.acc_fecrec,c.prv_codi,pv.prv_nomb,sum(acl_canti) as acl_canti " +
             " FROM v_albacol as l, v_albacoc as c,v_proveedo as pv " +
             " WHERE C.emp_codi = "+emp_codiE.getValorInt()+
             " and l.emp_codi = "+emp_codiE.getValorInt()+
             " and l.acc_ano = "+dtCon1.getString("dcc_ejerc")+
             " and l.acc_serie = '"+dtCon1.getString("acc_serie")+"'"+
             " and l.acc_nume = "+dtCon1.getInt("frt_numalb")+
             " and c.acc_ano = "+dtCon1.getInt("dcc_ejerc")+
             " and c.acc_serie = '"+dtCon1.getString("acc_serie")+"'"+
             " and c.acc_nume = "+dtCon1.getInt("frt_numalb")+
             " and pv.emp_codi = "+emp_codiE.getValorInt()+
             " and l.acl_porpag =0 "+
             " and c.acc_portes = 'D' "+
             " and c.prv_codi = pv.prv_codi "+
              " group by c.acc_fecrec,c.prv_codi,pv.prv_nomb ";
         if (dtStat.select(s))
         {
           fecalb=dtStat.getFecha("acc_fecrec","dd-MM-yyyy");
           prvCodi=dtStat.getInt("prv_codi")+"-"+dtStat.getString("prv_nomb");
           k=dtStat.getDouble("acl_canti");
           kgAlb+=k;
         }
         else
           frtAlccam = "-";
       }
       v.add(dtCon1.getString("frt_alccam").equals("A")?"Alb.Com":"");
       v.add(dtCon1.getString("dcc_ejerc"));
       v.add(dtCon1.getString("acc_serie"));
       v.add(dtCon1.getString("frt_numalb"));
       v.add(dtCon1.getString("dcl_numli"));
       v.add(fecalb); // Fecha Alb.
       v.add(""+k);
       v.add(dtCon1.getString("frl_canti"));
       v.add(dtCon1.getString("tap_codi"));
       v.add(dtCon1.getString("tap_impor"));
       v.add(prvCodi);
       v.add(dtCon1.getString("frt_numlin"));
       jtFra.addLinea(v);
     }  while (dtCon1.next());
     jtFra.setEnabled(grAct);
     jtFra.requestFocusInicio();
     frt_kglinE.setValorDec(kilos);
     frt_kgalbaE.setValorDec(kgAlb);
     frt_numlinE.setValorDec(nLin);
     frtPrekil_Ant=frt_prekilE.getValorDec();
     jtVto.llenaGrid(dtCon1,'T',emp_codiE.getValorInt(),eje_numeE.getValorInt(),frt_numeE.getValorInt());
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
  verDatos(dtCons);
}
/**
 * Usuada para insertar una lin. en alb. cuando se borra de la fra.
 * @param row int N. Linea de la Fra.,
 * @throws Exception error base datos
 */
void insLinAlb(int row) throws Exception
{
  if (jtFra.getValString(row,0,true).startsWith("Alb"))
  {
    s = "SELECT c.acc_ano,c.acc_serie,c.acc_nume,c.acc_fecrec,c.prv_codi,pv.prv_nomb,sum(acl_canti) as acl_canti " +
        " FROM v_albacol as l, v_albacoc as c,v_proveedo as pv " +
        " WHERE c.emp_codi = " + EU.em_cod +
        " and l.emp_codi = " + EU.em_cod +
        " and pv.emp_codi = " + EU.em_cod +
        " and l.acl_porpag = 0 "+
        " and pv.prv_codi = c.prv_codi "+
        " and c.acc_ano = l.acc_ano " +
        " and c.acc_nume = l.acc_nume " +
        " AND c.acc_ano = " + jtFra.getValorInt(row, 1) +
        " and c.acc_serie = '" + jtFra.getValString(row, 2) + "'" +
        " and c.acc_nume= " + jtFra.getValorInt(row, 3) +
        " group by c.acc_ano,c.acc_serie,c.acc_nume,c.acc_fecrec,c.prv_codi,pv.prv_nomb ";

    if (!dtCon1.select(s))
      return; // No existe Linea para ese Albaran
    insLiAlb0();
  }
}
int cambiaLinAlb()
{
  return cambiaLinAlb(jtFra.getSelectedRow());
}
int cambiaLinAlb(int row)
{
  if (frl_cantiE.getValorDec()==0)
     return -1;
   if (tap_codiE.getValorInt()==0)
   {
     mensajeErr("Tarifa NO puede ser 0s");
     return 8;
   }
   try
   {
//     System.out.println("kilos: "+frl_cantiE.getValorDec());
     impTra = getImporteTra(tra_codiE.getValorInt(),
                                   tap_codiE.getValorInt(),
                                   frl_cantiE.getValorDec());
     if (impTra < 0)
     {
       mensajeErr("Tarifa NO encontrada");
       return 8;
     }
     jtFra.setValor(""+impTra,row,9);
//     tap_imporE.setValorDec(impTra);
   } catch (Exception k)
   {
     Error("Error en CAMBIO DE LINEA",k);
   }

   return -1;

 }
 double getImporteTra(int traCodi, int tapCodi,double kilos) throws Exception
 {
   if (kilos<0)
     return 0;
   String s1 = "SELECT  * FROM taripor WHERE " +
       " tra_codi = " + traCodi +
       " and tap_codi = " + tapCodi +
       " and tap_fecini <= to_date('" + frt_fefrtrE.getText() +"','dd-MM-yyyy')" +
       " and tap_fecfin >= to_date('" + frt_fefrtrE.getText() +"','dd-MM-yyyy')";
   s = s1 + " and tap_kilos >= " + kilos +
       " order by tap_kilos";
   if (!dtStat.select(s))
   {
     s = s1 + "and tap_kilos = 0";
     if (!dtStat.select(s))
       return -1;
   }
   if (dtStat.getString("tap_fijkil").equals("K"))
     return kilos * dtStat.getDouble("tap_impor");
   else
     return dtStat.getDouble("tap_impor");

 }
  void insLinFra(int row) throws Exception
  {
    if (jtAlb.isVacio())
      return;
    int nCol;
    nCol=cambiaLinAlb();
    if (nCol>=0)
    {
      jtFra.requestFocus(nCol,jtFra.getSelectedColumn());
      return;
    }
    ArrayList v= getLinAlb(row);
    jtFra.setEnabled(false);
    jtFra.addLinea(v);
    jtFra.setEnabled(true);
//    jtFra.requestFocusFinal();
    jtAlb.removeLinea(row);
    actAcumFra();
    if (impTra < 0)
    {
      jtFra.requestFocusFinal();
      mensajeErr("Tarifa NO encontrada");
    }

  }

  ArrayList getLinAlb(int row)  throws Exception
  {
    ArrayList v = new ArrayList();
    v.add("Alb.Com");
    v.add(jtAlb.getValString(row, 0)); // Ejerc
    v.add(jtAlb.getValString(row, 1)); // Serie
    v.add(jtAlb.getValString(row, 2)); // Alb.
    v.add("0"); // Alb.
    v.add(jtAlb.getValString(row, 3)); // Fec.Alb
    v.add(jtAlb.getValString(row, 4)); // Kg. Alb.
    v.add(jtAlb.getValString(row, 4)); // Kg. Fra
    v.add(tap_codiE.getText()); // Tarifa
    impTra = getImporteTra(tra_codiE.getValorInt(),
                           tap_codiE.getValorInt(),
                           jtAlb.getValorDec(row, 4));
    v.add(""+impTra); // Importe Portes
    v.add(jtAlb.getValString(row,5)+"-"+jtAlb.getValString(row,6));
    v.add("0"); // N. Linea
    return v;
  }

  /**
   * Actualiza el acumulado de la Fra. (importes del pie)
   *
   */
  void actAcumFra()
  {
    if (frt_fefrtrE.isNull() || frt_fefrtrE.getError())
      return;
    String s1 = "SELECT  * FROM taripor WHERE " +
         " tra_codi = "+tra_codiE.getValorInt()+
         " and tap_codi = "+tpc_codiE.getValorInt()+
         " and tap_fecini <= to_date('" + frt_fefrtrE.getText() + "','dd-MM-yyyy')" +
         " and tap_fecfin >= to_date('" + frt_fefrtrE.getText() +"','dd-MM-yyyy')";

    int n=0;
    int nRow=jtFra.getRowCount();
    double kilos = 0,kilAlb=0;
    int nLin = 0;

    double impLin,impLiT=0;
    try
    {
      for (n = 0; n < nRow; n++)
      {
        if (jtFra.getValorDec(n, 7) == 0)
          continue;
        if (jtFra.getValString(n,0).startsWith("A"))
          kilAlb += jtFra.getValorDec(n, 6);
        kilos += jtFra.getValorDec(n, 7);
        nLin++;
        impLin=jtFra.getValorDec(n,9);
        impLiT+=impLin;
      }
      frt_kglinE.setValorDec(kilos);
      frt_kgalbaE.setValorDec(kilAlb);

      frt_numlinE.setValorDec(nLin);
      if (frt_caltotE.isSelected())
      {
        s = s1+
            " and tap_kilos >= " + kilos +
            " order by tap_kilos";
        if (!dtStat.select(s))
        {
          s = s1+" and tap_kilos = 0";
          if (!dtStat.select(s))
            return;
        }
        if (dtStat.getString("tap_fijkil").equals("K"))
          impLiT = dtStat.getDouble("tap_impor") * kilos;
        else
          impLiT = dtStat.getDouble("tap_impor");
      }
      double impLi0 = impLiT;
      frt_implinE.setValorDec(impLiT);

      if (frt_dtoppE.getValorDec() != 0)
        impLiT -= impLi0 * frt_dtoppE.getValorDec() / 100;
      if (frt_dtocomE.getValorDec() != 0)
        impLiT -= impLi0 * frt_dtocomE.getValorDec() / 100;
      impLiT = Formatear.Redondea(impLiT, numDec);
      frt_basimpE.setValorDec(impLiT);
      frt_impiva.setValorDec(Formatear.Redondea(impLiT * frt_pivaE.getValorDec() /
                                                100, numDec));
      frt_impreeE.setValorDec(Formatear.Redondea(impLiT *
                                                 frt_preequE.getValorDec() /
                                                 100, numDec));
      double impAnt=frt_imptotE.getValorDec();
      double imptot=frt_imptotE.getValorDec();
      frt_imptotE.setValorDec(impLiT + frt_impreeE.getValorDec() +
                              frt_impiva.getValorDec());
      if (frt_imptotE.getValorInt()!=imptot && (nav.pulsado==navegador.EDIT || nav.pulsado==navegador.ADDNEW))
        jtVto.ponValoresDef(frt_imptotE.getValorDec(),fpa_codiE.getValorInt(),frt_fefrtrE.getText());
      imptot=frt_imptotE.getValorDec();
      frt_prekilE.setValorDec(kilAlb==0?0:frt_basimpE.getValorDec()/kilAlb);
      if (jtVto.getLinActivas()==0)
      {
        jtVto.jtVto.setValor(frt_imptotE.getText(), 0, 2);
        jtVto.lbv_impvtoE.setValorDec(frt_imptotE.getValorDec());
      }
      if (jtVto.getLinActivas() == 1 && jtVto.jtVto.getValorDec(0, 2) == impAnt && impAnt != imptot)
      {
        jtVto.jtVto.setValor(frt_imptotE.getText(), 0, 2);
        jtVto.lbv_impvtoE.setValorDec(frt_imptotE.getValorDec());
      }

    }  catch (Exception k)
    {
      Error("Error al buscar datos de Compras", k);
    }

  }

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
      k.printStackTrace();
    }

  }

  private void confGrid() throws Exception
  {
    Vector v = new Vector();
    v.addElement("A/C/N"); // 0
    v.addElement("Ejerc"); // 1
    v.addElement("Serie"); // 2
    v.addElement("Albaran"); // 3
    v.addElement("NL.Cam"); // 4
    v.addElement("Fec.Alb."); // 5
    v.addElement("Kilos Alb"); // 6
    v.addElement("Kilos Fra"); // 7
    v.addElement("Tarifa"); // 8 (Tarifa Portes)
    v.addElement("Imp.Trans"); // 9
    v.addElement("Proveedor"); // 10
    v.addElement("NL"); // 11
    jtFra.setCabecera(v);
    jtFra.setAlinearColumna(new int[]
                            {1, 2, 1, 2, 2, 1, 2, 2, 2, 2, 0,2});
    jtFra.setAnchoColumna(new int[]
                          {50, 40, 40, 50, 40, 90, 80, 80, 40, 80,150,30});

    jtFra.setFormatoColumna(6, "---,--9.99");
    jtFra.setFormatoColumna(7, "--,--9.99");
    jtFra.setFormatoColumna(9, "--,--9.99");

    jtFra.setAjustarGrid(true);
    jtFra.ajustar(false);
    jtAlb.setBotonInsert();
    jtAlb.Binser.setToolTipText("Insertar Linea Albaran en Factura (F7)");
    ArrayList vc = new ArrayList();

    frt_alccamE.setToolTipText("Alb.Compra / Desc.Camion");
    frt_alccamE.setHorizontalAlignment(JTextField.CENTER);

    frt_numliE.setEnabled(false);
    tap_codiE.setValorDec(1);
    frt_alccamE.setEnabled(false);
    dcc_ejerE.setEnabled(false);
    acc_serieE.setEnabled(false);
    frt_numalbE.setEnabled(false);
    dcl_numliE.setEnabled(false);
    acc_fecalbE.setEnabled(false);
    frl_canalbE.setEnabled(false);
    tap_imporE.setEnabled(false);
    prv_codiE.setEnabled(false);
    vc.add(frt_alccamE); //0
    vc.add(dcc_ejerE); //1
    vc.add(acc_serieE); // 2
    vc.add(frt_numalbE); // 3
    vc.add(dcl_numliE); // 4
    vc.add(acc_fecalbE); // 5
    vc.add(frl_canalbE); // 7
    vc.add(frl_cantiE); // 8
    vc.add(tap_codiE); // 9
    vc.add(tap_imporE); // 9
    vc.add(prv_codiE); // 10
    vc.add(frt_numliE); // 11
    jtFra.setCampos(vc);

    Vector v1 = new Vector();
    v1.addElement("Eje."); // 0
    v1.addElement("Serie"); // 1
    v1.addElement("Alb."); // 2
    v1.addElement("Fec.Alb"); // 3
    v1.addElement("Kilos"); // 4
    v1.addElement("Prv."); // 5
    v1.addElement("Nombre Proveedor"); // 6
    jtAlb.setCabecera(v1);

    jtAlb.setAlinearColumna(new int[]{2, 1, 2, 1, 2,2,0});
    jtAlb.setAnchoColumna(new int[] {50, 60, 80, 90, 90,60,150});
    jtAlb.setAjustarGrid(true);
    jtAlb.setFormatoColumna(4, "---,--9.99");

  }

  void BinsAlb_actionPerformed()
  {
    try
    {
      if (! jtAlb.isEnabled())
      {
        if (! irGridFra())
          return;
      }
      java.util.Date fecAlb;
      jtFra.setEnabled(false);
      int nRow = jtAlb.getRowCount();

      for (int n = nRow - 1; n >= 0 && !jtAlb.isVacio(); )
      {
        try
        {
          fecAlb = Formatear.getDate(jtAlb.getValString(n, 3), "dd-MM-yyyy");
        }
        catch (Exception k)
        {
          mensajes.mensajeAviso("Fecha Albaran NO VALIDA");
          jtFra.setEnabled(true);
          return;
        }
        if (Formatear.comparaFechas(feinalE.getDate(), fecAlb) >= 0 &&
            Formatear.comparaFechas(fecAlb, fefialE.getDate()) >= 0)
        {
          ArrayList v = getLinAlb(n);
          jtFra.addLinea(v);
          jtAlb.removeLinea(n);
        }
        n--;
      }
      jtFra.setEnabled(true);
      actAcumFra();
    }
    catch (Exception k)
    {
      Error("Error al Procesar Alb. a Insertar", k);
    }
  }
}
