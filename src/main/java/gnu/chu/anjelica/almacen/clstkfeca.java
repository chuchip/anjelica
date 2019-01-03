
package gnu.chu.anjelica.almacen;
import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.despiece.MantDesp;
import gnu.chu.anjelica.despiece.utildesp;
import gnu.chu.anjelica.pad.MantFamPro;
import gnu.chu.camposdb.empPanel;
import gnu.chu.controles.StatusBar;
import gnu.chu.interfaces.ejecutable;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.miThread;
import gnu.chu.utilidades.ventana;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *   Consulta Listado de Stock Agrupandolos por fecha caducidad.
 *   Saca los datos de la tabla de stock-partidas (v_stkpart)
 *
 *  <p>  Copyright: Copyright (c) 2005-2012
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los terminos de la Licencia Pública General de GNU según es publicada por
 *  la Free Software Foundation, bien de la versión 2 de dicha Licencia
 *  o bien (según su elección) de cualquier versión posterior.
 *  Este programa se distribuye con la esperanza de que sea útil,ed
 *  pero SIN NINGUNA GARANTIA, incluso sin la garantía MERCANTIL implícita
 *  o sin garantizar la CONVENIENCIA PARA UN PROPOSITO PARTICULAR.
 *  Véase la Licencia Pública General de GNU para más detalles.
 *  Debería haber recibido una copia de la Licencia Pública General junto con este programa.
 *  Si no ha sido así, escriba a la Free Software Foundation, Inc.,
 *  en 675 Mass Ave, Cambridge, MA 02139, EEUU.
 * </p>
 *
 * @author cpuente
 */
public class clstkfeca extends ventana
{
  private boolean swPrimeraVez=true;
  private String filtroEmp=null;
  private boolean cancelarConsulta=false;
  utildesp utdesp;
  DatosTabla dtDesp;
  private  int uniAct,uniTot;
  private double kilAct,kilTot,impAct,impTot;
  private int nDiasAgr;
  boolean PARAM_VERCOSTOS=false;
  
  public clstkfeca(EntornoUsuario eu, Principal p)
  {
      this(eu,p, null);
  }
  public clstkfeca(EntornoUsuario eu, Principal p,Hashtable ht)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    setTitulo("Cons/List Stock con Fechas Caducidad");

