package gnu.chu.anjelica.facturacion;
/**
 *
 * <p>Título: Panel para condiciones busqueda de Fras ventas</p>
 * <p>Descripción: Panel para condiciones busqueda de Fras ventas
 *  </p>
 * <p>Copyright: Copyright (c) 2005-2009
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los términos de la Licencia Pública General de GNU segun es publicada por
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
 * @author chuchi P.
 * @version 1.0
 */
import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.utilidades.*;
import gnu.chu.controles.*;
import java.awt.*;
import java.sql.*;
import gnu.chu.camposdb.*;

import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.border.*;
import gnu.chu.sql.*;
import javax.swing.JLayeredPane;

public class condBusFra extends CPanel
{
  ventana padre;
  String s;
  CPanel Pprinc = new CPanel();
  CTextField feciniE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel1 = new CLabel();
  CLabel cLabel2 = new CLabel();
  CTextField fecfinE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel3 = new CLabel();
  CLinkBox empIniE = new CLinkBox();
  CLinkBox empFinE = new CLinkBox();
  CLabel cLabel6 = new CLabel();
  CLabel cLabel7 = new CLabel();
  CTextField fvc_anoE = new CTextField(Types.DECIMAL,"###9");
  CTextField fvc_numeE = new CTextField(Types.DECIMAL,"#####9");
  CTextField fvc_anoE1 = new CTextField(Types.DECIMAL,"###9");
  CTextField fvc_numeE1 = new CTextField(Types.DECIMAL,"#####9");
  CLabel cLabel8 = new CLabel();
  CLabel cLabel9 = new CLabel();
  CLabel cLabel10 = new CLabel();
  cliPanel cliIniE = new cliPanel();
  CLabel cLabel12 = new CLabel();
  CLabel cLabel13 = new CLabel();
  cliPanel cliFinE = new cliPanel();
  CLabel cLabel15 = new CLabel();
  CComboBox cli_giroE = new CComboBox();
  CLinkBox cli_zoncreE = new CLinkBox();
  CPanel PcondCli = new CPanel();
  CLinkBox cli_zonrepE = new CLinkBox();
  CLabel cLabel16 = new CLabel();
  CLabel cLabel21 = new CLabel();
  CLabel cLabel17 = new CLabel();
  CComboBox cli_activE = new CComboBox();
  TitledBorder titledBorder3;
  private CTextField fvc_serieE = new CTextField(Types.CHAR,"X",1);
  private CTextField fvc_serieE1 = new CTextField(Types.CHAR,"X",1);

    public condBusFra() {
    try  {
      jbInit();
    }
    catch (Exception e) {
      SystemOut.print(e);
    }
  }

