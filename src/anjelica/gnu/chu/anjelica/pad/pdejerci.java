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
 * <p>Título: pdejerci </p>
 * <p>Descripción: Mantenimiento de Ejercicios</p>
 * <p>Empresa: miCasa</p>
 * <p>Copyright: Copyright (c) 2005-2009 
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
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
 * @author chuchiP
 * @version 1.0
 */
public class pdejerci extends ventanaPad   implements PAD
{
  String s;
  CPanel Pprinc = new CPanel();
  CLabel eje_nombL = new CLabel();
  CTextField eje_numeE = new CTextField(Types.DECIMAL,"###9");
  CTextField eje_feciniE = new CTextField(Types.DATE,"dd-MM-yyyy");
  boolean modConsulta=false;
//  CButton Baceptar = new CButton();
//  CButton Bcancelar = new CButton();
  CLinkBox emp_codiE = new CLinkBox();
  CLabel cLabel13 = new CLabel();
  CTextField eje_fecfinE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel cLabel14 = new CLabel();
  CComboBox eje_cerradE = new CComboBox();
  CLabel cLabel1 = new CLabel();
  CLabel cLabel2 = new CLabel();

  public pdejerci(EntornoUsuario eu, Principal p)
  {
    this(eu, p, null);
  }

  public pdejerci(EntornoUsuario eu, Principal p,Hashtable ht)
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
      setTitulo("Mantenimiento Ejercicios");
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

  public pdejerci(gnu.chu.anjelica.menu p, EntornoUsuario eu,Hashtable ht)
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
      setTitulo("Mantenimiento Ejercicios" );
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
    this.setVersion("2006-08-04"+(modConsulta ? "SOLO LECTURA" : ""));
    strSql = "SELECT * FROM ejercicio where emp_codi = "+EU.em_cod+
       "  ORDER BY eje_nume ";


