package gnu.chu.anjelica.almacen;
/**
 * <p>Titulo: Consulta de stock actual</p>
 * <p>Descripcion: Permite Consultar el stock actual en camaras
 * </p>
* <p>Copyright: Copyright (c) 2005-2017
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
 * <p>Empresa: micasa</p>
 * @author chuchiP
 * @version 1.0
 */
import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.listados.Listados;
import gnu.chu.anjelica.pad.MantArticulos;
import gnu.chu.anjelica.pad.pdprove;
import gnu.chu.controles.CButton;
import gnu.chu.controles.CLabel;
import gnu.chu.controles.Cgrid;
import gnu.chu.controles.StatusBar;
import gnu.chu.eventos.CambioEvent;
import gnu.chu.eventos.CambioListener;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.miThread;
import gnu.chu.utilidades.ventana;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class CLResumStock extends ventana implements  JRDataSource
{
   Date FECORIGEN;
   final int LISTADO_GENERICO=99;
   boolean swFecCad,swFecSacr,swFecProd;
   JPopupMenu JpopupMenu = new JPopupMenu("Filtro");
   ArrayList vc;
   ArrayList<Integer> ancGrid;
   ArrayList<Integer> alineaGrid ;
   ArrayList<Integer> listaPro=new ArrayList();
   int redondeo;
   int orden;
   char opVerDatos;
   int posX;
   int posY;
   int almCodi;
   int tlaCodi;
   boolean opIncPrv;
   boolean opIncLote;
   Date fechaLimCad;
   private boolean P_VERPRECIO=false;
   Date feulin;
   MvtosAlma mvtosAlm=null;
   String tablaTemp;    
  private boolean VERNEGATIVO=true;
  CButton Blistar = new CButton(Iconos.getImageIcon("print"));
   Date fecSacr  ;
   Date fecProd  ;
  int n,n1,tlaNulipr;
  ArrayList productos = new ArrayList();
  ArrayList grids=new ArrayList();
  int numProd=-1;
  int linGrid=0;
  int maxLinGrid=0;
  Date fecCad;
  PreparedStatement psStock,pst1,pst2;
  boolean swPanel = true;
  DatosTabla dtAux, dtAux1;
  double cantC,cantV;
  double cantTSU,cantTSK,cantTC,cantTV;
  double kilosT;
  int unidT;
  String s;
  
  public CLResumStock(EntornoUsuario eu, Principal p)
  {
      this(eu,p,null);
  }
 public CLResumStock(EntornoUsuario eu, Principal p,Hashtable<String,String> ht)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    setTitulo(" Cons./List. Resumen Stock");

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
    }
  }

  public CLResumStock(gnu.chu.anjelica.menu p, EntornoUsuario eu,Hashtable<String,String> ht)
  {
    EU = eu;
    vl = p.getLayeredPane();
    setTitulo(" Cons./List. Resumen Stock");
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
private void ponParametros(Hashtable<String,String> ht)
  {
      if (ht == null)
        return;
    if (ht.get("verPrecio") != null)
      P_VERPRECIO = Boolean.valueOf(ht.get("verPrecio"));
       
  }
private void jbInit() throws Exception
{
   iniciarFrame();

   this.setVersion("2017-06-24");
   statusBar = new StatusBar(this);
 
   initComponents();
   this.setSize(720,542);
   this.getContentPane().add(statusBar, BorderLayout.SOUTH);

   conecta();
   VERNEGATIVO=EU.getValorParam("verNegResStock",VERNEGATIVO);
    Blistar.setPreferredSize(new Dimension(24, 24));
    Blistar.setMaximumSize(new Dimension(24, 24));
    Blistar.setMinimumSize(new Dimension(24, 24));
    Blistar.setToolTipText("Generar Listado sobre condiciones");
    JpopupMenu.add(opFecCad);
    JpopupMenu.add(opFecSacr);
    JpopupMenu.add(opFecProd);

   statusBar.add(Blistar, new GridBagConstraints(8, 0, 1, 2, 0.0, 0.0,
                                                  GridBagConstraints.EAST,
                                                  GridBagConstraints.VERTICAL,
                                                  new Insets(0, 5, 0, 0), 0, 0));
}
   @Override
  public void iniciarVentana() throws Exception
  { 
     FECORIGEN=Formatear.getDate("01-01-2000", "dd-MM-yyyy");
     feulin=ActualStkPart.getDateUltInv(Formatear.getDateAct(),dtStat);
     jtRes.setEnabled(false);
     tablaTemp="resstock"+ + Formatear.getDateAct().getTime();
     if (!P_VERPRECIO)
     {
         opVerCostos.setSelected(false);
         opVerCostos.setEnabled(false);
     }
     s="SELECT table_name FROM   information_schema.tables   WHERE    table_name = '"+tablaTemp+"'";
      if (dtAdd.select(s))
        dtAdd.executeUpdate("drop table  "+tablaTemp); 
      
       s="create temp table "+tablaTemp+"  ("
          + " orden int, " 
          + " pro_codi int,"
          + " pro_nomb  varchar(50),"
          + " prv_codi int,"
          + " prv_nomb varchar(50),"        
          + " unidades int,"
          + " cajas float,"
          + " cantidad float,"
          + " importe float,"
          + " unPedVen int," 
          + " kgPedVen float,"        
          + " unPedCom int," 
          + " kgPedCom float,"                 
          + " fecsac date,"
          + " fecpro date,"
          + " feccad date)";
        dtAdd.executeUpdate(s);
    dtAux = new DatosTabla(dtCon1.getConexion());
    dtAux1 = new DatosTabla(dtCon1.getConexion());
    Pprinc.setButton(KeyEvent.VK_F4, Baceptar);
   
    s = "SELECT tla_codi,tla_nomb FROM tilialca order by tla_codi";
    dtStat.select(s);      
    tla_codiE.addDatos(dtStat);
    tla_codiE.addDatos(""+LISTADO_GENERICO , "Definido Usuario");
    tla_codiE.setValorInt(LISTADO_GENERICO);
  
    pdalmace.llenaLinkBox(alm_codiE, dtStat,'*');
//    s = "SELECT alm_codi,alm_nomb FROM v_almacen " +
//        " ORDER BY alm_codi";
//    dtStat.select(s);
//    alm_codiE.addDatos(dtStat);
 
    gnu.chu.anjelica.pad.pdconfig.llenaDiscr(dtStat,cam_codiE,"AC",EU.em_cod);


    proiniE.iniciar(dtStat, this, vl, EU);
    profinE.iniciar(dtStat, this, vl, EU);

    fecStockE.setDate(Formatear.getDateAct());
    sbe_codiE.iniciar(dtStat, this, vl, EU);
    sbe_codiE.setTipo("A");
    sbe_codiE.setValorInt(0);
    sbe_codiE.setAceptaNulo(true);
     tla_codiE.resetCambio();
    ponTipListado();
    activarEventos();
  }
  void activarEventos()
  {
  
     BFechas.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JpopupMenu.show(BFechas,0,24);
      }
    });
    Baceptar.addActionListener(new ActionListener()
    {
            @Override
      public void actionPerformed(ActionEvent e)
      {
        Baceptar_actionPerformed();
      }
    });

    Blistar.addActionListener(new ActionListener()
    {
            @Override
      public void actionPerformed(ActionEvent e)
      {
        Blistar_actionPerformed();
      }
    });
    tla_codiE.addCambioListener(new CambioListener()
    {
         @Override
         public void cambio(CambioEvent event) {
               ponTipListado();
         }
     });
   
    jtRes.tableView.getSelectionModel().addListSelectionListener(new
        ListSelectionListener()
    {
            @Override
      public void valueChanged(ListSelectionEvent e)
      {

        if (e.getValueIsAdjusting()) // && e.getFirstIndex() == e.getLastIndex())
          return;
        if (!jtRes.isEnabled() || jtRes.isVacio())
            return;
        verDatosDet(jtRes.getValorInt(0));
      }
    });
  }
    
  void verDatosDet(int proCodi)
  {
       try
       {
           s="select * from "+tablaTemp+" where pro_codi ="+proCodi+
               " order by feccad";
           dtAdd.select(s);
           jtDet.removeAllDatos();
           do
           {
               ArrayList v = new ArrayList();
               if (opIncPrvE.isSelected())
               {
                   if (dtAdd.getObject("prv_nomb")==null)
                       v.add(dtAdd.getString("prv_codi"));
                   else
                       v.add(dtAdd.getObject("prv_nomb")); // Proveedor
               }
               else
                   v.add(dtAdd.getString("prv_codi")); 
               if (MantArticulos.isEncajado(proCodi, dtStat))
                v.add(Formatear.redondea(dtAdd.getDouble("cantidad") / 
                       MantArticulos.getPesoCaja( dtStat,proCodi),0));
               else
                v.add(dtAdd.getInt("unidades")) ;
                   
               v.add(dtAdd.getDouble("cantidad"));
               v.add(dtAdd.getFecha("feccad","dd-MM-yy"));               
               v.add(dtAdd.getFecha("fecpro","dd-MM-yy"));
               v.add(dtAdd.getFecha("fecsac","dd-MM-yy"));
               jtDet.addLinea(v);
           } while (dtAdd.next());
       } catch (SQLException ex)
       {
           Error("Error al ver Detalle",ex);
       }
  }
  void ponTipListado()
  {
    try
    {
      if (dtStat.getConexion().isClosed())
          return;
      s = "select * from tilialca where tla_codi = " + tla_codiE.getValorInt();
      if (!dtStat.select(s))
        return;
      tla_nuliprE.setValorInt(dtStat.getInt("tla_nulipr"));
      tla_diagfeE.setValorInt(dtStat.getInt("tla_diagfe"));
      tla_vekgcaE.setValor(dtStat.getString("tla_vekgca"));
      if (dtStat.getObject("cam_codi")!=null)
        cam_codiE.setText(dtStat.getString("cam_codi"));  
    }
    catch (Exception k)
    {
      Error("Poner Tipo de Listado", k);
    }
  }

  void Baceptar_actionPerformed()
  {
    if (! checkCond()) return;
    
    new miThread("xx")
    {
      @Override
      public void run()
      {
        msgEspere("Espere ... Buscando Datos");
        jtRes.tableView.setVisible(false);
        jtDet.tableView.setVisible(false);
        jtRes.setEnabled(false);
        consulta();       
        resetMsgEspere();
        jtRes.setEnabled(true);
        jtRes.tableView.setVisible(true);
        if (! jtRes.isVacio())
            verDatosDet(jtRes.getValorInt(0));
        jtDet.tableView.setVisible(true);
      }
    };
  }

  void consulta() 
  {  
    
    Pgrids.removeAll();
    P_VERPRECIO=opVerCostos.isSelected();
    jtRes.removeAllDatos();
    swFecCad=opFecCad.isSelected();
    swFecSacr=opFecSacr.isSelected();
    swFecProd=opFecProd.isSelected();
    tlaCodi = tla_codiE.getValorInt();
//    int sbeCodi = sbe_codiE.getValorInt();
    almCodi = alm_codiE.getValorInt();
    maxLinGrid=0;
    try
    {
      if (mvtosAlm==null && P_VERPRECIO)
      {
        mvtosAlm = new MvtosAlma();
        mvtosAlm.setUsaDocumentos(false);
        mvtosAlm.setIncUltFechaInv(false);
        mvtosAlm.setIgnDespSinValor(true);
        mvtosAlm.setEntornoUsuario(EU);
      
        mvtosAlm.setSoloInventario(false);
        mvtosAlm.setIncluyeSerieX(false);
        mvtosAlm.iniciarMvtos(feulin,Formatear.getDateAct(),dtCon1);
      }
      
      if (tlaCodi != LISTADO_GENERICO && !cam_codiE.isNull() )
      { // Busco los diferentes Articulos en stock en la camara indicada
         s="select distinct(pro_codi) as pro_codi from v_stkpart where cam_codi = '"+cam_codiE.getText()+"'"+
             " and stp_unact >0 "+
             " order by pro_codi";
         listaPro.clear();
         if (dtStat.select(s))
         {
             do
             {
                 listaPro.add(dtStat.getInt("pro_codi"));
             } while (dtStat.next());
         }         
      }
      s = getSqlCab(tlaCodi,sbe_codiE.getValorInt());
      if (!dtCon1.select(s))
      {
        mensajeErr("NO encontrados datos para estos criterios");
        mensaje("");
        return;
      }
      dtAdd.executeUpdate("delete from "+tablaTemp);
      posX = 0;
      posY = 0;      
      redondeo = tla_diagfeE.getValorInt();
      
      opVerDatos=tla_vekgcaE.getValor().charAt(0);
      java.util.Date dt;
      opIncPrv=opIncPrvE.isSelected();
      opIncLote=opIncLoteE.isSelected();
      productos.clear();
      grids.clear();
      
      
      vc= new ArrayList();
      alineaGrid = new ArrayList();
      ancGrid= new ArrayList();
      if (opIncPrv)
      {
        vc.add("Proveed");
        ancGrid.add(95);
        alineaGrid.add(0);       
      }
      else
      {
        if (opIncLote)
        {
            vc.add("Lote");
            ancGrid.add(55);
            alineaGrid.add(2);
        }
      }
      if (swFecCad)
      {
        vc.add("F.Cad.");
        ancGrid.add(55);
        alineaGrid.add(1);
      }
      if (swFecProd )
      {
        vc.add("F.Pro.");
        ancGrid.add(55);
        alineaGrid.add(1);
      }
      
      if (swFecSacr )
      {
        vc.add("F.Sac.");
        ancGrid.add(55);
        alineaGrid.add(1);
      }
              
      
      if (opVerDatos=='U' || opVerDatos=='A')
      {
        vc.add("Unid");
        ancGrid.add(35);
        alineaGrid.add(2);
      }
      if (opVerDatos=='K' || opVerDatos=='A')
      {
        vc.add("Kilos");
        ancGrid.add(45);
        alineaGrid.add(2);
      }
      
      if (!incPedidosC.getValor().equals("N"))
      {
        vc.add("Stock");
        vc.add("Comp");
        vc.add("Vent");
        ancGrid.add(45);
        ancGrid.add(45);
        ancGrid.add(45);        
        alineaGrid.add(2);
        alineaGrid.add(2);
        alineaGrid.add(2); 
      }
    
      s="SELECT sum(stp_unact) as unidades,SUM(stp_kilact) as cantidad " +
            (opIncPrv?", prv_codi ":"")+
            (opIncLote?", pro_nupar":"")+
            (swFecCad?", stp_feccad as feccad ":"") +
            (swFecSacr?", stp_fecsac as fecsac ":"") +
            (swFecProd?", stp_fecpro as fecpro ":"") +
            " FROM v_stkpart where pro_codi = ?" +
            (VERNEGATIVO?
                "  and (stp_kilact > 0.49 or stp_kilact < -0.49)"+
                (opVerDatos=='K'?"":" and stp_unact != 0 "):
                "   and stp_kilact > 0.49 "+
                (opVerDatos=='K'?"":" and stp_unact >0 ")) +
            (cam_codiE.isNull()?"":" and cam_codi = '"+cam_codiE.getText()+"'")+
            " and eje_nume > 0 " +
            (almCodi == 0 ? "" : " and alm_codi = " + almCodi)+
            " group by "+
            (opIncPrv?" prv_codi,":"")+
            (opIncLote?" pro_nupar,":"")+   
            (swFecCad?"feccad,":"") +
            (swFecSacr?"fecsac,":"") +
            (swFecProd?"fecpro,":"");
      s=s.substring(0,s.length()-1);
      if (incPedidosC.getValor().equals("A") || incPedidosC.getValor().equals("C"))
      {
        s+=" union all "+
           " select sum(acp_canind)*-1 as unidades,sum(acp_canti)*-1 as cantidad,  "+
            (opIncPrv?" c.prv_codi,":"")+
            " 0 as pro_nupar"+
            (swFecCad?", acp_feccad as feccad ":"") +
            (swFecSacr?", acp_feccad as fecsac ":"") +
            (swFecProd?", acp_feccad as fecpro ":"") +            
            " from v_compras as c"+          
            " where pro_codi = ?"+            
            " AND c.acc_cerra = 0 "+ // No estan cerradas las lineas
            (almCodi == 0 ? "" : " and alm_codi = " + almCodi)+
            " and exists ( select   emp_codi "+
            " from pedicoc as p " +
            " where p.emp_codi = c.emp_codi "+
            " and p.acc_ano = c.acc_ano "+
            " and p.acc_serie = c.acc_serie "+
            " and p.acc_nume = c.acc_nume) "+
            " group by "+
            (opIncPrv?" c.prv_codi":"")+
            (swFecCad?",feccad":"") +
            (swFecSacr?",fecsac":"") +
            (swFecProd?",fecpro":"")+
            " UNION ALL " +
// Albaranes Ventas sin CERRAR Y con pedidos
            " select sum(avp_numuni) as unidades,sum(avp_canti) as cantidad, " +
            (opIncPrv?" s.prv_codi,":"")+
            "0 as pro_nupar"+
            (swFecCad?", s.stp_feccad as feccad ":"") +
            (swFecSacr?",s.stp_fecsac as fecsac ":"") +
            (swFecProd?",s.stp_fepro as fecpro ":"") +    
            " from v_albventa_detalle as a,v_stkpart as s " +
            " WHERE  s.eje_nume = a.avp_ejelot " +
            " and s.emp_codi = a.avp_emplot " +
            " and s.pro_serie = a.avp_serlot " +
            " and s.pro_nupar = a.avp_numpar " +
            " and s.pro_codi = a.pro_codi " +
            " and s.pro_numind = a.avp_numind " +
            " AND avc_cerra = 0 " + // Abierto y con pedido
            (almCodi == 0 ? "" : " and s.alm_codi = " + almCodi) +
            " and a.pro_codi = ? "+           
            " and  exists ( select emp_codi from pedvenc as p " +
            " where p.emp_codi = a.emp_codi " +
            " and p.avc_ano = a.avc_ano " +
            " and p.avc_serie = a.avc_serie " +
            " and p.avc_nume = a.avc_nume) " +
            " group by "+
            (opIncPrv?" s.prv_codi,":"")+
            (swFecCad?",feccad":"") +
            (swFecSacr?",fecsac":"") +
            (swFecProd?",fecpro":"");
      } // Fin de incluir pedidos
      s+=" order by ";
      int nCampo=3+(opIncPrv?1:0)+
            (opIncLote?1:0);          
      if (swFecCad)
      {
          s+=nCampo+",";
          nCampo++;
      }
      if (swFecSacr)
      {
          s+=nCampo+",";
          nCampo++;
      }
      if (swFecProd)
      {
          s+=nCampo+",";
      }
      if (opIncPrv || opIncLote)
          s+="3";
      else
        s=s.substring(0,s.length()-1);
                
//      debug("s: "+s);
      psStock = ct.prepareStatement(s);

      s = // Pedidos de Compras Pendientes de confirmar
          " SELECT sum(pcl_nucape) as unidCompr, sum(pcl_cantpe) as cantCompr, " +
          " 0 as unidVent, 0 as cantVent, " +
          (opIncPrv?" prv_codi,":"")+
          "pcl_feccad as feccad " +
          " FROM v_pedico  " +        
          " where pro_codi = ?" +
          (almCodi == 0 ? "" : " and alm_codi = " + almCodi) +
          " and pcc_estad = 'P' " +
          " AND pcc_estrec = 'P' "+
          " and pcc_fecrec <=  TO_DATE('" + fecStockE.getText() + "','dd-MM-yyyy')" +
          " group by "+
          (opIncPrv?" s.prv_codi,":"")
          + "pcl_feccad " +
          " UNION ALL " +
          // Pedidos Compras Confirmados
          " SELECT sum(pcl_nucaco) as unidCompr, sum(pcl_cantco) as cantCompr, " +
          " 0 as unidVent, 0 as cantVent, " +
          (opIncPrv?" prv_codi,":"")+
          " pcl_feccad as feccad " +
          " FROM v_pedico " +       
          " where pro_codi = ? " +
          (almCodi == 0 ? "" : " and alm_codi = " + almCodi) +
          " and pcc_estad = 'C' " +
          " AND pcc_estrec = 'P' "+
          " and pcc_fecrec <=  TO_DATE('" + fecStockE.getText() + "','dd-MM-yyyy')" +
          " group by "+
          (opIncPrv?" prv_codi,":"")+
           "pcl_feccad " +
          " UNION ALL " +
          // Pedidos Compras Pre-Factura
          " SELECT sum(pcl_nucafa) as unidCompr, sum(pcl_cantfa) as cantCompr, " +
          " 0 as unidVent, 0 as cantVent, " +
          (opIncPrv?" prv_codi,":"")+
          " pcl_feccad as feccad " +
          " FROM v_pedico " +          
          " where pro_codi = ?" +
          (almCodi == 0 ? "" : " and alm_codi = " + almCodi) +
          " and pcc_estad = 'F' " +
          " AND pcc_estrec = 'P' "+
          " and pcc_fecrec <=  TO_DATE('" + fecStockE.getText() + "','dd-MM-yyyy')" +
          " group by "+
          (opIncPrv?" prv_codi,":"")+
          "pcl_feccad " +
          " UNION ALL " +
          // Pedidos Ventas Pendientes de preparar albaran
          "SELECT  0 as unidCompr,0 as cantCompr, " +
          " sum(pvl_unid) as  unidVent, sum(pvl_kilos) as cantVent, " +
          (opIncPrv?" prv_codi,":"")+
          " pvl_feccad as feccad " +
          "  FROM v_pedven " +
          " where (avc_ano = 0 or pvc_cerra = 0) " + // Sin Albaran o Albaran sin CERRAR
          " and pvc_confir = 'S' "+
          (almCodi == 0 ? "" : " and alm_codi = " + almCodi) +
          " and pro_codi = ?" +
          " AND pvc_fecent <= TO_DATE('" + fecStockE.getText() + "','dd-MM-yyyy')" +
          " group by "+
          (opIncPrv?" prv_codi,":"")+
          "pvl_feccad "+
          " order by "+(opIncPrv?5:4); // Ordenador por fecha caduc.
      pst2 = ct.prepareCall(dtAux.parseaSql(s));
      preparaPst1();
     
       fechaLimCad= Formatear.sumaDiasDate(Formatear.getDateAct(),diasCaducE.getValorInt());
     
      /*
      // Bucle inicial
      */
      orden=1;
      do
      {
        trataArticulo();        
    }   while (dtCon1.next());
    if (tlaCodi!=LISTADO_GENERICO && !cam_codiE.isNull())
    {// Busco referencia no incluido en tipo listado pero que estan en camara
         tlaCodi=LISTADO_GENERICO;
         preparaPst1();
         s="select  pro_codi,pro_nomcor as pro_desc,pro_cosinc from v_articulo where pro_tiplot = 'V' AND  pro_codi in (";
         Iterator<Integer> it= listaPro.iterator();       
         while (it.hasNext())
         {
             s+=it.next()+",";
         }
         s=s.substring(0,s.length()-1)+") order by pro_codi";
         if (dtCon1.select(s))
         {
            do
            {
                trataArticulo();        
            }   while (dtCon1.next());  
        }
    }
      this.repaint();
//       this.pack();
      mensaje("");
      mensajeErr("Consulta ... REALIZADA");
    }
    catch (SQLException | NumberFormatException | ParseException k)
    {
      Error("Error al Buscar datos", k);
    }

  }
  void preparaPst1() throws SQLException
  {
      String s;
       if (tlaCodi == 99)
      {
        s = "SELECT sum(stp_unact) as unidades " +
           " FROM v_stkpart where  pro_codi = ? " +
           " and stp_kilact != 0 " +
           " and eje_nume > 0 " + // Para no incluir Acumulados
           (cam_codiE.isNull()?"":" and cam_codi = '"+cam_codiE.getText()+"'")+
           (almCodi == 0 ? "" : " and alm_codi = " + almCodi);
        pst1 = ct.prepareStatement(s);
      }
      else
      {
          s = "SELECT * FROM tilialpr where tla_codi = " + tlaCodi +
              " and tla_orden = ?";
          pst1 = ct.prepareStatement(s);
      }
  }
  /**
   * Trata articulo venta o de grupo despiece
   * @throws SQLException 
   */
  void trataArticulo() throws SQLException,ParseException
  {
       int refCodi;
       int proCodi;
       mensaje("Tratando producto: " + dtCon1.getString("pro_desc"), false);
        if (tlaCodi == LISTADO_GENERICO)
        {
          refCodi=dtCon1.getInt("pro_codi");
          pst1.setInt(1,refCodi);          
          dtAux.setResultSet(pst1.executeQuery());
          if (! dtAux.next())
            return;
          if (dtAux.getDouble("unidades", true) <= 0)
            return;
        }
        else
        {
          refCodi= dtCon1.getInt("tla_orden");
          pst1.setInt(1, refCodi);
          dtAux.setResultSet(pst1.executeQuery());
          if (!dtAux.next())
            return;
        }

        
        boolean incArticulo=false;    
       
        do
        { // Para cada producto busca el stock
          proCodi=tlaCodi == LISTADO_GENERICO ? dtCon1.getInt("pro_codi") : dtAux.getInt("pro_codi");
          if (calcDatos(proCodi,refCodi,orden))
              incArticulo=true;
          if (tlaCodi!=LISTADO_GENERICO)
              listaPro.remove((Object) proCodi);

        } while (dtAux.next());
        
        if (! incArticulo  && tlaCodi!=LISTADO_GENERICO)
        {
            dtAdd.addNew(tablaTemp);
            dtAdd.setDato("pro_codi",refCodi);
            dtAdd.setDato("pro_nomb",dtCon1.getString("pro_desc")); 
            dtAdd.update();
        }
        orden++;

      
      /**
       * Ahora muestro los datos en pantalla
       */
      cantTSU=0;
      cantTSK=0;
       
        s="select * from "+tablaTemp+ " where pro_codi = "+refCodi+
            (opCaducE.isSelected()?" and feccad <= '"+Formatear.getFechaDB(fechaLimCad)+"'":"")+
            " order by orden,feccad";
        double importeT=0;
        double kgCad=0;
        if (dtAdd.select(s))
        {
            Cgrid jt = new Cgrid( ancGrid.size());
            CLabel pro_descL = new CLabel();
            confCamposTemp(jt,pro_descL);
            
            do 
            {                    
              ArrayList v = new ArrayList();
              if (dtAdd.getDouble("cantidad",true) > corteKilosE.getValorDec())
              {
                if (opIncPrv)
                {
                  if (dtAdd.getObject("prv_nomb")==null)
                    v.add(dtAdd.getString("prv_codi"));
                  else
                    v.add(dtAdd.getObject("prv_nomb")); // Proveedor          
                }         
                else
                {
                    if (opIncLote)
                        v.add(dtAdd.getString("prv_codi"));
                }
                if (Formatear.comparaFechas(dtAdd.getDate("feccad"), fechaLimCad)<0)
                  kgCad+=dtAdd.getDouble("cantidad");
                if (swFecCad)
                    v.add(dtAdd.getFecha("feccad","dd-MM-yy"));
                if (swFecProd)
                    v.add(dtAdd.getFecha("fecpro","dd-MM-yy"));
                if (swFecSacr)
                    v.add(dtAdd.getFecha("fecsac","dd-MM-yy"));

      //          cantS=result.getKilos()+result.getKilosCompra()-result.getKilosVenta();
      //          kilosT+=cantS;
      //          cantS=result.getUnidades()+result.getUnidadesCompra()-result.getUnidadesVenta();
      //          unidT+=cantS;
                int cajas=dtAdd.getInt("unidades");
                if (MantArticulos.isEncajado(proCodi,dtStat))
                {
                    cajas=(int) Formatear.redondea(dtAdd.getDouble("cantidad") / 
                        MantArticulos.getPesoCaja( dtStat,proCodi),0);
                }
                cantTSU+=cajas;
                cantTSK+=dtAdd.getDouble("cantidad");
                importeT+=dtAdd.getDouble("importe");
                if (opVerDatos=='U' || opVerDatos=='A')
                  v.add(cajas);            
                if (opVerDatos=='K' || opVerDatos=='A')
                  v.add(dtAdd.getDouble("cantidad"));
      //          if (incPedidosC.getValor().equals("A"))
      //          {            
      //            if (opVerDatos=='U')
      //            {
      //               v.add(result.getUnidades());
      //               v.add(result.getUnidadesCompra());
      //               v.add(result.getUnidadesVenta());              
      //               cantTC+=result.getUnidadesCompra();
      //               cantTV+=result.getUnidadesVenta();
      //            }     
      //             if (opVerDatos=='K' )
      //            {
      //               v.add(result.getKilos());
      //               v.add(result.getKilosCompra());
      //               v.add(result.getKilosVenta());
      //               
      //               cantTC+=result.getKilosCompra();
      //               cantTV+=result.getKilosVenta();
      //
      //            }    
      //          }     
                jt.addLinea(v);
              }
              
            } while (dtAdd.next());
            if (maxLinGrid<jt.getRowCount())
              maxLinGrid=jt.getRowCount();
            ArrayList vt = new ArrayList();
            vt.add("TOTAL");
            for (int nf=3;nf<jt.getColumnCount();nf++)
                vt.add("");
            if (opVerDatos!='A')
                vt.add("");           
            if (opVerDatos=='U' || opVerDatos=='A')
                vt.add(cantTSU); // Unidades Totales
            if (opVerDatos=='K' || opVerDatos=='A')
                vt.add(cantTSK); // Kilos  Totales
            
    //        if (incPedidosC.getValor().equals("A"))
    //        {
    //          v.add( cantTSK);
    //          v.add( cantTC);
    //          v.add( cantTV);
    //        }
            jt.addLinea(vt);
            // Añado la linea del grid de totales.
            ArrayList vr=new ArrayList();
            vr.add(refCodi);
            vr.add(dtCon1.getString("pro_desc"));
            vr.add(cantTSU); // Unidades Totales
            vr.add(cantTSK); // Kilos  Totales
            vr.add(kgCad==0?"":kgCad);
            if (P_VERPRECIO)
            {
                vr.add(Formatear.redondea(cantTSK>0?importeT/cantTSK:0,2));
                jt.valorAux=Formatear.redondea(cantTSK>0?importeT/cantTSK:0,2);
            }
            else
                vr.add("");
            jtRes.addLinea(vr);
            
            productos.add(pro_descL.getText());
            grids.add(jt);
            Pgrids.add(pro_descL, new GridBagConstraints(posX, posY, 1, 1, 1.0, 1.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

            Pgrids.add(jt, new GridBagConstraints(posX, posY + 1, 1, 1, 1.0, 1.0
                                                  , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                                  new Insets(0, 0, 0, 0), 0, 0));
            posX++;
            if (posX > 2)
            {
              posX = 0;
              posY += 2;
            }
          
    }
  }
  
  /**
   * 
   * @param proCodi
   * @throws SQLException 
   */
  boolean calcDatos(int proCodi,int refCodi,int orden) throws SQLException,ParseException
  {
          psStock.setInt(1,  proCodi);
          if (incPedidosC.getValor().equals("A"))
          {
            psStock.setInt(2, proCodi);
            psStock.setInt(3, proCodi);
          }
         
          dtAux1.setResultSet(psStock.executeQuery());
          if (dtAux1.next())
          { // Busco stock agrupandolo fecha cad. y Prv (si corresponde)            
            
            fecCad=swFecCad?dtAux1.getDate("feccad"):FECORIGEN;
            fecSacr=swFecSacr?dtAux1.getDate("fecsac"):FECORIGEN;
            fecProd=swFecProd?dtAux1.getDate("fecpro"):FECORIGEN;
            do
            {
              fecCad=redondeaFecha(redondeo, swFecCad?dtAux1.getDate("feccad"):FECORIGEN,fecCad);
              fecSacr=redondeaFecha(redondeo, swFecSacr?dtAux1.getDate("fecsac"):FECORIGEN,fecSacr);
              fecProd=redondeaFecha(redondeo, swFecProd?dtAux1.getDate("fecpro"):FECORIGEN,fecProd);             
//               debug("prv: "+dtAux1.getString("prv_codi")+" fecCad: "+
//                     Formatear.formatearFecha(new java.util.Date(fecCad),"dd-MM-yyyy")+" dt: "+dtAux1.getFecha("feccad","dd-MM-yyyy"));
              int prvCodi=opIncPrv?dtAux1.getInt("prv_codi"):
                  opIncLote?dtAux1.getInt("pro_nupar"):0;
              s="select * from "+tablaTemp+" where pro_codi ="+refCodi+
                  " and prv_codi =" +prvCodi+
                  (swFecCad? " and feccad='"+Formatear.getFechaDB(fecCad)+"'":"")+
                  (swFecSacr? " and fecsac='"+Formatear.getFechaDB(fecSacr)+"'":"")+
                  (swFecProd? " and fecpro='"+Formatear.getFechaDB(fecProd)+"'":"");
              if (!dtAdd.select(s,true))
              {                  
                dtAdd.addNew(tablaTemp);
                dtAdd.setDato("orden",orden);
                dtAdd.setDato("pro_codi",refCodi);
                dtAdd.setDato("pro_nomb",dtCon1.getString("pro_desc"));
                dtAdd.setDato("prv_codi",prvCodi);
                dtAdd.setDato("prv_nomb",pdprove.getNombPrv(prvCodi, dtStat));
                dtAdd.setDato("feccad",fecCad);
                dtAdd.setDato("fecsac",fecSacr);
                dtAdd.setDato("fecpro",fecProd);
                dtAdd.setDato("cantidad",dtAux1.getDouble("cantidad",true));
                dtAdd.setDato("unidades",dtAux1.getInt("unidades",true));    
                if (P_VERPRECIO)
                {                    
                    if ( mvtosAlm.calculaMvtos(proCodi, dtAux, dtStat, null,null))
                        dtAdd.setDato("importe",dtAux1.getDouble("cantidad",true)*(mvtosAlm.getPrecioStock()+
                            MantArticulos.getCostoIncrementar(dtStat, proCodi)) );    
                    else
                      dtAdd.setDato("importe",0 );    
                }
                else
                    dtAdd.setDato("importe",0);    
              }
              else
              {  
                dtAdd.edit();
                dtAdd.setDato("cantidad",dtAdd.getDouble("cantidad",true)+dtAux1.getDouble("cantidad",true));
                dtAdd.setDato("unidades",dtAdd.getInt("unidades",true)+dtAux1.getInt("unidades",true));  
                if (P_VERPRECIO)
                {                    
                    if ( mvtosAlm.calculaMvtos(proCodi, dtAux, dtStat, null,null))
                        dtAdd.setDato("importe",dtAdd.getDouble("importe",true)+
                            (dtAux1.getDouble("cantidad",true)*(mvtosAlm.getPrecioStock()+
                            MantArticulos.getCostoIncrementar(dtStat, proCodi))) );
                }
              }
              dtAdd.update();
            }  while (dtAux1.next());
            /**
            if (incPedidosC.getValor().equals("A"))
            {
              pst2.setInt(1,proCodi);
              pst2.setInt(2,proCodi);
              pst2.setInt(3,proCodi);
              pst2.setInt(4,proCodi);
              dtAux1.setResultSet(pst2.executeQuery());
              if (!dtAux1.next())
                continue;
              do
              {
              
                DatProducto dtProd=new DatProducto(proCodi,opIncPrv?dtAux1.getInt("prv_codi"):
                    opIncLote?dtAux1.getInt("pro_nupar"):0,null);
              
                int pos;
                if (  (pos =hm.indexOf(dtProd)) == -1)
                {
                    
//                cantC+=verKilos ? dtAux1.getDouble("cantCompr") : dtAux1.getDouble("unidCompr");
//                cantV+= verKilos? dtAux1.getDouble("cantVent") : dtAux1.getDouble("unidVent");
                hm.add(dtProd);
                }
       
              } while (dtAux1.next())
            }
            */
            return true;
          }
          return false;
  }
  
  void confCamposTemp(Cgrid jt,CLabel pro_descL ) throws SQLException
  {
        jt.setCabecera(vc);
        jt.setAnchoColumna(ancGrid );
        jt.setAlinearColumna(alineaGrid);
        String formCanti="--,--9";
        int nCol=jt.getColumnCount()-2 ;
        if (opVerDatos=='U' || opVerDatos=='A')
            jt.setFormatoColumna(nCol++, formCanti);
        if (opVerDatos=='K' || opVerDatos=='A')
        {
              formCanti="-,--9.9";
              jt.setFormatoColumna(nCol, formCanti);
        }
        
        
        if (incPedidosC.getValor().equals("A"))
        {
          jt.setFormatoColumna(nCol++, formCanti);
          jt.setFormatoColumna(nCol++,formCanti);
          jt.setFormatoColumna(nCol++, formCanti);
        }
       
        pro_descL.setText((tlaCodi == LISTADO_GENERICO?dtCon1.getInt("pro_codi")+" -> ":"")+ dtCon1.getString("pro_desc"));
        pro_descL.setBackground(Color.orange);
        pro_descL.setMaximumSize(new Dimension(176, 17));
        pro_descL.setMinimumSize(new Dimension(176, 17));
        pro_descL.setPreferredSize(new Dimension(176, 17));
        pro_descL.setOpaque(true);
        pro_descL.setHorizontalAlignment(SwingConstants.CENTER);
        jt.setMaximumSize(new Dimension(236, 128));
        jt.setMinimumSize(new Dimension(236, 128));
        jt.setPreferredSize(new Dimension(236, 128));
        jt.setBuscarVisible(false);
        
}
  private Date redondeaFecha(int redondeo, Date fecha,Date ultFecha) throws ParseException
  {
      if (fecha==null)
          fecha=Formatear.getDate("01-01-2000", "dd-MM-yyyy");
      if (ultFecha==null)
          ultFecha=Formatear.getDate("01-01-2000", "dd-MM-yyyy");
      int dias=Math.abs((int)Formatear.comparaFechas(fecha, ultFecha));
      if (dias>redondeo)
          return fecha;      
      return ultFecha;
  }
  void Blistar_actionPerformed()
  {
    if (! checkCond()) return;

    new miThread("jjj")
    {
            @Override
      public void run()
      {
        listar();
      }
    };
  }
  boolean checkCond()
  {
      try
      {
          if (tla_codiE.isNull())
              tla_codiE.setValorInt(99);
          if (incPedidosC.getValor().equals("A") && tla_vekgcaE.getValor().equals("A"))
          {
              mensajeErr("No se puede incluir Pedidos si se desea sacar Unid. y Kilos");
              tla_vekgcaE.requestFocus();
              return false;
          }
          if (fecStockE.isNull())
          {
              mensajeErr("Introduzca Fecha de Stock");
              fecStockE.requestFocus();
              return false;
          }
          if (!sbe_codiE.controla())
          {
              mensajeErr("Seccion no valida");
              return false;
          }          
      } catch (SQLException ex)
      {
          Error("Error al comprobar seccion de articulo",ex);
          return false;
      }
      return true; 
  }
  void listar()
  {
    mensaje("Espere .... generando Listado");
    this.setEnabled(false);
    try
    {
      if (tla_nuliprE.getValorInt()>0)
        tlaNulipr=tla_nuliprE.getValorInt();
      else
        tlaNulipr=maxLinGrid;
      String report=incPedidosC.getValor().equals("A")?"resstock_d":"resstock";
      JasperReport jr=Listados.getJasperReport(EU,  report);
      numProd=-1;
      linGrid=999;
      java.util.HashMap mp = new java.util.HashMap();
      mp.put("tla_nombP",tla_codiE.getTextCombo());
      mp.put("tla_diagfeP",tla_diagfeE.getText());
      mp.put("tla_vekgcaP",tla_vekgcaE.getText());
      mp.put("alm_nombP",alm_codiE.getTextCombo());

      JasperPrint jp = JasperFillManager.fillReport(jr, mp, this);
     
      if (!gnu.chu.print.util.printJasper(jp, EU)) {
            mensaje("");
            this.setEnabled(true);
            return;
      }
    
      mensaje("");
      mensajeErr("Listado ... GENERADO");
    }
    catch (PrinterException | JRException k)
    {
      Error("Error al imprimir", k);
    }
    this.setEnabled(true);
  }

  @Override
  public boolean next() throws JRException
  {

    if (linGrid+1 >= tlaNulipr)
    {
      numProd++;
      if (numProd == productos.size())
        return false;
      linGrid = -1;
    }
    linGrid++;
    return true;
  }

    @Override
  public Object getFieldValue(JRField jRField) throws JRException
  {
    String campo = jRField.getName();
   
    try
    {
      if (campo.equals("pro_nomcor"))
        return productos.get(numProd).toString();
      Cgrid grid=( (Cgrid) grids.get(numProd));
      if (linGrid+1 >= grid.getRowCount())
        return null;
      int nCol=grid.getColumnCount()-1;
//      if (campo.equals("stp_kilact"))
//        nCol=tla_vekgcaE.getValor().equals("A")?3:2;
      if (campo.equals("prv_nomb"))
          return   opIncPrv? grid.getValString(linGrid, "Proveed"):"";
      if (campo.equals("stp_feccad"))
          return swFecCad?Formatear.getDate(grid.getValString(linGrid, "F.Cad."), "dd-MM-yy"):"";

      if (campo.equals("stp_fecsac"))
         return swFecSacr?Formatear.getDate(grid.getValString(linGrid, "F.Sac."), "dd-MM-yy"):"";
      if (campo.equals("stp_cajact"))
          return opVerDatos=='A'|| opVerDatos=='U'?grid.getValorInt(linGrid, "Unid"):0;
      if (campo.equals("stp_kilact"))
          return opVerDatos=='A'|| opVerDatos=='K'?grid.getValorDec(linGrid, "Kilos"):0;
      if (campo.equals("stp_preact"))
          return opVerCostos.isSelected()?grid.valorAux:null;
//      
//      if (campo.equals("stp_kilstk"))
//        nCol=3;
//      if (campo.equals("stp_kilcom"))
//        nCol=4;
//      if (campo.equals("stp_kilven"))
//        nCol=5;
    

      
//      }
//      else
//      {
//        if (campo.equals("prv_nomb"))
//            return "***";
//        if (campo.equals("stp_feccad"))
//          return null;
//        if (nCol==2)
//        {
//            int cajas=0;
//            for (int n=linGrid;n+1<grid.getRowCount();n++)
//                 cajas+=grid.getValorInt(n, nCol);     
//             return cajas;
//        }
//         if (nCol>0)
//         {
//           double kilos=0;
//           for (int n=linGrid;n+1<grid.getRowCount();n++)
//             kilos+=grid.getValorDec(n, nCol);                       
//           return kilos;
//         }
//      }


      throw new JRException("Campo: "+campo+ " No definido");
    }
    catch (ParseException | JRException k)
    {
      throw new JRException(k);
    }
  }

  /**
   * Devuelve string para buscar todos los productos selecionados
   * @param tlaCodi
   * @param sbeCodi
   * @return 
   */

  String getSqlCab(int tlaCodi, int sbeCodi)
  {
    if (tlaCodi == LISTADO_GENERICO)
    {        
        String condArt = " 1 = 1 "+
          (!proiniE.isNull() ? "  and pro_codi "+(profinE.isNull()?"":">") +
            "= " + proiniE.getValorInt() : "") +
            (!profinE.isNull() ? "  and pro_codi <= " + profinE.getValorInt() :
             "")+
           " and pro_tiplot = 'V'";
        if (sbeCodi!=0)
          condArt+=" and sbe_codi ="+sbeCodi;
        s = "SELECT pro_codi,pro_nomcor as pro_desc,pro_cosinc from v_articulo where " +
            condArt +
            " order by pro_codi";
    }
    else
        s = "SELECT * FROM tilialgr WHERE tla_codi = " + tlaCodi +
            " order by tla_orden";

    return s;
  }

//  boolean buscaGrupo(int tlaCodi,int proCodi,int almCodi) throws SQLException
//  {
//    if (tlaCodi == 99)
//    {
//      s = "SELECT sum(stp_unact) as unidades " +
//          " FROM v_stkpart where pro_codi = " + proCodi +
//          " and stp_kilact != 0 " +
//          " and eje_nume > 0 " +
//          (almCodi == 0 ? "" : " and alm_codi = " + almCodi);
//      dtAux.select(s);
//      if (dtAux.getDouble("unidades", true) <= 0)
//        return false;
//    }
//    else
//    {
//      s = "SELECT * FROM tilialpr where tla_codi = " + tlaCodi +
//          " and tla_orden = " + dtCon1.getInt("tla_orden");
//
//      if (!dtAux.select(s))
//        return false;
//    }
//    return true;
//  }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        opFecSacr = new javax.swing.JCheckBoxMenuItem();
        opFecCad = new javax.swing.JCheckBoxMenuItem();
        opFecProd = new javax.swing.JCheckBoxMenuItem();
        Pprinc = new gnu.chu.controles.CPanel();
        Pcabe = new gnu.chu.controles.CPanel();
        cLabel1 = new gnu.chu.controles.CLabel();
        tla_codiE = new gnu.chu.controles.CLinkBox();
        cLabel2 = new gnu.chu.controles.CLabel();
        tla_vekgcaE = new gnu.chu.controles.CComboBox();
        cLabel3 = new gnu.chu.controles.CLabel();
        tla_nuliprE = new gnu.chu.controles.CTextField(Types.DECIMAL, "#9");
        cLabel4 = new gnu.chu.controles.CLabel();
        alm_codiE = new gnu.chu.controles.CLinkBox();
        cLabel5 = new gnu.chu.controles.CLabel();
        tla_diagfeE = new gnu.chu.controles.CTextField(Types.DECIMAL, "#9");
        cLabel6 = new gnu.chu.controles.CLabel();
        fecStockE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        opIncPrvE = new gnu.chu.controles.CCheckBox();
        opIncLoteE = new gnu.chu.controles.CCheckBox();
        cLabel7 = new gnu.chu.controles.CLabel();
        incPedidosC = new gnu.chu.controles.CComboBox();
        cLabel8 = new gnu.chu.controles.CLabel();
        cam_codiE = new gnu.chu.controles.CLinkBox();
        proiniE = new gnu.chu.camposdb.proPanel();
        cLabel10 = new gnu.chu.controles.CLabel();
        profinE = new gnu.chu.camposdb.proPanel();
        Baceptar = new gnu.chu.controles.CButton("F4 Aceptar", Iconos.getImageIcon("check"));
        cLabel11 = new gnu.chu.controles.CLabel();
        sbe_codiE = new gnu.chu.camposdb.sbePanel();
        cLabel12 = new gnu.chu.controles.CLabel();
        diasCaducE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#9");
        opCaducE = new gnu.chu.controles.CCheckBox();
        opVerCostos = new gnu.chu.controles.CCheckBox();
        cLabel13 = new gnu.chu.controles.CLabel();
        corteKilosE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#9");
        cLabel14 = new gnu.chu.controles.CLabel();
        BFechas = new gnu.chu.controles.CButton();
        PTab1 = new gnu.chu.controles.CTabbedPane();
        Presum = new gnu.chu.controles.CPanel();
        jtRes = new gnu.chu.controles.Cgrid(6);
        jtDet = new gnu.chu.controles.Cgrid(6);
        PScroll = new javax.swing.JScrollPane();
        Pgrids = new gnu.chu.controles.CPanel();

        opFecSacr.setMnemonic('V');
        opFecSacr.setText("Sacrificio");

        opFecCad.setMnemonic('V');
        opFecCad.setSelected(true);
        opFecCad.setText("Caducidad");

        opFecProd.setMnemonic('V');
        opFecProd.setText("Producion");

        Pprinc.setLayout(new java.awt.GridBagLayout());

        Pcabe.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        Pcabe.setMaximumSize(new java.awt.Dimension(739, 89));
        Pcabe.setMinimumSize(new java.awt.Dimension(739, 89));
        Pcabe.setPreferredSize(new java.awt.Dimension(739, 89));
        Pcabe.setLayout(null);

        cLabel1.setText("Inc. Pedidos");
        Pcabe.add(cLabel1);
        cLabel1.setBounds(340, 20, 72, 17);

        tla_codiE.setAncTexto(25);
        tla_codiE.setFormato(true);
        tla_codiE.setFormato(Types.DECIMAL, "#9", 2);
        tla_codiE.setPreferredSize(new java.awt.Dimension(250, 17));
        Pcabe.add(tla_codiE);
        tla_codiE.setBounds(80, 1, 160, 17);

        cLabel2.setText("Tipo Listado");
        Pcabe.add(cLabel2);
        cLabel2.setBounds(0, 1, 80, 17);

        tla_vekgcaE.addItem("Ambos", "A");
        tla_vekgcaE.addItem("Kilos", "K");
        tla_vekgcaE.addItem("Unid", "U");
        Pcabe.add(tla_vekgcaE);
        tla_vekgcaE.setBounds(280, 1, 70, 17);

        cLabel3.setText("Ver");
        Pcabe.add(cLabel3);
        cLabel3.setBounds(250, 1, 30, 20);

        tla_nuliprE.setText("0");
        Pcabe.add(tla_nuliprE);
        tla_nuliprE.setBounds(420, 1, 20, 17);

        cLabel4.setText("De Producto ");
        Pcabe.add(cLabel4);
        cLabel4.setBounds(10, 40, 75, 17);

        alm_codiE.setAncTexto(25);
        alm_codiE.setFormato(Types.DECIMAL, "#9", 2);
        alm_codiE.setPreferredSize(new java.awt.Dimension(250, 17));
        alm_codiE.setText("1");
        Pcabe.add(alm_codiE);
        alm_codiE.setBounds(510, 1, 200, 17);

        cLabel5.setText("LIn.Prod");
        Pcabe.add(cLabel5);
        cLabel5.setBounds(370, 1, 50, 17);

        tla_diagfeE.setText("3");
        tla_diagfeE.setToolTipText("Introducir Numero de Dias por los que agrupar");
        Pcabe.add(tla_diagfeE);
        tla_diagfeE.setBounds(270, 60, 20, 17);

        cLabel6.setText("Dias Caduc.");
        Pcabe.add(cLabel6);
        cLabel6.setBounds(10, 60, 70, 17);
        Pcabe.add(fecStockE);
        fecStockE.setBounds(70, 20, 70, 17);

        opIncPrvE.setSelected(true);
        opIncPrvE.setText("Inc. Prov.");
        Pcabe.add(opIncPrvE);
        opIncPrvE.setBounds(250, 20, 80, 17);

        opIncLoteE.setSelected(true);
        opIncLoteE.setText("Inc. Lote");
        Pcabe.add(opIncLoteE);
        opIncLoteE.setBounds(380, 60, 70, 17);

        cLabel7.setText("Camara");
        Pcabe.add(cLabel7);
        cLabel7.setBounds(480, 20, 50, 17);

        incPedidosC.addItem("No","N");
        incPedidosC.addItem("Ventas","V");
        incPedidosC.addItem("Compras","C");
        incPedidosC.addItem("Ambos","A");
        incPedidosC.setPreferredSize(new java.awt.Dimension(100, 20));
        incPedidosC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                incPedidosCActionPerformed(evt);
            }
        });
        Pcabe.add(incPedidosC);
        incPedidosC.setBounds(420, 20, 50, 17);

        cLabel8.setText("Almacen");
        Pcabe.add(cLabel8);
        cLabel8.setBounds(450, 1, 50, 17);

        cam_codiE.setAncTexto(25);
        cam_codiE.setFormato(Types.CHAR,"X",2);
        cam_codiE.setMayusculas(true);
        cam_codiE.setMayusculas(true);
        cam_codiE.setPreferredSize(new java.awt.Dimension(250, 17));
        Pcabe.add(cam_codiE);
        cam_codiE.setBounds(540, 20, 170, 17);
        Pcabe.add(proiniE);
        proiniE.setBounds(90, 40, 240, 17);

        cLabel10.setText("A");
        Pcabe.add(cLabel10);
        cLabel10.setBounds(350, 40, 20, 17);
        Pcabe.add(profinE);
        profinE.setBounds(370, 40, 240, 17);
        Pcabe.add(Baceptar);
        Baceptar.setBounds(610, 60, 100, 22);

        cLabel11.setText("Agr. Dias");
        Pcabe.add(cLabel11);
        cLabel11.setBounds(210, 60, 60, 17);
        Pcabe.add(sbe_codiE);
        sbe_codiE.setBounds(670, 40, 40, 18);

        cLabel12.setText("Fec.Stock ");
        Pcabe.add(cLabel12);
        cLabel12.setBounds(10, 20, 60, 17);

        diasCaducE.setText("10");
        Pcabe.add(diasCaducE);
        diasCaducE.setBounds(80, 60, 20, 17);

        opCaducE.setText("Ver solo Cad.");
        Pcabe.add(opCaducE);
        opCaducE.setBounds(110, 60, 100, 17);

        opVerCostos.setSelected(true);
        opVerCostos.setText("Ver Costos");
        Pcabe.add(opVerCostos);
        opVerCostos.setBounds(290, 60, 90, 17);

        cLabel13.setText("Seccion");
        Pcabe.add(cLabel13);
        cLabel13.setBounds(620, 40, 50, 17);

        corteKilosE.setText("1");
        corteKilosE.setToolTipText("");
        Pcabe.add(corteKilosE);
        corteKilosE.setBounds(210, 20, 30, 17);

        cLabel14.setText("Ign. Kilos");
        Pcabe.add(cLabel14);
        cLabel14.setBounds(150, 20, 60, 17);

        BFechas.setText("Fechas");
        Pcabe.add(BFechas);
        BFechas.setBounds(510, 60, 70, 19);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        Pprinc.add(Pcabe, gridBagConstraints);

        PTab1.setPreferredSize(new java.awt.Dimension(739, 69));

        Presum.setLayout(new java.awt.GridBagLayout());

        ArrayList v=new ArrayList();
        v.add("Articulo");//0
        v.add("Nombre");//1
        v.add("Unid.");//2
        v.add("Kilos");//3
        v.add("Kg.Cad");//4
        v.add("Costo");//5
        jtRes.setCabecera(v);
        jtRes.setAnchoColumna(new int[]{60,250,50,50,70,50});
        jtRes.setAlinearColumna(new int[]{2,0,2,2,2,2});
        jtRes.setFormatoColumna(2,"---,--9");
        jtRes.setFormatoColumna(3,"----,--9.9");
        jtRes.setFormatoColumna(4,"----,--9");
        jtRes.setFormatoColumna(5,"##9.99");
        jtRes.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtRes.setMaximumSize(new java.awt.Dimension(100, 100));
        jtRes.setMinimumSize(new java.awt.Dimension(100, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Presum.add(jtRes, gridBagConstraints);

        ArrayList vd=new ArrayList();
        vd.add("Prv");
        vd.add("Unid");
        vd.add("Kilos");
        vd.add("Fec.Cad.");
        vd.add("Fec.Pro.");
        vd.add("Fec.Sac.");
        jtDet.setCabecera(vd);
        jtDet.setAnchoColumna(new int[]{150,40,60,70,70,70});
        jtDet.setAlinearColumna(new int[]{0,2,2,1,1,1});
        jtDet.setFormatoColumna(1,"---,--9");
        jtDet.setFormatoColumna(2,"----,--9.9");
        jtDet.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtDet.setMaximumSize(new java.awt.Dimension(100, 50));
        jtDet.setMinimumSize(new java.awt.Dimension(100, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Presum.add(jtDet, gridBagConstraints);

        PTab1.addTab("Resumen", Presum);

        Pgrids.setLayout(new java.awt.GridBagLayout());
        PScroll.setViewportView(Pgrids);

        PTab1.addTab("Extendido", PScroll);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Pprinc.add(PTab1, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void incPedidosCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_incPedidosCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_incPedidosCActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton BFechas;
    private gnu.chu.controles.CButton Baceptar;
    private javax.swing.JScrollPane PScroll;
    private gnu.chu.controles.CTabbedPane PTab1;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Pgrids;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CPanel Presum;
    private gnu.chu.controles.CLinkBox alm_codiE;
    private gnu.chu.controles.CLabel cLabel1;
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
    private gnu.chu.controles.CLinkBox cam_codiE;
    private gnu.chu.controles.CTextField corteKilosE;
    private gnu.chu.controles.CTextField diasCaducE;
    private gnu.chu.controles.CTextField fecStockE;
    private gnu.chu.controles.CComboBox incPedidosC;
    private gnu.chu.controles.Cgrid jtDet;
    private gnu.chu.controles.Cgrid jtRes;
    private gnu.chu.controles.CCheckBox opCaducE;
    private javax.swing.JCheckBoxMenuItem opFecCad;
    private javax.swing.JCheckBoxMenuItem opFecProd;
    private javax.swing.JCheckBoxMenuItem opFecSacr;
    private gnu.chu.controles.CCheckBox opIncLoteE;
    private gnu.chu.controles.CCheckBox opIncPrvE;
    private gnu.chu.controles.CCheckBox opVerCostos;
    private gnu.chu.camposdb.proPanel profinE;
    private gnu.chu.camposdb.proPanel proiniE;
    private gnu.chu.camposdb.sbePanel sbe_codiE;
    private gnu.chu.controles.CLinkBox tla_codiE;
    private gnu.chu.controles.CTextField tla_diagfeE;
    private gnu.chu.controles.CTextField tla_nuliprE;
    private gnu.chu.controles.CComboBox tla_vekgcaE;
    // End of variables declaration//GEN-END:variables
}


