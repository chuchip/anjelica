package gnu.chu.utilidades;
/**
 *  Clase con los datos de un producto. Usado para el arbol de familias.
 *
 * @version 1.0
 *
 * <p>Copyright: Copyright (c) 2005-2011
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los terminos de la Licencia Pública General de GNU seg�n es publicada por
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
*
*/

public class DatosProd {
    private int proCodi;
    private double kilos=0;
    private int unidades=0;
    private double importe=0;
    private double impGanan=0;

    public double getImpGanan() {
        return impGanan;
    }

    public void setImpGanan(double impGanan) {
        this.impGanan = impGanan;
    }
    private String nombre;
    
    public int getUnidades() {
        return unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }

    public  DatosProd(int proCodi)
    {
        this.proCodi=proCodi;
    }
    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public double getKilos() {
        return kilos;
    }

    public void setKilos(double kilos) {
        this.kilos = kilos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
  
    /**
     * Suma los valores de un datosprod a este
     * @param dt
     */
    public void add(DatosProd dt)
    {
        kilos+=dt.getKilos();
        importe+=dt.getImporte();
        unidades+=dt.getUnidades();
        impGanan+=dt.getImpGanan();
    }
    public double getGananciaKilo()
    {
        if (impGanan==0 || kilos==0)
            return 0;
        return impGanan/kilos;
    }
}
