  package gnu.chu.anjelica.pad;

import gnu.chu.controles.*;
import gnu.chu.sql.*;
import java.sql.*;
import gnu.chu.Menu.*;
import java.awt.*;
import java.util.*;
import gnu.chu.interfaces.*;
import gnu.chu.utilidades.*;
import java.awt.event.*;
import javax.swing.BorderFactory;

/**
 *
 * <p>Título: PDTRANSP </p>
 * <p>Descripción: Mantenimiento Tabla de Transportistas</p>
 * <p>Empresa: MISL</p>
 * <p>Copyright: Copyright (c) 2005-2016
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
 * @version 2.0
 */
public class pdtransp extends ventanaPad   implements PAD
{
  String s;
  CPanel Pprinc = new CPanel();
  CTextField tra_nombE = new CTextField(Types.CHAR,"X",100);
  public boolean modConsulta=false;
//  CButton Baceptar = new CButton();
//  CButton Bcancelar = new CButton();
  CLabel cLabel7 = new CLabel();
  CTextField tra_codiE = new CTextField(Types.DECIMAL,"###9");
  CTextField tra_poblE = new CTextField(Types.CHAR,"X",30);
  CTextField tra_codposE = new CTextField(Types.DECIMAL,"#99999");
  CLabel cLabel4 = new CLabel();
  CTextField tra_telefE = new CTextField(Types.CHAR,"X",15);
  CLabel cLabel33 = new CLabel();
  CTextField tra_faxE = new CTextField(Types.CHAR,"X",15);
  CLabel cLabel8 = new CLabel();
  CLabel cLabel5 = new CLabel();
  CLabel cLabel3 = new CLabel();
  CTextField tra_direcE = new CTextField(Types.CHAR,"X",40);
  CLabel cli_nifL = new CLabel();
  CTextField tra_nifE = new CTextField(Types.CHAR,"X",15);
  CLabel cLabel1 = new CLabel();
  CTextField tra_vehicE = new CTextField(Types.CHAR,"X",30);
  CLabel cLabel2 = new CLabel();
  CTextField tra_matricE = new CTextField(Types.CHAR,"X",10);
  CLabel cLabel6 = new CLabel();
  CComboBox tra_tipcalE = new CComboBox();
  CPanel Ptarfija = new CPanel();
  CLabel cLabel9 = new CLabel();
  CTextField tra_prebasE = new CTextField(Types.DECIMAL,"###,##9.99");
  CLabel cLabel10 = new CLabel();
  CTextField tra_impkilE = new CTextField(Types.DECIMAL,"#,##9.999");
  CLabel cLabel11 = new CLabel();
  CTextField tra_porsegE = new CTextField(Types.DECIMAL,"#9.999");
  CLabel cLabel12 = new CLabel();
  CTextField tra_porreeE = new CTextField(Types.DECIMAL,"###,##9.99");
  CLabel cLabel13 = new CLabel();
  CTextField tra_aduoriE = new CTextField(Types.DECIMAL,"###,##9.99");
  CLabel cLabel14 = new CLabel();
  CTextField tra_adudesE = new CTextField(Types.DECIMAL,"###,##9.99");
  CLabel tra_tipoL = new CLabel("Tipo");
  CComboBox tra_tipoE = new CComboBox();
  public pdtransp(EntornoUsuario eu, Principal p)
  {
    this(eu,p,null);
  }

