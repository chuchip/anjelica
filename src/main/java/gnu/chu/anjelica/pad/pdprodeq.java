package gnu.chu.anjelica.pad;

import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import java.sql.*;
import gnu.chu.Menu.*;
import java.awt.*;
import java.util.*;
import javax.swing.BorderFactory;
import gnu.chu.camposdb.*;
import gnu.chu.interfaces.*;
import java.awt.event.*;

/**
 *
 * <p>Título: pdproeq </p>
 * <p>Descripció: PAD Equivalencias de Productos. Marca la equivalencia
 * entre un producto tipo CHAR a un INT.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: anjelica</p>
 *   Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
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
 * @author gnu.chup
 * @version 1.0
 */
public class pdprodeq extends ventanaPad   implements PAD
{
  String s;
  proPanel pro_numeE = new proPanel();
  CPanel Pprinc = new CPanel();
  CLabel cLabel1 = new CLabel();
  CLabel cLabel2 = new CLabel();
  CTextField pro_codiE = new CTextField(Types.CHAR,"X",12);
  CLabel cLabel3 = new CLabel();
  CLinkBox pro_animE = new CLinkBox();
  CLabel cLabel4 = new CLabel();
  CTextField pro_corteE = new CTextField(Types.DECIMAL,"999");
  CLabel cLabel5 = new CLabel();
  CLinkBox pro_subcorE = new CLinkBox();
  CLabel cLabel6 = new CLabel();
  CLinkBox pro_clasifE = new CLinkBox();
  CLabel cLabel7 = new CLabel();
  CLinkBox pro_tipoE = new CLinkBox();
  CLabel cLabel8 = new CLabel();
  CLinkBox pro_origE = new CLinkBox();
  CLabel cLabel9 = new CLabel();
  CLinkBox pro_estadE = new CLinkBox();
  CLabel cLabel10 = new CLabel();
  CTextField pro_otrosE = new CTextField(Types.CHAR,"XX",2);
//  CButton Baceptar = new CButton();
//  CButton Bcancelar = new CButton();
  CPanel Pprod = new CPanel();
  CButton Bsal = new CButton();
//  proPanel pro_numeE = new proPanel();
//  CTextField pro_codiE = new CTextField(Types.CHAR,"X",12);

  public pdprodeq(EntornoUsuario eu, Principal p)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    setTitulo("PAD Equivalencias Productos (V 1.0)");

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

