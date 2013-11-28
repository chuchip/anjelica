/**
 *
 * <p>Titulo: VentDiscrCli</p>
 * <p>Descripcion: Ventana que saca los discriminadores,  de
 * los clientes. Creando la sentencia SQL para poder buscar los cliennes
 * por esos conceptos.
 * Es llamada desde el control gnu.chu.camposdb.DiscButon
 * </p>
 * <p>Copyright: Copyright (c) 2005-2010
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
 * <p>Empresa: miSL</p>
 * @author chuchi P
 */

package gnu.chu.winayu;

import com.jgoodies.looks.plastic.PlasticInternalFrameUI;
import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.controles.CInternalFrame;
import gnu.chu.controles.StatusBar;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.ventana;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.Types;
import javax.swing.plaf.basic.BasicInternalFrameUI;

public class VentDiscrCli extends ventana {
    DatosTabla dt;
    private boolean aceptado=false;
    CInternalFrame papa;
    public boolean isAceptado() {
        return aceptado;
    }

    public void setAceptado(boolean aceptado) {
        this.aceptado = aceptado;
    }

    /** Creates new form VentDiscrCli */
    public VentDiscrCli() {
        this.setTitle("Discriminadores Clientes");

        this.setVersion("2010-12-16");
        statusBar = new StatusBar(this);

        initComponents();
        this.setSize(500, 302);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        BasicInternalFrameUI miUi = (BasicInternalFrameUI)this.getUI();
        this.putClientProperty(
             PlasticInternalFrameUI.IS_PALETTE,
             Boolean.TRUE);
//        miUi.setNorthPane(null); // Para quitar el titulo a la ventana
    }

    public void iniciar(DatosTabla dt,EntornoUsuario EU,CInternalFrame papa) throws SQLException
    {
        this.dt=dt;
        this.EU=EU;
        this.papa=papa;
        discrim1L.setText(pdconfig.getNombreDiscr(EU.em_cod,"C1",dt));
        discrim2L.setText(pdconfig.getNombreDiscr(EU.em_cod,"C2",dt));
        discrim3L.setText(pdconfig.getNombreDiscr(EU.em_cod,"C3",dt));
        discrim4L.setText(pdconfig.getNombreDiscr(EU.em_cod,"C4",dt));

        pdconfig.llenaDiscr(dt, discrim1E, "C1", EU.em_cod);
        pdconfig.llenaDiscr(dt, discrim2E, "C2", EU.em_cod);
        pdconfig.llenaDiscr(dt, discrim3E, "C3", EU.em_cod);
        pdconfig.llenaDiscr(dt, discrim4E, "C4", EU.em_cod);
        pdconfig.llenaDiscr(dt, discrim5E, "CR", EU.em_cod);
        pdconfig.llenaDiscr(dt, discrim6E, "CC", EU.em_cod);
        activarEventos();
    }
    public void resetTexto()
    {
        Pprinc.resetTexto();
    }
    @Override
    public void requestFocus()
    {
        if (discrim1E==null)
            this.requestFocus();
        discrim1E.requestFocus();
    }
/**
 * Devuelve las condiciones WHERE (siempre empiezan con un 'AND'
 * @param aliasCliente
 * @return
 */
    public String getCondWhere(String aliasCliente)
    {
         if (aliasCliente!=null)
           if (aliasCliente.trim().equals(""))
            aliasCliente=null;
       return (discrim1E.isNull()?"":" and "+(aliasCliente==null?"":aliasCliente+".")+"cli_disc1 like '%"+discrim1E.getText()+"%'")+
              (discrim2E.isNull()?"":" and "+(aliasCliente==null?"":aliasCliente+".")+"cli_disc2 like '%"+discrim2E.getText()+"%'")+
              (discrim3E.isNull()?"":" and "+(aliasCliente==null?"":aliasCliente+".")+"cli_disc3 like '%"+discrim3E.getText()+"%'")+
              (discrim4E.isNull()?"":" and "+(aliasCliente==null?"":aliasCliente+".")+"cli_disc4 like '%"+discrim4E.getText()+"%'")+
              (discrim5E.isNull()?"":" and "+(aliasCliente==null?"":aliasCliente+".")+"cli_zonrep like '%"+discrim5E.getText()+"%'")+
              (discrim6E.isNull()?"":" and "+(aliasCliente==null?"":aliasCliente+".")+"cli_zoncre like '%"+discrim6E.getText()+"%'");
    }
    void activarEventos()
    {
      bAceptar.addActionListener(new ActionListener()
      {
            public void actionPerformed(ActionEvent e) {
                aceptado=true;
                matar();
            }
        });
    }
  
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Pprinc = new gnu.chu.controles.CPanel();
        discrim1L = new gnu.chu.controles.CLabel();
        discrim2L = new gnu.chu.controles.CLabel();
        discrim3L = new gnu.chu.controles.CLabel();
        discrim4L = new gnu.chu.controles.CLabel();
        discrim1E = new gnu.chu.controles.CLinkBox();
        discrim2E = new gnu.chu.controles.CLinkBox();
        discrim3E = new gnu.chu.controles.CLinkBox();
        discrim4E = new gnu.chu.controles.CLinkBox();
        discrim5L = new gnu.chu.controles.CLabel();
        discrim5E = new gnu.chu.controles.CLinkBox();
        discrim6L = new gnu.chu.controles.CLabel();
        discrim6E = new gnu.chu.controles.CLinkBox();
        bAceptar = new gnu.chu.controles.CButton("Aceptar (F4)",Iconos.getImageIcon("check"));

