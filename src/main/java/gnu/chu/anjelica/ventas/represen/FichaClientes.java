package gnu.chu.anjelica.ventas.represen;

import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.controles.*;
import gnu.chu.interfaces.*;
import gnu.chu.utilidades.*;
import java.awt.*;
import java.awt.GridBagLayout;
import java.awt.event.*;
import java.sql.*;
import gnu.chu.sql.*;
import java.util.*;
import gnu.chu.anjelica.ventas.*;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.*;
import gnu.chu.Menu.*;
import gnu.chu.anjelica.menu;
import gnu.chu.anjelica.pad.MantRepres;
import gnu.chu.anjelica.pad.pdclien;
import gnu.chu.camposdb.cliPanel;
import java.text.ParseException;
import java.util.Date;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * <p>Titulo: FichaClientes</p>
 * <p>Descripcion: Programa que permite ver los ultimos datos de un cliente. Esto incluye sus
 * ultimos albaranes y/o facturas. Cobros realizados, importe pendiente de cobro y comentarios.
 *  </p><p>Tambien permite modificar ciertos datos de conctacto del cliente.
 * </p>
 * <p>Copyright: Copyright (c) 2005-2017
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
 */

public class FichaClientes extends ventana implements PAD
{
  private boolean verCobros=false; // Para no ver cobros pend.
  private boolean  swActCli=false;
  private boolean relClien=true;
  boolean mataConsulta=false;
  int numDec=2;
  boolean swBusDat=false;
  final boolean CONBD=true;
  String s;
  CPanel Pprinc = new CPanel();
  PRelClien pRelClien = new PRelClien();
  CTabbedPane TPanel = new CTabbedPane();
  CPanel Pcliente = new CPanel();
  CPanel pGraf = new CPanel();
  CPanel pGrNorth = new CPanel();
  CPanel pGrSouth = new CPanel();
  CLabel cLabel1 = new CLabel();
  cliPanel cli_codiE = new cliPanel();
  CTextField cli_nombE = new CTextField(Types.CHAR,"X",40);
  CTextField cli_codrepE = new CTextField(Types.CHAR,"X",5);
  CLabel cLabel2 = new CLabel();
  CTextField cli_poblE = new CTextField(Types.CHAR,"X",30);
  CLabel cLabel4 = new CLabel();
  CTextField cli_direE = new CTextField(Types.CHAR,"X",50);
  CLabel cLabel5 = new CLabel();
  CTextField cli_telefE = new CTextField(Types.CHAR,"X",15);
  CLabel cLabel6 = new CLabel();
  CTextField cli_perconE = new CTextField(Types.CHAR,"x",30);
  CButton Bbuscar = new CButton("Aceptar F4",Iconos.getImageIcon("check"));
  CPanel Pdatcli = new CPanel();
  CLabel cLabel7 = new CLabel();
  CTextField cli_faxE = new CTextField(Types.CHAR,"X",15);
  CLabel cLabel8 = new CLabel();
  CLinkBox zon_codiE = new CLinkBox();
  CLabel cLabel9 = new CLabel();
  CLinkBox rep_codiE = new CLinkBox();
  CLabel cLabel10 = new CLabel();
  CComboBox cli_activE = new CComboBox();
  Cgrid jtAlb = new Cgrid(7);
//  conexion ct;
  CLabel cLabel11 = new CLabel();
  CTextField avc_serieE = new CTextField(Types.CHAR,"X",1);
  CTextField avc_numeE = new CTextField(Types.DECIMAL,"####9");
  CLabel cLabel12 = new CLabel();
  CTextField avc_fecalbE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel cLabel13 = new CLabel();
  CTextField fvc_numeE = new CTextField(Types.DECIMAL,"####9");
  CLabel cLabel14 = new CLabel();
  CTextField usu_nombE = new CTextField(Types.CHAR,"X",20);
  CLabel cLabel15 = new CLabel();
  CLabel cLabel16 = new CLabel();
  CTextField avc_impalbE = new CTextField(Types.DECIMAL,"-,---,--9.99");
  CTextField avc_impcobE = new CTextField(Types.DECIMAL,"-,---,--9.99");
  CPanel Palbave = new CPanel();
  DatosTabla dtCli;
  DatosTabla dtAlb;
  DatosTabla dtCont;
//  DatosTabla dtCon1;
//   DatosTabla dtStat;
  DatosTabla dtCob;
  DatosTabla dtBlo;
  ayuVenPro ayVePr =null;
  lotVenPro ayLoPr =null;

  navegador navcli = new navegador(this,false,navegador.CURYCON);
  CButton realPedi = new CButton(Iconos.getImageIcon("pon"));
     navegador navAlb = new navegador(null, false, navegador.SOLCUR)
     {
        @Override
       protected void procesaAnt()
       {
         try
         {
           if (dtAlb.isFirst())
             return;
           if (dtAlb.previous())
           {
             verDatAlbaran(dtAlb);
             mensaje("");
           }
         }
         catch (Exception ex)
         {
         }
       }

        @Override
       protected void procesaSigu()
       {
         try
         {
           if (dtAlb.isLast())
             return;
           if (dtAlb.next())
           {
             verDatAlbaran(dtCli);
             mensaje("");

           }
         }
         catch (Exception ex)
         {
           SystemOut.print(ex);
         }

       }

        @Override
       protected void procesaPrimero()
       {
         try
         {
           if (dtAlb.isFirst())
             return;
           if (dtAlb.first())
           {
             verDatAlbaran(dtAlb);
             mensaje("");
           }
         }
         catch (Exception ex)
         {
           SystemOut.print(ex);
         }
       }

        @Override
       protected void procesaUltimo()
       {
         try
         {
           if (dtAlb.isLast())
             return;
           if (dtAlb.last())
           {
             verDatAlbaran(dtAlb);
             mensaje("");
           }
         }
         catch (Exception ex)
         {
           SystemOut.print(ex);
         }
       }
     };
  CPanel Pcobros = new CPanel();
  CPanel cPanel2 = new CPanel();
  CLabel cLabel17 = new CLabel();
  CTextField cdo_conceE = new CTextField(Types.DECIMAL,"-,---,--9.99");
  CLabel cLabel18 = new CLabel();

  CLabel cLabel19 = new CLabel();
  CTextField cob_riestotE = new CTextField(Types.DECIMAL,"-,---,--9.99");
  CTextField cbo_albpeiE = new CTextField(Types.DECIMAL,"-,---,--9.99");
  CTextField cob_frapenE = new CTextField(Types.DECIMAL,"##9");
  CLabel cLabel110 = new CLabel();
  CLabel cLabel111 = new CLabel();
  CTextField cob_talcaiE = new CTextField(Types.DECIMAL,"-,---,--9.99");
  CTextField cob_talcanE = new CTextField(Types.DECIMAL,"##9");
  CLabel cLabel112 = new CLabel();

  CLabel cLabel20 = new CLabel();
  CTextField cob_frapeiE= new CTextField(Types.DECIMAL,"-,---,--9.99");
  CTextField cob_devoiE= new CTextField(Types.DECIMAL,"-,---,--9.99");
  CTextField cob_devonE= new CTextField(Types.DECIMAL,"##9");
  CTextField fpa_nombE = new CTextField();
  CTextField cob_albpenE = new CTextField(Types.DECIMAL,"##9");
  CTextField cob_albpeiE = new CTextField(Types.DECIMAL,"-,---,--9.99");
  CLabel cLabel21 = new CLabel();
  CTextField cob_crelibE = new CTextField(Types.DECIMAL,"-,---,--9.99");
  CLabel cLabel22 = new CLabel();
  CTextField cob_cresobE = new CTextField(Types.DECIMAL,"-,---,--9.99");
  Cgrid jtAco = new Cgrid(8);
  Cgrid jtFco = new Cgrid(6);
  CPanel cPanel3 = new CPanel();
  CPanel cPanel4 = new CPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CPanel cPanel5 = new CPanel();
  Cgrid jtTpe = new Cgrid(7);
  CLabel cLabel113 = new CLabel();
  CTextField cob_devanE = new CTextField(Types.DECIMAL,"##9");
  CTextField cob_devaiE = new CTextField(Types.DECIMAL,"-,---,--9.99");
  CLabel cLabel114 = new CLabel();
  CTextField cob_devfnE = new CTextField(Types.DECIMAL,"##9");
  CTextField cob_devfiE = new CTextField(Types.DECIMAL,"-,---,--9.99");
  CPanel cPanel6 = new CPanel();
  BorderLayout borderLayout3 = new BorderLayout();
  CPanel cPanel8 = new CPanel();
  CLabel cLabel24 = new CLabel();
  CLabel cLabel25 = new CLabel();
  CLinkBox zon_codiE1 = new CLinkBox();
  CTextField cli_cod1E = new CTextField(Types.DECIMAL,"####9");
  CLabel cli_nombL = new CLabel();
  CButton Bacezon = new CButton();
  CLinkBox zon_codiE2 = new CLinkBox();
  CLabel cLabel26 = new CLabel();
  menu padre;
  CPanel Pcomen = new CPanel();
    Cgrid jtCom = new Cgrid(2);
  JScrollPane jScrollPane1 = new JScrollPane();
  JTextArea cpv_textE = new JTextArea();
  CLabel cLabel28 = new CLabel();
  CTextField cpv_fechaE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CButton Bmodcom = new CButton();
  CPanel Pvenpro = new CPanel();
  CLabel cLabel29 = new CLabel();
  CTextField feinvpE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CTextField fefivpE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel cLabel210 = new CLabel();
  Cgrid jtcopr = new Cgrid(5);
  JScrollPane jScrollPane2 = new JScrollPane();
  JTextArea cpc_comeE = new JTextArea();
  CButton Bmocopr = new CButton();
  CButton Bincopr = new CButton();
  CButton Bbocopr = new CButton();
  CLabel cLabel30 = new CLabel();
  CTextField pro_codiE = new CTextField(Types.DECIMAL,"####9");
  CLabel pro_nombL = new CLabel();
  CButton Bbusalb = new CButton(Iconos.getImageIcon("find"));
  CPanel cPanel9 = new CPanel();
  CTextField emp_codiE = new CTextField(Types.DECIMAL,"99");
  CLabel cLabel32 = new CLabel();
  CCheckBox opAgrlin = new CCheckBox();
  String agente=null;
  String agenteDef=null;
  CLabel exceCredL = new CLabel();
  CLabel exceFechaL = new CLabel();
  CButton Blote = new CButton();
  CLabel cli_servirL = new CLabel("Servir");
  CComboBox cli_servirE = new CComboBox();
  CLabel cLabel34 = new CLabel();
  CTextField cli_comenE = new CTextField(Types.CHAR,"X",255);
  CButton Bcancel = new CButton("Cancelar",Iconos.getImageIcon("cancel"));
  CLabel cLabel35 = new CLabel();
  CTextField cli_telconE = new CTextField(Types.CHAR,"X",15);
  boolean supUsua=false;
  CPanel cPanel1 = new CPanel();
  Cgrid cgrid1 = new Cgrid(4);
  CCheckBox opSoloCli = new CCheckBox();
  CLabel cLabel23 = new CLabel();
  CComboBox albfacE = new CComboBox();
  CButton Bbusfra = new CButton(Iconos.getImageIcon("find"));
  CTextField fvc_anoE = new CTextField(Types.DECIMAL,"###9");
  CTextField cli_nomcoE = new CTextField(Types.CHAR,"X",50);
  CTextField cli_pobleE = new CTextField(Types.CHAR,"X",30);
  CLabel cLabel36 = new CLabel();
  CLabel cLabel37 = new CLabel();
  CTextField cli_direeE = new CTextField(Types.CHAR,"X",50);
  CLabel cLabel115 = new CLabel();
  CTextField fvc_basimpE = new CTextField(Types.DECIMAL,"-,---,--9.99");
    private GridBagLayout gridBagLayout2 = new GridBagLayout();
    private CPanel pCabCli = new CPanel();
    private CLabel cli_nombL1 = new CLabel();

    public FichaClientes(EntornoUsuario eu, Principal p) {
    this(eu,p,null);
 }


  public FichaClientes(EntornoUsuario eu, Principal p,Hashtable ht) {
   EU=eu;
   vl=p.panel1;
   jf=p;
   eje=true;

   setTitulo("Consulta Datos Clientes ");

   try  {
     if (ht != null)
     {
       if (ht.get("agente") != null)
         agente = ht.get("agente").toString();
       if (ht.get("agenteDef") != null)
         agenteDef = ht.get("agenteDef").toString();
       if (ht.get("supUsua") != null)
         supUsua = Boolean.parseBoolean(ht.get("supUsua").toString());
     }

     if(jf.gestor.apuntar(this))
         jbInit();
     else
       setErrorInit(true);
   }
   catch (Exception e) {
     ErrorInit(e);
   }
 }

 public FichaClientes(menu p,EntornoUsuario eu,Hashtable ht) {

   EU=eu;
   vl=p.getLayeredPane();
   setTitulo("Consulta Datos Clientes");
   eje=false;

   try  {
      if (ht != null)
     {
       if (ht.get("agente") != null)
         agente = ht.get("agente").toString();
       if (ht.get("agenteDef") != null)
         agenteDef = ht.get("agenteDef").toString();
       if (ht.get("supUsua") != null)
         supUsua = Boolean.parseBoolean(ht.get("supUsua").toString());
     }

     jbInit();
   }
   catch (Exception e) {
     setErrorInit(true);
   }
 }

