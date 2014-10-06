package gnu.chu.anjelica.compras;

import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.almacen.MvtosAlma;
import gnu.chu.anjelica.almacen.actStkPart;
import gnu.chu.anjelica.almacen.paregalm;
import gnu.chu.anjelica.despiece.utildesp;
import gnu.chu.anjelica.listados.etiqueta;
import gnu.chu.anjelica.pad.MantPaises;
import gnu.chu.anjelica.pad.pdejerci;
import gnu.chu.anjelica.ventas.pdalbara;
import gnu.chu.camposdb.*;
import gnu.chu.comm.BotonBascula;
import gnu.chu.controles.*;     
import gnu.chu.interfaces.PAD;
import gnu.chu.print.util;
import gnu.chu.sql.DatosTabla;
import gnu.chu.sql.vlike;
import gnu.chu.utilidades.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyVetoException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;     
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.*;
import net.sf.jasperreports.engine.*;

/**
 *
 * <p>Título: MantAlbCom</p>
 * <p>Descripción: Mantenimiento Albaranes de Compra</p>
 * Parametros: modPrecio Indica si se puede modificar los precios del albaran.
 *  admin: Modo Aministrador.
 *  AlbSinPed true/False Indica si se pueden cargar albaranes sin un pedido de compras
 * </p>
 * <p> Creado a partir pdalbaco2</p>
 *  <p>Copyright: Copyright (c) 2005-2014
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
 * <p>Empresa: MISL</p>
 * @author Chuchi P
 * @version 1.0
 */
public abstract class MantAlbCom extends ventanaPad   implements PAD, JRDataSource,MantAlbCom_Interface
{
  Cgrid jtHist=new Cgrid(4);
  CPanel Phist=new CPanel();
  private String tablaCab="desporig";
  private String tablaLin="desorilin";
  private String tablaInd="v_despfin";

  private int hisRowid=0;
  private String condHist=""; // Condiciones del historico
//  private int frtEjer,frtNume;
  final int JT_NLIN=0;
  final int JT_PROCOD=1;
  final int JT_CANIND=3;
  final int JT_KILALB=4;
  final int JT_PRCOM=5;
  final int JT_COMENT=13;
  final int JT_PORPAG=14;
  final int JT_DTOPP=15;
  /**
   * Numero Individuo 0
   */
  final int JTD_NUMIND=0;
  /**
   * Kilos (cantidad) 1
   */
  final int JTD_CANTI=1;
  /** 
   * Numero Crotal 2
   */
  final int JTD_NUMCRO=2;
  /**
   * Numero Linea del Grid de desglose 11
   */
  int JTD_NUMLIN=11;
  /**
   * Cantidad de Individuos 12
   */
  int JTD_CANIND=12;

  private CLabel acc_dtoppL=new CLabel("Dto PP");
  private CTextField acc_dtoppE=new CTextField(Types.DECIMAL,"#9.99");
  private CTextField acl_dtoppE=new CTextField(Types.DECIMAL,"#9.99");
  BotonBascula botonBascula;
  int proNumcro; // Numeros de crotal iguales, maximos por albaran
  private boolean swCargaLin=false;
  int numIndAnt;
  private boolean swCambioPrv;
  boolean proOblfsa; // Obligatorio el Introduc. Fecha Sacrificio
  int proCodeti=0; // Tipo de etiqueta para el producto.
  private java.util.Date feulin;
  private actStkPart stkPart;
  private boolean swCargaAlb=false,cargaAlbVentas=false;
  static  int ROWTREG=9;
  static  int ROWNVERT=14;
  private  DatosTabla dtCursor;
  private  DatosTabla dtInd;
  utildesp utdes=null;
  paregalm pRegAlm;
  int almOrig;
  vlike lkConfig=new vlike();
  private JMenuItem MIchangeProd;
  private JMenuItem MIactNombre;
  private JMenuItem MIimprEtiq;
  CInternalFrame frProd= new CInternalFrame();
  CInternalFrame frChFeEn= new CInternalFrame();
  CButton BCamFeEnt = new CButton(Iconos.getImageIcon("copia"));
  boolean regDesg; // Tiene Registros de Desglose (para el report)
  int cambio;
  boolean swIniRep = true;
  String prvNomb;
  CTextField acp_canindE = new CTextField(Types.DECIMAL, "##9");
  ResultSet rs;
  CButton  Bimpri   = new CButton(Iconos.getImageIcon("print"));
  CCheckBox opIncDet=new CCheckBox("Inc. Individuos");
  CComboBox div_codiE = new CComboBox();
  
  final static int DESNIND=0;
  
  CTextField acp_numlinE= new CTextField(Types.DECIMAL,"###9");
  CButton  acl_nulinE= new CButton("");
  CTextField acp_numindE = new CTextField(Types.DECIMAL,"##9");
  char estPedi;
  String campPedi;
  copedco conped;  
  
  String lineaAnt;
  etiqueta etiq;
  int ALMACEN=1;
  int swGridDes=0;
  String s;
  int ejPedAnt,nuPedAnt;
  int prvAnt;
  boolean swActDesg;
  CPanel Pprinc = new CPanel();
  CPanel Pcabe = new CPanel();
  CLabel cLabel1 = new CLabel();
  CTextField acc_anoE = new CTextField(Types.DECIMAL,"###9");
  empPanel emp_codiE = new empPanel();
  CTextField acc_numeE = new CTextField(Types.DECIMAL,"#####9");
  CComboBox acc_serieE = new CComboBox();
  CLabel emp_codiL = new CLabel();
  CLabel acc_anoL = new CLabel();
  CLabel acc_fecrecL = new CLabel();
  CTextField acc_fecrecE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel usu_nombL = new CLabel();
  CTextField usu_nombE = new CTextField(Types.CHAR,"X",20);
  CLabel frt_ejercL=new CLabel("Factura Transp");
  CTextField frt_ejercE = new CTextField(Types.DECIMAL,"###9");
  CTextField frt_numeE = new CTextField(Types.DECIMAL,"#####9");

  CLabel prv_codiL = new CLabel();
  prvPanel prv_codiE = new prvPanel()
  {
    @Override
    public void afterCambioPrv()
    {
      if (prv_codiE.isEnabled() && !prv_codiE.isQuery())
        cambioPrv(true);
    };
  };
  CLabel fcc_numeL = new CLabel();
  CTextField fcc_anoE = new CTextField(Types.DECIMAL,"###9");
  CTextField fcc_numeE = new CTextField(Types.DECIMAL,"#####9");
  CLabel acc_obserL = new CLabel();
  CTextField acc_obserE = new CTextField(Types.CHAR,"X",255);
  CComboBox alm_codiE= new CComboBox();
  CCheckBox acl_porpagE = new CCheckBox("0","-1");

