package gnu.chu.anjelica.ventas;

import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import gnu.chu.sql.*;
import java.sql.*;
import gnu.chu.Menu.*;
import java.awt.*;
import java.util.*;
import gnu.chu.camposdb.*;
import gnu.chu.interfaces.*;
import java.awt.event.*;
import javax.swing.*;
import gnu.chu.anjelica.almacen.pstockAct;
import gnu.chu.anjelica.listados.Listados;
import gnu.chu.anjelica.pad.MantArticulos;
import gnu.chu.anjelica.pad.MantTarifa;
import gnu.chu.anjelica.pad.pdclien;
import net.sf.jasperreports.engine.*;
import gnu.chu.anjelica.pad.pdconfig;

import gnu.chu.winayu.AyuArt;
import java.awt.print.PrinterException;
import java.net.UnknownHostException;
import java.text.ParseException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
/**
 *
 * <p>Título: pdpeve </p>
 * <p>Descripción: Mantenimiento Pedidos de Ventas. Incluye panel consulta stock de Productos</p>
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
 * @author chuchiP
 * @version 1.0
 *
 */

public class pdpeve  extends ventanaPad   implements PAD
{
  ArrayList<Integer> alArtStock=new ArrayList();
  ArrayList<Integer> alArtStockP=new ArrayList();
   JMenuItem MbusCliente = new JMenuItem("Buscar Ped. Cliente");
  DatosTabla dtHist;
  private String tablaCab="pedvenc";
  ayuVenPro ayVePr = null;
  private String vistaPed="v_pedven";
  final static String TABLACAB="pedvenc";

  public final static String VISTAPED="v_pedven";
  
  Cgrid jtHist=new Cgrid(4);
  private int hisRowid=0;
  private boolean P_ADMIN=false;
  private boolean P_VERPRECIOS=false;
  AyuArt aypro;
  CLabel pvc_estadL=new CLabel("Estado");
  CLabel pvc_deposL=new CLabel("Deposito");
  CComboBox pvc_deposE=new CComboBox();
  CCheckBox opAvisoStock=new CCheckBox("Aviso Stock",true);
  CComboBox pvc_estadE=new CComboBox();
  CButton Bimpri=new CButton(Iconos.getImageIcon("print"));
  CButton BbusProd=new CButton(Iconos.getImageIcon("buscar"));
  String s;
  int ALMACEN = 1;
  CTextField prv_nombE = new CTextField();
  proPanel pro_codiE = new proPanel();
  prvPanel prv_codiE = new prvPanel();
  CTextField pvl_feccadE= new CTextField(Types.DATE,"dd-MM-yy");
  CTextField pvl_numlinE=new CTextField(Types.DECIMAL,"##9");
  CTextField pro_nombE=new CTextField(Types.CHAR,"X",50);
  CTextField pvl_cantiE=new CTextField(Types.DECIMAL,"--,--9");
  CTextField pvm_cantiE=new CTextField(Types.DECIMAL,"--,--9");
  CTextField pvl_precioE=new CTextField(Types.DECIMAL,"---9.99");
  CTextField tar_precioE=new CTextField(Types.DECIMAL,"--9.99");
  CCheckBox pvl_confirE=new CCheckBox("S","N");
  CTextField pvl_comenE=new CTextField(Types.CHAR,"X",100);
  CTextField pvl_dtoE=new CTextField(Types.DECIMAL,"#9.99");
  CTextField pvl_prclprE=new CTextField(Types.DECIMAL,"---,--9.99");
  CTextField pvl_dtoproE=new CTextField(Types.DECIMAL,"#9.99");
  CLabel pvc_idL = new CLabel ("Id");
  CTextField pvc_idE = new CTextField (Types.DECIMAL,"###,##9");
  CComboBox pvl_tipoE=new CComboBox(); // Types.CHAR,"?",1
  CLabel cLabel9=new CLabel();
//  CComboBox alm_codiE = new CComboBox();
  CPanel Pprinc = new CPanel();
  CPanel Pcabe = new CPanel();
  CLabel cli_codiL = new CLabel();
  boolean swExterno;
  cliAvcPanel cli_codiE = new cliAvcPanel()
  {
    @Override
    public void afterFocusLost(boolean error)
    {
      cli_codiE_afterFocusLost(error);
    }
  };
  CButton BirAlbaran = new CButton(Iconos.getImageIcon("up"));
  CButton BponPrecio = new CButton(Iconos.getImageIcon("precio"));
  CLabel cli_poblE = new CLabel();
  CLabel cli_poblL = new CLabel();
  CLabel cLabel3 = new CLabel();
  
  CTextField eje_numeE = new CTextField(Types.DECIMAL,"###9");
  CTextField pvc_numeE = new CTextField(Types.DECIMAL,"#####9");
  CLabel pvc_fecpedL = new CLabel();
  CTextField pvc_fecpedE = new CTextField(Types.DATE,"dd-MM-yy");
  CTextField pvc_horpedE = new CTextField(Types.DECIMAL,"99.99");
  CLabel pvc_fecentL = new CLabel("Fec.Entrega");  
  CTextField pvc_fecentE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel pvc_fecpreL = new CLabel("Fec.Prepar.");
  CTextField pvc_fecpreE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CButton BirGrid= new CButton();
  CLabel cLabel7 = new CLabel();
  CComboBox pvc_confirE = new CComboBox();
  CLabel pvc_nupeclL = new CLabel();
  CTextField pvc_nupeclE = new CTextField(Types.CHAR,"X",12);
  CLabel rut_codiL = new CLabel("Ruta");
  CLinkBox rut_codiE= new CLinkBox();
  CTextField usu_nombE = new CTextField(Types.CHAR,"X",20);
  CLabel pvc_comenL = new CLabel();
  CLabel pvc_comalbL = new CLabel("Comentario Ruta");
    final int JT_PROD=0;
    final int JT_NOMBPRO=1;
    final int JT_CANTI=2;
    final int JT_TIPCAN=3;
    final int JT_PRECIO=4;
    final int JT_PRECON=5;
    final int JT_PRETAR=6;
    final int JT_COMEN=7;
    final int JT_PROV=8;
    final int JT_NOMPRV=9;
    final int JT_FECCAD=10;   
    final int JT_NL=11;
    final int JT_CANMOD=12;
  CGridEditable jt = new CGridEditable(13)
  {
    @Override
    public void cambiaColumna(int col, int colNueva, int row)
    {
      try
      {
        if (col == JT_PROD)
        {
          jt.setValor(pro_codiE.getNombArtCli(pro_codiE.getValorInt(),
                                              cli_codiE.getValorInt()), row, JT_NOMBPRO);
          jt.setValor(MantTarifa.getPrecTar(dtStat,pro_codiE.getValorInt(), cli_codiE.getValorInt(),
              cli_codiE.getLikeCliente().getInt("tar_codi"), 
            pvc_fecentE.getText()) , row, JT_PRETAR);
        }
        if (col == JT_PROV)
          jt.setValor(prv_codiE.getNombPrv(prv_codiE.getText()), row, JT_NOMPRV);
        if (col==JT_PRECIO)
        {
          if (pvl_precioE.hasCambio())
          {
            jt.setValor(pvl_precioE.getValorInt() != 0, JT_PRECON);
            pvl_precioE.resetCambio();
          }
        }
      }
      catch (SQLException k)
      {
        Error("Error al buscar Nombre Articulo", k);
      }
    }

    @Override
    public int cambiaLinea(int row, int col)
    {
      return cambiaLineaJT(row);
    }
    @Override
    public void afterCambiaLinea()
    {
      actAcumJT();
      pvl_precioE.resetCambio();
    }
  };
//  CButton Baceptar = new CButton("Aceptar",Iconos.getImageIcon("check"));
//  CButton Bcancelar = new CButton("Cancelar",Iconos.getImageIcon("cancel"));
  CPanel Ppie = new CPanel();
  CScrollPane pvc_comenS = new CScrollPane();
  CTextArea pvc_comenE = new CTextArea();
  CScrollPane pvc_comalbS = new CScrollPane();
  CTextArea pvc_comrepE = new CTextArea();
  CTabbedPane Ptab1 = new CTabbedPane();
  CPanel Pcomen = new CPanel();
  pstockAct pstock;
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CLabel cLabel10 = new CLabel();
  CComboBox opVerProd = new CComboBox();
  CCheckBox opPedidos = new CCheckBox();
//  CLabel avc_dtoppL = new CLabel();
  CLabel cLabel5 = new CLabel();
  CTextField nlE = new CTextField(Types.DECIMAL,"#9");
  CTextField cantE = new CTextField(Types.DECIMAL,"---9");
  CLabel cLabel13 = new CLabel();
  CCheckBox pvc_incfraE = new CCheckBox("Incluir Fra");
//  CLabel cLabel14 = new CLabel();
  CLabel cLabel15 = new CLabel();
  CComboBox avc_serieE = new CComboBox();
  CTextField avc_numeE = new CTextField(Types.DECIMAL, "#####9");
  CTextField avc_anoE = new CTextField(Types.DECIMAL, "###9");
  CLabel cLabel16 = new CLabel();
  CTextField pvc_verfecE = new CTextField(Types.DECIMAL,"##9");
  CCheckBox pvc_impresE = new CCheckBox("S","N");

  public pdpeve(EntornoUsuario eu, Principal p)
  {
    this(eu, p, new Hashtable());
  }

