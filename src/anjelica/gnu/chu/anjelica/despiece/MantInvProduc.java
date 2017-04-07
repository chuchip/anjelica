package gnu.chu.anjelica.despiece;


import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.almacen.StkPartid;
import gnu.chu.anjelica.almacen.pdalmace;
import gnu.chu.anjelica.listados.Listados;
import gnu.chu.anjelica.pad.MantArticulos;
import gnu.chu.anjelica.pad.pdprove;
import gnu.chu.controles.StatusBar;
import gnu.chu.interfaces.PAD;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.mensajes;
import gnu.chu.utilidades.navegador;
import gnu.chu.utilidades.ventanaPad;
import gnu.chu.winayu.ayuLote;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import java.beans.PropertyVetoException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

/**
 *
 * <p>Título: MantInvProduc</p>
 * <p>Descripción: Programa utilizado para cargar el inventario fisico de piezas para producción. 
 * Luego permitira realizar el alta de ordenes de producción sobre las piezas introducidas
 *  </p>
 * <p> Parametros:</p>
 * <p> admin: </p>
 * <p>Copyright: Copyright (c) 2005-2017
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
 * <p>Empresa: miSL</p>
 * @author chuchi P
 * @version 1.0
 */
public class MantInvProduc extends ventanaPad implements PAD
{
  utildesp utdesp;
  private final String CAMDEFECTO="LR";
  private final int JT_NUMLIN=0;
  private final int JT_PROCOD=1;
  private final int JT_PRONOMB=2;
  private final int JT_PRPEJE=3;
  private final int JT_PRPSER=4;
  private final int JT_PRPPART=5;
  private final int JT_PRPIND=6;
  private final int JT_PRPPESO=7;
  private final int JT_PRVCOD=8;
  private final int JT_PRVNOMB=9;
  private final int JT_FECSAC=10;
  private final int  JT_PAINAC=11;
  private ayuLote ayuLot = null;
 private String condLineas="";
  private String condWhere="where 1=1 ";
  boolean swKilos0 = false;
  DatosTabla dtCab;
  boolean swSal = false;
  double kgAnt=0;
  String s;
  boolean swRepLineas=false;
  boolean swAdmin=false;
  
  public MantInvProduc(EntornoUsuario eu, Principal p)
  {
    this(eu,p,null);
  }

  public MantInvProduc(EntornoUsuario eu, Principal p,Hashtable ht)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    setTitulo("Mantenimiento  Inventarios Produccion");

