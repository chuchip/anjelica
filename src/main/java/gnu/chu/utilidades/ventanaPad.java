package gnu.chu.utilidades;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import gnu.chu.interfaces.*;
import gnu.chu.controles.*;
import gnu.chu.sql.*;

public class ventanaPad extends ventana
{  
  public boolean swThread = false; // Ejecutar los ej_addnew1, ej_edit1, etc en background (true) o no (false)
  public CButton Baceptar = new CButton("Aceptar F4", Iconos.getImageIcon("check"));
  public CButton Bcancelar = new CButton("Cancelar",
                                         Iconos.getImageIcon("cancel"));
  protected BorderLayout borderLayout1 = new BorderLayout();
  public navegador nav;
  public boolean alta = false;
  public PAD pad;
  public static final int ALL = 0;
  public static final int INDICE = 1;
  public static final int RESTO = 2;

  // Variables de Base de datos.
  public String strSql;
  public DatosTabla dtCons = new DatosTabla();
  public DatosTabla dtBloq = new DatosTabla();
  public DatosTabla dtAdd = new DatosTabla();

  public int pid = 1;

  private boolean sWrgSelect = true;

  public ventanaPad()
  {
    try
    {
      Integer aux = Integer.valueOf(Thread.currentThread().getName());
      pid = aux.intValue();
    }
    catch (Exception j)
    {}
  }

  public void iniciar(PAD p, boolean activ) throws Exception
  {
    iniciar(p, activ, true);
  }
  public void setPad(PAD p)
  {
      pad=p;
  }
  
