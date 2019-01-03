package gnu.chu.Menu;

import java.awt.*;
import javax.swing.*;

/**
 *
 * <p>T�tulo: MENU </p>
 * <p>Descripción: Programa de entrada a Anjelica. </p>
 * <p>Copyright: Copyright (c) 2005
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los términos de la Licencia P�blica General de GNU según es publicada por
 *  la Free Software Foundation, bien de la versi�n 2 de dicha Licencia
 *  o bien (según su elección) de cualquier versi�n posterior.
 *  Este programa se distribuye con la esperanza de que sea �til,
 *  pero SIN NINGUNA GARANT�A, incluso sin la garant�a MERCANTIL impl�cita
 *  o sin garantizar la CONVENIENCIA PARA UN PROP�SITO PARTICULAR.
 *  V�ase la Licencia P�blica General de GNU para m�s detalles.
 *  Deber�a haber recibido una copia de la Licencia P�blica General junto con este programa.
 *  Si no ha sido as�, escriba a la Free Software Foundation, Inc.,
 *  en 675 Mass Ave, Cambridge, MA 02139, EEUU.
 * </p>
 * <p>Empresa: miSL</p>
 * @author chuchi P
 * @version 1.0
 */

public class Menu {
  boolean packFrame = false;

  //Construct the application
  public Menu() {
		// Recoger usuario y password de una pantalla de login
    LoginDB login = new LoginDB();
    login.setVisible(true);
    login.iniciar();
  }

  //Main method
  public static void main(String[] args) {
	try {
//          LookAndFeelInfo[] lkf;
//
//          lkf = UIManager.getInstalledLookAndFeels();
//          for (int n=0;n<lkf.length;n++)
//            System.out.println(lkf[n].getClassName());
//           MetalLookAndFeel.setCurrentTheme(new javax.swing.plaf.metal.DefaultMetalTheme());
//          PlasticLookAndFeel.setMyCurrentTheme(new com.jgoodies.looks.plastic.theme.ExperienceBlue());
//          PlasticLookAndFeel.setHighContrastFocusColorsEnabled(true);

//          UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
          Color cDef = new Color(120, 120, 120);
          UIManager.put("ComboBox.disabledForeground", cDef);
          UIManager.put("Button.disabledText", cDef);
          UIManager.put("CheckBox.disabledText", cDef);
          UIManager.put("ToggleButton.disabledText", cDef);
          UIManager.getDefaults().put("PopupMenu.consumeEventOnClose", Boolean.FALSE);
          Toolkit.getDefaultToolkit().getSystemEventQueue().push(new FilteringEventQueue());
//          UIManager.setLookAndFeel(new de.javasoft.plaf.synthetica.SyntheticaStandardLookAndFeel());

//	   UIManager.setLookAndFeel(new com.sun.java.swing.plaf.gtk.GTKLookAndFeel());
  	} catch (Exception f) {}
          new Menu();
  }
}

class FilteringEventQueue extends EventQueue {
    protected void dispatchEvent(AWTEvent event) {
        if (event.getClass().getName().indexOf("UngrabEvent")!=-1) {
            //filter the UngrabEvent
        } else {
            //dispatch other events
            super.dispatchEvent(event);
        }
    }
}



