package gnu.chu.anjelica.inventario;
/**
 *
 * <p>Título: pdInvControl</p>
 * <p>Descripción: Programa utilizado para cargar el inventario fisico en el almacen
 *  </p>
 * <p> Parametros: <em>repLineas=true</em> permitira meter dos lineas
 * con el mismo individuo,lote y peso. Por defecto, lo permite. </p>
 * <p> <em>admin=true</em> Permitira meter Generar inventario a  partir de otro.
 * Por defecto esta deshabilitada esa caracteristica. </p>
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
import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.almacen.DatIndiv;
import gnu.chu.anjelica.almacen.ActualStkPart;
import gnu.chu.anjelica.almacen.pdalmace;
import gnu.chu.controles.StatusBar;
import gnu.chu.interfaces.PAD;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Fecha;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.mensajes;
import gnu.chu.utilidades.navegador;
import gnu.chu.utilidades.ventanaPad;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class PdInvControl extends ventanaPad implements PAD
{
 private String condLineas="";
  private  final int JT_NUMPAL=11;
  private  final int JT_NUMCAJ=10;
  private String condWhere;
  boolean swKilos0 = false;
  DatosTabla dtCab;
  boolean swSal = false;
  double kgAnt=0;
  String s;
  boolean swRepLineas=false;
  boolean swAdmin=false;
  
  public PdInvControl(EntornoUsuario eu, Principal p)
  {
    this(eu,p,null);
  }

  public PdInvControl(EntornoUsuario eu, Principal p,Hashtable ht)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    setTitulo("Mantenimiento  Inventarios de Control");

    try
    {
      if (ht != null)
      {
        if (ht.get("repLineas") != null)
          swRepLineas = Boolean.parseBoolean(ht.get("repLineas").toString());
        if (ht.get("admin") != null)
            swAdmin = Boolean.valueOf(ht.get("admin").toString());
      }

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

  public PdInvControl(gnu.chu.anjelica.menu p, EntornoUsuario eu,Hashtable ht)
  {

    EU = eu;
    vl = p.getLayeredPane();
    setTitulo("Mantenimiento  Inventarios de Control");
    eje = false;

    try
    {
      if (ht != null)
      {
          if (ht.get("repLineas") != null)
            swRepLineas = Boolean.valueOf(ht.get("repLineas").toString());
           if (ht.get("admin") != null)
            swAdmin = Boolean.valueOf(ht.get("admin").toString());
      }
      jbInit();
    }
    catch (Exception e)
    {
      ErrorInit(e);
      setErrorInit(true);
    }
  }

  private void jbInit() throws Exception
  {
       statusBar = new StatusBar(this);
        nav = new navegador(this, dtCons, false, navegador.NORMAL);
        
        iniciarFrame();
        this.setVersion("2017-05-26 "+(swAdmin?"Administrador":""));
        condWhere=" where emp_codi =  "+EU.em_cod;
        strSql = "SELECT * FROM coninvcab "+condWhere+
         "order by cci_feccon,cam_codi,alm_codi";

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
    Bcopia.setEnabled(swAdmin);
    cci_fecoriE.setEnabled(swAdmin);
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
    cci_codiE.setColumnaAlias("cci_codi");
    cci_fecconE.setColumnaAlias("cci_feccon");
    cam_codiE.setColumnaAlias("cam_codi");
    alm_codiE.setColumnaAlias("alm_codi");  
    pro_codiE.setColumnaAlias("pro_codi");
    prp_anoE.setColumnaAlias("prp_ano");
    prp_serieE.setColumnaAlias("prp_seri");
    prp_partE.setColumnaAlias("prp_part");
    prp_indiE.setColumnaAlias("prp_indi");
    lci_numpalE.setColumnaAlias("lci_numpal");
    lci_numcajE.setColumnaAlias("lci_numcaj");
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
        public void actionPerformed(ActionEvent e)
        {
            Bimpri_actionPerformed();
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
 
    
    cci_fecconE.addFocusListener(new FocusAdapter()
    {

            @Override
      public void focusLost(FocusEvent e)
      {
        if (nav.pulsado != navegador.ADDNEW)
          return;
        long nDias = Fecha.comparaFechas(cci_fecconE.getText(), "dd-MM-yyyy",
                                         Fecha.getFechaSys("dd-MM-yyyy"), "dd-MM-yyyy");
        if (nDias > 3)
          mensajes.mensajeAviso("Fecha introducida excede a la actual en mas de 3 dias");
        if (nDias < -3)
          mensajes.mensajeAviso("Fecha Introducida es inferior a la actual en mas de 3 dias");
      }
    });
    

//    Bsincr.addActionListener(new ActionListener()
//    {
//      public void actionPerformed(ActionEvent e)
//      {
//        Bsincr_actionPerformed();
//      }
//    });
   
    Bcopia.addActionListener(new ActionListener()
    {
            @Override
      public void actionPerformed(ActionEvent e)
      {
            try
            {
                String accion=Bcopia.getValor(e.getActionCommand());
                switch (accion)
                {
                    case "U":
                        fusionar();
                        break;
                    case "I":
                        importaInvProduc();
                        return;
                    case "M":
                        moverInventario();
                        break;
                    default:
                        copiarInventario();
                }
               
                activaTodo();
                nav.pulsado = navegador.NINGUNO;
            } catch (Exception ex)
            {
               Error("Error al copiar/fusionar inventario",ex);
            }
           
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

  void prp_indiE_focusLost()
  {
//    if (DEBUG)
//      return;
    if (prp_pesoE.getValorDec() != 0)
      return;
    try
    {
      if (buscaPeso())
      {
        prp_pesoE.setValorDec(dtCon1.getDouble("stp_kilact", true));
        prp_numpieE.setValorInt(dtCon1.getInt("stp_unact", true));
        jt.setValor(dtCon1.getString("stp_unact"), 9);
      }
      if (buscaC.isSelected())
      {
          String numPal=buscaPalet();
          lci_numpalE.setText(numPal);
          jt.setValor(numPal,JT_NUMPAL);
      }
     
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
  /**
   * Devuelve el palet donde se leyo por ultima vez un indiviuo
   * @return palet o cadena vacia ("") si no se encuentra.
   * @throws SQLException
   */
  String buscaPalet() throws SQLException
  {
      String cw=" where prp_ano="+prp_anoE.getValorInt()+
                  //" and prp_empcod="+prp_empcodE.getValorInt()+
                  " and prp_seri='"+prp_serieE.getText()+"'"+
                  " and prp_part="+prp_partE.getValorInt()+
                  " and prp_indi="+prp_indiE.getValorInt()+
                  " and pro_codi ="+pro_codiE.getValorInt();
      s="select max(cci_codi) as cci_codi from v_coninvent "+cw;
      dtStat.select(s);
      if (dtStat.getInt("cci_codi",true)==0)
          return "";
      s="select lci_numpal from v_coninvent "+cw+
                  " and cci_codi="+dtStat.getInt("cci_codi");
      if (!dtStat.select(s))
          return "";
      return dtStat.getString("lci_numpal");
  }
  boolean buscaPeso()
  {
    try
    {
      s = "SELECT * FROM V_STKPART WHERE " +
          " eje_nume = " + prp_anoE.getValorInt() +
          " AND pro_serie ='" + prp_serieE.getText() + "'" +
          " AND pro_nupar= " + prp_partE.getValorInt() +
          " and pro_numind= " + prp_indiE.getValorInt() +
          " and pro_codi= " + pro_codiE.getValorInt() +
          " and alm_codi = " + alm_codiE.getValorInt();
      if (!dtCon1.select(s))
      {
        // Compruebo si el producto tiene control stock por individuo
        s = "SELECT pro_coinst,pro_stock as stp_kilact,pro_stkuni as stp_unact "+
                " FROM V_ARTICULO WHERE pro_codi = " +pro_codiE.getValorInt();
        if (!dtCon1.select(s))
        {
          mensajeErr("Articulo NO encontrado");
          return false;
        }
        if (dtCon1.getInt("pro_coinst") == 0)
        { // SIN CONTROL individual devolvemos el STOCK
          return true;
        }
        mensajeErr("NO encontrado Partida para estos valores");
        return false;
      }
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
    if (cci_fecconE.isNull())
    {
      mensajeErr("Introduzca la fecha de Control");
      cci_fecconE.requestFocus();
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
  
    try
    {
      jt.setDefaultValor(4, ""+EU.em_cod);
      jt.setValor(EU.em_cod,0,4);
      if (nav.pulsado == navegador.ADDNEW)
      {
        s = "SELECT * FROM coninvcab WHERE cci_feccon = TO_DATE('" + cci_fecconE.getText() + "','dd-MM-yyyy') " +
            " AND cam_codi = '" + cam_codiE.getText() + "'" +
            " and alm_codi = " + alm_codiE.getValorInt();

        if (dtBloq.select(s))
        {
          mensajeErr("Registro ya existe .. EDITANDOLO");
          mensaje("Editando el registro");
          verDatos(dtBloq); // Resulta que ya existe.
          nav.pulsado = navegador.EDIT;
        }
      }

    }
    catch (Exception k)
    {
      Error("Error al realizar select", k);
      return;
    }

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
      cci_codiE.setText(dt.getString("cci_codi"));
      
      jt.setEnabled(false);
      jt.removeAllDatos();
      s = "select  * from coninvcab where emp_codi = " + EU.em_cod+" AND  cci_codi = " + dt.getInt("cci_codi");
      if (!dtCon1.select(s))
      {
        usu_nombE.resetTexto();
        cci_fecconE.resetTexto();
        cam_codiE.resetTexto();
        alm_codiE.resetTexto();

        msgBox("Datos de cabecera no encontrados ");
        return;
      }
      usu_nombE.setText(dtCon1.getString("usu_nomb"));
      cci_fecconE.setText(dtCon1.getFecha("cci_feccon", "dd-MM-yyyy"));
      cam_codiE.setText(dtCon1.getString("cam_codi"));
      alm_codiE.setText(dtCon1.getString("alm_codi"));

      jt.tableView.setVisible(false);
      s = "select  * from coninvlin where  cci_codi = " + dt.getInt("cci_codi") +
          " and lci_nume <> 0 " +
          " and lci_regaut = 0" + // Solo registros no automaticos
          condLineas+
          " ORDER BY lci_nume ";
      if (!dtCon1.select(s))
        return;
      int n=0;
            
    
      do
      {
       
        ArrayList v = new ArrayList();
        v.add(dtCon1.getString("lci_nume")); // 0
        v.add(dtCon1.getString("pro_codi")); // 1
        v.add(dtCon1.getString("pro_nomb")); // 2 
        v.add(dtCon1.getString("prp_ano")); // 3
        v.add(dtCon1.getString("prp_empcod")); // 4
        v.add(dtCon1.getString("prp_seri")); // 5  
        v.add(dtCon1.getString("prp_part")); // 6 
        v.add(dtCon1.getString("prp_indi")); // 7
        v.add(dtCon1.getString("lci_peso")); // 8
        v.add(dtCon1.getString("lci_numind")); // 9
        v.add(dtCon1.getInt("lci_numcaj",true));
        v.add(dtCon1.getString("lci_numpal"));
        jt.addLinea(v);
        n++;
      }
      while (dtCon1.next());
      jt.tableView.setVisible(true);
      
      jt.requestFocusInicio();
 
      calcTotales(true);
    }
    catch (SQLException k)
    {
      Error("Error al visualizar datos", k);
    }
  }
  public void setProducto(int proCodi)
  {
      pro_codiE.setValorInt(proCodi);
  }
  
  public void setEjercicio(int ejerc)
  {
      prp_anoE.setValorInt(ejerc);
  }
  public void setSerie(String serie)
  {
      prp_serieE.setText(serie);
  }
  public void setLote(int lote)
  {
      prp_partE.setValorInt(lote);
  }
  public void setIndividuo(int indiv)
  {
      prp_indiE.setValorInt(indiv);
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

    v.add(cci_codiE.getStrQuery());
    v.add(cci_fecconE.getStrQuery());
    v.add(cam_codiE.getStrQuery());
    v.add(usu_nombE.getStrQuery());
    v.add(alm_codiE.getStrQuery());
    
    v.add(pro_codiE.getStrQuery());
    v.add(prp_anoE.getStrQuery());
    v.add(prp_serieE.getStrQuery());
    v.add(prp_partE.getStrQuery());
    v.add(prp_indiE.getStrQuery());
    v.add(lci_numcajE.getStrQuery());
    v.add(lci_numpalE.getStrQuery());
       
    s = creaWhere("select distinct(cci_codi) as cci_codi from v_coninvent "+
         condWhere +" and lci_peso>0 ", v,false);
    s += " ORDER BY cci_codi";
//    debug("Query: "+s);
    
    
    try
    {
      if (!dtCons.select(s))     
      {
        mensaje("");
        msgBox("No encontrados Inventarios de Control para estos criterios");
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
      v1.add(lci_numpalE.getStrQuery());
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
          s="SELECT count(*) as cuantos FROM v_coninvent where cci_codi = "+cci_codiE.getValorInt()+
             " and lci_nume<"+   jt.getValorInt(jt.getSelectedRowDisab(),0)+
             " and lci_regaut = 0" ; // Solo registros no automaticos
             
          dtStat.select(s);
          linEdit=dtStat.getInt("cuantos",true);
          condLineas="";
          dtCons.select("SELECT * FROM coninvcab where cci_codi = "+cci_codiE.getValorInt());
//          rgSelect();
          verDatos(dtCons);
      }
      s = "SELECT * FROM coninvcab WHERE  cci_codi = " + cci_codiE.getValorInt();
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
    try
    {
      dtCab.edit();
      dtCab.setDato("cci_feccon", cci_fecconE.getText(), "dd-MM-yyyy");
      dtCab.setDato("cam_codi", cam_codiE.getText());
      dtCab.setDato("alm_codi", alm_codiE.getValorInt());
      dtCab.update();
      ctUp.commit();
    }
    catch (Exception k)
    {
      Error("Error al Modificar cabecera de Inventario", k);
    }
    mensajeErr("Control de Inventario ... MODIFICADO");
    activaTodo();
    mensaje("");
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
    cci_fecconE.requestFocus();
    mensaje("Introduzca Criterios de Busqueda");
  }

 @Override
  public void PADAddNew()
  {
    activar(true, navegador.ADDNEW);
    mensaje("Insertar Nuevo Registro");
    kgAnt=0;
    Pcabe.resetTexto();
    usu_nombE.setText(EU.usuario);
    cci_fecconE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
    jt.removeAllDatos();
    kiltotE.setValorDec(0);
    kiltotE1.setValorDec(0);
    numpesE.setValorDec(0);
    numpesE1.setValorDec(0);
    cci_codiE.setText("");
    swSal = false;
    cci_fecconE.requestFocus();
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
      if (dtCons.getNOREG())
        rgSelect();
      activaTodo();
      mensajeErr("Control de Inventario ... Insertado");
    }
    catch (Exception k)
    {
      Error("Error al Insertar datos", k);
      return;
    }
    nav.pulsado = navegador.NINGUNO;
  }

/*  private void insLinea() throws ParseException, SQLException
  {
    int rw = jt.getRowCount();
//    int nl=0;
    dtAdd.addNew("coninvlin");
    s = "SELECT MAX(lci_nume) as lci_nume from coninvlin where emp_codi = " + EU.em_cod +
        " and cci_codi = " + cci_codiE.getText();
    dtStat.select(s);
    int nl = dtStat.getInt("lci_nume", true);
//    nl++;
    for (int n = 0; n < rw; n++)
    {
      if (n % 20 == 0)
        mensajeErr("Guardadas " + n + " Lineas", false);
      if (jt.getValString(n, 1).trim().equals("0") || jt.getValString(n, 1).trim().equals(""))
        continue;
      nl++;
      dtAdd.addNew();
      dtAdd.setDato("cci_codi", cci_codiE.getText());
      dtAdd.setDato("lci_nume", nl);
      dtAdd.setDato("prp_ano", jt.getValInt(n, 3));
      dtAdd.setDato("emp_codi", jt.getValInt(n, 4));
      dtAdd.setDato("prp_seri", jt.getValString(n, 5));
      dtAdd.setDato("prp_part", jt.getValInt(n, 6));
      dtAdd.setDato("pro_codi", jt.getValInt(n, 1));
      dtAdd.setDato("pro_nomb", jt.getValString(n, 2));
      dtAdd.setDato("prp_indi", jt.getValInt(n, 7));
      dtAdd.setDato("lci_peso", jt.getValorDec(n, 8));
      dtAdd.setDato("lci_numind", jt.getValorDec(n, 9));
      dtAdd.update(stUp);
    }
    mensajeErr("", false);

  }
*/
  int cambiaLineaGrid(int row)
  {
    try
    {
        if (swSal)
            return -1;
//      if (DEBUG)
//        return -1;
      if (pro_codiE.getText().trim().equals("0") || pro_codiE.getText().trim().equals(""))
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
        if (prp_pesoE.getValorDec() == 0)
        {
          msgBox("Introduzca el peso");
          return 6;
        }
        if (prp_indiE.getValorInt() == 0)
        {

          msgBox("Introduzca Numero Individuo");
          return -1;
        }
      }
      swKilos0=false;
      if (prp_numpieE.getValorInt() == 0)
      {
        msgBox("Introduzca Numero de Individuos");
        return 7;
      }
      // Busco lineas Repetidas
      if (row < 0)
        return -1;
      for (int n = 0; n < jt.getRowCount(); n++)
      {
        if (n == row)
          continue;
        if (jt.getValString(n, 1).trim().equals("0"))
          continue;
        if (jt.getValorInt(n, 7) == 0)
          continue; // Sin individuo. -- Permitimos todos los que quiera.
        if (jt.getValorInt(n, 1) == pro_codiE.getValorInt() &&
            jt.getValorInt(n, 3) == prp_anoE.getValorInt() &&
//            jt.getValInt(n, 4) == prp_empcodE.getValorInt() &&
            jt.getValString(n, 5).equals(prp_serieE.getText()) &&
            jt.getValorInt(n, 6) == prp_partE.getValorInt() &&
            jt.getValorInt(n, 7) == prp_indiE.getValorInt() &&
            Formatear.redondea(jt.getValorDec(n, 8), 2) == Formatear.redondea(prp_pesoE.getValorDec(), 2))
        {
          if (swRepLineas)
          {
            msgBox("Linea duplicada en posicion: " + (n + 1) + " La linea se aceptara");
          }
          else
          {
              msgBox("Linea duplicada en posicion: " + (n + 1) );
              return 1;
          }
        }
      }
      jt.setValor(""+ insLineaInv(jt.getValorInt(row,0)),row,0);
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

  int insLineaInv(int nl) throws SQLException, ParseException
  {
    if (cci_codiE.getValorInt()==0)
      insCabInv(cci_fecconE.getDate(),alm_codiE.getValorInt());
    if (nl == 0)
    {
      s = "SELECT MAX(lci_nume) as lci_nume from coninvlin where  cci_codi = " + cci_codiE.getValorInt();
      dtStat.select(s);
      nl = dtStat.getInt("lci_nume", true) + 1;
//    nl++;

      dtAdd.addNew("coninvlin");
      dtAdd.setDato("emp_codi", EU.em_cod);
      dtAdd.setDato("cci_codi", cci_codiE.getValorInt());
      dtAdd.setDato("lci_nume", nl);
    }
    else
    {
      s="SELECT * FROM coninvlin WHERE cci_codi = "+cci_codiE.getValorInt()+
         " and lci_nume = "+nl;
      if (!dtAdd.select(s,true))
      {
        msgBox("Linea de INVENTARIO no encontrada para modificar");
        aviso("Linea Inventario NO encontrada en modificacion: "+s);
        return nl;
      }
      dtAdd.edit();
    }
    dtAdd.setDato("prp_ano", prp_anoE.getValorInt());
    dtAdd.setDato("prp_empcod", EU.em_cod);
    dtAdd.setDato("prp_seri", prp_serieE.getText());
    dtAdd.setDato("prp_part", prp_partE.getValorInt());
    dtAdd.setDato("pro_codi", pro_codiE.getValorInt());
    dtAdd.setDato("pro_nomb", pro_nombE.getText());
    dtAdd.setDato("prp_indi", prp_indiE.getValorInt());
    dtAdd.setDato("lci_peso", prp_pesoE.getValorDec());
    dtAdd.setDato("lci_numind", prp_numpieE.getValorDec());
    dtAdd.setDato("lci_numpal",lci_numpalE.getText());
    dtAdd.setDato("lci_numcaj",lci_numcajE.getText());
    dtAdd.setDato("alm_codlin",alm_codiE.getValorInt());
    dtAdd.update(stUp);
    ctUp.commit();
    return nl;
  }
/**
 * inserta cabecera inventario
 * @param cciFeccon
 * @param almCodi
 * @return
 * @throws SQLException 
 */
  int insCabInv(Date cciFeccon,int almCodi) throws SQLException
  {
    s = "SELECT MAX(cci_codi) as cci_codi from coninvcab ";
    dtAdd.select(s);

    cci_codiE.setText("" + (dtAdd.getInt("cci_codi", true) + 1));

    dtAdd.addNew("coninvcab");
    dtAdd.setDato("emp_codi", EU.em_cod);
    dtAdd.setDato("cci_codi", cci_codiE.getValorInt());
    dtAdd.setDato("usu_nomb", EU.usuario);
    dtAdd.setDato("cci_feccon", cciFeccon);
    dtAdd.setDato("cam_codi", cam_codiE.getText());
    dtAdd.setDato("alm_codi", almCodi);
    dtAdd.update(stUp);
    return cci_codiE.getValorInt();
  }

 @Override
  public void canc_addnew()
  {
    try
    {

      if (cci_codiE.getValorInt()!=0)
      {
        if (mensajes.mensajeYesNo("Continuar DADO de ALTA", this) != mensajes.NO)
          return;

        int nReg;
        s = "delete from coninvcab where cci_codi = " + cci_codiE.getValorInt() ;
        if ( (nReg = stUp.executeUpdate(s)) != 1)
          throw new Exception("Error al borrar control inventario .. Num. Registros Borrados: " +
                              nReg);
        s = "delete from coninvlin where cci_codi = " + cci_codiE.getValorInt() ;
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

    public void PADDelete()
    {
      Baceptar.setEnabled(true);
      Bcancelar.setEnabled(true);
      mensaje("Borrar  .. Control Inventario");
      Bcancelar.requestFocus();
    }
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
        s = "delete from coninvcab where cci_codi = " + dtCons.getInt("cci_codi");
        if ( (nReg = stUp.executeUpdate(s)) != 1)
          throw new Exception("Error al borrar control inventario .. Num. Registros Borrados: " + nReg);
        s = "delete from coninvlin where cci_codi = " + dtCons.getInt("cci_codi");
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
            cci_codiE.setEnabled(b);
        case navegador.ADDNEW:
          Pcabe.setEnabled(b);
          cci_fecconE.setEnabled(b);
          buscaC.setEnabled(b);
          Birlin.setEnabled(b);
          cam_codiE.setEnabled(b);
          Bcancelar.setEnabled(b);
          if (swAdmin)
          {
            Bcopia.setEnabled(b);
            cci_fecoriE.setEnabled(b);
          }
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

    public void PADPrimero()
    {
      verDatos(dtCons);
    }

    public void PADAnterior()
    {
      verDatos(dtCons);
    }

    public void PADSiguiente()
    {
      verDatos(dtCons);
    }

    public void PADUltimo()
    {
      verDatos(dtCons);
    }
    /**
     * 
     * @param proCodi
     * @param ano
     * @param serie
     * @param lote
     * @param numind
     * @return
     * @throws SQLException 
     */
    boolean checkSalida(int proCodi,int ano,String serie, int lote, int numind) throws SQLException, ParseException
    {
        s="SELECT c.pro_codi "+
          " from v_albventa_detalle as c  " +
          " where  c.avc_serie!='X'"+
           " and pro_codi = "+proCodi+
           " and avp_ejelot = "+ano+
           " and avp_serlot = '"+serie+"'"+
           " and avp_numpar = "+lote+
           " and avp_numind  = "+numind+ 
           " AND c.avl_fecalt <= {d '" + cci_fecconE.getFechaDB() + "'} "+
           " AND c.avl_fecalt > {d '" + cci_fecoriE.getFechaDB() + "'} "+
           " union "+
           "SELECT  c.pro_codi "+
           " from v_despori as c  " +
           " where c.deo_kilos <> 0 " +           
           " and pro_codi = "+proCodi+
           " and deo_ejelot = "+ano+
           " and deo_serlot = '"+serie+"'"+
           " and pro_lote = "+lote+
           " and pro_numind = "+numind+ 
           " AND c.deo_tiempo <= {d '" + cci_fecconE.getFechaDB() + "'} "+
           " AND c.deo_tiempo > {d '" + cci_fecoriE.getFechaDB() + "'} ";
          return dtStat.select(s);
    }
    
    PreparedStatement getPreparedInventario(DatosTabla dt,String fecori,int almCodi) throws SQLException
    {
        String s="select * from v_coninvent where cci_feccon = '"+fecori+"'"+
                " and alm_codi = "+almCodi+
                " and lci_peso>0"+
                " and pro_codi = ?"+
                " and prp_ano = ?"+
                " and prp_seri = ?"+
                " and prp_part = ? "+
                " and prp_indi = ?";
        return dtStat.getPreparedStatement(s);
    }
    
    boolean importaInvProduc() throws SQLException, ParseException 
    {
         if (nav.pulsado!=navegador.EDIT)
         {
             msgBox("Importacion solo se puede hacer en Modo Edicion");
             return false;
         }
        if (cci_fecoriE.isNull())
        {
            msgBox("Introduzca fecha origen");
            cci_fecoriE.requestFocus();
            return false;
        }
        if (nav.pulsado==navegador.EDIT)
        {
            PreparedStatement ps=getPreparedInventario(dtStat,cci_fecconE.getFechaDB(),alm_codiE.getValorInt());           
           
            ResultSet rs;
            s="select * from v_invproduc where cip_fecinv = '"+cci_fecoriE.getFechaDB()+"'"+
                " and cam_codi = '"+cam_codiE.getText()+"'"+
                " and alm_codi = "+alm_codiE.getValorInt()+
                " order by lip_numlin";
            if (!dtCon1.select(s))
            {
                msgBox("No encontrado ningun inventario produccion con esa fecha para esta camara");
                return false; 
            }
            int lciNume;           
            jt.setEnabled(false);
            prp_numpieE.setValorInt(1);
            lci_numcajE.setValorDec(0);
            lci_numpalE.setText("IP");
           
            do
            {                
                ArrayList v=new ArrayList();
                ps.setInt(1, dtCon1.getInt("pro_codi"));
                ps.setInt(2, dtCon1.getInt("prp_ano"));
                ps.setString(3, dtCon1.getString("prp_seri"));
                ps.setInt(4, dtCon1.getInt("prp_part"));
                ps.setInt(5, dtCon1.getInt("prp_indi"));
                rs=ps.executeQuery();
                if (rs.next())
                    continue; // ya existe ese individuo.
                pro_codiE.setText(dtCon1.getString("pro_codi"));
                pro_nombE.setText(dtCon1.getString("pro_nomb"));
                prp_anoE.setValorInt(dtCon1.getInt("prp_ano"));
                prp_serieE.setText(dtCon1.getString("prp_seri"));
                prp_partE.setValorDec(dtCon1.getInt("prp_part"));
                prp_indiE.setValorDec(dtCon1.getInt("prp_indi"));
                prp_pesoE.setValorDec(dtCon1.getDouble("prp_peso"));
                
                
                lciNume=insLineaInv(0);
                v.add(lciNume); // 0
                v.add(dtCon1.getString("pro_codi")); // 1
                v.add(dtCon1.getString("pro_nomb")); // 2 
                v.add(dtCon1.getString("prp_ano")); // 3
                v.add(EU.em_cod); // 4
                v.add(dtCon1.getString("prp_seri")); // 5  
                v.add(dtCon1.getString("prp_part")); // 6 
                v.add(dtCon1.getString("prp_indi")); // 7
                v.add(dtCon1.getString("prp_peso")); // 8
                v.add(1); // 9
                v.add(0);
                v.add("IP");
                jt.addLinea(v);
            } while (dtCon1.next());
            jt.setEnabled(true);

        }
        msgBox("Insertado inventario produccion");
        return true;

    }
    /**
     * Fusiona el inventario de una fecha con el de otra.
     * Mete el inventario de la fecha de Origen a la fecha de control.
     * Coge el inventario inicial y le suma las entradas, y le resta las salidas, 
     * tanto de despieces como de ventas,generando un nueva entrada en la fecha inventario final.
     * Con el resultado de los stocks disponibles, se comprueba con el inventario final, 
     * despreciandose los registros iguales.
     * Se avisara en el caso de:
     * - Registros iguales (en los dos inventarios)
     * - Entradas en el periodo existente entre los dos inventarios y q no esten el inventario final.
     * - Salidas del inventario inicial hasta el inventario final.
     */
    
    void fusionar() throws SQLException, ParseException
    {
     
      int cciCodi=moverInventario();
      if (cciCodi==0)
          return;

          
      String msg="";
      String condProd = " and a.pro_artcon " + (cam_codiE.getText().equals("X")? "<> 0" : " = 0");
      // Busco compras en el periodo que no esten en inventario final.
       s= "SELECT c.pro_codi,a.pro_nomb, acc_ano as ano, acc_serie as serie, acc_nume as lote, acp_numind as numind"+
          " from v_compras as c, v_articulo as a   " +
          " where c.pro_codi = a.pro_codi " +
          " and c.acp_canti <> 0 " +        
           condProd +
          " AND c.acc_fecrec <= {d '" + cci_fecconE.getFechaDB() + "'} "+
          " AND c.acc_fecrec > {d '" + cci_fecoriE.getFechaDB() + "'} "+
          " and not exists (select cci_codi from v_coninvent as i "+
          " where c.acc_ano = i.prp_ano "+
          " and c.pro_codi = i.pro_codi " +
          " and c.acc_serie = i.prp_seri "+
          " and c.acc_nume = i.prp_part "+
          " and c.acp_numind= i.prp_indi "+
          " and i.cci_feccon = {d '"+cci_fecconE.getText()+"'} "+
          " and i.cci_codi != "+cciCodi+")";
       if ( dtCon1.select(s))
       {
          do
          {
              if (checkSalida(dtCon1.getInt("pro_codi"),dtCon1.getInt("ano"),dtCon1.getString("serie"),
                  dtCon1.getInt("lote"),
                  dtCon1.getInt("numind")))
                  continue;
              msg+=" Compra en periodo y no estaba en inventario final: "+dtCon1.getInt("pro_codi")+
                  " ("+dtCon1.getString("pro_nomb")+")"+
                  " Lote: "+dtCon1.getInt("ano")+dtCon1.getString("serie")+dtCon1.getInt("lote")+
                  "- " +dtCon1.getInt("numind")+"\n";
          } while (dtCon1.next());
      }  
       // Busco Entradas en despieces en el periodo que no esten en inventario final.
       s= "SELECT c.pro_codi,A.pro_nomb,def_ejelot as ano, def_serlot as serie, pro_lote as lote, pro_numind as numind"+
          " from v_despsal as c, v_articulo as a   " +
          " where c.pro_codi = a.pro_codi " +
          " and c.def_kilos <> 0 " +  
          " and a.pro_tiplot = 'V' "+
           condProd +
          " AND c.def_tiempo <= {d '" + cci_fecconE.getFechaDB() + "'} "+
          " AND c.def_tiempo > {d '" + cci_fecoriE.getFechaDB() + "'} "+
          " and not exists (select cci_codi from v_coninvent as i "+
          " where c.def_ejelot = i.prp_ano "+
          " and c.pro_codi = i.pro_codi " +
          " and c.def_serlot = i.prp_seri "+
          " and c.pro_lote = i.prp_part "+
          " and c.pro_numind= i.prp_indi "+
          " and i.cci_feccon = {d '"+cci_fecconE.getText()+"'} "+
          " and i.cci_codi != "+cciCodi+")";
       if ( dtCon1.select(s))
       {
          do
          {
             if (checkSalida(dtCon1.getInt("pro_codi"),dtCon1.getInt("ano"),dtCon1.getString("serie"),
                  dtCon1.getInt("lote"),
                  dtCon1.getInt("numind")))
                  continue;
              msg+="Entrada de Despiece  en periodo y no estaba en inventario final: "+dtCon1.getInt("pro_codi")+
                   " ("+dtCon1.getString("pro_nomb")+")"+
                  " Lote: "+dtCon1.getInt("ano")+dtCon1.getString("serie")+dtCon1.getInt("lote")+
                  "- " +dtCon1.getInt("numind")+"\n";
          } while (dtCon1.next());
      }  
       // Busco ventas de productos entre los dos inventarios
      s= "SELECT c.pro_codi,A.pro_nomb,avp_ejelot as ano, avp_serlot as serie, avp_numpar as lote, "
          + " avp_numind as numind"+
          " from v_albventa_detalle as c, v_articulo as a   " +
          " where c.pro_codi = a.pro_codi " +
          " and c.avp_canti <> 0 " +  
          " and a.pro_tiplot = 'V' "+
          " and c.avc_serie!='X'"+
           condProd +
          " AND c.avl_fecalt <= {d '" + cci_fecconE.getFechaDB() + "'} "+
          " AND c.avl_fecalt > {d '" + cci_fecoriE.getFechaDB() + "'} ";
      if ( dtCon1.select(s))
       {
          do
          {
              msg+=" Venta en periodo de articulos de  inventario Inicial: "+dtCon1.getInt("pro_codi")+
                   " ("+dtCon1.getString("pro_nomb")+")"+
                  " Lote: "+dtCon1.getInt("ano")+dtCon1.getString("serie")+dtCon1.getInt("lote")+
                  "- " +dtCon1.getInt("numind")+"\n";
          } while (dtCon1.next());
      }  
      s= "SELECT c.pro_codi,A.pro_nomb,deo_ejelot as ano, deo_serlot as serie, pro_lote as lote, pro_numind as numind"+
          " from v_despori as c, v_articulo as a   " +
          " where c.pro_codi = a.pro_codi " +
          " and c.deo_kilos <> 0 " +  
          " and a.pro_tiplot = 'V' "+
           condProd +
          " AND c.deo_tiempo <= {d '" + cci_fecconE.getFechaDB() + "'} "+
          " AND c.deo_tiempo > {d '" + cci_fecoriE.getFechaDB() + "'} ";
      if ( dtCon1.select(s))
       {
          do
          {
              msg+=" Desp. en periodo de articulos de  inventario Inicial: "+dtCon1.getInt("pro_codi")+
                   " ("+dtCon1.getString("pro_nomb")+")"+
                  " Lote: "+dtCon1.getInt("ano")+dtCon1.getString("serie")+dtCon1.getInt("lote")+
                  "- " +dtCon1.getInt("numind")+"\n";
          } while (dtCon1.next());
      }  
/*       

        " select 'D' as sel,'-' as tipmov,"+
        (swFecMvto?"deo_tiempo": "deo_fecha")+" as fecmov," +
        "  deo_serlot as serie,pro_lote as  lote," +
        " deo_kilos as canti,pro_numind as numind," +
        " deo_ejelot as ejeNume," +
        " 1 as empcodi,l.pro_codi as pro_codi, " +
        " deo_prcost as precio,1 as canind, " +
        " deo_almori as almori,deo_almori as almfin, '' as avc_serie" +
        " from  v_despori as l,v_articulo as a  " +
        " where deo_kilos <> 0 " +
        " and a.pro_tiplot = 'V' "+
         (pro_codi == 0 ? "" : " and l.pro_codi = " + pro_codi) +
        (almCodi==0?"":" and deo_almori = " + almCodi) +
        " and a.pro_codi = l.pro_codi " +
        condProd +
        (fecsup==null?"":" AND "+  (swFecMvto?"deo_tiempo": "deo_fecha")+
        " <= TO_DATE('" + fecsup + "','dd-MM-yyyy') ")+
        " AND "+ (swFecMvto?"deo_tiempo": "deo_fecha")+
        " > TO_DATE('" + fecinv + "','dd-MM-yyyy') ";     
     */
        
      // Busco duplicados en los dos inventarios.
      s= " select a.pro_nomb,R.* FROM v_coninvent as r,v_articulo as a " +
        " where lci_peso > 0 " +
        " and lci_regaut = 0 "+
        " and a.pro_codi = r.pro_codi " +
        condProd +
        " AND r.cci_feccon = TO_DATE('" + cci_fecconE.getText() + "','dd-MM-yyyy') "+
        " and r.cci_codi != "+cciCodi+
        " and exists (select * FROM v_coninvent as r1  " +
        " where r1.cci_codi= "+cciCodi+
        " and r1.prp_ano = r.prp_ano "+
        " and r1.pro_codi = r.pro_codi " +
        " and r1.prp_seri = r.prp_seri "+
        " and r1.prp_part = r.prp_part "+
         " and r1.prp_indi= r.prp_indi) ";
      if ( dtCon1.select(s))
      {
          do
          {
              msg+=" Individuo Duplicado: "+dtCon1.getInt("pro_codi")+
                   " ("+dtCon1.getString("pro_nomb")+")"+
                  " Lote: "+dtCon1.getInt("prp_ano")+dtCon1.getString("prp_seri")+dtCon1.getInt("prp_part")+
                  "- " +dtCon1.getInt("prp_indi")+"\n";
             s="delete from coninvlin where cci_codi="+dtCon1.getInt("cci_codi")+" and lci_nume="+dtCon1.getInt("lci_nume");
             int n=dtBloq.executeUpdate(s);   
             if (n!=1)
                 msgBox("no borrado indiv. "+dtCon1.getInt("pro_codi")+
                   " ("+dtCon1.getString("pro_nomb")+")"+
                  " Lote: "+dtCon1.getInt("prp_ano")+dtCon1.getString("prp_seri")+dtCon1.getInt("prp_part")+
                  "- " +dtCon1.getInt("prp_indi"));
          } while (dtCon1.next());
      }
      dtBloq.commit();
      mensajes.mensajeExplica("Incidencias", msg);
        //System.out.println("msg: "+msg);
    

      
    }
    public boolean inTransation()
    {
        return (nav.getPulsado()==navegador.ADDNEW || nav.getPulsado()==navegador.EDIT || nav.getPulsado()==navegador.DELETE);
    }
    
    int copiarInventario() throws SQLException, ParseException
    {
       if (!checkCabecera())
          return 0;
       if (cci_fecoriE.isNull())
           return 0;
        s="select * from v_coninvent where alm_codi ="+alm_codiE.getValorInt()+
           " and cam_codi = '"+cam_codiE.getText()+"'"+
           " and cci_feccon = '"+cci_fecoriE.getFechaDB()+"'";
       if (!dtCon1.select(s))
       {
           msgBox("No encontrado inventario en esa fecha");
           return 0;
       }
       PreparedStatement ps=getPreparedInventario(dtStat, cci_fecconE.getFechaDB(),alm_codiE.getValorInt());
       ResultSet rs;
     
       int cciCodi=cci_codiE.getValorInt();
       int nl=1;
       if (cciCodi==0)
              cciCodi=insCabInv(cci_fecconE.getDate(),alm_codiE.getValorInt());
       else
       {
           s="select max(lci_nume) as lciNume from coninvlin where cci_codi= "+cci_codiE.getValorInt()+
               " and emp_codi="+EU.em_cod;
           dtStat.select(s);
           nl=dtStat.getInt("lciNume",true)+1;
       }
       
       do
       {
            ps.setInt(1, dtCon1.getInt("pro_codi"));
            ps.setInt(2, dtCon1.getInt("prp_ano"));
            ps.setString(3, dtCon1.getString("prp_seri"));
            ps.setInt(4, dtCon1.getInt("prp_part"));
            ps.setInt(5, dtCon1.getInt("prp_indi"));
            rs=ps.executeQuery();
            if (rs.next())
               continue; // ya existe ese individuo.
            dtAdd.addNew("coninvlin");
            dtAdd.setDato("emp_codi", EU.em_cod);
            dtAdd.setDato("cci_codi", cciCodi);
            dtAdd.setDato("lci_nume", nl++);
            dtAdd.setDato("prp_ano", dtCon1.getInt("prp_ano"));
            dtAdd.setDato("prp_empcod",EU.em_cod);
            dtAdd.setDato("prp_seri", dtCon1.getString("prp_seri"));
            dtAdd.setDato("prp_part", dtCon1.getInt("prp_part"));
            dtAdd.setDato("pro_codi", dtCon1.getInt("pro_codi"));
            dtAdd.setDato("pro_nomb", dtCon1.getString("pro_nomb"));
            dtAdd.setDato("prp_indi", dtCon1.getInt("prp_indi"));
            dtAdd.setDato("lci_peso", dtCon1.getDouble("lci_peso"));
            dtAdd.setDato("lci_numind", dtCon1.getInt("lci_numind"));
            dtAdd.setDato("lci_numpal",dtCon1.getString("lci_numind"));
            dtAdd.setDato("alm_codlin",dtCon1.getString("alm_codlin"));
            dtAdd.update(stUp);  
            
       } while (dtCon1.next());
       dtAdd.commit();
       msgBox(nl+" Lineas de Inventario: "+cci_fecoriE.getText()+" copiado a fecha: "+cci_fecconE.getText());
        
       rgSelect();
       verDatos(dtCons);
       return cciCodi;
    }
    /** 
     * Generar inventario a partir de otro anterior.
     * @return int numero de registro en inventario generado.
     *  si no se genera el invetnario por cualquier error, se devuelve 0.
     */
    int moverInventario() throws SQLException, ParseException
    {
       if (!checkCabecera())
        return 0;
       if (cci_fecoriE.isNull())
           return 0;
       int cciCodi=0;
       
//         if (Formatear.comparaFechas(cci_fecoriE.getDate(), cci_fecconE.getDate())>=0)
//         {
//            mensajeErr("Fecha Origen debe ser Inferior a la de control");
//            cci_fecoriE.requestFocus();
//            return 0;
//         }
         ActualStkPart stkPart=new ActualStkPart(dtAdd,EU.em_cod);
         HashMap ht = stkPart.getStockControl(dtCon1,cam_codiE.getText().equals("X")?1:0,alm_codiE.getValorInt(),
           cci_fecoriE.getDate(),cci_fecconE.getDate());
         if (ht == null || ht.isEmpty())
             return 0;
       
         Iterator it;
         it = ht.keySet().iterator();
         String key;
         String valor;
   //    int ejeNume,lote,numind;
   //    String serie;
         int n = 0;
         int nl=0;
         
         DatIndiv datInd;
         HashMap<Integer,Dimension> lAlm=new HashMap();
         
         while (it.hasNext())
         {
            key = it.next().toString();
            valor = (ht.get(key)).toString();

            datInd =new DatIndiv(key,valor);
            if (datInd.canti<=0 || datInd.getNumuni()<=0)
                continue;
            if (! lAlm.containsKey(datInd.getAlmCodi()))
            {
              cciCodi=insCabInv(cci_fecconE.getDate(),datInd.getAlmCodi());
              nl=1;
            }   
            dtAdd.addNew("coninvlin");
            dtAdd.setDato("emp_codi", EU.em_cod);
            dtAdd.setDato("cci_codi", cciCodi);
            dtAdd.setDato("lci_nume", nl);
            dtAdd.setDato("prp_ano", datInd.getEjercLot());
            dtAdd.setDato("prp_empcod",EU.em_cod);
            dtAdd.setDato("prp_seri", datInd.getSerie());
            dtAdd.setDato("prp_part", datInd.getLote());
            dtAdd.setDato("pro_codi", datInd.getProducto());
            dtAdd.setDato("pro_nomb", "");
            dtAdd.setDato("prp_indi", datInd.getNumind());
            dtAdd.setDato("lci_peso", datInd.getCanti());
            dtAdd.setDato("lci_numind", datInd.getNumuni());
            dtAdd.setDato("lci_numpal",0);
            dtAdd.setDato("alm_codlin",datInd.getAlmCodi());

            dtAdd.update(stUp);  
            lAlm.put(datInd.getAlmCodi(),new Dimension(cciCodi,++nl));
          }
          dtAdd.commit();
       
       return cciCodi;
    }
    
    void Bgrupo_addActionPerformed()
    {
      if (jt.getSelectedRow() > 0)
      {
        pro_codiE.setText(jt.getValString(jt.getSelectedRow() - 1, 1));
        pro_nombE.setText(jt.getValString(jt.getSelectedRow() - 1, 2));
        prp_anoE.setText(jt.getValString(jt.getSelectedRow() - 1, 3));
        prp_serieE.setText(jt.getValString(jt.getSelectedRow() - 1, 5));
        prp_partE.setText(jt.getValString(jt.getSelectedRow() - 1, 6));
        jt.salirGrid();
        javax.swing.SwingUtilities.invokeLater(new Thread()
        {
          public void run()
          {
            jt.requestFocus(jt.getSelectedRow(), 7);

          }
        });

        return;
      }
      /*
         try
         {
           s = "SELECT pro_numind,stp_kilact FROM v_stkpart WHERE emp_codi = " + jt.getValInt(4) +
          " AND eje_nume = " + jt.getValInt(3) +
          " and pro_serie = '" + jt.getValString(5) +"'"+
          " and pro_nupar = " + jt.getValInt(6) +
          " and pro_numind >= " + jt.getValInt(7) +
          " and pro_numind <= " + jt.getValorDec(8)+
//        " and stp_kilact > 0"+
          " and alm_codi = 1";
           if (!dtStat.select(s))
           {
        mensajeErr("No encontrados Registros para estos individuos");
        return;
           }
           int res= mensajes.mensajeYesNo("Insertar registros del Individuo: "+
                                 jt.getValString(7)+ "  A individuo: "+jt.getValString(8));
           if (res != mensajes.YES)
           {
        mensajeErr("Registros NO insertados");
        jt.requestFocus();
        return;
           }
           int nl=jt.getSelectedRow();
           jt.setEnabled(false);
           do
           {
        Vector v=new Vector();
        v.addElement("");
        v.addElement(jt.getValString(nl,1));
        v.addElement(jt.getValString(nl,2));
        v.addElement(jt.getValString(nl,3));
        v.addElement(jt.getValString(nl,4));
        v.addElement(jt.getValString(nl,5));
        v.addElement(jt.getValString(nl,6));
        v.addElement(dtStat.getString("pro_numind"));
        v.addElement(dtStat.getString("stp_kilact"));
        jt.addLinea(v);
           } while (dtStat.next());
           jt.removeLinea(nl);
           jt.setEnabled(true);
           jt.requestFocus(jt.getRowCount()-1,0);
         }
         catch (Exception ex)
         {
           Error("Error al Insertar grupo",ex);
         }
       */
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
        s = "delete from coninvlin where cci_codi = " + cci_codiE.getValorInt() +
            " and lci_nume = " + jt.getValorInt(row, 0);
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

//    void modifCab()
//    {
//      try  {
//        s="SELECT * FROM coninvcab WHERE  cci_codi = "+cci_codiE.getText();
//        if (! dtAdd.select(s,true))
//        {
//          nav.pulsado=nav.NINGUNO;
//          mensajeErr("NO encontrado registro en inventario");
//          return;
//        }
//      } catch (Exception k)
//      {
//        Error("Error al Modificar cabecera",k);
//      }
//      mensaje("Modificando datos de Cabecera ...");
//      mensajeErr("");
//      nav.ponEnabled(false);
//      nav.pulsado=navegador.CHOSE;
//      activar(true);
//      Birlin.setEnabled(false);
//      jt.setEnabled(false);
//    
//      Baceptar.setEnabled(true);
//      Bcancelar.setEnabled(true);
////    }
//    /**
//     * Rutina a machacar si se quiere hacer algo más cuando se haya pulsado
//     * el boton Aceptar
//     */
//    protected void pulsadoAceptar()
//    {
//      if (nav.pulsado != navegador.CHOSE)
//        return;
//      try  {
//        dtAdd.edit();
//        dtAdd.setDato("cci_feccon", cci_fecconE.getText(), "dd-MM-yyyy");
//        dtAdd.setDato("cam_codi", cam_codiE.getText());
//        dtAdd.setDato("alm_codi", alm_codiE.getValorInt());
//        ctUp.commit();
//      } catch (Exception k)
//      {
//        Error("Error al Modificar cabecera de Inventario",k);
//      }
//      activaTodo();
//      mensaje("");
//      mensajeErr("Modificaci�n de cabecera ... Realizada");
//      nav.pulsado=nav.NINGUNO;
//    }
//    /**
//     * Rutina a machacar si se quiere hacer algo m�s cuando se haya pulsado
//     * el boton Cancelar
//     */
//    protected void pulsadoCancelar()
//    {
//      if (nav.pulsado != navegador.CHOSE)
//        return;
//      activaTodo();
//      mensaje("");
//      mensajeErr("Modificaciónn de cabecera ... CANCELADA");
//      nav.pulsado=nav.NINGUNO;
//    }
    void  Bkilos0_actionPerformed()
    {
      swKilos0=true;
      prp_pesoE.setValorDec(0);
      jt.setValor("0",8);
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

       HashMap mp = new HashMap();
       JasperReport jr;
       jr = gnu.chu.print.util.getJasperReport(EU,"coninvent");

       s="select a.alm_nomb,c.cam_nomb,i.* from v_coninvent as i "+
           " left join v_almacen as a on i.alm_codi=a.alm_codi  "+
           " left join v_camaras as c on c.cam_codi=i.cam_codi and c.emp_codi=i.emp_codi "+
            " where cci_codi="+cci_codiE.getValorInt()+
            " order by lci_numpal,pro_codi,prp_part,prp_seri,prp_indi";
       ResultSet rs;

       rs=dtCon1.getStatement().executeQuery(dtCon1.parseaSql(s));

       JasperPrint jp = JasperFillManager.fillReport(jr, mp,
           new JRResultSetDataSource(rs));
       gnu.chu.print.util.printJasper(jp, EU);
     }
     catch (Exception k)
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
                    if (prp_numpieE.getValorInt() == 0)
                    {
                        if (buscaPeso())
                        {
                            prp_numpieE.setValorInt(dtCon1.getInt("stp_unact"));
                            jt.setValor(dtCon1.getString("stp_unact"), 9);
                        }
                        if (buscaC.isSelected())
                        {
                            String numPal=buscaPalet();
                            lci_numpalE.setText(numPal);
                            jt.setValor(numPal,JT_NUMPAL);
                        }
                    }
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
        prp_numpieE = new gnu.chu.controles.CTextField(Types.DECIMAL, "##9");
        lci_numpalE = new gnu.chu.controles.CTextField(Types.CHAR, "X",4);
        Bimpri = new gnu.chu.controles.CButton(Iconos.getImageIcon("print"));
        lci_numeE = new gnu.chu.controles.CTextField(Types.DECIMAL, "#999");
        lci_numcajE = new gnu.chu.controles.CTextField(Types.DECIMAL, "#999");
        Pprinc = new gnu.chu.controles.CPanel();
        Pcabe = new gnu.chu.controles.CPanel();
        cLabel5 = new gnu.chu.controles.CLabel();
        cci_fecconE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel6 = new gnu.chu.controles.CLabel();
        cci_codiE = new gnu.chu.controles.CTextField(Types.DECIMAL,"####9");
        cLabel7 = new gnu.chu.controles.CLabel();
        usu_nombE = new gnu.chu.controles.CTextField(Types.CHAR, "X", 15);
        cLabel8 = new gnu.chu.controles.CLabel();
        cam_codiE = new gnu.chu.controles.CLinkBox();
        cLabel9 = new gnu.chu.controles.CLabel();
        alm_codiE = new gnu.chu.controles.CLinkBox();
        buscaC = new gnu.chu.controles.CCheckBox();
        Birlin = new gnu.chu.controles.CButton();
        cci_fecoriE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel12 = new gnu.chu.controles.CLabel();
        Bcopia = new gnu.chu.controles.CButtonMenu();
        jt = new gnu.chu.controles.CGridEditable(12) {
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
                lci_numpalE.resetCambio();
                lci_numcajE.resetCambio();
                return cambiaLineaGrid(row);
            }

            @Override
            public boolean afterInsertaLinea(boolean insLinea)
            {
                numpesE.setValorDec(numpesE.getValorDec() + 1);
                numpesE1.setValorDec(numpesE1.getValorDec() + 1);
                if (! buscaC.isSelected())
                {
                    jt.setText(lci_numpalE.getCopia(),jt.getSelectedRow(),JT_NUMPAL);
                    lci_numpalE.setText(lci_numpalE.getCopia());
                }
                jt.setValor(lci_numcajE.getCopiaInt()+1,jt.getSelectedRow(),JT_NUMCAJ);
                lci_numcajE.setValorInt(lci_numcajE.getCopiaInt()+1);
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

            prp_empcodE.setToolTipText("");

            prp_partE.setText("cTextField1");

            prp_numpieE.setText("1");

            lci_numpalE.setMaximumSize(new java.awt.Dimension(24, 24));
            lci_numpalE.setMinimumSize(new java.awt.Dimension(24, 24));
            lci_numpalE.setPreferredSize(new java.awt.Dimension(24, 24));

            Bimpri.setToolTipText("Imprimir Registro Activo");

            lci_numeE.setEnabled(false);

            Pprinc.setLayout(new java.awt.GridBagLayout());

            Pcabe.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
            Pcabe.setMaximumSize(new java.awt.Dimension(600, 49));
            Pcabe.setMinimumSize(new java.awt.Dimension(600, 49));
            Pcabe.setPreferredSize(new java.awt.Dimension(600, 49));
            Pcabe.setQuery(true);
            Pcabe.setLayout(null);

            cLabel5.setText("Usuario ");
            cLabel5.setPreferredSize(new java.awt.Dimension(52, 18));
            Pcabe.add(cLabel5);
            cLabel5.setBounds(80, 2, 50, 17);
            Pcabe.add(cci_fecconE);
            cci_fecconE.setBounds(290, 2, 71, 17);

            cLabel6.setText("Camara");
            cLabel6.setPreferredSize(new java.awt.Dimension(52, 18));
            Pcabe.add(cLabel6);
            cLabel6.setBounds(370, 2, 50, 17);

            cci_codiE.setEnabled(false);
            Pcabe.add(cci_codiE);
            cci_codiE.setBounds(30, 2, 40, 17);

            cLabel7.setText("ID");
            cLabel7.setPreferredSize(new java.awt.Dimension(52, 18));
            Pcabe.add(cLabel7);
            cLabel7.setBounds(10, 2, 20, 17);

            usu_nombE.setEnabled(false);
            Pcabe.add(usu_nombE);
            usu_nombE.setBounds(130, 2, 70, 17);

            cLabel8.setText("Fecha Control");
            cLabel8.setPreferredSize(new java.awt.Dimension(52, 18));
            Pcabe.add(cLabel8);
            cLabel8.setBounds(210, 2, 80, 17);

            cam_codiE.setAncTexto(30);
            cam_codiE.setFormato(true);
            cam_codiE.texto.setMayusc(true);
            cam_codiE.setFormato(Types.CHAR, "XX", 2);
            cam_codiE.combo.setPreferredSize(new Dimension(200,17));
            Pcabe.add(cam_codiE);
            cam_codiE.setBounds(420, 2, 180, 17);

            cLabel9.setText("Almacen ");
            cLabel9.setPreferredSize(new java.awt.Dimension(52, 18));
            Pcabe.add(cLabel9);
            cLabel9.setBounds(10, 22, 52, 17);

            alm_codiE.setAncTexto(30);
            alm_codiE.setFormato(true);
            alm_codiE.setFormato(Types.DECIMAL, "#9", 2);
            alm_codiE.combo.setPreferredSize(new Dimension(200,17));
            Pcabe.add(alm_codiE);
            alm_codiE.setBounds(60, 22, 200, 17);

            buscaC.setText("Busca");
            buscaC.setToolTipText("Cargar Inventario en modo busqueda Palets");
            Pcabe.add(buscaC);
            buscaC.setBounds(260, 22, 80, 17);
            Pcabe.add(Birlin);
            Birlin.setBounds(340, 30, 1, 1);
            Pcabe.add(cci_fecoriE);
            cci_fecoriE.setBounds(430, 22, 71, 17);

            cLabel12.setText("Fecha Origen");
            cLabel12.setPreferredSize(new java.awt.Dimension(52, 18));
            Pcabe.add(cLabel12);
            cLabel12.setBounds(350, 22, 80, 17);

            Bcopia.setText("Utiles");
            Bcopia.addMenu("Aceptar","A");
            Bcopia.addMenu("Copiar","C");
            Bcopia.addMenu("Trasladar","M");
            Bcopia.addMenu("Unifica","U");
            Bcopia.addMenu("Importa","I");
            Pcabe.add(Bcopia);
            Bcopia.setBounds(510, 20, 90, 26);

            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
            Pprinc.add(Pcabe, gridBagConstraints);

            jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
            jt.setMaximumSize(new java.awt.Dimension(569, 169));
            jt.setMinimumSize(new java.awt.Dimension(569, 169));

            javax.swing.GroupLayout jtLayout = new javax.swing.GroupLayout(jt);
            jt.setLayout(jtLayout);
            jtLayout.setHorizontalGroup(
                jtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 567, Short.MAX_VALUE)
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
                prp_empcodE.setEnabled(false);
                lci_numpalE.setMayusc(true);
                prp_anoE.setText("" + EU.ejercicio);
                prp_empcodE.setText("" + EU.em_cod);
                prp_serieE.setText("A");
                prp_numpieE.setValorInt(1);
                lci_numpalE.setText("");
                lci_numcajE.setValorDec(0);
                cci_codiE.setEnabled(false);
                prp_serieE.setMayusc(true);
                ArrayList v = new ArrayList();
                v.add("NL"); // 0
                v.add("Producto"); // 1
                v.add("Descripcion"); // 2
                v.add("Eje"); // 3
                v.add("Emp"); // 4
                v.add("Ser"); // 5
                v.add("Part"); // 6
                v.add("Ind."); // 7
                v.add("Cantidad"); // 8
                v.add("N.Pzas"); // 9
                v.add("Caja"); // 10
                v.add("Palet"); // 10
                jt.setCabecera(v);
                Vector v1 = new Vector();

                v1.add(lci_numeE); // 0
                v1.add(pro_codiE.getFieldProCodi()); // 1
                v1.add(pro_nombE); // 2
                v1.add(prp_anoE); // 3
                v1.add(prp_empcodE); // 4
                v1.add(prp_serieE); // 5
                v1.add(prp_partE); // 6
                v1.add(prp_indiE); // 7
                v1.add(prp_pesoE); //8
                v1.add(prp_numpieE); // 9 Numero de Piezas
                v1.add(lci_numcajE); // 10  Palet
                v1.add(lci_numpalE); // 10  Palet
                jt.setCampos(v1);
                jt.setAjustarGrid(true);
                jt.setAjustarColumnas(false);
                jt.setAnchoColumna(new int[]
                    {45, 80, 200, 40,30, 20, 40, 40, 60, 40,30,40});
                jt.setAlinearColumna(new int[]
                    {2, 2, 0, 2,2, 0, 2, 2, 2, 2,2,0});
                jt.setFormatoCampos();
            } catch (Exception k) {
                Error ("Error al configurar grid",k);
            }
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 1.0;
            Pprinc.add(jt, gridBagConstraints);

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
    private gnu.chu.controles.CButtonMenu Bcopia;
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
    private gnu.chu.controles.CLinkBox alm_codiE;
    private gnu.chu.controles.CCheckBox buscaC;
    private gnu.chu.controles.CLabel cLabel10;
    private gnu.chu.controles.CLabel cLabel11;
    private gnu.chu.controles.CLabel cLabel12;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.controles.CLabel cLabel9;
    private gnu.chu.controles.CLinkBox cam_codiE;
    private gnu.chu.controles.CTextField cci_codiE;
    private gnu.chu.controles.CTextField cci_fecconE;
    private gnu.chu.controles.CTextField cci_fecoriE;
    private gnu.chu.controles.CGridEditable jt;
    private gnu.chu.controles.CTextField kiltotE;
    private gnu.chu.controles.CTextField kiltotE1;
    private gnu.chu.controles.CTextField lci_numcajE;
    private gnu.chu.controles.CTextField lci_numeE;
    private gnu.chu.controles.CTextField lci_numpalE;
    private gnu.chu.controles.CTextField nlinE;
    private gnu.chu.controles.CTextField numpesE;
    private gnu.chu.controles.CTextField numpesE1;
    private gnu.chu.camposdb.proPanel pro_codiE;
    private gnu.chu.controles.CTextField pro_nombE;
    private gnu.chu.controles.CTextField prp_anoE;
    private gnu.chu.controles.CTextField prp_empcodE;
    private gnu.chu.controles.CTextField prp_indiE;
    private gnu.chu.controles.CTextField prp_numpieE;
    private gnu.chu.controles.CTextField prp_partE;
    private gnu.chu.controles.CTextField prp_pesoE;
    private gnu.chu.controles.CTextField prp_serieE;
    private gnu.chu.controles.CTextField usu_nombE;
    // End of variables declaration//GEN-END:variables
}
