package gnu.chu.utilidades;

import gnu.chu.controles.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;


public class getMsgTexto extends CInternalFrame
{
  public String respuesta;
  ventana ven;
  CPanel cPanel1 = new CPanel();
  CLabel msgE = new CLabel();
  JScrollPane jScrollPane1 = new JScrollPane();
  JTextArea respuestE = new JTextArea();
  CButton Baceptar = new CButton("Aceptar",Iconos.getImageIcon("check"));
  CButton Bcancelar = new CButton("Cancelar",Iconos.getImageIcon("cancel"));
  GridBagLayout gridBagLayout1 = new GridBagLayout();

  public getMsgTexto(JLayeredPane vl,String msg,String titulo,String respuesta,ventana ven)
  {
    this.ven=ven;
    ven.setEnabled(false);
    msgE.setText(msg);

    this.respuesta=respuesta;
    respuestE.setText(respuesta);
    vl.add(this,new Integer(1));
    this.setTitle(titulo);
    try
    {
      jbInit();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

  }

  private void jbInit() throws Exception {
    this.setClosable(false);
    cPanel1.setLayout(gridBagLayout1);
    msgE.setBackground(Color.blue);
    msgE.setForeground(SystemColor.activeCaptionText);
    msgE.setMaximumSize(new Dimension(381, 16));
    msgE.setMinimumSize(new Dimension(381, 16));
    msgE.setOpaque(true);
    msgE.setPreferredSize(new Dimension(381, 16));
    msgE.setHorizontalAlignment(SwingConstants.CENTER);
    msgE.setHorizontalTextPosition(SwingConstants.CENTER);
    Baceptar.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Baceptar_actionPerformed(e);
      }
    });

    Bcancelar.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Bcancelar_actionPerformed(e);
      }
    });
    cPanel1.setMaximumSize(new Dimension(32767, 32767));
    jScrollPane1.setMaximumSize(new Dimension(379, 205));
    jScrollPane1.setMinimumSize(new Dimension(379, 205));
    jScrollPane1.setPreferredSize(new Dimension(379, 205));
    Baceptar.setMaximumSize(new Dimension(112, 22));
    Baceptar.setMinimumSize(new Dimension(112, 22));
    Baceptar.setPreferredSize(new Dimension(112, 22));
    Bcancelar.setMaximumSize(new Dimension(112, 22));
    Bcancelar.setMinimumSize(new Dimension(112, 22));
    Bcancelar.setPreferredSize(new Dimension(112, 22));
    this.getContentPane().add(cPanel1, BorderLayout.CENTER);
    cPanel1.add(msgE,    new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 0, 6), 0, 0));
    cPanel1.add(jScrollPane1,   new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 2, 0, 2), 0, 0));
    jScrollPane1.getViewport().add(respuestE, null);
    cPanel1.add(Baceptar,   new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 6, 4, 0), 0, 0));
    cPanel1.add(Bcancelar,    new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 4, 6), 0, 0));
    this.setSize(new Dimension(403, 300));
    this.setVisible(true);
  }

  void Baceptar_actionPerformed(ActionEvent e) {
    respuesta=respuestE.getText();
    matar();

  }
  void matar()
  {
    ven.setEnabled(true);
    try
    {
      setClosed(true);
      ven.setSelected(true);
    }
    catch (Exception k)
    {}
    processEvent(new InternalFrameEvent(this,
                                        InternalFrameEvent.
                                        INTERNAL_FRAME_CLOSING));
  }
  void Bcancelar_actionPerformed(ActionEvent e) {
    respuesta=null;
    matar();
  }
}
