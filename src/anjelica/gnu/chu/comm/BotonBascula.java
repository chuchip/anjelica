package gnu.chu.comm;

/**
 *
 * <p>Título: BotonBascula </p>
 * <p>Descripción: Clase que muestra un Boton con un menu y que sirve para
 *  elegir de que bascula se debe leer un peso en un Ctextfield </p>
 * <p>Copyright: Copyright (c) 2005-2014
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
* <p>Empresa: MISL</p>
* @author chuchiP
* @version 1.0
*/
import gnu.chu.controles.CButtonMenu;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.ventana;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.InternalFrameEvent;


public class BotonBascula extends CButtonMenu{
  IFBascula ifBascula;
  int bascActiva=0;
  EntornoUsuario EU;
  private final int numBasculas;
  private final ventana papa;

    public BotonBascula(EntornoUsuario eu, ventana padre) {
        this.EU = eu;
        this.papa=padre;
        numBasculas = EU.getBascula().getNumBasculas();
        for (int n = 0; n < numBasculas; n++) {
            this.addMenu(EU.getBascula().getNombreBascula(n));
        }
        
        this.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {                
                if (e.getID() < 0) {
                    verBascula();
//                    int nBasc = bascActiva + 1;
//                    if (nBasc >= numBasculas)
//                        nBasc = 0;
//                    cambiaBascula(nBasc);
                } else {
                    cambiaBascula(e.getID());
                }
            }
        });
        cambiaBascula(0);
        this.setIcon(Iconos.getImageIcon("balanza"));
    }
    public void setNumeroCajas(int numeroCajas)
    {
         for (int n = 0; n < numBasculas; n++) {
            EU.getBascula().getClassleePeso(n).setNumeroCajas(numeroCajas);
         if (ifBascula!=null)
              ifBascula.setNumeroCajas(numeroCajas);

        }
    }
     public void setPesoCajas(double pesoCajas)
    {
         for (int n = 0; n < numBasculas; n++) {
            EU.getBascula().getClassleePeso(n).setPesoCajas(pesoCajas);
            if (ifBascula!=null)
                ifBascula.setPesoCaja(pesoCajas);
        }
    }
  void verBascula()
  {
      if (ifBascula==null)
      {
         EU.getBascula().getPesoBascula(bascActiva);  
         ifBascula = new IFBascula(EU.getBascula().getClassleePeso(bascActiva));
       
         papa.vl.add(ifBascula,new Integer(1));
         ifBascula.setLocation(25, 25);
      }
      try
      {
          ifBascula.setVisible(true);  
          ifBascula.setSelected(true);
          ifBascula.setClosed(false);
          ifBascula.actualTemporizador();
      } catch (PropertyVetoException ex)
      {
          Logger.getLogger(BotonBascula.class.getName()).log(Level.SEVERE, null, ex);
      }
  }
  private void cambiaBascula(int numBasc)
  {
    String nombre="";
    if (EU.getBascula().isActivo(numBasc))
        nombre="(I) ";
    this.setToolTipText(nombre+EU.getBascula().getNombreBascula(numBasc));
    bascActiva=numBasc;
    papa.mensajeRapido("Nueva bascula activa: "+EU.getBascula().getNombreBascula(numBasc));
  }

  public double getPesoBascula()
  {
     return EU.getBascula().getPesoBascula(bascActiva);
  }
  public void dispose()
  {
      if (ifBascula!=null)
          ifBascula.dispose();
  }
}
