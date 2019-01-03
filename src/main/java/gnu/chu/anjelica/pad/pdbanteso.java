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
import gnu.chu.camposdb.*;
import javax.swing.SwingConstants;

/**
 *
 * <p>Título: pdbanteso </p>
 * <p>Descripción: Mantenimiento Bancos de Tesoreria</p>
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
public class pdbanteso extends ventanaPad   implements PAD
{
  String s;
  CPanel Pprinc = new CPanel();
  CLabel usu_nombreL = new CLabel();
  CTextField bat_codiE = new CTextField(Types.CHAR,"X",30);
  CTextField bat_nombE = new CTextField(Types.CHAR,"X",50);
  CLabel cLabel5 = new CLabel();
  CTextField bat_nifE = new CTextField(Types.CHAR,"X",9);
  boolean modConsulta=false;
//  CButton Baceptar = new CButton();
//  CButton Bcancelar = new CButton();
  CLabel cLabel4 = new CLabel();
  CTextField bat_nomsucE = new CTextField(Types.CHAR,"X",50);
  CLabel cLabel7 = new CLabel();
  CTextField bat_titcueE = new CTextField(Types.CHAR,"X",50);
  CLabel cLabel9 = new CLabel();
  CTextField bat_nifsufE = new CTextField(Types.DECIMAL,"##9");
  CTextField bat_numcueE = new CTextField(Types.DECIMAL,"9999999999");
  CLabel ban_codiL = new CLabel();
  CLabel cLabel13 = new CLabel();
  CTextField bat_cusubaE = new CTextField(Types.DECIMAL,"9999");
  banPanel bat_cucobaE = new banPanel();
  CLabel cLabel12 = new CLabel();
  CLabel cLabel14 = new CLabel();
  CTextField bat_digicoE = new CTextField(Types.DECIMAL,"99");
  CLabel cLabel21 = new CLabel();
  CTextField bat_cueconE = new CTextField(Types.CHAR,"X",12);
  CLabel cLabel1 = new CLabel();
  CTextField bat_limcreE = new CTextField(Types.DECIMAL,"--,---,--9.99");

  public pdbanteso(EntornoUsuario eu, Principal p)
  {
    this(eu, p, null);
  }

  public pdbanteso(EntornoUsuario eu, Principal p,Hashtable ht)
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
      setTitulo("Mant. Bancos de Tesoreria" + (modConsulta ? "SOLO LECTURA" : ""));
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

  public pdbanteso(gnu.chu.anjelica.menu p, EntornoUsuario eu,Hashtable ht)
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
      setTitulo("Mant. Bancos de Tesoreria" + (modConsulta ? "SOLO LECTURA" : ""));
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
    this.setSize(new Dimension(491, 288));
    this.setVersion("2006-07-13");
    strSql = "SELECT * FROM bancteso ORDER BY bat_codi ";



    statusBar = new StatusBar(this);
    conecta();
    nav = new navegador(this, dtCons, false, modConsulta? navegador.CURYCON:navegador.NORMAL);
    Iniciar(this);
    cLabel5.setHorizontalAlignment(SwingConstants.RIGHT);
    ban_codiL.setHorizontalAlignment(SwingConstants.RIGHT);
    cLabel13.setHorizontalAlignment(SwingConstants.RIGHT);
    cLabel21.setHorizontalAlignment(SwingConstants.RIGHT);

    Pprinc.setDefButton(Baceptar);
    Pprinc.setEscButton(Bcancelar);
    Pprinc.setLayout(null);
    usu_nombreL.setText("Banco");
    usu_nombreL.setBounds(new Rectangle(4, 3, 47, 17));
    cLabel5.setText("NIF");
    cLabel5.setBounds(new Rectangle(70, 69, 27, 17));
    cLabel4.setText("Sucursal");
    cLabel4.setBounds(new Rectangle(48, 24, 56, 16));
    cLabel7.setText("Titular de Cuenta");
    cLabel7.setBounds(new Rectangle(9, 43, 97, 16));
    cLabel9.setText("Sufijo");
    cLabel9.setBounds(new Rectangle(391, 69, 48, 17));
    ban_codiL.setText("Banco");
    ban_codiL.setBounds(new Rectangle(55, 89, 42, 17));
    cLabel13.setText("Digito Control");
    cLabel13.setBounds(new Rectangle(11, 109, 86, 17));
    cLabel12.setText("Oficina");
    cLabel12.setBounds(new Rectangle(379, 89, 49, 17));
    cLabel14.setText("Num.Cuenta");
    cLabel14.setBounds(new Rectangle(310, 109, 75, 17));
    cLabel21.setText("Cuenta Contable");
    cLabel21.setBounds(new Rectangle(-2, 131, 99, 19));
    cLabel1.setText("Limite Credito");
    cLabel1.setBounds(new Rectangle(306, 131, 86, 17));
    Bcancelar.setBounds(new Rectangle(260, 160, 106, 27));
    Baceptar.setBounds(new Rectangle(102, 160, 106, 27));
    bat_cueconE.setBounds(new Rectangle(103, 131, 95, 19));
    bat_limcreE.setBounds(new Rectangle(389, 131, 83, 17));
    bat_numcueE.setBounds(new Rectangle(387, 109, 85, 17));
    bat_digicoE.setBounds(new Rectangle(104, 109, 26, 17));
    bat_cucobaE.setBounds(new Rectangle(103, 89, 257, 18));
    bat_cusubaE.setBounds(new Rectangle(431, 89, 41, 17));
    bat_nifsufE.setBounds(new Rectangle(433, 69, 36, 17));
    bat_nifE.setBounds(new Rectangle(103, 69, 87, 17));
    bat_titcueE.setBounds(new Rectangle(108, 41, 364, 17));
    bat_nomsucE.setBounds(new Rectangle(109, 22, 364, 17));
    bat_nombE.setBounds(new Rectangle(109, 3, 364, 17));
    bat_codiE.setBounds(new Rectangle(57, 3, 49, 17));
    this.getContentPane().add(nav, BorderLayout.NORTH);
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);

    Pprinc.add(usu_nombreL, null);
    Pprinc.add(bat_codiE, null);
    Pprinc.add(bat_nombE, null);
    Pprinc.add(cLabel4, null);
    Pprinc.add(bat_nomsucE, null);
    Pprinc.add(cLabel7, null);
    Pprinc.add(bat_titcueE, null);
    Pprinc.add(bat_cusubaE, null);
    Pprinc.add(cLabel12, null);
    Pprinc.add(bat_numcueE, null);
    Pprinc.add(cLabel14, null);
    Pprinc.add(bat_cueconE, null);
    Pprinc.add(bat_digicoE, null);
    Pprinc.add(bat_cucobaE, null);
    Pprinc.add(cLabel1, null);
    Pprinc.add(bat_limcreE, null);
    Pprinc.add(Bcancelar, null);
    Pprinc.add(Baceptar, null);
    Pprinc.add(bat_nifsufE, null);
    Pprinc.add(cLabel9, null);
    Pprinc.add(bat_nifE, null);
    Pprinc.add(cLabel5, null);
    Pprinc.add(ban_codiL, null);
    Pprinc.add(cLabel13, null);
    Pprinc.add(cLabel21, null);

  }

  public void iniciarVentana() throws Exception
  {
    bat_cucobaE.iniciar(dtStat,this,vl,EU);

    Pprinc.setButton(KeyEvent.VK_F4,Baceptar);
    bat_codiE.setColumnaAlias("bat_codi");
    bat_nombE.setColumnaAlias("bat_nomb");
    bat_nomsucE.setColumnaAlias("bat_nomsuc");
    bat_titcueE.setColumnaAlias("bat_titcue");
    bat_nifsufE.setColumnaAlias("bat_nifsuf");
    bat_nifE.setColumnaAlias("bat_nif");
    bat_cucobaE.setColumnaAlias("bat_cucoba");
    bat_cusubaE.setColumnaAlias("bat_cusuba");
    bat_digicoE.setColumnaAlias("bat_digico");
    bat_numcueE.setColumnaAlias("bat_numcue");
    bat_cueconE.setColumnaAlias("bat_cuecon");
    bat_limcreE.setColumnaAlias("bat_limcre");

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
      bat_codiE.setText(dtCons.getString("bat_codi"));
      s="SELECT * FROM bancteso WHERE bat_codi = '" +bat_codiE.getText()+"'";
      if (! dtCon1.select(s))
      {
        mensajeErr("Banco NO ENCONTRADO ... SEGURAMENTE SE BORRO");
        Pprinc.resetTexto();
        bat_codiE.setText(dtCons.getString("bat_codi"));
        return;
      }
      bat_nombE.setText(dtCon1.getString("bat_nomb"));
      bat_nomsucE.setText(dtCon1.getString("bat_nomsuc"));
      bat_titcueE.setText(dtCon1.getString("bat_titcue"));
      bat_nifsufE.setText(dtCon1.getString("bat_nifsuf"));
      bat_nifE.setText(dtCon1.getString("bat_nif"));
      bat_cucobaE.setText(dtCon1.getString("bat_cucoba"));
      bat_cusubaE.setText(dtCon1.getString("bat_cusuba"));
      bat_digicoE.setText(dtCon1.getString("bat_digico"));
      bat_numcueE.setText(Formatear.format(dtCon1.getDouble("bat_numcue"), "##########"));
      bat_cueconE.setText(dtCon1.getString("bat_cuecon"));
      bat_limcreE.setText(dtCon1.getString("bat_limcre"));

    } catch (Exception k)
    {
      Error("Error al ver Datos",k);
      return;
    }
  }

  public void activar(boolean enab)
  {
    bat_codiE.setEnabled(enab);
    bat_nombE.setEnabled(enab);
    bat_nomsucE.setEnabled(enab);
    bat_titcueE.setEnabled(enab);
    bat_nifsufE.setEnabled(enab);
    bat_nifE.setEnabled(enab);
    bat_cucobaE.setEnabled(enab);
    bat_cusubaE.setEnabled(enab);
    bat_digicoE.setEnabled(enab);
    bat_numcueE.setEnabled(enab);
    bat_cueconE.setEnabled(enab);
    bat_limcreE.setEnabled(enab);


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
    v.addElement(bat_codiE.getStrQuery());
    v.addElement(bat_nombE.getStrQuery());
    v.addElement(bat_nomsucE.getStrQuery());

    v.addElement(bat_titcueE.getStrQuery());
    v.addElement(bat_nifsufE.getStrQuery());
    v.addElement(bat_nifE.getStrQuery());
    v.addElement(bat_cucobaE.getStrQuery());
    v.addElement(bat_cusubaE.getStrQuery());
    v.addElement(bat_digicoE.getStrQuery());
    v.addElement(bat_numcueE.getStrQuery());
    v.addElement(bat_cueconE.getStrQuery());
    v.addElement(bat_limcreE.getStrQuery());

    s = "SELECT * FROM bancteso ";
    s = creaWhere(s, v,true);
    s+=" ORDER BY bat_codi ";
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
    bat_codiE.setEnabled(false);
    try
    {
      if (!setBloqueo(dtAdd, "bancteso",bat_codiE.getText()))
      {
        msgBox(msgBloqueo);
        nav.pulsado = navegador.NINGUNO;
        activaTodo();
        dtAdd.getConexion().rollback();
        return;
      }
      if (! dtAdd.select("select * from bancteso where bat_codi= "+bat_codiE.getValorInt(),true))
      {
        mensajeErr("Registro ha sido borrado");
        resetBloqueo(dtAdd, "bancteso", bat_codiE.getText());
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
    bat_nombE.requestFocus();
  }

  public void ej_edit1()
  {
    try
    {
      dtAdd.edit();
      actValores(dtAdd);
      dtAdd.update(stUp);
      resetBloqueo(dtAdd, "bancteso",bat_codiE.getText(),false);
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
      resetBloqueo(dtAdd, "bancteso", bat_codiE.getText(), true);
    } catch (Exception ex)
    {
      Error("Error al Quitar Bloqeuo", ex);
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
    bat_codiE.requestFocus();
    mensaje("Insertando Banco ...");
  }
  public boolean checkAddNew()
  {

    if (bat_codiE.isNull())
    {
      mensajeErr("Introduzca Codigo de Banco");
      bat_codiE.requestFocus();
      return false;
    }
    if (bat_nombE.isNull())
    {
      mensajeErr("Introduzca Nombre de Banco");
      bat_nombE.requestFocus();
      return false;
    }
    if (bat_titcueE.isNull())
    {
      mensajeErr("Introduzca Titular de Cuenta");
      bat_titcueE.requestFocus();
      return false;
    }
    if (bat_nifE.isNull())
    {
      mensajeErr("Introduzca NIF del Titular de Cuenta");
      bat_nifE.requestFocus();
      return false;
    }
    if (bat_cucobaE.getValorInt()==0)
    {
      mensajeErr("Introduzca CODIGO de Banco");
      bat_cucobaE.requestFocus();
      return false;
    }
    if (bat_numcueE.getValorInt() == 0)
    {
      mensajeErr("Introduzca Numero de Cuenta");
      bat_numcueE.requestFocus();
      return false;
    }
      String cuenta=Formatear.format(bat_cucobaE.getValorInt(),"9999")+
          Formatear.format(bat_cusubaE.getValorInt(),"9999");
      if (Formatear.getNumControl(Integer.parseInt(cuenta)) !=
          Integer.parseInt(Formatear.format(bat_digicoE.getValorInt(), "99").
                           substring(0, 1)))
      {
        mensajeErr("Digito control NO VALIDO para banco y sucursal");
        bat_digicoE.requestFocus();
        return false;
      }
      if (Formatear.getNumControl(bat_numcueE.getValorDec()) !=
          Integer.parseInt(Formatear.format(bat_digicoE.getValorInt(), "99").
                           substring(1, 2)))
      {
        mensajeErr("Digito control NO VALIDO para Num. Cuenta");
        bat_digicoE.requestFocus();
        return false;
      }

    return true;
  }
  public void ej_addnew1()
  {
    try
    {
      s="SELECT * FROM bancteso WHERE bat_codi = '"+bat_codiE.getText()+"'";
      if (dtCon1.select(s))
      {
        mensajeErr("Banco YA EXISTE");
        return;
      }
      dtAdd.addNew("bancteso");
      dtAdd.setDato("bat_codi",bat_codiE.getText());
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
     dt.setDato("bat_nomb",bat_nombE.getText());
     dt.setDato("bat_nomsuc",bat_nomsucE.getText());
     dt.setDato("bat_titcue",bat_titcueE.getText());
     dt.setDato("bat_nifsuf",bat_nifsufE.getValorInt());
     dt.setDato("bat_nif",bat_nifE.getText());
     dt.setDato("bat_cucoba",bat_cucobaE.getValorInt());
     dt.setDato("bat_cusuba",bat_cusubaE.getValorInt());
     dt.setDato("bat_digico",bat_digicoE.getValorInt());
     dt.setDato("bat_numcue",bat_numcueE.getText());
     dt.setDato("bat_cuecon",bat_cueconE.getValorInt());
     dt.setDato("bat_limcre",bat_limcreE.getValorDec());

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

      if (!setBloqueo(dtAdd, "bancteso", bat_codiE.getText()))
      {
        msgBox(msgBloqueo);
        nav.pulsado = navegador.NINGUNO;
        activaTodo();
        return;
      }
      if (!dtAdd.select("select * from bancteso where bat_codi= " +bat_codiE.getValorInt(), true))
      {
        mensajeErr("Registro ha sido borrado");
        resetBloqueo(dtAdd, "bancteso", bat_codiE.getText());
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
      resetBloqueo(dtAdd, "bancteso",bat_codiE.getText(),false);
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
      resetBloqueo(dtAdd, "bancteso", bat_codiE.getText(), true);
    } catch (Exception k)
    {
      Error("Error al Anular bloqueo sobre tabla bancos",k);
    }
    mensajeErr("Borrado  de Datos Cancelada");
    verDatos();
  }


}
