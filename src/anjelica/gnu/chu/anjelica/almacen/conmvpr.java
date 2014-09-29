package gnu.chu.anjelica.almacen;

/**
 *
 * <p>Titulo: conmvpr </p>
 * <p>Descripcion: Consulta Mvtos de Almacen Valorados</p>
 * <p>Copyright: Copyright (c) 2005-2013
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
 */

import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.compras.MantAlbComCarne;
import gnu.chu.anjelica.despiece.MantDesp;
import gnu.chu.anjelica.despiece.pdprvades;
import gnu.chu.anjelica.pad.MantRepres;
import gnu.chu.anjelica.ventas.pdalbara;
import gnu.chu.camposdb.empPanel;
import gnu.chu.camposdb.proPanel;
import gnu.chu.controles.*;
import gnu.chu.eventos.CambioEvent;
import gnu.chu.eventos.CambioListener;
import gnu.chu.interfaces.ejecutable;
import gnu.chu.utilidades.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import javax.swing.BorderFactory;


public class conmvpr extends ventana
{
  private MvtosAlma mvtosAlm = new MvtosAlma();
  private boolean cancelarConsulta=false;
  String fecMov=null;
  int almCodi;
  //boolean acepNeg=false; // Aceptar Negativos (por defecto false)
  boolean valDesp=false;  // Valorar despieces
//  private final static boolean MSGBOX=false;
  CPanel Pprinc = new CPanel();
  CPanel Pentra = new CPanel();
  CLabel cLabel1 = new CLabel();
  CLabel incCostoL=new CLabel("Inc.Costo");
  CTextField pro_cosincE = new CTextField(Types.DECIMAL, "#9.99");
  CTextField feciniE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CTextField fecfinE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel2 = new CLabel();
  CLabel cLabel3 = new CLabel();
  private double costoFijo=-1;
  proPanel pro_codiE = new proPanel()
  {
    @Override
   public void afterFocusLost(boolean error)
   {
    if (!error)
        return;
      try {
        pro_cosincE.setValorDec(getLikeProd().getDouble("pro_cosinc"));
        costoFijo=pdprvades.getPrecio(dtStat, pro_codiE.getValorInt(),feciniE.getDate());
        opCostFij.setEnabled(costoFijo>0);
     } catch (Exception k )
     {
        Error("Error al buscar Costo a incrementar al producto",k);
     }
   }
  };
  CButton Bacepta = new CButton("Aceptar", Iconos.getImageIcon("check"));
  Cgrid jt = new Cgrid(13);
  CPanel PtotalE = new CPanel();
  CLabel cLabel4 = new CLabel();
  CTextField kgVentaE = new CTextField(Types.DECIMAL, "----,--9.99");
  CLabel cLabel5 = new CLabel();
  CTextField impVentaE = new CTextField(Types.DECIMAL, "----,--9.99");
  CLabel cLabel6 = new CLabel();
  CTextField pmVentaE = new CTextField(Types.DECIMAL, "----,--9.99");
  CLabel cLabel7 = new CLabel();
  CLabel cLabel8 = new CLabel();
  CTextField impCompra1 = new CTextField(Types.DECIMAL, "----,--9.99");
  CLabel cLabel9 = new CLabel();
  CLabel cLabel10 = new CLabel();
  CTextField pmCompraE = new CTextField(Types.DECIMAL, "----,--9.99");
  CLabel cLabel11 = new CLabel();
  CTextField kgCompraE = new CTextField(Types.DECIMAL, "----,--9.99");
  CLabel cLabel13 = new CLabel();
  CLabel cLabel12 = new CLabel();
  CTextField impGanaE = new CTextField(Types.DECIMAL, "----,--9.99");
  CLabel cLabel14 = new CLabel();
  CTextField ganKilE = new CTextField(Types.DECIMAL, "---9.99");
  CTextField ganKilPorE = new CTextField(Types.DECIMAL, "---9.99");
  CLabel cLabel15 = new CLabel();
  CLabel cLabel16 = new CLabel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CLabel cLabel17 = new CLabel();
  CComboBox feulinE = new CComboBox();
  CLabel cLabel18 = new CLabel();
  CLinkBox rep_codiE = new CLinkBox();
  CCheckBox opVenta = new CCheckBox();
  CCheckBox opCompra = new CCheckBox();
  CCheckBox opDespSal = new CCheckBox();
  CCheckBox opDesEnt = new CCheckBox();
  CCheckBox opRegul = new CCheckBox();
  CCheckBox opCostFij=new CCheckBox("Costos Fijos");
  CLabel cLabel19 = new CLabel();
  CTextField pro_loteE = new CTextField(Types.DECIMAL,"####9");
  CLabel cLabel20 = new CLabel();
  CLinkBox zon_codiE = new CLinkBox();
  CLabel cLabel21 = new CLabel();
  CTextField kgDesEntE = new CTextField(Types.DECIMAL, "----,--9.99");
  CLabel cLabel22 = new CLabel();
  CTextField kgDesSalE = new CTextField(Types.DECIMAL, "----,--9.99");
  CCheckBox opStkNeg = new CCheckBox();
  CCheckBox opSerieX = new CCheckBox();
  CLabel cLabel23 = new CLabel();
  CTextField pro_numindE = new CTextField(Types.DECIMAL,"####9");
  CLabel almacen = new CLabel();
  CLinkBox alm_codiE = new CLinkBox();
  CCheckBox opDesES = new CCheckBox();
  CCheckBox opValDesp = new CCheckBox();

  public conmvpr(EntornoUsuario eu, Principal p)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    setTitulo("Consulta Mvtos de Almacen");

