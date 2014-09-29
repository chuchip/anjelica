package gnu.chu.anjelica.ventas;

import gnu.chu.camposdb.cliPanel;
import gnu.chu.camposdb.proPanel;
import gnu.chu.controles.*;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.SystemOut;
import gnu.chu.utilidades.ventana;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.sql.Types;
import javax.swing.JLayeredPane;
import javax.swing.border.TitledBorder;
/**
 *
 * <p>Título: condBusq</p>
 * <p>Descripción: Panel con condiciones búsqueda para buscar albaranes de ventas<br>
 * <p>Copyright: Copyright (c) 2005-2012
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los términos de la Licencia Pública General de GNU según es publicada por
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
 * @author chuchi P
 * @version 1.0
 */

public class condBusq extends CPanel
{
  private CComboBox div_codiE=new CComboBox();
  boolean verProd=true;
  ventana padre;
  CLabel cLabel1 = new CLabel();
  cliPanel cliIniE = new cliPanel();
  CLabel cLabel2 = new CLabel();
  cliPanel cliFinE = new cliPanel();
  CLabel cLabel11 = new CLabel();
  CTextField ejeIniE = new CTextField(Types.DECIMAL,"###9");
  CTextField ejeFinE = new CTextField(Types.DECIMAL,"###9");
  CLabel cLabel12 = new CLabel();
  CLabel cLabel6 = new CLabel();
  CLinkBox empIniE = new CLinkBox();
  CLabel cLabel3 = new CLabel();
  CLinkBox empFinE = new CLinkBox();
  CTextField serieFinE = new CTextField(Types.CHAR,"X",1);
  CTextField serieIniE = new CTextField(Types.CHAR,"X",1);
  CLabel cLabel4 = new CLabel();
  CLabel cLabel5 = new CLabel();
  CTextField albFinE = new CTextField(Types.DECIMAL,"#####9");
  CLabel cLabel8 = new CLabel();
  CTextField albIniE = new CTextField(Types.DECIMAL,"#####9");
  CLabel cLabel7 = new CLabel();
  proPanel proIniE = new proPanel();
  CLabel proIniL = new CLabel();
  CLabel proFinL = new CLabel();
  proPanel proFinE = new proPanel();
  CLabel cLabel13 = new CLabel();
  CTextField fecIniE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel14 = new CLabel();
  CTextField fecFinE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel20 = new CLabel();
  CLinkBox zon_codiE = new CLinkBox();
  CLabel cLabel15 = new CLabel();
  CComboBox cli_activE = new CComboBox();
  CLabel cLabel16 = new CLabel();
  CLinkBox rep_codiE = new CLinkBox();
  CLabel cLabel17 = new CLabel();
  CComboBox cli_giroE = new CComboBox();
  CPanel PcondCli = new CPanel();
  TitledBorder titledBorder2=new TitledBorder("");;
  CPanel PcondPro = new CPanel();
  TitledBorder titledBorder1=new TitledBorder("");
  CLabel cLabel18 = new CLabel();
  CLinkBox cam_codiE = new CLinkBox();
  CLabel cLabel19 = new CLabel();
  CComboBox pro_artconE = new CComboBox();

