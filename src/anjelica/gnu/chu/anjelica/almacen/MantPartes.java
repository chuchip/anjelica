package gnu.chu.anjelica.almacen;

import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.compras.AlbProv;
import gnu.chu.anjelica.compras.MantAlbCom;
import gnu.chu.anjelica.despiece.utildesp;
import gnu.chu.anjelica.menu;
import gnu.chu.anjelica.ventas.AlbClien;
import gnu.chu.anjelica.ventas.pdalbara;
import gnu.chu.camposdb.cliPanel;
import gnu.chu.controles.CComboBox;
import gnu.chu.controles.CTextArea;
import gnu.chu.controles.Cgrid;
import gnu.chu.controles.StatusBar;
import gnu.chu.eventos.GridAdapter;
import gnu.chu.eventos.GridEvent;
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
import java.util.Date;
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
 *   '0' Generar (Sala).  1 Gerencia , '2' Cerrar (Oficina)
 * admin: Especifica si podra cambiar el permiso desde el programa
 * Según el estado mandado como parametro podra Modificar/Borrar los partes de estado anterior o igual.
 * @author jpuente.ext
 */

public class MantPartes  extends ventanaPad implements PAD
{
    final static String[][] PAA_TIPO={{"Cliente","C"},
        {"Proveedor","P"} };
    final static String[][] PAC_ESTADO={{"Generada", "0"},
        {"Proc.Dev.", "1"},
        {"Procesada","2"},
        {"Resuelta", "3"},
        {"Cancelada", "4"}
    };
     final static String[][] PAC_TIPO={{"Sala", "S"},
         {  "Devolucion", "D"   },
         {"No Recep", "N"},
         {"Entrada", "E"},
         {"Dev.Recep", "R"}
     };
    
    int parLinea;
//    int focoGrid=1;
    String s;
    boolean swModificaTodo=true;
    AlbClien alCli=null;
    AlbProv  alPrv=null;
   
    final int ESTADO_GENERADA=0;
    final int ESTADO_PROCDEV=1;
    final int ESTADO_PROCESADA=2;
    final int ESTADO_CERRADO=3;
    final int ESTADO_CANCELA=4;
    
    final int JTABO_TIPO=0;
    final int JTABO_PROCOD=1;
    final int JTABO_CLIPRV=3;
    final int JTABO_COMEN=5;
    final int JTABO_KILOS=6;
    final int  JTABO_PRECIO=7;
    final int  JTABO_ID=9;
    
    //private final int JTLINEAS_PROCOD=2;
    
    private final int JTL_NUMPAR=0;
    final int JTLINEAS_PROCOD=0;
    private final int JTLINEAS_COMENT=2; 
    private final int JTLINEAS_ACCION=3; 
    
    final int JTLINEAS_NUMLIN=4;
    int proIndLot, proNumLot,proCodi,proEjeLot;
    String proSerLot;
    private boolean swCargaList=true,swCargaLin=true,swCargaLinAbo=false; // Cargando grid de listado
  
    private int P_PERMEST=0; // Especifica que Estados se permiten establecer
    final int PERM_INSERTAR=0;
    final int PERM_GERENC=1;
    final int PERM_OFICI=2;
    private boolean P_ADMIN;
    private final int  JT_PROCODI=0;
    private final int  JT_PRONOMB=1;
    private final int JT_EJERC=2;
    private final int JT_SERIE=3;
    private final int JT_LOTE=4;
    private final int JT_INDI=5;
    private final int JT_FECCAD=6;
    private final int JT_UNID=7;
    private final int JT_PESO=8;
    private final int JT_ACSALA=9;
    private final int JT_COMSAL=10;
    private final int JT_ACCION=11;
    private final int JT_COMENT=12; 
    
  
 
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
    
  public static String getDescrEstadoParte(int valorEstado)
  {
        for (String[] PAA_TIPO1 : PAC_ESTADO)
        {
            if (PAA_TIPO1[1].equals(""+valorEstado))
                return PAA_TIPO1[0];
        }
        return "";
  }
  public static String getDescrEstadoSala(String valorEstadoSala)
  {
       for (String[] PAA_TIPO1 : PAC_TIPO)
        {
            if (PAA_TIPO1[1].equals(valorEstadoSala))
                return PAA_TIPO1[0];
        }
        return "";    
  }
  public static String getNombreClase()
  {
      return "gnu.chu.anjelica.almacen.MantPartes";
  }
  public boolean inTransation()
  {
      return nav.isEdicion();
  }
  public void setTipoParte(String pacTipo)
  {
      pac_tipoE.setValor(pacTipo);
  }
  public void setProveedor(int prvCodi)
  {
        try
        {
            prv_codiE.setValorInt(prvCodi);
            prv_codiE.controla(false, true);
        } catch (SQLException ex)
        {
            Logger.getLogger(MantPartes.class.getName()).log(Level.SEVERE, null, ex);
        }
  }
  public void setNumeroParte(int numParte)
  {
      par_codiE.setValorInt(numParte);
  }
  public void setDocuAno(int docAno)
  {
      pac_docanoE.setValorInt(docAno);
      pac_docanoE.setEnabled(false);
  }
  public void setDocuSerie(String docSerie)
  {
      pac_docserE.setText(docSerie);
      pac_docserE.setEnabled(false);
  }
  public void setDocuNume(int docNume)
  {
      pac_docnumE.setValorInt(docNume);
      pac_docnumE.setEnabled(false);
      BbuscaDoc.setEnabled(false);
  }
  public void irGrid()
  {
   if (jt.isEnabled())
    jt.requestFocusInicioLater();
  }
  private void ponParametros(Hashtable<String,String> ht)
  {
      if (ht==null)
          return;
       if (ht.get("estados") != null)
             P_PERMEST = Integer.parseInt(ht.get("estados"));
       if (ht.get("admin") != null)
           P_ADMIN= ht.get("admin").equals("true");              
  }
  private void jbInit() throws Exception
  {
        nav = new navegador(this, dtCons, false,P_ADMIN?navegador.NORMAL:
            P_PERMEST==PERM_GERENC?navegador.SOLOEDIT | navegador.CURYCON :navegador.NORMAL);
        statusBar = new StatusBar(this);
        this.setVersion("(20160314) Modo: "+P_PERMEST);
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
      
      this.setSize(new Dimension(700,520));
      
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
      pro_codilE.iniciar(dtStat, this, vl, EU);
      paa_procodE.iniciar(dtStat, this, vl, EU);
      paa_procodE.setProNomb(paa_pronomE);
      pro_codiE.iniciar(dtStat, this, vl, EU);
      pro_codiE.setCamposLote(pro_ejelotE, pro_serlotE, pro_numlotE, pro_indlotE,pal_kilosE);
      pro_codiE.setAyudaLotes(true);
      PPie.setEnabled(false);
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
//      if (P_PERMEST==10)
//      {
//          String res=mensajes.mensajeGetTexto("Elija modo del programa", "Elija",this,"Administrador",new String[]{"Administrador","Gerencia","Oficina"});
//          switch (res)
//          {
//              case "Gerencia":
//                 P_PERMEST=ESTADO_PROCESADA;
//                 break;
//              case "Oficina":
//                 P_PERMEST=ESTADO_CERRADO;
//                 break;
//          }              
//      }
      if (P_PERMEST== PERM_OFICI)
        estadoE.setValor(""+ESTADO_PROCESADA);
      if (P_PERMEST==PERM_INSERTAR)
        estadoE.setValor(""+ESTADO_PROCDEV);
      cargaListado(estadoE.getValor());
      if (P_PERMEST!=PERM_INSERTAR )
      {
        PTabPane1.setSelectedIndex(1); 
        nav.setEnabled(pid, eje);
      }
      CPermiso.setValor(+P_PERMEST);
  }
  
