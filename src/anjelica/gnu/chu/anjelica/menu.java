package gnu.chu.anjelica;

import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.jgoodies.looks.plastic.theme.ExperienceBlue;
import gnu.chu.anjelica.ventas.represen.FichaClientes;
import gnu.chu.Menu.LoginDB;
import java.io.FileNotFoundException;
import java.util.*;
import java.net.*; 
import java.awt.*; 
import java.awt.event.*;  
import javax.swing.*;
import javax.swing.event.*;
import gnu.chu.controles.*; 
import gnu.chu.utilidades.*;

import gnu.chu.sql.*;  
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * 
 * <p>Titulo: menu </p>
 * <p>Descripcionn: Programa utilizado para lanzar otros modulos en pruebas </p>
 * <p>Copyright: Copyright (c) 2004-2016</p>
 * <p>Empresa: </p>
 *   Este programa es software libre. Puede redistribuirlo y/o modficarlo bajo
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
 * @author Chuchi P.
 * @version 1.0
 */
public class menu extends JFrame
{
    int nVentana=0;
  DatosTabla dtAdd;
  DatosTabla dtBloq;
//  static Logger logger = Logger.getLogger(menu.class);
  JDesktopPane Pprinc = new JDesktopPane();
  CPanel Pmenu = new CPanel();
  CButton Bvarios = new CButton();
  CButton BPedVen = new CButton();
  CButton BvenZona = new CButton();
  EntornoUsuario EU;
  CButton Balbcom = new CButton();
  CButton despiece = new CButton();
  CButton Bpad = new CButton();
  CButton Bmargen = new CButton();
  CButton Balmac = new CButton();
  CButton Briesgo = new CButton();
  CButton Bfactur = new CButton();
  JMenuItem calculadora = new JMenuItem("Calculadora");
  JPopupMenu popupDesp = new JPopupMenu("PopupMenu");
  JMenuItem Mpdprvades= new JMenuItem("Precios Desp.");
  JPopupMenu popupMarg = new JPopupMenu("Margenes");
  JMenuItem mnuZoMarg= new JMenuItem("Por Zonas");
  JMenuItem mnuHistVen= new JMenuItem("Hist.Ventas");
  JMenuItem mnuCalcTarifa= new JMenuItem("Calc.Tarifa");
  JMenuItem mnuInvMarg= new JMenuItem("Por Inv");
  JMenuItem codegen= new JMenuItem("Cons.Desp.");
  JMenuItem recosdes= new JMenuItem("Reg.Costos Desp.");
  JMenuItem mnuCompras=new JMenuItem("Compras");
//  JMenuItem mnuAlbaCom=new JMenuItem("Albaranes Compras");
  JMenuItem mnuReclPrv=new JMenuItem("Recl.Prv.");
  JMenuItem pdtipdes= new JMenuItem("Tipos Desp");
  JPopupMenu JPopupAlmacen = new JPopupMenu();
  JMenuItem jMenuAlmacen = new JMenuItem();
  JPopupMenu jPopupCompras = new JPopupMenu();
  JMenuItem albComCarne = new JMenuItem();
  JMenuItem clprpecoI = new JMenuItem();
  JMenuItem albComPlanta = new JMenuItem();
  JMenuItem comprasItem2 = new JMenuItem();
  JMenuItem conalbco = new JMenuItem();
   JMenuItem albaPrv = new JMenuItem("Alb.Prv");

  JMenu ItemInventario = new JMenu();
  JMenuItem clvenrep = new JMenuItem();
  JMenuItem CLRankClie = new JMenuItem();
  JMenuItem mantTariCliente = new JMenuItem();
  JMenuItem cargaInv = new JMenuItem();
  JMenuItem mantInv = new JMenuItem();
  JMenuItem valInv = new JMenuItem();
   JMenuItem trasInv = new JMenuItem();
  JMenuItem lisdifIn = new JMenuItem();
  JPopupMenu JPopupPad = new JPopupMenu();
  JMenuItem PadFPA = new JMenuItem();
  JMenuItem Padclientes = new JMenuItem();
  JMenuItem PadPaises = new JMenuItem("Paises");
  JMenuItem PadIdiomas = new JMenuItem("Idioma");
  JMenuItem MantVehi = new JMenuItem();
  JMenuItem pdconfig = new JMenuItem();
  JMenuItem pddiscrim = new JMenuItem();
  JMenuItem pdreprese = new JMenuItem();
  JMenuItem ALclstkdes = new JMenuItem();
  JMenuItem ALclresstock = new JMenuItem();
  JMenuItem ALlisaldos = new JMenuItem();
  JMenuItem ALCheckMvt = new JMenuItem();
  JMenuItem ALclUbiArt = new JMenuItem();

  JMenuItem ALConmvpr = new JMenuItem();
  JMenuItem ALMantPartes = new JMenuItem();
  JMenuItem pdproveed = new JMenuItem();
  JMenuItem pdtranspo = new JMenuItem();
  JMenuItem pdarticu = new JMenuItem();
  JMenuItem mantDisProVenta = new JMenuItem();
JMenuItem mantArticVenta = new JMenuItem();
  JMenuItem lisclien = new JMenuItem();
  JMenuItem tratclien = new JMenuItem();
  JPopupMenu jPopupMenu2 = new JPopupMenu();
  JMenuItem despTactil = new JMenuItem();
  JMenuItem mantDespTactil = new JMenuItem();
  JMenuItem despMant = new JMenuItem();
//  JMenuItem pddesp = new JMenuItem();

  JMenuItem pdgrupro = new JMenuItem();
  JMenuItem pdfamipro = new JMenuItem();
  JMenuItem pdtiposiva = new JMenuItem();
  JMenuItem pddivisa = new JMenuItem();
  JMenuItem pdregalm = new JMenuItem();
  JMenuItem costkpar = new JMenuItem();
  JMenuItem trasAlma = new JMenuItem();
  JMenuItem pdbanteso = new JMenuItem();
  JMenuItem coarbtraz = new JMenuItem();
  JPopupMenu JpopupVentas = new JPopupMenu();
  JPopupMenu JpopupPedVentas=new JPopupMenu();
  JMenuItem pdpedven = new JMenuItem("PAD Pedidos Ventas");
 
  JMenuItem clpevepr = new JMenuItem("Cons Ped.Ventas x Producto");
  JMenuItem clAlbSinCosto = new JMenuItem();
  JMenuItem pdalbara = new JMenuItem();
  JMenuItem cldepcli = new JMenuItem();
  JMenuItem lialbara = new JMenuItem();
  JMenuItem clrelpedven = new JMenuItem();

   JMenuItem manpralbar = new JMenuItem();
  JMenuItem clvenArt = new JMenuItem();
  JMenuItem coVenPro = new JMenuItem();
  JMenuItem conVenZonas = new JMenuItem();
  JPopupMenu popupFact = new JPopupMenu();
  JMenuItem lirelfact = new JMenuItem();
  JMenuItem coinvent = new JMenuItem();
  JMenuItem creaStk = new JMenuItem();
  JMenuItem pdfactu = new JMenuItem();
  JMenuItem genfactu = new JMenuItem();
  JPopupMenu jPopupRiesgo = new JPopupMenu();
   JPopupMenu jPopupVarios = new JPopupMenu();
  JMenuItem clfactcob = new JMenuItem();
  JMenuItem clriescli = new JMenuItem();
  JMenuItem MenuFicha= new JMenuItem();
  JMenuItem MenuLogs= new JMenuItem();
  JMenuItem clcobreal = new JMenuItem();
  JMenuItem cacobrea = new JMenuItem();
    JMenuItem pdcobruta = new JMenuItem();
  JMenuItem pdcobros = new JMenuItem();
  JMenuItem pdmotregu = new JMenuItem();
   JMenuItem repeti = new JMenuItem();
  JMenuItem clstkfeca = new JMenuItem();
  JMenuItem clinvcong = new JMenuItem();
   JMenuItem clresmvtos = new JMenuItem();
   JMenuItem cvregalm = new JMenuItem();
   JMenuItem mantAlbRuta = new JMenuItem();
   JMenuItem clprodrec = new JMenuItem();
  JMenuItem pdnumerac = new JMenuItem();
  JMenuItem clvertprv = new JMenuItem();
  JMenuItem clintalco = new JMenuItem();
  JMenuItem pdpagreal = new JMenuItem();
  JMenuItem creareme = new JMenuItem();
  JMenuItem pdusbempr = new JMenuItem();
  JMenuItem pdempresa = new JMenuItem();

  JMenuItem lkpdusua = new JMenuItem();
  JMenuItem cocaalco = new JMenuItem();
  JMenuItem clrealco = new JMenuItem();
  JMenuItem lirefacco = new JMenuItem();
  JMenuItem tipotari = new JMenuItem();
  JMenuItem tarifa = new JMenuItem();
  JMenuItem mantCalendar = new JMenuItem();
  JMenuItem anufactu = new JMenuItem();
  JMenuItem traspcont = new JMenuItem();
  JMenuItem lisfactu = new JMenuItem();
  JMenuItem pdpedco = new JMenuItem();
  JMenuItem copedco = new JMenuItem();
  JMenuItem pdfatra = new JMenuItem();
  JMenuItem mantartra = new JMenuItem();
  JMenuItem pdjercici = new JMenuItem();
  JMenuItem pdmatadero = new JMenuItem();
  JMenuItem pdgruart = new JMenuItem();
  JMenuItem valdespi2 = new JMenuItem();
  JMenuItem Valdespi = new JMenuItem();
  JMenuItem cldessv = new JMenuItem();
  JMenuItem prueba3 = new JMenuItem();
  JMenu ItemRepresen = new JMenu();
  public menu()
  {
    try {
      jbInit();
      String hostname = InetAddress.getLocalHost().getHostName();
      System.out.println("Hostname: "+hostname);

      System.out.println("Version java: "+System.getProperties().getProperty("java.runtime.version"));

//      PlasticLookAndFeel.setMyCurrentTheme(new  com.jgoodies.looks.plastic.theme.ExperienceBlue());
//      PlasticLookAndFeel.setHighContrastFocusColorsEnabled(true);
//
      Plastic3DLookAndFeel.setPlasticTheme(new ExperienceBlue());
      UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
      Color cDef = new Color(120, 120, 120);
      UIManager.put("ComboBox.disabledForeground", cDef);
      UIManager.put("Button.disabledText", cDef);
      UIManager.put("CheckBox.disabledText", cDef);
      UIManager.put("ToggleButton.disabledText", cDef);


      /*
      UIDefaults defaults = UIManager.getDefaults();
      System.out.println("Count Item = " + defaults.size());
              String[ ] colName = {"Key", "Value"};
              String[ ][ ] rowData = new String[ defaults.size() ][ 2 ];
              int i = 0;
              for(Enumeration e = defaults.keys(); e.hasMoreElements(); i++){
                  Object key = e.nextElement();
                  rowData[ i ] [ 0 ] = key.toString();
                  rowData[ i ] [ 1 ] = ""+defaults.get(key);
                  System.out.println(rowData[i][0]+" ,, "+rowData[i][1]);
              }
*/
//      prueba1();
    }
    catch(Exception e) {
        e.printStackTrace();
      
    }

  }

