/*
 * AvcPanel.java
 *
 * Panel con los campos que define un número de albaran
 * Created on 08-feb-2010, 22:59:20
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
 */
 
package gnu.chu.camposdb;

import gnu.chu.controles.CPanel;
import gnu.chu.controles.CTextField;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.sql.Types;


public class AvcPanel extends CPanel {
    EntornoUsuario EU;
    private boolean isFacturado=false;
    
    public boolean isFacturado()
    {
        return isFacturado;
    }
    public void setFacturado(boolean isFacturado)
    {
        this.isFacturado=isFacturado;
    }
    
    public CTextField getAvc_serieE() {
        return avc_serieE;
    }
    /** Creates new form AvcPanel */
    public AvcPanel() {
        initComponents();
    }
    public void iniciar(EntornoUsuario eu)
    {
       this.EU=eu;
       avc_anoE.setValorDec(eu.ejercicio);
       avc_serieE.setText("A");
    }
    @Override
    public void resetTexto()
    {
       avc_anoE.setValorDec(EU.ejercicio);
       avc_serieE.setText("A");
       avc_numeE.resetTexto();
    }
    @Override
    public void requestFocus()
    {
        if (avc_numeE!=null)
            avc_numeE.requestFocus();
    }
    public boolean isNull()
    {
        return avc_anoE.getValorInt()==0 || avc_serieE.isNull() || avc_numeE.getValorInt()==0;
    }
    /**
     * Devuelve condiciones para buscar sobre los campos v_albavec o v_albavel
     * @param tabla Nombre de tabla en where (si es null se ignora)
     * @param incAnd Incluir AND inicial
     * @return sentencia SQL 
     */
    public String getCondWhere(String tabla,boolean incAnd)
    {
        if (isNull())
            return "";
        return (incAnd?" and ":"")+
             (tabla==null?"":tabla+".")+"avc_nume = "+avc_numeE.getValorInt()+
             " and "+ (tabla==null?"":tabla+".")+"avc_serie='"+avc_serieE.getText()+"'"+
             " and "+ (tabla==null?"":tabla+".")+"avc_ano ="+avc_anoE.getValorInt();
    }
    @Override
    public void setText(String albaran) {
        avc_anoE.setText(albaran.substring(0,4));
        avc_serieE.setText(albaran.substring(4,5));
        avc_numeE.setText(""+Integer.parseInt(albaran.substring(5)));
    }
    
    @Override
    public String getText()
    {
        return avc_anoE.getText()+avc_serieE.getText()+
            avc_numeE.getText();
    }
    public int geValorIntAno()
    {
        return avc_anoE.getValorInt();
    }
    public String getTextSerie()
    {
        return avc_serieE.getText();
    }
     public int geValorIntNume()
    {
        return avc_numeE.getValorInt();
    }
    public  boolean selCabAlb(DatosTabla dt,int empCodi,boolean block,boolean excepNotFound) throws SQLException
    {
         return selCabAlb("v_albavec",dt,avc_anoE.getValorInt(),empCodi,avc_serieE.getText(),avc_numeE.getValorInt(),block, excepNotFound);
    }
    public  boolean selCabAlb(String tablaCab,DatosTabla dt,int empCodi,boolean block,boolean excepNotFound) throws SQLException
    {
         return selCabAlb(tablaCab,dt,avc_anoE.getValorInt(),empCodi,avc_serieE.getText(),avc_numeE.getValorInt(),block, excepNotFound);
    }
    /**
     * Devuelve la cabecera del albaran o hist. Albaran
     * @param tablaCab
     * @param dt
     * @param avcAno
     * @param empCodi
     * @param avcSerie
     * @param avcNume
     * @param block bloquear?
     * @param excepNotFound lanzar exception si no encuentra el albaran
      * @return true si encuentra el albaran. 
     * @throws SQLException 
     */
    public static boolean selCabAlb(String tablaCab,DatosTabla dt,int avcAno,int empCodi,String avcSerie,int avcNume,boolean block,boolean excepNotFound) throws SQLException
    {
      String sql = "SELECT * FROM "+tablaCab+" WHERE "+
          (empCodi>0?" avc_ano =" + avcAno +
        " and emp_codi = " + empCodi +
        " and avc_serie = '" + avcSerie+ "'" +
        " and avc_nume = " + avcNume:
          "  his_rowid= "+avcNume);
      boolean ret=dt.select(sql, block);
      if (block && ! ret  && excepNotFound)
       throw new SQLException("No encontrado Cabecera Albaran.\n Select: " + sql);
     return ret;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        avc_serieE = new gnu.chu.controles.CTextField(Types.CHAR,"X",1);
        avc_anoE = new gnu.chu.controles.CTextField(Types.DECIMAL,"9999");
        avc_numeE = new gnu.chu.controles.CTextField(Types.DECIMAL,"99999");
        this.setLayout(new GridBagLayout());
        avc_serieE.setPreferredSize(new Dimension(16,18));
        avc_anoE.setPreferredSize(new Dimension(42,18));
        avc_numeE.setPreferredSize(new Dimension(60,18));
        avc_serieE.setMinimumSize(new Dimension(16,18));
        avc_anoE.setMinimumSize(new Dimension(42,18));
        avc_numeE.setMinimumSize(new Dimension(60,18));
        avc_serieE.setMaximumSize(new Dimension(16,18));
        avc_anoE.setMaximumSize(new Dimension(42,18));
        avc_numeE.setMaximumSize(new Dimension(60,18));
        this.add(avc_serieE,new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
        this.add(avc_anoE,new GridBagConstraints(1, 0, 1, 1, 0.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
        this.add(avc_numeE,new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CTextField avc_numeE;
    private gnu.chu.controles.CTextField avc_serieE;
    private gnu.chu.controles.CTextField avc_anoE;
    // End of variables declaration//GEN-END:variables

}
