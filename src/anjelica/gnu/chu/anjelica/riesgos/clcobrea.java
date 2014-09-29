package gnu.chu.anjelica.riesgos;

import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import gnu.chu.Menu.*;
import java.awt.*;
import javax.swing.BorderFactory;
import gnu.chu.camposdb.*;
import java.sql.*;
import java.awt.event.*;
import java.util.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.*;
/**
 *
 * <p>Titulo: clcobrea</p>
 * <p>Descripción: Consulta/Listado Cobros realizados</p>
 * <p>Copyright: Copyright (c) 2005-2012
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
public class clcobrea extends ventana
{
  String s;
  CPanel Pprinc = new CPanel();
  CPanel PCabe = new CPanel();
  CLabel cLabel8 = new CLabel();
  CLabel cLabel1 = new CLabel();
  CLabel cLabel3 = new CLabel();
  CLabel cLabel10 = new CLabel();
  CTextField fvc_numeE1 = new CTextField(Types.DECIMAL,"#####9");
  CTextField serieFinE = new CTextField(Types.CHAR,"X",1);
  CTextField serieIniE = new CTextField(Types.CHAR,"X",1);
  CButton Baceptar = new CButton("Aceptar",Iconos.getImageIcon("check"));
  CTextField eje_numeE = new CTextField(Types.DECIMAL,"###9");
  CLabel cLabel9 = new CLabel();
  CLinkBox empFinE = new CLinkBox();
  CLinkBox empIniE = new CLinkBox();
  CTextField feciniE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel4 = new CLabel();
  CTextField fvc_numeE = new CTextField(Types.DECIMAL,"#####9");
  CLabel cLabel7 = new CLabel();
  CLabel cLabel2 = new CLabel();
  cliPanel cli_codiE1 = new cliPanel();
  CLinkBox zon_codiE1 = new CLinkBox();
  CTextField fecfinE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLinkBox zon_codiE = new CLinkBox();
  CLabel cLabel6 = new CLabel();
  CLabel cLabel21 = new CLabel();
  cliPanel cli_codiE = new cliPanel();
  CLabel cLabel20 = new CLabel();
  CTextField eje_numeE1 = new CTextField(Types.DECIMAL,"###9");
  CLabel cLabel5 = new CLabel();
  CLabel cLabel11 = new CLabel();
  CLabel cLabel12 = new CLabel();
  Cgrid jt = new Cgrid(11);
  CLinkBox tpc_codiE = new CLinkBox();
  CLabel cLabel13 = new CLabel();
  CLabel cLabel14 = new CLabel();
  CLinkBox tpc_codiE1 = new CLinkBox();
  CButton Blistar = new CButton("Listar",Iconos.getImageIcon("print"));
  CPanel cPanel1 = new CPanel();
  CLabel cLabel15 = new CLabel();
  CTextField numdocE = new CTextField(Types.DECIMAL,"###9");
  CLabel cLabel16 = new CLabel();
  CTextField impdocE = new CTextField(Types.DECIMAL,"-,---,--9.99");
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CCheckBox opTotFecha = new CCheckBox("S","N");

  public clcobrea(EntornoUsuario eu, Principal p)
{
  EU = eu;
  vl = p.panel1;
  jf = p;
  eje = true;

  setTitulo("Cons./List. Cobros Realizados");

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

public clcobrea(gnu.chu.anjelica.menu p, EntornoUsuario eu)
{
  EU = eu;
  vl = p.getLayeredPane();
  setTitulo("Cons./List. Cobros Realizados");
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
  this.setSize(new Dimension(750, 530));
  this.setVersion("2012-04-19");
  conecta();
  statusBar = new StatusBar(this);

    Pprinc.setLayout(gridBagLayout1);
    PCabe.setBorder(BorderFactory.createRaisedBevelBorder());
    PCabe.setMaximumSize(new Dimension(588, 160));
    PCabe.setMinimumSize(new Dimension(588, 160));
    PCabe.setPreferredSize(new Dimension(588, 160));
    PCabe.setLayout(null);
    cLabel8.setText("A Factura");
    cLabel8.setBounds(new Rectangle(463, 20, 60, 16));
    cLabel1.setText("De Cliente");
    cLabel1.setBounds(new Rectangle(7, 76, 59, 19));
    cLabel3.setText("De Empresa");
    cLabel3.setBounds(new Rectangle(7, 37, 70, 19));
    cLabel10.setOpaque(true);
    cLabel10.setText("A Fecha");
    cLabel10.setBounds(new Rectangle(145, 3, 47, 17));
    Baceptar.setBounds(new Rectangle(13, 118, 149, 24));
    Baceptar.setText("Consultar (F4)");
    cLabel9.setText("De Fecha");
    cLabel9.setBounds(new Rectangle(7, 3, 53, 17));
    empFinE.setAncTexto(30);
    empFinE.setBounds(new Rectangle(365, 37, 218, 19));
    empIniE.setAncTexto(30);
    empIniE.setBounds(new Rectangle(91, 37, 201, 19));
    cLabel4.setText("De Serie");
    cLabel4.setBounds(new Rectangle(7, 20, 47, 16));
    cLabel7.setText("De Factura");
    cLabel7.setBounds(new Rectangle(295, 20, 62, 16));
    cLabel2.setText("A Cliente");
    cLabel2.setBounds(new Rectangle(295, 75, 56, 19));
    zon_codiE1.setAncTexto(30);
    zon_codiE1.setBounds(new Rectangle(365, 57, 218, 18));
    zon_codiE.setAncTexto(30);
    zon_codiE.setBounds(new Rectangle(91, 57, 200, 18));
    cLabel6.setText("A Empresa");
    cLabel6.setBounds(new Rectangle(295, 37, 70, 19));
    cLabel21.setText("A Zona/Rep");
    cLabel21.setBounds(new Rectangle(295, 57, 69, 16));
    cLabel20.setText("De Zona/Rep");
    cLabel20.setBounds(new Rectangle(7, 57, 78, 16));
    cLabel5.setText("A Serie");
    cLabel5.setBounds(new Rectangle(145, 20, 41, 16));
    cLabel11.setText("De Ejercicio");
    cLabel11.setBounds(new Rectangle(295, 3, 72, 17));
    cLabel12.setText("A Ejercicio");
    cLabel12.setBounds(new Rectangle(463, 3, 60, 17));
    tpc_codiE.setAceptaNulo(false);
    tpc_codiE.setCeroIsNull(true);
    tpc_codiE1.setCeroIsNull(true);
    cli_codiE.setCeroIsNull(true);
    cli_codiE1.setCeroIsNull(true);
    tpc_codiE.setAncTexto(30);
    tpc_codiE.setBounds(new Rectangle(65, 97, 227, 19));
    cLabel13.setText("T. Cobro");
    cLabel13.setBounds(new Rectangle(4, 97, 58, 18));
    cLabel14.setText("T. Cobro");
    cLabel14.setBounds(new Rectangle(295, 97, 58, 18));
    tpc_codiE1.setAncTexto(30);
    tpc_codiE1.setBounds(new Rectangle(350, 97, 233, 19));
    tpc_codiE1.setAceptaNulo(false);
    Vector v = new Vector();
    Blistar.setBounds(new Rectangle(201, 118, 116, 24));
    Blistar.setMaximumSize(new Dimension(69, 24));
    Blistar.setMinimumSize(new Dimension(69, 24));
    Blistar.setPreferredSize(new Dimension(116, 24));
    Blistar.setText("Listar");
    cPanel1.setBorder(BorderFactory.createLoweredBevelBorder());
    cPanel1.setBounds(new Rectangle(335, 117, 244, 24));
    cPanel1.setLayout(null);
    cLabel15.setText("N.Doc");
    cLabel15.setBounds(new Rectangle(9, 4, 40, 18));
    cLabel16.setText("Importe");
    cLabel16.setBounds(new Rectangle(96, 4, 47, 18));
    jt.setMaximumSize(new Dimension(578, 166));
    jt.setMinimumSize(new Dimension(578, 166));
    jt.setPreferredSize(new Dimension(578, 166));
    cli_codiE1.setBounds(new Rectangle(358, 76, 225, 19));
    cli_codiE.setBounds(new Rectangle(66, 76, 226, 19));
    fvc_numeE1.setBounds(new Rectangle(528, 20, 55, 16));
    fvc_numeE.setBounds(new Rectangle(362, 20, 55, 16));
    serieFinE.setBounds(new Rectangle(195, 20, 19, 16));
    serieIniE.setBounds(new Rectangle(59, 20, 19, 16));
    eje_numeE1.setBounds(new Rectangle(542, 3, 41, 17));
    eje_numeE.setBounds(new Rectangle(362, 3, 41, 17));
    fecfinE.setBounds(new Rectangle(195, 3, 81, 17));
    feciniE.setBounds(new Rectangle(59, 3, 81, 17));
    impdocE.setBounds(new Rectangle(143, 4, 81, 18));
    numdocE.setBounds(new Rectangle(45, 4, 42, 18));
    opTotFecha.setText("Total p/Fecha");
    opTotFecha.setBounds(new Rectangle(203, 145, 112, 12));
    v.add("Cliente"); // 0
    v.add("Nombre Cliente"); // 1
    v.add("Fact."); // 2
    v.add("Imp.Fra."); // 3
    v.add("Fec.Fr/Al"); // 4
    v.add("Imp.Cob."); // 5
    v.add("Fec.Cob"); // 6
    v.add("Fec.Vto"); // 7
    v.add("T.Cobro"); // 8
    v.add("Usuario"); // 9
    v.add("Coment"); // 10
    jt.setCabecera(v);
    jt.setAlinearColumna(new int[]
                       {0, 0, 0, 2, 1, 2, 1, 1, 0,0,0});
    jt.setAnchoColumna(new int[]
                     {51, 120, 105, 62, 79, 67, 72, 72, 139,50,120});
    jt.setFormatoColumna(5,"---,--9.99");
    jt.setFormatoColumna(3,"---,--9.99");
    jt.setAjustarGrid(true);
    jt.setAjustarColumnas(false);
    numdocE.setEnabled(false);
    impdocE.setEnabled(false);
    serieIniE.setMayusc(true);
    serieFinE.setMayusc(true);
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
    Pprinc.add(PCabe,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    PCabe.add(Baceptar, null);
    cPanel1.add(cLabel15, null);
    cPanel1.add(numdocE, null);
    cPanel1.add(cLabel16, null);
    cPanel1.add(impdocE, null);
    PCabe.add(Blistar, null);
    PCabe.add(opTotFecha, null);
    PCabe.add(cLabel10, null);
    PCabe.add(cLabel9, null);
    PCabe.add(feciniE, null);
    PCabe.add(eje_numeE1, null);
    PCabe.add(cLabel12, null);
    PCabe.add(eje_numeE, null);
    PCabe.add(cLabel11, null);
    PCabe.add(fecfinE, null);
    PCabe.add(cLabel8, null);
    PCabe.add(fvc_numeE1, null);
    PCabe.add(cLabel4, null);
    PCabe.add(serieIniE, null);
    PCabe.add(cLabel5, null);
    PCabe.add(serieFinE, null);
    PCabe.add(cLabel7, null);
    PCabe.add(fvc_numeE, null);
    PCabe.add(cLabel3, null);
    PCabe.add(empIniE, null);
    PCabe.add(empFinE, null);
    PCabe.add(cLabel6, null);
    PCabe.add(cLabel20, null);
    PCabe.add(zon_codiE, null);
    PCabe.add(cLabel21, null);
    PCabe.add(zon_codiE1, null);
    PCabe.add(cli_codiE1, null);
    PCabe.add(cLabel1, null);
    PCabe.add(cli_codiE, null);
    PCabe.add(cLabel2, null);
    PCabe.add(cLabel13, null);
    PCabe.add(tpc_codiE, null);
    PCabe.add(cLabel14, null);
    PCabe.add(tpc_codiE1, null);
    PCabe.add(cPanel1, null);
    Pprinc.add(jt,   new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
}
    @Override
  public void iniciarVentana() throws Exception
  {
    PCabe.setButton(KeyEvent.VK_F4,Baceptar);

    cli_codiE.iniciar(dtStat, this, vl, EU);
    cli_codiE1.iniciar(dtStat, this, vl, EU);

    zon_codiE.setFormato(Types.CHAR, "XX", 2);
    zon_codiE.texto.setMayusc(true);
    zon_codiE1.setFormato(Types.CHAR, "XX", 2);
    zon_codiE1.texto.setMayusc(true);

    gnu.chu.anjelica.pad.pdconfig.llenaDiscr(dtCon1, zon_codiE, "CR", EU.em_cod);
    gnu.chu.anjelica.pad.pdconfig.llenaDiscr(dtCon1, zon_codiE1, "CR", EU.em_cod);

    empIniE.setFormato(true);
    empIniE.setFormato(true);
    empIniE.setFormato(Types.DECIMAL, "#9", 2);
    empFinE.setFormato(true);
    empFinE.setFormato(Types.DECIMAL, "#9", 2);

    s = "SELECT emp_codi,emp_nomb FROM v_empresa ORDER BY emp_codi";
    dtCon1.select(s);
    empIniE.addDatos(dtCon1);
    dtCon1.first();
    empIniE.setText(dtCon1.getString("emp_codi"));
    empFinE.addDatos(dtCon1);
    dtCon1.last();
    empFinE.setText(dtCon1.getString("emp_codi"));

    tpc_codiE.setFormato(Types.DECIMAL, "#9", 2);
    tpc_codiE.setFormato(true);
    tpc_codiE1.setFormato(Types.DECIMAL, "#9", 2);
    tpc_codiE1.setFormato(true);

    s = "SELECT tpc_codi,tpc_nomb FROM v_cobtipo ORDER BY tpc_codi";
    dtStat.select(s);
    tpc_codiE.addDatos(dtStat);
    dtStat.first();
    tpc_codiE1.addDatos(dtStat);

    eje_numeE.setValorDec(EU.ejercicio);
    eje_numeE1.setValorDec(EU.ejercicio);
    activarEventos();
    fecfinE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
    feciniE.setText(Fecha.Suma(Formatear.getFechaAct("dd-MM-yyyy"),"dd-MM-yyyy",-2));
    feciniE.requestFocus();
  }

  void activarEventos()
  {
    Baceptar.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Baceptar_actionPerformed();
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
 void Blistar_actionPerformed()
 {
   try
    {
      s = getStrSelect();
      dtCon1.setStrSelect(s);
      ResultSet rs = ct.createStatement().executeQuery(dtCon1.getStrSelect());

      JasperReport jr = gnu.chu.print.util.getJasperReport(EU,"cobrealiz");
      java.util.HashMap mp = new java.util.HashMap();
      mp.put("fecact", Formatear.getDateAct());

      String cond="";
      if (! serieIniE.isNull(true))
        mp.put("serieIni", serieIniE.getText());
      if (! serieFinE.isNull(true))
        mp.put("serieFin", serieFinE.getText());

      if (eje_numeE.getValorDec() != 0)
        mp.put("ejeIni", eje_numeE.getText());
      if (eje_numeE1.getValorDec() != 0)
        mp.put("ejeFin", eje_numeE1.getText());
      if (!feciniE.isNull())
        mp.put("fecIni",feciniE.getText());
      if (!fecfinE.isNull())
        mp.put("fecFin",fecfinE.getText());
      if (fvc_numeE.getValorDec()>0)
          mp.put("facIni",fvc_numeE.getText());
        if (fvc_numeE1.getValorDec()>0)
          mp.put("facFin",fvc_numeE1.getText());
        if (! tpc_codiE.isNull())
          mp.put("tpcIni",tpc_codiE.getTextCombo());
        if (! tpc_codiE1.isNull())
          mp.put("tpcFin",tpc_codiE1.getTextCombo());

        if (cli_codiE.getValorInt()>0)
          mp.put("cliIni",cli_codiE.getTextNomb());
        if (cli_codiE1.getValorInt()>0)
          mp.put("cliFin",cli_codiE1.getTextNomb());
        if (! zon_codiE.isNull())
          mp.put("zonIni",zon_codiE.getTextCombo());
        if (! zon_codiE1.isNull())
          mp.put("zonFin",zon_codiE1.getTextCombo());

      mp.put("totFecha",opTotFecha.getSelecion());

      JasperPrint jp = JasperFillManager.fillReport(jr, mp,
          new JRResultSetDataSource(rs));
      gnu.chu.print.util.printJasper(jp, EU);
    }
    catch (Exception k)
    {
      Error("Error al generar Listado", k);
      return;
    }

 }
  void Baceptar_actionPerformed()
  {

    s=getStrSelect();
//    debug (s);
    jt.removeAllDatos();
    numdocE.resetTexto();
    impdocE.resetTexto();
    try
    {
      if (!dtCon1.select(s))
      {
        mensajeErr("No encontrados COBROS para estos criterios");
        feciniE.requestFocus();
        return;
      }
    }
    catch (Exception k)
    {
      Error("Error al realizar consulta cobros", k);
      return;
    }
    new clcobreaTh(this);
  }

  private String getStrSelect()
  {
    String cond="";
    if (empFinE.getText().trim().equals(""))
      empFinE.setText("99");
    if (! serieIniE.isNull(true))
    cond+=" and co.cob_serie >= '" + serieIniE.getText() + "'" ;
    if (! serieFinE.isNull(true))
      cond+=" and co.cob_serie <= '" + serieFinE.getText() + "'" ;

    if (eje_numeE.getValorDec()!=0)
      cond+= " AND co.cob_ano >= "+eje_numeE.getValorInt();
    if (eje_numeE1.getValorDec()!=0)
      cond+= " AND co.cob_ano <= "+eje_numeE1.getValorInt();
    if (!feciniE.isNull())
      cond+=" and co.cob_feccob >= TO_DATE('"+feciniE.getText()+"','dd-MM-yyyy')";
    if (!fecfinE.isNull())
      cond+=" and co.cob_feccob <= TO_DATE('"+fecfinE.getText()+"','dd-MM-yyyy')";

    if (fvc_numeE.getValorDec()>0)
      cond+=" AND co.fac_nume >= "+fvc_numeE.getValorInt();
    if (fvc_numeE1.getValorDec()>0)
      cond+=" AND co.fac_nume <= "+fvc_numeE1.getValorInt();
    if (! tpc_codiE.isNull())
      cond+=" AND co.tpc_codi >= "+tpc_codiE.getValorInt();
    if (! tpc_codiE1.isNull())
      cond+=" AND co.tpc_codi <= "+tpc_codiE1.getValorInt();
    if (cli_codiE.getValorInt()>0)
      cond+=" and f.cli_codi >= "+cli_codiE.getValorInt();
    if (cli_codiE1.getValorInt()>0)
      cond+=" and f.cli_codi <= "+cli_codiE1.getValorInt();
    if (! zon_codiE.isNull())
      cond+=" and cl.cli_zonrep >= '"+zon_codiE.getText()+"'";
    if (! zon_codiE1.isNull())
      cond+=" and cl.cli_zonrep <= '"+zon_codiE1.getText()+"'";

    s = "SELECT co.cob_feccob, co.cob_fecvto,co.cob_impor, co.cob_serie, fac_nume as doc,co.cob_anofac,co.usu_nomb, co.cob_obser"+
        ", cl.cli_codi,cl.cli_nomb,f.fvc_fecfra,f.fvc_sumtot,ct.tpc_nomb,co.emp_codi "+
        " FROM v_cobros as co,v_facvec f,clientes cl,v_cobtipo as ct  " +
        "  WHERE co.emp_codi >= " + empIniE.getValorInt() +
        " and co.emp_codi <= " + empFinE.getValorInt() +
        " and co.alb_nume = 0 "+
        " and co.tpc_codi = ct.tpc_codi " +
        " and f.fvc_nume=co.fac_nume " +
        " and f.fvc_ano = co.cob_anofac " +
        " and f.fvc_serie = co.fvc_serie "+
        " and f.emp_codi = co.emp_codi " +
        " and f.cli_codi = cl.cli_codi " + cond +
        " union " +
        "SELECT co.cob_feccob, co.cob_fecvto, co.cob_impor,cob_serie, alb_nume as doc,cob_anofac,co.usu_nomb, co.cob_obser "+
        " ,cl.cli_codi,cl.cli_nomb, avc_fecalb as fvc_fecfra,avc_impalb as fvc_sumtot,ct.tpc_nomb,co.emp_codi "+
        " FROM v_cobros as co,v_albavec f,clientes cl, v_cobtipo as ct " +
        "  WHERE co.emp_codi >= " + empIniE.getValorInt() +
        " and co.emp_codi <= " + empFinE.getValorInt() +
        (EU.isRootAV()?"":" AND f.div_codi > 0 ")+
        " and co.tpc_codi = ct.tpc_codi " +
        " and co.alb_nume > 0 "+
        " and f.avc_nume=co.alb_nume " +
        " and f.avc_ano = co.cob_anofac " +
        " and f.emp_codi = co.emp_codi " +
        " and f.cli_codi = cl.cli_codi " +
        " and f.avc_serie = co.cob_serie " + cond+
        " order by 1, 9"; // Fecha Cobro,Cliente
//    debug(s);
    return s;
  }

  void llenaGrid()
  {
    try
    {
      mensaje("Esperate, tio  ... estoy buscando cobros");
      int nFras = 0;
      double impCobr = 0;
      jt.panelG.setVisible(false);
      do
      {
        mensaje("Tratando Fecha: " + dtCon1.getFecha("cob_feccob", "dd-MM-yyyy"), false);
        ArrayList v = new ArrayList();
        v.add(dtCon1.getString("cli_codi"));
        v.add(dtCon1.getString("cli_nomb"));
        v.add(Formatear.format(dtCon1.getInt("emp_codi"),"99")+"-"+dtCon1.getString("cob_serie")+"/"+dtCon1.getString("cob_anofac") + "-" +
                     dtCon1.getString("doc"));
        v.add(dtCon1.getString("fvc_sumtot"));
        v.add(dtCon1.getFecha("fvc_fecfra", "dd-MM-yyyy"));
        v.add(dtCon1.getString("cob_impor"));
        v.add(dtCon1.getFecha("cob_feccob", "dd-MM-yyyy"));
        v.add(dtCon1.getFecha("cob_fecvto", "dd-MM-yyyy"));
        v.add(dtCon1.getString("tpc_nomb"));
        v.add(dtCon1.getString("usu_nomb"));
        v.add(dtCon1.getString("cob_obser"));
        nFras++;
        impCobr += dtCon1.getDouble("cob_impor");
        jt.addLinea(v);
      }  while (dtCon1.next());
      numdocE.setValorDec(nFras) ;
      impdocE.setValorDec(impCobr);

      jt.panelG.setVisible(true);
      jt.requestFocusInicio();
      mensaje("");
      mensajeErr("Consulta ... Realizada");
    }
    catch (Exception k)
    {
      Error("Error al ver cobros Realizados", k);
    }
  }
}

class clcobreaTh extends Thread
{
  clcobrea clcorea;

  public clcobreaTh(clcobrea clcorea)
  {
    this.clcorea=clcorea;
    this.start();
  }
    @Override
  public void run()
  {
    clcorea.setEnabled(false);
    clcorea.llenaGrid();
    clcorea.setEnabled(true);
  }
}


