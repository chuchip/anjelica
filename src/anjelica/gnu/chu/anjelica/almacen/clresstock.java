package gnu.chu.anjelica.almacen;

import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.pad.pdprove;
import gnu.chu.camposdb.empPanel;
import gnu.chu.camposdb.proPanel;
import gnu.chu.controles.*;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import net.sf.jasperreports.engine.*;

/**
 *
 * <p>Título: clresstock </p>
 * <p>Descripción: Consulta/Listado Resumen de stock desglosandolo
 * por proveedor y fecha de caducidad</p>
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
 * <p>Empresa: miSL</p>
 * @author chuchi P
 * @version 1.0
 */
public class clresstock extends ventana implements  JRDataSource
{
  
  int n,n1,tlaNulipr;
  ArrayList productos = new ArrayList();
  ArrayList grids=new ArrayList();
  int numProd=-1;
  int linGrid=0;
  int maxLinGrid=0;
  PreparedStatement pst,pst1,pst2;
  boolean swPanel = true;
  DatosTabla dtAux, dtAux1;
  double cantS, cantT,cantC,cantV;
  double cantTS,cantTC,cantTV;
  String s;
  CPanel Pprinc = new CPanel();
  CPanel PtipoCons = new CPanel();
  CLabel cLabel1 = new CLabel();
  CLinkBox tla_codiE = new CLinkBox();
  CButton Baceptar = new CButton("F4 Aceptar", Iconos.getImageIcon("check"));
  CButton Blistar = new CButton(Iconos.getImageIcon("print"));
  CLabel cLabel2 = new CLabel();
  JScrollPane Pscroll = new JScrollPane();
  CPanel Pgrids = new CPanel();
  CLabel cLabel5 = new CLabel();
  CComboBox tla_vekgcaE = new CComboBox();
  CLabel cLabel6 = new CLabel();
  CTextField tla_nuliprE = new CTextField(Types.DECIMAL, "#9");
  CLabel cLabel7 = new CLabel();
  CTextField tla_diagfeE = new CTextField(Types.DECIMAL, "#9");
  CLabel cLabel9 = new CLabel();
  CTextField fecStockE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CCheckBox opIncPedE = new CCheckBox();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CLabel cLabel3 = new CLabel();
  CLinkBox alm_codiE = new CLinkBox();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  proPanel profinE = new proPanel();
  proPanel proiniE = new proPanel();
  CLabel cLabel8 = new CLabel();
  CLabel cLabel4 = new CLabel();
  CLabel cLabel18 = new CLabel();
  CLinkBox cam_codiE = new CLinkBox();
  CLabel cLabel10 = new CLabel();
  empPanel emp_codiE = new empPanel();
  CCheckBox opSoloVend = new CCheckBox();
  private boolean VERNEGATIVO=true;
  
  public clresstock(EntornoUsuario eu, Principal p)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    setTitulo(" Cons./List. Resumen Stock");

