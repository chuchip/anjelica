package gnu.chu.anjelica.pad;

/**
 *
 * <p>Título: MantArticulos </p>
 * <p>Descripcion: Mantenimiento Tabla de Articulos</p>
 * <p>Empresa: miSL</p>
*  <p>Copyright: Copyright (c) 2005-2016
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
 * @author ChuchiP
 * @version 1.0
 */ 
import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.menu;
import gnu.chu.controles.CButton;
import gnu.chu.controles.CGridEditable;
import gnu.chu.controles.StatusBar;
import gnu.chu.interfaces.PAD;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.SystemOut;
import gnu.chu.utilidades.navegador;
import gnu.chu.utilidades.ventanaPad;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;


public class MantArticulos extends ventanaPad  implements PAD
{
    String idiomaEmpresa;
    final private int PROEXCLUYE=-1;
    final private int PROINCLUYE=1;
    final private int PROINCCONG=2;
    public static final String TIPO_VENDIBLE="V";
    public static final String TIPO_COMENTARIO="C";
    public static final String TIPO_DESHECHO="D";
    boolean modConsulta=false;
    String s;
    CButton  Bimpri   = new CButton(Iconos.getImageIcon("print"));

  public MantArticulos(EntornoUsuario eu, Principal p)
  {
    this(eu,p,null);
  }
  public MantArticulos(EntornoUsuario eu, Principal p,Hashtable ht)
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
      setTitulo("Mantenimiento de Articulos");

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

