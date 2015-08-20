package gnu.chu.anjelica.ventas;
/**
 *
 * <p>Titulo: pdalbara </p>
 * <p>Descripción: Mantenimiento Albaranes de Ventas</p>
 * <p>Parámetros (Respetar mayúsculas y minúsculas):
 * - modPrecio (true/false)
 *   Controla si: Permite enviar Albaranes por FAX (por defecto, no se permite)
 *   Permite valorar los albaranes según precio de tarifa (botón valorar tarifas)
 *   Ver o modificar Precios.
 *   Modificar los campos:
 *    > dto pronto pago,
 *    > dto. Comercial
 *    > Valorado
 *    > cambiar sección a un cliente.
 *    
 * - ponPrecio (true/false).
 * Si esta variable es true permite poner precios y modificarlos en los albaranes
 * del dia.
 * - zona (String)
 *   Solo muestra albaranes de clientes de una zona dada
 * - conpedido (true/false)
 *   Permite introducir albaranes sin pedido
 * - admin (true/false)
 *   Permite todo lo anterior y algunas cosas especiales como.
 *   Permite modificar kilos de individuos.
 *   Permite ver albaranes de traspasos de albaranes
 *   Permite modificar albaranes de un ejercicio cerrado.
 *   Permite modificar albaranes que ya hayan sido facturados.
 *   Poner albarán como No facturar (conforme)
 * checkPedido: true o false, indica si se comprobara si exiten pedidos pendientes. Por defecto es true.
 *</p>
 * <p>Copyright: Copyright (c) 2005-2015
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los términos de la Licencia Pública General de GNU según es publicada por
 *  la Free Software Foundation, bien de la versión 2 de dicha Licencia
 *  o bien (según su elección) de cualquier versión posterior.
 *  Este programa se distribuye con la esperanza de que sea útil,
 *  pero SIN
 * NINGUNA GARANTIA, incluso sin la garantía MERCANTIL implícita
 *  o sin garantizar la CONVENIENCIA PARA UN PROPOSITO PARTICULAR.
 *  Véase la Licencia Pública General de GNU para más detalles.
 *  Debería haber recibido una copia de la Licencia Pública General junto con este programa.
 *  Si no ha sido así, escriba a la Free Software Foundation, Inc.,
 *  en 675 Mass Ave, Cambridge, MA 02139, EEUU.
 * </p>
 * @author chuchiP
 * @version 2.4 Incluido Etiquetas especificas de cliente (06-06-2009)
 *          2.3 Incluida serie de factura
 *          2.2 Incluida opción de generar despieces al Vuelo.
 *   NO permito introducir movimientos con lote igual a 0.
 *  Incluyo opción de Listar Albaranes en Modo gráfico.
 *
 */ 
import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.DatosIVA;
import gnu.chu.anjelica.almacen.DatIndiv;
import gnu.chu.anjelica.almacen.StkPartid;
import gnu.chu.anjelica.almacen.ActualStkPart;
import gnu.chu.anjelica.almacen.pdalmace;
import gnu.chu.anjelica.almacen.pdmotregu;
import gnu.chu.anjelica.compras.MantAlbComCarne;
import gnu.chu.anjelica.despiece.DatTrazFrame;
import gnu.chu.anjelica.despiece.DespVenta;
import gnu.chu.anjelica.despiece.listraza;
import gnu.chu.anjelica.despiece.utildesp;
import gnu.chu.anjelica.listados.etiqueta;
import gnu.chu.anjelica.menu;
import gnu.chu.anjelica.pad.*;
import gnu.chu.camposdb.*;
import gnu.chu.comm.BotonBascula;
import gnu.chu.controles.*;
import gnu.chu.hylafax.IFFax;
import gnu.chu.hylafax.SendFax;
import gnu.chu.interfaces.PAD;
import gnu.chu.mail.IFMail;
import gnu.chu.sql.DatosTabla;
import gnu.chu.sql.conexion;
import gnu.chu.sql.vlike;
import gnu.chu.utilidades.*;
import gnu.chu.winayu.ayuLote;
import gnu.hylafax.HylaFAXClient;
import gnu.inet.ftp.ServerResponseException;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

 
public class pdalbara extends ventanaPad  implements PAD
{  
  private boolean swPreguntaDestruir=false; // Pregunta si se debe destruir todo rastro de un albaran
  private final char IMPR_ALB_GRAF='A';
  private final char IMPR_ALB_TEXT='a';
  private final char IMPR_HOJA_TRA='T';
  /**
   * hoja Traz + Albaran
   */
  private final char IMPR_ALB_TRA='t'; 
  private final char IMPR_PALETS='P';
  private final char IMPR_ETIQUETAS='E';
  private char ultSelecImpr=IMPR_ALB_GRAF;
  
  private final String DSAL_IMPRE="I";
  private final String DSAL_FAX="F";
  private final String DSAL_EMAIL="M";
  
  double impDtoCom=0,impDtoPP=0;
  JMenuItem MFechaCab = new JMenuItem("Rest.Fec.Mvto");
  JMenuItem MAllFechaCab = new JMenuItem("Rest.Todas Fec.Mvto");
  private javax.swing.Timer temporizador; 
  int numPedPend=0;
  
  private boolean pesoManual=false; // Indica si se restara peso de cajas al darle enter en campo PESO.
  private boolean swUsaPalets=true;
  private boolean swCanti=false;
  private boolean isEmpPlanta=false;
  public final static String TABLACAB="v_albavec";
  public final static String TABLALIN="v_albavel";
  public final static String TABLAIND="v_albvenpar";
  public final static String VISTAIND="v_albventa_detalle";
  Cgrid jtHist=new Cgrid(4);
  CPanel Phist=new CPanel();
  DatosTabla dtHist,dtPedi;
  private int hisRowid=0;
  private BotonBascula botonBascula;
  private int nLiMaxEdit; // Usada para ver cuando una linea se puede borrar / Modificar al editar 
                          // un albaran de deposito con genero entregado.
  private String tablaCab="v_albavec";
  private String tablaLin="v_albavel";
  private String tablaInd="v_albvenpar";
  private String vistaInd="v_albventa_detalle";
  private boolean swProcesaEdit=false;
  private boolean confAlbDep=false;
  private boolean IMPALBTEXTO=false;
  DatTrazFrame datTrazFrame;
  private vlike lkDepo;
  JMenuItem verDatTraz = new JMenuItem("Ver Datos Trazabilidad");
  private boolean swTieneEnt=false;
  private int avsNume=0; // Numero de albaran Deposito
  private boolean swEntdepos=false; // Indica si estamos añadiendo/modificando un albaran de entrega de genero
//  boolean swPasLin  = false;
  public final static int NUMCOPIAS_ALBGRAF=2; // Numero de Copias de Alb. Grafico
  public final static int REVPRE_PCOSTO=1;
  public final static int REVPRE_PVALOR=2;
  public final static int  REVPRE_REVISA=3;
  public final static int ETICLIENTE=-1;
  private final static int FD_PRTARI=6; // Campo de Pr. tarifa en grid.
  private final static int FD_PRECIO=5; // Campo de Precio
  
  private final int JTDES_PROCODI=0;
  private final int JTDES_EMP=2;
  private final int JTDES_EJE=3;
  private final int JTDES_UNID=4;
  private final int JTDES_SERIE=5;
  private final int JTDES_LOTE=6;
  private final int JTDES_NUMIND=7;
  private final int JTDES_KILOS=8; // Campo Kilos en grid desglose individuos
  private final int JTDES_NUMLIN=9; // Numero Linea de desglose individuos
  /**
   * Codigo de Producto (1)
   */
  private final int JT_PROCODI=1; 
  /**
   * Numero Linea Albaran (0)
   */
  private final int JT_NULIAL=0; // Numero de Albaran.
  /**
   * Kilos Linea albaran (3)
   */
  private int JT_KILOS=3; 
  private  int JT_FECMVT=7; // Fecha Mvto.
  private int JT_NUMPALE=8; // Numero Pale
  private int JT_CODENV=9; // Codigo Envase
  private final int JTRES_PROCODI=0;
  private final int JTRES_KILOS=2;
  private final int JTRES_NL=3;
  private boolean swCompra=false,accCerra=false,inAddNew=false;
  private int accAno, accNume;
//  private boolean swCliSub=false;
  private int lastAvcNume,lastAvcAno,lastEmpCodi;
  private String lastAvcSerie;
  int avcImpres=0;
  boolean swAvisoAlbRep;
  sepAlbVen dgAlb;
  boolean isLock=false;
  CButton Bdesgl=new CButton(Iconos.getImageIcon("duplicar"));
  int colDesp=6;
//  despVentas despAlbar=null;
  DespVenta despVenta;
  int rowDesp;
  boolean PEDIRDESP=true; // Posibilibad de pedir despice al vuelo. Se busca en mant. configuracion
  int TIDCODI=0; // Tipo despiece por defecto.
  int NUMDECPRECIO=2; // Numero de decimales
  String FORMDECPRECIO=".9999";
  final int NUMDEC=2;  
  final String FORMDEC=".99";
  int tirCodi=0;
  boolean swAvisoDto=true;
  boolean P_CONPEDIDO=true; // Depende de campo sbe_albped en tabla subempresa
  ActualStkPart stkPart;
  prvPanel prv_codiE = new prvPanel();
  CLPedidVen copeve;
  String sqlAlb;
  boolean traspCont,traspReci;
  double antPrecio;
  IFFax ifFax=new IFFax();
  IFMail ifMail=new IFMail();
  ifregalm ifRegAlm;//=new CInternalFrame("Regularización Almacén",false,false,false,false); 
  actCabAlbFra datCab;
  utildesp utdesp;
  etiqueta etiq;
  ayuVenPro ayVePr = null;
  CPanel Pgrid = new CPanel(new GridBagLayout());
  CPanel Ppedido = new CPanel();
  CTabbedPane Ptab1 = new CTabbedPane();
  boolean  opVerdat=false;
  double prLiTar = 0;
//  private double tarIncPre;
  private boolean P_ADMIN = false;
  boolean modModif = true;
  boolean P_MODPRECIO = false; // Indica si se pueden Modificar Precios
  boolean P_PONPRECIO = false; // Indica si se pueden Poner Precios
  boolean P_CHECKPED= true; // Comprueba si hay pedidos nuevos.
  private boolean verPrecios=false;
  boolean graba = false;
  lialbven liAlb = null;
  listraza liTra = null;
  ayuLote ayuLot = null;
  int ALMACEN = pdalmace.getAlmacenPrincipal();
  int swGridDes = 0;
  String s;
  CLabel residuosL=new CLabel();
  CGridEditable jtRes=new CGridEditable(4)
  {
       @Override
    public void cambiaColumna(int col, int colNueva,int row)
    {
      try
      {
        if (col == 0)
          jtRes.setValor(pro_codresE.getNombArt(pro_codresE.getValorInt()),row, 1);
      }
      catch (SQLException k)
      {
        Error("Error al buscar Nombre Articulo", k);
      }
    }
@Override
    public int cambiaLinea(int row, int col)
    {
      return cambiaLinRes(row);
    }
  };
  CPanel Pprinc = new CPanel();
  CPanel Pcabe = new CPanel();
  CLabel Clabel1 = new CLabel();
  empPanel emp_codiE = new empPanel();
  CLabel cLabel1 = new CLabel();
  CTextField avc_anoE = new CTextField(Types.DECIMAL, "###9");
  CLabel cLabel2 = new CLabel();
  CComboBox avc_seriE = new CComboBox();
  CTextField avc_numeE = new CTextField(Types.DECIMAL, "#####9");
  CTextField avp_numuniE = new CTextField(Types.DECIMAL, "---9");
  CLabel cLabel3 = new CLabel();
  CTextField repiteIndE=new CTextField(Types.DECIMAL,"#9");
  cliAvcPanel cli_codiE = new cliAvcPanel()
  {
      @Override
    public void afterFocusLost(boolean error)
    {
      afterFocusLostCli(error);
    }
  };
  CLabel cLabel4 = new CLabel();
  CTextField avc_fecalbE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel5 = new CLabel();
  CTextField usu_nombE = new CTextField(Types.CHAR, "X", 15);
  CLabel cLabel6 = new CLabel();
  CTextField avc_fecemiE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel facturadoL = new CLabel();
  CTextField fvc_anoE = new CTextField(Types.DECIMAL, "###9");
  CTextField fvc_numeE = new CTextField(Types.DECIMAL, "#####9");
  CButtonMenu Bimpri = new CButtonMenu(Iconos.getImageIcon("print"));
  proPanel pro_codresE = new proPanel();
  CTextField pro_nomresE=new CTextField();
  CTextField avr_cantiE=new CTextField(Types.DECIMAL,"#,##9.99");
  
  proPanel pro_codiE = new proPanel()
  {
    @Override
    protected void despuesLlenaCampos()
    {
      try
      {
        pro_codicE.setText(pro_codiE.getText());
        pro_nombcE.setText(getNombArtCli(pro_codicE.getValorInt(),
                                         cli_codiE.getValorInt()));
        pro_codiE.setText(pro_codicE.getText());
        pro_nombE.setText(pro_nombcE.getText());
        jt.setValor(pro_codiE.getText(), 1);
        jt.setValor(pro_nombE.getText(), 2);
        jtDes.removeAllDatos();
        jtDes.setValor(pro_nombE.getText(),0,2);
        avp_numuniE.setValorInt(1);
        ArrayList v = new ArrayList();
        v.add(pro_codiE.getText().trim());
        v.add(pro_nombcE.getText().trim());
        v.add(EU.em_cod);
        v.add(avp_ejelotE.getText().trim());
        v.add("1");
        v.add(avp_serlotE.getText().trim());
        v.add(avp_numparE.getText().trim());
        v.add(avp_numindE.getText().trim());
        v.add(avp_cantiE.getText().trim());
        v.add("1");
       
        if (! checkIndiv(jt.getSelectedRow(),false) || avp_cantiE.getValorDec()==0 )
        {         
          jtDes.setLinea(v);
          jtDes.ponValores(0);
          swGridDes++;
          swCanti=avp_cantiE.getValorDec()==0;
          irGridDes( swCanti?JTDES_UNID: JTDES_LOTE);
          return;
        }
        jtDes.addLinea(v);

        avl_cantiE.setValorDec(avp_cantiE.getValorDec());
        avl_unidE.setValorDec(1);
//        jt.setValor(pro_nombE.getText(),2,0);
        jt.actualizarGrid();
        if ( swEntdepos)
        {
             if ( avc_numeE.getValorInt() == 0)
              {
                  avc_numeE.setValorInt(lkDepo.getInt("avc_nume"));
                  emp_codiE.setValorInt(lkDepo.getInt("emp_codi"));
                  avc_anoE.setValorInt(lkDepo.getInt("avc_ano"));
                  avc_seriE.setText(lkDepo.getString("avc_serie"));
                  avc_almoriE.setValor(lkDepo.getString("avc_almori"));
                  guardaCabEnt();
              }
        }
        guardaLinDes(jt.getSelectedRow());
        jt.mueveSigLinea(1);
        jtDes.removeAllDatos();
        actAcumLin();
      }
      catch (Exception k)
      {
        Error("Error Al poner Desglose Producto", k);
      }
    }
  };
  CTextField pro_nombE = new CTextField(Types.CHAR, "X", 45);
  CTextField avl_numlinE = new CTextField(Types.DECIMAL, "---9");
  CTextField avl_cantiE = new CTextField(Types.DECIMAL, "---,--9.99");
  CTextField avl_unidE = new CTextField(Types.DECIMAL, "---9");
  CTextField avl_fecaltE=new CTextField();
  CTextField avl_numpalE=new CTextField(Types.DECIMAL, "##9");
  CTextField avl_prvenE = new CTextField(Types.DECIMAL, "--,--9");
  CTextField tar_preciE = new CTextField(Types.DECIMAL, "#,##9.99");

  CGridEditable jt;
  CPanel Ppie = new CPanel();
  CLabel cLabel7 = new CLabel();
  CLinkBox tar_codiE = new CLinkBox();
  CLabel cLabel8 = new CLabel();
  CTextField numLinE = new CTextField(Types.DECIMAL, "##9");
  CLabel cLabel9 = new CLabel();
  CTextField kilosE = new CTextField(Types.DECIMAL, "----,--9.99");
  CLabel cLabel10 = new CLabel();
  CTextField impLinE = new CTextField(Types.DECIMAL, "----,--9");
  CLabel cLabel11 = new CLabel();
  CTextField avc_dtoppE = new CTextField(Types.DECIMAL, "#9.99");
  CLabel cLabel12 = new CLabel();
  CLabel rut_codiL = new CLabel();
  CTextField rut_codiE = new CTextField(Types.DECIMAL, "###9");
  CLabel rut_nombE = new CLabel();
  CTextField impDtoE = new CTextField(Types.DECIMAL, "---,--9.99");
  CLabel cLabel15 = new CLabel();
  CTextField avc_impalbE = new CTextField(Types.DECIMAL, "----,--9.99");
  CLabel cLabel16 = new CLabel();
  CTextField avc_impcobE = new CTextField(Types.DECIMAL, "----,--9.99");
  CCheckBox avc_cerraE = new CCheckBox("-1", "0");

  CGridEditable jtDes = new CGridEditable(10)
  {
    @Override
    public void cambiaColumna(int col, int colNueva,int row)
    {
      try
      {
        if (col == JTDES_PROCODI)
          jtDes.setValor(pro_codicE.getNombArtCli(pro_codicE.getValorInt(),
                          cli_codiE.getValorInt()), row, 1);
        if (col== JTDES_UNID)
        {
            if (pro_codiE.getEnvase()>0)                
                botonBascula.setPesoCajas(pro_codiE.getPesoCajas());
            botonBascula.setNumeroCajas(avp_numuniE.getValorInt());
        }
        if (col == JTDES_NUMIND)
              pro_numindE_focusLost();
      }
      catch (SQLException k)
      {
        Error("Error al buscar Nombre Articulo", k);
      }
    }
@Override
    public int cambiaLinea(int row, int col)
    {
      return cambiaLinDes(row);
    }
    @Override
    public boolean afterInsertaLinea(boolean insLinea)
    {
      if (swCanti)
      {   
          irGridLin();
          jt.mueveSigLinea(1);          
          return false;
      }
      pro_codicE.setText(pro_codiE.getText());
      jtDes.setValor(pro_codiE.getText(), jtDes.getSelectedRow(), 0);
      jtDes.setValor(pro_nombE.getText(), jtDes.getSelectedRow(), 1);
      jtDes.setValor("" + EU.em_cod, jtDes.getSelectedRow(), 2);
      jtDes.setValor("" + EU.ejercicio, jtDes.getSelectedRow(), 3);
      jtDes.setValor("1", jtDes.getSelectedRow(), 4);
      jtDes.ponValores(jtDes.getSelectedRow());
      return true;
    }
@Override
    public void afterCambiaLinea()
    {
     resetCambioIndividuo();
    }
  };
  CButton Birgrid = new CButton(Iconos.getImageIcon("reload"));
  CCheckBox opAgru = new CCheckBox();
//  CButton Baceptar = new CButton();
//  CButton Bcancelar = new CButton();
  boolean swActDesg = true;
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CCheckBox opValora = new CCheckBox();
  CComboBox opDispSalida = new CComboBox();
  CComboBox avc_valoraE = new CComboBox();
  CButton Bfincab = new CButton();
  proPanel pro_codicE = new proPanel();
  CTextField pro_nombcE = new CTextField(Types.CHAR, "X", 45);
  CTextField avp_emplotE = new CTextField(Types.DECIMAL, "#9");
  CTextField avp_ejelotE = new CTextField(Types.DECIMAL, "###9");
  CTextField avp_serlotE = new CTextField(Types.CHAR, "X", 1);
  CTextField avp_numparE = new CTextField(Types.DECIMAL, "####9");
  CTextField avp_numindE = new CTextField(Types.DECIMAL, "###9");
  CTextField avp_cantiE = new CTextField(Types.DECIMAL, "---,--9.99")
  {
      @Override
      public void afterProcesaEnter()
      {
          if (! avp_cantiE.hasCambio())
              return;          
          if (pesoManual)
          {
              avp_cantiE.setValorDec(avp_cantiE.getValorDec()-(avp_numuniE.getValorInt()*botonBascula.getPesoCajas()));
              avp_cantiE.resetCambio();
          }
      }
  };
  CTextField avp_numlinE = new CTextField(Types.DECIMAL, "##9");
  CLabel cLabel17 = new CLabel();
  CLabel cli_pobleE = new CLabel();
  CLabel cLabel13 = new CLabel();
  CTextField avc_dtocomE = new CTextField(Types.DECIMAL, "#9.99");
  CLabel dtComPL = new CLabel();
  CLabel cLabel19 = new CLabel();
  CLabel cLabel20 = new CLabel();
  CComboBox eti_codiE = new CComboBox();
  CCheckBox avc_confoE = new CCheckBox("-1","0");
  String P_ZONA = null;

  boolean PERMFAX=false;
  CCheckBox opCopia = new CCheckBox();
  CComboBox avc_almoriE = new CComboBox();
  CComboBox alm_codoriE = new CComboBox();
  CLabel alm_codoriL = new CLabel();
  CComboBox alm_coddesE = new CComboBox();
  CLabel alm_coddesL = new CLabel();
  CLabel avc_almoriL = new CLabel();
  CLabel cLabel24 = new CLabel();
  CTextField pvc_anoE = new CTextField(Types.DECIMAL, "###9");
  CTextField pvc_numeE = new CTextField(Types.DECIMAL, "#####9");
  CButton BbusPed = new CButton(Iconos.getImageIcon("find"));
  CPanel Pcabped = new CPanel();
  Cgrid jtLinPed = new Cgrid(11);
  CLabel cLabel25 = new CLabel();
//  CTextField pvc_fecentE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel cLabel26 = new CLabel();
  CTextField pvc_fecentE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CScrollPane jScrollPane1 = new CScrollPane();
  CLabel cLabel110 = new CLabel();
  CTextArea pvc_comenE = new CTextArea();
  CTextField pvc_fecpedE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel cLabel27 = new CLabel();
  CLabel cLabel111 = new CLabel();
  CTextField usu_nompedE = new CTextField(Types.CHAR,"X",20);
  CTextField pvc_horpedE = new CTextField(Types.DECIMAL,"99.99");
  CTextField cantE = new CTextField(Types.DECIMAL,"---9");
  CTextField nlE = new CTextField(Types.DECIMAL,"#9");
  CLabel usu_nombL = new CLabel();
  CLabel cLabel29 = new CLabel();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  CPanel Pped1 = new CPanel();
  CPanel cPanel2 = new CPanel();
  CLabel cLabel30 = new CLabel();
  CScrollPane jScrollPane2 = new CScrollPane();
  CTextArea avc_obserE = new CTextArea();
  CCheckBox opAgrPrv = new CCheckBox();
  CCheckBox opAgrFecha = new CCheckBox();
  CButton BmvReg = new CButton(Iconos.getImageIcon("run"));
  CLabel cLabel31 = new CLabel();
  CComboBox div_codiE = new CComboBox();
  CButton Bdespiece = new CButton(Iconos.getImageIcon("cuchillo"));
  CLabel lockE = new CLabel();
  CLabel printE = new CLabel();
  CPanel PotroDat = new CPanel();
  CLabel cLabel33 = new CLabel();
  sbePanel sbe_codiE = new sbePanel();
  CLabel cLabel210 = new CLabel();
  CLabel sbe_nombL;
  private CTextField fvc_serieE = new CTextField(Types.CHAR,"X");
  private CButton BValTar = new CButton();
  CLabel avc_revpreL = new CLabel();
  CComboBox avc_revpreE = new CComboBox();
  private CComboBox avc_deposE = new CComboBox();
  private CComboBox verDepoC = new CComboBox();

  public pdalbara(EntornoUsuario eu, Principal p)
  {
    this(eu, p, new Hashtable());
  }

  public pdalbara(EntornoUsuario eu, Principal p, Hashtable<String,String> ht)
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
          P_MODPRECIO = Boolean.valueOf(ht.get("modPrecio"));
        if (ht.get("ponPrecio") != null)
          P_PONPRECIO = Boolean.parseBoolean(ht.get("ponPrecio"));
        if (ht.get("checkPedido")!=null)
            P_CHECKPED=Boolean.parseBoolean(ht.get("checkPedido"));
        if (ht.get("admin") != null)
          P_ADMIN = Boolean.parseBoolean(ht.get("admin"));
        if (ht.get("zona") != null)
          P_ZONA = ht.get("zona");
        if (ht.get("conPedido") != null)
          P_CONPEDIDO = Boolean.parseBoolean(ht.get("conPedido"));

      }
     
      setTitulo("Mant. Albaranes Ventas");
      if (jf.gestor.apuntar(this))
        jbInit();
      else
        setErrorInit(true);
    }
    catch (Exception e)
    {
      Logger.getLogger(pdalbara.class.getName()).log(Level.SEVERE, null, e);
      setErrorInit(true);
    }
  }

  public pdalbara(menu p, EntornoUsuario eu, Hashtable<String,String> ht)
  {
    EU = eu;
    vl = p.getLayeredPane();
    eje = false;
    jf = null;
    try
    {
      if (ht != null)
      {
        if (ht.get("modPrecio") != null)
          P_MODPRECIO = Boolean.parseBoolean(ht.get("modPrecio"));
        if (ht.get("ponPrecio") != null)
          P_PONPRECIO = Boolean.parseBoolean(ht.get("ponPrecio"));
        if (ht.get("admin") != null)
          P_ADMIN = Boolean.parseBoolean(ht.get("admin"));
        if (ht.get("zona") != null)
          P_ZONA = ht.get("zona");
        if (ht.get("conPedido") != null)
          P_CONPEDIDO = Boolean.parseBoolean(ht.get("conPedido"));
      }
      
      setTitulo("Mant Albaranes Ventas");

      jbInit();
    }
    catch (Exception e)
    {
      Logger.getLogger(pdalbara.class.getName()).log(Level.SEVERE, null, e);
      setErrorInit(true);
    }
  }
  /**
   * Pone a disabled los campos indice del albaran
   */
    private void disableCamposIndice() {
        avc_numeE.setEnabled(false);
        avc_seriE.setEnabled(false);
        emp_codiE.setEnabled(false);
        avc_almoriE.setEnabled(false);
        avc_deposE.setEnabled(false);
    }

    private void jbInit() throws Exception
    {
        if (P_ADMIN)
        {
            P_MODPRECIO = true;
            P_CONPEDIDO=false;
        }
        if (P_MODPRECIO)
            PERMFAX=true;
        iniciarFrame();
        this.setSize(new Dimension(701, 535));
        setVersion("2015-08-19" + (P_MODPRECIO ? "-CON PRECIOS-" : "")
                + (P_ADMIN ? "-ADMINISTRADOR-" : ""));
        strSql = getStrSql(null, null);

        statusBar = new StatusBar(this);
        nav = new navegador(this, dtCons, false, navegador.NORMAL);
      
        
        ifFax.setVisible(false);
        ifFax.setIconifiable(false);
        ifMail.setVisible(false);
        ifMail.setIconifiable(false);

      
        Bdesgl.setMargin(new Insets(0, 0, 0, 0));
        Bdesgl.setPreferredSize(new Dimension(24, 24));
        Bdesgl.setMaximumSize(new Dimension(24, 24));
        Bdesgl.setMinimumSize(new Dimension(24, 24));

        Bdesgl.setToolTipText("Desglosar Albaranes");
        // @todo poner este CTextField en el Panel.
        repiteIndE.setValorInt(2); // No permitir crotales iguales.
//        lockE.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        lockE.setBounds(new Rectangle(644, 75, 33, 21));
        printE.setHorizontalAlignment(SwingConstants.CENTER);
        printE.setHorizontalTextPosition(SwingConstants.CENTER);
        printE.setBounds(new Rectangle(446, 2, 41, 19));
        PotroDat.setLayout(null);
        cLabel33.setText("SubEmpr");
        cLabel33.setBounds(new Rectangle(6, 76, 62, 18));
        BmvReg.setText("Mvto.Reg (F9)");
        opAgru.setToolTipText("Ver Lineas Agrupadas");
        sbe_codiE.setBounds(new Rectangle(58, 76, 43, 18));
        cLabel210.setBounds(new Rectangle(485, 3, 101, 17));
        cLabel210.setText("Disp.Salida");
        cLabel210.setHorizontalTextPosition(SwingConstants.CENTER);
        cLabel210.setHorizontalAlignment(SwingConstants.CENTER);
        cLabel210.setOpaque(true);
        cLabel210.setForeground(Color.white);
        cLabel210.setBackground(Color.blue);
        opValora.setToolTipText("Listar Alb. Valorado ?");
        statusBar.add(Bdesgl, new GridBagConstraints(9, 0, 1, 2, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.VERTICAL,
                new Insets(0, 5, 0, 0), 0, 0));
        botonBascula = new BotonBascula(EU, this);
        botonBascula.setPreferredSize(new Dimension(50, 24));
        botonBascula.setMinimumSize(new Dimension(50, 24));
        botonBascula.setMaximumSize(new Dimension(50, 24));
        statusBar.add(botonBascula, new GridBagConstraints(8, 0, 1, 2, 0.0, 0.0, GridBagConstraints.EAST,
            GridBagConstraints.VERTICAL,
            new Insets(0, 5, 0, 0), 0, 0));
        
        
        avp_cantiE.setEditable( P_ADMIN);

        ArrayList v1 = new ArrayList();
        v1.add("Prod"); // 0
        v1.add("Descr."); // 1
        v1.add("Emp"); // 2
        v1.add("Ejer"); // 3
        v1.add("Unid."); // 4
        v1.add("Serie"); // 5
        v1.add("Lote"); //6
        v1.add("N.Ind"); // 7
        v1.add("Kilos"); // 8
        v1.add("NL");
        jtDes.setCabecera(v1);
        jtDes.setAjustarGrid(true);
        jtDes.setAnchoColumna(new int[]{50, 200, 35, 40, 40, 35, 40, 40, 60,40});
        jtDes.setAlinearColumna(new int[]{2, 0, 2, 2, 2, 0, 2, 2, 2,2});
        jtDes.setFormatoColumna(8, "---,--9.99");
        jtDes.setFormatoColumna(4, "--9");
        pro_nombcE.setEnabled(false);

        avp_emplotE.setValorDec(EU.em_cod);
        avp_emplotE.setEnabled(false);
        avp_ejelotE.setValorDec(EU.ejercicio);
        avp_serlotE.setText("A");
        avp_serlotE.setMayusc(true);
        avp_numuniE.setValorDec(1);
        ArrayList vc1 = new ArrayList();

        avp_ejelotE.setToolTipText("F3 Cons. Lotes Disponibles");
        avp_numuniE.setToolTipText("F3 Cons. Lotes Disponibles");
        avp_serlotE.setToolTipText("F3 Cons. Lotes Disponibles");
        avp_numparE.setToolTipText("F3 Cons. Lotes Disponibles");
        avp_numlinE.setValorInt(0);
        pro_codicE.setEditable(false);
        pro_nombcE.setEditable(false);
        avp_numlinE.setEnabled(false);
        avp_cantiE.setLeePesoBascula(botonBascula);
        botonBascula.setNumeroCajas(0);
        botonBascula.setPesoCajas(0);
        
       
        vc1.add(pro_codicE.getTextField()); // 0
        
        vc1.add(pro_nombcE); // 1
        vc1.add(avp_emplotE); // 2
        vc1.add(avp_ejelotE); // 3
        vc1.add(avp_numuniE); // 4
        vc1.add(avp_serlotE); // 5
        vc1.add(avp_numparE); // 6
        vc1.add(avp_numindE); // 7
        vc1.add(avp_cantiE); // 8
        vc1.add(avp_numlinE); // 9
        jtDes.setCampos(vc1);
       
        jtDes.setPonValoresInFocus(false);

        opAgru.setSelected(true);
        conf_jtLinPed(jtLinPed);

        avc_deposE.addItem("Normal","N");
        avc_deposE.addItem("Deposito","D");
        avc_deposE.addItem("Entregado","E");
        llenaVerDepos();
        
        pro_codiE.getFieldProCodi().setToolTipText("F3 Buscar por nombre. Doble Click Restaurar Nombre Articulo");
        Pprinc.setLayout(gridBagLayout1);
        Pcabe.setBorder(BorderFactory.createRaisedBevelBorder());
        Pcabe.setMaximumSize(new Dimension(595, 98));
        Pcabe.setMinimumSize(new Dimension(595, 98));
        Pcabe.setPreferredSize(new Dimension(595, 98));
        Pcabe.setLayout(null);
        Clabel1.setText("Empresa");
        Clabel1.setBounds(new Rectangle(2, 4, 51, 16));
        emp_codiE.setBounds(new Rectangle(52, 4, 43, 16));
        cLabel1.setRequestFocusEnabled(true);
        cLabel1.setText("Año");
        cLabel1.setBounds(new Rectangle(101, 4, 24, 16));
        avc_anoE.setBounds(new Rectangle(123, 4, 38, 16));
        cLabel2.setText("Albaran");
        cLabel2.setBounds(new Rectangle(167, 4, 47, 16));
        avc_seriE.setBounds(new Rectangle(218, 4, 40, 16));
//    avc_numeE.setDisabledTextColor(new Color(0, 0, 128));
//    avc_numeE.setDisabledTextColor(new Color(120, 120, 120));
        avc_numeE.setBounds(new Rectangle(260, 4, 58, 16));
        cLabel3.setText("Cliente");
        cLabel3.setBounds(new Rectangle(3, 22, 43, 16));
        cli_codiE.setBounds(new Rectangle(51, 22, 384, 16));
        cLabel4.setText("Fec.Alb");
        cLabel4.setBounds(new Rectangle(321, 4, 49, 16));
        avc_fecalbE.setBounds(new Rectangle(369, 4, 75, 16));
        cLabel5.setText("Usuario");
        cLabel5.setBounds(new Rectangle(1, 80, 45, 16));
        usu_nombE.setBounds(new Rectangle(49, 80, 123, 16));
        cLabel6.setText("Fec.Emision");
        cLabel6.setBounds(new Rectangle(186, 80, 68, 16));
        avc_fecemiE.setBounds(new Rectangle(254, 80, 75, 16));
        facturadoL.setMinimumSize(new Dimension(35, 15));
        facturadoL.setText("N.Fact");
        facturadoL.setBounds(new Rectangle(415, 40, 40, 15));
        fvc_anoE.setCeroIsNull(false);
        fvc_numeE.setCeroIsNull(false);
        fvc_anoE.setBounds(new Rectangle(475, 38, 37, 16));
        fvc_numeE.setBounds(new Rectangle(515, 38, 56, 16));
        Bimpri.setMargin(new Insets(0, 0, 0, 0));
        Bimpri.setText("F9");
        Bimpri.setBounds(new Rectangle(331, 21, 65, 17));
        Bimpri.setToolTipText("Imprimir Albaran");

        Ppie.setBorder(BorderFactory.createLoweredBevelBorder());
        Ppie.setMaximumSize(new Dimension(550, 40));
        Ppie.setMinimumSize(new Dimension(550, 40));
        Ppie.setPreferredSize(new Dimension(550, 40));
        Ppie.setQuery(false);
        Ppie.setLayout(null);
        cLabel7.setRequestFocusEnabled(true);
        cLabel7.setText("Tarifa");
        cLabel7.setBounds(new Rectangle(9, 57, 35, 16));
        tar_codiE.setAncTexto(25);
        tar_codiE.setBounds(new Rectangle(51, 57, 157, 18));
        cLabel8.setPreferredSize(new Dimension(50, 15));
        cLabel8.setText("N. Lineas");
        cLabel8.setBounds(new Rectangle(4, 2, 56, 17));
        numLinE.setMinimumSize(new Dimension(4, 17));
        numLinE.setOpaque(true);
        numLinE.setPreferredSize(new Dimension(17, 17));
        numLinE.setBounds(new Rectangle(62, 2, 29, 17));
        cLabel9.setText("Kilos");
        cLabel9.setBounds(new Rectangle(94, 2, 31, 17));
        kilosE.setBounds(new Rectangle(130, 2, 61, 17));
        cLabel10.setText("Imp. Lineas");
        cLabel10.setBounds(new Rectangle(198, 2, 65, 17));
        impLinE.setBounds(new Rectangle(266, 2, 68, 17));
        cLabel11.setText("Dto PP");
        cLabel11.setBounds(new Rectangle(412, 57, 39, 16));
        avc_dtoppE.setToolTipText("Dto. Pronto Pago");
        avc_dtoppE.setValorDec(0.0);
        avc_dtoppE.setBounds(new Rectangle(452, 57, 39, 16));
        BValTar.setText("Valor Tar.");
        BValTar.setToolTipText("<F5> Valorar Albarán según precios de Tarifa");

        BValTar.setBounds(new Rectangle(315, 5, 110, 20));
        BValTar.setMaximumSize(new Dimension(110, 20));
        BValTar.setMinimumSize(new Dimension(110, 20));
        BValTar.setPreferredSize(new Dimension(110, 20));
        if (!P_MODPRECIO) {
            BValTar.setEnabled(false);
            avc_dtoppE.setEnabled(false);
            avc_dtocomE.setEnabled(false);
            avc_valoraE.setEnabled(false);
            avc_revpreE.setEnabled(false);
            sbe_codiE.setEnabled(false);
        }
        cLabel12.setText("%");
        cLabel12.setBounds(new Rectangle(491, 57, 15, 16));
        rut_codiL.setText("Ruta");
        rut_codiL.setBounds(new Rectangle(0, 160, 25, 15));
        rut_codiE.setBounds(new Rectangle(35, 160, 45, 15));
        rut_codiL.setBounds(new Rectangle(211, 57, 29, 16));
        rut_codiE.setBounds(new Rectangle(237, 57, 44, 16));
        rut_nombE.setBackground(Color.orange);
        rut_nombE.setOpaque(true);
        rut_nombE.setBounds(new Rectangle(85, 160, 125, 15));
        rut_nombE.setBounds(new Rectangle(283, 57, 125, 16));
        impDtoE.setBounds(new Rectangle(402, 2, 69, 17));
        cLabel15.setText("Imp.Total");
        cLabel15.setBounds(new Rectangle(1, 21, 58, 17));
        avc_impalbE.setTipoCampo(3);
        avc_impalbE.setFormato("----,--9.99");
        avc_impalbE.setText("");
        avc_impalbE.setBounds(new Rectangle(53, 21, 68, 17));
        cLabel16.setRequestFocusEnabled(true);
        cLabel16.setText("Imp. Cobrado");
        cLabel16.setBounds(new Rectangle(133, 21, 79, 17));
        avc_impcobE.setBounds(new Rectangle(211, 21, 69, 17));
        avc_cerraE.setMaximumSize(new Dimension(74, 23));
        avc_cerraE.setText("Cerrado");
        avc_cerraE.setBounds(new Rectangle(330, 57, 76, 18));
        avc_cerraE.setBounds(new Rectangle(330, 57, 76, 16));
        jtDes.setMaximumSize(new Dimension(534, 77));
        jtDes.setMinimumSize(new Dimension(534, 77));
        jtDes.setPreferredSize(new Dimension(534, 77));
        jtDes.setBuscarVisible(false);
        Birgrid.setBounds(new Rectangle(281, 21, 47, 17));
        Birgrid.setToolTipText("Ir a Grid");
        Birgrid.setMargin(new Insets(0, 0, 0, 0));
        Birgrid.setText("F2");
        opAgru.setMargin(new Insets(0, 0, 0, 0));
        opAgru.setText("Agrupar Lineas");
        opAgru.setBounds(new Rectangle(187, 5, 128, 17));
        Baceptar.setBounds(new Rectangle(428, 2, 112, 24));
        Baceptar.setMaximumSize(new Dimension(110, 28));
        Baceptar.setMinimumSize(new Dimension(110, 28));
        Baceptar.setPreferredSize(new Dimension(110, 28));
        Bcancelar.setBounds(new Rectangle(551, 2, 112, 24));
        Bcancelar.setMaximumSize(new Dimension(110, 28));
        Bcancelar.setMinimumSize(new Dimension(110, 28));
        Bcancelar.setPreferredSize(new Dimension(110, 28));
        Baceptar.setText("Aceptar (F4)");
        opValora.setMargin(new Insets(0, 0, 0, 0));
        opValora.setText("Valorado");
        opValora.setBounds(new Rectangle(398, 21, 85, 16));
        opDispSalida.setBounds(new Rectangle(485, 20, 101, 16));
        avc_valoraE.setText("Valorado");
        avc_valoraE.setBounds(new Rectangle(458, 22, 95, 16));
        Bfincab.setBounds(new Rectangle(540, 66, 1, 1));
        cLabel17.setText("Pobl. Cliente");
        cLabel17.setBounds(new Rectangle(31, 39, 71, 16));
        cli_pobleE.setBackground(Color.orange);

        cli_pobleE.setOpaque(true);
        cli_pobleE.setBounds(new Rectangle(103, 39, 306, 16));
        cLabel13.setToolTipText("");
        cLabel13.setVerifyInputWhenFocusTarget(true);
        cLabel13.setText("Dto.Com");
        cLabel13.setBounds(new Rectangle(574, 38, 55, 15));
        avc_dtocomE.setToolTipText("Descuento Comercial");
        avc_dtocomE.setBounds(new Rectangle(633, 38, 39, 16));
        dtComPL.setBounds(new Rectangle(674, 38, 15, 16));
        dtComPL.setText("%");
        cLabel19.setText("Imp.Dtos");
        cLabel19.setBounds(new Rectangle(340, 2, 63, 17));
        cLabel20.setBackground(Color.blue);
        cLabel20.setForeground(Color.white);
        cLabel20.setOpaque(true);
        cLabel20.setHorizontalAlignment(SwingConstants.CENTER);
        cLabel20.setHorizontalTextPosition(SwingConstants.CENTER);
        cLabel20.setText("Tipo Etiqueta");
        cLabel20.setBounds(new Rectangle(588, 3, 101, 17));
        eti_codiE.setPreferredSize(new Dimension(202, 24));
        eti_codiE.setBounds(new Rectangle(588, 20, 101, 17));
        avc_confoE.setSelected(true);
        avc_confoE.setText("Facturar");
        avc_confoE.setBounds(new Rectangle(590, 20, 76, 16));
        
        avc_confoE.setEnabled(P_ADMIN);
        opCopia.setSelected(true);
        opCopia.setText("Imprimir Copia a Papel");
        opCopia.setBounds(new Rectangle(293, 20, 165, 17));
        avc_almoriE.setBounds(new Rectangle(390, 75, 120, 20));
        avc_almoriE.setPreferredSize(new Dimension(180, 20));
        alm_codoriL.setText("Alm. Origen");
        alm_codoriL.setBounds(new Rectangle(2, 105, 80, 18));
        alm_codoriE.setBounds(new Rectangle(85, 105, 200, 18));
        alm_codoriE.setEnabled(false);
        alm_coddesL.setBounds(new Rectangle(300, 105, 80, 18));
        alm_coddesE.setBounds(new Rectangle(385, 105, 200, 18));
        alm_coddesE.setEnabled(false);
        alm_coddesL.setText("Alm. Destino");
        avc_almoriL.setBounds(new Rectangle(336, 75, 53, 18));
        residuosL.setText("Residuos");
        
        residuosL.setOpaque(true);
        residuosL.setBackground(Color.CYAN);
        residuosL.setHorizontalAlignment(SwingConstants.CENTER);
        residuosL.setFont(new Font("Dialog", Font.BOLD, 12));
        residuosL.setBounds(new Rectangle(275, 125, 75, 16));
        ArrayList<String> vc2=new ArrayList(3);
        vc2.add("Producto");
        vc2.add("Nombre");
        vc2.add("Kilos");
        vc2.add("NL");
        jtRes.setCabecera(vc2);
        jtRes.setAnchoColumna(new int[]{80,250,70,30});
        jtRes.setAlinearColumna(new int[]{2,0,2,2});
        jtRes.setFormatoColumna(2, "#,##9.99");
         jtRes.setFormatoColumna(3, "#9");
        jtRes.setAjustarGrid(true);
        CTextField nlE=new CTextField(Types.DECIMAL,"#9");
        nlE.setValorInt(0);
        nlE.setEnabled(false);
        pro_nomresE.setEnabled(false);
        Vector vca2=new Vector();
        vca2.add(pro_codresE.getTextField());
        vca2.add(pro_nomresE);
        vca2.add(avr_cantiE);
        vca2.add(nlE);
        jtRes.setCampos(vca2);
        jtRes.setBounds(new Rectangle(2, 145, 550, 115));
        
        
        avc_almoriL.setText("Almacen");
        cLabel24.setText("Pedido");
        cLabel24.setBounds(new Rectangle(1, 1, 40, 16));
        BbusPed.setBounds(new Rectangle(149, 1, 18, 17));
        BbusPed.setMargin(new Insets(0, 0, 0, 0));
        BbusPed.setFocusable(false);
        Pcabped.setBorder(BorderFactory.createLoweredBevelBorder());
        Pcabped.setMaximumSize(new Dimension(617, 60));
        Pcabped.setMinimumSize(new Dimension(617, 60));
        Pcabped.setPreferredSize(new Dimension(617, 60));
        Pcabped.setLayout(null);

        Ppedido.setLayout(gridBagLayout2);
        cLabel25.setBounds(new Rectangle(333, 20, 67, 16));
        cLabel25.setText("Fec.Entrega");
//    pvc_fecentE.setBounds(new Rectangle(396, 20, 75, 16));
        cLabel26.setText("Fec.Entrega");
        cLabel26.setBounds(new Rectangle(4, 3, 67, 16));
        cLabel110.setText("Comentario");
        cLabel110.setBounds(new Rectangle(2, 21, 68, 18));
        cLabel27.setText("Fecha Ped.");
        cLabel27.setBounds(new Rectangle(148, 3, 59, 16));
        cLabel111.setText("Cant");
        cLabel111.setBounds(new Rectangle(531, 3, 33, 16));
        cantE.setToolTipText("Cantidad de piezas del pedido");
        cantE.setBounds(new Rectangle(566, 3, 43, 16));
        nlE.setToolTipText("Numero Lineas del pedido");
        nlE.setBounds(new Rectangle(502, 3, 28, 16));
        usu_nombL.setText("Usuario");
        usu_nombL.setBounds(new Rectangle(328, 3, 49, 16));
        cLabel29.setText("NL");
        cLabel29.setBounds(new Rectangle(478, 3, 21, 16));
        jtLinPed.setMaximumSize(new Dimension(636, 259));
        jtLinPed.setMinimumSize(new Dimension(636, 259));
        jtLinPed.setPreferredSize(new Dimension(636, 259));
        jScrollPane1.setBounds(new Rectangle(71, 21, 417, 35));
        usu_nompedE.setBounds(new Rectangle(373, 3, 100, 16));
        pvc_horpedE.setBounds(new Rectangle(290, 3, 35, 16));
        pvc_fecpedE.setBounds(new Rectangle(208, 3, 81, 16));
        pvc_fecentE.setBounds(new Rectangle(71, 3, 75, 16));
        Pped1.setBorder(BorderFactory.createLineBorder(Color.black));

        Pped1.setBounds(new Rectangle(489, 3, 181, 19));
        Pped1.setLayout(null);
        pvc_numeE.setBounds(new Rectangle(79, 1, 65, 16));
        pvc_anoE.setBounds(new Rectangle(38, 1, 38, 16));
        cPanel2.setForeground(Color.black);
        cPanel2.setBorder(BorderFactory.createLineBorder(Color.black));
        cPanel2.setMaximumSize(new Dimension(110, 32));
        cPanel2.setMinimumSize(new Dimension(110, 32));
        cPanel2.setOpaque(true);
        cPanel2.setPreferredSize(new Dimension(110, 32));
        fvc_serieE.setMayusc(true);
        cPanel2.setLayout(null);
        cLabel30.setText("Comentarios");
        cLabel30.setBounds(new Rectangle(-1, 7, 76, 14));
        jScrollPane2.setBounds(new Rectangle(75, 6, 538, 66));
        opAgrPrv.setToolTipText("Agrupar Productos con Diferentes Proveedores");
        opAgrPrv.setVerifyInputWhenFocusTarget(true);
        opAgrPrv.setText("Agrupar Proveed.");
        opAgrPrv.setBounds(new Rectangle(488, 21, 137, 17));
        opAgrFecha.setBounds(new Rectangle(488, 38, 137, 17));
        opAgrFecha.setText("Agrupar Fec.Cad");
        opAgrFecha.setVerifyInputWhenFocusTarget(true);
        opAgrFecha.setToolTipText("Agrupar Productos con Diferente Fec.Cad.");
        BmvReg.setBounds(new Rectangle(7, 5, 126, 18));
        BmvReg.setMargin(new Insets(0, 0, 0, 0));
        BmvReg.setToolTipText("Insertar Mvto. de Regularizacion (F9)");
        cLabel31.setText("Divisa");
        cLabel31.setBounds(new Rectangle(521, 56, 43, 16));

        div_codiE.setBounds(new Rectangle(558, 56, 120, 16));
        Bdespiece.setPreferredSize(new Dimension(32, 24));
        Bdespiece.setToolTipText("Generar Despieces (F10)");
        Bdespiece.setMargin(new Insets(0, 0, 0, 0));
        Bdespiece.setBounds(new Rectangle(136, 5, 42, 17));

        avc_revpreL.setText("Revisar Precios");
        avc_deposE.setBounds(new Rectangle(215, 57, 105, 18));
        verDepoC.setBounds(new Rectangle(530, 75, 105, 20));
        verDepoC.setPreferredSize(new Dimension(140,20));
        avc_revpreL.setBounds(new Rectangle(351, 80, 90, 16));
        avc_revpreE.setBounds(new Rectangle(443, 80, 121, 16));

        Ppie.add(cLabel10, null);
        Ppie.add(impDtoE, null);
        Ppie.add(numLinE, null);
        Ppie.add(cLabel8, null);
        Ppie.add(cLabel9, null);
        Ppie.add(impLinE, null);
        Ppie.add(cLabel19, null);
        Ppie.add(kilosE, null);
        Ppie.add(cLabel15, null);
        Ppie.add(avc_impalbE, null);
        Ppie.add(cLabel16, null);
        Ppie.add(avc_impcobE, null);
        Ppie.add(Birgrid, null);
        Ppie.add(eti_codiE, null);
        Ppie.add(cLabel20, null);
        Ppie.add(opDispSalida, null);
        Ppie.add(cLabel210, null);
        Ppie.add(opValora, null);
        Ppie.add(Bimpri, null);
        Ptab1.setMaximumSize(new Dimension(538, 381));
        Ptab1.setMinimumSize(new Dimension(538, 381));
        Ptab1.setPreferredSize(new Dimension(538, 381));

        Pprinc.add(Ptab1, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        Ptab1.addTab("Albaran", Pgrid);
        Ptab1.addTab("Pedidos", Ppedido);
        Ptab1.addTab("Historico",Phist);
        Pcabped.add(cLabel26, null);
        Pcabped.add(cLabel110, null);
        Pcabped.add(cLabel29, null);
        Pcabped.add(nlE, null);
        Pcabped.add(cLabel111, null);
        Pcabped.add(cantE, null);
        Pcabped.add(pvc_fecentE, null);
        Pcabped.add(cLabel27, null);
        Pcabped.add(pvc_fecpedE, null);
        Pcabped.add(pvc_horpedE, null);
        Pcabped.add(usu_nombL, null);
        Pcabped.add(usu_nompedE, null);

        Pcabped.add(jScrollPane1, null);
        Pcabped.add(opAgrPrv, null);
        Pcabped.add(opAgrFecha, null);
        Ptab1.add(PotroDat, "Otros");
        PotroDat.add(jScrollPane2, null);
        PotroDat.add(alm_codoriL, null);
        PotroDat.add(alm_codoriE, null);
        PotroDat.add(alm_coddesL, null);
        PotroDat.add(residuosL,null);
        PotroDat.add(jtRes,null);
        PotroDat.add(alm_coddesE, null);
        PotroDat.add(cLabel30, null);
        PotroDat.add(usu_nombE, null);
        PotroDat.add(cLabel5, null);
        PotroDat.add(avc_fecemiE, null);
        PotroDat.add(cLabel6, null);
        PotroDat.add(avc_revpreL, null);
        PotroDat.add(avc_revpreE, null);
        PotroDat.add(rut_codiL, null);
        PotroDat.add(rut_codiE, null);
        PotroDat.add(rut_nombE, null);
        jScrollPane2.getViewport().add(avc_obserE, null);
        jScrollPane1.getViewport().add(pvc_comenE, null);
        conecta();
        iniciar(this);
        Ppedido.add(jtLinPed,
                new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH,
                new Insets(3, 0, 2, 0), 0, 0));
        Ppedido.add(Pcabped,
                new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE,
                new Insets(1, 0, 0, 0), 12, 0));
        Pgrid.add(jt,
                new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

        Pgrid.add(jtDes,
                new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        Pprinc.add(Ppie,
                new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        Pprinc.add(Pcabe,
                new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 4, 0, 4), 0, 0));
        this.getContentPane().add(Pprinc, BorderLayout.CENTER);
        Pcabe.add(verDepoC, null);
        Pcabe.add(avc_deposE, null);
        Pcabe.add(fvc_serieE, null);
        Pcabe.add(Clabel1, null);
        Pcabe.add(emp_codiE, null);
        Pcabe.add(cli_codiE, null);
        Pcabe.add(cLabel3, null);
        Pcabe.add(tar_codiE, null);
        Pcabe.add(cLabel7, null);
        Pcabe.add(avc_valoraE, null);
        Pcabe.add(Bfincab, null);
        Pcabe.add(facturadoL, null);
        Pcabe.add(fvc_anoE, null);
        Pcabe.add(cLabel17, null);
        Pcabe.add(cli_pobleE, null);
        Pcabe.add(avc_dtoppE, null);
        Pcabe.add(cLabel11, null);
        Pcabe.add(cLabel12, null);
        Pcabe.add(avc_almoriE, null);
        Pped1.add(pvc_numeE, null);
        Pped1.add(cLabel24, null);
        Pped1.add(pvc_anoE, null);
        Pped1.add(BbusPed, null);
        sbe_nombL.setBounds(new Rectangle(104, 76, 229, 16));
        fvc_serieE.setBounds(new Rectangle(456, 38, 18, 16));
        Pcabe.add(sbe_nombL, null);
        Pcabe.add(avc_fecalbE, null);
        Pcabe.add(cLabel4, null);
        Pcabe.add(printE, null);
        Pcabe.add(avc_numeE, null);
        Pcabe.add(cLabel1, null);
        Pcabe.add(avc_anoE, null);
        Pcabe.add(cLabel2, null);
        Pcabe.add(avc_seriE, null);
        Pcabe.add(cLabel33, null);
        Pcabe.add(sbe_codiE, null);
        Pcabe.add(avc_cerraE, null);
        Pcabe.add(lockE, null);
        Pcabe.add(avc_confoE, null);
        Pcabe.add(avc_almoriL, null);
        
        Pcabe.add(fvc_numeE, null);
        Pcabe.add(div_codiE, null);
        Pcabe.add(cLabel31, null);
        Pcabe.add(avc_dtocomE, null);
        Pcabe.add(cLabel13, null);
        Pcabe.add(dtComPL, null);
        Pcabe.add(Pped1, null);
        Pprinc.add(cPanel2,
                new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        cPanel2.add(BmvReg, null);
        cPanel2.add(Bdespiece, null);
        cPanel2.add(opAgru, null);
        cPanel2.add(BValTar, null);
        cPanel2.add(Bcancelar, null);
        cPanel2.add(Baceptar, null);
        confGridHist();
        Phist.setLayout(new java.awt.BorderLayout());
        Phist.add(jtHist, java.awt.BorderLayout.CENTER);
        
    }
    private void confGridHist()
    {
       ArrayList vh=new ArrayList();
       vh.add("Fecha/Hora");
       vh.add("Usuario");
       vh.add("Comentario");
       vh.add("Id");
       jtHist.setCabecera(vh);
       jtHist.setAjustarGrid(true);
       jtHist.setAlinearColumna(new int[]{1,0,0,2});
       jtHist.setAnchoColumna(new int[]{90,120,200,40});
       jtHist.setFormatoColumna(3, "####9");
       jtHist.setFormatoColumna(0,"dd-MM-yyyy HH:mm");
    }
    private void llenaVerDepos() {
        verDepoC.addItem("Original", "O");
        verDepoC.addItem("Servido", "S");
        verDepoC.addItem("Pendiente", "P");
    }

  String getStrSql(String condWhere,String orderBy)
  {
    String s1="SELECT * FROM "+TABLACAB+" AS c " +
        (P_ZONA != null ? ", clientes cl " : "") +
        " WHERE 1=1 " +
        (P_ZONA != null ? " and cl.cli_codi = c.cli_codi " +
         " AND cl.zon_codi LIKE '" + P_ZONA + "'" : "") +
        (EU.isRootAV() ? "" : " AND c.div_codi > 0 ");
    if (condWhere==null)
      s1+=  " and c.emp_codi = " + EU.em_cod +" and c.avc_serie != 'X' " +
        " and c.avc_fecalb > CURRENT_DATE - 60 ";
    else
      s1+=condWhere.equals("")?"":" and "+condWhere;
    s1+=orderBy==null?" ORDER BY avc_ano,avc_serie desc,avc_nume ":orderBy;
    return s1;
  }

    @Override
  public void afterConecta() throws SQLException, ParseException
  {    
      try
      {
          isEmpPlanta=pdconfig.getTipoEmpresa(EU.em_cod, dtStat)==pdconfig.TIPOEMP_PLANTACION;
          swUsaPalets=pdconfig.getUsaPalets(EU.em_cod, dtStat);
          avl_numpalE.setEnabled(swUsaPalets);
          if (pdconfig.getConfiguracion(EU.em_cod,dtStat))
          {
              ALMACEN=dtStat.getInt("cfg_almven");
              PEDIRDESP=dtStat.getInt("cfg_desven",true)!=0;
              TIDCODI=dtStat.getInt("cfg_tideve",true);
              NUMDECPRECIO=dtStat.getInt("cfg_numdec",true);
              FORMDECPRECIO=".";
              for (int n=0;n<NUMDECPRECIO;n++)
                  FORMDECPRECIO+="9";
          }
          confGridCab();
          IMPALBTEXTO=EU.getValorParam("impAlbTexto",IMPALBTEXTO);
          IMPALBTEXTO=EU.getValorParam("impAlbTexto",IMPALBTEXTO);
          pesoManual=isEmpPlanta;
          dtAdd.setConexion(ctUp);
          cli_codiE.setZona(P_ZONA);
          cli_codiE.iniciar(dtStat, this, vl, EU);
          ifMail.iniciar(this);
          
          ifFax.iniciar(this);
          ifFax.getCliField().setPeso(1);
          ifFax.setCopiaPapel(true);
          pro_codiE.iniciar(dtStat, this, vl, EU);
          pro_codiE.setEntrada(true);
          pro_codiE.getFieldProCodi().setToolTipText("Doble click para actualizar nombre");
          pro_codicE.iniciar(dtStat, this, vl, EU);
          pro_codicE.setEntrada(true);
          pro_codresE.iniciar(dtStat, this, vl, EU);
          emp_codiE.iniciar(dtStat,this,vl, EU);
          sbe_codiE.iniciar(dtStat,this,vl,EU);
          sbe_codiE.setFieldEmpCodi(emp_codiE.getTextField());
          sbe_nombL=sbe_codiE.creaLabelSbe();
          sbe_codiE.setLabelSbe(sbe_nombL);
          pdnumeracion.llenaSeriesAlbVen(true,avc_seriE,true,P_ADMIN);
          
          s="SELECT div_codi,div_nomb FROM v_divisa ORDER BY div_nomb";
          dtCon1.select(s);
          div_codiE.setDatos(dtCon1);
          if (EU.getUsuReser1().equals("S"))
              div_codiE.addItem("-------","0");
          div_codiE.setColumnaAlias("div_codi");
          tar_codiE.setFormato(true);
          tar_codiE.setFormato(Types.DECIMAL, "#9", 1);
          s = "SELECT tar_codi,tar_nomb FROM tipotari " +
              " ORDER BY tar_codi ";
          dtStat.select(s);
          tar_codiE.addDatos(dtStat);
          avc_deposE.setColumnaAlias("avc_depos");
          initAvcRevpreE(avc_revpreE,true);
      } catch (IllegalArgumentException | ClassNotFoundException ex  )
      {
         Error("Error al configurar grid",ex);
      } 
  }
  public static void initAvcRevpreE(CComboBox cb, boolean incNo)
  {
    if (incNo)
     cb.addItem("No","0");
    cb.addItem("Pend.Costos",""+REVPRE_PCOSTO);
    cb.addItem("Pend.Valor",""+REVPRE_PVALOR);
    cb.addItem("Revisado",""+REVPRE_REVISA);
  }
    @Override
  public void iniciarVentana() throws Exception
  {
    jtDes.getPopMenu().add(verDatTraz);
    Bdespiece.setSelected(false);
    EU.getImpresora(gnu.chu.print.util.ALBARAN);
    dtHist=new DatosTabla(ct);
    dtPedi=new DatosTabla(ct);
    s="SELECT * FROM v_motregu WHERE tir_tipo='"+pdmotregu.MERM_CLIENTE+"'" ; // Merma Cliente
    if (dtStat.select(s))
      tirCodi=dtStat.getInt("tir_codi");
    etiq = new etiqueta(EU);
    utdesp = new utildesp();
    datCab = new actCabAlbFra(dtCon1,dtAdd,EU.em_cod,NUMDECPRECIO);
    ifFax.setLocation(this.getLocation().x+30,this.getLocation().x+30);
    vl.add(ifFax,new Integer(1));
    ifMail.setLocation(this.getLocation().x+30,this.getLocation().x+30);
    vl.add(ifMail,new Integer(1));
    avc_almoriE.setValor(""+ALMACEN); // Almacen de VENTAS
    opDispSalida.addItem("Impresora", DSAL_IMPRE);
    if (PERMFAX)
    {
        opDispSalida.addItem("Fax", DSAL_FAX);
        opDispSalida.addItem("E-Mail", DSAL_EMAIL);
    }
    
    Bimpri.addMenu("---","-");
    Bimpri.addMenu("Alb Gráfico", ""+IMPR_ALB_GRAF);
    
    if (IMPALBTEXTO)   
        Bimpri.addMenu("Alb Texto", ""+IMPR_ALB_TEXT);    
    Bimpri.addMenu("H.Traz.", ""+IMPR_HOJA_TRA );
    
    Bimpri.addMenu("Alb/H.Traz", ""+IMPR_ALB_TRA);
       
    Bimpri.addMenu("Palets", ""+IMPR_PALETS);
    Bimpri.addMenu("Etiquetas", ""+IMPR_ETIQUETAS);
    
    
    opValora.setSelected(false);
    jtRes.setDefButton(Baceptar);
    nav.setButton(KeyEvent.VK_F9, Bimpri.getBotonAccion());
    Pcabe.setButton(KeyEvent.VK_F9, Bimpri.getBotonAccion());
    Pcabe.setButton(KeyEvent.VK_F9, Bimpri.getBotonAccion());
    nav.setButton(KeyEvent.VK_F5, BValTar);
    jt.setButton(KeyEvent.VK_F5, BValTar);
    Pcabe.setButton(KeyEvent.VK_F5, BValTar);
    Pcabe.setDefButton(Baceptar);
    Pcabe.setButton(KeyEvent.VK_F4, Baceptar);
    jt.setButton(KeyEvent.VK_F4, Baceptar);
    jt.setButton(KeyEvent.VK_F10,Bdespiece);
   
    fvc_serieE.setColumnaAlias("fvc_serie");
    cli_codiE.setColumnaAlias("C.cli_codi");
    emp_codiE.setColumnaAlias("emp_codi");
    avc_anoE.setColumnaAlias("avc_ano");
    avc_seriE.setColumnaAlias("avc_serie");
    avc_numeE.setColumnaAlias("avc_nume");
    avc_fecalbE.setColumnaAlias("avc_fecalb");
    usu_nombE.setColumnaAlias("usu_nomb");
    avc_fecemiE.setColumnaAlias("avc_fecemi");
    avc_revpreE.setColumnaAlias("avc_revpre");
    fvc_anoE.setColumnaAlias("fvc_ano");
    fvc_numeE.setColumnaAlias("fvc_nume");
    rut_codiE.setColumnaAlias("cli_ruta");
    avc_dtoppE.setColumnaAlias("avc_dtopp");
    avc_dtocomE.setColumnaAlias("avc_dtocom");
    sbe_codiE.setColumnaAlias("sbe_codi");
    pro_codiE.setCamposLote(avp_ejelotE, avp_serlotE, avp_numparE,
                            avp_numindE, avp_cantiE);
    Pcabe.setButton(KeyEvent.VK_F2, Birgrid);
    jt.setButton(KeyEvent.VK_F2, Birgrid);
    jtDes.setButton(KeyEvent.VK_F2, Birgrid);
    jtDes.setButton(KeyEvent.VK_F9, BmvReg);
    jtDes.setButton(KeyEvent.VK_F5, BValTar);
    Pped1.setButton(KeyEvent.VK_F3,BbusPed);

    eti_codiE.setDatos(etiqueta.getReports(dtStat, EU.em_cod,0));
    eti_codiE.addItem("Del Cliente",""+ETICLIENTE);
    eti_codiE.addItem("Defecto","0");
    eti_codiE.setValor("0");

    avc_valoraE.addItem("Valorado","-1");
    avc_valoraE.addItem("NO Valor","0");
    avc_valoraE.addItem("Pend.Val","1");

    avc_valoraE.setColumnaAlias("avc_valora");
    stkPart=new ActualStkPart(dtAdd,EU.em_cod);
    pdalmace.llenaCombo(avc_almoriE, dtCon1);
    pdalmace.llenaCombo(alm_codoriE, dtCon1);
    pdalmace.llenaCombo(alm_coddesE, dtCon1);
//    s = "SELECT alm_codi,alm_nomb FROM v_almacen ORDER BY alm_nomb";
//    dtCon1.select(s);
//    avc_almoriE.addItem(dtCon1);
    PEDIRDESP=true;

     
    // Bdespiece.setEnabled(PEDIRDESP);

    activarEventos();
    if (getLabelEstado()!=null)
        if (getLabelEstado().getText().contains("*DIRECTO*"))
            setChequeaPedidos(false);
    if (P_CHECKPED)
    {
        temporizador=new javax.swing.Timer(15000,new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                  checkPedidos();
                }
        });
        temporizador.setDelay(15000);
        temporizador.start();
        setLabelMsgEspere("Pedidos Pendientes");
        checkPedidos();
    }
    if (P_MODPRECIO)
      jt.setEnabled(true);
//    if (P_MODPRECIO)
//        avc_revpreE.setEnabled(true);
//    verDatos(dtCons);
  }
  public void setChequeaPedidos(boolean checkPedidos)
  {
      P_CHECKPED=checkPedidos;
  }
  public boolean getChequeaPedidos()
  {
      return P_CHECKPED;
  }
  void activar() throws PropertyVetoException
  {
       if (isIcon())
            setIcon(false);
        moveToFront();
        show();
        setSelected(true);
  }
  void checkPedidos()
  {
     
      try
      {          
          if (nav.isEdicion() )
              return;
          if (P_CHECKPED)
            temporizador.stop();         
          
          dtPedi.select("select count(*) as cuantos from pedvenc where pvc_fecent <= CURRENT_DATE AND pvc_confir='S' and avc_ano = 0");
          if (dtPedi.getInt("cuantos")!= numPedPend)
          {
              numPedPend=dtPedi.getInt("cuantos");
              if (! isSelected() || isIcon() )
              {   
               SwingUtilities.invokeLater(new Thread(){
                @Override
                public void run()
                {                       
                    try
                    {
                       activar();
                    } catch (PropertyVetoException ex)
                    {
                        Logger.getLogger(pdalbara.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
              });               
              }
              msgEspere("Existen "+numPedPend+ " pedidos pendientes");
          }      
      } catch (SQLException ex)
      {
          Logger.getLogger(pdalbara.class.getName()).log(Level.SEVERE, null, ex);
      }      
  }

  void afterFocusLostCli(boolean error)
  {
    if (error)
      return;
    if (! cli_codiE.hasCambio())
        return;
    cli_codiE.resetCambio();
    try
    {
      cli_pobleE.setText(cli_codiE.getLikeCliente().getString("cli_poble"));
      tar_codiE.setText(cli_codiE.getLikeCliente().getString("tar_codi"));
      avc_dtoppE.setValorDec(cli_codiE.getLikeCliente().getDouble(
          "cli_dtopp"));
      avc_dtocomE.setValorDec(cli_codiE.getLikeCliente().getDouble(
          "cli_pdtoco"));
      sbe_codiE.setValorInt(cli_codiE.getLikeCliente().getInt("sbe_codi"));
      if (nav.pulsado==navegador.ADDNEW)
        avc_revpreE.setValor(cli_codiE.getLikeCliente().getInt("cli_precfi"));
      if ( MantTarifa.isTarifaCosto(dtStat,cli_codiE.getLikeCliente().getInt("tar_codi")) )
       avc_valoraE.setValor("1");
    }
    catch (SQLException k)
    {
      Error("Error al Buscar datos de Cliente", k);
    }
  }
  
  boolean canModDepos() throws SQLException
  {
        if ( verDepoC.getValor().equals("O") && swTieneEnt)
        {
            msgBox("ATENCION!!. Este albaran de deposito ya tiene genero entregado");
            return false;
        }
        if (avc_deposE.getValor().equals("E"))
        {
            msgBox("Un albaran solo se puede modificar de Deposito a Normal o viceversa");
            return false;
        }
        if (checkEdicionAlbaran())
            return false;
      
        return true;
  }
  void activarEventos()
  {
      avc_deposE.addActionListener(new java.awt.event.ActionListener()
      {
          @Override
          public void actionPerformed(java.awt.event.ActionEvent evt) {
              try
              {
                  if (nav.pulsado!=navegador.NINGUNO  || !avc_deposE.hasCambio() )
                      return ;
                  if (!canModDepos())
                  {
                      avc_deposE.setValor(avc_deposE.getValorOld());
                      return;
                  }
                  
                  dtAdd.executeUpdate("update v_albavec set avc_depos='"+avc_deposE.getValor()+"' where "+getCondCurrent());
                  dtAdd.commit();
                  msgBox("Tipo Albaran cambiado a "+avc_deposE.getText());
                  avc_deposE.resetCambio();
              } catch (SQLException ex)
              {
                  Error("Error al cambiar albaran a deposito", ex);     
              }
            
          }
      });
      MAllFechaCab.addActionListener(new java.awt.event.ActionListener()
      {
          @Override
          public void actionPerformed(java.awt.event.ActionEvent evt) {
              MFechaCabActionPerformed(-1);
          }
      });
      MFechaCab.addActionListener(new java.awt.event.ActionListener()
      {
          @Override
          public void actionPerformed(java.awt.event.ActionEvent evt) {
              MFechaCabActionPerformed(jt.getSelectedRowDisab());
          }
      });
      printE.addMouseListener(new MouseAdapter() {
        @Override
      public void mouseClicked(MouseEvent e)
      {
        if (e.getClickCount()<2 || (avcImpres & 1) == 0)
          return;
        if (inTransation())
          return;
        try
        {
            setAlbaranImpreso(0);
            mensaje("Quitada marca de listado al albaran");
        } catch (SQLException ex)
        {
            Error("Error al establece estado de listado a albaran",ex);
        }
        
      }
    });
      jtHist.tableView.getSelectionModel().addListSelectionListener(new
       ListSelectionListener()
    {
        @Override
        public void valueChanged(ListSelectionEvent e)
        {
            if (e.getValueIsAdjusting() || !jtHist.isEnabled()) // && e.getFirstIndex() == e.getLastIndex())
                return;
            cambiaLineaHist(jtHist.getValorInt(3));
        }
    });
    jtRes.addMouseListener(new MouseAdapter() {
        @Override
      public void mouseClicked(MouseEvent e)
      {
        if (e.getClickCount()<2 || jtRes.isEnabled())
          return;
        if (nav.pulsado!=navegador.ADDNEW && nav.pulsado!=navegador.EDIT)
          return;
        jtRes.setEnabled(true);
        jtRes.requestFocusInicioLater();
      }
    });
    
    popEspere_BCancelaraddActionListener(new ActionListener()
    {
           @Override
        public void actionPerformed(ActionEvent e)
        {
             if (P_CHECKPED)
                temporizador.restart();
             resetMsgEspere();
        }
    });
    avc_revpreE.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        try {
            if (! avc_revpreE.isEnabled() || nav.isEdicion() || nav.getPulsado()==navegador.QUERY)
                return;
            dtAdd.executeUpdate("update v_albavec set avc_revpre="+avc_revpreE.getValor()+" where "+getCondCurrent());
            dtAdd.commit();
            mensajeErr("Actualizado estado Revision precios");
        } catch (SQLException ex) {
           Error("Error al modificar estado revisión precios",ex);
        }
      }
    });
    Bdespiece.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        if ( pro_codiE.isEditable())
            realizaDesp();
      }
    });
    Bdespiece.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
          if (!P_ADMIN)
             avp_cantiE.setEditable(Bdespiece.isSelected());
      }
    });


    avc_deposE.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        if (! avc_deposE.isEnabled())
            return;
        swEntdepos=avc_deposE.getValor().equals("E");
        avp_cantiE.setEditable( P_ADMIN);
        if (swEntdepos  )
            avp_cantiE.setEditable(false);
      }
    });
    verDepoC.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        try {
            if (! verDepoC.isEnabled())
                return;
            avsNume=Integer.parseInt(verDepoC.getValor());
            swEntdepos= true;
          } catch (NumberFormatException k)
          {
             try {
                avc_fecalbE.setDate(dtCons.getDate("avc_fecalb"));
             } catch (SQLException k1)
             {
                Error("Error al poner fecha de albaran",k1);
                return;
             }
              avsNume=0;
              swEntdepos=false;
          }
          try {
            avl_prvenE.setEditable(verDepoC.getValor().equals("O"));
            if (avsNume>0)
            {
                if (! dtStat.select("select avs_fecha from albvenserc where avs_nume="+avsNume))
                {
                    mensajes.mensajeAviso("No encontrado albaran de entrega de deposito");
                    return;
                }
                avc_fecalbE.setDate(dtStat.getDate("avs_fecha"));
            }
            verDatLin(dtCons,opAgru.isSelected(),true);
        } catch (Exception k)
        {
              Error("Error al ver lineas de albaran entregadas en deposito",k);
        }
      }
    });
    BValTar.addActionListener(new ActionListener()
    {
            @Override
      public void actionPerformed(ActionEvent e)
      {
        BValTar_actionPerformed();
      }
    });
    Bdesgl.addActionListener(new ActionListener()
    {
            @Override
      public void actionPerformed(ActionEvent e)
      {
        Bdesgl_actionPerformed();
      }
    });
    Bdespiece.addActionListener(new ActionListener()
    {
            @Override
      public void actionPerformed(ActionEvent e)
      {
        jt.requestFocusLater();
      }
    });
    BmvReg.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        if (! jtDes.isEnabled())
          return;
        BmvReg_actionPerformed();
      }
    });
    pro_codiE.getFieldProCodi().addMouseListener(new MouseAdapter()
    {
      @Override
      public void mouseClicked(MouseEvent e)
      {
        if (e.getClickCount()<2)
          return;
        if (nav.pulsado!=navegador.ADDNEW && nav.pulsado!=navegador.EDIT)
          return;
        try {
          pro_codiE.resetCambio();
          String proNomb = pro_codiE.getNombArtCli(pro_codiE.getValorInt(),
              cli_codiE.getValorInt());
          jt.setValor(proNomb,  2);
          pro_nombE.setText(proNomb);
        } catch (SQLException k)
        {
          Error("Error al actualizar nombre de producto",k);
        }
      }

    });

   
    avc_anoE.addFocusListener(new FocusAdapter()
    {
        @Override
      public void focusLost(FocusEvent e)
      {
        if (avc_anoE.hasCambio() && nav.pulsado == navegador.EDIT)
        {
          if (mensajes.mensajePreguntar("Cambiar Ejercicio ?") != mensajes.YES)
          {
            avc_anoE.setText(avc_anoE.getTextAnt());
            return;
          }
          cambiaEmp();
        }
      }
    });

    avc_numeE.addFocusListener(new FocusAdapter()
    {
        @Override
      public void focusLost(FocusEvent e)
      {
        if (avc_numeE.hasCambio() && nav.pulsado == navegador.EDIT)
        {
          if (mensajes.mensajePreguntar("Cambiar Numero de Alb. ?") !=
              mensajes.YES)
          {
            avc_numeE.setText(avc_numeE.getTextAnt());
            return;
          }
          if (!cambiaNumALb(avc_numeE.getValorInt(),
                            Integer.parseInt(avc_numeE.getTextAnt().trim())))
            avc_numeE.setText(avc_numeE.getTextAnt());
        }
      }
    });

    emp_codiE.addFocusListener(new FocusAdapter()
    {
      @Override
      public void focusLost(FocusEvent e)
      {
        if (emp_codiE.hasCambio())
        {
            if (nav.pulsado == navegador.EDIT)
            {
                System.out.println("Cambiado empresa");
              if (!emp_codiE.controla(false))
              {
                msgBox("Empresa NO es valida");
                emp_codiE.setText(emp_codiE.getTextAnt());
                return;
              }
              if (mensajes.mensajePreguntar("Cambiar Empresa ?") != mensajes.YES)
              {
                emp_codiE.setText(emp_codiE.getTextAnt());
                return;
              }
              cambiaEmp();
            }
          }
      }
    });

    avc_seriE.addFocusListener(new FocusAdapter()
    {
      @Override
      public void focusLost(FocusEvent e)
      {
        try
        {
          if (avc_seriE.hasCambio() && nav.pulsado == navegador.EDIT)
          {
            if (cli_codiE.getLikeCliente().getInt("cli_intern") == 0)
            {
              if (avc_seriE.getValor().equals("Y"))
              {
                msgBox("ESTE CLIENTE NO ESTA MARCADO COMO INTERNO");
                avc_seriE.setText(avc_seriE.getTextAnt());
                return;
              }
            }
            else
            {
              if (!avc_seriE.getValor().equals("Y"))
              {
                msgBox("CLIENTE NO ESTA MARCADO COMO INTERNO. SE DEBE UTILIZAR LA SERIE Y");
                avc_seriE.setText(avc_seriE.getTextAnt());
                return;
              }
            }

            if (mensajes.mensajePreguntar("Cambiar SERIE ?") != mensajes.YES)
            {
              avc_seriE.setText(avc_seriE.getTextAnt());
              return;
            }
            cambiaEmp();
          }
        }
        catch (SQLException k)
        {
          Error("Error al cambiar empresa", k);
        }
      }
    });
    Bimpri.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {        
        imprimir(e.getActionCommand().equals("---")?ultSelecImpr:Bimpri.getValor(e.getActionCommand()).charAt(0) );
      }
    });
    opAgru.addActionListener(new ActionListener()
    {
            @Override
      public void actionPerformed(ActionEvent e)
      {
        verDatos(dtCons);
      }
    });
    Bfincab.addFocusListener(new FocusAdapter()
    {
        @Override
      public void focusGained(FocusEvent e)
      {
        irGridLin();
      }
    });
    Birgrid.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        irGrid();
      }
    });

    jt.tableView.getSelectionModel().addListSelectionListener(new
        ListSelectionListener()
    {
            @Override
      public void valueChanged(ListSelectionEvent e)
      {
        if (e.getValueIsAdjusting() || ( (nav.pulsado == navegador.ADDNEW
                                          || nav.pulsado == navegador.EDIT) &&
                                        jt.isEnabled() == false)) // && e.getFirstIndex() == e.getLastIndex())
          return;
        verDesgLinea(emp_codiE.getValorInt(), avc_anoE.getValorInt(),
                     avc_seriE.getText(), avc_numeE.getValorInt(),
                     jt.getValorInt(jt.tableView.getSelectedRow(), 0),
                     jt.getValorInt(jt.tableView.getSelectedRow(), JT_PROCODI),
                     jt.getValorInt(jt.tableView.getSelectedRow(), JT_NUMPALE),
                     jt.getValString(jt.tableView.getSelectedRow(), 2),                     
                     verPrecios?jt.getValorDec(jt.tableView.getSelectedRow(), 5):0);
      }
    });
    jtDes.tableView.addMouseListener(new MouseAdapter()
    {
            @Override
      public void mouseClicked(MouseEvent e)
      {
        if (! nav.isEdicion() && e.getClickCount()>1)
        {
            mostrarDatosTraz();
            return;
        }
        if (Birgrid.isEnabled())
        {
          if (!jtDes.isEnabled())
            irGridDes();
        }
      }
    });
    verDatTraz.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (! nav.isEdicion() )
                    mostrarDatosTraz();
            }
        });
    jt.tableView.addMouseListener(new MouseAdapter()
    {
        @Override
      public void mouseClicked(MouseEvent e)
      {
        if (Birgrid.isEnabled())
        {
          if (!jt.isEnabled())
            irGridLin();
        }
      }
    });
    jt.setReqFocusEdit(true);
    if (P_MODPRECIO )
    {
      avc_valoraE.addActionListener(new ActionListener()
      {
                @Override
        public void actionPerformed(ActionEvent e)
        {
          if (!opVerdat && nav.pulsado==navegador.NINGUNO)
            actValora();
        }
      });

      avl_prvenE.addKeyListener(new KeyAdapter()
      {
          @Override
        public void keyPressed(KeyEvent e)
        {
          if (e.getKeyCode() == KeyEvent.VK_F3)
            consPrecios();
        }

      });
    
      avl_cantiE.addFocusListener(new FocusAdapter()
      {
        @Override
        public void focusGained(FocusEvent e)
        {
          irGridFoco();
        }
      });
    }
    else
    {
      jt.setReqFocusEdit(true);
      avl_cantiE.addFocusListener(new FocusAdapter()
      {
        @Override
        public void focusGained(FocusEvent e)
        {
          irGridFoco();
        }
      });
    }

    avp_ejelotE.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyPressed(KeyEvent e)
      {
        if (e.getKeyCode() == KeyEvent.VK_F3)
          ayudaLote();
      }
    });
    avp_serlotE.addKeyListener(new KeyAdapter()
    {
            @Override
      public void keyPressed(KeyEvent e)
      {
        if (e.getKeyCode() == KeyEvent.VK_F3)
          ayudaLote();
      }
    });
    avp_numparE.addKeyListener(new KeyAdapter()
    {
        @Override
      public void keyPressed(KeyEvent e)
      {
        if (e.getKeyCode() == KeyEvent.VK_F3)
          ayudaLote();
      }
    });
    avp_numindE.addKeyListener(new KeyAdapter()
    {
        @Override
      public void keyPressed(KeyEvent e)
      {
        if (e.getKeyCode() == KeyEvent.VK_F3)
          ayudaLote();
      }
    });
    BbusPed.addActionListener(new ActionListener()
    {
            @Override
      public void actionPerformed(ActionEvent e)
      {
        BbusPed_actionPerformed();
      }
    });
    pvc_numeE.addFocusListener(new FocusAdapter()
    {
      @Override
      public void focusLost(FocusEvent e)
      {
        try {
          if (pvc_numeE.hasCambio() || pvc_anoE.hasCambio())
          {     
            pvc_anoE.resetCambio();
            pvc_numeE.resetCambio();
            cli_codiE.setValorInt(getClientePedido());
            afterFocusLostCli(false);
            actPedAlbaran();
          }
        } catch (SQLException | ParseException k)
        {
          Error("Error al Chequear Pedido de ventas ",k);
        }
      }
    });

    opAgrPrv.addActionListener(new ActionListener()
    {
            @Override
      public void actionPerformed(ActionEvent e)
      {
        try {
          actAcumPed(0);
        } catch (SQLException | ParseException k)
        {
          Error("Error al ver acumulados de Pedidos",k);
        }
      }
    });
    opAgrFecha.addActionListener(new ActionListener()
    {
            @Override
      public void actionPerformed(ActionEvent e)
      {
        try
        {
          actAcumPed(0);
        }
        catch (SQLException | ParseException k)
        {
          Error("Error al ver acumulados de Pedidos", k);
        }
      }
    });

  }
   private void cambiaLineaHist(int rowid)
   {
    hisRowid=rowid;
    if (hisRowid==0)
    {
        verDatos(dtCons);
        return;
    }
    tablaCab="hisalcave";
    tablaLin="hisallive";
    tablaInd="hisalpave";
    vistaInd="v_halbventa_detalle";    
    
    try {
         s="SELECT * FROM "+tablaCab+" WHERE his_rowid = "+hisRowid;
         
        dtHist.select(s);
        verDatos(dtHist,opAgru.isSelected());
    } catch (SQLException k)
    {
        Error("Error al ver datos de historicos",k);
    }
 }
  void mostrarDatosTraz()
  {
      try {
          if (datTrazFrame==null)
          {
              datTrazFrame=new DatTrazFrame(EU,vl,this)
              {
                    @Override
                    public void matar()
                    {
                       salirDatTraza();
                    }
              };
              datTrazFrame.iniciar(dtStat, dtCon1,this,vl,EU);
              vl.add(datTrazFrame);
              datTrazFrame.setLocation(this.getLocation().x, this.getLocation().y + 30);
          }
          datTrazFrame.setDatos(jtDes.getValorInt(jtDes.getSelectedRowDisab(),JTDES_PROCODI),
                      jtDes.getValString(jtDes.getSelectedRowDisab(),JTDES_SERIE),
                      jtDes.getValorInt(jtDes.getSelectedRowDisab(),JTDES_EJE),
                      jtDes.getValorInt(jtDes.getSelectedRowDisab(),JTDES_LOTE),
                      jtDes.getValorInt(jtDes.getSelectedRowDisab(),JTDES_NUMIND));
           datTrazFrame.actualizar();
           this.setEnabled(false);
           datTrazFrame.mostrar();
      } catch (SQLException k)
      {
          Error("Error a mostrar Datos de Trazabilidad",k);
      }
  }
  private void salirDatTraza()
  {
    datTrazFrame.setVisible(false);
    this.toFront();
    this.setEnabled(true);
    try
    {
      this.setSelected(true);
    }
    catch (Exception k)
    {}
    
  }
  /**
   * Abre la ventana para realizar los despieces
   */
  void realizaDesp()
  {
    try {
        if (despVenta==null)
        {
          despVenta = new DespVenta()
          {
            @Override
            public void despuesMatar()
            {
               salirDespieceVenta();
            }
          };
          despVenta.iniciar(this);
          vl.add(despVenta);
          despVenta.setLocation(this.getLocation().x, this.getLocation().y + 30);
         }
        this.setEnabled(false);
        despVenta.setProCodi(pro_codiE.getValorInt());
        despVenta.mostrar();
        
        despVenta.setAlmacen(alm_codoriE.getValorInt());
      } catch (Exception k ){
          Error("Error al iniciar aplicacion despiece en ventas",k);
      }
  }
  /**
   * Salir del despiece de ventas
   */
  void salirDespieceVenta()
  {
    despVenta.setVisible(false);
    this.toFront();
    this.setEnabled(true);
    try
    {
      this.setSelected(true);
    }
    catch (Exception k)
    {}
    if (despVenta.isNuevoDespiece())
    { // Nuevo despiece
        if (despVenta.getGrid().getValorDec(despVenta.JT_KILOS)==0 || despVenta.getGrid().getValorInt(0)==0 )
            return; // Linea no valida
        pro_codiE.setText(despVenta.getCodBarras());
        pro_codiE.llenaCampos();
        //pro_codiE.setValorInt(despVenta.getGrid().getValorInt(0));
    }
  }
  /**
   * Valorar Albaran segun precios de Tarifa
   * Pone a todas las linas de albaran cuyo precio sea 0, el precio de la tarifa
   * siempre y cuando esta no sea 0.
   */
  void BValTar_actionPerformed()
  {
    if (avc_revpreE.getValorInt()!=0)
    {
       if( mensajes.mensajeYesNo("¿ Poner precios de tarifa a albaran marcado como a revisar Precios?", this)!=mensajes.YES)
           return;
    }
    int nRow = jt.getRowCount();
    // Actualizo las Linea de Albaran.
    try {
    for (int n = 0; n < nRow; n++)
    {
      if (jt.getValorDec(n,FD_PRECIO)!=0 || jt.getValorDec(n,FD_PRTARI)==0)
          continue;
   //   avl_prvenE.setValorDec(jt.getValorDec(n,FD_PRTARI));
      jt.setValor(""+jt.getValorDec(n,FD_PRTARI),n,FD_PRECIO);
      actPrecioAlb(n,jt.getValorDec(n,FD_PRTARI));
    }
    if (jt.isEnabled())
        jt.ponValores(jt.getSelectedRow());
    mensaje("Precios de Albaran actualizados a los de Tarifa");
    } catch (Exception k)
    {
        Error("Error al poner Precios de Tarifa a Precio de albaran",k);
    }
  }

  void Bdesgl_actionPerformed()
  {
    try
    {
      if (fvc_numeE.getValorInt()!=0)
      {
        mensajeErr("Albaran YA esta facturado...IMPOSIBLE DESGLOSAR");
        return;
      }
      if (pdejerci.isCerrado(dtStat, avc_anoE.getValorInt(), emp_codiE.getValorInt()))
      {
        if (!P_ADMIN)
        {
          msgBox("Albaran es de un ejercicio YA cerrado ... IMPOSIBLE MODIFICAR");
          return;
        }
        else
          msgBox("ATENCION!!! Albaran es de un ejercicio YA cerrado");
      }

      if (impLinE.getValorDec() <= 1)
      {
        mensajeErr("Importe de Albaran debe ser superior a 1 ");
        return;
      }

      if (dgAlb == null)
      {
        dgAlb = new sepAlbVen(EU)
        {
                    @Override
          public void matar(boolean cerrarConexion)
          {
            cerrarDesgAlb();
          }
        };
        vl.add(dgAlb);
        dgAlb.setLocation(this.getLocation().x, this.getLocation().y + 30);
      }

      s = dgAlb.iniciar(dtCon1, dtStat, dtAdd, emp_codiE.getValorInt(), avc_anoE.getValorInt(),
                        avc_seriE.getText(), avc_numeE.getValorInt(), kilosE.getValorDec(),
                        impLinE.getValorDec());
      if (s != null)
      {
        mensajeErr(s);
        return;
      }
      dgAlb.setVisible(true);
      this.setEnabled(false);
    }
    catch (Exception k)
    {
      Error("Error al inicializar ventana Desglose albaranes", k);
    }

  }

  private void cerrarDesgAlb()
  {
    try {
      resetBloqueo(dtAdd, "V_albavec",
                   avc_anoE.getValorInt() + "|" + emp_codiE.getValorInt() +
                   "|" + avc_seriE.getText() + "|" + avc_numeE.getValorInt());
      if (!dgAlb.Baceptar.isEnabled())
        rgSelect();
    } catch (Exception k)
    {
      aviso("Imposible quitar bloqueo en cerrarDesgAlb: "+
                 avc_anoE.getValorInt() + "|" + emp_codiE.getValorInt() +
                 "|" + avc_seriE.getText() + "|" + avc_numeE.getValorInt(),k);
    }
    if (!dgAlb.isVisible())
      return;
    dgAlb.setVisible(false);
    this.setEnabled(true);

    nav.requestFocus();
  }

  void BbusPed_actionPerformed()
  {
    try
    {

      if (copeve == null)
      {
        copeve = new CLPedidVen(this)
        {
          @Override
          public void matar(boolean cerrarConexion)
          {
            cerrarCopeve();
          }
        };
        this.getLayeredPane().add(copeve, new Integer(1));
        
        copeve.setLocation(5,  5);
        copeve.iniciarVentana();
        copeve.setVisibleCabeceraVentana(false);
      }
      copeve.setVisible(true);
      this.setEnabled(false);
      copeve.setCliCodiText(cli_codiE.getText());
      copeve.setEmpCodiText(emp_codiE.getText());
      copeve.empCodiS=0;
      copeve.ejeNumeS=0;
      copeve.pvcNumeS=0;
      copeve.Baceptar_doClick();
    }
    catch (Exception k)
    {
      Error("Error al inicializar ventana consulta Pedidos", k);
    }
  }

  void cerrarCopeve()
  {
    if (! copeve.isVisible())
      return;
    copeve.setVisible(false);
    this.setEnabled(true);
    if (copeve.empCodiS!=0)
    {
      pvc_anoE.setValorInt(copeve.ejeNumeS);
      pvc_numeE.setValorInt(copeve.pvcNumeS);
      cli_codiE.setValorInt(copeve.cliCodiS);
      cli_codiE.getCliNomb().setText(copeve.getCliNomb());
      afterFocusLostCli(false);
    }
    try {
      actPedAlbaran();
    } catch (SQLException | ParseException k)
    {
      Error("Error al Actualizar pedido de albaran",k);
    }
    cli_codiE.requestFocus();
  }
  /**
   * Cambia campo avc_valora para especificar si esta valorado o no el albaran.
   */
  void actValora()
  {
    try {
      if (dtCons.getNOREG())
        return;

      // Actualizo la Cabecera.
      selCabAlb(dtAdd,avc_anoE.getValorInt(),emp_codiE.getValorInt(),avc_seriE.getText(),
           avc_numeE.getValorInt(),true,true);

      dtAdd.edit(dtAdd.getCondWhere());
      dtAdd.setDato("avc_valora", P_MODPRECIO ? avc_valoraE.getValor() : "0");
      // Se utiliza para saber si los precios son aut. o Manuales
      int nRowUpd=dtAdd.update(stUp);
      ctUp.commit();
    } catch (Exception k)
    {
      Error("Error al poner el Albaran a "+avc_valoraE.getText() ,k);
    }
  }
  /**
   * Consulta Lotes Disponibles de Productos.
   */
  public void ayudaLote()
  {
    try
    {
      if (ayuLot == null)
      {
        ayuLot = new ayuLote(EU, vl, dtCon1, pro_codiE.getValorInt())
        {
          @Override
            public void matar(boolean cerrarConexion)
           {
            ayuLot.setVisible(false);
            ej_consLote();
           }
        };

        vl.add(ayuLot);
        ayuLot.setIconifiable(false);
        ayuLot.setLocation(25, 25);
        ayuLot.iniciarVentana();
      }
      ayuLot.jt.removeAllDatos();
      ayuLot.setVisible(true);
      ayuLot.muerto = false;
      ayuLot.statusBar.setEnabled(true);
      ayuLot.statusBar.Bsalir.setEnabled(true);


      this.setEnabled(false);
      this.setFoco(ayuLot);
      ayuLot.cargaGrid(pro_codiE.getText(),avc_almoriE.getValorInt());
      SwingUtilities.invokeLater(new Thread()
      {
        @Override
        public void run()
        {
          ayuLot.jt.requestFocusInicio();
        }
      });

    }
    catch (Exception j)
    {
      this.setEnabled(true);
    }
  }

  void ej_consLote()
  {
    if (ayuLot.consulta)
    {
//      avp_emplotE.setValorDec(ayuLot.jt.getValorInt(0));
      avp_ejelotE.setValorDec(ayuLot.jt.getValorInt(ayuLot.rowAct, 1));
      avp_serlotE.setText(ayuLot.jt.getValString(ayuLot.rowAct, 2));

      avp_numparE.setValorDec(ayuLot.jt.getValorInt(ayuLot.rowAct, 3));
      avp_numindE.setValorDec(ayuLot.jt.getValorInt(ayuLot.rowAct, 4));
      avp_cantiE.setText(ayuLot.jt.getValString(ayuLot.rowAct, 5));
//    avp_numuniE.setText("1");
      jtDes.setValor(ayuLot.jt.getValString(ayuLot.rowAct, 0), 2);
      jtDes.setValor(ayuLot.jt.getValString(ayuLot.rowAct, 1), 3);
      jtDes.setValor(ayuLot.jt.getValString(ayuLot.rowAct, 2), 5);
      jtDes.setValor(ayuLot.jt.getValString(ayuLot.rowAct, 3), 6);
      jtDes.setValor(ayuLot.jt.getValString(ayuLot.rowAct, 4), 7);
      jtDes.setValor(ayuLot.jt.getValString(ayuLot.rowAct, 5), 8);
    }
    ayuLot.setVisible(false);
    this.setEnabled(true);
    this.toFront();
    try
    {
      this.setSelected(true);
    }
    catch (PropertyVetoException k)
    {}
    this.setFoco(null);
    SwingUtilities.invokeLater(new Thread()
    {
       @Override
      public void run()
      {
        jtDes.requestFocus(jtDes.getSelectedRow(), 8);
      }
    });
  }

  @Override
  public void PADPrimero()
  {
    verDatos(dtCons);
    nav.setPulsado(navegador.NINGUNO);
  }

  public void PADAnterior()
  {
    verDatos(dtCons);
    nav.setPulsado(navegador.NINGUNO);
  }

    @Override
  public void PADSiguiente()
  {
    verDatos(dtCons);
    nav.setPulsado(navegador.NINGUNO);
  }

    @Override
  public void PADUltimo()
  {
    verDatos(dtCons);
    nav.setPulsado(navegador.NINGUNO);
  }

  void verDatos(DatosTabla dt)
  {
    tablaCab=TABLACAB;
    tablaLin=TABLALIN;
    tablaInd=TABLAIND;
    vistaInd=VISTAIND;
    hisRowid=0;

    verDatos(dt, opAgru.isSelected());
    resetTiempoPedidos();
  }
  void resetTiempoPedidos()
  {
    if (temporizador!=null)
    {
        if (temporizador.isRunning())
            temporizador.restart();
    }
  }
  void verDatos(DatosTabla dt, boolean agrupa)
  {
    try
    {
      avsNume=0;
      swEntdepos=false;
      if (dtCons.getNOREG())
        return;

      opVerdat=true;
      if (!selCabAlb(tablaCab,dtAdd,dt.getInt("avc_ano"),
                    hisRowid>0?-1: dt.getInt("emp_codi"),dt.getString("avc_serie"),
                        hisRowid>0?hisRowid: dt.getInt("avc_nume"),true,false))
      {
        mensajes.mensajeAviso("Registro NO encontrado ... SEGURAMENTE SE BORRO");
        Pcabe.resetTexto();
        emp_codiE.setValorInt(dt.getInt("emp_codi"));
        avc_anoE.setValorDec(dt.getInt("avc_ano"));
        avc_seriE.setText(dt.getString("avc_serie"));
        avc_numeE.setValorDec(dt.getInt("avc_nume"));
        jt.removeAllDatos();
        jtDes.removeAllDatos();
        nav.setEnabled(navegador.EDIT, false);
        nav.setEnabled(navegador.DELETE, false);
        opVerdat=false;
        return;
      }
      nav.setEnabled(navegador.EDIT, true);
      nav.setEnabled(navegador.DELETE, true);
      avc_revpreE.setEnabled(false);
      emp_codiE.setValorInt(dt.getInt("emp_codi"));
      avc_anoE.setValorDec(dt.getInt("avc_ano"));
      avc_seriE.setText(dt.getString("avc_serie"));
      avc_numeE.setValorDec(dt.getInt("avc_nume"));
//      cli_codiE.setAlbaran(dt.getInt("emp_codi"),dt.getInt("avc_ano"),
//                           dt.getString("avc_serie"),dt.getInt("emp_codi"));
      cli_codiE.setValorInt(dtAdd.getInt("cli_codi"),dtAdd.getString("avc_clinom",false));
//      cli_codiE.getCliNomb().setText(dtAdd.getString("avc_clinom"));
      cli_pobleE.setText(cli_codiE.getLikeCliente().getString("cli_poble"));
      if (cli_codiE.getLikeCliente().getInt("eti_codi")!=0)
          eti_codiE.setValor(""+ETICLIENTE);
      avc_fecalbE.setText(dtAdd.getFecha("avc_fecalb", "dd-MM-yyyy"));
      verPrecios=false;
      if (P_MODPRECIO ||
              (P_PONPRECIO && Formatear.comparaFechas(avc_fecalbE.getDate(),Formatear.getDateAct())==0) )
          verPrecios=true;
    

      usu_nombE.setText(dtAdd.getString("usu_nomb"));
      avc_fecemiE.setText(dtAdd.getFecha("avc_fecemi", "dd-MM-yyyy"));
      avc_revpreE.setValor(dtAdd.getString("avc_revpre"));
      fvc_anoE.setValorDec(dtAdd.getInt("fvc_ano", true));
      fvc_serieE.setText(dtAdd.getString("fvc_serie"));
      fvc_numeE.setValorDec(dtAdd.getInt("fvc_nume", true));
      tar_codiE.setText(cli_codiE.getLikeCliente().getString("tar_codi"));
//      rut_codiE.setValorDec(dt.getInt("cli_ruta"));
      avc_cerraE.setSelected(dtAdd.getInt("avc_cerra")!=0);
      avc_dtoppE.setValorDec(dtAdd.getDouble("avc_dtopp"));
      avc_dtocomE.setValorDec(dtAdd.getDouble("avc_dtocom"));
      avc_confoE.setSelected(dtAdd.getInt("avc_confo") != 0);
      avc_valoraE.setValor(""+dtAdd.getInt("avc_valora"));
      avc_almoriE.setValor(dtAdd.getString("avc_almori"));
      avc_obserE.setText(dtAdd.getString("avc_obser"));
      div_codiE.setValor(dtAdd.getString("div_codi"));
      sbe_codiE.setValorInt(dtAdd.getInt("sbe_codi",true));
      alm_codoriE.setValor(dtAdd.getString("alm_codori"));
      alm_coddesE.setValor(dtAdd.getString("alm_coddes"));
      avc_deposE.setValor(dtAdd.getString("avc_depos"));
      avc_deposE.resetCambio();
      verDepoC.setEnabled(false);
      swTieneEnt=false;
      verDepoC.removeAllItems();
      if (avc_deposE.getValor().equals("D"))
      {
        verDepoC.removeAllItems();
        
        if (getAlbDepos(dtCon1))
        {
            swTieneEnt=true;
            llenaVerDepos();
            do
            {
               verDepoC.addItem(dtCon1.getInt("avs_nume")+" ("+dtCon1.getFecha("avs_fecha","dd-MM-yy")+")" ,
                       dtCon1.getString("avs_nume"));
            } while (dtCon1.next());
         }
        else
             verDepoC.addItem("Original", "O");
      }
      else
          verDepoC.addItem("Original","O");
      verDepoC.setEnabled(true);
      verIconoListado(dtAdd.getInt("avc_impres",true));
      accCerra=false;
      swCompra=MantAlbComCarne.isAlbCompra(dtStat,emp_codiE.getValorInt(),
                                    avc_anoE.getValorInt(),
                                    avc_numeE.getValorInt(),false);
      if (swCompra)
      {
        accNume=dtStat.getInt("acc_nume");
        accAno = dtStat.getInt("acc_ano");
        accCerra = dtStat.getInt("acc_cerra") != 0;
      }
      traspCont=isTraspCont();
      avl_prvenE.setEditable((P_MODPRECIO || verPrecios) && hisRowid==0);
      if ( isBloqueado(dtStat, TABLACAB,
                      avc_anoE.getValorInt() + "|" + emp_codiE.getValorInt() +
                      "|" + avc_seriE.getText() + "|" + avc_numeE.getValorInt(),true,false))
      {
        lockE.setIcon(Iconos.getImageIcon("lock"));
        isLock = true;
        avl_prvenE.setEditable(false);
        Bimpri.setEnabled(false);
        lockE.setToolTipText(msgBloqueo);
//        jt.setEnabled(false);
      }
      else
      {
        lockE.setIcon(null);
        lockE.setToolTipText("");
        isLock = false;
//        avl_prvenE.setEditable(true);
        Bimpri.setEnabled(true);
//        jt.setEnabled(true);
      }
      if (P_MODPRECIO && fvc_numeE.getValorInt() == 0 && hisRowid==0)
         avc_revpreE.setEnabled(true);
      avl_prvenE.setEditable((P_MODPRECIO || verPrecios )&& hisRowid==0);
      traspReci=false;
      if ( fvc_anoE.getValorDec() > 0 || fvc_numeE.getValorDec() > 0)
      {
        s = "SELECT * FROM v_recibo WHERE eje_nume = " + fvc_anoE.getValorInt() +
            " and emp_codi = " + emp_codiE.getValorInt() +
            " and fvc_serie = '"+fvc_serieE.getText()+"'"+
            " and fvc_nume = " + fvc_numeE.getValorInt() +
            " and rem_codi != 0 ";
        if (dtStat.select(s))
          traspReci=true;
      }
      if (cli_codiE.hasError())
      {
        aviso("Cliente: "+cli_codiE.getValorInt()+" NO valido en Albaran: "+
              " avc_ano = " + dt.getInt("avc_ano") +
              " and emp_codi = " + dt.getInt("emp_codi") +
              " and avc_serie = '" + dt.getString("avc_serie") + "'" +
              " and avc_nume = " + dt.getInt("avc_nume"));

        msgBox("Cliente NO VALIDO... NO se mostraran lineas de Albaran");
      }
      else
        verDatLin(dtAdd, agrupa, cli_codiE.getLikeCliente().getInt("cli_exeiva") == 0);
      
      verDatProdRec(dt.getInt("emp_codi"),dt.getInt("avc_ano"),
          dt.getString("avc_serie"),
           dt.getInt("avc_nume"));
      verDatPedido(true);
      if (hisRowid==0)
      {
          actGridHist(dt.getInt("emp_codi"),dt.getInt("avc_ano"),dt.getString("avc_serie"),dt.getInt("avc_nume"));

      }
    }
    catch (Exception k)
    {
      Error("Error al Ver Datos de Albaran", k);
    }
    opVerdat=false;
  }
  void actGridHist(int empCodi,int avcAno,String avcSerie, int avcNume) throws SQLException
  {
            jtHist.setEnabled(false);
            jtHist.removeAllDatos();
            s="SELECT * FROM hisalcave WHERE emp_codi = "+empCodi+
              " and avc_ano = "+avcAno+
              " and avc_serie = '"+avcSerie+"'"+
              " and avc_nume = "+avcNume+
              " order by  his_rowid desc";
            if (dtCon1.select(s))
            {
                ArrayList v1=new ArrayList();
                v1.add("");
                v1.add("");
                v1.add("** ACTUAL **");
                v1.add(0);
                jtHist.addLinea(v1);
                do
                {
                    ArrayList v=new ArrayList();
                    v.add(dtCon1.getTimeStamp("his_fecha"));
                    v.add(dtCon1.getString("his_usunom"));
                    v.add(dtCon1.getString("his_coment"));
                    v.add(dtCon1.getInt("his_rowid"));
                    jtHist.addLinea(v);
                } while (dtCon1.next());
                jtHist.requestFocus(0,0);
                jtHist.setEnabled(true);
            }
  }
  /**
   * Muestra los productos de Reciclaje (mer, grasa y hueso) en un albaran de venta.
   * @param empCodi
   * @param avcAno
   * @param avcSerie
   * @param avcNume
   * @throws SQLException M
   */
  void verDatProdRec(int empCodi,int avcAno, String avcSerie,int avcNume) throws SQLException
  {
      
      jtRes.removeAllDatos();
      s="select l.*,a.pro_nomb from albvenres as l left join v_articulo as a "+
          " on l.pro_codi = a.pro_codi where avc_ano =" + avcAno +
        " and l.emp_codi = " + empCodi +
        " and avc_serie = '" + avcSerie+ "'" +
        " and avc_nume = " + avcNume+
          " order by avr_numlin";
      if (!dtCon1.select(s))
          return;
      ArrayList<ArrayList> da=new ArrayList();
      do {
          ArrayList d=new ArrayList();
          d.add(dtCon1.getInt("pro_codi"));
          d.add(dtCon1.getString("pro_nomb"));
          d.add(dtCon1.getDouble("avr_canti"));
          d.add(dtCon1.getInt("avr_numlin"));
          da.add(d);
      } while (dtCon1.next());
      jtRes.addLineas(da);
  }
  /**
   * Devuelve true si encuentra albaranes. False en caso contrario
   * En el DatosTabla mandado estaran los albaranes internos que tuviera este
   * albaran.
   * 
   * @throws SQLException
   */
   boolean getAlbDepos(DatosTabla dt ) throws SQLException
   {
    s = "SELECT avs_nume,avs_fecha FROM albvenserc as a " +
        " WHERE a.emp_codi = " + emp_codiE.getValorInt() +
        " AND a.avc_ano = " + avc_anoE.getValorInt() +
        " and a.avc_nume = " + avc_numeE.getValorInt() +
        " and a.avc_serie = '" + avc_seriE.getText() + "'" +
        " order by avs_nume ";
    return dt.select(s);
  }

  void verIconoListado(int avc_impres)
  {
    avcImpres=avc_impres;
    if ( (avcImpres & 3) == 3)
    {
      printE.setIcon(Iconos.getImageIcon("printmod"));
      printE.setToolTipText("Impreso y Modificado");
    }
    else if ( (avcImpres & 1) == 1)
    {
      printE.setIcon(Iconos.getImageIcon("printer"));
      printE.setToolTipText("Albaran Impreso");
    }
    else
    {
      printE.setIcon(null);
      printE.setToolTipText("");
    }
  }
  /**
   * Devuelve la fecha minima de mvto para un albaran
   * @param avcAno
   * @param empCodi
   * @param avcSerie
   * @param avcNume
   * @return
   * @throws SQLException 
   */
  java.sql.Date getMinFechaMvto( int avcAno,int empCodi,String avcSerie, int avcNume) throws SQLException
  {
    dtStat.select("SELECT min(avl_fecalt) as avl_fecalt FROM v_albavel WHERE "+
         " avc_ano =" + avcAno +
        " and emp_codi = " + empCodi +
        " and avc_serie = '" + avcSerie+ "'" +
        " and avc_nume = " + avcNume);
    return dtStat.getDate("avl_fecalt");
  }
  public static boolean selCabAlb(DatosTabla dt,int avcAno,int empCodi,String avcSerie,int avcNume,boolean block,boolean excepNotFound) throws SQLException
  {
      return selCabAlb(TABLACAB,dt, avcAno, empCodi, avcSerie, avcNume, block, excepNotFound);
  }
  public static boolean selCabAlb(String tablaCab,DatosTabla dt,int avcAno,int empCodi,String avcSerie,int avcNume,boolean block,boolean excepNotFound) throws SQLException
  {
    return AvcPanel.selCabAlb(tablaCab, dt, avcAno, empCodi, avcSerie, avcNume, block, excepNotFound);
  }

  void actCabecAlb(int empCodi,int avcAno,String avcSerie,int avcNume,boolean cierra) throws SQLException
  {
    try
    {
        datCab.actDatosAlb(empCodi, avcAno, avcSerie, avcNume,
                cli_codiE.getLikeCliente().getInt("cli_exeiva") == 0 && emp_codiE.getValorInt() < 90, 
                avc_dtoppE.getValorDec(),avc_dtocomE.getValorDec(),
                cli_codiE.getLikeCliente().getInt("cli_recequ"), avc_fecalbE.getDate());
    } catch (ParseException ex)
    {
       Error("Error al actualizar cabecera de albaran", ex);
       return;
    }

    if ( datCab.getCambioIva())
        throw new SQLException("ALBARAN "+empCodi+"-"+avcAno+"-"+avcSerie+"/"+avcNume+
                            " ERRONEO ... TIENE TIPOS DE IVA DIFERENTES");

    selCabAlb(dtAdd,avc_anoE.getValorInt(), emp_codiE.getValorInt() ,avc_seriE.getText(),
              avc_numeE.getValorInt(),true,true);

    dtAdd.edit(dtAdd.getCondWhere());
    avc_impalbE.setValorDec(datCab.getValDouble("avc_impalb"));
    impDtoE.setValorDec(datCab.getValDouble("avc_impdpp")+datCab.getValDouble("avc_impdco"));
    impLinE.setValorDec(datCab.getValDouble("avc_impbru"));
    dtAdd.setDato("avc_depos",avc_deposE.getValor());
    dtAdd.setDato("avc_impalb", datCab.getValDouble("avc_impalb"));
    dtAdd.setDato("avc_basimp", datCab.getValDouble("avc_basimp"));
    dtAdd.setDato("avc_kilos", datCab.getValDouble("kilos"));
    if (! cierra)
      return;
    dtAdd.update(stUp);
    if (fvc_numeE.getValorInt() > 0)
      actImpFra();
  }

  /**
   * Actualizar Importe de Factura
   * @throws Throwable En caso de error de BD
   */
  void actImpFra() throws SQLException
  {
    double sumtot;
    if (! datCab.actDatosFra(fvc_anoE.getValorInt() ,emp_codiE.getValorInt()  ,fvc_serieE.getText() ,fvc_numeE.getValorInt()))
      return;
    if ( datCab.getCambioIva())
        throw new SQLException("FACTURA ERRONEA ... TIENE TIPOS DE IVA DIFERENTES");

    s = "SELECT * FROM v_facvec WHERE fvc_ano = " + fvc_anoE.getValorInt() +
        " and emp_codi = " + emp_codiE.getValorInt() +
        " and fvc_serie = '"+fvc_serieE.getText()+"'"+
        " and fvc_nume = " + fvc_numeE.getValorInt();
    if (!dtAdd.select(s, true))
      return;
    if (dtAdd.getString("fvc_modif").equals("M"))
      return;
    sumtot=dtAdd.getDouble("fvc_sumtot");
    dtAdd.edit();
    dtAdd.setDato("fvc_basimp",datCab.getValDouble("fvc_basimp"));
    dtAdd.setDato("fvc_imprec", datCab.getValDouble("fvc_impree"));
    dtAdd.setDato("fvc_impiva", datCab.getValDouble("fvc_impiva"));
    dtAdd.setDato("fvc_sumlin", datCab.getValDouble("fvc_impbru"));
    dtAdd.setDato("fvc_sumtot", datCab.getValDouble("fvc_sumtot"));
    dtAdd.setDato("fvc_poriva", datCab.getValDouble("fvc_tipiva"));
    dtAdd.setDato("fvc_porreq", datCab.getValDouble("fvc_tipree"));
    dtAdd.update(stUp);
    // Busco  giros SIN Remesar
    s="SELECT * FROM v_recibo WHERE eje_nume = "+fvc_anoE.getValorInt()+
        " and emp_codi = " + emp_codiE.getValorInt() +
        " and fvc_serie = '"+fvc_serieE.getText()+"'"+
        " and fvc_nume = " + fvc_numeE.getValorInt()+
        " and rem_codi = 0 ";
    if (!dtAdd.select(s, true))
      return;
    if (dtAdd.getDouble("rec_import")==sumtot)
    {
      dtAdd.edit();
      dtAdd.setDato("rec_import",datCab.getValDouble("fvc_sumtot"));
      dtAdd.update(stUp);
    }
    else
        msgBox("Factura TIENE recibos cuyo IMPORTE no coincide con el de la Fra\n"+
               "AVISE AL DEPARTAMTENTO DE TESORERIA");
  }

  void verDatLin(DatosTabla dt, boolean agrupa, boolean incIva) throws
      Exception
  {
    verDatLin(dt.getInt("avc_ano"), hisRowid>0?-1:dt.getInt("emp_codi"),
              dt.getString("avc_serie"),
              hisRowid>0?hisRowid:dt.getInt("avc_nume"), dt.getDouble("avc_impcob"), agrupa, incIva);
  }
  /**
   * Setencia sql que devuelve las lineas albaran con producto para entrega
   * @param avsNume Numero de Albaran
   * @return String con la sntencia SQL.
   */
  public static String getSqlLinAlbEnt(int avsNume)
  {
    String s= "SELECT avs_numlin as avl_numlin,l.pro_codi, " +
          " avs_canti as avl_canti, avs_unid as avl_unid," +
          " a.pro_nomb,a.pro_indtco, " +
          " l.pro_nomb as avl_pronom " +
          " FROM albvenserl as l left join v_articulo as a on l.pro_codi = a.pro_codi " +
          " WHERE l.avs_nume = " +avsNume +
          " order by avl_numlin ";
    return s;
  }
  public static String getSqlLinAlb(String tablaLin,int ano,int empCodi,String serie,int nume)
  {
    String s= "SELECT avl_numlin,l.pro_codi, " +
          " avl_canti, avl_unid," +
          " avl_prven,avl_prepvp,avl_profer,tar_preci,a.pro_nomb,a.pro_indtco, " +
          " l.pro_nomb as avl_pronom,avl_numpal, a.pro_tipiva,l.alm_codi,avl_coment,avl_fecalt " +
          " FROM "+tablaLin+"  as l left join v_articulo as a on l.pro_codi = a.pro_codi " +
          " WHERE l.avc_ano = " + ano +
          " and l.emp_codi = " + empCodi +
          " and avc_ano = " + ano +
          " and avc_nume = " + nume +
          " and avc_serie = '" + serie + "'" +
          " order by avl_numlin ";
    return s;
  }
  public static String getSqlLinAgr(int ano, int empCodi,String serie, int nume,boolean modPrecio)
  {
      return getSqlLinAgr(TABLALIN,ano,empCodi,serie,nume,modPrecio);
  }
  /**
   * Devuelve la setencia SQL para mostrar las lineas de un albaran agrupadas
   * @param tablaLin
   * @param ano Ejercicio
   * @param empCodi Empresa de Albaran
   * @param serie Serie
   * @param nume Numero
   * @param modPrecio Mostrar precio ?
   * @return Sentencia SQL a ejecutar
   */
  public static String getSqlLinAgr(String tablaLin,int ano, int empCodi,String serie, int nume,boolean modPrecio)
  {

    return "SELECT -1 as avl_numlin,l.pro_codi,avl_numpal,sum(avl_canti) as avl_canti, " +
         " sum(avl_unid) as avl_unid,"+
         (modPrecio? " avl_prven,":"")+
         "tar_preci,a.pro_nomb,l.pro_nomb as avl_pronom,"
        + " a.pro_tipiva,a.pro_indtco,l.alm_codi,avl_coment " +
         (modPrecio? " ,avl_prepvp,avl_profer":"")+
         " FROM "+tablaLin+" as l left join v_articulo as a on l.pro_codi = a.pro_codi " +
//         " and a.emp_codi = "+EU_emCod+
         " WHERE l.avc_ano = " + ano +
         " and l.emp_codi = " + empCodi +
         " and l.avc_serie = '" + serie + "'" +
         " and l.avc_nume = " + nume +
         " and l.avl_canti >= 0 " +
         " group by l.pro_codi,"+(modPrecio?"avl_prven,avl_prepvp,avl_profer,":"")+
         "tar_preci,a.pro_nomb,l.pro_nomb,a.pro_tipiva,a.pro_indtco,l.alm_codi,avl_coment,avl_numpal " +
         " UNION ALL " +
         "SELECT -1 as avl_numlin,l.pro_codi,avl_numpal,sum(avl_canti) as avl_canti, " +
         " sum(avl_unid) as avl_unid,"+
         (modPrecio? " avl_prven,":"")+
         "tar_preci, a.pro_nomb,l.pro_nomb as avl_pronom,"
        + "a.pro_tipiva,a.pro_indtco,l.alm_codi,avl_coment " +
          (modPrecio? " ,avl_prepvp,avl_profer ":"")+
         " FROM "+tablaLin+" as l left join v_articulo as a on l.pro_codi = a.pro_codi " +
//         " and a.emp_codi = "+EU_emCod+
         " WHERE l.avc_ano = " + ano +
         " and l.emp_codi = " + empCodi +
         " and l.avc_serie = '" + serie + "'" +
         " and l.avc_nume = " + nume +
         " and l.avl_canti < 0 " +
         " group by l.pro_codi,"+(modPrecio?"avl_prven,avl_prepvp,avl_profer,":"")+
         " avl_numpal,tar_preci,a.pro_nomb,l.pro_nomb,a.pro_tipiva,a.pro_indtco,l.alm_codi,avl_coment " +
         " ORDER BY 3,2";
  }
  public static String getSqlLinList(int ano, int empCodi,String serie, int nume,boolean modPrecio)
  {
      return getSqlLinList("v_albavel",ano,empCodi,serie,nume,modPrecio);
  }
  /**
   * Sentencia SQL para listar el albarán
   * @param tablaLin
   * @param ano
   * @param empCodi
   * @param serie
   * @param nume
   * @param modPrecio consultar campo precio (true)
   * @return
   */
  public static String getSqlLinList(String tablaLin,int ano, int empCodi,String serie, int nume,boolean modPrecio)
  {

    return "SELECT -1 as avl_numlin,l.pro_codi,avl_numpal,sum(avl_canti) as avl_canti, " +
         " sum(avl_unid) as avl_unid,tar_preci,"+
         (modPrecio? " avl_prven,":"")+
         "a.pro_nomb,l.pro_nomb as avl_pronom,  a.pro_tipiva,a.pro_indtco " +
         " FROM "+tablaLin +" as l left join v_articulo as a on l.pro_codi = a.pro_codi " +
         " WHERE "+(empCodi<=0?" his_rowid ="+nume:
         " l.avc_ano = " + ano +
         " and l.emp_codi = " + empCodi +
         " and l.avc_serie = '" + serie + "'" +
         " and l.avc_nume = " + nume )+
         " and l.avl_canti >= 0 " +
         " group by l.pro_codi,"+(modPrecio?"avl_prven,":"")+
         " avl_numpal,tar_preci,a.pro_nomb,l.pro_nomb,a.pro_tipiva,a.pro_indtco " +
         " UNION ALL " +
         "SELECT -1 as avl_numlin,l.pro_codi,avl_numpal,sum(avl_canti) as avl_canti, " +
         " sum(avl_unid) as avl_unid,tar_preci,"+
         (modPrecio? " avl_prven,":"")+
         " a.pro_nomb,l.pro_nomb as avl_pronom,a.pro_tipiva,a.pro_indtco " +
         " FROM "+tablaLin+" as l left join v_articulo as a on l.pro_codi = a.pro_codi " +
//         " and a.emp_codi = "+EU_emCod+
         " WHERE "+(empCodi<=0?" his_rowid ="+nume:
         " l.avc_ano = " + ano +
         " and l.emp_codi = " + empCodi +
         " and l.avc_serie = '" + serie + "'" +
         " and l.avc_nume = " + nume )+
         " and l.avl_canti < 0 " +
         " group by l.pro_codi,"+(modPrecio?"avl_prven,":"")+
          " avl_numpal,tar_preci,a.pro_nomb,l.pro_nomb,a.pro_tipiva,a.pro_indtco " +
         " ORDER BY 3,2";
  }
  /**
   * Devuelve sentencia SQL de las lineas de albarán agrupadas por producto 
   * para albarán de entrega de producto en deposito
   * @param avsNume Numero d Albarán
   * @return
   */
  public static String getSqlLinListEnt(int avsNume)
  {
    return "SELECT -1 as avl_numlin,l.pro_codi,sum(avs_canti) as avl_canti, " +
         " sum(avs_unid) as avl_unid,"+
         "a.pro_nomb,a.pro_indtco,l.pro_nomb as avl_pronom " +
         " FROM albvenserl as l left join v_articulo as a on l.pro_codi = a.pro_codi " +
         " WHERE l.avs_nume = " + avsNume+
         " and l.avs_canti >= 0 " +
         " group by l.pro_codi, "+
         " a.pro_nomb,l.pro_nomb,pro_indtco " +
         " ORDER BY pro_codi";
  }
  /**
   * Ver Datos de Linea de Albaran
   * @param ano int 
   * @param empCodi int si es < 0 El numero se refiere al numero de Historico
   * @param serie String
   * @param nume int
   * @param avcImpcob double
   * @param agrupa boolean
   * @param incIva boolean
   * @throws Exception
   */
  void verDatLin(int ano, int empCodi, String serie, int nume, double avcImpcob,
                 boolean agrupa, boolean incIva) throws Exception
  {
     String proNomb;
     swActDesg = false;
     jt.setEnabled(false);
     jtDes.setEnabled(false);
     if (!jt.isVacio())
      jt.removeAllDatos();
     jtDes.removeAllDatos();
     if (swEntdepos)
     {
         verLinEnt(agrupa);
         return;
     }
     if (verDepoC.getValor().equals("S"))
     { // Ver lo servido
        verLinServido(empCodi,ano,serie,nume);
        return;
     }
     if (verDepoC.getValor().equals("P"))
     { // Ver lo Pendiente de servir
        verLinPend(empCodi,ano,serie,nume);
        return;
     }
     if (agrupa)
      s=getSqlLinList(tablaLin,ano,empCodi,serie,nume,verPrecios);
    else
      s =getSqlLinAlb(tablaLin,ano,empCodi,serie,nume);

    int nLin = 0;
    int tipIva = -1;
    boolean swCamTipIva = false;
//    double recEqu;
    double impIva = 0, kilosT = 0;
    double impReq = 0, impLin, impBim = 0;
    double impDtCom=0;
    impDtoCom=0;
    impDtoPP=0;
//    avc_valoraE.setSelected(false);
    if (dtCon1.select(s))
    {
      do
      {
        ArrayList v = new ArrayList();
        nLin++;
        if (dtCon1.getInt("pro_codi", true) == 0)
        {
          String msgErr = "Atencion PRODUCTO de Linea: " + nLin + "ESTA VACIO.\n" +
              " ESTO ES DEBIDO A UN ERROR DEL PROGRAMA\n" +
              " AVISE A INFORMATICA";

          mensajes.mensajeAviso(msgErr);
          enviaMailError("Albaran: " + empCodi + "/" + ano + "-" + nume +
                         "\n" + msgErr + "\n" + systemOut.getMessage());
        }
//          pro_codiE.getNombArt(dtCon1.getString("pro_codi"));
        proNomb = dtCon1.getString("avl_pronom");
        if (proNomb == null)
          proNomb = dtCon1.getString("pro_nomb");
        v.add(dtCon1.getString("avl_numlin"));
        v.add(dtCon1.getString("pro_codi"));
        v.add(proNomb);
        v.add(""+Formatear.redondea(dtCon1.getDouble("avl_canti",true), 2));
        v.add(dtCon1.getString("avl_unid"));
       
//        if (dtCon1.getDouble("avl_prven") != 0)
//          avc_valoraE.setSelected(true);
        if (P_MODPRECIO || P_PONPRECIO)
        {
          if (!verPrecios)
          {
             v.add("0");
             v.add("0");
          }
          else
          {
              v.add(""+Formatear.redondea(dtCon1.getDouble("avl_prven",true),NUMDECPRECIO));
              if (dtCon1.getDouble("tar_preci")==0)
              { // Si no tiene precio tarifa guardado en el alb. pongo el precio de tarifa estandard
                v.add(""+
                  MantTarifa.getPrecTar(dtStat,dtCon1.getInt("pro_codi"), tar_codiE.getValorInt(), avc_fecalbE.getText()));
              }
              else
                v.add(dtCon1.getString("tar_preci"));
          }
        }

       
        if (tipIva != -1 &&
            tipIva != dtCon1.getInt("pro_tipiva") &&
            dtCon1.getDouble("avl_canti") != 0)
          swCamTipIva = true;
        v.add(agrupa?"":Formatear.getFecha(dtCon1.getTimeStamp("avl_fecalt"),"dd-MM-yy HH:mm"));
        v.add(dtCon1.getInt("avl_numpal"));
        tipIva = dtCon1.getInt("pro_tipiva"); // pro_codiE.getLikeProd().getDatoInt("pro_tipiva");
        jt.addLinea(v);
        if (verPrecios)
        {
          impLin = Formatear.redondea(Formatear.redondea(dtCon1.getDouble("avl_canti", true), 2) *
                                      Formatear.redondea(dtCon1.getDouble("avl_prven", true), NUMDECPRECIO), NUMDEC);
          impBim += impLin;
          impDtCom+=dtCon1.getInt("pro_indtco")==0?0:impLin;    
        }
        kilosT += dtCon1.getDouble("avl_canti");
      }  while (dtCon1.next());
      impBim=Formatear.redondea(impBim,NUMDEC);
      avl_prvenE.setCambio(true);
      jt.requestFocusInicio();
      if (swCamTipIva)
        msgBox("ALBARAN ERRONEO ... TIENE TIPOS DE IVA DIFERENTES");
      numLinE.setValorDec(nLin);
      kilosE.setValorDec(kilosT);
      impLinE.setValorDec(impBim);
//      double dtos=Formatear.redondea(avc_dtoppE.getValorDec() + avc_dtocomE.getValorDec(),NUMDEC);

      if (avc_dtoppE.getValorDec() != 0)
        impDtoPP=Formatear.redondea(impBim * (avc_dtoppE.getValorDec() / 100),NUMDEC);
    
      if (avc_dtocomE.getValorDec()!=0)
          impDtoCom=Formatear.redondea(impDtCom*(avc_dtocomE.getValorDec()/100),NUMDEC);
      
      impDtoE.setValorDec(impDtoCom+impDtoPP);
      impBim = Formatear.redondea(impBim - impDtoE.getValorDec(),NUMDEC);
      if (empCodi < 90 && incIva)
      {
        DatosIVA dtIva=MantTipoIVA.getDatosIva(dtStat, tipIva,avc_fecalbE.getDate());

        if (dtIva!=null)
        {
          impIva = Formatear.redondea(impBim * dtIva.getPorcIVA() / 100,NUMDEC);
          if (cli_codiE.getLikeCliente().getInt("cli_recequ") == -1)
            impReq = Formatear.redondea(impBim * dtIva.getPorcREQ() / 100,NUMDEC);
        }
        else
        {
          msgBox("Tipo de Iva (" +tipIva+
                 ") de Prod: " + dtCon1.getString("pro_Codi") +
                 " NO ENCONTRADO");
        }
      }
      avc_impalbE.setValorDec(Formatear.redondea(impBim + impIva + impReq,NUMDEC));
      avc_impcobE.setValorDec(avcImpcob);

      swActDesg = true;
       
      verDesgLinea(hisRowid<=0?empCodi:-1,
                    ano,
                   serie,
                   hisRowid<=0?nume:hisRowid,
                   jt.getValorInt(0, 0),
                   jt.getValorInt(0, JT_PROCODI), 
                   jt.getValorInt(0, JT_NUMPALE),
                   jt.getValString(0, 2), 
                   verPrecios?jt.getValorDec(0, 5):0);
      actModPrecio();
      antPrecio = verPrecios?jt.getValorDec(5):0;
//      debug("Ant. Precio: "+antPrecio);
    }
    else
      swActDesg = true;
  }
  /**
   * Ver lineas con lo pendiente de servir
   * @param empCodi
   * @param avcAno
   * @param serie
   * @param numAlb
   * @throws SQLException
   */
  private void verLinPend(int empCodi, int avcAno, String serie, int numAlb)
          throws  SQLException
  {
      s = "select -1 as avl_numlin,l.pro_codi, sum(p.avp_canti)  as avl_canti,"
              + " sum(p.avp_numuni)  as avl_unid, "
              + " a.pro_nomb,l.pro_nomb as avl_pronom "
              + "from v_albavel as l  left join v_articulo as a on l.pro_codi = a.pro_codi,v_albvenpar as p "
              + "where l.emp_codi=" + empCodi
              + " and l.avc_ano= " + avcAno
              + " and l.avc_serie='" + serie + "'"
              + " AND l.avc_nume= " + numAlb
              + " and l.emp_codi=p.emp_codi and l.avc_ano=p.avc_ano "
              + "and l.avc_serie=p.avc_serie "
              + "and l.avc_nume=p.avc_nume"
              + " and l.avl_numlin=p.avl_numlin "
              + " and not exists (select avs_numuni from  albvenserc as c, albvenseri as i "
              + "where l.emp_codi=c.emp_codi and l.avc_ano=c.avc_ano "
              + " and l.avc_serie=c.avc_serie and l.avc_nume=c.avc_nume "
              + "and c.avs_nume= i.avs_nume and p.avp_ejelot = i.avs_ejelot "
              + "and p.avp_serlot=i.avs_serlot and p.avp_numpar=i.avs_numpar "
              + "and p.avp_numind = i.avs_numind ) "
              + "group by l.pro_codi,a.pro_nomb,l.pro_nomb order by l.pro_codi ";
       llenaGridLineas(s);
       swActDesg = true;
       if (jt.isVacio())
       {
           msgBox("Albaran de deposito totalmente servido");
           return;
       }
      
       verDesgLinea(empCodi,avcAno, serie, numAlb, -1, jt.getValorInt(0, JT_PROCODI),
           jt.getValorInt(0, JT_NUMPALE),jt.getValString(0, 2),
           jt.getValorDec(0, 5));

  }
  /**
   * Ver linas de genero ya servido
   * Siempre muestar las lineas agrupadas
   */
  private void verLinServido(int empCodi, int avcAno, String serie, int numAlb)
          throws  SQLException
  {
      s ="SELECT -1 as avl_numlin,l.pro_codi,sum(l.avs_canti) as avl_canti, " +
         " sum(l.avs_unid) as avl_unid,"+
         "a.pro_nomb,l.pro_nomb as avl_pronom " +
         " FROM albvenserc as c,albvenserl as l left join v_articulo as a on l.pro_codi = a.pro_codi " +
         " WHERE l.avs_nume = c.avs_nume "+
         " and c.emp_codi = "+empCodi+
         " and c.avc_ano ="+avcAno+
         " and c.avc_serie='"+serie+"'"+
         " and c.avc_nume = "+numAlb+
         " and l.avs_canti >= 0 " +
         " group by l.pro_codi, "+
         " a.pro_nomb,l.pro_nomb " +
         " ORDER BY pro_codi";
       llenaGridLineas(s);
       swActDesg = true;
       verDesgLinea(empCodi,avcAno,serie,numAlb, -1, jt.getValorInt(0, JT_PROCODI),
           jt.getValorInt(0, JT_NUMPALE),
           jt.getValString(0, 2),  jt.getValorDec(0, 5));
  }
  /**
   * Llena el grid de lineas de albaran
   * @param sql Sentencia sql a ejecutar para sacar los registros a mandar.
   *
   * @throws SQLException
   */
  void llenaGridLineas(String sql) throws SQLException
  {
     String proNomb;
       if (dtCon1.select(sql)) {
            do {
                ArrayList v = new ArrayList();
                proNomb = dtCon1.getString("avl_pronom");
                if (proNomb == null) {
                    proNomb = dtCon1.getString("pro_nomb");
                }
                v.add(dtCon1.getString("avl_numlin"));
                v.add(dtCon1.getString("pro_codi"));
                v.add(proNomb);
                v.add("" + Formatear.redondea(dtCon1.getDouble("avl_canti", true), 2));
                v.add(dtCon1.getString("avl_unid"));
                if (verPrecios) {
                    v.add("0");
                    v.add("0");
                }
                v.add(""); 
                v.add(0);
                jt.addLinea(v);
            } while (dtCon1.next());
            jt.requestFocusInicio();
        }
  }
  
  /**
   * Ver Lineas de un albaran de entrega de genero
   * @param agrupa
   * @throws IllegalArgumentException
   * @throws SQLException
   */
    private void verLinEnt(boolean agrupa) throws IllegalArgumentException, SQLException {
       
        avsNume = verDepoC.getValorInt();
        if (agrupa) {
            s = getSqlLinListEnt(avsNume);
        } else {
            s = getSqlLinAlbEnt(avsNume);
        }
        llenaGridLineas(s);
        swActDesg = true;
        verDesgLinea(0,0, "", 0, jt.getValorInt(0, 0), jt.getValorInt(0, JT_PROCODI),
            jt.getValorInt(0, JT_NUMPALE),
             jt.getValString(0, 1),jt.getValorDec(0, 5));
    }
    
    String getStrSqlDesgPend(int empCodi, int avcAno, String serie, int numAlb,int proCodi)
    {
      return "select  l.pro_codi,a.pro_nomb,avp_emplot as avp_emplot,"
              + "avp_ejelot as avp_ejelot,"
              + " p.avp_serlot as avp_serlot, "
              + " avp_numpar as avp_numpar,"
              + "avp_numind as avp_numind,"
              + " p.avp_canti as avp_canti, avp_numuni as avp_numuni,avp_numlin "
              + "from v_albavel as l  left join v_articulo as a on l.pro_codi = a.pro_codi,v_albvenpar as p "
              + "where l.emp_codi=" + empCodi
              + " and l.avc_ano= " + avcAno
              + " and l.avc_serie='" + serie + "'"
              + " AND l.avc_nume= " + numAlb
              + " and l.pro_codi = "+proCodi
              + " and l.emp_codi=p.emp_codi and l.avc_ano=p.avc_ano "
              + "and l.avc_serie=p.avc_serie "
              + "and l.avc_nume=p.avc_nume"
              + " and l.avl_numlin=p.avl_numlin "
              + " and not exists (select avs_numuni from  albvenserc as c, albvenseri as i "
              + "where l.emp_codi=c.emp_codi and l.avc_ano=c.avc_ano "
              + " and l.avc_serie=c.avc_serie and l.avc_nume=c.avc_nume "
              + "and c.avs_nume= i.avs_nume and p.avp_ejelot = i.avs_ejelot "
              + "and p.avp_serlot=i.avs_serlot and p.avp_numpar=i.avs_numpar "
              + "and p.avp_numind = i.avs_numind ) "
              + " order by l.pro_codi ";
  }
    
  void verDesgLinea(int empCodi, int ejeNume, String serie, int numAlb,
                    int nLin, int proCodi,int numPale,String proNomb, double precio)
  {
    if (!swActDesg)
      return;

    try
    {
      if (! swEntdepos)
      {
          switch (verDepoC.getValor())
          {
              case "S":
                  s=getStrSqlDesgServ(empCodi, ejeNume, serie, numAlb,proCodi);
                  break;
              case "P":
                  s=getStrSqlDesgPend(empCodi, ejeNume, serie, numAlb, proCodi);
                  break;
              default:
                  s=getStrSqlDesg(vistaInd,
                      hisRowid<=0?empCodi:-1, ejeNume, serie,
                      hisRowid<=0?numAlb:hisRowid, nLin, proCodi,numPale, proNomb, precio,verPrecios);
                  break;
          }
      }
      else
        s=getStrSqlDesgEnt(avsNume, nLin, proCodi);
      boolean enab = jtDes.isEnabled();
      jtDes.setEnabled(false);
      jtDes.removeAllDatos();
      if (!dtCon1.select(s))
      {
        jtDes.setEnabled(enab);
        return;
      }
      double canti=0;
      int unid=0;
      do
      {
        ArrayList v = new ArrayList();
        v.add(dtCon1.getString("pro_codi"));
        v.add(dtCon1.getString("pro_nomb"));
        v.add(dtCon1.getString("avp_emplot"));
        v.add(dtCon1.getString("avp_ejelot"));
        v.add(dtCon1.getString("avp_numuni"));
        v.add(dtCon1.getString("avp_serlot"));
        v.add(dtCon1.getString("avp_numpar"));
        v.add(dtCon1.getString("avp_numind"));
        v.add(dtCon1.getString("avp_canti"));
        v.add(dtCon1.getInt("avp_numlin"));
        canti= Formatear.redondea(canti+dtCon1.getDouble("avp_canti"),2);
        unid+=dtCon1.getInt("avp_numuni");
        
        jtDes.addLinea(v);
      }  while (dtCon1.next());
      if (canti!=jt.getValorDec(jt.getSelectedRowDisab(), 3))
      {
          msgBox(("KILOS DIFERENTES ENTRE LINEAS ("+jt.getValorDec(jt.getSelectedRowDisab(), 3)+") E INDIVIDUOS ("+canti+")"));
      }
       if (unid!=jt.getValorInt(jt.getSelectedRowDisab(),4))
      {
          msgBox(("UNIDADES DIFERENTES ENTRE LINEAS E INDIVIDUOS"+unid+" != "+jt.getValorInt(jt.getSelectedRowDisab(),4)));
      }
      jtDes.requestFocus(0, 0);
      jtDes.setEnabled(enab);
    }
    catch (SQLException k)
    {
      Error("Error al Ver Desglose Linea de Albaran", k);
    }
  }
  public static String getStrSqlDesg(int empCodi, int ejeNume, String serie, int numAlb, int nLin,
                             int proCodi, int numPale,String proNomb,double precio,boolean modPrecio)
  {
      return getStrSqlDesg(VISTAIND ,empCodi,ejeNume,serie, numAlb, nLin,proCodi, numPale,
          proNomb, precio, modPrecio);
  }
/**
 * Devuelve sentencia SQL para mostras los individuos sobre un albaran normal
 *
 * @param vistaDet
 * 
 * @param empCodi Si es <0 El numero de Albaran es el ID del Historico de Albaranes.
 * @param ejeNume
 * @param serie
 * @param numAlb
 * @param nLin Numero Linea Albaran. Si 0 mostrara todo lo que pertenezca a un producto y precio
 * @param proCodi
 * @param numPale
 * @param proNomb Nombre de producto
 * @param precio
 * @param modPrecio Sacar precio (true=si)
 * @return
 */
  public static String getStrSqlDesg(String vistaDet,
      int empCodi, int ejeNume, String serie, int numAlb, int nLin,
                             int proCodi,int numPale, String proNomb,double precio,boolean modPrecio)
  {
    return "SELECT p.pro_codi,p.pro_nomb,a.avp_emplot,a.avp_ejelot,a.avp_serlot, " +
        " a.avp_numpar,a.avp_numind,a.avp_canti,a.avp_numuni,a.avp_numlin "+
        " FROM "+vistaDet+" as a,v_articulo as p " +
        " WHERE "+ (empCodi==-1? " a.his_rowid = "+numAlb: " a.emp_codi = " + empCodi +
        " and a.avc_ano = " + ejeNume +
        " and a.avc_serie = '" + serie + "'" +
        " and a.avc_nume = " + numAlb) +
        " and avl_numpal = "+numPale+
        (nLin >= 0 ? " and a.avl_numlin = " + nLin :
         " and a.pro_codi = " + proCodi +
         (proNomb==null?"":" and (a.pro_nomb is null or a.pro_nomb = '"+proNomb+"')")+
         (modPrecio?" and a.avl_prven = " + precio:"")) +
          " and p.pro_codi = a.pro_codi " +      
        " order by a.avp_numlin";
  }
 /**
  * Devuelve sentencia SQL para buscar lineas desglose albaran de entrega
  * @param avsNume
  * @param nLin
  * @param proCodi
  * @return
  */
 String getStrSqlDesgEnt(int avsNume, int nLin,
                             int proCodi)
  {
      return "SELECT p.pro_codi,p.pro_nomb,avs_emplot as avp_emplot,"
              + "avs_ejelot as avp_ejelot,"
              + "a.avs_serlot as avp_serlot, "
              + " avs_numpar as avp_numpar,"
              + "avs_numind as avp_numind,"
              + "a.avs_canti as avp_canti, 1 as avp_numuni,1 as avp_numlin"
              + " FROM albvenseri as a,v_articulo as p,albvenserl as l "
              + " WHERE l.avs_nume = " + avsNume
              + (nLin >= 0 ? " and l.avs_numlin = " + nLin
              : " and l.pro_codi = " + proCodi)
              + "  and a.avs_numlin = l.avs_numlin "
              + " and p.pro_codi = l.pro_codi "
              + " and l.avs_nume = a.avs_nume "
              + " order by a.avs_emplot,a.avs_ejelot,a.avs_serlot,a.avs_numpar,a.avi_numlin";
  }
  String getStrSqlDesgServ(int empCodi, int ejeNume, String serie, int numAlb,
                             int proCodi)
  {
      return "SELECT p.pro_codi,p.pro_nomb,avs_emplot as avp_emplot,"
              + "avs_ejelot as avp_ejelot,"
              + "a.avs_serlot as avp_serlot, "
              + " avs_numpar as avp_numpar,"
              + "avs_numind as avp_numind,"
              + "a.avs_canti as avp_canti, 1 as avp_numuni,1 as avp_numlin "
              + " FROM albvenseri as a,v_articulo as p,albvenserl as l,albvenserc as c "
              + " WHERE l.avs_nume = c.avs_nume "
              + " and c.emp_codi = " + empCodi
              + " and c.avc_ano = " + ejeNume
              + " and c.avc_serie = '" + serie + "'"
              + " and c.avc_nume = " + numAlb
              + " and l.pro_codi = " + proCodi
              + " and p.pro_codi = l.pro_codi "
              + " and l.avs_nume = a.avs_nume "
              + " and a.avs_numlin = l.avs_numlin "
              + " order by a.avs_emplot,a.avs_ejelot,a.avs_serlot,a.avs_numpar,a.avi_numlin";
  }
  public static String getNombreClase()
  {
   return "gnu.chu.anjelica.ventas.pdalbara";
  }
  public void setSerieAlbaran(String serie)
  {
      avc_seriE.setText(serie);
  }
  public void setNumeroAlbaran(String numAlbaran)
  {
      avc_numeE.setText(numAlbaran);
  }
  public void setNumeroAlbaran(int numAlbaran)
  {
      avc_numeE.setValorInt(numAlbaran);
  }
  public void setEjercAlbaran(int avcAno)
  {
      avc_anoE.setValorInt(avcAno);
  }
  public void setEmpresaAlbaran(int empCodi)
  {
      emp_codiE.setValorInt(empCodi);
  }
  public boolean inTransation()
  {
      return (nav.getPulsado()==navegador.ADDNEW || nav.getPulsado()==navegador.EDIT || nav.getPulsado()==navegador.DELETE);
  }
  @Override
  public void PADQuery()
  {
    try {
      resetBloqueo(dtAdd);
    } catch (SQLException | ParseException k){}
    mensaje("Introduzca Criterios de Busqueda");
    nav.pulsado = navegador.QUERY;
    activar(true);
    Birgrid.setEnabled(false);
    Bimpri.setEnabled(false);
    jtDes.setEnabled(false);
    tar_codiE.setEnabled(false);
    Pcabe.setQuery(true);
    avc_revpreE.setQuery(true);
    jt.setEnabled(false);
    pvc_anoE.setEnabled(false);
    pvc_numeE.setEnabled(false);
    avc_obserE.setEnabled(false);
    Pcabe.resetTexto();
    if (Integer.parseInt(Formatear.getFechaAct("MM"))<2)
        avc_anoE.setText(">="+(EU.ejercicio-1));
    else
        avc_anoE.setValorDec(EU.ejercicio);
    emp_codiE.setValorDec(EU.em_cod);
    if (EU.getSbeCodi()!=0)
      sbe_codiE.setValorInt(EU.getSbeCodi());
    cli_codiE.requestFocus();
  }

  @Override
  public void ej_query1()
  {

      Component c = Pcabe.getErrorConf();
      if (c != null)
      {
        mensajeErr("Condiciones de Busqueda NO validas");
        c.requestFocus();
        return;
      }
      new miThread("")
      {
        @Override
        public void run()
        {
          buscaAlb();
        }
      };
    }
    void buscaAlb()
    {
      this.setEnabled(false);
      try
      {
      ArrayList v = new ArrayList();
      v.add(cli_codiE.getStrQuery());
      v.add(emp_codiE.getStrQuery());
      v.add(avc_anoE.getStrQuery());
      v.add(avc_seriE.getStrQuery());
      v.add(avc_numeE.getStrQuery());
      v.add(avc_fecalbE.getStrQuery());
      v.add(usu_nombE.getStrQuery());
      v.add(avc_fecemiE.getStrQuery());
      v.add(avc_revpreE.getStrQuery());
      v.add(fvc_anoE.getStrQuery());
      v.add(fvc_serieE.getStrQuery());
      v.add(fvc_numeE.getStrQuery());
      v.add(rut_codiE.getStrQuery());
      v.add(avc_dtoppE.getStrQuery());
      v.add(avc_dtocomE.getStrQuery());
      v.add(avc_valoraE.getStrQuery());
      v.add(div_codiE.getStrQuery());
      v.add(avc_deposE.getStrQuery());
      v.add(sbe_codiE.getStrQuery());
      s = getStrSql("","");

      s = creaWhere(s, v,  false);
      s += " ORDER BY avc_ano,avc_serie desc,avc_nume ";

      mensaje("Espere, por favor ... buscando datos");
      Pcabe.setQuery(false);
      avc_revpreE.setQuery(false);
      
//      debug("s: "+s);
      if (!dtCon1.select(s))
      {
        msgBox("No encontrados Albaranes con estos criterios");
        rgSelect();
        activaTodo();
        this.setEnabled(true);
        actModPrecio();
        return;
      }
      strSql = s;
      activaTodo();
      actModPrecio();

      this.setEnabled(true);
      rgSelect();
      mensaje("");
      mensajeErr("Nuevas Condiciones ... Establecidas");
    }
    catch (Exception k)
    {
      Error("Error al buscar datos", k);
    }

    nav.pulsado = navegador.NINGUNO;
  }

  void actModPrecio()
  {
    if (verPrecios && (fvc_anoE.getValorInt() == 0 || P_ADMIN) &&
        (fvc_numeE.getValorInt() == 0 || P_ADMIN) && ! traspCont && ! traspReci && ! accCerra)
    {
      pro_codiE.setEditable(false);
      pro_nombE.setEditable(false);
      jt.setEnabled(true);
      jt.requestFocusInicio();
    }
  }

    @Override
  public void canc_query()
  {
    Pcabe.setQuery(false);
    avc_revpreE.setQuery(false);
    activaTodo();
    verDatos(dtCons);
    mensaje("");
    mensajeErr("Consulta ... Cancelada");
    nav.pulsado = navegador.NINGUNO;
  }
  
  boolean isTraspCont() throws Exception
  {
    if ( fvc_anoE.getValorDec() == 0 || fvc_numeE.getValorDec() == 0)
      return false;
    s = "SELECT * FROM v_facvec WHERE fvc_ano = " + fvc_anoE.getValorInt() +
        " and emp_codi = " + emp_codiE.getValorInt() +
        " and fvc_serie = '"+fvc_serieE.getText()+"'"+
        " and fvc_nume = " + fvc_numeE.getValorInt();
    if (! dtStat.select(s))
      return false;
    return dtStat.getInt("fvc_trasp")!=0;
  }
  
  private boolean canModif() throws SQLException
  {
      if (verDepoC.getValor().equals("S") || verDepoC.getValor().equals("P") )
      {
          msgBox("Elija un albaran para modificar/borrar. Debe ser el entrega o el facturado");
          return false;
      }
      if ( verDepoC.getValor().equals("O") && swTieneEnt)
      {
          if (P_ADMIN )
          {
              msgBox("ATENCION!!. Este albaran de deposito ya tiene genero entregado");
              nLiMaxEdit=getMaxNumLinAlb(false,emp_codiE.getValorInt(),avc_anoE.getValorInt(),avc_seriE.getText(),
                avc_numeE.getValorInt(),dtCon1); 
          }
          else
          {
            msgBox("Este albaran de deposito, ya tiene genero entregado. Imposible modificar o borrar");
            return false;
          }
      }
      return true;
  }
  
  private boolean canDelete()
  {
      if (verDepoC.getValor().equals("S") || verDepoC.getValor().equals("P") )
      {
          msgBox("Elija un albaran para modificar/borrar. Debe ser el entrega o el facturado");
          return false;
      }
      if ( verDepoC.getValor().equals("O") && swTieneEnt)
      {
            msgBox("Este albaran de deposito, ya tiene genero entregado. Imposible modificar o borrar");
            return false;
      }
      return true;
  }
   public void copiaAlbaranNuevo(DatosTabla dt, DatosTabla dtUpd,String coment,String usuario, 
          int avcAno, int empCodi,String avcSerie,int avcNume) throws SQLException
  {
      String s1="select max(his_rowid) as his_rowid from hisalcave ";
      dt.select(s1);
      int rowid=dt.getInt("his_rowid",true);
      rowid++;

      String condAlb=" emp_codi = "+empCodi+
          " and avc_serie = '" + avcSerie+ "'" +
         " and avc_ano = "+avcAno+
         " and avc_nume = "+avcNume;
      s1 = "select * from "+TABLACAB+" WHERE "+condAlb;
      if (dt.select(s1))
      {
         dtUpd.addNew("hisalcave");
         dt.copy(dtUpd,usuario, coment,rowid);    
      }
      s1 = "select * from v_albavel WHERE "+condAlb;
      if (dt.select(s1))
      {
         dtUpd.addNew("hisallive");
         dt.copy(dtUpd,usuario, coment,rowid);    
      }
      s1 = "select * from v_albvenpar WHERE "+condAlb;
      if (dt.select(s1))
      {
         dtUpd.addNew("hisalpave");
         dt.copy(dtUpd, usuario, coment,rowid);    
      }
      dtUpd.commit();
  }
  
    @Override
  public void PADEdit()
  {         
//   debug("Entrando en Edit: "+ avc_anoE.getValorInt() + "|" + emp_codiE.getValorInt() +
//                      "|" + avc_seriE.getText() + "|" + avc_numeE.getValorInt());
    try
    {
      if (hisRowid!=0)
      {
        msgBox("Viendo albaran historico ... IMPOSIBLE MODIFICAR");
        activaTodo();
        return;
      }
      nLiMaxEdit=0;
      if (! canModif())
      {
          nav.pulsado = navegador.NINGUNO;
          activaTodo();
          return;
      }

      if (! P_MODPRECIO && avc_valoraE.getValor().equals("0"))
      {
        if (jf != null)
        {
          jf.ht.clear();
          jf.ht.put("%a", avc_numeE.getText());
          jf.ht.put("%u", EU.usuario);
          jf.guardaMens("V1", jf.ht);
        }
      }

      if (pdejerci.isCerrado(dtStat, avc_anoE.getValorInt(), emp_codiE.getValorInt()) && ! swEntdepos)
      {
        if (!P_ADMIN)
        {
          msgBox("Albaran es de un ejercicio YA cerrado ... IMPOSIBLE MODIFICAR");
          nav.pulsado = navegador.NINGUNO;
          activaTodo();
          return;
        }
        else
          msgBox("ATENCION!!! Albaran es de un ejercicio YA cerrado");
      }

      if ( (fvc_anoE.getValorDec() > 0 || fvc_numeE.getValorDec() > 0) &&
          !P_ADMIN && ! swEntdepos)
      {
        msgBox("Albaran YA se HA FACTURADO ... IMPOSIBLE MODIFICAR");
        nav.pulsado = navegador.NINGUNO;
        activaTodo();
        return;
      }
      if ( traspReci && ! swEntdepos)
      {
        msgBox("Factura ha emitido algun recibo bancario .. IMPOSIBLE MODIFICAR");
        nav.pulsado = navegador.NINGUNO;
        activaTodo();
        return;
      }
      if ( traspCont && ! swEntdepos)
      {
        msgBox("Factura se traspaso a contabilidad .. IMPOSIBLE MODIFICAR");
        nav.pulsado = navegador.NINGUNO;
        activaTodo();
        return;
      }
      
      swAvisoDto=true;
      //selCabAlb(dtAdd,avc_anoE.getValorInt(), emp_codiE.getValorInt(),avc_seriE.getText() ,avc_numeE.getValorInt(),true);

      if ( ! selCabAlb(dtAdd,avc_anoE.getValorInt(), emp_codiE.getValorInt(),avc_seriE.getText(),
                       avc_numeE.getValorInt(),true,false))
      {
        msgBox("Albaran NO encontrado .. PROBABLEMENTE se ha borrado");
        nav.pulsado = navegador.NINGUNO;
        activaTodo();
        return;
      }
     
      
      if (avcImpres == 1 && ! P_MODPRECIO )
      {
        int res = mensajes.mensajeYesNo("Albaran YA se ha listado\n ¿ Cancelar modficación ?");
        if (res != mensajes.NO)
        {
          nav.pulsado = navegador.NINGUNO;
          activaTodo();
          return;
        }
      }
      if (checkEdicionAlbaran())
        return;

    pro_nombE.setEditable(true);
    avc_valoraE.setValor("0");
//    opModif.setSelected(false);
    activar(true);
//    Bimpri.setEnabled(false);
    avc_numeE.resetCambio();
    resetCambioIndividuo();
    avc_numeE.setEnabled(true);
    avc_seriE.setEnabled(true);
    if (! avc_deposE.getValor().equals("N"))
    {
        if (avc_deposE.getValor().equals("D"))
        {
            if (swTieneEnt)
               avc_deposE.setEnabled(false);
        }
        else
            avc_deposE.setEnabled(false);      
    }
    confAlbDep=true;
    cli_codiE.setEnabled(avsNume==0);
    
//    pvc_anoE.setEnabled(false);
//    pvc_numeE.setEnabled(false);
    jt.setEnabled(false);
    mensaje("Editando Albaran ...");
    opAgru.setEnabled(false);
    if (opAgru.isSelected())
    {
      if (swEntdepos)
        verDatLin(dtCons, false, true);
      else
        verDatos(dtCons, false);
    }
    if (!setBloqueo(dtStat, "v_albavec",
                    avc_anoE.getValorInt() + "|" + emp_codiE.getValorInt() +
                    "|" + avc_seriE.getText() + "|" + avc_numeE.getValorInt(), true))
    {
      msgBox(msgBloqueo);
      nav.pulsado = navegador.NINGUNO;
      activaTodo();
      return;
    }
    copiaAlbaranNuevo(dtCon1,dtAdd,"Modificado Albaran",EU.usuario,avc_anoE.getValorInt(),
              emp_codiE.getValorInt(),avc_seriE.getText(),avc_numeE.getValorInt());
    jtDes.setEnabled(false);
    if (fvc_numeE.getValorInt() > 0 || fvc_anoE.getValorDec() > 0 )
    {
      cli_codiE.setEnabled(false);
      emp_codiE.setEnabled(false);
      avc_seriE.setEnabled(false);
      avc_anoE.setEnabled(false);
      avc_numeE.setEnabled(false);
      avc_revpreE.setEnabled(false);
      if (P_MODPRECIO)
      {
        avc_dtoppE.setEnabled(false);
        avc_dtocomE.setEnabled(false);
      }
    }
    cli_codiE.requestFocus();
    emp_codiE.resetCambio();
    avc_seriE.resetCambio();
    avc_anoE.resetCambio();
    nav.pulsado=navegador.EDIT;
    fvc_serieE.setEnabled(false);
    fvc_numeE.setEnabled(false);
    fvc_anoE.setEnabled(false);
    Bcancelar.setEnabled(false);
    pro_codiE.setEditable(false);
    pro_nombE.setEditable(true);
    avc_almoriE.setEnabled(false);
    usu_nombE.setEnabled(false);
    avc_fecemiE.setEnabled(false);
    if ( swTieneEnt)
       avc_seriE.setEnabled(false);
    nav.setEnabled(false);
    //avc_revpreE.setEnabled(false);
    pro_codiE.setEditable(false);
    Bdespiece.setEnabled(true);
    irGridLin();
  }  catch (Exception k)
    {
      Error("Error al Modificar Albarán", k);
    }

  }
  /**
   *
   * Comprueba si es posible editar un albaran
   *
   * @return true en caso de error. False si todo esta bien.
   * @throws SQLException 
   */
  private boolean checkEdicionAlbaran() throws SQLException
  {
      if (checkEdicionAlbaran0())
      {
          nav.pulsado = navegador.NINGUNO;
          activaTodo();
          return true;
      }
      return false;
  }
  /**
   * Comprueba si es posible editar un albaran
   * @return true en caso de error. False si todo esta bien.
   * @throws SQLException 
   */
  private boolean  checkEdicionAlbaran0() throws SQLException
  {
    if ( avc_seriE.getValor().equals("X"))
    {
        msgBox("Albaranes de serie X ... IMPOSIBLE MODIFICAR O BORRAR");       
        return true;
    }
    if (avsNume==0)
    {
        java.sql.Date fecMinMvt=getMinFechaMvto(avc_anoE.getValorInt(), emp_codiE.getValorInt(),avc_seriE.getText(),
                           avc_numeE.getValorInt());
        if (fecMinMvt!=null)
        {
            if (Formatear.comparaFechas(pdalmace.getFechaInventario(avc_almoriE.getValorInt(), dtStat) , fecMinMvt)>= 0 )
            {
                  msgBox("Albaran con Mvtos anteriores a Ult. Fecha Inventario. Imposible Editar/Borrar");
                  return true;
            }
        }
    }
    
    if (avc_seriE.getText().equals(EntornoUsuario.SERIEY) && swCompra)
    {
      if (MantAlbComCarne.isAlbCompra(dtStat,emp_codiE.getValorInt(),
                               avc_anoE.getValorInt(),avc_numeE.getValorInt(),false))
      {
        msgBox("Albaran se ha utilizado para realizar la compra: " + dtStat.getInt("acc_ano") + "-" +
               dtStat.getInt("acc_nume") + " IMPOSIBLE MODIFICAR O BORRAR");
        return true;
      }
    }
    return false;
  }

  private boolean checkCli() throws SQLException,
      DataFormatException, ParseException
  {
    if (!emp_codiE.controla())
    {
      mensajeErr("Empresa NO es valida");
      return false;
    }
    if (!cli_codiE.controlar())
    {
      mensajeErr(cli_codiE.getMsgError());
      return false;
    }
    if (!cli_codiE.getLikeCliente().getString("cli_activ").toUpperCase().
        startsWith("S"))
    {
      mensajeErr("CLIENTE NO ESTA MARCADO COMO ACTIVO");
      cli_codiE.requestFocus();
      return false;
    }
    if ( avc_seriE.getValor().equals("X"))
    {
        msgBox("IMPOSIBLE DAR DE ALTA ALBARANES SERIE X. Use programa traspaso Almacenes");
        avc_seriE.requestFocus();
        return false;
    }
    if (cli_codiE.getLikeCliente().getInt("cli_intern") == 0)
    {
      if (avc_seriE.getValor().equals("Y"))
      {
        mensajeErr("ESTE CLIENTE NO ESTA MARCADO COMO INTERNO");
        cli_codiE.requestFocus();
        return false;
      }
    }
    else
    {
      if (!avc_seriE.getValor().equals("Y"))
      {
        mensajeErr("CLIENTE NO ESTA MARCADO COMO INTERNO. SE DEBE UTILIZAR LA SERIE Y");
        cli_codiE.requestFocus();
        return false;
      }
    }

    if (avc_fecalbE.getError() || avc_fecalbE.isNull())
    {
      mensajeErr("Fecha Albaran NO es valida");
      avc_fecalbE.requestFocus();
      return false;
    }
    if (! swEntdepos)
    {
        s=pdejerci.checkFecha(dtStat,avc_anoE.getValorInt(),emp_codiE.getValorInt(),avc_fecalbE.getText(),true);
        if (s!=null)
        {
          mensajeErr(s);
          avc_fecalbE.requestFocus();
          return false;
        }
    }
    if (nav.pulsado==navegador.ADDNEW && swAvisoAlbRep && cli_codiE.getLikeCliente().getInt("cli_gener")==0)
    {
      s = "SELECT * FROM v_albavec WHERE cli_codi = " + cli_codiE.getValorInt() +
          " and emp_codi = " + emp_codiE.getValorInt() +
          " and avc_ano = " + avc_anoE.getValorInt() +
          " and avc_nume != "+avc_numeE.getValorInt()+
          " and avc_fecalb = TO_DATE('" + avc_fecalbE.getText() + "','dd-MM-yyyy')";
      if (dtStat.select(s))
      {
        int res = mensajes.mensajePreguntar("YA existe un albaran (" +
                                            dtStat.getString("avc_nume") +
                                            ") para este cliente en esta fecha\n Continuar ?");
        if (res != mensajes.YES)
          return false;
        swAvisoAlbRep=false;
      }
    }
    if (! sbe_codiE.controla(true))
    {
      mensajeErr("SubEmpresa de Cliente NO valida");
      return false;
    }
    if (pvc_anoE.getValorInt() != 0)
    { // Albaran sobre un pedido
      if (getClientePedido() == 0)
      {
        mensajeErr("Pedido NO ENCONTRADO ...");
        pvc_anoE.requestFocus();
        return false;
      }

      if (dtStat.getString("pvc_confir").equals("N"))
      {
        mensajeErr("Pedido SIN confirmar .. Imposible continuar");
        pvc_anoE.requestFocus();
        return false;
      }
      if ( dtStat.getInt("avc_nume")!=0 && (dtStat.getInt("avc_nume") != avc_numeE.getValorInt() ||
          ! dtStat.getString("avc_serie").equals(avc_seriE.getText())  ||
          dtStat.getInt("avc_ano")!= avc_anoE.getValorInt())  )
      {
        mensajeErr("Pedido ya esta asignado a albaran: " + dtStat.getInt("avc_ano") +
                   "-" + dtStat.getString("avc_serie") + "/" + dtStat.getInt("avc_nume"));
        pvc_anoE.requestFocus();
        return false;
      }
      if (dtStat.getInt("cli_codi") != cli_codiE.getValorInt() && !P_ADMIN )
      {
        mensajeErr("PEDIDO es para Cliente: " + dtStat.getInt("cli_codi") +
                   " Imposible continuar");
        cli_codiE.requestFocus();
        return false;
      }
    }
    else
    {
        if (P_CONPEDIDO && !avc_deposE.getValor().equals("E") && avc_seriE.getText().equals("A"))
        {
            if (sbePanel.incPedidosAlb(dtStat, emp_codiE.getValorInt(),sbe_codiE.getValorInt()))
            {
              pvc_anoE.requestFocus();
              mensajeErr("Introduzca Pedido del Albaran");
              return false;
            }
        }
    }
   
    if (!P_MODPRECIO)
      return true;
    int res;
    if (swAvisoDto && cli_codiE.getLikeCliente().getInt("cli_dtopp") !=
        avc_dtoppE.getValorInt())
    {
      swAvisoDto=false;
      res = mensajes.mensajeYesNo("En la ficha del cliente el Dto PP es: " +
                                  cli_codiE.getLikeCliente().getInt("cli_dtopp") +
                                  " Sin embargo has introducido: "+
                                  avc_dtoppE.getValorInt()+" Es correcto ?");
      if (res != mensajes.YES)
        return false;
    }
    if (swAvisoDto && cli_codiE.getLikeCliente().getInt("cli_pdtoco") !=
        avc_dtocomE.getValorInt())
    {
      swAvisoDto=false;
      res = mensajes.mensajeYesNo("En la ficha del cliente el Dto Comercial es: " +
                                  cli_codiE.getLikeCliente().getInt("cli_pdtoco") +
                                  " Sin embargo has introducido: "+
                                  avc_dtocomE.getValorInt()+" Es correcto ?" );
      if (res != mensajes.YES)
        return false;
    }
    return true;
  }

    @Override
  public void ej_edit1()
  {
    try
    {      
      if (emp_codiE.hasCambio() || avc_anoE.hasCambio() || avc_numeE.hasCambio() || avc_seriE.hasCambio())
      {
          swProcesaEdit=true;
          return; // No permito editar si estos campos tienen cambio.
      }
      swProcesaEdit=false;
      if (!checkCli())
        return;

      jt.salirGrid();
      int nCol = cambiaLinAlb(jt.getSelectedRow());
      if (nCol >= 0)
      {
        jt.requestFocus(jt.getSelectedRow(), nCol);
        return;
      }
      if (jtRes.isEnabled())
      {
          jtRes.salirGrid();
          nCol=cambiaLinRes(jtRes.getSelectedRow());
          if (nCol >= 0)
          {
            jtRes.requestFocus(jtRes.getSelectedRow(), nCol);
            return;
          }
      }
      if (swEntdepos)
      {
        s="UPDATE albvenserc set avs_fecha = TO_DATE('"+avc_fecalbE.getText()+"','dd-MM-yyyy') "+
                " where avs_nume ="+avsNume;
        dtAdd.executeUpdate(s);
      }
      else
        actAlbaran();
      actProdRecicla();
      resetBloqueo(dtAdd);
      ctUp.commit();
      activaTodo();
      if (P_MODPRECIO &&  fvc_numeE.getValorInt() == 0  )
         avc_revpreE.setEnabled(true);
      if (opAgru.isSelected())
        verDatLin(avc_anoE.getValorInt(), emp_codiE.getValorInt(),
                  avc_seriE.getText(),
                  avc_numeE.getValorInt(), dtCons.getDouble("avc_impcob"),
                  true,
                  cli_codiE.getLikeCliente().getInt("cli_exeiva") == 0);
      if (avcImpres == 1)
      {
        if (jf != null)
        {
          jf.ht.clear();
          jf.ht.put("%a", emp_codiE.getValorInt()+"-"+avc_anoE.getValorInt()+
                  avc_seriE.getText()+"/"+avc_numeE.getText());
          jf.guardaMens("V7", jf.ht);
        }
      }

      verIconoListado(avcImpres | 2);
      actGridHist(emp_codiE.getValorInt(),avc_anoE.getValorInt(),
                  avc_seriE.getText(),avc_numeE.getValorInt());
      mensajeErr("Modificacion .. Realizada");
      mensaje("");
    }
    catch (Throwable k)
    {
      Error("Error al Modificar Albarán", k);
    }
    graba = true;
    if (P_MODPRECIO)
      jt.setEnabled(true);
    avl_prvenE.setCambio(true);
    nav.pulsado = navegador.NINGUNO;
  }

    @Override
  public void canc_edit()
  {
    swProcesaEdit=false;
    activaTodo();
    verDatos(dtCons);
    try
    {
      resetBloqueo(dtAdd);
    }
    catch (SQLException | ParseException k)
    {
      mensajes.mensajeAviso("Error al Quitar el bloqueo\n" + k.getMessage());
    }

    mensajeErr("Modificacion ... Cancelada");
    mensaje("");
    nav.pulsado = navegador.NINGUNO;
  }
@Override
  public void PADAddNew()
  {
//    swPasLin=false;
    swAvisoAlbRep=true;
    if (P_PONPRECIO || P_MODPRECIO)
     verPrecios=true;
    avl_prvenE.setEditable(verPrecios);
    activar(true);
    swAvisoDto=true;
    Pcabe.resetTexto();
    jt.setEnabled(false);
//    Bimpri.setEnabled(false);
    jt.removeAllDatos();
    Ppie.resetTexto();
    cli_pobleE.setText("");
    cli_codiE.resetCambio();
    cli_codiE.requestFocus();
    emp_codiE.setValorDec(EU.em_cod);
    avc_anoE.setValorDec(EU.ejercicio);
    avc_seriE.setText("A");
    avc_fecalbE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
    avc_fecemiE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
    usu_nombE.setText(EU.usuario);
    avc_numeE.setEnabled(false);
    fvc_serieE.setEnabled(false);
    fvc_anoE.setEnabled(false);
    fvc_numeE.setEnabled(false);
    usu_nombE.setEnabled(false);
    avc_fecemiE.setEnabled(false);
    tar_codiE.setEnabled(false);

//    avc_cerraE.setEnabled(false);
    avc_cerraE.setSelected(true);
    avc_anoE.setEnabled(false);
    Baceptar.setEnabled(false);
    Bimpri.setEnabled(false);
    jtDes.setEnabled(false);
    opAgru.setEnabled(false);
    cli_codiE.resetAlbaran();
    jtDes.removeAllDatos();
    pro_codiE.setEditable(true);
    pro_nombE.setEditable(true);
    avc_confoE.setSelected(true);
    avc_almoriE.setValor(""+ALMACEN);
    pvc_numeE.resetCambio();
    jtLinPed.removeAllDatos();
    Pcabped.resetCambio();
//    opModif.setSelected(false);
    avc_valoraE.setValor("0");
    nav.pulsado=navegador.ADDNEW;
    Bdespiece.setSelected(false);
    confAlbDep=false;

    avsNume=0;
    mensaje("Introduciendo Nuevo Albaran ...");
  }

  @Override
  public void ej_addnew1()
  {
    try
    {
      if (!checkCli())
        return;
      if (avc_numeE.getValorInt() == 0)
      {
        mensajeErr("Introduzca Alguna Linea de Albaran");
        jt.requestFocusInicio();
        return;
      }
      
      jt.salirGrid();
     
      int nCol = cambiaLinAlb(jt.getSelectedRow());
      if (nCol >= 0)
      {
        jt.requestFocus(jt.getSelectedRow(), nCol);
        return;
      }
      if (jtRes.isEnabled())
      {
          jtRes.salirGrid();
          nCol=cambiaLinRes(jtRes.getSelectedRow());
          if (nCol >= 0)
          {
            jtRes.requestFocus(jtRes.getSelectedRow(), nCol);
            return;
          }
      }
      if (! swEntdepos)
        actAlbaran();
      else
          ponAlbPedido();
      actProdRecicla();
      
      resetBloqueo(dtAdd);
      ctUp.commit();
    }
    catch (Throwable k)
    {
      Error("Error al Insertar el Albaran", k);
      return;
    }
    mensajeErr("Alta .. Realizada");
    inAddNew=true;
    lastEmpCodi=emp_codiE.getValorInt();
    lastAvcAno=avc_anoE.getValorInt();
    lastAvcNume=avc_numeE.getValorInt();
    lastAvcSerie=avc_seriE.getText();

    mensaje("");
    activaTodo();
    if (isEmpPlanta)
    {       
         strSql = getStrSql(" avc_nume = " + lastAvcNume +
                           " and emp_codi = " + lastEmpCodi +
                           " and avc_ano = " + lastAvcAno +
                           " and avc_serie = '" + lastAvcSerie + "'", null);
        try
        {
            rgSelect();
        } catch (SQLException ex)
        {
              Error("Error al buscar datos de Albaran dado de alta", ex);
              return;
        }
        verDatos(dtCons);       
        nav.pulsado = navegador.NINGUNO;
        return;
    }
    nav.ponEnabled(false);
    PADAddNew();
    graba = true;
  }
  void actNumPale(int row) throws SQLException
  {
      if (!avl_numpalE.hasCambio())
        return;
      avl_numpalE.resetCambio();
      s = "UPDATE  V_albavel set avl_numpal = " +avl_numpalE.getValorInt()+
          getCondWhereActAlb(row);
      dtAdd.executeUpdate(s);
      dtAdd.commit();
      mensajeErr("Modificado Numero Pale de Linea Albaran");
  }
  void actPrecioAlb(int row) throws SQLException
  {
      if (!avl_prvenE.hasCambio())
        return;
      if (avl_prvenE.getValorDec() == 0 && avl_prvenE.getValorDecAnt() != 0)
      {
        if (jf != null)
        { // Dejar mensaje avisando de q una linea q tenia precio ahora ya no lo tiene.
          jf.ht.clear();
          jf.ht.put("%a",
                    "act " + emp_codiE.getValorInt() + "-" +
                    avc_anoE.getValorInt() +
                    "/" + avc_seriE.getText() + avc_numeE.getValorInt());
          jf.ht.put("%p", jt.getValString(row, 1));
          jf.guardaMens("V6", jf.ht);
        }
      }
      actPrecioAlb(row,avl_prvenE.getValorDec());
      avl_prvenE.resetCambio();
      avl_numpalE.resetCambio();
  }
  /**
   * Devuelve condiciones (where) para la sentencia sql para modificar el
   * precio en un albaran
   * @param row Linea de grid
   * @return 
   */
  String getCondWhereActAlb(int row)
  {
      String cw= " WHERE avc_ano =" + avc_anoE.getValorInt() +
          " and emp_codi = " + emp_codiE.getValorInt() +
          " and avc_serie = '" + avc_seriE.getText() + "'" +
          " and avc_nume = " + avc_numeE.getValorInt() +
          (jt.getValorDec(row, JT_KILOS) > 0 ? " and avl_canti > 0" :
           " and avl_canti < 0");
      if (jt.getValorInt(row, JT_NULIAL) >= 0)
        cw += " AND avl_numlin = " + jt.getValorInt(row, JT_NULIAL);
      else
      {
        cw += " AND pro_codi = " + jt.getValorInt(row, JT_PROCODI) +
            " and avl_prven = " + antPrecio+
            " and avl_numpal = "+jt.getValorInt(row,JT_NUMPALE);
      }
      return cw;
   }
  /**
   * Actualiza precio de albaran en una linea
   * @param row
   */
  void actPrecioAlb(int row,double precio) throws SQLException
  {
      int nRows;
  
      double prTari=jt.getValorDec(row,FD_PRTARI);
    
//      jt.setValor(""+avl_prvenE.getValorDec(),5);
      if (isBloqueado(dtAdd, "v_albavec",
                      avc_anoE.getValorInt() + "|" + emp_codiE.getValorInt() +
                      "|" + avc_seriE.getText() + "|" + avc_numeE.getValorInt(),true,false))
      {
        msgBox(msgBloqueo);
        verDatos(dtCons);
        return;
      }
      String condWhere = getCondWhereActAlb(row);
    
      s = "select avl_prven from  v_albavel " + condWhere;
//      debug(s);
      if (! dtStat.select(s))
      {
        msgBox("ATENCION: No encontradas LINEAS a modificar");
        verDatos(dtCons);
//        avl_prvenE.setValorDec(jt.getValorDec(row,5)); // Restaurado valor anterior
        return;
      }
      
      double avlPrven=Formatear.redondea(precio,NUMDECPRECIO);
      double avlPrbase = avlPrven - (avlPrven * ((avc_dtocomE.getValorDec()+avc_dtoppE.getValorDec())/100)) ;

      avlPrbase=Formatear.redondea(avlPrbase,NUMDECPRECIO);

      if (fvc_numeE.getValorInt() > 0)
      { // Este Albaran ya estaba facturado
        boolean actLinFra=true;
        s= "SELECT * FROM  v_facvec WHERE fvc_ano = " + fvc_anoE.getValorInt() +
                " and emp_codi = " + emp_codiE.getValorInt() +
                " and fvc_serie = '"+fvc_serieE.getText()+"'"+
                " and fvc_nume = " + fvc_numeE.getValorInt();
        if (!dtAdd.select(s))
          actLinFra=false;
        else
        {
          if (dtAdd.getString("fvc_modif").equals("M"))
            actLinFra = false;
        }
        s = "select *  from  v_albavel " + condWhere +
            " AND fvl_numlin > 0";
        if (actLinFra && dtAdd.select(s))
        {
          do
          {
            s = "UPDATE v_facvel SET FVL_PRVEN = " +avlPrven+
                " WHERE eje_nume = " + fvc_anoE.getValorInt() +
                " and emp_codi = " + emp_codiE.getValorInt() +
                " and fvc_nume = " + fvc_numeE.getValorInt() +
                " and fvc_serie = '"+fvc_serieE.getText()+"'"+
                " and fvl_numlin = " + dtAdd.getInt("fvl_numlin");
            nRows=stUp.executeUpdate(s);
            if (nRows!=1)
              aviso("pdalbara: (Mod Precio)\n" +s+"\n "+" No Columnas Act.: "+nRows);
          }  while (dtAdd.next());
        }
      }

      if (swCompra && opAgru.isSelected())
      { // Modificar precio de Alb. de Compra.
        if ( MantAlbComCarne.getLineaAlb(dtAdd,emp_codiE.getValorInt(),
                                  accAno,"Y",accNume,row+1,true))
        {
          dtAdd.edit();
          dtAdd.setDato("acl_prcom",avlPrven);
          dtAdd.update(stUp);
        }
      }

      s = "UPDATE  V_albavel set avl_prven = " +avlPrven +
          ", avl_prbase = "+avlPrbase+
          ", tar_preci =  "+prTari+
          condWhere;
//      debug("actPrecioAlb - s: "+s);
      nRows=stUp.executeUpdate(dtAdd.getStrSelect(s));
      if (nRows==0)
       aviso("pdalbara: (Mod Precio)\n" +s+"\n "+" No Columnas Act.: "+nRows);
      actCabecAlb(emp_codiE.getValorInt(),avc_anoE.getValorInt(),avc_seriE.getText(),
                  avc_numeE.getValorInt(),true);
      ctUp.commit();
    
  }
  /**
     * Actualizar datos Albarán. LLamado  una vez aceptada la alta o la modificación
     *
     * @throws Throwable
     */
  void actAlbaran() throws Throwable
  {
    if (nav.pulsado==navegador.QUERY)
      return;
    int nRow = jt.getRowCount();
    // Actualizo las Linea de Albaran.
    for (int n = 0; n < nRow; n++)
    {
      if (jt.getValorInt(n, 0) == 0)
        continue;

      s = "SELECT * FROM V_albavel WHERE avc_ano =" + avc_anoE.getValorInt() +
          " and emp_codi = " + emp_codiE.getValorInt() +
          " and avc_serie = '" + avc_seriE.getText() + "'" +
          " and avc_nume = " + avc_numeE.getValorInt() +
          " AND avl_numlin = " + jt.getValorInt(n, 0);
      if (!dtAdd.select(s, true))
        throw new Exception("No encontrado Linea Albaran: " + jt.getValorInt(n, 0) +
                            "\n Select: " + s);
      dtAdd.edit(dtAdd.getCondWhere());
      dtAdd.setDato("pro_codi", jt.getValorInt(n, 1));
      dtAdd.setDato("pro_nomb", jt.getValString(n, 2));
      dtAdd.setDato("avl_canti", jt.getValorDec(n, 3));
      dtAdd.setDato("avc_cerra", avc_cerraE.isSelected()?-1:0);
      double precio = dtAdd.getDouble("avl_prven");
      double prTari = dtAdd.getDouble("tar_preci");
      if  (verPrecios)
      {
        precio = jt.getValorDec(n, 5);
        prTari = jt.getValorDec(n, 6);
      }
      if (precio == 0 && ! verPrecios) // Si el precio es 0 Y esta en modo NO mod. precio
      { // Intento poner el precio de Tarifa en Modo Automatico
        prTari = MantTarifa.getPrecTar(dtStat,jt.getValorInt(n, 1), tar_codiE.getValorInt(), avc_fecalbE.getText());
        if (avc_revpreE.getValorInt()==0)
         precio=prTari;
      }
      if (avl_prvenE.getValorDec() == 0 && dtAdd.getDouble("avl_prven") != 0)
      {
        if (jf != null)
        { // Mensaje Avisando de que se le ha puesto como precio = 0 a una linea albaran que ya tenia precio
          jf.ht.clear();
          jf.ht.put("%a",emp_codiE.getValorInt() + "-" + avc_anoE.getValorInt() +
                    "/" +
                    avc_seriE.getText() + avc_numeE.getValorInt());
          jf.ht.put("%p", jt.getValString(n, 1) + " Lin: " + jt.getValorInt(n, 0));
          jf.guardaMens("V6", jf.ht);
        }
      }
      precio=Formatear.redondea(precio,NUMDECPRECIO);
      double avlPrbase = precio - (precio * ((avc_dtocomE.getValorDec() + avc_dtoppE.getValorDec())/100)) ;
//      if (avc_dtocomE.getValorDec()!=0)
//          avlPrbase-=precio*(avc_dtocomE.getValorDec()/100);
//      if (avc_dtoppE.getValorDec()!=0)
//          avlPrbase-=precio*(avc_dtoppE.getValorDec()/100);
      if ( verPrecios)
      {
          avlPrbase=Formatear.redondea(avlPrbase,NUMDECPRECIO);
          dtAdd.setDato("avl_prven", precio);
          dtAdd.setDato("avl_prbase", avlPrbase);
          dtAdd.setDato("tar_preci", prTari);
      }
      dtAdd.setDato("avl_unid", jt.getValorInt(n, 4));
      dtAdd.setDato("avl_canbru", jt.getValorDec(n, 3));
      dtAdd.setDato("avl_numpal", jt.getValorDec(n, JT_NUMPALE));
      pro_codiE.getNombArt(jt.getValString(n, 1));
      dtAdd.update(stUp);
    }
    actCabecAlb(emp_codiE.getValorInt(),avc_anoE.getValorInt(),avc_seriE.getText(),
                avc_numeE.getValorInt(),false);

    numLinE.setValorDec(datCab.getValInt("nLin"));
    kilosE.setValorDec(datCab.getValDouble("kilos"));
    if (cli_codiE.getLikeCliente().getInt("cli_gener")==0)
      dtAdd.setDato("avc_clinom",(String) null);
    else
      dtAdd.setDato("avc_clinom",cli_codiE.getTextNomb());
    dtAdd.setDato("div_codi",div_codiE.getValorInt());
    dtAdd.setDato("div_codi",div_codiE.getValorInt());
    dtAdd.setDato("cli_codi", cli_codiE.getValorInt());
    dtAdd.setDato("avc_fecalb", avc_fecalbE.getText(), "dd-MM-yyyy");
    dtAdd.setDato("avc_tipfac",
                  cli_codiE.getLikeCliente().getString("cli_tipfac")); // Tipo Facturacion
    dtAdd.setDato("cli_ruta", rut_codiE.getValorInt());
    dtAdd.setDato("cli_codfa",
                  cli_codiE.getLikeCliente().getInt("cli_codfa"));
    dtAdd.setDato("avc_impalb", datCab.getValDouble("avc_impalb"));
    dtAdd.setDato("avc_basimp", datCab.getValDouble("avc_basimp"));
    dtAdd.setDato("avc_kilos", datCab.getValDouble("kilos"));

    dtAdd.setDato("avc_dtopp", avc_dtoppE.getValorDec());
    dtAdd.setDato("avc_dtocom", avc_dtocomE.getValorDec());
    dtAdd.setDato("avc_cerra", avc_cerraE.isSelected()?-1:0);
    dtAdd.setDato("avc_confo", avc_confoE.isSelected()?-1:0);
    dtAdd.setDato("avc_valora", P_MODPRECIO?avc_valoraE.getValor():"0"); // Se utiliza para saber si los precios son Aut. o Manuales
    dtAdd.setDato("avc_impres",(dtAdd.getInt("avc_impres") | 2));
    dtAdd.setDato("sbe_codi",sbe_codiE.getValorInt());
    dtAdd.setDato("avc_revpre",avc_revpreE.getValor());
    dtAdd.update(stUp);
    // Actualizo tabla de clientes
    dtAdd.executeUpdate("update clientes set "+
            " cli_feulve = to_date('"+avc_fecalbE.getFecha("dd-MM-yyyy")+"','dd-MM-yyyy')  "+
            " where cli_codi = "+cli_codiE.getValorInt());
    if (getClientePedido(dtAdd,true)>0)
    {
      dtAdd.edit();
      dtAdd.setDato("pvc_cerra", avc_cerraE.isSelected()?-1:0);
      dtAdd.update();
    }

    if (fvc_numeE.getValorInt() > 0)
      actFactura();

    if (checkAlbaran(emp_codiE.getValorInt(),avc_anoE.getValorInt(), avc_seriE.getText(),avc_numeE.getValorInt(), dtStat)<0)
    {
        actAcumLinAlb(emp_codiE.getValorInt(),avc_anoE.getValorInt(), avc_seriE.getText(),avc_numeE.getValorInt());
    }
  }
  
  /**
   * Actualizar tabla productos de reciclaje.
   */
  void actProdRecicla() throws SQLException
  {
      if (! jtRes.isEnabled())
          return;
      HashMap<Integer,albvenres> dat=new HashMap();
      int maxNL=0;
      s="SELECT * FROM  albvenres where avc_ano =" + avc_anoE.getValorInt() +
        " and emp_codi = " + emp_codiE.getValorInt() +
        " and avc_serie = '" + avc_seriE.getText()+ "'" +
        " and avc_nume = " + avc_numeE.getValorInt();
      if (dtCon1.select(s))
      {
          do
          {
              dat.put(dtCon1.getInt("avr_numlin"),
                  new albvenres(dtCon1.getInt("pro_codi"),dtCon1.getDouble("avr_canti")));
              if (maxNL<dtCon1.getInt("avr_numlin"))
                  maxNL=dtCon1.getInt("avr_numlin");
          } while (dtCon1.next());
      }
      maxNL++;
      int nRow=jtRes.getRowCount();
      for (int n=0;n<nRow;n++)
      {
          if (jtRes.getValorInt(n,0)==0)
              continue;
          albvenres al=dat.get(jtRes.getValorInt(n,JTRES_NL));
          if (al==null)
          { // No existia la linea. Inserto una nueva.
            dtAdd.addNew("albvenres");
            dtAdd.setDato("avc_ano", avc_anoE.getValorInt());
            dtAdd.setDato("emp_codi", emp_codiE.getValorInt());
            dtAdd.setDato("avc_serie", avc_seriE.getText());
            dtAdd.setDato("avc_nume", avc_numeE.getValorInt());
            dtAdd.setDato("avr_numlin", maxNL++);
            dtAdd.setDato("pro_codi", jtRes.getValorInt(n, 0));
            dtAdd.setDato("avr_canti",jtRes.getValorDec(n,2));
            dtAdd.setDato("avr_fecalt","current_timestamp");
            dtAdd.update();
          }
          else
          {
              dat.remove(jtRes.getValorInt(n,JTRES_NL));
              if (al.proCodi!=jtRes.getValorInt(n, 0) || al.avrCanti!=jtRes.getValorDec(n,2))
              {
                  
                  if (!dtAdd.select("select * from albvenres where avc_ano =" + avc_anoE.getValorInt() +
                    " and emp_codi = " + emp_codiE.getValorInt() +
                    " and avc_serie = '" + avc_seriE.getText()+ "'" +
                    " and avc_nume = " + avc_numeE.getValorInt()+
                    " and avr_numlin = "+jtRes.getValorInt(n,JTRES_NL),true))
                  {
                      enviaMailError("Error al buscar Linea Productos recicables\n"+s);
                      continue;
                  }
                  dtAdd.edit();
                  dtAdd.setDato("pro_codi", jtRes.getValorInt(n, 0));
                  dtAdd.setDato("avr_canti",jtRes.getValorDec(n,2));
                  dtAdd.setDato("avr_canti",jtRes.getValorDec(n,2));
                  dtAdd.setDato("avr_fecalt","current_timestamp");
                  dtAdd.update();
              }
          }
      }
      Iterator it=dat.entrySet().iterator();
      while (it.hasNext())
      {
          Map.Entry<Integer,albvenres> e = (Map.Entry) it.next();
          albvenres al=e.getValue();
          dtAdd.executeUpdate("delete from  albvenres where avc_ano =" + avc_anoE.getValorInt() +
                    " and emp_codi = " + emp_codiE.getValorInt() +
                    " and avc_serie = '" + avc_seriE.getText()+ "'" +
                    " and avc_nume = " + avc_numeE.getValorInt()+
                    " and avr_numlin = "+e.getKey());
      }
      s="SELECT *  from  albvenres where avc_ano =" + avc_anoE.getValorInt() +
                    " and emp_codi = " + emp_codiE.getValorInt() +
                    " and avc_serie = '" + avc_seriE.getText()+ "'" +
                    " and avc_nume = " + avc_numeE.getValorInt()+
                    " order by avr_numlin";
      // Reorganizo los  numeros de Linea
      if (!dtAdd.select(s,true))
          return;
      nRow=0;
      do
      {
          nRow++;
          if (nRow==dtAdd.getInt("avr_numlin"))
              continue;
          dtBloq.executeUpdate("update albvenres set avr_numlin="+nRow+
              "  where avc_ano =" + avc_anoE.getValorInt() +
                    " and emp_codi = " + emp_codiE.getValorInt() +
                    " and avc_serie = '" + avc_seriE.getText()+ "'" +
                    " and avc_nume = " + avc_numeE.getValorInt()+
              " and avr_numlin ="+dtAdd.getInt("avr_numlin"));
      } while (dtAdd.next());
      
  }

  /**
   * Actualiza Todas las lineas de la factura y los totales de la factura
   * @throws Throwable En caso de error  en BD
   */
  void actFactura() throws Throwable
  {
    s = "SELECT * FROM v_facvec WHERE fvc_ano = " + fvc_anoE.getValorInt() +
        " and emp_codi = " + emp_codiE.getValorInt() +
        " and fvc_serie = '"+fvc_serieE.getText()+"'"+
        " and fvc_nume = " + fvc_numeE.getValorInt();
    if (!dtAdd.select(s))
      return; // No encontrada cabecera de FRA.
    if (dtAdd.getString("fvc_modif").equals("M"))
      return; // Factura esta marcada como YA modificada en el Mant. Fras.

    s = "SELECT * FROM V_albavel WHERE avc_ano =" + avc_anoE.getValorInt() +
        " and emp_codi = " + emp_codiE.getValorInt() +
        " and avc_serie = '" + avc_seriE.getText() + "'" +
        " and avc_nume = " + avc_numeE.getValorInt() +
        " and fvl_numlin > 0 ";
    if (dtAdd.select(s))
    {
      do
      {
        s = "UPDATE v_facvel SET pro_codi = " + dtAdd.getInt("pro_codi") +
            ", fvl_canti = " + dtAdd.getDouble("avl_canti") +
            ", fvl_prven = " + dtAdd.getDouble("avl_prven") +
            " WHERE eje_nume = " + fvc_anoE.getValorInt() +
            " and emp_codi = " + emp_codiE.getValorInt() +
            " and fvc_nume = " + fvc_numeE.getValorInt() +
            " and fvc_serie = '"+fvc_serieE.getText()+"'"+
            " and fvl_numlin = " + dtAdd.getInt("fvl_numlin");
        stUp.executeUpdate(s);
      } while (dtAdd.next());
    }
    actImpFra();
  }

  public void canc_addnew()
  {
    try
    {
      if (avc_numeE.getValorInt() > 0)
      { // Borrar Cabecera
        if (mensajes.mensajeYesNo("VOLVER AL ALBARAN ? ", this) == mensajes.YES)
          return;
        if (jf != null)
        {
          jf.ht.clear();
          jf.ht.put("%a", avc_numeE.getText());
          jf.guardaMens("V4", jf.ht);
        }
        borraAlbaran(true);
        ctUp.commit();
        try
        {
          resetBloqueo(dtAdd);
        }
        catch (SQLException | ParseException k)
        {
          mensajes.mensajeAviso("Error al Quitar el bloqueo\n" + k.getMessage());
        }
      }
      if (inAddNew)
      {
        strSql = getStrSql(" avc_nume = " + lastAvcNume +
                           " and emp_codi = " + lastEmpCodi +
                           " and avc_ano = " + lastAvcAno +
                           " and avc_serie = '" + lastAvcSerie + "'", null);
        rgSelect();
      }

    }
    catch (Exception k)
    {
      Error("Error al Cancelar ALTA de Albaran", k);
      return;
    }

    inAddNew=false;
    activaTodo();
    verDatos(dtCons);
    mensajeErr("ALTA ... Cancelada");
    mensaje("");
    
    nav.pulsado = navegador.NINGUNO;
  }

  void borraAlbaran(boolean swDestruir) throws Exception
  {
     if (swEntdepos)
     {
        s = "DELETE FROM albvenseri where avs_nume="+avsNume;
        dtAdd.executeUpdate(s,stUp);
        s = "DELETE FROM albvenserl where avs_nume="+avsNume;
        dtAdd.executeUpdate(s,stUp);
        s = "DELETE FROM albvenserc where avs_nume="+avsNume;
        dtAdd.executeUpdate(s,stUp);
        return;
      }
    if (pvc_anoE.getValorInt()!=0)
    {
      if (getClientePedido(dtAdd, true) != 0)
      {
        dtAdd.edit();
        dtAdd.setDato("avc_nume", 0);
        dtAdd.setDato("avc_ano", 0);
        dtAdd.setDato("avc_serie", "A");
        dtAdd.update(stUp);
      }
    }
    int nRow = jt.getRowCount();
// Actualizo las Linea de Albaran.
    for (int n = 0; n < nRow; n++)
    {
      if (jt.getValorInt(n, 0) == 0)
        continue;
      borraLinea(n);
    }
    if (swDestruir)
    {
        s = "DELETE FROM v_albavec WHERE "+getCondCurrent();
         if (dtAdd.executeUpdate(s,stUp) != 1)
          throw new Exception("No encontrado Cabecera Albaran.\n Select: " + s);   
         // Borro Historicos
        dtAdd.executeUpdate( "DELETE FROM hisalpave WHERE "+getCondCurrent(),stUp);
        dtAdd.executeUpdate( "DELETE FROM hisallive WHERE "+getCondCurrent(),stUp);
        dtAdd.executeUpdate( "DELETE FROM hisalcave WHERE "+getCondCurrent(),stUp);        
    }
  }
  
  private String getCondCurrent()
  {
      return " avc_ano =" + avc_anoE.getValorInt() +
        " and emp_codi = " + emp_codiE.getValorInt() +
        " and avc_serie = '" + avc_seriE.getText() + "'" +
        " and avc_nume = " + avc_numeE.getValorInt();
  }
  /**
   * Borra un Albaran. Si se pulso SHIFT al mismo tiempo que el boton borrar, da la opcion de DESTRUIR el albaran.
   */
  @Override
  public void PADDelete()
  {
      try {
        if (hisRowid!=0)
        {
          msgBox("Viendo albaran historico ... IMPOSIBLE BORRAR");
          activaTodo();
          return;
        }
          if (!canDelete()) {
              nav.pulsado = navegador.NINGUNO;
              activaTodo();
              return;
          }
//      aviso("Entrando en Delete: con Albaran: "+avc_anoE.getValorInt() + "|" + emp_codiE.getValorInt() +
//                      "|" + avc_seriE.getText() + "|" + avc_numeE.getValorInt());
          nav.pulsado = navegador.DELETE;
          if (pdejerci.isCerrado(dtStat, avc_anoE.getValorInt(), emp_codiE.getValorInt()) && !swEntdepos) {
              if (!P_ADMIN) {
                  msgBox("Albaran es de un ejercicio YA cerrado ... IMPOSIBLE BORRAR");
                  nav.pulsado = navegador.NINGUNO;
                  activaTodo();
                  return;
              } else {
                  msgBox("ATENCION!!! Albaran es de un ejercicio YA cerrado");
              }
          }

          if ((fvc_anoE.getValorDec() > 0 || fvc_numeE.getValorDec() > 0) && !swEntdepos) {
              msgBox("Albaran YA se HA FACTURADO ... IMPOSIBLE BORRAR");
              nav.pulsado = navegador.NINGUNO;
              activaTodo();
              return;
          }



          if (traspReci && !swEntdepos) {
              msgBox("Factura ha emitido algun recibo bancario .. IMPOSIBLE BORRAR");
              nav.pulsado = navegador.NINGUNO;
              activaTodo();
              return;
          }

          if (traspCont && !swEntdepos) {
              msgBox("Factura ya esta traspasada a contabilidad .. IMPOSIBLE BORRAR");
              nav.pulsado = navegador.NINGUNO;
              activaTodo();
              return;
          }
          if (avcImpres == 1) {
              int res = mensajes.mensajeYesNo("Albaran YA se ha listado\n ¿ Cancelar Borrado ?");
              if (res != mensajes.NO) {
                  nav.pulsado = navegador.NINGUNO;
                  activaTodo();
                  return;
              }
          }

          //    s = "SELECT * FROM V_albavec WHERE avc_ano =" + avc_anoE.getValorInt() +
          //        " and emp_codi = " + emp_codiE.getValorInt() +
          //        " and avc_serie = '" + avc_seriE.getText() + "'" +
          //        " and avc_nume = " + avc_numeE.getValorInt();
          if (!selCabAlb(dtAdd, avc_anoE.getValorInt(), emp_codiE.getValorInt(), avc_seriE.getText(),
                  avc_numeE.getValorInt(), true, false)) {
              msgBox("Albaran NO encontrado .. PROBABLEMENTE se ha borrado");
              nav.pulsado = navegador.NINGUNO;
              activaTodo();
              return;
          }
          if (checkEdicionAlbaran()) 
              return;
          
          if (!setBloqueo(dtStat, "v_albavec",
                  avc_anoE.getValorInt() + "|" + emp_codiE.getValorInt()
                  + "|" + avc_seriE.getText() + "|" + avc_numeE.getValorInt(), true)) {
              msgBox(msgBloqueo);
              activaTodo();
              return;
          }
          
          Bimpri.setEnabled(false);
          BValTar.setEnabled(false);
          Baceptar.setEnabled(true);
          Bcancelar.setEnabled(true);
          opAgru.setEnabled(false);
          if (opAgru.isSelected()) {
              if (swEntdepos) {
                  verDatLin(dtCons, false, true);
              } else {
                  verDatos(dtCons, false);
              }
          }
          avc_revpreE.setEnabled(false);
      } catch (Exception k) {
          Error("Error al Borrar Albarán", k);
          return;
      }
      swPreguntaDestruir=(nav.getModifiers() & ActionEvent.SHIFT_MASK) !=0;
      
      nav.setEnabled(false);
      mensaje("Borrar Albaran? .... ");
      Bcancelar.requestFocus();
  }

  @Override
  public void ej_delete1()
  {
    try
    {
      if (opValora.isSelected())
      {
        if (! verPrecios)
        {
          msgBox("Albaran esta valorado ... IMPOSIBLE BORRAR");
          return;
        }
        String valor = "NO";
        valor = mensajes.mensajeGetTexto(
            "Para borrar el ALBARAN teclee la palabra 'BORRAR'",
            "Confirme BORRADO", this, valor);
        if (valor == null)
          valor = "";
        if (!valor.toUpperCase().equals("BORRAR"))
          return;
      }
      else
      {
        if (mensajes.mensajeYesNo("ANULAR BORRADO ? ", this) == mensajes.YES)
          return;
      }
      boolean swDestruir=false;
      if (swPreguntaDestruir)
      {
          swDestruir=mensajes.mensajeYesNo("DESTRUIR TODO RASTRO DE ESTE ALBARAN ? ", this) == mensajes.YES;
      }
      else
      {
        copiaAlbaranNuevo(dtCon1,dtAdd,"Borrado Albaran",EU.usuario,avc_anoE.getValorInt(),
              emp_codiE.getValorInt(),avc_seriE.getText(),avc_numeE.getValorInt());
      }
        
      if (jf != null )
      {
        jf.ht.clear();
        jf.ht.put("%a", avc_numeE.getText());
        jf.ht.put("%u", EU.usuario);
        jf.guardaMens("V2", jf.ht);
      }

      borraAlbaran(swDestruir);
      ctUp.commit();
      try
      {
        resetBloqueo(dtAdd);
      }
      catch (SQLException | ParseException k)
      {
        mensajes.mensajeAviso("Error al Quitar el bloqueo\n" + k.getMessage());
      }

      activaTodo();
      if (dtCons.next())
        verDatos(dtCons);
      else
      {
        if (dtCons.previous())
          verDatos(dtCons);
        else
        {
          Pcabe.resetTexto();
          jtDes.removeAllDatos();
          jt.removeAllDatos();
        }
      }
      mensajeErr("Borrado Albaran .. Realizado");
      mensaje("");
    }
    catch (Exception k)
    {
      Error("Error al Borrar Albaran", k);
      return;
    }
    nav.pulsado = navegador.NINGUNO;

  }

  @Override
  public void canc_delete()
  {
    activaTodo();
    verDatos(dtCons);
    try
    {
      resetBloqueo(dtAdd);
    }
    catch (SQLException | ParseException k)
    {
      mensajes.mensajeAviso("Error al Quitar el bloqueo\n" + k.getMessage());
    }

    mensajeErr("Borrado Albaran ... Cancelado");
    mensaje("");
    nav.pulsado = navegador.NINGUNO;

  }

  void irGrid()
  {
    if (!jt.isEnabled())
      irGridLin();
    else
      irGridDes();
  }
  int getClientePedido() throws SQLException,ParseException
  {
    return getClientePedido(dtStat,false);
  }
  int getClientePedido(DatosTabla dt,boolean block) throws SQLException,ParseException
  {
    s = "SELECT * FROM pedvenc WHERE emp_codi = " + emp_codiE.getValorInt() +
         " and eje_nume = " + pvc_anoE.getValorInt() +
         " and pvc_nume = " + pvc_numeE.getValorInt()+
         " and pvc_confir = 'S' ";
     if (!dt.select(s,block))
       return 0;
     return dt.getInt("cli_codi");
  }

  private void actPedAlbaran() throws SQLException,ParseException
  {
    if (nav.pulsado != navegador.EDIT)
      return;
    s = "SELECT * FROM pedvenc WHERE avc_ano =" + avc_anoE.getValorInt() +
      " and emp_codi = " + emp_codiE.getValorInt() +
      " and avc_serie = '" + avc_seriE.getText() + "'" +
      " and avc_nume = " + avc_numeE.getValorInt();
    if (dtAdd.select(s, true))
    {
      if (dtAdd.getInt("pvc_nume") != pvc_numeE.getValorInt() ||
          dtAdd.getInt("eje_nume") != pvc_anoE.getValorInt())
      {
        // Quito el albaran del antiguo pedido
        dtAdd.edit();
        dtAdd.setDato("avc_nume", 0);
        dtAdd.setDato("avc_ano", 0);
        dtAdd.setDato("avc_serie", "A");
        dtAdd.update(stUp);
      }
    }
    ponAlbPedido();
    ctUp.commit();
  }
  /**
   * Ir a Lineas de Albaran
   */
  void irGridLin()
  {
    try
    {
      if (!jtDes.isEnabled())
      { // Se supone que entro desde padaddnew o padedit
        if (! checkCli())
          return;
//        swPasLin=true;
        if (nav.pulsado!=navegador.EDIT)
        {
          pvc_anoE.setEnabled(false);
          pvc_numeE.setEnabled(false);
          BbusPed.setEnabled(false);
        }
        avp_cantiE.setEditable( P_ADMIN);
        if (swEntdepos)
            avp_cantiE.setEditable(false);
        if (avc_deposE.getValor().equals("N"))
        { // Es un albaran normal. Compruebo anteriores a ver si habia de deposito.
            if (! confAlbDep)
            {
                s="select  max(avc_fecalb) as avc_fecalb from v_albavec where avc_fecalb>=current_date - 120 "+
                    " and cli_Codi = "+cli_codiE.getValorInt()+" and avc_depos= 'D' ";
                dtStat.select(s);
                if (dtStat.getObject("avc_fecalb")!=null)
                {
                    confAlbDep=true;
                    int ret=mensajes.mensajeYesNo("Este cliente tiene un albarane de deposito en fecha: "+
                        dtStat.getFecha("avc_fecalb")+" ¿Desea poner el albaran como de deposito ?",this);
                    if (ret==mensajes.YES)
                    {
                        avc_deposE.setValor("D");
                        avc_deposE.requestFocus();
                        return;
                    }
                }
            }
        }
        if (pvc_anoE.getValorInt()!=0)
          verDatPedido();
        if (nav.pulsado == navegador.EDIT)
          Bcancelar.setEnabled(false);
        if (nav.pulsado==navegador.ADDNEW)
           swEntdepos=avc_deposE.getValor().equals("E");
        //sbe_codiE.setEnabled(false);
        Baceptar.setEnabled(true);
        Bimpri.setEnabled(true);
        jt.setEnabled(true);
        jt.requestFocusInicio();
        Bdespiece.setEnabled(true);
        pro_nombE.setText(jt.getValString(0,2)) ;
        pro_codiE.resetCambio();
        pro_codiE.setSeccionCliente(sbe_codiE.getValorInt());
        if (pro_codiE.getTipoLote()=='V' && jt.getValorInt(1) >= 1)
        {
          avl_unidE.setEnabled(false);
          avl_cantiE.setEditable(false);
        }
        else
        {
          avl_unidE.setEnabled(true);
          avl_cantiE.setEditable(true);
        }
      }
      else
      { // Acabo de meter lineas de  desglose...
        pro_numindE_focusLost();
        jtDes.salirGrid();
        if (avp_numindE.getValorInt() != 0 || avp_numparE.getValorInt() != 0 ||
            avp_cantiE.getValorDec() != 0)
        {
          final int colErr = cambiaLinDes(jtDes.getSelectedRow());
          if (colErr >= 0)
          {
            jtDes.requestFocusLater(jtDes.getSelectedRow(), colErr);
            return;
          }
        }
        guardaLinDes(jt.getSelectedRow());
        if (jt.getValorDec(3)==0 && jt.getValorInt(0)>0)
        { // Esta linea de albaran se habia guardado pero ahora no tiene KILOS
          mensajeErr("La suma de Kilos de los individuos  NO puede ser 0");
          jtDes.requestFocusLater(0, 0);
          return;
        }
        if (jt.getSelectedColumn() == 3 &&  ! verPrecios)
            swGridDes++;
        jtDes.setEnabled(false);
        jt.setEnabled(true);

        Baceptar.setEnabled(true);
        Bimpri.setEnabled(true);
        if (pro_codiE.getTipoLote()=='V' && jt.getValorInt(0) > 0 )
        {
          pro_codiE.setEditable(false);
//          pro_nombE.setEditable(false);
        }
        else
        {
          pro_codiE.setEditable(true);
          pro_nombE.setEditable(true);
        }
        SwingUtilities.invokeLater(new Thread()
        {
          @Override
          public void run()
          {
            Bdespiece.setEnabled(true);
            if (verPrecios  && jt.getSelectedColumn() == 3)
              jt.requestFocus(jt.getSelectedRow(),5);
            else
              jt.requestFocusSelected();
          }
        });
        actAcumLin();
      }
    }
    catch (Exception k)
    {
      Error("Error al ir a Lineas Albaran", k);
    }
  }

  /**
   * Actualiza Grid de Desglose cuando se esta modificando una linea de albaran
   * que ya existia.
   * @throws Exception caso de error
   */
  void actGridDes() throws Exception
  { 
    // Meto todas las lineas anteriores en un vector.
    ArrayList<DatIndiv> datAnt = new ArrayList();
    if (swEntdepos)
    {
        s = "SELECT  "+jt.getValorInt(1)
                + " as pro_codi, avs_ejelot as avp_ejelot,avs_numpar as avp_numpar,"
                + " avs_numind as avp_numind,avs_serlot as avp_serlot,"
                + " avs_canti  as avp_canti,avi_numlin as avp_numlin,avs_numuni as avp_numuni "
                + " FROM albvenseri "
                + " WHERE avs_nume = " + avsNume
                + " and avs_numlin = " + jt.getValorInt(0);
    }
    else
        s = "SELECT * FROM v_albvenpar " +
            " WHERE "+getCondCurrent()+
            " and avl_numlin = " + jt.getValorInt(JT_NULIAL);
    if (dtCon1.select(s))
    {
      do
      {
        DatIndiv di = new DatIndiv();
        di.setAuxiliar("N"); // 0
        di.setProducto(dtCon1.getInt("pro_codi")); 
        di.setEjercLot(dtCon1.getInt("avp_ejelot")); // 2
        di.setLote(dtCon1.getInt("avp_numpar")); // 3
        di.setNumind(dtCon1.getInt("avp_numind")); // 4
        di.setSerie(dtCon1.getString("avp_serlot")); // 5
        di.setCanti(dtCon1.getDouble("avp_canti")); // 6
        di.setNumLinea(dtCon1.getInt("avp_numlin")); // 7
        di.setNumuni(dtCon1.getInt("avp_numuni")); // 8
        datAnt.add(di);
      }  while (dtCon1.next());
    }

    int nRowIndAnt = datAnt.size();
    int nRow = jtDes.getRowCount();
    int l;
    DatIndiv v;
    // Busco en datos anteriores para ver si YA no existen
    for (int n = 0; n < nRowIndAnt; n++)
    {
      v =  datAnt.get(n);
      l = buscaIndiv(v,nRow);
      if (l == -1)
      { // Una linea que antes existia ahora NO esta. La borro        
        if (swEntdepos)
            s="delete from albvenseri " +
                " WHERE avs_nume = " + avsNume +
                " and avs_numlin = " + jt.getValorInt(0);
        else
            s = "delete from v_albvenpar WHERE avc_ano = " + avc_anoE.getValorInt() +
                " and emp_codi = " + emp_codiE.getValorInt() +
                " and avc_nume = " + avc_numeE.getValorInt() +
                " and avc_serie = '" + avc_seriE.getText() + "'" +
                " and avl_numlin = " + jt.getValorInt(JT_NULIAL) +
                " AND avp_numlin = " + v.getNumLinea();
        stUp.executeUpdate(s);
         
//        if ( ! swEntdepos)
//        { // Vuelvo a crear el stock
//             anuStkPart(v.getProducto(),
//                   v.getEjercLot(),
//                   emp_codiE.getValorInt(),
//                   v.getSerie(),
//                   v.getLote(),
//                   v.getNumind(),
//                   v.getCanti()* -1,
//                   v.getNumuni()* -1 );
//        }
      }
    }
    double kg = 0;
    int nUn = 0;
    // Miro los datos actuales para ver si debo actualizarlos
    for (int n = 0; n < nRow; n++)
    {
      if (!isLinDesVal(n))
        continue;
      
      kg += jtDes.getValorDec(n, JTDES_KILOS);
      nUn += jtDes.getValorDec(n, JTDES_UNID);
      l = buscaIndiv(datAnt,n);
      if (l==-1)
      {
         if (swEntdepos)
             guardaLinEnt(n,jt.getValorInt(0));
        else
             guardaLinDes(n, jt.getValorInt(0));
      }
    }
    jt.setValor("" + kg, 3);
    jt.setValor("" + nUn, 4);
    ctUp.commit();
  }
  /**
   * Actualizar Individuos entregados
   * @throws SQLException
   */
  void actIndEntreg() throws SQLException
  {

  }
  /**
   * Busca los datos del individuo que hay en la linea de jtDes mandada 
   * en un vector anteriormente cargado con todos los individuos existentes.
   * @param dat vector con los datos anteriores
   * @param nLin Numero de Linea con la que comparar
   * @return posicion donde lo ha encontrado.
   */
  int buscaIndiv(DatIndiv v,int nRows)
  {
   
    for (int nLin = 0; nLin < nRows; nLin++)
    {     
      if ( jtDes.getValorInt(nLin, JTDES_PROCODI)==v.getProducto()  && // Prod.
         jtDes.getValorInt(nLin, JTDES_EJE) == v.getEjercLot() && // ejer
          jtDes.getValString(nLin, JTDES_SERIE).equals(v.getSerie()) && // Serie
          jtDes.getValorInt(nLin, JTDES_LOTE)==v.getLote() && // Lote
          jtDes.getValorInt(nLin, JTDES_NUMIND)==v.getNumind() && // Ind
           jtDes.getValorDec(nLin, JTDES_KILOS ) ==v.getCanti() && // Cantidad
          jtDes.getValorInt(nLin, JTDES_UNID)==v.getNumuni() && // N. Indiv
          jtDes.getValorInt(nLin,JTDES_NUMLIN) == v.getNumLinea()) // No Linea
      { 
        v.setAuxiliar("E"); // Lo marco como encontrado
        return nLin;
      }
    }
    return -1;
  }
/**
   * Busca los datos del individuo que hay en la linea de jtDes mandada 
   * en un vector anteriormente cargado con todos los individuos existentes.
   * @param dat vector con los datos anteriores
   * @param nLin Numero de Linea con la que comparar
   * @return posicion donde lo ha encontrado.
   */
  int buscaIndiv(ArrayList<DatIndiv>  datInd,int nRow)
  {
    int nRows=datInd.size();
    for (int nLin = 0; nLin < nRows; nLin++)
    {     
      DatIndiv v=datInd.get(nLin);
      if ( jtDes.getValorInt(nRow, JTDES_PROCODI)==v.getProducto()  && // Prod.
         jtDes.getValorInt(nRow, JTDES_EJE) == v.getEjercLot() && // ejer
          jtDes.getValString(nRow, JTDES_SERIE).equals(v.getSerie()) && // Serie
          jtDes.getValorInt(nRow, JTDES_LOTE)==v.getLote() && // Lote
          jtDes.getValorInt(nRow, JTDES_NUMIND)==v.getNumind() && // Ind
           jtDes.getValorDec(nRow, JTDES_KILOS ) ==v.getCanti() && // Cantidad
          jtDes.getValorInt(nRow, JTDES_UNID)==v.getNumuni() && // N. Indiv
          jtDes.getValorInt(nRow,JTDES_NUMLIN) == v.getNumLinea()) // No Linea
      {        
        return nLin;
      }
    }
    return -1;
  }

  boolean igualInt(String val1, String val2)
  {
    val1 = val1.replaceAll(",", "");
    val2 = val2.replaceAll(",", "");

    return Integer.parseInt(val1.trim()) == Integer.parseInt(val2.trim());
  }

  boolean igualDouble(String val1, String val2)
  {
    val1 = val1.replaceAll(",", "");
    val2 = val2.replaceAll(",", "");
    return Double.parseDouble(val1.trim()) == Double.parseDouble(val2.trim());
  }
  /**
   * Guarda Lineas de Desglose del albaran y la linea del albaran en si
   * Las lineas de Desglose guarda todas las disponibles sobre la linea de albaran mandada.
   * Actualiza o inserta la linea segun sea necesario
   * @param nLiGrAl int Nº de Linea de Albaran
   * @throws Exception Error en DB
   */
  void guardaLinDes(int nLiGrAl) throws Exception
  {
    String condWhere;
    if (pvc_numeE.isEnabled())
    {
      pvc_anoE.setEnabled(false);
      pvc_numeE.setEnabled(false);
      BbusPed.setEnabled(false);
    }

    if (jt.getValorInt(nLiGrAl, 0) != 0)
    { // Linea de albaran ya existia
      if (pro_codiE.getTipoLote()!='V')
      { // Producto NO es Vendible.
        condWhere = " WHERE emp_codi = " + emp_codiE.getValorInt() +
            " and avc_ano = " + avc_anoE.getValorInt() +
            " and avc_serie = '" + avc_seriE.getText() + "'" +
            " and avc_nume = " + avc_numeE.getValorInt() +
            " and avl_numlin = " + jt.getValorInt(nLiGrAl, 0);

        jt.setValor("" + avl_cantiE.getValorDec(), nLiGrAl, 3);
        jt.setValor("" + avl_unidE.getValorInt(), nLiGrAl, 4);
        if (avc_deposE.getValor().equals("E"))
            s = "UPDATE albvenserl set avs_canti = " + avl_cantiE.getValorDec() +
                " , avs_unid = " + avl_unidE.getValorInt() + condWhere;
        else
            s = "UPDATE v_albavel set avl_canti = " + avl_cantiE.getValorDec() +
               " , avl_unid = " + avl_unidE.getValorInt() +
                ", avl_numpal = "+avl_numpalE.getValorInt() +
                condWhere;
//        debug(s);
        stUp.executeUpdate(dtAdd.getStrSelect(s));
        ctUp.commit();
      }
      else
      { // Nueva linea
        actGridDes();
        actAcumPed(pro_codiE.getValorInt());
      }
      return;
    }

    if (avc_numeE.getValorInt() == 0)
    {  // GUARDA la cabecera del albaran
       
      avc_deposE.setEnabled(false);
      if (! swEntdepos)
      {
        avc_numeE.setValorDec(getNumAlb());
        disableCamposIndice();
        guardaCab();
      }
      else
      {
        if (avsNume==0)
        {
            if (avc_numeE.getValorInt()==0)
            {
                if (!checkIndiv(jtDes.getSelectedRow() ,true))
                    return;
                ponAlbaran(lkDepo);
            }
            guardaCabEnt();
        }
      }
      setBloqueo(dtAdd, "v_albavec",
                 avc_anoE.getValorInt() + "|" + emp_codiE.getValorInt() +
                 "|" + avc_seriE.getText() + "|" + avc_numeE.getValorInt(), false);
    }
    boolean swGuarLin = false;
    int nRow = jtDes.getRowCount();
    int nLiAlb ;
    nLiAlb=getMaxNumLinAlb(swEntdepos,emp_codiE.getValorInt(),avc_anoE.getValorInt(),avc_seriE.getText(),
        swEntdepos?avsNume:avc_numeE.getValorInt(),dtCon1);
 
    nLiAlb++;
    double kilos = 0;
    int nUni = 0;
    // Busca todas las lineas desglosadas y calcula acumulados
    for (int n = 0; n < nRow; n++)
    {
      if (!isLinDesVal(n))
        continue;
      swGuarLin = true;
      kilos += jtDes.getValorDec(n, 8);
      nUni += jtDes.getValorDec(n, 4);
    }

    if (pro_codiE.getTipoLote()!='V'  && pro_codiE.getValorInt() > 0)
    { // Producto NO ES VENDIBLE
      jt.setValor("" + nLiAlb, nLiGrAl, 0);
      jt.setValor("" + avl_cantiE.getValorDec(), nLiGrAl, 3);
      jt.setValor("" + avl_unidE.getValorInt(), nLiGrAl, 4);
      if (swEntdepos)
        guardaLinAlbEnt(nLiAlb,jt.getSelectedRow());
      else
        guardaLinAlb(nLiAlb, jt.getSelectedRow());
    }
    else
    { // Producto es vendible
      if (swGuarLin)
      {
        if (kilos==0)
        {
            enviaMailError("Kilos a 0 en linea: "+jt.getSelectedRow()+" de albaran: "+
                    emp_codiE.getValorInt()+avc_seriE.getText()+avc_numeE.getValorInt());
        }
        jt.setValor("" + nLiAlb, 0);
        jt.setValor("" + kilos, 3);
        jt.setValor("" + nUni, 4);
        if (swEntdepos)
            guardaLinAlbEnt(nLiAlb,jt.getSelectedRow());
        else
            guardaLinAlb(nLiAlb, jt.getSelectedRow());
      }
    }
    // Inserta lineas de desglose.
    for (int n = 0; n < nRow; n++)
    {
      if (!isLinDesVal(n))
        continue;
      if (swEntdepos)
        guardaLinEnt(n, nLiAlb);
      else
        guardaLinDes(n, nLiAlb);
    }
    if (! swEntdepos)
        actAcumPed(pro_codiE.getValorInt());
    ctUp.commit();
  }
  int getMaxNumLinAlb(boolean swEntDepos,int empCodi,int avcAno,String avcSerie,int avcNume,DatosTabla dt) throws SQLException
  {
    if (swEntDepos)
           s="SELECT max(avs_numlin) as avl_numlin FROM albvenserl "
                + " WHERE avs_nume = " + avcNume ;
       else
           s = "select max(avl_numlin) as avl_numlin FROM v_albavel " +
               " WHERE avc_ano =" + avcAno +
               " and emp_codi = " + empCodi+
               " and avc_serie = '" +avcSerie+ "'" +
               " and avc_nume = " + avcNume;
    dt.select(s);
    return dtCon1.getInt("avl_numlin", true);
  }
  /**
   * Comprueba si una linea del grid de partidas es valida
   * Para ello comprueba el campo 8 (kilos) y 6 (Lote) sean igual a 0
   * Si ninguno es 0 la linea es valida.
   *
   * @param n int Linea del grid
   * @return boolean true si es valida.
   */
  boolean isLinDesVal(int n)
  {
    return !(jtDes.getValorDec(n, 8) == 0);// && jtDes.getValorDec(n, 6) == 0);
  }

  /**
   * Guarda la linea de desglose del albaran de Entrega de deposito
   * @param nLin  Numero de Linea en el grid
   * @param nLiAlb Numero de linea de albaran.
   * @throws Exception
   */
  void guardaLinEnt(int nLin, int nLiAlb) throws Exception
  {
    if (avsNume==0)
    {
        guardaCabEnt(); // Todavia no se ha guardado la cabecera de alb. de deposito. La guardo
//        s="Error al guardar Individuos de albaran de Entrega. Numero Albaran Interno"+avsNume
//                + " Albaran: "+emp_codiE.getValorInt()+avc_seriE.getText()+avc_numeE.getValorInt();
//        enviaMailError(s);
//        throw new Exception(s);
    }
    dtAdd.addNew("albvenseri");
    dtAdd.setDato("avs_nume",avsNume);
    dtAdd.setDato("avs_numlin", nLiAlb);
    dtAdd.setDato("avi_numlin", nLin);
//    dtAdd.setDato("pro_codi", jtDes.getValorInt(nLin, 0));
    dtAdd.setDato("avs_ejelot", jtDes.getValorInt(nLin, 3));
    dtAdd.setDato("avs_emplot", jtDes.getValorInt(nLin, 2));
    dtAdd.setDato("avs_serlot", jtDes.getValString(nLin, 5));
    dtAdd.setDato("avs_numpar", jtDes.getValorInt(nLin, 6));
    dtAdd.setDato("avs_numind", jtDes.getValorInt(nLin, 7));
    dtAdd.setDato("avs_numuni", jtDes.getValorInt(nLin, 4));
    dtAdd.setDato("avs_canti", jtDes.getValorDec(nLin, 8));
    dtAdd.update(stUp);
  }
  /**
   * Guarda la linea de desglose del albaran, los individuos, vamos.
   * @param nLin  Numero de Linea en el grid
   * @param nLiAlb
   * @throws Exception
   */
  void guardaLinDes(int nLin, int nLiAlb) throws Exception
  {
    
    s="SELECT max(avp_numlin) as avp_numlin FROM v_albvenpar "
        + " WHERE emp_codi="+emp_codiE.getValorInt()+
        " and avc_ano = "+avc_anoE.getValorInt()+
        " and avc_serie ='"+avc_seriE.getText()+"'"+
        " and avc_nume = "+avc_numeE.getValorInt()+
        " and avl_numlin = "+nLiAlb;
    dtAdd.select(s);
    int avpNumlin=dtAdd.getInt("avp_numlin",true)+1;
    int avpNumpar=jtDes.getValorInt(nLin, 6);
    if (avpNumpar==0)
    {
        throw new Exception("Error interno. Numero partida invalido\n Albaran: "+emp_codiE.getValorInt()+"/"
            +avc_anoE.getValorInt()+
             avc_seriE.getText()+avc_numeE.getValorInt()+" Linea desgl: "+nLin+" Lin.Alb: "+nLiAlb );
    }
    dtAdd.addNew("v_albvenpar");
    dtAdd.setDato("avc_ano", avc_anoE.getValorInt());
    dtAdd.setDato("emp_codi", emp_codiE.getValorInt());
    dtAdd.setDato("avc_serie", avc_seriE.getText());
    dtAdd.setDato("avc_nume", avc_numeE.getValorInt());
    dtAdd.setDato("avl_numlin", nLiAlb);
    dtAdd.setDato("avp_numlin",avpNumlin); 
    dtAdd.setDato("pro_codi", jtDes.getValorInt(nLin, 0));
    dtAdd.setDato("avp_tiplot", "L");
    dtAdd.setDato("avp_ejelot", jtDes.getValorInt(nLin, 3));
    dtAdd.setDato("avp_emplot", jtDes.getValorInt(nLin, 2));
    dtAdd.setDato("avp_serlot", jtDes.getValString(nLin, 5));
    dtAdd.setDato("avp_numpar", avpNumpar);
    dtAdd.setDato("avp_numind", jtDes.getValorInt(nLin, 7));
    dtAdd.setDato("avp_numuni", jtDes.getValorInt(nLin, 4));
    dtAdd.setDato("avp_canti", jtDes.getValorDec(nLin, 8));
    dtAdd.update(stUp);
//    anuStkPart(jtDes.getValorInt(nLin, JTDES_PROCODI), jtDes.getValorInt(nLin, JTDES_EJE),
//               jtDes.getValorInt(nLin, JTDES_EMP), jtDes.getValString(nLin, JTDES_SERIE),
//               jtDes.getValorInt(nLin, JTDES_LOTE), jtDes.getValorInt(nLin, JTDES_NUMIND),
//               jtDes.getValorDec(nLin, JTDES_KILOS) * 1, jtDes.getValorInt(nLin, JTDES_UNID));
    jtDes.setValor(avpNumlin,nLin,JTDES_NUMLIN);
  }

//  private boolean anuStkPart(int proCodi, int ejeLot, int empLot, String serLot, int numLot,
//                 int nInd, double kilos, int unid) throws Exception
//  {
//    try
//    {
//      if (numLot==0)
//      { // Solo resto del Acumulado.
//        return stkPart.sumar(ejeLot, serLot, numLot, nInd, proCodi,
//                            avc_almoriE.getValorInt(), kilos*-1, unid*-1,avc_fecalbE.getText(),
//                            actStkPart.CREAR_SI,0,avc_fecalbE.getDate());
////        stkPart.actAcum(proCodi,alm_codiE.getValorInt(),kilos*-1,unid*-1,avc_fecalbE.getText());
////        return false;
//      }
//      return stkPart.restar(ejeLot, serLot, numLot, nInd, proCodi,
//                            avc_almoriE.getValorInt(), kilos, unid);
//    } catch (SQLWarning k)
//    {
//       aviso("NO SE Pudo restar stock en pdalbara:\nAlb: "+emp_codiE.getValorInt()+"-"+
//             avc_seriE.getText()+avc_numeE.getValorInt()+"\n"+ k.getMessage());
//       return false;
//    }
//  }
  /**
   * Guarda linea albaran de entrega
   * @param nLiAlb
   * @param nLin
   * @throws Exception G
   */
  void guardaLinAlbEnt(int nLiAlb, int nLin) throws Exception
  {
    if (avsNume==0)
    {
        guardaCabEnt();
//        s="Error al guardar linea de albaran de Entrega. Numero Albaran Interno"+avsNume
//                + " Albaran: "+emp_codiE.getValorInt()+avc_seriE.getText()+avc_numeE.getValorInt();
//        enviaMailError(s);
//        throw new Exception(s);
    }
    dtAdd.addNew("albvenserl");
    dtAdd.setDato("avs_nume", avsNume);
    dtAdd.setDato("avs_numlin", nLiAlb);
    dtAdd.setDato("pro_codi", jt.getValorInt(nLin, 1));
    dtAdd.setDato("pro_nomb", jt.getValString(nLin, 2));
    dtAdd.setDato("avs_canti", jt.getValorDec(nLin, 3));
    dtAdd.setDato("avs_unid", jt.getValorInt(nLin, 4));
    dtAdd.update(stUp);
  }
  void guardaLinAlb(int nLiAlb, int nLin) throws Exception
  {
    int nLiFra = 0;
    if (fvc_numeE.getValorInt() > 0)
    {
      s = "select max(fvl_numlin) as fvl_numlin from v_facvel " +
          " WHERE eje_nume = " + fvc_anoE.getValorInt() +
          " and emp_codi = " + emp_codiE.getValorInt() +
          " and fvc_serie = '"+fvc_serieE.getText()+"'"+
          " and fvc_nume = " + fvc_numeE.getValorInt();
      dtAdd.select(s);
      nLiFra = dtAdd.getInt("fvl_numlin") + 1;
      dtAdd.addNew("v_facvel");
      dtAdd.setDato("eje_nume", fvc_anoE.getValorInt());
      dtAdd.setDato("emp_codi", emp_codiE.getValorInt());
      dtAdd.setDato("fvc_nume", fvc_numeE.getValorInt());
      dtAdd.setDato("fvc_serie",fvc_serieE.getText());
      dtAdd.setDato("fvl_numlin", nLiFra);
      dtAdd.setDato("fvl_tipdes", "%");
      dtAdd.setDato("pro_codi", jt.getValorInt(nLin, 1));
      dtAdd.setDato("avc_nume", avc_numeE.getValorInt());
      dtAdd.setDato("avc_ano", avc_anoE.getValorInt());
      dtAdd.setDato("avc_serie", avc_seriE.getText());
      dtAdd.setDato("avc_fecalb", avc_fecalbE.getText(), "dd-MM-yyyy");
      dtAdd.setDato("fvl_prve2", 0);
      dtAdd.setDato("fvl_dto2", 0);
      dtAdd.setDato("fvl_tireta", "%");
      dtAdd.setDato("fvl_rectas", 0);
      dtAdd.setDato("fvl_reta2", 0);
      dtAdd.setDato("fvl_canti", jt.getValorDec(nLin, 3));
      dtAdd.setDato("fvl_dto", 0);
      dtAdd.setDato("fvl_prven", 0);
      dtAdd.update(stUp);
    }

    dtAdd.addNew("v_albavel");
    dtAdd.setDato("avc_ano", avc_anoE.getValorInt());
    dtAdd.setDato("emp_codi", emp_codiE.getValorInt());
    dtAdd.setDato("avc_serie", avc_seriE.getText());
    dtAdd.setDato("avc_nume", avc_numeE.getValorInt());
    dtAdd.setDato("avl_numlin", nLiAlb);
    dtAdd.setDato("pvc_nume", 0);
    dtAdd.setDato("pro_codi", jt.getValorInt(nLin, 1));
    dtAdd.setDato("avl_tipdes", "%");
    dtAdd.setDato("fvl_numlin", nLiFra);
    dtAdd.setDato("alm_codi", avc_almoriE.getValorInt());
    dtAdd.setDato("pro_nomb", jt.getValString(nLin, 2));
    dtAdd.setDato("avl_numues", 0);
    dtAdd.setDato("avl_fecmue", (java.sql.Date) null);
    dtAdd.setDato("avl_fecrli", Formatear.getFechaAct("dd-MM-yyyy"),"dd-MM-yyyy");
    dtAdd.setDato("avl_numcaj", 0);
    dtAdd.setDato("avl_numpal", jt.getValorInt(nLin,JT_NUMPALE));
    dtAdd.setDato("avl_coment", "");
    dtAdd.setDato("aux_2", (String) null);
    dtAdd.setDato("aux_3", (String) null);
    dtAdd.setDato("precioventa2", 0);
    dtAdd.setDato("descuento2", 0);
    dtAdd.setDato("preciobase2", 0);
    dtAdd.setDato("ptsincremento2", 0);
    dtAdd.setDato("preciotarifa2", 0);
    dtAdd.setDato("precioclientearticulo2", 0);
    dtAdd.setDato("preciopvp2", 0);
    dtAdd.setDato("tiporecargotasa", "%");
    dtAdd.setDato("recargotasa", 0);
    dtAdd.setDato("recargotasa2", 0);
    dtAdd.setDato("avl_canti", jt.getValorDec(nLin, 3));
    dtAdd.setDato("avl_prven", 0);
    dtAdd.setDato("avl_dtolin", 0);
    dtAdd.setDato("avl_prbase", 0);
    dtAdd.setDato("avl_ptsinc", 0);
    dtAdd.setDato("tar_preci", jt.getValorDec(nLin, 6));
    dtAdd.setDato("avl_unid", jt.getValorInt(nLin, 4));
    dtAdd.setDato("avl_pincre", 0);
    dtAdd.setDato("avl_pcomi", 0);
    dtAdd.setDato("avl_profer", 0);
    dtAdd.setDato("avl_prclar", 0);
    dtAdd.setDato("avl_prepvp", 0);
    dtAdd.setDato("avl_poreo", 0);
    dtAdd.setDato("avl_canbru", jt.getValorDec(nLin, 3));
    dtAdd.setDato("avl_anorec", 0);
    dtAdd.setDato("avl_serere", "");
    dtAdd.setDato("numrecepcion", 0);
    dtAdd.setDato("avc_cerra", 0);
    dtAdd.setDato("codproveedor", 0);
    dtAdd.setDato("serierecepcion", 0);
    dtAdd.setDato("avl_fecalt","current_timestamp");
    dtAdd.update(stUp);
  }

  int cambiaLinAlb(int row)
  {
    try
    {
      if (pro_codiE.getText().length() > 6)
      {
        mensajeErr("Codigo de Producto .. NO VALIDO");
        return 1;
      }
      if (pro_codiE.isNull())
        return -1;
      if (!pro_codiE.controla(false, false,sbe_codiE.getValorInt()))
      {
        mensajeErr(pro_codiE.getMsgError());
        return 1;
      }
      if (! pro_codiE.isActivo())
      {
          mensajeErr("Producto esta marcado como INACTIVO");
          return 1;
      }
      // Si el producto NO es vendible ( linea Comentario) se guarda directamente.
      if (pro_codiE.getTipoLote()!='V')
        guardaLinDes(row);

      if (jt.getValorInt(row, 0) == 0)
      {
        mensajeErr("Introduzca Individuos de Producto");
        return 1;
      }


      return -1;
    }
    catch (Exception k)
    {
      Error("Error al cambiar Linea Albaran", k);
    }
    return 0;
  }

  void actAcumLin()
  {
    int nRow = jt.getRowCount();
    int nLin = 0;
    double kilosT = 0, impBim = 0;

    for (int n = 0; n < nRow; n++)
    {
      if (jt.getValorInt(n, 0) == 0)
        continue;
      nLin++; //= jt.getValorInt(n, 4);
      kilosT += jt.getValorDec(n, 3);
      impBim += jt.getValorDec(n, 3) * jt.getValorDec(n, 6);
    }
    numLinE.setValorDec(nLin);
    kilosE.setValorDec(kilosT);
    impLinE.setValorDec(impBim);
  }
  /**
   * Cambia Linea Grid de Residuos
   * @param row
   * @return 
   */
  int cambiaLinRes(int row)
  {
      if (pro_codresE.isNull())
          return -1;
      try {
        if (! pro_codresE.controla(false, false))
        {
            mensajeErr(pro_codresE.getMsgError());
            return 0;
        }
        if (pro_codresE.getFamInt()!=MantFamPro.getFamiliaRecicla(EU))
        {
            mensajeErr("Solo permitidos productos de la Familia de Reciclaje");
            return 0;
        }
        if (avr_cantiE.getValorDec()==0)
        {
            mensajeErr("Introduzca Kilos");
            return 2;
        }
      } catch (SQLException k)
      {
          Error("Error al controlar Lineas de Residuos",k);
          return 0;
      }
      return -1;
  }
  /**
   * Cambia Linea Despiece
   * @param row Linea activa
   * @return < 0 si todo ha ido bien. >=0 campo de error
   */
  int cambiaLinDes(int row)
  {
      try {

           if (avp_cantiE.getValorDec() == 0)// && avp_numparE.getValorDec() == 0)
              return -1; // Sin Kilos ... NO compruebo NADA pues ignorare la linea
          if (swEntdepos)
          {// Entrega de deposito
              int nRow=jtDes.getRowCount();
              int nAct=0;
              for (int n=0;n<nRow;n++)
              {
                  if (jtDes.getValorDec(n,JTDES_KILOS)!=0)
                      nAct++;
              }
              if (nAct>1)
              {
                  msgBox("En albaranes de entrega solo se puede meter un individuo por linea albaran");
                  return JTDES_KILOS;
              }
          }
          if (pro_codicE.getValorInt() != jt.getValorInt(1)) {
              mensajeErr("Producto en Linea Albaran y Desglose debe ser el mismo");
              pro_codicE.setValorInt(jt.getValorInt(1));
              return 0;
          }
          if (!pro_codicE.controla(false, false)) {
              mensajeErr(pro_codicE.getMsgError());
              return 0;
          }
//          if (! emp_codiE.hasAcceso(avp_emplotE.getValorInt()))
//          {
//              mensajeErr("Empresa no valida");
//              return 2;
//          }
          if (avp_ejelotE.getValorInt() == 0) {
              mensajeErr("Introduzca Ejercicio de Lote");
              return 3;
          }
          if (avp_serlotE.getText().trim().equals("")) {
              mensajeErr("Introduzca Serie de Lote");
              return 5;
          }
          if (avp_numuniE.getValorInt() == 0) {
              mensajeErr("Introduzca Número de Unidades");
              return 4;
          }
          if (avp_cantiE.getValorDec() == 0) {
              mensajeErr("Introduzca peso de Producto");
              return 8;
          }
          if (avp_numparE.getValorDec() == 0) {
              mensajeErr("Introduzca Número Lote de Producto");
              return 6;
          }

          actAcuLiDes(row); // Actualiza Acumulados lineas desglose

        //  double unid = 0;
        //  double canti = 0;
          if (!checkIndiv(row,true))
              return 6;
         
          if (! swEntdepos)
          { // No es entrega de desposito
              if (avp_cantiE.isEditable() && ! isEmpPlanta)
              {
                if ( dtCon1.getDouble("stp_kilact") > avp_cantiE.getValorDec() + 0.1
                      && avp_numuniE.getValorInt() == 1) {
                  lanzaDespVentas(row);
                }
              }
//              else
//              {
//                  if (avp_cantiE.getValorDec() > dtCon1.getDouble("stp_kilact"))
//                  {
//                      mensajeErr("No hay suficientes kilos de este individuo");
//                      return 6;
//                  }
//              }
          }
          else
          { // Es entrega de deposito.
              if ( avc_numeE.getValorInt() == 0)
              {
                  ponAlbaran(lkDepo);
                  guardaCabEnt();   
              }
          }
      } catch (Exception k) {
          Error("Error al cambiar Linea Desglose de Ventas", k);
      }
      return -1;

  }
  private void ponAlbaran(vlike lkDepo) throws SQLException
  {
        avc_numeE.setValorInt(lkDepo.getInt("avc_nume"));
        emp_codiE.setValorInt(lkDepo.getInt("emp_codi"));
        avc_anoE.setValorInt(lkDepo.getInt("avc_ano"));
        avc_seriE.setText(lkDepo.getString("avc_serie"));
        avc_almoriE.setValor(lkDepo.getString("avc_almori"));
  }
  /**
   * Chequea si un individuo es valido.
   * Llena la variable lkdepo con los datos del albaran de deposito en caso de ser
   * un albaran de entrega.
   * @param row Linea del grid de albaran.
   * @param swActual Actualizar campos en grid?
   * @return false si el individuo no es valido
   */
    private boolean checkIndiv(int row,boolean swActual) throws Exception {
        double unid = 0;
        double canti = 0;
        
        if (jt.getValorInt(0) > 0)
        { // Tiene Numero de Linea el Albaran
            if (swEntdepos)
            { // Es un albaran de  entrega de  deposito.
                s = "SELECT 1 as avp_numuni, sum(avs_canti) as avp_canti FROM albvenseri "
                        + " WHERE avs_nume = " + avsNume
                        + " and avs_numlin=" + jt.getValorInt(0)
                        + " and avs_serlot = '" + avp_serlotE.getText() + "'"
                        + " and avs_numpar = " + avp_numparE.getValorInt()
                        + " and avs_ejelot = " + avp_ejelotE.getValorInt()
                        + " and avs_emplot = " + avp_emplotE.getValorInt()
                        + " and avs_numind = " + avp_numindE.getValorInt();
            }
            else
            {
                s = "SELECT sum(avp_numuni) as avp_numuni, sum(avp_canti) as avp_canti FROM v_albvenpar "
                        + " WHERE emp_codi = " + emp_codiE.getValorInt()
                        + " AND avc_ano = " + avc_anoE.getValorInt()
                        + " and avc_nume = " + avc_numeE.getValorInt()
                        + " and avc_serie = '" + avc_seriE.getText() + "'"
                        + " and avp_canti > 0 "  // Solo tenemos en cuenta cargos.
                        + " and avl_numlin = " + jt.getValorInt(0)                              
                        + " and avp_serlot = '" + avp_serlotE.getText() + "'"
                        + " and avp_numpar = " + avp_numparE.getValorInt()
                        + " and avp_ejelot = " + avp_ejelotE.getValorInt()
                        + " and avp_emplot = " + avp_emplotE.getValorInt()
                        + " and avp_numind = " + avp_numindE.getValorInt()
                        + " and pro_codi = " + pro_codicE.getText();
            }
            dtCon1.select(s);
            canti = dtCon1.getDouble("avp_canti", true) * -1;
            unid = dtCon1.getDouble("avp_numuni", true) * -1;
        }
        canti += avp_cantiE.getValorDec();
        unid += avp_numuniE.getValorInt();
        int nRow = jt.getRowCount();
        
        for (int n = 0; n < nRow; n++)
        { // Suma los kilos y unidades del mismo individuo q pudiera haber.
            if (n == row) 
                continue;
            
            if (!isLinDesVal(n)) {
                continue;
            }
            if (avp_serlotE.getText().equals(jtDes.getValString(n, 5))
                    && avp_numparE.getValorInt() == jtDes.getValorDec(n, 6)
                    && avp_numindE.getValorInt() == jtDes.getValorInt(n, 7)
                    && avp_ejelotE.getValorInt() == jtDes.getValorInt(n, 3)
                    ) {
                canti += jtDes.getValorDec(n, 8);
                unid += jtDes.getValorInt(n, 4);
            }
        }
        lkDepo = null;
        if (swEntdepos)
        { // Entregando mercancia
            lkDepo = new vlike();
            s = getSqlIndDepos();

            if (!dtStat.selectInto(s, lkDepo)) {
                mensajeErr("A este cliente NO  se le guardo este individuo en deposito");
                return false;
            }
            double kilAlb = dtStat.getDouble("avp_canti");

            // Compruebo si se le ha servido en otro albaran.
            s = "SELECT c.avs_fecha,c.avs_nume FROM albvenseri as i, albvenserc as c "
                    + " WHERE i.avs_nume != " + avsNume
                    + " and c.avs_nume = i.avs_nume "
                    + " and c.cli_codi = " + cli_codiE.getValorInt()
                    + " and i.avs_serlot = '" + avp_serlotE.getText() + "'"
                    + " and i.avs_numpar = " + avp_numparE.getValorInt()
                    + " and i.avs_ejelot = " + avp_ejelotE.getValorInt()
                    + " and i.avs_emplot = " + avp_emplotE.getValorInt()
                    + " and i.avs_numind = " + avp_numindE.getValorInt();
            if (dtStat.select(s)) {
                mensajeErr("Individuo ya se registro como entregado en Alb. Interior: "+dtStat.getInt("avs_nume")
                        +" de fecha: "
                        + dtStat.getFecha("avs_fecha"));
                return false;
            }
            double unAlb = 0;
            // Compruebo lo ha  servido ya en este albaran
            s = "SELECT 1 as avp_numuni FROM albvenseri "
                    + " WHERE avs_nume = " + avsNume
                    + " and avs_serlot = '" + avp_serlotE.getText() + "'"
                    + " and avs_numpar = " + avp_numparE.getValorInt()
                    + " and avs_ejelot = " + avp_ejelotE.getValorInt()
                    + " and avs_emplot = " + avp_emplotE.getValorInt()
                    + " and avs_numind = " + avp_numindE.getValorInt();
            if (dtStat.select(s))
                unAlb++;

            // Miro a ver si se ha servido en este albaran.
            if (unid + unAlb > 1) {
                mensajeErr("Individuo ya se registro en este albaran");
                return false;
            }
            if (swActual)
            {
                avp_cantiE.setValorDec(kilAlb);
                jtDes.setValor(kilAlb, row, 8);
            }
        } // Fin del caso de entregando mercancia

        if (!swEntdepos)
        { // No es entrega de mercancia
            StkPartid canStk=buscaPeso();
            if (canStk.hasError())
                 return false;
            if (canStk.isControlExist())
            {
                if ( canti >= 0
                        && canStk.getKilos() <= canti - 0.1) {
                    mensajeErr("Partida de Stock solo tiene " +  canStk.getKilos()
                            + "  kg. Disponibles");
                    return false;
                }
                if (canStk.getUnidades() < unid) {
                    mensajeErr("Partida de Stock solo tiene " + canStk.getUnidades()
                            + " Unidades Disponibles");
                    return false;
                }
            }
        }
        else
        { //Es entrega de mercancia
            if (avc_seriE.isEnabled() || avc_numeE.getValorInt()==0) {
                avc_numeE.setValorInt(lkDepo.getInt("avc_nume"));
                emp_codiE.setValorInt(lkDepo.getInt("emp_codi"));
                avc_anoE.setValorInt(lkDepo.getInt("avc_ano"));
                avc_seriE.setText(lkDepo.getString("avc_serie"));
                avc_almoriE.setValor(lkDepo.getString("avc_almori"));
                 
            } else {
                if (avc_numeE.getValorInt() != lkDepo.getInt("avc_nume")
                        || emp_codiE.getValorInt() != lkDepo.getInt("emp_codi")
                        || avc_anoE.getValorInt() != lkDepo.getInt("avc_ano")
                        || !avc_seriE.getText().equals(lkDepo.getString("avc_serie"))) {
                    mensajeErr("Este individuo pertenece a otro albaran de deposito");
                    return false;
                }
            }
        }
        return true;
    }
  /**
   * Busca en albaranes para deposito de un cliene, un individuo en particular
   * @return setencia SQL a ejecutar.
   */
  private String getSqlIndDepos()
    {
          return  "SELECT c.emp_codi,c.avc_ano,c.avc_serie,c.avc_nume,c.avc_almori,avp_canti"
                      + " FROM v_albvenpar as p, v_albavec as c "
                      + " WHERE cli_codi = " + cli_codiE.getValorInt()
                      + " and avc_depos = 'D' "
                      + " and p.emp_codi = c.emp_codi "
                      + " and p.avc_ano =  c.avc_ano "
                      + " and p.avc_serie = c.avc_serie "
                      + " and p.avc_nume = c.avc_nume "
                      + " and p.avp_serlot = '" + avp_serlotE.getText() + "'"
                      + " and p.avp_numpar = " + avp_numparE.getValorInt()
                      + " and p.avp_ejelot = " + avp_ejelotE.getValorInt()
                      + " and p.avp_emplot = " + avp_emplotE.getValorInt()
                      + " and p.avp_numind = " + avp_numindE.getValorInt()
                      + " and p.pro_codi = " + pro_codicE.getText();
    }
  /**
   *
   * Lanza el despiece de ventas (despieces al vuelo)
   * @param row
   * @throws Exception
   */
  void lanzaDespVentas(int row) throws Exception
  {
    if (! Bdespiece.isSelected())
      return;
/*

    if (despAlbar==null)
    {
      despAlbar = new despVentas()
      {
        @Override
        public void matar()
        {
         salirDespiece();
        }
      };
      despAlbar.setTidCodi(TIDCODI);
      vl.add(despAlbar);
      despAlbar.setLocation(this.getLocation().x, this.getLocation().y + 30);
      despAlbar.iniciar(dtStat,dtCon1,dtAdd,EU);
    }
    rowDesp=row;
    SwingUtilities.invokeLater(new albveThread(row)
    {
            @Override
      public void run()
      {
        try
        {
          int etiCodi=eti_codiE.getValorInt();
          if (etiCodi < 0)
              etiCodi=0;
          despAlbar.insertaDesp(jtDes.getValorInt(row,0), jtDes.getValorInt(row,3),
                             jtDes.getValorInt(row,2),
                              jtDes.getValString(row,5), jtDes.getValorInt(row,6),
                              jtDes.getValorInt(row,7),
                              avc_almoriE.getValorInt(), avc_fecalbE.getDate(),
                              jtDes.getValorDec(row,8),emp_codiE.getValorInt(),
                              avc_anoE.getValorInt(),
                              eti_codiE.getValorInt());

//          despAlbar.insertaDesp(pro_codicE.getValorInt(), avp_ejelotE.getValorInt(),
//                                avp_emplotE.getValorInt(),
//                                avp_serlotE.getText(), avp_numparE.getValorInt(),
//                                avp_numindE.getValorInt(),
//                                alm_codiE.getValorInt(), avc_fecalbE.getDate(),
//                                avp_cantiE.getValorDec(),
//                                eti_codiE.getValorInt());
          despAlbar.setVisible(true);
          pdalbara.this.setEnabled(false);
        }
        catch (Exception k)
        {
          Error("Error al lanzar ventana de despiece", k);
        }
      }
    });

 */
  }
  /*
  void salirDespiece()
  {
//    despAlbar.setVisible(false);
    this.toFront();
    this.setEnabled(true);
    try
    {
      this.setSelected(true);
    }
    catch (Exception k)
    {}
    if (! despAlbar.isCancelado())
    {
      jtDes.setValor(""+despAlbar.getKilos(),  rowDesp,8);
      jtDes.setValor(""+despAlbar.getNumInd(),  rowDesp,7);
//      avp_cantiE.setValorDec(despAlbar.getKilos());
//      avp_numindE.setValorDec(despAlbar.getNumInd());
      jtDes.procesaAllFoco();
    }
    SwingUtilities.invokeLater(new Thread()
    {
      @Override
      public void run()
      {
        jtDes.requestFocusSelected();
      }
    });

  }*/
  /**
   * Actualizar Acumulados de Linea de Desglose
   *
   * @param row int linea
   */
  void actAcuLiDes(int row)
  {
    int nRow = jtDes.getRowCount();

    double kilos = 0, impBim = 0;
    int nUni = 0;

    for (int n = 0; n < nRow; n++)
    {
      if (!isLinDesVal(n))
        continue;
      if (row == n)
      {
        kilos += avp_cantiE.getValorDec();
        nUni += avp_numuniE.getValorDec();
        impBim += avp_cantiE.getValorDec() * jt.getValorDec(5);
      }
      else
      {
        kilos += jtDes.getValorDec(n, 8);
        nUni += jtDes.getValorDec(n, 4);
        impBim += jtDes.getValorDec(n, 8) * jt.getValorDec(5);
      }
    }
    jt.setValor("" + kilos, 3);
    jt.setValor("" + nUni, 4);
    avl_cantiE.setValorDec(kilos);
    avl_unidE.setValorDec(nUni);
    impLinE.setValorDec(impBim);
//    actAcumPed(jt.getRowCount());
  }
  /**
   * Actualizar acumulados de pedidos de ventas
   * @param proCodi
   * @throws SQLException
   * @throws ParseException
   */
  void actAcumPed(int proCodi) throws SQLException,ParseException
  {

    if (pvc_numeE.getValorInt()==0 || swEntdepos)
      return; // No tiene pedido o  es un albaran de deposito que se esta Entregando
   
    // Borro las lineas de pedido actuales
    for (int n=0;n<jtLinPed.getRowCount();)
    {
      if (! jtLinPed.getValString(n,0).equals("A"))
      {
        n++;
        continue;
      }
      if (proCodi==0 || jtLinPed.getValorInt(n,1)==proCodi)
        jtLinPed.removeLinea(n);
      else
        n++;
    }
    // Se busca por un lado lo que tiene stock, para buscar el prv. y si no tiene stock
    // se pone al cliente como prv.
    s="select 1 as tipo,l.pro_codi,sum(avp_numuni) as avp_numuni,sum(avp_canti) as avp_canti " +
             (! opAgrPrv.isSelected()?", s.prv_codi": "")+
              (!opAgrFecha.isSelected()?", s.stp_feccad ":"") +
             " from v_albvenpar as l,v_stkpart as s " +
             " WHERE s.eje_nume = l.avp_ejelot " +
             " and s.emp_codi = l.avp_emplot " +
             " and s.pro_serie = l.avp_serlot " +
             " and s.pro_nupar = l.avp_numpar " +
             " and s.pro_codi = l.pro_codi " +
             " and s.pro_numind = l.avp_numind " +
             " and l.emp_codi = "+emp_codiE.getValorInt()+
             " and l.avc_ano = " + avc_anoE.getValorInt() +
             " and l.avc_serie = '" + avc_seriE.getText() + "'" +
             " and l.avc_nume = " + avc_numeE.getValorInt() +
             (proCodi == 0 ? "" : " and l.pro_codi = " + proCodi)+
             " GROUP BY l.pro_codi "+
             (!opAgrPrv.isSelected()?" ,s.prv_codi ":"")+
             (!opAgrFecha.isSelected()?", stp_feccad ":"")+
             " UNION ALL "+
             "select 0 as tipo, l.pro_codi,sum(avp_numuni) as avp_numuni,sum(avp_canti) as avp_canti " +
             (!opAgrPrv.isSelected() ? ",c.cli_codi AS prv_codi" : "") +
             (!opAgrFecha.isSelected() ? ", c.avc_fecalb as stp_feccad " : "") +
              " from v_albvenpar as l,v_albavec as c  where  c.avc_ano = l.avc_ano  "+
              " and c.emp_codi = l.emp_codi "+
              " and c.avc_serie = l.avc_serie "+
              " and c.avc_nume = l.avc_nume  "+
              " and l.avp_numpar = 0 "+
             " AND l.emp_codi = " + emp_codiE.getValorInt() +
             " and l.avc_ano = " + avc_anoE.getValorInt() +
             " and l.avc_serie = '" + avc_seriE.getText() + "'" +
             " and l.avc_nume = " + avc_numeE.getValorInt() +
             (proCodi == 0 ? "" : " and l.pro_codi = " + proCodi) +
             " GROUP BY l.pro_codi "+
             (!opAgrPrv.isSelected()?" ,c.cli_codi ":"")+
             (!opAgrFecha.isSelected()?", avc_fecalb ":"")+
             " order by 2 ";

//    debug(s);
    if (! dtCon1.select(s))
      return;
    int rowCount;
    int nLin=0;
    do
    {
      rowCount = jtLinPed.getRowCount();
      // Busco posibles lineas de Venta con estas caracteristicas
      for (int n=0;n<rowCount;n++)
      {
        if (jtLinPed.getValString(nLin,0).equals("A") &&
          jtLinPed.getValorInt(nLin,1)==dtCon1.getInt("pro_codi") &&
          (opAgrPrv.isSelected() || jtLinPed.getValorInt(nLin,10)==0
              || jtLinPed.getValorInt(nLin,10)==dtCon1.getInt("prv_codi")) &&
          (opAgrFecha.isSelected() || jtLinPed.getValString(nLin,4).equals("")
             || jtLinPed.getValString(nLin,4).equals(dtCon1.getFecha("stp_feccad","dd-MM-yy")))
           )
         {
           jtLinPed.setValor(""+(dtCon1.getDouble("avp_numuni")+jtLinPed.getValorDec(n,5)),n,5);
           break;
         }
      }
      ArrayList v = new ArrayList();
      v.add("A");
      v.add(dtCon1.getString("pro_codi"));
      v.add(pro_codiE.getNombArtCli(dtCon1.getInt("pro_codi"),
                                           cli_codiE.getValorInt(), EU.em_cod, dtStat));
      if (opAgrPrv.isSelected() || dtCon1.getInt("tipo")==0)
        v.add("");
      else
        v.add(prv_codiE.getNombPrv(dtCon1.getString("prv_codi"), dtStat));
      if (opAgrFecha.isSelected() || dtCon1.getInt("tipo")==0)
        v.add("");
      else
        v.add(dtCon1.getFecha("stp_feccad","dd-MM-yy"));

      v.add(dtCon1.getString("avp_numuni"));
      v.add("");
      v.add(dtCon1.getString("avp_canti"));
      v.add(false);
      v.add("");
      if (opAgrPrv.isSelected())
        v.add("");
      else
        v.add(dtCon1.getString("prv_codi"));
      nLin=0;
      while (nLin<rowCount)
      {

        if (jtLinPed.getValString(nLin,0).equals("A") ||
            jtLinPed.getValorInt(nLin,1)!=dtCon1.getInt("pro_codi") /*||
            ! ((opAgrPrv.isSelected() ||  jtLinPed.getValorInt(nLin,10)==0
           || jtLinPed.getValorInt(nLin,10)==dtCon1.getInt("prv_codi")) &&
           (opAgrFecha.isSelected() || jtLinPed.getValString(nLin,4).equals("")
             || jtLinPed.getValString(nLin,4).equals(dtCon1.getFecha("stp_feccad","dd-MM-yy"))))*/
            )
        {
          nLin++;
          continue;
        }
        v.set(1,"");
        v.set(2,"");
        jtLinPed.addLinea(v,nLin+1);
        break;
      }
      if (nLin>=rowCount)
        jtLinPed.addLinea(v);
    } while (dtCon1.next());

  }
  /**
   * Busca el peso disponible para un individuo
   * @return null si el
   * @throws Exception
   */
  StkPartid buscaPeso() throws Exception
  {
    StkPartid ret = utildesp.buscaPeso(dtCon1, avp_ejelotE.getValorInt(),
                                       EU.em_cod,
                                       avp_serlotE.getText(),
                                       avp_numparE.getValorInt(),
                                       avp_numindE.getValorInt(),
                                       pro_codicE.getValorInt(),
                                       avc_almoriE.getValorInt());
    if (ret.hasError())
        mensajeErr(ret.getMensaje());
    return ret;
  }

  private void resetCambioIndividuo()
  {
//    avp_emplotE.resetCambio();
    avp_ejelotE.resetCambio();
    avp_serlotE.resetCambio();
    avp_numindE.resetCambio();
    avp_numparE.resetCambio();
    pro_codicE.resetCambio();
  }
  /**
   * Pone el valor de la cantidad segun los datos del invidividuo
   */
  void pro_numindE_focusLost()
  {
    if (avp_numparE.getValorInt() == 0 || ! jtDes.isEnabled())
      return;
    if (  !avp_ejelotE.hasCambio() && ! avp_serlotE.hasCambio()
            && !avp_numindE.hasCambio() && !avp_numparE.hasCambio() &&
        !pro_codicE.hasCambio())
      return;
    resetCambioIndividuo();
    if (avp_cantiE.getValorDec() != 0 && avp_cantiE.isEditable())
      return;
    try
    {
      if (swEntdepos)
      {
        s=getSqlIndDepos();
        if (dtStat.select(s))
        {
            jtDes.setValor("" + dtStat.getDouble("avp_canti"), 8);
            avp_cantiE.setValorDec(dtStat.getDouble("avp_canti"));
        }
      }
      else
      {
           s = "SELECT sum(avp_numuni) as avp_numuni, sum(avp_canti) as avp_canti FROM v_albvenpar "
                        + " WHERE emp_codi = " + emp_codiE.getValorInt()
                        + " AND avc_ano = " + avc_anoE.getValorInt()
                        + " and avc_nume = " + avc_numeE.getValorInt()
                        + " and avc_serie = '" + avc_seriE.getText() + "'"
                        + " and avl_numlin = " + jt.getValorInt(0)
                        + " and avp_serlot = '" + avp_serlotE.getText() + "'"
                        + " and avp_numpar = " + avp_numparE.getValorInt()
                        + " and avp_ejelot = " + avp_ejelotE.getValorInt()
                        + " and avp_emplot = " + avp_emplotE.getValorInt()
                        + " and avp_numind = " + avp_numindE.getValorInt()
                        + " and pro_codi = " + pro_codicE.getText();
          dtStat.select(s);
          
          int indiv=dtStat.getInt("avp_numuni",true);
          double canti=dtStat.getDouble("avp_canti",true);
          StkPartid cantStk=buscaPeso();
          if (!cantStk.hasError())
          {
             if (! cantStk.hasControlInd())
                  canti=0;
             else
                canti+=cantStk.getKilos();
             if (canti<0)
                  canti=0;
          }

          avp_cantiE.setEditable( cantStk.hasControlInd() && cantStk.isControlExist() ?P_ADMIN:true);
          jtDes.setValor(""+canti,8);
          avp_cantiE.setValorDec(canti);
      }
    }
    catch (Exception k)
    {
      Error("Error al Leer Peso", k);
    }
  }

  int getNumAlb() throws SQLException, ParseException
  {
    return getNumAlb(true);
  }

  int getNumAlb(boolean act) throws SQLException, ParseException
  {
    return getNumAlb(act,dtAdd,emp_codiE.getValorInt(),avc_anoE.getValorInt(),avc_seriE.getText());
  }
  /**
   * 
   * Devuelve Siguiente Numero de Albaran que se deberia asignar
   * @param act Realizar SELECT FORUPDATE
   * @param dt DatosTabla 
   * @param empCodi Empresa
   * @param avcAno Año
   * @param serie Serie
   * @return Numero de albaran
   * @throws SQLException
   * @throws ParseException 
   */
  public static int getNumAlb(boolean act,DatosTabla dt,int empCodi,int avcAno,String serie) throws SQLException, ParseException
   {
     int nAlb;
     String s = "SELECT num_serieA,num_serieB,num_serieC,num_serieD,num_serieX,num_serieY " +
         " FROM v_numerac WHERE emp_codi = " + empCodi +
         " and eje_nume=" + avcAno;
     if (!dt.select(s, true))
       throw new SQLException(
           "NO encontrado GUIA Numeraciones para esta Empresa\n" + s);
     nAlb = dt.getInt("num_serie" + serie) + 1;
     if (!act)
       return nAlb;
     dt.edit(dt.getCondWhere());
     dt.setDato("num_serie" + serie, nAlb);
     dt.update();
     return nAlb;
   }

  /**
   * Guarda cabecera de albaran de entrega
   * @throws SQLException
   */
  void guardaCabEnt() throws SQLException,ParseException
  {
    if (avc_numeE.getValorInt()==0 )
       throw new SQLException("Numero albaran Origen sin poner al guardar cabecera Entrega"); 
    dtAdd.addNew("albvenserc",false);
    dtAdd.setDato("avs_fecha", avc_fecalbE.getDate());
    dtAdd.setDato("avc_ano", avc_anoE.getValorInt());
    dtAdd.setDato("emp_codi", emp_codiE.getValorInt());
    dtAdd.setDato("avc_serie", avc_seriE.getText());
    dtAdd.setDato("avc_nume", avc_numeE.getValorInt());
    dtAdd.setDato("cli_codi", cli_codiE.getValorInt());
    dtAdd.update(stUp);
    ct.commit();
    s="SELECT  currval(pg_get_serial_sequence('albvenserc', 'avs_nume')) ";
    ResultSet rs=dtAdd.getStatement().executeQuery(s);
    rs.next();
    avsNume=rs.getInt(1);
    disableCamposIndice();
  }
  /**
   * Guarda Cabecera del Albaran
   * @throws Exception
   */
  void guardaCab() throws Exception
  {
    dtAdd.addNew("v_albavec");
    dtAdd.setDato("avc_ano", avc_anoE.getValorInt());
    dtAdd.setDato("emp_codi", emp_codiE.getValorInt());
    dtAdd.setDato("avc_serie", avc_seriE.getText());
    dtAdd.setDato("avc_nume", avc_numeE.getValorInt());
    dtAdd.setDato("cli_codi", cli_codiE.getValorInt());
    dtAdd.setDato("avc_cofra", 1); // Cod. Frase
    dtAdd.setDato("avc_fecalb", avc_fecalbE.getText(), "dd-MM-yyyy");
    dtAdd.setDato("usu_nomb", usu_nombE.getText());
    dtAdd.setDato("avc_tipfac",
                  cli_codiE.getLikeCliente().getString("cli_tipfac")); // Tipo Facturacion
    dtAdd.setDato("cli_ruta", rut_codiE.getValorInt());
    dtAdd.setDato("cli_codfa",
                  cli_codiE.getLikeCliente().getInt("cli_codfa"));
    dtAdd.setDato("fvc_ano", 0);
    dtAdd.setDato("fvc_serie",(String) null);
    dtAdd.setDato("fvc_nume", 0);
    dtAdd.setDato("avc_cerra", 0); // Cerrado 0 ES QUE NO. -1 QUE SI
    dtAdd.setDato("avc_impres", 0); // Impreso
    dtAdd.setDato("fvc_trasp", 0); // Traspasado
    dtAdd.setDato("avc_fecemi", avc_fecemiE.getText(), "dd-MM-yyyy"); // Fecha Emision
    dtAdd.setDato("avc_revpre", avc_revpreE.getValor()); // Revisar Precios
    dtAdd.setDato("sbe_codi", sbe_codiE.getValorInt());
    dtAdd.setDato("avc_desrec", "");
    dtAdd.setDato("alm_codori", avc_almoriE.getValorInt()); // Almacen Origen.
    dtAdd.setDato("avc_almori", avc_almoriE.getValorInt()); // Almacen Destino.
    dtAdd.setDato("avc_confo", avc_confoE.isSelected() ? -1 : 0); // Conforme
    dtAdd.setDato("avc_preiva", 0);
    dtAdd.setDato("avc_cobrad", 0);
    dtAdd.setDato("avc_obser", Formatear.strCorta(avc_obserE.getText(),255));
    dtAdd.setDato("avc_fecrca", Formatear.getFechaAct("dd-MM-yyyy"),
                  "dd-MM-yyyy");
    dtAdd.setDato("avc_almori", avc_almoriE.getValor());
    dtAdd.setDato("avc_tarimp", 0);
//    dtAdd.setDato("avc_aux1",null);
//    dtAdd.setDato("avc_aux2",null);
//    dtAdd.setDato("avc_aux3",null);
    dtAdd.setDato("avc_impre2", 0);
    dtAdd.setDato("avc_basimp", impLinE.getValorDec()-impDtoE.getValorDec());
    dtAdd.setDato("avc_imcob2", 0);
    dtAdd.setDato("avc_kilos", kilosE.getValorDec());
    dtAdd.setDato("div_codi", div_codiE.getValor());
    dtAdd.setDato("div_codi2", 1);
    dtAdd.setDato("avc_tottas", 0);
    dtAdd.setDato("avc_totta2", 0);
    dtAdd.setDato("avc_apltas", 0);
   // dtAdd.setDato("avc_tainpr", 0);
    dtAdd.setDato("avc_recarg", 0);
    dtAdd.setDato("avc_imprec", 0);
    dtAdd.setDato("avc_impalb", avc_impalbE.getValorDec());
    dtAdd.setDato("avc_impcob", 0);
    dtAdd.setDato("avc_impuv", 0);
    dtAdd.setDato("avc_cucomi", 0);
    dtAdd.setDato("avc_valora", P_MODPRECIO?avc_valoraE.getValor():"0"); // Se utiliza para saber si los precios son aut. o Manuales
    dtAdd.setDato("avc_dtopp", 0);
    dtAdd.setDato("avc_dtocom", 0);
    dtAdd.setDato("avc_dtootr", 0);
    dtAdd.setDato("avc_recfin", 0);
    dtAdd.setDato("avc_depos",avc_deposE.getValor());

//    dtAdd.setDato("avc_tipalb",null);
//    dtAdd.setDato("avc_rcaedi",null);
//    dtAdd.setDato("avc_nalsab",null);
//    dtAdd.setDato("avc_ncarg",null);
//    dtAdd.setDato("avc_nrelen",null);
    dtAdd.update(stUp);

    // Actualiza la cabecera del pedido si la hay
    ponAlbPedido();
  }

  private void ponAlbPedido() throws ParseException, SQLException
  {
    if (pvc_anoE.getValorInt()!=0)
    { // Albaran con pedido
      if (getClientePedido(dtAdd,true)==0)
        return;
      dtAdd.edit();
      dtAdd.setDato("avc_nume",avc_numeE.getValorInt());
      dtAdd.setDato("avc_ano",avc_anoE.getValorInt());
      dtAdd.setDato("avc_serie",avc_seriE.getText());
      dtAdd.update(stUp);
    }
  }
  void irGridDes()
  {
      swCanti=false;
      irGridDes(JTDES_LOTE);
  }
  void irGridDes(int col_desp)
  {
    if (jt.isVacio())
        return;
    if (nav.pulsado==navegador.EDIT  )
    {
          if (nLiMaxEdit>0 && jt.getValorInt(JT_NULIAL) > 0 && jt.getValorInt(JT_NULIAL)<= nLiMaxEdit )
          {
              msgBox("Albaran de deposito y con genero entregado. Imposible editar lineas");
              return ;
          }
    }
    this.colDesp=col_desp;
    try {
      jt.salirGrid();
      pro_codiE.getNombArt(jt.getValString(1),EU.em_cod, 0,
                           dtStat);
    } catch (SQLException k)
    {
      Error("Error al buscar caracteristicas del producto",k);
    }
    if (pro_codiE.getTipoLote()!='V' || ! pro_codiE.isActivo())
    {
      mensajeErr("Imposible ir a Desglose");
      SwingUtilities.invokeLater(new Thread()
      {
        @Override
        public void run()
        {
          Bdespiece.setEnabled(true);
          jt.requestFocusSelected();
        }
      });
      return;
    }


    SwingUtilities.invokeLater(new Thread()
    {
      @Override
      public void run()
      {
        jt.setEnabled(false);
        if (jtDes.isVacio())
        {
//          jtDes.removeAllDatos();
          ArrayList v=new ArrayList();
          v.add(pro_codiE.getText());
          v.add(pro_nombE.getText());
          v.add( EU.em_cod);
          v.add( EU.ejercicio);
          v.add("1");
          v.add("A");
          v.add("0");
          v.add("0");
          v.add("0");
          v.add("0");
          jtDes.addLinea(v);
          pro_codicE.setText(pro_codiE.getText());
          jtDes.afterInsertaLinea(true);
          jtDes.actualizarGrid(0);

        }
        Bdespiece.setEnabled(false);
        jtDes.setEnabled(true);
        jtDes.requestFocus();
        jtDes.requestFocus(0, colDesp);
        jtDes.actualizarGrid();
        resetCambioIndividuo();
       
        Baceptar.setEnabled(false);
        Bimpri.setEnabled(false);
      }
    });


  }
  /**
   * Rutina que prepara todo para ir al grid de desglose
   */
  void irGridFoco()
  {
    try
    {
      if (swGridDes > 0)
      {
        swGridDes--;
        return;
      }

      if ( (nav.pulsado == navegador.ADDNEW ||
            nav.pulsado == navegador.EDIT)
          && pro_codiE.controla(false, false)
          && jt.getValorInt(0) == 0
          && pro_codiE.getTipoLote()=='V' && pro_codiE.isActivo() && pro_codiE.getValorInt() > 0)
      {
        SwingUtilities.invokeLater(new Thread()
        {
          @Override
          public void run()
          {
            irGridDes0();
          }
        });
      }
    }
    catch (SQLException k)
    {
      Error("Error al Ir al Grid de Despiece", k);
    }
  }

  void irGridDes0()
  {
    if (jt.getValorInt(0) == 0)
    {
      jtDes.removeAllDatos();
      avp_numuniE.resetTexto();
//      avp_emplotE.resetTexto();
      avp_ejelotE.resetTexto();
      avp_serlotE.resetTexto();
      avp_numparE.resetTexto();
      avp_numindE.resetTexto();
      avp_cantiE.resetTexto();
    }
    jt.setEnabled(false);
    jtDes.setEnabled(true);
    pro_codicE.setText(pro_codiE.getText());
    jtDes.setValor(pro_codiE.getText(), 0, 0);
    jtDes.setValor(pro_nombE.getText(), 0, 1);
    jtDes.setValor(""+EU.em_cod, 0, 2);
    jtDes.setValor(""+EU.ejercicio, 0, 3);
    jtDes.setValor("1", 0, 4);
    jtDes.ponValores(0);
    Baceptar.setEnabled(false);
    Bimpri.setEnabled(false);
        SwingUtilities.invokeLater(new Thread()
    {
      @Override
      public void run()
      {
        Bdespiece.setEnabled(false);
        jtDes.requestFocus();
        jtDes.requestFocus(0,  isEmpPlanta?JTDES_UNID:JTDES_LOTE);
        resetCambioIndividuo();
      }
    });

  }

    @Override
  public void activar(boolean b)
  {
    jtRes.setEnabled(false);
    verDatTraz.setEnabled(!b);
    verDepoC.setEnabled(!b);
    if (! P_ADMIN)
        avc_deposE.setEnabled(b);
    Bdesgl.setEnabled(!b);
    Bfincab.setEnabled(b);
    Baceptar.setEnabled(b);
    Bcancelar.setEnabled(b);
    numLinE.setEnabled(false);
    kilosE.setEnabled(false);
    impDtoE.setEnabled(false);
    impLinE.setEnabled(false);
    avc_impalbE.setEnabled(false);
    avc_impcobE.setEnabled(false);
    Bdespiece.setEnabled(false);    
          
    emp_codiE.setEnabled(b);
    avc_anoE.setEnabled(b);
    avc_seriE.setEnabled(b);
    avc_numeE.setEnabled(b);
    div_codiE.setEnabled(b);
    avc_fecalbE.setEnabled(b);
    avc_cerraE.setEnabled(b);
    cli_codiE.setEnabled(b);
    usu_nombE.setEnabled(b);
    avc_fecemiE.setEnabled(b);
    jt.setEnabled(b);
    jtDes.setEnabled(b);
    Birgrid.setEnabled(b);
    Bimpri.setEnabled(true);
    fvc_anoE.setEnabled(b);
    fvc_numeE.setEnabled(b);
    fvc_serieE.setEnabled(b);
    tar_codiE.setEnabled(b);
    rut_codiE.setEnabled(b);
    avc_almoriE.setEnabled(b);
    if (P_MODPRECIO)
    {
      sbe_codiE.setEnabled(b);  
      BValTar.setEnabled(!b);
      avc_dtoppE.setEnabled(b);
      avc_dtocomE.setEnabled(b);      
    }
    if (P_ADMIN)
        avc_confoE.setEnabled(b);
    avc_obserE.setEnabled(b);
    pvc_anoE.setEnabled(b);
    pvc_numeE.setEnabled(b);
    BbusPed.setEnabled(b);
    avl_unidE.setEnabled(b);
    avl_cantiE.setEnabled(b);
    pro_nombE.setEditable(b);
    usu_nompedE.setEnabled(false);
    opAgru.setEnabled(!b);        
  }
@Override
  public void rgSelect() throws SQLException
  {
 //   debug("en rgSelect");

    dtCons.setFetchSize(50);
//    setMaxRows
    super.rgSelect();
    if (!dtCons.getNOREG())
    {
      dtCons.last();
      nav.setEnabled(navegador.ULTIMO, false);
      nav.setEnabled(navegador.SIGUIENTE, false);
    }
//    System.out.println("Memoria Usada: "+(Runtime.getRuntime().totalMemory() -
//  Runtime.getRuntime().freeMemory()));

    verDatos(dtCons);

  }
  /**
   * Borra linea de albaran. Tambien borra todos los individuos asignados
   * @param row
   * @throws Exception
   */
  void borraLinea(int row) throws Exception
  {
    if (jt.getValorInt(row, 0) == 0)
      return;
    if (swEntdepos)
    {
        s = "DELETE FROM albvenserl where avs_nume="+avsNume+
                " and avs_numlin="+jt.getValorInt(row, 0);
        stUp.executeUpdate(s);
        s = "DELETE FROM albvenseri where avs_nume="+avsNume+
                " and avs_numlin="+jt.getValorInt(row, 0);
        stUp.executeUpdate(s);
        ctUp.commit();
        return;
    }
//    s = "SELECT * FROM v_albvenpar " +
//        " WHERE emp_codi = " + emp_codiE.getValorInt() +
//        " AND avc_ano = " + avc_anoE.getValorInt() +
//        " and avc_nume = " + avc_numeE.getValorInt() +
//        " and avc_serie = '" + avc_seriE.getText() + "'" +
//        " and avl_numlin = " + jt.getValorInt(row, 0);
//    if (dtCon1.select(s))
//    {
//      do
//      {
//        anuStkPart(dtCon1.getInt("pro_codi"),
//                   dtCon1.getInt("avp_ejelot"),
//                   dtCon1.getInt("avp_emplot"),
//                   dtCon1.getString("avp_serlot"),
//                   dtCon1.getInt("avp_numpar"),
//                   dtCon1.getInt("avp_numind"),
//                   dtCon1.getDouble("avp_canti") * -1,
//                   dtCon1.getInt("avp_numuni") * -1);
//
//      }  while (dtCon1.next());
//    }
    s = "delete from v_albvenpar WHERE avc_ano = " + avc_anoE.getValorInt() +
        " and emp_codi = " + emp_codiE.getValorInt() +
        " and avc_nume = " + avc_numeE.getValorInt() +
        " and avc_serie = '" + avc_seriE.getText() + "'" +
        " and avl_numlin = " + jt.getValorInt(row, 0);
    stUp.executeUpdate(s);
    if (fvc_numeE.getValorInt() > 0)
    {
      s = "select * from v_albavel WHERE avc_ano = " + avc_anoE.getValorInt() +
          " and emp_codi = " + emp_codiE.getValorInt() +
          " and avc_nume = " + avc_numeE.getValorInt() +
          " and avc_serie = '" + avc_seriE.getText() + "'" +
          " and avl_numlin = " + jt.getValorInt(row, 0) +
          " and fvl_numlin > 0 ";
      if (dtStat.select(s))
      {
        s = "delete from v_facvel WHERE eje_nume = " + fvc_anoE.getValorInt() +
            " and emp_codi = " + emp_codiE.getValorInt() +
            " and fvc_nume = " + fvc_numeE.getValorInt() +
            " and fvc_serie = '"+fvc_serieE.getText()+"'"+
            " and fvl_numlin = " + dtStat.getInt("fvl_numlin");
        stUp.executeUpdate(s);
      }
    }
    s = "delete from v_albavel WHERE avc_ano = " + avc_anoE.getValorInt() +
        " and emp_codi = " + emp_codiE.getValorInt() +
        " and avc_nume = " + avc_numeE.getValorInt() +
        " and avc_serie = '" + avc_seriE.getText() + "'" +
        " and avl_numlin = " + jt.getValorInt(row, 0);
    stUp.executeUpdate(s);

    ctUp.commit();
  }

/**
 * Imprime albaran/hojas de trazabilidad,palets, etc. Segun lo mandado en indice
 */
  private void imprimir(char indice)
  {
    if (!opDispSalida.getValor().equals(DSAL_IMPRE))
    {
        if (indice!=IMPR_ALB_GRAF && indice!=IMPR_HOJA_TRA)
        {
            msgBox("Este tipo de listado solo puede ser mandado a impresora");
            return;
        }
    }
    this.setEnabled(false);
    if (nav.pulsado == navegador.ADDNEW || nav.pulsado == navegador.EDIT)
    {
      graba = false;
      if (nav.pulsado == navegador.EDIT)
        ej_edit();
      else
        ej_addnew();
//      Baceptar.doClick();
      if (!graba)
      {
        this.setEnabled(true);
        return;
      }
    }
    else
    {
      if (jt.getSelectedColumn() == 5 && verPrecios &&
          fvc_anoE.getValorInt() == 0 &&
          fvc_numeE.getValorInt() == 0)
      {
        try {
           actPrecioAlb(jt.getSelectedRow());
        } catch (SQLException k)
        {
            Error("Error al Actualizar Precio de Albaran",k);
            return;
        }
      }      
      if (jt.getSelectedColumn()==JT_NUMPALE)
      {
          try {
            actNumPale(jt.getSelectedRow());
           } catch (SQLException k)
           {
             Error("Error al Actualizar Pale de Albaran",k);
             return;
           }
      }
    }
   
    ultSelecImpr=indice;
    if (indice==IMPR_ALB_GRAF || indice == IMPR_ALB_TEXT
        || indice== IMPR_ALB_TRA )
      imprAlbar(indice);
    if (indice==IMPR_HOJA_TRA || indice == IMPR_ALB_TRA)
      imprHojaTraza();
    if (indice==IMPR_ETIQUETAS)
      imprEtiq();
    if (indice==IMPR_PALETS)
      imprPalets();
    this.setEnabled(true);
    mensaje("");
  }

  void imprEtiq()
  {
    try
    {
//     if (nav.pulsado!=navegador.NINGUNO)
//     {
//         msgBox("Solo se pueden imprimir las etiquetas en modo consulta");
//         return;
//     }
     int etiCodi=eti_codiE.getValorInt();
     if (etiCodi < 0)
     {
         etiCodi=etiqueta.getEtiquetaCliente(dtCon1,emp_codiE.getValorInt(),
              cli_codiE.getValorInt());
     }
     etiq.setTipoEtiq(dtStat, EU.em_cod,
                       etiCodi);
      if (dtCons.getNOREG())
        return;
      s = "SELECT * FROM v_albvenpar " +
          " WHERE emp_codi = " + emp_codiE.getValorInt() +
          " AND avc_ano = " + avc_anoE.getValorInt() +
          " and avc_nume = " + avc_numeE.getValorInt() +
          " and avc_serie = '" + avc_seriE.getText() + "'";
      if (!dtCon1.select(s))
      {
        mensajeErr("No encontradas lineas de Partidas en este albaran");
        return;
      }
      String codBarras, proNomb;
      do
      {
        proNomb = pro_codiE.getNombArt(dtCon1.getString("pro_codi"));
        int avpNumpar=dtCon1.getInt("avp_numpar");
        utdesp.busDatInd(dtCon1.getString("avp_serlot"),
                         dtCon1.getInt("pro_codi"),
                         dtCon1.getInt("avp_emplot"),
                         dtCon1.getInt("avp_ejelot"),
                         avpNumpar,
                         dtCon1.getInt("avp_numind"),
                         avc_almoriE.getValorInt(),
                         dtBloq, dtStat, EU);
        
        String lote=   (avpNumpar > 9999 ? Formatear.format(avpNumpar, "99999") :
        Formatear.format(avpNumpar, "9999"));
        codBarras = dtCon1.getString("avp_ejelot").substring(2) +
            Formatear.format(dtCon1.getString("avp_emplot"), avpNumpar > 9999?"9":"99") +
            dtCon1.getString("avp_serlot") +
            lote+
            Formatear.format(dtCon1.getString("pro_codi"), "99999") +
            Formatear.format(dtCon1.getString("avp_numind"), "999") +
            Formatear.format(dtCon1.getString("avp_canti"), "999.99");
        etiq.iniciar(codBarras,
                     Formatear.format(dtCon1.getString("avp_ejelot"), "9999") +
                     "/" +
                     Formatear.format(dtCon1.getString("avp_emplot"), "99") +
                     "/" +
                     dtCon1.getString("avp_serlot") + "/" +
                     lote +
                     "/" +
                     Formatear.format(dtCon1.getString("avp_numind"), "999"),
                     dtCon1.getString("pro_codi"), proNomb,
                     utdesp.nacidoE, utdesp.cebadoE, utdesp.despiezadoE,
                     utdesp.ntrazaE, dtCon1.getDouble("avp_canti") + " Kg",
                     utdesp.conservarE, utdesp.sacrificadoE,
                     utdesp.fecrepL + utdesp.fecrecepE,
                     "F.Cad",null,
                     utdesp.feccadE,utdesp.fecSacrE);
        etiq.setFechaCongelado(utildesp.getFechaCongelado(dtCon1.getInt("pro_codi"), utdesp.getFechaProduccion(), dtStat));
        etiq.listarDefec();
      } while (dtCon1.next());

    }
    catch (Throwable k)
    {
      Error("Error al imprimir etiquetas", k);
    }
    mensajeErr("Etiqueta ... Listada");
  }
  
  private String getSqlListaAlb()
  {
      return "SELECT c.emp_codi as avc_empcod, c.*,cl.*" +
          " FROM v_albavec as c,clientes cl WHERE c.avc_ano =" + avc_anoE.getValorInt() +
          " and c.emp_codi = " + emp_codiE.getValorInt() +
          " and c.avc_serie = '" + avc_seriE.getText() + "'" +
          " and c.avc_nume = " + avc_numeE.getValorInt() +
          " and c.cli_codi = cl.cli_codi ";
  }
  void imprPalets()
  {
    try
    {
      mensaje("Imprimiendo Hojas de Palets ...");
            
      sqlAlb =   "SELECT c.*, cl.cli_nomb,cli_nomen,cli_diree,cli_poble,cli_codpoe "+
          " FROM v_albventa as c,clientes cl "
          + " WHERE c.avc_ano =" + avc_anoE.getValorInt() +
          " and c.emp_codi = " + emp_codiE.getValorInt() +
          " and c.avc_serie = '" + avc_seriE.getText() + "'" +
          " and c.avc_nume = " + avc_numeE.getValorInt() +
          " and c.cli_codi = cl.cli_codi "+
          " and avl_numpal!=0"+
          " order by avl_numpal,pro_codi";
           
      if (liAlb == null)
        liAlb = new lialbven(dtStat, EU);
      liAlb.imprEtiqPalets(sqlAlb,liAlb,dtCon1,EU);
          
      mensajeErr("Hojas de Palets  ... Impresa");
      mensaje("");
    }
    catch (Exception k)
    {
      Error("Error al Imprimir Hoja de Ruta", k);
    }
    
  }
  void imprHojaTraza()
  {
    try
    {
      mensaje("Imprimiendo HOJA DE Trazabilidad ...");
      if (liTra == null)
      {
        liTra = new listraza(new DatosTabla(ct), dtStat, dtCon1,EU,(ventana) this);
//        liTra.setPreview(true);
      }
      liTra.setRepiteIndiv(repiteIndE.getValorInt());
      liTra.setDatosAlbaran(avc_numeE.getValorInt(), emp_codiE.getValorInt(),
                  avc_anoE.getValorInt(), avc_seriE.getText());
      int retCargaDatos=liTra.cargaDatosTraz();
      if (retCargaDatos<0)
      {
          mensajeErr("Error al generar la Hoja Trazabilidad ");
          mensaje("");
          return;
      }
      switch (opDispSalida.getValor())
      {
          case DSAL_FAX:             
            this.setEnabled(false);
            ifFax.setVisible(true);
            ifFax.setSelected(true);
            String numfax=cli_codiE.getLikeCliente().getString("cli_fax",true);
            ifFax.setLialbven(liAlb);
            ifFax.setDatosDoc("A",sqlAlb, opValora.isSelected());
            ifFax.setCliCodi(cli_codiE.getText());
            ifFax.setNumFax(numfax);  
            break;
           case DSAL_EMAIL:
            this.setEnabled(false);
            ifMail.setVisible(true);
            ifMail.setSelected(true);            
            ifMail.setHojaTraz(liTra);
            ifMail.setAsunto("Hoja Trazabilidad de Albaran "+avc_anoE.getValorInt()+avc_seriE.getText()+ avc_numeE.getValorInt()+"  de fecha: "+avc_fecalbE.getText());
            ifMail.setText("Estimado cliente,\n\nAdjunto le enviamos la hoja de trazabilidad del albaran "+avc_anoE.getValorInt()+avc_seriE.getText()+
                avc_numeE.getValorInt()+
                "  de fecha: "+avc_fecalbE.getText()+
                "\n\nAtentamente\n\n"+emp_codiE.getEmpNomb());
            ifMail.setDatosDoc("T",sqlAlb, opValora.isSelected());
            ifMail.setCliCodi(cli_codiE.getText());

             break;
           default:
             
             liTra.setToEmail(null);
             if (retCargaDatos==0)
                liTra.imprimir();
      }
         
      mensajeErr("Hoja Trazabilidad  ... generada");
      mensaje("");
    }
    catch (Exception k)
    {
      Error("Error al Imprimir Hoja de Ruta", k);
    }
  }
  /**
   * Imprimir albaran
   */
  void imprAlbar(char indice)
  {
//    int nAlbImp = 0;
    try
    {
//       int empCodi,int avcAno,String avcSerie,int avcNume,DatosTabla dt
      if (checkAlbaran(emp_codiE.getValorInt(),avc_anoE.getValorInt(),avc_seriE.getText(), avc_numeE.getValorInt(),dtCon1)<0)
      {
          msgBox("IMPOSIBLE IMPRIMIR. Albaran con problemas integridad. Revise lineas contra individuos");
          enviaMailError(s);
          return;
      }
      if (isBloqueado(dtStat, "v_albavec",
                      avc_anoE.getValorInt() + "|" + emp_codiE.getValorInt() +
                      "|" + avc_seriE.getText() + "|" + avc_numeE.getValorInt(),true,false))
      {
        msgBox("NO se pudo listar albaran\n"+msgBloqueo);
        verDatos(dtCons);
        return;
      }
      mensaje("Imprimiendo albaran ...");

      if (liAlb == null)
        liAlb = new lialbven(dtStat, EU);
   
      setAlbaranImpreso(1);      

//      if (swEntdepos)
//        sqlAlb = "SELECT c.emp_codi as avc_empcod, c.*,cl.*" +
//          " FROM albvenserc as c ,clientes cl WHERE c.avs_nume =" + avsNume +
//          " and c.cli_codi = cl.cli_codi ";
//      else
      sqlAlb = getSqlListaAlb();
      if (opDispSalida.getValor().equals(DSAL_IMPRE) )
      { // Albaran grafico o Hoja trazabilidad + Albaran. Solo imprime y sale.
        liAlb.envAlbarFax(ct.getConnection(), dtStat, sqlAlb, EU,
                  opValora.isSelected(),null,
                  null,true,NUMCOPIAS_ALBGRAF,avsNume);
        return;
      }
      switch (opDispSalida.getValor())
      {
          case DSAL_FAX:             
            this.setEnabled(false);
            ifFax.setVisible(true);
            ifFax.setSelected(true);
            String numfax=cli_codiE.getLikeCliente().getString("cli_fax",true);
            ifFax.setLialbven(liAlb);
            ifFax.setDatosDoc("A",sqlAlb, opValora.isSelected());
            ifFax.setCliCodi(cli_codiE.getText());
            ifFax.setNumFax(numfax);  
            break;
           case DSAL_EMAIL:
            this.setEnabled(false);
            ifMail.setVisible(true);
            ifMail.setSelected(true);            
            ifMail.setLialbven(liAlb);
            ifMail.setAsunto("Albaran "+avc_anoE.getValorInt()+avc_seriE.getText()+ avc_numeE.getValorInt()+"  de fecha: "+avc_fecalbE.getText());
            ifMail.setText("Estimado cliente,\n\nAdjunto le enviamos el albaran "+avc_anoE.getValorInt()+avc_seriE.getText()+
                avc_numeE.getValorInt()+
                "  de fecha: "+avc_fecalbE.getText()+
                "\n\nAtentamente\n\n"+emp_codiE.getEmpNomb());
            ifMail.setDatosDoc("A",sqlAlb, opValora.isSelected());
            ifMail.setCliCodi(cli_codiE.getText());

             break;
           default:
             liAlb.impAlbaran(emp_codiE.getValorInt(),dtStat, dtCon1, sqlAlb, EU, opValora.isSelected());
      }
      mensajeErr("Albaran ... Impreso");

    }
    catch (Exception k)
    {
      Error("Error al imprimir Albaran", k);

    }
  }
  /**
   * Ponea la fecha de mvto a la fecha de alta de la linea.
   * @param nl  Numero de Linea del grid (0 todas)
   */
  private void MFechaCabActionPerformed(int nl) {                                          
        try
        {
            if (jt.isVacio())
                return;
            if (nav.isEdicion())
                return;
            if (opAgru.isSelected())
            {
                msgBox("Desagrupe las lineas para restaurar fecha mvto");
                return;
            }
            if (nl>=0)
                changeFecMvto(nl);
            else
            {
                for (int n=0;n<jt.getRowCount();n++)
                {
                  changeFecMvto(n);   
                }
            }
            dtAdd.commit();
            verDatos(dtCons);
            msgBox("Fecha mvto, puesta a la misma que la fecha de alta");
        } catch (ParseException | SQLException ex)
        {
            Error("Error al actualizar fecha de mvto",ex);
        }

    }  
    private void   changeFecMvto(int nl) throws SQLException,ParseException
    {
        s="UPDATE mvtosalm set mvt_time='"+
                Formatear.getFechaDB(jt.getValString(nl,JT_FECMVT),"dd-MM-yy")+
                "' where mvt_ejedoc="+avc_anoE.getValorInt()+
                " and mvt_numdoc="+avc_numeE.getValorInt()+
                " and mvt_serdoc='"+avc_seriE.getText()+"'"+
                " and mvt_tipdoc='V'"+
                " and mvt_lindoc="+jt.getValorInt(nl,JT_NULIAL);
        dtAdd.executeUpdate(s);     
    }            
            

  /**
   * Establece el estado de albaran impreso
   * @param albImpreso
   * @throws SQLException 
   */
  void setAlbaranImpreso(int albImpreso) throws SQLException
  {
      s= "update  v_albavec set avc_impres = "+albImpreso +" WHERE avc_ano =" + avc_anoE.getValorInt() +
               " and emp_codi = " + emp_codiE.getValorInt() +
               " and avc_serie = '" + avc_seriE.getText() + "'" +
               " and avc_nume = " + avc_numeE.getValorInt();
      dtAdd.executeUpdate(s);
      dtAdd.commit();
      verIconoListado(albImpreso);
  }
  void consPrecios()
  {
    if (ayVePr == null)
    {
      ayVePr = new ayuVenPro(true)
      {
        @Override
        public void matar()
        {
          ej_consPro();
        }
      };
      ayVePr.setIconifiable(false);
      vl.add(ayVePr);
      ayVePr.setLocation(0, 0);
    }
    try
    {

      new miThread("")
      {
        @Override
        public void run()
        {
          consultaPrecios();
        }
      };

    }
    catch (Exception ex)
    {
      fatalError("Error al Cargar datos de productos", ex);
    }
  }

  void consultaPrecios()
  {
    try
    {
      this.setEnabled(false);
      Thread.currentThread().setPriority(Thread.MAX_PRIORITY - 1);
      ayVePr.setVisible(true);
      String lastMensaje=statusBar.getText();
      mensaje("Buscando datos.. de Ultimas ventas");
      ayVePr.cargaDatos(ct, cli_codiE.getText(), cli_codiE.getTextNomb(),
                        jt.getValString(1), jt.getValString(2), EU);
      mensaje(lastMensaje);
      mensajeErr("Datos de Ultimas ventas... encontrados");
    }
    catch (Exception ex)
    {
      fatalError("Error al Cargar datos de productos", ex);
    }

  }

  void ej_consPro()
  {
    if (ayVePr.precio != 0 && avl_prvenE.isEditable())
    {
      jt.setValor("" + ayVePr.precio, 5);
      avl_prvenE.setValorDec(ayVePr.precio);
    }
    ayVePr.setVisible(false);
    this.setEnabled(true);
    this.toFront();
    try
    {
      this.setSelected(true);
    }
    catch (Exception k)
    {}
    SwingUtilities.invokeLater(new Thread()
    {
      @Override
      public void run()
      {
        jt.requestFocusSelected();
      }
    });
  }
  @Override
  public void matar(boolean cerrarConexion)
  {
    if (muerto)
      return;
//    cli_codiE.close();
    if (dgAlb != null)
    {
      dgAlb.setVisible(false);
      dgAlb.dispose();
    }

    if (ayVePr != null)
    {
      ayVePr.setVisible(false);
      ayVePr.dispose();
    }
    if (ayuLot != null)
    {
      ayuLot.setVisible(false);
      ayuLot.dispose();
    }
    if (copeve!=null)
    {
      copeve.matar(false);
      copeve.setVisible(false);
      copeve.dispose();
    }

    if (ifFax!=null)
    {
      ifFax.setVisible(false);
      ifFax.dispose();
    }
    if (ifMail!=null)
    {
      ifMail.setVisible(false);
      ifMail.dispose();
    }
    botonBascula.dispose();
//    if (despAlbar!=null)
//    {
//      despAlbar.setVisible(false);
//      despAlbar.dispose();
//    }
    if (P_CHECKPED)
        temporizador.stop();
    try
    {
      dtHist.close();
      dtPedi.close();
      resetBloqueo(dtAdd);
    } catch (SQLException | ParseException k) {}
    super.matar(cerrarConexion);
  }

  void cambiaEmp()
  {
    try
    {
      int avcNume = avc_numeE.getValorInt();
      avc_numeE.setValorDec(getNumAlb());
      cambiaEmp(avcNume, avc_numeE.getValorInt());
      if (swProcesaEdit)
              ej_edit1();
    }
    catch (SQLException | ParseException k)
    {
      Error("Error al Cambiar Datos Albaran ", k);
    }
  }

  void cambiaEmp(int avcNumeAnt, int avcNume) throws SQLException
  {
    stUp.getConnection().getAutoCommit();
    stUp.getConnection().setAutoCommit(false);
   
   
    s = "update v_albavel set avc_ano = " + avc_anoE.getValorInt() +
        ", emp_codi = " + emp_codiE.getValorInt() +
        ", avc_serie = '" + avc_seriE.getText() + "'" +
        ",avc_nume = " + avcNume +
        "  WHERE avc_ano =" + avc_anoE.getTextAnt() +
        " and emp_codi = " + emp_codiE.getTextAnt() +
        " and avc_serie = '" + avc_seriE.getTextAnt() + "'" +
        " and avc_nume = " + avcNumeAnt;
    stUp.executeUpdate(s);
    s = "update V_albavec set avc_ano = " + avc_anoE.getValorInt() +
        ", emp_codi = " + emp_codiE.getValorInt() +
        ", avc_serie = '" + avc_seriE.getText() + "'" +
        ",avc_nume = " + avcNume +
        "  WHERE avc_ano =" + avc_anoE.getTextAnt() +
        " and emp_codi = " + emp_codiE.getTextAnt() +
        " and avc_serie = '" + avc_seriE.getTextAnt() + "'" +
        " and avc_nume = " + avcNumeAnt;
    stUp.executeUpdate(s);
     s = "update V_albvenpar set avc_ano = " + avc_anoE.getValorInt() +
        ", emp_codi = " + emp_codiE.getValorInt() +
        ", avc_serie = '" + avc_seriE.getText() + "'" +
        ",avc_nume = " + avcNume +
        "  WHERE avc_ano =" + avc_anoE.getTextAnt() +
        " and emp_codi = " + emp_codiE.getTextAnt() +
        " and avc_serie = '" + avc_seriE.getTextAnt() + "'" +
        " and avc_nume = " + avcNumeAnt;
    stUp.executeUpdate(s);
    if (jf != null)
    {
      jf.ht.clear();
      jf.ht.put("%s", s);
      jf.guardaMens("V5", jf.ht);
    }
    ctUp.commit();
    avc_numeE.resetCambio();
    avc_anoE.resetCambio();
    emp_codiE.resetCambio();
    avc_seriE.resetCambio();

  }

  /**
   * Cambia un Numero de Albaran por otro.
   *
   * @param numAlb int Numero Albaran a Poner
   * @param numAlbAnt int Numero Alb. Anterior
   * @throws Exception Error de BD
   * @return boolean si el Numero Albaran NO es correcto.
   */
  boolean cambiaNumALb(int numAlb, int numAlbAnt)
  {
    try
    {
      int nAlb = getNumAlb(false);
      if (numAlb >= nAlb)
      {
        mensajeErr("Numero de Albaran NO puede ser igual o superior al ULTIMO");
        return false;
      }

      if (getAlbaranCab(dtStat,avc_anoE.getValorInt(),
                        emp_codiE.getValorInt(),
                        avc_seriE.getText(),
                        numAlb))
      {
        mensajeErr("Numero de Albaran YA Existe");
        return false;
      }
      cambiaEmp(numAlbAnt, numAlb);
      mensajeErr("Numero de Albaran .... CAMBIADO");
    }
    catch (SQLException | ParseException k)
    {
      Error("Error al Insertar Numero de Albaran", k);
    }
    return true;
  }

  public static boolean getAlbaranCab(DatosTabla dt, int empCodi,int avcAno, String avcSerie,int avcNume) throws SQLException
  {
    String s = "select * from v_albavec  WHERE avc_ano =" + avcAno +
          " and emp_codi = " +empCodi+
          " and avc_serie = '" + avcSerie + "'" +
          " and avc_nume = " + avcNume;
    return dt.select(s);
  }
  /**
   * Busca albaranes con una antiguedad maxima del parametro mandado
   * @param dias 
     * @param dt 
     * @param dt1 
   * @return Hashtable con los albaranes con incidencia y una descripcion de la incidencia
     * @throws java.sql.SQLException
   */
  public static Hashtable checkAlbaran(int dias,DatosTabla dt,DatosTabla dt1) throws SQLException
  {
      String s="select emp_codi,avc_ano,avc_serie,avc_nume from v_albavec where "
              + " avc_fecalb >= current_date -"+dias;
             
      if (! dt.select(s))
          return null;
      Hashtable<String, String> ht=new Hashtable();
      int res;
      do
      {
          res=checkAlbaran(dt.getInt("emp_codi"),dt.getInt("avc_ano"),dt.getString("avc_serie"),dt.getInt("avc_nume"),dt1);
          if (res<0)
          {
              if (res==-1)
                  ht.put(dt.getInt("emp_codi")+"-"+dt.getInt("avc_ano")+dt.getString("avc_serie")+"-"+dt.getInt("avc_nume"), "DIF. KILOS");
              if (res==-2)
                  ht.put(dt.getInt("emp_codi")+"-"+dt.getInt("avc_ano")+dt.getString("avc_serie")+"-"+dt.getInt("avc_nume"), "DIF. UNID");
          }
      } while (dt.next());
      if (ht.isEmpty())
          return null;
      return ht;
  }
  void actAcumLinAlb(int empCodi,int avcAno,String avcSerie,int avcNume) throws SQLException
  {
       String s="select avl_numlin, sum(avl_canti) as canti, sum(avl_unid) as unid "+
          " from v_albavel  WHERE avc_ano =" + avcAno +
          " and emp_codi = " +empCodi+
          " and avc_serie = '" + avcSerie + "'" +
          " and avc_nume = " + avcNume+
          " group by avl_numlin";
       if (!dtCon1.select(s))
           return;
       double canti;
       int unid;
       int nl;
       do
       {
          canti=Formatear.redondea(dtCon1.getDouble("canti"),2);
          unid=dtCon1.getInt("unid");
          nl=dtCon1.getInt("avl_numlin");
          s="select sum(avp_canti ) as canti, sum(avp_numuni) as unid from v_albvenpar  WHERE avc_ano =" + avcAno +
            " and emp_codi = " +empCodi+
            " and avc_serie = '" + avcSerie + "'" +
            " and avc_nume = " + avcNume+
             " and avl_numlin="+nl;
          dtStat.select(s);
          if (dtStat.getObject("canti")==null)
              continue; // No hay  lineas en desglose. Lo ignoro.
          if (canti!=dtStat.getDouble("canti") || unid!=dtStat.getInt("unid"))
          { // La actualizo
              s="UPDATE v_albavel SET avl_canti="+dtStat.getDouble("canti")+
                  ",avl_unid = "+dtStat.getInt("unid")+
                  " WHERE  avc_ano =" + avcAno +
                  " and emp_codi = " +empCodi+
                  " and avc_serie = '" + avcSerie + "'" +
                  " and avc_nume = " + avcNume+
                  " and avl_numlin="+nl;
              dtAdd.executeUpdate(s);
//              enviaMailError("(actAcumLinAlb) Problemas integridad Alb.Venta: "+empCodi+
//                  "-"+avcAno+avcSerie+avcNume+"\n"+" NL: "+nl+"Kilos: "+dtStat.getDouble("canti")+"("+
//                  canti+") Unid: "+dtStat.getInt("unid")+"("+unid+")");
          }
       } while (dtCon1.next());
       dtAdd.commit();
  }
  /**
   * Comprueba integridad de un albaran sobre productos vendibles.
   * @param empCodi Empresa
   * @param avcAno Ejercicio
   * @param avcSerie Serie
   * @param avcNume año
   * @param dt DatosTabla sobre el q realizar las select
   * @return 0 -> TODO BIEN
   *         1 -> No encontradas lineas albaran
   *         2 -> No encontrados lineas en partidas
   *        -1 -> Diferencias en kilos
   *        -2 -> Diferencias en Unidades
   * @throws SQLException 
   */
  public static int checkAlbaran(int empCodi,int avcAno,String avcSerie,int avcNume,DatosTabla dt) throws SQLException
  {
     String s="select sum(avl_canti) as canti, sum(avl_unid) as unid from v_albavel as l, v_articulo a"         
         + " WHERE avc_ano =" + avcAno +
          " and l.emp_codi = " +empCodi+
          " and avc_serie = '" + avcSerie + "'" +
          " and avc_nume = " + avcNume+
          " and l.pro_codi = a.pro_codi "+
          " and a.pro_tiplot = 'V'";
         
     
     dt.select(s);
     if (dt.getObject("canti")==null)
         return 1; // Puede que sea un albaran sin lineas. 
     double canti=Formatear.redondea(dt.getDouble("canti"),2);
     int unid=dt.getInt("unid");
     s="select sum(avp_canti ) as canti, sum(avp_numuni) as unid from v_albvenpar  WHERE avc_ano =" + avcAno +
          " and emp_codi = " +empCodi+
          " and avc_serie = '" + avcSerie + "'" +
          " and avc_nume = " + avcNume;  
     dt.select(s);
     if (dt.getObject("canti")==null)
         return 2; // Puede que todas las lineas sean de comentario
     
     if (canti!= Formatear.redondea(dt.getDouble("canti"),2))
         return -1;
     if (unid !=dt.getInt("unid"))
        return -2;
     return 0;
  }
//  void enviarFax()
//  {
//    try
//    {
//      if (cli_faxE.isNull(true) )
//      {
//        msgBox("Introduzca No de Fax del Cliente");
//        return;
//      }
//      liAlb.envAlbarFax(ct.getConnection(), dtStat, sqlAlb, EU,
//                        opValora.isSelected(),cli_faxE.getText(),
//                        respuestE.getText(),opCopia.isSelected());
//      cancelFax();
//      mensajeErr("Fax ..... Enviado");
//    }
//    catch (Exception k)
//    {
//      Error("Error al enviar Albaran por Fax por usuario: "+EU.usuario, k);
//    }
//  }

 

  private void conf_jtLinPed(Cgrid jt) throws Exception
  {
    pvc_fecentE.setEnabled(false);
    usu_nombE.setEnabled(false);
    pvc_fecpedE.setEnabled(false);
    pvc_horpedE.setEnabled(false);
    pvc_comenE.setEnabled(false);
    nlE.setEnabled(false);
    cantE.setEnabled(false);
    ArrayList v = new ArrayList();
    v.add("A/P"); // 0
    v.add("Prod."); // 1
    v.add("Desc. Prod."); // 2
    v.add("Proveed"); // 3
    v.add("Fec.Cad"); // 4
    v.add("Cant"); // 5
    v.add("Prec"); // 6
    v.add("Comentario"); // 7 Comentario
    v.add("CP"); // 8 Confirmado Precio ?
    v.add("NL."); // 9
    v.add("Prv"); // 10
    jt.setCabecera(v);
    jt.setMaximumSize(new Dimension(477, 250));
    jt.setMinimumSize(new Dimension(31, 250));
    jt.setPreferredSize(new Dimension(477, 250));
    jt.setPuntoDeScroll(50);
    jt.setAnchoColumna(new int[]
                       {30,60, 160, 150, 90, 70, 60, 150,30,30,50});
    jt.setAlinearColumna(new int[]
                         {1, 2, 0, 0, 1, 2, 2,0,1,2,2});
    jt.setFormatoColumna(5, "--,---9");
    jt.setFormatoColumna(6, "---9.99");

    jt.setFormatoColumna(8, "BSN");
  }
  void verDatPedido() throws Exception
  {
    verDatPedido(false);
  }
  /**
   * Muestra los datos del pedido.
   * @param busAlbaran boolean true Busca datos sobre un pedido
   * @throws Exception En caso de Error en DB
   */
  void verDatPedido(boolean busAlbaran) throws Exception
  {
    if (busAlbaran)
      s = "SELECT * FROM v_pedven " +
      " WHERE emp_codi =  " + emp_codiE.getValorInt() +
      " AND avc_ano = " + avc_anoE.getValorInt() +
      " and avc_nume = " + avc_numeE.getValorInt() +
      " and avc_serie = '"+avc_seriE.getText()+"'"+
      " order by pvl_numlin ";
    else
      s = "SELECT * FROM v_pedven " +
        " WHERE emp_codi =  " + emp_codiE.getValorInt() +
        " AND eje_nume = " + pvc_anoE.getValorInt() +
        " and pvc_nume = " + pvc_numeE.getValorInt() +
        " order by pvl_numlin ";
    jtLinPed.removeAllDatos();
    pvc_fecentE.resetTexto();
    usu_nompedE.resetTexto();
    pvc_fecpedE.resetTexto();
    pvc_horpedE.resetTexto();
    pvc_comenE.resetTexto();
    nlE.resetTexto();
    cantE.resetTexto();
    pvc_anoE.resetTexto();
    pvc_numeE.resetTexto();

    if (!dtCon1.select(s))
    {
      if (! busAlbaran)
        mensajeErr("NO ENCONTRADOS DATOS PARA ESTE PEDIDO");
      return;
    }
    pvc_anoE.setValorInt(dtCon1.getInt("eje_nume"));
    pvc_numeE.setValorInt(dtCon1.getInt("pvc_nume"));
    pvc_fecentE.setText(dtCon1.getFecha("pvc_fecent","dd-MM-yyyy"));
    usu_nompedE.setText(dtCon1.getString("usu_nomb"));
    pvc_fecpedE.setText(dtCon1.getFecha("pvc_fecped"));
    pvc_horpedE.setText(dtCon1.getFecha("pvc_fecped", "hh.mm"));
    pvc_comenE.setText(dtCon1.getString("pvc_comen"));

    do
    {
      ArrayList v = new ArrayList();
      v.add("P");
      v.add(dtCon1.getString("pro_codi"));
      v.add(pro_codiE.getNombArtCli(dtCon1.getInt("pro_codi"),
                                           cli_codiE.getValorInt(), EU.em_cod, dtStat));
      v.add(prv_codiE.getNombPrv(dtCon1.getString("prv_codi"), dtStat));
      v.add(dtCon1.getFecha("pvl_feccad","dd-MM-yy"));
      v.add(dtCon1.getString("pvl_canti")+" "+dtCon1.getString("pvl_tipo") );
      if (verPrecios)
        v.add(dtCon1.getString("pvl_precio"));
      else
        v.add("");
      v.add(dtCon1.getString("pvl_comen"));
      v.add((dtCon1.getInt("pvl_precon") != 0));
      v.add(dtCon1.getString("pvl_numlin"));
      v.add(dtCon1.getString("prv_codi"));
      jtLinPed.addLinea(v);
    }    while (dtCon1.next());
    actAcumJT();
    actAcumPed(0);
  }

  void actAcumJT()
  {
    int nRows = jtLinPed.getRowCount(), nl = 0, nu = 0;

    for (int n = 0; n < nRows; n++)
    {
      if (jtLinPed.getValorInt(n, 1) == 0 || !jtLinPed.getValString(n,0).equals("P"))
        continue;
      nl++;
      nu += jtLinPed.getValorDec(n, 5);
    }
    nlE.setValorInt(nl);
    cantE.setValorDec(nu);
  }

  void BmvReg_actionPerformed()
  {
    try {
      // Guardo lineas de despiece
     int nl=cambiaLinDes(jtDes.getSelectedRow());
     if (nl>=0)
     {
         jtDes.requestFocusLater(jtDes.getSelectedRow(), nl);
         return;
     }
     jtDes.salirGrid();
     guardaLinDes(jt.getSelectedRow());
     this.setEnabled(false);
     if (ifRegAlm==null)
     {
      ifRegAlm = new ifregalm()
       {
         @Override
         public void matar(boolean cerrarConexion)
         {
           cerrarRegAlm();
         }
       };
       vl.add(ifRegAlm);
       ifRegAlm.setLocation(this.getLocation().x, this.getLocation().y + 30);
       ifRegAlm.iniciar(dtStat,dtCon1,dtAdd,this);
     }
     else
       ifRegAlm.statusBar.setEnabled(true);
     ifRegAlm.setVisible(true);
     ifRegAlm.getPanelReg().setCampos(avc_fecalbE.getDate(), pro_codicE.getValorInt(),
                                        EU.em_cod,
                                      avp_ejelotE.getValorInt(), avp_serlotE.getText(),avp_numparE.getValorInt(),
                                      avp_numindE.getValorInt(),avp_numuniE.getValorInt()*-1,
                                      avp_cantiE.getValorDec()*-1,
                                      avc_almoriE.getValorInt(),tirCodi,cli_codiE.getValorInt(),
                                      "A.V:"+emp_codiE.getValorInt()+"-"+avc_anoE.getValorInt()+"-"+
                                      avc_seriE.getText()+"/"+avc_numeE.getValorInt(),0,null,0,sbe_codiE.getValorInt(),1,0);
    } catch (Exception k)
    {
      Error("ERROR al iniciar ventana de Regularización",k);
    }
  }
  void  cerrarRegAlm()
  {
      if (! ifRegAlm.isVisible())
       return;
    ifRegAlm.setVisible(false);
    this.setEnabled(true);

    jtDes.requestFocusLater();
  }

  private void confGridCab() throws IllegalArgumentException, ClassNotFoundException 
  {
    
    JT_NUMPALE=P_MODPRECIO || P_PONPRECIO ? 8: 6;
    JT_FECMVT=JT_NUMPALE - 1;
    JT_CODENV=JT_NUMPALE+1;
    avl_prvenE.setFormato(avl_prvenE.getFormato()+FORMDECPRECIO);
    impLinE.setFormato(impLinE.getFormato()+FORMDEC);
    jt = new CGridEditable(P_MODPRECIO || P_PONPRECIO ? 9 : 7)
    {
      @Override
      public boolean deleteLinea(int row, int col)
      {
        if (nav.pulsado != navegador.EDIT && nav.pulsado != navegador.ADDNEW)
          return false;
        try
        {
          if (nLiMaxEdit>0 && jt.getValorInt(row,JT_NULIAL) > 0 && jt.getValorInt(row,JT_NULIAL)<= nLiMaxEdit )
          {
              msgBox("Albaran de deposito y con genero entregado. Imposible borra lineas");
              return false;
          }
          borraLinea(row);
          ctUp.commit();
        }
        catch (Exception k)
        {
          Error("Error al Borrar Linea", k);
        }
        return true;
      }

      @Override
      public void afterDeleteLinea()
      {
        actAcumLin();
      }

      @Override
      public void cambiaColumna(int col, int colNueva, int row)
      {
        try
        {
          if (nav.pulsado != navegador.EDIT && nav.pulsado != navegador.ADDNEW && nav.pulsado != navegador.QUERY)
          {
            if (col == 5 && P_MODPRECIO)
              if ( (fvc_anoE.getValorInt() == 0 || P_ADMIN) &&
                  (fvc_numeE.getValorInt() == 0 || P_ADMIN) && !traspCont &&
                  !traspReci && ! isLock)
              {
                actPrecioAlb(row);
                antPrecio = avl_prvenE.getValorDec();
              }
              else
                avl_prvenE.setValorDec(jt.getValorDec(col));
            if (col==JT_NUMPALE)
                actNumPale(row);
            return;
          }
          if (col == JT_PROCODI)
          {
            prLiTar = 0;
            if (!pro_nombE.isNull() && !pro_codiE.hasCambio() || pro_codiE.getValorInt() == 0)
              return;
            if (pro_codiE.hasCambio())
              pro_codiE.getNombArt(pro_codiE.getText(), EU.em_cod);
            if (pro_codiE.getTipoLote() == 'V' && pro_codiE.getValorInt() > 0)
            {
              if (avl_unidE.isEnabled())
              {
                avl_unidE.setEnabled(false);
                avl_cantiE.setEditable(false);
              }
            }
            else
            {
              if (!avl_unidE.isEnabled())
              {
                avl_unidE.setEnabled(true);
                avl_cantiE.setEditable(true);
              }
            }
            pro_codiE.resetCambio();
            String proNomb = pro_codiE.getNombArtCli(pro_codiE.getValorInt(),
                cli_codiE.getValorInt());
            jt.setValor(proNomb, row, 2);
            if (pro_nombE.isEnabled())
              pro_nombE.setText(proNomb);
            if (!P_MODPRECIO)
              return;
            prLiTar = MantTarifa.getPrecTar(dtStat, pro_codiE.getValorInt(), tar_codiE.getValorInt(), avc_fecalbE.getText());
            if (prLiTar != 0)
            {
              jt.setValor("" + prLiTar, 6);
              if (jt.getValorDec(5) == 0 && avc_revpreE.getValorInt()==0)
                jt.setValor("" + prLiTar, 5);
            }
            else
              jt.setValor("0", 6);
          }
        }
        catch (SQLException k)
        {
          Error("Error al buscar Nombre Articulo", k);
        }
      }

        @Override
      public void afterCambiaLinea()
      {
        antPrecio = avl_prvenE.getValorDec();
        avl_prvenE.resetCambio();
        avl_numpalE.resetCambio();
        if ((nav.pulsado != navegador.EDIT && nav.pulsado != navegador.ADDNEW) || isLock)
          return;
        pro_codiE.resetCambio();
        if (jt.getValorInt(0) > 0)
        {
          pro_codiE.setEditable(false);
//          pro_nombE.setEditable(false);
        }
        else
        {
          pro_codiE.setEditable(true);
          pro_nombE.setEditable(true);
        }
        try
        {
          pro_codiE.getNombArt(jt.getValString(1), EU.em_cod, 0,
                               dtStat);
          botonBascula.setPesoCajas(pro_codiE.getPesoCajas());
        }
        catch (SQLException k)
        {
          Error("Error al buscar caracteristicas del producto", k);
        }
        if (pro_codiE.getTipoLote() == 'V' && jt.getValorInt(1) >= 1)
        {
          avl_unidE.setEnabled(false);
          avl_cantiE.setEditable(false);
        }
        else
        {
          avl_unidE.setEnabled(true);
          avl_cantiE.setEditable(true);
        }
      }
     @Override
      public int cambiaLinea(int row, int col)
      {
        if (nav.pulsado != navegador.EDIT && nav.pulsado != navegador.ADDNEW)
          return -1;
        return cambiaLinAlb(row);
      }
       @Override
      public boolean insertaLinea(int row, int col)
      {
        return nav.pulsado == navegador.EDIT || nav.pulsado == navegador.ADDNEW;
      }
    };
    if (P_ADMIN)
    {
        jt.getPopMenu().add(MFechaCab);
        jt.getPopMenu().add(MAllFechaCab);
    }
    jt.setMaximumSize(new Dimension(538, 181));
    jt.setMinimumSize(new Dimension(538, 181));
    jt.setPreferredSize(new Dimension(538, 181));
//    jt.setBuscarVisible(false);
    ArrayList v = new ArrayList(); 
    v.add("NL");// 0
    v.add("Prod."); // 1
    v.add("Descripcion"); // 2
    v.add("Kilos"); // 3
    v.add("Unid"); // 4
    if (P_MODPRECIO || P_PONPRECIO)
    {
      v.add("Precio"); // 5
      v.add("Pr.Tar"); // 6
    }
    v.add("Fec.Mvto");
    v.add("Palet");
    jt.setCabecera(v);
    if (P_MODPRECIO || P_PONPRECIO)
    {
      jt.setAnchoColumna(new int[]
                         {30, 50, 200, 70, 40, 60, 60,100,50});
      jt.setAlinearColumna(new int[]
                           {2, 2, 0, 2, 2, 2, 2,0,2});
    }
    else
    {
      jt.setAnchoColumna(new int[]
                         {40, 50, 200, 70, 40,100,50});
      jt.setAlinearColumna(new int[]
                           {2, 2, 0, 2, 2,0,2});

    }
    
    ArrayList vc = new ArrayList();
    pro_nombE.setRequestFocusEnabled(false);
    avl_prvenE.setToolTipText("F3 Consulta Ultimos Precios");
    avl_numlinE.setEnabled(false);
    pro_codiE.setProNomb(null);
    pro_codicE.setProNomb(null);
    tar_preciE.setEnabled(false);
    avl_fecaltE.setEnabled(false);
  
    vc.add(avl_numlinE); // 0
    vc.add(pro_codiE.getTextField()); // 1
    vc.add(pro_nombE); // 2
    vc.add(avl_cantiE); // 3
    vc.add(avl_unidE); // 4
   
    if (P_MODPRECIO || P_PONPRECIO)
    {
      vc.add(avl_prvenE); // 5
      vc.add(tar_preciE); // 6
    }
    vc.add(avl_fecaltE);
    vc.add(avl_numpalE);
    jt.setCampos(vc);
    jt.setFormatoCampos();
    jt.setAjustarGrid(true);
    jt.setPonValoresInFocus(false);
  }
}

class checkFax extends Thread
{
  pdalbara padre;
  HylaFAXClient c= new HylaFAXClient();
  Vector list = new Vector();
  Statement stUp;
  DatosTabla dtStat;
  int p2=-1,p1;
  conexion ctUp;

  public checkFax(pdalbara papa)
  {
    padre=papa;
    this.start();
  }
  @Override
  public void run()
  {
    int nErr=0;
    try
    {
      if (SendFax.getServFax()==null)
        return;
      ctUp=new conexion(padre.EU.usuario,padre.EU.password,padre.EU.driverDB,padre.EU.addressDB);
      ctUp.setAutoCommit(true);
      stUp = ctUp.crearEstamento();
      dtStat = new DatosTabla(ctUp);
    } catch (SQLException | ClassNotFoundException | IllegalAccessException | InstantiationException k)
    {
      SystemOut.print(k);
      return;
    }
    while (! padre.muerto)
    {
      try
      {
//        c.setDebug(false); // enable debug messages
        c.open(SendFax.getServFax());
        if (c.user(padre.EU.usuario))
        {
          c.pass(padre.EU.password);
        }

        c.jobfmt("%5j\t%a\t%o\t%.25s");
        list.removeAllElements();
        list = c.getList("doneq");
        Enumeration lines = list.elements();

        String linea;
        String s;
        int job;
        String usuario, estado ;
        String msgerr ;
        while (lines.hasMoreElements())
        {
          linea = lines.nextElement().toString();
          p2 = -1;
          job = Integer.parseInt(getValor(linea, "\t"));
          estado = getValor(linea, "\t");
          usuario = getValor(linea, "\t");
          msgerr = getValor(linea, "\t");
          if (!usuario.equals(padre.EU.usuario))
            continue;
          s ="SELECT a.*,CL.cli_nomb FROM albvefax a,clientes cl "+
               " WHERE avf_jobid= " +   job +
              " and cl.cli_codi = a.cli_codi " +
              " and cl.emp_codi = " + padre.EU.em_cod +
              " and avf_contro = 'S'";
          if (!dtStat.select(s))
            continue;
          if (estado.equals(dtStat.getString("avf_estad")))
            continue; // No ha cambiado el estado
          if (estado.equals("F"))
          { // Avisar
            mensajes.mensajeAviso("El fax para: " + dtStat.getString("cli_nomb") +
                                  " con numero: " +
                                  dtStat.getString("avc_ano") + "-" +
                                  dtStat.getString("emp_codi") + "/" +
                                  dtStat.getString("avc_serie") +
                                  dtStat.getInt("avc_nume") +
                                  "\nNO ha podido ser enviado\n" +
                                  " Razon: " + msgerr);
          }
          s = "UPDATE albvefax set avf_estad = '" + estado +
               "',avf_msgerr='" +   msgerr + "'," +
              " avf_contro = 'N' " +
              " WHERE avf_jobid= " + job;
          stUp.executeUpdate(s);
          ctUp.commit();
        }
        c.quit();
//        c.finalize();
      }
      catch (IOException | ServerResponseException | NumberFormatException | SQLException k)
      {
        nErr++;
        if (nErr>9)
          return; // Demasiados Errores. Paso del Fax
      }
      try {
        Thread.sleep(300000); // 300 Segundos (5 minutos)
      } catch (Exception k)
      {
         SystemOut.print(k);
      }
    }
  }
  String getValor(String linea,String separador)
 {
   p1 = p2 + 1;
   p2 = linea.indexOf(separador, p1);
   if (p2==-1)
   {
     p2=p1;
     return linea.substring(p1).trim();
   }
   return linea.substring(p1, p2).trim();
 }
}

class albveThread extends Thread
{
  int row;
  public albveThread(int row)
  {
    this.row=row;
  }
}
/**
 * Clase con datos para tratatamiento residuos de albaranes de ventas.
 * @author jpuente
 */
class albvenres 
{
    int proCodi;
    double avrCanti;
    boolean swExiste=false;
    
    public albvenres(int proCodi, double avrCanti)
    {
        this.proCodi=proCodi;
        this.avrCanti=avrCanti;
    }
    
}