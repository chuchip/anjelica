package gnu.chu.anjelica.despiece;

import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.almacen.StkPartid;
import gnu.chu.anjelica.almacen.ActualStkPart;
import gnu.chu.anjelica.almacen.pdalmace;
import gnu.chu.anjelica.listados.etiqueta;
import gnu.chu.anjelica.pad.MantArticulos;
import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.anjelica.pad.pdejerci;
import gnu.chu.anjelica.sql.Desorilin;
import gnu.chu.anjelica.sql.DesorilinId;
import gnu.chu.anjelica.sql.Desporig;
import gnu.chu.anjelica.sql.DesporigId;
import gnu.chu.camposdb.cliPanel;
import gnu.chu.camposdb.proPanel;
import gnu.chu.camposdb.tidCodi2;
import gnu.chu.comm.BotonBascula;
import gnu.chu.controles.*;
import gnu.chu.interfaces.PAD;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.*;
import gnu.chu.winayu.ayuLote;
import java.awt.*;
import java.awt.event.*;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

/**
 *
 * <p>Titulo: MantDespTactil</p>
 * <p>Descripción: Mant. DESPIECES POR TACTIL</p>
 * <p>Copyright: Copyright (c) 2005-2016
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
 * <p>Empresa: miSL</p>
 * @author chuchiP
 * @version 1.0
 */
public class MantDespTactil  extends ventanaPad implements PAD
{
 private gnu.chu.controles.CComboBox eti_codiE=new gnu.chu.controles.CComboBox();
  cliPanel cli_codiE = new cliPanel();
  int tipoEmp; // Tipo Empresa (Sala Despiece o Plantacion)
  private boolean isEmpPlanta=false; // Indica si la empresa es tipo Plantacion
  final static String TABLA_BLOCK="desporig";
  Desporig desorca;
  Desorilin desorli;
   
  private DatosTabla dtAux;
  int x=0,y=0;
  boolean CARGAPROEQU=false;
  
  public static final String SERIE="T";
  BotonBascula botonBascula;
   // Mantener fecha de caducidad del producto origen en productos finales (true)
  //  Poner la de producion + los dias de caducidad del producto final (false)
  final static boolean MANTFECDES=false;
  ActualStkPart stkPart;
  String serLotDF;
  boolean swTienePrec;
  HashMap <Integer,Integer>  htAcu=new HashMap();
  private int  tipoetiq=0,tipoetiqOld=0;
  boolean modLinSal=false;
  final static int CABECERA=0;
  final static int  TODOS=1;
  final static int ENTRADA=2;
  final static int SALIDA=3;
  HashMap <Integer,CButton> htProd=new HashMap();
  etiqueta etiq;
  utildesp utdesp;
  String s;
  ayuLote ayuLot=null; 

  CPanel Pprinc = new CPanel();
  JTabbedPane Ptabed = new JTabbedPane();
//  datTraza IFdatOr;
  DatTrazFrame datTrazFrame;
  CPanel Pconsul = new CPanel();
  
  //CTextField pro_loteE = new CTextField(Types.DECIMAL,"####9");
  CLabel cLabel3 = new CLabel();
  CTextField grd_fechaE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CPanel Pcabe = new CPanel();
  CLabel cLabel4 = new CLabel();
  CTextField eje_numeE = new CTextField(Types.DECIMAL,"###9");
  CLabel cLabel6 = new CLabel();
  CTextField deo_codiE = new CTextField(Types.DECIMAL,"####9");
  CTextField grd_unidE = new CTextField(Types.DECIMAL,"###9");
  CLabel tid_codiL = new CLabel();
  tidCodi2 tid_codiE = new tidCodi2();
  CTextField del_numlinE=new CTextField(Types.DECIMAL,"##9");
  Cgrid jtOri = new Cgrid(9);
  CLabel cLabel1 = new CLabel();
  CLabel cLabel5 = new CLabel();
  Cgrid jtFin = new Cgrid(6);
  CLabel cLabel7 = new CLabel();
  CTextField grd_unioriE = new CTextField(Types.DECIMAL,"###9");
  CLabel cLabel8 = new CLabel();
  CLabel cLabel9 = new CLabel();
  CLabel grd_blockL = new CLabel();
  CComboBox deo_blockE = new CComboBox();
  CTextField grd_kilorgE = new CTextField(Types.DECIMAL,"###,##9.999");
  CLabel cLabel11 = new CLabel();
  CLabel cLabel12 = new CLabel();
  CTextField grd_unifinE = new CTextField(Types.DECIMAL,"###9");
  CTextField grd_kilfinE = new CTextField(Types.DECIMAL,"###,##9.999");
  CPanel Porig = new CPanel();
  CButton Bimpetin = new CButton();
  private int JTENT_PROCODI=0;
  private int JTENT_PRONOMB=1;
  private int JTENT_EMP=2;
  private int JTENT_EJER=3;
  private int JTENT_SERIE=4;
  private int JTENT_LOTE=5;
  private int JTENT_NUMIND=6;
  private int JTENT_KILOS=7;
  private int JTENT_NL=8;

  public final static int JTSAL_PROCODI=0;
  public final static int JTSAL_PRONOMB=1;
  public final static int JTSAL_KILOS=2;
  public final static int JTSAL_NUMPIE=3;
  public final static int JTSAL_NUMIND=4;
  public final static int JTSAL_USUNOM=5;
  public final static int JTSAL_IMPRIM=6;
  CGridEditable jtEnt = new CGridEditable(9)
  {
        @Override
    public boolean insertaLinea(int row, int col)
    {
      if (!jtEnt.binsert)
        return true;
      if (!jtSal.isVacio() && row == 0)
      {
        mensajeErr("NO se puede insertar encima de la primera Linea con prod. Salida");
        return false;
      }
      return true;
    }
    public void afterDeleteLinea()
    {
      calcSumCab(jtEnt.getValorInt(JTENT_PROCODI), jtEnt.getValorDec(JTENT_KILOS));
    }

    public boolean afterInsertaLinea(boolean insLinea)
    {
      pdf_jtEnt();
      return true;
    }
    public void cambiaColumna(int col, int colNueva,int row)
    {
      try
      {
        if (col == 0)
          jtEnt.setValor(pro_codenE.getNombArt(pro_codenE.getText()), row, JTENT_PRONOMB);
      }
      catch (SQLException k)
      {
        Error("Error al buscar Nombre Articulo", k);
      }
    }

        @Override
    public int cambiaLinea(int row, int col)
    {
      return check_jtEnt(row);
    }
    @Override
    public void afterCambiaLinea()
    {
      try
      {
          pro_codenE.getNombArt();
          pro_kilenE.setEnabled(isEmpPlanta || ! pro_codenE.hasControlIndiv() ||  !pro_codenE.hasControlExist() );
      } catch (SQLException k)
      {
          Error("Error al cambiar linea cabecera",k);
          return;
      }
      pro_codenE.setEditable(jtEnt.getValorDec(JTENT_NL)==0);
      int row=jtEnt.getSelectedRow();
      if (!jtSal.isVacio() && row == JTENT_PROCODI)
        setEditEnt(false);
      else
        setEditEnt(true);
    }

        @Override
    public boolean deleteLinea(int row, int col)
    {
      if (!jtSal.isVacio() && row == JTENT_PROCODI)
      {
        mensajeErr("La primera Linea NO se puede BORRAR con PROD. en Salida");
        return false;
      }
      if (jtEnt.getValorInt(row,JTENT_NL)==0)
        return true;
      del_jtEnt(eje_numeE.getValorInt(),deo_codiE.getValorInt(), jtEnt.getValorInt(row,JTENT_NL));
      return true;
    }
//
  };
  CLabel numPiezasL = new CLabel();
  CLabel cLabel14 = new CLabel();
  CTextField grd_unioriE1 = new CTextField(Types.DECIMAL,"###9");
  CTextField grd_kilorgE1 = new CTextField(Types.DECIMAL,"###,##9.999");
  CPanel cPanel1 = new CPanel();
  CPanel Pfin = new CPanel();
  CPanel Pprofin = new CPanel();

  GridBagLayout gridBagLayout1 = new GridBagLayout();
  Cgrid jtSal = new Cgrid(7);
  CLabel def_kilsalL = new CLabel();
  CTextField kilsalE = new CTextField(Types.DECIMAL,"###9.999");
  CLabel def_usunomL = new CLabel();
  CComboBox def_usunomE = new CComboBox();
  
//  CLabel cLabel16 = new CLabel();
  proPanel pro_codsalE = new proPanel();
  CPanel Psalpro = new CPanel();
  CButton Bsalkil = new CButton();
  CCheckBox opImpEti = new CCheckBox();
  CButton BrepEti = new CButton();
  CPanel Psalpr1 = new CPanel();
  CLabel cLabel17 = new CLabel();
  CTextField grd_unisalE = new CTextField(Types.DECIMAL,"###9");
  CLabel cLabel18 = new CLabel();
  CTextField grd_kilsalE = new CTextField(Types.DECIMAL,"###,##9.999");
  CPanel cPanel3 = new CPanel();
  CPanel cPanel4 = new CPanel();
  CPanel Ppie = new CPanel();
  CButton Bfincab = new CButton();
//  CButton Baceptar = new CButton(Iconos.getImageIcon("ibr3.jpg"));
//  CButton Bcancelar = new CButton();

  proPanel pro_codenE = new proPanel()
  {
        @Override
    protected void despuesLlenaCampos()
    {
      jtEnt.procesaAllFoco();
      try {
        jtEnt.setValor(pro_codenE.getNombArt(pro_codenE.getText()), 1);
      } catch (Exception k){}
      jtEnt.mueveSigLinea();
    }
        @Override
    public void afterSalirLote(ayuLote ayuLot)
    {
        if (ayuLot.consulta)
        {
          jtEnt.setValor(ayuLot.jt.getValString(ayuLot.rowAct,ayuLote.JT_EMP),JTENT_EMP);
          jtEnt.setValor(ayuLot.jt.getValString(ayuLot.rowAct,ayuLote.JT_EJE),JTENT_EJER);
          jtEnt.setValor(ayuLot.jt.getValString(ayuLot.rowAct,ayuLote.JT_SER),JTENT_SERIE);
          jtEnt.setValor(ayuLot.jt.getValString(ayuLot.rowAct,ayuLote.JT_LOTE ),JTENT_LOTE);
          jtEnt.setValor(ayuLot.jt.getValString(ayuLot.rowAct,ayuLote.JT_IND),JTENT_NUMIND);
          jtEnt.setValor(ayuLot.jt.getValString(ayuLot.rowAct,ayuLote.JT_PESO),JTENT_KILOS);
        }
    }
        @Override
    public void afterFocusLost(boolean correcto)
    {
        if (!correcto)
            return;        
        pro_kilenE.setEnabled(isEmpPlanta || !this.hasControlIndiv() || !this.hasControlExist());
    }
  };
  CTextField pro_nomenE = new CTextField();
  CTextField emp_codenE = new CTextField(Types.DECIMAL,"#9");
  CTextField eje_numenE = new CTextField(Types.DECIMAL,"###9");
  CTextField pro_serenE = new CTextField(Types.CHAR,"X",1);
  CTextField pro_lotenE = new CTextField(Types.DECIMAL,"####9");
  CTextField pro_indenE = new CTextField(Types.DECIMAL,"###9");
  CTextField pro_kilenE = new CTextField(Types.DECIMAL,"##,##9.99");
  CLabel usu_nombL = new CLabel();
  CTextField usu_nombE = new CTextField();
  CLinkBox deo_almoriE = new CLinkBox();
  CLabel cLabel20 = new CLabel();
  CLabel cli_codiL = new CLabel("Cliente");
  CLinkBox deo_almdesE = new CLinkBox();
  CLabel cLabel110 = new CLabel();
  JScrollPane jScrollPane1 = new JScrollPane();
  CButton BborLiSa = new CButton(Iconos.getImageIcon("delete-row"));
  CLabel def_numpiL = new CLabel();
  CTextField def_numpiE = new CTextField(Types.DECIMAL,"--9");
  CLabel def_numliL = new CLabel();
  CTextField nlSalE = new CTextField(Types.DECIMAL,"--9");
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  GridBagLayout gridBagLayout3 = new GridBagLayout();
  GridBagLayout gridBagLayout4 = new GridBagLayout();
  CLabel cLabel23 = new CLabel();
  CTextField kildifE = new CTextField(Types.DECIMAL,"---,--9.999");
  CLabel cLabel24 = new CLabel();
  CTextField grd_feccadE = new CTextField(Types.DATE,"dd-MM-yyyy");
  boolean PARAM_ADMIN=false;
  CButton BmodDaIn = new CButton();
  CLabel numCopiasL = new CLabel();
  CTextField numCopiasE = new CTextField(Types.DECIMAL,"##9");
  public MantDespTactil(EntornoUsuario eu, Principal p)
  {
    this(eu,p,null);
  }
  public MantDespTactil(EntornoUsuario eu, Principal p,Hashtable ht)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    setTitulo("Despieces TACTIL");

