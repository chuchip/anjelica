package gnu.chu.anjelica.almacen;

/**
 *
 * <p>Título: MantTraspAlm</p>
 * <p>Descripción: Mantenimiento traspasar Individuos de un almacen a otro.
 *  </p>
 * <p>Copyright: Copyright (c) 2005-2017
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los términos de la Licencia Pública General de GNU segun es publicada por
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
 * <p>Empresa: miSL</p>
 * @author chuchi P
 * @version 2.0 (Antes era traspalma)
 */
import com.jgoodies.looks.plastic.PlasticInternalFrameUI;
import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.despiece.MantDesp;
import gnu.chu.anjelica.despiece.utildesp;
import gnu.chu.anjelica.menu;
import gnu.chu.anjelica.pad.pdejerci;
import gnu.chu.comm.BotonBascula;
import gnu.chu.controles.StatusBar;
import gnu.chu.interfaces.PAD;
import gnu.chu.isql.utilSql;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.mensajes;
import gnu.chu.utilidades.miThread;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;


public class MantTraspAlm extends ventanaPad implements PAD
{
  private int almCodio,almCodif;
  ListStock ifList=new ListStock(this)
  {
    @Override
    public void matado()
    {
        insertarRegHist();
    }
  };     
  private boolean ARG_ADMIN=false;
  private boolean addVentanaLote=true;
  private boolean swAddnew=false;
  private utilSql utsql =new utilSql();
  String fichero = null;
  private StkPartid stkPartid;
  private final static int JT_ARTIC=0;
  private final static int JT_NOMBR=1;
  private final static int JT_EJERC=2;
  private final static int JT_SERIE=3;
  private final static int JT_LOTE=4;
  private final static int JT_INDI=5;
  private final static int JT_PESO=6;
  private final static int JT_UNID=7;
  private final static int JT_NUMPAL=8;
  private final static int JT_NUMCAJ=9;
  private final static int JT_INSER=10;
  private String s;
  private ActualStkPart stkPart;
  JFileChooser ficeleE=null;
 
  private int cliCodi;
  private BotonBascula botonBascula;
    /**
     * Creates new form MantTraspAlm
     */
    public MantTraspAlm() {
        initComponents();
    }
    public MantTraspAlm(EntornoUsuario eu, Principal p) {
        this(eu, p, null);
    }