    /**
     * Iniciar Baceptar y Bcancelar.
     * Le pone los iconos el texto y le asigna el action performed
     * @param bAcep
     * @param bCanc
     */
    public void iniciarBotones(CButton bAcep, CButton bCanc) {
        Baceptar = bAcep;
        Bcancelar = bCanc;
        Baceptar.setMargin(new Insets(0, 0, 0, 0));
        Bcancelar.setMargin(new Insets(0, 0, 0, 0));

        Baceptar.setText("Aceptar F4");

        Baceptar.setIcon(Iconos.getImageIcon("check"));


        Bcancelar.setText("Cancelar");
        Bcancelar.setIcon(Iconos.getImageIcon("cancel"));

        addEventosBotones();
    }
    public void addEventosBotones() {
        Baceptar.setMnemonic('A');
        Baceptar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                pad.ej_Baceptar(e);
            }
        });

        Bcancelar.setMnemonic('C');
        Bcancelar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                pad.ej_Bcancelar(e);
            }
        });
    }
    public void iniciar(PAD p, boolean activ, boolean navegador) throws Exception {
        pad = p;
        // Inicializacion Basica para todos los programas.

        this.getContentPane().setLayout(borderLayout1);
        if (navegador) {
            this.getContentPane().add(nav, BorderLayout.NORTH);
        }
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        navActivarAll();
        dtCons.setLanzaDBCambio(false);
        iniciarBotones(Baceptar, Bcancelar);
//        addEventosBotones();

        if (activ) {
            pad.activar(false);
        }
    }

  public void iniciar(PAD p) throws Exception
  {
    iniciar(p, true);
  }

  /**
   * @deprecated Usar iniciar(PAD) (con minusculas)
   *
   * @param p PAD  Ventana
   * @throws Exception si no puede con ello.
   */
  public void Iniciar(PAD p) throws Exception
  {
    iniciar(p, true);
  }

  /**
   * Regenera la select, actual.
   */
  public void rgSelect() throws SQLException
  {
    strSql = strSql.trim();
    if (!strSql.toUpperCase().startsWith("SELECT"))
    {
      dtCons.addNew(strSql);
      dtCons.setNOREG(true);
      return;
    }
    if (dtCons.select(strSql, false) == false)
      mensajeErr(" -- NO ENCONTRADOS REGISTROS -- ");
  }

    @Override
  public void matar(boolean cerrarConexion)
  {
    super.matar(cerrarConexion);
  }

  /**
   * Asigna si se tiene que lanzar el rgSelect
   * cuando conecte con la base de datos
   *
   * Por defecto si conecta
   *
   */
  public void setLanzaRgSelect(boolean sino)
  {
    sWrgSelect = sino;
  }

  /**
   * Retorna si realiza un rgSelect cuando
   * conecte con la base de datos
   *
   *
   */
  public boolean getLanzaRgSelect()
  {
    return sWrgSelect;
  }

  /**
   * Conecta a La base de Datos.
   */
    @Override
  public void conecta() throws SQLException, ClassNotFoundException,
      IllegalAccessException, InstantiationException, java.text.ParseException
  {
//    debug("(ventanaPad) Iniciando Conexion a Base Datos ");
    super.conecta();
    dtCons.setConexion(ct); // Conexion para la select for update.
    dtBloq.setConexion(ct); // Conexion para la select for update.
    dtAdd.setConexion(ct); // Conexion para  Insert, Updates y deletes.
    afterConecta();
    if (sWrgSelect)
      rgSelect();
  }

  public void afterConecta() throws SQLException, java.text.ParseException
  {

  }

  /**
   * Monta las condiciones where de una Select.
   */
  public String creaWhere(String sel, String c[])
  {
    return creaWhere(sel, c, c.length);
  }

  public String creaWhere(String sel, String c[], boolean incEmp)
  {
    return creaWhere(sel, c, c.length, incEmp);
  }

  public String creaWhere(String sel, String c[], int nc)
  {
    return creaWhere(sel, c, nc, true);
  }

  /**
   * Monta las condiciones where de una Select.
     * @param sel String con la select base
     * @param c Array de Strings con los valores
     * @param nc Numero de Campos
     * @param incEmp Incluir Empresa
     * @return 
   */
  public String creaWhere(String sel, String c[], int nc, boolean incEmp)
  {
    int n;
    String s = "";
    for (n = 0; n < nc; n++)
    {
      if (c[n] == null) // Añadido por si acaso
        continue;
      if (c[n].compareTo("") == 0)
        continue;
      if (s.compareTo("") == 0)
        s = c[n];
      else
        s = s + " AND " + c[n];

    }
    if (!s.equals(""))
    {
      if (incEmp)
        s = sel + " WHERE emp_codi = " + EU.em_cod + " AND " + s;
      else
        s = sel + " WHERE " + s;
    }
    else
    {
      if (incEmp)
        s = sel + " WHERE emp_codi = " + EU.em_cod;
      else
        s = sel;
    }
    return s;
  }

  /**
   * Bloquea el registro activo
     * @param s
   */
  public boolean bloqueaRegistro(String s)
  {
    try
    {
      if (!dtBloq.select(s, true))
      {
        SQLException e = new SQLException("bloqueaRegistro: Registro Seleccionado NO Encontrado\nSelect: " + s, "", 0);
        fatalError("", e);
//	   	  	fatalError("bloqueaRegistro: Registro Seleccionado NO Encontrado\n"+dtBloq.getMsgError());
        return false;
      }
    }
    catch (Exception k)
    {
      if (fatalError("bloqueaRegistro: Abrir Cursor For Update\n", k) == PopError.REINTENTAR)
      {
        return bloqueaRegistro(s);
      }
//		 		fatalError("bloqueaRegistro: Abrir Cursor For Update\n"+s+"\n"+k.getMessage());
      return false;
    }
    return true;
  }

  /**
   * Rutina a ejecutarse cuando se pulsa el BOTON Aceptar.
   */
  public void ej_Baceptar(ActionEvent e)
  {
    // Pulsado Aceptar -
    switch (nav.getPulsado())
    {
      case navegador.EDIT:
        pad.ej_edit();
        break;
      case navegador.QUERY:
        pad.ej_query();
        break;
      case navegador.ADDNEW:
        pad.ej_addnew();
        break;
      case navegador.DELETE:
        pad.ej_delete();
        break;
    }
    pulsadoAceptar();
  }
  /**
   * Rutina a ejecutarse cuando se pulsa el BOTON Cancelar
   */
  public void ej_Bcancelar(ActionEvent e)
  {
    switch (nav.getPulsado())
    {
      case navegador.EDIT:
        pad.canc_edit();
        break;
      case navegador.QUERY:
        pad.canc_query();
        break;
      case navegador.ADDNEW:
        pad.canc_addnew();
        break;
      case navegador.DELETE:
        pad.canc_delete();
        break;
    }
    pulsadoCancelar();
  }
  /**
   * Rutina a machacar si se quiere hacer algo m�s cuando se haya pulsado
   * el boton Aceptar
   */
  protected void pulsadoAceptar()  {   }
  /**
   * Rutina a machacar si se quiere hacer algo m�s cuando se haya pulsado
   * el boton Cancelar
   */
  protected void pulsadoCancelar()  { }

  /*
   * Pone la pantalla activa, poniendo el el navegador Activo, y los demas
   * Campos inactivos.
   */
  public void activaTodo()
  {
    pad.activar(false);
    this.setEnabled(true);
    navActivarAll();
    nav.requestFocus();
  }

  /**
   *  Activa Navegador
   */
  public void navActivarAll()
  {
    if (pad != null)
      pad.salirEnabled(true);
    if (nav == null)
      return;

    switch (EU.modo)
    {
      case PAD.NORMAL:
        if (dtCons.getNOREG())
        {
          nav.setEnabled(navegador.TODOS, false);
          nav.setEnabled(navegador.ADDNEW, true);
          nav.setEnabled(navegador.QUERY, true);
        }
        else
          nav.setEnabled(navegador.TODOS, true);
        break;
      case PAD.CONSULTA:
        if (dtCons.getNOREG())
        {
          mensajes.mensajeUrgente(
              "NO ENCONTRADOS REGISTROS .. Este programa se Finalizara");
          matar();
        }
        nav.setEnabled(navegador.TODOS, true);
        nav.removeBoton(navegador.ADDNEW);
        nav.removeBoton(navegador.DELETE);
        nav.removeBoton(navegador.EDIT);
        nav.setEnabled(navegador.CHOSE, true);
        validate();
        repaint();
        break;
      case PAD.ALTA:
        nav.setEnabled(navegador.TODOS, false);
        nav.pulsado = navegador.ADDNEW;
        pad.PADAddNew();
    }
  }

  /*
   * Ejecuta una select
   * Si no encuentra ningun registro devuelve false.
   * Si encuentra alg�n registro devuelve true.
   */
  public boolean ej_select(String s)
  {
    try
    {
      return (dtCon1.select(s));
    }
    catch (Exception k)
    {
      fatalError("ej_select ", k);
      return false;
    }
  }

  public void ej_addnew()
  {
    if (!checkAddNew())
      return;
    if (!swThread)
      pad.ej_addnew1();
    else
      new PADThread(pad, PAD.ej_addnew);
  }

  public boolean checkAddNew()
  {
    return true;
  }

  public void ej_edit()
  {
    if (!checkEdit())
      return;
    if (!swThread)
      pad.ej_edit1();
    else
      new PADThread(pad, PAD.ej_edit);
  }

  public boolean checkEdit()
  {
    return true;
  }

  public void ej_delete()
  {
    if (!checkDelete())
      return;
    if (!swThread)
      pad.ej_delete1();
    else
      new PADThread(pad, PAD.ej_delete);
  }

  public boolean checkDelete()
  {
    return true;
  }

  public void ej_query()
  {
    if (!checkQuery())
      return;
    if (!swThread)
      pad.ej_query1();
    else
      new PADThread(pad, PAD.ej_query);
  }

  public boolean checkQuery()
  {
    return true;
  }

  public void PADChose()
  {}

  public void PADAddNew()
  {new PADThread(pad, PAD.PADADDNEW);
  }

  public void PADAddNew1()
  {}

  public void PADEdit()
  {new PADThread(pad, PAD.PADEDIT);
  }

  public void PADEdit1()
  {}

  public void PADQuery()
  {new PADThread(pad, PAD.PADQUERY);
  }

  public void PADQuery1()
  {}

  public void PADDelete()
  {new PADThread(pad, PAD.PADDELETE);
  }

  public void PADDelete1()
  {}

  /**
   * Implementar Interface en un ventapad
      private void verDatos()
      {

      }
      public void PADPrimero() { verDatos(); }
      public void PADAnterior(){ verDatos(); }
      public void PADSiguiente(){ verDatos(); }
      public void PADUltimo(){ verDatos(); }
      public void PADQuery(){}


      public void ej_query1(){}

      public void canc_query(){}

      public void PADEdit(){}

      public void ej_edit1(){}

      public void canc_edit(){}


      public void PADAddNew(){}

      public void ej_addnew1(){}

      public void canc_addnew(){}

      public void PADDelete(){}


      public void ej_delete1(){}

      public void canc_delete(){}
   */
}
