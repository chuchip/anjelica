package gnu.chu.anjelica.ventas;
/*
 *<p>Titulo: CLVenRep </p>
 * <p>Descripción: Consulta Listado Ventas a Representantes</p>
 * Este programa saca los margenes sobre el precio de tarifa entre unas fechas
 * y para una zona/Representante dada.
 * Tambien permite sacar una relacion de los albaranes, que no tienen precio de tarifa
 * puestos, dandoz< la opción de actualizarlos.
 * Created on 03-dic-2009, 22:41:09
 *
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
 */


import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.listados.Listados;
import gnu.chu.anjelica.pad.MantRepres;
import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.camposdb.proPanel;
import gnu.chu.camposdb.prvPanel;
import gnu.chu.controles.StatusBar;
import gnu.chu.controles.miCellRender;
import gnu.chu.interfaces.ejecutable;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.cgpedven;
import gnu.chu.utilidades.ventana;
import java.awt.BorderLayout;
import java.awt.Color;
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
import java.util.Hashtable;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;


public class CLPedidVen extends  ventana   implements  JRDataSource
{
    String pvc_comen;
    ArrayList<String> htCam=new ArrayList();
    ArrayList<String> htCamAll=new ArrayList();
    ArrayList<String> htCamDef=new ArrayList();
    
    ArrayList<JCheckBoxMenuItem> alMenuItems=new ArrayList();
    JPopupMenu JpopupMenu = new JPopupMenu("Camaras");
    JCheckBoxMenuItem mCamTodas=new JCheckBoxMenuItem("*TODAS*");
    boolean swCliente=false;
    int nLineaReport,nLineaSalto;
    int nLineaDet;
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
    private final int JTCAB_CLICOD=3;
    private final int JTCAB_CLINOMB=4;
    private final int JTCAB_CLIPOBL=5;
    private final int JTCAB_FECENT=6;
    private final int JTCAB_CODREP=7;
    private final int JTCAB_RUTA=10;
    private final int JTCAB_SERALB=12;
    private final int JTCAB_NUMALB= 13;   
    private final int JTCAB_EJEALB=11;
    private final int JTCAB_NOMCLI=4;
    private final int JTLIN_CANTI=6;
    private final int JTLIN_PROCOD=1;
    private final int JTLIN_PRONOMB=2;
    private final int JTLIN_TIPLIN=0;
    private final int JTLIN_COMENT=9;
    private final int JTLIN_PRV=3;
    private final int JTLIN_FECCAD=5;
//    private final int JTCAB_POBCLI=5;
    
    public CLPedidVen(EntornoUsuario eu, Principal p) {
        this(eu, p, null);
    }

    public CLPedidVen(EntornoUsuario eu, Principal p, Hashtable<String,String> ht) {
        EU = eu;
        vl = p.panel1;
        jf = p;
        eje = true;

        try {
            ponParametros(ht);
           
            setTitulo("Cons/List.  Pedidos Ventas");
            setAcronimo("clpeve");
            if (jf.gestor.apuntar(this)) {
                jbInit();
            } else {
                setErrorInit(true);
            }
        } catch (Exception e) {
           ErrorInit(e);
        }
    }

    public CLPedidVen(gnu.chu.anjelica.menu p, EntornoUsuario eu, Hashtable <String,String> ht) {
        EU = eu;
        vl = p.getLayeredPane();
        eje = false;

        try {
            ponParametros(ht);
            setTitulo("Cons/List.  Pedidos de Ventas");
            setAcronimo("clpeve");
            jbInit();
        } catch (Exception e) {
           ErrorInit(e);
        }
    }
    void  ponParametros(Hashtable<String,String> ht)
    {
        if (ht != null) 
        {
            if (ht.get("repCodi") != null) 
               ARG_REPCODI = ht.get("repCodi");

           if (ht.get("sbeCodi") != null)
               ARG_SBECODI = ht.get("sbeCodi");       
           if (ht.get("admin") != null)
               setArgumentoAdmin(Boolean.parseBoolean(ht.get("admin")));
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

        this.setVersion("2018-01-19");

        initComponents();
        this.setSize(new Dimension(730, 535));
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);

        conecta();
    }