    try
    {
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

  public conmvpr(gnu.chu.anjelica.menu p, EntornoUsuario eu)
  {

    EU = eu;
    vl = p.getLayeredPane();
    setTitulo("Consulta Mvtos de Almacen");
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
   
    this.setSize(new Dimension(750, 531));
    this.setVersion("2011-11-26");

    iniciarFrame();
    Pprinc.setLayout(gridBagLayout1);
    Pentra.setBorder(BorderFactory.createRaisedBevelBorder());
    Pentra.setMaximumSize(new Dimension(530, 100));
    Pentra.setMinimumSize(new Dimension(540, 100));
    Pentra.setPreferredSize(new Dimension(540, 100));
    Pentra.setQuery(false);
    Pentra.setLayout(null);
    conecta();
    cLabel1.setRequestFocusEnabled(true);
    cLabel1.setText("De Fecha");
    cLabel1.setBounds(new Rectangle(174, 2, 53, 16));
    feciniE.setBounds(new Rectangle(226, 2, 81, 16));
    fecfinE.setOpaque(true);
    fecfinE.setBounds(new Rectangle(362, 2, 81, 16));
    cLabel2.setBounds(new Rectangle(312, 2, 47, 16));
    cLabel2.setText("A Fecha");
    cLabel2.setOpaque(true);
    cLabel2.setRequestFocusEnabled(true);
    cLabel3.setText("Producto");
    cLabel3.setBounds(new Rectangle(6, 22, 54, 14));
    pro_codiE.setAncTexto(50);
    pro_codiE.setBounds(new Rectangle(62, 20, 333, 17));
    Bacepta.setBounds(new Rectangle(536, 77, 103, 21));
    Bacepta.setMargin(new Insets(0, 0, 0, 0));
    Bacepta.setText("Aceptar F4");
    ArrayList v = new ArrayList();
    v.add("Fecha"); // 0
    v.add("Entrada"); // 1
    v.add("Salida"); // 2
    v.add("Tipo"); // 3
    v.add("Precio"); // 4
    v.add("Unid"); // 5
    v.add("Kilos"); // 6
    v.add("Costo"); // 7
    v.add("Ganan"); // 8
    v.add("Documento");  //9
    v.add("Ej/Em/Lo/In"); // 10
    v.add("Gan.Kg");  // 11
    v.add("Alm");  // 11
    jt.setCabecera(v);
    jt.setToolTipText("Doble click para ir a Mvto");
    jt.setAnchoColumna(new int[]
                       {76, 61, 63, 31, 58,50, 64, 65, 64,100,80,60,30});
    jt.setAlinearColumna(new int[]
                         {1, 2, 2, 1, 2, 2,2, 2, 2,0,0,2,2});
    jt.setFormatoColumna(0,"dd-MM-yy");
    jt.setMinimumSize(new Dimension(422, 282));
    jt.setPreferredSize(new Dimension(422, 282));
    jt.setAjustarGrid(true);
    jt.setConfigurar("gnu.chu.anjelica.almacen.conmvpr",EU,dtStat);
    statusBar = new StatusBar(this);
    PtotalE.setBorder(BorderFactory.createRaisedBevelBorder());
    PtotalE.setMaximumSize(new Dimension(520, 68));
    PtotalE.setMinimumSize(new Dimension(520, 68));
    PtotalE.setPreferredSize(new Dimension(520, 68));
    PtotalE.setEnabled(false);
    PtotalE.setLayout(null);
    cLabel4.setBackground(Color.blue);
    cLabel4.setForeground(Color.cyan);
    cLabel4.setOpaque(true);
    cLabel4.setText("Venta");
    cLabel4.setBounds(new Rectangle(3, 2, 34, 17));
    kgVentaE.setBounds(new Rectangle(76, 2, 78, 17));
    cLabel5.setText("Importe");
    cLabel5.setBounds(new Rectangle(154, 2, 45, 17));
    impVentaE.setBounds(new Rectangle(198, 2, 78, 17));
    cLabel6.setToolTipText("");
    cLabel6.setText("Pr.Medio");
    cLabel6.setBounds(new Rectangle(282, 2, 49, 17));
    pmVentaE.setBounds(new Rectangle(338, 2, 78, 17));
    cLabel7.setText("Kgs");
    cLabel7.setBounds(new Rectangle(51, 2, 24, 21));
    cLabel8.setBounds(new Rectangle(283, 22, 49, 17));
    cLabel8.setText("Pr.Medio");
    cLabel8.setToolTipText("");
    impCompra1.setBounds(new Rectangle(201, 22, 78, 17));
    cLabel9.setBounds(new Rectangle(2, 22, 47, 17));
    cLabel9.setText("Entr.");
    cLabel9.setOpaque(true);
    cLabel9.setForeground(Color.cyan);
    cLabel9.setBackground(Color.blue);
    cLabel10.setBounds(new Rectangle(51, 22, 27, 17));
    cLabel10.setText("Kgs");
    pmCompraE.setBounds(new Rectangle(337, 22, 78, 17));
    cLabel11.setBounds(new Rectangle(157, 22, 45, 17));
    cLabel11.setText("Importe");
    kgCompraE.setBounds(new Rectangle(77, 22, 78, 17));
    cLabel13.setBounds(new Rectangle(4, 41, 59, 17));
    cLabel13.setText("Margenes");
    cLabel13.setOpaque(true);
    cLabel13.setForeground(Color.cyan);
    cLabel13.setBackground(Color.blue);
    cLabel12.setText("Ganancia");
    cLabel12.setBounds(new Rectangle(72, 41, 59, 18));
    impGanaE.setBounds(new Rectangle(127, 41, 78, 17));
    cLabel14.setRequestFocusEnabled(false);
    cLabel14.setText("Gan. Kg.");
    cLabel14.setBounds(new Rectangle(214, 41, 49, 19));
    ganKilE.setBounds(new Rectangle(260, 41, 53, 19));
    ganKilPorE.setBounds(new Rectangle(346, 41, 53, 19));
    cLabel15.setText("%");
    cLabel15.setBounds(new Rectangle(402, 41, 17, 17));
    cLabel16.setText("Euro");
    cLabel16.setBounds(new Rectangle(316, 41, 26, 19));
    cLabel17.setText("Fec.Ult.Inv");
    cLabel17.setBounds(new Rectangle(6, 2, 58, 16));
    feulinE.setBounds(new Rectangle(64, 2, 107, 16));
    cLabel18.setText("Repres.");
    cLabel18.setBounds(new Rectangle(8, 38, 65, 16));
    rep_codiE.setAncTexto(30);
    rep_codiE.setBounds(new Rectangle(70, 38, 231, 18));
    opVenta.setMargin(new Insets(0, 0, 0, 0));
    opVenta.setSelected(true);
    opVenta.setText("Ventas");
    opVenta.setBounds(new Rectangle(7, 78, 65, 17));
    opCompra.setMargin(new Insets(0, 0, 0, 0));
    opCompra.setSelected(true);
    opCompra.setText("Compras");
    opCompra.setBounds(new Rectangle(79, 78, 77, 17));
    opDespSal.setToolTipText("Despieces (Salidas)");
    opDespSal.setMargin(new Insets(0, 0, 0, 0));
    opDespSal.setSelected(true);
    opDespSal.setText("Desp. (Salida)");
    opDespSal.setBounds(new Rectangle(155, 78, 99, 17));
    opDesEnt.setToolTipText("Despieces (Entradas)");
    opDesEnt.setMargin(new Insets(0, 0, 0, 0));
    opDesEnt.setSelected(true);
    opDesEnt.setText("Desp. (Entrada)");
    opDesEnt.setBounds(new Rectangle(255, 78, 116, 17));
    opRegul.setToolTipText("Regularizaciones");
    opRegul.setMargin(new Insets(0, 0, 0, 0));
    opRegul.setSelected(true);
    opRegul.setText("Regul.");
    opRegul.setBounds(new Rectangle(374, 78, 57, 17));
    incCostoL.setBounds(new Rectangle(440,78,50,17));
    incCostoL.setToolTipText("Incremento de Costo");
    pro_cosincE.setBounds(new Rectangle(492,78,40,17));
    cLabel19.setText("Lote");
    cLabel19.setBounds(new Rectangle(464, 18, 27, 17));
    pro_loteE.setBounds(new Rectangle(491, 18, 48, 16));
    cLabel20.setRequestFocusEnabled(true);
    cLabel20.setText("Zona");
    cLabel20.setBounds(new Rectangle(343, 37, 60, 16));
    zon_codiE.setBounds(new Rectangle(404, 37, 235, 18));
    zon_codiE.setAncTexto(30);
    cLabel21.setText("Kil.Desp. Entra");
    cLabel21.setBounds(new Rectangle(427, 2, 82, 16));
    kgDesEntE.setEnabled(false);
    kgDesEntE.setBounds(new Rectangle(427, 18, 78, 17));
    cLabel22.setText("Kil.Desp. Salida");
    cLabel22.setBounds(new Rectangle(423, 34, 86, 16));
    kgDesSalE.setEnabled(false);
    kgDesSalE.setBounds(new Rectangle(427, 48, 78, 17));
    opStkNeg.setToolTipText("Resetar Costo si Stock < 0");
    opStkNeg.setMargin(new Insets(0, 0, 0, 0));
    opStkNeg.setText("Reset Costo");
    opStkNeg.setBounds(new Rectangle(448, 2, 107, 16));
    opStkNeg.setFocusable(false);
    opStkNeg.setSelected(true);
    opSerieX.setToolTipText("Incluir Traspasos entre Almacenes");
    opSerieX.setMargin(new Insets(0, 0, 0, 0));
    opSerieX.setText("Inc. Trasp.Alm.");
    opSerieX.setFocusable(false);
    cLabel23.setText("Ind");
    cLabel23.setBounds(new Rectangle(565, 20, 23, 16));
    pro_numindE.setText("");
    pro_numindE.setBounds(new Rectangle(599, 19, 40, 16));
    almacen.setText("Almacen");
    almacen.setBounds(new Rectangle(17, 56, 51, 20));
    alm_codiE.setAncTexto(25);
    alm_codiE.setBounds(new Rectangle(71, 58, 200, 18));
    opDesES.setToolTipText("Buscar SOLO Desp.Salida");
    opDesES.setMargin(new Insets(0, 0, 0, 0));
    opDesES.setText("Solo Desp. Salida");
    opDesES.setBounds(new Rectangle(275, 58, 128, 18));
    opSerieX.setBounds(new Rectangle(405, 58, 110, 18));
    opCostFij.setToolTipText("Usar Costos Fijos Desp.");
    opCostFij.setBounds(new Rectangle(518, 58, 110, 18));
    opValDesp.setBounds(new Rectangle(566, 2, 74, 16));
    opValDesp.setText("Val. DE");
    opValDesp.setMargin(new Insets(0, 0, 0, 0));
    opValDesp.setToolTipText("Considerar despieces de Salida como entradas en negativo");
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);

    Pprinc.add(Pentra,        new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 0, 0), 0, 0));
    Pentra.add(cLabel17, null);
    Pentra.add(feulinE, null);
    Pprinc.add(jt, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
                                          , GridBagConstraints.CENTER,
                                          GridBagConstraints.BOTH,
                                          new Insets(0, 2, 0, 0), 0, 0));
    Pprinc.add(PtotalE, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.NONE,
                                               new Insets(0, 2, 0, 0), 0, 0));
    PtotalE.add(impVentaE, null);
    PtotalE.add(cLabel6, null);
    PtotalE.add(pmVentaE, null);
    PtotalE.add(cLabel4, null);
    PtotalE.add(cLabel5, null);
    PtotalE.add(cLabel9, null);
    PtotalE.add(cLabel7, null);
    PtotalE.add(cLabel10, null);
    PtotalE.add(kgVentaE, null);
    PtotalE.add(kgCompraE, null);
    PtotalE.add(cLabel11, null);
    PtotalE.add(impCompra1, null);
    PtotalE.add(cLabel8, null);
    PtotalE.add(pmCompraE, null);
    PtotalE.add(cLabel13, null);
    PtotalE.add(cLabel15, null);
    PtotalE.add(ganKilPorE, null);
    PtotalE.add(ganKilE, null);
    PtotalE.add(cLabel12, null);
    PtotalE.add(cLabel14, null);
    PtotalE.add(impGanaE, null);
    PtotalE.add(cLabel16, null);
    PtotalE.add(cLabel21, null);
    PtotalE.add(kgDesEntE, null);
    PtotalE.add(cLabel22, null);
    PtotalE.add(kgDesSalE, null);
    Pentra.add(fecfinE, null);
    Pentra.add(cLabel1, null);
    Pentra.add(feciniE, null);
    Pentra.add(cLabel2, null);
    Pentra.add(opStkNeg, null);
    Pentra.add(pro_codiE, null);
    Pentra.add(cLabel3, null);
    Pentra.add(cLabel18, null);
    Pentra.add(rep_codiE, null);
    Pentra.add(almacen, null);
    Pentra.add(alm_codiE, null);
    Pentra.add(opDesES, null);
    Pentra.add(opRegul, null);
    Pentra.add(incCostoL, null);
    Pentra.add(pro_cosincE,null);
    Pentra.add(opDesEnt, null);
    Pentra.add(opDespSal, null);
    Pentra.add(opVenta, null);
    Pentra.add(opCompra, null);
    Pentra.add(opCostFij, null);
    Pentra.add(cLabel19, null);
    Pentra.add(pro_loteE, null);
    Pentra.add(opSerieX, null);
    Pentra.add(opValDesp, null);
    Pentra.add(pro_numindE, null);
    Pentra.add(cLabel23, null);
    Pentra.add(zon_codiE, null);
    Pentra.add(Bacepta, null);
    Pentra.add(cLabel20, null);
    Pentra.setDefButton(Bacepta);
  }

    @Override
  public void iniciarVentana() throws Exception
  {
    String s;
    mvtosAlm.setAccesoEmp(empPanel.getStringAccesos(dtStat, EU.usuario,true));

    alm_codiE.setFormato(true);
    alm_codiE.setFormato(Types.DECIMAL, "#9", 2);
    s = "SELECT alm_codi,alm_nomb FROM v_almacen " +
        " ORDER BY alm_codi";
    dtStat.select(s);
    alm_codiE.addDatos(dtStat);
    alm_codiE.setText("0");
    pro_codiE.iniciar(dtStat, this, vl, EU);
    rep_codiE.setFormato(Types.CHAR, "XX", 2);
    rep_codiE.texto.setMayusc(true);
    rep_codiE.setFormato(true);
    Pentra.setButton(KeyEvent.VK_F4,Bacepta);
    String feulin=MvtosAlma.llenaComboFecInv(dtStat,EU.em_cod,EU.ejercicio,feulinE,0);
    feciniE.setText(feulin);
    fecfinE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
    MantRepres.llenaLinkBox(rep_codiE,dtCon1);
    zon_codiE.setFormato(Types.CHAR, "XX", 2);
    zon_codiE.texto.setMayusc(true);

    gnu.chu.anjelica.pad.pdconfig.llenaDiscr(dtCon1,zon_codiE,"Cz",EU.em_cod);
    pro_numindE.setText("",false);
    activarEventos();
    this.setEnabled(true);
    PtotalE.setEnabled(false);
    pro_codiE.requestFocus();
  }
  public void setProCodi(int proCodi)
  {
      pro_codiE.setValorInt(proCodi,true);
  }
  public void setLote(int lote)
  {
      pro_loteE.setValorInt(lote);
  }
  public void setIndividuo(int indiv)
  {
      pro_numindE.setValorInt(indiv);
  }
  
  public void ejecutaConsulta()
  {
      Bacepta.doClick();
  }
  void activarEventos()
  {
    alm_codiE.addCambioListener(new CambioListener()
    {
            @Override
            public void cambio(CambioEvent event) {
                 opSerieX.setSelected(alm_codiE.getValorInt()!=0);
            }
    });
    Bacepta.addActionListener(new ActionListener()
    {
            @Override
      public void actionPerformed(ActionEvent e)
      {
        buscaMvtos();
      }
    });
      popEspere_BCancelaraddActionListener(new ActionListener()
       {
        @Override
        public void actionPerformed(ActionEvent e)
        {
           mvtosAlm.setCancelarConsulta(true);
           mensaje("A esperar.. estoy cancelando la consulta");
        }
       });
     feciniE.addFocusListener(new FocusAdapter() {
          @Override
         public void focusLost(FocusEvent e) {
             if (feciniE.isNull())
                feciniE.setText(feulinE.getText());
         }
     });
     jt.addMouseListener(new MouseAdapter() {
          public void mouseClicked(MouseEvent e) {
              if (e.getClickCount()<2)
                  return;
              if (jf==null)
                  return;
              String coment=jt.getValString(9);
              
              if (coment.contains("N. Desp:"))
              {
                  irDespiece(coment);
                  return;
              }
              ejecutable prog;
              if (jt.getValString(3).equals("VE"))
              {
                 if ((prog=jf.gestor.getProceso(pdalbara.getNombreClase()))==null)
                    return;
                 pdalbara cm=(pdalbara) prog;
                 if (cm.inTransation())
                 {
                    msgBox("Mantenimiento Albaranes de Ventas ocupado. No se puede realizar la busqueda");
                    return;
                 }
                 cm.PADQuery();
                 int n=coment.indexOf("(")+1;
                 cm.setSerieAlbaran(coment.substring(n,n+1));
                 cm.setNumeroAlbaran(coment.substring(n+1,coment.indexOf(")")));
                 cm.ej_query();
                 jf.gestor.ir(cm);
              }
              if (jt.getValString(3).equals("CO"))
              {
                 if ((prog=jf.gestor.getProceso(MantAlbComCarne.getNombreClase()))==null)
                    return;
                 MantAlbComCarne cm=(MantAlbComCarne) prog; 
                 if (cm.inTransation())
                 {
                    msgBox("Mantenimiento Albaranes de Compras ocupado. No se puede realizar la busqueda");
                    return;
                 }
                 cm.PADQuery();
                 int n=coment.indexOf("(")+1;
                 cm.setAccCodi(coment.substring(n,coment.indexOf(")")));
                 cm.ej_query();
                 jf.gestor.ir(cm);
              }   
          }
      });
  }
  
  void irDespiece(String coment)
  {
    ejecutable prog;
    if ((prog=jf.gestor.getProceso(MantDesp.getNombreClase()))==null)
      return;
    MantDesp cm=(MantDesp) prog;
    if (cm.inTransation())
    {
        msgBox("Mantenimiento Despieces ocupado. No se puede realizar la busqueda");
        return;
    }
    cm.PADQuery();
    cm.setDeoCodi(coment.substring(coment.indexOf(":")+1));
    cm.ej_query();
    jf.gestor.ir(cm);
  }
  void buscaMvtos()
  {
    if (fecfinE.getError() || fecfinE.isNull())
    {
      mensajeErr("Fecha Final NO VALIDA");
      return;
    }
    if (feciniE.getError() || feciniE.isNull())
    {
      mensajeErr("Fecha Inicial NO Valida");
      return;
    }
    if (pro_codiE.isNull())
    {
      mensajeErr("Introduzca Código de Producto");
      pro_codiE.requestFocus();
      return;
    }
    new miThread("")
    {
        @Override
        public void run()
        {
          try
          {
            jt.panelG.setVisible(false);
            msgEspere("Buscando datos ... ");
           
            buscaStock1(feulinE.getText(), feciniE.getText(), fecfinE.getText(),
                       pro_codiE.getValorInt());

            resetMsgEspere();

            jt.panelG.setVisible(true);
            if (cancelarConsulta)
            {
               mensajeErr("Consulta CANCELADA!!");
               jt.removeAllDatos();
            }
            else
               mensajeErr("OKEI -- MAKEI");
            mensaje("");
            
          }
          catch (Exception ex)
          {
            Error("Error al buscar Stock", ex);
          }
        }
    };

  }

  void buscaStock1(String feulin,String fecIni, String fecFin, int proCodi) throws Exception
  {
     cancelarConsulta=false;
     mvtosAlm.setCancelarConsulta(false);
     fecMov=null;
     if (alm_codiE.getText().trim().equals(""))
       almCodi=0;
     else
       almCodi=alm_codiE.getValorInt();
     valDesp=opValDesp.isSelected();

    String zonCodi=zon_codiE.getText().trim().equals("")?null:zon_codiE.getText();
    if (zonCodi != null)
    {
      if (zonCodi.equals("*") || zonCodi.equals("**"))
        zonCodi = null;
    }
    String zonCre=rep_codiE.getText().trim().equals("")?null:rep_codiE.getText();
    if (zonCre != null)
    {
      if (zonCre.equals("*") || zonCre.equals("**"))
        zonCre = null;
    }
    if (zonCodi!=null)
      zonCodi=zonCodi.replace('*','.');
    if (zonCre!=null)
      zonCre=zonCre.replace('*','.');

    mvtosAlm.setPadre(this);
    mvtosAlm.setIncUltFechaInv(true);
    mvtosAlm.setLote(pro_loteE.getValorInt());
    mvtosAlm.setIndividuo(pro_numindE.getValorInt());
    mvtosAlm.setAlmacen(almCodi);
    mvtosAlm.setDespSalida(opDesES.isSelected());
    mvtosAlm.setSerieX(opSerieX.isSelected());
    mvtosAlm.setValorarDesp(valDesp);
    mvtosAlm.setVerRegul(opRegul.isSelected());
    mvtosAlm.setVerVenta(opVenta.isSelected());
    mvtosAlm.setVerCompra(opCompra.isSelected());
    mvtosAlm.setCostoFijo(opCostFij.isEnabled() && opCostFij.isSelected()?costoFijo:-1);
    
    mvtosAlm.setVerDesEnt(opDesEnt.isSelected());
    mvtosAlm.setVerDespSal(opDespSal.isSelected());
    mvtosAlm.setResetCostoStkNeg(opStkNeg.isSelected());
    mvtosAlm.setIncrementarCosto(pro_cosincE.getValorDec());
    if (! mvtosAlm.calculaMvtos(feulin,fecIni,fecFin,proCodi,zonCodi,zonCre,pro_loteE.getValorInt(),dtCon1,dtStat,jt))
    {
        msgBox("No encontrados movimientos para estas condiciones");
        jt.removeAllDatos();
        PtotalE.resetTexto();
        return;
    }

    kgVentaE.setValorDec(mvtosAlm.getKilosVenta());
    impVentaE.setValorDec(mvtosAlm.getImporteVenta());
    pmVentaE.setValorDec(mvtosAlm.getImporteVenta()==0?0:mvtosAlm.getImporteVenta()/mvtosAlm.getKilosVenta());

    kgCompraE.setValorDec(mvtosAlm.getKilosEntrada());
    impCompra1.setValorDec(mvtosAlm.getImporteEntrada());
    pmCompraE.setValorDec(mvtosAlm.getKilosEntrada()==0?0:mvtosAlm.getImporteEntrada()/mvtosAlm.getKilosEntrada());

    impGanaE.setValorDec(mvtosAlm.getImpGana());
    ganKilE.setValorDec(mvtosAlm.getKilosVenta()==0?0:mvtosAlm.getImpGana()/mvtosAlm.getKilosVenta());
    ganKilPorE.setValorDec((pmCompraE.getValorDec()==0  || ganKilE.getValorDec() == 0)
            ? 0:ganKilE.getValorDec()/ pmCompraE.getValorDec()*100 );
    kgDesEntE.setValorDec(mvtosAlm.getKilosEntDesp());
    kgDesSalE.setValorDec(mvtosAlm.getKilosSalDesp());

  }

