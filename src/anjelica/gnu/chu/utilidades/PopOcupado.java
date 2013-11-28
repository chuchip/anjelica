package gnu.chu.utilidades;


/**
 *
 * <p>Título: popOcupado</p>
 * <p>Descripción: Clase que implementa un internalFrame con el texto Procesando.
*   y un grafico moviendose.</p>
 * <p>Copyright: Copyright (c) 2005-2012
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los terminos de la Licencia Pública General de GNU seg�n es publicada por
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
 * @author Chuchi P
 * @version 1.0
 */

import com.jgoodies.looks.plastic.PlasticInternalFrameUI;
import gnu.chu.controles.CInternalFrame;
import gnu.chu.controles.CLabel;
import gnu.chu.controles.CPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicInternalFrameUI;

public class PopOcupado extends CInternalFrame
{
  int nPos=0;
  CInternalFrame papaFrame;
  CPanel cPanel1 = new CPanel();
  CLabel textoL = new CLabel();
  
  CLabel imagen = new CLabel(Iconos.getImageIcon("ocupado.gif"));
 
  


    public PopOcupado( CInternalFrame framePadre)
    {
      papaFrame = framePadre;
      try
      {
        jbInit();
      }
      catch (Exception k)
      {
        k.printStackTrace();
      }
    }

    private void jbInit() throws Exception
    {
      cPanel1.setLayout(null);
      int ancho=(int)(papaFrame.getWidth()*.8);
      if (ancho<150)
          ancho=150;
      int alto=(int) (papaFrame.getHeight()*.8);
      if (alto<110)
          alto=110;
      this.setSize(new Dimension(ancho,alto));
//      this.setIconifiable(false);
//      this.setResizable(false);
//      this.setMaximizable(false);
//      this.setBorder(null);
      this.putClientProperty(
             PlasticInternalFrameUI.IS_PALETTE,
             Boolean.TRUE);
//      ((BasicInternalFrameUI)this.getUI()).setNorthPane(null); // Para quitar el titulo a la ventana
      textoL.setBackground(Color.yellow);
      textoL.setFont(new java.awt.Font("Dialog", 1, 13));
      textoL.setForeground(Color.blue);
      textoL.setBorder(BorderFactory.createRaisedBevelBorder());
      textoL.setDebugGraphicsOptions(0);
      textoL.setOpaque(true);
      textoL.setRequestFocusEnabled(true);
      textoL.setHorizontalAlignment(SwingConstants.CENTER);
      textoL.setText("Procesando");
      textoL.setBounds(new Rectangle((ancho/2)-60, alto>170?80:20, 120, 18));
   
      imagen.setBounds(new Rectangle(10,alto>170?120:45, ancho-30, 55));
    
     
      cPanel1.setBorder(BorderFactory.createRaisedBevelBorder());
      this.getContentPane().add(cPanel1, BorderLayout.CENTER);
      cPanel1.add(textoL, null);
      cPanel1.add(imagen, null);
   
      papaFrame.getLayeredPane().add(this, new Integer(1));
      this.setVisible(false);
    }

 
    public void mostrar()
    {
      mostrar(papaFrame);
    }
    public void mostrar(CInternalFrame papa)
    {
      this.papaFrame=papa;
      javax.swing.SwingUtilities.invokeLater(new Thread()
      {
            @Override
        public void run()
        {
          muestra();
        }
      });
    }

    private void muestra()
    {
      nPos=0;
      this.setVisible(true);
      if (papaFrame==null)
        return;
      papaFrame.getLocation();
      Dimension dimen= papaFrame.getSize();
      double x= (dimen.getWidth()/2) - (this.getWidth()/2);
      double y=(dimen.getHeight()/2)- (this.getHeight()/2);
      this.setLocation((int) x,(int)y);
      papaFrame.setEnabled(false);
//      this.pack();
    

    }
    public void oculta()
    {
      papaFrame.setEnabled(true);
      this.setVisible(false);
    }
   

}