  public condBusq() {
    try {
      jbInit();
    }
    catch(Exception e) {
      SystemOut.print(e);
    }
  }
  private void jbInit() throws Exception {
    this.setLayout(null);

    cliIniE.setCeroIsNull(true);
    cliFinE.setCeroIsNull(true);
    ejeIniE.setCeroIsNull(true);
    ejeFinE.setCeroIsNull(true);

    cliFinE.setBounds(new Rectangle(354, 2, 225, 19));
    cLabel2.setText("A Cliente");
    cLabel2.setBounds(new Rectangle(291, 1, 56, 19));
    cliIniE.setBounds(new Rectangle(62, 2, 226, 19));
    cLabel1.setText("De Cliente");
    cLabel1.setBounds(new Rectangle(3, 2, 59, 19));
    cLabel11.setText("De Ejercicio");
    cLabel11.setBounds(new Rectangle(3, 23, 72, 17));
    cLabel12.setText("A Ejercicio");
    cLabel12.setBounds(new Rectangle(184, 23, 60, 17));
    cLabel6.setText("A Empresa");
    cLabel6.setBounds(new Rectangle(291, 41, 70, 19));
    empIniE.setAncTexto(30);
    empIniE.setBounds(new Rectangle(69, 41, 219, 19));
    cLabel3.setText("De Empresa");
    cLabel3.setBounds(new Rectangle(2, 41, 70, 19));
    empFinE.setAncTexto(30);
    empFinE.setBounds(new Rectangle(354, 41, 225, 19));
    serieFinE.setMayusc(true);
    serieFinE.setBounds(new Rectangle(266, 61, 19, 17));
    serieIniE.setMayusc(true);
    serieIniE.setBounds(new Rectangle(71, 61, 19, 17));
    cLabel4.setText("De Serie");
    cLabel4.setBounds(new Rectangle(19, 61, 47, 17));
    cLabel5.setText("A Serie");
    cLabel5.setBounds(new Rectangle(216, 61, 41, 17));
    cLabel8.setText("A Albaran");
    cLabel8.setBounds(new Rectangle(461, 61, 60, 17));
    cLabel7.setRequestFocusEnabled(true);
    cLabel7.setText("De Albaran");
    cLabel7.setBounds(new Rectangle(291, 61, 62, 17));
    proIniE.setCeroIsNull(true);
    proIniE.setBounds(new Rectangle(62, 79, 226, 18));
    proIniL.setText("De Prod.");
    proIniL.setBounds(new Rectangle(3, 79, 59, 18));
    proFinL.setText("A Prod.");
    proFinL.setBounds(new Rectangle(291, 79, 49, 18));
    proFinE.setCeroIsNull(true);
    proFinE.setBounds(new Rectangle(354, 79, 225, 18));
    cLabel13.setText("A Fecha");
    cLabel13.setBounds(new Rectangle(446, 23, 47, 17));
    cLabel13.setOpaque(true);
    cLabel14.setText("De Fecha");
    cLabel14.setBounds(new Rectangle(291, 23, 53, 17));
    cLabel20.setText("Zona");
    cLabel20.setBounds(new Rectangle(5, 17, 60, 16));
    zon_codiE.setAncTexto(30);
    zon_codiE.setBounds(new Rectangle(70, 17, 161, 18));
    cLabel15.setText("Activo");
    cLabel15.setBounds(new Rectangle(240, 17, 38, 18));
    cli_activE.setBounds(new Rectangle(279, 17, 47, 19));
    cLabel16.setText("Repres");
    cLabel16.setBounds(new Rectangle(7, 38, 65, 18));
    rep_codiE.setAncTexto(30);
    rep_codiE.setBounds(new Rectangle(70, 38, 164, 18));
    cLabel17.setText("Giro");
    cLabel17.setBounds(new Rectangle(240, 38, 38, 18));
    cli_giroE.setBounds(new Rectangle(280, 38, 46, 19));
    PcondCli.setBorder(titledBorder2);
    PcondCli.setMaximumSize(new Dimension(32767, 32767));
    PcondCli.setBounds(new Rectangle(5, 100, 336, 66));
    titledBorder2.setTitle("Discriminadores Clientes");
    CLabel div_codiL = new CLabel("Divisa");
    div_codiL.setBounds(new Rectangle(5, 80, 65, 18));
    div_codiE.setBounds(new Rectangle(71, 80, 90, 18));
    
    PcondCli.setLayout(null);
    PcondPro.setBorder(titledBorder1);
    PcondPro.setBounds(new Rectangle(348, 100, 229, 66));
    PcondPro.setLayout(null);
    titledBorder1.setTitle("Discrim. Productos");
    cLabel18.setText("Camara");
    cLabel18.setBounds(new Rectangle(4, 17, 50, 18));
    cam_codiE.setAncTexto(30);
    cam_codiE.setBounds(new Rectangle(50, 17, 172, 18));
    cLabel19.setText("Congelado");
    cLabel19.setBounds(new Rectangle(31, 39, 72, 18));
    pro_artconE.setBounds(new Rectangle(107, 39, 115, 18));


    albFinE.setBounds(new Rectangle(524, 61, 55, 17));
    albIniE.setBounds(new Rectangle(355, 61, 55, 17));
    fecFinE.setBounds(new Rectangle(496, 23, 81, 17));
    fecIniE.setBounds(new Rectangle(354, 23, 81, 17));
    ejeFinE.setBounds(new Rectangle(247, 23, 41, 17));
    ejeIniE.setBounds(new Rectangle(70, 23, 41, 17));
    this.add(cliFinE, null);
    this.add(cLabel1, null);
    this.add(cliIniE, null);
    this.add(cLabel2, null);
    this.add(cLabel11, null);
    this.add(empFinE, null);
    this.add(cLabel6, null);
    this.add(cLabel3, null);
    this.add(cLabel4, null);
    this.add(albIniE, null);
    this.add(cLabel7, null);
    this.add(albFinE, null);
    this.add(cLabel8, null);
    this.add(proFinL, null);
    this.add(proIniL, null);
    this.add(proFinE, null);
    this.add(ejeFinE, null);
    this.add(cLabel12, null);
    this.add(cLabel14, null);
    this.add(fecFinE, null);
    this.add(cLabel13, null);
    this.add(fecIniE, null);
    this.add(div_codiL,null);
    this.add(div_codiE,null);
    PcondCli.add(zon_codiE, null);
    PcondCli.add(cLabel20, null);
    PcondCli.add(cli_giroE, null);
    PcondCli.add(cli_activE, null);
    PcondCli.add(rep_codiE, null);
    PcondCli.add(cLabel16, null);
    PcondCli.add(cLabel17, null);
    PcondCli.add(cLabel15, null);
    this.add(PcondPro, null);
    PcondPro.add(cam_codiE, null);
    PcondPro.add(pro_artconE, null);
    PcondPro.add(cLabel18, null);
    PcondPro.add(cLabel19, null);
    this.add(ejeIniE, null);
    this.add(empIniE, null);
    this.add(serieIniE, null);
    this.add(serieFinE, null);
    this.add(cLabel5, null);
    this.add(proIniE, null);
    this.add(PcondCli, null);
  }
  public void iniciar(DatosTabla dt,ventana papa,JLayeredPane layPan,EntornoUsuario eu) throws Exception
  {
    padre=papa;
    cliIniE.iniciar(dt,padre,layPan,eu);
    cliFinE.iniciar(dt,padre,layPan,eu);

    String s="SELECT emp_codi,emp_nomb FROM v_empresa ORDER BY emp_codi";

    empIniE.setFormato(true);
    empIniE.setFormato(Types.DECIMAL,"#9",2);
    dt.select(s);
    empIniE.addDatos(dt);

    empFinE.setFormato(true);
    empFinE.setFormato(Types.DECIMAL, "#9", 2);
    dt.select(s);
    empFinE.addDatos(dt);

    proIniE.iniciar(dt,padre,layPan,eu);
    proFinE.iniciar(dt,padre,layPan,eu);

    zon_codiE.setFormato(Types.CHAR, "XX", 2);
    zon_codiE.texto.setMayusc(true);

    gnu.chu.anjelica.pad.pdconfig.llenaDiscr(dt,zon_codiE,"Cz",eu.em_cod);
    zon_codiE.addDatos("**","TODOS");


    rep_codiE.setFormato(Types.CHAR, "XX", 2);
    rep_codiE.texto.setMayusc(true);
    gnu.chu.anjelica.pad.MantRepres.llenaLinkBox(rep_codiE,dt);

    rep_codiE.addDatos("**","TODOS");

    cli_activE.addItem("Todos","%");
    cli_activE.addItem("Si","S");
    cli_activE.addItem("No","N");
//    cli_activE.setModificable(true);

    cli_giroE.addItem("Todos", "%");
    cli_giroE.addItem("Si", "S");
    cli_giroE.addItem("No", "N");
//    cli_giroE.setModificable(true);

    cam_codiE.setFormato(Types.CHAR, "XX", 2);
    cam_codiE.texto.setMayusc(true);
    gnu.chu.anjelica.pad.pdconfig.llenaDiscr(dt,cam_codiE,"AC",eu.em_cod);
    cam_codiE.addDatos("**","TODOS");
    pro_artconE.addItem("Todos","*");
    pro_artconE.addItem("Fresco","F");
    pro_artconE.addItem("Congelado","C");
    
    div_codiE.addItem("**","TODAS");
    s="SELECT div_codi,div_nomb FROM v_divisa ORDER BY div_nomb";
    dt.select(s);
    div_codiE.addItem(dt,false);
    if (eu.getUsuReser1().equals("S"))
      div_codiE.addItem("-------","0");
    div_codiE.setColumnaAlias("div_codi");
  }
  boolean checkCampos() throws java.text.ParseException
  {
    if (cliFinE.getValorInt()<cliIniE.getValorInt())
    {
      padre.mensajeErr("Codigo Final de Cliente Incorrecto");
      cliFinE.setValorInt(cliIniE.getValorInt());
      cliFinE.requestFocus();
      return false;
    }
    if (ejeFinE.getValorInt()<ejeIniE.getValorInt())
    {
      padre.mensajeErr("Codigo Final de Ejercicio Incorrecto");
      ejeFinE.setValorDec(ejeIniE.getValorInt());
      ejeFinE.requestFocus();
      return false;
    }
    if (fecIniE.getError())
      return false;
    if (fecFinE.getError())
      return false;
    if (Formatear.comparaFechas(fecIniE.getDate(true), fecFinE.getDate(true)) > 0)
    {
      padre.mensajeErr("Fecha Final NO puede ser Inferior a Fecha Inicial");
      fecFinE.setDate(fecIniE.getDate());
      fecFinE.requestFocus();
      return false;
    }
    if (empFinE.getValorInt() < empIniE.getValorInt())
    {
      padre.mensajeErr("Empresa Final NO puede ser inferior a la Inicial");
      empFinE.setValorInt(empIniE.getValorInt());
      empFinE.requestFocus();
      return false;
    }
    if (albFinE.getValorInt() < albIniE.getValorInt())
    {
      padre.mensajeErr("Albaran Final NO puede ser inferior al Inicial");
      albFinE.setValorInt(albIniE.getValorInt());
      albFinE.requestFocus();
      return false;
    }
    if (proFinE.getValorInt() < proIniE.getValorInt())
    {
      padre.mensajeErr("Producto Final NO puede ser inferior al Inicial");
      proFinE.setValorInt(proIniE.getValorInt());
      proFinE.requestFocus();
      return false;
    }
    return true;
  }