//   void buscaStock(String feulin,String fecIni, String fecFin, int proCodi) throws Exception
//   {
//       msgBox("en buscastock");
//     String s;
//     cancelarConsulta=false;
//     fecMov=null;
//     char sel;
//     if (alm_codiE.getText().trim().equals(""))
//       almCodi=0;
//     else
//       almCodi=alm_codiE.getValorInt();
//     String cliNomb;
//     boolean swErr=false;
//     String feulst; // Fecha Ultimo Stock
//     double precio;
//     String tipMov;
//     double canti;
//     int unid;
//
//     int ejeNume=EU.ejercicio;
//     int empCodi=EU.em_cod;
//
//     acepNeg=opStkNeg.isSelected();
//     valDesp=opValDesp.isSelected();
//
//      s = "";
//
//    String fefi=fecIni;
//    GregorianCalendar gc= new GregorianCalendar();
//    gc.setTime(Formatear.getDate(fecIni, "dd-MM-yyyy"));
//    gc.add(GregorianCalendar.DAY_OF_MONTH, -1);
//    fefi=Formatear.getFecha(gc.getTime(),"dd-MM-yyyy");
//
//    String zonCodi=zon_codiE.getText().trim().equals("")?null:zon_codiE.getText();
//    if (zonCodi != null)
//    {
//      if (zonCodi.equals("*") || zonCodi.equals("**"))
//        zonCodi = null;
//    }
//    String zonCre=rep_codiE.getText().trim().equals("")?null:rep_codiE.getText();
//    if (zonCre != null)
//    {
//      if (zonCre.equals("*") || zonCre.equals("**"))
//        zonCre = null;
//    }
//    if (zonCodi!=null)
//      zonCodi=zonCodi.replace('*','.');
//    if (zonCre!=null)
//      zonCre=zonCre.replace('*','.');
//
//    mvtosAlm.setLote(0);
//    mvtosAlm.setIndividuo(0);
//    mvtosAlm.setAlmacen(almCodi);
//    mvtosAlm.setDespSalida(opDesES.isSelected());
//    mvtosAlm.setSerieX(opSerieX.isSelected());
//    mvtosAlm.setValorarDesp(valDesp);
//    s = mvtosAlm.getSqlMvt(feulin, fefi, proCodi, null,null);
//    debug(s);
//
//
//    double kg;
//    int cj;
//    double canStk=0;
//    int uniStk=0;
//    double preStk=0;
//    feulst="";
//    // Primero tratamos lo que NO  se va a mostrar en pantalla. Es decir calculamos
//    // todo desde la fecha de inventnario hasta la fecha inicial que nos han marcado.
//    if (dtCon1.select(s))
//    {
////      debug("Select: "+dtCon1.getStrSelect()+"\n");
//      do
//      {
//          if (cancelarConsulta)
//              return ;
//        fecMov=dtCon1.getFecha("fecmov");
//        precio =preStk;
//        tipMov=dtCon1.getString("tipmov");
//        canti=0;
//        unid=0;
//        sel=dtCon1.getString("sel").charAt(0);
//        if (dtCon1.getString("sel").equals("DE"))
//          sel='d';
//        if (tipMov.equals("="))
//        {
//          if (!feulst.equals(dtCon1.getFecha("fecmov")))
//          {
//            feulst = dtCon1.getFecha("fecmov");
//            precio = dtCon1.getDouble("precio");
//            canStk = 0;
//            uniStk=0;
//            canti = dtCon1.getDouble("canti");
//            cj=dtCon1.getInt("unidades");
//          }
//          else
//            tipMov = "+";
//        }
//
//        if (tipMov.equals("+"))
//        { // Entrada.
//          kg = dtCon1.getDouble("canti");
//          cj=dtCon1.getInt("unidades");
//          if (sel=='D') // Despiece (Salida Almacen - Cabecera)
//          { // Si el tipo Mov. es positivo es porque se estan tomando como entradas.
//            kg = kg * -1;
//            cj = cj * -1;
//          }
//
//          canti = canStk + kg;
//          unid = uniStk + cj;
//
//          if (sel=='d')
//          { // Entrada a almacen del despiece ... El producto resultante, vamos.
//            if ((dtCon1.getInt("pro_codori") == proCodi && ! valDesp)||
//                dtCon1.getDouble("precio") == 0)
//            {
//            // Si el codigo de prod. de entrada = producto que estamos tratando
//            // y NO Influyen despieces  salida en costos entonces NO hacemos
//            // caso a esa entrada para los precios
//              canStk = canti;
//              uniStk = unid;
//              continue;
//            }
//          }
//
//          if ((canStk < 0 || canti < 0) && acepNeg)
//          { // Cantidad anterior en Negativo
//            if (swErr == false)
//            {
//              swErr = true;
//              if (MSGBOX)
//                msgBox("Atencion Stock en Negativo: " + canStk +
//                     " EN Fecha: " + dtCon1.getFecha("fecmov"));
//              else
//                mensajeErr("Atencion Stock en Negativo: " + canStk +
//                       " EN Fecha: " + dtCon1.getFecha("fecmov"));
//
//            }
//            // Si el stock acumulado < 0 entonces ponemos el precio igual al de esta entrada.
//            // Pasamos de los acumulados, vaya.
//            precio= dtCon1.getDouble("precio");
//          }
//          else
//          {
//            precio = (preStk * canStk) +
//                (dtCon1.getDouble("canti") * dtCon1.getDouble("precio"));
//            if ((precio >= 0.01 || precio <= -0.01) && (canti >= 0.01 || canti <= -0.01))
//              precio = precio / canti;
//            else
//              precio=preStk;
//          }
//        }
//        if (tipMov.equals("-"))
//        {
//          canti = canStk - dtCon1.getDouble("canti");
//          unid  = uniStk - dtCon1.getInt("unidades");
//        }
//        preStk = precio;
//        canStk = canti;
//        uniStk = unid;
//      } while (dtCon1.next());
//    }
//    // Ahora tratamos los movimientos de la fecha.
//    // Los q se van a ver en pantalla.
//
//    mvtosAlm.setLote(pro_loteE.getValorInt());
//    s = mvtosAlm.getSqlMvt(fecIni, fecFin, proCodi,zonCre,zonCodi);
//
//    double kgVen=0;
//    double prVen=0;
//    double kgEnt=0;
//    double prEnt=0;
//    double impGana=0;
//    double kgSalDes=0;
//    double kgEntDes=0;
//    boolean swPas1;
//    jt.removeAllDatos();
//    if (dtCon1.select(s))
//    {
//      fecMov=dtCon1.getFecha("fecmov");
//      feulst="";
////      debug("\n Select: "+dtCon1.getStrSelect());
//      do
//      {
//          if (cancelarConsulta)
//              return;
//
//        canti = 0;
//        unid=0;
//        precio =preStk;
//        tipMov=dtCon1.getString("tipmov");
//        sel=dtCon1.getString("sel").charAt(0);
//        if (dtCon1.getString("sel").equals("DE"))
//          sel='d';
//
//        if (tipMov.equals("+"))
//        {
//            kgEnt+=dtCon1.getDouble("canti"); // Ignoramos los Inventar.
//            prEnt+=dtCon1.getDouble("precio")*dtCon1.getDouble("canti");
//        }
//        if (tipMov.equals("="))
//        {
//            if (!feulst.equals(dtCon1.getFecha("fecmov")))
//            {
//              feulst = dtCon1.getFecha("fecmov");
//              precio = dtCon1.getDouble("precio") ;
//              canStk=0;
//              uniStk=0;
//              canti = dtCon1.getDouble("canti");
//              unid= dtCon1.getInt("unidades");
//            }
//            else
//              tipMov = "+";
//        }
//
//        if (tipMov.equals("+"))
//        { // Entrada.
//          kg=dtCon1.getDouble("canti");
//          cj=dtCon1.getInt("unidades");
//          if (sel=='D')
//          {
//            kg = kg * -1;
//            cj = cj * -1;
//          }
//
//          canti = canStk + kg;
//          unid = uniStk + cj;
//          swPas1=false;
//          if (sel=='d')
//          {
//            if ((dtCon1.getInt("pro_codori") == proCodi && ! valDesp) ||
//                dtCon1.getDouble("precio") == 0)
//              swPas1=true; // Marco para que pase de tener en cuenta este mvto. para precios
//          }
//          if (swPas1==false)
//          {
//              if ((canStk < 0 || canti < 0) && acepNeg)
//              { // Cant. anterior y/o actual en Negativo
//                if (swErr == false)
//                {
//                  swErr = true;
//                  msgBox("Atencion Stock en Negativo: " + canStk +
//                         " EN Fecha: " + dtCon1.getFecha("fecmov"));
//                }
//                else
//                  mensajeErr("Atencion Stock en Negativo: " + canStk +
//                             " EN Fecha: " + dtCon1.getFecha("fecmov"));
//                precio = dtCon1.getDouble("precio");
//              }
//              else
//              {
//                precio = (preStk * canStk) +
//                    (kg * dtCon1.getDouble("precio"));
//                if ((precio >= 0.01 || precio <= -0.01) && (canti >= 0.01 || canti <= -0.01))
//                  precio = precio / canti;
//                else
//                  precio=preStk;
//              }
//            }
//        }
//        if (tipMov.equals("-"))
//        { // Es una salida.
//          if (sel=='V')
//          {
//            if (zonCodi!= null)
//            {
//              if (!dtCon1.getString("zonrep").toUpperCase().matches(zonCodi))
//              { // NO coinciden las zonas. Anulo los kilos y unid. q luego sumare.
//                canti = canStk - dtCon1.getDouble("canti", true);
//                unid =  uniStk - dtCon1.getInt("unidades", true);
//                preStk = precio;
//                canStk = canti;
//                uniStk = unid;
//                continue;
//              }
//            }
//            if (zonCre != null)
//            {
//              if (!dtCon1.getString("zoncre").toUpperCase().matches(zonCre))
//              { // NO coinciden el Representante. Anulo los kilos y unid. q luego sumare.
//                canti = canStk - dtCon1.getDouble("canti", true);
//                unid = uniStk -  dtCon1.getInt("unid", true);
//                preStk = precio;
//                canStk = canti;
//                uniStk = unid;
//                continue;
//              }
//            }
//          }
//          if ((sel=='V' || sel=='R' && zonCodi==null))
//          { // Tener solo en cuenta Ventas y Regularizaciones.
//            if (sel=='V' && dtCon1.getInt("div_codi")<=0 && ! EU.isRootAV())
//              EU.isRootAV();
//            else
//            {
//              kgVen += dtCon1.getDouble("canti");
//              prVen += dtCon1.getDouble("canti", true) * dtCon1.getDouble("precio", true);
//              impGana += dtCon1.getDouble("canti", true) *
//                  (dtCon1.getDouble("precio", true) - preStk);
//            }
//          }
//          canti = canStk - dtCon1.getDouble("canti",true);
//          unid = uniStk - dtCon1.getInt("unidades");
//        }
//        preStk=precio;
//        canStk=canti;
//        uniStk = unid;
//        if (sel=='d')
//          kgEntDes += dtCon1.getDouble("canti",true);
//        if (sel=='D')
//          kgSalDes += dtCon1.getDouble("canti",true);
//
//
//        if (sel == 'V' && !opVenta.isSelected())
//            continue;
//        if (sel=='C' && !opCompra.isSelected())
//            continue;
//        if (sel=='d' && !opDesEnt.isSelected())
//              continue;
//        if (sel=='D' && !opDespSal.isSelected())
//          continue;
//        if (sel=='R' && !opRegul.isSelected())
//          continue;
//        if (sel=='V' && dtCon1.getInt("div_codi")<=0 && ! EU.isRootAV())
//          continue;
//        Vector v = new Vector();
//        v.addElement(dtCon1.getFecha("fecmov", "dd-MM-yyyy"));
//        if (tipMov.equals("+") || tipMov.equals("="))
//        {
//          if (sel=='D')
//            v.addElement(Formatear.format(dtCon1.getDouble("canti") * -1,
//                                          "---,--9.99"));
//          else
//            v.addElement(Formatear.format(dtCon1.getString("canti"),
//                                          "---,--9.99"));
//          v.addElement("");
//        }
//        else
//        {
//          v.addElement("");
//          v.addElement(Formatear.format(dtCon1.getString("canti"),
//                                        "---,--9.99"));
//        }
//        if (dtCon1.getString("tipmov").equals("="))
//          v.addElement("IN");
//        else
//          v.addElement(dtCon1.getString("sel"));
//        v.addElement(Formatear.format(dtCon1.getString("precio"),
//                                      "---,--9.99"));
//        v.addElement(Formatear.format(uniStk, "---,--9"));
//        v.addElement(Formatear.format(canStk, "---,--9.99"));
//        v.addElement(Formatear.format(preStk, "---,--9.99"));
//        v.addElement(Formatear.format(impGana, "---,--9.99"));
//        cliNomb="";
//        if (sel=='V')
//        {
//          if (dtStat.select("select cli_nomb from clientes"+
//                           " where cli_codi="+dtCon1.getInt("cliCodi",true)))
//           cliNomb = "("+dtCon1.getString("numalb")+") "+dtStat.getString("cli_nomb");
//        }
//        if (sel=='C')
//        {
//          if (dtStat.select("select prv_nomb from v_proveedo"+
//                           " where prv_codi="+dtCon1.getInt("cliCodi")))
//           cliNomb = "("+dtCon1.getString("numalb")+") "+ dtStat.getString("prv_nomb");
//        }
//        if (sel=='D' || sel=='d')
//          cliNomb="N. Desp: "+dtCon1.getString("numalb");
//        v.addElement(""+dtCon1.getInt("cliCodi",true)+" "+cliNomb);
//        v.addElement(dtCon1.getString("ejenume")+"-"+dtCon1.getString("empcodi")+"-"+
//                     dtCon1.getString("lote")+"-"+dtCon1.getString("numind") );
//        if (sel=='V')
//          v.addElement(Formatear.format(dtCon1.getDouble("precio")-preStk,"---9.99"));
//        else
//          v.addElement("");
//        jt.addLinea(v);
//
//      } while (dtCon1.next());
//      jt.requestFocusInicio();
//    }
//    kgVentaE.setValorDec(kgVen);
//    impVentaE.setValorDec(prVen);
//    pmVentaE.setValorDec(kgVen==0?0:prVen/kgVen);
//
//    kgCompraE.setValorDec(kgEnt);
//    impCompra1.setValorDec(prEnt);
//    pmCompraE.setValorDec(kgEnt==0?0:prEnt/kgEnt);
//
//    impGanaE.setValorDec(impGana);
//    ganKilE.setValorDec(kgVen==0?0:impGana/kgVen);
//    kgDesEntE.setValorDec(kgEntDes);
//    kgDesSalE.setValorDec(kgSalDes);
//    this.setEnabled(true);
////    return;
// }