  public static void main(String[] args)
  {
    try
    {
      menu frame = new menu();
      frame.iniciar();
    }
    catch (Throwable ex)
    {
      ex.printStackTrace();

    }

  }
  private void jbInit() throws Exception
  {
    this.setTitle("Lanzador");
       // ntp();
    this.setSize(new Dimension(780, 610));

    Pmenu.setBorder(BorderFactory.createEtchedBorder());
    Pmenu.setMaximumSize(new Dimension(32767, 32767));
    Pmenu.setMinimumSize(new Dimension(10, 30));
    Pmenu.setPreferredSize(new Dimension(50, 30));
    Pmenu.setLayout(null);
    Bvarios.setBounds(new Rectangle(5, 2, 90, 20));
    Bvarios.setMargin(new Insets(0, 0, 0, 0));
    Bvarios.setText("Varios");

    BPedVen.setBounds(new Rectangle(99, 2, 68, 18));
    BPedVen.setMaximumSize(new Dimension(117, 25));
    BPedVen.setToolTipText("");
    BPedVen.setMargin(new Insets(0, 0, 0, 0));
    BPedVen.setText("Ped.Ventas");

    BvenZona.setBounds(new Rectangle(170, 2, 59, 19));
    BvenZona.setMargin(new Insets(0, 0, 0, 0));
    BvenZona.setText("Ventas");

    Balbcom.setBounds(new Rectangle(231, 2, 99, 18));
    Balbcom.setMargin(new Insets(0, 0, 0, 0));


    Balbcom.setText("Compras");
    despiece.setBounds(new Rectangle(330, 2, 75, 18));
    despiece.setMargin(new Insets(0, 0, 0, 0));
    despiece.setText("Despiece");
    Bpad.setBounds(new Rectangle(407, 2, 53, 18));
    Bpad.setMargin(new Insets(0, 0, 0, 0));
    Bpad.setText("PAD");
    Bmargen.setBounds(new Rectangle(462, 2, 67, 18));
    Bmargen.setMargin(new Insets(0, 0, 0, 0));
    Bmargen.setText("Margenes");
    Balmac.setBounds(new Rectangle(533, 2, 60, 18));
    Balmac.setMargin(new Insets(0, 0, 0, 0));
    Balmac.setSelectedIcon(null);
    Balmac.setText("Almacen");
    Briesgo.setBounds(new Rectangle(596, 4, 62, 16));
    Briesgo.setToolTipText("");
    Briesgo.setMargin(new Insets(0, 0, 0, 0));
    Briesgo.setText("Riesgos");
    Bfactur.setBounds(new Rectangle(662, 3, 62, 16));
    Bfactur.setMargin(new Insets(0, 0, 0, 0));
    Bfactur.setText("Factur.");
   jMenuAlmacen.setText("Mant.Almac");
    jMenuAlmacen.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Balmacen_ActionPerformed(e);
      }
    });
    albComCarne.setText("Albar. Carne");
    albComCarne.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        comprasItem1_actionPerformed(e);
      }
    });
    clprpecoI.setText("Cons.Ped.Prod");
    clprpecoI.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        clprpecoI_actionPerformed(e);
      }
    });
     mnuReclPrv.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        mnuReclPrv_actionPerformed(e);
      }
    });
    albComPlanta.setText("Albar.Planta");
    albComPlanta.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        albComPlanta_actionPerformed(e);
      }
    });
    comprasItem2.setText("Facturas");
    comprasItem2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        comprasItem2_actionPerformed(e);
      }
    });
    conalbco.setText("Consultas");
    conalbco.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        conalbco_actionPerformed(e);
      }
    });
    albaPrv.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        albaPrv_actionPerformed(e);
      }
    });
    ItemInventario.setText("Inventario");
    cargaInv.setText("Carga Inventario");
    cargaInv.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cargaInv_actionPerformed(e);
      }
    });
     ItemRepresen.setText("Represen");
     ItemRepresen.add(clvenrep);
 clvenrep.setText("Ventas Repr.");
    clvenrep.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        clvenrep_actionPerformed(e);
      }
    });
     mantTariCliente.setText("Mant.Tarifa Clientes");
    mantTariCliente.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        mantTariCliente_actionPerformed(e);
      }
    });
    CLRankClie.setText("Ranking Clientes");
    CLRankClie.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        CLRankClie_actionPerformed(e);
      }
    });
    mantInv.setText("Mant. Inventario");
    mantInv.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        mantInv_actionPerformed(e);
      }
    });
    valInv.setText("Valora Inv");
    valInv.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        valInv_actionPerformed(e);
      }
    });
     trasInv.setText("Trasp. Inv");
    trasInv.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        trasInv_actionPerformed(e);
      }
    });
    lisdifIn.setText("List.Diferencias");
    lisdifIn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        lisdifIn_actionPerformed(e);
      }
    });
    PadFPA.setText("Formas Pago");
    PadFPA.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        PadFPA_actionPerformed(e);
      }
    });
    Padclientes.setText("Clientes");
    Padclientes.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Padclientes_actionPerformed(e);
      }
    });
    MantVehi.setText("Vehiculos");
    MantVehi.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        MantVehi_actionPerformed(e);
      }
    });

    PadPaises.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        PadPaises_actionPerformed(e);
      }
    });
    PadIdiomas.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        PadIdiomas_actionPerformed(e);
      }
    });        
    pdconfig.setText("Configuracion");
    pdconfig.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        pdconfig_actionPerformed(e);
      }
    });
    pddiscrim.setText("Discriminadores");
    pddiscrim.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        pddiscrim_actionPerformed(e);
      }
    });
    pdreprese.setText("Representantes");
    pdreprese.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        pdreprese_actionPerformed(e);
      }
    });
    ALclstkdes.setText("C/L Stock Desg");
    ALclstkdes.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ALclstkdes_actionPerformed(e);
      }
    });
    ALclresstock.setText("C/Res.Stock");
    ALclresstock.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ALclresstock_actionPerformed(e);
      }
    });
    ALclUbiArt.setText("CL Ubic.Art");
    ALlisaldos.setText("Listado Saldos");
    ALCheckMvt.setText("Compr.Mvtos/Doc");
    ALConmvpr.setText("Cons.Mvtos");
    ALConmvpr.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ALConmvpr_actionPerformed(e);
      }
    });
    ALMantPartes.setText("Mant.Partes");
    ALMantPartes.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ALMantPartes_actionPerformed(e);
      }
    });

    clprodrec.setText("Cons.Prod.Rec");
    clprodrec.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        clprodreci_actionPerformed(e);
      }
    });
    cvregalm.setText("Cons./Act. Reg. Almacen");
    cvregalm.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cvregalm_actionPerformed(e);
      }
    });
    mantAlbRuta.setText("Mant.Alb.Ruta");
    mantAlbRuta.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        mantAlbRuta_actionPerformed(e);
      }
    });

    pdproveed.setText("Proveedores");
    pdproveed.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        pdproveed_actionPerformed(e);
      }
    });
     pdtranspo.setText("Transportista");
    pdtranspo.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        pdtranspo_actionPerformed(e);
      }
    });
    pdarticu.setText("Articulos");
    pdarticu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        pdarticu_actionPerformed(e);
      }
    });
    mantDisProVenta.setText("Discr.Prod");
    mantDisProVenta.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        mantDisProVenta_actionPerformed(e);
      }
    });
    mantArticVenta.setText("Mant. Art.Venta");
    mantArticVenta.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        mantArticVenta_actionPerformed(e);
      }
    });
    lisclien.setText("Listado clientes");
    lisclien.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        lisclien_actionPerformed(e);
      }
    });
    tratclien.setText("Tratamiento clientes");
    tratclien.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        tratclien_actionPerformed(e);
      }
    });
    mantDespTactil.setText("Mant. Tactil");
    mantDespTactil.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        mantDespTactil_actionPerformed(e);
      }
    });
    despMant.setText("Mant. Desp.");
    despMant.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        despMant_actionPerformed(e);
      }
    });
     
    pdgrupro.setText("Grupo Prod");
    pdgrupro.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        pdgrupro_actionPerformed(e);
      }
    });
    pdfamipro.setText("Familia Prod");
    pdfamipro.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        pdfamipro_actionPerformed(e);
      }
    });
    pdtiposiva.setText("Tipos iva");
    pdtiposiva.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        pdtiposiva_actionPerformed(e);
      }
    });
    pddivisa.setText("Divisas");
    pddivisa.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        pddivisa_actionPerformed(e);
      }
    });
    pdregalm.setActionCommand("Mant. Regularizac.");
    pdregalm.setText("Mant. Regularizac.");
    pdregalm.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        pdregalm_actionPerformed(e);
      }
    });
    costkpar.setText("Cons.Stock Part.");
    costkpar.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        costkpar_actionPerformed(e);
      }
    });
     trasAlma.setText("Tras.entre alm.");
    trasAlma.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        trasAlma_actionPerformed(e);
      }
    });
    pdbanteso.setText("Bancos Tesor");
    pdbanteso.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        pdbanteso_actionPerformed(e);
      }
    });
    coarbtraz.setText("Cons. Arbol Traz");
    coarbtraz.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        coarbtraz_actionPerformed(e);
      }
    });
    clAlbSinCosto.setText("C/List.Alb s/Fra");
    clAlbSinCosto.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        clAlbSinCosto_actionPerformed(e);
      }
    });
      manpralbar.setText("Mant.Precios Alb.");
    manpralbar.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        manpralb_actionPerformed(e);
      }
    });

    pdalbara.setText("Mant. Albaranes");
    pdalbara.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        pdalbara_actionPerformed(e);
      }
    });
    cldepcli.setText("Dep.Clientes");
    cldepcli.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cldepcli_actionPerformed();
      }
    });
     lialbara.setText("List. Albaranes");
    lialbara.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        lialbara_actionPerformed(e);
      }
    });
    clrelpedven.setText("CL Ped.Ventas");
    clrelpedven.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        clrelalbve_actionPerformed(e);
      }
    });

    clvenArt.setText("Ventas p/Art");
    clvenArt.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        clvenArt_actionPerformed(e);
      }
    });
     coVenPro.setText("Ventas Articulos");
    coVenPro.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        coVenPro_actionPerformed(e);
      }
    });
    conVenZonas.setText("Ventas por Zonas");
    conVenZonas.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        conVenZonas_actionPerformed(e);
      }
    });
    lirelfact.setText("List.Rel.Fras");
    lirelfact.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        lirelfact_actionPerformed(e);
      }
    });
    coinvent.setText("Cons. Inventario");
    coinvent.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        coinvent_actionPerformed(e);
      }
    });
    creaStk.setText("Crea Stock-Part");
    creaStk.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        creaStk_actionPerformed(e);
      }
    });
    pdfactu.setText("Mant. Fras");
    pdfactu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        pdfactu_actionPerformed(e);
      }
    });
    genfactu.setText("Gen. Facturas");
    genfactu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        genfactu_actionPerformed(e);
      }
    });
     cacobrea.setText("Carga Cobros Real. en Ruta");
    cacobrea.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cacobrea_actionPerformed(e);
      }
    });
     pdcobruta.setText("Mant. Cobros Ruta");
     pdcobruta.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        pdcobruta_actionPerformed(e);
      }
    });
    clriescli.setText("Cons. Cobros Pend");
    clriescli.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        clriescli_actionPerformed(e);
      }
    });
    MenuFicha.setText("Cons.Clientes");
    MenuFicha.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        lanzaFichaClientes();
      }
    });
     MenuLogs.setText("Ver Logs");
    MenuLogs.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        lanzaVerLogs();
      }
    });
     clcobreal.setText("Cons. Cobros Realizados");
    clcobreal.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        clcobreal_actionPerformed(e);
      }
    });
      clfactcob.setText("Cons./List. Fact. Cobradas");
    clfactcob.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        clfactcob_actionPerformed(e);
      }
    });
    pdcobros.setText("Man. Cobros");
    pdcobros.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        pdcobros_actionPerformed(e);
      }
    });
    pdmotregu.setText("Mant. Tipo Reg");
    pdmotregu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        pdmotregu_actionPerformed(e);
      }
    });
     repeti.setText("Repetir Etiqueta");
    repeti.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        repeti_actionPerformed(e);
      }
    });
      clresmvtos.setText("Cons/List.Res.Mvtos");
    clresmvtos.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        clresmvtos_actionPerformed(e);
      }
    });
     clstkfeca.setText("Cons/List. Stk. Fec.Cad");
    clstkfeca.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        clstkfeca_actionPerformed(e);
      }
    });
     clinvcong.setText("Cons/List. Stock Cong");
    clinvcong.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        clinvcong_actionPerformed(e);
      }
    });
    pdnumerac.setText("Numeraciones");
    pdnumerac.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        pdnumerac_actionPerformed(e);
      }
    });
     clintalco.setText("Cons. Int");
    clintalco.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        clintalco_actionPerformed(e);
      }
    });
    clvertprv.setText("C/L.Vert");
    clvertprv.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        clvertprv_actionPerformed(e);
      }
    });
    pdpagreal.setText("Pag real.");
    pdpagreal.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        pdpagreal_actionPerformed(e);
      }
    });
    creareme.setText("Creac. Remesas");
    creareme.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        creareme_actionPerformed(e);
      }
    });

    pdusbempr.setText("SubEmpresa");
    pdusbempr.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        pdusbempr_actionPerformed(e);
      }
    });
    pdempresa.setText("Empresa");
    pdempresa.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        pdempresa_actionPerformed(e);
      }
    });

    lkpdusua.setText("Usuarios");
    lkpdusua.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        pdusua_actionPerformed(e);
      }
    });
    cocaalco.setText("Cons.Cab.");
    cocaalco.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cocaalco_actionPerformed(e);
      }
    });
    clrealco.setText("Rel.Alb.");
    clrealco.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
       clrelalco_actionPerformed(e);
      }
    });
    lirefacco.setText("Rel.Fras");
    lirefacco.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        lirefacco_actionPerformed(e);
      }
    });
    tarifa.setText("tarifas");
     tarifa.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        tarifa_actionPerformed(e);
      }
    });
    mantCalendar.setText("Calendarios");
    mantCalendar.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        mantCalendar_actionPerformed(e);
      }
    });
    tipotari.setText("Tipo Tarifa");
    tipotari.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        tipotari_actionPerformed(e);
      }
    });
    anufactu.setText("Anul. Fras");
    anufactu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        anufactu_actionPerformed(e);
      }
    });
    traspcont.setText("Trasp. Contab");
    lisfactu.setText("List.Fact");
    pdpedco.setText("Ped.Compras");
    pdpedco.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        pdpedco_actionPerformed(e);
      }
    });
    copedco.setText("Cons. Ped. Comp.");
    copedco.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        copedco_actionPerformed(e);
      }
    });
    pdfatra.setText("Fras.Transp.");
    pdfatra.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        pdfatra_actionPerformed(e);
      }
    });
    mantartra.setText("Tarifas Transp.");
    mantartra.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        mantartra_actionPerformed(e);
      }
    });
    pdjercici.setText("Ejercicios");
    pdjercici.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        pdjercici_actionPerformed(e);
      }
    });
    pdmatadero.setText("Mataderos");
    pdmatadero.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        pdmatadero_actionPerformed(e);
      }
    });
    pdgruart.setText("Grupos Art.");
    pdgruart.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        pdgruart_actionPerformed(e);
      }
    });
    Mpdprvades.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Mpdprvades_actionPerformed();
      }
    });
        
     Valdespi.setText("Val.Despieces");
    Valdespi.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Valdespi_actionPerformed(e);
      }
    });
     cldessv.setText("Desp.SinValor");
    cldessv.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cldessv_actionPerformed(e);
      }
    });
    codegen.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        codegen_actionPerformed(e);
      }
    });
     recosdes.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        recosdes_actionPerformed(e);
      }
    });
      pdtipdes.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        pdtipdes_actionPerformed(e);
      }
    });
    prueba3.setText("Prueba3");
    prueba3.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        prueba3_actionPerformed(e);
      }
    });
    
    this.getContentPane().add(Pprinc,  BorderLayout.CENTER);
    this.getContentPane().add(Pmenu, BorderLayout.NORTH);
    Pmenu.add(Bvarios, null);
    Pmenu.add(BPedVen, null);
    Pmenu.add(BvenZona, null);
    Pmenu.add(despiece, null);
    Pmenu.add(Bpad, null);
    Pmenu.add(Bmargen, null);
    Pmenu.add(Balmac, null);
    Pmenu.add(Briesgo, null);
    Pmenu.add(Bfactur, null);
    Pmenu.add(Balbcom, null);
    JPopupAlmacen.add(jMenuAlmacen);
    JPopupAlmacen.add(ItemInventario);
    JpopupVentas.add(ItemRepresen);
    JpopupPedVentas.add(pdpedven);
    JpopupPedVentas.add(clpevepr);
    //JpopupPedVentas.add(clpedven);