  public pdpeve(EntornoUsuario eu, Principal p, Hashtable<String, String> ht)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    try
    {
      ponParametros(ht);
      setTitulo("Mant. Pedidos de Ventas");
      setAcronimo("mapeve");
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

  public pdpeve(gnu.chu.anjelica.menu p, EntornoUsuario eu,Hashtable<String, String> ht)
  {
    EU = eu;
    vl = p.getLayeredPane();
    setTitulo("Mant. Pedidos de Ventas");
    eje = false;

    try
    {
      ponParametros(ht);
      jbInit();
    }
    catch (Exception e)
    {
       ErrorInit(e);   
    }
  }
 public static String getNombreClase()
  {
   return "gnu.chu.anjelica.ventas.pdpeve";
  }
  public boolean inTransation()
  {
      return (nav.getPulsado()==navegador.ADDNEW || nav.getPulsado()==navegador.EDIT || nav.getPulsado()==navegador.DELETE);
  }
  
  private void ponParametros(Hashtable<String, String> ht)
  {
      if (ht == null)
          return;
      if (ht.get("admin") != null)
         P_ADMIN = Boolean.parseBoolean(ht.get("admin"));
       if (ht.get("verPrecios") != null)
         P_VERPRECIOS = Boolean.parseBoolean(ht.get("verPrecios"));
  }
  private void jbInit() throws Exception
  {
    iniciarFrame();
    this.setSize(new Dimension(779, 530));
    this.setMinimumSize(new Dimension(769, 530));
    this.setVersion("2017-07-31"+ (P_ADMIN?" (Admin) ":""));

    Pprinc.setLayout(gridBagLayout1);
    strSql = "SELECT * FROM pedvenc WHERE emp_codi = " + EU.em_cod +
        " ORDER BY eje_nume,pvc_nume ";

    statusBar = new StatusBar(this);
    nav = new navegador(this, dtCons, false, navegador.NORMAL);
    conecta();
    iniciar(this);
    pstock = new pstockAct(dtCon1,dtStat,this,EU.em_cod)
    {
            @Override
      protected void insertarDesgProd(int proCodi,double precio, int prvCodi, String feccad)
      {
        int row=jt.getSelectedRow();
        try
        {
          if (! jt.isEnabled())
              return;
          ArrayList v = new ArrayList();
          
          jt.setEnabled(false);
          pro_codiE.getFieldBotonCons().setEnabled(false);
          if (jt.getValorInt(JT_PROD) == proCodi && jt.getValorInt(JT_PROV) == prvCodi
              && jt.getValString(JT_FECCAD).equals(feccad))
            jt.setValor("" + (jt.getValorInt(JT_CANTI) + 1),JT_CANTI);
          else
          {
            v.add( proCodi); // 0
            v.add(pro_codiE.getNombArt(proCodi)); // 1           
            v.add(1); // 5
            v.add(pvl_tipoE.getText(pro_codiE.getTipoUnidVenta())); // 6 Tipo Unidad
            v.add(0); // 7
            v.add(0); // 8
            v.add(precio); // Pr.Tarifa
            v.add(""); // 9
            v.add(prvCodi); // 2
            v.add(prv_codiE.getNombPrv("" + prvCodi)); //3
            v.add(feccad); // 4
            v.add(0); // 10 Numero Linea.
            v.add(0); // Cantidad Modif.
            if (jt.getValorInt(JT_PROD) == 0)
              jt.setLinea(v);
            else
            {
              jt.addLinea(v);
              row=jt.getRowCount()-1;
            }
          }
          pro_codiE.setValorInt(proCodi);
          pvl_cantiE.setValorDec(pvl_cantiE.getValorDec() + 1);
          prv_codiE.setValorInt(prvCodi);
          pvl_feccadE.setText(feccad);
          if (! jt.isEnabled())
            jt.setEnabled(true);
          pro_codiE.getFieldBotonCons().setEnabled(true);
          if (precio != 0)
          {
            pvl_precioE.setValorDec(precio);
            pvl_confirE.setSelected(true);
            jt.requestFocus(row, JT_COMEN);
          }
          else
            jt.requestFocus(row, JT_PRECIO);

        }
        catch (SQLException k)
        {
          Error("Error al pasar datos al grid", k);
        }
      }
            @Override
      public boolean isEdit()
      {
        return jt.isEnabled();
      }
    };
    pstock.setVerProductos(""+pstockAct.VER_ULTVENTAS);
    pstock.setVerPrecios(P_VERPRECIOS);
    Bimpri.setMargin(new Insets(0, 0, 0, 0));
    Bimpri.setPreferredSize(new Dimension(24, 24));
    Bimpri.setMaximumSize(new Dimension(24, 24));
    Bimpri.setMinimumSize(new Dimension(24, 24));

    Bimpri.setToolTipText("Imprimir Pedido");

    BbusProd.setMargin(new Insets(0, 0, 0, 0));
    BbusProd.setPreferredSize(new Dimension(24, 24));
    BbusProd.setMaximumSize(new Dimension(24, 24));
    BbusProd.setMinimumSize(new Dimension(24, 24));
    BbusProd.setToolTipText("Buscar datos de un Producto");

    opPedidos.setSelected(false);
    Baceptar.setText("Aceptar");
    pvc_impresE.setMargin(new Insets(0, 0, 0, 0));
    pvc_impresE.setText("Impreso");
    pvc_impresE.setBounds(new Rectangle(438, 4, 70, 16));
    statusBar.add(Bimpri, new GridBagConstraints(9, 0, 1, 2, 0.0, 0.0
                                                   , GridBagConstraints.EAST,
                                                   GridBagConstraints.VERTICAL,
                                                   new Insets(0, 5, 0, 0), 0, 0));
      statusBar.add(BbusProd, new GridBagConstraints(8, 0, 1, 2, 0.0, 0.0
                                                     , GridBagConstraints.EAST,
                                                     GridBagConstraints.VERTICAL,
                                                     new Insets(0, 5, 0, 0), 0, 0));

    pvc_verfecE.setEnabled(false);
    pvc_verfecE.setValorInt(pstock.getDiasVer_Cliente());
    pstock.setEmpresa(EU.em_cod);
    Ptab1.setPreferredSize(new Dimension(100,320));
    Ptab1.setMinimumSize(new Dimension(100,320));
    Ptab1.setMaximumSize(new Dimension(100,320));
    Pcomen.setLayout(null);
    Ptab1.addTab("Pedido",pstock);
    Ptab1.addTab("Historico",jtHist);
    Ptab1.addTab("Comentario",Pcomen);
//    pstock.setPreferredSize(new Dimension(100,320));
//    pstock.setMinimumSize(new Dimension(100,320));
//    pstock.setMaximumSize(new Dimension(100,320));
    Pcabe.setBorder(BorderFactory.createRaisedBevelBorder());
    Pcabe.setMaximumSize(new Dimension(668, 75));
    Pcabe.setMinimumSize(new Dimension(668, 75));
    Pcabe.setPreferredSize(new Dimension(668, 75));
    Pcabe.setLayout(null);
    cli_poblE.setBackground(Color.orange);
    cli_poblE.setForeground(Color.black);
   
    cLabel3.setText("N. Pedido");
    cLabel3.setBounds(new Rectangle(3, 20, 57, 16));
    pvc_fecpedL.setText("Fec. Ped.");
    
    
    
    cLabel7.setText("Conf");
    cLabel7.setBounds(new Rectangle(474, 20, 33, 16));
    pvc_nupeclL.setText("N.Ped. Cliente");
   
    cLabel9.setText("Usuario");
    cLabel9.setBounds(new Rectangle(182, 4, 49, 16));
    pvc_comenL.setText("Comentario");
    pvc_comenL.setBounds(new Rectangle(3, 4, 120, 16));    
    pvc_comenS.setBounds(new Rectangle(3, 20, 390, 65));
    pvc_comalbL.setBounds(new Rectangle(3, 90, 120, 16));    
    pvc_comalbS.setBounds(new Rectangle(3, 110, 390, 65));
    
    pvc_estadE.addItem("Pendiente","P");
    pvc_estadE.addItem("Preparado","L");
    pvc_estadE.addItem("Cancelado","C");
    
    pvc_estadL.setBounds(new Rectangle(3, 54, 60, 17));
    pvc_estadE.setBounds(new Rectangle(65, 54, 138, 17));
    pvc_deposE.addItem(pdalbara.DEPOSITOS);
    pvc_deposL.setBounds(new Rectangle(210, 54, 60, 17));
    pvc_deposE.setBounds(new Rectangle(275, 54, 88, 17));
    opAvisoStock.setBounds(new Rectangle(370, 54, 88, 17));
    cli_codiL.setText("Cliente");
    cli_codiL.setBounds(new Rectangle(3, 3, 43, 16));
    Ppie.setBorder(BorderFactory.createRaisedBevelBorder());
    Ppie.setMaximumSize(new Dimension(469, 24));
    Ppie.setMinimumSize(new Dimension(469, 24));
    Ppie.setPreferredSize(new Dimension(469, 24));
    Ppie.setLayout(null);
    pstock.setBorder(BorderFactory.createLineBorder(Color.black));
    
    conf_jt();
    BirAlbaran.setToolTipText("Busca Albaranes de este cliente");
    BirAlbaran.setFocusable(false);
    Baceptar.setMaximumSize(new Dimension(125, 20));
    Baceptar.setMinimumSize(new Dimension(125, 20));
    Baceptar.setPreferredSize(new Dimension(125, 20));
    Baceptar.setFocusPainted(true);
    Baceptar.setMargin(new Insets(0, 0, 0, 0));
    Bcancelar.setMaximumSize(new Dimension(125, 20));
    Bcancelar.setMinimumSize(new Dimension(125, 20));
    Bcancelar.setPreferredSize(new Dimension(125, 20));
    Bcancelar.setMargin(new Insets(0, 0, 0, 0));
    usu_nombE.setBounds(new Rectangle(226, 4, 81, 16));
    BponPrecio.setToolTipText("Poner precio tarifa");
    BponPrecio.setBounds(new Rectangle(2, 4, 18, 18));
    pvc_fecpedL.setBounds(new Rectangle(25, 4, 57, 16));
    pvc_fecpedE.setBounds(new Rectangle(87, 4, 50, 16));
    pvc_horpedE.setBounds(new Rectangle(146, 4, 35, 16));
    pvc_fecentL.setBounds(new Rectangle(147, 20, 67, 16));
    pvc_fecentE.setBounds(new Rectangle(218, 20, 75, 16));
    pvc_fecpreL.setBounds(new Rectangle(300, 20, 67, 16));
    pvc_fecpreE.setBounds(new Rectangle(370, 20, 75, 16));
    BirGrid.setBounds(new Rectangle(295, 20, 1, 1));
    pvc_nupeclL.setBounds(new Rectangle(2, 37, 77, 16));
    pvc_nupeclE.setBounds(new Rectangle(80, 37, 105, 16));
    rut_codiE.setAncTexto(30);
    rut_codiE.setMayusculas(true);
    rut_codiE.setFormato(Types.CHAR,"X",2);
    rut_codiL.setBounds(new Rectangle(190, 37, 50, 16));
    rut_codiE.setBounds(new Rectangle(242, 37, 200, 16));

    
    pvc_confirE.setBounds(new Rectangle(511, 20, 54, 16));
    pvc_numeE.setBounds(new Rectangle(92, 20, 50, 16));
    eje_numeE.setBounds(new Rectangle(57, 20, 33, 16));
   
    cli_codiE.setBounds(new Rectangle(45, 3, 370, 16));
    BirAlbaran.setBounds(new Rectangle(415,3,18,18));
    cLabel10.setText("Ver Prod.");
    cli_poblL.setText("Pobl.");
    cli_poblL.setBounds(new Rectangle(440, 3, 34, 16));
    cli_poblE.setOpaque(true);
    cli_poblE.setBounds(new Rectangle(474, 3, 274, 16));

    cLabel10.setBounds(new Rectangle(468, 56, 63, 16));

    opVerProd.setBounds(new Rectangle(518, 56, 112, 16));
    opPedidos.setHorizontalAlignment(SwingConstants.RIGHT);
    opPedidos.setHorizontalTextPosition(SwingConstants.LEFT);
    opPedidos.setMargin(new Insets(0, 0, 0, 0));
    opPedidos.setText("Incluir Pedidos");
    opPedidos.setToolTipText("Incluir pedidos en calculo stock actual");
    opPedidos.setBounds(new Rectangle(630, 37, 119, 17));
    cLabel5.setText("NL");
    cLabel5.setBounds(new Rectangle(310, 4, 21, 16));
    nlE.setToolTipText("Numero Lineas del pedido");
    nlE.setBounds(new Rectangle(329, 4, 28, 16));
    cantE.setBounds(new Rectangle(394, 4, 43, 16));
    cantE.setToolTipText("Cantidad de piezas del pedido");
    cLabel13.setBounds(new Rectangle(365, 4, 33, 16));
    cLabel13.setText("Cant");
//    cLabel14.setText("Almacen");
    pvc_incfraE.setBounds(new Rectangle(448, 37, 71, 16));
    pvc_idL.setBounds(new Rectangle(535, 37, 20, 16));
    pvc_idE.setBounds(new Rectangle(550, 37, 80, 16));
//    alm_codiE.setBounds(new Rectangle(522, 37, 116, 16));
    cLabel15.setText("Albaran");
    cLabel15.setBounds(new Rectangle(569, 20, 50, 16));
    avc_serieE.setBounds(new Rectangle(652, 20, 40, 16));
    avc_numeE.setBounds(new Rectangle(694, 20, 65, 16));
    avc_anoE.setBounds(new Rectangle(613, 20, 38, 16));
    cLabel16.setRequestFocusEnabled(true);
    cLabel16.setText("Antiguedad");
    cLabel16.setBounds(new Rectangle(652, 56, 71, 16));
    pvc_verfecE.setToolTipText("Antiguedad en dias");
    pvc_verfecE.setBounds(new Rectangle(724, 56, 36, 16));
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
    Pprinc.add(Pcabe,                new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(1, 2, 0, 0), 0, 0));
    Pcabe.add(cli_poblE, null);
    Pcabe.add(cli_codiE, null);
    Pcabe.add(BirAlbaran, null);
    Pcabe.add(cli_poblL, null);
    Pcabe.add(cli_codiL, null);
    Pcabe.add(avc_serieE, null);
    Pcabe.add(avc_numeE, null);
    Pcabe.add(cLabel3, null);
    Pcabe.add(eje_numeE, null);
    Pcabe.add(pvc_numeE, null);
    Pcabe.add(pvc_nupeclE, null);
    Pcabe.add(pvc_fecentE, null);
    Pcabe.add(BirGrid, null);
    Pcabe.add(cLabel7, null);
    Pcabe.add(pvc_confirE, null);
    Pcabe.add(cLabel15, null);
    Pcabe.add(avc_anoE, null);
    Pcabe.add(opVerProd, null);
    Pcabe.add(pvc_estadL,null);
    Pcabe.add(pvc_estadE,null);
    Pcabe.add(pvc_deposL,null);
    Pcabe.add(pvc_deposE,null);
    Pcabe.add(opAvisoStock,null);
    Pcabe.add(cLabel10, null);
    Pcabe.add(pvc_incfraE, null);
    Pcabe.add(pvc_idL, null);
    Pcabe.add(pvc_idE, null);
//    Pcabe.add(alm_codiE, null);
//    Pcabe.add(cLabel14, null);
    Pcomen.add(pvc_comenL, null);
    Pcomen.add(pvc_comenS, null);
    Pcomen.add(pvc_comalbL, null);
    Pcomen.add(pvc_comalbS, null);
    Pcabe.add(pvc_nupeclL, null);
    Pcabe.add(pvc_fecentL, null);
    Pcabe.add(pvc_fecpreL, null);
    Pcabe.add(pvc_fecpreE, null);
    Pcabe.add(rut_codiL,null);
    Pcabe.add(rut_codiE,null);

    Pcabe.add(opPedidos, null);
    Pcabe.add(cLabel16, null);
    Pcabe.add(pvc_verfecE, null);
    pvc_comenS.getViewport().add(pvc_comenE, null);
    pvc_comalbS.getViewport().add(pvc_comrepE, null);
    cantE.setEnabled(false);        
    nlE.setEnabled(false);    
    pvc_impresE.setEnabled(false);

    Ppie.add(BponPrecio, null);
    Ppie.add(pvc_fecpedL, null);
    Ppie.add(pvc_fecpedE, null);
    Ppie.add(pvc_horpedE, null);
    Ppie.add(cLabel9, null);
    Ppie.add(usu_nombE, null);
    Ppie.add(cLabel13, null);
    Ppie.add(cantE, null);
    Ppie.add(cLabel5, null);
    Ppie.add(nlE, null);
    Ppie.add(pvc_impresE, null);
    Pprinc.add(Baceptar,                  new GridBagConstraints(1, 3, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 2), 0, 0));
    Pprinc.add(Bcancelar,        new GridBagConstraints(1, 3, 3, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    Pprinc.add(Ptab1,               new GridBagConstraints(0, 1, 4, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    Pprinc.add(jt,            new GridBagConstraints(0, 2, 4, 1, 2.0, 2.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

    Pprinc.add(Ppie,        new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 40, 0));
  }

  private void conf_jt() throws Exception
  {
   
    ArrayList v=new ArrayList();
    v.add("Prod."); // 0
    v.add("Desc. Prod."); // 1  
    v.add("Cant"); // 2
    v.add("Tipo");// 3
    v.add("Precio"); // 4    
    v.add("Conf"); // 5 Confirmado Precio ?
    v.add("Pr.Tar."); // 6
    v.add("Comentario"); //7 Comentario
    v.add("Prv"); // 8
    v.add("Nombre Prv"); // 9
    v.add("Fec.Cad"); // 10
    v.add("NL.");// 11
    v.add("C.Prep");// 12 -- Cantidad preparada en sala
    jt.setCabecera(v);
    jt.setMaximumSize(new Dimension(477, 250));
    jt.setMinimumSize(new Dimension(477, 250));
    jt.setPreferredSize(new Dimension(477, 250));
    jt.setPuntoDeScroll(50);
    jt.setAnchoColumna(new int[]{50,160,45,50,45,40,45,150,50,150,80,30,40});    
    jt.setAlinearColumna(new int[]{2, 0, 2, 0, 2, 1, 2,  0, 2,  0, 1, 2, 2});
    

    ArrayList v1=new ArrayList();
    pvl_tipoE.addItem("Kilos","K");
    pvl_tipoE.addItem("Piezas","P");
    pvl_tipoE.addItem("Cajas","C");
    
//    pvl_tipoE.setText("K");
//    pvl_tipoE.setMayusc(true);
//    pvl_tipoE.setCaracterAceptar("PCK");
    pvl_tipoE.setToolTipText("Tipo Cantidad: (K)ilos, (P)iezas, (C)ajas");
    pro_nombE.setEnabled(false);
    pro_codiE.setProNomb(null);
    prv_codiE.setCampoNombre(null);
    pvl_numlinE.setEnabled(false);
    prv_nombE.setEnabled(false);
    pvm_cantiE.setEnabled(false);
    tar_precioE.setEnabled(false);
    v1.add(pro_codiE.getFieldProCodi()); // 0
    v1.add(pro_nombE); // 1  
    v1.add(pvl_cantiE); // 2
    v1.add(pvl_tipoE);//3
    v1.add(pvl_precioE); // 4
    v1.add(pvl_confirE); // 5
    v1.add(tar_precioE); // 6
    v1.add(pvl_comenE); // 7
    v1.add(prv_codiE.prv_codiE); //8
    v1.add(prv_nombE); // 9
    v1.add(pvl_feccadE); // 10
    v1.add(pvl_numlinE); // 11
    v1.add(pvm_cantiE); // Cantidad Preparada en sala
    jt.setCampos(v1);
    jt.setFormatoCampos();
    pro_codiE.getFieldBotonCons().setText("Ayuda Producto");
    jt.getPanelBotones().add(pro_codiE.getFieldBotonCons());
  }

  @Override
  public void afterConecta() throws SQLException, java.text.ParseException
  {
    cli_codiE.iniciar(dtStat, this, vl, EU);
    cli_codiE.iniciar(jf);
    cli_codiE.setAceptaNulo(false);
    cli_codiE.setCampoReparto(true);
    pdconfig.llenaDiscr(dtStat, rut_codiE, pdconfig.D_RUTAS, EU.em_cod);
    pro_codiE.iniciar(dtStat, this, vl, EU);
    pro_codiE.setAceptaInactivo(false);
    prv_codiE.iniciar(dtStat,this,vl,EU);
    prv_codiE.setAceptaNulo(true);
//  pdalmace.llenaCombo(alm_codiE, dtCon1); 
//    s = "SELECT alm_codi,alm_nomb FROM v_almacen ORDER BY alm_nomb";
//    dtCon1.select(s);
//    alm_codiE.addItem(dtCon1);
    ALMACEN = 1;

    if (pdconfig.getConfiguracion(EU.em_cod,dtStat))
    {
      ALMACEN = dtStat.getInt("cfg_almven");
//      alm_codiE.setValor("" + ALMACEN); // Almacen de VENTAS
    }

    pvc_confirE.addItem("Si", "S");
    pvc_confirE.addItem("NO", "N");
    pvc_confirE.addItem("Cancelado", "C");
    pvc_confirE.setPreferredSize(new Dimension(150,150));
    opVerProd.addItem("Ult.Ventas", ""+pstockAct.VER_ULTVENTAS);
    opVerProd.addItem("Con Stock", ""+pstockAct.VER_CONSTOCK);
    opVerProd.addItem("TODOS", ""+pstockAct.VER_TODOS );
    avc_serieE.addItem("A", "A");
    avc_serieE.addItem("B", "B");
    avc_serieE.addItem("C", "C");
    avc_serieE.addItem("D", "D");
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
  void actGridHist(int empCodi,int pvcAno, int pvcNume) throws SQLException
  {
            jtHist.setEnabled(false);
            jtHist.removeAllDatos();
            s="SELECT * FROM hispedvenc WHERE emp_codi = "+empCodi+
              " and eje_nume = "+pvcAno+
              " and pvc_nume = "+pvcNume+
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
  @Override
  public void iniciarVentana() throws Exception
  {
    cli_codiE.getPopMenu().add(MbusCliente);
    Pprinc.setButton(KeyEvent.VK_F2, BirGrid);
    pstock.setPedidos(opPedidos.isSelected());
    pro_codiE.getFieldBotonCons().setEnabled(false);
    dtHist=new DatosTabla(ct);
    confGridHist();
    pvc_idE.setColumnaAlias("pvc_id");
    eje_numeE.setColumnaAlias("eje_nume");
    cli_codiE.setColumnaAlias("cli_codi");
    pvc_numeE.setColumnaAlias("pvc_nume");
    pvc_nupeclE.setColumnaAlias("pvc_nupecl");
    pvc_fecentE.setColumnaAlias("pvc_fecent");
    rut_codiE.setColumnaAlias("rut_codi");
    pvc_fecpreE.setColumnaAlias("pvc_fecpre");
    pvc_confirE.setColumnaAlias("pvc_confir");
//    alm_codiE.setColumnaAlias("alm_codi");
    avc_anoE.setColumnaAlias("avc_ano");
    avc_serieE.setColumnaAlias("avc_serie");
    avc_numeE.setColumnaAlias("avc_nume");
    Pcabe.setDefButton(Baceptar);
    jt.setButton(KeyEvent.VK_F5,BponPrecio);
    jt.setDefButton(Baceptar);
    boolean verDatDif=true;
    if (getLabelEstado()!=null)
    {
        if (getLabelEstado().getText().contains("*DIRECTO*"))
            verDatDif=false;        
    }
    if (verDatDif)
    {
       SwingUtilities.invokeLater(new Thread()
      {
        @Override
        public void run()
        {
            try{  finalIniciar();  } catch (Exception k)
            {
                Error("Error al iniciar Ventana",k);
            }
        }
      });
    }
    else
       finalIniciar();
    
  }
  
  void finalIniciar() throws Exception
  {
    verDatos();
    pstock.verFamilias();
    activarEventos();  
  }
   private void cambiaLineaHist(int rowid)
   {
    hisRowid=rowid;
    if (hisRowid==0)
    {
        verDatos();
        return;
    }
    tablaCab="hispedvenc";
   
    vistaPed="v_hispedven";    
    
    try {
         s="SELECT * FROM "+tablaCab+" WHERE his_rowid = "+hisRowid;
         
        dtHist.select(s);
        verDatos(dtHist);
    } catch (SQLException k)
    {
        Error("Error al ver datos de historicos",k);
    }
 }
   void buscaCliente()
  {
       if (cli_codiE.isNull() || nav.isEdicion() )
           return;
        int cliCodi=cli_codiE.getValorInt();
        PADQuery();
        cli_codiE.setValorInt(cliCodi);
        ej_query();
  }
  void activarEventos()
  {
     MbusCliente.addActionListener(new java.awt.event.ActionListener()
      {
          @Override
          public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscaCliente();
          }
      });
   BponPrecio.addActionListener(new ActionListener()
   {
     @Override
     public void actionPerformed(ActionEvent e)
     {
        if ( ! jt.isEnabled())
          return;       
        if (jt.getValorDec(JT_PRETAR)!=0)
        {
            jt.setValor(jt.getValorDec(JT_PRETAR),JT_PRECIO);
            pvl_precioE.setValorDec(jt.getValorDec(JT_PRETAR));
        }
        jt.requestFocus(JT_PRECIO);
      }
    });
   
     pvl_precioE.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyPressed(KeyEvent e)
      {
        if (e.getKeyCode() == KeyEvent.VK_F3)
          consPrecios();
      }
    });
    BirGrid.addFocusListener(new FocusAdapter()
    {
      @Override
      public void focusGained(FocusEvent e)
      {
              irGrid();
      }    
    });
    jt.addMouseListener(new MouseAdapter()
    {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (cli_codiE.getEnabled() && ! jt.isEnabled() && e.getClickCount()>1)
                irGrid();
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
    BirAlbaran.addActionListener(new ActionListener()
   {
     @Override
     public void actionPerformed(ActionEvent e)
     {
        if ( cli_codiE.getValorInt() == 0)
          return;       
        verAlbaranesCliente(cli_codiE.getValorInt());                          
      }
    });
    pvl_tipoE.addKeyListener(new KeyAdapter()
    {
        @Override
        public void keyPressed(KeyEvent ke) {
            s=(""+ke.getKeyChar()).toUpperCase();
            
            if ( pvl_tipoE.getValores().contains(s))
            {
              pvl_tipoE.setValor(s);
              jt.requestFocusLater(jt.getSelectedRow(), jt.getSelectedColumn()+1 );
            }
        }
    });
    pvc_fecentE.addFocusListener(new FocusAdapter()
    {
      @Override
      public void focusLost(FocusEvent e)
      {
        if (! jt.isEnabled())
          return;
        if (pvc_fecentE.hasCambio())
        {
          pvc_fecentE.resetCambio();
          pstock.setFechaPedido(pvc_fecentE.getText());
          try {
            pstock.verFamilias();
          } catch (Exception k)
          {
            Error("Error al Ver Familias",k);
          }
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
   BbusProd.addActionListener(new ActionListener()
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      BbusProd_actionPerformed();
    }
  });

    opPedidos.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        if (! jt.isEnabled())
          return;
        try  {
          pstock.setPedidos(opPedidos.isSelected());
          pstock.verFamilias();
        } catch (Exception k)
        {
          Error("Error al ver Familias",k);
        }
      }
    });
    opVerProd.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        try
        {
//          if (! jt.isEnabled())
//            return;
          if (nav.pulsado==navegador.QUERY)
            return;
          pstock.setVerProductos(opVerProd.getValor());
          if (opVerProd.getValor().equals(""+pstockAct.VER_ULTVENTAS))
          { // Ultimas Ventas
            pvc_verfecE.setEnabled(true);
            if (cli_codiE.getValorInt()==0)
              return;
            pstock.setCliente(cli_codiE.getValorInt());
          }
          else
            pvc_verfecE.setEnabled(false);
          pstock.verFamilias();
        }
        catch (Exception k)
        {
          Error("Error al ver Familias", k);
        }

      }
    });
    pvc_verfecE.addFocusListener(new FocusAdapter()
    {
      @Override
      public void focusLost(FocusEvent e)
      {
        try
        {
          if (pvc_verfecE.hasCambio())
          {
            pvc_verfecE.resetCambio();
            pstock.setDiasVer_Cliente(pvc_verfecE.getValorInt());
            if (opVerProd.getValor().equals(""+pstockAct.VER_ULTVENTAS) && pstock.getCliente()!=0)
              pstock.verFamilias();
          }
        }  catch (Exception k)
        {
          Error("Error al ver Familias", k);
        }

      }
    });
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
      this.getLayeredPane().add(ayVePr,1);
//      vl.add(ayVePr);
      ayVePr.setLocation(25, 25);
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
  
  void ej_consPro()
  {
    if (ayVePr.precio != 0 && pvl_precioE.isEditable())
    {
      jt.setValor("" + ayVePr.precio, JT_PRECIO);
      pvl_precioE.setValorDec(ayVePr.precio);
    }
    ayVePr.setVisible(false);
    this.setEnabled(true);
    this.toFront();
    setFoco(null);
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
  void consultaPrecios()
  {
    try
    {
      this.setFoco(ayVePr);
      this.setEnabled(false);
      Thread.currentThread().setPriority(Thread.MAX_PRIORITY - 1);
      ayVePr.setVisible(true);
      String lastMensaje=statusBar.getText();
      mensaje("Buscando datos.. de Ultimas ventas");
      ayVePr.cargaDatos(ct, cli_codiE.getText(), cli_codiE.getTextNomb(),
                        jt.getValString(JT_PROD), jt.getValString(JT_NOMBPRO), EU);
      mensaje(lastMensaje);
      mensajeErr("Datos de Ultimas ventas... encontrados");
    }
    catch (Exception ex)
    {
      fatalError("Error al Cargar datos de productos", ex);
    }

  }
    private void verAlbaranesCliente(final int cliCodi) {
        if (jf == null )
            return;
        msgEspere("Buscando Albaranes del Cliente");
        new miThread("")
        {
            @Override
            public void run() {
                javax.swing.SwingUtilities.invokeLater(new Thread()
                {
                    @Override
                    public void run() {
                        ejecutable prog;
                        if ((prog = jf.gestor.getProceso(pdalbara.getNombreClase())) == null)
                            return;
                        pdalbara cm = (pdalbara) prog;
                        if (cm.inTransation())
                        {
                            msgBox("Mantenimiento Albaranes de Ventas ocupado. No se puede realizar la busqueda");
                            return;
                        }
                        cm.PADQuery();
                        cm.setCliente(cliCodi);

                        cm.ej_query();
                        cm.goLast();
                        jf.gestor.ir(cm);
                        resetMsgEspere();
                    }
                });

            }
        };
    }
  @Override
  public void PADPrimero()  {    verDatos();  }
  
  @Override
  public void PADAnterior()  {    verDatos();}
  
  @Override
  public void PADSiguiente()  {    verDatos();}

  @Override
  public void PADUltimo()  {    verDatos();}

  void setPanelQuery(boolean query)
  {
    cli_codiE.setQuery(query);
    pvc_idE.setQuery(query);
    pvc_nupeclE.setQuery(query);
    pvc_fecentE.setQuery(query);
    rut_codiE.setQuery(query);
    pvc_fecpreE.setQuery(query);
    pvc_confirE.setQuery(query);
    pvc_comenE.setQuery(query);
    eje_numeE.setQuery(query);
    pvc_numeE.setQuery(query);
   
    avc_anoE.setQuery(query);
    avc_serieE.setQuery(query);
    avc_numeE.setQuery(query);

  }
  
  @Override
  public void PADQuery()
  {
    setPanelQuery(true);


    cli_codiE.resetTexto();
    cli_poblE.setText("");
    pvc_nupeclE.resetTexto();
    pvc_fecpreE.resetTexto();
    pvc_fecentE.resetTexto();
    rut_codiE.resetTexto();
    pvc_confirE.resetTexto();
    pvc_comenE.resetTexto();
    pvc_idE.resetTexto();
    eje_numeE.resetTexto();
    pvc_numeE.resetTexto();
  
    avc_anoE.resetTexto();
    avc_serieE.resetTexto();
    avc_numeE.resetTexto();


    activar(navegador.QUERY,true);
    cli_codiE.requestFocus();
    mensaje("Introduzca filtro de Consulta ...");
  }

  @Override
  public void ej_query1()
  {
    
    try
    {
     Component c = Pcabe.getErrorConf();
     if (c != null)
     {
       mensajeErr("Condiciones de Busqueda NO validas");
       c.requestFocus();
       return;
     }
     nav.pulsado=navegador.NINGUNO;
     ArrayList v = new ArrayList();

     v.add(cli_codiE.getStrQuery());
      v.add(pvc_idE.getStrQuery());
     v.add(eje_numeE.getStrQuery());
     v.add(pvc_numeE.getStrQuery());
     v.add(pvc_fecentE.getStrQuery());
     v.add(rut_codiE.getStrQuery());
     v.add(pvc_fecpreE.getStrQuery());
     v.add(cli_codiE.getStrQuery());
     v.add(pvc_confirE.getStrQuery());
     v.add(avc_anoE.getStrQuery());
     v.add(avc_serieE.getStrQuery());
     v.add(avc_numeE.getStrQuery());

     s = "SELECT * FROM pedvenc " ;

     s = creaWhere(s, v, true);
     s += " ORDER BY emp_codi,eje_nume,pvc_nume ";
     this.setEnabled(false);
     mensaje("Espere, por favor ... buscando datos");
     setPanelQuery(false);
     if (!dtCon1.select(s))
     {
       msgBox("No encontrados Pedidos con estos criterios");
       rgSelect();
       activaTodo();
       this.setEnabled(true);
       return;
     }
     strSql = s;
     activaTodo();
     this.setEnabled(true);
     rgSelect();
     mensaje("");
     mensajeErr("Nuevas Condiciones ... Establecidas");
     if (opVerProd.getValor().equals(""+pstockAct.VER_ULTVENTAS))
       cli_codiE_afterFocusLost(false);
   }
   catch (SQLException k)
   {
     Error("Error al buscar datos", k);
   }

   nav.pulsado = navegador.NINGUNO;

  }
  
  @Override
  public void canc_query()
  {
    setPanelQuery(false);
    activaTodo();
    verDatos();
    mensajeErr("Introducion Filtro de busqueda ... CANCELADO");
    mensaje("");
    nav.pulsado=navegador.NINGUNO;
  }
  
  @Override
  public void PADEdit()
  {
    try {
      alArtStock.clear();
      if (hisRowid!=0)
      {
        msgBox("Viendo Pedido historico ... IMPOSIBLE MODIFICAR");
        nav.pulsado = navegador.NINGUNO;
        activaTodo();
        return;
      }
      activar(navegador.EDIT, true);
      verDatos();
      if (avc_anoE.getValorInt()==9999)
      {
          msgBox("Albaran se BORRO. Imposible editar");
          nav.pulsado = navegador.NINGUNO;
          activaTodo();
          return;
      }
      if ( avc_anoE.getValorDec() > 0 )
      {
        if (P_ADMIN || EU.usuario.equals(usu_nombE.getText()))
        {
            int ret=mensajes.mensajeYesNo("Pedido ya tiene albaran.\n ¿Volver a Abrir?");
            if (ret!=mensajes.YES)
            {
              nav.pulsado = navegador.NINGUNO;
              activaTodo();
              return; 
            }
            if (jf != null)
            {
                jf.ht.clear();
                jf.ht.put("%p", eje_numeE.getValorInt()+"-"+pvc_numeE.getText());
                jf.ht.put("%a", avc_anoE.getValorInt()+avc_serieE.getText()+avc_numeE.getValorInt());
                jf.guardaMens("P1", jf.ht);
             }
        }        
        else
        {
            msgBox("Pedido YA TIENE albaran ... IMPOSIBLE MODIFICAR");
            nav.pulsado = navegador.NINGUNO;
            activaTodo();
            return;
        }
      }
      if (!setBloqueo(dtAdd, "pedvenc",
                      eje_numeE.getValorInt() + "|" + EU.em_cod +
                      "|" + pvc_numeE.getValorInt()))
      {
        msgBox(msgBloqueo);
        activaTodo();
        return;
      }
      // Busco Lineas con Numero inferior a 0 (Cuelgues anteriores)
      s = " select *  from pedvenl where pvl_numlin<0  and emp_codi = " + EU.em_cod  +
        " and eje_nume= " + eje_numeE.getValorInt() +
        " and pvc_nume = " + pvc_numeE.getValorInt()+ " order by pvl_numlin ";
      if (dtCon1.select(s))
      {
          int nl=100;          
          do
          {
              s="update pedvenl set pvl_numlin="+nl+ " where  emp_codi = " + EU.em_cod +
                " and eje_nume= " + eje_numeE.getValorInt() +
                " and pvc_nume = " + pvc_numeE.getValorInt()+
                " and pvl_numlin = "+dtCon1.getInt("pvl_numlin");
              dtAdd.executeUpdate(s);
              nl++;
          } while (dtCon1.next());
          dtAdd.commit();
          verDatos();
      }
      pvc_verfecE.setEnabled(opVerProd.getValor().equals(""+pstockAct.VER_ULTVENTAS));
      pvl_precioE.resetCambio();
      pvc_fecentE.resetCambio();
      rut_codiE.resetCambio();
      pvc_fecpreE.resetCambio();
//      pvc_estadE.setValor("P");
      jt.setEnabled(true);
      pro_codiE.getFieldBotonCons().setEnabled(true);
      cli_codiE_afterFocusLost(false);
      mensaje("Editando Pedido ...");
      jt.requestFocusInicio();
    } catch (SQLException | UnknownHostException k)
    {
      Error("Error al editar registro",k);
    }
  }

  @Override
 public boolean checkEdit()
 {
   return checkAddNew();
 }
 public void copiaPedidoNuevo(DatosTabla dt, DatosTabla dtUpd,String coment,String usuario, 
          int avcAno, int empCodi,int pvcNume) throws SQLException
  {
      String s1="select max(his_rowid) as his_rowid from hispedvenc ";
      dt.select(s1);
      int rowid=dt.getInt("his_rowid",true);
      rowid++;
      String condAlb=" emp_codi = "+empCodi+
         " and eje_nume = "+avcAno+
         " and pvc_nume = "+pvcNume;
      s1 = "select * from pedvenc WHERE "+condAlb;
      if (dt.select(s1))
      {
         dtUpd.addNew("hispedvenc",false);
         dt.copy(dtUpd,usuario, coment,rowid);    
      }
    
      s1 = "select * from pedvenl WHERE "+condAlb;
      if (dt.select(s1))
      {
         dtUpd.addNew("hispedvenl");
         dt.copy(dtUpd,usuario, coment,rowid);    
      }
    
      dtUpd.commit();
  }
 @Override
 public void ej_edit1()
 {
   try
   {
     copiaPedidoNuevo(dtCon1,dtAdd,"Modificado Pedido",EU.usuario,eje_numeE.getValorInt(),
              EU.em_cod ,pvc_numeE.getValorInt());
     dtAdd.commit();
     s="select * from pedvenc where emp_codi = " + EU.em_cod  +
          " and eje_nume= " + eje_numeE.getValorInt() +
          " and pvc_nume = " + pvc_numeE.getValorInt();
     
     dtAdd.select(s);
     dtAdd.edit();
     actCabecera();
     actLinea();
     resetBloqueo(dtAdd, "pedvenc",
                 eje_numeE.getValorInt() + "|" + EU.em_cod  +
                 "|" + pvc_numeE.getValorInt(),false);
     dtAdd.commit();
     
     activaTodo();
     actGridHist(EU.em_cod ,eje_numeE.getValorInt(),
                  pvc_numeE.getValorInt());
     mensaje("");
     mensajeErr("Pedido ... MODIFICADO");
     nav.pulsado=navegador.NINGUNO;
   }
   catch (SQLException | ParseException k)
   {
     Error("Error al Insertar Pedido", k);
     return;
   }

 }

  @Override
  public void canc_edit()
  {
    nav.pulsado=navegador.NINGUNO;
    activaTodo();
    verDatos();
    try
    {
      resetBloqueo(dtAdd, "pedvenc",
                   eje_numeE.getValorInt() + "|" + EU.em_cod  +
                   "|" + pvc_numeE.getValorInt()
                   );
    }
    catch (Exception k)
    {
      mensajes.mensajeAviso("Error al Quitar el bloqueo\n" + k.getMessage());
    }

    mensajeErr("Modificacion ... Cancelada");
    mensaje("");
  }
  void nuevoPedido()
  {
      try
      {
          nav.setEnabled(false);
          activar(navegador.ADDNEW, true);
         
          jt.setEnabled(false);
          jt.removeAllDatos();
          pvc_incfraE.setSelected(false);
          pvc_fecentE.resetCambio();
          rut_codiE.resetCambio();
          pvc_fecpreE.resetCambio();
          pvc_confirE.setValor("S");
          pvc_verfecE.setEnabled(opVerProd.getValor().equals("" + pstockAct.VER_ULTVENTAS));
          cli_codiE.resetTexto();
          cli_codiE.resetCambio();
          pvc_comenE.resetTexto();
          pvc_comrepE.resetTexto();
          pvc_nupeclE.resetTexto();
        
          eje_numeE.setValorDec(EU.ejercicio);
          pvc_numeE.setValorDec(getNumPed(false));
//      alm_codiE.setValor(""+ALMACEN);
          pvl_precioE.setValorInt(-1);
          pvl_precioE.resetCambio();
          usu_nombE.setText(EU.usuario);
          pvc_comenE.resetTexto();
          pvc_comrepE.resetTexto();
          pvc_fecpedE.setText(Formatear.getFechaAct("dd-MM-yy"));
          pvc_horpedE.setText(Formatear.getFechaAct("HH.ss"));
          pvc_deposE.setValor("N");
          pvc_estadE.setValor("P");
         
          pro_codiE.getFieldBotonCons().setEnabled(true);
    } catch (SQLException k)
    {
      Error("Error en PADAddNew ",k);
      return;
    }
    Baceptar.setEnabled(false);
    mensaje("Creando nuevo Pedido ...");
    jt.requestFocusInicio();
    cli_codiE.requestFocus();
  }
  public void setExterno(boolean externo)
  {
      swExterno=externo;
  }
  @Override
  public void PADAddNew()
  {  
      alArtStock.clear();
      jt.removeAllDatos();
      swExterno=false;
      pvc_fecentE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
      pvc_fecpreE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
      nuevoPedido();
  }

  @Override
  public boolean checkAddNew()
  {
    try
    {
      if (!cli_codiE.controlar())
      {
        mensajeErr(cli_codiE.getMsgError());
        return false;
      }
      if (!cli_codiE.isActivo())
      {
        mensajeErr("Cliente NO esta activo o esta forzado a no Servir");
        return false;
      }
    
      if (pvc_fecentE.getError() || pvc_fecentE.isNull())
      {
        mensajeErr("Fecha de Entrega NO valida");
        pvc_fecentE.requestFocus();
        return false;
      }
      
      if (nav.pulsado==navegador.ADDNEW && Formatear.comparaFechas(pvc_fecentE.getDate(),Formatear.getDateAct())<0)
      {
        mensajeErr("Fecha de Entrega NO puede ser inferior a la de hoy");
        pvc_fecentE.requestFocus();
        return false;
      }
      if (pvc_fecpreE.isNull())
          pvc_fecpreE.setText(pvc_fecentE.getText());
      if (Formatear.comparaFechas(pvc_fecentE.getDate(),pvc_fecpreE.getDate())<0)
      {
        mensajeErr("Fecha de preparacion no puede ser superior a la de Entrega");
        rut_codiE.requestFocus();
        return false;
      }
      if (!rut_codiE.controla())
      {
        mensajeErr("Ruta de entrega no es valida");
        rut_codiE.requestFocus();
        return false;
          
      }
      if (pvc_estadE.getValor().equals("L") && ! P_ADMIN)
      {
          pvc_estadE.requestFocus();
          mensajeErr("Este estado solo se puede poner desde mantenimiento Albaranes venta");
          return false;
      }
      jt.salirGrid();
      int nr=cambiaLineaJT(jt.getSelectedRow());
      if (nr>=0)
      {
          jt.requestFocusLater(jt.getSelectedRow(), nr);
          return false;
      }
      actAcumJT();
      if (nlE.getValorInt()==0)
      {
        mensajeErr("Introducir alguna linea de pedido");
        jt.requestFocusInicio();
        return false;
      }
    } catch (SQLException | ParseException k)
    {
      Error("Error al validar datos",k);
    }
    return true;
  }

    /**
     *
     */
    @Override
  public void ej_addnew1()
  {
    try {
      pvc_numeE.setValorInt(getNumPed(true));
      
      dtAdd.addNew("pedvenc",false);
      dtAdd.setDato("emp_codi",EU.em_cod );
      dtAdd.setDato("eje_nume",eje_numeE.getValorInt());
      dtAdd.setDato("pvc_nume",pvc_numeE.getValorInt());
      dtAdd.setDato("pvc_impres","N"); // No impreso
      
      dtAdd.setDato("avc_serie","A");
      dtAdd.setDato("avc_nume",0);
      actCabecera();
      actLinea();
      dtAdd.commit();
      activaTodo();

      mensajeErr("Pedido ... INSERTADO");
      mensaje("");
      if (! swExterno)
      {
         salirEnabled(false);
         nuevoPedido();
      }
      else
        nav.pulsado=navegador.NINGUNO;
    } catch (Exception k)
    {
      Error("Error al Insertar Pedido",k);      
    }

  }

  private void actLinea() throws SQLException
  {
    int nRow=jt.getRowCount();
    
    
    if (nav.pulsado==navegador.EDIT)
    { // Borro las lineas que ya no estan.    
        s = " SELECT * FROM pedvenl WHERE emp_codi = " + EU.em_cod  +
          " and eje_nume= " + eje_numeE.getValorInt() +
          " and pvc_nume = " + pvc_numeE.getValorInt() ;
        if (dtStat.select(s,false))
        {
            do
            {
              boolean swEnc=false;
              for (int n=0;n<nRow;n++)
              { 
                    if (jt.getValorInt(n, JT_NL)==dtStat.getInt("pvl_numlin"))
                    {
                        swEnc=true;
                        break;
                    }
              }
              if (!swEnc)
              {
                    dtAdd.executeUpdate(" delete FROM pedvenl WHERE emp_codi = " + EU.em_cod +
                        " and eje_nume= " + eje_numeE.getValorInt() +
                        " and pvc_nume = " + pvc_numeE.getValorInt()+
                        " and pvl_numlin="+dtStat.getInt("pvl_numlin"));   
                     dtAdd.executeUpdate(" delete FROM pedvenmod WHERE emp_codi = " + EU.em_cod  +
                        " and eje_nume= " + eje_numeE.getValorInt() +
                        " and pvc_nume = " + pvc_numeE.getValorInt()+
                        " and pvl_numlin="+dtStat.getInt("pvl_numlin"));    
              }
            } while (dtStat.next());
        }
    }
    int nl=1;
   
    for (int n=0;n<nRow;n++)
    {
      if (jt.getValorInt(n, 0) == 0)
        continue;
      s = " SELECT * FROM pedvenl WHERE emp_codi = " + EU.em_cod  +
          " and eje_nume= " + eje_numeE.getValorInt() +
          " and pvc_nume = " + pvc_numeE.getValorInt() +
          " and pvl_numlin = " + jt.getValorInt(n, JT_NL);
      if (! dtAdd.select(s,true))
      {
        dtAdd.addNew();
        dtAdd.setDato("emp_codi", EU.em_cod );
        dtAdd.setDato("eje_nume", eje_numeE.getValorInt());
        dtAdd.setDato("pvc_nume", pvc_numeE.getValorInt());
        dtAdd.setDato("pvl_numlin", nl);
      }
      else
        dtAdd.edit();
      
      dtAdd.setDato("pvl_canti", jt.getValorDec(n,JT_CANTI)); 
      HashMap<Integer, Double> hm= MantArticulos.getRelUnidadKilos(jt.getValorInt(n,JT_PROD),dtStat);
              
      double kilos=jt.getValorDec(n,JT_CANTI)*  (jt.getValString(n,JT_TIPCAN).startsWith("P")?hm.get(MantArticulos.KGXUNI):
          jt.getValString(n,JT_TIPCAN).startsWith("C")?hm.get(MantArticulos.KGXCAJ):1);
      double unid=(hm.get(MantArticulos.TIPVENCAJA)==1?
          hm.get(MantArticulos.KGXCAJ):hm.get(MantArticulos.KGXUNI))==0?1:
          kilos/
          (hm.get(MantArticulos.TIPVENCAJA)==1?
          hm.get(MantArticulos.KGXCAJ):hm.get(MantArticulos.KGXUNI));
    
      dtAdd.setDato("pvl_kilos", kilos);             
      dtAdd.setDato("pvl_unid",unid );
      dtAdd.setDato("pvl_tipo", jt.getValString(n,JT_TIPCAN).substring(0,1));
      dtAdd.setDato("pro_codi", jt.getValorInt(n,JT_PROD));
      dtAdd.setDato("pvl_comen", jt.getValString(n,JT_COMEN));
      dtAdd.setDato("pvl_precio", jt.getValorDec(n,JT_PRECIO));
      dtAdd.setDato("pvl_precon", jt.getValBoolean(n,JT_PRECON)?-1:0);
      dtAdd.setDato("prv_codi", jt.getValorInt(n,JT_PROV));
      dtAdd.setDato("pvl_feccad",jt.getValString(n,JT_FECCAD).trim().equals("")?
          null:jt.getValString(n,JT_FECCAD),"dd-MM-yy");
      if (dtAdd.getTipoUpdate()==DatosTabla.ADDNEW)
      {
        dtAdd.setDato("pvl_fecped", "{ts '"+Formatear.getFechaAct("yyyy-MM-dd hh:mm:ss")+"'}");
        dtAdd.setDato("pvl_fecmod", (Timestamp)null);
      }
      else
        dtAdd.setDato("pvl_fecmod", "{ts '"+Formatear.getFechaAct("yyyy-MM-dd hh:mm:ss")+"'}");
      dtAdd.update();    
      nl++;
    }
 
  }

  private void actCabecera() throws  SQLException,ParseException
  {
    dtAdd.setDato("cli_codi",cli_codiE.getValorInt());
    dtAdd.setDato("pvc_depos",pvc_deposE.getValor());
    dtAdd.setDato("pvc_clinom",cli_codiE.getTextNomb());
    dtAdd.setDato("alm_codi", ALMACEN); //alm_codiE.getValorInt());
    dtAdd.setDato("pvc_incfra", pvc_incfraE.isSelected()?"S":"N"); //alm_codiE.getValorInt());
    dtAdd.setDato("pvc_fecped","current_timestamp");
    dtAdd.setDato("pvc_fecent",pvc_fecentE.getDate());
    dtAdd.setDato("pvc_fecpre",pvc_fecpreE.getDate());
    dtAdd.setDato("rut_codi",rut_codiE.getText());
    dtAdd.setDato("pvc_confir",pvc_confirE.getValor());
    dtAdd.setDato("pvc_comen",Formatear.strCorta(pvc_comenE.getText(),200));
    dtAdd.setDato("pvc_comrep",Formatear.strCorta(pvc_comrepE.getText(),200));

    dtAdd.setDato("pvc_fecpre",pvc_fecpreE.getDate());
    
    if (nav.getPulsado()==navegador.EDIT)
    {
        if (pvc_estadE.getValor().equals("C"))
            dtAdd.setDato("avc_ano", -1);
        if (pvc_estadE.getValor().equals("L"))
        { // Se quiere poner como preparado.
            if (dtAdd.getInt("avc_ano")==0)
                dtAdd.setDato("avc_ano", 1);
        }
        if (pvc_estadE.getValor().equals("P"))
        {
            dtAdd.setDato("avc_ano", 0);
            dtAdd.setDato("avc_nume", 0);            
        }
    }

    dtAdd.setDato("usu_nomb",EU.usuario);
    dtAdd.setDato("pvc_cerra",0); // Albaran Abierto
    dtAdd.setDato("pvc_nupecl",pvc_nupeclE.getText());
    
    dtAdd.update();
  }

  @Override
  public void canc_addnew()
  {
    if (! jt.isVacio())
    {
        int ret=       mensajes.mensajeYesNo("Has PULSADO CANCELAR EL PEDIDO. CONTINUAR ALTA DE PEDIDO? ");
        if (ret!=mensajes.NO)
            return;
    }
    nav.pulsado=navegador.NINGUNO;
    activaTodo();
    verDatos();
    mensaje("");
    mensajeErr("Creacion de Nuevo Pedido ... CANCELADO");
  }

  @Override
  public void PADDelete()
  {
    try
    {
      if (hisRowid!=0)
      {
        msgBox("Viendo Pedido historico ... IMPOSIBLE MODIFICAR");
        nav.pulsado = navegador.NINGUNO;
        activaTodo();
        return;
      }
      if (avc_anoE.getValorDec() > 0)
      {
        msgBox("Pedido YA TIENE albaran ... IMPOSIBLE BORRAR");
        nav.pulsado = navegador.NINGUNO;
        activaTodo();
        return;
      }
      if (!setBloqueo(dtAdd, "pedvenc",
                      eje_numeE.getValorInt() + "|" + EU.em_cod +
                      "|" + pvc_numeE.getValorInt()))
      {
        msgBox(msgBloqueo);
        activaTodo();
        return;
      }
      mensaje("Borrando Pedido ...");
      Baceptar.setEnabled(true);
      Bcancelar.setEnabled(true);
      Bcancelar.requestFocus();
    }
    catch (SQLException | UnknownHostException k)
    {
      Error("Error al Borrar registro", k);
    }

  }


  @Override
  public void ej_delete1()
  {
    try
    {
      copiaPedidoNuevo(dtCon1,dtAdd,"Borrado Pedido",EU.usuario,eje_numeE.getValorInt(),
              EU.em_cod ,pvc_numeE.getValorInt());
      s = " DELETE FROM pedvenl WHERE emp_codi = " + EU.em_cod  +
          " and eje_nume= " + eje_numeE.getValorInt() +
          " and pvc_nume = " + pvc_numeE.getValorInt();
      dtAdd.executeUpdate(s);
      s = "UPDATE pedvenc  set pvc_confir='C',avc_ano=9999 where emp_codi = " + EU.em_cod  +
          " and eje_nume= " + eje_numeE.getValorInt() +
          " and pvc_nume = " + pvc_numeE.getValorInt();
      dtAdd.executeUpdate(s);
      resetBloqueo(dtAdd, "pedvenc",
                   eje_numeE.getValorInt() + "|" + EU.em_cod  +
                   "|" + pvc_numeE.getValorInt(), false);
      dtAdd.commit();
      activaTodo();
      mensaje("");
      rgSelect();
      mensajeErr("Pedido ... BORRADO");
    }
    catch (Exception k)
    {
      Error("Error al Insertar Pedido", k);
    }

  }

  public void canc_delete()
  {
    nav.pulsado = navegador.NINGUNO;
    activaTodo();
    try
    {
      resetBloqueo(dtAdd, "pedvenc",
                   eje_numeE.getValorInt() + "|" + EU.em_cod  +
                   "|" + pvc_numeE.getValorInt()
                   );
    }
    catch (Exception k)
    {
      mensajes.mensajeAviso("Error al Quitar el bloqueo\n" + k.getMessage());
    }

    mensajeErr("BORRADO ... Cancelado");
    mensaje("");
  }

  public void activar(int modo,boolean b)
  {
    BponPrecio.setEnabled(b);
    Baceptar.setEnabled(b);
    Bcancelar.setEnabled(b);
    cli_codiE.setEnabled(b);  
    pvc_incfraE.setEnabled(b);  
    
    if (modo!=navegador.ADDNEW && modo!=navegador.EDIT )
    {
      if (modo!=navegador.QUERY)
      {
        jt.setEnabled(b);
        pro_codiE.getFieldBotonCons().setEnabled(b);        
      }
      pvc_idE.setEnabled(b);
      eje_numeE.setEnabled(b);
      pvc_numeE.setEnabled(b);
    }
    if (modo==navegador.QUERY || !b)
    {
        pvc_fecpedE.setEnabled(b);
        pvc_horpedE.setEnabled(b);
        usu_nombE.setEnabled(b);      
    }
   
    pvc_estadE.setEnabled(modo==navegador.EDIT && b);  
    pvc_deposE.setEnabled(b);
    opAvisoStock.setEnabled(b);
    
    pvc_nupeclE.setEnabled(b);
    pvc_fecentE.setEnabled(b);
    rut_codiE.setEnabled(b);
    pvc_fecpreE.setEnabled(b);
    pvc_confirE.setEnabled(b);
    if (modo!=navegador.QUERY)
    {
      pvc_comenE.setEnabled(b);
      pvc_comrepE.setEnabled(b);
//      opPedidos.setEnabled(b);
//      opVerProd.setEnabled(b);
    }
    if (modo==navegador.QUERY || modo==navegador.TODOS)
    {

        avc_anoE.setEnabled(b);
        avc_serieE.setEnabled(b);
        avc_numeE.setEnabled(b);
    }
//    pvc_verfecE.setEnabled(false);
//    Pcabe.setEnabled(b);
    Bimpri.setEnabled(!b);
  }

  @Override
  public void activar(boolean b)
  {
    activar(navegador.TODOS,b);
  }
  void verDatos()
  {
    tablaCab=TABLACAB;
    
    vistaPed=VISTAPED;
    hisRowid=0;

    verDatos(dtCons);
    
  }
  void verDatos(DatosTabla dt)
  {
    if (dt.getNOREG())
      return;
    pvc_idE.resetTexto();
    cli_codiE.resetTexto();
    cli_poblE.setText("");
    pvc_nupeclE.resetTexto();
    pvc_fecentE.resetTexto();
    rut_codiE.resetTexto();
    pvc_fecpreE.resetTexto();
    pvc_confirE.resetTexto();
    pvc_comenE.resetTexto();
    pvc_comrepE.resetTexto();
    eje_numeE.resetTexto();
    pvc_numeE.resetTexto();
    Ppie.resetTexto();
    jt.removeAllDatos();
    try {
      
      eje_numeE.setValorInt(dt.getInt("eje_nume"));
      pvc_numeE.setValorInt(dt.getInt("pvc_nume"));
      s = "SELECT * FROM "+tablaCab+" WHERE "+
          (hisRowid>0?" his_rowid = "+dt.getInt("his_rowid"):
          " emp_codi = " + EU.em_cod  +
          " and eje_nume= " + eje_numeE.getValorInt() +
          " and pvc_nume = " + pvc_numeE.getValorInt());
      if (! dtCon1.select(s))
      {
        mensajeErr("NO ENCONTRADO PEDIDO ... SEGURAMENTE SE BORRO");
        return;
      }
      cli_codiE.setValorInt(dtCon1.getInt("cli_codi"));
      if (dtCon1.getObject("pvc_clinom")!=null)          
          cli_codiE.setNombreCliente(dtCon1.getString("pvc_clinom"));
      cli_poblE.setText(cli_codiE.getLikeCliente().getString("cli_pobl"));
      pvc_nupeclE.setText(dtCon1.getString("pvc_nupecl"));
      pvc_fecentE.setText(dtCon1.getFecha("pvc_fecent","dd-MM-yyyy"));
      rut_codiE.setText(dtCon1.getString("rut_codi"));
      pvc_fecpreE.setText(dtCon1.getFecha("pvc_fecpre","dd-MM-yyyy"));
      pvc_confirE.setValor(dtCon1.getString("pvc_confir"));
      pvc_comenE.setText(dtCon1.getString("pvc_comen"));
      pvc_comrepE.setText(dtCon1.getString("pvc_comrep"));
      pvc_incfraE.setSelected(dtCon1.getString("pvc_incfra").equals("S"));
//      alm_codiE.setValor(dtCon1.getInt("alm_codi"));
      pvc_fecpedE.setDate(dtCon1.getDate("pvc_fecped"));
      pvc_horpedE.setText(dtCon1.getFecha("pvc_fecped","HH.mm"));
      usu_nombE.setText(dtCon1.getString("usu_nomb"));
      avc_numeE.setText(dtCon1.getString("avc_nume"));
      avc_serieE.setValor(dtCon1.getString("avc_serie"));
      avc_anoE.setValorDec(dtCon1.getInt("avc_ano",true));
      pvc_idE.setValorInt(dtCon1.getInt("pvc_id"));
      pvc_impresE.setSelecion(dtCon1.getString("pvc_impres"));
      pvc_estadE.setValor(dtCon1.getInt("avc_ano",true)==0?"P":dtCon1.getInt("avc_ano",true)>0?"L":"C" );
      pvc_deposE.setValor(dtCon1.getString("pvc_depos"));
      s = "SELECT * FROM "+vistaPed+" WHERE "+
          (hisRowid>0?" his_rowid = "+dt.getInt("his_rowid"):
           " emp_codi = " + EU.em_cod  +
              " and eje_nume= " + eje_numeE.getValorInt() +
              " and pvc_nume = " + pvc_numeE.getValorInt());
      if (dtCon1.select(s))
      {
        do
        {
          ArrayList v=new ArrayList();
          v.add(dtCon1.getString("pro_codi"));
          v.add(pro_codiE.getNombArtCli(dtCon1.getInt("pro_codi"),
                                              cli_codiE.getValorInt()));    
         v.add(dtCon1.getDouble("pvl_canti"));
         v.add(pvl_tipoE.getText(dtCon1.getString("pvl_tipo")));
         v.add(dtCon1.getString("pvl_precio"));
         v.add(dtCon1.getInt("pvl_precon")!=0);
         v.add(MantTarifa.getPrecTar(dtStat,dtCon1.getInt("pro_codi"),cli_codiE.getValorInt(),
             cli_codiE.getLikeCliente().getInt("tar_codi"), 
            pvc_fecentE.getText()));
         v.add(dtCon1.getString("pvl_comen"));
         v.add(dtCon1.getString("prv_codi"));
         v.add(prv_codiE.getNombPrv(dtCon1.getString("prv_codi")));
         v.add(dtCon1.getFecha("pvl_feccad","dd-MM-yy"));
         v.add(dtCon1.getString("pvl_numlin"));
         v.add(dtCon1.getString("pvm_canti"));
         jt.addLinea(v);
        }
        while (dtCon1.next());
      }
      actAcumJT();
      if (hisRowid==0)
        actGridHist(EU.em_cod ,eje_numeE.getValorInt(),
                  pvc_numeE.getValorInt());
    } catch (SQLException k)
    {
      Error("Error al Visualizar Pedidos",k);
    }
  }
  int cambiaLineaJT(int row)
  {
    if (pro_codiE.isNull())
      return -1;
    try {
      if (!pro_codiE.controla(false))
      {
        mensajeErr(pro_codiE.getMsgError());
        return JT_PROD;
      }
      if (!prv_codiE.controla(false))
      {
        mensajeErr(prv_codiE.getMsgError());
        return JT_PROV;
      }
      if (pvl_cantiE.getValorDec()==0)
      {
        mensajeErr("Introduzca Cantidad de Producto Pedido");
        return JT_CANTI;
      }
      if (pvl_tipoE.isNull())
      {
          mensajeErr("Introduzca tipo de cantidad (Kilos o Piezas o Cajas)");
          return JT_TIPCAN;
      }
      if (pvl_tipoE.getError())
      {
          mensajeErr("Tipo de cantidad Invalida. Aceptables: Kilos/Piezas/Cajas");
          return JT_TIPCAN;         
      }
      if (pvl_tipoE.getValor().equals("P") &&    MantArticulos.getKilosUnid(pro_codiE.getValorInt(), dtAdd)==0)
      {
          mensajeErr("Este producto no tiene equivalencia Kilos/Unidades. Imposible realizar el pedido en Unidades");
          return JT_TIPCAN;
      }
      if (pvl_tipoE.getValor().equals("C") &&    MantArticulos.getKilosCaja(pro_codiE.getValorInt(), dtAdd)==0)
      {
          mensajeErr("Este producto no tiene equivalencia Kilos/Caja. Imposible realizar el pedido en Cajas");
          return JT_TIPCAN;
      }
      if (alArtStock.indexOf(pro_codiE.getValorInt())>=0 || alArtStockP.indexOf(pro_codiE.getValorInt())>=0 || !opAvisoStock.isSelected() )
          return -1; // ya aviso sobre este producto
       HashMap<Integer, Double> hm= MantArticulos.getRelUnidadKilos(pro_codiE.getValorInt() ,dtStat);
       double kilos= pvl_cantiE.getValorInt() *  ( pvl_tipoE.getValor().startsWith("P")?hm.get(MantArticulos.KGXUNI):
           pvl_tipoE.getValor().startsWith("C")?hm.get(MantArticulos.KGXCAJ):1);
      if (pro_codiE.getLikeProd().getDouble("pro_stock")< kilos)
      {
          int ret=mensajes.mensajeYesNo("ATENCION. SOLO HAY "+Formatear.redondea(pro_codiE.getLikeProd().getDouble("pro_stock"),0)+ " KG. ("+
              Formatear.redondea(pro_codiE.getLikeProd().getDouble("pro_stkuni"),0)+
              " Unidades/Cajas)  EN STOCK\nSeguir Avisando de este producto?");
          if (ret==mensajes.NO)
              alArtStockP.add(pro_codiE.getValorInt());
          alArtStock.add(pro_codiE.getValorInt());
      }
    } catch (Exception k)
    {
      Error("Erro al controlar cambio de linea",k);
    }
    return -1;
  }

  int getNumPed(boolean act) throws SQLException
  {
    int nAlb;
    s = "SELECT num_pedid  FROM v_numerac WHERE emp_codi = " +  EU.em_cod  +
        " and eje_nume=" + eje_numeE.getValorInt();
    if (!dtAdd.select(s, true))
      throw new SQLException("NO encontrado GUIA Numeraciones para esta Empresa\n" + s);
    nAlb = dtAdd.getInt("num_pedid") + 1;
    if (!act)
      return nAlb;
    dtAdd.edit(dtAdd.getCondWhere());
    dtAdd.setDato("num_pedid", nAlb);
    dtAdd.update(stUp);
    return nAlb;
  }

  /**
   * Actualizar Acumulados de Pedido
   */
  void actAcumJT()
  {
    int nRows=jt.getRowCount();
    int nl=0;
    int nu=0;
    for (int n=0;n<nRows;n++)
    {
      if (jt.getValorInt(n,JT_PROD)==0)
        continue;
      nl++;
      nu+=jt.getValorDec(n,JT_CANTI);
    }
    nlE.setValorInt(nl);
    cantE.setValorDec(nu);
  }
    @Override
    @SuppressWarnings("static-access")
  public void rgSelect() throws SQLException
  {
    super.rgSelect();
    if (!dtCons.getNOREG())
    {
      dtCons.last();
      nav.setEnabled(navegador.ULTIMO, false);
      nav.setEnabled(navegador.SIGUIENTE, false);
    }
    verDatos();
  }
  void Bimpri_actionPerformed()
   {
     if (dtCons.getNOREG())
       return;
     try {
       java.util.HashMap mp = Listados.getHashMapDefault();
       
       JasperReport jr = Listados.getJasperReport(EU,"pedventas");
       s="select 1 as orden, l.*,p.prv_nomb ,c.cli_codi,c.alm_codi,c.pvc_fecped, "+
        " c.pvc_fecent,c.pvc_clinom,c.usu_nomb,c.pvc_comen,al.alm_nomb, "+
        " a.pro_nomb, cl.cli_nomb,cl.cli_pobl "+
        " from pedvenl as l left join v_proveedo p on  p.prv_codi = l.prv_codi, "+
        " pedvenc as c,v_articulo as a,v_almacen as al,clientes as cl "+
        " where  c.emp_codi = l.emp_codi "+
        " and c.eje_nume = l.eje_nume "+
        " and c.pvc_nume = l.pvc_nume "+
        " and l.pro_codi = a.pro_codi "+
        " and al.alm_codi = c.alm_codi "+
        " and c.cli_codi = cl.cli_codi "+
        " and c.emp_codi = "+EU.em_cod +
        " and c.eje_nume = "+eje_numeE.getValorInt()+
        " and c.pvc_nume = "+pvc_numeE.getValorInt()+
        " order by l.emp_codi,l.eje_nume,l.pvc_nume,l.pvl_numlin ";
       ResultSet rs;

       rs=dtCon1.getStatement().executeQuery(dtStat.getStrSelect(s));

       JasperPrint jp = JasperFillManager.fillReport(jr, mp, new JRResultSetDataSource(rs));
       gnu.chu.print.util.printJasper(jp, EU);
       s = "update PEDVENC SET pvc_impres = 'S' WHERE emp_codi = " + EU.em_cod +
           " and eje_nume = " + eje_numeE.getValorInt() +
           " and pvc_nume = " + pvc_numeE.getValorInt() ;
        dtAdd.executeUpdate(s);
        ctUp.commit();
        mensajeErr("Pedido Ventas ... IMPRESO ");
     }
     catch (JRException | SQLException | PrinterException k)
     {
       Error("Error al imprimir Pedido Venta", k);
     }
   }
   public void irGrid() {
        try
        {
            if (nav.pulsado != navegador.EDIT && nav.pulsado != navegador.ADDNEW)
                return;
            if (!cli_codiE.controlar())
            {
                mensajeErr("Cliente no valido");
                return;
            }
            if (!cli_codiE.isActivo())
            {
                mensajeErr("Cliente NO esta activo O esta forzado a no Servir");
                cli_codiE.requestFocus();
                return;
            }
            if (!jt.isEnabled())
                jt.setEnabled(true);

            if (cli_codiE.hasCambio())
            {
                 if (cli_codiE.isIncluirFra())
                 {
//                    String msgAviso=pdclien.getUltimoCambio(dtStat,cli_codiE.getValorInt());
//                    msgExplica("ATENCION!. Cliente esta marcado como Incluir Facturas",msgAviso);  
                    pvc_incfraE.setSelected(true);
                 }
                 else
                 {
                    if (cli_codiE.getEstadoServir()==cliPanel.SERVIR_NO )
                    {
                       String msgAviso=pdclien.getUltimoCambio(dtStat,cli_codiE.getValorInt());
                       msgExplica("ATENCION!. Cliente esta marcado como para no servir",msgAviso);  
                       pvc_incfraE.setSelected(true);
                    }
                 }
               
                cli_codiE.resetCambio();
                rut_codiE.setText(cli_codiE.getLikeCliente().getString("rut_codi"));
                if (cli_codiE.getLikeCliente().getInt("cli_gener") == 0 && nav.getPulsado() == navegador.ADDNEW)
                {
                    s = "select p.eje_nume,p.pvc_nume,p.avc_ano,p.avc_serie,p.avc_nume,avc_impres,pvc_fecent from pedvenc as p left join v_albavec as a on p.avc_ano=a.avc_ano "
                        + " and p.avc_nume = a.avc_nume and p.avc_serie = a.avc_serie "
                        + " where p.cli_codi = " + cli_codiE.getValorInt()
                        + " and pvc_fecent >= '" + Formatear.getFechaDB(Formatear.sumaDiasDate(pvc_fecentE.getDate(), -7)) + "'"
                        + " and pvc_confir!='C'"
                        + " order by avc_nume asc";
                    if (dtStat.select(s))
                    {
                        int res = mensajes.NO;
                        if (dtStat.getInt("avc_nume") == 0)
                        {
                            res = mensajes.mensajeYesNo("Cliente ya tiene pedido " + dtStat.getInt("pvc_nume")
                                + " en fecha: " + dtStat.getFecha("pvc_fecent", "dd-MM-yy") + " sin preparar. ¿ Editar pedido ?");
                        }
                        else
                        {
                            do
                            {
                                if ((dtStat.getInt("avc_impres", true) & 1) == 0 && dtStat.getString("avc_serie").equals("A"))
                                {
                                    res = mensajes.mensajeYesNo("Cliente con albaran " + dtStat.getInt("avc_nume")
                                        + " en fecha: " + dtStat.getFecha("pvc_fecent", "dd-MM-yy")
                                        + " preparado sin servir. ¿ Editar Pedido ?");
                                    break;
                                }
                            } while (dtStat.next());
                        }
                        if (res == mensajes.YES)
                        {
                            if (dtCons.select("select * from pedvenc where eje_nume = " + dtStat.getInt("eje_nume")
                                + " and pvc_nume = " + dtStat.getInt("pvc_nume")))
                            {
                                nav.pulsado = navegador.NINGUNO;
                                activaTodo();
                                verDatos();
                                nav.pulsado = navegador.EDIT;
                                PADEdit();
                            }
                        }
                    }
                }
            }
            if (cli_codiE.getTarifa() != pstock.getTarifa())
                pstock.setTarifa(cli_codiE.getTarifa());
            if (opVerProd.getValor().equals("" + pstockAct.VER_ULTVENTAS))
                pstock.setCliente(cli_codiE.getValorInt());

            pstock.verFamilias();

            jt.requestFocusInicioLater();
            Baceptar.setEnabled(true);
        } catch (SQLException | ParseException k)
        {
            Error("Error al ir al grid", k);
        }
   }
   public void setCliente(int cliCodi)
   {
       cli_codiE.setValorInt(cliCodi);
   }
   public void setEjercicioPedido(int ejerc)
   {
       eje_numeE.setValorInt(ejerc);
   }
   public void setNumeroPedido(int numPed)
   {
       pvc_numeE.setValorInt(numPed);
   }
   
  
   public void leerDatosCliente() throws Exception
   {
      cli_poblE.setText(cli_codiE.getLikeCliente().getString("cli_pobl"));            
   }
   void cli_codiE_afterFocusLost(boolean error)
   {
     if (error)
     {
       cli_poblE.setText("");
       return;
     }

     try
     {
       leerDatosCliente();
     }
     catch (Exception k)
     {
       Error("Error al ver Productos de nuevo cliente", k);
     }   
   }

   void BbusProd_actionPerformed()
   {
     try
     {
       if (aypro == null)
       {
         aypro = new AyuArt(EU, vl, dtCon1)
         {
           @Override
           public void matar()
           {
             ej_consPro(aypro);
           }
         };
         vl.add(aypro);
         aypro.setLocation(25, 25);
         aypro.iniciarVentana();
       }
       aypro.setVisible(true);
       this.setEnabled(false);
       this.setFoco(aypro);
       aypro.reset();

       }  catch (Exception j)
       {
         this.setEnabled(true);
       }

     }

     void ej_consPro(AyuArt aypro)
     {
       try
       {
         aypro.setVisible(false);

         this.setEnabled(true);
         this.toFront();

         {
           this.setSelected(true);
         }
         this.setFoco(null);
         if (aypro.getChose())
           pstock.verProducto(aypro.getProCodi());
       }
       catch (Exception k)
       {
         Error("Error al buscar datos de Producto",k);
       }

     }
     public static boolean getPedido(DatosTabla dt,int empCodi,int ejeNume,int pvcNume) throws SQLException
     {
            String s="select * from pedvenc where eje_nume="+ejeNume+
                " and pvc_nume = "+pvcNume+
                " and emp_codi = "+empCodi;
            return  dt.select(s);
     }
     public static String getRuta(DatosTabla dt,int empCodi,int ejeNume,int pvcNume) throws SQLException
     {
       if (!getPedido(dt,empCodi,ejeNume,pvcNume))
           return null;
       return dt.getString("rut_codi");        
     }
     /**
      * Devuelve el identificador de un pedido
      * @param dt
      * @param empCodi Empresa
      * @param pvcAno Año
      * @param pvcNume Numero Pedido
      * @return Identificador pedido. -1 Si no lo encuentra
      * @throws SQLException 
      */
    public static int getIdPedido(DatosTabla dt,int empCodi,int pvcAno,int pvcNume) throws SQLException
    {
        if (!getPedido(dt, empCodi,  pvcAno, pvcNume))
            return -1;
        else
            return dt.getInt("pvc_id");
    }
    /**
     * Llamar a Mantenimiento Pedidos desde otro programa
     * @param jf clase Principal (menu)
     * @param ejeNume Ejercicio
     * @param numPedido  Numero Pedido
     */
    public static void irMantPedido( Principal jf, int ejeNume,int numPedido)
    {
        if (jf==null)
            return;
        ejecutable prog;
        if ((prog = jf.gestor.getProceso(pdpeve.getNombreClase())) == null)
            return;
        pdpeve cm = (pdpeve) prog;
        if (cm.inTransation())
        {
            mensajes.mensajeAviso("Mantenimiento Pedidos de Ventas ocupado. No se puede realizar el Alta");
            return;
        }
        cm.PADQuery();
        cm.setEjercicioPedido(ejeNume);
        cm.setNumeroPedido(numPedido);
       

        cm.ej_query();
        jf.gestor.ir(cm);
    }
}
