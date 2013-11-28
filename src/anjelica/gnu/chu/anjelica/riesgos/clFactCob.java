package gnu.chu.anjelica.riesgos;

import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import gnu.chu.sql.*;
import java.util.*;
import javax.swing.*;
import gnu.chu.Menu.*;
import gnu.chu.camposdb.*;

/**
 *
 * <p>Titulo: clFactCob</p>
 * <p>Descripción: Consulta/Listado Facturas Cobradas.
 *    Saca la media de los dias de cobro</p>
 * <p>Copyright: Copyright (c) 2005-2009
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
 * <p>Empresa: miSL</p>
 * @author chuchi P
 * @version 1.0
 */

public class clFactCob  extends ventana
{
  public static String diasVto[] = new String[4];
  CPanel Pprinc = new CPanel();
  CPanel cPanel1 = new CPanel();
  CTextField feciniE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel4 = new CLabel();
  CTextField fecfinE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel3 = new CLabel();
  CLabel cLabel1 = new CLabel();
  CLinkBox tpcIniE = new CLinkBox();
  CLabel cLabel2 = new CLabel();
  CLinkBox tpcFinE = new CLinkBox();
  CLabel cLabel5 = new CLabel();
  CButton Baceptar = new CButton();
  CButton Bacfevt = new CButton();
  CComboBox opCobra = new CComboBox();
  Cgrid jt = new Cgrid(9);
  CPanel cPanel2 = new CPanel();
  CLabel cLabel6 = new CLabel();
  CTextField numFrasE = new CTextField(Types.DECIMAL, "###9");
  CLabel cLabel7 = new CLabel();
  CTextField impFrasE = new CTextField(Types.DECIMAL, "---,---,--9.9");
  CLabel cLabel8 = new CLabel();
  CTextField medDiasE = new CTextField(Types.DECIMAL, "##9");
  CLabel cLabel9 = new CLabel();
  CLinkBox zon_codiE = new CLinkBox();
  String s;
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CLabel cLabel10 = new CLabel();
  CTextField fecCobE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel11 = new CLabel();
  CLabel cLabel12 = new CLabel();
  cliPanel cli_codiE = new cliPanel();
  CLabel cLabel13 = new CLabel();
  CTextField medImpE = new CTextField(Types.DECIMAL, "##9");

  public clFactCob(EntornoUsuario eu, Principal p)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    setTitulo("Cons./List. Media Tiempo Cobros");

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

  public clFactCob(gnu.chu.anjelica.menu p, EntornoUsuario eu)
  {
    EU = eu;
    vl = p.getLayeredPane();
     setTitulo("Cons./List. Media Tiempo Cobros") ;
    eje = false;

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
    iniciarFrame();
    this.setSize(new Dimension(547, 403));
    this.setVersion("2009-26-03");
    conecta();

    statusBar = new StatusBar(this);
    cPanel1.setMaximumSize(new Dimension(532, 81));
    cPanel1.setMinimumSize(new Dimension(532, 81));
    cPanel1.setPreferredSize(new Dimension(532, 81));
    jt.setMaximumSize(new Dimension(530, 270));
    jt.setMinimumSize(new Dimension(530, 270));
    jt.setPreferredSize(new Dimension(530, 270));
    cPanel2.setMaximumSize(new Dimension(525, 25));
    cPanel2.setMinimumSize(new Dimension(525, 25));
    cPanel2.setPreferredSize(new Dimension(525, 25));
    medDiasE.setBounds(new Rectangle(346, 4, 46, 16));
    cLabel8.setBounds(new Rectangle(258, 4, 88, 16));
    impFrasE.setBounds(new Rectangle(162, 4, 94, 16));
    cLabel7.setBounds(new Rectangle(102, 4, 54, 16));
    numFrasE.setBounds(new Rectangle(52, 4, 42, 16));
    cLabel6.setBounds(new Rectangle(4, 4, 43, 16));
    cLabel10.setText("Fec.Cobro");
    cLabel10.setBounds(new Rectangle(389, 40, 58, 16));
    fecCobE.setBounds(new Rectangle(449, 40, 73, 17));
    cLabel11.setText("Ver");
    cLabel11.setBounds(new Rectangle(379, 22, 29, 16));
    cLabel12.setText("Cliente");
    cLabel12.setBounds(new Rectangle(5, 63, 40, 16));
    cli_codiE.setBounds(new Rectangle(48, 61, 312, 17));
    cLabel13.setText("Media S/Imp.");
    cLabel13.setBounds(new Rectangle(395, 4, 73, 16));
    medImpE.setEnabled(false);
    medImpE.setBounds(new Rectangle(467, 4, 44, 16));
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);

