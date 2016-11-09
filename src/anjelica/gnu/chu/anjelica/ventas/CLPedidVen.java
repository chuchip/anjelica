/*
 *<p>Titulo: CLVenRep </p>
 * <p>Descripción: Consulta Listado Ventas a Representantes</p>
 * Este programa saca los margenes sobre el precio de tarifa entre unas fechas
 * y para una zona/Representante dada.
 * Tambien permite sacar una relacion de los albaranes, que no tienen precio de tarifa
 * puestos, dandoz< la opción de actualizarlos.
 * Created on 03-dic-2009, 22:41:09
 *
 * <p>Copyright: Copyright (c) 2005-2016
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
package gnu.chu.anjelica.ventas;

import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.almacen.pdalmace;
import gnu.chu.anjelica.listados.Listados;
import gnu.chu.anjelica.pad.MantRepres;
import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.camposdb.proPanel;
import gnu.chu.camposdb.prvPanel;
import gnu.chu.controles.StatusBar;
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
import java.awt.print.PrinterException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;


public class CLPedidVen extends  ventana   implements  JRDataSource
{
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
    private final int JTCAB_EMPPED=0;
    private final int JTCAB_EJEPED=1;
    private final int JTCAB_NUMPED=2;
    private final int JTCAB_SERALB=12;
    private final int JTCAB_NUMALB= 13;   
    private final int JTCAB_EJEALB=11;
    private final int JTCAB_NOMCLI=4;
//    private final int JTCAB_POBCLI=5;
    
    public CLPedidVen(EntornoUsuario eu, Principal p) {
        this(eu, p, null);
    }

    public CLPedidVen(EntornoUsuario eu, Principal p, HashMap<String,String> ht) {
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
            setTitulo("Cons/List.  Pedidos Ventas");
            if (jf.gestor.apuntar(this)) {
                jbInit();
            } else {
                setErrorInit(true);
            }
        } catch (Exception e) {
           ErrorInit(e);
        }
    }

    public CLPedidVen(gnu.chu.anjelica.menu p, EntornoUsuario eu, HashMap <String,String> ht) {
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
             setTitulo("Cons/List.  Pedidos de Ventas");

            jbInit();
        } catch (Exception e) {
           ErrorInit(e);
        }
    }
    public CLPedidVen(ventana papa) throws Exception
    {
    padre=papa;
    dtStat=padre.dtStat;
    dtCon1=padre.dtCon1;
    vl=padre.vl;
    jf=padre.jf;

    EU=padre.EU;
    setTitulo("Cons/List. Pedidos de Ventas");

    jbInit();
    }
    private void jbInit() throws Exception {
        statusBar = new StatusBar(this);

        iniciarFrame();

        this.setVersion("2016-09-28");

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
     pdalmace.llenaLinkBox(alm_codiE, dtStat);
     
     
     sbe_codiE.iniciar(dtStat, this, vl, EU);
     
     sbe_codiE.setAceptaNulo(true);
     sbe_codiE.setValorInt(0);
     pvc_feciniE.setText(Formatear.sumaDias(Formatear.getDateAct(), -7));
     pvc_fecfinE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
      activarEventos();
    }
    void activarEventos()
    {
        Bimpri.addActionListener(new ActionListener()
        {
          @Override
          public void actionPerformed(ActionEvent e)
          {
            Bimpri_actionPerformed(e.getActionCommand());
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
      jtCabPed.addListSelectionListener(new ListSelectionListener()
     {
      @Override
      public void valueChanged(ListSelectionEvent e)
      {
        if (e.getValueIsAdjusting() || ! jtCabPed.isEnabled() || jtCabPed.isVacio() ) // && e.getFirstIndex() == e.getLastIndex())
          return;
        verDatPed(jtCabPed.getValorInt(0),jtCabPed.getValorInt(1),jtCabPed.getValorInt(2));
//      System.out.println(" Row "+getValString(0,5)+ " - "+getValString(1,5));

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
        cm.setEmpresaPedido(jtCabPed.getValorInt(JTCAB_EMPPED));

        cm.ej_query();
        jf.gestor.ir(cm);
    } 
    public String getCliNomb()
    {
        return jtCabPed.getValString(JTCAB_NOMCLI);
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
     
       usu_nombE.setText(dtCon1.getString("usu_nomb"));
       pvc_fecpedE.setText(dtCon1.getFecha("pvc_fecped"));
       pvc_horpedE.setText(dtCon1.getFecha("pvc_fecped","hh.mm"));
       pvc_comenE.setText(dtCon1.getString("pvc_comen"));
       pvc_impresE.setSelecion(dtCon1.getString("pvc_impres"));
       pvc_nupeclE.setText(dtCon1.getString("cli_pobl"));
       pvc_nupeclE.setText(dtCon1.getString("pvc_nupecl"));
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
       } while (dtCon1.next());
       if (jtCabPed.getValorInt(JTCAB_EJEALB)!=0)
       {
           verDatAlbaranPed(empCodi,jtCabPed.getValorInt(JTCAB_EJEALB),jtCabPed.getValString(JTCAB_SERALB),
               jtCabPed.getValorInt(JTCAB_NUMALB) );
       }
//       actAcumJT();
     } catch (Exception k)
     {
       Error("Error al Ver datos de pedido",k);
     }
   }
    
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
    } while (dtCon1.next());
    
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
   
   void Bimpri_actionPerformed(String accion)
   {
      if (jtCabPed.isVacio())
      {
          msgBox("No hay pedidos para listar");
          return;
      }
      if (accion.startsWith("P"))
      {
        imprImpreso();
        return;
      }
      try {
        swImpreso=false;
        java.util.HashMap mp = Listados.getHashMapDefault();
        mp.put("fecini",pvc_feciniE.getDate());
        mp.put("fecfin",pvc_fecfinE.getDate());
//        mp.put("cli_zonrep",cli_zonrepE.getText());
//        mp.put("cli_zoncre",cli_zoncreE.getText());
        JasperReport jr;
        jr =  Listados.getJasperReport(EU, "relpedven");

        ResultSet rs;
        nLineaReport=0;
//        rs=dtCon1.getStatement().executeQuery(dtCon1.getStrSelect());

        JasperPrint jp = JasperFillManager.fillReport(jr, mp,this);
        gnu.chu.print.util.printJasper(jp, EU);

         mensajeErr("Relacion Pedido Ventas ... IMPRESO ");
      }
      catch (ParseException | JRException | PrinterException k)
      {
        Error("Error al imprimir Pedido Venta", k);
      }
    }
   
    @Override
     public boolean next() throws JRException
     {
        try
        {
        
           if (nLineaReport==0 || !dtCon1.next())
           {
               if (nLineaReport>=jtCabPed.getRowCount())
                return false;
               if (swImpreso)
               {
                   s = "select l.*,p.prv_nomb ,c.cli_codi,c.alm_codi,c.pvc_fecped, "
                       + " c.pvc_fecent,c.pvc_clinom,c.usu_nomb,c.pvc_comen,al.alm_nomb, "
                       + " a.pro_nomb, cl.cli_nomb,cl.cli_pobl "
                       + " from pedvenl as l left join v_proveedo p on  p.prv_codi = l.prv_codi, "
                       + " pedvenc as c,v_articulo as a,v_almacen as al,clientes as cl "
                       + " where  c.emp_codi = l.emp_codi "
                       + " and c.eje_nume = l.eje_nume "
                       + " and c.pvc_nume = l.pvc_nume "
                       + " and l.pro_codi = a.pro_codi "
                       + " and al.alm_codi = c.alm_codi "
                       + " and c.cli_codi = cl.cli_codi "
                       + " and c.alm_codi = al.alm_codi "
                       + " and c.emp_codi =  " + jtCabPed.getValorInt(nLineaReport, JTCAB_EMPPED)
                       + " and C.cli_codi = cl.cli_codi "
                       + " AND C.eje_nume = " + jtCabPed.getValorInt(nLineaReport, JTCAB_EJEPED)
                       + " and C.pvc_nume = " + jtCabPed.getValorInt(nLineaReport, JTCAB_NUMPED)
                       + " order by pvl_numlin ";
               } 
               else
               {
                   s = "SELECT c.*, cl.cli_nomb,cl.cli_poble"
                       + "  FROM pedvenc as c,v_cliente as cl "
                       + " WHERE c.emp_codi =  " + jtCabPed.getValorInt(nLineaReport, JTCAB_EMPPED)
                       + " and C.cli_codi = cl.cli_codi "
                       + " AND C.eje_nume = " + jtCabPed.getValorInt(nLineaReport, JTCAB_EJEPED)
                       + " and C.pvc_nume = " + jtCabPed.getValorInt(nLineaReport, JTCAB_NUMPED);
               }
               if (!dtCon1.select(s))
               {
                   throw new JRException("Error al localizar pedido para listar. Linea: "
                       + nLineaReport + " Pedido: " + jtCabPed.getValorInt(nLineaReport, JTCAB_NUMPED));
               }               
               nLineaReport++;

           }
           return true;
        } catch (SQLException ex)
        {
            throw new JRException("Error al buscar pedido a listar",ex);
        }
     }
  
    void imprImpreso()
    {
      try
      {
        swImpreso=true;
       

        java.util.HashMap mp =Listados.getHashMapDefault();
        JasperReport jr;
        jr = Listados.getJasperReport(EU, "pedventas");

      
        nLineaReport=0;

        JasperPrint jp = JasperFillManager.fillReport(jr, mp,this);
        gnu.chu.print.util.printJasper(jp, EU);

        mensajeErr("Relacion Pedido Ventas ... IMPRESO ");
//        dtCon1.select(s);
//        do
//        {
//          s = "update PEDVENC SET pvc_impres = 'S' WHERE emp_codi = " + dtCon1.getInt("emp_codi")+
//            " and eje_nume = " + dtCon1.getInt("eje_nume")+
//            " and pvc_nume = " +dtCon1.getInt("pvc_nume");
//          stUp.executeUpdate(s);
//        } while (dtCon1.next());
//        ctUp.commit();
        mensajeErr("Pedido Ventas ... IMPRESO ");
      }
      catch (JRException | PrinterException k)
      {
        Error("Error al imprimir Pedido Venta", k);
      }
    }
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
     s = "SELECT c.*,av.avc_id,av.avc_impres,av.cli_ruta, cl.cli_nomb,cl.cli_poble,"
         + " cl.rut_codi, al.rut_nomb FROM pedvenc as c"
         + " left join v_albavec as av on c.avc_ano = av.avc_ano "
         + " and c.avc_serie= av.avc_serie and c.avc_nume =  av.avc_nume "
         + ",clientes as cl,v_rutas as al " +       
        " WHERE pvc_fecent between to_date('" + pvc_feciniE.getText() + "','dd-MM-yyyy')" +
        " and  to_date('" + pvc_fecfinE.getText()  + "','dd-MM-yyyy')" +
        " and c.alm_codi >= " + alm_codiE.getValorInt() +
        " and c.pvc_confir = 'S' "+
        " and cl.cli_codi = c.cli_codi " +
        " and cl.rut_codi = al.rut_codi "+     
        (sbe_codiE.getValorInt()==0?"":" and cl.sbe_codi = "+sbe_codiE.getValorInt())+
        (zon_codiE.isNull() || swCliente?"":" and cl.zon_codi = '"+zon_codiE.getText()+"'")+
        (rep_codiE.isNull() || swCliente?"":" and cl.rep_codi = '"+rep_codiE.getText()+"'");
;

    if (verPedidosE.getValor().equals("P"))
      s += " AND (c.avc_ano = 0 or pvc_cerra=0)";
    if (verPedidosE.getValor().equals("L"))
      s += " AND c.avc_ano != 0";
    if (!pvc_confirE.getValor().equals("*"))
      s += " and pvc_confir = '" + pvc_confirE.getValor() + "'";
    if (swCliente)
      s += " AND c.cli_codi = " + cli_codiE.getValorInt();
    if (! pvc_listadoE.getValor().equals("*"))
      s+=" AND c.pvc_impres = '"+pvc_listadoE.getValor()+"'";
    s += " order by c.pvc_fecent,c.cli_codi ";

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
    v.add("Em"); // 0
    v.add("Eje."); // 1
    v.add("Num.");// 2
    v.add("Cliente"); // 3
    v.add("Nombre Cliente"); // 4
    v.add("Población"); // 5
    v.add("Fec.Entrega"); // 6
    v.add("Conf"); // 7
    v.add("Cerr");// 8
    v.add("Dep?"); // 9
    v.add("Ruta");// 10
    v.add("Ej.Alb");//11
    v.add("S.Alb"); //12
    v.add("Num.Alb"); //13
    jtCabPed.setCabecera(v);
    jtCabPed.setMaximumSize(new Dimension(548, 158));
    jtCabPed.setMinimumSize(new Dimension(548, 158));
    jtCabPed.setPreferredSize(new Dimension(548, 158));
    jtCabPed.setAnchoColumna(new int[]{26,40,49,55,150,100,76,30,40,40,100,40,40,60});
    jtCabPed.setAlinearColumna(new int[]{2,2,2,2,0,0,1,1,1,1,0,2,1,2});

    jtCabPed.setFormatoColumna(7,"BSN");
    jtCabPed.setFormatoColumna(8,"BSN");
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
      verDatPed(jtCabPed.getValorInt(0),jtCabPed.getValorInt(1),jtCabPed.getValorInt(2));
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
        cLabel16 = new gnu.chu.controles.CLabel();
        rep_codiE = new gnu.chu.controles.CLinkBox();
        zon_codiE = new gnu.chu.controles.CLinkBox();
        cLabel18 = new gnu.chu.controles.CLabel();
        cLabel9 = new gnu.chu.controles.CLabel();
        Baceptar = new gnu.chu.controles.CButton(Iconos.getImageIcon("check"));
        cLabel10 = new gnu.chu.controles.CLabel();
        verPedidosE = new gnu.chu.controles.CComboBox();
        pvc_confirE = new gnu.chu.controles.CComboBox();
        cLabel12 = new gnu.chu.controles.CLabel();
        pvc_listadoE = new gnu.chu.controles.CComboBox();
        cLabel21 = new gnu.chu.controles.CLabel();
        alm_codiE = new gnu.chu.controles.CLinkBox();
        cLabel2 = new gnu.chu.controles.CLabel();
        servRutaC = new gnu.chu.controles.CComboBox();
        cLabel3 = new gnu.chu.controles.CLabel();
        albListadoC = new gnu.chu.controles.CComboBox();
        cLabel22 = new gnu.chu.controles.CLabel();
        rut_codiE = new gnu.chu.controles.CLinkBox();
        jtLinPed = new gnu.chu.controles.Cgrid(11);
        jtCabPed = new gnu.chu.controles.Cgrid(14);
        Ppie = new gnu.chu.controles.CPanel();
        cLabel17 = new gnu.chu.controles.CLabel();
        nPedT = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9");
        cLabel19 = new gnu.chu.controles.CLabel();
        pvc_fecpedE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        pvc_horpedE = new gnu.chu.controles.CTextField(Types.DECIMAL, "99.99");
        cLabel20 = new gnu.chu.controles.CLabel();
        usu_nombE = new gnu.chu.controles.CTextField();
        pvc_impresE = new gnu.chu.controles.CCheckBox();
        scrollarea1 = new javax.swing.JScrollPane();
        pvc_comenE = new gnu.chu.controles.CTextArea();
        Bimpri = new gnu.chu.controles.CButtonMenu();
        cLabel8 = new gnu.chu.controles.CLabel();
        pvc_nupeclE = new gnu.chu.controles.CTextField();
        cLabel23 = new gnu.chu.controles.CLabel();
        cli_poblE1 = new gnu.chu.controles.CTextField();

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

        cLabel16.setText("Alm");
        cLabel16.setPreferredSize(new java.awt.Dimension(60, 18));
        Pcabe.add(cLabel16);
        cLabel16.setBounds(480, 22, 30, 18);

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

        cLabel9.setText("Confirmado");
        Pcabe.add(cLabel9);
        cLabel9.setBounds(470, 1, 70, 18);

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

        cLabel12.setText("Listado");
        Pcabe.add(cLabel12);
        cLabel12.setBounds(610, 1, 50, 18);

        pvc_listadoE.addItem("**","*");
        pvc_listadoE.addItem("Si","S");
        pvc_listadoE.addItem("No","N");
        Pcabe.add(pvc_listadoE);
        pvc_listadoE.setBounds(660, 1, 50, 18);

        cLabel21.setText("Repres.");
        cLabel21.setPreferredSize(new java.awt.Dimension(60, 18));
        Pcabe.add(cLabel21);
        cLabel21.setBounds(5, 42, 50, 18);

        alm_codiE.setAncTexto(25);
        Pcabe.add(alm_codiE);
        alm_codiE.setBounds(510, 22, 200, 17);

        cLabel2.setText("Servidos en Ruta ");
        Pcabe.add(cLabel2);
        cLabel2.setBounds(10, 65, 100, 17);

        servRutaC.addItem("**","*");
        servRutaC.addItem("Si","S");
        servRutaC.addItem("No","N");
        Pcabe.add(servRutaC);
        servRutaC.setBounds(110, 65, 50, 17);

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

        rut_codiE.setAncTexto(30);
        rut_codiE.setMayusculas(true);
        rut_codiE.setPreferredSize(new java.awt.Dimension(152, 18));
        Pcabe.add(rut_codiE);
        rut_codiE.setBounds(390, 65, 200, 18);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        PPrinc.add(Pcabe, gridBagConstraints);

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

        Ppie.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Ppie.setMaximumSize(new java.awt.Dimension(650, 60));
        Ppie.setMinimumSize(new java.awt.Dimension(650, 60));
        Ppie.setPreferredSize(new java.awt.Dimension(650, 60));
        Ppie.setLayout(null);

        cLabel17.setText("Usuario");
        cLabel17.setPreferredSize(new java.awt.Dimension(60, 18));
        Ppie.add(cLabel17);
        cLabel17.setBounds(440, 20, 50, 18);

        nPedT.setEnabled(false);
        Ppie.add(nPedT);
        nPedT.setBounds(305, 1, 30, 18);

        cLabel19.setText("Ped.Cliente ");
        cLabel19.setPreferredSize(new java.awt.Dimension(60, 18));
        Ppie.add(cLabel19);
        cLabel19.setBounds(343, 1, 70, 17);

        pvc_fecpedE.setEnabled(false);
        Ppie.add(pvc_fecpedE);
        pvc_fecpedE.setBounds(305, 20, 76, 18);

        pvc_horpedE.setEnabled(false);
        Ppie.add(pvc_horpedE);
        pvc_horpedE.setBounds(383, 20, 40, 18);

        cLabel20.setText("Fecha Pedido ");
        cLabel20.setPreferredSize(new java.awt.Dimension(60, 18));
        Ppie.add(cLabel20);
        cLabel20.setBounds(225, 20, 80, 18);

        usu_nombE.setEnabled(false);
        Ppie.add(usu_nombE);
        usu_nombE.setBounds(490, 20, 110, 18);

        pvc_impresE.setText("Listado ");
        pvc_impresE.setEnabled(false);
        pvc_impresE.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        Ppie.add(pvc_impresE);
        pvc_impresE.setBounds(225, 40, 70, 17);

        pvc_comenE.setColumns(20);
        pvc_comenE.setRows(5);
        scrollarea1.setViewportView(pvc_comenE);

        Ppie.add(scrollarea1);
        scrollarea1.setBounds(10, 0, 210, 55);

        Bimpri.setText("Listar");
        Bimpri.addMenu("Relacion", "R");
        Bimpri.addMenu("Pedidos", "P");
        Ppie.add(Bimpri);
        Bimpri.setBounds(520, 1, 80, 20);

        cLabel8.setText("Poblacion");
        Ppie.add(cLabel8);
        cLabel8.setBounds(310, 40, 60, 15);

        pvc_nupeclE.setEnabled(false);
        Ppie.add(pvc_nupeclE);
        pvc_nupeclE.setBounds(410, 1, 100, 17);

        cLabel23.setText("Total Pedidos ");
        cLabel23.setPreferredSize(new java.awt.Dimension(60, 18));
        Ppie.add(cLabel23);
        cLabel23.setBounds(225, 1, 80, 18);

        cli_poblE1.setEnabled(false);
        Ppie.add(cli_poblE1);
        cli_poblE1.setBounds(370, 40, 230, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        PPrinc.add(Ppie, gridBagConstraints);

        getContentPane().add(PPrinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CButtonMenu Bimpri;
    private gnu.chu.controles.CPanel PPrinc;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Ppie;
    private gnu.chu.controles.CComboBox albListadoC;
    private gnu.chu.controles.CLinkBox alm_codiE;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel10;
    private gnu.chu.controles.CLabel cLabel12;
    private gnu.chu.controles.CLabel cLabel16;
    private gnu.chu.controles.CLabel cLabel17;
    private gnu.chu.controles.CLabel cLabel18;
    private gnu.chu.controles.CLabel cLabel19;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel20;
    private gnu.chu.controles.CLabel cLabel21;
    private gnu.chu.controles.CLabel cLabel22;
    private gnu.chu.controles.CLabel cLabel23;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.controles.CLabel cLabel9;
    private gnu.chu.camposdb.cliPanel cli_codiE;
    private gnu.chu.controles.CTextField cli_poblE1;
    private gnu.chu.controles.Cgrid jtCabPed;
    private gnu.chu.controles.Cgrid jtLinPed;
    private gnu.chu.controles.CTextField nPedT;
    private gnu.chu.controles.CTextArea pvc_comenE;
    private gnu.chu.controles.CComboBox pvc_confirE;
    private gnu.chu.controles.CTextField pvc_fecfinE;
    private gnu.chu.controles.CTextField pvc_feciniE;
    private gnu.chu.controles.CTextField pvc_fecpedE;
    private gnu.chu.controles.CTextField pvc_horpedE;
    private gnu.chu.controles.CCheckBox pvc_impresE;
    private gnu.chu.controles.CComboBox pvc_listadoE;
    private gnu.chu.controles.CTextField pvc_nupeclE;
    private gnu.chu.controles.CLinkBox rep_codiE;
    private gnu.chu.controles.CLinkBox rut_codiE;
    private gnu.chu.camposdb.sbePanel sbe_codiE;
    private javax.swing.JScrollPane scrollarea1;
    private gnu.chu.controles.CComboBox servRutaC;
    private gnu.chu.controles.CTextField usu_nombE;
    private gnu.chu.controles.CComboBox verPedidosE;
    private gnu.chu.controles.CLinkBox zon_codiE;
    // End of variables declaration//GEN-END:variables

    @Override
    public Object getFieldValue(JRField jrf) throws JRException {
        try
        {
            String campo = jrf.getName().toLowerCase();
            if (campo.equals("orden"))
                return nLineaReport;
            return dtCon1.getObject(campo);
        } catch (SQLException ex)
        {
            throw new JRException("Error al listar pedidos",ex);
        }
    }
}
