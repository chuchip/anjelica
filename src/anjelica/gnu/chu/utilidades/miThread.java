package gnu.chu.utilidades;
/**
 *
 * <p>Título: miThread  </p>
 * <p>Descripción: Clase generica para crear threads.
 * </p>.
 * <p>Realizar un new y despues machacar la funcion run</p>
 * <p>Copyright: Copyright (c) 2005-2010</p>
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
 * @author chuchi P.
 * @version 1.0
 */
public class miThread extends Thread
{
      public miThread(String title, ThreadGroup grupo)
      {
             super(grupo, "gnu.chu." + title);
             start();
      }
      public miThread(String title)
      {
             super("gnu.chu.." + title);
             start();
      }
    @Override
      public void run() {}
}
