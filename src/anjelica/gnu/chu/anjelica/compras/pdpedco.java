package gnu.chu.anjelica.compras;

import gnu.chu.controles.*;
import gnu.chu.sql.*;
import java.sql.*;
import gnu.chu.Menu.*;
import gnu.chu.anjelica.almacen.pdalmace;
import gnu.chu.anjelica.listados.Listados;
import gnu.chu.anjelica.pad.pdtransp;
import java.awt.*;
import java.util.*;
import javax.swing.BorderFactory;
import gnu.chu.camposdb.*;
import gnu.chu.interfaces.*;
import gnu.chu.utilidades.*;
import java.awt.event.*;
import java.text.*; 
import net.sf.jasperreports.engine.*;
import javax.swing.event.*;
 /**
 * <p>Título: pdpedco</p>
 * <p>Descripción: Mantenimiento Pedidos de Compra</p>
 * <p>Copyright: Copyright (c) 2005-2018
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
 * @author Chuchi P
 * @version 1.0
 */

public class pdpedco extends ventanaPad   implements PAD
{
  final int NDIACAD=25;  // No Dias de Caducidad por defecto.
  int nRow=-1;
  getMsgTexto msgTexto;
  String s;
  ResultSet rs;
  Statement st;
  private boolean SWADMIN=false;
  String subject;
  CPanel Pprinc = new CPanel();
  CPanel Pcabe = new CPanel();
  CLabel cLabel1 = new CLabel();
  CTextField pcc_numeE = new CTextField(Types.DECIMAL,"####9");
  CLabel cLabel2 = new CLabel();
  CTextField eje_numeE = new CTextField(Types.DECIMAL,"###9");
  CLabel cLabel3 = new CLabel();
  CTextField pcc_fecpedE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel cLabel5 = new CLabel();
  CTextField pcc_fecrecE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel pcc_fecconL=new CLabel("Conf:");
  CLabel pcc_feclisL=new CLabel("List:");
  CTextField pcc_fecconE = new CTextField(Types.DATE,"dd-MM-yy");
  CTextField pcc_feclisE = new CTextField(Types.DATE,"dd-MM-yy");
  CTextField pcl_feccadE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel cLabel4 = new CLabel();
  prvPanel prv_codiE = new prvPanel();
  CLabel cLabel6 = new CLabel();
  CLinkBox alm_codiE = new CLinkBox();
  CLabel cLabel7 = new CLabel();
  CComboBox pcc_estadE = new CComboBox();
  CLabel tra_codiL = new CLabel("Transport.");
  CLinkBox tra_codiE = new CLinkBox();
  CGridEditable jt = new CGridEditable(14)
  {
    @Override
    public void cambiaColumna(int col,int colNueva, int row)
    {
      String nombArt;
      try
      {
        if (col == 0)
        {
          if (pro_codiE.hasCambio() || nRow!=row)
          {
            nombArt = pro_codiE.getNombArt(pro_codiE.getText(),emp_codiE.getValorInt(),prv_codiE.getValorInt());
            jt.setValor(nombArt, row, 1);
//            pro_nombE.setText(nombArt);
            pro_codiE.resetCambio();
            nRow=row;
          }
        }
      }
      catch (Exception k)
      {
        Error("Error al buscar Nombre Articulo", k);
      }
    }

        @Override
    public int cambiaLinea(int row, int col)
    {
      return checkJt(row);
    }

        @Override
    public boolean afterInsertaLinea(boolean insLinea)
    {
      try
      {
        pcl_feccadE.setText(Formatear.sumaDias(pcc_fecrecE.getText(),
                                               "dd-MM-yyyy", NDIACAD));
        jt.setValor(pcl_feccadE.getText(), 2);
      }
      catch (ParseException k)
      {
        Error("Error en afterInsertaLinea", k);
      }
      return true;
    }

        @Override
    public void afterCambiaLinea()
    {
      try {
        pro_codiE.resetCambio();
      } catch (Exception k)
      {
        Error("Error en AfterCambiaLinea",k);
        return;
      }
      calcSum();
    }
  };

//  CButton Baceptar = new CButton();
//  CButton Bcancelar = new CButton();
  
  CLabel cLabel8 = new CLabel();
  CTextField npiTotE = new CTextField(Types.DECIMAL,"###9");
  CLabel cLabel9 = new CLabel();
  CTextField nprTotE = new CTextField(Types.DECIMAL,"###9");
  CLabel pcc_comenL = new CLabel("Comentario");
  CPanel Ptotal = new CPanel();
  CTextField pcc_comenE = new CTextField(Types.CHAR,"X",256);
  proPanel pro_codiE = new proPanel();
  CTextField pro_nombE = new CTextField(Types.CHAR,"X",60);
  CTextField pcl_nucapeE = new CTextField(Types.DECIMAL,"###9");
  CTextField pcl_cantpeE = new CTextField(Types.DECIMAL,"###,##9");
  CTextField pcl_precpeE = new CTextField(Types.DECIMAL,"###,##9.99");
  CTextField pcl_nucacoE = new CTextField(Types.DECIMAL, "###9");
  CTextField pcl_cantcoE = new CTextField(Types.DECIMAL, "###,##9");
  CTextField pcl_preccoE = new CTextField(Types.DECIMAL, "###,##9.99");
  CTextField pcl_nucafaE = new CTextField(Types.DECIMAL, "###9");
  CTextField pcl_cantfaE = new CTextField(Types.DECIMAL, "###,##9");
  CTextField pcl_precfaE = new CTextField(Types.DECIMAL, "###,##9.99");
  CTextField pcl_comenE = new CTextField(Types.CHAR, "X",50);
  CComboBox div_codiE = new CComboBox();
  CPanel Palbar = new CPanel();
  CLabel cLabel11 = new CLabel();
  CTextField acc_anoE = new CTextField(Types.DECIMAL,"###9");
  CLabel cLabel12 = new CLabel();
  CTextField acc_numeE = new CTextField(Types.DECIMAL,"####9");
  CTextField acc_serieE = new CTextField(Types.CHAR,"X",1);
  CButton Birgrid = new CButton();
  CLabel cLabel13 = new CLabel();
  CTextField kgTotE = new CTextField(Types.DECIMAL,"##,##9.9");
  CLabel cLabel14 = new CLabel();
  CTextField impTotE = new CTextField(Types.DECIMAL,"###,##9.9");
  CComboBox pcc_tiplistE = new CComboBox();
//  CLabel cLabel15 = new CLabel();
//  CCheckBox opPedi = new CCheckBox();
//  CCheckBox opConf = new CCheckBox();
//  CCheckBox opFact = new CCheckBox();
  CButton Blistar = new CButton(Iconos.getImageIcon("print"));
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CLabel pcc_estrecL = new CLabel();
  CComboBox pcc_estrecE = new CComboBox();
  CLabel cLabel17 = new CLabel();
  CTextField pcc_impporE = new CTextField(Types.DECIMAL,"##9.999");
  CCheckBox opPrecios = new CCheckBox();
  CLabel cLabel18 = new CLabel();
  empPanel emp_codiE = new empPanel();
  CLabel pcc_portesL = new CLabel("Portes");
  CComboBox pcc_portesE = new CComboBox();
  CLabel cLabel110 = new CLabel();
  sbePanel sbe_codiE = new sbePanel();

