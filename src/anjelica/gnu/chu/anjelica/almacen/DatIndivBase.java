package gnu.chu.anjelica.almacen;

import java.util.Objects;

/**
  <p> Descripción: Clase para almacenar los datos de un individuo mandados a traves de 
 * HashMap.
 * Usada por actStkPart y PdInvControl, lisaldos</p>
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
public class DatIndivBase 
{
    public double canti;
    double precio;
    int proCodi,ejeNume,lote,numind,almCodi,numuni;    
    
    String serie;
   

  
//    public int getNumuni() {
//        return numuni;
//    }
//
//    public void setNumuni(int numuni) {
//        this.numuni = numuni;
//    }

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
    public void setPrecio(double precio)
    {
        this.precio=precio;
    }
    public double getPrecio()
    {
        return precio;
    }
    public DatIndivBase()
    {
        
    }
     
    @Override
    public boolean equals(Object obj) {
      if ( obj instanceof  DatIndivBase )
      {
          if (((DatIndivBase)obj).getProducto()==getProducto() &&
              ((DatIndivBase)obj).getEjercLot()==getEjercLot() &&
              ((DatIndivBase)obj).getLote()==getLote() && 
              ((DatIndivBase)obj).getSerie().equals(getSerie()) &&
              ((DatIndivBase)obj).getNumind()==getNumind() && 
              ((DatIndivBase)obj).getAlmCodi()==getAlmCodi())
              return true;
      }
      return false;
      
  }

    @Override
    public int hashCode() {
        int hash = 5;
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
   public int getNumuni()
    {
        return numuni;
    }
   public void setNumuni(int unidades)
   {
       numuni=unidades;
   }
}