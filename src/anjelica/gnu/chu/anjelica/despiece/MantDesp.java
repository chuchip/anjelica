
package gnu.chu.anjelica.despiece;
/**
 *
 * <p>Título: MantDesp </p>
    * <p>Descripción: Mantenimiento de Despieces </p>
 * <p>Parametros:<br>
 * boolean admin: Modo administrador
 
 * boolean modPrecio Permite modificar precios.
 * 
 * </p>
 * <p> Comprueba las variables de entorno
 *   checktidcodi y autollenardesp para ver si tiene que comprobar el
 *   tipo de despiece (por defecto SI) y si debe hacer autollenado de
 *   los productos para un tipo de despiece (por defecto, NO)
 *   Comprueba variabenos le despPend para ve si puede salir de un despiece faltando kilos
 *   por defecto NO.
 * </p>
 * <P>
 * El parametro MODPRECIO indica si se veran los precios.  Los precios NO se pueden 
 * modificar.
 * </P>
 * <p>Copyright: Copyright (c) 2005-2018
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
* <p>Empresa: MISL</p>
* @author chuchiP
* @version 2.0
*/

import gnu.chu.Menu.LoginDB;
import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.almacen.MvtosAlma;
import gnu.chu.anjelica.almacen.StkPartid;
import gnu.chu.anjelica.almacen.ActualStkPart;
import gnu.chu.anjelica.almacen.CheckStock;
import gnu.chu.anjelica.almacen.Comvalm;
import gnu.chu.anjelica.almacen.pdalmace;
import gnu.chu.anjelica.compras.MantAlbCom;
import gnu.chu.anjelica.compras.MantAlbComCarne;
import gnu.chu.anjelica.compras.MantAlbComPlanta;

import gnu.chu.anjelica.listados.etiqueta;
import gnu.chu.anjelica.menu;
import gnu.chu.anjelica.pad.MantArticulos;
import gnu.chu.anjelica.pad.MantPaises;
import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.anjelica.pad.pdejerci;
import gnu.chu.anjelica.sql.Desorilin;
import gnu.chu.anjelica.sql.DesorilinId;
import gnu.chu.anjelica.sql.Desporig;
import gnu.chu.anjelica.sql.DesporigId;
import gnu.chu.comm.BotonBascula;
import gnu.chu.controles.CComboBox;
import gnu.chu.controles.StatusBar;
import gnu.chu.interfaces.PAD;
import gnu.chu.interfaces.ejecutable;
import gnu.chu.sql.DatosTabla;
import gnu.chu.sql.conexion;
import gnu.chu.utilidades.*;
import gnu.chu.winayu.ayuLote;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.*;
import java.beans.PropertyVetoException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class MantDesp extends ventanaPad implements PAD
{    
    boolean swForzadoTraz=false;
    int ART_MER=80;
    int ART_MERMA=4;
    DatTrazFrame datTrazFrame;
    public final static int MIN_DIAS_CAD=10;
    Date ultFecCaduc;
    Date deoFecpro;
    Date deoFecSacr;
    int tipoEtiqueta=0;
    int etiquetaInterior;
//    Date fechaCaduc;
    String crotal;
    int tidCodAnt=0;
    ArrayList<Integer> dtFinAnt=new ArrayList();
    public final static String DESP_CERRADO="N";
    public final static String DESP_ABIERTO="S";
    public final static String DESP_BLOQUEADO="B";
    DatosTabla dtAux;
    private boolean swErrCab=false;// Indica si la linea desglose esta en error.
    int tipoEmp; // Tipo Empresa (Sala Despiece o Plantacion)
    private boolean isEmpPlanta=false; // Indica si la empresa es tipo Plantacion
    private StkPartid stkPartid;
    private String tablaCab="desporig";
    private String tablaEnt="desorilin";
    private String tablaSal="v_despfin";
    private String vistaDesp="v_despori";
    private String condHist=""; // COndiciones del historico
    private String deoBlockAnt;
//    AutoDesp autoDesp;
    String lastSqlWhereLin;
//    int proCodiB, ejeLoteB, numLoteB, numIndiB, almOrigB, numLinB;
    String serLoteB;
    double deoKilosB;
    final static String TABLA_BLOCK = "desporig";
    final int FORZAR_LOTE=1;
    Desporig desorca;
    Desorilin desorli;
    int proCodAnt;
    double defKilAnt;
    boolean MODPRECIO = false;
    
    private boolean verGrupo;
    private boolean AUTOLLENARDESP; // Variable cogida de tabla parametros
    //private boolean DESPPENDIENTE=false;
// Variable que indica si se podra mantener el lote de entrada
    private boolean AGRUPALOTE; // Variable cogida de tabla parametros.
    final private int JTCAB_GRID = 1;
    final private int JTLIN_GRID = 2;
    private boolean swMantLote = false;
    private boolean CHECKTIDCODI = true;// Variable cogida de tabla parametros
    private boolean AVISATIDCODI = false;// Variable cogida de tabla parametros
    public static final String SERIE = "G";
    //final static int JTCAB_EMPLOT=2;
    final static int JTCAB_PROCODI = 0;
    final static int JTCAB_PRONOMB = 1;
    final static int JTCAB_EJELOT = 2;
    final static int JTCAB_SERLOT = 3;
    final static int JTCAB_NUMLOT = 4;
    final static int JTCAB_NUMIND = 5;
    final static int JTCAB_KILOS = 6;
    final static int JTCAB_PRECIO = 7;
    final static int JTCAB_NL = 8;
    final static int JTCAB_PREUSU = 9;
    final static int JTLIN_PROCODI = 0;
    final static int JTLIN_PRONOMB = 1;
    final static int JTLIN_KILOS = 2;
    final static int JTLIN_UNID = 3;
    final static int JTLIN_COSTO = 4;
    final static int JTLIN_FECCAD = 5;
    final static int JTLIN_NUMIND = 6;
    final static int JTLIN_ORDEN = 7;
    final static int JTLIN_NUMCAJ = 8;
    final static int JTLIN_PREUSU = 9;
//    private boolean swArtBloq = false;
    private BotonBascula botonBascula;
    conexion ctProd;
    DatosTabla dtProd;
//    boolean P_ADMIN = false;
    ActualStkPart stkPart;
    boolean swTienePrec = false;
    boolean inTidCodi = false;
    public final static double LIMDIF = 0.02; // 2 %.
    int proCodTD = 0;
//  ayuLote ayuLot=null;
    utildesp utdesp;
    etiqueta etiq;
    String s; 
    int nIndDes = 0;
    int nLiCab = 0;
  //  JMenuItem ImprEtiqMI = new JMenuItem("Imprimir");
    private int proCodeti;
    private int ultCodigoEtiqueta = 0;
    boolean swPrimeraLinea = false;
    private int hisRowid=0;
    /** Creates new form MantDesp */
    public MantDesp() {
        initComponents();
    }

    public MantDesp(EntornoUsuario eu, Principal p) {
        this(eu, p, null);
    }

    public MantDesp(EntornoUsuario eu, Principal p, Hashtable<String, String> ht) {
        EU = eu;
        vl = p.panel1;
        jf = p;
        eje = true;

        setTitulo("Mantenimiento Despieces");
        setAcronimo("mandes");
        try
        {
            setParametros(ht);
            if (jf.gestor.apuntar(this))
                jbInit();
            else
            {
                setErrorInit(true);
            }
        } catch (Exception e)
        {
            ErrorInit(e);
        }
    }

    public MantDesp(menu p, EntornoUsuario eu, Hashtable<String,String> ht) {

        EU = eu;
        vl = p.getLayeredPane();
        setTitulo("Mantenimiento Despieces");
        eje = false;
        setParametros(ht);
        try
        {
            jbInit();
        } catch (Exception e)
        {
            ErrorInit(e);            
        }
    }
    private void setParametros(Hashtable<String,String> ht)
    {
        if (ht != null)
        {                
            if (ht.get("modPrecio") != null)
                MODPRECIO = Boolean.parseBoolean(ht.get("modPrecio"));
            if (ht.get("admin") != null)
                setArgumentoAdmin(Boolean.parseBoolean(ht.get("admin")));
        }
    }
    public static boolean irDespiece(int ejeNume,int deoCodi,Principal jf )
    {
        ejecutable prog;
        if (jf==null)
            return false;
        if ((prog = jf.gestor.getProceso(MantDesp.getNombreClase())) == null)
            return false;
        MantDesp cm = (MantDesp) prog;
        if (cm.inTransation())
        {
            mensajes.mensajeAviso("Mantenimiento Despieces ocupado. No se puede realizar la busqueda");
            return false;
        }
        cm.PADQuery();
        cm.setEjeNume(ejeNume);
        cm.setDeoCodi(deoCodi);
        cm.ej_query();
        jf.gestor.ir(cm);
        return true;
    }
    private void jbInit() throws Exception {
        if (isAdmin() )
            MODPRECIO=true; 
        setVersion("2018-04-08" + (MODPRECIO ? " (VER PRECIOS)" : "") + (isArgumentoAdmin() ? " ADMINISTRADOR" : ""));
        swThread = false; // Desactivar Threads en ej_addnew1/ej_edit1/ej_delete1 .. etc

        CHECKTIDCODI = EU.getValorParam("checktidcodi", CHECKTIDCODI);
        AUTOLLENARDESP = EU.getValorParam("autollenardesp", AUTOLLENARDESP);
        AGRUPALOTE = EU.getValorParam("agrupalote", AGRUPALOTE);
        AVISATIDCODI = EU.getValorParam("avisatidcodi", AVISATIDCODI);
//        DESPPENDIENTE=EU.getValorParam("despPend", DESPPENDIENTE);
        nav = new navegador(this, dtCons, false, navegador.NORMAL);
        statusBar = new StatusBar(this);

        iniciarFrame();
      
        
        this.getContentPane().add(nav, BorderLayout.NORTH);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        this.setPad(this);
        dtCons.setLanzaDBCambio(false);
        botonBascula = new BotonBascula(EU, this);
        botonBascula.setPreferredSize(new Dimension(50, 24));
        botonBascula.setMinimumSize(new Dimension(50, 24));
        botonBascula.setMaximumSize(new Dimension(50, 24));
        statusBar.add(botonBascula, new GridBagConstraints(9, 0, 1, 2, 0.0, 0.0, GridBagConstraints.EAST,
            GridBagConstraints.VERTICAL,
            new Insets(0, 5, 0, 0), 0, 0));

        initComponents();
        conecta();

        iniciarBotones(Baceptar, Bcancelar);
        opVerAgrup.setDependePadre(false);
        opVerGrupo.setDependePadre(false);
        opSimular.setDependePadre(false);
        navActivarAll();
        this.setSize(700, 524);
        activar(false);
    }

    @Override
    public void afterConecta() throws SQLException, ParseException {
        ctProd = ct;
        dtProd = new DatosTabla(ctProd);
        dtAux  = new DatosTabla(ctProd);
        tipoEmp=pdconfig.getTipoEmpresa(EU.em_cod, dtStat);
        isEmpPlanta=tipoEmp==pdconfig.TIPOEMP_PLANTACION;
        prv_codiE.iniciar(dtProd, this, vl, EU);
        pro_codiE.setProNomb(pro_nocabE);
        pro_codiE.iniciar(dtProd, this, vl, EU);
        pro_codiE.setCamposLote(deo_ejelotE, deo_serlotE, pro_loteE,
            pro_numindE, deo_kilosE,deo_almoriE.getTextField());
        pro_codiE.setAyudaLotes(true);
        pro_codlE.setProNomb(null);
        pro_codlE.iniciar(dtProd, this, vl, EU);
        pro_codlE.setEntrada(true);
        tid_codiE.setCeroIsNull(true);       
        tid_codiE.setModoConsulta(false);
        tid_codiE.iniciar(dtProd, this, vl, EU);
        tid_codiE.setAdmin(isAdmin());
        pdalmace.llenaLinkBox(deo_almoriE, dtCon1);
        pdalmace.llenaLinkBox(deo_almdesE, dtCon1);
//        s = "SELECT alm_codi, alm_nomb from v_almacen order by alm_codi";
//        dtCon1.select(s);
//        deo_almoriE.addDatos(dtCon1);
//        dtCon1.first();
//        deo_almdesE.addDatos(dtCon1);
        opVerGrupo.setSelected(false);
        strSql = getStrSql() + getCondWhere()
            + getOrderQuery();
    }

    @Override
    public void iniciarVentana() throws Exception {
        etiquetaInterior=etiqueta.getCodigoEtiqInterior(EU);
        cli_codiE.setColumnaAlias("cli_codi");
        deo_desnueE.setEnabled(false);
        deo_feccadE.setEnabled(false);
        deo_fecproE.setEnabled(false);
        deo_fecsacE.setEnabled(false);
        LoginDB.iniciarLKEmpresa(EU, dtStat);
//    stUp.setMaxRows(1);
        jtCab.cuadrarGrid();
        jtLin.cuadrarGrid();
        utdesp = new utildesp();
        opMantFecha.setEnabled(isAdmin());
        deo_incvalE.setEnabled(isAdmin());
        deo_cerraE.setEnabled(isAdmin());
        opAnularControl.setEnabled(isAdmin());
        Pcabe.setDefButton(Baceptar);
        Pcabe.setEscButton(Bcancelar);
        Pcabe.setAltButton(BirGrid);
        jtLin.setButton(KeyEvent.VK_F9, Bimpeti.getBotonAccion());
        Plotgen.setButton(KeyEvent.VK_F2, BirGrid);
        Ptipdes.setButton(KeyEvent.VK_F2, BirGrid);
        Plotgen.setDefButton(Baceptar);
        Plotgen.setEscButton(Bcancelar);
        Plotgen.setButton(KeyEvent.VK_F4, Baceptar);
        tid_codiE.setButton(KeyEvent.VK_F4, Baceptar);
        jtCab.setDefButton(Baceptar);
        jtLin.setDefButton(Baceptar);
        cli_codiE.iniciar(dtAux, this, vl, EU);

        botonBascula = new BotonBascula(EU, this);
        botonBascula.setPreferredSize(new Dimension(50, 24));
        botonBascula.setMinimumSize(new Dimension(50, 24));
        botonBascula.setMaximumSize(new Dimension(50, 24));
        statusBar.add(botonBascula, new GridBagConstraints(9, 0, 1, 2, 0.0, 0.0, GridBagConstraints.EAST,
            GridBagConstraints.VERTICAL,
            new Insets(0, 5, 0, 0), 0, 0));
        deo_kilosE.setLeePesoBascula(botonBascula);
        def_kilosE.setLeePesoBascula(botonBascula);
        deo_kilosE.setToolTipText("Pulse <F1> Para coger peso de la bascula");


        jtLin.setButton(KeyEvent.VK_F2, BirGrid);
        jtCab.setButton(KeyEvent.VK_F2, BirGrid);
        Pcabe.setButton(KeyEvent.VK_F2, BirGrid);

//    jtLin.tableView.getColumnModel().getColumn(n).setIdentifier(null);
//    emp_codiE.setColumnaAlias("emp_codi");
        deo_almdesE.setColumnaAlias("deo_almdes");
        deo_almoriE.setColumnaAlias("deo_almori");
        deo_incvalE.setColumnaAlias("deo_incval");
        eje_numeE.setColumnaAlias("eje_nume");
        deo_fechaE.setColumnaAlias("deo_fecha");
        deo_codiE.setColumnaAlias("deo_codi");
        tid_codiE.setColumnaAlias("tid_codi");
        pro_codiE.setColumnaAlias("pro_codi");
        deo_ejlogeE.setColumnaAlias("deo_ejloge");
//    deo_emplotE.setColumnaAlias("deo_emplot");

        pro_loteE.setColumnaAlias("pro_lote");
        pro_numindE.setColumnaAlias("pro_numind");
        deo_kilosE.setColumnaAlias("deo_kilos");
        deo_numdesE.setColumnaAlias("deo_numdes");
        usu_nombE.setColumnaAlias("usu_nomb");
//    deo_prcostE.setColumnaAlias("deo_prcost");
        deo_ejelotE.setColumnaAlias("deo_ejloge");

        deo_selogeE.setColumnaAlias("deo_seloge");
        deo_nulogeE.setColumnaAlias("deo_nuloge");
        jtLin.setButton(KeyEvent.VK_F5, BcopLin);
        stkPart = new ActualStkPart(dtAdd, EU.em_cod);
        cargaPSC.setSelected(AUTOLLENARDESP);
         BvalDesp.setVisible(MODPRECIO);
        activarEventos();
        verDatos(dtCons);
//    this.setEnabled(true);
        mensajeErr("");
        etiqueta.getReports(dtStat, EU.em_cod,0);
        if (!dtStat.getNOREG())
        {
            do 
            {
               Bimpeti.addMenu(dtStat.getString("eti_nomb"), dtStat.getString("eti_codi")); 
            } while (dtStat.next());
        }        
        
        activar(false);
    }

    void activarEventos() 
    {
        Btrazabilidad.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarDatosTraz();
            }
        });
        
//        BRestFec.addActionListener(new ActionListener()
//        {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                try
//                {
//                    ultFecCaduc=deo_feccadE.getDate();
//                    def_feccadE.setDate(ultFecCaduc);
//                    jtLin.setValor(ultFecCaduc,JTLIN_FECCAD);
//                } catch (ParseException ex)
//                {
//                   Error("ERROR AL resturar fechas caducidad,produccion",ex);
//                }
//            }
//        });
        BForzarProd.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                forzarProd();
            }
        });
        deo_codiE.addMouseListener(new MouseAdapter()
        {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (!deo_codiE.isEnabled() && e.getClickCount()>1)
                    irTactil();
            }
        });
        BvalDesp.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                ejecutable prog;
                if ((prog=jf.gestor.getProceso(ValDespi.getNombreClase()))==null)
                    return;
                 ValDespi cm=(ValDespi) prog;
                 if (cm.inTransation())
                 {
                    msgBox("Valoracion Despieces ocupado. No se puede realizar la busqueda");
                    return;
                 }
                 cm.setDespiece(deo_codiE.getValorInt());
                 cm.buscarDespieces();
                 jf.gestor.ir(cm);
            }
        });
        opVerAgrup.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
//                if (opVerGrupo.isSelected())
//                    return;
                verDatos(dtCons, opVerGrupo.isSelected(), opVerAgrup.isSelected(),true);
            }
        });

        deo_numdesE.addFocusListener(new FocusAdapter()
        {

            @Override
            public void focusLost(FocusEvent e) {
                if (nav.getPulsado() == navegador.QUERY && deo_numdesE.getValorInt() > 0)
                {
                    opVerGrupo.setSelected(true);
                }
            }
        });
        deo_codiE.addFocusListener(new FocusAdapter()
        {

            @Override
            public void focusLost(FocusEvent e) {
                if (nav.getPulsado() == navegador.QUERY && deo_codiE.getValorInt() > 0)
                {
                    opVerGrupo.setSelected(false);
                }
            }
        });
        
        deo_desnueE.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!deo_desnueE.isEnabled() || nav.pulsado == navegador.QUERY)
                    return;
               
                try
                {
                    if (utdesp == null)
                        return;
                    utdesp.setDespieceNuestro(deo_desnueE.isSelected());
                } catch (Exception k)
                {
                    Error("Error al poner el Despiece como nuestro", k);
                }

            }
        });
        opSimular.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e) {
                opSimular.setEnabled(false);
                Baceptar.setEnabled(false);
            }
        });

        deo_lotnueE.addActionListener(new ActionListener()
        {

            public void actionPerformed(ActionEvent e) {
                try
                {
                    if (deo_lotnueE.isEnabled())
                    {
                        cambiaLote();
                    }
                } catch (Exception k)
                {
                    Error("Error al Cambiar Lote", k);
                }
            }
        });
  
        tid_codiE.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusGained(FocusEvent e) {
                inTidCodi = true;
                if (jtLin.isEnabled())
                {
                    int col;
                    jtLin.ponValores(jtLin.getSelectedRow());
                    jtLin.salirGrid();

                    if ((col = cambiaLineajtLin(jtLin.getSelectedRow())) >= 0)
                    {
                        jtLin.requestFocusLater(jtLin.getSelectedRow(), col);
                        return;
                    }
                }
                jtLin.setEnabled(false);
            }
            @Override
            public void focusLost(FocusEvent e) {
                inTidCodi = false;              
                llenaGridProSal(); // Si no ha metido ningun producto
                
            }
        });
        BcopLin.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (jtLin.isEnabled() == false || jtLin.isVacio() || jtLin.getSelectedRow() < 1)
                {
                    return;
                }
                jtLin.setValor(jtLin.getValString(jtLin.getSelectedRow() - 1, 0), 0);
                pro_codlE.setText(jtLin.getValString(jtLin.getSelectedRow() - 1, 0));
                jtLin.setValor(jtLin.getValString(jtLin.getSelectedRow() - 1, 1), 1);
                jtLin.requestFocusLater(jtLin.getSelectedRow(), 2);
            }
        });


        BsaltaCab.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusGained(FocusEvent e) {
                if (nav.pulsado != navegador.QUERY)
                {
                    irGridCab();
                }
            }
        });
        BsalLin.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusGained(FocusEvent e) {
                if (nav.pulsado != navegador.QUERY)
                {
                    irGridLin();
                }
            }
        });

        BirGrid.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (inTidCodi)
                {
                    irGridLin();
                    return;
                }
                if (!jtCab.isEnabled())
                    irGridCab();
                else
                    irGridLin();
            }
        });
     
        jtLin.addMouseListener(new MouseAdapter()
        {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (!jtLin.isEnabled())
                {
                    irGridLin();
                }
            }
        });
        jtCab.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!jtCab.isEnabled())
                    irGridCab(e.getClickCount());

            }
        });
        
        jtLin.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e) {
               if (!jtLin.isEnabled() && e.getClickCount()>1)
                   verMvtosLinDes();
            }
        });

        opImpEt.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (jtLin.isEnabled())
                {
                    jtLin.requestFocus(jtLin.getSelectedRow(), jtLin.getSelectedColumn());
                }
            }
        });
        jtDesp.tableView.addMouseListener(new MouseAdapter()
        {

            @Override
            public void mouseClicked(MouseEvent e) {

                if (jtDesp.isVacio() || nav.isEdicion() || !opVerGrupo.isSelected() || !jtDesp.isEnabled())
                {
                    return;
                }
                s = getStrSql(false) + " and deo_codi = " + jtDesp.getValorInt(0) + " and eje_nume = " + eje_numeE.getValorInt();
                try
                {
                    if (!dtAdd.select(s))
                    {
                        msgBox("¡¡ ERROR!! No encontrado Despiece");
                        return;
                    }
                    verDatos(dtAdd, false, false);
                } catch (SQLException k)
                {
                    Error("Error al ver detalle de grupo", k);
                }
            }
        });

//    deo_nulogeE.addFocusListener(new FocusAdapter() {
//      public void focusLost(FocusEvent e) {
//        if (e.getOppositeComponent()==BirGrid)
//          BirGrid.doClick();
//      }
//    });
        Bimpeti.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
               
                if (jtLin.getValorInt(jtLin.getSelectedRowDisab(),JTLIN_NUMIND) == 0)
                {
                    mensajeErr("Linea sin individuo. Imposible imprimir");
                    return;
                }             
                if (Bimpeti.getValor(e.getActionCommand()).equals("U"))
                    tipoEtiqueta=ultCodigoEtiqueta;
                else
                    tipoEtiqueta=Integer.parseInt( Bimpeti.getValor(e.getActionCommand()));
                ultCodigoEtiqueta = tipoEtiqueta;
                try {
                    if (!jtLin.isEnabled())
                    {
                        pro_codlE.setText(jtLin.getValString(jtLin.getSelectedRowDisab(),JTLIN_PROCODI));
                        pro_codlE.getNombArt(jtLin.getValString(jtLin.getSelectedRowDisab(),JTLIN_PROCODI),EU.em_cod);
                    }
                    ponFechas(pro_codlE.getDiasCaducidad(),jtLin.getValDate(jtLin.getSelectedRowDisab(),JTLIN_FECCAD));
                }catch (ParseException | SQLException ex ) {
                       Error("Error al poner fechas produccion",ex);
                       return;
                }
                imprEtiq(jtLin.isEnabled()?jtLin.getSelectedRow():
                    jtLin.getSelectedRowDisab(), tipoEtiqueta);
                if (jtLin.isEnabled())
                    jtLin.requestFocusLater(jtLin.getSelectedRowDisab(), jtLin.getSelectedColumn());
            }
        });
        BImpEtiInt.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {                
                try {
                   if (!jtLin.isEnabled())
                   {
                        pro_codlE.setText(jtLin.getValString(jtLin.getSelectedRowDisab(),JTLIN_PROCODI));
                        pro_codlE.getNombArt(jtLin.getValString(jtLin.getSelectedRowDisab(),JTLIN_PROCODI));
                   }
                   ponFechas(pro_codlE.getDiasCaducidad(),jtLin.getValDate(jtLin.getSelectedRowDisab(),JTLIN_FECCAD));
                } catch (ParseException | SQLException ex) {
                       Error("Error al poner fechas produccion",ex);
                       return;
                }

                if (jtLin.getValorInt(jtLin.getSelectedRowDisab(),JTLIN_NUMIND) == 0)
                {
                    mensajeErr("Linea sin individuo. Imposible imprimir");
                    return;
                }     
                imprEtiqInt();
            }
         });
        jtHist.tableView.getSelectionModel().addListSelectionListener(new
        ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                if (e.getValueIsAdjusting() || !jtHist.isEnabled()) // && e.getFirstIndex() == e.getLastIndex())
                    return;
                cambiaLineaHist(jtHist.getValorInt(3));
            }
        });

    }
    private void cambiaLineaHist(int rowid)
    {
        hisRowid=rowid;
        if (hisRowid==0)
        {
            verDatos(dtCons);
            return;
        }
        tablaCab="deorcahis";
        tablaEnt="deorlihis";
        tablaSal="desfinhis";
        vistaDesp="v_hisdespori";
        condHist=" and his_rowid = "+hisRowid;
        try {
            s="SELECT * FROM "+ tablaCab +" where eje_nume = " + eje_numeE.getValorInt()
                        + " and deo_codi = " +deo_codiE.getValorInt()
                        + " and his_rowid = "+hisRowid;
            dtStat.select(s);
            verDatos(dtStat,false,opVerAgrup.isSelected(),true);
        } catch (SQLException k)
        {
            Error("Error al ver datos de historicos",k);
        }
     }
