 package gnu.chu.anjelica.pad;

import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import java.sql.*;
import gnu.chu.Menu.*;
import java.awt.*;
import java.util.*;
import gnu.chu.interfaces.*;
import gnu.chu.utilidades.ventanaPad;
import java.awt.event.KeyEvent;
import gnu.chu.sql.DatosTabla;
import javax.swing.SwingConstants;


/**
 *
 * <p>T�tulo: pddvisa </p>
 * <p>Descripción: Mantenimiento Divisas</p>
 * <p>Empresa: chuchiP</p>
 * <p>Copyright: Copyright (c) 2006-2012
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
 * @author chuchiP
 * @version 1.0
 */
public class pddivisa extends ventanaPad   implements PAD
{
  String s;
  String divCodEdi;
  CPanel Pprinc = new CPanel();
  CLabel usu_nombreL = new CLabel();
  CTextField div_codiE = new CTextField(Types.DECIMAL,"####9");
  CTextField div_nombE = new CTextField(Types.CHAR,"X",50);
  boolean modConsulta=false;
//  CButton Baceptar = new CButton();
//  CButton Bcancelar = new CButton();
  CTextField div_codediE = new CTextField(Types.CHAR,"X",3);
  CTextField div_colorE = new CTextField(Types.CHAR,"X",20);
  CLabel cLabel12 = new CLabel();
  CLabel cLabel14 = new CLabel();
  CTextField div_nudeprE = new CTextField(Types.DECIMAL,"99");
  CTextField div_nomabE = new CTextField(Types.CHAR,"X",15);
  CLabel cLabel1 = new CLabel();
  CLabel cLabel2 = new CLabel();
  CTextField div_nudeimE = new CTextField(Types.DECIMAL,"99");
  CLabel cLabel4 = new CLabel();
  CTextField div_maspreE = new CTextField(Types.CHAR,"X",30);
  CTextField div_masimpE = new CTextField(Types.CHAR,"X",20);
  CLabel cLabel6 = new CLabel();
  CLabel cLabel7 = new CLabel();

  public pddivisa(EntornoUsuario eu, Principal p)
  {
    this(eu, p, null);
  }

