package gnu.chu.utilidades;


/**
 *
 * <p>Titulo: FloatDimension </p>
* <p>Descripcion: Clase tonta para emular a Dimension pero con float</p>
*
 * <p>Copyright: Copyright (c) 2005-2018
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
* @author Chuchi P
* @version 1.0
*/
public class FloatDimension
{
    double valor1;

    public double getValor1() {
        return valor1;
    }

    public void setValor1(double valor1) {
        this.valor1 = valor1;
    }

    public double getValor2() {
        return valor2;
    }

    public void setValor2(double valor2) {
        this.valor2 = valor2;
    }
    double valor2;
    public FloatDimension(double valor1, double valor2)
    {
        this.valor1=valor1;
        this.valor2=valor2;
    }
    public void setValores(double valor1,double valor2)
    {
        this.valor1=valor1;
        this.valor2=valor2;
    }
    
}