    @Override
    public void iniciarVentana() throws Exception 
    {
     tit_usunomE.setEnabled(isArgumentoAdmin());
     tit_usunomE.setText(EU.usuario);
     Pcabe.setDefButton(Baceptar);
     pvc_feciniE.setAceptaNulo(false);
     pvc_fecfinE.setAceptaNulo(false);
     cli_codiE.setCeroIsNull(true);
     MantRepres.llenaLinkBox(rep_codiE, dtCon1);
     pdconfig.llenaDiscr(dtStat, zon_codiE, pdconfig.D_ZONA,EU.em_cod);
     pdconfig.llenaDiscr(dtStat, rut_codiE, pdconfig.D_RUTAS, EU.em_cod);
     rut_codiE.setCeroIsNull(true);
     cli_codiE.iniciar(dtStat, this, vl, EU);     
     cli_codiE.setVerCampoReparto(true);
     s="select * from programasparam where prf_host='"+Formatear.getHostName()+"'"+
         " and prf_id ='clpedven.camaras'";
     if (dtStat.select(s))
     {
        do
        {
            htCamDef.add(dtStat.getString("prf_valor"));
        } while (dtStat.next());
     }
     s="select cam_codi,cam_nomb from v_camaras order by cam_codi";
     if (dtStat.select(s))
     {
        mCamTodas.setSelected(htCamDef.isEmpty());
        JpopupMenu.add(mCamTodas);
        do
        {         
            JCheckBoxMenuItem mCam=new JCheckBoxMenuItem(dtStat.getString("cam_nomb"));
            alMenuItems.add(mCam);
                        
            JpopupMenu.add(mCam);
            final String camCodi=dtStat.getString("cam_codi");
            if (htCamDef.indexOf(camCodi)>=0)
            {
                mCam.setSelected(true);
                htCam.add(camCodi);
            }
            htCamAll.add(camCodi);
            mCam.addActionListener(new ActionListener() 
            {             
               @Override
               public void actionPerformed(ActionEvent e) {                
                if (mCamTodas.isSelected())
                     mCamTodas.setSelected(false);
                if (htCam.indexOf(camCodi)>=0)
                   htCam.remove(camCodi);
                else
                   htCam.add(camCodi);
                JpopupMenu.show(BFiltroCam,0,24);
               }
           });
        } while (dtStat.next());
     }
     sbe_codiE.iniciar(dtStat, this, vl, EU);
     
     sbe_codiE.setAceptaNulo(true);
     sbe_codiE.setValorInt(0);
     pvc_feciniE.setText(Formatear.sumaDias(Formatear.getDateAct(), -7));
     pvc_fecfinE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
     activarEventos();
    }