//    /**
//     * NO USADA!!. Obsoleta
//     */
//    private void autoDesp() {
//        try
//        {
//            ArrayList<DesorilinId> desorlinId = new ArrayList();
//            s = "select l.eje_nume,l.deo_codi,l.del_numlin " + lastSqlWhereLin;
//            if (jtCab.getValorInt(jtCab.getSelectedRowDisab(), JTCAB_EJELOT) != 0)
//            {
//                s += " and  l.del_numlin = "
//                    + jtCab.getValorInt(jtCab.getSelectedRowDisab(), JTCAB_NL);
//            } else
//            {
//                    s += " and  l.pro_codi = "
//                    + jtCab.getValorInt(jtCab.getSelectedRowDisab(), JTCAB_PROCODI);
//            }
//            if (!dtStat.select(s))
//            {
//                msgBox("No encontradas lineas de despiece");
//                return;
//            }
//            do
//            {
//                desorlinId.add(new DesorilinId(dtStat.getInt("eje_nume"),
//                    dtStat.getInt("deo_codi"), dtStat.getInt("del_numlin")));
//            } while (dtStat.next());
//            if (autoDesp == null)
//            {
//                autoDesp = new AutoDesp(this)
//                {
//
//                    @Override
//                    public void matar() {
//                        salirAutoDesp();
//                    }
//                };
//                autoDesp.setLocation(25, 25);
//                autoDesp.iniciarVentana();
//                vl.add(autoDesp);
//                autoDesp.setTidCodi(MantTipDesp.AUTO_DESPIECE);
//            }
//            autoDesp.setVisible(true);
//            autoDesp.muerto = false;
//            autoDesp.statusBar.setEnabled(true);
//            autoDesp.statusBar.Bsalir.setEnabled(true);
//            autoDesp.setClosed(false);
//
//            this.setEnabled(false);
//            this.setFoco(autoDesp);
//
//            autoDesp.reset(jtCab.getValorInt(jtCab.getSelectedRowDisab(), JTCAB_PROCODI), desorlinId);
//        } catch (Exception j)
//        {
//            Error("Error al Buscar Linea de despiece para autodespieces", j);
//            this.setEnabled(true);
//        }
//    }
//
//    void salirAutoDesp() {
//        autoDesp.setVisible(false);
//        this.setEnabled(true);
//        this.toFront();
//        try
//        {
//            this.setSelected(true);
//        } catch (Exception k)
//        {
//        }
//        this.setFoco(null);
//        if (autoDesp.getDeoCodiNew() != 0)
//        {
//            verDatos(dtCons);
//        }
//    }

    void irGridCab() {
        irGridCab(1);
    }

    void irGridCab(int numClicks) {

        if (nav.pulsado != navegador.EDIT && nav.pulsado != navegador.ADDNEW)
        {
            try
            {
                if (numClicks < 2)
                    return;
                if (utdesp == null)
                    utdesp = new utildesp();
                int row=jtCab.getSelectedRowDisab();
                utdesp.busDatInd(jtCab.getValString(row, JTCAB_SERLOT),
                    jtCab.getValorInt(row,JTCAB_PROCODI),
                    EU.em_cod, jtCab.getValorInt(row,JTCAB_EJELOT),
                    jtCab.getValorInt(row,JTCAB_NUMLOT),
                    jtCab.getValorInt(row,JTCAB_NUMIND), dtCon1, dtStat, EU);
                if (utdesp.getNumeroDespiece() == 0)
                {
                    if (utdesp.getNumeroAlbaranCompra() != 0)
                    {
                        ejecutable prog;
                        if (isEmpPlanta)
                        {
                           if ((prog = jf.gestor.getProceso(MantAlbComPlanta.getNombreClase())) == null)
                              return;
                        }
                        else
                        {
                           if ((prog = jf.gestor.getProceso(MantAlbComCarne.getNombreClase())) == null)
                              return;
                        }
                        
                        MantAlbCom cm = (MantAlbCom) prog;
                        if (cm.inTransation())
                        {
                            msgBox("Mantenimiento Compras ocupado. No se puede realizar la busqueda");
                            return;
                        }
                        cm.PADQuery();
                        cm.setEjeNume(utdesp.getEjercicioAlbaranCompra());
                        cm.setAccCodi("" + utdesp.getNumeroAlbaranCompra());
                        cm.ej_query();
                        jf.gestor.ir(cm);
                        return;
                    }
                    msgBox("No encontrado despiece de origen");
                    return;
                }
                PADQuery();
                setEjeNume(jtCab.getValorInt(JTCAB_EJELOT));
                setDeoCodi("" + utdesp.getNumeroDespiece());
                ej_query();
            } catch (SQLException ex)
            {
                Error("Error al buscar despiece origen", ex);
            }

            return;
        }
        
        if (jtLin.isEnabled())
        {
//      if (nav.pulsado==navegador.EDIT && deo_blockE.getValor().equals("S"))
//      {
//          mensajeErr("Imposible ir a cabecera despiece en edicion de un despiece abierto");
//          jtLin.requestFocusInicioLater();
//           return; // No permito ir a cabecera en edicion si es desp. abierto
//      }
            try
            {
                if (swMantLote && getNuLiDes(true) > 0 && nav.pulsado==navegador.EDIT)
                {
                    mensajeErr("Imposible volver a cabecera si se esta manteniendo el lote");
                    jtLin.requestFocusInicioLater();
                    return; // No permito ir a cabecera en edicion si es desp. abierto
                }
            } catch (SQLException k)
            {
                Error("Error al contar el numero de lineas finales", k);
                return;
            }

            if (cambiaLineajtLin(jtLin.getSelectedRow()) >= 0)
            {
                jtLin.requestFocusSelectedLater();
                return;
            }
        }
        
        jtCab.setEnabled(true);
        
        if (uniFinE.getValorInt() == 0)
            Baceptar.setEnabled(false);
        if (swMantLote)
        { // Vuelvo a poner como lote uno nuevo
            ponLoteNuevo();
            swMantLote = false;
        }
        jtLin.setEnabled(false);

        jtCab.requestFocusInicio();
        jtCab.afterCambiaLinea();
    }

    void resetCambioLineaCab() 
    {
        boolean swEditable=true;
        if (jtCab.getSelectedRow()==0 && existLineasSalida())
             swEditable=false;
        pro_numindE.setEditable(swEditable);
        pro_loteE.setEditable(swEditable);
        pro_codiE.setEditable(swEditable);
        deo_ejelotE.setEditable(swEditable);
        deo_serlotE.setEditable(swEditable);       
        pro_numindE.resetCambio();
        pro_loteE.resetCambio();
        pro_codiE.resetCambio();
        deo_ejelotE.resetCambio();
        deo_serlotE.resetCambio();
        deo_kilosE.resetCambio();
    }

    /**
     * Busca peso de Linea de Cabecera
     * @return false si hubo error al buscar stockPartidas
     */
    private boolean buscaPesoLin() {        
        try {
            stkPartid=buscaPeso();
            if (stkPartid.hasError())
                return true;
            if (stkPartid.getKilos()<=0)
                return true;
        } catch (Exception k)
        {
            Error("Error al buscar Peso", k);
            return false;
        }
        return true;
    }
    /*
     * Devuelve clase StkPartid con el peso del individuo de origen activo
     * Escribe mensaje de error y pone variable swArtBloq si procede
     */

    StkPartid buscaPeso() throws Exception {
       
        stkPartid = utildesp.buscaPeso(dtCon1, deo_ejelotE.getValorInt(), EU.em_cod,
            deo_serlotE.getText(), pro_loteE.getValorInt(),
            pro_numindE.getValorInt(), pro_codiE.getValorInt(), deo_almoriE.getValorInt());
        return stkPartid;
    }

    /**
     * Llena grid con los productos del tipo de despiece (si existe)
     */
    void llenaGridProSal() 
    {
        try 
        {
            if (tid_codiE.getValorDec() == MantTipDesp.CONGELADO_DESPIECE)
            { // Es a congelar
                if (Formatear.comparaFechas(deo_feccadE.getDate(), deo_fecproE.getDate()) < 60)
                {
                    Date fecCadCong = Formatear.sumaDiasDate(deo_fecproE.getDate(), MantTipDesp.DIAS_CONGELADO);
                    deo_feccadE.setDate(fecCadCong);
                    def_feccadE.setDate(fecCadCong);
                    int nRow = jtLin.getRowCount();
                    for (int n = 0; n < nRow; n++)
                    {
                        if (jtLin.getValorInt(n, JTLIN_NUMIND) == 0)
                            jtLin.setValor(fecCadCong, n, JTLIN_FECCAD);
                    }
                }
            }
            boolean swVacio = true;
            for (int n = 0; n < jtLin.getRowCount(); n++)
            {
                if (jtLin.getValorDec(n, JTLIN_KILOS) != 0)
                    swVacio = false;
            }
            if (!swVacio)
                return; // Tiene individuos de salida. No hago nada
            if (tidCodAnt!=0 && tidCodAnt==tid_codiE.getValorInt())
            {             
                llenaGridSalDefecto(dtFinAnt);
                return;
            }
            if (tid_codiE.getValorDec() == MantTipDesp.LIBRE_DESPIECE || !cargaPSC.isSelected())
                return;
            s = DespVenta.getSqlProdSal(tid_codiE.getValorInt(), jtCab.getValorInt(0, JTCAB_PROCODI));   
            if (!dtCon1.select(s))        
                return;
            ArrayList<Integer> dtArt=new ArrayList();
            do
            {
                dtArt.add(dtCon1.getInt("pro_codi"));
            } while (dtCon1.next());
            llenaGridSalDefecto(dtArt);
        } catch (ParseException | SQLException ex)
        {
            Error("Error al buscar Desp. Lineas", ex);
        }
    }
    void llenaGridSalDefecto(ArrayList<Integer> dtList) throws SQLException
    {
         boolean enab = jtLin.isEnabled();
            jtLin.setEnabled(false);
            jtLin.removeAllDatos();
           
            int nRow=dtList.size();
            for (int n=0;n<nRow;n++)
            {
                ArrayList v = new ArrayList();
                v.add(dtList.get(n));
                v.add(pro_codlE.getNombArt(dtList.get(n)));
                v.add("0"); // kgs
                v.add("1"); //Unid.
                v.add("0");  // Costo
                v.add(deo_feccadE.getText());  // Fecha Cad.
                v.add("0"); // N Ind.
                v.add("0"); // N. Orden
                v.add("1"); // N. Piezas por cajas
                v.add("0");
                v.add("");
                jtLin.addLinea(v);
            }
            jtLin.setEnabled(enab);
    }
    boolean checkCab() throws Exception {
        return checkCab(true);
    }

    boolean checkCab(boolean reset) throws Exception {
        jtCab.actualizarGrid();
        actKilos(JTCAB_GRID);

        if (eje_numeE.getValorInt() == 0)
        {
            mensajeErr("Introduzca Ejercicio");
            eje_numeE.requestFocus();
            return false;
        }

        if (deo_fechaE.isNull() || deo_fechaE.getError())
        {
            mensajeErr("Introduzca Fecha de Despiece");
            deo_fechaE.requestFocusLater();
            return false;
        }
        if (Formatear.comparaFechas(deo_fechaE.getDate(), Formatear.getDateAct()) > 3)
        {
            mensajeErr("Fecha Despiece es superior en más de tres dias a la actual");
            deo_fechaE.requestFocusLater();
            return false;
        }
        s = pdejerci.checkFecha(dtStat, eje_numeE.getValorInt(), EU.em_cod, deo_fechaE.getDate());
        if (s != null)
        {
            mensajeErr(s);
            deo_fechaE.requestFocusLater();
            return false;
        }
        if (!deo_almdesE.controla(true))
        {
            mensajeErr("Introduzca Almacen Destino");
            return false;
        }
        if (!deo_almoriE.controla(true))
        {
            mensajeErr("Introduzca Almacen Origen");
            return false;
        }
        if (deo_feccadE.getError())
        {
            mensajeErr("Fecha CADUCIDAD NO es Valida");
            deo_feccadE.requestFocusLater();
            return false;
        }
        if (deo_feccadE.isNull())
        {
            if (utdesp.getFechaCaducidad() != null)
            {
                deo_feccadE.setDate(utdesp.getFechaCaducidad());
                ultFecCaduc=utdesp.getFechaCaducidad();
                deo_feccadE.resetCambio();
            }
        }
     
        if (deo_fecproE.isNull())
        {
            if (utdesp.getFechaProduccion() != null)
                deo_fecproE.setDate(utdesp.getFechaProduccion());
        }
        if (nav.pulsado==navegador.ADDNEW && Formatear.comparaFechas(deo_fecproE.getDate(),Formatear.getDateAct()) > 0)
        {
            msgBox("Fecha Produccion NO puede ser superior a la actual");
            deo_fecproE.requestFocus();
            return false;
        }
        if (deo_fecsacE.isNull())
        {
            if (utdesp.getFechaSacrificio()!= null)
                deo_fecsacE.setDate(utdesp.getFechaSacrificio());
        }
        
        if (deo_feccadE.isNull() && reset)
        {
            mensajeErr("Introduzca Fecha de Caducidad");
            deo_feccadE.requestFocusLater();
            return false;
        }
        
        if (Formatear.comparaFechas(deo_feccadE.getDate(), deo_fecproE.getDate()) < 1)
        {
            msgBox("Fecha Despiece deberia ser superior en más de siete dias a la producion");
///            deo_feccadE.requestFocusLater();
//            return false;
        }
      
        if (!deo_fecsacE.isNull() )
        {
            if (Formatear.comparaFechas(deo_fecsacE.getDate(), deo_fecproE.getDate())>0)
                msgBox("Fecha Sacrificio NO puede ser superior a la de produccion");              
        }
        if (Formatear.comparaFechas(deo_fecproE.getDate(),deo_fecsacE.getDate() ) < 0)
            msgBox("Fecha Produccion NO puede ser inferior a Sacrificio");

        if ((prv_codiE.isNull() || !prv_codiE.controla(true)) && reset)
        {
            mensajeErr("Proveedor del Despiece NO es Valido");
            return false;
        }
        if (reset)
            resetCambioLineaCab();
 
        return true;
    }

    /**
     * Devuelve el numero de lineas de cabecera activas
     * @return
     */
    private int getNuLiCab() {
        int nLin = 0;
        for (int n = 0; n < jtCab.getRowCount(); n++)
        {
            if (jtCab.getValorDec(n, JTCAB_PROCODI) != 0
                    && jtCab.getValorDec(n, JTCAB_NUMLOT) != 0
                    && jtCab.getValorDec(n, JTCAB_KILOS) != 0
                    && jtCab.getValorInt(n,JTCAB_NUMIND)!=0)
                nLin++;
        }
        return nLin;
    }

    /**
     * Devuelve el numero de lineas de salidas activas
     * @param swSoloVend contar solo los productos vendibles
     *
     * @return
     */
    private int getNuLiDes(boolean swSoloVend) throws SQLException {
        int nLin = 0;
        for (int n = 0; n < jtLin.getRowCount(); n++)
        {
            if (jtLin.getValorDec(n, 2) != 0)
            {
                if (swSoloVend)
                {
                    pro_codiE.getNombArt(jtLin.getValorInt(n, 0));
                    if (!pro_codiE.isVendible())
                    {
                        continue;
                    }
                }
                nLin++;
            }

        }
        return nLin;
    }

    private void cambiaLote() throws SQLException {
//    if (getNuLiDes(true)!=0)
//        opMantLote.setValor(opMantLote.getValor().equals("N")?"M":"N"); // Ni caso.
        if (deo_lotnueE.getValor().equals("-1"))
        { // Poner a Mantener
            ponLoteNuevo();
        } else
        {
            ponLoteOri();
        }
    }

    /**
     * Comprueba si todas las lineas de cabecera son del mismo lote
     *
     * @return boolean true si son del mismo lote
     */
    boolean esLoteIgual() {
        int nRow = jtCab.getRowCount();

        int ejeNume = 0;
        String serie = "";
        int nuloge = 0;

        for (int n = 0; n < nRow; n++)
        {
            if (jtCab.getValorInt(n, JTCAB_PROCODI) == 0)
            {
                continue;
            }
            if (ejeNume == 0)
            {
                ejeNume = jtCab.getValorInt(n, JTCAB_EJELOT);
                serie = jtCab.getValString(n, JTCAB_SERLOT);
                nuloge = jtCab.getValorInt(n, JTCAB_NUMLOT);
            } else
            {
                if (ejeNume != jtCab.getValorInt(n, JTCAB_EJELOT)
                    || !serie.equals(jtCab.getValString(n, JTCAB_SERLOT)) || nuloge != jtCab.getValorInt(n, JTCAB_NUMLOT))
                {
                    return false;
                }
            }
        }
        return true;
    }

    void irGridLin() {
        if (nav.pulsado != navegador.EDIT && nav.pulsado != navegador.ADDNEW)
            return;
        if (jtLin.isEnabled())
        {
            jtLin.requestFocusInicio();
            return;
        }
        try
        {
            if (!checkGridCab())
                return;
            if (!genDatEtiq())
                return;
           
            if (!checkCab())
                return;
      
            resetCambioLineaCab();
            if (nav.pulsado==navegador.ADDNEW && deo_lotnueE.getValorInt()!=FORZAR_LOTE)
            {
                if (!AGRUPALOTE)
                {
                    boolean isDespAgru = MantTipDesp.isDespieceAgrup(tid_codiE.getValorInt(), dtStat);
                    deo_lotnueE.setEnabled(isDespAgru);
                    deo_lotnueE.setValor(isDespAgru ? "0" : "-1");
                } else
                {
                    boolean isDespAgru = MantTipDesp.isDespieceAgrup(tid_codiE.getValorInt(), dtStat);
                    if (isDespAgru)
                    {
                        deo_lotnueE.setEnabled(isDespAgru);
                        deo_lotnueE.setValor("0");
                    }
                    else
                    { // Compruebo si son los mismos lotes en cabecera.
                       int numLote=0;
                       isDespAgru=true;
                       for (int n=0;n< jtCab.getRowCount();n++)
                       {
                           if (jtCab.getValorInt(n,JTCAB_NUMLOT)!=0)
                           {
                               if (numLote==0)
                                   numLote=jtCab.getValorInt(n,JTCAB_NUMLOT);
                               if (numLote!=jtCab.getValorInt(n,JTCAB_NUMLOT))
                               {
                                       isDespAgru=false;
                                       break;
                               }
                           }
                       }
                       int lotNue=deo_lotnueE.getValorInt();                       
                       deo_lotnueE.setEnabled(isDespAgru);
                       deo_lotnueE.setValor(isDespAgru ? "0" : "-1");
                       if (lotNue!=deo_lotnueE.getValorInt())
                           cambiaLote();
                    }
                }
            }
            if (proCodTD == 0 || proCodTD != jtCab.getValorInt(0, JTCAB_PROCODI))
            {
                proCodTD = jtCab.getValorInt(0, JTCAB_PROCODI);

                tid_codiE.resetTexto();
                tid_codiE.setEnabled(true);
                if (AGRUPALOTE)
                    deo_lotnueE.setEnabled(true);

                cambiaLote();
                jtCab.setEnabled(false);
                if (jtCab.getValorInt(JTCAB_PROCODI) == 0)
                {
                    jtCab.removeLinea();
                }
                tid_codiE.setArticulos(jtCab.getValorColumna(JTCAB_PROCODI));
                if (!CHECKTIDCODI && !AVISATIDCODI)
                    tid_codiE.clearArticulos();
                tid_codiE.setVerSoloActivo(nav.getPulsado()==navegador.ADDNEW);
                tid_codiE.releer();
   
                tid_codiE.resetTexto();
                
                if (!opRepet.isSelected() || tidCodAnt==0)
                    tid_codiE.setValorInt(MantTipDesp.AUTO_DESPIECE);
                else
                {
                    tid_codiE.setValorInt(tidCodAnt);
                    if (!tid_codiE.controla(false))
                         tid_codiE.setValorInt(MantTipDesp.AUTO_DESPIECE);
                }
                tid_codiE.requestFocusLater();

                return;
            }
            if (!tid_codiE.controla())
            {
                mensajeErr(tid_codiE.getMsgError());
                if (!tid_codiE.isEnabled())
                    jtCab.requestFocusInicioLater();
                return;
            }
            if (desorca!=null)
            {
                if (tid_codiE.getValorInt()!=desorca.getTidCodi())
                { // Actualizo el Tipo de despiece.
                    desorca.setTidCodi(tid_codiE.getValorInt());
                    desorca.updateTidCodi(dtAdd);
                    dtAdd.commit();
                    if (tid_codiE.getValorDec() == MantTipDesp.CONGELADO_DESPIECE)
                        deo_feccadE.setDate(Formatear.sumaDiasDate(deo_fecproE.getDate(), MantTipDesp.DIAS_CONGELADO));
                }
            }

            activar(false, 1);
            Pcabe.setEnabled(false);
            POtros.setEnabled(false);
            Plotgen.setEnabled(true);
            //    opMantLote.setEnabled(true);
            if (!opSimular.isSelected())
                Baceptar.setEnabled(true);
            if (nav.pulsado == navegador.EDIT)
                Bcancelar.setEnabled(false);
            Bimpeti.setEnabled(true);
            jtCab.setEnabled(false);
            llenaGridProSal();
            pro_codiE.setValorInt(jtCab.getValorInt(0, JTCAB_PROCODI));
            pro_codlE.setText(jtLin.getValString(0, JTLIN_PROCODI));
            pro_codlE.resetCambio();
            jtLin.setDefaultValor(5, deo_feccadE.getText());
            def_feccadE.setText(deo_feccadE.getText());
            if (jtLin.isVacio())
                jtLin.removeAllDatos();
            
            def_feccadE.resetCambio();
            jtLin.setEnabled(true);
//            jtLin.requestFocusInicio();
            inTidCodi=false;
            jtLin.requestFocusInicioLater();

            def_kilosE.setValorDec(jtLin.getValorDec(0, JTLIN_KILOS));
            def_kilosE.resetCambio();
            proCodAnt = jtLin.getValorInt(0, JTLIN_PROCODI);
            defKilAnt = jtLin.getValorDec(0, JTLIN_KILOS);
        } catch (Exception k)
        {
            Error("Error en IrGrid", k);
        }
    }

    String getStrSql() {
        return getStrSql(opVerGrupo.isSelected());
    }

    String getStrSql(boolean verGrupo) {
        if (verGrupo)
        {
            return "select * from grupdesp WHERE grd_nume > 99 "
                + " and exists ("
                + " SELECT * FROM v_despori WHERE emp_codi = grupdesp.emp_codi "
                + " and eje_nume = grupdesp.eje_nume "
                + " and deo_numdes = grupdesp.grd_nume  ";
        } else
        {
            return "select * from desporig where 1=1 ";
        }
    }

    String getCondWhere() {
        return (Integer.parseInt(Formatear.getFechaAct("MM")) > 2 ? " and eje_nume = " + EU.ejercicio : "")
            + " and deo_fecha >= current_date - 60";
    }

    String getOrderQuery() {
        if (!opVerGrupo.isSelected())
        {
            return " order by eje_nume,deo_codi";
        } else
        {
            return ") order by eje_nume,grd_nume";
        }
    }
    public void setDeoCodi(int deoCodi) {
        deo_codiE.setValorInt(deoCodi);
        opVerGrupo.setSelected(false);
    }
    public void setDeoCodi(String deoCodi) {
        deo_codiE.setText(deoCodi);
        opVerGrupo.setSelected(false);
    }
    public void setEjeNume(String ejeNume)
    {
        eje_numeE.setText(ejeNume);
    }
    public void setEjeNume(int ejeNume)
    {
        eje_numeE.setValorInt(ejeNume);
    }
    public boolean inTransation()
    {
        return (nav.getPulsado()==navegador.ADDNEW || nav.getPulsado()==navegador.EDIT || nav.getPulsado()==navegador.DELETE);
    }
    public void setGrupo(int grdNume) {
        deo_numdesE.setValorInt(grdNume);
        opVerGrupo.setSelected(true);
    }

    public static String getNombreClase() {
        return "gnu.chu.anjelica.despiece.MantDesp";
    }

    @Override
    public void PADQuery() {
        mensajeErr("");
        Pcabe.setEnabled(true);
        deo_almoriE.setEnabled(true);
        deo_almdesE.setEnabled(true);
        POtros.setEnabled(true);
        Plotgen.setEnabled(true);
//    Ptotal.setEnabled(true);
        usu_nombE.setEnabled(true);
        deo_codiE.setEnabled(true);
        deo_selogeE.setEnabled(true);
        deo_nulogeE.setEnabled(true);
        tid_codiE.setEnabled(true);
        cli_codiE.setEnabled(true);
        Pcabe.setQuery(true);
        Plotgen.setQuery(true);
        tid_codiE.setQuery(true);
        deo_numdesE.setEnabled(true);
//    Ptotal.setQuery(true);
        Baceptar.setEnabled(true);
        Bcancelar.setEnabled(true);
        jtHist.setEnabled(false);
        Pcabe.resetTexto();
        if (Integer.parseInt(Formatear.getFechaAct("MM"))<2)
            eje_numeE.setText(">="+(EU.ejercicio-1));
        else
            eje_numeE.setValorDec(EU.ejercicio);
        eje_numeE.setEnabled(true);
        Plotgen.resetTexto();
        tid_codiE.clearArticulos();
        try
        {
            tid_codiE.setVerSoloActivo(false);
            tid_codiE.releer();
            tid_codiE.resetTexto();
        } catch (SQLException k)
        {
            Error("Error al leer tipos de despieces", k);
        }
//    Ptotal.resetTexto();
        verGrupo = opVerGrupo.isSelected();
        opVerGrupo.setEnabled(true);
        mensaje("Introduzca condiciones de busqueda ...");
        deo_codiE.requestFocus();
    }

    @Override
    public void ej_query() {
        new miThread("")
        {

            @Override
            public void run() {
                mensaje("Espere, por favor.. buscando Datos");
                ej_query1();
                mensaje("");
            }
        };
    }

    @Override
    public void ej_query1() {
        if (Pcabe.getErrorConf() != null)
        {
            mensajeErr("Error en condiciones de busqueda");
            Pcabe.getErrorConf().requestFocus();
            return;
        }
//        this.setEnabled(false);
        nav.pulsado = navegador.NINGUNO;

        ArrayList v = new ArrayList();
//        v.add(emp_codiE.getStrQuery());
        v.add(deo_almdesE.getStrQuery());
        v.add(deo_almoriE.getStrQuery());
        v.add(eje_numeE.getStrQuery());
        v.add(deo_fechaE.getStrQuery());
        v.add(deo_codiE.getStrQuery());
        v.add(tid_codiE.getStrQuery());
        v.add(deo_numdesE.getStrQuery());
        v.add(pro_numindE.getStrQuery());
        v.add(deo_ejelotE.getStrQuery());
        v.add(deo_serlotE.getStrQuery());
        v.add(deo_selogeE.getStrQuery());
        v.add(deo_nulogeE.getStrQuery());
        v.add(usu_nombE.getStrQuery());
        v.add(cli_codiE.getStrQuery());
        v.add(deo_incvalE.getStrQuery());
        s = creaWhere(getStrSql(), v, false);
        s += getOrderQuery();
//       debug("Query: "+s);

        Pcabe.setQuery(false);
        Plotgen.setQuery(false);
        tid_codiE.setQuery(false);
//       Ptotal.setQuery(false);
        try
        {
            boolean res = dtCons.select(s);
            mensaje("");

            if (!res)
            {
                msgBox("No encontrados Despieces para estos criterios");
                opVerGrupo.setSelected(verGrupo);
            } else
            {
                strSql = s;
                mensajeErr("Nuevos registros selecionados");
            }
            rgSelect();
            verDatos(dtCons, opVerGrupo.isSelected(), true);
            activaTodo();
//            this.setEnabled(true);
        } catch (Exception ex)
        {
            fatalError("Error al buscar Despieces: ", ex);
        }
    }

    @Override
    public void canc_query() {
        opVerGrupo.setSelected(verGrupo);
        Pcabe.setQuery(false);
        Plotgen.setQuery(false);
        tid_codiE.setQuery(false);
        verDatos(dtCons);
        mensaje("");
        mensajeErr("Consulta ... CANCELADA");
        activaTodo();
        nav.pulsado = navegador.NINGUNO;
    }
    /**
     * Recupera un despiece a un estado anterior.
     */
    private void recuperaHist() throws Exception
    {
        int hisRowidOld=hisRowid;
        verDatos(dtCons); // Muestro el registro original.
        s=checkBorrar();
        if (s!=null)
        {
               msgBox(s);
               activaTodo();
               return;
        }
       
        copiaRegistro("Vuelta atras en Historico");
        borraRegistro(true);
        
        cambiaLineaHist(hisRowidOld); // Vuelvo a mostrar el registro de historico
         if (deo_nulogeE.isNull() )
        {
            msgBox("Numero de Lote esta a cero. Creo un lote nuevo");
            ponLoteNuevo();
        }
        guardaCabOrig(deo_codiE.getValorInt());
        int nRow=jtCab.getRowCount();
        for (int n=0;n<nRow;n++)
        {
            guardaLinOrig(jtCab.getValorInt(n,JTCAB_PROCODI),
                jtCab.getValorInt(n, JTCAB_EJELOT), jtCab.getValString(n, JTCAB_SERLOT),
                jtCab.getValorInt(n,JTCAB_NUMLOT),
                jtCab.getValorInt(n,JTCAB_NUMIND), 
                jtCab.getValorDec(n,JTCAB_KILOS), jtCab.getValorDec(n,JTCAB_PRECIO),
                dtAdd);
        }
        nRow=jtLin.getRowCount();
        for (int n=0;n<nRow;n++)
        {
             guardaLinDespFin(deo_ejlogeE.getValorInt(), EU.em_cod,
                    deo_selogeE.getText(), deo_nulogeE.getValorInt(), 
                    jtLin.getValorInt(n,JTLIN_NUMIND),
                    jtLin.getValorInt(n,JTLIN_PROCODI),
                    jtLin.getValorDec(n,JTLIN_KILOS),
                    jtLin.getValorInt(n,JTLIN_UNID),
                    jtLin.getValorInt(n,JTLIN_NUMCAJ),
                    jtLin.getValDate(n,JTLIN_FECCAD),
                    jtLin.getValorInt(n,JTLIN_ORDEN)
                    );
//             insStkPart(jtLin.getValorInt(n,JTLIN_PROCODI), deo_ejlogeE.getValorInt(),
//                    EU.em_cod,
//                    deo_selogeE.getText(), deo_nulogeE.getValorInt(),
//                    jtLin.getValorInt(n,JTLIN_NUMIND),
//                    jtLin.getValorDec(n,JTLIN_KILOS));
        }
        dtAdd.commit();
        mensajeErr("Datos restaurados ");
        mensaje("");
        activaTodo();
    }
    @Override
    public void PADEdit() {
        
        if (opVerGrupo.isSelected())
        {
            msgBox("No puede editar si se estan viendo GRUPOS");
            activaTodo();
            return;
        }
        try {
            
            if (hisRowid>0)
            {                 
                int ret=mensajes.mensajeYesNo("Esta viendo el historico del despiece."+
                    " Si edita este despiece volvera al estado anterior. ¿ Seguro que desea continuar ? ", this);
                if (ret!=mensajes.YES)
                {
                    msgBox("Cancelada edicion de datos de historico");
                    nav.pulsado=navegador.NINGUNO;
                    activaTodo();
                    return;
                }
                recuperaHist();
                return;
            }
            if (! verDatos(dtCons,false,false,true))
            {
                nav.pulsado = navegador.NINGUNO;
                activaTodo();
                return;
            }
            swErrCab=false;
            swMantLote = false;
            mensajeErr("");
            if (deo_blockE.getValor().equals("B"))
            {
                mensajeErr("REGISTRO BLOQUEADO");
                nav.pulsado = navegador.NINGUNO;
                activaTodo();
                return;
            }
            if (deo_blockE.getValor().equals("S") && deo_serlotE.getText().equals(MantDesp.SERIE))
            { // Abierto y del tactil.
                mensajeErr("REGISTRO BLOQUEADO ... CIERRELO EN EL TACTIL PRIMERO");
                nav.pulsado = navegador.NINGUNO;
                activaTodo();
                return;
            }
            if (swTienePrec)
            {
                if (!MODPRECIO)
                {
                    mensajeErr("DESPIECE VALORADO ... IMPOSIBLE MODIFICAR");
                    nav.pulsado = navegador.NINGUNO;
                    activaTodo();
                    return;
                }
                int ret = mensajes.mensajeYesNo("Despiece ya ha sido valorado. Si lo edita se pondran los costos a cero.\n"
                    + " Seguro que desea editarlo ?");
                if (ret != mensajes.YES)
                {
                    nav.pulsado = navegador.NINGUNO;
                    activaTodo();
                    return;
                }
                if (jf != null)
                {
                    jf.ht.clear();
                    jf.ht.put("%a", deo_codiE.getText());
                    jf.guardaMens("D6", jf.ht);
                }
            }
            if (!checkMvtos(eje_numeE.getValorInt(), deo_codiE.getValorInt()))
                return;
            if (deo_nulogeE.isNull() )
            {
                msgBox("Numero de Lote esta a cero. Creo un lote nuevo");
                ponLoteNuevo();
            }
            inTidCodi=false;
//            dtAdd.cerrar();
            if (!setBloqueo(dtAdd, TABLA_BLOCK, eje_numeE.getValorInt() + "|"
                + deo_codiE.getValorInt(),false))
            {
                nav.pulsado = navegador.NINGUNO;
                msgBox(msgBloqueo);
                activaTodo();
                return;
            }
            copiaRegistro("Modificado desde PAD Despieces");
            deoBlockAnt=deo_blockE.getValor();
            
// Pongo registro a estado 'Abierto'
            s="UPDATE desporig set deo_block = 'S'"
                + " WHERE eje_nume = " + eje_numeE.getValorInt()
                + " and deo_codi = " + deo_codiE.getValorInt();
            dtAdd.executeUpdate(s);
            dtAdd.commit();
            nav.pulsado = navegador.EDIT;
            mensaje("Editar Despiece");

            activar(true, navegador.EDIT);

            pro_codiE.resetCambio();
            deo_feccadE.resetCambio();
            proCodTD = 1;
            opMantFecha.setSelected(false);
            Baceptar.setEnabled(true);
            deo_desnueE.setPulsado(false);
            eje_numeE.setEnabled(false);
            deo_almoriE.setEnabled(false);
            deo_almdesE.setEnabled(false);
            if (uniFinE.getValorInt() > 0 )
            {
//                if ( tid_codiE.getValorInt()!=0 && ! P_ADMIN)
//                    tid_codiE.setEnabled(false);
                deo_lotnueE.setEnabled(false);
            }
            tid_codiE.setDeoCodi(deo_codiE.getText()); // Para q no se controle a si mismo.
            proCodTD = jtCab.getValorInt(0, JTCAB_PROCODI);
            genDatEtiq();
            swForzadoTraz=utdesp.isForzadaTrazab();
            ultFecCaduc=jtLin.getValDate(jtLin.getRowCount()-1,JTLIN_FECCAD);
//            proCodiB = 0;
            boolean isBlock = deo_blockE.getValor().equals("S");
//            if (isBlock && ActualStkPart.isBloqueado(dtStat, jtCab.getValorInt(0, JTCAB_PROCODI), jtCab.getValorInt(0, JTCAB_EJELOT),
//                EU.em_cod, jtCab.getValString(0, JTCAB_SERLOT),
//                jtCab.getValorInt(0, JTCAB_NUMLOT), jtCab.getValorInt(0, JTCAB_NUMIND),
//                deo_almoriE.getValorInt()))
//            {
//                proCodiB = jtCab.getValorInt(0, JTCAB_PROCODI);
//                ejeLoteB = jtCab.getValorInt(0, JTCAB_EJELOT);
//                serLoteB = jtCab.getValString(0, JTCAB_SERLOT);
//                numLoteB = jtCab.getValorInt(0, JTCAB_NUMLOT);
//                numIndiB = jtCab.getValorInt(0, JTCAB_NUMIND);
//                numLinB = jtCab.getValorInt(0, JTCAB_NL);
//                almOrigB = deo_almoriE.getValorInt();
//                deoKilosB = Formatear.redondea((jtCab.getValorDec(0, JTCAB_KILOS) - kgDifE.getValorDec()) * -1, 2);
//            }
            opSimular.setSelected(false);
            opSimular.setEnabled(false);
            deo_blockE.setValor("N");
            opAnularControl.setEnabled(isAdmin());
            POtros.setEnabled(isAdmin());
            if (isBlock)
                irGridLin();
            else
                irGridCab();
        } catch (Exception k)
        {
            Error("Error al bloquear registro", k);
        }
    }
    private boolean checkMvtos(int ejeNume, int deoCodi ) throws SQLException
    {               
        dtStat.select("select min(deo_tiempo) as tiempo from desorilin where eje_nume="+ejeNume+
            " and deo_codi = "+deoCodi);
        java.sql.Date feulmv=dtStat.getDate("tiempo");
        if (!checkMvto(pdalmace.getFechaInventario(deo_almoriE.getValorInt(), dtStat),feulmv))
            return false;
    
        dtStat.select("select min(def_tiempo) as tiempo from v_despfin where eje_nume="+ejeNume+
            " and deo_codi = "+deoCodi);
        feulmv=dtStat.getDate("tiempo");
        return checkMvto(pdalmace.getFechaInventario(deo_almdesE.getValorInt(), dtStat),feulmv);
    }
    private boolean checkMvto(java.sql.Date fecInv,java.sql.Date fecMvto)
    {
         if (fecMvto != null && Formatear.comparaFechas(fecInv, fecMvto)>= 0 )
        {
              msgBox("Despiece con Mvtos anteriores a Ult. Fecha Inventario. Imposible Editar/Borrar");
              nav.pulsado = navegador.NINGUNO;
              activaTodo();
              return false;
        }
        return true;
    }
    private void copiaRegistro(String coment) throws SQLException {
        utdesp.copiaDespieceNuevo(dtStat, dtAdd, coment, EU.usuario,
            eje_numeE.getValorInt(), deo_codiE.getValorInt());

//        dtAdd.commit();
    }

    @Override
    public boolean isMatable() {
        return true;
    }

    @Override
    public void activaTodo() {
        nav.pulsado = navegador.NINGUNO;
        super.activaTodo();
    }

    @Override
    public boolean checkEdit() {
        return checkAddNew();
    }

    @Override
    public void ej_edit1() {
       // int nRow, l;
        if (jtCab.isEnabled())
        {
            jtCab.salirGrid();
            swErrCab=cambiaLineajtCab(jtCab.getSelectedRow()) >= 0;
            if (swErrCab)
                return;
        } else
        {
            jtLin.salirGrid();
            if (cambiaLineajtLin(jtLin.getSelectedRow()) >= 0)
                return;
        }
        if (deo_blockE.getValor().equals("N"))
        {
             if (kgDifE.getValorDec() >= kgOrigE.getValorDec() * LIMDIF)
             {
                    msgBox("Kilos entrada diferentes a Kilos salida. IMPOSIBLE GUARDAR DESPIECE");
                    return;
             }
        }
       
//    debug("pddespie: en ej_edit1 ");
        inTidCodi = false;
        try
        {
//            dtAdd.cerrar();
            mensaje("Modificando Datos .. Espere, por favor");
            ordenarLineasCab();
            if (opMantFecha.isSelected())
            {// Mantener fecha de Despiece en mvto.
                dtAdd.executeUpdate("update v_despfin set def_tiempo= {d '"+deo_fechaE.getFecha("yyyy-MM-dd")+"'}"
                      + " where eje_nume = "+eje_numeE.getValorInt()
                      + " and deo_codi = "+deo_codiE.getValorInt());
            }
            // Ordenar las lineas
           
//            if (proCodiB != 0)
//            { // Antes tenia un producto bloqueado. Pongo kilos a 0 de producto entrada, si todavia sigue bloqueado
//                if (ActualStkPart.isBloqueado(dtStat, proCodiB, ejeLoteB, EU.em_cod, serLoteB, numLoteB, numIndiB, almOrigB))
//                {
//                    stkPart.ponerStock(proCodiB, ejeLoteB,
//                        EU.em_cod,
//                        serLoteB, numLoteB,
//                        numIndiB, almOrigB, 0,
//                        0);
//                    stkPart.setBloqueo(proCodiB, ejeLoteB,
//                        EU.em_cod,
//                        serLoteB, numLoteB,
//                        numIndiB, almOrigB, false);
//                }
//            }
            if (deo_blockE.getValor().equals("N"))
            {
                if (kgDifE.getValorDec() >= kgOrigE.getValorDec() * LIMDIF)
                {
                    msgBox("DIFERENCIA KILOS ENTRE ENTRADA Y SALIDA. IMPOSIBLE GUARDAR");
                    return;
                }
            }
            
           ValDespi.anulaValoracionDesp(dtAdd, eje_numeE.getValorInt(),
               deo_numdesE.getValorInt()==0?"D":"G",
                deo_numdesE.getValorInt()==0?deo_codiE.getValorInt():deo_numdesE.getValorInt());
           
            actualCabDesp();
//            actFechaStock();
            ctUp.commit();
            resetBloqueo(dtAdd, TABLA_BLOCK,
                eje_numeE.getValorInt()
                + "|" + deo_codiE.getValorInt());
        } catch (Throwable k)
        {
            Error("Error al Guardar Datos", k);
            return;
        }

        nav.pulsado = navegador.NINGUNO;
//    this.setEnabled(true);
        mensaje("");
        activaTodo();
        mensajeErr("Despiece .... Modificado");
//    verDatos(dtCons);
    }

    @Override
    public void canc_edit() {
        activaTodo();
        mensajeErr("Cancelada ... Edicion");
       
        try
        {
             s="UPDATE desporig set deo_block = '"+deoBlockAnt+"'"
                + " WHERE eje_nume = " + eje_numeE.getValorInt()
                + " and deo_codi = " + deo_codiE.getValorInt();
            dtAdd.executeUpdate(s);
            resetBloqueo(dtAdd, TABLA_BLOCK,
                eje_numeE.getValorInt()
                + "|" + deo_codiE.getValorInt(), true);
        } catch (Exception k)
        {
            msgBox("Error al Desbloquear registro\n" + k.getMessage());
        }
        verDatos(dtCons);
        nav.pulsado = navegador.NINGUNO;
    }

    @Override
    public void PADAddNew() {
        try
        {
            swForzadoTraz=false;
            inTidCodi=false;
            swErrCab=false;
//            proCodiB = 0;
            utdesp.setFechaCaducidad( null);
            ultCodigoEtiqueta=0;
            swMantLote = false;
            mensaje("Introducir Nuevo Despiece");
            nav.pulsado = navegador.ADDNEW;
            nav.setEnabled(false);
            jtCab.removeAllDatos();
            
            activar(true, navegador.ADDNEW);
            tid_codiE.setDeoCodi(null);
            
            Pcabe.resetTexto();
            deo_feccadE.resetTexto();
            deo_fecproE.resetTexto();
            deo_fecsacE.resetTexto();
            deo_feccadE.resetCambio();
           
            pro_codiE.resetCambio();
            deo_fechaE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
            usu_nombE.setText(EU.usuario);
            eje_numeE.setValorDec(EU.ejercicio);
            
            deo_cerraE.setSelected(false);
            deo_blockE.setValor("S"); // Abierto
            deo_ejlogeE.setValorDec(EU.ejercicio);
            
            deo_desnueE.setPulsado(false);
            deo_desnueE.setSelected(false);
            nIndDes = 0;
            deo_ejelotE.setValorDec(EU.ejercicio);
            
            deo_selogeE.setText(SERIE);
            
            tid_codiE.setEnabled(false);
            
            eje_numeE.setEnabled(false);
            jtCab.setValor("" + EU.ejercicio, jtCab.getSelectedRow(), JTCAB_EJELOT);
            
            kgDifE.setValorDec(0);
            kgOrigE.setValorDec(0);
            kgFinE.setValorDec(0);
            
            deo_kilosE.setEnabled(true);
//    deo_prcostE.setEnabled(false); // El costo no se puede introducir  o si?.
            jtLin.removeAllDatos();
            tid_codiE.setValorDec(0);
           
            proCodTD = 0;
            deo_lotnueE.setValor("-1");
            opSimular.setSelected(false);
            opAnularControl.setSelected(false);
            opAnularControl.setEnabled(isAdmin());
            dtAdd.commit();
            irGridCab();
//    jtCab.requestFocusInicio();
//    tid_codiE.requestFocus();
        } catch (SQLException ex)
        {
           Error("Error al iniciar transaccion de insertar",ex);
        }
    }
    /**
     * Funcion para ordenar las lineas de cabecera, para que mantenga el orden de pantalla
     * y no el de introduccion.
     * @throws SQLException 
     */
    void ordenarLineasCab() throws SQLException,ParseException
    {
         int numLin=1;
         if (nav.getPulsado()==navegador.ADDNEW)
         {
            s="select max(del_numlin) as numlinMax,min(del_numlin) as numLinMin,count(*) as numLin"
                    + " from desorilin where eje_nume = "+eje_numeE.getValorInt()
                    +  " and deo_codi = "+deo_codiE.getValorInt();
               dtCon1.select(s);
               if (dtCon1.getInt("numLinMin")-dtCon1.getInt("numLin")<=1)
                   numLin=dtCon1.getInt("numlinMax")+1;
         }
         for (int n=0;n<jtCab.getRowCount();n++)
         {            
            if (jtCab.getValorInt(n,JTCAB_NL)==0)
                continue;
            if (jtCab.getValorInt(n,JTCAB_NL)!=numLin || opMantFecha.isSelected() )
            {
                s="update desorilin  set del_numlin = "+numLin+
                    (opMantFecha.isSelected()?", deo_tiempo= {d '"+deo_fechaE.getFecha("yyyy-MM-dd")+"'}":"")
                    + " where eje_nume = "+eje_numeE.getValorInt()
                    + " and deo_codi = "+deo_codiE.getValorInt()
                    + " and  del_numlin= "+jtCab.getValorInt(n,JTCAB_NL);
                int nRows=dtAdd.executeUpdate(s);
                if (nRows!=1)
                    throw new SQLException("Error al regenerar numeros de linea: "+s+" Lineas modificadas: "+nRows);
                jtCab.setValor(numLin,n,JTCAB_NL);
            }
            numLin++;
        
         }
    }
    /**
     * Comprueba la validez del grid de cabecera
     * @return false si hay error.
     */
    boolean checkGridCab() {
        if (jtCab.isEnabled())
        {            
            swErrCab=cambiaLineajtCab(jtCab.getSelectedRow()) >= 0;
            if (swErrCab)
            {
                resetCambioLineaCab();
                jtCab.requestFocusSelected();
                return false;
            }
        }
        int nLin = getNuLiCab();

        if (nLin == 0)
        {
            mensajeErr("Introduzca algún  Producto en Cabecera");
            jtCab.requestFocusSelected();
            return false;
        }
        nLiCab = nLin;
        return true;
    }

    @Override
    public boolean checkAddNew() {
        deo_blockE.setValor("N");
        int col;
        try
        {

            if (!checkCab(false))
                return false;
            if (!checkGridCab())
                return false;
           
            if (jtLin.isEnabled())
            {
                jtLin.ponValores(jtLin.getSelectedRow());
                jtLin.salirGrid();

                if ((col = cambiaLineajtLin(jtLin.getSelectedRow())) >= 0)
                {
                    jtLin.requestFocus(jtLin.getSelectedRow(), col);
                    return false;
                }
            }
            if (getNuLiDes(false) == 0)
            {
                msgBox("Introduzca algun producto de salida");
                jtLin.requestFocusInicioLater();
                return false;
            }
            actKilos(0);
            if (kgDifE.getValorDec() < 0)
            {                   
                    if (kgDifE.getValorDec()> -2)
                    { // Kilos diferencia son menor de 2.
                         jtCab.resetCambio();
                         jtCab.setEnabled(false);
                         jtCab.requestFocusFinal();
                         ArrayList v = new ArrayList();
                         v.add("99");
                         v.add(pro_codiE.getNombArt(99));
                         v.add(0);
                         v.add("A");
                         v.add(0);
                         v.add(0);
                         v.add(kgDifE.getValorDec()*-1);
                         v.add(0);
                         v.add(0);
                         v.add(0);
                         v.add("");
                         jtCab.addLinea(v);
                         jtCab.setEnabled(true);
                         jtCab.requestFocusFinal();
                         jtCab.ponValores(jtCab.getSelectedRow());
                         cambiaLineajtCab(jtCab.getSelectedRow());
                         jtLin.setEnabled(false);
                         msgBox("Introducido "+kgDifE.getValorDec()*-1+" KG en entrada para cuadrar despiece");
                    }
                    else
                    {
                        msgBox("SOBRAN "+kgDifE.getValorDec()*-1+" en SALIDA");
                        return false;
                    }
            }
            if (kgDifE.getValorDec() < kgOrigE.getValorDec() * 0.10 && kgDifE.getValorDec()> 0.01)
            { // Insertar linea de mer
                    jtLin.resetCambio();
                    jtLin.setEnabled(false);
                    jtLin.requestFocusFinal();
                    ArrayList v = new ArrayList();
                    v.add(tid_codiE.isAutoMer()?ART_MER:ART_MERMA);
                    v.add(pro_codiE.getNombArt(tid_codiE.isAutoMer()?ART_MER:ART_MERMA));
                    v.add(kgDifE.getValorDec());
                    v.add(1);            
                    v.add("");
                    v.add( "");
                    v.add("" );
                    v.add(0);
                    v.add(1);
                    v.add("");
                    v.add("");
                    jtLin.addLinea(v);
                    jtLin.setEnabled(true);
                    jtLin.requestFocusFinal();
                    jtLin.ponValores(jtLin.getSelectedRow());
                    cambiaLineajtLin(jtLin.getSelectedRow());
                    jtCab.setEnabled(false);
                    msgBox("Introducido "+kgDifE.getValorDec()+" KG en salida para cuadrar despiece!");
            }
            
            if (kgDifE.getValorDec() <= kgOrigE.getValorDec() * MantDesp.LIMDIF)
            {                
                // Compruebo integridad de despiece, ya q se marcara como cerrado.
                if (isComprobarTipoDespiece())
                {
                    s = DespVenta.checkArtSalidaDesp(dtCon1, tid_codiE.getValorInt(),
                        jtLin, pro_codiE.getValorInt(), uniOrigE.getValorInt(),true);
                    if (s != null)
                    {
                        mensajeErr(s);
                        jtLin.requestFocusInicio();
                        return false;
                    }
                }
            }
            else
            {
                if (getNuLiCab() >= 1)
                {
                    if (!isAdmin() )
                    {
                        msgBox("Faltan kilos en salida. Imposible guardar despiece");
                        return false;
                    }
                    int res = mensajes.mensajePreguntar("Faltan kilos en productos salida. ¿Dejar despiece como pendiente?");
                    if (res != mensajes.OK)
                    {
                        mensajeErr("Introduzca los kilos de salida que faltan");
                        jtLin.requestFocusInicio();
                        return false;
                    }
                    deo_blockE.setValor("S");
                }
            }

            if (AVISATIDCODI && !isComprobarTipoDespiece())
            { // Comprueba si se han usado articulos que no estan en ese tipo de despiece
                int nRow = jtLin.getRowCount();
                for (int n = 0; n < nRow; n++)
                {
                    if (jtLin.getValorDec(n, DespVenta.JT_KILOS) == 0 || jtLin.getValorInt(n, 0) == 0)
                        continue;
                    if (!DespVenta.checkArticuloTipoDesp(tid_codiE.getValorInt(), pro_codiE.getValorInt(),
                        jtLin.getValorInt(n, 0), dtStat))
                    {
                        s = "Articulo: " + jtLin.getValorInt(n, 0) + " NO definido en Tipo Despiece: " + tid_codiE.getValorInt();
                        int res = mensajes.mensajePreguntar(s + " Continuar ?");
                        if (res != mensajes.YES)
                        {
                            return false;
                        }
                        enviaMailError(s + " en despiece: " + deo_codiE.getValorInt());
                    }
                }
            }
//      this.setEnabled(false);
            return true;
        } catch (Exception k)
        {
            Error("Error en checkAddNew", k);
        }
        return false;
    }

    /**
     * Pone stock por diferencia a producto original.
     * @throws Exception
     */
