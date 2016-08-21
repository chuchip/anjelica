package gnu.chu.camposdb;
/**
 *
 * <p>Titulo: DatRutaRep </p>
 * <p>Descripcion: Panel para sacar los datos de una ruta de reparto
 * </p>
 * <p>Copyright: Copyright (c) 2005-2016
 *
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los términos de la Licencia Publica General de GNU según es publicada por
 *  la Free Software Foundation, bien de la versión 2 de dicha Licencia
 *  o bien (según su elección) de cualquier versión posterior.
 *  Este programa se distribuye con la esperanza de que sea útil,
 *  pero SIN NINGUNA GARANTÍA, incluso sin la garantía MERCANTIL implícita
 *  o sin garantizar la CONVENIENCIA PARA UN PROPÓSITO PARTICULAR.
 *  Véase la Licencia Pública General de GNU para más detalles.
 *  Debería haber recibido una copia de la Licencia Pública General junto con este programa.
 *  Si no ha sido así, escriba a la Free Software Foundation, Inc.,
 *  en 675 Mass Ave, Cambridge, MA 02139, EEUU.
 * @author chuchiP
 * @version 1.1
 */
import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.anjelica.ventas.pdalbara;
import gnu.chu.controles.CPanel;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.mensajes;
import gnu.chu.utilidades.ventana;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DatRutaRepPanel extends CPanel
{
    DatosTabla dtStat,dtAdd;
    ventana padre;
    int avc_id;
    
    public DatRutaRepPanel() {
        initComponents();
    }
      
    public void iniciar(DatosTabla dt,DatosTabla dtAdd,ventana papa)
    {
      dtStat=dt;  
      this.dtAdd=dtAdd;
      this.padre=papa;
      BSalir.addActionListener(new ActionListener()
      {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                generarSalidaRuta();
            }
      });
    }
    
    void generarSalidaRuta() {
        try
        {
            String s;
            int alrNume;
            int alrOrden=1;
            if (alr_numeE.getValorInt() == 0)
            {
                s = "select c.*,cl.cli_nomen,cl.cli_diree,cl.cli_codpoe,cl.cli_poble "
                    + "from v_albavec as c,clientes as cl where avc_id=" + avc_id
                    + " and c.cli_codi = cl.cli_codi ";
                if (!dtStat.select(s))
                    return;
                if ((dtStat.getInt("avc_impres") & 1) == 0)
                {
                    int res = mensajes.mensajeYesNo("Albaran no se listo. Dar por repartido, seguro ?");
                    if (res != mensajes.YES)
                        return;
                }
                s = "select alr_nume from albrutacab where alr_fecha= current_date ";
                if (!dtAdd.select(s))
                {
                    dtAdd.addNew("albrutacab", false);
                    dtAdd.setDato("rut_codi", pdconfig.RUTA_RECOGER);
                    dtAdd.setDato("usu_nomb", padre.EU.usuario);
                    dtAdd.setDato("alr_fecha", "current_date");
                    dtAdd.setDato("alr_fecsal", "current_timestamp");
                    dtAdd.setDato("alr_fecreg", "current_timestamp");
                    dtAdd.setDato("veh_codi", 0);
                    dtAdd.setDato("alr_vekmin", 0);
                    dtAdd.setDato("alr_vekmfi", 0);
                    dtAdd.setDato("alr_coment", "Recogido por cliente");
                    dtAdd.update();
                    dtAdd.select("SELECT lastval()");
                    alrNume = dtAdd.getInt(1);
                } else
                {
                    alrNume = dtAdd.getInt("alr_nume");
                    s = "select max(alr_orden) as alr_orden from albrutalin where alr_nume= "+alrNume;
                    dtAdd.select(s);
                    alrOrden=dtAdd.getInt("alr_orden",true)+1;
                }
                dtAdd.addNew("albrutalin");
                dtAdd.setDato("alr_nume", alrNume);
                dtAdd.setDato("alr_orden", alrOrden);
                dtAdd.setDato("avc_id", avc_id);
                dtAdd.setDato("alr_bultos", 1);
                dtAdd.setDato("alr_palets", 0);
                dtAdd.setDato("alr_kilos", 1);
                dtAdd.setDato("alr_unid", 1);
                dtAdd.setDato("alr_horrep", "");
                dtAdd.setDato("alr_comrep", "");
                dtAdd.setDato("cli_nomen", dtStat.getString("avc_clinom").equals("")
                    ? dtStat.getString("cli_nomen") : dtStat.getString("avc_clinom"));
                dtAdd.setDato("cli_diree", dtStat.getString("cli_diree"));
                dtAdd.setDato("cli_codpoe", dtStat.getString("cli_codpoe"));
                dtAdd.setDato("cli_poble", dtStat.getString("cli_poble"));
                dtAdd.setDato("alr_repet", 0);
                dtAdd.update();
                dtAdd.commit();
                setRutaAlb(dtStat,avc_id);
                padre.msgBox("Albaran metido en ruta " + alrNume);
            }
        } catch (SQLException ex)
        {
            padre.Error("Error al dar salida en una ruta al albaran ", ex);
        }
    }
    /**
     * Muestra los datos de una ruta mandando como parametro el albaran de venta 
     * @param dt
     * @param avcId
     * @throws SQLException 
     */
    public void setRutaAlb(DatosTabla dt,int avcId) throws SQLException
    {
      avc_id=avcId;
      String s=" select r.*,v.veh_nomb,t.tra_nomb,ru.rut_nomb from v_albruta as r left join vehiculos as v on v.veh_codi=r.veh_codi "+
               " left join v_tranpvent as t on r.usu_nomb = t.tra_codi  "+
               " left join v_rutas as ru on r.rut_codi=ru.rut_codi "+
               " where r.avc_id = "+avcId;
      if (dt.select(s))
      {
          alr_numeE.setValorInt(dt.getInt("alr_nume"));
          alr_fecsalE.setText(dt.getFecha("alr_fecsal","dd-MM-yy"));
          alr_fecsalH.setText(dt.getFecha("alr_fecsal","HH"));
          alr_fecsalM.setText(dt.getFecha("alr_fecsal","mm"));
          alr_fecregE.setText(dt.getFecha("alr_fecreg","dd-MM-yy"));
          alr_fecregH.setText(dt.getFecha("alr_fecreg","HH"));
          alr_fecregM.setText(dt.getFecha("alr_fecreg","mm"));
          rut_nombE.setText(dt.getString("rut_nomb"));
          tra_nombE.setText(dt.getString("tra_nomb"));
          alr_numeE.setText(dt.getString("alr_nume"));
          
      }
      else
      {
          this.resetTexto();
      }
    }
    public int getNumeroRuta()
    {
        return alr_numeE.getValorInt();
    }
    public void setEnabledRuta(boolean enabRuta)
    {
        BSalir.setEnabled(enabRuta);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cLabel6 = new gnu.chu.controles.CLabel();
        alr_fecsalE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        alr_fecsalH = new gnu.chu.controles.CTextField(Types.DECIMAL,"99");
        alr_fecsalM = new gnu.chu.controles.CTextField(Types.DECIMAL,"99");
        cLabel3 = new gnu.chu.controles.CLabel();
        cLabel7 = new gnu.chu.controles.CLabel();
        cLabel9 = new gnu.chu.controles.CLabel();
        alr_numeE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###,##9");
        tra_nombE = new gnu.chu.controles.CTextField();
        rut_nombE = new gnu.chu.controles.CTextField();
        cLabel8 = new gnu.chu.controles.CLabel();
        alr_fecregE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        alr_fecregH = new gnu.chu.controles.CTextField(Types.DECIMAL,"99");
        alr_fecregM = new gnu.chu.controles.CTextField(Types.DECIMAL,"99");
        BSalir = new gnu.chu.controles.CButton(Iconos.getImageIcon("finish"));

        setLayout(null);

        cLabel6.setText("Salida Ruta");
        cLabel6.setPreferredSize(new java.awt.Dimension(52, 18));
        add(cLabel6);
        cLabel6.setBounds(0, 2, 70, 17);

        alr_fecsalE.setPreferredSize(new java.awt.Dimension(10, 18));
        add(alr_fecsalE);
        alr_fecsalE.setBounds(70, 2, 70, 17);

        alr_fecsalH.setText("0");
        add(alr_fecsalH);
        alr_fecsalH.setBounds(142, 2, 20, 17);

        alr_fecsalM.setText("0");
        add(alr_fecsalM);
        alr_fecsalM.setBounds(165, 2, 20, 17);

        cLabel3.setText("Ruta");
        add(cLabel3);
        cLabel3.setBounds(190, 2, 35, 20);

        cLabel7.setText("Transp.");
        cLabel7.setPreferredSize(new java.awt.Dimension(52, 18));
        add(cLabel7);
        cLabel7.setBounds(190, 20, 55, 17);

        cLabel9.setText("Id");
        add(cLabel9);
        cLabel9.setBounds(420, 2, 20, 17);
        add(alr_numeE);
        alr_numeE.setBounds(440, 2, 50, 17);
        add(tra_nombE);
        tra_nombE.setBounds(250, 20, 190, 17);
        add(rut_nombE);
        rut_nombE.setBounds(220, 2, 190, 17);

        cLabel8.setText("Vuelta Ruta");
        cLabel8.setPreferredSize(new java.awt.Dimension(52, 18));
        add(cLabel8);
        cLabel8.setBounds(0, 20, 70, 17);

        alr_fecregE.setPreferredSize(new java.awt.Dimension(10, 18));
        add(alr_fecregE);
        alr_fecregE.setBounds(70, 20, 70, 17);

        alr_fecregH.setText("0");
        add(alr_fecregH);
        alr_fecregH.setBounds(142, 20, 20, 17);

        alr_fecregM.setText("0");
        add(alr_fecregM);
        alr_fecregM.setBounds(165, 20, 20, 17);

        BSalir.setToolTipText("Poner Albaran como Repartido");
        BSalir.setDependePadre(false);
        add(BSalir);
        BSalir.setBounds(447, 20, 40, 21);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton BSalir;
    private gnu.chu.controles.CTextField alr_fecregE;
    private gnu.chu.controles.CTextField alr_fecregH;
    private gnu.chu.controles.CTextField alr_fecregM;
    private gnu.chu.controles.CTextField alr_fecsalE;
    private gnu.chu.controles.CTextField alr_fecsalH;
    private gnu.chu.controles.CTextField alr_fecsalM;
    private gnu.chu.controles.CTextField alr_numeE;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.controles.CLabel cLabel9;
    private gnu.chu.controles.CTextField rut_nombE;
    private gnu.chu.controles.CTextField tra_nombE;
    // End of variables declaration//GEN-END:variables
}