    Pprinc.setLayout(gridBagLayout1);
    cPanel1.setBorder(BorderFactory.createRaisedBevelBorder());
    cPanel1.setLayout(null);
    feciniE.setBounds(new Rectangle(60, 22, 73, 17));
    cLabel4.setBounds(new Rectangle(135, 22, 47, 17));
    cLabel4.setText("A Fecha");
    fecfinE.setBounds(new Rectangle(181, 22, 73, 17));
    cLabel3.setBounds(new Rectangle(6, 22, 51, 18));
    cLabel3.setText("De Fecha");
    cLabel1.setBackground(Color.orange);
    cLabel1.setForeground(Color.white);
    cLabel1.setOpaque(true);
    cLabel1.setText("Tipo Cobro");
    cLabel1.setBounds(new Rectangle(6, 3, 66, 16));
    tpcIniE.setAncTexto(30);
    tpcIniE.setBounds(new Rectangle(101, 2, 197, 18));
    cLabel2.setBounds(new Rectangle(301, 3, 20, 16));
    cLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
    cLabel2.setText("A");
    tpcFinE.setBounds(new Rectangle(329, 3, 197, 18));
    tpcFinE.setAncTexto(30);
    cLabel5.setHorizontalAlignment(SwingConstants.RIGHT);
    cLabel5.setHorizontalTextPosition(SwingConstants.RIGHT);
    cLabel5.setText("De");
    cLabel5.setBounds(new Rectangle(73, 3, 21, 16));
    Baceptar.setBounds(new Rectangle(366, 59, 67, 19));
    Baceptar.setMargin(new Insets(0, 0, 0, 0));
    Baceptar.setText("Consultar");
    Bacfevt.setBounds(new Rectangle(446, 59, 80, 19));
    Bacfevt.setMargin(new Insets(0, 0, 0, 0));
    Bacfevt.setText("Act. Fec.Vto");
    opCobra.setText("Inc. NO cobrado");
    opCobra.setBounds(new Rectangle(411, 22, 112, 17));
    jt.setBuscarVisible(false);
    cPanel2.setBorder(BorderFactory.createLoweredBevelBorder());
    cPanel2.setLayout(null);
    cLabel6.setToolTipText("");
    cLabel6.setText("N.Fras");
    numFrasE.setText("");
    numFrasE.setEnabled(false);
    cLabel7.setText("Imp. Fras");
    impFrasE.setEnabled(false);
    cLabel8.setToolTipText("");
    cLabel8.setText("Media S/N.FRA");
    medDiasE.setText("");
    medDiasE.setEnabled(false);
    cLabel9.setText("Zona");
    cLabel9.setBounds(new Rectangle(6, 44, 33, 16));
    zon_codiE.setAncTexto(30);
    Vector v = new Vector();
    v.add("Cliente"); // 0
    v.add("Nombre Cliente"); // 1
    v.add("Fact."); // 2
    v.add("Imp.Fra."); // 3
    v.add("Fec.Fr/Al"); // 4
    v.add("Imp.Cob."); // 5
    v.add("Fec.Cob/Vto"); // 6
    v.add("Dias"); // 7
    v.add("T.Cobro"); // 8
    jt.setCabecera(v);
    jt.setAlinearColumna(new int[]
                         {0, 0, 0, 2, 1, 2, 1, 2, 0});
    jt.setAnchoColumna(new int[]
                       {30, 120, 50, 50, 50, 50, 50, 20, 100});
    jt.setAjustarGrid(true);
    zon_codiE.setBounds(new Rectangle(48, 42, 229, 18));
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
    Pprinc.add(cPanel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.NONE,
                                               new Insets(0, 1, 0, 4), 0, 0));
    cPanel1.add(cLabel1, null);
    cPanel1.add(tpcIniE, null);
    cPanel1.add(cLabel5, null);
    cPanel1.add(tpcFinE, null);
    cPanel1.add(cLabel2, null);
    cPanel1.add(opCobra, null);
    cPanel1.add(cLabel3, null);
    cPanel1.add(feciniE, null);
    Pprinc.add(cPanel2, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.NONE,
                                               new Insets(2, 0, 0, 0), -4, 0));
    cPanel2.add(cLabel6, null);
    cPanel2.add(numFrasE, null);
    cPanel2.add(cLabel7, null);
    cPanel2.add(impFrasE, null);
    cPanel2.add(cLabel8, null);
    cPanel2.add(medDiasE, null);
    cPanel2.add(cLabel13, null);
    cPanel2.add(medImpE, null);
    Pprinc.add(jt, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
                                          , GridBagConstraints.CENTER,
                                          GridBagConstraints.BOTH,
                                          new Insets(2, 1, 0, 4), 0, 0));
    cPanel1.add(cLabel9, null);
    cPanel1.add(fecfinE, null);
    cPanel1.add(cLabel4, null);
    cPanel1.add(cLabel11, null);
    cPanel1.add(cLabel12, null);
    cPanel1.add(cli_codiE, null);
    cPanel1.add(Bacfevt, null);
    cPanel1.add(Baceptar, null);
    cPanel1.add(fecCobE, null);
    cPanel1.add(cLabel10, null);
    cPanel1.add(zon_codiE, null);
  }

  public void iniciarVentana() throws Exception
  {
    cli_codiE.iniciar(dtStat, this, vl, EU);

    opCobra.addItem("Cobrado", "C");
    opCobra.addItem("Pendiente", "P");
    opCobra.addItem("Cobr+Pen", "CP");

    tpcIniE.setFormato(true);
    tpcIniE.setFormato(Types.DECIMAL, "##9", 3);
    s = "select * from v_cobtipo order by tpc_codi ";
    int valIni = 0;
    if (dtCon1.select(s))
      valIni = dtCon1.getInt("tpc_codi");
    tpcIniE.addDatos(dtCon1);
    tpcIniE.setText("" + valIni);

    tpcFinE.setFormato(true);
    tpcFinE.setFormato(Types.DECIMAL, "##9", 3);
    s = "select * from v_cobtipo order by tpc_codi ";
    dtCon1.select(s);
    tpcFinE.addDatos(dtCon1);
    if (!dtCon1.getNOREG())
      tpcFinE.setText(dtCon1.getString("tpc_codi"));
    zon_codiE.setFormato(Types.CHAR, "XX");
    zon_codiE.texto.setMayusc(true);
    gnu.chu.anjelica.pad.pdconfig.llenaDiscr(dtCon1,zon_codiE,"CR",EU.em_cod);

    zon_codiE.addDatos("**", "TODOS");
    zon_codiE.setText("**");
    activarEventos();
  }

  void activarEventos()
  {
    Bacfevt.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Bacfevt_actionPerformed();
      }
    });
    Baceptar.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Baceptar_actionPerformed();
      }
    });
  }

  void Baceptar_actionPerformed()
  {
    if (tpcIniE.getError())
    {
      mensajeErr("Codigo Cobro Inicio NO Valido");
      tpcIniE.requestFocus();
      return;
    }
    if (tpcFinE.getError())
    {
      mensajeErr("Codigo Cobro Final NO Valido");
      tpcFinE.requestFocus();
      return;
    }

    if (feciniE.isNull())
    {
      mensajeErr("Introduzca Fecha Inicio");
      feciniE.requestFocus();
      return;
    }
    if (fecfinE.isNull())
    {
      mensajeErr("Introduzca Fecha Final");
      fecfinE.requestFocus();
      return;
    }
    if (fecCobE.isNull() && opCobra.getValor().indexOf("P") >= 0)
    {
      mensajeErr("Introduzca Fecha Cobro");
      fecCobE.requestFocus();
      return;
    }
    new clFactCobTh(this);
  }

  void verCobros()
  {
    String zonCodi = zon_codiE.getText().trim().equals("") ? null :
        zon_codiE.getText();
    if (zonCodi != null)
    {
      if (zonCodi.equals("*") || zonCodi.equals("**"))
        zonCodi = null;
    }
    if (zonCodi != null)
      zonCodi = zonCodi.replace('*', '%');
    try
    {
      this.setEnabled(false);

      mensaje("Espere, buscando datos ...");
      s = "";
      if (opCobra.getValor().indexOf("C") >= 0)
        s = "SELECT f.cli_codi,f.fvc_serie,f.fvc_nume,f.fvc_ano,f.emp_codi, f.fvc_sumtot,c.cob_impor,f.fvc_fecfra," +
            " c.cob_feccob,c.cob_fecvto,ct.tpc_nomb,cl.cli_nomb " +
            " FROM v_facvec f,v_cobros c, v_cobtipo as ct,clientes as cl " +
            " where fvc_cobrad = -1 " +
            " and  c.cob_anofac=f.fvc_ano " +
            " and f.cli_codi = cl.cli_codi " +
            (zonCodi != null ? " and cli_zonrep  like '" + zonCodi + "'" : "") +
            " and c.tpc_codi = ct.tpc_codi " +
            " and c.fac_nume =f.fvc_nume " +
            " and c.fvc_serie = f.fvc_serie "+
            (cli_codiE.isNull() || cli_codiE.getValorInt() == 0 ? "" :
             " and cl.cli_codi = " + cli_codiE.getText()) +
//          " and a.emp_codi = f.emp_codi "+
//          " and a.fvc_ano = f.fvc_ano "+
//          " and a.fvc_nume = f.fvc_nume "+
            " and ct.tpc_codi >= " + tpcIniE.getValorInt() +
            " and ct.tpc_codi <= " + tpcFinE.getValorInt() +
            " and f.fvc_fecfra >= TO_DATE('" + feciniE.getText() +
            "','dd-MM-yyyy') " +
            " and f.fvc_fecfra <= TO_DATE('" + fecfinE.getText() +
            "','dd-MM-yyyy') ";
      if (opCobra.getValor().indexOf("P") >= 0)
      {
        if (!s.equals(""))
          s += " union ";
        s += "SELECT f.cli_codi,f.fvc_serie,f.fvc_nume,f.fvc_ano,f.emp_codi, f.fvc_sumtot," +
            " FVC_impcob as cob_impor,f.fvc_fecfra," +
            " TO_DATE('" + fecCobE.getText() +"','dd-MM-yyyy')  as cob_feccob," +
            " TO_DATE('" + fecCobE.getText() +"','dd-MM-yyyy')  as cob_fecvto," +
            "'' as tpc_nomb,cl.cli_nomb " +
            " FROM v_facvec f,clientes as cl " +
            " where fvc_cobrad = 0 " +
            " and f.cli_codi = cl.cli_codi " +
            (cli_codiE.isNull() || cli_codiE.getValorInt() == 0 ? "" :
             " and cl.cli_codi = " + cli_codiE.getText()) +
//            " and a.emp_codi = f.emp_codi " +
//            " and a.fvc_ano = f.fvc_ano " +
//            " and a.fvc_nume = f.fvc_nume " +
            (zonCodi != null ? " and cli_zonrep  like '" + zonCodi + "'" : "") +
            " and f.fvc_fecfra >= TO_DATE('" + feciniE.getText() +
            "','dd-MM-yyyy') " +
            " and f.fvc_fecfra <= TO_DATE('" + fecfinE.getText() +
            "','dd-MM-yyyy') ";
      }
      s += "  order by 1,2 ";
      if (!dtCon1.select(s))
      {
        mensajeErr("NO encontradas Facturas con estas condiciones");
        jt.removeAllDatos();
        this.setEnabled(true);
        return;
      }
//      debug(s);
      jt.panelG.setVisible(false);
      jt.removeAllDatos();
      int dias;
      int nFras = 0;
      int diasCob = 0;
      double diCobImp = 0;
      double impFras = 0;
      String fecCob, fecFra;
      do
      {
        mensaje("Tratando Cliente: " + dtCon1.getInt("cli_codi"),false);
        Vector v = new Vector();
        v.addElement(dtCon1.getString("cli_codi"));
        v.addElement(dtCon1.getString("cli_nomb"));
        v.addElement(dtCon1.getString("fvc_ano") + "-" +dtCon1.getString("fvc_serie")+"/"+
                     dtCon1.getString("fvc_nume"));
        v.addElement(dtCon1.getString("fvc_sumtot"));
        v.addElement(dtCon1.getFecha("fvc_fecfra", "dd-MM-yy"));
//        v.addElement(opAlbar.isSelected()?
//                     dtCon1.getFecha("avc_fecalb","dd-MM-yyyy"):
//                     dtCon1.getFecha("fvc_fecfra","dd-MM-yyyy"));
        v.addElement(dtCon1.getString("cob_impor"));
        fecCob = dtCon1.getDatos("cob_fecvto") == null ?
            dtCon1.getFecha("cob_feccob", "dd-MM-yy") :
            dtCon1.getFecha("cob_fecvto", "dd-MM-yy");
        dias = Formatear.restaDias(fecCob,
                                   dtCon1.getFecha("fvc_fecfra", "dd-MM-yy"));
        diCobImp += dias * dtCon1.getDouble("fvc_sumtot");
        v.addElement(fecCob);
        v.addElement("" + dias);
        v.addElement(dtCon1.getString("tpc_nomb"));
        nFras++;
        diasCob += dias;
        impFras += dtCon1.getDouble("fvc_sumtot");
        jt.addLinea(v);
      }      while (dtCon1.next());
      mensaje("");
      numFrasE.setValorDec(nFras);
      impFrasE.setValorDec(impFras);
      medDiasE.setValorDec(diasCob / nFras);

      medImpE.setValorDec(diCobImp / impFras);
      jt.panelG.setVisible(true);
      jt.requestFocusInicio();
      this.setEnabled(true);
      mensajeErr("Consulta ... REALIZADA");
    }
    catch (Exception k)
    {
      Error("Error al Buscar Facturas Cobradas", k);
    }

  }

  /**
   * Actualizar Fecha de Vencimiento
   */
  void Bacfevt_actionPerformed()
  {
    if (feciniE.isNull())
    {
      mensajeErr("Introduzca Fecha Inicio");
      feciniE.requestFocus();
      return;
    }
    if (fecfinE.isNull())
    {
      mensajeErr("Introduzca Fecha Final");
      fecfinE.requestFocus();
      return;
    }

    new miThread("")
    {
      public void run()
      {
        actFecVto();
      }
    };
  }