    try
    {
      if (jf.gestor.apuntar(this))
        jbInit();
      else
        setErrorInit(true);
    }
    catch (Exception e)
    {
      Logger.getLogger(clresstock.class.getName()).log(Level.SEVERE, null, e);
      setErrorInit(true);
    }
  }

  public clresstock(gnu.chu.anjelica.menu p, EntornoUsuario eu)
  {
    EU = eu;
    vl = p.getLayeredPane();
    setTitulo(" Cons./List. Resumen Stock");
    eje = false;

    try
    {
      jbInit();
    }
    catch (Exception e)
    {
      Logger.getLogger(clresstock.class.getName()).log(Level.SEVERE, null, e);
      setErrorInit(true);
    }
  }

  private void jbInit() throws Exception
  {
    iniciarFrame();
    this.setSize(new Dimension(760, 540));
    this.setVersion("2011-12-07");
    VERNEGATIVO=EU.getValorParam("verNegResStock",VERNEGATIVO);
    statusBar = new StatusBar(this);
    conecta();
    cLabel10.setText("Empr.");
    cLabel10.setBounds(new Rectangle(385, 23, 39, 17));
    emp_codiE.setBounds(new Rectangle(427, 23, 35, 17));
    tla_diagfeE.setToolTipText("Introducir Numero de Dias por los que agrupar");
    cam_codiE.setAncTexto(25);
    tla_diagfeE.setValorDec(3);
    opSoloVend.setToolTipText("Ver solo Productos Vendibles");
    opSoloVend.setSelected(true);
    opSoloVend.setText("SV");
    opSoloVend.setBounds(new Rectangle(467, 23, 43, 17));
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
    Pprinc.setLayout(gridBagLayout2);
    Blistar.setPreferredSize(new Dimension(24, 24));
    Blistar.setMaximumSize(new Dimension(24, 24));
    Blistar.setMinimumSize(new Dimension(24, 24));
    Blistar.setToolTipText("Generar Listado sobre condiciones");

    Pprinc.setBounds(new Rectangle(0, 0, 750, 528));
    PtipoCons.setMaximumSize(new Dimension(732, 64));
    PtipoCons.setMinimumSize(new Dimension(732, 64));
    PtipoCons.setPreferredSize(new Dimension(732, 64));
    Baceptar.setBounds(new Rectangle(632, 41, 113, 22));
    Baceptar.setMargin(new Insets(0, 0, 0, 0));
    tla_codiE.setBounds(new Rectangle(75, 3, 156, 17));
    tla_codiE.setAncTexto(20);
    tla_codiE.getComboBox().setPreferredSize(new Dimension(250,30));
    cLabel1.setBounds(new Rectangle(3, 3, 75, 17));
    Pgrids.setLayout(gridBagLayout1);
    cLabel5.setText("Ver");
    cLabel5.setBounds(new Rectangle(239, 2, 31, 17));
    tla_vekgcaE.setBounds(new Rectangle(264, 3, 96, 17));
    cLabel6.setText("No. Lineas P/Prod.");
    cLabel6.setBounds(new Rectangle(373, 3, 101, 17));
    tla_nuliprE.setBounds(new Rectangle(473, 2, 27, 17));
    cLabel7.setText("Agrupar Fec. Cad.");
    cLabel7.setBounds(new Rectangle(1, 23, 106, 17));
    tla_diagfeE.setBounds(new Rectangle(107, 23, 26, 17));
    cLabel9.setText("Fec.Stock");
    cLabel9.setBounds(new Rectangle(139, 23, 60, 17));
    fecStockE.setSelectionEnd(10);
    fecStockE.setFormato("dd-MM-yyyy");
    fecStockE.setBounds(new Rectangle(196, 23, 73, 17));
    opIncPedE.setText("Inc. Pedidos");
    opIncPedE.setBounds(new Rectangle(282, 23, 101, 17));
    cLabel3.setText("Almacen");
    cLabel3.setBounds(new Rectangle(504, 3, 57, 17));
    alm_codiE.setAncTexto(30);
    alm_codiE.setBounds(new Rectangle(554, 3, 190, 17));

    profinE.setBounds(new Rectangle(362, 43, 267, 17));
    proiniE.setBounds(new Rectangle(52, 43, 267, 17));
    cLabel8.setBounds(new Rectangle(323, 43, 45, 17));
    cLabel8.setText("A Prod.");
    cLabel4.setBounds(new Rectangle(4, 46, 54, 17));
    cLabel4.setText("De Prod.");
    cLabel18.setRequestFocusEnabled(true);
    cLabel18.setText("Cámara");
    cLabel18.setBounds(new Rectangle(515, 22, 48, 17));
    cam_codiE.setBounds(new Rectangle(568, 22, 172, 17));
    statusBar.add(Blistar, new GridBagConstraints(8, 0, 1, 2, 0.0, 0.0,
                                                  GridBagConstraints.EAST,
                                                  GridBagConstraints.VERTICAL,
                                                  new Insets(0, 5, 0, 0), 0, 0));
    Pprinc.add(Pscroll, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0
                                               , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                               new Insets(0, 1, 0, 1), 0, 0));
    Pscroll.getViewport().add(Pgrids, null);
    Pprinc.add(PtipoCons, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                                                 , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                                 new Insets(0, 1, 0, 0), 0, 0));
    PtipoCons.setBorder(BorderFactory.createRaisedBevelBorder());
    PtipoCons.setLayout(null);
    cLabel1.setText("Tipo Listado");
    PtipoCons.add(cLabel2, null);
    PtipoCons.add(cLabel8, null);
    PtipoCons.add(proiniE, null);
    PtipoCons.add(profinE, null);
    PtipoCons.add(cLabel4, null);
    PtipoCons.add(Baceptar, null);
    PtipoCons.add(cLabel7, null);
    PtipoCons.add(opIncPedE, null);
    PtipoCons.add(cLabel10, null);
    PtipoCons.add(emp_codiE, null);
    PtipoCons.add(tla_diagfeE, null);
    PtipoCons.add(cLabel9, null);
    PtipoCons.add(fecStockE, null);
    PtipoCons.add(cLabel5, null);
    PtipoCons.add(tla_codiE, null);
    PtipoCons.add(alm_codiE, null);
    PtipoCons.add(cLabel3, null);
    PtipoCons.add(tla_nuliprE, null);
    PtipoCons.add(cLabel6, null);
    PtipoCons.add(tla_vekgcaE, null);
    PtipoCons.add(cLabel1, null);
    PtipoCons.add(cLabel18, null);
    PtipoCons.add(cam_codiE, null);
    PtipoCons.add(opSoloVend, null);
  }

    @Override
  public void iniciarVentana() throws Exception
  {
    tla_codiE.setFormato(Types.DECIMAL, "#9");
    dtAux = new DatosTabla(dtCon1.getConexion());
    dtAux1 = new DatosTabla(dtCon1.getConexion());
    Pprinc.setButton(KeyEvent.VK_F4, Baceptar);
    int tlaCodi=0;
    s = "SELECT tla_codi,tla_nomb FROM tilialca order by tla_codi";
    if (dtStat.select(s))
      tlaCodi=dtStat.getInt("tla_codi");
    tla_codiE.addDatos(dtStat);
    tla_codiE.addDatos("99", "Definido Usuario");
    tla_codiE.setValorInt(99);
    
    tla_vekgcaE.addItem("Kilos", "K");
    tla_vekgcaE.addItem("Unid", "U");
    alm_codiE.setFormato(true);
    alm_codiE.setFormato(Types.DECIMAL, "#9", 2);
    s = "SELECT alm_codi,alm_nomb FROM v_almacen " +
        " ORDER BY alm_codi";
    dtStat.select(s);
    alm_codiE.addDatos(dtStat);
    alm_codiE.setText("1");

    cam_codiE.setFormato(Types.CHAR, "XX", 2);
    cam_codiE.texto.setMayusc(true);
    gnu.chu.anjelica.pad.pdconfig.llenaDiscr(dtStat,cam_codiE,"AC",EU.em_cod);


    proiniE.iniciar(dtStat, this, vl, EU);
    profinE.iniciar(dtStat, this, vl, EU);

    fecStockE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
    emp_codiE.iniciar(dtStat, this, vl, EU);
    ponTipListado();
    activarEventos();
  }

  void activarEventos()
  {
    Baceptar.addActionListener(new ActionListener()
    {
            @Override
      public void actionPerformed(ActionEvent e)
      {
        Baceptar_actionPerformed();
      }
    });

    Blistar.addActionListener(new ActionListener()
    {
            @Override
      public void actionPerformed(ActionEvent e)
      {
        Blistar_actionPerformed();
      }
    });
    tla_codiE.addFocusListener(new FocusAdapter()
    {
            @Override
      public void focusGained(FocusEvent e)
      {
        tla_codiE.resetCambio();
      }

            @Override
      public void focusLost(FocusEvent e)
      {
        if (!tla_codiE.hasCambio())return;
        ponTipListado();
      }
    });
  }

  void ponTipListado()
  {
    try
    {
      s = "select * from tilialca where tla_codi = " + tla_codiE.getValorInt();
      if (!dtStat.select(s))
        return;
      tla_nuliprE.setValorInt(dtStat.getInt("tla_nulipr"));
      tla_diagfeE.setValorInt(dtStat.getInt("tla_diagfe"));
      tla_vekgcaE.setValor(dtStat.getString("tla_vekgca"));

    }
    catch (Exception k)
    {
      Error("Poner Tipo de Listado", k);
    }
  }

  void Baceptar_actionPerformed()
  {
    if (! checkCond()) return;
    new miThread("xx")
    {
      @Override
      public void run()
      {
        msgEspere("Espere ... Buscando Datos");
        consulta();
        resetMsgEspere();
      }
    };
  }

  void consulta()
  {  
    Pgrids.removeAll();
    int tlaCodi = tla_codiE.getValorInt();
    int empCodi = emp_codiE.getValorInt();
    int almCodi = alm_codiE.getValorInt();
    maxLinGrid=0;
    try
    {
      s = getSqlCab(tlaCodi,empCodi);
      if (!dtCon1.select(s))
      {
        mensajeErr("NO encontrados datos para estos criterios");
        mensaje("");
        return;
      }
      int posX = 0;
      int posY = 0;
      int prv;
      int proCodi;
      long redondeo = tla_diagfeE.getValorLong() * 1000 * 60 * 60 * 24; //  1000 Ms * 60 Segundos * 60 Minutos * 24 Horas
      long fecCad;
      String llave;
      java.util.Date dt;
      boolean verKilos = tla_vekgcaE.getValor().equals("K");
      productos.clear();
      grids.clear();
      Object o;
      ArrayList vc = new ArrayList();
      vc.add("Proveed");
      vc.add("Fecha");
      vc.add("Cantid.");
      int[] ancGrid ;
      int[]alineaGrid;
      if (opIncPedE.isSelected())
      {
        vc.add("Stock");
        vc.add("Comp");
        vc.add("Vent");
        ancGrid = new int[]  {95, 60, 45,45,45,45};
        alineaGrid = new int[] {  0, 1, 2,2,2,2};
      }
      else
      {
        ancGrid = new int[]  {95, 60, 55};
        alineaGrid = new int[] {  0, 1, 2};
      }
      s="SELECT sum(stp_unact) as unidades,SUM(stp_kilact) as cantidad," +
            " prv_codi, stp_feccad as feccad " +
            " FROM v_stkpart where pro_codi = ?" +
            " and emp_codi = " + empCodi +
            (VERNEGATIVO?
                "  and (stp_kilact > 0.49 or stp_kilact < -0.49)"+
                (verKilos?"":" and stp_unact != 0 "):
                "   and stp_kilact > 0.49 "+
                (verKilos?"":" and stp_unact >0 ")) +
            " and eje_nume > 0 " +
            (almCodi == 0 ? "" : " and alm_codi = " + almCodi)+
            " group by prv_codi,stp_feccad";
      if (opIncPedE.isSelected())
      {
        s+=" union all "+
           " select sum(acp_canind)*-1 as unidades,sum(acp_canti)*-1 as cantidad,  "+
            " c.prv_codi , acp_feccad as feccad "+
            " from v_compras as c"+
            " WHERE emp_codi = "+empCodi+
            " and pro_codi = ?"+            
            " AND C.ACC_CERRA = 0 "+ // No estan cerradas las lineas
            (almCodi == 0 ? "" : " and alm_codi = " + almCodi)+
            " and exists ( select   emp_codi "+
            " from pedicoc as p " +
            " where p.emp_codi = c.emp_codi "+
            " and p.acc_ano = c.acc_ano "+
            " and p.acc_serie = c.acc_serie "+
            " and p.acc_nume = c.acc_nume) "+
            " group by prv_codi,acp_feccad "+
            " UNION ALL " +
// Albaranes Ventas sin CERRAR Y con pedidos
            " select sum(avp_numuni) as unidades,sum(avp_canti) as cantidad, " +
            " s.prv_codi, s.stp_feccad as feccad " +
            " from v_albventa_detalle as a,v_stkpart as s " +
            " WHERE  s.eje_nume = a.avp_ejelot " +
            " and s.emp_codi = a.avp_emplot " +
            " and s.pro_serie = a.avp_serlot " +
            " and s.pro_nupar = a.avp_numpar " +
            " and s.pro_codi = a.pro_codi " +
            " and s.pro_numind = a.avp_numind " +
            " AND avc_cerra = 0 " + // Abierto y con pedido
            (almCodi == 0 ? "" : " and s.alm_codi = " + almCodi) +
            " and a.pro_codi = ? "+
            " and a.emp_codi = " + empCodi +
            " and  exists ( select emp_codi from pedvenc as p " +
            " where p.emp_codi = a.emp_codi " +
            " and p.avc_ano = a.avc_ano " +
            " and p.avc_serie = a.avc_serie " +
            " and p.avc_nume = a.avc_nume) " +
            " group by s.prv_codi, s.stp_feccad ";
      } // Fin de incluir pedidos
//      debug("s: "+s);
      pst = ct.prepareStatement(s);

      s = // Pedidos de Compras Pendientes de confirmar
          " SELECT sum(pcl_nucape) as unidCompr, sum(pcl_cantpe) as cantCompr, " +
          " 0 as unidVent, 0 as cantVent, " +
          " prv_codi,pcl_feccad as feccad " +
          " FROM v_pedico  " +
          " where EMP_CODI = " + empCodi +
          " and pro_codi = ?" +
          (almCodi == 0 ? "" : " and alm_codi = " + almCodi) +
          " and pcc_estad = 'C' " +
          " AND pcc_estrec = 'P' "+
          " and pcc_fecrec <=  TO_DATE('" + fecStockE.getText() + "','dd-MM-yyyy')" +
          " group by prv_codi,pcl_feccad " +
          " UNION ALL " +
          // Pedidos Compras Confirmados
          " SELECT sum(pcl_nucaco) as unidCompr, sum(pcl_cantco) as cantCompr, " +
          " 0 as unidVent, 0 as cantVent, " +
          " prv_codi,pcl_feccad as feccad " +
          " FROM v_pedico " +
          " where EMP_CODI = " + empCodi +
          " and pro_codi = ? " +
          (almCodi == 0 ? "" : " and alm_codi = " + almCodi) +
          " and pcc_estad = 'C' " +
          " AND pcc_estrec = 'P' "+
          " and pcc_fecrec <=  TO_DATE('" + fecStockE.getText() + "','dd-MM-yyyy')" +
          " group by prv_codi,pcl_feccad " +
          " UNION ALL " +
          // Pedidos Compras Pre-Factura
          " SELECT sum(pcl_nucafa) as unidCompr, sum(pcl_cantfa) as cantCompr, " +
          " 0 as unidVent, 0 as cantVent, " +
          " prv_codi,pcl_feccad as feccad " +
          " FROM v_pedico " +
          " where EMP_CODI = " + empCodi +
          " and pro_codi = ?" +
          (almCodi == 0 ? "" : " and alm_codi = " + almCodi) +
          " and pcc_estad = 'F' " +
          " AND pcc_estrec = 'P' "+
          " and pcc_fecrec <=  TO_DATE('" + fecStockE.getText() + "','dd-MM-yyyy')" +
          " group by prv_codi,pcl_feccad " +
          " UNION ALL " +
          // Pedidos Ventas Pendientes de preparar albaran
          "SELECT  0 as unidCompr,0 as cantCompr, " +
          " sum(pvl_unid) as  unidVent, sum(pvl_kilos) as cantVent, " +
          " prv_codi,pvl_feccad as feccad " +
          "  FROM v_pedven " +
          " where  EMP_CODI = " + empCodi +
          " and (avc_ano = 0 or pvc_cerra = 0) " + // Sin Albaran o Albaran sin CERRAR
          (almCodi == 0 ? "" : " and alm_codi = " + almCodi) +
          " and pro_codi = ?" +
          " AND pvc_fecent <= TO_DATE('" + fecStockE.getText() + "','dd-MM-yyyy')" +
          " group by prv_codi,pvl_feccad ";
      pst2 = ct.prepareCall(dtAux.parseaSql(s));
      if (tlaCodi == 99)
      {
        s = "SELECT sum(stp_unact) as unidades " +
           " FROM v_stkpart where emp_codi = " + empCodi +
           " and pro_codi = ? " +
           " and stp_kilact != 0 " +
           " and eje_nume > 0 " + // Para no incluir Acumulados
           (almCodi == 0 ? "" : " and alm_codi = " + almCodi);
        pst1 = ct.prepareStatement(s);
      }
      else
      {
          s = "SELECT * FROM tilialpr where tla_codi = " + tlaCodi +
              " and tla_orden = ?";
          pst1 = ct.prepareStatement(s);
      }

      do
      {
        mensaje("Tratando producto: " + dtCon1.getString("pro_desc"), false);
        if (tlaCodi == 99)
        {
          pst1.setInt(1,dtCon1.getInt("pro_codi"));

          dtAux.setResultSet(pst1.executeQuery());
          if (! dtAux.next())
            continue;
          if (dtAux.getDouble("unidades", true) <= 0)
            continue;
        }
        else
        {
          pst1.setInt(1, dtCon1.getInt("tla_orden"));
          dtAux.setResultSet(pst1.executeQuery());
          if (!dtAux.next())
            continue;
        }

//        if (! buscaGrupo(tlaCodi,empCodi,dtCon1.getInt("pro_codi"),almCodi))
//          continue;
        Cgrid jt = new Cgrid(opIncPedE.isSelected() ? 6 : 3);
//        Cgrid jt = new Cgrid(3);
        jt.setCabecera(vc);
        jt.setAnchoColumna(ancGrid );
        jt.setAlinearColumna(alineaGrid);
        jt.setFormatoColumna(2, "--,---9");
        if (opIncPedE.isSelected())
        {
          jt.setFormatoColumna(3, "--,---9");
          jt.setFormatoColumna(4, "--,---9");
          jt.setFormatoColumna(5, "--,---9");
        }
        CLabel pro_descL = new CLabel();
        pro_descL.setText((tlaCodi == 99?dtCon1.getInt("pro_codi")+" -> ":"")+ dtCon1.getString("pro_desc"));
        pro_descL.setBackground(Color.orange);
        pro_descL.setMaximumSize(new Dimension(176, 17));
        pro_descL.setMinimumSize(new Dimension(176, 17));
        pro_descL.setPreferredSize(new Dimension(176, 17));
        pro_descL.setOpaque(true);
        pro_descL.setHorizontalAlignment(SwingConstants.CENTER);
        jt.setMaximumSize(new Dimension(236, 128));
        jt.setMinimumSize(new Dimension(236, 128));
        jt.setPreferredSize(new Dimension(236, 128));
        jt.setBuscarVisible(false);
        HashMap hm = new HashMap();

        do
        { // Para cada producto busca el stock
          proCodi=tlaCodi == 99 ? dtCon1.getInt("pro_codi") : dtAux.getInt("pro_codi");
          pst.setInt(1,  proCodi);
          if (opIncPedE.isSelected())
          {
            pst.setInt(2, proCodi);
            pst.setInt(3, proCodi);
          }
          dtAux1.setResultSet(pst.executeQuery());
          if (dtAux1.next())
          { // Busco stock agrupandolo por prv. y fecha cad.
            do
            {
              if (dtAux1.getDate("feccad") == null)
                fecCad = 0;
              else
              {
                if (redondeo > 0)
                  dt = new java.util.Date( ( (long) (dtAux1.getDate("feccad").getTime() / redondeo)) *
                                          redondeo);
                else
                  dt = dtAux1.getDate("feccad");
                fecCad = dt.getTime();
              }
//               debug("prv: "+dtAux1.getString("prv_codi")+" fecCad: "+
//                     Formatear.formatearFecha(new java.util.Date(fecCad),"dd-MM-yyyy")+" dt: "+dtAux1.getFecha("feccad","dd-MM-yyyy"));

              llave = fecCad + "/" + dtAux1.getInt("prv_codi", true);
              if ( (o = hm.get(llave)) == null)
                hm.put(llave, verKilos ? dtAux1.getString("cantidad") : dtAux1.getString("unidades"));
              else
              {
                cutValString(o.toString());
                cantS += verKilos ? dtAux1.getDouble("cantidad") : dtAux1.getDouble("unidades");
                hm.put(llave, cantS + ":" + cantC + ":" + cantV);
              }
            }  while (dtAux1.next());
            if (opIncPedE.isSelected())
            {
              pst2.setInt(1,proCodi);
              pst2.setInt(2,proCodi);
              pst2.setInt(3,proCodi);
              pst2.setInt(4,proCodi);
              dtAux1.setResultSet(pst2.executeQuery());
              if (!dtAux1.next())
                continue;
              do
              {
                if (dtAux1.getDate("feccad") == null)
                  fecCad = 0;
                else
                {
                  if (redondeo > 0)
                    dt = new java.util.Date( ( (long) (dtAux1.getDate("feccad").getTime() /
                        redondeo)) *   redondeo);
                  else
                    dt = dtAux1.getDate("feccad");
                  fecCad = dt.getTime();
                }
                llave = fecCad + "/" + dtAux1.getInt("prv_codi", true);
                cantV=0;
                cantC=0;
                cantS=0;
                if ( (o = hm.get(llave)) != null)
                  cutValString(o.toString());
                cantC+=verKilos ? dtAux1.getDouble("cantCompr") : dtAux1.getDouble("unidCompr");
                cantV+=dtAux1.getDouble("unidVent");
                hm.put(llave,cantS+":"+cantC+":"+cantV);
              } while (dtAux1.next());
            }
          }
        } while (dtAux.next());

        Iterator it = hm.keySet().iterator();
        SortedSet set = new TreeSet();

        // Cargo el SortedSet
        while (it.hasNext())
        {
          s = it.next().toString();
          set.add(s);
        }

        cantT=0;cantTS=0;
        cantTV=0;cantTC = 0;
        it = set.iterator();
        while (it.hasNext())
        {
          s = it.next().toString();
          ArrayList v = new ArrayList();
          n = s.indexOf("/");
          prv = Integer.parseInt(s.substring(n + 1));
          llave = pdprove.getNombPrv(Integer.parseInt(s.substring(n + 1)), dtStat);
          if (llave == null)
            v.add("PRV." + prv + " ERROR");
          else
            v.add(llave); // Proveedor
          if (Long.parseLong(s.substring(0, n)) == 0)
            v.add("");
          else
            v.add(Formatear.getFecha(new java.util.Date(
                Long.parseLong(s.substring(0, n))), "dd-MM-yy"));
          cutValString(hm.get(s).toString());
          if (opIncPedE.isSelected())
          {
            v.add(""+(cantS+cantC-cantV));
            cantTS+=cantS;
            cantTC+=+cantC;
            cantTV+=cantV;
            v.add(""+cantS);
            v.add(""+cantC);
            v.add(""+cantV);
          }
          else
          {
            v.add(""+cantS);
          }
          cantT += cantS+cantC-cantV;
          jt.addLinea(v);
        }
        if (maxLinGrid<jt.getRowCount())
          maxLinGrid=jt.getRowCount();
        ArrayList v = new ArrayList();
        v.add("TOTAL"); // Proveedor
        v.add(""); // Fec. Caducidad
        v.add("" + cantT); // Kilos o Unidades Totales
        if (opIncPedE.isSelected())
        {
          v.add("" + cantTS);
          v.add("" + cantTC);
          v.add("" + cantTV);
        }
        jt.addLinea(v);
        productos.add(pro_descL.getText());
        grids.add(jt);
        Pgrids.add(pro_descL, new GridBagConstraints(posX, posY, 1, 1, 1.0, 1.0
            , GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        Pgrids.add(jt, new GridBagConstraints(posX, posY + 1, 1, 1, 1.0, 1.0
                                              , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                              new Insets(0, 0, 0, 0), 0, 0));

        posX++;
        if (posX > 2)
        {
          posX = 0;
          posY += 2;
        }
      }   while (dtCon1.next());
      this.repaint();
//       this.pack();
      mensaje("");
      mensajeErr("Consulta ... REALIZADA");
    }
    catch (Exception k)
    {
      Error("Error al Buscar datos", k);
    }
   

  }
  /**
   * Trozea un string con 3 partes en formato
   * CANTIDAD_STOCK:CANT_PED_COMPRAS:CANT_PED_VENTAS
   * dejandolo en las vaiables globales cantS,cantP,cantV respectivamente.
   * El string NO puede ser null.
   *
   * @param s String String a trocear
   */
  private void cutValString(String s)
  {
    n = s.indexOf(":");
    if (n < 0)
    {
      cantS = Double.parseDouble(s);
      cantC = 0;
      cantV = 0;
    }
    else
    {
      cantS = Double.parseDouble(s.substring(0, n));
      n1 = s.indexOf(":", n + 1);
      cantC = Double.parseDouble(s.substring(n + 1, n1));
      cantV = Double.parseDouble(s.substring(n1 + 1));
    }
  }

  void Blistar_actionPerformed()
  {
    if (! checkCond()) return;

    new miThread("jjj")
    {
            @Override
      public void run()
      {
        listar();
      }
    };
  }
  boolean checkCond()
  {
//    if (opIncPedE.isSelected() && tla_vekgcaE.getValor().equals("K"))
//    {
//        mensajeErr("No se puede incluir Pedidos si se desea sacar kilos");
//        tla_vekgcaE.requestFocus();
//        return false;
//    }
    if (fecStockE.isNull())
    {
        mensajeErr("Introduzca Fecha de Stock");
        fecStockE.requestFocus();
        return false;
    }
    if (!emp_codiE.controla())
    {
        mensajeErr(emp_codiE.getMsgError());
        return false;
    }
    return true;
  }
  void listar()
  {
    mensaje("Espere .... generando Listado");
    this.setEnabled(false);
    try
    {
      if (tla_nuliprE.getValorInt()>0)
        tlaNulipr=tla_nuliprE.getValorInt();
      else
        tlaNulipr=maxLinGrid;
      JasperReport jr;
      numProd=-1;
      linGrid=999;
      if (opIncPedE.isSelected())
        jr = gnu.chu.print.util.getJasperReport(EU,  "resstock_d");
      else
        jr = gnu.chu.print.util.getJasperReport(EU, "resstock");
      java.util.HashMap mp = new java.util.HashMap();
      mp.put("tla_nombP",tla_codiE.getTextCombo());
      mp.put("tla_diagfeP",tla_diagfeE.getText());
      mp.put("tla_vekgcaP",tla_vekgcaE.getText());
      mp.put("alm_nombP",alm_codiE.getTextCombo());

      JasperPrint jp = JasperFillManager.fillReport(jr, mp, this);
     
      if (!gnu.chu.print.util.printJasper(jp, EU)) {
            mensaje("");
            this.setEnabled(true);
            return;
      }
    
      mensaje("");
      mensajeErr("Listado ... GENERADO");
    }
    catch (Exception k)
    {
      Error("Error al imprimir", k);
    }
    this.setEnabled(true);
  }

  public boolean next() throws JRException
  {

    if (linGrid+1 >= tlaNulipr)
    {
      numProd++;
      if (numProd == productos.size())
        return false;
      linGrid = -1;
    }
    linGrid++;
    return true;
  }

    @Override
  public Object getFieldValue(JRField jRField) throws JRException
  {
    String campo = jRField.getName();
    try
    {
      if (campo.equals("pro_nomcor"))
        return productos.get(numProd).toString();
      Cgrid grid=( (Cgrid) grids.get(numProd));
      if (linGrid+1 >= grid.getRowCount())
        return null;
      int nCol=-1;
      if (campo.equals("stp_kilact"))
        nCol=2;
      if (campo.equals("stp_kilstk"))
        nCol=3;
      if (campo.equals("stp_kilcom"))
        nCol=4;
      if (campo.equals("stp_kilven"))
        nCol=5;

      if (linGrid+1<tlaNulipr || linGrid +2 ==grid.getRowCount() )
      {
        if (campo.equals("prv_nomb"))
          return grid.getValString(linGrid, 0);

        if (campo.equals("stp_feccad"))
          return Formatear.getDate(grid.getValString(linGrid, 1), "dd-MM-yy");
        if ( nCol>0)
          return new Double(grid.getValorDec(linGrid, nCol));
      }
      else
      {
        if (campo.equals("prv_nomb"))
            return "***";
        if (campo.equals("stp_feccad"))
          return null;
         if (nCol>0)
         {
           double kilos=0;
           for (int n=linGrid;n+1<grid.getRowCount();n++)
             kilos+=grid.getValorDec(n, nCol);
           return new Double(kilos);
         }
      }


      throw new JRException("Campo: "+campo+ " No definido");
    }
    catch (Exception k)
    {
//      debug("Campo: "+campo);
      k.printStackTrace();
      throw new JRException(k);
    }
  }



  String getSqlCab(int tlaCodi, int empCodi)
  {
    if (tlaCodi == 99)
    {
      String proDisc3 = Formatear.reemplazar(cam_codiE.getText(), "*", "%").trim();
      if (proDisc3.equals("%%") || proDisc3.equals("%") || proDisc3.equals(""))
        proDisc3 = null;
    String condArt = (proDisc3 == null ? "" : " and cam_codi like '%" + proDisc3 + "%'") +
        (!proiniE.isNull() ? "  and pro_codi >= " + proiniE.getValorInt() : "") +
          (!profinE.isNull() ? "  and pro_codi <= " + profinE.getValorInt() :
           "")+
         (opSoloVend.isSelected()?" and pro_tiplot = 'V'":"");

      s = "SELECT pro_codi,pro_nomcor as pro_desc from v_articulo where 1 = 1" +
          condArt +
          " order by pro_codi";
    }
    else
      s = "SELECT * FROM tilialgr WHERE tla_codi = " + tlaCodi +
          " order by tla_orden";

    return s;
  }

  boolean buscaGrupo(int tlaCodi,int empCodi,int proCodi,int almCodi) throws SQLException
  {
    if (tlaCodi == 99)
    {
      s = "SELECT sum(stp_unact) as unidades " +
          " FROM v_stkpart where emp_codi = " + empCodi +
          " and pro_codi = " + proCodi +
          " and stp_kilact != 0 " +
          " and eje_nume > 0 " +
          (almCodi == 0 ? "" : " and alm_codi = " + almCodi);
      dtAux.select(s);
      if (dtAux.getDouble("unidades", true) <= 0)
        return false;
    }
    else
    {
      s = "SELECT * FROM tilialpr where tla_codi = " + tlaCodi +
          " and tla_orden = " + dtCon1.getInt("tla_orden");

      if (!dtAux.select(s))
        return false;
    }
    return true;
  }
}


