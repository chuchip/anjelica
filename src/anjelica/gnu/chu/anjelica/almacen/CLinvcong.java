
package gnu.chu.anjelica.almacen;
import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.despiece.MantDesp;
import gnu.chu.anjelica.despiece.MantTipDesp;
import gnu.chu.anjelica.despiece.utildesp;
import gnu.chu.anjelica.pad.MantFamPro;
import gnu.chu.camposdb.empPanel;
import gnu.chu.controles.StatusBar;
import gnu.chu.controles.miCellRender;
import gnu.chu.interfaces.VirtualGrid;
import gnu.chu.interfaces.ejecutable;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import java.sql.Types;
import java.util.*;
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
public class CLinvcong extends ventana
{
  private boolean swPrimeraVez=true;
  private int proCodi;
  private String proNomb;
  private String filtroEmpr;
  private boolean cancelarConsulta=false;
  utildesp utdesp;
  DatosTabla dtDesp;
  DatosTabla dtAdd;
  private  int uniAct,uniTot;
  private double kilAct,kilTot,impAct,impTot;
  private int nDiasAgr;
  boolean PARAM_VERCOSTOS=false;
  
  public CLinvcong(EntornoUsuario eu, Principal p)
  {
      this(eu,p, null);
  }
  public CLinvcong(EntornoUsuario eu, Principal p,Hashtable ht)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