//    JpopupPedVentas.add(clvenrep);
    JPopupAlmacen.add(ALclstkdes);
    JPopupAlmacen.add(ALclresstock);
    JPopupAlmacen.add(ALlisaldos);
    JPopupAlmacen.add(ALCheckMvt);
    JPopupAlmacen.add(ALclUbiArt);

     JPopupAlmacen.add(ALConmvpr);
    JPopupAlmacen.add(ALMantPartes);
    JPopupAlmacen.add(pdregalm);
    JPopupAlmacen.add(costkpar);
     JPopupAlmacen.add(trasAlma);
    JPopupAlmacen.add(coarbtraz);
    JPopupAlmacen.add(creaStk);
    JPopupAlmacen.add(pdmotregu);
     JPopupAlmacen.add(repeti);
    JPopupAlmacen.add(clstkfeca);
    JPopupAlmacen.add(clinvcong);
    
     JPopupAlmacen.add(clresmvtos);
     JPopupAlmacen.add(clprodrec);
     JPopupAlmacen.add(cvregalm);
    
    jPopupCompras.add(albComCarne);
    jPopupCompras.add(albComPlanta);
    jPopupCompras.add(mnuReclPrv);
    jPopupCompras.add(comprasItem2);
    jPopupCompras.add(albaPrv);
    jPopupCompras.add(conalbco);
    jPopupCompras.add(clvertprv);
    jPopupCompras.add(clintalco);
    jPopupCompras.add(pdpagreal);
    jPopupCompras.add(cocaalco);
    jPopupCompras.add(clprpecoI);
    jPopupCompras.add(clrealco);
    jPopupCompras.add(lirefacco);
    jPopupCompras.add(pdpedco);
    jPopupCompras.add(copedco);
    jPopupCompras.add(pdfatra);
    jPopupCompras.add(mantartra);
    ItemInventario.add(cargaInv);
    ItemInventario.add(mantInv);
    ItemInventario.add(valInv);
     ItemInventario.add(trasInv);
    ItemInventario.add(lisdifIn);
    ItemInventario.add(coinvent);
    JPopupPad.add(PadFPA);
    JPopupPad.add(Padclientes);
    JPopupPad.add(PadPaises);
    JPopupPad.add(PadIdiomas);
    JPopupPad.add(MantVehi);
    JPopupPad.add(pdconfig);
    JPopupPad.add(pddiscrim);
    JPopupPad.add(pdreprese);
    JPopupPad.add(pdproveed);
    JPopupPad.add(pdtranspo);
    JPopupPad.add(pdarticu);
    JPopupPad.add(mantDisProVenta);
    JPopupPad.add(mantArticVenta);
    JPopupPad.add(lisclien);
    JPopupPad.add(tratclien);

    JPopupPad.add(pdgrupro);
    JPopupPad.add(pdfamipro);
    JPopupPad.add(pdtiposiva);
    JPopupPad.add(pddivisa);
    JPopupPad.add(pdbanteso);
    JPopupPad.add(pdnumerac);
    JPopupPad.add(pdusbempr);
    JPopupPad.add(pdempresa);
    JPopupPad.add(lkpdusua);
    JPopupPad.add(tipotari);
     JPopupPad.add(tarifa);
     JPopupPad.add(mantCalendar);
    JPopupPad.add(pdjercici);
    JPopupPad.add(pdmatadero);
    JPopupPad.add(pdgruart);
     popupMarg.add(mnuZoMarg) ;
     popupMarg.add(mnuInvMarg);
    popupMarg.add(mnuHistVen);
    popupMarg.add(mnuCalcTarifa);
    popupDesp.add(despMant);
    popupMarg.add(calculadora);
   // popupDesp.add(despTactil);
    popupDesp.add(mantDespTactil);
   // popupDesp.add(valdespi2);
    popupDesp.add(Valdespi);
    popupDesp.add(Mpdprvades);
    popupDesp.add(cldessv);
    popupDesp.add(codegen);
    popupDesp.add(recosdes);
    popupDesp.add(pdtipdes);
    JpopupVentas.add(clAlbSinCosto);
    JpopupVentas.add(pdalbara);
    JpopupVentas.add(cldepcli);
    JpopupVentas.add(lialbara);
    JpopupPedVentas.add(clrelpedven);
     JpopupVentas.add(manpralbar);
    JpopupVentas.add(clvenArt);
    JpopupVentas.add(coVenPro);
    JpopupVentas.add(conVenZonas);
    JpopupVentas.add(CLRankClie);
     JpopupVentas.add(mantTariCliente);
     JpopupVentas.add(mantAlbRuta);
    popupFact.add(lirelfact);
    popupFact.add(pdfactu);
    popupFact.add(genfactu);
    popupFact.add(anufactu); 
    popupFact.add(traspcont);
    popupFact.add(lisfactu);
    jPopupRiesgo.add(clriescli);
    jPopupRiesgo.add(clcobreal);
    jPopupRiesgo.add(clfactcob);
    jPopupRiesgo.add(cacobrea);
    jPopupRiesgo.add(pdcobruta);
    jPopupRiesgo.add(pdcobros);
    jPopupRiesgo.add(creareme);
    jPopupRiesgo.add(prueba3);
    jPopupVarios.add(MenuFicha);
    jPopupVarios.add(MenuLogs);
    EU=gnu.chu.Menu.LoginDB.cargaEntornoUsu();
    EU.usuario="cpuente";
    EU.password="anjelica";
    EU.email="jpuente.ext@riojasalud.es";
    EU.setUsuReser1("S");
    SystemOut sout=new SystemOut(System.err);    
    System.setOut(sout);        
    SystemOut serr=new SystemOut(System.err);
    serr.setSalidaError(true);
    System.setErr(serr);
    ventana.logger.trace("trace!!!");
    ventana.logger.debug("Debug!!!");
    ventana.logger.info("informacion!!!");
    ventana.logger.warn("warn!!!");
      System.out.println("salida estandar");
      System.err.println("salida errores");
    
  
