package gnu.chu.anjelica.almacen;

import java.util.Date;

/**
  <p> Descripción: Clase para almacenar los datos de un individuo mandados a traves de 
 * HashMap.
 * Usada por actStkPart y PdInvControl</p>
 * <p>Copyright: Copyright (c) 2005-2017
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
 * @author jpuente.ext
 */
public class DatIndiv extends DatIndivBase
{
 
    private Date fecCaduc, fecSacrif;
    private int prvCodi=0;
    private String auxiliar;
    int posFin;
    private int numLinea;

    public int getNumLinea() {
        return numLinea;
    }
    /**
     * Especifica datos De individuo Base.
     * @param dt 
     */
    public void setIndivBase(DatIndivBase dt)
    {
        setProducto(dt.getProducto());
        setEjercLot(dt.getEjercLot());
        setLote(dt.getLote());
        setSerie(dt.getSerie());        
        setNumind(dt.getNumind()) ;
    }
    public void setNumLinea(int numLinea) {
        this.numLinea = numLinea;
    }
    
    public void setProveedor(int codProveed)
    {
        prvCodi=codProveed;
    }
    public int  getProveedor()
    {
        return prvCodi;
    }
   
    public String getAuxiliar()
    {
        return auxiliar;
    }
    public void setAuxiliar(String auxiliar)
    {
        this.auxiliar=auxiliar;
    }
    public DatIndiv()
    {
        
    }
     public void setFechaCaducidad(Date fecCad)
    {
        fecCaduc=fecCad;
    }
    
    public Date getFechaCaducidad()
    {
        return fecCaduc;
    }
     public void setFechaSacrificio(Date fecSacr)
    {
        fecSacrif=fecSacr;
    }
    
    public Date getFechaSacrificio()
    {
        return fecSacrif;
    }
    /**
     * Almacena los datos de un individuo.
     * @param key datos  del individuo, separados por Pipes (|). 
     * @param valor Valores del individuo (Cantidad y Numero Indiv.) separados por pipes.
     */
    public DatIndiv(String key,String valor)
    {
       canti = Double.parseDouble(getCampoLlave(valor, 0));
       numuni = Integer.parseInt(getCampoLlave(valor, posFin));

       proCodi= Integer.parseInt(getCampoLlave(key, 0));
       //empCodi=Integer.parseInt(getCampoLlave(key, posFin));
       ejeNume=Integer.parseInt(getCampoLlave(key, posFin));
       serie=getCampoLlave(key, posFin);
       lote=Integer.parseInt(getCampoLlave(key, posFin));
       numind=Integer.parseInt(getCampoLlave(key, posFin));
       almCodi=Integer.parseInt(getCampoLlave(key, posFin));
    }
    
    private String getCampoLlave(String llave, int posIni)
    {
        posFin = llave.indexOf("|", posIni);
        if (posFin < 0)
          return llave.substring(posIni);
        else
          return llave.substring(posIni, posFin++);
    }
    
  
}