    public MantTraspAlm(EntornoUsuario eu, Principal p, Hashtable<String,String> ht) {
        EU = eu;
        vl = p.panel1;
        jf = p;
        eje = true;

        setTitulo("Mantenimiento Traspaso Almacenes");

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
            Logger.getLogger(MantDesp.class.getName()).log(Level.SEVERE, null, e);
            setErrorInit(true);
        }
    }
    public MantTraspAlm(menu p, EntornoUsuario eu) {
        this(p,eu, null);
    }
    public MantTraspAlm(menu p, EntornoUsuario eu,Hashtable ht) {
    

        EU = eu;
        vl = p.getLayeredPane();
        setTitulo("Mantenimiento Traspaso Almacenes");
        eje = false;
       
        try
        {
            setParametros(ht);
            jbInit();
        } catch (Exception e)
        {
            Logger.getLogger(MantDesp.class.getName()).log(Level.SEVERE, null, e);
            setErrorInit(true);
        }
    }
    private void setParametros(Hashtable<String,String> ht)
    {
          if (ht!=null)
                ARG_ADMIN=ht.get("admin")==null?false:
                    Boolean.parseBoolean(ht.get("admin"));
    }
    private void jbInit() throws Exception {      
        setVersion("2017-02-17 "+ (ARG_ADMIN?"ADMIN":""));
  
        nav = new navegador(this, dtCons, false, navegador.NORMAL);
        statusBar = new StatusBar(this);
        iniciarFrame();

        this.getContentPane().add(nav, BorderLayout.NORTH);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        this.setPad(this);
        dtCons.setLanzaDBCambio(false);

        initComponents();
        strSql = getStrSql()+
             " and avc_ano >= "+(EU.ejercicio-1)+
             getOrderQuery();
            
        conecta();

        iniciarBotones(Baceptar, Bcancelar);
        botonBascula = new BotonBascula(EU, this);
        botonBascula.setPreferredSize(new Dimension(50, 24));
        botonBascula.setMinimumSize(new Dimension(50, 24));
        botonBascula.setMaximumSize(new Dimension(50, 24));
        statusBar.add(botonBascula, new GridBagConstraints(9, 0, 1, 2, 0.0, 0.0, GridBagConstraints.EAST,
            GridBagConstraints.VERTICAL,
            new Insets(0, 5, 0, 0), 0, 0));
        navActivarAll();
        this.setSize(700, 524);
        activar(false);
    }
 
    String getStrSql()
    {
        return "select * from v_albavec where emp_codi="+EU.em_cod+
            " and avc_serie='X' ";
    }
    String getOrderQuery()
    {
        return " order by avc_ano,avc_nume";
    }
  @Override
    public void iniciarVentana() throws Exception
    {
        Pcabe.setDefButton(Baceptar);
        IFLote.putClientProperty(
             PlasticInternalFrameUI.IS_PALETTE,
             Boolean.TRUE);
        IFLote.setSize(new Dimension(390, 150));
        IFLote.setIconifiable(false);
        IFLote.setResizable(false);
        IFLote.setClosable(false);
        pro_codi1E.iniciar(new DatosTabla(ct), this, vl, EU);
        deo_ejelot1E.setValorInt(EU.ejercicio);
        deo_serlot1E.setText("A");
        pro_codiE.setProNomb(pro_nocabE);
        pro_codiE.iniciar(new DatosTabla(ct), this, vl, EU);
        pro_codiE.setCamposLote(deo_ejelotE,  deo_serlotE, pro_loteE,
            pro_numindE, deo_kilosE);
        pro_codiE.setAyudaLotes(true);
        jt.setButton(KeyEvent.VK_F5, Bfill);
        pdalmace.llenaCombo(alm_codifE, dtCon1,'*');
        pdalmace.llenaCombo(alm_codioE, dtCon1,'*');
        ifList.setVisible(false);
        ifList.setClosable(false);
        this.getLayeredPane().add(ifList);
        ifList.setLocation(5,5);     

        stkPart=new ActualStkPart(dtCon1,EU.em_cod);
        avc_fecalbE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
        s="SELECT cli_codi from configuracion WHERE emp_codi = "+EU.em_cod;
        if (! dtStat.select(s))
        {
            Error("Codigo de Cliente INTERNO NO encontrado",new Exception());
            return;
        }
        cliCodi=dtStat.getInt("cli_codi");
        avc_anoE.setColumnaAlias("avc_ano");
        avc_numeE.setColumnaAlias("avc_nume");       
        avc_fecalbE.setColumnaAlias("avc_fecalb");
        usu_nombE.setColumnaAlias("usu_nomb");
        alm_codioE.setColumnaAlias("alm_codori");
        alm_codifE.setColumnaAlias("alm_coddes");
        activarEventos();
        navActivarAll();    
        verDatos(dtCons);
    }

    private void activarEventos()
    {
      Bfill.addActionListener(new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {          
                if (jt.isEnabled() == false || jt.isVacio() || jt.getSelectedRow() < 1)
                    return;
                jt.setValor(jt.getValString(jt.getSelectedRow() - 1, JT_ARTIC), JT_ARTIC);
                jt.setValor(jt.getValString(jt.getSelectedRow() - 1, JT_NOMBR), JT_NOMBR);
                jt.setValor(jt.getValString(jt.getSelectedRow() - 1, JT_EJERC), JT_EJERC);
                jt.setValor(jt.getValString(jt.getSelectedRow() - 1, JT_SERIE), JT_SERIE);
                jt.setValor(jt.getValString(jt.getSelectedRow() - 1, JT_LOTE), JT_LOTE);                
                pro_codiE.setText(jt.getValString(jt.getSelectedRow() - 1, JT_ARTIC));
                pro_nocabE.setText(jt.getValString(jt.getSelectedRow() - 1, JT_NOMBR));
                deo_ejelotE.setText(jt.getValString(jt.getSelectedRow() - 1, JT_EJERC));
                deo_serlotE.setText(jt.getValString(jt.getSelectedRow() - 1, JT_SERIE));
                pro_loteE.setText(jt.getValString(jt.getSelectedRow() - 1, JT_LOTE));
               
                jt.requestFocusLater(jt.getSelectedRow(), JT_INDI);
        }
      });
      Bcancelar1.addActionListener(new ActionListener()
      {
               @Override
        public void actionPerformed(ActionEvent e)
        {          
                ocultarVentanaLote();
        }
      });
      Baceptar1.addActionListener(new ActionListener()
      {
               @Override
        public void actionPerformed(ActionEvent e)
        {          
                cargaDatosLote();
        }
      });
      BirGrid.addFocusListener(new FocusAdapter()
      {
          @Override
          public void focusGained(FocusEvent e) {
              irGrid();
          }
      });
      Bherr.addActionListener(new ActionListener()
      {
            @Override
        public void actionPerformed(ActionEvent e)
        {
            if (e.getActionCommand().equals("Lote"))
                verVentanaLote();
            if (e.getActionCommand().equals("List.Ubic"))
                verHistoricoList();
             if (e.getActionCommand().equals("Importar"))
                 importaDat();
                 
        }
      });
      Bselec.addActionListener(new ActionListener()
      {
            @Override
        public void actionPerformed(ActionEvent e)
        {
            if (e.getActionCommand().equals("Todos"))
               invertirSelec(1);
            if (e.getActionCommand().equals("Ninguno"))
               invertirSelec(0);
            if (e.getActionCommand().equals("Invert"))
               invertirSelec(-1);

        }
      });
      pro_numindE.addFocusListener(new FocusAdapter()
      {
          public void focusLost(FocusEvent e) {
              buscaPesoLin();
          }
      });
      jt.addMouseListener(new MouseAdapter()
      {
                @Override
          public void mouseClicked(MouseEvent e)
          {
            selecInd();
          }
        });
        jt.tableView.addKeyListener(new KeyAdapter()
        {

                @Override
          public void keyPressed(KeyEvent e)
          {
            if (e.getKeyCode() == KeyEvent.VK_INSERT)
            {
              selecInd();
              if (jt.getSelectedRow() < jt.getRowCount())
                jt.requestFocus(jt.getSelectedRow() + 1, JT_INSER);
            }
          }

      });
    }
    public void matar(boolean cerrarConexion)
    {
        if (muerto)
          return;
        if (ifList!=null)
        {
          ifList.setVisible(false);
          ifList.dispose();
        } 

        super.matar(cerrarConexion);
    }
    void verHistoricoList()
    {
        
      try
      {
          ifList.setVisible(true);
          ifList.iniciar(dtCon1,alm_codioE.getValorInt());
      } catch (SQLException ex)
      {
          Error("Error al ver datos historico de listados", ex);
      }
    }
    private void insertarRegHist()
    {
      try
      {
          if (ifList.getFechaHist()==null)
              return;
          if (!dtCon1.select("select h.*,s.*,pro_nomb from hislisstk as h,v_stkpart as s "
              + "  left join v_articulo as p on s.pro_codi =p.pro_codi "
              + " where hls_fecstk={ts '"+Formatear.getFecha(ifList.getFechaHist(),"yyyy-MM-dd HH:mm:ss")+"'}"
              + " and h.alm_codi=  "+ifList.getAlmacen()
              + " and s.alm_codi = h.alm_codi "
              + " and h.pro_codi= s.pro_codi "
              + " and  h.pro_ejelot= eje_nume"
              + " and h.pro_serlot=pro_serie"
              + " and  h.pro_numlot=pro_nupar "
              + " and h.pro_indlot = pro_numind"
              +" order by  h.pro_codi, h.pro_ejelot,h.pro_serlot, h.pro_numlot,h.pro_indlot "))
          {
              msgBox("No encontrado stocks del listado selecionado");
              return;
          }
          do
          {
               ArrayList v=new ArrayList();
               v.add(dtCon1.getInt("pro_codi")); // 0
               v.add(dtCon1.getString("pro_nomb")); // 1
               v.add(dtCon1.getInt("pro_ejelot"));
               v.add(dtCon1.getString("pro_serlot")); // 3
               v.add(dtCon1.getInt("pro_numlot")); // 4
               v.add(dtCon1.getInt("pro_indlot")); // 5
               v.add(dtCon1.getDouble("stp_kilact")); // 6
               v.add(dtCon1.getInt("stp_unact")); // 7
               v.add(dtCon1.getInt("stp_numpal")); // 8
               v.add(dtCon1.getInt("stp_numcaj")); // 8
               v.add("S"); // 8
               jt.addLinea(v);
          } while (dtCon1.next());
      } catch (SQLException ex)
      {
          Error("Error al buscar datos de listado anterior", ex);
      }
    }
    private void invertirSelec(int estado)
    {
       int nRow=jt.getRowCount();
       for (int n=0;n<nRow;n++)
       {
            jt.setValor(estado>=0?estado==1:!jt.getValBoolean(n,JT_INSER),n,JT_INSER);
       }    
      calcAcumulados();
    }
    void selecInd()
    {
    if (jt.isVacio())
      return;
    if (jt.getSelectedColumn() != JT_INSER || jt.getValorInt(JT_UNID)==0)
      return;
    jt.getSelectedRow();
    if (jt.getValBoolean(JT_INSER))
    { // Esta activado se desactivara.
      numIndE.setValorDec(numIndE.getValorInt() - jt.getValorDec(JT_UNID));
      kilosE.setValorDec(kilosE.getValorDec() - jt.getValorDec(JT_PESO));
    }
    else
    {
      numIndE.setValorDec(numIndE.getValorInt() + jt.getValorDec(JT_UNID));
      kilosE.setValorDec(kilosE.getValorDec() + jt.getValorDec(JT_PESO));
    }
    jt.setValor(! jt.getValBoolean(JT_INSER), JT_INSER);
  }


    private void irGrid() 
    {
        if (! checkFecha())
            return;
        if (swAddnew)
        {
            jt.setEnabled(true);       
            jt.requestFocusInicio();
            jt.setValor(true,0,JT_INSER);            
            swAddnew=false;
        }
        else
        {
            jt.requestFocusLater();
        }
            
    }
    private void activar(boolean estado, int modo)
    {
        Baceptar.setEnabled(estado);
        Bcancelar.setEnabled(estado);
        if (modo==navegador.DELETE)
            return;
        if (modo!=navegador.EDIT)
        {
          
            avc_fecalbE.setEnabled(estado);
        }
        alm_codioE.setEnabled(estado);
        alm_codifE.setEnabled(estado);
        Pcabe.setEnabled(estado);
        Bselec.setEnabled(estado);
        Bherr.setEnabled(estado);
        if (modo==navegador.QUERY || ! estado)
        {
            avc_anoE.setEnabled(estado);
            avc_numeE.setEnabled(estado);
            if (modo==navegador.QUERY)
                return;
        }
        BirGrid.setEnabled(estado);       
        jt.setEnabled(estado);
    }
     @Override
    public void PADQuery() {
        mensajeErr("");
        activar(true,navegador.QUERY);
        Pcabe.setQuery(true);
        Pcabe.resetTexto();
        mensaje("Introduzca condiciones de busqueda ...");
        avc_anoE.requestFocus();
   }
    /**
     *  Devuelve numero de unidades en stock de un producto.
     */ 
    int getUnidIndi()
    {           
      try
      {
          StkPartid stkPartid = buscaPeso();
          return stkPartid.getUnidades()<1?0:stkPartid.getUnidades();
      } catch (Exception ex)
      {
          Error("Error al buscar Unidades de un inviduo",ex);
      }
      return 0;
          
    }
    
    private void cargaDatosLote()
    {
      try {
         
         if (!pro_codi1E.controlar())
         {
           mensajeErr(pro_codi1E.getMsgError());
           return;
         }
         if (pro_lote1E.getValorInt() == 0)
         {
           mensajeErr("Introduzca N. de Lote");
           pro_lote1E.requestFocus();
           return;
         }
         if ( deo_serlot1E.isNull())
         {
             mensajeErr("Introduzca Serie");
             deo_serlot1E.requestFocus();
             return;             
         }
       s = "SELECT * FROM V_STKPART WHERE " +
          " EJE_NUME= " + deo_ejelot1E.getValorInt() +
          " AND EMP_CODI= " + EU.em_cod+
          " AND PRO_SERIE='" + deo_serlot1E.getText() + "'" +
          " AND pro_nupar= " + pro_lote1E.getValorInt() +
          " and pro_codi= " + pro_codi1E.getValorInt() +
          " and stp_kilact > 0" +
          " and alm_codi = " + alm_codioE.getValor() +
          " and pro_numind >= " + pro_indiniE.getValorInt() +
          (pro_indiniE.getValorInt() > 0 ?
           " and PRO_NUMIND <= " + pro_indfinE.getValorInt() : "") +
          " order by pro_numind";
      
      if (!dtCon1.select(s))
      {
        msgBox("No encontrados datos en Stock-Partidas");
        pro_codiE.requestFocus();
        return;
      }
      do
      {
        ArrayList v = new ArrayList();
   
        v.add(pro_codi1E.getValorInt());
        v.add(pro_codi1E.getNombArt());
        v.add(deo_ejelot1E.getValorInt());
        v.add(deo_serlot1E.getText());
        v.add(pro_lote1E.getValorInt());
        v.add(dtCon1.getString("pro_numind"));
        v.add(Formatear.format(dtCon1.getString("stp_kilact"),"##,##9.99"));
        v.add(Formatear.format(dtCon1.getString("stp_unact"),"##9"));
        v.add(true);
        jt.addLinea(v);
      } while (dtCon1.next());
      } catch (SQLException k)
      {
          Error("Error al cargar datos",k);
      }
      calcAcumulados();
      ocultarVentanaLote();
      mensajeErr("Cargados Individuos de Lote "+pro_lote1E.getValorInt());
    }
    private void ocultarVentanaLote() 
    {
      IFLote.setVisible(false);
      this.setFoco(null);
      this.setEnabled(true);
      try {
            this.setSelected(true);
      } catch (Exception k){}
    }
    private void verVentanaLote()
    {
      if (! checkFecha())
          return;
      if (addVentanaLote)
        vl.add(IFLote);    
     
      addVentanaLote=false;
      IFLote.setLocation(this.getLocation().x+100, this.getLocation().y+80);
      try
      {
          IFLote.setSelected(true);
      } catch (PropertyVetoException ex)
      {
          Logger.getLogger(MantTraspAlm.class.getName()).log(Level.SEVERE, null, ex);
      }
     this.setEnabled(false);
     this.setFoco(IFLote);    
     IFLote.setVisible(true);
    }
  @Override
    public void PADAddNew() {
      
        mensaje("Introducir Nuevo traspaso");
        swAddnew=true;
        nav.pulsado = navegador.ADDNEW;
        nav.setEnabled(false);
        jt.removeAllDatos();
        resetCambioLineaCab();
        activar(true, navegador.ADDNEW);
        
        Pcabe.resetTexto();
       
        avc_fecalbE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
       
        avc_anoE.setValorDec(EU.ejercicio);

        avc_fecalbE.requestFocus();
    }
    void resetCambioLineaCab() {
        
        pro_numindE.resetCambio();
        pro_loteE.resetCambio();
        pro_codiE.resetCambio();
        deo_ejelotE.resetCambio();
        deo_serlotE.resetCambio();
        deo_kilosE.resetCambio();
        deo_unidE.resetCambio();
    }
    /*
     * Devuelve clase StkPartid con el peso del individuo de origen activo
     * Escribe mensaje de error 
     */
    StkPartid buscaPeso() throws Exception {
       
        stkPartid = utildesp.buscaPeso(dtCon1, deo_ejelotE.getValorInt(), EU.em_cod,
            deo_serlotE.getText(), pro_loteE.getValorInt(),
            pro_numindE.getValorInt(), pro_codiE.getValorInt(), alm_codioE.getValorInt());
        if (nav.pulsado==navegador.EDIT)
        { // Sumo los kilos del albaran si los hubiera.        
            s="select * from v_albvenpar "+
                " where emp_codi = "+EU.em_cod+
                " AND avc_ano =" +avc_anoE.getValorInt()+
                " and avc_serie = 'X' and avc_nume= "+avc_numeE.getValorInt()+
                " and avp_ejelot = "+deo_ejelotE.getValorInt()+
                " and avp_serlot = '"+deo_serlotE.getText()+"'"+
                " and avp_numpar = "+  pro_loteE.getValorInt()+
                " and avp_numind = "+pro_numindE.getValorInt()+
                " and pro_codi = " +pro_codiE.getValorInt();
             
            if (dtCon1.select(s))
            {
                stkPartid.setKilos(stkPartid.getKilos()+ Formatear.redondea(dtCon1.getDouble("avp_canti"),2));
                stkPartid.setUnidades(stkPartid.getUnidades()+dtCon1.getInt("avp_numuni"));
            }
        }
        if (! stkPartid.hasError())
            return stkPartid;
        
//        if (stkPartid.isLockIndiv())
//            return stkPartid;
        mensajeErr(stkPartid.getMensaje());
        return stkPartid;
    }
     /**
     * Devuelve -2 si no ha habido cambios
     *          -1 Si no encuentra el registro.
     *          >=0 Numero de unidades encontradas
     * @return
     */
    int buscaPesoLin() {
        if (!pro_numindE.hasCambio() && !pro_loteE.hasCambio()
            && !pro_codiE.hasCambio() && !deo_serlotE.hasCambio()
            && !deo_ejelotE.hasCambio())
//        deo_kilosE.getValorDec() != 0)
        {
            return -2;
        }

        resetCambioLineaCab();
        try
        {
            StkPartid stkPartid= buscaPeso();
            if ( ! stkPartid.hasError() && stkPartid.hasStock()) 
            {               
                jt.setValor(stkPartid.getKilos(), JT_PESO);
                deo_kilosE.setValorDec(stkPartid.getKilos());
                jt.setValor(stkPartid.getUnidades(), JT_UNID);
                deo_unidE.setValorDec(stkPartid.getUnidades());  
                return stkPartid.getUnidades();
            }
            
            jt.setValor(0, JT_PESO);
            deo_kilosE.setValorDec(0);
            jt.setValor(0, JT_UNID);
            deo_unidE.setValorDec(0);
            return 0;
            
        } catch (Exception k)
        {
            Error("Error al Leer Peso", k);
        }
        return -2;
    }
    
    int cambiaLineajt(int linea) 
    {
        try
        {
            if (pro_codiE.isNull() || ! jt.getValBoolean(linea,JT_INSER))
                return -1; // SI no hay producto o no esta puesto para insertar lo doy como bueno.
            if (!pro_numindE.hasCambio() && !pro_loteE.hasCambio()
            && !pro_codiE.hasCambio() && !deo_serlotE.hasCambio()
            && !deo_ejelotE.hasCambio() && !deo_kilosE.hasCambio() && !deo_unidE.hasCambio())
                return -1; // No hubo cambios
            if (!pro_codiE.controla(false))
            {
                mensajeErr("Codigo de Producto NO valido");
                return 0;
            }
            if (!pro_codiE.isVendible())
            {
                mensajeErr("Producto NO es vendible");
                return 0;
            }
            if (!pro_codiE.isActivo())
            {
                mensajeErr("Producto NO esta ACTIVO");
                return 0;
            }
            if (deo_ejelotE.getValorInt() == 0 && pro_codiE.hasControlIndiv() )
            {
                mensajeErr("Introduzca Ejercicio de Lote");
                return JT_EJERC;
            }
            if (deo_serlotE.getText().trim().equals("")  && pro_codiE.hasControlIndiv())
            {
                mensajeErr("Introduzca Serie de Lote");
                return JT_SERIE;
            }
            if (pro_loteE.getValorInt() == 0  && pro_codiE.hasControlIndiv())
            {
                mensajeErr("Introduzca Número de Lote");
                return JT_LOTE;
            }
            if ( deo_kilosE.isNull())
            {                              
                int res = buscaPesoLin();
                if (res< 1)
                     return 0;  // No encontrado registro o sin stock                
            }
            else
                 stkPartid= buscaPeso();
            if (deo_unidE.isNull())
            {
                mensajeErr("Introduzca Unidades a traspasar");
                return JT_UNID;
            }
            if ( pro_codiE.hasControlIndiv() && !ARG_ADMIN )
            {
              if (stkPartid.getKilos() < deo_kilosE.getValorDec())
              {               
                    mensajeErr("Partida Sin kilos suficientes de Stock");
                    return 0;
              }
              if (stkPartid.getUnidades()<deo_unidE.getValorInt())
              {
                    mensajeErr("Partida Sin unidades suficientes de Stock");
                    return 0;                  
              }
              if (stkPartid.getUnidades()==deo_unidE.getValorInt() && 
                  stkPartid.getKilos() != deo_kilosE.getValorDec())
              {
                  mensajeErr("Kilos deben ser los disponibles en stock para estas unidades");
                  return JT_PESO;
              }
            }
            int nRow;
            // Compruebo que no me metan el mismo individuo dos veces ?
  
            nRow = jt.getRowCount();
            for (int n = 0; n < nRow; n++)
            {
                if (n == linea)
                    continue;
                if (jt.getValorInt(n, JT_ARTIC) == pro_codiE.getValorInt()
                    && jt.getValorInt(n, JT_EJERC) == deo_ejelotE.getValorInt()
                    && jt.getValString(n, JT_SERIE).equals(deo_serlotE.getText())
                    && jt.getValorInt(n, JT_LOTE) == pro_loteE.getValorInt()
                    && jt.getValorInt(n, JT_INDI) == pro_numindE.getValorInt())
                {
                    mensajes.mensajeAviso("!! ATENCION !!! Linea Repetida en posicion:   " + n);
                    break;
                }
            }
            

        } catch (Exception k)
        {
            Error("Error al Controlar Linea de Cabececera", k);
            return 0;
        }
        alm_codioE.setEnabled(false);
        return -1;
    }
    @Override
    public void PADPrimero() {        verDatos(dtCons);    }

    @Override
    public void PADAnterior() {           verDatos(dtCons);    }

    @Override
    public void PADSiguiente() {         verDatos(dtCons);    }

    @Override
    public void PADUltimo() {          verDatos(dtCons);    }

    @Override
    public void ej_query1() 
    {
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
        v.add(avc_anoE.getStrQuery());
        v.add(avc_numeE.getStrQuery());       
        v.add(avc_fecalbE.getStrQuery());
        v.add(usu_nombE.getStrQuery());
        v.add(alm_codioE.getStrQuery());
        v.add(alm_codifE.getStrQuery());
        s = creaWhere(getStrSql(), v, false);
        s += getOrderQuery();
//       debug("Query: "+s);

        Pcabe.setQuery(false);
       
//       Ptotal.setQuery(false);
        try
        {
            boolean res = dtCons.select(s);
            mensaje("");

            if (!res)
            {
                msgBox("No encontrados Datos para estos criterios");
               
            } else
            {
                strSql = s;
                mensajeErr("Nuevos registros selecionados");
            }
            rgSelect();
            verDatos(dtCons);
            activaTodo();
//            this.setEnabled(true);
        } catch (SQLException ex)
        {
            fatalError("Error al buscar Despieces: ", ex);
        }
    }

    @Override
    public void canc_query() {          
        Pcabe.setQuery(false);
        
        verDatos(dtCons);
        mensaje("");
        mensajeErr("Consulta ... CANCELADA");
        activaTodo();
        nav.pulsado = navegador.NINGUNO;
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
    public void ej_edit1() {
         jt.salirGrid();
        int nCol=cambiaLineajt(jt.getSelectedRow());
        if (nCol>0)
        {
            jt.requestFocusLater(jt.getSelectedRow(),nCol);
            return;
        }
       
        if (numIndE.getValorInt()==0)
        {
            mensajeErr("Introduzca al menos un individuo");
            jt.requestFocusInicioLater();
            return;
        }
        int numAlb=0;
        try {
            Timestamp fecAlta=borrarTraspaso();
//            borrarTraspaso();
            if (ARG_ADMIN)
            {
                int ret=mensajes.mensajePreguntar("Mantener fecha de mvto?");
                if (ret!=mensajes.YES)
                    fecAlta=null;
            }
            numAlb=traspDato1(fecAlta,avc_numeE.getValorInt());
            dtAdd.commit();
        } catch (SQLException | ParseException k)
        {
            Error("Error al actualizar Traspasado",k);
        }
         
        mensaje("");
        msgBox("Traspaso modificado ... Creado ALBARAN N. " + numAlb);
        navActivarAll();       
        activaTodo();
        mensajeErr("Traspaso Realizado ... ");
        nav.pulsado = navegador.NINGUNO;
    }

    @Override
    public void canc_edit() {
        nav.pulsado=navegador.NINGUNO;
        mensaje("");
        mensajeErr("Insercion ... CANCELADA");
        activaTodo();
        
        verDatos(dtCons);     
    }

    @Override
    public void ej_addnew1() {
        
        jt.salirGrid();
        int nCol=cambiaLineajt(jt.getSelectedRow());
        if (nCol>0)
        {
            jt.requestFocusLater(jt.getSelectedRow(),nCol);
            return;
        }
        if (! checkFecha())
            return;
        if (numIndE.getValorInt()==0)
        {
            mensajeErr("Introduzca al menos un individuo");
            jt.requestFocusInicioLater();
            return;
        }
        int numAlb=0;
        try {
            Timestamp fecAlta=null;
            if (ARG_ADMIN && Math.abs(Formatear.comparaFechas(avc_fecalbE.getDate(),Formatear.getDateAct()))>7)
            {
                int ret=mensajes.mensajePreguntar("Mantener fecha de mvto?");
                if (ret==mensajes.YES)
                    fecAlta=new Timestamp(avc_fecalbE.getDate().getTime());
            }
            numAlb=traspDato1(fecAlta,0);
            dtAdd.commit();
        } catch (SQLException | ParseException k)
        {
            Error("Error al insertar registro",k);
        }
         
        mensaje("");
        msgBox("Traspaso REALIZADO ... ALBARAN N. " + numAlb);
        navActivarAll();       
        activaTodo();
        mensajeErr("Traspaso Realizado ... ");
        nav.pulsado = navegador.NINGUNO;
    }

    @Override
    public void canc_addnew() 
    {
        nav.pulsado=navegador.NINGUNO;
        mensaje("");
        mensajeErr("Insercion ... CANCELADA");
        activaTodo();
        
        verDatos(dtCons);    
    }
       @Override
    public void PADEdit() {
        s=checkMvtos();
        if (s!=null)
        {
            msgBox(s);
            nav.pulsado=navegador.NINGUNO;
            activaTodo();
            return;
        }   
        almCodio=alm_codioE.getValorInt();
        almCodif=alm_codifE.getValorInt();
        

        activar(true,navegador.EDIT);
        mensaje("Editando registro...");
        jt.requestFocusInicioLater();
    }
     @Override
    public void PADDelete() {
        s=checkMvtos();
        if (s!=null)
        {
            msgBox(s);
            nav.pulsado=navegador.NINGUNO;
            activaTodo();
            return;
        }            
        activar(true,navegador.DELETE);
        mensaje("Borrar registro...");
        Bcancelar.requestFocus();
    }
    @Override
    public void ej_delete1() {
        try {
            borrarTraspaso();
            ctUp.commit();
        } catch (SQLException k)
        {
            Error("Error al anular traspaso",k);
        }
        
        navActivarAll();       
        activaTodo();
        mensaje("");
        mensajeErr("Anulado  traspaso ... ");
        nav.pulsado = navegador.NINGUNO;
    }

    @Override
    public void canc_delete() {
        nav.pulsado=navegador.NINGUNO;
        mensaje("");
        mensajeErr("Insercion ... CANCELADA");
        activaTodo();
        
        verDatos(dtCons); 
    }

    @Override
    public void activar(boolean b) {
        activar(b,navegador.TODOS);
    }
    
    private void verDatos(DatosTabla dt)
    {
        try {
            if (dt.getNOREG())
                return;
            avc_anoE.setValorInt(dt.getInt("avc_ano"));
            avc_numeE.setValorInt(dt.getInt("avc_nume"));
            s="select p.pro_nomb,a.pro_codi,a.avp_ejelot,a.avp_serlot,a.avp_numpar,avp_numind,"
                + "avp_canti,avp_numuni,a.usu_nomb,a.alm_codori,a.alm_coddes, "
                + " avc_fecalb,avl_numpal,avl_numcaj "
                + " from v_albventa_detalle as a left join v_articulo as p on a.pro_codi =p.pro_codi  WHERE a.emp_codi="+EU.em_cod+
                " and avc_serie='X' and avc_ano ="+dt.getInt("avc_ano")+
                " and avc_nume = "+dt.getInt("avc_nume");            
            if (! dtCon1.select(s))
            {
               msgBox("Registro no encontrado. Probablemente se borro");
               jt.removeAllDatos();
               return;
            }
            usu_nombE.setText(dtCon1.getString("usu_nomb"));
            alm_codioE.setValor(dtCon1.getInt("alm_codori"));
            alm_codifE.setValor(dtCon1.getInt("alm_coddes"));
            avc_fecalbE.setText(dtCon1.getFecha("avc_fecalb","dd-MM-yyyy"));
            jt.removeAllDatos();
            do
            {
                ArrayList v=new ArrayList();
                v.add(dtCon1.getInt("pro_codi")); // 0
                v.add(dtCon1.getString("pro_nomb")); // 1
                v.add(dtCon1.getInt("avp_ejelot"));
                v.add(dtCon1.getString("avp_serlot")); // 3
                v.add(dtCon1.getInt("avp_numpar")); // 4
                v.add(dtCon1.getInt("avp_numind")); // 5
                v.add(dtCon1.getDouble("avp_canti")); // 6
                v.add(dtCon1.getInt("avp_numuni")); // 7
                v.add(dtCon1.getInt("avl_numpal")); // 8
                v.add(dtCon1.getInt("avl_numcaj")); // 8
                v.add("S"); // 8
                jt.addLinea(v);
            } while (dtCon1.next());
            jt.requestFocusLater(0, 0);
            calcAcumulados();
        } catch (SQLException k)
        {
            Error("Error al ver datos",k);
        }
    }
    private void calcAcumulados()
    {
        int nPiezas=0;
        double kilos=0;
        int nRow=jt.getRowCount();
        for (int n=0;n<nRow;n++)
        {
            if (jt.getValorInt(n,JT_ARTIC)>0 && jt.getValBoolean(n,JT_INSER))
            {
                nPiezas+=jt.getValorInt(n,JT_UNID);
                kilos+=jt.getValorDec(n,JT_PESO);
            }
        }
        numIndE.setValorDec(nPiezas);
        kilosE.setValorDec(kilos);
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

        pro_codiE = new gnu.chu.camposdb.proPanel()
        {

            @Override
            protected void despuesLlenaCampos()
            {
                int deoUnid=getUnidIndi();
                deo_unidE.setValorInt(deoUnid);
                if (deoUnid<1)
                return;

                jt.procesaAllFoco();
                jt.setValor(true,JT_INSER);
                jt.mueveSigLinea();
            }
            public void afterSalirLote(ayuLote ayuLot)
            {
                if (ayuLot.consulta)
                {
                    jt.setValor(ayuLot.jt.getValString(ayuLot.rowAct,ayuLote.JT_EJE),JT_EJERC);
                    jt.setValor(ayuLot.jt.getValString(ayuLot.rowAct,ayuLote.JT_SER),JT_SERIE);
                    jt.setValor(ayuLot.jt.getValString(ayuLot.rowAct,ayuLote.JT_LOTE ),JT_LOTE);
                    jt.setValor(ayuLot.jt.getValString(ayuLot.rowAct,ayuLote.JT_IND),JT_INDI);
                    jt.setValor(ayuLot.jt.getValString(ayuLot.rowAct,ayuLote.JT_PESO),JT_PESO);
                    jt.setValor(ayuLot.jt.getValString(ayuLot.rowAct,ayuLote.JT_NUMUNI),JT_UNID);
                    resetCambioLineaCab();
                }
            }
        }
        ;
        pro_nocabE = new gnu.chu.controles.CTextField();
        deo_ejelotE = new gnu.chu.controles.CTextField(Types.DECIMAL, "###9");
        deo_serlotE = new gnu.chu.controles.CTextField(Types.CHAR, "X",1);
        pro_loteE = new gnu.chu.controles.CTextField(Types.DECIMAL, "####9");
        deo_kilosE = new gnu.chu.controles.CTextField(Types.DECIMAL, "---,--9.99");
        pro_numindE = new gnu.chu.controles.CTextField(Types.DECIMAL, "###9");
        deo_unidE = new gnu.chu.controles.CTextField(Types.DECIMAL, "###9");
        linselE = new gnu.chu.controles.CCheckBox("S","N");
        IFLote = new gnu.chu.controles.CInternalFrame();
        cPanel1 = new gnu.chu.controles.CPanel();
        cLabel8 = new gnu.chu.controles.CLabel();
        pro_codi1E = new gnu.chu.camposdb.proPanel();
        cLabel9 = new gnu.chu.controles.CLabel();
        deo_ejelot1E = new gnu.chu.controles.CTextField(Types.DECIMAL, "###9");
        deo_serlot1E = new gnu.chu.controles.CTextField(Types.CHAR, "X",1);
        cLabel10 = new gnu.chu.controles.CLabel();
        cLabel11 = new gnu.chu.controles.CLabel();
        pro_indiniE = new gnu.chu.controles.CTextField(Types.DECIMAL, "###9");
        cLabel12 = new gnu.chu.controles.CLabel();
        pro_lote1E = new gnu.chu.controles.CTextField(Types.DECIMAL, "####9");
        cLabel13 = new gnu.chu.controles.CLabel();
        pro_indfinE = new gnu.chu.controles.CTextField(Types.DECIMAL, "###9");
        Bcancelar1 = new gnu.chu.controles.CButton(Iconos.getImageIcon("cancel"));
        Baceptar1 = new gnu.chu.controles.CButton(Iconos.getImageIcon("check"));
        avl_numpalE = new gnu.chu.controles.CTextField(Types.DECIMAL, "##9");
        avl_numcajE = new gnu.chu.controles.CTextField(Types.DECIMAL, "##9");
        Pinic = new gnu.chu.controles.CPanel();
        Pcabe = new gnu.chu.controles.CPanel();
        cLabel1 = new gnu.chu.controles.CLabel();
        avc_anoE = new gnu.chu.controles.CTextField(Types.DECIMAL,"9999");
        avc_numeE = new gnu.chu.controles.CTextField(Types.DECIMAL,"####9");
        cLabel2 = new gnu.chu.controles.CLabel();
        avc_fecalbE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel3 = new gnu.chu.controles.CLabel();
        alm_codioE = new gnu.chu.controles.CComboBox();
        cLabel4 = new gnu.chu.controles.CLabel();
        alm_codifE = new gnu.chu.controles.CComboBox();
        BirGrid = new gnu.chu.controles.CButton();
        cLabel5 = new gnu.chu.controles.CLabel();
        usu_nombE = new gnu.chu.controles.CTextField();
        jt = new gnu.chu.controles.CGridEditable(11){
            @Override
            public int cambiaLinea(int row, int col)
            {
                int reCaLin=cambiaLineajt(row);
                return reCaLin;
            }
            public void afterCambiaLinea()
            {
                pro_codiE.setAlmacen(alm_codioE.getValorInt());
                calcAcumulados();
            }
            public boolean afterInsertaLinea(boolean insLinea)
            {
                if (jt.getSelectedRow()==0)
                return true;
                jt.setValor(jt.getValorInt(jt.getSelectedRow()-1,JT_NUMPAL),JT_NUMPAL);
                jt.setValor(jt.getValorInt(jt.getSelectedRow()-1,JT_NUMCAJ),JT_NUMCAJ);
                return true;
            }
        }
        ;
        Ppie = new gnu.chu.controles.CPanel();
        Baceptar = new gnu.chu.controles.CButton();
        Bcancelar = new gnu.chu.controles.CButton();
        cLabel6 = new gnu.chu.controles.CLabel();
        numIndE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        cLabel7 = new gnu.chu.controles.CLabel();
        kilosE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##,##9.99");
        Bselec = new gnu.chu.controles.CButtonMenu(Iconos.getImageIcon("filter"));
        Bherr = new gnu.chu.controles.CButtonMenu(Iconos.getImageIcon("configure"));
        Bfill = new gnu.chu.controles.CButton(Iconos.getImageIcon("fill"));

        pro_nocabE.setEnabled(false);

        deo_serlotE.setText("A");
        deo_serlotE.setMayusc(true);

        deo_kilosE.setLeePesoBascula(botonBascula);
        deo_kilosE.setToolTipText("Pulse <F1> Para coger peso de la bascula");

        deo_unidE.setText("1");

        linselE.setText("cCheckBox1");

        IFLote.setTitle("Traspaso Individuos");
        IFLote.setVisible(true);

        cPanel1.setLayout(null);

        cLabel8.setText("Articulo");
        cPanel1.add(cLabel8);
        cLabel8.setBounds(10, 10, 50, 15);
        cPanel1.add(pro_codi1E);
        pro_codi1E.setBounds(60, 10, 300, 17);

        cLabel9.setText("Lote");
        cPanel1.add(cLabel9);
        cLabel9.setBounds(280, 32, 30, 15);
        cPanel1.add(deo_ejelot1E);
        deo_ejelot1E.setBounds(60, 32, 40, 17);

        deo_serlot1E.setText("A");
        deo_serlotE.setMayusc(true);
        cPanel1.add(deo_serlot1E);
        deo_serlot1E.setBounds(200, 32, 12, 17);

        cLabel10.setText("Individuo Inicial ");
        cPanel1.add(cLabel10);
        cLabel10.setBounds(10, 54, 100, 15);

        cLabel11.setText("Serie");
        cPanel1.add(cLabel11);
        cLabel11.setBounds(160, 32, 40, 15);
        cPanel1.add(pro_indiniE);
        pro_indiniE.setBounds(110, 54, 40, 17);

        cLabel12.setText("Ejercicio");
        cPanel1.add(cLabel12);
        cLabel12.setBounds(10, 32, 50, 15);
        cPanel1.add(pro_lote1E);
        pro_lote1E.setBounds(310, 32, 50, 17);

        cLabel13.setText("Individuo Final");
        cPanel1.add(cLabel13);
        cLabel13.setBounds(220, 54, 90, 15);
        cPanel1.add(pro_indfinE);
        pro_indfinE.setBounds(320, 54, 40, 17);

        Bcancelar1.setText("Cancelar");
        cPanel1.add(Bcancelar1);
        Bcancelar1.setBounds(260, 90, 100, 30);

        Baceptar1.setText("Aceptar");
        cPanel1.add(Baceptar1);
        Baceptar1.setBounds(10, 90, 100, 30);

        IFLote.getContentPane().add(cPanel1, java.awt.BorderLayout.CENTER);

        Pinic.setLayout(new java.awt.GridBagLayout());

        Pcabe.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Pcabe.setMaximumSize(new java.awt.Dimension(460, 75));
        Pcabe.setMinimumSize(new java.awt.Dimension(460, 75));
        Pcabe.setPreferredSize(new java.awt.Dimension(460, 75));
        Pcabe.setLayout(null);

        cLabel1.setText("Traspaso ");
        Pcabe.add(cLabel1);
        cLabel1.setBounds(2, 2, 55, 17);
        Pcabe.add(avc_anoE);
        avc_anoE.setBounds(70, 2, 37, 17);
        Pcabe.add(avc_numeE);
        avc_numeE.setBounds(110, 2, 52, 17);

        cLabel2.setText("Usuario");
        Pcabe.add(cLabel2);
        cLabel2.setBounds(310, 2, 50, 17);
        Pcabe.add(avc_fecalbE);
        avc_fecalbE.setBounds(230, 2, 67, 17);

        cLabel3.setText("Almacen Origen ");
        Pcabe.add(cLabel3);
        cLabel3.setBounds(2, 25, 99, 17);
        Pcabe.add(alm_codioE);
        alm_codioE.setBounds(110, 25, 344, 17);

        cLabel4.setText("Almacen Final");
        Pcabe.add(cLabel4);
        cLabel4.setBounds(2, 45, 96, 17);
        Pcabe.add(alm_codifE);
        alm_codifE.setBounds(110, 45, 344, 17);

        BirGrid.setText("cButton1");
        Pcabe.add(BirGrid);
        BirGrid.setBounds(400, 62, 1, 1);

        cLabel5.setText("Fecha ");
        Pcabe.add(cLabel5);
        cLabel5.setBounds(190, 2, 35, 17);

        usu_nombE.setEnabled(false);
        Pcabe.add(usu_nombE);
        usu_nombE.setBounds(360, 2, 93, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        Pinic.add(Pcabe, gridBagConstraints);

        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jt.setPreferredSize(new java.awt.Dimension(101, 101));

        ArrayList v=new ArrayList();
        v.add("Artic"); // 0
        v.add("Nombre"); // 1
        v.add("Ejerc"); // 2
        v.add("Serie"); // 3
        v.add("Lote"); // 4
        v.add("Indiv"); // 5
        v.add("Peso"); // 6
        v.add("Unid."); // 7
        v.add("Palet"); // 8
        v.add("N.Caja"); // 9
        v.add("NL"); // 9
        jt.setCabecera(v);
        jt.setAnchoColumna(new int[]    {60,120,40,40,60,30, 50,40, 40,40,40});
        jt.setAlinearColumna(new int[] {2,0,2,1,2,2, 2,2,2, 2,1});
        jt.setFormatoColumna(JT_INSER, "BSN");

        jt.resetRenderer(JT_INSER);

        linselE.setEnabled(false);
        linselE.setSelected(true);
        try {
            ArrayList vc1=new ArrayList();
            vc1.add(pro_codiE.getTextField()); // 0
            vc1.add(pro_nocabE); // 1
            vc1.add(deo_ejelotE); // 2
            vc1.add(deo_serlotE); // 3
            vc1.add(pro_loteE); // 4
            vc1.add(pro_numindE); // 5
            vc1.add(deo_kilosE); // 6
            vc1.add(deo_unidE); // 7
            vc1.add(avl_numpalE); // 8
            vc1.add(avl_numcajE); // 9
            vc1.add(linselE); // 10
            jt.setCampos(vc1);
            jt.setFormatoCampos();
        } catch (Exception k){Error("Error al cargar campos en grid",k);}

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Pinic.add(jt, gridBagConstraints);

        Ppie.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        Ppie.setMaximumSize(new java.awt.Dimension(550, 25));
        Ppie.setMinimumSize(new java.awt.Dimension(550, 25));
        Ppie.setPreferredSize(new java.awt.Dimension(550, 25));
        Ppie.setLayout(null);

        Baceptar.setText("Aceptar");
        Ppie.add(Baceptar);
        Baceptar.setBounds(360, 0, 90, 24);

        Bcancelar.setText("Cancelar");
        Ppie.add(Bcancelar);
        Bcancelar.setBounds(460, 0, 90, 24);

        cLabel6.setText("Unidades");
        Ppie.add(cLabel6);
        cLabel6.setBounds(10, 2, 60, 17);

        numIndE.setEditable(false);
        Ppie.add(numIndE);
        numIndE.setBounds(70, 2, 40, 17);

        cLabel7.setText("Kilos");
        Ppie.add(cLabel7);
        cLabel7.setBounds(120, 2, 40, 17);

        kilosE.setEditable(false);
        Ppie.add(kilosE);
        kilosE.setBounds(160, 2, 60, 17);

        Bselec.addMenu("Todos");
        Bselec.addMenu("Ninguno");
        Bselec.addMenu("Invert");
        Ppie.add(Bselec);
        Bselec.setBounds(230, 2, 40, 20);

        Bherr.addMenu("Lote");
        Bherr.addMenu("List.Ubic");
        Bherr.addMenu("Importar");
        Ppie.add(Bherr);
        Bherr.setBounds(280, 2, 40, 20);

        Bfill.setToolTipText("Copiar Ant. Linea (F5)");
        Ppie.add(Bfill);
        Bfill.setBounds(330, 2, 24, 24);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        Pinic.add(Ppie, gridBagConstraints);

        getContentPane().add(Pinic, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CButton Baceptar1;
    private gnu.chu.controles.CButton Bcancelar;
    private gnu.chu.controles.CButton Bcancelar1;
    private gnu.chu.controles.CButton Bfill;
    private gnu.chu.controles.CButtonMenu Bherr;
    private gnu.chu.controles.CButton BirGrid;
    private gnu.chu.controles.CButtonMenu Bselec;
    private gnu.chu.controles.CInternalFrame IFLote;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Pinic;
    private gnu.chu.controles.CPanel Ppie;
    private gnu.chu.controles.CComboBox alm_codifE;
    private gnu.chu.controles.CComboBox alm_codioE;
    private gnu.chu.controles.CTextField avc_anoE;
    private gnu.chu.controles.CTextField avc_fecalbE;
    private gnu.chu.controles.CTextField avc_numeE;
    private gnu.chu.controles.CTextField avl_numcajE;
    private gnu.chu.controles.CTextField avl_numpalE;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel10;
    private gnu.chu.controles.CLabel cLabel11;
    private gnu.chu.controles.CLabel cLabel12;
    private gnu.chu.controles.CLabel cLabel13;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.controles.CLabel cLabel9;
    private gnu.chu.controles.CPanel cPanel1;
    private gnu.chu.controles.CTextField deo_ejelot1E;
    private gnu.chu.controles.CTextField deo_ejelotE;
    private gnu.chu.controles.CTextField deo_kilosE;
    private gnu.chu.controles.CTextField deo_serlot1E;
    private gnu.chu.controles.CTextField deo_serlotE;
    private gnu.chu.controles.CTextField deo_unidE;
    private gnu.chu.controles.CGridEditable jt;
    private gnu.chu.controles.CTextField kilosE;
    private gnu.chu.controles.CCheckBox linselE;
    private gnu.chu.controles.CTextField numIndE;
    private gnu.chu.camposdb.proPanel pro_codi1E;
    private gnu.chu.camposdb.proPanel pro_codiE;
    private gnu.chu.controles.CTextField pro_indfinE;
    private gnu.chu.controles.CTextField pro_indiniE;
    private gnu.chu.controles.CTextField pro_lote1E;
    private gnu.chu.controles.CTextField pro_loteE;
    private gnu.chu.controles.CTextField pro_nocabE;
    private gnu.chu.controles.CTextField pro_numindE;
    private gnu.chu.controles.CTextField usu_nombE;
    // End of variables declaration//GEN-END:variables
  /**
   * Devuelve la fecha minima de mvto para un albaran
   * @param avcAno
   * @param empCodi
   * @param avcSerie
   * @param avcNume
   * @return
   * @throws SQLException 
   */
  java.sql.Date getMinFechaMvto( int avcAno,int empCodi,String avcSerie, int avcNume) throws SQLException
  {
    dtStat.select("SELECT min(avl_fecalt) as avl_fecalt FROM v_albavel WHERE "+
         " avc_ano =" + avcAno +
        " and emp_codi = " + empCodi +
        " and avc_serie = '" + avcSerie+ "'" +
        " and avc_nume = " + avcNume);
    return dtStat.getDate("avl_fecalt");
  }
    /**
   * Comprueba si ha tenido mvtos algun individuo
   */
  String checkMvtos()
  {
    
      try 
      {
        java.sql.Date fecMinMvt=getMinFechaMvto(avc_anoE.getValorInt(), EU.em_cod,"X",
                       avc_numeE.getValorInt());
        if (Formatear.comparaFechas(pdalmace.getFechaInventario(alm_codioE.getValorInt(), dtStat) , fecMinMvt)>= 0 )
          return "Almacen "+alm_codioE.getValorInt() +" con Mvtos anteriores a Ult. Fecha Inventario. Imposible Editar/Borrar";
        if (Formatear.comparaFechas(pdalmace.getFechaInventario(alm_codifE.getValorInt(), dtStat) , fecMinMvt)>= 0 )
          return "Almacen "+alm_codifE.getValorInt() +" con Mvtos anteriores a Ult. Fecha Inventario. Imposible Editar/Borrar";
        int nl = jt.getRowCount();
        int ret=mensajes.NO;
        for (int n = 0; n < nl; n++)    
        {
            if (!checkStock(jt.getValorInt(n, JT_ARTIC),
                jt.getValorInt(n, JT_EJERC),EU.em_cod,
                jt.getValString(n, JT_SERIE),jt.getValorInt(n, JT_LOTE),
                jt.getValorInt(n, JT_INDI),alm_codifE.getValorInt()))
            { // Sin Registro Stock
                
                String msg="Sin registro stock de "+jt.getValorInt(n, JT_ARTIC)+" "+
                     jt.getValorInt(n, JT_EJERC)+jt.getValString(n, JT_SERIE)+"-"+
                     jt.getValorInt(n, JT_LOTE)+"/"+jt.getValorInt(n, JT_INDI);
                if (ARG_ADMIN)
                {
                    ret=mensajes.mensajePreguntar(msg+"\ncontinuar");
                    if (ret!=mensajes.YES)
                        return msg;        
                }
                else
                    return msg;                
            }
            if (ret != mensajes.YES &&  jt.getValorDec(n,JT_PESO)!= dtStat.getDouble("stp_kilact"))
            {
                 return "Kilos actuales no son los traspasados de:  "+jt.getValorInt(n, JT_ARTIC)+" "+
                     jt.getValorInt(n, JT_EJERC)+jt.getValString(n, JT_SERIE)+"-"+
                     jt.getValorInt(n, JT_LOTE)+"/"+jt.getValorInt(n, JT_INDI);
            }
        }
        return null;
      } catch (SQLException k)
      {
          Error("Error al chequear mvtos",k);
          return "Error";
      }
  }
  /**
   * Borrar el traspaso.
   * @return fecha de Alta de albaran
   */
  Timestamp borrarTraspaso() throws SQLException
  {
      
      s=" where emp_codi = "+EU.em_cod+
          " AND avc_ano =" +avc_anoE.getValorInt()+" and avc_serie = 'X' and avc_nume= "+avc_numeE.getValorInt();
      mensaje("Espere, por favor ... Borrando Traspaso");
      if (!dtStat.select("select * from v_albvenpar "+s))
          return null;
      
//      do
//      {    
//        stkPart.anuStkPart(dtStat.getInt("pro_codi"),
//            dtStat.getInt("avp_ejelot"),
//             EU.em_cod,
//             dtStat.getString("avp_serlot"),
//             dtStat.getInt("avp_numpar"),
//             dtStat.getInt("avp_numind"),
//             alm_codifE.getValorInt(),
//             dtStat.getDouble("avp_canti"),
//             dtStat.getInt("avp_numuni"));
//                     
//         stkPart.sumar(dtStat.getInt("avp_ejelot"),dtStat.getString("avp_serlot"),
//            dtStat.getInt("avp_numpar"), dtStat.getInt("avp_numind"),            
//            dtStat.getInt("pro_codi"),
//            alm_codioE.getValorInt(),
//            dtStat.getDouble("avp_canti"), dtStat.getInt("avp_numuni"));        
//      }  while (dtStat.next());
      dtStat.select("select avl_fecalt,alm_coddes from v_albventa "+s);
      Timestamp fecAlta=dtStat.getTimeStamp("avl_fecalt");
      
      if (pdalmace.isAlmacenExterno(dtStat.getInt("alm_coddes"),dtStat))
      { // Era entrada a almacen externo. Quito almacen y caja en stock-partida
        dtAdd.executeUpdate("update stockpart"
            + " SET stp_numpal=0, STP_NUMCAJ=0 where exists(select pro_codi from v_albvenpar as v "+s+
            " and v.pro_codi =  stockpart.pro_codi "
            + "and v.avp_ejelot=stockpart.eje_nume "
            + "and  v.avp_serlot=stockpart.pro_serie "
            + "and v.avp_numpar=stockpart.pro_nupar "
            + "and v.avp_numind=stockpart.pro_numind)");        
      }
      dtAdd.executeUpdate("delete from v_albvenpar "+s);
      dtAdd.executeUpdate("delete from v_albavel "+s);
      dtAdd.executeUpdate("delete from v_albavec "+s);
      return fecAlta;
  }
  /**
   * Crear el traspaso.
   * @param fecAlta Fecha de alta para mvtos (Null si debe ser la del dia)
   * @param numAlbaran si 0 asignar uno nuevo
   * @return numero albaran generado
   */
  int traspDato1(Timestamp fecAlta,int numAlb) throws SQLException, ParseException
  {         
     
      mensaje("Espere, por favor ... traspasando Individuos");

      // Busco el numero de Albaran a asignar.
      if (numAlb==0)
      {
        s = "SELECT num_serieX FROM v_numerac WHERE emp_codi = " + EU.em_cod +
            " AND eje_nume = " + EU.ejercicio;
        if (!dtCon1.select(s))
          throw new SQLException("s: " + s + "\nError al buscar numeracion serie X");
        numAlb = dtCon1.getInt("num_serieX");
        numAlb++;
      // Lo guardo .
        s = "UPDATE v_numerac set  num_serieX = " + numAlb +
            " WHERE emp_codi = " + EU.em_cod +
            " AND eje_nume = " + EU.ejercicio;

        stUp.executeUpdate(s);
      }
      boolean isAlmExt=pdalmace.isAlmacenExterno(alm_codifE.getValorInt(),dtStat);

      // Genero la cabecera del Albaran
      dtCon1.addNew("v_albavec");

      dtCon1.setDato("emp_codi", EU.em_cod);
      dtCon1.setDato("avc_ano", EU.ejercicio);
      dtCon1.setDato("cli_codi",cliCodi);
      dtCon1.setDato("avc_serie", "X");
      dtCon1.setDato("avc_nume", numAlb);
      dtCon1.setDato("avc_fecalb", avc_fecalbE.getText(), "dd-MM-yyyy");
      dtCon1.setDato("usu_nomb", EU.usuario);
      dtCon1.setDato("alm_codori", alm_codioE.getValor());
      dtCon1.setDato("alm_coddes", alm_codifE.getValor());
      dtCon1.setDato("avc_almori", alm_codioE.getValor());
      dtCon1.setDato("avc_cerra", -1); // Cerrado
      dtCon1.setDato("avc_tarimp", 0);
      dtCon1.setDato("div_codi",1);
      dtCon1.update(stUp);

      int nl = jt.getRowCount();
      int prvCodi;
      Date fecCad;
      for (int n = 0; n < nl; n++)
      {
        if (!jt.getValBoolean(n, JT_INSER) || jt.getValorInt(n,JT_ARTIC)==0)
          continue;
        if (! checkStock(jt.getValorInt(n, JT_ARTIC),
            jt.getValorInt(n, JT_EJERC),EU.em_cod,
            jt.getValString(n, JT_SERIE),jt.getValorInt(n, JT_LOTE),
            jt.getValorInt(n, JT_INDI),alm_codioE.getValorInt()))
        {
                continue;
        }        
        prvCodi=dtStat.getInt("prv_codi");
        fecCad=dtStat.getDate("stp_feccad");
        // Insertamos linea de albaran
        dtCon1.addNew("v_albavel");
        dtCon1.setDato("emp_codi", EU.em_cod);
        dtCon1.setDato("avc_ano", EU.ejercicio);
        dtCon1.setDato("avc_serie", "X");
        dtCon1.setDato("avc_nume", numAlb);
        dtCon1.setDato("avl_numlin", n);
        dtCon1.setDato("avl_unid", jt.getValorInt(n, JT_UNID));
        dtCon1.setDato("pro_codi", jt.getValorInt(n, JT_ARTIC));
        dtCon1.setDato("pro_nomb", jt.getValString(n, JT_NOMBR));
        dtCon1.setDato("alm_codi", alm_codifE.getValor());
        dtCon1.setDato("avl_canti", jt.getValorDec(n, JT_PESO));
        dtCon1.setDato("avl_numpal",jt.getValorInt(n,JT_NUMPAL));
        dtCon1.setDato("avl_numcaj",jt.getValorInt(n,JT_NUMCAJ));
        dtCon1.setDato("avc_cerra",-1);
//        dtCon1.setDato("avl_fecalt","{ts '"+Formatear.getFecha(avc_fecalbE.getDate(),"yyyy-MM-dd")+ " 01:01:01'}");
        if (fecAlta==null)
            dtCon1.setDato("avl_fecalt","current_timestamp");
        else
            dtCon1.setDato("avl_fecalt",fecAlta);
        dtCon1.update(stUp);

        // Insertamos linea partida de albaran
        dtCon1.addNew("v_albvenpar");

        dtCon1.setDato("emp_codi", EU.em_cod);
        dtCon1.setDato("avc_ano", EU.ejercicio);
        dtCon1.setDato("avc_serie", "X");
        dtCon1.setDato("avc_nume", numAlb);
        dtCon1.setDato("avl_numlin", n);
        dtCon1.setDato("pro_codi", jt.getValorInt(n, JT_ARTIC));        
        dtCon1.setDato("avp_ejelot", jt.getValorInt(n, JT_EJERC));
        dtCon1.setDato("avp_emplot", EU.em_cod);
        dtCon1.setDato("avp_serlot",  jt.getValString(n, JT_SERIE));
        dtCon1.setDato("avp_numpar", jt.getValorInt(n, JT_LOTE));
        dtCon1.setDato("avp_numind", jt.getValorInt(n, JT_INDI));
        dtCon1.setDato("avp_numuni", jt.getValorInt(n,JT_UNID));
        dtCon1.setDato("avp_canti", jt.getValorDec(n, JT_PESO));
        dtCon1.setDato("avp_canori", jt.getValorDec(n, JT_PESO));
        dtCon1.setDato("avp_canbru", jt.getValorDec(n, JT_PESO));
        dtCon1.update(stUp);      
        if (isAlmExt)
        {
          if (dtAdd.executeUpdate("update stockpart"+
             " SET stp_numpal="+jt.getValorInt(n,JT_NUMPAL)+
             ", STP_NUMCAJ="+jt.getValorInt(n,JT_NUMCAJ)+
              " where pro_codi ="+jt.getValorInt(n, JT_ARTIC)+
              " and pro_serie='"+ jt.getValString(n, JT_SERIE)+"'"+
              " and pro_nupar="+jt.getValorInt(n, JT_LOTE)+
              " and pro_numind="+jt.getValorInt(n, JT_INDI)+
              " and alm_codi = "+alm_codifE.getValorInt())==0)
              throw new SQLException("Error al establecer palet en individuo: "+
                 jt.getValorInt(n, JT_ARTIC)+"-"+
                 jt.getValString(n, JT_SERIE)+
                 jt.getValorInt(n, JT_LOTE)+"-"+
                 jt.getValorInt(n, JT_INDI));
              
        }
      }
      
      if (fecAlta!=null)
      { // Poner mvtos a fecha anterior
         s="update mvtosalm set mvt_time = {d '"+Formatear.getFecha(fecAlta,"yyyyMMdd")+"'}"+          
          " where mvt_tipdoc='V' and mvt_empcod="+EU.em_cod+
          " and mvt_numdoc="+numAlb+
          " and mvt_serdoc = '"+"X"+"'"+
          " and mvt_ejedoc ="+avc_anoE.getValorInt();
         dtCon1.executeUpdate(s);
      }
     
      return numAlb;
    
    
  }
  private boolean checkFecha()
  {
      try
      {
          if (avc_fecalbE.isNull())
          {
              mensajeErr("Introduzca Fecha de Traspaso");
              avc_fecalbE.requestFocus();
              return false;
          }
          s=pdejerci.checkFecha(dtStat,avc_anoE.getValorInt(),EU.em_cod,avc_fecalbE.getText(),true);
          if (s!=null)
          {
              mensajeErr(s);
              avc_fecalbE.requestFocus();
              return false;
          }
          if (Math.abs(Formatear.comparaFechas(avc_fecalbE.getDate(),Formatear.getDateAct()))>7 && ! ARG_ADMIN)
          {
              mensajeErr("La fecha deberia es un rango de 7 dias sobre la actual");
              avc_fecalbE.requestFocus();
              return false;
          }
          if (alm_codifE.getValor().equals(alm_codioE.getValor()))
          {
              mensajeErr("Los dos almacenes NO pueden ser iguales");
              alm_codioE.requestFocus();
              return false;
          }
          pro_codiE.setAlmacen(alm_codioE.getValorInt());
          return true;
      } catch (SQLException | ParseException ex )
      {
          Error("Error al comprobar datos", ex);
          return false;
      }     
  }
    
  void importaDat()  
  {
        if (!checkFecha())
            return;

        try
        {
            configurarFile();
            int returnVal = ficeleE.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION)
                fichero = ficeleE.getSelectedFile().getAbsolutePath();
            else
                return;
        } catch (Exception k)
        {
            fatalError("error al elegir el fichero", k);
        }
        new miThread("")
        {
            @Override
            public void run() {
                msgEspere("Importando datos...");
                importaDat0();
                resetMsgEspere();
            }
        };
   }
   private void importaDat0()
   {
       try 
       {
          ArrayList<ArrayList> al = utsql.load(fichero, ";");
          int nEle=al.size();
          jt.setEnabled(false);      
          jt.removeAllDatos();
          int  numErrorStock=0;
          int n;
          for ( n=0;n<nEle;n++)
          {
            ArrayList<String> lin=al.get(n);
             if (lin.size()<=7)
                lin.add("1");
            if (lin.size()<=8)
                lin.add("0");
            if (lin.size()<=9)
                lin.add("0");
            if (lin.size()<=10)
                lin.add("S");
            if (! checkStock(Integer.parseInt(lin.get(JT_ARTIC)),
                Integer.parseInt(lin.get(JT_EJERC)),
                 EU.em_cod,
                lin.get(JT_SERIE),
                Integer.parseInt(lin.get(JT_LOTE)),
                Integer.parseInt(lin.get(JT_INDI)),
                alm_codioE.getValorInt()))
            {
               numErrorStock++;
//               lin.set(JT_UNID, 0);
               lin.set(JT_INSER,"N");
            }
            if (lin.get(JT_PESO).contains(","))
                   lin.set(JT_PESO,lin.get(JT_PESO).replace(',','.'));
           
            
            jt.addLinea(lin);
          }
          jt.setEnabled(true);
          if (numErrorStock>0)
              msgBox("Se encontraron "+numErrorStock+" errores en control de Stocks");
          
          calcAcumulados();
          mensajeErr("Se importaron: "+n+" Lineas,  correctamente");
          activar(true);
      } catch (IOException ex)
      {
          Error("Error al importar datos",ex);
      }

      catch (SQLException ex)
      {
          Error("Error al chequear integridad en Base datos",ex);
      }
      catch (Exception ex)
      {
          Error("Otros errores",ex);
      }
  }
  /***
   * Comprueba si un individuo tiene stock disponible
   * @param proCodi
   * @param ejeNume
   * @param empCodi
   * @param proSerie
   * @param proNumlot
   * @param proNumind
   * @param almCodi
   * @return
   * @throws SQLException 
   */
  private boolean checkStock(int proCodi,int ejeNume,int empCodi,String proSerie,int proNumlot,int proNumind,int almCodi) throws SQLException
  {
       s = "SELECT * FROM V_STKPART WHERE " +
          " EJE_NUME= " + ejeNume+
          " AND EMP_CODI= " + empCodi +
          " AND PRO_SERIE='" + proSerie + "'" +
          " AND pro_nupar= " + proNumlot +
          " and pro_codi= " + proCodi +
          " and stp_kilact > 0" +
          " and alm_codi = " + almCodi+
          " and pro_numind = " + proNumind ;
      return dtStat.select(s);
  }
  void configurarFile() throws Exception
  {
      if (ficeleE != null)
        return;
      ficeleE = new JFileChooser();
      ficeleE.setName("Abrir Fichero");
      ficeleE.setCurrentDirectory(new java.io.File("c:/"));
  }
}