  private void jbInit() throws Exception
  {
    titledBorder3 = new TitledBorder("");
    Pprinc.setLayout(null);
    feciniE.setBounds(new Rectangle(70, 3, 81, 16));
    cLabel1.setRequestFocusEnabled(true);
    cLabel1.setText("De Fecha");
    cLabel1.setBounds(new Rectangle(3, 3, 53, 16));
    cLabel2.setBounds(new Rectangle(347, 2, 47, 16));
    cLabel2.setText("A Fecha");
    cLabel2.setOpaque(true);
    cLabel2.setRequestFocusEnabled(true);
    fecfinE.setOpaque(true);
    fecfinE.setBounds(new Rectangle(406, 1, 81, 16));
    cLabel3.setText("De Empr.");
    cLabel3.setBounds(new Rectangle(3, 20, 70, 19));
    empIniE.setAncTexto(30);
    empIniE.setBounds(new Rectangle(70, 20, 233, 16));
    empFinE.setBounds(new Rectangle(406, 20, 233, 16));
    empFinE.setAncTexto(30);
    cLabel6.setBounds(new Rectangle(347, 20, 69, 19));
    cLabel6.setText("A Empr.");
    cLabel7.setText("De Factura");
    cLabel7.setBounds(new Rectangle(3, 40, 60, 16));
    fvc_anoE.setBounds(new Rectangle(70, 63, 41, 16));
    fvc_numeE.setBounds(new Rectangle(100, 40, 55, 16));
    fvc_anoE1.setBounds(new Rectangle(406, 63, 41, 16));
    fvc_numeE1.setBounds(new Rectangle(440, 40, 55, 16));
    cLabel8.setBounds(new Rectangle(347, 40, 60, 16));
    cLabel8.setText("A Factura");
    cLabel9.setText("De Ejercicio");
    cLabel9.setBounds(new Rectangle(4, 64, 66, 17));
    cLabel10.setBounds(new Rectangle(347, 62, 66, 17));
    cLabel10.setText("A Ejercicio");
    cliIniE.setBounds(new Rectangle(70, 84, 272, 19));
    cliIniE.setCeroIsNull(true);
    cLabel12.setBounds(new Rectangle(3, 84, 59, 19));
    cLabel12.setText("De Cliente");
    cLabel13.setBounds(new Rectangle(347, 84, 56, 19));
    cLabel13.setText("A Cliente");
    cliFinE.setBounds(new Rectangle(406, 84, 269, 19));
    cliFinE.setCeroIsNull(true);
    cLabel15.setBounds(new Rectangle(222, 17, 38, 18));
    cLabel15.setText("Activo");
    cli_giroE.setBounds(new Rectangle(261, 38, 65, 19));
    cli_zoncreE.setBounds(new Rectangle(70, 38, 148, 18));
    cli_zoncreE.setAncTexto(30);
    PcondCli.setLayout(null);
    PcondCli.setBorder(titledBorder3);
    PcondCli.setBounds(new Rectangle(1, 112, 334, 67));
    Pprinc.setBorder(BorderFactory.createRaisedBevelBorder());
    Pprinc.setBounds(new Rectangle(0, 1, 676, 109));
    cli_zonrepE.setAncTexto(30);
    cli_zonrepE.setBounds(new Rectangle(70, 17, 148, 18));
    cLabel16.setText("Zona/Cdto");
    cLabel16.setBounds(new Rectangle(7, 38, 65, 18));
    cLabel21.setText("Ag/Zona");
    cLabel21.setBounds(new Rectangle(5, 17, 60, 16));
    cLabel17.setText("Giro");
    cLabel17.setBounds(new Rectangle(222, 38, 38, 18));
    cli_activE.setBounds(new Rectangle(261, 17, 65, 19));
    titledBorder3.setTitle("Discriminadores Clientes");
    fvc_serieE.setMayusc(true);
    fvc_serieE1.setMayusc(true);
    fvc_serieE.setBounds(new Rectangle(70, 40, 25, 16));
    fvc_serieE1.setBounds(new Rectangle(405, 40, 30, 16));
        this.setLayout(null);
    this.setForeground(Color.black);
        this.setSize(new Dimension(669, 176));
        Pprinc.add(fvc_serieE1, null);
        Pprinc.add(fvc_serieE, null);
        Pprinc.add(feciniE, null);
    Pprinc.add(cLabel1, null);
    Pprinc.add(cLabel2, null);
    Pprinc.add(empIniE, null);
    Pprinc.add(cLabel6, null);
        Pprinc.add(fvc_numeE, null);
        Pprinc.add(cLabel7, null);
    Pprinc.add(cLabel9, null);
    Pprinc.add(fvc_anoE, null);
    Pprinc.add(cLabel8, null);
    Pprinc.add(cLabel10, null);
    Pprinc.add(cLabel3, null);
    Pprinc.add(fecfinE, null);
    Pprinc.add(empFinE, null);
        Pprinc.add(fvc_numeE1, null);
        Pprinc.add(fvc_anoE1, null);
    Pprinc.add(cliFinE, null);
    Pprinc.add(cLabel12, null);
    Pprinc.add(cliIniE, null);
    Pprinc.add(cLabel13, null);
    this.add(PcondCli, null);
    PcondCli.add(cli_zonrepE, null);
    PcondCli.add(cLabel21, null);
    PcondCli.add(cLabel15, null);
    PcondCli.add(cli_giroE, null);
    PcondCli.add(cli_activE, null);
    PcondCli.add(cli_zoncreE, null);
    PcondCli.add(cLabel16, null);
    PcondCli.add(cLabel17, null);
    this.add(Pprinc, null);
  }
  /**
   * Devuelve la fecha inicio
   * @return String con al fecha inicio (Formato: dd-MM-yyyy)
   */
  public String getFechaInicio()
  {
      return feciniE.getText();
  }
  /**
   * Devuelve la fecha Final
   * @return String con al fecha Final (Formato: dd-MM-yyyy)
   */
  public String getFechaFinal()
  {
      return fecfinE.getText();
  }

  public void iniciar(DatosTabla dt,ventana papa,JLayeredPane layPan,EntornoUsuario eu) throws Exception
  {

    padre=papa;
    cliIniE.iniciar(dt,papa,layPan,eu);
    cliFinE.iniciar(dt,papa,layPan,eu);
    empIniE.setFormato(true);
    empIniE.setFormato(Types.DECIMAL,"#9",2);
    empFinE.setFormato(true);
    empFinE.setFormato(Types.DECIMAL,"#9",2);
    fvc_serieE.setText("1");
    fvc_serieE1.setText("Z");
    
    s="SELECT emp_codi,emp_nomb FROM v_empresa ORDER BY emp_codi";
    dt.select(s);
    empIniE.addDatos(dt);
    dt.first();
    empIniE.setText(dt.getString("emp_codi"));
    empFinE.addDatos(dt);
    dt.last();
    empFinE.setText(dt.getString("emp_codi"));
    cli_zonrepE.setFormato(Types.CHAR, "XX", 2);
    cli_zonrepE.texto.setMayusc(true);

        pdconfig.llenaDiscr(dt,cli_zonrepE,"CR",eu.em_cod);

     cli_zonrepE.addDatos("**","TODOS");


     cli_zoncreE.setFormato(Types.CHAR, "XX", 2);
     cli_zoncreE.texto.setMayusc(true);
        pdconfig.llenaDiscr(dt,cli_zoncreE,"CC",eu.em_cod);

     cli_zoncreE.addDatos("**","TODOS");

     cli_activE.addItem("Todos","%");
     cli_activE.addItem("Si","S");
     cli_activE.addItem("No","N");
//    cli_activE.setModificable(true);

     cli_giroE.addItem("Todos", "%");
     cli_giroE.addItem("Si", "S");
     cli_giroE.addItem("No", "N");

     fvc_anoE.setValorDec(eu.ejercicio);
     fvc_anoE1.setValorDec(eu.ejercicio);
  }

