package gnu.chu.anjelica.sql;
/**
 *
 * <p>Título: DesporigId </p>
 * <p>Descripción: Almacena los datos de identificacion de una linea Origen de un despiece</p>
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
public class DesporigId  implements java.io.Serializable {


     private int ejeNume;
     private int deoCodi;

    public DesporigId() {
    }

    public DesporigId(int ejeNume, int deoCodi) {
       this.ejeNume = ejeNume;
       this.deoCodi = deoCodi;
    }
   
    public int getEjeNume() {
        return this.ejeNume;
    }
    
    public void setEjeNume(int ejeNume) {
        this.ejeNume = ejeNume;
    }
    public int getDeoCodi() {
        return this.deoCodi;
    }
    
    public void setDeoCodi(int deoCodi) {
        this.deoCodi = deoCodi;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof DesporigId) ) return false;
		 DesporigId castOther = ( DesporigId ) other; 
         
		 return (this.getEjeNume()==castOther.getEjeNume())
 && (this.getDeoCodi()==castOther.getDeoCodi());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getEjeNume();
         result = 37 * result + this.getDeoCodi();
         return result;
   }   


}


