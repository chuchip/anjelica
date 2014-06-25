package gnu.chu.anjelica.inventario;
 
import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import java.sql.*;
import gnu.chu.Menu.*;
import java.awt.*;
import java.util.*;
import javax.swing.BorderFactory;
import gnu.chu.camposdb.*;
import gnu.chu.interfaces.*;
import java.awt.event.*;
import gnu.chu.anjelica.almacen.actStkPart;
import gnu.chu.anjelica.almacen.paregalm;

/**
 * <p>Titulo:   Mant. de  INVENTARIO </p>
 * <p>Descripcion: Mantenimiento tabla de Inventario.</p>
 * <p>Copyright: Copyright (c) 2005-2013
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
* <p>Empresa: MISL</p>
* @author chuchiP
* @version 1.0
*/
public class pdinven extends ventanaPad implements PAD
{
  paregalm pRegAlm;
  actStkPart stkPart;
  int tirCodi;
  String fecini,tipo;
  CPanel Pprinc = new CPanel();
  CPanel Pcabe = new CPanel();
  CTextField eje_numeE = new CTextField(Types.DECIMAL, "###9");
  CTextField emp_codiE = new CTextField(Types.DECIMAL, "#9");
  CTextField pro_serieE = new CTextField(Types.CHAR, "X", 1);
  CTextField pro_nuparE = new CTextField(Types.DECIMAL, "####9");
  CTextField pro_numindE = new CTextField(Types.DECIMAL, "###9");
  CTextField prp_pesoE = new CTextField(Types.DECIMAL, "###9.99");
  CTextField prp_numindE=new CTextField(Types.DECIMAL,"##9");
  CTextField rgs_prreguE =new CTextField(Types.DECIMAL,"##9.99");

  CGridEditable jt = new CGridEditable(8)
  {
        @Override
    public void cambiaColumna(int col,int colNueva, int row)
    {
      try
      {
      }
      catch (Exception k)
      {
        Error("Error al buscar Nombre Articulo", k);
      }
    }

        @Override
    public void afterCambiaLinea()
    {
      actAcumu();
    }

        @Override
    public int cambiaLinea(int row, int col)
    {
      return cambiaLineaJT();
    }
  };

  String s;
  CTextField rgs_fechaE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel1 = new CLabel();
//  CButton Baceptar = new CButton();
//  CButton Bcancelar = new CButton();
  CButton Bocul = new CButton();
  boolean modoConsulta=false;
  proPanel pro_codiE = new proPanel();
  CLabel cLabel2 = new CLabel();
  CLabel cLabel3 = new CLabel();
  CLinkBox alm_codiE = new CLinkBox();
  CPanel Pacum = new CPanel();
  CLabel cLabel4 = new CLabel();
  CTextField kilostE = new CTextField(Types.DECIMAL,"##,##9.99");
  CLabel cLabel5 = new CLabel();
  CTextField unidtE = new CTextField(Types.DECIMAL,"#,##9");
  CLabel cLabel6 = new CLabel();
  CTextField importeE = new CTextField(Types.DECIMAL,"###,##9.99");
  CLabel cLabel7 = new CLabel();
  CTextField lineasE = new CTextField(Types.DECIMAL,"##9");
  GridBagLayout gridBagLayout1 = new GridBagLayout();


  public pdinven(EntornoUsuario eu, Principal p)
  {
    this(eu,p,null);
  }

