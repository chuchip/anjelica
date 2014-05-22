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
import net.sf.jasperreports.engine.*;
import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.winayu.AyuArt;
/**
 *
 * <p>Título: pdpeve </p>
 * <p>Descripción: Mantenimiento Pedidos de Ventas. Incluye panel consulta stock de Productos</p>
 * <p>Copyright: Copyright (c) 2005-2014
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los terminos de la Licencia Pública General de GNU seg�n es publicada por
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
  AyuArt aypro;
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
  CTextField pvl_precioE=new CTextField(Types.DECIMAL,"---,--9.99");
  CCheckBox pvl_confirE=new CCheckBox("S","N");
  CTextField pvl_comenE=new CTextField(Types.CHAR,"X",100);
  CTextField pvl_dtoE=new CTextField(Types.DECIMAL,"#9.99");
  CTextField pvl_prclprE=new CTextField(Types.DECIMAL,"---,--9.99");
  CTextField pvl_dtoproE=new CTextField(Types.DECIMAL,"#9.99");
  CTextField pvl_tipoE=new CTextField(Types.CHAR,"?",1);
  
  CComboBox alm_codiE = new CComboBox();
  CPanel Pprinc = new CPanel();
  CPanel Pcabe = new CPanel();
  CLabel cLabel1 = new CLabel();
  cliPanel cli_codiE = new cliPanel()
  {
    public void afterFocusLost(boolean error)
    {
      cli_codiE_afterFocusLost(error);
    }
  };
  CLabel cli_poblE = new CLabel();
  CLabel cLabel2 = new CLabel();
  CLabel cLabel3 = new CLabel();
  CTextField eje_numeE = new CTextField(Types.DECIMAL,"###9");
  CTextField pvc_numeE = new CTextField(Types.DECIMAL,"#####9");
  CLabel cLabel4 = new CLabel();
  CTextField pvc_fecpedE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CTextField pvc_horpedE = new CTextField(Types.DECIMAL,"99.99");
  CLabel cLabel6 = new CLabel();
  CTextField pvc_fecentE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel cLabel7 = new CLabel();
  CComboBox pvc_confirE = new CComboBox();
  CLabel cLabel8 = new CLabel();
  CTextField pvc_nupeclE = new CTextField(Types.CHAR,"X",12);
  CLabel cLabel9 = new CLabel();
  CTextField usu_nombE = new CTextField(Types.CHAR,"X",20);
  CLabel cLabel12 = new CLabel();
   final int JT_PROD=0;
    final int JT_NOMBPRO=1;
    final int JT_PROV=2;
    final int JT_NOMPRV=3;
    final int JT_FECCAD=4;    
    final int JT_CANTI=5;
    final int JT_TIPCAN=6;
    final int JT_PRECIO=7;
    final int JT_PRECON=8;
    final int JT_COMEN=9;
    final int JT_NL=10;
  CGridEditable jt = new CGridEditable(11)
  {
    public void cambiaColumna(int col, int colNueva, int row)
    {
      try
      {
        if (col == JT_PROD)
          jt.setValor(pro_codiE.getNombArtCli(pro_codiE.getValorInt(),
                                              cli_codiE.getValorInt()), row, JT_NOMBPRO);
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

    public int cambiaLinea(int row, int col)
    {
      return cambiaLineaJT(row);
    }
    public void afterCambiaLinea()
    {
      actAcumJT();
      pvl_precioE.resetCambio();
    }
  };
//  CButton Baceptar = new CButton("Aceptar",Iconos.getImageIcon("check"));
//  CButton Bcancelar = new CButton("Cancelar",Iconos.getImageIcon("cancel"));
  CPanel Ppie = new CPanel();
  CScrollPane jScrollPane1 = new CScrollPane();
  CTextArea pvc_comenE = new CTextArea();
  pstockAct pstock;
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CLabel cLabel10 = new CLabel();
  CComboBox opVerProd = new CComboBox();
  CCheckBox opPedidos = new CCheckBox();
  CLabel cLabel11 = new CLabel();
  CTextField emp_codiE = new CTextField(Types.DECIMAL, "#9");
  CLabel cLabel5 = new CLabel();
  CTextField nlE = new CTextField(Types.DECIMAL,"#9");
  CTextField cantE = new CTextField(Types.DECIMAL,"---9");
  CLabel cLabel13 = new CLabel();
  CLabel cLabel14 = new CLabel();
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

  public pdpeve(EntornoUsuario eu, Principal p, Hashtable ht)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    try
    {
      if (ht != null)
      {
//       if (ht.get("modPrecio") != null)
//         modPrecio = Boolean.valueOf(ht.get("modPrecio").toString()).
//             booleanValue();
      }
      setTitulo("Mant. Pedidos de Ventas");

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

  public pdpeve(gnu.chu.anjelica.menu p, EntornoUsuario eu)
  {
    EU = eu;
    vl = p.getLayeredPane();
    setTitulo("Mant. Pedidos de Ventas");
    eje = false;

    try
    {
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
    this.setSize(new Dimension(779, 530));
    this.setMinimumSize(new Dimension(769, 530));
    this.setVersion("2014-02-03");

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
          if (jt.getValorInt(JT_PROD) == proCodi && jt.getValorInt(JT_PROV) == prvCodi
              && jt.getValString(JT_FECCAD).equals(feccad))
            jt.setValor("" + (jt.getValorInt(JT_CANTI) + 1),JT_CANTI);
          else
          {
            v.add("" + proCodi); // 0
            v.add(pro_codiE.getNombArt("" + proCodi)); // 1
            v.add("" + prvCodi); // 2
            v.add(prv_codiE.getNombPrv("" + prvCodi)); //3
            v.add(feccad); // 4
            v.add("1"); // 5
            v.add("U"); // 6 Unidades
            v.add(""+precio); // 7
            v.add(precio!=0); // 8
            v.add(""); // 9
            v.add("0"); // 10
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
          jt.setEnabled(true);
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
      public boolean isEdit()
      {
        return jt.isEnabled();
      }
    };
    pstock.setVerProductos(""+pstockAct.VER_ULTVENTAS);
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

    opPedidos.setSelected(true);
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
    pstock.setPreferredSize(new Dimension(100,320));
    pstock.setMinimumSize(new Dimension(100,320));
    pstock.setMaximumSize(new Dimension(100,320));
    Pcabe.setBorder(BorderFactory.createRaisedBevelBorder());
    Pcabe.setMaximumSize(new Dimension(668, 75));
    Pcabe.setMinimumSize(new Dimension(668, 75));
    Pcabe.setPreferredSize(new Dimension(668, 75));
    Pcabe.setLayout(null);
    cli_poblE.setBackground(Color.orange);
    cli_poblE.setForeground(Color.black);
    cli_poblE.setOpaque(true);
    cli_poblE.setBounds(new Rectangle(454, 3, 294, 16));
    cLabel2.setText("Pobl.");
    cLabel2.setBounds(new Rectangle(422, 3, 34, 16));
    cLabel3.setText("N. Pedido");
    cLabel3.setBounds(new Rectangle(3, 20, 57, 16));
    cLabel4.setText("Fecha Ped.");
    cLabel4.setBounds(new Rectangle(2, 4, 59, 16));
    cLabel6.setText("Fec.Entrega");
    cLabel6.setBounds(new Rectangle(331, 20, 67, 16));
    cLabel7.setText("Conf");
    cLabel7.setBounds(new Rectangle(474, 20, 33, 16));
    cLabel8.setText("N.Ped. Cliente");
    cLabel8.setBounds(new Rectangle(147, 20, 77, 16));
    cLabel9.setText("Usuario");
    cLabel9.setBounds(new Rectangle(182, 4, 49, 16));
    cLabel12.setText("Comentario");
    cLabel12.setBounds(new Rectangle(3, 38, 68, 18));

    cLabel1.setText("Cliente");
    cLabel1.setBounds(new Rectangle(75, 3, 43, 16));
    Ppie.setBorder(BorderFactory.createRaisedBevelBorder());
    Ppie.setMaximumSize(new Dimension(469, 24));
    Ppie.setMinimumSize(new Dimension(469, 24));
    Ppie.setPreferredSize(new Dimension(469, 24));
    Ppie.setLayout(null);
    pstock.setBorder(BorderFactory.createLineBorder(Color.black));
    conf_jt();

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
    pvc_horpedE.setBounds(new Rectangle(146, 4, 35, 16));
    pvc_fecpedE.setBounds(new Rectangle(62, 4, 81, 16));
    jScrollPane1.setBounds(new Rectangle(77, 38, 390, 35));
    pvc_fecentE.setBounds(new Rectangle(396, 20, 75, 16));
    pvc_nupeclE.setBounds(new Rectangle(225, 20, 105, 16));
    pvc_confirE.setBounds(new Rectangle(511, 20, 44, 16));
    pvc_numeE.setBounds(new Rectangle(92, 20, 50, 16));
    eje_numeE.setBounds(new Rectangle(57, 20, 33, 16));
    cli_codiE.setBounds(new Rectangle(118, 3, 297, 16));
    cLabel10.setText("Ver Prod.");
    cLabel10.setBounds(new Rectangle(468, 56, 63, 16));

    opVerProd.setBounds(new Rectangle(518, 56, 112, 16));
    opPedidos.setHorizontalAlignment(SwingConstants.RIGHT);
    opPedidos.setHorizontalTextPosition(SwingConstants.LEFT);
    opPedidos.setMargin(new Insets(0, 0, 0, 0));
    opPedidos.setText("Incluir Pedidos");
    opPedidos.setBounds(new Rectangle(644, 37, 119, 17));
    cLabel11.setText("Empr.");
    cLabel11.setBounds(new Rectangle(3, 3, 35, 16));
    emp_codiE.setBounds(new Rectangle(37, 3, 33, 16));
    cLabel5.setText("NL");
    cLabel5.setBounds(new Rectangle(310, 4, 21, 16));
    nlE.setToolTipText("Numero Lineas del pedido");
    nlE.setBounds(new Rectangle(329, 4, 28, 16));
    cantE.setBounds(new Rectangle(394, 4, 43, 16));
    cantE.setToolTipText("Cantidad de piezas del pedido");
    cLabel13.setBounds(new Rectangle(365, 4, 33, 16));
    cLabel13.setText("Cant");
    cLabel14.setText("Almacen");
    cLabel14.setBounds(new Rectangle(468, 37, 51, 16));
    alm_codiE.setBounds(new Rectangle(522, 37, 116, 16));
    cLabel15.setText("Albaran");
    cLabel15.setBounds(new Rectangle(559, 20, 50, 16));
    avc_serieE.setBounds(new Rectangle(642, 20, 40, 16));
    avc_numeE.setBounds(new Rectangle(684, 20, 65, 16));
    avc_anoE.setBounds(new Rectangle(603, 20, 38, 16));
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
    Pcabe.add(cLabel2, null);
    Pcabe.add(cLabel1, null);
    Pcabe.add(cLabel11, null);
    Pcabe.add(emp_codiE, null);
    Pcabe.add(avc_serieE, null);
    Pcabe.add(avc_numeE, null);
    Pcabe.add(cLabel3, null);
    Pcabe.add(eje_numeE, null);
    Pcabe.add(pvc_numeE, null);
    Pcabe.add(pvc_nupeclE, null);
    Pcabe.add(pvc_fecentE, null);
    Pcabe.add(cLabel7, null);
    Pcabe.add(pvc_confirE, null);
    Pcabe.add(cLabel15, null);
    Pcabe.add(avc_anoE, null);
    Pcabe.add(opVerProd, null);
    Pcabe.add(cLabel12, null);
    Pcabe.add(cLabel10, null);
    Pcabe.add(alm_codiE, null);
    Pcabe.add(cLabel14, null);
    Pcabe.add(jScrollPane1, null);
    Pcabe.add(cLabel8, null);
    Pcabe.add(cLabel6, null);
    Pcabe.add(opPedidos, null);
    Pcabe.add(cLabel16, null);
    Pcabe.add(pvc_verfecE, null);
    jScrollPane1.getViewport().add(pvc_comenE, null);
    Ppie.add(cLabel4, null);
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
    Pprinc.add(pstock,               new GridBagConstraints(0, 1, 4, 1, 1.0, 1.0
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
    v.add("Prv"); // 2
    v.add("Nombre Prv"); // 3
    v.add("Fec.Cad"); // 4
    v.add("Cant"); // 5
    v.add("Tipo");// 6
    v.add("Precio"); // 7
    v.add("Conf"); // 8 Confirmado Precio ?
    v.add("Comentario"); // 9 Comentario
    v.add("NL.");// 10
    jt.setCabecera(v);
    jt.setMaximumSize(new Dimension(2147483647, 200));
    jt.setMinimumSize(new Dimension(31, 250));
    jt.setPreferredSize(new Dimension(477, 250));
    jt.setPuntoDeScroll(50);
    jt.setAnchoColumna(new int[]{60,160,50,150,90,70,40,60,50,150,30});
    jt.setAlinearColumna(new int[]{2,0,2,0,1,1,0,2,1,0,2});
    jt.setFormatoColumna(JT_CANTI,"--,---9");
    jt.setFormatoColumna(JT_PRECIO,"---,--9.99");
    jt.setFormatoColumna(JT_PRECON,"BSN");

    ArrayList v1=new ArrayList();
    pvl_tipoE.setText("U");
    pvl_tipoE.setMayusc(true);
    pvl_tipoE.setCaracterAceptar("UK");
    pvl_tipoE.setToolTipText("Tipo Cantidad: (K)ilos, (U)nidades");
    pro_nombE.setEnabled(false);
    pro_codiE.setProNomb(null);
    prv_codiE.setCampoNombre(null);
    pvl_numlinE.setEnabled(false);
    prv_nombE.setEnabled(false);
    v1.add(pro_codiE.pro_codiE); // 0
    v1.add(pro_nombE); // 1
    v1.add(prv_codiE.prv_codiE); // 2
    v1.add(prv_nombE); // 3
    v1.add(pvl_feccadE); // 4
    v1.add(pvl_cantiE); // 5
    v1.add(pvl_tipoE);//6
    v1.add(pvl_precioE); // 7
    v1.add(pvl_confirE); // 8
    v1.add(pvl_comenE); // 9
    v1.add(pvl_numlinE); // 10
    jt.setCampos(v1);
  }

  public void afterConecta() throws SQLException, java.text.ParseException
  {
    cli_codiE.iniciar(dtStat, this, vl, EU);
    cli_codiE.setAceptaNulo(false);
    pro_codiE.iniciar(dtStat, this, vl, EU);
    pro_codiE.setAceptaInactivo(false);
    prv_codiE.iniciar(dtStat,this,vl,EU);
    prv_codiE.setAceptaNulo(true);
    s = "SELECT alm_codi,alm_nomb FROM v_almacen ORDER BY alm_nomb";
    dtCon1.select(s);
    alm_codiE.addItem(dtCon1);
    ALMACEN = 1;

    if (pdconfig.getConfiguracion(EU.em_cod,dtStat))
    {
      ALMACEN = dtStat.getInt("cfg_almven");
      alm_codiE.setValor("" + ALMACEN); // Almacen de VENTAS
    }

    pvc_confirE.addItem("Si", "S");
    pvc_confirE.addItem("NO", "N");
    opVerProd.addItem("Ult.Ventas", ""+pstockAct.VER_ULTVENTAS);
    opVerProd.addItem("Con Stock", ""+pstockAct.VER_CONSTOCK);
    opVerProd.addItem("TODOS", ""+pstockAct.VER_TODOS );
    avc_serieE.addItem("A", "A");
    avc_serieE.addItem("B", "B");
    avc_serieE.addItem("C", "C");
    avc_serieE.addItem("D", "D");
  }

  public void iniciarVentana() throws Exception
  {
    pstock.setPedidos(opPedidos.isSelected());

    emp_codiE.setColumnaAlias("emp_codi");
    eje_numeE.setColumnaAlias("eje_nume");
    cli_codiE.setColumnaAlias("cli_codi");
    pvc_numeE.setColumnaAlias("pvc_nume");
    pvc_nupeclE.setColumnaAlias("pvc_nupecl");
    pvc_fecentE.setColumnaAlias("pvc_fecent");
    pvc_confirE.setColumnaAlias("pvc_confir");
    alm_codiE.setColumnaAlias("alm_codi");
    avc_anoE.setColumnaAlias("avc_ano");
    avc_serieE.setColumnaAlias("avc_serie");
    avc_numeE.setColumnaAlias("avc_nume");
    Pcabe.setDefButton(Baceptar);
    jt.setDefButton(Baceptar);
    verDatos();
//    opPedidos.setEnabled(true);
//    opVerProd.setEnabled(true);
//    pvc_verfecE.setEnabled(true);
    pstock.verFamilias();
    activarEventos();
  }
  void activarEventos()
  {
    pvc_fecentE.addFocusListener(new FocusAdapter()
    {
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
     public void actionPerformed(ActionEvent e)
     {
       Bimpri_actionPerformed();
     }
   });
   BbusProd.addActionListener(new ActionListener()
  {
    public void actionPerformed(ActionEvent e)
    {
      BbusProd_actionPerformed();
    }
  });

    opPedidos.addActionListener(new ActionListener()
    {
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
  public void PADPrimero()  {    verDatos();  }

  public void PADAnterior()  {    verDatos();}

  public void PADSiguiente()  {    verDatos();}

  public void PADUltimo()  {    verDatos();}

  void setPanelQuery(boolean query)
  {
    cli_codiE.setQuery(query);
    pvc_nupeclE.setQuery(query);
    pvc_fecentE.setQuery(query);
    pvc_confirE.setQuery(query);
    pvc_comenE.setQuery(query);
    eje_numeE.setQuery(query);
    pvc_numeE.setQuery(query);
    emp_codiE.setQuery(query);
    avc_anoE.setQuery(query);
    avc_serieE.setQuery(query);
    avc_numeE.setQuery(query);

  }
  public void PADQuery()
  {
    setPanelQuery(true);


    cli_codiE.resetTexto();
    cli_poblE.setText("");
    pvc_nupeclE.resetTexto();
    pvc_fecentE.resetTexto();
    pvc_confirE.resetTexto();
    pvc_comenE.resetTexto();
    eje_numeE.resetTexto();
    pvc_numeE.resetTexto();
    emp_codiE.resetTexto();
    avc_anoE.resetTexto();
    avc_serieE.resetTexto();
    avc_numeE.resetTexto();


    activar(navegador.QUERY,true);
    emp_codiE.requestFocus();
    mensaje("Introduzca filtro de Consulta ...");
  }


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
     ArrayList v = new ArrayList();

     v.add(cli_codiE.getStrQuery());
     v.add(emp_codiE.getStrQuery());
     v.add(eje_numeE.getStrQuery());
     v.add(pvc_numeE.getStrQuery());
     v.add(pvc_fecentE.getStrQuery());
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

  public void canc_query()
  {
    setPanelQuery(false);
    activaTodo();
    verDatos();
    mensajeErr("Introducion Filtro de busqueda ... CANCELADO");
    mensaje("");
    nav.pulsado=navegador.NINGUNO;
  }

  public void PADEdit()
  {
    try {
      activar(navegador.EDIT, true);
      if ( avc_anoE.getValorDec() > 0 )
      {
        msgBox("Pedido YA TIENE albaran ... IMPOSIBLE MODIFICAR");
        nav.pulsado = navegador.NINGUNO;
        activaTodo();
        return;
      }
      if (!setBloqueo(dtAdd, "pedvenc",
                      eje_numeE.getValorInt() + "|" + emp_codiE.getValorInt() +
                      "|" + pvc_numeE.getValorInt()))
      {
        msgBox(msgBloqueo);
        activaTodo();
        return;
      }
      pvc_verfecE.setEnabled(opVerProd.getValor().equals(""+pstockAct.VER_ULTVENTAS));
      pvl_precioE.resetCambio();
      pvc_fecentE.resetCambio();
      jt.setEnabled(true);
      cli_codiE_afterFocusLost(false);
      mensaje("Editando Pedido ...");
      jt.requestFocusInicio();
    } catch (Exception k)
    {
      Error("Error al editar registro",k);
    }
  }

 public boolean checkEdit()
 {
   return checkAddNew();
 }

 public void ej_edit1()
 {
   try
   {
     s="select * from pedvenc where emp_codi = " + emp_codiE.getValorInt() +
          " and eje_nume= " + eje_numeE.getValorInt() +
          " and pvc_nume = " + pvc_numeE.getValorInt();
     dtAdd.select(s);
     dtAdd.edit();
     actCabecera();
     actLinea();
     resetBloqueo(dtAdd, "pedvenc",
                 eje_numeE.getValorInt() + "|" + emp_codiE.getValorInt() +
                 "|" + pvc_numeE.getValorInt(),false);
     dtAdd.commit();
     activaTodo();
     mensaje("");
     mensajeErr("Pedido ... MODIFICADO");
   }
   catch (SQLException k)
   {
     Error("Error al Insertar Pedido", k);
     return;
   }

 }

  public void canc_edit()
  {
    nav.pulsado=navegador.NINGUNO;
    activaTodo();
    verDatos();
    try
    {
      resetBloqueo(dtAdd, "pedvenc",
                   eje_numeE.getValorInt() + "|" + emp_codiE.getValorInt() +
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

  public void PADAddNew()
  {
    try {
      jt.removeAllDatos();
      activar(navegador.ADDNEW, true);
      pvc_confirE.setValor("S");
      pvc_verfecE.setEnabled(opVerProd.getValor().equals(""+pstockAct.VER_ULTVENTAS));
      cli_codiE.resetTexto();
      pvc_comenE.resetTexto();
      pvc_nupeclE.resetTexto();
      emp_codiE.setValorDec(EU.em_cod);
      eje_numeE.setValorDec(EU.ejercicio);
      pvc_numeE.setValorDec(getNumPed(false));
      alm_codiE.setValor(""+ALMACEN);
      pvl_precioE.setValorInt(-1);
      pvl_precioE.resetCambio();
      usu_nombE.setText(EU.usuario);
      pvc_comenE.resetTexto();
      pvc_fecpedE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
      pvc_horpedE.setText(Formatear.getFechaAct("hh.ss"));
      pvc_fecentE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
      pvc_fecentE.resetCambio();
      jt.setEnabled(true);
    } catch (SQLException k)
    {
      Error("Error en PADAddNew ",k);
      return;
    }
    mensaje("Creando nuevo Pedido ...");
    jt.requestFocusInicio();
    cli_codiE.requestFocus();
  }

  public boolean checkAddNew()
  {
    try
    {
      if (!cli_codiE.controlar())
      {
        mensajeErr(cli_codiE.getMsgError());
        return false;
      }
      if (pvc_fecentE.getError() || pvc_fecentE.isNull())
      {
        mensajeErr("Fecha de Entrega NO valida");
        pvc_fecentE.requestFocus();
        return false;
      }
      jt.procesaAllFoco();
      actAcumJT();
      if (nlE.getValorInt()==0)
      {
        mensajeErr("Introduze alguna linea de pedido ... �� Cachondo !!");
        jt.requestFocusInicio();
        return false;
      }

    } catch (Exception k)
    {
      Error("Error al validar datos",k);
    }
    return true;
  }
  public void ej_addnew1()
  {
    try {
      pvc_numeE.setValorInt(getNumPed(true));
      dtAdd.addNew("pedvenc");
      dtAdd.setDato("emp_codi",emp_codiE.getValorInt());
      dtAdd.setDato("eje_nume",eje_numeE.getValorInt());
      dtAdd.setDato("pvc_nume",pvc_numeE.getValorInt());
      dtAdd.setDato("pvc_impres","N"); // No impreso
      actCabecera();
      actLinea();
      dtAdd.commit();
      activaTodo();

      mensajeErr("Pedido ... INSERTADO");
      mensaje("");
      PADAddNew();
    } catch (Exception k)
    {
      Error("Error al Insertar Pedido",k);      
    }

  }

  private void actLinea() throws SQLException
  {
    int nRow=jt.getRowCount();
    int nl=1;
    if (nav.pulsado==navegador.EDIT)
     nl=-1;
    for (int n=0;n<nRow;n++)
    {
      if (jt.getValorInt(n, 0) == 0)
        continue;
      s = " SELECT * FROM pedvenl WHERE emp_codi = " + emp_codiE.getValorInt() +
          " and eje_nume= " + eje_numeE.getValorInt() +
          " and pvc_nume = " + pvc_numeE.getValorInt() +
          " and pvl_numlin = " + jt.getValorInt(n, 9);
      if (! dtAdd.select(s,true))
      {
        dtAdd.addNew();
        dtAdd.setDato("emp_codi", emp_codiE.getValorInt());
        dtAdd.setDato("eje_nume", eje_numeE.getValorInt());
        dtAdd.setDato("pvc_nume", pvc_numeE.getValorInt());
      }
      else
        dtAdd.edit();
      dtAdd.setDato("pvl_numlin", nl);
      dtAdd.setDato("pvl_unid", jt.getValorInt(n,JT_CANTI));
      dtAdd.setDato("pvl_kilos", jt.getValorInt(n,JT_CANTI));
      dtAdd.setDato("pvl_unid", jt.getValorDec(n,JT_CANTI));
      dtAdd.setDato("pvl_unid", jt.getValorDec(n,JT_CANTI));
      dtAdd.setDato("pvl_tipo", jt.getValString(n,JT_TIPCAN));
      dtAdd.setDato("pro_codi", jt.getValorInt(n,JT_PROD));
      dtAdd.setDato("pvl_comen", jt.getValString(n,JT_COMEN));
      dtAdd.setDato("pvl_precio", jt.getValorDec(n,JT_PRECIO));
      dtAdd.setDato("pvl_precon", jt.getValBoolean(n,JT_PRECON)?-1:0);
      dtAdd.setDato("prv_codi", jt.getValorInt(n,JT_PROV));
      dtAdd.setDato("pvl_feccad",jt.getValString(n,JT_FECCAD).trim().equals("")?null:jt.getValString(n,JT_FECCAD),"dd-MM-yy");
      if (dtAdd.getTipoUpdate()==DatosTabla.ADDNEW)
      {
        dtAdd.setDato("pvl_fecped", "{ts '"+Formatear.getFechaAct("yyyy-MM-dd hh:mm:ss")+"'}");
        dtAdd.setDato("pvl_fecmod", (Timestamp)null);
      }
      else
        dtAdd.setDato("pvl_fecmod", "{ts '"+Formatear.getFechaAct("yyyy-MM-dd hh:mm:ss")+"'}");
      dtAdd.update();
      if (nav.pulsado==navegador.EDIT)
        nl--;
      else
        nl++;
    }
     if (nav.pulsado==navegador.ADDNEW)
       return;
    // Borro los registros con numero de linea > 0 pues si no los he actualizado
    // es que se han borrado.
    s = " DELETE FROM pedvenl   WHERE emp_codi = " + emp_codiE.getValorInt() +
       " and eje_nume= " + eje_numeE.getValorInt() +
       " and pvc_nume = " + pvc_numeE.getValorInt() +
       " AND pvl_numlin > 0 ";
    dtAdd.executeUpdate(s);
    // Pongo los registros con numero de linea a > 0

    s = " UPDATE pedvenl SET pvl_numlin=pvl_numlin*-1  WHERE emp_codi = " + emp_codiE.getValorInt() +
        " and eje_nume= " + eje_numeE.getValorInt() +
        " and pvc_nume = " + pvc_numeE.getValorInt() ;
    dtAdd.executeUpdate(s);
  }

  private void actCabecera() throws  SQLException
  {
    dtAdd.setDato("cli_codi",cli_codiE.getValorInt());
    dtAdd.setDato("alm_codi",alm_codiE.getValorInt());
    dtAdd.setDato("pvc_fecped","{ts '"+Formatear.getFechaAct("yyyy-MM-dd hh:mm:ss")+"'}");
    dtAdd.setDato("pvc_fecent",pvc_fecentE.getText(),"dd-MM-yyyy");
    dtAdd.setDato("pvc_comen",Formatear.strCorta(pvc_comenE.getText(),200));
    dtAdd.setDato("pvc_confir",pvc_confirE.getValor());
    dtAdd.setDato("avc_ano",0);
    dtAdd.setDato("avc_serie","A");
    dtAdd.setDato("avc_nume",0);
    dtAdd.setDato("usu_nomb",usu_nombE.getText());
    dtAdd.setDato("avc_cerra",0); // Albaran Abierto
    dtAdd.setDato("pvc_nupecl",pvc_nupeclE.getText());
    dtAdd.update();
  }

  public void canc_addnew()
  {
    nav.pulsado=navegador.NINGUNO;
    activaTodo();
    verDatos();
    mensaje("");
    mensajeErr("Creacion de Nuevo Pedido ... CANCELADO");
  }

  public void PADDelete()
  {
    try
    {
      if (avc_anoE.getValorDec() > 0)
      {
        msgBox("Pedido YA TIENE albaran ... IMPOSIBLE BORRAR");
        nav.pulsado = navegador.NINGUNO;
        activaTodo();
        return;
      }
      if (!setBloqueo(dtAdd, "pedvenc",
                      eje_numeE.getValorInt() + "|" + emp_codiE.getValorInt() +
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
    catch (Exception k)
    {
      Error("Error al Borrar registro", k);
    }

  }


  public void ej_delete1()
  {
    try
    {
      s = " DELETE FROM pedvenl WHERE emp_codi = " + emp_codiE.getValorInt() +
          " and eje_nume= " + eje_numeE.getValorInt() +
          " and pvc_nume = " + pvc_numeE.getValorInt();
      dtAdd.executeUpdate(s);
      s = "DELETE from pedvenc where emp_codi = " + emp_codiE.getValorInt() +
          " and eje_nume= " + eje_numeE.getValorInt() +
          " and pvc_nume = " + pvc_numeE.getValorInt();
      dtAdd.executeUpdate(s);
      resetBloqueo(dtAdd, "pedvenc",
                   eje_numeE.getValorInt() + "|" + emp_codiE.getValorInt() +
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
      return;
    }

  }

  public void canc_delete()
  {
    nav.pulsado = navegador.NINGUNO;
    activaTodo();
    try
    {
      resetBloqueo(dtAdd, "pedvenc",
                   eje_numeE.getValorInt() + "|" + emp_codiE.getValorInt() +
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

    Baceptar.setEnabled(b);
    Bcancelar.setEnabled(b);
    cli_codiE.setEnabled(b);
    if (modo!=navegador.ADDNEW && modo!=navegador.EDIT )
    {
      if (modo!=navegador.QUERY)
      {
        jt.setEnabled(b);
        Ppie.setEnabled(b);
      }
      eje_numeE.setEnabled(b);
      pvc_numeE.setEnabled(b);
    }
    if (modo!=navegador.EDIT)
      emp_codiE.setEnabled(b);
    pvc_nupeclE.setEnabled(b);
    pvc_fecentE.setEnabled(b);
    pvc_confirE.setEnabled(b);
    if (modo!=navegador.QUERY)
    {
      pvc_comenE.setEnabled(b);
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

  public void activar(boolean b)
  {
    activar(navegador.TODOS,b);
  }

  void verDatos()
  {
    if (dtCons.getNOREG())
      return;
    cli_codiE.resetTexto();
    cli_poblE.setText("");
    pvc_nupeclE.resetTexto();
    pvc_fecentE.resetTexto();
    pvc_confirE.resetTexto();
    pvc_comenE.resetTexto();

    eje_numeE.resetTexto();
    pvc_numeE.resetTexto();
    Ppie.resetTexto();
    jt.removeAllDatos();
    try {
      emp_codiE.setValorInt(dtCons.getInt("emp_codi"));
      eje_numeE.setValorInt(dtCons.getInt("eje_nume"));
      pvc_numeE.setValorInt(dtCons.getInt("pvc_nume"));
      s = "SELECT * FROM pedvenc WHERE emp_codi = " + emp_codiE.getValorInt() +
          " and eje_nume= " + eje_numeE.getValorInt() +
          " and pvc_nume = " + pvc_numeE.getValorInt();
      if (! dtCon1.select(s))
      {
        mensajeErr("NO ENCONTRADO PEDIDO ... SEGURAMENTE SE BORRO");
        return;
      }
      cli_codiE.setValorInt(dtCon1.getInt("cli_codi"));
      cli_poblE.setText(cli_codiE.getLikeCliente().getString("cli_pobl"));
      pvc_nupeclE.setText(dtCon1.getString("pvc_nupecl"));
      pvc_fecentE.setText(dtCon1.getFecha("pvc_fecent","dd-MM-yyyy"));
      pvc_confirE.setValor(dtCon1.getString("pvc_confir"));
      pvc_comenE.setText(dtCon1.getString("pvc_comen"));
      alm_codiE.setValor(dtCon1.getInt("alm_codi"));
      pvc_fecpedE.setText(dtCon1.getFecha("pvc_fecped"));
      pvc_horpedE.setText(dtCon1.getFecha("pvc_fecped","hh.mm"));
      usu_nombE.setText(dtCon1.getString("usu_nomb"));
      avc_numeE.setText(dtCon1.getString("avc_nume"));
      avc_serieE.setValor(dtCon1.getString("avc_serie"));
      avc_anoE.setValorDec(dtCon1.getInt("avc_nume"));
      pvc_impresE.setSelecion(dtCon1.getString("pvc_impres"));

      s = "SELECT * FROM pedvenl WHERE emp_codi = " + emp_codiE.getValorInt() +
              " and eje_nume= " + eje_numeE.getValorInt() +
              " and pvc_nume = " + pvc_numeE.getValorInt();
      if (dtCon1.select(s))
      {
        do
        {
          ArrayList v=new ArrayList();
          v.add(dtCon1.getString("pro_codi"));
          v.add(pro_codiE.getNombArtCli(dtCon1.getInt("pro_codi"),
                                              cli_codiE.getValorInt()));
         v.add(dtCon1.getString("prv_codi"));
         v.add(prv_codiE.getNombPrv(dtCon1.getString("prv_codi")));
         v.add(dtCon1.getFecha("pvl_feccad","dd-MM-yy"));
         v.add(dtCon1.getString("pvl_tipo").equals("K")?dtCon1.getDouble("pvl_kilos"):
             dtCon1.getDouble("pvl_unid"));
          v.add(dtCon1.getString("pvl_tipo"));
         v.add(dtCon1.getString("pvl_precio"));
         v.add(dtCon1.getInt("pvl_precon")!=0);
         v.add(dtCon1.getString("pvl_comen"));
         v.add(dtCon1.getString("pvl_numlin"));
         jt.addLinea(v);
        }
        while (dtCon1.next());
      }
      actAcumJT();
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
          mensajeErr("Introduzca tipo de cantidad (Kilos o Unidades)");
          return JT_TIPCAN;
      }
      if (pvl_tipoE.getText().equals("U") && pvl_tipoE.getText().equals("K"))
      {
          mensajeErr("Tipo cantidad Invalida. Aceptables: Kilos/Unidades");
          return JT_TIPCAN;
         
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
    s = "SELECT num_pedid  FROM v_numerac WHERE emp_codi = " +  emp_codiE.getValorInt() +
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
      if (jt.getValorInt(n,0)==0)
        continue;
      nl++;
      nu+=jt.getValorDec(n,5);
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
       java.util.HashMap mp = new java.util.HashMap();
       
       JasperReport jr = gnu.chu.print.util.getJasperReport(EU,"pedventas");
       s="select l.*,p.prv_nomb ,c.cli_codi,c.alm_codi,c.pvc_fecped, "+
       " c.pvc_fecent,c.usu_nomb,c.pvc_comen,al.alm_nomb, "+
       " a.pro_nomb, cl.cli_nomb,cl.cli_pobl "+
       " from pedvenl as l left join v_proveedo p on  p.prv_codi = l.prv_codi, "+
       " pedvenc as c,v_articulo as a,v_almacen as al,clientes as cl "+
       " where  c.emp_codi = l.emp_codi "+
       " and c.eje_nume = l.eje_nume "+
       " and c.pvc_nume = l.pvc_nume "+
       " and l.pro_codi = a.pro_codi "+
       " and al.alm_codi = c.alm_codi "+
       " and c.cli_codi = cl.cli_codi "+
       " and c.emp_codi = "+emp_codiE.getValorInt()+
       " and c.eje_nume = "+eje_numeE.getValorInt()+
       " and c.pvc_nume = "+pvc_numeE.getValorInt()+
       " order by l.emp_codi,l.eje_nume,l.pvc_nume,l.pvl_numlin ";
       ResultSet rs;

       rs=dtCon1.getStatement().executeQuery(dtStat.getStrSelect(s));

       JasperPrint jp = JasperFillManager.fillReport(jr, mp, new JRResultSetDataSource(rs));
       gnu.chu.print.util.printJasper(jp, EU);
       s = "update PEDVENC SET pvc_impres = 'S' WHERE emp_codi = " + emp_codiE.getValorInt() +
           " and eje_nume = " + eje_numeE.getValorInt() +
           " and pvc_nume = " + pvc_numeE.getValorInt() ;
        dtAdd.executeUpdate(s);
        ctUp.commit();
        mensajeErr("Pedido Ventas ... IMPRESO ");
     }
     catch (Exception k)
     {
       Error("Error al imprimir Pedido Venta", k);
     }
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
       cli_poblE.setText(cli_codiE.getLikeCliente().getString("cli_pobl"));
       boolean releer = false;
       if (cli_codiE.getTarifa() != pstock.getTarifa())
       {
         pstock.setTarifa(cli_codiE.getTarifa());
         releer = true;
       }
       if (opVerProd.getValor().equals("" + pstockAct.VER_ULTVENTAS))
       {
         pstock.setCliente(cli_codiE.getValorInt());
         releer = true;
       }
       if (releer)
         pstock.verFamilias();
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

}
