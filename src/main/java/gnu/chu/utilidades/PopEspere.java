package gnu.chu.utilidades;


/**
 *
 * <p>Título: popEspere</p>
 * <p>Descripción: Clase que implementa un internalFrame con el texto espere, por favor...
*   y un grafico moviendose.</p>
 * <p>Copyright: Copyright (c) 2005-2014
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
 * @author Chuchi P
 * @version 1.0
 */

//import com.jgoodies.looks.plastic.PlasticInternalFrameUI;
import gnu.chu.controles.CButton;
import gnu.chu.controles.CInternalFrame;
import gnu.chu.controles.CLabel;
import gnu.chu.controles.CPanel;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class PopEspere extends CInternalFrame
{
  int nPos=0;
  CInternalFrame papaFrame;
  String msg;
  CPanel cPanel1 = new CPanel();
  JScrollPane jScrollPane1 = new JScrollPane();
  CLabel textoL = new CLabel();
  JTextArea msgE = new JTextArea();
  CLabel imagen = new CLabel(Iconos.getImageIcon("ocupado2.gif"));
  CButton BCancelar = new CButton("Cancelar",Iconos.getImageIcon("cancel"));

  public PopEspere(String msg)
    {
      this(msg,null);
    }

    public PopEspere(String msg, CInternalFrame framePadre)
    {
      papaFrame = framePadre;
      try
      {
        this.msg = msg;
        jbInit();
      }
      catch (Exception k)
      {
       SystemOut.print(k);
      }
    }

    public PopEspere()
    {
      this("");
    }

    private void jbInit() throws Exception
    {
      cPanel1.setLayout(null);
      this.setSize(new Dimension(385, 180));
//      this.setIconifiable(false);
//      this.setResizable(false);
//      this.setMaximizable(false);
//      this.setBorder(null);
      this.setVisibleCabeceraVentana(false);
//      ((BasicInternalFrameUI)this.getUI()).setNorthPane(null); // Para quitar el titulo a la ventana
      textoL.setBackground(Color.yellow);
      textoL.setFont(new java.awt.Font("Dialog", 1, 13));
      textoL.setForeground(Color.blue);
      textoL.setBorder(BorderFactory.createRaisedBevelBorder());
      textoL.setDebugGraphicsOptions(0);
      textoL.setOpaque(true);
      textoL.setRequestFocusEnabled(true);
      textoL.setHorizontalAlignment(SwingConstants.CENTER);
      textoL.setText("Espere, por favor ...");
      textoL.setBounds(new Rectangle(114, 5, 136, 20));
      msgE.setEnabled(true);
      msgE.setEditable(false);
      msgE.setText("");
      
      imagen.setBounds(new Rectangle(7, 105, 361, 30));
      BCancelar.setBounds(new Rectangle(115, 138, 131, 30));
      BCancelar.setSelected(false);
      BCancelar.setText("Cancelar");
      BCancelar.setEnabled(false);
      jScrollPane1.setBounds(new Rectangle(7, 28, 360, 70));
      cPanel1.setBorder(BorderFactory.createRaisedBevelBorder());
      this.getContentPane().add(cPanel1, BorderLayout.CENTER);
      cPanel1.add(jScrollPane1, null);
      cPanel1.add(textoL, null);
      jScrollPane1.getViewport().add(msgE, null);
      cPanel1.add(imagen, null);
      cPanel1.add(BCancelar, null);
      papaFrame.getLayeredPane().add(this, new Integer(1));
      this.setVisible(false);
    }

    public void BCancelar_addActionListener(ActionListener actList)
    {
      BCancelar.setEnabled(actList!=null);
      BCancelar.addActionListener(actList);

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
//      new miThread("")
//      {
//            @Override
//        public void run()
//        {
//          try {
//            while (PopEspere.this.isVisible())
//            {
//                Thread.sleep(1000);
//                javax.swing.SwingUtilities.invokeAndWait(new Thread()
//                {
//                            @Override
//                    public void run()
//                    {
//                        actualizaMsg();
//                    }
//                });
//            }
//          } catch (Exception k)
//          {}
//        }
//      };

    }
    public void oculta()
    {
      papaFrame.setEnabled(true);
      this.setVisible(false);
    }
    public void setMensaje(String msg)
    {
      msgE.setText(msg);
    }

    public String getMensaje()
    {
      return msgE.getText();
    }
    public boolean isBCancelarEnabled()
    {
        return BCancelar.isEnabled();
    }
    /**
     * Función llamada por el Thread que actualiza la barra de progreso.
     */
    public void actualizaMsg()
    {

    }
    public void setTextoMsgEspere(String msg)
    {
        textoL.setText(msg);
    }
}
