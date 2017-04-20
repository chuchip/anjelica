package gnu.chu.anjelica.almacen;

import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.pad.pdprove;
import gnu.chu.camposdb.proPanel;
import gnu.chu.camposdb.sbePanel;
import gnu.chu.controles.*;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.*;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import net.sf.jasperreports.engine.*;

/**
 *
 * <p>Título: clresstock </p>
 * <p>Descripción: Consulta/Listado Resumen de stock desglosandolo
 * por proveedor y fecha de caducidad</p>
 * <p>Copyright: Copyright (c) 2005-2015
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
 * @version 1.1
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
  double cantC,cantV;
  double cantTS,cantTC,cantTV;
  double kilosT;
  int unidT;
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
  CComboBox opVerFecC=new CComboBox();
  CLabel opVerFecL=new CLabel("Fecha");
  CLabel cLabel7 = new CLabel();
  CTextField tla_diagfeE = new CTextField(Types.DECIMAL, "#9");
  CLabel cLabel9 = new CLabel();
  CTextField fecStockE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CCheckBox opIncPedE = new CCheckBox();
  CCheckBox opIncPrvE= new CCheckBox();
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
  sbePanel sbe_codiE = new sbePanel();
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
      ErrorInit(e);
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
      ErrorInit(e);
    }
  }

  private void jbInit() throws Exception
  {
    iniciarFrame();
    this.setSize(new Dimension(760, 540));
    this.setVersion("2016-12-20");
    VERNEGATIVO=EU.getValorParam("verNegResStock",VERNEGATIVO);
    statusBar = new StatusBar(this);
    conecta();
    cLabel10.setText("Seccion");
    cLabel10.setBounds(new Rectangle(365, 23, 49, 17));
    sbe_codiE.setBounds(new Rectangle(414, 23, 40, 17));
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
    tla_vekgcaE.setBounds(new Rectangle(264, 3, 70, 17));
    cLabel6.setText("Lin.Prod");
    cLabel6.setBounds(new Rectangle(335, 3, 50, 17));
    tla_nuliprE.setBounds(new Rectangle(387, 2, 20, 17));
    opVerFecL.setBounds(new Rectangle(410, 2, 40, 17));
    opVerFecC.addItem("Caduc.","C");
    opVerFecC.addItem("Sacr.","S");
    opVerFecC.setBounds(new Rectangle(452, 2, 65, 17));
    cLabel3.setText("Alm.");
    cLabel3.setBounds(new Rectangle(524, 3, 40, 17));
    alm_codiE.setAncTexto(30);
    alm_codiE.setBounds(new Rectangle(565, 3, 190, 17));

    cLabel7.setText("Dias");
    cLabel7.setBounds(new Rectangle(1, 23, 30, 17));
    tla_diagfeE.setBounds(new Rectangle(31, 23, 26, 17));
    cLabel9.setText("Stock");
    cLabel9.setBounds(new Rectangle(58, 23, 40, 17));
    
    fecStockE.setFormato("dd-MM-yy");
    fecStockE.setBounds(new Rectangle(100, 23, 60, 17));
    opIncPrvE.setText("Inc.Prv");
    opIncPrvE.setToolTipText("Incluir Prv. en consulta");
    opIncPrvE.setBounds(new Rectangle(162, 23, 60, 17));
    opIncPedE.setText("Inc.Pedidos");
    opIncPedE.setBounds(new Rectangle(275, 23, 90, 17));

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
    PtipoCons.add(opIncPrvE, null);
    PtipoCons.add(cLabel10, null);
    PtipoCons.add(sbe_codiE, null);
    PtipoCons.add(tla_diagfeE, null);
    PtipoCons.add(cLabel9, null);
    PtipoCons.add(fecStockE, null);
    PtipoCons.add(cLabel5, null);
    PtipoCons.add(tla_codiE, null);
    PtipoCons.add(alm_codiE, null);
    PtipoCons.add(cLabel3, null);
    PtipoCons.add(tla_nuliprE, null);
    PtipoCons.add(opVerFecL, null);
    PtipoCons.add(opVerFecC, null);
    
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
   
    s = "SELECT tla_codi,tla_nomb FROM tilialca order by tla_codi";
    dtStat.select(s);      
    tla_codiE.addDatos(dtStat);
    tla_codiE.addDatos("99", "Definido Usuario");
    tla_codiE.setValorInt(99);
    
    tla_vekgcaE.addItem("Ambos", "A");
    tla_vekgcaE.addItem("Kilos", "K");
    tla_vekgcaE.addItem("Unid", "U");

    alm_codiE.setFormato(true);
    alm_codiE.setFormato(Types.DECIMAL, "#9", 2);
    pdalmace.llenaLinkBox(alm_codiE, dtStat,'*');
//    s = "SELECT alm_codi,alm_nomb FROM v_almacen " +
//        " ORDER BY alm_codi";
//    dtStat.select(s);
//    alm_codiE.addDatos(dtStat);
    alm_codiE.setText("1");

    cam_codiE.setFormato(Types.CHAR, "XX", 2);
    cam_codiE.texto.setMayusc(true);
    gnu.chu.anjelica.pad.pdconfig.llenaDiscr(dtStat,cam_codiE,"AC",EU.em_cod);


    proiniE.iniciar(dtStat, this, vl, EU);
    profinE.iniciar(dtStat, this, vl, EU);

    fecStockE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
    sbe_codiE.iniciar(dtStat, this, vl, EU);
    sbe_codiE.setTipo("A");
    sbe_codiE.setValorInt(0);
    sbe_codiE.setAceptaNulo(true);
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
//    int sbeCodi = sbe_codiE.getValorInt();
    int almCodi = alm_codiE.getValorInt();
    maxLinGrid=0;
    try
    {
      s = getSqlCab(tlaCodi,sbe_codiE.getValorInt());
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
      char opVerDatos=tla_vekgcaE.getValor().charAt(0);
      java.util.Date dt;
      boolean opIncPrv=opIncPrvE.isSelected();
      productos.clear();
      grids.clear();
      DatProducto result;
      ArrayList vc = new ArrayList();
      vc.add("Proveed");
      vc.add("Fecha");
      ArrayList<Integer> ancGrid = new ArrayList();
      ancGrid.add(95);
      ancGrid.add(60);
      ArrayList<Integer> alineaGrid = new ArrayList();
      alineaGrid.add(0);
      alineaGrid.add(1);
      
      if (opVerDatos=='U' || opVerDatos=='A')
      {
        ancGrid.add(35);
        alineaGrid.add(2);
        vc.add("Unid");
        
      }
      if (opVerDatos=='K' || opVerDatos=='A')
      {
        ancGrid.add(45);
        alineaGrid.add(2);
        vc.add("Kilos");
      }
      
      if (opIncPedE.isSelected())
      {
        vc.add("Stock");
        vc.add("Comp");
        vc.add("Vent");
        ancGrid.add(45);
        ancGrid.add(45);
        ancGrid.add(45);        
        alineaGrid.add(2);
        alineaGrid.add(2);
        alineaGrid.add(2);        
      }
    
      s="SELECT sum(stp_unact) as unidades,SUM(stp_kilact) as cantidad," +
            " prv_codi, stp_feccad as feccad " +
            " FROM v_stkpart where pro_codi = ?" +
            (VERNEGATIVO?
                "  and (stp_kilact > 0.49 or stp_kilact < -0.49)"+
                (opVerDatos=='K'?"":" and stp_unact != 0 "):
                "   and stp_kilact > 0.49 "+
                (opVerDatos=='K'?"":" and stp_unact >0 ")) +
            " and eje_nume > 0 " +
            (almCodi == 0 ? "" : " and alm_codi = " + almCodi)+
            " group by prv_codi,stp_feccad";
      if (opIncPedE.isSelected())
      {
        s+=" union all "+
           " select sum(acp_canind)*-1 as unidades,sum(acp_canti)*-1 as cantidad,  "+
            " c.prv_codi , acp_feccad as feccad "+
            " from v_compras as c"+          
            " where pro_codi = ?"+            
            " AND c.acc_cerra = 0 "+ // No estan cerradas las lineas
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
          " where pro_codi = ?" +
          (almCodi == 0 ? "" : " and alm_codi = " + almCodi) +
          " and pcc_estad = 'P' " +
          " AND pcc_estrec = 'P' "+
          " and pcc_fecrec <=  TO_DATE('" + fecStockE.getText() + "','dd-MM-yyyy')" +
          " group by prv_codi,pcl_feccad " +
          " UNION ALL " +
          // Pedidos Compras Confirmados
          " SELECT sum(pcl_nucaco) as unidCompr, sum(pcl_cantco) as cantCompr, " +
          " 0 as unidVent, 0 as cantVent, " +
          " prv_codi,pcl_feccad as feccad " +
          " FROM v_pedico " +       
          " where pro_codi = ? " +
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
          " where pro_codi = ?" +
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
          " where (avc_ano = 0 or pvc_cerra = 0) " + // Sin Albaran o Albaran sin CERRAR
          " and pvc_confir = 'S' "+
          (almCodi == 0 ? "" : " and alm_codi = " + almCodi) +
          " and pro_codi = ?" +
          " AND pvc_fecent <= TO_DATE('" + fecStockE.getText() + "','dd-MM-yyyy')" +
          " group by prv_codi,pvl_feccad ";
      pst2 = ct.prepareCall(dtAux.parseaSql(s));
      if (tlaCodi == 99)
      {
        s = "SELECT sum(stp_unact) as unidades " +
           " FROM v_stkpart where  pro_codi = ? " +
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
        Cgrid jt = new Cgrid( ancGrid.size());
//        Cgrid jt = new Cgrid(3);
        jt.setCabecera(vc);
        jt.setAnchoColumna(ancGrid );
        jt.setAlinearColumna(alineaGrid);
        String formCanti="--,--9";
        int nCol=2;
        if (opVerDatos=='U' || opVerDatos=='A')
            jt.setFormatoColumna(nCol++, formCanti);
        if (opVerDatos=='K' || opVerDatos=='A')
        {
              formCanti="-,--9.9";
              jt.setFormatoColumna(nCol, formCanti);
        }
        
        
        if (opIncPedE.isSelected())
        {
          jt.setFormatoColumna(nCol++, formCanti);
          jt.setFormatoColumna(nCol++,formCanti);
          jt.setFormatoColumna(nCol++, formCanti);
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
        double cantS;
        ArrayList<DatProducto> hm = new ArrayList();

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
              DatProducto dtProd=new DatProducto(proCodi, 
                  opIncPrv?dtAux1.getInt("prv_codi"):0, fecCad);
             
            
              int pos;
              if (  (pos =hm.indexOf(dtProd)) == -1)
              {
                dtProd.setKilos(dtAux1.getDouble("cantidad"));
                dtProd.setUnidades(dtAux1.getInt("unidades"));
                hm.add(dtProd);
              }
              else
              {   
                result=hm.get(pos);
                result.addKilos( dtAux1.getDouble("cantidad"));
                result.addUnidades( dtAux1.getInt("unidades"));             
                hm.add(result);
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
                DatProducto dtProd=new DatProducto(proCodi,opIncPrv?dtAux1.getInt("prv_codi"):0, fecCad);
              
                int pos;
                if (  (pos =hm.indexOf(dtProd)) == -1)
                {
                    
//                cantC+=verKilos ? dtAux1.getDouble("cantCompr") : dtAux1.getDouble("unidCompr");
//                cantV+= verKilos? dtAux1.getDouble("cantVent") : dtAux1.getDouble("unidVent");
                hm.add(dtProd);
                }
              } while (dtAux1.next());
            }
          }
        } while (dtAux.next());

        Iterator<DatProducto> it = hm.iterator();
        SortedSet<DatProducto> set = new TreeSet();

        // Cargo el SortedSet
        while (it.hasNext())
        {
          result = it.next();
          set.add(result);
        }

        unidT=0;kilosT=0;cantTS=0;
        cantTV=0;cantTC = 0;
        it = set.iterator();
        while (it.hasNext())
        {
          result = it.next();
          ArrayList v = new ArrayList();
          if (opIncPrv)
          {
            s=pdprove.getNombPrv(result.getProveedor(), dtStat);
            if (s == null)
              v.add("PRV." + result.getProveedor() + " ERROR");
            else
              v.add(s); // Proveedor          
          }
          else
              v.add("");
           v.add(Formatear.getFecha(new java.util.Date(
                result.getFecha()), "dd-MM-yy"));
        
            
          if (opVerDatos=='U' || opVerDatos=='A')
          {
            cantS=result.getUnidades()+result.getUnidadesCompra()-result.getUnidadesVenta();
            v.add(cantS);
            unidT+=cantS;
          }
          if (opVerDatos=='K' || opVerDatos=='A')
          {
            cantS=result.getKilos()+result.getKilos()-result.getKilosVenta();
            v.add(cantS);
            kilosT+=cantS;
          }
          if (opIncPedE.isSelected())
          {            
            if (opVerDatos=='U')
            {
               v.add(result.getUnidades());
               v.add(result.getUnidadesCompra());
               v.add(result.getUnidadesVenta());
               cantTS+=result.getUnidades();
               cantC+=result.getUnidadesCompra();
               cantTV+=result.getUnidadesVenta();
            }     
             if (opVerDatos=='K' )
            {
               v.add(result.getKilos());
               v.add(result.getKilosCompra());
               v.add(result.getKilosVenta());
               cantTS+=result.getKilos();
               cantC+=result.getKilosCompra();
               cantTV+=result.getKilosVenta();

            }    
          }        
          jt.addLinea(v);
        }
        if (maxLinGrid<jt.getRowCount())
          maxLinGrid=jt.getRowCount();
        ArrayList v = new ArrayList();
        v.add("TOTAL"); // Proveedor
        v.add(""); // Fec. Caducidad     
        if (opVerDatos=='U' || opVerDatos=='A')
            v.add(unidT); // Unidades Totales
        if (opVerDatos=='K' || opVerDatos=='A')
            v.add(kilosT); // Kilos  Totales
        if (opIncPedE.isSelected())
        {
          v.add( cantTS);
          v.add( cantTC);
          v.add( cantTV);
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
    catch (SQLException | NumberFormatException k)
    {
      Error("Error al Buscar datos", k);
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
      try
      {
          if (opIncPedE.isSelected() && tla_vekgcaE.getValor().equals("A"))
          {
              mensajeErr("No se puede incluir Pedidos si se desea sacar Unid. y Kilos");
              tla_vekgcaE.requestFocus();
              return false;
          }
          if (fecStockE.isNull())
          {
              mensajeErr("Introduzca Fecha de Stock");
              fecStockE.requestFocus();
              return false;
          }
          if (!sbe_codiE.controla())
          {
              mensajeErr("Seccion no valida");
              return false;
          }          
      } catch (SQLException ex)
      {
          Error("Error al comprobar seccion de articulo",ex);
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
      String report=opIncPedE.isSelected()?"resstock_d":"resstock";
      JasperReport jr=gnu.chu.print.util.getJasperReport(EU,  report);
      numProd=-1;
      linGrid=999;
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
    catch (PrinterException | JRException k)
    {
      Error("Error al imprimir", k);
    }
    this.setEnabled(true);
  }

  @Override
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
          return grid.getValorDec(linGrid, nCol);
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
           return kilos;
         }
      }


      throw new JRException("Campo: "+campo+ " No definido");
    }
    catch (ParseException | JRException k)
    {
      throw new JRException(k);
    }
  }



  String getSqlCab(int tlaCodi, int sbeCodi)
  {
    if (tlaCodi == 99)
    {
        String proDisc3 = Formatear.reemplazar(cam_codiE.getText(), "*", "%").trim();
        if (proDisc3.equals("%%") || proDisc3.equals("%") || proDisc3.trim().equals(""))
          proDisc3 = null;
        String condArt = " 1 = 1 "+
          (proDisc3 == null ? "" : " and cam_codi like '%" + proDisc3 + "%'") +
          (!proiniE.isNull() ? "  and pro_codi "+(profinE.isNull()?"":">") +
            "= " + proiniE.getValorInt() : "") +
            (!profinE.isNull() ? "  and pro_codi <= " + profinE.getValorInt() :
             "")+
           (opSoloVend.isSelected()?" and pro_tiplot = 'V'":"");
        if (sbeCodi!=0)
          condArt+=" and sbe_codi ="+sbeCodi;
        s = "SELECT pro_codi,pro_nomcor as pro_desc from v_articulo where " +
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
class DatProducto implements Comparable<DatProducto>
{
    int proCodi, prvCodi;
    long fecha;
    double kilos=0,kilosCompra=0,kilosVenta=0;
    int unidad=0,unidCompra=0,unidVenta=0;
    public DatProducto(int proCodi,int prvCodi,long fecha)
    {
        this.proCodi=proCodi;
        this.prvCodi=prvCodi;
        this.fecha=fecha;
    }
    
    public void setKilos(double kilos)
    {
        this.kilos=kilos;
    }
    public void setUnidades(int unidad)
    {
        this.unidad=unidad;
    }
    public double getKilos()
    {
        return this.kilos;
    }
    public int getUnidades()
    {
        return this.unidad;
    }
    public void addKilos(double kilos)
    {
        this.kilos+=kilos;
    }
     public void addUnidades(int unid)
    {
        this.unidad+=unid;
    }
    public int getProducto()
    {
        return proCodi;
    }
    public int getProveedor()
    {
        return prvCodi;
    }
    public long getFecha()
    {
        return fecha;
    }
    public void setKilosCompra(double kilCompra)
    {
        kilosCompra=kilCompra;
    }
    public void setKilosVenta(double kilVenta)
    {
        kilosVenta=kilVenta;
    }
    public void setUnidadesCompra(int unidad)
    {
        this.unidCompra=unidad;
    }
    public void setUnidadesVenta(int unidad)
    {
        this.unidVenta=unidad;
    }
    public double getKilosCompra()
    {
        return kilosCompra;
    }
    public double getKilosVenta()
    {
        return kilosVenta;
    }
    public int getUnidadesCompra()
    {
        return this.unidCompra;
    }
    public int getUnidadesVenta()
    {
        return this.unidVenta;
    }
    @Override
    public boolean equals(Object obj) {
      if ( obj instanceof  DatIndivBase )
      {
          if (((DatProducto)obj).getProducto()==getProducto() &&
              ((DatProducto)obj).getProveedor()==getProveedor() &&
              ((DatProducto)obj).getFecha()==getFecha()      )
              return true;
      }
      return false;
      
  }
     @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + getProducto();
        hash = 89 * hash + getProveedor();
        hash = 89 * hash + (int) this.getFecha();      
        return hash;
    }

   
    @Override
  public String toString()
  {
          return getProducto()+" "+getProveedor() +"-"+
              this.getFecha(); 
  }

    @Override
    public int compareTo(DatProducto o) {
        if (o.getFecha()>getFecha() || o.getProveedor()>getProveedor())
            return 1;
         if (o.getFecha()<getFecha() || o.getProveedor()<getProveedor())
            return -1;
        return 0;

    }
}

