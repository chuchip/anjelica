package gnu.chu.anjelica.despiece;

import gnu.chu.controles.*;
import gnu.chu.utilidades.SystemOut;
import java. awt.*;
/**
 * <p>Titulo:   datTraPanel </p>
 * <p>Descripción: Panel con campos para mostrar los datos de trazabilidad
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
 * <p>Empresa: MISL</p>
 * @author chuchiP
 * 
 */

public class datTraPanel    extends CPanel
{
  CTextField nacidoE = new CTextField();
  CLabel textoL = new CLabel();
  CTextField sacrificadoE = new CTextField();
  CLabel cLabel4 = new CLabel();
  CTextField ntrazaE = new CTextField();
  CLabel cLabel11 = new CLabel();
  CTextField cebadoE = new CTextField();
  CLabel cLabel3 = new CLabel();
  CLabel cLabel10 = new CLabel();
  CTextField fecrecepE = new CTextField();
  CTextField conservarE = new CTextField();
  CTextField despiezadoE = new CTextField();
  CLabel cebadoL = new CLabel();
  CLabel fecrepL = new CLabel();


  public datTraPanel()
  {
    try
   {
     jbInit();
   }
   catch (Exception e)
   {
     SystemOut.print(e);
   }
  }

  private void jbInit() throws Exception
  {

   cLabel11.setBounds(new Rectangle(4, 62, 85, 18));
   cLabel11.setText("N. Trazabilidad");
   ntrazaE.setBounds(new Rectangle(86, 62, 303, 16));
   ntrazaE.setSelectedTextColor(Color.black);
   cLabel4.setBounds(new Rectangle(4, 24, 83, 16));
   cLabel4.setText("Sacrificado en");
   sacrificadoE.setBounds(new Rectangle(86, 24, 303, 16));
   sacrificadoE.setAlignmentY((float) 0.5);
   textoL.setBounds(new Rectangle(4, 97, 82, 16));
   textoL.setText("Conservar");
   nacidoE.setBounds(new Rectangle(86, 5, 147, 16));
   cebadoE.setBounds(new Rectangle(302, 5, 147, 16));
   cLabel3.setText("Nacido en");
   cLabel3.setBounds(new Rectangle(4, 5, 59, 16));
   cLabel10.setText("Despiezado en");
   cLabel10.setBounds(new Rectangle(4, 42, 84, 20));
   fecrecepE.setBounds(new Rectangle(86, 79, 207, 16));
   conservarE.setBounds(new Rectangle(86, 97, 303, 16));
   despiezadoE.setBounds(new Rectangle(86, 44, 303, 16));
   cebadoL.setText("Cebado en");
   cebadoL.setBounds(new Rectangle(236, 5, 63, 16));
   fecrepL.setText("Fecha Recep:");
   fecrepL.setBounds(new Rectangle(4, 79, 82, 16));
   this.setLayout(null);

   this.add(cLabel10, null);
   this.add(despiezadoE, null);
   this.add(ntrazaE, null);
   this.add(cLabel11, null);
   this.add(fecrecepE, null);
   this.add(fecrepL, null);
   this.add(conservarE, null);
   this.add(textoL, null);
   this.add(cebadoE, null);
   this.add(cLabel3, null);
   this.add(nacidoE, null);
   this.add(cebadoL, null);
   this.add(sacrificadoE, null);
   this.add(cLabel4, null);
 }

}
