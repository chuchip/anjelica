package gnu.chu.anjelica.ventas.represen;
/**
 *
 * <p>Título: PRelClien</p>
 * <p>Descripción: Panel de relacion de clientes </p>
 * <p`>Usado por clase FichaClientes.
* <p>Copyright: Copyright (c) 2005-2015
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
 * @author chuchiP
 * <p>Empresa: MISL</p>
 *
 * Created on 23-jul-2010, 8:33:54
 */
import gnu.chu.anjelica.pad.pdclien;
import gnu.chu.anjelica.pad.pdtipotar;
import gnu.chu.controles.CPanel;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author jpuente
 */
public class PRelClien extends CPanel {
    static final  int JT_FECCON=5;
    DatosTabla dtStat;
    EntornoUsuario EU;
    FichaClientes padre;
    private Thread th ;
    /** Creates new form PRelClien */
    public PRelClien() {
        initComponents();
    }
    public void iniciar(FichaClientes papi,DatosTabla dt, EntornoUsuario entUsu) throws SQLException
    {
        padre=papi;
        dtStat=dt;
        this.EU=entUsu;
        pdclien.llenaEstCont(cli_estconE);
       
        pdtipotar.llenaLinkBox(tar_codiE, dt);
    }

    void actualCampos()
    {
        
        try {
          if (ncliSelE.getValorInt()!=0)          
              getDatosCliente(padre.dtCli);
          
          tar_codiE.setText(padre.dtCli.getString("tar_codi"));
          cli_telconE.setText(padre.dtCli.getString("cli_telcon"));
          cli_telefE.setText(padre.dtCli.getString("cli_telef"));
          cli_perconE.setText(padre.dtCli.getString("cli_percon"));
          String s=" SELECT * FROM compedven WHERE cli_codi = "+getCliente()+
             " ORDER BY cpv_fecha desc";
          if (! dtStat.select(s))
              cli_comenE.resetTexto();
          else
              cli_comenE.setText(dtStat.getString("cpv_come"));
        } catch (SQLException k)
        {
            padre.Error("Error al buscar cliente", k);
            return;
        }
        if (ncliSelE.getValorInt()==0)
           return;
        padre.setCabecera(jt.getValString(1),jt.getValString(2));
 //       padre.setRelacionClientes(false);
  //     th =new consClienteTH(padre);  
//        padre.verDatClien(padre.dtCli);
    }
    void actualEstado(int row,String estcon)
    {
        String s="UPDATE clientes set cli_estcon = '"+estcon+"'"+
                (estcon.equals(""+pdclien.EST_CONTACT)?", cli_feulco= current_date ":"")+
                " where cli_codi = "+jt.getValorInt(row,0);
        try {
            padre.dtBlo.executeUpdate(s);
            padre.dtBlo.commit();
        } catch (SQLException k ) {
            padre.Error("Error al actualizar estado de cliente",k);
        }
        if (estcon.equals("N"))
            ncliPendE.setValorInt(ncliPendE.getValorInt()+1);
        else
            ncliPendE.setValorInt(ncliPendE.getValorInt()-1);
        
        if (estcon.equals(""+pdclien.EST_CONTACT))
            jt.setValor( Formatear.getFechaAct("dd-MM-yyyy"),row,JT_FECCON);
        padre.mensajeRapido("Estado de cliente actualizado");
    }
    /**
     * 
     * @return codigo de cliente activo en el grid
     */
    public int getCliente()
    {
        return jt.getValorInt(0);
    }
    public boolean getDatosCliente(DatosTabla dt) throws SQLException
    { 
         String s="select c.*,d.zon_nomb as dis_nomb from clientes as c "+
                    " left join v_zonas as d on "+
                     "  dis_codi = c.zon_codi "+
                     " where cli_codi = "+getCliente();
         
         return dt.select(s);
    }
    public void resetTexo()
    {
        jt.setEnabled(false);
        jt.removeAllDatos();
        Ppie.resetTexto();
    }
    /**
     * Llena grid con los datos de los clientes selecionados
     *
     * @param sql setencia select sobre la tabla clientess y
     * que incluye el nombre de la zona
     * @param dt DatosTabla sobre la que hacer la select
     * @throws SQLException
     */
    public void llenaGrid(String sql,DatosTabla dt) throws SQLException
    {
        jt.setEnabled(false);
        jt.removeAllDatos();
        ncliSelE.setValorInt(0);
        if (!dt.select(sql))
            return;
        int nCli=0;
        int cliPend=0;
        do
        {
            nCli++;
            if (dt.getString("cli_estcon").equals("N"))
                cliPend++;
             if (padre.mataConsulta)
                return;
            ArrayList v=new ArrayList();
            v.add(dt.getInt("cli_codi"));
            v.add(dt.getString("cli_nomb"));
            v.add(dt.getString("cli_pobl"));
            v.add(dt.getString("dis_nomb"));
            v.add(dt.getDate("cli_feulve"));
            v.add(dt.getDate("cli_feulco"));
            v.add(dt.getString("cli_estcon"));
            jt.addLinea(v);
        } while (dt.next());
        jt.setEnabled(true);
        jt.requestFocusInicioLater();
        actualCampos();
        ncliSelE.setValorInt(nCli);
        ncliPendE.setValorInt(cliPend);
        cli_estconE.resetTexto();
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

        cli_codiE = new gnu.chu.controles.CTextField(Types.DECIMAL,"####9");
        cli_nombE = new gnu.chu.controles.CTextField();
        cli_poblE = new gnu.chu.controles.CTextField();
        zon_nombE = new gnu.chu.controles.CTextField();
        feulveE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cli_estconE = new gnu.chu.controles.CTextField();
        feulcoE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        jt = new gnu.chu.controles.CGridEditable(7){
            public void afterCambiaLinea()
            {
                actualCampos();
            }
            public int cambiaLinea(int row, int col)
            {
                if (! cli_estconE.hasCambio())
                return -1;
                actualEstado(row,cli_estconE.getText());
                return -1;
            }
        };
        Vector v= new Vector();
        v.add("Cliente"); // 0
        v.add("Nombre"); // 1
        v.add("Poblac."); // 2
        v.add("Zona"); // 3
        v.add("Ult.Venta"); // 4
        v.add("Ult.Cont."); // 5
        v.add("Estado"); // 6
        jt.setCabecera(v);
        jt.setFormatoColumna(4,"dd-MM-yyyy");
        jt.setFormatoColumna(5,"dd-MM-yyyy");
        jt.setAnchoColumna(new int[]{40,180,120,90,80,80,70});
        jt.setAlinearColumna(new int[]{2,0,0,0,1,1,0});
        Vector vc=new Vector();
        vc.add(cli_codiE);
        vc.add(cli_nombE);
        vc.add(cli_poblE);
        vc.add(zon_nombE);
        vc.add(feulveE);
        vc.add(feulcoE);
        vc.add(cli_estconE);
        try {
            jt.setCampos(vc); } catch (Exception k11){}
        jt.setCanDeleteLinea(false);
        jt.setCanInsertLinea(false);
        Ppie = new gnu.chu.controles.CPanel();
        cLabel1 = new gnu.chu.controles.CLabel();
        cli_telconE = new gnu.chu.controles.CTextField();
        cLabel2 = new gnu.chu.controles.CLabel();
        cli_perconE = new gnu.chu.controles.CTextField();
        cLabel3 = new gnu.chu.controles.CLabel();
        cli_telefE = new gnu.chu.controles.CTextField();
        cLabel4 = new gnu.chu.controles.CLabel();
        tar_codiE = new gnu.chu.controles.CLinkBox();
        cLabel5 = new gnu.chu.controles.CLabel();
        ncliPendE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        bReset = new gnu.chu.controles.CButton();
        bPedido = new gnu.chu.controles.CButton();
        cLabel6 = new gnu.chu.controles.CLabel();
        ncliSelE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        cli_comenE = new gnu.chu.controles.CTextField();

        cli_codiE.setEnabled(false);

        cli_nombE.setText("cTextField1");
        cli_nombE.setEnabled(false);

        cli_poblE.setText("cTextField1");
        cli_poblE.setEnabled(false);

        zon_nombE.setText("cTextField1");
        zon_nombE.setEnabled(false);

        feulveE.setText("cTextField1");
        feulveE.setEnabled(false);

        feulcoE.setEnabled(false);

        setLayout(new java.awt.GridBagLayout());

        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jt.setPreferredSize(new java.awt.Dimension(100, 100));

        org.jdesktop.layout.GroupLayout jtLayout = new org.jdesktop.layout.GroupLayout(jt);
        jt.setLayout(jtLayout);
        jtLayout.setHorizontalGroup(
            jtLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 661, Short.MAX_VALUE)
        );
        jtLayout.setVerticalGroup(
            jtLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 355, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(jt, gridBagConstraints);

        Ppie.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Ppie.setMaximumSize(new java.awt.Dimension(50, 84));
        Ppie.setMinimumSize(new java.awt.Dimension(50, 84));
        Ppie.setPreferredSize(new java.awt.Dimension(50, 84));
        Ppie.setLayout(null);

        cLabel1.setText("Tel. Contacto ");
        Ppie.add(cLabel1);
        cLabel1.setBounds(320, 4, 75, 15);

        cli_telconE.setEditable(false);
        Ppie.add(cli_telconE);
        cli_telconE.setBounds(399, 3, 122, 17);

        cLabel2.setText("Persona Contacto ");
        Ppie.add(cLabel2);
        cLabel2.setBounds(3, 4, 101, 15);

        cli_perconE.setEditable(false);
        Ppie.add(cli_perconE);
        cli_perconE.setBounds(108, 3, 208, 17);

        cLabel3.setText("Telefono");
        Ppie.add(cLabel3);
        cLabel3.setBounds(3, 29, 49, 15);

        cli_telefE.setEditable(false);
        Ppie.add(cli_telefE);
        cli_telefE.setBounds(56, 28, 130, 17);

        cLabel4.setText("Tarifa");
        Ppie.add(cLabel4);
        cLabel4.setBounds(204, 29, 31, 15);

        tar_codiE.setAncTexto(30);
        tar_codiE.setEnabled(false);
        Ppie.add(tar_codiE);
        tar_codiE.setBounds(239, 28, 300, 17);

        cLabel5.setText("N.Clientes Pend");
        Ppie.add(cLabel5);
        cLabel5.setBounds(190, 50, 87, 17);

        ncliPendE.setEditable(false);
        Ppie.add(ncliPendE);
        ncliPendE.setBounds(280, 50, 38, 17);

        bReset.setText("Resetear Todo");
        bReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bResetActionPerformed(evt);
            }
        });
        Ppie.add(bReset);
        bReset.setBounds(550, 30, 104, 18);

        bPedido.setText("Pedido");
        Ppie.add(bPedido);
        bPedido.setBounds(590, 2, 65, 19);

        cLabel6.setText("N.Clientes Selecionados");
        Ppie.add(cLabel6);
        cLabel6.setBounds(3, 50, 133, 17);

        ncliSelE.setEditable(false);
        Ppie.add(ncliSelE);
        ncliSelE.setBounds(140, 50, 38, 17);

        cli_comenE.setEditable(false);
        Ppie.add(cli_comenE);
        cli_comenE.setBounds(340, 50, 310, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 2, 5);
        add(Ppie, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void bResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bResetActionPerformed
        // TODO add your handling code here:
        int nRow=jt.getRowCount();
        if (nRow==0 ||  ! jt.isEnabled())
            return;
        for (int n=0;n<nRow;n++)
        {
            actualEstado(n, "N");
            jt.setValor("N",n,6);
        }
        cli_estconE.setText("N");
        jt.requestFocusInicio();
        ncliPendE.setValorInt(ncliSelE.getValorInt());
    }//GEN-LAST:event_bResetActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CPanel Ppie;
    private gnu.chu.controles.CButton bPedido;
    private gnu.chu.controles.CButton bReset;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CTextField cli_codiE;
    private gnu.chu.controles.CTextField cli_comenE;
    private gnu.chu.controles.CTextField cli_estconE;
    private gnu.chu.controles.CTextField cli_nombE;
    private gnu.chu.controles.CTextField cli_perconE;
    private gnu.chu.controles.CTextField cli_poblE;
    private gnu.chu.controles.CTextField cli_telconE;
    private gnu.chu.controles.CTextField cli_telefE;
    private gnu.chu.controles.CTextField feulcoE;
    private gnu.chu.controles.CTextField feulveE;
    private gnu.chu.controles.CGridEditable jt;
    private gnu.chu.controles.CTextField ncliPendE;
    private gnu.chu.controles.CTextField ncliSelE;
    private gnu.chu.controles.CLinkBox tar_codiE;
    private gnu.chu.controles.CTextField zon_nombE;
    // End of variables declaration//GEN-END:variables

}