//    void ponStockDif() throws Throwable
//    {
////      deo_blockE.setValor("N");
//
//        deo_blockE.setValor("S");
//        jtCab.salirFoco(0);
////        stkPart.ponerStock(pro_codiE.getValorInt(), deo_ejelotE.getValorInt(),
////            EU.em_cod,
////            deo_serlotE.getText(), pro_loteE.getValorInt(),
////            pro_numindE.getValorInt(), deo_almoriE.getValorInt(), kgDifE.getValorDec(),
////            1);
//        stkPart.setBloqueo(pro_codiE.getValorInt(), deo_ejelotE.getValorInt(),
//            EU.em_cod,
//            deo_serlotE.getText(), pro_loteE.getValorInt(),
//            pro_numindE.getValorInt(), deo_almoriE.getValorInt(), true);
//        utdesp.iniciar(dtAdd, eje_numeE.getValorInt(), EU.em_cod,
//            deo_almdesE.getValorInt(), deo_almoriE.getValorInt(), EU);
//        utdesp.setLogotipo(null);
//        utdesp.setDirEmpresa(null);
//
//        proCodeti=pro_codiE.getLikeProd().isNull("pro_codeti")?0:pro_codiE.getLikeProd().getInt("pro_codeti");
//        
//        utdesp.imprEtiq(proCodeti, dtCon1, pro_codiE.getValorInt(), pro_nombE.getText(),
//           "D",
//            pro_loteE.getValorInt(),
//            "" + deo_ejelotE.getValorInt(), deo_serlotE.getText(),
//            pro_numindE.getValorInt(),
//            kgDifE.getValorDec(),
//            utdesp.getFechaDespiece(),
//            utdesp.getFechaProduccion(),
//            utdesp.getFechaCaducidad(),
//            utdesp.getFechaSacrificio(),//jtLin.getValDate(linea,5,def_feccadE.getFormato()),
//            utdesp.getFechaCaducidad(),deo_numdesE.getValorInt() );
//    }

    @Override
    public void ej_addnew1() {
        inTidCodi = false;
        try
        {
            if (deo_blockE.getValor().equals("N"))
            {
                 if (kgDifE.getValorDec() >= kgOrigE.getValorDec() * LIMDIF)
                 {
                     if (tid_codiE.isAutoMer() )
                        msgBox("KILOS ENTRADA Y SALIDA SON DISTINTOS. IMPOSIBLE CERRAR");
                        return;
                 }
            }

            pro_codiE.resetCambio();
            mensaje("Insertando Datos .. Espere, por favor");
            ordenarLineasCab();
            actualCabDesp(); // Actualizo Cabecera Despiece
//            actFechaStock();
            ctUp.commit();
            resetBloqueo(dtAdd, TABLA_BLOCK, eje_numeE.getValorInt()
                + "|" + deo_codiE.getValorInt());

        } catch (Throwable k)
        {
            Error("Error al Guardar Datos", k);
            return;
        }
//    this.setEnabled(true);
        mensaje("");
        activaTodo();
        mensajeErr("Despiece .... Introducido");
        nav.pulsado = navegador.NINGUNO;
        if (opRepet.isSelected())
        {
            tidCodAnt=tid_codiE.getValorInt();
            dtFinAnt.clear();
            int nRows=jtLin.getRowCount();
            for (int n=0;n<nRows;n++)
            {
                if (jtLin.getValorInt(n,JTLIN_PROCODI)>0 && jtLin.getValorDec(n,JTLIN_KILOS)>0 )
                    dtFinAnt.add(jtLin.getValorInt(n,JTLIN_PROCODI));
            }
        }
        else
           tidCodAnt=0;
        PADAddNew(); // Vuelta a Introducir.
    }
    /**
     * Comprueba si existen lineas de salida en el despiece
     * @return true hay lineas de salida.
     */
    boolean existLineasSalida()
    {        
         for (int n = 0; n < jtLin.getRowCount(); n++)
         {
              if (jtLin.getValorInt(n, JTLIN_ORDEN) > 0 )
                  return true;
         }
         return false;
    }
    /**
     * Borra Linea de Cabecera
     * @param numLin int Numero de Linea de la tabla desorilin
     * @throws SQLException
     */
    boolean borraLinCab(int numLin) throws SQLException {
        String condS = "desorilin "
            + " WHERE eje_nume = " + eje_numeE.getValorInt()
            + " and deo_codi = " + deo_codiE.getValorInt()
            + " and del_numlin = " + numLin;
        s = "select * FROM " + condS;
        if (!dtAdd.select(s, true))
            return false;

//        if (proCodiB != 0 && numLinB == numLin)
//        {
//            anuStkPart(dtAdd.getInt("pro_codi"),
//                dtAdd.getInt("deo_ejelot"),
//                EU.em_cod,
//                dtAdd.getString("deo_serlot"),
//                dtAdd.getInt("pro_lote"),
//                dtAdd.getInt("pro_numind"),
//                deoKilosB, 0, almOrigB, true);
//            proCodiB = 0;
//        } else
//        {
//            anuStkPart(dtAdd.getInt("pro_codi"),
//                dtAdd.getInt("deo_ejelot"),
//                EU.em_cod,
//                dtAdd.getString("deo_serlot"),
//                dtAdd.getInt("pro_lote"),
//                dtAdd.getInt("pro_numind"),
//                dtAdd.getDouble("deo_kilos") * -1, -1, deo_almoriE.getValorInt(), false);
//        }
        dtAdd.executeUpdate("delete from " + condS);
        dtAdd.commit();
        return true;
    }

    @Override
    public void canc_addnew() {
        try
        {
            if (deo_codiE.getValorInt() != 0)
            {
                s=checkMvtosSalida();
                if (s!=null)
                {
                    msgBox(s);
                    return;
                }
                if (mensajes.mensajeYesNo("VOLVER AL DESPIECE ? ", this)
                    == mensajes.YES)
                    return;
                copiaRegistro("Anulada Alta de despiece");
                jtLin.setValor("" + proCodAnt, JTLIN_PROCODI); // Restauro el valor Antiguo
                jtLin.setValor("" + defKilAnt, JTLIN_KILOS);
                nav.pulsado = navegador.NINGUNO;
                for (int n = 0; n < jtLin.getRowCount(); n++)
                {
                    if (jtLin.getValorInt(n, JTLIN_ORDEN) > 0)
                    {
                        borraLinDesp(jtLin.getValorInt(n, JTLIN_ORDEN));
                    }
                }
                for (int n = 0; n < jtCab.getRowCount(); n++)
                {
                    if (jtCab.getValorInt(n, JTCAB_NL) > 0)
                    {
                        borraLinCab(jtCab.getValorInt(n, JTCAB_NL));
                    }
                }

                

                resetBloqueo(dtAdd, TABLA_BLOCK, eje_numeE.getValorInt()
                    + "|" + deo_codiE.getValorInt());

                if (jf != null)
                {
                    jf.ht.clear();
                    jf.ht.put("%a", deo_codiE.getText());
                    jf.guardaMens("D1", jf.ht);
                }
                ctUp.commit();
            }
            opVerGrupo.setSelected(false);
            strSql = getStrSql() + getCondWhere() + getOrderQuery();
            rgSelect();
        } catch (Exception k)
        {
            Error("Error al Realizar La Anulacion", k);
            return;
        }
        mensaje("");
        mensajeErr("Insercion ... CANCELADA");
        activaTodo();

        verDatos(dtCons);
    }
    /**
     * Comprueba si un despiece se puede borrar.
     * @return Cadena con error. Null si no hay error.
     * @throws SQLException 
     */
    String checkBorrar() throws SQLException
    {
         if (deo_blockE.getValor().equals("B"))
            return "REGISTRO BLOQUEADO ... CIERRELO EN EL TACTIL PRIMERO";
      
        if (!MODPRECIO && swTienePrec)
            return "DESPIECE VALORADO ... IMPOSIBLE BORRAR";
           
        if (swTienePrec && deo_numdesE.getValorInt()!=0 )
            return "Despiece valorado en un grupo. Imposible borrar";
        // Busco lineas de salida para ver si no ha habido mvtos de Almacen
       return checkMvtosSalida();
    }
    /**
     * Comprueba mvtos salida sobre despiece en pantalla
     * @return null si no hubo errores
     */
    private String checkMvtosSalida() throws SQLException
    {
        s="select * from v_despfin where eje_nume ="+eje_numeE.getValorInt()+
            " and deo_codi = "+deo_codiE.getValorInt();
        if (!dtCon1.select(s))
            return null;
        do 
        {
            if (!MantArticulos.isVendible(dtCon1.getInt("pro_codi"), dtStat))
                continue;
            if (MvtosAlma.hasMvtosSalida(dtStat,dtCon1.getInt("pro_codi"), EU.em_cod,
                dtCon1.getInt("def_ejelot"),
                dtCon1.getString("def_serlot"),dtCon1.getInt("pro_lote"),dtCon1.getInt("pro_numind"),
                dtCon1.getDouble("def_kilos"),deo_fechaE.getText()) != 0)
                    return  "Producto " + dtCon1.getInt("pro_codi") + " con Individuo: "
                        + dtCon1.getInt("pro_numind")
                        + " generado en despiece tiene mvtos de salida. Imposible BORRAR";
        } while (dtCon1.next());
        return null;
    }
    @Override
    public void PADDelete() {
        if (opVerGrupo.isSelected())
        {
            msgBox("No puede BORRAR si se estan viendo GRUPOS");
            activaTodo();
            return;
        }
        if (hisRowid>0)
        {
            msgBox("No se puede Borrar viendo datos de historico");
            activaTodo();
            return;
        }
        mensajeErr("");
        try {
           s=checkBorrar();
           if (s!=null)
           {
               msgBox(s);
               activaTodo();
               return;
           }
             if (!checkMvtos(eje_numeE.getValorInt(), deo_codiE.getValorInt()))
                return;
           if (!setBloqueo(dtAdd, TABLA_BLOCK,
                eje_numeE.getValorInt()
                + "|" + deo_codiE.getValorInt()))
           {
                msgBox(msgBloqueo);
                activaTodo();
                return;
           }
        } catch (SQLException | UnknownHostException k)
        {
            Error("Error al bloquear registro", k);
        }

        mensaje("Borrando Despiece");
        Baceptar.setEnabled(true);
        Bcancelar.setEnabled(true);
        jtHist.setEnabled(false);
        Bcancelar.requestFocus();
    }
    /**
     * Borra el registro activo.
     * @param boolean delCab Borrar cabecera?. Normalmente es false
     * @throws SQLException 
     */
    private void borraRegistro(boolean delCab) throws SQLException
    {
        int nRow = jtCab.getRowCount();
        if (delCab)
        {
            s = "delete from desporig WHERE eje_nume = " + eje_numeE.getValorInt()
                + " and deo_codi = " + deo_codiE.getValorInt();
            stUp.executeUpdate(s);
        }
        s = "delete from desorilin WHERE eje_nume = " + eje_numeE.getValorInt()
            + " and deo_codi = " + deo_codiE.getValorInt();
        stUp.executeUpdate(s);
     
//        for (int n = 0; n < nRow; n++)
//        {
//            if (deo_blockE.getValor().equals("S"))
//            {
//                stkPart.ponerStock(jtCab.getValorInt(n, JTCAB_PROCODI), jtCab.getValorInt(n, JTCAB_EJELOT),
//                    EU.em_cod, jtCab.getValString(n, JTCAB_SERLOT),
//                    jtCab.getValorInt(n, JTCAB_NUMLOT), jtCab.getValorInt(n, JTCAB_NUMIND),
//                    deo_almoriE.getValorInt(),
//                    jtCab.getValorDec(n, JTCAB_KILOS), 1);
//                stkPart.setBloqueo(jtCab.getValorInt(n, JTCAB_PROCODI), jtCab.getValorInt(n, JTCAB_EJELOT),
//                    EU.em_cod, jtCab.getValString(n, JTCAB_SERLOT),
//                    jtCab.getValorInt(n, JTCAB_NUMLOT), jtCab.getValorInt(n, JTCAB_NUMIND),
//                    deo_almoriE.getValorInt(),
//                    false);
//            } else
//            {
//                anuStkPart(jtCab.getValorInt(n, JTCAB_PROCODI), jtCab.getValorInt(n, JTCAB_EJELOT),
//                    EU.em_cod, jtCab.getValString(n, JTCAB_SERLOT),
//                    jtCab.getValorInt(n, JTCAB_NUMLOT), jtCab.getValorInt(n, JTCAB_NUMIND),
//                    jtCab.getValorDec(n, JTCAB_KILOS) * -1, -1, deo_almoriE.getValorInt());
//            }
//        }
        nRow = jtLin.getRowCount();

        for (int n = 0; n < nRow; n++)
        {
            if (jtLin.getValorInt(n, JTLIN_ORDEN) > 0)
                borraLinDesp(jtLin.getValorInt(n, JTLIN_ORDEN));
        }
    }
    @Override
    public void ej_delete1() {
        
        try
        {
            if (mensajes.mensajeYesNo("ANULAR BORRADO ? ", this) == mensajes.YES)
                return;
            copiaRegistro("Registro BORRADO");
            borraRegistro(false);
            
            if (jf != null)
            {
                jf.ht.clear();
                jf.ht.put("%a", deo_codiE.getText());
                jf.guardaMens("D3", jf.ht);
            }

            ctUp.commit();
            resetBloqueo(dtAdd, TABLA_BLOCK,
                eje_numeE.getValorInt()
                + "|" + deo_codiE.getValorInt());

            activaTodo();
            rgSelect();
            verDatos(dtCons);
            mensaje("");
            mensajeErr("Despiece ... BORRADO");
            nav.pulsado = navegador.NINGUNO;
        } catch (Exception ex)
        {
            Error("Erorr al ANULAR registro", ex);
        }
    }

    @Override
    public void canc_delete() {
        activaTodo();
        mensajeErr("Borrado ... CANCELADO");
        try
        {
            resetBloqueo(dtAdd, TABLA_BLOCK,
                eje_numeE.getValorInt()
                + "|" + deo_codiE.getValorInt());
        } catch (Exception k)
        {
            msgBox("Error al Desbloquear registro\n" + k.getMessage());
        }
        verDatos(dtCons);
        nav.pulsado = navegador.NINGUNO;
    }
    void forzarProd()
    {
        try
        {
            String s="update v_despfin set def_tippro= '"+deo_incvalE.getValor()+"'"+
                " where eje_nume = "+eje_numeE.getValorInt()+
                " and deo_codi = "+deo_codiE.getText();
            int nRows=dtAdd.executeUpdate(s);
            dtAdd.commit();
            msgBox(nRows+" Lineas Actualizada");
        } catch (SQLException ex)
        {
            Error("Error al actualizar estado produccion", ex);
        }
    }
    void irTactil()
    {
        try
        {
            s="select * from desporig WHERE deo_seloge ='"+ MantDespTactil.SERIE+"'"+
                " and deo_lotnue!=0 "+
                " and eje_nume = "+eje_numeE.getValorInt()+
                " and deo_codi = "+deo_codiE.getText();
            if (! dtStat.select(s))
            {
                msgBox("Despiece no es tipo TACTIL");
                return;
            }
            ejecutable prog;
            if ((prog = jf.gestor.getProceso(MantDespTactil.getNombreClase())) == null)
                return;
            MantDespTactil cm = (MantDespTactil) prog;
            if (cm.inTransation())
            {
                msgBox("Mantenimiento Despieces ocupado. No se puede realizar la busqueda");
                return;
            }
            cm.PADQuery();
            cm.setEjeNume(eje_numeE.getValorInt());
            cm.setDeoCodi(deo_codiE.getText());
            
            cm.ej_query();
            jf.gestor.ir(cm);
        } catch (SQLException ex)
        {
           Error("Error al ir a Mantenimiento Tactil",ex);
        }
    }
    @Override
    public void activar(boolean enab) {
        activar(enab, navegador.TODOS);

    }

    void activar(boolean enab, int est) {

        switch (est)
        {
            case navegador.TODOS:  
                
                opVerGrupo.setEnabled(enab);
                deo_nulogeE.setEnabled(false);
                deo_selogeE.setEnabled(false);
                usu_nombE.setEnabled(false);
                deo_codiE.setEnabled(false);
                deo_numdesE.setEnabled(false);
                Bimpeti.setEnabled(!enab);
                jtLin.setEnabled(enab);
                jtCab.setEnabled(enab);
                Baceptar.setEnabled(enab);
                deo_codiE.setEnabled(enab);
                usu_nombE.setEnabled(enab);                               
            case navegador.ADDNEW:
//               deo_numdesE.setEnabled(enab);
//               deo_fecsacE.setEnabled(enab);     
//               deo_fecproE.setEnabled(enab);    
               deo_selogeE.setEnabled(enab);
               deo_nulogeE.setEnabled(enab);
//        deo_blockE.setEnabled(enab);
            case navegador.EDIT:
//                deo_feccadE.setEnabled(enab);                  
                cli_codiE.setEnabled(enab);
                deo_almoriE.setEnabled(enab);
                deo_almdesE.setEnabled(enab);
                ImprEtiqMI.setEnabled(!enab);
                cli_codiE.setEnabled(enab);
//                deo_numdesE.setEnabled(enab);
                cli_codiE.setEnabledNombre(enab);
                deo_selogeE.setEnabled(enab);
                deo_nulogeE.setEnabled(enab);
                deo_desnueE.setEnabled(enab);
                BsalLin.setEnabled(enab);
                BsaltaCab.setEnabled(enab);
                opImpEt.setEnabled(enab);
                Plotgen.setEnabled(enab);
                cargaPSC.setEnabled(enab);
                Pcabe.setEnabled(enab);
                POtros.setEnabled(enab);
                deo_lotnueE.setEnabled(false);
                Plotgen.setEnabled(enab);
                tid_codiE.setEnabled(enab);
                Bcancelar.setEnabled(enab);
                jtHist.setEnabled(!enab);
                BirGrid.setEnabled(enab);
                opSimular.setEnabled(enab);
                opVerAgrup.setEnabled(!enab);
                BvalDesp.setEnabled(!enab);
        }
    }

    @Override
    public void PADPrimero() {
        verDatos(dtCons);
    }

    @Override
    public void PADAnterior() {
        verDatos(dtCons);
    }

    @Override
    public void PADSiguiente() {
        verDatos(dtCons);
    }

    @Override
    public void PADUltimo() {
        verDatos(dtCons);
    }

    boolean verDatos(DatosTabla dt) {
        tablaCab="desporig";
        tablaEnt="desorilin";
        tablaSal="v_despfin";
        vistaDesp="v_despori";
        hisRowid=0;
        condHist="";
        return verDatos(dt, opVerGrupo.isSelected(),opVerAgrup.isSelected(),  true);
    }
    boolean verDatos(DatosTabla dt, boolean verGrupo, boolean limpiaDesp)
    {
      return verDatos(dt, verGrupo,opVerAgrup.isSelected(),  true);  
    }
    
    boolean verDatos(DatosTabla dt, boolean verGrupo,boolean verAgrup, boolean limpiaDesp) {
        try
        {
            jtCab.removeAllDatos();
            jtLin.setEnabled(false);
            jtLin.removeAllDatos();
            if (limpiaDesp)
            {
                jtDesp.setEnabled(false);
                jtDesp.removeAllDatos();
            }
            if (dt.getNOREG())
                return false;
            

            if (!tid_codiE.getArticulos().isEmpty())
            {
                tid_codiE.setVerSoloActivo(false);
                tid_codiE.clearArticulos();
                tid_codiE.releer();
            }

            if (verGrupo)
                s = "select * from grupdesp WHERE eje_nume = " + dt.getInt("eje_nume")
                    + " and grd_nume = " + dt.getInt("grd_nume");
            else
                s = "select * from "+tablaCab+" WHERE  eje_nume = " + dt.getInt("eje_nume")
                    + " and deo_codi = " + dt.getInt("deo_codi")+
                    condHist;


            eje_numeE.setValorDec(dt.getInt("eje_nume"));

            if (!dtStat.select(s))
            {
                msgBox("Datos de  DESPIECE ... NO ENCONTRADOS");
                Pcabe.resetTexto();
                return false;
            }

            double kilos;

            int nLin = 0;
//      this.setEnabled(false);
            if (verGrupo)
            {
                jtHist.setEnabled(false);
                jtHist.removeAllDatos();
                deo_numdesE.setValorDec(dtStat.getInt("grd_nume", true));
                deo_fechaE.setDate(dtStat.getDate("grd_fecha"));
                if (limpiaDesp)
                {
                    s = "SELECT deo_codi,deo_fecha "
                        + " FROM "+tablaCab+" as c where  c.eje_nume = " + dt.getInt("eje_nume")
                        + " and c.deo_numdes = " + dt.getInt("grd_nume")
                        + condHist;
                    dtCon1.select(s);
                    jtDesp.setDatos(dtCon1);
                    jtDesp.setEnabled(true);
                }
                lastSqlWhereLin = "FROM "+tablaEnt+" as l,"+tablaCab+" as c where c.eje_nume = " + dt.getInt("eje_nume")
                    + " and c.deo_numdes = " + dt.getInt("grd_nume")
                    + " and c.eje_nume=l.eje_nume "
                    + " and c.deo_codi = l.deo_codi "+
                    condHist;
                s = "SELECT pro_codi,sum(deo_kilos) as deo_kilos, count(*) as numUnid, "
                    + " deo_prcost,deo_preusu "
                    + lastSqlWhereLin
                    + " group by pro_codi,deo_prcost, deo_preusu "
                    + " order by pro_codi";
            }
            else
            {
                deo_codiE.setValorInt(dtStat.getInt("deo_codi"));
                deo_numdesE.setValorDec(dtStat.getInt("deo_numdes", true));
                deo_blockE.setValor(dtStat.getString("deo_block"));
                deo_incvalE.setValor(dtStat.getString("deo_incval"));
                deo_fechaE.setDate(dtStat.getDate("deo_fecha"));
                deo_desnueE.setSelected(dtStat.getString("deo_desnue", true).equals("S"));
                deo_feccadE.setText(dtStat.getFecha("deo_feccad", "dd-MM-yyyy"));
                deo_feccadE.resetCambio();
                deo_fecproE.setDate(dtStat.getDate("deo_fecpro"));
                deo_fecsacE.setDate(dtStat.getDate("deo_fecsac"));
                prv_codiE.setText(dtStat.getString("prv_codi"), true);
                deo_ejlogeE.setText(dtStat.getString("deo_ejloge"));
                deo_selogeE.setText(dtStat.getString("deo_seloge")); 
                deo_nulogeE.setText(dtStat.getString("deo_nuloge"));
                deo_lotnueE.setValor(dtStat.getString("deo_lotnue"));
                deo_fechaE.setText(dtStat.getFecha("deo_fecha", "dd-MM-yyyy"));
                usu_nombE.setText(dtStat.getString("usu_nomb"));
                deo_cerraE.setSelected(dtStat.getInt("deo_cerra",true) == 0);
                deo_almoriE.setText(dtStat.getString("deo_almori"));
                deo_almdesE.setText(dtStat.getString("deo_almdes"));
                tid_codiE.setText(dtStat.getString("tid_codi"));
                cli_codiE.setValorInt(dtStat.getInt("cli_codi",true));
                opAnularControl.setSelected(dtStat.getInt("deo_anucon",true)!=0);
                if (hisRowid==0)
                {
                    jtHist.setEnabled(false);
                    jtHist.removeAllDatos();
                    s="SELECT * FROM deorcahis where eje_nume = " + dt.getInt("eje_nume")
                        + " and deo_codi = " + dt.getInt("deo_codi")+
                        " order by  his_rowid desc";
                    if (dtCon1.select(s))
                    {
                        ArrayList v1=new ArrayList();
                        v1.add("");
                        v1.add("");
                        v1.add("** ACTUAL **");
                        v1.add(0);
                        jtHist.addLinea(v1);
                        do
                        {
                            ArrayList v=new ArrayList();
                            v.add(dtCon1.getTimeStamp("his_fecha"));
                            v.add(dtCon1.getString("his_usunom"));
                            v.add(dtCon1.getString("his_coment"));
                            v.add(dtCon1.getInt("his_rowid"));
                            jtHist.addLinea(v);
                        } while (dtCon1.next());
                        jtHist.requestFocus(0,0);
                        jtHist.setEnabled(true);
                    }
                }
                lastSqlWhereLin = "FROM "+tablaEnt+" as l where l.eje_nume = " + dt.getInt("eje_nume")
                    + " and l.deo_codi = " + dt.getInt("deo_codi")+
                    condHist;
                if (verAgrup)
                    s="SELECT pro_codi,sum(deo_kilos) as deo_kilos, count(*) as numUnid, "
                       + " deo_prcost,deo_preusu "
                       + lastSqlWhereLin
                       + " group by pro_codi,deo_prcost, deo_preusu "
                       + " order by pro_codi";
                else
                    s = "SELECT *,1 as numUnid " + lastSqlWhereLin
                         + " order by del_numlin";
            }
            kilos = 0;
            double importe = 0;
            if (dtCon1.select(s))
            {
                do
                {
                    ArrayList v = new ArrayList();
                    v.add(dtCon1.getString("pro_codi"));
                    v.add(pro_codiE.getNombArt(dtCon1.getString("pro_codi")));
                    v.add(verGrupo || verAgrup ? 0 : dtCon1.getInt("deo_ejelot"));
                    v.add(verGrupo || verAgrup ? "" : dtCon1.getString("deo_serlot"));
                    v.add(verGrupo || verAgrup ? 0 : dtCon1.getInt("pro_lote"));
                    v.add(verGrupo || verAgrup ? 0:dtCon1.getInt("pro_numind"));
                    v.add(dtCon1.getDouble("deo_kilos"));
                    v.add(MODPRECIO ? dtCon1.getDouble("deo_prcost") : 0);
                    v.add(verGrupo || verAgrup ? dtCon1.getInt("numUnid") : dtCon1.getInt("del_numlin"));
                    v.add(MODPRECIO ? dtCon1.getDouble("deo_preusu") : 0);
                    v.add(verGrupo || verAgrup ? "": Formatear.getFecha(dtCon1.getTimeStamp("deo_tiempo"),"dd-MM-yy HH:mm:ss"));
                    kilos += dtCon1.getDouble("deo_kilos");
                    importe += dtCon1.getDouble("deo_kilos") * dtCon1.getDouble("deo_prcost");
                    jtCab.addLinea(v);
                    if (pro_codiE.isVendible())
                        nLin += dtCon1.getInt("numUnid");
                } while (dtCon1.next());
              jtCab.requestFocusInicio();
            }
            kgOrigE.setValorDec(kilos);
            uniOrigE.setValorDec(nLin);
            impOrigE.setValorDec(importe);
            verDatLin(dt.getInt("eje_nume"),
                verGrupo ? dt.getInt("grd_nume") : dt.getInt("deo_codi"), verGrupo,verAgrup);
//     this.setEnabled(true);
        } catch (Exception k)
        {
            Error("Error al ver datos", k);
            return false;
        }
        return true;
    }

    void verDatLin(int ejeNume, int numDesp, boolean opGrupo,boolean opAgrup) throws SQLException {
        if (!opGrupo && ! opAgrup )
        {
            s = " select f.pro_codi as pro_codi,f.def_numpie as def_numpie, "
                + " f.def_kilos as def_kilos ,f.def_prcost as def_prcost,def_preusu,pro_numind,"
                + " def_feccad,def_orden,def_unicaj,def_tiempo "
                + " from "+tablaSal+" as f where "
                + " f.def_kilos <> 0 "
                + " and eje_nume = " + eje_numeE.getValorInt()
                + " AND deo_codi = " + numDesp
                + condHist
                + " order by def_orden,f.pro_codi ";
        } else
        {
            s = " select f.pro_codi as pro_codi,sum(f.def_numpie) as def_numpie, "
                + " sum(f.def_kilos) as def_kilos,f.def_prcost as def_prcost,def_preusu,sum(pro_numind) as pro_numind,"
                + " sum(def_unicaj) as def_unicaj,'' as def_tiempo "
                + " from "+tablaSal+" as f where "
                + " f.def_kilos <> 0 "
                + " and eje_nume = " + ejeNume
                + " AND "+(opGrupo?" def_numdes ":"deo_codi")+" = " + numDesp
                + condHist
                + " group by pro_codi,def_prcost,def_preusu "
                + " order by f.pro_codi,def_prcost ";
        }

        double kilos = 0;
        int nLin = 0;
        jtLin.setEnabled(false);
        jtLin.removeAllDatos();
        if (!dtCon1.select(s))
        {
            kgFinE.setValorDec(0);
            uniFinE.setValorDec(0);
            kgDifE.setValorDec(kgOrigE.getValorDec());
            return;
        }
//   deo_codiE.setText(dtCon1.getString("deo_codi"));
        swTienePrec = false;
        double importe = 0;
        do
        {
            ArrayList v = new ArrayList();
            v.add(dtCon1.getString("pro_codi"));
            v.add(pro_codiE.getNombArt(dtCon1.getString("pro_codi")));
            v.add(dtCon1.getString("def_kilos"));
            v.add(dtCon1.getString("def_numpie"));
            if (dtCon1.getDouble("def_prcost") != 0)
                swTienePrec = true;
            if (MODPRECIO)
                v.add(dtCon1.getString("def_prcost"));
            else
                v.add("");
            v.add(opGrupo || opAgrup ? "" : dtCon1.getFecha("def_feccad", "dd-MM-yyyy"));
            v.add(opGrupo || opAgrup ? "" : dtCon1.getString("pro_numind"));
            v.add(opGrupo || opAgrup ? "" : dtCon1.getString("def_orden"));
            v.add(dtCon1.getString("def_unicaj"));
            if (MODPRECIO)
                v.add(dtCon1.getString("def_preusu"));
            else
                v.add("");
            v.add(opGrupo || opAgrup ? "": Formatear.getFecha(dtCon1.getTimeStamp("def_tiempo"),"dd-MM-yy HH:mm:ss"));
            jtLin.addLinea(v);
            kilos += dtCon1.getDouble("def_kilos");
            importe += dtCon1.getDouble("def_kilos") * dtCon1.getDouble("def_prcost");
            nLin += dtCon1.getInt("def_numpie")*dtCon1.getInt("def_unicaj");
        } while (dtCon1.next());
        kgFinE.setValorDec(kilos);
        uniFinE.setValorDec(nLin);
        if (MODPRECIO)
            impFinE.setValorDec(importe);
        kgDifE.setValorDec(kgOrigE.getValorDec() - kilos);
    }

    int cambiaLineajtCab(int linea) 
    {
        try
        {
            
            if (!pro_numindE.hasCambio() && !pro_loteE.hasCambio()
               && !pro_codiE.hasCambio() && !deo_serlotE.hasCambio()
               && !deo_ejelotE.hasCambio() && !deo_kilosE.hasCambio() && !swErrCab) 
               return -1; // No hubo cambios
            resetCambioLineaCab();
            
            if (pro_codiE.isNull())
                return -1; // SI no hay producto lo doy como bueno.
            if (jtCab.getValorInt(linea,JTCAB_NL)>0 && !deo_kilosE.isEnabled())
            {
                  s="select * from desorilin "
                    + " WHERE eje_nume = " + eje_numeE.getValorInt()
                    + " and deo_codi = " + deo_codiE.getValorInt()
                    + " and del_numlin = " + jtCab.getValorInt(linea,JTCAB_NL)+
                      " and pro_numind = "+pro_numindE.getValorInt()+
                      " and pro_lote = "+pro_loteE.getValorInt()+
                      " and pro_codi = "+pro_codiE.getValorInt()+
                      " and deo_serlot = '"+deo_serlotE.getText()+"'"+
                      " and deo_ejelot = "+deo_ejelotE.getValorInt();
                  if (dtStat.select(s))
                      return -1; // No ha cambiado. posible lag en el programa
            }
            if (!pro_codiE.controla(false))
            {
                mensajeErr("Codigo de Producto NO valido");
                return 0;
            }

            if (!pro_codiE.isActivo())
            {
                mensajeErr("Producto NO esta ACTIVO");
                return 0;
            }
            if (pro_codiE.isVendible())
            {
                if (deo_ejelotE.getValorInt() == 0 && pro_codiE.hasControlIndiv() )
                {
                    mensajeErr("Introduzca Ejercicio de Lote");
                    return 2;
                }
                if (deo_serlotE.getText().trim().equals("")  && pro_codiE.hasControlIndiv())
                {
                    mensajeErr("Introduzca Serie de Lote");
                    return 4;
                }
                if (pro_loteE.getValorInt() == 0  && pro_codiE.hasControlIndiv())
                {
                    mensajeErr("Introduzca Número de Lote");
                    return 5;
                }
                if (jtCab.getValorDec(linea, JTCAB_KILOS) < 0)
                {
                    mensajeErr("Kilos no pueden estar en negativo");
                    return 5;
                }
                if (!tid_codiE.isNull() && tid_codiE.getValorInt() != MantTipDesp.LIBRE_DESPIECE
                    && CHECKTIDCODI && !opSimular.isSelected() && pro_codiE.isVendible())
                { // Tipo despiece esta metido. Comprobar productos
                    if (tid_codiE.getValorInt() == MantTipDesp.AUTO_DESPIECE
                        || tid_codiE.getValorInt() == MantTipDesp.CONGELADO_DESPIECE)
                    {
                        int nRow = jtCab.getRowCount();
                        for (int n = 0; n < nRow; n++)
                        {
                            if (tid_codiE.getValorInt() == MantTipDesp.CONGELADO_DESPIECE && pro_codiE.isCongelado())
                            {
                                mensajeErr("Para tipo despiece CONGELAR, los productos de entrada deben ser frescos");
                                return 0;
                            }
                            if (n == linea)
                            {
                                continue;
                            }
                            if (jtCab.getValorDec(n, JTCAB_KILOS) == 0 || jtCab.getValorInt(n, JTCAB_PROCODI) == 0)
                                continue;

                            if (MantArticulos.getTipoProd(jtCab.getValorInt(n, JTCAB_PROCODI),dtStat).equals(MantArticulos.TIPO_VENDIBLE) && !MantTipDesp.esEquivalente(jtCab.getValorInt(n, JTCAB_PROCODI), pro_codiE.getValorInt(), dtStat) )
                            {
                                mensajeErr("Para autodespieces TODOS los productos entrada deben ser iguales");
                                return 0;
                            }
                        }
                    } 
                    else
                    {
                        if (!MantTipDesp.checkArticuloEntrada(dtStat, pro_codiE.getValorInt(), tid_codiE.getValorInt()))
                        {
                            mensajeErr("ARTICULO no valido para este tipo de despiece");
                            return 0;
                        }
                    }
                }
            }
//      jtCab.setValor("" + 1, linea, 7);
//      deo_kilosE.setValorDec(1);
            boolean checkStock=false;
            if (!opSimular.isSelected() && pro_codiE.isVendible())
            {
                checkStock = buscaPesoLin();
                if (checkStock)
                {
                    if (stkPartid.hasError())
                    {
                        if (!swErrCab)
                        {
                            int ret = mensajes.mensajeYesNo(stkPartid.getMensaje()+" ¿SOLUCIONAR ?");
                            if (ret == mensajes.YES)
                            {                       
                                CheckStock.ir(jf, pro_codiE.getValorInt(),deo_ejelotE.getValorInt(),
                                    deo_serlotE.getText(), pro_loteE.getValorInt(), pro_numindE.getValorInt());
                                pro_loteE.setCambio(true);                
                            }
                        }
                        else
                            mensajeErr(stkPartid.getMensaje());
                         return JTCAB_PROCODI;
                    }
                    if (stkPartid.isLockIndiv())
                    {
                        mensajeErr(stkPartid.getMensaje());
                        return JTCAB_EJELOT;
                    }
                    
                }
            }
                                          

            double kilact=deo_kilosE.isEnabled()? deo_kilosE.getValorDec():stkPartid.getKilos();
            if (! opSimular.isSelected() && checkStock)
            {
                s = "SELECT * FROM desorilin "+
                    " WHERE  eje_nume = " + eje_numeE.getValorInt() +
                    " and deo_codi = "+deo_codiE.getValorInt()+
                    " and pro_codi  = "+pro_codiE.getValorInt()+
                    " and deo_ejelot = " + deo_ejelotE.getValorInt() +
                    " and deo_serlot = '" + deo_serlotE.getText() + "'" +
                    " and pro_lote = " + pro_loteE.getValorDec()+
                    " AND pro_numind = "+pro_numindE.getValorInt()+
                    " and del_numlin = " + jtCab.getValorInt(linea,JTCAB_NL);
                if (dtCon1.select(s))
                   kilact += dtCon1.getDouble("deo_kilos");
                if (! stkPartid.hasStock( kilact))
                {               
                        int ret = mensajes.mensajeYesNo("NO HAY "+deo_kilosE.getValorDec()+" KG. DISPONIBLES DE ESTE INDIVIDUO ¿SOLUCIONAR ?");
                        if (ret == mensajes.YES)
                        {                       
                            CheckStock.ir(jf, pro_codiE.getValorInt(),deo_ejelotE.getValorInt(),
                                deo_serlotE.getText(), pro_loteE.getValorInt(), pro_numindE.getValorInt());
                            pro_loteE.setCambio(true);                
                        }
                        return JTLIN_PROCODI;
                 }
            }

            if (opSimular.isSelected())
                return -1;


            int nRow;
            // Compruebo que no me metan el mismo individuo dos veces ?
            if (pro_numindE.getValorInt() != 0)
            {
                // Compruebo que el producto no sea uno metido en las lineas (por si las moscas)
                nRow = jtLin.getRowCount();
                for (int n = 0; n < nRow; n++)
                {
                    if (jtLin.getValorInt(n, 0) == pro_codiE.getValorInt()
                        && deo_nulogeE.getValorInt() == pro_loteE.getValorInt()
                        && jtLin.getValorInt(n, 5) == pro_numindE.getValorInt())
                    {
                        mensajes.mensajeAviso(
                            "!! ERROR !!! Individuo se genera a si mismo en la linea  " + n);
                        return 0;
                    }
                }
            }


            nRow = jtCab.getRowCount();
            for (int n = 0; n < nRow; n++)
            {
                if (n == linea)
                    continue;
                if (jtCab.getValorInt(n, JTCAB_PROCODI) == pro_codiE.getValorInt()
                    && jtCab.getValorInt(n, JTCAB_EJELOT) == deo_ejlogeE.getValorInt()
                    && jtCab.getValString(n, JTCAB_SERLOT).equals(deo_selogeE.getText())
                    && jtCab.getValorInt(n, JTCAB_NUMLOT) == pro_loteE.getValorInt()
                    && jtCab.getValorInt(n, JTCAB_NUMIND) == pro_numindE.getValorInt())
                {
                    mensajes.mensajeAviso("!! ATENCION !!! Linea Repetida en posicion:   " + n);
                    return 0;
                }
            }
            
            if (checkStock)
            {
                jtCab.setValor(stkPartid.getKilos(),linea, JTCAB_KILOS);
                deo_kilosE.setValorDec(stkPartid.getKilos());
            }
            if (deo_kilosE.getValorDec()<=0)
            {
                mensajeErr("Kilos debe ser superior a 0");
                return JTCAB_PROCODI;
            }
                
            // Procedo a guardar la linea.
            dtAdd.commit();
            if (jtCab.getValorInt(linea, JTCAB_NL) != 0)
            { // Esta linea ya se ha guardado.
                // Miro a ver si se ha cambiado.
                String condS = "desorilin "
                    + " WHERE eje_nume = " + eje_numeE.getValorInt()
                    + " and deo_codi = " + deo_codiE.getValorInt()
                    + " and del_numlin = " + jtCab.getValorInt(linea, JTCAB_NL);

                s = "SELECT * FROM " + condS;
                if (!dtAdd.select(s, true))
                { // Ahora va y no existe la linea... Lanzamos un error.
                    Error("Linea Entrada de Despiece no encontrada: " + s,
                        new Exception("Error al Actualizar Linea entrada"));
                    return 0;
                }                
                dtAdd.executeUpdate("delete from " + condS);
                guardaDesOrig(linea);
            } else
            {
                guardaDesOrig(linea);
            }
            if (linea==0)
            {
                deo_feccadE.resetTexto();
                deo_fecsacE.resetTexto();
                deo_fecproE.resetTexto();
            }
            deo_almoriE.setEnabled(false);
        } catch (Exception k)
        {
            Error("Error al Controlar Linea de Cabececera", k);
            return 0;
        }
        return -1;
    }

    /**
     * LLamado cuando se cambia la linea en el grid de cabecera
     * Guarda la linea de origen (producto que se despieza) y si hace 
     * falta crea la cabecera.
     * @param linea
     * @throws Exception
     */
    private void guardaDesOrig(int linea) throws Exception {
        dtAdd.commit();
        if (deo_codiE.getValorInt() == 0)
            guardaCabOrig();
        
        guardaLinOrig(pro_codiE.getValorInt(),
            deo_ejelotE.getValorInt(), deo_serlotE.getText(),
            pro_codiE.isVendible()?pro_loteE.getValorInt():1,
            pro_codiE.isVendible()?pro_numindE.getValorInt():1, deo_kilosE.getValorDec(), deo_prcostE.getValorDec(),
            dtAdd);
        dtAdd.commit();
        jtCab.setValor(desorli.getId().getDelNumlin(), linea, JTCAB_NL);
    }

    int cambiaLineajtLin(int linea) 
    {
        mensajeErr("", false);
        try
        {
            if (def_kilosE.getValorDec() == 0 )
            {
                if (jtLin.getValorInt(linea,JTLIN_NUMIND)>0)
                {
                    if (!borraLineajtLin(linea))
                        return JTLIN_KILOS;
                    jtLin.setValor(0,linea,JTLIN_NUMIND);
                }
                return -1; // Si NO tengo Kilos paso de todo
            }
            if ( (!pro_codlE.hasCambio() && !def_kilosE.hasCambio() && !def_numpieE.hasCambio()
                &&  !def_feccadE.hasCambio()) || pro_codlE.isNull())
                  return -1; // No Hubo cambios
           
            if (def_kilosE.getValorDec() < 0)
            {
                mensajeErr("Kilos no pueden estar en negativo");
                return 0;
            }
            if (def_numpieE.getValorDec()==0)
            {
               mensajeErr("Numero de Piezas deberia ser 1");
               def_numpieE.setValorDec(1);
               return JTLIN_UNID;
            }
            
            if (def_numpieE.getValorDec()>1 && ! isArgumentoAdmin())
            {
               msgBox("Numero de Piezas deberia ser 1");
               def_numpieE.setValorDec(1);
               return JTLIN_UNID;
            }   
            if (def_prcostE.getValorDec() < 0)
            {
                mensajeErr("Precio de costo no puede estar en negativo");
                return 0;
            }
            if (!pro_codlE.controla(false))
            {
                mensajeErr("Codigo de Producto NO valido");
                return 0;
            }
            if (opAutoClas.isSelected() && tid_codiE.getClasificaPeso())
            {
                int proCodi=MantTipDesp.getProductoPeso(dtStat,pro_codlE.getValorInt(),def_kilosE.getValorDec(),tid_codiE.getValorInt());
                if (proCodi!=0)
                {
                    pro_codlE.setValorInt(proCodi);
                    jtLin.setValor(proCodi,linea,JTLIN_PROCODI);
                    jtLin.setValor(pro_codlE.getNombArt( proCodi),linea,JTLIN_PRONOMB);
                }
            }
           
            
//     if (! pro_codlE.isVendible())
//     {
//         mensajeErr("Producto NO es vendible");
//         return 0;
//     }
            if (!pro_codlE.isActivo())
            {
                mensajeErr("Producto NO esta ACTIVO");
                return 0;
            }
            if (pro_codlE.isVendible() && isComprobarTipoDespiece() && !opSimular.isSelected())
            {
                int proCodi = pro_codiE.getValorInt();
                if (proCodi == 0)
                {
                    for (int n = 0; n < jtCab.getRowCount(); n++)
                    {
                        proCodi = jtCab.getValorInt(0, JTCAB_PROCODI);
                        if (proCodi != 0)
                            break;
                    }
                }
                if (tid_codiE.getValorInt() == MantTipDesp.AUTO_DESPIECE
                    && !MantTipDesp.esEquivalente(pro_codlE.getValorInt(), proCodi, dtStat))
                {
                    mensajeErr("Para auto despieces solo permitidos el mismo producto o NO vendibles");
                    def_kilosE.setValorDec(0);
                    return 0;
                }
                if (tid_codiE.getValorInt() == MantTipDesp.CONGELADO_DESPIECE
                    && !MantTipDesp.esEquivalenteCongelado(proCodi, pro_codlE.getValorInt(), dtStat))
                {
                    mensajeErr("Para Despieces a Congelados solo permitidos equivalentes congelados de productos entrada");
                    def_kilosE.setValorDec(0);
                    return 0;
                }
                if (tid_codiE.getValorInt() != MantTipDesp.AUTO_DESPIECE
                    && tid_codiE.getValorInt() != MantTipDesp.CONGELADO_DESPIECE
                    && !MantTipDesp.checkArticuloSalida(dtAdd, pro_codlE.getValorInt(), tid_codiE.getValorInt()))
                {
                    mensajeErr("Articulo no valido para este tipo despiece");
                    def_kilosE.setValorDec(0);
                    return 0;
                }
            }
            if (deo_fechaE.isNull() || deo_fechaE.getError())
            {
                mensajeErr("Introduzca Fecha de Despiece");
                deo_fechaE.requestFocus();
                return 0;
            }
            
            if (def_feccadE.isNull() || def_feccadE.getError())
            {
                def_feccadE.setText(deo_feccadE.getText());
                jtLin.setValor(deo_feccadE.getText(), linea, 5);
                msgBox("Fecha Caducidad NO valida");
                return JTLIN_FECCAD;
            }
            if (Formatear.comparaFechas(def_feccadE.getDate(), deo_fechaE.getDate())<=0 && pro_codlE.getDiasCaducidad()>0)
            {
                msgBox("Fecha Caducidad  debe ser superior a la del despiece");
                return JTLIN_FECCAD;
            }
            if (Formatear.comparaFechas(def_feccadE.getDate(),deo_fechaE.getDate() )<0 && pro_codlE.getDiasCaducidad()>0)
            {
                int ret=mensajes.mensajeYesNo("Fecha Caducidad  deberia ser superior a la del Despiece. ¿Continuar?");
                if (ret!=mensajes.YES)
                    return JTLIN_FECCAD;
            }
            else
            {
                if (Formatear.comparaFechas(def_feccadE.getDate(), deo_fechaE.getDate())<= MIN_DIAS_CAD && pro_codlE.getDiasCaducidad()>0)
                {
                    int ret=mensajes.mensajeYesNo("Fecha Caducidad  deberia ser superior en 10 dias a la del despiece. Continuar?");
                    if (ret!=mensajes.YES)
                         return JTLIN_FECCAD;
                }
            }
//            if (pro_codlE.hasCambio() || def_kilosE.hasCambio() || def_numpieE.hasCambio() ||  def_feccadE.hasCambio() && pro_codlE.isNull() == false)
//            {
                if (jtLin.getValorInt(linea,JTLIN_NUMIND)!=0)
                {
                        if (!ActualStkPart.checkStock(dtStat, pro_codlE.getCopiaInt(),
                            deo_ejlogeE.getValorInt(), EU.em_cod,
                            deo_selogeE.getText(), deo_nulogeE.getValorInt(),
                            jtLin.getValorInt(linea,JTLIN_NUMIND), deo_almdesE.getValorInt(),
                            def_kilosE.getCopiaDouble(), def_numpieE.getCopiaInt()))
                        {
                            if (! ActualStkPart.checkIndiv(dtStat, pro_codlE.getCopiaInt(),
                                deo_ejlogeE.getValorInt(),
                                deo_selogeE.getText(), deo_nulogeE.getValorInt(),
                                jtLin.getValorInt(linea,JTLIN_NUMIND)))
                            {
                                  enviaMailError("MantDesp. Ind. No encontrado: "+ pro_codlE.getCopiaInt()+" Lote: "+
                                  deo_ejlogeE.getValorInt()+
                                  deo_selogeE.getText()+ deo_nulogeE.getValorInt()+"-"+
                                  jtLin.getValorInt(linea,JTLIN_NUMIND));
                                  msgBox("Individuo NO ENCONTRADO. Hay un error interno");
                            }
                            else
                            {
                                mensajeErr("Individuo ha tenido mvtos. Imposible modificar linea");
                                def_numpieE.resetValor();
                                pro_codlE.resetValor();
                                def_kilosE.resetValor();
                                return 0;
                            }
                        }
                
                }
                mensajeErr("");
                ultFecCaduc=def_feccadE.getDate();
                ponFechas(pro_codlE.getDiasCaducidad(),ultFecCaduc);
                if (!opSimular.isSelected())
                {
                    guardaLineaFin(linea);
                    mensajeErr("Linea " + linea + "... Guardada");
                }
            
               
                if (opImpEt.isSelected() && pro_codlE.isVendible() && pro_codlE.getEtiCodi() >= 0
                    && jtLin.isEnabled()) // && pro_codlE.getConStkInd())
                {                                        
                    imprEtiq(linea, ultCodigoEtiqueta);
                }
//            }
        } catch (Exception ex)
        {
            Error("Error al Cambiar Linea de Grid", ex);
        }
        return -1;
    }

    /**
     * Retorna true si esta activa la variable CHECKTIDCODI o tipo despiece no es LIBRE_DESPIECE
     * @return
     */
    boolean isComprobarTipoDespiece() {
        if (!CHECKTIDCODI)
            return false;
        return tid_codiE.getValorInt() != MantTipDesp.LIBRE_DESPIECE;
    }

    boolean genDatEtiq() throws Exception {
        int prvDespiece = pdconfig.getPrvDespiece(EU.em_cod, dtStat);
        pro_codiE.getNombArt(jtCab.getValString(0, JTCAB_PROCODI), EU.em_cod);
        if (!utdesp.busDatInd(jtCab.getValString(0, JTCAB_SERLOT), jtCab.getValorInt(0, JTCAB_PROCODI),
            EU.em_cod,
            jtCab.getValorInt(0, JTCAB_EJELOT),
            jtCab.getValorInt(0, JTCAB_NUMLOT),
            jtCab.getValorInt(0, JTCAB_NUMIND), // N. Ind.
            0, // deo_almoriE.getValorInt(),
            dtCon1, dtStat, EU))
        {
            mensajeErr(utdesp.getMsgAviso());
            if (prv_codiE.isNull())
                prv_codiE.setValorInt(prvDespiece);
             return true;
        }
        crotal=utdesp.getNumeroCrotal();
        if (prv_codiE.isNull())
        {
            if (tid_codiE.getValorInt() == MantTipDesp.AUTO_DESPIECE
                || tid_codiE.getValorInt() == MantTipDesp.CONGELADO_DESPIECE
                || tid_codiE.isNull())
                prv_codiE.setValorInt(utdesp.prvCodi);
             else
                prv_codiE.setValorInt(prvDespiece);
        }
        if (nav.pulsado != navegador.ADDNEW || deo_desnueE.isPulsado())
        {            
            utdesp.setDespieceNuestro(deo_desnueE.isSelected());
            return true;
        }
        if (! deo_desnueE.isPulsado())
        {
            if (prv_codiE.getValorInt() == prvDespiece)
            {
                deo_desnueE.setSelected(true);
                utdesp.setDespieceNuestro(deo_desnueE.isSelected());
            }
            else
               deo_desnueE.setSelected(false);
        }
        return true;
    }

    /**
     * Guarda lineas finales de despiece. (v_despfin)
     * @param linea
     */
    void guardaLineaFin(int linea) 
    {
        try
        {
            dtAdd.commit(); // Para resetear el current_timestamp
            int nInd = jtLin.getValorInt(linea, JTLIN_NUMIND);
            int nOrd = jtLin.getValorInt(linea, JTLIN_ORDEN);
            tid_codiE.setEnabled(false);
            deo_lotnueE.setEnabled(false);
//     debug("En guarda Linea: "+linea);
            if (nInd == 0)
            {
                if (getNuLiDes(true) == 1 && deo_lotnueE.getValorInt()!= FORZAR_LOTE)
                {
                    swMantLote = true;
                    if (tid_codiE.getValorInt() == MantTipDesp.AUTO_DESPIECE || tid_codiE.getValorInt() == MantTipDesp.CONGELADO_DESPIECE)
                    {
                        int numLote = 0;
                        for (int n = 0; n < jtCab.getRowCount(); n++)
                        {
                            if (jtCab.getValorInt(n, 0) == 0)
                                continue;
                           
                            if (numLote == 0)
                                numLote = jtCab.getValorInt(n, JTCAB_NUMLOT);
                            if (numLote != jtCab.getValorInt(n, JTCAB_NUMLOT))
                            {
                                swMantLote = false;
                                break;
                            }
                        }
                    } else
                    {
                        swMantLote = false;
                    }
                    if (swMantLote)
                    {
                        deo_lotnueE.setValor(0);
                        ponLoteOri();
                    }

                }
                nInd = utildesp.getMaxNumInd(dtAdd, pro_codlE.getValorInt(),
                    deo_ejlogeE.getValorInt(), EU.em_cod,
                    deo_selogeE.getText(), deo_nulogeE.getValorInt());
                utdesp.setGrupoDesp(deo_numdesE.getValorInt());
                nOrd = guardaLinDespFin(deo_ejlogeE.getValorInt(), EU.em_cod,
                    deo_selogeE.getText(), deo_nulogeE.getValorInt(), nInd,
                    pro_codlE.getValorInt(),
                    def_kilosE.getValorDec(), def_numpieE.getValorInt(), def_unicajE.getValorInt(),
                    def_feccadE.getDate(), 0);
//                insStkPart(pro_codlE.getValorInt(), deo_ejlogeE.getValorInt(),
//                    EU.em_cod,
//                    deo_selogeE.getText(), deo_nulogeE.getValorInt(),
//                    nInd, def_kilosE.getValorDec());
            } 
            else
            {
//                if (proCodAnt!=0)
//                    anuStkPart(proCodAnt, deo_ejlogeE.getValorInt(),
//                        EU.em_cod,
//                        deo_selogeE.getText(), deo_nulogeE.getValorInt(),
//                        nInd, defKilAnt, 1, deo_almoriE.getValorInt());
////                else
////                {
////                    enviaMailError("Anulo stock partidas con producto=0"+ ventana.getCurrentStackTrace());
////                }
                guardaLinDespFin(deo_ejlogeE.getValorInt(), EU.em_cod,
                    deo_selogeE.getText(), deo_nulogeE.getValorInt(), nInd,
                    pro_codlE.getValorInt(), def_kilosE.getValorDec(),
                    def_numpieE.getValorInt(), def_unicajE.getValorInt(),
                    def_feccadE.getDate(), nOrd);//
//                insStkPart(pro_codlE.getValorInt(), deo_ejlogeE.getValorInt(),
//                    EU.em_cod,
//                    deo_selogeE.getText(), deo_nulogeE.getValorInt(),
//                    nInd, def_kilosE.getValorDec());
            }
            jtLin.setValor("" + nInd, linea, 6);
            jtLin.setValor("" + nOrd, linea, 7);
            ctUp.commit();
           
        } catch (Exception ex)
        {
            Error("Error al Guardar Datos Despiece", ex);
        }

    }

    /**
     * Imprime la etiqueta
     * @param linea Linea de despiece
     * @param fecCad Fecha Caducidad
     * @param personal Cambia el nombre de la empresa por el que se elija.
     * Tipo etiqueta:
     */
    void imprEtiq(int linea, int tipoEtiq) {
        try
        {          
            String nombArt = pro_codlE.getNombArt(pro_codlE.getText());
            if (pro_codlE.getLikeProd().isNull("pro_codeti"))
                proCodeti = 0;
            else         
                proCodeti = pro_codlE.getLikeProd().getInt("pro_codeti");
        
//     debug("Nombre Articulo: "+nombArt);
            utdesp.iniciar(dtAdd, eje_numeE.getValorInt(), EU.em_cod, deo_almdesE.getValorInt(),
                deo_almoriE.getValorInt(), EU);
            if (tipoEtiq !=0 && tipoEtiq !=etiqueta.getCodigoEtiqInterior(EU) &&  cli_codiE.getValorInt()!=0)
                utdesp.setDirEmpresa(cli_codiE.getTextNomb());
            else
                utdesp.setDirEmpresa(null);
            if (!utdesp.busDatInd(deo_selogeE.getText(),pro_codlE.getValorInt(),
                EU.em_cod,
                deo_ejlogeE.getValorInt(),
                deo_nulogeE.getValorInt(),
                jtLin.getValorInt(linea, JTLIN_NUMIND), // N. Ind.
                0, // deo_almoriE.getValorInt(),
                dtCon1, dtStat, EU))
            {
                msgBox(utdesp.getMsgAviso());
            }
          
            utdesp.imprEtiq(tipoEtiqueta, 
                dtStat, pro_codlE.getValorInt(), nombArt,
                "D",
                opSimular.isSelected() ? jtCab.getValorInt(0, JTCAB_NUMLOT) : deo_nulogeE.getValorInt(),
                opSimular.isSelected() ? jtCab.getValString(0, JTCAB_EJELOT) : deo_ejlogeE.getText(),
                opSimular.isSelected() ? jtCab.getValString(0, JTCAB_SERLOT) : deo_selogeE.getText(),
                opSimular.isSelected() ? jtCab.getValorInt(0, JTCAB_NUMIND) : jtLin.getValorInt(linea, JTLIN_NUMIND),
                jtLin.getValorDec(linea, JTLIN_KILOS),
                deoFecpro,
                deoFecpro==null ? deo_fechaE.getDate() : deoFecpro,
                jtLin.getValDate(linea, JTLIN_FECCAD),
                deoFecSacr,//jtLin.getValDate(linea,5,def_feccadE.getFormato()),
                jtLin.getValDate(linea, JTLIN_FECCAD),deo_numdesE.getValorInt());
            mensajeErr("Etiqueta ... Listada");
        } catch (Throwable ex)
        {
            Error("Error al Guardar Datos Despiece", ex);
        }
    }

    void imprEtiqInt() {
        try
        {
            if (deo_nulogeE.getValorDec() == 0)
            {
                mensajeErr("Despiece sin Numero Lote asignado");
                return;
            }
            if (jtCab.isVacio())
            {
                mensajeErr("Introduce Algun producto de Entrada");
                return;
            }
            if (numCopiasE.getValorInt() == 0)
            {
                mensajeErr("Introduzca Numero de Etiquetas a sacar");
                numCopiasE.requestFocus();
                return;
            }
            buscaDatInd();
            int nRow=jtLin.isEnabled()?jtLin.getSelectedRow():jtLin.getSelectedRowDisab();
            int proCodi=jtLin.getValorInt(nRow, JTLIN_PROCODI);
            String proNomb=jtLin.getValString(nRow, JTLIN_PRONOMB);
            CodigoBarras codBarras = new CodigoBarras("D",eje_numeE.getText(),deo_selogeE.getText(),
                deo_nulogeE.getValorInt(),proCodi,0,0,deo_numdesE.getValorInt()); 
            
//            java.util.Date fecCong=utildesp.getDateCongelado(proCodi, deo_fecproE.getDate(), dtStat);    
            etiq.setCongelado(MantArticulos.isCongelado(proCodi, dtStat));
            etiq.iniciar(codBarras.getLote(),
               codBarras.getLote(),
                ""+proCodi, proNomb, MantPaises.getNombrePais(utdesp.getPaisNacimiento(),dtStat),
                MantPaises.getNombrePais(utdesp.getPaisEngorde(),dtStat),
                utdesp.getDespiezado(),
                null,
                0, utdesp.getConservar(),
                MantPaises.getNombrePais(utdesp.getPaisSacrificio(),dtStat),
                null,
                deoFecpro,
                def_feccadE.getDate(), 
                deoFecSacr);
            etiq.setNumCopias(numCopiasE.getValorInt());
            etiq.listar(etiquetaInterior);
        
//     this.setEnabled(true);
            mensaje("");
            mensajeErr("Etiqueta Interior ... Listada");
        } catch (Throwable ex)
        {
            Error("Error al Guardar Datos Despiece", ex);
        }
    }

    boolean buscaDatInd() throws Exception {
        if (etiq == null)
        {
            etiq = new etiqueta(EU);
        }

        if (utdesp.cambio)
        {
            genDatEtiq();
        }

        utdesp.cambio = false;

        return true;
    }

    /**
     *
     * @param dt DatosTabla conexion para realiar la select.
     * @throws Exception Error BD
     * @return int Proximo Numero De despiece
     */
    int incNumDesp(DatosTabla dt) throws Exception {
        int numDesp = utildesp.incNumDesp(dt, EU.em_cod, eje_numeE.getValorInt());
        dt.commit();
        return numDesp;
    }

    int borraLinDesp(int defOrden) throws SQLException {
        s = "SELECT * FROM v_despfin "
            + " WHERE eje_nume = " + eje_numeE.getValorInt()
            + " and deo_codi = " + deo_codiE.getValorInt()
            + " and def_orden = " + defOrden;
        if (!dtAdd.select(s, true))
            return 0;
                
//        if (anuStkPart(dtAdd.getInt("pro_codi"), dtAdd.getInt("def_ejelot"), EU.em_cod,
//            dtAdd.getString("def_serlot"), dtAdd.getInt("pro_lote"),
//            dtAdd.getInt("pro_numind"), dtAdd.getDouble("def_kilos"),
//            dtAdd.getInt("def_numpie"), deo_almdesE.getValorInt()) == 0)
//        {
//            msgBox("No encontrado apunte en Stock-Partidas");
//            return 0;
//        }
        
        s = "DELETE FROM v_despfin " 
            + " WHERE eje_nume = " + eje_numeE.getValorInt()
            + " and deo_codi = " + deo_codiE.getValorInt()
            + " and def_orden = " + defOrden;

        return stUp.executeUpdate(s);
    }

    void anuLinDesp(int ejeLot, int empLot, String serLot, int numLot, int nInd) throws Exception {
        s = "DELETE FROM v_despfin "
            + " WHERE eje_nume = " + eje_numeE.getValorInt()
            + " and def_ejelot = " + ejeLot
            + " and def_emplot = " + empLot
            + " and def_serlot = '" + serLot + "'"
            + " and pro_lote = " + numLot
            + " and pro_numind = " + nInd;
        stUp.executeUpdate(s);
    }

    /**
     * Guarda Lineas de final de despiece
     * @param ejeLot
     * @param empLot
     * @param serLot
     * @param numLot
     * @param nInd
     * @param proCodi
     * @param kilos
     * @param numPiezas
     * @param uniCaj
     * @param feccad
     * @param defOrden
     * @return
     * @throws Exception
     */
    int guardaLinDespFin(int ejeLot, int empLot, String serLot, int numLot, int nInd,
        int proCodi, double kilos, int numPiezas, int uniCaj, Date feccad, int defOrden) throws Exception 
    {   
        if (desorca!=null )
        {
            if (desorca.getDeoFeccad()==null)
            { // Guardo Datos trazabilidad
                desorca.setDeoFecsac(deo_fecsacE.getDate());
                desorca.setDeoFecpro(deo_fecproE.getDate());
                desorca.setDeoFeccad(deo_feccadE.getDate());
                desorca.update(dtAdd);
            }
        }
        deo_almdesE.setEnabled(false);
        pro_codlE.getNombArt(proCodi);
        utdesp.iniciar(dtAdd, eje_numeE.getValorInt(), EU.em_cod,
            deo_almdesE.getValorInt(), deo_almoriE.getValorInt(), EU);
        utdesp.setTipoProduccion(deo_incvalE.getValor());
        
        int ret= utdesp.guardaLinDesp(ejeLot, empLot, serLot, numLot, nInd, deo_codiE.getValorInt(), proCodi,
            kilos, numPiezas, feccad, defOrden, uniCaj,
            MODPRECIO ? def_prcostE.getValorDec() : 0,
            Integer.parseInt(deo_cerraE.getSelecion()));
        if (uniOrigE.getValorInt()>1 && pro_codlE.getNumeroCrotales()>1 )
        {// Compruebo si tengo que poner otro numero crotal
           s="select count(*) as cuantos from stockpart where pro_nupar="+ numLot+
               " and pro_serie='"+serLot+"' "+
               " and eje_nume="+ejeLot+
               " and pro_codi = "+proCodi+
               " and stp_nucrot='"+crotal+"'";
           dtStat.select(s);
           if (dtStat.getInt("cuantos",true)>= pro_codlE.getNumeroCrotales() )
                crotal=MantAlbComCarne.getRandomCrotal(crotal,EU);              
           utdesp.setNumeroCrotal(crotal);       
        }
        s="update stockpart set stp_nucrot='"+crotal+"'"+
                ", stp_traaut = 0 "+
                ", stp_painac='"+utdesp.getPaisNacimiento()+"'"+
                ", stp_paisde='"+utdesp.getPaisSalaDespiece()+"'"+
                ", stp_engpai='"+utdesp.getPaisEngorde()+"'"+
                ", stp_paisac='"+utdesp.getPaisSacrificio()+"'"+
                ", stp_matad='"+utdesp.getMatadero()+"'"+
                ", stp_saldes='"+utdesp.getSalaDespiece()+"'"+            
                ", stp_fecpro='"+Formatear.getFechaDB(deoFecpro)+"'"+
                (deoFecSacr==null?", stp_fecsac = null":", stp_fecsac = '"+Formatear.getFechaDB(deoFecSacr)+"'")+
               " where pro_nupar="+ numLot+
                " and pro_serie='"+serLot+"' "+
                " and eje_nume="+ejeLot+
                " and pro_codi = "+proCodi+
                " and pro_numind="+nInd;
        dtAdd.executeUpdate(s);

        return ret;
    }
    /**
     * Establece las fechas de produccion y sacrificio, segun la fecha caducidad y dias 
     * @param diasCad
     * @param fecCaduc
     * @throws ParseException 
     */
    void ponFechas(int diasCad,Date fecCaduc) throws ParseException
    {
        if (Formatear.comparaFechas(fecCaduc, deo_feccadE.getDate() )==0)
        {
            deoFecpro=deo_fecproE.getDate();
            deoFecSacr=deo_fecsacE.getDate();
        }
        else
        {
            deoFecpro= Formatear.sumaDiasDate(fecCaduc,diasCad*-1);
            if (deo_fecsacE.isNull())
                deoFecSacr=null;
            else
                deoFecSacr=Formatear.sumaDiasDate(fecCaduc,(diasCad+2)*-1);
        }
   }
    /**
     * Establece el numero de lote cuando hay varios productos
     * Pongo como numero de Lote el del Grupo con la serie "G"
     */
   private void ponLoteNuevo() {
        deo_ejlogeE.setValorDec(eje_numeE.getValorInt());
        deo_selogeE.setText(SERIE);
        deo_nulogeE.setValorDec(deo_codiE.getValorInt());
    }

    /**
     * Pone el Lote de salida igual al del primer
     * Producto a despiezar.
     */
    private void ponLoteOri() {
        for (int n = 0; n < jtCab.getRowCount(); n++)
        {
            if (jtCab.getValorInt(n, JTCAB_PROCODI) == 0)
            {
                continue;
            }
            deo_ejlogeE.setValorDec(jtCab.getValorInt(n, JTCAB_EJELOT));
            deo_selogeE.setText(jtCab.getValString(n, JTCAB_SERLOT));
            deo_nulogeE.setValorDec(jtCab.getValorInt(n, JTCAB_NUMLOT));
            break;
        }
        deo_desnueE.setSelected(false);
    }
    void guardaCabOrig() throws SQLException, ParseException {
        guardaCabOrig(0);
    }
    void guardaCabOrig(int deoCodi) throws SQLException, ParseException {
        desorca = new Desporig();
        deo_codiE.setValorInt(deoCodi==0?utildesp.incNumDesp(dtAdd, EU.em_cod, eje_numeE.getValorInt()):deoCodi);
        desorca.setId(new DesporigId(eje_numeE.getValorInt(), deo_codiE.getValorInt()));
       
        setValDesorca();
        desorca.save(dtAdd, eje_numeE.getValorInt(), EU);
        setBloqueo(dtAdd, TABLA_BLOCK,
            eje_numeE.getValorInt()
            + "|" + deo_codiE.getValorInt(), false);
        deo_almoriE.setEnabled(false);
    }

    /**
     * Llamada tanto en cambio de linea en el grid, como al guardar el registro (ej_addnew y ej_edit)
     * @param proCodi
     * @param ejeLot
     * @param serLot
     * @param numLot
     * @param numInd
     * @param kilos
     * @param dtAdd
     * @throws Exception
     */
    void guardaLinOrig(int proCodi, int ejeLot, String serLot, int numLot,
        int numInd, double kilos, double precio, DatosTabla dtAdd) throws Exception {

        desorli = new gnu.chu.anjelica.sql.Desorilin();
        desorli.setId(new DesorilinId(eje_numeE.getValorInt(), deo_codiE.getValorInt(), 0));
        desorli.getId().setDelNumlin(desorli.getNextLinea(dtAdd));
        desorli.setDeoEjelot(ejeLot);
        desorli.setDeoKilos(kilos);
        desorli.setDeoPrcost(precio);
        //despOriLin.setDeoPrusu(null);
        desorli.setDeoSerlot(serLot.charAt(0));
        desorli.setProCodi(proCodi);
        desorli.setProLote(numLot);
        desorli.setProNumind(numInd);
        
        desorli.save(dtAdd);
//        try
//        {
//            stkPart.restar(ejeLot, serLot, numLot, numInd, proCodi,
//                deo_almoriE.getValorInt(), kilos, 1);
//        } catch (java.sql.SQLWarning k)
//        {
//            enviaMailError("Usuario: " + EU.usuario + "\nNO SE Pudo restar stock en pddespie: "
//                + " deo_codi: " + deo_codiE.getValorInt() + "\n" + k.getMessage());
//        }
    }
    
