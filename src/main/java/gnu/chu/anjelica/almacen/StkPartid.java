package gnu.chu.anjelica.almacen;

import java.util.Date;

/**
 * <p>Titulo: StkPartid</p>
 * <p>Descripcion: Clase para almacen stock actual de un producto,
 * indicando ademas el estado resultante a la hora de buscar el stock
 * disponible</p>
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
 * <p>Empresa: micasa</p>
 * @author chuchiP
 * @version 1.0
 */

public class StkPartid {
    private boolean lockIndiv=false;
    private Date fecCad=null;
    private double kilos=0;
    private double kilosIniciales=0;
    private int unidadesIniciales=0;
    private boolean indivStock;
    private boolean controlInd;
    private int estado;
    public final static int ARTIC_NOT_FOUND = 1; // Articulo NO encontrado
    public final static int INDIV_NOT_FOUND= 2; //"NO encontrado Partida para estos valores";
    public final static int INDIV_LOCK=3; //Bloqueado a otro cliente
    public final static int INDIV_OK=0; // Individuo Encontrado
    
    private final String[] literal=new String[]{null,"Articulo NO encontrado",
        "Individuo NO encontrado en Stock-Partidas",
        "Individuo Reservado a otro cliente",
        "Articulo sin Control Individuos",
        "Articulo sin Control Existencias",
        "Articulo sin Control Existencias ni Indiv."
  };
    private int reservadoCliente=0;
    private boolean controlExist;
    public boolean hasControlInd() {
        return controlInd;
    }

    public void setControlInd(boolean controlInd) {
        this.controlInd = controlInd;
    }

    public boolean isControlExist() {
        return controlExist;
    }
   
    public void setControlExist(boolean controlExist) {
        this.controlExist = controlExist;
    }

    public int getReservadoCliente() {
        return reservadoCliente;
    }

    public void setReservadoCliente(int reservaCliente) {
        this.reservadoCliente = reservaCliente;
    }
    
    /**
     * Dice si el articulo esta bloqueado para otro cliente
     *
     * @return
     */
    public boolean isLockIndiv() {
        return lockIndiv;
    }

    public void setLockIndiv(boolean bloqueado)
    {
        if (bloqueado)
            setEstado(INDIV_LOCK);
        lockIndiv = bloqueado;
    }
    
    public double getKilos() {
        return kilos;
    }

    public void setKilos(double kilos) {
        this.kilos = kilos;
    }
    public void setKilosIniciales(double kilosIni) {
        this.kilosIniciales = kilosIni;
    }
    public double getKilosIniciales()
    {
        return kilosIniciales;
    }
    public void setUnidadesIniciales(int unidadesInic)
    {
        unidadesIniciales=unidadesInic;
    }
    public int getUnidadesIniciales()
    {
        return unidadesIniciales;
    }

    /**
     * Establece la fecha de caducidad
     * @param fecCad 
     */
    public void setFechaCad(Date fecCad)
    {
        this.fecCad=fecCad;
    }
    /**
     * Devuelve la fecha de caducidad
     * @return 
     */
    public Date getFechaCad()
    {
        return this.fecCad;
    }
    public int getUnidades() {
        return unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }
    private int unidades=0;


  

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
    
  public String getMensaje(int estad)
  {
      return literal[estad];
  }
  public String getMensaje()
  {
      return getMensaje(estado)+(estado==INDIV_LOCK?". (Cliente: "+getReservadoCliente()+")":"");
  }
  /**
   * Indica si existe el individuo existe en stock partidas
   * @param indivStock 
   */
  public void setIndivStock(boolean indivStock)
  {
      this.indivStock=indivStock;
  }
  
  public boolean getIndivStock()
  {
      return indivStock;
  }
  
 
  
  public StkPartid(int estado,double kilos, int unidad)
  {
    this.estado=estado;
    unidades=unidad;
    this.kilos=kilos;
  }
  public StkPartid(int estado)
  {   
      this(estado,0,0);
  }
  /**
   * Devuelve si el individuo tiene errores.
   * @return 
   */
  public boolean hasError()
  {
      return hasError(0);
  }
  
   /**
   * Devuelve si el individuo tiene errores.
   * @param cliCodi si esta bloqueado y el cliente es diferente al mandado considera que tiene error.
   * @return 
   */
  public boolean hasError(int cliCodi)
  {
      if (estado==ARTIC_NOT_FOUND)
          return true;
      if (estado==INDIV_LOCK)
          return cliCodi==getReservadoCliente();
      return estado==INDIV_NOT_FOUND && hasControlInd();
  }
  
  /**
   * Comprueba si hay algo de stock para este individuo
   * @return 
   */
  public boolean hasStock()
  {
       if (estado==ARTIC_NOT_FOUND)
          return false;
       return kilos != 0 || !isControlExist();
 }
  
  public boolean hasStock(double kilos)
  {
      if (hasError())
          return false;
      if (! isControlExist())
          return true;
      if (getKilos()==0)
          return false;
      return getKilos()>=kilos;
  }
}
