
package gnu.chu.camposdb;

/**
 *
 * <p>Título: PaiPanel</p>
 * <p>Descripción: Panel para introducir el codigo del Pais</p>
 * <p>Copyright: Copyright (c) 2016-2016
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los términos de la Licencia Pública General de GNU según es publicada por
 *  la Free Software Foundation, bien de la versión 2 de dicha Licencia
 *  o bien (según su elección) de cualquier versión posterior.
 *  Este programa se distribuye con la esperanza de que sea útil,
 *  pero SIN NINGUNA GARANTÍA, incluso sin la garant�a MERCANTIL impl�cita
 *  o sin garantizar la CONVENIENCIA PARA UN PROPOSITO PARTICULAR.
 *  Vease la Licencia P�blica General de GNU para más detalles.
 *  Debería haber recibido una copia de la Licencia Pública General junto con este programa.
 *  Si no ha sido así, escriba a la Free Software Foundation, Inc.,
 *  en 675 Mass Ave, Cambridge, MA 02139, EEUU.
 * </p>
 * <p>Empresa: MISL</p>
 * @author chuchiP
 * @version 2.0
 */
import gnu.chu.anjelica.pad.MantPaises;
import static gnu.chu.camposdb.cliPanel.DBSKIP;
import static gnu.chu.camposdb.cliPanel.LOSTFOCUS;
import gnu.chu.controles.CInternalFrame;
import gnu.chu.controles.CLabel;
import gnu.chu.controles.CPanel;
import gnu.chu.controles.CTextField;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.winayu.AyuClientes;
import gnu.chu.winayu.AyuPaises;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLayeredPane;

public class PaiPanel extends CPanel
{
    AyuPaises ayuPais;
    private Integer peso=1;
    private boolean error=false;
    DatosTabla dt;
    CInternalFrame intfr = null;
    EntornoUsuario eu;
    JLayeredPane vl;
    private boolean botonConsultar = true;
    
    boolean swControl =true;