//    /**
//     * Actualiza las fechas de produccion y sacrificio en stock-partidas.
//     * @throws SQLException 
//     */
//    void actFechaStock() throws SQLException,ParseException
//    {
//       actFechaStock(dtAdd,deo_fecproE.getDate(),deo_fecsacE.getDate(),eje_numeE.getValorInt(),deo_codiE.getValorInt(),EU.em_cod);
//    }
//    public static void actFechaStock(DatosTabla dt,Date fecProd, Date fecSacr,int ejeNume,int deoCodi,int empCodi) throws SQLException,ParseException
//    {
//       String s="update stockpart set stp_fecpro='"+Formatear.getFechaDB(fecProd)+"'"+
//         " , stp_fecsac="+(fecSacr==null?"null ":"'"+Formatear.getFechaDB(fecSacr)+ "'")+
//         " where exists (select * from v_despfin as d where d.pro_codi = stockpart.pro_codi"+
//         " and d.pro_lote=stockpart.pro_nupar"+
//         " and d.pro_numind=stockpart.pro_numind"+
//         " and d.def_serlot=stockpart.pro_serie"+
//         " and d.def_ejelot=stockpart.eje_nume"+
//         " and d.eje_nume="+ejeNume+
//         " and deo_codi="+deoCodi+
//         "and d.emp_codi="+empCodi+
//         ")";
//         dt.executeUpdate(s);
//    }        
    /**
     * Actualiza cabecera de despiece
     * @throws SQLException
     */
    void actualCabDesp() throws SQLException, ParseException {
        if (deo_codiE.getValorInt()==0)
        { // No encontrado Numero de despiece.
            enviaMailError("Numero despiece sin asignar al intentar actualizarlo"+
                    "N. lineas Cabecera: "+jtCab.getRowCount()+
                    "Producto: "+jtCab.getValorInt(0,JTCAB_PROCODI)+
                    "-"+jtCab.getValorInt(0,JTCAB_EJELOT)+
                    jtCab.getValString(0,JTCAB_SERLOT)+
                    jtCab.getValorInt(0,JTCAB_NUMLOT)+"/"+
                    jtCab.getValorInt(0,JTCAB_NUMIND)+
                    " KG: "+jtCab.getValorDec(0,JTCAB_KILOS));
        }
        actKilos(JTCAB_GRID);
        desorca = new Desporig();
        desorca.setId(new DesporigId(eje_numeE.getValorInt(), deo_codiE.getValorInt()));
        setValDesorca();
        desorca.update(dtAdd);
    }

    void setValDesorca() throws SQLException, ParseException {
        desorca.setCliente(cli_codiE.getValorInt());
        desorca.setDeoNumdes(deo_numdesE.getValorInt());
        desorca.setDeoFecha(deo_fechaE.getDate());
        desorca.setTidCodi(tid_codiE.getValorInt());
        desorca.setDeoFeccad(deo_feccadE.getDate());
        desorca.setDeoFecpro(deo_fecproE.getDate() == null ? deo_fechaE.getDate() : deo_fecproE.getDate());
        desorca.setDeoFecsac(deo_fecsacE.getDate());
        desorca.setPrvCodi(prv_codiE.getValorInt());
        desorca.setDeoDesnue(deo_desnueE.getSelecionChar());
        desorca.setDeoEjloge(deo_ejlogeE.getValorInt());
        desorca.setDeoSeloge(deo_selogeE.getText());
        desorca.setDeoNuloge(deo_nulogeE.getValorInt());
        desorca.setDeoAlmori(deo_almoriE.getValorInt());
        desorca.setDeoAlmdes(deo_almdesE.getValorInt());
        desorca.setDeoLotnue(new Short(deo_lotnueE.getValor()));
        desorca.setDeoCerra(new Short(deo_cerraE.getSelecion()));
        desorca.setDeoIncval(deo_incvalE.getValor());
        desorca.setDeoBlock(deo_blockE.getValor());        
        desorca.setDeoValor("N");
        desorca.setAnularControl(opAnularControl.isSelected()?1:0);
    }