//    EU.catalog="qc_pruebas";
    EU.ejercicio=Integer.parseInt( Formatear.getFechaAct("yyyy"));
//    EU.catalog="pruebas";
    EU.previsual=true;
    EU.setSimulaPrint(false);
    conexion ct = new conexion(EU);
    dtAdd=new DatosTabla(ct);
    
    EU.iniciarParametros(dtAdd);
    EU.lkEmpresa=gnu.chu.anjelica.pad.pdusua.getVLikeEmpresa("cpuente",dtAdd);
    LoginDB.iniciarLKEmpresa(EU,dtAdd);  
    this.setEnabled(false);
  }

    private void ntp() throws FileNotFoundException {
        File f = new File("c:/a/putty.log");
        BufferedReader fr = new BufferedReader(new FileReader(f));
        Hashtable<String, Integer> ht = new Hashtable();
        Integer i;
        //    char[] cbuf=new char[250];
        //    int ncar=0;
        String ip;
        String s;
        try {
            while ((s = fr.readLine()) != null) {
                if (s.startsWith("NTP Response sent to")) {
                    ip = s.substring(21);
                    //            System.out.println("ip: "+ip);
                    i = ht.get(ip);
                    if (i == null) {
                        ht.put(ip, new Integer(1));
                    } else {
                        ht.put(ip, i + 1);
                    }
                }
            }
            Enumeration en = ht.keys();
            while (en.hasMoreElements()) {
                s = en.nextElement().toString();
                System.out.println(s + ":" + ht.get(s));
            }
        } catch (IOException k) {
            System.out.println("Saliendo del fichero");
        }
    }

  void iniciar()
  {

    this.setEnabled(true);
    this.setIconImage(gnu.chu.utilidades.Iconos.getImageIcon("lanzador").
                      getImage());

//    Bvarios.requestFocus();
    this.setVisible(true);
    System.out.println(System.getProperty("java.io.tmpdir"));



   activarEventos();
//   prueba();
//    Balmac.doClick();
//    BvenZona.doClick();

//    BPedVen.doClick();
//    Bvarios.doClick();
//    Balbcom.doClick();
//    Bvenangulo.doClick();
//    despiece.doClick();
//    Bpad.doClick();
//    Bmargen.doClick();
//    Briesgo.doClick();
//    Bfactur.doClick();
  }


  void activarEventos()
  {
     pdpedven.addActionListener(new ActionListener()
     {
       public void actionPerformed(ActionEvent e)
       {
         Hashtable ht = new Hashtable();
         ht.put("admin", "true");
         lanzaEjecutable(new gnu.chu.anjelica.ventas.pdpeve(menu.this,EU, ht));         
       }
     });
    
     clpevepr.addActionListener(new ActionListener()
     {
       public void actionPerformed(ActionEvent e)
       {
            clpedidven_actionPerformed(e);
       }
     });

      mnuZoMarg.addActionListener(new ActionListener()
     {
       public void actionPerformed(ActionEvent e)
       {
         lanzaEjecutable(new gnu.chu.anjelica.margenes.Clmarzona(menu.this,EU));
       }
     });
      mnuInvMarg.addActionListener(new ActionListener()
     {
       public void actionPerformed(ActionEvent e)
       {
         lanzaEjecutable(new gnu.chu.anjelica.margenes.coresinv(menu.this,EU));
       }
     });
     mnuHistVen.addActionListener(new ActionListener()
     {
            @Override
       public void actionPerformed(ActionEvent e)
       {
         lanzaEjecutable(new gnu.chu.anjelica.margenes.CLHistVentas(menu.this,EU));
       }
     });
     calculadora.addActionListener(new ActionListener()
     {
            @Override
       public void actionPerformed(ActionEvent e)
       {
         lanzaEjecutable(new gnu.chu.utilidades.Calculadora());
       }
     });
     mnuCalcTarifa.addActionListener(new ActionListener()
     {
            @Override
       public void actionPerformed(ActionEvent e)
       {
         lanzaEjecutable(new gnu.chu.anjelica.margenes.CalcTarifa(menu.this,EU));
       }
     });

    lisfactu.addActionListener(new ActionListener()
     {
       public void actionPerformed(ActionEvent e)
       {
         lisfactu_actionPerformed(e);
       }
     });

    traspcont.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        traspcont_actionPerformed(e);
      }
    });

    ALlisaldos.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        ALlisaldos_actionPerformed(e);
      }
    });
    ALCheckMvt.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        ALCheckMvt_actionPerformed(e);
      }
    });

    ALclUbiArt.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        ALclUbiArt_actionPerformed(e);
      }
    });
    Briesgo.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Briesgo_actionPerformed();

      }
    });
    Bmargen.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Bmargen_actionPerformed();
      }
    });
    Balmac.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Balmac_actionPerformed();

      }
    });
    Balbcom.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent e) {
                jPopupCompras.show(Balbcom,20,20);
              }
            });
    Bfactur.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Bfactur_actionPerformed();
      }
    });

    Bvarios.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Bvarios_actionPerformed(e);
      }
    });
  BvenZona.addActionListener(new java.awt.event.ActionListener() {
     public void actionPerformed(ActionEvent e) {
       BvenZona_actionPerformed(e);
     }
   });
   BPedVen.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        BPedVen_actionPerformed(e);
      }
    });
//    Balbcom.addMouseListener(new MouseAdapter()
//    {
//      public void mouseReleased(MouseEvent e)
//      {
//       showPopMenu(e);
//      }
//
//      void showPopMenu(MouseEvent e)
//      {
//        if (e.isPopupTrigger())
//        {
//     ds
//        jPopupMenu1.show(e.getComponent(),
//                           Balbcom.getWidth()/2, Balbcom.getHeight()+2);
//        }
//      }
//      public void mousePressed(MouseEvent e)
//      {
//        showPopMenu(e);
//      }
//
//    });
    mnuCompras.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Bcompras_actionPerformed();
      }
    });
