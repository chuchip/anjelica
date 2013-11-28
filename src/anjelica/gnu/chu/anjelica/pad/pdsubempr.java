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
 * <p>Título: pdsubempr </p>
 * <p>Descripción: Mantenimiento SubEmpresas</p>
 * <p>Empresa: miSL</p>
 * <p>Copyright: Copyright (c) 2005-2011
 *    Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
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
 * @author Fernando Gallego Santos
 * @version 1.0
 */

public class pdsubempr    extends ventanaPad    implements PAD
{
  String s;

  CPanel Pprinc = new CPanel();
  CLabel sbe_codiL = new CLabel();
  CLabel sbe_nombL = new CLabel();
  CTextField sbe_codiE = new CTextField(Types.DECIMAL, "#9");
  CTextField sbe_nombE = new CTextField(Types.CHAR, "X", 40);
  CLabel sbe_tipoL = new CLabel("Tipo");
  CComboBox sbe_tipoE = new CComboBox();
  boolean modConsulta = false;
  CLabel cLabel1 = new CLabel();
  CLinkBox emp_codiE = new CLinkBox();

//  CButton Baceptar = new CButton();
//  CButton Bcancelar = new CButton();

  public pdsubempr(EntornoUsuario eu, Principal p)

  {

    this(eu, p, null);
  }

  public pdsubempr(EntornoUsuario eu, Principal p, Hashtable ht)

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

      setTitulo("Mant. SubEmpresas");

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

  public pdsubempr(gnu.chu.anjelica.menu p, EntornoUsuario eu, Hashtable ht)
  {
    EU = eu;
    vl = p.getLayeredPane();
    eje = false;
    try

    {
      if (ht != null)

      {
        if (ht.get("modConsulta") != null)
          modConsulta = Boolean.valueOf(ht.get("modConsulta").toString()). booleanValue();
      }
     setTitulo("Mant. SubEmpresas");
      jbInit();
    } catch (Exception e)
    {
      e.printStackTrace();
      setErrorInit(true);
    }
  }

  private void jbInit() throws Exception
  {
    iniciarFrame();
    this.setSize(new Dimension(426, 233));

    strSql = "SELECT * FROM subempresa ORDER BY emp_codi,sbe_codi ";
    this.setVersion("2011-04-10"+(modConsulta?"SOLO LECTURA":""));
    statusBar = new StatusBar(this);
    conecta();

    nav = new navegador(this, dtCons, false,
                        modConsulta ? navegador.CURYCON : navegador.NORMAL);

    iniciar(this);
    this.setMaximizable(false);
    this.setResizable(false);
//    pai_nombE.setMayusc(true);
    Pprinc.setDefButton(Baceptar);
    Pprinc.setEscButton(Bcancelar);
    Pprinc.setLayout(null);
    
    sbe_codiL.setText("SubEmpresa");
    sbe_codiL.setBounds(new Rectangle(4, 26, 80, 17));
    sbe_nombL.setBounds(new Rectangle(3, 46, 70, 15));
    sbe_tipoL.setBounds(new Rectangle(240, 26, 40, 17));
    sbe_tipoE.setBounds(new Rectangle(290, 26, 80, 17));
    Bcancelar.setBounds(new Rectangle(235, 68, 106, 27));
    Baceptar.setBounds(new Rectangle(77, 68, 106, 27));
   
    sbe_nombE.setBounds(new Rectangle(83, 45, 321, 17));
    sbe_codiE.setBounds(new Rectangle(83, 26, 49, 17));
    sbe_codiE.setCeroIsNull(true);

    sbe_nombL.setText("Nombre");

   
    cLabel1.setRequestFocusEnabled(true);
    cLabel1.setText("Empresa");
    cLabel1.setBounds(new Rectangle(4, 2, 71, 19));
    emp_codiE.setBounds(new Rectangle(83, 3, 294, 19));
    emp_codiE.setAncTexto(30);

    this.getContentPane().add(nav, BorderLayout.NORTH);
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);