// /**
//  * Actualiza grupo de despiece
//  * @param empCodi
//  * @param ejeNume
//  * @param grdNume
//  * @return true si se actualizo. False, si no lo encontro
//  * @throws SQLException
//  * @throws ParseException
//  */
// boolean actDatGruDesp( int ejeNume, int grdNume) throws SQLException, ParseException
// {
//   s = "select * from grupdesp WHERE eje_nume = " + ejeNume +
//       " and grd_nume = " + grdNume;
//   if (!dtBloq.select(s, true))
//     return false;
//
//   dtBloq.edit();
//   dtBloq.setDato("grd_fecpro",deo_fecproE.getDate());
//   dtBloq.setDato("grd_feccad", deo_feccadE.getText(), "dd-MM-yyyy");
//   dtBloq.setDato("prv_codi", prv_codiE.getValorInt());
//   dtBloq.setDato("grd_block", deo_blockE.getValor());
//   dtBloq.setDato("grd_desnue", deo_desnueE.isSelected() ? "S" : "N");
//   dtBloq.update(stUp);
//   return true;
// }

    /**
     * Busca los datos de un despiece en un vector
     *
     * @param dat vector con los datos
     * @param nLin Numero de Linea activa
     * @return posicion donde lo ha encontrado.
     */
    int buscaDesp(Vector dat, int nLin) {

        int nl = dat.size();
        for (int n = 0; n < nl; n++)
        {
            Vector v = (Vector) dat.elementAt(n);
            if (igualInt(v.elementAt(2).toString(), jtCab.getValString(nLin, 0)) && // Prod.
                igualInt(v.elementAt(3).toString(), jtCab.getValString(nLin, 2)) && // emp
                igualInt(v.elementAt(4).toString(), jtCab.getValString(nLin, 3)) && // ejer
                v.elementAt(5).toString().equals(jtCab.getValString(nLin, 4)) && // Serie
                igualInt(v.elementAt(6).toString(), jtCab.getValString(nLin, 5)) && // Lote
                igualInt(v.elementAt(7).toString(), jtCab.getValString(nLin, 6)) && // Ind
                igualDouble(v.elementAt(8).toString(), jtCab.getValString(nLin, 7))) // Peso
            {
                v.set(0, "E");
                dat.set(n, v);
                return n;
            }
        }
        return -1;
    }

    boolean igualInt(String val1, String val2) {
        return Integer.parseInt(val1.trim()) == Integer.parseInt(val2.trim());
    }

    boolean igualDouble(String val1, String val2) {
        return Double.parseDouble(val1.trim()) == Double.parseDouble(val2.trim());
    }
    /**
     * Borrar Linea de salida
     * @param row
     * @return 
     */
    boolean borraLineajtLin(int row) {
        if (jtLin.getValorInt(row, JTLIN_NUMIND) == 0)        
            return true;

        try
        {
            if (!ActualStkPart.checkStock(dtStat, pro_codlE.getCopiaInt(),
                deo_ejlogeE.getValorInt(), EU.em_cod,
                deo_selogeE.getText(), deo_nulogeE.getValorInt(),
                jtLin.getValorInt(row,JTLIN_NUMIND), deo_almdesE.getValorInt(),
                def_kilosE.getCopiaDouble(), def_numpieE.getCopiaInt()))
            {
                if (! ActualStkPart.checkIndiv(dtStat, pro_codlE.getCopiaInt(),
                    deo_ejlogeE.getValorInt(),
                    deo_selogeE.getText(), deo_nulogeE.getValorInt(),
                    jtLin.getValorInt(row,JTLIN_NUMIND)))
                {
                     enviaMailError("MantDesp. Ind. No encontrado: "+ pro_codlE.getCopiaInt()+" Lote: "+
                        deo_ejlogeE.getValorInt()+
                        deo_selogeE.getText()+ deo_nulogeE.getValorInt()+"-"+
                        jtLin.getValorInt(row,JTLIN_NUMIND));
                     msgBox("Individuo NO ENCONTRADO. Hay un error interno");
                     return false;
                }
                msgBox("Individuo ha tenido mvtos. Imposible borrar linea");
                return false;
            }
                
            if (borraLinDesp(jtLin.getValorInt(row, JTLIN_ORDEN)) == 0)
            {
                msgBox("Linea de Despiece no encontrada");
                return false;
            }

            ctUp.commit();
        } catch (SQLException ex)
        {
            Error("Error al borrar Linea", ex);
        }
        return true;
    }