  public MantArticulos(menu p, EntornoUsuario eu,Hashtable ht)
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
      setTitulo("Mantenimiento de Articulos");
      jbInit();
    }
    catch (Exception e)
    {
      ErrorInit(e);
      setErrorInit(true);
    }
  }
  private int cambiaLineaGrid(int tipoProd)
  {
      gnu.chu.camposdb.proPanel proCodi;
      proCodi = pro_coexclE;
      if (tipoProd==PROINCLUYE)
        proCodi = pro_coequiE; 
      if (tipoProd==PROINCCONG)
        proCodi = pro_coeqcoE; 
      if (proCodi.getValorInt() == 0)
         return -1; // No hay producto ... paso
      try
      {
        if (!proCodi.controla(false))
        {
          mensajeErr(proCodi.getMsgError());
          return 0;
        }
        if (tipoProd==PROINCCONG && ! proCodi.isCongelado())
        {
            mensajeErr("PRODUCTO INVALIDO: NO es congelado");
            return 0;
        }
    } catch (Exception k)
    {
      Error("ERROR AL controlar Salida del Grid",k);
      return 0;
    }

    return -1;
  } 
  private void jbInit() throws Exception {
        statusBar = new StatusBar(this);
        nav = new navegador(this, dtCons, false, modConsulta ? navegador.CURYCON : navegador.NORMAL);
        iniciarFrame();
//        this.setResizable(false);

        this.setVersion("2017-04-04" + (modConsulta ? "SOLO LECTURA" : ""));
        strSql = "SELECT * FROM v_articulo where pro_activ != 0 "+
                " ORDER BY pro_codi";

        this.getContentPane().add(nav, BorderLayout.NORTH);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        this.setPad(this);
        dtCons.setLanzaDBCambio(false);
        conecta();
        initComponents();
        iniciarBotones(Baceptar, Bcancelar);
        Bimpri.setPreferredSize(new Dimension(24,24));
        Bimpri.setMaximumSize(new Dimension(24,24));
        Bimpri.setMinimumSize(new Dimension(24,24));
        Bimpri.setToolTipText("Imprimir Articulos Selecionados");
        statusBar.add(Bimpri, new GridBagConstraints(9, 0, 1, 2, 0.0, 0.0
                                             , GridBagConstraints.EAST,
                                             GridBagConstraints.VERTICAL,
                                             new Insets(0, 5, 0, 0), 0, 0));
        navActivarAll();
        this.setSize(663,524);
        activar(false);

    }
    @Override
    public void iniciarVentana() throws Exception
    { 
        pro_codartE.iniciar(dtStat, this, vl, EU);
        pro_codequE.iniciar(dtStat, this, vl, EU);
        pro_codartE.setUsaCodigoVenta(true);
    Pdiscrim.iniciar(dtCon1, EU);
    Pprinc.setButton(KeyEvent.VK_F4,Baceptar);
    s="SELECT eti_codi,eti_nomb FROM etiquetas where emp_codi = "+EU.em_cod+
       " and eti_client = 0 "+ // solo etiquetas q no pertenezcan a clientes
       " ORDER BY eti_nomb";
    dtStat.select(s);
    pro_codetiE.addDatos(dtStat);  
    
     s="SELECT env_codi,env_nomb from envases "+
       " ORDER BY env_nomb ";
    dtStat.select(s);
    env_codiE.setCeroIsNull(false);
    env_codiE.addDatos(dtStat,false);    
    
    dtStat.select("SELECT cat_codi,cat_nomb FROM categorias_art  ORDER BY cat_codi");
    cat_codiE.addDatos(dtStat);    
    
    dtStat.select("SELECT cal_codi,cal_nomb FROM calibres_art  ORDER BY cal_codi");
    cal_codiE.addDatos(dtStat);    
    
    idiomaEmpresa=MantPaises.getLocalePais(pdempresa.getPais(dtStat, EU.em_cod),dtStat);
    pro_codetiE.addDatos("-1","Sin Etiqueta");
    s="SELECT fpr_codi,fpr_nomb FROM v_famipro "+
        " ORDER BY fpr_nomb";
    dtStat.select(s);
    fam_codiE.addDatos(dtStat);
    fam_codiE.setFormato(Types.DECIMAL,"#9",2);
   
  
    sbe_codiE.iniciar(dtStat, this, vl, EU);
    sbe_codiE.setTipo("A");
   
    pro_prvulcoE.iniciar(dtStat,this,vl,EU);
    MantTipoIVA.llenaLinkBoxIVA(pro_tipivaE, dtCon1);
    gnu.chu.anjelica.pad.pdconfig.llenaDiscr(dtCon1, cam_codiE, "AC", EU.em_cod);

    pro_codiE.setColumnaAlias("pro_codi");
    pro_nombE.setColumnaAlias("pro_nomb");
    pro_nomcorE.setColumnaAlias("pro_nomcor");
    fam_codiE.setColumnaAlias("fam_codi");
    pro_univenE.setColumnaAlias("pro_univen");
    pro_deunveE.setColumnaAlias("pro_deunve");
    pro_diacomE.setColumnaAlias("pro_diacom");
    pro_cadcongE.setColumnaAlias("pro_cadcong");
    pro_tipivaE.setColumnaAlias("pro_tipiva");
    pro_costkmiE.setColumnaAlias("pro_costkmi");
    pro_mancosE.setColumnaAlias("pro_mancos");
    pro_codartE.setColumnaAlias("pro_codart");
    pro_unicomE.setColumnaAlias("pro_unicom");
    pro_feulcoE.setColumnaAlias("pro_feulco");
    pro_prvulcoE.setColumnaAlias("pro_prvulco");
    pro_fulconE.setColumnaAlias("pro_fulcon");
    pro_coexisE.setColumnaAlias("pro_coexis");

    pro_conmaxE.setColumnaAlias("pro_conmax");
//    pro_disc1E.setColumnaAlias("pro_disc1");
//    pro_disc2E.setColumnaAlias("pro_disc2");
//    pro_disc3E.setColumnaAlias("pro_disc3");
//    pro_disc4E.setColumnaAlias("pro_disc4");
    cam_codiE.setColumnaAlias("cam_codi");
    sbe_codiE.setColumnaAlias("sbe_codi");
    pro_artconE.setColumnaAlias("pro_artcon");
    pro_indtcoE.setColumnaAlias("pro_indtco");
    pro_envvacE.setColumnaAlias("pro_envvac");
    pro_activE.setColumnaAlias("pro_activ");
   
    pro_cajpalE.setColumnaAlias("pro_cajpal");
    pro_kguniE.setColumnaAlias("pro_kguni");
    pro_cosincE.setColumnaAlias("pro_cosinc");
    pro_kgcajE.setColumnaAlias("pro_kgcaj");
    pro_stkuniE.setColumnaAlias("pro_stkuni");
    pro_stockE.setColumnaAlias("pro_stock");
    pro_numcroE.setColumnaAlias("pro_numcro");
    pro_kgminE.setColumnaAlias("pro_kgmin");
    pro_kgmaxE.setColumnaAlias("pro_kgmax");
    pro_coinstE.setColumnaAlias("pro_coinst");
    pro_tiplotE.setColumnaAlias("pro_tiplot");
    pro_codetiE.setColumnaAlias("pro_codeti");
    env_codiE.setColumnaAlias("env_codi");
    cat_codiE.setColumnaAlias("cat_codi");
    cal_codiE.setColumnaAlias("cal_codi");
    activarEventos();
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
         jtEqui.addMouseListener(new MouseAdapter() {
            @Override
             public void mouseClicked(MouseEvent e)
             {
                 if (nav.getPulsado()==navegador.EDIT || nav.getPulsado()==navegador.ADDNEW)
                 {
                     if (!jtEqui.isEnabled())
                     {
                         jtEqui.setEnabled(true);
                         jtEqui.requestFocusInicioLater();
                     }
                 }
             }
         });
         
         jtIdiomas.addMouseListener(new MouseAdapter() {
            @Override
             public void mouseClicked(MouseEvent e)
             {
                 if (nav.getPulsado()==navegador.EDIT || nav.getPulsado()==navegador.ADDNEW)
                 {
                     if (!jtIdiomas.isEnabled())
                     {
                         jtIdiomas.setEnabled(true);
                         jtIdiomas.requestFocusInicioLater();
                     }
                 }
             }
         });
          jtEquCon.addMouseListener(new MouseAdapter() {
            @Override
             public void mouseClicked(MouseEvent e)
             {
                 if (nav.getPulsado()==navegador.EDIT || nav.getPulsado()==navegador.ADDNEW)
                 {
                     if (pro_artconE.getValorInt()==0  &&  !jtEquCon.isEnabled())
                     {
                         jtEquCon.setEnabled(true);
                         jtEquCon.requestFocusInicioLater();
                     }
                 }
             }
         });
         jtExclu.addMouseListener(new MouseAdapter() {
            @Override
             public void mouseClicked(MouseEvent e)
             {
                 if (nav.getPulsado()==navegador.EDIT || nav.getPulsado()==navegador.ADDNEW)
                 {
                     if (!jtExclu.isEnabled())
                     {
                         jtExclu.setEnabled(true);
                         jtExclu.requestFocusInicioLater();
                     }
                 }
             }
         });
    }
    
   void verDatos(DatosTabla dt)
  {
    try {
      if (dt.getNOREG())
        return;

      pro_codiE.setText(dt.getString("pro_codi"));

      s="SELECT * FROM v_articulo WHERE  pro_codi = "+dt.getInt("pro_codi");
      if (! dtCon1.select(s))
      {
        mensajeErr("Codigo NO ENCONTRADO ... SEGURAMENTE SE BORRO");
        resetTexto();
        pro_codiE.setText(dt.getString("pro_codi"));
        return;
      }
      pro_nombE.setText(dtCon1.getString("pro_nomb"));
      pro_nomcorE.setText(dtCon1.getString("pro_nomcor"));
      fam_codiE.setText(dtCon1.getString("fam_codi"));
      pro_univenE.setText(dtCon1.getString("pro_univen"));
      pro_deunveE.setText(dtCon1.getString("pro_deunve"));
      pro_diacomE.setText(dtCon1.getString("pro_diacom"));
      pro_cadcongE.setText(dtCon1.getString("pro_cadcong"));
        pro_tipivaE.setText(dtCon1.getString("pro_tipiva"));
      pro_costkmiE.setValor(dtCon1.getInt("pro_costkmi"));
      pro_mancosE.setValor(dtCon1.getInt("pro_mancos"));
      pro_codartE.setText(dtCon1.getString("pro_codart"));
      pro_unicomE.setValor(dtCon1.getString("pro_unicom"));
      pro_feulcoE.setText(dtCon1.getFecha("pro_feulco","dd-MM-yyyy"));
      pro_prvulcoE.setText(dtCon1.getString("pro_prvulco"));
      pro_fulconE.setText(dtCon1.getFecha("pro_fulcon","dd-MM-yyyy"));
      pro_coexisE.setValor(dtCon1.getString("pro_coexis"));

      pro_conmaxE.setValor(dtCon1.getInt("pro_conmax"));
      Pdiscrim.setText(dtCon1);

      sbe_codiE.setValorInt(dtCon1.getInt("sbe_codi"));
      cam_codiE.setText(dtCon1.getString("cam_codi"));
      pro_artconE.setValor(dtCon1.getInt("pro_artcon"));
      pro_indtcoE.setValor(dtCon1.getInt("pro_indtco"));
      pro_envvacE.setValor(dtCon1.getInt("pro_envvac"));
      pro_activE.setValor(dtCon1.getInt("pro_activ"));
      
      pro_cajpalE.setValorDec(dtCon1.getDouble("pro_cajpal"));
      pro_kguniE.setValorDec(dtCon1.getDouble("pro_kguni"));
      pro_cosincE.setValorDec(dtCon1.getDouble("pro_cosinc"));
      pro_kgcajE.setValorDec(dtCon1.getDouble("pro_kgcaj"));
      pro_stkuniE.setValorDec(dtCon1.getDouble("pro_stkuni"));
      pro_stockE.setValorDec(dtCon1.getDouble("pro_stock"));
      pro_numcroE.setValorDec(dtCon1.getDouble("pro_numcro"));
      pro_kgminE.setValorDec(dtCon1.getDouble("pro_kgmin"));
      pro_kgmaxE.setValorDec(dtCon1.getDouble("pro_kgmax"));
      pro_coinstE.setValor(dtCon1.getInt("pro_coinst"));
      pro_tiplotE.setValor(dtCon1.getString("pro_tiplot"));
      pro_codetiE.setValorDec(dtCon1.getInt("pro_codeti",true));
      env_codiE.setValorDec(dtCon1.getInt("env_codi",true));
      pro_oblfsaE.setSelected(dtCon1.getInt("pro_oblfsa")!=0);
      pro_fecaltE.setDate(dtCon1.getDate("pro_fecalt"));
      pro_feulmoE.setDate(dtCon1.getDate("pro_feulmo"));
      usu_nombE.setText(dtCon1.getString("usu_nomb"));
      cat_codiE.setValorDec(dtCon1.getInt("cat_codi"));
      cal_codiE.setValorDec(dtCon1.getInt("cal_codi"));
      pro_codequE.setValorInt(dtCon1.getInt("pro_codequ",true));
      verDatosAgru(pro_codiE.getValorInt(),pro_codiE.getValorInt(),jtExclu,"artiexcl");
      verDatosAgru(pro_codiE.getValorInt(),pro_codiE.getValorInt(),jtEqui,"artiequiv");
      verDatosAgru(pro_codiE.getValorInt(),0,jtEquCon,"artequcon");
      verDatosIdiomas(pro_codiE.getValorInt());
    } catch (Exception k)
    {
      Error("Error al ver Datos",k);
    }
  }
   
  void verDatosIdiomas(int proCodi) throws SQLException
  {
    jtIdiomas.removeAllDatos();
    s=" select l.loc_codi,loc_nomb,pro_codi, pro_nomloc"
        + " from anjelica.locales as l left join anjelica.articulo_locale as al on al.loc_codi=l.loc_codi and al.pro_codi="+
         proCodi+" where l.loc_codi != '"+idiomaEmpresa+ "'order by l.loc_codi";
    if (dtCon1.select(s))
    {
        do
        {
            ArrayList v=new ArrayList();
            v.add(dtCon1.getString("loc_codi"));
            v.add(dtCon1.getString("loc_nomb",true));
            v.add(dtCon1.getString("pro_nomloc",true));
            jtIdiomas.addLinea(v);
            
        } while (dtCon1.next());
    }
    
  }
  void verDatosAgru(int proCodIni,int proCodFin, CGridEditable jt,String tabla) throws SQLException
  {
      jt.removeAllDatos();

      s="SELECT * FROM "+tabla+" WHERE pro_codini="+proCodIni+" or pro_codfin ="+proCodFin;
      if (! dtCon1.select(s))
          return;
      int proCodfin;
      do
      {
          ArrayList v=new ArrayList();
         if (dtCon1.getInt("pro_codfin")==proCodFin)
             proCodfin=dtCon1.getInt("pro_codini");
         else
             proCodfin=dtCon1.getInt("pro_codfin");

          v.add(proCodfin);
          v.add(getNombProd(proCodfin, dtStat));
          jt.addLinea(v);
      } while (dtCon1.next());
     
  }
    @Override
  public void activar(boolean act)
  {
    pro_codiE.setEnabled(act);
    pro_nombE.setEnabled(act);
    pro_nomcorE.setEnabled(act);
    fam_codiE.setEnabled(act);
    pro_deunveE.setEnabled(act);
    pro_diacomE.setEnabled(act);
    pro_cadcongE.setEnabled(act);
    pro_univenE.setEnabled(act);
    pro_tipivaE.setEnabled(act);
    pro_costkmiE.setEnabled(act);
    pro_mancosE.setEnabled(act);
    pro_codartE.setEnabled(act);
    pro_unicomE.setEnabled(act);
    jtEqui.setEnabled(act);
    jtExclu.setEnabled(act);
    jtEquCon.setEnabled(act);
    pro_coexisE.setEnabled(act);
    jtIdiomas.setEnabled(act);
    pro_conmaxE.setEnabled(act);
    pro_codequE.setEnabled(act);
    Pdiscrim.setEnabled(act);

    sbe_codiE.setEnabled(act);
    cam_codiE.setEnabled(act);
    pro_artconE.setEnabled(act);
    pro_indtcoE.setEnabled(act);
    pro_envvacE.setEnabled(act);
    pro_activE.setEnabled(act);

    pro_cajpalE.setEnabled(act);
    pro_kguniE.setEnabled(act);
    pro_cosincE.setEnabled(act);
    pro_kgcajE.setEnabled(act);
    pro_stkuniE.setEnabled(act);
    pro_stockE.setEnabled(act);
    pro_numcroE.setEnabled(act);
    pro_kgminE.setEnabled(act);
    pro_kgmaxE.setEnabled(act);
    pro_oblfsaE.setEnabled(act);
    Baceptar.setEnabled(act);
    Bcancelar.setEnabled(act);
    pro_coinstE.setEnabled(act);
    pro_tiplotE.setEnabled(act);
    pro_codetiE.setEnabled(act);
    env_codiE.setEnabled(act);
    cat_codiE.setEnabled(act);
    cal_codiE.setEnabled(act);
    if (!act || nav.pulsado==navegador.QUERY)
    {
        pro_feulcoE.setEnabled(act);
        pro_prvulcoE.setEnabled(act);
        pro_fulconE.setEnabled(act);
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

    @Override
  public void PADQuery()
  {
//      System.out.println("Ancho: "+this.getSize().width+ " Alto: "+this.getSize().height);
    activar(true);
    pro_coinstE.setEnabled(true);
    setQuery(true);
    
    pro_oblfsaE.setEnabled(false);
    resetTexto();
    
    pro_activE.setValor("-1");
    pro_codiE.requestFocus();
  }

    @Override
  public void ej_query1()
  {
    Component c;
    if ((c=Pprinc.getErrorConf())!=null)
    {
      c.requestFocus();
      mensaje("Error en Criterios de busqueda");
      return;
    }
    if ((c=Pinicio.getErrorConf())!=null)
    {
      c.requestFocus();
      mensaje("Error en Criterios de busqueda (Panel Inicio)");
      return;
    }
      if ((c=Pfamil.getErrorConf())!=null)
    {
      c.requestFocus();
      mensaje("Error en Criterios de busqueda (Panel parametros)");
      return;
    }
    ArrayList v = new ArrayList();
    v.add(pro_codiE.getStrQuery());
    v.add(pro_nombE.getStrQuery());
    v.add(pro_nomcorE.getStrQuery());
    v.add(fam_codiE.getStrQuery());
    v.add(pro_univenE.getStrQuery());
    v.add(pro_deunveE.getStrQuery());
    v.add(pro_diacomE.getStrQuery());
    v.add(pro_cadcongE.getStrQuery());  
    v.add(pro_tipivaE.getStrQuery());
    v.add(pro_costkmiE.getStrQuery());
    v.add(pro_mancosE.getStrQuery());
    v.add(pro_codartE.getStrQuery());
    v.add(pro_unicomE.getStrQuery());
    v.add(pro_feulcoE.getStrQuery());
    v.add(pro_prvulcoE.getStrQuery());
    v.add(pro_fulconE.getStrQuery());
    v.add(pro_coexisE.getStrQuery());

    v.add(pro_conmaxE.getStrQuery());


    v.add(sbe_codiE.getStrQuery());
    v.add(cam_codiE.getStrQuery());
    v.add(pro_artconE.getStrQuery());
    v.add(pro_indtcoE.getStrQuery());
    v.add(pro_envvacE.getStrQuery());
    v.add(pro_activE.getStrQuery());

    v.add(pro_cajpalE.getStrQuery());
    v.add(pro_kguniE.getStrQuery());
    v.add(pro_cosincE.getStrQuery());
    v.add(pro_kgcajE.getStrQuery());
    v.add(pro_stkuniE.getStrQuery());
    v.add(pro_stockE.getStrQuery());
    v.add(pro_numcroE.getStrQuery());
    v.add(pro_kgminE.getStrQuery());
    v.add(pro_kgmaxE.getStrQuery());
    v.add(pro_coinstE.getStrQuery());
    v.add(pro_tiplotE.getStrQuery());
    v.add(pro_codetiE.getStrQuery());
    v.add(env_codiE.getStrQuery());
    v.add(cat_codiE.getStrQuery());
    v.add(cal_codiE.getStrQuery());
    s = "SELECT * FROM v_articulo ";
    s = creaWhere(s, v,true);
    s+=  Pdiscrim.getCondWhere(null);
    s+=" ORDER BY pro_nomb ";
    setQuery(false);
    
//    debug(s);
    try
    {
      if (!dtCons.select(s))
      {
        mensaje("");
        mensajeErr("No encontrados Registros para estos criterios");
        rgSelect();
        activaTodo();
        verDatos(dtCons);
        return;
      }
      mensaje("");
      strSql = s;
      nav.pulsado=navegador.NINGUNO;
      activaTodo();
      rgSelect();
      verDatos(dtCons);
      mensajeErr("Nuevos registros selecionados");
    }
    catch (Exception ex)
    {
      fatalError("Error al buscar Producots: ", ex);
    }

  }

  public void canc_query()
  {
    setQuery(false);
    
    mensaje("");
    mensajeErr("Consulta ... CANCELADA");
    nav.pulsado=navegador.NINGUNO;
    activaTodo();
    verDatos(dtCons);
  }
  void setQuery(boolean query)
  {
    Pprinc.setQuery(query);
    Pdiscrim.setQuery(query);
    Pinicio.setQuery(query);
    Pfamil.setQuery(query);
        

  }
  void resetTexto()
  {
    Pprinc.resetTexto();
    Pdiscrim.resetTexto();
    Pinicio.resetTexto();
  }
    @Override
  public void PADEdit()
  {
    activar(true);
    pro_codiE.setEnabled(false);
    try
    {
      if (!setBloqueo(dtAdd, "v_articulo", pro_codiE.getText()))
       {
         msgBox(msgBloqueo);
         nav.pulsado = navegador.NINGUNO;
         activaTodo();
         return;
       }

      if (! dtAdd.select("select * from v_articulo where pro_codi= "+pro_codiE.getText(),true))
      {
        mensajeErr("Registro ha sido borrado");
        resetBloqueo(dtAdd, "v_articulo",  pro_codiE.getText());
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
    jtIdiomas.setEnabled(false);
    jtEqui.setEnabled(false);
    jtExclu.setEnabled(false);
    jtEquCon.setEnabled(false);
    pro_codiE.setEnabled(false);
  }

    @Override
  public void ej_edit1()
  {
    try
    {
      dtAdd.edit(dtAdd.getCondWhere());
      dtAdd.setDato("pro_feulmo","current_date");
      actTabla(dtAdd);
      dtAdd.update(stUp);
      borraAgrupa(pro_codiE.getValorInt());
      insertaAgrupa(pro_codiE.getValorInt());
      borraLengua(pro_codiE.getValorInt());
      insertaLengua(pro_codiE.getValorInt());
      resetBloqueo(dtAdd, "v_articulo",  pro_codiE.getText(),false);
      ctUp.commit();
      verDatos(dtCons);
    }
    catch (Throwable ex)
    {
      Error("Error al Modificar datos", ex);
      return;
    }
    mensaje("");
    mensajeErr("Datos ... Modificados");
    nav.pulsado=navegador.NINGUNO;
    activaTodo();
//    verDatos(dtCons);
  }
  /**
   * Borra agrupaciones para el producto. Articulos Equivalentes y excluyentes.
   * @param proCodi
   * @throws SQLException
   */
  private void borraAgrupa (int proCodi ) throws SQLException
    {
      s="DELETE FROM artiexcl WHERE pro_codini="+proCodi+" or pro_codfin ="+proCodi;
      dtAdd.executeUpdate(s);
      s="DELETE FROM artiequiv WHERE pro_codini="+proCodi+" or pro_codfin ="+proCodi;
      dtAdd.executeUpdate(s);
      s="DELETE FROM artequcon WHERE pro_codini="+proCodi+" or pro_codfin ="+proCodi;
      dtAdd.executeUpdate(s);
  }
  private void borraLengua(int proCodi) throws SQLException
  {
      s="DELETE FROM articulo_locale WHERE pro_codi="+proCodi;
      dtAdd.executeUpdate(s);
  }
  /**
   * Inserta nuevas agrupaciones para el producto. Articulos Equivalentes y excluyentes.
   * @param proCodi
   * @throws SQLException
   */
  private void insertaAgrupa (int proCodi ) throws SQLException
    {
      insertaAgrupa1(proCodi,  jtExclu,"artiexcl");
      insertaAgrupa1(proCodi,  jtEqui,"artiequiv");
      insertaAgrupa1(proCodi,  jtEquCon,"artequcon");
  }
  
  private void  insertaLengua(int proCodi) throws SQLException
  {
      int nRow=jtIdiomas.getRowCount();
      for (int n=0;n< nRow;n++)
      {
          if (jtIdiomas.getValString(n,2).trim().equals(""))
              continue;
          dtAdd.addNew("articulo_locale");
          dtAdd.setDato("pro_codi",proCodi);
          dtAdd.setDato("loc_codi",jtIdiomas.getValString(n,0));
          dtAdd.setDato("pro_nomloc",jtIdiomas.getValString(n,2));
          dtAdd.update();
      }
  }
  
  private void  insertaAgrupa1(int proCodi,CGridEditable jt,String tabla) throws SQLException
  {
      int nRow=jt.getRowCount();
      for (int n=0;n< nRow;n++)
      {
          if (jt.getValorInt(n,0)==0)
              continue;
          dtAdd.addNew(tabla);
          dtAdd.setDato("pro_codini",proCodi);
          dtAdd.setDato("pro_codfin",jt.getValorInt(n,0));
          dtAdd.update();
      }
  }
    @Override
  public void canc_edit()
  {
    try
    {
      resetBloqueo(dtAdd, "v_articulo", pro_codiE.getText());
    }
    catch (Exception k)
    {
      Error("Error al Desbloquear el registro", k);
      return;
    }

    mensaje("");
    nav.pulsado=navegador.NINGUNO;
    activaTodo();
    mensajeErr("Modificacion de Datos Cancelada");
    verDatos(dtCons);
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
    jtIdiomas.setEnabled(false);
    pro_codiE.requestFocus();
    pro_coinstE.setValor("-1");
    pro_feulcoE.setEnabled(false);
    pro_prvulcoE.setEnabled(false);
    pro_stkuniE.setEnabled(false);
    pro_stockE.setEnabled(false);
    pro_fulconE.setEnabled(false);
    pro_artconE.setValor("0");
    pro_indtcoE.setValor("-1");
    pro_envvacE.setValor("0");
    pro_activE.setValor("-1");
    pro_codetiE.setValorInt(0);
    env_codiE.setValorInt(0);
    cat_codiE.setValorInt(1);
    cal_codiE.setValorInt(1);
    try
    {
        verDatosIdiomas(0);
    } catch (SQLException ex)
    {
        Error("Error al inicializar grid de idiomas", ex);
    }
    mensaje("Introduzca datos del producto a insertar ...");
  }
    @Override
  public boolean checkAddNew()
  {

    if (pro_codiE.isNull())
    {
      mensajeErr("Introduzca un codigo para el Producto");
      pro_codiE.requestFocus();
      return false;
    }
    if (pro_nombE.isNull())
    {
      mensajeErr("Introduzca Nombre de Producto");
      pro_nombE.requestFocus();
      return false;
    }
    if (pro_codartE.isNull()  && pro_tiplotE.getValor().equals("V"))
    {
      mensajeErr("Introduzca Codigo Interno de Articulo ");
      pro_codartE.setText(pro_codiE.getText());
      pro_codartE.requestFocus();
      return false;
    }
    
    if (!fam_codiE.controla(true))
    {
      mensajeErr("Familia NO es valida");
      return false;
    }
    if (pro_diacomE.getValorInt()==0)
    {
      mensajeErr("Introduzca Dias de Consumo (Caducidad fresco)");
      pro_diacomE.requestFocus();
      return false;
    }
    if (pro_cadcongE.getValorInt()==0)
    {
      mensajeErr("Introduzca Meses Dias de Consumo Congelado");
      pro_cadcongE.requestFocus();
      return false;
    }
    if (!pro_tipivaE.controla(true))
    {
      mensajeErr("Tipo de IVA no valido");
      return false;
    }
    s=Pdiscrim.controla();
    if (s!=null)
    {
        mensajeErr(s);
        return false;
    }
    try {
        if (MantArticVenta.getNombreArticulo(dtStat,pro_codartE.getText())==null && pro_tiplotE.getValor().equals("V"))
        {
            mensajeErr("Articulo de VENTA no existe");
            pro_codartE.requestFocus();
            return false;
        }
        if (! sbe_codiE.controla())
        {
         mensajeErr("SubEmpresa  NO VALIDA");
         return false;
        }
    } catch (SQLException k1)
    {
          Error("Error al controlar subempresa",k1);
          return false;
    }
    if (! cam_codiE.controla())
    {
      mensajeErr("Camara  NO VALIDA");
      cam_codiE.requestFocus();
      return false;
    }
   if (! pro_codetiE.controla())
   {
     mensajeErr("Codigo Etiqueta  NO VALIDA");
     return false;
   }
   if (! env_codiE.controla())
   {
     mensajeErr("Tipo Envase,  NO VALIDO");
     return false;
   }
   if (! cat_codiE.controla())
   {
     mensajeErr("Categoria NO VALIDA");
     return false;
   }
   if (! cal_codiE.controla())
   {
     mensajeErr("Clasificacion NO VALIDA");
     return false;
   }
   if (jtIdiomas.isEnabled())
       jtIdiomas.salirGrid();
   
    if (jtExclu.isEnabled())
    {
        jtExclu.salirGrid();
        if (cambiaLineaGrid(PROEXCLUYE)>=0)
            return false;
    }
     if (jtEqui.isEnabled())
    {
        jtEqui.salirGrid();
        if (cambiaLineaGrid(PROINCLUYE)>=0)
            return false;
    }
   
    if (jtEquCon.isEnabled())
    {
        jtEquCon.salirGrid();
        if (cambiaLineaGrid(PROINCCONG)>=0)
            return false;
    }
    return true;
  }

    @Override
  public void ej_addnew1()
  {
    try
    {
      s="SELECT * FROM v_articulo WHERE "
              + " pro_codi = "+pro_codiE.getValorInt();
      if (dtStat.select(s))
      {
        mensajeErr("Codigo de Articulo ya existe");
        return;
      }
      mensaje("Insertando PRODUCTO ...",false);
      dtAdd.addNew("v_articulo");
      dtAdd.setDato("emp_codi", EU.em_cod);
      dtAdd.setDato("pro_codi", pro_codiE.getValorInt());
      dtAdd.setDato("pro_fecalt","current_date");

      actTabla(dtAdd);

      dtAdd.update(stUp);
      insertaAgrupa(pro_codiE.getValorInt());
      insertaLengua(pro_codiE.getValorInt());
      ctUp.commit();
    }
    catch (Exception ex)
    {
      Error("Error al Insertar datos",ex);
      return;
    }
    mensaje("");
    mensajeErr("Datos ... Insertados");
    nav.pulsado=navegador.NINGUNO;
    activaTodo();
  }

  void actTabla(DatosTabla dt) throws Exception
  {
    dt.setDato("pro_nomb", pro_nombE.getText());
    dt.setDato("pro_nomcor", pro_nomcorE.getText());
    dt.setDato("fam_codi", fam_codiE.getText());
    dt.setDato("pro_univen", pro_univenE.getValor());
    dt.setDato("pro_deunve", pro_deunveE.getText());
    dt.setDato("pro_diacom", pro_diacomE.getValorInt());
    dt.setDato("pro_cadcong", pro_cadcongE.getValorInt());    
    dt.setDato("pro_tipiva", pro_tipivaE.getText());
    dt.setDato("pro_costkmi", pro_costkmiE.getValor());
    dt.setDato("pro_mancos", pro_mancosE.getValor());
    dt.setDato("pro_codart", pro_codartE.getText());
    dt.setDato("pro_unicom", pro_unicomE.getValor());
    dt.setDato("pro_feulco", pro_feulcoE.getText(), "dd-MM-yyyy");
    dt.setDato("pro_prvulco", pro_prvulcoE.getValorInt());
    dt.setDato("pro_fulcon", pro_fulconE.getText(), "dd-MM-yyyy");
    dt.setDato("pro_coexis", pro_coexisE.getValor());
    dt.setDato("pro_conmax", pro_conmaxE.getValor());
    Pdiscrim.setDato(dt);
    
    dt.setDato("sbe_codi",sbe_codiE.getValorInt());
    dt.setDato("cam_codi",cam_codiE.getText());
    dt.setDato("pro_artcon", pro_artconE.getValor());
    dt.setDato("pro_indtco", pro_indtcoE.getValor());
    dt.setDato("pro_envvac", pro_envvacE.getValor());
    dt.setDato("pro_activ", pro_activE.getValor());

    dt.setDato("pro_cajpal", pro_cajpalE.getValorInt());
    dt.setDato("pro_kguni", pro_kguniE.getValorDec());
    dt.setDato("pro_cosinc", pro_cosincE.getValorDec());
    dt.setDato("pro_kgcaj", pro_kgcajE.getValorDec());
    dt.setDato("pro_stkuni", pro_stkuniE.getValorDec());
    dt.setDato("pro_stock", pro_stockE.getValorDec());
    dt.setDato("pro_numcro", pro_numcroE.getValorInt());
    dt.setDato("pro_kgmin", pro_kgminE.getValorDec());
    dt.setDato("pro_kgmax", pro_kgmaxE.getValorDec());
    dt.setDato("pro_coinst", pro_coinstE.getValor());
    dt.setDato("pro_tiplot", pro_tiplotE.getValor());
    dt.setDato("pro_codeti",pro_codetiE.getValorInt());
    dt.setDato("env_codi",env_codiE.getValorInt());
    dt.setDato("cat_codi",cat_codiE.getValorInt());
    dt.setDato("cal_codi",cal_codiE.getValorInt());
    dt.setDato("pro_oblfsa",pro_oblfsaE.isSelected()?1:0);
    dt.setDato("usu_nomb",EU.usuario);
    dt.setDato("pro_codequ",pro_codequE.isNull()?null:pro_codequE.getValorInt());
  }
    @Override
  public void canc_addnew()
  {
    mensaje("");
    nav.pulsado=navegador.NINGUNO;
    activaTodo();
    mensajeErr("Insercion de Datos Cancelada");
    verDatos(dtCons);
  }

    @Override
  public void PADDelete()
  {
    try
    {
      s="SELECT * FROM V_STKPART WHERE pro_codi =  "+pro_codiE.getValorInt();
      if (dtStat.select(s))
      {
          msgBox("Articulo tiene apuntes en stock-Partidas. Imposible BORRAR");
          nav.pulsado = navegador.NINGUNO;
          activaTodo();
          return;
      }
      if (!setBloqueo(dtAdd, "v_articulo",
                       pro_codiE.getText()))
      {
        msgBox(msgBloqueo);
        nav.pulsado = navegador.NINGUNO;
        activaTodo();
        return;
      }

      if (!dtAdd.select("select * from v_articulo where pro_codi= " + pro_codiE.getText(), true))
      {
        mensajeErr("Registro ha sido borrado");
        resetBloqueo(dtAdd, "v_articulo", pro_codiE.getText());
        activaTodo();
        mensaje("");
        return;
      }
    } catch (Exception k)
    {
      Error("Error al bloquear el registro", k);
      return;
    }
    Baceptar.setEnabled(true);
    Bcancelar.setEnabled(true);
    Bcancelar.requestFocus();
    mensaje("Borrar Registro ...");
  }

    @Override
  public void ej_delete1()
  {
    try
    {
      dtAdd.delete(stUp);
      resetBloqueo(dtAdd, "v_articulo", pro_codiE.getText(),false);
      borraAgrupa(pro_codiE.getValorInt());
      borraLengua(pro_codiE.getValorInt());
      ctUp.commit();
      rgSelect();
    }
    catch (Exception ex)
    {
      Error("Error al borrar Registro",ex);
    }
    nav.pulsado=navegador.NINGUNO;
    activaTodo();
    verDatos(dtCons);
    mensaje("");
    mensajeErr("Registro ... Borrado");
  }
  public void canc_delete()
  {
    try
    {
      resetBloqueo(dtAdd, "v_articulo", pro_codiE.getText());
    }
    catch (Exception k)
    {
      Error("Error al Desbloquear el registro", k);
      return;
    }

    mensaje("");
    nav.pulsado=navegador.NINGUNO;
    activaTodo();
    mensajeErr("Borrado  de Datos Cancelada");
    verDatos(dtCons);
  }
  void Bimpri_actionPerformed()
   {    
     try {
       if (dtCons.getNOREG())
       {
         mensajeErr("NO HAY NINGUN PRODUCTO SELECIONADO");
         return;
       }

       HashMap mp = new HashMap();
       JasperReport jr;
       jr = gnu.chu.print.util.getJasperReport(EU,"articulos");

       String condWhere=(dtCons.getCondWhere().equals("")?"":" AND ")+
              dtCons.getCondWhere();
        s="select a.*,f.fpr_nomb from v_articulo as a, v_famipro as f "+
            " where a.fam_codi = f.fpr_codi "+
            condWhere+
            " order by a.pro_codi";
       ResultSet rs;

       rs=dtCon1.getStatement().executeQuery(dtCon1.parseaSql(s));

       JasperPrint jp = JasperFillManager.fillReport(jr, mp,
           new JRResultSetDataSource(rs));
       gnu.chu.print.util.printJasper(jp, EU);
     }
     catch (JRException | SQLException | PrinterException k)
     {
       Error("Error al imprimir albaran", k);
     }
   }
/**
     * Devuelve literal para Imprimir en etiquetas dependiendo si el producto es congelado
     * o no lo es.
     * @param codProd codigo de producto
     * @param dt DatosTabla sobre el que ejecutar la select
     * @return String con la cadena a poner en etiqueta
     * @throws SQLException
     */
   public static String getConservar(int codProd, DatosTabla dt) throws SQLException
   {
     String conservarE;
     String s = "select pro_artcon from v_articulo  where " +
         " pro_artcon = 0 " + // Articulo NO congelado
         " and pro_codi = " + codProd;

     if (dt.select(s))
       conservarE = "Conservar entre 0 y 2ºC";
     else
       conservarE = "Conservar a -18ºC";
     return conservarE;
   }
   /**
    * Devuelve los kilos estimados para las unidades mandadas de un producto
    * @param codProd
    * @param dt
    * @param unidades
    * @return kilos estimados para las unidades mandadas de un producto
    * @throws SQLException 
    */
   public static double getKilos(int codProd, DatosTabla dt, double unidades) throws SQLException
   {
        return unidades*getKilosUnid(codProd,dt);  
   }
    /**
    * Devuelve las unidades estimadas para unos kilos mandados de un producto
    * @param codProd
    * @param dt
    * @param kilos
    * @return unidades estimadas para unos kilos mandados de un producto
    * @throws SQLException 
    */
   public static double getUnidades(int codProd, DatosTabla dt, double kilos) throws SQLException
   {
       double kilosUnid=getKilosUnid(codProd,dt);
       if (kilosUnid==0)
           kilosUnid=1;
       return gnu.chu.utilidades.Formatear.redondea(kilos/ kilosUnid,0);
   }
   /**
    * Devuelve los kilos estimados para las unidades mandadas de un producto
    * @param codProd
    * @param dt
    * @return kilos estimados para las unidades mandadas de un producto
    * @throws SQLException 
    */
   public static double getKilosUnid(int codProd, DatosTabla dt) throws SQLException
   {
        String s = "select pro_kguni from v_articulo  where " +
         "  pro_codi = " + codProd;
        if (!dt.select(s))
            return 0;
        return dt.getDouble("pro_kguni"); 
   }
    /**
    * Devuelve los kilos por caja
    * @param codProd
    * @param dt
    * @return kilos estimados por caja. 0 si no encuentra el producto.
    * @throws SQLException 
    */
   public static double getKilosCaja(int codProd, DatosTabla dt) throws SQLException
   {
        String s = "select pro_kgcaj from v_articulo  where " +
         "  pro_codi = " + codProd;
        if (!dt.select(s))
            return 0;
        return dt.getDouble("pro_kgcaj"); 
   }
     /**
    * Devuelve la camara asignada a un producto
    * @param codProd
    * @param dt
    * @return camara asignada a un producto. Null si no encuentra el producto
    * @throws SQLException 
    */
   public static String getCamara(int codProd, DatosTabla dt) throws SQLException
   {
        String s = "select cam_codi from v_articulo  where " +
         "  pro_codi = " + codProd;
        if (!dt.select(s))
            return null;
        return dt.getString("cam_codi"); 
   }
   public static final  int KGXUNI=1;
   public static final  int KGXCAJ=2;
   public static final  int TIPVENCAJA=3;
   /**
    * Devuelve un HashMap<String, Integer> con la relacion entre unidades y kilos
    * El hashmap tendra los campos: 
    * KGXUNI: Kilos por unidad (Double)
    * KGXCAJ: Kilos por caja (Double)
    * TIPVENCAJA: Ventas por caja (1) o por unidades (0)
    * @param codProd Codigo de producto a buscar
    * @param dt DatosTabla
    * @return null si no encuentra el producto
    * @throws SQLException 
    */
   public static HashMap<Integer, Double> getRelUnidadKilos(int codProd, DatosTabla dt) throws SQLException
   {
        if (!dt.select("select pro_kguni,pro_kgcaj, pro_univen from v_articulo  where " +
         "  pro_codi = " + codProd))
            return null;
        HashMap<Integer, Double> ht =new HashMap();
        ht.put(KGXUNI, dt.getDouble("pro_kguni"));
        ht.put(KGXCAJ, dt.getDouble("pro_kgcaj"));
        ht.put(TIPVENCAJA, dt.getString("pro_univen").equals("C")?1.0:0);
        return ht;
   }
   /**
    * Devuelve el numero de piezas en las cajas mandadas
    * @param codProd
    * @param dt 
    * @param caja Numero de cajas
    * @return Numero de unidades resultantes. 0 Si no se encuentra el productro
    * @throws SQLException 
    */
//   public static double getUnidCaja(int codProd, DatosTabla dt, double caja) throws SQLException
//   {
//        return caja*getUnidCaja(codProd,dt);  
//   }
//   /**
//    * Devuelve el numero de unidades (piezas) por caja
//    * @param codProd Codigo producto
//    * @param dt DatosTabla
//    * @return 0 si no encuentra el producto.
//    * @throws SQLException 
//    */
//   public static double getUnidCaja(int codProd, DatosTabla dt) throws SQLException
//   {
//        String s = "select pro_unicaj from v_articulo  where " +
//         "  pro_codi = " + codProd;
//        if (!dt.select(s))
//            return 0;
//        return dt.getDouble("pro_unicaj"); 
//   }
   /**
     * Devuelve el nombre de producto
     * @param codProd Codigo Producto
     * @param dt DatosTabla sobre el que ejecutar la select
     * @return Nombre de producto. NULL si no lo encuentra
     * @throws SQLException
     */
   public static String getNombProd(int codProd, DatosTabla dt) throws SQLException
   {
     String s = "select pro_nomb from v_articulo  where " +
         "  pro_codi = " + codProd;

     if (!dt.select(s))
       return null;
     return dt.getString("pro_nomb");
   }
    /**
     * Devuelve el nombre del articulo (Codigo NO numerico)
     * @param codArtic Codigo Producto
     * @param dt DatosTabla sobre el que ejecutar la select
     * @return Nombre de producto. NULL si no lo encuentra
     * @throws SQLException
     */
   public static String getNombreArticulo(String codArtic, DatosTabla dt) throws SQLException
   {
     String s = "select pro_nomb from v_articulo  where " +
         "  pro_codart= '" + codArtic+"'";

     if (!dt.select(s))  
       return null;
     return dt.getString("pro_nomb");
   }
   /**
    * Devuelve el tipo de Producto
    * @param codProd Codigo de producto a buscar
    * @param dt Datos Tabla
    * @return 'C' -> Comentario, 'V' -> Vendible, 'D' -> Deshecho
    * @throws SQLException 
    */
   public static String getTipoProd(int codProd, DatosTabla dt) throws SQLException
   {
     String s = "select pro_tiplot from v_articulo  where " +
         "  pro_codi = " + codProd;
     if (!dt.select(s))
       return null;
     return dt.getString("pro_tiplot");
   }
   
   public static boolean hasControlExist(int codProd, DatosTabla dt) throws SQLException
   {
     String s = "select pro_coexis from v_articulo  where " +
         "  pro_codi = " + codProd;
     if (!dt.select(s))
       throw new SQLException("Articulo"+codProd+" no encontrado en Maestro Articulos");
     return dt.getString("pro_coexis").equals("S");
   }

   
   /**
    * Devuelve si el producto es vendible
    * @param codProd
    * @param dt
    * @return true es vendible. False pues de que no. 
    *          Si el codigo de producto no existe devolvera como NO vendible.
    * @throws SQLException 
    */
   public static boolean isVendible(int codProd, DatosTabla dt) throws SQLException
   {
       String vend=getTipoProd(codProd,dt);
       if (vend==null)
           return false;
       return vend.equals("V");
   }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pro_coexclE = new gnu.chu.camposdb.proPanel();
        pro_noexclE = new gnu.chu.controles.CTextField();
        pro_coequiE = new gnu.chu.camposdb.proPanel();
        pro_noequiE = new gnu.chu.controles.CTextField();
        pro_coeqcoE = new gnu.chu.camposdb.proPanel();
        pro_noeqcoE = new gnu.chu.controles.CTextField();
        loc_codiE = new gnu.chu.controles.CTextField();
        pro_nomlocE = new gnu.chu.controles.CTextField();
        loc_nombE = new gnu.chu.controles.CTextField();
        Pprinc = new gnu.chu.controles.CPanel();
        Pcabe = new gnu.chu.controles.CPanel();
        cLabel1 = new gnu.chu.controles.CLabel();
        pro_codiE = new gnu.chu.controles.CTextField(Types.DECIMAL,"####9");
        pro_nombE = new gnu.chu.controles.CTextField(Types.CHAR,"X",50);
        cLabel6 = new gnu.chu.controles.CLabel();
        pro_activE = new gnu.chu.controles.CComboBox();
        Ptab = new gnu.chu.controles.CTabbedPane();
        Pinicio = new gnu.chu.controles.CPanel();
        cLabel2 = new gnu.chu.controles.CLabel();
        pro_nomcorE = new gnu.chu.controles.CTextField(Types.CHAR,"X",30);
        cLabel3 = new gnu.chu.controles.CLabel();
        cLabel4 = new gnu.chu.controles.CLabel();
        fam_codiE = new gnu.chu.controles.CLinkBox();
        cLabel5 = new gnu.chu.controles.CLabel();
        cam_codiE = new gnu.chu.controles.CLinkBox();
        cLabel7 = new gnu.chu.controles.CLabel();
        pro_univenE = new gnu.chu.controles.CComboBox();
        pro_deunveE = new gnu.chu.controles.CTextField(Types.CHAR,"X",15);
        cLabel8 = new gnu.chu.controles.CLabel();
        cLabel9 = new gnu.chu.controles.CLabel();
        pro_tipivaE = new gnu.chu.controles.CLinkBox();
        pro_diacomE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9");
        cLabel10 = new gnu.chu.controles.CLabel();
        pro_numcroE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9");
        cLabel11 = new gnu.chu.controles.CLabel();
        pro_tiplotE = new gnu.chu.controles.CComboBox();
        cLabel12 = new gnu.chu.controles.CLabel();
        pro_codetiE = new gnu.chu.controles.CLinkBox();
        pro_oblfsaE = new gnu.chu.controles.CCheckBox();
        cLabel13 = new gnu.chu.controles.CLabel();
        pro_artconE = new gnu.chu.controles.CComboBox();
        cLabel14 = new gnu.chu.controles.CLabel();
        pro_envvacE = new gnu.chu.controles.CComboBox();
        cLabel16 = new gnu.chu.controles.CLabel();
        pro_cadcongE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9");
        cPanel1 = new gnu.chu.controles.CPanel();
        cLabel15 = new gnu.chu.controles.CLabel();
        pro_coexisE = new gnu.chu.controles.CComboBox();
        cLabel17 = new gnu.chu.controles.CLabel();
        pro_coinstE = new gnu.chu.controles.CComboBox();
        cLabel18 = new gnu.chu.controles.CLabel();
        pro_conmaxE = new gnu.chu.controles.CComboBox();
        pro_costkmiE = new gnu.chu.controles.CComboBox();
        cLabel20 = new gnu.chu.controles.CLabel();
        pro_kgmaxE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###,##9");
        cLabel21 = new gnu.chu.controles.CLabel();
        cLabel22 = new gnu.chu.controles.CLabel();
        pro_kgminE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###,##9");
        cLabel38 = new gnu.chu.controles.CLabel();
        pro_mancosE = new gnu.chu.controles.CComboBox();
        env_codiL = new gnu.chu.controles.CLabel();
        env_codiE = new gnu.chu.controles.CLinkBox();
        cPanel2 = new gnu.chu.controles.CPanel();
        cLabel25 = new gnu.chu.controles.CLabel();
        pro_feulcoE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel27 = new gnu.chu.controles.CLabel();
        pro_fulconE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel28 = new gnu.chu.controles.CLabel();
        cLabel19 = new gnu.chu.controles.CLabel();
        pro_stockE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----,--9.99");
        cLabel23 = new gnu.chu.controles.CLabel();
        pro_stkuniE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---,--9");
        pro_prvulcoE = new gnu.chu.camposdb.prvPanel();
        cLabel26 = new gnu.chu.controles.CLabel();
        pro_unicomE = new gnu.chu.controles.CComboBox();
        cLabel24 = new gnu.chu.controles.CLabel();
        pro_cajpalE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9");
        cLabel30 = new gnu.chu.controles.CLabel();
        pro_kguniE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9.99");
        cLabel31 = new gnu.chu.controles.CLabel();
        pro_kgcajE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9.99");
        cLabel32 = new gnu.chu.controles.CLabel();
        sbe_codiE = new gnu.chu.camposdb.sbePanel();
        sbe_nombL = new gnu.chu.controles.CLabel();
        cLabel33 = new gnu.chu.controles.CLabel();
        pro_fecaltE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel34 = new gnu.chu.controles.CLabel();
        usu_nombE = new gnu.chu.controles.CTextField(Types.CHAR,"X",15);
        cLabel35 = new gnu.chu.controles.CLabel();
        pro_feulmoE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel37 = new gnu.chu.controles.CLabel();
        pro_cosincE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#9.999");
        pro_codartE = new gnu.chu.camposdb.proPanel();
        Pdiscrim = new gnu.chu.camposdb.DiscProPanel();
        Pfamil = new gnu.chu.controles.CPanel();
        cLabel39 = new gnu.chu.controles.CLabel();
        cat_codiE = new gnu.chu.controles.CLinkBox();
        cLabel40 = new gnu.chu.controles.CLabel();
        cal_codiE = new gnu.chu.controles.CLinkBox();
        cLabel29 = new gnu.chu.controles.CLabel();
        pro_indtcoE = new gnu.chu.controles.CComboBox();
        jtIdiomas = new gnu.chu.controles.CGridEditable(3);
        cLabel41 = new gnu.chu.controles.CLabel();
        cLabel42 = new gnu.chu.controles.CLabel();
        pro_codequE = new gnu.chu.camposdb.proPanel();
        Pexclu = new gnu.chu.controles.CPanel();
        jtExclu = new gnu.chu.controles.CGridEditable(2) {
            public void cambiaColumna(int col,int colNueva, int row)
            {
                try
                {
                    if (col == 0)
                    {
                        String nombArt = pro_coexclE.getNombArt(pro_coexclE.getText());
                        if (nombArt == null)
                        jtExclu.setValor("**PRODUCTO NO ENCONTRADO**", row, 1);
                        else
                        jtExclu.setValor(nombArt, row, 1);
                    }
                }
                catch (Exception k)
                {
                    Error("Error al buscar Nombre Articulo", k);
                }
            }
            @Override
            public int cambiaLinea(int row, int col)
            {
                return cambiaLineaGrid(PROINCLUYE);
            }
        }
        ;
        Pequiv = new gnu.chu.controles.CPanel();
        jtEqui = new gnu.chu.controles.CGridEditable(2) {
            public void cambiaColumna(int col, int colNueva,int row)
            {
                try
                {
                    if (col == 0)
                    {
                        String nombArt = pro_coequiE.getNombArt(pro_coequiE.getText());
                        if (nombArt == null)
                        jtEqui.setValor("**PRODUCTO NO ENCONTRADO**", row, 1);
                        else
                        jtEqui.setValor(nombArt, row, 1);
                    }
                }
                catch (Exception k)
                {
                    Error("Error al buscar Nombre Articulo", k);
                }
            }
            @Override
            public int cambiaLinea(int row, int col)
            {
                return  cambiaLineaGrid(PROINCLUYE);
            }
        }
        ;
        jtEquCon = new gnu.chu.controles.CGridEditable(2) {
            public void cambiaColumna(int col, int colNueva,int row)
            {
                try
                {
                    if (col == 0)
                    {
                        String nombArt = pro_coeqcoE.getNombArt(pro_coeqcoE.getText());
                        if (nombArt == null)
                        jtEquCon.setValor("**PRODUCTO NO ENCONTRADO**", row, 1);
                        else
                        jtEquCon.setValor(nombArt, row, 1);
                    }
                }
                catch (Exception k)
                {
                    Error("Error al buscar Nombre Articulo", k);
                }
            }
            @Override
            public int cambiaLinea(int row, int col)
            {
                return  cambiaLineaGrid(PROINCCONG);
            }
        }
        ;
        cLabel36 = new gnu.chu.controles.CLabel();
        Baceptar = new gnu.chu.controles.CButton();
        Bcancelar = new gnu.chu.controles.CButton();

        pro_noexclE.setEnabled(false);

        pro_noequiE.setEnabled(false);

        pro_noeqcoE.setEnabled(false);

        loc_codiE.setText("cTextField1");
        loc_codiE.setEnabled(false);

        pro_nomlocE.setText("cTextField1");

        loc_nombE.setText("cTextField1");
        loc_nombE.setEnabled(false);

        Pprinc.setLayout(null);

        Pcabe.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.gray, null));
        Pcabe.setLayout(null);

        cLabel1.setText("Articulo");
        Pcabe.add(cLabel1);
        cLabel1.setBounds(20, 5, 51, 20);
        Pcabe.add(pro_codiE);
        pro_codiE.setBounds(80, 5, 49, 20);
        Pcabe.add(pro_nombE);
        pro_nombE.setBounds(130, 5, 342, 20);

        cLabel6.setText("Activo");
        Pcabe.add(cLabel6);
        cLabel6.setBounds(480, 5, 43, 20);

        pro_activE.addItem("Si","-1");
        pro_activE.addItem("Pe","1");
        pro_activE.addItem("No","0");
        Pcabe.add(pro_activE);
        pro_activE.setBounds(530, 5, 50, 20);

        Pprinc.add(Pcabe);
        Pcabe.setBounds(10, 0, 600, 30);

        Pinicio.setLayout(null);

        cLabel2.setText("Descripción Abreviada");
        Pinicio.add(cLabel2);
        cLabel2.setBounds(0, 3, 130, 20);
        Pinicio.add(pro_nomcorE);
        pro_nomcorE.setBounds(130, 3, 201, 18);

        cLabel3.setText("Artic. Venta");
        Pinicio.add(cLabel3);
        cLabel3.setBounds(0, 50, 70, 17);

        cLabel4.setText("Familia");
        Pinicio.add(cLabel4);
        cLabel4.setBounds(0, 26, 51, 18);

        fam_codiE.setAncTexto(30);
        Pinicio.add(fam_codiE);
        fam_codiE.setBounds(60, 26, 285, 18);

        cLabel5.setText("Seccion");
        Pinicio.add(cLabel5);
        cLabel5.setBounds(0, 150, 51, 18);

        cam_codiE.setAncTexto(30);
        cam_codiE.setFormato(Types.CHAR, "XX");
        Pinicio.add(cam_codiE);
        cam_codiE.setBounds(60, 170, 285, 18);

        cLabel7.setText("Unidades Venta");
        Pinicio.add(cLabel7);
        cLabel7.setBounds(0, 76, 92, 18);

        pro_univenE.addItem("Piezas","P");
        pro_univenE.addItem("Cajas","C");
        pro_univenE.setPreferredSize(new Dimension(100,100));
        Pinicio.add(pro_univenE);
        pro_univenE.setBounds(100, 76, 51, 18);
        Pinicio.add(pro_deunveE);
        pro_deunveE.setBounds(160, 76, 183, 18);

        cLabel8.setText("Caducid. Fresco");
        Pinicio.add(cLabel8);
        cLabel8.setBounds(0, 100, 88, 18);

        cLabel9.setText("IVA");
        Pinicio.add(cLabel9);
        cLabel9.setBounds(350, 26, 29, 18);

        pro_tipivaE.setAncTexto(30);
        Pinicio.add(pro_tipivaE);
        pro_tipivaE.setBounds(380, 26, 233, 18);
        Pinicio.add(pro_diacomE);
        pro_diacomE.setBounds(100, 100, 41, 18);

        cLabel10.setText("Caducid. Congelado (Meses)");
        Pinicio.add(cLabel10);
        cLabel10.setBounds(154, 100, 165, 18);
        Pinicio.add(pro_numcroE);
        pro_numcroE.setBounds(400, 125, 35, 18);

        cLabel11.setText("Tipo Articulo");
        Pinicio.add(cLabel11);
        cLabel11.setBounds(340, 0, 82, 18);

        pro_tiplotE.addItem("Vendible","V");
        pro_tiplotE.addItem("Comentario","C");
        pro_tiplotE.addItem("Coment. Acum.Kg","c");
        pro_tiplotE.addItem("Deshecho","D");
        Pinicio.add(pro_tiplotE);
        pro_tiplotE.setBounds(430, 0, 180, 18);

        cLabel12.setText("Etiqueta");
        Pinicio.add(cLabel12);
        cLabel12.setBounds(370, 76, 52, 18);

        pro_codetiE.setAncTexto(40);
        pro_codetiE.setPreferredSize(new java.awt.Dimension(122, 17));
        pro_codetiE.setFormato(Types.DECIMAL,"---9");
        Pinicio.add(pro_codetiE);
        pro_codetiE.setBounds(430, 76, 186, 18);

        pro_oblfsaE.setText("Fecha Sacrificio Obligatoria");
        pro_oblfsaE.setFocusable(false);
        pro_oblfsaE.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        pro_oblfsaE.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        Pinicio.add(pro_oblfsaE);
        pro_oblfsaE.setBounds(0, 125, 180, 18);

        cLabel13.setText("Congelado");
        Pinicio.add(cLabel13);
        cLabel13.setBounds(370, 100, 68, 18);

        pro_artconE.addItem("No","0");
        pro_artconE.addItem("Si","-1");
        Pinicio.add(pro_artconE);
        pro_artconE.setBounds(440, 100, 51, 18);

        cLabel14.setText("Env. Vacio");
        Pinicio.add(cLabel14);
        cLabel14.setBounds(500, 100, 60, 18);

        pro_envvacE.addItem("No","0");
        pro_envvacE.addItem("Si","-1");
        Pinicio.add(pro_envvacE);
        pro_envvacE.setBounds(560, 100, 51, 18);

        cLabel16.setText("Nº Crotales Iguales");
        Pinicio.add(cLabel16);
        cLabel16.setBounds(290, 125, 110, 18);
        Pinicio.add(pro_cadcongE);
        pro_cadcongE.setBounds(320, 100, 35, 18);

        cPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cPanel1.setLayout(null);

        cLabel15.setText("Control Existencias");
        cPanel1.add(cLabel15);
        cLabel15.setBounds(4, 1, 115, 18);

        pro_coexisE.addItem("Si","S");
        pro_coexisE.addItem("NO","N");
        cPanel1.add(pro_coexisE);
        pro_coexisE.setBounds(130, 1, 51, 18);

        cLabel17.setText("Control Individuos");
        cPanel1.add(cLabel17);
        cLabel17.setBounds(194, 1, 109, 18);

        pro_coinstE.addItem("Si","-1");
        pro_coinstE.addItem("No","0");
        cPanel1.add(pro_coinstE);
        pro_coinstE.setBounds(313, 1, 51, 18);

        cLabel18.setText("Control Maximos");
        cPanel1.add(cLabel18);
        cLabel18.setBounds(368, 1, 109, 18);

        pro_conmaxE.addItem("Si","-1");
        pro_conmaxE.addItem("No","0");
        cPanel1.add(pro_conmaxE);
        pro_conmaxE.setBounds(481, 1, 51, 18);

        pro_costkmiE.addItem("Si","-1");
        pro_costkmiE.addItem("No","0");
        cPanel1.add(pro_costkmiE);
        pro_costkmiE.setBounds(130, 25, 51, 18);

        cLabel20.setText("Kilos minimos");
        cPanel1.add(cLabel20);
        cLabel20.setBounds(194, 25, 82, 18);
        cPanel1.add(pro_kgmaxE);
        pro_kgmaxE.setBounds(480, 25, 49, 18);

        cLabel21.setText("Mantener Costo ");
        cPanel1.add(cLabel21);
        cLabel21.setBounds(4, 47, 110, 17);

        cLabel22.setText("Control Stock Minimo");
        cPanel1.add(cLabel22);
        cLabel22.setBounds(4, 25, 128, 18);

        pro_kgminE.setToolTipText("Mantener Costo en Valoración Despieces");
        cPanel1.add(pro_kgminE);
        pro_kgminE.setBounds(310, 25, 49, 18);

        cLabel38.setText("Kilos maximos");
        cPanel1.add(cLabel38);
        cLabel38.setBounds(370, 25, 82, 18);

        pro_mancosE.addItem("Si","-1");
        pro_mancosE.addItem("No","0");
        cPanel1.add(pro_mancosE);
        pro_mancosE.setBounds(130, 47, 51, 18);

        env_codiL.setText("Envase");
        cPanel1.add(env_codiL);
        env_codiL.setBounds(190, 47, 52, 18);

        env_codiE.setAncTexto(40);
        env_codiE.setPreferredSize(new java.awt.Dimension(122, 17));
        env_codiE.setFormato(Types.DECIMAL,"###9");
        env_codiE.addDatos("0", "Generico");
        cPanel1.add(env_codiE);
        env_codiE.setBounds(250, 47, 280, 18);

        Pinicio.add(cPanel1);
        cPanel1.setBounds(30, 190, 540, 70);

        cPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Historico"));
        cPanel2.setLayout(null);

        cLabel25.setText("Ult. Compra");
        cPanel2.add(cLabel25);
        cLabel25.setBounds(6, 22, 70, 15);

        pro_feulcoE.setEnabled(false);
        cPanel2.add(pro_feulcoE);
        pro_feulcoE.setBounds(77, 20, 70, 17);

        cLabel27.setText("Ult. Coste");
        cPanel2.add(cLabel27);
        cLabel27.setBounds(460, 20, 60, 17);

        pro_fulconE.setEnabled(false);
        cPanel2.add(pro_fulconE);
        pro_fulconE.setBounds(520, 20, 70, 17);

        cLabel28.setText("Prov. Ult. Compra");
        cPanel2.add(cLabel28);
        cLabel28.setBounds(6, 46, 112, 16);

        cLabel19.setText("Kilos Actuales");
        cPanel2.add(cLabel19);
        cLabel19.setBounds(150, 20, 90, 18);

        pro_stockE.setEnabled(false);
        cPanel2.add(pro_stockE);
        pro_stockE.setBounds(240, 20, 70, 17);

        cLabel23.setText("Unid. Actuales");
        cPanel2.add(cLabel23);
        cLabel23.setBounds(320, 20, 90, 17);

        pro_stkuniE.setEnabled(false);
        cPanel2.add(pro_stkuniE);
        pro_stkuniE.setBounds(400, 20, 50, 17);
        cPanel2.add(pro_prvulcoE);
        pro_prvulcoE.setBounds(120, 46, 470, 16);

        Pinicio.add(cPanel2);
        cPanel2.setBounds(20, 260, 600, 70);

        cLabel26.setText("Unidades Compra");
        Pinicio.add(cLabel26);
        cLabel26.setBounds(460, 50, 100, 18);

        pro_unicomE.addItem("Kg","Kg");
        pro_unicomE.addItem("Un","Un");
        pro_unicomE.addItem("**","**");
        Pinicio.add(pro_unicomE);
        pro_unicomE.setBounds(570, 50, 51, 18);

        cLabel24.setText("Camara");
        Pinicio.add(cLabel24);
        cLabel24.setBounds(10, 170, 60, 18);
        Pinicio.add(pro_cajpalE);
        pro_cajpalE.setBounds(580, 125, 35, 18);

        cLabel30.setText("Costo Añadido");
        Pinicio.add(cLabel30);
        cLabel30.setBounds(450, 170, 90, 18);
        Pinicio.add(pro_kguniE);
        pro_kguniE.setBounds(560, 146, 50, 18);

        cLabel31.setText("Kilos por caja");
        Pinicio.add(cLabel31);
        cLabel31.setBounds(310, 150, 80, 18);
        Pinicio.add(pro_kgcajE);
        pro_kgcajE.setBounds(390, 150, 50, 18);

        cLabel32.setText("Cajas por Palet");
        Pinicio.add(cLabel32);
        cLabel32.setBounds(490, 125, 90, 18);

        sbe_codiE.setLabelSbe(sbe_nombL);
        Pinicio.add(sbe_codiE);
        sbe_codiE.setBounds(60, 150, 40, 17);

        sbe_nombL.setBackground(java.awt.Color.orange);
        sbe_nombL.setOpaque(true);
        Pinicio.add(sbe_nombL);
        sbe_nombL.setBounds(100, 150, 190, 17);

        cLabel33.setText("Fecha Alta");
        Pinicio.add(cLabel33);
        cLabel33.setBounds(10, 330, 60, 18);

        pro_fecaltE.setEnabled(false);
        Pinicio.add(pro_fecaltE);
        pro_fecaltE.setBounds(70, 330, 80, 18);

        cLabel34.setText("Usuario");
        Pinicio.add(cLabel34);
        cLabel34.setBounds(400, 330, 50, 18);

        usu_nombE.setEnabled(false);
        Pinicio.add(usu_nombE);
        usu_nombE.setBounds(450, 330, 150, 18);

        cLabel35.setText("Fecha Ultima Modificación");
        Pinicio.add(cLabel35);
        cLabel35.setBounds(160, 330, 150, 18);

        pro_feulmoE.setEnabled(false);
        Pinicio.add(pro_feulmoE);
        pro_feulmoE.setBounds(310, 330, 80, 18);

        cLabel37.setText("Kilos por unidad");
        Pinicio.add(cLabel37);
        cLabel37.setBounds(450, 146, 100, 18);
        Pinicio.add(pro_cosincE);
        pro_cosincE.setBounds(560, 170, 50, 18);

        pro_codartE.setAncTexto(70);
        Pinicio.add(pro_codartE);
        pro_codartE.setBounds(70, 50, 380, 18);

        Ptab.addTab("Inicio", Pinicio);
        Ptab.addTab("Discriminadores", Pdiscrim);

        Pfamil.setLayout(null);

        cLabel39.setText("Categoria");
        Pfamil.add(cLabel39);
        cLabel39.setBounds(10, 10, 60, 18);

        cat_codiE.setAncTexto(40);
        cat_codiE.setPreferredSize(new java.awt.Dimension(122, 17));
        cat_codiE.setFormato(Types.DECIMAL,"###9");
        Pfamil.add(cat_codiE);
        cat_codiE.setBounds(100, 10, 270, 18);

        cLabel40.setText("Clasificación");
        Pfamil.add(cLabel40);
        cLabel40.setBounds(10, 30, 80, 18);

        cal_codiE.setAncTexto(40);
        cal_codiE.setPreferredSize(new java.awt.Dimension(122, 17));
        cal_codiE.setFormato(Types.DECIMAL,"###9");;
        Pfamil.add(cal_codiE);
        cal_codiE.setBounds(100, 30, 270, 18);

        cLabel29.setText("Cod. Padre");
        Pfamil.add(cLabel29);
        cLabel29.setBounds(10, 72, 80, 18);

        pro_indtcoE.addItem("No","0");
        pro_indtcoE.addItem("Si","-1");
        Pfamil.add(pro_indtcoE);
        pro_indtcoE.setBounds(140, 50, 51, 18);

        try {
            ArrayList v=new ArrayList();
            v.add("Idioma");
            v.add("Descr.Idioma");
            v.add("Descr.Producto");
            jtIdiomas.setCabecera(v);
            jtIdiomas.setAnchoColumna(new int[]{60,100,180});
            jtIdiomas.setAlinearColumna(new int[]{0,0,0});
            ArrayList vc=new ArrayList();
            vc.add(loc_codiE);
            vc.add(loc_nombE);
            vc.add(pro_nomlocE);
            jtIdiomas.setCampos(vc);
            jtIdiomas.setCanDeleteLinea(false);
            jtIdiomas.setCanInsertLinea(false);
        } catch (Exception k) {Error("Error al iniciar grid de idiomas",k);}
        jtIdiomas.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Pfamil.add(jtIdiomas);
        jtIdiomas.setBounds(10, 120, 610, 210);

        cLabel41.setBackground(new java.awt.Color(255, 255, 0));
        cLabel41.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cLabel41.setText("Traduciones");
        cLabel41.setMaximumSize(new java.awt.Dimension(159, 15));
        cLabel41.setMinimumSize(new java.awt.Dimension(159, 15));
        cLabel41.setOpaque(true);
        cLabel41.setPreferredSize(new java.awt.Dimension(159, 15));
        Pfamil.add(cLabel41);
        cLabel41.setBounds(220, 100, 159, 15);

        cLabel42.setText("Incluir Dto.Comercial");
        Pfamil.add(cLabel42);
        cLabel42.setBounds(10, 50, 130, 18);
        Pfamil.add(pro_codequE);
        pro_codequE.setBounds(100, 72, 380, 17);

        Ptab.addTab("Parametros", Pfamil);

        Pexclu.setLayout(new javax.swing.BoxLayout(Pexclu, javax.swing.BoxLayout.LINE_AXIS));

        try {
            pro_coexclE.iniciar(dtStat, this, vl, EU);
            pro_coexclE.setProNomb(null);
            ArrayList v=new ArrayList();
            v.add("Producto");
            v.add("Nombre");
            jtExclu.setCabecera(v);
            jtExclu.setAnchoColumna(new int[]{60,250});
            jtExclu.setAlinearColumna(new int[]{2,0});
            Vector v1=new Vector();
            v1.add(pro_coexclE.getFieldProCodi());
            v1.add(pro_noexclE);
            jtExclu.setCampos(v1);
            jtExclu.setAjustarColumnas(true);
        } catch (Exception k ){ SystemOut.print(k);}
        Pexclu.add(jtExclu);

        Ptab.addTab("Exclusion", Pexclu);

        Pequiv.setLayout(new java.awt.GridBagLayout());

        try {
            pro_coequiE.iniciar(dtStat, this, vl, EU);
            pro_coequiE.setProNomb(null);
            ArrayList v=new ArrayList();
            v.add("Producto");
            v.add("Nombre");
            jtEqui.setCabecera(v);
            jtEqui.setAnchoColumna(new int[]{60,250});
            jtEqui.setAlinearColumna(new int[]{2,0});
            Vector v1=new Vector();
            v1.add(pro_coequiE.getFieldProCodi());
            v1.add(pro_noequiE);
            jtEqui.setCampos(v1);
            jtEqui.setAjustarColumnas(true);
        } catch (Exception k ){SystemOut.print(k);}
        jtEqui.setMaximumSize(new java.awt.Dimension(100, 100));
        jtEqui.setMinimumSize(new java.awt.Dimension(100, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Pequiv.add(jtEqui, gridBagConstraints);

        try {
            pro_coeqcoE.iniciar(dtStat, this, vl, EU);
            pro_coeqcoE.setProNomb(null);
            ArrayList v=new ArrayList();
            v.add("Producto");
            v.add("Nombre");
            jtEquCon.setCabecera(v);
            jtEquCon.setAnchoColumna(new int[]{60,250});
            jtEquCon.setAlinearColumna(new int[]{2,0});
            Vector v1=new Vector();
            v1.add(pro_coeqcoE.getFieldProCodi());
            v1.add(pro_noeqcoE);
            jtEquCon.setCampos(v1);
            jtEquCon.setAjustarColumnas(true);
        } catch (Exception k ){SystemOut.print(k);}
        jtEquCon.setMaximumSize(new java.awt.Dimension(100, 100));
        jtEquCon.setMinimumSize(new java.awt.Dimension(100, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Pequiv.add(jtEquCon, gridBagConstraints);

        cLabel36.setBackground(new java.awt.Color(255, 255, 0));
        cLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cLabel36.setText("Equivalentes Congelados");
        cLabel36.setMaximumSize(new java.awt.Dimension(159, 15));
        cLabel36.setMinimumSize(new java.awt.Dimension(159, 15));
        cLabel36.setOpaque(true);
        cLabel36.setPreferredSize(new java.awt.Dimension(159, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        Pequiv.add(cLabel36, gridBagConstraints);

        Ptab.addTab("Equivalentes", Pequiv);

        Pprinc.add(Ptab);
        Ptab.setBounds(0, 30, 630, 380);
        Pprinc.add(Baceptar);
        Baceptar.setBounds(90, 410, 130, 30);
        Pprinc.add(Bcancelar);
        Bcancelar.setBounds(440, 410, 130, 30);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CButton Bcancelar;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.camposdb.DiscProPanel Pdiscrim;
    private gnu.chu.controles.CPanel Pequiv;
    private gnu.chu.controles.CPanel Pexclu;
    private gnu.chu.controles.CPanel Pfamil;
    private gnu.chu.controles.CPanel Pinicio;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CTabbedPane Ptab;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel10;
    private gnu.chu.controles.CLabel cLabel11;
    private gnu.chu.controles.CLabel cLabel12;
    private gnu.chu.controles.CLabel cLabel13;
    private gnu.chu.controles.CLabel cLabel14;
    private gnu.chu.controles.CLabel cLabel15;
    private gnu.chu.controles.CLabel cLabel16;
    private gnu.chu.controles.CLabel cLabel17;
    private gnu.chu.controles.CLabel cLabel18;
    private gnu.chu.controles.CLabel cLabel19;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel20;
    private gnu.chu.controles.CLabel cLabel21;
    private gnu.chu.controles.CLabel cLabel22;
    private gnu.chu.controles.CLabel cLabel23;
    private gnu.chu.controles.CLabel cLabel24;
    private gnu.chu.controles.CLabel cLabel25;
    private gnu.chu.controles.CLabel cLabel26;
    private gnu.chu.controles.CLabel cLabel27;
    private gnu.chu.controles.CLabel cLabel28;
    private gnu.chu.controles.CLabel cLabel29;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel30;
    private gnu.chu.controles.CLabel cLabel31;
    private gnu.chu.controles.CLabel cLabel32;
    private gnu.chu.controles.CLabel cLabel33;
    private gnu.chu.controles.CLabel cLabel34;
    private gnu.chu.controles.CLabel cLabel35;
    private gnu.chu.controles.CLabel cLabel36;
    private gnu.chu.controles.CLabel cLabel37;
    private gnu.chu.controles.CLabel cLabel38;
    private gnu.chu.controles.CLabel cLabel39;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel40;
    private gnu.chu.controles.CLabel cLabel41;
    private gnu.chu.controles.CLabel cLabel42;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.controles.CLabel cLabel9;
    private gnu.chu.controles.CPanel cPanel1;
    private gnu.chu.controles.CPanel cPanel2;
    private gnu.chu.controles.CLinkBox cal_codiE;
    private gnu.chu.controles.CLinkBox cam_codiE;
    private gnu.chu.controles.CLinkBox cat_codiE;
    private gnu.chu.controles.CLinkBox env_codiE;
    private gnu.chu.controles.CLabel env_codiL;
    private gnu.chu.controles.CLinkBox fam_codiE;
    private gnu.chu.controles.CGridEditable jtEquCon;
    private gnu.chu.controles.CGridEditable jtEqui;
    private gnu.chu.controles.CGridEditable jtExclu;
    private gnu.chu.controles.CGridEditable jtIdiomas;
    private gnu.chu.controles.CTextField loc_codiE;
    private gnu.chu.controles.CTextField loc_nombE;
    private gnu.chu.controles.CComboBox pro_activE;
    private gnu.chu.controles.CComboBox pro_artconE;
    private gnu.chu.controles.CTextField pro_cadcongE;
    private gnu.chu.controles.CTextField pro_cajpalE;
    private gnu.chu.camposdb.proPanel pro_codartE;
    private gnu.chu.camposdb.proPanel pro_codequE;
    private gnu.chu.controles.CLinkBox pro_codetiE;
    private gnu.chu.controles.CTextField pro_codiE;
    private gnu.chu.camposdb.proPanel pro_coeqcoE;
    private gnu.chu.camposdb.proPanel pro_coequiE;
    private gnu.chu.camposdb.proPanel pro_coexclE;
    private gnu.chu.controles.CComboBox pro_coexisE;
    private gnu.chu.controles.CComboBox pro_coinstE;
    private gnu.chu.controles.CComboBox pro_conmaxE;
    private gnu.chu.controles.CTextField pro_cosincE;
    private gnu.chu.controles.CComboBox pro_costkmiE;
    private gnu.chu.controles.CTextField pro_deunveE;
    private gnu.chu.controles.CTextField pro_diacomE;
    private gnu.chu.controles.CComboBox pro_envvacE;
    private gnu.chu.controles.CTextField pro_fecaltE;
    private gnu.chu.controles.CTextField pro_feulcoE;
    private gnu.chu.controles.CTextField pro_feulmoE;
    private gnu.chu.controles.CTextField pro_fulconE;
    private gnu.chu.controles.CComboBox pro_indtcoE;
    private gnu.chu.controles.CTextField pro_kgcajE;
    private gnu.chu.controles.CTextField pro_kgmaxE;
    private gnu.chu.controles.CTextField pro_kgminE;
    private gnu.chu.controles.CTextField pro_kguniE;
    private gnu.chu.controles.CComboBox pro_mancosE;
    private gnu.chu.controles.CTextField pro_noeqcoE;
    private gnu.chu.controles.CTextField pro_noequiE;
    private gnu.chu.controles.CTextField pro_noexclE;
    private gnu.chu.controles.CTextField pro_nombE;
    private gnu.chu.controles.CTextField pro_nomcorE;
    private gnu.chu.controles.CTextField pro_nomlocE;
    private gnu.chu.controles.CTextField pro_numcroE;
    private gnu.chu.controles.CCheckBox pro_oblfsaE;
    private gnu.chu.camposdb.prvPanel pro_prvulcoE;
    private gnu.chu.controles.CTextField pro_stkuniE;
    private gnu.chu.controles.CTextField pro_stockE;
    private gnu.chu.controles.CLinkBox pro_tipivaE;
    private gnu.chu.controles.CComboBox pro_tiplotE;
    private gnu.chu.controles.CComboBox pro_unicomE;
    private gnu.chu.controles.CComboBox pro_univenE;
    private gnu.chu.camposdb.sbePanel sbe_codiE;
    private gnu.chu.controles.CLabel sbe_nombL;
    private gnu.chu.controles.CTextField usu_nombE;
    // End of variables declaration//GEN-END:variables
 /**
     * Devuelve si es congelado o no.
     * @param codProd codigo de producto
     * @param dt DatosTabla sobre el que ejecutar la select
     * @return boolean Null si no encuentra el producto.
     *         true si es congelado. False si no.
     * @throws SQLException
     */
    public static Boolean isCongelado(int codProd, DatosTabla dt) throws SQLException
   {
     String s = "select pro_artcon,pro_cadcong from v_articulo  where " +
         "  pro_codi = " + codProd;
     if (! dt.select(s))
        return null;

     return dt.getInt("pro_artcon")!=0;
   }
    /**
     *  Devuelve  si el producto influye en el dto comercial..
     * @param codProd
     * @param dt
     * @return  Devuelve true si el producto influye en el dto comercial.
     *          null si no encuentra el producto.
     * @throws SQLException 
     */
    public static Boolean getInclDtoCom(int codProd, DatosTabla dt) throws SQLException
   {
     String s = "select pro_indtco from v_articulo  where " +
         "  pro_codi = " + codProd;
     if (! dt.select(s))
        return null;

     return dt.getInt("pro_indtco")!=0;
   }
    
    public static String getNombreProdLocale(int codProd,String idioma, DatosTabla dt) throws SQLException
    {
        String s="select pro_nomloc from articulo_locale where pro_codi="+codProd+
            " and loc_codi = '"+idioma+"'";
        if (dt.select(s))
            return dt.getString("pro_nomloc");
        return getNombProd(codProd, dt);
    }
     public static String getNombreProdLocale(String codArticulo,String idioma, DatosTabla dt) throws SQLException
    {        
        String s="select pro_nomloc from articulo_locale where pro_codi  in ("+getNumerosArticulo(codArticulo,dt)+")"+
            " and loc_codi = '"+idioma+"'";
        if (dt.select(s))
            return dt.getString("pro_nomloc");
        return getNombreArticulo(codArticulo, dt);
    }
    public static int getNumeroArticulo(String codArticulo, DatosTabla dt) throws SQLException
   {
     String s = "select pro_codi  from v_articulo  where " +
         "  pro_codart = '" + codArticulo+"'";
     if (! dt.select(s))
        return -1;

     return dt.getInt("pro_codi");
   }
     public static String getNumerosArticulo(String codArticulo, DatosTabla dt) throws SQLException
   {
     String s = "select pro_codi  from v_articulo  where " +
         "  pro_codart = '" + codArticulo+"'";
     if (! dt.select(s))
        return "";
     s="";
     do
     {
         s+=s.equals("")?"":",";
         s+=dt.getInt("pro_codi");
     } while (dt.next());
     
     return s;
   }
    /**
     * Devuelve el tipo de Unidad en que se vende un producto
     * Kilos, Cajas, Piezas
     * @param codProd
     * @param dt
     * @return null si no encuentra el producto.
     * @throws SQLException 
     */
   public static String getTipoUnidad(int codProd, DatosTabla dt) throws SQLException
   {
     String s = "select pro_univen  from v_articulo  where " +
         "  pro_codi = " + codProd;
     if (! dt.select(s))
        return null;

     return dt.getString("pro_univen");
   }
    
   /**
    * Devuelve el costo a incrementar a un producto
    * @param dt
    * @param codProd
    * @return 0 si no encuentra el producto.
    * @throws SQLException
    */
   public static double getCostoIncrementar(DatosTabla dt,int codProd) throws SQLException
   {
     String s = "select pro_cosinc  from v_articulo  where " +
         "  pro_codi = " + codProd;
     if (! dt.select(s))
        return 0;

     return dt.getDouble("pro_cosinc");
   }
   /**
     * Devuelve literal para Imprimir en etiquetas dependiendo si el producto es congelado
     * o no lo es.
     * @param codProd codigo de producto
     * @param dt DatosTabla sobre el que ejecutar la select
     * @return String con la cadena a poner en etiqueta.
     *                null si no encuentra el producto.
     * @throws SQLException
     */
   public static String getStrConservar(int codProd, DatosTabla dt) throws SQLException
   {
       return getStrConservar(codProd,dt,null);
   }
  /**
     * Devuelve literal para Imprimir en etiquetas dependiendo si el producto es congelado
     * o no lo es.
     * @param codProd codigo de producto
     * @param dt DatosTabla sobre el que ejecutar la select
     * @param idioma 
     * @return String con la cadena a poner en etiqueta.
     *                null si no encuentra el producto.
     * @throws SQLException
     */
   public static String getStrConservar(int codProd, DatosTabla dt, String idioma) throws SQLException
   {
//     String s = "select pro_artcon from v_articulo  where " +
//         " pro_artcon = 0 " + // Articulo NO congelado
//         " and pro_codi = " + codProd;
     Boolean b=    isCongelado(codProd,dt);
     if (b==null)
         return null;
     if (idioma==null)
     {
        if (!b)
            return   "Conservar entre 0 y 2ºC";
        else
            return "Conservar a -18ºC";
     }
    ResourceBundle rsB =ResourceBundle.getBundle("gnu.chu.anjelica.locale.jasper",
        new Locale(idioma));
     if (!b)
            return  rsB.getString("conservar.fresco");// "Conservar entre 0 y 2ºC";
        else
            return rsB.getString("conservar.congelado"); //"Conservar a -18ºC";
   }
   /**
    * Devuelve un  ArrayList con todos los productos equivalentes a uno dado.
    * Si no tiene productos equivalentes devolvera NULL
    * @param proCodi Producto
    * @return arrayList
    * @throws SQLException 
    */
    public static ArrayList<Integer> getEquivalentes(int proCodi,DatosTabla dt) throws SQLException
    {
       String s="SELECT * FROM artiequiv WHERE pro_codini="+proCodi+" or pro_codfin ="+proCodi; 
       if (! dt.select(s))
           return null;
       ArrayList<Integer> al=new ArrayList();
       do
       {
           al.add(dt.getInt("pro_codini")==proCodi?dt.getInt("pro_codfin"):dt.getInt("pro_codini"));
       } while (dt.next());
       return al;
    }
    public static int getProductoPadre(int codProd, DatosTabla dt) throws SQLException
    {
        String s = "select pro_codequ  from v_articulo  where "
            + "  pro_codi = " + codProd;
        if (!dt.select(s))
            return codProd;
        return dt.getObject("pro_codequ")==null?codProd:dt.getInt("pro_codequ");
    }
}
