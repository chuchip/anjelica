package gnu.chu.utilidades;

import gnu.chu.controles.*;
/**
 * <p>
 * Este Thread es Utilizado para seleccionar la pantalla.<p>
 * <p>
 * Para utilizarlo solo hay que intanciarlo.<p>
 * <p>
 * Creado el 08/06/2000
 * <p>
 * @author   Angel J. Apellaniz
 * @author   EMAIL: aapella@virtualcom.es<p>
 * <p>
 * @version  1.0 del 08/06/2000
 * <p>
 */
public class ThreadSelected extends Thread {
      CInternalFrame vif;
      final static String titulo = "Virtual.Selected.Seleccionado Ventana";

      public ThreadSelected(CInternalFrame ventana, String title, ThreadGroup grupo) {
             super(grupo, titulo + "-" + title);
             vif = ventana;
             start();
      }
      public ThreadSelected(CInternalFrame ventana, String title) {
             super(titulo + "-" + title);
             vif = ventana;
             start();
      }
      public ThreadSelected(CInternalFrame ventana) {
             super(titulo);
             vif = ventana;
             start();
      }
      public void run() {
             try {
                 sleep(100);
             } catch (Exception j) {}
             try {
                 vif.setSelected(true);
             } catch (Exception j) {}
             vif = null;
      }
}