    @Override
    public void matar(boolean cerrarConexion) {
        try
        {
            if (muerto || ct.isClosed())
            {
                super.matar(cerrarConexion);
                return;
            }
            guardaParam();
           
            super.matar(cerrarConexion); //To change body of generated methods, choose Tools | Templates.
        } catch (SQLException ex)
        {
            muerto=true;
            Error("Error al guardar parametros programa",ex);
        }
    }
    void guardaParam() throws SQLException
    {
        s="delete from programasparam where prf_host='"+Formatear.getHostName()+"'"+
                " and prf_id ='clpedven.camaras'";
        dtAdd.executeUpdate(s);
        if (!mCamTodas.isSelected())
        {
            for (String cam:htCam)
            {
                dtAdd.addNew("programasparam");
                dtAdd.setDato("prf_host",Formatear.getHostName());
                dtAdd.setDato("prf_id","clpedven.camaras");
                dtAdd.setDato("prf_valor",cam);
                dtAdd.update();
            }
        }
        dtAdd.commit();
    }    
    void activarEventos()
    {
        BFiltroCam.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              JpopupMenu.show(BFiltroCam,0,24);
            }
        });
        mCamTodas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {            
              htCam.clear();
              for (JCheckBoxMenuItem menuItem:alMenuItems)
              {
                  menuItem.setSelected(mCamTodas.isSelected());                
              }
              if (mCamTodas.isSelected())
              {
                for (String cam:htCamAll)
                {
                      htCam.add(cam);
                }
              }
              JpopupMenu.show(BFiltroCam,0,24);
            }
        });
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
        verDatPed(jtCabPed.getValorInt(JTCAB_EMPPED),jtCabPed.getValorInt(JTCAB_EJEPED),jtCabPed.getValorInt(JTCAB_NUMPED),
            jtCabPed.getValorInt(JTCAB_EJEALB),jtCabPed.getValString(JTCAB_SERALB),jtCabPed.getValorInt(JTCAB_NUMALB));
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
        pdpeve.irMantPedido(jf,jtCabPed.getValorInt(JTCAB_EJEPED),jtCabPed.getValorInt(JTCAB_NUMPED));
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
    public String getSerieAlbaran() 
    {
        return jtCabPed.getValString(JTCAB_SERALB);
    }
     public int getEjercicioAlbaran() 
    {
        return jtCabPed.getValorInt(JTCAB_EJEALB);
    }
    public int getNumeroAlbaran() 
    {
        return jtCabPed.getValorInt(JTCAB_NUMALB);
    }

    void verDatPed(int empCodi,int ejeNume,int pvcNume,int ejeAlb,String serAlb,int numAlb)
   {
     try
     {
       s="SELECT p.*,cl.cli_pobl,tit_tiempo,usu_nomco FROM v_pedven as p"
           + " left join v_tiempospedido as tt on  tit_id=p.pvc_id and tt.usu_nomb='"+tit_usunomE.getText()+"' "
           + ",v_cliente as cl "+
           " WHERE p.emp_codi =  "+empCodi+
           " and p.cli_codi = cl.cli_codi "+ 
           " AND p.eje_nume = "+ejeNume+
           " and p.pvc_nume = "+pvcNume+
           " order by p.pvl_numlin ";
       jtLinPed.removeAllDatos();
      // Ppie.resetTexto();
       nPedT.setValorDec(jtCabPed.getRowCount());
       if (! dtCon1.select(s))
       {
         msgBox("NO ENCONTRADOS DATOS PARA ESTE PEDIDO");
         return;
       }
      
       usu_nomcoE.setText(dtCon1.getString("usu_nomco",true));
       tit_tiempoE.setText(dtCon1.getString("tit_tiempo",true));
       pvc_comen=dtCon1.getString("pvc_comen");
       usu_nombE.setText(dtCon1.getString("usu_nomb"));
       pvc_fecpedE.setText(dtCon1.getFecha("pvc_fecped"));
       pvc_horpedE.setText(dtCon1.getFecha("pvc_fecped","HH.mm"));
       pvc_comenE.setText(dtCon1.getString("pvc_comen"));
       
       pvc_impresE.setSelecion(dtCon1.getString("pvc_impres"));
       
    
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
       if (ejeAlb!=0)
       {
           verDatAlbaranPed(empCodi,ejeAlb,serAlb,numAlb );
       }
//       actAcumJT();
     } catch (Exception k)
     {
       Error("Error al Ver datos de pedido",k);
     }
   }
    
   void verDatAlbaranPed(int empCodi,int avcAno,String avcSerie, int avcNume) throws SQLException
   {
      s="select 1 as tipo,l.pro_codi,sum(avp_numuni) as avp_numuni,sum(avp_canti) as avp_canti " +
             (opIgnPrvE.isSelected()?"":", s.prv_codi")+
             (opIgnCadE.isSelected()?"":",s.stp_feccad ")+
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
             " GROUP BY l.pro_codi  "+
            (opIgnPrvE.isSelected()?"":",s.prv_codi")
            + (opIgnCadE.isSelected()?"":", stp_feccad ")+
             " UNION ALL "+
             "select 0 as tipo, l.pro_codi,sum(avp_numuni) as avp_numuni,sum(avp_canti) as avp_canti " +
              (opIgnPrvE.isSelected()?"":", c.cli_codi AS prv_codi ")
              + (opIgnCadE.isSelected()?"":", c.avc_fecalb as stp_feccad ") +
              " from v_albvenpar as l,v_albavec as c  where  c.avc_ano = l.avc_ano  "+
              " and c.emp_codi = l.emp_codi "+
              " and c.avc_serie = l.avc_serie "+
              " and c.avc_nume = l.avc_nume  "+
              " and l.avp_numpar = 0 "+
             " AND l.emp_codi = " + empCodi+
             " and l.avc_ano = " + avcAno +
             " and l.avc_serie = '" + avcSerie + "'" +
             " and l.avc_nume = " + avcNume +
             " GROUP BY l.pro_codi"
            + (opIgnPrvE.isSelected()?"":" ,c.cli_codi")
            + (opIgnCadE.isSelected()?"":" , avc_fecalb ")+
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
        v.add(opIgnPrvE.isSelected()?"":dtCon1.getString("prv_codi"));
        v.add(opIgnPrvE.isSelected()?"":prv_codiE.getNombPrv(dtCon1.getString("prv_codi"), dtStat));
      }
      if (dtCon1.getInt("tipo")==0)
        v.add("");
      else
        v.add(opIgnCadE.isSelected()?"":dtCon1.getFecha("stp_feccad","dd-MM-yy"));
    
      v.add(dtCon1.getString("avp_numuni"));
      v.add(""); // Precio
      v.add(false); // COnf
      v.add(Formatear.format(dtCon1.getString("avp_canti"),"#,##9.99")+" Kg");         
      v.add(""); // NL
      nLin=0;
      while (nLin<rowCount)
      {
        if (jtLinPed.getValString(nLin,0).equals("A") ||
            jtLinPed.getValorInt(nLin,JTLIN_PROCOD)!=dtCon1.getInt("pro_codi"))
        {
          nLin++;
          continue;
        }
        v.set(JTLIN_PROCOD,"");
        v.set(JTLIN_PRONOMB,"");
        jtLinPed.addLinea(v,nLin+1);
        break;
      }
      if (nLin>=rowCount)
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
          /**
           * Listado relacion de pedidos. 
           */
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
        nLineaSalto=0;
        nLineaDet=9999;
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
            
            if (!swImpreso)
            { 
                 if (nLineaReport>=jtCabPed.getRowCount())
                    return false;
                  s = "SELECT c.*,ti.usu_nomco,tit_tiempo, cl.cli_nomb,cl.cli_poble"
                       + "  FROM pedvenc as c left join v_tiempospedido as ti on ti.tid_id=c.pvc_id "
                      + " where ,v_cliente as cl "
                       + " WHERE c.emp_codi =  " + jtCabPed.getValorInt(nLineaReport, JTCAB_EMPPED)
                       + " and C.cli_codi = cl.cli_codi "
                       + " AND C.eje_nume = " + jtCabPed.getValorInt(nLineaReport, JTCAB_EJEPED)
                       + " and C.pvc_nume = " + jtCabPed.getValorInt(nLineaReport, JTCAB_NUMPED);
               if (!dtCon1.select(s))
               {
                   throw new JRException("Error al localizar pedido para listar. Linea: "
                       + nLineaReport + " Pedido: " + jtCabPed.getValorInt(nLineaReport, JTCAB_NUMPED));
               }      
               nLineaReport++;               
          }
          else
          {            
                if (nLineaReport<0 || nLineaDet+1>=jtLinPed.getRowCount())
                {
                      nLineaReport++;
                      if (nLineaReport>=jtCabPed.getRowCount())
                         return false;
                      nLineaDet=0;
                      nLineaSalto++;
                      verDatPed(jtCabPed.getValorInt(nLineaReport, JTCAB_EMPPED),
                              jtCabPed.getValorInt(nLineaReport, JTCAB_EJEPED),
                              jtCabPed.getValorInt(nLineaReport, JTCAB_NUMPED),
                              jtCabPed.getValorInt(nLineaReport,JTCAB_EJEALB),
                              jtCabPed.getValString(nLineaReport,JTCAB_SERALB),jtCabPed.getValorInt(nLineaReport,JTCAB_NUMALB)
                      );                      
                  }
                  else   
                    nLineaDet++;

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
      
        nLineaReport=-1;
        nLineaDet=9999;
        nLineaSalto=0;
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
     String camCodi="";
     if (!mCamTodas.isSelected())
     {
        for (String cam:htCam)
        {
             camCodi+="'"+cam+"',";
        }
        if (!camCodi.equals(""))
         camCodi=camCodi.substring(0,camCodi.length()-1);
     }
     if (!cli_codiE.isNull())
         swCliente=true;
     s = "SELECT c.*,av.avc_id,av.avc_impres,av.cli_ruta, cl.cli_nomb,cl.cli_codrut, cl.cli_poble,"
         + " c.rut_codi, al.rut_nomb FROM pedvenc as c"
         + " left join v_albavec as av on c.avc_ano = av.avc_ano "
         + " and c.avc_serie= av.avc_serie and c.avc_nume =  av.avc_nume and av.emp_codi = c.emp_codi "
         + " left join tiempostarea as tt on tit_tipdoc='P' and tit_id=c.pvc_id and tt.usu_nomb='"+tit_usunomE.getText()+"'"
         + ",clientes as cl,v_rutas as al "       
        + " WHERE pvc_fecent between to_date('" + pvc_feciniE.getText() + "','dd-MM-yyyy')" +       
        " and  to_date('" + pvc_fecfinE.getText()  + "','dd-MM-yyyy')" +
        " and c.emp_codi = "+EU.em_cod+
        " and cl.cli_codi = c.cli_codi " +
        " and c.rut_codi = al.rut_codi "+     
         " and pvc_confir = 'S'"+
         (opPedAsigUsu.isSelected()?" and tt.usu_nomb='"+tit_usunomE.getText()+"'":"")+
         (camCodi.equals("")?"":
         " and exists (select a.pro_codi from v_pedven as l,v_articulo as a where"
         + " a.pro_codi=l.pro_codi and l.pvc_id=c.pvc_id and a.cam_codi in ("+camCodi+"))" )+
        (sbe_codiE.getValorInt()==0?"":" and cl.sbe_codi = "+sbe_codiE.getValorInt())+
        (zon_codiE.isNull() || swCliente?"":" and cl.zon_codi = '"+zon_codiE.getText()+"'")+
        (rep_codiE.isNull() || swCliente?"":" and cl.rep_codi = '"+rep_codiE.getText()+"'");

    if (verPedidosE.getValor().equals("P"))
      s += " AND (c.avc_ano = 0 or pvc_cerra=0)";
    if (verPedidosE.getValor().equals("L"))
      s += " AND c.avc_ano != 0";

    if (swCliente)
      s += " AND c.cli_codi = " + cli_codiE.getValorInt();
 
    s += " order by c.rut_codi,c.pvc_fecent,c.cli_codi ";

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
    v.add("C.Rep"); // 7
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
    jtCabPed.setAnchoColumna(new int[]{26,40,49,55,150,100,76,40,40,40,100,40,40,60});
    jtCabPed.setAlinearColumna(new int[]{2,2,2,2,0,0,1,0,1,1,0,2,1,2});

    
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
      cgpedven vg = new cgpedven(jtLinPed);
        for (int n = 0; n < jtLinPed.getColumnCount(); n++)
        {
            miCellRender mc = jtLinPed.getRenderer(n);
            if (mc == null)
                continue;
            mc.setVirtualGrid(vg);
            mc.setErrBackColor(Color.CYAN);
            mc.setErrForeColor(Color.BLACK);
        }
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
      boolean swServ=verPedidosE.getValor().equals("S") || 
          verPedidosE.getValor().equals("M") ; // A servir (tienen albaran y no esta listado)
           
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
                if (dtCon1.getInt("pvc_cerra")==0)
                    continue;
                if (dtCon1.getObject("avc_impres")==null)
                    continue;
                if ((dtCon1.getInt("avc_impres") & 1) != 0 )
                { // Esta impreso
                    if ( verPedidosE.getValor().equals("S"))
                        continue;
                    if (dtCon1.getInt("avc_impres")  == 1 && verPedidosE.getValor().equals("M"))
                        continue;
                }
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
      verDatPed(jtCabPed.getValorInt(JTCAB_EMPPED),jtCabPed.getValorInt(JTCAB_EJEPED),jtCabPed.getValorInt(JTCAB_NUMPED),
            jtCabPed.getValorInt(JTCAB_EJEALB),jtCabPed.getValString(JTCAB_SERALB),jtCabPed.getValorInt(JTCAB_NUMALB));
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
        rep_codiE = new gnu.chu.controles.CLinkBox();
        zon_codiE = new gnu.chu.controles.CLinkBox();
        cLabel18 = new gnu.chu.controles.CLabel();
        Baceptar = new gnu.chu.controles.CButton(Iconos.getImageIcon("check"));
        cLabel10 = new gnu.chu.controles.CLabel();
        verPedidosE = new gnu.chu.controles.CComboBox();
        cLabel21 = new gnu.chu.controles.CLabel();
        cLabel2 = new gnu.chu.controles.CLabel();
        servRutaC = new gnu.chu.controles.CComboBox();
        cLabel3 = new gnu.chu.controles.CLabel();
        albListadoC = new gnu.chu.controles.CComboBox();
        cLabel22 = new gnu.chu.controles.CLabel();
        rut_codiE = new gnu.chu.controles.CLinkBox();
        BFiltroCam = new gnu.chu.controles.CButton(Iconos.getImageIcon("filter"));
        cLabel8 = new gnu.chu.controles.CLabel();
        tit_usunomE = new gnu.chu.controles.CTextField(Types.CHAR,"X",15);
        opPedAsigUsu = new gnu.chu.controles.CCheckBox();
        jtLinPed = new gnu.chu.controles.Cgrid(11);
        jtCabPed = new gnu.chu.controles.Cgrid(14);
        Ppie = new gnu.chu.controles.CPanel();
        cLabel17 = new gnu.chu.controles.CLabel();
        nPedT = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9");
        pvc_fecpedE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        pvc_horpedE = new gnu.chu.controles.CTextField(Types.DECIMAL, "99.99");
        cLabel20 = new gnu.chu.controles.CLabel();
        usu_nombE = new gnu.chu.controles.CTextField();
        pvc_impresE = new gnu.chu.controles.CCheckBox();
        scrollarea1 = new javax.swing.JScrollPane();
        pvc_comenE = new gnu.chu.controles.CTextArea();
        Bimpri = new gnu.chu.controles.CButtonMenu();
        cLabel23 = new gnu.chu.controles.CLabel();
        opIgnCadE = new gnu.chu.controles.CCheckBox();
        opIgnPrvE = new gnu.chu.controles.CCheckBox();
        cLabel24 = new gnu.chu.controles.CLabel();
        cLabel25 = new gnu.chu.controles.CLabel();
        tit_tiempoE = new gnu.chu.controles.CTextField(Types.DECIMAL, "##9");
        usu_nomcoE = new gnu.chu.controles.CTextField();

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
        cLabel1.setBounds(2, 2, 70, 18);
        Pcabe.add(cli_codiE);
        cli_codiE.setBounds(80, 22, 340, 18);

        cLabel5.setText("Delegación");
        Pcabe.add(cLabel5);
        cLabel5.setBounds(590, 22, 70, 18);
        Pcabe.add(sbe_codiE);
        sbe_codiE.setBounds(660, 22, 37, 18);

        cLabel6.setText("Usuario");
        Pcabe.add(cLabel6);
        cLabel6.setBounds(250, 2, 49, 17);
        Pcabe.add(pvc_feciniE);
        pvc_feciniE.setBounds(500, 2, 76, 18);

        cLabel7.setText("A Fecha");
        Pcabe.add(cLabel7);
        cLabel7.setBounds(580, 2, 43, 18);
        Pcabe.add(pvc_fecfinE);
        pvc_fecfinE.setBounds(630, 2, 75, 18);

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

        Baceptar.setText("Aceptar");
        Pcabe.add(Baceptar);
        Baceptar.setBounds(610, 50, 100, 30);

        cLabel10.setText("De Cliente");
        Pcabe.add(cLabel10);
        cLabel10.setBounds(5, 22, 70, 18);

        verPedidosE.addItem("Pendientes","P");
        verPedidosE.addItem("A servir","S");
        verPedidosE.addItem("A servir + Modif.","M");
        verPedidosE.addItem("Preparados","L");
        verPedidosE.addItem("Todos","T");
        Pcabe.add(verPedidosE);
        verPedidosE.setBounds(80, 2, 160, 18);

        cLabel21.setText("Repres.");
        cLabel21.setPreferredSize(new java.awt.Dimension(60, 18));
        Pcabe.add(cLabel21);
        cLabel21.setBounds(5, 42, 50, 18);

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

        rut_codiE.setFormato(Types.CHAR,"X",2);
        rut_codiE.setAncTexto(30);
        rut_codiE.setMayusculas(true);
        rut_codiE.setPreferredSize(new java.awt.Dimension(152, 18));
        Pcabe.add(rut_codiE);
        rut_codiE.setBounds(390, 65, 200, 18);

        BFiltroCam.setText("Camaras");
        Pcabe.add(BFiltroCam);
        BFiltroCam.setBounds(490, 20, 90, 19);

        cLabel8.setText("De Fecha");
        Pcabe.add(cLabel8);
        cLabel8.setBounds(440, 2, 49, 18);
        Pcabe.add(tit_usunomE);
        tit_usunomE.setBounds(300, 2, 76, 17);

        opPedAsigUsu.setText("Asig.");
        opPedAsigUsu.setToolTipText("Ver solo pedidos asignados a este usuario");
        Pcabe.add(opPedAsigUsu);
        opPedAsigUsu.setBounds(380, 2, 60, 17);

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
        Ppie.setMaximumSize(new java.awt.Dimension(780, 60));
        Ppie.setMinimumSize(new java.awt.Dimension(780, 60));
        Ppie.setPreferredSize(new java.awt.Dimension(680, 60));
        Ppie.setLayout(null);

        cLabel17.setText("Tiempos");
        cLabel17.setPreferredSize(new java.awt.Dimension(60, 18));
        Ppie.add(cLabel17);
        cLabel17.setBounds(570, 0, 60, 18);

        nPedT.setEnabled(false);
        Ppie.add(nPedT);
        nPedT.setBounds(305, 1, 30, 18);

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
        usu_nombE.setBounds(480, 20, 90, 18);

        pvc_impresE.setText("Listado ");
        pvc_impresE.setEnabled(false);
        pvc_impresE.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        Ppie.add(pvc_impresE);
        pvc_impresE.setBounds(230, 40, 70, 17);

        pvc_comenE.setColumns(20);
        pvc_comenE.setRows(5);
        scrollarea1.setViewportView(pvc_comenE);

        Ppie.add(scrollarea1);
        scrollarea1.setBounds(10, 0, 210, 55);

        Bimpri.setText("Listar");
        Bimpri.addMenu("Relacion", "R");
        Bimpri.addMenu("Pedidos", "P");
        Ppie.add(Bimpri);
        Bimpri.setBounds(560, 40, 80, 20);

        cLabel23.setText("Total Pedidos ");
        cLabel23.setPreferredSize(new java.awt.Dimension(60, 18));
        Ppie.add(cLabel23);
        cLabel23.setBounds(225, 1, 80, 18);

        opIgnCadE.setSelected(true);
        opIgnCadE.setText("IgnorarCaducidad");
        opIgnCadE.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        Ppie.add(opIgnCadE);
        opIgnCadE.setBounds(440, 40, 130, 17);

        opIgnPrvE.setSelected(true);
        opIgnPrvE.setText("Ignorar Proveedor");
        opIgnPrvE.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        Ppie.add(opIgnPrvE);
        opIgnPrvE.setBounds(310, 40, 120, 17);

        cLabel24.setText("Usuario");
        cLabel24.setPreferredSize(new java.awt.Dimension(60, 18));
        Ppie.add(cLabel24);
        cLabel24.setBounds(430, 20, 50, 18);

        cLabel25.setText("Asignado");
        cLabel25.setPreferredSize(new java.awt.Dimension(60, 18));
        Ppie.add(cLabel25);
        cLabel25.setBounds(340, 0, 60, 18);

        tit_tiempoE.setEnabled(false);
        Ppie.add(tit_tiempoE);
        tit_tiempoE.setBounds(630, 2, 40, 18);

        usu_nomcoE.setEnabled(false);
        Ppie.add(usu_nomcoE);
        usu_nomcoE.setBounds(400, 2, 170, 18);

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
    private gnu.chu.controles.CButton BFiltroCam;
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CButtonMenu Bimpri;
    private gnu.chu.controles.CPanel PPrinc;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Ppie;
    private gnu.chu.controles.CComboBox albListadoC;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel10;
    private gnu.chu.controles.CLabel cLabel17;
    private gnu.chu.controles.CLabel cLabel18;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel20;
    private gnu.chu.controles.CLabel cLabel21;
    private gnu.chu.controles.CLabel cLabel22;
    private gnu.chu.controles.CLabel cLabel23;
    private gnu.chu.controles.CLabel cLabel24;
    private gnu.chu.controles.CLabel cLabel25;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.camposdb.cliPanel cli_codiE;
    private gnu.chu.controles.Cgrid jtCabPed;
    private gnu.chu.controles.Cgrid jtLinPed;
    private gnu.chu.controles.CTextField nPedT;
    private gnu.chu.controles.CCheckBox opIgnCadE;
    private gnu.chu.controles.CCheckBox opIgnPrvE;
    private gnu.chu.controles.CCheckBox opPedAsigUsu;
    private gnu.chu.controles.CTextArea pvc_comenE;
    private gnu.chu.controles.CTextField pvc_fecfinE;
    private gnu.chu.controles.CTextField pvc_feciniE;
    private gnu.chu.controles.CTextField pvc_fecpedE;
    private gnu.chu.controles.CTextField pvc_horpedE;
    private gnu.chu.controles.CCheckBox pvc_impresE;
    private gnu.chu.controles.CLinkBox rep_codiE;
    private gnu.chu.controles.CLinkBox rut_codiE;
    private gnu.chu.camposdb.sbePanel sbe_codiE;
    private javax.swing.JScrollPane scrollarea1;
    private gnu.chu.controles.CComboBox servRutaC;
    private gnu.chu.controles.CTextField tit_tiempoE;
    private gnu.chu.controles.CTextField tit_usunomE;
    private gnu.chu.controles.CTextField usu_nombE;
    private gnu.chu.controles.CTextField usu_nomcoE;
    private gnu.chu.controles.CComboBox verPedidosE;
    private gnu.chu.controles.CLinkBox zon_codiE;
    // End of variables declaration//GEN-END:variables

    @Override
    public Object getFieldValue(JRField jrf) throws JRException {
        try          
        {
            
            String campo = jrf.getName().toLowerCase();
            if (!swImpreso)
            {
                if (campo.equals("orden"))
                    return nLineaReport;
                return dtCon1.getObject(campo);
            }
            switch (campo)
            {
                case "salto":
                    return (int) ( (nLineaSalto-1)/4);
                case "nlineasalto":
                    return nLineaSalto;
                case "usu_nomco":
                    return usu_nomcoE.getText();
                case "tit_tiempo":
                    return  tit_tiempoE.getValorInt();
                case "emp_codi":
                    return jtCabPed.getValorInt(nLineaReport,JTCAB_EMPPED);
                case "eje_nume":
                    return jtCabPed.getValorInt(nLineaReport,JTCAB_EJEPED);
                case "pvc_nume":
                    return jtCabPed.getValorInt(nLineaReport,JTCAB_NUMPED);
                case "pvl_numlin":
                    return  nLineaDet;
                case "pvl_canti":
                    return jtLinPed.getValString(nLineaDet,JTLIN_CANTI);
                case "pro_codi":
                    return jtLinPed.getValorInt(nLineaDet,JTLIN_PROCOD);
                case "pro_nomb":
                    return jtLinPed.getValString(nLineaDet,JTLIN_PRONOMB);
                case "pvl_comen":
                    return jtLinPed.getValString(nLineaDet,JTLIN_COMENT);
                case "pvc_comen":
                    return pvc_comenE.getText();
                case "pvl_tipo":
                    return jtLinPed.getValString(nLineaDet,JTLIN_TIPLIN);
                case "cli_codi":
                    return jtCabPed.getValorInt(nLineaReport,JTCAB_CLICOD);
                case "pvc_clinom":
                    return jtCabPed.getValString(nLineaReport,JTCAB_CLINOMB);
                case "cli_pobl":
                    return jtCabPed.getValString(nLineaReport,JTCAB_CLIPOBL);
                case "cli_codrut":
                    return jtCabPed.getValString(nLineaReport,JTCAB_CODREP);
                case "rut_nomb":
                    return jtCabPed.getValString(nLineaReport,JTCAB_RUTA);
                case "pvc_fecent":
                    return jtCabPed.getValString(nLineaReport,JTCAB_FECENT);
                    
            }
            throw new JRException("Campo NO VALIDO: "+campo);
        } catch (SQLException ex)
        {
            throw new JRException("Error al leer campo de base datos",ex);
        }
    }
    
}
