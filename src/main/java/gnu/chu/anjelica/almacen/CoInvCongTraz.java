package gnu.chu.anjelica.almacen;

import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.compras.MantAlbCom;
import gnu.chu.anjelica.compras.MantAlbComCarne;
import gnu.chu.anjelica.despiece.MantDesp;
import gnu.chu.anjelica.despiece.MantTipDesp;
import gnu.chu.anjelica.despiece.utildesp;
import gnu.chu.anjelica.pad.MantFamPro;
import gnu.chu.camposdb.empPanel;
import gnu.chu.controles.StatusBar;
import gnu.chu.controles.miCellRender;
import gnu.chu.interfaces.ejecutable;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *   Consulta Inventario Congelado con datos Trazabilidad.
 *   Saca los datos de la tabla de stock-partidas (v_stkpart)
 *
 *  <p>  Copyright: Copyright (c) 2005-2020
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
public class CoInvCongTraz extends ventana
{
  String feulinv;
  PreparedStatement ps,psStk;
  ResultSet rs;
  ArrayList<DatIndiv>  listIndiv=new ArrayList();
  String condAlmacen;

  PreparedStatement psAdd,psSel;
  ResultSet rsSel;
  
  private boolean swPrimeraVez=true;
  

  private boolean cancelarConsulta=false;
  utildesp utdesp;
  DatosTabla dtProd;
  DatosTabla dtDesp;
  
  private  int uniAct,uniTot;
  private double kilAct,kilTot;
 
 
  private final int JT_PROCODI=0;
  private final int JT_LOTE=3;
  public CoInvCongTraz(EntornoUsuario eu, Principal p)
  {
      this(eu,p, null);
  }
  public CoInvCongTraz(EntornoUsuario eu, Principal p,Hashtable ht)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

     setTitulo("Cons. Stock Congelado con Trazabilidad");

