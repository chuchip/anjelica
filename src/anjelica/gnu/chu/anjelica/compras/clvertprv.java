package gnu.chu.anjelica.compras;

import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import java.sql.*;
import gnu.chu.Menu.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.*;
import gnu.chu.anjelica.almacen.paregalm;
import gnu.chu.camposdb.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.*;

/**
 * Consulta/Listado de  Reclamaciones  a Proveedores.
 *
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
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
 * </p>
 * @author chuchiP
 * @version 1.0
*/
public class clvertprv  extends ventana
{
  boolean error;
  int rgsRecprv=0;
  String s;
  CButton Blistar = new CButton(Iconos.getImageIcon("print"));
  CPanel Pprinc = new CPanel();
  CPanel Pcond = new CPanel();
  Cgrid jtCab = new Cgrid(7);
  CTextField fercliniE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel1 = new CLabel();
  CLabel cLabel2 = new CLabel();
  CTextField ferclfinE = new CTextField(Types.DATE, "dd-MM-yyyy");
  prvPanel prv_codfinE = new prvPanel();
  prvPanel prv_codiniE = new prvPanel();
  CLabel cLabel17 = new CLabel();
  CLabel cLabel4 = new CLabel();
  CLabel cLabel3 = new CLabel();
  CComboBox tir_codiE = new CComboBox();
  CButton Bconsultar = new CButton( "Buscar (F4)",Iconos.getImageIcon("buscar"));
  CTextField fealiniE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel5 = new CLabel();
  CLabel cLabel6 = new CLabel();
  CTextField fealfinE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CCheckBox opIncFraE = new CCheckBox();
  Cgrid jtLin = new Cgrid(10);
  CLabel cLabel23 = new CLabel();
  CPanel PVertInf = new CPanel();
  CTextField vertNPiezE = new CTextField("###9");
  CTextField vertImporE = new CTextField(Types.DECIMAL,"---,--9.99");
  CLabel cLabel22 = new CLabel();
  CTextField vertKilosE = new CTextField(Types.DECIMAL,"---,--9.99");
  CLabel cLabel26 = new CLabel();
  CTextField vertNLinE = new CTextField("###9");
  CLabel cLabel24 = new CLabel();
  CCheckBox opGrupos = new CCheckBox();
  CLabel cLabel7 = new CLabel();
  CTextField vertImporTE = new CTextField(Types.DECIMAL,"---,--9.99");
  CTextField vertNLinTE = new CTextField("###9");
  CLabel cLabel25 = new CLabel();
  CLabel cLabel27 = new CLabel();
  CLabel cLabel8 = new CLabel();
  CPanel PacuTot = new CPanel();
  CTextField vertKilosTE = new CTextField(Types.DECIMAL,"---,--9.99");
  CLabel cLabel28 = new CLabel();
  CLabel cLabel29 = new CLabel();
  CTextField vertNPiezTE = new CTextField("###9");
  JTabbedPane Presul = new JTabbedPane();
  CPanel Pdetalle = new CPanel();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  CPanel Pprove = new CPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  Cgrid jtPrv = new Cgrid(9);
  Cgrid jtAcuPrv = new Cgrid(4);
  GridBagLayout gridBagLayout3 = new GridBagLayout();

  public clvertprv(EntornoUsuario eu, Principal p)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    setTitulo("Cons/List Reclamac. a Proveedores");