  private void jbInit() throws Exception
  {
    iniciarFrame();
    this.setSize(732, 535);
    this.setVersion("2018-05-02");
    if (CONBD)
    {
      conecta();

      dtCli = new DatosTabla(ct);
//      dtCon1 = new DatosTabla(ct);
//      dtStat = new DatosTabla(ct);
      dtAlb = new DatosTabla(ct);
      dtCob= new DatosTabla(ct);
      dtBlo= new  DatosTabla(ct);
      if (! EU.getValorParam("jdbc_url_cont", "").equals(""))
      {  //Esta definida la conexion a bd contabilidad       
        try {
            conexion ctCont=new conexion(EU.getValorParam("jdbc_usu_cont", "sa")
              ,EU.getValorParam("jdbc_pass_cont", "as")
              ,EU.getValorParam("jdbc_driver_cont", "com.microsoft.sqlserver.jdbc.SQLServerDriver")
              ,EU.getValorParam("jdbc_url_cont", ""));
            dtCont= new DatosTabla(ctCont);
        } catch (SQLException k1)
        {
           msgBox("Error al conectar a base datos contabilidad");
           k1.printStackTrace();
           System.out.println("jdbc_url_cont: "+EU.getValorParam("jdbc_url_cont", ""));
               
           dtCont=null;            
        }
      }
    }

    statusBar = new StatusBar(this);

    Pdatcli.setLayout(null);
    Pdatcli.setMaximumSize(new Dimension(711, 140));
    Pdatcli.setMinimumSize(new Dimension(711, 140));
    Pdatcli.setPreferredSize(new Dimension(711, 140));
    Pdatcli.setDefButton(Bbuscar);
    Pdatcli.setButton(KeyEvent.VK_F4, Bbuscar);
    pCabCli.setButton(KeyEvent.VK_F4, Bbuscar);
    Pdatcli.setDefButtonDisable(false);
    Pdatcli.setBounds(new Rectangle(0, 20, 720, 125));
    avc_impalbE.setEnabled(false);
    avc_impcobE.setEnabled(false);
    cLabel7.setText("Fax");
    cLabel7.setBounds(new Rectangle(0, 60, 29, 18));
    cli_faxE.setBounds(new Rectangle(35, 60, 129, 17));
    cLabel8.setText("Zona");
    cLabel8.setBounds(new Rectangle(0, 80, 31, 18));
    zon_codiE.setAlignmentX((float) 0.5);
    zon_codiE.setBounds(new Rectangle(35, 80, 172, 19));

    zon_codiE.setAncTexto(30);
    cLabel9.setText("Repr.");
    cLabel9.setBounds(new Rectangle(210, 80, 34, 18));
    cli_codiE.iniciar(dtStat,this,vl,EU);
    cli_codiE.iniciar(jf);
    cli_codiE.setCliNomb(null);
    cli_codiE.setEnabled(true);
    rep_codiE.setAncTexto(30);
    rep_codiE.setBounds(new Rectangle(240, 80, 166, 18));
    cLabel10.setText("Activo");
    cLabel10.setBounds(new Rectangle(410, 80, 42, 16));

    cli_activE.setBounds(new Rectangle(465, 80, 53, 18));
    Vector v= new Vector();
    v.addElement("Producto"); // 0
    v.addElement("Nombre Producto"); // 1
    v.addElement("Cantidad"); // 2
    v.addElement("Unid."); // 3
    v.addElement("Precio"); // 4
    v.addElement("Importe"); // 5
    v.addElement("NL");
    jtAlb.setCabecera(v);
    jtAlb.setMaximumSize(new Dimension(712, 138));
    jtAlb.setMinimumSize(new Dimension(712, 138));
    jtAlb.setPreferredSize(new Dimension(712, 138));
    jtAlb.setAnchoColumna(new int[]{52,273,72,40,72,87,30});
    jtAlb.setAjustarGrid(true);
    jtAlb.setFormatoColumna(2,"---,--9.99");
    jtAlb.setFormatoColumna(3,"---9");
    jtAlb.setFormatoColumna(4,"---,--9.99");
    jtAlb.setFormatoColumna(5,"--,---,--9.99");
    jtAlb.setAlinearColumna(new int[]{0,0,2,2,2,2,2});
    jtAlb.setBounds(new Rectangle(0, 195, 710, 155));
    jtAlb.setConfigurar("gnu.chu.anjelica.consCliente",EU,dtStat);
    ArrayList v1=new ArrayList();
    v1.add("Emp"); // 0
    v1.add("Año"); // 1
    v1.add("Serie"); // 2
    v1.add("Numero"); // 3
    v1.add("Fec.Alb."); // 4
    v1.add("Imp.Alb."); // 5
    v1.add("Imp.Cob"); // 6
    v1.add("Imp.Pend"); // 7

    jtAco.setCabecera(v1);
    jtAco.setAnchoColumna(new int[]{33,28,30,56,75,82,83,83});
    jtAco.setFormatoColumna(5,"--,---,--9.99");
    jtAco.setFormatoColumna(6,"--,---,--9.99");
    jtAco.setFormatoColumna(7,"--,---,--9.99");
    jtAco.setAlinearColumna(new int[]{2,2,0,2,1,2,2,2});
    ArrayList v2 = new ArrayList();
    v2.add("Emp"); // 0
    v2.add("Año"); // 1
    v2.add("TE"); // 2
    v2.add("Numero"); // 3
    v2.add("Fec.Fra."); // 4
    v2.add("Imp.Pend."); // 5

    jtFco.setCabecera(v2);
    jtFco.setAnchoColumna(new int[]{33,58,30,86,75,82});
    jtFco.setFormatoColumna(5, "--,---,--9.99");
    jtFco.setAlinearColumna(new int[]{2,2,0,2,1,2});
    ArrayList v3 = new ArrayList();
    v3.add("Emp"); // 0
    v3.add("Año"); // 1
    v3.add("Serie"); // 2
    v3.add("Numero"); // 3
    v3.add("Fec.Vto."); // 4
    v3.add("Imp.Cobro."); // 5
    v3.add("Tipo Cob"); // 6

   jtTpe.setCabecera(v3);
   jtTpe.setAnchoColumna(new int[]{33,28,30,56,75,82,83});
   jtTpe.setFormatoColumna(5, "--,---,--9.99");
   jtTpe.setAlinearColumna(new int[]{2,2,0,2,1,2,0});

   ArrayList v4=new ArrayList();

   v4.add("Fec.Cobro");
   v4.add("Imp.Cobro");
   v4.add("Tipo Cobro");
   v4.add("Vto.Cobro");

   s="select c.cob_feccob,c.cob_impor,tpc_codi,cob_fecvto ";
   ArrayList v5=new ArrayList();
   cPanel1.setOpaque(true);
    cPanel1.setBounds(new Rectangle(0, 350, 360, 65));
    cgrid1.setBounds(new Rectangle(360, 350, 345, 65));
    Palbave.setBounds(new Rectangle(20, 165, 693, 27));
    navAlb.setBounds(new Rectangle(60, 145, 602, 18));
    navcli.setBounds(new Rectangle(116, 2, 481, 18));
    realPedi.setBounds(new Rectangle(600,2,18,18));
    opSoloCli.setSelectedIcon(null);
    opSoloCli.setText("Ver Solo Datos de Clientes");
    opSoloCli.setBounds(new Rectangle(173, 22, 179, 16));
    cLabel23.setText("Ver");
    cLabel23.setBounds(new Rectangle(225, 38, 25, 18));
    albfacE.setBounds(new Rectangle(259, 38, 96, 20));
    cLabel12.setToolTipText("");
    Bbusfra.setBounds(new Rectangle(658, 4, 33, 18));
    fvc_anoE.setBounds(new Rectangle(562, 3, 35, 16));
    
    cli_pobleE.setBounds(new Rectangle(60, 20, 227, 17));
    cLabel36.setBounds(new Rectangle(5, 20, 59, 16));
    cLabel36.setText("Pobl. Entr.");
    cLabel37.setText("Dir.Entr.");
    cLabel37.setBounds(new Rectangle(345, 45, 51, 14));
    cli_direeE.setBounds(new Rectangle(395, 40, 311, 17));
    cLabel115.setBounds(new Rectangle(2, 3, 79, 17));
    cLabel115.setToolTipText("");
    cLabel115.setText("Base Imp.");
    fvc_basimpE.setBounds(new Rectangle(82, 3, 80, 17));
    fvc_basimpE.setEnabled(false);
        pCabCli.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        pCabCli.setLayout(null);
        cli_nombL1.setText("Nombre Fiscal");
        cli_nombL1.setBounds(new Rectangle(230, 0, 85, 20));
        v5.add("Fec.Cobro");
   v5.add("Importe");
   v5.add("TC");
   v5.add("Fec.Vto");
   cgrid1.setCabecera(v5);
   cgrid1.setAnchoColumna(new int[]{75,80,120,75});
   cgrid1.setFormatoColumna(1, "--,---,--9.99");
   cgrid1.setAlinearColumna(new int[]{1,2,0,1});
   cgrid1.setAjustarGrid(true);
    cLabel11.setText("Albaran");
    cLabel11.setBounds(new Rectangle(61, 4, 49, 14));
    avc_serieE.setMayusc(true);
    avc_serieE.setAutoNext(true);
    avc_serieE.setBounds(new Rectangle(111, 4, 21, 14));
    avc_numeE.setBounds(new Rectangle(133, 4, 50, 14));
    cLabel12.setText("Fec.Alb./Fra.");
    cLabel12.setBounds(new Rectangle(226, 4, 75, 15));
    avc_fecalbE.setBounds(new Rectangle(297, 4, 72, 15));
    cLabel13.setText("Factura");
    cLabel13.setBounds(new Rectangle(519, 4, 46, 15));
    fvc_numeE.setBounds(new Rectangle(598, 4, 59, 15));
    cLabel14.setText("Usuario");
    cLabel14.setBounds(new Rectangle(373, 4, 48, 15));
    usu_nombE.setBounds(new Rectangle(416, 4, 100, 15));
    cLabel15.setText("Imp. Cobrado");
    cLabel15.setBounds(new Rectangle(1, 21, 79, 17));
    cLabel16.setText("Imp. Total");
    cLabel16.setBounds(new Rectangle(2, 42, 79, 17));

    avc_impalbE.setBounds(new Rectangle(82, 42, 80, 17));
    avc_impcobE.setBounds(new Rectangle(82, 21, 80, 17));
    Palbave.setBorder(BorderFactory.createLoweredBevelBorder());
    Palbave.setMaximumSize(new Dimension(680, 28));
    Palbave.setMinimumSize(new Dimension(680, 28));
    Palbave.setPreferredSize(new Dimension(680, 28));
    Palbave.setLayout(null);
    Bcancel.setBounds(new Rectangle(380, 100, 93, 22));
    Bcancel.setMargin(new Insets(0, 0, 0, 0));
    navcli.insBedit();
    cLabel35.setText("Tel.Cto.");
    cLabel35.setBounds(new Rectangle(175, 40, 45, 17));
    cli_telconE.setBounds(new Rectangle(215, 40, 129, 17));
    cPanel1.setBorder(BorderFactory.createEtchedBorder());
    cPanel1.setMaximumSize(new Dimension(359, 40));
    cPanel1.setMinimumSize(new Dimension(359, 40));
    cPanel1.setPreferredSize(new Dimension(359, 40));
    cPanel1.setText("cPanel1");
    cPanel1.setLayout(null);
    cgrid1.setMaximumSize(new Dimension(354, 96));
    cgrid1.setMinimumSize(new Dimension(354, 96));
    cgrid1.setPreferredSize(new Dimension(354, 96));
    cgrid1.setBuscarVisible(false);
    realPedi.setToolTipText("Realizar pedido a cliente");
    realPedi.setMaximumSize(new Dimension(18,18));
    realPedi.setMinimumSize(new Dimension(18,18));
    realPedi.setPreferredSize(new Dimension(18,18));
    navcli.setMaximumSize(new Dimension(478, 22));
    navcli.setMinimumSize(new Dimension(478, 22));
    navcli.setPreferredSize(new Dimension(478, 22));
    navAlb.setMaximumSize(new Dimension(596, 18));
    navAlb.setMinimumSize(new Dimension(596, 18));
    navAlb.setPreferredSize(new Dimension(596, 18));
    navcli.add(navcli.btnEdit,null);
    Pcobros.setLayout(borderLayout3);
    cPanel2.setBorder(BorderFactory.createLineBorder(Color.black));
    cPanel2.setOpaque(true);
    cPanel2.setPreferredSize(new Dimension(50, 45));
    cPanel2.setLayout(null);


    cLabel17.setText("Cdto. Concedido");
    cLabel17.setBounds(new Rectangle(4, 4, 94, 17));
    cdo_conceE.setText("");
    cdo_conceE.setBounds(new Rectangle(99, 4, 72, 17));
    cLabel18.setText("Forma Pago");
    cLabel18.setBounds(new Rectangle(195, 4, 71, 17));


    cLabel19.setText("Fras. Pend.");
    cLabel19.setBounds(new Rectangle(4, 3, 70, 17));  
    cob_frapeiE.setSonidoAutoNext(false);
    cob_frapeiE.setBounds(new Rectangle(72, 4, 93, 17));
    cob_frapeiE.setText("");
    cob_frapenE.setBackground(Color.CYAN);
    cob_frapenE.setForeground(Color.BLACK);
    cob_frapenE.setOpaque(true);
    cob_frapenE.setText("");
//    cTextField1.setBounds(new Rectangle(210, 35, 30, 18));
    cLabel110.setText("Alb. Pend.");
    cLabel110.setBounds(new Rectangle(9, 3, 61, 17));
    cbo_albpeiE.setText("");
    cbo_albpeiE.setSonidoAutoNext(false);
    cbo_albpeiE.setBounds(new Rectangle(337, 34, 101, 19));
    cob_frapenE.setText("");
    cob_frapenE.setBounds(new Rectangle(167, 4, 29, 17));
    cob_frapenE.setOpaque(true);
    cLabel111.setText("Talones Ctra");
    cLabel111.setBounds(new Rectangle(114, 4, 73, 17));

    cob_talcanE.setBackground(Color.YELLOW);
    cob_talcanE.setForeground(Color.BLACK);
    cob_talcanE.setOpaque(true);
    cob_talcanE.setText("");
    cob_talcanE.setBounds(new Rectangle(297, 4, 25, 17));
    cLabel112.setText("Devoluciones");
    cLabel112.setBounds(new Rectangle(17, 25, 80, 17));
    cob_devoiE.setBackground(Color.YELLOW);
    cob_devoiE.setDisabledTextColor(Color.BLACK);
    cob_devoiE.setText("0");
    cob_devoiE.setSonidoAutoNext(false);
    cob_devoiE.setBounds(new Rectangle(98, 23, 71, 17));
    cob_devonE.setText("");
    cob_devonE.setBounds(new Rectangle(172, 23, 30, 17));
    cob_devonE.setOpaque(true);
    cob_devonE.setForeground(Color.BLACK);
    cob_devonE.setBackground(Color.YELLOW);
    cLabel20.setText("Riesgo Total");
    cLabel20.setBounds(new Rectangle(209, 23, 75, 17));

    cob_riestotE.setBackground(Color.blue);
    cob_riestotE.setForeground(Color.white);
    cob_riestotE.setCaretColor(Color.white);
    cob_riestotE.setCaretPosition(0);
    cob_riestotE.setDisabledTextColor(Color.white);
    cob_riestotE.setText("aaa");
    cob_riestotE.setBounds(new Rectangle(282, 23, 71, 17));
//    cTextField1.setBounds(new Rectangle(336, 60, 119, 18));

    fpa_nombE.setText("");
    fpa_nombE.setBounds(new Rectangle(265, 4, 414, 17));
    cob_albpenE.setBackground(Color.CYAN);
    cob_albpenE.setForeground(Color.WHITE);
    cob_albpenE.setOpaque(true);
    cob_albpenE.setText("");
    cob_albpenE.setText("");
    cob_albpenE.setBounds(new Rectangle(171, 3, 29, 17));
    cob_albpenE.setOpaque(true);
    cob_albpeiE.setText("");
    cob_albpeiE.setSonidoAutoNext(false);
    cob_albpeiE.setBounds(new Rectangle(72, 3, 93, 17));
    cLabel21.setText("Credito Libre");
    cLabel21.setBounds(new Rectangle(358, 23, 76, 17));
    cob_crelibE.setBackground(Color.blue);
    cob_crelibE.setForeground(Color.white);
    cob_crelibE.setDisabledTextColor(Color.white);
    cob_crelibE.setColumns(0);
    cob_crelibE.setText("ss");
    cob_crelibE.setBounds(new Rectangle(434, 23, 71, 17));
    cLabel22.setText("Cdto.Sobrepasado");
    cLabel22.setBounds(new Rectangle(508, 23, 107, 17));
    cob_cresobE.setBackground(Color.blue);
    cob_cresobE.setForeground(Color.white);
    cob_cresobE.setDisabledTextColor(Color.white);
    cob_cresobE.setBounds(new Rectangle(619, 23, 71, 17));
    jtAco.setPreferredSize(new Dimension(400,5));
    jtAco.setMinimumSize(new Dimension(400,5));
    jtAco.setMaximumSize(new Dimension(400, 5));
   
    jtFco.setPreferredSize(new Dimension(400,5));
    jtFco.setMinimumSize(new Dimension(400,5));
    jtFco.setMaximumSize(new Dimension(400, 5));
    jtTpe.setPreferredSize(new Dimension(400,5));      
    jtTpe.setMinimumSize(new Dimension(400,5));
    jtTpe.setMaximumSize(new Dimension(400, 5));
        
    cPanel3.setBorder(BorderFactory.createRaisedBevelBorder());
    cPanel3.setPreferredSize(new Dimension(580, 104));

    cPanel3.setLayout(gridBagLayout1);
    cPanel4.setBorder(BorderFactory.createRaisedBevelBorder());
    cPanel4.setMaximumSize(new Dimension(2147483647, 2147483647));
    cPanel4.setMinimumSize(new Dimension(180, 32));
    cPanel4.setPreferredSize(new Dimension(200, 24));

    cPanel4.setLayout(null);
    cPanel5.setBorder(BorderFactory.createRaisedBevelBorder());
    cPanel5.setMinimumSize(new Dimension(180, 24));
    cPanel5.setPreferredSize(new Dimension(200, 24));
    cPanel5.setLayout(null);

    cLabel113.setText("Devoluciones");
    cLabel113.setBounds(new Rectangle(217, 3, 80, 17));
    cob_devanE.setBackground(Color.CYAN);
    cob_devanE.setForeground(Color.BLACK);
    cob_devanE.setOpaque(true);
    cob_devanE.setBounds(new Rectangle(410, 3, 30, 17));

    cob_devaiE.setDisabledTextColor(Color.BLACK);
    cob_devaiE.setBounds(new Rectangle(304, 3, 101, 17));
    cob_devaiE.setBackground(Color.CYAN);
    cLabel114.setText("Devoluciones");
    cLabel114.setBounds(new Rectangle(211, 2, 80, 17));
    cob_devfnE.setBackground(Color.CYAN);
    cob_devfnE.setForeground(Color.BLACK);

    cob_devfnE.setBounds(new Rectangle(404, 2, 30, 17));
    
    cob_devfiE.setBounds(new Rectangle(297, 2, 101, 17));
    cob_devfiE.setText("");
    cob_devfiE.setDisabledTextColor(Color.BLACK);
    cob_devfiE.setBackground(Color.CYAN);
    cPanel6.setBorder(BorderFactory.createRaisedBevelBorder());
    cPanel6.setMinimumSize(new Dimension(180, 24));
    cPanel6.setPreferredSize(new Dimension(180, 24));
    cPanel6.setEnabled(true);
    cPanel6.setLayout(null);

    cob_talcaiE.setBounds(new Rectangle(192, 4, 101, 17));
    cPanel8.setDefButton(Bacezon);
    cPanel8.setLayout(null);
    cLabel24.setText("Cliente");
    cLabel24.setBounds(new Rectangle(4, 45, 45, 19));
    cLabel25.setText("Ponerle Zona");
    cLabel25.setBounds(new Rectangle(102, 6, 76, 17));
    zon_codiE1.setAncTexto(30);
    zon_codiE1.setBounds(new Rectangle(186, 7, 282, 20));
    zon_codiE1.setAlignmentX((float) 0.5);

    cli_cod1E.setBounds(new Rectangle(57, 46, 67, 19));

    cli_nombL.setBackground(Color.orange);
    cli_nombL.setForeground(Color.white);
    cli_nombL.setDoubleBuffered(false);
    cli_nombL.setOpaque(true);
    cli_nombL.setBounds(new Rectangle(132, 45, 443, 19));
    Bacezon.setBounds(new Rectangle(202, 107, 94, 27));
    Bacezon.setText("Aceptar");

    zon_codiE2.setAlignmentX((float) 0.5);
    zon_codiE2.setEnabled(false);
    zon_codiE2.setBounds(new Rectangle(131, 68, 318, 18));
    zon_codiE2.setAncTexto(30);
    cLabel26.setText("Zona actual");
    cLabel26.setBounds(new Rectangle(54, 68, 70, 17));
    Pcomen.setLayout(null);
        cli_nombL.setBounds(new Rectangle(165, 5, 332, 19));

        //    cli_codiL1.setHorizontalAlignment(SwingConstants.TRAILING);

        jScrollPane1.setBounds(new Rectangle(7, 218, 694, 164));

    cLabel28.setVerifyInputWhenFocusTarget(true);
    cLabel28.setText("Fecha");
    cLabel28.setBounds(new Rectangle(265, 5, 40, 16));
    cpv_fechaE.setBounds(new Rectangle(300, 5, 73, 16));
    cpv_fechaE.setEnabled(false);



    Bmodcom.setBounds(new Rectangle(257, 388, 105, 24));
    Bmodcom.setText("Modificar");



    Pvenpro.setLayout(null);
    cLabel29.setToolTipText("");
    cLabel29.setText("De fecha");
    cLabel29.setBounds(new Rectangle(195, 4, 51, 18));

    feinvpE.setBounds(new Rectangle(254, 4, 76, 20));
    fefivpE.setBounds(new Rectangle(354, 4, 76, 20));

    cLabel210.setBounds(new Rectangle(335, 4, 15, 20));
    cLabel210.setText("A");

    Vector v6 = new Vector();
    v6.addElement("Referencia"); // 0
    v6.addElement("Descripcion"); // 1
    v6.addElement("Fe.Ul.Ve"); // 2
    v6.addElement("Ult.Precio"); // 3
    v6.addElement("Comentario"); // 4
    jtcopr.setCabecera(v6);
    jtcopr.setAnchoColumna(new int[]{64,172,73,62,288});
    jtcopr.setFormatoColumna(3,"---,--9.99");
    jtcopr.setAlinearColumna(new int[]{0,0,1,2,0});

    jtcopr.setBounds(new Rectangle(9, 28, 688, 178));
    jScrollPane2.setBounds(new Rectangle(8, 235, 690, 143));

    Bmocopr.setBounds(new Rectangle(213, 383, 89, 25));
    Bmocopr.setMargin(new Insets(0, 0, 0, 0));
    Bmocopr.setMnemonic('M');
    Bmocopr.setText("Modificar");

    Pvenpro.setDefButton(Bmocopr);
    Bincopr.setBounds(new Rectangle(343, 383, 89, 25));
    Bincopr.setMnemonic('I');
    Bincopr.setText("Insertar");

    Bbocopr.setBounds(new Rectangle(469, 383, 89, 25));
    Bbocopr.setMnemonic('B');
    Bbocopr.setText("Borrar");
    cLabel30.setText("Producto");
    cLabel30.setBounds(new Rectangle(11, 209, 55, 18));

    pro_codiE.setBounds(new Rectangle(70, 208, 70, 19));
    pro_nombL.setBackground(Color.yellow);
    pro_nombL.setForeground(Color.black);
    pro_nombL.setOpaque(true);
    pro_nombL.setBounds(new Rectangle(147, 208, 548, 21));
    Bbusalb.setBounds(new Rectangle(184, 4, 33, 18));
    cPanel9.setBorder(BorderFactory.createRaisedBevelBorder());
    cPanel9.setDefButton(Bbusalb);
    cPanel9.setDefButtonDisable(false);
    cPanel9.setBounds(new Rectangle(4, 3, 221, 24));
    cPanel9.setLayout(null);
    emp_codiE.setBounds(new Rectangle(36, 3, 20, 15));
    emp_codiE.setValorDec(EU.em_cod);
    cLabel32.setText("Emp");
    cLabel32.setBounds(new Rectangle(5, 3, 28, 14));
    opAgrlin.setSelected(true);
    opAgrlin.setText("Agr. Lineas");
    opAgrlin.setBounds(new Rectangle(163, 2, 101, 18));
    exceCredL.setBackground(SystemColor.activeCaption);
    exceCredL.setFont(new Font("Dialog", 1, 13));
    exceCredL.setForeground(SystemColor.info);
    exceCredL.setBorder(null);
    exceCredL.setMaximumSize(new Dimension(125, 16));
    exceCredL.setOpaque(true);
    exceCredL.setToolTipText("");
    exceCredL.setHorizontalAlignment(SwingConstants.CENTER);
    exceCredL.setHorizontalTextPosition(SwingConstants.TRAILING);
    exceCredL.setText("CREDITO EXCEDIDO");
    exceFechaL.setBounds(new Rectangle(5, 100, 265, 22));
    exceFechaL.setBackground(Color.red);
    exceFechaL.setFont(new Font("Dialog", 1, 12));
    exceFechaL.setForeground(Color.yellow);
    exceFechaL.setOpaque(true);
    exceFechaL.setHorizontalAlignment(SwingConstants.CENTER);
    exceFechaL.setText("Fact. con + 30 Dias");
    exceCredL.setBounds(new Rectangle(475, 100, 230, 22));
    Blote.setBounds(new Rectangle(266, 2, 90, 18));
    Blote.setMargin(new Insets(0, 0, 0, 0));
    Blote.setMnemonic('0');
    Blote.setText("Ver Lote (F2)");
    Bbuscar.setMargin(new Insets(0, 0, 0, 0));
   
    cli_servirL.setBounds(new Rectangle(530, 80, 61, 16));
    cli_servirE.setBounds(new Rectangle(600, 80, 50, 15));
    cLabel34.setText("Coment");
    cLabel34.setBounds(new Rectangle(415, 60, 47, 14));
    cli_comenE.setBounds(new Rectangle(455, 60, 247, 17));
    ArrayList vCom=new ArrayList();
    vCom.add("Fecha");
    vCom.add("Comentario");
    jtCom.setCabecera(vCom);
    jtCom.setAlinearColumna(0,1);
     jtCom.setAnchoColumna(new int[]{63,607});
    jtCom.setBounds(new Rectangle(5, 27, 698, 189));
        Pdatcli.add(cli_nombL1, null);
        Pdatcli.add(exceCredL, null);
        Pdatcli.add(exceFechaL, null);
        Pdatcli.add(Bbuscar, null);
        Pdatcli.add(Bcancel, null);
        Pdatcli.add(cLabel4, null);
        Pdatcli.add(cli_direE, null);
        Pdatcli.add(cLabel5, null);
        Pdatcli.add(cli_telefE, null);
        Pdatcli.add(cLabel35, null);
        Pdatcli.add(cli_telconE, null);
        Pdatcli.add(cLabel7, null);
        Pdatcli.add(cli_faxE, null);
        Pdatcli.add(cli_activE, null);
        Pdatcli.add(cLabel8, null);
        Pdatcli.add(zon_codiE, null);
        Pdatcli.add(cLabel9, null);
        Pdatcli.add(rep_codiE, null);
        Pdatcli.add(cLabel10, null);
        Pdatcli.add(cli_servirL, null);
        Pdatcli.add(cli_servirE, null);
        Pdatcli.add(cli_comenE, null);
        Pdatcli.add(cli_pobleE, null);
        Pdatcli.add(cLabel36, null);
        Pdatcli.add(cLabel6, null);
        Pdatcli.add(cli_perconE, null);
        Pdatcli.add(cLabel34, null);
        Pdatcli.add(cLabel37, null);
        Pdatcli.add(cli_direeE, null);
        Pdatcli.add(cli_codiE, null);
        Pdatcli.add(cLabel1, null);
        Pdatcli.add(cli_nomcoE, null);

        Pcliente.add(cgrid1, null);
        Pcliente.add(cPanel1, null);
        Pcliente.add(navAlb, null);
        Pcliente.add(Palbave, null);
        Pcliente.add(jtAlb, null);
        Pprinc.setLayout(gridBagLayout2);
        Pcliente.setLayout(null);
        pGraf.setLayout(new GridBagLayout());
        pGrSouth.setLayout(new BorderLayout());
        pGrSouth.setLayout(new BorderLayout());
        pGrNorth.setPreferredSize(new Dimension(70, 200));
        pGrSouth.setPreferredSize(new Dimension(70, 200));
        pGraf.add(pGrNorth,new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.CENTER,
                GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));
        pGraf.add(pGrSouth,new GridBagConstraints(0,1,1,1,1,1,GridBagConstraints.CENTER,
                GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));
        cLabel1.setText("Codigo Cliente");
        cLabel1.setBounds(new Rectangle(5, 0, 85, 15));

