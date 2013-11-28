
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

    public DatosIVA()   {}
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
   
}
