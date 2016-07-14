
package gnu.chu.camposdb;

import gnu.chu.anjelica.compras.AlbProv;
import gnu.chu.anjelica.compras.MantAlbCom;
import gnu.chu.controles.CInternalFrame;
import gnu.chu.controles.CPanel;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.ventana;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JLayeredPane;

/*
 * AvcPanel.java
 *
 * Panel con los campos que define un número de albaran
 * Created on 08-feb-2010, 22:59:20
 * <p>Copyright: Copyright (c) 2005-2016
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
public class AccPanel extends CPanel
{
    ArrayList<FocusListener> eventVector = new ArrayList();
    private int id=0;
    private EntornoUsuario EU;
    private DatosTabla dt;
    private ventana padre;
    AlbProv  alPrv=null;
    /**
     * Creates new form AccPanel
     */
    public AccPanel() {
        initComponents();
    }
    public void iniciar(DatosTabla datTabla, ventana papa,
                    JLayeredPane layPan, EntornoUsuario entUsu)
    {
       this.EU=entUsu;
       this.dt=datTabla;
       this.padre=papa;
       acc_anoE.setValorDec(EU.ejercicio);
       acc_serieE.setText("A");
       acc_numeE.setValorInt(0);
       Bconsulta.addActionListener(new ActionListener()
       {
            @Override
            public void actionPerformed(ActionEvent e)
            {
              consulta();
            }
       });
    }
    
    void  consulta()
  {
       try
       {       
         if (alPrv == null)
         {
           alPrv = new AlbProv()
           {
               @Override
               public void muerto()
               {
                   if (alPrv.getAccNume()>0)
                   {
                       acc_anoE.setValorInt(alPrv.getAccAno());
                       acc_serieE.setText(alPrv.getAccSerie());
                       acc_numeE.setValorInt(alPrv.getAccNume());
                   }
               }
           };
          
           alPrv.iniciar(padre);
           alPrv.setVerPrecios(false);
           alPrv.setOrdenAlbaranDescendente(true);            
           padre.vl.add(alPrv);
         }
         alPrv.setLocation(this.getLocation().x+100, this.getLocation().y+80);
         alPrv.setSelected(true);
         padre.setEnabled(false);
         padre.setFoco(alPrv);
         alPrv.cargaDatos(Formatear.sumaDiasDate(Formatear.getDateAct(),-90),
             Formatear.getDateAct(),0);
         alPrv.setVisible(true);
       }
       catch (Exception ex)
       {
         padre.fatalError("Error al Cargar datos de Proveedor",ex);
       }
  }
    /**
     * Establece el ID. Llena los campos Serie, ejercicio y Numero de albaran
     * @param id 
     */
  public void setId(int id) throws SQLException
  {
      if (!getDatosAlb(id))
      {
          this.resetTexto();
          return;
      } 
      acc_anoE.setValorInt(dt.getInt("acc_ano"));
      acc_numeE.setValorInt(dt.getInt("acc_nume"));
      acc_serieE.setValorInt(dt.getInt("acc_serie"));
  }
  @Override
  public void addFocusListener(FocusListener fc)
  {
    if (eventVector.isEmpty())
    {
      FocusListener fa1=new FocusListener()
      {
          @Override
          public void focusGained(FocusEvent e) {
             
          }

          @Override
          public void focusLost(FocusEvent e) {
             if (e.isTemporary())
                 return;
             Component c = e.getOppositeComponent();
             if (c != acc_anoE && c != acc_serieE && c != acc_numeE)
                 processFocusEvent(e);
          }
          
      };
      acc_anoE.addFocusListener(fa1);
      acc_numeE.addFocusListener(fa1);
      acc_serieE.addFocusListener(fa1);
    }
    eventVector.add(fc);
    
  }
   @Override
  public void processFocusEvent(FocusEvent e)
  {
    super.processFocusEvent(e);
    if (eventVector.isEmpty())
      return;
 
    for (FocusListener eventVector1 : eventVector)
    {
          int id = e.getID();
          //FocusListener listener = (FocusListener) eventVector1;
          if (eventVector1 != null)
          {
              switch (id)
              {
                  case FocusEvent.FOCUS_GAINED:
                      eventVector1.focusGained(e);
                      break;
                  case FocusEvent.FOCUS_LOST:
                      eventVector1.focusLost(e);
                      break;
              }
          }
    }
  }
  /**
   * Busca los datos del albaran a traves del ID
   * @param id
   * @return
   * @throws SQLException 
   */
  public boolean getDatosAlb(int avcId) throws SQLException
  {
      this.id=0;
      String s = "SELECT * FROM v_albacoc WHERE acc_id = " + avcId;
      if (dt.select(s))
      {
          this.id=avcId;
          return true;
      }
      return false;
  }
  public boolean hasIdValido()
  {
      return id!=0;
  }
  
  public Date getFechaAlb() throws SQLException
  {
    if (id==0)
        return null;
    else
        return dt.getDate("acc_fecha");
  }
  public int getPrvAlb() throws SQLException
  {
    if (id==0)
        return 0;
    else
        return dt.getInt("prv_codi");
  }
  
    @Override
  public void requestFocus()
  {
      if (acc_anoE == null)
          super.requestFocus();
      else
        acc_anoE.requestFocus();
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

        acc_anoE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        acc_serieE = new gnu.chu.controles.CTextField(Types.CHAR,"X",1);
        acc_numeE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#####9");
        Bconsulta = new gnu.chu.controles.CButton(Iconos.getImageIcon("find"));

        setLayout(new java.awt.GridBagLayout());

        acc_anoE.setMaximumSize(new java.awt.Dimension(30, 17));
        acc_anoE.setMinimumSize(new java.awt.Dimension(30, 17));
        acc_anoE.setPreferredSize(new java.awt.Dimension(30, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 0);
        add(acc_anoE, gridBagConstraints);

        acc_serieE.setAutoNext(true);
        acc_serieE.setMaximumSize(new java.awt.Dimension(12, 17));
        acc_serieE.setMayusc(true);
        acc_serieE.setMinimumSize(new java.awt.Dimension(12, 17));
        acc_serieE.setPreferredSize(new java.awt.Dimension(12, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 0);
        add(acc_serieE, gridBagConstraints);

        acc_numeE.setMaximumSize(new java.awt.Dimension(50, 17));
        acc_numeE.setMinimumSize(new java.awt.Dimension(50, 17));
        acc_numeE.setPreferredSize(new java.awt.Dimension(50, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 0);
        add(acc_numeE, gridBagConstraints);

        Bconsulta.setToolTipText("Buscar Albaranes Compra");
        Bconsulta.setMaximumSize(new java.awt.Dimension(18, 18));
        Bconsulta.setMinimumSize(new java.awt.Dimension(18, 18));
        Bconsulta.setPreferredSize(new java.awt.Dimension(18, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        add(Bconsulta, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Bconsulta;
    private gnu.chu.controles.CTextField acc_anoE;
    private gnu.chu.controles.CTextField acc_numeE;
    private gnu.chu.controles.CTextField acc_serieE;
    // End of variables declaration//GEN-END:variables
}
