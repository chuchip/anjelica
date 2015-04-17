package gnu.chu.anjelica.pad;

/**
 *
 * <p>Título: pdtiposiva </p>
 * <p>Descripción: Mantenimiento de Tipos de IVA</p>
 * <p>Empresa: misl</p>
 * <p>Copyright: Copyright (c) 2005-2012
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
import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.DatosIVA;
import gnu.chu.anjelica.menu;
import gnu.chu.controles.CComboBox;
import gnu.chu.controles.CLinkBox;
import gnu.chu.controles.StatusBar;
import gnu.chu.interfaces.PAD;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.navegador;
import gnu.chu.utilidades.ventanaPad;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MantTipoIVA extends  ventanaPad     implements PAD 
{
    boolean ARG_MODCONSULTA=false;
    String s;
  public MantTipoIVA(EntornoUsuario eu, Principal p)
  {
    this(eu,p,null);
  }
  public MantTipoIVA(EntornoUsuario eu, Principal p,Hashtable ht)
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
          ARG_MODCONSULTA = Boolean.valueOf(ht.get("modConsulta").toString()).
              booleanValue();
      }
      setTitulo("Mantenimiento Tipos IVA");

      if (jf.gestor.apuntar(this))
        jbInit();
      else
        setErrorInit(true);
    }
    catch (Exception e)
    {
      Logger.getLogger(MantCalendar.class.getName()).log(Level.SEVERE, null, e);
      setErrorInit(true);
    }
  }

  public MantTipoIVA(menu p, EntornoUsuario eu,Hashtable ht)
  {
    EU = eu;
    vl = p.getLayeredPane();
    eje = false;

    try
    {
      if (ht != null)
      {
        if (ht.get("modConsulta") != null)
          ARG_MODCONSULTA = Boolean.valueOf(ht.get("modConsulta").toString()).
              booleanValue();
      }
      setTitulo("Mantenimiento Tipos IVA");
      jbInit();
    }
    catch (Exception e)
    {
      Logger.getLogger(MantCalendar.class.getName()).log(Level.SEVERE, null, e);
      setErrorInit(true);
    }
  }

  private void jbInit() throws Exception {
        statusBar = new StatusBar(this);
        nav = new navegador(this, dtCons, false);
        iniciarFrame();
//        this.setResizable(false);

        this.setVersion("2012-09-13" + (ARG_MODCONSULTA ? "SOLO LECTURA" : ""));
        strSql = "SELECT * FROM tiposiva "+
                "  ORDER BY tii_codi ";

        this.getContentPane().add(nav, BorderLayout.NORTH);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        this.setPad(this);
        dtCons.setLanzaDBCambio(false);
        initComponents();
        iniciarBotones(Baceptar, Bcancelar);

        conecta();
        navActivarAll();
        this.setSize(503,320);
        activar(false);
        if (ARG_MODCONSULTA)
        {
            nav.removeBoton(navegador.ADDNEW);
            nav.removeBoton(navegador.EDIT);
            nav.removeBoton(navegador.DELETE);
        }
    }
  public void iniciarVentana() throws Exception
  {

    Pprinc.setButton(KeyEvent.VK_F4,Baceptar);
    tii_feciniE.setColumnaAlias("tii_fecini");
    tii_fecfinE.setColumnaAlias("tii_fecfin");
    tii_codiE.setColumnaAlias("tii_codi");
    tii_ctaivaE.setColumnaAlias("tii_ctaiva");
    tii_ctareeE.setColumnaAlias("tii_ctaree");
    tii_ctairvE.setColumnaAlias("tii_ctairv");
    tii_ctaivcE.setColumnaAlias("tii_ctaivc");
    tii_ctarecE.setColumnaAlias("tii_ctarec");
    tii_ctaircE.setColumnaAlias("tii_ctairc");
    tii_ivaE.setColumnaAlias("tii_iva");
    tii_recE.setColumnaAlias("tii_rec");
    tii_irpfE.setColumnaAlias("tii_irpf");

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

      tii_codiE.setValorInt(dtCons.getInt("tii_codi"));
      s="SELECT * FROM tiposiva WHERE tii_codi = "+tii_codiE.getValorInt()+
          " and tii_fecini = "+Formatear.getSQLDate(dtCons.getDate("tii_fecini"))+
          " and tii_fecfin ="+Formatear.getSQLDate(dtCons.getDate("tii_fecfin"));
      
      if (! dtCon1.select(s))
      {
        mensajeErr("TIPO DE IVA  NO ENCONTRADO ... SEGURAMENTE SE BORRO");
        Pprinc.resetTexto();
        tii_codiE.setValorInt(dtCons.getInt("tii_codi"));
        return;
      }
      tii_feciniE.setDate(dtCon1.getDate("tii_fecini"));
      tii_fecfinE.setDate(dtCon1.getDate("tii_fecfin"));
      tii_ctaivaE.setText(dtCon1.getString("tii_ctaiva"));
      tii_ctareeE.setText(dtCon1.getString("tii_ctaree"));
      tii_ctairvE.setText(dtCon1.getString("tii_ctairv"));
      tii_ctaivcE.setText(dtCon1.getString("tii_ctaivc"));
      tii_ctarecE.setText(dtCon1.getString("tii_ctarec"));
      tii_ctaircE.setText(dtCon1.getString("tii_ctairc"));
      tii_ivaE.setValorDec(dtCon1.getDouble("tii_iva"));
      tii_recE.setValorDec(dtCon1.getDouble("tii_rec"));
      tii_irpfE.setValorDec(dtCon1.getDouble("tii_irpf"));
    } catch (Exception k)
    {
      Error("Error al ver Datos",k);
    }
  }

  public void activar(boolean enab)
  {
    tii_feciniE.setEnabled(enab);
    tii_fecfinE.setEnabled(enab);
    tii_codiE.setEnabled(enab);
    tii_ctaivaE.setEnabled(enab);
    tii_ctareeE.setEnabled(enab);
    tii_ctairvE.setEnabled(enab);
    tii_ctaivcE.setEnabled(enab);
    tii_ctarecE.setEnabled(enab);
    tii_ctaircE.setEnabled(enab);
    tii_ivaE.setEnabled(enab);
    tii_recE.setEnabled(enab);
    tii_irpfE.setEnabled(enab);

    Baceptar.setEnabled(enab);
    Bcancelar.setEnabled(enab);
  }
  public void afterConecta() throws SQLException,java.text.ParseException
  {
    tii_codiE.setAceptaNulo(false);
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
    tii_codiE.requestFocus();
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
    v.add(tii_feciniE.getStrQuery());
    v.add(tii_fecfinE.getStrQuery());
    v.add(tii_codiE.getStrQuery());
    v.add(tii_ctaivaE.getStrQuery());
    v.add(tii_ctareeE.getStrQuery());
    v.add(tii_ctairvE.getStrQuery());
    v.add(tii_ctaivcE.getStrQuery());
    v.add(tii_ctarecE.getStrQuery());
    v.add(tii_ctaircE.getStrQuery());
    v.add(tii_ivaE.getStrQuery());
    v.add(tii_recE.getStrQuery());
    v.add(tii_irpfE.getStrQuery());

    v.add(tii_codiE.getStrQuery());

    s = "SELECT * FROM tiposiva ";
    s = creaWhere(s, v,true);
    s+=" ORDER BY tii_codi ";
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
      fatalError("Error al buscar Tipos de IVA: ", ex);
    }

  }

    @Override
  public void canc_query()
  {
    Pprinc.setQuery(false);
    mensaje("");
    mensajeErr("Consulta ... CANCELADA");
    activaTodo();
    verDatos();
  }

    @Override
  public void PADEdit()
  {
    if (! checkRegistro())
      return;
    activar(true);
    tii_codiE.setEnabled(false);
    tii_ivaE.requestFocus();
  }

    @Override
  public void ej_edit1()
  {
    try
    {
      dtAdd.edit();
      actValores(dtAdd);
      dtAdd.update(stUp);
      resetBloqueo(dtAdd, "tiposiva", tii_codiE.getText()+tii_feciniE.getText()+tii_fecfinE.getText(),false);
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

    @Override
  public void canc_edit()
  {
    mensaje("");
    try {
      resetBloqueo(dtAdd, "tiposiva", tii_codiE.getText()+tii_feciniE.getText()+tii_fecfinE.getText(), true);
    } catch (Exception ex)
    {
      Error("Error al Quitar Bloqueo", ex);
      return;
    }
    mensajeErr("Modificacion de Datos Cancelada");
    activaTodo();
    verDatos();
  }

    @Override
  public boolean checkEdit()
  {
    return checkAddNew();
  }


    @Override
  public void PADAddNew()
  {
    Pprinc.resetTexto();
    activar(true);
    tii_codiE.requestFocus();
    mensaje("Insertando Nuevo Registro ...");
  }
    @Override
  public boolean checkAddNew()
  {
    try {
      if (tii_codiE.getValorInt()==0)
      {
        mensajeErr("Codigo  de IVA  NO VALIDO");
        tii_codiE.requestFocus();
        return false;
      }
      if (tii_feciniE.isNull())
      {
          mensajeErr("Fecha Inicio NO valida");
          tii_feciniE.requestFocus();
          return false;
      }
      if (tii_fecfinE.isNull())
      {
          mensajeErr("Fecha Final NO valida");
          tii_fecfinE.requestFocus();
          return false;
      }
    } catch (Exception k)
    {
      Error("Error al comprobar campos",k);
      return false;
    }
    return true;
  }
    @Override
  public void ej_addnew1()
  {
    try
    {
//      s="SELECT * FROM tiposiva WHERE tii_codi = "+tii_codiE.getValorInt();
//      if (dtCon1.select(s))
//      {
//        mensajeErr("Tipo de IVA YA EXISTE");
//        return;
//      }
      
      dtAdd.addNew("tiposiva");
      dtAdd.setDato("tii_codi",tii_codiE.getText());
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
    mensajeErr("Registo ... Insertado");

    activaTodo();
  }

  void actValores(DatosTabla dt) throws Exception
  {
     dt.setDato("tii_fecini",tii_feciniE.getDate());
     dt.setDato("tii_fecfin",tii_fecfinE.getDate());
     dt.setDato("tii_ctaiva",tii_ctaivaE.getText());
     dt.setDato("tii_ctaree",tii_ctareeE.getText());
     dt.setDato("tii_ctairv",tii_ctairvE.getText());
     dt.setDato("tii_ctaivc",tii_ctaivcE.getText());
     dt.setDato("tii_ctarec",tii_ctarecE.getText());
     dt.setDato("tii_ctairc",tii_ctaircE.getText());
     dt.setDato("tii_iva",tii_ivaE.getValorDec());
     dt.setDato("tii_rec",tii_recE.getValorDec());
     dt.setDato("tii_irpf",tii_irpfE.getValorDec());
  }
    @Override
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
      if (!setBloqueo(dtAdd, "tiposiva", tii_codiE.getText()+
          tii_feciniE.getText()+tii_fecfinE.getText()))
      {
        msgBox(msgBloqueo);
        nav.pulsado = navegador.NINGUNO;
        mensaje("");
        activaTodo();
        return false;
      }
      if (!dtAdd.select("select * from tiposiva where tii_codi = " + tii_codiE.getValorInt()+
          " and tii_fecini = "+tii_feciniE.getSQLDate()+
          " and tii_fecfin = "+tii_fecfinE.getSQLDate(),true))
      {
        mensajeErr("Registro ha sido borrado");
        resetBloqueo(dtAdd,"tiposiva", tii_codiE.getText()+tii_feciniE.getText()+tii_fecfinE.getText());
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
      resetBloqueo(dtAdd,"tiposiva", tii_codiE.getText()+tii_feciniE.getText()+tii_fecfinE.getText(),false);
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
      resetBloqueo(dtAdd,"tiposiva", tii_codiE.getText()+tii_feciniE.getText()+tii_fecfinE.getText(), true);
    } catch (Exception k)
    {
      Error("Error al Anular bloqueo sobre tabla Tipos de IVA",k);
    }
    mensajeErr("Borrado  de Datos Cancelada");
    verDatos();
  }
     /**
   * Devuelve el porcentaje del IVA para un tipo de IVA
   * @param dt DatosTabla con el q hacer la consulta
   * @param tipoIva tipo IVA
   * @return Porcentaje de IVA . -1 si no se encontro el tipo de IVA.
   * @throws SQLException Error en la base de datos
   */
  public static int getPorcIva(DatosTabla dt, int tipoIva) throws SQLException
  {
      return getPorcIva(dt,tipoIva,Formatear.getDateAct());
  }
  /**
   * Devuelve el porcentaje del IVA para un tipo de IVA
   * @param dt DatosTabla con el q hacer la consulta
   * @param tipoIva tipo IVA
   * @param Fecha donde buscar IVA.
   * @return Porcentaje de IVA . -1 si no se encontro el tipo de IVA.
   * @throws SQLException Error en la base de datos
   */
  public static int getPorcIva(DatosTabla dt, int tipoIva,Date fecha) throws SQLException
  {
      if (dt.select("SELECT * FROM tiposiva " +
            "WHERE "+
            Formatear.getSQLDate(fecha)+" between tii_fecini and tii_fecfin "+
            " and tii_codi = " + tipoIva))
         return -1;
      else
         return dt.getInt("tii_iva");
  }
  /**
   * Devuelve el porcentaje del IVA para un tipo de IVA
   * @param dt DatosTabla con el q hacer la consulta
   * @param tipoIva tipo IVA
   * @return Porcentaje de IVA . -1 si no se encontro el tipo de IVA.
   * @throws SQLException Error en la base de datos
   */
  public static DatosIVA getDatosIva(DatosTabla dt, int tipoIva) throws SQLException
  {

     return getDatosIva(dt,tipoIva,Formatear.getDateAct());
  }
  /**
   * Devuelve el porcentaje del IVA para un tipo de IVA
   * @param dt DatosTabla con el q hacer la consulta
   * @param tipoIva tipo IVA
   * @param fecha Fecha del documento.
   * @return Porcentaje de IVA . -1 si no se encontro el tipo de IVA.
   * @throws SQLException Error en la base de datos
   */
  public static DatosIVA getDatosIva(DatosTabla dt, int tipoIva,Date fecha) throws SQLException
  {
      String s="SELECT * FROM tiposiva " +
            "WHERE tii_codi = " + tipoIva+
            " and "+Formatear.getSQLDate(fecha)+" between tii_fecini and tii_fecfin "+
            " order by tii_fecini desc";
      if (! dt.select(s))
         return null;
      else
         return new DatosIVA(dt);
  }
  /**
   * Llena un linkBox con los datos de los tipos de IVA existentes.
   * Tambien le da el formato adecuado al linkBox
   * @param lkbox
   * @param dt
   * @throws SQLException
   */
  public static void llenaLinkBoxIVA (CLinkBox lkbox,DatosTabla dt) throws SQLException
  {
    if (dt.select("SELECT tii_codi,tii_iva,tii_rec FROM tiposiva "+
            " where "+Formatear.getSQLDate(Formatear.getDateAct())+" between tii_fecini and tii_fecfin "+
            " ORDER BY tii_iva"))
    {
      do
      {
        lkbox.addDatos(dt.getString("tii_codi"),"Iva: "+dt.getDouble("tii_iva")+
                " ..Req.Eq. "+dt.getDouble("tii_rec"));
      } while (dt.next());
    }
    lkbox.setFormato(true);
    lkbox.setFormato(Types.DECIMAL,"#9",2);

  }
  public static void llenaComboIVA(CComboBox cb,DatosTabla dt)  throws SQLException
  {
    dt.select("SELECT tii_codi,tii_iva from tiposiva "+
            " where "+Formatear.getSQLDate(Formatear.getDateAct())+" between tii_fecini and tii_fecfin "+
            " ORDER BY tii_iva");
    cb.setDatos(dt);
  }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Pprinc = new gnu.chu.controles.CPanel();
        cLabel1 = new gnu.chu.controles.CLabel();
        tii_ivaE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#9.99");
        cLabel2 = new gnu.chu.controles.CLabel();
        tii_codiE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#9");
        cLabel3 = new gnu.chu.controles.CLabel();
        tii_feciniE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel4 = new gnu.chu.controles.CLabel();
        tii_recE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#9.99");
        cLabel5 = new gnu.chu.controles.CLabel();
        tii_irpfE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#9.99");
        Pcont = new gnu.chu.controles.CPanel();
        cLabel6 = new gnu.chu.controles.CLabel();
        cLabel7 = new gnu.chu.controles.CLabel();
        cLabel8 = new gnu.chu.controles.CLabel();
        cLabel9 = new gnu.chu.controles.CLabel();
        cLabel10 = new gnu.chu.controles.CLabel();
        tii_ctaivaE = new gnu.chu.controles.CTextField(Types.CHAR,"X",12);
        tii_ctaivcE = new gnu.chu.controles.CTextField(Types.CHAR,"X",12);
        tii_ctareeE = new gnu.chu.controles.CTextField(Types.CHAR,"X",12);
        tii_ctarecE = new gnu.chu.controles.CTextField(Types.CHAR,"X",12);
        tii_ctairvE = new gnu.chu.controles.CTextField(Types.CHAR,"X",12);
        tii_ctaircE = new gnu.chu.controles.CTextField(Types.CHAR,"X",12);
        Baceptar = new gnu.chu.controles.CButton();
        Bcancelar = new gnu.chu.controles.CButton();
        cLabel12 = new gnu.chu.controles.CLabel();
        tii_fecfinE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");

        Pprinc.setLayout(null);

        cLabel1.setText("% IVA ");
        Pprinc.add(cLabel1);
        cLabel1.setBounds(60, 25, 40, 18);
        Pprinc.add(tii_ivaE);
        tii_ivaE.setBounds(100, 25, 40, 18);

        cLabel2.setText("Tipo IVA ");
        Pprinc.add(cLabel2);
        cLabel2.setBounds(10, 2, 60, 18);
        Pprinc.add(tii_codiE);
        tii_codiE.setBounds(70, 2, 30, 18);

        cLabel3.setText("Fecha Inicio");
        Pprinc.add(cLabel3);
        cLabel3.setBounds(120, 2, 70, 18);
        Pprinc.add(tii_feciniE);
        tii_feciniE.setBounds(200, 2, 80, 18);

        cLabel4.setText("Recargo Equivalencia");
        Pprinc.add(cLabel4);
        cLabel4.setBounds(150, 25, 130, 18);
        Pprinc.add(tii_recE);
        tii_recE.setBounds(280, 25, 40, 18);

        cLabel5.setText("IRPF");
        Pprinc.add(cLabel5);
        cLabel5.setBounds(330, 25, 40, 18);
        Pprinc.add(tii_irpfE);
        tii_irpfE.setBounds(370, 25, 40, 18);

        Pcont.setBorder(javax.swing.BorderFactory.createTitledBorder("Cuentas Contables"));
        Pcont.setLayout(null);

        cLabel6.setBackground(new java.awt.Color(0, 0, 255));
        cLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cLabel6.setText("IVA");
        cLabel6.setOpaque(true);
        Pcont.add(cLabel6);
        cLabel6.setBounds(96, 16, 85, 15);

        cLabel7.setBackground(new java.awt.Color(0, 0, 255));
        cLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cLabel7.setText("Recargo Equiv.");
        cLabel7.setOpaque(true);
        Pcont.add(cLabel7);
        cLabel7.setBounds(199, 16, 123, 15);

        cLabel8.setBackground(new java.awt.Color(0, 0, 255));
        cLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cLabel8.setText("Recargo Equiv. + IVA");
        cLabel8.setOpaque(true);
        Pcont.add(cLabel8);
        cLabel8.setBounds(328, 16, 123, 15);

        cLabel9.setBackground(new java.awt.Color(0, 0, 255));
        cLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cLabel9.setText("VENTAS");
        cLabel9.setOpaque(true);
        Pcont.add(cLabel9);
        cLabel9.setBounds(16, 35, 60, 15);

        cLabel10.setBackground(new java.awt.Color(0, 0, 255));
        cLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cLabel10.setText("COMPRAS");
        cLabel10.setOpaque(true);
        Pcont.add(cLabel10);
        cLabel10.setBounds(16, 57, 60, 15);
        Pcont.add(tii_ctaivaE);
        tii_ctaivaE.setBounds(100, 35, 80, 18);
        Pcont.add(tii_ctaivcE);
        tii_ctaivcE.setBounds(100, 57, 80, 18);
        Pcont.add(tii_ctareeE);
        tii_ctareeE.setBounds(220, 35, 80, 18);
        Pcont.add(tii_ctarecE);
        tii_ctarecE.setBounds(220, 57, 80, 18);
        Pcont.add(tii_ctairvE);
        tii_ctairvE.setBounds(350, 35, 80, 18);
        Pcont.add(tii_ctaircE);
        tii_ctaircE.setBounds(350, 57, 80, 18);

        Pprinc.add(Pcont);
        Pcont.setBounds(0, 50, 470, 90);

        Baceptar.setText("Aceptar");
        Pprinc.add(Baceptar);
        Baceptar.setBounds(100, 150, 100, 30);

        Bcancelar.setText("Cancelar");
        Pprinc.add(Bcancelar);
        Bcancelar.setBounds(240, 150, 100, 30);

        cLabel12.setText("Fecha Final");
        Pprinc.add(cLabel12);
        cLabel12.setBounds(290, 2, 70, 18);
        Pprinc.add(tii_fecfinE);
        tii_fecfinE.setBounds(370, 2, 80, 18);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CButton Bcancelar;
    private gnu.chu.controles.CPanel Pcont;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel10;
    private gnu.chu.controles.CLabel cLabel12;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.controles.CLabel cLabel9;
    private gnu.chu.controles.CTextField tii_codiE;
    private gnu.chu.controles.CTextField tii_ctaircE;
    private gnu.chu.controles.CTextField tii_ctairvE;
    private gnu.chu.controles.CTextField tii_ctaivaE;
    private gnu.chu.controles.CTextField tii_ctaivcE;
    private gnu.chu.controles.CTextField tii_ctarecE;
    private gnu.chu.controles.CTextField tii_ctareeE;
    private gnu.chu.controles.CTextField tii_fecfinE;
    private gnu.chu.controles.CTextField tii_feciniE;
    private gnu.chu.controles.CTextField tii_irpfE;
    private gnu.chu.controles.CTextField tii_ivaE;
    private gnu.chu.controles.CTextField tii_recE;
    // End of variables declaration//GEN-END:variables
}