  CGridEditable jt = new CGridEditable(16)
  {
        @Override
    public int cambiaLinea(int row, int col)
    {
      
      int ret=cambiaLinAlb(row);
//      if (ret<0)
//      {
//        try  {
//          verDesgLinea(row);
//        } catch (Exception k)
//        {
//          Error("Error al ver desglose Linea",k);
//        }
//      }
      return ret;
    }
//     public void afterCambiaLineaDis(int nRow)
//    {
//      if  (nav.pulsado == navegador.ADDNEW || nav.pulsado == navegador.EDIT )
//          return;
//        try  {
//          verDesgLinea();
//        } catch (Exception k)
//        {
//          Error("Error al ver desglose Linea",k);
//        }
//
//    }
        @Override
    public boolean deleteLinea(int row, int col)
    {
      if (nav.pulsado != navegador.ADDNEW && nav.pulsado!=navegador.EDIT)
        return false;
      try
      {
        if (! isLineaEntera(row))
          return false;
        if (jt.getValorInt(row,0)==0 && !jtDes.isVacio())
        {
            int res=mensajes.mensajeYesNo("Linea tiene individuos. Mantenerla?");
            if (res==mensajes.YES)
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
    public void afterCambiaColumna(int col,int colNueva,int row)
    {
      try
      {
        if (nav.pulsado != navegador.ADDNEW && nav.pulsado!=navegador.EDIT)
        {
          if (ARG_MODPRECIO)
          {
              if (col==JT_PRCOM || col==JT_DTOPP )
                actPrecio(col,row);
              if (col==JT_PORPAG && acl_porpagE.isEnabled())
                actPortesPag(row);
          }
      
          if (col==JT_COMENT)
               actComent(row);
          
          return;
        }
        if (col == 1)
        {
          if (!pro_nombE.isNull() && !pro_codiE.hasCambio() ||
              pro_codiE.getValorInt() == 0)
          {
           
          }
          else
          {
            pro_codiE.resetCambio();
            actLinPed(pro_codiE.getValorInt());
            MIactNombreProd(row);
          }
        }
        if (colNueva == JT_PRCOM)
            ganaFocoPrCompra();
        
      }
      catch (Exception k)
      {
        Error("Error al buscar Nombre Articulo", k);
      }
    }
    @Override
    public void afterCambiaLinea()
    {
      pro_codiE.resetCambio();
      try {
//        if (nav.pulsado != navegador.ADDNEW && nav.pulsado!=navegador.EDIT)
//        {
        try  {
              verDesgLinea(jt.getSelectedRow());
            } catch (Exception k)
            {
              Error("Error al ver desglose Linea",k);
            }
            
//        }
        actLinPed(jt.getValorInt(1));
        pro_codiE.controla(false);
        setEditCant(pro_codiE.getTipoLote());
      } catch (Exception k)
      {
        Error("Error al Cambiar Datos de Linea",k);
      }
    }
        @Override
    public boolean insertaLinea(int row, int col)
    {
      return nav.pulsado == navegador.EDIT || nav.pulsado == navegador.ADDNEW;
    }

  };
  double kgIndivAnt=0; // Kilos anteriores. Usada para autoclasificacion
  CGridEditable jtDes; 
//  CButton Baceptar = new CButton("Aceptar(F4)",Iconos.getImageIcon("check"));
//  CButton Bcancelar = new CButton("Cancelar",Iconos.getImageIcon("cancel"));
  proPanel pro_codiE = new proPanel();
  proPanel pro_codverE = new proPanel();
  CTextField pro_nombE = new CTextField(Types.CHAR,"X",45);
  CTextField acl_numcajE = new CTextField(Types.DECIMAL,"---9");
  CTextField acl_cantiE = new CTextField(Types.DECIMAL,"---,--9.99");
  CTextField acl_prcomE = new CTextField(Types.DECIMAL,"---9.999");
  CTextField acl_kgrecE = new CTextField(Types.DECIMAL,"---9.999");
  CTextField pcl_nucapeE = new CTextField(Types.DECIMAL,"---9");
  CTextField pcl_cantpeE = new CTextField(Types.DECIMAL,"---,--9.99");
  CTextField pcl_precpeE = new CTextField(Types.DECIMAL,"---9.999");
  CTextField dif_nucapeE = new CTextField(Types.DECIMAL,"---9");
  CTextField dif_cantpeE = new CTextField(Types.DECIMAL,"---,--9.99");
  CTextField dif_precpeE = new CTextField(Types.DECIMAL,"---9.999");
  CTextField acl_comenE = new CTextField(Types.CHAR,"X",50);

  CPanel Ptotal = new CPanel();
  CLabel cLabel10 = new CLabel();
  CLabel cLabel8 = new CLabel();
  CTextField imptotE = new CTextField(Types.DECIMAL,"----,--9.99");
  CTextField kilosE = new CTextField(Types.DECIMAL,"---,--9.99");
  CLabel cLabel9 = new CLabel();
  CTextField nLinE = new CTextField(Types.DECIMAL,"---9");
  CTextField kilostE = new CTextField(Types.DECIMAL,"---,--9.99");
  CLabel cLabel11 = new CLabel();
  CLabel cLabel2 = new CLabel();
  CLabel cLabel3 = new CLabel();
  CTextField acp_cantiE= new CTextField(Types.DECIMAL,"--,--9.99");
  CTextField acp_nucrotE= new CTextField(Types.CHAR,"X",30);
 
  CButton Bulcabe = new CButton();
  CButton Birgrid = new CButton(Iconos.getImageIcon("reload"));
  CCheckBox opImpEti = new CCheckBox();
  CButton BimpEti = new CButton("F9",Iconos.getImageIcon("print"));
  CCheckBox opAutoClas = new CCheckBox();
  boolean opAutoCl=false;
  prvPanel acc_copvfaE = new prvPanel();
  CLabel prv_codiL1 = new CLabel();
  CCheckBox opBloquea = new CCheckBox();
  boolean ARG_ADMIN=false;
  boolean ARG_MODPRECIO=false;
  boolean ARG_ALBSINPED=false;
  //private int ARG_TIPOETIQ=etiqueta.NORMAL;
  CLabel cLabel4 = new CLabel();
  CTextField eje_numeE = new CTextField(Types.DECIMAL,"###9");
  CTextField pcc_numeE = new CTextField(Types.DECIMAL,"####9");
  CButton BbusPed = new CButton(Iconos.getImageIcon("find"));
  CPanel Ppedi = new CPanel();
  CLabel cLabel5 = new CLabel();
  CLabel cLabel6 = new CLabel();
  CTextField dif_unidE = new CTextField(Types.DECIMAL,"---9.99");
  CLabel cLabel7 = new CLabel();
  CTextField dif_kgsE = new CTextField(Types.DECIMAL,"---,--9.99");
  CCheckBox opAgrupar = new CCheckBox();
  CLabel cLabel13 = new CLabel();
  CTextField alc_kgsE = new CTextField(Types.DECIMAL,"---,--9.99");
  CTextField alc_unidE = new CTextField(Types.DECIMAL,"---9.99");
  CTextField pcl_kgsE = new CTextField(Types.DECIMAL,"---,--9.99");
  CTextField pcl_unidE = new CTextField(Types.DECIMAL,"---9.99");
  CLabel cLabel14 = new CLabel();
  CPanel cPanel1 = new CPanel();
  CLabel cLabel15 = new CLabel();
  CLabel pcl_comenE = new CLabel();
  CLabel pcc_comenE = new CLabel();
//  JTabbedPane Tpanel1 = new JTabbedPane();
  JTabbedPane Tpanel1 = new JTabbedPane(JTabbedPane.TOP);
  CPanel Palbar = new CPanel();
  CPanel Ppedido = new CPanel();
  Cgrid jtPed = new Cgrid(8);
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  CLabel cLabel17 = new CLabel();
  CTextField dif_porcE = new CTextField(Types.DECIMAL,"--9.99");
  CComboBox cll_codiE = new CComboBox();
  CLabel cLabel12 = new CLabel();
  CComboBox acc_portesE = new CComboBox();
  CLabel cLabel18 = new CLabel();
  CTextField acc_impokgE = new CTextField(Types.DECIMAL,"##9.999");
  CCheckBox opIncPortes = new CCheckBox();
  CCheckBox opVerPrecios = new CCheckBox();
  CPanel cPanel2 = new CPanel();
  CLabel cLabel19 = new CLabel();
  proPanel pro_codcamE = new proPanel();
  CButton BaceCam = new CButton("Aceptar",Iconos.getImageIcon("check"));
  CButton BcanCam = new CButton("Cancelar",Iconos.getImageIcon("cancel"));
  CCheckBox opRepEti = new CCheckBox();
  CCheckBox opActFra = new CCheckBox();
  CPanel cPanel3 = new CPanel();
  CLabel acc_fecrecL1 = new CLabel();
  CTextField fereinE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel acc_fecrecL2 = new CLabel();
  CTextField ferefiE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel acc_fecrecL3 = new CLabel();
  CTextField ferepoE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CButton Baccafe = new CButton("Aceptar",Iconos.getImageIcon("check"));
  CButton Bcacafe = new CButton("Cancelar",Iconos.getImageIcon("cancel"));
  CLabel cLabel20 = new CLabel();
  CComboBox acc_cerraE = new CComboBox();
  CLabel cLabel21 = new CLabel();
  CLabel cLabel16 = new CLabel();
  CComboBox acc_totfraE = new CComboBox();
  CButton Bdesagr = new CButton("F6",Iconos.getImageIcon("stock_insert"));
  CPanel PVerted = new CPanel();
  cliPanel rgs_clidevE= new cliPanel();
  CTextField rgs_clinombE=new CTextField(Types.CHAR,"X",50);
  CGridEditable jtRecl = new CGridEditable(15)
  {
        @Override
    public void cambiaColumna(int col,int colNueva, int row)
    {
      try
      {
//        debug ("Num. ind:"+pro_numindE.getValorInt());
        if (col == 0)
        {
          pro_codverE.resetCambio();
          actNombProdVert(row);
          return;
        }
        if (col==2)
        {
          if (pro_numindE.hasCambio())
          {
            rgs_kilosE.resetTexto();
            pro_codverE.resetTexto();
          }
          pro_numindE.resetCambio();
          if (rgs_kilosE.getValorDec()==0 || pro_codverE.isNull())
            actLinVert(pro_numindE.getValorInt(),row);
        }
        if (col==7) // Estado
        {
//          if (tir_codiE.getValorInt()==0)
//          {
//            int tirCodi=rgs_recprvE.getText().equals("R")?paregalm.TIRVERPRVREC:
//                rgs_recprvE.getText().equals("A")?paregalm.TIRVERPRVACE:paregalm.TIRVERPRVPEN;
//            tir_codiE.setValorInt(tirCodi);
//            jtRecl.setValor(""+tirCodi, row,ROWTREG);
//          }
          if (rgs_recprvE.getText().equals("R"))
          {
            rgs_prreguE.setValorDec(0);
            jtRecl.setValor("0", row,5);
          }
          if (rgs_recprvE.getText().equals("A") || rgs_recprvE.getText().equals("R"))
          {
            if (rgs_fecresE.isNull())
              rgs_fecresE.setText(Formatear.getFechaAct("dd-MM-yy"));
          }
          else
            rgs_fecresE.resetTexto();
         jtRecl.setValor(rgs_fecresE.getText(),row,8);
        }
        if (col==11) // Cliente
        {
          String nombCli="";
          if (rgs_clidevE.getValorInt()==0)
            nombCli="";
          else
            nombCli=rgs_clidevE.getNombCliente(dtStat,rgs_clidevE.getValorInt());
          if (nombCli==null)
            nombCli="Cliente NO encontrado";
//          rgs_clinombE.setText(nombCli);
          jtRecl.setValor(nombCli,row,12);
        }
      }
      catch (SQLException k)
      {
        Error("Error al cambiar Columna",k);
      }
    }
    
    @Override
    public boolean deleteLinea(int row, int col)
    {
      if (jtRecl.getValorInt(ROWNVERT) == 0)
        return true;
      try
      {
        boolean res = pRegAlm.borrarRegistro(jtRecl.getValorInt(row,ROWNVERT));
        if (!res)
        {
          msgBox("No se encontro REGISTRO de Vertedero");
          ctUp.rollback();
          return false;
        }
        ctUp.commit();

        return true;
      }
      catch (Exception k)
      {
        Error("Error al Borrar Linea", k);
      }
      return true;
    }

        @Override
    public int cambiaLinea(int row, int col)
    {
//        jtRecl.setValor(jtRecl.actValGrid(row,col),row,col);
      return cambiaLinRecl(row);
    }

  };
  CPanel PVertInf = new CPanel();
  CLabel cLabel22 = new CLabel();
  CTextField vertNPiezE = new CTextField("###9");
  CLabel cLabel23 = new CLabel();
  CTextField vertKilosE = new CTextField(Types.DECIMAL,"---,--9.99");
  CTextField pro_nomverE = new CTextField();
  CTextField pro_numindE = new CTextField(Types.DECIMAL, "####9");
  CTextField rgs_kilosE = new CTextField(Types.DECIMAL, "--,--9.99");
  CTextField rgs_prreguE = new CTextField(Types.DECIMAL, "--,--9.99");

  CTextField rgs_fechaE = new CTextField(Types.DATE,"dd-MM-yy");
  CTextField rgs_fecresE = new CTextField(Types.DATE,"dd-MM-yy"); // Fecha Resoluci�n
  CTextField rgs_numeE= new CTextField(Types.DECIMAL,"#####9");
  CTextField rgs_cantiE= new CTextField(Types.DECIMAL,"----9");
  CTextField rgs_recprvE= new CTextField(Types.CHAR,"?");
  CTextField rgs_comenE = new CTextField(Types.CHAR,"X",100);
  CButton BvertSala = new CButton();
  GridBagLayout gridBagLayout3 = new GridBagLayout();
  CLabel cLabel24 = new CLabel();
  CPanel cPanel4 = new CPanel();
  CComboBox estIniE = new CComboBox();
  CLabel cLabel25 = new CLabel();
  CComboBox estFinE = new CComboBox();
  CLabel cLabel26 = new CLabel();
  CTextField vertImporE = new CTextField(Types.DECIMAL,"---,--9.99");
  CLinkBox tir_codiE = new CLinkBox();
  CCheckBox rgs_traspE = new CCheckBox();
  CPanel Potros = new CPanel();
  CLabel cLabel27 = new CLabel();
  GridBagLayout gridBagLayout4 = new GridBagLayout();
  sbePanel sbe_codiE = new sbePanel();
  CLabel cLabel110 = new CLabel();
  CLabel sbe_nombL;
  CLabel cLabel28 = new CLabel();
  CTextField avc_anoE = new CTextField(Types.DECIMAL,"###9");
  CTextField avc_numeE = new CTextField(Types.DECIMAL,"#####9");
//  CComboBox estIniE = new CComboBox();

  public MantAlbCom(EntornoUsuario eu, Principal p)
  {
    this(eu,p,null);
  }
  public MantAlbCom(EntornoUsuario eu, Principal p,Hashtable ht)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    try
    {
      if (ht != null)
      {
//        if (ht.get("tipoEtiq") != null)
//          ARG_TIPOETIQ = Integer.parseInt(ht.get("tipoEtiq").toString());
        if (ht.get("modPrecio") != null)
          ARG_MODPRECIO = Boolean.valueOf(ht.get("modPrecio").toString()).
              booleanValue();
        if (ht.get("admin") != null)
          ARG_ADMIN = Boolean.valueOf(ht.get("admin").toString()).
              booleanValue();
        if (ht.get("AlbSinPed") != null)
          ARG_ALBSINPED = Boolean.valueOf(ht.get("AlbSinPed").toString()).booleanValue();

        if (ARG_ADMIN)
        {
          ARG_ALBSINPED=true;
          ARG_MODPRECIO = true;
        }

      }
      setTitulo("Mantenimiento Albaranes Compras");

      if (jf.gestor.apuntar(this))
        jbInit();
      else
        setErrorInit(true);
    }
    catch (Exception e)
    {
      Logger.getLogger(MantAlbCom.class.getName()).log(Level.SEVERE, null, e);
      setErrorInit(true);
    }
  }

  public MantAlbCom(gnu.chu.anjelica.menu p, EntornoUsuario eu)
  {
    this(p,eu,null);
  }
  public MantAlbCom(gnu.chu.anjelica.menu p, EntornoUsuario eu,Hashtable ht)
  {
    EU = eu;
    vl = p.getLayeredPane();
    eje = false;

    try
    {
      if (ht != null)
      {
//        if (ht.get("tipoEtiq") != null)
//          ARG_TIPOETIQ = Integer.parseInt(ht.get("tipoEtiq").toString());
        if (ht.get("modPrecio") != null)
          ARG_MODPRECIO = Boolean.valueOf(ht.get("modPrecio").toString()).
              booleanValue();
        if (ht.get("admin") != null)
          ARG_ADMIN = Boolean.valueOf(ht.get("admin").toString()).
              booleanValue();
        if (ht.get("AlbSinPed") != null)
          ARG_ALBSINPED = Boolean.valueOf(ht.get("AlbSinPed").toString()).booleanValue();

        if (ARG_ADMIN)
        {
          ARG_ALBSINPED=true;
          ARG_MODPRECIO = true;
        }
      }
      setTitulo("Mantenimiento Albaranes Compras");
      jf=null;
      jbInit();
    }
    catch (Exception e)
    {
      Logger.getLogger(MantAlbCom.class.getName()).log(Level.SEVERE, null, e);
      setErrorInit(true);
    }
  }

    boolean getIndAlbcompar(int nLiAlb, int nInd,DatosTabla dt) throws SQLException {
        // No lo encuentro por Num. Linea. Pruebo por Num. Individuo
        String ss = "SELECT * FROM v_albcompar " +
             " WHERE emp_codi = " + emp_codiE.getValorInt() +
             " AND acc_ano = " + acc_anoE.getValorInt() +
             " and acc_nume = " + acc_numeE.getValorInt() +
             " and acc_serie = '" + acc_serieE.getText() + "'" +
             (nLiAlb<0?"":" and acl_nulin = " + nLiAlb )+
             " and acp_numind = " + nInd;
        return dt.select(ss);
    }

  void jbInit() throws Exception
  {
    iniciarFrame();
    this.setSize(new Dimension(770, 530));
    this.setVersion("(20141006)  "+(ARG_MODPRECIO?"- Modificar Precios":"")+
          (ARG_ADMIN?"--ADMINISTRADOR--":"")+(ARG_ALBSINPED?"Alb. s/Ped":""));

    statusBar = new StatusBar(this);
    nav = new navegador(this, dtCons, false, navegador.NORMAL);
    conecta();
    iniciar(this);

    Pprinc.setLayout(gridBagLayout1);
    frProd.setTitle("Cambia Codigo de Producto");
    frProd.setLocation(this.getLocation().x+10,this.getLocation().y+20);
    frProd.setSize(new Dimension(541, 123));
    frProd.setVisible(false);

    frChFeEn.setTitle("Cambiar Fecha de Entrada");
    frChFeEn.setLocation(this.getLocation().x+10,this.getLocation().y+20);
    frChFeEn.setSize(new Dimension(394, 194));
    frChFeEn.setVisible(false);

    opRepEti.setMargin(new Insets(0, 0, 0, 0));
    opRepEti.setSelected(true);
    opRepEti.setText("Repetir Etiquetas");
    opRepEti.setBounds(new Rectangle(42, 38, 134, 20));
    opActFra.setText("Act. Factura");
    opActFra.setBounds(new Rectangle(6, 22, 103, 16));
    opActFra.setEnabled(false);
    opAutoClas.setToolTipText("Clasificacion de Productos Automatica según su peso");
    opImpEti.setToolTipText("Imprimir Etiqueta");
    opAgrupar.setToolTipText("Agrupar Productos");
    opBloquea.setMargin(new Insets(0, 0, 0, 0));
    cPanel3.setOpaque(true);
    rgs_clinombE.setEnabled(false);

    cPanel3.setLayout(null);
    fereinE.setBounds(new Rectangle(2, 4, 95, 16));
    acc_fecrecL1.setText("De Fecha Recep.");
    acc_fecrecL1.setBounds(new Rectangle(1, 3, 98, 17));
    ferefiE.setBounds(new Rectangle(301, 3, 78, 16));
    acc_fecrecL2.setText("A Fecha Recep.");
    acc_fecrecL2.setBounds(new Rectangle(206, 3, 88, 16));
    fereinE.setBounds(new Rectangle(304, 4, 78, 16));
    acc_fecrecL3.setText("Poner Fecha Recepcion");
    acc_fecrecL3.setBounds(new Rectangle(71, 29, 128, 16));
    fereinE.setBounds(new Rectangle(95, 3, 78, 16));
    ferepoE.setBounds(new Rectangle(201, 30, 72, 15));
    Baccafe.setBounds(new Rectangle(13, 58, 118, 29));
    Bcacafe.setBounds(new Rectangle(219, 58, 118, 29));
    cLabel20.setText("Almacén");
    cLabel20.setBounds(new Rectangle(2, 63, 54, 18));
    alm_codiE.setToolTipText("Almacen de Recepci�n");
    alm_codiE.setBounds(new Rectangle(59, 63, 304, 18));
    prv_codiL1.setToolTipText("");

    acc_cerraE.setBounds(new Rectangle(469, 64, 43, 18));
    cLabel21.setText("Cerrado");
    cLabel21.setBounds(new Rectangle(403, 64, 54, 18));
    cLabel16.setToolTipText("Totalmente Facturado");
    cLabel16.setText("Facturado");
    cLabel16.setBounds(new Rectangle(404, 44, 62, 18));
    acc_totfraE.setBounds(new Rectangle(469, 44, 43, 18));
    acc_totfraE.setDependePadre(false);
    acc_cerraE.setDependePadre(false);
    alm_codiE.setDependePadre(false);
    Bdesagr.setBounds(new Rectangle(204, 20, 77, 18));
    Bdesagr.setToolTipText("Desagrupar Productos");
    Bdesagr.setMargin(new Insets(0, 0, 0, 0));
    Bdesagr.setText("F6");

    PVerted.setLayout(gridBagLayout3);
    PVertInf.setBorder(BorderFactory.createRaisedBevelBorder());

    PVertInf.setMaximumSize(new Dimension(652, 27));
    PVertInf.setMinimumSize(new Dimension(652, 27));
    PVertInf.setPreferredSize(new Dimension(652, 27));

    PVertInf.setLayout(null);
    cLabel22.setText("No. Piezas");
    cLabel22.setBounds(new Rectangle(5, 4, 57, 16));

    cLabel23.setText("Kilos");
    cLabel23.setBounds(new Rectangle(114, 5, 37, 16));
    vertKilosE.setEnabled(false);
    vertKilosE.setBounds(new Rectangle(141, 5, 55, 16));
    BvertSala.setBounds(new Rectangle(249, 1, 80, 18));
    BvertSala.setToolTipText("Pasar de un estado a Otro");
    BvertSala.setText("Pasar");
    vertNPiezE.setEnabled(false);
    vertNPiezE.setBounds(new Rectangle(62, 5, 44, 16));
    cLabel24.setMaximumSize(new Dimension(46, 16));
    cLabel24.setMinimumSize(new Dimension(46, 16));
    cLabel24.setPreferredSize(new Dimension(46, 16));
    cLabel24.setRequestFocusEnabled(true);

    cLabel24.setText("Pasar De");
    cLabel24.setBounds(new Rectangle(1, 2, 50, 16));
    cPanel4.setBorder(BorderFactory.createLineBorder(Color.black));

    cPanel4.setBounds(new Rectangle(316, 3, 334, 21));
    cPanel4.setLayout(null);
    estIniE.setText("estIniE");
    estIniE.setBounds(new Rectangle(53, 2, 86, 16));
    cLabel25.setText("A");
    cLabel25.setBounds(new Rectangle(143, 2, 15, 16));
    estFinE.setText("estIniE");
    estFinE.setBounds(new Rectangle(158, 2, 86, 16));
    cLabel26.setText("Imp.");
    cLabel26.setBounds(new Rectangle(211, 4, 32, 17));
    vertImporE.setBounds(new Rectangle(244, 4, 61, 16));
    vertImporE.setEnabled(false);

    Potros.setLayout(null);
    cLabel27.setText("Comentario");
    jtPed.setMaximumSize(new Dimension(755, 312));
    jtPed.setMinimumSize(new Dimension(755, 312));
    jtPed.setPreferredSize(new Dimension(755, 312));
    pcc_comenE.setMaximumSize(new Dimension(677, 18));
    pcc_comenE.setMinimumSize(new Dimension(677, 18));
    pcc_comenE.setPreferredSize(new Dimension(677, 18));
    sbe_codiE.setBounds(new Rectangle(59, 43, 52, 18));

    cLabel110.setBounds(new Rectangle(4, 43, 53, 18));
    cLabel110.setText("SubEmp.");
    cLabel110.setToolTipText("SubEmpresa");

    sbe_nombL.setBounds(new Rectangle(118, 43, 243, 18));
    cLabel28.setText("Alb. Venta");
    cLabel28.setBounds(new Rectangle(173, 84, 63, 16));
    avc_anoE.setBounds(new Rectangle(230, 84, 35, 16));
    avc_numeE.setBounds(new Rectangle(268, 84, 44, 16));
    acc_dtoppL.setBounds(new Rectangle(320,84,35,16));
    acc_dtoppE.setBounds(new Rectangle(357,84,38,16));
    acc_dtoppE.setDependePadre(false);
    vl.add(frProd,new Integer(1));
    vl.add(frChFeEn,new Integer(1));
    opImpEti.setSelected(true);
    opBloquea.setEnabled(false);
    Bimpri.setPreferredSize(new Dimension(24,24));
    Bimpri.setMaximumSize(new Dimension(24,24));
    Bimpri.setMinimumSize(new Dimension(24,24));
    opIncDet.setMargin(new Insets(0,0,0,0));
    opIncDet.setPreferredSize(new Dimension(120,20));
    opIncDet.setMaximumSize(new Dimension(120,20));
    opIncDet.setMinimumSize(new Dimension(120,20));
    Bimpri.setToolTipText("Imprimir Albaran");
    opVerPrecios.setSelected(true);
    opVerPrecios.setText("Ver Precios");
    opVerPrecios.setBounds(new Rectangle(102, 24, 100, 14));

    cPanel2.setLayout(null);
    cLabel19.setText("Producto");
    cLabel19.setBounds(new Rectangle(9, 5, 55, 21));
    pro_codcamE.setBounds(new Rectangle(68, 5, 422, 21));
    BaceCam.setBounds(new Rectangle(185, 32, 126, 32));
    BcanCam.setBounds(new Rectangle(362, 32, 128, 32));

    BCamFeEnt.setToolTipText("Cambiar Fecha Albaran");
    if (!ARG_ADMIN)
      BCamFeEnt.setVisible(false);
    BCamFeEnt.setMinimumSize(new Dimension(24,24));
    BCamFeEnt.setMaximumSize(new Dimension(24,24));
    BCamFeEnt.setPreferredSize(new Dimension(24,24));

    statusBar.add(Bimpri, new GridBagConstraints(9, 0, 1, 2, 0.0, 0.0
                                                 , GridBagConstraints.EAST,
                                                 GridBagConstraints.VERTICAL,
                                                 new Insets(0, 5, 0, 0), 0, 0));
    statusBar.add(opIncDet, new GridBagConstraints(8, 0, 1, 2, 0.0, 0.0
                                                  , GridBagConstraints.EAST,
                                                  GridBagConstraints.VERTICAL,
                                                  new Insets(0, 5, 0, 0), 0, 0));
    statusBar.add(BCamFeEnt, new GridBagConstraints(7, 0, 1, 2, 0.0, 0.0
                       , GridBagConstraints.EAST,GridBagConstraints.VERTICAL,
                       new Insets(0, 5, 0, 0), 0, 0));

   
    botonBascula.setPreferredSize(new Dimension(50,24));
    botonBascula.setMinimumSize(new Dimension(50,24));
    botonBascula.setMaximumSize(new Dimension(50,24));
    statusBar.add(botonBascula, new GridBagConstraints(6, 0, 1, 2, 0.0, 0.0
                                               , GridBagConstraints.EAST,
                                               GridBagConstraints.VERTICAL,
                                               new Insets(0, 5, 0, 0), 0, 0));

    if (!ARG_MODPRECIO)
    {
      opVerPrecios.setEnabled(false);
      opVerPrecios.setSelected(false);
      acl_prcomE.setEditable(false);
      acl_dtoppE.setEditable(false);
      acc_impokgE.setEnabled(false);
      acc_portesE.setEnabled(false);
      acl_dtoppE.setEnabled(eje);
      acl_porpagE.setEnabled(false);
    }
    acc_portesE.addItem("Pagados", "P");
    acc_portesE.addItem("Debidos", "D");
   

    confGridLin();
    //confGridDesglose();
    confGridHist();
    Pcabe.setBorder(BorderFactory.createRaisedBevelBorder());
    Pcabe.setMaximumSize(new Dimension(713, 105));
    Pcabe.setMinimumSize(new Dimension(713, 105));
    Pcabe.setPreferredSize(new Dimension(713, 105));
    Pcabe.setLayout(null);
    cLabel1.setText("Albaran");
    cLabel1.setBounds(new Rectangle(197, 3, 49, 16));
    emp_codiL.setText("Empresa");
    emp_codiL.setBounds(new Rectangle(1, 3, 53, 16));
    acc_anoL.setText("Ejercicio");
    acc_anoL.setBounds(new Rectangle(102, 3, 49, 16));
    acc_fecrecL.setText("Fec.Recep.");
    acc_fecrecL.setBounds(new Rectangle(354, 3, 61, 16));
    usu_nombL.setText("Usuario");
    usu_nombL.setBounds(new Rectangle(0, 58, 45, 16));
    frt_ejercL.setBounds(new Rectangle(0, 78, 95, 16));
    frt_ejercE.setBounds(new Rectangle(100, 78, 45, 16));
    frt_numeE.setBounds(new Rectangle(150, 78, 55, 16));
    frt_ejercE.setEnabled(false);
    frt_numeE.setEnabled(false);
    prv_codiL.setText("Proveedor");
    prv_codiL.setBounds(new Rectangle(1, 23, 66, 18));
    prv_codiE.setBounds(new Rectangle(59, 23, 432, 18));
    prv_codiE.setAncTexto(50);
    fcc_numeL.setText("Fra.");
    fcc_numeL.setBounds(new Rectangle(531, 3, 28, 16));
    acc_obserL.setText("Observ.");
    acc_obserL.setBounds(new Rectangle(0, 9, 43, 18));
    acc_obserE.setBounds(new Rectangle(51, 9, 704, 19));
    fcc_numeE.setBounds(new Rectangle(589, 3, 49, 16));
    fcc_anoE.setBounds(new Rectangle(553, 3, 33, 16));
    usu_nombE.setBounds(new Rectangle(51, 57, 73, 16));
    acc_fecrecE.setBounds(new Rectangle(416, 3, 78, 16));
    acc_numeE.setBounds(new Rectangle(288, 2, 44, 16));
    acc_serieE.setBounds(new Rectangle(247, 3, 40, 16));
    acc_anoE.setBounds(new Rectangle(152, 3, 35, 16));
    emp_codiE.setBounds(new Rectangle(49, 3, 49, 18));
    Ptotal.setBorder(BorderFactory.createLoweredBevelBorder());
    Ptotal.setMaximumSize(new Dimension(540, 40));
    Ptotal.setMinimumSize(new Dimension(540, 40));
    Ptotal.setPreferredSize(new Dimension(540, 40));
    Ptotal.setLayout(null);
    cLabel10.setText("Imp. Lineas");
    cLabel8.setText("N. Lin");
    cLabel8.setBounds(new Rectangle(344, 2, 38, 17));
    cLabel9.setText("Kilos");
    cLabel10.setToolTipText("");
    cLabel10.setText("Importe");
    cLabel10.setBounds(new Rectangle(566, 2, 47, 17));
    cLabel9.setText("Kilos");
    cLabel9.setBounds(new Rectangle(252, 2, 31, 17));
    imptotE.setMinimumSize(new Dimension(4, 21));
    imptotE.setEnabled(false);
    imptotE.setBounds(new Rectangle(613, 2, 57, 17));
    kilosE.setEnabled(false);
    kilosE.setBounds(new Rectangle(279, 2, 61, 17));
    nLinE.setEnabled(false);
    nLinE.setBounds(new Rectangle(381, 2, 40, 17));
    kilostE.setEnabled(false);
    kilostE.setBounds(new Rectangle(503, 2, 61, 17));
    cLabel11.setText("Kilos");
    cLabel11.setBounds(new Rectangle(473, 2, 31, 17));
    cLabel2.setBackground(Color.red);
    cLabel2.setForeground(Color.yellow);
    cLabel2.setOpaque(true);
    cLabel2.setHorizontalAlignment(SwingConstants.CENTER);
    cLabel2.setHorizontalTextPosition(SwingConstants.CENTER);
    cLabel2.setText("DESG");
    cLabel2.setBounds(new Rectangle(206, 2, 44, 14));
    cLabel3.setText("TOTAL");
    cLabel3.setBounds(new Rectangle(424, 2, 45, 17));
    cLabel3.setHorizontalTextPosition(SwingConstants.CENTER);
    cLabel3.setHorizontalAlignment(SwingConstants.CENTER);
    cLabel3.setOpaque(true);
    cLabel3.setForeground(Color.yellow);
    cLabel3.setBackground(Color.red);
    Bulcabe.setBounds(new Rectangle(564, 92, 2, 2));
    Birgrid.setBounds(new Rectangle(607, 20, 56, 17));
    Birgrid.setToolTipText("Ir a Grid");
    Birgrid.setMargin(new Insets(0, 0, 0, 0));
    Birgrid.setText("F2");
    opImpEti.setSelected(true);
    opImpEti.setText("Impr. Etiq.");
    opImpEti.setBounds(new Rectangle(446, 20, 101, 17));
    BimpEti.setBounds(new Rectangle(548, 21, 58, 17));
    BimpEti.setToolTipText("Imprimir Etiqueta");
    BimpEti.setMargin(new Insets(0, 0, 0, 0));
    opAutoClas.setText("Clasif. Productos");
    opAutoClas.setBounds(new Rectangle(321, 20, 126, 16));
    acc_copvfaE.setBounds(new Rectangle(99, 32, 462, 18));
    acc_copvfaE.setAncTexto(50);
    prv_codiL1.setText("Prov. a Facturar");
    prv_codiL1.setBounds(new Rectangle(1, 32, 94, 18));
    opBloquea.setText("Bloqueado");
    opBloquea.setBounds(new Rectangle(656, 2, 90, 17));
    cLabel4.setText("Pedido");
    cLabel4.setBounds(new Rectangle(3, 83, 43, 16));
    eje_numeE.setTipoCampo(3);
    eje_numeE.setText("0");
    eje_numeE.setBounds(new Rectangle(57, 83, 36, 16));
    pcc_numeE.setBounds(new Rectangle(95, 83, 44, 16));
    BbusPed.setBounds(new Rectangle(141, 83, 18, 17));
    BbusPed.setMargin(new Insets(0, 0, 0, 0));


    Ppedi.setBorder(BorderFactory.createLoweredBevelBorder());
    Ppedi.setInputVerifier(null);
    Ppedi.setBounds(new Rectangle(525, 27, 229, 66));
    Ppedi.setLayout(null);
    cLabel5.setBackground(Color.red);
    cLabel5.setForeground(Color.white);
    cLabel5.setOpaque(true);
    cLabel5.setHorizontalAlignment(SwingConstants.CENTER);
    cLabel5.setHorizontalTextPosition(SwingConstants.CENTER);
    cLabel5.setText("Diferencia");
    cLabel5.setBounds(new Rectangle(3, 47, 67, 15));
    cLabel6.setBackground(Color.blue);
    cLabel6.setForeground(Color.white);
    cLabel6.setOpaque(true);
    cLabel6.setHorizontalAlignment(SwingConstants.CENTER);
    cLabel6.setText("Unid.");
    cLabel6.setBounds(new Rectangle(71, 2, 43, 15));
    dif_unidE.setEnabled(false);
    dif_unidE.setBounds(new Rectangle(71, 47, 42, 15));
    cLabel7.setBackground(Color.blue);
    cLabel7.setForeground(Color.white);
    cLabel7.setOpaque(true);
    cLabel7.setHorizontalAlignment(SwingConstants.CENTER);
    cLabel7.setText("Kilos");
    cLabel7.setBounds(new Rectangle(117, 2, 60, 15));

    dif_kgsE.setEnabled(false);
    dif_kgsE.setBounds(new Rectangle(117, 47, 61, 15));
    dif_porcE.setEnabled(false);
    opAgrupar.setText("Agr. Prod.");
    opAgrupar.setSelected(true);
    opAgrupar.setBounds(new Rectangle(665, 20, 89, 17));
    cLabel13.setText("Recepcion");
    cLabel13.setBounds(new Rectangle(3, 17, 67, 15));
    cLabel13.setHorizontalTextPosition(SwingConstants.CENTER);
    cLabel13.setHorizontalAlignment(SwingConstants.CENTER);
    cLabel13.setOpaque(true);
    cLabel13.setForeground(Color.white);
    cLabel13.setBackground(Color.red);
    alc_kgsE.setEnabled(false);
    alc_kgsE.setBounds(new Rectangle(117, 17, 60, 15));
    alc_unidE.setEnabled(false);
    alc_unidE.setBounds(new Rectangle(71, 17, 43, 15));
    pcl_kgsE.setEnabled(false);
    pcl_kgsE.setBounds(new Rectangle(117, 32, 61, 15));
    pcl_unidE.setEnabled(false);
    pcl_unidE.setBounds(new Rectangle(71, 32, 43, 15));
    cLabel14.setBackground(Color.red);
    cLabel14.setForeground(Color.white);
    cLabel14.setOpaque(true);
    cLabel14.setHorizontalAlignment(SwingConstants.CENTER);
    cLabel14.setHorizontalTextPosition(SwingConstants.CENTER);
    cLabel14.setText("Pedido");
    cLabel14.setBounds(new Rectangle(3, 32, 67, 15));
    cPanel1.setBorder(BorderFactory.createLineBorder(Color.black));
    cPanel1.setMaximumSize(new Dimension(734, 22));
    cPanel1.setMinimumSize(new Dimension(734, 22));
    cPanel1.setPreferredSize(new Dimension(734, 22));
    cPanel1.setLayout(null);
    cLabel15.setText("Com. Linea Ped.");
    cLabel15.setBounds(new Rectangle(4, 2, 91, 18));
    pcl_comenE.setBackground(Color.yellow);
    pcl_comenE.setForeground(Color.black);
    pcl_comenE.setOpaque(true);
    pcl_comenE.setBounds(new Rectangle(91, 3, 560, 15));
    pcc_comenE.setOpaque(true);
    Baceptar.setMargin(new Insets(0,0,0,0));
    Bcancelar.setMargin(new Insets(0,0,0,0));
    Baceptar.setBounds(new Rectangle(3, 1, 105, 21));
   Bcancelar.setBounds(new Rectangle(111, 1, 90, 21));

    pcc_comenE.setForeground(Color.black);
    pcc_comenE.setBackground(Color.yellow);


    Palbar.setLayout(gridBagLayout2);
//    Ppedido.setMaximumSize(new Dimension(732, 375));
//    Ppedido.setMinimumSize(new Dimension(732, 375));
//    Ppedido.setPreferredSize(new Dimension(732, 375));

    Tpanel1.setMaximumSize(new Dimension(732, 375));
    Tpanel1.setMinimumSize(new Dimension(732, 300));
    Tpanel1.setPreferredSize(new Dimension(732, 375));

//    Palbar.setMaximumSize(new Dimension(732, 375));
//    Palbar.setMinimumSize(new Dimension(732, 300));
//    Palbar.setPreferredSize(new Dimension(732, 375));

    Ppedido.setLayout(gridBagLayout4);
    cLabel17.setBounds(new Rectangle(179, 2, 48, 15));
    cLabel17.setText("%");
    cLabel17.setHorizontalAlignment(SwingConstants.CENTER);
    cLabel17.setOpaque(true);
    cLabel17.setForeground(Color.white);
    cLabel17.setBackground(Color.blue);
    dif_porcE.setBounds(new Rectangle(179, 47, 48, 15));
    dif_porcE.setText("");
    dif_porcE.setEnabled(false);

    cll_codiE.setBounds(new Rectangle(285, 20, 36, 16));
    cLabel12.setText("Portes");
    cLabel12.setBounds(new Rectangle(637, 3, 39, 16));

    acc_portesE.setBounds(new Rectangle(676, 3, 79, 17));
    cLabel18.setText("Portes/Kg");
    cLabel18.setBounds(new Rectangle(395, 83, 62, 16));
    acc_impokgE.setBounds(new Rectangle(458, 83, 54, 16));

    opIncPortes.setToolTipText("Inc. Portes en precios");
    opIncPortes.setMargin(new Insets(0, 0, 0, 0));
    opIncPortes.setSelected(true);
    opIncPortes.setText("Inc. Portes");
    opIncPortes.setBounds(new Rectangle(671, 2, 89, 17));
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
    Pprinc.add(Pcabe,    new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    Pcabe.add(prv_codiL, null);
    Pprinc.add(Ptotal,      new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 1, 0), 0, 0));
    BbusPed.setFocusable(false);
    Ppedi.add(cLabel14, null);
    Ppedi.add(cLabel5, null);
    Ppedi.add(cLabel13, null);
    Ppedi.add(cLabel6, null);
    Ppedi.add(alc_unidE, null);
    Ppedi.add(pcl_unidE, null);
    Ppedi.add(dif_unidE, null);
    Ppedi.add(alc_kgsE, null);
    Ppedi.add(pcl_kgsE, null);
    Ppedi.add(dif_kgsE, null);
    Ppedi.add(cLabel7, null);
    Ppedi.add(cLabel17, null);
    Ppedi.add(dif_porcE, null);
    Pcabe.add(avc_numeE, null);
    Pcabe.add(acc_dtoppL,null);
    Pcabe.add(acc_dtoppE,null);
    Pcabe.add(cLabel28, null);
    Pcabe.add(avc_anoE, null);
    Pcabe.add(sbe_nombL, null);
    Pcabe.add(cLabel1, null);
    Pcabe.add(acc_numeE, null);
    Pcabe.add(Bulcabe, null);
    Pcabe.add(acc_fecrecE, null);
    Pcabe.add(acc_fecrecL, null);
    Pcabe.add(acc_serieE, null);
    Pcabe.add(acc_anoE, null);
    Pcabe.add(acc_anoL, null);
    Pcabe.add(acc_totfraE, null);
    Pcabe.add(cLabel110, null);
    Pcabe.add(sbe_codiE, null);
    Pcabe.add(cLabel16, null);
    Pcabe.add(cLabel21, null);
    Pcabe.add(acc_cerraE, null);
    Pcabe.add(cLabel4, null);
    Pcabe.add(acc_impokgE, null);
    Pcabe.add(cLabel18, null);
    Pcabe.add(pcc_numeE, null);
    Pcabe.add(eje_numeE, null);
    Pcabe.add(BbusPed, null);
    cPanel1.add(cLabel15, null);
    cPanel1.add(pcl_comenE, null);
    cPanel1.add(opBloquea, null);
    ArrayList v2=new ArrayList();
    v2.add("Producto"); // 0
    v2.add("Nombre"); // 1
    v2.add("Fec.Cad"); // 2
    v2.add("Un.Ped."); // 3
    v2.add("KG Ped."); // 4
    v2.add("Prec.Ped."); // 5
    v2.add("Mon."); // 6
    v2.add("Coment"); // 7
    jtPed.setCabecera(v2);
    jtPed.setAnchoColumna(new int[]
                       {60, 200,80, 46, 60, 55,  80, 150});
    jtPed.setAlinearColumna(new int[]
                         {2, 0, 1,2, 2, 2, 1, 0});
    
    jtPed.setAjustarGrid(true);
    jtPed.setFormatoColumna(4, "###,##9.99");
    jtPed.setFormatoColumna(5, "##9.99");
    jtPed.setAjustarColumnas(false);
//    Ppedido.setVisible(false);
    Phist.setLayout(new java.awt.BorderLayout());
    Phist.add(jtHist, java.awt.BorderLayout.CENTER);

    
    Palbar.add(jtDes,     new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    Palbar.add(jt,     new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    Palbar.add(cPanel1,     new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 21, 0));
//    Pprinc.add(Ppedido,   new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
//            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    Pprinc.add(Tpanel1,   new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

    Tpanel1.addTab( "Albaran",Palbar);
    Tpanel1.addTab( "Pedido",Ppedido);
    Tpanel1.addTab( "Reclamac.",PVerted);
    Tpanel1.addTab( "Más Datos",Potros);
    Tpanel1.addTab("Historico", Phist);
    Ppedido.add(jtPed,   new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 0, 0, 0), 0, 0));
    Ppedido.add(cLabel27,   new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 2, 2, 0), 10, 5));
    Ppedido.add(pcc_comenE,   new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
    

    Ptotal.add(opAgrupar, null);
    Ptotal.add(opImpEti, null);
    Ptotal.add(BimpEti, null);
    Ptotal.add(Birgrid, null);
    Ptotal.add(opAutoClas, null);
    Ptotal.add(cll_codiE, null);
    Ptotal.add(cLabel2, null);
    Ptotal.add(cLabel9, null);
    Ptotal.add(kilosE, null);
    Ptotal.add(cLabel8, null);
    Ptotal.add(nLinE, null);
    Ptotal.add(imptotE, null);
    Ptotal.add(cLabel3, null);
    Ptotal.add(cLabel11, null);
    Ptotal.add(kilostE, null);
    Ptotal.add(cLabel10, null);
    Ptotal.add(Bcancelar, null);
    Ptotal.add(Baceptar, null);
    Ptotal.add(opVerPrecios, null);
    Ptotal.add(opActFra, null);
    Ptotal.add(opIncPortes, null);
    Ptotal.add(Bdesagr, null);
    Pcabe.add(acc_portesE, null);
    Pcabe.add(emp_codiE, null);
    Pcabe.add(fcc_numeE, null);
    Pcabe.add(fcc_numeL, null);
    Pcabe.add(fcc_anoE, null);
    Pcabe.add(cLabel12, null);
    Pcabe.add(emp_codiL, null);
    Pcabe.add(prv_codiE, null);
    Pcabe.add(cLabel20, null);
    Pcabe.add(alm_codiE, null);
    Pcabe.add(Ppedi, null);
    frProd.getContentPane().add(cPanel2, BorderLayout.CENTER);
    cPanel2.add(cLabel19, null);
    cPanel2.add(pro_codcamE, null);
    cPanel2.add(BcanCam, null);
    cPanel2.add(BaceCam, null);
    cPanel2.add(opRepEti, null);
    frChFeEn.getContentPane().add(cPanel3, BorderLayout.CENTER);
    cPanel3.add(acc_fecrecL2, null);
    cPanel3.add(acc_fecrecL3, null);
    cPanel3.add(ferefiE, null);
    cPanel3.add(ferepoE, null);
    cPanel3.add(acc_fecrecL1, null);
    cPanel3.add(fereinE, null);
    cPanel3.add(Baccafe, null);
    cPanel3.add(Bcacafe, null);
    confGridRecl();
    PVerted.add(jtRecl,  new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 0, 0, 1), 0, 0));
    PVerted.add(PVertInf,    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    PVertInf.add(cLabel22, null);
    cPanel4.add(estIniE, null);
    cPanel4.add(cLabel25, null);
    cPanel4.add(BvertSala, null);
    cPanel4.add(estFinE, null);
    cPanel4.add(cLabel24, null);
    PVertInf.add(vertImporE, null);
    PVertInf.add(cLabel26, null);
    PVertInf.add(cPanel4, null);
    PVertInf.add(vertKilosE, null);
    PVertInf.add(vertNPiezE, null);
    PVertInf.add(cLabel23, null);
    
    Potros.add(acc_obserE, null);
    Potros.add(acc_obserL, null);
    Potros.add(prv_codiL1, null);
    Potros.add(acc_copvfaE, null);
    Potros.add(usu_nombL, null);
    Potros.add(usu_nombE, null);
    Potros.add(frt_ejercL, null);
    Potros.add(frt_ejercE, null);
    Potros.add(frt_numeE, null);
    acc_totfraE.addItem("Si", "-1");
    acc_totfraE.addItem("No", "0");
    
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
  private void confGridLin() throws Exception
  {
    ArrayList<String> v = new ArrayList();
    v.add("N.Lin"); // 0
    v.add("Prod."); // 1
    v.add("Descripcion"); // 2
    v.add("Unid"); // 3
    v.add("Kgs.Alb"); // 4
    v.add("Prec.Alb"); // 5
    v.add("Recorte"); // 6
    v.add("Un.Ped"); // 7
    v.add("kg.Ped"); // 8
    v.add("Prec.Ped"); // 9
    v.add("Dif.Un."); // 10
    v.add("Dif.Kg"); // 11
    v.add("Dif.Prec"); // 12
    v.add("Coment"); // 13
    v.add("PP"); // 14  
    v.add("Dto.PP");// 15 
    jt.setCabecera(v);
    jt.setAnchoColumna(new int[]{40,50,200,30,50,50,40,30,50,50,30,50,50,150,30,40});
    jt.setAlinearColumna(new int[]
                         {2,2, 0, 2, 2, 2, 2,2, 2, 2, 2, 2, 2,0,1,2});
    ArrayList vp=new ArrayList();
    vp.add("Numero Linea"); // 0
    vp.add("Prod."); // 1
    vp.add("Descripcion"); // 2
    vp.add("Unidades"); // 3
    vp.add("Kgs. Albaran"); // 4
    vp.add("Precio Albaran"); // 5
    vp.add("Recorte"); // 6
    vp.add("Unidades Pedido"); // 7
    vp.add("Kg. Pedido"); // 8
    vp.add("Precio Pedido"); // 9
    vp.add("Diferencia Unidades"); // 10
    vp.add("Diferencia Kg entre pedido y albaran"); // 11
    vp.add("Diferencia Precio entre pedido y albaran"); // 12
    vp.add("Comentario"); // 13
    vp.add("Portes Pagados"); // 14  
    vp.add("Dto.Pronto Pago");// 15 
    jt.setToolTipHeader(vp);

//    jt.setFormatoColumna(3, "##9");
//    jt.setFormatoColumna(4, "---,--9.99");
//    jt.setFormatoColumna(5, "---9.999");
//    jt.setFormatoColumna(6, "---9.9");
//    jt.setFormatoColumna(7, "##9");
//    jt.setFormatoColumna(8, "---,--9.99");
//    jt.setFormatoColumna(9, "---9.99");
//
//    jt.setFormatoColumna(10, "##9");
//    jt.setFormatoColumna(11, "---,--9.9");
//    jt.setFormatoColumna(12, "---9.99");
//    jt.setFormatoColumna(14, "B1-");
//    jt.setFormatoColumna(JT_DTOPP,"#9.99");
    jt.setToolTipText(ROOT);
    ArrayList vc = new ArrayList();
    pro_codiE.setProNomb(null);
    if (! ARG_ADMIN)
        acp_numindE.setEnabled(false);
    acl_cantiE.setName("acl_canti");
    acl_cantiE.setEnabled(false);
    acl_numcajE.setEnabled(false);
//    acl_numcajE.setEditable(false);
//    acl_cantiE.setEditable(false);
    pro_nombE.setEnabled(false);
    pcl_nucapeE.setEnabled(false);
    pcl_cantpeE.setEnabled(false);
    pcl_precpeE.setEnabled(false);
    dif_nucapeE.setEnabled(false);
    dif_cantpeE.setEnabled(false);
    dif_precpeE.setEnabled(false);
    if (ARG_MODPRECIO)
        acl_porpagE.setEnabled(true);
    acl_porpagE.setSelected(false);
    acl_nulinE.setEnabled(false);
    vc.add(acl_nulinE); // 0
    vc.add(pro_codiE.getTextField()); // 1
    vc.add(pro_nombE); // 2
    vc.add(acl_numcajE); // 3
    vc.add(acl_cantiE); // 4
    vc.add(acl_prcomE); // 5
    vc.add(acl_kgrecE); // 6
    vc.add(pcl_nucapeE); // 7
    vc.add(pcl_cantpeE); // 8
    vc.add(pcl_precpeE); // 9
    vc.add(dif_nucapeE); // 10
    vc.add(dif_cantpeE); // 11
    vc.add(dif_precpeE); // 12
    vc.add(acl_comenE); // 13
    vc.add(acl_porpagE); // 14
    vc.add(acl_dtoppE); // 15

    jt.setCampos(vc);
    jt.setFormatoCampos();
    jt.setMaximumSize(new Dimension(725, 137));
    jt.setMinimumSize(new Dimension(725, 137));
    jt.setPreferredSize(new Dimension(725, 137));
    jt.setAjustarGrid(true);
    jt.setAjustarColumnas(false);
   
  }

    @Override
  public void afterConecta() throws SQLException, java.text.ParseException
  {
    acc_cerraE.addItem("Si","-1");
    acc_cerraE.addItem("No","0");
    emp_codiE.iniciar(dtStat,this,vl,EU);
    gnu.chu.anjelica.pad.pdnumeracion.llenaSeriesAlbCom(acc_serieE);
    prv_codiE.iniciar(dtStat, this, vl, EU);
    acc_copvfaE.iniciar(dtStat, this, vl, EU);
    pro_codiE.iniciar(dtStat, this, vl, EU);
    pro_codverE.iniciar(dtStat, this, vl, EU);
    rgs_clidevE.setCliNomb(null);
    rgs_clidevE.iniciar(dtStat,this,vl,EU);
    s = "SELECT alm_codi,alm_nomb FROM v_almacen ORDER BY alm_nomb";
    dtCon1.select(s);
    alm_codiE.addItem(dtCon1);
    sbe_codiE.iniciar(dtStat,this,vl,EU);
    sbe_codiE.setFieldEmpCodi(emp_codiE.getTextField());
    sbe_nombL = sbe_codiE.creaLabelSbe();
    sbe_codiE.setLabelSbe(sbe_nombL);
    strSql = getStrSql();
    try
    {
          confGridDesglose();
    } catch (Exception ex)
    {
          Error("Error al configurar grid Desglose",ex);
    }
  }

    @Override
  public void iniciarVentana() throws Exception
  {

    pRegAlm= new paregalm();
    pRegAlm.iniciar(EU, dtStat, dtAdd, vl, this,dtCon1);
    if (! gnu.chu.anjelica.pad.pdconfig.getConfiguracion(EU.em_cod,dtStat,lkConfig))
    {
      fatalError("Configuracion para empresa por defecto: " + EU.em_cod + " NO ENCONTRADA");
      return;
    }
    pro_codiE.pro_codiE.setToolTipText("Boton derecho para otras opciones sobre este campo");

    pro_codcamE.iniciar(dtStat,frProd,vl,EU);

    Baceptar.setText("Aceptar(F4)");
    Pcabe.setDefButton(Baceptar);
    Pcabe.setButton(KeyEvent.VK_F4,Baceptar);
    jt.setButton(KeyEvent.VK_F4,Baceptar);
    jtDes.setButton(KeyEvent.VK_F4,Baceptar);

    Pcabe.setButton(KeyEvent.VK_F2,Birgrid);
    jt.setButton(KeyEvent.VK_F2,Birgrid);
    jtDes.setButton(KeyEvent.VK_F2,Birgrid);
    jtDes.setButton(KeyEvent.VK_F6,Bdesagr);
    jtDes.setButton(KeyEvent.VK_F9,BimpEti);

    acc_cerraE.setColumnaAlias("acc_cerra");
    emp_codiE.setColumnaAlias("emp_codi");
    acc_anoE.setColumnaAlias("acc_ano");
    acc_serieE.setColumnaAlias("acc_serie");
    acc_numeE.setColumnaAlias("acc_nume");
    prv_codiE.setColumnaAlias("prv_codi");
    acc_copvfaE.setColumnaAlias("acc_copvfa");

    acc_fecrecE.setColumnaAlias("acc_fecrec");
    usu_nombE.setColumnaAlias("usu_nomb");
    fcc_anoE.setColumnaAlias("fcc_ano");
    fcc_numeE.setColumnaAlias("fcc_nume");
    acc_obserE.setColumnaAlias("acc_obser");
    acc_impokgE.setColumnaAlias("acc_impokg");
    acc_portesE.setColumnaAlias("acc_portes");
    acc_totfraE.setColumnaAlias("acc_totfra");
    sbe_codiE.setColumnaAlias("sbe_codi");
   
    s = "SELECT div_codi,div_codedi FROM v_divisa order by div_codedi";
    if (!dtStat.select(s))
      throw new SQLException("NO HAY NINGUNA DIVISA DEFINIDA");
    div_codiE.addItem(dtStat);
    stkPart=new actStkPart(dtAdd,EU.em_cod);
    String fecinve=actStkPart.getFechaUltInv(0,0,null,dtStat);
    if (fecinve==null)
       fecinve = "01-01-" + EU.ejercicio; // Buscamos desde el principio del a�o.
    feulin=Formatear.getDate(fecinve,"dd-MM-yyyy");
    MIchangeProd = new JMenuItem("Cambiar Ref.", Iconos.getImageIcon("duplicar"));
    MIactNombre =  new JMenuItem("Actual.Descr.", Iconos.getImageIcon("reload"));
    MIimprEtiq =   new JMenuItem("Impr.Etiq.", Iconos.getImageIcon("print"));
    pro_codiE.getPopMenu().add(MIchangeProd,0);
    pro_codiE.getPopMenu().add(MIactNombre,1);
    pro_codiE.getPopMenu().add(MIimprEtiq,2);
    activarEventos();

    activar(false);
    verDatos(dtCons);
  }

  void activarEventos()
  {
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
    acp_nucrotE.addKeyListener(new KeyAdapter()
    {
            @Override
      public void keyPressed(KeyEvent e)
      {
        if (e.getKeyCode() == KeyEvent.VK_F3)
          genNumCrotal();
      }
    });
    acc_dtoppE.addFocusListener(new FocusAdapter()
    {
      @Override
      public void focusLost(FocusEvent e)
      {
          if (!acc_dtoppE.hasCambio() || nav.getPulsado()==navegador.ADDNEW || nav.getPulsado()==navegador.DELETE)
              return;
          actualDtoPP();
          acc_dtoppE.resetCambio();  
      }
    });
   
    acc_serieE.addFocusListener(new FocusAdapter()
    {

            @Override
      public void focusLost(FocusEvent e)
      {
        boolean enab = acc_serieE.getText().equals("Y");
        avc_numeE.setEnabled(enab);
        avc_anoE.setEnabled(enab);
      }
    });
    BvertSala.addActionListener(new ActionListener()
    {
            @Override
      public void actionPerformed(ActionEvent e)
      {
        BvertSala_actionPerformed();
      }
    });

    Tpanel1.addChangeListener(new ChangeListener()
    {
            @Override
      public void stateChanged(ChangeEvent e)
      {
//                System.out.println("Cambio: "+Tpanel1.getSelectedIndex());
        if (Tpanel1.getSelectedIndex() == 2 && jtRecl.isEnabled())
        {
          if (jtRecl.isVacio())
            jtRecl.requestFocusInicioLater();
        }
      }
    });

    Bdesagr.addActionListener(new ActionListener()
    {
            @Override
      public void actionPerformed(ActionEvent e)
      {
        Bdesagr_actionPerformed();
      }
    });
    acc_totfraE.addActionListener(new ActionListener()
    {
            @Override
      public void actionPerformed(ActionEvent e)
      {
        if (! acc_totfraE.isEnabled())
          return;
        if (nav.pulsado == navegador.NINGUNO)
          actTotFra();
      }
    });
    acc_cerraE.addActionListener(new ActionListener()
    {
            @Override
      public void actionPerformed(ActionEvent e)
      {
        if (!acc_cerraE.isEnabled())
          return;
        if (nav.pulsado == navegador.NINGUNO)
          actCerrado();
      }
    });

    alm_codiE.addActionListener(new ActionListener()
    {
            @Override
      public void actionPerformed(ActionEvent e)
      {
        if (!alm_codiE.isEnabled())
          return;
        if (nav.pulsado == navegador.NINGUNO)
          cambiarAlmacen();

      }
    });

    opVerPrecios.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        if (nav.pulsado==navegador.NINGUNO)
          verDatos(dtCons);
      }
    });
    BaceCam.addActionListener(new ActionListener()
    {
            @Override
      public void actionPerformed(ActionEvent e)
      {
        BaceCam_actionPerformed();
      }
    });
    BcanCam.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        BcanCam_actionPerformed();
        mensajeErr("Cancelado .... CAMBIO DE REFERENCIA");
      }
    });
    Baccafe.addActionListener(new ActionListener()
    {
       @Override
        public void actionPerformed(ActionEvent e)
        {
          Baccafe_actionPerformed();
        }
    });
    Bcacafe.addActionListener(new ActionListener()
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          Bcacafe_actionPerformed();
          mensajeErr("Cancelado .... CAMBIO DE FECHAS");
        }
    });

    BCamFeEnt.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        BCamFeEnt_actionPerformed();
      }
    });
    MIactNombre.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        if (nav.pulsado != navegador.EDIT && nav.pulsado != navegador.ADDNEW )
        {
          msgBox("Solo se puede Actualizar el Nombre del Producto si se esta editando el registro");
          return;
        }
        try {
          MIactNombreProd(jt.getSelectedRow());
        } catch (Exception k)
        {
          Error("Error al actualizar nombre de producto",k);
        }
      }
    });
    MIimprEtiq.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        imprEtiLin();
      }
    });
    MIchangeProd.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        if (nav.pulsado != navegador.NINGUNO )
        {
          msgBox("Solo se puede cambiar de referencia si NO se esta editando el registro");
          return;
        }
        if (opAgrupar.isSelected())
        {
          msgBox("Lineas NO pueden estar agrupadas para cambiar la referencia del producto");
          return;
        }

        cambiaRefProd();
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

    BbusPed.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        BbusPed_actionPerformed();
      }
    });
    opAgrupar.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        verDatos(dtCons);
      }
    });
    opIncPortes.addActionListener(new ActionListener()
    {
        @Override
      public void actionPerformed(ActionEvent e)
      {
        verDatos(dtCons);
      }
    });

    pcc_numeE.addKeyListener(new KeyAdapter()
    {
            @Override
      public void keyPressed(KeyEvent e)
      {
        if (e.getKeyCode()==KeyEvent.VK_F3)
           BbusPed_actionPerformed();
      }
    });
    eje_numeE.addKeyListener(new KeyAdapter()
    {
            @Override
      public void keyPressed(KeyEvent e)
      {
        if (e.getKeyCode() == KeyEvent.VK_F3)
          BbusPed_actionPerformed();
      }
    });

    BimpEti.addActionListener(new ActionListener() {
        @Override
      public void actionPerformed(ActionEvent e) {
        try
        {
          if (jtDes.getValorInt(0)==0)
          {
            mensajeErr("La linea NO tiene N. Individuo asignado");
            jtDesRequestFocusSelected();
            return;
          }
          imprEtiq(jtDes.getSelectedRow(), jtDes.getValorInt(0));
          jtDesRequestFocusSelected();
        } catch (Exception k)
        {
          Error("Error al Imprimir Etiqueta",k);
        }
      }
    });
  
    Bulcabe.addFocusListener(new FocusAdapter() {
            @Override
      public void focusGained(FocusEvent e) {
       if (nav.pulsado==navegador.QUERY)
         return;
       try {
         irGridLin();
       } catch (Exception k)
       {
         Error("Error al ir al grid",k);
       }
      }
    });
    Birgrid.addActionListener(new ActionListener() {
            @Override
      public void actionPerformed(ActionEvent e) {
        irGrid();
      }
    });
    
    acl_prcomE.addFocusListener(new FocusAdapter()
    {
      @Override
      public void focusGained(FocusEvent e)
      {
//        ganaFocoPrCompra();
      }
    });
    jtDes.tableView.addMouseListener(new MouseAdapter()
    {
            @Override
      public void mouseClicked(MouseEvent e)
      {
        try
        {
          if (Birgrid.isEnabled())
          {
            if (!jtDes.isEnabled())
              irGridDes0();
          }
        }
        catch (Exception k)
        {
          Error("Error al Ir a Lineas de despiece", k);
        }
      }
    });
    jt.tableView.addMouseListener(new MouseAdapter()
    {
            @Override
      public void mouseClicked(MouseEvent e)
      {
        try{
          if (Birgrid.isEnabled())
          {
            if (!jt.isEnabled())
            {
              if (jt.getSelectedColumnDisab()==0)
              {
                jtDes.requestFocusSelected();//jtDes.getSelectedRow(),jtDes.getSelectedColumn());
                return;
              }
              irGridLin();
            }
          }
        }
        catch (Exception k)
        {
          Error("Error al Ir a Lineas de despiece", k);
        }
      }
    });

  }
  void ganaFocoPrCompra()
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
              && pro_codiE.getTipoLote() == 'V' && pro_codiE.isActivo())
          {
            javax.swing.SwingUtilities.invokeLater(new Thread()
            {
              @Override
              public void run()
              {
                irGridDes0();
              }
            });
          }
        }
        catch (Exception k)
        {
          Error("Error al Controlar Producto", k);
        }
  }
  private void actualDtoPP()
  {
      int res=mensajes.mensajeYesNo("Poner un "+acc_dtoppE.getValorDec()+" de Dto PP ?");
      if (res!=mensajes.YES)
      {
          mensajeErr("Mantenido Dto. PP anterior");
          acc_dtoppE.setValorDec(acc_dtoppE.getValorDecAnt());
          return;
      }
      try 
      {
          int nRow=jt.getRowCount();
          for (int n=0;n<nRow;n++)
          {
           jt.setValor(acc_dtoppE.getValorDec(),n,JT_DTOPP);
           actPrecioLinAlb(0,n, jt.getValorDec(n,JT_PRCOM),jt.getValBoolean(n,JT_PORPAG),jt.getValorDec(n,JT_DTOPP) );   
          }
//          actAcuTot();
//          actImporteAlb(dtAdd ,emp_codiE.getValorInt(),acc_anoE.getValorInt(),acc_serieE.getText(),
//              acc_numeE.getValorInt(),imptotE.getValorDec());
      } catch (SQLException k)
      {
          Error("Error al actualizar Descuento Pronto Pago",k);
      }
  }

  /**
   * Ejecutado cuando se pulsa el botón de desagrupar
   * Mueve un Individuo de Una linea del Albarán a otra.
   * @see Bdesagr
   */
  void Bdesagr_actionPerformed()
  {
    jtDes.setLineaEditable(true);

    int nColErr=cambiaLinDes(jtDes.getSelectedRow());
    if (nColErr>=0)
    {
      jtDes.requestFocus(jtDes.getSelectedRow(), nColErr);
      return;
    }

    if (jtDes.getValorInt(0) == 0)
    {
      mensajeErr("Linea NO tiene asignado ningún Individuo");
      jtDes.requestFocusSelected();
      return;
    }
    int nuLiAlbAnt=jt.getValorInt(0); // Numero linea original.
    int nLin = jt.getSelectedRowDisab();
    int nCol=jt.getColumnCount()-5;
    if (jt.getSelectedRowDisab() == jt.getSelectedRow())
    { // Inserto Linea al Final y la seleciono
      ArrayList v = new ArrayList();
      v.add("0"); // 0
      v.add("" + jt.getValorInt(1)); // 1
      v.add(jt.getValString(2)); //2
      for (int n1 = 0; n1 < nCol; n1++)
        v.add("" + 0);
      v.add(false); // Portes Pagados
      v.add(acc_dtoppE.getValorDec() );
      jt.addLinea(v);
      jt.requestFocusFinal();
      nLin=jt.getRowCount()-1;
    }

    try
    {
      int nLiAlb=jt.getValorInt(nLin,0);
      if (nLiAlb == 0)
        nLiAlb = getNextLinAlb();
      if (jt.getValorInt(nLin, 1) != jt.getValorInt(1))
      {
        mensajeErr("Productos de linea origen y linea destino NO pueden ser diferentes");
        jtDes.requestFocusSelected();
        return;
      }

      // Cambio la linea de Albaran a la selecionada. Actualizo Numero Linea de Desglose
      int nLiAlDe=getNumLinDes(nLiAlb);
      s="UPDATE v_albcompar set acl_nulin = "+nLiAlb+","+
          " acp_numlin = "+nLiAlDe+
          " WHERE acc_ano = " + acc_anoE.getValorInt() +
          " AND emp_codi = " + emp_codiE.getValorInt() +
          " and acc_serie = '" + acc_serieE.getText() + "'" +
          " and acc_nume = " + acc_numeE.getValorInt() +
          " and acp_numlin = " + jtDes.getValorInt(JTD_NUMLIN)+
          " and acl_nulin = "+ nuLiAlbAnt;
//      debug("s: "+s);
      int nLiAfe=dtAdd.executeUpdate(s);
      if (nLiAfe!=1)
      {
        msgBox("No SE PUDO Cambiar Linea de Desglose. Lineas Afectadas: "+nLiAfe);
        return;
      }
      if (jt.getValorInt(nLin,0)==0)
      { // Inserto Linea de albaran en albacol y pongo linea en grid
        guardaLinAlb(nLiAlb, nLin, 0,0, 0, 0, 0, "",0);
        jt.setValor(""+nLiAlb,nLin,0);
      }

      actKgLinAlb(nLiAlb,nLin);
      actKgLinAlb(nuLiAlbAnt,jt.getSelectedRow());
      jtDes.setValor("0",0);
      jtDes.Bborra.doClick();

//      jtDes.removeLinea();
      ctUp.commit();
//      borraInd(jtDes.getSelectedRow(),jtDes.getValorInt(0));
    }
    catch (SQLException k)
    {
      Error("Error al Cambiar Individuo de Linea", k);
    }
  }

  void actKgLinAlb(int nLiAlb, int nLinGrid) throws SQLException
  {
    getAcumLin(nLiAlb);
    jt.setValor(""+dtStat.getDouble("acp_canti",true),nLinGrid,4);
    jt.setValor(""+dtStat.getInt("cuantos",true),nLinGrid,3);

    s = "UPDATE v_albacol set acl_numcaj=" + dtStat.getInt("cuantos", true) + ","+
        "acl_canti = " + dtStat.getDouble("acp_canti", true) +
        " WHERE acc_ano = " + acc_anoE.getValorInt() +
        " and emp_codi = " + emp_codiE.getValorInt() +
        " and acc_nume = " + acc_numeE.getValorInt() +
        " and acc_serie = '" + acc_serieE.getText() + "'" +
        " and acl_nulin = " + nLiAlb;
    dtAdd.executeUpdate(s,stUp);
  }
  
  void cambioPrv()
  {    
      cambioPrv(false);
  }
  
  
  /**
   * Comprueba que los datos de la cabecera del albaran son validos
   * @throws SQLException
   * @return boolean True son validos. False hay algun tipo de error
   */
  private boolean checkCabecera() throws SQLException
  {
    if (!emp_codiE.controla())
    {
      mensajeErr("Empresa NO valida");
      return false;
    }
    if (!prv_codiE.controlar())
    {
      mensajeErr(prv_codiE.getMsgError());
      return false;
    }
    s = pdejerci.checkFecha(dtStat, acc_anoE.getValorInt(), emp_codiE.getValorInt(), acc_fecrecE.getText());
    if (s != null)
    {
      mensajeErr(s);
      acc_fecrecE.requestFocus();
      return false;
    }

    if (prv_codiE.isInterno())
    {
      if (!acc_serieE.getValor().equals("Y"))
      {
        mensajeErr("PROVEEDOR NO ESTA MARCADO COMO INTERNO. SE DEBE UTILIZAR LA SERIE Y");
        prv_codiE.requestFocus();
        return false;
      }
      if (avc_anoE.getValorInt()==0)
      {
        mensajeErr("Introduzca el a�o del Albaran de Venta");
        avc_anoE.requestFocus();
        return false;
      }
      if (avc_numeE.getValorInt() == 0)
      {
        mensajeErr("Introduzca el N�mero del Albaran de Venta");
        avc_anoE.requestFocus();
        return false;
      }
    }
    else
    {
      if (acc_serieE.getValor().equals("Y"))
      {
        mensajeErr("ESTE PROVEEDOR NO ESTA MARCADO COMO INTERNO");
        prv_codiE.requestFocus();
        return false;
      }
    }


    if (!acc_copvfaE.controlar())
    {
      mensajeErr(acc_copvfaE.getMsgError());
      return false;
    }
    if (!sbe_codiE.controla(true))
    {
      mensajeErr("SubEmpresa NO valida");
      return false;
    }
    if (pcc_numeE.isEnabled())
    {
      if (!cargaLinPedido(true))
      {
        pcc_numeE.requestFocus();
        return false;
      }
    }
    if (avc_numeE.getValorInt() != 0)
    { // A buscar el albaran de venta.
      if (! pdalbara.getAlbaranCab(dtStat, emp_codiE.getValorInt(), avc_anoE.getValorInt(), EntornoUsuario.SERIEY,
                                 avc_numeE.getValorInt()))
      {
        mensajeErr("Albaran de Ventas NO encontrado");
        avc_anoE.requestFocus();
        return false;
      }
//      s="SELECT  pro_codi FROM v_albavel WHERE emp_codi = "+emp_codiE.getValorInt()+
//          " AND avc_ano = "+avc_anoE.getValorInt()+
//          " and avc_serie = '" + EU.SERIEY+ "'" +
//          " and avc_nume = " + avc_numeE.getValorInt()+
//          " and avl_canti != 0 "+
//          " and avl_prven = 0 ";
//      if (dtStat.select(s))
//      {
//        mensajeErr("Este albaran  de venta tiene el producto: "+dtStat.getInt("pro_codi")+ " SIN poner precios");
//        avc_anoE.requestFocus();
//        return false;
//      }

      if (isAlbCompra(dtStat, emp_codiE.getValorInt(), avc_anoE.getValorInt(), avc_numeE.getValorInt(),false))
      {
        if (dtStat.getInt("acc_nume")!=acc_numeE.getValorInt())
        {
          mensajeErr("Albaran de venta ya utilizado en compra: " + dtStat.getInt("acc_nume") + " DE fecha: " +
                     dtStat.getFecha("acc_fecrec", "dd-MM-yyyy"));
          avc_anoE.requestFocus();
          return false;
        }
      }

    }
    return true;
  }

  public static boolean isAlbCompra(DatosTabla dt, int empCodi, int avcAno,
                                    int avcNume,boolean bloquea) throws SQLException
  {
    return dt.select("SELECT * FROM v_albacoc WHERE emp_codi = "+empCodi+
       " and acc_serie = '" + EntornoUsuario.SERIEY+ "'" +
       " and avc_ano = "+avcAno+
       " and avc_nume = "+avcNume,bloquea);
  }

  void irGridLin() throws Exception
  {
    if (swCargaAlb)
      return;
    if (!jtDes.isEnabled())
    { // Vengo de la cabecera
      if (!checkCabecera())
        return;
//      if (acc_serieE.getText().equals(EntornoUsuario.SERIEY))
//      {
//        cargaALbVentas();
//        return;
//      }
      if (pcc_numeE.isEnabled())
      {
        if (!cargaLinPedido(false))
        {
          pcc_numeE.requestFocus();
          return;
        }
      }
      else
        Bcancelar.setEnabled(false);

      alm_codiE.setEnabled(true);
      Baceptar.setEnabled(true);
      jt.setEnabled(true);
      Bdesagr.setEnabled(false);

      jt.requestFocusInicio();
      pro_codiE.setEditable(nLinE.getValorInt()==0);
//      alm_codiE.setEditable(nLinE.getValorInt()==0);
      pro_codiE.getNombArt(jt.getValString(0,JT_PROCOD));
      setEditCant(pro_codiE.getTipoLote());
      pro_codiE.resetCambio();
    }
    else
    { // Vengo de Las lineas de individuo
      procLineaInd();
    }
  }
  /**
   * Procesa linea de individuos. (desglose) y  va a lineas de productos.
   * 
   */
  private boolean procLineaInd()
  {
      int nCol;
      jtDes.salirGrid();
      nCol = cambiaLinDes(jtDes.getSelectedRow());
      if (nCol >= 0)
      {
        jtDes.requestFocus(jtDes.getSelectedRow(), nCol);
        return false;
      }
      if (jt.getSelectedColumn() == 5 && ARG_MODPRECIO)
        swGridDes++;
      actAcuTot();
//      javax.swing.SwingUtilities.invokeLater(new Thread()
//      {
//        public void run()
//        {
          pro_codiE.setEditable(jt.getValorDec(3)==0);
//          alm_codiE.setEditable(jt.getValorDec(3)==0);
          opAutoClas.setEnabled(false);
          jtDes.setEnabled(false);
          jt.setEnabled(true);
//          Baceptar.setEnabled(true);
          BimpEti.setEnabled(false);
          Bdesagr.setEnabled(false); 
          if (ARG_MODPRECIO)
            jt.requestFocusSelectedLater();
          else
            jt.requestFocusLater(jt.getSelectedRow(), 1);

          if (!opAutoClas.isSelected())
            return true;
          try
          {
            actAcuLomos();
            double prCompra=jt.getValorDec( JT_PRCOM);
            prCompra-=jt.getValorDec( JT_PRCOM)*
                      jt.getValorDec(JT_DTOPP) / 100;
            // Se actualiza otra vez el desglose de linea, por si se han metido
            // productos que se han ido autoclasificando.
            verDesgLinea(emp_codiE.getValorInt(), acc_anoE.getValorInt(),
                         acc_serieE.getText(), acc_numeE.getValorInt(),
                         jt.getValorInt(JT_NLIN),
                         false,
                         jt.getValorInt(JT_PROCOD),jt.getValorDec(JT_KILALB),prCompra);
          }
          catch (Exception k)
          {
            Error("Error al Actualizar Acumulados de Lomos", k);
          }
//        }
//
//      });
      return true;
  }
  /**
   * Actualiza acumulados
   */
  private void actAcuTot()
  {
    int nCol=jt.getRowCount();
    double kilosT=0,impTot=0,impLin;
    for (int n=0;n<nCol;n++)
    {
      kilosT+=jt.getValorDec(n,JT_KILALB);
      impLin=jt.getValorDec(n,JT_PRCOM);
      if (opIncPortes.isSelected() && ! acl_porpagE.isSelected())
          impLin-=acc_impokgE.getValorDec(); // Saco el precio sin portes
      impLin-=impLin*jt.getValorDec(n,JT_DTOPP)/100;
      if (opIncPortes.isSelected() && ! acl_porpagE.isSelected())
          impLin+=acc_impokgE.getValorDec();// Le vuelvo a sumar los portes.
      impTot+=impLin* jt.getValorDec(n,JT_KILALB);
    }
    kilostE.setValorDec(kilosT);
    imptotE.setValorDec(impTot);
  }
  /**
   * Actualizar Acumulados de Lineas de albaran cuando se pone la opcion
   * de clasificación Automaticas
   * @throws SQLException error BD
   * @throws ParseException error BD
   */
  private void actAcuLomos() throws SQLException, ParseException
  {
    int cllCodi=cll_codiE.getValorInt();

    for (int n=0;n<jt.getRowCount();n++)
    {
      s="SELECT * FROM claslomos WHERE pro_codi = "+jt.getValorInt(n,1)+
          " and cll_codi = "+cllCodi;
      if (! dtStat.select(s))
        continue; // Si el codigo Prod. no esta en Clas. Lomos no actualizo
      getAcumLin(jt.getValorInt(n,0));

      jt.setValor(""+dtStat.getDouble("acp_canti",true),n,4);
      jt.setValor(""+dtStat.getInt("cuantos",true),n,3);
      dtAdd.executeUpdate("update v_albacol  set acl_canti = "+dtStat.getDouble("acp_canti",true)+
              ", acl_numcaj ="+dtStat.getInt("cuantos",true)+
              "where "+dtStat.getCondWhere());
    }
  }
  /**
   * Busca los kilos acumulaods en una linea en particular del albaran.
   * @param nLinAlb Numero de Linea del Albaran
   * @throws java.sql.SQLException
   */
  void getAcumLin(int nLinAlb) throws SQLException
  {
    s = "SELECT sum(acp_canti) as acp_canti,count(*) as cuantos " +
        " FROM v_albcompar WHERE acc_ano = " + acc_anoE.getValorInt() +
        " AND emp_codi = " + emp_codiE.getValorInt() +
        " and acc_serie = '" + acc_serieE.getText() + "'" +
        " and acc_nume = " + acc_numeE.getValorInt() +
        " and acl_nulin = " + nLinAlb;
    dtStat.select(s);
  }

  int cambiaLinDes(int row) 
  {
    if (swCargaAlb || swCargaLin)
        return -1;
    try
    {
        if (acp_numindE.getValorInt() != numIndAnt && ARG_ADMIN)
        { // Han cambiado la linea. Comprobar q no existe ese individuo.
            if (getIndAlbcompar(-1, acp_numindE.getValorInt(), dtStat))
            {
                mensajeErr("Numero de individuo ya esta asignado en este albaran");
                return 0;
            }
        }
        if (acp_cantiE.getValorDec() == 0 && jtDes.getValorInt(row, DESNIND) == 0)
            return -1;

        int ret = cambiaLinDesg0(row);
        if (ret >= 0)
            return ret;

        int nLinAnt = numIndAnt;
        String linea = getLinGrDes(); // Un String con los datos concatenados del grid
        numIndAnt = nLinAnt;
        if (acc_numeE.getValorInt() == 0)
        {
            if (!checkCabecera())
            {
                msgBox("Error en condiciones de Albaran (Cabecera).\n" + getMensajeErr());
                return 0;
            }
        }

        if (acp_canindE.getValorDec() <= 0)
        {
            acp_canindE.setValorDec(1);
            mensajeErr("Introduzca Numero de Individuos");
            return JTD_CANIND;
        }

        if (!linea.equals(lineaAnt))
        { // Hubo cambios
            lineaAnt = linea;
//        jtDes.procesaAllFoco(row);
            try
            {
                guardaLinDes(row);
            } catch (NumberFormatException k1)
            {
                msgBox("Datos introducidos NO VALIDOS");
                return 0;
            }
            guardaUltValoresDesg();
        }
    } catch (Exception k)
    {
        Error("Error al Guardar Individuo en  Albaran de compra", k);
    }
    kgIndivAnt = acp_cantiE.getValorDec();
    acp_cantiE.resetCambio();
    return -1;
  }

  /**
   * Actualizar Acumulado en Linea de Albaran
   */
  void actAcuLiAlb()
  {
    int nRow=jtDes.getRowCount();
    int nInd=0;
    double kilos=0;
    for (int n=0;n<nRow;n++)
    {
      if (jtDes.getValorDec(n,1)==0)
        continue;
      nInd=nInd+jtDes.getValorInt(n,JTD_CANIND);
      kilos+=jtDes.getValorDec(n,1);
    }
    nLinE.setValorDec(nInd);
    kilosE.setValorDec(kilos);
  }

  
  /**
   * Retorna el Numero de Linea (del grid) donde esta un individuo
   *
   * @param nInd int Numero de Individuo
   * @throws SQLException Error en DB
   * @return int Numero de Linea. -1 si NO esta en ninguno.
   */
  int getLinProducto(int nInd) throws SQLException
  {
    dtStat.select("SELECT min(acl_nulin) as acl_nulin FROM v_albcompar " +
       " WHERE emp_codi = " + emp_codiE.getValorInt() +
       " AND acc_ano = " + acc_anoE.getValorInt() +
       " and acc_nume = " + acc_numeE.getValorInt() +
       " and acc_serie = '" + acc_serieE.getText() + "'" +
       " and acp_numind = "+nInd);
       
   if (dtStat.getObject("acl_nulin")==null)
     return -1;
   int nLin=dtStat.getInt("acl_nulin");
   for (int n = 0; n < jt.getRowCount(); n++)
   {
     if (jt.getValorInt(n, 0) == nLin)
       return n;
   }
   return -1;
  }
  /**
   * Devuelve numeros de crotal iguales sin tener en cuenta el individuo actual.
   * para un articulo en particulo
   * @param numCrot Numero de crotal a comprobar
   * @param nInd Individuo actual.
   * @param proCodi Codigo de Producto
   * @return Nº Crotales iguales.
   * @throws java.sql.SQLException
   */
  int getNumCrotal(String numCrot,int nInd,int proCodi) throws SQLException
  {
     s = "SELECT count(*) as numcrot FROM v_albcompar " +
       " WHERE emp_codi = " + emp_codiE.getValorInt() +
       " AND acc_ano = " + acc_anoE.getValorInt() +
       " and acc_nume = " + acc_numeE.getValorInt() +
       " and acc_serie = '" + acc_serieE.getText() + "'"+
       " and acp_numind != "+nInd+
       " and pro_codi = "+proCodi+
       " and acp_nucrot = '"+numCrot+"'";
     dtStat.select(s);
     return dtStat.getInt("numcrot");
  }
  /**
   * Devuelve la linea del grid donde insertar un producto autoclasificado
   * @param kilos double Kilos
   * @param creaLin boolean Puede Crear Linea ? TRUE=si
   * @throws SQLException Error en DB
   * @throws ParseException Error en sentencia SQL
   * @return int Numero de Linea donde insertarla.
   * -1 si Debe insertar linea pero se le ha mandadao creaLin=false
   */
  synchronized int getLinAutoClas(double kilos,boolean creaLin) throws SQLException,java.text.ParseException
  {
    if (cll_codiE.isVacio())
        llenaCllCodi();
    int proCodi=jt.getValorInt(1);
    int cllCodi=cll_codiE.getValorInt();
    if (!cll_codiE.isVacio())
    {
        s = "SELECT * FROM claslomos WHERE cll_kilos <= " + kilos+
            " and cll_codi = "+cllCodi+
            " order by cll_kilos desc";
        if (dtStat.select(s))
          proCodi = dtStat.getInt("pro_codi");
        else
          enviaMailError("No encontrada clasificacion para prod:"+proCodi+" Kilos: "+kilos+" Alb: "+avc_numeE.getValorInt());
    }
    else
        enviaMailError("cllCodi sigue estando vacio para prod:"+proCodi+" Alb: "+avc_numeE.getValorInt());
    int n;
    // Busco la primera linea en el grid donde exista el producto.
    for (n = 0; n < jt.getRowCount(); n++)
    {
      if (jt.getValorInt(n, 1) == proCodi)
        break;
    }
    if (n == jt.getRowCount())
    { // No existe el producto .. INSERTAR LINEA
      if (! creaLin)
        return -1;
  
      ArrayList v = jt.getLineaDefecto();
        
      v.set(0,0); // 0
      v.set(1, proCodi); // 1
      v.set(2,  pro_codiE.getNombArt("" + proCodi)); //2
      v.set(jt.getColumnCount()-1,acc_dtoppE.getValorDec() );
      jt.addLinea(v);
    }
    return n;
  }
  /**
   * Devuelve el proximo numero de linea a asignar a un albaran
   * @return num. Linea albaran
   * @throws java.sql.SQLException
   */
  int getNextLinAlb() throws SQLException
  {
    s = "select max(acl_nulin) as acl_nulin FROM v_albacol " +
        " WHERE acc_ano =" + acc_anoE.getValorInt() +
        " and emp_codi = " + emp_codiE.getValorInt() +
        " and acc_serie = '" + acc_serieE.getText() + "'" +
        " and acc_nume = " + acc_numeE.getValorInt();
    dtCon1.select(s);
    return dtCon1.getInt("acl_nulin", true) + 1;
  }

  public static boolean getLineaAlb(DatosTabla dt, int empCodi, int accAno,
                                    String accSerie, int accNume, int aclNulin,
                                    boolean bloquea) throws SQLException
  {
    String s = "select * FROM v_albacol " +
        " WHERE acc_ano =" + accAno +
        " and emp_codi = " + empCodi +
        " and acc_serie = '" + accSerie + "'" +
        " and acc_nume = " + accNume +
        " and acl_nulin = " + aclNulin;
    return dt.select(s, bloquea);
  }

