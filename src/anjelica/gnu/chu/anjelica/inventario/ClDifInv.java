package gnu.chu.anjelica.inventario;

/**
 *
 * <p>Titulo: ClDifInv </p>
 * <p>Descripción: Consulta / Listado Diferencias Existencias Reales sobre las introducidas en Control </p>
 * <p>Copyright: Copyright (c) 2005-2015
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los términos de la Licencia Pública General de GNU según es publicada por
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
* <p>Empresa: MISL</p>
* @author chuchiP
* @version 1.0
*/
import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.almacen.MvtosAlma;
import gnu.chu.controles.StatusBar;
import gnu.chu.interfaces.ejecutable;
import gnu.chu.print.util;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.SystemOut;
import gnu.chu.utilidades.mensajes;
import gnu.chu.utilidades.miThread;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;


public class ClDifInv extends ventana {
    private String TABLA_INV_CAB="coninvcab";
    private String TABLA_INV_LIN="coninvlin";
    private String VISTA_INV="v_coninvent";
    boolean swConsulta=false;
    String camCodiE;
    String s,s1;
    int PROCODI = 0; 
    int LOTE = -1;
    int almCodi=1;
    boolean swLeeOrd=true;
    DatosTabla dtAdd;
      
    public ClDifInv(EntornoUsuario eu, Principal p) {
        EU = eu;
        vl = p.panel1;
        jf = p;
        eje = true;

        setTitulo("Cons/List. Diferencias Inventario");

        try {
            if (jf.gestor.apuntar(this)) {       
                    jbInit();
            } else {
                setErrorInit(true);
            }
        } catch (Exception ex) {
            ErrorInit(ex);
            setErrorInit(true);
        }
    }

    public ClDifInv(gnu.chu.anjelica.menu p, EntornoUsuario eu) {

        EU = eu;
        vl = p.getLayeredPane();
        setTitulo("Cons/List. Diferencias Inventario");
        eje = false;

        try {
            jbInit();
        } catch (Exception ex) {
            ErrorInit(ex);
            setErrorInit(true);
        }
    }

    private void jbInit() throws Exception {
     
        iniciarFrame(); 
       
        this.setVersion("2015-04-10");
        statusBar = new StatusBar(this);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        conecta();

        initComponents();
        this.setSize(new Dimension(640, 530));
    }

    @Override
    public void iniciarVentana() throws Exception {
      stUp=ct.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
      dtAdd=new DatosTabla(ct);
      MvtosAlma.llenaComboFecInv(dtStat,EU.em_cod,EU.ejercicio,feulinE,12);
      cci_fecconE.iniciar(dtStat, this, vl, EU);
      s = "SELECT alm_codi,alm_nomb FROM v_almacen " +
          " ORDER BY alm_codi";
      dtStat.select(s);
      alm_codiE.addDatos(dtStat);
      alm_codiE.addDatos("0","Todos");
      alm_codiE.setCeroIsNull(false);
      alm_codiE.setText("0");
      
      s ="select MAX(cci_feccon) as cci_feccon from "+TABLA_INV_CAB +
          " where emp_codi = " + EU.em_cod ;

      dtStat.select(s);
      if (dtStat.getObject("cci_feccon") != null)
        cci_fecconE.setText(dtStat.getFecha("cci_feccon","dd-MM-yyyy"));
    
      gnu.chu.anjelica.pad.pdconfig.llenaDiscr(dtCon1, cam_codiE, "AC", EU.em_cod);
      pro_codiE.iniciar(dtStat,this,vl,EU);
      cam_codiE.addDatos("--","TODAS LAS CAMARAS LEIDAS");
      cam_codiE.addDatos("**","TODAS");
      cam_codiE.setFormato(true);
      cam_codiE.texto.setMayusc(true);
      cam_codiE.setFormato(Types.CHAR,"XX",2);
      cam_codiE.setText("**");
      pro_loteE.setText("",false);
    
      
  
      Pcondic.setDefButton(Baceptar.getBotonAccion());
      cci_fecconE.requestFocus();
      activarEventos();
    }
    /**
     * Activar Eventos
     */
    void activarEventos()
    {
      Baceptar.addActionListener(new ActionListener() {
            @Override
        public void actionPerformed(ActionEvent e) {
          Baceptar_actionPerformed(e);
        }
      });
      jt.addMouseListener(new MouseAdapter() {
            @Override
          public void mouseClicked(MouseEvent e) {
              if (e.getClickCount()<2)
                  return;
              if (jf==null)
                  return;
              ejecutable prog;
              if ((prog=jf.gestor.getProceso("gnu.chu.anjelica.almacen.conmvpr"))==null)
                  return;
              gnu.chu.anjelica.almacen.conmvpr cm=(gnu.chu.anjelica.almacen.conmvpr) prog;
              for (int n=jt.getSelectedRow();n>=0;n--)
              {
                  if (jt.getValorInt(n,0)!=0)
                  {
                      cm.setProCodi(jt.getValorInt(n,0));
                      break;
                  }
              }          
              cm.setLote(jt.getValorInt(4));
              cm.setIndividuo(jt.getValorInt(5));
              cm.ejecutaConsulta();
              jf.gestor.ir(cm);
          }
      });
    }
    
    void Baceptar_actionPerformed(ActionEvent e)
    {
      if (cci_fecconE.getError())
        return;
      if (cci_fecconE.isNull())
      {
        mensajeErr("Introduzca la Fecha de Control");
        return;
      }
    
      swConsulta=e.getActionCommand().startsWith("Consultar");
      LOTE=-1;
      PROCODI=pro_codiE.getValorInt();
      if (! pro_loteE.isNull() && PROCODI>0 )
        LOTE = pro_loteE.getValorInt();
      camCodiE=cam_codiE.getText();
      almCodi=alm_codiE.getValorInt();
      if (cam_codiE.getText().equals("**") || cam_codiE.getText().equals("*") || cam_codiE.getText().trim().equals(""))
        camCodiE="";
      try 
      {
        if (! opDatStock.isSelected())
        {
            TABLA_INV_CAB="coninvcab";
            TABLA_INV_LIN="coninvlin";
            VISTA_INV="v_coninvent";
            s="select * from "+VISTA_INV+" as c where cci_feccon= TO_DATE('" +
                  cci_fecconE.getText() + "','dd-MM-yyyy')  "+
                  " and lci_regaut=0 "+
                  " and c.emp_codi = "+EU.em_cod+
                  " and (select count(*) from "+VISTA_INV+" as cc where c.pro_codi = cc.pro_codi "+
                  " and c.prp_indi=cc.prp_indi and c.prp_part= cc.prp_part "+
                  " and c.prp_seri=cc.prp_seri and c.prp_ano=cc.prp_ano  "+
                  " and cci_Feccon= TO_DATE('" + cci_fecconE.getText() + "','dd-MM-yyyy') "+
                  " and cc.emp_codi = "+EU.em_cod+
                  " and lci_regaut=0 ) > 1 order by  PRO_CODI,PRP_PART,PRP_INDI";
            if (dtCon1.select(s))
            {
              String msg="Inventario en fecha: "+cci_fecconE.getText()+"\n";
              do 
              {
                  msg+=" Linea: "+dtCon1.getString("lci_nume")+" en ID: "+dtCon1.getString("cci_codi")+": "+
                      dtCon1.getString("pro_codi")+" "+dtCon1.getString("prp_ano")+
                      dtCon1.getString("prp_seri")+
                      dtCon1.getString("PRP_PART")+" "+dtCon1.getString("lci_peso")+" Kg";
                  dtCon1.next();
                  msg+=" Repetido en linea: "+dtCon1.getString("lci_nume")+" del ID: "+dtCon1.getString("cci_codi")+"\n";
              } while (dtCon1.next());
              mensajes.mensajeExplica("Aviso",  "Individuos metidos dos veces en inventario",msg);              
              enviaMailError("Listado diferencias Inventario con individuos metidos mas de una vez: "+msg);
            }
            String s2 = "SELECT c.* FROM "+VISTA_INV+" AS c " +
              " WHERE c.emp_codi =" + EU.em_cod +
              " and c.cci_feccon = TO_DATE('" + cci_fecconE.getText() + "','dd-MM-yyyy') " +
              (camCodiE.equals("") || camCodiE.equals("--") ? "" :
               " AND c.cam_codi = '" + camCodiE + "'") +
              (almCodi == 0 ? "" : " and alm_codi = " + almCodi) +
              (usu_nombE.isNull()?"":" and usu_nomb ='"+usu_nombE.getText()+"'")+
              " ORDER BY pro_codi,prp_seri,prp_part,prp_indi ";
    //      debug("S: " + s1);
            if (!dtCon1.select(s2))
            {
              mensajeErr("No encontrados Control de Inventario para estas condiciones con esta fecha");
              return;
            }
        }
      }
      catch (SQLException ex)
      {
        Error("Error al Buscar Control de Inventario", ex);
        return;
      }

      new miThread("")
      {
            @Override
        public void run()
        {
          buscaDatos();
        }
      };
    }