    Pprinc.add(sbe_nombL, null);
    Pprinc.add(sbe_nombE, null);
    Pprinc.add(cLabel1, null);
    Pprinc.add(emp_codiE, null);
    Pprinc.add(sbe_codiE, null);
    Pprinc.add(sbe_codiL, null);
    Pprinc.add(sbe_tipoL, null);
    Pprinc.add(sbe_tipoE, null);
    Pprinc.add(Bcancelar, null);
    Pprinc.add(Baceptar, null);
  }

  public void iniciarVentana() throws Exception

  {
    emp_codiE.setFormato(Types.DECIMAL, "#9");
    s = "SELECT emp_codi,emp_nomb FROM v_empresa order by emp_codi";
    dtStat.select(s);
    emp_codiE.addDatos(dtStat);
    sbe_tipoE.addItem("Cliente", "C");
    sbe_tipoE.addItem("Articulo", "A");
    Pprinc.setButton(KeyEvent.VK_F4, Baceptar);

    emp_codiE.setColumnaAlias("sbe_codi");

    sbe_codiE.setColumnaAlias("sbe_codi");

    sbe_nombE.setColumnaAlias("sbe_nomb");
    sbe_tipoE.setColumnaAlias("sbe_tipo");
    activarEventos();
    activaTodo();
    verDatos();
  }

  void activarEventos()
  {
  }

  void verDatos()
  {
    try
    {
      if (dtCons.getNOREG())
        return;

      sbe_codiE.setText(dtCons.getString("sbe_codi"));
      emp_codiE.setText(dtCons.getString("emp_codi"));
// Si el codigo es numerico Usar getValorInt() no getText()
      s = "SELECT * FROM subempresa WHERE emp_codi = " +  emp_codiE.getValorInt()+
          " and sbe_codi = "+  sbe_codiE.getValorInt();
      if (!dtCon1.select(s))
      {
        mensajeErr("SubEmpresa NO ENCONTRADA ... SEGURAMENTE SE BORRO");
        Pprinc.resetTexto();
        sbe_codiE.setText(dtCons.getString("pai_codi"));
        emp_codiE.setText(dtCons.getString("emp_codi"));
        return;
      }
      sbe_tipoE.setValor(dtCon1.getString("sbe_tipo"));
      sbe_nombE.setText(dtCon1.getString("sbe_nomb"));
    }  catch (Exception k)
    {
      Error("Error al ver Datos", k);
      return;
    }
  }

  public void activar(boolean enab)
  {
    sbe_codiE.setEnabled(enab);
    emp_codiE.setEnabled(enab);
    sbe_nombE.setEnabled(enab);
    sbe_tipoE.setEnabled(enab);
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
    sbe_codiE.requestFocus(); // Poner el foco al primer campo
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
    v.addElement(sbe_codiE.getStrQuery());
    v.addElement(emp_codiE.getStrQuery());
    v.addElement(sbe_nombE.getStrQuery());
    v.addElement(sbe_tipoE.getStrQuery());
    s = "SELECT * FROM subempresa ";
    s = creaWhere(s, v, true);
    s += " ORDER BY emp_codi,sbe_codi ";
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
      fatalError("Error al buscar SubEmpresa: ", ex);
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
    sbe_codiE.setEnabled(false);
    try
    {
      if (!setBloqueo(dtAdd, "subempresa", emp_codiE.getValorInt()+"-"+sbe_codiE.getText()))
      {
        msgBox(msgBloqueo);
        nav.pulsado = navegador.NINGUNO;
        activaTodo();
        dtAdd.getConexion().rollback();
        return;
      }

      if (!dtAdd.select("select * from subempresa where sbe_codi= " +
                        sbe_codiE.getValorInt()+
                        " and emp_codi = "+emp_codiE.getValorInt(), true))
      {
        mensajeErr("Registro ha sido borrado");
        resetBloqueo(dtAdd, "subempresa", emp_codiE.getValorInt()+"-"+sbe_codiE.getText());
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
    sbe_nombE.requestFocus();
  }

  public void ej_edit1()
  {
    try
    {
      dtAdd.edit();
      actValores(dtAdd);
      dtAdd.update(stUp);
      resetBloqueo(dtAdd, "subempresa", emp_codiE.getValorInt()+"-"+sbe_codiE.getText(), false);
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
    try
    {
      resetBloqueo(dtAdd, "subempresa", emp_codiE.getValorInt()+"-"+sbe_codiE.getText(), true);
    }
    catch (Exception ex)
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
    emp_codiE.setValorInt(EU.em_cod);
    activar(true);
    sbe_codiE.requestFocus();
    mensaje("Insertando SubEmpresa ...");
  }

  public boolean checkAddNew()
  {
    if (emp_codiE.isNull())
    {
      mensajeErr("Introduzca Codigo  de Empresa");
      emp_codiE.requestFocus();
      return false;
    }

    if (sbe_codiE.isNull())
    {
      try {
        s="SELECT max(sbe_codi) as sbe_codi  from subempresa where emp_codi "+emp_codiE.getValorInt();
        dtStat.select(s);
        sbe_codiE.setValorInt(dtStat.getInt("sbe_codi",true)+1);
        mensajeErr("Introduzca Codigo de SubEmpresa (PUESTO EL ULTIMO + 1)");
        sbe_codiE.requestFocus();
        return false;
      } catch (Exception k)
      {
        Error("Error al buscar Cod. SubEmpresa Maximo",k);
      }
    }



    if (sbe_nombE.isNull())
    {
      mensajeErr("Introduzca Nombre de SubEmpresa");
      sbe_nombE.requestFocus();
      return false;
    }

    return true;
  }

  public void ej_addnew1()
  {
    try
    {
      // Comprobamos que no existe el c�digo
      s = "SELECT * FROM subempresa WHERE sbe_codi = " + sbe_codiE.getValorInt()+
          " and emp_codi = "+emp_codiE.getValorInt();
      if (dtCon1.select(s))
      {
        mensajeErr("Codigo de subEmpresa YA EXISTE");
        sbe_codiE.requestFocus();
        return;
      }


      // Comprobamos que no existe el nombre de la SubEmpresa
      s = "SELECT * FROM subempresa WHERE UPPER(sbe_nomb) = '" +
          sbe_nombE.getText().trim().toUpperCase() + "'"+
          " and sbe_tipo = '"+sbe_tipoE.getValor()+"'";

      if (dtCon1.select(s))
      {
        mensajeErr("Nombre de SUBEmpresa YA EXISTE");
        sbe_nombE.requestFocus();
        return;
      }
      // A�adimos el Registro
      dtAdd.addNew("subempresa");
      actValores(dtAdd);
      dtAdd.update(stUp);
      ctUp.commit();
    } catch (Exception ex)
    {
      Error("Error al Insertar datos", ex);
      return;
    }

    mensaje("");
    mensajeErr("SubEmpresa ... Insertada");
    activaTodo();
  }

  void actValores(DatosTabla dt) throws Exception
  {
    dt.setDato("emp_codi", emp_codiE.getValorInt());
    dt.setDato("sbe_codi", sbe_codiE.getValorInt());
    dt.setDato("sbe_nomb", sbe_nombE.getText());
    dt.setDato("sbe_tipo", sbe_tipoE.getValor());
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
      if (!setBloqueo(dtAdd, "subempresa", emp_codiE.getValorInt()+"-"+sbe_codiE.getText()))
      {
        msgBox(msgBloqueo);
        nav.pulsado = navegador.NINGUNO;
        activaTodo();
        return;
      }

      if (!dtAdd.select("select * from subempresa where sbe_codi= " +
                        sbe_codiE.getValorInt()+
                        " and emp_codi = "+emp_codiE.getValorInt(), true))
      {
        mensajeErr("Registro ha sido borrado");
        resetBloqueo(dtAdd,  "subempresa", emp_codiE.getValorInt()+"-"+sbe_codiE.getText());
        activaTodo();
        mensaje("");
        return;
      }
    } catch (Exception k)
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
      resetBloqueo(dtAdd,  "subempresa", emp_codiE.getValorInt()+"-"+sbe_codiE.getText(), false);
      ctUp.commit();
      rgSelect();
    }
    catch (Exception ex)
    {
      Error("Error al borrar Registro", ex);
    }
    activaTodo();
    verDatos();
    mensaje("");
    mensajeErr("Registro ... Borrado");
  }

  public void canc_delete()
  {
    mensaje("");
    activaTodo();
    try
    {
      resetBloqueo(dtAdd,  "subempresa", emp_codiE.getValorInt()+"-"+sbe_codiE.getText(), true);
    }
    catch (Exception k)
    {
      Error("Error al Anular bloqueo sobre tabla SubEmpresas", k);
    }
    mensajeErr("Borrado de Datos Cancelada");
    verDatos();
  }
}
