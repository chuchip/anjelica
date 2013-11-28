/**
 *
 * <p>Titulo: DiscProPanel  </p>
 * <p>Descripción: Panel Discriminadores de productos </p>
 * <p>Copyright: Copyright (c) 2005-2011
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los terminos de la Licencia P�blica General de GNU según es publicada por
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
 * @author chuchiP
 * Created on 29-dic-2010, 10:11:29
 *
 */


package gnu.chu.camposdb;

import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.controles.CPanel;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import java.sql.SQLException;
import java.sql.Types;

public class DiscProPanel extends CPanel {
   DatosTabla dt;
   EntornoUsuario EU;
    /** Creates new form DiscProPanel */
    public DiscProPanel() {
        initComponents();
    }
  public void iniciar(DatosTabla dt,EntornoUsuario EU) throws SQLException
    {
        this.dt=dt;
        this.EU=EU;

        discrim1L.setText(pdconfig.getNombreDiscr(EU.em_cod,"A1",dt));
        discrim2L.setText(pdconfig.getNombreDiscr(EU.em_cod,"A2",dt));
        discrim3L.setText(pdconfig.getNombreDiscr(EU.em_cod,"A3",dt));
        discrim4L.setText(pdconfig.getNombreDiscr(EU.em_cod,"A4",dt));
        
        pdconfig.llenaDiscr(dt, discrim1E, "A1", EU.em_cod);
        pdconfig.llenaDiscr(dt, discrim2E, "A2", EU.em_cod);
        pdconfig.llenaDiscr(dt, discrim3E, "A3", EU.em_cod);
        pdconfig.llenaDiscr(dt, discrim4E, "A4", EU.em_cod);
      
        activarEventos();
    }
    @Override
   public void requestFocus()
   {
         if (discrim1E==null)
            this.requestFocus();
        discrim1E.requestFocus();
   }
   void  activarEventos()
   {
  
   }
    public void limpiar()
    {
        discrim1E.resetTexto();
        discrim2E.resetTexto();
        discrim3E.resetTexto();
        discrim4E.resetTexto();
    }
    @Override
    public void setQuery(boolean b)
    {
        discrim1E.setQuery(b);
         discrim2E.setQuery(b);
        discrim3E.setQuery(b);
        discrim4E.setQuery(b);
    }
    public void setText(DatosTabla dt) throws SQLException {
        discrim1E.setText(dt.getString("pro_disc1"));
        discrim2E.setText(dt.getString("pro_disc2"));
        discrim3E.setText(dt.getString("pro_disc3"));
        discrim4E.setText(dt.getString("pro_disc4"));
        discrim4E.setText(dt.getString("pro_disc4"));
    }
    /**
     * Devuelve las condiciones para buscar los productos por estos alis
     * @param aliasProducto Alias de la tabla productos en la sentencia query a
     * completar. NULL o vacio si no existe.
     * @return condiciones del WHERE
     */
   public String getCondWhere(String aliasProducto)
    {
       if (aliasProducto!=null)
           if (aliasProducto.trim().equals(""))
            aliasProducto=null;
       return (discrim1E.isNull()?"":" and "+(aliasProducto==null?"":aliasProducto+".")+"pro_disc1 like '%"+discrim1E.getText()+"%'")+
              (discrim2E.isNull()?"":" and "+(aliasProducto==null?"":aliasProducto+".")+"pro_disc2 like '%"+discrim2E.getText()+"%'")+
              (discrim3E.isNull()?"":" and "+(aliasProducto==null?"":aliasProducto+".")+"pro_disc3 like '%"+discrim3E.getText()+"%'")+
              (discrim4E.isNull()?"":" and "+(aliasProducto==null?"":aliasProducto+".")+"pro_disc4 like '%"+discrim4E.getText()+"%'");
    }
   public String controla()
   {
       return controla(true);
   }
   /**
    * Controla si los valores metidos en los discriminadores son validos
    * @param reqFocus Realizar un requestFocus en el caso de no ser valido
    * @return String con el mensaje de error. NULL si no hay error.
    */
   public String controla(boolean reqFocus)
    {
     if (!discrim1E.controla(reqFocus))
      return "Primer Discriminador  NO es valido";
     if (!discrim2E.controla(reqFocus))
      return "Segundo Discriminador  NO es valido";
     if (!discrim3E.controla(reqFocus))
      return "Tercer Discriminador  NO es valido";
     if (!discrim4E.controla(reqFocus))
      return "Cuarto Discriminador  NO es valido";
     return null;
   }
   public void setDato(DatosTabla dt) throws SQLException
   {
    dt.setDato("pro_disc1", discrim1E.getText());
    dt.setDato("pro_disc2", discrim2E.getText());
    dt.setDato("pro_disc3", discrim3E.getText());
    dt.setDato("pro_disc4", discrim4E.getText());
   }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        discrim1L = new gnu.chu.controles.CLabel();
        discrim2L = new gnu.chu.controles.CLabel();
        discrim3L = new gnu.chu.controles.CLabel();
        discrim4L = new gnu.chu.controles.CLabel();
        discrim1E = new gnu.chu.controles.CLinkBox();
        discrim2E = new gnu.chu.controles.CLinkBox();
        discrim3E = new gnu.chu.controles.CLinkBox();
        discrim4E = new gnu.chu.controles.CLinkBox();

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(discrim1L, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(discrim1E, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(discrim2L, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(discrim2E, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(discrim3L, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(discrim3E, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(discrim4L, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(discrim4E, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(discrim1L, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(discrim1E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(discrim2L, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(discrim2E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(discrim3L, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(discrim3E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(discrim4E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(discrim4L, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CLinkBox discrim1E;
    private gnu.chu.controles.CLabel discrim1L;
    private gnu.chu.controles.CLinkBox discrim2E;
    private gnu.chu.controles.CLabel discrim2L;
    private gnu.chu.controles.CLinkBox discrim3E;
    private gnu.chu.controles.CLabel discrim3L;
    private gnu.chu.controles.CLinkBox discrim4E;
    private gnu.chu.controles.CLabel discrim4L;
    // End of variables declaration//GEN-END:variables

}