        cli_codiE.setBounds(new Rectangle(90, 0, 60, 18));
        
        cli_codiE.setSize(new Dimension(70, 18));
        cli_codiE.setPreferredSize(new Dimension(70, 18));
        cli_codiE.setMinimumSize(new Dimension(70, 18));
        cli_nomcoE.setBounds(new Rectangle(325, 0, 385, 18));
        cli_nombE.setBounds(new Rectangle(50, 1, 390, 18));
        cli_codrepE.setMayusculas(true);
        cli_codrepE.setBounds(new Rectangle(442, 1, 40, 18));
        cLabel2.setText("Cliente");
        cLabel2.setBounds(new Rectangle(1, 1, 55, 18));
        cLabel2.setSize(new Dimension(55, 18));

        cli_poblE.setBounds(new Rectangle(485, 1, 230, 18));
        cLabel4.setText("Direccion");
        cLabel4.setBounds(new Rectangle(330, 20, 59, 19));

        cli_direE.setBounds(new Rectangle(390, 20, 316, 17));
        cLabel5.setText("Telef.");
        cLabel5.setBounds(new Rectangle(0, 40, 32, 15));

        cli_telefE.setBounds(new Rectangle(35, 40, 129, 17));
        cLabel6.setToolTipText("");
        cLabel6.setText(" Contacto");
        cLabel6.setBounds(new Rectangle(160, 60, 53, 17));

