package gnu.chu.anjelica.ventas;

import gnu.chu.Menu.Principal;
import static gnu.chu.anjelica.ventas.pdalbara.CAMBIA_CODIGO;
import static gnu.chu.anjelica.ventas.pdalbara.DEPOSITOS;
import static gnu.chu.anjelica.ventas.pdalbara.IMPR_ETIQDIRE;
import static gnu.chu.anjelica.ventas.pdalbara.IMPR_ETIQUETAS;
import gnu.chu.controles.StatusBar;
import gnu.chu.interfaces.PAD;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.navegador;
import gnu.chu.utilidades.ventanaPad;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Hashtable;

public class CargaAlbVentas extends ventanaPad  implements PAD  
{
    String condWhere;
    public CargaAlbVentas(EntornoUsuario eu, Principal p, Hashtable<String, String> ht) {
        EU = eu;
        vl = p.panel1;
        jf = p;
        eje = true;

        setTitulo("Carga Albaranes Venta");
        setAcronimo("caalve");
        try
        {

            ponParametros(ht);
            if (jf.gestor.apuntar(this))
                jbInit();
            else
                setErrorInit(true);
        } catch (Exception e)
        {
            ErrorInit(e);          
        }
    }
    private void jbInit() throws Exception
    {
       this.setSize(663,524);
       statusBar = new StatusBar(this);
        nav = new navegador(this, dtCons, false, navegador.NORMAL);
      
        iniciarFrame();
        this.setVersion("2018-05-01 "+(isAdmin() ?"Administrador":""));
        condWhere=" where emp_codi =  "+EU.em_cod;
        strSql = "SELECT * FROM coninvcab "+condWhere+
         "order by cci_feccon,cam_codi,alm_codi";

        this.getContentPane().add(nav, BorderLayout.NORTH);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        this.setPad(this);
        dtCons.setLanzaDBCambio(false);
        conecta();
       
        initComponents();
        iniciarBotones(Baceptar, Bcancelar);
        navActivarAll();
       
       
        
        activar(false);  
  }
    private void ponParametros(Hashtable<String, String> ht) {
        if (ht == null)
            return;

    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cPanel1 = new gnu.chu.controles.CPanel();
        cPanel2 = new gnu.chu.controles.CPanel();
        Pprinc = new gnu.chu.controles.CPanel();
        Pcabe = new gnu.chu.controles.CPanel();
        cLabel11 = new gnu.chu.controles.CLabel();
        cli_codiE = new gnu.chu.camposdb.cliPanel();
        cLabel6 = new gnu.chu.controles.CLabel();
        avc_fecalbE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel7 = new gnu.chu.controles.CLabel();
        avc_numeE = new gnu.chu.controles.CTextField(Types.DECIMAL,"####9");
        cLabel8 = new gnu.chu.controles.CLabel();
        pvc_anoE = new gnu.chu.controles.CTextField(Types.DECIMAL, "###9");
        pvc_numeE = new gnu.chu.controles.CTextField(Types.DECIMAL, "#####9");
        BbusPed = new gnu.chu.controles.CButton(Iconos.getImageIcon("find"));
        avc_cerraE = new gnu.chu.controles.CCheckBox();
        avc_deposE = new gnu.chu.controles.CComboBox();
        Ppie = new gnu.chu.controles.CPanel();
        cLabel1 = new gnu.chu.controles.CLabel();
        numLinE = new gnu.chu.controles.CTextField(Types.DECIMAL, "##9");
        cLabel2 = new gnu.chu.controles.CLabel();
        kilosE = new gnu.chu.controles.CTextField(Types.DECIMAL, "----,--9.99");
        cLabel3 = new gnu.chu.controles.CLabel();
        unidE = new gnu.chu.controles.CTextField(Types.DECIMAL, "---9");
        BmvReg = new gnu.chu.controles.CButton(Iconos.getImageIcon("run"));
        despieceC = new gnu.chu.controles.CButtonMenu(Iconos.getImageIcon("cuchillo"));
        Butil = new gnu.chu.controles.CButtonMenu(Iconos.getImageIcon("configure"));
        Baceptar = new gnu.chu.controles.CButton();
        Bcancelar = new gnu.chu.controles.CButton();
        TCabe = new gnu.chu.controles.CTabbedPane();
        Pgrid = new gnu.chu.controles.CPanel();
        jt = new gnu.chu.controles.Cgrid(8);
        jtRes = new gnu.chu.controles.Cgrid(5);
        BReset = new gnu.chu.controles.CButton(Iconos.getImageIcon("reload"));
        Ppedido = new gnu.chu.controles.CPanel();
        PCliente = new gnu.chu.controles.CPanel();
        Phist = new gnu.chu.controles.CPanel();

        javax.swing.GroupLayout cPanel1Layout = new javax.swing.GroupLayout(cPanel1);
        cPanel1.setLayout(cPanel1Layout);
        cPanel1Layout.setHorizontalGroup(
            cPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        cPanel1Layout.setVerticalGroup(
            cPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout cPanel2Layout = new javax.swing.GroupLayout(cPanel2);
        cPanel2.setLayout(cPanel2Layout);
        cPanel2Layout.setHorizontalGroup(
            cPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        cPanel2Layout.setVerticalGroup(
            cPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        Pprinc.setLayout(null);

        Pcabe.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        Pcabe.setLayout(null);

        cLabel11.setText("Cliente");
        Pcabe.add(cLabel11);
        cLabel11.setBounds(10, 20, 50, 18);
        Pcabe.add(cli_codiE);
        cli_codiE.setBounds(60, 20, 350, 18);

        cLabel6.setText("Albaran");
        Pcabe.add(cLabel6);
        cLabel6.setBounds(10, 0, 50, 17);
        Pcabe.add(avc_fecalbE);
        avc_fecalbE.setBounds(170, 0, 76, 17);

        cLabel7.setText("Pedido");
        Pcabe.add(cLabel7);
        cLabel7.setBounds(260, 0, 50, 17);
        Pcabe.add(avc_numeE);
        avc_numeE.setBounds(60, 0, 50, 17);

        cLabel8.setText("Fecha");
        Pcabe.add(cLabel8);
        cLabel8.setBounds(120, 0, 50, 17);
        Pcabe.add(pvc_anoE);
        pvc_anoE.setBounds(300, 0, 35, 17);
        Pcabe.add(pvc_numeE);
        pvc_numeE.setBounds(340, 0, 45, 17);
        Pcabe.add(BbusPed);
        BbusPed.setBounds(390, 0, 20, 20);

        avc_cerraE.setText("Cerrado");
        Pcabe.add(avc_cerraE);
        avc_cerraE.setBounds(420, 2, 70, 18);

        avc_deposE.addItem(DEPOSITOS);
        Pcabe.add(avc_deposE);
        avc_deposE.setBounds(420, 20, 90, 17);

        Pprinc.add(Pcabe);
        Pcabe.setBounds(0, 0, 530, 50);

        Ppie.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Ppie.setLayout(null);

        cLabel1.setText("N.Lineas");
        Ppie.add(cLabel1);
        cLabel1.setBounds(10, 0, 60, 15);

        numLinE.setEditable(false);
        Ppie.add(numLinE);
        numLinE.setBounds(70, 0, 40, 17);

        cLabel2.setText("Kilos");
        Ppie.add(cLabel2);
        cLabel2.setBounds(240, 0, 40, 15);

        kilosE.setEditable(false);
        Ppie.add(kilosE);
        kilosE.setBounds(280, 0, 50, 17);

        cLabel3.setText("Unidades");
        Ppie.add(cLabel3);
        cLabel3.setBounds(120, 0, 60, 15);

        unidE.setEditable(false);
        Ppie.add(unidE);
        unidE.setBounds(180, 0, 40, 17);

        BmvReg.setText("Mvto.Reg (F9)");
        Ppie.add(BmvReg);
        BmvReg.setBounds(10, 20, 100, 19);

        despieceC.addMenu(" ", " ");
        despieceC.addMenu("NO Desp.", "N");
        despieceC.addMenu("Despiece", "D");
        despieceC.setToolTipText("Realizar AutoDespiece sobre lecturas");
        Ppie.add(despieceC);
        despieceC.setBounds(120, 20, 100, 20);

        Butil.addMenu("Etiq.Direc.", ""+IMPR_ETIQDIRE);
        Butil.addMenu("Etiq.Prod.", ""+IMPR_ETIQUETAS);
        Butil.addMenu("Cambia.Prod.", ""+CAMBIA_CODIGO);
        Butil.setToolTipText("Utilidades");
        Ppie.add(Butil);
        Butil.setBounds(350, 0, 60, 20);

        Baceptar.setText("Aceptar");
        Ppie.add(Baceptar);
        Baceptar.setBounds(260, 20, 80, 19);

        Bcancelar.setText("Cancelar");
        Ppie.add(Bcancelar);
        Bcancelar.setBounds(360, 20, 100, 19);

        Pprinc.add(Ppie);
        Ppie.setBounds(0, 250, 500, 50);

        Pgrid.setLayout(null);

        ArrayList vcjt=new ArrayList();
        vcjt.add("Codigo"); // 0
        vcjt.add("Nombre"); // 1
        vcjt.add("Ejer"); // 2
        vcjt.add("Ser"); // 3
        vcjt.add("Lote"); // 4
        vcjt.add("Ind"); // 5
        vcjt.add("Kilos"); // 6
        vcjt.add("NL"); // 7
        jt.setCabecera(vcjt);
        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Pgrid.add(jt);
        jt.setBounds(0, 10, 510, 90);

        ArrayList vrjt= new ArrayList();
        vrjt.add("Codigo"); // 0
        vrjt.add("Nombre"); // 1
        vcjt.add("Kg.Total"); // 2
        vcjt.add("Kg.Parc"); // 3
        vcjt.add("Un.Total"); // 4
        vcjt.add("Un.Parcial"); // 5
        jtRes.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Pgrid.add(jtRes);
        jtRes.setBounds(0, 110, 470, 50);

        BReset.setToolTipText("Resetear Parciales");
        Pgrid.add(BReset);
        BReset.setBounds(480, 120, 24, 24);
        BReset.getAccessibleContext().setAccessibleName("");

        TCabe.addTab("Albaran", Pgrid);

        javax.swing.GroupLayout PpedidoLayout = new javax.swing.GroupLayout(Ppedido);
        Ppedido.setLayout(PpedidoLayout);
        PpedidoLayout.setHorizontalGroup(
            PpedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 525, Short.MAX_VALUE)
        );
        PpedidoLayout.setVerticalGroup(
            PpedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 162, Short.MAX_VALUE)
        );

        TCabe.addTab("Pedidos", Ppedido);

        javax.swing.GroupLayout PClienteLayout = new javax.swing.GroupLayout(PCliente);
        PCliente.setLayout(PClienteLayout);
        PClienteLayout.setHorizontalGroup(
            PClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 525, Short.MAX_VALUE)
        );
        PClienteLayout.setVerticalGroup(
            PClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 162, Short.MAX_VALUE)
        );

        TCabe.addTab("Cliente", PCliente);

        javax.swing.GroupLayout PhistLayout = new javax.swing.GroupLayout(Phist);
        Phist.setLayout(PhistLayout);
        PhistLayout.setHorizontalGroup(
            PhistLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 525, Short.MAX_VALUE)
        );
        PhistLayout.setVerticalGroup(
            PhistLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 162, Short.MAX_VALUE)
        );

        TCabe.addTab("Historico", Phist);

        Pprinc.add(TCabe);
        TCabe.setBounds(0, 50, 530, 190);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton BReset;
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CButton BbusPed;
    private gnu.chu.controles.CButton Bcancelar;
    private gnu.chu.controles.CButton BmvReg;
    private gnu.chu.controles.CButtonMenu Butil;
    private gnu.chu.controles.CPanel PCliente;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Pgrid;
    private gnu.chu.controles.CPanel Phist;
    private gnu.chu.controles.CPanel Ppedido;
    private gnu.chu.controles.CPanel Ppie;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CTabbedPane TCabe;
    private gnu.chu.controles.CCheckBox avc_cerraE;
    private gnu.chu.controles.CComboBox avc_deposE;
    private gnu.chu.controles.CTextField avc_fecalbE;
    private gnu.chu.controles.CTextField avc_numeE;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel11;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.controles.CPanel cPanel1;
    private gnu.chu.controles.CPanel cPanel2;
    private gnu.chu.camposdb.cliPanel cli_codiE;
    private gnu.chu.controles.CButtonMenu despieceC;
    private gnu.chu.controles.Cgrid jt;
    private gnu.chu.controles.Cgrid jtRes;
    private gnu.chu.controles.CTextField kilosE;
    private gnu.chu.controles.CTextField numLinE;
    private gnu.chu.controles.CTextField pvc_anoE;
    private gnu.chu.controles.CTextField pvc_numeE;
    private gnu.chu.controles.CTextField unidE;
    // End of variables declaration//GEN-END:variables

    @Override
    public void PADPrimero() {
          verDatos(dtCons);
    nav.setPulsado(navegador.NINGUNO);
    }

    @Override
    public void PADAnterior() {
          verDatos(dtCons);
        nav.setPulsado(navegador.NINGUNO);
    }

    @Override
    public void PADSiguiente() {
      verDatos(dtCons);
      nav.setPulsado(navegador.NINGUNO);
    }

    @Override
    public void PADUltimo() {
      verDatos(dtCons);
    nav.setPulsado(navegador.NINGUNO);
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
    
    void verDatos(DatosTabla dt)
    {
        
    }
}