  public pdpedco(EntornoUsuario eu, Principal p)
  {
    this(eu,p,null);
  }
  public pdpedco(EntornoUsuario eu, Principal p,Hashtable<String,String> ht)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    try
    {
      ponParametros(ht);
     
      setTitulo("Mantenimiento Pedidos Compras");


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

  public pdpedco(gnu.chu.anjelica.menu p, EntornoUsuario eu)
  {
    this(p,eu,null);
  }
  public pdpedco(gnu.chu.anjelica.menu p, EntornoUsuario eu,Hashtable<String,String> ht)
  {
    EU = eu;
    vl = p.getLayeredPane();
    eje = false;

    try
    {
      ponParametros(ht);
      if (ht != null)
      {
        if (ht.get("admin") != null)
          SWADMIN = Boolean.parseBoolean(ht.get("admin").toString());
      }
      setTitulo("Mantenimiento Pedidos Compras");

      jbInit();
    }
    catch (Exception e)
    {
      ErrorInit(e);
    }
  }
  private void ponParametros(Hashtable<String,String> ht)
  {
      if (ht != null)
      {
        if (ht.get("admin") != null)
          SWADMIN = Boolean.valueOf(ht.get("admin"));

      }
  }
  private void jbInit() throws Exception
  {
    iniciarFrame();
    this.setSize(new Dimension(760,522));
    this.setVersion("2017-07-09"+ (SWADMIN?"(ADMINISTRADOR)":""));

    Pprinc.setLayout(gridBagLayout1);

    statusBar = new StatusBar(this);
    nav = new navegador(this, dtCons, false, navegador.NORMAL);
    ponItemsEstado(pcc_estadE);
    pcc_estrecE.addItem("Pendiente","P");
    pcc_estrecE.addItem("Cancelado","C");
    pcc_estrecE.addItem("Recibido","R");
    pcc_estrecE.addItem("Bloqueado","B");

    pcc_portesE.addItem("Pagados","P");
    pcc_portesE.addItem("Debidos","D");
    conecta();
    iniciar(this);
    Pcabe.setBorder(BorderFactory.createRaisedBevelBorder());
    Pcabe.setMaximumSize(new Dimension(720, 120));
    Pcabe.setMinimumSize(new Dimension(720, 120));
    Pcabe.setPreferredSize(new Dimension(720, 120));
    npiTotE.setEnabled(false);
    npiTotE.setBounds(new Rectangle(187, 3, 44, 19));
    nprTotE.setEnabled(false);
    kgTotE.setEnabled(false);
    impTotE.setEnabled(false);
    Pcabe.setLayout(null);
    cLabel1.setText("N. Pedido");
    cLabel1.setBounds(new Rectangle(310, 5, 59, 18));
    cLabel2.setText("Ejerc.");
    cLabel2.setBounds(new Rectangle(223, 5, 38, 18));
    cLabel3.setToolTipText("");
    cLabel3.setText("Fec.Pedido");
    cLabel3.setBounds(new Rectangle(431, 5, 61, 18));
    cLabel5.setText("Fec. Recep.");
    cLabel5.setBounds(new Rectangle(576, 5, 65, 18));
    cLabel4.setText("Proveedor");
    cLabel4.setBounds(new Rectangle(5, 28, 59, 19));
    prv_codiE.setAncTexto(40);
    prv_codiE.setBounds(new Rectangle(64, 27, 327, 18));
    cLabel6.setText("Almacen");
    cLabel6.setBounds(new Rectangle(5, 54, 57, 18));

    alm_codiE.setAncTexto(30);
    alm_codiE.setBounds(new Rectangle(55, 53, 195, 18));
    cLabel7.setToolTipText("");
    cLabel7.setText("Est.Ped");
    cLabel7.setBounds(new Rectangle(396, 27, 49, 18));
   
    cLabel8.setVerifyInputWhenFocusTarget(true);
    cLabel8.setText("N.Piezas");
    cLabel8.setBounds(new Rectangle(130, 3, 59, 19));
    cLabel9.setToolTipText("");
    cLabel9.setText("N.Productos");
    cLabel9.setBounds(new Rectangle(3, 3, 81, 19));
    nprTotE.setTipoCampo(3);
    nprTotE.setBounds(new Rectangle(81, 3, 44, 19));
    
    
    Ptotal.setBorder(BorderFactory.createLoweredBevelBorder());
    Ptotal.setMaximumSize(new Dimension(500, 48));
    Ptotal.setMinimumSize(new Dimension(500, 48));
    Ptotal.setPreferredSize(new Dimension(500, 48));
    Ptotal.setQuery(false);
    Ptotal.setLayout(null);
    Palbar.setBorder(BorderFactory.createLoweredBevelBorder());
    Palbar.setBounds(new Rectangle(275, 50, 262, 25));
    Palbar.setLayout(null);

    cLabel11.setText("Ejercicio");
    cLabel11.setBounds(new Rectangle(2, 3, 55, 18));
    acc_anoE.setBounds(new Rectangle(53, 3, 50, 18));
    cLabel12.setText("N.Albaran");
    cLabel12.setBounds(new Rectangle(108, 3, 65, 18));
    acc_numeE.setBounds(new Rectangle(195, 3, 57, 18));
    acc_serieE.setBounds(new Rectangle(169, 3, 22, 18));
    pcc_comenL.setBounds(new Rectangle(3, 73, 69, 18));
    pcc_comenE.setBounds(new Rectangle(74, 73, 472, 18));
    pcc_portesL.setBounds(new Rectangle(560, 73, 44, 18));
    pcc_portesE.setBounds(new Rectangle(620, 73, 93, 18));
    tra_codiL.setBounds(new Rectangle(3, 93, 69, 18));
    tra_codiE.setBounds(new Rectangle(74, 93, 350, 18));
    tra_codiE.setAncTexto(45);
    tra_codiE.setFormato(Types.DECIMAL,"###9");
    cLabel13.setText("Kilos");
    cLabel13.setBounds(new Rectangle(240, 3, 36, 19));
    kgTotE.setBounds(new Rectangle(271, 3, 70, 19));
    cLabel14.setText("Importe");
    cLabel14.setBounds(new Rectangle(345, 3, 55, 19));
    impTotE.setBounds(new Rectangle(394, 3, 97, 19));
    ponItemsEstado(pcc_tiplistE);
    pcc_tiplistE.setPreferredSize(new Dimension(100,100));
    pcc_tiplistE.setBounds(new Rectangle(4, 25, 80, 17));
//    cLabel15.setBackground(Color.blue);
//    cLabel15.setForeground(Color.cyan);
//    cLabel15.setMaximumSize(new Dimension(48, 17));
//    cLabel15.setOpaque(true);
//    cLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
//    cLabel15.setText("Listar");
//    cLabel15.setBounds(new Rectangle(4, 25, 43, 14));
//    opPedi.setMargin(new Insets(0, 0, 0, 0));
//    opPedi.setText("Pedido");
//    opPedi.setBounds(new Rectangle(52, 25, 64, 14));
//    opConf.setMargin(new Insets(0, 0, 0, 0));
//    opConf.setSelectedIcon(null);
//    opConf.setText("Confirmado");
//    opConf.setBounds(new Rectangle(120, 25, 97, 14));
//    opFact.setMargin(new Insets(0, 0, 0, 0));
//    opFact.setText("Facturado");
//    opFact.setBounds(new Rectangle(216, 25, 82, 14));
    Blistar.setBounds(new Rectangle(88, 23, 87, 22));
    Blistar.setText("Listar");
    opPrecios.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    opPrecios.setBounds(new Rectangle(180, 25, 60, 14));
    
    pcc_fecconL.setBounds(new Rectangle(242, 25, 40, 17));
    pcc_fecconE.setBounds(new Rectangle(285, 25, 60, 17));
    pcc_feclisL.setBounds(new Rectangle(350, 25, 40, 17));
    pcc_feclisE.setBounds(new Rectangle(403, 25, 60, 17));

    jt.setMaximumSize(new Dimension(745, 276));
    jt.setMinimumSize(new Dimension(745, 276));
    jt.setPreferredSize(new Dimension(745, 276));
    Baceptar.setMaximumSize(new Dimension(111, 31));
    Baceptar.setMinimumSize(new Dimension(111, 31));
    Baceptar.setPreferredSize(new Dimension(111, 31));
    Bcancelar.setMaximumSize(new Dimension(111, 31));
    Bcancelar.setMinimumSize(new Dimension(111, 31));
    Bcancelar.setPreferredSize(new Dimension(111, 31));
    pcc_estadE.setBounds(new Rectangle(446, 26, 98, 20));
    pcc_estrecL.setText("Est.Recep.");
    pcc_estrecL.setBounds(new Rectangle(550, 51, 75, 18));

    Birgrid.setBounds(new Rectangle(696, 63, 1, 1));
    pcc_estrecE.setBounds(new Rectangle(620, 51, 93, 18));
    pcc_estadE.setBounds(new Rectangle(445, 27, 98, 18));
    pcc_fecrecE.setBounds(new Rectangle(636, 5, 77, 18));
    pcc_fecpedE.setBounds(new Rectangle(493, 5, 82, 18));
    pcc_numeE.setBounds(new Rectangle(369, 5, 57, 18));
    eje_numeE.setBounds(new Rectangle(262, 5, 44, 18));
    cLabel17.setRequestFocusEnabled(true);
    cLabel17.setText("Imp.Portes p/kilo");
    cLabel17.setBounds(new Rectangle(548, 27, 104, 18));
    pcc_impporE.setToolTipText("Importe Portes (por kilo)");
    pcc_impporE.setBounds(new Rectangle(651, 27, 62, 18));
    opPrecios.setMargin(new Insets(0, 0, 0, 0));
    opPrecios.setSelected(true);
    opPrecios.setText("Precios");
   
    cLabel18.setText("Emp.");
    cLabel18.setBounds(new Rectangle(1, 5, 33, 16));

    emp_codiE.setBounds(new Rectangle(31, 5, 58, 18));
    
    cLabel110.setBounds(new Rectangle(101, 5, 55, 16));
    cLabel110.setText("SubEmp.");
    sbe_codiE.setBounds(new Rectangle(157, 5, 58, 18));
     ArrayList v=new ArrayList();
    v.add("Producto"); // 0
    v.add("Nombre");// 1
    v.add("Fec.Cad");// 2
    v.add("Un.Ped.");// 3
    v.add("KG Ped.");// 4
    v.add("Prec.Ped.");// 5
    v.add("Un.Conf.");// 6
    v.add("KG Conf."); // 7
    v.add("Prec.Conf."); // 8
    v.add("Unid.Fra."); // 9
    v.add("KG Fra."); // 10
    v.add("Prec.Fra."); // 11
    v.add("Mon.");// 12
    v.add("Coment");// 13
    jt.setCabecera(v);
    jt.setAnchoColumna(new int[]{60,200,80,46,60,55,60,60,70,60,57,55,80,150});
    jt.setAlinearColumna(new int[]{2,0,1,2,2,2,2,2,2,2,2,2,1,0});
    ArrayList vc=new ArrayList();
    pro_codiE.setProNomb(null);
    div_codiE.resetTexto();
    pcl_nucapeE.setToolTipText("Introduzca Unidades  a pedir");
    pcl_nucacoE.setToolTipText("Introduzca Unidades Confirmada de Compras");
    pcl_nucafaE.setToolTipText("Introduzca Unidades PreFacturada");
    vc.add(pro_codiE.getTextField()); // 0
    vc.add(pro_nombE); // 1
    vc.add(pcl_feccadE); // 2
    vc.add(pcl_nucapeE); // 3
    vc.add(pcl_cantpeE); // 4
    vc.add(pcl_precpeE);  // 5
    vc.add(pcl_nucacoE); // 6
    vc.add(pcl_cantcoE); // 7
    vc.add(pcl_preccoE); // 8
    vc.add(pcl_nucafaE); // 9
    vc.add(pcl_cantfaE); // 10
    vc.add(pcl_precfaE); // 11
    vc.add(div_codiE); // 12
    vc.add(pcl_comenE); //13
    jt.setCampos(vc);
    jt.setAjustarGrid(true);
    jt.setAjustarColumnas(false);

    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
 
    Ptotal.add(cLabel13, null);
    Ptotal.add(kgTotE, null);
    Ptotal.add(cLabel14, null);
    Ptotal.add(impTotE, null);
    Ptotal.add(nprTotE, null);
    Ptotal.add(cLabel9, null);
    Ptotal.add(npiTotE, null);
    Ptotal.add(cLabel8, null);
    
    Pprinc.add(Pcabe,   new GridBagConstraints(0, 0, 3, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    Pprinc.add(jt,  new GridBagConstraints(0, 1, 3, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 1, 0, 0), 0, 0));
    Pprinc.add(Ptotal,   new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 1, 0, 0), 0, 0));
    Pcabe.add(pcc_comenL, null);
    Pcabe.add(pcc_comenE, null);
    Pcabe.add(pcc_portesL, null);
    Pcabe.add(pcc_portesE, null);
    Pcabe.add(tra_codiL, null);
    Pcabe.add(tra_codiE, null);
    Palbar.add(cLabel11, null);
    Palbar.add(cLabel12, null);
    Palbar.add(acc_anoE, null);
    Palbar.add(acc_numeE, null);
    Palbar.add(acc_serieE, null);
  
    Pcabe.add(Palbar, null);
    Pcabe.add(cLabel18, null);
    Pcabe.add(emp_codiE, null);

    Pcabe.add(cLabel5, null);
    Pcabe.add(pcc_fecpedE, null);
    Pcabe.add(cLabel3, null);
    Pcabe.add(pcc_numeE, null);
    Pcabe.add(cLabel1, null);
    Pcabe.add(eje_numeE, null);
    Pcabe.add(cLabel2, null);
    Pcabe.add(pcc_impporE, null);
    Pcabe.add(pcc_fecrecE, null);
    Pcabe.add(pcc_estadE, null);
    Pcabe.add(cLabel7, null);
    Pcabe.add(Birgrid, null);
    Pcabe.add(alm_codiE, null);
    Pcabe.add(cLabel6, null);
    Pcabe.add(prv_codiE, null);
    Pcabe.add(cLabel4, null);
    Pcabe.add(cLabel17, null);
    Pcabe.add(pcc_estrecE, null);
    Pcabe.add(pcc_estrecL, null);
    Pcabe.add(sbe_codiE, null);
    Pcabe.add(cLabel110, null);
    Ptotal.add(pcc_tiplistE, null);
    Ptotal.add(Blistar, null);
    Ptotal.add(opPrecios, null);
    Ptotal.add(pcc_fecconL, null);
    Ptotal.add(pcc_fecconE, null);
    Ptotal.add(pcc_feclisL, null);
    Ptotal.add(pcc_feclisE, null);

    Pprinc.add(Bcancelar,   new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 9, 0, 8), 0, 0));
    Pprinc.add(Baceptar,   new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
    Baceptar.setText("Aceptar (F4)");
  }
  public static String getNombreClase()
  {
      return "gnu.chu.anjelica.compras.pdpedco";
  }
  public boolean inTransation()
  {
      return nav.isEdicion();
  }
  public void setPedido(int pccNume)
  {
      pcc_numeE.setValorInt(pccNume);
  }
  public void setEjercicio(int ejeNume)
  {
      eje_numeE.setValorInt(ejeNume);
  }
  @Override
  public void iniciarVentana() throws Exception
  {
    Pcabe.setButton(KeyEvent.VK_F4,Baceptar);
    Pcabe.setButton(KeyEvent.VK_F2,Birgrid);
    jt.setButton(KeyEvent.VK_F4,Baceptar);


    emp_codiE.setColumnaAlias("emp_codi");
    sbe_codiE.setColumnaAlias("sbe_codi");
    eje_numeE.setColumnaAlias("eje_nume");
    prv_codiE.setColumnaAlias("prv_codi");
    pcc_numeE.setColumnaAlias("pcc_nume");
    prv_codiE.setColumnaAlias("prv_codi");
    pcc_fecpedE.setColumnaAlias("pcc_fecped");
    pcc_fecrecE.setColumnaAlias("pcc_fecrec");
    alm_codiE.setColumnaAlias("alm_codi");
    pcc_estadE.setColumnaAlias("pcc_estad");
    pcc_estrecE.setColumnaAlias("pcc_estrec");
    pcc_comenE.setColumnaAlias("pcc_comen");
    acc_numeE.setColumnaAlias("acc_nume");
    acc_anoE.setColumnaAlias("acc_ano");
    acc_serieE.setColumnaAlias("acc_serie");
    pcc_impporE.setColumnaAlias("pcc_imppor");
    pcc_portesE.setColumnaAlias("pcc_portes");
    pcc_fecconE.setColumnaAlias("pcc_feccon");
    pcc_feclisE.setColumnaAlias("pcc_feclis");
    tra_codiE.setColumnaAlias("tra_codi");
    activarEventos();
    verDatos(dtCons);
  }
  
  void ponItemsEstado(CComboBox combo)
  {
    combo.addItem("Pedido", "P");
    combo.addItem("Confirmado", "C");
    combo.addItem("PreFacturado", "F");
    combo.resetTexto();
  }
  void activarEventos()
  {
    Birgrid.addFocusListener(new FocusAdapter() {
      public void focusGained(FocusEvent e)
      {
        irGrid();
      }
    });
    Blistar.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Blistar_actionPerformed();
      }
    });
  }

  void irGrid()
  {
    try   {
      if (! checkCabec())
        return;


    jt.setEnabled(true);
    jt.requestFocusInicio();
    pro_codiE.resetCambio();
    pcl_feccadE.setText(Formatear.sumaDias(pcc_fecrecE.getText(),
                                           "dd-MM-yyyy", NDIACAD));
    jt.setValor(pcl_feccadE.getText(), 2);
    } catch (Exception k){
       Error("Error en irGrid",k);
     }

  }

  private boolean checkCabec() throws ParseException, SQLException
  {
    if (! emp_codiE.controla())
    {
      mensajeErr("Empresa NO VALIDA");
      return false;
    }
    if (! sbe_codiE.controla())
    {
      mensajeErr("Subempresa NO VALIDA");
      return false;
    }

    if (! prv_codiE.controla(true))
    {
      mensajeErr("Introduzca un Proveedor valido");
      return false;
    }
    if (pcc_fecpedE.getError() || pcc_fecpedE.isNull())
    {
      mensajeErr("Fecha Pedido NO es Valida");
      pcc_fecpedE.requestFocus();
      return false;
    }
    if (pcc_fecrecE.getError() || pcc_fecrecE.isNull())
    {
      mensajeErr("Fecha Recepcion NO valida");
      pcc_fecrecE.requestFocus();
      return false;
    }
    if (Formatear.comparaFechas(pcc_fecrecE.getDate(),pcc_fecpedE.getDate())<0)
    {
      mensajeErr("Fecha Recepcion NO puede ser inferior a la del Pedido");
      pcc_fecpedE.requestFocus();
      return false;
    }
    if (alm_codiE.getError())
    {
      mensajeErr("Almacen NO valido");
      alm_codiE.requestFocus();
      return false;
    }
    if (!tra_codiE.controla() || tra_codiE.isNull())
    {
      mensajeErr("Transportista NO Valido");
      tra_codiE.requestFocus();
      return false;
    }
    return true;
  }
  @Override
  public void PADPrimero()
  {
    verDatos(dtCons);
     nav.pulsado=navegador.NINGUNO;
  }
  @Override
  public void PADAnterior()
  {
    verDatos(dtCons);
    nav.pulsado=navegador.NINGUNO;
  }
    @Override
  public void PADSiguiente()
  {
    verDatos(dtCons);
    nav.pulsado=navegador.NINGUNO;
  }
  @Override
  public void PADUltimo()
  {
    verDatos(dtCons);
    nav.pulsado=navegador.NINGUNO;
  }
  @Override
  public void PADQuery()
  {
    activar(true);
    jt.setEnabled(false);
    mensaje("Buscar datos ...");
    Pcabe.setQuery(true);
    Pcabe.resetTexto();
    eje_numeE.setValorDec(EU.ejercicio);
    emp_codiE.setText(""+EU.em_cod);
    pcc_fecconE.setEnabled(true);
    pcc_feclisE.setEnabled(true);
    
    if (EU.getSbeCodi()!=0)
      sbe_codiE.setValorInt(EU.getSbeCodi());
    acc_numeE.requestFocus();
  }
  @Override
  public void ej_query1()
  {
    Component c = Pcabe.getErrorConf();
    if (c != null)
    {
      c.requestFocus();
      mensajeErr("Error en condiciones Busqueda");
      return;
    }
    ArrayList v = new ArrayList();
    v.add(emp_codiE.getStrQuery());
    v.add(sbe_codiE.getStrQuery());
    v.add(eje_numeE.getStrQuery());
    v.add(prv_codiE.getStrQuery());
    v.add(pcc_numeE.getStrQuery());
    v.add(prv_codiE.getStrQuery());
    v.add(pcc_fecpedE.getStrQuery());
    v.add(pcc_fecrecE.getStrQuery());
    v.add(alm_codiE.getStrQuery());
    v.add(pcc_portesE.getStrQuery());    
    v.add(tra_codiE.getStrQuery());    
    v.add(pcc_estadE.getStrQuery());
    v.add(pcc_estrecE.getStrQuery());
    v.add(pcc_comenE.getStrQuery());
    v.add(pcc_impporE.getStrQuery());
    v.add(pcc_fecconE.getStrQuery());
    v.add(pcc_feclisE.getStrQuery());
    try
    {
      Pcabe.setQuery(false);
      s = "SELECT * FROM pedicoc ";
      s = creaWhere(s, v,true);
      s += " ORDER BY emp_codi,eje_nume,pcc_nume";
      nav.pulsado=navegador.NINGUNO;
      if (!dtCons.select(s))
      {
        mensaje("");
        mensajeErr("No encontrados PEDIDOS para estos criterios");
        activaTodo();
        rgSelect();
        return;
      }
      
      mensaje("");
      strSql = s;
      rgSelect();
      mensajeErr("Nuevos registros selecionados");
    }
    catch (Exception k)
    {
      Error("Error al buscar datos", k);
    }
    activaTodo();
   
  }
  @Override
  public void canc_query()
  {
    nav.pulsado=navegador.NINGUNO;
    mensajeErr("Consulta ... CANCELADA");
    mensaje("");
    Pcabe.setQuery(false);
    activaTodo();
    verDatos(dtCons);
  }
  @Override
  public void PADEdit()
  {
    try {
      nRow=0;
      if (dtCons.getNOREG())
      {
        mensajeErr("Sin registros para editar");
        activaTodo();
        return;
      }
      if (pcc_estrecE.getValor().equals("R"))
        msgBox("ATENCION: Ya se recibio albaran sobre el pedido");
      
      if (pcc_estrecE.getValor().equals("B") && !SWADMIN)
      {
          mensajeErr("Pedido Bloqueado. Imposible Modificar o borrar");
          activaTodo();
          return;
       }
      // Bloqueo el Registro
      s="SELECT * FROM pedicoc WHERE emp_codi = "+emp_codiE.getValorInt()+
                   " and eje_nume = "+eje_numeE.getValorInt()+
                   " and pcc_nume = "+pcc_numeE.getValorInt();
      dtAdd.select(s,true);

      activar(navegador.EDIT, true);
      eje_numeE.setEnabled(false);
      pcc_numeE.setEnabled(false);
      emp_codiE.setEnabled(false);
      pcc_estadE.setValor(dtCons.getString("pcc_estad"));
      jt.setEnabled(true);
      jt.requestFocusInicio();
    } catch (Exception k)
    {
      Error("Error al Editar Registrro",k);
    }
    mensaje("Editando .... PEDIDO");
  }
  @Override
  public void ej_edit1()
  {
    try  {
      
      int nRow=jt.getRowCount();
      if (! checkCampos())
        return;
      jt.salirGrid();
      s="SELECT * FROM pedicoc WHERE emp_codi = "+emp_codiE.getValorInt()+
                   " and eje_nume = "+eje_numeE.getValorInt()+
                   " and pcc_nume = "+pcc_numeE.getValorInt();
      if (pcc_fecconE.isNull() && pcc_estadE.getValor().equals("C"))
          pcc_fecconE.setDate(Formatear.getDateAct());
      dtAdd.select(s,true);
      dtAdd.edit(dtAdd.getCondWhere());
      guardaDatCab(dtAdd);
      int nLin=0;
      s="DELETE FROM pedicol WHERE emp_codi = "+emp_codiE.getValorInt()+
                   " and eje_nume = "+eje_numeE.getValorInt()+
                   " and pcc_nume = "+pcc_numeE.getValorInt();
      stUp.executeUpdate(s);
      dtAdd.addNew("pedicol");
      for (int n=0;n<nRow;n++)
      {
        if (jt.getValorInt(n, 0) == 0)
          continue;

        guardaLinPed(dtAdd,nLin++,n);
      }
      ctUp.commit();
      nav.pulsado=navegador.NINGUNO;
      activaTodo();
      mensajeErr("Pedido .. MODIFICADO");
      mensaje("");
      pcc_tiplistE.setValor(pcc_estadE.getValor());
    } catch (SQLException | ParseException k)
    {
      Error("Error al Editar Registro",k);
    }
  }

  private void guardaLinPed(DatosTabla dt, int nLin, int n) throws SQLException, java.text.ParseException
  {
    dtAdd.addNew();
    dt.setDato("emp_codi", emp_codiE.getValorInt());
    dt.setDato("eje_nume", eje_numeE.getValorInt());
    dt.setDato("pcc_nume", pcc_numeE.getValorInt());
    dt.setDato("prv_codi", prv_codiE.getValorInt());
    dt.setDato("pcl_numli", nLin++);
    dt.setDato("pro_codi", jt.getValorInt(n, 0));
    dt.setDato("pro_nomb", jt.getValString(n, 1));

    dt.setDato("pcL_feccad", jt.getValString(n, 2),"dd-MM-yyyy");
    dt.setDato("pcl_nucape", jt.getValorDec(n, 3));
    dt.setDato("pcl_cantpe", jt.getValorDec(n, 4));
    dt.setDato("pcl_precpe", jt.getValorDec(n, 5));
    dt.setDato("div_codi",  div_codiE.getValor(jt.getValString(n, 12)) == null ? "1" :
               div_codiE.getValor(jt.getValString(n, 12)));
    dt.setDato("pcl_nucaco", jt.getValorDec(n, 6));
    dt.setDato("pcl_cantco", jt.getValorDec(n, 7));
    dt.setDato("pcl_precco", jt.getValorDec(n, 8));
    dt.setDato("pcl_nucafa", jt.getValorDec(n, 9));
    dt.setDato("pcl_cantfa", jt.getValorDec(n, 10));
    dt.setDato("pcl_precfa", jt.getValorDec(n, 11));
    dt.setDato("pcl_comen", jt.getValString(n, 13));
    dt.update(stUp);
  }
  @Override
  public void canc_edit()
  {
    nav.pulsado=navegador.NINGUNO;
    activaTodo();
    mensajeErr("Modificacion de Pedido ... CANCELADA");
    mensaje("");
    verDatos(dtCons);
  }
    @Override
  public void PADAddNew()
  {
    nRow=-1;
    Pcabe.resetTexto();
    pcc_fecconE.resetTexto();
    pcc_feclisE.resetTexto();
    pcc_comenE.resetTexto();
    jt.removeAllDatos();
    jt.requestFocusInicio();
    activar(navegador.ADDNEW,true);
    pcc_estadE.setValor("P");
    pcc_estrecE.setValor("P");
    pcc_estadE.setEnabled(false);
    eje_numeE.setEnabled(false);
    emp_codiE.setEnabled(true);
    pcc_numeE.setEnabled(false);
    pcc_portesE.setValor("P");  
    mensaje("Crear nuevo pedido ...");
    eje_numeE.setValorDec(EU.ejercicio);
    emp_codiE.setValorInt(EU.em_cod);
    sbe_codiE.setValorInt(EU.getSbeCodi()==0?1:EU.getSbeCodi());
    pro_codiE.resetTexto();
    pro_codiE.resetCambio();
    pcc_estrecE.setEnabled(false);
    pcc_fecpedE.setDate(Formatear.getDateAct());

    pcc_fecrecE.requestFocus();
  }
  @Override
  public void ej_addnew1()
  {
    try
    {
//      debug("Addnew");
      int nRow=jt.getRowCount();
      if (! checkCampos())
        return;

      jt.salirGrid();
      s="SELECT num_pedcom FROM v_numerac WHERE eje_nume = "+eje_numeE.getValorInt()+
          " AND emp_codi = "+emp_codiE.getValorInt();
      if (!dtAdd.select(s,true))
        throw new Exception("No encontrado guia de NUMERACIONES\n"+s);
      if (pcc_estadE.getValor().equals("C"))
          pcc_fecconE.setDate(Formatear.getDateAct());
      pcc_numeE.setValorDec(dtAdd.getInt("num_pedcom",true)+1);
      dtAdd.edit();
//      dtAdd.setDato("num_pedcom",pcc_numeE.getValorInt());
      dtAdd.setDato("num_pedcom",dtAdd.getInt("num_pedcom",true)+1);
      dtAdd.update(stUp);

      dtAdd.addNew("pedicoc");
      dtAdd.setDato("eje_nume",eje_numeE.getValorInt());
      dtAdd.setDato("emp_codi",emp_codiE.getValorInt());
      dtAdd.setDato("sbe_codi",sbe_codiE.getValorInt());
      dtAdd.setDato("pcc_nume",pcc_numeE.getValorInt());
      dtAdd.setDato("pcc_subjec",(String) null);
      guardaDatCab(dtAdd);
      int nLin=0;
      dtAdd.addNew("pedicol");
      for (int n=0;n<nRow;n++)
      {
        if (jt.getValorInt(n, 0) == 0)
          continue;
        guardaLinPed(dtAdd,nLin++,n);
      }
      ctUp.commit();
      nav.pulsado=navegador.NINGUNO;
      activaTodo();
      if (dtCons.getNOREG())
        rgSelect();
      mensajeErr("Pedido .. INSERTADO");
      mensaje("");
      pcc_tiplistE.setValor(pcc_estadE.getValor());
    } catch (Exception k)
    {
      Error("ERROR AL insertar NUEVOS DATOS",k);
    }
  }

  private void guardaDatCab(DatosTabla dt) throws SQLException, java.text.ParseException
  {
 
    dt.setDato("prv_codi", prv_codiE.getValorInt());
    dt.setDato("pcc_fecped", pcc_fecpedE.getText(), "dd-MM-yyyy");
    dt.setDato("pcc_fecrec", pcc_fecrecE.getText(), "dd-MM-yyyy");
    dt.setDato("alm_codi", alm_codiE.getValorInt());
    dt.setDato("pcc_estad", pcc_estadE.getValor());
    dt.setDato("pcc_estrec", pcc_estrecE.getValor());
    dt.setDato("pcc_imppor", pcc_impporE.getValorDec());
    dt.setDato("pcc_comen", pcc_comenE.getText());
    dt.setDato("pcc_portes", pcc_portesE.getValor());
    dt.setDato("tra_codi", tra_codiE.getValorInt());
    dt.setDato("pcc_feccon", pcc_fecconE.getDate());
    dtAdd.update(stUp);
  }
  private boolean checkCampos() throws SQLException, ParseException
  {
    if (!checkCabec())
      return false;
    if (pcc_estrecE.getValor().equals("R") && (nav.pulsado==navegador.ADDNEW || !SWADMIN) )
    {
        mensajeErr("No se puede establecer un Pedido a RECEPCIONADO");
        pcc_estrecE.requestFocus();
        return false;
    }
    if (pcc_fecrecE.isNull())
    {
      mensajeErr("Introduzca una fecha Prevista Recepcion de Pedido");
      pcc_fecrecE.requestFocus();
      return false;
    }
    jt.actualizarGrid();
    if (checkJt(jt.getSelectedRow()) >= 0)
    {
      jt.requestFocusSelected();
      return false;
    }
    int nRow = jt.getRowCount();
    boolean swVacio = true;
    char tipo='P';
    for (int n = 0; n < nRow; n++)
    {
      if (jt.getValorInt(n, 0) != 0)
        swVacio = false;
      if (tipo!='C' && tipo!='F')
        if (jt.getValorDec(n,6)!=0 || jt.getValorDec(n,7)!=0)
          tipo='C';

      if (jt.getValorDec(n,9)!=0 || jt.getValorDec(n,10)!=0)
        tipo='F';
    }
    if (swVacio)
    {
      mensajeErr("Introduzca Alguna linea de pedido");
      return false;
    }
    if (tipo!=pcc_estadE.getValor().charAt(0))
    {
      pcc_estadE.setValor(""+tipo);
      mensajeErr("Tipo Pedido en estado: "+pcc_estadE.getText());
      if (nav.pulsado!=navegador.ADDNEW)
        return false;
    }
    return true;
  }