    void buscaDatos()
    {       
      msgEspere("Buscando Diferencias Inventarios");
      try
      {
        int cciCodi;
        if (opDatStock.isSelected())
        {
            cciCodi=calcDatosStock();
            if (cciCodi==0)
            {
                this.setEnabled(true);
                return;
            }
             if (!calcDatos(cciCodi))
            {
                this.setEnabled(true);
                return;
            }
        }
        else
        {
          cciCodi= dtCon1.getInt("cci_codi"); // El primer codigo (para inserts);
          
          if (opCalInv.isSelected())
          {                
            if (!calcDatos(cciCodi))
            {
                this.setEnabled(true);
                return;
            }
          }
        }

        actualizaMsg("Generando Listado ... Espere, por favor",false);
        s="select  c.cci_codi,P.cam_codi,c.PRO_CODI,p.pro_nomb,c.PRP_ANO,"+
            "c.prp_empcod ,c.PRP_SERI,"+
            " c.PRP_PART,c.PRP_INDI,c.lci_coment, "+
            " sum(c.LCI_PESO) as lci_peso,sum(c.LCI_KGSORD) as lci_kgsord "+
           "  from v_articulo  as p,"+VISTA_INV+" as c "+
            " where  p.pro_tiplot= 'V' "+
            " and c.pro_codi = p.pro_codi "+
            " AND c.LCI_PESO <> c.LCI_KGSORD "+
            (pro_artconE.getValorInt()==2?"":
              (" and p.pro_artcon " + (pro_artconE.getValorInt() == 0 ? "= 0" : " <> 0")))+
            (usu_nombE.isNull()?"":" and c.usu_nomb ='"+usu_nombE.getText()+"'")+
            (PROCODI>0?" AND c.pro_codi = "+PROCODI:"")+
            (LOTE>=0?" AND c.prp_part = "+LOTE:"")+
            " and c.emp_codi =" + EU.em_cod+
            " and c.cci_feccon = TO_DATE('" + cci_fecconE.getText() +"','dd-MM-yyyy') "+
            (almCodi== 0 ? "" : " and c.alm_codi = " +almCodi) +
            (camCodiE.equals("") || camCodiE.equals("--")?"":" AND P.cam_codi = '" + camCodiE + "'")+
            " and  (select abs(sum(cl.LCI_PESO-cl.LCI_KGSORD)) from "+VISTA_INV+" as cl "+
            " where c.pro_codi = cl.pro_codi "+
            (almCodi == 0 ? "" : " and c.alm_codi = " + almCodi) +
            " and cl.cci_feccon = TO_DATE('" + cci_fecconE.getText() +"','dd-MM-yyyy')) > "+margenE.getValorDec();
       s+= " GROUP BY c.cci_codi, P.cam_codi,c.PRO_CODI,p.pro_nomb,c.PRP_ANO,c.prp_empcod,c.PRP_SERI,"+
            " c.PRP_PART,c.PRP_INDI,c.lci_coment ";
        s+=" ORDER BY c.PRO_CODI,c.PRP_PART,c.PRP_INDI";
        if (!dtCon1.select(s))
        {
            resetMsgEspere();
            msgBox("Sin registros para estas condiciones");
            if (swConsulta)
                    jt.removeAllDatos();
            this.setEnabled(true);
            return;
        } 
        // Busco los albaranes
      
        int proCodi=0;
        String lciComent;
        do
        {
             if (proCodi!=dtCon1.getInt("pro_codi")) {
                    actualizaMsg("Buscando Posibles Ventas/Compras. Producto: "+dtCon1.getInt("pro_codi"), false);
                    proCodi=dtCon1.getInt("pro_codi");
              }
              lciComent=null;
              if (dtStat.select("select c.* from v_albvenpar as p, v_albavec as c where pro_codi="+dtCon1.getInt("pro_codi")+
                            " and avp_numpar = "+dtCon1.getInt("PRP_PART")+
                            " and avp_serlot = '"+dtCon1.getString("PRP_SERI")+"'"+
                            " and avp_ejelot = "+dtCon1.getInt("PRP_ANO")+
                            " and avp_numind = "+dtCon1.getInt("PRP_INDI")+
                            " and c.avc_ano = p.avc_ano "+
                            " and c.emp_codi = p.emp_codi"+
                            " and c.avc_serie = p.avc_serie "+
                            " and c.avc_nume = p.avc_nume "+
                            //" AND avc_fecalb <= TO_DATE('" + cci_fecconE.getText() + "','dd-MM-yyyy') "+
                            " order by avc_fecalb desc "))
              { 
                  lciComent= "V:"+dtStat.getInt("emp_codi")+"-"+dtStat.getInt("avc_ano")+
                                dtStat.getString("avc_serie")+dtStat.getInt("avc_nume")+"."+
                                dtStat.getFecha("avc_fecalb","dd-MM")+
                                (dtStat.getString("avc_depos").equals("D")?"D":"");
                   if (dtStat.getString("avc_depos").equals("D"))
                   {
                    s="select * from v_albvenserv where "+
                           " pro_codi = "+dtCon1.getInt("pro_codi")+
                           " and avs_ejelot ="+dtCon1.getInt("PRP_ANO")+
                           " and avs_serlot ='"+dtCon1.getString("PRP_SERI")+"'"+
                           " and avs_numpar ="+dtCon1.getInt("PRP_PART")+
                           " and avs_numind ="+dtCon1.getInt("PRP_INDI");
                   if (dtStat.select(s))
                     lciComent+=dtStat.getFecha("avs_fecha","dd-MM");
                   }
              } 
              if (lciComent==null)
              { // Comprobar si Salio en despieces
                  s="SELECT * FROM v_despsal where  "+
                       " pro_codi = "+dtCon1.getInt("pro_codi")+
                       " and def_ejelot ="+dtCon1.getInt("PRP_ANO")+
                       " and def_serlot ='"+dtCon1.getString("PRP_SERI")+"'"+
                       " and pro_lote ="+dtCon1.getInt("PRP_PART")+
                       " and pro_numind ="+dtCon1.getInt("PRP_INDI");
                    if (dtStat.select(s))
                      lciComent= "D: "+dtStat.getInt("deo_codi")+ "("+dtStat.getFecha("deo_fecha","dd-MM")+
                          ")"+" "+ dtStat.getFecha("def_tiempo","dd-MM");                         
              }
              if (lciComent==null)
              { // Busco Compras.
                s="select acc_fecrec,prv_codi from v_compras where  pro_codi="+dtCon1.getInt("pro_codi")+
                              " and acc_nume = "+dtCon1.getInt("PRP_PART")+
                              " and acc_serie = '"+dtCon1.getString("PRP_SERI")+"'"+
                              " and acc_ano = "+dtCon1.getInt("PRP_ANO")+
                              " and acp_numind = "+dtCon1.getInt("PRP_INDI")+
                             // " AND acc_fecrec > TO_DATE('" + cci_fecconE.getText() + "','dd-MM-yyyy') "+
                              " order by acc_fecrec desc ";
                if (dtStat.select(s))
                {
                    lciComent= "C:"+dtStat.getFecha("acc_fecrec","dd-MM")+" PRV:"+
                                  dtStat.getString("prv_codi");
                }
              }
              if (dtCon1.getDouble("lci_peso")==0)
              { // Busco si hay inventarios posteriores.
                  s="select max(cci_feccon) as cci_feccon from "+VISTA_INV+" where pro_codi="+dtCon1.getInt("pro_codi")+
                              " and prp_part = "+dtCon1.getInt("PRP_PART")+
                              " and prp_seri = '"+dtCon1.getString("PRP_SERI")+"'"+
                              " and prp_ano= "+dtCon1.getInt("PRP_ANO")+
                              " and prp_indi = "+dtCon1.getInt("PRP_INDI")+
                              " AND LCI_PESO > 0"+
                              " and cci_feccon > TO_DATE('" + cci_fecconE.getText() + "','dd-MM-yyyy') ";
                  dtStat.select(s);
                  if (dtStat.getObject("cci_feccon")!=null)
                    lciComent += "I:"+dtStat.getFecha("cci_feccon","dd-MM");
                  
              }         
              if (lciComent!=null)
              {
                   s="UPDATE "+TABLA_INV_LIN+" set lci_coment = '"+
                                lciComent+"'"+
                                " where cci_codi = "+dtCon1.getInt("cci_codi")+
                                " and pro_codi ="+dtCon1.getInt("pro_codi")+
                                " and PRP_PART = "+dtCon1.getInt("PRP_PART")+
                                " and prp_seri = '"+dtCon1.getString("PRP_SERI")+"'"+
                                " and prp_ano = "+dtCon1.getInt("PRP_ANO")+
                                " and prp_indi ="+dtCon1.getInt("PRP_INDI");
                  dtAdd.executeUpdate(s);
              }
        } while (dtCon1.next());
        dtAdd.commit();
        if (swConsulta)
        {
            consultar();
            return;
        }
      //  dtCon1.setStrSelect(s);
//        debug("s: "+dtCon1.getStrSelect());

        ResultSet rs=ct.createStatement().executeQuery(dtCon1.getStrSelect());

        JasperReport jr = gnu.chu.print.util.getJasperReport(EU, "lidiexct");
        java.util.HashMap mp = new java.util.HashMap();
        mp.put("empresa",EU.em_cod);
        mp.put("fechas", cci_fecconE.getText());
        mp.put("camara", camCodiE);
        mp.put("almacen", almCodi==0?"":alm_codiE.getText());
        JasperPrint jp = JasperFillManager.fillReport(jr, mp, new JRResultSetDataSource(rs));

        
        if ( ! util.printJasper(jp, EU))
        {
          this.setEnabled(true);
          return;
        }
        resetMsgEspere();
        mensajeErr("Listado ... Generado");
        this.setEnabled(true);
      }
      catch (SQLException | JRException | PrinterException ex)
      {
        Error("Error al Generar Listado",ex);
      }
    }
    /**
     * Muestra la diferencia de inventarios en pantalla
     * @param s
     * @throws SQLException 
     */
    void consultar() throws SQLException
    {
        dtCon1.select(dtCon1.getStrSelect());
        jt.removeAllDatos();
        
        int proCodi=0;
        ArrayList<ArrayList> ar=new ArrayList();
        do
        {
            ArrayList v=new ArrayList();
            if (proCodi!=dtCon1.getInt("pro_codi"))
            {
                v.add(dtCon1.getInt("pro_codi"));
                v.add(dtCon1.getString("pro_nomb"));
                proCodi=dtCon1.getInt("pro_codi");
            }
            else
            {
                v.add("");
                v.add("");
            }
            v.add(dtCon1.getInt("prp_ano"));
            v.add(dtCon1.getString("prp_seri"));
            v.add(dtCon1.getInt("prp_part"));
            v.add(dtCon1.getInt("prp_indi"));
            v.add(dtCon1.getDouble("lci_peso"));
            v.add(dtCon1.getDouble("lci_kgsord"));
            v.add(dtCon1.getString("lci_coment"));
            ar.add(v);
        } while (dtCon1.next());
        jt.setDatos(ar);
        resetMsgEspere();
        jt.setEnabled(true);
        jt.requestFocusInicio();   
        mensajeErr("Consulta diferencias Inventario, terminada");      
    }
    /**
     * Calcula datos sobre stock ya introducidos.
     * El inventario FINAL realmente lo coge de lo que haya en v_regstock en la fecha indicada 
     * en la fecha de control.
     * 
     */
    int calcDatosStock()
    {
      try {
        
        setMensajePopEspere("Espere, por favor, Insertando Datos Inventario ...",false);
        TABLA_INV_CAB="tmp_invcab";
        TABLA_INV_LIN="tmp_invlin";   
        VISTA_INV="vista_inven";
        // Compruebo si la tabla temporal ya existe
        s= "SELECT *  FROM   pg_catalog.pg_tables  WHERE   tablename  = '"+TABLA_INV_CAB+"'";
        if (dtStat.select(s))
        {
            dtCon1.executeUpdate("drop view "+VISTA_INV);
            dtCon1.executeUpdate("drop table "+TABLA_INV_CAB);
            dtCon1.executeUpdate("drop table "+TABLA_INV_LIN);            
        }
        s="select  min(cci_codi) as cci_codi"
            + " from coninvcab where  cci_feccon= TO_DATE('" +  cci_fecconE.getText() + "','dd-MM-yyyy') " ;
        dtStat.select(s);
        int cciCodi=dtStat.getInt("cci_codi");
        String tt=" TEMP ";
//        tt="";
        s="CREATE "+tt+" TABLE  "+TABLA_INV_CAB+" as select * from coninvcab where cci_codi="+cciCodi;
        dtCon1.executeUpdate(s);
        s="CREATE "+tt+"  TABLE "+TABLA_INV_LIN+" as select * from coninvlin where cci_codi=0";
        dtCon1.executeUpdate(s);

        
        s="create OR REPLACE  "+tt+" view "+VISTA_INV+" as"+
          " select c.emp_codi,c.cci_codi,c.usu_nomb,cci_feccon, cam_codi,alm_codi,lci_nume,prp_ano, prp_empcod, prp_seri, prp_part, pro_codi, pro_nomb,"+
          " prp_indi,lci_peso,lci_kgsord,lci_numind,lci_regaut,lci_coment,lci_numpal from "+
            TABLA_INV_CAB+" as c, "+
            TABLA_INV_LIN+" as l where"+
          " c.emp_codi=c.emp_codi"+
          " and c.cci_codi=l.cci_codi";   
         dtCon1.executeUpdate(s);
        s= " select * FROM v_inventar WHERE " +
          "  rgs_kilos <> 0"+
          (almCodi==0?"":" and alm_codi = "+almCodi)+
          " AND rgs_fecha = TO_DATE('" +  cci_fecconE.getText() + "','dd-MM-yyyy') " ;
        if (!dtCon1.select(s))
            return 0;
        int lciNume=0;
        do
        {
              dtAdd.addNew(TABLA_INV_LIN);
              dtAdd.setDato("cci_codi", cciCodi);
              dtAdd.setDato("emp_codi", EU.em_cod);
              dtAdd.setDato("lci_nume", ++lciNume);
              dtAdd.setDato("lci_numind", dtCon1.getDouble("rgs_canti"));
              dtAdd.setDato("prp_ano", dtCon1.getInt("eje_nume"));
              dtAdd.setDato("prp_empcod", EU.em_cod);
              dtAdd.setDato("prp_seri", dtCon1.getString("pro_serie"));
              dtAdd.setDato("prp_part", dtCon1.getInt("pro_nupar"));
              dtAdd.setDato("prp_indi", dtCon1.getInt("pro_numind"));
              dtAdd.setDato("pro_codi", dtCon1.getInt("pro_codi"));
              dtAdd.setDato("pro_nomb", "");
              dtAdd.setDato("lci_peso", dtCon1.getDouble("rgs_kilos"));
              dtAdd.setDato("lci_kgsord", 0);
              dtAdd.setDato("lci_regaut", 0);
              dtAdd.update();
        } while (dtCon1.next());
        return cciCodi;
      } catch (SQLException k)
      {
          Error("Error al introducir datos de stock",k);
          return 0;
      }
    }
    
