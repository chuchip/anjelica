package gnu.chu.camposdb;
/**
 *  Clase q extiende de PFechaInv
 *
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
 * <p>Empresa: MISL</p>
 * @author chuchiP
 * @version 1.0
 */

import gnu.chu.anjelica.inventario.PfechaInv;
import gnu.chu.sql.DatosTabla;


public class fechaCal extends PfechaInv {
    public static int INICIO=1;
    public static int FINAL=2;
    int tipFecha=1;
    /**
     * Define el tipo de fecha.
     * utilizar las variables estaticas INICIO y FINAL
     * @param tipoFecha
     */
    public void setTipoFecha(int tipoFecha)
    {
        tipFecha=tipoFecha;
    }
    public int getTipoFecha()
    {
        return tipFecha;
    }
    @Override
    public void cargaGrid()
    {
        DatosTabla dtStat=getDatosTabla();
        String s;
        if (tipFecha==INICIO)
            s="SELECT cal_fecini  FROM calendario "
             + " where cal_fecini < current_date "
             + "order by cal_fecini desc";
        else
            s="SELECT cal_fecfin  FROM calendario "
             + " where cal_fecfin < current_date "
             + "order by cal_fecfin desc";
        try {
            dtStat.select(s);
            this.setDatos(dtStat);
        } catch (Exception k)
        {
            Error("Error al buscar Fechas de Inventario",k);
        }
    }
}