    statusBar = new StatusBar(this);
    conecta();
    nav = new navegador(this, dtCons, false, modConsulta? navegador.CURYCON:navegador.NORMAL);
    iniciar(this);
    Pprinc.setDefButton(Baceptar);
    Pprinc.setEscButton(Bcancelar);
    Pprinc.setLayout(null);
    eje_nombL.setText("Ejercicio");
    eje_nombL.setBounds(new Rectangle(15, 30, 53, 17));
    cLabel13.setText("Cerrado");
    cLabel13.setBounds(new Rectangle(180, 55, 63, 17));
    cLabel14.setText("Empresa");
    cLabel14.setBounds(new Rectangle(15, 6, 56, 17));
    Bcancelar.setBounds(new Rectangle(272, 74, 106, 27));
    Baceptar.setBounds(new Rectangle(114, 74, 106, 27));
    emp_codiE.setAncTexto(30);
    emp_codiE.setBounds(new Rectangle(68, 5, 326, 17));
    eje_cerradE.setBounds(new Rectangle(233, 53, 54, 17));
    eje_fecfinE.setBounds(new Rectangle(387, 29, 81, 17));
    eje_feciniE.setText("");
    eje_feciniE.setBounds(new Rectangle(206, 29, 86, 17));
    eje_numeE.setText("");
    eje_numeE.setBounds(new Rectangle(68, 30, 46, 17));
    cLabel1.setText("Fecha Inicial");
    cLabel1.setBounds(new Rectangle(133, 29, 70, 17));
    cLabel2.setBounds(new Rectangle(316, 29, 70, 17));
    cLabel2.setText("Fecha Final");
    this.getContentPane().add(nav, BorderLayout.NORTH);
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);

    Pprinc.add(Bcancelar, null);
    Pprinc.add(Baceptar, null);
    Pprinc.add(eje_numeE, null);
    Pprinc.add(eje_nombL, null);
    Pprinc.add(cLabel2, null);
    Pprinc.add(cLabel1, null);
    Pprinc.add(eje_feciniE, null);
    Pprinc.add(eje_fecfinE, null);
    Pprinc.add(cLabel13, null);
    Pprinc.add(eje_cerradE, null);
    Pprinc.add(cLabel14, null);
    Pprinc.add(emp_codiE, null);

  }

    @Override
  public void iniciarVentana() throws Exception
  {

    Pprinc.setButton(KeyEvent.VK_F4,Baceptar);
    eje_numeE.setColumnaAlias("eje_nume");
    eje_feciniE.setColumnaAlias("eje_fecini");
    eje_fecfinE.setColumnaAlias("eje_fecfin");
    eje_cerradE.setColumnaAlias("eje_cerrad");
    emp_codiE.setColumnaAlias("emp_codi");

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
      eje_numeE.setValorInt(dtCons.getInt("eje_nume"));
      emp_codiE.setValorInt(dtCons.getInt("emp_codi"));
      s="SELECT * FROM ejercicio WHERE emp_codi = "+emp_codiE.getValorInt()+
         " and eje_nume = " +eje_numeE.getText();
      if (! dtCon1.select(s))
      {
        mensajeErr("EJERCICIO NO ENCONTRADO ... SEGURAMENTE SE BORRO");
        eje_fecfinE.resetTexto();
        eje_feciniE.resetTexto();
        return;
      }
      eje_feciniE.setText(dtCon1.getFecha("eje_fecini","dd-MM-yyyy"));
      eje_fecfinE.setText(dtCon1.getFecha("eje_fecfin","dd-MM-yyyy"));
      eje_cerradE.setValor(dtCon1.getString("eje_cerrad"));

    } catch (Exception k)
    {
      Error("Error al ver Datos",k);
      return;
    }
  }

  public void activar(boolean enab)
  {
    eje_numeE.setEnabled(enab);
    eje_feciniE.setEnabled(enab);
    eje_fecfinE.setEnabled(enab);
    eje_cerradE.setEnabled(enab);
    emp_codiE.setEnabled(enab);


    Baceptar.setEnabled(enab);
    Bcancelar.setEnabled(enab);
  }
  public void afterConecta() throws SQLException,java.text.ParseException
  {
    eje_cerradE.addItem("No","0");
    eje_cerradE.addItem("Si","-1");
    emp_codiE.setFormato(Types.DECIMAL,"#9");
    emp_codiE.setAceptaNulo(false);
    s="SELECT emp_codi,emp_nomb FROM v_empresa order by emp_nomb";
    dtCon1.select(s);
    emp_codiE.addDatos(dtCon1);
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
    emp_codiE.requestFocus();
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
    v.addElement(eje_numeE.getStrQuery());
    v.addElement(eje_feciniE.getStrQuery());
    v.addElement(eje_fecfinE.getStrQuery());
    v.addElement(eje_cerradE.getStrQuery());
    v.addElement(emp_codiE.getStrQuery());

    s = "SELECT * FROM ejercicio ";
    s = creaWhere(s, v,true);
    s+=" ORDER BY emp_codi,eje_nume ";
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
      fatalError("Error al buscar Ejercicios: ", ex);
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
    eje_numeE.setEnabled(false);
    emp_codiE.setEnabled(false);
    eje_feciniE.requestFocus();
  }

  public void ej_edit1()
  {
    try
    {
      dtAdd.edit();
      actValores(dtAdd);
      dtAdd.update(stUp);
      resetBloqueo(dtAdd, "ejercicio",emp_codiE.getValorInt()+ eje_numeE.getText(),false);
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
      resetBloqueo(dtAdd, "ejercicio",emp_codiE.getValorInt()+ eje_numeE.getText(), true);
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
    eje_numeE.requestFocus();
    mensaje("Insertando Ejercicio ...");
  }
  public boolean checkAddNew()
  {
    try {
      if (!emp_codiE.controla())
      {
        mensajeErr("Empresa NO VALIDA");
        return false;
      }

      if (eje_numeE.isNull())
      {
        mensajeErr("Introduzca Codigo de Ejercicio");
        eje_numeE.requestFocus();
        return false;
      }
      if (eje_feciniE.isNull() || eje_feciniE.getError())
      {
        mensajeErr("Fecha Inicial de Ejercicio .. NO VALIDA");
        eje_feciniE.requestFocus();
        return false;
      }
      if (eje_fecfinE.isNull() || eje_fecfinE.getError())
      {
        mensajeErr("Fecha Final de Ejercicio ... NO VALIDA");
        eje_fecfinE.requestFocus();
        return false;
      }
      if (Formatear.comparaFechas(eje_feciniE.getDate(), eje_fecfinE.getDate()) >= 0)
      {
        mensajeErr("Fecha Final debe ser superior a Fecha Inicial");
        eje_feciniE.requestFocus();
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
      s="SELECT * FROM ejercicio WHERE emp_codi = "+emp_codiE.getValorInt()+
          " and eje_nume = '"+eje_numeE.getText()+"'";
      if (dtCon1.select(s))
      {
        mensajeErr("Ejercicio YA EXISTE");
        return;
      }
      dtAdd.addNew("ejercicio");
      dtAdd.setDato("eje_nume",eje_numeE.getText());
      dtAdd.setDato("emp_codi",emp_codiE.getText());
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
    mensajeErr("Ejercicio ... Insertado");

    activaTodo();
  }

  void actValores(DatosTabla dt) throws Exception
  {
     dt.setDato("eje_fecini",eje_feciniE.getText(),"dd-MM-yyyy");
     dt.setDato("eje_fecfin",eje_fecfinE.getText(),"dd-MM-yyyy");
     dt.setDato("eje_cerrad",eje_cerradE.getValor());
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
      if (!setBloqueo(dtAdd, "ejercicio", emp_codiE.getValorInt() + eje_numeE.getText()))
      {
        msgBox(msgBloqueo);
        nav.pulsado = navegador.NINGUNO;
        mensaje("");
        activaTodo();
        return false;
      }
      if (!dtAdd.select("select * from ejercicio where emp_codi = " + emp_codiE.getValorInt() +
                        " and eje_nume = " + eje_numeE.getValorInt(), true))
      {
        mensajeErr("Registro ha sido borrado");
        resetBloqueo(dtAdd, "ejercicio", emp_codiE.getValorInt() + eje_numeE.getText());
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
      resetBloqueo(dtAdd,"ejercicio", emp_codiE.getValorInt() + eje_numeE.getText(),false);
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
      resetBloqueo(dtAdd,"ejercicio", emp_codiE.getValorInt() + eje_numeE.getText(), true);
    } catch (Exception k)
    {
      Error("Error al Anular bloqueo sobre tabla Ejercicio",k);
    }
    mensajeErr("Borrado  de Datos Cancelada");
    verDatos();
  }

  public static String checkFecha(DatosTabla dt, int ejeNume, int empCodi, java.util.Date fecha) throws
      SQLException
  {
    return checkFecha(dt, ejeNume, empCodi, Formatear.getFechaVer(fecha), true);
  }

  public static String checkFecha(DatosTabla dt,int ejeNume,int empCodi,String fecha) throws SQLException
  {
    return checkFecha(dt,ejeNume,empCodi,fecha,true);
  }
  /**
   * Comprueba si una fecha esta dentro del rango de un ejercicio
   * @param dt DatosTabla
   * @param ejeNume int
   * @param empCodi int
   * @param fecha String
   * @param abierto boolean true-> El ejercicio tiene que estar abierto
   * @throws SQLException
   * @return String Mensaje de error. Null si todo esta bien
   */
  public static String checkFecha(DatosTabla dt,int ejeNume,int empCodi,String fecha,boolean abierto) throws SQLException
  {
    try {
      java.util.Date dtFec=Formatear.getDate(fecha, "dd-MM-yyyy");
      GregorianCalendar gc=new GregorianCalendar();
      gc.setTime(dtFec);
      if (gc.get(GregorianCalendar.YEAR)> 2100)
        return "Año de Fecha superior al 2100";
    } catch (Exception k)
    {
//      k.printStackTrace();
      return "Formato de  Fecha NO valida";
    }
    String s="SELECT * FROM ejercicio WHERE emp_codi = "+empCodi+
        " and eje_nume = "+ejeNume;
    if (!dt.select(s))
      return "Ejercicio "+ejeNume+" en empresa: "+empCodi+" NO ENCONTRADO";
    s=s+" and eje_fecini <= to_Date('"+fecha+"','dd-MM-yyyy')"+
        " and eje_fecfin >= to_Date('"+fecha+"','dd-MM-yyyy')";
    if ( ! dt.select(s))
      return "Fecha NO esta dentro de los limites de este ejercicio";
    if ( abierto && dt.getInt("eje_cerrad")!=0)
       return "Ejercicio ESTA MARCADO como cerrado";
    return null;
  }
  /**
   * Comprueba si una fecha esta dentro del rango de un ejercicio
   * @param dt DatosTabla
   * @param ejeNume int
   * @param empCodi int
   * @throws SQLException
   * @return boolean Cerrado = true
   */
  public static boolean isCerrado(DatosTabla dt,int ejeNume,int empCodi) throws SQLException
  {
    String s="SELECT * FROM ejercicio WHERE emp_codi = "+empCodi+
        " and eje_nume = "+ejeNume;
    if ( ! dt.select(s))
      return true;
    return dt.getInt("eje_cerrad")!=0;
  }

}

