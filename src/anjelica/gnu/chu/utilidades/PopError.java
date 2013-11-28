// Programador: Chuchi.
// Fecha: 7/5/98
// Version: 1.0

// Rutina generica que Lanza una ventana en MODO non MDI, con un
// Mensaje definido por el Usuario.

package gnu.chu.utilidades;


import javax.swing.event.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import gnu.chu.controles.*;
import gnu.chu.interfaces.*;
//import gnu.chu.

public class PopError
{
  private ejecutable prog;
  JDialog frame;
//  VXYLayout xYLayout2 = new VXYLayout();
	public CButton Bcancelar = new CButton("Cancelar", Iconos.getImageIcon("cancel"));
  public CButton Breintentar = new CButton(Iconos.getImageIcon("data-undo"));
  public CButton Listar = new CButton(Iconos.getImageIcon("print"));

  JTextArea Texto=new JTextArea();
  String msg="";
  JScrollPane jScrollPane1 = new JScrollPane();
  private int resul=CANCELAR;
  public final static int CANCELAR=0;
  public final static int REINTENTAR=1;
  public final static int IGNORAR=2;
  CLabel titulo1 = new CLabel();
  CLabel accionL = new CLabel();
  Frame miFrame;
  CPanel vPanel1 = new CPanel(null);

  GridBagLayout gridBagLayout1 = new GridBagLayout();

  public PopError(String msg)
  {
    this(msg,null);
  }

  public PopError(String msg, Frame vent)
  {
    miFrame = vent;
    try
    {
      this.msg = msg;
      jbInit();
    }
    catch (Exception k)
    {
      k.printStackTrace();
    }
  }

  public PopError()
  {
    this("");
  }