    try
    {
      if (ht != null)
      {
        if (ht.get("verCostos") != null)
          PARAM_VERCOSTOS = Boolean.valueOf(ht.get("verCostos").toString()).
              booleanValue();
      }
      if (jf.gestor.apuntar(this))
        jbInit();
      else
        setErrorInit(true);
    }
    catch (Exception e)
    {
      Logger.getLogger(clstkfeca.class.getName()).log(Level.SEVERE, null, e);
      setErrorInit(true);
    }
  }

  public clstkfeca(gnu.chu.anjelica.menu p, EntornoUsuario eu,Hashtable ht)
  {

    EU = eu;
    vl = p.getPanelPrinc();
    setTitulo("Cons/List Stock con Fechas Caducidad");
    eje = false;

    try
    {
        PARAM_VERCOSTOS=true;
      if (ht != null)
      {
        if (ht.get("verCostos") != null)
          PARAM_VERCOSTOS = Boolean.valueOf(ht.get("verCostos").toString()).
              booleanValue();
      }
      jbInit();
    }
    catch (Exception e)
    {
      Logger.getLogger(clstkfeca.class.getName()).log(Level.SEVERE, null, e);
      setErrorInit(true);
    }
  }

  private void jbInit() throws Exception
  {
    iniciarFrame();
    statusBar = new StatusBar(this);
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);

    conecta();
    initComponents();
    this.setSize(new Dimension(632, 502));
    this.setVersion("2018-03-15");
  }
    @Override
  public void iniciarVentana() throws Exception
  {
      filtroEmp=empPanel.getStringAccesos(dtStat, EU.usuario,true);
//      filtroEmp=empPanel.getStringAccesos(dtStat, "pedro",true);
      pdalmace.llenaLinkBox(alm_codiE,dtStat);
      alm_codiE.addDatos("0","*TODOS*");
      alm_codiE.setText("0");
      alm_codiE.setFormato(Types.DECIMAL,"#9");
      alm_codiE.setCeroIsNull(true);
      MantFamPro.llenaLinkBox(fam_codiE, dtStat);
      pro_codiE.iniciar(dtStat, this, vl, EU);
      Pcabe.setDefButton(Baceptar);
      dtDesp=new DatosTabla(ct);
      utdesp =new utildesp();
      kilminE.setValorDec(1);
      activarEventos();
      this.setEnabled(true);
  }
  
   private void activarEventos()
   {
       popEspere_BCancelaraddActionListener(new ActionListener()
       {
            @Override
        public void actionPerformed(ActionEvent e)
        {
            cancelarConsulta();
            popEspere_BCancelarSetEnabled(false);
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
            if ((prog=jf.gestor.getProceso(MantDesp.getNombreClase()))==null)
            return;
            MantDesp cm=(MantDesp) prog;
            if (cm.inTransation())
            {
                msgBox("Mantenimiento Despieces ocupado. No se puede realizar la busqueda");
                return;
            }
            cm.PADQuery();
            cm.setEjeNume(jt.getValString(12).substring(0,jt.getValString(12).indexOf("/")));
            cm.setDeoCodi(jt.getValString(12).substring(jt.getValString(12).indexOf("/")+1));
            cm.ej_query();
            jf.gestor.ir(cm); 
          }
      });
   }
   void cancelarConsulta()
   {
       mensaje("Espere, por favor.. CANCELADO CONSULTA");
       cancelarConsulta=true;
   }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        cTextField1 = new gnu.chu.controles.CTextField();
        Pprinc = new gnu.chu.controles.CPanel();
        Pcabe = new gnu.chu.controles.CPanel();
        Baceptar = new gnu.chu.controles.CButton(Iconos.getImageIcon("check"));
        alm_codiE = new gnu.chu.controles.CLinkBox();
        cLabel1 = new gnu.chu.controles.CLabel();
        fam_codiE = new gnu.chu.controles.CLinkBox();
        cLabel2 = new gnu.chu.controles.CLabel();
        tipoProdE = new gnu.chu.controles.CComboBox();
        cLabel3 = new gnu.chu.controles.CLabel();
        cLabel4 = new gnu.chu.controles.CLabel();
        kilminE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9.9");
        cLabel5 = new gnu.chu.controles.CLabel();
        pro_codiE = new gnu.chu.camposdb.proPanel();
        cLabel8 = new gnu.chu.controles.CLabel();
        numDiasAgrE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#9");
        opIncRes = new gnu.chu.controles.CCheckBox();
        jt = new gnu.chu.controles.Cgrid(13);
        Ppie = new gnu.chu.controles.CPanel();
        cLabel6 = new gnu.chu.controles.CLabel();
        uniTotE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----,--9");
        cLabel7 = new gnu.chu.controles.CLabel();
        kilTotE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----,--9.99");
        cLabel9 = new gnu.chu.controles.CLabel();
        impTotE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----,--9.99");

        cTextField1.setText("cTextField1");

        Pprinc.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Pprinc.setLayout(new java.awt.GridBagLayout());

        Pcabe.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.orange, null));
        Pcabe.setMaximumSize(new java.awt.Dimension(584, 75));
        Pcabe.setMinimumSize(new java.awt.Dimension(584, 75));
        Pcabe.setPreferredSize(new java.awt.Dimension(584, 75));
        Pcabe.setLayout(null);

        Baceptar.setText("Aceptar");
        Baceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BaceptarActionPerformed(evt);
            }
        });
        Pcabe.add(Baceptar);
        Baceptar.setBounds(440, 42, 130, 24);

        alm_codiE.setAncTexto(30);
        Pcabe.add(alm_codiE);
        alm_codiE.setBounds(71, 2, 200, 17);

        cLabel1.setText("Almacen");
        Pcabe.add(cLabel1);
        cLabel1.setBounds(10, 2, 48, 17);

        fam_codiE.setAncTexto(30);
        fam_codiE.setMinimumSize(new java.awt.Dimension(64, 20));
        fam_codiE.setPreferredSize(new java.awt.Dimension(80, 20));
        Pcabe.add(fam_codiE);
        fam_codiE.setBounds(71, 22, 200, 17);

        cLabel2.setText("Estado");
        Pcabe.add(cLabel2);
        cLabel2.setBounds(420, 22, 50, 17);

        tipoProdE.addItem("No Congelado", "N");
        tipoProdE.addItem("Congelado", "C");
        tipoProdE.addItem("Todos", "T");
        tipoProdE.setMinimumSize(new java.awt.Dimension(32, 20));
        Pcabe.add(tipoProdE);
        tipoProdE.setBounds(470, 22, 100, 17);

        cLabel3.setText("Producto ");
        Pcabe.add(cLabel3);
        cLabel3.setBounds(10, 40, 60, 17);

        cLabel4.setText("Kg Minimos");
        Pcabe.add(cLabel4);
        cLabel4.setBounds(280, 22, 80, 17);
        cLabel4.getAccessibleContext().setAccessibleName("Con mas de");

        Pcabe.add(kilminE);
        kilminE.setBounds(360, 22, 43, 17);

        cLabel5.setText("Familia");
        Pcabe.add(cLabel5);
        cLabel5.setBounds(10, 22, 38, 17);
        Pcabe.add(pro_codiE);
        pro_codiE.setBounds(70, 40, 350, 17);

        cLabel8.setText("Dias Agrupar");
        Pcabe.add(cLabel8);
        cLabel8.setBounds(290, 2, 80, 17);

        numDiasAgrE.setText("3");
        Pcabe.add(numDiasAgrE);
        numDiasAgrE.setBounds(370, 2, 20, 17);

        opIncRes.setText("Inc. Reservado");
        opIncRes.setToolTipText("Incluir reservas a clientes");
        Pcabe.add(opIncRes);
        opIncRes.setBounds(460, 2, 110, 18);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 0);
        Pprinc.add(Pcabe, gridBagConstraints);

        ArrayList v = new ArrayList();
        v.add("Prod"); // 0
        v.add("Nombre"); // 1 Producto
        v.add("Proveed"); //  2 Proveedor
        v.add("Lote"); // 3
        v.add("Unid."); // 4
        v.add("Kilos"); // 5
        v.add("Fec.Com"); // 6
        v.add("Fec.Cad"); // 7
        v.add("Fec.Desp"); // 8
        v.add("Fec.Cad."); // 9
        v.add("Costo"); // 10
        v.add("Importe"); //11
        v.add("N.Desp");// 12
        jt.setCabecera(v);
        jt.setAlinearColumna(new int[]{0,0,0,0,2,2,1,1,1,1,2,2,2});
        jt.setAnchoColumna(new int[]{40,150,130,70,40,60,70,70,70,70,70,90,50});
        jt.setFormatoColumna(6, "dd-MM-yy");
        jt.setFormatoColumna(7, "dd-MM-yy");
        jt.setFormatoColumna(8, "dd-MM-yy");
        jt.setFormatoColumna(9, "dd-MM-yy");
        jt.setFormatoColumna(5, "---,--9.99");
        jt.setFormatoColumna(10, "##9.99");
        jt.setFormatoColumna(11, "--,--9.99");
        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jt.setMaximumSize(new java.awt.Dimension(100, 100));
        jt.setMinimumSize(new java.awt.Dimension(100, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        Pprinc.add(jt, gridBagConstraints);

        Ppie.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        Ppie.setMaximumSize(new java.awt.Dimension(450, 24));
        Ppie.setMinimumSize(new java.awt.Dimension(450, 24));
        Ppie.setPreferredSize(new java.awt.Dimension(450, 24));
        Ppie.setLayout(null);

        cLabel6.setText("Unidades");
        Ppie.add(cLabel6);
        cLabel6.setBounds(10, 2, 60, 15);

        uniTotE.setEditable(false);
        Ppie.add(uniTotE);
        uniTotE.setBounds(70, 2, 70, 17);

        cLabel7.setText("Kilos");
        Ppie.add(cLabel7);
        cLabel7.setBounds(160, 2, 40, 15);

        kilTotE.setEditable(false);
        Ppie.add(kilTotE);
        kilTotE.setBounds(210, 2, 80, 17);

        cLabel9.setText("Importe ");
        Ppie.add(cLabel9);
        cLabel9.setBounds(300, 2, 50, 15);

        impTotE.setEditable(false);
        Ppie.add(impTotE);
        impTotE.setBounds(350, 2, 80, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 1.0;
        Pprinc.add(Ppie, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BaceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BaceptarActionPerformed
        new miThread("")
        {
            @Override
            public void run()
            {
                consultar();
            }
        };
    }
    private ArrayList addSubTotal()
    {
        ArrayList v=new ArrayList();
        v.add("");
        v.add("Total");
        v.add("");
        v.add("");
        v.add(uniAct);
        v.add(kilAct);
        v.add("");
        v.add("");
        v.add("");
        v.add("");
        v.add(PARAM_VERCOSTOS?impAct/kilAct:0);
        v.add(PARAM_VERCOSTOS?impAct:0);
        v.add("");
        return v;
    }
    
    private void consultaAgruFec() throws SQLException
    {
        actualizaMsg("Buscando datos... preparando datos temporales ",false);
        if (!swPrimeraVez)
            dtStat.executeUpdate("drop table clstkfecca");
        swPrimeraVez=false;
        String s="CREATE  TEMP TABLE clstkfecca ("+
            " pro_codi int,"+
            " prv_codi int,"+
            " stp_unact int,"+
            " stp_kilact float,"+
            " costo float,"+
            " feccom date,"+
            " feccad date,"+
            " fecdes date,"+
            " fecade date)";
        dtStat.executeUpdate(s);
        int prvCodi;
        int proCodi=dtCon1.getInt("pro_codi");
     
        do
        {
            if (cancelarConsulta)
            {
                verDatos(true);
                return;
            }
            if (proCodi!=dtCon1.getInt("pro_codi"))
            {
                actualizaMsg("Buscando datos... \nPreparando datos temporales.Producto: "+
                    proCodi,false);
                proCodi=dtCon1.getInt("pro_codi");
            }
            if (!utdesp.busDatInd(dtCon1.getString("pro_serie"),dtCon1.getInt("pro_codi"),
                        EU.em_cod, dtCon1.getInt("eje_nume"), dtCon1.getInt("pro_nupar"),
                        0,dtDesp,dtStat, EU))
                        prvCodi=0;
                else
                        prvCodi=utdesp.getPrvCompra();
            dtStat.addNew("clstkfecca");
            dtStat.setDato("pro_codi",dtCon1.getInt("pro_codi"));
            dtStat.setDato("prv_codi",prvCodi);
            dtStat.setDato("stp_unact",dtCon1.getInt("stp_unact"));
            dtStat.setDato("stp_kilact",dtCon1.getDouble("stp_kilact"));
            dtStat.setDato("costo",utdesp.getPrecioCompra()==-1?0:utdesp.getPrecioCompra()*dtCon1.getDouble("stp_kilact"));
            dtStat.setDato("feccom",redondeaFecha(utdesp.getFechaCompra()));
            dtStat.setDato("feccad",redondeaFecha(utdesp.getFechaCaducidadCompra()));
            dtStat.setDato("fecdes",redondeaFecha(utdesp.getFechaDespiece()));
            dtStat.setDato("fecade",redondeaFecha(utdesp.getFechaCadDesp()));
            
            dtStat.update();
        } while (dtCon1.next());
        s="select pro_codi,prv_codi,sum(stp_kilact) as stp_kilact, " +
            " sum(stp_unact) as stp_unact,sum(costo) as costo,feccom,feccad,fecdes,fecade "+ 
            " from clstkfecca group by pro_codi,prv_codi,feccom,feccad,fecdes,fecade"+
            " order by pro_codi,feccom,prv_codi";
        dtCon1.select(s);
        verDatos(true);
    }
    
    private Date redondeaFecha(Date fecha)
    {
        if (fecha==null)
            return null;
         long nDias=fecha.getTime()/86400000;
         nDias=((int)nDias/nDiasAgr)*nDiasAgr;
         return new Date(nDias*86400000);
    }
    private void consultar()
    {
        cancelarConsulta=false;
        jt.removeAllDatos();
        msgEspere("A esperar.. estoy buscando datos");
        popEspere_BCancelarSetEnabled(true);
        activar(false);
        nDiasAgr=numDiasAgrE.getValorInt();
       
        String s = "select r.pro_codi,r.eje_nume,r.pro_serie,r.pro_nupar,sum(stp_kilact) as stp_kilact, " +
                " sum(stp_unact) as stp_unact "+
                " from v_stkpart as r,v_articulo as a where  a.pro_codi = r.pro_codi "+
                (fam_codiE.isNull() ? "" : " and a.fam_codi = " + fam_codiE.getValorInt()) +
                (alm_codiE.isNull() ? "" : " and r.alm_codi =" + alm_codiE.getValorInt()) +
                (tipoProdE.getValor().equals("T") ? "" : " and a.pro_artcon " +
                (tipoProdE.getValor().equals("N") ? "=0" : "!=0")) +
                (pro_codiE.isNull()?"":" and a.pro_codi = "+pro_codiE.getValorInt())+
                (filtroEmp==null?"":" and r.emp_codi in ("+filtroEmp+")")+
                (opIncRes.isSelected()?"":" and r.stk_block = 0 ")+
                " and stp_tiplot != 'S' "+ // Quito los registros de Acumulados
                " and a.pro_tiplot = 'V' "+ // Solo Prod. Vendibles
                " group by r.pro_codi,r.eje_nume,r.pro_serie,r.pro_nupar " +
                "having sum(stp_kilact) > "+(kilminE.getValorDec() == 0 ? "1" :  kilminE.getValorDec()) +
                " order by pro_codi,eje_nume,pro_serie, pro_nupar ";
        try 
        {
      //      System.out.println("s:" + s);
           
            if (!dtCon1.select(s)) {
                mensajeErr("No encontrados registros para estas condiciones");
                activar(true);
                resetMsgEspere();
                return;
            }
             if (nDiasAgr>0)
            {
                consultaAgruFec();
                return;
            }
            verDatos(false);
        } catch (SQLException k) {
             Error("Error al buscar datos", k);
        }
    }
    
    private void verDatos(boolean agruFec) throws SQLException
    {
            String proNomb, prvNomb;
            int proCodi=dtCon1.getInt("pro_codi");
            proNomb=gnu.chu.anjelica.pad.MantArticulos.getNombProd(proCodi,dtStat);
            int nProd=0;
            impAct=0;
            impTot=0;
            uniAct=0;
            kilAct=0;
            kilTot=0;
            uniTot=0;
            double precCompra;
            
            ArrayList<ArrayList> datos=new ArrayList();
            do  
            {
                if (cancelarConsulta)
                    break;
                
                if (proCodi!=dtCon1.getInt("pro_codi") )
                {
                    actualizaMsg("Cargando datos de consulta. Producto: "+proCodi,false);
                    if (nProd>1)
                        datos.add(addSubTotal());
                    nProd=0;
                    proCodi=dtCon1.getInt("pro_codi");
                    proNomb=gnu.chu.anjelica.pad.MantArticulos.getNombProd(proCodi,dtStat);
                    if (proNomb==null)
                        proNomb="*PRODUCTO NO ENCONTRADO*";
                    uniAct=0;
                    kilAct=0; 
                    impAct=0;
                }
                if (!agruFec)
                {
                   if (!utdesp.busDatInd(dtCon1.getString("pro_serie"),proCodi,
                        EU.em_cod, dtCon1.getInt("eje_nume"), dtCon1.getInt("pro_nupar"),
                        0,dtDesp,dtStat, EU))
                   {
                        prvNomb="*DESCONOCIDO*";
                        precCompra=0;
                   }
                   else
                   {
                       precCompra=utdesp.getPrecioCompra()==-1?0:utdesp.getPrecioCompra() ;
                       prvNomb=gnu.chu.anjelica.pad.pdprove.getNombPrv(utdesp.getPrvCompra(),dtStat);
                   }
                }
                else
                {
                    precCompra=dtCon1.getDouble("costo")/dtCon1.getDouble("stp_kilact");
                    prvNomb=dtCon1.getInt("prv_codi")==0?"*DESCONOCIDO*":gnu.chu.anjelica.pad.pdprove.getNombPrv(
                        dtCon1.getInt("prv_codi"),dtStat);
                }
                ArrayList v=new ArrayList();
                if (nProd==0)
                {
                    v.add(proCodi);
                    v.add(proNomb); //Producto
                }
                else
                {
                    v.add("");
                    v.add(""); //Producto
                }
                nProd++;
                v.add(prvNomb); // Proveedor
                v.add(agruFec?"":dtCon1.getString("eje_nume")+dtCon1.getString("pro_serie")+dtCon1.getString("pro_nupar"));
                
                v.add(dtCon1.getInt("stp_unact"));
                v.add(dtCon1.getDouble("stp_kilact"));
                v.add(agruFec?dtCon1.getDate("feccom"):utdesp.getFechaCompra());
                v.add(agruFec?dtCon1.getDate("feccad"):utdesp.getFechaCaducidadCompra());
                v.add(agruFec?dtCon1.getDate("fecdes"):utdesp.getFechaDespiece());
                v.add(agruFec?dtCon1.getDate("fecade"):utdesp.getFechaCadDesp());
                v.add(PARAM_VERCOSTOS?precCompra:0);
                v.add(PARAM_VERCOSTOS?precCompra*dtCon1.getDouble("stp_kilact"):0);
                v.add(agruFec?0:utdesp.getEjercicioDespiece()+"/"+utdesp.getNumeroDespiece());
                uniAct+=dtCon1.getInt("stp_unact");
                kilAct+=dtCon1.getDouble("stp_kilact");
                uniTot+=dtCon1.getInt("stp_unact");
                kilTot+=dtCon1.getDouble("stp_kilact");
                impAct+=precCompra*dtCon1.getDouble("stp_kilact");
                impTot+=precCompra*dtCon1.getDouble("stp_kilact");
                datos.add(v);
            } while (dtCon1.next());
            jt.addLineas(datos);
            dtStat.commit();
            if (nProd>1)
                 datos.add(addSubTotal());
            kilTotE.setValorDec(kilTot);
            uniTotE.setValorInt(uniTot);
            if (PARAM_VERCOSTOS)
                impTotE.setValorDec(impTot);
            activar(true);
            resetMsgEspere();
            if (cancelarConsulta) {
                mensajeErr("Consulta Cancelada");
            } else {
                mensajeErr("Consulta realizada ...");
            }
            activar(true);
            mensaje("");
       
    }//GEN-LAST:event_BaceptarActionPerformed
    private void activar(boolean activ)
    {
        Pcabe.setEnabled(activ);
        statusBar.setEnabled(activ);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Ppie;
    private gnu.chu.controles.CPanel Pprinc;
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
    private gnu.chu.controles.CTextField cTextField1;
    private gnu.chu.controles.CLinkBox fam_codiE;
    private gnu.chu.controles.CTextField impTotE;
    private gnu.chu.controles.Cgrid jt;
    private gnu.chu.controles.CTextField kilTotE;
    private gnu.chu.controles.CTextField kilminE;
    private gnu.chu.controles.CTextField numDiasAgrE;
    private gnu.chu.controles.CCheckBox opIncRes;
    private gnu.chu.camposdb.proPanel pro_codiE;
    private gnu.chu.controles.CComboBox tipoProdE;
    private gnu.chu.controles.CTextField uniTotE;
    // End of variables declaration//GEN-END:variables

}
