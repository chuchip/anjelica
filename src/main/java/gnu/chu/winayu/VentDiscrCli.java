package gnu.chu.winayu;
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
        setVisibleCabeceraVentana(false);
      
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
        pdconfig.llenaDiscr(dt, rut_codiE, pdconfig.D_RUTAS, EU.em_cod);
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
       return (rut_codiE.isNull()?"":" and rut_codi = '"+rut_codiE.getText()+"'")+    
              (discrim1E.isNull()?"":" and "+(aliasCliente==null?"":aliasCliente+".")+"cli_disc1 like '%"+discrim1E.getText()+"%'")+
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
        discrim1E = new gnu.chu.controles.CLinkBox();
        discrim2E = new gnu.chu.controles.CLinkBox();
        discrim3E = new gnu.chu.controles.CLinkBox();
        discrim4E = new gnu.chu.controles.CLinkBox();
        discrim5L = new gnu.chu.controles.CLabel();
        discrim5E = new gnu.chu.controles.CLinkBox();
        discrim6L = new gnu.chu.controles.CLabel();
        discrim6E = new gnu.chu.controles.CLinkBox();
        bAceptar = new gnu.chu.controles.CButton("Aceptar (F4)",Iconos.getImageIcon("check"));
        discrim4L = new gnu.chu.controles.CLabel();
        discrim6L1 = new gnu.chu.controles.CLabel();
        rut_codiE = new gnu.chu.controles.CLinkBox();

        Pprinc.setDefButton(bAceptar);
        Pprinc.setLayout(null);

        discrim1L.setText("Discrim 1");
        Pprinc.add(discrim1L);
        discrim1L.setBounds(10, 5, 91, 17);

        discrim2L.setText("Discrim 2");
        Pprinc.add(discrim2L);
        discrim2L.setBounds(10, 25, 91, 17);

        discrim3L.setText("Discrim 3");
        Pprinc.add(discrim3L);
        discrim3L.setBounds(10, 45, 91, 17);

        discrim1E.setFormato(Types.CHAR,"XX");
        discrim1E.setMayusculas(true);
        discrim1E.setAncTexto(30);
        Pprinc.add(discrim1E);
        discrim1E.setBounds(110, 5, 350, 18);

        discrim1E.setFormato(Types.CHAR,"XX");
        discrim1E.setMayusculas(true);
        discrim2E.setAncTexto(30);
        Pprinc.add(discrim2E);
        discrim2E.setBounds(110, 25, 350, 18);

        discrim1E.setFormato(Types.CHAR,"XX");
        discrim1E.setMayusculas(true);
        discrim3E.setAncTexto(30);
        Pprinc.add(discrim3E);
        discrim3E.setBounds(110, 45, 350, 18);

        discrim1E.setFormato(Types.CHAR,"XX");
        discrim1E.setMayusculas(true);
        discrim4E.setAncTexto(30);
        Pprinc.add(discrim4E);
        discrim4E.setBounds(110, 65, 350, 18);

        discrim5L.setText("Zona/Repr.");
        Pprinc.add(discrim5L);
        discrim5L.setBounds(10, 85, 91, 17);

        discrim1E.setFormato(Types.CHAR,"XX");
        discrim1E.setMayusculas(true);
        discrim5E.setAncTexto(30);
        Pprinc.add(discrim5E);
        discrim5E.setBounds(110, 85, 350, 18);

        discrim6L.setText("Ruta");
        Pprinc.add(discrim6L);
        discrim6L.setBounds(10, 125, 91, 17);

        discrim1E.setFormato(Types.CHAR,"XX");
        discrim1E.setMayusculas(true);
        discrim6E.setAncTexto(30);
        Pprinc.add(discrim6E);
        discrim6E.setBounds(110, 105, 350, 18);
        Pprinc.add(bAceptar);
        bAceptar.setBounds(190, 150, 119, 33);

        discrim4L.setText("Discrim 4");
        Pprinc.add(discrim4L);
        discrim4L.setBounds(10, 70, 91, 17);

        discrim6L1.setText("Zona/Credito");
        Pprinc.add(discrim6L1);
        discrim6L1.setBounds(10, 105, 91, 17);

        rut_codiE.setAncTexto(30);
        rut_codiE.setMayusculas(true);
        rut_codiE.setPreferredSize(new java.awt.Dimension(152, 18));
        Pprinc.add(rut_codiE);
        rut_codiE.setBounds(110, 125, 350, 18);

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
    private gnu.chu.controles.CLabel discrim6L1;
    private gnu.chu.controles.CLinkBox rut_codiE;
    // End of variables declaration//GEN-END:variables

}