	/**
   * Component initialization
   */
  private void jbInit() throws Exception
  {
    frame = new JDialog(miFrame, "Error", true);
    frame.setSize(new Dimension(520, 460));

    //Center the window
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = frame.getSize();
    if (frameSize.height > screenSize.height)
      frameSize.height = screenSize.height;
    if (frameSize.width > screenSize.width)
      frameSize.width = screenSize.width;
    frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

    vPanel1.setLayout(gridBagLayout1);
    Listar.setMaximumSize(new Dimension(29, 30));
    Listar.setMinimumSize(new Dimension(29, 30));
    Listar.setPreferredSize(new Dimension(29, 30));
    Listar.setMargin(new Insets(0, 0, 0, 0));

    double i = Math.random();
    int x = (int)(i * 2) + 1;
    String icon = "error" + x;
    icon="obrasinf.gif";
/*    String hoy = Fecha.getFechaSys("dd-MM-yyyy");
    int ano = Formatear.strToInt(Fecha.getFechaSys("yyyy"));
    int mes = Formatear.strToInt(Fecha.getFechaSys("MM"));
    int dia = Formatear.strToInt(Fecha.getFechaSys("dd"));
    lblNav:
    if (mes == 1 || mes == 12) {
       if (mes == 12) {
          if (dia < 11)
             break lblNav;
       } else {
           if (dia > 7)
              break lblNav;
       }

       i = Math.random();
       x = (int)(i * 5) + 1;
       icon = "navidad" + x;
    }*/
    CLabel dibujo = new CLabel(Iconos.getImageIcon(icon));

//    dibujo.setMaximumSize(new Dimension(71, 79));
//    dibujo.setMinimumSize(new Dimension(71, 79));
//    dibujo.setPreferredSize(new Dimension(71, 79));
    titulo1.setMaximumSize(new Dimension(430, 13));
    titulo1.setMinimumSize(new Dimension(430, 13));
    titulo1.setPreferredSize(new Dimension(430, 13));
    accionL.setMaximumSize(new Dimension(430, 26));
    accionL.setMinimumSize(new Dimension(430, 26));
    accionL.setPreferredSize(new Dimension(430, 26));
    jScrollPane1.setMaximumSize(new Dimension(508, 300));
    jScrollPane1.setMinimumSize(new Dimension(508, 300));
    jScrollPane1.setPreferredSize(new Dimension(508, 300));
    Breintentar.setMaximumSize(new Dimension(142, 30));
    Breintentar.setMinimumSize(new Dimension(142, 30));
    Breintentar.setPreferredSize(new Dimension(142, 30));
    Breintentar.setMargin(new Insets(0, 0, 0, 0));
    Bcancelar.setMaximumSize(new Dimension(142, 30));
    Bcancelar.setMinimumSize(new Dimension(142, 30));
    Bcancelar.setPreferredSize(new Dimension(142, 30));
    frame.getContentPane().add(vPanel1,BorderLayout.CENTER);
    Texto.setBackground(new java.awt.Color(255, 255, 227));
    Texto.setFont(new Font("Dialog", 0, 12));
    Texto.setText(msg);
//   Texto.setCaretPosition(2);
    Texto.setEditable(false);
    titulo1.setText("Se ha Producido un Error en esta Aplicacion");
    titulo1.setHorizontalAlignment(0);
    titulo1.setForeground(Color.cyan);
    titulo1.setBackground(Color.red);
    titulo1.setOpaque(true);
    accionL.setOpaque(true);
    accionL.setBackground(Color.yellow);
    accionL.setHorizontalAlignment(0);
    accionL.setText("Avise a Proceso de Datos");
//    xYLayout2.setHeight(437);
    vPanel1.setDefButton(Bcancelar);
    vPanel1.setAltButton(Breintentar);

//    xYLayout2.setWidth(519);
    Bcancelar.setText("Cancelar");
    Bcancelar.setMargin(new Insets(0, 0, 0, 0));
    Bcancelar.setToolTipText("Cancela la Aplicaccion que produjo este Error");
    Bcancelar.setMnemonic('C');
    Breintentar.setText("Reintentar");
    Breintentar.setToolTipText("Reintenta ejecutar esta Instruccion");
    Breintentar.setMnemonic('R');
//    Breintentar.setEnabled(false);
    Listar.setToolTipText("Imprime en la salida Estandar este mensaje");

    vPanel1.add(Listar, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(3, 0, 3, 0), 0, 0));
    vPanel1.add(dibujo, new GridBagConstraints(0, 0, 1, 2, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(3, 4, 3, 2), 0, 0));
    vPanel1.add(jScrollPane1, new GridBagConstraints(0, 2, 3, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
    jScrollPane1.getViewport().add(Texto, null);
    vPanel1.add(accionL, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(3, 3, 3, 3), 0, 0));
    vPanel1.add(titulo1, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(6, 3, 3, 3), 0, 0));
    vPanel1.add(Bcancelar, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(3, 0, 3, 0), 0, 0));
    vPanel1.add(Breintentar, new GridBagConstraints(2, 3, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(3, 0, 3, 0), 0, 0));

    frame.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    frame.addWindowListener(new WindowAdapter()
    {
       public void windowClosing(WindowEvent e)
       {
         resul=CANCELAR;
         if (prog!=null)
           matar();
         setVisible(false);
       }
    });

    Bcancelar.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        resul = CANCELAR;
        if (prog != null)
          matar();
        setVisible(false);
      }
    });

    Breintentar.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        resul = REINTENTAR;
        setVisible(false);
        prog = null;
      }
    });

    Listar.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        System.out.println(msg);
      }
    });
  }

  public int getResultado()
  {
    return resul;
  }

  public void setEjecutable(ejecutable v)
  {
    prog=v;
  }

  public void setMsgAccion(String msg)
  {
    accionL.setText(msg);
  }
  public void setVisible(boolean visible)
  {
    frame.setVisible(visible);
    if (! visible)
      frame.dispose();
  }
  void matar()
  {
    prog.matar();
    prog = null;
/*    new miThread("")
    {
      public void run()
      {
        try {
          this.sleep(2000);
        } catch (Exception k){}
        prog.matar();
        prog=null;
      }
    };*/
  }
}
