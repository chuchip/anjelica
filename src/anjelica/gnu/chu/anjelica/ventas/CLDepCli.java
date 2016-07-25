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

        this.setVersion("2016-07-24");

        initComponents();
        this.setSize(new Dimension(730, 535));
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);

        conecta();
    }

    @Override
    public void iniciarVentana() throws Exception 
    {
        fechaE.setDate(Formatear.getDateAct());
        cli_codiE.iniciar(dtStat, this, vl, EU);
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
    void verDatos()
    {
        try
        {
            tipoConsulta=tipoConsC.getValor();
            if (fechaE.isNull())
            {
                mensajeErr("Introduzca Fecha Inicio");
                fechaE.requestFocus();
                return;
            }
            if (!tipoConsulta.equals("I"))
            {
                if (fecfinE.isNull())
                    fecfinE.setText(fechaE.getText());
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
        String s="select * from v_albdepserv   where avs_fecha between '"+fechaE.getFechaDB()+
            "' and '"+fecfinE.getFechaDB()+"'"+
            (cli_codiE.isNull()?"":" and cli_codi = "+cli_codiE.getValorInt())+
            " order by avs_fecha";
        if (! dtCon1.select(s))
        {
            msgBox("Ningun albaran deposito tiene mercancia servida con estos criterios");
            return;
        }
        jtCab.removeAllDatos();
        
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
        
    }
     void verInventario() throws Exception
    {
        
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
        fechaE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel10 = new gnu.chu.controles.CLabel();
        cli_codiE = new gnu.chu.camposdb.cliPanel();
        cLabel1 = new gnu.chu.controles.CLabel();
        tipoConsC = new gnu.chu.controles.CComboBox();
        Baceptar = new gnu.chu.controles.CButton(Iconos.getImageIcon("check"));
        cLabel7 = new gnu.chu.controles.CLabel();
        fecfinE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        jtCab = new gnu.chu.controles.Cgrid(9);
        jtLin = new gnu.chu.controles.Cgrid(5);
        PPie = new gnu.chu.controles.CPanel();

        Pprinc.setLayout(new java.awt.GridBagLayout());

        PCabe.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        PCabe.setMaximumSize(new java.awt.Dimension(549, 51));
        PCabe.setMinimumSize(new java.awt.Dimension(549, 51));
        PCabe.setPreferredSize(new java.awt.Dimension(549, 51));
        PCabe.setLayout(null);

        cLabel6.setText("De Fecha");
        PCabe.add(cLabel6);
        cLabel6.setBounds(0, 2, 60, 18);
        PCabe.add(fechaE);
        fechaE.setBounds(70, 2, 76, 18);

        cLabel10.setText("De Cliente");
        PCabe.add(cLabel10);
        cLabel10.setBounds(5, 26, 65, 18);
        PCabe.add(cli_codiE);
        cli_codiE.setBounds(70, 26, 360, 18);

        cLabel1.setText("Tipo");
        PCabe.add(cLabel1);
        cLabel1.setBounds(310, 2, 40, 17);

        tipoConsC.addItem("Salidas","S");
        tipoConsC.addItem("Entradas","E");
        tipoConsC.addItem("Inventario","I");
        //tipoConsC.addItem("Movimientos","M");
        PCabe.add(tipoConsC);
        tipoConsC.setBounds(360, 2, 120, 17);

        Baceptar.setText("Aceptar");
        PCabe.add(Baceptar);
        Baceptar.setBounds(440, 22, 100, 24);

        cLabel7.setText("A Fecha");
        PCabe.add(cLabel7);
        cLabel7.setBounds(160, 2, 50, 17);
        PCabe.add(fecfinE);
        fecfinE.setBounds(210, 2, 76, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        Pprinc.add(PCabe, gridBagConstraints);

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
        jtCab.setPreferredSize(new java.awt.Dimension(100, 200));
        jtCab.setAnchoColumna(new int[]{55,20,30,20,40,40,200,45,55});
        jtCab.setAlinearColumna(new int[]{1,2,2,1,2,2,0,2,1});
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Pprinc.add(jtCab, gridBagConstraints);

        ArrayList v=new ArrayList();

        v.add("Articulo"); // 0
        v.add("Descrip."); // 1
        v.add("Individuo"); // 2
        v.add("Unid"); // 3
        v.add("Kilos"); // 4
        jtLin.setCabecera(v);
        jtLin.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtLin.setMaximumSize(new java.awt.Dimension(100, 100));
        jtLin.setMinimumSize(new java.awt.Dimension(100, 100));
        jtLin.setPreferredSize(new java.awt.Dimension(100, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Pprinc.add(jtLin, gridBagConstraints);

        PPie.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        PPie.setMaximumSize(new java.awt.Dimension(500, 30));
        PPie.setMinimumSize(new java.awt.Dimension(500, 30));
        PPie.setPreferredSize(new java.awt.Dimension(500, 31));
        PPie.setLayout(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        Pprinc.add(PPie, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CPanel PCabe;
    private gnu.chu.controles.CPanel PPie;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel10;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.camposdb.cliPanel cli_codiE;
    private gnu.chu.controles.CTextField fecfinE;
    private gnu.chu.controles.CTextField fechaE;
    private gnu.chu.controles.Cgrid jtCab;
    private gnu.chu.controles.Cgrid jtLin;
    private gnu.chu.controles.CComboBox tipoConsC;
    // End of variables declaration//GEN-END:variables
}
