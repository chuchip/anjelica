package gnu.chu.anjelica.almacen;

import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.compras.AlbProv;
import gnu.chu.anjelica.compras.MantAlbCom;
import gnu.chu.anjelica.despiece.utildesp;
import gnu.chu.anjelica.menu;
import gnu.chu.anjelica.ventas.AlbClien;
import gnu.chu.anjelica.ventas.pdalbara;
import gnu.chu.camposdb.cliPanel;
import gnu.chu.controles.StatusBar;
import gnu.chu.interfaces.PAD;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.mensajes;
import gnu.chu.utilidades.navegador;
import gnu.chu.utilidades.ventanaPad;
import gnu.chu.winayu.ayuLote;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

/**
 * Parametros: 
 * estados: Nivel de Estado a los que se pueden cambiar los partes.
 *  '10' Administrador, '1' Generar (Sala). 2 Gerencia , '3' Cerrar (Oficina)
 * Según el estado mandado como parametro podra Modificar/Borrar los partes de estado anterior o igual.
 * @author jpuente.ext
 */

public class MantPartes  extends ventanaPad implements PAD
{
    AlbClien alCli=null;
    AlbProv  alPrv=null;
    
    int proIndLot, proNumLot,proCodi,proEjeLot;
    String proSerLot;
    private boolean swCargaList=true; // Cargando grid de listado
  
    private int P_ESTADOS=1; // Especifica que Estados se permiten establecer
    private final int  JT_PROCODI=0;
    private final int  JT_PRONOMB=1;
    private final int JT_EJERC=2;
    private final int JT_SERIE=3;
    private final int JT_LOTE=4;
    private final int JT_INDI=5;
    private final int JT_UNID=6;
    private final int JT_PESO=7;
    private final int JT_ACSALA=8;
    private final int JT_COMSAL=9;
    private final int JT_ACCION=10;
    private final int JT_COMENT=11; 
    private final int JT_SOLABO=12;
    private final int JT_KILABO=13;
    private final int JT_PREABO=14;
       
    public MantPartes() {
        initComponents();
    }
    public MantPartes(EntornoUsuario eu, Principal p) {
        this(eu,p,null);
    }
    public MantPartes(EntornoUsuario eu, Principal p,Hashtable<String,String> ht) {
        EU = eu;
        vl = p.panel1;
        jf = p;
        eje = true;

        setTitulo("Mantenimiento Partes");
        ponParametros(ht);
        try
        { 
            if (jf.gestor.apuntar(this))
                jbInit();
            else
                setErrorInit(true);
        } catch (Exception e)
        {
            Logger.getLogger(CLProdReci.class.getName()).log(Level.SEVERE, null, e);
            setErrorInit(true);
        }
    }
    
    public MantPartes(menu p, EntornoUsuario eu,Hashtable<String,String> ht) {

        EU = eu;
        vl = p.getLayeredPane();
        setTitulo("Mantenimiento Partes");
        eje = false;


        try
        {
            ponParametros(ht);
                   
            jbInit();
        } catch (Exception e)
        {
            Logger.getLogger(CLProdReci.class.getName()).log(Level.SEVERE, null, e);
            setErrorInit(true);
        }
  }
  private void ponParametros(Hashtable<String,String> ht)
  {
      if (ht==null)
          return;
       if (ht.get("estados") != null)
             P_ESTADOS = Integer.parseInt(ht.get("estados"));
      
  }
  private void jbInit() throws Exception
  {
        nav = new navegador(this, dtCons, false, navegador.NORMAL);
        statusBar = new StatusBar(this);
        this.setVersion("(20150812) Modo: "+P_ESTADOS);
        iniciarFrame();

        this.getContentPane().add(nav, BorderLayout.NORTH);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        this.setPad(this);
        dtCons.setLanzaDBCambio(false);

        initComponents();
        strSql = getStrSql()+             
             getOrderQuery();
            
        conecta();
        
        iniciarBotones(Baceptar, Bcancelar);
      
      this.setSize(new Dimension(700,500));
      
      Pcabe.setDefButton(Baceptar);
      jt.setDefButton(Baceptar);
  }
  
  String getStrSql()
  {
        return "select * from partecab where emp_codi="+EU.em_cod;          
  }
 