@Override
  public void canc_addnew()
  {
    nav.pulsado=navegador.NINGUNO;
    activaTodo();
    mensajeErr("Insercion de Pedido ... CANCELADA");
    mensaje("");
    verDatos(dtCons);
  }
    @Override
  public void PADDelete()
  {
    if (dtCons.getNOREG())
    {
      mensajeErr("Sin registros para BORRAR");
      activaTodo();
      return;
    }

    if (pcc_estrecE.getValor().equals("R"))
    {
      mensajeErr("Ya se ha recibido un pedido SOBRE el Albaran .. Imposible Borrarlo");
      activaTodo();
      return;
    }
    if (pcc_estrecE.getValor().equals("B"))
    {
          mensajeErr("Pedido Bloqueado. Imposible Modificar o borrar");
          activaTodo();
          return;
    }
    mensaje("Borrar registro ....");
    Baceptar.setEnabled(true);
    Bcancelar.setEnabled(true);
    Bcancelar.requestFocus();
  }

  @Override
  public void ej_delete1()
  {
    try
    {
      s = "DELETE FROM pedicoc WHERE emp_codi = " + emp_codiE.getValorInt()+
          " and eje_nume = " + eje_numeE.getValorInt() +
          " and pcc_nume = " + pcc_numeE.getValorInt();

      stUp.executeUpdate(s);
      int nLin = 0;
      s = "DELETE FROM pedicol WHERE emp_codi = " +emp_codiE.getValorInt()+
          " and eje_nume = " + eje_numeE.getValorInt() +
          " and pcc_nume = " + pcc_numeE.getValorInt();
      stUp.executeUpdate(s);
      ctUp.commit();
      nav.pulsado=navegador.NINGUNO;
      activaTodo();
      rgSelect();
      mensajeErr("Registro .. BORRADO");
      mensaje("");
    } catch (Exception k)
    {
      Error("Error al borrar Pedido",k);
    }
  }
  @Override
  public void canc_delete()
  {
    nav.pulsado=navegador.NINGUNO;
    activaTodo();
    mensajeErr("Borrado de Pedido ... CANCELADO");
    mensaje("");
  }
   @Override
  public void activar(boolean b)
  {
    activar(navegador.TODOS,b);
  }
  public void activar(int modo,boolean b)
  {
    Birgrid.setEnabled(b);
    if (modo==navegador.TODOS || !b)
    {
      acc_numeE.setEnabled(b);
      acc_anoE.setEnabled(b);
      acc_serieE.setEnabled(b);
      jt.setEnabled(b);
      pro_codiE.resetCambio();
    }
    pcc_fecconE.setEnabled(false);
    pcc_feclisE.setEnabled(false);
    pcc_estrecE.setEnabled(b);
    emp_codiE.setEnabled(b);
    sbe_codiE.setEnabled(b);
    eje_numeE.setEnabled(b);
    prv_codiE.setEnabled(b);
    pcc_numeE.setEnabled(b);
    prv_codiE.setEnabled(b);
    pcc_fecpedE.setEnabled(b);
    pcc_fecrecE.setEnabled(b);
    alm_codiE.setEnabled(b);
    pcc_estadE.setEnabled(b);
    pcc_comenE.setEnabled(b);
    Baceptar.setEnabled(b);
    Bcancelar.setEnabled(b);
    pcc_impporE.setEnabled(b);
    pcc_portesE.setEnabled(b);
    tra_codiE.setEnabled(b);
  }
  public int checkJt(int row)
  {
    if (pro_codiE.isNull())
      return -1;
    try {
      if (!pro_codiE.controla(false))
      {
        mensajeErr("Codigo de Producto .. No valido");
        return 0;
      }
      if (!pro_codiE.isActivo())
      {
        mensajeErr("Producto .. No esta activo");
        return 0;
      }
      if (pcl_feccadE.isNull())
      {
        mensajeErr("Introduzca una fecha de Caducidad");
        pcl_feccadE.setText(Formatear.sumaDias(pcc_fecrecE.getText(),"dd-MM-yyyy",NDIACAD));
        jt.setValor(pcl_feccadE.getText(),row,2);
        return 2;
      }
      if (Formatear.comparaFechas(pcl_feccadE.getDate(),pcc_fecrecE.getDate())<0)
      {
        mensajeErr("Fecha Caducidad NO PUEDE ser inferior a la de Recepcion");
        pcc_fecpedE.requestFocus();
        return 2;
      }

      if (pcl_nucacoE.getValorDec()+pcl_nucapeE.getValorDec()+pcl_nucafaE.getValorDec() == 0)
//      if (pcl_cantcoE.getValorDec()+pcl_nucacoE.getValorDec()+pcl_cantpeE.getValorDec()+pcl_nucapeE.getValorDec()+
//          pcl_cantfaE.getValorDec()+pcl_nucafaE.getValorDec() == 0)
      {
        mensajeErr("Introduzca el Número de Unidades (El Peso es opcional)");
        return 0;
      }

      calcSum();
    } catch (Exception k)
    {
      Error("Error al controlar el grid",k);
    }
    return -1;
  }
  void verDatos(DatosTabla dt)
  {
    try  {
      if (dt.getNOREG())
        return;
      s="SELECT * FROM pedicoc WHERE emp_codi = "+dt.getInt("emp_codi")+
          " AND eje_nume = "+dt.getInt("eje_nume")+
          " and pcc_nume = "+dt.getInt("pcc_nume");

      if (! dtCon1.select(s))
      {

        mensajeErr("Registro NO encontrado");
        Pcabe.resetTexto();
        jt.removeAllDatos();
        emp_codiE.setText(dt.getString("emp_codi"));
        eje_numeE.setText(dt.getString("eje_nume"));
        pcc_numeE.setText(dt.getString("pcc_nume"));
        return;
      }
      emp_codiE.setText(dt.getString("emp_codi"));
      eje_numeE.setText(dt.getString("eje_nume"));
      pcc_numeE.setText(dt.getString("pcc_nume"));
      sbe_codiE.setText(dtCon1.getString("sbe_codi"));
      prv_codiE.setText(dtCon1.getString("prv_codi"),true);
      pcc_fecpedE.setText(dtCon1.getFecha("pcc_fecped","dd-MM-yyyy"));
      pcc_fecrecE.setText(dtCon1.getFecha("pcc_fecrec","dd-MM-yyyy"));
      alm_codiE.setText(dtCon1.getString("alm_codi"));
      pcc_estadE.setValor(dtCon1.getString("pcc_estad"));
      pcc_estrecE.setValor(dtCon1.getString("pcc_estrec"));
      acc_anoE.setText(dtCon1.getString("acc_ano"));
      acc_numeE.setText(dtCon1.getString("acc_nume"));
      acc_serieE.setText(dtCon1.getString("acc_serie"));
      pcc_impporE.setText(dtCon1.getString("pcc_imppor"));
      pcc_portesE.setValor(dtCon1.getString("pcc_portes"));
      tra_codiE.setValorInt(dtCon1.getInt("tra_codi",true));
      subject=dtCon1.getString("pcc_subjec");
      pcc_fecconE.setDate(dtCon1.getDate("pcc_feccon"));
      pcc_feclisE.setDate(dtCon1.getDate("pcc_feclis"));
      pcc_tiplistE.setValor(pcc_estadE.getValor());
      

      pcc_comenE.setText(dtCon1.getString("pcc_comen"));
      verDatLin(dt);
    } catch (Exception k)
    {
      Error("Error al Mostrar Datos de Pedido",k);
    }
  }

 
 
 
  /**
   * Ver Datos Lineas del pedido
   * @param dt DatosTabla Con los datos de cabecera
   * @throws Exception Si ocurre un error
   */
  void verDatLin(DatosTabla dt) throws Exception
  {
    jt.removeAllDatos();
    s="SELECT * FROM pedicol WHERE emp_codi = "+dt.getInt("emp_codi")+
        " and eje_nume = "+dt.getInt("eje_nume")+
        " and pcc_nume = "+dt.getInt("pcc_nume")+
        " order by pcl_numli";
    if (! dtCon1.select(s))
    {
      mensajeErr("NO encontradas LINEAS de Pedido");
      return;
    }

    do
    {
      Vector v=new Vector();
      v.addElement(dtCon1.getString("pro_codi"));
      v.addElement(dtCon1.getString("pro_nomb"));
      v.addElement(dtCon1.getFecha("pcl_feccad","dd-MM-yyyy"));
      v.addElement(dtCon1.getString("pcl_nucape"));
      v.addElement(dtCon1.getString("pcl_cantpe"));
      v.addElement(dtCon1.getString("pcl_precpe"));
      v.addElement(dtCon1.getString("pcl_nucaco"));
      v.addElement(dtCon1.getString("pcl_cantco"));
      v.addElement(dtCon1.getString("pcl_precco"));
      v.addElement(dtCon1.getString("pcl_nucafa"));
      v.addElement(dtCon1.getString("pcl_cantfa"));
      v.addElement(dtCon1.getString("pcl_precfa"));
      v.addElement(div_codiE.getText(dtCon1.getString("div_codi")));
      v.addElement(dtCon1.getString("pcl_comen"));
      jt.addLinea(v);
    } while (dtCon1.next());
    jt.requestFocusInicio();
    calcSum();

  }
  /**
   * ReCalcula Suma de Individuos y Kilos para el pedido.
   *
   */
  void calcSum()
  {
    int nRow = jt.getRowCount();
    char estad;
    if (pcc_estadE.getValor().equals(""))
      estad='P';
    else
      estad=pcc_estadE.getValor().charAt(0);
    int nLin=0;
    int nInd=0;
    double kg=0;
    double impPed=0;
    for (int n = 0; n < nRow; n++)
    {
      if (jt.getValorInt(n, 0) == 0)
        continue;
      nLin++;
      switch (estad)
      {
          case 'P':
            nInd += jt.getValorDec(n, 3);
            kg += jt.getValorDec(n, 4);
            impPed=jt.getValorDec(n, 4)*jt.getValorDec(n,5);
            break;
         case 'C':
           nInd += jt.getValorDec(n, 6);
           kg += jt.getValorDec(n, 7);
           impPed=jt.getValorDec(n, 7)*jt.getValorDec(n,8);
           break;
         case 'F':
           nInd += jt.getValorDec(n, 9);
           kg += jt.getValorDec(n, 10);
           impPed=jt.getValorDec(n, 10)*jt.getValorDec(n,11);
           break;
      }
    }
    npiTotE.setValorDec(nInd);
    nprTotE.setValorDec(nLin);
    kgTotE.setValorDec(kg);
    impTotE.setValorDec(impPed);
  }