  public pdtransp(EntornoUsuario eu, Principal p, Hashtable ht)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    try
    {
      setTitulo("Mant. Transportistas");
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

  public pdtransp(gnu.chu.anjelica.menu p, EntornoUsuario eu)
  {
    EU = eu;
    vl = p.getLayeredPane();
    eje = false;

    try
    {
      setTitulo("Mant. Transportistas");

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
    this.setSize(new Dimension(436, 341));
    this.setVersion("20160315");

    strSql = "SELECT * FROM transportista  ORDER BY tra_codi ";

    statusBar = new StatusBar(this);
    conecta();

    nav = new navegador(this, dtCons, false, modConsulta? navegador.CURYCON:navegador.NORMAL);
    Iniciar(this);
    Pprinc.setMaximumSize(new Dimension(32767, 32767));
    Pprinc.setMinimumSize(new Dimension(471, 149));
    Pprinc.setDefButton(Baceptar);
    Pprinc.setEscButton(Bcancelar);
    Pprinc.setLayout(null);
    Bcancelar.setBounds(new Rectangle(223, 224, 133, 31));
    Baceptar.setBounds(new Rectangle(61, 224, 133, 31));
    tra_adudesE.setBounds(new Rectangle(347, 195, 75, 17));
    cLabel14.setBounds(new Rectangle(222, 195, 105, 17));
    tra_aduoriE.setBounds(new Rectangle(117, 195, 75, 17));
    cLabel13.setBounds(new Rectangle(2, 195, 106, 17));
    tra_porreeE.setBounds(new Rectangle(375, 174, 47, 17));
    cLabel12.setBounds(new Rectangle(222, 174, 143, 17));
    tra_porsegE.setBounds(new Rectangle(119, 174, 47, 17));
    cLabel11.setBounds(new Rectangle(3, 174, 117, 17));
    Ptarfija.setBounds(new Rectangle(27, 141, 350, 27));
    tra_tipcalE.setBounds(new Rectangle(317, 115, 105, 18));
    cLabel6.setBounds(new Rectangle(243, 115, 75, 17));
    tra_matricE.setBounds(new Rectangle(62, 115, 87, 17));
    cLabel2.setBounds(new Rectangle(2, 115, 59, 16));
    tra_vehicE.setBounds(new Rectangle(62, 97, 219, 16));
    cLabel1.setBounds(new Rectangle(0, 97, 59, 16));
    tra_faxE.setBounds(new Rectangle(293, 60, 129, 17));
    cLabel8.setBounds(new Rectangle(243, 60, 29, 17));
    tra_telefE.setBounds(new Rectangle(61, 60, 129, 17));
    cLabel5.setBounds(new Rectangle(1, 60, 56, 17));
    tra_direcE.setBounds(new Rectangle(61, 41, 316, 17));
    cLabel4.setBounds(new Rectangle(1, 41, 59, 17));
    tra_codposE.setBounds(new Rectangle(372, 22, 50, 17));
    cLabel33.setBounds(new Rectangle(311, 22, 61, 17));
    tra_poblE.setBounds(new Rectangle(61, 22, 227, 17));
    cLabel3.setBounds(new Rectangle(1, 22, 59, 17));
    tra_nombE.setBounds(new Rectangle(125, 1, 297, 17));
    tra_codiE.setBounds(new Rectangle(80, 1, 40, 17));
    cLabel7.setBounds(new Rectangle(1, 1, 79, 17));
    this.getContentPane().add(nav, BorderLayout.NORTH);
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);

    cLabel7.setText("Transportista");
    cLabel4.setText("Direccion");
    cLabel33.setText("Cod Postal");
    cLabel8.setText("Fax");
    cLabel5.setText("Telefono");
    cLabel3.setText("Poblacion");
    cli_nifL.setText("NIF");
    cli_nifL.setBounds(new Rectangle(15, 78, 32, 17));
    tra_nifE.setBounds(new Rectangle(62, 78, 129, 17));
    tra_tipoE.addItem("Compra","C");
    tra_tipoE.addItem("Venta","V");
    tra_tipoL.setBounds(new Rectangle(243, 78, 45, 17));
    tra_tipoE.setBounds(new Rectangle(293, 78, 80, 17));
    cLabel1.setText("Vehiculo");
    cLabel2.setText("Matricula");
    cLabel6.setRequestFocusEnabled(true);
    cLabel6.setText("Tipo Calculo");
    tra_tipcalE.addItem("Variable","2");
    tra_tipcalE.addItem("Fijo","1");
    Ptarfija.setBorder(BorderFactory.createLineBorder(Color.black));
    Ptarfija.setLayout(null);
    cLabel9.setText("Precio Base");
    cLabel9.setBounds(new Rectangle(10, 5, 70, 17));

    cLabel10.setText("Precio por Kilo");
    cLabel10.setBounds(new Rectangle(174, 5, 93, 17));
    tra_impkilE.setBounds(new Rectangle(260, 5, 75, 17));
    tra_prebasE.setBounds(new Rectangle(85, 5, 75, 17));
    cLabel11.setText("Incremento % Seguro");
    cLabel12.setText("% Incremento Reembolso");
    cLabel13.setText("Imp. Aduana Orig.");
    cLabel14.setText("Imp. Aduana Dest.");
    Ptarfija.add(cLabel10, null);
    Ptarfija.add(tra_impkilE, null);
    Ptarfija.add(tra_prebasE, null);
    Ptarfija.add(cLabel9, null);
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);

    Pprinc.add(cLabel7, null);
    Pprinc.add(tra_codiE, null);
    Pprinc.add(tra_nombE, null);
    Pprinc.add(tra_poblE, null);
    Pprinc.add(cLabel3, null);
    Pprinc.add(tra_direcE, null);
    Pprinc.add(cLabel4, null);
    Pprinc.add(cLabel33, null);
    Pprinc.add(tra_codposE, null);
    Pprinc.add(tra_faxE, null);
    Pprinc.add(cLabel8, null);
    Pprinc.add(tra_telefE, null);
    Pprinc.add(cLabel5, null);
    Pprinc.add(cli_nifL, null);
    Pprinc.add(tra_nifE, null);
    Pprinc.add(tra_tipoL, null);
    Pprinc.add(tra_tipoE, null);
    Pprinc.add(tra_tipcalE, null);
    Pprinc.add(Ptarfija, null);
    Pprinc.add(cLabel13, null);
    Pprinc.add(tra_aduoriE, null);
    Pprinc.add(tra_adudesE, null);
    Pprinc.add(cLabel14, null);
    Pprinc.add(Bcancelar, null);
    Pprinc.add(Baceptar, null);
    Pprinc.add(cLabel6, null);
    Pprinc.add(tra_vehicE, null);
    Pprinc.add(cLabel1, null);
    Pprinc.add(cLabel11, null);
    Pprinc.add(tra_porsegE, null);

    Pprinc.add(tra_matricE, null);
    Pprinc.add(cLabel2, null);
    Pprinc.add(tra_porreeE, null);
    Pprinc.add(cLabel12, null);
  }

