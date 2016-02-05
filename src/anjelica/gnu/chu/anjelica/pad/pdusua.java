 package gnu.chu.anjelica.pad;

import gnu.chu.Menu.Principal;
import gnu.chu.controles.*;
import gnu.chu.eventos.CambioEvent;
import gnu.chu.eventos.CambioListener;
import gnu.chu.interfaces.PAD;
import gnu.chu.isql.utilSql;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
/**
 *
 * <p>Título: pdusua </p>
 * <p>Descripción: Mantenimiento Tabla de Usuarios</p>
 * <p>Empresa: miCasa</p>
 *  <p>Copyright: Copyright (c) 2005-2013
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los términos de la Licencia Pública General de GNU según es publicada por
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
public class pdusua extends ventanaPad   implements PAD
{
  String s;
  CPanel Pprinc = new CPanel();
  CLabel usu_nombreL = new CLabel();
  CLabel cLabel6 = new CLabel();
  CButton Bpermisos=new CButton("Perm",Iconos.getImageIcon("data-undo"));
  CLinkBox emp_codiE = new CLinkBox();
  CLabel cLabel8 = new CLabel();
  CTextField usu_nombE = new CTextField(Types.CHAR,"X",30);
  CTextField eje_numeE = new CTextField(Types.DECIMAL,"###9");
  CLabel cLabel4 = new CLabel();
  CTextField usu_nomcoE = new CTextField(Types.CHAR,"X",100);
  CLabel cLabel5 = new CLabel();
  CTextField usu_emailE = new CTextField(Types.CHAR,"X",100);
  CLabel cLabel1 = new CLabel();
  CComboBox usu_puejpaE = new CComboBox();
  CLabel cLabel2 = new CLabel();
  CComboBox usu_admdbE = new CComboBox();
  CLabel cLabel3 = new CLabel();
  CComboBox usu_activE = new CComboBox();
  boolean modConsulta=true;
  CCheckBox usu_rese1E = new CCheckBox();
  CCheckBox usu_previE = new CCheckBox();
  CLabel cLabel7 = new CLabel();
  CPanel cPanel1 = new CPanel();
  CCheckBox usu_diapriE = new CCheckBox();
  CLabel passL = new CLabel();
  CPasswordField usu_passE = new CPasswordField(Types.CHAR,"X",15);
  CLabel cLabel9 = new CLabel();
  CLinkBox sbe_codiE = new CLinkBox();

  public pdusua(EntornoUsuario eu, Principal p)
  {
    this(eu, p, null);
  }

  public pdusua(EntornoUsuario eu, Principal p,Hashtable ht)
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
      setTitulo("Mantenimiento de Usuarios");
      if (jf.gestor.apuntar(this))
        jbInit();
      else
        setErrorInit(true);
    }
    catch (Exception e)
    {
      Logger.getLogger(pdusua.class.getName()).log(Level.SEVERE, null, e);
      setErrorInit(true);
    }
  }

  public pdusua(gnu.chu.anjelica.menu p, EntornoUsuario eu,Hashtable ht)
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
       setTitulo("Mantenimiento de Usuarios");


      jbInit();
    }
    catch (Exception e)
    {
      Logger.getLogger(pdusua.class.getName()).log(Level.SEVERE, null, e);
      setErrorInit(true);
    }
  }

  private void jbInit() throws Exception
  {
    iniciarFrame();
    this.setSize(new Dimension(497, 334));

    this.setVersion("2015-12-27"+(modConsulta ? "SOLO LECTURA" : ""));
    strSql = "SELECT * FROM usuarios  ORDER BY usu_nomb ";

    statusBar = new StatusBar(this);
    conecta();
    nav = new navegador(this, dtCons, false, modConsulta? navegador.CURYCON:navegador.NORMAL);
    iniciar(this);
//    this.getContentPane().add(nav, BorderLayout.NORTH);
//    this.getContentPane().add(statusBar, BorderLayout.SOUTH);
    
    Pprinc.setMaximumSize(new Dimension(32767, 32767));
    Pprinc.setMinimumSize(new Dimension(471, 149));
    Pprinc.setDefButton(Baceptar);
    Pprinc.setEscButton(Bcancelar);
    Pprinc.setLayout(null);
//    Baceptar.setBounds(new Rectangle(98, 65, 100, 24));
//    Bcancelar.setBounds(new Rectangle(228, 65, 100, 24));
    usu_nombreL.setText("Usuario");
    usu_nombreL.setBounds(new Rectangle(4, 3, 47, 18));
    cLabel6.setText("Empresa");
    cLabel6.setBounds(new Rectangle(4, 44, 58, 18));
    emp_codiE.setAncTexto(30);
    emp_codiE.setBounds(new Rectangle(57, 44, 378, 18));
    cLabel8.setText("Ejercicio");
    cLabel8.setBounds(new Rectangle(3, 109, 49, 18));
    Bcancelar.setBounds(new Rectangle(318, 124, 97, 25));
    Baceptar.setBounds(new Rectangle(96, 124, 97, 25));
    cLabel4.setRequestFocusEnabled(true);
    cLabel4.setText("Nombre");
    cLabel4.setBounds(new Rectangle(4, 24, 50, 18));
    cLabel5.setText("E-Mail");
    cLabel5.setBounds(new Rectangle(3, 88, 52, 18));
    Baceptar.setBounds(new Rectangle(99, 206, 133, 31));
    Bcancelar.setBounds(new Rectangle(261, 206, 133, 31));
    Bpermisos.setBounds(new Rectangle(400, 206, 80, 24));
    Bpermisos.setToolTipText("Regenerar permisos sobre base de datos");
    cLabel1.setText("Ejecuta programas externos");
    cLabel1.setBounds(new Rectangle(271, 109, 161, 18));
    cLabel2.setText("Administrador Bloqueos");
    cLabel2.setBounds(new Rectangle(1, 133, 137, 18));
    usu_nomcoE.setRequestFocusEnabled(true);
    usu_nomcoE.setBounds(new Rectangle(57, 22, 423, 17));
    usu_nombE.setBounds(new Rectangle(57, 3, 115, 17));
    usu_emailE.setBounds(new Rectangle(56, 86, 420, 17));
    eje_numeE.setBounds(new Rectangle(56, 109, 54, 18));
    usu_puejpaE.setBounds(new Rectangle(432, 109, 45, 18));
    usu_admdbE.setBounds(new Rectangle(141, 133, 45, 18));
    cLabel3.setText("Activo");
    cLabel3.setBounds(new Rectangle(376, 133, 45, 16));
    usu_activE.setBounds(new Rectangle(432, 133, 45, 18));

    usu_rese1E.setText("Ver Albaranes Ventas Ocultos");
    usu_rese1E.setBounds(new Rectangle(258, 182, 218, 17));
    usu_previE.setText("Previsualizar");
    usu_previE.setBounds(new Rectangle(62, 2, 119, 18));
    cLabel7.setBackground(Color.white);
    cLabel7.setFont(new java.awt.Font("Dialog", 1, 11));
    cLabel7.setForeground(Color.red);
    cLabel7.setOpaque(true);
    cLabel7.setText("Listados");
    cLabel7.setBounds(new Rectangle(2, 2, 55, 19));
    cPanel1.setBorder(BorderFactory.createLineBorder(Color.black));  
    cPanel1.setBounds(new Rectangle(61, 155, 350, 22));
    cPanel1.setLayout(null);
    usu_diapriE.setToolTipText("Mostrar Dialogo Impresion");
    usu_diapriE.setText("Mostrar Dialogo");
    usu_diapriE.setBounds(new Rectangle(191, 2, 144, 17));
    passL.setRequestFocusEnabled(true);
    passL.setText("Contraseña");
    passL.setBounds(new Rectangle(2, 182, 70, 18));
    usu_passE.setMayusc(false);
    usu_passE.setBounds(new Rectangle(70, 182, 152, 18));
    cLabel9.setBounds(new Rectangle(3, 63, 73, 18));
    cLabel9.setText("SubEmpresa");
    sbe_codiE.setBounds(new Rectangle(89, 64, 346, 18));
    sbe_codiE.setAncTexto(30);
    
    Pprinc.add(usu_nombreL, null);
    Pprinc.add(cLabel4, null);
    Pprinc.add(cLabel6, null);
    Pprinc.add(emp_codiE, null);
    Pprinc.add(usu_nombE, null);
    Pprinc.add(usu_nomcoE, null);
   
    cPanel1.add(cLabel7, null);
    cPanel1.add(usu_previE, null);
    cPanel1.add(usu_diapriE, null);
    Pprinc.add(Bcancelar, null);
    Pprinc.add(Baceptar, null);
    Pprinc.add(Bpermisos);
    Pprinc.add(cLabel9, null);
    Pprinc.add(sbe_codiE, null);
    Pprinc.add(usu_emailE, null);
    Pprinc.add(cLabel5, null);
    Pprinc.add(cLabel8, null);
    Pprinc.add(eje_numeE, null);
    Pprinc.add(usu_admdbE, null);
    Pprinc.add(cLabel2, null);
    Pprinc.add(usu_puejpaE, null);
    Pprinc.add(cLabel1, null);
    Pprinc.add(cLabel3, null);
    Pprinc.add(usu_activE, null);
    Pprinc.add(usu_passE, null);
    Pprinc.add(passL, null);
    Pprinc.add(usu_rese1E, null);
    Pprinc.add(cPanel1, null);
    Pprinc.add(Baceptar, null);
    Pprinc.add(Bcancelar, null);
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
    usu_rese1E.setVisible(EU.getUsuReser1().equals("S"));

  }

    @Override
  public void iniciarVentana() throws Exception
  {
    s="SELECT emp_codi,emp_nomb FROM empresa order by emp_codi";
    dtCon1.select(s);
    emp_codiE.addDatos(dtCon1);
    emp_codiE.setFormato(true);
    emp_codiE.setFormato(Types.DECIMAL,"#9",2);
    emp_codiE.setAceptaNulo(false);

    s="SELECT sbe_codi,sbe_nomb FROM subempresa where emp_codi = 1 order by sbe_codi";
    dtCon1.select(s);
    sbe_codiE.addDatos(dtCon1);
    sbe_codiE.setFormato(true);
    sbe_codiE.setFormato(Types.DECIMAL,"#9",2);
    sbe_codiE.setAceptaNulo(true);

    Pprinc.setButton(KeyEvent.VK_F4,Baceptar);
    usu_nombE.setColumnaAlias("usu_nomb");
    emp_codiE.setColumnaAlias("emp_codi");
    sbe_codiE.setColumnaAlias("sbe_codi");
    usu_nomcoE.setColumnaAlias("usu_nomco");
    eje_numeE.setColumnaAlias("eje_nume");
    usu_emailE.setColumnaAlias("usu_email");
    usu_puejpaE.setColumnaAlias("usu_puejpa");
    usu_admdbE.setColumnaAlias("usu_admdb");
    usu_activE.setColumnaAlias("usu_activ");

    usu_admdbE.addItem("Si", "S");
    usu_admdbE.addItem("No", "N");
    usu_puejpaE.addItem("Si", "S");
    usu_puejpaE.addItem("No", "N");
    usu_activE.addItem("Si", "S");
    usu_activE.addItem("No", "N");

    if (dtStat.getConexion().getDriverType()!=gnu.chu.sql.conexion.POSTGRES)
    {
      usu_passE.setVisible(false);
      passL.setVisible(false);
    }
    activarEventos();
    activaTodo();
    verDatos();
  }
  void activarEventos()
  {
    Bpermisos.addActionListener(new ActionListener(){
     public void actionPerformed(ActionEvent e)
     {
         if (usu_nombE.isNull())
             return;
         try
         {
             darPermisos(usu_nombE.getText());
             dtAdd.commit();
         } catch (SQLException ex)
         {
             Error("Error al regenerar Permisos",ex);
         }
         msgBox("Regenerados permisos de "+usu_nombE.getText());
     }
     
    });
    emp_codiE.addCambioListener(new CambioListener()
    {
      public void cambio(CambioEvent event)
      {
        try {
         updateSbeCodi(dtStat);
        } catch (Exception k)
        {

        }
      }
    });
  }
  private void updateSbeCodi(DatosTabla dt) throws SQLException
  {
    if (emp_codiE.isNull())
      return;
    s = "SELECT sbe_codi,sbe_nomb FROM subempresa where emp_codi = " + emp_codiE.getValorInt() + " order by sbe_codi";
    dt.select(s);
    sbe_codiE.addDatos(dt);
  }
  void verDatos()
  {
    try {
      if (dtCons.getNOREG())
        return;
      usu_nombE.setText(dtCons.getString("usu_nomb"));
      s="SELECT * FROM usuarios WHERE usu_nomb = '" +usu_nombE.getText()+"'";
      if (! dtCon1.select(s))
      {
        mensajeErr("Usuario NO ENCONTRADO ... SEGURAMENTE SE BORRO");
        Pprinc.resetTexto();
        usu_nombE.setText(dtCons.getString("usu_nomb"));
        return;
      }

      emp_codiE.setText(dtCon1.getString("emp_codi"));
      eje_numeE.setText(dtCon1.getString("eje_nume"));
      usu_nomcoE.setText(dtCon1.getString("usu_nomco"));
      usu_emailE.setText(dtCon1.getString("usu_email"));
      usu_puejpaE.setValor(dtCon1.getString("usu_puejpa"));
      usu_admdbE.setValor(dtCon1.getString("usu_admdb"));
      usu_activE.setValor(dtCon1.getString("usu_activ"));
      usu_rese1E.setSelected(dtCon1.getString("usu_rese1").equals("S"));
      usu_previE.setSelected(dtCon1.getString("usu_previ").equals("S"));
      usu_diapriE.setSelected(dtCon1.getString("usu_diapri").equals("S"));
      try {
        if (EU.isRootAV() && dtCon1.getString("usu_pass",true).length()>8)
            usu_passE.setToolTipText(EU.decryptAES(dtCon1.getString("usu_pass")));
        else
            usu_passE.setToolTipText("");
      } catch (Exception k ){ }
      updateSbeCodi(dtStat);
      sbe_codiE.setText(dtCon1.getString("sbe_codi"));

    } catch (Exception k)
    {
      Error("Error al ver Datos",k);
    }
  }

  public void activar(boolean act)
  {
    Bpermisos.setEnabled(!act);
    emp_codiE.setEnabled(act);
    sbe_codiE.setEnabled(act);
    usu_nombE.setEnabled(act);
    eje_numeE.setEnabled(act);
    usu_nomcoE.setEnabled(act);
    usu_emailE.setEnabled(act);
    usu_puejpaE.setEnabled(act);
    usu_activE.setEnabled(act);
    usu_admdbE.setEnabled(act);
    usu_rese1E.setEnabled(act);
    usu_previE.setEnabled(act);
    usu_diapriE.setEnabled(act);
    Baceptar.setEnabled(act);
    Bcancelar.setEnabled(act);
    usu_passE.setEnabled(act);
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
//    Pprod.setEnabled(false);
//    pro_numeE.requestFocus();
  }

    @Override
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
    v.add(usu_nombE.getStrQuery());
    v.add(usu_nomcoE.getStrQuery());
    v.add(usu_emailE.getStrQuery());
    v.add(emp_codiE.getStrQuery());
    v.add(usu_puejpaE.getStrQuery());
    v.add(usu_admdbE.getStrQuery());
    v.add(usu_activE.getStrQuery());
    v.add(sbe_codiE.getStrQuery());
    s = "SELECT * FROM usuarios ";
    s = creaWhere(s, v,true);
    s+=" ORDER BY usu_nomb ";
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
      fatalError("Error al buscar Usuarios: ", ex);
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
    usu_nombE.setEnabled(false);
    try
    {
      if (!setBloqueo(dtAdd, "usuarios",usu_nombE.getText()))
      {
        msgBox(msgBloqueo);
        nav.pulsado = navegador.NINGUNO;
        activaTodo();
        dtAdd.getConexion().rollback();
        return;
      }
      if (! dtAdd.select("select * from usuarios where usu_nomb= '"+usu_nombE.getText()+"'",true))
      {
        mensajeErr("Registro ha sido borrado");
        resetBloqueo(dtAdd, "usuarios", usu_nombE.getText());
        activaTodo();
        mensaje("");
       return;
      }
      usu_passE.setText(usu_passE.getToolTipText());
    }
    catch (Exception k)
    {
      Error("Error al bloquear el registro", k);
      return;
    }
    usu_nomcoE.requestFocus();
    usu_passE.setText("***+++");
  }

  public void ej_edit1()
  {
    try
    {
      dtAdd.edit();
      actValores(dtAdd);
      dtAdd.update(stUp);
      resetBloqueo(dtAdd, "usuarios",usu_nombE.getText(),false);
      ctUp.commit();
      if (EU.usuario.equals(usu_nombE.getText()))
      { // Actualizar entorno Usuario
        EU.usu_nomb=usu_nomcoE.getText();
        EU.ejercicio=eje_numeE.getValorInt();
        EU.em_cod=emp_codiE.getValorInt();
        EU.setUsuReser1(usu_rese1E.isSelected()?"S":"N");
      }
      try
      {
        if (! usu_passE.getTextSuper().equals("***+++")  && ctUp.getDriverType()==gnu.chu.sql.conexion.POSTGRES)
        {
          stUp.executeUpdate("ALTER USER " + usu_nombE.getText() + " WITH PASSWORD '" +
                             usu_passE.getTextSuper() + "'");
//          debug("Actualizados permisos en DB");
          darPermisos(usu_nombE.getText());
          ctUp.commit();
        }
      }
      catch (SQLException k)
      {
        mensajes.mensajeUrgente("Error al MODIFICAR usuario en base de datos\n" + k.getMessage());
        mensajes.mensajeAviso("El programa se Abortara...");
      }
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
      resetBloqueo(dtAdd, "usuarios", usu_nombE.getText(), true);
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
    usu_puejpaE.setValor("N");
    usu_admdbE.setValor("N");
    usu_activE.setValor("S");
    usu_rese1E.setSelected(false);
    usu_nombE.requestFocus();
    eje_numeE.setValorDec(EU.ejercicio);
    emp_codiE.setValorDec(EU.em_cod);
    sbe_codiE.setValorInt(1);
    mensaje("Insertando Usuario ...");
  }

  public boolean checkAddNew()
  {
    if (usu_nombE.isNull())
    {
      mensajeErr("Introduzca Nombre de Usuario");
      usu_nombE.requestFocus();
      return false;
    }
    if (usu_nomcoE.isNull())
    {
      mensajeErr("Introduzca Nombre Completo de Usuario");
      usu_nomcoE.requestFocus();
      return false;
    }
    if (!emp_codiE.controla())
    {
      mensajeErr("Empresa NO VALIDA");
      return false;
    }
    if (!sbe_codiE.controla())
    {
      mensajeErr("SubEmpresa NO VALIDA");
      return false;
    }

    if (eje_numeE.isNull())
    {
      mensajeErr("Ejercicio NO VALIDO");
      eje_numeE.requestFocus();
      return false;
    }
    return true;
  }
  @Override
  public void ej_addnew1()
  {
    try
    {
      s="SELECT * FROM usuarios WHERE usu_nomb = '"+usu_nombE.getText()+"'";
      if (dtCon1.select(s))
      {
        mensajeErr("Usuario YA EXISTE");
        return;
      }
      dtAdd.addNew("usuarios");
      dtAdd.setDato("usu_nomb",usu_nombE.getText());
      actValores(dtAdd);
      dtAdd.update(stUp);
      ctUp.commit();

    }
    catch (Exception ex)
    {
      Error("Error al Insertar datos",ex);
      return;
    }
    try
    {
      if (ctUp.getDriverType()==gnu.chu.sql.conexion.POSTGRES)
      {
        stUp.executeUpdate("CREATE USER " + usu_nombE.getText() + " WITH PASSWORD '" +
                           usu_passE.getTextSuper() + "'");
        darPermisos(usu_nombE.getText());
              
        ctUp.commit();
      }
    } catch (SQLException k)
    {
      mensajes.mensajeUrgente("Error al insertar usuario en base de datos\n"+k.getMessage());
      mensajes.mensajeAviso("El programa se Abortara...");
    }
    mensaje("");
    mensajeErr("Usuario ... Insertado");

    activaTodo();
//    verDatos();
  }
  void actValores(DatosTabla dt) throws Exception
  {
    dt.setDato("usu_nomco", usu_nomcoE.getText());
    dt.setDato("eje_nume", eje_numeE.getText());
    dt.setDato("usu_email", usu_emailE.getText());
    dt.setDato("emp_codi", emp_codiE.getValorInt());
    dt.setDato("usu_puejpa", usu_puejpaE.getValor());
    dt.setDato("usu_admdb", usu_admdbE.getValor());
    dt.setDato("usu_activ", usu_activE.getValor());
    dt.setDato("usu_rese1", usu_rese1E.isSelected()?"S":"N");
    dt.setDato("usu_previ", usu_previE.isSelected()?"S":"N");
    dt.setDato("usu_diapri", usu_diapriE.isSelected()?"S":"N");
    dt.setDato("sbe_codi", sbe_codiE.getValorInt());
    dt.setDato("usu_pass",EU.encryptAES(usu_passE.getTextSuper()));
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

      if (!setBloqueo(dtAdd, "usuarios", usu_nombE.getText()))
      {
        msgBox(msgBloqueo);
        nav.pulsado = navegador.NINGUNO;
        activaTodo();
        return;
      }
      if (!dtAdd.select("select * from usuarios where usu_nomb= '" +
                        usu_nombE.getText() + "'", true))
      {
        mensajeErr("Registro ha sido borrado");
        resetBloqueo(dtAdd, "usuarios", usu_nombE.getText());
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
      resetBloqueo(dtAdd, "usuarios",usu_nombE.getText(),false);
      ctUp.commit();
      try
      {
        if (ctUp.getDriverType()==gnu.chu.sql.conexion.POSTGRES)
        {
          stUp.executeUpdate("DROP USER " + usu_nombE.getText());
          ctUp.commit();
        }
      }
      catch (SQLException k)
      {
        mensajes.mensajeUrgente("Error al BORRAR usuario en base de datos\n" + k.getMessage());
        mensajes.mensajeAviso("El programa se Abortara...");
      }

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
      resetBloqueo(dtAdd, "usuarios",usu_nombE.getText(),true);
    } catch (SQLException k)
    {
      Error("Error al Anular bloqueo sobre tabla usuarios",k);
    }

    mensajeErr("Borrado  de Datos Cancelada");
    verDatos();
  }
  /**
   * Devuelve a que empresa pertenece un usuario en la tabla Usuarios
   * @param dt
   * @param usuario
   * @return Código de Empresa. 0 Si no existe el usuario
   * @throws SQLException 
   */
  public static int getEmpresa(DatosTabla dt,String usuario ) throws SQLException
  {
      if (dt.select("select emp_codi from usuarios where usu_nomb= '"+usuario+"'"))
          return dt.getInt("emp_codi");
      
      return 0;
  }
  public static gnu.chu.sql.vlike getVLikeEmpresa(String usuario,DatosTabla dt) throws SQLException
  {
    String s="select e.* from v_empresa as e, usuarios as u WHERE e.emp_codi = u.emp_codi "+
        " and u.usu_nomb= '"+usuario+"'";
    gnu.chu.sql.vlike vl=new gnu.chu.sql.vlike();
    if (! dt.selectInto(s,vl))
      return null;
    return vl;
  }
  public static gnu.chu.sql.vlike getVLikeUsuario(String usuario,DatosTabla dt) throws SQLException
  {
    String s="select u.* from usuarios as u WHERE u.usu_nomb= '"+usuario+"'";
    gnu.chu.sql.vlike vl=new gnu.chu.sql.vlike();
    if (! dt.selectInto(s,vl))
      return null;
    return vl;
  }
  /**
   * Comprueba si un usuario contraseña son correctos
   * @param usuario  Usuario a validar
   * @param passwd Contraseña a validar
   * @param dt DatosTabla
   * @return true si es correcta la pareja usuario/contraseña. 
   * @throws Exception
   */
  public static boolean checkPass(String usuario,String passwd, DatosTabla dt) throws Exception
  {
     String s="select u.* from usuarios as u WHERE u.usu_nomb= '"+usuario+"'";
     if (!dt.select(s))
         return false;
     return dt.getString("usu_pass").equals(Formatear.encrypt(passwd));
  }
  
   
    public  void darPermisos(String usuNomb) throws SQLException {
        utilSql.darPermisos(usuNomb,stUp,dtCon1);

    }
    /**
     * Funcion para comprobar si un usuario puede ejecutar programa Programas Externos en el menu
     * @param usuario Usuario a comprobar
     * @param dt Conexion con base de datos
     * @return  true = Si Puede. False = No puede.
     * @throws java.sql.SQLException
     */
    public static boolean canEjecutarProgExt(String usuario,DatosTabla dt) throws SQLException
    {
       return dt.select("SELECT * FROM usuarios WHERE usu_nomb = '"+usuario+"'"+
            " and usu_puejpa = 'S'");
    }
}
