package gnu.chu.anjelica.pad;

import gnu.chu.controles.*;
import java.awt.*;
import javax.swing.border.*;
import java.sql.*;
import gnu.chu.camposdb.*;
import gnu.chu.sql.*;
import gnu.chu.utilidades.*;
import java.util.Vector;
//import gnu.chu.controles.CPanel;

/**
 *
 * <p>T�tulo: condBusq</p>
 * <p>Descripci�n: Panel con condiciones busqueda para buscar Clientes<br>
 * <p>Copyright: Copyright (c) 2006
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los t�rminos de la Licencia P�blica General de GNU seg�n es publicada por
 *  la Free Software Foundation, bien de la versi�n 2 de dicha Licencia
 *  o bien (seg�n su elecci�n) de cualquier versi�n posterior.
 *  Este programa se distribuye con la esperanza de que sea �til,
 *  pero SIN NINGUNA GARANT�A, incluso sin la garant�a MERCANTIL impl�cita
 *  o sin garantizar la CONVENIENCIA PARA UN PROP�SITO PARTICULAR.
 *  V�ase la Licencia P�blica General de GNU para m�s detalles.
 *  Deber�a haber recibido una copia de la Licencia P�blica General junto con este programa.
 *  Si no ha sido as�, escriba a la Free Software Foundation, Inc.,
 *  en 675 Mass Ave, Cambridge, MA 02139, EEUU.
 * </p>
 * @author chuchi P
 * @version 1.0
 */

public class condBusqCl extends CPanel
{
  EntornoUsuario EU;
  CLabel discrim2L = new CLabel();
  CLinkBox cli_zoncreE = new CLinkBox();
  CLabel discrim3L = new CLabel();
  CTextField fealinE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel discrim1L = new CLabel();
  CTextField fealfiE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel cLabel5 = new CLabel();
  CLinkBox cli_zonrepE = new CLinkBox();
  CLabel fealfiL = new CLabel();
  CComboBox cli_activE = new CComboBox();
  CLabel cli_disc1L = new CLabel();
  CPanel Pdiscrim = new CPanel();
  CLabel cli_disc4L = new CLabel();
  CLinkBox cli_disc4E = new CLinkBox();
  CLinkBox cli_disc3E = new CLinkBox();
  CLabel cli_disc2L = new CLabel();
  CLinkBox cli_disc1E = new CLinkBox();
  CLabel cli_disc3L = new CLabel();
  CLinkBox cli_disc2E = new CLinkBox();
  TitledBorder titledBorder1=new TitledBorder("");
  CLabel cLabel39 = new CLabel();
  CLabel cLabel36 = new CLabel();
  CLinkBox div_codiE = new CLinkBox();
  CLinkBox emp_codiE = new CLinkBox();
  CLabel cLabel1 = new CLabel();
  cliPanel cli_inicE = new cliPanel();
  CLabel cLabel2 = new CLabel();
  cliPanel cli_finE = new cliPanel();
  CComboBox cli_giroE = new CComboBox();
  CLabel cLabel18 = new CLabel();
  CLabel tar_codiL = new CLabel();
  CLinkBox tar_codiE = new CLinkBox();
  DatosTabla dtStat;
  ventana papa;

