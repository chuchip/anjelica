package gnu.chu.anjelica.almacen;

import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import gnu.chu.Menu.*;
import java.awt.*;

/**
 *
 * <p>Título: trAlbVert </p>
 * <p>Descripción: Traspasar Albaranes a Verteederos. Con este programa se generaran,
 * por cada individuo, una regularizacion a Tipo Vertedero Ventas asignadole el cliente
 * que pone en el Albaran</p>
 * <p>Copyright: Copyright (c) 2005
 *  <p>  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
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
 * @author chuchiP
 * @version 1.0
 *
 */
public class trAlbVert extends ventana
{
  public trAlbVert(EntornoUsuario eu, Principal p) {
   EU=eu;
   vl=p.panel1;
   jf=p;
   eje=true;

   setTitulo("Trasp. Albaranes Ventas a Vert.");

   try  {
     if(jf.gestor.apuntar(this))
         jbInit();
     else
       setErrorInit(true);
   }
   catch (Exception e) {
     e.printStackTrace();
     setErrorInit(true);
   }
 }

 public trAlbVert(gnu.chu.anjelica.menu p,EntornoUsuario eu) {

   EU=eu;
   vl=p.getLayeredPane();
   setTitulo("Trasp. Albaranes Ventas a Vert.");
   eje=false;

   try  {
     jbInit();
   }
   catch (Exception e) {
     e.printStackTrace();
     setErrorInit(true);
   }
 }

 private void jbInit() throws Exception
 {
   iniciarFrame();
   this.setSize(new Dimension(539, 438));

   statusBar = new StatusBar(this);
   this.getContentPane().add(statusBar, BorderLayout.SOUTH);

   conecta();
 }
}

