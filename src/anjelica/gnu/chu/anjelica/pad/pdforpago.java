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

/**
 *
 * <p>Título: pdforpago </p>
 * <p>Descripción: Mantenimiento de Formas de Pago</p>
 * <p>Empresa: miSL</p>
 * <p>Copyright: Copyright (c) 2006
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
public class pdforpago extends ventanaPad   implements PAD
{
  String s;
  CPanel Pprinc = new CPanel();
  CLabel eje_nombL = new CLabel();
  CTextField fpa_dia1E = new CTextField(Types.DECIMAL,"#99");
  boolean modConsulta=false;
//  CButton Baceptar = new CButton();
//  CButton Bcancelar = new CButton();
  CTextField fpa_codiE = new CTextField(Types.DECIMAL,"###9");
  CLabel cLabel13 = new CLabel();
  CLabel cLabel14 = new CLabel();
  CComboBox fpa_esgirE = new CComboBox();
  CTextField fpa_nombE = new CTextField(Types.CHAR,"X");
  CTextField fpa_dia2E = new CTextField(Types.DECIMAL,"#99");
  CTextField fpa_dia3E = new CTextField(Types.DECIMAL,"#99");

  public pdforpago(EntornoUsuario eu, Principal p)
  {
    this(eu, p, null);
  }

  public pdforpago(EntornoUsuario eu, Principal p,Hashtable ht)
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
      setTitulo("Mantenimiento Formas Pago ");
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

  public pdforpago(gnu.chu.anjelica.menu p, EntornoUsuario eu,Hashtable ht)
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
      setTitulo("Mantenimiento Formas Pago ");
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
    this.setSize(new Dimension(488,203));
    this.setVersion("2012-05-31"+ (modConsulta ? "SOLO LECTURA" : ""));
    strSql = "SELECT * FROM v_forpago ORDER BY fpa_codi ";

    statusBar = new StatusBar(this);
    conecta();
    nav = new navegador(this, dtCons, false, modConsulta? navegador.CURYCON:navegador.NORMAL);
    iniciar(this);
    Pprinc.setDefButton(Baceptar);
    Pprinc.setEscButton(Bcancelar);
    Pprinc.setLayout(null);
    eje_nombL.setText("Dias de Pago");
    eje_nombL.setBounds(new Rectangle(15, 30, 89, 17));
    cLabel13.setText("Es giro ");
    cLabel13.setBounds(new Rectangle(364, 30, 54, 17));
    cLabel14.setText("Forma Pago");
    cLabel14.setBounds(new Rectangle(15, 6, 73, 17));
    Bcancelar.setBounds(new Rectangle(265, 65, 106, 27));
    Baceptar.setBounds(new Rectangle(107, 65, 106, 27));
    fpa_codiE.setBounds(new Rectangle(84, 6, 38, 17));
    fpa_esgirE.setBounds(new Rectangle(415, 30, 54, 17));
    fpa_dia1E.setBounds(new Rectangle(97, 30, 29, 17));
    fpa_nombE.setBounds(new Rectangle(128, 6, 341, 17));
    fpa_dia2E.setBounds(new Rectangle(132, 30, 29, 17));
    fpa_dia3E.setBounds(new Rectangle(169, 30, 29, 17));
    this.getContentPane().add(nav, BorderLayout.NORTH);
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);

    Pprinc.add(eje_nombL, null);
    Pprinc.add(cLabel14, null);
    Pprinc.add(fpa_codiE, null);
    Pprinc.add(fpa_nombE, null);
    Pprinc.add(fpa_dia1E, null);
    Pprinc.add(fpa_dia2E, null);
    Pprinc.add(fpa_dia3E, null);
    Pprinc.add(fpa_esgirE, null);
    Pprinc.add(cLabel13, null);
    Pprinc.add(Bcancelar, null);
    Pprinc.add(Baceptar, null);
  }

  public void iniciarVentana() throws Exception
  {
    Pprinc.setButton(KeyEvent.VK_F4,Baceptar);
    fpa_dia1E.setColumnaAlias("fpa_dia1");
    fpa_dia2E.setColumnaAlias("fpa_dia2");
    fpa_dia3E.setColumnaAlias("fpa_dia3");
    fpa_esgirE.setColumnaAlias("fpa_esgir");
    fpa_codiE.setColumnaAlias("fpa_codi");
    fpa_nombE.setColumnaAlias("fpa_nomb");
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
      fpa_codiE.setValorInt(dtCons.getInt("fpa_codi"));
      s="SELECT * FROM v_forpago WHERE fpa_codi = " +fpa_codiE.getValorInt();
      if (! dtCon1.select(s))
      {
        mensajeErr("FORMA PAGO NO ENCONTRADA ... SEGURAMENTE SE BORRO");
        return;
      }
      fpa_nombE.setText(dtCon1.getString("fpa_nomb"));
      fpa_dia1E.setValorInt(dtCon1.getInt("fpa_dia1"));
      fpa_dia2E.setValorInt(dtCon1.getInt("fpa_dia2"));
      fpa_dia3E.setValorInt(dtCon1.getInt("fpa_dia3"));
      fpa_esgirE.setValor(dtCon1.getString("fpa_esgir"));
    } catch (Exception k)
    {
      Error("Error al ver Datos",k);
      return;
    }
  }

  public void activar(boolean enab)
  {
    fpa_dia1E.setEnabled(enab);
    fpa_dia1E.setEnabled(enab);
    fpa_dia2E.setEnabled(enab);
    fpa_dia3E.setEnabled(enab);
    fpa_esgirE.setEnabled(enab);
    fpa_codiE.setEnabled(enab);
    fpa_nombE.setEnabled(enab);

    Baceptar.setEnabled(enab);
    Bcancelar.setEnabled(enab);
  }
  public void afterConecta() throws SQLException,java.text.ParseException
  {
    fpa_esgirE.addItem("No","0");
    fpa_esgirE.addItem("Si","-1");
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
    fpa_codiE.requestFocus();
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
    v.addElement(fpa_dia1E.getStrQuery());
    v.addElement(fpa_dia2E.getStrQuery());
    v.addElement(fpa_dia3E.getStrQuery());
    v.addElement(fpa_esgirE.getStrQuery());
    v.addElement(fpa_codiE.getStrQuery());
    v.addElement(fpa_nombE.getStrQuery());
    s = "SELECT * FROM v_forpago ";
    s = creaWhere(s, v,true);
    s+=" ORDER BY fpa_codi ";
    Pprinc.setQuery(false);
    debug(s);
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
      fatalError("Error al buscar Formas de Pago: ", ex);
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
    if (! checkRegistro())
      return;
    activar(true);
    fpa_codiE.setEnabled(false);
    fpa_nombE.requestFocus();
    mensajeErr("");
    mensaje("Editando registro ...");
  }

  public void ej_edit1()
  {
    try
    {
      dtAdd.edit();
      actValores(dtAdd);
      dtAdd.update(stUp);
      resetBloqueo(dtAdd, "v_forpago",""+ fpa_codiE.getValorInt(),false);
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
      resetBloqueo(dtAdd, "v_forpago",""+ fpa_codiE.getValorInt(), true);
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
    try
    {
      s="SELECT MAX(fpa_codi) as fpa_codi from v_forpago ";
      dtStat.select(s);
      fpa_codiE.setValorInt(dtStat.getInt("fpa_codi",true)+1);
    } catch (Exception k)
    {
      Error("Error al buscar siguiente N� Forma pago",k);
    }
    fpa_codiE.requestFocus();
    mensajeErr("");
    mensaje("Insertando Registro  ...");
  }
  public boolean checkAddNew()
  {
    try {
      if (fpa_codiE.getValorInt()==0)
      {
        mensajeErr("FORMA DE PAGO ... NO VALIDO");
        fpa_codiE.requestFocus();
        return false;
      }
      if (fpa_nombE.isNull())
      {
        mensajeErr("Introduzca nombre forma de pago");
        fpa_nombE.requestFocus();
        return false;
      }

      if (fpa_dia2E.getValorInt()>0 && fpa_dia1E.getValorInt() ==0)
      {
        mensajeErr("NO puede introducir segudno d�a de pago si no hay primero");
        fpa_dia2E.requestFocus();
        return false;
      }

      if (fpa_dia2E.getValorInt()>0 && fpa_dia2E.getValorInt() <= fpa_dia1E.getValorInt())
      {
        mensajeErr("Segundo Dia de Pago debe ser superior al primero");
        fpa_dia2E.requestFocus();
        return false;
      }
      if (fpa_dia3E.getValorInt()>0 && fpa_dia2E.getValorInt() ==0)
      {
        mensajeErr("NO puede introducir tercer d�a de pago si no hay segundo");
        fpa_dia2E.requestFocus();
        return false;
      }

      if (fpa_dia3E.getValorInt()>0 && fpa_dia3E.getValorInt() <= fpa_dia2E.getValorInt())
      {
        mensajeErr("Tercer Dia de Pago debe ser superior  al segundo");
        fpa_dia3E.requestFocus();
        return false;
      }
      s="SELECT * FROM v_forpago where fpa_dia1= "+fpa_dia1E.getValorInt()+
          " and fpa_dia2= "+fpa_dia2E.getValorInt()+
          " and fpa_dia3= "+fpa_dia3E.getValorInt()+
          " and fpa_esgir = "+fpa_esgirE.getValor()+
          (nav.pulsado==nav.EDIT?" and fpa_codi != "+fpa_codiE.getValorInt():"");
      if (dtStat.select(s))
      {
        int resp=mensajes.mensajeYesNo("Forma de Pago: "+dtStat.getInt("fpa_codi")+" -> "+
                                  dtStat.getString("fpa_nomb")+"\n YA TIENE ESTAS FORMAS DE PAGO\n"+
                                  " Cancelar insercion ?");
        if (resp!=mensajes.NO)
          return false;
      }
    } catch (Exception k)
    {
      Error("Error al comprobar campos",k);
      return false;
    }
    return true;
  }
  public void ej_addnew1()
  {
    try
    {
      s="SELECT * FROM v_forpago WHERE fpa_codi = "+fpa_codiE.getValorInt();
      if (dtCon1.select(s))
      {
        mensajeErr("FORMA DE PAGO ... YA EXISTE");
        return;
      }

      dtAdd.addNew("v_forpago");
      dtAdd.setDato("fpa_codi",fpa_codiE.getText());
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
    mensajeErr("Forma de Pago ... Insertada");

    activaTodo();
  }

  void actValores(DatosTabla dt) throws Exception
  {
    dt.setDato("fpa_nomb", fpa_nombE.getText());
    dt.setDato("fpa_dia1", fpa_dia1E.getValorInt());
    dt.setDato("fpa_dia2", fpa_dia2E.getValorInt());
    dt.setDato("fpa_dia3", fpa_dia3E.getValorInt());
    dt.setDato("fpa_esgir",fpa_esgirE.getValor());
  }
  public void canc_addnew()
  {
    mensaje("");
    activaTodo();
    mensajeErr("Insercion de Datos Cancelada");
    verDatos();
  }

  private boolean checkRegistro()
  {
    try
    {
      if (!setBloqueo(dtAdd, "v_forpago",""+ fpa_codiE.getValorInt()))
      {
        msgBox(msgBloqueo);
        nav.pulsado = navegador.NINGUNO;
        mensaje("");
        activaTodo();
        return false;
      }
      if (!dtAdd.select("select * from v_forpago where fpa_codi = " + fpa_codiE.getValorInt(),true))
      {
        mensajeErr("Registro ha sido borrado");
        resetBloqueo(dtAdd,"v_forpago",""+ fpa_codiE.getValorInt());
        activaTodo();
        nav.pulsado = navegador.NINGUNO;
        mensaje("");
        return false;
      }
    }
    catch (Exception k)
    {
      Error("Error al bloquear el registro", k);
      return false;
    }
    return true;
  }

  public void PADDelete()
  {
    mensaje("Borrar Registro ...");
    mensajeErr("");
    if (!checkRegistro())
      return;
    Baceptar.setEnabled(true);
    Bcancelar.setEnabled(true);
    Bcancelar.requestFocus();
  }

  public void ej_delete1()
  {
    try
    {
      dtAdd.delete(stUp);
      resetBloqueo(dtAdd,"v_forpago",""+ fpa_codiE.getValorInt(),false);
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
      resetBloqueo(dtAdd,"v_forpago",""+ fpa_codiE.getValorInt(), true);
    } catch (Exception k)
    {
      Error("Error al Anular bloqueo sobre tabla forma de pago",k);
    }
    mensajeErr("Borrado  de Datos Cancelada");
//    verDatos();
  }

}

