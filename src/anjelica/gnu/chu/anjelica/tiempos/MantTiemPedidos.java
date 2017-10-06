package gnu.chu.anjelica.tiempos;
/*
 *<p>Titulo: MantTiempPedidos </p>
 * <p>Descripción: Mantenimiento Tiempos utilizados para los diferentes Pedidos de Venta.
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


import gnu.chu.anjelica.ventas.*;
import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.pad.MantRepres;
import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.camposdb.proPanel;
import gnu.chu.camposdb.prvPanel;
import gnu.chu.controles.CCheckBox;
import gnu.chu.controles.CTextField;
import gnu.chu.controles.StatusBar;
import gnu.chu.eventos.GridAdapter;
import gnu.chu.eventos.GridEvent;
import gnu.chu.interfaces.ejecutable;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.ventana;
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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class MantTiemPedidos extends  ventana  
{
    PreparedStatement ps;
    boolean inCambio=true;
    String usuNomAnt;
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
    private final int JTCAB_USUA=0;
    private final int JTCAB_TIEMPO=1;    
    private final int JTCAB_EMPPED=2;
    private final int JTCAB_EJEPED=3;
    private final int JTCAB_NUMPED=4;
    private final int JTCAB_NOMCLI=6;
    private final int JTCAB_EJEALB=13;
    private final int JTCAB_SERALB=14;
    private final int JTCAB_NUMALB= 15;   
    
    
//    private final int JTCAB_POBCLI=5;
    
    public MantTiemPedidos(EntornoUsuario eu, Principal p) {
        this(eu, p, null);
    }

    public MantTiemPedidos(EntornoUsuario eu, Principal p, HashMap<String,String> ht) {
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
            setTitulo("Mant. Tiempos Pedidos Ventas");
            if (jf.gestor.apuntar(this)) {
                jbInit();
            } else {
                setErrorInit(true);
            }
        } catch (Exception e) {
           ErrorInit(e);
        }
    }

    public MantTiemPedidos(gnu.chu.anjelica.menu p, EntornoUsuario eu, HashMap <String,String> ht) {
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
            setTitulo("Mant. Tiempos Pedidos Ventas");

            jbInit();
        } catch (Exception e) {
           ErrorInit(e);
        }
    }
    public MantTiemPedidos(ventana papa) throws Exception
    {
        padre=papa;
        dtStat=padre.dtStat;
        dtCon1=padre.dtCon1;
        vl=padre.vl;
        jf=padre.jf;

        EU=padre.EU;
        setTitulo("Mant. Tiempos Pedidos Ventas");

        jbInit();
    }
    private void jbInit() throws Exception {
        statusBar = new StatusBar(this);

        iniciarFrame();

        this.setVersion("2017-10-04");

        initComponents();
        this.setSize(new Dimension(730, 535));
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);

        conecta();
    }

    @Override
    public void iniciarVentana() throws Exception 
    {
     Pcabe.setDefButton(Baceptar);
     pvc_feciniE.setAceptaNulo(false);
     pvc_fecfinE.setAceptaNulo(false);
     cli_codiE.setCeroIsNull(true);
     MantRepres.llenaLinkBox(rep_codiE, dtCon1);
     pdconfig.llenaDiscr(dtStat, zon_codiE, pdconfig.D_ZONA,EU.em_cod);
     pdconfig.llenaDiscr(dtStat, rut_codiE, pdconfig.D_RUTAS, EU.em_cod);
     rut_codiE.setCeroIsNull(true);
     cli_codiE.iniciar(dtStat, this, vl, EU);
     cli_codiE.setCampoReparto(true);
     ps = dtCon1.getPreparedStatement("select * from tiempostarea where tit_tipdoc = 'P' and tit_id= ?");
     
     
     sbe_codiE.iniciar(dtStat, this, vl, EU);
     
     sbe_codiE.setAceptaNulo(true);
     sbe_codiE.setValorInt(0);
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
     jtCabPed.addGridListener(new GridAdapter()
     {
         @Override
          public void cambiaLinea(GridEvent event){
              guardaDatos(event.getLinea());
          }
           @Override
          public void afterCambiaLinea(GridEvent event){
//              if (tit_usunomE.getText().equals(""))
//              {
//                tit_usunomE.setText(usuNomAnt);
//                jtCabPed.setValor(usuNomAnt,JTCAB_USUA);
//              }
              verDatPed(jtCabPed.getValorInt(JTCAB_EMPPED),
                jtCabPed.getValorInt(JTCAB_EJEPED),jtCabPed.getValorInt(JTCAB_NUMPED));
              actTotalGrid();
              verUsuPedido(event.getLinea());
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
    
    void guardaDatos(int linea)
    {
        if (inCambio)
            return;
        try
        {
            
            int pvcId=pdpeve.getIdPedido(dtStat,jtCabPed.getValorInt(linea,JTCAB_EMPPED),
                jtCabPed.getValorInt(linea,JTCAB_EJEPED),jtCabPed.getValorInt(linea,JTCAB_NUMPED));
            if ( pvcId<0)
                throw new SQLException("Error al Buscar pedido "+jtCabPed.getValorInt(linea,JTCAB_EMPPED)+
                    "-"+jtCabPed.getValorInt(linea,JTCAB_EJEPED)+"-"+jtCabPed.getValorInt(linea,JTCAB_NUMPED));
            if (tit_usunomE.getText().equals("") && tit_tiempoE.getValorInt()>0)
            {
                jtCabPed.setValor(usuNomAnt,linea,JTCAB_USUA);
                tit_usunomE.setText(usuNomAnt);
            }
            usuNomAnt=tit_usunomE.getText();
            s="Select * from tiempostarea where tit_tipdoc = 'P' and tit_id="+pvcId+
                " and usu_nomb='"+tit_usunomE.getText()+"'";
            if (!dtAdd.select(s,true))
            {
                dtAdd.addNew();
                dtAdd.setDato("usu_nomb",tit_usunomE.getText());
                dtAdd.setDato("tit_tipdoc","P");
                dtAdd.setDato("tit_id",pvcId);
            }
            else
                dtAdd.edit();
            dtAdd.setDato("tit_tiempo",tit_tiempoE.getValorInt());
            dtAdd.update();
            dtAdd.commit();
            mensajeRapido("Guardado Registo");
        } catch (SQLException ex)
        {
            Error("Error al actualizar tiempo de pedido",ex);
        }
    }
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
     

       pvc_fecpedE.setText(dtCon1.getFecha("pvc_fecped"));
       pvc_horpedE.setText(dtCon1.getFecha("pvc_fecped","HH.mm"));
       pvc_comenE.setText(dtCon1.getString("pvc_comen"));
       double kilosColgado=0;
       double kilosEncajado=0;
       do
       {
         ArrayList v=new ArrayList();
         v.add("P");
         v.add(dtCon1.getString("pro_codi"));
         v.add(pro_codiE.getNombArtCli(dtCon1.getInt("pro_codi"),
                                              cli_codiE.getValorInt(),EU.em_cod,dtStat));
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
       int nRows=jtCabPed.getRowCount();
        kilosCajasE.setValorDec(kilosEncajado);
       kilosColgadoE.setValorDec(kilosColgado);
       kilosPedidE.setValorDec(kilosEncajado+kilosColgado);
     } catch (Exception k)
     {
       Error("Error al Ver datos de pedido",k);
     }
   }
   void verUsuPedido(int row)
   {
      jtUsuPed.removeAllDatos(); 
      try {
        ResultSet rs;
        int pvcId = pdpeve.getIdPedido(dtStat, jtCabPed.getValorInt(row, JTCAB_EMPPED),
                    jtCabPed.getValorInt(row, JTCAB_EJEPED),
                    jtCabPed.getValorInt(row, JTCAB_NUMPED));
         if (pvcId > 0)
         {
                  ps.setInt(1, pvcId);
                  rs = ps.executeQuery();
                  while (rs.next())
                  {
                      ArrayList v=new ArrayList();
                      if (rs.getInt("tit_tiempo")<1)
                          continue;
                      v.add(rs.getString("usu_nomb"));
                      v.add(rs.getInt("tit_tiempo"));                      
                      jtUsuPed.addLinea(v);
                  }
         }
      } catch (SQLException k)
      {
          Error("Error al ver usuarios del pedido ",k);
      }
   }
   void actTotalGrid()
   {
      try {
          jtUsu.removeAllDatos();
          
          ResultSet rs;
          int nRows = jtCabPed.getRowCount();
          
          HashMap<String, Dimension> hmP = new HashMap();
          Dimension hmU;
          for (int n = 0; n < nRows; n++)
          {
              int pvcId = pdpeve.getIdPedido(dtStat, jtCabPed.getValorInt(n, JTCAB_EMPPED),
                  jtCabPed.getValorInt(n, JTCAB_EJEPED),
                  jtCabPed.getValorInt(n, JTCAB_NUMPED));
              if (pvcId > 0)
              {
                  ps.setInt(1, pvcId);
                  rs = ps.executeQuery();
                  while (rs.next())
                  {
                      hmU = hmP.get(rs.getString("usu_nomb"));
                      if (hmU == null)
                      {
                          hmU = new Dimension();
                          hmU.setSize(rs.getInt("tit_tiempo"), 1);
                      } else
                      {
                          hmU.setSize(hmU.getWidth() + rs.getInt("tit_tiempo"),
                              hmU.getHeight() + 1);
                      }
                      hmP.put(rs.getString("usu_nomb"), hmU);
                  }
                 
              }
          }
          int totMin=0,numReg=0;
          for (Map.Entry pair : hmP.entrySet())
          {
              ArrayList v = new ArrayList();
              v.add(pair.getKey());
              hmU = (Dimension) pair.getValue();
              if (hmU.getWidth() <= 1)
                  continue;
              v.add(hmU.getWidth());
              v.add(hmU.getHeight());
              jtUsu.addLinea(v);
              numReg+=hmU.getHeight();
              totMin += hmU.getWidth();
          }
          tit_numregE.setValorDec(numReg);
          tiemTotalE.setValorDec(totMin);
      } catch (SQLException k)
      {
          Error("Error al ver acumulados de pedidos",k);
      }
   }
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
      v.add(pro_codiE.getNombArtCli(dtCon1.getInt("pro_codi"),
                                           cli_codiE.getValorInt(), EU.em_cod, dtStat));
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
      v.add(false); // COnf
      v.add(dtCon1.getString("avp_canti"));            
      v.add(""); // NL
      jtLinPed.addLinea(v);
      if (pro_codiE.getLikeProd().getInt("pro_encaja")==0)
         kilosColgado+=dtCon1.getDouble("avp_canti");
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
        if (! ejecSelect)
          return true;
     swCliente=false;
     if (!cli_codiE.isNull())
         swCliente=true;
     s = "SELECT c.*,av.avc_id,av.avc_impres,av.cli_ruta, cl.cli_nomb,cl.cli_codrut, cl.cli_poble,"
         + " c.rut_codi, al.rut_nomb FROM pedvenc as c"
         + " left join v_albavec as av on c.avc_ano = av.avc_ano "
         + " and c.avc_serie= av.avc_serie and c.avc_nume =  av.avc_nume and av.emp_codi = c.emp_codi "
         + ",clientes as cl,v_rutas as al " +       
        " WHERE c.emp_codi = "+EU.em_cod+       
        " and c.pvc_confir = '"+pvc_confirE.getValor()+"' "+
        " and cl.cli_codi = c.cli_codi " +
        " and c.rut_codi = al.rut_codi "+     
        (sbe_codiE.getValorInt()==0?"":" and cl.sbe_codi = "+sbe_codiE.getValorInt())+
        (zon_codiE.isNull() || swCliente?"":" and cl.zon_codi = '"+zon_codiE.getText()+"'")+
        (rep_codiE.isNull() || swCliente?"":" and cl.rep_codi = '"+rep_codiE.getText()+"'");

    if (verPedidosE.getValor().equals("P"))
      s += " AND (c.avc_ano = 0 or pvc_cerra=0)";
    if (verPedidosE.getValor().equals("L"))
      s += " AND c.avc_ano != 0";
    if (!pvc_confirE.getValor().equals("*"))
      s += " and pvc_confir = '" + pvc_confirE.getValor() + "'";
    if (swCliente)
      s += " AND c.cli_codi = " + cli_codiE.getValorInt();
   
    s+="and  "+
            (albPedidC.getValor().equals("P")?"pvc_fecent":"avc_fecalb ")+
            " between to_date('" + pvc_feciniE.getText() + "','dd-MM-yyyy')" +       
        " and  to_date('" + pvc_fecfinE.getText()  + "','dd-MM-yyyy')" ;
    s += " order by c.rut_codi, c.pvc_fecent,c.cli_codi ";

    jtCabPed.setEnabled(false);
    jtCabPed.removeAllDatos();
//      debug("s: "+s);
    if (!dtCon1.select(s))
    {
      mensajeErr("NO hay PEDIDOS que cumplan estas condiciones");
      verPedidosE.requestFocus();
      return false;
    }
    return true;
  }
  private void confJTCab()
  {
    ArrayList v=new ArrayList();
    v.add("Usuario"); //0
    v.add("Tiempo"); //1
    v.add("Em"); // 2
    v.add("Eje."); // 3
    v.add("Num.");// 4
    v.add("Cliente"); // 5
    v.add("Nombre Cliente"); // 6
    v.add("Población"); // 7
    v.add("Fec.Entrega"); // 8
    v.add("C.Rep"); // 9
    v.add("Cerr");// 10
    v.add("Dep?"); // 11
    v.add("Ruta");// 12
    v.add("Ej.Alb");//13
    v.add("S.Alb"); //14
    v.add("Num.Alb"); // 15
    
    jtCabPed.setCabecera(v);
    ArrayList v1=new ArrayList();
    v1.add(tit_usunomE);
    v1.add(tit_tiempoE);
    for (int n=0;n<14;n++)
    {
        if (n==8)
        {
            CCheckBox cc=new CCheckBox();
            cc.setEnabled(false);            
            v1.add(cc);
        }
        else
        {
            CTextField tf=new CTextField();
            tf.setEnabled(false);
            v1.add(tf);
        }
    }
    try {
      jtCabPed.setCampos(v1);
    }  catch (Exception k)
    {
        msgBox(k.getMessage());
        Error("Error al establecer campos",k);
    }
    jtCabPed.setMaximumSize(new Dimension(548, 158));
    jtCabPed.setMinimumSize(new Dimension(548, 158));
    jtCabPed.setPreferredSize(new Dimension(548, 158));
    jtCabPed.setAnchoColumna(new int[]{80,30,26,40,49,55,150,100,76,40,40,40,100,40,40,60});
    jtCabPed.setAlinearColumna(new int[]{0,2,2,2,2,2,0,0,1,0,1,1,0,2,1,2});
    
    jtCabPed.setFormatoCampos();
    jtCabPed.setCanDeleteLinea(false);
    jtCabPed.setCanInsertLinea(false);
    
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
    public void setCliCodiText(String cliCodi)
    {
        cli_codiE.setText(cliCodi);
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
            guardaDatos(jtCabPed.getSelectedRow());
        inCambio=true;
        usuNomAnt=EU.usuario;
        if (! iniciarCons(true))
          return;
        boolean swServ=verPedidosE.getValor().equals("S"); // A servir (tienen albaran y no esta listado)
     
      
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
        if (swServ) 
        {      // Mostrar solo los disponibles para servir (tienen albaran y no estan listados)
                if (dtCon1.getObject("avc_impres")==null)
                    continue;
                if ((dtCon1.getInt("avc_impres") & 1) == 1)
                    continue;
        }
        boolean swImpres=false;
        if (!albListadoC.getValor().equals("*"))
        {                      
            if (dtCon1.getObject("avc_impres")!=null)               
                swImpres= (dtCon1.getInt("avc_impres") & 1) == 1;
            if (swImpres && albListadoC.getValor().equals("N"))
                continue;
            if (! swImpres && albListadoC.getValor().equals("S"))
                continue;
        }
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
             s="Select * from tiempostarea where tit_tipdoc = 'P' and tit_id="+pvcId+
                 " and tit_tiempo>0 "+
                 (usu_nombE.isNull(true)?"": " and usu_nomb ='"+usu_nombE.getText()+"'");
             if (!dtStat.select(s))
             {
                v.add("");
                v.add(0);  
             }
             else
             {
                v.add(dtStat.getString("usu_nomb"));
                v.add(dtStat.getInt("tit_tiempo"));
             }
        }
        if (!usu_nombE.isNull(true))
        {
            if (dtStat.getNOREG())
                continue;
//            if (! dtStat.getString("usu_nomb").equals(usu_nombE.getText()))
//                continue;
        }
        v.add(dtCon1.getString("emp_codi")); // 0
        v.add(dtCon1.getString("eje_nume")); // 1
        v.add(dtCon1.getString("pvc_nume")); // 2
        v.add(dtCon1.getString("cli_codi")); // 3
        v.add(dtCon1.getObject("pvc_clinom")==null?dtCon1.getString("cli_nomb"):dtCon1.getString("pvc_clinom")); // 4
        v.add(dtCon1.getObject("cli_poble")); // 5 
        v.add(dtCon1.getFecha("pvc_fecent","dd-MM-yyyy")); // 5
        v.add(dtCon1.getString("cli_codrut")); // 6
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
      actTotalGrid();
      verUsuPedido(0);
      
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

        tit_tiempoE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#99");
        tit_usunomE = new gnu.chu.controles.CTextField(Types.CHAR,"X",15);
        PPrinc = new gnu.chu.controles.CPanel();
        Pcabe = new gnu.chu.controles.CPanel();
        cLabel1 = new gnu.chu.controles.CLabel();
        cli_codiE = new gnu.chu.camposdb.cliPanel();
        cLabel5 = new gnu.chu.controles.CLabel();
        sbe_codiE = new gnu.chu.camposdb.sbePanel();
        cLabel6 = new gnu.chu.controles.CLabel();
        pvc_feciniE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel7 = new gnu.chu.controles.CLabel();
        pvc_fecfinE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        rep_codiE = new gnu.chu.controles.CLinkBox();
        zon_codiE = new gnu.chu.controles.CLinkBox();
        cLabel18 = new gnu.chu.controles.CLabel();
        cLabel9 = new gnu.chu.controles.CLabel();
        Baceptar = new gnu.chu.controles.CButton(Iconos.getImageIcon("check"));
        cLabel10 = new gnu.chu.controles.CLabel();
        verPedidosE = new gnu.chu.controles.CComboBox();
        pvc_confirE = new gnu.chu.controles.CComboBox();
        cLabel21 = new gnu.chu.controles.CLabel();
        cLabel2 = new gnu.chu.controles.CLabel();
        servRutaC = new gnu.chu.controles.CComboBox();
        cLabel3 = new gnu.chu.controles.CLabel();
        albListadoC = new gnu.chu.controles.CComboBox();
        cLabel22 = new gnu.chu.controles.CLabel();
        rut_codiE = new gnu.chu.controles.CLinkBox();
        albPedidC = new gnu.chu.controles.CComboBox();
        cLabel11 = new gnu.chu.controles.CLabel();
        usu_nombE = new gnu.chu.controles.CTextField(Types.CHAR,"X",15);
        Ppie = new gnu.chu.controles.CPanel();
        cLabel17 = new gnu.chu.controles.CLabel();
        nPedT = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9");
        pvc_fecpedE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        pvc_horpedE = new gnu.chu.controles.CTextField(Types.DECIMAL, "99.99");
        cLabel20 = new gnu.chu.controles.CLabel();
        scrollarea1 = new javax.swing.JScrollPane();
        pvc_comenE = new gnu.chu.controles.CTextArea();
        cLabel23 = new gnu.chu.controles.CLabel();
        cLabel24 = new gnu.chu.controles.CLabel();
        tiemTotalE = new gnu.chu.controles.CTextField(Types.DECIMAL, "###9");
        tit_numregE = new gnu.chu.controles.CTextField(Types.DECIMAL, "###9");
        jtUsu = new gnu.chu.controles.Cgrid(3);
        jtUsuPed = new gnu.chu.controles.Cgrid(2);
        Bborrar = new gnu.chu.controles.CButton(Iconos.getImageIcon("delete-row"));
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
        jtLinPed = new gnu.chu.controles.Cgrid(11);
        jtCabPed = new gnu.chu.controles.CGridEditable(16);

        tit_usunomE.setText("cTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        PPrinc.setLayout(new java.awt.GridBagLayout());

        Pcabe.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        Pcabe.setMaximumSize(new java.awt.Dimension(725, 85));
        Pcabe.setMinimumSize(new java.awt.Dimension(725, 85));
        Pcabe.setName(""); // NOI18N
        Pcabe.setPreferredSize(new java.awt.Dimension(725, 85));
        Pcabe.setLayout(null);

        cLabel1.setText("Ver Pedidos");
        Pcabe.add(cLabel1);
        cLabel1.setBounds(2, 1, 70, 18);
        Pcabe.add(cli_codiE);
        cli_codiE.setBounds(80, 22, 280, 18);

        cLabel5.setText("Delegación");
        Pcabe.add(cLabel5);
        cLabel5.setBounds(370, 22, 70, 18);
        Pcabe.add(sbe_codiE);
        sbe_codiE.setBounds(440, 22, 37, 18);

        cLabel6.setText("De Fecha");
        Pcabe.add(cLabel6);
        cLabel6.setBounds(200, 1, 49, 18);
        Pcabe.add(pvc_feciniE);
        pvc_feciniE.setBounds(260, 1, 76, 18);

        cLabel7.setText("A Fecha");
        Pcabe.add(cLabel7);
        cLabel7.setBounds(340, 1, 43, 18);
        Pcabe.add(pvc_fecfinE);
        pvc_fecfinE.setBounds(390, 1, 75, 18);

        rep_codiE.setAncTexto(30);
        rep_codiE.setMayusculas(true);
        rep_codiE.setPreferredSize(new java.awt.Dimension(152, 18));
        Pcabe.add(rep_codiE);
        rep_codiE.setBounds(60, 42, 190, 18);

        zon_codiE.setAncTexto(30);
        zon_codiE.setMayusculas(true);
        zon_codiE.setPreferredSize(new java.awt.Dimension(152, 18));
        Pcabe.add(zon_codiE);
        zon_codiE.setBounds(310, 42, 280, 18);

        cLabel18.setText("Zona");
        cLabel18.setPreferredSize(new java.awt.Dimension(60, 18));
        Pcabe.add(cLabel18);
        cLabel18.setBounds(270, 42, 40, 18);

        cLabel9.setText("Usuario");
        Pcabe.add(cLabel9);
        cLabel9.setBounds(490, 20, 50, 18);

        Baceptar.setText("Aceptar");
        Pcabe.add(Baceptar);
        Baceptar.setBounds(610, 50, 100, 30);

        cLabel10.setText("De Cliente");
        Pcabe.add(cLabel10);
        cLabel10.setBounds(5, 22, 70, 18);

        verPedidosE.addItem("Pendientes","P");
        verPedidosE.addItem("A servir","S");
        verPedidosE.addItem("Preparados","L");
        verPedidosE.addItem("Todos","T");
        Pcabe.add(verPedidosE);
        verPedidosE.setBounds(80, 1, 110, 18);

        pvc_confirE.addItem("Si","S");
        pvc_confirE.addItem("No","N");
        pvc_confirE.addItem("**","*");
        Pcabe.add(pvc_confirE);
        pvc_confirE.setBounds(540, 1, 50, 18);

        cLabel21.setText("Repres.");
        cLabel21.setPreferredSize(new java.awt.Dimension(60, 18));
        Pcabe.add(cLabel21);
        cLabel21.setBounds(5, 42, 50, 18);

        cLabel2.setText("Servidos en Ruta ");
        Pcabe.add(cLabel2);
        cLabel2.setBounds(2, 65, 100, 17);

        servRutaC.addItem("**","*");
        servRutaC.addItem("Si","S");
        servRutaC.addItem("No","N");
        Pcabe.add(servRutaC);
        servRutaC.setBounds(100, 65, 50, 17);

        cLabel3.setText("Listados Albaran ");
        Pcabe.add(cLabel3);
        cLabel3.setBounds(190, 65, 100, 17);

        albListadoC.addItem("**","*");
        albListadoC.addItem("Si","S");
        albListadoC.addItem("No","N");
        Pcabe.add(albListadoC);
        albListadoC.setBounds(290, 65, 50, 17);

        cLabel22.setText("Ruta");
        cLabel22.setPreferredSize(new java.awt.Dimension(60, 18));
        Pcabe.add(cLabel22);
        cLabel22.setBounds(350, 65, 40, 18);

        rut_codiE.setFormato(Types.CHAR,"X",2);
        rut_codiE.setAncTexto(30);
        rut_codiE.setMayusculas(true);
        rut_codiE.setPreferredSize(new java.awt.Dimension(152, 18));
        Pcabe.add(rut_codiE);
        rut_codiE.setBounds(390, 65, 200, 18);

        albPedidC.addItem("Pedidos","P");
        albPedidC.addItem("Albaran","A");
        Pcabe.add(albPedidC);
        albPedidC.setBounds(620, 2, 90, 17);

        cLabel11.setText("Confirmado");
        Pcabe.add(cLabel11);
        cLabel11.setBounds(470, 1, 70, 18);
        Pcabe.add(usu_nombE);
        usu_nombE.setBounds(540, 20, 120, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        PPrinc.add(Pcabe, gridBagConstraints);

        Ppie.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Ppie.setMaximumSize(new java.awt.Dimension(705, 130));
        Ppie.setMinimumSize(new java.awt.Dimension(705, 130));
        Ppie.setPreferredSize(new java.awt.Dimension(705, 130));
        Ppie.setLayout(null);

        cLabel17.setText("N.Docs");
        cLabel17.setPreferredSize(new java.awt.Dimension(60, 18));
        Ppie.add(cLabel17);
        cLabel17.setBounds(130, 70, 50, 18);

        nPedT.setEnabled(false);
        Ppie.add(nPedT);
        nPedT.setBounds(660, 110, 30, 18);

        pvc_fecpedE.setEnabled(false);
        Ppie.add(pvc_fecpedE);
        pvc_fecpedE.setBounds(570, 80, 70, 18);

        pvc_horpedE.setEnabled(false);
        Ppie.add(pvc_horpedE);
        pvc_horpedE.setBounds(650, 80, 40, 18);

        cLabel20.setText("Minutos");
        cLabel20.setPreferredSize(new java.awt.Dimension(60, 18));
        Ppie.add(cLabel20);
        cLabel20.setBounds(10, 70, 60, 18);

        pvc_comenE.setColumns(20);
        pvc_comenE.setRows(5);
        scrollarea1.setViewportView(pvc_comenE);

        Ppie.add(scrollarea1);
        scrollarea1.setBounds(10, 0, 210, 60);

        cLabel23.setText("Total Pedidos ");
        cLabel23.setPreferredSize(new java.awt.Dimension(60, 18));
        Ppie.add(cLabel23);
        cLabel23.setBounds(570, 110, 80, 18);

        cLabel24.setText("Fecha Ped.");
        cLabel24.setPreferredSize(new java.awt.Dimension(60, 18));
        Ppie.add(cLabel24);
        cLabel24.setBounds(510, 80, 70, 18);

        tiemTotalE.setEnabled(false);
        Ppie.add(tiemTotalE);
        tiemTotalE.setBounds(70, 70, 40, 18);

        tit_numregE.setEnabled(false);
        Ppie.add(tit_numregE);
        tit_numregE.setBounds(180, 70, 40, 18);

        ArrayList vu=new ArrayList();
        vu.add("Usuario");
        vu.add("Min.");
        vu.add("Ped");
        jtUsu.setCabecera(vu);
        jtUsu.setAnchoColumna(new int[]{100,40,40});

        jtUsu.setAjustarColumnas(true);
        jtUsu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtUsu.setBuscarVisible(false);
        Ppie.add(jtUsu);
        jtUsu.setBounds(230, 0, 270, 90);

        ArrayList vu1=new ArrayList();
        vu1.add("Usuario");
        vu1.add("Min.");

        jtUsuPed.setCabecera(vu1);
        jtUsuPed.setAnchoColumna(new int[]{100,40});

        jtUsuPed.setAjustarColumnas(true);
        jtUsuPed.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtUsuPed.setBuscarVisible(false);
        Ppie.add(jtUsuPed);
        jtUsuPed.setBounds(500, 0, 170, 80);

        Bborrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BborrarActionPerformed(evt);
            }
        });
        Ppie.add(Bborrar);
        Bborrar.setBounds(670, 30, 30, 30);

        cLabel25.setText("Kilos");
        cLabel25.setPreferredSize(new java.awt.Dimension(60, 18));
        Ppie.add(cLabel25);
        cLabel25.setBounds(70, 90, 40, 18);

        kilosPedidE.setEnabled(false);
        Ppie.add(kilosPedidE);
        kilosPedidE.setBounds(110, 90, 40, 18);

        cLabel26.setText("Kg. Cajas");
        cLabel26.setPreferredSize(new java.awt.Dimension(60, 18));
        Ppie.add(cLabel26);
        cLabel26.setBounds(160, 90, 70, 18);

        kilosCajasE.setEnabled(false);
        Ppie.add(kilosCajasE);
        kilosCajasE.setBounds(230, 90, 40, 18);

        cLabel27.setText("Kg. Colgado");
        cLabel27.setPreferredSize(new java.awt.Dimension(60, 18));
        Ppie.add(cLabel27);
        cLabel27.setBounds(280, 90, 70, 18);

        kilosColgadoE.setEnabled(false);
        Ppie.add(kilosColgadoE);
        kilosColgadoE.setBounds(350, 90, 35, 18);

        cLabel28.setBackground(java.awt.Color.orange);
        cLabel28.setText("Pedido");
        cLabel28.setOpaque(true);
        cLabel28.setPreferredSize(new java.awt.Dimension(60, 18));
        Ppie.add(cLabel28);
        cLabel28.setBounds(10, 90, 50, 18);

        kilosAlbarE.setEnabled(false);
        Ppie.add(kilosAlbarE);
        kilosAlbarE.setBounds(110, 110, 40, 18);

        cLabel29.setText("Kg. Cajas");
        cLabel29.setPreferredSize(new java.awt.Dimension(60, 18));
        Ppie.add(cLabel29);
        cLabel29.setBounds(160, 110, 70, 18);

        kilosCajasAlbE.setEnabled(false);
        Ppie.add(kilosCajasAlbE);
        kilosCajasAlbE.setBounds(230, 110, 40, 18);

        cLabel30.setText("Kg. Colgado");
        cLabel30.setPreferredSize(new java.awt.Dimension(60, 18));
        Ppie.add(cLabel30);
        cLabel30.setBounds(280, 110, 70, 18);

        kilosColgadoAlbE.setEnabled(false);
        Ppie.add(kilosColgadoAlbE);
        kilosColgadoAlbE.setBounds(350, 110, 35, 18);

        cLabel31.setText("Kilos");
        cLabel31.setPreferredSize(new java.awt.Dimension(60, 18));
        Ppie.add(cLabel31);
        cLabel31.setBounds(70, 110, 40, 18);

        cLabel32.setBackground(java.awt.Color.orange);
        cLabel32.setText("Albaran");
        cLabel32.setOpaque(true);
        cLabel32.setPreferredSize(new java.awt.Dimension(60, 18));
        Ppie.add(cLabel32);
        cLabel32.setBounds(10, 110, 50, 18);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        PPrinc.add(Ppie, gridBagConstraints);

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

        try {confJTCab();} catch (Exception k){Error("Error al configurar grid cabecera",k);}
        jtCabPed.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtCabPed.setMaximumSize(new java.awt.Dimension(681, 110));
        jtCabPed.setMinimumSize(new java.awt.Dimension(681, 110));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        PPrinc.add(jtCabPed, gridBagConstraints);

        getContentPane().add(PPrinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BborrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BborrarActionPerformed
       try
       {
           if (jtUsuPed.isVacio())
               return;
            int pvcId=pdpeve.getIdPedido(dtStat,jtCabPed.getValorInt(JTCAB_EMPPED),
                jtCabPed.getValorInt(JTCAB_EJEPED),jtCabPed.getValorInt(JTCAB_NUMPED));
            if ( pvcId<0)
                throw new SQLException("Error al Buscar pedido "+jtCabPed.getValorInt(JTCAB_EMPPED)+
                    "-"+jtCabPed.getValorInt(JTCAB_EJEPED)+"-"+jtCabPed.getValorInt(JTCAB_NUMPED));
            
           dtAdd.executeUpdate("delete from tiempostarea where tit_tipdoc = 'P' and tit_id="+pvcId+
                " and usu_nomb='"+jtUsuPed.getValString(0)+"'");
           dtAdd.commit();
           jtUsuPed.removeLinea();
       } catch (SQLException k)
       {
           
       }
    }//GEN-LAST:event_BborrarActionPerformed

   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CButton Bborrar;
    private gnu.chu.controles.CPanel PPrinc;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Ppie;
    private gnu.chu.controles.CComboBox albListadoC;
    private gnu.chu.controles.CComboBox albPedidC;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel10;
    private gnu.chu.controles.CLabel cLabel11;
    private gnu.chu.controles.CLabel cLabel17;
    private gnu.chu.controles.CLabel cLabel18;
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
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel9;
    private gnu.chu.camposdb.cliPanel cli_codiE;
    private gnu.chu.controles.CGridEditable jtCabPed;
    private gnu.chu.controles.Cgrid jtLinPed;
    private gnu.chu.controles.Cgrid jtUsu;
    private gnu.chu.controles.Cgrid jtUsuPed;
    private gnu.chu.controles.CTextField kilosAlbarE;
    private gnu.chu.controles.CTextField kilosCajasAlbE;
    private gnu.chu.controles.CTextField kilosCajasE;
    private gnu.chu.controles.CTextField kilosColgadoAlbE;
    private gnu.chu.controles.CTextField kilosColgadoE;
    private gnu.chu.controles.CTextField kilosPedidE;
    private gnu.chu.controles.CTextField nPedT;
    private gnu.chu.controles.CTextArea pvc_comenE;
    private gnu.chu.controles.CComboBox pvc_confirE;
    private gnu.chu.controles.CTextField pvc_fecfinE;
    private gnu.chu.controles.CTextField pvc_feciniE;
    private gnu.chu.controles.CTextField pvc_fecpedE;
    private gnu.chu.controles.CTextField pvc_horpedE;
    private gnu.chu.controles.CLinkBox rep_codiE;
    private gnu.chu.controles.CLinkBox rut_codiE;
    private gnu.chu.camposdb.sbePanel sbe_codiE;
    private javax.swing.JScrollPane scrollarea1;
    private gnu.chu.controles.CComboBox servRutaC;
    private gnu.chu.controles.CTextField tiemTotalE;
    private gnu.chu.controles.CTextField tit_numregE;
    private gnu.chu.controles.CTextField tit_tiempoE;
    private gnu.chu.controles.CTextField tit_usunomE;
    private gnu.chu.controles.CTextField usu_nombE;
    private gnu.chu.controles.CComboBox verPedidosE;
    private gnu.chu.controles.CLinkBox zon_codiE;
    // End of variables declaration//GEN-END:variables

   
}
