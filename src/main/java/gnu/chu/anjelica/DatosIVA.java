
package gnu.chu.anjelica;
/**
 *
 * <p>Título: DatosIVA</p>
 * <p>Descripción: Clase que almacen los datos de Un IVA <br>
 *   </p>
 * <p>Copyright: Copyright (c) 2005-2012
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
 * <p>Empresa: miSL</p>
 * @author chuchi P
 */
import gnu.chu.sql.DatosTabla;
import java.sql.SQLException;


public class DatosIVA {
    private int tipoIVA; // Tipo de IVA
    private double porcIVA; // Porcentaje de IVA
    private double porcREQ; // Porcentaje de Recargo de Equivalencias
    private double baseImp;
    private double imporIva;
    private double imporReq;
            
    public DatosIVA()   {}

    /**
     *
     * @param dt
     * @throws SQLException
     */
    public DatosIVA(DatosTabla dt) throws SQLException
    {
        setTipoIVA(dt.getInt("tii_codi"));
        setPorcIVA(dt.getDouble("tii_iva"));
        setPorcREQ(dt.getDouble("tii_rec"));
    }
    
    public double getPorcIVA() {
        return porcIVA;
    }

    public void setPorcIVA(double porcIVA) {
        this.porcIVA = porcIVA;
    }

    public double getPorcREQ() {
        return porcREQ;
    }

    public void setPorcREQ(double porcREQ) {
        this.porcREQ = porcREQ;
    }

    public int getTipoIVA() {
        return tipoIVA;
    }

    public void setTipoIVA(int tipoIVA) {
        this.tipoIVA = tipoIVA;
    }

    /**
     * @return the baseImp
     */
    public double getBaseImp() {
        return baseImp;
    }

    /**
     * @param baseImp the baseImp to set
     */
    public void setBaseImp(double baseImp) {
        this.baseImp = baseImp;
    }

    /**
     * @return the imporIva
     */
    public double getImporIva() {
        return imporIva;
    }

    /**
     * @param imporIva the imporIva to set
     */
    public void setImporIva(double imporIva) {
        this.imporIva = imporIva;
    }

    /**
     * @return the imporReq
     */
    public double getImporReq() {
        return imporReq;
    }

    /**
     * @param imporReq the imporReq to set
     */
    public void setImporReq(double imporReq) {
        this.imporReq = imporReq;
    }
   
}