//    /**
//     * Inserta registro de stock 
//     * @param proCodi
//     * @param ejeLot
//     * @param empLot
//     * @param serLot
//     * @param numLot
//     * @param nInd Numero de Individuo 
//     * @param kilos Kilos a Insertar.
//     * @throws Exception 
//     */
//    void insStkPart(int proCodi, int ejeLot, int empLot, String serLot, int numLot, int nInd, double kilos) throws Exception {
//        stkPart.sumar(ejeLot, serLot, numLot, nInd, proCodi, deo_almdesE.getValorInt(),
//            kilos, 1, deo_fechaE.getText(), actStkPart.CREAR_SI,
//            prv_codiE.getValorInt(), deo_feccadE.getDate());
//
//    }

//    int anuStkPart(int proCodi, int ejeLot, int empLot, String serLot, int numLot, int nInd, double kilos, int unid, int almCodi) throws SQLException {
//        return anuStkPart(proCodi, ejeLot, empLot, serLot, numLot, nInd, kilos, unid, almCodi, false);
//    }
//
//    int anuStkPart(int proCodi, int ejeLot, int empLot, String serLot, int numLot, int nInd, double kilos, int unid, int almCodi, boolean anulaBloqueo) throws SQLException {
//        if (anulaBloqueo)
//        {
//            stkPart.setBloqueo(proCodi, ejeLot,
//                empLot,
//                serLot, numLot,
//                nInd, almCodi, false);
//        }
//        return stkPart.anuStkPart(proCodi, ejeLot, empLot, serLot, numLot,
//            nInd, almCodi, kilos, unid);
//    }

     
 
  
  
    /**
     * Actualiza Total de kilos
     * @param grid Número de grid a actualizar (0: TODOS)
     */
    void actKilos(int grid) throws SQLException
    {
        int nRow = jtCab.getRowCount();
        double kg = 0;
        int nLin = 0;
        if (grid == JTCAB_GRID || grid == 0)
        {
            for (int n = 0; n < nRow; n++)
            {
                if (jtCab.getValorInt(n, JTCAB_PROCODI) == 0)
                    continue;
                if (MantArticulos.isVendible(jtCab.getValorInt(n, JTCAB_PROCODI) , dtStat))
                    nLin++;
                kg += jtCab.getValorDec(n, JTCAB_KILOS);
            }
            kgOrigE.setValorDec(kg);
            uniOrigE.setValorDec(nLin);
        }
        kg = 0;
        nLin = 0;
        if (grid == JTLIN_GRID || grid == 0)
        {
            nRow = jtLin.getRowCount();
            for (int n = 0; n < nRow; n++)
            {
                if (jtLin.getValorDec(n, JTLIN_KILOS) == 0)
                    continue;
                kg += jtLin.getValorDec(n, JTLIN_KILOS);
                nLin++;
            }
            kgFinE.setValorDec(kg);
            uniFinE.setValorDec(nLin);

        }
        kgDifE.setValorDec(kgOrigE.getValorDec() - kgFinE.getValorDec());
    }

