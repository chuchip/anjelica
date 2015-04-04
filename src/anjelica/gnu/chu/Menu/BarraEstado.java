/*
* <p>Copyright: Copyright (c) 2005-2015
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
package gnu.chu.Menu;

import gnu.chu.controles.CButton;
import gnu.chu.controles.CLabel;
import gnu.chu.controles.CPanel;
import gnu.chu.utilidades.Fecha;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.ThreadSelected;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;

public class BarraEstado extends CPanel
{
  MenuPrincipal MenuPri;

  BevelBorder bordeLowered = new BevelBorder(BevelBorder.LOWERED);
  BevelBorder bordeRaised = new BevelBorder(BevelBorder.RAISED);

  CButton BMenu = new CButton()
  {
        @Override
    public boolean isFocusTraversable()
    {
      return false;

    }
  };
  CLabel Hora = new CLabel();
  Timer time;
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  FlowLayout flowLayout1 = new FlowLayout();
  CButton cButton1 = new CButton();
  CButton cButton2 = new CButton();
  CButton cButton3 = new CButton();
  BorderLayout borderLayout1 = new BorderLayout();
  CPanel panel2 = new CPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  CButton Bsalir = new CButton(Iconos.getImageIcon("salir"));

	/**
	* Constructor
	*/
  public BarraEstado() {
    try {
      jbInit();
    }
    catch (Exception e) {
    	JOptionPane.showMessageDialog(null, "" + e, "Aviso Urgente", JOptionPane.ERROR_MESSAGE);
    }
  }
  public BarraEstado(MenuPrincipal a) {
    this();
  	MenuPri = a;
  }
  public void setMenuPri(MenuPrincipal a) {
   	MenuPri = a;
  }

  private void jbInit() throws Exception
  {
    BMenu.setText("Menu");
    BMenu.setMnemonic('M');
//    String iconEmp = System.getProperty("empresa");
//    if (iconEmp == null)
    String iconEmp = "anjelica";
    BMenu.setIcon(Iconos.getImageIcon(iconEmp));
//    BMenu.setMargin(new Insets(0, 0, 0, 0));
    BMenu.setBorder(BorderFactory.createRaisedBevelBorder());
    BMenu.setMaximumSize(new Dimension(95, 24));
    BMenu.setPreferredSize(new Dimension(95, 24));
    BMenu.setMinimumSize(new Dimension(95, 24));

    Bsalir.setMaximumSize(new Dimension(24, 24));
    Bsalir.setPreferredSize(new Dimension(24, 24));
    Bsalir.setMinimumSize(new Dimension(24, 24));
    Bsalir.setToolTipText("Salir de la aplicación");
    BMenu.setVerifyInputWhenFocusTarget(true);

    Hora.setBorder(bordeLowered);

    this.setLayout(gridBagLayout1);

    this.setBorder(bordeRaised);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    panel2.setLayout(new GridBagLayout());
    this.add(BMenu, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                                           , GridBagConstraints.WEST, GridBagConstraints.NONE,
                                           new Insets(0, 0, 0, 0), 0, 0));

    this.add(panel2, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
                                            , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                            new Insets(0, 0, 0, 0), 0, 0));
    this.add(Bsalir,    new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));

    this.add(Hora, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
                                          , GridBagConstraints.EAST, GridBagConstraints.BOTH,
                                          new Insets(0, 0, 0, 0), 0, 0));
    actualizaFecha();

    /**
     * Actualiza el Reloj cada minuto
     */
    time = new javax.swing.Timer(10000, new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        actualizaFecha();
      }
    });
    activarEventos();

    // Pone activo el timer
    time.start();
  }

  void activarEventos()
  {
    BMenu.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        if (MenuPri == null)
          return;
        MenuPri.ponerUsuarioEmp();
        MenuPri.setVisible(true);
        MenuPri.Eleccion.requestFocus();
        new ThreadSelected(MenuPri);
        MenuPri.toFront();
      }
    });
    BMenu.addMouseListener(new MouseAdapter()
    {
      @Override
      public void mouseClicked(MouseEvent e)
      {
        if (e.getModifiers() == MouseEvent.BUTTON1_MASK)
          return;
        showMenu(BMenu);
      }
    });
    Bsalir.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
//        System.out.println("saliendo...");
        MenuPri.frmPrincipal.generarEventoSalir();
      }
    });
  }
  // Recoge la hora del Sistema
  private void actualizaFecha() {
  	Hora.setText(Fecha.getFechaSys("dd-MM-yyyy  HH:mm"));
  }

  /**
   * Visualiza el BMenu de Persianas
   * @param c componente a mostrar
   */
  public void showMenu(Component c) {}
}
