package gnu.chu.controles;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import gnu.chu.utilidades.*;
import gnu.chu.controles.*;
import gnu.chu.interfaces.*;

/**
 *
 * <p>Título: StatusBar</p>
 * <p>Descripción: Barra utilizada para dar mensajes y salir de los programas
 *  </p>
 * <p>Copyright: Copyright (c) 2005-2009
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
 * @author chuchi P.
 * @version 1.0
 */

public class StatusBar  extends CPanel
{
  public boolean salido=false;
  CLabel textErr = new CLabel(" ");
  public CLabel texto = new CLabel(" ");
  BevelBorder bb1 = new BevelBorder(BevelBorder.RAISED);
  public CButton Bsalir = new CButton("Salir",Iconos.getImageIcon("salir"));
  ejecutable vc;
  GridBagLayout gridBagLayout1 = new GridBagLayout();

  private boolean swLimpia = false;
  private Timer timer = new Timer(5000, new ActionListener() {
    public void actionPerformed(ActionEvent e) {
      setText("");
      if (timer != null)
        timer.stop();
    }
  });
  private Timer timerErr = new Timer(5000, new ActionListener() {
    public void actionPerformed(ActionEvent e) {
      setTextErr("", false);
      if (timerErr != null)
        timerErr.stop();
    }
  });
  private Timer timerRapido = new Timer(1500, new ActionListener() {
     public void actionPerformed(ActionEvent e) {
       setTextErr("", false);
       if (timerRapido != null)
         timerRapido.stop();
     }
   });

  public StatusBar() {
    this(null);
  }

  public StatusBar(ejecutable v) {

    vc = v;
    try {
      jbInit();
    }
    catch (Exception k) {
      k.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    Bsalir.setFocusable(false);

    this.setLayout(gridBagLayout1);
    this.setBorder(bb1);
    Bsalir.setPreferredSize(new Dimension(70, 24));
    Bsalir.setMaximumSize(Bsalir.getPreferredSize());
    Bsalir.setMinimumSize(Bsalir.getPreferredSize());
    Bsalir.setMargin(new Insets(0, 0, 0, 0));
    textErr.setBackground(Color.red);
    textErr.setForeground(SystemColor.info);
    textErr.setOpaque(true);
    textErr.setPreferredSize(new Dimension(3, 12));
    texto.setMaximumSize(new Dimension(1, 12));
    texto.setMinimumSize(new Dimension(3, 12));
    texto.setPreferredSize(new Dimension(3, 12));
    textErr.setMaximumSize(new Dimension(1, 12));
    textErr.setMinimumSize(new Dimension(3, 12));
    this.add(texto, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
                                           , GridBagConstraints.EAST,
                                           GridBagConstraints.BOTH,
                                           new Insets(0, 2, 0, 2), 0, 0));
    this.add(textErr, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
                                             , GridBagConstraints.EAST,
                                             GridBagConstraints.BOTH,
                                             new Insets(0, 2, 0, 2), 0, 0));
    this.add(Bsalir, new GridBagConstraints(10, 0, 1, 2, 0.0, 0.0
                                            , GridBagConstraints.EAST,
                                            GridBagConstraints.VERTICAL,
                                            new Insets(0, 5, 0, 0), 0, 0));

    texto.setForeground(Color.blue);
    Bsalir.setMnemonic('S');
//    textErr.setVisible(false);

    if (vc != null) {
      Bsalir.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if (vc != null)
            salido=true;
            vc.matar();
        }
      });
    }
  }

  public void setText(String s) {
    setText(s, false);
  }

  public String getText() {
    return texto.getText();
  }

  public void setLimpiaMensaje(boolean limpia) {
    swLimpia = limpia;
    if (!limpia)
      timer.stop();
  }

  public boolean getLimpiaMensaje() {
    return swLimpia;
  }

  public void setText(String s, boolean b) {
    if (swLimpia) {
      if (timer != null)
        timer.stop();
    }
    texto.setText(s);
    if (b)
      sonido();
    if (swLimpia) {
      if (!s.equals(""))
        if (timer != null)
          timer.start();
    }
  }
 private void sonido()
 {
   this.getToolkit().beep();
 }
  public void setTextErr(String s) {
    setTextErr(s, true);
  }

  public void setTextErr(String s, boolean sonido) {
    if (timerErr == null)
      return;
    if (textErr == null)
      return;
    timerErr.stop();
    textErr.setText(s);
    if (sonido)
      this.getToolkit().beep();
    if (!s.equals("")) {
      timerErr.start();
    }
  }

  public void setMsgRapido(String s) {
    if (timerRapido == null)
      return;
    if (textErr == null)
      return;
    timerRapido.stop();
    textErr.setText(s);
    if (!s.equals(""))
      timerRapido.start();
  }

  public String getTextErr() {
    return textErr.getText();
  }

  public void setEnabled(boolean t) {
    if (vc!=null)
      vc.setClosable(t);
    Bsalir.setEnabled(t);
  }

  public void setEnabledSuper(boolean enab) {
    super.setEnabled(enab);
  }

  public void dispose() {
    timer = null;
    timerErr = null;
  }
} // Fin de Clase.
