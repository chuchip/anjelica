package gnu.chu.anjelica.almacen;
/**
 * <p>Titulo: StkPartid</p>
 * <p>Descripcion: Clase para almacen stock actual de un producto,
 * indicando ademas el estado resultante a la hora de buscar el stock
 * disponible</p>
* <p>Copyright: Copyright (c) 2005-2012
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
 * <p>Empresa: micasa</p>
 * @author chuchiP
 * @version 1.0
 */

public class StkPartid {
    private double kilos=0;

    public double getKilos() {
        return kilos;
    }

    public void setKilos(double kilos) {
        this.kilos = kilos;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getUnidades() {
        return unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }
    private int unidades=0;
    private int estado=0;

    public final static int ARTIC_NOT_FOUND = 1; // "Articulo NO encontrado";
    public final static int INDIV_NOT_FOUND= 2; //"NO encontrado Partida para estos valores";
    public final static int INDIV_LOCK=3; // "Individuo Bloqueado"
    public final static int ARTIC_SIN_CONTROL_INDIV=4;// Articulo sin Control Individuos
    public final static int INDIV_OK=0;
    private final String[] literal=new String[]{null,"Articulo NO encontrado",
        "Individuo NO encontrado en Stock-Partidas",
        "Individuo Bloqueado",
        "Articulo sin Control Individuos"
  };
  public String getMensaje(int estad)
  {
      return literal[estad];
  }
  public String getMensaje()
  {
      return getMensaje(estado);
  }
  public boolean hasError()
  {
    return estado!=INDIV_OK && estado!=ARTIC_SIN_CONTROL_INDIV;
  }
  public boolean hasControlIndiv()
  {
    return estado!=ARTIC_SIN_CONTROL_INDIV;
  }

  public StkPartid(int estado, double kilos, int unidad)
  {
    unidades=unidad;
    this.kilos=kilos;
    this.estado=estado;
  }
  public StkPartid(int estado)
  {
     this(estado,0,0);
  }
}