    try
    {
      
      ponParametros(ht);
      if (jf.gestor.apuntar(this))
        jbInit();
      else
        setErrorInit(true);
    }
    catch (Exception e)
    {
      ErrorInit(e);
      setErrorInit(true);
    }
  }

  public MantInvProduc(gnu.chu.anjelica.menu p, EntornoUsuario eu,Hashtable ht)
  {

    EU = eu;
    vl = p.getLayeredPane();
    setTitulo("Mantenimiento  Inventarios Produccion");
    eje = false;

    try
    {
      ponParametros(ht);
   
      jbInit();
    }
    catch (Exception e)
    {
      ErrorInit(e);     
    }
  }
  
  void ponParametros(Hashtable<String,String> ht)
  {
      if (ht != null)
      {
           if (ht.get("admin") != null)
            swAdmin = Boolean.valueOf(ht.get("admin"));
      }
  }
  private void jbInit() throws Exception
  {
       statusBar = new StatusBar(this);
        nav = new navegador(this, dtCons, false, navegador.NORMAL);
        
        iniciarFrame();
        this.setVersion("2017-04-03 "+(swAdmin?"Administrador":""));
       
        strSql = "SELECT * FROM cinvproduc "+
         "order by cip_fecinv,cam_codi,alm_codi";

        this.getContentPane().add(nav, BorderLayout.NORTH);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        this.setPad(this);
        dtCons.setLanzaDBCambio(false);
        conecta();
       
        initComponents();
        iniciarBotones(Baceptar, Bcancelar);
        navActivarAll();
        statusBar.add(Bimpri, new GridBagConstraints(9, 0, 1, 2, 0.0, 0.0
                                         , GridBagConstraints.EAST,
                                         GridBagConstraints.VERTICAL,
                                         new Insets(0, 5, 0, 0), 0, 0));
        this.setSize(663,524);
        
        activar(false);  
  }
 @Override
 public void iniciarVentana() throws Exception
  {
    dtCab=new DatosTabla(ctUp);
    tid_codiE.iniciar(dtStat, this, vl, EU);
    Pcabe.setButton(KeyEvent.VK_F4, Baceptar);
    jt.setButton(KeyEvent.VK_F4, Baceptar);
    Pcabe.setButton(KeyEvent.VK_F2,Birlin);
    Pcabe.setButton(KeyEvent.VK_F6,Bkilos0);
    jt.setButton(KeyEvent.VK_F6,Bkilos0);
    Pcabe.setButton(KeyEvent.VK_F5,Bgrupo);
    jt.setButton(KeyEvent.VK_F5,Bgrupo);
    cam_codiE.setFormato(true);
    cam_codiE.texto.setMayusc(true);
    cam_codiE.setFormato(Types.CHAR, "XX", 2);
    gnu.chu.anjelica.pad.pdconfig.llenaDiscr(dtCon1, cam_codiE, "AC", EU.em_cod);
    jt.setButton(KeyEvent.VK_F2, Bressub);
    alm_codiE.setFormato(true);
    alm_codiE.setFormato(Types.DECIMAL, "#9", 2);
    pdalmace.llenaLinkBox(alm_codiE, dtStat,'*');
//    s = "SELECT alm_codi,alm_nomb FROM v_almacen " +
//        " ORDER BY alm_codi";
//    dtStat.select(s);
//    alm_codiE.addDatos(dtStat);

    usu_nombE.setColumnaAlias("usu_nomb");
    cip_codiE.setColumnaAlias("cip_codi");
    cip_fecinvE.setColumnaAlias("cip_fecinv");
    cam_codiE.setColumnaAlias("cam_codi");
    alm_codiE.setColumnaAlias("alm_codi");  
    pro_codiE.setColumnaAlias("pro_codi");
    prp_anoE.setColumnaAlias("prp_ano");
    prp_serieE.setColumnaAlias("prp_seri");
    prp_partE.setColumnaAlias("prp_part");
    prp_indiE.setColumnaAlias("prp_indi");
 
    activarEventos();
    this.setEnabled(true);
    activar(false);
    nav.requestFocus();
    kiltotE.setEditable(false);
    kiltotE1.setEditable(false);
    numpesE.setEditable(false);
    numpesE1.setEditable(false);
    usu_nombE.setEditable(false);
    nlinE.setEditable(false);
    verDatos(dtCons);
  }

  void activarEventos()
  {
    Bimpri.addActionListener(new ActionListener()
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            Bimpri_actionPerformed();
        }
     });
     prp_anoE.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyPressed(KeyEvent e)
      {
        if (e.getKeyCode() == KeyEvent.VK_F3)
          ayudaLote();
      }
    });
       prp_serieE.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyPressed(KeyEvent e)
      {
        if (e.getKeyCode() == KeyEvent.VK_F3)
          ayudaLote();
      }
    });
    prp_indiE.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyPressed(KeyEvent e)
      {
        if (e.getKeyCode() == KeyEvent.VK_F3)
          ayudaLote();
      }
    });
      prp_partE.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyPressed(KeyEvent e)
      {
        if (e.getKeyCode() == KeyEvent.VK_F3)
          ayudaLote();
      }
    });
    Bkilos0.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        Bkilos0_actionPerformed();
      }
    });
 
    
    
    Bgrupo.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        Bgrupo_addActionPerformed();
      }
    });
    Birlin.addFocusListener(new FocusAdapter()
    {
            @Override
      public void focusGained(FocusEvent e)
      {
        if (nav.pulsado != navegador.QUERY)
          Birlin.doClick();
      }
    });

    Birlin.addActionListener(new ActionListener()
    {
            @Override
      public void actionPerformed(ActionEvent e)
      {
        Birlin_actionPerformed();
      }
    });
    Bressub.addActionListener(new ActionListener()
    {
            @Override
      public void actionPerformed(ActionEvent e)
      {
        Bressub_actionPerformed();
      }
    });
    prp_indiE.addFocusListener(new FocusAdapter()
    {
            @Override
      public void focusLost(FocusEvent e)
      {
        prp_indiE_focusLost();

      }
    });
  }
 /**
   * Consulta Lotes Disponibles de Productos.
   */
  public void ayudaLote()
  {
    try
    {
      if (ayuLot == null)
      {
        ayuLot = new ayuLote(EU, vl, dtCon1, pro_codiE.getValorInt())
        {
          @Override
            public void matar(boolean cerrarConexion)
           {
            ayuLot.setVisible(false);
            ej_consLote();
           }
        };
        this.getLayeredPane().add(ayuLot,1);
//        vl.add(ayuLot);
        ayuLot.setIconifiable(false);
        ayuLot.setLocation(25, 25);
        ayuLot.iniciarVentana();
      }
      ayuLot.jt.removeAllDatos();
      ayuLot.setVisible(true);
      ayuLot.muerto = false;
      ayuLot.statusBar.setEnabled(true);
      ayuLot.statusBar.Bsalir.setEnabled(true);


      this.setEnabled(false);
      this.setFoco(ayuLot);
      ayuLot.cargaGrid(pro_codiE.getText(),alm_codiE.getValorInt());
      SwingUtilities.invokeLater(new Thread()
      {
        @Override
        public void run()
        {
          ayuLot.jt.requestFocusInicio();
        }
      });

    }
    catch (Exception j)
    {
      this.setEnabled(true);
    }
  }

  void ej_consLote()
  {
    if (ayuLot.consulta)
    {
      prp_anoE.setValorDec(ayuLot.jt.getValorInt(ayuLot.rowAct, ayuLote.JT_EJE));
      prp_serieE.setText(ayuLot.jt.getValString(ayuLot.rowAct, ayuLote.JT_SER));
      prp_partE.setValorDec(ayuLot.jt.getValorInt(ayuLot.rowAct, ayuLote.JT_LOTE));
      prp_indiE.setValorDec(ayuLot.jt.getValorInt(ayuLot.rowAct, ayuLote.JT_IND));
      prp_pesoE.setText(ayuLot.jt.getValString(ayuLot.rowAct, ayuLote.JT_PESO ));
//    
      jt.setValor(ayuLot.jt.getValString(ayuLot.rowAct, ayuLote.JT_EJE), JT_PRPEJE);
      jt.setValor(ayuLot.jt.getValString(ayuLot.rowAct, ayuLote.JT_SER), JT_PRPSER);
      jt.setValor(ayuLot.jt.getValString(ayuLot.rowAct, ayuLote.JT_LOTE), JT_PRPPART);
      jt.setValor(ayuLot.jt.getValString(ayuLot.rowAct, ayuLote.JT_IND), JT_PRPIND);
      jt.setValor(ayuLot.jt.getValString(ayuLot.rowAct, ayuLote.JT_PESO), JT_PRPPESO);
    }
    ayuLot.setVisible(false);
    this.setEnabled(true);
    this.toFront();
    try
    {
      this.setSelected(true);
    }
    catch (PropertyVetoException k)
    {}
    this.setFoco(null);
    jt.requestFocusLater(jt.getSelectedRow(),JT_PRPPESO);
  }
  
  void prp_indiE_focusLost()
  {
//    if (DEBUG)
//      return;
    if (prp_indiE.getValorDec()== 0)
      return;
    try
    {
      buscaPeso(jt.getSelectedRow());   
    }
    catch (Exception k)
    {
      Error("Error al Leer Peso", k);
    }
  }

  void Bressub_actionPerformed()
  {
    numpesE1.setText("0");
    kiltotE1.setText("0");
    jt.requestFocusLater(jt.getSelectedRow(), jt.getSelectedColumn());
  }
  
  boolean buscaPeso(int linea)
  {
    try
    {
          if (utdesp==null)
            utdesp = new utildesp();
        StkPartid stkPart = utildesp.buscaPeso(dtCon1, prp_anoE.getValorInt(),
                                       EU.em_cod,
                                       prp_serieE.getText(),
                                       prp_partE.getValorInt(),
                                       prp_indiE.getValorInt(),
                                       pro_codiE.getValorInt(),
                                       alm_codiE.getValorInt());
        if (stkPart.hasError())
        {
            mensajeErr(stkPart.getMensaje());
            return false;
        }
        prp_pesoE.setValorDec(stkPart.getKilos());
        jt.setValor(stkPart.getKilos(),linea,JT_PRPPESO);
        utdesp.setStockPartidas(stkPart);
          if (!utdesp.busDatInd(prp_serieE.getText(),pro_codiE.getValorInt(),EU.em_cod,            
            prp_anoE.getValorInt(),
            prp_partE.getValorInt(),
            prp_indiE.getValorInt(), // N. Ind.
            alm_codiE.getValorInt(),
            dtCon1, dtStat, EU))
        {
            mensajeErr(utdesp.getMsgAviso());
            return false;
        }
        jt.setValor(utdesp.getFecSacrif()==null?Formatear.getDate("01-01-2000", "dd-MM-yyyy"):
            utdesp.getFecSacrif(),linea,JT_FECSAC);
        jt.setValor(utdesp.getPrvCompra(),linea,JT_PRVCOD);
        jt.setValor( pdprove.getNombPrv(utdesp.getPrvCompra(), dtStat),linea,JT_PRVNOMB);
        jt.setValor(utdesp.getAcpPainac(),linea,JT_PAINAC);
        return true;
    }
    catch (Exception k)
    {
      Error("Error al buscar Peso", k);
      return false;
    }
  }
  private boolean checkCabecera()
  {
    if (cip_fecinvE.isNull())
    {
      mensajeErr("Introduzca la fecha de Inventario");
      cip_fecinvE.requestFocus();
      return false;
    }
    if (cam_codiE.getError() || cam_codiE.getText().trim().equals(""))
    {
      mensajeErr("Camara ... incorrecta");
      cam_codiE.requestFocus();
      return false;
    }
    if (alm_codiE.getError() || alm_codiE.getValorInt() == 0)
    {
      mensajeErr("Introduzca Almacen");
      alm_codiE.requestFocus();
      return false;
    }
    return true;
  }
  
  void Birlin_actionPerformed()
  {
    
    if (!checkCabecera())
      return;
  
//    try
//    {
//      
//      if (nav.pulsado == navegador.ADDNEW)
//      {
//        s = "SELECT * FROM cinvproduc WHERE cip_fecinv = TO_DATE('" + cip_fecinvE.getText() + "','dd-MM-yyyy') " +
//            " AND cam_codi = '" + cam_codiE.getText() + "'" +
//            " and alm_codi = " + alm_codiE.getValorInt();
//
//        if (dtBloq.select(s))
//        {
//          mensajeErr("Registro ya existe .. EDITANDOLO");
//          mensaje("Editando el registro");
//          verDatos(dtBloq); // Resulta que ya existe.
//          nav.pulsado = navegador.EDIT;
//        }
//      }
//
//    }
//    catch (Exception k)
//    {
//      Error("Error al realizar select", k);
//      return;
//    }

    Pcabe.setEnabled(false);
    activar(true, navegador.EDIT);
    jt.requestFocusInicio();
  }

  /**
   * Verdatos de un datostabla. muestra cabecera y lineas.
   * @param dt DatosTabla
   */
  void verDatos(DatosTabla dt)
  {
    try
    {
      if (dt.getNOREG())
        return;
      cip_codiE.setText(dt.getString("cip_codi"));
      
      jt.setEnabled(false);
      jt.removeAllDatos();
      jtRes.removeAllDatos();
      s = "select  * from cinvproduc where  cip_codi = " + dt.getInt("cip_codi");
      if (!dtCon1.select(s))
      {
        usu_nombE.resetTexto();
        cip_fecinvE.resetTexto();
        cam_codiE.resetTexto();
        alm_codiE.resetTexto();

        msgBox("Datos de cabecera no encontrados ");
        return;
      }
      usu_nombE.setText(dtCon1.getString("usu_nomb"));
      cip_fecinvE.setDate(dtCon1.getDate("cip_fecinv"));
      cam_codiE.setText(dtCon1.getString("cam_codi"));
      alm_codiE.setText(dtCon1.getString("alm_codi"));
      cip_comentE.setText(dtCon1.getString("cip_coment"));
      tid_codiE.setText(dtCon1.getString("tid_codi"));
      jt.tableView.setVisible(false);
      s = "select  * from v_invproduc where  cip_codi = " + dt.getInt("cip_codi") +
          condLineas+
          " ORDER BY lip_numlin ";
      if (!dtCon1.select(s))
        return;
      int n=0;
      
    
      do
      {       
        ArrayList v = new ArrayList();
        v.add(dtCon1.getString("lip_numlin")); // 0
        v.add(dtCon1.getString("pro_codi")); // 1
        v.add(dtCon1.getString("pro_nomb")); // 2 
        v.add(dtCon1.getString("prp_ano")); // 3        
        v.add(dtCon1.getString("prp_seri")); // 5  
        v.add(dtCon1.getString("prp_part")); // 6 
        v.add(dtCon1.getString("prp_indi")); // 7
        v.add(dtCon1.getString("prp_peso")); // 8
        v.add(dtCon1.getString("prv_codi")); // 9
        v.add(dtCon1.getString("prv_nomb")); // 9
        v.add(dtCon1.getDate("prp_fecsac")); // 9
        v.add(dtCon1.getString("pai_codi")); // 9
        v.add(dtCon1.getFecha("lip_fecalt","dd-MM-yy HH:mm")); // 9
        jt.addLinea(v);
        n++;
      } while (dtCon1.next());
      jt.tableView.setVisible(true);
      
      jt.requestFocusInicio();
      calcDatosGrupo();
     
     
      do{          
        ArrayList v = new ArrayList();
        v.add(dtAdd.getString("pro_codi")); // 1
        v.add(dtAdd.getString("pro_nomb")); // 2 
        v.add(dtAdd.getDouble("cuantos")); // 8
        v.add(dtAdd.getDouble("peso")); // 8
        v.add(dtAdd.getString("prv_codi")); // 9
        v.add(dtAdd.getString("prv_nomb")); // 9
        v.add(dtAdd.getDate("prp_fecsac")); // 9
        v.add(dtAdd.getString("pai_codi")); // 9
        jtRes.addLinea(v);
      } while (dtAdd.next());
      calcTotales(true);
      dtAdd.executeUpdate("drop table invprdtmp ");
      dtAdd.commit();
    }
    catch (SQLException k)
    {
      Error("Error al visualizar datos", k);
    }
  }
  
  void calcDatosGrupo() throws SQLException
  {
       s="create temp table invprdtmp ("
          + " cuantos int,"
          + " peso float,"
          + " pro_codi int,"
          + " pro_nomb varchar(50),"
          + " prv_codi int,"
          + " prv_nomb varchar(50),"
          + " prp_fecsac date,"
           + "pai_codi varchar(2))";
      dtAdd.executeUpdate(s);
      s = "select  count(*) as cuantos,sum(prp_peso) as peso, pro_codi,pro_nomb,prv_codi,prv_nomb,prp_fecsac,pai_codi from v_invproduc where  cip_codi = " + cip_codiE.getValorInt() +
          condLineas+
          " group by pro_codi,pro_nomb,prv_codi,prv_nomb,prp_fecsac,pai_codi "+
          " ORDER BY prp_fecsac,pro_codi,prv_codi ";
      dtCon1.select(s);
      int redondeo=diasRedon.getValorInt();
      int proCodi;
      do
      {    
        dtAdd.addNew("invprdtmp");
        dtAdd.setDato("cuantos",dtCon1.getDouble("cuantos"));
        dtAdd.setDato("peso",dtCon1.getDouble("peso"));
        proCodi=MantArticulos.getProductoPadre(dtCon1.getInt("pro_codi"),dtStat);
        dtAdd.setDato("pro_codi",proCodi);
        dtAdd.setDato("pro_nomb",MantArticulos.getNombProd(proCodi, dtStat));
        dtAdd.setDato("prv_codi",dtCon1.getString("prv_codi"));
        dtAdd.setDato("prv_nomb",dtCon1.getString("prv_nomb"));
        GregorianCalendar gc=new GregorianCalendar();
        gc.setTime(dtCon1.getDate("prp_fecsac"));
        int dia=gc.get(GregorianCalendar.DAY_OF_YEAR);
        dia= dia/ redondeo;
        dia=dia*redondeo ;
        gc.set(GregorianCalendar.DAY_OF_YEAR,dia);
        dtAdd.setDato("prp_fecsac",gc.getTime());
        dtAdd.setDato("pai_codi",dtCon1.getString("pai_codi"));
        dtAdd.update();
      } while (dtCon1.next());
       s="select sum(cuantos) as cuantos, sum(peso) as peso,pro_codi,pro_nomb,prv_codi,prv_nomb,prp_fecsac,pai_codi "
          + "  from invprdtmp "
          + " group by pro_codi,pro_nomb,prv_codi,prv_nomb,prp_fecsac,pai_codi "
          + " ORDER BY pro_codi,prp_fecsac,prv_codi";
      dtAdd.select(s);
  }
    @Override
  public void ej_query1()
  {
    if (Pcabe.getErrorConf() != null)
    {
      mensajeErr("Error en condiciones de busqueda");
      Pcabe.getErrorConf().requestFocus();
      return;
    }
    ArrayList v = new ArrayList();

    v.add(cip_codiE.getStrQuery());
    v.add(cip_fecinvE.getStrQuery());
    v.add(cam_codiE.getStrQuery());
    v.add(usu_nombE.getStrQuery());
    v.add(alm_codiE.getStrQuery());
    
    v.add(pro_codiE.getStrQuery());
    v.add(prp_anoE.getStrQuery());
    v.add(prp_serieE.getStrQuery());
    v.add(prp_partE.getStrQuery());
    v.add(prp_indiE.getStrQuery());
  
       
    s = creaWhere("select distinct(cip_codi) as cip_codi from v_invproduc "+
         condWhere , v,false);
    s += " ORDER BY cip_codi";
//    debug("Query: "+s);
    
    
    try
    {
      if (!dtCons.select(s))     
      {
        mensaje("");
        msgBox("No encontrados Inventarios de Produccion estos criterios");
        activaTodo();
        Pcabe.setQuery(false);
        jt.setQuery(false);   
        rgSelect();
        verDatos(dtCons);
        return;
      }
      ArrayList v1 = new ArrayList();
      v1.add(pro_codiE.getStrQuery());
      v1.add(prp_anoE.getStrQuery());
      v1.add(prp_serieE.getStrQuery());
      v1.add(prp_partE.getStrQuery());
      v1.add(prp_indiE.getStrQuery());
     
      condLineas=creaWhere("",v1,false);
      activaTodo();
      Pcabe.setQuery(false);
      jt.setQuery(false);    
      mensaje("");
      strSql = s;
   
      rgSelect();
      verDatos(dtCons);
      mensajeErr("Nuevos registros selecionados");
    }
    catch (SQLException ex)
    {
      fatalError("Error al buscar Inventarios: ", ex);
    }
  }

 @Override
  public void canc_query()
  {
    Pcabe.setQuery(false);
    jt.setQuery(false);
    mensaje("");
    verDatos(dtCons);
    mensajeErr("Consulta ... CANCELADA");
    activaTodo();
  }

 @Override
  public void PADEdit()
  {
    int  linEdit=0;
    try
    {
      if (!condLineas.equals(""))
      {
          mensajeErr("Refrescando registro para edicion");
          s="SELECT count(*) as cuantos FROM v_invproduc where cip_codi = "+cip_codiE.getValorInt()+
             " and lip_numlin <"+   jt.getValorInt(jt.getSelectedRowDisab(),0);             
             
          dtStat.select(s);
          linEdit=dtStat.getInt("cuantos",true);
          condLineas="";
          dtCons.select("SELECT * FROM cinvproduc where cip_codi = "+cip_codiE.getValorInt());
//          rgSelect();
          verDatos(dtCons);
      }
      s = "SELECT * FROM cinvproduc WHERE  cip_codi = " + cip_codiE.getValorInt();
      if (!dtCab.select(s, true))
      {
        nav.pulsado = navegador.NINGUNO;
        mensajeErr("NO encontrado registro en inventario");
        activaTodo();
        return;
      }
    }
    catch (SQLException k)
    {
      Error("Error al Modificar datos de Inventario",k);
      return;
    }
    swSal=true;
    kgAnt=0;
    activar(true, navegador.ADDNEW);
    activar(true, navegador.EDIT);
    
    mensaje("Modificar .. Registro");
    if (linEdit==0)
    {
        jt.requestFocusFinal();
        jt.mueveSigLinea();
    }
    else
        jt.requestFocus(linEdit, 1);
    
//    Bcancelar.setEnabled(false);
    numpesE1.setText("0");
    kiltotE1.setValorDec(0);
    swSal = false;
  }

    @Override
  public void ej_edit1()
  {
    jt.salirGrid();
    if (cambiaLineaGrid(jt.getSelectedRow()) >= 0)
    {
      jt.requestFocus();
      return;
    }
    try  {
        actCabecera();
        ctUp.commit();
    }
    catch (SQLException | ParseException k)
    {
      Error("Error al Modificar cabecera de Inventario", k);
    }
    mensajeErr("Control de Inventario ... MODIFICADO");
    activaTodo();
    verDatos(dtCons);
    mensaje("");
  }
  
  void actCabecera() throws SQLException,ParseException
  {   
      dtCab.edit();
      dtCab.setDato("tid_codi", tid_codiE.getValorInt());
      dtCab.setDato("cip_fecinv", cip_fecinvE.getDate());
      dtCab.setDato("cam_codi", cam_codiE.getText());
      dtCab.setDato("alm_codi", alm_codiE.getValorInt());
      dtCab.setDato("cip_coment", cip_comentE.getText());
      dtCab.update();
  }
    @Override
  public void canc_edit()
  {
    mensaje("");
    mensajeErr("Modificación ... CANCELADA");
    verDatos(dtCons);
    activaTodo();
  }

    @Override
  public void PADQuery()
  {
    activar(true, navegador.QUERY);
    Baceptar.setEnabled(true);
    Birlin.setEnabled(false);
    usu_nombE.setEnabled(true);
    Pcabe.setQuery(true);
    Pcabe.resetTexto();
      
    jt.setQuery(true);
    jt.removeAllDatos();  
    jt.ponValores(0);
    jt.salirGrid();
    jt.setEnabled(true);
    cip_fecinvE.requestFocus();
    mensaje("Introduzca Criterios de Busqueda");
  }

 @Override
  public void PADAddNew()
  {    
    activar(true, navegador.ADDNEW);
   
    jt.removeAllDatos();
    mensaje("Insertar Nuevo Registro");
    kgAnt=0;
    Pcabe.resetTexto();
    usu_nombE.setText(EU.usuario);
    cip_fecinvE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
    cam_codiE.setText(CAMDEFECTO);
    jt.removeAllDatos();
    kiltotE.setValorDec(0);
    kiltotE1.setValorDec(0);
    numpesE.setValorDec(0);
    numpesE1.setValorDec(0);
    cip_codiE.setText("");
    swSal = false;
    cip_fecinvE.requestFocus();
  }

 @Override
  public void ej_addnew1()
  {
    jt.salirGrid();
    if (cambiaLineaGrid( -1) >= 0)
    {
      jt.requestFocus();
      return;
    }

    mensaje("");
    try
    {
        s = "SELECT * FROM cinvproduc WHERE  cip_codi = " + cip_codiE.getValorInt();
        dtCab.select(s, true);
        actCabecera();
        ctUp.commit();
      if (dtCons.getNOREG())
        rgSelect();
      activaTodo();
      mensajeErr("Control de Inventario ... Insertado");
      verDatos(dtCons);
    }
    catch (Exception k)
    {
      Error("Error al Insertar datos", k);
      return;
    }
    nav.pulsado = navegador.NINGUNO;
  }

  int cambiaLineaGrid(int row)
  {
    try
    {
        if (swSal)
            return -1;

      /**
       * @todo no hacer nada en caso de que no haya cambios
       */
      if (pro_codiE.isNull())
        return -1; // Paso de Comprobar si el prod. es 0.
      if (!pro_codiE.controla(false))
      {
        msgBox(pro_codiE.getMsgError());
        return 1;
      }
      if (prp_anoE.getValorInt() == 0)
      {
        msgBox("Introduzca el Ejercicio del Lote");
        return 3;
      }

      if (jt.getValorInt(row,0) != 0 && prp_pesoE.getValorDec() == 0)
        swKilos0=true;
      if (!swKilos0)
      {
        if (prp_serieE.getText().trim().equals(""))
        {
          msgBox("Introduzca la Serie del Lote");
          return 4;
        }
        if (prp_partE.getValorInt() == 0)
        {
          msgBox("Introduzca el numero de partida del Lote");
          return 5;
        }
      
        if (prp_indiE.getValorInt() == 0)
        {

          msgBox("Introduzca Numero Individuo");
          return -1;
        }
      }
      
      swKilos0=false;
   
      // Busco lineas Repetidas
      if (row < 0)
        return -1;
      for (int n = 0; n < jt.getRowCount(); n++)
      {
        if (n == row)
          continue;      
        if (jt.getValorInt(n, JT_PROCOD) == pro_codiE.getValorInt() &&
            jt.getValorInt(n, JT_PRPEJE) == prp_anoE.getValorInt() &&
//            jt.getValInt(n, 4) == prp_empcodE.getValorInt() &&
            jt.getValString(n, JT_PRPSER).equals(prp_serieE.getText()) &&
            jt.getValorInt(n, JT_PRPPART) == prp_partE.getValorInt() &&
            jt.getValorInt(n, JT_PRPIND) == prp_indiE.getValorInt() )
        {      
              msgBox("Linea duplicada en posicion: " + (n + 1) );
              return JT_PROCOD;
        }
      }     
      if (!buscaPeso(row))
           return JT_PROCOD;          
      
      jt.setValor(""+ insLineaInv(jt.getValorInt(row,0),row),row,0);
      kiltotE.setValorDec(kiltotE.getValorDec() - kgAnt + prp_pesoE.getValorDec());
      kiltotE1.setValorDec(kiltotE1.getValorDec() - kgAnt + prp_pesoE.getValorDec());
    }
    catch (Exception k)
    {
      Error("Error al Cambiar linea", k);
      return 0;
    }
    return -1;
  }

  int insLineaInv(int nl,int row) throws Exception
  {
    dtAdd.commit(); 
    if (cip_codiE.getValorInt()==0)
      insCabInv(cip_fecinvE.getDate(),alm_codiE.getValorInt());
    
    if (nl == 0)
    {
      s = "SELECT MAX(lip_numlin) as lip_numlin from linvproduc where  cip_codi = " + cip_codiE.getValorInt();
      dtStat.select(s);
      nl = dtStat.getInt("lip_numlin", true) + 1;
//    nl++;

      dtAdd.addNew("linvproduc");    
      dtAdd.setDato("cip_codi", cip_codiE.getValorInt());
      dtAdd.setDato("lip_numlin", nl);
    }
    else
    {
      s="SELECT * FROM linvproduc WHERE cip_codi = "+cip_codiE.getValorInt()+
         " and lip_numlin = "+nl;
      if (!dtAdd.select(s,true))
      {
        msgBox("Linea de INVENTARIO no encontrada para modificar");
        aviso("Linea Inventario NO encontrada en modificacion: "+s);
        return nl;
      }
      dtAdd.edit();
    }
    dtAdd.setDato("prp_ano", prp_anoE.getValorInt());    
    dtAdd.setDato("prp_seri", prp_serieE.getText());
    dtAdd.setDato("prp_part", prp_partE.getValorInt());
    dtAdd.setDato("pro_codi", pro_codiE.getValorInt()); 
    dtAdd.setDato("prp_indi", prp_indiE.getValorInt());
    dtAdd.setDato("prp_peso", prp_pesoE.getValorDec());    
    dtAdd.setDato("prv_codi", jt.getValorInt(row,JT_PRVCOD));
    dtAdd.setDato("prp_fecsac",jt.getValDate(row,JT_FECSAC));
    dtAdd.setDato("pai_codi",jt.getValString(row,JT_PAINAC));
    dtAdd.setDato("lip_fecalt","current_timestamp");
    dtAdd.update(stUp);
    ctUp.commit();
    return nl;
  }

  int insCabInv(Date cipFecinv,int almCodi) throws SQLException
  {
    s = "SELECT MAX(cip_codi) as cip_codi from cinvproduc ";
    dtAdd.select(s);

    cip_codiE.setValorInt(dtAdd.getInt("cip_codi", true) + 1);

    dtAdd.addNew("cinvproduc");    
    dtAdd.setDato("cip_codi", cip_codiE.getValorInt());
    dtAdd.setDato("usu_nomb", EU.usuario);
    dtAdd.setDato("cip_fecinv", cipFecinv);
    dtAdd.setDato("cam_codi", cam_codiE.getText());
    dtAdd.setDato("alm_codi", almCodi);
    dtAdd.setDato("cip_coment", cip_comentE.getText());
    dtAdd.setDato("tid_codi", tid_codiE.getValorInt());
    dtAdd.update(stUp);
    return cip_codiE.getValorInt();
  }

 @Override
  public void canc_addnew()
  {
    try
    {

      if (cip_codiE.getValorInt()!=0)
      {
        if (mensajes.mensajeYesNo("Continuar DADO de ALTA", this) != mensajes.NO)
          return;

        int nReg;
        s = "delete from cinvproduc where cip_codi = " + cip_codiE.getValorInt() ;
        if ( (nReg = stUp.executeUpdate(s)) != 1)
          throw new Exception("Error al borrar control inventario .. Num. Registros Borrados: " +
                              nReg);
        s = "delete from linvproduc where cip_codi = " + cip_codiE.getValorInt() ;
        stUp.executeUpdate(s);
        ctUp.commit();
      }
    }
    catch (Exception k)
    {
      Error("Error al cancelar Alta de Inventario ", k);
      return;
    }

      mensaje("");
      mensajeErr("Insercion ... CANCELADA");
      verDatos(dtCons);
      activaTodo();
      nav.pulsado = navegador.NINGUNO;
    }

  @Override
    public void PADDelete()
    {
      Baceptar.setEnabled(true);
      Bcancelar.setEnabled(true);
      mensaje("Borrar  .. Control Inventario");
      Bcancelar.requestFocus();
    }
  @Override
    public void ej_delete1()
    {
      try
      {
        String valor = "NO";
        valor = mensajes.mensajeGetTexto("Para borrar el Inventario teclee la palabra 'BORRAR'",
                                         "Confirme BORRADO", this, valor);
        if (valor == null)
          valor = "";
        if (!valor.toUpperCase().equals("BORRAR"))
          return;
        int nReg;
        s = "delete from cinvproduc where cip_codi = " + dtCons.getInt("cip_codi");
        if ( (nReg = stUp.executeUpdate(s)) != 1)
          throw new Exception("Error al borrar control inventario .. Num. Registros Borrados: " + nReg);
        s = "delete from linvproduc where cip_codi = " + dtCons.getInt("cip_codi");
        stUp.executeUpdate(s);

        ctUp.commit();
        stUp = creaStamento(stUp);
        mensaje("Control de Inventario ... BORRADO");
        rgSelect();
        activaTodo();
      }
      catch (Exception k)
      {
        Error("ERROR AL modificar Registro", k);
      }

    }
  @Override
    public void canc_delete()
    {
      mensaje("");
      mensajeErr("Borrado ... CANCELADO");
      activaTodo();
    }
   
 @Override
    public void activar(boolean b)
    {
      activar(b, navegador.QUERY);
      activar(b, navegador.EDIT);
    }
    void activar(boolean b, int modo)
    {
      usu_nombE.setEnabled(false);
      Bimpri.setEnabled(!b);
      switch (modo)
      {
        case navegador.QUERY:
            cip_codiE.setEnabled(b);
        case navegador.ADDNEW:
          Pcabe.setEnabled(b);
          cip_fecinvE.setEnabled(b);
          Birlin.setEnabled(b);
          cam_codiE.setEnabled(b);
          Bcancelar.setEnabled(b);
       
          break;
        case navegador.EDIT:
          jt.setEnabled(b);
          Bkilos0.setEnabled(b);
          Bgrupo.setEnabled(b);
          Bressub.setEnabled(b);
          Baceptar.setEnabled(b);
          Bcancelar.setEnabled(b);
      }

    }

    void calcTotales(boolean subTotales)
    {
      int rw = jt.getRowCount();
      int nLin = 0;
      double kg = 0;

      for (int n = 0; n < rw; n++)
      {
        if (jt.getValString(n, 1).trim().equals("0"))
          continue;
        nLin++;
        kg += jt.getValorDec(n, 8);
      }
      numpesE.setValorDec(nLin);
      kiltotE.setValorDec(kg);
      if (subTotales)
      {
        numpesE1.setValorDec(0);
        kiltotE1.setValorDec(0);
      }
    }

  @Override
    public void PADPrimero()
    {
      verDatos(dtCons);
    }