// void ej_consLote()
// {
//   if (ayuLot.consulta)
//   {
//     deo_ejlogeE.setValorDec(ayuLot.jt.getValorInt(ayuLot.rowAct, ayuLote.JT_EJE));
//     deo_selogeE.setText(ayuLot.jt.getValString(ayuLot.rowAct,ayuLote.JT_SER));
//     pro_loteE.setValorDec(ayuLot.jt.getValorInt(ayuLot.rowAct,ayuLote.JT_LOTE));
//     pro_numindE.setValorDec(ayuLot.jt.getValorInt(ayuLot.rowAct,ayuLote.JT_IND));
//     jtCab.setValor(ayuLot.jt.getValString(ayuLot.rowAct,ayuLote.JT_EJE),JTCAB_EJELOT);
//     jtCab.setValor(ayuLot.jt.getValString(ayuLot.rowAct,ayuLote.JT_SER),JTCAB_SERLOT);
//     jtCab.setValor(ayuLot.jt.getValString(ayuLot.rowAct,ayuLote.JT_LOTE),JTCAB_NUMLOT);
//     jtCab.setValor(ayuLot.jt.getValString(ayuLot.rowAct,ayuLote.JT_IND),JTCAB_NUMIND);
//     jtCab.setValor(ayuLot.jt.getValString(ayuLot.rowAct,ayuLote.JT_PESO),JTCAB_KILOS );
//     deo_kilosE.setText(ayuLot.jt.getValString(ayuLot.rowAct,ayuLote.JT_PESO));
//   }
//   ayuLot.setVisible(false);
//   this.setEnabled(true);
//   this.setFoco(null);
////   this.toFront();
//   try
//   {
//     this.setSelected(true);
//   }
//   catch (Exception k) {}
//       SwingUtilities.invokeLater(new Thread()
//       {
//         @Override
//         public void run()
//         {
//           jtCab.requestFocus(jtCab.getSelectedRow(), 5);
//         }
//       });
// }
    public static void deoBlockE_addItem(CComboBox grdBlock) {
        grdBlock.addItem("Abierto", "S");
        grdBlock.addItem("Cerrado", DESP_CERRADO);      
        grdBlock.addItem("Bloqueado", "B");
    }

    @Override
    public void matar(boolean cerrCon) {
        try
        {
            if (datTrazFrame!=null)
                datTrazFrame.dispose();
            if (cerrCon)
            {
                if (ctProd != null && ctProd.isConectado())
                {
                    ctProd.close();
                }
            }
        } catch (SQLException k)
        {
            debug("Error al cerrar conexion ctProd");
        }
//   if (ayuLot!=null)
//     ayuLot.dispose();
        super.matar(cerrCon);
    }

    @Override
    public void rgSelect() throws SQLException {
        super.rgSelect();
        if (!dtCons.getNOREG())
        {
            dtCons.last();
            nav.setEnabled(navegador.ULTIMO, false);
            nav.setEnabled(navegador.SIGUIENTE, false);
        }
        //verDatos(dtCons);
    }

    @Override
    public boolean Error(String msg, Throwable k) {
        msg = "Despiece: " + eje_numeE.getValorInt() + "/"
            + deo_codiE.getValorInt() + " Grupo: " + deo_numdesE.getValorInt() + "\n" + msg;
        return super.Error(msg, k);
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

        pro_numindE = new gnu.chu.controles.CTextField(Types.DECIMAL, "###9");
        pro_codiE = new gnu.chu.camposdb.proPanel()
        {
            @Override
            public void afterFocusLost(boolean correcto)
            {
                if (! correcto)
                return;
                deo_kilosE.setEnabled( !this.hasControlIndiv());
            }
            @Override
            protected void despuesLlenaCampos()
            {
                jtCab.procesaAllFoco();
                jtCab.mueveSigLinea();
            }
            public void afterSalirLote(ayuLote ayuLot)
            {
                if (ayuLot.consulta)
                {
                    jtCab.setValor(ayuLot.jt.getValString(ayuLot.rowAct,ayuLote.JT_EJE),JTCAB_EJELOT);
                    jtCab.setValor(ayuLot.jt.getValString(ayuLot.rowAct,ayuLote.JT_SER),JTCAB_SERLOT);
                    jtCab.setValor(ayuLot.jt.getValString(ayuLot.rowAct,ayuLote.JT_LOTE ),JTCAB_NUMLOT);
                    jtCab.setValor(ayuLot.jt.getValString(ayuLot.rowAct,ayuLote.JT_IND),JTCAB_NUMIND);
                    jtCab.setValor(ayuLot.jt.getValString(ayuLot.rowAct,ayuLote.JT_PESO),JTCAB_KILOS);
                }
            }
        }
        ;
        pro_nocabE = new gnu.chu.controles.CTextField();
        deo_ejelotE = new gnu.chu.controles.CTextField(Types.DECIMAL, "###9");
        deo_serlotE = new gnu.chu.controles.CTextField(Types.CHAR, "X",1);
        pro_loteE = new gnu.chu.controles.CTextField(Types.DECIMAL, "####9");
        deo_kilosE = new gnu.chu.controles.CTextField(Types.DECIMAL, "---,--9.99");
        deo_prcostE = new gnu.chu.controles.CTextField(Types.DECIMAL, "---9.9999");
        pro_codlE = new gnu.chu.camposdb.proPanel()
        {
            @Override
            protected void despuesLlenaCampos()
            {
                jtCab.procesaAllFoco();
                jtCab.mueveSigLinea();
            }
        }
        ;
        def_kilosE = new gnu.chu.controles.CTextField(Types.DECIMAL, "---,--9.99");
        def_numpieE = new gnu.chu.controles.CTextField(Types.DECIMAL, "##9");
        def_feccadE = new gnu.chu.controles.CTextField(Types.DATE, "dd-MM-yyyy");
        def_numindE = new gnu.chu.controles.CTextField(Types.DECIMAL, "###9");
        def_ordenE = new gnu.chu.controles.CTextField(Types.DECIMAL, "###9");
        def_unicajE = new gnu.chu.controles.CTextField(Types.DECIMAL, "##9");
        pro_nombE = new gnu.chu.controles.CTextField();
        def_prcostE = new gnu.chu.controles.CTextField(Types.DECIMAL, "---9.9999");
        del_numlinE = new gnu.chu.controles.CTextField(Types.DECIMAL, "##9");
        deo_preusuE = new gnu.chu.controles.CTextField(Types.DECIMAL, "---9.9999");
        def_preusuE = new gnu.chu.controles.CTextField(Types.DECIMAL, "---9.9999");
        def_tiempoE = new gnu.chu.controles.CTextField();
        deo_tiempoE = new gnu.chu.controles.CTextField();
        MFechaCab = new javax.swing.JMenuItem();
        MFechaLin = new javax.swing.JMenuItem();
        MVerMvtCab = new javax.swing.JMenuItem();
        MVerMvtLin = new javax.swing.JMenuItem();
        ImprEtiqMI = new javax.swing.JMenuItem("Imprimir");
        MVerTraCab = new javax.swing.JMenuItem();
        MVerTraLin = new javax.swing.JMenuItem();
        Pprinc = new gnu.chu.controles.CPanel();
        Pcabe = new gnu.chu.controles.CPanel();
        eje_numeL = new gnu.chu.controles.CLabel();
        eje_numeE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        deo_codiL = new gnu.chu.controles.CLabel();
        deo_codiE = new gnu.chu.controles.CTextField(Types.DECIMAL,"####9");
        deo_cerraE = new gnu.chu.controles.CCheckBox("0","-1");
        deo_numdesL = new gnu.chu.controles.CLabel();
        deo_numdesE = new gnu.chu.controles.CTextField(Types.DECIMAL,"####9");
        cLabel1 = new gnu.chu.controles.CLabel();
        deo_fechaE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        Plotgen = new gnu.chu.controles.CPanel();
        cLabel2 = new gnu.chu.controles.CLabel();
        deo_lotnueE = new gnu.chu.controles.CComboBox();
        eje_numeL1 = new gnu.chu.controles.CLabel();
        deo_ejlogeE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        eje_numeL2 = new gnu.chu.controles.CLabel();
        deo_selogeE = new gnu.chu.controles.CTextField(Types.CHAR,"X",1);
        eje_numeL3 = new gnu.chu.controles.CLabel();
        deo_nulogeE = new gnu.chu.controles.CTextField(Types.DECIMAL,"####9");
        deo_desnueE = new gnu.chu.controles.CCheckBox("S","N");
        cLabel3 = new gnu.chu.controles.CLabel();
        deo_blockE = new gnu.chu.controles.CComboBox();
        prv_codiE = new gnu.chu.camposdb.prvPanel();
        deo_fecproE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel5 = new gnu.chu.controles.CLabel();
        cLabel6 = new gnu.chu.controles.CLabel();
        deo_feccadE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        BsaltaCab = new gnu.chu.controles.CButton();
        usu_nombE = new gnu.chu.controles.CTextField();
        cLabel9 = new gnu.chu.controles.CLabel();
        cLabel10 = new gnu.chu.controles.CLabel();
        cli_codiE = new gnu.chu.camposdb.cliPanel();
        cLabel22 = new gnu.chu.controles.CLabel();
        cLabel21 = new gnu.chu.controles.CLabel();
        deo_fecsacE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel11 = new gnu.chu.controles.CLabel();
        deo_almoriE = new gnu.chu.controles.CLinkBox();
        cLabel12 = new gnu.chu.controles.CLabel();
        deo_almdesE = new gnu.chu.controles.CLinkBox();
        Btrazabilidad = new gnu.chu.controles.CButton();
        Ppie = new gnu.chu.controles.CPanel();
        Baceptar = new gnu.chu.controles.CButton();
        Bcancelar = new gnu.chu.controles.CButton();
        Bimpeti = new gnu.chu.controles.CButtonMenu();
        cLabel8 = new gnu.chu.controles.CLabel();
        numCopiasE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9");
        opVerGrupo = new gnu.chu.controles.CCheckBox("0","-1");
        opVerAgrup = new gnu.chu.controles.CCheckBox();
        opRepet = new gnu.chu.controles.CCheckBox();
        BImpEtiInt = new gnu.chu.controles.CButton(Iconos.getImageIcon("print"));
        Ptabpan = new gnu.chu.controles.CTabbedPane();
        Pgrid = new gnu.chu.controles.CPanel();
        jtLin = new gnu.chu.controles.CGridEditable(11)
        {
            @Override
            public void afterCambiaLinea()
            {
                proCodAnt=pro_codlE.getValorInt();
                defKilAnt=def_kilosE.getValorDec();
                pro_codlE.resetCambio();
                def_kilosE.resetCambio();
                def_feccadE.resetCambio();
                String txt = jtLin.getValString(JTLIN_FECCAD).trim().replace(def_feccadE.getSepFecha(), ' ').trim();
                if (txt.equals(""))
                {
                    jtLin.setValor(ultFecCaduc,JTLIN_FECCAD);
                    def_feccadE.setDate(ultFecCaduc);
                }
                try {
                    actKilos(JTLIN_GRID);
                } catch (SQLException k)
                {
                    Error("Error al actualizar kilos grid",k);
                }
            }
            @Override
            public boolean afterInsertaLinea(boolean ins)
            {
                jtLin.setValor(ultFecCaduc, JTLIN_FECCAD);
                def_feccadE.setDate(ultFecCaduc);
                return true;
            }
            @Override
            public int cambiaLinea(int row, int col)
            {
                //      System.out.println("cambiaLinea: "+pro_codlE.getText()+
                    //                         " - "+def_kilosE.getValorDec());
                return cambiaLineajtLin(row);
            }
            @Override
            public boolean deleteLinea(int row, int col)
            {
                jtLin.setValor(""+proCodAnt,JTLIN_PROCODI); // Rest. El valor Antiguo
                jtLin.setValor(""+defKilAnt,JTLIN_KILOS); // Rest. Kilos ANTIGUOS
                return borraLineajtLin(row);

            }
            @Override
            public void afterDeleteLinea()
            {
                SwingUtilities.invokeLater(new Thread()
                    {
                        public void run()
                        {
                            try {
                                tid_codiE.setEnabled(getNuLiDes(true)==0);
                                if (AGRUPALOTE)
                                deo_lotnueE.setEnabled(tid_codiE.isEnabled());
                            } catch (SQLException k)
                            {
                                Error("Error al contar el numero de lineas finales",k);
                                return;
                            }
                        }
                    });
                }
                @Override
                public void cambiaColumna(int col,int colNueva,int row)
                {
                    try {
                        if (col == JTLIN_PROCODI)
                        jtLin.setValor(pro_codlE.getNombArt(pro_codlE.getText()), row, JTLIN_PRONOMB);
                    } catch (Exception k)
                    {
                        Error("Error al buscar Nombre Articulo",k);
                    }
                }

            };
            jtCab = new gnu.chu.controles.CGridEditable(11){

                @Override
                public boolean insertaLinea(int row, int col)
                {
                    if (proCodTD==0 && jtCab.getRowCount()==1)
                    {
                        jtCab.salirGrid();
                        irGridLin();
                    }
                    return true;
                }
                @Override
                public boolean afterInsertaLinea(boolean insLinea)
                {
                    if (proCodTD==0 && insLinea==false &&  jtCab.getRowCount()==1)
                    {
                        jtCab.setProcInsLinea(false);
                        swPrimeraLinea=true;
                        BirGrid.doClick();
                        return false;
                    }
                    return true;
                    // deo_ejelotE.setValorDec(EU.ejercicio);
                    // jtCab.setValor(""+EU.ejercicio,jtCab.getSelectedRow(),JTCAB_EJELOT);
                }
                @Override
                public void afterCambiaLinea()
                {

                    try {
                        actKilos(JTCAB_GRID);
                        resetCambioLineaCab();
                        pro_codiE.getNombArt(); // Actualizo datos de Producto
                    } catch (SQLException k){
                        Error("Error al actualizar propiedades articulo",k);
                        return;
                    }
                    //        if (! deo_kilosE.isEnabled())
                    //           deo_kilosE.setValorDec(jtCab.getValorDec(JTCAB_KILOS));
                    deo_kilosE.setEnabled( ! pro_codiE.hasControlIndiv());
                }

                @Override
                public int cambiaLinea(int row, int col)
                {
                    int reCaLin=cambiaLineajtCab(row);
                    swErrCab=reCaLin>=0;
                    return reCaLin;
                }

                @Override
                public boolean deleteLinea(int row, int col)
                {
                    try
                    {
                        if (jtCab.getValorInt(row, JTCAB_NL) == 0)
                        return true;
                        if (row==0 && existLineasSalida() && ! isArgumentoAdmin())
                        {
                            msgBox("Hay productos de salida. Imposible borrar producto Origen");
                            return false;
                        }
                        if (!borraLinCab(jtCab.getValorInt(row, JTCAB_NL)))
                        {
                            throw new SQLException("Linea con Numero de Despiece No ENCONTRADA: "+jtCab.getValorInt(row, JTCAB_NL) );
                        }
                        dtAdd.commit();
                        mensajeErr("Linea borrada... ");
                    }
                    catch (SQLException k)
                    {
                        Error("Error al borrar la linea", k);
                    }
                    return true;
                }

            };
            try {
                Vector vc = new Vector();
                vc.addElement("Producto"); // 0
                vc.addElement("Nombre"); // 1
                vc.addElement("Eje"); // 2
                vc.addElement("Ser"); // 3
                vc.addElement("Lote"); // 4
                vc.addElement("Ind"); // 5
                vc.addElement("Peso"); // 6
                vc.addElement("Precio"); // 7
                vc.addElement("NL"); //8
                vc.addElement("Pr.Usu"); // 9
                vc.addElement("Fec.Mvto"); // 9
                jtCab.setCabecera(vc);
                jtCab.setMaximumSize(new Dimension(740, 159));
                jtCab.setMinimumSize(new Dimension(740, 159));
                jtCab.setPreferredSize(new Dimension(740, 159));
                jtCab.setAnchoColumna(new int[]{50, 200, 30, 20, 40, 30, 60, 70,30,70,100});
                jtCab.setAlinearColumna(new int[]{2, 0, 2, 1, 2, 2, 2, 2,2,2,0});
                jtCab.setFormatoColumna(JTCAB_KILOS, "---,--9.99");
                jtCab.setFormatoColumna(JTCAB_PRECIO,"---9.9999");
                jtCab.setFormatoColumna(JTCAB_PREUSU,"---9.9999");
                jtCab.setAjusAncCol(true);
                jtCab.setAjustarGrid(true);
                jtCab.setAjustarColumnas(false);
                pro_nocabE.setEnabled(false);
                deo_ejelotE.setValorInt(EU.ejercicio);
                ArrayList vc1=new ArrayList();
                vc1.add(pro_codiE.getTextField()); // 0
                vc1.add(pro_nocabE); // 1
                vc1.add(deo_ejelotE); // 2
                vc1.add(deo_serlotE); // 3
                vc1.add(pro_loteE); // 4
                vc1.add(pro_numindE); // 5
                vc1.add(deo_kilosE); // 6
                vc1.add(deo_prcostE); // 9
                vc1.add(del_numlinE); //10
                vc1.add(deo_preusuE);
                vc1.add(deo_tiempoE);
                jtDesp = new gnu.chu.controles.Cgrid(2);
                {
                    ArrayList v=new ArrayList();
                    v.add("NºDesp");
                    v.add("Fecha");
                    jtDesp.setCabecera(v);
                    jtDesp.setAnchoColumna(new int[]{60,90});
                    jtDesp.setAlinearColumna(new int[]{0,1});
                    jtDesp.setFormatoColumna(1, "dd-MM-yyyy");
                    jtDesp.setAjustarGrid(true);
                }
                Ptipdes = new gnu.chu.controles.CPanel();
                tid_codiE = new gnu.chu.camposdb.tidCodi2();
                cLabel13 = new gnu.chu.controles.CLabel();
                cLabel14 = new gnu.chu.controles.CLabel();
                cLabel15 = new gnu.chu.controles.CLabel();
                kgOrigE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---,--9.99");
                uniOrigE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9");
                BsalLin = new gnu.chu.controles.CButton();
                cLabel19 = new gnu.chu.controles.CLabel();
                impOrigE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---,--9.99");
                BvalDesp = new gnu.chu.controles.CButton(Iconos.getImageIcon("precio"));
                Phist = new gnu.chu.controles.CPanel();
                jtHist = new gnu.chu.controles.Cgrid(4);
                POtros = new gnu.chu.controles.CPanel();
                cLabel7 = new gnu.chu.controles.CLabel();
                deo_incvalE = new gnu.chu.controles.CComboBox();
                BForzarProd = new gnu.chu.controles.CButton(Iconos.getImageIcon("insertar"));
                opSimular = new gnu.chu.controles.CCheckBox("0","-1");
                opMantFecha = new gnu.chu.controles.CCheckBox();
                opAnularControl = new gnu.chu.controles.CCheckBox("0","-1");
                Ptotal1 = new gnu.chu.controles.CPanel();
                BcopLin = new gnu.chu.controles.CButton("F5",Iconos.getImageIcon("fill"));
                cLabel16 = new gnu.chu.controles.CLabel();
                kgFinE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---,--9.99");
                cLabel17 = new gnu.chu.controles.CLabel();
                uniFinE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9");
                cLabel18 = new gnu.chu.controles.CLabel();
                kgDifE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---,--9.99");
                opImpEt = new gnu.chu.controles.CCheckBox();
                BirGrid = new gnu.chu.controles.CButton(Iconos.getImageIcon("duplicar"));
                cLabel20 = new gnu.chu.controles.CLabel();
                impFinE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---,--9.99");
                cargaPSC = new gnu.chu.controles.CCheckBox();
                opAutoClas = new gnu.chu.controles.CCheckBox();

                deo_serlotE.setText("A");
                deo_serlotE.setMayusc(true);

                deo_kilosE.setLeePesoBascula(botonBascula);
                deo_kilosE.setToolTipText("Pulse <F1> Para coger peso de la bascula");

                deo_prcostE.setEnabled(false);

                deo_kilosE.setLeePesoBascula(botonBascula);
                def_kilosE.setToolTipText("Pulse <F1> Para coger peso de la bascula");

                def_feccadE.setToolTipText("");

                def_numindE.setText("0");
                def_numindE.setEnabled(false);

                def_ordenE.setText("0");
                def_ordenE.setEnabled(false);

                def_unicajE.setText("1");

                def_prcostE.setEnabled(false);

                del_numlinE.setText("0");
                del_numlinE.setEnabled(false);

                deo_preusuE.setEnabled(false);

                def_preusuE.setEnabled(false);

                def_tiempoE.setEnabled(false);

                deo_tiempoE.setEnabled(false);

                MFechaCab.setText("Rest. Fecha");
                MFechaCab.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        MFechaCabActionPerformed(evt);
                    }
                });

                MFechaLin.setText("Rest. Fecha");
                MFechaLin.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        MFechaLinActionPerformed(evt);
                    }
                });

                MVerMvtCab.setText("Ver Mvtos");
                MVerMvtCab.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        MVerMvtCabActionPerformed(evt);
                    }
                });

                MVerMvtLin.setText("Ver Mvtos");
                MVerMvtLin.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        MVerMvtLinActionPerformed(evt);
                    }
                });

                ImprEtiqMI.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        ImprEtiqMIActionPerformed(evt);
                    }
                });

                MVerTraCab.setText("Ver Datos Trazabilidad");
                MVerTraCab.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        MVerTraCabActionPerformed(evt);
                    }
                });

                MVerTraLin.setText("Ver Datos Trazabilidad");
                MVerTraLin.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        MVerTraLinActionPerformed(evt);
                    }
                });

                Pprinc.setForeground(new java.awt.Color(255, 255, 255));
                Pprinc.setMaximumSize(new java.awt.Dimension(669, 187));
                Pprinc.setMinimumSize(new java.awt.Dimension(669, 187));
                Pprinc.setLayout(new java.awt.GridBagLayout());

                Pcabe.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
                Pcabe.setMaximumSize(new java.awt.Dimension(690, 110));
                Pcabe.setMinimumSize(new java.awt.Dimension(690, 110));
                Pcabe.setPreferredSize(new java.awt.Dimension(690, 110));
                Pcabe.setQuery(true);
                Pcabe.setLayout(null);

                eje_numeL.setText("Ejercicio");
                Pcabe.add(eje_numeL);
                eje_numeL.setBounds(3, 2, 46, 17);
                Pcabe.add(eje_numeE);
                eje_numeE.setBounds(53, 2, 35, 17);

                deo_codiL.setText("Despiece ");
                Pcabe.add(deo_codiL);
                deo_codiL.setBounds(98, 2, 54, 17);
                Pcabe.add(deo_codiE);
                deo_codiE.setBounds(158, 2, 41, 17);

                deo_cerraE.setText("Cerrado");
                Pcabe.add(deo_cerraE);
                deo_cerraE.setBounds(480, 2, 65, 17);

                deo_numdesL.setText("Grupo ");
                Pcabe.add(deo_numdesL);
                deo_numdesL.setBounds(570, 2, 37, 17);
                Pcabe.add(deo_numdesE);
                deo_numdesE.setBounds(610, 2, 48, 17);

                cLabel1.setText("Fecha");
                Pcabe.add(cLabel1);
                cLabel1.setBounds(209, 2, 40, 17);

                deo_fechaE.setPreferredSize(new java.awt.Dimension(10, 18));
                Pcabe.add(deo_fechaE);
                deo_fechaE.setBounds(250, 2, 70, 17);

                Plotgen.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                Plotgen.setLayout(null);

                cLabel2.setBackground(new java.awt.Color(204, 0, 51));
                cLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                cLabel2.setForeground(new java.awt.Color(255, 255, 255));
                cLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                cLabel2.setText("LOTE GENERADO");
                cLabel2.setOpaque(true);
                Plotgen.add(cLabel2);
                cLabel2.setBounds(0, 2, 100, 17);

                deo_lotnueE.addItem("Mantener","0");
                deo_lotnueE.addItem("Nuevo","-1");
                deo_lotnueE.addItem("Forzar",""+FORZAR_LOTE);
                Plotgen.add(deo_lotnueE);
                deo_lotnueE.setBounds(110, 2, 100, 17);

                eje_numeL1.setText("Serie");
                Plotgen.add(eje_numeL1);
                eje_numeL1.setBounds(280, 2, 40, 17);

                deo_ejlogeE.setEnabled(false);
                Plotgen.add(deo_ejlogeE);
                deo_ejlogeE.setBounds(250, 2, 30, 17);

                eje_numeL2.setText("Ejerc.");
                Plotgen.add(eje_numeL2);
                eje_numeL2.setBounds(220, 2, 30, 17);

                deo_selogeE.setEnabled(false);
                deo_selogeE.setInheritsPopupMenu(true);
                deo_selogeE.setMayusc(true);
                Plotgen.add(deo_selogeE);
                deo_selogeE.setBounds(320, 2, 17, 17);

                eje_numeL3.setText("Lote");
                Plotgen.add(eje_numeL3);
                eje_numeL3.setBounds(340, 2, 30, 17);

                deo_nulogeE.setEnabled(false);
                Plotgen.add(deo_nulogeE);
                deo_nulogeE.setBounds(370, 2, 40, 17);

                deo_desnueE.setText("Desp. Interno");
                Plotgen.add(deo_desnueE);
                deo_desnueE.setBounds(410, 2, 100, 17);

                Pcabe.add(Plotgen);
                Plotgen.setBounds(2, 22, 520, 23);

                cLabel3.setText("Estado");
                Pcabe.add(cLabel3);
                cLabel3.setBounds(530, 22, 37, 15);

                deo_blockE.setEnabled(false);
                deoBlockE_addItem(deo_blockE);
                Pcabe.add(deo_blockE);
                deo_blockE.setBounds(570, 22, 90, 17);
                Pcabe.add(prv_codiE);
                prv_codiE.setBounds(70, 48, 320, 17);

                deo_fecproE.setDependePadre(false);
                deo_fecproE.setPreferredSize(new java.awt.Dimension(10, 18));
                Pcabe.add(deo_fecproE);
                deo_fecproE.setBounds(470, 48, 70, 17);

                cLabel5.setText("Fec.Produc.");
                Pcabe.add(cLabel5);
                cLabel5.setBounds(400, 48, 70, 17);

                cLabel6.setText("Fec. Caduc.");
                Pcabe.add(cLabel6);
                cLabel6.setBounds(400, 67, 70, 17);

                deo_feccadE.setDependePadre(false);
                deo_feccadE.setPreferredSize(new java.awt.Dimension(10, 18));
                Pcabe.add(deo_feccadE);
                deo_feccadE.setBounds(470, 67, 70, 17);

                BsaltaCab.setText("cButton1");
                Pcabe.add(BsaltaCab);
                BsaltaCab.setBounds(660, 50, 1, 1);

                usu_nombE.setEnabled(false);
                Pcabe.add(usu_nombE);
                usu_nombE.setBounds(380, 2, 90, 17);

                cLabel9.setText("Usuario");
                Pcabe.add(cLabel9);
                cLabel9.setBounds(330, 2, 50, 17);

                cLabel10.setText("Proveedor");
                Pcabe.add(cLabel10);
                cLabel10.setBounds(10, 48, 70, 15);
                Pcabe.add(cli_codiE);
                cli_codiE.setBounds(60, 67, 330, 17);

                cLabel22.setText("Cliente");
                Pcabe.add(cLabel22);
                cLabel22.setBounds(10, 67, 50, 15);

                cLabel21.setText("Fec. Sacr.");
                Pcabe.add(cLabel21);
                cLabel21.setBounds(550, 48, 55, 17);

                deo_fecsacE.setDependePadre(false);
                deo_fecsacE.setPreferredSize(new java.awt.Dimension(10, 18));
                Pcabe.add(deo_fecsacE);
                deo_fecsacE.setBounds(610, 48, 70, 17);

                cLabel11.setText("Alm. Origen");
                Pcabe.add(cLabel11);
                cLabel11.setBounds(0, 85, 80, 17);

                deo_almoriE.setToolTipText("Almacen Origen");
                deo_almoriE.setAncTexto(30);
                deo_almoriE.setFormato(Types.DECIMAL,"##9");
                Pcabe.add(deo_almoriE);
                deo_almoriE.setBounds(80, 85, 214, 17);

                cLabel12.setText("Alm. Destino");
                Pcabe.add(cLabel12);
                cLabel12.setBounds(300, 85, 80, 17);

                deo_almdesE.setToolTipText("Almacen Destino");
                deo_almdesE.setAncTexto(30);
                deo_almoriE.setFormato(Types.DECIMAL,"##9");
                Pcabe.add(deo_almdesE);
                deo_almdesE.setBounds(390, 85, 214, 17);

                Btrazabilidad.setText("Dat.Trazabilidad");
                Btrazabilidad.setDependePadre(false);
                Pcabe.add(Btrazabilidad);
                Btrazabilidad.setBounds(550, 65, 120, 19);

                Pprinc.add(Pcabe, new java.awt.GridBagConstraints());

                Ppie.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                Ppie.setMaximumSize(new java.awt.Dimension(559, 26));
                Ppie.setMinimumSize(new java.awt.Dimension(559, 26));
                Ppie.setPreferredSize(new java.awt.Dimension(559, 26));
                Ppie.setLayout(null);

                Baceptar.setText("Aceptar");
                Ppie.add(Baceptar);
                Baceptar.setBounds(480, 1, 100, 24);

                Bcancelar.setText("Cancelar");
                Ppie.add(Bcancelar);
                Bcancelar.setBounds(590, 1, 100, 24);

                Bimpeti.setText("(F9) Impr.Etiq");
                Bimpeti.addMenu("-----","U");
                Ppie.add(Bimpeti);
                Bimpeti.setBounds(170, 0, 110, 17);

                cLabel8.setText("Copias ");
                Ppie.add(cLabel8);
                cLabel8.setBounds(0, 0, 45, 17);
                Ppie.add(numCopiasE);
                numCopiasE.setBounds(45, 0, 25, 17);

                opVerGrupo.setText("Ver Grupo");
                opVerGrupo.setToolTipText("Ver grupo de despiece");
                Ppie.add(opVerGrupo);
                opVerGrupo.setBounds(400, 2, 80, 17);

                opVerAgrup.setText("Agrupar");
                opVerAgrup.setToolTipText("Ver Lineas agrupadas");
                Ppie.add(opVerAgrup);
                opVerAgrup.setBounds(330, 2, 70, 17);

                opRepet.setSelected(true);
                opRepet.setText("RR");
                opRepet.setToolTipText("Repetir Despiece en Alta");
                Ppie.add(opRepet);
                opRepet.setBounds(285, 2, 40, 17);

                BImpEtiInt.setText("Etiq. Inter.");
                Ppie.add(BImpEtiInt);
                BImpEtiInt.setBounds(78, 0, 90, 18);

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 5;
                gridBagConstraints.gridwidth = 2;
                gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
                Pprinc.add(Ppie, gridBagConstraints);

                Pgrid.setLayout(new java.awt.GridBagLayout());

                jtLin.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                jtLin.setMaximumSize(new java.awt.Dimension(669, 99));
                jtLin.setMinimumSize(new java.awt.Dimension(669, 99));
                try {
                    ArrayList v = new ArrayList();

                    v.add("Producto"); // 0
                    v.add("Descripcion"); // 1
                    v.add("Kgs"); // 2
                    v.add("Unid"); // 3 (Unidades Pesadas).
                    v.add("Costo"); // 4
                    v.add("Fec.Cad"); // 5
                    v.add("N.Ind"); // 6
                    v.add("N.Lin"); // 7
                    v.add("U/C"); // 8 Unidades por Caja
                    v.add("Pr.Usu"); // 9
                    v.add("Fec.Mvto"); // 10
                    jtLin.setCabecera(v);

                    jtLin.setAnchoColumna(new int[]
                        {54, 220, 70, 40, 70,90, 40, 40,40,70,100});
                    jtLin.setAlinearColumna(new int[]
                        {2, 0, 2, 2, 2, 1,2, 2,2,2,0});

                    jtLin.setAjustarGrid(true);
                    jtLin.setAjustarColumnas(false);

                    pro_nombE.setEnabled(false);
                    if (! MODPRECIO)
                    def_prcostE.setEnabled(false);
                    def_numindE.setEnabled(false);
                    //   def_numpieE.setEnabled(false);
                    def_numpieE.setValorDec(1);
                    def_ordenE.setValorDec(1);
                    def_ordenE.setEnabled(false);
                    ArrayList v1= new ArrayList();

                    def_unicajE.setText("1");
                    def_ordenE.setText("0");
                    v1.add(pro_codlE.getTextField()); // 0
                    v1.add(pro_nombE); // 1
                    v1.add(def_kilosE); // 2
                    v1.add(def_numpieE); // 3
                    v1.add(def_prcostE); // 4
                    v1.add(def_feccadE); // 5
                    v1.add(def_numindE); // 6
                    v1.add(def_ordenE); // 7
                    v1.add(def_unicajE); // 8
                    v1.add(def_preusuE); // 9
                    v1.add(def_tiempoE); // 10
                    jtLin.setCampos(v1);
                    jtLin.setFormatoCampos();
                    /*jtLin.setFormatoColumna(JTLIN_KILOS, "--,---9.99");
                    jtLin.setFormatoColumna(JTLIN_UNID, "##9");
                    jtLin.setFormatoColumna(JTLIN_COSTO, "---9.9999");
                    jtLin.setFormatoColumna(JTLIN_FECCAD, deo_feccadE.getFormato());
                    jtLin.setFormatoColumna(JTLIN_NUMIND, "---9");
                    jtLin.setFormatoColumna(JTLIN_ORDEN, "##9");
                    jtLin.setFormatoColumna(JTLIN_NUMCAJ, "##9");
                    jtLin.setFormatoColumna(JTLIN_PREUSU, "---9.9999"); */
                    JMenu etiqueta = new JMenu("Etiqueta");

                    etiqueta.add(ImprEtiqMI);

                    jtLin.getPopMenu().add(etiqueta);
                    if (isAdmin())
                    jtLin.getPopMenu().add(MFechaLin);
                    jtLin.getPopMenu().add(MVerMvtLin);
                    jtLin.getPopMenu().add(MVerTraLin);
                } catch (Exception k) { Error("Error al configurar grid lineas",k);}

                javax.swing.GroupLayout jtLinLayout = new javax.swing.GroupLayout(jtLin);
                jtLin.setLayout(jtLinLayout);
                jtLinLayout.setHorizontalGroup(
                    jtLinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 683, Short.MAX_VALUE)
                );
                jtLinLayout.setVerticalGroup(
                    jtLinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 188, Short.MAX_VALUE)
                );

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 3;
                gridBagConstraints.gridwidth = 3;
                gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.weighty = 2.0;
                gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
                Pgrid.add(jtLin, gridBagConstraints);

                jtCab.setCampos(vc1);
            } catch (Exception k){Error("Error al inicializa jtCab",k); }
            jtCab.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
            jtCab.setMaximumSize(new java.awt.Dimension(449, 109));
            jtCab.setMinimumSize(new java.awt.Dimension(449, 109));
            if (isAdmin())
            jtCab.getPopMenu().add(MFechaCab);
            jtCab.getPopMenu().add(MVerMvtCab);
            jtCab.getPopMenu().add(MVerTraCab);

            javax.swing.GroupLayout jtCabLayout = new javax.swing.GroupLayout(jtCab);
            jtCab.setLayout(jtCabLayout);
            jtCabLayout.setHorizontalGroup(
                jtCabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 682, Short.MAX_VALUE)
            );
            jtCabLayout.setVerticalGroup(
                jtCabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 188, Short.MAX_VALUE)
            );

            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 2.0;
            gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
            Pgrid.add(jtCab, gridBagConstraints);

            jtDesp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
            jtDesp.setBuscarVisible(false);
            jtDesp.setMaximumSize(new java.awt.Dimension(150, 50));
            jtDesp.setMinimumSize(new java.awt.Dimension(150, 50));

            javax.swing.GroupLayout jtDespLayout = new javax.swing.GroupLayout(jtDesp);
            jtDesp.setLayout(jtDespLayout);
            jtDespLayout.setHorizontalGroup(
                jtDespLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 148, Short.MAX_VALUE)
            );
            jtDespLayout.setVerticalGroup(
                jtDespLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 189, Short.MAX_VALUE)
            );

            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
            gridBagConstraints.weighty = 1.0;
            Pgrid.add(jtDesp, gridBagConstraints);

            Ptipdes.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
            Ptipdes.setMaximumSize(new java.awt.Dimension(669, 23));
            Ptipdes.setMinimumSize(new java.awt.Dimension(669, 23));
            Ptipdes.setPreferredSize(new java.awt.Dimension(669, 23));
            Ptipdes.setLayout(null);

            tid_codiE.setAncTexto(40);
            Ptipdes.add(tid_codiE);
            tid_codiE.setBounds(30, 2, 310, 20);

            cLabel13.setText("Tipo");
            Ptipdes.add(cLabel13);
            cLabel13.setBounds(0, 2, 40, 17);

            cLabel14.setText("Unid");
            Ptipdes.add(cLabel14);
            cLabel14.setBounds(350, 1, 40, 18);

            cLabel15.setText("Kilos");
            Ptipdes.add(cLabel15);
            cLabel15.setBounds(430, 1, 27, 18);

            kgOrigE.setEnabled(false);
            Ptipdes.add(kgOrigE);
            kgOrigE.setBounds(470, 1, 60, 18);

            uniOrigE.setEnabled(false);
            Ptipdes.add(uniOrigE);
            uniOrigE.setBounds(390, 1, 30, 18);

            BsalLin.setText("cButton1");
            Ptipdes.add(BsalLin);
            BsalLin.setBounds(647, 2, 2, 2);

            cLabel19.setText("Importe");
            Ptipdes.add(cLabel19);
            cLabel19.setBounds(530, 1, 44, 17);

            impOrigE.setEnabled(false);
            Ptipdes.add(impOrigE);
            impOrigE.setBounds(580, 1, 70, 17);

            BvalDesp.setToolTipText("Generar Auto Despiece");
            BvalDesp.setFocusable(false);
            Ptipdes.add(BvalDesp);
            BvalDesp.setBounds(660, 1, 18, 18);
            BvalDesp.getAccessibleContext().setAccessibleDescription("");

            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 2;
            gridBagConstraints.gridwidth = 3;
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
            Pgrid.add(Ptipdes, gridBagConstraints);

            Ptabpan.addTab("Principal", Pgrid);

            Phist.setLayout(new java.awt.BorderLayout());

            ArrayList vh=new ArrayList();
            vh.add("Fecha/Hora");
            vh.add("Usuario");
            vh.add("Comentario");
            vh.add("Id");
            jtHist.setCabecera(vh);
            jtHist.setAjustarGrid(true);
            jtHist.setAlinearColumna(new int[]{1,0,0,2});
            jtHist.setAnchoColumna(new int[]{90,120,200,40});
            jtHist.setFormatoColumna(3, "####9");
            jtHist.setFormatoColumna(0,"dd-MM-yyyy HH:mm");

            javax.swing.GroupLayout jtHistLayout = new javax.swing.GroupLayout(jtHist);
            jtHist.setLayout(jtHistLayout);
            jtHistLayout.setHorizontalGroup(
                jtHistLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 685, Short.MAX_VALUE)
            );
            jtHistLayout.setVerticalGroup(
                jtHistLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 408, Short.MAX_VALUE)
            );

            Phist.add(jtHist, java.awt.BorderLayout.CENTER);

            Ptabpan.addTab("Historico", Phist);

            POtros.setLayout(null);

            cLabel7.setText("Produc.");
            POtros.add(cLabel7);
            cLabel7.setBounds(10, 10, 50, 15);

            deo_incvalE.addItem("No","N");
            deo_incvalE.addItem("Si","S");
            POtros.add(deo_incvalE);
            deo_incvalE.setBounds(60, 10, 60, 17);

            BForzarProd.setToolTipText("Forzar Todos individuos a Produccion");
            POtros.add(BForzarProd);
            BForzarProd.setBounds(130, 10, 24, 18);

            opSimular.setText("Simular");
            opSimular.setToolTipText("Simula despiece");
            POtros.add(opSimular);
            opSimular.setBounds(160, 10, 70, 17);

            opMantFecha.setText("Mantener Fecha Despiece en Mvtos");
            opMantFecha.setToolTipText("Mantener Fecha Despiece en Mvtos");
            POtros.add(opMantFecha);
            opMantFecha.setBounds(10, 40, 220, 17);

            opAnularControl.setText("Anular Controles");
            opAnularControl.setToolTipText("Anular controles");
            POtros.add(opAnularControl);
            opAnularControl.setBounds(230, 10, 150, 17);

            Ptabpan.addTab("Varios", POtros);

            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.weightx = 2.0;
            gridBagConstraints.weighty = 2.0;
            Pprinc.add(Ptabpan, gridBagConstraints);

            Ptotal1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
            Ptotal1.setMaximumSize(new java.awt.Dimension(669, 22));
            Ptotal1.setMinimumSize(new java.awt.Dimension(669, 22));
            Ptotal1.setPreferredSize(new java.awt.Dimension(669, 23));
            Ptotal1.setLayout(null);

            BcopLin.setToolTipText("Copiar Linea Anterior");
            Ptotal1.add(BcopLin);
            BcopLin.setBounds(2, 2, 53, 17);

            cLabel16.setText("Kilos");
            Ptotal1.add(cLabel16);
            cLabel16.setBounds(130, 2, 27, 17);

            kgFinE.setEnabled(false);
            Ptotal1.add(kgFinE);
            kgFinE.setBounds(160, 2, 60, 17);

            cLabel17.setText("Unid");
            Ptotal1.add(cLabel17);
            cLabel17.setBounds(60, 2, 30, 17);

            uniFinE.setEnabled(false);
            Ptotal1.add(uniFinE);
            uniFinE.setBounds(90, 2, 30, 17);

            cLabel18.setText("Dif Kilos");
            Ptotal1.add(cLabel18);
            cLabel18.setBounds(350, 2, 50, 17);

            kgDifE.setEnabled(false);
            Ptotal1.add(kgDifE);
            kgDifE.setBounds(400, 2, 60, 17);

            opImpEt.setSelected(true);
            opImpEt.setText("Impr.Etiqueta");
            opImpEt.setToolTipText("Carga productos de salida del tipo de despiece");
            Ptotal1.add(opImpEt);
            opImpEt.setBounds(570, 2, 100, 17);

            BirGrid.setToolTipText("Moverse entre grids");
            BirGrid.setActionCommand("r Grid");
            Ptotal1.add(BirGrid);
            BirGrid.setBounds(670, 2, 20, 17);

            cLabel20.setText("Importe");
            Ptotal1.add(cLabel20);
            cLabel20.setBounds(230, 2, 44, 17);

            impFinE.setEnabled(false);
            Ptotal1.add(impFinE);
            impFinE.setBounds(275, 2, 70, 17);

            cargaPSC.setSelected(true);
            cargaPSC.setText("PS");
            cargaPSC.setToolTipText("Carga Productos Salidos Tipo Despiece");
            Ptotal1.add(cargaPSC);
            cargaPSC.setBounds(530, 2, 40, 17);

            opAutoClas.setSelected(true);
            opAutoClas.setText("Clasif");
            opAutoClas.setToolTipText("Carga Productos Salidos Tipo Despiece");
            Ptotal1.add(opAutoClas);
            opAutoClas.setBounds(465, 2, 60, 17);

            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 2;
            gridBagConstraints.gridwidth = 2;
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
            Pprinc.add(Ptotal1, gridBagConstraints);

            getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void MFechaCabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MFechaCabActionPerformed
        try
        {
            if (jtCab.isVacio())
                return;
            if (nav.isEdicion())
                return;
            if (opVerAgrup.isSelected())
            {
                msgBox("Desagrupe las lineas para restaurar fecha mvto");
                return;
            }
            s="UPDATE desorilin set deo_tiempo='"+deo_fechaE.getFechaDB()+"' where eje_nume="+eje_numeE.getValorInt()+
                " and deo_codi="+deo_codiE.getValorInt()+
                " and del_numlin="+jtCab.getValorInt(jtCab.getSelectedRowDisab(),JTCAB_NL);
            dtAdd.executeUpdate(s);
            s="UPDATE mvtosalm set mvt_time='"+deo_fechaE.getFechaDB()+"' where mvt_ejedoc="+eje_numeE.getValorInt()+
                " and mvt_numdoc="+deo_codiE.getValorInt()+" and mvt_tipdoc='D'"+
                " and mvt_lindoc="+jtCab.getValorInt(jtCab.getSelectedRowDisab(),JTCAB_NL);
            dtAdd.executeUpdate(s);
            dtAdd.commit();
            verDatos(dtCons);
            msgBox("Fecha mvto, puesta a la misma que la fecha de documento");
        } catch (Exception ex)
        {
            Error("Error al actualizar fecha de mvto",ex);
        }

    }//GEN-LAST:event_MFechaCabActionPerformed

    private void MFechaLinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MFechaLinActionPerformed
       try
        {
            if (jtLin.isVacio())
                return;
            if (nav.isEdicion())
                return;
            if (opVerAgrup.isSelected())
            {
                msgBox("Desagrupe las lineas para restaurar fecha mvto");
                return;
            }
            s="UPDATE v_despfin set def_tiempo='"+deo_fechaE.getFechaDB()+"' where eje_nume="+eje_numeE.getValorInt()+
                " and deo_codi="+deo_codiE.getValorInt()+
                " and def_orden="+jtLin.getValorInt(jtLin.getSelectedRowDisab(),JTLIN_ORDEN);
            dtAdd.executeUpdate(s);
            s="UPDATE mvtosalm set mvt_time='"+deo_fechaE.getFechaDB()+"' where mvt_ejedoc="+eje_numeE.getValorInt()+
                " and mvt_numdoc="+deo_codiE.getValorInt()+" and mvt_tipdoc='d'"+
                " and mvt_lindoc="+jtLin.getValorInt(jtLin.getSelectedRowDisab(),JTLIN_ORDEN);
            dtAdd.executeUpdate(s);
            dtAdd.commit();
            verDatos(dtCons);
            msgBox("Fecha mvto, puesta a la misma que la fecha de documento");
        } catch (Exception ex)
        {
            Error("Error al actualizar fecha de mvto",ex);
        }

    }//GEN-LAST:event_MFechaLinActionPerformed
    
    void verMvtosLinDes()
    {
        if (!jtLin.isVacio())
            mostrarMvtos(jtLin.getValorInt(jtLin.getSelectedRowDisab(),JTLIN_PROCODI), 
                deo_ejlogeE.getValorInt(),deo_selogeE.getText(),
                deo_nulogeE.getValorInt(),
                jtLin.getValorInt(jtLin.getSelectedRowDisab(),JTLIN_NUMIND));
    }
    private void MVerMvtLinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MVerMvtLinActionPerformed
     verMvtosLinDes();
    }//GEN-LAST:event_MVerMvtLinActionPerformed

    private void MVerMvtCabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MVerMvtCabActionPerformed
        if (!jtCab.isVacio())
            mostrarMvtos(jtCab.getValorInt(jtCab.getSelectedRowDisab(),JTCAB_PROCODI), 
                jtCab.getValorInt(jtCab.getSelectedRowDisab(),JTCAB_EJELOT),
                jtCab.getValString(jtCab.getSelectedRowDisab(),JTCAB_SERLOT),
                jtCab.getValorInt(jtCab.getSelectedRowDisab(),JTCAB_NUMLOT),                
                jtCab.getValorInt(jtCab.getSelectedRowDisab(),JTCAB_NUMIND));
    }//GEN-LAST:event_MVerMvtCabActionPerformed

    private void ImprEtiqMIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ImprEtiqMIActionPerformed
         Bimpeti.getBotonAccion().doClick();
    }//GEN-LAST:event_ImprEtiqMIActionPerformed

    private void MVerTraCabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MVerTraCabActionPerformed
        mostrarDatosTraz(jtCab.getValorInt(jtCab.getSelectedRowDisab(),JTCAB_PROCODI),
            jtCab.getValString(jtCab.getSelectedRowDisab(),JTCAB_SERLOT),
            jtCab.getValorInt(jtCab.getSelectedRowDisab(),JTCAB_EJELOT),
            jtCab.getValorInt(jtCab.getSelectedRowDisab(),JTCAB_NUMLOT), 
            jtCab.getValorInt(jtCab.getSelectedRowDisab(),JTCAB_NUMIND));
    }//GEN-LAST:event_MVerTraCabActionPerformed

    private void MVerTraLinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MVerTraLinActionPerformed
           mostrarDatosTraz(jtLin.getValorInt(jtLin.getSelectedRowDisab(),JTLIN_PROCODI), 
                deo_selogeE.getText(),deo_ejlogeE.getValorInt(),
                deo_nulogeE.getValorInt(),
                jtLin.getValorInt(jtLin.getSelectedRowDisab(),JTLIN_NUMIND));
    }//GEN-LAST:event_MVerTraLinActionPerformed
    void mostrarMvtos(int proCodi,int ejeNume,String serie, int lote,int numInd) 
    {
        ejecutable prog;
        if ((prog = jf.gestor.getProceso(Comvalm.getNombreClase())) == null)
            return;
        gnu.chu.anjelica.almacen.Comvalm cm = (gnu.chu.anjelica.almacen.Comvalm) prog;
    
        cm.setProCodi(proCodi);
        cm.setLote(lote);
        cm.setIndividuo(numInd);
        cm.setSerie(serie);
        cm.setEjercicio(ejeNume);
        cm.ejecutaConsulta();
        jf.gestor.ir(cm);
  }
  void mostrarDatosTraz()
  {
      if (jtCab.isVacio())
          return;
       mostrarDatosTraz(jtCab.getValorInt(0,JTCAB_PROCODI),
            jtCab.getValString(0,JTCAB_SERLOT),
            jtCab.getValorInt(0,JTCAB_EJELOT),
            jtCab.getValorInt(0,JTCAB_NUMLOT), 
            jtCab.getValorInt(0,JTCAB_NUMIND));
  }
  void mostrarDatosTraz(int proCodi,String serie,int ejeLote,int lote,int numind)
  {
      try {
          if (datTrazFrame==null)
          {
              datTrazFrame=new DatTrazFrame(this)
              {
                    @Override
                    public void matar()
                    {
                       salirDatTraza();
                    }
              };
              
              datTrazFrame.iniciar(dtStat, dtCon1,this,vl,EU);
              datTrazFrame.setEditable(true);
              vl.add(datTrazFrame);            
              datTrazFrame.setLocation(this.getLocation().x, this.getLocation().y + 30);
          }
          
          datTrazFrame.setDatos(proCodi,
                      serie,
                      ejeLote,
                      lote,
                      numind);
           datTrazFrame.setActualizado(false);
           if (nav.pulsado==navegador.EDIT || nav.pulsado==navegador.ADDNEW)
           {
            datTrazFrame.activar(true);          
            datTrazFrame.setFechaCaducidad(deo_feccadE.getDate());
            datTrazFrame.resetCambio();                  
          
           }
           else
              datTrazFrame.activar(false);
           datTrazFrame.actualizar();
           this.setEnabled(false);
           datTrazFrame.mostrar();
      } catch (SQLException | ParseException k)
      {
          Error("Error a mostrar Datos de Trazabilidad",k);
      }
  }
  
  private void salirDatTraza()
  {
    datTrazFrame.setVisible(false);
    this.toFront();
    this.setEnabled(true);
    try
    {
      if (datTrazFrame.getActualizado())
      {
        swForzadoTraz=true;
        utdesp=datTrazFrame.getUtilDespiece();
        deo_feccadE.setDate(datTrazFrame.getUtilDespiece().getFechaCaducidad());
        deo_fecproE.setDate(datTrazFrame.getUtilDespiece().getFechaProduccion() );
        deo_fecsacE.setDate(datTrazFrame.getUtilDespiece().getFechaSacrificio());
        ultFecCaduc=datTrazFrame.getFechaCaducidad();
        for (int n=0;n<jtLin.getRowCount();n++)
        {
            if (jtLin.getValorInt(n,JTLIN_NUMIND)==0)
            {
                jtLin.setValor(ultFecCaduc,n,JTLIN_FECCAD);
                if (n==jtLin.getSelectedRow())
                    def_feccadE.setDate(ultFecCaduc);
            }
        }
      }
      
      this.setSelected(true);
      if (jtLin.isEnabled())
          jtLin.requestFocusLater();
      if (jtCab.isEnabled())
          jtCab.requestFocusLater();
      
    }
    catch (ParseException | SQLException | PropertyVetoException k)
    {}
    
  }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton BForzarProd;
    private gnu.chu.controles.CButton BImpEtiInt;
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CButton Bcancelar;
    private gnu.chu.controles.CButton BcopLin;
    private gnu.chu.controles.CButtonMenu Bimpeti;
    private gnu.chu.controles.CButton BirGrid;
    private gnu.chu.controles.CButton BsalLin;
    private gnu.chu.controles.CButton BsaltaCab;
    private gnu.chu.controles.CButton Btrazabilidad;
    private gnu.chu.controles.CButton BvalDesp;
    private javax.swing.JMenuItem ImprEtiqMI;
    private javax.swing.JMenuItem MFechaCab;
    private javax.swing.JMenuItem MFechaLin;
    private javax.swing.JMenuItem MVerMvtCab;
    private javax.swing.JMenuItem MVerMvtLin;
    private javax.swing.JMenuItem MVerTraCab;
    private javax.swing.JMenuItem MVerTraLin;
    private gnu.chu.controles.CPanel POtros;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Pgrid;
    private gnu.chu.controles.CPanel Phist;
    private gnu.chu.controles.CPanel Plotgen;
    private gnu.chu.controles.CPanel Ppie;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CTabbedPane Ptabpan;
    private gnu.chu.controles.CPanel Ptipdes;
    private gnu.chu.controles.CPanel Ptotal1;
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
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.controles.CLabel cLabel9;
    private gnu.chu.controles.CCheckBox cargaPSC;
    private gnu.chu.camposdb.cliPanel cli_codiE;
    private gnu.chu.controles.CTextField def_feccadE;
    private gnu.chu.controles.CTextField def_kilosE;
    private gnu.chu.controles.CTextField def_numindE;
    private gnu.chu.controles.CTextField def_numpieE;
    private gnu.chu.controles.CTextField def_ordenE;
    private gnu.chu.controles.CTextField def_prcostE;
    private gnu.chu.controles.CTextField def_preusuE;
    private gnu.chu.controles.CTextField def_tiempoE;
    private gnu.chu.controles.CTextField def_unicajE;
    private gnu.chu.controles.CTextField del_numlinE;
    private gnu.chu.controles.CLinkBox deo_almdesE;
    private gnu.chu.controles.CLinkBox deo_almoriE;
    private gnu.chu.controles.CComboBox deo_blockE;
    private gnu.chu.controles.CCheckBox deo_cerraE;
    private gnu.chu.controles.CTextField deo_codiE;
    private gnu.chu.controles.CLabel deo_codiL;
    private gnu.chu.controles.CCheckBox deo_desnueE;
    private gnu.chu.controles.CTextField deo_ejelotE;
    private gnu.chu.controles.CTextField deo_ejlogeE;
    private gnu.chu.controles.CTextField deo_feccadE;
    private gnu.chu.controles.CTextField deo_fechaE;
    private gnu.chu.controles.CTextField deo_fecproE;
    private gnu.chu.controles.CTextField deo_fecsacE;
    private gnu.chu.controles.CComboBox deo_incvalE;
    private gnu.chu.controles.CTextField deo_kilosE;
    private gnu.chu.controles.CComboBox deo_lotnueE;
    private gnu.chu.controles.CTextField deo_nulogeE;
    private gnu.chu.controles.CTextField deo_numdesE;
    private gnu.chu.controles.CLabel deo_numdesL;
    private gnu.chu.controles.CTextField deo_prcostE;
    private gnu.chu.controles.CTextField deo_preusuE;
    private gnu.chu.controles.CTextField deo_selogeE;
    private gnu.chu.controles.CTextField deo_serlotE;
    private gnu.chu.controles.CTextField deo_tiempoE;
    private gnu.chu.controles.CTextField eje_numeE;
    private gnu.chu.controles.CLabel eje_numeL;
    private gnu.chu.controles.CLabel eje_numeL1;
    private gnu.chu.controles.CLabel eje_numeL2;
    private gnu.chu.controles.CLabel eje_numeL3;
    private gnu.chu.controles.CTextField impFinE;
    private gnu.chu.controles.CTextField impOrigE;
    private gnu.chu.controles.CGridEditable jtCab;
    private gnu.chu.controles.Cgrid jtDesp;
    private gnu.chu.controles.Cgrid jtHist;
    private gnu.chu.controles.CGridEditable jtLin;
    private gnu.chu.controles.CTextField kgDifE;
    private gnu.chu.controles.CTextField kgFinE;
    private gnu.chu.controles.CTextField kgOrigE;
    private gnu.chu.controles.CTextField numCopiasE;
    private gnu.chu.controles.CCheckBox opAnularControl;
    private gnu.chu.controles.CCheckBox opAutoClas;
    private gnu.chu.controles.CCheckBox opImpEt;
    private gnu.chu.controles.CCheckBox opMantFecha;
    private gnu.chu.controles.CCheckBox opRepet;
    private gnu.chu.controles.CCheckBox opSimular;
    private gnu.chu.controles.CCheckBox opVerAgrup;
    private gnu.chu.controles.CCheckBox opVerGrupo;
    private gnu.chu.camposdb.proPanel pro_codiE;
    private gnu.chu.camposdb.proPanel pro_codlE;
    private gnu.chu.controles.CTextField pro_loteE;
    private gnu.chu.controles.CTextField pro_nocabE;
    private gnu.chu.controles.CTextField pro_nombE;
    private gnu.chu.controles.CTextField pro_numindE;
    private gnu.chu.camposdb.prvPanel prv_codiE;
    private gnu.chu.camposdb.tidCodi2 tid_codiE;
    private gnu.chu.controles.CTextField uniFinE;
    private gnu.chu.controles.CTextField uniOrigE;
    private gnu.chu.controles.CTextField usu_nombE;
    // End of variables declaration//GEN-END:variables
}