        cli_perconE.setBounds(new Rectangle(215, 60, 199, 17));
        Bbuscar.setBounds(new Rectangle(275, 100, 98, 22));
        Pdatcli.setBorder(BorderFactory.createRaisedBevelBorder());
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        this.getContentPane().add(Pprinc, BorderLayout.CENTER);
        Pprinc.add(TPanel,
                   new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                                          GridBagConstraints.BOTH,
                                          new Insets(0, 0, 0, 0), 0, 0));
        pCabCli.add(cLabel2, null);
        pCabCli.add(cli_poblE, null);
        pCabCli.add(cli_nombE, null);
        pCabCli.add(cli_codrepE, null);
        Pprinc.add(pCabCli, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 0), 0, 20));
        cPanel9.add(Bbusalb, null);
        cPanel9.add(avc_numeE, null);
        cPanel9.add(avc_serieE, null);
        cPanel9.add(emp_codiE, null);
        cPanel9.add(cLabel32, null);
        cPanel9.add(cLabel11, null);
        Palbave.add(cLabel13, null);
        Palbave.add(fvc_anoE, null);
        Palbave.add(fvc_numeE, null);
        Palbave.add(Bbusfra, null);
        Palbave.add(cLabel12, null);
        Palbave.add(avc_fecalbE, null);
        Palbave.add(cLabel14, null);
        Palbave.add(usu_nombE, null);
        Palbave.add(cPanel9, null);
        cPanel1.add(Blote, null);
        cPanel1.add(opAgrlin, null);
        cPanel1.add(avc_impcobE, null);
        cPanel1.add(cLabel15, null);
        cPanel1.add(albfacE, null);
        cPanel1.add(opSoloCli, null);
        cPanel1.add(cLabel115, null);
        cPanel1.add(fvc_basimpE, null);
        cPanel1.add(avc_impalbE, null);
        cPanel1.add(cLabel16, null);


        cPanel1.add(cLabel23, null);
        TPanel.add(Pcliente, "Cliente");
        TPanel.add(Pcobros, "Cobros Pend");
        TPanel.add(pRelClien, "Relacion");
        TPanel.add(pGraf,"Grafico");
        cPanel2.add(cLabel17, null);
        cPanel2.add(cdo_conceE, null);
        cPanel2.add(cLabel18, null);
        cPanel2.add(fpa_nombE, null);
        cPanel2.add(cLabel112, null);
        cPanel2.add(cob_devoiE, null);
        cPanel2.add(cob_devonE, null);
        cPanel2.add(cLabel20, null);
        cPanel2.add(cob_riestotE, null);
        cPanel2.add(cLabel21, null);
        cPanel2.add(cob_crelibE, null);
        cPanel2.add(cob_cresobE, null);
        cPanel2.add(cLabel22, null);
        TPanel.add(Pcomen, "Comentarios");
        Pcomen.add(jScrollPane1, null);
        Pcomen.add(jtCom, null);
        Pcomen.add(cLabel28, null);
        Pcomen.add(cpv_fechaE, null);
        Pcomen.add(Bmodcom, null);
        TPanel.add(Pvenpro, "Com.Prod.");
        Pvenpro.add(cLabel29, null);
        Pvenpro.add(feinvpE, null);
        Pvenpro.add(fefivpE, null);
        Pvenpro.add(jtcopr, null);
        Pvenpro.add(jScrollPane2, null);
        Pvenpro.add(cLabel210, null);
        Pvenpro.add(Bbocopr, null);
        Pvenpro.add(Bmocopr, null);
        Pvenpro.add(Bincopr, null);
        Pvenpro.add(cLabel30, null);
        Pvenpro.add(pro_codiE, null);
        Pvenpro.add(pro_nombL, null);
        jScrollPane2.getViewport().add(cpc_comeE, null);
        jScrollPane1.getViewport().add(cpv_textE, null);

        //    TPanel.add(cPanel8, "Asig.Zona");
        cPanel8.add(cLabel24, null);
        cPanel8.add(cli_cod1E, null);
        cPanel8.add(cli_nombL, null);
        cPanel8.add(zon_codiE1, null);
        cPanel8.add(cLabel25, null);
        cPanel8.add(Bacezon, null);
        cPanel8.add(zon_codiE2, null);
        cPanel8.add(cLabel26, null);
        Pcobros.add(cPanel3, BorderLayout.CENTER);
        cPanel3.add(jtFco,
                    new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.EAST,
                                           GridBagConstraints.BOTH,
                                           new Insets(0, 0, 0, 0), 0, 0));
        cPanel3.add(jtTpe,
                    new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.EAST,
                                           GridBagConstraints.BOTH,
                                           new Insets(0, 0, 0, 0), 0, 0));
        jtTpe.add(cPanel6, BorderLayout.SOUTH);
        cPanel6.add(cLabel111, null);
        cPanel6.add(cob_talcanE, null);
        cPanel6.add(cob_talcaiE, null);
        cPanel3.add(jtAco,
                    new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTH,
                                           GridBagConstraints.BOTH,
                                           new Insets(0, 0, 0, 0), 0, 0));
        jtAco.add(cPanel4,  BorderLayout.SOUTH);
        Pcobros.add(cPanel2, BorderLayout.NORTH);
        cPanel5.add(cLabel19, null);
        cPanel5.add(cob_frapeiE, null);
        cPanel5.add(cob_frapenE, null);
        cPanel5.add(cob_devfnE, null);
        cPanel5.add(cLabel114, null);
        cPanel5.add(cob_devfiE, null);
        cPanel4.add(cLabel110, null);
        cPanel4.add(cob_albpeiE, null);
        cPanel4.add(cob_albpenE, null);
        cPanel4.add(cLabel113, null);
        cPanel4.add(cob_devaiE, null);
        cPanel4.add(cob_devanE, null);
        jtFco.add(cPanel5, BorderLayout.SOUTH);
        Pcliente.add(navcli, null);
        Pcliente.add(realPedi);
        Pcliente.add(Pdatcli, null);
    }

    @Override
 public void iniciarVentana() throws Exception
 {

   jtAlb.setButton(KeyEvent.VK_F2,Blote);
   pRelClien.iniciar(this,dtStat, EU);
   cli_codiE.setColumnaAlias("cli_codi");
   cli_nombE.setMayusc(true);
   cli_nombE.setColumnaAlias("upper(cli_nomb)");
   cli_codrepE.setColumnaAlias("cli_codrut");
   cli_nomcoE.setMayusc(true);
   cli_nomcoE.setColumnaAlias("upper(cli_nomco)");

   cli_direE.setMayusc(true);
   cli_direE.setColumnaAlias("upper(cli_direc)");
   cli_direeE.setMayusc(true);
   cli_direeE.setColumnaAlias("upper(cli_diree)");
   cli_poblE.setMayusc(true);
   cli_poblE.setColumnaAlias("upper(cli_pobl)");
   cli_pobleE.setMayusc(true);
   cli_pobleE.setColumnaAlias("upper(cli_poble)");

   zon_codiE.setColumnaAlias("c.zon_codi");
   rep_codiE.setColumnaAlias("rep_codi");
   if (agente!=null)
   {
       rep_codiE.setEnabled(false);
       rep_codiE.setText(agente);
   }
   rep_codiE.getComboBox().setPreferredSize(new Dimension(200,18));
   zon_codiE.getComboBox().setPreferredSize(new Dimension(200,18));
   cli_activE.setColumnaAlias("cli_activ");
   pdclien.iniciarServir(cli_servirE);
   cli_servirE.setColumnaAlias("cli_servir");
   cli_comenE.setMayusc(true);
   cli_comenE.setColumnaAlias("upper(cli_comen)");

   albfacE.addItem("Albaran","A");
   albfacE.addItem("Factura","F");

   if (CONBD)
   {
            pdconfig.llenaDiscr(dtCon1,zon_codiE,"Cz",EU.em_cod);
            pdconfig.llenaDiscr(dtCon1,zon_codiE1,"Cz",EU.em_cod);
            pdconfig.llenaDiscr(dtCon1,zon_codiE2,"Cz",EU.em_cod);
            MantRepres.llenaLinkBox(rep_codiE, dtCon1);
   }
//   rep_codiE.getComboModel().avisaAdd();
   cli_activE.setColumnaAlias("cli_activ");
   cli_activE.addItem("Si","S");
   cli_activE.addItem("No","N");

   activarEventos();
   statusBar.setEnabled(true);
   this.setEnabled(true);
   avc_serieE.setEnabled(true);
   avc_numeE.setEnabled(true);
   avc_fecalbE.setEnabled(false);
   usu_nombE.setEnabled(false);
//   fvc_numeE.setEnabled(false);
   cPanel2.setEnabled(false);
   cob_devfiE.setEnabled(false);
   cob_devfnE.setEnabled(false);
   cob_devaiE.setEnabled(false);
   cob_devanE.setEnabled(false);
   cob_albpeiE.setEnabled(false);
   cob_albpenE.setEnabled(false);
   cob_frapeiE.setEnabled(false);
   cob_frapenE.setEnabled(false);
   cob_talcaiE.setEnabled(false);
   cob_talcanE.setEnabled(false);
   cpv_fechaE.setEnabled(false);
   feinvpE.setEnabled(false);
   fefivpE.setEnabled(false);
   PADQuery();
   limpiaCobros();
   limpiaComen();
   limpiaComPro();
   statusBar.setEnabled(true);
 }
 void buscaCli()
 {
   mensaje("Introduzca Condiciones de Busqueda");
   navcli.setEnabled(navegador.TODOS,false);
   statusBar.setEnabled(false);
   Pdatcli.setEnabled(true);
   pCabCli.setEnabled(true);
   cli_codiE.setError(false);
   Pdatcli.setQuery(true);
   pCabCli.setQuery(true);
   cli_codiE.setEnabled(true);
//   zon_codiE.combo.setEnabled(true);
//   rep_codiE.combo.setEnabled(true);
   Pdatcli.resetTexto();
   pCabCli.resetTexto();
   cli_activE.setValor("S");
   cli_nombE.requestFocus();
   if (agenteDef!=null)
       rep_codiE.setText(agenteDef);
   if (agente!=null)
         rep_codiE.setText(agente);
 }
 void cambioFraAlb()
 {
   try {
     verAlbFra(dtCli);
   } catch (Exception k)
   {
     Error("Error al ver Albaranes/Facturas",k);
   }
 }

 void activarEventos()
 {
     realPedi.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          try{
            if (cli_codiE.isNull())
                return;
             ejecutable prog;
             if ((prog=jf.gestor.getProceso(pdpeve.getNombreClase()))==null)
                       return;
                    pdpeve cm=(pdpeve) prog;
                    if (cm.inTransation())
                    {
                       msgBox("Mantenimiento Pedidos de Ventas ocupado. No se puede realizar el Alta");
                       return;
                    }
                    cm.nav.setPulsado(navegador.ADDNEW);
                    cm.nav.setEnabled(navegador.TODOS, false);
                    cm.PADAddNew();
                    cm.setExterno(true);
                    cm.setCliente(cli_codiE.getValorInt());
                    cm.leerDatosCliente();
                    cm.irGrid();
                    jf.gestor.ir(cm);
          }catch (Exception k)
          {
            Error("Error al cambiar formato vista albaran ",k);
          }
        }
     });
     TPanel.addChangeListener(new ChangeListener() {
        public void stateChanged(ChangeEvent e) {
            if ( swActCli)
            {
                swActCli=false;
                try {
                    pRelClien.getDatosCliente(dtCli);
                } catch (SQLException k)
                {
                    Error("Error al buscar cliente",k);
                }
                setRelacionClientes(false);
                verDatClien(dtCli);   
            }            
        }
    });
   albfacE.addActionListener(new ActionListener() {
     public void actionPerformed(ActionEvent e) {
       if (navcli.pulsado==navegador.QUERY || swBusDat)
         return;
       cambioFraAlb();
     }
   });
   Bbusalb.addActionListener(new ActionListener() {
     public void actionPerformed(ActionEvent e) {
         buscAlb();
     }
   });
   Bbusfra.addActionListener(new ActionListener() {
     public void actionPerformed(ActionEvent e) {
         buscFra();
     }
   });

  opSoloCli.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
      try {
        if (navcli.pulsado==navegador.QUERY)
          return;
        if (!opSoloCli.isSelected())
          verDatCli(dtCli, true);
      } catch (Exception k)
      {
        Error("Error Al ver Datos Cliente",k);
      }
    }
  });
   Bcancel.addActionListener(new ActionListener() {
     public void actionPerformed(ActionEvent e) {
       if (navcli.pulsado==navegador.QUERY)
       {
         try {
           mensaje("");
           Pdatcli.setQuery(false);
           Pdatcli.setEnabled(false);
           pCabCli.setQuery(false);
           pCabCli.setEnabled(false);
           statusBar.setEnabled(true);
           navcli.setEnabled(navegador.TODOS, true);
           verDatClien(dtCli);
           activar(false);
           mensajeErr("Consulta cancelada ...");
         } catch (Exception k)
         {
           Error("Error al ver datos Albaran",k);
         }
       }
       else
       {
         canc_edit();
       }
       navcli.pulsado=navegador.NINGUNO;
     }
   });
   opAgrlin.addActionListener(new ActionListener() {
     public void actionPerformed(ActionEvent e) {
       try{
         verDatAlbaran(dtAlb);
       }catch (Exception k)
       {
         Error("Error al cambiar formato vista albaran ",k);
       }
     }
   });
   pro_codiE.addFocusListener(new FocusAdapter() {
            @Override
     public void focusLost(FocusEvent e) {
       try
       {
         s="SELECT pro_nomb FROM v_articulo WHERE pro_codi = "+pro_codiE.getValorInt();
         if (dtCon1.select(s))
           pro_nombL.setText(dtCon1.getString("pro_nomb"));
         else
           pro_nombL.setText("");
       } catch (Exception k)
       {
         fatalError("Error al buscar nombre de producto");
       }
     }
   });
   Bincopr.addActionListener(new ActionListener()
     {
       public void actionPerformed(ActionEvent e)
       {
         Bincopr_actionPerformed(e);
       }
     });
     Bbocopr.addActionListener(new ActionListener()
          {
            public void actionPerformed(ActionEvent e)
            {
              Bbocopr_actionPerformed(e);
            }
          });

   Bmodcom.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          Bmodcom_actionPerformed(e);
        }
      });
      Bmocopr.addActionListener(new ActionListener()
            {
              public void actionPerformed(ActionEvent e)
              {
                Bmocopr_actionPerformed(cli_codiE.getValorInt(), jtcopr.getValorInt(0));
              }
            });

       Bbuscar.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
           if (navcli.pulsado == navegador.QUERY)
           {
             if (! Bbuscar_actionPerformed(e))
                 return;
           }
           else
             ej_edit();
           navcli.pulsado=navegador.NINGUNO;
         }
       });
       cli_cod1E.addFocusListener(new FocusAdapter()
       {
         public void actionPerformed(ActionEvent e)
         {
           Bacezon_actionPerformed(e);
         }

       });
       Bacezon.addActionListener(new ActionListener()
       {
         public void actionPerformed(ActionEvent e)
         {
           Bacezon_actionPerformed(e);
         }
       });
       jtAlb.addMouseListener(new MouseAdapter()
       {
            @Override
         public void mouseClicked(MouseEvent e) {
           if (jtAlb.isVacio())
             return;
           if (e.getClickCount()<2)
             return;

           busVenProd();
         }
       });
       Blote.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
