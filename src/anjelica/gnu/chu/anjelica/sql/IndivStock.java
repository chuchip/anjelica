package gnu.chu.anjelica.sql;

/**
 *
 * <p>Título: IndivStock </p>
 * <p>Descripción: Almacena los datos de identificacion de un IndivStock </p>
 * <p>Copyright: Copyright (c) 2005-2016
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
 *
 *
 */
public class IndivStock
{
    String proNomb;

    public String getProNomb() {
        return proNomb;
    }

    public void setProNomb(String proNomb) {
        this.proNomb = proNomb;
    }
    int almCodi;
    int proCodi;
    int ejeNume;
    int proNupar;
    String proSerie;
    int proNumind;        
    
    int stpUnact ;
    double stpKilact;
    /**
     * 
     * @param alm_codi
     * @param pro_codi
     * @param eje_nume
     * @param pro_nupar
     * @param pro_serie
     * @param pro_numind
     * @param stp_unidades
     * @param stp_kilos 
     */
    public IndivStock(int alm_codi,int pro_codi,int eje_nume, int pro_nupar,String pro_serie,
        int pro_numind,int stp_unidades,double stp_kilos)
    {
        almCodi=alm_codi;
        proCodi=pro_codi;
        ejeNume=eje_nume;
        proNupar=pro_nupar;
        proSerie=pro_serie;
        proNumind=pro_numind;
        stpUnact=stp_unidades;
        stpKilact=stp_kilos;
    }
    public int getAlmCodi() {
        return almCodi;
    }

    public void setAlmCodi(int almCodi) {
        this.almCodi = almCodi;
    }

    public int getProCodi() {
        return proCodi;
    }

    public void setProCodi(int proCodi) {
        this.proCodi = proCodi;
    }

    public int getEjeNume() {
        return ejeNume;
    }

    public void setEjeNume(int ejeNume) {
        this.ejeNume = ejeNume;
    }

    public int getProNupar() {
        return proNupar;
    }

    public void setProNupar(int proNupar) {
        this.proNupar = proNupar;
    }

    public String getProSerie() {
        return proSerie;
    }

    public void setProSerie(String proSerie) {
        this.proSerie = proSerie;
    }

    public int getProNumind() {
        return proNumind;
    }

    public void setProNumind(int proNumind) {
        this.proNumind = proNumind;
    }

    public int getStpUnact() {
        return stpUnact;
    }

    public void setStpUnact(int stpUnact) {
        this.stpUnact = stpUnact;
    }

    public double getStpKilact() {
        return stpKilact;
    }

    public void setStpKilact(double stpKilact) {
        this.stpKilact = stpKilact;
    }
    
}