    try
    {
      if (ht!=null)
      {
        if (ht.get("admin") != null)
          PARAM_ADMIN = Boolean.valueOf(ht.get("admin").toString()).
              booleanValue();
      }
      if (jf.gestor.apuntar(this))
        jbInit();
      else
        setErrorInit(true);
    }
    catch (Exception e)
    {
      ErrorInit(e);
      setErrorInit(true);
    }
  }

 public MantDespTactil(gnu.chu.anjelica.menu p, EntornoUsuario eu,Hashtable ht)
 {
   EU = eu;
   vl = p.getLayeredPane();
   setTitulo("Despieces TACTIL");
   eje = false;
   if (ht!=null)
   {
        if (ht.get("admin") != null)
          PARAM_ADMIN = Boolean.valueOf(ht.get("admin").toString()).
              booleanValue();
   }

   try
   {
     jbInit();
   }
   catch (Exception e)
   {
     ErrorInit(e);
     setErrorInit(true);
   }
 }
 public static String getNombreClase() {
        return "gnu.chu.anjelica.despiece.MantDespTactil";
 }
   public void setDeoCodi(String deoCodi) {
        deo_codiE.setText(deoCodi);       
    }
    public void setEjeNume(String ejeNume)
    {
        eje_numeE.setText(ejeNume);
    }
    public void setEjeNume(int ejeNume)
    {
        eje_numeE.setValorInt(ejeNume);
    }
    public boolean inTransation()
    {
        return (nav.getPulsado()==navegador.ADDNEW || nav.getPulsado()==navegador.EDIT || nav.getPulsado()==navegador.DELETE);
    }
 private void jbInit() throws Exception
 {
   iniciarFrame();
   this.setSize(new Dimension(679,519));
   setVersion("2016-102-13"+(PARAM_ADMIN?"(MODO ADMINISTRADOR)":""));
   CARGAPROEQU=EU.getValorParam("cargaproequi",CARGAPROEQU);
   nav = new navegador(this,dtCons,false,navegador.NORMAL);
   statusBar=new StatusBar(this);

   strSql = getStrSql() + " and eje_nume = " + EU.ejercicio +
       getOrderQuery();

   conecta();

   iniciar(this);
   opImpEti.setMargin(new Insets(0, 0, 0, 0));
   opImpEti.setSelected(true);
   grd_unioriE.setEnabled(false);
   grd_unioriE.setBounds(new Rectangle(228, 2, 44, 17));
   deo_blockE.setEnabled(false);
   
   grd_unifinE.setEnabled(false);
   grd_unifinE.setBounds(new Rectangle(226, 1, 44, 17));
   grd_kilfinE.setEnabled(false);
   grd_kilfinE.setBounds(new Rectangle(381, 1, 93, 17));
   grd_kilorgE.setEnabled(false);
   grd_kilorgE.setBounds(new Rectangle(383, 2, 93, 17));
   grd_kilorgE1.setEnabled(false);

   Pconsul.setLayout(gridBagLayout2);
    
   cLabel3.setToolTipText("");
   cLabel3.setText("Fecha Produccion");
   cLabel3.setBounds(new Rectangle(190, 2, 129, 16));
   Pcabe.setBorder(BorderFactory.createRaisedBevelBorder());
   Pcabe.setMaximumSize(new Dimension(14, 82));
   Pcabe.setMinimumSize(new Dimension(14, 82));
   Pcabe.setPreferredSize(new Dimension(14, 82));
   Pcabe.setLayout(null);
   cLabel4.setText("N. Piezas");
   cLabel4.setBounds(new Rectangle(391, 2, 53, 16));
    cLabel6.setText("Despiece");
    cLabel6.setBounds(new Rectangle(4, 2, 53, 16));
    tid_codiL.setText("Tipo Desp.");
    tid_codiL.setBounds(new Rectangle(4, 22, 58, 18));
    tid_codiE.setAncTexto(30);
    tid_codiE.setBounds(new Rectangle(67, 22, 285, 18));
    grd_unidE.setBounds(new Rectangle(441, 2, 42, 16));
    cLabel1.setBackground(Color.red);
    cLabel1.setForeground(Color.white);
    cLabel1.setOpaque(true);
    cLabel1.setPreferredSize(new Dimension(37, 14));
    cLabel1.setRequestFocusEnabled(true);
    cLabel1.setHorizontalAlignment(SwingConstants.CENTER);
    cLabel1.setHorizontalTextPosition(SwingConstants.TRAILING);
    cLabel1.setText("Piezas Origen");
    cLabel1.setBounds(new Rectangle(5, 2, 142, 16));
    cLabel5.setText("Piezas Generadas");
    cLabel5.setBounds(new Rectangle(2, 1, 142, 16));
    cLabel5.setHorizontalTextPosition(SwingConstants.TRAILING);
    cLabel5.setHorizontalAlignment(SwingConstants.CENTER);
    cLabel5.setRequestFocusEnabled(true);
    cLabel5.setPreferredSize(new Dimension(37, 14));
    cLabel5.setOpaque(true);
    cLabel5.setForeground(Color.white);
    cLabel5.setBackground(Color.red);
    cLabel7.setText("N. Piezas");
    cLabel7.setBounds(new Rectangle(172, 2, 52, 17));
    cLabel9.setRequestFocusEnabled(true);
    cLabel9.setText("Kilos");
    cLabel9.setBounds(new Rectangle(351, 2, 31, 17));
    grd_blockL.setText("Situacion");
    
    cLabel11.setText("N. Piezas");
    numPiezasL.setBounds(new Rectangle(0, 4, 52, 17));
    cLabel11.setBounds(new Rectangle(170, 1, 52, 17));
    cLabel12.setText("Kilos");
    cLabel12.setBounds(new Rectangle(349, 1, 31, 17));
    Porig.setLayout(gridBagLayout3);
    
    Bimpetin.setToolTipText("Imp.Etiq.Int.");
    Bimpetin.setText("Imp. Etiquetas Interiores");

    numCopiasL.setText("Copias");
    Bimpetin.setBounds(new Rectangle(415, 2, 147, 18));
    numCopiasE.setBounds(new Rectangle(565, 2, 38, 17));
    numCopiasL.setBounds(new Rectangle(610, 2, 49, 17));
    
    numCopiasE.setValorInt(1);    
    numPiezasL.setText("N. Piezas");    
    
    cLabel14.setText("Kilos");
    cLabel14.setBounds(new Rectangle(104, 4, 31, 17));
    cLabel14.setRequestFocusEnabled(true);
    cPanel1.setBorder(BorderFactory.createEtchedBorder());
    cPanel1.setMaximumSize(new Dimension(600, 26));
    cPanel1.setMinimumSize(new Dimension(600, 26));
    cPanel1.setPreferredSize(new Dimension(600, 26));
    cPanel1.setLayout(null);
    grd_kilorgE1.setBounds(new Rectangle(136, 4, 73, 17));
    grd_unioriE1.setEnabled(false);
    grd_unioriE1.setBounds(new Rectangle(56, 4, 44, 17));
    Pfin.setLayout(gridBagLayout4);
    Pprofin.setBorder(BorderFactory.createRaisedBevelBorder());
    Pprofin.setLayout(gridBagLayout1);
    Psalpro.setBorder(BorderFactory.createEtchedBorder());
  
//    cLabel16.setBounds(new Rectangle(1, 3, 57, 16));
    pro_codsalE.setVisibleBotonCons(false);
    pro_codsalE.setBounds(new Rectangle(2, 2, 250, 18));
    
    def_numliL.setText("NL");
    def_numliL.setBounds(new Rectangle(260, 2, 15, 18));
    nlSalE.setEnabled(false);
    nlSalE.setBounds(new Rectangle(276, 2, 20, 18));

    def_numpiL.setText("Unid.");
    def_numpiL.setBounds(new Rectangle(315, 2, 30, 18));
    def_numpiE.setBounds(new Rectangle(347, 2, 35, 18));

    def_kilsalL.setText("Kilos");
    def_kilsalL.setBounds(new Rectangle(395, 2, 30, 18));
    kilsalE.setBounds(new Rectangle(430, 2, 56, 18));

    def_usunomL.setText("Operario");
    def_usunomL.setBounds(new Rectangle(500, 2, 53, 18));
    
    def_usunomE.setBounds(new Rectangle(555, 2, 95, 18));
    def_usunomE.setPreferredSize(new Dimension(120,18));
    def_usunomE.setEnabled(false);
    
//    cLabel16.setText("Producto");
    Psalpro.setMaximumSize(new Dimension(658, 26));
    Psalpro.setMinimumSize(new Dimension(658, 26));
    Psalpro.setPreferredSize(new Dimension(658, 26));
    Psalpro.setLayout(null);
    opImpEti.setText("Impr. Etiqueta");
    opImpEti.setBounds(new Rectangle(155, 5, 115, 15));
    BrepEti.setIcon(Iconos.getImageIcon("print"));
    BrepEti.setBounds(new Rectangle(260, 3, 45, 17));
    BrepEti.setMargin(new Insets(0, 0, 0, 0));
    BrepEti.setText("F9");
    BrepEti.setToolTipText("Repetir Etiqueta");
    eti_codiE.setBounds(307, 3, 95, 17);
    Psalpr1.setBorder(BorderFactory.createEtchedBorder());
    Psalpr1.setMaximumSize(new Dimension(658, 26));
    Psalpr1.setMinimumSize(new Dimension(658, 26));
    Psalpr1.setPreferredSize(new Dimension(658, 26));
    Psalpr1.setLayout(null);
    cLabel17.setRequestFocusEnabled(true);
    cLabel17.setBounds(new Rectangle(528, 3, 31, 17));
    cLabel17.setText("Kilos");
    grd_unisalE.setBounds(new Rectangle(480, 3, 44, 17));
    grd_unisalE.setEnabled(false);
    cLabel18.setBounds(new Rectangle(424, 3, 52, 17));
    cLabel18.setText("N. Piezas");
    grd_kilsalE.setBounds(new Rectangle(560, 3, 73, 17));
    grd_kilsalE.setEnabled(false);
    kilsalE.setEnabled(!isEmpPlanta);
    
    ArrayList vc=new ArrayList();
    vc.add("Producto"); // 0
    vc.add("Nombre"); // 1
    vc.add("Emp"); // 2
    vc.add("Eje"); // 3
    vc.add("Serie"); // 4
    vc.add("Lote"); // 5
    vc.add("Ind"); // 6
    vc.add("Peso"); // 7
    vc.add("NL"); // 8
    jtEnt.setCabecera(vc);
    ArrayList v1=new ArrayList();
   
    pro_codenE.iniciar(dtStat,this,vl,EU);
    pro_codenE.setCamposLote(eje_numenE,  pro_serenE, pro_lotenE,
                        pro_indenE, pro_kilenE,deo_almoriE.getTextField());
    pro_codenE.setProNomb(null);
    pro_nomenE.setEnabled(false);
    pro_serenE.setMayusc(true);
    del_numlinE.setEnabled(false);
    botonBascula = new BotonBascula(EU,this);
    statusBar.add(botonBascula, new GridBagConstraints(9, 0, 1, 2, 0.0, 0.0
                                               , GridBagConstraints.EAST,
                                               GridBagConstraints.VERTICAL,
                                               new Insets(0, 5, 0, 0), 0, 0));
    pro_kilenE.setLeePesoBascula(botonBascula);
    
    pro_kilenE.setToolTipText("Pulsa F1 para recoger el peso de la bascula");
    emp_codenE.setValorInt(EU.em_cod);
    emp_codenE.setEnabled(false);
    v1.add(pro_codenE.getTextField()); // 0
    v1.add(pro_nomenE); // 1
    v1.add(emp_codenE); // 2
    v1.add(eje_numenE); // 3
    v1.add(pro_serenE); // 4
    v1.add(pro_lotenE); // 5
    v1.add(pro_indenE); // 6
    v1.add(pro_kilenE); // 7
    v1.add(del_numlinE); // 8
    jtEnt.setCampos(v1);
    jtEnt.setMaximumSize(new Dimension(500, 326));
    jtEnt.setMinimumSize(new Dimension(500, 326));
    jtEnt.setPreferredSize(new Dimension(500, 326));
    jtEnt.setAnchoColumna(new int[]
                          {70, 230, 30, 50, 30, 60, 40, 70,30});
    jtEnt.setAlinearColumna(new int[]
                            {2, 0, 2, 2, 1, 2, 2, 2,2});
    jtEnt.setFormatoColumna(7, "---,--9.99");
    jtEnt.setAjusAncCol(true);
//    jtEnt.setAjustarGrid(true);
    jtEnt.setAjustarGrid(true);
    jtEnt.setAjustarColumnas(false);

    jtOri.setCabecera(vc);
    jtOri.setMaximumSize(new Dimension(497, 148));
    jtOri.setMinimumSize(new Dimension(497, 148));
    jtOri.setPreferredSize(new Dimension(497, 148));
    jtOri.setBuscarVisible(false);
    jtOri.setAnchoColumna(new int[]
                          {70, 230, 30, 50, 30, 60, 40, 70,30});
    jtOri.setAlinearColumna(new int[]
                            {2, 0, 2, 2,1, 2, 2, 2, 2});
    jtOri.setFormatoColumna(7, "---,--9.99");
    jtOri.setAjustarGrid(true);
    jtOri.setAjustarColumnas(false);
    ArrayList v= new ArrayList();
    v.add("Producto"); // 0
    v.add("Descripcion"); // 1
    v.add("Kgs"); // 2
    v.add("N.Piez"); // 3
    v.add("Ind"); // 4
    v.add("Usuario");// 5
    jtFin.setCabecera(v);
    v.add("Impr"); // 6
    jtSal.setCabecera(v);

    
    jtFin.setFormatoColumna(2, "--,---9.99");
    jtFin.setFormatoColumna(3, "###9");
    
    jtFin.setMaximumSize(new Dimension(497, 113));
    jtFin.setMinimumSize(new Dimension(497, 113));
    jtFin.setPreferredSize(new Dimension(497, 113));
    jtFin.setBuscarVisible(false);
    jtFin.setAnchoColumna(new int[]  {70, 230, 70, 50,50,60});
    jtFin.setAlinearColumna(new int[] {2, 0, 2, 2, 2,2});
    jtFin.setAjustarGrid(true);
    jtFin.setAjustarColumnas(false);

    jtSal.setFormatoColumna(2, "--,---9.99");
    jtSal.setFormatoColumna(3, "###9");
    jtSal.setFormatoColumna(4, "---9");
    jtSal.setFormatoColumna(JTSAL_IMPRIM, "BSN");   
    jtSal.setMaximumSize(new Dimension(640, 108));
    jtSal.setMinimumSize(new Dimension(640, 108));
    jtSal.setPreferredSize(new Dimension(640, 108));
    jtSal.setBuscarVisible(false);
//    jtSal.setPuntoDeScroll(50);
    jtSal.setAnchoColumna(new int[]  {70, 230, 90,60,60,90,40});
    jtSal.setAlinearColumna(new int[] {2, 0, 2, 2,2,2,1});
    jtSal.setAjustarGrid(true);
    jtSal.setAjustarColumnas(false);

//    this.getContentPane().add(nav,BorderLayout.NORTH);
//    this.getContentPane().add(statusBar,BorderLayout.SOUTH);
    cPanel3.setBorder(BorderFactory.createEtchedBorder());
    cPanel3.setMaximumSize(new Dimension(482, 22));
    cPanel3.setMinimumSize(new Dimension(482, 22));
    cPanel3.setPreferredSize(new Dimension(482, 22));
    cPanel3.setLayout(null);
    cPanel4.setBorder(BorderFactory.createEtchedBorder());
    cPanel4.setMaximumSize(new Dimension(492, 21));
    cPanel4.setMinimumSize(new Dimension(492, 21));
    cPanel4.setPreferredSize(new Dimension(492, 21));
    cPanel4.setLayout(null);
    Ppie.setMaximumSize(new Dimension(24, 30));
    Ppie.setMinimumSize(new Dimension(24, 30));
    Ppie.setPreferredSize(new Dimension(24, 30));
    Ppie.setLayout(null);
    Baceptar.setBounds(new Rectangle(100, 1, 100, 26));
    Baceptar.setText("Aceptar");
    Bcancelar.setBounds(new Rectangle(310, 1, 100, 26));
    Bcancelar.setMaximumSize(new Dimension(81, 29));
    Bcancelar.setMinimumSize(new Dimension(81, 29));
    Bcancelar.setPreferredSize(new Dimension(81, 29));
    Bcancelar.setText("Cancelar");
    usu_nombL.setText("Usuario");
    grd_blockL.setBounds(new Rectangle(360, 22, 54, 17));
    deo_blockE.setBounds(new Rectangle(417, 22, 92, 17));
    usu_nombL.setBounds(new Rectangle(516, 22, 51, 17));
    usu_nombE.setBounds(new Rectangle(568, 22, 93, 17));
    deo_almoriE.setAncTexto(30);
    deo_almoriE.setBounds(new Rectangle(69, 41, 200, 19));
    cLabel20.setText("Alm.Origen");
    cLabel20.setBounds(new Rectangle(3, 41, 62, 18));
    cli_codiL.setBounds(new Rectangle(3, 61, 62, 18));
    cli_codiE.setBounds(new Rectangle(65, 61, 315, 18));
    deo_almdesE.setAncTexto(30);
    deo_almdesE.setBounds(new Rectangle(349, 41, 186, 19));
    cLabel110.setText("Alm.Final");
    cLabel110.setBounds(new Rectangle(289, 41, 56, 19));
    jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    jScrollPane1.setMaximumSize(new Dimension(661, 140));
    jScrollPane1.setMinimumSize(new Dimension(661, 140));
    jScrollPane1.setPreferredSize(new Dimension(661, 140));
    jScrollPane1.setRequestFocusEnabled(true);
    BborLiSa.setBounds(new Rectangle(6, 3, 147, 20));
    BborLiSa.setMaximumSize(new Dimension(90, 29));
    BborLiSa.setMargin(new Insets(0, 0, 0, 0));
    BborLiSa.setText("F8 Borrar Linea");
   
    Bsalkil.setBounds(new Rectangle(648, 8, 2, 2));
    kilsalE.setLeePesoBascula(botonBascula);
    kilsalE.setToolTipText("Pulsa F1 para leer el peso de la bascula");
    Bfincab.setBounds(new Rectangle(562, 46, 2, 2));
    cLabel8.setBounds(new Rectangle(125, 18, 0, 0));
    grd_fechaE.setBounds(new Rectangle(298, 2, 75, 16));
    eje_numeE.setBounds(new Rectangle(67, 2, 38, 16));
    deo_codiE.setBounds(new Rectangle(110, 2, 44, 16));
    cLabel23.setText("Diferencia Kg.");
    cLabel23.setBounds(new Rectangle(501, 2, 78, 15));
    kildifE.setEnabled(false);
    kildifE.setBounds(new Rectangle(587, 2, 73, 17));
    cLabel24.setText("Fec.Cad.");
    cLabel24.setBounds(new Rectangle(534, 41, 51, 19));
    grd_feccadE.setBounds(new Rectangle(586, 43, 75, 16));
    BmodDaIn.setBounds(new Rectangle(526, 4, 132, 19));
    BmodDaIn.setMargin(new Insets(0, 0, 0, 0));
    BmodDaIn.setText("Ver/Mod. Datos Ind.");
    
    this.getContentPane().add(Pprinc,  BorderLayout.CENTER);
    Pprinc.setLayout(new BorderLayout());
    Pprinc.add(Ptabed,BorderLayout.CENTER);
    Pprinc.add(Pcabe, BorderLayout.NORTH);

    Pfin.add(jScrollPane1,  new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 2), 0, 0));
    jScrollPane1.getViewport().add(Pprofin, null);
    Ptabed.add(Pconsul, "Consulta");
    Psalpr1.add(grd_kilsalE, null);
    Psalpr1.add(cLabel17, null);
    Psalpr1.add(grd_unisalE, null);
    Psalpr1.add(cLabel18, null);
    Psalpr1.add(BrepEti, null);
    Psalpr1.add(eti_codiE, null);
    Psalpr1.add(BborLiSa, null);
    Psalpr1.add(opImpEti, null);
    Pfin.add(jtSal,   new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 2), 0, 0));
    Pfin.add(Psalpro,    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 2), 0, 0));

    Psalpro.add(pro_codsalE, null);
    Psalpro.add(kilsalE, null);
    Psalpro.add(def_numpiL, null);
    Psalpro.add(def_numpiE, null);
    Psalpro.add(def_kilsalL, null);
    Psalpro.add(Bsalkil, null);
    Psalpro.add(def_numliL, null);
    Psalpro.add(nlSalE, null);
    Psalpro.add(def_usunomL, null);
    Psalpro.add(def_usunomE, null);
    Pfin.add(Psalpr1,   new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
    Pconsul.add(jtOri,   new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 1, 0, 0), 0, 0));
    Pconsul.add(jtFin,   new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 1, 0, 0), 0, 0));
    Pconsul.add(cPanel3,   new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    cPanel3.add(grd_kilfinE, null);
    cPanel3.add(cLabel5, null);
    cPanel3.add(cLabel11, null);
    cPanel3.add(grd_unifinE, null);
    cPanel3.add(cLabel12, null);
    Pconsul.add(cPanel4,     new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 0, 0));
    cPanel4.add(grd_kilorgE, null);
    cPanel4.add(cLabel1, null);
    cPanel4.add(cLabel7, null);
    cPanel4.add(grd_unioriE, null);
    cPanel4.add(cLabel9, null);
    Pcabe.add(cLabel6, null);
    Pcabe.add(deo_codiE, null);
    Pcabe.add(eje_numeE, null);