    try
    {
     
      if (jf.gestor.apuntar(this))
        jbInit();
      else
        setErrorInit(true);
    }
    catch (Exception e)
    {      
      setErrorInit(true);
    }
  }

  public CoInvCongTraz(gnu.chu.anjelica.menu p, EntornoUsuario eu,Hashtable ht)
  {

    EU = eu;
    vl = p.getPanelPrinc();
         setTitulo("Cons. Stock Congelado con Trazabilidad");
    eje = false;

    try
    {    
      jbInit();
    }
    catch (Exception e)
    {      
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
    this.setSize(new Dimension(712, 502));
    this.setVersion("2020-05-13");
  }
    @Override
  public void iniciarVentana() throws Exception
  {      
      pdalmace.llenaLinkBox(alm_codiniE,dtStat,'*');
      alm_codiniE.addDatos("0","*TODOS*");
      alm_codiniE.setText("0");
      alm_codiniE.setFormato(Types.DECIMAL,"#9");
      alm_codiniE.setCeroIsNull(true);
      pdalmace.llenaLinkBox(alm_codfinE,dtStat,'*');
      alm_codfinE.addDatos("0","*TODOS*");
      alm_codfinE.setText("0");
      alm_codfinE.setFormato(Types.DECIMAL,"#9");
      alm_codfinE.setCeroIsNull(true);
      Pcabe.setDefButton(Baceptar);
      pro_codiniE.iniciar(dtStat, this, vl, EU);
      pro_codfinE.iniciar(dtStat, this, vl, EU);
      acp_painacE.iniciar(dtStat, this, vl, EU);
      acp_paisacE.iniciar(dtStat, this, vl, EU);
      acp_engpaiE.iniciar(dtStat, this, vl, EU);
      acp_paisdeE.iniciar(dtStat, this, vl, EU);
      dtProd=new DatosTabla(ct);
      dtDesp=new DatosTabla(ct);
      dtAdd=new DatosTabla(ct);
      utdesp =new utildesp();
      utdesp.setTidCodi(MantTipDesp.CONGELADO_DESPIECE);

      activarEventos();      
      this.setEnabled(true);   
      fecsalE.setDate(Formatear.getDateAct());
      fecsalE.requestFocus();
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
      
      
   
   }
   void verMvtos()
   {
         if (jf==null)
                return;
         ejecutable prog;
            if ((prog = jf.gestor.getProceso(Comvalm.getNombreClase())) == null)
                return;
            gnu.chu.anjelica.almacen.Comvalm cm = (gnu.chu.anjelica.almacen.Comvalm) prog;
            for (int n=jt.getSelectedRow();n>=0;n--)
             {
                 if (jt.getValorInt(n,JT_PROCODI)!=0)
                 {
                     cm.setProCodi(jt.getValorInt(n,JT_PROCODI));
                     break;
                 }
             }
            String[] valores= jt.getValString(JT_LOTE).split("-");
            cm.setEjercicio(Integer.parseInt(valores[0]));
            cm.setSerie(valores[1]);
            cm.setLote(Integer.parseInt(valores[2]));
            cm.setIndividuo(Integer.parseInt(valores[3]));
                        
            cm.ejecutaConsulta();
            jf.gestor.ir(cm);  
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

        Pprinc = new gnu.chu.controles.CPanel();
        Pcabe = new gnu.chu.controles.CPanel();
        Baceptar = new gnu.chu.controles.CButton(Iconos.getImageIcon("check"));
        alm_codiniE = new gnu.chu.controles.CLinkBox();
        cLabel1 = new gnu.chu.controles.CLabel();
        cLabel3 = new gnu.chu.controles.CLabel();
        cLabel4 = new gnu.chu.controles.CLabel();
        pro_codiniE = new gnu.chu.camposdb.proPanel();
        cLabel11 = new gnu.chu.controles.CLabel();
        opIncRes = new gnu.chu.controles.CCheckBox();
        diasProdInfE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#9");
        pro_codfinE = new gnu.chu.camposdb.proPanel();
        cLabel8 = new gnu.chu.controles.CLabel();
        cLabel12 = new gnu.chu.controles.CLabel();
        alm_codfinE = new gnu.chu.controles.CLinkBox();
        opDatosCompra = new gnu.chu.controles.CCheckBox();
        cLabel13 = new gnu.chu.controles.CLabel();
        diasProdSupE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#9");
        diasParaCadE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9");
        cLabel2 = new gnu.chu.controles.CLabel();
        acp_painacE = new gnu.chu.camposdb.PaiPanel();
        cLabel10 = new gnu.chu.controles.CLabel();
        acp_engpaiE = new gnu.chu.camposdb.PaiPanel();
        cLabel5 = new gnu.chu.controles.CLabel();
        acp_paisacE = new gnu.chu.camposdb.PaiPanel();
        cLabel15 = new gnu.chu.controles.CLabel();
        acp_matadE = new gnu.chu.controles.CTextField(Types.CHAR,"X",15);
        cLabel16 = new gnu.chu.controles.CLabel();
        acp_paisdeE = new gnu.chu.camposdb.PaiPanel();
        acp_saldesE = new gnu.chu.controles.CTextField(Types.CHAR,"X",15);
        cLabel18 = new gnu.chu.controles.CLabel();
        fecsalE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        jt = new gnu.chu.controles.Cgrid(19);
        Ppie = new gnu.chu.controles.CPanel();
        cLabel6 = new gnu.chu.controles.CLabel();
        uniTotE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----,--9");
        cLabel7 = new gnu.chu.controles.CLabel();
        kilTotE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----,--9.99");

        Pprinc.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Pprinc.setLayout(new java.awt.GridBagLayout());

        Pcabe.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.orange, null));
        Pcabe.setMaximumSize(new java.awt.Dimension(704, 130));
        Pcabe.setMinimumSize(new java.awt.Dimension(704, 130));
        Pcabe.setPreferredSize(new java.awt.Dimension(704, 130));
        Pcabe.setLayout(null);

        Baceptar.setText("Aceptar");
        Baceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BaceptarActionPerformed(evt);
            }
        });
        Pcabe.add(Baceptar);
        Baceptar.setBounds(530, 100, 90, 24);

        alm_codiniE.setAncTexto(30);
        Pcabe.add(alm_codiniE);
        alm_codiniE.setBounds(80, 22, 200, 17);

        cLabel1.setText("De Almacen");
        Pcabe.add(cLabel1);
        cLabel1.setBounds(0, 22, 70, 17);

        cLabel3.setText("A");
        Pcabe.add(cLabel3);
        cLabel3.setBounds(370, 2, 20, 17);

        cLabel4.setText("Menor a");
        Pcabe.add(cLabel4);
        cLabel4.setBounds(440, 40, 50, 17);
        cLabel4.getAccessibleContext().setAccessibleName("Con mas de");

        Pcabe.add(pro_codiniE);
        pro_codiniE.setBounds(80, 2, 270, 17);

        cLabel11.setText("Días antes caducidad inf a");
        Pcabe.add(cLabel11);
        cLabel11.setBounds(10, 40, 160, 17);

        opIncRes.setText("Inc. Reservas");
        Pcabe.add(opIncRes);
        opIncRes.setBounds(580, 20, 110, 18);

        diasProdInfE.setText("0");
        diasProdInfE.setToolTipText("Congelados despues de  n Dias  desde la producción");
        Pcabe.add(diasProdInfE);
        diasProdInfE.setBounds(490, 40, 30, 17);
        Pcabe.add(pro_codfinE);
        pro_codfinE.setBounds(390, 2, 270, 17);

        cLabel8.setText("De Producto ");
        Pcabe.add(cLabel8);
        cLabel8.setBounds(2, 2, 70, 17);

        cLabel12.setText("De Almacen");
        Pcabe.add(cLabel12);
        cLabel12.setBounds(290, 22, 70, 17);

        alm_codfinE.setAncTexto(30);
        Pcabe.add(alm_codfinE);
        alm_codfinE.setBounds(370, 22, 200, 17);

        opDatosCompra.setSelected(true);
        opDatosCompra.setText("Ver Datos Compra");
        opDatosCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opDatosCompraActionPerforme(evt);
            }
        });
        Pcabe.add(opDatosCompra);
        opDatosCompra.setBounds(540, 40, 150, 18);

        cLabel13.setText("Dias transc. hasta cong sup.");
        Pcabe.add(cLabel13);
        cLabel13.setBounds(230, 40, 170, 17);

        diasProdSupE.setText("0");
        diasProdSupE.setToolTipText("Congelados despues de  n Dias  desde la producción");
        Pcabe.add(diasProdSupE);
        diasProdSupE.setBounds(400, 40, 30, 17);

        diasParaCadE.setToolTipText("Congelados despues de  n Dias  desde la producción");
        Pcabe.add(diasParaCadE);
        diasParaCadE.setBounds(170, 40, 40, 17);

        cLabel2.setText("Pais Nacido");
        Pcabe.add(cLabel2);
        cLabel2.setBounds(10, 60, 70, 15);
        Pcabe.add(acp_painacE);
        acp_painacE.setBounds(80, 60, 180, 18);

        cLabel10.setText("Cebado");
        Pcabe.add(cLabel10);
        cLabel10.setBounds(280, 60, 50, 15);
        Pcabe.add(acp_engpaiE);
        acp_engpaiE.setBounds(330, 60, 180, 18);

        cLabel5.setText("Sacrificado");
        Pcabe.add(cLabel5);
        cLabel5.setBounds(10, 80, 72, 15);
        Pcabe.add(acp_paisacE);
        acp_paisacE.setBounds(80, 80, 180, 18);

        cLabel15.setText("Matadero");
        Pcabe.add(cLabel15);
        cLabel15.setBounds(10, 100, 72, 15);

        acp_matadE.setMayusc(true);
        Pcabe.add(acp_matadE);
        acp_matadE.setBounds(80, 100, 150, 17);

        cLabel16.setText("Sala Desp.");
        Pcabe.add(cLabel16);
        cLabel16.setBounds(280, 80, 72, 15);
        Pcabe.add(acp_paisdeE);
        acp_paisdeE.setBounds(350, 80, 160, 18);

        acp_saldesE.setMayusc(true);
        Pcabe.add(acp_saldesE);
        acp_saldesE.setBounds(520, 80, 150, 17);

        cLabel18.setText("En Fecha");
        Pcabe.add(cLabel18);
        cLabel18.setBounds(290, 100, 60, 17);
        Pcabe.add(fecsalE);
        fecsalE.setBounds(350, 100, 70, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 0);
        Pprinc.add(Pcabe, gridBagConstraints);

        ArrayList v = new ArrayList();
        v.add("Fec.Inv"); // 0 Fecha Inv.
        v.add("Prod"); // 1 Codigo Producto.
        v.add("Nombre"); // 2 Nombre Producto
        v.add("Alm."); // 3 Almacen
        v.add("Ejerc."); // 4
        v.add("Serie"); // 5
        v.add("Lote"); // 6
        v.add("Indiv"); // 7
        v.add("Kilos"); // 8
        v.add("Unid."); // 9
        v.add("Fec.Cong"); // 10
        v.add("Fec.Cad"); // 11
        v.add("Nacido");// 12
        v.add("Engorde"); //13
        v.add("Sacrif."); // 14
        v.add("Desp."); // 14
        v.add("R.S. Despiece"); // 15
        v.add("R.S. Sacrif"); // 16
        v.add("Cod. E.A.N"); // 17
        jt.setCabecera(v);
        jt.setAlinearColumna(new int[]{1,0,0,2,2, 1,2,2,2,2, 1,1,0,0,0, 0,0,0,0});
        jt.setAnchoColumna(new int[]{70,40,150,30,40, 30,50,40,70,40, 70,70,40,40,40, 40,60,60,80});

        jt.setFormatoColumna(0, "dd-MM-yy");
        jt.setFormatoColumna(10, "dd-MM-yy");
        jt.setFormatoColumna(11, "dd-MM-yy");
        jt.setFormatoColumna(8, "---,--9.99");
        jt.setFormatoColumna(9, "---9");
        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jt.setMaximumSize(new java.awt.Dimension(500, 500));
        jt.setMinimumSize(new java.awt.Dimension(100, 100));
        jt.setPreferredSize(new java.awt.Dimension(101, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Pprinc.add(jt, gridBagConstraints);

        Ppie.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        Ppie.setMaximumSize(new java.awt.Dimension(300, 24));
        Ppie.setMinimumSize(new java.awt.Dimension(300, 24));
        Ppie.setPreferredSize(new java.awt.Dimension(300, 24));
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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 1.0;
        Pprinc.add(Ppie, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void opDatosCompraActionPerforme(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opDatosCompraActionPerforme
        // TODO add your handling code here:
    }//GEN-LAST:event_opDatosCompraActionPerforme

    private void BaceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BaceptarActionPerformed
        if (fecsalE.isNull()) {
            mensajeErr("Introduzca Fecha de Saldo");
            return;
        }
        try {
            feulinv = Formatear.getFechaDB(MvtosAlma.getFechaUltInv(dtCon1, fecsalE.getDate(), EU.em_cod));
        } catch (SQLException | ParseException ex) {
           Error("Error al recoger fecha ultimo inventario",ex);
        }
        
        if (!alm_codiniE.isNull() && alm_codfinE.isNull()) {
            alm_codfinE.setValorInt(alm_codiniE.getValorInt());
        }
        if (!pro_codiniE.isNull() && pro_codfinE.isNull()) {
            pro_codfinE.setValorInt(pro_codiniE.getValorInt());
        }
        condAlmacen = (alm_codiniE.getValorInt() == 0 ? "" : " and alm_codi >= " + alm_codiniE.getValorInt())
                + (alm_codfinE.getValorInt() == 0 ? "" : " and alm_codi <= " + alm_codfinE.getValorInt());
        ps = null;
        new miThread("") {
            @Override
            public void run() {
                consultar();
            }
        };
    }
    

        /**
        * Funcion principal , llamada cuando se pulsa el boton 'Aceptar'.
        */
        private void consultar()
        {
            cancelarConsulta=false;
            jt.removeAllDatos();

            msgEspere("Buscando datos...");
            popEspere_BCancelarSetEnabled(true);
            activar(false);

            String s = "select a.* from v_articulo as a where  a.pro_artcon !=0 "+
                (pro_codiniE.isNull()?"":" and a.pro_codi >= "+pro_codiniE.getValorInt())+
                (pro_codfinE.isNull()?"":" and a.pro_codi <= "+pro_codfinE.getValorInt())+
                " and a.pro_tiplot = 'V' "+ // Solo Prod. Vendibles                
                " order by pro_codi ";
            try
            {                
                if (!dtProd.select(s)) {
                    mensajeErr("No encontrados registros para estas condiciones");
                    activar(true);
                    resetMsgEspere();
                    return;
                }
                jt.tableView.setVisible(false);
                verDatos();
                jt.tableView.setVisible(true);
               
            } catch (Exception k) {
                Error("Error al buscar datos", k);
            }
        }
        /**
        *
        * 
        * @throws Exception
        */
       private void verDatos() throws Exception {
        String s;

        int nProd = 0;
        uniAct = 0;
        kilAct = 0;
        kilTot = 0;
        uniTot = 0;

        java.util.Date fecDesp, fecCadCong;
                
        ArrayList<ArrayList> datos = new ArrayList();
        
        utdesp.setVerDatosCompra(opDatosCompra.isSelected());
        do {
            if (cancelarConsulta) 
                break;

            iniciarStatement(dtProd.getInt("pro_codi"), feulinv, fecsalE.getFechaDB());
            rs = ps.executeQuery();
          
            setMensajePopEspere("Cargando datos de consulta. Producto: " + dtProd.getInt("pro_codi"), false);
            nProd = 0;
            uniAct = 0;
            kilAct = 0;

            int row;
            DatIndiv dtIndT;
            listIndiv.clear();
            while (rs.next()) {   // Sacando individuos activos de este producto.
                
                dtIndT = new DatIndiv();
                dtIndT.setAlmCodi(rs.getInt("alm_codi"));
                dtIndT.setProducto(dtProd.getInt("pro_codi"));
                dtIndT.setEjercLot(rs.getInt("pro_ejelot"));
                dtIndT.setSerie(rs.getString("pro_serlot"));
                dtIndT.setLote(rs.getInt("pro_numlot"));
                dtIndT.setNumind(rs.getInt("pro_indlot"));

                row = listIndiv.indexOf(dtIndT);
                if (row >= 0) {
                    dtIndT = listIndiv.get(row);
                }
                dtIndT.setCanti((rs.getString("tipmov").equals("=") ? 0 : dtIndT.getCanti())
                        + rs.getDouble("canti") * (rs.getString("tipmov").equals("S") ? -1 : 1));
                dtIndT.setNumuni((rs.getString("tipmov").equals("=") ? 0 : dtIndT.getNumuni())
                        + rs.getInt("unid") * (rs.getString("tipmov").equals("S") ? -1 : 1));
                if (row >= 0) {
                    listIndiv.set(row, dtIndT);
                } else {
                    listIndiv.add(dtIndT);
                }
            }
            if (opIncRes.isSelected())
            {
               rs = psStk.executeQuery();
           
               while (rs.next())
               {
                    dtIndT = new DatIndiv();
                    dtIndT.setAlmCodi(rs.getInt("alm_codori"));
                    dtIndT.setProducto(dtProd.getInt("pro_codi"));          
                    dtIndT.setEjercLot(rs.getInt("avp_ejelot"));
                    dtIndT.setSerie(rs.getString("avp_serlot"));
                    dtIndT.setLote(rs.getInt("avp_numpar"));
                    dtIndT.setNumind(rs.getInt("avp_numind"));              
                    dtIndT.setCanti(rs.getDouble("avp_canti"));
                    dtIndT.setNumuni(rs.getInt("avp_numuni"));
                    listIndiv.add(dtIndT);
               } 
            }

            boolean swEncDatTrazab;
            nProd=0;
            for (DatIndiv dtIndi : listIndiv) {
                if (cancelarConsulta) 
                    break;              
                if (dtIndi.getCanti() == 0) {
                    continue;
                }
                if (nProd % 10 == 0 )
                  setMensajePopEspere("Cargando datos de consulta. Producto: " + dtProd.getInt("pro_codi")+ " Linea: "+nProd, false);
                swEncDatTrazab = utdesp.busDatInd(dtIndi.getSerie(), dtIndi.getProducto(),
                        EU.em_cod, dtIndi.getEjercLot(), dtIndi.getLote(),
                        dtIndi.getNumind(), dtDesp, dtStat, EU);
                ArrayList v = new ArrayList();
                v.add(getFechaUltInv(dtStat, EU.em_cod,  dtIndi.getEjercLot(), dtIndi.getProducto(),dtIndi.getLote(), dtIndi.getSerie(),dtIndi.getNumind()));
                v.add(dtProd.getInt("pro_codi")); // 1
                v.add(dtProd.getString("pro_nomb")); //2 Producto
                v.add(dtIndi.getAlmCodi()); // 3
                v.add(dtIndi.getEjercLot()); // 4
                v.add(dtIndi.getSerie()); // 5
                v.add(dtIndi.getLote()); // 6
                v.add(dtIndi.getNumind()); //7
                v.add(dtIndi.getCanti()); // 8
                v.add(dtIndi.getNumuni());
                if (swEncDatTrazab) {
                    // Si el producto se compro ya congelado la fecha despiece es nulo, y hago equivalente la fecha Desp. a la fecha de cong.
                    fecCadCong = utdesp.getFechaCadDesp();

                    if (fecCadCong == null) {
                        fecCadCong = utdesp.getFechaCaducidadCompra();
                    }
                    
                    Date fechaCong=utdesp.getFechaDespiece()==null?utdesp.getFechaCompra():utdesp.getFechaDespiece();
                    if (Formatear.comparaFechas(fecCadCong,  fechaCong) < 40) {
                        fecCadCong = Formatear.sumaDiasDate( fechaCong, 23 * 30); // Le sumo 23 Meses  a la fecha Despiece                   
                    }
                    if (diasProdInfE.getValorInt()!=0 || diasProdSupE.getValorInt()!=0)
                    {
                        long dias;
                        if (utdesp.getFechaDespiece()==null)
                            dias=0;
                        else
                            dias=Formatear.comparaFechas(fechaCong,utdesp.getFechaCompra());
                        if (diasProdInfE.getValorInt()>0 && dias > diasProdInfE.getValorInt())
                           continue;
                        if (diasProdSupE.getValorInt()>0 && dias <= diasProdSupE.getValorInt())
                           continue;
                    }
                    if (diasParaCadE.getValorInt()!=0)
                    {
                        if (Formatear.comparaFechas(fecCadCong, fechaCong)<diasParaCadE.getValorInt())
                            continue;
                    }              
                    v.add(fechaCong);
                    v.add(fecCadCong);
                    if (!acp_painacE.isNull() && ! utdesp.getPaisNacimiento().equals(acp_painacE.getText()) )
                        continue;
                    v.add(utdesp.getPaisNacimiento());
                    if (!acp_engpaiE.isNull() && ! utdesp.getPaisEngorde().equals(acp_engpaiE.getText()) )
                        continue;
                    v.add(utdesp.getPaisEngorde());
                    if (!acp_paisacE.isNull() && ! utdesp.getPaisSacrificio().equals(acp_paisacE.getText()) )
                        continue;
                    v.add(utdesp.getPaisSacrificio());
                    if (!acp_paisdeE.isNull() && ! utdesp.getPaisSalaDespiece().equals(acp_paisdeE.getText()) )
                        continue;                    
                    v.add(utdesp.getPaisSalaDespiece()); 
                    if (!acp_saldesE.isNull() && ! utdesp.getSalaDespiece().equals(acp_saldesE.getText()) )
                        continue;       
                    v.add(utdesp.getSalaDespiece()); 
                    if (!acp_matadE.isNull() && ! utdesp.getSacrificado().equals(acp_matadE.getText()) )
                        continue;       
                    v.add(utdesp.getSacrificado());
                } else {
                    for (int n = 0; n < 8; n++) {
                        v.add("");
                    }
                }
                CodigoBarras codBarras=new CodigoBarras("",Formatear.format(dtIndi.getEjercLot(),"###9"),
                dtIndi.getSerie(),
                dtIndi.getLote(),dtIndi.getProducto(),
                dtIndi.getNumind(),
                dtIndi.getCanti());
                v.add(codBarras.getCodBarra()); // EAN
                
                datos.add(v);
             
//                jt.addLineas(datos);
                nProd++;
                uniAct += dtIndi.getNumuni();
                kilAct += dtIndi.getCanti();
                uniTot += dtIndi.getNumuni();;
                kilTot += dtIndi.getCanti();
            } // Fin bucle lista individuos   
            if (nProd > 1) 
                datos.add(addSubTotal());
        } while (dtProd.next() && !cancelarConsulta);
        jt.setDatos(datos);
        kilTotE.setValorDec(kilTot);
        uniTotE.setValorInt(uniTot);
        activar(true);
        resetMsgEspere();
        if (cancelarConsulta) {
            msgBox("Consulta Cancelada");
        } else {
            mensajeErr("Consulta realizada ...");
        }
        activar(true);
        mensaje("");
    }//GEN-LAST:event_BaceptarActionPerformed
    private Date getFechaUltInv(DatosTabla dt,int empCodi,int ejeNume,int proCodi,int proNupar,String proSerie,int proNumind) throws SQLException
    {
          String s = "select max(rgs_fecha) as cci_feccon from v_inventar as r  " +               
                "  where  pro_codi = "+proCodi+
                " and  emp_codi = " + empCodi+
                " and eje_nume = "+ejeNume+
                " and pro_nupar = "+proNupar+
                " and pro_serie = '"+proSerie+"'"+
                " and pro_numind= "+proNumind;                
                  
         if (!dt.select(s))
             return null;
         return dt.getDate("cci_feccon");
    }
       private ArrayList addSubTotal() {
        ArrayList v = new ArrayList();
        v.add(""); // 0 Fecha Inv.
        v.add(""); // 1 Codigo Producto.
        v.add("Total"); // 2 Nombre Producto
        v.add("."); // 3 Almacen
        v.add(""); // 4 
        v.add(""); // 5
        v.add(""); // 6
        v.add(""); // 7
        v.add(uniAct); // 8
        v.add(kilAct); // 9
        v.add(""); // 10
        v.add(""); // 11
        v.add("");// 12
        v.add(""); //13
        v.add(""); // 14
        v.add(""); // 14
        v.add(""); // 15
        v.add(""); // 16
        v.add(""); // 17
        
      
        return v;
    }
       /**
     * 
     * @param proCodi
     * @param fecini Formato yyyyMMdd
     * @param fecfin Formato yyyyMMdd
     * @throws SQLException 
     */
    private void iniciarStatement(int proCodi, String fecini, String fecfin) throws SQLException
    {      
        if (ps==null)
        {
           String sql="SELECT  mvt_tipdoc as sel, mvt_tipo as tipmov,mvt_time as fecmov, alm_codi, pro_codi,"
               + "pro_ejelot,pro_serlot,pro_numlot,pro_indlot,"+
                " "+        
                " mvt_canti as canti,mvt_prec as precio "+
                ", mvt_unid as unid " +
                " from mvtosalm where "+
                " mvt_canti <> 0 "+
                " AND pro_codi = ?" +
                  condAlmacen+
                 " AND mvt_time::date >= TO_DATE('" + fecini + "','yyyyMMdd') " +
              "   and  mvt_time::date <= TO_DATE('" + fecfin + "','yyyyMMdd') "+
              " UNION all " + // Inventarios
              " select 'RE' as sel,tir_afestk as tipmov,rgs_fecha as fecmov,alm_codi, pro_codi, eje_nume as pro_ejelot,"
               + "pro_serie as pro_serlot,pro_nupar as pro_numlot, pro_numind as pro_indlot,"+
              " r.rgs_kilos as canti,r.rgs_prregu as precio,1 as unid  " +
              " FROM v_regstock as r WHERE " +
              " tir_afestk = '='"+
              " and rgs_kilos <> 0" +
              " and rgs_trasp != 0 "+
              condAlmacen+
              " AND r.pro_codi = ? " +
              " AND r.rgs_fecha::date >= TO_DATE('" + fecini + "','yyyyMMdd') " +
              " and r.rgs_fecha::date <= TO_DATE('" + fecfin + "','yyyyMMdd') ";
          sql += " ORDER BY 3,2 desc"; // FECHA y tipo
          ps=dtCon1.getConexion().prepareStatement(dtCon1.getStrSelect(sql));
          sql="select a.* from v_albventa_detalle as a "
            + " where pro_codi = ?"+
            (alm_codiniE.getValorInt() == 0 ? "" : " and alm_codori >= " + alm_codiniE.getValorInt())
             + (alm_codfinE.getValorInt() == 0 ? "" : " and alm_codori <= " + alm_codfinE.getValorInt())+
            " and avc_fecalb <='"+fecfin+"'"+            
            " and avc_depos='D'"+ // Albaranes de Deposito
            " and  not exists(select * from v_proservdep as s "
            + " where a.pro_codi=s.pro_codi and a.avc_nume=s.avc_nume "
            + " and avs_fecha <= '"+fecfin+"'"
            + " and a.avc_serie=s.avc_serie and a.avc_ano=s.avc_ano"
            + " and avs_ejelot = avp_ejelot and avs_emplot= avp_emplot"
            + " and avs_serlot = avp_serlot and avs_numpar=avp_numpar and avs_numind=avp_numind)";           
          psStk = dtStat.getPreparedStatement(sql);
        }
        ps.setInt(1,proCodi);
        ps.setInt(2,proCodi);   
        psStk.setInt(1, proCodi);
    }
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
    private gnu.chu.camposdb.PaiPanel acp_engpaiE;
    private gnu.chu.controles.CTextField acp_matadE;
    private gnu.chu.camposdb.PaiPanel acp_painacE;
    private gnu.chu.camposdb.PaiPanel acp_paisacE;
    private gnu.chu.camposdb.PaiPanel acp_paisdeE;
    private gnu.chu.controles.CTextField acp_saldesE;
    private gnu.chu.controles.CLinkBox alm_codfinE;
    private gnu.chu.controles.CLinkBox alm_codiniE;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel10;
    private gnu.chu.controles.CLabel cLabel11;
    private gnu.chu.controles.CLabel cLabel12;
    private gnu.chu.controles.CLabel cLabel13;
    private gnu.chu.controles.CLabel cLabel15;
    private gnu.chu.controles.CLabel cLabel16;
    private gnu.chu.controles.CLabel cLabel18;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.controles.CTextField diasParaCadE;
    private gnu.chu.controles.CTextField diasProdInfE;
    private gnu.chu.controles.CTextField diasProdSupE;
    private gnu.chu.controles.CTextField fecsalE;
    private gnu.chu.controles.Cgrid jt;
    private gnu.chu.controles.CTextField kilTotE;
    private gnu.chu.controles.CCheckBox opDatosCompra;
    private gnu.chu.controles.CCheckBox opIncRes;
    private gnu.chu.camposdb.proPanel pro_codfinE;
    private gnu.chu.camposdb.proPanel pro_codiniE;
    private gnu.chu.controles.CTextField uniTotE;
    // End of variables declaration//GEN-END:variables

}