    /**
     * Calcula datos de nuevo, metiendo los resultados en la tabla
     * de inventarios.
     */
    @SuppressWarnings("CallToThreadDumpStack")
    boolean calcDatos(int cciCodi) 
    {
        setMensajePopEspere("Espere, por favor, Buscando Datos ...",false);
        
      
        int lciNume;
        double canti;
        char tipMov;

        int nLec = 0;
        HashMap<String,Double> ht = new HashMap(5000);
        Double cant;
        String ref;
        Iterator<String> pr;
        String[] sArray;
        int proCodi = 0, ejeNume, lote, numind;
        String serie;
        String feulin;
        try {
            
            feulin = feulinE.getText();
            
            s = getStrSql(feulin, cci_fecconE.getText());
            if (!dtCon1.select(s)) {
                resetMsgEspere();
                mensajeErr("No encontrado Movimientos entre estas fechas");
                return false;
            }
            setMensajePopEspere("Tratando datos ...",true);
//        debug("calcDatos: "+dtCon1.getStrSelect()+"\nCci_codi: "+cciCodi);
//        debug("S: "+dtCon1.getStrSelect());
            // Pongo a 0 los Individuos y Kgs.
            s = "UPDATE "+TABLA_INV_LIN+" set  lci_kgsord=0,lci_coment = null "
                    + " where emp_codi = " + EU.em_cod
                    + " and  cci_codi IN (SELECT cci_codi from "+TABLA_INV_CAB+ " c "
                    + " where c.emp_codi =" + EU.em_cod
                    + " and c.cci_feccon = TO_DATE('" + cci_fecconE.getText() + "','dd-MM-yyyy')) ";
            swLeeOrd = true;
            dtCon1.setSqlUpdate(s);
//        debug("Reseteados los kilos de ordenador: "+dtCon1.getSqlUpdate());
            stUp.executeUpdate(dtCon1.getSqlUpdate());

            // Borro los individuos que no tenga stock real.
            s = "DELETE FROM "+ TABLA_INV_LIN
                    + " where emp_codi = " + EU.em_cod
                    + " and  cci_codi IN (SELECT cci_codi from "+TABLA_INV_CAB+" c where "
                    + " c.emp_codi =" + EU.em_cod
                    + " and c.cci_feccon = TO_DATE('" + cci_fecconE.getText() + "','dd-MM-yyyy') "
                    + " ) "
                    + " and lci_peso = 0 ";
            dtCon1.setSqlUpdate(s);

            stUp.executeUpdate(dtCon1.getSqlUpdate());
            /**
             * Busco máximo numero de linea de inventarios
             */
            s = "SELECT MAX(lci_nume) as lci_nume from "+ TABLA_INV_LIN+" where emp_codi = " + EU.em_cod
                    + " and cci_codi = " + cciCodi;
            dtStat.select(s);
            lciNume = dtStat.getInt("lci_nume", true);
            // Variables para temas de debug
            double totInv = 0, totReg = 0, totVen = 0;
            double totCom = 0, totDeEnt = 0, totDeSal = 0;

            /**
             * Guardo en un HashTable(ht) los kilos para todos los productos, según el ordenador.
             */
            do {
                ref = //dtCon1.getInt("almori")+
                    dtCon1.getInt("pro_codi") + "|" + dtCon1.getInt("eje_nume", true) + "|"                       
                        + dtCon1.getString("serie") + "|"
                        + dtCon1.getInt("lote", true) + "|"
                        + dtCon1.getInt("numind", true);
                cant =  ht.get(ref);
                if (cant == null) {
                    canti = 0;
                } else {
                    canti = cant;
                }
                nLec++;
                tipMov = dtCon1.getString("tipmov").charAt(0);
                if (tipMov=='E')
                    tipMov='+';
                if (tipMov=='S')
                    tipMov='-';
                if (tipMov=='=') {
                    canti = dtCon1.getDouble("canti", true);
                    totInv += dtCon1.getDouble("canti", true);
                }
                if (tipMov=='+') 
                {
                    canti = canti + dtCon1.getDouble("canti", true);
                    if (dtCon1.getString("sel").equals("C")) {
                        totCom += dtCon1.getDouble("canti", true);
                    }
                    if (dtCon1.getString("sel").equals("d")) {
                        totDeEnt += dtCon1.getDouble("canti", true);
                    }
                }
                if (tipMov=='-') 
                {
                    canti = canti - dtCon1.getDouble("canti", true);
                    if (dtCon1.getString("sel").equals("V")) {
                        totVen += dtCon1.getDouble("canti", true);
                    }
                    if (dtCon1.getString("sel").equals("R")) {
                        totReg += dtCon1.getDouble("canti", true);
                    }
                    if (dtCon1.getString("sel").equals("D")) {
                        totDeSal += dtCon1.getDouble("canti", true);
                    }
                }
                ht.put(ref, canti);
                if (nLec % 100 == 0) {
                    setMensajePopEspere("Calculando Datos. Leidos " + nLec + " Registros - Individuos: " + ht.size(),false);
                }
            } while (dtCon1.next());
//            actualizaMsg("Calculando Datos .... \n Leidos " + nLec + " Registros - Individuos: " + ht.size() + " **", false);
        } catch (SQLException ex1) {
//        ex1.printStackTrace();
            Error("1.- Error al Actualizar Datos ", ex1);
            return false;
        }
        double canOri = 0;
        actualizaMsg("Calculando Datos. Insertando en temporales",false);
        try {
            nLec = 1;
            pr = ht.keySet().iterator();
            while (pr.hasNext()) 
            {
                nLec++;
                if (nLec % 100 == 0) {
                    actualizaMsg("Calculando Datos. Insertando registro N. " + nLec + " de: " + ht.size(), false);
                }
               
                ref =  pr.next();
                canti =  ht.get(ref);
               
//                System.out.println(ref+"|"+canti);
                if (canti == 0) {
                    continue;
                }
                sArray = ref.split("\\|");
                proCodi = Integer.parseInt(sArray[0]);
                ejeNume = Integer.parseInt(sArray[1]);
              
                serie = sArray[2];
                lote = Integer.parseInt(sArray[3]);
                numind = Integer.parseInt(sArray[4]);

                s = " SELECT l.* FROM "+TABLA_INV_LIN+" l,"+TABLA_INV_CAB+" c WHERE pro_codi = " + proCodi
                        + " AND prp_ano = " + ejeNume
                        + " and l.prp_empcod = " + EU.em_cod
                        + " and c.emp_codi = " + EU.em_cod
                        + " and c.cci_feccon = TO_DATE('" + cci_fecconE.getText() + "','dd-MM-yyyy') "
                        + (usu_nombE.isNull()?"":" and usu_nomb ='"+usu_nombE.getText()+"'")
                        + " and c.cci_codi = l.cci_codi "
                        + " and c.emp_codi = l.emp_codi "
                        + " and prp_seri = '" + serie + "'"
                        + " and prp_part = " + lote
                        + " and prp_indi = " + numind;
                if (dtAdd.select(s, true)) 
                { // Actualizo el Stock.
                    s = "emp_codi = " + EU.em_cod
                            + " AND cci_codi = " + dtAdd.getInt("cci_codi")
                            + " and lci_nume = " + dtAdd.getInt("lci_nume");
                    dtAdd.edit(s); 
                    canOri = dtAdd.getDouble("lci_kgsord")+canti;
                    dtAdd.setDato("lci_kgsord", canOri );
//                    canOri=canOri-dtAdd.getDouble("lci_peso");
                } 
                else
                {
                    dtAdd.addNew(TABLA_INV_LIN);
                    dtAdd.setDato("cci_codi", cciCodi);
                    dtAdd.setDato("emp_codi", EU.em_cod);
                    dtAdd.setDato("lci_nume", ++lciNume);
                    dtAdd.setDato("lci_numind", 1);
                    dtAdd.setDato("prp_ano", ejeNume);
                    dtAdd.setDato("prp_empcod", EU.em_cod);
                    dtAdd.setDato("prp_seri", serie);
                    dtAdd.setDato("prp_part", lote);
                    dtAdd.setDato("prp_indi", numind);
                    dtAdd.setDato("pro_codi", proCodi);
                    dtAdd.setDato("pro_nomb", "");
                    dtAdd.setDato("lci_peso", 0);
                    dtAdd.setDato("lci_kgsord", Formatear.redondea(canti, 2));
                    dtAdd.setDato("lci_regaut", 1);
                    canOri=canti;
                }
                dtAdd.update(stUp);
            }
            ctUp.commit();
            
        } catch (Exception ex1) {
            SystemOut.print(ex1);
            try {
                Error("2.- Error al Actualizar Datos. proCodi: " + proCodi + " Canti: "
                        + canti
                        + " CanOri: " + canOri + " getSqlUpdate: " + dtAdd.getSqlUpdate(),
                        ex1);
            } catch (Exception k) {
               SystemOut.print(k);
            }
            return false;
        }
        return true;
    }
/**
 * Monta la select que buscara los movimientos de salida y entrada 
 * en todas las tablas necesarias, entre la fecha de Ultimo Stock Pasado y la 
 * Fecha de Control de inventario.
 * @param feulst Fecha Ultimo Stock pasado (Fecha inferior)
 * @param fecStockStr Fecha Control Stock  (Fecha Superior)
 * @return String con la SQL a ejecutar
 */
    private String getStrSql(String feulst, String fecStockStr)
    {
        String condProd = " and a.pro_tiplot = 'V' ";
        if (pro_artconE.getValorInt() != 2)
            condProd += " and a.pro_artcon " + (pro_artconE.getValorInt() == 0 ? "= 0" : " <> 0");
        boolean incDep = leidoDepoC.isSelected() && !opDatStock.isSelected();
        String condCamaras = "(select cam_codi from " + TABLA_INV_CAB
                + " where emp_codi = " + EU.em_cod + 
                " and cci_feccon = TO_DATE('" + cci_fecconE.getText() + "','dd-MM-yyyy'))";

        if (opMvtos.isSelected())
        {
            s = "SELECT 0 as orden,mvt_tipdoc as sel, mvt_tipo as tipmov,  "
                + " mvt_time as fecmov,"
                + "  pro_serlot as serie,pro_numlot as  lote,"
                + " mvt_canti as canti,pro_indlot as numind,"
                + " m.pro_codi,"
                + " pro_ejelot as eje_nume,"
                + " m.alm_codi,mvt_serdoc as seralb "
                + " from mvtosalm as m,v_articulo as a where "
                + "  mvt_canti <> 0 "
                + " and a.pro_codi = m.pro_codi "
                + (almCodi == 0 ? "" : " and m.alm_codi = " + almCodi)
                + (camCodiE.equals("--") ? " and a.cam_codi in "
                    + condCamaras
                    : (camCodiE.equals("") ? "" : " and a.cam_codi = '" + camCodiE + "'"))
                + (LOTE <= 0 ? "" : " and pro_numlot  = " + LOTE)
                + (PROCODI != 0 ? " and a.pro_codi = " + PROCODI : "")
                + " AND mvt_time::date > TO_DATE('" + feulst + "','dd-MM-yyyy') "
                + " and mvt_time::date <= TO_DATE('" + fecStockStr + "','dd-MM-yyyy') ";
            if (incDep)
            {
                s += " union all " // Incluyo salidas de deposito.
                    + " select 2 as orden,'V' as sel,'-' as tipmov,"
                    + "avs_fecha as fecmov,"
                    + "  avs_serlot as serie,avs_numpar as  lote,"
                    + " avs_canti as canti,avs_numind as numind, "
                    + " a.pro_codi,avs_ejelot as eje_nume,alm_codori as almori,avc_serie AS seralb "
                    + "  from v_albvenserv as al, v_articulo a "
                    + " WHERE avs_canti <> 0 "
                    + " and a.pro_codi = al.pro_codi "
                    + (PROCODI != 0 ? " and a.pro_codi = " + PROCODI : "")
                    + (LOTE >= 0 ? " and avs_numpar = " + LOTE : "")
                    + (almCodi == 0 ? "" : " and alm_codori = " + almCodi)
                    + (camCodiE.equals("--") ? " and a.cam_codi in "
                    + condCamaras
                    : (camCodiE.equals("") ? "" : " and a.cam_codi = '" + camCodiE + "'"))
                    + condProd
                    + " and avs_fecha > TO_DATE('" + feulst + "','dd-MM-yyyy') "
                    + " and avs_fecha <= TO_DATE('" + fecStockStr + "','dd-MM-yyyy') "
                    + (almCodi == 0 ? "" : " and alm_codori = " + almCodi);
                s += " union all " + // Anulo Mvtos q pertenezcan  a albaranes de Deposito
                    "SELECT 0 as orden,mvt_tipdoc as sel, '+' as tipmov,  "
                    + " mvt_time as fecmov,"
                    + "  pro_serlot as serie,pro_numlot as  lote,"
                    + " mvt_canti as canti,pro_indlot as numind,"
                    + " m.pro_codi,"
                    + " pro_ejelot as eje_nume,"
                    + " m.alm_codi,mvt_serdoc as seralb "
                    + " from mvtosalm as m,v_articulo as a,v_albavec as c where "
                    + "  mvt_canti <> 0 "
                    + " and mvt_tipdoc = 'V' "
                    + " and c.avc_depos='D'"
                    + " and c.emp_codi = m.mvt_empcod "
                    + " and c.avc_ano = m.mvt_ejedoc "
                    + " and c.avc_serie=m.mvt_serdoc "
                    + " and c.avc_nume =m.mvt_numdoc "
                    + " and a.pro_codi = m.pro_codi "
                    + (almCodi == 0 ? "" : " and alm_codi = " + almCodi)
                    + (camCodiE.equals("--") ? " and a.cam_codi in "
                    + condCamaras
                    : (camCodiE.equals("") ? "" : " and a.cam_codi = '" + camCodiE + "'"))
                    + (LOTE <= 0 ? "" : " and pro_numlot  = " + LOTE)
                    + (PROCODI != 0 ? " and a.pro_codi = " + PROCODI : "")
                    + " AND mvt_time::date > TO_DATE('" + feulst + "','dd-MM-yyyy') "
                    + " and mvt_time::date <= TO_DATE('" + fecStockStr + "','dd-MM-yyyy') ";
            }          
        }
        else
        {
            s = // Albaranes de Compras
                "SELECT 1 as orden,'C' as sel,'+' as tipmov,c.acc_fecrec as fecmov, c.acc_serie as serie,"
                + " c.acc_nume as  lote,"
                + " acp_canti as canti,acp_numind as numind, "
                + " c.pro_codi,c.acc_ano as eje_nume,alm_codi as almori,'' AS seralb "
                + " FROM v_compras as c, v_articulo a "
                + " where  c.acc_fecrec between TO_DATE('" + feulst + "','dd-MM-yyyy') "
                + " and  TO_DATE('" + fecStockStr + "','dd-MM-yyyy') "
                + " and c.pro_codi = a.pro_codi "
                + (almCodi == 0 ? "" : " and c.alm_codi = " + almCodi)
                + (camCodiE.equals("--") ? " and a.cam_codi in "
                    + condCamaras
                    : (camCodiE.equals("") ? "" : " and a.cam_codi = '" + camCodiE + "'"))
                + (PROCODI != 0 ? " and c.pro_codi = " + PROCODI : "")
                + (LOTE >= 0 ? " and c.acc_nume = " + LOTE : "")
                + condProd;
            s += " UNION all"; // Albaranes de Venta
            String condAlb = " where  a.pro_codi = l.pro_codi "
                + " and l.avp_canti <> 0 "
                + (camCodiE.equals("--") ? " and a.cam_codi in "
                    + condCamaras
                    : (camCodiE.equals("") ? "" : " and a.cam_codi = '" + camCodiE + "'"))
                + (PROCODI != 0 ? " and l.PRo_codi = " + PROCODI : "")
                + (LOTE >= 0 ? " and l.avp_numpar = " + LOTE : "")
                + condProd
                + " AND l.avl_fecalt::date > TO_DATE('" + feulst + "','dd-MM-yyyy') "
                + " and l.avl_fecalt::date <= TO_DATE('" + fecStockStr + "','dd-MM-yyyy') ";
            s += " select 2 as orden,'V' as sel,'-' as tipmov,l.avl_fecalt as fecmov,"
                + "  l.avp_serlot as serie,l.avp_numpar as  lote,"
                + " l.avp_canti as canti,l.avp_numind as numind, "
                + " l.pro_codi,l.avp_ejelot as eje_nume,"
                + "l.alm_codori as almori,l.avc_serie AS seralb "
                + "  from v_albventa_detalle as l,v_articulo a"
                + condAlb
                + " and l.avc_serie != 'X'" + // No incluir traspasos entre almacenes
                (incDep ? " and l.avc_depos != 'D' " : ""); // No tratar los albaranes de DEPOSITO.

            if (incDep)
            {
                s += " UNION ALL select 2 as orden,'V' as sel,'-' as tipmov,avs_fecha as fecmov,"
                    + "  avs_serlot as serie,avs_numpar as  lote,"
                    + " avs_canti as canti,avs_numind as numind, "
                    + " a.pro_codi,avs_ejelot as eje_nume,alm_codori as almori,avc_serie AS seralb "
                    + "  from v_albvenserv as al, v_articulo a "
                    + " WHERE avs_canti <> 0 "
                    + " and a.pro_codi = al.pro_codi "
                    + (almCodi == 0 ? "" : " and m.alm_codori = " + almCodi)                  
                    + (camCodiE.equals("--") ? " and a.cam_codi in "
                        + condCamaras
                        : (camCodiE.equals("") ? "" : " and a.cam_codi = '" + camCodiE + "'"))
                    + (PROCODI != 0 ? " and a.pro_codi = " + PROCODI : "")
                    + (LOTE >= 0 ? " and avs_numpar = " + LOTE : "")
                    + condProd
                    + " and avc_depos = 'D' "
                    + " and avs_fecha > TO_DATE('" + feulst + "','dd-MM-yyyy') "
                    + " and avs_fecha <= TO_DATE('" + fecStockStr + "','dd-MM-yyyy') "
                    + (almCodi == 0 ? "" : " and alm_codori = " + almCodi);
            }

            if (almCodi != 0)
            { // Incluir Traspasos entre almacenes
                s += " UNION ALL  select  1 as orden,'V' as sel,'-' as tipmov,l.avc_fecalb as fecmov,"
                    + "  l.avp_serlot as serie,l.avp_numpar as  lote,"
                    + " l.avp_canti as canti,l.avp_numind as numind, "
                    + " l.pro_codi,l.avp_ejelot as eje_nume,l.alm_codori as almori,"
                    + "l.avc_serie AS seralb "
                    + "  from v_albventa_detalle as l,v_articulo a"
                    + condAlb
                    + " and l.avc_serie = 'X'" + // Incluir Serie X
                    " and l.alm_codori = " + almCodi
                    + " UNION ALL "
                    + " select  2 as orden,'V' as sel,'-' as tipmov,l.avc_fecalb as fecmov,"
                    + "  l.avp_serlot as serie,l.avp_numpar as  lote,"
                    + " l.avp_canti*-1 as canti,l.avp_numind as numind , "
                    + " l.pro_codi,l.avp_ejelot as eje_nume,l.alm_coddes as almori,"
                    + "l.avc_serie AS seralb "
                    + "  from v_albventa_detalle as l,v_articulo a"
                    + condAlb
                    + " and l.avc_serie = 'X'" + // Incluir Serie X
                    " and l.alm_coddes = " + almCodi;
            }
            s += " UNION all " + // Despieces (Salidas de almacen)
                " select 2 as orden,'D' as sel,'-' as tipmov,deo_tiempo as fecmov,"
                + "  deo_serlot as serie,pro_lote as  lote,"
                + " deo_kilos as canti,pro_numind as numind, "
                + " L.pro_codi,deo_ejelot as eje_nume,0 as almori,'' AS seralb "
                + " from  v_despori l,v_articulo a where "
                + "  a.pro_codi = l.pro_codi "
                + " and deo_kilos <> 0 "
                + (camCodiE.equals("--") ? " and a.cam_codi in "
                    + condCamaras
                    : (camCodiE.equals("") ? "" : " and a.cam_codi = '" + camCodiE + "'"))
                + (almCodi == 0 ? "" : " and deo_almori = " + almCodi)
                + (PROCODI != 0 ? " AND  L.PRo_codi = " + PROCODI : "")
                + (LOTE >= 0 ? " and L.pro_lote = " + LOTE : "")
                + condProd
                + " and deo_tiempo::date > TO_DATE('" + feulst + "','dd-MM-yyyy') "
                + " and deo_tiempo::date <= TO_DATE('" + fecStockStr + "','dd-MM-yyyy') ";
            s += " UNION all " + // Despieces (Entradas a almacen)
                " select 1 as orden,'d' as sel, '+' as tipmov,d.def_tiempo as fecmov,"
                + "  d.def_serlot as serie,d.pro_lote as  lote,"
                + " d.def_kilos as canti,d.pro_numind as numind, "
                + " d.pro_codi,d.def_ejelot as eje_nume,0 as numind,'' AS seralb "
                + " from  v_despsal as d,v_articulo a where "
                + "  d.def_kilos <> 0 "
                + " and d.pro_codi= a. pro_codi "
                + (almCodi == 0 ? "" : " and d.alm_codi = " + almCodi)
                + (camCodiE.equals("--") ? " and a.cam_codi in "
                    + condCamaras
                    : (camCodiE.equals("") ? "" : " and a.cam_codi = '" + camCodiE + "'"))
                + (PROCODI != 0 ? " and  d.PRo_codi = " + PROCODI : "")
                + (LOTE >= 0 ? " and d.pro_lote = " + LOTE : "")
                + condProd
                + " AND d.def_tiempo::date > TO_DATE('" + feulst + "','dd-MM-yyyy') "
                + " and d.def_tiempo::date <= TO_DATE('" + fecStockStr + "','dd-MM-yyyy') ";
            s += " UNION all " + // Regularizaciones.
                " select 3 as orden,'R' as sel,tir_afestk as tipmov,r.rgs_fecha as fecmov,"
                + "  r.pro_serie as serie,r.pro_nupar as  lote,"
                + " r.rgs_kilos as canti,r.pro_numind as numind, "
                + " R.pro_codi, eje_nume,alm_codi as almori,'' AS seralb "
                + " FROM v_regstock r,v_articulo a WHERE "
                + " a.pro_codi =r.pro_codi "
                + " and r.rgs_kilos <> 0"
                + //          " and rgs_trasp != 0 "+
                (almCodi == 0 ? "" : " and r.alm_codi = " + almCodi)
                + (camCodiE.equals("--") ? " and a.cam_codi in "
                    + condCamaras
                    : (camCodiE.equals("") ? "" : " and a.cam_codi = '" + camCodiE + "'"))
                + (PROCODI != 0 ? "  and R.PRo_codi = " + PROCODI : "")
                + (LOTE >= 0 ? " and r.pro_nupar = " + LOTE : "")
                + condProd
                + " and tir_afestk != '=' "
                + " AND r.rgs_fecha > TO_DATE('" + feulst + "','dd-MM-yyyy') "
                + " and r.rgs_fecha <= TO_DATE('" + fecStockStr + "','dd-MM-yyyy') ";              
        }
        
        if (incDep)
        {
            s += " UNION ALL select 0 as orden,'R' as sel,'=' as tipmov,r.ind_fecha as fecmov,"
                + "  r.pro_serie as serie,r.pro_nupar as  lote,"
                + " r.ind_kilos as canti,r.pro_numind as numind, "
                + " R.pro_codi, eje_nume, alm_codi as almori,'' AS seralb "
                + " FROM invdepos r,  v_articulo a WHERE "
                + " a.pro_codi =r.pro_codi "
                + (almCodi == 0 ? "" : " and r.alm_codi = " + almCodi)
                + (camCodiE.equals("--") ? " and a.cam_codi in "
                    + condCamaras
                    : (camCodiE.equals("") ? "" : " and a.cam_codi = '" + camCodiE + "'"))
                + (PROCODI != 0 ? "  and r.PRo_codi = " + PROCODI : "")
                + (LOTE >= 0 ? " and r.pro_nupar = " + LOTE : "")
                + condProd
                + " AND r.ind_fecha = TO_DATE('" + feulst + "','dd-MM-yyyy') ";
        }
        s += " union all "+// Inventario Inicial.
            " select 0 as orden,'R' as sel,'=' as tipmov,r.rgs_fecha as fecmov,"
            + "  r.pro_serie as serie,r.pro_nupar as  lote,"
            + " r.rgs_kilos as canti,r.pro_numind as numind, "
            + " R.pro_codi, eje_nume,rgs_canti as almori,'' AS seralb "
            + " FROM v_inventar r,v_articulo a WHERE "
            + " a.pro_codi =r.pro_codi "
            + " and r.rgs_kilos <> 0"
            + //          " and rgs_trasp != 0 "+
            (almCodi == 0 ? "" : " and r.alm_codi = " + almCodi)
            + (camCodiE.equals("--") ? " and a.cam_codi in "
                + condCamaras
                : (camCodiE.equals("") ? "" : " and a.cam_codi = '" + camCodiE + "'"))
            + (PROCODI != 0 ? "  and R.PRo_codi = " + PROCODI : "")
            + (LOTE >= 0 ? " and r.pro_nupar = " + LOTE : "")
            + condProd
            + " AND r.rgs_fecha = TO_DATE('" + feulst + "','dd-MM-yyyy') ";
        s += " ORDER BY 4,1,3 desc"; // FECHA y tipo
        return s;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        Pgeneral = new gnu.chu.controles.CPanel();
        Pcondic = new gnu.chu.controles.CPanel();
        cLabel1 = new gnu.chu.controles.CLabel();
        cLabel2 = new gnu.chu.controles.CLabel();
        feulinE = new gnu.chu.controles.CComboBox();
        cLabel3 = new gnu.chu.controles.CLabel();
        alm_codiE = new gnu.chu.controles.CLinkBox();
        cLabel4 = new gnu.chu.controles.CLabel();
        cam_codiE = new gnu.chu.controles.CLinkBox();
        cLabel5 = new gnu.chu.controles.CLabel();
        pro_loteE = new gnu.chu.controles.CTextField(Types.DECIMAL,"####9");
        cLabel6 = new gnu.chu.controles.CLabel();
        pro_codiE = new gnu.chu.camposdb.proPanel();
        cLabel7 = new gnu.chu.controles.CLabel();
        pro_artconE = new gnu.chu.controles.CComboBox();
        cLabel8 = new gnu.chu.controles.CLabel();
        usu_nombE = new gnu.chu.controles.CTextField(Types.CHAR, "X", 15);
        opCalInv = new gnu.chu.controles.CCheckBox();
        cLabel9 = new gnu.chu.controles.CLabel();
        margenE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#9.9");
        leidoDepoC = new gnu.chu.controles.CCheckBox();
        Baceptar = new gnu.chu.controles.CButtonMenu();
        cci_fecconE = new gnu.chu.anjelica.inventario.PfechaInv();
        opDatStock = new gnu.chu.controles.CCheckBox();
        opMvtos = new gnu.chu.controles.CCheckBox();
        jt = new gnu.chu.controles.Cgrid(9);

        Pgeneral.setLayout(new java.awt.GridBagLayout());

        Pcondic.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        Pcondic.setMaximumSize(new java.awt.Dimension(440, 150));
        Pcondic.setMinimumSize(new java.awt.Dimension(440, 150));
        Pcondic.setPreferredSize(new java.awt.Dimension(440, 150));
        Pcondic.setLayout(null);

        cLabel1.setText("Almacen");
        Pcondic.add(cLabel1);
        cLabel1.setBounds(10, 30, 70, 17);

        cLabel2.setText("Fecha Control ");
        Pcondic.add(cLabel2);
        cLabel2.setBounds(10, 10, 80, 17);
        Pcondic.add(feulinE);
        feulinE.setBounds(330, 10, 90, 17);

        cLabel3.setText("Ult. Inventario");
        Pcondic.add(cLabel3);
        cLabel3.setBounds(250, 10, 80, 17);

        alm_codiE.setAncTexto(30);
        alm_codiE.setFormato(Types.DECIMAL, "#9", 2);
        Pcondic.add(alm_codiE);
        alm_codiE.setBounds(80, 30, 310, 17);

        cLabel4.setText("Usuario");
        Pcondic.add(cLabel4);
        cLabel4.setBounds(260, 90, 50, 17);

        cam_codiE.setAncTexto(30);
        cam_codiE.texto.setMayusc(true);
        cam_codiE.setFormato(Types.CHAR,"XX",2);
        Pcondic.add(cam_codiE);
        cam_codiE.setBounds(80, 50, 230, 17);

        cLabel5.setText("Lote");
        Pcondic.add(cLabel5);
        cLabel5.setBounds(320, 50, 25, 15);
        Pcondic.add(pro_loteE);
        pro_loteE.setBounds(370, 50, 50, 17);

        cLabel6.setText("Camara");
        Pcondic.add(cLabel6);
        cLabel6.setBounds(10, 50, 60, 17);
        Pcondic.add(pro_codiE);
        pro_codiE.setBounds(80, 70, 340, 17);

        cLabel7.setText("Producto");
        Pcondic.add(cLabel7);
        cLabel7.setBounds(10, 70, 60, 17);

        pro_artconE.addItem("Fresco","0");
        pro_artconE.addItem("Congelado","-1");
        pro_artconE.addItem("Ambos","2");
        Pcondic.add(pro_artconE);
        pro_artconE.setBounds(110, 90, 140, 17);

        cLabel8.setText("Incluir Productos ");
        Pcondic.add(cLabel8);
        cLabel8.setBounds(10, 90, 100, 17);
        Pcondic.add(usu_nombE);
        usu_nombE.setBounds(310, 90, 110, 17);

        opCalInv.setSelected(true);
        opCalInv.setText("Calcular Inventarios");
        Pcondic.add(opCalInv);
        opCalInv.setBounds(150, 130, 140, 17);

        cLabel9.setText("Umbral Kilos ");
        cLabel9.setToolTipText("Ignorar si Kilos por producto es inferior que el valor introducido ");
        Pcondic.add(cLabel9);
        cLabel9.setBounds(10, 110, 90, 15);

        margenE.setText("1");
        Pcondic.add(margenE);
        margenE.setBounds(90, 110, 40, 17);

        leidoDepoC.setSelected(true);
        leidoDepoC.setText("Leido Depositos");
        Pcondic.add(leidoDepoC);
        leidoDepoC.setBounds(140, 110, 140, 17);

        Baceptar.setText("Aceptar");
        Baceptar.addMenu("Consultar");
        Baceptar.addMenu("Listar");
        Pcondic.add(Baceptar);
        Baceptar.setBounds(290, 120, 130, 26);
        Pcondic.add(cci_fecconE);
        cci_fecconE.setBounds(90, 10, 100, 18);

        opDatStock.setText("Stock");
        opDatStock.setToolTipText("Usar Inventario ya Traspasado");
        opDatStock.setFocusable(false);
        opDatStock.setMaximumSize(new java.awt.Dimension(41, 17));
        Pcondic.add(opDatStock);
        opDatStock.setBounds(190, 10, 60, 17);

        opMvtos.setSelected(true);
        opMvtos.setText("Usar Mvtos.");
        Pcondic.add(opMvtos);
        opMvtos.setBounds(10, 130, 130, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        Pgeneral.add(Pcondic, gridBagConstraints);

        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        ArrayList v=new ArrayList();
        v.add("Producto"); //0
        v.add("Nombre"); // 1
        v.add("Ejer"); // 2
        v.add("Serie"); // 3
        v.add("Lote"); //4
        v.add("Ind"); // 5
        v.add("K.Inv"); // 6
        v.add("K.Cal"); // 7
        v.add("Albaran"); // 8
        jt.setCabecera(v);
        jt.setAjustarGrid(true);
        jt.setFormatoColumna(6, "---,--9.99");
        jt.setFormatoColumna(7, "---,--9.99");
        jt.setAlinearColumna(new int[]{2,0,2,1,2,2,2,2,0});
        jt.setAnchoColumna(new int[]{50,150,40,30,45,40,60,60,80});

        javax.swing.GroupLayout jtLayout = new javax.swing.GroupLayout(jt);
        jt.setLayout(jtLayout);
        jtLayout.setHorizontalGroup(
            jtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 526, Short.MAX_VALUE)
        );
        jtLayout.setVerticalGroup(
            jtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 209, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        Pgeneral.add(jt, gridBagConstraints);

        getContentPane().add(Pgeneral, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButtonMenu Baceptar;
    private gnu.chu.controles.CPanel Pcondic;
    private gnu.chu.controles.CPanel Pgeneral;
    private gnu.chu.controles.CLinkBox alm_codiE;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.controles.CLabel cLabel9;
    private gnu.chu.controles.CLinkBox cam_codiE;
    private gnu.chu.anjelica.inventario.PfechaInv cci_fecconE;
    private gnu.chu.controles.CComboBox feulinE;
    private gnu.chu.controles.Cgrid jt;
    private gnu.chu.controles.CCheckBox leidoDepoC;
    private gnu.chu.controles.CTextField margenE;
    private gnu.chu.controles.CCheckBox opCalInv;
    private gnu.chu.controles.CCheckBox opDatStock;
    private gnu.chu.controles.CCheckBox opMvtos;
    private gnu.chu.controles.CComboBox pro_artconE;
    private gnu.chu.camposdb.proPanel pro_codiE;
    private gnu.chu.controles.CTextField pro_loteE;
    private gnu.chu.controles.CTextField usu_nombE;
    // End of variables declaration//GEN-END:variables
}
