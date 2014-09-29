package gnu.chu.anjelica.tesoreria;

/**
 * <p>Titulo: pdlibrovto </p>
 * <p>Descripción: Mantenimiento Libro de Vencimientos.
 * <p>Copyright: Copyright (c) 2005
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
 *  en 675 Mass Ave, Cambridge, MA 02139, EEUU..
 * </p>
 * <p>Empresa: MISL</p>
 * @author chuchiP
 * @version 1.0
 */

import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import gnu.chu.sql.*;
import java.sql.*;
import gnu.chu.Menu.*;
import java.awt.*;
import java.util.*;
import gnu.chu.interfaces.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;
import gnu.chu.camposdb.*;

public class pdlibrovto extends ventanaPad implements PAD
{
  String s;
  boolean admin = false;
  CPanel Pprinc = new CPanel();
  prvPanel prv_codiE = new prvPanel();
  CComboBox lbv_origeE = new CComboBox();
  traPanel tra_codiE = new traPanel();
  CLinkBox emp_codiE = new CLinkBox();
  CLabel cLabel1 = new CLabel();
  CLabel eje_numeL = new CLabel();
  CTextField eje_numeE = new CTextField(Types.DECIMAL, "###9");
  CLabel cLabel2 = new CLabel();
  CTextField lbv_numfraE = new CTextField(Types.DECIMAL, "#####9");
  CTextField lbv_numeE = new CTextField(Types.DECIMAL, "###9");
  CLabel cLabel3 = new CLabel();
  CLabel cLabel4 = new CLabel();
  CTextField lbv_fecvtoE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel5 = new CLabel();
  CTextField lbv_impvtoE = new CTextField(Types.DECIMAL, "----,--9.99");
  CTextField lbv_imppagE = new CTextField(Types.DECIMAL, "----,--9.99");
  CLabel cLabel6 = new CLabel();
  CLabel cLabel7 = new CLabel();
  CComboBox lbv_pagadoE = new CComboBox();
  CLabel cLabel8 = new CLabel();
  CTextArea lbv_comenE = new CTextArea();
  CScrollPane lbv_comenS = new CScrollPane();
//  CButton Baceptar = new CButton("Aceptar");
//  CButton Bcancelar = new CButton("Cancelar");
  CPanel Pcabe = new CPanel();
  CLabel cLabel9 = new CLabel();
  CTextField lbv_fecfraE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CPanel Pdatos = new CPanel();
  ppagreal Ppagos = new ppagreal();
  CLabel cLabel10 = new CLabel();

  public pdlibrovto(EntornoUsuario eu, Principal p)
  {
    this(eu, p, null);
  }