  public pddivisa(EntornoUsuario eu, Principal p,Hashtable ht)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    try
    {
      if (ht != null)
      {
        if (ht.get("modConsulta") != null)
          modConsulta = Boolean.valueOf(ht.get("modConsulta").toString()).
              booleanValue();
      }
      setTitulo("Mantenimiento Divisas" + (modConsulta ? "SOLO LECTURA" : ""));
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

  public pddivisa(gnu.chu.anjelica.menu p, EntornoUsuario eu,Hashtable ht)
  {
    EU = eu;
    vl = p.getLayeredPane();
    eje = false;

    try
    {
      if (ht != null)
      {
        if (ht.get("modConsulta") != null)
          modConsulta = Boolean.valueOf(ht.get("modConsulta").toString()).
              booleanValue();
      }
      setTitulo("Mantenimiento Divisas" + (modConsulta ? "SOLO LECTURA" : ""));
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
    this.setSize(new Dimension(524, 221));
    this.setVersion("2006-06-18");
    strSql = "SELECT * FROM v_divisa ORDER BY div_codi ";


    statusBar = new StatusBar(this);
    conecta();
    nav = new navegador(this, dtCons, false, modConsulta? navegador.CURYCON:navegador.NORMAL);
    iniciar(this);
    Pprinc.setDefButton(Baceptar);
    Pprinc.setEscButton(Bcancelar);
    Pprinc.setLayout(null);
    div_masimpE.setBounds(new Rectangle(330, 97, 153, 17));

    usu_nombreL.setText("Divisa");
    usu_nombreL.setBounds(new Rectangle(1, 3, 40, 17));
    cLabel12.setText("Color");
    cLabel12.setBounds(new Rectangle(98, 39, 41, 17));
    cLabel14.setText("Codigo Edi");
    cLabel14.setBounds(new Rectangle(299, 22, 75, 17));
    Bcancelar.setBounds(new Rectangle(311, 121, 106, 27));
    Baceptar.setBounds(new Rectangle(122, 121, 106, 27));
    cLabel1.setText("Nombre Abreviado");
    cLabel1.setBounds(new Rectangle(38, 22, 111, 17));
    div_nombE.setBounds(new Rectangle(94, 3, 364, 17));
    div_nomabE.setBounds(new Rectangle(148, 22, 131, 17));
    cLabel2.setRequestFocusEnabled(true);
    cLabel2.setText("Num. Decimales");
    cLabel2.setBounds(new Rectangle(3, 79, 93, 16));
    cLabel4.setText("Mascara");
    cLabel4.setBounds(new Rectangle(5, 97, 83, 17));
    div_maspreE.setBounds(new Rectangle(91, 97, 224, 17));
    cLabel6.setBackground(Color.orange);
    cLabel6.setOpaque(true);
    cLabel6.setHorizontalAlignment(SwingConstants.CENTER);
    cLabel6.setHorizontalTextPosition(SwingConstants.CENTER);
    cLabel6.setText("Precios");
    cLabel6.setBounds(new Rectangle(90, 60, 224, 16));
    cLabel7.setText("Importes");
    cLabel7.setBounds(new Rectangle(330, 60, 153, 16));
    cLabel7.setHorizontalTextPosition(SwingConstants.CENTER);
    cLabel7.setHorizontalAlignment(SwingConstants.CENTER);
    cLabel7.setOpaque(true);
    cLabel7.setRequestFocusEnabled(true);
    cLabel7.setBackground(Color.orange);
    div_nudeimE.setBounds(new Rectangle(330, 79, 26, 17));
    div_nudeprE.setBounds(new Rectangle(90, 79, 26, 17));
    div_colorE.setBounds(new Rectangle(147, 41, 165, 17));
    div_codediE.setBounds(new Rectangle(377, 22, 45, 17));
    div_codiE.setBounds(new Rectangle(44, 3, 49, 17));
    Pprinc.add(div_codiE, null);
    Pprinc.add(div_nombE, null);
    Pprinc.add(usu_nombreL, null);
    this.getContentPane().add(nav, BorderLayout.NORTH);
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);

    Pprinc.add(div_nomabE, null);
    Pprinc.add(cLabel1, null);
    Pprinc.add(cLabel14, null);
    Pprinc.add(div_codediE, null);
    Pprinc.add(div_colorE, null);
    Pprinc.add(cLabel12, null);
    Pprinc.add(cLabel6, null);
    Pprinc.add(div_masimpE, null);
    Pprinc.add(cLabel7, null);
    Pprinc.add(div_nudeprE, null);
    Pprinc.add(div_nudeimE, null);
    Pprinc.add(cLabel2, null);
    Pprinc.add(cLabel4, null);
    Pprinc.add(div_maspreE, null);
    Pprinc.add(Bcancelar, null);
    Pprinc.add(Baceptar, null);
    this.getContentPane().add(Pprinc,  BorderLayout.CENTER);

  }

  public void iniciarVentana() throws Exception
  {

    Pprinc.setButton(KeyEvent.VK_F4,Baceptar);
    div_codiE.setColumnaAlias("div_codi");
    div_nombE.setColumnaAlias("div_nomb");
    div_nomabE.setColumnaAlias("div_nomab");
    div_nudeprE.setColumnaAlias("div_nudepr");
    div_nudeimE.setColumnaAlias("div_nudeim");
    div_maspreE.setColumnaAlias("div_maspre");
    div_masimpE.setColumnaAlias("div_masimp");
    div_colorE.setColumnaAlias("div_color");

    div_codediE.setColumnaAlias("div_codedi");

    activarEventos();
    activaTodo();
    verDatos();
  }
  void activarEventos()
  {
  }

  void verDatos()
  {
    try {
      if (dtCons.getNOREG())
        return;
      div_codiE.setText(dtCons.getString("div_codi"));
      s="SELECT * FROM v_divisa WHERE div_codi = " +div_codiE.getValorInt();
      if (! dtCon1.select(s))
      {
        mensajeErr("Divisa NO ENCONTRADO ... SEGURAMENTE SE BORRO");
        Pprinc.resetTexto();
        div_codiE.setText(dtCons.getString("div_codi"));
        return;
      }
      div_nombE.setText(dtCon1.getString("div_nomb"));
      div_nomabE.setText(dtCon1.getString("div_nomab"));
      div_nudeprE.setText(dtCon1.getString("div_nudepr"));
      div_nudeimE.setText(dtCon1.getString("div_nudeim"));
      div_maspreE.setText(dtCon1.getString("div_maspre"));
      div_masimpE.setText(dtCon1.getString("div_masimp"));
      div_colorE.setText(dtCon1.getString("div_color"));
      div_codediE.setText(dtCon1.getString("div_codedi"));

    } catch (Exception k)
    {
      Error("Error al ver Datos",k);
      return;
    }
  }

  public void activar(boolean enab)
  {
    div_codiE.setEnabled(enab);
    div_nombE.setEnabled(enab);
    div_nomabE.setEnabled(enab);
    div_nudeprE.setEnabled(enab);
    div_nudeimE.setEnabled(enab);
    div_maspreE.setEnabled(enab);
    div_masimpE.setEnabled(enab);
    div_colorE.setEnabled(enab);
    div_codediE.setEnabled(enab);

    Baceptar.setEnabled(enab);
    Bcancelar.setEnabled(enab);
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
  }

  public void ej_query1()
  {
    Component c;
    if ( (c = Pprinc.getErrorConf()) != null)
    {
      c.requestFocus();
      mensaje("Error en Criterios de busqueda");
      return;
    }

    Vector v = new Vector();
    v.addElement(div_codiE.getStrQuery());
    v.addElement(div_nombE.getStrQuery());
    v.addElement(div_nomabE.getStrQuery());
    v.addElement(div_nudeprE.getStrQuery());
    v.addElement(div_nudeimE.getStrQuery());
    v.addElement(div_maspreE.getStrQuery());
    v.addElement(div_masimpE.getStrQuery());
    v.addElement(div_colorE.getStrQuery());
    v.addElement(div_codediE.getStrQuery());

    s = "SELECT * FROM v_divisa ";
    s = creaWhere(s, v,true);
    s+=" ORDER BY div_codi ";
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
      mensajeErr("Nuevos registros selecionados");
    }
    catch (Exception ex)
    {
      fatalError("Error al buscar Divisas ", ex);
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
    div_codiE.setEnabled(false);
    try
    {
      if (!setBloqueo(dtAdd, "v_divisa",div_codiE.getText()))
      {
        msgBox(msgBloqueo);
        nav.pulsado = navegador.NINGUNO;
        activaTodo();
        dtAdd.getConexion().rollback();
        return;
      }
      if (! dtAdd.select("select * from v_divisa where div_codi= "+div_codiE.getValorInt(),true))
      {
        mensajeErr("Registro ha sido borrado");
        resetBloqueo(dtAdd, "v_divisa", div_codiE.getText());
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
    divCodEdi=div_codediE.getText().trim();
    div_nombE.requestFocus();
  }

  public void ej_edit1()
  {
    try
    {
      if (! div_codediE.getText().trim().equals(divCodEdi))
      {
        s = "SELECT * FROM v_divisa WHERE div_codedi = '" + div_codediE.getText() + "'";
        if (dtCon1.select(s))
        {
          mensajeErr("Codigo EDI Ya asignado a divisa: " + dtCon1.getString("div_codi"));
          return;
        }
      }
      dtAdd.edit();
      actValores(dtAdd);
      dtAdd.update(stUp);
      resetBloqueo(dtAdd, "v_divisa",div_codiE.getText(),false);
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
    try {
      resetBloqueo(dtAdd, "v_divisa", div_codiE.getText(), true);
    } catch (Exception ex)
    {
      Error("Error al Quitar Bloqueo", ex);
      return;
    }
    mensajeErr("Modificacion de Datos Cancelada");
    activaTodo();
    verDatos();
  }

  public boolean checkEdit()
  {
    return checkAddNew();
  }


  public void PADAddNew()
  {
    Pprinc.resetTexto();
    activar(true);
    div_codiE.requestFocus();
    mensaje("Insertando Banco ...");
  }
  public boolean checkAddNew()
  {

    if (div_codiE.isNull())
    {
      mensajeErr("Introduzca Codigo de Divisa");
      div_codiE.requestFocus();
      return false;
    }
    if (div_nombE.isNull())
    {
      mensajeErr("Introduzca Nombre de Divisa");
      div_nombE.requestFocus();
      return false;
    }
    if (div_codediE.isNull())
    {
      mensajeErr("Introduzca C�digo EDI de Divisa");
      div_codediE.requestFocus();
      return false;
    }


    return true;
  }
  public void ej_addnew1()
  {
    try
    {
      s="SELECT * FROM v_divisa WHERE div_codi = '"+div_codiE.getText()+"'";
      if (dtCon1.select(s))
      {
        mensajeErr("Divisa YA EXISTE");
        return;
      }
      s = "SELECT * FROM v_divisa WHERE div_codedi = '" + div_codediE.getText() + "'";
      if (dtCon1.select(s))
      {
        mensajeErr("Codigo EDI Ya asignado a divisa: "+dtCon1.getString("div_codi"));
        return;
      }

      dtAdd.addNew("v_divisa");
      dtAdd.setDato("div_codi",div_codiE.getText());
      actValores(dtAdd);
      dtAdd.update(stUp);
      ctUp.commit();
    }
    catch (Exception ex)
    {
      Error("Error al Insertar datos",ex);
      return;
    }
    mensaje("");
    mensajeErr("Banco ... Insertado");

    activaTodo();
//    verDatos();
  }

  void actValores(DatosTabla dt) throws Exception
  {
     dt.setDato("div_nomb",div_nombE.getText());
     dt.setDato("div_nomab",div_nomabE.getText());
     dt.setDato("div_nudepr",div_nudeprE.getText());
     dt.setDato("div_nudeim",div_nudeimE.getText());
     dt.setDato("div_maspre",div_maspreE.getText());
     dt.setDato("div_masimp",div_masimpE.getText());
     dt.setDato("div_color",div_colorE.getText());
     dt.setDato("div_codedi",div_codediE.getText());

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

      if (!setBloqueo(dtAdd, "v_divisa", div_codiE.getText()))
      {
        msgBox(msgBloqueo);
        nav.pulsado = navegador.NINGUNO;
        activaTodo();
        return;
      }
      if (!dtAdd.select("select * from v_divisa where div_codi= " +div_codiE.getValorInt(), true))
      {
        mensajeErr("Registro ha sido borrado");
        resetBloqueo(dtAdd, "v_divisa", div_codiE.getText());
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

    Baceptar.setEnabled(true);
    Bcancelar.setEnabled(true);
    Bcancelar.requestFocus();
    mensaje("Borrar Registro ...");
  }

  public void ej_delete1()
  {
    try
    {
      dtAdd.delete(stUp);
      resetBloqueo(dtAdd, "v_divisa",div_codiE.getText(),false);
      ctUp.commit();
      rgSelect();
    }
    catch (Exception ex)
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
    try {
      resetBloqueo(dtAdd, "v_divisa", div_codiE.getText(), true);
    } catch (Exception k)
    {
      Error("Error al Anular bloqueo",k);
    }
    mensajeErr("Borrado  de Datos Cancelada");
    verDatos();
  }


}