//    Pcabe.add(pro_loteE, null);
  
    Pcabe.add(cLabel4, null);
    Pcabe.add(grd_unidE, null);
    Pcabe.add(cLabel8, null);
    Pcabe.add(grd_fechaE, null);
    Pcabe.add(cLabel3, null);
    Pcabe.add(Bfincab, null);
    Pcabe.add(tid_codiE, null);
    Pcabe.add(tid_codiL, null);
    Pcabe.add(grd_blockL, null);
    Pcabe.add(deo_blockE, null);
    Pcabe.add(usu_nombE, null);
    Pcabe.add(usu_nombL, null);
    Pcabe.add(deo_almdesE, null);
    Pcabe.add(cLabel20, null);
    Pcabe.add(cli_codiL, null);
    Pcabe.add(cli_codiE, null);
    Pcabe.add(deo_almoriE, null);
    Pcabe.add(cLabel110, null);
    Pcabe.add(cLabel23, null);
    Pcabe.add(kildifE, null);
    Pcabe.add(cLabel24, null);
    Pcabe.add(grd_feccadE, null);
    Pprinc.add(Ppie,  BorderLayout.SOUTH);
    Ptabed.add(Porig,   "Origen");
    Porig.add(jtEnt,   new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 3, 0, 0), 0, 0));
    Porig.add(cPanel1,     new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
    cPanel1.add(grd_kilorgE1, null);
    cPanel1.add(cLabel14, null);
    cPanel1.add(grd_unioriE1, null);
    cPanel1.add(numPiezasL, null);
    cPanel1.add(BmodDaIn, null);
   
    Ptabed.add(Pfin, "Final");
    Ppie.add(Bcancelar, null);
    Ppie.add(Baceptar, null);
    
    Ppie.add(Bimpetin, null);
    Ppie.add(numCopiasL, null);
    Ppie.add(numCopiasE, null);
    grd_unioriE1.setEnabled(false);


 }
    @Override
 public void iniciarVentana() throws Exception
 {
   cli_codiE.iniciar(dtAux,this,vl,EU);
   jtEnt.cuadrarGrid();
   jtFin.cuadrarGrid();
   jtOri.cuadrarGrid();
   jtSal.cuadrarGrid();
   pro_kilenE.setEnabled(isEmpPlanta);
   Pcabe.setButton(KeyEvent.VK_F2,Bfincab);
   gnu.chu.Menu.LoginDB.iniciarLKEmpresa(EU,dtStat);
   pro_codenE.setAyudaLotes(true);
   Pcabe.setButton(KeyEvent.VK_F4,Baceptar);
   utdesp =new utildesp();
   pro_codsalE.iniciar(dtStat, this, vl, EU);
   pro_codsalE.setEnabled(false);
   pro_codsalE.setEntrada(true);
   
   tid_codiE.iniciar(dtStat, this, vl, EU);
   tid_codiE.setIncluirEstaticos(false);
   jtSal.setButton(KeyEvent.VK_F8,BborLiSa);
   jtSal.setButton(KeyEvent.VK_F9,BrepEti);

   Pfin.setButton(KeyEvent.VK_F8,BborLiSa);
   Pfin.setButton(KeyEvent.VK_F9,BrepEti);
   pdalmace.llenaLinkBox(deo_almoriE, dtCon1);
   pdalmace.llenaLinkBox(deo_almdesE, dtCon1);
//   s = "SELECT alm_codi, alm_nomb from v_almacen order by alm_codi";
//   dtCon1.select(s);
//   deo_almoriE.addDatos(dtCon1);
//   dtCon1.first();
//   deo_almdesE.addDatos(dtCon1);
   dtCon1.select("select usu_nomb from usuarios where usu_activ='S'");
   def_usunomE.setDatos(dtCon1);
   def_usunomE.setText(EU.usu_nomb);
   
   stkPart=new ActualStkPart(dtAdd,EU.em_cod);
   MantDesp.deoBlockE_addItem(deo_blockE);
   eje_numeE.setColumnaAlias("eje_nume");
   deo_codiE.setColumnaAlias("deo_codi");
  // pro_loteE.setColumnaAlias("deo_numdes");
   grd_fechaE.setColumnaAlias("deo_fecha");
   cli_codiE.setColumnaAlias("cli_codi");
   tid_codiE.setColumnaAlias("tid_codi");
   usu_nombE.setColumnaAlias("usu_nomb");
   deo_almoriE.setColumnaAlias("deo_almori");
   deo_almdesE.setColumnaAlias("deo_almdes");
   deo_blockE.setColumnaAlias("deo_block");
   grd_feccadE.setColumnaAlias("grd_feccad");
   tid_codiE.combo.setPreferredSize(new Dimension(350,18));
   tid_codiE.releer(); // Carga todos los despieces activos 
   eti_codiE.setDatos(etiqueta.getReports(dtStat, EU.em_cod,0));    
   eti_codiE.addItem("Defecto", "0");
   eti_codiE.setValor("0");
 
   activarEventos();
   verDatos(dtCons);
   actButton(true);
 }

 void activarEventos()
 {
     
   BmodDaIn.addActionListener(new ActionListener()
   {
      @Override
     public void actionPerformed(ActionEvent e)
     {
       mostrarDatosTraz();
     }
   });
   jtSal.addMouseListener(new MouseAdapter() {
            @Override
     public void mouseClicked(MouseEvent e) {
         
       if (! jtSal.isVacio() && e.getClickCount()==1 && jtSal.getSelectedColumnDisab()==JTSAL_IMPRIM)
             jtSal.setValor(!jtSal.getValBoolean( jtSal.getSelectedRowDisab(),JTSAL_IMPRIM),
                  jtSal.getSelectedRowDisab(),JTSAL_IMPRIM );

       if (jtSal.isVacio() || !jtSal.isEnabled() || e.getClickCount()<2)
         return;  
        try
        {
            if (!ActualStkPart.checkStock(dtStat, jtSal.getValorInt(JTSAL_PROCODI),
                eje_numeE.getValorInt(), EU.em_cod,
                SERIE, deo_codiE.getValorInt(),
                jtSal.getValorInt( JTSAL_NUMIND), deo_almdesE.getValorInt(),
                jtSal.getValorDec(JTSAL_KILOS),1))
            {
                msgBox("Individuo ha tenido mvtos. Imposible editar linea");
                return;
            }     
        } catch (SQLException ex)
        {
            Error("Error al comprobar si articulo ha tenido mvtos",ex);
            return;
        }
       modLinSal=true;
       nlSalE.setValorDec(jtSal.getSelectedRow());
       pro_codsalE.setText(jtSal.getValString(JTSAL_PROCODI));
       def_numpiE.setText(jtSal.getValString( JTSAL_NUMPIE));
       kilsalE.setValorDec(jtSal.getValorDec( JTSAL_KILOS));
       def_usunomE.setText(jtSal.getValString(JTSAL_USUNOM));
       kilsalE.resetCambio();
       def_usunomE.resetCambio();
       def_numpiE.resetCambio();
       if (isEmpPlanta)
           def_numpiE.requestFocusLater();
       else
            kilsalE.requestFocusLater();
     }
   });
   BborLiSa.addActionListener(new ActionListener() {
            @Override
     public void actionPerformed(ActionEvent e) {
       if (! borrarLinSa(jtSal.getSelectedRow()))
         return;
       jtSal.removeLinea();
       actAcumSal();
     }
   });
   BrepEti.addActionListener(new ActionListener()
   {
            @Override
     public void actionPerformed(ActionEvent e)
     {
       if (nav.getPulsado()==navegador.NINGUNO)
       {
           if (jtSal.getValorInt(jtSal.getSelectedRowDisab(),JTSAL_PROCODI)!=0)
           {
               if (getNumLinImprimir()==0)
                jtSal.setValor(true,
                  jtSal.getSelectedRowDisab(),JTSAL_IMPRIM );
           }
       }
       imprEtiSal(jtSal.getSelectedRow(),true);
     }
   });
   Bsalkil.addFocusListener(new FocusAdapter() {
            @Override
     public void focusGained(FocusEvent e) {
       Bsalkil_focusGained();
     }
   });
   Bfincab.addFocusListener(new FocusAdapter() {
            @Override
     public void focusGained(FocusEvent e) {
       irGridEnt();
     }
   });

   Bimpetin.addActionListener(new ActionListener()
   {
            @Override
     public void actionPerformed(ActionEvent e)
     {
       imprEtiqInt();
     }
   });
 }
