package gnu.chu.anjelica.almacen;

import java.util.Objects;

/**
  <p> Descripción: Clase para almacenar los datos de un individuo mandados a traves de 
 * HashMap.
 * Usada por actStkPart y PdInvControl</p>
 * <p>Copyright: Copyright (c) 2005-2013
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
public class DatIndiv 
{
    public double canti;
    public int numuni,proCodi,ejeNume,lote,numind,almCodi;
    public String serie;
    private String auxiliar;
    int posFin;
    private int numLinea;

    public int getNumLinea() {
        return numLinea;
    }

    public void setNumLinea(int numLinea) {
        this.numLinea = numLinea;
    }
    
    public int getNumuni() {
        return numuni;
    }

    public void setNumuni(int numuni) {
        this.numuni = numuni;
    }

    public int getProducto() {
        return proCodi;
    }

    public void setProducto(int proCodi) {
        this.proCodi = proCodi;
    }



    public int getEjercLot() {
        return ejeNume;
    }

    public void setEjercLot(int ejeNume) {
        this.ejeNume = ejeNume;
    }

    public int getLote() {
        return lote;
    }

    public void setLote(int lote) {
        this.lote = lote;
    }

    public int getNumind() {
        return numind;
    }

    public void setNumind(int numind) {
        this.numind = numind;
    }

    public int getAlmCodi() {
        return almCodi;
    }

    public void setAlmCodi(int almCodi) {
        this.almCodi = almCodi;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }
    
    public void setCanti(double canti)
    {
        this.canti=canti;
    }
    public double getCanti()
    {
        return canti;
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
    
    @Override
  public boolean equals(Object obj) {
      if ( obj instanceof  DatIndiv )
      {
          if (((DatIndiv)obj).getProducto()==getProducto() &&
              ((DatIndiv)obj).getEjercLot()==getEjercLot() &&
              ((DatIndiv)obj).getLote()==getLote() && 
              ((DatIndiv)obj).getSerie().equals(getSerie()) &&
              ((DatIndiv)obj).getNumind()==getNumind() && 
              ((DatIndiv)obj).getAlmCodi()==getAlmCodi())
              return true;
      }
      return false;
      
  }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + this.numuni;
        hash = 89 * hash + this.proCodi;
        hash = 89 * hash + this.ejeNume;
        hash = 89 * hash + this.lote;
        hash = 89 * hash + this.numind;
        hash = 89 * hash + this.almCodi;
        hash = 89 * hash + Objects.hashCode(this.serie);
        return hash;
    }

   
    @Override
  public String toString()
  {
          return getAlmCodi()+" "+getProducto() + 
              getEjercLot()+ 
              getSerie() +
              getLote() +"-"+ 
              getNumind();
  }
}