    public PaiPanel() {
        initComponents();
    }
    public void iniciar(DatosTabla datTabla, CInternalFrame intFrame,
                    JLayeredPane layPan, EntornoUsuario entUsu) throws SQLException
   {
     intfr = intFrame;
     dt = datTabla;
     vl = layPan;
     eu = entUsu;
//    pro_codiE.setMascaraDB(dt,eu.em_cod);
     activarEventos();
     setText("");

     if (swControl)
     {
       setText("");
       controlar();
     }
  }
  public CTextField getFieldPaiNomb()
  {
      return pai_nombE;
  }
  public CTextField getFieldPaiCodi()
  {
      return pai_codiE;
  }
  public void setBotonConsultar(boolean activo) {
    Bcons.setVisible(activo);
    botonConsultar=activo;
  }
  public boolean getBotonConsultar() {
    return botonConsultar;
  }
  public void setQuery(boolean modoQuery) {
    pai_codiE.setQuery(modoQuery);
    //Bcons.setEnabled(!modoQuery);
  }
  public boolean isQuery() {
    return pai_codiE.isQuery();
    //Bcons.setEnabled(!modoQuery);
  }
  private void activarEventos()
  {
       Bcons.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        consPais();
      }
    });
        pai_codiE.addFocusListener(new FocusAdapter()
      {
        @Override
        public void focusLost(FocusEvent e)
        {
          if (isQuery())
              return;
          try
          {
            if (pai_codiE.hasCambio())
            {
              pai_codiE.resetCambio();
              afterFocusLost(!controlar());
            }
          }
          catch (Exception k)
          {}
        }
      });    
    pai_codiE.addKeyListener(new KeyAdapter()
    {
            @Override
      public void keyPressed(KeyEvent e)
      {
        if (botonConsultar && (e.getKeyCode() == KeyEvent.VK_F3 ||
            (e.getKeyChar()=='3' && e.getModifiers()==KeyEvent.CTRL_MASK)) )
        {
          consPais();
        }

      }
    });
  }
  void consPais()
  {
      try
      {
        if (ayuPais==null)
        {
          ayuPais = new AyuPaises(eu, vl, dt)
          {
            @Override
            public void matar()
            {
               ej_consPais(ayuPais);
            }
          };
          ayuPais.iniciarVentana();
          ayuPais.setLocation(25, 25);
          if (intfr!=null)
            intfr.getLayeredPane().add(ayuPais,1);
          else
            vl.add(ayuPais,peso);
        }
        ayuPais.mostrar();
        
        
        if (intfr!=null)
        {
          intfr.setEnabled(false);
          intfr.setFoco(ayuPais);
        }
      }
      catch (Exception j)
      {        
        if (intfr != null)
          intfr.setEnabled(true);
      }

  }

  void ej_consPais(AyuPaises ayuPais)
  {

    if (ayuPais.getChose())
    {
      pai_codiE.setText(ayuPais.getInicPais());
      pai_nombE.setText(ayuPais.getNombrePais());
      error=false;
    }
    pai_codiE.requestFocus();
    ayuPais.setVisible(false);

    if (intfr != null)
    {
      intfr.setEnabled(true);
      intfr.toFront();
      try
      {
        intfr.setSelected(true);
      }
      catch (Exception k)
      {}
      intfr.setFoco(null);
      this.requestFocus();      
    }
  }
  /**
   * Machacar para controlar que hacer despues de un focus lost.
   * Esta funcion es llamada despues de un focusLost, si ha habido cambio en el
   * codigo de cliente y despues de haber llamado a controlar
   * @see controlar()
   * @param noError boolean Recibe el resultado de la funcion controlar
   *              true significa que NO ha habido error
   *              false que hay error
   *
   */
  protected void afterFocusLost(boolean noError)
  {
  
  }
 public void setText(String pais) {

    pai_codiE.setText(pais);
    if (! swControl)
        return; // Pasa de buscar y poner el nombre del producto.
    try {
      controlar();
    } catch (Exception k)
    {

    }
    pai_codiE.resetCambio();
  }
 public String getText()
 {
     return pai_codiE.getText();
 }
  public String getTextNomb()
 {
     return pai_nombE.getText();
 }
 public boolean controlar() throws  SQLException
 {
     return controlar(true);
 }
 public boolean controlar(boolean reqFocus) throws  SQLException
 {
     error=true;
     String paiNomb=MantPaises.getNombrePais(pai_codiE.getText(), dt);
     if (paiNomb==null)
     {
         pai_nombE.setText("Pais NO encontrado");
         pai_codiE.requestFocus();
         return false;
     }
     pai_nombE.setText(paiNomb);
     error=false;
     return true;
 }
 public boolean isNull()
 {
    return pai_codiE.isNull();
 }
 
 public boolean getError()
 {
     if (hasCambio())
     {
         try
         {
             error=MantPaises.getNombrePais(pai_codiE.getText(), dt)==null;
         } catch (SQLException ex)
         {
             Logger.getLogger(PaiPanel.class.getName()).log(Level.SEVERE, null, ex);
         }
     }
    return error;
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

        pai_codiE = new gnu.chu.controles.CTextField(Types.CHAR,"X",2);
        pai_nombE = new gnu.chu.controles.CTextField();
        Bcons = new gnu.chu.controles.CButton();

        setLayout(new java.awt.GridBagLayout());

        pai_codiE.setMayusculas(true);
        pai_codiE.setMaximumSize(new java.awt.Dimension(30, 17));
        pai_codiE.setMinimumSize(new java.awt.Dimension(30, 17));
        pai_codiE.setPreferredSize(new java.awt.Dimension(30, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        add(pai_codiE, gridBagConstraints);

        pai_nombE.setBackground(java.awt.Color.orange);
        pai_nombE.setEnabled(false);
        pai_nombE.setMaximumSize(new java.awt.Dimension(100, 17));
        pai_nombE.setMinimumSize(new java.awt.Dimension(100, 17));
        pai_nombE.setPreferredSize(new java.awt.Dimension(100, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(pai_nombE, gridBagConstraints);

        Bcons.setMaximumSize(new java.awt.Dimension(20, 18));
        Bcons.setMinimumSize(new java.awt.Dimension(20, 18));
        Bcons.setPreferredSize(new java.awt.Dimension(20, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weighty = 1.0;
        add(Bcons, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Bcons;
    private gnu.chu.controles.CTextField pai_codiE;
    private gnu.chu.controles.CTextField pai_nombE;
    // End of variables declaration//GEN-END:variables
}