//    mnuAlbaCom.addActionListener(new ActionListener()
//    {
//      public void actionPerformed(ActionEvent e)
//      {
//        lanzaEjecutable(new gnu.chu.anjelica.compras.MantAlbComCarne(menu.this, EU));
//      }
//    });



        this.addWindowListener(new WindowAdapter()
        {
            @Override
          public void windowClosing(WindowEvent e)
          {
            System.exit(0);
          }
        });

        despiece.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            bdespiece_actionPerformed();
          }
        });
        Bpad.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            Bpad_actionPerformed();
          }
        });
  }
  void bdespiece_actionPerformed()
  {
    popupDesp.show( despiece,0,despiece.getSize().height);

//    Hashtable ht=new Hashtable();
//    ht.put("modPrecio","true");
//    ht.put("admin","true");
//    ht.put("tipoEtiq",""+gnu.chu.anjelica.despiece.pddespie.NORMAL);
//    lanzaEjecutable(new gnu.chu.anjelica.despiece.pddespie(menu.this,EU,ht));

//
//    lanzaEjecutable( new gnu.chu.anjelica.despiece. (menu.this,EU));
//    lanzaEjecutable( new gnu.chu.anjelica.despiece.codegen(menu.this,EU));
//    lanzaEjecutable( new gnu.chu.anjelica.despiece.pdprvades(menu.this,EU));
//    lanzaEjecutable( new gnu.chu.anjelica.despiece.cldespsv(menu.this,EU));

  }

  void Bcompras_actionPerformed()
  {
    jPopupCompras.show(mnuCompras,20,20);
    Hashtable ht = new Hashtable();
    ht.put("modPrecio","true");
//    ht.put("modConsulta","true");
//    ht.put("admin","true");
    ht.put("verPrecio","true");
//      lanzaEjecutable( new gnu.chu.anjelica.compras.pdclaslom(menu.this,EU));

//
//                  lanzaEjecutable(new gnu.chu.pruebas. gridedita(menu.this,EU));



//    lanzaEjecutable(new gnu.chu.anjelica.compras.conalbco(menu.this, EU));

//    lanzaEjecutable(new gnu.chu.anjelica.compras.pdfaccom(menu.this, EU,ht));
//    lanzaEjecutable(new gnu.chu.anjelica.compras.clintalco(menu.this, EU));
//        lanzaEjecutable(new gnu.chu.anjelica.compras.pdpedco(menu.this, EU,ht));
//        lanzaEjecutable(new gnu.chu.anjelica.compras.copedco(menu.this, EU,ht));
//             lanzaEjecutable(new gnu.chu.anjelica.despiece.valdespi(menu.this,EU));
//             lanzaEjecutable(new gnu.chu.anjelica.almacen.conmvpr(menu.this,EU));
//              lanzaEjecutable(new gnu.chu.anjelica.riesgos.impriesgo(menu.this,EU));
             lanzaEjecutable(new gnu.chu.pruebas.pru3(menu.this,EU));

//             lanzaEjecutable(new gnu.chu.pruebas.pru1(menu.this,EU));
            
//        lanzaEjecutable(new gnu.chu.anjelica.compras.anuEntra(menu.this,EU));
//    lanzaEjecutable(new gnu.chu.anjelica.compras.caimppor(menu.this, EU));
//        lanzaEjecutable(new gnu.chu.anjelica.compras.pdpedco(menu.this, EU));
//        lanzaEjecutable(new gnu.chu.anjelica.compras.lirefacco(menu.this, EU));
//    lanzaEjecutable( new gnu.chu.anjelica.compras.pdtransp(menu.this,EU,ht));
//    lanzaEjecutable( new gnu.chu.anjelica.compras.pdtaripor(menu.this,EU,ht));
//    lanzaEjecutable( new gnu.chu.anjelica.compras.pdfactra(menu.this,EU,ht));
//    lanzaEjecutable( new gnu.chu.anjelica.compras.clprpeco(menu.this,EU,ht));


  }
  void Bpad_actionPerformed()
  {
//    Hashtable ht=new Hashtable();
//    ht.put("verPrecio","true");
//    ht.put("modConsulta","false");
    JPopupPad.show(Bpad,10,20);
//    System.out.println(Formatear.format(-123.22,"---,--9.99"));

//    lanzaEjecutable( new gnu.chu.anjelica.pad.pdprodnue(menu.this,EU));
//    lanzaEjecutable( new gnu.chu.anjelica.pad.pdprodeq(menu.this,EU));
//    lanzaEjecutable( new gnu.chu.anjelica.pad.pdtarifa(menu.this,EU));
//    lanzaEjecutable( new gnu.chu.anjelica.pad.pdclien(menu.this,EU,ht));
//  lanzaEjecutable( new gnu.chu.anjelica.pad.pdprove(menu.this,EU,ht));
//    lanzaEjecutable( new gnu.chu.anjelica.pad.pdusua(menu.this,EU,ht));
//    lanzaEjecutable( new gnu.chu.anjelica.pad.pdartic(menu.this,EU,ht));
//    lanzaEjecutable( new gnu.chu.anjelica.pad.pdpais(menu.this,EU,ht));
//
//    lanzaEjecutable( new gnu.chu.anjelica.pad.pdnumerac(menu.this,EU,ht));
//    lanzaEjecutable( new gnu.chu.anjelica.inventario.creaStk(menu.this,EU));
//    lanzaEjecutable( new gnu.chu.anjelica.pad.pdmatadero(menu.this,EU,ht));
//        lanzaEjeacutable( new gnu.chu.anjelica.pad.pdsaladesp(menu.this,EU,ht));
//        lanzaEjecutable( new gnu.chu.anjelica.pad.pdbanteso(menu.this,EU,ht));
//        lanzaEjecutable( new gnu.chu.anjelica.pad.pdbanco(menu.this,EU,ht));
//    lanzaEjecutable( new gnu.chu.anjelica.pad.pdgruart(menu.this,EU));
//      lanzaEjecutable( new gnu.chu.anjelica.pad.pdejerci(menu.this,EU,ht));
//    lanzaEjecutable( new gnu.chu.anjelica.pad.pde mpresa(menu.this,EU,ht));
//    lanzaEjecutable( new gnu.chu.anjelica.pad.pdconfig(menu.this,EU,ht));
  }
  void Bvarios_actionPerformed(ActionEvent e) {
      jPopupVarios.show(Bvarios,0,18);
  
//     lanzaEjecutable(new gnu.chu.hylafax.envfaxcli(this,EU));
  }
  void lanzaFichaClientes()
  {
     Hashtable ht = new Hashtable();
    ht.put("agenteDef", "MA");
    lanzaEjecutable(new FichaClientes(this,EU,ht));  
  }
 void lanzaVerLogs()
  {
     Hashtable ht = new Hashtable();
    ht.put("allUser", "true");
    ht.put("modConsulta", "false");
    lanzaEjecutable(new gnu.chu.logs.MantLogs(this,EU,ht));
  }

  void Briesgo_actionPerformed()
  {
    jPopupRiesgo.show(Briesgo,0,18);
//    lanzaEjecutable(new gnu.chu.anjelica.margenes.coresinv(menu.this,EU));
//    lanzaEjecutable(new gnu.chu.anjelica.riesgos.clFactCob(menu.this,EU));
//    lanzaEjecutable(new gnu.chu.anjelica.riesgos.pdcobros(menu.this,EU));
//    lanzaEjecutable(new gnu.chu.anjelica.riesgos.pdcobruta(menu.this,EU));
//    lanzaEjecutable(new gnu.chu.anjelica.riesgos.cacobrea(menu.this,EU));
//    lanzaEjecutable(new gnu.chu.anjelica.riesgos.clriescli(menu.this,EU));
//    lanzaEjecutable(new gnu.chu.anjelica.riesgos.clcobrea(menu.this,EU));
//    lanzaEjecutable(new gnu.chu.anjelica.riesgos.pdriescl(menu.this,EU));
//    lanzaEjecutable(new gnu.chu.anjelica.tesoreria.creareme(menu.this,EU));
   
//    lanzaEjecutable(new gnu.chu.anjelica.tesoreria.pdlibrovto(menu.this,EU));
//        new instalar();
//        lanzaEjecutable(new gnu.chu.anjelica.tesoreria.mcllibrovto(menu.this,EU));
//        lanzaEjecutable(new gnu.chu.anjelica.tesoreria.pdpagreal(menu.this,EU));
//        lanzaEjecutable(new gnu.chu.anjelica.tesoreria.clpagreal(menu.this,EU));
  }

  void Bmargen_actionPerformed()
  {
       popupMarg.show( Bmargen,0,Bmargen.getSize().height);
//    lanzaEjecutable(new gnu.chu.anjelica.margenes.coresinv(menu.this,EU));
//    lanzaEjecutable(new gnu.chu.anjelica.margenes.colizona(menu.this,EU));

  }
  void  Bfactur_actionPerformed()
  {

    popupFact.show(Bfactur,0,18);
//    Hashtable ht=new Hashtable();
//    ht.put("modCons","false");
//    lanzaEjecutable(new gnu.chu.anjelica.facturacion.lirecibos(menu.this,EU));
//    lanzaEjecutable(new gnu.chu.anjelica.facturacion.lisfactu(menu.this,EU));
//    lanzaEjecutable(new gnu.chu.anjelica.facturacion.genfactu(menu.this,EU));
//    lanzaEjecutable(new gnu.chu.anjelica.ventas.ajuPrecios(menu.this,EU,ht));
//    lanzaEjecutable(new gnu.chu.anjelica.facturacion.pdfactu(menu.this,EU,ht));
//    System.out.println("num"+Formatear.Redondea(1234.125,2));
//      lanzaEjecutable(new gnu.chu.anjelica.facturacion.lirelfact(menu.this,EU));
//      lanzaEjecutable(new gnu.chu.anjelica.facturacion.traspcont(menu.this,EU));
  }

  void Balmac_actionPerformed()
  {
   
    JPopupAlmacen.show(Balmac,0,10);
 //   limpiaDB();

//      lanzaEjecutable(new gnu.chu.anjelica.inventario.cargainv(menu.this,EU));
//    lanzaEjecutable(new gnu.chu.anjelica.inventario.valinve(menu.this,EU));
//    lanzaEjecutable(new gnu.chu.anjelica.inventario.traspalma(menu.this,EU));

//    lanzaEjecutable(new gnu.chu.anjelica.almacen.pdalmace(menu.this,EU));
//     lanzaEjecutable(new gnu.chu.anjelica.almacen.conmvpr(menu.this,EU));
//     lanzaEjecutable(new gnu.chu.anjelica.inventario.cargainv(menu.this,EU));
//     lanzaEjecutable(new gnu.chu.anjelica.inventario.pdinven(menu.this,EU));
//     lanzaEjecutable(new gnu.chu.anjelica.almacen.pdregalm(menu.this,EU));
//     lanzaEjecutable(new gnu.chu.anjelica.almacen.pdmotregu(menu.this,EU));
//          lanzaEjecutable(new gnu.chu.anjelica.almacen.lisaldos(menu.this,EU));
//          lanzaEjecutable(new gnu.chu.anjelica.almacen.costkpar(menu.this,EU));
//
//     lanzaEjecutable(new gnu.chu.anjelica.almacen.clstkdes(menu.this,EU));
//     lanzaEjecutable(new gnu.chu.isql.isql(menu.this,EU));
//    lanzaEjecutable(new gnu.chu.isql.impExp(menu.this,EU));
  }

  public void lanzaEjecutable(ventana ejec)
  {
    ejec.setVisible(true);
    try {
      ejec.iniciarVentana();
      Pprinc.add( ejec);
      nVentana++;
      ejec.setLocation(0,0);
      
      ejec.addInternalFrameListener(new ifListen(ejec));
      ejec.guardaTamanoOriginal();
    } catch (Exception k)
    {
      k.printStackTrace();
      SystemOut.print(k);
   }
  }

  public JLayeredPane getPanelPrinc()
  {
    return Pprinc;
  }

  void BPedVen_actionPerformed(ActionEvent e) {
       JpopupPedVentas.show(BPedVen,20,20);
//    lanzaEjecutable(new gnu.chu.comm.cajanegra(menu.this,EU));
// lanzaEjecutable(new gnu.chu.logs.verLogs(menu.this,EU));
 //   lanzaEjecutable(new gnu.chu.hylafax.confaxes(menu.this,EU));
//    lanzaEjecutable(new gnu.chu.anjelica.angulo.Cointprtar(menu.this,EU));

//          lanzaEjecutable(new gnu.chu.anjelica.almacen.lisaldos(menu.this,EU));
//

//    lanzaEjecutable(new consVentas(this,EU));
//    lanzaEjecutable(new gnu.chu.anjelica.ventas.pdpeve(menu.this,EU));
//    lanzaEjecutable(new gnu.chu.anjelica.ventas.clpedven(menu.this,EU,new Hashtable()));
//      lanzaEjecutable(new gnu.chu.anjelica.JpopupVentas.clpevepr(menu.this,EU,new Hashtable()));
  }

  void BvenZona_actionPerformed(ActionEvent e) {
//    Hashtable ht=new Hashtable();
//    ht.put("modPrecio","true");
//    ht.put("admin","true");
//    ht.put("tipoEtiq",""+gnu.chu.anjelica.despiece.pddespie.NORMAL);
    JpopupVentas.show(BvenZona,20,20);
//    ht .put("zona","R1");

//    lanzaEjecutable(new gnu.chu.anjelica.JpopupVentas.pdalbara(menu.this,EU,ht));
//    lanzaEjecutable(new gnu.chu.anjelica.ventas.lihojruta(menu.this,EU));
//      lanzaEjecutable(new gnu.chu.anjelica.listados.lisclien(menu.this,EU));

  
//    lanzaEjecutable(new gnu.chu.anjelica.ventas.conVenProd(this,EU,ht));
//    lanzaEjecutable(new gnu.chu.anjelica.ventas.clAlbSinCosto(this,EU));

//    lanzaEjecutable(new gnu.chu.pruebas.pru1(menu.this,EU));
//    lanzaEjecutable(new gnu.chu.anjelica.almacen.trmaycalle(menu.this,EU));

//     lanzaEjecutable(new gnu.chu.anjelica.almacen.conmvpr(menu.this,EU));

//    lanzaEjecutable(new gnu.chu.anjelica.ventas.clvenart(menu.this,EU));
//    lanzaEjecutable(new gnu.chu.anjelica.ventas.clrealve(menu.this,EU));
//    lanzaEjecutable(new gnu.chu.anjelica.ventas.pdcliart(menu.this,EU));
  }