/**
 * Imprimir etiquetas interiores. (van dentro del plastico del producto)
 * 
 */
 void imprEtiqInt()
 {
   try
   {
     if (deo_codiE.getValorDec()==0)
     {
       mensajeErr("Despiece sin Numero Lote asignado");
       return;
     }
     if (jtEnt.isVacio())
     {
       mensajeErr("Introduce Algun producto de Entrada");
       return;
     }
     if (numCopiasE.getValorInt()==0)
     {
       mensajeErr("Introduzca Numero de Etiquetas a sacar");
       numCopiasE.requestFocus();
       return;
     }
     if (pro_codsalE.isNull())
     {
         mensajeErr("Debe elegir el Codigo de Producto de Salida");
         return;
     }
     if (!pro_codsalE.isVendible())
     {
         mensajeErr("Producto debe ser vendible para imprimir etiquetas interiores");
         return;
     }
     buscaDatInd();
    CodigoBarras codBarras = new CodigoBarras("D",eje_numeE.getText(),SERIE,
                deo_codiE.getValorInt(),pro_codsalE.getValorInt(),0,0); 
     etiq.iniciar(deo_codiE.getText(),
                 codBarras.getLote(),
                  pro_codsalE.getText(), pro_codsalE.getTextNomb(), utdesp.nacidoE, utdesp.cebadoE,
                  utdesp.despiezadoE, null,
                  0,utdesp.getConservar(),
                  utdesp.sacrificadoE,
                  grd_fechaE.getDate(),grd_fechaE.getDate(),
                  grd_feccadE.getDate(), 
                  utdesp.fecSacrE);
       

//     new miThread("aaa")
//     {
//       public void run()
//       {
           imprEtiq0();
//       }
//     };
   }
   catch (Exception ex)
   {
     Error("Error al Guardar Datos Despiece", ex);
   }

 }
 /**
  * Busca los datos del individuo
  * @return
  * @throws Exception 
  */
 boolean buscaDatInd() throws Exception
 {
   if (etiq == null)
     etiq = new etiqueta(EU);
   if (utdesp.cambio)
   {
     pro_codenE.getNombArt(jtEnt.getValString(0, JTENT_PROCODI), EU.em_cod);
     if (!utdesp.busDatInd(jtEnt.getValString(0, JTENT_SERIE), jtEnt.getValorInt(0, JTENT_PROCODI),
                           EU.em_cod,
                           jtEnt.getValorInt(0, JTENT_EJER),
                           jtEnt.getValorInt(0, JTENT_LOTE),
                           jtEnt.getValorInt(0, JTENT_NUMIND), // No Ind.
                           deo_almoriE.getValorInt(),
                           dtCon1, dtStat, EU))
     {
       msgBox(utdesp.getMsgAviso());
       return false;
     }
     else
     {
       utdesp.setDespNuestro(grd_fechaE.getText(),dtStat);
       if (grd_feccadE.isNull())
       {
         if (MANTFECDES)
           grd_feccadE.setText(utdesp.feccadE);
         else
           grd_feccadE.setText(Formatear.sumaDias(grd_fechaE.getDate(),pro_codenE.lkPrd.getInt("pro_diacom")));
       }
     }
     utdesp.cambio = false;
   }
   return true;
 }
 void imprEtiq0()
 {
   try
   {
//     this.setEnabled(false);
//     for (int n = 0; n < netiintE.getValorInt(); n++)
//     {
//     EU.previsual=false;
     if (etiq == null)
        etiq = new etiqueta(EU);
     etiq.setNumCopias(numCopiasE.getValorInt());
//     if ( numCopiasE.getValorInt()>1 )
//       etiq.setPrintDialog(false);
//     else
//       etiq.setPrintDialog(true);
     etiq.listar(etiqueta.ETIQINT);
     etiq.setTipoEtiq(dtStat, EU.em_cod, 0);
//       mensaje("Imprimiendo etiqueta: " + n + " de " + netiintE.getValorInt());
//     }
   }
   catch (Throwable ex)
   {
     Error("Error al imprimir etiqueta Interior", ex);
   }
   this.setEnabled(true);
   mensaje("");
   mensajeErr("Etiqueta ... Listada");
 }



 private void irGridEnt()
 {
   try {
//     if (tid_codiE.combo.getItemCount()==0)
//     {
//       pro_codiE.requestFocus();
//       mensajeErr("Producto SIN tipos despieces VALIDOS");
//       return;
//     }

     if (! tid_codiE.controla() || tid_codiE.isNull())
     {
       mensajeErr("Tipo de Despiece NO VALIDO");
       return;
     }
     if (grd_unidE.getValorInt()==0 && ! isEmpPlanta)
     {
       mensajeErr("Introduzca UNIDADES a Despiezar");
       grd_unidE.requestFocus();
       return;
     }
     if (grd_fechaE.isNull())
     {
       mensajeErr("Introduzca la Fecha de Despiece");
       grd_fechaE.requestFocus();
       return;
     }
     s = pdejerci.checkFecha(dtStat, eje_numeE.getValorInt(), EU.em_cod, grd_fechaE.getDate());
     if (s != null) {
         mensajeErr(s);
         grd_fechaE.requestFocus();
         return;
     }
//     if (emp_codiE.getValorDec()==0)
//     {
//       mensajeErr("Empresa NO VALIDA");
//       emp_codiE.requestFocus();
//       return;
//     }
     if (eje_numeE.getValorInt()==0)
     {
       mensajeErr("Ejercicio NO VALIDO");
       eje_numeE.requestFocus();
       return;
     }
   } catch (ParseException | SQLException k)
   {
     Error("Error al Ir al grid de Entrada",k);
     return;
   }
   Porig.setEnabled(true);
   jtEnt.setEnabled(true);
   jtEnt.requestFocusInicio();
   jtEnt.afterCambiaLinea();
   if (jtEnt.isVacio())
     pdf_jtEnt();
 }
    @Override
  public void PADPrimero() {
    nav.pulsado=navegador.NINGUNO;
    verDatos(dtCons);
    actButton(true);
  }
    @Override
  public void PADAnterior() {
    nav.pulsado=navegador.NINGUNO;
    verDatos(dtCons);
    actButton(true);
 }
    @Override
  public void PADSiguiente() {
    nav.pulsado=navegador.NINGUNO;
    verDatos(dtCons);
    actButton(true);
  }
    @Override
  public void PADUltimo() {
    nav.pulsado=navegador.NINGUNO;
    verDatos(dtCons);
    actButton(true);
  }
    @Override
  public void PADQuery() {
    activar(CABECERA,true);
    Baceptar.setEnabled(true);
    Bcancelar.setEnabled(true);
    Pcabe.setQuery(true);
    Pcabe.resetTexto();
    grd_unidE.setEnabled(false);
    if (Integer.parseInt(Formatear.getFechaAct("MM"))>2)
        eje_numeE.setValorInt(EU.ejercicio);
    deo_codiE.requestFocus();
    mensaje("Introduzca Condiciones de Busqueda");
  }
    @Override
  public void ej_query1() {
    if (Pcabe.getErrorConf() != null)
       {
         mensajeErr("Error en condiciones de busqueda");
         Pcabe.getErrorConf().requestFocus();
         return;
       }
       nav.pulsado=navegador.NINGUNO;

       ArrayList v = new ArrayList();
       v.add(eje_numeE.getStrQuery());
       v.add(deo_codiE.getStrQuery());
//       v.add(pro_loteE.getStrQuery());
       v.add(grd_fechaE.getStrQuery());
       v.add(grd_unidE.getStrQuery());
       v.add(tid_codiE.getStrQuery());
       v.add(deo_blockE.getStrQuery());
       v.add(grd_feccadE.getStrQuery());
       v.add(usu_nombE.getStrQuery());
       v.add(deo_almoriE.getStrQuery());
       v.add(deo_almdesE.getStrQuery());
       v.add(cli_codiE.getStrQuery());


       s = creaWhere(getStrSql(), v,false);
       s += getOrderQuery();
//       debug("Query: "+s);
       Pcabe.setQuery(false);
       nav.pulsado=navegador.NINGUNO;
       try
       {
         if (!dtCons.select(s))
         {
           mensaje("");
           msgBox("No encontrados Despieces para estos criterios");
           rgSelect();
           activaTodo();
           return;
         }
         mensaje("");
         strSql=s;
         rgSelect();
         activaTodo();
         mensajeErr("Nuevos registros selecionados");
       }
       catch (Exception ex)
       {
         fatalError("Error al buscar Despieces: ", ex);
       }
       actButton(true);
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
    @Override
  public void afterConecta() throws SQLException, java.text.ParseException
  {
      datTrazFrame = new DatTrazFrame(EU, vl, this)
      {
          @Override
          public void matar() {
              salirDatTraza();
          }
      };
      datTrazFrame.iniciar(dtStat, dtCon1, this, vl, EU);
      datTrazFrame.setEditable(true);
      vl.add(datTrazFrame);
      datTrazFrame.setLocation(this.getLocation().x, this.getLocation().y + 30);
      dtAux = new DatosTabla(ct);
      tipoEmp=pdconfig.getTipoEmpresa(EU.em_cod, dtStat);
      isEmpPlanta=tipoEmp==pdconfig.TIPOEMP_PLANTACION;
  }
    @Override
  public void canc_query() {
    Pcabe.setQuery(false);
    verDatos(dtCons);
    activaTodo();
    actButton(true);
    nav.pulsado=navegador.NINGUNO;
    mensajeErr("Consulta .. cancelada");
    mensaje("");
  }

    @Override
  public void PADEdit()
  {      
    if (jtEnt.isVacio())
    {
        msgBox("Despiece VACIO. Imposible Modificar");
        nav.pulsado = navegador.NINGUNO;
        activaTodo();
        return;
    }
    if (deo_blockE.getValor().equals("B") && ! EU.isRootAV())
    {
        msgBox("Despiece Bloqueado. Imposible Modificar");
        nav.pulsado = navegador.NINGUNO;
        activaTodo();
        return;
    }
    try {
     if (!checkMvtos(eje_numeE.getValorInt(), deo_codiE.getValorInt()))
                return;
     if (swTienePrec)
     {
       if (!PARAM_ADMIN)
       {
          mensajeErr("DESPIECE VALORADO ... IMPOSIBLE MODIFICAR");
          nav.pulsado = navegador.NINGUNO;
          activaTodo();
          return;
       }
       int ret=mensajes.mensajeYesNo("Despiece ya ha sido valorado. Si lo edita se pondran los costos a cero.\n"+
              " Seguro que desea editarlo ?");
       if (ret!=mensajes.YES)
       {
         nav.pulsado = navegador.NINGUNO;
         activaTodo();
         return;
       }
     }
   
     if (! setBloqueo(dtAdd,TABLA_BLOCK, eje_numeE.getValorInt()+"|"+
                   deo_codiE.getValorInt()))
      {
            msgBox(msgBloqueo);
            activaTodo();
            return;
      }
      utdesp.copiaDespieceNuevo(dtStat,dtAdd, "EDITANDO despiece TACTIL",EU.usuario,
              eje_numeE.getValorInt(),deo_codiE.getValorInt());
      dtAdd.commit();
      
    } catch (SQLException | UnknownHostException k)
    {
        Error("Error al realizar copia de registro actual",k);
        return;
    }
    mensaje("Editando despiece");
    setEditEnt(true);
    utdesp.cambio = true;
    modLinSal=false;

    activar(true);
    activar(CABECERA,false);
    if (PARAM_ADMIN)
        tid_codiE.setEnabled(true);
    deo_blockE.setEnabled(true);
    grd_unidE.setEnabled(true);
    if (PARAM_ADMIN)
      grd_fechaE.setEnabled(true);

//    grd_feccadE.setEnabled(true);
    Bcancelar.setEnabled(false);
    jtEnt.requestFocusInicio();
  }
  private boolean checkMvtos(int ejeNume, int deoCodi ) throws SQLException
  {               
       dtStat.select("select min(deo_tiempo) as tiempo from desorilin where eje_nume="+ejeNume+
            " and deo_codi = "+deoCodi);
        java.sql.Date feulmv=dtStat.getDate("tiempo");
        if (!checkMvto(pdalmace.getFechaInventario(deo_almoriE.getValorInt(), dtStat),feulmv))
            return false;
    
        dtStat.select("select min(def_tiempo) as tiempo from v_despfin where eje_nume="+ejeNume+
            " and deo_codi = "+deoCodi);
        feulmv=dtStat.getDate("tiempo");
        return checkMvto(pdalmace.getFechaInventario(deo_almdesE.getValorInt(), dtStat),feulmv);
    }
    private boolean checkMvto(java.sql.Date fecInv,java.sql.Date fecMvto)
    {
         if (fecMvto != null && Formatear.comparaFechas(fecInv, fecMvto)>= 0 )
        {
              msgBox("Despiece con Mvtos anteriores a Ult. Fecha Inventario. Imposible Editar/Borrar");
              nav.pulsado = navegador.NINGUNO;
              activaTodo();
              return false;
        }
        return true;
    }
    @Override
  public void ej_edit1() {
    ej_addnew1();
  }
    @Override
  public void canc_edit() {
  }
    @Override
  public void PADAddNew() {
    mensaje("Insertando Nuevo despiece");
    Pcabe.resetTexto();
    jtEnt.removeAllDatos();
    grd_unioriE1.setValorDec(0);
    grd_kilorgE1.setValorDec(0);
    modLinSal=false;
    jtSal.removeAllDatos();
    Pprofin.removeAll();
    datTrazFrame.resetInit();
//    emp_codiE.setValorDec(EU.em_cod);
    eje_numeE.setValorDec(EU.ejercicio);
    grd_fechaE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
    if (PARAM_ADMIN)
      grd_fechaE.setEnabled(true);
    Ptabed.setSelectedIndex(1);
//    deo_blockE.setValor("S");
    activar(true);
    activar(SALIDA,false);
    usu_nombE.setEnabled(false);
    usu_nombE.setText(EU.usuario);
//    pro_loteE.setEnabled(false);
    deo_codiE.setEnabled(false);
    pro_codenE.setEditable(true);
    setEditEnt(true);
    utdesp.cambio=true;
   
//    Porig.setEnabled(false);
//    Pfin.setEnabled(false);
//    Pconsul.setEnabled(false);
    grd_unidE.requestFocus();
  }

    @Override
  public void ej_addnew1()
  {
    if (grd_unioriE1.getValorInt()==0)
    {
      mensajeErr("Introduzca alguna Linea de Entrada");
      return;
    }
    if ( grd_feccadE.getError())
    {
      mensajeErr("Fecha Caducidad .. Incorrecta");
      return;
    }
    if (deo_blockE.getValor().equals("B") && ! EU.isRootAV())
    {
        msgBox("Estado NO puede ser  Bloqueado");
        return;
    }
    jtEnt.ponValores(jtEnt.getSelectedRow());

    int nCol;
    if ((nCol=check_jtEnt(jtEnt.getSelectedRow()))>=0)
    {
      jtEnt.requestFocus(jtEnt.getSelectedRow(),nCol);
      return;
    }
    if (deo_codiE.getValorInt()==0)
    {
      mensajeErr("Introduzca al menos un producto a Despiezar");
      tid_codiE.requestFocus();
      return;
    }
    try
    {
      if (deo_blockE.getValor().equals("N"))
      {
        String msgErr;
        if ((msgErr=checkCerrar())!=null)
        {
          if (!PARAM_ADMIN)
          {
            msgBox(msgErr);
            return;
          }
          else
          {
              int res=mensajes.mensajeYesNo(msgErr+"\n Cerrar de todos modos ? ");
              if (res!=mensajes.YES)
                return;
              if (jf != null)
              {
                jf.ht.clear();
                jf.ht.put("%a", deo_codiE.getText());
                jf.guardaMens("D5", jf.ht);
              }

          }
        }
      }
       
      if (isEmpPlanta && getNumLinImprimir()>0)
      {
         int ret= mensajes.mensajeYesNo("¿ Salir sin imprimir etiquetas ?");
         if (ret!=mensajes.YES)
             return;
      }
      actualCabDesp(); // Actualizo Cabecera Despiece
      
      resetBloqueo(dtAdd,TABLA_BLOCK,
                   eje_numeE.getValorInt() +
                   "|" + deo_codiE.getValorInt(),true);
      ctUp.commit();
    } catch (Exception k)
    {
      Error("Error al actualizar dato de Despiece",k);
      return;
    }
    activaTodo();
    mensaje("");
    mensajeErr("Despiece ... GUARDADO");
    nav.pulsado=navegador.NINGUNO;
  }
  
  int getNumLinImprimir()
  {
      int nRow=jtSal.getRowCount();
      int nLinImpr=0;
      for (int n=0;n<nRow;n++)
      {
          if (jtSal.getValBoolean(n,JTSAL_IMPRIM))
              nLinImpr++;
      }
      return nLinImpr;
  }
  /**
   * Actualizo cabecera de despiece
   * @throws SQLException 
   */
  void actualCabDesp() throws SQLException,ParseException
  {       
       desorca=new Desporig();
       desorca.setId(new DesporigId(eje_numeE.getValorInt(),deo_codiE.getValorInt()));
       setDesorca();
       desorca.update(dtAdd);
  }
  void setDesorca() throws SQLException,ParseException
  {
   desorca.setCliente(cli_codiE.getValorInt());
   desorca.setDeoFecha(grd_fechaE.getDate());
   desorca.setTidCodi(tid_codiE.getValorInt());
   desorca.setDeoFeccad(grd_feccadE.getDate());
   desorca.setDeoFecpro(grd_fechaE.getDate());
   desorca.setPrvCodi(getPrvDesp(dtStat,EU.em_cod));
   desorca.setDeoDesnue('S');
   desorca.setDeoEjloge(eje_numeE.getValorInt());
   desorca.setDeoSeloge(SERIE);
   desorca.setDeoNuloge(deo_codiE.getValorInt());
   desorca.setDeoLotnue((short) -1);
   desorca.setDeoCerra((short) -1);
   desorca.setDeoAlmori(deo_almoriE.getValorInt());
   desorca.setDeoAlmdes(deo_almdesE.getValorInt());
   desorca.setDeoBlock(deo_blockE.getValor());
   desorca.setDeoNumuni(grd_unidE.getValorInt());
   desorca.setDeoIncval("S");
   desorca.setDeoValor("N");
  }
  
    @Override
  public void canc_addnew() {
    if (deo_codiE.getValorInt()!=0)
    {
        String txt=
      txt=mensajes.mensajeGetTexto(" Teclee 'ANULAR' para anular el Alta del despiece","¡¡Atencion!!");
      if (!txt.toUpperCase().equals("ANULAR"))          
        return;
      try {
        utdesp.copiaDespieceNuevo(dtStat,dtAdd, "Anulada alta de  despiece",EU.usuario,
              eje_numeE.getValorInt(),deo_codiE.getValorInt());
        borraDespEnt();
        borraDespSal();
        if (jf != null)
        {
          jf.ht.clear();
          jf.ht.put("%a", deo_codiE.getText());
          jf.guardaMens("D2", jf.ht);
        }

        ctUp.commit();
      } catch (Exception k)
      {
        Error("Error al ANULAR ALTA",k);
        return;
      }
    }
    nav.pulsado=navegador.NINGUNO;
    mensajeErr("Cancelada ALTA");
    activaTodo();
    verDatos(dtCons);
    
    mensaje("");
  }

  void borraDespEnt() throws Exception
  {
    s="DELETE FROM desporig WHERE  eje_nume ="+eje_numeE.getValorInt()+
        " and deo_codi = "+deo_codiE.getValorInt();
    stUp.executeUpdate(s);
    s="SELECT * FROM desorilin   WHERE eje_nume = "+eje_numeE.getValorInt()+
        " AND deo_codi = "+deo_codiE.getValorInt();
    if (dtBloq.select(s))
    {
      do
      {
        anuStkOri(dtBloq);
      } while (dtBloq.next());
      stUp.executeUpdate("DELETE FROM desorilin WHERE "+dtBloq.getCondWhere());
    }
  }
  
  void borraDespSal() throws Exception
  {
//    s = "SELECT * FROM v_despfin WHERE emp_codi = " + EU.em_cod +
//        " AND eje_nume = " + eje_numeE.getValorInt() +
//        " AND deo_codi = " + deo_codiE.getValorInt();
//    if (dtBloq.select(s))
//    {
//      do
//      {
//        borStkPart(dtBloq.getInt("pro_codi"),eje_numeE.getValorInt(),
//                   EU.em_cod,
//                   dtBloq.getString("def_serlot"),
//                   dtBloq.getInt("pro_lote"),
//                   dtBloq.getInt("pro_numind"),
//                   dtBloq.getDouble("def_kilos"),
//                   dtBloq.getInt("def_numpie"),
//                   dtBloq.getInt("alm_codi"));
//      } while (dtBloq.next());
      stUp.executeUpdate("DELETE FROM v_despfin WHERE "+
          " emp_codi = " + EU.em_cod +
          " AND eje_nume = " + eje_numeE.getValorInt() +
          " AND deo_codi = " + deo_codiE.getValorInt());
    
  }

    @Override
  public void PADDelete()
  {
    if (jtEnt.isVacio() && !PARAM_ADMIN) 
    {
        msgBox("Despiece VACIO. Imposible Borrar");
        nav.pulsado = navegador.NINGUNO;
        activaTodo();
        return;
    }
    if (deo_blockE.getValor().equals("B") && ! EU.isRootAV())
    {
        msgBox("Despiece Bloqueado. Imposible Borrar");
        nav.pulsado = navegador.NINGUNO;
        activaTodo();
        return;
    }
    if (!PARAM_ADMIN && swTienePrec)
    {
      mensajeErr("DESPIECE VALORADO ... IMPOSIBLE BORRAR");
      activaTodo();
      return;

    }
    try {
        if (!checkMvtos(eje_numeE.getValorInt(), deo_codiE.getValorInt()))
                return;
        if (!setBloqueo(dtAdd, TABLA_BLOCK,
                          eje_numeE.getValorInt() +
                          "|" + deo_codiE.getValorInt()))
        {
            msgBox(msgBloqueo);
            activaTodo();
            return;
        }
    } catch (Exception ex) {
      Error("Error al bloquear despiece",ex);
      return;
    }
    Baceptar.setEnabled(true);
    Bcancelar.setEnabled(true);
    mensaje("BORRAR  DESPIECE ...");
    Bcancelar.requestFocus();

  }

    @Override
  public void ej_delete1()
  {
    String valor = "NO";
    valor = mensajes.mensajeGetTexto(
        "Para borrar el DESPIECE teclee la palabra 'BORRAR'",
        "Confirme BORRADO", this, valor);
    if (valor == null)
      valor = "";
    if (!valor.toUpperCase().equals("BORRAR"))
      return;
    try {
      utdesp.copiaDespieceNuevo(dtStat,dtAdd, "Borrado despiece TACTIL",EU.usuario,
              eje_numeE.getValorInt(),deo_codiE.getValorInt());
      borraDespEnt();
      borraDespSal();
      if (jf != null)
      {
        jf.ht.clear();
        jf.ht.put("%a", deo_codiE.getText());
        jf.guardaMens("D4", jf.ht);
      }  
      resetBloqueo(dtAdd, TABLA_BLOCK,
                 eje_numeE.getValorInt() +
                 "|" + deo_codiE.getValorInt(),false);

      ctUp.commit();
      activaTodo();
      rgSelect();
      mensajeErr("Despiece ... BORRADO");
      mensaje("");
      nav.pulsado=navegador.NINGUNO;
    } catch (Exception k)
    {
      Error("Error al ANULAR ALTA",k);
    }
  }
    @Override
  public void canc_delete() {
    activaTodo();
    actButton(true);
    mensajeErr("Borrado .. cancelada");
    nav.pulsado=navegador.NINGUNO;
    mensaje("");
  }

    @Override
  public void activar(boolean b) {
      activar(TODOS,b);
    }
  void activar(int modo, boolean activ)
  {
    if (modo == TODOS)
    {
      activar(CABECERA, activ);
      activar(ENTRADA, activ);
      activar(SALIDA, activ);
     
      grd_feccadE.setEnabled(activ);
      Baceptar.setEnabled(activ);
      Bcancelar.setEnabled(activ);
    }
    switch (modo)
    {
      case CABECERA:
        deo_codiE.setEnabled(activ);
        eje_numeE.setEnabled(activ);
        deo_codiE.setEnabled(activ);
        grd_fechaE.setEnabled(activ);
        grd_unidE.setEnabled(activ);
        tid_codiE.setEnabled(activ);
        deo_almoriE.setEnabled(activ);
        deo_almdesE.setEnabled(activ);
        usu_nombE.setEnabled(activ);
        cli_codiE.setEnabled(activ);
        Bfincab.setEnabled(activ);
        deo_blockE.setEnabled(activ);
        break;
      case SALIDA:
        Bsalkil.setEnabled(activ);
        jtSal.setEnabled(activ);
        grd_kilsalE.setEnabled(activ);
        if (! isEmpPlanta)
            kilsalE.setEnabled(activ);
        if (PARAM_ADMIN || isEmpPlanta)
            def_usunomE.setEnabled(activ);
        def_numpiE.setEnabled(activ);
//        actButton(activ);
        if (!isEmpPlanta)
            BrepEti.setEnabled(activ);
        eti_codiE.setEnabled(activ);
        opImpEti.setEnabled(activ);
        BborLiSa.setEnabled(activ);
        break;
      case ENTRADA:
        jtEnt.setEnabled(activ);
        break;
    }
  }
/**
   * Sentencia sql para mostrar despieces de tactil
   * Saca los de serie generada 'T' (VARIABLE 'SERIE') Y el numero de lote sea nuevo.
   * @return  String con sentencia SQL
   */
  String getStrSql()
  { 
    return "select * from desporig WHERE deo_seloge ='"+SERIE+"'"+
            " and deo_lotnue!=0 ";
    
  }

  String getOrderQuery()
  {
    return " order by eje_nume,deo_codi ";
  }

  void verDatos(DatosTabla dt)
  {
    if (! jtEnt.isVacio())
    {
        jtEnt.removeAllDatos();
        jtOri.removeAllDatos();
        jtFin.removeAllDatos();
        jtSal.removeAllDatos();
    }
    datTrazFrame.resetInit();
    if (dt.getNOREG())
      return;
    if (utdesp!=null)
      utdesp.cambio=true;
    try
    {
      /**
       * Vuelvo a leer el despiece por si acaso
       */
     
      eje_numeE.setValorInt(dt.getInt("eje_nume"));
      deo_codiE.setValorInt(dt.getInt("deo_codi"));
     
     
      s="select * from desporig WHERE eje_nume = "+dt.getInt("eje_nume")+
          " and deo_codi = "+dt.getInt("deo_codi");
      if (! dtStat.select(s))
      {
          msgBox("Despiece NO Encontrado. Introduzca nuevos criterios de consulta");
          return;
      }
//      deo_codiE.setValorInt(dtStat.getInt("deo_nuloge"));
      grd_unidE.setValorDec(dtStat.getInt("deo_numuni"));
      deo_blockE.setValor(dtStat.getString("deo_block"));
      grd_feccadE.setText(dtStat.getFecha("deo_feccad","dd-MM-yyyy"));
      cli_codiE.setValorInt(dtStat.getInt("cli_codi",true));
      
      s="SELECT d.*,pr.pro_nomb FROM desorilin d,v_articulo pr "+
          " WHERE d.pro_codi = pr.pro_codi "+
          " AND d.eje_nume = "+eje_numeE.getValorInt()+
          " AND d.deo_codi = "+deo_codiE.getValorInt()+
          " ORDER BY d.del_numlin";
      
      int nLiOr=0;
      double kgLiOr=0;
      if (dtCon1.select(s))
      { 
        tid_codiE.setText(dtStat.getString("tid_codi"));
        grd_fechaE.setText(dtStat.getFecha("deo_fecha","dd-MM-yyyy"));
        usu_nombE.setText(dtStat.getString("usu_nomb"));
        deo_almoriE.setText(dtStat.getString("deo_almori"));
        deo_almdesE.setText(dtStat.getString("deo_almdes"));
        do
        {
          ArrayList v=new ArrayList();
          v.add(dtCon1.getString("pro_codi"));
          v.add(dtCon1.getString("pro_nomb"));
          v.add(EU.em_cod);
          v.add(dtCon1.getString("deo_ejelot"));
          v.add(dtCon1.getString("deo_serlot"));
          v.add(dtCon1.getString("pro_lote"));
          v.add(dtCon1.getString("pro_numind"));
          v.add(dtCon1.getString("deo_kilos"));
          v.add(dtCon1.getString("del_numlin"));
          jtEnt.addLinea(v);
          jtOri.addLinea(v);
          nLiOr++;
          kgLiOr+=dtCon1.getDouble("deo_kilos");
        } while (dtCon1.next());
        grd_unioriE1.setValorDec(nLiOr);
        grd_kilorgE1.setValorDec(kgLiOr);
        grd_unioriE.setValorDec(nLiOr);
        grd_kilorgE.setValorDec(kgLiOr);
        kildifE.setValorDec(grd_kilsalE.getValorDec()-grd_kilorgE1.getValorDec());

        verProTipDes(tid_codiE.getValorInt());
        verLinSal(EU.em_cod,eje_numeE.getValorInt(),deo_codiE.getValorInt());
      }
    } catch (Exception k)
    {
      Error("Error al Ver deo_blockEDatos",k);
    }
  }
/**
   * Ver Lineas de salida
   * @param empCodi Empresa (EU.em_cod)
   * @param ejeNume Ejercicio 
   * @param deoCodi Numero despiece
   * @throws SQLException 
   */
  void verLinSal(int empCodi,int ejeNume,int deoCodi) throws SQLException
  {
    s = "SELECT d.*,pr.pro_nomb FROM v_despfin d,v_articulo pr " +
        " WHERE d.emp_codi = " + empCodi +
        " and d.pro_codi = pr.pro_codi " +
        " AND d.eje_nume = " + ejeNume+
        " AND d.deo_codi = " + deoCodi+
        " ORDER BY pro_numind";
    jtSal.removeAllDatos();
    jtFin.removeAllDatos();
    grd_unisalE.resetTexto();
    grd_kilsalE.resetTexto();
    grd_unifinE.resetTexto();
    grd_kilfinE.resetTexto();
    swTienePrec = false;
    if (!dtCon1.select(s))
      return;
    do
    {
      ArrayList v=new ArrayList();
      v.add(dtCon1.getString("pro_codi"));
      v.add(dtCon1.getString("pro_nomb"));
      v.add(dtCon1.getString("def_kilos"));
      v.add(dtCon1.getString("def_unicaj"));
      v.add(dtCon1.getString("pro_numind"));      
      if (dtCon1.getDouble("def_prcost") != 0)
        swTienePrec = true;      
     v.add(dtCon1.getString("usu_nomb"));
      jtFin.addLinea(v);      
      v.add(false);
      jtSal.addLinea(v);
    } while (dtCon1.next());
    actAcumSal();
    grd_unifinE.setText(grd_unisalE.getText());
    grd_kilfinE.setText(grd_kilsalE.getText());

    jtSal.requestFocusInicio();
  }
  /**
   * Carga en el panel los productos para los tipos de despiece.
   * Muestra tanto los que aparecen en tipdessal como los equivalentes a estos
   * y los productos sin venta.
   * 
   * @param tidCodi
   * @throws Exception 
   */
  void verProTipDes(int tidCodi) throws Exception
  {
    s="select * from tipdessal WHERE tid_codi="+tidCodi+" order by tds_grupo,pro_codi";
    Pprofin.removeAll();
    htProd.clear();
    if (!dtCon1.select(s))
    {
      msgBox("Tipo Despiece SIN lineas");
      return;
    }
    
    x=0;
    y=0;
    int proCodfin;
    
    do
    {
      ponBoton(dtCon1.getInt("pro_codi"));
      if (CARGAPROEQU)
      {
          // Busco los productos equivalentes.
          s="SELECT * FROM artiequiv WHERE pro_codini="+dtCon1.getInt("pro_codi")+
                  " or pro_codfin ="+dtCon1.getInt("pro_codi");
          if ( dtAux.select(s))
          {
            do
            {
              if (dtAux.getInt("pro_codfin")==dtCon1.getInt("pro_codi"))
                 proCodfin=dtAux.getInt("pro_codini");
             else
                 proCodfin=dtAux.getInt("pro_codfin");
              if (! htProd.containsKey(proCodfin))
                  ponBoton(proCodfin);
            } while (dtAux.next());
          }
      }
    } while (dtCon1.next());
    // Busco los productos de desecho y los pongo.
    s="SELECT * FROM v_articulo WHERE pro_tiplot = 'D' ";
    if ( dtCon1.select(s))
    {
      do
      {
          if (! htProd.containsKey(dtCon1.getInt("pro_codi")))
              ponBoton(dtCon1.getInt("pro_codi"));
      } while (dtCon1.next());
    }
   
  }
  
  private void ponBoton(int proCodi) throws SQLException
  {
      CButton bpr=new CButton();
      String proNomb,proNom1;
      s="SELECT * FROM v_articulo where pro_codi="+proCodi;
      if (!dtStat.select(s))
      {
        proNomb = "ART. NO ENCONTRADO";
        proNom1=proNomb;
      }
      else
      {
        if (dtStat.getInt("pro_activ")>=0)
        {
        //  bpr.setEnabled(false);
          proNomb ="*I*"+dtStat.getString("pro_nomb");// "ART. INACTIVO";
          proNom1=proNomb;
        }
        else
        {
            proNomb = dtStat.getString("pro_nomb");
            proNom1= dtStat.getString("pro_nomcor");
        }
      }
     
      bpr.setMaximumSize(new Dimension(229, 24));
      bpr.setMinimumSize(new Dimension(229, 24));
      bpr.setPreferredSize(new Dimension(229, 24));
      bpr.setMargin(new Insets(0, 0, 0, 0));
      htProd.put(proCodi,bpr);
      bpr.setText("(0) "+Formatear.format(proCodi,"####9")+" - "+proNom1);
      bpr.setToolTipText(Formatear.format(proCodi,"####9")+" - "+proNomb);

      actionPerforMDT actPerf=new actionPerforMDT(bpr,proCodi,this);
      bpr.addActionListener(actPerf);
      Pprofin.add(bpr, new GridBagConstraints(x, y, 1, 1, 1.0, 1.0
               ,GridBagConstraints.CENTER, GridBagConstraints.NONE,
               new Insets(2, 0, 0, 2), 0, 0));
      x++;
      if (x>2)
      {
        x = 0;
        y++;
      }
  
    
  }

  
  void pdf_jtEnt()
  {
    emp_codenE.setValorDec(EU.em_cod);
    eje_numenE.setValorDec(eje_numeE.getValorDec());
    pro_serenE.setText("A");
    jtEnt.setValor(EU.em_cod, 2);
    jtEnt.setValor(eje_numeE.getText(), 3);
    jtEnt.setValor("A", 4);
  }

  int check_jtEnt(int row)
  {
    try
    {
      double kilEnt= pro_kilenE.isEnabled() && pro_kilenE.isEditable() ?pro_kilenE.getValorDec():jtEnt.getValorDec(row,JTENT_KILOS);
      double kilAnt=0;
      if (pro_codenE.isNull())
      {
        calcSumCab(0,0);
        return -1; // SI no hay producto lo doy como bueno.
      }
      if (!pro_codenE.controla(false))
      {
        mensajeErr("Codigo de Producto NO valido");
        return 0;
      }
      if (! pro_codenE.isVendible())
      {
         mensajeErr("Producto NO es vendible");
         return 0;
      }
      if (! pro_codenE.isActivo())
      {
         mensajeErr("Producto NO esta ACTIVO");
         return 0;
      }
      s="SELECT * FROM TIPDESENT WHERE tid_codi = "+tid_codiE.getValorInt()+
          " and pro_codi = "+pro_codenE.getValorInt();
      if (! dtStat.select(s))
      {
        mensajeErr("Producto "+pro_codenE.getValorInt()+ " NO ENCONTRADO EN TIPO DESPIECE");
        return JTENT_PROCODI;
      }

      if (eje_numenE.getValorInt() == 0 && pro_codenE.hasControlIndiv())
      {
        mensajeErr("Introduzca Ejercicio de Lote");
        return JTENT_EJER;
      }
      
      if (pro_serenE.getText().trim().equals("") && pro_codenE.hasControlIndiv())
      {
        mensajeErr("Introduzca Serie de Lote");
        return JTENT_SERIE; 
      }
      if (pro_lotenE.getValorInt() == 0 && pro_codenE.hasControlIndiv() )
      {
        mensajeErr("Introduzca Número de Lote");
        return JTENT_LOTE;
      }
      if (pro_indenE.getValorInt() == 0 && pro_codenE.hasControlIndiv() && !isEmpPlanta)
      {
        mensajeErr("Introduzca Numero de INDIVIDUO");
        return JTENT_NUMIND;
      }
      String condS="desorilin " +
            " WHERE eje_nume = " + eje_numeE.getValorInt() +
            " and deo_codi = " + deo_codiE.getValorInt()+
            " and del_numlin = " + jtEnt.getValorInt(row, JTENT_NL);
      if (jtEnt.getValorInt(row,JTENT_NL)!=0  )
      {
          // Esta linea ya se ha guardado.
         s = "SELECT * FROM  "+condS;
         if (! dtStat.select(s))
         { // Ahora va y no existe la linea... Lanzamos un error.
              Error("Linea Entrada de Despiece no encontrada: " + s,
                    new Exception("Error al Actualizar Linea entrada"));
              return 0;
         }
         if (dtStat.getInt("pro_codi") == pro_codenE.getValorInt() && // Prod.
            dtStat.getInt("deo_ejelot") == eje_numenE.getValorInt() && // ejer
            dtStat.getString("deo_serlot").equals(pro_serenE.getText()) && // Serie
            dtStat.getInt("pro_lote") == pro_lotenE.getValorInt() && // Lote
            dtStat.getInt("pro_numind") == pro_indenE.getValorInt()   ) // Individuo
         { // No Ha cambiado el individuo ni el peso.
             if (dtStat.getDouble("deo_kilos") == kilEnt)
               return -1; // No hago nada.
             else
               kilAnt=0;
         }
      }
      StkPartid stkPartid= buscaPeso();
      if (stkPartid.hasError())
      {
         mensajeErr(stkPartid.getMensaje());
        return JTENT_PROCODI;
      }
      if ( stkPartid.isLockIndiv())
      {
          mensajeErr("Individuo esta bloqueado. Imposible realizar despiece");
          return JTENT_PROCODI;
      }
      if ( stkPartid.isControlExist())
      {
          double kilStock=stkPartid.getKilos();
          if (! pro_kilenE.isEnabled() )
          {
           jtEnt.setValor(kilStock,row,JTENT_KILOS);
           kilEnt=kilStock;
          }
          if (kilEnt  == 0)
          {
            mensajeErr("Introduzca peso de Producto");
            return 7;
          }
          if (jtEnt.getValorInt(row,JTENT_NL)>0 )
          { // Ya existia linea en desglose (con otros kilos, claro)            
            if (dtCon1.select(s))
              kilEnt -= kilAnt;
          }
//          kilact=Formatear.redondea(kilact,2);
//          if (! stkPartid.hasStock())
//          {
//            mensajeErr("Individuo Sin Stock");
//            return 0;
//          }
          if (! stkPartid.hasStock(kilEnt) )
          {
            mensajeErr("No hay suficiente Stock de este individuo. Exist:"+kilStock);
            return 7;
          }
          
      }
      if (row==0)
      {
        utdesp.cambio=true;
        jtEnt.procesaAllFoco(row);
        if (! buscaDatInd())
          return JTENT_EJER;
      }
      if (jtEnt.getValorInt(row,JTENT_NL)!=0)
      {
          // Esta linea ya se ha guardado.
         s = "SELECT * FROM "+condS;
         if (! dtAdd.select(s, true))
         { // Ahora va y no existe la linea... Lanzamos un error.
              Error("Linea Entrada de Despiece no encontrada: " + s,
                    new Exception("Error al Actualizar Linea entrada"));
              return 0;
         }
         if (dtAdd.getInt("pro_codi") != pro_codenE.getValorInt() || // Prod.
            dtAdd.getInt("deo_ejelot") != eje_numenE.getValorInt() || // ejer
            !dtAdd.getString("deo_serlot").equals(pro_serenE.getText()) || // Serie
            dtAdd.getInt("pro_lote") != pro_lotenE.getValorInt() || // Lote
            dtAdd.getInt("pro_numind") != pro_indenE.getValorInt() || // Ind
            dtAdd.getDouble("deo_kilos") != kilEnt ) // Peso
         { // Ha cambiado. Anulo la linea.
//            stkPart.anuStkPart(dtAdd.getInt("pro_codi") ,
//                     dtAdd.getInt("deo_ejelot"),
//                     EU.em_cod,
//                     dtAdd.getString("deo_serlot"),
//                     dtAdd.getInt("pro_lote"),
//                     dtAdd.getInt("pro_numind"),
//                     deo_almoriE.getValorInt(),
//                     dtAdd.getDouble("deo_kilos")*-1,-1);
            dtAdd.executeUpdate("delete from "+condS);
            guardaOrigDesp(row,kilEnt);
          }  
        }
      else
        guardaOrigDesp(row,kilEnt);
      calcSumCab(pro_codenE.getValorInt(), kilEnt );
    }
    catch (Exception k)
    {
      Error("Error al cambiar linea Grid Entrada", k);
    }

    return -1;
  }

  void calcSumCab(int proCodi,double kgLin)
  {
    int nRow=jtEnt.getRowCount();
    int nInd=0,selRow=jtEnt.getSelectedRow();
    double kg=0;
    for (int n=0;n<nRow;n++)
    {
      if (n==selRow)
      {
        if (proCodi == 0)
          continue;
      }
      else
      {
        if (jtEnt.getValorInt(n, 0) == 0)
          continue;
      }
      nInd++;
      if (n==selRow)
        kg+=kgLin;
      else
        kg+=jtEnt.getValorDec(n,7);
    }
    grd_unioriE1.setValorDec(nInd);
    grd_kilorgE1.setValorDec(kg);
    kildifE.setValorDec(grd_kilsalE.getValorDec()-grd_kilorgE1.getValorDec());

  }

  StkPartid buscaPeso() throws Exception
  {
    StkPartid stkPartid = utildesp.buscaPeso(dtCon1, eje_numenE.getValorInt(),
                                       EU.em_cod,
                                       pro_serenE.getText(),
                                       pro_lotenE.getValorInt(),
                                       pro_indenE.getValorInt(),
                                       pro_codenE.getValorInt(),deo_almoriE.getValorInt());
    if (stkPartid.hasError())
      return stkPartid;
//    if (stkPartid.getEstado()==StkPartid.INDIV_LOCK)
//    {
//        swArtBloq=true;
//        return stkPartid;
//    }
    mensajeErr(stkPartid.getMensaje());
    return stkPartid;
  }
  /**
   * Consulta Lotes Disponibles de Productos.
   */
  public void consLote()
  {
    try
    {
      if (ayuLot == null)
      {
        ayuLot = new ayuLote(EU, vl, dtCon1, pro_codenE.getValorInt())
         {
             @Override
             public void matar(boolean cerrarConexion)
             {
               ayuLot.setVisible(false);
               ej_consLote();
             }
        };

//       vl.add(ayuLot);
        ayuLot.setLocation(25, 25);
        ayuLot.iniciarVentana();
      }
      vl.add(ayuLot);
      ayuLot.setVisible(true);
      ayuLot.muerto=false;
      ayuLot.statusBar.setEnabled(true);
      ayuLot.statusBar.Bsalir.setEnabled(true);
      ayuLot.setClosed(false);

      this.setEnabled(false);
      this.setFoco(ayuLot);
      ayuLot.cargaGrid(pro_codenE.getText());
    }
    catch (Exception j)
    {
      this.setEnabled(true);
    }
  }

    @SuppressWarnings("empty-statement")
  void ej_consLote()
  {
    if (ayuLot.consulta)
    {
//     deo_emplotE.setValorDec(ayuLot.jt.getValInt(0));
      eje_numenE.setValorDec(ayuLot.jt.getValorInt(ayuLot.rowAct, 1));
      pro_serenE.setText(ayuLot.jt.getValString(ayuLot.rowAct,2));
      pro_lotenE.setValorDec(ayuLot.jt.getValorInt(ayuLot.rowAct,3));
      pro_indenE.setValorDec(ayuLot.jt.getValorInt(ayuLot.rowAct,4));
      pro_kilenE.setValorDec(ayuLot.jt.getValorDec(ayuLot.rowAct,5));
      jtEnt.setValor(ayuLot.jt.getValorDec(ayuLot.rowAct,5));
    }
    ayuLot.setVisible(false);
    this.setEnabled(true);
    this.toFront();
    try
    {
      this.setSelected(true);
    }
    catch (Exception k) {}
    this.setFoco(null);
    javax.swing.SwingUtilities.invokeLater(new Thread()
    {
            @Override
      public void run()
      {
        jtEnt.requestFocus(jtEnt.getSelectedRow(), 5);
      }
    });
  }
  void guardaOrigDesp(int n,double kilEnt) throws Exception
  {
    dtAdd.commit();// Para comenzar la transaccion.
    if (deo_codiE.getValorDec() == 0)
    {     
      deo_codiE.setValorInt(guardaOrigCabDesp());
//      pro_loteE.setValorInt(deo_codiE.getValorInt());
      activar(CABECERA,false);
      activar(SALIDA, true);
      deo_blockE.setEnabled(true);
      verProTipDes(tid_codiE.getValorInt());
    }
    
    int nl=guardaOrigLinDesp(pro_codenE.getValorInt(), 
                          eje_numenE.getValorInt(),
                          pro_serenE.getText(),
                          pro_lotenE.getValorInt(),pro_indenE.getValorInt(),
                          kilEnt,dtAdd);
    ctUp.commit();
    jtEnt.setValor(""+nl,n,JTENT_NL);
    mensajeErr("Linea Guardada ...",false);
  }
  /**
   * Guarda cabecera  origen de despiece
   * @return 
   */
  int guardaOrigCabDesp() throws SQLException,ParseException
  {
      desorca=new Desporig();
      deo_codiE.setValorInt(utildesp.incNumDesp(dtAdd,EU.em_cod,eje_numeE.getValorInt()));
      desorca.setId(new DesporigId(eje_numeE.getValorInt(),deo_codiE.getValorInt()));
      
      setDesorca();
      desorca.save(dtAdd, eje_numeE.getValorInt(),EU);
      setBloqueo(dtAdd,TABLA_BLOCK,
                   eje_numeE.getValorInt() +
                   "|" + deo_codiE.getValorInt(),false);
      return desorca.getId().getDeoCodi();
  }
  /**
   * Guarda lineas de origen de despiece
   * 
   * @param proCodi
   * @param ejeLot
   * @param serLot
   * @param numLot
   * @param numInd
   * @param kilos
   * @param dtAdd
   * @throws Exception 
   */
  int guardaOrigLinDesp(int proCodi,  int ejeLot, String serLot,
                    int numLot,int numInd, double kilos, DatosTabla dtAdd) throws Exception
  {
    
      desorli=new gnu.chu.anjelica.sql.Desorilin();
      desorli.setId(new DesorilinId(eje_numeE.getValorInt(),deo_codiE.getValorInt(),0));
      desorli.getId().setDelNumlin(desorli.getNextLinea(dtAdd));
      desorli.setDeoEjelot(ejeLot);
      desorli.setDeoKilos(kilos);
      desorli.setDeoPrcost(0.0);
      //despOriLin.setDeoPrusu(null);
      desorli.setDeoSerlot(serLot.charAt(0));
      desorli.setProCodi(proCodi);
      desorli.setProLote(numLot);
      desorli.setProNumind(numInd);
      desorli.save(dtAdd);
 
      mensajeErr("Cabecera -- Actualizando Stock Partidas ... ", false);
    // Descuento de Stock Partidas los kilos
      stkPart.restar(ejeLot,serLot,numLot,numInd,proCodi,deo_almoriE.getValorInt(),
                     kilos,1);
      mensajeErr("Linea origen guardada");
      return desorli.getId().getDelNumlin();
  }

 

 /**
  * Aumenta el No de despice en v_numerac
  *
  * @param dt DatosTabla
  * @throws Exception
  * @return int Numero de despiece una vez incrementado
  */
 int incNumDesp(DatosTabla dt) throws Exception
 {
   s = "SELECT num_despi FROM v_numerac WHERE emp_codi = " + EU.em_cod+
       " AND eje_nume = " + eje_numeE.getValorInt();
    if (! dt.select(s,true))
      throw new Exception("NO encontrado NUMERO de despiece");
    int numDesp=dt.getInt("num_despi")+1;
    dt.edit(dt.getCondWhere());
    dt.setDato("num_despi",numDesp);
    dt.update(stUp);
    return numDesp;
 }
// void guardaGrupDesp() throws Exception
// {
//   // Localiza el Grupo de Despiece a asignar
//   pro_loteE.setValorDec(deo_codiE.getValorInt());
//  
//
//   mensajeErr("Guardando Cabecera de Despiece ...",false);
//   dtAdd.update(stUp);
//   activar(CABECERA,false);
//   activar(SALIDA, true);
//   deo_blockE.setEnabled(true);
////   grd_feccadE.setEnabled(true);
//   verProTipDes(tid_codiE.getValorInt());
// }
 void del_jtEnt(int ejeNume,int numDesp,int numLin)
 {
   try
   {         
     String condWhere=" WHERE eje_nume = "+ejeNume+
         " AND deo_codi = "+numDesp+
         " and del_numlin = "+numLin;
      s="SELECT * FROM desorilin "+condWhere;
     if (! dtAdd.select(s,true))
       throw new Exception("Linea de Despiece: "+numDesp+"-"+numLin+" NO Encontrada");
     // Repongo el Stock
     anuStkOri(dtAdd);
     stUp.executeUpdate("delete from desorilin "+condWhere);
     ctUp.commit();
     mensajeErr("LINEA ... BORRADA",false);
   } catch (Exception k)
   {
     Error("Error al Borrar Linea de Entrada",k);
    }
 }
 /**
  * Anula Stock (lo suma) cuando anulamos una linea de Desp. Origen
  * @param dt DatosTabla donde esta la cabecera Desp. Origen a Anular
  * @throws Exception En caso error base datos
  */
 void anuStkOri(DatosTabla dt) throws Exception
 {
   stkPart.sumar( dt.getInt("deo_ejelot"),dt.getString("deo_serlot"),
                  dt.getInt("pro_lote") ,dt.getInt("pro_numind"),
                  dt.getInt("pro_codi"),deo_almoriE.getValorInt(),
                  dt.getDouble("deo_kilos"),1);

 }

 void Bsalkil_focusGained()
 {
   if (! kilsalE.hasCambio() && ! def_usunomE.hasCambio() && ! def_numpiE.hasCambio()  )
       return;
   if (pro_codsalE.isNull())
   {
     mensajeErr("Introduzca Código de Producto");
     return;
   }
   if (def_numpiE.getValorDec()==0)
   {
     mensajeErr("Introduzca Número de Piezas");
     return;
   }
   if (kilsalE.getValorDec()==0 && ! isEmpPlanta)
   {
     mensajeErr("Introduzca Kilos de Salida");
     return;
   }
   kilsalE.resetCambio();
   def_usunomE.resetCambio();
   def_numpiE.resetCambio();
   try {
     if (modLinSal)
     {
       if (! borrarLinSa(nlSalE.getValorInt()))
         return;
     }
     dtAdd.commit();// Para comenzar la transaccion.
     int nInd = guardaLinDesp(eje_numeE.getValorInt(), EU.em_cod,
                              SERIE,
                              deo_codiE.getValorInt(), pro_codsalE.getValorInt(),
                              kilsalE.getValorDec(), def_numpiE.getValorInt(),
                              def_usunomE.getText(),
                              deo_codiE.getValorInt());
//     if (pro_codsalE.isVendible())
//     {
//        insStkPart(pro_codsalE.getValorInt(), eje_numeE.getValorInt(),
//                EU.em_cod,
//                SERIE+"  ", deo_codiE.getValorInt(),
//                nInd, kilsalE.getValorDec(),1);//def_numpiE.getValorInt());
//     }
     ctUp.commit();
     ArrayList v = new ArrayList();
     v.add(pro_codsalE.getText());
     v.add(pro_codsalE.getTextNomb());
     v.add(kilsalE.getText());
     v.add(def_numpiE.getText());
     v.add("" + nInd);
     v.add(def_usunomE.getText());
     v.add(true);
     if (!modLinSal)
       jtSal.addLinea(v);
     else
     {
       jtSal.requestFocus(nlSalE.getValorInt(),0);
       jtSal.setLinea(v);
     }
     if (jtEnt.getSelectedRow() == 0)
       setEditEnt(false);
     if (opImpEti.isSelected() && pro_codsalE.isVendible())
       imprEtiSal(jtSal.getSelectedRow());
     if (!modLinSal)
     {
        pro_codsalE.resetTexto();
        nlSalE.setText("");
     }
    // modLinSal=false;
     actAcumSal();
   } catch (Exception k)
   {
     Error("Error al Insertar Linea Despiece",k);
   }
 }

 void actAcumSal()
 {
   int nRow=jtSal.getRowCount();
   int nPieza=0;
   double kgSal=0;
   int unid;

   htAcu.clear();
   for (int n=0;n<nRow;n++)
   {
     nPieza+=jtSal.getValorInt(n,3);
     kgSal+=jtSal.getValorDec(n,2);
     if (htAcu.get(jtSal.getValorInt(n,0))==null)
       htAcu.put(jtSal.getValorInt(n,0),jtSal.getValorInt(n,3));
     else
     {
       unid=htAcu.get(jtSal.getValorInt(n,0));
       unid+=jtSal.getValorInt(n,3);
       htAcu.put(jtSal.getValorInt(n,0),unid);
     }
   }

   Iterator <Integer>  en = htProd.keySet().iterator();
   
   CButton cbut;
   int codPro;
   String text;
   int p;
   while (en.hasNext())
   {
     codPro=en.next();
     cbut=(CButton) htProd.get(codPro);
     text=cbut.getText();
     p=text.indexOf(")");
     text=text.substring(p+1);
     unid=0;
     if (htAcu.get(codPro)!=null)
       unid= htAcu.get(codPro);
     cbut.setText("("+Formatear.format(unid,"##9").trim()+")"+text);
   }
   grd_unisalE.setValorDec(nPieza);
   grd_kilsalE.setValorDec(kgSal);
   kildifE.setValorDec(grd_kilsalE.getValorDec()-grd_kilorgE1.getValorDec());
 }
 
 void imprPaginaEtiq() throws Exception
 {
      if (getNumLinImprimir()==0)
     {
         msgBox("No hay etiquetas para imprimir");
         return;
     }
     utdesp.cambio=true;
     
     buscaDatInd();         
     etiq.setPrintDialog(false);
     CodigoBarras codBarras= new CodigoBarras("D",eje_numeE.getText().substring(2),
            SERIE,deo_codiE.getValorInt(),1,
                1,
                0);
     etiq.listarPagina(dtStat,grd_fechaE.getDate(),
         jtSal,codBarras);
     mensajeErr("Etiquetas impresas");     
 }
 
 void imprEtiSal(int linea) 
 {
     imprEtiSal(linea, false);
   
 }
 /**
  * 
  * @param linea
  * @param forzar 
  */
 void imprEtiSal(int linea,boolean forzar)
 {
   try
   {        
     pro_codsalE.getNombArt(jtSal.getValString(linea,JTSAL_PROCODI));
     if (eti_codiE.getValorInt()==0)
     {
        if (pro_codsalE.getLikeProd().isNull("pro_codeti"))
          tipoetiq = 0;
        else
          tipoetiq = pro_codsalE.getLikeProd().getInt("pro_codeti");
     }
     else
         tipoetiq=eti_codiE.getValorInt();
     if (etiq == null)
        etiq = new etiqueta(EU);
     etiq.setTipoEtiq(dtStat,EU.em_cod,tipoetiq);
     //@todo Implementar funcion de diferentes tipos de etiquetas en el mismo despiece
     
     if (etiq.getEtiquetasPorPagina()>1)
     {// Mas de una etiqueta x pagina
       if (forzar)
       {
           imprPaginaEtiq();
           return;
       }
       jtSal.setValor(true,linea,JTSAL_IMPRIM);
       return;
     }
     buscaDatInd(); 
     utdesp.actualConservar(jtSal.getValorInt(linea,JTSAL_PROCODI),dtStat);
     CodigoBarras codBarras= new CodigoBarras("D",eje_numeE.getText().substring(2),
            SERIE,deo_codiE.getValorInt(),jtSal.getValorInt(linea, JTSAL_PROCODI),
                jtSal.getValorInt(linea, JTSAL_NUMIND),
                jtSal.getValorDec(linea, JTSAL_KILOS ));
     
                 
     etiq.iniciar(codBarras.getCodBarra(),codBarras.getLote(),
                  jtSal.getValString(linea,JTSAL_PROCODI),jtSal.getValString(linea,JTSAL_PRONOMB),
                  utdesp.nacidoE, utdesp.cebadoE, utdesp.despiezadoE,
                  utdesp.ntrazaE, jtSal.getValorDec(linea,JTSAL_KILOS),
                  utdesp.getConservar(), utdesp.sacrificadoE,
                   grd_fechaE.getDate(),grd_fechaE.getDate(),
                  pro_codsalE.isCongelado()?null:grd_feccadE.getDate(),utdesp.getFecSacrif());
     etiq.setPrintDialog(false);    
    
//     etiq.setFechaCongelado(utildesp.getFechaCongelado(jtSal.getValorInt(linea,JTSAL_PROCODI ),
//         grd_fechaE.getDate(), dtStat));
     etiq.listarDefec();
   }
   catch (Throwable k)
   {
     Error("Error al Imprimir Etiqueta Salida", k);
   }
 }


 int guardaLinDesp(int ejeLot,int empLot,String serLot,int numLot,
                    int proCodi,double kilos,int numPie,String defUsunom, int numDes) throws Exception
 {
   int nInd = utildesp.getMaxNumInd(dtAdd,proCodi,eje_numeE.getValorInt(),
                                    EU.em_cod,SERIE,deo_codiE.getValorInt());
  
   s = "SELECT max(pro_numind) as numind FROM v_despfin " +
       " WHERE eje_nume = " + eje_numeE.getValorInt() +
       " and emp_codi = " + EU.em_cod+
       " and def_ejelot = " + ejeLot +
//       " and def_emplot = " + empLot +
       " and def_serlot = '" + serLot + "'" +
       " and pro_lote = " + numLot;
   dtAdd.select(s);
   if (dtAdd.getInt("numind",true)>=nInd )
       nInd=dtAdd.getInt("numind",true)+1;
   s="SELECT MAX(def_orden) as def_orden "+
       " from v_despfin WHERE  eje_nume = " + eje_numeE.getValorInt() +
       " and emp_codi = " + EU.em_cod+
       " and deo_codi = "+ numDes;
   dtAdd.select(s);
   int defOrden=dtAdd.getInt("def_orden",true);
   defOrden++;

   s = "SELECT * FROM v_despfin " +
       " WHERE eje_nume = " + eje_numeE.getValorInt() +
       " and emp_codi = " + EU.em_cod +
       " and def_ejelot = " + ejeLot +
       " and def_emplot = " + empLot +
       " and def_serlot = '" + serLot + "'" +
       " and pro_lote = " + numLot +
       " and pro_numind = " + nInd;

   if (! dtAdd.select(s, true))
   {
     dtAdd.addNew();
     dtAdd.setDato("emp_codi", EU.em_cod);
     dtAdd.setDato("eje_nume", eje_numeE.getValorInt());
     dtAdd.setDato("deo_codi", numDes);
     dtAdd.setDato("def_orden", defOrden);
     dtAdd.setDato("def_ejelot", ejeLot);
     dtAdd.setDato("def_emplot", empLot);
     dtAdd.setDato("def_serlot", serLot);
     dtAdd.setDato("pro_lote",numLot);
     dtAdd.setDato("pro_numind",nInd);
     dtAdd.setDato("def_tippes","V");
     dtAdd.setDato("def_numdes",0); // Grupo del No Desp.
     dtAdd.setDato("alm_codi",deo_almdesE.getValorInt());
     dtAdd.setDato("usu_nomb", defUsunom);
     dtAdd.setDato("def_cerra",-1);
     dtAdd.setDato("def_prcost",0);
     dtAdd.setDato("def_tiempo",   "current_timestamp");
   }
   else // NO SE DEBERIA DAR NUNCA ESTE CASO. 
     dtAdd.edit(dtAdd.getCondWhere());

   dtAdd.setDato("pro_codi", proCodi);//pro_codlE.getValorInt());
   dtAdd.setDato("def_unicaj",numPie);
   dtAdd.setDato("def_numpie",1);
   dtAdd.setDato("def_kilos",kilos);//def_kilosE.getValorDec());
   java.util.Date fecha;
   try {
     fecha=grd_feccadE.getDate();
   } catch (ParseException k)
   {
     aviso("Problema fecha caducidad en tactil\nFecha Cad. No valida "+grd_feccadE.getText()+" EN Tactil con grupo: "+
                                          deo_codiE.getValorInt(),k);
     fecha=null;
   }
   dtAdd.setDato("def_feccad",fecha);//def_kilosE.getValorDec());
   dtAdd.update(stUp);
   return nInd;
 }
 
 /**
  * Devuelve el Proveedor de Despieces para la empresa dada
  * @param dt DatosTabla
  * @param empCod int
  * @throws SQLException
  * @return int Proveedor de Despieces
  */
 int getPrvDesp(DatosTabla dt,int empCod) throws SQLException
 {
   return gnu.chu.anjelica.pad.pdconfig.getPrvDespiece(empCod,dt);
 }
// int borStkPart(int proCodi,int ejeLot,int empLot,String serLot,int numLot,int nInd,
//                double kilos,int numPie,int almCodi) throws Exception
// {
//   return stkPart.anuStkPart(proCodi,ejeLot,empLot,serLot,numLot,nInd,almCodi,
//                     kilos,numPie);
//
// }

 void setEditEnt(boolean edit)
 {
   eje_numenE.setEditable(edit);
   pro_lotenE.setEditable(edit);
   pro_serenE.setEditable(edit);
   pro_indenE.setEditable(edit);
   pro_kilenE.setEditable(edit);
 }
 
 private void actButton(boolean activ)
 {    
  
   Iterator <CButton> en=htProd.values().iterator();
   while (en.hasNext())
   {
      en.next().setEnabled(activ);
   }
 }

 boolean borrarLinSa(int row)
 {
   try
   {
     if (!ActualStkPart.checkStock(dtStat, jtSal.getValorInt(row, JTSAL_PROCODI),
                eje_numeE.getValorInt(), EU.em_cod,
                SERIE, deo_codiE.getValorInt(),
                jtSal.getValorInt(row, JTSAL_NUMIND), deo_almdesE.getValorInt(),
                jtSal.getValorDec(row,JTSAL_KILOS),1))
     {
                msgBox("Individuo ha tenido mvtos. Imposible borrar linea");
                return false;
     }
     if (borraLinDesp(eje_numeE.getValorInt(), EU.em_cod,
                      SERIE,deo_codiE.getValorInt(),jtSal.getValorInt(row, 0),
                      jtSal.getValorInt(row, 4)) != 1)
     {
       msgBox("ERROR AL BORRAR LINEA DE DESPIECE");
       enviaMailError("Tactil: Al borrar, no encontrada linea de despiece \n"+
               "Articulo: "+jtSal.getValorInt(row, 0)+" Lote: "+
               eje_numeE.getValorInt()+"-"+ EU.em_cod+"-"+
                      SERIE+ deo_codiE.getValorInt()+"-"+jtSal.getValorInt(row, 4));
       return false;
     }
//     if (pro_codsalE.isVendible())
//     {
//        borStkPart(jtSal.getValorInt(row,0), eje_numeE.getValorInt(), EU.em_cod,
//                serLotDF, deo_codiE.getValorInt(),
//                jtSal.getValorInt(row, 4),jtSal.getValorDec(row,2),jtSal.getValorInt(row,3),
//                deo_almdesE.getValorInt());
//     }
     ctUp.commit();
   }
   catch (Exception ex)
   {
     Error("Error al borrar Linea", ex);
   }
   return true;
 }
 int borraLinDesp(int ejeLot,int empLot,String serLot,int numLot,int proCodi,int nInd) throws Exception
 {
   s = "select * from v_despfin " +
       " WHERE eje_nume = " + eje_numeE.getValorInt() +
       " and emp_codi = " + EU.em_cod +
       " and pro_codi = "+proCodi+
       " and def_ejelot = " + ejeLot +
       " and def_emplot = " + empLot +
       " and pro_lote = "+ numLot+
       " and def_serlot = '" + serLot + "'" +
       " and pro_numind = " + nInd;
   if (!dtAdd.select(s))
     return 0;
   serLotDF=dtAdd.getString("def_serlot");
   s = "DELETE FROM v_despfin "+
    " WHERE "+dtAdd.getCondWhere();
   int nLiAf=stUp.executeUpdate(s);
   return nLiAf;
 }

 String checkCerrar() throws Exception
 {
   if ( isEmpPlanta)
   {
       grd_unidE.setValorInt(grd_unioriE1.getValorInt());
   }
   else
   {
    // Compruebo que el Num. de Individuos de Entrada coincide con el que me han dicho
    if (grd_unidE.getValorInt()!= grd_unioriE1.getValorInt())
    {
      grd_unidE.requestFocus();
      return "Num.  Productos a Despiezar ("+grd_unioriE1.getValorInt()+") no es el introducido ";

    }
    // Compruebo que los Kilos Entrada/Salida Coincidan
    if (kildifE.getValorDec() > grd_kilorgE1.getValorDec() * MantDesp.LIMDIF ||
        kildifE.getValorDec() <
        grd_kilorgE1.getValorDec() * MantDesp.LIMDIF * -1)
    {
       jtSal.requestFocusInicio();
       Ptabed.setSelectedIndex(2);
       return "Diferencia de Kilos SUPERA EL " + (MantDesp.LIMDIF * 100) +
                  " POR CIENTO";
    }
   }
   // Compruebo que el Número de codigos salidos coincidan con el tipo despiece
   HashMap<Integer,Integer> htGru = new HashMap();
   s="SELECT distinct(tds_grupo) as tds_grupo FROM tipdessal  WHERE tid_codi = "+tid_codiE.getValorInt();
   if (dtStat.select(s))
   {
     do
     {
       htGru.put(dtStat.getInt("tds_grupo"),0);
     } while (dtStat.next());
   }


   int nRow=jtSal.getRowCount();
   int nEle;
   for (int n=0;n<nRow;n++)
   {
     if (MantArticulos.getTipoProd(jtSal.getValorInt(n,0),dtStat).equals("D"))
         continue; // Es de deshecho.
     s="SELECT tds_grupo FROM tipdessal  WHERE tid_codi = "+tid_codiE.getValorInt()+
         " and pro_codi = "+jtSal.getValorInt(n,0);
     if (! dtStat.select(s))
     { // Quizas es un producto equivalente.
       s="SELECT tds_grupo FROM tipdessal  WHERE tid_codi = "+tid_codiE.getValorInt()+
         " and (pro_codi in (select pro_codini from artiequiv where pro_codfin = "+
               jtSal.getValorInt(n, 0)+") "+
               " or pro_codi in (select pro_codfin from artiequiv where pro_codini = "+
               jtSal.getValorInt(n, 0)+"))";
       if (!dtStat.select(s))
       {
           jtSal.requestFocusInicio();
           Ptabed.setSelectedIndex(2);
           return "Producto: " + jtSal.getValorInt(n, 0) +
                      " NO Encontrado en Tipos de Despiece";
       }
     }
     if (htGru.get(dtStat.getInt("tds_grupo"))==null)
       htGru.put(dtStat.getInt("tds_grupo"),jtSal.getValorInt(n,3));
     else
     {
       nEle= htGru.get(dtStat.getInt("tds_grupo"));
       nEle+=jtSal.getValorInt(n,3);
       htGru.put(dtStat.getInt("tds_grupo"),nEle);
     }
   }
   Iterator<Integer> en=htGru.keySet().iterator();
   int grupo;
   int tdsUnid;
   while (en.hasNext())
   {
     grupo =  en.next();
     s="SELECT tds_unid,pro_codi FROM tipdessal  WHERE tid_codi = "+tid_codiE.getValorInt()+
         " and tds_grupo = " + grupo;
     dtStat.select(s);
     nEle=htGru.get(grupo);
     tdsUnid=dtStat.getInt("tds_unid")*grd_unidE.getValorInt();
     if (nEle != tdsUnid && tdsUnid>0 )
     {
       jtSal.requestFocusInicio();
       Ptabed.setSelectedIndex(2);
       return "Grupo: " + grupo +" ( Prod: "+dtStat.getInt("pro_codi")+" )"+
                  " con "+nEle+" Elementos. Deberia Tener "+tdsUnid;
     }
   }
   return null;
 }
  void mostrarDatosTraz()
  {
      try {
        
         
          if  (!datTrazFrame.isInit())
          {
             datTrazFrame.setDatos(jtEnt.getValorInt(0,JTENT_PROCODI),
                jtEnt.getValString(0,JTENT_SERIE),
                jtEnt.getValorInt(0,JTENT_EJER),
                jtEnt.getValorInt(0,JTENT_LOTE),
                jtEnt.getValorInt(0,JTENT_NUMIND));
             datTrazFrame.actualizar();
          }
                   
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
      utdesp=datTrazFrame.getUtilDespiece();   
      this.setSelected(true);
    }
    catch (Exception k)
    {}
    
  }
// void BmodDaIn_actionPerformed()
// {
//   try
//      {
//        if (IFdatOr == null)
//        {
//          IFdatOr = new datTraza();
//          IFdatOr.addInternalFrameListener(new InternalFrameAdapter()
//          {
//            @Override
//            public void internalFrameClosing(InternalFrameEvent e)
//            {
//              salDatTraza();
//            }
//          });
//
//          IFdatOr.setLocation(25, 25);
//          IFdatOr.iniciarVentana();
//        }
//        vl.add(IFdatOr);
//        IFdatOr.setVisible(true);
//        IFdatOr.muerto=false;
//        IFdatOr.statusBar.setEnabled(true);
//        IFdatOr.statusBar.Bsalir.setEnabled(true);
//        IFdatOr.setClosed(false);
//
//        this.setEnabled(false);
//        this.setFoco(IFdatOr);
//        buscaDatInd();
//        IFdatOr.setUtilDesp(utdesp);
//      }
//      catch (Exception j)
//      {
//        this.setEnabled(true);
//      }
//    }

//    void salDatTraza()
//    {
//      IFdatOr.setVisible(false);
//      utdesp=IFdatOr.getUtilDesp();
//      this.setEnabled(true);
//      this.toFront();
//      try
//      {
//        this.setSelected(true);
//      }
//      catch (Exception k) {}
//      this.setFoco(null);
//    }
//
//}
}
class actionPerforMDT implements ActionListener
{
 int proCodi;
 CButton button;
 MantDespTactil pdtac;

 public actionPerforMDT(CButton but,int prod,MantDespTactil pdtac)
 {
   proCodi=prod;
   but=button;
   this.pdtac=pdtac;
 }

    @Override
 public void actionPerformed(ActionEvent e)
 {
   pdtac.pro_codsalE.setText(""+proCodi);
   pdtac.def_numpiE.setText("1");
   pdtac.nlSalE.setText("");
   pdtac.modLinSal=false;
   pdtac.kilsalE.resetTexto();
   pdtac.kilsalE.setCambio(true); 
   pdtac.def_numpiE.requestFocus();
//   System.out.println("pulsado: "+proCodi);
 }
}
 