//           if (opAgrlin.isSelected())
//           {
//             mensajeErr("No puede consultar los lotes sobre lineas agrupadas");
//             return;
//           }
           busLoteProd();
         }
       });
       jtAlb.setAltButton(Blote);

       jtCom.tableView.getSelectionModel().addListSelectionListener(new
           ListSelectionListener()
       {
         public void valueChanged(ListSelectionEvent e)
         {
           if (e.getValueIsAdjusting()) // && e.getFirstIndex() == e.getLastIndex())
             return;
           if (!isEnabled())
             return;
           cpv_textE.setText(jtCom.getValString(1));
         }
       });
    jtcopr.tableView.getSelectionModel().addListSelectionListener(new
            ListSelectionListener()
     {
        public void valueChanged(ListSelectionEvent e)
        {
         if ( e.getValueIsAdjusting())// && e.getFirstIndex() == e.getLastIndex())
           return;
         if (! isEnabled())
           return;
         cpc_comeE.setText(jtcopr.getValString(4));
       }
     });



 }
 void busLoteProd()
 {
   if (jtAlb.getValString(0,true).equals(""))
     return;

   if (ayLoPr == null)
   {
     ayLoPr = new lotVenPro(this)
     {
         public void matar()
         {
             salirAyuLote();
         }
     };
     this.getLayeredPane().add(ayLoPr,new Integer(1));
     ayLoPr.setLocation(50, 40);
   }
   else
     ayLoPr.setIniciado(false);
   try
   {
     boolean swEnc = false;
     int p=0;
     String serie="";
     int  numAlb=0;
     if (albfacE.getValor().equals("F"))
     {
       for (int n=jtAlb.getSelectedRow();n>=0;n--)
       {
         if (jtAlb.getValString(n,0,true).equals(""))
         { // Cabecera de Albaran
           s=jtAlb.getValString(n,1,true);
           serie=s.substring(5,6);
           numAlb=Integer.parseInt(s.substring(7,13));
           break;
         }
       }
     }
     else
     {
       serie = dtAlb.getString("avc_serie");
       numAlb = dtAlb.getInt("avc_nume",true);
     }
     if (!opAgrlin.isSelected())
       ayLoPr.cargaDatos(ct, jtAlb.getValString(0), jtAlb.getValString(1),
                         albfacE.getValor().equals("A")?dtAlb.getInt("avc_ano",true):dtAlb.getInt("fvc_ano",true),
                       dtAlb.getInt("emp_codi",true),serie,
                       numAlb,jtAlb.getValorInt("NL"),jtAlb.getValorDec(4));
     else
       ayLoPr.cargaDatos(ct, jtAlb.getValString(0), jtAlb.getValString(1),
                albfacE.getValor().equals("A")?dtAlb.getInt("avc_ano",true):dtAlb.getInt("fvc_ano",true),
                dtAlb.getInt("emp_codi",true),serie,
                numAlb,-1,jtAlb.getValorDec(4));

//     int nEl = vl.getComponentCount();
//     for (int n = 0; n < nEl; n++)
//     {
//       if (vl.getComponent(n) == ayLoPr)
//         swEnc = true;
//     }
//     if (!swEnc)
//       vl.add(ayLoPr);
     ayLoPr.setVisible(true);
     ayLoPr.setSelected(true);
     setFoco(ayLoPr);
     ayLoPr.setIniciado(true);
   }
   catch (Exception ex)
   {
     fatalError("Error al Cargar datos de productos", ex);
   }
 }
 void salirAyuLote()
 {
    ayLoPr.setVisible(false);
    try 
    {
         this.setSelected(true);
    } catch (Exception k){}
    setFoco(null);
    this.toFront();
    jtAlb.requestFocus();
 }
 void busVenProd()
 {
  if (ayVePr==null)
  {
    ayVePr = new ayuVenPro();
    vl.add(ayVePr);
    ayVePr.setLocation(0, 0);
  }
  try
  {
    boolean swEnc=false;
    ayVePr.cargaDatos(ct, cli_codiE.getText(),cli_nombE.getText(), jtAlb.getValString(0),jtAlb.getValString(1),EU);
    int nEl=vl.getComponentCount();
    for (int n=0;n<nEl;n++)
    {
      if (vl.getComponent(n)==ayVePr)
        swEnc=true;
    }
    if (!swEnc)
      vl.add(ayVePr);
    ayVePr.muerto=false;
    ayVePr.setClosed(false);
    ayVePr.setVisible(true);
  }
  catch (Exception ex)
  {
    fatalError("Error al Cargar datos de productos",ex);
  }
 }
  boolean Bbuscar_actionPerformed(ActionEvent e) {
    if (Pdatcli.getErrorConf()!=null)
    {
      mensajeErr("Error en condiciones de busqueda");
      Pdatcli.getErrorConf().requestFocus();
      return false;
    }
    if (relClien)
        mensaje("Buscando datos de cliente ... ");

//    msgBox("en Buscar");
    ArrayList v=new ArrayList();
    v.add(cli_codiE.getStrQuery());
    v.add(cli_nombE.getStrQuery());
    v.add(cli_codrepE.getStrQuery());
    v.add(cli_nomcoE.getStrQuery());
    v.add(cli_direE.getStrQuery());
    v.add(cli_direeE.getStrQuery());
    v.add(cli_poblE.getStrQuery());
    v.add(cli_pobleE.getStrQuery());
    v.add(zon_codiE.getStrQuery());
    v.add(cli_nomcoE.getStrQuery());
    v.add(rep_codiE.getStrQuery());
    v.add(cli_activE.getStrQuery());
    v.add(cli_servirE.getStrQuery());
    v.add(cli_comenE.getStrQuery());

    if (agente!=null)
      v.add("rep_codi = '"+agente+"'");
    relClien=true;
    s=creaWhere("select c.*,d.zon_nomb as dis_nomb from clientes as c "+
            " left join v_zonas as d  "+
            " on d.zon_codi = c.zon_codi ",v);
    s+=" ORDER BY cli_activ desc,cli_nomb";
//    debug("Query: "+s);
    navcli.setEnabled(navegador.TODOS, true);

    Pdatcli.setQuery(false);
    pCabCli.setQuery(false);
    pCabCli.setEnabled(false);
    Pdatcli.setEnabled(false);
    statusBar.setEnabled(true);
    try
    {
      if (!dtCli.select(s))
      {
        if (relClien)
            pRelClien.resetTexo();
        msgBox("No encontrados Clientes para esos criterios");
        mensaje("");
        cli_codiE.requestFocus();
        return false;
      }
      if (relClien)
        mensaje("");
      new consClienteTH(this);
    }
    catch (Exception ex)
    {
      fatalError("Error al buscar cliente: ",ex);
    }
    return true;
  }

  void verDatClien(DatosTabla dt)
  {
    try {
//      if (consProceso>=0)
//      {
//             mataConsulta=true;
//             while (consProceso>0)
//             {
//                 Thread.sleep(100);
//             }
//             mataConsulta=false;
//      }
//      consProceso++;
      mataConsulta=false;
      this.setEnabled(false);
      verDatCliente(dt,true);
      this.setEnabled(true);
      if (relClien)
        mensajeErr("Datos ... Encontrados");
      relClien=true;
     // mensaje("");
    } catch (Exception k)
    {
      Error("Error al Ver datos Clientes",k);
    }
//    consProceso--;
  }
  void verDatCliente(DatosTabla dt) throws Exception
  {
    verDatCliente(dt,true);
  }

  void verDatCliente( DatosTabla dt, boolean verAlb) throws Exception
  {

    if (dt.getNOREG() || dt.getSqlOpen() == false)
    {
       if (relClien)
        pRelClien.resetTexo();
      return;
    }
    cli_codiE.setValorInt(dt.getInt("cli_codi",true));
    cli_nombE.setText(dt.getString("cli_nomb"));
    cli_codrepE.setText(dt.getString("cli_codrut"));
    cli_nomcoE.setText(dt.getString("cli_nomco"));

    cli_poblE.setText(dt.getString("cli_pobl"));
    cli_pobleE.setText(dt.getString("cli_poble"));

    cli_direE.setText(dt.getString("cli_direc"));
    cli_direeE.setText(dt.getString("cli_diree"));

    cli_telefE.setText(dt.getString("cli_telef"));
    cli_telconE.setText(dt.getString("cli_telcon"));

    cli_perconE.setText(dt.getString("cli_percon"));
    cli_faxE.setText(dt.getString("cli_fax"));
    zon_codiE.setText(dt.getString("zon_codi"));
    rep_codiE.setText(dt.getString("rep_codi"));
    cli_activE.setValor(dt.getString("cli_activ").toUpperCase());
    cli_servirE.setValor(dt.getInt("cli_servir"));
    cli_comenE.setText(dt.getString("cli_comen"));
    if (mataConsulta)
        return;
    if (relClien)
        pRelClien.llenaGrid(dtCli.getStrSelect(),dtCon1);
    if (mataConsulta)
           return;
    if (opSoloCli.isSelected())
    {
      cPanel2.resetTexto();
      jtAlb.removeAllDatos();
      limpiaCobros();
      limpiaComen();
      limpiaComPro();
      return;
    }
    verDatCli(dt,verAlb);
    if (relClien)
    {
     mensajeErr("Datos de clientes encontrados ... ");
     mensaje("");
    }
  }
  public void setRelacionClientes(boolean relClientes)
  {
      relClien=relClientes;
  }
  void verDatCli(DatosTabla dt,boolean verAlb) throws Exception
  {
    if (dt.getNOREG() || dt.getSqlOpen() == false)
      return;

    int cliCodi = dt.getInt("cli_codi",true);
    int fpaCodi = dt.getInt("fpa_codi",true);
    if (verAlb)
      verAlbFra(dt);
    if (mataConsulta)
        return;
    verDatCobros(cliCodi, fpaCodi);
    if (mataConsulta)
        return;
    verDatComen(cliCodi);
    if (mataConsulta)
        return;
    verGrafico(cliCodi);
    if (mataConsulta)
        return;
    verDatComPr(cliCodi);
    if (mataConsulta)
        return;
  }

  void verAlbFra(DatosTabla dt) throws SQLException
  {
    if (dt.getNOREG() || dt.getSqlOpen() == false)
      return;
    if (albfacE.getValor().equals("A"))
    {
      s = "select  * from v_albavec c where " +
          " c.cli_codi = " + dt.getInt("cli_codi",true) +
          (EU.isRootAV()?"":" AND c.div_codi > 0 ")+
          " order by avc_fecalb desc";
      if (!dtAlb.select(s))
      {
        mensajeErr("No encontrados Albaranes para este cliente");
        Palbave.resetTexto();
        jtAlb.removeAllDatos();
        return;
      }
    }
    else
    {
      s = "select  * from v_facvec c where " +
          " c.cli_codi = " + dt.getInt("cli_codi",true) +
          " order by  fvc_fecfra desc";
      if (!dtAlb.select(s))
      {
        mensajeErr("No encontradas FACTURAS para este cliente");
        Palbave.resetTexto();
        jtAlb.removeAllDatos();
        return;
      }
    }
    verDatAlbaran(dtAlb);
  }

  void verDatAlbaran(DatosTabla dt) throws SQLException
  {

    if (dt.getNOREG() || dt.getSqlOpen() == false)
      return;
    if (albfacE.getValor().equals("F"))
    {
      verDatFra(dt);
      return;
    }
    if (relClien)
        mensaje("Buscando datos de Albaran ...",false);
//    Formatear.verAncGrid(jtAlb);
    emp_codiE.setText(dtAlb.getString("emp_codi"));
    avc_serieE.setText(dtAlb.getString("avc_serie"));
    avc_numeE.setValorDec(dtAlb.getInt("avc_nume",true));
    avc_fecalbE.setText(dtAlb.getFecha("avc_fecalb","dd-MM-yyyy"));
    usu_nombE.setText(dtAlb.getString("usu_nomb"));
    fvc_anoE.setValorDec(dtAlb.getInt("fvc_ano",true));
    fvc_numeE.setValorDec(dtAlb.getInt("fvc_nume",true));
    avc_impalbE.setValorDec(dtAlb.getDouble("avc_impalb",true));
    fvc_basimpE.setValorDec(0);

    cPanel2.resetTexto();
    avc_impcobE.setValorDec(dtAlb.getDouble("avc_impcob",true));
    if (opAgrlin.isSelected())
      s=getSqlLinAlb(dtAlb);
    else
    {
      s = "select l.*,p.pro_nomb as art_nomb from v_albavel as l,v_articulo as p where " +
          " l.avc_ano = " + dtAlb.getInt("avc_ano") +
          " and l.emp_Codi = " + dtAlb.getInt("emp_codi") +
          " and  l.avc_Serie = '" + dtAlb.getString("avc_Serie") + "'" +
          " and l.avc_nume = " + dtAlb.getInt("avc_nume") +
          " and l.pro_codi = p.pro_codi " +
          " order by l.avl_numlin ";
    }
//    debug(s);
    jtAlb.removeAllDatos();
    if (! dtCon1.select(s))
    {
      mensajeErr("Lineas de Albaran NO encontradas");
      return;
    }
    jtAlb.setEnabled(false);
    verLinAlb(dtCon1);
    jtAlb.setEnabled(true);
    jtAlb.requestFocus(0,0);
    verDatCob(dtAlb);
  }
  private String getSqlLinFra(DatosTabla dt) throws SQLException
  {
    if (opAgrlin.isSelected())
      return "SELECT l.pro_codi,p.pro_nomb as art_nomb,fvl_prven as avl_prven,sum(fvl_canti) AS avl_canti, " +
          " 0 AS avl_unid, 0 AS avl_numlin" +
          "  FROM   v_facvel l,v_articulo p where " +
          " l.avc_ano = " + dt.getInt("avc_ano") +
          " and l.emp_Codi = " + dt.getInt("emp_codi") +
          " and  l.avc_Serie = '" + dt.getString("avc_Serie") + "'" +
          " and l.avc_nume = " + dt.getInt("avc_nume") +
          " and l.pro_codi = p.pro_codi " +
          " and l.fvl_canti >= 0 " +
          " group by l.pro_codi,p.pro_nomb,fvl_prven " +
          " union all " +
          "SELECT l.pro_codi,p.pro_nomb as art_nomb,fvl_prven as avl_prven,sum(fvl_canti) AS avl_canti, " +
          " 0 AS avl_unid,0 AS avl_numlin " +
          "  FROM   v_facvel l,v_articulo p where " +
          " l.avc_ano = " + dt.getInt("avc_ano") +
          " and l.emp_Codi = " + dt.getInt("emp_codi") +
          " and  l.avc_Serie = '" + dt.getString("avc_serie") + "'" +
          " and l.avc_nume = " + dt.getInt("avc_nume") +
          " and l.pro_codi = p.pro_codi " +
          " and l.fvl_canti < 0 " +
          " group by l.pro_codi,p.pro_nomb,fvl_prven " +
          " order by 1 ";
    else
      return
          "select  l.pro_codi,p.pro_nomb as art_nomb,fvl_prven as avl_prven,fvl_canti AS avl_canti, " +
          " 0 AS avl_unid, fvl_numlin as avl_numlin from v_facvel as l,v_articulo as p where " +
          " l.avc_ano = " + dt.getInt("avc_ano") +
          " and l.emp_Codi = " + dt.getInt("emp_codi") +
          " and  l.avc_Serie = '" + dt.getString("avc_Serie") + "'" +
          " and l.avc_nume = " + dt.getInt("avc_nume") +
          " and l.pro_codi = p.pro_codi " +
          " order by l.fvl_numlin ";
  }
  private String getSqlLinAlb(DatosTabla dt) throws SQLException
  {
    if (opAgrlin.isSelected())
      return "SELECT l.pro_codi,p.pro_nomb as art_nomb,avl_prven,sum(avl_canti) AS avl_canti, " +
          " sum(avl_unid) AS avl_unid  " +
          "  FROM   v_albavel l,v_articulo p where " +
          " l.avc_ano = " + dt.getInt("avc_ano") +
          " and l.emp_Codi = " + dt.getInt("emp_codi") +
          " and  l.avc_Serie = '" + dt.getString("avc_Serie") + "'" +
          " and l.avc_nume = " + dt.getInt("avc_nume") +
          " and l.avl_canti + l.avl_prven != 0 " +
          " and l.avl_canti > 0 " +
          " and l.pro_codi = p.pro_codi " +
          " group by l.pro_codi,p.pro_nomb,avl_prven " +
          " union all " +
          "SELECT l.pro_codi,p.pro_nomb as art_nomb,avl_prven,sum(avl_canti) AS avl_canti, " +
          " sum(avl_unid) AS avl_unid " +
          "  FROM   v_albavel l,v_articulo p where " +
          " l.avc_ano = " + dt.getInt("avc_ano") +
          " and l.emp_Codi = " + dt.getInt("emp_codi") +
          " and  l.avc_Serie = '" + dt.getString("avc_serie") + "'" +
          " and l.avc_nume = " + dt.getInt("avc_nume") +
          " and l.avl_canti + l.avl_prven != 0 " +
          " and l.pro_codi = p.pro_codi " +
          " and l.avl_canti < 0 " +
          " group by l.pro_codi,p.pro_nomb,avl_prven  " +
          " order by 1 ";
    else
      return
          "select l.*,p.pro_nomb as art_nomb from v_albavel as l,v_articulo as p where " +
          " l.avc_ano = " + dt.getInt("avc_ano") +
          " and l.emp_Codi = " + dt.getInt("emp_codi") +
          " and  l.avc_Serie = '" + dt.getString("avc_Serie") + "'" +
          " and l.avc_nume = " + dt.getInt("avc_nume") +
          " and l.pro_codi = p.pro_codi " +
          " order by l.avl_numlin ";
  }

  private void verLinAlb(DatosTabla dt) throws SQLException, IllegalArgumentException
  {
    do
    {
      if (mataConsulta)
        return;
      ArrayList v=new ArrayList();
      v.add(dt.getString("pro_Codi"));
      v.add(dt.getString("art_nomb"));
      v.add(dt.getString("avl_canti"));
      v.add(dt.getString("avl_unid"));
      v.add(dt.getString("avl_prven"));
      v.add(""+(dt.getDouble("avl_canti")*dt.getDouble("avl_prven")));
      if (opAgrlin.isSelected())
        v.add("");
      else
        v.add(dt.getString("avl_numlin"));
      jtAlb.addLinea(v);
    } while (dt.next());
  }

  void verDatCob(DatosTabla dtAlb) throws SQLException
  {
    cgrid1.removeAllDatos();
    s ="select  cob_anofac, fac_nume,c.cob_feccob,c.cob_impor,tc.tpc_nomb,cob_fecvto " +
        " from v_cobros c,v_cobtipo tc " +
        " WHERE cob_anofac = " + dtAlb.getInt("fvc_ano") +
        " and c.emp_codi = " + dtAlb.getInt("emp_codi") +
        " and c.fac_nume = " + dtAlb.getInt("fvc_nume") +
        " and c.fac_nume > 0 " +
        " and tc.tpc_codi = c.tpc_codi ";
    if (albfacE.getValor().equals("A"))
    {
      s += " UNION ALL " +
          "select cob_ano, alb_nume,c.cob_feccob,c.cob_impor,tc.tpc_nomb,cob_fecvto " +
          " from v_cobros c,v_cobtipo tc " +
          " WHERE cob_ano = " + dtAlb.getInt("fvc_ano") +
          " and c.emp_codi = " + dtAlb.getInt("emp_codi") +
          " and c.alb_nume = " + dtAlb.getInt("avc_nume") +
          " and c.cob_serie = '" + dtAlb.getString("avc_serie") + "'" +
          " and tc.tpc_codi = c.tpc_codi " +
          " order by 1 desc,2 desc";
    }
    if (dtCon1.select(s))
    {
      do
      {
        if (mataConsulta)
            return;
        Vector v=new Vector();
        v.addElement(dtCon1.getFecha("cob_feccob","dd-MM-yy"));
        v.addElement(dtCon1.getString("cob_impor"));
        v.addElement(dtCon1.getString("tpc_nomb"));
        v.addElement(dtCon1.getFecha("cob_fecvto","dd-MM-yy"));
        cgrid1.addLinea(v);
      } while (dtCon1.next());
    }
  }

  void verDatFra(DatosTabla dt) throws SQLException
  {
    avc_serieE.resetTexto();
    avc_numeE.resetTexto();
    usu_nombE.resetTexto();
    cPanel2.resetTexto();
    emp_codiE.setValorDec(dtAlb.getInt("emp_codi"));
    fvc_numeE.setValorDec(dtAlb.getInt("fvc_nume",true));
    avc_impalbE.setValorDec(dtAlb.getDouble("fvc_sumtot"));
    avc_impcobE.setValorDec(dtAlb.getDouble("fvc_impcob"));
    avc_fecalbE.setText(dtAlb.getFecha("fvc_fecfra","dd-MM-yyyy"));
    fvc_anoE.setValorDec(dtAlb.getInt("fvc_ano"));
    fvc_basimpE.setValorDec(dtAlb.getDouble("fvc_basimp"));

    jtAlb.removeAllDatos();
    // Busco las cabeceras de albaran que tiene la factura.
    s="SELECT avc_nume,avc_ano,avc_serie,emp_codi,avc_fecalb FROM v_facvel "+
        " WHERE eje_nume = "+dtAlb.getInt("fvc_ano")+
        " and emp_codi = "+dtAlb.getInt("emp_codi")+
        " and fvc_nume = "+dtAlb.getInt("fvc_nume")+
        " group by  avc_nume,avc_ano,avc_serie,emp_codi,avc_fecalb"+
        " order by avc_ano,emp_codi,avc_serie";
    if (!dtCon1.select(s))
    {
      msgBox("No encontrados Lineas de FRA");
      return;
    }

    do
    {
      if (mataConsulta)
        return;
      Vector v=new Vector();
      v.addElement("");
      v.addElement(" ALB: "+dtCon1.getString("avc_serie")+"-"+Formatear.format(dtCon1.getInt("avc_nume"),"999999")+
                   " DE FECHA: "+dtCon1.getFecha("avc_fecalb","dd-MM-yyyy"));
      v.addElement("");
      v.addElement("");
      v.addElement("");
      v.addElement("");
      v.addElement("");
      jtAlb.addLinea(v);
      s=getSqlLinFra(dtCon1);
      if (dtStat.select(s))
        verLinAlb(dtStat);
    } while (dtCon1.next());
    verDatCob(dtAlb);
  }

  /**
   * Limpia datos de cobros
   */
  void limpiaCobros()
  {
    exceCredL.setText("");
    exceFechaL.setText("");
    cPanel3.resetTexto();
    cPanel2.resetTexto();
    jtAco.removeAllDatos();
    jtFco.removeAllDatos();
    jtTpe.removeAllDatos();
  }

  void verDatCobros(int cliCodi,int fpaCodi) throws Exception
  {
    
    int nAlbCob=0;
    int nFacCob=0,nTalCob=0,nDevol=0;
    double iAlbCob=0,iFacCob=0,iTalCob=0,iDevol=0;
    limpiaCobros();
    if (relClien)
        mensaje("Buscando datos de Cobros ...",false);
    String cueCodi="";
    s="SELECT * FROM clientes WHERE cli_codi = "+cliCodi;    
    if (dtCob.select(s))
    {
      cdo_conceE.setValorDec(dtCob.getDouble("cli_riesg"));
      cueCodi=dtCob.getString("cue_codi");
    }
    else
      cdo_conceE.setValorDec(0);

    s = "SELECT  * FROM v_albavec WHERE fvc_ano = 0 " +
        " AND  fvc_nume = 0" +
        " AND cli_codi = "+cliCodi+
        " AND avc_cobrad = 0 " +
        (EU.isRootAV()?"":" AND div_codi > 0 ")+
        " and avc_impalb <> 0"+
        " ORDER BY avc_fecalb";

//    System.out.println("s: "+s);
    if (dtCob.select(s))
    {
//      mensajeErr("Sin albaranes pendientes de Cobro");
      jtAco.setEnabled(false);
      do
      {
        if (mataConsulta)
            return;
        ArrayList v = new ArrayList();
        v.add(dtCob.getString("emp_codi"));
        v.add(dtCob.getString("avc_ano"));
        v.add(dtCob.getString("avc_serie"));
        v.add(dtCob.getString("avc_nume"));
        v.add(dtCob.getFecha("avc_fecalb","dd-MM-yyyy"));
        v.add(dtCob.getString("avc_impalb"));
        v.add(dtCob.getString("avc_impcob"));
        v.add(dtCob.getDouble("avc_impalb") - dtCob.getDouble("avc_impcob"));
        if (dtCob.getDouble("avc_impcob")>=0)
        {
          nAlbCob++;
          iAlbCob+=dtCob.getDouble("avc_impalb") - dtCob.getDouble("avc_impcob");
        }
        else
        {
          nDevol++;
          iDevol+=dtCob.getDouble("avc_impalb") - dtCob.getDouble("avc_impcob");
        }
        jtAco.addLinea(v);
      }
      while (dtCob.next());
      jtAco.requestFocus(0,0);
      jtAco.setEnabled(true);
    }
    if (dtCont!=null)
    {
    s = "SELECT  ano,empresa,tipo_efec,test_sit, nominal,COB_PARCIAL,cuenta,num_efec,TIPO_DOC,fec_fra FROM EFECTOS WHERE  "+
        " cuenta = '"+cueCodi+"'"+
        " and empresa='VP' "+
       " ORDER BY fec_fra";
//   System.out.println("s: "+s);
   nDevol=0;
   iDevol=0;
   GregorianCalendar gc= new GregorianCalendar();

   if (dtCont.select(s))
   {
     jtFco.setEnabled(false);
     do
     {
       if (mataConsulta)
            return;
       ArrayList v = new ArrayList();
       v.add(dtCont.getString("empresa"));
       v.add(dtCont.getString("ano"));
       v.add(dtCont.getString("tipo_efec")); //dtCob.getString("fvc_serie"));
       v.add(dtCont.getString("num_efec"));
       v.add(dtCont.getFecha("fec_fra","dd-MM-yyyy"));
       if (restaDias(Formatear.getFechaAct("dd-MM-yyyy"),dtCont.getFecha("fec_fra","dd-MM-yyyy"))>30)
         exceFechaL.setText("FACTURAS CON + 30 DIAS");
       v.add(dtCont.getDouble("nominal")-dtCont.getDouble("COB_PARCIAL",true));
       if (dtCont.getDouble("test_sit")!=3)
       {
         nFacCob++;
         iFacCob += dtCont.getDouble("nominal")-dtCont.getDouble("COB_PARCIAL",true);
       }
       else
       {
         nDevol++;
         iDevol+=dtCont.getDouble("nominal")-dtCont.getDouble("COB_PARCIAL",true);
       }
       jtFco.addLinea(v);
     }  while (dtCont.next());
     cob_devfiE.setValorDec(iDevol);
     cob_devfnE.setValorDec(nDevol);
     cob_devaiE.setValorDec(iDevol);
     cob_devanE.setValorDec(nDevol);
     jtFco.requestFocus(0,0);
     jtFco.setEnabled(true);
   }
    if (verCobros)
    {
            s = "SELECT * FROM v_forpago where fpa_codi = " + fpaCodi;
            if (!dtCon1.select(s))
                fpa_nombE.setText("Forma de Pago: " + fpaCodi + " NO encontrada");
            else
                fpa_nombE.setText(dtCon1.getString("fpa_nomb"));

            // Buscar cobros cuya fecha vto. sea superior a la de hoy.
            s = "SELECT  f.emp_codi,f.fvc_ano,f.fvc_nume,c.cob_fecvto,c.cob_impor,"
                + " c.tpc_codi, ct.tpc_nomb,c.cob_impor,cob_obser "
                + " FROM v_facvec f,v_cobros c, v_cobtipo as ct "
                + " where cli_codi = " + cliCodi
                + " AND fvc_cobrad = -1 "
                + " and  c.cob_anofac=f.fvc_ano "
                + " and c.tpc_codi = ct.tpc_codi "
                + " and c.fac_nume =f.fvc_nume"
                + " and cob_fecvto > TO_DATE('" + Formatear.getFechaAct("dd-MM-yyyy") + "','dd-MM-yyyy') "
                + " order by cob_fecvto";

            //   System.out.println("s: "+s);
            if (dtCob.select(s))
            {
                jtTpe.setEnabled(false);
                do
                {
                    if (mataConsulta)
                        return;
                    ArrayList v = new ArrayList();
                    v.add(dtCob.getString("emp_codi"));
                    v.add(dtCob.getString("fvc_ano"));
                    v.add("T"); //dtCob.getString("fvc_serie"));
                    v.add(dtCob.getString("fvc_nume"));
                    v.add(dtCob.getFecha("cob_fecvto", "dd-MM-yyyy"));
                    v.add(dtCob.getString("cob_impor"));
                    v.add(dtCob.getString("cob_obser"));
                    iTalCob += dtCob.getDouble("cob_impor");
                    nTalCob++;
                    jtTpe.addLinea(v);
                } while (dtCob.next());
                jtTpe.requestFocus(0, 0);
                jtTpe.setEnabled(true);
            }
        }
    }
   cob_talcaiE.setValorDec(iTalCob);
   cob_talcanE.setValorDec(nTalCob);
   cob_frapeiE.setValorDec(iFacCob);
   cob_frapenE.setValorDec(nFacCob);
   cob_albpeiE.setValorDec(iAlbCob);
   cob_albpenE.setValorDec(nAlbCob);
   cob_devoiE.setValorDec(cob_devaiE.getValorDec()+cob_devfiE.getValorDec());
   cob_devonE.setValorDec(cob_devanE.getValorDec()+cob_devfnE.getValorDec());
   cob_riestotE.setValorDec(cob_devoiE.getValorDec()+cob_talcaiE.getValorDec()+
                            cob_frapeiE.getValorDec()+cob_albpeiE.getValorDec());
   cob_crelibE.setValorDec(0);
   cob_cresobE.setValorDec(0);
   if (cdo_conceE.getValorDec()> cob_riestotE.getValorDec())
     cob_crelibE.setValorDec(cdo_conceE.getValorDec() -
                             cob_riestotE.getValorDec());
   else
     cob_cresobE.setValorDec(cob_riestotE.getValorDec() -
                             cdo_conceE.getValorDec());
   if (cob_cresobE.getValorDec()>0)
     exceCredL.setText("CREDITO EXCEDIDO EN "+cob_cresobE.getValorDec());
   /*s="select c.cob_feccob,c.cob_impor,tpc_codi,cob_fecvto "+
      " from v_cobros c,v_facvec f "+
      " WHERE cob_anofac = f.fvc_ano "+
      " and c.emp_codi = f.emp_codi  "+
      " and c.fac_nume = f.fvc_nume "+
      " and f.cli_codi= "+cliCodi+
      " order by cob_anofac desc,fac_nume desc";
  dtCob.select(s);
  jtdCo.setDatos(dtCob);
*/
  }

  boolean cli_cod1E_focusLost() {

    s="SELECT cli_nomb,cli_zonrep FROM clientes  WHERE cli_codi = "+cli_cod1E.getValorInt();
    try
    {
      if (!dtCon1.select(s))
      {
        cli_nombL.setText(" CLIENTE NO ENCONTRADO ");
        return false;
      }
      else
      {
        cli_nombL.setText(dtCon1.getString("cli_nomb"));
        zon_codiE2.setText(dtCon1.getString("zon_codi"));
        return true;
      }
    }
    catch (SQLException ex)
    {
      Error("1- Error al buscar codigo de cliente: ",ex);
    }
    return false;
  }

  void Bacezon_actionPerformed(ActionEvent e) {
    if (! cli_cod1E_focusLost())
    {
      mensajeErr("Cliente no encontrado");
      cli_cod1E.requestFocus();
      return;
    }
    if (zon_codiE1.getError())
    {
      mensajeErr("Zona NO VALIDA");
      zon_codiE.requestFocus();
      return;
    }
    s="UPDATE cliente set zon_codi = '"+zon_codiE.getText()+"'"+
        " WHERE cliente = "+cli_cod1E.getValorInt();

    try
    {
      int nReg = dtCon1.executeUpdate(s);
      if (nReg != 1)
      {
        fatalError("N. de registros modificados diferente de uno (" + nReg +")");
        return;
      }
      dtCon1.commit();
    }
    catch (Exception ex)
    {
      fatalError("Error al modificar zona de cliente");
    }
    cli_cod1E.requestFocus();
     mensajeErr("Cliente Modificado");
  }
  void limpiaComen()
  {
    Bmodcom.setEnabled(false);
    jtCom.setEnabled(false);
    jtCom.removeAllDatos();
 
   
  }
  boolean getSqlComent(DatosTabla dt, int cliCodi) throws SQLException
  {
       s="SELECT * FROM compedven WHERE cli_codi = "+cliCodi+
        " ORDER BY cpv_fecha desc";
       return dt.select(s);
  }
  void verDatComen(int cliCodi) throws SQLException
  {
   
    limpiaComen();
    if (relClien)
        mensaje("Buscando datos Comentarios de Ventas ...",false);

    if (getSqlComent(dtCon1,cliCodi))
    {
      do
      {
        if (mataConsulta)
            return;
        Vector v = new Vector();
        v.addElement(dtCon1.getFecha("cpv_fecha","dd-MM-yyyy"));
        v.addElement(dtCon1.getString("cpv_come"));
        jtCom.addLinea(v);
      } while (dtCon1.next());
       cpv_textE.setText(jtCom.getValString(0,1));
    }
    else
        cpv_textE.setText("");
    if (mataConsulta)
            return;
    String fecAct=Formatear.getFechaAct("dd-MM-yyyy");
    cpv_fechaE.setText(fecAct);
//    cli_codiL.setText(cli_codiE.getText());
//    cli_codiL1.setText(cli_nombE.getText());
    if (jtCom.isVacio() || ! jtCom.getValString(0).equals(fecAct))
    {
      ArrayList v=new ArrayList();
      v.add(fecAct);
      v.add("");
      jtCom.addLinea(v,0);
    }
    jtCom.requestFocus(0,0);
    jtCom.setEnabled(true);
    Bmodcom.setEnabled(true);
  }

  void Bmodcom_actionPerformed(ActionEvent e)
  {
//    Formatear.verAncGrid(jtCom);
    if (cpv_textE.getText().trim().equals(""))
      return;
    try
    {
      s = "SELECT * FROM compedven WHERE cli_codi = " + cli_codiE.getText() +
          " AND cpv_fecha = to_date('" + cpv_fechaE.getFecha("dd-MM-yyyy") +
          "','dd-MM-yyyy')";
      if (!dtBlo.select(s))
        s = "INSERT INTO COMPEDVEN VALUES(" + cli_codiE.getText().trim() +
          ",to_date('" + cpv_fechaE.getFecha("dd-MM-yyyy") + "','dd-MM-yyyy')" +
          ",'" + cpv_textE.getText() + "'" +
          ")";
      else
        s="UPDATE compedven set cpv_come = '"+cpv_textE.getText() +"'"+
            " WHERE cli_codi = " + cli_codiE.getText() +
            " AND cpv_fecha = to_date('" + cpv_fechaE.getFecha("dd-MM-yyyy") +"','dd-MM-yyyy')";


      dtBlo.executeUpdate(s);
      dtBlo.commit();
      jtCom.setValor(cpv_textE.getText(),1);
    } catch (ParseException | SQLException k)
    {
      fatalError("Error al Insertar Comentario",k);
    }

  }

  void verDatComPr(int cliCodi) throws SQLException,ParseException
  {
    if (ct.getDriverType()==conexion.POSTGRES)
      return;
//    boolean sinComPr=true; // Que no busque comentarios sobre prod.
//    if (sinComPr)
//      return;
//    Formatear.verAncGrid(jtcopr);
    if (relClien)
        mensaje("Buscando datos Comentarios Ventas Prod. ...",false);

    fefivpE.setText(Fecha.getFechaSys("dd-MM-yyyy"));
    GregorianCalendar gc= new GregorianCalendar();
    gc.setTime(new Date(System.currentTimeMillis()));
    gc.add(GregorianCalendar.MONTH,-2);
    feinvpE.setText(Formatear.getFecha(gc.getTime(),"dd-MM-yyyy"));
//    feinvpE.setText();
    s = "select pro_codi,max(avc_fecalb) as avc_fecalb from v_albavec c, v_albavel l" +
        " where  l.avc_ano = c.avc_ano " +
        " and l.emp_Codi =c.emp_codi " +
        " and  l.avc_Serie = c.avc_serie " +
        " and l.avc_nume =  c.avc_nume " +
        " and c.cli_codi=" + cliCodi +
        (EU.isRootAV()?"":" AND c.div_codi > 0 ")+
        " and c.avc_fecalb>= to_date('" +feinvpE.getText()+"','dd-MM-yyyy')"+
      " group by pro_codi"+
      " order by pro_codi";
//  System.out.println("s: "+s);
  limpiaComPro();
  jtcopr.setEnabled(false);
  jtcopr.removeAllDatos();
  Bmocopr.setEnabled(false);
    if (dtCon1.select(s))
    {
      do
      {
        if (mataConsulta)
            return;
        Vector v = new Vector();
        v.add(dtCon1.getString("pro_codi"));
        s =
            "select p.pro_nomb,l.avl_prven FROM v_articulo p,v_albavel l,v_albavec c  " +
            " where  l.avc_ano = c.avc_ano " +
            " and l.emp_Codi =c.emp_codi " +
            " and  l.avc_Serie = c.avc_serie " +
            " and l.avc_nume =  c.avc_nume " +
            " and c.cli_codi=" + cliCodi +
            (EU.isRootAV()?"":" AND c.div_codi > 0 ")+
            " and c.avc_fecalb>= to_date('" +dtCon1.getFecha("avc_fecalb", "dd-MM-yyyy") + "','dd-MM-yyyy')" +
            " AND p.pro_codi = l.pro_codi ";
//    System.out.println("s:"+s);
        if (!dtStat.select(s))
        {
          v.add("**NO ENCONTRADO **");
          v.add(dtCon1.getFecha("dd-MM-yyyy"));
          v.add("");
        }
        else
        {
           v.add(dtStat.getString("pro_nomb"));
          v.add(dtCon1.getFecha("avc_fecalb", "dd-MM-yyyy"));
          v.add(dtStat.getString("avl_prven"));
        }
        s = "SELECT cpc_come FROM comprocli WHERE cli_codi = " + cliCodi +
            " AND pro_codi = " + dtCon1.getInt("pro_codi");
        if (dtStat.select(s))
          v.add(dtStat.getString("cpc_come"));
        else
          v.add("");
        jtcopr.addLinea(v);
      }
      while (dtCon1.next());

    }
    s = "SELECT c.pro_codi,c.cpc_come FROM comprocli c "+
        " WHERE c.cli_codi = " + cliCodi +
        " order by c.PRO_CODI";
    if (dtCon1.select(s))
    {
      boolean swEnc=false;
      do
      {
        if (mataConsulta)
            return;
        swEnc=false;
        for (int n=0;n<jtcopr.getRowCount();n++)
        {
          if (dtCon1.getInt("pro_codi")==jtcopr.getValorInt(n,0))
          {
            swEnc = true;
            break;
          }
        }
        if (!swEnc)
        {
          Vector v=new Vector();
          v.add(dtCon1.getString("pro_codi"));
          s="SELECT pro_nomb FROM v_articulo WHERE pro_codi = "+dtCon1.getInt("pro_codi");
          if (dtStat.select(s))
            v.add(dtStat.getString("pro_nomb"));
          else
            v.add("** NO ENCONTRADO **");
          v.add("");
          v.add("");
          v.add(dtCon1.getString("cpc_come"));
          jtcopr.addLinea(v);
        }
      } while (dtCon1.next());
    }
    jtcopr.requestFocus(0,0);
     jtcopr.setEnabled(true);
     Bmocopr.setEnabled(true);
     Bincopr.setEnabled(true);
     Bbocopr.setEnabled(true);
  }

  void Bmocopr_actionPerformed(int cliCodi,int proCodi) {

     if (cpc_comeE.getText().trim().equals(""))
     {
       mensajeErr("Introduzca un texto en el comentario");
       return;
     }
     insComPro(cliCodi,proCodi);
     jtcopr.setValor(cpc_comeE.getText(),4);
  }
  boolean insComPro(int cliCodi,int proCodi)
  {
    boolean swExCom=false;
     try
     {
       s = "SELECT * FROM comprocli WHERE cli_codi = " + cliCodi +
           " AND pro_codi = "+proCodi;
       if (!dtBlo.select(s))
       {
         s = "INSERT INTO comprocli VALUES(" + cliCodi +
             "," + proCodi +
             ",'" + cpc_comeE.getText() + "'" +
             ")";
         swExCom=true;
       }
       else
         s="UPDATE comprocli set cpc_come = '"+cpc_comeE.getText() +"'"+
             " WHERE cli_codi = " + cliCodi +
             " AND pro_codi = " +proCodi;

       dtBlo.executeUpdate(s);
       dtBlo.commit();

     } catch (Exception k)
     {
       fatalError("Error al Insertar Comentario",k);
     }
     return swExCom;
  }

  void limpiaComPro()
  {
    pro_codiE.resetTexto();
    pro_nombL.setText("");
    jtcopr.setEnabled(false);
    jtcopr.removeAllDatos();
    Bmocopr.setEnabled(false);
    Bbocopr.setEnabled(false);
    Bincopr.setEnabled(false);
  }

  void Bincopr_actionPerformed(ActionEvent e) {
    if (pro_codiE.getValorInt()==0)
    {
      mensajeErr("Introduzca Codigo de Producto");
      pro_codiE.requestFocus();
      return;
    }
    if (cpc_comeE.getText().trim().equals(""))
     {
       cpc_comeE.requestFocus();
       mensajeErr("Introduzca un texto en el comentario");
       return;
     }

    if (insComPro(cli_codiE.getValorInt(),pro_codiE.getValorInt()))
    {
      Vector v = new Vector();
      v.add(pro_codiE.getText());
      v.add(pro_nombL.getText());
      v.add("");
      v.add("");
      v.add(cpc_comeE.getText());
      jtcopr.addLinea(v);
    }
    else
    {
      for (int n=0;n<jtcopr.getRowCount();n++)
      {
        if (pro_codiE.getValorInt()==jtcopr.getValorInt(n,0))
        {
          jtcopr.setValor(cpc_comeE.getText(),n,4);
          break;
        }
      }

    }
  }
  void Bbocopr_actionPerformed(ActionEvent e) {
    try
    {
      if (jtcopr.isVacio())
        return;
      s = "DELETE  FROM comprocli WHERE cli_codi = " + cli_codiE.getValorInt()  +
           " AND pro_codi = "+jtcopr.getValorInt(0);
       if (dtBlo.executeUpdate(s)>1)
         throw new SQLException("Numero de Registros borrados superior de uno\n"+s);
       dtBlo.commit();
       jtcopr.removeLinea();
   } catch (Exception k)
   {
     fatalError("Error al Borrar Comentario",k);
   }
  }
  void setCabecera(String nombCli, String poblCli)
  {
      swActCli=true;
      cli_nombE.setText(nombCli);
      cli_poblE.setText(poblCli);
  }
  public void PADPrimero() {
    try
       {
         if (dtCli.isFirst())
           return;
         if (dtCli.first())
         {
           verDatCliente(dtCli);
         }
       }
       catch (Exception ex)
       {
         SystemOut.print(ex);
       }
}
  public void PADAnterior() {
    try
        {
          if (dtCli.isFirst())
            return;
          if (dtCli.previous())
          {
            verDatCliente(dtCli);
          }
        }
        catch (Exception ex)
        {
        }

  }
  public void PADSiguiente() {
    try
         {
           if (dtCli.isLast())
             return;
           if (dtCli.next())
             verDatCliente(dtCli);
         }
         catch (Exception ex)
         {
           SystemOut.print(ex);
         }
  }

  @Override
  public void PADUltimo()
  {
    try
    {
      if (dtCli.isLast())
        return;
      if (dtCli.last())
      {
        verDatCliente(dtCli);
      }
    }
    catch (Exception ex)
    {
      SystemOut.print(ex);
    }
  }

  /**
   * Devuelve la diferencia en dias entre dos fechas
   * @param fecFin Fecha Final (la mayor)
   * @param fecIni Fecha Inicial (la menos)
   * @return dias que pasan
   * @throws ParseException Si hay errores de formato etc.
   */
  int restaDias(String fecFin, String fecIni) throws ParseException
  {
    return Formatear.restaDias(fecFin, fecIni);
  }
  @Override
  public void PADQuery() {
    activar(true);
    navcli.pulsado=navegador.QUERY;
    buscaCli();
  }
  @Override
  public void ej_query() {
  }
  @Override
  public void ej_query1() {
  }
  @Override
  public void canc_query() {
    navcli.pulsado=navegador.NINGUNO;
  }
  @Override
  public void PADEdit() {
    if (cli_codiE.getValorInt()==0)
    {
      activar(true);
      mensajeErr("NO HAY REGISTROS ACTIVOS");
      navcli.setEnabled(navegador.TODOS, true);
      statusBar.setEnabled(true);
      return;
    }
    Pdatcli.setEnabled(true);

    pCabCli.setEnabled(true);
    activar(false);
    navAlb.pulsado=navegador.EDIT;

    cli_codiE.setEnabled(false);
    cli_nombE.requestFocus();
    mensaje("Editando Registro ...");
  }

  public void ej_edit()
  {
    int res=mensajes.mensajeYesNo("Aceptar Modificaciones ");
    if (res!=mensajes.YES)
        return;
    try {
      s = "SELECT * FROM clientes WHERE cli_codi = " + cli_codiE.getValorInt();
      if (! dtBlo.select(s, true))
      {
        msgBox("CLIENTE NO ENCONTRADO");
        canc_edit();
        return;
      }
      dtBlo.edit(dtBlo.getCondWhere());
      dtBlo.setDato("cli_nomco",cli_nomcoE.getText());
      dtBlo.setDato("cli_nomb",cli_nombE.getText());
      dtBlo.setDato("cli_telef",cli_telefE.getText());
      dtBlo.setDato("cli_telcon",cli_telconE.getText());
      dtBlo.setDato("cli_percon",cli_perconE.getText());
      dtBlo.setDato("cli_fax",cli_faxE.getText());
      dtBlo.setDato("cli_comen",cli_comenE.getText());
      dtBlo.setDato("cli_poble",cli_pobleE.getText());

      if (supUsua)
      {
        dtBlo.setDato("cli_pobl",cli_poblE.getText());
        dtBlo.setDato("cli_direc",cli_direE.getText());
        dtBlo.setDato("cli_diree",cli_direeE.getText());
        dtBlo.setDato("cli_zonrep",zon_codiE.getText());
        dtBlo.setDato("cli_zoncre",rep_codiE.getText());
        dtBlo.setDato("cli_activ",cli_activE.getValor());
      }
      dtBlo.update(stUp);
      s=" INSERT INTO cliencamb values("+dtBlo.getStrInsert()+
         ", '"+EU.usuario+"'"+
         ",TO_DATE('"+Formatear.getFechaAct("dd-MM-yyyy")+"','dd-MM-yyyy')"+
         ","+Formatear.getFechaAct("HH.mm")+
         ",'Cambiado desde consCliente')";
      stUp.executeUpdate(dtBlo.getStrSelect(s));
      ctUp.commit();
      mensaje("");
      mensajeErr("Datos ... Modificados");
      Pdatcli.setEnabled(false);
      pCabCli.setEnabled(false);
      statusBar.setEnabled(true);
      navcli.setEnabled(navegador.TODOS, true);
      navcli.pulsado=navegador.NINGUNO;
//      verDatClien(dtCli);
    } catch (Exception k)
    {
      Error("Error al Modificar datos de Cliente",k);
    }
  }
  public void ej_edit1() {
  }
  public void canc_edit() {
    mensaje("");
    Pdatcli.setEnabled(false);
    pCabCli.setEnabled(false);
    statusBar.setEnabled(true);
    navcli.setEnabled(navegador.TODOS, true);
    verDatClien(dtCli);
    navcli.pulsado=navegador.NINGUNO;
    mensajeErr("Edicion cancelada ...");
  }  
   @Override
  public void PADAddNew() {
  }
   @Override
  public void ej_addnew() {
  }
   @Override
  public void ej_addnew1() {
  }
   @Override
  public void canc_addnew() {
  }
   @Override
  public void PADDelete() {
  }
   @Override
  public void ej_delete() {
  }
   @Override
  public void ej_delete1() {
  }
   @Override
  public void canc_delete() {
  }
   @Override
  public void PADChose() {
  }
  @Override
  public void activar(boolean b) {
    cli_codiE.setEnabled(b);
//    opSoloCli.setEnabled(!b);
    if (supUsua)
      return;
   // cli_nomcoE.setEnabled(b);
    cli_poblE.setEnabled(b);
    cli_direE.setEnabled(b);
    cli_direeE.setEnabled(b);
    cli_servirE.setEnabled(b);
    zon_codiE.setEnabled(b);
    if (agente==null)
        rep_codiE.setEnabled(b);
    cli_activE.setEnabled(b);
  }
  @Override
  public void ej_Bcancelar(ActionEvent e) {
  }
  @Override
  public void ej_Baceptar(ActionEvent e) {
  }
  void buscAlb()
  {
    swBusDat=true;
    try
    {
      String s1;
      s1 = "SELECT * FROM  v_albavec c,clientes cl "+
          " where c.emp_Codi =" +emp_codiE.getValorInt() +
          " AND c.avc_ano = " +(fvc_anoE.getValorInt()==0?EU.ejercicio:fvc_anoE.getValorInt())+
          " AND c.avc_serie ='" + avc_serieE.getText() + "'" +
          " and c.avc_nume = " + avc_numeE.getValorInt() +
          (EU.isRootAV()?"":" AND c.div_codi > 0 ")+
          " and c.cli_codi = cl.cli_codi ";

      if (agente != null)
        s1 += " AND cl.cli_zonrep LIKE '" + agente + "'";

//      debug(s1);
      if (!dtCon1.select(s1))
      {
        msgBox("NO ENCONTRADO ALBARAN");
        swBusDat=false;
        verDatAlbaran(dtAlb);
        return;
      }
      s="select * from clientes where cli_codi = "+dtCon1.getInt("cli_codi");
      if (!dtCli.select(s))
      {
        msgBox("No encontrados datos del cliente: "+dtCon1.getInt("cli_codi"));
        swBusDat=false;
        return;
      }
      navcli.setEnabled(navegador.TODOS, true);
      Pdatcli.setQuery(false);
      Pdatcli.setEnabled(false);
      pCabCli.setQuery(false);
      pCabCli.setEnabled(false);
      statusBar.setEnabled(true);
      relClien=false;
      verDatCliente(dtCli,false);
      dtAlb.select(s1);
      if (albfacE.getValor().equals("F"))
      {
        if (dtAlb.getInt("fvc_nume")<=0)
        {
          msgBox("ALBARAN NO ESTA FACTURADO");
          swBusDat=false;
          return;
        }
        s1="SELECT * FROM v_facvec "+
            " WHERE emp_codi = "+dtAlb.getInt("emp_codi")+
            " and fvc_ano = "+dtAlb.getInt("fvc_ano")+
            " and fvc_nume = "+dtAlb.getInt("fvc_nume");
        if (!dtAlb.select(s1))
        {
          msgBox("No encontrado Factura  DE Albaran SELECIONADO");
          swBusDat=false;
          return;
        }
      }
      verDatAlbaran(dtAlb);
    }
    catch (Exception ex)
    {
      Error("Error al buscar albaran",ex);
    }
     swBusDat=false;
  }

  void buscFra()
  {
    swBusDat=true;
    albfacE.setValor("F");
    try
    {
      String s1;
      s1 = "SELECT * FROM v_facvec c,clientes cl " +
          " WHERE c.emp_codi = " + emp_codiE.getValorInt() +
          " and fvc_ano = " + fvc_anoE.getValorInt() +
          " and fvc_nume = " + fvc_numeE.getValorInt() +
          " and c.cli_codi = cl.cli_codi "+
          " and cl.cli_codi = c.cli_codi ";
      if (agente != null)
        s1 += " AND cl.cli_zonrep LIKE '" + agente + "'";

//      debug(s1);
      if (!dtCon1.select(s1))
      {
        msgBox("NO ENCONTRADO FACTURA");
        verDatAlbaran(dtAlb);
        swBusDat=false;
        return;
      }
      s = "select * from clientes where cli_codi = " +
          dtCon1.getInt("cli_codi");
      if (!dtCli.select(s))
      {
        msgBox("No encontrados datos del cliente: " + dtCon1.getInt("cli_codi"));
        swBusDat=false;
        return;
      }
      relClien=false;
      navcli.setEnabled(navegador.TODOS, true);
      Pdatcli.setQuery(false);
      Pdatcli.setEnabled(false);
      pCabCli.setQuery(false);
      pCabCli.setEnabled(false);
      statusBar.setEnabled(true);
      verDatCliente(dtCli, false);
      dtAlb.select(s1);
      verDatFra(dtAlb);
    }
    catch (Exception ex)
    {
      Error("Error al buscar FACTURA", ex);
    }
    swBusDat=false;
  }

    @Override
  public void matar(boolean cerrarConexion)
  {
    if (muerto)
      return;
    if (ayVePr != null)
    {
      ayVePr.setVisible(false);
      ayVePr.dispose();
    }
    if (ayLoPr != null)
    {
      ayLoPr.setVisible(false);
      ayLoPr.dispose();
    }

    super.matar(cerrarConexion);
  }

  void Bmodif_actionPerformed()
  {

  }

  public void PADAddNew1()
  {}

  public void PADQuery1()
  {}

  public void PADDelete1()
  {}

  public void PADEdit1()
  {}
   void verGrafico(int cliCodi) throws SQLException,ParseException
   {
    java.util.Date dt=  Formatear.sumaDiasDate(Formatear.getDateAct(),-60) ;

    s="SELECT sum(avc_basimp) as avc_basimp,sum(avc_kilos) as avc_kilos FROM v_albavec where  cli_codi = "+cliCodi+
          (EU.isRootAV()?"":" AND div_codi > 0 ")+
          " and avc_fecalb between ?  and ? ";
    PreparedStatement psGraf=ct.prepareStatement(s);
    ResultSet rsGraf;


    String ACTUAL = "Kilos "+Formatear.getFechaAct("yyyy");
    String ANTERIOR = "Kilos "+ (Integer.parseInt(Formatear.getFechaAct("yyyy"))-1);
    String KILOS = "Kilos";
    String IMPORTE = "Importe";
    pGrNorth.removeAll();
    pGrSouth.removeAll();
    // Creamos y rellenamos el modelo de datos

    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    int nLin=0;
    while (nLin<8)
    {
       if (mataConsulta)
        return;
       nLin++;
       
       psGraf.setDate(1, new java.sql.Date(dt.getTime()));
       psGraf.setDate(2,  new java.sql.Date(Formatear.sumaDiasDate(dt,+6).getTime()) );

       rsGraf=psGraf.executeQuery();
       rsGraf.next();
       if (rsGraf.getObject("avc_kilos")!=null)
       {
          dataset.setValue(rsGraf.getDouble("avc_kilos"), KILOS, Formatear.getFecha(dt, "dd-MM"));
          dataset.setValue(rsGraf.getDouble("avc_basimp")/10, IMPORTE, Formatear.getFecha(dt, "dd-MM"));
       }
       else
       {
          dataset.setValue(0, KILOS, Formatear.getFecha(dt, "dd-MM"));
          dataset.setValue(0, IMPORTE, Formatear.getFecha(dt, "dd-MM"));
       }
       dt=Formatear.sumaDiasDate(dt,+7);
    }
    JFreeChart chart = ChartFactory.createBarChart("Ultimas Ventas","",
        null, dataset, PlotOrientation.VERTICAL, true,
        true, false);

    ChartPanel panel = new ChartPanel(chart);
    panel.setPreferredSize(new Dimension(700,200));
    // Crear grafico de ventas anual. Mes x Mes
    dt=  Formatear.sumaMesDate(
            Formatear.getDate(
            "01-"+(Integer.parseInt(Formatear.getFechaAct("MM")))+"-"+
            (Integer.parseInt(Formatear.getFechaAct("yyyy"))),
            "dd-MM-yyyy"),-18);

    DefaultCategoryDataset dts1 = new DefaultCategoryDataset();
    nLin=0;
    while (nLin<7)
    {
       if (mataConsulta)
        return;
       nLin++;

       psGraf.setDate(1, new java.sql.Date(dt.getTime()));
       psGraf.setDate(2,  new java.sql.Date(Formatear.sumaMesDate(dt,1).getTime()) );

       rsGraf=psGraf.executeQuery();
       rsGraf.next();
       if (rsGraf.getObject("avc_kilos")!=null)
          dts1.setValue(rsGraf.getDouble("avc_kilos"), ANTERIOR, Formatear.getFecha(dt, "MM"));
       else
          dts1.setValue(0, ANTERIOR, Formatear.getFecha(dt, "MM"));
       psGraf.setDate(1, new java.sql.Date(Formatear.sumaMesDate(dt,+12).getTime()));
       psGraf.setDate(2, new java.sql.Date(Formatear.sumaMesDate(dt,+13).getTime()));
       rsGraf=psGraf.executeQuery();
       rsGraf.next();
       if (rsGraf.getObject("avc_kilos")!=null)
          dts1.setValue(rsGraf.getDouble("avc_kilos"), ACTUAL, Formatear.getFecha(dt, "MM"));
       else
          dts1.setValue(0, ACTUAL, Formatear.getFecha(dt, "MM"));
       dt=Formatear.sumaMesDate(dt,1);
    }
    JFreeChart chart1 = ChartFactory.createBarChart("Comparativa Ventas Anuales", "Meses",
        "Kilos", dts1, PlotOrientation.VERTICAL, true,
        true, false);

    ChartPanel chpanel1 = new ChartPanel(chart1);
    chpanel1.setPreferredSize(new Dimension(700,200));
    pGrNorth.add(panel,BorderLayout.CENTER);
    pGrSouth.add(chpanel1,BorderLayout.CENTER);
    
//    this.pack();
   }
  
}
class consClienteTH extends Thread
{
  FichaClientes padre;
  public consClienteTH(FichaClientes papa)
  {
    padre=papa;
    this.start();
  }
    @Override
  public void run()
  {
   padre.verDatClien(padre.dtCli);
  }
}