@Override
    public void PADAnterior()
    {
      verDatos(dtCons);
    }
@Override
    public void PADSiguiente()
    {
      verDatos(dtCons);
    }
@Override
    public void PADUltimo()
    {
      verDatos(dtCons);
    }
  
   
    void Bgrupo_addActionPerformed()
    {
      if (jt.getSelectedRow() > 0)
      {
        pro_codiE.setText(jt.getValString(jt.getSelectedRow() - 1, JT_PROCOD));
        pro_nombE.setText(jt.getValString(jt.getSelectedRow() - 1, JT_PRONOMB));
        prp_anoE.setText(jt.getValString(jt.getSelectedRow() - 1, JT_PRPEJE));
        prp_serieE.setText(jt.getValString(jt.getSelectedRow() - 1, JT_PRPSER));
        prp_partE.setText(jt.getValString(jt.getSelectedRow() - 1, JT_PRPPART));
        jt.salirGrid();
        jt.requestFocusLater(jt.getSelectedRow(), JT_PRPPESO);
     
      }
   
    }
    

    /**
     * Borra la linea de inventario.
     * @param row int
     */
    void borraLinea(int row)
    {
      if (jt.getValorInt(row,0)==0 )
        return;
      try {
        s = "delete from linvproduc where cip_codi = " + cip_codiE.getValorInt() +
            " and lip_numlin = " + jt.getValorInt(row, JT_NUMLIN);
        int nLinAf = 0;

        nLinAf = stUp.executeUpdate(s);
        if (nLinAf != 1)
        {
          msgBox("Error al borrar Linea Inventario ("+nLinAf+")");
          aviso("Error al borrar Linea Inventario ("+ nLinAf+")\n"+s);
        }
      } catch (SQLException k)
      {
        Error("Error al borrar Lineas",k);
      }
    }

    @Override
    public void rgSelect() throws SQLException
    {
  //    dtCons.setFetchSize(50);
      super.rgSelect();
      if (!dtCons.getNOREG())
      {
        dtCons.last();
        nav.setEnabled(navegador.ULTIMO, false);
        nav.setEnabled(navegador.SIGUIENTE, false);
      }
    }

    void  Bkilos0_actionPerformed()
    {
      swKilos0=true;
      prp_pesoE.setValorDec(0);
      jt.setValor(0,JT_PRPPESO);
      jt.mueveSigLinea();
    }

    void Bimpri_actionPerformed()
    {
     if (dtCons.getNOREG())
       return;
     try {
       if (dtCons.getNOREG())
       {
         mensajeErr("NO hay Registro selecionados");
         return;
       }

       
       JasperReport jr;
       java.util.HashMap mp = Listados.getHashMapDefault();
       mp.put("fecinv",cip_fecinvE.getDate());
       mp.put("id",cip_codiE.getValorInt());
       mp.put("coment",cip_comentE.getText());
       mp.put("alm_codi",alm_codiE.getValorInt());
       mp.put("alm_nomb",alm_codiE.getTextCombo());
       mp.put("cam_codi",cam_codiE.getText());
       mp.put("cam_nomb",cam_codiE.getTextCombo());
       jr = Listados.getJasperReport(EU,"InvenProduc");
       calcDatosGrupo();
      
       ResultSet rs;

       rs=dtCon1.getStatement().executeQuery(dtAdd.parseaSql(s));

       JasperPrint jp = JasperFillManager.fillReport(jr, mp,
           new JRResultSetDataSource(rs));
       gnu.chu.print.util.printJasper(jp, EU);
       dtAdd.executeUpdate("drop table invprdtmp ");
       dtAdd.commit();
     }
     catch (ParseException | JRException | SQLException | PrinterException k)
     {
       Error("Error al imprimir albaran", k);
     }
   }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pro_codiE = new gnu.chu.camposdb.proPanel(){
            @Override
            protected void despuesLlenaCampos()
            {
                jt.procesaAllFoco();
                try
                {

                    buscaPeso(jt.getSelectedRow());
                }
                catch (Exception k)
                {
                    Error("Error al buscar numero de piezas para este individuo", k);
                }
                jt.mueveSigLinea();
            }
        };
        pro_nombE = new gnu.chu.controles.CTextField(Types.CHAR,"X",35);
        prp_anoE = new gnu.chu.controles.CTextField(Types.DECIMAL, "###9");
        prp_empcodE = new gnu.chu.controles.CTextField(Types.DECIMAL, "#9");
        prp_serieE = new gnu.chu.controles.CTextField(Types.CHAR, "X", 1);
        prp_partE = new gnu.chu.controles.CTextField(Types.DECIMAL, "#9999");
        prp_indiE = new gnu.chu.controles.CTextField(Types.DECIMAL, "#999");
        prp_pesoE = new gnu.chu.controles.CTextField(Types.DECIMAL, "999.99");
        Bimpri = new gnu.chu.controles.CButton(Iconos.getImageIcon("print"));
        lip_numlinE = new gnu.chu.controles.CTextField(Types.DECIMAL, "#999");
        lip_fecaltE = new gnu.chu.controles.CTextField(Types.CHAR,"X");
        prp_fecsacE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        prv_codiE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#####9");
        prv_nombE = new gnu.chu.controles.CTextField(Types.CHAR,"X");
        pai_codiE = new gnu.chu.controles.CTextField();
        Pprinc = new gnu.chu.controles.CPanel();
        Pcabe = new gnu.chu.controles.CPanel();
        cLabel5 = new gnu.chu.controles.CLabel();
        cLabel6 = new gnu.chu.controles.CLabel();
        cip_codiE = new gnu.chu.controles.CTextField(Types.DECIMAL,"####9");
        cLabel7 = new gnu.chu.controles.CLabel();
        cam_codiE = new gnu.chu.controles.CLinkBox();
        cLabel9 = new gnu.chu.controles.CLabel();
        alm_codiE = new gnu.chu.controles.CLinkBox();
        Birlin = new gnu.chu.controles.CButton();
        cLabel8 = new gnu.chu.controles.CLabel();
        cip_comentE = new gnu.chu.controles.CTextField(Types.CHAR,"X",95);
        cip_fecinvE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        tid_codiE = new gnu.chu.camposdb.tidCodi2();
        cLabel13 = new gnu.chu.controles.CLabel();
        PtabPane1 = new gnu.chu.controles.CTabbedPane();
        jt = new gnu.chu.controles.CGridEditable(13) {
            @Override
            public void afterCambiaLinea()
            {
                //      prp_empcodE.setValorInt(emp_codiE.getValorInt());
                //      pro_codiE.setText("");
                pro_codiE.resetCambio();
                pro_codiE.setCambio();
                nlinE.setText("" + (this.getSelectedRow() + 1));
                kgAnt= jt.getValorDec(8);
            }
            @Override
            public boolean insertaLinea(int row, int col)
            {
                if (getQuery())
                return true;
                return pro_codiE.getValorInt()!=0;
            }
            @Override
            public int cambiaLinea(int row, int col)
            {

                return cambiaLineaGrid(row);
            }

            @Override
            public boolean afterInsertaLinea(boolean insLinea)
            {
                numpesE.setValorDec(numpesE.getValorDec() + 1);
                numpesE1.setValorDec(numpesE1.getValorDec() + 1);

                return true;
                //      jt.setValor("1",9);
            }

            @Override
            public boolean deleteLinea(int row, int col)
            {
                numpesE.setValorDec(numpesE.getValorDec() - 1);
                numpesE1.setValorDec(numpesE1.getValorDec() - 1);
                kiltotE.setValorDec(kiltotE.getValorDec() - prp_pesoE.getValorDec());
                kiltotE1.setValorDec(kiltotE1.getValorDec() - prp_pesoE.getValorDec());
                borraLinea(row);
                return true;
            }}
            ;
            jtRes = new gnu.chu.controles.Cgrid(8);
            Ppie = new gnu.chu.controles.CPanel();
            PAcum = new gnu.chu.controles.CPanel();
            cLabel2 = new gnu.chu.controles.CLabel();
            numpesE = new gnu.chu.controles.CTextField(Types.DECIMAL, "###9");
            cLabel3 = new gnu.chu.controles.CLabel();
            kiltotE = new gnu.chu.controles.CTextField(Types.DECIMAL, "##,##9.99");
            Bgrupo = new gnu.chu.controles.CButton("F5", Iconos.getImageIcon("fill"));
            PAcum1 = new gnu.chu.controles.CPanel();
            cLabel4 = new gnu.chu.controles.CLabel();
            numpesE1 = new gnu.chu.controles.CTextField(Types.DECIMAL, "###9");
            cLabel10 = new gnu.chu.controles.CLabel();
            kiltotE1 = new gnu.chu.controles.CTextField(Types.DECIMAL, "##,##9.99");
            Bressub = new gnu.chu.controles.CButton("F2",Iconos.getImageIcon("reload"));
            Baceptar = new gnu.chu.controles.CButton();
            Bcancelar = new gnu.chu.controles.CButton();
            Bkilos0 = new gnu.chu.controles.CButton("F6",Iconos.getImageIcon("stock_insert"));
            cLabel11 = new gnu.chu.controles.CLabel();
            nlinE = new gnu.chu.controles.CTextField(Types.DECIMAL, "###9");
            cLabel12 = new gnu.chu.controles.CLabel();
            usu_nombE = new gnu.chu.controles.CTextField(Types.CHAR, "X", 15);
            cLabel14 = new gnu.chu.controles.CLabel();
            diasRedon = new gnu.chu.controles.CTextField(Types.DECIMAL,"#9");

            prp_empcodE.setToolTipText("");

            prp_serieE.setText("A");

            prp_pesoE.setEnabled(false);

            Bimpri.setToolTipText("Imprimir Registro Activo");

            lip_numlinE.setEnabled(false);

            lip_fecaltE.setEnabled(false);

            prp_fecsacE.setEnabled(false);

            prv_codiE.setEnabled(false);

            prv_nombE.setEnabled(false);

            pai_codiE.setEnabled(false);

            Pprinc.setLayout(new java.awt.GridBagLayout());

            Pcabe.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
            Pcabe.setMaximumSize(new java.awt.Dimension(680, 49));
            Pcabe.setMinimumSize(new java.awt.Dimension(680, 49));
            Pcabe.setPreferredSize(new java.awt.Dimension(680, 49));
            Pcabe.setQuery(true);
            Pcabe.setLayout(null);

            cLabel5.setText("Tipo Desp");
            cLabel5.setPreferredSize(new java.awt.Dimension(52, 18));
            Pcabe.add(cLabel5);
            cLabel5.setBounds(10, 22, 70, 17);

            cLabel6.setText("Coment.");
            cLabel6.setPreferredSize(new java.awt.Dimension(52, 18));
            Pcabe.add(cLabel6);
            cLabel6.setBounds(330, 22, 50, 17);

            cip_codiE.setEnabled(false);
            Pcabe.add(cip_codiE);
            cip_codiE.setBounds(30, 2, 40, 17);

            cLabel7.setText("ID");
            cLabel7.setPreferredSize(new java.awt.Dimension(52, 18));
            Pcabe.add(cLabel7);
            cLabel7.setBounds(10, 2, 20, 17);

            cam_codiE.setAncTexto(30);
            cam_codiE.setFormato(true);
            cam_codiE.texto.setMayusc(true);
            cam_codiE.setFormato(Types.CHAR, "XX", 2);
            cam_codiE.combo.setPreferredSize(new Dimension(200,17));
            Pcabe.add(cam_codiE);
            cam_codiE.setBounds(250, 2, 150, 17);

            cLabel9.setText("Almacen ");
            cLabel9.setPreferredSize(new java.awt.Dimension(52, 18));
            Pcabe.add(cLabel9);
            cLabel9.setBounds(410, 2, 52, 17);

            alm_codiE.setAncTexto(30);
            alm_codiE.setFormato(true);
            alm_codiE.setFormato(Types.DECIMAL, "#9", 2);
            alm_codiE.combo.setPreferredSize(new Dimension(200,17));
            Pcabe.add(alm_codiE);
            alm_codiE.setBounds(460, 2, 200, 17);
            Pcabe.add(Birlin);
            Birlin.setBounds(665, 30, 2, 1);

            cLabel8.setText("Camara");
            cLabel8.setPreferredSize(new java.awt.Dimension(52, 18));
            Pcabe.add(cLabel8);
            cLabel8.setBounds(200, 2, 50, 17);
            Pcabe.add(cip_comentE);
            cip_comentE.setBounds(380, 22, 280, 17);
            Pcabe.add(cip_fecinvE);
            cip_fecinvE.setBounds(120, 2, 70, 17);

            tid_codiE.setAncTexto(40);
            Pcabe.add(tid_codiE);
            tid_codiE.setBounds(80, 22, 240, 17);

            cLabel13.setText("Fecha");
            cLabel13.setPreferredSize(new java.awt.Dimension(52, 18));
            Pcabe.add(cLabel13);
            cLabel13.setBounds(80, 2, 40, 17);

            Pprinc.add(Pcabe, new java.awt.GridBagConstraints());

            jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
            jt.setMaximumSize(new java.awt.Dimension(569, 169));
            jt.setMinimumSize(new java.awt.Dimension(569, 169));

            javax.swing.GroupLayout jtLayout = new javax.swing.GroupLayout(jt);
            jt.setLayout(jtLayout);
            jtLayout.setHorizontalGroup(
                jtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 962, Short.MAX_VALUE)
            );
            jtLayout.setVerticalGroup(
                jtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 167, Short.MAX_VALUE)
            );

            try {
                pro_codiE.iniciar(dtStat, this, vl, EU);
                pro_codiE.setCamposLote(prp_anoE, prp_serieE, prp_partE,
                    prp_indiE, prp_pesoE);

                pro_codiE.setProNomb(pro_nombE);
                prp_anoE.setText("" + EU.ejercicio);
                prp_empcodE.setText("" + EU.em_cod);
                prp_serieE.setText("A");

                prp_serieE.setMayusc(true);
                ArrayList v = new ArrayList();
                v.add("NL"); // 0
                v.add("Producto"); // 1
                v.add("Descripcion"); // 2
                v.add("Eje"); // 3
                v.add("Ser"); // 4
                v.add("Part"); // 5
                v.add("Ind."); // 6
                v.add("Peso"); // 7
                v.add("Prv"); // 8
                v.add("Nombre Prv"); // 9
                v.add("Fec.Sacr."); // 10
                v.add("Pais"); // 11
                v.add("Fec.Alta"); // 12
                jt.setCabecera(v);
                Vector v1 = new Vector();

                v1.add(lip_numlinE); // 0 Numero Linea
                v1.add(pro_codiE.getFieldProCodi()); // 1
                v1.add(pro_nombE); // 2
                v1.add(prp_anoE); // 3
                v1.add(prp_serieE); // 4
                v1.add(prp_partE); // 5
                v1.add(prp_indiE); // 6
                v1.add(prp_pesoE); //7
                v1.add(prv_codiE); // 8  Proveedor
                v1.add(prv_nombE); // 9 Nombre Proveedor
                v1.add(prp_fecsacE); // 10  Fecha Sacrificio
                v1.add(pai_codiE); // Pais Origen
                v1.add(lip_fecaltE); //11  Fecha Alta Linea
                jt.setCampos(v1);
                jt.setAjustarGrid(true);
                jt.setAjustarColumnas(false);
                jt.setAnchoColumna(new int[]
                    {30,45, 200, 40, 30, 50, 40, 50, 50,150,70,40,80});
                jt.setAlinearColumna(new int[]
                    {2,2, 0, 2,0, 2, 2, 2, 2, 0,1,0,0});
                jt.setFormatoCampos();
            } catch (Exception k) {
                Error ("Error al configurar grid",k);
            }
            PtabPane1.addTab("Datos", jt);

            ArrayList vr=new ArrayList();
            vr.add("Producto"); // 0
            vr.add("Descripcion"); // 1
            vr.add("NºPzas"); // 2
            vr.add("Kilos"); // 3
            vr.add("Prv"); // 4
            vr.add("Nombre Prv"); // 5
            vr.add("Fec.Sacr."); // 6
            vr.add("Pais"); // 7
            jtRes.setCabecera(vr);
            jtRes.setAlinearColumna(new int[]{2,0,2,2,2,0,1,0});
            jtRes.setAnchoColumna(new int[]{60,200,40,50,50,150,70,40});
            jtRes.setFormatoColumna(2, "###9");
            jtRes.setFormatoColumna(3, "##,##9.99");
            PtabPane1.addTab("Resumen", jtRes);

            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 1.0;
            Pprinc.add(PtabPane1, gridBagConstraints);

            Ppie.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            Ppie.setMaximumSize(new java.awt.Dimension(539, 79));
            Ppie.setMinimumSize(new java.awt.Dimension(539, 79));
            Ppie.setPreferredSize(new java.awt.Dimension(539, 79));
            Ppie.setLayout(null);

            PAcum.setBorder(javax.swing.BorderFactory.createTitledBorder("Total"));
            PAcum.setLayout(null);

            cLabel2.setText("Nº Pesadas");
            PAcum.add(cLabel2);
            cLabel2.setBounds(0, 13, 70, 15);

            numpesE.setEditable(false);
            numpesE.setBackground(new java.awt.Color(0, 255, 255));
            PAcum.add(numpesE);
            numpesE.setBounds(70, 13, 40, 17);

            cLabel3.setText("Kilos");
            PAcum.add(cLabel3);
            cLabel3.setBounds(120, 13, 30, 15);

            kiltotE.setEditable(false);
            kiltotE.setBackground(new java.awt.Color(0, 255, 255));
            PAcum.add(kiltotE);
            kiltotE.setBounds(150, 13, 70, 17);

            Ppie.add(PAcum);
            PAcum.setBounds(250, 2, 230, 40);

            Bgrupo.setText("F5");
            Bgrupo.setToolTipText("Copia los datos de la linea anterior");
            Ppie.add(Bgrupo);
            Bgrupo.setBounds(20, 42, 50, 20);

            PAcum1.setBorder(javax.swing.BorderFactory.createTitledBorder("Parcial"));
            PAcum1.setLayout(null);

            cLabel4.setText("Nº Pesadas");
            PAcum1.add(cLabel4);
            cLabel4.setBounds(0, 13, 70, 15);

            numpesE1.setEditable(false);
            numpesE1.setBackground(new java.awt.Color(0, 255, 255));
            PAcum1.add(numpesE1);
            numpesE1.setBounds(70, 13, 40, 17);

            cLabel10.setText("Kilos");
            PAcum1.add(cLabel10);
            cLabel10.setBounds(120, 13, 30, 15);

            kiltotE1.setEditable(false);
            kiltotE1.setBackground(new java.awt.Color(0, 255, 255));
            PAcum1.add(kiltotE1);
            kiltotE1.setBounds(150, 13, 70, 17);

            Ppie.add(PAcum1);
            PAcum1.setBounds(0, 2, 230, 40);

            Bressub.setText("F2");
            Bressub.setToolTipText("Pone Contadores subtotal a 0");
            Ppie.add(Bressub);
            Bressub.setBounds(480, 10, 50, 20);

            Baceptar.setText("Aceptar");
            Ppie.add(Baceptar);
            Baceptar.setBounds(300, 44, 100, 30);

            Bcancelar.setText("Cancelar");
            Ppie.add(Bcancelar);
            Bcancelar.setBounds(430, 44, 100, 30);

            Bkilos0.setText("F6");
            Bkilos0.setToolTipText("Insertar linea con kilos =0");
            Ppie.add(Bkilos0);
            Bkilos0.setBounds(80, 42, 50, 20);

            cLabel11.setText("Nº Lin");
            Ppie.add(cLabel11);
            cLabel11.setBounds(150, 42, 40, 17);

            nlinE.setEditable(false);
            nlinE.setBackground(new java.awt.Color(0, 255, 255));
            Ppie.add(nlinE);
            nlinE.setBounds(190, 42, 40, 17);

            cLabel12.setText("Redondeo");
            cLabel12.setPreferredSize(new java.awt.Dimension(52, 18));
            Ppie.add(cLabel12);
            cLabel12.setBounds(20, 60, 70, 17);

            usu_nombE.setEnabled(false);
            Ppie.add(usu_nombE);
            usu_nombE.setBounds(200, 60, 70, 17);

            cLabel14.setText("Usuario ");
            cLabel14.setPreferredSize(new java.awt.Dimension(52, 18));
            Ppie.add(cLabel14);
            cLabel14.setBounds(150, 60, 50, 17);

            diasRedon.setText("3");
            diasRedon.setToolTipText("Dias a Redondear ");
            Ppie.add(diasRedon);
            diasRedon.setBounds(80, 60, 30, 17);

            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 2;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
            Pprinc.add(Ppie, gridBagConstraints);

            getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

            pack();
        }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CButton Bcancelar;
    private gnu.chu.controles.CButton Bgrupo;
    private gnu.chu.controles.CButton Bimpri;
    private gnu.chu.controles.CButton Birlin;
    private gnu.chu.controles.CButton Bkilos0;
    private gnu.chu.controles.CButton Bressub;
    private gnu.chu.controles.CPanel PAcum;
    private gnu.chu.controles.CPanel PAcum1;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Ppie;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CTabbedPane PtabPane1;
    private gnu.chu.controles.CLinkBox alm_codiE;
    private gnu.chu.controles.CLabel cLabel10;
    private gnu.chu.controles.CLabel cLabel11;
    private gnu.chu.controles.CLabel cLabel12;
    private gnu.chu.controles.CLabel cLabel13;
    private gnu.chu.controles.CLabel cLabel14;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.controles.CLabel cLabel9;
    private gnu.chu.controles.CLinkBox cam_codiE;
    private gnu.chu.controles.CTextField cip_codiE;
    private gnu.chu.controles.CTextField cip_comentE;
    private gnu.chu.controles.CTextField cip_fecinvE;
    private gnu.chu.controles.CTextField diasRedon;
    private gnu.chu.controles.CGridEditable jt;
    private gnu.chu.controles.Cgrid jtRes;
    private gnu.chu.controles.CTextField kiltotE;
    private gnu.chu.controles.CTextField kiltotE1;
    private gnu.chu.controles.CTextField lip_fecaltE;
    private gnu.chu.controles.CTextField lip_numlinE;
    private gnu.chu.controles.CTextField nlinE;
    private gnu.chu.controles.CTextField numpesE;
    private gnu.chu.controles.CTextField numpesE1;
    private gnu.chu.controles.CTextField pai_codiE;
    private gnu.chu.camposdb.proPanel pro_codiE;
    private gnu.chu.controles.CTextField pro_nombE;
    private gnu.chu.controles.CTextField prp_anoE;
    private gnu.chu.controles.CTextField prp_empcodE;
    private gnu.chu.controles.CTextField prp_fecsacE;
    private gnu.chu.controles.CTextField prp_indiE;
    private gnu.chu.controles.CTextField prp_partE;
    private gnu.chu.controles.CTextField prp_pesoE;
    private gnu.chu.controles.CTextField prp_serieE;
    private gnu.chu.controles.CTextField prv_codiE;
    private gnu.chu.controles.CTextField prv_nombE;
    private gnu.chu.camposdb.tidCodi2 tid_codiE;
    private gnu.chu.controles.CTextField usu_nombE;
    // End of variables declaration//GEN-END:variables
}