  public void iniciarVentana() throws Exception
  {
/*
    Boolean b=new Boolean("true");
    Class c = this.getClass();
    Field f = c.getField("modConsulta");
    System.out.println("Typo: "+f.getType());
    System.out.println(f.getBoolean(this));
    f.setBoolean(this, true);
*/
    Pprinc.setButton(KeyEvent.VK_F4,Baceptar);
    tra_codiE.setColumnaAlias("tra_codi");
    tra_nombE.setColumnaAlias("tra_nomb");
    tra_direcE.setColumnaAlias("tra_direc");
    tra_poblE.setColumnaAlias("tra_pobl");
    tra_codposE.setColumnaAlias("tra_codpos");
    tra_telefE.setColumnaAlias("tra_telef");
    tra_faxE.setColumnaAlias("tra_fax");
    tra_nifE.setColumnaAlias("tra_nif");
    tra_tipoE.setColumnaAlias("tra_tipo");
    tra_vehicE.setColumnaAlias("tra_vehic");
    tra_matricE.setColumnaAlias("tra_matric");
    tra_tipcalE.setColumnaAlias("tra_tipcal");
    tra_prebasE.setColumnaAlias("tra_prebas");
    tra_impkilE.setColumnaAlias("tra_impkil");
    tra_porsegE.setColumnaAlias("tra_porseg");
    tra_porreeE.setColumnaAlias("tra_porree");
    tra_aduoriE.setColumnaAlias("tra_aduori");
    tra_adudesE.setColumnaAlias("tra_adudes");

    activarEventos();
    activaTodo();
    verDatos();
  }
  void activarEventos()
  {
    tra_tipcalE.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        if (nav.pulsado==navegador.EDIT || nav.pulsado==navegador.ADDNEW)
          Ptarfija.setEnabled(tra_tipcalE.getValor().equals("1"));
      }
    });
  }

  void verDatos()
  {
    try {
      if (dtCons.getNOREG())
        return;
      tra_codiE.setText(dtCons.getString("tra_codi"));
      s="SELECT * FROM transportista WHERE tra_codi = '" +tra_codiE.getValorInt()+"'";
      if (! dtCon1.select(s))
      {
        mensajeErr("Transportista NO ENCONTRADO ... SEGURAMENTE SE BORRO");
        Pprinc.resetTexto();
        tra_codiE.setText(dtCons.getString("tra_codi"));
        return;
      }
      tra_nombE.setText(dtCon1.getString("tra_nomb"));
      tra_direcE.setText(dtCon1.getString("tra_direc"));
      tra_poblE.setText(dtCon1.getString("tra_pobl"));
      tra_codposE.setText(dtCon1.getString("tra_codpos"));
      tra_telefE.setText(dtCon1.getString("tra_telef"));
      tra_faxE.setText(dtCon1.getString("tra_fax"));
      tra_nifE.setText(dtCon1.getString("tra_nif"));
      tra_tipoE.setValor(dtCon1.getString("tra_tipo"));
      tra_vehicE.setText(dtCon1.getString("tra_vehic"));
      tra_matricE.setText(dtCon1.getString("tra_matric"));
      tra_tipcalE.setValor(dtCon1.getString("tra_tipcal"));
      tra_prebasE.setText(dtCon1.getString("tra_prebas"));
      tra_prebasE.setText(dtCon1.getString("tra_prebas"));
      tra_impkilE.setText(dtCon1.getString("tra_impkil"));
      tra_porsegE.setText(dtCon1.getString("tra_porseg"));
      tra_porreeE.setText(dtCon1.getString("tra_porree"));
      tra_aduoriE.setText(dtCon1.getString("tra_aduori"));
      tra_adudesE.setText(dtCon1.getString("tra_adudes"));

    } catch (Exception k)
    {
      Error("Error al ver Datos",k);
      return;
    }
  }

  public void activar(boolean act)
  {
    tra_codiE.setEnabled(act);
    tra_nombE.setEnabled(act);
    tra_direcE.setEnabled(act);
    tra_poblE.setEnabled(act);
    tra_codposE.setEnabled(act);
    tra_telefE.setEnabled(act);
    tra_faxE.setEnabled(act);
    tra_nifE.setEnabled(act);
    tra_tipoE.setEnabled(act);
    tra_vehicE.setEnabled(act);
    tra_matricE.setEnabled(act);
    tra_tipcalE.setEnabled(act);
    tra_prebasE.setEnabled(act);
    tra_impkilE.setEnabled(act);
    tra_porsegE.setEnabled(act);
    tra_porreeE.setEnabled(act);
    tra_aduoriE.setEnabled(act);
    tra_adudesE.setEnabled(act);

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
    Ptarfija.setEnabled(true);
    Pprinc.setQuery(true);
    Pprinc.resetTexto();
//    Pprod.setEnabled(false);
//    pro_numeE.requestFocus();
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
    v.add(tra_nombE.getStrQuery());
    v.add(tra_codiE.getStrQuery());
    v.add(tra_nombE.getStrQuery());
    v.add(tra_direcE.getStrQuery());
    v.add(tra_poblE.getStrQuery());
    v.add(tra_codposE.getStrQuery());
    v.add(tra_telefE.getStrQuery());
    v.add(tra_faxE.getStrQuery());
    v.add(tra_nifE.getStrQuery());
    v.add(tra_tipoE.getStrQuery());
    v.add(tra_vehicE.getStrQuery());
    v.add(tra_matricE.getStrQuery());
    v.add(tra_tipcalE.getStrQuery());
    v.add(tra_prebasE.getStrQuery());
    v.add(tra_impkilE.getStrQuery());
    v.add(tra_porsegE.getStrQuery());
    v.add(tra_porreeE.getStrQuery());
    v.add(tra_aduoriE.getStrQuery());
    v.add(tra_adudesE.getStrQuery());

    s = "SELECT * FROM transportista ";
    s = creaWhere(s, v,true);
    s+=" ORDER BY tra_codi ";
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
      fatalError("Error al buscar Transportistas: ", ex);
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
    tra_codiE.setEnabled(false);
    try
    {
      if (!setBloqueo(dtAdd, "transportista",tra_codiE.getText()))
      {
        msgBox(msgBloqueo);
        nav.pulsado = navegador.NINGUNO;
        activaTodo();
        dtAdd.getConexion().rollback();
        return;
      }
      if (! dtAdd.select("select * from transportista where tra_codi = '"+tra_codiE.getText()+"'",true))
      {
        mensajeErr("Registro ha sido borrado");
        resetBloqueo(dtAdd, "transportista", tra_codiE.getText(),true);
        activaTodo();
        mensaje("");
       return;
      }
      Ptarfija.setEnabled(tra_tipcalE.getValor().equals("1"));
    }
    catch (Exception k)
    {
      Error("Error al bloquear el registro", k);
      return;
    }
    tra_nombE.requestFocus();
  }

  public void ej_edit1()
  {
    try
    {
      dtAdd.edit();
      actValores(dtAdd);
      dtAdd.update(stUp);
      resetBloqueo(dtAdd, "transportista",tra_codiE.getText(),false);
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
      resetBloqueo(dtAdd, "transportista", tra_codiE.getText(), true);
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
    tra_codiE.setEnabled(false);
    Ptarfija.setEnabled(false);
    tra_nombE.requestFocus();
    mensaje("Insertando Tranportista ...");
  }
  public boolean checkAddNew()
  {

    if (tra_nombE.isNull())
    {
      mensajeErr("Introduzca Nombre de Tranportista");
      tra_nombE.requestFocus();
      return false;
    }
    return true;
  }
  @Override
  public void ej_addnew1()
  {
    try
    {
      s="SELECT max(tra_codi) as tra_codi FROM transportista";
      dtCon1.select(s);
      dtAdd.addNew("transportista");
      tra_codiE.setValorInt(dtCon1.getInt("tra_codi",true)+1);
      dtAdd.setDato("tra_codi",tra_codiE.getValorInt());
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
    mensajeErr("Transportista ... Insertado");

    activaTodo();
  }
  void actValores(DatosTabla dt) throws Exception
  {
    dt.setDato("tra_nomb", tra_nombE.getText());
    dt.setDato("tra_direc", tra_direcE.getText());
    dt.setDato("tra_pobl", tra_poblE.getText());
    dt.setDato("tra_codpos", tra_codposE.getText());
    dt.setDato("tra_telef", tra_telefE.getText());
    dt.setDato("tra_fax", tra_faxE.getText());
    dt.setDato("tra_nif", tra_nifE.getText());
    dt.setDato("tra_tipo", tra_tipoE.getValor());
    dt.setDato("tra_vehic", tra_vehicE.getText());
    dt.setDato("tra_matric", tra_matricE.getText());
    dt.setDato("tra_tipcal", tra_tipcalE.getValor());
    dt.setDato("tra_prebas", tra_prebasE.getText());
    dt.setDato("tra_impkil", tra_impkilE.getText());
    dt.setDato("tra_porseg", tra_porsegE.getText());
    dt.setDato("tra_porree", tra_porreeE.getText());
    dt.setDato("tra_aduori", tra_aduoriE.getText());
    dt.setDato("tra_adudes", tra_adudesE.getText());
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

      if (!setBloqueo(dtAdd, "transportista", tra_codiE.getText()))
      {
        msgBox(msgBloqueo);
        nav.pulsado = navegador.NINGUNO;
        activaTodo();
        return;
      }
      if (!dtAdd.select("select * from transportista where tra_codi= '" +
                        tra_codiE.getText() + "'", true))
      {
        mensajeErr("Registro ha sido borrado");
        resetBloqueo(dtAdd, "transportista", tra_codiE.getText());
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
      resetBloqueo(dtAdd, "transportista",tra_codiE.getText(),false);
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
  @Override
  public void canc_delete() {
    mensaje("");
    activaTodo();
    try {
      resetBloqueo(dtAdd, "transportista",tra_codiE.getText(),true);
    } catch (Exception k)
    {
      Error("Error al Anular bloqueo sobre tabla usuarios",k);
    }

    mensajeErr("Borrado  de Datos Cancelada");
    verDatos();
  }


}
