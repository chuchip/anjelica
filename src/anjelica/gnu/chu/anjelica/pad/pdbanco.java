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
 * <p>Título: pdbanco </p>
 * <p>Descripción: Mantenimiento Bancos de Ventas</p>
 * <p>Empresa: miCasa</p>
 * <p>Copyright: Copyright (c) 2005
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
 * @author chuchiP
 * @version 1.0
 */
public class pdbanco extends ventanaPad   implements PAD
{
  String s;
  CPanel Pprinc = new CPanel();
  CLabel usu_nombreL = new CLabel();
  CTextField ban_codiE = new CTextField(Types.DECIMAL,"####9");
  CTextField ban_nombE = new CTextField(Types.CHAR,"X",50);
  boolean modConsulta=false;
//  CButton Baceptar = new CButton();
//  CButton Bcancelar = new CButton();
  CTextField ban_cuentaE = new CTextField(Types.DECIMAL,"9999999999");
  CLabel cLabel13 = new CLabel();
  CTextField ban_oficiE = new CTextField(Types.DECIMAL,"9999");
  CLabel cLabel12 = new CLabel();
  CLabel cLabel14 = new CLabel();
  CTextField ban_digitoE = new CTextField(Types.DECIMAL,"99");

  public pdbanco(EntornoUsuario eu, Principal p)
  {
    this(eu, p, null);
  }

  public pdbanco(EntornoUsuario eu, Principal p,Hashtable ht)
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
      setTitulo("Mantenimiento Bancos" + (modConsulta ? "SOLO LECTURA" : ""));
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

  public pdbanco(gnu.chu.anjelica.menu p, EntornoUsuario eu,Hashtable ht)
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
      setTitulo("Mantenimiento Bancos" + (modConsulta ? "SOLO LECTURA" : ""));
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
    this.setSize(new Dimension(488,203));
    this.setVersion("2012-10-01");
    strSql = "SELECT * FROM v_banco ORDER BY ban_codi ";

    cLabel13.setHorizontalAlignment(SwingConstants.RIGHT);