//  private String getSqlMvt1(String fecIni, String fecFin, int proCodi, String s,
//                           int ejeNume, int empCodi,String zonCre,String zoncli,int lote)
//  {
//    if (zonCre != null)
//      zonCre = zonCre.replace('*', '%');
//    if (zoncli != null)
//      zoncli = zoncli.replace('*', '%');
//    if (opDesES.isSelected())
//      return  " select 'DE' as sel, '+' as tipmov,'' as fecmov,"+
//          "  l.def_serlot as serie,l.pro_lote as  lote,"+
//          " l.def_kilos as canti,l.def_prcost as precio,l.pro_numind as numind,"+
//          " 0 as cliCodi,l.deo_codi  as numalb,l.def_ejelot as ejenume, "+
//          " l.def_emplot as empcodi,'0' as pro_codori "+
//          ", '' as zoncre,'' as zonrep "+
//          ", l.def_numpie as unidades,0 as div_codi "+
//          " from  v_despfin l where "+
//          " l.def_kilos <> 0 "+
//          (almCodi==0?"":" and alm_codi = "+almCodi)+
//          (lote==0?"":" and l.pro_lote  = "+lote)+
//          (pro_numindE.getText().equals("")?"":" and l.pro_numind = "+pro_numindE.getValorInt())+
//          " AND l.pro_codi = " + proCodi +
//          " ORDER BY 3,2 desc"; // FECHA y tipo
//
//    s="SELECT 'CO' as sel,'+' as tipmov,c.acc_fecrec as fecmov, c.acc_serie as serie,"+
//        " c.acc_nume as  lote,"+
//        " l.acl_canti as canti,l.acl_prcom as precio,0 as numind,"+
//        " c.prv_codi as cliCodi,  c.acc_nume as numalb, "+
//        " c.acc_ano as ejeNume,c.emp_codi as empCodi,l.pro_codi as pro_codori "+
//        ", '' as zoncre,'' as zonrep,acl_numcaj as unidades,0 as div_codi "+
//        " FROM v_albacoc c,v_albacol l " + //,v_albcompar i "+
//        " where c.emp_codi = l.emp_codi "+
//        " AND c.acc_serie = l.acc_serie "+
//        " AND c.acc_nume = l.acc_nume "+
//        " and c.acc_ano = l.acc_ano "+
//        " and l.acl_canti <> 0 "+
//        (almCodi==0?"":" and l.alm_codi = "+almCodi)+
//        (lote==0?"":" and c.acc_nume = "+lote)+
//        " AND l.pro_codi = "+proCodi+
//        " AND c.acc_fecrec >= TO_DATE('"+fecIni+"','dd-MM-yyyy') "+
//        " and c.acc_fecrec <= TO_DATE('"+fecFin+"','dd-MM-yyyy') ";
//    s+=" UNION all"; // Albaranes de Venta
//    s+= " select 'VE' as sel,'-' as tipmov,c.avc_fecalb as fecmov,"+
//        "  i.avp_serlot  as serie,i.avp_numpar as  lote,"+
//        " i.avp_canti  as canti,l.avl_prven as precio,i.avp_numind as numind,"+
//        " c.cli_codi as cliCodi,c.avc_nume as numalb, "+
//        " i.avp_ejelot as ejeNume,i.avp_emplot as empcodi,l.pro_codi as pro_codori, "+
//        (opSerieX.isSelected()?" '' as zoncre,'' as zonrep":" cli_zoncre as zoncre,cli_zonrep as zonrep ")+
//        ", avp_numuni as unidades, c.div_codi "+
//        "  from v_albavel l, v_albvenpar i, v_albavec c"+
//        (opSerieX.isSelected()?"":" left join   clientes as cl on cl.cli_codi = c.cli_codi ")+
//        " where c.emp_codi = l.emp_codi "+
//          " AND c.avc_serie = l.avc_serie "+
//          (opSerieX.isSelected()?"":" AND C.AVC_SERIE !='X'")+
//          " AND c.avc_nume = l.avc_nume "+
//          " and c.avc_ano = l.avc_ano "+
//          (almCodi==0?"":" and c.alm_codori = "+almCodi)+
//          " and l.avl_canti <> 0 "+
//          " and i.emp_codi = l.emp_codi " +
//          " AND i.avc_serie = l.avc_serie " +
//          " AND i.avc_nume = l.avc_nume " +
//          " and i.avc_ano = l.avc_ano " +
//          " and i.avl_numlin = l.avl_numlin " +
////          " and i.pro_codi = l.pro_codi "+
//          (lote==0?"":" and i.avp_numpar  = "+lote)+
//          (pro_numindE.getText().equals("")?"":" and i.avp_numind = "+pro_numindE.getValorInt())+
////          " AND c.avc_ano = "+ejeNume+
//          " AND l.pro_codi = "+proCodi+
//          " AND c.avc_fecalb >= TO_DATE('"+fecIni+"','dd-MM-yyyy') "+
//          " and c.avc_fecalb <= TO_DATE('"+fecFin+"','dd-MM-yyyy') ";
//      s+=" UNION all "+ // Despieces (Salidas de Stock)
//        " select 'DS' as sel,'"+
//         (valDesp?"+":"-")+"' as tipmov ,"+
//         " deo_fecha as fecmov,"+
//        "  deo_serlot as serie,pro_lote as  lote,"+
//        " deo_kilos as canti,deo_prcost as precio,pro_numind as numind,"+
//        " 0 as cliCodi,deo_codi as numalb,deo_ejelot as ejeNume," +
//        " deo_emplot as empcodi,pro_codi as pro_codori "+
//        ", '' as zoncre,'' as zonrep "+
//        ", 1 as unidades,0 as div_codi "+
//        " from  v_desporig where "+
//        "  pro_codi = " + proCodi +
//        " and deo_kilos <> 0 "+
//        (almCodi==0?"":" and deo_almori = "+almCodi)+
//        (lote==0?"":" and pro_lote  = "+lote)+
//        (pro_numindE.getText().equals("")?"":" and pro_numind = "+pro_numindE.getValorInt())+
//        " AND deo_fecha >= TO_DATE('" + fecIni + "','dd-MM-yyyy') " +
//        " and deo_fecha <= TO_DATE('"+fecFin+"','dd-MM-yyyy') ";
//      s+=" UNION all "+ // Despieces (Entradas)
//       " select 'DE' as sel, '+' as tipmov,c.deo_fecha as fecmov,"+
//       "  l.def_serlot as serie,l.pro_lote as  lote,"+
//       " l.def_kilos as canti,l.def_prcost as precio,l.pro_numind as numind,"+
//       " 0 as cliCodi,l.deo_codi  as numalb,l.def_ejelot as ejenume, "+
//       " l.def_emplot as empcodi,c.pro_codi as pro_codori "+
//       ", '' as zoncre,'' as zonrep "+
//       ", l.def_numpie as unidades,0 as div_codi "+
//       " from  v_desporig c,v_despfin l where "+
//       " C.EMP_codi = l.emp_codi "+
//       " and c.eje_nume = l.eje_nume "+
//       " and c.deo_codi = l.deo_codi "+
//       " and l.def_kilos <> 0 "+
//       (almCodi==0?"":" and alm_codi = "+almCodi)+
//       (lote==0?"":" and l.pro_lote  = "+lote)+
//       (pro_numindE.getText().equals("")?"":" and l.pro_numind = "+pro_numindE.getValorInt())+
//       " AND l.pro_codi = " + proCodi +
//       " AND c.deo_fecha >= TO_DATE('" + fecIni + "','dd-MM-yyyy') " +
//       " and c.deo_fecha <= TO_DATE('"+fecFin+"','dd-MM-yyyy') ";
//    s+=" UNION all "+ // Regularizaciones.
//       " select 'RE' as sel,tir_afestk as tipmov,r.rgs_fecha as fecmov,"+
//       "  r.pro_serie as serie,r.pro_nupar as  lote,"+
//       " r.rgs_kilos as canti,r.rgs_prregu as precio,r.pro_numind as numind, "+
//       " 0 as cliCodi,0 as numalb, r.eje_nume as ejeNume,"+
//       " r.emp_codi  as empcodi,r.pro_codi as pro_codori"+
//       ", '' as zoncre,'' as zonrep "+
//       ", rgs_canti as unidades, 0 as div_codi "+
//       " FROM v_regstock r, v_motregu m WHERE "+
//       " m.tir_codi = r.tir_codi "+
//       " and rgs_kilos <> 0 "+
//        " and rgs_trasp != 0 "+
//       (almCodi==0?"":" and alm_codi = "+almCodi)+
//       (lote==0?"":" and r.pro_nupar  = "+lote)+
//        " AND r.pro_codi = " + proCodi +
//        " AND r.rgs_fecha >= TO_DATE('" + fecIni + "','dd-MM-yyyy') " +
//        " and r.rgs_fecha <= TO_DATE('"+fecFin+"','dd-MM-yyyy') ";
//    s+= " ORDER BY 3,2 desc"; // FECHA y tipo
//    return s;
//  }

   
}