    try
    {
      if (jf.gestor.apuntar(this))
        jbInit();
      else
        setErrorInit(true);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      setErrorInit(true);
    }
  }

  public clvertprv(gnu.chu.anjelica.menu p, EntornoUsuario eu)
  {

    EU = eu;
    vl = p.getLayeredPane();

    setTitulo("Cons/List Reclamac. a Proveedores");


    try
    {
      jbInit();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      setErrorInit(true);
    }
  }

  private void jbInit() throws Exception
  {
    jtPrv.setMaximumSize(new Dimension(663, 280));
    jtPrv.setMinimumSize(new Dimension(663, 280));
    jtPrv.setPreferredSize(new Dimension(663, 280));
    iniciarFrame();
    this.setSize(new Dimension(689, 575));
    this.setVersion("2007-04-29");

    statusBar = new StatusBar(this);
    conecta();
    jtLin.setMaximumSize(new Dimension(644, 136));
    jtLin.setMinimumSize(new Dimension(644, 136));
    jtLin.setPreferredSize(new Dimension(644, 136));

    Pcond.setMaximumSize(new Dimension(533, 90));
    Pcond.setMinimumSize(new Dimension(533, 90));
    Pcond.setOpaque(true);
    Pcond.setPreferredSize(new Dimension(533, 90));
    Blistar.setPreferredSize(new Dimension(24, 24));
    Blistar.setMaximumSize(new Dimension(24, 24));
    Blistar.setMinimumSize(new Dimension(24, 24));
    Blistar.setToolTipText("Listar Reclamaciones a Proveedores");

    Bconsultar.setBounds(new Rectangle(409, 62, 120, 25));
    Bconsultar.setMargin(new Insets(0, 0, 0, 0));
    opIncFraE.setBounds(new Rectangle(263, 62, 144, 20));
    tir_codiE.setBounds(new Rectangle(43, 65, 108, 20));
    cLabel3.setBounds(new Rectangle(2, 64, 45, 21));
    prv_codfinE.setBounds(new Rectangle(80, 42, 441, 18));
    cLabel17.setBounds(new Rectangle(2, 42, 73, 18));
    prv_codiniE.setBounds(new Rectangle(81, 22, 442, 18));
    cLabel4.setBounds(new Rectangle(2, 22, 80, 18));
    cLabel1.setBounds(new Rectangle(257, 3, 98, 18));
    cLabel5.setBounds(new Rectangle(2, 3, 65, 18));
    ferclfinE.setBounds(new Rectangle(447, 3, 77, 17));
    cLabel2.setBounds(new Rectangle(433, 3, 19, 17));
    fercliniE.setBounds(new Rectangle(356, 3, 76, 17));
    fealfinE.setBounds(new Rectangle(162, 3, 77, 17));
    cLabel6.setBounds(new Rectangle(148, 3, 19, 17));
    fealiniE.setBounds(new Rectangle(70, 3, 76, 17));
    cLabel23.setText("Kilos");
    cLabel23.setBounds(new Rectangle(223, 2, 37, 16));
    PVertInf.setBorder(BorderFactory.createRaisedBevelBorder());
    PVertInf.setMaximumSize(new Dimension(564, 21));
    PVertInf.setMinimumSize(new Dimension(564, 21));
    PVertInf.setPreferredSize(new Dimension(564, 21));
    PVertInf.setLayout(null);
    vertNPiezE.setEnabled(false);
    vertNPiezE.setBounds(new Rectangle(160, 2, 44, 16));
    vertImporE.setBounds(new Rectangle(387, 2, 61, 16));
    vertImporE.setEnabled(false);
    cLabel22.setText("No. Piezas");
    cLabel22.setBounds(new Rectangle(103, 2, 57, 16));
    vertKilosE.setEnabled(false);
    vertKilosE.setBounds(new Rectangle(259, 2, 55, 16));
    cLabel26.setText("Importe");
    cLabel26.setBounds(new Rectangle(338, 2, 49, 17));
    vertNLinE.setBounds(new Rectangle(517, 2, 44, 16));
    vertNLinE.setEnabled(false);
    cLabel24.setBounds(new Rectangle(454, 2, 57, 16));
    cLabel24.setText("No. Lineas");
    opGrupos.setText("Agrupar Prod.");
    opGrupos.setBounds(new Rectangle(153, 62, 112, 20));
    Pprinc.setMaximumSize(new Dimension(2147483647, 2147483647));
    cLabel7.setBackground(Color.red);
    cLabel7.setForeground(Color.white);
    cLabel7.setOpaque(true);
    cLabel7.setHorizontalAlignment(SwingConstants.CENTER);
    cLabel7.setHorizontalTextPosition(SwingConstants.CENTER);
    cLabel7.setText("Acum. Prv.");
    cLabel7.setBounds(new Rectangle(4, 1, 98, 18));
    vertImporTE.setEnabled(false);
    vertImporTE.setBounds(new Rectangle(387, 2, 61, 16));
    vertNLinTE.setEnabled(false);
    vertNLinTE.setBounds(new Rectangle(517, 2, 44, 16));
    cLabel25.setBounds(new Rectangle(223, 2, 37, 16));
    cLabel25.setText("Kilos");
    cLabel27.setBounds(new Rectangle(103, 2, 57, 16));
    cLabel27.setText("No. Piezas");
    cLabel8.setBounds(new Rectangle(4, 1, 98, 18));
    cLabel8.setText("Acum. Total");
    cLabel8.setHorizontalTextPosition(SwingConstants.CENTER);
    cLabel8.setHorizontalAlignment(SwingConstants.CENTER);
    cLabel8.setOpaque(true);
    cLabel8.setForeground(Color.white);
    cLabel8.setBackground(Color.red);
    PacuTot.setLayout(null);
    PacuTot.setPreferredSize(new Dimension(564, 21));
    PacuTot.setMinimumSize(new Dimension(564, 21));
    PacuTot.setMaximumSize(new Dimension(564, 21));
    PacuTot.setBorder(BorderFactory.createRaisedBevelBorder());
    vertKilosTE.setEnabled(false);
    vertKilosTE.setBounds(new Rectangle(259, 2, 55, 16));
    cLabel28.setBounds(new Rectangle(454, 2, 57, 16));
    cLabel28.setText("No. Lineas");
    cLabel29.setText("Importe");
    cLabel29.setBounds(new Rectangle(338, 2, 49, 17));
    vertNPiezTE.setEnabled(false);
    vertNPiezTE.setBounds(new Rectangle(160, 2, 44, 16));
    Pdetalle.setText("cPanel1");
    Pdetalle.setLayout(gridBagLayout2);
    Pprove.setText("cPanel1");
    Pprove.setLayout(gridBagLayout3);
    Presul.setPreferredSize(new Dimension(669, 397));
    jtPrv.setBounds(new Rectangle(22, 5, 601, 351));
    jtAcuPrv.setMaximumSize(new Dimension(663, 138));
    jtAcuPrv.setMinimumSize(new Dimension(663, 138));
    jtAcuPrv.setPreferredSize(new Dimension(663, 138));
    jtAcuPrv.setText("cgrid1");
    jtAcuPrv.setBuscarVisible(false);
    PacuTot.add(cLabel8, null);
    PacuTot.add(vertNLinTE, null);
    PacuTot.add(cLabel27, null);
    PacuTot.add(vertNPiezTE, null);
    PacuTot.add(cLabel25, null);
    PacuTot.add(vertKilosTE, null);
    PacuTot.add(cLabel29, null);
    PacuTot.add(vertImporTE, null);
    PacuTot.add(cLabel28, null);
    Pdetalle.add(jtLin,    new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    Pdetalle.add(PVertInf,    new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    PVertInf.add(cLabel7, null);
    PVertInf.add(vertNLinE, null);
    PVertInf.add(cLabel22, null);
    PVertInf.add(vertNPiezE, null);
    PVertInf.add(cLabel23, null);
    PVertInf.add(vertKilosE, null);
    PVertInf.add(cLabel26, null);
    PVertInf.add(vertImporE, null);
    PVertInf.add(cLabel24, null);
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
    Pprinc.setLayout(gridBagLayout1);
    Pcond.setBorder(BorderFactory.createRaisedBevelBorder());
    Pcond.setLayout(null);

    cLabel1.setText("De Fecha Reclam.");
    cLabel2.setText("A");
    prv_codfinE.setAncTexto(50);
    prv_codiniE.setAncTexto(50);
    cLabel17.setText("A Proveedor");
    cLabel4.setText("De Proveedor");
    cLabel3.setText("Estado");


    cLabel5.setText("De Fec.Alb");

    cLabel6.setText("A");
    fealiniE.setAceptaNulo(true);
    opIncFraE.setToolTipText("Incluir Albaranes sin Facturar");
    opIncFraE.setSelected(true);
    opIncFraE.setText("Incluir Alb. Sin Fra.");
    statusBar.add(Blistar, new GridBagConstraints(8, 0, 1, 2, 0.0, 0.0,
                                                 GridBagConstraints.EAST,
                                                 GridBagConstraints.VERTICAL,
                                                 new Insets(0, 5, 0, 0), 0, 0));

    Pcond.add(prv_codfinE, null);
    Pcond.add(cLabel17, null);
    Pcond.add(cLabel4, null);
    Pcond.add(prv_codiniE, null);
    Pcond.add(cLabel5, null);
    Pcond.add(cLabel6, null);
    Pcond.add(fealiniE, null);
    Pcond.add(fealfinE, null);
    Pcond.add(fercliniE, null);
    Pcond.add(cLabel2, null);
    Pcond.add(ferclfinE, null);
    Pcond.add(cLabel1, null);
    Pcond.add(tir_codiE, null);
    Pcond.add(opGrupos, null);
    Pcond.add(Bconsultar, null);
    Pcond.add(cLabel3, null);
    Pcond.add(opIncFraE, null);
    Pprinc.add(PacuTot,   new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    Pprinc.add(Presul,  new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 8, 0, 2), 0, 0));

    Pprinc.add(Pcond,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    Presul.add(Pdetalle,  "Detalle");
    Pdetalle.add(jtCab,    new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    Presul.add(Pprove,  "Proveed.");
    Pprove.add(jtPrv,  new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 1), 0, 0));
    Pprove.add(jtAcuPrv,   new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 0, 0, 0), 0, 0));
    confGridCab();
    confGridLin();
    confGridPrv();
  }

  public void iniciarVentana() throws Exception
  {
    Pcond.setDefButton(Bconsultar);
    tir_codiE.addItem("Pendiente", "P");
    tir_codiE.addItem("Aceptado", "A");
    tir_codiE.addItem("Rechazado", "R");
    tir_codiE.addItem("Recl.Pend", "E");
    tir_codiE.addItem("TODOS", "T");

    prv_codfinE.iniciar(dtStat, this, vl, EU);
    prv_codiniE.iniciar(dtStat, this, vl, EU);
    activarEventos();
  }

  void activarEventos()
  {
    Blistar.addActionListener(new ActionListener()
   {
     public void actionPerformed(ActionEvent e)
     {
       Blistar_actionPerformed();
     }
   });

    Bconsultar.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Bconsultar_actionPerformed();
      }
    });
    jtCab.tableView.getSelectionModel().addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent e)
      {
        if (e.getValueIsAdjusting() || jtCab.isVacio() || ! jtCab.isEnabled())
          return;
        try {
            verDatAlb(jtCab.getValorInt(3),jtCab.getValorInt(4),jtCab.getValString(5),jtCab.getValorInt(6));
        } catch (Exception k)
        {
          Error("Error al ver desglose vertederos sobre albaran",k);
        }
      }
    });

  }
  void Blistar_actionPerformed()
  {

    new miThread("jjj")
    {
      public void run()
      {
        listar();
      }
    };
  }

  void listar()
   {
     mensaje("Espere .... generando Listado");
     this.setEnabled(false);
     try
     {
       String s="select c.*,r.*,pv.prv_nomb,ar.pro_nomb,mr.tir_nomb "+
          " from v_albacoc as c left join v_proveedo as pv on "+
        " pv.prv_codi =  c.prv_codi, "+
           " v_regstock  as r left join v_articulo as ar on " +
           " r.pro_codi = ar. pro_codi "+
           " left join v_motregu as mr on "+
           "  mr.tir_codi = r.tir_codi "+
        " where r.emp_codi = c.emp_codi " +
        " and r.pro_nupar = c.acc_nume " +
        " and r.pro_serie = c.acc_serie" +
        " and r.eje_nume = c.acc_ano" +
        " and rgs_cliprv = c.prv_codi " +
        " and r.rgs_recprv " +
        getCondiciones() +
        " order by  c.emp_codi,c.acc_ano,c.acc_serie,c.acc_nume ";

       ResultSet rs = dtCon1.getStatement().executeQuery(dtCon1.parseaSql(s));
       JasperReport jr;
       jr =  gnu.chu.print.util.getJasperReport(EU, "reclprv");
       java.util.HashMap mp = new java.util.HashMap();
       mp.put("acc_fecrec",fealiniE.getDate());
       mp.put("acc_fecrec1",fealfinE.getDate());
       mp.put("estado",tir_codiE.getText());
       mp.put("acc_fecrcl",fercliniE.getDate());
       mp.put("acc_fecrcl1",ferclfinE.getDate());
       mp.put("agrupProd",new Boolean(opGrupos.isSelected()));
       mp.put("inAlbsFra",new Boolean(opIncFraE.isSelected()));
       mp.put("prv_codi",new Integer(prv_codiniE.getValorInt()));
       mp.put("prv_nomb",prv_codiniE.getTextNomb());
       mp.put("prv_codi1",new Integer(prv_codfinE.getValorInt()));
       mp.put("prv_nomb1",prv_codfinE.getTextNomb());
//       mp.put("alm_nombP",alm_codiE.getTextCombo());

       JasperPrint jp = JasperFillManager.fillReport(jr, mp, new JRResultSetDataSource(rs));
       
       gnu.chu.print.util.printJasper(jp, EU);
       mensaje("");
       mensajeErr("Listado ... GENERADO");
     }
     catch (Exception k)
     {
       Error("Error al imprimir", k);
     }
     this.setEnabled(true);
   }
   String getCondiciones()
   {
     char tirCodi = tir_codiE.getValor().charAt(0);
     return ( tirCodi=='T'?" != 0":
        " = "+(tirCodi=='P'? paregalm.ESTPEND : tirCodi=='A'? paregalm.ESTACEP:tirCodi=='E'?paregalm.PENDREC:paregalm.ESTRECH))+
       (prv_codiniE.isNull()?"":" and c.prv_codi >= "+prv_codiniE.getValorInt())+
       (prv_codfinE.isNull()?"":" and c.prv_codi <= "+prv_codfinE.getValorInt())+
       (fealiniE.isNull()?"":" and c.acc_fecrec >= TO_DATE('"+fealiniE.getText()+"','dd-MM-yyyy')")+
       (fealfinE.isNull()?"":" and c.acc_fecrec <= TO_DATE('"+fealfinE.getText()+"','dd-MM-yyyy')")+
       (ferclfinE.isNull()?"":" and r.rgs_fecha <= TO_DATE('"+ferclfinE.getText()+"','dd-MM-yyyy')")+
       (fercliniE.isNull()?"":" and r.rgs_fecha <= TO_DATE('"+fercliniE.getText()+"','dd-MM-yyyy')")+
       (opIncFraE.isSelected()?"":" and c.fcc_nume > 0 ");

   }

   void Bconsultar_actionPerformed()
   {
     new miThread("")
     {
       public void run()
       {
         consulta();
       }
     };
   }

   void consulta()
   {
     error = false;
     this.setEnabled(false);
     verDetalle();
     verAcumPrv();
     this.setEnabled(true);
     mensaje("");
     if (!error)
       mensajeErr("Consulta realizada ");
   }

   void verDetalle()
   {
     s = "select c.emp_codi,c.acc_ano,c.acc_serie,c.acc_nume from v_albacoc as c,v_regstock  as r" +
         " where r.emp_codi = c.emp_codi " +
         " and r.pro_nupar = c.acc_nume " +
         " and r.pro_serie = c.acc_serie" +
         " and r.eje_nume = c.acc_ano" +
         " and rgs_cliprv = c.prv_codi " +
         " and r.rgs_recprv " +
         getCondiciones() +
         " group by c.emp_codi,c.acc_ano,c.acc_serie,c.acc_nume " +
         " order by  c.emp_codi,c.acc_ano,c.acc_serie,c.acc_nume ";

 //    debug(s);
     jtCab.setEnabled(false);
     jtCab.removeAllDatos();
     jtLin.removeAllDatos();
     try
     {
       if (!dtCon1.select(s))
       {
         mensajeErr("No encontrados datos para estos criterios");
         jtCab.setEnabled(true);
         error = true;
         return;
       }
       s = "SELECT p.prv_nomb,acc_fecrec,p.prv_codi FROM v_albacoc as c, v_proveedo AS P " +
           " where  p.prv_codi=c.prv_codi " +
           " and c.emp_codi = ? " +
           " and c.acc_nume = ? " +
           " and c.acc_serie = ?" +
           " and c.acc_ano = ?";
       PreparedStatement ps = dtStat.getPreparedStatement(s);
       ResultSet rs;
 //      double rgsKilos = 0;
 //      int rgsCanti = 0;
 //      double rgsPrreg = 0;
 //      s = "SELECT sum(r.rgs_kilos) as rgs_kilos," +
 //          " sum(r.rgs_canti) as rgs_canti ,sum(r.rgs_prregu*r.rgs_kilos) as rgs_prregu" +
 //          " FROM v_regstock as r " +
 //          " and r.emp_codi = ? "  +
 //          " and r.pro_nupar = ? " +
 //          " and r.pro_serie = ? " +
 //          " and r.eje_nume = ? ";
 //
 //      PreparedStatement psAc=dtStat.getPreparedStatement(s);
       int nLin = 0;
       do
       {
         ps.setInt(1, dtCon1.getInt("emp_codi"));
         ps.setInt(2, dtCon1.getInt("acc_nume"));
         ps.setString(3, dtCon1.getString("acc_serie"));
         ps.setInt(4, dtCon1.getInt("acc_ano"));
         rs = ps.executeQuery();
         rs.next();
         Vector v = new Vector();
         v.addElement(rs.getString("prv_codi"));
         v.addElement(rs.getString("prv_nomb"));

         v.addElement(Formatear.formatearFecha(rs.getDate("acc_fecrec"), "dd-MM-yy"));
         v.addElement(dtCon1.getString("emp_codi"));
         v.addElement(dtCon1.getString("acc_ano"));
         v.addElement(dtCon1.getString("acc_serie"));
         v.addElement(dtCon1.getString("acc_nume"));
         jtCab.addLinea(v);
         nLin++;
       }
       while (dtCon1.next());
       s = "select  sum(r.rgs_kilos) as rgs_kilos," +
           " sum(r.rgs_canti) as rgs_canti ,sum(r.rgs_prregu*r.rgs_kilos) as rgs_prregu " +
           "  from v_regstock  as r, v_albacoc as c " +
           " where r.emp_codi = c.emp_codi " +
           " and r.pro_nupar = c.acc_nume " +
           " and r.pro_serie = c.acc_serie" +
           " and r.eje_nume = c.acc_ano" +
           " and rgs_cliprv = c.prv_codi " +
           " and r.rgs_recprv " +
           getCondiciones();
       dtStat.select(s);
       vertNPiezTE.setValorDec(dtStat.getDouble("rgs_canti", true));
       vertKilosTE.setValorDec(dtStat.getDouble("rgs_kilos", true));
       vertImporTE.setValorDec(dtStat.getDouble("rgs_prregu", true));
       vertNLinTE.setValorInt(nLin);
       jtCab.requestFocusInicio();
       jtCab.setEnabled(true);
       verDatAlb(jtCab.getValorInt(3), jtCab.getValorInt(4), jtCab.getValString(5), jtCab.getValorInt(6));
     }
     catch (Exception k)
     {
       Error("Error al buscar datos de vertederos reclamados a proveedores", k);
       error = true;
     }
   }

  void verAcumPrv()
  {
    jtPrv.removeAllDatos();
    s = "SELECT pv.prv_nomb, "+
        " c.emp_codi, c.acc_nume,c.acc_serie,c.acc_ano, "+
       " r.rgs_fecha, p.pro_codi,p.pro_nomcor,r.rgs_kilos,r.rgs_canti,r.rgs_prregu," +
        " mr.tir_nomb,rgs_recprv,r.rgs_fecres,r.rgs_coment,r.rgs_nume,r.rgs_clidev " +
        " FROM v_regstock as r left join v_proveedo as pv on pv.prv_codi = r.rgs_cliprv , "+
        "  v_articulo AS p,v_motregu as mr,v_albacoc as c " +
        " where  r.pro_codi = p.pro_codi " +
        " and mr.tir_codi = r.tir_codi " +
        " and r.rgs_recprv != 0" +
        " and r.emp_codi = c.emp_codi " +
        " and r.pro_nupar = c.acc_nume " +
        " and r.pro_serie = c.acc_serie" +
        " and r.eje_nume = c.acc_ano" +
        " order by r.rgs_fecha,r.rgs_cliprv ";
//    debug("grid Prv: "+s);
    try {
      if (! dtCon1.select(s))
      {
        mensajeErr("No encontrados datos para Acumulado Proveedores");
        error=true;
        return;
      }

      PreparedStatement ps=dtCon1.getPreparedStatement("select cli_nomco,cli_nomb from clientes "+
          " where cli_codi = ?");
      ResultSet rs;
      do
      {
        Vector v=new Vector();
        v.addElement(dtCon1.getFecha("rgs_fecha","dd-MM-yy"));
        v.addElement(dtCon1.getString("prv_nomb"));
        v.addElement(""+dtCon1.getInt("emp_codi")+"-"+dtCon1.getInt("acc_ano")+"/"+dtCon1.getString("acc_serie")+
                     dtCon1.getInt("acc_nume"));
        v.addElement(dtCon1.getString("rgs_kilos"));
        v.addElement(dtCon1.getString("rgs_prregu"));
        v.addElement(""+(dtCon1.getDouble("rgs_kilos",true)*dtCon1.getDouble("rgs_prregu",true)));
        v.addElement(paregalm.getStrLongTipRecl(dtCon1.getInt("rgs_recprv")));
        v.addElement(dtCon1.getString("pro_nomcor")+"("+dtCon1.getString("pro_codi")+")");
        if (dtCon1.getInt("rgs_clidev",true)==0)
          v.addElement("");
        else
        {
          ps.setInt(1,dtCon1.getInt("rgs_clidev",true));
          rs = ps.executeQuery();
          if (rs.next())
            v.addElement(rs.getString("cli_nomb")+"("+ dtCon1.getString("rgs_clidev")+")");
          else
            v.addElement( dtCon1.getString("rgs_clidev")+" NO ENCONTRADO");
        }
        jtPrv.addLinea(v);
      } while (dtCon1.next());
      jtPrv.requestFocusInicio();
      s = "SELECT rgs_cliprv,sum(rgs_kilos) as rgs_kilos, "+
          " sum(r.rgs_canti) as rgs_canti,sum (r.rgs_prregu*rgs_kilos) as importe " +
          " FROM v_regstock as r , " +
          " v_albacoc as c " +
          " where r.rgs_recprv != 0" +
          " and r.emp_codi = c.emp_codi " +
          " and r.pro_nupar = c.acc_nume " +
          " and r.pro_serie = c.acc_serie" +
          " and r.eje_nume = c.acc_ano" +
          " and rgs_cliprv = c.prv_codi and " +
          " r.rgs_recprv " + getCondiciones() +
          " group by rgs_cliprv " +
          " order by r.rgs_cliprv ";
//      debug("grid Prv1: "+s);
      jtAcuPrv.removeAllDatos();
      if (! dtCon1.select(s))
        return;
      ps=dtCon1.getPreparedStatement("select prv_nomb from v_proveedo "+
          " where prv_codi = ?");
      do
      {
        Vector v=new Vector();
        ps.setInt(1,dtCon1.getInt("rgs_cliprv",true));
        rs = ps.executeQuery();
        if (rs.next())
          v.addElement(rs.getString("prv_nomb"));
        else
          v.addElement(dtCon1.getString("rgs_clidev")+ " NO ENCONTRADO");
        v.addElement(dtCon1.getString("rgs_canti"));
        v.addElement(dtCon1.getString("rgs_kilos"));
        v.addElement(dtCon1.getString("importe"));
        jtAcuPrv.addLinea(v);
      } while (dtCon1.next());
      jtAcuPrv.requestFocusInicio();
    } catch (Exception k)
    {
      Error("Error al llenar grid Proveedores",k);
      error=true;
    }
  }
  void verDatAlb(int empCodi, int ejeNume, String accSerie, int accNume) throws Exception
  {
    if (! opGrupos.isSelected())
      s = "SELECT p.pro_codi,p.pro_nomb,r.rgs_kilos,r.rgs_canti,r.rgs_prregu," +
        " r.pro_numind,mr.tir_nomb,rgs_recprv,r.rgs_fecha,r.rgs_fecres,r.rgs_coment,r.rgs_nume" +
        " FROM v_regstock as r, v_articulo AS p,v_motregu as mr " +
        " where  r.pro_codi = p.pro_codi " +
        " and r.emp_codi = " + empCodi +
        " and r.pro_nupar = " + accNume +
        " and r.pro_serie = '" + accSerie + "'" +
        " and r.eje_nume = " + ejeNume+
        " and mr.tir_codi = r.tir_codi "+
        " and r.rgs_recprv != 0"+
        " order by r.rgs_nume ";
    else
      s = "SELECT p.pro_codi,p.pro_nomb,mr.tir_nomb,rgs_recprv,sum(r.rgs_kilos) as rgs_kilos,"+
          " sum(r.rgs_canti) as rgs_canti ,sum(r.rgs_prregu*r.rgs_kilos) as rgs_prregu" +
        " FROM v_regstock as r, v_articulo AS p,v_motregu as mr " +
        " where  r.pro_codi = p.pro_codi " +
        " and r.emp_codi = " + empCodi +
        " and r.pro_nupar = " + accNume +
        " and r.pro_serie = '" + accSerie + "'" +
        " and r.eje_nume = " + ejeNume+
        " and r.rgs_recprv != 0"+
        " and mr.tir_codi = r.tir_codi "+
        " group by p.pro_codi,p.pro_nomb,rgs_recprv,mr.tir_nomb "+
        " order by p.pro_codi,rgs_recprv, mr.tir_nomb ";

//    debug (s);
    jtLin.removeAllDatos();
    if (!dtCon1.select(s))
    {
      mensajeErr("ERROR: No encontrados vertederos para esta linea");
      return;
    }
    rgsRecprv=dtCon1.getInt("rgs_recprv");
    double rgsKilos=0;
    int rgsCanti=0;
    double rgsPrreg=0;
    int proCodi=dtCon1.getInt("pro_codi");
//    String proNomb=dtCon1.getString("pro_nomb");
    String tirNomb=dtCon1.getString("tir_nomb");
    do
    {
      if (! opGrupos.isSelected())
      {
        if (proCodi != dtCon1.getInt("pro_codi") || ! tirNomb.equals( dtCon1.getString("tir_nomb"))
            ||  rgsRecprv != dtCon1.getInt("rgs_recprv"))
        {
          ponAcumPro(proCodi, "Total Producto", rgsRecprv, rgsKilos, rgsCanti, rgsPrreg);
          proCodi = dtCon1.getInt("pro_codi");
          tirNomb = dtCon1.getString("tir_nomb");
          rgsRecprv=dtCon1.getInt("rgs_recprv");
          rgsKilos = 0;
          rgsCanti = 0;
          rgsPrreg = 0;
        }

        rgsKilos+=dtCon1.getDouble("rgs_kilos");
        rgsCanti+=dtCon1.getInt("rgs_canti");
        rgsPrreg+=dtCon1.getDouble("rgs_kilos")*dtCon1.getDouble("rgs_prregu");
        Vector v = new Vector();
        v.addElement(dtCon1.getString("pro_codi"));
        v.addElement(dtCon1.getString("pro_nomb"));
        v.addElement(dtCon1.getString("rgs_kilos"));
        v.addElement(dtCon1.getString("rgs_canti"));
        v.addElement(dtCon1.getString("rgs_prregu"));
        v.addElement(dtCon1.getString("pro_numind"));
        v.addElement(dtCon1.getInt("rgs_recprv") == paregalm.ESTPEND ? "P" :
                     dtCon1.getInt("rgs_recprv") == paregalm.ESTACEP ? "A" : "R");
        v.addElement(dtCon1.getFecha("rgs_fecha", "dd-MM-yy"));
        v.addElement(dtCon1.getFecha("rgs_fecres", "dd-MM-yy"));
        v.addElement(dtCon1.getString("tir_nomb"));
        jtLin.addLinea(v);
      }
      else
        ponAcumPro(dtCon1.getInt("pro_codi"), dtCon1.getString("pro_nomb"),
                   dtCon1.getInt("rgs_recprv"),
                   dtCon1.getDouble("rgs_kilos"), dtCon1.getInt("rgs_canti"),
                 dtCon1.getDouble("rgs_prregu"));
    } while (dtCon1.next());
    if (! opGrupos.isSelected())
      ponAcumPro(proCodi, "Total Producto", rgsRecprv, rgsKilos, rgsCanti, rgsPrreg);
    jtLin.requestFocusInicio();
    actAcumVert();
  }
  void  ponAcumPro(int proCodi,String proNomb,int rgsRecprv,double rgsKilos,int rgsCanti,double rgsPrreg)
  {
    Vector v=new Vector();
    v.addElement(""+proCodi);
    v.addElement(""+proNomb);
    v.addElement(""+rgsKilos);
    v.addElement(""+rgsCanti);
    v.addElement(""+rgsPrreg/rgsKilos);
    v.addElement("");
    v.addElement(rgsRecprv == paregalm.ESTPEND ? "P" :
                     rgsRecprv == paregalm.ESTACEP ? "A" : "R");
    v.addElement("");
    v.addElement("");
    v.addElement("");
    jtLin.addLinea(v);
  }
  private void confGridCab() throws Exception
  {
    Vector v = new Vector();
    v.addElement("Prov"); //0
    v.addElement("Nombre Prv"); // 1
    v.addElement("Fec.Alb"); // 2
    v.addElement("Emp"); // 3
    v.addElement("Ejer"); // 4
    v.addElement("Serie"); // 5
    v.addElement("Numero"); // 6
    jtCab.setCabecera(v);

    jtCab.setAnchoColumna(new int[]
                          {50, 250, 70, 40, 40, 40, 60});
    jtCab.setAlinearColumna(new int[]{2,0,1,2,2,2,2});
    jtCab.setMaximumSize(new Dimension(664, 214));
    jtCab.setMinimumSize(new Dimension(664, 214));
    jtCab.setPreferredSize(new Dimension(664, 214));
    jtCab.setAjustarGrid(true);
  }

  private void confGridLin() throws Exception
  {
    Vector v = new Vector();
    v.addElement("Prod."); // 0
    v.addElement("Nombre Prod"); // 1
    v.addElement("Kilos"); // 2
    v.addElement("Unid"); // 3
    v.addElement("Precio"); // 4
    v.addElement("Ind."); // 5 Inviduo
    v.addElement("Est"); // 6
    v.addElement("Fec.Recl"); // 7
    v.addElement("Fec.Res."); // 8
    v.addElement("T.Recl"); //9
    jtLin.setCabecera(v);
    jtLin.setAnchoColumna(new int[]
                          {50, 160, 60, 50, 50, 35, 40, 70, 70,120});
    jtLin.setAlinearColumna(new int[]{2,0,2,2,2,2,1,1,1,20});
    jtLin.setFormatoColumna(3, "---9");
    jtLin.setFormatoColumna(4, "--,--9.99");
    jtLin.setFormatoColumna(2, "--,--9.99");

    jtLin.setAjustarGrid(true);
  }

  private void confGridPrv() throws Exception
  {
    Vector v=new Vector();
    v.addElement("Fec.Recl"); // 0
    v.addElement("Proveedor"); // 1
    v.addElement("Albaran"); // 2
    v.addElement("Kilos"); // 3
    v.addElement("Precio"); // 4
    v.addElement("Importe"); // 5
    v.addElement("Estado"); // 6
    v.addElement("Producto"); // 7
    v.addElement("Cliente"); // 8
    jtPrv.setCabecera(v);
    jtPrv.setAnchoColumna(new int[]{60,130,90,55,50,60,40,130,150});
    jtPrv.setAlinearColumna(new int[]{1,0,0,2,2,2,1,0,0});
    jtPrv.setFormatoColumna(3, "--,--9.99");
    jtPrv.setFormatoColumna(4, "---9.99");
    jtPrv.setFormatoColumna(5, "--,--9.99");
    confGridAcuPrv();
  }

  private void confGridAcuPrv() throws Exception
  {
    Vector v = new Vector();
    v.addElement("Proveedor"); // 0
    v.addElement("Unid."); // 1
    v.addElement("Kilos"); // 2
    v.addElement("Importe"); // 3
    jtAcuPrv.setCabecera(v);
    jtAcuPrv.setAnchoColumna(new int[]
                             {250, 50, 65, 80});
    jtAcuPrv.setAlinearColumna(new int[]
                               {0, 2, 2, 2});
    jtAcuPrv.setFormatoColumna(1, "---9");
    jtAcuPrv.setFormatoColumna(2, "--,--9.99");
    jtAcuPrv.setFormatoColumna(3, "---,--9.99");

  }
  void actAcumVert()
  {
    double kilos = 0, impor = 0;
    int unid = 0;
    int numVert = 0;
    for (int n = 0; n < jtLin.getRowCount(); n++)
    {
      if (jtLin.getValorInt(n,5)>0)
        continue;
      numVert++;

      kilos += jtLin.getValorDec(n, 2);
      unid += jtLin.getValorInt(n, 3);
      impor += (jtLin.getValorDec(n, 2) * jtLin.getValorDec(n, 4));
//      debug("kilos:"+jtLin.getValorDec(n, 2)+" Impor: "+jtLin.getValorDec(n, 4) );
    }
    vertNPiezE.setValorDec(unid);
    vertKilosE.setValorDec(kilos);
    vertImporE.setValorDec(impor);
    vertNLinE.setValorDec(numVert);
  }

}