  public pdinven(EntornoUsuario eu, Principal p, Hashtable ht)
  {
      EU = eu;
      vl = p.panel1;
      jf = p;
      eje = true;

      setTitulo("Mantenimiento Inventario ");

      try
      {
        if (ht != null)
        {
          if (ht.get("modoConsulta") != null)
            modoConsulta = Boolean.valueOf(ht.get("modoConsulta").toString()).booleanValue();
        }

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

    public pdinven(gnu.chu.anjelica.menu p, EntornoUsuario eu)
    {

      EU = eu;
      vl = p.getLayeredPane();
      setTitulo("Mantenimiento Inventario");
      eje = false;

      try
      {
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
      this.setSize(new Dimension(502,529));
      this.setVersion("2010-06-25");

      statusBar = new StatusBar(this);
      nav = new navegador(this, dtCons, false, navegador.NORMAL);
      conecta();
      if (modoConsulta)
      {
        nav.removeBoton(navegador.ADDNEW);
        nav.removeBoton(navegador.EDIT);
        nav.removeBoton(navegador.DELETE);
      }
      Pprinc.setLayout(gridBagLayout1);
      Pcabe.setBorder(BorderFactory.createRaisedBevelBorder());
      Pcabe.setMaximumSize(new Dimension(485, 45));
      Pcabe.setMinimumSize(new Dimension(485, 45));
      Pcabe.setPreferredSize(new Dimension(485, 45));
      Pcabe.setLayout(null);
      kilostE.setEnabled(false);
      unidtE.setEnabled(false);
      importeE.setEnabled(false);
      lineasE.setEnabled(false);

      Vector cabecera = new Vector();
      cabecera.addElement("Ejer."); // 0 -- Ejerc.
      cabecera.addElement("Emp"); //1 -- Emp.
      cabecera.addElement("Serie"); // 2 -- Serie
      cabecera.addElement("Lote"); // 3 -- Lote
      cabecera.addElement("Ind"); // 4 -- Indiv.
      cabecera.addElement("Kgs"); // 5 -- Kgs.
      cabecera.addElement("Un."); // 6 Unid.
      cabecera.addElement("Precio"); // 7 -- Precio

      jt.setCabecera(cabecera);
      jt.setMinimumSize(new Dimension(468, 401));
      jt.setAnchoColumna(new int[]
                         {50, 50, 60, 80, 80, 100, 60, 100});
      jt.alinearColumna(new int[]
                        {2, 2, 0, 2, 2, 2, 2, 2});
      Vector v1 = new Vector();
      cLabel3.setText("Almacen");
      cLabel3.setBounds(new Rectangle(130, 5, 56, 17));
      alm_codiE.setAncTexto(40);
      alm_codiE.setBounds(new Rectangle(192, 5, 287, 17));
      eje_numeE.setValorDec(EU.ejercicio);
      emp_codiE.setValorDec(EU.em_cod);
      pro_serieE.setText("A");
      prp_numindE.setValorDec(1);
      pro_codiE.iniciar(dtStat, this, vl, EU);
      pro_codiE.setAceptaInactivo(false);
      Pacum.setBorder(BorderFactory.createLoweredBevelBorder());
    Pacum.setMaximumSize(new Dimension(491, 23));
    Pacum.setMinimumSize(new Dimension(491, 23));
    Pacum.setPreferredSize(new Dimension(491, 23));
    Pacum.setLayout(null);
    cLabel4.setText("Kilos");
    cLabel4.setBounds(new Rectangle(3, 3, 35, 16));
    kilostE.setBounds(new Rectangle(40, 3, 74, 16));
    cLabel5.setText("Unidades");
    cLabel5.setBounds(new Rectangle(127, 3, 54, 16));
    unidtE.setBounds(new Rectangle(185, 3, 48, 16));
    cLabel6.setText("Importe");
    cLabel6.setBounds(new Rectangle(239, 3, 47, 16));
    importeE.setBounds(new Rectangle(290, 3, 85, 16));
    cLabel7.setText("Lineas");
    cLabel7.setBounds(new Rectangle(384, 3, 39, 16));
    lineasE.setBounds(new Rectangle(429, 3, 46, 16));
    rgs_fechaE.setText("");
    v1.add(eje_numeE); // 0
    v1.add(emp_codiE); // 1
    v1.add(pro_serieE); // 2
    v1.add(pro_nuparE); // 3
    v1.add(pro_numindE); // 4
    v1.add(prp_pesoE); //5
    v1.add(prp_numindE); // 6
    v1.add(rgs_prreguE); // 7
    jt.setCampos(v1);
    jt.setFormatoColumna(0, "###9");
    jt.setFormatoColumna(1,"#9");
    jt.setFormatoColumna(3, "####9");
    jt.setFormatoColumna(5, "###9.99");
    jt.setFormatoColumna(6, "##9");
    jt.setFormatoColumna(7, "##9.99");

    jt.setMaximumSize(new Dimension(468, 401));
    jt.setPreferredSize(new Dimension(468, 401));
    jt.setAjustarGrid(true);
    jt.setAjusAncCol(true);
    jt.setNumRegCargar(100);
    iniciar(this);
    Baceptar.setText("F4 Aceptar");
    Baceptar.setMaximumSize(new Dimension(100, 26));
    Baceptar.setMinimumSize(new Dimension(100, 26));
    Baceptar.setPreferredSize(new Dimension(100, 26));
    Bcancelar.setMaximumSize(new Dimension(100, 26));
    Bcancelar.setMinimumSize(new Dimension(100, 26));
    Bcancelar.setPreferredSize(new Dimension(100, 26));
    rgs_fechaE.setBounds(new Rectangle(41, 5, 85, 17));
    cLabel1.setText("Fecha");
    cLabel1.setBounds(new Rectangle(3, 5, 40, 17));
    Bocul.setBounds(new Rectangle(453, 28, 1, 1));
    Pprinc.setInputVerifier(null);
    pro_codiE.setBounds(new Rectangle(58, 24, 417, 17));
    cLabel2.setText("Producto");
    cLabel2.setBounds(new Rectangle(5, 27, 54, 17));
    this.getContentPane().add(nav, BorderLayout.NORTH);
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);
    Pprinc.add(Bcancelar, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
                                                 , GridBagConstraints.EAST,
                                                 GridBagConstraints.NONE,
                                                 new Insets(2, 0, 0, 50), 0, 0));
    Pprinc.add(jt,   new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 1, 0, 0), 0, 0));
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
    Pcabe.add(Bocul, null);
    Pcabe.add(cLabel2, null);
    Pcabe.add(pro_codiE, null);
    Pcabe.add(cLabel1, null);
    Pcabe.add(rgs_fechaE, null);
    Pcabe.add(alm_codiE, null);
    Pcabe.add(cLabel3, null);
    Pprinc.add(Pacum,   new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 1, 0, 0), 0, 0));
    Pacum.add(cLabel5, null);
    Pacum.add(unidtE, null);
    Pacum.add(cLabel6, null);
    Pacum.add(importeE, null);
    Pacum.add(cLabel7, null);
    Pacum.add(lineasE, null);
    Pacum.add(cLabel4, null);
    Pacum.add(kilostE, null);
    Pprinc.add(Pcabe,   new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(2, 0, 0, 1), 0, 0));
    Pprinc.add(Baceptar, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
                                                , GridBagConstraints.WEST,
                                                GridBagConstraints.NONE,
                                                new Insets(2, 50, 2, 0), 0, 0));
  }

  public void iniciarVentana() throws Exception
  {
    alm_codiE.setFormato(true);
    alm_codiE.setFormato(Types.DECIMAL, "#9", 2);

    stkPart = new actStkPart(dtAdd,EU.em_cod);
    stkPart.setVentana(this);

    s = "SELECT alm_codi,alm_nomb FROM v_almacen " +
        " ORDER BY alm_codi";
    dtStat.select(s);
    alm_codiE.addDatos(dtStat);
    Pcabe.setDefButton(Baceptar);
    Pcabe.setEscButton(Bcancelar);

    Pcabe.setButton(KeyEvent.VK_F4, Baceptar);
    jt.setButton(KeyEvent.VK_F4, Baceptar);

     rgs_fechaE.setColumnaAlias("rgs_fecha");
    pro_codiE.setColumnaAlias("pro_codi");
    alm_codiE.setColumnaAlias("alm_codi");
    pRegAlm=new paregalm();
    pRegAlm.iniciar(EU,dtStat,dtAdd,vl,this,dtBloq);
    activarEventos();
    activar(false);
    verDatos();
    nav.requestFocus();
    Pprinc.setDefButton(Baceptar);
//      Pprinc.setEscButton(Bcancelar);
    }

    void activarEventos()
    {
      Bocul.addFocusListener(new FocusAdapter()
      {
        public void focusGained(FocusEvent e)
        {
          irGrid();
        }
      });
    }

    int actFecha(java.util.Date fecha)
    {
      GregorianCalendar gc = new GregorianCalendar();
      gc.setTime(fecha);
      return gc.get(GregorianCalendar.WEEK_OF_YEAR);
    }

    void irGrid()
    {
      if (nav.pulsado!=navegador.ADDNEW && nav.pulsado!=navegador.EDIT)
        return;
      if (rgs_fechaE.isNull())
      {
        mensajeErr("Introduzca Fecha Inventario");
        rgs_fechaE.requestFocus();
        return;
      }
      try {
        if (!pro_codiE.controla(true))
        {
          mensajeErr(pro_codiE.getMsgError());
          return;
        }
        if (alm_codiE.getError())
        {
          mensajeErr("ALMACEN NO ES VALIDO");
          return;
        }
        if (nav.pulsado == navegador.ADDNEW)
        {
          s = "SELECT * FROM v_regstock  "+
              " WHERE rgs_fecha = TO_DATE('" + rgs_fechaE.getText() + "','dd-MM-yyyy') " +
              " AND pro_codi = " + pro_codiE.getValorInt()+
              " and alm_codi = "+alm_codiE.getValorInt();
          if (dtCon1.select(s))
          {
            verDatLin(rgs_fechaE.getText(), pro_codiE.getValorInt(),alm_codiE.getValorInt());
            jt.cargaTodo();
            nav.pulsado = navegador.EDIT;
            mensaje("Editando ... ");
          }
        }

        Pcabe.setEnabled(false);
        Baceptar.setEnabled(true);
        jt.setEnabled(true);
        jt.requestFocusInicio();
      } catch (Exception k)
      {
        Error("ERROR al ir al Grid",k);
      }
    }

    void guardaDatos(String fecha,int proCodi,int almCodi)
    {
      String s;
      try {
        int rgsNume=-1;
        borDatos(fecha,proCodi,almCodi);
        dtAdd.addNew("v_regstock");
        if (!dtAdd.getMetaData().isAutoIncrement(dtAdd.getNomCol("rgs_nume")))
         {
           s = "SELECT MAX(rgs_nume) as rgs_nume FROM v_regstock";
           dtStat.select(s);
           rgsNume = dtStat.getInt("rgs_nume", true) + 1;
//     debug("Campo rgsNume NO isAutoIncrement");
         }
        int sbeCodi=1;
        int nRow = jt.getRowCount();
        for (int n = 0; n < nRow; n++)
        {
          if ( jt.getValorDec(n,5)==0 || jt.getValorDec(n,6)==0)
            continue;
          pRegAlm.setRegNume(++rgsNume);
          pRegAlm.insRegistro(rgs_fechaE.getFecha("dd-MM-yyyy"), pro_codiE.getValorInt(),
                            jt.getValorInt(n,1), jt.getValorInt(n,0),
                            jt.getValString(n,2),jt.getValorInt(n,3),
                            jt.getValorInt(n,4),jt.getValorInt(n,6),
                            jt.getValorDec(n,5), almCodi, tirCodi,
                            0, "", jt.getValorDec(n,7), null,0,sbeCodi,1,0,0,"",0,0);
          pRegAlm.setRegNume(++rgsNume);

        }
        stkPart.regeneraStock(dtBloq,2,almCodi,rgs_fechaE.getText(),pro_codiE.getValorInt());
        dtAdd.executeUpdate("update ajustedb set aju_regacu=1"); // Habilito Reg. Acum.
        ctUp.commit();
        if (jf != null)
        {
          jf.ht.clear();
          jf.ht.put("%f", rgs_fechaE.getText());
          jf.ht.put("%p", pro_codiE.getText());
          jf.ht.put("%a", alm_codiE.getText());
          jf.guardaMens("I5", jf.ht);
        }

        mensajeErr("Datos ... Guardados");
      } catch (Exception k)
      {
        Error("Error en La insercion de Referencias",k);
        return;
      }
    }
    /**
     * Borrar datos
     * @param fecha String
     * @param proCodi int
     * @param almCodi int
     * @throws SQLException
     * @throws ParseException
     */
    void  borDatos(String fecha,int proCodi,int almCodi) throws SQLException,java.text.ParseException
    {
      s = "DELETE FROM v_regstock " +
          " WHERE rgs_fecha = TO_DATE('" + fecha + "','dd-MM-yyyy') " +
          " AND pro_codi = " + proCodi+
          " and tir_codi = "+tirCodi+
          " and alm_codi = "+almCodi;

      stUp.executeUpdate(dtAdd.parseaSql(s));
      dtAdd.executeUpdate("update ajustedb set aju_regacu=0"); // Deshabilito Reg. Acum.
      s = "UPDATE stockpart set stp_unact = 0,stp_kilact= 0 " +
          " where alm_codi = " + almCodi+
          " and pro_codi = "+proCodi;      
      dtAdd.executeUpdate(s, stUp);
//      s = "UPDATE v_articulo set pro_stock = 0, pro_stkuni = 0 " +
//          " where pro_codi = "+proCodi;
//      dtAdd.executeUpdate(s, stUp);
    }

    void verDatos()
    {
      try
      {
        if (dtCons.getNOREG())
          return;
        rgs_fechaE.setText(dtCons.getFecha("rgs_fecha","dd-MM-yyyy"));
        pro_codiE.setText(dtCons.getString("pro_codi"));
        alm_codiE.setText(dtCons.getString("alm_codi"));
        verDatLin(rgs_fechaE.getText(),pro_codiE.getValorInt(),alm_codiE.getValorInt());
      } catch (Exception k)
      {
        Error("Error al ver datos",k);
        return;
      }
    }

    void verDatLin(String fecha,int proCodi,int almCodi) throws Exception
    {
      /*
             cabecera.addElement("Ejer."); // 0 -- Ejerc.
      cabecera.addElement("Emp"); //1 -- Emp.
      cabecera.addElement("Serie"); // 2 -- Serie
      cabecera.addElement("Lote"); // 3 -- Lote
      cabecera.addElement("Ind"); // 4 -- Indiv.
      cabecera.addElement("Kgs"); // 5 -- Kgs.
      cabecera.addElement("Un."); // 6 Unid.
      cabecera.addElement("Precio"); // 7 -- Precio

          */
      s = "SELECT eje_nume,emp_codi,pro_serie,pro_nupar,pro_numind,rgs_kilos,rgs_canti,rgs_prregu " +
          " FROM v_regstock " +
          " WHERE rgs_fecha = TO_DATE('"+fecha+"','dd-MM-yyyy') "+
          " AND pro_codi = "+proCodi+
          " and alm_codi = "+almCodi+
          " and tir_codi = "+tirCodi+
          " order by eje_nume,emp_codi,pro_serie,pro_nupar,pro_numind";
      if (jt.isEnabled())
        jt.setEnabled(false);
      if (! dtCon1.select(s))
      {
        mensajeErr("Registro BORRADO");
        jt.removeAllDatos();
        return;
      }
      jt.setDatos(dtCon1);
      jt.requestFocusInicio();
      actAcumu();
    }
    public void activar(boolean act)
    {
      activar(navegador.TODOS,act);
    }
    void activar(int modo,boolean act)
    {
      if (modo==navegador.TODOS)
        jt.setEnabled(act);
      Baceptar.setEnabled(act);
      Bcancelar.setEnabled(act);
      Pcabe.setEnabled(act);
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
  public void PADQuery() {
    activar(navegador.QUERY, true);
    Pcabe.setQuery(true);
    Pcabe.resetTexto();
    rgs_fechaE.requestFocus();

  }

  public void ej_query1() {
    Baceptar.setEnabled(false);
    Vector v=new Vector();
    v.addElement(rgs_fechaE.getStrQuery());
    v.addElement(pro_codiE.getStrQuery());
    v.addElement(alm_codiE.getStrQuery());

    Pcabe.setQuery(false);
    s = "SELECT pro_codi,rgs_fecha,alm_codi FROM v_regstock";
    s=creaWhere(s,v);
    s += " group by pro_codi,rgs_fecha,alm_codi" +
        " order by rgs_fecha,pro_codi,alm_codi";
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
      mensajeErr("Nuevos Registros selecionados ...");
    }
    catch (Exception ex)
    {
      fatalError("Error al buscar Tarifas: ", ex);
    }

  }

  public void canc_query() {
    Pcabe.setQuery(false);

    mensaje("");
    mensajeErr("Consulta ... CANCELADA");
    activaTodo();
    verDatos();
  }

  public void PADEdit() {
    if (dtCons.getNOREG())
    {
      mensajeErr("NO HAY REGISTROS SELECIONADOS");
      activaTodo();
      return;
    }
    jt.cargaTodo();
    activar(true);
    Pcabe.setEnabled(false);
    jt.requestFocusFinal();
    jt.mueveSigLinea(0);
    mensaje("Editando ....");
  }

  public void ej_edit1() {
    jt.salirGrid();
    jt.actualizarGrid();
    if (cambiaLineaJT()>=0)
    {
      jt.requestFocusSelected();
      return;
    }
    new miThread("")
    {
      public void run()
      {
        ej_edit2();
      }
    };
  }
  void ej_edit2()
  {
    this.setEnabled(false);
    mensaje("Espere, por favor... Guardando datos");
    guardaDatos(rgs_fechaE.getText(),pro_codiE.getValorInt(),alm_codiE.getValorInt());
    activaTodo();
    verDatos();
    mensajeErr("Datos Guardados...");
    mensaje("");
  }

  public void canc_edit() {
    activaTodo();
    mensajeErr("Modificacion de Datos ... Cancelada");
    verDatos();
    mensaje("");
  }
  public void PADAddNew() {
    Pcabe.resetTexto();
    jt.removeAllDatos();
    activar(navegador.QUERY, true);
    alm_codiE.setText("1");
    Baceptar.setEnabled(false);
//    eje_numeE.setValorDec(EU.ejercicio);
    rgs_fechaE.requestFocus();
    mensaje("Insertando ....");
  }

  public void ej_addnew1() {
    jt.procesaAllFoco();
    jt.salirFoco();
    if (cambiaLineaJT()>=0)
    {
      jt.requestFocusSelected();
      return;
    }
    guardaDatos(rgs_fechaE.getText(),pro_codiE.getValorInt(),alm_codiE.getValorInt());
    activaTodo();
    verDatos();
    mensaje("");
  }
  public void canc_addnew() {
    activaTodo();
    mensajeErr("Insercion de Datos ... Cancelada");
    verDatos();
    mensaje("");
  }
  public void PADDelete() {
    Baceptar.setEnabled(true);
    Bcancelar.setEnabled(true);
    Bcancelar.requestFocus();
    mensaje("Borrando ....");
  }
  public void ej_delete1() {
    try
    {
      borDatos(rgs_fechaE.getText(), pro_codiE.getValorInt(),alm_codiE.getValorInt());
      ctUp.commit();
    } catch (Exception k)
    {
      Error("Error al borrar datos",k);
    }
    activaTodo();
    verDatos();
    mensaje("");
    mensajeErr("Datos .... Borrados");
  }
  public void canc_delete() {
    activaTodo();
    verDatos();
    mensaje("");
    mensajeErr("Borrado de Registro ... ANULADO");
  }
  void actAcumu()
  {
    int nRow=jt.getRowCount();
    double kilos=0;
    double importe=0;
    double unid=0;
    for (int n=0;n<nRow;n++)
    {
      kilos+=jt.getValorDec(n,5);
      unid+=jt.getValorDec(n,6);
      importe+=jt.getValorDec(n,5)*jt.getValorDec(n,7);
    }
    kilostE.setValorDec(kilos);
    unidtE.setValorDec(unid);
    importeE.setValorDec(importe);
    lineasE.setValorDec(nRow);
  }
  int cambiaLineaJT()
  {
    if (prp_pesoE.getValorDec()==0 || prp_numindE.getValorInt()==0)
      return -1; // Sin kilos o No Individuos PASO
    try {
      if (eje_numeE.getValorInt() == 0)
      {
        msgBox("Introduzca el Ejercicio del Lote");
        return 0;
      }
      if (emp_codiE.getValorInt() == 0)
      {
        msgBox("Introduzca la empresa del Lote");
        return 1;
      }

      if (pro_serieE.getText().trim().equals(""))
      {
        msgBox("Introduzca la Serie del Lote");
        return 2;
      }
      if (pro_nuparE.getValorDec() == 0)
      {
        msgBox("Introduzca el numero de partida del Lote");
        return 3;
      }
      if (prp_pesoE.getValorDec() == 0)
      {
        msgBox("Introduzca el peso");
        return 4;
      }

    } catch (Exception k)
    {
      Error("ERROR AL controlar Linea del Grid",k);
      return 0;
    }
    return -1;
  }

  public void afterConecta() throws SQLException, java.text.ParseException
  {
    s = "select  tir_codi from v_motregu where tir_afestk = '='";
    if (!dtCon1.select(s))
      throw new SQLException("No encontrado Motivo Regulacion '='");

    tirCodi = dtCon1.getInt("tir_codi");

    strSql = "SELECT pro_codi,rgs_fecha,alm_codi FROM v_regstock " +
        " where tir_codi = '" + tirCodi + "'" +
        " group by pro_codi,rgs_fecha,alm_codi" +
        " order by rgs_fecha,pro_codi,alm_codi";
  }
  public void rgSelect() throws SQLException
   {
     super.rgSelect();
     if (!dtCons.getNOREG())
     {
       dtCons.last();
       nav.setEnabled(navegador.ULTIMO, false);
       nav.setEnabled(navegador.SIGUIENTE, false);
     }
   }

}