void prueba1(){
  String listaUsuarios="comunicacion\n"+
  "pmartinez";
  String usuario;
  StringTokenizer st=new StringTokenizer(listaUsuarios,"\n",false) ;
  while (st.hasMoreElements())
  {
    usuario=st.nextElement().toString();
    System.out.println("Usuario: "+usuario);
  }
  System.exit(0);
}

void limpiaDB()
{
  try{
    conexion ct = new conexion(EU);
    conexion ct1 = new conexion(EU);
    ct.setAutoCommit(false);
    ct1.setAutoCommit(false);
    java.sql.Statement st=ct.createStatement();
    dtBloq=new DatosTabla(ct);
    dtAdd=new DatosTabla(ct);

    String s;
    s = "select * from clientes where cli_codi=119";
    s = "update clientes set cli_nomb='EMBORG1' where cli_codi=119";
    dtAdd.executeUpdate(s);

    s = "select * from clientes where cli_codi = 119";

    dtBloq.select(s, true);

    dtBloq.next();
    System.out.println("Cliente: " + dtBloq.getString("cli_nomb"));
    dtAdd.rollback();
//    s="SELECT * FROM clientes ";
//    dtAdd.select(s,true);
//    do
//    {
//      dtAdd.edit(" cli_codi = "+dtAdd.getInt("cli_codi"));
//      dtAdd.setDato("cli_comen","Comentario: "+dtAdd.getInt("cli_codi"));
//      dtAdd.setDato("cli_nomb","Nombre "+dtAdd.getInt("cli_codi"));
//      dtAdd.setDato("cli_nomb","Nombre "+dtAdd.getInt("cli_codi"));
//      dtAdd.setDato("cli_nomco","Nombre Comercial"+dtAdd.getInt("cli_codi"));
//      dtAdd.setDato("cli_direc","Direccion "+dtAdd.getInt("cli_codi"));
//      dtAdd.setDato("cli_pobl","Poblacion "+dtAdd.getInt("cli_codi"));
//      dtAdd.setDato("cli_codpo",12345);
//      dtAdd.setDato("cli_telef","555-12345");
//      dtAdd.setDato("cli_fax","555-54321");
//      dtAdd.setDato("cli_nif","NIF123");
//      dtAdd.setDato("cli_percon","Persona Contacto "+dtAdd.getInt("cli_codi"));
//      dtAdd.setDato("cli_telcon","455-12345");
//      dtAdd.setDato("cli_nomen","Nombre Envio "+dtAdd.getInt("cli_codi"));
//      dtAdd.setDato("cli_diree","Direccion Envio "+dtAdd.getInt("cli_codi"));
//      dtAdd.setDato("cli_poble","Poblacion Envio "+dtAdd.getInt("cli_codi"));
//      dtAdd.setDato("cli_codpoe","2222");
//      dtAdd.setDato("cli_telefe","355-12345");
//      dtAdd.setDato("cli_faxe","355-8888");
//      dtAdd.setDato("cli_baofic","333332222");
//      dtAdd.setDato("cli_badico","00");
//      dtAdd.setDato("cli_bacuen","3333343333");
//      dtAdd.update(st);
//    } while (dtAdd.next());
//    s="SELECT * FROM v_proveedo ";
//    dtAdd.select(s,true);
//    do
//    {
//      dtAdd.edit(" prv_codi = " + dtAdd.getInt("prv_codi"));
//      dtAdd.setDato("prv_nomb", "Nombre " + dtAdd.getInt("prv_codi"));
//      dtAdd.setDato("prv_nomco", "Nombre Comercial " + dtAdd.getInt("prv_codi"));
//      dtAdd.setDato("prv_direc", "Direccion " + dtAdd.getInt("prv_codi"));
//      dtAdd.setDato("prv_pobl", "Poblacion " + dtAdd.getInt("prv_codi"));
//      dtAdd.setDato("prv_percon", "Pers. Contacto " + dtAdd.getInt("prv_codi"));
//      dtAdd.setDato("prv_nombre", "Nombre Rec. " + dtAdd.getInt("prv_codi"));
//      dtAdd.setDato("prv_direre", "Dir. Recogida  " + dtAdd.getInt("prv_codi"));
//      dtAdd.setDato("prv_poblre", "Pobl.Recogida " + dtAdd.getInt("prv_codi"));
//      dtAdd.setDato("prv_observ", "Observ. " + dtAdd.getInt("prv_codi"));
//      dtAdd.setDato("prv_codpo","112");
//      dtAdd.setDato("prv_telef","33112");
//      dtAdd.setDato("prv_fax","1332");
//      dtAdd.setDato("prv_nif","nif-112");
//      dtAdd.setDato("prv_telcon","23200112");
//      dtAdd.setDato("prv_telere","2222112");
//      dtAdd.setDato("prv_faxre","77112");
//      dtAdd.setDato("prv_nurgsa","NRGS"+dtAdd.getInt("prv_codi"));
//      dtAdd.setDato("prv_nexplo","Explot."+dtAdd.getInt("prv_codi"));
//       dtAdd.update(st);
//    } while (dtAdd.next());

    dtAdd.commit();
  } catch (Exception k)
  {
    SystemOut.print(k);
  }
}

  void Balmacen_ActionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.anjelica.almacen.pdalmace(menu.this,EU));
  }

  void BInventario_actionPerformed(ActionEvent e) {

  }

    void mnuReclPrv_actionPerformed(ActionEvent e) {
    Hashtable ht = new Hashtable();
     ht.put("modPrecio","true");
////    ht.put("modConsulta","true");
     ht.put("admin","true");
     ht.put("AlbSinPed","true");

//     ht.put("verPrecio","true");
     lanzaEjecutable(new gnu.chu.anjelica.compras.MantReclPrv(menu.this, EU,ht));
  }
  void comprasItem1_actionPerformed(ActionEvent e) {
    Hashtable ht = new Hashtable();
     ht.put("modPrecio","true");
////    ht.put("modConsulta","true");
     ht.put("admin","true");
     ht.put("AlbSinPed","true");

//     ht.put("verPrecio","true");
     lanzaEjecutable(new gnu.chu.anjelica.compras.MantAlbComCarne(menu.this, EU,ht));
  }
 void albComPlanta_actionPerformed(ActionEvent e) {
    Hashtable ht = new Hashtable();
     ht.put("modPrecio","true");
////    ht.put("modConsulta","true");
//     ht.put("admin","true");
     ht.put("AlbSinPed","true");

//     ht.put("verPrecio","true");
     lanzaEjecutable(new gnu.chu.anjelica.compras.MantAlbComPlanta(menu.this, EU,ht));
  }
  void comprasItem2_actionPerformed(ActionEvent e) {
    Hashtable ht = new Hashtable();
     ht.put("modPrecio","true");
//    ht.put("modConsulta","true");
//    ht.put("admin","true");
     ht.put("admin","true");

    lanzaEjecutable(new gnu.chu.anjelica.compras.pdfaccom(menu.this, EU,ht));
  }

  void conalbco_actionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.anjelica.compras.conalbco(menu.this, EU));
  }
 void albaPrv_actionPerformed(ActionEvent e) {
     Hashtable ht = new Hashtable();
     ht.put("modConsulta","false");
    lanzaEjecutable(new gnu.chu.anjelica.compras.MantAlbPrv(menu.this, EU,ht));
  }
  void cargaInv_actionPerformed(ActionEvent e) {
    Hashtable ht = new Hashtable();
    ht.put("repLineas","true");
    ht.put("admin","true");
    lanzaEjecutable(new gnu.chu.anjelica.inventario.PdInvControl(menu.this,EU,ht));
  }
 void clvenrep_actionPerformed(ActionEvent e) {
    Hashtable ht = new Hashtable();
//    ht.put("zonaRep","FV");
    ht.put("modif","true");
    lanzaEjecutable(new gnu.chu.anjelica.ventas.represen.CLVenRep(menu.this,EU,ht));
  }
 void CLRankClie_actionPerformed(ActionEvent e) {

    lanzaEjecutable(new gnu.chu.anjelica.ventas.CLRankClie(menu.this,EU));
  }
  void mantTariCliente_actionPerformed(ActionEvent e) {

    lanzaEjecutable(new gnu.chu.anjelica.pad.MantTariCliente(menu.this,EU));
  }
  void mantInv_actionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.anjelica.inventario.pdinven(menu.this,EU));
  }

  void valInv_actionPerformed(ActionEvent e) {
  lanzaEjecutable(new gnu.chu.anjelica.inventario.Valinven(menu.this,EU));
  }
 void trasInv_actionPerformed(ActionEvent e) {
  lanzaEjecutable(new gnu.chu.anjelica.inventario.TrasInven(menu.this,EU));
  }
  void lisdifIn_actionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.anjelica.inventario.ClDifInv(menu.this,EU));
  }

  void Padclientes_actionPerformed(ActionEvent e) {
    Hashtable ht=new Hashtable();
//    ht.put("verPrecio","true");
    ht.put("modConsulta","false");

    lanzaEjecutable( new gnu.chu.anjelica.pad.pdclien(menu.this,EU,ht));
  }
 void MantVehi_actionPerformed(ActionEvent e) {
    Hashtable ht=new Hashtable();
//    ht.put("verPrecio","true");
    ht.put("modConsulta","false");

    lanzaEjecutable( new gnu.chu.anjelica.pad.MantVehiculos(menu.this,EU,ht));
  }
  void PadPaises_actionPerformed(ActionEvent e) {
    Hashtable ht=new Hashtable();
//    ht.put("verPrecio","true");
    ht.put("modConsulta","false");

    lanzaEjecutable( new gnu.chu.anjelica.pad.MantPaises(menu.this,EU,ht));
  }
   void PadIdiomas_actionPerformed(ActionEvent e) {    

    lanzaEjecutable( new gnu.chu.anjelica.pad.MantIdiomas(menu.this,EU));
  }
  void PadFPA_actionPerformed(ActionEvent e) {
    Hashtable ht=new Hashtable();
//    ht.put("verPrecio","true");
//    ht.put("modConsulta","false");

        lanzaEjecutable( new gnu.chu.anjelica.pad.pdforpago(menu.this,EU,ht));

  }

  void pdconfig_actionPerformed(ActionEvent e) {
    Hashtable ht=new Hashtable();
    ht.put("modConsulta","false");

    lanzaEjecutable( new gnu.chu.anjelica.pad.pdconfig(menu.this,EU,ht));

  }

  void pddiscrim_actionPerformed(ActionEvent e) {
    Hashtable ht=new Hashtable();
        ht.put("modConsulta","false");

        lanzaEjecutable( new gnu.chu.anjelica.pad.pddiscrim(menu.this,EU,ht));

  }