  String getCondWhere(EntornoUsuario eu)
  {
    String condWhere="";
    if (!cliIniE.isNull())
      condWhere += " and cl.cli_codi >= " + cliIniE.getValorInt();
    if (!cliFinE.isNull())
      condWhere += " and cl.cli_codi <= " + cliFinE.getValorInt();
    if (ejeIniE.getValorInt()>0)
      condWhere += " and a.avc_ano >= " + ejeIniE.getValorInt();
    if (ejeFinE.getValorInt()>0)
      condWhere += " and a.avc_ano <= " + ejeFinE.getValorInt();
    if (! fecIniE.isNull())
      condWhere+=" and a.avc_fecalb >= TO_DATE('"+fecIniE.getText()+"','dd-MM-yyyy')";
    if (! fecFinE.isNull())
      condWhere+=" and a.avc_fecalb <= TO_DATE('"+fecFinE.getText()+"','dd-MM-yyyy')";
    if (empIniE.getValorInt()>0)
      condWhere += " and a.emp_codi >= " + empIniE.getValorInt();
    if (empFinE.getValorInt()>0)
      condWhere += " and a.emp_codi <= " + empFinE.getValorInt();

    if (!serieIniE.isNull(true))
      condWhere += " and a.avc_serie >= '" + serieIniE.getText()+"'";
    if (! serieFinE.isNull(true))
      condWhere +=  " and a.avc_serie <= '" + serieFinE.getText()+"'";

    if (albIniE.getValorInt()>0)
      condWhere += " and a.avc_nume >= " + albIniE.getValorInt();
    if (albFinE.getValorInt()>0)
      condWhere += " and a.avc_nume <= " + albFinE.getValorInt();
    if (verProd)
    {
      if (proIniE.getValorInt() > 0)
        condWhere += " and l.pro_codi >= " + proIniE.getValorInt();
      if (proFinE.getValorInt() > 0)
        condWhere += " and l.pro_codi <= " + proFinE.getValorInt();
      if (! cam_codiE.isNull(true) && !cam_codiE.getText().equals("**") && !cam_codiE.getText().equals("*"))
        condWhere+=" and p.cam_codi  LIKE '"+Formatear.reemplazar(cam_codiE.getText(),"*","%")+"'";
      if ( !pro_artconE.getValor().equals("*") )
        condWhere+=" and p.pro_artcon  "+
                (pro_artconE.getValor().equals("C")?"!=0":"=0");
    }
    if (! zon_codiE.isNull(true) && !zon_codiE.getText().equals("**") && !zon_codiE.getText().equals("*"))
      condWhere+=" and cl.zon_codi  LIKE '"+Formatear.reemplazar(zon_codiE.getText(),"*","%")+"'";
    if (! rep_codiE.isNull(true) && !rep_codiE.getText().equals("**") && !rep_codiE.getText().equals("*"))
      condWhere+=" and cl.rep_codi  LIKE '"+Formatear.reemplazar(rep_codiE.getText(),"*","%")+"'";
    if (! div_codiE.isNull(true) && !div_codiE.getText().equals("**") && !div_codiE.getText().equals("*"))
        condWhere+=" and a.div_codi="+div_codiE.getValor();
    if (!cli_activE.getValor().equals("%"))
      condWhere+=" and cl.cli_activ = '"+cli_activE.getValor()+"'";
    if (!cli_giroE.getValor().equals("%"))
      condWhere+=" and cl.cli_giro = '"+cli_giroE.getValor()+"'";
    condWhere+=(eu.isRootAV()?"":" and a.div_codi > 0 ");
    return condWhere;
  }
    @Override
  public void requestFocus()
  {
    super.requestFocus();
    if (cliIniE!=null)
      cliIniE.requestFocus();
  }

  public void setVerProd(boolean verProd)
  {
    this.verProd = verProd;
    PcondPro.setVisible(verProd);
    proIniE.setVisible(verProd);
    proFinE.setVisible(verProd);
    proIniL.setVisible(verProd);
    proFinL.setVisible(verProd);
  }

}