        Pprinc.setDefButton(bAceptar);

        discrim1L.setText("Discrim 1");

        discrim2L.setText("Discrim 2");

        discrim3L.setText("Discrim 3");

        discrim4L.setText("Discrim 4");

        discrim1E.setFormato(Types.CHAR,"XX");
        discrim1E.setMayusculas(true);
        discrim1E.setAncTexto(30);

        discrim1E.setFormato(Types.CHAR,"XX");
        discrim1E.setMayusculas(true);
        discrim2E.setAncTexto(30);

        discrim1E.setFormato(Types.CHAR,"XX");
        discrim1E.setMayusculas(true);
        discrim3E.setAncTexto(30);

        discrim1E.setFormato(Types.CHAR,"XX");
        discrim1E.setMayusculas(true);
        discrim4E.setAncTexto(30);

        discrim5L.setText("Zona/Repr.");

        discrim1E.setFormato(Types.CHAR,"XX");
        discrim1E.setMayusculas(true);
        discrim5E.setAncTexto(30);

        discrim6L.setText("Zona/Credito");

        discrim1E.setFormato(Types.CHAR,"XX");
        discrim1E.setMayusculas(true);
        discrim6E.setAncTexto(30);

        org.jdesktop.layout.GroupLayout PprincLayout = new org.jdesktop.layout.GroupLayout(Pprinc);
        Pprinc.setLayout(PprincLayout);
        PprincLayout.setHorizontalGroup(
            PprincLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(PprincLayout.createSequentialGroup()
                .add(PprincLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(PprincLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(PprincLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(PprincLayout.createSequentialGroup()
                                .add(discrim1L, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 91, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(discrim1E, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE))
                            .add(PprincLayout.createSequentialGroup()
                                .add(discrim2L, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 91, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(discrim2E, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE))
                            .add(PprincLayout.createSequentialGroup()
                                .add(discrim3L, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 91, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(discrim3E, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE))
                            .add(PprincLayout.createSequentialGroup()
                                .add(discrim4L, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 91, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(discrim4E, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE))
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, PprincLayout.createSequentialGroup()
                                .add(discrim5L, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 91, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(discrim5E, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE))
                            .add(PprincLayout.createSequentialGroup()
                                .add(discrim6L, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 91, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(discrim6E, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE))))
                    .add(PprincLayout.createSequentialGroup()
                        .add(188, 188, 188)
                        .add(bAceptar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 119, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        PprincLayout.setVerticalGroup(
            PprincLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(PprincLayout.createSequentialGroup()
                .addContainerGap()
                .add(PprincLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(discrim1L, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(discrim1E, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(PprincLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(discrim2L, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(discrim2E, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(PprincLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(discrim3L, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(discrim3E, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(PprincLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(discrim4E, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(discrim4L, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(PprincLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(discrim5E, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(discrim5L, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(PprincLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(discrim6E, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(discrim6L, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(bAceptar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 33, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(108, Short.MAX_VALUE))
        );

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CButton bAceptar;
    private gnu.chu.controles.CLinkBox discrim1E;
    private gnu.chu.controles.CLabel discrim1L;
    private gnu.chu.controles.CLinkBox discrim2E;
    private gnu.chu.controles.CLabel discrim2L;
    private gnu.chu.controles.CLinkBox discrim3E;
    private gnu.chu.controles.CLabel discrim3L;
    private gnu.chu.controles.CLinkBox discrim4E;
    private gnu.chu.controles.CLabel discrim4L;
    private gnu.chu.controles.CLinkBox discrim5E;
    private gnu.chu.controles.CLabel discrim5L;
    private gnu.chu.controles.CLinkBox discrim6E;
    private gnu.chu.controles.CLabel discrim6L;
    // End of variables declaration//GEN-END:variables

}
