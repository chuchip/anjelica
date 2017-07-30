package gnu.chu.anjelica.ventas;
/*
 *<p>Titulo: MantTransPedidos </p>
 * <p>Descripción: Mantenimiento Transportes para los Pedidos de Venta.
 * Created on 27-02-2017
 *
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
 */



import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.pad.MantRepres;
import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.anjelica.pad.pdtransp;
import gnu.chu.camposdb.proPanel;
import gnu.chu.camposdb.prvPanel;
import gnu.chu.controles.CCheckBox;
import gnu.chu.controles.CTextField;
import gnu.chu.controles.StatusBar;
import gnu.chu.eventos.GridAdapter;
import gnu.chu.eventos.GridEvent;
import gnu.chu.interfaces.PAD;
import gnu.chu.interfaces.ejecutable;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.navegador;
import gnu.chu.utilidades.ventana;
import gnu.chu.utilidades.ventanaPad;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MantTransPedidos extends  ventanaPad implements PAD  
{
    boolean DEBUG=true;
    PreparedStatement psTra;
    boolean inCambio=true;
    int traCodiAnt;
    String fecTranAnt;
    java.util.Date fechaReparto;
    boolean swCliente=false;
    int nLineaReport;
    boolean swImpreso=true;
    proPanel pro_codiE= new proPanel();
    prvPanel prv_codiE = new prvPanel();
    int empCodiS,ejeNumeS,pvcNumeS,cliCodiS;
    ventana padre=null;
    String s;
    boolean verPrecio;
    String ARG_REPCODI = "";
    String ARG_SBECODI = "";
    private final int JTCAB_ORDEN=0;    
    private final int JTCAB_TRACOD=1;
    private final int JTCAB_TRANOM=2;
    private final int JTCAB_KILOS=3;    
    private final int JTCAB_TIPO=4;    
    private final int JTCAB_FECHA=5;    
    private final int JTCAB_HORA=6;    
    private final int JTCAB_MINUT=7;  
    private final int JTCAB_EMPPED=8;
    private final int JTCAB_EJEPED=9;
    private final int JTCAB_NUMPED=10;
    private final int JTCAB_NOMCLI=12;
    private final int JTCAB_EJEALB=19;
    private final int JTCAB_SERALB=20;
    private final int JTCAB_NUMALB= 21;   
    
    
//    private final int JTCAB_POBCLI=5;
    
    public MantTransPedidos(EntornoUsuario eu, Principal p) {
        this(eu, p, null);
    }

    public MantTransPedidos(EntornoUsuario eu, Principal p, HashMap<String,String> ht) {
        EU = eu;
        vl = p.panel1;
        jf = p;
        eje = true;

        try {
            if (ht != null) {
                 if (ht.get("repCodi") != null) 
                    ARG_REPCODI = ht.get("repCodi");
                
                if (ht.get("sbeCodi") != null)
                    ARG_SBECODI = ht.get("sbeCodi");
              
            }
            setTitulo("Mant. Transportes Pedidos Ventas");
            if (jf.gestor.apuntar(this)) {
                jbInit();
            } else {
                setErrorInit(true);
            }
        } catch (Exception e) {
           ErrorInit(e);
        }
    }

    public MantTransPedidos(gnu.chu.anjelica.menu p, EntornoUsuario eu, HashMap <String,String> ht) {
        EU = eu;
        vl = p.getLayeredPane();
        eje = false;

        try {
            if (ht != null) {
                if (ht.get("repCodi") != null) 
                    ARG_REPCODI = ht.get("repCodi");
                
                if (ht.get("sbeCodi") != null)
                    ARG_SBECODI = ht.get("sbeCodi");
            }
             setTitulo("Mant. Transportes Pedidos Ventas");

            jbInit();
        } catch (Exception e) {
           ErrorInit(e);
        }
    }
    public MantTransPedidos(ventana papa) throws Exception
    {
        padre=papa;
        dtStat=padre.dtStat;
        dtCon1=padre.dtCon1;
        vl=padre.vl;
        jf=padre.jf;

        EU=padre.EU;
         setTitulo("Mant. Transportes Pedidos Ventas");

        jbInit();
    }
    private void jbInit() throws Exception {
        statusBar = new StatusBar(this);

        iniciarFrame();

        this.setVersion("2017-07-23");

        initComponents();
        this.setSize(new Dimension(730, 535));
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
   statusBar = new StatusBar(this);
        nav = new navegador(this, dtCons, false, navegador.NORMAL);
        
        iniciarFrame();
        this.setVersion("2017-97-25 ");
        
        strSql = "SELECT * FROM cabtrapedven "+
            " order by trp_fecha,rut_codi";

        this.getContentPane().add(nav, BorderLayout.NORTH);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        this.setPad(this);
        conecta();
       
        initComponents();
        iniciarBotones(Baceptar, Bcancelar);
     
        navActivarAll();
        conecta();
    }

    @Override
    public void iniciarVentana() throws Exception 
    {
     tra_codbusE.setFormato(Types.DECIMAL, "####");
     tra_codbusE.setCeroIsNull(true);
     Pcabe.setDefButton(Baceptar);
     pvc_feciniE.setAceptaNulo(false);
     pvc_fecfinE.setAceptaNulo(false);
    
     pdconfig.llenaDiscr(dtStat, rut_codiE, pdconfig.D_RUTAS, EU.em_cod);
     String s="select tra_codi,tra_nomb from transportista where tra_tipo='V' ORDER BY tra_nomb";
     dtCon1.select(s);
     tra_codbusE.addDatos(dtCon1);
     trp_fecbusE.setDate(Formatear.getDateAct());
     rut_codiE.setCeroIsNull(true);
   
   
     pvc_feciniE.setText(Formatear.sumaDias(Formatear.getDateAct(), -7));
     pvc_fecfinE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
      activarEventos();
    }
    void activarEventos()
    {
 

        Baceptar.addActionListener(new ActionListener()
        {
                @Override
          public void actionPerformed(ActionEvent e)
          {
            Baceptar_actionPerformed();
          }
        });
     jtCab.addGridListener(new GridAdapter()
     {
         @Override
         public void cambioColumna(GridEvent event)   { 
             if (event.getColumna()==JTCAB_TRACOD)
             {
                 try
                 {
                     String traNomb=pdtransp.getNombreTransportista(dtStat,tra_codiE.getValorInt(),"V");
                     if (traNomb==null)
                         jtCabPed.setValor("**NO EXISTE**",event.getLinea(),JTCAB_TRANOM);
                     else
                         jtCabPed.setValor(traNomb,event.getLinea(),JTCAB_TRANOM);
                 } catch (SQLException ex)
                 {
                    Error("Error al buscar transportista",ex);
                 }
             }
         }
         @Override
          public void cambiaLinea(GridEvent event){
              int nCol=guardaDatos(event.getLinea());
              event.setColError(nCol);
          }
           @Override
          public void afterCambiaLinea(GridEvent event){
//              if (tit_usunomE.getText().equals(""))
//              {
//                tit_usunomE.setText(traCodiAnt);
//                jtCabPed.setValor(traCodiAnt,JTCAB_USUA);
//              }
              verDatPed(jtCabPed.getValorInt(JTCAB_EMPPED),
                jtCabPed.getValorInt(JTCAB_EJEPED),jtCabPed.getValorInt(JTCAB_NUMPED));
//              actTotalGrid();
//              verUsuPedido(event.getLinea());
          }
     });
     
     
  
    jtCabPed.addMouseListener(new MouseAdapter()
    {
      @Override
      public void mouseClicked(MouseEvent e)
      {
        if (e.getClickCount() < 2)
          return;
        if (jtCabPed.isVacio())
          return;
        if (padre!=null)
        {
          empCodiS=jtCabPed.getValorInt(0);
          ejeNumeS=jtCabPed.getValorInt(1);
          pvcNumeS=jtCabPed.getValorInt(2);
          cliCodiS=jtCabPed.getValorInt(3);
          matar();
        }
        else
        { 
             if (jtCabPed.getValorInt(JTCAB_EJEALB)==0)
             {
                irPedido();
                return;
             }
             if (jtCabPed.getSelectedColumn()<=JTCAB_NUMPED)
                irPedido();
             else
                irAlbaran();
          
        }
      }

    });    
    }
    void irAlbaran()
    {         
        ejecutable prog;
        if ((prog=jf.gestor.getProceso(pdalbara.getNombreClase()))==null)
               return;
       pdalbara cm=(pdalbara) prog;
       if (cm.inTransation())
       {
          msgBox("Mantenimiento Albaranes de Ventas ocupado. No se puede realizar la busqueda");
          return;
       }
       cm.PADQuery();

       cm.setSerieAlbaran(jtCabPed.getValString(JTCAB_SERALB));
       cm.setNumeroAlbaran(jtCabPed.getValString(JTCAB_NUMALB));
       cm.setEjercAlbaran(jtCabPed.getValorInt(JTCAB_EJEALB));

       cm.ej_query();
       jf.gestor.ir(cm);
    }
    void irPedido() {
        ejecutable prog;
        if ((prog = jf.gestor.getProceso(pdpeve.getNombreClase())) == null)
            return;
        pdpeve cm = (pdpeve) prog;
        if (cm.inTransation())
        {
            msgBox("Mantenimiento Pedidos de Ventas ocupado. No se puede realizar el Alta");
            return;
        }
        cm.PADQuery();
        cm.setEjercicioPedido(jtCabPed.getValorInt(JTCAB_EJEPED));
        cm.setNumeroPedido(jtCabPed.getValorInt(JTCAB_NUMPED));
       

        cm.ej_query();
        jf.gestor.ir(cm);
    } 
    public String getCliNomb()
    {
        return jtCabPed.getValString(JTCAB_NOMCLI);
    }
    /**
     * Devuelve la ruta que tiene asignado un pedido.
     * @return
     * @throws SQLException 
     */
    public String getRuta() throws SQLException
    {
        return pdpeve.getRuta(dtCon1, empCodiS, ejeNumeS, pvcNumeS);
    }
    
    
    int guardaDatos(int linea)
    {
        if (inCambio )
            return -1;
        try
        {
            if (jtCabPed.isVacio() || !jtCabPed.isEnabled() )
                return -1;
            if ( tra_codiE.isNull())
                return -1;
            if (! tra_codiE.isNull() && jtCabPed.getValString(linea,JTCAB_TRANOM).startsWith("**"))
            {
                mensajeErr("Codigo Transportista No valido");
                return JTCAB_TRACOD; // Transportista no valido           
            }
          
            int pvcId=pdpeve.getIdPedido(dtStat,jtCabPed.getValorInt(linea,JTCAB_EMPPED),
                jtCabPed.getValorInt(linea,JTCAB_EJEPED),jtCabPed.getValorInt(linea,JTCAB_NUMPED));
            if ( pvcId<0)
                throw new SQLException("Error al Buscar pedido "+jtCabPed.getValorInt(linea,JTCAB_EMPPED)+
                    "-"+jtCabPed.getValorInt(linea,JTCAB_EJEPED)+"-"+jtCabPed.getValorInt(linea,JTCAB_NUMPED));
//            if (tra_codiE.isNull() && trp_kilosE.getValorInt()>0)
//            {
//                jtCabPed.setValor(traCodiAnt,linea,JTCAB_TRACOD);
//                tra_codiE.setText(fecTranAnt);            
//            }
//            if (tra_codiE.getValorInt()==0)
//                return -1;
            if ((trp_fechaE.isNull() || trp_fechaE.getError()) && trp_kilosE.getValorInt()>0)
            {
                jtCabPed.setValor(fecTranAnt,linea,JTCAB_FECHA);
                trp_fechaE.setText(fecTranAnt);               
            }
          
            traCodiAnt=tra_codiE.getValorInt();
            fecTranAnt=trp_fechaE.getText();
            
            s="Select * from transpedidos where trp_id="+pvcId+
                " and tra_codi='"+tra_codiE.getValorInt()+"'"+
                " and trp_tipo = '"+trp_tipoE.getText()+"'"+
                " and trp_fecha = '"+Formatear.getFechaDB(fechaReparto)+"'";  
            if (!dtAdd.select(s,true))
            {
                dtAdd.addNew();
                dtAdd.setDato("tra_codi",tra_codiE.getValorInt());
                dtAdd.setDato("trp_fecha",trp_fechaE.getDate());
                dtAdd.setDato("trp_tipo",trp_tipoE.getText().equals("C")?"C":"E");
                dtAdd.setDato("trp_id",pvcId);
            }
            else
                dtAdd.edit();
            if (!trp_fechaE.isNull())
            {
                java.util.Date fecReparto=Formatear.getDate(trp_fechaE.getText()+" "+trp_horaE.getText()+":"+trp_minutoE.getText()
                    , "dd-MM-yy HH:mm");
                dtAdd.setDato("trp_fecent",fecReparto);
            }
            else
                dtAdd.setDato("trp_fecent",(java.sql.Timestamp) null);
            dtAdd.setDato("trp_orden",trp_ordenE.getValorInt());
            dtAdd.setDato("trp_kilos",trp_kilosE.getValorDec());
            dtAdd.update();
            dtAdd.commit();
            mensajeRapido("Guardado Registo");
            
        } catch (SQLException | ParseException ex)
        {
            Error("Error al actualizar transporte de pedido",ex);
        }
        return -1;
    }
    /**
     * Muestra los kilos de pedido
     * @param empCodi
     * @param ejeNume
     * @param pvcNume 
     */
    void verDatPed(int empCodi,int ejeNume,int pvcNume)
   {
     try
     {
       s="SELECT p.*,cl.cli_pobl FROM v_pedven as p,v_cliente as cl "+
           " WHERE p.emp_codi =  "+empCodi+
           " and p.cli_codi = cl.cli_codi "+ 
           " AND p.eje_nume = "+ejeNume+
           " and p.pvc_nume = "+pvcNume+
           " order by p.pvl_numlin ";
       jtLinPed.removeAllDatos();
       Ppie.resetTexto();
       nPedT.setValorDec(jtCabPed.getRowCount());
       if (! dtCon1.select(s))
       {
         msgBox("NO ENCONTRADOS DATOS PARA ESTE PEDIDO");
         return;
       }
     

       
       
       double kilosColgado=0;
       double kilosEncajado=0;
       do
       {
         ArrayList v=new ArrayList();
         v.add("P");
         v.add(dtCon1.getString("pro_codi"));
         v.add(pro_codiE.getNombArt(dtCon1.getInt("pro_codi")));
         v.add(dtCon1.getString("prv_codi"));
         v.add(prv_codiE.getNombPrv(dtCon1.getString("prv_codi"),dtStat));
         v.add(dtCon1.getFecha("pvl_feccad"));
         v.add(dtCon1.getString("pvl_canti")+" "+dtCon1.getString("pvl_tipo"));
         v.add(dtCon1.getString("pvl_precio"));
         v.add(dtCon1.getInt("pvl_precon") != 0);
         v.add(dtCon1.getString("pvl_comen"));
         v.add(dtCon1.getString("pvl_numlin"));
         jtLinPed.addLinea(v);
         if (pro_codiE.getLikeProd().getInt("pro_encaja")==0)
         {// Genero colgado
             kilosColgado+=dtCon1.getDouble("pvl_kilos");
         }
         else
             kilosEncajado+=dtCon1.getDouble("pvl_kilos");
       } while (dtCon1.next());
       if (jtCabPed.getValorInt(JTCAB_EJEALB)!=0)
       {
           verDatAlbaranPed(empCodi,jtCabPed.getValorInt(JTCAB_EJEALB),jtCabPed.getValString(JTCAB_SERALB),
               jtCabPed.getValorInt(JTCAB_NUMALB) );
       }
       kilosCajasE.setValorDec(kilosEncajado);
       kilosColgadoE.setValorDec(kilosColgado);
       kilosPedidE.setValorDec(kilosEncajado+kilosColgado);
       
     } catch (Exception k)
     {
       Error("Error al Ver datos de pedido",k);
     }
   }
//   void verUsuPedido(int row)
//   {    
//      jtUsuPed.removeAllDatos(); 
//      try {
//        ResultSet rs;
//    
//        int pvcId = pdpeve.getIdPedido(dtStat, jtCabPed.getValorInt(row, JTCAB_EMPPED),
//                    jtCabPed.getValorInt(row, JTCAB_EJEPED),
//                    jtCabPed.getValorInt(row, JTCAB_NUMPED));
//         if (pvcId > 0)
//         {
//             s="select tp.*,tra_nomb from transpedidos tp left join transportista  as tr on tr.tra_tipo='V' "
//               + " and tr.tra_codi = tp.tra_codi where trp_id= "+pvcId+
//                   " and trp_fecha = '"+ Formatear.getFecha(fechaReparto ,"yyyyMMdd")+"'" ;
//             if (! dtStat.select(s))
//                 return;
//                  do
//                  {
//                      ArrayList v=new ArrayList();
//                      if (dtStat.getInt("trp_kilos")<1)
//                          continue;
//                      v.add(dtStat.getString("tra_codi"));
//                      v.add(dtStat.getString("tra_nomb"));
//                      v.add(dtStat.getInt("trp_kilos"));    
//                      v.add(dtStat.getString("trp_tipo")); 
//                      jtUsuPed.addLinea(v);
//                  } while (dtStat.next());
//         }
//      } catch (SQLException  k )
//      {
//          Error("Error al ver usuarios del pedido ",k);
//      }
//   }
//   void actTotalGrid()
//   {
//      try {
//          jtUsu.removeAllDatos();
//          
//          ResultSet rs;
//          int nRows = jtCabPed.getRowCount();
//          
//          HashMap<String, Dimension> hmP = new HashMap();
//          Dimension hmU;
//          for (int n = 0; n < nRows; n++)
//          {
//              int pvcId = pdpeve.getIdPedido(dtStat, jtCabPed.getValorInt(n, JTCAB_EMPPED),
//                  jtCabPed.getValorInt(n, JTCAB_EJEPED),
//                  jtCabPed.getValorInt(n, JTCAB_NUMPED));
//              if (pvcId > 0)
//              {
//                  psTra.setInt(1, pvcId);
//                  
//                  rs = psTra.executeQuery();
//                  while (rs.next())
//                  {
//                      hmU = hmP.get(rs.getString("tra_nomb"));
//                      if (hmU == null)
//                      {
//                          hmU = new Dimension();
//                          hmU.setSize(rs.getInt("trp_kilos"), 1);
//                      } else
//                      {
//                          hmU.setSize(hmU.getWidth() + rs.getInt("trp_kilos"),
//                              hmU.getHeight() + 1);
//                      }
//                      hmP.put(rs.getString("tra_nomb"), hmU);
//                  }
//                 
//              }
//          }
//          int totMin=0,numReg=0;
//          for (Map.Entry pair : hmP.entrySet())
//          {
//              ArrayList v = new ArrayList();
//              v.add(pair.getKey());
//              hmU = (Dimension) pair.getValue();
//              if (hmU.getWidth() <= 1)
//                  continue;
//              v.add(hmU.getWidth());
//              v.add(hmU.getHeight());
//              jtUsu.addLinea(v);
//              numReg+=hmU.getHeight();
//              totMin += hmU.getWidth();
//          }
//          tit_numregE.setValorDec(numReg);
//          kilosTotalE.setValorDec(totMin);
//      } catch (SQLException k)
//      {
//          Error("Error al ver acumulados de pedidos",k);
//      }
//   }
   /**
    * Muestra las lineas del pedido y del albaran si existe.
    * @param empCodi
    * @param avcAno
    * @param avcSerie
    * @param avcNume
    * @throws SQLException 
    */
   void verDatAlbaranPed(int empCodi,int avcAno,String avcSerie, int avcNume) throws SQLException
   {
      s="select 1 as tipo,l.pro_codi,sum(avp_numuni) as avp_numuni,sum(avp_canti) as avp_canti, " +
             " s.prv_codi,s.stp_feccad "+
             " from v_albvenpar as l,v_stkpart as s " +
             " WHERE s.eje_nume = l.avp_ejelot " +
             " and s.emp_codi = l.avp_emplot " +
             " and s.pro_serie = l.avp_serlot " +
             " and s.pro_nupar = l.avp_numpar " +
             " and s.pro_codi = l.pro_codi " +
             " and s.pro_numind = l.avp_numind " +
             " and l.emp_codi = "+empCodi+
             " and l.avc_ano = " + avcAno +
             " and l.avc_serie = '" +avcSerie + "'" +
             " and l.avc_nume = " + avcNume +
             " GROUP BY l.pro_codi  ,s.prv_codi, stp_feccad "+
             " UNION ALL "+
             "select 0 as tipo, l.pro_codi,sum(avp_numuni) as avp_numuni,sum(avp_canti) as avp_canti, " +
              " c.cli_codi AS prv_codi, c.avc_fecalb as stp_feccad " +
              " from v_albvenpar as l,v_albavec as c  where  c.avc_ano = l.avc_ano  "+
              " and c.emp_codi = l.emp_codi "+
              " and c.avc_serie = l.avc_serie "+
              " and c.avc_nume = l.avc_nume  "+
              " and l.avp_numpar = 0 "+
             " AND l.emp_codi = " + empCodi+
             " and l.avc_ano = " + avcAno +
             " and l.avc_serie = '" + avcSerie + "'" +
             " and l.avc_nume = " + avcNume +
             " GROUP BY l.pro_codi ,c.cli_codi , avc_fecalb "+
             " order by 2 ";

//    debug(s);
    if (! dtCon1.select(s))
      return;
    int rowCount;
    int nLin=0;
    double kilosColgado=0;
    double kilosEncajado=0;
    do
    {
      rowCount = jtLinPed.getRowCount();
      
      ArrayList v = new ArrayList();
      v.add("A");
      v.add(dtCon1.getString("pro_codi"));
      v.add(pro_codiE.getNombArt(dtCon1.getInt("pro_codi")));
      if (dtCon1.getInt("tipo")==0)
      {
        v.add("");
        v.add("");
      }
      else
      {
        v.add(dtCon1.getString("prv_codi"));
        v.add(prv_codiE.getNombPrv(dtCon1.getString("prv_codi"), dtStat));
      }
      if (dtCon1.getInt("tipo")==0)
        v.add("");
      else
        v.add(dtCon1.getFecha("stp_feccad","dd-MM-yy"));
    
      v.add(dtCon1.getString("avp_numuni"));
      v.add(""); // Precio
      v.add(false); // Conf
      v.add(dtCon1.getString("avp_canti"));            
      v.add(""); // NL
      jtLinPed.addLinea(v);
        if (pro_codiE.getLikeProd().getInt("pro_encaja")==0)
         {// Genero colgado
             kilosColgado+=dtCon1.getDouble("avp_canti");
         }
         else
             kilosEncajado+=dtCon1.getDouble("avp_canti");
    } while (dtCon1.next());
       kilosCajasAlbE.setValorDec(kilosEncajado);
       kilosColgadoAlbE.setValorDec(kilosColgado);
       kilosAlbarE.setValorDec(kilosEncajado + kilosColgado);
   }
//   void actAcumJT()
//   {
//     int nRows = jtLinPed.getRowCount(),nl = 0,nu=0;
//
//     for (int n = 0; n < nRows; n++)
//     {
//       if (jtLinPed.getValorInt(n, 1) == 0 || !jtLinPed.getValString(n,0).equals("P"))
//         continue;
//       nl++;
//     }
//     numPedTE.setValorInt(nl);    
//   }
   
   
    private boolean iniciarCons(boolean ejecSelect) throws SQLException, ParseException
    {
        if (pvc_feciniE.getError())
        {
          mensajeErr("Fecha INICIAL no es valida");
          pvc_feciniE.requestFocus();
          return false;
        }
        if (pvc_fecfinE.getError())
        {
          mensajeErr("Fecha FINAL no es valida");
          pvc_feciniE.requestFocus();
          return false;
        }
        if (trp_fecbusE.isNull())
        {
            msgBox("Introduzca Fecha Reparto");
            trp_fecbusE.requestFocus();
            return false;
        }
        fechaReparto=trp_fecbusE.getDate();
        if (! ejecSelect)
          return true;
     
     s = "SELECT c.*,trp_orden, av.avc_id,av.avc_impres,av.cli_ruta, cl.cli_nomb,cl.cli_poble,"
         + " c.rut_codi, al.rut_nomb FROM pedvenc as c"
         + " left join transpedidos on trp_id = pvc_id and trp_kilos > 0"
         + " left join v_albavec as av on c.avc_ano = av.avc_ano "
         + " and c.avc_serie= av.avc_serie and c.avc_nume =  av.avc_nume and av.emp_codi = c.emp_codi "
         + ",clientes as cl,v_rutas as al " +       
        " WHERE c.emp_codi = "+EU.em_cod+       
        " and c.pvc_confir = 'S'"+
        " and cl.cli_codi = c.cli_codi " +
        " and c.rut_codi = al.rut_codi "    ;

    s += " order by trp_orden,c.pvc_fecent,c.cli_codi ";

    jtCabPed.setEnabled(false);
    jtCabPed.removeAllDatos();
//      debug("s: "+s);
    if (!dtCon1.select(s))
    {
      mensajeErr("NO hay PEDIDOS que cumplan estas condiciones");
    //  verPedidosE.requestFocus();
      return false;
    }
    return true;
  }
 
  private void confJtLin() throws Exception
   {
     ArrayList v = new ArrayList();
     v.add("Tipo"); // 0 Albaran o Pedido
     v.add("Prod."); // 1
     v.add("Desc. Prod."); // 2
     v.add("Prv"); // 3
     v.add("Nombre Prv"); // 4
     v.add("Fec.Cad"); // 5
     v.add("Cant"); // 6
     v.add("Precio"); // 7
     v.add("Conf"); // 8 Confirmado Precio ?
     v.add("Comentario"); // 9 Comentario
     v.add("NL."); // 10
     jtLinPed.setCabecera(v);
     jtLinPed.setMaximumSize(new Dimension(548, 127));
     jtLinPed.setMinimumSize(new Dimension(548, 127));
     jtLinPed.setPreferredSize(new Dimension(548, 127));
     jtLinPed.setPuntoDeScroll(50);
     jtLinPed.setAnchoColumna(new int[]
                        {20,50, 160, 50, 100, 55, 50, 50, 30, 200, 30});
     jtLinPed.setAlinearColumna(new int[]
                          {1,2, 0, 2, 0, 1, 2, 2, 1, 0, 2});
     
     jtLinPed.setFormatoColumna(7, "-,--9.99");
     jtLinPed.setFormatoColumna(8, "BSN");
     jtLinPed.setAjustarGrid(true);
    }
  
 
    public void Baceptar_doClick()
    {
        Baceptar.doClick();
    }
   
    void Baceptar_actionPerformed()
    {
    try
    {
        if (! jtCabPed.isVacio())
        {
            jtCab.salirGrid();            
            guardaDatos(jtCabPed.getSelectedRow());                        
        }
        
        inCambio=true;
        traCodiAnt=0;
        fecTranAnt=Formatear.getFechaAct("dd-MM-yy");
        if (! iniciarCons(true))
          return;
//        boolean swServ=verPedidosE.getValor().equals("S"); // A servir (tienen albaran y no esta listado)
      psTra = dtCon1.getPreparedStatement("select tp.*,tra_nomb from transpedidos tp left join transportista  as tr on tr.tra_tipo='V' "
         + " and tr.tra_codi = tp.tra_codi where trp_id= ? "
         + (tra_codbusE.isNull()?"":" and tp.tra_codi = "+tra_codbusE.getValorInt()) );
     
      do
      {
        if (!servRutaC.getValor().equals("*"))
        {
            boolean servRuta=false;
            if (dtCon1.getInt("avc_id",true)!=0)                
            {
                s="select alr_nume from albrutalin where avc_id="+dtCon1.getInt("avc_id");
                servRuta=dtStat.select(s);
            }
            if (servRuta && servRutaC.getValor().equals("N"))
                continue;
            if (! servRuta && servRutaC.getValor().equals("S"))
                continue;

            
        }     
        boolean swImpres=false;
//        if (!albListadoC.getValor().equals("*"))
//        {                      
//            if (dtCon1.getObject("avc_impres")!=null)               
//                swImpres= (dtCon1.getInt("avc_impres") & 1) == 1;
//            if (swImpres && albListadoC.getValor().equals("N"))
//                continue;
//            if (! swImpres && albListadoC.getValor().equals("S"))
//                continue;
//        }
        if (!rut_codiE.isNull() && !swCliente)
        {
            if (dtCon1.getObject("cli_ruta")!=null)
            {
                if (! rut_codiE.getText().equals(dtCon1.getString("cli_ruta")))
                    continue;
            }
            else
                 if (! rut_codiE.getText().equals(dtCon1.getString("rut_codi")))
                    continue;
        }
        ArrayList v=new ArrayList();
        int pvcId=pdpeve.getIdPedido(dtStat, dtCon1.getInt("emp_codi"), dtCon1.getInt("eje_nume"), 
            dtCon1.getInt("pvc_nume"));
        if (pvcId<0)
        {
            v.add("");
            v.add(0);
        }
        else
        {
             s="Select tp.*,tra_nomb from transpedidos as tp, transportista as tr where  trp_id="+pvcId+
                 " and trp_kilos>0 "+
                 " and tr.tra_codi=tp.tra_codi "+
                 " and tra_tipo='V'"+
                 (tra_codbusE.isNull()?"": " and tp.tra_codi ="+tra_codbusE.getValorInt());
             if (!dtStat.select(s))
             {
                v.add("");
                v.add(""); // Cod. transp
                v.add(""); // Nombre transp.
                v.add(0); 
                v.add("C");
                v.add(trp_fecbusE.getText());
                v.add("");
                v.add("");
             }
             else
             {               
                v.add(dtStat.getInt("trp_orden",false));
                v.add(dtStat.getInt("tra_codi"));
                v.add(dtStat.getString("tra_nomb"));
                v.add(dtStat.getInt("trp_kilos"));
                v.add(dtStat.getString("trp_tipo"));
                v.add(dtStat.getFecha("trp_fecent","dd-MM-yy"));
                v.add(dtStat.getFecha("trp_fecent","HH"));
                v.add(dtStat.getFecha("trp_fecent","mm"));
             }
        }
        if (!tra_codbusE.isNull())
        {
            if (dtStat.getNOREG())
                continue;
            if ( dtStat.getInt("tra_codi")!=tra_codbusE.getValorInt())
                continue;
        }
        v.add(dtCon1.getString("emp_codi")); // 0
        v.add(dtCon1.getString("eje_nume")); // 1
        v.add(dtCon1.getString("pvc_nume")); // 2
        v.add(dtCon1.getString("cli_codi")); // 3
        v.add(dtCon1.getObject("pvc_clinom")==null?dtCon1.getString("cli_nomb"):dtCon1.getString("pvc_clinom")); // 4
        v.add(dtCon1.getObject("cli_poble")); // 5 
        v.add(dtCon1.getFecha("pvc_fecent","dd-MM-yyyy")); // 5
        v.add(dtCon1.getString("pvc_confir")); // 6
        v.add(dtCon1.getInt("pvc_cerra")!=0); // 7
        v.add(dtCon1.getString("pvc_depos")); // 8
        v.add(dtCon1.getString("rut_nomb")); // 9
        v.add(dtCon1.getString("avc_ano")); //10
        v.add(dtCon1.getString("avc_serie")); // 11
        v.add(dtCon1.getString("avc_nume")); //12
        jtCabPed.addLinea(v);
           
      } while (dtCon1.next());
      nPedT.setValorDec(jtCabPed.getRowCount());
      if (jtCabPed.isVacio())
      {
          mensajeErr("No encontrados pedidos con estos criterios");
          return;
      }
      jtCabPed.requestFocusInicio();
      jtCabPed.setEnabled(true);
      verDatPed(jtCabPed.getValorInt(JTCAB_EMPPED),
          jtCabPed.getValorInt(JTCAB_EJEPED),jtCabPed.getValorInt(JTCAB_NUMPED));
      jtCabPed.requestFocusInicio();
      inCambio=false;
//      actTotalGrid();
//      verUsuPedido(0);
      
    }
    catch (SQLException | ParseException k)
    {
      Error("Error al buscar pedidos", k);
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

        trp_kilosE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        tra_codiE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        tra_nombE = new gnu.chu.controles.CTextField();
        trp_fechaE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yy");
        trp_tipoE = new gnu.chu.controles.CTextField(Types.CHAR,"X",1);
        trp_ordenE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        trp_horaE = new gnu.chu.controles.CTextField(Types.DECIMAL,"99");
        trp_minutoE = new gnu.chu.controles.CTextField(Types.DECIMAL,"99");
        PPrinc = new gnu.chu.controles.CPanel();
        Pcabe = new gnu.chu.controles.CPanel();
        cLabel6 = new gnu.chu.controles.CLabel();
        pvc_feciniE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel7 = new gnu.chu.controles.CLabel();
        pvc_fecfinE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel9 = new gnu.chu.controles.CLabel();
        Baceptar = new gnu.chu.controles.CButton(Iconos.getImageIcon("check"));
        cLabel2 = new gnu.chu.controles.CLabel();
        servRutaC = new gnu.chu.controles.CComboBox();
        cLabel22 = new gnu.chu.controles.CLabel();
        rut_codiE = new gnu.chu.controles.CLinkBox();
        tra_codbusE = new gnu.chu.controles.CLinkBox();
        cLabel8 = new gnu.chu.controles.CLabel();
        trp_fecbusE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yy");
        cLabel4 = new gnu.chu.controles.CLabel();
        trp_idE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#####9");
        jtCab = new gnu.chu.controles.CGridEditable(16);
        jtCabPed = new gnu.chu.controles.Cgrid(12);
        jtLinPed = new gnu.chu.controles.Cgrid(11);
        Ppie = new gnu.chu.controles.CPanel();
        nPedT = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9");
        cLabel23 = new gnu.chu.controles.CLabel();
        cLabel25 = new gnu.chu.controles.CLabel();
        kilosPedidE = new gnu.chu.controles.CTextField(Types.DECIMAL, "###9");
        cLabel26 = new gnu.chu.controles.CLabel();
        kilosCajasE = new gnu.chu.controles.CTextField(Types.DECIMAL, "###9");
        cLabel27 = new gnu.chu.controles.CLabel();
        kilosColgadoE = new gnu.chu.controles.CTextField(Types.DECIMAL, "###9");
        cLabel28 = new gnu.chu.controles.CLabel();
        kilosAlbarE = new gnu.chu.controles.CTextField(Types.DECIMAL, "###9");
        cLabel29 = new gnu.chu.controles.CLabel();
        kilosCajasAlbE = new gnu.chu.controles.CTextField(Types.DECIMAL, "###9");
        cLabel30 = new gnu.chu.controles.CLabel();
        kilosColgadoAlbE = new gnu.chu.controles.CTextField(Types.DECIMAL, "###9");
        cLabel31 = new gnu.chu.controles.CLabel();
        cLabel32 = new gnu.chu.controles.CLabel();
        cLabel33 = new gnu.chu.controles.CLabel();
        cLabel34 = new gnu.chu.controles.CLabel();
        kilosPedidE1 = new gnu.chu.controles.CTextField(Types.DECIMAL, "###9");
        cLabel35 = new gnu.chu.controles.CLabel();
        kilosCajasE1 = new gnu.chu.controles.CTextField(Types.DECIMAL, "###9");
        cLabel36 = new gnu.chu.controles.CLabel();
        kilosColgadoE1 = new gnu.chu.controles.CTextField(Types.DECIMAL, "###9");
        BInsTodoPed = new gnu.chu.controles.CButton();

        tra_codiE.setText("0");

        tra_nombE.setEnabled(false);

        trp_fechaE.setText("0");

        trp_tipoE.setText("E");
        trp_tipoE.setMayusc(true);

        trp_ordenE.setText("0");

        trp_horaE.setText("0");

        trp_minutoE.setText("0");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        PPrinc.setLayout(new java.awt.GridBagLayout());

        Pcabe.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        Pcabe.setMaximumSize(new java.awt.Dimension(725, 50));
        Pcabe.setMinimumSize(new java.awt.Dimension(725, 50));
        Pcabe.setName(""); // NOI18N
        Pcabe.setPreferredSize(new java.awt.Dimension(725, 50));
        Pcabe.setLayout(null);

        cLabel6.setText("De Fecha");
        Pcabe.add(cLabel6);
        cLabel6.setBounds(10, 3, 49, 18);
        Pcabe.add(pvc_feciniE);
        pvc_feciniE.setBounds(70, 3, 76, 18);

        cLabel7.setText("A Fecha");
        Pcabe.add(cLabel7);
        cLabel7.setBounds(160, 3, 43, 18);
        Pcabe.add(pvc_fecfinE);
        pvc_fecfinE.setBounds(210, 3, 75, 18);

        cLabel9.setText("Transp.");
        Pcabe.add(cLabel9);
        cLabel9.setBounds(310, 25, 50, 18);

        Baceptar.setText("Aceptar");
        Pcabe.add(Baceptar);
        Baceptar.setBounds(620, 30, 100, 25);

        cLabel2.setText("ID");
        Pcabe.add(cLabel2);
        cLabel2.setBounds(630, 0, 20, 17);

        servRutaC.addItem("**","*");
        servRutaC.addItem("Si","S");
        servRutaC.addItem("No","N");
        Pcabe.add(servRutaC);
        servRutaC.setBounds(570, 0, 50, 17);

        cLabel22.setText("Ruta");
        cLabel22.setPreferredSize(new java.awt.Dimension(60, 18));
        Pcabe.add(cLabel22);
        cLabel22.setBounds(10, 25, 40, 18);

        rut_codiE.setFormato(Types.CHAR,"X",2);
        rut_codiE.setAncTexto(30);
        rut_codiE.setMayusculas(true);
        rut_codiE.setPreferredSize(new java.awt.Dimension(152, 18));
        Pcabe.add(rut_codiE);
        rut_codiE.setBounds(50, 25, 250, 18);

        tra_codbusE.setAncTexto(30);
        Pcabe.add(tra_codbusE);
        tra_codbusE.setBounds(360, 25, 230, 17);

        cLabel8.setText("Fec.Transporte");
        Pcabe.add(cLabel8);
        cLabel8.setBounds(300, 0, 90, 18);
        Pcabe.add(trp_fecbusE);
        trp_fecbusE.setBounds(400, 0, 60, 18);

        cLabel4.setText("Servidos en Ruta ");
        Pcabe.add(cLabel4);
        cLabel4.setBounds(470, 0, 100, 17);

        trp_idE.setToolTipText("");
        trp_idE.setEnabled(false);
        Pcabe.add(trp_idE);
        trp_idE.setBounds(650, 0, 2, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        PPrinc.add(Pcabe, gridBagConstraints);

        try {
            ArrayList v=new ArrayList();
            v.add("Ord."); //0
            v.add("Kilos"); //1
            v.add("Fecha"); // 2Fec.Transporte
            v.add("H:"); // 3Hora.
            v.add("M:"); // 4Minuto
            v.add("Em"); // 5
            v.add("Eje."); // 6
            v.add("N.Ped");// 7
            v.add("Cliente"); // 8
            v.add("Nombre Cliente"); // 9
            v.add("Población"); // 10
            v.add("Ej.Alb");//11
            v.add("S.Alb"); //12
            v.add("Num.Alb"); //13
            v.add("Cerr");// 14
            v.add("Dep?"); // 15
            jtCab.setCabecera(v);
            ArrayList v1=new ArrayList();
            v1.add(trp_ordenE); //0
            v1.add(trp_kilosE); // 1
            v1.add(trp_fechaE); // 2
            v1.add(trp_horaE); // 3
            v1.add(trp_minutoE); // 4
            for (int n=5;n<14;n++)
            {

                CTextField tf=new CTextField();
                tf.setEnabled(false);
                v1.add(tf);
            }
            CCheckBox cb1=new CCheckBox();
            cb1.setEnabled(false);
            v1.add(cb1);
            CCheckBox cb2=new CCheckBox();
            cb2.setEnabled(false);
            v1.add(cb2);
            try {
                jtCab.setCampos(v1);
            }  catch (Exception k)
            {
                msgBox(k.getMessage());
                Error("Error al establecer campos",k);
            }
            jtCab.setMaximumSize(new Dimension(548, 158));
            jtCab.setMinimumSize(new Dimension(548, 158));
            jtCab.setPreferredSize(new Dimension(548, 158));
            jtCabPed.setAnchoColumna(new int[]{20,40,55,20,20,26,40,49,55,150,100});
            jtCabPed.setAlinearColumna(new int[]{2,2,1,2,2,0,2,2,2,0,0});

            jtCab.setFormatoCampos();
            jtCab.setCanDeleteLinea(false);
            jtCab.setCanInsertLinea(false);
        } catch (Exception k){Error("Error al configurar grid cabecera",k);}
        jtCab.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtCab.setMaximumSize(new java.awt.Dimension(681, 220));
        jtCab.setMinimumSize(new java.awt.Dimension(681, 220));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        PPrinc.add(jtCab, gridBagConstraints);

        try {
            ArrayList v=new ArrayList();
            v.add("Em"); // 0
            v.add("Eje."); // 1
            v.add("N.Ped");// 2
            v.add("Cliente"); // 3
            v.add("Nombre Cliente"); // 4
            v.add("Población"); // 5
            v.add("Fec.Entrega"); // 6
            v.add("Conf"); // 7
            v.add("Cerr");// 8
            v.add("Dep?"); // 9
            v.add("Ruta");// 10
            v.add("Inc");//11
            jtCabPed.setCabecera(v);
        } catch (Exception k){Error("Error al configurar grid cabecera",k);}
        jtCabPed.setFormatoColumna(11,"BS");
        jtCabPed.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtCabPed.setMaximumSize(new java.awt.Dimension(681, 220));
        jtCabPed.setMinimumSize(new java.awt.Dimension(681, 220));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        PPrinc.add(jtCabPed, gridBagConstraints);

        try {confJtLin();} catch (Exception k){Error("Error al configurar grid Lineas",k);}
        jtLinPed.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtLinPed.setMaximumSize(new java.awt.Dimension(681, 101));
        jtLinPed.setMinimumSize(new java.awt.Dimension(681, 101));
        jtLinPed.setPreferredSize(new java.awt.Dimension(681, 102));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        PPrinc.add(jtLinPed, gridBagConstraints);

        Ppie.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Ppie.setMaximumSize(new java.awt.Dimension(705, 125));
        Ppie.setMinimumSize(new java.awt.Dimension(705, 125));
        Ppie.setPreferredSize(new java.awt.Dimension(705, 125));
        Ppie.setLayout(null);

        nPedT.setEnabled(false);
        Ppie.add(nPedT);
        nPedT.setBounds(500, 40, 30, 17);

        cLabel23.setText("Kilos");
        cLabel23.setPreferredSize(new java.awt.Dimension(60, 18));
        Ppie.add(cLabel23);
        cLabel23.setBounds(80, 70, 40, 18);

        cLabel25.setText("Num.Pedidos ");
        cLabel25.setPreferredSize(new java.awt.Dimension(60, 18));
        Ppie.add(cLabel25);
        cLabel25.setBounds(420, 40, 80, 17);

        kilosPedidE.setEnabled(false);
        Ppie.add(kilosPedidE);
        kilosPedidE.setBounds(120, 70, 40, 18);

        cLabel26.setText("Kg. Cajas");
        cLabel26.setPreferredSize(new java.awt.Dimension(60, 18));
        Ppie.add(cLabel26);
        cLabel26.setBounds(170, 70, 70, 18);

        kilosCajasE.setEnabled(false);
        Ppie.add(kilosCajasE);
        kilosCajasE.setBounds(240, 70, 40, 18);

        cLabel27.setText("Kg. Colgado");
        cLabel27.setPreferredSize(new java.awt.Dimension(60, 18));
        Ppie.add(cLabel27);
        cLabel27.setBounds(290, 70, 70, 18);

        kilosColgadoE.setEnabled(false);
        Ppie.add(kilosColgadoE);
        kilosColgadoE.setBounds(360, 70, 35, 18);

        cLabel28.setBackground(java.awt.Color.orange);
        cLabel28.setText("Pedido");
        cLabel28.setOpaque(true);
        cLabel28.setPreferredSize(new java.awt.Dimension(60, 18));
        Ppie.add(cLabel28);
        cLabel28.setBounds(20, 70, 50, 18);

        kilosAlbarE.setEnabled(false);
        Ppie.add(kilosAlbarE);
        kilosAlbarE.setBounds(120, 90, 40, 18);

        cLabel29.setText("Kg. Cajas");
        cLabel29.setPreferredSize(new java.awt.Dimension(60, 18));
        Ppie.add(cLabel29);
        cLabel29.setBounds(170, 90, 70, 18);

        kilosCajasAlbE.setEnabled(false);
        Ppie.add(kilosCajasAlbE);
        kilosCajasAlbE.setBounds(240, 90, 40, 18);

        cLabel30.setText("Kg. Colgado");
        cLabel30.setPreferredSize(new java.awt.Dimension(60, 18));
        Ppie.add(cLabel30);
        cLabel30.setBounds(290, 90, 70, 18);

        kilosColgadoAlbE.setEnabled(false);
        Ppie.add(kilosColgadoAlbE);
        kilosColgadoAlbE.setBounds(360, 90, 35, 18);

        cLabel31.setText("Kilos");
        cLabel31.setPreferredSize(new java.awt.Dimension(60, 18));
        Ppie.add(cLabel31);
        cLabel31.setBounds(80, 90, 40, 18);

        cLabel32.setBackground(java.awt.Color.orange);
        cLabel32.setText("Albaran");
        cLabel32.setOpaque(true);
        cLabel32.setPreferredSize(new java.awt.Dimension(60, 18));
        Ppie.add(cLabel32);
        cLabel32.setBounds(20, 90, 50, 18);

        cLabel33.setBackground(java.awt.Color.orange);
        cLabel33.setText("Transp.");
        cLabel33.setOpaque(true);
        cLabel33.setPreferredSize(new java.awt.Dimension(60, 18));
        Ppie.add(cLabel33);
        cLabel33.setBounds(20, 40, 50, 17);

        cLabel34.setText("Kilos");
        cLabel34.setPreferredSize(new java.awt.Dimension(60, 18));
        Ppie.add(cLabel34);
        cLabel34.setBounds(80, 40, 40, 17);

        kilosPedidE1.setEnabled(false);
        Ppie.add(kilosPedidE1);
        kilosPedidE1.setBounds(120, 40, 40, 17);

        cLabel35.setText("Kg. Cajas");
        cLabel35.setPreferredSize(new java.awt.Dimension(60, 18));
        Ppie.add(cLabel35);
        cLabel35.setBounds(170, 40, 70, 17);

        kilosCajasE1.setEnabled(false);
        Ppie.add(kilosCajasE1);
        kilosCajasE1.setBounds(240, 40, 40, 17);

        cLabel36.setText("Kg. Colgado");
        cLabel36.setPreferredSize(new java.awt.Dimension(60, 18));
        Ppie.add(cLabel36);
        cLabel36.setBounds(290, 40, 70, 17);

        kilosColgadoE1.setEnabled(false);
        Ppie.add(kilosColgadoE1);
        kilosColgadoE1.setBounds(360, 40, 35, 17);

        BInsTodoPed.setText("cButton1");
        Ppie.add(BInsTodoPed);
        BInsTodoPed.setBounds(10, 0, 90, 20);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        PPrinc.add(Ppie, gridBagConstraints);

        getContentPane().add(PPrinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton BInsTodoPed;
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CPanel PPrinc;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Ppie;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel22;
    private gnu.chu.controles.CLabel cLabel23;
    private gnu.chu.controles.CLabel cLabel25;
    private gnu.chu.controles.CLabel cLabel26;
    private gnu.chu.controles.CLabel cLabel27;
    private gnu.chu.controles.CLabel cLabel28;
    private gnu.chu.controles.CLabel cLabel29;
    private gnu.chu.controles.CLabel cLabel30;
    private gnu.chu.controles.CLabel cLabel31;
    private gnu.chu.controles.CLabel cLabel32;
    private gnu.chu.controles.CLabel cLabel33;
    private gnu.chu.controles.CLabel cLabel34;
    private gnu.chu.controles.CLabel cLabel35;
    private gnu.chu.controles.CLabel cLabel36;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.controles.CLabel cLabel9;
    private gnu.chu.controles.CGridEditable jtCab;
    private gnu.chu.controles.Cgrid jtCabPed;
    private gnu.chu.controles.Cgrid jtLinPed;
    private gnu.chu.controles.CTextField kilosAlbarE;
    private gnu.chu.controles.CTextField kilosCajasAlbE;
    private gnu.chu.controles.CTextField kilosCajasE;
    private gnu.chu.controles.CTextField kilosCajasE1;
    private gnu.chu.controles.CTextField kilosColgadoAlbE;
    private gnu.chu.controles.CTextField kilosColgadoE;
    private gnu.chu.controles.CTextField kilosColgadoE1;
    private gnu.chu.controles.CTextField kilosPedidE;
    private gnu.chu.controles.CTextField kilosPedidE1;
    private gnu.chu.controles.CTextField nPedT;
    private gnu.chu.controles.CTextField pvc_fecfinE;
    private gnu.chu.controles.CTextField pvc_feciniE;
    private gnu.chu.controles.CLinkBox rut_codiE;
    private gnu.chu.controles.CComboBox servRutaC;
    private gnu.chu.controles.CLinkBox tra_codbusE;
    private gnu.chu.controles.CTextField tra_codiE;
    private gnu.chu.controles.CTextField tra_nombE;
    private gnu.chu.controles.CTextField trp_fecbusE;
    private gnu.chu.controles.CTextField trp_fechaE;
    private gnu.chu.controles.CTextField trp_horaE;
    private gnu.chu.controles.CTextField trp_idE;
    private gnu.chu.controles.CTextField trp_kilosE;
    private gnu.chu.controles.CTextField trp_minutoE;
    private gnu.chu.controles.CTextField trp_ordenE;
    private gnu.chu.controles.CTextField trp_tipoE;
    // End of variables declaration//GEN-END:variables

    @Override
    public void PADPrimero() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void PADAnterior() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void PADSiguiente() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void PADUltimo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void ej_query1() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void canc_query() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void ej_edit1() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void canc_edit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void ej_addnew1() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void canc_addnew() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void ej_delete1() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void canc_delete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void activar(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   
}