  boolean checkCampos() throws ParseException
  {
    if (feciniE.getError())
    {
      padre.mensajeErr("Fecha Inicial de Factura NO es correcta");
      feciniE.requestFocus();
      return false;
    }
    if (fecfinE.getError())
    {
      padre.mensajeErr("Fecha Final de Factura NO es correcta");
      fecfinE.requestFocus();
      return false;
    }
    if (empIniE.getError())
    {
      padre.mensajeErr("Empresa INICIAL No Valida");
      empIniE.requestFocus();
      return false;
    }
    if (empFinE.getError())
    {
      padre.mensajeErr("Empresa FINAL No Valida");
      empFinE.requestFocus();
      return false;
    }
    if (fvc_serieE.isNull()) {
        padre.mensajeErr("Introduzca Serie de Factura Inicial");
        fvc_serieE.requestFocus();
        return false;
    }
   if (fvc_serieE1.isNull()) {
          padre.mensajeErr("Introduzca Serie de Factura Final");
          fvc_serieE1.requestFocus();
          return false;
    }
    return true;
  }
  /**
   * Devuelve las condiciones Where segun los criterios introducidos
   * La tabla de las facturas debera tener un alias a 'C'
   * Ejemplo: SELECT * from v_facvec as c, clientes as cl ....
   *
   * @return String Condiciones Where
   */
  public String getCondWhere()
  {
    String condWhere = "and c.fvc_serie between '"+fvc_serieE.getText()+"' and  '"+
        fvc_serieE1.getText()+"'";
    if (empIniE.getValorInt() > 0)
      condWhere += "  and c.emp_codi >= " + empIniE.getValorInt();
    if (empFinE.getValorInt() > 0)
      condWhere += " and c.emp_codi <= " + empFinE.getValorInt();
    if (fvc_anoE.getValorInt() > 0)
      condWhere += "  and c.fvc_ano >= " + fvc_anoE.getValorInt();
    if (fvc_anoE1.getValorInt() > 0)
      condWhere += " and c.fvc_ano <= " + fvc_anoE1.getValorInt();

    if (!feciniE.isNull())
      condWhere += " AND  c.fvc_fecfra >= TO_DATE('" + feciniE.getText() +
          "','dd-MM-yyyy') ";
    if (!fecfinE.isNull())
      condWhere += " and c.fvc_fecfra <= TO_DATE('" + fecfinE.getText() +
          "','dd-MM-yyyy') ";
    if (!cliIniE.isNull())
      condWhere += " and c.cli_codi >= " + cliIniE.getValorInt();
    if (!cliFinE.isNull())
      condWhere += " and c.cli_codi <= " + cliFinE.getValorInt();
    if (fvc_numeE.getValorInt() > 0)
      condWhere += " and c.fvc_nume >= " + fvc_numeE.getValorInt();
    if (fvc_numeE1.getValorInt() > 0)
      condWhere += " and c.fvc_nume <= " + fvc_numeE1.getValorInt();
    if (!cli_zonrepE.isNull(true) && !cli_zonrepE.equals("**"))
      condWhere += " and cl.cli_zonrep  LIKE '" +
          Formatear.reemplazar(cli_zonrepE.getText(), "*", "%") + "'";
    if (!cli_zoncreE.isNull(true) && !cli_zoncreE.equals("**"))
      condWhere += " and cl.cli_zoncre  LIKE '" +
          Formatear.reemplazar(cli_zoncreE.getText(), "*", "%") + "'";
    if (!cli_activE.getValor().equals("%") && !cli_activE.getValor().equals("*"))
      condWhere += " and cl.cli_activ = '" + cli_activE.getValor() + "'";
    if (!cli_giroE.getValor().equals("%") && !cli_giroE.getValor().equals("*"))
      condWhere += " and cl.cli_giro = '" + cli_giroE.getValor() + "'";
    
    return condWhere;
  }
  public void requestFocus()
  {
    if (feciniE!=null)
      feciniE.requestFocus();
    else
      this.requestFocus();
  }

}