void pdreprese_actionPerformed(ActionEvent e) {
    Hashtable ht=new Hashtable();
        ht.put("modConsulta","false");

        lanzaEjecutable( new gnu.chu.anjelica.pad.MantRepres(menu.this,EU,ht));

  }
  void ALclstkdes_actionPerformed(ActionEvent e) {
       lanzaEjecutable(new gnu.chu.anjelica.almacen.clstkdes(menu.this,EU));

  }

  void ALclresstock_actionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.anjelica.almacen.clresstock(menu.this,EU));
  }
  void ALlisaldos_actionPerformed(ActionEvent e) {
      
    lanzaEjecutable(new gnu.chu.anjelica.almacen.lisaldos(menu.this,EU));
  }
  void ALCheckMvt_actionPerformed(ActionEvent e) {
             
    lanzaEjecutable(new gnu.chu.anjelica.almacen.CheckMvtos(menu.this,EU));
  }
   void ALclUbiArt_actionPerformed(ActionEvent e) {
      
    lanzaEjecutable(new gnu.chu.anjelica.almacen.CLUbicArt(menu.this,EU));
  }
  
  void ALConmvpr_actionPerformed(ActionEvent e) {
    Hashtable<String,String> ht=new Hashtable();
    ht.put("verprecio", "1");
    
    lanzaEjecutable(new gnu.chu.anjelica.almacen.Comvalm(menu.this,EU,ht));

  }
  void ALMantPartes_actionPerformed(ActionEvent e) {
    Hashtable ht=new Hashtable();
    ht.put("estados", "0");
    ht.put("admin", "true");
    lanzaEjecutable(new gnu.chu.anjelica.almacen.MantPartes(menu.this,EU,ht));
    
    
  }
  
  void pdproveed_actionPerformed(ActionEvent e) {
    Hashtable ht = new Hashtable();
    ht.put("modConsulta", "false");
      lanzaEjecutable( new gnu.chu.anjelica.pad.pdprove(menu.this,EU,ht));
  }
 void pdtranspo_actionPerformed(ActionEvent e) {
      lanzaEjecutable( new gnu.chu.anjelica.pad.pdtransp(menu.this,EU));
  }
  void pdarticu_actionPerformed(ActionEvent e)
  {
    Hashtable ht = new Hashtable();
    ht.put("modConsulta", "false");
    lanzaEjecutable(new gnu.chu.anjelica.pad.MantArticulos(menu.this, EU, ht));

  }
   void mantDisProVenta_actionPerformed(ActionEvent e)
  {
    Hashtable ht = new Hashtable();
    ht.put("modConsulta", "false");
    lanzaEjecutable(new gnu.chu.anjelica.pad.MantDisProVenta(menu.this, EU, ht));

  }
  void mantArticVenta_actionPerformed(ActionEvent e)
  {
    Hashtable ht = new Hashtable();
    
    lanzaEjecutable(new gnu.chu.anjelica.pad.MantArticVenta(menu.this, EU));

  }
  void lisclien_actionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.anjelica.listados.ClClien(menu.this, EU));
  }
 void tratclien_actionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.anjelica.pad.MantCliActiv(menu.this, EU));
  }
  void despMant_actionPerformed(ActionEvent e) {
    Hashtable ht=new Hashtable();
//    ht.put("modPrecio","true");
    ht.put("admin","true"); 
    ht.put("tipoEtiq",""+gnu.chu.anjelica.despiece.MantDesp.NORMAL);

//    lanzaEjecutable(new gnu.chu.anjelica.despiece.pddespie(menu.this,EU,ht));
    lanzaEjecutable(new gnu.chu.anjelica.despiece.MantDesp(menu.this,EU,ht));
  }

  
  void mantDespTactil_actionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.anjelica.despiece.MantDespTactil(menu.this,EU,null));

  }
  void pdgrupro_actionPerformed(ActionEvent e) {
    Hashtable ht=new Hashtable();
    ht.put("modConsulta","false");
    lanzaEjecutable(new gnu.chu.anjelica.pad.pdgruppro(menu.this,EU,ht));
  }

  void pdfamipro_actionPerformed(ActionEvent e) {
    Hashtable ht=new Hashtable();
       ht.put("modConsulta","false");
       lanzaEjecutable(new gnu.chu.anjelica.pad.MantFamPro(menu.this,EU,ht));

  }

  void pdtiposiva_actionPerformed(ActionEvent e) {
    Hashtable ht=new Hashtable();
           ht.put("modConsulta","false");
          
        lanzaEjecutable(new gnu.chu.anjelica.pad.MantTipoIVA(menu.this,EU,ht));
  }

  void pddivisa_actionPerformed(ActionEvent e) {
    Hashtable ht=new Hashtable();
            ht.put("modConsulta","false");
            lanzaEjecutable(new gnu.chu.anjelica.pad.pddivisa(menu.this,EU,ht));

  }

  void pdregalm_actionPerformed(ActionEvent e) {
       lanzaEjecutable(new gnu.chu.anjelica.almacen.pdregalm(menu.this,EU,new Hashtable<String, String>()));

  }

  void costkpar_actionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.anjelica.almacen.costkpar(menu.this,EU));
  }
   void trasAlma_actionPerformed(ActionEvent e) {
    Hashtable ht=new Hashtable();
    ht.put("admin","true");
    //lanzaEjecutable(new gnu.chu.anjelica.inventario.traspalma(menu.this,EU));
    lanzaEjecutable(new gnu.chu.anjelica.almacen.MantTraspAlm(menu.this,EU,ht));
  }
  void prueba()
  {
//    lanzaEjecutable(new gnu.chu.pruebas.arboles(menu.this,EU));
  }

  void pdbanteso_actionPerformed(ActionEvent e) {
    Hashtable ht=new Hashtable();
    ht.put("modConsulta","false");
    lanzaEjecutable( new gnu.chu.anjelica.pad.pdbanteso(menu.this,EU,ht));
  }

  void coarbtraz_actionPerformed(ActionEvent e) {
    lanzaEjecutable( new gnu.chu.anjelica.almacen.coarbtraz(menu.this,EU));
  }

  void clAlbSinCosto_actionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.anjelica.ventas.clAlbSinCosto(this,EU));
  }

  void pdalbara_actionPerformed(ActionEvent e) {
    Hashtable ht=new Hashtable();
    ht.put("modPrecio","true");
//     ht.put("ponPrecio","true");
    ht.put("admin","true");
    if (dtAdd.getConexion().getConURL().contains("anjelicars"))
    {
        ht.put("facil","true");
        ht.put("etiAlbaran","true");
    }
//    ht.put("tipoEtiq",""+gnu.chu.anjelica.despiece.pddespie.NORMAL);

    lanzaEjecutable(new gnu.chu.anjelica.ventas.pdalbara(this,EU, ht));
  }
  void cldepcli_actionPerformed() {
   
//    ht.put("tipoEtiq",""+gnu.chu.anjelica.despiece.pddespie.NORMAL);

    lanzaEjecutable(new gnu.chu.anjelica.ventas.CLDepCli(this,EU));
  }
  void lialbara_actionPerformed(ActionEvent e) {

    lanzaEjecutable(new gnu.chu.anjelica.ventas.clrealve(this,EU));
  }
    void clrelalbve_actionPerformed(ActionEvent e) {
    HashMap ht=new HashMap();
    lanzaEjecutable(new gnu.chu.anjelica.ventas.CLPedidVen(this,EU,ht));
  }
  void clpedidven_actionPerformed(ActionEvent e) {
    HashMap ht=new HashMap();
    lanzaEjecutable(new gnu.chu.anjelica.ventas.CLPeVenPro(this,EU,ht));
  }
 void manpralb_actionPerformed(ActionEvent e) {
    Hashtable ht=new Hashtable();
//    ht.put("modConsulta","true");
  
    lanzaEjecutable(new gnu.chu.anjelica.ventas.MantPrAlb(this,EU, ht));
  }
  void clvenArt_actionPerformed(ActionEvent e) {
   lanzaEjecutable(new gnu.chu.anjelica.ventas.clvenart(menu.this,EU));
  }
   void coVenPro_actionPerformed(ActionEvent e) {
   lanzaEjecutable(new gnu.chu.anjelica.ventas.conVenProd(menu.this,EU));
  }
 void conVenZonas_actionPerformed(ActionEvent e) {
   Hashtable ht=new Hashtable();
   ht.put("verMargen","true");
   lanzaEjecutable(new gnu.chu.anjelica.ventas.Covezore(menu.this,EU,ht));
  }
  void lirelfact_actionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.anjelica.facturacion.Clrelfact(menu.this,EU));
  }

  void coinvent_actionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.anjelica.inventario.coinvent(menu.this,EU));
  }

  void creaStk_actionPerformed(ActionEvent e) {
    lanzaEjecutable( new gnu.chu.anjelica.inventario.CreaStkPart(menu.this,EU));
  }

  void pdfactu_actionPerformed(ActionEvent e) {
    Hashtable ht=new Hashtable();
    ht.put("modCons","false");
    lanzaEjecutable(new gnu.chu.anjelica.facturacion.PadFactur(menu.this,EU,ht));
  }

  void genfactu_actionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.anjelica.facturacion.GenFactur(menu.this,EU));
  }
  void lisfactu_actionPerformed(ActionEvent e) {
     lanzaEjecutable(new gnu.chu.anjelica.facturacion.lisfactu(menu.this,EU));
   }

  void pdcobros_actionPerformed(ActionEvent e) {
  lanzaEjecutable(new gnu.chu.anjelica.riesgos.pdcobros(menu.this,EU));
  }

  void clriescli_actionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.anjelica.riesgos.ClRiesClien(menu.this,EU));
  }
   void clcobreal_actionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.anjelica.riesgos.clcobrea(menu.this,EU));
  }
   void clfactcob_actionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.anjelica.riesgos.clFactCob(menu.this,EU));
  }
  void cacobrea_actionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.anjelica.riesgos.cacobrea(menu.this,EU));
  }
  void pdcobruta_actionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.anjelica.riesgos.pdcobruta(menu.this,EU));
  }
  void pdmotregu_actionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.anjelica.almacen.pdmotregu(menu.this,EU));

  }
   void repeti_actionPerformed(ActionEvent e) {
   lanzaEjecutable(new gnu.chu.anjelica.listados.repetiqu(menu.this,EU));

  }
 void clstkfeca_actionPerformed(ActionEvent e) {
     Hashtable ht=new Hashtable();
    ht.put("verCostos","true");
//    ht.put("verCostos","false");
    lanzaEjecutable(new gnu.chu.anjelica.almacen.clstkfeca(menu.this,EU,ht));

  }
 void clinvcong_actionPerformed(ActionEvent e) {
     Hashtable ht=new Hashtable();
    ht.put("verCostos","true");
//    ht.put("verCostos","false");
    lanzaEjecutable(new gnu.chu.anjelica.almacen.CLinvcong(menu.this,EU,ht));

  }
 void clresmvtos_actionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.anjelica.almacen.CLResMvtos(menu.this,EU));

  }
  void clprodreci_actionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.anjelica.almacen.CLProdReci(menu.this,EU));
  }
  void cvregalm_actionPerformed(ActionEvent e) {
    Hashtable ht=new Hashtable();
    ht.put("admin","true");
    lanzaEjecutable(new gnu.chu.anjelica.almacen.CVRegAlm(menu.this,EU,ht));
  }
  void mantAlbRuta_actionPerformed(ActionEvent e) {
    Hashtable ht=new Hashtable();
//    ht.put("modSala","true");
    lanzaEjecutable(new gnu.chu.anjelica.ventas.ManAlbRuta(menu.this,EU,ht));
  }
  
  void pdnumerac_actionPerformed(ActionEvent e) {
    Hashtable ht=new Hashtable();
//    ht.put("verPrecio","true");
    ht.put("modConsulta","false");

    lanzaEjecutable(new gnu.chu.anjelica.pad.pdnumeracion(menu.this,EU,ht));
  }

  void clvertprv_actionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.anjelica.compras.clvertprv(menu.this,EU));
  }
