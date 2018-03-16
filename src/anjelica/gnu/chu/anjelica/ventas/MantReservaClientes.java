package gnu.chu.anjelica.ventas;

/**
 *
 * <p>Título: MantReservasClientes</p>
 * <p>Descripción: Mantenimiento Reservas para clientes
 *  </p>
 * <p>Copyright: Copyright (c) 2005-2018
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
 * 
 * 
 */
import gnu.chu.anjelica.almacen.*;
import com.jgoodies.looks.plastic.PlasticInternalFrameUI;
import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.despiece.utildesp;
import gnu.chu.anjelica.menu;
import gnu.chu.anjelica.pad.pdejerci;
import gnu.chu.controles.StatusBar;
import gnu.chu.interfaces.PAD;
import gnu.chu.isql.utilSql;
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
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import javax.swing.JFileChooser;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class MantReservaClientes extends ventanaPad implements PAD
{
  private int cliCodio;
 
  
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

  private String s;
  private ActualStkPart stkPart;
  JFileChooser ficeleE=null;
 
  
  
    /**
     * Creates new form MantTraspAlm
     */
    public MantReservaClientes() {
        initComponents();
    }
    public MantReservaClientes(EntornoUsuario eu, Principal p) {
        this(eu, p, null);
    }

    public MantReservaClientes(EntornoUsuario eu, Principal p, Hashtable<String,String> ht) {
        EU = eu;
        vl = p.panel1;
        jf = p;
        eje = true;

        setTitulo("Mantenimiento Reservas a Clientes");

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
    public MantReservaClientes(menu p, EntornoUsuario eu) {
        this(p,eu, null);
    }
    public MantReservaClientes(menu p, EntornoUsuario eu,Hashtable ht) {
    

        EU = eu;
        vl = p.getLayeredPane();
        setTitulo("Mantenimiento Reservas a Clientes");
        eje = false;
       
        try
        {
            setParametros(ht);
            jbInit();
        } catch (Exception e)
        {
           ErrorInit(e);
        }
    }
    private void setParametros(Hashtable<String,String> ht)
    {
          if (ht!=null)
                setArgumentoAdmin(ht.get("admin")==null?false:
                    Boolean.parseBoolean(ht.get("admin")));
    }
    
    private void jbInit() throws Exception {      
        setVersion("2018-03-15 "+ (isArgumentoAdmin()?"ADMIN":""));
        
        nav = new navegador(this, dtCons, false,  navegador.NORMAL);
        
        statusBar = new StatusBar(this);
        iniciarFrame();
       
        this.getContentPane().add(nav, BorderLayout.NORTH);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        this.setPad(this);
        dtCons.setLanzaDBCambio(false);

        initComponents();
        strSql = getStrSql()+
             " where extract(year from rpc_fecha) >= "+(EU.ejercicio-1)+
             getOrderQuery();
        
        conecta();

        iniciarBotones(Baceptar, Bcancelar);
        navActivarAll();
        this.setSize(700, 524);
        activar(false);
    }
 
    String getStrSql()
    {
        return "select * from cabresprcli ";
    }
    String getOrderQuery()
    {
        return " order by rpc_fecha";
    }
  @Override
    public void iniciarVentana() throws Exception
    {
        Pcabe.setButton(KeyEvent.VK_F2, BirGrid);
        jt.setDefButton(Baceptar);

        Pcabe.setDefButton(Baceptar);
        IFLote.putClientProperty(
             PlasticInternalFrameUI.IS_PALETTE,
             Boolean.TRUE);
        IFLote.setSize(new Dimension(390, 150));
        IFLote.setIconifiable(false);
        IFLote.setResizable(false);
        IFLote.setClosable(false);
        pro_codi1E.iniciar(new DatosTabla(ct), this, vl, EU);
        cli_codiE.iniciar(dtStat,this,vl,EU);
        deo_ejelot1E.setValorInt(EU.ejercicio);
        deo_serlot1E.setText("A");
        pro_codiE.setProNomb(pro_nocabE);
        pro_codiE.iniciar(new DatosTabla(ct), this, vl, EU);
        pro_codiE.setCamposLote(rpc_ejelotE,  rpc_serlotE, rpc_numparE,
            rpc_numindE, rpc_cantiE);
        pro_codiE.setAyudaLotes(true);
      
              

        stkPart=new ActualStkPart(dtCon1,EU.em_cod);
        rpc_fechaE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
      
    
        rpc_idE.setColumnaAlias("rpc_id");       
        rpc_fechaE.setColumnaAlias("rpc_fecha");
        usu_nombE.setColumnaAlias("usu_nomb");
        cli_codiE.setColumnaAlias("cli_codi");
        rpc_cerraE.setColumnaAlias("rpc_cerra");
        activarEventos();
        navActivarAll();    
        verDatos(dtCons);
    }

    private void activarEventos()
    {
     
      Ptab1.addChangeListener(new ChangeListener()
      {
          @Override
          public void stateChanged(ChangeEvent e) {
              if (Ptab1.getSelectedIndex()==1 && cliCodio!=cli_codiE.getValorInt() 
                  && !nav.isEdicion())
              {
                  cliCodio=cli_codiE.getValorInt();
                  verDatosResumen(cli_codiE.getValorInt());
              }
           }
      }          );
      BirGrid.addFocusListener(new FocusAdapter()
      {
          @Override
          public void focusGained(FocusEvent e) {
              irGrid();
          }
      });
    }
  
    /**
     * 
     * @return 
     */
  private boolean checkFecha()
  {
      try
      {
          if (rpc_fechaE.isNull())
          {
              mensajeErr("Introduzca Fecha de Traspaso");
              rpc_fechaE.requestFocus();
              return false;
          }
          s=pdejerci.checkFecha(dtStat,EU.ejercicio,EU.em_cod,rpc_fechaE.getText(),true);
          if (s!=null)
          {
              mensajeErr(s);
              rpc_fechaE.requestFocus();
              return false;
          }
          if (Math.abs(Formatear.comparaFechas(rpc_fechaE.getDate(),Formatear.getDateAct()))>7 && ! isArgumentoAdmin())
          {
              mensajeErr("La fecha deberia es un rango de 7 dias sobre la actual");
              rpc_fechaE.requestFocus();
              return false;
          }
          pro_codiE.setAlmacen(pdalmace.ALMACENPRINCIPAL);
          return true;
      } catch (SQLException | ParseException ex )
      {
          Error("Error al comprobar datos", ex);
          return false;
      }     
    }
    private void irGrid() 
    {
      try
      {
          if (! checkFecha())
              return;
          if (!cli_codiE.controlar())
          {
              msgBox(cli_codiE.getMsgError());
              return;
          }
          if (swAddnew)
          {
              jt.setEnabled(true);
              jt.requestFocusInicio();
              swAddnew=false;
          }
          else
          {
              jt.requestFocusLater();
          }
      } catch (SQLException ex)
      {
          Error("Error al ir al grid",ex);
      }
            
    }
    private void activar(boolean estado, int modo)
    {
       
        Baceptar.setEnabled(estado);
        Bcancelar.setEnabled(estado);
        if (modo==navegador.DELETE)
            return;
        cli_codiE.setEnabled(estado);
        rpc_cerraE.setEnabled(estado);
        if (modo!=navegador.EDIT)
            rpc_fechaE.setEnabled(estado);
     
        Pcabe.setEnabled(estado);
       
        if (modo==navegador.QUERY || ! estado)
        {            
            rpc_idE.setEnabled(estado);
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
        cliCodio=0;
        rpc_idE.requestFocus();
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
//    
//    private void cargaDatosLote()
//    {
//      try {
//         
//         if (!pro_codi1E.controlar())
//         {
//           mensajeErr(pro_codi1E.getMsgError());
//           return;
//         }
//         if (pro_lote1E.getValorInt() == 0)
//         {
//           mensajeErr("Introduzca N. de Lote");
//           pro_lote1E.requestFocus();
//           return;
//         }
//         if ( deo_serlot1E.isNull())
//         {
//             mensajeErr("Introduzca Serie");
//             deo_serlot1E.requestFocus();
//             return;             
//         }
//       s = "SELECT * FROM V_STKPART WHERE " +
//          " EJE_NUME= " + deo_ejelot1E.getValorInt() +
//          " AND EMP_CODI= " + EU.em_cod+
//          " AND PRO_SERIE='" + deo_serlot1E.getText() + "'" +
//          " AND pro_nupar= " + pro_lote1E.getValorInt() +
//          " and pro_codi= " + pro_codi1E.getValorInt() +
//          " and stp_kilact > 0" +
//          " and alm_codi = " + pdalmace.ALMACENPRINCIPAL +
//          " and pro_numind >= " + pro_indiniE.getValorInt() +
//          (pro_indiniE.getValorInt() > 0 ?
//           " and PRO_NUMIND <= " + pro_indfinE.getValorInt() : "") +
//          " order by pro_numind";
//      
//      if (!dtCon1.select(s))
//      {
//        msgBox("No encontrados datos en Stock-Partidas");
//        pro_codiE.requestFocus();
//        return;
//      }
//      do
//      {
//        ArrayList v = new ArrayList();
//   
//        v.add(pro_codi1E.getValorInt());
//        v.add(pro_codi1E.getNombArt());
//        v.add(deo_ejelot1E.getValorInt());
//        v.add(deo_serlot1E.getText());
//        v.add(pro_lote1E.getValorInt());
//        v.add(dtCon1.getString("pro_numind"));
//        v.add(Formatear.format(dtCon1.getString("stp_kilact"),"##,##9.99"));
//        v.add(Formatear.format(dtCon1.getString("stp_unact"),"##9"));
//        v.add(true);
//        jt.addLinea(v);
//      } while (dtCon1.next());
//      } catch (SQLException k)
//      {
//          Error("Error al cargar datos",k);
//      }
//      calcAcumulados();
//     
//      mensajeErr("Cargados Individuos de Lote "+pro_lote1E.getValorInt());
//    }
//    private void ocultarVentanaLote() 
//    {
//      IFLote.setVisible(false);
//      this.setFoco(null);
//      this.setEnabled(true);
//      try {
//            this.setSelected(true);
//      } catch (Exception k){}
//    }
//    
//    private void verVentanaLote()
//    {
//      if (! checkFecha())
//          return;
//      if (addVentanaLote)
//        vl.add(IFLote);    
//     
//      addVentanaLote=false;
//      IFLote.setLocation(this.getLocation().x+100, this.getLocation().y+80);
//      try
//      {
//          IFLote.setSelected(true);
//      } catch (PropertyVetoException ex)
//      {
//          Logger.getLogger(MantReservaClientes.class.getName()).log(Level.SEVERE, null, ex);
//      }
//     this.setEnabled(false);
//     this.setFoco(IFLote);    
//     IFLote.setVisible(true);
//    }
  @Override
    public void PADAddNew() {
      Ptab1.setSelectedIndex(0);
      rpc_cerraE.setValor("0");
      
        mensaje("Introducir Nuevo traspaso");
        swAddnew=true;
        nav.pulsado = navegador.ADDNEW;
        nav.setEnabled(false);
        
        jt.removeAllDatos();
        resetCambioLineaCab();
        activar(true, navegador.ADDNEW);
        rpc_cerraE.setEnabled(false);
        Pcabe.resetTexto();
       
        rpc_fechaE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
        usu_nombE.setText(EU.usuario);
        

        rpc_fechaE.requestFocus();
    }
    void resetCambioLineaCab() {
        
        rpc_numindE.resetCambio();
        rpc_numparE.resetCambio();
        pro_codiE.resetCambio();
        rpc_ejelotE.resetCambio();
        rpc_serlotE.resetCambio();
        rpc_cantiE.resetCambio();
        rpc_numuniE.resetCambio();
    }
    /*
     * Devuelve clase StkPartid con el peso del individuo de origen activo
     * Escribe mensaje de error 
     */
    StkPartid buscaPeso() throws Exception {
       
        stkPartid = utildesp.buscaPeso(dtCon1, rpc_ejelotE.getValorInt(), EU.em_cod,
            rpc_serlotE.getText(), rpc_numparE.getValorInt(),
            rpc_numindE.getValorInt(), pro_codiE.getValorInt(), pdalmace.ALMACENPRINCIPAL,cli_codiE.getValorInt());
       
        if (! stkPartid.hasError(cli_codiE.getValorInt()))
            return stkPartid;
        
//        if (stkPartid.isLockIndiv())
//            return stkPartid;
        msgBox(stkPartid.getMensaje());
        return stkPartid;
    }
     /**
     * Devuelve -2 si no ha habido cambios
     *          -1 Si no encuentra el registro.
     *          >=0 Numero de unidades encontradas
     * @return
     */
    int buscaPesoLin() {
       


        try
        {
            stkPartid= buscaPeso();
            if ( ! stkPartid.hasError() && stkPartid.hasStock()) 
            {               
                if (stkPartid.isLockIndiv() )
                {
                    if (getNumeroReserva(dtStat,pro_codiE.getValorInt(),
                        rpc_ejelotE.getValorInt(),
                        rpc_serlotE.getText(),
                        rpc_numparE.getValorInt(),
                        rpc_numindE.getValorInt())!=rpc_idE.getValorInt() )
                    {
                         msgBox("Individuo ya esta reservado para este cliente en otra reserva");
                         jt.setValor(0, JT_PESO);
                         rpc_cantiE.setValorDec(0);
                         jt.setValor(0, JT_UNID);
                         rpc_numuniE.setValorDec(0);
                         return 0;
                    }    
                }
                jt.setValor(stkPartid.getKilos(), JT_PESO);
                rpc_cantiE.setValorDec(stkPartid.getKilos());
                jt.setValor(stkPartid.getUnidades(), JT_UNID);
                rpc_numuniE.setValorDec(stkPartid.getUnidades());                  
                return stkPartid.getUnidades();
            }
            
            jt.setValor(0, JT_PESO);
            rpc_cantiE.setValorDec(0);
            jt.setValor(0, JT_UNID);
            rpc_numuniE.setValorDec(0);
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
            if (pro_codiE.isNull() )
                return -1; // SI no hay producto o no esta puesto para insertar lo doy como bueno.
            if (!rpc_numindE.hasCambio() && !rpc_numparE.hasCambio()
            && !pro_codiE.hasCambio() && !rpc_serlotE.hasCambio()
            && !rpc_ejelotE.hasCambio() && !rpc_cantiE.hasCambio() && !rpc_numuniE.hasCambio())
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
            if (rpc_ejelotE.getValorInt() == 0 && pro_codiE.hasControlIndiv() )
            {
                mensajeErr("Introduzca Ejercicio de Lote");
                return JT_EJERC;
            }
            if (rpc_serlotE.getText().trim().equals("")  && pro_codiE.hasControlIndiv())
            {
                mensajeErr("Introduzca Serie de Lote");
                return JT_SERIE;
            }
            if (rpc_numparE.getValorInt() == 0  && pro_codiE.hasControlIndiv())
            {
                mensajeErr("Introduzca Número de Lote");
                return JT_LOTE;
            }
           
            int ret=buscaPesoLin();
            if (ret!=-2 && ret<=0)
                return 0;
            
            if (stkPartid.getKilos() < rpc_cantiE.getValorDec())
            {
                mensajeErr("Partida Sin kilos suficientes de Stock");
                return 0;
            }
            if (stkPartid.getUnidades() < rpc_numuniE.getValorInt())
            {
                mensajeErr("Partida Sin unidades suficientes de Stock");
                return 0;
            }
            if (stkPartid.getUnidades() == rpc_numuniE.getValorInt()
                && stkPartid.getKilos() != rpc_cantiE.getValorDec())
            {
                mensajeErr("Kilos deben ser los disponibles en stock para estas unidades");
                return JT_PESO;
            }
         
            // Compruebo que no me metan el mismo individuo dos veces ?
  
            int nRow = jt.getRowCount();
            for (int n = 0; n < nRow; n++)
            {
                if (n == linea)
                    continue;
                if (jt.getValorInt(n, JT_ARTIC) == pro_codiE.getValorInt()
                    && jt.getValorInt(n, JT_EJERC) == rpc_ejelotE.getValorInt()
                    && jt.getValString(n, JT_SERIE).equals(rpc_serlotE.getText())
                    && jt.getValorInt(n, JT_LOTE) == rpc_numparE.getValorInt()
                    && jt.getValorInt(n, JT_INDI) == rpc_numindE.getValorInt())
                {
                    mensajes.mensajeAviso("!! ATENCION !!! Linea Repetida en posicion:   " + n);
                    return 0;
                }
            }
            

        } catch (Exception k)
        {
            Error("Error al Controlar Linea de Cabececera", k);
            return 0;
        }
        cli_codiE.setEnabled(false);
        resetCambioLineaCab();
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
        
        v.add(rpc_idE.getStrQuery());       
        v.add(rpc_fechaE.getStrQuery());
        v.add(usu_nombE.getStrQuery());
        v.add(cli_codiE.getStrQuery());
        v.add(rpc_cerraE.getStrQuery());
        s = creaWhere(getStrSql(), v, true);
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
        if (nCol>=0)
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
            borrarTraspaso();          
            numAlb=traspDato1(rpc_idE.getValorInt());
            dtAdd.commit();
        } catch (SQLException | ParseException k)
        {
            Error("Error al actualizar Traspasado",k);
        }
         
        mensaje("");
        msgBox("Reserva modificada ... Creado ALBARAN N. " + numAlb);
        navActivarAll();       
        activaTodo();
        mensajeErr("Reserva Realizada ... ");
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
        if (nCol>=0)
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
            if (isArgumentoAdmin() && Math.abs(Formatear.comparaFechas(rpc_fechaE.getDate(),Formatear.getDateAct()))>7)
            {
                int ret=mensajes.mensajePreguntar("Mantener fecha de mvto?");
                if (ret==mensajes.YES)
                    fecAlta=new Timestamp(rpc_fechaE.getDate().getTime());
            }
            numAlb=traspDato1(0);
            dtAdd.commit();
        } catch (SQLException | ParseException k)
        {
            Error("Error al insertar registro",k);
        }
         
        mensaje("");
        msgBox("RESERVA REALIZADA ... ALBARAN N. " + numAlb);
        navActivarAll();       
        activaTodo();
        mensajeErr("Reserva Realizada ... ");
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
        Ptab1.setSelectedIndex(0);
        s=checkMvtos();
        if (s!=null)
        {
            msgBox(s);
            nav.pulsado=navegador.NINGUNO;
            activaTodo();
            return;
        }   
        cliCodio=cli_codiE.getValorInt();

          

        activar(true,navegador.EDIT);
         rpc_cerraE.setEnabled(isArgumentoAdmin());
        mensaje("Editando registro...");
        jt.requestFocusInicioLater();
    }
     @Override
    public void PADDelete() {
        Ptab1.setSelectedIndex(0);
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
            Error("Error al anular Reserva",k);
        }
        
        navActivarAll();       
        activaTodo();
        mensaje("");
        mensajeErr("Anulada  Reserva ... ");
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
    private void verDatosResumen(int cliCodi)
    {
      try
      {
          jtArt.removeAllDatos();
          s="select p.pro_nomb,a.pro_codi,sum(rpc_canti) as rpc_canti, sum(rpc_numuni) as rpc_numuni"
              + " from v_resprcli as a left join v_articulo as p on a.pro_codi =p.pro_codi "+
              " where cli_codi = "+cliCodi+
//              " and rpc_fecha <= '"+rpc_fechaE.getFechaDB()+"'"+
              " and rpc_cerra= 0 " + // No este cerrado.
              " group by a.pro_codi,p.pro_nomb"+
              " order by pro_codi";
          if (!dtCon1.select(s))
              return;
          do
          {
              ArrayList v=new ArrayList();
              v.add(dtCon1.getInt("pro_codi"));
              v.add(dtCon1.getString("pro_nomb"));
              s="select sum(rpc_canti) as rpc_canti, sum(rpc_numuni) as rpc_numuni "
                  + "from  stockpart as s,v_resprcli as r where stk_block="+cliCodi+
                  " and alm_codi = 1"+
                  " and stp_unact>0 "+
                  " and s.pro_codi ="+dtCon1.getInt("pro_codi")+
                  " and r.cli_codi = "+cliCodi+
                  " and rpc_fecha >= '"+rpc_fechaE.getFechaDB()+"'"+
                  " and r.pro_codi=s.pro_codi "+
                  " and s.eje_nume= r.rpc_ejelot "+
                  " and s.pro_serie= r.rpc_serlot "+
                  " and s.pro_nupar  = r.rpc_numpar "+
                  " and s.pro_numind  = r.rpc_numind";
              dtStat.select(s);
              v.add(dtStat.getInt("rpc_numuni",true));
              v.add(dtStat.getDouble("rpc_canti",true));
              s="select sum(rpc_canti) as rpc_canti, sum(rpc_numuni) as rpc_numuni "
                  + "from  stockpart as s,v_resprcli as r where stk_block="+cliCodi+
                  " and alm_codi = 1"+
                  " and stp_unact=0 "+
                  " and s.pro_codi= "+dtCon1.getInt("pro_codi")+
                  " and r.cli_codi = "+cliCodi+
                  " and rpc_fecha >= '"+rpc_fechaE.getFechaDB()+"'"+
                  " and r.pro_codi=s.pro_codi "+
                   " and s.eje_nume= r.rpc_ejelot "+
                  " and s.pro_serie= r.rpc_serlot "+
                  " and s.pro_nupar  = r.rpc_numpar "+
                  " and s.pro_numind  = r.rpc_numind";
              dtStat.select(s);
              v.add(dtStat.getInt("rpc_numuni",true));
              v.add(dtStat.getDouble("rpc_canti",true));
              v.add(dtCon1.getInt("rpc_numuni"));
              v.add(dtCon1.getDouble("rpc_canti"));
              jtArt.addLinea(v);
          } while (dtCon1.next());
          
      } catch (SQLException | ParseException ex)
      {
         Error("Error al ver datos resumen", ex);
      }
    }
    private void verDatos(DatosTabla dt)
    {
        try {
            if (dt.getNOREG())
                return;
            cliCodio=0;
            rpc_idE.setValorInt(dt.getInt("rpc_id"));
            s="select p.pro_nomb,a.pro_codi,a.rpc_ejelot,a.rpc_serlot,a.rpc_numpar,rpc_numind,rpc_numlin,rpc_cerra,"
                + "rpc_canti,rpc_numuni,a.usu_nomb,cli_codi, rpc_fecha  "
                + " from v_resprcli as a left join v_articulo as p on a.pro_codi =p.pro_codi "+
                " where rpc_id = "+dt.getInt("rpc_id")+
                " order by rpc_numlin ";            
            if (! dtCon1.select(s))
            {
               msgBox("Registro no encontrado. Probablemente se borro");
               jt.removeAllDatos();
               return;
            }
            usu_nombE.setText(dtCon1.getString("usu_nomb"));
            cli_codiE.setValorInt(dtCon1.getInt("cli_codi"));
            rpc_cerraE.setValor(dtCon1.getString("rpc_cerra"));
            rpc_fechaE.setText(dtCon1.getFecha("rpc_fecha","dd-MM-yyyy"));
            jt.removeAllDatos();
            do
            {
                ArrayList v=new ArrayList();
                v.add(dtCon1.getInt("pro_codi")); // 0
                v.add(dtCon1.getString("pro_nomb")); // 1
                v.add(dtCon1.getInt("rpc_ejelot"));
                v.add(dtCon1.getString("rpc_serlot")); // 3
                v.add(dtCon1.getInt("rpc_numpar")); // 4
                v.add(dtCon1.getInt("rpc_numind")); // 5
                v.add(dtCon1.getDouble("rpc_canti")); // 6
                v.add(dtCon1.getInt("rpc_numuni")); // 7
                v.add(dtCon1.getInt("rpc_numlin")); // 8
                jt.addLinea(v);
            } while (dtCon1.next());
            jt.requestFocusLater(0, 0);
            calcAcumulados();
            if (Ptab1.getSelectedIndex()==1)
                verDatosResumen(cli_codiE.getValorInt());
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
        jtAcumArt.removeAllDatos();
        HashMap<Integer,ArrayList> dt=new HashMap();
        for (int n=0;n<nRow;n++)
        {
            if (jt.getValorInt(n,JT_ARTIC)>0 )
            {
                ArrayList v;
                if ((v=dt.get(jt.getValorInt(n,JT_ARTIC)))==null)
                {
                    v=new ArrayList();
                    v.add(jt.getValString(n,JT_NOMBR));
                    v.add(jt.getValorInt(n,JT_UNID));
                    v.add(jt.getValorDec(n,JT_PESO));
                    dt.put(jt.getValorInt(n,JT_ARTIC),v);
                }
                else
                {
                    v.set(1, ((int) v.get(1)) + jt.getValorInt(n,JT_UNID));
                    v.set(2, ((double) v.get(2)) + jt.getValorDec(n,JT_PESO));
                    dt.put(jt.getValorInt(n,JT_ARTIC),v);
                }
                nPiezas+=jt.getValorInt(n,JT_UNID);
                kilos+=jt.getValorDec(n,JT_PESO);
            }
        }
        numIndE.setValorDec(nPiezas);
        kilosE.setValorDec(kilos);
        Iterator<Integer> it= dt.keySet().iterator();
        int proCodi;
        while (it.hasNext())
        {
          proCodi=it.next();
          ArrayList v=dt.get(proCodi);
          ArrayList v1=new ArrayList();
          v1.add(proCodi);
          v1.add(v.get(0));
          v1.add(v.get(1));
          v1.add(v.get(2));
          jtAcumArt.addLinea(v1);
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

        pro_codiE = new gnu.chu.camposdb.proPanel()
        {

            @Override
            protected void despuesLlenaCampos()
            {
                int deoUnid=getUnidIndi();
                rpc_numuniE.setValorInt(deoUnid);
                if (deoUnid<1)
                return;

                jt.procesaAllFoco();

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
                }
            }
        }
        ;
        pro_nocabE = new gnu.chu.controles.CTextField();
        rpc_ejelotE = new gnu.chu.controles.CTextField(Types.DECIMAL, "###9");
        rpc_serlotE = new gnu.chu.controles.CTextField(Types.CHAR, "X",1);
        rpc_numparE = new gnu.chu.controles.CTextField(Types.DECIMAL, "####9");
        rpc_cantiE = new gnu.chu.controles.CTextField(Types.DECIMAL, "---,--9.99");
        rpc_numindE = new gnu.chu.controles.CTextField(Types.DECIMAL, "###9");
        rpc_numuniE = new gnu.chu.controles.CTextField(Types.DECIMAL, "###9");
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
        rpc_numlinE = new gnu.chu.controles.CTextField(Types.DECIMAL, "###9");
        Pinic = new gnu.chu.controles.CPanel();
        Pcabe = new gnu.chu.controles.CPanel();
        cLabel1 = new gnu.chu.controles.CLabel();
        rpc_idE = new gnu.chu.controles.CTextField(Types.DECIMAL,"####9");
        cLabel2 = new gnu.chu.controles.CLabel();
        rpc_fechaE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel3 = new gnu.chu.controles.CLabel();
        BirGrid = new gnu.chu.controles.CButton();
        cLabel5 = new gnu.chu.controles.CLabel();
        usu_nombE = new gnu.chu.controles.CTextField();
        cli_codiE = new gnu.chu.camposdb.cliPanel();
        cLabel4 = new gnu.chu.controles.CLabel();
        rpc_cerraE = new gnu.chu.controles.CComboBox();
        Ptab1 = new javax.swing.JTabbedPane();
        Pcarga = new gnu.chu.controles.CPanel();
        jt = new gnu.chu.controles.CGridEditable(9){
            @Override
            public int cambiaLinea(int row, int col)
            {
                int reCaLin=cambiaLineajt(row);
                return reCaLin;
            }
            public void afterCambiaLinea()
            {
                pro_codiE.setAlmacen(pdalmace.ALMACENPRINCIPAL);
                calcAcumulados();
            }

        }
        ;
        jtAcumArt = new gnu.chu.controles.Cgrid(4);
        Ppie = new gnu.chu.controles.CPanel();
        Baceptar = new gnu.chu.controles.CButton();
        Bcancelar = new gnu.chu.controles.CButton();
        cLabel6 = new gnu.chu.controles.CLabel();
        numIndE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        cLabel7 = new gnu.chu.controles.CLabel();
        kilosE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##,##9.99");
        PCons = new gnu.chu.controles.CPanel();
        jtArt = new gnu.chu.controles.Cgrid(8);
        jtAlb = new gnu.chu.controles.Cgrid(5);

        pro_nocabE.setEnabled(false);

        rpc_serlotE.setText("A");
        rpc_serlotE.setMayusc(true);

        rpc_cantiE.setToolTipText("Pulse <F1> Para coger peso de la bascula");
        rpc_cantiE.setEnabled(false);

        rpc_numuniE.setText("1");
        rpc_numuniE.setEnabled(false);

        IFLote.setTitle("Reserva Individuos");
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
        rpc_serlotE.setMayusc(true);
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

        rpc_numlinE.setText("1");
        rpc_numlinE.setEnabled(false);

        Pinic.setLayout(new java.awt.GridBagLayout());

        Pcabe.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Pcabe.setMaximumSize(new java.awt.Dimension(460, 70));
        Pcabe.setMinimumSize(new java.awt.Dimension(460, 70));
        Pcabe.setPreferredSize(new java.awt.Dimension(460, 70));
        Pcabe.setLayout(null);

        cLabel1.setText("Reserva");
        Pcabe.add(cLabel1);
        cLabel1.setBounds(2, 2, 45, 17);
        Pcabe.add(rpc_idE);
        rpc_idE.setBounds(60, 2, 52, 17);

        cLabel2.setText("Usuario");
        Pcabe.add(cLabel2);
        cLabel2.setBounds(310, 2, 50, 17);
        Pcabe.add(rpc_fechaE);
        rpc_fechaE.setBounds(180, 2, 67, 17);

        cLabel3.setText("Estado");
        Pcabe.add(cLabel3);
        cLabel3.setBounds(0, 40, 60, 18);

        BirGrid.setText("cButton1");
        Pcabe.add(BirGrid);
        BirGrid.setBounds(400, 62, 1, 1);

        cLabel5.setText("Fecha ");
        Pcabe.add(cLabel5);
        cLabel5.setBounds(130, 2, 35, 17);

        usu_nombE.setEnabled(false);
        Pcabe.add(usu_nombE);
        usu_nombE.setBounds(360, 2, 93, 17);

        cli_codiE.setVerCampoReparto(true);
        Pcabe.add(cli_codiE);
        cli_codiE.setBounds(60, 22, 389, 18);

        cLabel4.setText("Cliente");
        Pcabe.add(cLabel4);
        cLabel4.setBounds(2, 22, 60, 18);

        rpc_cerraE.addItem("Abierto","0");
        rpc_cerraE.addItem("Cerrado","1");
        Pcabe.add(rpc_cerraE);
        rpc_cerraE.setBounds(60, 40, 90, 20);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        Pinic.add(Pcabe, gridBagConstraints);

        Pcarga.setLayout(new java.awt.GridBagLayout());

        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jt.setMaximumSize(new java.awt.Dimension(101, 200));
        jt.setMinimumSize(new java.awt.Dimension(101, 200));
        jt.setPreferredSize(new java.awt.Dimension(101, 200));

        ArrayList v=new ArrayList();
        v.add("Artic"); // 0
        v.add("Nombre"); // 1
        v.add("Ejerc"); // 2
        v.add("Serie"); // 3
        v.add("Lote"); // 4
        v.add("Indiv"); // 5
        v.add("Peso"); // 6
        v.add("Unid."); // 7
        v.add("NL"); // 8
        jt.setCabecera(v);
        jt.setAnchoColumna(new int[]    {60,120,40,40,60,30, 50,40,30});
        jt.setAlinearColumna(new int[] {2,0,2,1,2,2, 2,2,2});

        try {
            ArrayList vc1=new ArrayList();
            vc1.add(pro_codiE.getTextField()); // 0
            vc1.add(pro_nocabE); // 1
            vc1.add(rpc_ejelotE); // 2
            vc1.add(rpc_serlotE); // 3
            vc1.add(rpc_numparE); // 4
            vc1.add(rpc_numindE); // 5
            vc1.add(rpc_cantiE); // 6
            vc1.add(rpc_numuniE); // 7
            vc1.add(rpc_numlinE); // 7
            jt.setCampos(vc1);
            jt.setFormatoCampos();
        } catch (Exception k){Error("Error al cargar campos en grid",k);}

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Pcarga.add(jt, gridBagConstraints);

        ArrayList vaa=new ArrayList();
        vaa.add("Articulo");
        vaa.add("Nombre");
        vaa.add("Ud.Reserva");
        vaa.add("Kg.Reserva");
        jtAcumArt.setCabecera(vaa);
        jtAcumArt.setAnchoColumna(new int[]{60,250,30,80});
        jtAcumArt.setAlinearColumna(new int[]{2,0,2,2});
        jtAcumArt.setFormatoColumna(2, "###9");
        jtAcumArt.setFormatoColumna(3, "#,##9.99");
        jtAcumArt.setAjustarGrid(true);
        jtAcumArt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtAcumArt.setMaximumSize(new java.awt.Dimension(400, 101));
        jtAcumArt.setMinimumSize(new java.awt.Dimension(400, 101));
        jtAcumArt.setPreferredSize(new java.awt.Dimension(400, 101));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Pcarga.add(jtAcumArt, gridBagConstraints);

        Ppie.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        Ppie.setMaximumSize(new java.awt.Dimension(210, 65));
        Ppie.setMinimumSize(new java.awt.Dimension(210, 65));
        Ppie.setPreferredSize(new java.awt.Dimension(210, 65));
        Ppie.setLayout(null);

        Baceptar.setText("Aceptar");
        Ppie.add(Baceptar);
        Baceptar.setBounds(10, 40, 90, 24);

        Bcancelar.setText("Cancelar");
        Ppie.add(Bcancelar);
        Bcancelar.setBounds(110, 40, 90, 24);

        cLabel6.setText("Unidades");
        Ppie.add(cLabel6);
        cLabel6.setBounds(10, 2, 60, 17);

        numIndE.setEditable(false);
        Ppie.add(numIndE);
        numIndE.setBounds(70, 2, 40, 17);

        cLabel7.setText("Kilos");
        Ppie.add(cLabel7);
        cLabel7.setBounds(10, 20, 40, 17);

        kilosE.setEditable(false);
        Ppie.add(kilosE);
        kilosE.setBounds(50, 20, 60, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        Pcarga.add(Ppie, gridBagConstraints);

        Ptab1.addTab("Carga", Pcarga);

        PCons.setLayout(new java.awt.GridBagLayout());

        ArrayList va=new ArrayList();
        va.add("Articulo");
        va.add("Nombre");
        va.add("Ud.Reserva");
        va.add("Kg.Reserva");
        va.add("Ud.Entrega");
        va.add("Kg.Entrega");
        va.add("Ud.Origen");
        va.add("Kg.Origen");
        jtArt.setCabecera(va);
        jtArt.setAnchoColumna(new int[]{50,200,50,80,50,80,50,80});
        jtArt.setAlinearColumna(new int[]{2,0,2,2,2,2,2,2});
        jtArt.setFormatoColumna(2, "###9");
        jtArt.setFormatoColumna(3, "#,##9.99");
        jtArt.setFormatoColumna(4, "###9");
        jtArt.setFormatoColumna(5, "#,##9.99");
        jtArt.setFormatoColumna(6, "###9");
        jtArt.setFormatoColumna(7, "#,##9.99");
        jtArt.setAjustarGrid(true);
        jtArt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtArt.setMaximumSize(new java.awt.Dimension(100, 220));
        jtArt.setMinimumSize(new java.awt.Dimension(100, 220));
        jtArt.setPreferredSize(new java.awt.Dimension(100, 220));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        PCons.add(jtArt, gridBagConstraints);

        ArrayList val=new ArrayList();
        val.add("Ejerc");
        val.add("Serie");
        val.add("Numero");
        val.add("Fecha");
        val.add("Kilos");
        jtAlb.setCabecera(val);
        jtAlb.setAjustarGrid(true);
        jtAlb.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtAlb.setMaximumSize(new java.awt.Dimension(100, 120));
        jtAlb.setMinimumSize(new java.awt.Dimension(100, 120));
        jtAlb.setPreferredSize(new java.awt.Dimension(100, 120));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        PCons.add(jtAlb, gridBagConstraints);

        Ptab1.addTab("Consulta", PCons);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Pinic.add(Ptab1, gridBagConstraints);

        getContentPane().add(Pinic, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CButton Baceptar1;
    private gnu.chu.controles.CButton Bcancelar;
    private gnu.chu.controles.CButton Bcancelar1;
    private gnu.chu.controles.CButton BirGrid;
    private gnu.chu.controles.CInternalFrame IFLote;
    private gnu.chu.controles.CPanel PCons;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Pcarga;
    private gnu.chu.controles.CPanel Pinic;
    private gnu.chu.controles.CPanel Ppie;
    private javax.swing.JTabbedPane Ptab1;
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
    private gnu.chu.camposdb.cliPanel cli_codiE;
    private gnu.chu.controles.CTextField deo_ejelot1E;
    private gnu.chu.controles.CTextField deo_serlot1E;
    private gnu.chu.controles.CGridEditable jt;
    private gnu.chu.controles.Cgrid jtAcumArt;
    private gnu.chu.controles.Cgrid jtAlb;
    private gnu.chu.controles.Cgrid jtArt;
    private gnu.chu.controles.CTextField kilosE;
    private gnu.chu.controles.CTextField numIndE;
    private gnu.chu.camposdb.proPanel pro_codi1E;
    private gnu.chu.camposdb.proPanel pro_codiE;
    private gnu.chu.controles.CTextField pro_indfinE;
    private gnu.chu.controles.CTextField pro_indiniE;
    private gnu.chu.controles.CTextField pro_lote1E;
    private gnu.chu.controles.CTextField pro_nocabE;
    private gnu.chu.controles.CTextField rpc_cantiE;
    private gnu.chu.controles.CComboBox rpc_cerraE;
    private gnu.chu.controles.CTextField rpc_ejelotE;
    private gnu.chu.controles.CTextField rpc_fechaE;
    private gnu.chu.controles.CTextField rpc_idE;
    private gnu.chu.controles.CTextField rpc_numindE;
    private gnu.chu.controles.CTextField rpc_numlinE;
    private gnu.chu.controles.CTextField rpc_numparE;
    private gnu.chu.controles.CTextField rpc_numuniE;
    private gnu.chu.controles.CTextField rpc_serlotE;
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
          return null;
/*    
      try 
      {
        java.sql.Date fecMinMvt=getMinFechaMvto(avc_anoE.getValorInt(), EU.em_cod,"X",
                       rpc_idE.getValorInt());
        if (Formatear.comparaFechas(pdalmace.getFechaInventario(alm_codiE.getValorInt(), dtStat) , fecMinMvt)>= 0 )
          return "Almacen "+alm_codiE.getValorInt() +" con Mvtos anteriores a Ult. Fecha Inventario. Imposible Editar/Borrar";
        if (Formatear.comparaFechas(pdalmace.getFechaInventario(cam_codiE.getValorInt(), dtStat) , fecMinMvt)>= 0 )
          return "Almacen "+cam_codiE.getValorInt() +" con Mvtos anteriores a Ult. Fecha Inventario. Imposible Editar/Borrar";
        int nl = jt.getRowCount();
        int ret=mensajes.NO;
        for (int n = 0; n < nl; n++)    
        {
            if (!checkStock(jt.getValorInt(n, JT_ARTIC),
                jt.getValorInt(n, JT_EJERC),EU.em_cod,
                jt.getValString(n, JT_SERIE),jt.getValorInt(n, JT_LOTE),
                jt.getValorInt(n, JT_INDI),cam_codiE.getValorInt()))
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
      }*/
  }
  /**
   * Borrar el traspaso.
   * @return fecha de Alta de albaran
   */
  void borrarTraspaso() throws SQLException
  {               
     s="update stockpart set stk_block=0"
            + " where exists(select pro_codi from linresprcli as v where rpc_id= "+rpc_idE.getValorInt()
         + " and v.pro_codi =  stockpart.pro_codi "
            + "and v.rpc_ejelot=stockpart.eje_nume "
            + "and  v.rpc_serlot=stockpart.pro_serie "
            + "and v.rpc_numpar=stockpart.pro_nupar "
            + "and v.rpc_numind=stockpart.pro_numind)";
      dtAdd.executeUpdate(s);
      dtAdd.executeUpdate("delete from linresprcli where rpc_id = "+rpc_idE.getValorInt());
      dtAdd.executeUpdate("delete from cabresprcli where rpc_id = "+rpc_idE.getValorInt());

  }
  /**
   * Crear el traspaso.
   * @param fecAlta Fecha de alta para mvtos (Null si debe ser la del dia)
   * @param numAlbaran si 0 asignar uno nuevo
   * @return numero albaran generado
   */
  int traspDato1(int numAlb) throws SQLException, ParseException
  {              
      mensaje("Espere, por favor ... traspasando Individuos");     
      
     // Genero la cabecera del Albaran
      dtAdd.addNew("cabresprcli",false);
        if (numAlb!=0)
            dtAdd.setDato("rpc_id", numAlb);
        dtAdd.setDato("rpc_fecha", rpc_fechaE.getText(), "dd-MM-yyyy");
        dtAdd.setDato("usu_nomb", EU.usuario);
        dtAdd.setDato("cli_codi", cli_codiE.getValorInt());
        dtAdd.setDato("rpc_cerra", rpc_cerraE.getValor());
        dtAdd.update();
        if (numAlb==0)
        {
            dtAdd.select("SELECT lastval()");
            numAlb=dtAdd.getInt(1);
            rpc_idE.setValorInt(numAlb);
        }
     
      int nl = jt.getRowCount();
   
      for (int n = 0; n < nl; n++)
      {
        if (jt.getValorInt(n,JT_ARTIC)==0)
          continue;

        // Insertamos linea de albaran
        dtAdd.addNew("linresprcli");     
        
        dtAdd.setDato("rpc_id", numAlb);
        dtAdd.setDato("rpc_numlin", n);
        dtAdd.setDato("rpc_numuni", jt.getValorInt(n, JT_UNID));
        dtAdd.setDato("pro_codi", jt.getValorInt(n, JT_ARTIC));        
        dtAdd.setDato("rpc_ejelot", jt.getValorInt(n, JT_EJERC));        
        dtAdd.setDato("rpc_serlot",  jt.getValString(n, JT_SERIE));
        dtAdd.setDato("rpc_numpar", jt.getValorInt(n, JT_LOTE));
        dtAdd.setDato("rpc_numind", jt.getValorInt(n, JT_INDI));
        dtAdd.setDato("rpc_numuni", jt.getValorInt(n,JT_UNID));
        dtAdd.setDato("rpc_canti", jt.getValorDec(n, JT_PESO));       
        dtAdd.setDato("rpc_fecalt","current_timestamp");
        dtAdd.update(stUp);
        dtAdd.executeUpdate("update stockpart SET stk_block= "+cli_codiE.getValorInt()+
            " where pro_codi =  "+jt.getValorInt(n, JT_ARTIC)
            + "and eje_nume = "+jt.getValorInt(n, JT_EJERC)
            + "and pro_serie ='"+ jt.getValString(n, JT_SERIE)+"'"
            + "and pro_nupar ="+jt.getValorInt(n, JT_LOTE)
            + "and pro_numind="+ jt.getValorInt(n, JT_INDI));  
        }
        return numAlb;
      }
    
  
// 
//  /***
//   * Comprueba si un individuo tiene stock disponible
//   * @param proCodi
//   * @param ejeNume
//   * @param empCodi
//   * @param proSerie
//   * @param proNumlot
//   * @param proNumind
//   * @param almCodi
//   * @return
//   * @throws SQLException 
//   */
//  private boolean checkStock(int proCodi,int ejeNume,int empCodi,String proSerie,int proNumlot,int proNumind,int almCodi) throws SQLException
//  {
//       s = "SELECT * FROM V_STKPART WHERE " +
//          " EJE_NUME= " + ejeNume+
//          " AND EMP_CODI= " + empCodi +
//          " AND PRO_SERIE='" + proSerie + "'" +
//          " AND pro_nupar= " + proNumlot +
//          " and pro_codi= " + proCodi +
//          " and stp_kilact > 0" +
//          " and alm_codi = " + almCodi+
//          " and pro_numind = " + proNumind ;
//      return dtStat.select(s);
//  }
// 
/**
 * Devuelve el numero de resrerva para un individuo
 * @param dt  Conexion base de datos
 * @param proCodi
 * @param ejeLote
 * @param serLote
 * @param numpar
 * @param numind
 * @throws SQLException 
 * @return  Numero reserva. 0 si no esta reservado
 */
  public static int getNumeroReserva(DatosTabla dt,int proCodi,
                        int ejeLote,
                        String serLote,
                        int numpar,
                        int numind) throws SQLException
  {
      String s="select rpc_id from linresprcli where pro_codi = "+proCodi+
          " and rpc_ejelot = "+ejeLote+
          " and rpc_serlot='"+serLote+"'"+
          " and rpc_numpar = "+numpar+
          " and rpc_numind="+numind;
      if (dt.select(s))
          return dt.getInt("rpc_id");
      return 0;
  }
}