     setTitulo("Cons/List Stock Congelado");

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
       Logger.getLogger(CLinvcong.class.getName()).log(Level.SEVERE, null, e);
      setErrorInit(true);
    }
  }

  public CLinvcong(gnu.chu.anjelica.menu p, EntornoUsuario eu,Hashtable ht)
  {

    EU = eu;
    vl = p.getPanelPrinc();
    setTitulo("Cons/List Stock Congelado");
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
      Logger.getLogger(CLinvcong.class.getName()).log(Level.SEVERE, null, e);
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
    this.setVersion("2013-08-28");
  }
    @Override
  public void iniciarVentana() throws Exception
  {
      filtroEmpr=empPanel.getStringAccesos(dtStat, EU.usuario,true);
      pdalmace.llenaLinkBox(alm_codiE,dtStat);
      alm_codiE.addDatos("0","*TODOS*");
      alm_codiE.setText("0");
      alm_codiE.setFormato(Types.DECIMAL,"#9");
      alm_codiE.setCeroIsNull(true);
      Pcabe.setDefButton(Baceptar);
      pro_codiE.iniciar(dtStat, this, vl, EU);
      dtDesp=new DatosTabla(ct);
      dtAdd=new DatosTabla(ct);
      utdesp =new utildesp();
      utdesp.setTidCodi(MantTipDesp.CONGELADO_DESPIECE);
      kilminE.setValorDec(1);
      GregorianCalendar gcal=new GregorianCalendar(Locale.getDefault());
      gcal.setTimeInMillis(System.currentTimeMillis());
      semanaE.setValorInt(gcal.get(Calendar.WEEK_OF_YEAR));
      //ponFechas(semanaE.getValorInt());
      /**
       * Establece el color de la celda con familia.
       */
      colorGrid vg=new colorGrid();
      for (int n=0;n<jt.getColumnCount();n++)
      {
         miCellRender mc= jt.getRenderer(n);
         if (mc==null)
             continue;
         mc.setVirtualGrid(vg);
         mc.setErrBackColor(Color.CYAN);
         mc.setErrForeColor(Color.BLACK);
      }
      activarEventos();
      this.setEnabled(true);
      semanaE.resetCambio();
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
      semanaE.addFocusListener(new FocusAdapter() {
            @Override
           public void focusLost(FocusEvent e) {
                if (!semanaE.hasCambio())
                    return;
                try {
                    semanaE.resetCambio();
                    ponFechas(semanaE.getValorInt());
                 }catch (Exception k)
                {
                    
                }
//                feciniE.setDate( );
           }
      } );
   }
   void ponFechas(int semana) throws Exception
   {
        GregorianCalendar gcal=new GregorianCalendar(Locale.getDefault());
        Date fecini=Formatear.getDate("01-01-"+Formatear.getFechaAct("yyyy"),"dd-MM-yyyy");
        gcal.setTime(fecini);

        for (int n=0;n<7;n++)
        {
            if (gcal.get(Calendar.DAY_OF_WEEK)==Calendar.MONDAY)
                break;
            gcal.add(GregorianCalendar.DAY_OF_MONTH, 1);
        }
        fecini=gcal.getTime();
        feciniE.setDate(Formatear.sumaDiasDate(fecini, (semana-1)*7));
        fecfinE.setDate(Formatear.sumaDiasDate(fecini, ((semana-1)*7)+6));
   }
   void cancelarConsulta()
   {
       mensaje("Espere, por favor.. CANCELADO CONSULTA");
       cancelarConsulta=true;
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

        cTextField1 = new gnu.chu.controles.CTextField();
        Pprinc = new gnu.chu.controles.CPanel();
        Pcabe = new gnu.chu.controles.CPanel();
        Baceptar = new gnu.chu.controles.CButton(Iconos.getImageIcon("check"));
        alm_codiE = new gnu.chu.controles.CLinkBox();
        cLabel1 = new gnu.chu.controles.CLabel();
        cLabel3 = new gnu.chu.controles.CLabel();
        cLabel4 = new gnu.chu.controles.CLabel();
        kilminE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9.9");
        pro_codiE = new gnu.chu.camposdb.proPanel();
        cLabel8 = new gnu.chu.controles.CLabel();
        numDiasAgrE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#9");
        cPanel1 = new gnu.chu.controles.CPanel();
        cLabel5 = new gnu.chu.controles.CLabel();
        semanaE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#9");
        cLabel2 = new gnu.chu.controles.CLabel();
        feciniE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel10 = new gnu.chu.controles.CLabel();
        fecfinE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel11 = new gnu.chu.controles.CLabel();
        ordenE = new gnu.chu.controles.CComboBox();
        opSoloTot = new gnu.chu.controles.CCheckBox();
        jt = new gnu.chu.controles.Cgrid(15);
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
        Pcabe.setMaximumSize(new java.awt.Dimension(584, 85));
        Pcabe.setMinimumSize(new java.awt.Dimension(584, 85));
        Pcabe.setPreferredSize(new java.awt.Dimension(584, 85));
        Pcabe.setLayout(null);

        Baceptar.setText("Aceptar");
        Baceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BaceptarActionPerformed(evt);
            }
        });
        Pcabe.add(Baceptar);
        Baceptar.setBounds(450, 42, 120, 30);

        alm_codiE.setAncTexto(30);
        Pcabe.add(alm_codiE);
        alm_codiE.setBounds(71, 2, 200, 17);

        cLabel1.setText("Almacen");
        Pcabe.add(cLabel1);
        cLabel1.setBounds(10, 2, 48, 17);

        cLabel3.setText("Producto ");
        Pcabe.add(cLabel3);
        cLabel3.setBounds(10, 22, 60, 17);

        cLabel4.setText("Kilos Minimos");
        Pcabe.add(cLabel4);
        cLabel4.setBounds(440, 2, 90, 17);
        cLabel4.getAccessibleContext().setAccessibleName("Con mas de");

        Pcabe.add(kilminE);
        kilminE.setBounds(530, 2, 43, 17);
        Pcabe.add(pro_codiE);
        pro_codiE.setBounds(70, 22, 350, 17);

        cLabel8.setText("Dias Agrupar");
        Pcabe.add(cLabel8);
        cLabel8.setBounds(290, 2, 80, 17);

        numDiasAgrE.setText("3");
        Pcabe.add(numDiasAgrE);
        numDiasAgrE.setBounds(370, 2, 20, 17);

        cPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Limitar fechas"));
        cPanel1.setLayout(null);

        cLabel5.setText("Semana ");
        cPanel1.add(cLabel5);
        cLabel5.setBounds(10, 16, 50, 17);
        cPanel1.add(semanaE);
        semanaE.setBounds(60, 16, 20, 17);

        cLabel2.setText("De ");
        cPanel1.add(cLabel2);
        cLabel2.setBounds(90, 16, 20, 17);
        cPanel1.add(feciniE);
        feciniE.setBounds(110, 16, 70, 17);

        cLabel10.setText("A");
        cPanel1.add(cLabel10);
        cLabel10.setBounds(190, 16, 20, 17);
        cPanel1.add(fecfinE);
        fecfinE.setBounds(210, 16, 70, 17);

        Pcabe.add(cPanel1);
        cPanel1.setBounds(10, 40, 300, 38);

        cLabel11.setText("Orden");
        Pcabe.add(cLabel11);
        cLabel11.setBounds(310, 50, 34, 15);

        ordenE.addItem("Familia","F");
        ordenE.addItem("Codigo", "C");
        Pcabe.add(ordenE);
        ordenE.setBounds(350, 50, 90, 20);

        opSoloTot.setText("Solo totales");
        Pcabe.add(opSoloTot);
        opSoloTot.setBounds(450, 22, 120, 18);

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
        v.add("D.Cad"); // 8
        v.add("Fec.Cong"); // 9
        v.add("F.Cad.C."); // 10
        v.add("D.Cong"); // 11
        v.add("Costo"); // 12
        v.add("Importe"); //13
        v.add("N.Desp");// 14
        jt.setCabecera(v);
        jt.setAlinearColumna(new int[]{0,0,0,0,2,2,1,1,2,1,1,2,2,2,2});
        jt.setAnchoColumna(new int[]{40,150,130,70,40,60,70,70,50,70,70,50,70,90,50});
        jt.setFormatoColumna(6, "dd-MM-yy");
        jt.setFormatoColumna(7, "dd-MM-yy");
        jt.setFormatoColumna(9, "dd-MM-yy");
        jt.setFormatoColumna(10, "dd-MM-yy");
        jt.setFormatoColumna(8, "---9");
        jt.setFormatoColumna(11, "---9");
        jt.setFormatoColumna(5, "---,--9.99");
        jt.setFormatoColumna(12, "##9.99");
        jt.setFormatoColumna(13, "--,--9.99");
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
        if (opSoloTot.isSelected())
        {
            v.add(proCodi);
            v.add(proNomb);
        }
        else
        {
            v.add("");
            v.add("Total");
        }
        v.add("");
        v.add("");
        v.add(uniAct);
        v.add(kilAct);
        v.add("");
        v.add("");
        v.add("");
        v.add("");
        v.add("");
        v.add("");
        v.add(PARAM_VERCOSTOS?impAct/kilAct:0);
        v.add(PARAM_VERCOSTOS?impAct:0);
        v.add("");
        return v;
    }
    
    private void consultaAgruFec() throws Exception
    {
        actualizaMsg("Buscando datos... preparando datos temporales ",false);
        dtAdd.setCerrarCursor(false);
        if (!swPrimeraVez)
            dtAdd.executeUpdate("drop table clstkfecca");
 
        swPrimeraVez=false;
        String s="CREATE  TEMP TABLE clstkfecca ("+
            " pro_codi int,"+
            " fam_codi int,"+
            " prv_codi int,"+
            " stp_unact int,"+
            " stp_kilact float,"+
            " costo float,"+
            " feccom date,"+
            " feccad date,"+
            " fecdes date,"+
            " fecade date)";
        dtAdd.executeUpdate(s);
       
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
            dtAdd.addNew("clstkfecca");
            dtAdd.setDato("pro_codi",dtCon1.getInt("pro_codi"));
            dtAdd.setDato("fam_codi",dtCon1.getInt("fam_codi"));
            dtAdd.setDato("prv_codi",prvCodi);
            dtAdd.setDato("stp_unact",dtCon1.getInt("stp_unact"));
            dtAdd.setDato("stp_kilact",dtCon1.getDouble("stp_kilact"));
            dtAdd.setDato("costo",utdesp.getPrecioCompra()==-1?0:utdesp.getPrecioCompra()*dtCon1.getDouble("stp_kilact"));
            dtAdd.setDato("feccom",redondeaFecha(utdesp.getFecCompra()));
            dtAdd.setDato("feccad",redondeaFecha(utdesp.getFecCadPrv()));
            dtAdd.setDato("fecdes",redondeaFecha(utdesp.getFecDesp()));
            dtAdd.setDato("fecade",redondeaFecha(utdesp.getFechaCadDesp()));
            
            dtAdd.update();
        } while (dtCon1.next());
          actualizaMsg("Buscando datos... \nPreparando consulta sobre datos temporales",false);
        s="select pro_codi,fam_codi,prv_codi,sum(stp_kilact) as stp_kilact, " +
            " sum(stp_unact) as stp_unact,sum(costo) as costo,feccom,feccad,fecdes,fecade "+ 
            " from clstkfecca group by fam_codi,pro_codi,prv_codi,feccom,feccad,fecdes,fecade"+
            " order by "+
           (ordenE.getValor().equals("F")?"fam_codi,":"")+
            " pro_codi,feccom,prv_codi";
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
        semanaE.resetCambio();
        cancelarConsulta=false;
        jt.removeAllDatos();
        msgEspere("A esperar.. estoy buscando datos");
        popEspere_BCancelarSetEnabled(true);
        activar(false);
        nDiasAgr=numDiasAgrE.getValorInt();
       
        String s = "select r.pro_codi,fam_codi,r.eje_nume,r.pro_serie,r.pro_nupar,"
                + " sum(stp_kilact) as stp_kilact, " +
                " sum(stp_unact) as stp_unact "+
                " from v_stkpart as r,v_articulo as a where  a.pro_artcon !=0 "+
                (alm_codiE.isNull() ? "" : " and r.alm_codi =" + alm_codiE.getValorInt()) +
                (pro_codiE.isNull()?"":" and a.pro_codi = "+pro_codiE.getValorInt())+
                (filtroEmpr==null?"":" and r.emp_codi in ("+filtroEmpr+")")+
                " and stp_tiplot != 'S' "+ // Quito los registros de Acumulados
                " and a.pro_codi = r.pro_codi " +
                " and a.pro_tiplot = 'V' "+ // Solo Prod. Vendibles
                " group by a.fam_codi,r.pro_codi,r.eje_nume,r.pro_serie,r.pro_nupar " +
                "having sum(stp_kilact) > "+(kilminE.getValorDec() == 0 ? "1" :  kilminE.getValorDec()) +
                " order by "+
                (ordenE.getValor().equals("F")?"fam_codi,":"")+
                " pro_codi,eje_nume,pro_serie, pro_nupar ";
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
        } catch (Exception k) {
             Error("Error al buscar datos", k);
        }
    }
    
    private void verDatos(boolean agruFec) throws Exception
    {
            String prvNomb;
            proCodi=dtCon1.getInt("pro_codi");
            int famCodi=0;
            proNomb=gnu.chu.anjelica.pad.MantArticulos.getNombProd(proCodi,dtStat);
            int nProd=0;
            impAct=0;
            impTot=0;
            uniAct=0;
            kilAct=0;
            kilTot=0;
            uniTot=0;
            Date fecDesp,fecCadCong;
            double precCompra;
            long diasCad;
            long diasCong;
            ArrayList<ArrayList> datos=new ArrayList();
            char orden=ordenE.getValor().charAt(0);
            do  
            {
                if (cancelarConsulta)
                    break;
              
                if (proCodi!=dtCon1.getInt("pro_codi") )
                {
                    actualizaMsg("Cargando datos de consulta. Producto: "+proCodi,false);
                    if (nProd>1 || opSoloTot.isSelected())
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
                if (orden=='F')
                {
                    if (famCodi!=dtCon1.getInt("fam_codi"))
                    { 
                       famCodi=dtCon1.getInt("fam_codi"); 
                       ArrayList al=new ArrayList();
                       al.add("");
                       al.add("Fam: "+MantFamPro.getNombreFam(famCodi,dtStat));
                       for (int n=0;n<13;n++)
                           al.add("");
                       datos.add(al);
                    }
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
               
                fecDesp=agruFec?dtCon1.getDate("fecdes"):utdesp.getFecDesp();
                 // Si el producto se compro ya congelado (es nulo).
                 // Hago equivalente la fecha Desp. a la fecha de cong.
                if (fecDesp==null)
                {
                    fecDesp=agruFec?dtCon1.getDate("feccom"):utdesp.getFecCompra();
                    diasCad=9999;
                }
                else
                    diasCad=agruFec?Formatear.comparaFechas(dtCon1.getDate("feccad"),fecDesp):
                        Formatear.comparaFechas(utdesp.getFecCadPrv(),fecDesp);
               
                fecCadCong=agruFec?dtCon1.getDate("fecade"):utdesp.getFechaCadDesp();
                if (fecCadCong==null)
                   fecCadCong=agruFec?dtCon1.getDate("feccad"):utdesp.getFecCadPrv();
                
                diasCong=agruFec?Formatear.comparaFechas(fecCadCong, fecDesp):
                    Formatear.comparaFechas(fecCadCong,fecDesp);
                if (diasCong<32)
                    fecCadCong=Formatear.sumaDiasDate(fecCadCong, 23*30); // Le sumo 23 Meses.
                    diasCong=Formatear.comparaFechas(fecCadCong, Formatear.getDateAct());
                if (! feciniE.isNull())
                {
                    if (fecDesp==null)
                        continue;
                    if (Formatear.comparaFechas(fecDesp,feciniE.getDate())<0)
                        continue;
                }
                
                if (!fecfinE.isNull())
                {
                    if (fecDesp==null)
                        continue;
                    if (Formatear.comparaFechas(fecfinE.getDate(), fecDesp)<0)
                        continue;
                }
                if (! opSoloTot.isSelected())
                {
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

                    v.add(prvNomb); // Proveedor
                    v.add(agruFec?"":dtCon1.getString("eje_nume")+dtCon1.getString("pro_serie")+dtCon1.getString("pro_nupar"));

                    v.add(dtCon1.getInt("stp_unact"));
                    v.add(dtCon1.getDouble("stp_kilact"));
                    v.add(agruFec?dtCon1.getDate("feccom"):utdesp.getFecCompra());
                    v.add(agruFec?dtCon1.getDate("feccad"):utdesp.getFecCadPrv());
                    v.add(diasCad==9999?"":diasCad);
                    v.add(fecDesp);
                    v.add(fecCadCong);
                    v.add(fecCadCong==null?"":diasCong);
                    v.add(PARAM_VERCOSTOS?precCompra:0);
                    v.add(PARAM_VERCOSTOS?precCompra*dtCon1.getDouble("stp_kilact"):0);
                    v.add(agruFec?0:utdesp.getEjeDesp()+"/"+utdesp.getDeoCodi());
                    datos.add(v);
                }
                nProd++;
                uniAct+=dtCon1.getInt("stp_unact");
                kilAct+=dtCon1.getDouble("stp_kilact");
                uniTot+=dtCon1.getInt("stp_unact");
                kilTot+=dtCon1.getDouble("stp_kilact");
                impAct+=precCompra*dtCon1.getDouble("stp_kilact");
                impTot+=precCompra*dtCon1.getDouble("stp_kilact");
            } while (dtCon1.next());
            dtStat.commit();
            if (nProd>1 || opSoloTot.isSelected())
                 datos.add(addSubTotal());
            jt.addLineas(datos);
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
    private gnu.chu.controles.CLabel cLabel10;
    private gnu.chu.controles.CLabel cLabel11;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.controles.CLabel cLabel9;
    private gnu.chu.controles.CPanel cPanel1;
    private gnu.chu.controles.CTextField cTextField1;
    private gnu.chu.controles.CTextField fecfinE;
    private gnu.chu.controles.CTextField feciniE;
    private gnu.chu.controles.CTextField impTotE;
    private gnu.chu.controles.Cgrid jt;
    private gnu.chu.controles.CTextField kilTotE;
    private gnu.chu.controles.CTextField kilminE;
    private gnu.chu.controles.CTextField numDiasAgrE;
    private gnu.chu.controles.CCheckBox opSoloTot;
    private gnu.chu.controles.CComboBox ordenE;
    private gnu.chu.camposdb.proPanel pro_codiE;
    private gnu.chu.controles.CTextField semanaE;
    private gnu.chu.controles.CTextField uniTotE;
    // End of variables declaration//GEN-END:variables

}
class colorGrid implements VirtualGrid
{
 public boolean getColorGrid(int row, int col, Object valor, boolean selecionado, String nombreGrid)
 {
     if (col==1 && ((String) valor).startsWith("Fam:"))
         return true;
     return false;
 }
}