/**
 * Si encuentra facturas cuyo tipo de pago sea giro y no tienen asignada
 * fecha de vto. se lo calcula y se lo pone.
 */
  void actFecVto()
  {
    this.setEnabled(false);
    try
    {
      s = "SELECT f.fvc_ano,f.emp_codi,f.fvc_nume, f.fvc_fecfra," +
          " c.cob_feccob,c.cob_fecvto,f.cli_codi,fp.*" +
          " FROM v_facvec f,v_cobros c, v_cobtipo as ct,clientes cl,v_forpago as fp " +
          " where fvc_cobrad = -1 " +
          " and f.cli_codi = cl.cli_codi " +
          " and cl.fpa_codi = fp.fpa_codi " +
          " and fp.fpa_esgir != 0" +
          " and  c.cob_anofac=f.fvc_ano " +
          " and c.tpc_codi = ct.tpc_codi " +
          " and c.fac_nume =f.fvc_nume " +
          " and c.fvc_serie = f.fvc_serie "+
          " and cob_fecvto is null " +
          " and f.fvc_fecfra >= TO_DATE('" + feciniE.getText() +"','dd-MM-yyyy') " +
          " and f.fvc_fecfra <= TO_DATE('" + fecfinE.getText() +
          "','dd-MM-yyyy') " +
          "  order by f.fvc_nume ";

      if (!dtCon1.select(s))
      {
        mensajeErr("No encontradas Facturas");
        this.setEnabled(true);
        return;
      }
      int nFras = 0;
//      debug(dtCon1.getStrSelect());
      do
      {
        mensajeErr("Tratando Fact: " + dtCon1.getInt("fvc_nume"), false);
        calDiasVto(dtCon1, 0, 0, 0, dtCon1.getFecha("fvc_fecfra", "dd-MM-yyyy"));
        s = "SELECT * FROM v_cobros WHERE cob_anofac= " +
            dtCon1.getInt("fvc_ano") +
            " and emp_codi = " + dtCon1.getInt("emp_codi") +
            " and fac_nume = " + dtCon1.getInt("fvc_nume");
        dtStat.select(s, true);
        dtStat.edit(dtStat.getCondWhere());
        dtStat.setDato("cob_fecvto", diasVto[0], "dd-MM-yyyy");
        dtStat.update(stUp);
        nFras++;
      }
      while (dtCon1.next());
      ctUp.commit();
//      ctUp.rollback();
      mensajeErr("Fecha de Vto ... ACTUALIZADO .. N.Fras: " + nFras);
      this.setEnabled(true);
    }
    catch (Throwable k)
    {
      Error("Error al Actualizar Fecha Vto", k);
    }
  }

  void fecfinE_actionPerformed(ActionEvent e)
  {

  }

  public static int calDiasVto(int fpaDia1, int fpaDia2, int fpaDia3, int dia1, int dia2, int dia3,
                               String fecfra) throws Exception
  {
    return calDiasVto(fpaDia1,fpaDia2,fpaDia3,dia1,dia2,dia3,fecfra,"C");
  }
  /**
   *  Calcula los dias de Vto. para una forma de pago.
   * @param fpaDia1 Dias de Pago -1 segun Forma Pago
   * @param fpaDia2 Dias de Pago -2 segun Forma Pago
   * @param fpaDia3 Dias de Pago -3 segun Forma Pago
   * @param dia1 int Dia de Pago 1
   * @param dia2 int Dia de Pago 2
   * @param dia3 int Dia de pago 3
   * @param fecfra String Fecha de Factura (En formato dd-MM-yyyy)
   * @param tipVto String tipo de Vto. (Comercial o Natural)
   * @throws Throwable En caso de error
   * @return int N� de Fechas de Vto en array  diasVto[]
   */

  public static int calDiasVto(int fpaDia1, int fpaDia2, int fpaDia3, int dia1, int dia2, int dia3,
                               String fecfra, String tipVto) throws Exception
  {

    diasVto[0] = null;
    diasVto[1] = null;
    diasVto[2] = null;
    diasVto[3] = null;

    diasVto[0] = calculaVtos(fpaDia1, fecfra,
                             fecfra, dia1, dia2, dia3, tipVto);
    if (fpaDia2 != 0)
      diasVto[1] = calculaVtos(fpaDia2, fecfra,
                               fecfra, dia1, dia2, dia3, tipVto);
    else
      return 1;
    if (fpaDia3 != 0)
      diasVto[2] = calculaVtos(fpaDia3, fecfra,
                               fecfra, dia1, dia2, dia3, tipVto);
    else
      return 2;

    return 3;

  }

   public static int calDiasVto(DatosTabla fpa, int dia1, int dia2, int dia3,
                                String fecfra) throws Exception
   {
     return calDiasVto(fpa.getInt("fpa_dia1"), fpa.getInt("fpa_dia2"), fpa.getInt("fpa_dia3"),
                       dia1, dia2, dia3, fecfra, "C");
   }

   public static int calDiasVto(int vto1, int vto2, int vto3,
                           String fecfra) throws Exception
    {
      return calDiasVto(vto1,vto2,vto3,0,0,0,fecfra,"C");
    }

  /**
   *  Calcula los dias de Vto. para una forma de pago.
   * @param fpa DatosTabla con los datos de la tabla v_forpago
   * @param dia1 int Dia de Pago 1
   * @param dia2 int Dia de Pago 2
   * @param dia3 int Dia de pago 3
   * @param fecfra String Fecha de Factura
   * @param tipVto String   Tipo de Vto (Comercial=30 o Natural seg�n Mes)
   * @throws Throwable En caso de error
   * @return int No de Fechas de Vto en array  diasVto[]
   */
  public static int calDiasVto(DatosTabla fpa, int dia1, int dia2, int dia3,
                         String fecfra,String tipVto) throws Exception
  {
    return calDiasVto(fpa.getInt("fpa_dia1"),fpa.getInt("fpa_dia2"),fpa.getInt("fpa_dia3"),
               dia1,dia2,dia3,fecfra,tipVto);
  }
  /**
   * Calcula  Vencimiento de los Efectos de una Factura
   * @param dias int Dias de Plazo,
   * @param fechaIni String Fecha Inicial (en formato dd-MM-yyyy)
   * @param fechaMin String Fecha Minima,
   * @param dia1 int  dia pago1,
   * @param dia2 int dia pago2,
   * @param dia3 int dia pago3,
   * @param vtocomer String   Tipo de Calculo (Comercial=30 o Natural seg�n Mes)
   * @throws Throwable Algun tipo de error
   * @return String Fecha de Vto.
   */

  private static String calculaVtos(int dias, String fechaIni, String fechaMin,
                             int dia1, int dia2, int dia3, String vtocomer) throws Exception
  {
    int Dia = 0, Mes = 0, Eje = 0, DiasMes = 0;
    Fecha vtoGiro = new Fecha(fechaIni, "dd-MM-yyyy");

    if (vtocomer.equals("C"))
    {
      Dia = vtoGiro.getDia(); //Vtos Comerciales
      Mes = vtoGiro.getMes();
      Eje = vtoGiro.getYear();
      Dia = Dia + dias;
      while (Dia > 30)
      {
        DiasMes = vtoGiro.getDiasMes(Mes, Eje);
        if (Dia == DiasMes && (vtoGiro.getDia() > 1))
          break;
        Dia = Dia - 30;
        Mes++;
        while (Mes > 12)
        {
          Mes = Mes - 12;
          Eje++;
        }
      }

      if (Mes == 2 && Dia > 28)
        Dia = vtoGiro.getDiasMes(Mes, Eje);
      if (Dia == 31)
        Dia = vtoGiro.getDiasMes(Mes, Eje);

      vtoGiro.setFecha(Formatear.format(Dia, "99") + "-" +
                       Formatear.format(Mes, "99") + "-" +
                       Formatear.format(Eje, "9999"));
      if (!vtoGiro.getError().equals(""))
        throw new Exception("ERROR AL FORMA UN VTO.  (creaFacturas) " +
                            vtoGiro.getError());
    }
    else
      vtoGiro.sumarDias(dias); //Vtos Naturales

    if (Fecha.comparaFechas(vtoGiro.getFecha("dd-MM-yyyy"), fechaMin) < 0)
      vtoGiro.sumarDias(15); //Si es menor que la minima se suma 15 dias

    vtoGiro = DiasFijosCli(vtoGiro, dia1, dia2, dia3);
    return vtoGiro.getFecha("dd-MM-yyyy");
  }

  /**
   * Adaptar el Vto a los dias Fijos de Pago.
   * @param vtoGiro  Vto., Ajustar a: dia1,dia2,dia3.
   * @param dia1 dia de Pago 1
   * @param dia2 dia de pago 2
   * @param dia3 dia de pago 3
   * @return Fecha Ajustada.
   * @throws Throwable si hay un error al formar la fecha.
   */
  private static Fecha DiasFijosCli(Fecha vtoGiro, int dia1, int dia2, int dia3) throws Exception
  {
    int Dia = 0, Mes = 0, Eje = 0, DiasMes = 0;

    if (dia1 > 0 || dia2 > 0 || dia3 > 0)
    {
      Dia = vtoGiro.getDia();
      if (Dia != dia1 && Dia != dia2 && Dia != dia3)
      {
        Mes = vtoGiro.getMes();
        Eje = vtoGiro.getYear();
        if (dia1 > Dia)
          Dia = dia1;
        else
        {
          if (dia2 > Dia)
            Dia = dia2;
          else
          {
            if (dia3 > Dia)
              Dia = dia3;
            else
            {
              if (dia1 > 0)
              {
                Dia = dia1;
                Mes++;
              }
            }
          }
        }
        if (Mes > 12)
        {
          Mes = Mes - 12;
          Eje++;
        }
        if (Mes == 2 && Dia > 28)
          Dia = vtoGiro.getDiasMes(Mes, Eje);
        if (Dia == 31)
          Dia = vtoGiro.getDiasMes(Mes, Eje);

        vtoGiro.setFecha(Formatear.format(Dia, "99") + "-" +
                         Formatear.format(Mes, "99") + "-" +
                         Formatear.format(Eje, "9999"));
        if (!vtoGiro.getError().equals(""))
          throw new Exception("ERROR AL FORMA UN VTO.  (creaFacturas) " +
                              vtoGiro.getError());
      }
    }
    return vtoGiro;
  }
}

class clFactCobTh  extends Thread
{
  clFactCob clfaco;
  public clFactCobTh(clFactCob clfaco)
  {
    this.clfaco=clfaco;
    this.start();
  }
  public void run()
  {
    clfaco.verCobros();
  }
}