  String getOrderQuery()
  {
        return " order by par_codi";
  }
  @Override
  public void iniciarVentana() throws Exception
  {
      opAdmin.setEnabled(P_ESTADOS>1);
      pro_codiE.iniciar(dtStat, this, vl, EU);
      pro_codiE.setCamposLote(pro_ejelotE, pro_serlotE, pro_numlotE, pro_indlotE,pal_kilosE);
      pro_codiE.setAyudaLotes(true);
      prv_codiE.iniciar(dtStat, this, vl, EU);
      prv_codiE.setAceptaNulo(false);
      cli_codiE.iniciar(dtStat, this, vl, EU);
      cli_codiE.setAceptaNulo(false);
      fecfinE.setDate(Formatear.getDateAct());
      feciniE.setDate(Formatear.sumaDiasDate(Formatear.getDateAct(), -60));
      par_codiE.setColumnaAlias("par_codi");
      pac_fecaltE.setColumnaAlias("pac_fecalt");
      pac_usuincE.setColumnaAlias("pac_usuinc");
      pac_usuproE.setColumnaAlias("pac_usupro");
      pac_usuresE.setColumnaAlias("pac_usures");
      pac_fecincE.setColumnaAlias("pac_fecinc");
      pac_fecproE.setColumnaAlias("pac_fecpro");
      pac_fecresE.setColumnaAlias("pac_fecres");
      pac_estadE.setColumnaAlias("pac_estad");
      pac_tipoE.setColumnaAlias("pac_tipo");
      pac_comentE.setColumnaAlias("pac_coment");
      cli_codiE.setColumnaAlias("pac_cliprv");
      prv_codiE.setColumnaAlias("pac_cliprv");
      pac_docanoE.setColumnaAlias("pac_docano");
      pac_docserE.setColumnaAlias("pac_docser");
      pac_docnumE.setColumnaAlias("pac_docnum");
      
      Pcabe.setAltButton(BirGrid);
      
      activaTodo();
      activarEventos();
      verDatos();
      cargaListado(estadoE.getValor());
  }
  private void activarEventos()
  {
      BRefresh.addActionListener(new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (nav.getPulsado()!=navegador.NINGUNO || swCargaList)
                return;
            cargaListado(estadoE.getValor());
        }
      });

      BirGrid.addActionListener(new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
           if (jt.isEnabled())
            jt.requestFocusInicio();
        }
      });
      BListar.addActionListener(new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
           Blistar_ActionPerformed();
        }
      });
      pal_solaboE.addActionListener(new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (pal_solaboE.isSelected() && pal_kilaboE.getValorDec()==0)
            {
                jt.setValor(pal_kilosE.getValorDec(),JT_KILABO);
                pal_kilaboE.setValorDec(pal_kilosE.getValorDec());
            }
        }
      });
      jtList.tableView.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (swCargaList)
                    return;
                if (e.getValueIsAdjusting()) {
                    return;
                }
                try
                {   
                     verDatos(jtList.getValorInt(0));
                } catch (SQLException ex)
                {
                Error("Error al buscar documentos disponibles",ex);
                }
            }
        });
      jtList.addMouseListener(new MouseAdapter()
      {
          @Override
         public void mouseClicked(MouseEvent e) {
             if (e.getClickCount()<2 || jtList.isVacio())
                 return;
            nav.btnEdit.doClick();
         } 
      });
      KeyAdapter kl1=new KeyAdapter()
      {
            @Override
            public void keyPressed(KeyEvent e) 
            {
                if (e.getKeyCode()==KeyEvent.VK_F3)
                    BbuscaDoc.doClick();
            }
      };
    pac_docanoE.addKeyListener(kl1);
    pac_docserE.addKeyListener(kl1);
    pac_docnumE.addKeyListener(kl1);
    BbuscaDoc.addActionListener(new ActionListener(){
     @Override
     public void actionPerformed(ActionEvent e)
     {
        try
        {
            
            if (cli_codiE.isVisible())
            {
                if (!cli_codiE.controlar(cliPanel.LOSTFOCUS))
                {
                    mensajeErr(cli_codiE.getMsgError());
                    return;
                }
                busAlbCli(cli_codiE.getValorInt());
            }
            else
            {
                if (!prv_codiE.controla(false))
                {
                    mensajeErr(prv_codiE.getMsgError());
                    return;
                }
                busAlbPrv(prv_codiE.getValorInt());
            }
        } catch (SQLException ex)
        {
        Error("Error al buscar documentos disponibles",ex);
        }

     }
    });
    estadoE.addActionListener(new ActionListener(){
     @Override
     public void actionPerformed(ActionEvent e)
     {
        if (nav.getPulsado()!=navegador.NINGUNO || swCargaList)
            return;
        cargaListado(estadoE.getValor());
     }
    });
    pac_tipoE.addActionListener(new ActionListener(){
     @Override
     public void actionPerformed(ActionEvent e)
     {
        boolean swProv=pac_tipoE.getValor().equals("E");
        actClientePrv(swProv);       
     }
    });
    BsaltaGrid.addFocusListener(new FocusAdapter()
    {
        @Override
        public void focusGained(FocusEvent e) {
            jt.requestFocusInicio();
        }
    });
  }
  
 
  void   busAlbPrv(int prvCodi)
  {
       try
       {       
         if (alPrv == null)
         {
           alPrv = new AlbProv()
           {
               public void muerto()
               {
                   if (alPrv.getAccNume()>0)
                   {
                       pac_docanoE.setValorInt(alPrv.getAccAno());
                       pac_docserE.setText(alPrv.getAccSerie());
                       pac_docnumE.setValorInt(alPrv.getAccNume());
                   }
               }
           };

          
           alPrv.iniciar(this);
           alPrv.setVerPrecios(false);
           alPrv.setOrdenAlbaranDescendente(true);            
           vl.add(alPrv);
         }
         alPrv.setLocation(this.getLocation().x+100, this.getLocation().y+80);
         alPrv.setSelected(true);
         this.setEnabled(false);
         this.setFoco(alCli);
         alPrv.cargaDatos(Formatear.sumaDiasDate(Formatear.getDateAct(),-90),
             Formatear.getDateAct(),prvCodi);
         alPrv.setVisible(true);
       }
       catch (Exception ex)
       {
         fatalError("Error al Cargar datos de Proveedor",ex);
       }
  }
   void busAlbCli(int cliCodi)
   {
       try
       {       
         if (alCli == null)
         {
           alCli = new AlbClien()
           {
               public void muerto()
               {
                   if (alCli.getAvcNume()>0)
                   {
                       pac_docanoE.setValorInt(alCli.getAvcAno());
                       pac_docserE.setText(alCli.getAvcSerie());
                       pac_docnumE.setValorInt(alCli.getAvcNume());
                   }
               }
           };

          
           alCli.iniciar(this);
           alCli.setVerPrecios(false);
           alCli.setOrdenAlbaranDescendente(true);            
           vl.add(alCli);
         }
         alCli.setLocation(this.getLocation().x+100, this.getLocation().y+80);
         alCli.setSelected(true);
         this.setEnabled(false);
         this.setFoco(alCli);
         alCli.cargaDatos(Formatear.sumaDiasDate(Formatear.getDateAct(),-90),
             Formatear.getDateAct(),cliCodi);
         alCli.setVisible(true);
       }
       catch (Exception ex)
       {
         fatalError("Error al Cargar datos de Clientes",ex);
       }
  }
  /**
   * Actualiza la visibilidad de los campos Cliente/Prov.
   * @param swProv true, indica que se debe ver el campo proveedor
   */
  private void actClientePrv(boolean swProv)
  {
    cli_codiE.setVisible(!swProv);
    prv_codiE.setVisible(swProv);
    pac_cliprvL.setText(swProv?"Proveedor":"Cliente");
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
     StkPartid buscaPeso() throws Exception {
       
        StkPartid stkPartid = utildesp.buscaPeso(dtCon1, pro_ejelotE.getValorInt(), EU.em_cod,
                    pro_serlotE.getText(), pro_numlotE.getValorInt(),
                    pro_indlotE.getValorInt(), pro_codiE.getValorInt(),pdalmace.ALMACENPRINCIPAL);
     
        if (! stkPartid.hasError())
            return stkPartid;
        
//        if (stkPartid.isLockIndiv())
//            return stkPartid;
        mensajeErr(stkPartid.getMensaje());
        return stkPartid;
    }
     
  @Override
  public void PADAddNew(){  
//   if ( P_ESTADOS>.equals("G"))
//   {
//       msgBox("No tiene permisos para crear nuevos partes");
//       nav.pulsado=navegador.NINGUNO;
//       activaTodo();
//       return;
//   }
   PTabPane1.setSelectedIndex(0);           
   resetTexto();
   
   jt.removeAllDatos();
   pac_estadE.setValor("G");
   activar(navegador.ADDNEW,true);
   mensaje("Insertando NUEVO Registro");
   pac_fecincE.setDate(Formatear.getDateAct());
   pac_usuincE.setText(EU.usuario);
   pac_docanoE.setValorInt(EU.ejercicio);
   pac_docserE.setText("A");
   pac_estadE.setValor("G");
   pac_estadE.setEnabled(false);
   pac_fecincE.requestFocus();
   activaCamposGrid();
 }
 @Override
 public void PADDelete()
 {
       try
        {
            PTabPane1.setSelectedIndex(0); 
            if (pac_usuincE.isNull())
            {
                mensajeErr("Parte no encontrado");
                nav.setPulsado(navegador.NINGUNO);
                activaTodo();
                return;
            }
            
            if ( pac_estadE.getValorInt()>P_ESTADOS)
            {
                msgBox(("Parte no puede ser Borrado con sus permisos"));
                nav.setPulsado(navegador.NINGUNO);
                return;
            }
            if (! setBloqueo(dtAdd,"v_partes", par_codiE.getText()))
            {
                msgBox(msgBloqueo);
                activaTodo();
                return;
            }
            activar(navegador.DELETE, true);            
            mensaje("Borrando parte");
            jt.requestFocusInicio();
        } catch (SQLException | UnknownHostException ex)
        {
            Error("Error al editar registro",ex);
        } 
 }
  @Override
 public void PADQuery()
 {
     PTabPane1.setSelectedIndex(0);     
     activar(navegador.QUERY,true);
     mensaje("Introduzca criterios de busqueda");
     Pcabe.setQuery(true);
    
     resetTexto();
     
     par_codiE.requestFocus();
 }
 @Override
 public void PADEdit()
 {
        try
        {
            PTabPane1.setSelectedIndex(0); 
            if (pac_usuincE.isNull())
            {
                mensajeErr("Parte no encontrado");
                nav.setPulsado(navegador.NINGUNO);
                activaTodo();
                return;
            }
            if ( pac_estadE.getValorInt()>P_ESTADOS)
            {
                msgBox(("Parte no puede ser modficado con sus permisos"));
                nav.setPulsado(navegador.NINGUNO);
                return;
            }
            if (! setBloqueo(dtAdd,"v_partes", par_codiE.getText()))
            {
                msgBox(msgBloqueo);
                activaTodo();
                return;
            }
         
            activar(navegador.EDIT, true);
            if (P_ESTADOS>1 && !opAdmin.isSelected())
            {
                if (pac_estadE.getValorInt()<3)
                    pac_estadE.setValor(pac_estadE.getValorInt()+1);
            }
            activaCamposGrid(); 
            mensaje("Editando parte");
            jt.requestFocusInicio();
            resetCambioLote();
        } catch (SQLException | UnknownHostException ex)
        {
            Error("Error al editar registro",ex);
        } 
 }

 private void calcAcumulados()
 {
    resetCambioLote();
 }
    void afterCambiaCol()
    {
        if (pro_indlotE.getValorInt() == 0 && pro_numlotE.getValorInt() == 0)
            return;
         
       
        if (pro_indlotE.getValorInt()==proIndLot && pro_numlotE.getValorInt()==proNumLot
              && pro_codiE.getValorInt()==proCodi && pro_serlotE.getText().equals(proSerLot)
              && pro_ejelotE.getValorInt()==proEjeLot)
            return; // Sin cambios en el producto/Lote
        resetCambioLote();
        
        try
        {
            StkPartid stkPartid = utildesp.buscaPeso(dtCon1, pro_ejelotE.getValorInt(), EU.em_cod,
                pro_serlotE.getText(), pro_numlotE.getValorInt(),
                pro_indlotE.getValorInt(), pro_codiE.getValorInt(), pdalmace.ALMACENPRINCIPAL);
            if (!stkPartid.hasError())
            {
                pal_kilosE.setValorDec(stkPartid.getKilos());
                pal_unidadE.setValorDec(stkPartid.getUnidades());
                jt.setValor(stkPartid.getKilos(),JT_PESO);
                jt.setValor(stkPartid.getUnidades(),JT_UNID);
            }
        } catch (SQLException ex)
        {
            Error("Error al buscar peso de individuo", ex);
        }
    

    }
 int cambiaLineajt(int linea) 
 {
        try
        {    
            if (! jt.hasCambio())
                return -1;
//            if (  !pro_indlotE.hasCambio() && !pro_numlotE.hasCambio()
//              && !pro_codiE.hasCambio() && !pro_serlotE.hasCambio()
//              && !pro_ejelotE.hasCambio() && !pal_kilosE.hasCambio() && !pal_unidadE.hasCambio())
//                return -1; // No hubo cambios
            
            if (pro_codiE.isNull() )
                return -1; // SI no hay producto o no esta puesto para insertar lo doy como bueno.
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
            if (pro_indlotE.getValorInt()!=0 && pro_numlotE.getValorInt()!=0)
            {                 
                StkPartid stkPartid = utildesp.buscaPeso(dtCon1, pro_ejelotE.getValorInt(), EU.em_cod,
                    pro_serlotE.getText(), pro_numlotE.getValorInt(),
                    pro_indlotE.getValorInt(), pro_codiE.getValorInt(),pdalmace.ALMACENPRINCIPAL);
                if ( stkPartid.hasError() )
                {
                    mensajeErr(stkPartid.getMensaje());
                    return JT_EJERC;    
                }
              
            }
            if (pal_kilosE.getValorDec()==0)
            {
                mensajeErr("Intoduzca kilos de producto");
                return JT_PESO;
            }
             if (pal_unidadE.getValorDec()==0)
            {
                mensajeErr("Intoduzca Unidades de producto");
                return JT_UNID;
            }
            if (!pal_accionE.getValor().equals("-") && pal_accionE.isEnabled())
            { // Tomada una accion.
              if (pal_solaboE.isSelected())
              {// Solicitado abono
                if (pal_kilaboE.getValorDec()==0)
                {
                    mensajeErr("Introduzca kilos del abono");
                    return JT_KILABO;
                }
              }
            }
        } catch (SQLException k)
        {
            Error("Error al comprobar integridad linea",k);
        }
        jt.resetCambio();
        resetCambioLote();
        return -1;
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
                int deoUnid=getUnidIndi();
                pal_unidadE.setValorInt(deoUnid);
                if (deoUnid<1)
                return;
                jt.procesaAllFoco();
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
                    jt.resetCambio();
                }
            }
        };
        pro_nombE = new gnu.chu.controles.CTextField();
        pal_unidadE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        pal_kilosE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9.99");
        pro_ejelotE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        pro_serlotE = new gnu.chu.controles.CTextField(Types.CHAR,"X",1);
        pro_numlotE = new gnu.chu.controles.CTextField(Types.DECIMAL,"####9");
        pro_indlotE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        pal_comentE = new gnu.chu.controles.CTextField(Types.CHAR,"X",50);
        pal_kilaboE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9.99");
        pal_preaboE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9.99");
        pal_solaboC = new gnu.chu.controles.CCheckBox();
        pal_acsalaE = new gnu.chu.controles.CComboBox();
        pal_accionE = new gnu.chu.controles.CComboBox();
        pal_comsalE = new gnu.chu.controles.CTextField(Types.CHAR,"X",50);
        pal_solaboE = new gnu.chu.controles.CCheckBox();
        pal_uniaboE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        Pprinc = new gnu.chu.controles.CPanel();
        PTabPane1 = new gnu.chu.controles.CTabbedPane();
        Pcabe = new gnu.chu.controles.CPanel();
        par_codiL = new gnu.chu.controles.CLabel();
        par_codiE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#####9");
        pac_fecaltL = new gnu.chu.controles.CLabel();
        par_comentL = new gnu.chu.controles.CLabel();
        pac_tipoL = new gnu.chu.controles.CLabel();
        pac_tipoE = new gnu.chu.controles.CComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        pac_comentE = new gnu.chu.controles.CTextArea();
        pac_cliprvL = new gnu.chu.controles.CLabel();
        cli_codiE = new gnu.chu.camposdb.cliPanel();
        prv_codiE = new gnu.chu.camposdb.prvPanel();
        cLabel2 = new gnu.chu.controles.CLabel();
        pac_fecincE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yy");
        pac_docanoE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        pac_docserE = new gnu.chu.controles.CTextField(Types.CHAR,"X");
        pac_docnumE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#####9");
        BsaltaGrid = new gnu.chu.controles.CButton();
        BbuscaDoc = new gnu.chu.controles.CButton(Iconos.getImageIcon("buscar"));
        Pusuarios = new gnu.chu.controles.CPanel();
        par_codiL3 = new gnu.chu.controles.CLabel();
        pac_usuincE = new gnu.chu.controles.CTextField(Types.CHAR,"X",15);
        par_codiL2 = new gnu.chu.controles.CLabel();
        pac_usuproE = new gnu.chu.controles.CTextField(Types.CHAR,"X",15);
        par_codiL1 = new gnu.chu.controles.CLabel();
        pac_usuresE = new gnu.chu.controles.CTextField(Types.CHAR,"X",15);
        Pfechas = new gnu.chu.controles.CPanel();
        pac_fecaltL1 = new gnu.chu.controles.CLabel();
        pac_fecaltE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yy");
        par_fecaltL3 = new gnu.chu.controles.CLabel();
        pac_fecproL = new gnu.chu.controles.CLabel();
        pac_fecproE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yy");
        pac_fecresE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yy");
        pac_estadL = new gnu.chu.controles.CLabel();
        pac_estadE = new gnu.chu.controles.CComboBox();
        BirGrid = new gnu.chu.controles.CButton();
        Plistado = new gnu.chu.controles.CPanel();
        jtList = new gnu.chu.controles.Cgrid(6);
        pac_estadL1 = new gnu.chu.controles.CLabel();
        estadoE = new gnu.chu.controles.CComboBox();
        pac_estadL2 = new gnu.chu.controles.CLabel();
        feciniE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        pac_estadL3 = new gnu.chu.controles.CLabel();
        fecfinE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        BListar = new gnu.chu.controles.CButton(Iconos.getImageIcon("print"));
        BRefresh = new gnu.chu.controles.CButton(Iconos.getImageIcon("reload"));
        jt = new gnu.chu.controles.CGridEditable(15)
        {
            @Override
            public int cambiaLinea(int row, int col)
            {
                int reCaLin=cambiaLineajt(row);
                return reCaLin;
            }
            public void afterCambiaLinea()
            {
                calcAcumulados();
            }
            protected void afterCambiaColumna(int col,int colNueva,int row)
            {
                if (col==JT_INDI)
                afterCambiaCol();
            }
        };
        Ppie = new gnu.chu.controles.CPanel();
        Baceptar = new gnu.chu.controles.CButton();
        Bcancelar = new gnu.chu.controles.CButton();
        opAdmin = new gnu.chu.controles.CCheckBox();

        pro_codiE.setProNomb(pro_nombE);
        pro_nombE.setEnabled(false);

        pal_unidadE.setText("1");
        pal_unidadE.setToolTipText("");

        pro_ejelotE.setValorDec(EU.ejercicio);

        pro_serlotE.setText("A");
        pro_serlotE.setMayusc(true);

        pal_solaboC.setText("cCheckBox1");

        pal_acsalaE.addItem("Vert.","V");
        pal_acsalaE.addItem("Reut.","R");
        pal_acsalaE.addItem("Cong.","C");
        pal_acsalaE.setGridEditable(jt);

        pal_accionE.addItem("NO DEF.","-");
        pal_accionE.addItem("Vert.","V");
        pal_accionE.addItem("Reut.","R");
        pal_accionE.addItem("Cong.","C");
        pal_accionE.setGridEditable(jt);

        pal_uniaboE.setText("1");
        pal_uniaboE.setToolTipText("");

        Pprinc.setLayout(new java.awt.GridBagLayout());

        PTabPane1.setMaximumSize(new java.awt.Dimension(700, 210));
        PTabPane1.setMinimumSize(new java.awt.Dimension(700, 210));
        PTabPane1.setPreferredSize(new java.awt.Dimension(700, 210));

        Pcabe.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Pcabe.setLayout(null);

        par_codiL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        par_codiL.setText("Parte");
        Pcabe.add(par_codiL);
        par_codiL.setBounds(20, 4, 50, 17);
        Pcabe.add(par_codiE);
        par_codiE.setBounds(80, 4, 50, 17);

        pac_fecaltL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        pac_fecaltL.setText("Fecha");
        Pcabe.add(pac_fecaltL);
        pac_fecaltL.setBounds(10, 44, 60, 17);

        par_comentL.setText("Comentario");
        Pcabe.add(par_comentL);
        par_comentL.setBounds(170, 0, 80, 17);

        pac_tipoL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        pac_tipoL.setText("Tipo");
        Pcabe.add(pac_tipoL);
        pac_tipoL.setBounds(20, 25, 50, 17);

        pac_tipoE.addItem("Sala", "S");
        pac_tipoE.addItem("Devolucion", "D");
        pac_tipoE.addItem("No-Recepcion", "N");
        pac_tipoE.addItem("Entrada", "E");
        Pcabe.add(pac_tipoE);
        pac_tipoE.setBounds(80, 25, 110, 17);

        pac_comentE.setColumns(20);
        pac_comentE.setRows(5);
        jScrollPane1.setViewportView(pac_comentE);

        Pcabe.add(jScrollPane1);
        jScrollPane1.setBounds(210, 20, 450, 50);

        pac_cliprvL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        pac_cliprvL.setText("Proveedor");
        Pcabe.add(pac_cliprvL);
        pac_cliprvL.setBounds(0, 92, 70, 17);
        Pcabe.add(cli_codiE);
        cli_codiE.setBounds(80, 92, 370, 18);
        Pcabe.add(prv_codiE);
        prv_codiE.setBounds(80, 92, 370, 18);

        cLabel2.setText("Documento");
        Pcabe.add(cLabel2);
        cLabel2.setBounds(460, 92, 70, 17);
        Pcabe.add(pac_fecincE);
        pac_fecincE.setBounds(80, 44, 70, 17);
        Pcabe.add(pac_docanoE);
        pac_docanoE.setBounds(530, 92, 40, 17);

        pac_docserE.setAutoNext(true);
        pac_docserE.setMayusc(true);
        Pcabe.add(pac_docserE);
        pac_docserE.setBounds(570, 92, 20, 17);
        Pcabe.add(pac_docnumE);
        pac_docnumE.setBounds(590, 92, 60, 17);

        BsaltaGrid.setText("cButton1");
        Pcabe.add(BsaltaGrid);
        BsaltaGrid.setBounds(699, 89, 2, 2);

        BbuscaDoc.setToolTipText("Buscar documentos (F3)");
        BbuscaDoc.setFocusable(false);
        Pcabe.add(BbuscaDoc);
        BbuscaDoc.setBounds(650, 90, 22, 22);

        Pusuarios.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Usuarios")));
        Pusuarios.setEnabled(false);
        Pusuarios.setLayout(null);

        par_codiL3.setText("Generado");
        Pusuarios.add(par_codiL3);
        par_codiL3.setBounds(10, 20, 70, 17);

        pac_usuincE.setMinusc(true);
        Pusuarios.add(pac_usuincE);
        pac_usuincE.setBounds(80, 20, 100, 17);

        par_codiL2.setText("Procesado");
        Pusuarios.add(par_codiL2);
        par_codiL2.setBounds(10, 40, 70, 17);

        pac_usuproE.setMinusc(true);
        Pusuarios.add(pac_usuproE);
        pac_usuproE.setBounds(80, 40, 100, 17);

        par_codiL1.setText("Resuelto");
        Pusuarios.add(par_codiL1);
        par_codiL1.setBounds(190, 20, 60, 17);

        pac_usuresE.setMinusc(true);
        Pusuarios.add(pac_usuresE);
        pac_usuresE.setBounds(250, 20, 100, 17);

        Pcabe.add(Pusuarios);
        Pusuarios.setBounds(10, 110, 360, 70);

        Pfechas.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED), "Fechas"));
        Pfechas.setEnabled(false);
        Pfechas.setLayout(null);

        pac_fecaltL1.setText("Resuelto");
        Pfechas.add(pac_fecaltL1);
        pac_fecaltL1.setBounds(150, 20, 60, 15);
        Pfechas.add(pac_fecaltE);
        pac_fecaltE.setBounds(75, 20, 60, 17);

        par_fecaltL3.setText("Alta");
        Pfechas.add(par_fecaltL3);
        par_fecaltL3.setBounds(10, 20, 60, 15);

        pac_fecproL.setText("Procesado");
        Pfechas.add(pac_fecproL);
        pac_fecproL.setBounds(10, 40, 60, 15);
        Pfechas.add(pac_fecproE);
        pac_fecproE.setBounds(75, 40, 60, 17);
        Pfechas.add(pac_fecresE);
        pac_fecresE.setBounds(210, 20, 60, 17);

        Pcabe.add(Pfechas);
        Pfechas.setBounds(390, 110, 280, 70);

        pac_estadL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        pac_estadL.setText("Estado");
        Pcabe.add(pac_estadL);
        pac_estadL.setBounds(10, 65, 60, 17);

        pac_estadE.addItem("Generada", "1");
        pac_estadE.addItem("Procesada", "2");
        pac_estadE.addItem("Resuelta", "3");
        Pcabe.add(pac_estadE);
        pac_estadE.setBounds(80, 65, 120, 17);

        BirGrid.setFocusable(false);
        Pcabe.add(BirGrid);
        BirGrid.setBounds(680, 99, 2, 2);

        PTabPane1.addTab("Datos", Pcabe);

        Plistado.setAltButton(BRefresh);
        Plistado.setLayout(null);

        jtList.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        ArrayList v1=new ArrayList();
        v1.add("Parte"); // 0
        v1.add("Fecha"); // 1
        v1.add("Tipo"); // 2
        v1.add("Cl/Prv"); // 3
        v1.add("Nombre"); // 4
        v1.add("Coment"); // 5
        jtList.setCabecera(v1);
        jtList.setAnchoColumna(new int[]{40,60,40,40,150,250});
        jtList.setAlinearColumna(new int[]{2,1,0,2,0,0});
        jtList.setFormatoColumna(1,"dd-mm-yy");
        jtList.setAjustarGrid(true);

        Plistado.add(jtList);
        jtList.setBounds(0, 22, 690, 160);

        pac_estadL1.setText("De fecha:");
        Plistado.add(pac_estadL1);
        pac_estadL1.setBounds(270, 1, 60, 17);

        estadoE.addItem("Generada", "1");
        if (P_ESTADOS>= 2)
        estadoE.addItem("Procesada", "2");
        if (P_ESTADOS>= 3)
        estadoE.addItem("Resuelta", "3");
        Plistado.add(estadoE);
        estadoE.setBounds(150, 1, 110, 17);

        pac_estadL2.setText("Ver partes con estado");
        Plistado.add(pac_estadL2);
        pac_estadL2.setBounds(10, 1, 130, 17);
        Plistado.add(feciniE);
        feciniE.setBounds(330, 1, 80, 17);

        pac_estadL3.setText("A fecha:");
        Plistado.add(pac_estadL3);
        pac_estadL3.setBounds(420, 1, 60, 17);
        Plistado.add(fecfinE);
        fecfinE.setBounds(480, 1, 80, 17);

        BListar.setText("Listar");
        Plistado.add(BListar);
        BListar.setBounds(610, 0, 80, 19);

        BRefresh.setToolTipText("Refrescar Consulta");
        Plistado.add(BRefresh);
        BRefresh.setBounds(570, 1, 20, 20);

        PTabPane1.addTab("Listado", Plistado);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        Pprinc.add(PTabPane1, gridBagConstraints);

        ArrayList vc=new ArrayList();
        vc.add("Articulo"); //0
        vc.add("Nombre"); // 1
        vc.add("Ejerc."); // 2
        vc.add("Serie"); // 3
        vc.add("Lote"); // 4
        vc.add("Ind."); // 5
        vc.add("Unid"); // 6
        vc.add("Kilos"); // 7
        vc.add("Sug."); // 8
        vc.add("Com.Sala"); // 9
        vc.add("Accion"); // 10
        vc.add("Comentario"); // 11
        vc.add("Abona"); // 12
        vc.add("Kil.Abo"); // 13
        vc.add("Pre.Abo"); // 14
        jt.setCabecera(vc);
        ArrayList v=new ArrayList();
        v.add(pro_codiE.getTextField()); // 0
        v.add(pro_nombE);  // 1
        v.add(pro_ejelotE); // 2
        v.add(pro_serlotE); // 3
        v.add(pro_numlotE); // 4
        v.add(pro_indlotE); // 5
        v.add(pal_unidadE); // 6
        v.add(pal_kilosE); // 7
        v.add(pal_acsalaE); // 8
        v.add(pal_comsalE); // 9
        v.add(pal_accionE); // 10
        v.add(pal_comentE); // 11
        v.add(pal_solaboE); // 12
        v.add(pal_kilaboE); // 13
        v.add(pal_preaboE); // 14
        try {
            jt.setCampos(v);
        } catch (Exception k)
        {
            Error("Error al iniciar campos del grid",k);
            return;
        }
        jt.setAlinearColumna(new int[]{2,0,2,1,2,2,2,2,0,0,0,0,1,2,2});
        jt.setAnchoColumna(new int[]{70,130,50,30,50,40,50,60,60,150,60,130,40,50,50});
        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jt.setMaximumSize(new java.awt.Dimension(700, 101));
        jt.setMinimumSize(new java.awt.Dimension(700, 101));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        Pprinc.add(jt, gridBagConstraints);

        Ppie.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Ppie.setMaximumSize(new java.awt.Dimension(440, 41));
        Ppie.setMinimumSize(new java.awt.Dimension(440, 41));
        Ppie.setPreferredSize(new java.awt.Dimension(440, 41));
        Ppie.setLayout(null);

        Baceptar.setText("Aceptar");
        Ppie.add(Baceptar);
        Baceptar.setBounds(10, 5, 90, 30);

        Bcancelar.setText("Cancelar");
        Ppie.add(Bcancelar);
        Bcancelar.setBounds(120, 5, 90, 30);

        opAdmin.setText("Modo Admin");
        opAdmin.setInheritsPopupMenu(true);
        opAdmin.setMargin(new java.awt.Insets(0, 0, 0, 0));
        Ppie.add(opAdmin);
        opAdmin.setBounds(333, 2, 100, 19);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        Pprinc.add(Ppie, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton BListar;
    private gnu.chu.controles.CButton BRefresh;
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CButton BbuscaDoc;
    private gnu.chu.controles.CButton Bcancelar;
    private gnu.chu.controles.CButton BirGrid;
    private gnu.chu.controles.CButton BsaltaGrid;
    private gnu.chu.controles.CTabbedPane PTabPane1;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Pfechas;
    private gnu.chu.controles.CPanel Plistado;
    private gnu.chu.controles.CPanel Ppie;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CPanel Pusuarios;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.camposdb.cliPanel cli_codiE;
    private gnu.chu.controles.CComboBox estadoE;
    private gnu.chu.controles.CTextField fecfinE;
    private gnu.chu.controles.CTextField feciniE;
    private javax.swing.JScrollPane jScrollPane1;
    private gnu.chu.controles.CGridEditable jt;
    private gnu.chu.controles.Cgrid jtList;
    private gnu.chu.controles.CCheckBox opAdmin;
    private gnu.chu.controles.CLabel pac_cliprvL;
    private gnu.chu.controles.CTextArea pac_comentE;
    private gnu.chu.controles.CTextField pac_docanoE;
    private gnu.chu.controles.CTextField pac_docnumE;
    private gnu.chu.controles.CTextField pac_docserE;
    private gnu.chu.controles.CComboBox pac_estadE;
    private gnu.chu.controles.CLabel pac_estadL;
    private gnu.chu.controles.CLabel pac_estadL1;
    private gnu.chu.controles.CLabel pac_estadL2;
    private gnu.chu.controles.CLabel pac_estadL3;
    private gnu.chu.controles.CTextField pac_fecaltE;
    private gnu.chu.controles.CLabel pac_fecaltL;
    private gnu.chu.controles.CLabel pac_fecaltL1;
    private gnu.chu.controles.CTextField pac_fecincE;
    private gnu.chu.controles.CTextField pac_fecproE;
    private gnu.chu.controles.CLabel pac_fecproL;
    private gnu.chu.controles.CTextField pac_fecresE;
    private gnu.chu.controles.CComboBox pac_tipoE;
    private gnu.chu.controles.CLabel pac_tipoL;
    private gnu.chu.controles.CTextField pac_usuincE;
    private gnu.chu.controles.CTextField pac_usuproE;
    private gnu.chu.controles.CTextField pac_usuresE;
    private gnu.chu.controles.CComboBox pal_accionE;
    private gnu.chu.controles.CComboBox pal_acsalaE;
    private gnu.chu.controles.CTextField pal_comentE;
    private gnu.chu.controles.CTextField pal_comsalE;
    private gnu.chu.controles.CTextField pal_kilaboE;
    private gnu.chu.controles.CTextField pal_kilosE;
    private gnu.chu.controles.CTextField pal_preaboE;
    private gnu.chu.controles.CCheckBox pal_solaboC;
    private gnu.chu.controles.CCheckBox pal_solaboE;
    private gnu.chu.controles.CTextField pal_uniaboE;
    private gnu.chu.controles.CTextField pal_unidadE;
    private gnu.chu.controles.CTextField par_codiE;
    private gnu.chu.controles.CLabel par_codiL;
    private gnu.chu.controles.CLabel par_codiL1;
    private gnu.chu.controles.CLabel par_codiL2;
    private gnu.chu.controles.CLabel par_codiL3;
    private gnu.chu.controles.CLabel par_comentL;
    private gnu.chu.controles.CLabel par_fecaltL3;
    private gnu.chu.camposdb.proPanel pro_codiE;
    private gnu.chu.controles.CTextField pro_ejelotE;
    private gnu.chu.controles.CTextField pro_indlotE;
    private gnu.chu.controles.CTextField pro_nombE;
    private gnu.chu.controles.CTextField pro_numlotE;
    private gnu.chu.controles.CTextField pro_serlotE;
    private gnu.chu.camposdb.prvPanel prv_codiE;
    // End of variables declaration//GEN-END:variables

    @Override
    public void PADPrimero() {
       verDatos();
       nav.setPulsado(navegador.NINGUNO);
    }

    @Override
    public void PADAnterior() {
        verDatos();
        nav.setPulsado(navegador.NINGUNO);
    }

    @Override
    public void PADSiguiente() {
        verDatos();
        nav.setPulsado(navegador.NINGUNO);
    }

    @Override
    public void PADUltimo() {
        verDatos();
        nav.setPulsado(navegador.NINGUNO);
    }

    @Override
    public void ej_query1() {
        Component c = Pcabe.getErrorConf();
        if (c != null)
        {
            PTabPane1.setSelectedIndex(0);
            c.requestFocus();
            mensajeErr("Error en condiciones Busqueda");
            return;
        }
      
        ArrayList v = new ArrayList();
        v.add(par_codiE.getStrQuery());
        v.add(pac_fecaltE.getStrQuery());
        v.add(pac_usuincE.getStrQuery());
        v.add(pac_usuproE.getStrQuery());
        v.add(pac_usuresE.getStrQuery());
        v.add(pac_fecincE.getStrQuery());
        v.add(pac_fecproE.getStrQuery());
        v.add(pac_fecresE.getStrQuery());
        v.add(pac_estadE.getStrQuery());
        v.add(pac_tipoE.getStrQuery());
        v.add(pac_comentE.getStrQuery());
        v.add(cli_codiE.getStrQuery());
        v.add(prv_codiE.getStrQuery());
        v.add(pac_docanoE.getStrQuery());
        v.add(pac_docserE.getStrQuery());
        v.add(pac_docnumE.getStrQuery());
        try
        {
            Pcabe.setQuery(false);
            String s = "SELECT * FROM partecab "; //WHERE emp_codi = " + EU.em_cod ;
            s = creaWhere(s, v, true);
            
            s += " ORDER BY par_codi ";
//      this.setEnabled(false);
            mensaje("Espere, por favor ... buscando datos");
//      debug(s);
            if (!dtCon1.select(s))
            {
                msgBox("No encontrados Partes con estos criterios");
                rgSelect();
                activaTodo();
                nav.pulsado = navegador.NINGUNO;
                this.setEnabled(true);
                return;
            }
            strSql = s;
            rgSelect();
            this.setEnabled(true);
            mensaje("");
            mensajeErr("Nuevas Condiciones ... Establecidas");
        } catch (Exception k)
        {
            Error("Error al buscar datos", k);
        }
        activaTodo();
        nav.pulsado = navegador.NINGUNO;
        verDatos();
    }

    @Override
    public void canc_query() {
        Pcabe.setQuery(false);
        mensaje("");
        mensajeErr("Cancelada busqueda de partes");
        activaTodo();
        nav.setPulsado(navegador.NINGUNO);
        verDatos();  
    }

    @Override
    public void ej_edit1() {
        try
        {
            if (! checkEntrada())
                return;
            if (pac_estadE.getValorInt()>1)
            { 
                int nRows=jt.getRowCount();
                for (int n=0;n<nRows;n++)
                {
                    if (jt.getValorInt(n,JT_PROCODI)==0)
                        continue;  
                    if ( jt.getValString(n,JT_ACCION).startsWith("NO") )
                    {
                        mensajeErr("Accion de linea: "+n+" no definida");
                        return;
                    }
                }
            }
            resetBloqueo(dtAdd, "v_partes",par_codiE.getText());
            dtAdd.select("select * from partecab where par_codi="+par_codiE.getValorInt(),true);
            dtAdd.edit();
            actualCab();
            deleteParteLin();
            insertaLin(par_codiE.getValorInt());
            dtAdd.commit();
            mensaje("");
            activaTodo();
            mensajeErr("Parte modificado correctamente");
        } catch (ParseException | SQLException  ex)
        {
          Error("Error al editar parte",ex);
        } 
    }

    @Override
    public void canc_edit() {
    
        try {
          resetBloqueo(dtAdd, "v_partes",par_codiE.getText());
        } catch (Exception k)
        {
          mensajes.mensajeAviso("Error al Quitar el bloqueo\n"+k.getMessage());
        }
        mensaje("");
        mensajeErr("Cancelada edicion de parte");
        activaTodo();
        nav.setPulsado(navegador.NINGUNO);
        verDatos(); 
    }

    @Override
    public void ej_addnew1() {
      
        try
        {
            if (! checkEntrada())
                return;
            int id=insertaCab();
            insertaLin(id);
            dtAdd.commit();
            msgBox("Parte insertado con identificador: "+id);
            nav.pulsado=navegador.NINGUNO;
            activaTodo();            
        } catch (ParseException | SQLException ex)
        {
            Error("Error al crear parte",ex);
        }
    }
    
    private void  insertaLin(int id)  throws SQLException,ParseException
    {
        int nRows=jt.getRowCount();
        for (int n=0;n<nRows;n++)
        {
            if (jt.getValorInt(n,JT_PROCODI)==0)
                continue;
            dtAdd.addNew("partelin",false);
            dtAdd.setDato("par_codi",id);
            dtAdd.setDato("pro_codi",jt.getValorInt(n,JT_PROCODI));
            dtAdd.setDato("pal_kilos",jt.getValorDec(n,JT_PESO));
            dtAdd.setDato("pal_unidad",jt.getValorDec(n,JT_UNID));
            dtAdd.setDato("pro_ejelot",jt.getValorDec(n,JT_EJERC));
            dtAdd.setDato("pro_serlot",jt.getValString(n,JT_SERIE));
            dtAdd.setDato("pro_numlot",jt.getValorDec(n,JT_LOTE));
            dtAdd.setDato("pro_indlot",jt.getValorDec(n,JT_INDI));
            dtAdd.setDato("pal_acsala",pal_acsalaE.getValor(jt.getValString(n,JT_ACSALA)));
            dtAdd.setDato("pal_comsal",jt.getValString(n,JT_COMSAL));
            dtAdd.setDato("pal_accion",pal_accionE.getValor(jt.getValString(n,JT_ACCION)));
            dtAdd.setDato("pal_coment",jt.getValString(n,JT_COMENT));
            dtAdd.setDato("pal_solabo",jt.getValBoolean(n,JT_SOLABO)?-1:0);
            dtAdd.setDato("pal_kilabo",jt.getValorDec(n,JT_KILABO));
            dtAdd.setDato("pal_preabo",jt.getValorDec(n,JT_PREABO));
            dtAdd.update();
        }
    }
    private int insertaCab() throws SQLException,ParseException
    {
        dtAdd.addNew("partecab",false);
        dtAdd.setDato("emp_codi",EU.em_cod);
        dtAdd.setDato("pac_usuinc",EU.usuario);
        dtAdd.setDato("pac_fecinc",pac_fecincE.getDate());
        actualCab();
        dtStat.select("SELECT lastval()");
        return dtStat.getInt(1);
    }
    private void actualCab() throws  SQLException,ParseException
    {
        dtAdd.setDato("pac_estad",pac_estadE.getValor());
        dtAdd.setDato("pac_tipo",pac_tipoE.getValor());
        dtAdd.setDato("pac_coment",pac_comentE.getText());
        dtAdd.setDato("pac_cliprv",cli_codiE.isVisible()?cli_codiE.getValorInt():prv_codiE.getValorInt());
        dtAdd.setDato("pac_docano",pac_docanoE.getValorInt());
        dtAdd.setDato("pac_docser",pac_docserE.getText());
        dtAdd.setDato("pac_docnum",pac_docnumE.getValorInt());
        dtAdd.update();       
    }
    void  deleteParteLin() throws  SQLException
    {
        dtAdd.executeUpdate("delete from partelin where par_codi="+par_codiE.getValorInt());
    }
    /**
     * Comprueba que la entrada es valida
     * @return
     * @throws ParseException
     * @throws SQLException 
     */
    private boolean checkEntrada() throws ParseException, SQLException
    {
        Component c;
        if ( (c=Pcabe.getErrorConf())!=null)
        {
            c.requestFocus();
            return false;
        }
        if (pac_fecincE.isNull())
        {
            mensajeErr("Introduzca una fecha de alta");
            pac_fecincE.setDate(Formatear.getDateAct());
            pac_fecincE.requestFocus();
            return false;
        }
        long dias=Formatear.comparaFechas( pac_fecincE.getDate(),
            Formatear.getDateAct());
        if (dias>0)
        {
            mensajeErr("Fecha alta no puede ser superior a la actual");
            pac_fecincE.requestFocus();
            return false;
        }
        if (dias>7 && P_ESTADOS==10)
        {
            mensajeErr("Fecha alta no puede ser inferior a la actual en mas de 7 dias");
            pac_fecaltE.requestFocus();
            return false;
        }
        if ( pac_estadE.getValorInt()>P_ESTADOS)
        {
            mensajeErr("No tiene permisos para poner este tipo de estado");
            return false;
        }
        if (pac_tipoE.getValor().equals("D") || pac_tipoE.getValor().equals("C"))
        {
            if (!cli_codiE.controlar())
            {
                mensajeErr("Cliente no es valido");
                return false;
            }
            if (pac_docnumE.getValorInt()==0)
            {
                mensajeErr("Introduzca Albaran de Cliente");
                pac_docnumE.requestFocus();
                return false;
            }
            if (!pdalbara.getAlbaranCab( dtStat, EU.em_cod,pac_docanoE.getValorInt(),pac_docserE.getText(),pac_docnumE.getValorInt()))
            {
                mensajeErr("Albaran de Venta, no encontrado");
                pac_docanoE.requestFocus();
                return false;
            }
            if (dtStat.getInt("cli_codi")!=cli_codiE.getValorInt())
            {
                mensajeErr("Este Albaran de Venta no es de este cliente");
                pac_docanoE.requestFocus();
                return false;
            }
        }
        if (pac_tipoE.getValor().equals("E"))
        {
            if (!prv_codiE.controlar())
            {
                mensajeErr("Proveedor no es valido");
                return false;
            }
            if (! MantAlbCom.getCabeceraAlb(EU.em_cod, 
                pac_docanoE.getValorInt(),
                pac_docserE.getText(),pac_docnumE.getValorInt(), dtStat))
            {
                mensajeErr("Albaran de Compra, no encontrado");
                pac_docanoE.requestFocus();
                return false;
            }
            if (dtStat.getInt("prv_codi")!=prv_codiE.getValorInt())
            {
                mensajeErr("Este Albaran de Compra no es de este Proveedor");
                pac_docanoE.requestFocus();
                return false;
            }
        }
        jt.salirGrid();
        if (cambiaLineajt(jt.getSelectedRow())>0)
            return false;
        int nRows=jt.getRowCount();
        for (int n=0;n<nRows;n++)
        {
            if (jt.getValorInt(n,JT_PROCODI)>0)
                return true;
        }
        return false;
    }
    @Override
    public void canc_addnew() {
        mensaje("");
        mensajeErr("Cancelada alta de parte");
        activaTodo();
        nav.setPulsado(navegador.NINGUNO);
        verDatos();
    }

    @Override
    public void ej_delete1() {
         try
        {
          
            resetBloqueo(dtAdd, "v_partes",par_codiE.getText());
                        
            deleteParteLin();
            dtAdd.executeUpdate("delete from partecab where par_codi="+par_codiE.getValorInt());            
            dtAdd.commit();
            mensaje("");
            activaTodo();
            mensajeErr("Parte borrado correctamente");
            rgSelect();
            verDatos();
        } catch ( SQLException  ex)
        {
          Error("Error al Borrar parte",ex);
        } 
    }

    @Override
    public void canc_delete() {
     
        try {
          resetBloqueo(dtAdd, "v_partes",par_codiE.getText());
        } catch (Exception k)
        {
          mensajes.mensajeAviso("Error al Quitar el bloqueo\n"+k.getMessage());
        }
        mensaje("");
        mensajeErr("Cancelada Borrado de parte");
        activaTodo();
        nav.setPulsado(navegador.NINGUNO);
        
    }
    private void resetTexto()
    {
        Pcabe.resetTexto();
        pac_comentE.resetTexto();
     
        resetCambioLote();
    }
    private void resetCambioLote()
    {
        proIndLot=pro_indlotE.getValorInt();
        proNumLot=pro_numlotE.getValorInt();
        proCodi=pro_codiE.getValorInt();
        proSerLot=pro_serlotE.getText();
        proEjeLot=pro_ejelotE.getValorInt();
    }
    @Override
    public void activar(boolean activo) {
        activar(navegador.TODOS,activo);
    }
    public void activar(int tipo,boolean activo) {
        Baceptar.setEnabled(activo);
        Bcancelar.setEnabled(activo);
        if (P_ESTADOS>1)
            opAdmin.setEnabled(!activo);
        
        if (tipo==navegador.DELETE )
            return;
        if ((!activo || tipo != navegador.EDIT) || opAdmin.isSelected())        
            Pcabe.setEnabled(activo);
        
        jt.setEnabled( activo  && tipo != navegador.QUERY);        
        jtList.setEnabled(!activo);
        Pcabe.setEnabled(activo);
        pac_comentE.setEnabled(activo);        
        par_codiE.setEnabled(activo && tipo == navegador.QUERY);
        pac_estadE.setEnabled(activo || tipo != navegador.ADDNEW);
   
        activaFechas(tipo != navegador.QUERY || false);
        
    }
    
    private void activaCamposGrid()
    {
        boolean editable=nav.pulsado==navegador.ADDNEW || P_ESTADOS==1 || opAdmin.isSelected();
        pro_codiE.setEditable(editable);
        pal_kilosE.setEditable(editable);
        pal_unidadE.setEditable(editable);
        pro_ejelotE.setEditable(editable);
        pro_serlotE.setEditable(editable);
        pro_numlotE.setEditable(editable);
        pro_indlotE.setEditable(editable);
        pal_acsalaE.setEnabled(editable);
        pal_comsalE.setEditable(editable);
        if (nav.pulsado==navegador.ADDNEW)
            editable=opAdmin.isSelected();
        else
            editable=  opAdmin.isSelected() || P_ESTADOS>1;
        
        pal_accionE.setEnabled(editable);
        pal_comentE.setEditable(editable);
        pal_solaboE.setEnabled(editable);
        pal_kilaboE.setEditable(editable);
        pal_preaboE.setEditable(editable);        
    }
    
    private void Blistar_ActionPerformed()
    {
     this.setEnabled(false);
     mensaje("A esperar ... estoy generando el listado");
     try {
       String s="SELECT p.*,a.pro_nomb,a.pro_codi,p.*,cl.nombre  "+ 
        " FROM v_partes AS p left join v_cliprv as cl on p.pac_cliprv=cl.codigo and tipo!=pac_tipo,"+
         "  anjelica.v_articulo AS a WHERE "+
        " a.pro_codi = p.pro_codi "+
        (feciniE.isNull()?"":" and pac_fecinc >= '"+feciniE.getFechaDB()+"'")+
        (fecfinE.isNull()?"":" and pac_fecinc <= '"+ fecfinE.getFechaDB()+"'")+
        " and pac_estad = "+estadoE.getValor()+
        " order by  p.par_codi ";
     
       ResultSet rs = dtCon1.getStatement().executeQuery(dtCon1.parseaSql(s));
       java.util.HashMap mp = new java.util.HashMap();
       JasperReport jr;
       mp.put("estadoP",estadoE.getValor());
       mp.put("estadoT",estadoE.getText());
       mp.put("feciniP",feciniE.isNull()?null:feciniE.getDate());
       mp.put("fecfinP",fecfinE.isNull()?null:fecfinE.getDate());
   
       jr = gnu.chu.print.util.getJasperReport(EU, "partes");
      

       JasperPrint jp = JasperFillManager.fillReport(jr, mp, new JRResultSetDataSource(rs));
       gnu.chu.print.util.printJasper(jp, EU);
     
       mensaje("");
       mensajeErr("Listado ... generado");
       this.setEnabled(true);       
     }
     catch (Exception k)
     {
       Error("Error al imprimir consulta", k);
     }
    }
    private void activaFechas(boolean activo)
    {        
        pac_fecaltE.setEnabled(activo);
        pac_fecproE.setEnabled(activo);
        pac_fecresE.setEnabled(activo);
        pac_usuincE.setEnabled(activo);
        pac_usuproE.setEnabled(activo);
        pac_usuresE.setEnabled(activo);
    }
    
    private void cargaListado(String estado)
    {
        try {
            swCargaList=true;
            jtList.removeAllDatos();
            String s="select * from partecab where pac_estad='"+estado+"'"+
                (feciniE.isNull()?"":" and pac_fecinc>='"+feciniE.getFechaDB()+"'")+
                (fecfinE.isNull()?"":" and pac_fecinc<='"+fecfinE.getFechaDB()+"'")+
                " order by par_codi";
            if (! dtCon1.select(s))
            {                
                swCargaList=false;
                return;
            }
            do
            {
                ArrayList v=new ArrayList();
                v.add(dtCon1.getInt("par_codi"));
                v.add(dtCon1.getDate("pac_fecinc"));
                v.add(pac_tipoE.getText(dtCon1.getString("pac_tipo")));
                v.add(dtCon1.getInt("pac_cliprv"));
                if (dtCon1.getInt("pac_cliprv")!=0)
                {
                    if (dtCon1.getString("pac_tipo").equals("E"))
                       s="select prv_nomb as nombre from v_proveedo where prv_codi="+dtCon1.getInt("pac_cliprv");
                    else
                        s="select cli_nomb as nombre from v_cliente where cli_codi="+dtCon1.getInt("pac_cliprv");
                        
                    if (dtStat.select(s))
                        v.add(dtStat.getString("nombre"));
                    else
                        v.add("**NO ENCONTRADO**");
                }
                else
                    v.add("");
                v.add(dtCon1.getString("pac_coment"));
                jtList.addLinea(v);
            } while (dtCon1.next());
            jtList.requestFocusInicio();
            swCargaList=false;
        } catch (SQLException | ParseException k )
        {
            Error ("Error al cargar listado",k);
        }
    }
    private void verDatos()
    {
        try
        {
            if (dtCons.getNOREG())
                return;
            verDatos(dtCons.getInt("par_codi"));
        }  catch (SQLException k)
        {
            Error("Error al ver datos",k);
        }
    }
    
    private void verDatos(int parCodi) throws SQLException
    {
        
        par_codiE.setValorInt(parCodi);
        if (!dtCon1.select("select * from partecab where par_codi="+parCodi))
        {
            msgBox("Parte NO encontado. Probablemente se borro");
            jt.removeAllDatos();
            resetTexto();
            return;
        }
        swCargaList=true;
        pac_fecincE.setDate(dtCon1.getDate("pac_fecinc"));
        pac_estadE.setValor(dtCon1.getString("pac_estad"));
        pac_tipoE.setValor(dtCon1.getString("pac_tipo"));
        pac_comentE.setText(dtCon1.getString("pac_coment"));
        if (dtCon1.getString("pac_tipo").equals("E"))
            prv_codiE.setValorInt(dtCon1.getInt("pac_cliprv",true));                
        else
            cli_codiE.setValorInt(dtCon1.getInt("pac_cliprv",true));

        pac_docanoE.setValorInt(dtCon1.getInt("pac_docano"));
        pac_docserE.setText(dtCon1.getString("pac_docser"));
        pac_docnumE.setValorInt(dtCon1.getInt("pac_docnum"));

        pac_fecaltE.setDate(dtCon1.getDate("pac_fecalt"));
        pac_fecproE.setDate(dtCon1.getDate("pac_fecpro"));
        pac_fecresE.setDate(dtCon1.getDate("pac_fecres"));
        pac_usuincE.setText(dtCon1.getString("pac_usuinc"));
        pac_usuproE.setText(dtCon1.getString("pac_usupro"));
        pac_usuresE.setText(dtCon1.getString("pac_usures"));
        swCargaList=false;
        jt.removeAllDatos();

        if (!dtCon1.select("select l.*,a.pro_nomb from partelin as l,v_articulo as a"+
            " where par_codi="+parCodi+
            " and l.pro_codi = a.pro_codi "))
        {
            msgBox("No encontradas lineas para este PARTE");
            return;
        }

        do
        {
            ArrayList v=new ArrayList();
            v.add(dtCon1.getString("pro_codi"));
            v.add(dtCon1.getString("pro_nomb"));
            v.add(dtCon1.getString("pro_ejelot"));
            v.add(dtCon1.getString("pro_serlot"));
            v.add(dtCon1.getString("pro_numlot"));
            v.add(dtCon1.getString("pro_indlot"));
            v.add(dtCon1.getString("pal_unidad"));
            v.add(dtCon1.getString("pal_kilos"));
            v.add(pal_acsalaE.getText(dtCon1.getString("pal_acsala")));
            v.add(dtCon1.getString("pal_comsal"));
            v.add(pal_accionE.getText(dtCon1.getString("pal_accion")));
            v.add(dtCon1.getString("pal_coment"));
           
            v.add(dtCon1.getInt("pal_solabo")!=0);
            v.add(dtCon1.getString("pal_kilabo"));
            v.add(dtCon1.getString("pal_preabo"));

            
            jt.addLinea(v);
        } while (dtCon1.next());
        jt.requestFocusInicio();
    }

}