  private void activarEventos()
  {
      jtAbo.addGridListener(new GridAdapter()
      {
        @Override
        public void cambioColumna(GridEvent event)   {
            
            if (event.getColumna()==JTABO_CLIPRV)
                afterCambiaColJTAbo(event.getLinea(),event.getColumna());
        }
        @Override
        public void cambiaLinea(GridEvent event){
           
              int reCaLin=cambiaLineaJTAbo(event.getLinea());
              event.setColError(reCaLin);
        }
        @Override
        public void afterCambiaLinea(GridEvent event){
              if (event.getColumna()==JTABO_CLIPRV)
                   afterCambiaColJTAbo(event.getLinea(),event.getColumna());
                 
        }
       
         @Override
        public boolean afterInsertaLinea(GridEvent event){
                if (jtAbo.getValorInt(event.getLinea(),  JTABO_ID)==0)
                  ponValDefAbo(); 
                return true;
        }
        @Override
        public void deleteLinea(GridEvent event){
            event.setPuedeBorrarLinea(deleteLineaJTAbo(event.getLinea()));
        }
        @Override
        public void focusLost(GridEvent event){             
//             guardaLineaAbono( parLinea);
        }
         @Override
         public void focusGained(GridEvent event){
//             focoGrid=2;
             parLinea=jtLineas.getValorInt(JTLINEAS_NUMLIN);
//             System.out.println("jtabo focusgained");
            if (jtAbo.getValorInt(JTABO_ID)==0)
                 ponValDefAbo();    
         }
      });
      jtAbo.addMouseListener(new MouseAdapter()
      {
          public void mouseClicked(MouseEvent e) {
              if (jtLineas.isEnabled() && !jtAbo.isEnabled() && e.getClickCount()>1)
              {
                  irGridAbo();
              }
                  
          }
      }); 
       jtLineas.addMouseListener(new MouseAdapter()
      {
          public void mouseClicked(MouseEvent e) {
              if (!jtLineas.isEnabled() && jtAbo.isEnabled() && e.getClickCount()>1)
              {
                  irGridLineas();
              }
                  
          }
      }); 
      jtLineas.addGridListener(new GridAdapter()
      {
          @Override
          public void focusGained(GridEvent event){
//              focoGrid=1;
//             System.out.println("jtLineas focusgained");
//                
//            jtAbo.setDefaultValor(JTABO_PROCOD, jtLineas.getValString(JTLINEAS_PROCOD));
//            jtAbo.setDefaultValor(JTABO_PROCOD+1, jtLineas.getValString(JTLINEAS_PROCOD+1));
//            jtAbo.setDefaultValor(0, paa_tipoE.getText(cli_codiE.isVisible()?"C":"P"));
//            jtAbo.setDefaultValor(JTABO_CLIPRV, cli_codiE.isVisible()?cli_codiE.getText():prv_codiE.getText());
          }   
       
      });
//  
      Bcerrar.addActionListener(new ActionListener()
       {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            try { 
              if (jtList.isVacio())
                  return;              
              cerrarIncidencia(e.getActionCommand().startsWith("Cerr")? ESTADO_CERRADO:ESTADO_CANCELA);
              swCargaLin=true;
              swCargaList=true;
              jtList.removeLinea();
              swCargaLin=false;
              swCargaList=false;
              verDetalleLinea();
           } catch (SQLException ex) {
               Error("Error al cerrar incidencia",ex);
           }
         }
       });
       CPermiso.addActionListener(new ActionListener()
       {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            P_PERMEST=CPermiso.getValorInt();
         }
       });
       BswGrid.addActionListener(new ActionListener()
       {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            if (jtLineas.isEnabled())
            {
                irGridAbo();           
            }
             else
            {
                irGridLineas();
         
            }
         }
       });
      
      pac_tipoE.addActionListener(new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            actClientePrv(pac_tipoE.getValor().equals("E"));
        }
      });
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
           irGrid();
          
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
    
     
        jtList.tableView.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (swCargaLin || nav.pulsado != navegador.NINGUNO)
                    return;
                if (e.getValueIsAdjusting()) {
                    return;
                }
                try
                {     
                     verDatos(jtList.getValorInt(0));
                     verLineas(jtList.getValorInt(JTL_NUMPAR));
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
             if (e.getClickCount()<2 || jtList.isVacio() )
                 return;
              try
              {
                  verDatos(jtList.getValorInt(JTL_NUMPAR));
              } catch (SQLException ex)
              {
                  Error("Error al ver datos", ex);
                  return;
              }
            swModificaTodo=false;
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
    
    tipoE.addActionListener(new ActionListener(){
     @Override
     public void actionPerformed(ActionEvent e)
     {
        if (nav.getPulsado()!=navegador.NINGUNO || swCargaList)
            return;
        cargaListado(estadoE.getValor());     
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
  void irGridAbo()
  {
      jtLineas.salirGrid();
      int nc = cambiaLineaJTLineas(jtLineas.getSelectedRow());
      if (nc >= 0)
      {
          jtLineas.requestFocus(nc);
          return;
      }
      jtAbo.setEnabled(true);
      jtLineas.setEnabled(false);
      jtAbo.requestFocusLater(0, JTABO_CLIPRV);
  }
  void irGridLineas()
  {
      jtAbo.salirGrid();
      int nc = cambiaLineaJTAbo(jtAbo.getSelectedRow());
      if (nc >= 0)
      {
          jtAbo.requestFocus(nc);
          return;
      }
      guardaLineaAbono(jtLineas.getValorInt(jtLineas.getSelectedRowDisab(), JTLINEAS_NUMLIN));
      jtAbo.setEnabled(false);
      jtLineas.setEnabled(true);
      jtLineas.requestFocusLater();
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
   nav.setPulsado(navegador.ADDNEW);
   PTabPane1.setSelectedIndex(0);           
   resetTexto();
   
   jt.removeAllDatos();
   pac_estadE.setValor(ESTADO_GENERADA);
   activar(navegador.ADDNEW,true);
   mensaje("Insertando NUEVO Registro");
   pac_fecincE.setDate(Formatear.getDateAct());
   pac_usuincE.setText(EU.usuario);
   pac_docanoE.setValorInt(EU.ejercicio);
   pac_docserE.setText("A");
   
   pac_estadE.setEnabled(false);
   pac_fecincE.requestFocus();
//   activaCamposGrid();
 }
 @Override
 public void PADDelete()
 {
       try
        {
            PTabPane1.setSelectedIndex(0); 
            if (!pac_fecproE.isNull())
            {
                msgBox("Parte ya se proceso. Imposible BORRAR.");
                nav.setPulsado(navegador.NINGUNO);
                activaTodo();
                return;
            }
            if (pac_usuincE.isNull())
            {
                mensajeErr("Parte no encontrado");
                nav.setPulsado(navegador.NINGUNO);
                activaTodo();
                return;
            }
            if ( pac_estadE.getValorInt() >  P_PERMEST)
            {
                msgBox(("Parte no puede ser Borrado con sus permisos"));
                nav.setPulsado(navegador.NINGUNO);
                activaTodo();
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
 
 /**
  * Carga Combo con las diferentes acciones posibles
  * @param c CComboBox
  * @param tipo   'D'evolucion -> Aceptado/No Aceptado
  *               'N'ormal -> Vert,Reut. Cong
  *               '*' -> Ambas
  */
 private void setAcciones(CComboBox c,char tipo)
 {
    c.removeAllItems();
    c.addItem("NO DEF.","-");
    if (tipo=='D' || tipo=='*')
    {
        c.addItem("Acep.Dev","A");
        c.addItem("No Acep. Dev","N");
    }
    if (tipo=='N' || tipo=='*')
    {    
        c.addItem("Vert.","V");
        c.addItem("Reut.","R");
        c.addItem("Cong.","C");
    }
 }
 void ponValDefAbo()
 {
    paa_tipoE.setValor(cli_codiE.isVisible()?"C":"P");
    jtAbo.setValor(paa_tipoE.getText(cli_codiE.isVisible()?"C":"P"),JTABO_TIPO);    
    paa_procodE.setText(jtLineas.getValString(JTLINEAS_PROCOD));             
    jtAbo.setValor(jtLineas.getValString(JTLINEAS_PROCOD),JTABO_PROCOD);
    jtAbo.setValor(jtLineas.getValString(JTLINEAS_PROCOD+1),JTABO_PROCOD+1);
       
    jtAbo.setValor(cli_codiE.isVisible()?cli_codiE.getText():prv_codiE.getText(),JTABO_CLIPRV);
    paa_cliprvE.setText(jtAbo.getValString(JTABO_CLIPRV));
    jtAbo.setValor(cli_codiE.isVisible()?cli_codiE.getTextNomb():prv_codiE.getTextNomb(),JTABO_CLIPRV+1);

 }
  @Override
  public void activaTodo()
  {
      swModificaTodo=true;
      setAcciones(pal_accionE, '*');
      activar(false);
//    this.setEnabled(true);
        navActivarAll();
        nav.requestFocus();
//      super.activaTodo();
  }
 private void calcAcumulados()
 {
    resetCambioLote();
 }
 /**
  * 
  * @param row
  * @param col 
  */
 void afterCambiaColJTAbo(int row, int col)
 {
        try
        {
            if (paa_cliprvE.isNull())
                return;
            String nombre="**NO ENCONTRADO**";
            s=getNombCliPrv(dtStat,paa_tipoE.getValor(),paa_cliprvE.getValorInt());
            if (s!=null)   
                nombre=s;
            paa_clpvnoE.setText(nombre);
            jtAbo.setValor(nombre,row,JTABO_CLIPRV);
        } catch (SQLException ex)
        {
            Error("Error al buscar Cliente/Prv.",ex);
        }
         
 }
 /**
  * Devuelve el nombre del cliente o prv.
  * @return Nombre. Null si no lo encuentra
  * @throws SQLException 
  */
 String getNombCliPrv(DatosTabla dt,String tipo,int cliprv) throws SQLException
 {
     if (tipo.equals("E") || tipo.equals("P"))
       s="select prv_nomb as nombre from v_proveedo where prv_codi="+cliprv;
     else
       s="select cli_nomb as nombre from v_cliente where cli_codi="  +cliprv;
                        
      if (dt.select(s))
          return dt.getString("nombre");
      return null;
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
                pro_feccadE.setDate(stkPartid.getFechaCad());
                jt.setValor(stkPartid.getKilos(),JT_PESO);
                jt.setValor(stkPartid.getUnidades(),JT_UNID);
                jt.setValor(stkPartid.getFechaCad(),JT_FECCAD);
            }
        } catch (SQLException ex)
        {
            Error("Error al buscar peso de individuo", ex);
        }
    

 }
 /**
  * Borra linea de abono
  * @param linea
  * @return 
  */
 boolean deleteLineaJTAbo(int linea)
 {
        try
        {
            if (jtAbo.getValorInt(linea,JTABO_ID)==0)
                return true;
            int nrAfected= dtAdd.executeUpdate("delete from parteabo where paa_codi= "+jtAbo.getValorInt(linea,JTABO_ID));
            if (nrAfected==0)
            {
                msgBox("No encontrado parte de abono a borrar");
                return false;
            }  
        } catch (SQLException ex)
        {
            Error("Error al borrar parte de abono", ex);           
        }
        return true;
 }
 int cambiaLineaJTAbo(int linea) 
 {
        try
        {
            if ( paa_kilosE.isNull())
                return -1;
            if (paa_cliprvE.isNull()) 
            {
                mensajeErr("Introduzca codigo de Proveedor/Cliente");
                return JTABO_CLIPRV;
            }
            
            if (getNombCliPrv(dtStat,paa_tipoE.getValor(),paa_cliprvE.getValorInt())==null)
            {
                mensajeErr(paa_tipoE.getValor()+"  Cliente/Proveedor no es valido");
                return JTABO_CLIPRV;
            }
            if (paa_procodE.isNull())
            {
                mensajeErr("Introduzca codigo de Articulo");
                return JTABO_PROCOD;                
            }
            if (!paa_procodE.controla(false))
            {
                mensajeErr("Articulo NO es valido");
                return JTABO_PROCOD;
            }
        
            if (paa_precioE.isNull())
            {
                mensajeErr("Introduzca Precio del Abono");
                return JTABO_PRECIO;
            }            
        } catch (SQLException ex)
        {
           Error("Error al comprobar linea abono",ex);
        }
        return -1;
 }
 /**
  * 
  * @param linea
  * @return 
  */
 int cambiaLineaJTLineas(int linea)
 {
//     jtAbo.salirGrid();
//     if (cambiaLineaJTAbo(jtAbo.getSelectedRow())>0)
//             return -1;     
////
     return -1;     
 }
 void guardaLineaAbono(int linea)
 {
      try
      {
          for (int n=0;n<jtAbo.getRowCount();n++)
          {        
             if (jtAbo.getValorDec(n,JTABO_KILOS)==0  )
                 continue;
             int rowid=jtAbo.getValorInt(n,JTABO_ID);
             if (rowid==0)
             {
                // System.out.println("guard. Linea abono: "+par_codiE.getValorInt()+"-"+linea+" kg: "+jtAbo.getValorDec(n,JTABO_KILOS));
                dtAdd.addNew("parteabo",false);
                dtAdd.setDato("par_codi",par_codiE.getValorInt());
                dtAdd.setDato("par_linea",linea);
             }
             else
             {
                 if (!dtAdd.select("select * from parteabo where paa_codi ="+rowid,true))
                 {
                     Error("No encontradas ID de abono: "+rowid,new SQLException(""));
                 }
//                 System.out.println("Act. Linea abono con rowid: "+rowid+" parte: "+par_codiE.getValorInt()+"-"+linea+" kg: "+jtAbo.getValorDec(n,JTABO_KILOS));
                 dtAdd.edit();
             }
             dtAdd.setDato("paa_tipo",paa_tipoE.getValor(jtAbo.getValString(n,JTABO_TIPO)));
             dtAdd.setDato("paa_cliprv",jtAbo.getValorInt(n,JTABO_CLIPRV));
             dtAdd.setDato("pro_codi",jtAbo.getValorInt(n,JTABO_PROCOD));
             dtAdd.setDato("paa_coment",jtAbo.getValString(n,JTABO_COMEN));
             dtAdd.setDato("paa_kilos",jtAbo.getValorDec(n,JTABO_KILOS));
             dtAdd.setDato("paa_precio",jtAbo.getValorDec(n,JTABO_PRECIO));
             dtAdd.update();
             if (rowid==0)
             {
                dtAdd.select("SELECT lastval()");
                jtAbo.setValor(dtAdd.getInt(1),n,JTABO_ID) ;             
             }
          }
          dtAdd.commit();
         } catch (SQLException ex)
         {
            Error("Error al grabar linea de parte abono",ex);  
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
        paa_precioE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9.99");
        pal_acsalaE = new gnu.chu.controles.CComboBox();
        pal_accionE = new gnu.chu.controles.CComboBox();
        pal_comsalE = new gnu.chu.controles.CTextField(Types.CHAR,"X",50);
        pal_uniaboE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        pro_feccadE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yy");
        pro_codilE = new gnu.chu.camposdb.proPanel();
        pro_nomblE = new gnu.chu.controles.CTextField();
        paa_kilosE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9.99");
        pro_feccadlE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yy");
        paa_comentE = new gnu.chu.controles.CTextField(Types.CHAR,"X",50);
        paa_tipoE = new gnu.chu.controles.CComboBox();
        paa_estadE = new gnu.chu.controles.CCheckBox();
        par_lineaE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        paa_procodE = new gnu.chu.camposdb.proPanel(){

        };
        paa_pronomE = new gnu.chu.controles.CTextField();
        paa_cliprvE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#####9");
        paa_clpvnoE = new gnu.chu.controles.CTextField();
        paa_codiE = new gnu.chu.controles.CTextField(Types.DECIMAL,"######9");
        Pprinc = new gnu.chu.controles.CPanel();
        PTabPane1 = new gnu.chu.controles.CTabbedPane();
        Pcabe = new gnu.chu.controles.CPanel();
        Pcab = new gnu.chu.controles.CPanel();
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
        BsaltaGrid = new gnu.chu.controles.CButton();
        jt = new gnu.chu.controles.CGridEditable(11)
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
            public boolean insertaLinea(int row, int col)
            {
                return  nav.getPulsado()==navegador.ADDNEW || pac_estadE.getValorInt()< ESTADO_PROCESADA;
            }
            public boolean deleteLinea(int row, int col) {
                return jt.getValorInt(row,JT_PROCODI)==0  || nav.getPulsado()==navegador.ADDNEW || pac_estadE.getValorInt()==ESTADO_GENERADA;

            }

        };
        Plistado = new gnu.chu.controles.CPanel();
        jtList = new gnu.chu.controles.Cgrid(6);
        jtLineas = new gnu.chu.controles.CGridEditable(5)
        {

            public void afterCambiaLinea()
            {
                verDetalleLinea();
            }
            public void afterCambiaLineaDis(int nRow)
            {
                if (!jtAbo.isEnabled())
                verDetalleLinea();
            }
        }
        ;
        jtAbo = new gnu.chu.controles.CGridEditable(10)
        ;
        PCondFiltro = new gnu.chu.controles.CPanel();
        pac_estadL1 = new gnu.chu.controles.CLabel();
        estadoE = new gnu.chu.controles.CComboBox();
        pac_estadL2 = new gnu.chu.controles.CLabel();
        feciniE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yy");
        pac_estadL3 = new gnu.chu.controles.CLabel();
        fecfinE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yy");
        BListar = new gnu.chu.controles.CButton(Iconos.getImageIcon("print"));
        BRefresh = new gnu.chu.controles.CButton(Iconos.getImageIcon("reload"));
        pac_tipoL1 = new gnu.chu.controles.CLabel();
        tipoE = new gnu.chu.controles.CComboBox();
        PPie = new gnu.chu.controles.CPanel();
        pro_ejelotE1 = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        pro_numlotE1 = new gnu.chu.controles.CTextField(Types.DECIMAL,"####9");
        pro_serlotE1 = new gnu.chu.controles.CTextField(Types.CHAR,"X",1);
        pro_indlotE1 = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        cLabel3 = new gnu.chu.controles.CLabel();
        pal_comsalE1 = new gnu.chu.controles.CTextField(Types.CHAR,"X",50);
        pal_acsalaE1 = new gnu.chu.controles.CComboBox();
        cLabel4 = new gnu.chu.controles.CLabel();
        cLabel5 = new gnu.chu.controles.CLabel();
        pal_kilosE1 = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9.99");
        pal_unidadE1 = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        cLabel6 = new gnu.chu.controles.CLabel();
        cLabel7 = new gnu.chu.controles.CLabel();
        pro_feccadE1 = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yy");
        Bcerrar = new gnu.chu.controles.CButtonMenu();
        BswGrid = new gnu.chu.controles.CButton(Iconos.getImageIcon("duplicar"));
        Ppie = new gnu.chu.controles.CPanel();
        Baceptar = new gnu.chu.controles.CButton();
        Bcancelar = new gnu.chu.controles.CButton();
        CPermiso = new gnu.chu.controles.CComboBox();

        pro_codiE.setProNomb(pro_nombE);
        pro_nombE.setEnabled(false);

        pal_unidadE.setText("1");
        pal_unidadE.setToolTipText("");

        pro_ejelotE.setValorDec(EU.ejercicio);

        pro_serlotE.setText("A");
        pro_serlotE.setMayusc(true);

        pal_acsalaE.addItem("NO Def","-");
        pal_acsalaE.addItem("Vert.","V");
        pal_acsalaE.addItem("Reut.","R");
        pal_acsalaE.addItem("Cong.","C");
        pal_acsalaE.setGridEditable(jt);

        setAcciones(pal_accionE,'*');
        pal_accionE.setGridEditable(jt);

        pal_uniaboE.setText("1");
        pal_uniaboE.setToolTipText("");

        pro_codilE.setEnabled(false);

        pro_codiE.setProNomb(pro_nombE);
        pro_nomblE.setEnabled(false);

        pro_feccadlE.setEnabled(false);

        paa_tipoE.addItem(PAA_TIPO);
        paa_tipoE.setGridEditable(jtAbo);

        paa_estadE.setEnabled(false);

        par_lineaE.setText("1");
        par_lineaE.setToolTipText("");
        par_lineaE.setEnabled(false);

        paa_procodE.setProNomb(pro_nombE);
        paa_pronomE.setEnabled(false);

        paa_clpvnoE.setEnabled(false);

        paa_codiE.setEnabled(false);

        Pprinc.setLayout(new java.awt.GridBagLayout());

        PTabPane1.setMaximumSize(new java.awt.Dimension(700, 410));
        PTabPane1.setMinimumSize(new java.awt.Dimension(700, 410));
        PTabPane1.setPreferredSize(new java.awt.Dimension(700, 410));

        Pcabe.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Pcabe.setLayout(new java.awt.GridBagLayout());

        Pcab.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Pcab.setMaximumSize(new java.awt.Dimension(700, 180));
        Pcab.setMinimumSize(new java.awt.Dimension(700, 180));
        Pcab.setPreferredSize(new java.awt.Dimension(700, 180));
        Pcab.setLayout(null);

        par_codiL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        par_codiL.setText("Parte");
        Pcab.add(par_codiL);
        par_codiL.setBounds(10, 3, 50, 17);
        Pcab.add(par_codiE);
        par_codiE.setBounds(70, 3, 50, 17);

        pac_fecaltL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        pac_fecaltL.setText("Fecha");
        Pcab.add(pac_fecaltL);
        pac_fecaltL.setBounds(0, 45, 60, 17);

        par_comentL.setText("Comentario");
        Pcab.add(par_comentL);
        par_comentL.setBounds(210, 3, 80, 17);

        pac_tipoL.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        pac_tipoL.setText("Tipo");
        Pcab.add(pac_tipoL);
        pac_tipoL.setBounds(10, 25, 50, 17);

        pac_tipoE.addItem(PAC_TIPO);
        Pcab.add(pac_tipoE);
        pac_tipoE.setBounds(70, 25, 110, 17);

        pac_comentE.setColumns(20);
        pac_comentE.setRows(5);
        jScrollPane1.setViewportView(pac_comentE);

        Pcab.add(jScrollPane1);
        jScrollPane1.setBounds(210, 20, 440, 45);

        pac_cliprvL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        pac_cliprvL.setText("Proveedor");
        Pcab.add(pac_cliprvL);
        pac_cliprvL.setBounds(0, 85, 70, 17);
        Pcab.add(cli_codiE);
        cli_codiE.setBounds(70, 85, 370, 17);
        Pcab.add(prv_codiE);
        prv_codiE.setBounds(70, 85, 370, 18);

        cLabel2.setText("Documento");
        Pcab.add(cLabel2);
        cLabel2.setBounds(450, 85, 70, 17);
        Pcab.add(pac_fecincE);
        pac_fecincE.setBounds(70, 45, 70, 17);
        Pcab.add(pac_docanoE);
        pac_docanoE.setBounds(520, 85, 40, 17);

        pac_docserE.setAutoNext(true);
        pac_docserE.setMayusc(true);
        Pcab.add(pac_docserE);
        pac_docserE.setBounds(560, 85, 20, 17);
        Pcab.add(pac_docnumE);
        pac_docnumE.setBounds(580, 85, 60, 17);

        BbuscaDoc.setToolTipText("Buscar documentos (F3)");
        BbuscaDoc.setFocusable(false);
        Pcab.add(BbuscaDoc);
        BbuscaDoc.setBounds(640, 85, 22, 22);

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

        Pcab.add(Pusuarios);
        Pusuarios.setBounds(10, 105, 360, 70);

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

        Pcab.add(Pfechas);
        Pfechas.setBounds(390, 105, 280, 70);

        pac_estadL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        pac_estadL.setText("Estado");
        Pcab.add(pac_estadL);
        pac_estadL.setBounds(0, 65, 60, 17);

        pac_estadE.addItem(PAC_ESTADO);
        Pcab.add(pac_estadE);
        pac_estadE.setBounds(70, 65, 110, 17);

        BirGrid.setFocusable(false);
        Pcab.add(BirGrid);
        BirGrid.setBounds(670, 90, 2, 2);

        BsaltaGrid.setText("cButton1");
        Pcab.add(BsaltaGrid);
        BsaltaGrid.setBounds(660, 90, 2, 2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        Pcabe.add(Pcab, gridBagConstraints);

        {
            confGridArt(jt);

            ArrayList v=new ArrayList();
            v.add(pro_codiE.getTextField()); // 0
            v.add(pro_nombE);  // 1
            v.add(pro_ejelotE); // 2
            v.add(pro_serlotE); // 3
            v.add(pro_numlotE); // 4
            v.add(pro_indlotE); // 5
            v.add(pro_feccadE); // 6
            v.add(pal_unidadE); // 7
            v.add(pal_kilosE); // 8
            v.add(pal_acsalaE); // 9
            v.add(pal_comsalE); // 10

            try {
                jt.setCampos(v);
            } catch (Exception k)
            {
                Error("Error al iniciar campos del grid",k);
                return;
            }
        }
        jt.setNumRegCargar(0);
        jt.setDefButton(Baceptar);
        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jt.setFormatoCampos();
        jt.setMaximumSize(new java.awt.Dimension(700, 101));
        jt.setMinimumSize(new java.awt.Dimension(700, 101));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Pcabe.add(jt, gridBagConstraints);

        PTabPane1.addTab("Datos", Pcabe);

        Plistado.setAltButton(BRefresh);
        Plistado.setLayout(new java.awt.GridBagLayout());

        jtList.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtList.setMaximumSize(new java.awt.Dimension(689, 90));
        jtList.setMinimumSize(new java.awt.Dimension(689, 90));

        ArrayList v1=new ArrayList();
        v1.add("Parte"); // 0
        v1.add("Fecha"); // 1
        v1.add("Tipo"); // 2
        v1.add("Cl/Prv"); // 3
        v1.add("Nombre"); // 4
        v1.add("Coment"); // 5
        jtList.setCabecera(v1);
        jtList.setAnchoColumna(new int[]{40,60,80,40,150,250});
        jtList.setAlinearColumna(new int[]{2,1,0,2,0,0});
        jtList.setFormatoColumna(1,"dd-MM-yy");
        jtList.setAjustarGrid(true);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 2.0;
        Plistado.add(jtList, gridBagConstraints);

        {
            ArrayList v=new ArrayList();
            v.add("Articulo"); // 0
            v.add("Nombre"); // 1
            v.add("Coment"); // 2
            v.add("Accion"); // 3
            v.add("Lin"); //4
            jtLineas.setCabecera(v);

            ArrayList vc=new ArrayList();

            vc.add(pro_codilE.getTextField());
            vc.add(pro_nomblE);
            vc.add(pal_comentE);
            vc.add(pal_accionE);
            vc.add(par_lineaE);
            try {
                jtLineas.setCampos(vc);
            } catch (Exception k)
            {
                Error("Error al iniciar campos del grid",k);
                return;
            }
            jtLineas.setFormatoCampos();
        }
        jtLineas.setAlinearColumna(new int[]{2,0,0,0,2});
        jtLineas.setAnchoColumna(new int[]{50,120,150,70,20});
        jtLineas.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtLineas.setAltButton(BswGrid);
        jtLineas.setCanDeleteLinea(false);
        jtLineas.setCanInsertLinea(false);
        jtLineas.setDefButton(Baceptar);
        jtLineas.setMaximumSize(new java.awt.Dimension(400, 90));
        jtLineas.setMinimumSize(new java.awt.Dimension(400, 90));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        Plistado.add(jtLineas, gridBagConstraints);

        {
            confGridAbo(jtAbo);

            ArrayList v=new ArrayList();
            v.add(paa_tipoE); // 0
            v.add(paa_procodE.getTextField()); // 1
            v.add(paa_pronomE);  // 2
            v.add(paa_cliprvE); // 3
            v.add(paa_clpvnoE); // 4
            v.add(paa_comentE); // 5
            v.add(paa_kilosE); // 6
            v.add(paa_precioE); // 7
            v.add(paa_estadE); // 8
            v.add(paa_codiE); // 9
            try {
                jtAbo.setCampos(v);
            } catch (Exception k)
            {
                Error("Error al iniciar campos del grid",k);
                return;
            }
        }
        jtAbo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtAbo.setAltButton(BswGrid);
        jtAbo.setDefButton(Baceptar);
        jtAbo.setColNueva(6);
        jtAbo.setMaximumSize(new java.awt.Dimension(400, 90));
        jtAbo.setMinimumSize(new java.awt.Dimension(400, 90));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        Plistado.add(jtAbo, gridBagConstraints);

        PCondFiltro.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        PCondFiltro.setMaximumSize(new java.awt.Dimension(709, 24));
        PCondFiltro.setMinimumSize(new java.awt.Dimension(709, 24));
        PCondFiltro.setPreferredSize(new java.awt.Dimension(709, 24));
        PCondFiltro.setLayout(null);

        pac_estadL1.setText("De fecha:");
        PCondFiltro.add(pac_estadL1);
        pac_estadL1.setBounds(300, 2, 60, 17);

        estadoE.addItem(PAC_ESTADO);
        PCondFiltro.add(estadoE);
        estadoE.setBounds(50, 2, 110, 17);

        pac_estadL2.setText("Estado");
        PCondFiltro.add(pac_estadL2);
        pac_estadL2.setBounds(10, 2, 50, 17);
        PCondFiltro.add(feciniE);
        feciniE.setBounds(360, 2, 70, 17);

        pac_estadL3.setText("A fecha:");
        PCondFiltro.add(pac_estadL3);
        pac_estadL3.setBounds(440, 2, 50, 17);
        PCondFiltro.add(fecfinE);
        fecfinE.setBounds(490, 2, 70, 17);

        BListar.setText("Listar");
        PCondFiltro.add(BListar);
        BListar.setBounds(610, 2, 80, 19);

        BRefresh.setToolTipText("Refrescar Consulta");
        PCondFiltro.add(BRefresh);
        BRefresh.setBounds(570, 2, 20, 20);

        pac_tipoL1.setText("Tipo");
        PCondFiltro.add(pac_tipoL1);
        pac_tipoL1.setBounds(170, 2, 30, 17);

        tipoE.addItem("TODOS", "*");
        tipoE.addItem(PAC_TIPO);
        PCondFiltro.add(tipoE);
        tipoE.setBounds(200, 2, 95, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 0);
        Plistado.add(PCondFiltro, gridBagConstraints);

        PPie.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        PPie.setEnabled(false);
        PPie.setMaximumSize(new java.awt.Dimension(689, 47));
        PPie.setMinimumSize(new java.awt.Dimension(689, 47));
        PPie.setPreferredSize(new java.awt.Dimension(689, 47));
        PPie.setLayout(null);

        pro_ejelotE.setValorDec(EU.ejercicio);
        PPie.add(pro_ejelotE1);
        pro_ejelotE1.setBounds(170, 5, 40, 17);
        PPie.add(pro_numlotE1);
        pro_numlotE1.setBounds(210, 5, 50, 17);

        pro_serlotE1.setText("A");
        pro_serlotE1.setMayusc(true);
        PPie.add(pro_serlotE1);
        pro_serlotE1.setBounds(260, 5, 20, 17);
        PPie.add(pro_indlotE1);
        pro_indlotE1.setBounds(280, 5, 50, 17);

        cLabel3.setText("Consejo");
        PPie.add(cLabel3);
        cLabel3.setBounds(0, 25, 60, 17);
        PPie.add(pal_comsalE1);
        pal_comsalE1.setBounds(180, 25, 455, 17);

        pal_acsalaE1.addItem("NO Def","-");
        pal_acsalaE1.addItem("Vert.","V");
        pal_acsalaE1.addItem("Reut.","R");
        pal_acsalaE1.addItem("Cong.","C");
        pal_acsalaE1.setGridEditable(jt);
        PPie.add(pal_acsalaE1);
        pal_acsalaE1.setBounds(60, 25, 110, 17);

        cLabel4.setText("Kilos");
        PPie.add(cLabel4);
        cLabel4.setBounds(540, 5, 35, 17);

        cLabel5.setText("Individuo");
        PPie.add(cLabel5);
        cLabel5.setBounds(120, 5, 50, 17);
        PPie.add(pal_kilosE1);
        pal_kilosE1.setBounds(580, 5, 57, 17);

        pal_unidadE1.setEnabled(false);
        PPie.add(pal_unidadE1);
        pal_unidadE1.setBounds(500, 5, 40, 17);

        cLabel6.setText("Caducid.");
        PPie.add(cLabel6);
        cLabel6.setBounds(340, 5, 55, 17);

        cLabel7.setText("Unid.");
        PPie.add(cLabel7);
        cLabel7.setBounds(460, 5, 35, 17);
        PPie.add(pro_feccadE1);
        pro_feccadE1.setBounds(400, 5, 60, 17);

        Bcerrar.addMenu("Cerrar", "C");
        Bcerrar.addMenu("Cancelar", "X");
        Bcerrar.setToolTipText("Cerrar o Cancelar Incidencia");
        Bcerrar.setText("Cerrar");
        PPie.add(Bcerrar);
        Bcerrar.setBounds(5, 3, 100, 20);

        BswGrid.setToolTipText("Ir o volver a lineas de abono");
        BswGrid.setDependePadre(false);
        PPie.add(BswGrid);
        BswGrid.setBounds(660, 20, 24, 24);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        Plistado.add(PPie, gridBagConstraints);

        PTabPane1.addTab("Listado", Plistado);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Pprinc.add(PTabPane1, gridBagConstraints);

        Ppie.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Ppie.setMaximumSize(new java.awt.Dimension(440, 35));
        Ppie.setMinimumSize(new java.awt.Dimension(440, 35));
        Ppie.setName(""); // NOI18N
        Ppie.setPreferredSize(new java.awt.Dimension(440, 35));
        Ppie.setLayout(null);

        Baceptar.setText("Aceptar");
        Ppie.add(Baceptar);
        Baceptar.setBounds(10, 2, 90, 30);

        Bcancelar.setText("Cancelar");
        Ppie.add(Bcancelar);
        Bcancelar.setBounds(140, 2, 90, 30);

        CPermiso.addItem("Insertar", "0");
        CPermiso.addItem("Gerencia", "1");
        CPermiso.addItem("Oficina", "2");
        Ppie.add(CPermiso);
        CPermiso.setBounds(260, 5, 140, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
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
    private gnu.chu.controles.CButtonMenu Bcerrar;
    private gnu.chu.controles.CButton BirGrid;
    private gnu.chu.controles.CButton BsaltaGrid;
    private gnu.chu.controles.CButton BswGrid;
    private gnu.chu.controles.CComboBox CPermiso;
    private gnu.chu.controles.CPanel PCondFiltro;
    private gnu.chu.controles.CPanel PPie;
    private gnu.chu.controles.CTabbedPane PTabPane1;
    private gnu.chu.controles.CPanel Pcab;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Pfechas;
    private gnu.chu.controles.CPanel Plistado;
    private gnu.chu.controles.CPanel Ppie;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CPanel Pusuarios;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.camposdb.cliPanel cli_codiE;
    private gnu.chu.controles.CComboBox estadoE;
    private gnu.chu.controles.CTextField fecfinE;
    private gnu.chu.controles.CTextField feciniE;
    private javax.swing.JScrollPane jScrollPane1;
    private gnu.chu.controles.CGridEditable jt;
    private gnu.chu.controles.CGridEditable jtAbo;
    private gnu.chu.controles.CGridEditable jtLineas;
    private gnu.chu.controles.Cgrid jtList;
    private gnu.chu.controles.CTextField paa_cliprvE;
    private gnu.chu.controles.CTextField paa_clpvnoE;
    private gnu.chu.controles.CTextField paa_codiE;
    private gnu.chu.controles.CTextField paa_comentE;
    private gnu.chu.controles.CCheckBox paa_estadE;
    private gnu.chu.controles.CTextField paa_kilosE;
    private gnu.chu.controles.CTextField paa_precioE;
    private gnu.chu.camposdb.proPanel paa_procodE;
    private gnu.chu.controles.CTextField paa_pronomE;
    private gnu.chu.controles.CComboBox paa_tipoE;
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
    private gnu.chu.controles.CLabel pac_tipoL1;
    private gnu.chu.controles.CTextField pac_usuincE;
    private gnu.chu.controles.CTextField pac_usuproE;
    private gnu.chu.controles.CTextField pac_usuresE;
    private gnu.chu.controles.CComboBox pal_accionE;
    private gnu.chu.controles.CComboBox pal_acsalaE;
    private gnu.chu.controles.CComboBox pal_acsalaE1;
    private gnu.chu.controles.CTextField pal_comentE;
    private gnu.chu.controles.CTextField pal_comsalE;
    private gnu.chu.controles.CTextField pal_comsalE1;
    private gnu.chu.controles.CTextField pal_kilosE;
    private gnu.chu.controles.CTextField pal_kilosE1;
    private gnu.chu.controles.CTextField pal_uniaboE;
    private gnu.chu.controles.CTextField pal_unidadE;
    private gnu.chu.controles.CTextField pal_unidadE1;
    private gnu.chu.controles.CTextField par_codiE;
    private gnu.chu.controles.CLabel par_codiL;
    private gnu.chu.controles.CLabel par_codiL1;
    private gnu.chu.controles.CLabel par_codiL2;
    private gnu.chu.controles.CLabel par_codiL3;
    private gnu.chu.controles.CLabel par_comentL;
    private gnu.chu.controles.CLabel par_fecaltL3;
    private gnu.chu.controles.CTextField par_lineaE;
    private gnu.chu.camposdb.proPanel pro_codiE;
    private gnu.chu.camposdb.proPanel pro_codilE;
    private gnu.chu.controles.CTextField pro_ejelotE;
    private gnu.chu.controles.CTextField pro_ejelotE1;
    private gnu.chu.controles.CTextField pro_feccadE;
    private gnu.chu.controles.CTextField pro_feccadE1;
    private gnu.chu.controles.CTextField pro_feccadlE;
    private gnu.chu.controles.CTextField pro_indlotE;
    private gnu.chu.controles.CTextField pro_indlotE1;
    private gnu.chu.controles.CTextField pro_nombE;
    private gnu.chu.controles.CTextField pro_nomblE;
    private gnu.chu.controles.CTextField pro_numlotE;
    private gnu.chu.controles.CTextField pro_numlotE1;
    private gnu.chu.controles.CTextField pro_serlotE;
    private gnu.chu.controles.CTextField pro_serlotE1;
    private gnu.chu.camposdb.prvPanel prv_codiE;
    private gnu.chu.controles.CComboBox tipoE;
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
 public void PADEdit()
 {
        try
        {
            if (P_PERMEST== PERM_GERENC)
            {
                swModificaTodo=false;
                if (jtList.isVacio())
                {
                    msgBox("Elija un parte en la pestaña 'Listado'");
                    PTabPane1.setSelectedIndex(1); 
                    nav.setPulsado(navegador.NINGUNO);
                    activaTodo();
                    return;
                }
            }
            if (P_PERMEST==PERM_INSERTAR )
                swModificaTodo=true;
            if (swModificaTodo)
                PTabPane1.setSelectedIndex(0);
            
            if (pac_usuincE.isNull())
            {
                mensajeErr("Parte no encontrado");
                nav.setPulsado(navegador.NINGUNO);
                activaTodo();
                return;
            }
            if ( P_PERMEST == PERM_INSERTAR && pac_estadE.getValorInt()> ESTADO_PROCDEV)
            {
                msgBox(("Parte no puede ser modficado con sus permisos"));
                nav.setPulsado(navegador.NINGUNO);
                activaTodo();
                return;
            }
            
            if (! setBloqueo(dtAdd,"v_partes", swModificaTodo?par_codiE.getText():jtList.getValString(0)))
            {
                msgBox(msgBloqueo);
                activaTodo();
                return;
            }
         
            activar(navegador.EDIT, true);
            if (swModificaTodo)
            {
//                activaCamposGrid(); 
                if (P_PERMEST== PERM_INSERTAR)
                    pac_tipoE.setEnabled(!pac_tipoE.getValor().equals("R") && pac_estadE.getValorInt()==ESTADO_GENERADA);
            }
            
            if (swModificaTodo)
            {
                mensaje("Editando parte...");
                if (pac_fecproE.isNull())
                    jt.requestFocusInicio();
                else
                {                    
                    jt.setEnabled(false);
                    paa_estadE.requestFocus();
                }
            }
            else
            {
                if (P_PERMEST== PERM_OFICI && estadoE.getValorInt()==ESTADO_PROCESADA)
                {
                    jtLineas.setEnabled(false);
                }
                else
                {
                    BswGrid.setEnabled(true);
                    if (pac_tipoE.getValor().equals("D") && pac_estadE.getValorInt()==ESTADO_GENERADA)
                    {
                        jtAbo.setEnabled(false);
                        setAcciones(pal_accionE, 'D');
                    }
                    else
                        setAcciones(pal_accionE, 'N');
                } 
                mensaje("Editando parte (Lineas)...");
                jtLineas.requestFocusInicio();
            }
            
            resetCambioLote();
        } catch (SQLException | UnknownHostException ex)
        {
            Error("Error al editar registro",ex);
        }     
    }
 
    @Override
    public void ej_edit1() {
        try
        {            
            if (! checkEntrada())
                return;
            
            if (!swModificaTodo)
            {
                if (jtAbo.isEnabled())

                {
                    jtAbo.salirGrid();
                    int nc = cambiaLineaJTAbo(jtAbo.getSelectedRow());
                    if (nc >= 0)
                    {
                        jtAbo.requestFocus(nc);
                        return;
                    }

                }
                Date fecProc = null;
                boolean swAccion = true;
                int nRows = jtLineas.getRowCount();
                for (int n = 0; n < nRows; n++)
                {
                    String accion = jtLineas.getValString(n, JTLINEAS_ACCION);
                    if (accion.startsWith("NO"))
                        swAccion = false;
                }
                if (swAccion)
                {
                    if (pac_estadE.getValorInt() == ESTADO_GENERADA)
                    {

                        if (pac_tipoE.getValor().equals("D"))
                            pac_estadE.setValor(ESTADO_PROCDEV);
                        else
                        {
                            fecProc = Formatear.getDateAct();
                            pac_estadE.setValor(ESTADO_PROCESADA);
                        }
                        dtAdd.select("select * from partecab where par_codi=" + par_codiE.getValorInt(), true);
                        dtAdd.edit();
                        dtAdd.setDato("pac_estad", pac_estadE.getValor());
                        if (fecProc != null)
                        {
                            dtAdd.setDato("pac_usupro", EU.usuario);
                            dtAdd.setDato("pac_fecpro", fecProc);
                        }
                        dtAdd.update();
                        if (!pac_estadE.getValor().equals(estadoE.getValor()))
                        {
                         jtList.removeLinea();
                        }
                    }
                }
                actualizaLineas(par_codiE.getValorInt());
            }
            else
            {
                if (pac_tipoE.getValor().equals("D") && pac_estadE.getValorInt() == ESTADO_PROCDEV)
                {
                    pac_estadE.setValor("" + ESTADO_GENERADA);
                    pac_tipoE.setValor("R");
                }
                if (!pac_estadE.getValor().equals(estadoE.getValor()))
                {
                 jtList.removeLinea();
                }
                dtAdd.select("select * from partecab where par_codi=" + par_codiE.getValorInt(), true);
                dtAdd.edit();
                actualCab();
                if (jt.isEnabled())
                {
                    deleteParteLin();
                    insertaLin(par_codiE.getValorInt());
                }
            }
            resetBloqueo(dtAdd, "v_partes", swModificaTodo ? par_codiE.getText() : jtList.getValString(0), false);
            dtAdd.commit();
            mensaje("");
            nav.pulsado = navegador.NINGUNO;
            boolean sw1 = swModificaTodo;
            activaTodo();
            
            mensajeErr("Parte modificado correctamente");
            if (!sw1)
            {
                verDatos(jtList.getValorInt(JTL_NUMPAR));
                verLineas(jtList.getValorInt(JTL_NUMPAR));
            }
            
            else
                verDatos();
           
//            if (!sw1)
//                cargaListado(estadoE.getValor());
        } catch (ParseException | SQLException  ex)
        {
          Error("Error al editar parte",ex);
        } 
    }

    @Override
    public void canc_edit() {
    
        try {
          resetBloqueo(dtAdd, "v_partes", swModificaTodo?par_codiE.getText():jtList.getValString(0));
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
            if (P_PERMEST==PERM_INSERTAR && pac_tipoE.getValor().equals("R"))
            {
                mensajeErr("Tipo no puede ser Devol. Recepcionada");
                pac_tipoE.requestFocus();
                return;
            }
            int id=insertaCab();
            insertaLin(id);
            dtAdd.commit();
            msgBox("Parte insertado con identificador: "+id);
            boolean swAlta= !BbuscaDoc.isEnabled();
            nav.pulsado=navegador.NINGUNO;
            activaTodo();
            if (swAlta)
            {
                matar();
            }
            else
            {
                strSql="SELECT * FROM partecab where par_codi = "+id;
                rgSelect();
                verDatos();
            }
            
        } catch (ParseException | SQLException ex)
        {
            Error("Error al crear parte",ex);
        }
    }
    private void  actualizaLineas(int id)  throws SQLException,ParseException
    {
        int nRows=jtLineas.getRowCount();

        for (int n=0;n<nRows;n++)
        {
            if (jtLineas.getValorInt(n,JTLINEAS_NUMLIN)==0)
                continue;
            s="select * from partelin where par_codi="+id+" and par_linea="+jtLineas.getValorInt(n,JTLINEAS_NUMLIN);
            if (!dtAdd.select(s,true))
               throw new SQLException("En Posicion: "+n+" Error al buscar linea: "+jtLineas.getValorInt(n,JTLINEAS_NUMLIN)+" de parte: "+id);
            dtAdd.edit();
            dtAdd.setDato("pal_accion",pal_accionE.getValor(jtLineas.getValString(n,JTLINEAS_ACCION)));
            dtAdd.setDato("pal_coment",jtLineas.getValString(n,JTLINEAS_COMENT));

            dtAdd.update();
        }
    }
    private void  insertaLin(int id)  throws SQLException,ParseException
    {
        int nRows=jt.getRowCount();
        int nl=1;
        for (int n=0;n<nRows;n++)
        {
            if (jt.getValorInt(n,JT_PROCODI)==0)
                continue;
            dtAdd.addNew("partelin",false);
            dtAdd.setDato("par_codi",id);
            dtAdd.setDato("par_linea",nl);
            dtAdd.setDato("pro_codi",jt.getValorInt(n,JT_PROCODI));
            dtAdd.setDato("pal_kilos",jt.getValorDec(n,JT_PESO));
            dtAdd.setDato("pal_unidad",jt.getValorDec(n,JT_UNID));
            dtAdd.setDato("pro_ejelot",jt.getValorDec(n,JT_EJERC));
            dtAdd.setDato("pro_serlot",jt.getValString(n,JT_SERIE));
            dtAdd.setDato("pro_numlot",jt.getValorDec(n,JT_LOTE));
            dtAdd.setDato("pro_indlot",jt.getValorDec(n,JT_INDI));
            dtAdd.setDato("pro_feccad",jt.getValDate(n,JT_FECCAD));
            dtAdd.setDato("pal_acsala",pal_acsalaE.getValor(jt.getValString(n,JT_ACSALA)));
            dtAdd.setDato("pal_comsal",jt.getValString(n,JT_COMSAL));
//            if (nav.getPulsado()==navegador.EDIT)
//            {
//                dtAdd.setDato("pal_accion",pal_accionE.getValor(jt1.getValString(n,JT_ACCION)));
//                dtAdd.setDato("pal_coment",jt1.getValString(n,JT_COMENT));                             
//            }
            dtAdd.update();
            nl++;
        }
    }
    private int insertaCab() throws SQLException,ParseException
    {
        dtAdd.addNew("partecab",false);
        dtAdd.setDato("emp_codi",EU.em_cod);
        dtAdd.setDato("pac_usuinc",EU.usuario);
        dtAdd.setDato("pac_fecinc",pac_fecincE.getDate());
        actualCab();
        dtAdd.select("SELECT lastval()");
        return dtAdd.getInt(1);
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
//        if (fecProc!=null)
//        {
//            dtAdd.setDato("pac_usupro",EU.usuario);
//            dtAdd.setDato("pac_fecpro",fecProc);
//        }
//        if (fecRes!=null)
//        {
//            dtAdd.setDato("pac_usures",EU.usuario);
//            dtAdd.setDato("pac_fecres",fecRes);
//        }
        
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
        if (dias>7 && !P_ADMIN)
        {
            mensajeErr("Fecha alta no puede ser inferior a la actual en mas de 7 dias");
            pac_fecaltE.requestFocus();
            return false;
        }
        if ( pac_estadE.getValorInt()>ESTADO_PROCDEV && P_PERMEST== PERM_INSERTAR)
        {
            mensajeErr("No tiene permisos para poner este tipo de estado");
            return false;
        }
        if (pac_tipoE.getValor().equals("D") || pac_tipoE.getValor().equals("N"))
        {
            if (!cli_codiE.controlar())
            {
                mensajeErr("Cliente no es valido");
                return false;
            }
            if (pac_docnumE.getValorInt()>0)
            {
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
        }
        if (pac_tipoE.getValor().equals("E"))
        {
            if (!prv_codiE.controlar())
            {
                mensajeErr("Proveedor no es valido");
                return false;
            }
            if (pac_docnumE.getValorInt()>0)
            {
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
        }
        if (pac_comentE.getText().length()>145)
        {
            mensajeErr("La longitud del comentario no puede exceder los 145 caracteres");
            pac_comentE.requestFocus();
            return false;
        }
        if (swModificaTodo)
        {
            if (jt.isEnabled())
            {
                jt.salirGrid();
                if (cambiaLineajt(jt.getSelectedRow())>0)
                    return false;

                int nRows=jt.getRowCount();
                for (int n=0;n<nRows;n++)
                {
                    if (jt.getValorInt(n,JT_PROCODI)>0)
                        return true;
                }
                mensajeErr("Introduzca alguna linea de Producto");
                return false;
            }
        }
        else
        {
            jtLineas.salirGrid();
        }
        return true;
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
            nav.pulsado=navegador.NINGUNO;
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
        BswGrid.setEnabled(false);
        Bcerrar.setEnabled(!activo && !jtList.isVacio() && P_PERMEST>=PERM_INSERTAR);
        jtLineas.setEnabled(activo &&  tipo == navegador.EDIT && P_PERMEST>=PERM_INSERTAR );
        jtAbo.setEnabled(false);//activo && tipo == navegador.EDIT && P_PERMEST>=PERM_INSERTAR );
        CPermiso.setEnabled(!activo && P_ADMIN );           
        if (tipo==navegador.DELETE  || ! swModificaTodo )
            return;
       
        Pcabe.setEnabled((activo || tipo==navegador.QUERY) && swModificaTodo && pac_fecproE.isNull());
        pac_tipoE.setEnabled(activo);
        pac_docanoE.setEnabled(activo);
        pac_docserE.setEnabled(activo);
        pac_docnumE.setEnabled(activo);
        BbuscaDoc.setEnabled(activo);
        jt.setEnabled(activo && swModificaTodo && tipo != navegador.QUERY);
        jtList.setEnabled(!activo);
        Pcabe.setEnabled(activo);
        pac_comentE.setEnabled(activo);        
        par_codiE.setEnabled(activo && tipo == navegador.QUERY);
        pac_estadE.setEnabled( (activo && tipo==navegador.QUERY) || 
            (activo && P_PERMEST>PERM_INSERTAR) );
   
        activaFechas(tipo != navegador.QUERY || false);
        
    }
    
    void cerrarIncidencia(int estado) throws SQLException
    {
           dtAdd.select("select * from partecab where par_codi="+par_codiE.getValorInt(),true);
           dtAdd.edit();    
           dtAdd.setDato("pac_estad",estado);
           dtAdd.setDato("pac_usures",EU.usuario);
           dtAdd.setDato("pac_fecres",Formatear.getDateAct());
           dtAdd.update(); 
           dtAdd.commit();
           msgBox("Incidencia "+(estado==ESTADO_CERRADO?"Cerrada":"Cancelada"));
    }
//    private void activaCamposGrid()
//    {
//        boolean editable=pac_estadE.getValorInt()<ESTADO_PROCESADA;
//        pro_codiE.setEditable(editable);
//        pal_kilosE.setEditable(editable);
//        pal_unidadE.setEditable(editable);
//        pro_ejelotE.setEditable(editable);
//        pro_serlotE.setEditable(editable);
//        pro_numlotE.setEditable(editable);
//        pro_indlotE.setEditable(editable);
//        pal_acsalaE.setEnabled(editable);
//        pal_comsalE.setEditable(editable);
//      
//        editable=   P_PERMEST>pac_estadE.getValorInt();
//        
//        pal_accionE.setEnabled(editable);
//        pal_comentE.setEditable(editable);
//
//        paa_precioE.setEditable(editable);        
//    }
    
    private void Blistar_ActionPerformed()
    {
     this.setEnabled(false);
     mensaje("A esperar ... estoy generando el listado");
     try {
        s="SELECT p.*,a.pro_nomb,a.pro_codi,p.*,cl.nombre  "+ 
            " FROM v_partes AS p left join v_cliprv as cl on p.pac_cliprv=cl.codigo and tipo!=pac_tipo,"+
             "  anjelica.v_articulo AS a WHERE "+
            " a.pro_codi = p.pro_codi "+
            (feciniE.isNull()?"":" and pac_fecinc >= '"+feciniE.getFechaDB()+"'")+
            (fecfinE.isNull()?"":" and pac_fecinc <= '"+ fecfinE.getFechaDB()+"'")+
            (tipoE.getValor().equals("*")?"":" and pac_tipo='"+tipoE.getValor()+"'")+
            " and pac_estad = "+estadoE.getValor()+
            " order by  p.par_codi,p.par_linea ";
     
       ResultSet rs = dtCon1.getStatement().executeQuery(dtCon1.parseaSql(s));
       java.util.HashMap mp = new java.util.HashMap();
       JasperReport jr;
       mp.put("estadoP",estadoE.getValor());
       mp.put("estadoT",estadoE.getText());
       mp.put("tipoP",tipoE.getValor().equals("*")?null:tipoE.getValor());
       mp.put("tipoT",tipoE.getText());
       
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
            swCargaLin=true;
            jtList.removeAllDatos();
            String s="select * from partecab where pac_estad='"+estado+"'"+
                (tipoE.getValor().equals("*")?"":" and pac_tipo='"+tipoE.getValor()+"'")+
                (feciniE.isNull()?"":" and pac_fecinc>='"+feciniE.getFechaDB()+"'")+
                (fecfinE.isNull()?"":" and pac_fecinc<='"+fecfinE.getFechaDB()+"'")+
                " order by par_codi";
            if (! dtCon1.select(s))
            {                
                swCargaList=false;
                jtLineas.removeAllDatos();
                jtAbo.removeAllDatos();
                Bcerrar.setEnabled(false);
//                if (P_PERMEST==PERM_ADMIN)
//                {
//                    Pcab.resetTexto();
//                    jt.removeAllDatos();
//                }
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
                    s=getNombCliPrv(dtStat,dtCon1.getString("pac_tipo"), dtCon1.getInt("pac_cliprv"));                   
                    v.add(s==null?"**NO ENCONTRADO**":s);
                }
                else
                    v.add("");
                v.add(dtCon1.getString("pac_coment"));
                jtList.addLinea(v);
            } while (dtCon1.next());
            jtList.requestFocusInicio();
            Bcerrar.setEnabled(true);
            swCargaList=false;
            verDatos(jtList.getValorInt(JTL_NUMPAR));
            verLineas(jtList.getValorInt(JTL_NUMPAR));
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
        
        if (!verParteLineas(parCodi,jt,dtCon1,pac_comentE))
        {
            msgBox("No encontradas lineas para este PARTE");
            return;
        }

        jt.requestFocusInicio();
    }
    
    public static boolean verParteLineas(int parCodi,Cgrid jt,DatosTabla dt,CTextArea comentE) throws SQLException
    {
        jt.removeAllDatos();
        if (!dt.select("select l.*,a.pro_nomb from partelin as l,v_articulo as a"+
            " where par_codi="+parCodi+
            " and l.pro_codi = a.pro_codi "+
            " order by par_linea"))
          return false;

        do
        {
            ArrayList v=new ArrayList();
            v.add(dt.getString("pro_codi"));
            v.add(dt.getString("pro_nomb"));
            v.add(dt.getString("pro_ejelot"));
            v.add(dt.getString("pro_serlot"));
            v.add(dt.getString("pro_numlot"));
            v.add(dt.getString("pro_indlot"));
            v.add(dt.getDate("pro_feccad"));
            v.add(dt.getString("pal_unidad"));
            v.add(dt.getString("pal_kilos"));
            v.add(getDescrEstadoSala(dt.getString("pal_acsala")));
            v.add(dt.getString("pal_comsal"));

            jt.addLinea(v);
        } while (dt.next());
        if (comentE!=null)
        {
            dt.select("select pac_coment from partecab "+
            " where par_codi="+parCodi);       
           comentE.setText(dt.getString("pac_coment"));
        }
        return true;
    }
    
    private void verLineas(int parCodi) throws SQLException
    {
        
        swCargaLinAbo=true;
        swCargaLin=true;
        jtLineas.removeAllDatos();
       
        
        if (!dtCon1.select("select l.*,a.pro_nomb from partelin as l,v_articulo as a"+
            " where par_codi="+parCodi+
            " and l.pro_codi = a.pro_codi "+
            " order by par_linea"))
        {
            msgBox("No encontradas lineas para este PARTE");
            jtAbo.removeAllDatos();
            swCargaLinAbo=false;
            swCargaLin=false;
            return;
        }
        do
        {
          
            ArrayList v=new ArrayList();
            
            v.add(dtCon1.getString("pro_codi"));
            v.add(dtCon1.getString("pro_nomb"));           
            v.add(dtCon1.getString("pal_coment"));
            v.add(pal_accionE.getText(dtCon1.getString("pal_accion")));
            v.add(dtCon1.getString("par_linea"));
            jtLineas.addLinea(v);
        } while (dtCon1.next());
        jtLineas.requestFocus(0,0);
        swCargaLinAbo=false;
        verDetalleLinea(parCodi,jtLineas.getValorInt(0, JTLINEAS_NUMLIN));
        swCargaLin=false;
    }
    void verDetalleLinea()
    {        
        try
        {
            if (jtLineas.isVacio() || jtLineas.getSelectedRowDisab()<0)
                return;
            verDetalleLinea(jtList.getValorInt(JTL_NUMPAR) ,jtLineas.getValorInt(jtLineas.getSelectedRowDisab(), JTLINEAS_NUMLIN));
        } catch (SQLException ex)
        {
            Error("Error al ver detalles de linea",ex);
        }
    }
    void verDetalleLinea(int parCodi,int nl) throws SQLException
    {
        if (parCodi==0)
            return;
        parLinea=nl;
        if (!dtStat.select("select * from partelin "+
            " where par_codi="+parCodi+
            " and par_linea="+ nl))
        {
            msgBox("No encontradas detalle de linea: "+nl+" para este PARTE");
            return;
        } 
        pro_ejelotE1.setText(dtStat.getString("pro_ejelot"));
        pro_serlotE1.setText(dtStat.getString("pro_serlot"));
        pro_numlotE1.setText(dtStat.getString("pro_numlot"));
        pro_indlotE1.setText(dtStat.getString("pro_indlot"));
        pal_acsalaE1.setValor(dtStat.getString("pal_acsala"));
        pal_comsalE1.setText(dtStat.getString("pal_comsal"));
        pal_unidadE1.setText(dtStat.getString("pal_unidad"));
        pal_kilosE1.setText(dtStat.getString("pal_kilos"));
       
        verLineasAbono(parCodi,nl);
    }
    /**
     * Devuelve el tipo de Abono
     * @param tipoAbo
     * @return 
     */
    public  static String getDescrTipoAbono(String tipoAbo)
    {                
        for (String[] PAA_TIPO1 : PAA_TIPO)
        {
            if (PAA_TIPO1[1].equals(tipoAbo))
                return PAA_TIPO1[0];
        }
        return "";
    }
    /**
     * Muestra las acciones sobre el parte
     * @param parCodi
     * @param nl Linea
     * @throws SQLException 
     */
    void verLineasAbono(int parCodi,int nl) throws SQLException
    {
        if (swCargaLinAbo)
            return; 
        swCargaLinAbo=true;
        boolean swEnab=jtAbo.isEnabled();
        if (swEnab)
            jtAbo.setEnabled(false);
        
        verLinAbono(parCodi,nl,dtCon1,dtStat,jtAbo);
        if (swEnab)
            jtAbo.setEnabled(true);
        //jtAbo.requestFocus(0,0);
         swCargaLinAbo=false;
        //jtAbo.requestFocus(0,0);
    }
    
    public static boolean verLinAbono(int parCodi,int nl, DatosTabla dt, DatosTabla dt1,Cgrid jt) throws SQLException
    {
       jt.removeAllDatos();
       if (! dt.select("select l.*,a.pro_nomb from parteabo as l"
            + ",v_articulo as a"+
            " where par_codi="+parCodi+
            (nl==-1?"":" and par_linea = "+nl)+
            " and a.pro_codi = l.pro_codi "+
            " order by paa_codi,par_linea"))
            return false; // Sin partes
        String nombre,s;
        do
        {
            ArrayList vc=new ArrayList();
            vc.add(getDescrTipoAbono(dt.getString("paa_tipo"))); // 0 Tipo Abono (C/P)
            vc.add(dt.getString("pro_codi")); //1
            vc.add(dt.getString("pro_nomb")); //2
            vc.add(dt.getString("paa_cliprv")); //3
            if (dt.getString("paa_cliprv").equals("C"))
                s="select cli_nomb as nombre from v_cliente where cli_codi="+dt.getString("paa_cliprv");
            else
                s="select prv_nomb as nombre from v_proveedo where prv_codi="+dt.getString("paa_cliprv");
            if (dt1.select(s))
                nombre=dt1.getString("nombre");
            else
                nombre="Cliente/Prv. NO ENCONTRADO";
            vc.add(nombre); //4
            vc.add(dt.getString("paa_coment")); //5
            vc.add(dt.getDouble("paa_kilos")); //6
            vc.add(dt.getDouble("paa_precio")); //7
            vc.add(dt.getInt("paa_estad")!=0); //8
            vc.add(dt.getInt("paa_codi")); // 9
            jt.addLinea(vc);
        } while (dt.next());
        return true;
    }
    
    public static void confGridArt(Cgrid jt)
    {
        ArrayList vc=new ArrayList();
        vc.add("Articulo"); //0
        vc.add("Nombre"); // 1
        vc.add("Ejerc."); // 2
        vc.add("Serie"); // 3
        vc.add("Lote"); // 4
        vc.add("Ind."); // 5
        vc.add("Fec.Cad"); // 6
        vc.add("Unid"); // 7
        vc.add("Kilos"); // 8
        vc.add("Sug."); // 9
        vc.add("Com.Sala"); // 10
        jt.setCabecera(vc); 
        jt.setAlinearColumna(new int[]{2,0,2,1,2,2,1,2,2,0,0});
        jt.setAnchoColumna(new int[]{70,130,50,30,50,40,60,60,60,60,150});

    }
    
    public static void confGridAbo(Cgrid jt)
    {
        ArrayList vc=new ArrayList();
        vc.add("Tipo"); // 0 Tipo Abono (C/P)
        vc.add("Articulo"); //1
        vc.add("Descrip."); // 2
        vc.add("Cli/Prv"); //3
        vc.add("Nombre"); // 4
        vc.add("Comentario"); // 5
        vc.add("Kilos"); // 6
        vc.add("Precio"); // 7
        vc.add("Proc."); // 8
        vc.add("Id");//9
        jt.setCabecera(vc);
        jt.setAlinearColumna(new int[]{0,2,0,2,0,0,2,2,0,2});
        jt.setAnchoColumna(new int[]{50,60,100,60,100,130,60,60,50,40});
    }


}