  public pdprodeq(gnu.chu.anjelica.menu p, EntornoUsuario eu)
  {
    EU = eu;
    vl = p.getLayeredPane();
    setTitulo("PAD Productos Nuevos (V 1.0)");
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

    iniciar(509, 270);
    strSql = "SELECT * FROM refproeq WHERE emp_codi = " + EU.em_cod+
        " ORDER BY pro_codi ";

    statusBar = new StatusBar(this);
    conecta();
    nav = new navegador(this, dtCons, false, navegador.NORMAL);
    Iniciar(this);
    Pprinc.setMaximumSize(new Dimension(32767, 32767));
    Pprinc.setMinimumSize(new Dimension(471, 149));
    Pprinc.setDefButton(Baceptar);
    Pprinc.setEscButton(Bcancelar);
    Pprinc.setLayout(null);
    cLabel1.setText("Producto");
    cLabel1.setBounds(new Rectangle(2, 1, 58, 17));
    cLabel2.setRequestFocusEnabled(true);
    cLabel2.setText("Referencia");
    cLabel2.setBounds(new Rectangle(164, 100, 63, 17));
//    Baceptar.setBounds(new Rectangle(98, 65, 100, 24));
//    Bcancelar.setBounds(new Rectangle(228, 65, 100, 24));
    cLabel3.setText("Animal");
    cLabel3.setBounds(new Rectangle(9, 8, 40, 18));
    pro_animE.setAncTexto(25);
    pro_animE.setBounds(new Rectangle(52, 8, 117, 18));
    cLabel4.setText("Corte");
    cLabel4.setBounds(new Rectangle(174, 8, 31, 18));
    pro_corteE.setText("123");
    pro_corteE.setBounds(new Rectangle(204, 8, 27, 18));
    cLabel5.setText("SubCorte");
    cLabel5.setBounds(new Rectangle(238, 8, 53, 18));
    pro_subcorE.setText("cLinkBox1");
    pro_subcorE.setAncTexto(15);
    pro_subcorE.setBounds(new Rectangle(288, 8, 164, 18));
    cLabel6.setText("Clasificacion");
    cLabel6.setBounds(new Rectangle(8, 29, 73, 18));
    pro_clasifE.setAncTexto(15);
    pro_clasifE.setBounds(new Rectangle(88, 29, 151, 18));
    cLabel7.setText("Tipo");
    cLabel7.setBounds(new Rectangle(252, 29, 33, 18));
    pro_tipoE.setFormato(false);
    pro_tipoE.setAncTexto(15);
    pro_tipoE.setBounds(new Rectangle(288, 29, 164, 18));
    cLabel8.setText("Origen");
    cLabel8.setBounds(new Rectangle(8, 47, 49, 18));
    pro_origE.setAncTexto(15);
    pro_origE.setBounds(new Rectangle(50, 47, 140, 18));
    cLabel9.setText("Estado");
    cLabel9.setBounds(new Rectangle(192, 47, 41, 18));
    pro_estadE.setAlignmentY((float) 0.5);
    pro_estadE.setAncTexto(15);
    pro_estadE.setBounds(new Rectangle(238, 47, 142, 18));
    cLabel10.setText("Otros");
    cLabel10.setBounds(new Rectangle(385, 47, 35, 18));
    pro_otrosE.setText("");
    pro_otrosE.setBounds(new Rectangle(425, 47, 23, 18));
    Pprod.setBorder(BorderFactory.createLoweredBevelBorder());
    Pprod.setBounds(new Rectangle(10, 21, 461, 74));
    Pprod.setLayout(null);
    Bsal.setBounds(new Rectangle(454, 53, 1, 1));
    Bcancelar.setBounds(new Rectangle(318, 124, 97, 25));
    Baceptar.setBounds(new Rectangle(96, 124, 97, 25));
    pro_codiE.setBounds(new Rectangle(232, 100, 97, 17));
    pro_numeE.setBounds(new Rectangle(72, 1, 367, 17));
    this.getContentPane().add(nav, BorderLayout.NORTH);
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);
    this.getContentPane().add(Pprinc,  BorderLayout.CENTER);
    Pprinc.add(pro_numeE, null);
    Pprinc.add(cLabel1, null);
    Pprod.add(pro_subcorE, null);
    Pprod.add(pro_corteE, null);
    Pprod.add(cLabel3, null);
    Pprod.add(pro_clasifE, null);
    Pprod.add(cLabel8, null);
    Pprod.add(cLabel7, null);
    Pprod.add(cLabel4, null);
    Pprod.add(pro_animE, null);
    Pprod.add(pro_tipoE, null);
    Pprod.add(cLabel6, null);
    Pprod.add(cLabel5, null);
    Pprod.add(pro_origE, null);
    Pprod.add(pro_estadE, null);
    Pprod.add(cLabel9, null);
    Pprod.add(pro_otrosE, null);
    Pprod.add(cLabel10, null);
    Pprod.add(Bsal, null);
    Pprinc.add(cLabel2, null);
    Pprinc.add(pro_codiE, null);
    Pprinc.add(Bcancelar, null);
    Pprinc.add(Baceptar, null);
    Pprinc.add(Pprod, null);
  }

  public void iniciarVentana() throws Exception
  {
    pro_animE.addDatos("VA","VAca",false);
    pro_animE.addDatos("AN","Añojo",false);
    pro_animE.addDatos("TL","Tern.Leche",false);
    pro_animE.addDatos("CE","CErdo",false);
    pro_animE.addDatos("AV","Avestruz",false);
    pro_animE.addDatos("CO","COrdero",false);
    pro_animE.addDatos("CL","Cordero Leche",true);
    pro_animE.setFormato(true);
    pro_animE.setFormato(Types.CHAR,"XX",2);
    pro_animE.setMayusculas(true);

    pro_subcorE.addDatos("X","----",false);
    pro_subcorE.addDatos("A","Alto",false);
    pro_subcorE.addDatos("B","Bajo",false);
    pro_subcorE.addDatos("T","T-BONE",false);
    pro_subcorE.addDatos("S","Sin Cordon",true);
    pro_subcorE.setFormato(true);
    pro_subcorE.setFormato(Types.CHAR,"X",1);
    pro_subcorE.setMayusculas(true);

    pro_clasifE.addDatos("X","----",false);
    pro_clasifE.addDatos("E","Extra",false);
    pro_clasifE.addDatos("A","3.0+",false);
    pro_clasifE.addDatos("B","2.5+",false);
    pro_clasifE.addDatos("C","2.0+",false);
    pro_clasifE.addDatos("D","Despiece",false);
    pro_clasifE.addDatos("M","Menu",false);
    pro_clasifE.addDatos("3","23.0+",false);
    pro_clasifE.addDatos("5","25.0+ / 5.0+",false);
    pro_clasifE.addDatos("7","7.0+",false);
    pro_clasifE.addDatos("8","28.0+ / 8.0+",false);
    pro_clasifE.addDatos("0","30.0+",true);

    pro_clasifE.setMayusculas(true);
    pro_clasifE.setFormato(true);
    pro_clasifE.setFormato(Types.CHAR,"X",1);

    pro_tipoE.addDatos("X","----",false);
    pro_tipoE.addDatos("M","C/MER",false);
    pro_tipoE.addDatos("S","S/MER",false);
    pro_tipoE.addDatos("F","Fileteado",false);
    pro_tipoE.addDatos("U","Por Unid",false);
    pro_tipoE.addDatos("E","Especial",true);
    pro_tipoE.setFormato(true);
    pro_tipoE.setFormato(Types.CHAR,"X",1);
    pro_tipoE.setMayusculas(true);

    pro_origE.addDatos("X","----");
    pro_origE.addDatos("A","Alemania",false);
    pro_origE.addDatos("H","Holanda",false);
    pro_origE.addDatos("I","IberRioja",false);
    pro_origE.addDatos("S","Suecia",false);
    pro_origE.addDatos("D","Dinamarca",true);
    pro_origE.setFormato(true);
    pro_origE.setFormato(Types.CHAR,"X",1);
    pro_origE.setMayusculas(true);

    pro_estadE.addDatos("X","----",false);
    pro_estadE.addDatos("C","Congelado",false);
    pro_estadE.addDatos("D","Descongelado",true);
    pro_estadE.setFormato(true);
    pro_estadE.setFormato(Types.CHAR,"X",1);
    pro_estadE.setMayusculas(true);

    pro_otrosE.setMayusc(true);

    pro_numeE.iniciar(dtStat,this,vl,EU);
    pro_numeE.setColumnaAlias("pro_nume");
    pro_codiE.setColumnaAlias("pro_codi");
    activarEventos();
    activaTodo();
    verDatos();
  }
  void activarEventos()
  {
    Bsal.addFocusListener(new FocusAdapter() {
      public void focusGained(FocusEvent e) {
        if (nav.pulsado!=navegador.QUERY)
          llenaCodPro();
      }
    });
  }

  void llenaCodPro()
  {
    String s=Formatear.ajusIzq(pro_animE.getText(),2)+
                  pro_corteE.getText()+
                  Formatear.ajusIzq(pro_subcorE.getText(),1)+
                  Formatear.ajusIzq(pro_clasifE.getText(),1)+
                  Formatear.ajusIzq(pro_tipoE.getText(),1)+
                  Formatear.ajusIzq(pro_origE.getText(),1)+
                  Formatear.ajusIzq(pro_estadE.getText(),1)+
                  pro_otrosE.getText();
    int pos=5;
    if (! pro_subcorE.getText().equals("X"))
      pos=6;
    if (! pro_clasifE.getText().equals("X"))
      pos=7;
    if (! pro_tipoE.getText().equals("X"))
      pos=8;
    if (! pro_origE.getText().equals("X"))
      pos=9;
    if (! pro_estadE.getText().equals("X"))
      pos=10;
    if (! pro_otrosE.getText().trim().equals(""))
      pos=10+pro_otrosE.getText().trim().length();

    pro_codiE.setText(s.substring(0,pos));
  }
  void verDatos()
  {
    try {
      if (dtCons.getNOREG())
        return;
      pro_numeE.setText(dtCons.getString("pro_nume"));
      s = "SELECT pro_codi FROM refproeq WHERE emp_codi = " + EU.em_cod +
          " and pro_nume = " + pro_numeE.getText();
      if (! dtCon1.select(s))
      {
        mensajeErr("Codigo NO ENCONTRADO ... SEGURAMENTE SE BORRO");
        pro_codiE.resetTexto();
        return;
      }
      pro_codiE.setText(dtCon1.getString("pro_codi"));
      verDatPro(pro_codiE.getText());
    } catch (Exception k)
    {
      Error("Error al ver Datos",k);
      return;
    }
  }
  void verDatPro(String codPro)
  {
    Pprod.resetTexto();
    if (codPro.length()>=2)
      pro_animE.setText(codPro.substring(0,2));
    if (codPro.length()>=5)
      pro_corteE.setText(codPro.substring(2,5));
    if (codPro.length()>=6)
      pro_subcorE.setText(codPro.substring(5,6));
    if (codPro.length()>=7)
      pro_clasifE.setText(codPro.substring(6,7));
    if (codPro.length()>=8)
      pro_tipoE.setText(codPro.substring(7,8));
    if (codPro.length()>=9)
      pro_corteE.setText(codPro.substring(8,9));
    if (codPro.length()>=10)
      pro_estadE.setText(codPro.substring(9,10));
    if (codPro.length()>=11)
      pro_corteE.setText(codPro.substring(11));

  }
  public void activar(boolean act)
  {
    pro_codiE.setEnabled(act);
    pro_numeE.setEnabled(act);
    Pprod.setEnabled(act);
    Baceptar.setEnabled(act);
    Bcancelar.setEnabled(act);
  }

  public void PADPrimero()
  {
    verDatos();
  }

  public void PADAnterior()
  {
    verDatos();
  }

  public void PADSiguiente()
  {
    verDatos();
  }

  public void PADUltimo()
  {
    verDatos();
  }

  public void PADQuery()
  {
    activar(true);
    Pprinc.setQuery(true);
    Pprinc.resetTexto();
    Pprod.setEnabled(false);
    pro_numeE.requestFocus();
  }

  public void ej_query1()
  {
    Vector v = new Vector();
    v.addElement(pro_numeE.getStrQuery());
    v.addElement(pro_codiE.getStrQuery());
    s = "SELECT * FROM refproeq WHERE emp_codi = " + EU.em_cod;
    s = creaWhere(s, v,false);
    s+=" ORDER BY pro_codi ";
    Pprinc.setQuery(false);
//    debug(s);
    try
    {
      if (!dtCons.select(s))
      {
        mensaje("");
        mensajeErr("No encontrados Registros para estos criterios");
        rgSelect();
        activaTodo();
        verDatos();
        return;
      }
      mensaje("");
      strSql = s;
      activaTodo();
      rgSelect();
      verDatos();
      mensajeErr("Nuevos regisgtros selecionados");
    }
    catch (Exception ex)
    {
      fatalError("Error al buscar Inventarios: ", ex);
    }

  }

  public void canc_query()
  {
    Pprinc.setQuery(false);
    mensaje("");
    mensajeErr("Consulta ... CANCELADA");
    activaTodo();
    verDatos();
  }

  public void PADEdit()
  {
    activar(true);
    try
    {
      s = "SELECT * FROM refproeq WHERE emp_codi = " + EU.em_cod +
          " and pro_nume = " + pro_numeE.getValorInt();
      if (!dtAdd.select(s, true))
      {
        mensajeErr("Registro ha sido borrado");
        activaTodo();
        mensaje("");
        return;
      }
    }
    catch (Exception k)
    {
      Error("Error al bloquear el registro", k);
      return;
    }
    pro_codiE.setEnabled(false);
    pro_numeE.setEnabled(false);
    pro_animE.requestFocus();
  }

  public void ej_edit1()
  {
    try
    {
      llenaCodPro();
      if (pro_codiE.getText().trim().length() < 5)
      {
        mensajeErr("Introduzca un Codigo de Prod. Valido. Min: 5 Caract.");
        return;
      }

      s = "SELECT * FROM refproeq WHERE emp_codi = " + EU.em_cod +
          " and pro_codi = '" + pro_codiE.getText() + "'" +
          " and pro_nume != " + pro_numeE.getValorInt();
      if (dtCon1.select(s))
      {
        mensajeErr("El producto " + dtCon1.getString("pro_nume") +
                   " YA esta asignado a  este codigo");
        return;
      }
      s = "SELECT pro_codi FROM refproeq WHERE emp_codi = " + EU.em_cod +
          " and pro_nume = " + pro_numeE.getValorInt();
      if (!dtAdd.select(s, true))
      {
        mensajeErr("Registro ha sido barrado mientras se editaba");
        activaTodo();
        mensaje("");
        return;
      }
      dtAdd.edit(dtAdd.getCondWhere());
      dtAdd.setDato("pro_codi", pro_codiE.getText());
      dtAdd.update(stUp);
      ctUp.commit();
    }
    catch (Throwable ex)
    {
      Error("Error al Modificar datos", ex);
      return;
    }
    mensaje("");
    mensajeErr("Datos ... Modificados");

    activaTodo();
    verDatos();

  }

  public void canc_edit()
  {
    mensaje("");
    activaTodo();
    mensajeErr("Modificacion de Datos Cancelada");
    verDatos();
  }

  public void PADAddNew()
  {
    Pprinc.resetTexto();
    activar(true);
    pro_numeE.requestFocus();
    pro_codiE.setEnabled(false);
    mensaje("Insertando Registro ...");
  }

  public void ej_addnew1()
  {
    try
    {
      if (pro_numeE.isNull())
      {
        mensajeErr("Introduzca Codigo de Producto");
        return;
      }
      if (!pro_numeE.controla(true))
      {
        mensajeErr(pro_numeE.getMsgError());
        return;
      }
      llenaCodPro();
      if (pro_codiE.getText().trim().length()<5)
      {
        mensajeErr("Introduzca un Codigo de Prod. Valido. Min: 5 Caract.");
        return;
      }

      s="SELECT * FROM refproeq WHERE emp_codi = "+EU.em_cod +
          " and pro_nume = "+pro_numeE.getValorInt();
      if (dtCon1.select(s))
      {
        mensajeErr("El producto "+dtCon1.getString("pro_codi")+" YA esta asignado a  este Numero");
        return;
      }
      s="SELECT * FROM refproeq WHERE emp_codi = "+EU.em_cod +
          " and pro_codi = '"+pro_codiE.getText()+"'";
      if (dtCon1.select(s))
      {
        mensajeErr("El producto "+dtCon1.getString("pro_nume")+" YA esta asignado a  este codigo");
//        return;
      }
      dtAdd.addNew("refproeq");
      dtAdd.setDato("emp_codi",EU.em_cod);
      dtAdd.setDato("pro_nume",pro_numeE.getValorInt());
      dtAdd.setDato("pro_codi",pro_codiE.getText());
      dtAdd.update(stUp);
      ctUp.commit();
    }
    catch (Exception ex)
    {
      Error("Error al Insertar datos",ex);
      return;
    }
    mensaje("");
    mensajeErr("Datos ... Insertados");

    activaTodo();
//    verDatos();
  }

  public void canc_addnew()
  {
    mensaje("");
    activaTodo();
    mensajeErr("Insercion de Datos Cancelada");
    verDatos();
  }

  public void PADDelete()
  {
    try
    {
      s = "SELECT * FROM refproeq WHERE emp_codi = " + EU.em_cod +
          " and pro_nume = " + pro_numeE.getValorInt();
      if (!dtAdd.select(s, true))
      {
        mensajeErr("Registro ha sido borrado");
        activaTodo();
        return;
      }
    }
    catch (Exception k)
    {
      Error("Error al bloquear el registro", k);
      return;
    }

    Baceptar.setEnabled(true);
    Bcancelar.setEnabled(true);
    Bcancelar.requestFocus();
    mensaje("Borrar Registro ...");
  }

  public void ej_delete1()
  {
    try
    {
      s = "DELETE FROM refproeq WHERE emp_codi = " + EU.em_cod +
          " and pro_nume = " + pro_numeE.getValorInt();
      stUp.executeUpdate(s);
      ctUp.commit();
    }
    catch (SQLException ex)
    {
      Error("Error al borrar Registro",ex);
    }
    activaTodo();
    verDatos();
    mensaje("");
    mensajeErr("Registro ... Borrado");
  }
  public void canc_delete() {
    mensaje("");
    activaTodo();
    mensajeErr("Borrado  de Datos Cancelada");
    verDatos();
  }


}
