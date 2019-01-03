package gnu.chu.anjelica.sql;
/**
 * <p>Titulo:   Albacoc </p>
 * <p>Descripción: Clase con referencia a tabla Albaranes de Compras
 * <p>Copyright: (c) 2005-2012
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
*  los términos de la Licencia Pública General de GNU según es publicada por
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
 * <p>Empresa: MISL</p>
 * @author chuchiP
 *
 */
public class Albacoc 
{
    int accNume;

    public int getAccAno() {
        return accAno;
    }

    public int getAccNume() {
        return accNume;
    }

    public String getAccSerie() {
        return accSerie;
    }
    int accAno;
    String accSerie;
    
    
    public Albacoc(int accNume,int accAno, String accSerie)
    {
     
        this.accNume=accNume;
        this.accAno=accAno;
        this.accSerie=accSerie;
    }
    @Override
    public String toString()
    {
        return accAno+accSerie+accNume;
    }
    @Override
    public boolean equals(Object obj) {
        return obj.toString().equals(toString());
    }
}