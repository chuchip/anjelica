package gnu.chu.anjelica.ventas;

/*
 *<p>Titulo: CLDepCli </p>
 * <p>Descripción: Consulta/Listado Depositos de clientes </p>
 * Este programa muestra el inventario en deposito tanto de un cliente como en total, en una fecha dada.
 * Permite sacar el genero entregado de una fecha a otra, asi como lo metido a deposito de una fecha a otra.
 * 
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
import gnu.chu.Menu.Principal;
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
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CLDepCli extends ventana
{
    String tipoConsulta;
   
    final int JTCAB_EMPALB=1;
    final int JTCAB_EJEALB=2;
    final int JTCAB_SERALB=3;
    final int JTCAB_NUMALB=4;

    final int JTCAB_ALBDEP=7;
    
     public CLDepCli(EntornoUsuario eu, Principal p) {
        this(eu, p, null);
    }

    public CLDepCli(EntornoUsuario eu, Principal p, HashMap<String,String> ht) {
        EU = eu;
        vl = p.panel1;
        jf = p;
        eje = true;

        try {
           
            setTitulo("Cons/List. Depositos de Clientes");
            if (jf.gestor.apuntar(this)) {
                jbInit();
            } else {
                setErrorInit(true);
            }
        } catch (Exception e) {
           ErrorInit(e);
        }
    }

    public CLDepCli(gnu.chu.anjelica.menu p, EntornoUsuario eu) {
        EU = eu;
        vl = p.getLayeredPane();
        eje = false;

        try {         
            setTitulo("Cons/List. Depositos de Clientes");
            jbInit();
        } catch (Exception e) {
           ErrorInit(e);
        }
    }
   
    private void jbInit() throws Exception {
        statusBar = new StatusBar(this);

        iniciarFrame();

        this.setVersion("2017-07-07");

        initComponents();
        this.setSize(new Dimension(730, 535));
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);

        conecta();
    }

    @Override
    public void iniciarVentana() throws Exception 
    {
        feciniE.setDate(Formatear.getDateAct());
        fecInvE.setDate(Formatear.getDateAct());
        cli_codiE.iniciar(dtStat, this, vl, EU);
        gnu.chu.anjelica.pad.pdconfig.llenaDiscr(dtStat,cam_codiE,"AC",EU.em_cod);
        PCabe.setDefButton(Baceptar);
        
        activarEventos();
    }
    
    void activarEventos()
    {
      Baceptar.addActionListener(new ActionListener(){
       @Override
       public void actionPerformed(ActionEvent e){
           verDatos();
       } 
      });
      tipoConsC.addActionListener(new ActionListener(){
       @Override
       public void actionPerformed(ActionEvent e){
           fecfinE.setEnabled(! tipoConsC.getValor().equals("I"));
       } 
      });
      jtCab.addMouseListener(new MouseAdapter()
      {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() < 2)
                    return;
                if (jtCab.isVacio())
                    return;

                irAlbaran();
            }
      });   
      jtStock.addMouseListener(new MouseAdapter()
      {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() < 2)
                    return;
                if (jtStock.isVacio())
                    return;

                irAlbaranStock();
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

       cm.setSerieAlbaran(jtCab.getValString(JTCAB_SERALB));
       cm.setEmpresaAlbaran(jtCab.getValorInt(JTCAB_EMPALB));
       cm.setNumeroAlbaran(jtCab.getValorInt(JTCAB_NUMALB));
       cm.setEjercAlbaran(jtCab.getValorInt(JTCAB_EJEALB));
    
       cm.ej_query();
       cm.setAlbaranDeposito(jtCab.getValorInt(JTCAB_ALBDEP));
       jf.gestor.ir(cm);
    }
    void irAlbaranStock()
    {         
        if (jtStock.getValString(7).equals(""))
            return;
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
       String[] alb=jtStock.getValString(7).split("-");

       cm.setSerieAlbaran(alb[1]);       
       cm.setNumeroAlbaran(Integer.parseInt(alb[2]));
       cm.setEjercAlbaran(Integer.parseInt(alb[0]));    
       cm.ej_query();
       jf.gestor.ir(cm);
    }
    void verDatos()
    {
        try
        {
            tipoConsulta=tipoConsC.getValor();
            if (feciniE.isNull())
            {
                mensajeErr("Introduzca Fecha Inicio");
                feciniE.requestFocus();
                return;
            }
            if (!tipoConsulta.equals("I"))
            {// Salida o Entrada
               
                if (fecfinE.isNull())
                    fecfinE.setText(tipoConsulta.equals("E")?Formatear.getFechaAct("dd-MM-yyyy"):feciniE.getText());
            }
            else
            {
                 if (fecInvE.isNull())
                {
                    mensajeErr("Introduzca Fecha Inventario");
                    fecInvE.requestFocus();
                    return;
                }
            }
            switch (tipoConsulta)
            {
                case "S":
                    verSalidas();
                    break;
                case "E":
                    verEntradas();
                    break;
                case "I":
                    verInventario();
                    break;
            }
        } catch (Exception ex)
        {
            Error("Error al Realizar Consulta",ex);
        }
            
    }   
    void verSalidas() throws Exception
    {
         jtCab.removeAllDatos();
        String s="select * from v_albdepserv   where avs_fecha between '"+feciniE.getFechaDB()+
            "' and '"+fecfinE.getFechaDB()+"'"+
            (cli_codiE.isNull()?"":" and cli_codi = "+cli_codiE.getValorInt())+
            " order by avs_fecha";
        if (! dtCon1.select(s))
        {
            msgBox("Ningun albaran deposito tiene mercancia servida con estos criterios");
            return;
        }
       
        
        do
        {
            ArrayList v=new ArrayList();
            v.add(dtCon1.getFecha("avc_fecalb","dd-MM-yy")); // 0
            v.add(dtCon1.getInt("emp_codi"));
            v.add(dtCon1.getInt("avc_ano"));
            v.add(dtCon1.getString("avc_serie"));
            v.add(dtCon1.getInt("avc_nume"));
            v.add(dtCon1.getInt("cli_codi"));
            v.add(dtCon1.getString("cli_nomb"));
            v.add(dtCon1.getInt("avs_nume"));
            v.add(dtCon1.getFecha("avs_fecha","dd-MM-yy"));
            jtCab.addLinea(v);
        } while (dtCon1.next());
        jtCab.requestFocus();
        mensajeErr("Consulta realizada");
    }
    void verEntradas() throws Exception
    {
         jtCab.removeAllDatos();
          String s="select * from v_albavec as c,v_cliente as cl   where avc_fecalb between '"+feciniE.getFechaDB()+
            "' and '"+fecfinE.getFechaDB()+"'"+
            " and avc_depos ='D'" +
              " and c.cli_codi = cl.cli_codi "+
            (cli_codiE.isNull()?"":" and c.cli_codi = "+cli_codiE.getValorInt())+
            " order by avc_fecalb";
        if (! dtCon1.select(s))
        {
            msgBox("Ningun albaran metido a Deposito en este periodo");
            return;
        }
       
        
        do
        {
            ArrayList v=new ArrayList();
            v.add(dtCon1.getFecha("avc_fecalb","dd-MM-yy")); // 0
            v.add(dtCon1.getInt("emp_codi"));
            v.add(dtCon1.getInt("avc_ano"));
            v.add(dtCon1.getString("avc_serie"));
            v.add(dtCon1.getInt("avc_nume"));
            v.add(dtCon1.getInt("cli_codi"));
            v.add(dtCon1.getString("cli_nomb"));
            v.add("");
            v.add("");
            jtCab.addLinea(v);
        } while (dtCon1.next());
        jtCab.requestFocus();
        mensajeErr("Consulta realizada");  
    }
    
    void verInventario() throws Exception
    {
        Ptab.setSelectedIndex(1);
        jtStock.removeAllDatos();
        int cciCodi=0;
        String s;
        if (!cam_codiE.isNull())
        {
            s="select cci_codi,cci_feccon  from coninvcab where cam_codi = '"+cam_codiE.getText()+"'"+
                " order by cci_feccon desc";
            if (!dtStat.select(s))
            {
                msgBox("No encontrados inventarios sobre esta camara");
                return;
            }
            cciCodi=dtStat.getInt("cci_codi");
        }
        s="select a.*,ar.pro_nomb as art_nomb,cl.cli_nomb from v_albventa_detalle as a,"
            + " v_articulo as ar,v_cliente as cl "
            + " where  a.pro_codi=ar.pro_codi "+
            " and cl.cli_codi = a.cli_codi "+
            (feciniE.isNull()?"":" and avc_fecalb >='"+feciniE.getFechaDB()+"'")+
            (fecfinE.isNull()?"":" and avc_fecalb <='"+fecfinE.getFechaDB()+"'")+
            (cli_codiE.isNull()?"":" and a.cli_codi = "+cli_codiE.getValorInt())+
            " and avc_depos='D'"+ // Albaranes de Deposito
            " and  not exists(select * from v_proservdep as s "
            + " where a.pro_codi=s.pro_codi and a.avc_nume=s.avc_nume "
            + " and avs_fecha < '"+fecInvE.getFechaDB()+"'"
            + " and a.avc_serie=s.avc_serie and a.avc_ano=s.avc_ano"
            + " and avs_ejelot = avp_ejelot and avs_emplot= avp_emplot"
            + " and avs_serlot = avp_serlot and avs_numpar=avp_numpar and avs_numind=avp_numind)"
            +(cciCodi==0?"": " and exists (select * from coninvlin as inl where cci_codi="+cciCodi+
            " and inl.pro_codi=a.pro_codi "+
            " and inl.prp_ano =a.avp_ejelot"+
            " and inl.prp_seri = a.avp_serlot"+
            " and inl.prp_part= a.avp_numpar"+
            " and inl.prp_indi=a.avp_numind)")
            +" order by cli_codi,pro_codi,avc_ano,avc_serie,avc_nume";
        if (!dtCon1.select(s))
        {
            msgBox("No encontrado genero en deposito con estas condiciones");
            return;
        }    
        int numUni=0,nl=0;
        double kg=0;
         int numUniCl=0,nlCl=0;
        double kgCl=0;
        int proCodi=dtCon1.getInt("pro_codi");
        int cliCodi=dtCon1.getInt("cli_codi");
        do
        {
          ArrayList v= new ArrayList();
          if (cliCodi!=dtCon1.getInt("cli_codi"))
          {
              verAcumulado("Producto:",numUni,kg,nl);
              verAcumulado("Cliente:",numUniCl,kgCl,nlCl-nl);
              cliCodi=dtCon1.getInt("cli_codi");
              proCodi=dtCon1.getInt("pro_codi");
              numUniCl=0;
              kgCl=0;
              nlCl=0;
              numUni=0;
              kg=0;
              nl=0;
          }
          if (proCodi!=dtCon1.getInt("pro_codi"))
          {
              verAcumulado("Producto:",numUni,kg,nl);
              proCodi=dtCon1.getInt("pro_codi");
              numUni=0;
              kg=0;
              nl=0;
          }
          v.add(dtCon1.getInt("pro_codi"));
          v.add(dtCon1.getString("pro_nomb"));
          v.add(dtCon1.getString("avp_ejelot")+dtCon1.getString("avp_serlot")+
              dtCon1.getString("avp_numpar")+"-"+
              dtCon1.getString("avp_numind"));
          v.add(dtCon1.getInt("avl_unid"));
          v.add(dtCon1.getDouble("avl_canti"));
          v.add(dtCon1.getString("cli_codi"));
          v.add(dtCon1.getString("cli_nomb"));
          v.add(dtCon1.getString("avc_ano")+"-"+dtCon1.getString("avc_serie")+"-"+dtCon1.getString("avc_nume"));
          v.add(dtCon1.getFecha("avc_fecalb","dd-MM-yy"));
          numUni+=dtCon1.getInt("avl_unid");
          kg+=dtCon1.getDouble("avl_canti");
          kgCl+=dtCon1.getDouble("avl_canti");
          numUniCl+=dtCon1.getInt("avl_unid");
          nl++;
          nlCl++;
          jtStock.addLinea(v);
        } while (dtCon1.next());
        verAcumulado("Producto:",numUni,kg,nl);
        mensajeErr("Consulta realizada");
    }
    void verAcumulado(String total,int numUni,double kg, int nl)
    {
        if (nl<=1)
            return;
        ArrayList v=new ArrayList();
        v.add("");
        v.add("Total "+total);
        v.add("");
        v.add(numUni);
        v.add(kg);
        v.add("");
        v.add("");
        v.add("");
        v.add("");
        jtStock.addLinea(v);
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

        Pprinc = new gnu.chu.controles.CPanel();
        PCabe = new gnu.chu.controles.CPanel();
        cLabel6 = new gnu.chu.controles.CLabel();
        feciniE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel10 = new gnu.chu.controles.CLabel();
        cli_codiE = new gnu.chu.camposdb.cliPanel();
        cLabel1 = new gnu.chu.controles.CLabel();
        tipoConsC = new gnu.chu.controles.CComboBox();
        Baceptar = new gnu.chu.controles.CButton(Iconos.getImageIcon("check"));
        cLabel7 = new gnu.chu.controles.CLabel();
        fecfinE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel8 = new gnu.chu.controles.CLabel();
        fecInvE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel11 = new gnu.chu.controles.CLabel();
        cam_codiE = new gnu.chu.controles.CLinkBox();
        PPie = new gnu.chu.controles.CPanel();
        Ptab = new gnu.chu.controles.CTabbedPane();
        PSalida = new gnu.chu.controles.CPanel();
        jtCab = new gnu.chu.controles.Cgrid(9);
        jtLin = new gnu.chu.controles.Cgrid(5);
        cPanel2 = new gnu.chu.controles.CPanel();
        jtStock = new gnu.chu.controles.Cgrid(9);

        Pprinc.setLayout(new java.awt.GridBagLayout());

        PCabe.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        PCabe.setMaximumSize(new java.awt.Dimension(549, 71));
        PCabe.setMinimumSize(new java.awt.Dimension(549, 71));
        PCabe.setPreferredSize(new java.awt.Dimension(549, 71));
        PCabe.setLayout(null);

        cLabel6.setText("De Fec. Alb");
        PCabe.add(cLabel6);
        cLabel6.setBounds(0, 2, 70, 17);
        PCabe.add(feciniE);
        feciniE.setBounds(70, 2, 76, 17);

        cLabel10.setText("Camara");
        PCabe.add(cLabel10);
        cLabel10.setBounds(10, 50, 65, 18);
        PCabe.add(cli_codiE);
        cli_codiE.setBounds(70, 26, 360, 18);

        cLabel1.setText("Tipo");
        PCabe.add(cLabel1);
        cLabel1.setBounds(270, 0, 40, 17);

        tipoConsC.addItem("Salidas","S");
        tipoConsC.addItem("Entradas","E");
        tipoConsC.addItem("Inventario","I");
        //tipoConsC.addItem("Movimientos","M");
        tipoConsC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipoConsCActionPerformed(evt);
            }
        });
        PCabe.add(tipoConsC);
        tipoConsC.setBounds(310, 0, 90, 17);

        Baceptar.setText("Aceptar");
        PCabe.add(Baceptar);
        Baceptar.setBounds(440, 40, 100, 24);

        cLabel7.setText("A ");
        PCabe.add(cLabel7);
        cLabel7.setBounds(160, 2, 20, 17);
        PCabe.add(fecfinE);
        fecfinE.setBounds(180, 0, 76, 17);

        cLabel8.setText("Fec. Inv.");
        PCabe.add(cLabel8);
        cLabel8.setBounds(420, 0, 50, 17);
        PCabe.add(fecInvE);
        fecInvE.setBounds(470, 0, 76, 17);

        cLabel11.setText("De Cliente");
        PCabe.add(cLabel11);
        cLabel11.setBounds(5, 26, 65, 18);

        cam_codiE.setAncTexto(25);
        cam_codiE.setMayusculas(true);
        cam_codiE.setPreferredSize(new java.awt.Dimension(250, 17));
        PCabe.add(cam_codiE);
        cam_codiE.setBounds(70, 50, 230, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        Pprinc.add(PCabe, gridBagConstraints);

        PPie.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        PPie.setMaximumSize(new java.awt.Dimension(500, 30));
        PPie.setMinimumSize(new java.awt.Dimension(500, 30));
        PPie.setPreferredSize(new java.awt.Dimension(500, 31));
        PPie.setLayout(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        Pprinc.add(PPie, gridBagConstraints);

        PSalida.setLayout(new java.awt.GridBagLayout());

        ArrayList v1=new ArrayList();
        v1.add("Fec.Alb"); // 0
        v1.add("Emp"); // 1
        v1.add("Ejerc."); // 2
        v1.add("Ser"); // 3
        v1.add("Num."); // 4
        v1.add("Cliente"); // 5
        v1.add("Nombre"); // 6
        v1.add("NºSal."); // 7
        v1.add("Fec.Sal"); // 8
        jtCab.setCabecera(v1);
        jtCab.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtCab.setAjustarGrid(true);
        jtCab.setMaximumSize(new java.awt.Dimension(100, 200));
        jtCab.setMinimumSize(new java.awt.Dimension(100, 200));
        jtCab.setPreferredSize(new java.awt.Dimension(100, 201));
        jtCab.setAnchoColumna(new int[]{55,20,30,20,40,40,200,45,55});
        jtCab.setAlinearColumna(new int[]{1,2,2,1,2,2,0,2,1});
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        PSalida.add(jtCab, gridBagConstraints);

        ArrayList v=new ArrayList();

        v.add("Articulo"); // 0
        v.add("Descrip."); // 1
        v.add("Individuo"); // 2
        v.add("Unid"); // 3
        v.add("Kilos"); // 4
        jtLin.setCabecera(v);
        jtLin.setAnchoColumna(new int[]{40,150,90,30,60});
        jtLin.setAlinearColumna(new int[]{2,0,0,2,2});
        jtLin.setAjustarGrid(true);
        jtLin.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtLin.setMaximumSize(new java.awt.Dimension(100, 100));
        jtLin.setMinimumSize(new java.awt.Dimension(100, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        PSalida.add(jtLin, gridBagConstraints);

        Ptab.addTab("Salidas", PSalida);

        cPanel2.setLayout(new java.awt.BorderLayout());

        {ArrayList vs=new ArrayList();

            vs.add("Articulo"); // 0
            vs.add("Descrip."); // 1
            vs.add("Individuo"); // 2
            vs.add("Unid"); // 3
            vs.add("Kilos"); // 4
            vs.add("Cliente"); // 5
            vs.add("Nombre Cliente"); // 6
            vs.add("Alb."); // 7
            vs.add("Fec.Alb."); // 8
            jtStock.setCabecera(vs);
        }
        jtStock.setAnchoColumna(new int[]{40,150,90,30,60,45,150,80,70});
        jtStock.setAlinearColumna(new int[]{2,0,0,2,2,2,0,0,1});
        jtStock.setFormatoColumna(4,"##,##9.99");
        jtStock.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtStock.setMaximumSize(new java.awt.Dimension(100, 100));
        jtStock.setMinimumSize(new java.awt.Dimension(100, 100));
        cPanel2.add(jtStock, java.awt.BorderLayout.CENTER);

        Ptab.addTab("Inventario", cPanel2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Pprinc.add(Ptab, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tipoConsCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipoConsCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tipoConsCActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CPanel PCabe;
    private gnu.chu.controles.CPanel PPie;
    private gnu.chu.controles.CPanel PSalida;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CTabbedPane Ptab;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel10;
    private gnu.chu.controles.CLabel cLabel11;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.controles.CPanel cPanel2;
    private gnu.chu.controles.CLinkBox cam_codiE;
    private gnu.chu.camposdb.cliPanel cli_codiE;
    private gnu.chu.controles.CTextField fecInvE;
    private gnu.chu.controles.CTextField fecfinE;
    private gnu.chu.controles.CTextField feciniE;
    private gnu.chu.controles.Cgrid jtCab;
    private gnu.chu.controles.Cgrid jtLin;
    private gnu.chu.controles.Cgrid jtStock;
    private gnu.chu.controles.CComboBox tipoConsC;
    // End of variables declaration//GEN-END:variables
}