/*  void borraRegLinAlb(int nLinea) throws SQLException,java.text.ParseException
  {
    s = "delete from v_albacol WHERE acc_ano = " + acc_anoE.getValorInt() +
          " and emp_codi = " + emp_codiE.getValorInt() +
          " and acc_nume = " + acc_numeE.getValorInt() +
          " and acc_serie = '" + acc_serieE.getText() + "'" +
          " and acl_nulin = " + jt.getValorInt(0);
      stUp.executeUpdate(s);
  }
*/

  /**
   * Guarda Linea despiece
   * @throws SQLException error en base datos
   * @throws java.text.ParseException error en sentencia select
   * @throws java.net.UnknownHostException error en SetBloqueo
   * @param row Linea en el grid que estoy tratando
   */
  void guardaLinDes(int row) throws SQLException,java.text.ParseException,java.net.UnknownHostException,NumberFormatException
  {
    if (acc_numeE.getValorInt() == 0)
      guardaCab();
    
    int nLiAlb;
    int nLiAlAnt;
//    int nRow=jtDes.getRowCount();
    int nIndiv;
    opAutoClas.setEnabled(false);
    nLiAlAnt=jt.getValorInt(0); // Numero linea anterior del grid de productos
    
    if (opAutoClas.isSelected())
    { // Autoclasificar
      int n=getLinAutoClas(acp_cantiE.getValorDec(),true);
      jt.setRowFocus(n);
      nLiAlAnt= getLinAutoClas(kgIndivAnt,false);
      if (nLiAlAnt>=0)
        nLiAlAnt=jt.getValorInt(nLiAlAnt,0);
    }
    double kgFac=0;// Kilos Facturados.
    double prLiAlb=0; // Precio de Compra
    double dtopp=0;
    if (jt.getValorInt(0)!=0)
    { // Modificando una linea del albaran. (NO de desglose)
      s = "SELECT *  from v_albacol WHERE acc_ano = " + acc_anoE.getValorInt() +
          " and emp_codi = " + emp_codiE.getValorInt() +
          " and acc_nume = " + acc_numeE.getValorInt() +
          " and acc_serie = '" + acc_serieE.getText() + "'" +
          " and acl_nulin = " + jt.getValorInt(0);
      if (dtStat.select(s))
      {
        kgFac = dtStat.getDouble("acl_canfac",true); // Kilos Facturados
        if (ARG_MODPRECIO)
        {
          prLiAlb=dtStat.getDouble("acl_prcom"); //+acc_impokgE.getValorDec();
          dtopp  = dtStat.getDouble("acl_dtopp");
        }
//        borraRegLinAlb(jt.getValorInt(0));
      }
      else
      { // No encontrada Linea de Albaran
        if (jf!=null)
        {
          jf.ht.clear();
          jf.ht.put("%s",s);
          jf.guardaMens("LC",jf.ht);
        }
      }
      nLiAlb=jt.getValorInt(0);
    }
    else
      nLiAlb=getNextLinAlb();
    actAcuLiAlb();
    
    guardaLinAlb(nLiAlb, jt.getSelectedRow(), nLinE.getValorInt(),
           kilosE.getValorDec(),kgFac,prLiAlb,acl_kgrecE.getValorDec() ,acl_comenE.getText(),dtopp);

    if (jtDes.getValorInt(row, DESNIND) == 0)
    { // Es un individuo nuevo
      int nLiAlDe=getNumLinDes(nLiAlb);

      nIndiv = utildesp.getMaxNumInd(dtCon1, jt.getValorInt(1),
                                     acc_anoE.getValorInt(),
                                     emp_codiE.getValorInt(),
                                     acc_serieE.getText(),
                                     acc_numeE.getValorInt());
      guardaLinDes(row, nLiAlDe, nLiAlb, nIndiv);
    }
    else // Ya existia el numero de individuo
      actGridDes(nLiAlb,row,jtDes.getValorInt(row,JTD_NUMLIN),jtDes.getValorInt(row,DESNIND),numIndAnt, nLiAlAnt);

    jt.setValor( nLiAlb, 0);
    if (! opAutoClas.isSelected() &&  ! cll_codiE.isNull())
    {
      jt.setValor( "" + nLinE.getValorInt(), 3);
      jt.setValor( "" + kilosE.getValorDec(),4);
    }
    else
      actAcuLomos();

    actAcuPro(jt.getValorInt(1));
    dif_unidE.setValorDec(alc_unidE.getValorInt() - pcl_unidE.getValorInt());
    dif_kgsE.setValorDec(alc_kgsE.getValorDec() - pcl_kgsE.getValorDec());
    dif_porcE.setValorDec(pcl_kgsE.getValorDec()!=0?dif_kgsE.getValorDec() / pcl_kgsE.getValorDec() * 100:0);
    ctUp.commit();
  }
  /**
   * Devuelve Proximo Numera linea de Desglose para una linea de ALBARAN
   * @param numLinAlb int NUMERO LINEA ALBARAN
   * @throws SQLException Error DB
   * @return int Proximo Num. Linea Desglose. -1 SI no la encuentra
   */
  int getNumLinDes(int numLinAlb) throws SQLException
  {
  s = "select max(acp_numlin) as acp_numlin " +
         "  FROM v_albcompar " +
         " WHERE acc_ano =" + acc_anoE.getValorInt() +
         " and emp_codi = " + emp_codiE.getValorInt() +
         " and acc_serie = '" + acc_serieE.getText() + "'" +
         " and acc_nume = " + acc_numeE.getValorInt() +
         " and acl_nulin= " + numLinAlb;
     if (! dtCon1.select(s))
       return -1;
     return dtCon1.getInt("acp_numlin", true) + 1; // N. Linea. Alb. Desp.
  }

  String getConservar(int codProd) throws SQLException
  {
    return gnu.chu.anjelica.pad.MantArticulos.getStrConservar(codProd,dtCon1);
  }
  
  void guardaLinAlb(int nLiAlb, int nLin,int nCaja,double kilos,
                    double kgFac,double prLiAlb,double kgRec,String coment,double dtopp) throws SQLException
  {
    guardaLinAlb(nLiAlb,nLin,nCaja, kilos, kgFac,prLiAlb, kgRec,prLiAlb,coment,dtopp);
  }
  /**
     * Inserta Linea de Albaran
     * @param nLiAlb int Numero Linea Albaran
     * @param nLin int Numero Linea en el Grid
     * @param nCaja int Numero de Cajas
     * @param kilos double Kilos
     * @param kgFac double Kilos  Facturados
     * @param prLiAlb double Precio Linea Albaran
     * @param kgRec double Kilos recepcionados
     * @param precStk double Precio de Stock (0 excepto para traspasos entre subempresas)
     * @param coment String Comentarios
     * @param dtoPP double dto pronto pago
     * @throws SQLException Error en DB
     * @throws ParseException Error al crear Sentencia SQL.
     */

  void guardaLinAlb(int nLiAlb, int nLin, int nCaja, double kilos,
                    double kgFac, double prLiAlb, double kgRec, double precStk, String coment,double dtopp) throws SQLException
  {
    s = "select * from v_albacol WHERE acc_ano = " + acc_anoE.getValorInt() +
       " and emp_codi = " + emp_codiE.getValorInt() +
       " and acc_nume = " + acc_numeE.getValorInt() +
       " and acc_serie = '" + acc_serieE.getText() + "'" +
       " and acl_nulin = " + nLiAlb;
    if (dtAdd.select(s,true))
    {
      dtAdd.edit(dtAdd.getCondWhere());
    }
    else
    {
      dtAdd.addNew("v_albacol");
      dtAdd.setDato("acc_ano", acc_anoE.getValorInt());
      dtAdd.setDato("emp_codi", emp_codiE.getValorInt());
      dtAdd.setDato("acc_serie", acc_serieE.getText());
      dtAdd.setDato("acc_nume", acc_numeE.getValorInt());
      dtAdd.setDato("acl_nulin", nLiAlb);
      dtAdd.setDato("acl_totfra",0);
    }
    dtAdd.setDato("pcc_nume",0);
    dtAdd.setDato("pro_codi",jt.getValorInt(nLin,1));
    dtAdd.setDato("acl_numcaj",nCaja);
    dtAdd.setDato("acl_tipdes","%");
    dtAdd.setDato("acl_porpag ",acl_porpagE.isEnabled()?0:acl_porpagE.isSelected()?-1:0);
    dtAdd.setDato("alm_codi",alm_codiE.getValor());

    dtAdd.setDato("acl_canti",kilos);
    dtAdd.setDato("acl_prcom",prLiAlb);
    dtAdd.setDato("acl_canfac",kgFac);
    dtAdd.setDato("acl_kgrec",kgRec); // Kilos de recorte
    dtAdd.setDato("acl_prstk",precStk);
    dtAdd.setDato("acl_comen",coment);
    dtAdd.setDato("acl_dtopp",dtopp);
    dtAdd.update(stUp);

  }
  /**
   * Guarda Cabecera Albarán Compra
   * @throws SQLException
   * @throws java.text.ParseException
   */
  void guardaCab() throws SQLException
  {
    acc_numeE.setValorDec(getNumAlb());
    acc_serieE.setEnabled(false);
    emp_codiE.setEnabled(false);
    acc_obserE.setEnabled(false);
    pcc_numeE.setEnabled(false);
    eje_numeE.setEnabled(false);
    prv_codiE.setEnabled(false);
    avc_numeE.setEnabled(false);
    avc_anoE.setEnabled(false);
    alm_codiE.setEnabled(false);
    prv_codiE.setEnabled(false);
    eje_numeE.setEnabled(false);
    acc_fecrecE.setEnabled(false);
    sbe_codiE.setEnabled(false);
//      sbe_codiE.setEnabled(false);

    dtAdd.addNew("v_albacoc");
    dtAdd.setDato("acc_ano",acc_anoE.getValorInt());
    dtAdd.setDato("emp_codi",emp_codiE.getValorInt());
    dtAdd.setDato("acc_serie",acc_serieE.getText());
    dtAdd.setDato("acc_nume",acc_numeE.getValorInt());
    dtAdd.setDato("prv_codi",prv_codiE.getValorInt());
    dtAdd.setDato("acc_fecrec",acc_fecrecE.getText(),"dd-MM-yyyy");
    dtAdd.setDato("usu_nomb",usu_nombE.getText());
    dtAdd.setDato("acc_copvfa",acc_copvfaE.getValorInt());
    dtAdd.setDato("fcc_ano",0);
    dtAdd.setDato("fcc_nume",0);
    dtAdd.setDato("acc_obser",acc_obserE.getText());
    dtAdd.setDato("acc_portes",acc_portesE.getValor());
    dtAdd.setDato("acc_impokg",acc_impokgE.getValorDec());
    dtAdd.setDato("acc_cerra",0); // Lo marco como NO cerrado
    dtAdd.setDato("acc_totfra",0); // NO facturado
    dtAdd.setDato("sbe_codi",sbe_codiE.getValorInt());
    dtAdd.setDato("avc_ano",avc_anoE.getValorInt());
    dtAdd.setDato("avc_nume",avc_numeE.getValorInt());

    dtAdd.update(stUp);
    setBloqueo(dtAdd,"v_albacoc", acc_anoE.getValorInt()+"|"+emp_codiE.getValorInt()+
                   "|"+acc_serieE.getText()+"|"+acc_numeE.getValorInt(),false);
  }
  /**
   * Busca No Alb. a Asignar.
   * @return proximo No. Albaran a asignar segun la serie
   * @throws SQLException
   *
   */
  int getNumAlb() throws SQLException

  {
    int nAlb;
    s = "SELECT num_secoma,num_secomb,num_secomc,num_secomd,num_secomy" +
        " FROM v_numerac WHERE emp_codi = " + emp_codiE.getValorInt() +
        " and eje_nume=" + acc_anoE.getValorInt();
    if (!dtAdd.select(s, true))
      throw new SQLException("NO encontrado GUIA Numeraciones para esta Empresa\n" + s);
    nAlb = dtAdd.getInt("num_secom" + acc_serieE.getText()) + 1;
    dtAdd.edit(dtAdd.getCondWhere());
    dtAdd.setDato("num_secom" + acc_serieE.getText(), nAlb);
    dtAdd.update(stUp);
    return nAlb;
  }
  
  /**
   * Busca los datos de un despiece en un vector
   * @param dat vector con los datos
   * @param nLin Numero de Linea con la que comparar
   * @return posicion donde lo ha encontrado.
   */
  int buscaDesp(Vector dat, int nLin)
  {
    int nl = dat.size();
    for (int n = 0; n < nl; n++)
    {
      Vector v = (Vector) dat.elementAt(n);
      if (igualInt(v.elementAt(1).toString(), jtDes.getValString(nLin, 0)) && // N� Ind.
          igualDouble(v.elementAt(2).toString(), jtDes.getValString(nLin, 1))) // Cantidad
      {
        v.set(0, "E");
        dat.set(n, v);
        return n;
      }
    }
    return -1;
  }
  boolean igualInt(String val1, String val2)
  {
    return Integer.parseInt(val1.trim()) == Integer.parseInt(val2.trim());
  }

  boolean igualDouble(String val1, String val2)
  {
    return Double.parseDouble(val1.trim()) == Double.parseDouble(val2.trim());
  }


  void irGrid()
  {
    if (swCargaAlb)
      return;
    try {
      if (!jt.isEnabled())
        irGridLin();
      else
        irGridDes0();
    } catch (Exception k)
    {
      Error("Error al ir al Grid",k);
    }
  }
  /**
   * LLena combo de tipos de clasificacion
   * @return true si tiene autoclasifacion para el producto activo.
   * @throws SQLException 
   */
  boolean llenaCllCodi() throws SQLException
  {
      s="SELECT distinct cll_codi as cll_codi FROM claslomos "+
         " WHERE pro_codi = "+pro_codiE.getValorInt();
      if (dtStat.select(s))
      {
        cll_codiE.setDatos(dtStat);
        cll_codiE.setEnabled(true);
        return true;
      }
      else
      {
        cll_codiE.removeAllItems();
        cll_codiE.resetTexto();
        cll_codiE.setEnabled(false);
        return false;
      }
  }
  /**
   * Comprueba si una linea del albaran de compra tiene diferentes clasificaciones.
   */
  boolean hasDiferentClas(int nLiAlb) throws SQLException
  {
      int cllCodi=cll_codiE.getValorInt();
      s = "SELECT * FROM v_albcompar " +
        " WHERE emp_codi = " + emp_codiE.getValorInt() +
        " AND acc_ano = " + acc_anoE.getValorInt() +
        " and acc_nume = " + acc_numeE.getValorInt() +
        " and acc_serie = '" + acc_serieE.getText() + "'" +
        " and acl_nulin = "+nLiAlb;
      if (! dtCon1.select(s))
          return false;
      
      do
      {
          s = "SELECT pro_codi FROM claslomos WHERE cll_kilos <= " + dtCon1.getDouble("acp_canti") +
                " and cll_codi = "+cllCodi+                
                " order by cll_kilos desc";
          if (! dtStat.select(s))
              return true;
          if (dtStat.getInt("pro_codi")!= jt.getValorInt(JT_PROCOD))
              return true;
      } while (dtCon1.next());
      return false;
  }
  /**
   * Ir a grid de despiece de lineas (de inviduos, vamos)
   */
  void irGridDes0()
  {
    try
    {
      swCargaLin=true;
      if (pro_codiE.getValorInt() == 0 || !pro_codiE.controla(false)  || pro_codiE.getTipoLote()!='V' || ! pro_codiE.isActivo())
      {
        mensajeErr("PRODUCTO .... NO Valido");
        jtRequestFocusSelected();
        return;
      }
      if (pro_codiE.getLikeProd().isNull("pro_codeti"))
        proCodeti = 0;
      else
        proCodeti = pro_codiE.getLikeProd().getInt("pro_codeti");
      if (pro_codiE.getLikeProd().isNull("pro_oblfsa"))
        proOblfsa=false;
      else
        proOblfsa=pro_codiE.getLikeProd().getInt("pro_oblfsa")!=0;
      proNumcro=pro_codiE.getLikeProd().getInt("pro_numcro");

     
      if (llenaCllCodi())
      {
        opAutoClas.setEnabled(true);
        opAutoClas.setSelected(jt.getValorInt(JT_CANIND)==0?true:!hasDiferentClas(jt.getValorInt(JT_NLIN)));
        opAutoCl=true;
      }
      else
      {
        opAutoCl=false;
        opAutoClas.setEnabled(false);
        opAutoClas.setSelected(false);
      }
    }
    catch (Exception k)
    {
      Error("Error al controlar el Codigo Prod.", k);
      return;
    }
    if (jt.getValorInt(0) == 0)
      jtDes.removeAllDatos();
    jt.setEnabled(false);
    Bdesagr.setEnabled(true);
    jtDes.setEnabled(true);

    if (jt.getValorInt(0)==0)
    { // NO hay lineas de individuos
      javax.swing.SwingUtilities.invokeLater(new Thread()
      {
        @Override
        public void run()
        {
            jtDes.requestFocusInicio();
           
                copiaJtValorAnt();
                
           
            jtDes.salirFoco();
            BimpEti.setEnabled(true);
            kgIndivAnt=acp_cantiE.getValorDec();
            afterCambiaLinDes();
            swCargaLin=false;
        }
       });
    }
    else
    { // Ya hay Lineas de invidividuos
      javax.swing.SwingUtilities.invokeLater(new Thread()
      {
        @Override
        public void run()
        {
            jtDes.requestFocusFinal();
            afterCambiaLinDes();
            BimpEti.setEnabled(true);         
            swCargaLin=false;
        }
      });
    }
  }

  void afterCambiaLinDes() {
      try {
            if (jtDes.getValorInt(0) != 0) {
                if (!actStkPart.checkStock(dtStat, jt.getValorInt(JT_PROCOD),
                     acc_anoE.getValorInt(), emp_codiE.getValorInt(),
                     acc_serieE.getText(), acc_numeE.getValorInt(),
                     jtDes.getValorInt(0), alm_codiE.getValorInt(),
                     acp_cantiE.getValorDec(), acp_canindE.getValorInt())) {
                    jtDes.setLineaEditable(false);
                } else {
                    if (ARG_ADMIN) {
                        acp_numindE.setEditable(true);
                    }
                    jtDes.setLineaEditable(true);
                }
            } else {
                if (ARG_ADMIN) {
                    acp_numindE.setEditable(false);
                }
                jtDes.setLineaEditable(true);
            }
        } catch (SQLException k) {
            Error("Error al comprobar stock anterior", k);
        }
        jtDes.ponValores(jtDes.getSelectedRow());
        lineaAnt = getLinGrDes();
        kgIndivAnt = acp_cantiE.getValorDec();
        acp_cantiE.resetCambio();
    }

    public void PADPrimero() {
        verDatos(dtCons);
        nav.pulsado = navegador.NINGUNO;
    }
  public void PADAnterior()
  {
    verDatos(dtCons);
    nav.pulsado=navegador.NINGUNO;
  }
  public void PADSiguiente()
  {
    verDatos(dtCons);
    nav.pulsado=navegador.NINGUNO;
  }
  public void PADUltimo()
  {
    verDatos(dtCons);
    nav.pulsado=navegador.NINGUNO;
  }
  
  public void setAccCodi(String accNume)
  {
      acc_numeE.setText(accNume);
  }
  public void setEjeNume(int ejeNume)
  {
      acc_anoE.setValorInt(ejeNume);
  }
  public void setAccSerie(String accSerie)
  {
      acc_serieE.setText(accSerie);
  }
  public boolean inTransation()
  {
      return (nav.getPulsado()==navegador.ADDNEW || nav.getPulsado()==navegador.EDIT || nav.getPulsado()==navegador.DELETE);
  }
  @Override
  public void PADQuery()
  {
    acc_anoE.setEnabled(true);

    activar(true);
    jt.setEnabled(false);
    jtDes.setEnabled(false);
    Birgrid.setEnabled(false);
    mensaje("Buscar datos ...");
    Pcabe.setQuery(true);
    Pcabe.resetTexto();
    if (Integer.parseInt(Formatear.getFechaAct("MM"))<2)
        acc_anoE.setText(">="+(EU.ejercicio-1));
    else
        acc_anoE.setValorDec(EU.ejercicio);
    
    emp_codiE.setValorInt(EU.em_cod);
    if (EU.getSbeCodi()!=0)
      sbe_codiE.setValorInt(EU.getSbeCodi());
    Tpanel1.setSelectedIndex(0);
    acc_numeE.requestFocus();
  }
    @Override
  public void ej_query1()
  {
    Component c=Pcabe.getErrorConf();
    if (c!=null)
    {
      c.requestFocus();
      mensajeErr("Error en condiciones Busqueda");
      return;
    }
    ArrayList v=new ArrayList();
    v.add(sbe_codiE.getStrQuery());
    v.add(emp_codiE.getStrQuery());
    v.add(acc_anoE.getStrQuery());
    v.add(acc_serieE.getStrQuery());
    v.add(acc_numeE.getStrQuery());
    v.add(prv_codiE.getStrQuery());
    v.add(acc_fecrecE.getStrQuery());
    v.add(usu_nombE.getStrQuery());
    v.add(fcc_anoE.getStrQuery());
    v.add(fcc_numeE.getStrQuery());
    v.add(acc_obserE.getStrQuery());
    v.add(acc_impokgE.getStrQuery());
    v.add(acc_portesE.getStrQuery());
    v.add(acc_cerraE.getStrQuery());
    v.add(acc_totfraE.getStrQuery());
    try
    {
      Pcabe.setQuery(false);
      s = "SELECT acc_fecrec,emp_codi,acc_ano,acc_serie,acc_nume FROM v_albacoc "; //WHERE emp_codi = " + EU.em_cod ;
      s = creaWhere(s, v, true);
      s += " ORDER BY acc_fecrec,acc_ano,acc_serie,acc_nume ";
//      this.setEnabled(false);
      mensaje("Espere, por favor ... buscando datos");
//      debug(s);
      if (!dtCon1.select(s))
      {
        msgBox("No encontrados Albaranes con estos criterios");
        rgSelect();
        activaTodo();
        nav.pulsado=navegador.NINGUNO;
        this.setEnabled(true);
        activaModPrecio();
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
    activaModPrecio();
    nav.pulsado=navegador.NINGUNO;
  }
  void activaModPrecio()
  {
    if (ARG_MODPRECIO &&  fcc_numeE.getValorInt() == 0)
    {
      pro_codiE.setEditable(false);
      acl_kgrecE.setEditable(false);
      jt.setEnabled(true);
    }
    if (! dtCons.getNOREG()  && ARG_ADMIN)
    {
      alm_codiE.setEnabled(true);
    }
    if (ARG_ADMIN)
      jt.setEnabled(true);
  }
  @Override
  public void canc_query()
  {
    Pcabe.setQuery(false);
    activaTodo();
    verDatos(dtCons);
    mensaje("");
    mensajeErr("Consulta ... Cancelada");
    nav.pulsado=navegador.NINGUNO;
  }
  @Override
  public void PADEdit()
  {
    try
    {
      if (hisRowid!=0)
      {
        msgBox("Viendo albaran historico ... IMPOSIBLE MODIFICAR");
        activaTodo();
        return;
      }
      if (avc_numeE.getValorInt() != 0)
      {
        msgBox("Albaran se creo sobre una Venta ... IMPOSIBLE MODIFICAR");
        activaTodo();
        return;
      }
      if (fcc_numeE.getValorDec() > 0 && !ARG_ADMIN)
      {
        msgBox("Albaran YA se HA FACTURADO ... IMPOSIBLE MODIFICAR");
        activaTodo();
        return;
      }
      if (!ARG_MODPRECIO && opBloquea.isSelected())
      {
        msgBox("ALBARAN YA TIENE PRECIOS ASIGNADOS .. IMPOSIBLE MODIFICAR");
        activaTodo();
        return;
      }
      if (pdejerci.isCerrado(dtStat, acc_anoE.getValorInt(), emp_codiE.getValorInt()))
      {
        if (!ARG_ADMIN)
        {
          msgBox("Albaran es de un ejercicio YA cerrado ... IMPOSIBLE MODIFICAR");
          nav.pulsado = navegador.NINGUNO;
          activaTodo();
          return;
        }
        else
          msgBox("ATENCION!!! Albaran es de un ejercicio YA cerrado");
      }

      if (! setBloqueo(dtAdd,"v_albacoc", acc_anoE.getValorInt()+"|"+emp_codiE.getValorInt()+
                      "|"+acc_serieE.getText()+"|"+acc_numeE.getValorInt()))
      {
        msgBox(msgBloqueo);
        activaTodo();
        return;
      }

      if (opAgrupar.isSelected() || opIncPortes.isSelected())
        verDatos(dtCons,false,false);
      s = "SELECT * FROM V_albacoc WHERE acc_ano =" + acc_anoE.getValorInt() +
          " and emp_codi = " + emp_codiE.getValorInt() +
          " and acc_serie = '" + acc_serieE.getText() + "'" +
          " and acc_nume = " + acc_numeE.getValorInt();
      if (!dtAdd.select(s, true))
      {
        msgBox("Albaran NO encontrado .. PROBABLEMENTE se ha borrado");
        activaTodo();
        return;
      }
      
//      accCerra=dtAdd.getInt("acc_cerra");
//      dtAdd.edit();
//      dtAdd.setDato("acc_cerra",0); // Lo pongo COMO No cerrado
//      dtAdd.update(stUp);
//      acc_totfraE.setEnabled(fcc_numeE.getValorDec() > 0);
      Tpanel1.setSelectedIndex(0);

      activar(true);
     
      if (dtAdd.getInt("frt_ejerc", true) == 0)
      {
        if (ARG_MODPRECIO)
            acl_porpagE.setEnabled(true);
      }
      else // En caso contrario pongo el combo de los portes  a disabled
        acc_portesE.setEnabled(false);
      copiaAlbaranNuevo(dtCon1,dtAdd,"Modificado Albaran",EU.usuario,acc_anoE.getValorInt(),
              emp_codiE.getValorInt(),acc_serieE.getText(),acc_numeE.getValorInt());
      acc_numeE.setEnabled(false);
      acc_serieE.setEnabled(false);
      acc_anoE.setEnabled(false);
      emp_codiE.setEnabled(false);
      usu_nombE.setEnabled(false);
      fcc_anoE.setEnabled(false);
      fcc_numeE.setEnabled(false);
   //   prv_codiE.setEnabled(false);
      eje_numeE.setEnabled(false);
      avc_anoE.setEnabled(false);
      avc_numeE.setEnabled(false);
      pcc_numeE.setEnabled(false);
      if (ARG_MODPRECIO)
        acc_totfraE.setEnabled(true);
//      alm_codiE.setEnabled(false);

      jt.setEnabled(false);
      cambioPrv();
      getNomArtPed(pro_codiE.getValorInt(),eje_numeE.getValorInt(),pcc_numeE.getValorInt());
      pro_codiE.setEditable(true);

      acl_kgrecE.setEditable(true);
      jtDes.setEnabled(false);
      ejPedAnt=eje_numeE.getValorInt();
      nuPedAnt=pcc_numeE.getValorInt();
      prvAnt=prv_codiE.getValorInt();
      estIniE.setValor("P");
      estFinE.setValor("A");
      
      jtRecl.resetCambio();
      jtRecl.requestFocusInicio();
      irGridLin();
      acc_dtoppE.resetCambio();
    }
    catch (Exception k)
    {
      Error("Error al Modificar Albaran", k);
    }

  }

  @Override
  public void ej_edit1()
  {
    try
    {
      mensaje("Modificando Compra .. Espere, por favor");
      if (!checkCabecera())
        return;
//      if (! cargaLinPedido(true))
//        return;

      
      if (jtDes.isEnabled() && ! swCargaAlb)
      {
          jtDes.salirGrid();
          if (! procLineaInd())
              return;
      }
     
      jt.salirGrid();
      int nCol = cambiaLinAlb(jt.getSelectedRow());
      if (nCol >= 0)
      {
        jt.requestFocus(jt.getSelectedRow(), nCol);
        return;
      }
      jtRecl.salirGrid();
      nCol = cambiaLinRecl(jtRecl.getSelectedRow());
      if (nCol >= 0)
      {
        jtRecl.requestFocus(jtRecl.getSelectedRow(), nCol);
        return;
      }

      this.setEnabled(false);

      if (!actAlbaran())
      {
        dtAdd.rollback();
        return;
      }
       if (jf != null)
       {
        if (fcc_numeE.getValorDec() > 0)
        {
          // Apunte de que se ha modificado el albaran teniendo factura.
          jf.ht.clear();
          jf.ht.put("%a", acc_anoE.getValorInt()+"|"+emp_codiE.getValorInt()+
                      "|"+acc_serieE.getText()+"|"+acc_numeE.getValorInt());
          jf.ht.put("%f",fcc_numeE.getText());
          jf.guardaMens("C1", jf.ht);
        }
        
      // Apunte de que se ha modificado el albaran.
          jf.ht.clear();
          jf.ht.put("%a", acc_anoE.getValorInt()+"|"+emp_codiE.getValorInt()+
                      "|"+acc_serieE.getText()+"|"+acc_numeE.getValorInt());
          jf.guardaMens("CA", jf.ht);
          if (Formatear.comparaFechas(acc_fecrecE.getDate(),feulin)<=0)
          { // Apunte de que se ha modificado el albaran anterior a inventario
             jf.ht.clear();
             jf.ht.put("%a", acc_anoE.getValorInt()+"|"+emp_codiE.getValorInt()+
                      "|"+acc_serieE.getText()+"|"+acc_numeE.getValorInt());
             jf.guardaMens("CC", jf.ht);
          }
      }
      if (fcc_numeE.getValorDec() > 0 && opActFra.isSelected())
        actDatosFra();
//      if ( swCambioPrv || alm_codiE.hasCambio())
//      {
//          s="update stockpart  set prv_codi= "+prv_codiE.getValorInt()+
//            ", alm_codi = "+alm_codiE.getValorInt()+
//            " where  eje_nume =  " + acc_anoE.getValorInt()+
//            " and emp_codi =  " + emp_codiE.getValorInt()+
//            " and pro_serie  = '" +acc_serieE.getText()+"'"+
//            " and pro_nupar = " + acc_numeE.getValorInt();
//          dtAdd.executeUpdate(s);
//      }
      ctUp.commit();
      resetBloqueo(dtAdd,"v_albacoc", acc_anoE.getValorInt()+"|"+emp_codiE.getValorInt()+
                      "|"+acc_serieE.getText()+"|"+acc_numeE.getValorInt());
    }
    catch (Exception k)
    {
      Error("Error al Modificar el Albaran", k);
      return;
    }
    this.setEnabled(true);
    nav.pulsado=navegador.NINGUNO;
    activaTodo();
    verDatos(dtCons);
    alm_codiE.setEnabled(false);

    mensajeErr("Modificacion .. Realizada");
    mensaje("");
  }
  public void canc_edit()
  {
    activaTodo();
    nav.pulsado=navegador.NINGUNO;
    verDatos(dtCons);
    try {
      resetBloqueo(dtAdd, "v_albacoc",
                   acc_anoE.getValorInt() + "|" + emp_codiE.getValorInt() +
                   "|" + acc_serieE.getText() + "|" + acc_numeE.getValorInt());
    } catch (Exception k)
    {
      mensajes.mensajeAviso("Error al Quitar el bloqueo\n"+k.getMessage());
    }
    mensajeErr("Modificacion ... Cancelada");
    mensaje("");
  }

    @Override
  public void PADAddNew()
  {
    cargaAlbVentas=false;
    mensaje("Insertando Albaran ...");
    Pcabe.resetTexto();
    Potros.resetTexto();
    swActDesg=false;
    jt.setEnabled(false);
    jtDes.setEnabled(false);    
    jt.removeAllDatos();
    jtDes.removeAllDatos();
    jtPed.removeAllDatos();
    jtRecl.removeAllDatos();
    vertKilosE.setValorInt(0);
    vertNPiezE.setValorInt(0);
    Tpanel1.setSelectedIndex(0);
    activar(true);
    try {
      alm_codiE.setValor(lkConfig.getString("cfg_almcom")); // Almacen de Compras
      sbe_codiE.setValorInt(EU.getSbeCodi()!=0?EU.getSbeCodi():1);
    } catch (Exception k)
    {
      Error("Error al Buscar SubEmpresa de Usuario",k);
    }
    if (ARG_MODPRECIO)
     acl_porpagE.setEnabled(true);
    emp_codiE.setValorDec(EU.em_cod);

    acc_anoE.setValorDec(EU.ejercicio);
    avc_anoE.setValorDec(EU.ejercicio);
    acc_numeE.setEnabled(false);
    avc_numeE.setEnabled(false);
    avc_anoE.setEnabled(false);
    acc_fecrecE.setText(Formatear.getFechaAct("dd-MM-yyyy"));

    acc_totfraE.setValor("0");
    acc_serieE.setText("A");

    acc_totfraE.setEnabled(false);

    pro_codiE.setEditable(true);
    alm_codiE.setEnabled(true);
    sbe_codiE.setEnabled(true);
    acl_kgrecE.setEditable(true);
    if (ARG_MODPRECIO)
        acl_porpagE.setEnabled(true);
    usu_nombE.setText(EU.usuario);
    usu_nombE.setEnabled(false);
    jtDes.setEnabled(false);
    jt.setEnabled(false);
    fcc_anoE.setEnabled(false);
    fcc_numeE.setEnabled(false);
    usu_nombE.setEnabled(false);
    boolean opVerPrecio=opVerPrecios.isSelected();
    Ptotal.resetTexto();
    opVerPrecios.setSelected(opVerPrecio);
    acc_dtoppE.resetTexto();
 
//    BimpEti.setEnabled(true);
    opImpEti.setSelected(true);
    estIniE.setValor("P");
    estFinE.setValor("A");
//    acl_prcomE.setEditable(true);
    prv_codiE.resetCambio();
    prv_codiE.requestFocus();
    
    swActDesg=true;
    PADAddNew0();
  }

    @Override
  public void ej_addnew1()
  {
    try
    {
      mensaje("Insertando Albaran Compra .. Espere, por favor");
      if (!checkCabecera())
        return;
      if (jtDes.isEnabled() && ! swCargaAlb)
      {
          jtDes.salirFoco();
          jtDes.procesaAllFoco();
          if (! procLineaInd())
              return;
      }
      if (acc_numeE.getValorInt() == 0)
      {
        mensajeErr("Introduzca Alguna Linea de Albaran");
        jt.requestFocusInicio();
        return;
      }
      if (! cargaLinPedido(true))
        return;
    
      jt.salirFoco();
      if (! swCargaAlb)
        jt.procesaAllFoco();
      int nCol = cambiaLinAlb(jt.getSelectedRow());
      if (nCol >= 0)
      {
        jt.requestFocus(jt.getSelectedRow(), nCol);
        return;
      }
      jtRecl.salirFoco();
      jtRecl.procesaAllFoco();
      nCol = cambiaLinRecl(jtRecl.getSelectedRow());
      if (nCol >= 0)
      {
        jtRecl.requestFocus(jtRecl.getSelectedRow(), nCol);
        return;
      }

      this.setEnabled(false);

      if (!actAlbaran())
      {
        dtAdd.rollback();
        return;
      }
      ctUp.commit();
      resetBloqueo(dtAdd, "v_albacoc", acc_anoE.getValorInt() + "|" + emp_codiE.getValorInt() +
                   "|" + acc_serieE.getText() + "|" + acc_numeE.getValorInt());
      swCargaAlb=false;
      strSql = "SELECT emp_codi,acc_ano,acc_serie,acc_nume " +
          " FROM v_albacoc WHERE emp_codi = " + EU.em_cod +
          " AND acc_ano = " + acc_anoE.getValorInt() +
          " and acc_serie = '" + acc_serieE.getText() + "'" +
          " and acc_nume = " + acc_numeE.getValorInt();
      rgSelect();
      this.setEnabled(true);
      activaTodo();
    }
    catch (Exception k)
    {
      Error("Error al Insertar el Albaran", k);
      return;
    }
    mensajeErr("Alta .. Realizada");
    mensaje("");
    nav.pulsado=navegador.NINGUNO;
  }

  public void canc_addnew()
  {
    try
    {
      swCargaAlb=false;
      if (acc_numeE.getValorInt() > 0)
      { // Borrar Cabecera
        if (mensajes.mensajeYesNo("VOLVER AL ALBARAN ? ",this)==mensajes.YES)
          return;
        if (jf != null)
        {
          jf.ht.clear();
          jf.ht.put("%a", acc_anoE.getValorInt()+"|"+emp_codiE.getValorInt()+
                      "|"+acc_serieE.getText()+"|"+acc_numeE.getValorInt());
          jf.guardaMens("C4", jf.ht);
        }
        copiaAlbaranNuevo(dtCon1,dtAdd,"ANULADA ALTA Albaran",EU.usuario,acc_anoE.getValorInt(),
              emp_codiE.getValorInt(),acc_serieE.getText(),acc_numeE.getValorInt());
        borraAlbaran();
        ctUp.commit();
        resetBloqueo(dtAdd,"v_albacoc", acc_anoE.getValorInt()+"|"+emp_codiE.getValorInt()+
                      "|"+acc_serieE.getText()+"|"+acc_numeE.getValorInt());
      }

    }
    catch (Exception k)
    {
      Error("Error al Cancelar ALTA de Albaran", k);
      return;
    }
    nav.pulsado=navegador.NINGUNO;
    activaTodo();
    verDatos(dtCons);
    mensaje("");
    mensajeErr("Alta ... Cancelada");

  }

    @Override
  public void PADDelete()
  {
    try
    {
     if (hisRowid!=0)
      {
        msgBox("Viendo albaran historico ... IMPOSIBLE BORRAR");
        activaTodo();
        return;
      }
      if (fcc_numeE.getValorDec() > 0)
      {
        if ( ARG_ADMIN)
          msgBox("Albaran se ha FACTURADO ... QUITELO PRIMERO DE LA FACTURA: "+fcc_numeE.getValorInt());
        else
          msgBox("Albaran YA se HA FACTURADO ... IMPOSIBLE BORRAR");
        activaTodo();
        return;
      }
      if (! jtRecl.isVacio())
      {
        msgBox("Albaran TIENE vertederos Asignados... IMPOSIBLE BORRAR");
        activaTodo();
        return;
      }
      if (!ARG_MODPRECIO && opBloquea.isSelected())
      {
        msgBox("ALBARAN YA TIENE PRECIOS ASIGNADOS .. IMPOSIBLE MODIFICAR");
        activaTodo();
        return;
      }
      if (pdejerci.isCerrado(dtStat, acc_anoE.getValorInt(), emp_codiE.getValorInt()))
      {
        if (!ARG_ADMIN)
        {
          msgBox("Albaran es de un ejercicio YA cerrado ... IMPOSIBLE BORRAR");
          nav.pulsado = navegador.NINGUNO;
          activaTodo();
          return;
        }
        else
          msgBox("ATENCION!!! Albaran es de un ejercicio YA cerrado");
      }
      if (MvtosAlma.hasMvtosSalida(dtCon1, 0, emp_codiE.getValorInt(), acc_anoE.getValorInt(), 
              acc_serieE.getText(), acc_numeE.getValorInt(), 0,0,acc_fecrecE.getText())!=0)
      {
          msgBox("Este albaran tiene mvtos de salida. Imposible BORRAR");
          activaTodo();
          return;
      }
     

      s = "SELECT * FROM V_albacoc WHERE acc_ano =" + acc_anoE.getValorInt() +
          " and emp_codi = " + emp_codiE.getValorInt() +
          " and acc_serie = '" + acc_serieE.getText() + "'" +
          " and acc_nume = " + acc_numeE.getValorInt();
      if (!dtAdd.select(s, true))
      {
        msgBox("Albaran NO encontrado .. PROBABLEMENTE se ha borrado");
        activaTodo();
        return;
      }
      if (dtAdd.getInt("frt_ejerc",true)!=0)
      {
        msgBox("Albaran ESTA metido en UNA FRA. DE TRANSP. IMPOSIBLE BORRAR");
        activaTodo();
        return;
      }
      if (!setBloqueo(dtAdd, "v_albacoc",
                      acc_anoE.getValorInt() + "|" + emp_codiE.getValorInt() +
                      "|" + acc_serieE.getText() + "|" + acc_numeE.getValorInt()))
      {
        msgBox(msgBloqueo);
        activaTodo();
        return;
      }
      Tpanel1.setSelectedIndex(0);
      activar(false);
      acc_totfraE.setEnabled(false);
      alm_codiE.setEnabled(false);
      acc_dtoppE.resetTexto();
      Baceptar.setEnabled(true);
      Bcancelar.setEnabled(true);
      mensaje("BORRAR  Albaran ...");
      Bcancelar.requestFocus();
    }
    catch (Exception k)
    {
      Error("Error al Modificar Albar�n", k);
    }

  }
  
  public void ej_delete1()
  {
    try
    {
      if (mensajes.mensajeYesNo("ANULAR BORRADO ? ",this)==mensajes.YES)
        return;
      if (jf != null)
      {
        jf.ht.clear();
        jf.ht.put("%a",acc_anoE.getValorInt()+"|"+emp_codiE.getValorInt()+
                      "|"+acc_serieE.getText()+"|"+acc_numeE.getValorInt());
        jf.guardaMens("C2", jf.ht);
      }

      mensaje("BORRANDO Compra .. Espere, por favor");
      copiaAlbaranNuevo(dtCon1,dtAdd,"BORRADO Albaran",EU.usuario,acc_anoE.getValorInt(),
              emp_codiE.getValorInt(),acc_serieE.getText(),acc_numeE.getValorInt());
      borraAlbaran();
      ctUp.commit();
      resetBloqueo(dtAdd, "v_albacoc",
                      acc_anoE.getValorInt() + "|" + emp_codiE.getValorInt() +
                      "|" + acc_serieE.getText() + "|" + acc_numeE.getValorInt());

      this.setEnabled(true);
      activaTodo();
      strSql = getStrSql();
      rgSelect();
      mensajeErr("BORRADO .. Realizado");
      mensaje("");
      nav.pulsado = navegador.NINGUNO;
    }
    catch (Exception k)
    {
      Error("Error al BORRAR el Albaran", k);
    }
  }
  public void canc_delete()
  {
    activaTodo();
    verDatos(dtCons);
    try
    {
      resetBloqueo(dtAdd, "v_albacoc",
                   acc_anoE.getValorInt() + "|" + emp_codiE.getValorInt() +
                   "|" + acc_serieE.getText() + "|" + acc_numeE.getValorInt());
    } catch (Exception k)
    {
      mensajes.mensajeAviso("Error al Quitar el bloqueo\n"+k.getMessage());
    }
    mensaje("");
    mensajeErr("BAJA ... Cancelada");
    nav.pulsado = navegador.NINGUNO;
  }

    @Override
  public void activar(boolean b)
  {
    if (MIactNombre!=null)
    {
      MIactNombre.setEnabled(b);
      MIchangeProd.setEnabled(!b);
      MIimprEtiq.setEnabled(!b);
    }
    if (ARG_MODPRECIO)
    {
      opVerPrecios.setEnabled(!b);
      acc_portesE.setEnabled(b);
      acc_totfraE.setEnabled(!b);
      acc_cerraE.setEnabled(!b);
      jtRecl.setEnabled(b);
      BvertSala.setEnabled(b);
      estIniE.setEnabled(b);
      estFinE.setEnabled(b);
    }
    else
    {
      jtRecl.setEnabled(false);
      BvertSala.setEnabled(false);
      estIniE.setEnabled(false);
      estFinE.setEnabled(false);
      acc_totfraE.setEnabled(false);
      acc_cerraE.setEnabled(false);
    }
    if (ARG_ADMIN)
      alm_codiE.setEnabled(!b);
    else
      alm_codiE.setEnabled(b);

    sbe_codiE.setEnabled(b);
    eje_numeE.setEnabled(b);
    pcc_numeE.setEnabled(b);
    cll_codiE.setEnabled(b);
    jt.setEnabled(b);
    if (!b)
      Bdesagr.setEnabled(b);
    jtDes.setEnabled(b);
    Pcabe.setEnabled(b);
    Potros.setEnabled(b);
    Baceptar.setEnabled(b);
    Bcancelar.setEnabled(b);
    emp_codiE.setEnabled(b);
    acc_serieE.setEnabled(b);
    acc_numeE.setEnabled(b);
    avc_anoE.setEnabled(b);
    avc_numeE.setEnabled(b);
    if (!b || nav.pulsado==navegador.QUERY)
      acc_impokgE.setEnabled(b);
    acc_fecrecE.setEnabled(b);
    usu_nombE.setEnabled(b);
    prv_codiE.setEnabled(b);
    fcc_anoE.setEnabled(b);
    fcc_numeE.setEnabled(b);
    acc_obserE.setEnabled(b);
    Birgrid.setEnabled(b);
    BimpEti.setEnabled(false);
    opAutoClas.setEnabled(b);
    opAgrupar.setEnabled(!b);
    opIncPortes.setEnabled(!b);
    Bimpri.setEnabled(!b);
    opIncDet.setEnabled(!b);
    BCamFeEnt.setEnabled(!b);
    
    pro_codiE.setEditable(false);
    alm_codiE.setEnabled(false);
    if (ARG_MODPRECIO)
        acl_porpagE.setEnabled(false);
    acl_cantiE.setEnabled(false);
    acl_numcajE.setEnabled(false);
    jt.setEnabled(true);

  }
 private void cambiaLineaHist(int rowid)
{
    hisRowid=rowid;
    if (hisRowid==0)
    {
        verDatos(dtCons);
        return;
    }
    tablaCab="hisalcaco";
    tablaLin="hisallico";
    tablaInd="hisalpaco";

    condHist=" and his_rowid = "+hisRowid;
    try {
         s="SELECT * FROM "+tablaCab+" WHERE emp_codi = "+emp_codiE.getValorInt()+
          " and acc_ano = "+acc_anoE.getValorInt()+
          " and acc_serie = '"+acc_serieE.getText()+"'"+
          " and acc_nume = "+acc_numeE.getValorInt() +
         condHist;

        dtCon1.select(s);
        verDatos(dtCon1,opAgrupar.isSelected(),opIncPortes.isSelected());
    } catch (SQLException k)
    {
        Error("Error al ver datos de historicos",k);
    }
 }
  void verDatos(DatosTabla dt)
  {
      tablaCab="v_albacoc";
      tablaLin="v_albacol";
      tablaInd="v_albcompar";

      hisRowid=0;
      condHist="";
      verDatos(dt,opAgrupar.isSelected(),opIncPortes.isSelected());
  }

  void verDatos(DatosTabla dt,boolean agruLin,boolean incPortes)
  {
    try
    {
      if (dtCons.getNOREG())
        return;
      
      int empCodi=dt.getInt("emp_codi");
      int accAno=dt.getInt("acc_ano");
      int accNume=dt.getInt("acc_nume");
      String accSerie=dt.getString("acc_serie");

      emp_codiE.setValorDec(empCodi);
      acc_anoE.setValorDec(accAno);
      acc_serieE.setText(accSerie);
      acc_numeE.setValorDec(accNume);
      s="SELECT * FROM "+tablaCab+" WHERE emp_codi = "+empCodi+
          " and acc_ano = "+accAno+
          " and acc_serie = '"+accSerie+"'"+
          " and acc_nume = "+accNume+
          condHist;
      if (! dtCon1.select(s))
      {
        mensajes.mensajeAviso("REGISTRO NO ENCONTRADO SEGURAMENTE SE HA BORRADO");
        return;
      }
      if (ARG_MODPRECIO)
        acl_porpagE.setEnabled(true);
      if (dtCon1.getInt("frt_ejerc",true)!=0)
      {
        if (ARG_MODPRECIO)
            acl_porpagE.setEnabled(false);
      }
      prv_codiE.setText(dtCon1.getString("prv_codi"));
      prv_codiE.controla(false);
      acc_copvfaE.setText(dtCon1.getString("acc_copvfa"));
      acc_fecrecE.setText(dtCon1.getFecha("acc_fecrec", "dd-MM-yyyy"));
      usu_nombE.setText(dtCon1.getString("usu_nomb"));
      fcc_anoE.setValorDec(dtCon1.getInt("fcc_ano", true));
      fcc_numeE.setValorDec(dtCon1.getInt("fcc_nume", true));
      acc_obserE.setText(dtCon1.getString("acc_obser"));
      acc_portesE.setValor(dtCon1.getString("acc_portes",true));
      frt_ejercE.setValorInt(dtCon1.getInt("frt_ejerc",true));
      frt_numeE.setValorInt(dtCon1.getInt("frt_nume",true));
      sbe_codiE.setValorInt(dtCon1.getInt("sbe_codi"));
      avc_anoE.setValorInt(dtCon1.getInt("avc_ano",true));
      avc_numeE.setValorInt(dtCon1.getInt("avc_nume",true));
      acc_totfraE.setEnabled(false);
      acc_cerraE.setEnabled(false);
      alm_codiE.setEnabled(false);
      acc_cerraE.setValor(dtCon1.getInt("acc_cerra"));
     
      acc_totfraE.setValor(dtCon1.getInt("acc_totfra"));
      if (ARG_MODPRECIO)
      {
//        acl_prcomE.setEditable((avc_numeE.getValorInt()==0 ||
//          acc_cerraE.getValorInt()==0) && fcc_numeE.getValorInt()==0);
        acc_totfraE.setEnabled(true);
        acc_cerraE.setEnabled(true);
      }

      if (opVerPrecios.isSelected())
        acc_impokgE.setValorDec(dtCon1.getDouble("acc_impokg",true));
      else
        acc_impokgE.setValorDec(0);
      if (hisRowid==0)
      {
            jtHist.setEnabled(false);
            jtHist.removeAllDatos();
            s="SELECT * FROM hisalcaco WHERE emp_codi = "+empCodi+
              " and acc_ano = "+accAno+
              " and acc_serie = '"+accSerie+"'"+
              " and acc_nume = "+accNume+
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
      pcc_numeE.resetTexto();
      eje_numeE.resetTexto();
      pcc_comenE.setText("");
      s="SELECT * FROM pedicoc as c "+
          " where c.emp_codi = "+EU.em_cod+
          " AND c.acc_ano = "+accAno+
          " and c.acc_nume = "+accNume+
          " and c.acc_serie = '"+accSerie+"'";
      int nLiPed=0;
      int nLin = 0;
      int prodPed[]=new int[1];
      int uniPed[]=new int[1];
      double cantPed[]=new double[1];
      double precPed[]=new double[1];
      int nucape;
      double cantpe;
      double precpe;
      int n;
      if (dtCon1.select(s))
      {
        pcc_numeE.setValorDec(dtCon1.getInt("pcc_nume"));
        eje_numeE.setValorDec(dtCon1.getInt("eje_nume"));
        estPedi = dtCon1.getString("pcc_estad").charAt(0);
        pcc_comenE.setText(dtCon1.getString("pcc_comen"));

        s = "SELECT pro_codi, ";
        if (estPedi=='P')
          s+= " pcl_precpe,"+
             " sum(pcl_nucape) as pcl_nucape, "+
            " sum(pcl_cantpe) as pcl_cantpe ";
        else if (estPedi=='C')
          s+= " pcl_precco as pcl_precpe, "+
              " sum(pcl_nucaco) as pcl_nucape, "+
            " sum(pcl_cantco) as pcl_cantpe ";
        else
          s+= " pcl_precfa as pcl_precpe, "+
              " sum(pcl_nucafa) as pcl_nucape, "+
            " sum(pcl_cantfa) as pcl_cantpe ";
        s+="  FROM pedicol as l " +
            " where l.emp_codi = " + EU.em_cod +
            " AND l.eje_nume = " + eje_numeE.getValorInt() +
            " and l.pcc_nume = " + pcc_numeE.getValorInt() +
            " group by pro_codi,"+(estPedi=='P'?"pcl_precpe":estPedi=='C'?"pcl_precco":"pcl_precfa");
        if (dtCon1.select(s))
        { // Cargo en arrays los datos del pedido.
          nLiPed=0;
          do // Cuento el numero de lineas del pedido.
          {
            nLiPed ++;
          } while (dtCon1.next());
          dtCon1.first();
          prodPed = new int[nLiPed];
          uniPed = new int[nLiPed];
          cantPed = new double[nLiPed];
          precPed = new double[nLiPed];
          do
          {
            prodPed[nLin] = dtCon1.getInt("pro_codi");
            uniPed[nLin] = dtCon1.getInt("pcl_nucape");
            cantPed[nLin] = dtCon1.getDouble("pcl_cantpe");
            precPed[nLin] = dtCon1.getDouble("pcl_precpe");
            nLin++;
          } while (dtCon1.next());
        }
      }

      String condWhere="l.emp_codi = " + empCodi +
          " and acc_ano = " + accAno +
          " and acc_nume = " + accNume+
          " and acc_serie = '" + accSerie + "'"+
          condHist;
      if (!agruLin)
        s = "SELECT acl_nulin,l.pro_codi, pro_nomart,acl_comen,alm_codi,acl_porpag ," +
          " acl_canti, acl_numcaj,acl_kgrec," +
          " acl_prcom,acl_dtopp " +
          " FROM "+tablaLin+" as l "+
          " where "+condWhere+
          " order by acl_nulin ";
      else
      {
        condWhere="SELECT 0 AS acl_nulin,pro_codi,pro_nomart,acl_prcom,acl_comen,alm_codi,acl_porpag, " +
          "sum(acl_canti) as acl_canti, sum(acl_numcaj) as acl_numcaj,sum(acl_kgrec) as acl_kgrec,acl_dtopp " +
          " FROM "+tablaLin+" AS l WHERE "+condWhere ;
        s = condWhere+" AND l.acl_canti >= 0" +
            " GROUP BY pro_codi,acl_prcom,pro_nomart,acl_comen,alm_codi,acl_porpag,acl_dtopp "+
            " UNION " +
            condWhere+" AND l.acl_canti < 0 "+
            " GROUP BY pro_codi,acl_prcom,pro_nomart,acl_comen,alm_codi,acl_porpag,acl_dtopp "+
            " ORDER BY 2";
      }
      swActDesg = false;
      jt.setEnabled(false);
      jtDes.setEnabled(false);
      if (!jt.isVacio())
        jt.removeAllDatos();
      jtDes.removeAllDatos();
      String proNomb;
      nLin = 0;
//      double tipIva = -1;
      double kilosT = 0;
      double impLin = 0;
      opBloquea.setSelected(false);
      double impLinAct;
      double accDtopp=0;
      if (dtCon1.select(s))
      {
        alm_codiE.setValor(dtCon1.getString("alm_codi"));
        almOrig=alm_codiE.getValorInt();
        do
        {
          ArrayList v = new ArrayList();
          nLin++;
          proNomb=dtCon1.getString("pro_nomart",false);
          if (proNomb==null)
          {
            proNomb = pro_codiE.getNombArt(dtCon1.getString("pro_codi"));
            if (proNomb == null)
              proNomb = "**PRODUCTO NO ENCONTRADO**";
          }
          v.add(dtCon1.getString("acl_nulin"));
          v.add(dtCon1.getString("pro_codi"));
          v.add(proNomb);
          v.add(dtCon1.getString("acl_numcaj"));
          v.add(dtCon1.getString("acl_canti"));
          if (dtCon1.getDouble("acl_prcom",true)!=0)
            opBloquea.setSelected(true);
           
          impLinAct=opVerPrecios.isSelected()?
                ( dtCon1.getDouble("acl_dtopp")==0?dtCon1.getDouble("acl_prcom",true):
                 (dtCon1.getDouble("acl_prcom",true)-acc_impokgE.getValorDec())/(1- (dtCon1.getDouble("acl_dtopp")/100) )+acc_impokgE.getValorDec())
                - (incPortes?0:acc_impokgE.getValorDec())
                 :0;
          
          v.add(impLinAct);
          v.add(dtCon1.getString("acl_kgrec"));
          nucape=0;
          cantpe=0;
          precpe=0;
          if (pcc_numeE.getValorInt()!=0)
          {
            for (n=0;n<nLiPed;n++)
            {
              if (prodPed[n]==dtCon1.getInt("pro_codi"))
              {
                nucape=uniPed[n];
                cantpe=cantPed[n];
                precpe=precPed[n];
                uniPed[n]=0;
                cantPed[n]=0;
                break;
              }
            }
          }
          v.add(""+nucape);
          v.add(""+cantpe);
          if (opVerPrecios.isSelected())
            v.add(""+precpe);
          else
            v.add("0");
          v.add(""+(dtCon1.getInt("acl_numcaj",true)-nucape));
          v.add(""+(dtCon1.getDouble("acl_canti",true)-cantpe));
          if (opVerPrecios.isSelected())
            v.add(""+(dtCon1.getDouble("acl_prcom",true)-precpe));
          else
            v.add("0");
          v.add(dtCon1.getString("acl_comen"));

          v.add(dtCon1.getInt("acl_porpag")!=0);
          v.add(dtCon1.getDouble("acl_dtopp"));
          if (dtCon1.getDouble("acl_dtopp")!=0)
            accDtopp=dtCon1.getDouble("acl_dtopp");
          jt.addLinea(v);
          if (opVerPrecios.isSelected())
            impLin += dtCon1.getDouble("acl_canti",true) * (dtCon1.getDouble("acl_prcom",true)-
                (incPortes?0:acc_impokgE.getValorDec()));
          kilosT += dtCon1.getDouble("acl_canti",true);
        }  while (dtCon1.next());
        acl_prcomE.resetCambio();
        acl_dtoppE.resetCambio();
        acc_dtoppE.setValorDec(accDtopp);
        activaModPrecio();

        jt.requestFocusInicio();
        jt.setEnabled(true);
//      numLinE.setValorDec(nLin);
        kilostE.setValorDec(kilosT);
        imptotE.setValorDec(impLin);
        swActDesg = true;
        getNomArtPed(jt.getValorInt(0,JT_PROCOD),eje_numeE.getValorInt(),pcc_numeE.getValorInt());
        double prCompra=jt.getValorDec(0, JT_PRCOM);
        prCompra-=jt.getValorDec(0, JT_PRCOM)*
                  jt.getValorDec(0, JT_DTOPP) / 100;
        verDesgLinea(empCodi, accAno,
                     accSerie, accNume,
                     jt.getValorInt(0,0),agruLin,jt.getValorInt(0,JT_PROCOD),
                     jt.getValorDec(0,JT_KILALB),prCompra );
        verDatPedido(empCodi,eje_numeE.getValorInt(),
                     pcc_numeE.getValorInt());
        verDatRecl();
      }
      if (ARG_ADMIN)
        alm_codiE.setEnabled(true);

    }
    catch (Exception k)
    {
      Error("Error al Ver Datos de Albaran", k);
    }
  }

  /**
   * Ver Datos de Reclamación
   * @throws SQLException Error conexión base de datos
   */
  void verDatRecl() throws SQLException
  {
    s="SELECT * FROM v_regstock WHERE emp_codi = "+emp_codiE.getValorInt()+
        " and acc_nume = "+acc_numeE.getValorInt()+
        " and acc_serie = '"+acc_serieE.getText()+"'"+
        " and acc_ano = "+acc_anoE.getValorInt()+
//        " and rgs_cliprv = "+prv_codiE.getValorInt()+
        " and rgs_recprv != 0 "+
        " order by rgs_nume";
    jtRecl.removeAllDatos();
    if ( dtCon1.select(s))
    {
      do
      {
        ArrayList v = new ArrayList();
        v.add(dtCon1.getString("pro_codi"));
        v.add(pro_codverE.getNombArt(dtCon1.getString("pro_codi")));
        v.add(dtCon1.getString("pro_numind"));
        v.add(dtCon1.getString("rgs_canti"));
        v.add(dtCon1.getString("rgs_kilos"));
        v.add(dtCon1.getString("rgs_prregu"));
        v.add(dtCon1.getFecha("rgs_fecha", "dd-MM-yy"));
        v.add(paregalm.getStrTipRecl(dtCon1.getInt("rgs_recprv")));
        v.add(dtCon1.getFecha("rgs_fecres", "dd-MM-yy"));
        v.add(dtCon1.getString("tir_codi")+" - "+ tir_codiE.getValor(dtCon1.getString("tir_codi")));
        v.add(Boolean.valueOf(dtCon1.getInt("rgs_trasp")!=0));
        v.add(dtCon1.getString("rgs_clidev"));
        v.add(rgs_clidevE.getNombCliente(dtStat,dtCon1.getInt("rgs_clidev")));
        v.add(dtCon1.getString("rgs_coment"));
        v.add(dtCon1.getString("rgs_nume"));
        jtRecl.addLinea(v);
      } while (dtCon1.next());
    }
    jtRecl.requestFocusInicio();
    jtRecl.resetCambio();
    actAcumVert(-1);
  }
  /**
   * Carga las lineas desglose en el grid jtDes
   * @param empCodi
   * @param accAno
   * @param serie
   * @param accNume
   * @param linea
   * @param agruLin
   * @param proCodi
   * @param canti
   * @param prec
   * @throws SQLException 
   */
  void verDesgLinea(int empCodi, int accAno, String serie, int accNume,
                    int linea,boolean agruLin,int proCodi,double canti,double prec)
      throws SQLException
  {
    if (nav.pulsado == navegador.ADDNEW || nav.pulsado==navegador.EDIT)
    {
      pro_codiE.setEditable(true);
//      alm_codiE.setEditable(true);
    }
    if (!swActDesg)
      return;
    if (! opIncPortes.isSelected() || nav.pulsado == navegador.ADDNEW || nav.pulsado==navegador.EDIT )
      prec+=acc_impokgE.getValorDec();
    s="SELECT p.* FROM "+tablaInd+" as p,"+tablaLin+" as l WHERE p.acc_ano = "+accAno+
        " AND p.emp_codi = "+empCodi+
        " and p.acc_serie = '"+serie+"'"+
        " and p.acc_nume = "+accNume+
        " AND p.emp_codi =  l.emp_codi "+
        " and p.acc_serie = l.acc_serie "+
        " and p.acc_nume = l.acc_nume "+
        " and p.acc_ano = l.acc_ano "+
        (agruLin?" and l.pro_codi = "+proCodi+
        (opVerPrecios.isSelected()?" and L.acl_prcom = "+prec:"")+
         " and l.acl_canti "+(canti>0?" >= 0":"< 0"):
        " and l.acl_nulin = "+linea)+
        " and p.acl_nulin = l.acl_nulin"+
        (hisRowid==0?"":
            " and p.his_rowid="+hisRowid+
            " and l.his_rowid="+hisRowid);
        
//    debug("verDesgLinea: "+s);
    jtDes.setEnabled(false);
    jtDes.removeAllDatos();
    if (!dtCon1.select(s))
    {
      if (nav.pulsado==navegador.EDIT || nav.pulsado==navegador.ADDNEW)
        jtDes.setEnabled(true);
      return;
    }
    swActDesg=false;
    double kilos=0;
    int nLin=0;
    do
    {
      ArrayList v=getDatosDesgl();
     
      jtDes.addLinea(v);
      kilos+=dtCon1.getDouble("acp_canti",true)==0?1:dtCon1.getDouble("acp_canti",true);
      nLin++;
    } while (dtCon1.next());
    kilosE.setValorDec(kilos);
    nLinE.setValorDec(nLin);
    if (nav.pulsado == navegador.ADDNEW || nav.pulsado==navegador.EDIT)
    {
      pro_codiE.setEditable(nLinE.getValorInt() <= 0);
    }
    else
    {
      pro_codiE.setEditable(false);
    }
    jtDes.requestFocusInicio();
    jtDes.setEnabled(false);
    swActDesg=true;
  }
  String getPais(int pais) throws SQLException
  {
    s=MantPaises.getNombrePais(pais, dtStat);
    if (s!=null)
      return s;
    else
      return "********";
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

  boolean borraInd(int nRow,int nInd) throws Exception
  {
    if (nInd == 0)
      return true;

    if (opAutoClas.isSelected())
    { // Busco el No de Linea dentro del Albaran.
      int n=getLinProducto(jtDes.getValorInt(nRow,JTD_NUMIND));
      if (n==-1)
      {
        msgBox("No encontrada Linea de Albaran donde esta este Producto");
        return false;
      }
      jt.setRowFocus(n);
    }

    int nLiAlb=jt.getValorInt(0);
    int nLinDes=jtDes.getValorInt(nRow,JTD_NUMLIN);
    s = "SELECT * FROM v_albcompar " +
        " WHERE emp_codi = " + emp_codiE.getValorInt() +
        " AND acc_ano = " + acc_anoE.getValorInt() +
        " and acc_nume = " + acc_numeE.getValorInt() +
        " and acc_serie = '" + acc_serieE.getText() + "'" +
        " and acl_nulin = "+nLiAlb+
        " and acp_numlin = "+nLinDes;

    if (!dtCon1.select(s,true))
    {
      msgBox("No ENCONTRADO Individuo en el Albaran");
      return false; // No encontrado Individuo
    }
    double canti=dtCon1.getDouble("acp_canti");

    s = "DELETE FROM v_albcompar " +
        " WHERE emp_codi = " + emp_codiE.getValorInt() +
        " AND acc_ano = " + acc_anoE.getValorInt() +
        " and acc_nume = " + acc_numeE.getValorInt() +
        " and acc_serie = '" + acc_serieE.getText() + "'" +
        " and acl_nulin = "+nLiAlb+
        " and acp_numlin = "+nLinDes;
    stUp.executeUpdate(s);
    if (nLiAlb==0)
    {
      msgBox("Individuo NO se pudo borrar (No encontrado)");
      return false;
    }
    double kgFac=0,prLiAlb=0,dtopp=0;
    s = "select * from v_albacol WHERE acc_ano = " + acc_anoE.getValorInt() +
        " and emp_codi = " + emp_codiE.getValorInt() +
        " and acc_nume = " + acc_numeE.getValorInt() +
        " and acc_serie = '" + acc_serieE.getText() + "'" +
        " and acl_nulin = " + nLiAlb;
    if (dtStat.select(s))
    {
      kgFac = dtStat.getDouble("acl_canfac");
      if (ARG_MODPRECIO)
      {
        dtopp=acl_dtoppE.getValorDec();
        prLiAlb=dtStat.getDouble("acl_prcom");
      }
    }
    s = "delete from v_albacol WHERE acc_ano = " + acc_anoE.getValorInt() +
        " and emp_codi = " + emp_codiE.getValorInt() +
        " and acc_nume = " + acc_numeE.getValorInt() +
        " and acc_serie = '" + acc_serieE.getText() + "'" +
        " and acl_nulin = " + nLiAlb;
    stUp.executeUpdate(s);
    nLinE.setValorDec(nLinE.getValorDec() - 1);
    kilosE.setValorDec(kilosE.getValorDec() - canti);

    if (opAutoClas.isSelected())
    {
      actAcuLomos();
      guardaLinAlb(nLiAlb, jt.getSelectedRow(), nLinE.getValorInt(),
                   kilosE.getValorDec(),kgFac,prLiAlb,acl_kgrecE.getValorDec(),
                   acl_comenE.getText(),dtopp);
      for (int n=0;n<jt.getRowCount();n++)
      {
        if (jt.getValorInt(n, JT_CANIND)==0) // N. Unidades = 0
        {
          borraLinea(n);
          jt.removeLinea(n);
        }
      }
    }
    else
    {
      guardaLinAlb(nLiAlb, jt.getSelectedRow(), nLinE.getValorInt(),
                   kilosE.getValorDec(),kgFac,prLiAlb,acl_kgrecE.getValorDec(),
                   acl_comenE.getText(),dtopp);
      jt.setValor("" + nLiAlb, 0);
      jt.setValor("" + nLinE.getValorInt(), JT_CANIND);
      jt.setValor("" + kilosE.getValorDec(), JT_KILALB);
      if (kilosE.getValorDec()==0 && opAutoCl)
        opAutoClas.setEnabled(true);
    }
    return true;
  }
  /**
   * Borra la linea de albaran.
   * @param row int N.De Linea dentro del grid
   * @throws Exception error BD
   */
  void borraLinea(int row) throws Exception
  {
      if (jt.getValorInt(row, 0) == 0)
        return;
      s = "SELECT * FROM v_albcompar " +
          " WHERE emp_codi = " + emp_codiE.getValorInt() +
          " AND acc_ano = " + acc_anoE.getValorInt() +
          " and acc_nume = " + acc_numeE.getValorInt() +
          " and acc_serie = '" + acc_serieE.getText() + "'" +
          " and acl_nulin = " + jt.getValorInt(row,0);
      if (dtCon1.select(s))
      {
        do
        { // Anulo el stock de todas las lineas de las partidas
          stkPart.anuStkPart(dtCon1.getInt("pro_codi"),
                     dtCon1.getInt("acc_ano"),
                     dtCon1.getInt("emp_codi"),
                     dtCon1.getString("acc_serie"),
                     dtCon1.getInt("acc_nume"),
                     dtCon1.getInt("acp_numind"),
                     alm_codiE.getValorInt(),
                     dtCon1.getDouble("acp_canti"),
                     dtCon1.getInt("acp_canind"));
        } while (dtCon1.next());
        s = "delete from v_albcompar WHERE acc_ano = " + acc_anoE.getValorInt() +
            " and emp_codi = " + emp_codiE.getValorInt() +
            " and acc_nume = " + acc_numeE.getValorInt() +
            " and acc_serie = '" + acc_serieE.getText() + "'" +
            " and acl_nulin = " + jt.getValorInt(row, 0);
        stUp.executeUpdate(s);
      }
      if (fcc_numeE.getValorInt()!=0 && opActFra.isSelected())
      {  // Busco la relacion con  Facturas y la anulo. NO BORRO LA LINEA DE LA FACTURA
        s="update v_falico set acc_ano =0, acc_nume=0,acl_nulin=0,acc_serie='' "+
           " WHERE emp_codi = "+emp_codiE.getValorInt()+
          " AND acc_ano =" +acc_anoE.getValorInt()  +
          " and acc_serie = '" +acc_serieE.getText()  + "'" +
          " and acc_nume = " + acc_numeE.getValorInt()+
          " and acl_nulin = "+jt.getValorInt(row, 0);
        stUp.executeUpdate(s);
      }
      s = "delete from v_albacol WHERE acc_ano = " + acc_anoE.getValorInt() +
          " and emp_codi = " + emp_codiE.getValorInt() +
          " and acc_nume = " + acc_numeE.getValorInt() +
          " and acc_serie = '" + acc_serieE.getText() + "'" +
          " and acl_nulin = " + jt.getValorInt(row, 0);
      stUp.executeUpdate(s);
  }
  /**
   * Poner Valores por defecto a la linea de Despice
   * @param rowFin int Linea final
   * @param rowOri int Linea Original
   */
  void ponValDefDes(int rowFin,int rowOri)
  {
    for (int n=2;n<jtDes.getColumnCount()-1;n++)
       jtDes.setValor(jtDes.getValString(rowOri,n),rowFin,n);
    jtDes.ponValores(rowFin);
  }

  int cambiaLinAlb(int linea)
  {
    try
    {
      if (swCargaAlb)
        return -1;
      s=pro_codiE.getNombArt();
      jt.setValor(s,linea,2);
      if (pro_codiE.getValorInt()!=0 &&  !pro_codiE.controla(false,false))
      {
        mensajeErr("PRODUCTO .... NO Valido");
        return 0;
      }
      if (! pro_codiE.isActivo())
      {
          mensajeErr("Producto NO esta ACTIVO");
          return 0;
      }
      if (prv_codiE.isNull())
        return -1;
      if (!prv_codiE.controla(false))
      {
        mensajeErr(prv_codiE.getMsgError());
        return 1;
      }
      if (!acc_copvfaE.controlar())
      {
        mensajeErr(acc_copvfaE.getMsgError());
        return 1;
      }
      if (pro_codiE.getTipoLote()!='V' && pro_codiE.getValorInt()>0)
      { // Guardo Linea de Albaran si Producto es de comentario
        double kgFac=0,prLiAlb=0,dtopp=0;
        int nLiAlb=jt.getValorInt(0);
        if (nLiAlb!=0)
        {
          s = "SELECT *  from v_albacol WHERE acc_ano = " +   acc_anoE.getValorInt() +
              " and emp_codi = " + emp_codiE.getValorInt() +
              " and acc_nume = " + acc_numeE.getValorInt() +
              " and acc_serie = '" + acc_serieE.getText() + "'" +
              " and acl_nulin = " + nLiAlb;
          if (dtStat.select(s))
          {
            kgFac = dtStat.getDouble("acl_canfac", true); // Kilos Facturados
            if (ARG_MODPRECIO)
            {
              prLiAlb = dtStat.getDouble("acl_prcom") + acc_impokgE.getValorDec()-
                 (dtStat.getDouble("acl_prcom") * acl_dtoppE.getValorDec() /100);
              dtopp = acl_dtoppE.getValorDec();
            }
//            borraRegLinAlb(nLiAlb);
          }
        }
        else
          nLiAlb=getNextLinAlb();

        guardaLinAlb(nLiAlb, jt.getSelectedRow(), acl_numcajE.getValorInt(),
                     acl_cantiE.getValorDec(), kgFac, prLiAlb,
                     acl_kgrecE.getValorDec(), acl_comenE.getText(),dtopp);
        jt.setValor(""+nLiAlb,0);
        ctUp.commit();
      }
//      if (jt.getValorInt(0) == 0 && !pro_codiE.isNull())
//      {
//        mensajeErr("Introduzca Individuos de Producto");
//        return 1;
//      }
      actAcuTot();
      return -1;
    }
    catch (Exception k)
    {
      Error("Error al cambiar Linea Albaran", k);
    }
    return 0;
  }
  /**
   * Actualiza todos los datos el albarán.
   * @return
   * @throws Exception
   */
  boolean actAlbaran() throws Exception
  {
    swCambioPrv=false;
   
    int nRow=jt.getRowCount();
//    double impBim=0;
    
    // Actualizo las Linea de Albaran.
    for (int n=0;n<nRow;n++)
    {
      if (jt.getValorDec(n,0)==0)
        continue;
      // Calculo Importes y unidades para cada linea.
      s="SELECT sum(acp_canind) as acp_canind,sum(acp_canti) as acp_canti  "+
          " FROM v_albcompar where acc_ano =" + acc_anoE.getValorInt() +
          " and emp_codi = " + emp_codiE.getValorInt() +
          " and acc_serie = '" + acc_serieE.getText() + "'" +
          " and acc_nume = " + acc_numeE.getValorInt()+
          " AND acl_nulin = "+jt.getValorInt(n,0);
//          " group by pro_codi ";
      dtAdd.select(s);
      jt.setValor(Formatear.Redondea(dtAdd.getDouble("acp_canti",true),2), n,4);
      jt.setValor(dtAdd.getInt("acp_canind",true), n,3);
      // Compruebo si todos los codigos de desglose son iguales que los de la linea.
      s="SELECT pro_codi  "+
          " FROM v_albcompar where acc_ano =" + acc_anoE.getValorInt() +
          " and emp_codi = " + emp_codiE.getValorInt() +
          " and acc_serie = '" + acc_serieE.getText() + "'" +
          " and acc_nume = " + acc_numeE.getValorInt()+
          " AND acl_nulin = "+jt.getValorInt(n,0)+
          " and pro_codi != "+jt.getValorInt(n,1);
      if ( dtAdd.select(s))
      { // Codigos diferentes. Envio correo y dejo mensaje aviso.
           enviaMailError("Alb. Compra: "+emp_codiE.getValorInt()+"-"+acc_anoE.getValorInt()+
                   acc_serieE.getText()+"-"+acc_numeE.getValorInt()+" Linea: "+jt.getValorInt(n,0)+
                   " productos diferentes. Desglose: "+dtAdd.getString("pro_codi")+
                   " Lineas: "+jt.getValorInt(n,1)
                   );
      }
      actDatosLinAlb(n); 
    }
   
    actDatosCabAlb();
    
    if (eje_numeE.getValorInt()!=0 && pcc_numeE.getValorInt()!=0)
    {
      s = "UPDATE pedicoc set acc_nume = " + acc_numeE.getValorInt() + "," +
          " acc_ano = " + acc_anoE.getValorInt() + "," +
          " acc_serie = '" + acc_serieE.getText() + "'," +
          " pcc_estrec = 'R', "+
          " acc_cerra = "+acc_cerraE.getValor()+
          " where emp_codi = " + emp_codiE.getValorInt() +
          " AND eje_nume = " + eje_numeE.getValorInt() +
          " and pcc_nume = " + pcc_numeE.getValorInt();
      stUp.executeUpdate(s);
    }
//    if (frtNume!=0)
//      actAcumFra(emp_codiE.getValorInt(),frtEjer,frtNume);
    return true;
  }
  void actDatosLinAlb(int n) throws SQLException 
  {
      String nombArt = pro_codiE.getNombArt(jt.getValString(n,1), EU.em_cod);
      s="SELECT * FROM v_albacol WHERE acc_ano =" + acc_anoE.getValorInt() +
          " and emp_codi = " + emp_codiE.getValorInt() +
          " and acc_serie = '" + acc_serieE.getText() + "'" +
          " and acc_nume = " + acc_numeE.getValorInt()+
           (jt.getValorInt(n, 0)==0?" and pro_codi = "+jt.getValorInt(n,JT_PROCOD)+
           "AND acl_canti "+(jt.getValorDec(n,JT_KILALB)>=0?" >= 0":"<0"):
          " and acl_nulin = " + jt.getValorInt(n, 0));
        
      if (! dtAdd.select(s,true))
        throw new SQLException("No encontrado Linea Albaran: "+jt.getValorInt(n,0)+"\n Select: "+s);
      dtAdd.edit(dtAdd.getCondWhere());
      dtAdd.setDato("pro_codi",jt.getValorInt(n,1));
      dtAdd.setDato("pro_nomart",nombArt);
      dtAdd.setDato("acl_canti",jt.getValorDec(n,JT_KILALB));
      if (ARG_MODPRECIO)
      {
        dtAdd.setDato("acl_prcom", jt.getValorDec(n,JT_PRCOM)- (jt.getValorDec(n,JT_PRCOM)*jt.getValorDec(n,JT_DTOPP)/100) +
            acc_impokgE.getValorDec());
        dtAdd.setDato("acl_dtopp", jt.getValorDec(n,JT_DTOPP));
      }

      dtAdd.setDato("alm_codi", alm_codiE.getValorInt());
      dtAdd.setDato("acl_numcaj", jt.getValorInt(n, 3));
      dtAdd.setDato("acl_kgrec",jt.getValorDec(n,6));
      dtAdd.setDato("acl_comen",jt.getValString(n,13));
      dtAdd.setDato("acl_porpag ",jt.getValBoolean(n,14)?-1:0);
      dtAdd.setDato("acl_totfra",acc_totfraE.getValor());
      dtAdd.update(stUp);
      s="UPDATE v_articulo SET pro_feulco = TO_DATE('"+acc_fecrecE.getText()+"','dd-MM-yyyy'),"+
        " pro_prvulco = "+prv_codiE.getValorInt()+
        " where pro_codi = "+jt.getValorInt(n,1);
//        " and emp_codi = "+emp_codiE.getValorInt();
      stUp.executeUpdate(dtAdd.getStrSelect(s));
  }
  /**
   * Actualizo campo total factura en v_albacoc
   * @param dt
   * @param empCodi
   * @param accAno
   * @param accSerie
   * @param accNume
   * @param accTotfra
   * @return
   * @throws SQLException 
   */
//  private int  actImporteAlb(DatosTabla dt,int empCodi,int accAno,String accSerie,int accNume,double accTotfra) throws SQLException
//  {
//    return dt.executeUpdate( "UPDATE v_albacoc set acc_totfra= "+accTotfra+
//         " WHERE acc_ano =" + accAno +
//        " and emp_codi = " + empCodi +
//        " and acc_serie = '" + accSerie + "'" +
//        " and acc_nume = " + accNume);
//  }
//  private int  actImporteAlb(DatosTabla dt,int empCodi,int accAno,String accSerie,int accNume) throws SQLException
//  {
//    return actImporteAlb(dt,empCodi,accAno,accSerie,accNume, getImporteAlb(dt,empCodi,accAno,accSerie,accNume));
//  }
  public static double getImporteAlb(DatosTabla dt,int empCodi,int accAno,String accSerie,int accNume) throws SQLException
  {
      String s="select sum(acl_canti*acl_prcom) as impalb  from v_albacol "+
         " WHERE acc_ano =" + accAno +
        " and emp_codi = " + empCodi +
        " and acc_serie = '" + accSerie + "'" +
        " and acc_nume = " + accNume;
      dt.select(s);
      return dt.getDouble("impalb",true);
  }
  /**
   * Actualiza datos Cabecera del Albarán
   * @throws SQLException 
   */
  private void actDatosCabAlb() throws SQLException
  {
    s = "SELECT * FROM V_albacoc WHERE acc_ano =" + acc_anoE.getValorInt() +
        " and emp_codi = " + emp_codiE.getValorInt() +
        " and acc_serie = '" + acc_serieE.getText() + "'" +
        " and acc_nume = " + acc_numeE.getValorInt();
    if (!dtAdd.select(s, true))
      throw new SQLException("No encontrado Cabecera Albaran.\n Select: " + s);
//    frtEjer=dtAdd.getInt("frt_ejerc",true);
//    frtNume=dtAdd.getInt("frt_nume",true);
    dtAdd.edit(dtAdd.getCondWhere());
    if (prv_codiE.getValorInt()!=dtAdd.getInt("prv_codi"))
        swCambioPrv=true;
    dtAdd.setDato("prv_codi",prv_codiE.getValorInt());
    dtAdd.setDato("acc_fecrec",acc_fecrecE.getText(),"dd-MM-yyyy");
    dtAdd.setDato("acc_copvfa",acc_copvfaE.getValorInt());
    dtAdd.setDato("acc_obser",acc_obserE.getText());
    dtAdd.setDato("acc_cerra",acc_cerraE.getValor());
    dtAdd.setDato("acc_totfra",acc_totfraE.getValor());
    dtAdd.setDato("sbe_codi",sbe_codiE.getValorInt());
//    dtAdd.setDato("acc_totfra",acc_totfraE.getValor());
    if (ARG_MODPRECIO)
    {
      dtAdd.setDato("acc_portes", acc_portesE.getValor());
      dtAdd.setDato("acc_impokg", acc_impokgE.getValorDec());
    }
    dtAdd.update(stUp);
  }
  void borraAlbaran() throws Exception
  {
    s = "DELETE FROM v_albacoc WHERE acc_ano =" + acc_anoE.getValorInt() +
        " and emp_codi = " + emp_codiE.getValorInt() +
        " and acc_serie = '" + acc_serieE.getText() + "'" +
        " and acc_nume = " + acc_numeE.getValorInt();
    if (dtAdd.executeUpdate(s) != 1)
      throw new Exception("No encontrado Cabecera Albaran.\n Select: " + s);
    if (acc_serieE.getText().equals("Y"))
    { // Borro los datos de las partidas en ventas
      s = "DELETE FROM v_albcopave WHERE acc_ano =" + acc_anoE.getValorInt() +
          " and emp_codi = " + emp_codiE.getValorInt() +
          " and acc_serie = '" + acc_serieE.getText() + "'" +
          " and acc_nume = " + acc_numeE.getValorInt();
      dtAdd.executeUpdate(s);
    }
    /**
     * Pone el Pedido a Pendiente.
     */
    s="SELECT * FROM pedicoc where emp_codi = " + EU.em_cod +
        " AND eje_nume = " + eje_numeE.getValorInt() +
        " and pcc_nume = " + pcc_numeE.getValorInt();
    if (dtCon1.select(s,true))
    {
      dtCon1.edit(dtCon1.getCondWhere());
      dtCon1.setDato("acc_nume",0);
      dtCon1.setDato("acc_ano",0);
      dtCon1.setDato("acc_serie","");
      dtCon1.setDato("pcc_estrec","P");
      dtCon1.setDato("acc_cerra",0); // Lo marco como NO cerrado
      dtCon1.update(stUp);
    }

    int nRow = jt.getRowCount();
// Actualizo las Linea de Albaran.
    for (int n = 0; n < nRow; n++)
    {
      if (jt.getValorInt(n, 1) == 0)
        continue;
      borraLinea(n);
    }

  }
  /**
   * Usada cuando se cambia el importe de la linea sin estar en edición.
   * @param col Columna del grid
   * @param row Linea del grid
   */
  void actPrecio(int col,int row)
  {
    try
    {
      if (! acl_prcomE.hasCambio() && !acl_dtoppE.hasCambio())
          return;
//      if (avc_anoE.getValorInt() != 0 && acc_cerraE.getValorInt() != 0)
//      {
//        acl_prcomE.setValorDec(jt.getValorDec(row, 5));
//        msgBox("Imposible modificar precios Alb. de serie 'Y' si esta cerrado");
//        return;
//      }
    
      if ( fcc_numeE.getValorInt()!=0)
      {
          mensajeErr("No se puede modificar precios en albaranes ya facturados");
          acl_prcomE.setValorDec(acl_prcomE.getValorDecAnt());
          jt.setValor(acl_prcomE.getValorDec(),row,JT_PRCOM);
          acl_dtoppE.setValorDec(acl_dtoppE.getValorDecAnt());
          jt.setValor(acl_dtoppE.getValorDec(),row,JT_DTOPP);
          return;
      }
      acl_prcomE.resetCambio();
      acl_dtoppE.resetCambio();
      actPrecioLinAlb(col,row,acl_prcomE.getValorDec(),acl_porpagE.isSelected(), acl_dtoppE.getValorDec());
     
      if (opIncPortes.isSelected() && !acl_porpagE.isSelected() && col==JT_PRCOM)
       // Me han modificado el precio y esta marcado q se sumen los portes. 
         acl_prcomE.setValorDec(acl_prcomE.getValorDec()+acc_impokgE.getValorDec());
         
    
      jt.setValor(acl_prcomE.getValorDec(),row,JT_PRCOM);
//      actAcuTot();
//      actImporteAlb(dtAdd ,emp_codiE.getValorInt(),acc_anoE.getValorInt(),acc_serieE.getText(),acc_numeE.getValorInt(),
//            imptotE.getValorDec());
    
      ctUp.commit();
      statusBar.setMsgRapido("Linea: "+ (row+1)+" Cambiado costo a "+acl_prcomE.getValorDec());
      if (jf!=null)
      {
          if (Formatear.comparaFechas(acc_fecrecE.getDate(),feulin)<=0)
          { // Apunte de que se ha modificado el albaran anterior a inventario
             jf.ht.clear();
             jf.ht.put("%a", acc_anoE.getValorInt()+"|"+emp_codiE.getValorInt()+
                      "|"+acc_serieE.getText()+"|"+acc_numeE.getValorInt());
              jf.ht.put("%p",jt.getValString(row,1));
             jf.guardaMens("CD", jf.ht);
          }
      }
    } catch (Exception k)
    {
      Error("Error al actualizar PRECIO ALBARAN",k);
    }
  }
  /**
   * Actualiza precio linea albarán
   * @param col
   * @param row
   * @param aclPrCom
   * @param aclPorpag
   * @param aclDtopp
   * @throws SQLException 
   */
  void actPrecioLinAlb(int col,int row,double aclPrCom,boolean aclPorpag,double aclDtopp ) throws SQLException
  {
      double impPortes=aclPorpag?0:acc_impokgE.getValorDec();

      if (col!=JT_PRCOM)
      { // Me han modificado el dto 
         if (opIncPortes.isSelected() && !aclPorpag) 
            aclPrCom-=acc_impokgE.getValorDec(); //  Se incluyen portes en costo y los portes no son pagados
      }
      aclPrCom-= (aclPrCom*aclDtopp/100); // Le quito el DTO PP
      aclPrCom+=impPortes; // Si no se estan viendo los portes en pantalla.
 
      aclPrCom=Formatear.Redondea(aclPrCom,3);
      s = "UPDATE v_albacol SET acl_prcom = " +aclPrCom +
          ", acl_dtopp = "+acl_dtoppE.getValorDec()+
          " WHERE acc_ano = " + acc_anoE.getValorInt() +
          " and emp_codi = " + emp_codiE.getValorInt() +
          " and acc_nume = " + acc_numeE.getValorInt() +
          " and acc_serie = '" + acc_serieE.getText() + "'" +
          (opAgrupar.isSelected()?" and pro_codi = "+jt.getValorInt(row,JT_PROCOD)+
           "AND acl_canti "+(jt.getValorDec(row,JT_PRCOM)>=0?" >= 0":"<0"):
          " and acl_nulin = " + jt.getValorInt(row, 0));
      stUp.executeUpdate(s);
  }
  void cambiarAlmacen()
  {
    if (dtCons.getNOREG())
        return;
    mensaje("Cambiando Almacen ... Regularizando Movimientos");
    this.setEnabled(false);
    alm_codiE.setEnabled(false);

    new miThread("")
    {
      @Override
      public void run() {
        cambiarAlmacen0();
      }
    };
  }
  void cambiarAlmacen0()
  {
    try
    {

      s = "SELECT * FROM v_albcompar  " +
          " WHERE acc_ano = " + acc_anoE.getValorInt() +
          " AND emp_codi = " + emp_codiE.getValorInt() +
          " and acc_serie = '" + acc_serieE.getText() + "'" +
          " and acc_nume = " + acc_numeE.getValorInt();
      if (dtCon1.select(s))
      {
        do
        {
          // Anular el stock sobre el Almacen Original
          if (stkPart.anuStkPart(dtCon1.getInt("pro_codi"),
                                 dtCon1.getInt("acc_ano"),
                                 dtCon1.getInt("emp_codi"),
                                 dtCon1.getString("acc_serie"),
                                 dtCon1.getInt("acc_nume"),
                                 dtCon1.getInt("acp_numind"),
                                 almOrig,
                                 dtCon1.getDouble("acp_canti"),
                                 dtCon1.getInt("acp_canind")) == 0)
          {
            s="No encontrado registro a borrar de albaran: "+
                  dtCon1.getInt("pro_codi")+"-"+dtCon1.getInt("acc_ano")+":"+
                                 dtCon1.getInt("emp_codi")+":"+
                                 dtCon1.getString("acc_serie")+":"+
                                 dtCon1.getInt("acc_nume")+": ALM:"+almOrig;
            aviso(s);
            mensajeErr(s,false);
          }
          stkPart.sumar(acc_anoE.getValorInt(),acc_serieE.getText(),
                          acc_numeE.getValorInt(),dtCon1.getInt("acp_numind"),
                          dtCon1.getInt("pro_codi"), // Producto
                          alm_codiE.getValorInt(), // Almacen
                          dtCon1.getDouble("acp_canti"), // Kilos
                          dtCon1.getInt("acp_canind"),  // Num. Unidades
                          acc_fecrecE.getText(),
                          actStkPart.CREAR_SI,
                          prv_codiE.getValorInt(), // Producto
                          dtCon1.getDate("acp_feccad")   // Fecha Cad.
                          );
        }  while (dtCon1.next());
        s = "UPDATE v_albacol SET alm_codi =  " + alm_codiE.getValorInt() +
            " WHERE acc_ano = " + acc_anoE.getValorInt() +
            " AND emp_codi = " + emp_codiE.getValorInt() +
            " and acc_serie = '" + acc_serieE.getText() + "'" +
            " and acc_nume = " + acc_numeE.getValorInt();
        dtAdd.executeUpdate(s);
        ctUp.commit();
        almOrig=alm_codiE.getValorInt();
      }
      msgBox("Almacen cambiado a " + alm_codiE.getValor());
      mensaje("");
      this.setEnabled(true);
      alm_codiE.setEnabled(true);
    }
    catch (Exception k)
    {
      Error("Error al cambiar almacen", k);
    }
  }
  
  void actTotFra()
  {
    try
    {
      if (dtCons.getNOREG())
        return;
      s = "UPDATE v_albacoc SET acc_totfra = " + acc_totfraE.getValor() +
          " WHERE acc_ano = " + acc_anoE.getValorInt() +
          " and emp_codi = " + emp_codiE.getValorInt() +
          " and acc_nume = " + acc_numeE.getValorInt() +
          " and acc_serie = '" + acc_serieE.getText() + "'";
      stUp.executeUpdate(s);
      ctUp.commit();
      msgBox("Estado de Totalmente facturado ... cambiado");
    }
    catch (Exception k)
    {
      Error("Error al actualizar estado Totalmente facturado", k);
    }
  }
  
  void actCerrado()
  {
    try
    {
      if (dtCons.getNOREG())
        return;
      String condWhere=" emp_codi = " + emp_codiE.getValorInt()+
          " and acc_ano = " + acc_anoE.getValorInt() +
          " and acc_nume = " + acc_numeE.getValorInt() +
          " and acc_serie = '" + acc_serieE.getText() + "'";

      if (acc_cerraE.getValorInt()!=0 && acc_serieE.getText().equals("Y"))
      { // Va a cerrarlo y serie es "Y"
        s="SELECT l.pro_codi FROM v_albacol as l, v_articulo as a where l."+condWhere+
            " and acl_prcom = 0 "+
            " and l.pro_codi = a.pro_codi "+
            " and a.pro_tiplot = 'V'";
        if (dtStat.select(s))
        {
          msgBox("No puede cerrar un Albaran de serie 'Y' que tiene precios sin poner");
          acc_cerraE.setValor("0");
          return;
        }
      }
      s = "UPDATE v_albacoc SET acc_cerra = " + acc_cerraE.getValor() +
          " WHERE "+condWhere;
      stUp.executeUpdate(s);
      ctUp.commit();
     // acl_prcomE.setEditable(avc_numeE.getValorInt()==0 || acc_cerraE.getValorInt()==0);
      msgBox("Estado de CERRADO ... cambiado");
    }
    catch (Exception k)
    {
      Error("Error al actualizar ESTADO de Albaran (Cerrado/Abierto)", k);
    }

  }
  /**
   * Actualiza Campo  Comentario de la linea mandada.
   * @param row linea a modificar.
   */
  void actComent(int row)
  {
    try
    {
      s = "UPDATE v_albacol SET  acl_comen = '" + acl_comenE.getText() + "'" +
          " WHERE acc_ano = " + acc_anoE.getValorInt() +
          " and emp_codi = " + emp_codiE.getValorInt() +
          " and acc_nume = " + acc_numeE.getValorInt() +
          " and acc_serie = '" + acc_serieE.getText() + "'" +
          (opAgrupar.isSelected() ? " and pro_codi = " + jt.getValorInt(row, 1) +
           "AND acl_canti " + (jt.getValorDec(row, 4) >= 0 ? " >= 0" : "<0") :
           " and acl_nulin = " + jt.getValorInt(row, 0));
      stUp.executeUpdate(s);
      ctUp.commit();
      statusBar.setMsgRapido("Linea: " + row +
                             " Cambiado Comentario y Portes Pagados");
    }
    catch (Exception k)
    {
      Error("Error al actualizar PRECIO ALBARAN", k);
    }
  }
  /**
   * Actualiza columna Portes pagados
   * @param row 
   */
  private  void actPortesPag(int row)
  {
     try
    {
      if (acl_porpagE.isSelected())
      {
          if (mensajes.mensajeYesNo("Poner linea con porte pagado, seguro ?")!=mensajes.YES)
          {
              acl_porpagE.setSelected(false);
              return;
          }
      }
      
      s = "UPDATE v_albacol SET acl_porpag  = " +  (acl_porpagE.isSelected()?-1:0)+
          " WHERE acc_ano = " + acc_anoE.getValorInt() +
          " and emp_codi = " + emp_codiE.getValorInt() +
          " and acc_nume = " + acc_numeE.getValorInt() +
          " and acc_serie = '" + acc_serieE.getText() + "'" +
          (opAgrupar.isSelected() ? " and pro_codi = " + jt.getValorInt(row, JT_PROCOD) +
           "AND acl_canti " + (jt.getValorDec(row, JT_KILALB) >= 0 ? " >= 0" : "<0") :
           " and acl_nulin = " + jt.getValorInt(row, 0));
      stUp.executeUpdate(s);
    
      ctUp.commit();
      statusBar.setMsgRapido("Linea: " + row +
                             " Cambiado Portes Pagados");
    }
    catch (Exception k)
    {
      Error("Error al actualizar Portes Pagados en Linea Albaran", k);
    }  
  }
  void BbusPed_actionPerformed()
  {
    try
    {
      conped = new copedco(EU, vl, ARG_MODPRECIO);
      conped.addInternalFrameListener(new InternalFrameAdapter()
      {
                @Override
        public void internalFrameClosing(InternalFrameEvent e)
        {
          ej_consPed();
        }
      });
      conped.emp_codiE.setEnabled(false);
      conped.sbe_codiE.setEnabled(false);

      vl.add(conped);
      conped.setLocation(25, 25);
      conped.setVisible(true);
      this.setEnabled(false);
      this.setFoco(conped);
      conped.iniciarVentana();
      conped.emp_codiE.setValorInt(emp_codiE.getValorInt());
      conped.sbe_codiE.setValorInt(sbe_codiE.getValorInt());
      if (!prv_codiE.isNull())
      {
        conped.prv_codiE.setText(prv_codiE.getText());
        conped.Baceptar_actionPerformed();
      }
    }
    catch (Exception j)
    {
      setEnabled(true);
    }
  }

  void ej_consPed()
  {
    if (conped.getNumPed() != 0)
    {
      pcc_numeE.setValorDec(conped.getNumPed());
      eje_numeE.setValorDec(conped.getEjePed());
      if (prv_codiE.isNull())
      {
          prv_codiE.setText(conped.prv_codiE.getText());         
          acc_copvfaE.setText(conped.prv_codiE.getText());  
      }

    }
    conped.dispose();

    this.setEnabled(true);
    this.toFront();
    try
    {
      this.setSelected(true);
    }
    catch (Exception k)
    {}
    this.setFoco(null);
    pcc_numeE.requestFocus();
  }

  boolean cargaLinPedido(boolean soloCheck) throws SQLException
  {
    if (pcc_numeE.getValorInt() == 0)
    {
      if (ARG_ALBSINPED)
        return true;
      else
      {
        mensajeErr("Introducir N. de Pedido");
        return false;
      }
    }
    s = "SELECT * FROM pedicoc as c " +
        " where c.emp_codi = " + emp_codiE.getValorInt() +
        " and c.sbe_codi = "+sbe_codiE.getValorInt()+
        " AND c.eje_nume = " + eje_numeE.getValorInt() +
        " and c.pcc_nume = " + pcc_numeE.getValorInt()+
        " and prv_codi = "+prv_codiE.getValorInt();
    jt.setEnabled(false);
    if (! soloCheck)
      jt.removeAllDatos();
    pcl_comenE.setText("");
    pcc_comenE.setText("");
    if (!dtCon1.select(s))
    {
      mensajeErr("Cabecera Pedido NO encontrado o de difentes empresa/subempresa");
      return false;
    }

    pcc_comenE.setText(dtCon1.getString("pcc_comen"));
    acc_portesE.setValor(dtCon1.getString("pcc_portes"));
    estPedi = dtCon1.getString("pcc_estrec").charAt(0);
    if (estPedi == 'R' || estPedi == 'C')
    {
      if (nav.pulsado == navegador.ADDNEW
          || (ejPedAnt != eje_numeE.getValorInt() &&
              nuPedAnt != pcc_numeE.getValorInt()))
      {
        mensajeErr("Pedido YA Esta Facturado o Cancelado (" + estPedi + ")");
        return false;
      }
    }

    s = "SELECT pro_codi,pcl_comen, ";
    if (estPedi == 'P')
      campPedi = " pcl_precpe," +
          " pcl_nucape as pcl_nucape, " +
          " pcl_cantpe as pcl_cantpe ";
    else if (estPedi == 'C')
      campPedi= " pcl_precco as pcl_precpe, " +
          " pcl_nucaco as pcl_nucape, " +
          " pcl_cantco as pcl_cantpe ";
    else
      campPedi = " pcl_precfa as pcl_precpe, " +
          " pcl_nucafa as pcl_nucape, " +
          " pcl_cantfa as pcl_cantpe ";
    s += campPedi+"  FROM pedicol as l " +
        " where l.emp_codi = " + EU.em_cod +
        " AND l.eje_nume = " + eje_numeE.getValorInt() +
        " and l.pcc_nume = " + pcc_numeE.getValorInt() +
        " order by pro_codi";
    verDatPedido(EU.em_cod,eje_numeE.getValorInt(),pcc_numeE.getValorInt());
    if (!dtCon1.select(s))
    {
      mensajeErr("Lineas de  Pedido NO encontrado");
      return false;
    }
    if (soloCheck)
      return true;
/*    if (nav.pulsado==navegador.EDIT)
    {
      if (ejPedAnt != eje_numeE.getValorInt() ||
          nuPedAnt != pcc_numeE.getValorInt() ||
          prv_codiE.getValorInt() != prvAnt)
      {
        s = "UPDATE pedicoc set acc_nume = " + acc_numeE.getValorInt() + "," +
            " acc_ano = " + acc_anoE.getValorInt() + "," +
            " acc_serie = '" + acc_serieE.getText() + "'," +
            " pcc_estrec = 'R' " +
            " where emp_codi = " + EU.em_cod +
            " AND eje_nume = " + eje_numeE.getValorInt() +
            " and pcc_nume = " + pcc_numeE.getValorInt();
        stUp.executeUpdate(s);
        s = "UPDATE pedicoc set acc_nume = " + 0 + "," +
            " acc_ano = " + 0 + "," +
            " acc_serie = ''," +
            " pcc_estrec = 'P' " +
            " where emp_codi = " + EU.em_cod +
            " AND eje_nume = " + ejPedAnt +
            " and pcc_nume = " + nuPedAnt;
        stUp.executeUpdate(s);

        ctUp.commit();
        verDatos(dtCons,false);
      }
      return true;
    }
      }
 */
    pcl_comenE.setText(dtCon1.getString("pcl_comen"));
    do
    {
      ArrayList v =new ArrayList();
      v.add("0"); // NL
      v.add(dtCon1.getString("pro_codi"));
      v.add(getNomArtPed(dtCon1.getInt("pro_codi"),eje_numeE.getValorInt(),pcc_numeE.getValorInt()));
      v.add("0"); // Unidades.
      v.add("0"); // Kgs.
      v.add("0"); // Pr.Com
      v.add("0"); // Recorte
      v.add(dtCon1.getString("pcl_nucape"));
      v.add(dtCon1.getString("pcl_cantpe"));
      v.add(dtCon1.getString("pcl_precpe"));
      v.add(dtCon1.getString("pcl_nucape"));
      v.add(dtCon1.getString("pcl_cantpe"));
      v.add(dtCon1.getString("pcl_precpe"));
      v.add("");
      v.add(false);
      v.add(acc_dtoppE.getValorDec() );
      jt.addLinea(v);
    } while (dtCon1.next());
    return true;
  }

  String getNomArtPed(int proCodi,int ejeNume,int pccNume) throws SQLException
  {
    if (nav.pulsado == navegador.EDIT)
      mensaje("Editando Albaran ... ", false);
    if (nav.pulsado == navegador.ADDNEW)
      mensaje("Insertando Albaran ... ", false);
    pcl_comenE.setText("");
    if (pccNume==0)
      return null;

    s="SELECT pro_nomb,pcl_comen FROM pedicol WHERE emp_codi = "+EU.em_cod+
        " and eje_nume = "+ejeNume+
        " and pcc_nume = "+pccNume+
        " and pro_codi = "+proCodi;
    if (! dtStat.select(s))
      return null;
    pcl_comenE.setText(dtStat.getString("pcl_comen"));
    String nombArt=dtStat.getString("pro_nomb");

    if (nombArt!=null)
    {
      if (nav.pulsado==navegador.EDIT)
        mensaje("Editando Albaran ... ARTICULO: " + nombArt,false);
      if (nav.pulsado==navegador.ADDNEW)
        mensaje("Insertando Albaran ... ARTICULO: " + nombArt,false);
    }
    return nombArt;
  }
  /**
   * Actualizar Acumulados de Producto
   * @param proCodi int Codigo de Producto
   */
  void actAcuPro(int proCodi)
  {
    int nRow = jt.getRowCount();
    int unid = 0;
    double kg = 0;
    for (int n = 0; n < nRow; n++)
    {
      if (jt.getValorInt(n, 1) == proCodi)
      {
        unid += jt.getValorInt(n, 3);
        kg += jt.getValorDec(n, 4);
      }
    }
    alc_unidE.setValorDec(unid);
    alc_kgsE.setValorDec(kg);
  }
  void actLinPed(int proCodi) throws Exception
  {
    actAcuPro(proCodi);
    actAcuPed(proCodi);
    dif_unidE.setValorDec(alc_unidE.getValorInt() -pcl_unidE.getValorInt());
    dif_kgsE.setValorDec(alc_kgsE.getValorDec()-pcl_kgsE.getValorDec());
    if (dif_kgsE.getValorDec() != 0 &&  pcl_kgsE.getValorDec() !=0)
      dif_porcE.setValorDec(dif_kgsE.getValorDec() / pcl_kgsE.getValorDec() * 100);
    else
      dif_porcE.setValorDec(0);
  }

  void actAcuPed(int proCodi) throws Exception
  {
    s="SELECT ";
    if (estPedi == 'P')
      campPedi = " sum(pcl_nucape) as pcl_nucape, " +
          " sum(pcl_cantpe) as pcl_cantpe ";
    else if (estPedi == 'C')
      campPedi= " sum(pcl_nucaco) as pcl_nucape, " +
          " sum(pcl_cantco) as pcl_cantpe ";
    else
      campPedi = " sum(pcl_nucafa) as pcl_nucape, " +
          " sum(pcl_cantfa) as pcl_cantpe ";

    s += campPedi+" FROM pedicol WHERE emp_codi = " + EU.em_cod +
        " and eje_nume = " + eje_numeE.getValorDec() +
        " and pcc_nume = " + pcc_numeE.getValorDec() +
        " and pro_codi = " + proCodi;
    dtCon1.select(s);
    pcl_unidE.setValorDec(dtCon1.getDouble("pcl_nucape",true));
    pcl_kgsE.setValorDec(dtCon1.getDouble("pcl_cantpe",true));
    getNomArtPed(proCodi,eje_numeE.getValorInt(),pcc_numeE.getValorInt());
  }
  String getStrSql()
  {
    return  "SELECT acc_fecrec,emp_codi,acc_ano,acc_serie,acc_nume "+
    " FROM v_albacoc WHERE emp_codi = " + EU.em_cod +
    (EU.getSbeCodi()==0?"":" and sbe_codi = "+EU.getSbeCodi())+
    " and acc_fecrec > CURRENT_DATE - 60 "+
    " ORDER BY acc_fecrec,acc_ano,acc_serie,acc_nume ";
  }
  void setEditCant(char tipLote)
  {
    if (nav.pulsado != navegador.ADDNEW && nav.pulsado != navegador.EDIT)
    {
      if (acl_cantiE.isEnabled())
      {
          acl_cantiE.setEnabled(false);
          acl_numcajE.setEnabled(false);
      }
      return;
    }

    acl_cantiE.setEnabled(tipLote != 'V');
    acl_numcajE.setEnabled(tipLote != 'V');
  }
  /**
    * Ver Datos Lineas del pedido
    * @param empCodi empresa
    * @param ejeNume Ejercicio
    * @param pccNume Numero de Pedido
    * @throws SQLException Si ocurre un error
    */
   void verDatPedido(int empCodi,int ejeNume, int pccNume) throws SQLException
   {

     jtPed.removeAllDatos();
     s = "SELECT * FROM pedicoc " +
         " where emp_codi = " + empCodi +
         " AND eje_nume = " + ejeNume +
         " and pcc_nume = " + pccNume ;
     if (!dtCon1.select(s))
       return;

     char estPediLocal = dtCon1.getString("pcc_estad").charAt(0);
     s="SELECT pro_codi, pro_nomb, pcl_feccad, ";
     if (estPediLocal=='P')
       s+= " pcl_precpe,"+
          " pcl_nucape as pcl_nucape, "+
         " pcl_cantpe as pcl_cantpe ";
     else if (estPediLocal=='C')
       s+= " pcl_precco as pcl_precpe, "+
           " pcl_nucaco as pcl_nucape, "+
         " pcl_cantco as pcl_cantpe ";
     else
       s+= " pcl_precfa as pcl_precpe, "+
           " pcl_nucafa as pcl_nucape, "+
         " pcl_cantfa as pcl_cantpe ";
    s+=", div_codi,pcl_comen FROM pedicol WHERE emp_codi = "+empCodi+
         " and eje_nume = "+ejeNume+
         " and pcc_nume = "+pccNume+
         " order by pcl_numli";
     if (! dtCon1.select(s))
       return;

     do
     {
       Vector v=new Vector();
       v.addElement(dtCon1.getString("pro_codi"));
       v.addElement(dtCon1.getString("pro_nomb"));
       v.addElement(dtCon1.getFecha("pcl_feccad","dd-MM-yyyy"));
       v.addElement(dtCon1.getString("pcl_nucape"));
       v.addElement(dtCon1.getString("pcl_cantpe"));
       if (ARG_MODPRECIO)
         v.addElement(dtCon1.getString("pcl_precpe"));
       else
         v.addElement("0");
       v.addElement(div_codiE.getText(dtCon1.getString("div_codi")));
       v.addElement(dtCon1.getString("pcl_comen"));
       jtPed.addLinea(v);
     } while (dtCon1.next());
     jtPed.requestFocusInicio();
   }

   void Bimpri_actionPerformed()
   {
     if (dtCons.getNOREG())
       return;
     try {
       java.util.HashMap mp = new java.util.HashMap();
       JasperReport jr;
       mp.put("ejeini",acc_anoE.getText());
       mp.put("ejefin",acc_anoE.getText());
       mp.put("albini",acc_numeE.getText());
       mp.put("albfin",acc_numeE.getText());
       mp.put("empcodi",emp_codiE.getText());
       mp.put("incPortes",Boolean.valueOf(opIncPortes.isSelected()));
       jr = (JasperReport) util.getJasperReport(EU,
                         (opIncDet.isSelected()?"albcodes":"lialbcom"));

       s = "SELECT *  FROM v_albacoc as c " +
           " where emp_codi = " + emp_codiE.getValorInt() +
           " and acc_ano = " + acc_anoE.getValorInt() +
           " and acc_nume = " + acc_numeE.getValorInt() +
           " and acc_serie = '" + acc_serieE.getText() + "'" +
           " and exists (select * from v_albacol where emp_codi = " + emp_codiE.getValorInt() +
           " and acc_ano = " + acc_anoE.getValorInt() +
           " and acc_nume = " + acc_numeE.getValorInt() +
           " and acc_serie = '" + acc_serieE.getText() + "')";

       regDesg=false;
//       debug(s);
       rs=dtCon1.getStatement().executeQuery(dtCon1.parseaSql(s));
       swIniRep = true;
       cambio=0;
       JasperPrint jp = JasperFillManager.fillReport(jr, mp,this);
       gnu.chu.print.util.printJasper(jp,EU);
     }
     catch (Exception k)
     {
       Error("Error al imprimir albaran", k);
     }
   }

   public boolean next() throws JRException
   {
     try
     {
       boolean leeLin=false;
       boolean res=false;
       if (swIniRep)
       {
         swIniRep=false;
         res = rs.next();
         leeLin=true;
       }
       else
       {
           if (regDesg)
           {
             if (dtAdd.next())
               return true; // Tiene desglose de Partidas
           }
           res=dtBloq.next(); // Busco siguiente linea de Albaran
           if (!opAgrupar.isSelected())
             cambio++; // Utilizada para impr. grupo producto aunque sea el mismo
           if (res)
             return true; // Tiene Linea de Albaranes
           res = rs.next(); // Busco siguiente Cabecera de Albaran
       }
       if (leeLin && res)
       {
         s = "SELECT prv_nomb FROM v_proveedo " +
             "where  prv_codi =" + rs.getInt("prv_codi");
         if (!dtStat.select(s))
           prvNomb = "Proveedor NO ENCONTRADO";
         else
           prvNomb = dtStat.getString("prv_nomb");
         String condWhere = "l.emp_codi = " + rs.getInt("emp_codi") +
             " and acc_ano = " + rs.getInt("acc_ano") +
             " and acc_nume = " + rs.getInt("acc_nume") +
             " and acc_serie = '" + rs.getString("acc_serie") + "'";
         if (!opAgrupar.isSelected())
         {
           s = "SELECT acl_nulin,l.pro_codi,l.pro_nomart, " +
               " acl_canti, acl_numcaj,acl_kgrec," +
               " acl_prcom " +
               " FROM v_albacol as l " +
               " where " + condWhere +
               " order by acl_nulin ";
         }
         else
         {
           condWhere = "SELECT 0 AS acl_nulin,pro_codi,acl_prcom, " +
               "sum(acl_canti) as acl_canti, sum(acl_numcaj) as acl_numcaj " +
               " FROM v_albacol AS l WHERE " + condWhere;
           s = condWhere + " AND l.acl_canti >= 0" +
               " GROUP BY pro_codi,acl_prcom" +
               " UNION " +
               condWhere + " AND l.acl_canti < 0 " +
               " GROUP BY pro_codi,acl_prcom" +
               " ORDER BY 2";
         }


         dtBloq.select(s);
         if (opIncDet.isSelected())
         {
           s ="SELECT p.* FROM v_albcompar as p,v_albacol as l WHERE p.acc_ano = " +
               rs.getInt("acc_ano") +
               " AND p.emp_codi = " + rs.getInt("emp_codi") +
               " and p.acc_serie = '" + rs.getString("acc_serie") + "'" +
               " and p.acc_nume = " + rs.getInt("acc_nume") +
               " AND p.emp_codi =  l.emp_codi " +
               " and p.acc_serie = l.acc_serie " +
               " and p.acc_nume = l.acc_nume " +
               " and p.acc_ano = l.acc_ano " +
               (opAgrupar.isSelected() ?
                " and l.pro_codi = " + dtBloq.getInt("pro_codi") +
                (opVerPrecios.isSelected() ?
                 " and L.acl_prcom = " + dtBloq.getDouble("acl_prcom") : "") +
                " and l.acl_canti " +
                (dtBloq.getDouble("acl_canti") > 0 ? " >= 0" : "< 0") :
                " and l.acl_nulin = " + dtBloq.getInt("acl_nulin")) +
               " and p.acl_nulin = l.acl_nulin";
           regDesg= dtAdd.select(s);
         }
       }
       return res;
     }
     catch (Exception k)
     {
       throw new JRException(k);
     }

   }

   public Object getFieldValue(JRField f) throws JRException
   {
       String campo=f.getName().toLowerCase();
     try
     {
       if (campo.equals("acc_fecrec"))
         return rs.getDate("acc_fecrec");
       if (campo.equals("acc_serie"))
         return rs.getString(campo);
       if (campo.equals("prv_codi") || campo.equals("acc_ano") || campo.equals("acc_nume"))
         return new Integer(rs.getInt(campo));
       if (campo.equals("acc_impokg"))
       {
         if (opVerPrecios.isSelected())
           return new Double(rs.getDouble(campo));
         else
           return new Double(0);
       }
       if (campo.equals("prv_nomb"))
         return prvNomb;
       // Datos de Lineas
       if (campo.equals("pro_nomb"))
       {
         if ( opAgrupar.isSelected() ||  dtBloq.getString("pro_nomart", true).equals(""))
         {
           s="SELECT pro_nomb FROM v_articulo WHERE pro_codi = "+dtBloq.getInt("pro_codi");
           if (dtStat.select(s))
             return dtStat.getString("pro_nomb");
           else
             return "**ARTICULO NO ENCONTRADO**";
         }
         else
           return dtBloq.getString("pro_nomart", true);
       }

       if (campo.equals("pro_codi"))
         return new Integer(dtBloq.getInt("pro_codi"));
       if ( campo.equals("acl_canti"))
        return new Double(dtBloq.getDouble(campo));
       if (campo.equals("acl_prcom"))
       {
         if (opVerPrecios.isSelected())
           return new Double(dtBloq.getDouble("acl_prcom")-(opIncPortes.isSelected()?0:rs.getDouble("acc_impokg")));
         else
           return new Double(0);
       }
       if (campo.equals("acl_impor"))
       {
         if (opVerPrecios.isSelected())
           return new Double(dtBloq.getDouble("acl_canti")*
                           (dtBloq.getDouble("acl_prcom")-(opIncPortes.isSelected()?0:rs.getDouble("acc_impokg"))));
         else
           return new Double(0);
       }
      if (campo.equals("acl_numcaj"))
        return new Integer(dtBloq.getInt(campo));

       if (campo.equals("acp_numind"))
         return new Integer(dtAdd.getInt(campo));
       if (campo.equals("acp_canti"))
         return new Double(dtAdd.getDouble(campo));
       if (campo.equals("acp_nucrot"))
         return dtAdd.getString(campo);
       if (campo.equals("mat_nomb"))
       {
         s = "SELECT mat_nrgsa FROM v_matadero " +
             " WHERE mat_codi = " + dtAdd.getInt("mat_codi");
         if (! dtStat.select(s))
           return "*****";
         else
           return dtStat.getString("mat_nrgsa");
       }
       if (campo.equals("sde_nomb"))
       {
         s = "SELECT sde_nrgsa FROM v_saladesp " +
             " WHERE sde_codi = " + dtAdd.getInt("sde_codi");
         if (!dtStat.select(s))
           return "*****";
         else
           return dtStat.getString("sde_nrgsa");
       }
       if (campo.equals("pai_nacid"))
         return getPais(dtAdd.getInt("acp_painac"));
       if (campo.equals("pai_engor"))
         return getPais(dtAdd.getInt("acp_engpai"));
       if (campo.equals("pai_sacrif"))
         return getPais(dtAdd.getInt("acp_paisac"));
       if (campo.equals("cambio"))
         return new Integer(cambio);
     }
     catch (Exception k)
     {
       throw new JRException(k);
     }
     throw new JRException("Campo: "+campo+" NO VALIDO");

   }
   void BCamFeEnt_actionPerformed()
   {
     frChFeEn.setVisible(true);
     this.setEnabled(false);
     fereinE.setText(acc_fecrecE.getText());
     ferefiE.setText(acc_fecrecE.getText());
     ferepoE.requestFocus();
   }
   void cambiaRefProd()
   {
     pro_codcamE.setText(jt.getValString(1), true);
 //     System.out.println("en CambiaRefProd");
     frProd.setVisible(true);
     MantAlbCom.this.setEnabled(false);
     try
     {
       frProd.setSelected(true);
     }
     catch (PropertyVetoException k)
     {}

     javax.swing.SwingUtilities.invokeLater(new Thread()
     {
            @Override
       public void run()
       {
         pro_codcamE.requestFocus();
       }
     });


   }

   void BcanCam_actionPerformed()
   {
     frProd.setVisible(false);
     this.setEnabled(true);
//     System.out.println(jt.getSelectedRow());
     jtRequestFocusSelected();
   }

   void Baccafe_actionPerformed()
   {
     if (fereinE.getError() || fereinE.isNull())
     {
       mensajes.mensajeAviso("Fecha Inicio NO VALIDA");
       fereinE.requestFocus();
       return;
     }
     if (ferefiE.getError() || ferefiE.isNull())
     {
       mensajes.mensajeAviso("Fecha Final NO VALIDA");
       ferefiE.requestFocus();
       return;
     }
     if (ferepoE.getError() || ferepoE.isNull())
     {
       mensajes.mensajeAviso("Fecha A establecer NO VALIDA");
       ferepoE.requestFocus();
       return;
     }
     try {
       s = "UPDATE v_albacoc SET acc_fecrec = to_date('" + ferepoE.getText() +
           "','dd-MM-yyyy')" +
           " WHERE  acc_fecrec >= to_date('" + fereinE.getText() +
           "','dd-MM-yyyy')" +
           " and acc_fecrec <= to_date('" + ferefiE.getText() +
           "','dd-MM-yyyy')";
       int nCoAf = dtAdd.executeUpdate(s, stUp);
       ctUp.commit();
       mensajes.mensajeAviso("Actualizados: " + nCoAf + " Albaranes");
       Bcacafe_actionPerformed();
       verDatos(dtCons);
     } catch (Exception k)
     {
       Error("Error al Actualizar Albaranes",k);
     }
   }
   void Bcacafe_actionPerformed()
  {
    frChFeEn.setVisible(false);
    this.setEnabled(true);
    nav.requestFocus();
  }
  /**
   * Cambiar codigo producto en Lineas
   */
  void BaceCam_actionPerformed()
   {
     try
     {
       if (!pro_codcamE.controlar())
       {
         mensajeErr(pro_codcamE.getMsgError());
         return;
       }
//       if (pro_codcamE.getValorInt() != jt.getValorInt(1))
//       {
         new miThread("")
         {
           @Override
           public void run()
           {
             cambRefPro();
           }


         };
//       }
     } catch (Exception k)
     {
       Error("Error al Cambiar Referencia de Producto",k);
     }
     BcanCam_actionPerformed();
   }


   void imprEtiLin()
   {
     try {
       for (int n = 0; n < jtDes.getRowCount(); n++)
         imprEtiq(n, jtDes.getValorInt(n, 0));
     } catch (Exception k)
     {
       Error("Error al imprimir etiquetas de una linea Albaran",k);
     }
   }
   /**
    * Cambiar Un producto de Referencia
    * @param nLiAlb int Linea Albaran del Alb. Compra
    * @param proCodi int Codigo Producto
    * @param proNomb String Nombre Producto.
    * @throws SQLException
    */
   void actRefProd(int nLiAlb, int proCodi, String proNomb) throws SQLException
   {
     frProd.setEnabled(false);
     // Actualizo Linea de Albaran
     int nReg=0;
     s = "UPDATE v_albacol SET pro_codi = " + proCodi + "," +
         " pro_nomart = '" + proNomb + "'" +
         " WHERE acc_ano = " + acc_anoE.getValorInt() +
         " and emp_codi = " + emp_codiE.getValorInt() +
         " and acc_nume = " + acc_numeE.getValorInt() +
         " and acc_serie = '" + acc_serieE.getText() + "'" +
         " and acl_nulin = " + nLiAlb;
     stUp.executeUpdate(s);

     // Actualizo Stock-Partidas y Desglose de Lineas
     s = "SELECT * FROM v_albcompar WHERE acc_ano = " + acc_anoE.getValorInt() +
         " AND emp_codi = " + emp_codiE.getValorInt() +
         " and acc_serie = '" + acc_serieE.getText() + "'" +
         " and acc_nume = " + acc_numeE.getValorInt() +
         " and acl_nulin = " + nLiAlb;
     if (dtCon1.select(s))
     {
       do
       {
         mensaje("Actualizando Individuo: "+dtCon1.getInt("acp_numind"));

         // Actualizo el registro de Stock Partidas.
//         stkPart.cambiaProd(dtStat,proCodi,dtCon1.getInt("pro_codi") ,
//             acc_anoE.getValorInt(),
//             emp_codiE.getValorInt(),
//             acc_serieE.getText(),
//             acc_numeE.getValorInt(),
//             dtCon1.getInt("acp_numind"),
//             alm_codiE.getValorInt());

         s = "UPDATE v_albcompar SET  pro_codi= " + proCodi +
             " WHERE acc_ano = " + acc_anoE.getValorInt() +
             " AND emp_codi = " + emp_codiE.getValorInt() +
             " and acc_serie = '" + acc_serieE.getText() + "'" +
             " and acc_nume = " + acc_numeE.getValorInt() +
             " and acl_nulin = " + nLiAlb+
             " and acp_numlin = "+dtCon1.getInt("acp_numlin");
         nReg=stUp.executeUpdate(s);
       }  while (dtCon1.next());
     }
     mensaje("");
     ctUp.commit();
     frProd.setEnabled(true);
   }
   /**
    * Actualizar los datos de la Factura
    *
    * @throws Exception si hay error de BD
    */
   void actDatosFra() throws Exception
   {
     int empCodi=emp_codiE.getValorInt();
     int accAno=acc_anoE.getValorInt();
     String accSerie=acc_serieE.getText();
     int accNume=acc_numeE.getValorInt();
     double impokg=acc_impokgE.getValorDec();
     if (accNume==0)
       return; // Sin Albaranes

     double impLin,impLiT;
     s="SELECT * FROM v_falico WHERE emp_codi = "+empCodi+
         " AND acc_ano =" +accAno +
         " and acc_serie = '" +accSerie + "'" +
         " and acc_nume = " + accNume;
     if (! dtAdd.select(s,true))
       return;
     do
     {
       s="SELECT acl_prcom,acl_canfac,acl_canti FROM v_albacol "+
        " WHERE acc_ano =" + accAno +
        " and emp_codi = " + empCodi +
        " and acc_serie = '" + accSerie + "'" +
        " and acc_nume = " + accNume+
        " and acl_nulin = "+ dtAdd.getInt("acl_nulin");
      if (! dtCon1.select(s))
      {
        aviso("pdalbaco: (actDatFra)\n" +s);
        continue; // Esto no se deberia dar ... SI DE DA ENVIO UN CORREO DE AVISO
      }
      // Si los Kilos Fact. segun albaran y Kilos de Fra NO son iguales. Le doy un mensaje
      // al Usuario. Probablemente esa factura habra que revisarla. EL PROCESO CONTINUA
      if (dtAdd.getDouble("fcl_canti") != dtCon1.getDouble("acl_canfac"))
      {
        msgBox("REVISAR Linea "+dtAdd.getInt("fcl_numlin")+"  DE FRA: "+dtAdd.getDouble("fcc_nume")+
               " CON KILOS EN FRA: "+dtAdd.getDouble("fcl_canti")+
               " Y KG EN ALBARAN:"+dtCon1.getDouble("acl_canfac")+"\n LA ACTUALIZACION CONTINUA");
      }
      dtAdd.edit("emp_codi = "+ empCodi+
         " AND eje_nume =" + dtAdd.getInt("eje_nume")+
         " and fcc_nume = " + dtAdd.getInt("fcc_nume")+
         " and fcl_numlin = "+ dtAdd.getInt("fcl_numlin"));
      dtAdd.setDato("fcl_prcom",dtCon1.getDouble("acl_prcom")-impokg);
      dtAdd.setDato("fcl_canti",dtCon1.getDouble("acl_canti"));
      dtAdd.update(stUp);
     } while (dtAdd.next());
     // Actualizar Importes de cabecera de Facturas.
     double impLi0,impIva,impRec;

     s = "SELECT distinct c.eje_nume,c.fcc_nume " +
         " FROM v_facaco as c, v_falico as l WHERE  c.emp_codi = l.emp_codi " +
         " AND c.eje_nume = l.eje_nume " +
         " and c.fcc_nume = l.fcc_nume " +
         " and l.emp_codi = "+empCodi+
         " AND l.acc_ano =" + accAno +
         " and l.acc_serie = '" + accSerie+ "'" +
         " and l.acc_nume = " + accNume;
     if (! dtAdd.select(s))
       return;
     do
     {
       impLiT=0;
       s = "SELECT l.*,c.fcc_dtopp,c.fcc_dtocom,c.fcc_piva1,c.fcc_prec1 "+
           " FROM v_falico as l,v_facaco as c "+
           "  WHERE l.emp_codi = " + empCodi+
           " and l.eje_nume = " + dtAdd.getInt("eje_nume") +
           " and l.fcc_nume = " + dtAdd.getInt("fcc_nume")+
           " and c.emp_codi = l.emp_codi " +
           " AND c.eje_nume = l.eje_nume " +
           " and c.fcc_nume = l.fcc_nume ";
       if (dtCon1.select(s))
       {
         do
         {
           impLin = Formatear.Redondea(dtCon1.getDouble("fcl_canti") * dtCon1.getDouble("fcl_prcom"),3);
           if (dtCon1.getString("fcl_tipdes").equals("%") &&
               dtCon1.getDouble("fcl_dto", true) > 0)
             impLin -= impLin * dtCon1.getDouble("fcl_dto", true) / 100;
           else
             impLin -= dtCon1.getDouble("fcl_dto", true);
           impLiT += Formatear.Redondea(impLin,3);
         }
         while (dtCon1.next());
       }
       impLiT=Formatear.Redondea(impLiT,3);
       impLi0 = impLiT;
       if (dtCon1.getDouble("fcc_dtopp") != 0)
         impLiT -= Formatear.redondea(impLi0 * dtCon1.getDouble("fcc_dtopp") / 100,3);
       if (dtCon1.getDouble("fcc_dtocom") != 0)
         impLiT -= Formatear.redondea(impLi0 * dtCon1.getDouble("fcc_dtocom") / 100,3);
       impIva = Formatear.redondea(impLiT * dtCon1.getDouble("fcc_piva1", true) / 100,3);
       impRec = Formatear.redondea(impLiT * dtCon1.getDouble("fcc_prec1", true) / 100,3);
       s = "UPDATE  v_facaco SET fcc_sumlin =" + impLi0 + "," +
           " fcc_basim1 =" + impLiT + "," +
           " fcc_impiv1 =" + impIva + "," +
           " fcc_impre1 =" + impRec + "," +
           " fcc_sumtot =" + (impLiT + impIva + impRec) +
           " where emp_codi = " + empCodi +
           " AND eje_nume =" + dtAdd.getInt("eje_nume") +
           " and fcc_nume = " + dtAdd.getInt("fcc_nume");
       stUp.executeUpdate(dtCon1.getStrSelect(s));
     } while (dtAdd.next());
   }
   /**
    * Actualiza el Importe de TODAS los Albaranes que incluyan esta factura de
    * Tranportista
    * @param empCodi int Empresa
    * @param frtEjer int Ejercicio
    * @param frtNume int Numero de Fra
    * @throws SQLException Error en DB
    * @throws ParseException Error en DB
    * @deprecated 
    */
   void actAcumFra(int empCodi,int frtEjer,int frtNume) throws SQLException,java.text.ParseException
    {
      s = "SELECT * FROM fratraca where emp_codi = " + empCodi +
          " and eje_nume = " + frtEjer +
          " and frt_nume = " + frtNume;
      if (! dtAdd.select(s))
        return;
      double frtBasimp=dtAdd.getDouble("frt_basimp");
      double impPortes=dtAdd.getDouble("frt_prekil");
      s = "SELECT sum(acl_canti) as acl_canti " +
          " FROM v_albacol as l, v_albacoc as c " +
          " WHERE c.emp_codi = " + empCodi+
          " and l.emp_codi = " + empCodi +
          " and c.frt_nume = "+frtNume+
          " and c.frt_ejerc = "+frtEjer+
          " and l.acl_porpag  = -1 "+ // Portes de LINEA marcados como PAGADOS
          " and c.acc_ano = l.acc_ano " +
          " and c.acc_nume = l.acc_nume " +
          " and c.acc_serie = l.acc_serie " +
          " and c.acc_portes = 'D' ";
      dtAdd.select(s);
      double frtPrekil=0;
      if (dtAdd.getDouble("acl_canti",true)!=0)
        frtPrekil=  Formatear.Redondea(frtBasimp  / dtAdd.getDouble("acl_canti",true),3);

      s = "SELECT acc_ano,acc_nume,acc_serie,acc_impokg FROM v_albacoc "+
          " WHERE frt_ejerc =" + frtEjer +
          " and emp_codi = " + empCodi+
          " and frt_nume = " + frtNume+
          " AND acc_portes = 'D' ";
      if (! dtAdd.select(s,true))
        return;
      do
      {
        s = "UPDATE v_albacol set acl_prcom = acl_prcom - "+( impPortes - frtPrekil  )+
            " WHERE emp_codi = " + empCodi +
            " and acc_ano = " + dtAdd.getInt("acc_ano") +
            " and acc_nume = " + dtAdd.getInt("acc_nume") +
            " and acc_serie = '" + dtAdd.getString("acc_serie")+ "'"+
            " and acl_porpag  = 0 ";
        stUp.executeUpdate(dtAdd.getStrSelect(s));
        dtAdd.edit(dtAdd.getCondWhere());
        if (dtAdd.getDouble("acc_impokg") - impPortes + frtPrekil != frtPrekil)
          aviso("pdalbco2: IMPORTE de portes != 0 en fra: "+frtNume+" Mod. Alb: "+dtAdd.getInt("acc_nume"));
        dtAdd.setDato("acc_impokg",dtAdd.getDouble("acc_impokg") - impPortes + frtPrekil);
        dtAdd.update(stUp);
      }  while (dtAdd.next());
      // Actualizo Cabecera de Fra. de Transportista
      s = "UPDATE fratraca SET frt_prekil = " + frtPrekil +
          " where emp_codi = " + empCodi +
          " and eje_nume = " + frtEjer +
          " and frt_nume = " + frtNume;
      stUp.executeUpdate (s);
    }

    void jtRequestFocusSelected()
    {
      javax.swing.SwingUtilities.invokeLater(new Thread()
      {
            @Override
        public void run()
        {
          jt.requestFocusSelected();
        }
      });
    }

    void jtDesRequestFocusSelected()
    {
      javax.swing.SwingUtilities.invokeLater(new Thread()
      {
            @Override
        public void run()
        {
          jtDes.requestFocusSelected();
        }
      });
    }
    @Override
    public void matar(boolean cerrarConexion)
     {
       if (muerto)
         return;
       if (frChFeEn != null)
       {
         frChFeEn.setVisible(false);
         frChFeEn.dispose();
       }
       if (frProd!=null)
       {
         frProd.setVisible(false);
         frProd.dispose();
       }

       super.matar(cerrarConexion);
     }

     void MIactNombreProd(int row) throws Exception
     {
       String nombArt;

       getNomArtPed(pro_codiE.getValorInt(), eje_numeE.getValorInt(), pcc_numeE.getValorInt());
       nombArt = pro_codiE.getNombArt(pro_codiE.getText(), EU.em_cod);
       setEditCant(pro_codiE.getTipoLote());
       jt.setValor(nombArt, row, 2);
       pro_nombE.setText(nombArt);
     }

     void actNombProdVert(int row) throws SQLException
     {
       String nombArt;

       nombArt = pro_codverE.getNombArt(jtRecl.getValString(row,0));
//       debug(""+row);
       jtRecl.setValor(nombArt, row, 1);
//       pro_nomverE.setText(nombArt);
     }

     private void confGridRecl() throws Exception
     {
       jtRecl.setMaximumSize(new Dimension(752, 315));
       jtRecl.setMinimumSize(new Dimension(752, 315));
       jtRecl.setPreferredSize(new Dimension(752, 315));

       ArrayList v = new ArrayList();
       v.add("Producto"); //0
       v.add("Nombre"); // 1
       v.add("Indiv."); // 2
       v.add("Unds"); // 3
       v.add("Peso"); // 4
       v.add("Importe"); // 5
       v.add("Fec.Recl"); // 6
       v.add("Estado"); // 7
       v.add("Fec.Resol"); // 8
       v.add("Tipo Reg."); // 9 Tipo Regularizaci�n
       v.add("Trasp."); // 10 Traspasado
       v.add("Cliente"); // 11 Cod. Cliente
       v.add("Nomb.Cliente"); // 12 Nombre Cliente
       v.add("Coment"); // 13
       v.add("N.Vert"); // 14
       jtRecl.setCabecera(v);
       jtRecl.setAnchoColumna(new int[] {70, 120, 40, 40,65, 65,60,30,60,180,30,50,150,150,50});
       jtRecl.setAlinearColumna(new int[]{2, 0, 2, 2,2,2, 1,1,1,0,1,2,0,0,2});
       jtRecl.setFormatoColumna(3, "---9");
       jtRecl.setFormatoColumna(4, "--,--9.99");
       jtRecl.setFormatoColumna(5, "--,--9.99");
       jtRecl.setFormatoColumna(10, "BSN");

       tir_codiE.setAncTexto(30);
       tir_codiE.setFormato(Types.DECIMAL, "##9", 3);

       s = "SELECT * FROM v_motregu ORDER BY tir_codi";
       if (dtCon1.select(s))
       {
         do
         {
           tir_codiE.addDatos(dtCon1.getString("tir_codi"),
                              dtCon1.getString("tir_nomb") + "  (" + dtCon1.getString("tir_afestk") +
                              ")", true);
         }
         while (dtCon1.next());
       }
       tir_codiE.setValorInt(paregalm.TIRRECPRV);
       rgs_cantiE.setValorDec(1);
       pro_codverE.setProNomb(null);
//       rgs_kilosE.setEnabled(false);
       rgs_recprvE.setAdmiteCar(CTextField.CHAR_LIMIT);
       rgs_recprvE.setStrCarEsp("PEAR");
       rgs_recprvE.setMayusc(true);
       rgs_recprvE.setText("P");
       rgs_recprvE.setToolTipText("Valores Posibles: Pendiente,Aceptado,Rechazado,rEclam. Pend.");
       rgs_recprvE.setHorizontalAlignment(CTextField.CENTER);
       rgs_recprvE.setAceptaNulo(false);
       estIniE.addItem("Pend","P");
       estIniE.addItem("Acep","A");
       estIniE.addItem("Rech","R");
       estIniE.addItem("Recl.Pend","E");
       estFinE.addItem("Pend", "P");
       estFinE.addItem("Acep", "A");
       estFinE.addItem("Rech", "R");
       estFinE.addItem("Recl.Pend","E");
       rgs_traspE.setSelected(true);

       rgs_fechaE.setText(Formatear.getFechaAct("dd-MM-yy"));
       rgs_numeE.setEnabled(false);
       pro_nomverE.setEnabled(false);
       rgs_fecresE.resetTexto();
       rgs_fecresE.setAceptaNulo(true);
       ArrayList v1 = new ArrayList();
       v1.add(pro_codverE.getTextField());
       v1.add(pro_nomverE);
       v1.add(pro_numindE); // 2
       v1.add(rgs_cantiE); //3
       v1.add(rgs_kilosE); //4
       v1.add(rgs_prreguE); // 5
       v1.add(rgs_fechaE); // 6
       v1.add(rgs_recprvE); // 7
       v1.add(rgs_fecresE); // 8 Fecha Resolución
       v1.add(tir_codiE); // 9 Tipo Regularización.
       v1.add(rgs_traspE); // 10
       v1.add(rgs_clidevE.cli_codiE); // 10
       v1.add(rgs_clinombE); // 10
       v1.add(rgs_comenE); // 11
       v1.add(rgs_numeE); // 12

       jtRecl.setCampos(v1);
       rgs_fecresE.setError(false);
       jtRecl.setDefButton(Baceptar);
     }

     void guardaLinRecl(int row) throws Exception
     {
       if (jtRecl.getValorInt(row,ROWNVERT)!=0)
       {
         s = "select * from v_regstock WHERE rgs_nume = " + jtRecl.getValorInt(row, ROWNVERT);
         if (dtStat.select(s))
         {
           pRegAlm.setSbeCodi(dtStat.getInt("sbe_codi"));
//           pRegAlm.setRgsCliDev(dtStat.getInt("rgs_clidev",true));
           pRegAlm.borrarRegistro(jtRecl.getValorInt(row, ROWNVERT)); // Borro el Registro Anterior
         }
       }
       pRegAlm.setRegNume(0);
       if (pRegAlm.insRegistro()==-1)
           throw new Exception("Error al Modificar Registro");

       jtRecl.setValor(""+pRegAlm.getNumReg(),row,ROWNVERT);
       actAcumVert(row);
       mensajeErr("Vertedero de linea: "+row+" ... Guardado");
     }

     void actAcumVert(int row)
     {
       double kilos=0,impor=0;
       int unid=0;
       int numVert=0;
       for (int n=0;n<jtRecl.getRowCount();n++)
       {
         if (jtRecl.getValorInt(n,0)==0)
           continue;
         numVert++;
         if (row==n)
         {
           kilos+=rgs_kilosE.getValorDec();
           unid+=rgs_cantiE.getValorInt();
           impor+=(rgs_kilosE.getValorDec()*rgs_prreguE.getValorDec());
         }
         else
         {
           kilos += jtRecl.getValorDec(n, 4);
           unid+=jtRecl.getValorInt(n,3);
           impor+=(jtRecl.getValorDec(n,4)*jtRecl.getValorDec(n,5));
         }
       }
       vertNPiezE.setValorDec(unid);
       vertKilosE.setValorDec(kilos);
       vertImporE.setValorDec(impor);
     }

     /**
      * Cambia Linea en el Grid de Vertederos.
      * @param row int
      * @return int Columna con Error (<0 si no hay error)
      */
     int cambiaLinRecl(int row)
     {
       try
       {

         if (jtRecl.getValorInt(row, 0) == 0)
         {
           if (jtRecl.getValorInt(row, 2) != 0)
           {
             if (getDatosInd(jtRecl.getValorInt(row, 2)))
             {
   //             pro_codverE.setText(dtStat.getString("pro_codi"));
               jtRecl.setValor(dtStat.getString("pro_codi"), row, 0);
             }
           }
         }

         if (jtRecl.getValorInt(row, 0) == 0)
         {
           if (jtRecl.getValorInt(row, 10) == 0)
             return -1; // No trato la linea
           else
           { // Ya tenia Numero de Regularizaci�n
             mensajeErr("Introduzca CODIGO de producto");
             return 0;
           }
         }
         if (!jtRecl.hasCambio())
           return -1;

         if (!pro_codverE.controla(false, false))
         {
           mensajeErr("Código de Producto NO existe");
           return 0;
         }
         jtRecl.setValor(pro_codverE.getLikeProd().getString("pro_nomb"), row, 1); // Actualizo el nombre (por si acaso).
         if (pro_numindE.getValorInt() == 0)
         {
           mensajeErr("Introduzca N�mero de Individuo");
           return 2;
         }
         if (!getDatosInd(pro_numindE.getValorInt()))
         {
           mensajeErr("Individuo NO encontrado en vertederos");
           return 2;
         }
         if (dtStat.getInt("pro_codi") != pro_codverE.getValorInt())
         {
           mensajeErr("Producto Introducido NO coincide con el del individuo");
           return 0;
         }
         if (rgs_recprvE.isNull())
         {
           mensajeErr("Introduzca Estado de Reclamación");
           return 7;
         }
         if (! tir_codiE.controla(false))
         {
           mensajeErr("Tipo Regularización NO valida");
           return ROWTREG;
         }
         int rgsRecprv = getRecPrv(rgs_recprvE.getText());

         double kilos = rgs_kilosE.getValorDec();
         int unids = rgs_cantiE.getValorInt();
         if (jtRecl.getValorInt(row, ROWNVERT) != 0)
         {
           if (pRegAlm.getDatosReg(dtStat, jtRecl.getValorInt(row, ROWNVERT)))
           {
             if (pro_numindE.getValorInt() == dtStat.getInt("pro_numind") &&
                 pro_codverE.getValorInt() == dtStat.getInt("pro_codi"))
             {
               kilos -= dtStat.getDouble("rgs_kilos");
               unids -= dtStat.getDouble("rgs_canti");
             }
           }
         }
         if (rgs_clidevE.getValorInt()!=0)
         {
           if (! rgs_clidevE.controlar(cliPanel.LOSTFOCUS))
           {
             mensajeErr("Cliente NO valido");
             return 11;
           }
         }
         if (rgs_traspE.isSelected())
         {
           if (!actStkPart.checkStock(dtStat, pro_codverE.getValorInt(), acc_anoE.getValorInt(),
                                      emp_codiE.getValorInt(), acc_serieE.getText(),
                                      acc_numeE.getValorInt(),
                                      pro_numindE.getValorInt(), alm_codiE.getValorInt(),
                                      kilos, unids))
           {
             mensajeErr("NO hay suficiente STOCK para crear este vertedero");
             return 2;
           }
         }
         pRegAlm.setCampos(rgs_fechaE.getDate(), pro_codverE.getValorInt(),
                           emp_codiE.getValorInt(), acc_anoE.getValorInt(),
                           acc_serieE.getText(), acc_numeE.getValorInt(),
                           pro_numindE.getValorInt(),
                           rgs_cantiE.getValorInt(), rgs_kilosE.getValorDec(),
                           alm_codiE.getValorInt(), tir_codiE.getValorInt(), prv_codiE.getValorInt(),
                           rgs_comenE.getText(),rgs_prreguE.getValorDec(),
                           rgs_fecresE.getDate(),rgs_clidevE.getValorInt(),sbe_codiE.getValorInt(),rgs_traspE.isSelected()?1:0,
                           rgsRecprv,acc_anoE.getValorInt(),acc_serieE.getText(),acc_numeE.getValorInt());

         guardaLinRecl(row);
         ctUp.commit();
       }
       catch (Exception k)
       {
         Error("Error al cambiar Linea Grid de Vertedero", k);
         return -1;
       }
       return -1;
     }

    boolean getDatosInd(int numInd) throws SQLException
    {
      s = "SELECT p.*,l.pro_codi,l.acl_prcom FROM v_albcompar as p,v_albacol as l  " +
          " where p.emp_codi =  l.emp_codi " +
          " and p.acc_serie = l.acc_serie " +
          " and p.acc_nume = l.acc_nume " +
          " and p.acc_ano = l.acc_ano " +
          " and p.acl_nulin = l.acl_nulin" +
          " and p.emp_codi = " + emp_codiE.getValorInt() +
          " AND p.acc_serie = '" + acc_serieE.getText() + "'" +
          " and p.acc_nume = " + acc_numeE.getValorInt() +
          " and p.acc_ano = " + acc_anoE.getValorInt() +
          " and p.acp_numind = " + numInd;
//      debug("s: "+s);
      return dtStat.select(s);
    }
     /**
      * Actualizar Linea de vertederos despues de introducir numero de Individuo
      * @param numInd int
      * @param row int Numero de Linea en Grid Vertedero
      * @throws SQLException
      */
     void actLinVert(int numInd,int row) throws SQLException
     {
       if (getDatosInd(numInd))
       {
//         jtRecl.setValor("1",row,3);
         if (rgs_kilosE.getValorDec()==0)
         {
//           rgs_kilosE.setValorDec(dtStat.getDouble("acp_canti"));
           jtRecl.setValor(dtStat.getString("acp_canti"), row, 4);
//           rgs_cantiE.setValorDec(dtStat.getDouble("acp_canind"));
           jtRecl.setValor(dtStat.getString("acp_canind"), row, 3);
         }
         if (jtRecl.getValorInt(row, 10) == 0)
         {
           jtRecl.setValor(dtStat.getString("acl_prcom"), row,5);
//           rgs_prreguE.setValorDec(dtStat.getDouble("acl_prcom"));
         }

         if (pro_codverE.isNull())
         {
//           pro_codverE.setText(dtStat.getString("pro_codi"));
           jtRecl.setValor(dtStat.getString("pro_codi"),row,0);
           actNombProdVert(row);
         }
       }
     }
    public static String getRandomCrotal(String crotalBase)
    {
         int len = crotalBase.length();
         int numAleatorio;
         String numCrot =crotalBase;
            if (len > 6) {
                numCrot = crotalBase.substring(0, len - 6);
                len = 6;
            } else
                numCrot="";
            if (len==0) len=6;
            int numMult = 1;
            String formato = "";
            for (int n = 0; n < len; n++) {
                numMult = numMult * 10;
                formato = formato + "9";
            }
            numAleatorio=(int) (Math.random() * numMult)+1;
            if (numAleatorio>=numMult)
                numAleatorio=numMult-1;
            numCrot = numCrot + Formatear.format(numAleatorio, formato);
            return numCrot;
    }
   /**
    * Genera un numero de crotal aleatorio sobre el ya introducido. Modifica los ultimos 6 digitos.
    */
    private void genNumCrotal() {
       
        String numCrot=null;
        int numIntentos=0;
        for (numIntentos=0;numIntentos<10;numIntentos++)
        {
            
            numCrot = getRandomCrotal( acp_nucrotE.getText());

            try {
                if (proNumcro > 0) {
                    int numCrotal = getNumCrotal(numCrot, 0, jt.getValorInt(1));
                    if (numCrotal < proNumcro) {
                        break;
                    }
                } else {
                    break;
                }
            } catch (SQLException k) {
                Error("Error al generar numero crotal automaticamente", k);
                return;
            }
        }
        if (numIntentos>=10)
        {
            msgBox("No ha sido posible generar un número crotal nuevo automaticamente. Aumente el numero de digitos del crotal");
            return;
        }
        acp_nucrotE.setText(numCrot);
        jtDes.requestFocus(jtDes.getSelectedRow(),3);
    }

   
 


     int getRecPrv(String tipo)
     {
       if (tipo.equals("P"))
         return paregalm.ESTPEND; // Vertedero Prov. Pendiente
       if (tipo.equals("A"))
         return paregalm.ESTACEP; // Vertedero Proveedor (Aceptado)
       if (tipo.equals("E"))
         return paregalm.PENDREC; // Pend. Reclamar
       return  paregalm.ESTRECH; // Vertedero Sala Calle.
     }

     boolean isLineaEntera(int row) throws SQLException
     {
       s = "SELECT * FROM v_albcompar WHERE emp_codi = " + emp_codiE.getValorInt() +
           " and acc_ano = " + acc_anoE.getValorInt() +
           " and acc_serie = '" + acc_serieE.getText() + "'" +
           " and acc_nume = " + acc_numeE.getValorInt() +
           " and acl_nulin = " + jt.getValorInt(row, 0);
       if (!dtCon1.select(s))
         return true;
       do
       {
         if (!actStkPart.checkStock(dtStat, jt.getValorInt(1), acc_anoE.getValorInt(),
                                 emp_codiE.getValorInt(), acc_serieE.getText(),
                                 acc_numeE.getValorInt(), dtCon1.getInt("acp_numind"),
                                 alm_codiE.getValorInt(), dtCon1.getDouble("acp_canti"),
                                 dtCon1.getInt("acp_canind")))
         {
           mensajeErr("Individuo: " + dtCon1.getInt("acp_numind") + " HA tenido movimientos de SALIDA");
           return false;
         }

       }
       while (dtCon1.next());
       return true;
     }
     /**
      * Funcion estatica para devolver la cabecera de un albaran de compra
      * @param empCodi int
      * @param accAno int
      * @param accSerie String
      * @param accNume int
      * @param dt DatosTabla
      * @throws SQLException
      * @return boolean si se encontro alg�n valor.
      *
      */
     public static boolean getCabeceraAlb(int empCodi,int accAno,String accSerie,
                                        int accNume, DatosTabla dt) throws SQLException
     {

       String s = "SELECT * FROM v_albacoc WHERE emp_codi = " + empCodi +
           " and acc_ano = " + accAno +
           " and acc_nume = " + accNume +
           " and acc_serie = '" + accSerie + "'";
       return dt.select(s);
     }

//     private void cargaALbVentas()
//     {
//       new miThread("")
//       {
//            @Override
//         public void run()
//         {
//           try {
//             cargaALbVentas0();
//           } catch (SQLException k)
//           {
//             Error("Error al cargar albaran de Ventas",k);
//           }
//         }
//       };
//     }
      /**
      * Función NO usada. Era para cuando se hicieran traspasos de una empresa a otra.
      * @throws SQLException 
      */
//     private void cargaALbVentas0() throws SQLException
//     {
//       int nLinDes;
//       if (cargaAlbVentas)
//         return; // Para que no se le llame mas que una vez.
//       cargaAlbVentas=true;
//       msgEspere("Cargando Albaran de Ventas ...");
//       swCargaAlb=false;
//       if (dtCursor == null)
//       {
//         dtCursor = new DatosTabla(ct);
//         dtInd = new DatosTabla(ct);
//         utdes = new utildesp();
//       }
//       try
//       {
//         String sLocal = pdalbara.getSqlLinAgr(pdalbara.TABLALIN,avc_anoE.getValorInt(), emp_codiE.getValorInt(),
//                                          EntornoUsuario.SERIEY, avc_numeE.getValorInt(), true);
//         int acp_numind;
//         if (dtCursor.select(sLocal))
//         {
//           guardaCab();
//           jt.removeAllDatos();
//           int nLin = 1;
//           acp_numind = 1;
//           ArrayList datos=new ArrayList();
//           do
//           {
//             ArrayList v = new ArrayList();
//             v.add("" + nLin); // 0
//             v.add(dtCursor.getString("pro_codi")); // 1
//             v.add(dtCursor.getString("avl_pronom")); // 2
//             v.add(dtCursor.getString("avl_unid")); // 3
//             v.add(dtCursor.getString("avl_canti")); // 4
//             if (opVerPrecios.isSelected())
//               v.add(dtCursor.getString("avl_prven", true)); // 5
//             else
//               v.add(0);
//             v.add("0");
//             v.add("0");
//             v.add("0");
//             v.add("0");
//             v.add("0");
//             v.add("0");
//             v.add("0");
//             v.add("");
//             v.add(0);
//             v.add(true);
//             datos.add(v);
//             stkPart.buscaStock(acc_fecrecE.getDate(),
//                                feulin,dtCursor.getInt("pro_codi"),emp_codiE.getValorInt(),0,null,0);
//
//             guardaLinAlb(nLin, nLin-1, dtCursor.getInt("avl_unid"), dtCursor.getDouble("avl_canti"), 0,
//                          dtCursor.getDouble("avl_prven"), 0,stkPart.getPrecioAcum(),
//                          "Alb.Venta: " + avc_anoE.getValorInt() + "-" + avc_numeE.getValorInt(),
//                          0);
//
//             sLocal = pdalbara.getStrSqlDesg(emp_codiE.getValorInt(), avc_anoE.getValorInt(),
//                                        EntornoUsuario.SERIEY, avc_numeE.getValorInt(), -1,
//                                        dtCursor.getInt("pro_codi"),null,
//                                        dtCursor.getDouble("avl_prven", true), true);
////             System.out.println("s: "+s);
//             if (dtInd.select(sLocal))
//             {
//               nLinDes=1;
//               do
//               {
//                 boolean ret = utdes.busDatInd(dtInd.getString("avp_serlot"), dtInd.getInt("pro_codi"),
//                                               dtInd.getInt("avp_emplot"),
//                                               dtInd.getInt("avp_ejelot"), dtInd.getInt("avp_numpar"),
//                                               dtInd.getInt("avp_numind"),
//                                               dtCon1, dtStat, EU);
//                 if (!ret)
//                 {
//                   swCargaAlb = true;
//                   cargaAlbVentas=false;
//                   dtAdd.rollback();
//                   resetMsgEspere();
//                   msgBox("No encontrados datos de Despiece del Producto: "+ dtInd.getInt("pro_codi")+
//                          " Individuo: "+ dtInd.getInt("avp_emplot")+"-"+dtInd.getInt("avp_ejelot")+"/"+
//                          dtInd.getString("avp_serlot")+dtInd.getInt("avp_numpar")+":"+dtInd.getInt("avp_numind"));
//                   return;
//                 }
//                 dtAdd.addNew("v_albcopave");
//                 dtAdd.setDato("acc_ano", acc_anoE.getValorInt());
//                 dtAdd.setDato("emp_codi", emp_codiE.getValorInt());
//                 dtAdd.setDato("acc_serie", acc_serieE.getText());
//                 dtAdd.setDato("acc_nume", acc_numeE.getValorInt());
//                 dtAdd.setDato("acl_nulin", nLin); // Numero Linea Albaran
//                 dtAdd.setDato("acp_numlin", nLinDes); // Numero Linea Desglose
//                 dtAdd.setDato("pro_codi",dtInd.getInt("pro_codi"));
//                 dtAdd.setDato("avp_ejelot",dtInd.getInt("avp_ejelot"));
//                 dtAdd.setDato("avp_serlot",dtInd.getString("avp_serlot"));
//                 dtAdd.setDato("avp_numpar",dtInd.getInt("avp_numpar"));
//                 dtAdd.setDato("avp_numind",dtInd.getInt("avp_numind"));
//                 dtAdd.update(stUp);
//                 guardaLinDes(nLinDes++, acp_numind++,
//                              utdes.getNumCrot(),
//                              utdes.getAcpPainac(), Formatear.getDate(utdes.feccadE, "dd-MM-yyyy"),
//                              utdes.getAcpPaisac(), utdes.getAcpEngpai(),
//                              utdes.fecSacrE,
//                              utdes.getFechaProduccion(),
//                              dtInd.getInt("pro_codi"), nLin, utdes.getMatCodi(),
//                              utdes.getSdeCodi(), dtInd.getDouble("avp_canti"),
//                              dtInd.getInt("avp_numuni"));
//               }  while (dtInd.next());
//             }
//             nLin++;
//           } while (dtCursor.next());
//           jt.setDatos(datos);
//         }
//         swCargaAlb = true;
//         if (jt.getRowCount()==1)
//           verDesgLinea(jt.tableView.getSelectedRow());
//         else
//           jt.requestFocusInicioLater();
//         dtAdd.commit();
//         resetMsgEspere();
//         mensajeErr("Albaran de Venta ... importado ");
//         acc_cerraE.setValor("0");
//         return;
//       }  catch (java.text.ParseException k1)
//     {
//       throw new SQLException("Error al pasear fechas. "+k1.getMessage());
//     }
//   }
    @Override
   public void activaTodo()
   {
//     MIactNombre.setEnabled(false);
//     MIchangeProd.setEnabled(true);
     super.activaTodo();
   }
   void verDesgLinea(int nRow) throws SQLException
   {
     double prCompra=jt.getValorDec(nRow, JT_PRCOM);
     prCompra-=jt.getValorDec(nRow, JT_PRCOM)*
               jt.getValorDec(nRow, JT_DTOPP) / 100;
     verDesgLinea(emp_codiE.getValorInt(), acc_anoE.getValorInt(),
                  acc_serieE.getText(), acc_numeE.getValorInt(),
                  jt.getValorInt(nRow, 0),
                  opAgrupar.isSelected() && opAgrupar.isEnabled(),
                  jt.getValorInt(nRow, JT_PROCOD),
                  jt.getValorDec(nRow, JT_KILALB),
                  prCompra);
   }
   /**
    * Cambia una referencia por otra.
    */
   private void cambRefPro()
   {
     try
     {
       s = "SELECT st.pro_numind FROM v_albcompar AS ac,v_stkpart as st " +
           " WHERE ac.acc_ano = " + acc_anoE.getValorInt() +
           " AND ac.emp_codi = " + emp_codiE.getValorInt() +
           " and ac.acc_serie = '" + acc_serieE.getText() + "'" +
           " and ac.acc_nume = " + acc_numeE.getValorInt() +
           " and ac.acl_nulin = " + jt.getValorInt(0) +
           " and st.eje_nume = ac.acc_ano " +
           " aND ac.emp_codi = st.emp_codi " +
           " and ac.acc_serie = st.pro_serie " +
           " and ac.acc_nume = st.pro_nupar " +
           " and st.pro_numind = ac.acp_numind " +
           " and st.stp_kilact != st.stp_kilini ";
       if (dtCon1.select(s))
       {
         if (! ARG_ADMIN)
         {
            mensajes.mensajeAviso(("Individuo: " + dtCon1.getInt("pro_numind") +
                                   " HA tenido movimientos.  Imposible Cambiar referencia"));
            mensajeErr("Referencia NO cambiada");
            BcanCam_actionPerformed();
            return;
         }
         else
         {
             if (mensajes.mensajeYesNo("Individuo: " + dtCon1.getInt("pro_numind") +
                                       " HA tenido movimientos.  Cambiar referencia") != mensajes.YES)
             {
               mensajeErr("Referencia NO cambiada");
               BcanCam_actionPerformed();
               return;
             }
             else
             {
               if (jf != null)
               {
                 jf.ht.clear();
                 jf.ht.put("%p", jt.getValString(1));
                 jf.ht.put("%i", acc_anoE.getValorInt() + "|" + emp_codiE.getValorInt() +
                           "|" + acc_serieE.getText() + "|" + acc_numeE.getValorInt() + "|" +
                           dtCon1.getInt("pro_numind"));
                 jf.guardaMens("C8", jf.ht); // Aviso cambio de referencia con ventas
               }
             }
         }
       }

       actRefProd(jt.getValorInt(0), pro_codcamE.getValorInt(),
                  pro_codcamE.getTextNomb());
       if (jf != null)
       {
         jf.ht.clear();
         jf.ht.put("%p", jt.getValString(1));
         jf.ht.put("%x", pro_codcamE.getText());
         jf.ht.put("%a", acc_anoE.getValorInt() + "|" + emp_codiE.getValorInt() +
                   "|" + acc_serieE.getText() + "|" + acc_numeE.getValorInt());
         jf.guardaMens("CB", jf.ht); // Aviso cambio de referencia
       }
       jt.setValor(pro_codcamE.getText(), 1);
       pro_codiE.setText(pro_codcamE.getText());
       jt.setValor(pro_codcamE.getTextNomb(), 2);
       if (opRepEti.isSelected())
         imprEtiLin();
       mensajeErr("REFERENCIA PRODUCTO ..... CAMBIADA");
     }
     catch (Exception k)
     {
       Error("Error al Cambiar Referencia de Producto", k);
     }
   }
   
   public void copiaAlbaranNuevo(DatosTabla dt, DatosTabla dtUpd,String coment,String usuario, 
          int accAno, int empCodi,String accSerie,int accNume) throws SQLException
  {
      String s="select max(his_rowid) as his_rowid from hisalcaco ";
      dt.select(s);
      int rowid=dt.getInt("his_rowid",true);
      rowid++;

      String condAlb=" emp_codi = "+empCodi+
          " and acc_serie = '" + accSerie+ "'" +
         " and acc_ano = "+accAno+
         " and acc_nume = "+accNume;
      s = "select * from v_albacoc WHERE "+condAlb;
      if (dt.select(s))
      {
         dtUpd.addNew("hisalcaco");
         dt.copy(dtUpd,usuario, coment,rowid);    
      }
      s = "select * from v_albacol WHERE "+condAlb;
      if (dt.select(s))
      {
         dtUpd.addNew("hisallico");
         dt.copy(dtUpd,usuario, coment,rowid);    
      }
      s = "select * from v_albcompar WHERE "+condAlb;
      if (dt.select(s))
      {
         dtUpd.addNew("hisalpaco");
         dt.copy(dtUpd, usuario, coment,rowid);    
      }
      dtUpd.commit();
  }
  void BvertSala_actionPerformed()
  {
       if (jtRecl.isVacio())
         return;
       int nLin=cambiaLinRecl(jtRecl.getSelectedRow());
       if (nLin>=0)
       {
         jtRecl.requestFocusLater(jtRecl.getSelectedRow(),nLin);
         return;
       }
       try {
         char estFin = estFinE.getValor().charAt(0);
         int nRow = jtRecl.getRowCount();
         for (int n = 0; n < nRow; n++)
         {
           if (jtRecl.getValorInt(n, ROWNVERT) == 0)
             continue;
           if (!jtRecl.getValString(n, 7).equals(estIniE.getValor()))
             continue;

           if (estFin == 'P' || estFin == 'E')
             jtRecl.setValor("", n, 8); // Pongo la Fecha Resol. vacia
           if (estFin == 'R' || estFin == 'A')
           {
             if (jtRecl.getValString(n, 8).trim().equals(""))
               jtRecl.setValor(Formatear.getFechaAct("dd-MM-yy"), n, 8);
           }
           if (estFin == 'R')
             jtRecl.setValor("0", n, 5);
           jtRecl.setValor(""+estFin,n,7);
           s = "update v_regstock set rgs_recprv = " + getRecPrv("" + estFin) + "," +
               " rgs_fecres = "+ (jtRecl.getValString(n,8).trim().equals("")?"null":
               "TO_DATE('" + jtRecl.getValString(n, 8) + "','dd-MM-yy')")+", " +
               " rgs_prregu = " + jtRecl.getValorDec(n, 5) +
               " WHERE rgs_nume = " + jtRecl.getValorInt(n, 10);
//           debug("s: "+s);
           dtAdd.executeUpdate(s);
         }
         ctUp.commit();
         jtRecl.requestFocusInicio();
         mensajeErr("Estado de las Reclamaciones  ... CAMBIADAS");
       } catch (SQLException k)
       {
         Error("Error al cambiar estado a Vertederos",k);
       }

  }
}
