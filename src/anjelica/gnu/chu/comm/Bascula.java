package gnu.chu.comm;

import gnu.chu.utilidades.mensajes;
import java.util.ArrayList;
import java.util.ResourceBundle;
 
/**
 *
 * <p>Título: Bascula</p>
 * <p>Descripción: Clase con las diferentes basculas definidas.
 * Se apoya en leePeso. Es usuada por BotonBascula</p>
 *
*  <p>Copyright: Copyright (c) 2005-2009
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
 * @author Chuchi P
 * @version 2.0
 */
public class Bascula {

//    private final int bascActiva=0; // Bascula Activa
    public gnu.chu.comm.leePeso leepeso=null;
 
    private String raiz;
    ArrayList<leePeso> pesos=new ArrayList(); // Fic. Propiedasdes Basculas Activas
//    ArrayList leePeso = new ArrayList();


    /**
     * Clase usada para leer basculas
     * @param entUsu
     */
    public Bascula()
    {
        raiz = System.getProperty("raiz");
        if (raiz == null)
             raiz = "gnu.chu";
        ResourceBundle param=null;
        try {
             param = ResourceBundle.getBundle(raiz + ".bascula");
             pesos.add(new leePeso(raiz + ".bascula"));
        } catch (Exception k)
        {
            System.err.println("No encontrado fichero de Propiedades Estandard");
        }
       
        for (int n=0;n<9;n++)
        {
             try {
                param = ResourceBundle.getBundle(raiz + ".bascula"+n);
                pesos.add(new leePeso(raiz + ".bascula"+n));
              }
             catch (Exception k)
             {
             }
        } 
    }
    public String getNombreBascula(int bascula)
    {
        return pesos.get(bascula).getNombre();
    }
    public boolean isActivo(int numBascula)
    {
        return pesos.get(numBascula).isActivo();
    }
    public ArrayList getNombreBasculas()
    {
        ArrayList lista=new ArrayList();
        for (int n=0;n<pesos.size();n++)
        {
            String nombre=pesos.get(n).getNombre();
            if (nombre==null)
                nombre="Basc: "+n;
            lista.add(pesos.get(n).getNombre());
        }
        return lista;
    }
    public int getNumBasculas()
    {
        return pesos.size();
    }

   
/**
   * Lee el peso de la bascula
   * @return el peso leido.
   */
  public double getPesoBascula(int bascula)
   {
     try {
         if (! pesos.get(bascula).isIniciado())
             pesos.get(bascula).iniciar();
         return pesos.get(bascula).getPeso();
     } catch (Throwable k)
     {
        mensajes.mensajeAviso("Erro al Inicializar bascula "+getNombreBascula(bascula)+"\n"+k.getMessage());
        k.printStackTrace();
     }
     return 0;
   }
    public leePeso getClassleePeso(int bascula)
    {                 
       return pesos.get(bascula);     
    }
}