void clintalco_actionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.anjelica.compras.clintalco(menu.this,EU));
  }
  void pdpagreal_actionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.anjelica.tesoreria.pdpagreal(menu.this,EU));
  }

  void creareme_actionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.anjelica.tesoreria.creareme(menu.this,EU));
  }

  void pdusbempr_actionPerformed(ActionEvent e) {
    Hashtable ht=new Hashtable();
//    ht.put("verPrecio","true");
    ht.put("modConsulta","false");

    lanzaEjecutable(new gnu.chu.anjelica.pad.pdsubempr(menu.this,EU,ht));
  }
 void pdempresa_actionPerformed(ActionEvent e) {
    Hashtable ht=new Hashtable();
//    ht.put("verPrecio","true");
    ht.put("modConsulta","false");

    lanzaEjecutable(new gnu.chu.anjelica.pad.pdempresa(menu.this,EU,ht));
  }
  void pdusua_actionPerformed(ActionEvent e) {
    Hashtable ht=new Hashtable();
//    ht.put("verPrecio","true");
  ht.put("modConsulta","false");

    lanzaEjecutable( new gnu.chu.anjelica.pad.pdusua(menu.this,EU,ht));
  }

  void cocaalco_actionPerformed(ActionEvent e) {
//    Hashtable ht=new Hashtable();
//    ht.put("verPrecio","true");
//   ht.put("modConsulta","false");

     lanzaEjecutable( new gnu.chu.anjelica.compras.cocaalco(menu.this,EU));

  }
  void clprpecoI_actionPerformed(ActionEvent e) {
    Hashtable<String,String> ht=new Hashtable();
    ht.put("verPrecio","true");
//   ht.put("modConsulta","false");

     lanzaEjecutable( new gnu.chu.anjelica.compras.clprpeco(menu.this,EU,ht));

  }
void clrelalco_actionPerformed(ActionEvent e) {
     lanzaEjecutable( new gnu.chu.anjelica.compras.Clrelalbco(menu.this,EU));

  }
  void lirefacco_actionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.anjelica.compras.lirefacco(menu.this, EU));
  }

  void tipotari_actionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.anjelica.pad.pdtipotar(menu.this, EU));
  }
  void tarifa_actionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.anjelica.pad.MantTarifa(menu.this, EU));
  }
  void mantCalendar_actionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.anjelica.pad.MantCalendar(menu.this, EU,null));
  }
  void anufactu_actionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.anjelica.facturacion.AnuFactur(menu.this, EU));
  }
  void traspcont_actionPerformed(ActionEvent e) {
      lanzaEjecutable(new gnu.chu.anjelica.facturacion.traspcont(menu.this, EU));
    }

  void pdpedco_actionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.anjelica.compras.pdpedco(menu.this, EU));
  }

  void copedco_actionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.anjelica.compras.copedco(menu.this, EU));
  }
void pdfatra_actionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.anjelica.compras.pdfactra(menu.this, EU));
  }
  void mantartra_actionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.anjelica.compras.pdtaripor(menu.this, EU));
  }
  void pdjercici_actionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.anjelica.pad.pdejerci(menu.this, EU,null));
  }
   void pdmatadero_actionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.anjelica.pad.pdmatadero(menu.this, EU,null));
  }
void pdgruart_actionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.anjelica.pad.pdgruart(menu.this, EU));
  }
  
  void Valdespi_actionPerformed(ActionEvent e) {
       Hashtable ht=new Hashtable();
       ht.put("modAdmin","true");
       lanzaEjecutable(new gnu.chu.anjelica.despiece.ValDespi(menu.this, EU,ht));
  }
  void Mpdprvades_actionPerformed() {
       
       lanzaEjecutable(new gnu.chu.anjelica.despiece.pdprvades(menu.this, EU));
  }
   void cldessv_actionPerformed(ActionEvent e) {
         Hashtable ht=new Hashtable();
       ht.put("admin","true");
    lanzaEjecutable(new gnu.chu.anjelica.despiece.cldespsv(menu.this, EU,ht));
  }
  void codegen_actionPerformed(ActionEvent e) {
 //   lanzaEjecutable(new gnu.chu.anjelica.despiece.codegen(menu.this, EU));
    lanzaEjecutable(new gnu.chu.anjelica.despiece.Cldegen(menu.this, EU));
  }
  void recosdes_actionPerformed(ActionEvent e) {
 //   lanzaEjecutable(new gnu.chu.anjelica.despiece.codegen(menu.this, EU));
    lanzaEjecutable(new gnu.chu.anjelica.despiece.RegCosDes(menu.this, EU));
  }
  void pdtipdes_actionPerformed(ActionEvent e) {
 //   lanzaEjecutable(new gnu.chu.anjelica.despiece.codegen(menu.this, EU));
    lanzaEjecutable(new gnu.chu.anjelica.despiece.MantTipDesp(menu.this, EU));
  }
  void prueba3_actionPerformed(ActionEvent e) {
    lanzaEjecutable(new gnu.chu.pruebas.pru3(menu.this,EU));
  }

}

class ifListen extends  InternalFrameAdapter
{
  ventana intfra;
  public ifListen(ventana ej)
  {
    this.intfra=ej;
  }
  public void internalFrameClosing(InternalFrameEvent e) {
    intfra.setVisible(false);
    intfra.dispose();
  }
}