    statusBar = new StatusBar(this);
    conecta();
    nav = new navegador(this, dtCons, false, modConsulta? navegador.CURYCON:navegador.NORMAL);
    iniciar(this);
    Pprinc.setDefButton(Baceptar);
    Pprinc.setEscButton(Bcancelar);
    Pprinc.setLayout(null);
    usu_nombreL.setText("Banco");
    usu_nombreL.setBounds(new Rectangle(4, 3, 47, 17));
    cLabel13.setText("Digito Control");
    cLabel13.setBounds(new Rectangle(196, 29, 76, 17));
    cLabel12.setText("Oficina");
    cLabel12.setBounds(new Rectangle(102, 29, 49, 17));
    cLabel14.setText("Num.Cuenta");
    cLabel14.setBounds(new Rectangle(311, 29, 75, 17));
    Bcancelar.setBounds(new Rectangle(262, 62, 106, 27));
    Baceptar.setBounds(new Rectangle(104, 62, 106, 27));
    ban_cuentaE.setBounds(new Rectangle(388, 29, 85, 17));
    ban_digitoE.setBounds(new Rectangle(279, 29, 26, 17));
    ban_oficiE.setBounds(new Rectangle(150, 29, 41, 17));
    ban_nombE.setBounds(new Rectangle(109, 3, 364, 17));
    ban_codiE.setBounds(new Rectangle(57, 3, 49, 17));
    this.getContentPane().add(nav, BorderLayout.NORTH);
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);

    Pprinc.add(usu_nombreL, null);
    Pprinc.add(ban_codiE, null);
    Pprinc.add(ban_nombE, null);
    Pprinc.add(ban_cuentaE, null);
    Pprinc.add(cLabel14, null);
    Pprinc.add(ban_digitoE, null);
    Pprinc.add(cLabel13, null);
    Pprinc.add(ban_oficiE, null);
    Pprinc.add(cLabel12, null);
    Pprinc.add(Bcancelar, null);
    Pprinc.add(Baceptar, null);

  }

  public void iniciarVentana() throws Exception
  {

    Pprinc.setButton(KeyEvent.VK_F4,Baceptar);
    ban_codiE.setColumnaAlias("ban_codi");
    ban_nombE.setColumnaAlias("ban_nomb");
    ban_oficiE.setColumnaAlias("ban_ofici");
    ban_digitoE.setColumnaAlias("ban_digito");
    ban_cuentaE.setColumnaAlias("ban_cuenta");

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
      ban_codiE.setText(dtCons.getString("ban_codi"));
      s="SELECT * FROM v_banco WHERE ban_codi = " +ban_codiE.getValorInt();
      if (! dtCon1.select(s))
      {
        mensajeErr("Banco NO ENCONTRADO ... SEGURAMENTE SE BORRO");
        Pprinc.resetTexto();
        ban_codiE.setText(dtCons.getString("ban_codi"));
        return;
      }
      ban_nombE.setText(dtCon1.getString("ban_nomb"));
      ban_oficiE.setText(dtCon1.getString("ban_ofici"));
      ban_digitoE.setText(dtCon1.getString("ban_digito"));
      ban_cuentaE.setValorInt(dtCon1.getInt("ban_cuenta",true));

    } catch (Exception k)
    {
      Error("Error al ver Datos",k);
      return;
    }
  }

  public void activar(boolean enab)
  {
    ban_codiE.setEnabled(enab);
    ban_nombE.setEnabled(enab);
    ban_oficiE.setEnabled(enab);
    ban_digitoE.setEnabled(enab);
    ban_cuentaE.setEnabled(enab);


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

    ArrayList v = new ArrayList();
    v.add(ban_codiE.getStrQuery());
    v.add(ban_nombE.getStrQuery());
    v.add(ban_oficiE.getStrQuery());
    v.add(ban_digitoE.getStrQuery());
    v.add(ban_cuentaE.getStrQuery());

    s = "SELECT * FROM v_banco ";
    s = creaWhere(s, v,true);
    s+=" ORDER BY ban_codi ";
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
      fatalError("Error al buscar Bancos Tesoreria: ", ex);
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
    ban_codiE.setEnabled(false);
    try
    {
      if (!setBloqueo(dtAdd, "v_banco",ban_codiE.getText()))
      {
        msgBox(msgBloqueo);
        nav.pulsado = navegador.NINGUNO;
        activaTodo();
        dtAdd.getConexion().rollback();
        return;
      }
      if (! dtAdd.select("select * from v_banco where ban_codi= "+ban_codiE.getValorInt(),true))
      {
        mensajeErr("Registro ha sido borrado");
        resetBloqueo(dtAdd, "v_banco", ban_codiE.getText());
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
    ban_nombE.requestFocus();
  }

  public void ej_edit1()
  {
    try
    {
      dtAdd.edit();
      actValores(dtAdd);
      dtAdd.update(stUp);
      resetBloqueo(dtAdd, "v_banco",ban_codiE.getText(),false);
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
      resetBloqueo(dtAdd, "v_banco", ban_codiE.getText(), true);
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
    ban_codiE.requestFocus();
    mensaje("Insertando Banco ...");
  }
  public boolean checkAddNew()
  {

    if (ban_codiE.isNull())
    {
      mensajeErr("Introduzca Codigo de Banco");
      ban_codiE.requestFocus();
      return false;
    }
    if (ban_nombE.isNull())
    {
      mensajeErr("Introduzca Nombre de Banco");
      ban_nombE.requestFocus();
      return false;
    }
    int digControl;
    if (ban_oficiE.getValorInt()!=0)
    {
      String cuenta = Formatear.format(ban_codiE.getValorInt(), "9999") +
          Formatear.format(ban_oficiE.getValorInt(), "9999");
      digControl=Formatear.getNumControl(Integer.parseInt(cuenta));
      if (digControl !=
          Integer.parseInt(Formatear.format(ban_digitoE.getValorInt(), "99").
                           substring(0, 1)))
      {
        mensajeErr("Digito control NO VALIDO para banco y sucursal ("+digControl+")");
        ban_digitoE.requestFocus();
        return false;
      }
    }
    digControl=Formatear.getNumControl(ban_cuentaE.getValorDec());
    if (digControl !=
          Integer.parseInt(Formatear.format(ban_digitoE.getValorInt(), "99").
                           substring(1, 2)) && ban_cuentaE.getValorDec()!=0)
      {
        mensajeErr("Digito control NO VALIDO para Num. Cuenta ("+digControl+")");
        ban_digitoE.requestFocus();
        return false;
      }

    return true;
  }
  public void ej_addnew1()
  {
    try
    {
      s="SELECT * FROM v_banco WHERE ban_codi = '"+ban_codiE.getText()+"'";
      if (dtCon1.select(s))
      {
        mensajeErr("Banco YA EXISTE");
        return;
      }
      dtAdd.addNew("v_banco");
      dtAdd.setDato("ban_codi",ban_codiE.getText());
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
     dt.setDato("ban_nomb",ban_nombE.getText());
     dt.setDato("ban_ofici",ban_oficiE.getValorInt());
     dt.setDato("ban_digito",ban_digitoE.getValorInt());
     dt.setDato("ban_cuenta",ban_cuentaE.getValorDec());
     dt.setDato("cli_codi",0);
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

      if (!setBloqueo(dtAdd, "v_banco", ban_codiE.getText()))
      {
        msgBox(msgBloqueo);
        nav.pulsado = navegador.NINGUNO;
        activaTodo();
        return;
      }
      if (!dtAdd.select("select * from v_banco where ban_codi= " +ban_codiE.getValorInt(), true))
      {
        mensajeErr("Registro ha sido borrado");
        resetBloqueo(dtAdd, "v_banco", ban_codiE.getText());
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
      resetBloqueo(dtAdd, "v_banco",ban_codiE.getText(),false);
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
      resetBloqueo(dtAdd, "v_banco", ban_codiE.getText(), true);
    } catch (Exception k)
    {
      Error("Error al Anular bloqueo sobre tabla bancos",k);
    }
    mensajeErr("Borrado  de Datos Cancelada");
    verDatos();
  }


}