  public condBusqCl() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    fealfiL.setText("A Fecha Alta ");
    fealfiL.setBounds(new Rectangle(424, 84, 77, 18));
    cli_zonrepE.setAncTexto(30);
    cli_zonrepE.setBounds(new Rectangle(77, 42, 222, 18));
    cLabel5.setText("De Fecha Alta ");
    cLabel5.setBounds(new Rectangle(259, 81, 86, 18));
    discrim1L.setText("Zona/Repres");
    discrim1L.setBounds(new Rectangle(2, 42, 73, 18));
    discrim3L.setText("Activo");
    discrim3L.setBounds(new Rectangle(430, 139, 73, 18));
    cli_zoncreE.setAncTexto(30);
    cli_zoncreE.setBounds(new Rectangle(362, 42, 212, 18));
    discrim2L.setText("Zona/Cdto");
    discrim2L.setBounds(new Rectangle(302, 42, 65, 18));
    this.setLayout(null);
    cli_disc1L.setText("Discriminador 1.");
    cli_disc1L.setBounds(new Rectangle(9, 20, 112, 19));
    cli_disc1L.setToolTipText("");
    Pdiscrim.setBorder(titledBorder1);
    Pdiscrim.setMaximumSize(new Dimension(2147483647, 2147483647));
    Pdiscrim.setBounds(new Rectangle(12, 114, 407, 106));
    Pdiscrim.setLayout(null);
    cli_disc4L.setText("Discriminador 4");
    cli_disc4L.setBounds(new Rectangle(10, 81, 112, 19));
    cli_disc4E.setAncTexto(30);
    cli_disc4E.setBounds(new Rectangle(124, 81, 276, 19));
    cli_disc3E.setAncTexto(30);
    cli_disc3E.setBounds(new Rectangle(124, 61, 276, 19));
    cli_disc2L.setText("Discriminador 2");
    cli_disc2L.setBounds(new Rectangle(9, 40, 112, 19));
    cli_disc1E.setAncTexto(30);
    cli_disc1E.setBounds(new Rectangle(124, 20, 276, 19));
    cli_disc3L.setText("Discriminador 3");
    cli_disc3L.setBounds(new Rectangle(10, 61, 112, 19));
    cli_disc2E.setAncTexto(30);
    cli_disc2E.setBounds(new Rectangle(124, 40, 276, 19));
    titledBorder1.setTitle("Discriminadores");
    cLabel39.setText("Divisa");
    cLabel39.setBounds(new Rectangle(302, 61, 49, 18));
    cLabel39.setRequestFocusEnabled(true);
    cLabel36.setRequestFocusEnabled(true);
    cLabel36.setText("Empresa");
    cLabel36.setBounds(new Rectangle(2, 61, 54, 18));
    div_codiE.setAncTexto(30);
    div_codiE.setBounds(new Rectangle(362, 61, 212, 18));
    emp_codiE.setAncTexto(30);
    emp_codiE.setBounds(new Rectangle(77, 61, 222, 18));
    cLabel1.setText("De Cliente");
    cLabel1.setBounds(new Rectangle(2, 2, 63, 16));
    cLabel2.setText("A Cliente");
    cLabel2.setBounds(new Rectangle(2, 21, 63, 16));
    cLabel18.setText("Giro");
    cLabel18.setBounds(new Rectangle(431, 115, 32, 16));
    tar_codiL.setText("Tarifa");
    tar_codiL.setBounds(new Rectangle(2, 84, 39, 18));
    tar_codiE.setAceptaNulo(true);
    tar_codiE.setAncTexto(30);
    tar_codiE.setBounds(new Rectangle(44, 83, 212, 18));
    cli_activE.setBounds(new Rectangle(508, 137, 68, 18));
    cli_giroE.setBounds(new Rectangle(524, 113, 53, 18));
    fealfiE.setBounds(new Rectangle(503, 84, 70, 18));
    fealinE.setBounds(new Rectangle(341, 81, 70, 18));
    cli_finE.setBounds(new Rectangle(75, 19, 405, 17));
    cli_inicE.setBounds(new Rectangle(75, 0, 405, 17));
    Pdiscrim.add(cli_disc1E, null);
    Pdiscrim.add(cli_disc1L, null);
    Pdiscrim.add(cli_disc2E, null);
    Pdiscrim.add(cli_disc2L, null);
    Pdiscrim.add(cli_disc3E, null);
    Pdiscrim.add(cli_disc3L, null);
    Pdiscrim.add(cli_disc4E, null);
    Pdiscrim.add(cli_disc4L, null);
    this.add(cLabel2, null);
    this.add(tar_codiL, null);
    this.add(discrim1L, null);
    this.add(cLabel36, null);
    this.add(cLabel1, null);
    this.add(cli_inicE, null);
    this.add(cli_finE, null);
    this.add(cli_activE, null);
    this.add(discrim3L, null);
    this.add(cli_giroE, null);
    this.add(tar_codiE, null);
    this.add(fealinE, null);
    this.add(cLabel5, null);
    this.add(cLabel18, null);
    this.add(cli_zonrepE, null);
    this.add(discrim2L, null);
    this.add(cli_zoncreE, null);
    this.add(emp_codiE, null);
    this.add(div_codiE, null);
    this.add(cLabel39, null);
    this.add(fealfiE, null);
    this.add(fealfiL, null);
    this.add(Pdiscrim, null);
  }

  public void iniciar(DatosTabla dt, ventana padre) throws Exception
  {
    String s;
    EU = padre.EU;
    dtStat = dt;
    papa = padre;
    cli_inicE.iniciar(dtStat, papa, papa.vl, papa.EU);
    cli_finE.iniciar(dtStat, papa, papa.vl, papa.EU);
    cli_zoncreE.setFormato(Types.CHAR, "XX");
    pdclien.llenaDiscr(dt, cli_zoncreE, "CR", padre.EU.em_cod);
    cli_zonrepE.setFormato(Types.CHAR, "XX");
    pdclien.llenaDiscr(dt, cli_zonrepE, "CR", padre.EU.em_cod);
    llenaDiscr(cli_zonrepE, "CR");
    llenaDiscr(cli_zoncreE, "CC");

    llenaDiscr(cli_disc1E, "C1");
    llenaDiscr(cli_disc2E, "C2");
    llenaDiscr(cli_disc3E, "C3");
    llenaDiscr(cli_disc4E, "C4");

    cli_activE.addItem("Si", "S");
    cli_activE.addItem("No", "N");
    cli_giroE.addItem("Si","S");
    cli_giroE.addItem("No","N");

    s = "SELECT tar_codi,tar_nomb FROM tipotari ORDER BY tar_codi";
    dtStat.select(s);
    tar_codiE.addDatos(dtStat);
    tar_codiE.setFormato(true);
    tar_codiE.setFormato(Types.DECIMAL, "#9", 2);
    s = "SELECT div_codi,div_nomb FROM v_divisa order by div_nomb";
    dtStat.select(s);
    div_codiE.addDatos(dtStat);
    div_codiE.setFormato(Types.DECIMAL, "##9");
    div_codiE.setAceptaNulo(true);

    s = "SELECT emp_codi,emp_nomb FROM v_empresa order by emp_codi";
    dtStat.select(s);
    emp_codiE.addDatos(dtStat);
    emp_codiE.setFormato(Types.DECIMAL, "#9", 2);
    emp_codiE.setAceptaNulo(true);

    cli_disc1L.setText(pdconfig.getNombreDiscr(EU.em_cod, "C1", dtStat));
    cli_disc2L.setText(pdconfig.getNombreDiscr(EU.em_cod, "C2", dtStat));
    cli_disc3L.setText(pdconfig.getNombreDiscr(EU.em_cod, "C3", dtStat));
    cli_disc4L.setText(pdconfig.getNombreDiscr(EU.em_cod, "C4", dtStat));
    emp_codiE.setColumnaAlias("emp_codi");
    tar_codiE.setColumnaAlias("tar_codi");
    cli_disc1E.setColumnaAlias("cli_disc1");
    cli_disc2E.setColumnaAlias("cli_disc2");
    cli_disc3E.setColumnaAlias("cli_disc3");
    cli_disc4E.setColumnaAlias("cli_disc4");
    cli_zonrepE.setColumnaAlias("cli_zonrep");
    cli_zoncreE.setColumnaAlias("cli_zoncre");
    div_codiE.setColumnaAlias("div_codi");
    cli_activE.setColumnaAlias("cli_activ");
    cli_giroE.setColumnaAlias("cli_giro");
    this.setQuery(true);
    cli_inicE.setQuery(false);
    cli_finE.setQuery(false);
    fealinE.setQuery(false);
    fealfiE.setQuery(false);
  }
  private void llenaDiscr(CLinkBox lkBox,String discr) throws SQLException
  {
    pdclien.llenaDiscr(dtStat,lkBox,discr,EU.em_cod);
  }
  public Component getErrorConf()
  {
    Component c;
    if ((c=super.getErrorConf())!=null)
      return c;

    return null;
  }
  public void resetTexto()
  {
    super.resetTexto();
    cli_activE.setValor("S");
  }
  public String getStrSql()
  {
    String s="select * from clientes where 1=1 ";
    if (cli_inicE.getValorInt()>0)
      s+=" AND cli_codi >= "+cli_inicE.getValorInt();
    if (cli_finE.getValorInt()>0)
      s+=" AND cli_codi <= "+cli_finE.getValorInt();
    if (! fealinE.isNull())
      s+=" and cli_fecalt >= to_date('"+fealinE.getFecha()+",' dd-MM-yyyy')";
    if (! fealfiE.isNull())
      s+=" and cli_fecalt <= to_date('"+fealfiE.getFecha()+",' dd-MM-yyyy')";
    Vector v=new Vector();
    v.add(cli_zoncreE.getStrQuery());
    v.add(cli_zonrepE.getStrQuery());
    v.add(emp_codiE.getStrQuery());
    v.add(div_codiE.getStrQuery());
    v.add(tar_codiE.getStrQuery());
    v.add(cli_giroE.getStrQuery());
    v.add(cli_activE.getStrQuery());
    v.add(cli_disc1E.getStrQuery());
    v.add(cli_disc2E.getStrQuery());
    v.add(cli_disc3E.getStrQuery());
    v.add(cli_disc4E.getStrQuery());
    s = ventana.creaWhere(s, v, false);
    s += " ORDER BY cli_codi";
    return s;
  }
}