  public pdlibrovto(EntornoUsuario eu, Principal p, Hashtable ht)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    try
    {
      if (ht != null)
      {
        if (ht.get("admin") != null)
          admin = Boolean.valueOf(ht.get("admin").toString()).
              booleanValue();
      }
      setTitulo("Mant. Libro Vencimientos");

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

  public pdlibrovto(gnu.chu.anjelica.menu p, EntornoUsuario eu)
  {
    this(p, eu, null);
  }

  public pdlibrovto(gnu.chu.anjelica.menu p, EntornoUsuario eu, Hashtable ht)
  {
    EU = eu;
    vl = p.getLayeredPane();
    eje = false;

    try
    {
      if (ht != null)
      {
        if (ht.get("admin") != null)
          admin = Boolean.valueOf(ht.get("admin").toString()).
              booleanValue();
      }
      setTitulo("Mant. Libro Vencimientos");

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
    this.setSize(new Dimension(701, 502));
    this.setVersion("2006-01-88 " + (admin ? "-ADMINISTRADOR-" : ""));

    strSql = "SELECT * FROM librovto WHERE emp_codi = " + EU.em_cod +
        getOrderQuery();


    statusBar = new StatusBar(this);
    nav = new navegador(this, dtCons, false, navegador.NORMAL);
    conecta();
    iniciar(this);
    prv_codiE.setEnabledNombre(true);
    tra_codiE.setEnabledNombre(true);
    Pprinc.setLayout(null);
    cLabel1.setText("Empresa");
    cLabel1.setBounds(new Rectangle(10, 29, 55, 17));
    eje_numeL.setText("Ejercicio");
    eje_numeL.setBounds(new Rectangle(394, 29, 53, 17));
    cLabel2.setText("Num. Fra.");
    cLabel2.setBounds(new Rectangle(9, 49, 60, 17));
    cLabel3.setText("Numero Vto");
    cLabel3.setBounds(new Rectangle(408, 49, 76, 17));
    cLabel4.setText("Fecha Vto.");
    cLabel4.setBounds(new Rectangle(380, 4, 65, 17));
    cLabel5.setText("Importe Pago");
    cLabel5.setBounds(new Rectangle(5, 24, 82, 17));
    lbv_impvtoE.setBounds(new Rectangle(84, 24, 72, 17));
    lbv_imppagE.setBounds(new Rectangle(445, 24, 72, 17));
    cLabel6.setText("Importe Pagado");
    cLabel6.setBounds(new Rectangle(348, 24, 96, 17));
    cLabel7.setText("Totalmente Pagado");
    cLabel7.setBounds(new Rectangle(164, 24, 115, 17));
    lbv_pagadoE.setBounds(new Rectangle(283, 24, 52, 17));
    cLabel8.setText("Comentario");
    cLabel8.setBounds(new Rectangle(3, 52, 79, 18));
    Baceptar.setBounds(new Rectangle(168, 366, 126, 28));
    Bcancelar.setBounds(new Rectangle(346, 366, 126, 28));
    lbv_comenS.setBounds(new Rectangle(80, 52, 439, 45));
    lbv_fecvtoE.setBounds(new Rectangle(445, 4, 72, 17));
    lbv_numeE.setBounds(new Rectangle(490, 49, 32, 17));
    lbv_numfraE.setBounds(new Rectangle(84, 49, 60, 17));
    emp_codiE.setBounds(new Rectangle(84, 29, 260, 18));
    eje_numeE.setBounds(new Rectangle(468, 29, 53, 17));
    tra_codiE.setBounds(new Rectangle(134, 5, 389, 18));
    prv_codiE.setBounds(new Rectangle(134, 5, 389, 18));
    lbv_origeE.setBounds(new Rectangle(9, 5, 111, 18));
    Pcabe.setBorder(BorderFactory.createRaisedBevelBorder());
    Pcabe.setBounds(new Rectangle(53, 2, 535, 71));
    Pcabe.setLayout(null);
    cLabel9.setBounds(new Rectangle(5, 2, 83, 17));
    cLabel9.setText("Fecha Factura");
    lbv_fecfraE.setBounds(new Rectangle(84, 2, 72, 17));
    Pdatos.setBorder(BorderFactory.createEtchedBorder());
    Pdatos.setBounds(new Rectangle(55, 74, 531, 103));
    Pdatos.setLayout(null);
    Ppagos.setBorder(BorderFactory.createEtchedBorder());
    Ppagos.setBounds(new Rectangle(2, 199, 682, 164));
    cLabel10.setBackground(Color.blue);
    cLabel10.setFont(new java.awt.Font("Dialog", 0, 11));
    cLabel10.setForeground(Color.white);
    cLabel10.setDebugGraphicsOptions(0);
    cLabel10.setOpaque(true);
    cLabel10.setHorizontalAlignment(SwingConstants.CENTER);
    cLabel10.setText("Pagos Realizados");
    cLabel10.setBounds(new Rectangle(211, 180, 228, 18));
    Pcabe.add(prv_codiE, null);
    Pcabe.add(tra_codiE, null);
    Pcabe.add(lbv_origeE, null);
    Pcabe.add(eje_numeE, null);
    Pcabe.add(cLabel1, null);
    Pcabe.add(emp_codiE, null);
    Pcabe.add(lbv_numfraE, null);
    Pcabe.add(eje_numeL, null);
    Pcabe.add(cLabel2, null);
    Pcabe.add(lbv_numeE, null);
    Pcabe.add(cLabel3, null);
    Pprinc.add(Baceptar, null);
    Pprinc.add(Bcancelar, null);
    Pprinc.add(Pdatos, null);
    Pprinc.add(cLabel10, null);
    Pdatos.add(lbv_pagadoE, null);
    Pdatos.add(lbv_comenS, null);
    Pdatos.add(lbv_fecvtoE, null);
    Pdatos.add(lbv_imppagE, null);
    Pdatos.add(cLabel6, null);
    Pdatos.add(cLabel8, null);
    Pdatos.add(cLabel5, null);
    Pdatos.add(cLabel4, null);
    Pdatos.add(cLabel9, null);
    Pdatos.add(cLabel7, null);
    Pdatos.add(lbv_fecfraE, null);
    Pdatos.add(lbv_impvtoE, null);
    Pprinc.add(Ppagos, null);
    lbv_comenS.getViewport().add(lbv_comenE, null);
    Pprinc.add(Pcabe, null);
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
    emp_codiE.setAncTexto(30);
    tra_codiE.setVisible(false);
  }

  public void iniciarVentana() throws Exception
  {
    emp_codiE.setCeroIsNull(true);
    emp_codiE.setAceptaNulo(false);
    Pprinc.setDefButton(Baceptar);
    lbv_origeE.setColumnaAlias("lbv_orige");
    tra_codiE.setColumnaAlias("lbv_copvtr");
    prv_codiE.setColumnaAlias("lbv_copvtr");
    tra_codiE.getCampoNombre().setColumnaAlias("lbv_nombre");
    prv_codiE.getCampoNombre().setColumnaAlias("lbv_nombre");
    emp_codiE.setColumnaAlias("emp_codi");
    eje_numeE.setColumnaAlias("eje_nume");
    lbv_numfraE.setColumnaAlias("lbv_numfra");
    lbv_numeE.setColumnaAlias("lbv_nume");
    lbv_fecfraE.setColumnaAlias("lbv_fecfra");
    lbv_fecvtoE.setColumnaAlias("lbv_fecvto");
    lbv_impvtoE.setColumnaAlias("lbv_impvto");
    lbv_pagadoE.setColumnaAlias("lbv_pagado");
    lbv_comenE.setColumnaAlias("lbv_comen");
    activar(false);
    verDatos();
    activarEventos();
  }
  void activarEventos(){
    lbv_origeE.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        setOrigen(lbv_origeE.getValor());
      }
    });
  }
  public void afterConecta() throws SQLException, java.text.ParseException
  {
    lbv_origeE.addItem("Proveedor", "C");
    lbv_origeE.addItem("Transport", "T");
    lbv_pagadoE.addItem("No", "N");
    lbv_pagadoE.addItem("Si", "S");

    prv_codiE.setAceptaNulo(false);
    tra_codiE.setAceptaNulo(false);

    s = "SELECT emp_codi,emp_nomb FROM v_empresa ORDER BY emp_codi";
    dtStat.select(s);
    emp_codiE.addDatos(dtStat);
    emp_codiE.setFormato(Types.DECIMAL, "#9");

    prv_codiE.iniciar(dtStat, this, vl, EU);
    tra_codiE.iniciar(dtStat, this, vl, EU);

  }
  void setOrigen(String origen)
  {
    lbv_origeE.setValor(origen);
    prv_codiE.setVisible(origen.equals("C"));
    tra_codiE.setVisible(!origen.equals("C"));
  }
  void verDatos()
  {
    try
    {
      if (dtCons.getNOREG())
        return;
      setOrigen(dtCons.getString("lbv_orige"));
      emp_codiE.setValorInt(dtCons.getInt("emp_codi"));
      eje_numeE.setValorInt(dtCons.getInt("eje_nume"));
      lbv_numfraE.setValorInt(dtCons.getInt("lbv_numfra"));
      lbv_numeE.setValorInt(dtCons.getInt("lbv_nume"));


      if (! selectRegPant(dtCon1,false))
      {
        mensajeErr("Datos del Pago  .. NO ENCONTRADO");
        Pdatos.resetTexto();
        prv_codiE.resetTexto();
        tra_codiE.resetTexto();
        return;
      }
      if (lbv_origeE.getValor().equals("C"))
      {
        prv_codiE.setValorInt(dtCon1.getInt("lbv_copvtr"));
        prv_codiE.setTextNomb(dtCon1.getString("lbv_nombre"));
      }
      else
      {
        tra_codiE.setValorInt(dtCon1.getInt("lbv_copvtr"));
        tra_codiE.setTextNomb(dtCon1.getString("lbv_nombre"));
      }
      lbv_fecfraE.setText(dtCon1.getFecha("lbv_fecfra", "dd-MM-yyyy"));
      lbv_fecvtoE.setText(dtCon1.getFecha("lbv_fecvto", "dd-MM-yyyy"));
      lbv_impvtoE.setValorDec(dtCon1.getDouble("lbv_impvto"));
      lbv_pagadoE.setValor(dtCon1.getString("lbv_pagado"));
      lbv_imppagE.setValorDec(dtCon1.getDouble("lbv_imppag"));
      lbv_comenE.setText(dtCon1.getString("lbv_comen"));
      Ppagos.buscaDatos(dtCons.getString("lbv_orige"),dtCons.getInt("emp_codi"),dtCons.getInt("eje_nume"),
                       dtCons.getInt("lbv_numfra"),dtCons.getInt("lbv_nume"),dtCon1);
    }
    catch (Exception k)
    {
      Error("Error al Mostrar Datos", k);
    }

  }

  private boolean selectRegPant(DatosTabla dt,boolean block) throws SQLException,ParseException
  {
    s = "select * from librovto WHERE lbv_orige = '" + lbv_origeE.getValor() + "'" +
        " and emp_codi = " + emp_codiE.getValorInt() +
        " and eje_nume = " + eje_numeE.getValorInt() +
        " and lbv_numfra =  " + lbv_numfraE.getValorInt() +
        " and lbv_nume = " + lbv_numeE.getValorInt();
    return dt.select(s,block);

  }

  public void PADPrimero()  {verDatos();}

  public void PADAnterior()  {verDatos();}

  public void PADSiguiente()  {verDatos();}

  public void PADUltimo()  {verDatos();}

  public void PADQuery()
  {
    Pprinc.setQuery(true);
    Pprinc.resetTexto();
    mensaje("Establezca FILTRO de Consulta");
    activar(true);
    lbv_origeE.requestFocus();
  }


  public void ej_query1()
  {
    Component c = Pcabe.getErrorConf();
    if (c != null)
    {
      mensajeErr("FILTRO DE CONSULTA NO VALIDO");
      c.requestFocus();
      return;
    }
    try
    {

      Vector v = new Vector();
      v.add(lbv_origeE.getStrQuery());
      if (lbv_origeE.getValor().equals("C"))
      {
        v.add(prv_codiE.getStrQuery());
        v.add(prv_codiE.getCampoNombre().getStrQuery());
      }
      if (lbv_origeE.getValor().equals("T"))
      {
        v.add(tra_codiE.getStrQuery());
        v.add(tra_codiE.getCampoNombre().getStrQuery());
      }
      v.add(emp_codiE.getStrQuery());
      v.add(eje_numeE.getStrQuery());
      v.add(lbv_numeE.getStrQuery());
      v.add(lbv_numfraE.getStrQuery());
      Pdatos.getVectorQuery(v);
      s = "SELECT * FROM librovto ";
      s = creaWhere(s, v, true);
      s += getOrderQuery();
//      debug("s: "+s);
      this.setEnabled(false);
      mensaje("Espere, por favor ... buscando datos");
      Pprinc.setQuery(false);
      if (!dtCon1.select(s))
      {
        msgBox("No encontrados datos a visualizar con el filtro introducido");
        mensaje("");
        rgSelect();
        verDatos();
        activaTodo();
        this.setEnabled(true);
        return;
      }
      strSql = s;
      activaTodo();

      this.setEnabled(true);
      rgSelect();
      verDatos();
      mensaje("");
      mensajeErr("FILTRO CONSULTA ... ESTABLECIDO");
    }
    catch (Exception k)
    {
      Error("Error al buscar datos", k);
    }
    nav.pulsado = navegador.NINGUNO;
  }

  public void canc_query()
  {
    Pprinc.setQuery(false);
    activaTodo();
    verDatos();
    mensaje("");
    mensajeErr("Introducion FILTRO CONSULTA ... CANCELADO");
  }

  public void PADEdit()
  {
    try
    {
      if (!setBloqueo(dtAdd, "librovto",
                      lbv_origeE.getValor() + "|" + emp_codiE.getValorInt() +
                      "|" + eje_numeE.getText() + "|" + lbv_numfraE.getValorInt() + "|" +
                      lbv_numeE.getValorInt()))
      {
        activaTodo();
        msgBox(msgBloqueo);
        nav.pulsado = navegador.NINGUNO;
        return;
      }

    }
    catch (Exception k)
    {
      Error("Error al bloquear Registro", k);
    }

    activar(true);
    Pcabe.setEnabled(false);
    mensaje("Modificando registro Actual ");
    lbv_origeE.requestFocus();
    prv_codiE.isEnabledNombre();
  }

  public void ej_edit1()
  {
    try
    {
      selectRegPant(dtAdd, true);
      dtAdd.edit();
      actDatos(dtAdd);
      dtAdd.update(stUp);
      resetBloqueo(dtAdd, "librovto",
                lbv_origeE.getValor() + "|" + emp_codiE.getValorInt() +
                "|" + eje_numeE.getText() + "|" + lbv_numfraE.getValorInt() + "|" +
                lbv_numeE.getValorInt(),false);

      ctUp.commit();
      mensajeErr("REGISTRO ... MODIFICADO");
      mensaje("");
      activaTodo();
    }
    catch (Exception k)
    {
      Error("Error al EDITAR registro", k);
    }

  }

  public void canc_edit()
  {
    try {
      resetBloqueo(dtAdd, "librovto",
                   lbv_origeE.getValor() + "|" + emp_codiE.getValorInt() +
                   "|" + eje_numeE.getText() + "|" + lbv_numfraE.getValorInt() + "|" +
                   lbv_numeE.getValorInt());
    } catch (Exception k)
    {
      Error("Error al quitar Bloqueo",k);
    }
    activaTodo();
    verDatos();
    mensaje("");
    mensajeErr("Edicion de registro ... CANCELADO");
  }

  public void PADAddNew()
  {
    activar(true);
    mensaje("Insertando NUEVO registro");
    Pprinc.resetTexto();
    eje_numeE.setValorInt(EU.ejercicio);
    emp_codiE.setValorInt(EU.em_cod);

    lbv_origeE.requestFocus();
    prv_codiE.isEnabledNombre();
  }

  public boolean checkAddNew()
  {
    try {
      if (lbv_origeE.getValor().equals("C"))
      {
        if (!prv_codiE.controla(true,false))
        {
          mensajeErr(prv_codiE.getMsgError());
          return false;
        }
      }
      else
      {
        if (!tra_codiE.controla(true,false))
        {
          mensajeErr(tra_codiE.getMsgError());
          return false;
        }
      }
      if (emp_codiE.getError())
      {
        mensajeErr("Empresa NO es Valida");
        emp_codiE.requestFocus();
        return false;
      }
      if (eje_numeE.getValorInt()==0)
      {
        mensajeErr("Introduzca N�mero de Ejercicio");
        eje_numeE.requestFocus();
        return false;
      }
      if (lbv_numfraE.getValorInt()==0)
      {
        mensajeErr("Introduzca N�mero de Factura");
        lbv_numfraE.requestFocus();
        return false;
      }
      if (lbv_numeE.getValorInt()==0)
      {
        mensajeErr("Introduzca N�mero de Vencimiento");
        lbv_numeE.requestFocus();
        return false;
      }
      if (lbv_fecfraE.getError())
      {
        lbv_fecfraE.requestFocus();
        mensajeErr("Fecha Factura NO es Valida");
        return false;
      }
      if (lbv_fecvtoE.getError() || lbv_fecvtoE.isNull() )
      {
        lbv_fecvtoE.requestFocus();
        mensajeErr("Fecha Vencimiento NO es Valida");
        return false;
      }
      if (lbv_impvtoE.getValorDec()==0)
      {
        lbv_impvtoE.requestFocus();
        mensajeErr("Importe de VTO NO puede ser cero");
        return false;
      }
      return true;
    } catch (Exception k)
    {
      Error("ERROR EN checkAddNew",k);
    }
    return false;
  }
  public void ej_addnew1()
  {
    try
    {
      if (selectRegPant(dtCon1,false))
      {
        mensajeErr("YA existe un registro CON esos datos de cabecera");
        lbv_numfraE.requestFocus();
        return;
      }
      dtAdd.addNew("librovto");
      dtAdd.setDato("lbv_orige",lbv_origeE.getValor());
      if (lbv_origeE.getValor().equals("C"))
      {
        dtAdd.setDato("lbv_copvtr", prv_codiE.getValorInt());
        dtAdd.setDato("lbv_nombre", prv_codiE.getTextNomb());
      }
      else
      {
        dtAdd.setDato("lbv_copvtr", tra_codiE.getValorInt());
        dtAdd.setDato("lbv_nombre", tra_codiE.getTextNomb());
      }
       dtAdd.setDato("emp_codi", emp_codiE.getValorInt());
       dtAdd.setDato("eje_nume", eje_numeE.getValorInt());
       dtAdd.setDato("lbv_numfra", lbv_numfraE.getValorInt());
       dtAdd.setDato("lbv_numfra", lbv_numfraE.getValorInt());
       dtAdd.setDato("lbv_nume", lbv_numeE.getValorInt());
       actDatos(dtAdd);
       dtAdd.update(stUp);
       ctUp.commit();
       mensajeErr("REGISTRO ... INSERTADO");
       mensaje("");
       if (dtCons.getNOREG())
       {
         rgSelect();
         verDatos();
       }
       activaTodo();
    } catch (Exception k)
    {
      Error("Error al INSERTAR registro",k);
    }
  }

  private void actDatos(DatosTabla dt) throws SQLException, ParseException
  {
    dt.setDato("lbv_fecfra", lbv_fecfraE.getDate());
    dt.setDato("lbv_fecvto", lbv_fecvtoE.getDate());
    dt.setDato("lbv_impvto", lbv_impvtoE.getValorDec());
    dt.setDato("lbv_pagado", lbv_pagadoE.getValor());
    dt.setDato("lbv_imppag", lbv_imppagE.getValorDec());
    dt.setDato("lbv_comen", Formatear.strCorta(lbv_comenE.getText(),255));
  }

  public void canc_addnew()
  {
    activaTodo();
    verDatos();
    mensaje("");
    mensajeErr("Insercion NUEVO registro ... CANCELADO");
  }

  public void PADDelete()
  {
    try
    {
      if (!setBloqueo(dtAdd, "librovto",
                      lbv_origeE.getValor() + "|" + emp_codiE.getValorInt() +
                      "|" + eje_numeE.getText() + "|" + lbv_numfraE.getValorInt() + "|" +
                      lbv_numeE.getValorInt()))
      {
        activaTodo();
        msgBox(msgBloqueo);
        nav.pulsado = navegador.NINGUNO;
        return;
      }
    }
    catch (Exception k)
    {
      Error("Error al bloquear Registro", k);
    }
    Baceptar.setEnabled(true);
    Bcancelar.setEnabled(true);
    mensaje("BORRANDO registro Actual ");
    Bcancelar.requestFocus();
  }


  public void ej_delete1()
  {
  try
  {
    selectRegPant(dtAdd, true);
    dtAdd.delete();
    resetBloqueo(dtAdd, "librovto",
              lbv_origeE.getValor() + "|" + emp_codiE.getValorInt() +
              "|" + eje_numeE.getText() + "|" + lbv_numfraE.getValorInt() + "|" +
              lbv_numeE.getValorInt(),false);

    ctUp.commit();
    mensaje("");
    rgSelect();
    verDatos();
    activaTodo();
    mensajeErr("REGISTRO ... BORRADO");
  }
  catch (Exception k)
  {
    Error("Error al BORRAR registro", k);
  }

  }

  public void canc_delete()
  {
    try
    {
      resetBloqueo(dtAdd, "librovto",
                   lbv_origeE.getValor() + "|" + emp_codiE.getValorInt() +
                   "|" + eje_numeE.getText() + "|" + lbv_numfraE.getValorInt() + "|" +
                   lbv_numeE.getValorInt());
    }
    catch (Exception k)
    {
      Error("Error al quitar Bloqueo", k);
    }
    activaTodo();
    mensaje("");
    mensajeErr("BORRADO de registro ... CANCELADO");
  }



  public void activar(boolean b)
  {
    Pcabe.setEnabled(b);
    prv_codiE.setEnabled(b);
    tra_codiE.setEnabled(b);
    Pdatos.setEnabled(b);
    Baceptar.setEnabled(b);
    Bcancelar.setEnabled(b);
  }
  String getOrderQuery()
  {
    return " ORDER BY eje_nume,lbv_numfra,lbv_nume,lbv_orige ";
  }

}