@Override
  public void rgSelect() throws SQLException
  {
    super.rgSelect();
    navActivarAll();
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
    emp_codiE.iniciar(dtStat,this,vl,EU);
    sbe_codiE.iniciar(dtStat,this,vl,EU);
    sbe_codiE.setFieldEmpCodi(emp_codiE.getTextField());
    strSql = "SELECT * FROM pedicoc WHERE emp_codi = " + EU.em_cod +
       (EU.getSbeCodi()==0?"":" and sbe_codi = "+EU.getSbeCodi())+
       " and pcc_estrec = 'P' "+
       " ORDER BY emp_codi,eje_nume,pcc_nume ";

    prv_codiE.iniciar(dtStat, this, vl, EU);
    prv_codiE.setEnabled(false);
    pdalmace.llenaLinkBox(alm_codiE, dtStat);
    pdtransp.llenaPrvCompra(dtStat, tra_codiE);
    
//    s = "SELECT alm_codi,alm_nomb FROM v_almacen order by alm_nomb ";
//    dtStat.select(s);
//    alm_codiE.addDatos(dtStat);
    pro_codiE.setEntrada(true);
    pro_codiE.iniciar(dtStat, this, vl, EU);
//    pro_codiE.setProdEquiv(true);
    pro_codiE.setProNomb(null);

    s = "SELECT div_codi,div_codedi FROM v_divisa order by div_codedi";
    if (!dtStat.select(s))
      throw new SQLException("NO HAY NINGUNA DIVISA DEFINIDA");
    div_codiE.addItem(dtStat);


  }

  void Blistar_actionPerformed()
  {
    try
    {
      if (dtCons.getNOREG())
        return;

      s =" select c.*,l.pro_codi,l.pro_nomb, l.pcl_numli,l.div_codi,l.pcl_comen,";
  
      
      switch (pcc_tiplistE.getValor())
      {
        case "C":
         s += " pcl_nucaco as pcl_nucape, pcl_cantco as pcl_cantpe, pcl_precco as pcl_precpe"; 
         break;
        case "P":
          s += "  pcl_nucape, pcl_cantpe, pcl_precpe";
          break;
        case "F":
          s +=" pcl_nucafa as pcl_nucape, pcl_cantfa as pcl_cantpe, pcl_precfa as pcl_precpe";
            
      }
     
   
      s +=", pv.prv_nomb,pv.prv_percon from pedicoc as c,pedicol as l,v_proveedo as pv" +
          " where c.emp_codi = l.emp_codi" +
          " and c.eje_nume= l.eje_nume " +
          " and c.pcc_nume =l .pcc_nume " +
          " and c.prv_codi  = pv.prv_codi " +
          " and c.emp_codi = " + emp_codiE.getValorInt()+
          " and c.eje_nume = " + eje_numeE.getValorInt() +
          " and c.pcc_nume = " + pcc_numeE.getValorInt() +
          " order by c.emp_codi,c.eje_nume,c.pcc_nume,l.pcl_numli";
      if (! dtStat.select(s))
      {
        mensajes.mensajeAviso("No encontrado ningun pedido para listar");
        return;
      }

      msgTexto = new getMsgTexto(vl, "Introduzca Asunto para el Pedido",
                                 "Listar Pedido",
          subject == null ? "I send you the order for this week. Please confirm" :
                                 subject, this);
      msgTexto.addInternalFrameListener(new InternalFrameAdapter()
      {
        @Override
        public void internalFrameClosing(InternalFrameEvent e)
        {
          salSubject();
        }
      });

    }  catch (Exception k)
    {
      Error("Error al Listar Pedido", k);
    }
  }

  void salSubject()
  {
    try
    {
      subject=msgTexto.respuesta;
      if (subject==null)
      {
        mensajeErr("Listado ... CANCELADO");
        return;
      }
      String s1 = "UPDATE pedicoc set  pcc_feclis='"+Formatear.getFechaAct("yyyyMMdd")+"'"+
          ",pcc_subjec = '" +
          (subject.length() > 255 ? subject.substring(0, 255) : subject) +
          "' WHERE emp_codi = " + emp_codiE.getValorInt()+
          " and eje_nume= " + eje_numeE.getValorInt() +
          " and pcc_nume = " + pcc_numeE.getValorInt();
//      debug(s1);
      stUp.executeUpdate(s1);
      ctUp.commit();
      dtStat.select(s);
      rs = dtStat.getResultSet().getStatement().executeQuery(dtStat.
          getStrSelect());
      JasperReport jr =  Listados.getJasperReport(EU,"lipedco");
      java.util.HashMap mp = Listados.getHashMapDefault();
      mp.put("nomusu", EU.usu_nomb);
      mp.put("subject", subject);
      mp.put("verPrecios", opPrecios.isSelected());

      String img= Iconos.getPathIcon()+"logotipo.jpg";
      mp.put("logotipo",img);
      JasperPrint jp = JasperFillManager.fillReport(jr, mp,
          new JRResultSetDataSource(rs));

      gnu.chu.print.util.printJasper(jp, EU);
      pcc_feclisE.setDate(Formatear.getDateAct());
      mensajeErr("Listado generado");
    }
    catch (Exception k)
    {
      Error("Error al Listar Pedido", k);
    }

  }
}
