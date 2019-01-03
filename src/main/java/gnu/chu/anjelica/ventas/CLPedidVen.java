package gnu.chu.anjelica.ventas;
/*
 *<p>Titulo: CLVenRep </p>
 * <p>Descripción: Consulta Listado Ventas a Representantes</p>
 * Este programa saca los margenes sobre el precio de tarifa entre unas fechas
 * y para una zona/Representante dada.
 * Tambien permite sacar una relacion de los albaranes, que no tienen precio de tarifa
 * puestos, dandoz< la opción de actualizarlos.
 * <P>Parametros</p>
 * <p>
 *   repCodi = representante
 *   verPrecio = Ver Precio de Pedidos
 *   sbeCodi = subEmpresa
 *   admin= administrador
 * </p> 
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
    boolean ARG_VERPRECIO=false;
    String ARG_REPCODI = "";
    String ARG_SBECODI = "";
    
    private final int JTCAB_EJEPED=0;
    private final int JTCAB_NUMPED=1;
    private final int JTCAB_CLICOD=2;
    private final int JTCAB_CLINOMB=3;   
    private final int JTCAB_CLIPOBL=4;
    private final int JTCAB_FECENT=5;
    private final int JTCAB_HORENT=6;
    private final int JTCAB_CODREP=7;
    private final int JTCAB_RUTA=10;
    private final int JTCAB_SERALB=12;
    private final int JTCAB_NUMALB= 13;   
    private final int JTCAB_EJEALB=11;
    
//    private final int JTLIN_CANTI=6;
//    private final int JTLIN_PROCOD=1;
//    private final int JTLIN_PRONOMB=2;
//    private final int JTLIN_TIPLIN=0;
//    private final int JTLIN_COMENT=9;
//    private final int JTLIN_PRV=3;
//    private final int JTLIN_FECCAD=5;
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
            if (ht.get("verPrecio") != null) 
               ARG_VERPRECIO = Boolean.parseBoolean(ht.get("verPrecio"));
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

        this.setVersion("2018-05-08");

        initComponents();
        this.setSize(new Dimension(730, 535));
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);

        conecta();
    }
    public void setVerPrecio(boolean verPrecio)
    {
         linPed.setVerPrecio(verPrecio);
    }
    @Override
    public void iniciarVentana() throws Exception 
    {
     tit_usunomE.setEnabled(isArgumentoAdmin());
     tit_usunomE.setText(EU.usuario);
     Pcabe.setDefButton(Baceptar.getBotonAccion());
     pvc_feciniE.setAceptaNulo(false);
     pvc_fecfinE.setAceptaNulo(false);
     cli_codiE.setCeroIsNull(true);
     linPed.iniciar(this);
     linPed.setVerPrecio(ARG_VERPRECIO);
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
   

        Baceptar.addActionListener(new ActionListener()
        {
                @Override
          public void actionPerformed(ActionEvent e)
          {     
            Baceptar_actionPerformed(Baceptar.getValor(e.getActionCommand()) );
          }
        });
      jtCabPed.addListSelectionListener(new ListSelectionListener()
      {
      @Override
      public void valueChanged(ListSelectionEvent e)
      {
        if (e.getValueIsAdjusting() || ! jtCabPed.isEnabled() || jtCabPed.isVacio() ) // && e.getFirstIndex() == e.getLastIndex())
          return;
        verDatPed(EU.em_cod,jtCabPed.getValorInt(JTCAB_EJEPED),jtCabPed.getValorInt(JTCAB_NUMPED),
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
          empCodiS=EU.em_cod;
          ejeNumeS=jtCabPed.getValorInt(JTCAB_EJEPED);
          pvcNumeS=jtCabPed.getValorInt(JTCAB_NUMPED);
          cliCodiS=jtCabPed.getValorInt(JTCAB_CLICOD);
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
        return jtCabPed.getValString(JTCAB_CLINOMB);
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
    public String getComentarioRuta()
    {
       return linPed.getComentarioRuta();
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
       linPed.setNumeroPedidos(jtCabPed.getRowCount());
       linPed.verDatPed(empCodi, ejeNume, pvcNume, ejeAlb, serAlb, numAlb);
     } catch (SQLException k)
     {
        Error("Error al ver Datos pedidos",k);
     }
   }
   void Bimpri_actionPerformed(String accion)
   {
      if (jtCabPed.isVacio())
      {
          msgBox("No hay pedidos para listar");
          return;
      }
      if (accion.equals("L"))
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
                       + " WHERE c.emp_codi =  " + EU.em_cod
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
                if (nLineaReport<0 || nLineaDet+1>=linPed.getLineasGrid())
                {
                      nLineaReport++;
                      if (nLineaReport>=jtCabPed.getRowCount())
                         return false;
                      nLineaDet=0;
                      nLineaSalto++;
                      verDatPed(EU.em_cod,
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
         + " c.rut_codi, al.rut_nomb,prc_fecsal FROM pedvenc as c"
         + " left join v_albavec as av on c.avc_ano = av.avc_ano "
         + " and c.avc_serie= av.avc_serie and c.avc_nume =  av.avc_nume and av.emp_codi = c.emp_codi "
         + " left join tiempostarea as tt on tit_tipdoc='P' and tit_id=c.pvc_id and tt.usu_nomb='"+tit_usunomE.getText()+"'"
         + " left join v_pedruta as pr on  c.pvc_id=pr.pvc_id "
         + ",clientes as cl,v_rutas as al "       
        + " WHERE c.pvc_fecpre between to_date('" + pvc_feciniE.getText() + "','dd-MM-yyyy')" +       
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
 
    s += " order by prc_fecsal,c.pvc_fecent,c.rut_codi,c.cli_codi ";

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
    v.add("Eje."); // 0
    v.add("Num.");// 1
    v.add("Cliente"); // 2
    v.add("Nombre Cliente"); // 3
    v.add("Población"); // 4
    v.add("Fec.Entrega"); // 5
    v.add("Hora.Entrega"); // 6
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
    jtCabPed.setAnchoColumna(new int[]{40,49,55,150,100,76,40,40,40,40,100,40,40,60});
    jtCabPed.setAlinearColumna(new int[]{2,2,2,0,0,1,0,1,1,1,0,2,1,2});

    
    jtCabPed.setFormatoColumna(8,"BSN");
  }
  
    public void setCliCodiText(String cliCodi)
    {
        cli_codiE.setText(cliCodi);
    }
 
    public void Baceptar_doClick()
    {
        Baceptar.getBotonAccion().doClick();
    }
   
    void Baceptar_actionPerformed(String accion)
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

        v.add(dtCon1.getString("eje_nume")); // 1
        v.add(dtCon1.getString("pvc_nume")); // 2
        v.add(dtCon1.getString("cli_codi")); // 3
        v.add(dtCon1.getObject("pvc_clinom")==null?dtCon1.getString("cli_nomb"):dtCon1.getString("pvc_clinom")); // 4
        v.add(dtCon1.getObject("cli_poble")); // 5 
        if (dtCon1.getObject("prc_fecsal")==null)
        {
            v.add(dtCon1.getFecha("pvc_fecent","dd-MM-yyyy")); // 5
            v.add("");
        }
        else
        {
            v.add(dtCon1.getFecha("prc_fecsal","dd-MM-yyyy")); // 5
            v.add(dtCon1.getFecha("prc_fecsal","HH:mm"));
        }

        v.add(dtCon1.getString("cli_codrut")); // 6
        v.add(dtCon1.getInt("pvc_cerra")!=0); // 7
        v.add(dtCon1.getString("pvc_depos")); // 8
        v.add(dtCon1.getString("rut_nomb")); // 9
        v.add(dtCon1.getString("avc_ano")); //10
        v.add(dtCon1.getString("avc_serie")); // 11
        v.add(dtCon1.getString("avc_nume")); //12
        jtCabPed.addLinea(v);
           
      } while (dtCon1.next());
      
      if (jtCabPed.isVacio())
      {
          mensajeErr("No encontrados pedidos con estos criterios");
          return;
      }
      jtCabPed.requestFocusInicio();
      jtCabPed.setEnabled(true);
      verDatPed(EU.em_cod,jtCabPed.getValorInt(JTCAB_EJEPED),jtCabPed.getValorInt(JTCAB_NUMPED),
            jtCabPed.getValorInt(JTCAB_EJEALB),jtCabPed.getValString(JTCAB_SERALB),jtCabPed.getValorInt(JTCAB_NUMALB));
      if (accion.equals("C"))
          return;
      Bimpri_actionPerformed(accion);
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
        Baceptar = new gnu.chu.controles.CButtonMenu(Iconos.getImageIcon("check"));
        jtCabPed = new gnu.chu.controles.Cgrid(14);
        linPed = new gnu.chu.anjelica.ventas.PCabPedVentas();

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

        Baceptar.addMenu("Consultar","C");
        Baceptar.addMenu("Listar Pedido","L");
        Baceptar.addMenu("Listar Rel.Ped.","R");
        Baceptar.setText("Aceptar");
        Pcabe.add(Baceptar);
        Baceptar.setBounds(600, 60, 110, 26);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        PPrinc.add(Pcabe, gridBagConstraints);

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

        linPed.setMaximumSize(new java.awt.Dimension(683, 164));
        linPed.setMinimumSize(new java.awt.Dimension(683, 164));
        linPed.setPreferredSize(new java.awt.Dimension(683, 164));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        PPrinc.add(linPed, gridBagConstraints);

        getContentPane().add(PPrinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton BFiltroCam;
    private gnu.chu.controles.CButtonMenu Baceptar;
    private gnu.chu.controles.CPanel PPrinc;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CComboBox albListadoC;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel10;
    private gnu.chu.controles.CLabel cLabel18;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel21;
    private gnu.chu.controles.CLabel cLabel22;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.camposdb.cliPanel cli_codiE;
    private gnu.chu.controles.Cgrid jtCabPed;
    private gnu.chu.anjelica.ventas.PCabPedVentas linPed;
    private gnu.chu.controles.CCheckBox opPedAsigUsu;
    private gnu.chu.controles.CTextField pvc_fecfinE;
    private gnu.chu.controles.CTextField pvc_feciniE;
    private gnu.chu.controles.CLinkBox rep_codiE;
    private gnu.chu.controles.CLinkBox rut_codiE;
    private gnu.chu.camposdb.sbePanel sbe_codiE;
    private gnu.chu.controles.CComboBox servRutaC;
    private gnu.chu.controles.CTextField tit_usunomE;
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
                    return linPed.getUsuarioPedido();
                case "tit_tiempo":
                    return  linPed.getTiempoPedido();
                case "emp_codi":
                    return EU.em_cod;
                case "eje_nume":
                    return jtCabPed.getValorInt(nLineaReport,JTCAB_EJEPED);
                case "pvc_nume":
                    return jtCabPed.getValorInt(nLineaReport,JTCAB_NUMPED);
                case "pvl_numlin":
                    return  nLineaDet;
                case "pvl_canti":
                    return linPed.getGrid().getValString(nLineaDet,linPed.JT_CANPED);
                case "pro_codi":
                    return linPed.getGrid().getValorInt(nLineaDet,linPed.JT_PROCOD);
                case "pro_nomb":
                    return linPed.getGrid().getValString(nLineaDet,linPed.JT_PRONOMB);
                case "pvl_comen":
                    return linPed.getGrid().getValString(nLineaDet,linPed.JT_COMENT);
                case "pvc_comen":
                    return linPed.getComentarioPedido();
                case "pvl_tipo":
                    return linPed.getGrid().getValString(nLineaDet,linPed.JT_TIPLIN);
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
