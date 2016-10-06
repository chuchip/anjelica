package gnu.chu.utilidades;

import com.toedter.calendar.JCalendar;
import  java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import gnu.chu.controles.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.Locale;
import javax.swing.border.BevelBorder;
/**
 *
 * <p>Titulo: mensajes </p>
 * <p>Descripción: Clase con funciones estaticas para mostrar diferentes
 * tipos de mensajes en ventanicas modal. ;)</p>
 * <p>Copyright: Copyright (c) 2005-2010
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los terminos de la Licencia Pública General de GNU según es publicada por
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
 * @author chuchiP
 */
public class mensajes {

  final public static int CANCEL = JOptionPane.CANCEL_OPTION;
  final public static int OK = JOptionPane.OK_OPTION;
  final public static int YES = JOptionPane.YES_OPTION;
  final public static int NO = JOptionPane.NO_OPTION;
  static int resul=CANCEL;
  static Date fecha;

  private boolean swAceptar = false;

  static public int mensajePreguntar(String msg) {
    return mensajePreguntar(msg, null);
  }
  /**
   * Muestra una ventana para preguntar sobre algo
   * @param msg Mensaje a mostrar
   * @param comp Ventana Padre
   * @return OK o CANCEL
   */
  static public int mensajePreguntar(String msg, Component comp) {
    return JOptionPane.showConfirmDialog(comp, msg, "¡¡ Aviso !!", JOptionPane.OK_CANCEL_OPTION,
                                         JOptionPane.QUESTION_MESSAGE);
  }
  static public int mensajeYesNo(String msg) {
    return mensajeYesNo(msg, null);
  }
  static public int mensajeYesNo(String titulo,String msg, Component comp) {
         int i = JOptionPane.showConfirmDialog(comp, msg, titulo, JOptionPane.YES_NO_OPTION,
                                         JOptionPane.QUESTION_MESSAGE);
         return i;
  }
  static public int mensajeYesNo(String msg, Component comp) {
       return mensajeYesNo("¡¡Atencion!!",msg,comp);
  }
  static public int mensajeYesNoCancel(String msg) {
    return mensajeYesNoCancel(msg, null);
  }
  static public int mensajeYesNoCancel(String msg, Component comp) {
      int i= JOptionPane.showConfirmDialog(comp,msg,"¡¡ Aviso !!",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
   return i;
  }
  static public void mensajeAviso(String msg) {
    mensajeAviso(msg, null);
  }
  static public void mensajeAviso(String msg, Component comp) {
    JOptionPane.showMessageDialog(comp, msg, "¡¡  Aviso !!", JOptionPane.INFORMATION_MESSAGE);
  }
  /**
   * Muestra un mensaje en una textarea, no editable.
   * Para mostrar mensajes largos.
   * @param title Titulo
   * @param msg mensaje
   */
  static public void mensajeExplica(String title,String msg)
  {
//    BevelBorder bv = new BevelBorder(BevelBorder.RAISED);
    final javax.swing.JDialog JDialog1 = new javax.swing.JDialog((Frame) null,true);
    CPanel CPanel1 = new CPanel();

    CTextArea explicL = new CTextArea();
    CButton Baceptar = new CButton("Aceptar", Iconos.getImageIcon("check"));

    CScrollPane jScrollPane1 = new CScrollPane();
    JDialog1.setSize(485,225);

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = JDialog1.getSize();
    if (frameSize.height > screenSize.height)
      frameSize.height = screenSize.height;
    if (frameSize.width > screenSize.width)
      frameSize.width = screenSize.width;
    JDialog1.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

    explicL.setEditable(false);
    CPanel1.setLayout(null);

    Baceptar.setMnemonic('A');
    Baceptar.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        JDialog1.setVisible(false);
      }
    });

    JDialog1.getContentPane().add(CPanel1, BorderLayout.CENTER);

    Baceptar.setBounds(new Rectangle(174, 159, 115, 31));

    jScrollPane1.setBounds(new Rectangle(4, 32, 470, 120));

    CPanel1.add(Baceptar, null);

    CPanel1.add(jScrollPane1, null);
    jScrollPane1.getViewport().add(explicL, null);

    JDialog1.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    JDialog1.addWindowListener(new WindowAdapter()
    {
            @Override
       public void windowClosing(WindowEvent e)
       {
         resul=CANCEL;
         JDialog1.setVisible(false);
       }
    });
    JDialog1.setTitle(title);

    explicL.setText(msg);
    JDialog1.setVisible(true);
  }
  /**
  * @author Chuchi P 23/4/99
  * Muestra un Dialog Box donde pide que se se le introduzca un Mensaje.
  * @param Title Titulo a Poner al DialogBox
  * @param msg Mensaje que aparecera encima del texto a teclear
  * @param explic Texto que aparecera por defecto para poder modificarlo.
  *
  * @return Si pulsa CANCELAR Devuelve NULL
  *         Si pulsa Aceptar devuelve el Texto Seleccionado
  */
  static public String mensajeExplica(String title,String msg,String explic)
  {

    BevelBorder bv = new BevelBorder(BevelBorder.RAISED);
    final javax.swing.JDialog JDialog1 = new javax.swing.JDialog((Frame) null,true);
    CPanel CPanel1 = new CPanel();
//    VXYLayout xYLayout1 = new VXYLayout();
    CLabel msgL = new CLabel();
    JTextArea explicL = new JTextArea();
    CButton Baceptar = new CButton("Aceptar", Iconos.getImageIcon("check"));
    CButton Bcancelar =  new CButton("Cancelar", Iconos.getImageIcon("cancel"));
    JScrollPane jScrollPane1 = new JScrollPane();
    JDialog1.setSize(485,225);

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = JDialog1.getSize();
    if (frameSize.height > screenSize.height)
      frameSize.height = screenSize.height;
    if (frameSize.width > screenSize.width)
      frameSize.width = screenSize.width;
    JDialog1.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);


    CPanel1.setLayout(null);
    msgL.setBorder(bv);
    Baceptar.setMnemonic('A');
    Baceptar.addActionListener(new java.awt.event.ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        resul=OK;
        JDialog1.setVisible(false);
      }
    });
    Bcancelar.setMnemonic('C');
    Bcancelar.addActionListener(new java.awt.event.ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        resul=CANCEL;
        JDialog1.setVisible(false);
      }
    });
    JDialog1.getContentPane().add(CPanel1, BorderLayout.CENTER);
    Bcancelar.setBounds(new Rectangle(280,159,115,31));
    Baceptar.setBounds(new Rectangle(94, 159, 115, 31));
    msgL.setBounds(new Rectangle(4, 6, 470, 23));
    jScrollPane1.setBounds(new Rectangle(4, 32, 470, 120));

    CPanel1.add(Bcancelar, null);
    CPanel1.add(Baceptar, null);
    CPanel1.add(msgL, null);
    CPanel1.add(jScrollPane1, null);
    jScrollPane1.getViewport().add(explicL, null);

    JDialog1.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    JDialog1.addWindowListener(new WindowAdapter()
    {
            @Override
       public void windowClosing(WindowEvent e)
       {
         resul=CANCEL;
         JDialog1.setVisible(false);
       }
    });
    JDialog1.setTitle(title);
    msgL.setText(msg);
    explicL.setText(explic);
    JDialog1.setVisible(true);
    if (resul==CANCEL)
      return null;
    else
      return explicL.getText();
  }

  static public void mensajeUrgente(String msg) {
    mensajeAviso(msg, null);
  }
  static public void mensajeUrgente(String msg, Component comp) {
    JOptionPane.showMessageDialog(comp, msg, "¡¡ Error Critico!!", JOptionPane.ERROR_MESSAGE);
  }

  /**
  * Mensaje con un TextField Para entrada de una cadena por parte del usuario
  * @param String msg    -> Mensaje de peticinn
  * @param String title  -> Tntulo de la ventana
  * @param Component comp-> Sobre el que echo la ventana
  * @return null Si el usuario <i>Cancela</i>
  *         String introducida por el usuario si <i>Acepta</i>
  */
  static public String mensajeGetTexto(String msg,String title,Component comp,String valor,String[] lstValor){
    Object o = JOptionPane.showInputDialog(comp,msg,title,JOptionPane.QUESTION_MESSAGE,Iconos.getImageIcon("tip"),lstValor,valor);
    if (o == null)
       return null;
    return o.toString();
  }
  static public String mensajeGetTexto(String msg,String title,Component comp,String valor){
    return mensajeGetTexto(msg,title,comp,valor,null);
  }
  static public String mensajeGetTexto(String msg,String title,Component comp){
    return mensajeGetTexto(msg,title,comp,"");
  }
  static public String mensajeGetTexto(String msg,String title){
    return mensajeGetTexto(msg,title,null);
  }
  /**
  * Mensaje con un TextField Para entrada de una cadena por parte del usuario
  * @param String msg    -> Mensaje de peticinn
  * @param String title  -> Tntulo de la ventana
  * @param Component comp-> Sobre el que echo la ventana
  * @return null Si el usuario <i>Cancela</i>
  *         String introducida por el usuario si <i>Acepta</i>
  */
  static public String mensajeGetTexto2(String msg,String title,Component comp,String valor,String[] lstValor){
    Object o = JOptionPane.showInternalInputDialog(comp,msg,title,JOptionPane.PLAIN_MESSAGE,Iconos.getImageIcon("tip"),lstValor,valor);
    if (o == null)
       return null;
    return o.toString();
  }
  static public String mensajeGetTexto2(String msg,String title,Component comp,String valor){
    return mensajeGetTexto2(msg,title,comp,"",null);
  }
  static public String mensajeGetTexto2(String msg,String title,Component comp){
    return mensajeGetTexto2(msg,title,comp,"");
  }
  static public String mensajeGetTexto2(String msg,String title){
    return mensajeGetTexto2(msg,title,null);
  }
  /**
  * @author Angel J. Apellaniz - 19/02/2001
  * Muestra un Dialogo con una pequena Ayuda
  *
  * @param Title Titulo a Poner al DialogBox
  * @param msg Mensaje que aparecera encima del texto a teclear
  * @param explic Texto que aparecera por defecto para poder modificarlo.
  */
  static public void mensajeAyuda(String title,String msg,String explic) {
         mensajeAyuda(new Dimension(400,320), title, msg, explic);
  }
  /**
  * @author Angel J. Apellaniz - 19/02/2001
  * Muestra un Dialogo con una pequena Ayuda
  *
  * @param Dimension tamano del Dialogo
  * @param Title Titulo a Poner al DialogBox
  * @param msg Mensaje que aparecera encima del texto a teclear
  * @param explic Texto que aparecera por defecto para poder modificarlo.
  */
  static public void mensajeAyuda(Dimension d, String title,String msg,String explic) {
    final JDialog JDialog1 = new JDialog((JFrame) null,true);
    CPanel CPanel1 = new CPanel(new GridBagLayout());
    CLabel imagen = new CLabel(Iconos.getImageIcon("logo"));
    CLabel msgL = new CLabel();
    JScrollPane jScrollPane1 = new JScrollPane();
    JTextArea explicL = new JTextArea();
    CButton Baceptar = new CButton("Aceptar", Iconos.getImageIcon("check"));

    JDialog1.setSize(d);
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = JDialog1.getSize();
    if (frameSize.height > screenSize.height)
      frameSize.height = screenSize.height;
    if (frameSize.width > screenSize.width)
      frameSize.width = screenSize.width;
    JDialog1.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    JDialog1.setTitle(title);

    msgL.setBackground(Color.blue);
    msgL.setFont(new java.awt.Font("Dialog", 1, 14));
    msgL.setForeground(Color.white);
    msgL.setBorder(BorderFactory.createRaisedBevelBorder());
    msgL.setMaximumSize(new Dimension(440, 30));
    msgL.setMinimumSize(new Dimension(440, 30));
    msgL.setOpaque(true);
    msgL.setPreferredSize(new Dimension(440, 30));
    msgL.setHorizontalAlignment(SwingConstants.CENTER);
    msgL.setText(msg);

    explicL.setText(explic);
    explicL.setEditable(false);

    Baceptar.setMaximumSize(new Dimension(115, 31));
    Baceptar.setMinimumSize(new Dimension(115, 31));
    Baceptar.setPreferredSize(new Dimension(115, 31));
    Baceptar.setMargin(new Insets(0, 0, 0, 0));
    Baceptar.setMnemonic('A');
    Baceptar.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) { JDialog1.setVisible(false); }
    });
    imagen.setBackground(Color.white);
    imagen.setBorder(BorderFactory.createRaisedBevelBorder());
    imagen.setMaximumSize(new Dimension(30, 30));
    imagen.setMinimumSize(new Dimension(30, 30));
    imagen.setOpaque(true);
    imagen.setPreferredSize(new Dimension(30, 30));
    jScrollPane1.setBorder(BorderFactory.createRaisedBevelBorder());
    jScrollPane1.setMaximumSize(new Dimension(470, 115));
    jScrollPane1.setMinimumSize(new Dimension(470, 115));
    jScrollPane1.setPreferredSize(new Dimension(470, 115));
    JDialog1.getContentPane().add(CPanel1, BorderLayout.CENTER);
    CPanel1.add(msgL, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 2, 0, 5), 0, 0));
    CPanel1.add(jScrollPane1, new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 5, 0, 5), 0, 0));
    CPanel1.add(imagen, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 0), 0, 0));
    CPanel1.add(Baceptar, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 0, 2, 0), 0, 0));
    jScrollPane1.getViewport().add(explicL, null);

    JDialog1.setVisible(true);
  }
  /**
   * Visualiza un Internal frame con el Aviso indicado
   */
  public void aviso(JFrame frame, JInternalFrame desactivar, String msgCab, String msg, Component foco) {
         aviso(frame, desactivar, msgCab, msg, foco, new Dimension(400,320));
  }
  /**
   * Visualiza un Internal frame con el Aviso indicado
   */
  public void aviso(JFrame frame, JInternalFrame desactivar, String msgCab, String msg, Component foco, Dimension d) {
        JDialog JDialog1 = getDialogo(frame, "�� Aviso !!", msgCab, msg, false, d);
        if (desactivar != null)
           desactivar.setEnabled(false);
        JDialog1.setVisible(true);

        JDialog1.dispose();
        if (desactivar != null)
           desactivar.setEnabled(true);
        if (foco != null)
           foco.requestFocus();
  }
  /**
   * Visualiza un Internal frame con el Aviso indicado
   */
  public boolean preguntar(JFrame frame, JInternalFrame desactivar, String msgCab, String msg, Component foco) {
         return preguntar(frame, desactivar, msgCab, msg, foco, new Dimension(400,320));
  }
  /**
   * Visualiza un Internal frame con el Aviso indicado
   */
  public boolean preguntar(JFrame frame, JInternalFrame desactivar, String msgCab, String msg, Component foco, Dimension d) {
        JDialog JDialog1 = getDialogo(frame, "¡¡ Atencion !!", msgCab, msg, true, d);
        if (desactivar != null)
           desactivar.setEnabled(false);
        JDialog1.setVisible(true);

        JDialog1.dispose();
        if (desactivar != null)
           desactivar.setEnabled(true);
        if (foco != null)
           foco.requestFocus();
        return swAceptar;
  }
  /**
   * Disena la Ventana
   */
  private JDialog getDialogo(JFrame frame, String wtitle, String msgCab, String msg, boolean swCancelar, Dimension d) {
        final JDialog JDialog1 = new JDialog(frame, true);
        CPanel CPanel1 = new CPanel(new GridBagLayout());
        CLabel imagen = new CLabel(Iconos.getImageIcon("logo"));
        CLabel msgL = new CLabel();
        JScrollPane jScrollPane1 = new JScrollPane();
        JTextArea explicL = new JTextArea();
        CButton Baceptar = new CButton("Aceptar", Iconos.getImageIcon("check"));
        CButton Bcancelar = new CButton("Cancelar", Iconos.getImageIcon("cancel"));

        JDialog1.setSize(d);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = JDialog1.getSize();
        if (frameSize.height > screenSize.height)
          frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width)
          frameSize.width = screenSize.width;
        JDialog1.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        JDialog1.setTitle(wtitle);

        msgL.setBackground(Color.blue);
        msgL.setFont(new java.awt.Font("Dialog", 1, 14));
        msgL.setForeground(Color.white);
        msgL.setBorder(BorderFactory.createRaisedBevelBorder());
        msgL.setMaximumSize(new Dimension(440, 30));
        msgL.setMinimumSize(new Dimension(440, 30));
        msgL.setOpaque(true);
        msgL.setPreferredSize(new Dimension(440, 30));
        msgL.setHorizontalAlignment(SwingConstants.CENTER);
        msgL.setText(msgCab);

        explicL.setText(msg);
        explicL.setEditable(false);

        Baceptar.setMaximumSize(new Dimension(115, 31));
        Baceptar.setMinimumSize(new Dimension(115, 31));
        Baceptar.setPreferredSize(new Dimension(115, 31));
        Baceptar.setMargin(new Insets(0, 0, 0, 0));
        Baceptar.setMnemonic('A');
        Baceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                   swAceptar = true;
                   JDialog1.setVisible(false);
            }
        });
        Bcancelar.setMaximumSize(new Dimension(115, 31));
        Bcancelar.setMinimumSize(new Dimension(115, 31));
        Bcancelar.setPreferredSize(new Dimension(115, 31));
        Bcancelar.setMargin(new Insets(0, 0, 0, 0));
        Bcancelar.setMnemonic('A');
        Bcancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                   swAceptar = false;
                   JDialog1.setVisible(false);
            }
        });
        imagen.setBackground(Color.white);
        imagen.setBorder(BorderFactory.createRaisedBevelBorder());
        imagen.setMaximumSize(new Dimension(30, 30));
        imagen.setMinimumSize(new Dimension(30, 30));
        imagen.setOpaque(true);
        imagen.setPreferredSize(new Dimension(30, 30));
        jScrollPane1.setBorder(BorderFactory.createRaisedBevelBorder());
        jScrollPane1.setMaximumSize(new Dimension(470, 115));
        jScrollPane1.setMinimumSize(new Dimension(470, 115));
        jScrollPane1.setPreferredSize(new Dimension(470, 115));
        JDialog1.getContentPane().add(CPanel1, BorderLayout.CENTER);
        CPanel1.add(Baceptar, new GridBagConstraints(1, 2, (swCancelar?1:2), 1, 0.0, 0.0
                ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 0, 2, 0), 0, 0));
        if (swCancelar)
           CPanel1.add(Bcancelar, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
                ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 0, 2, 0), 0, 0));
        CPanel1.add(msgL, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 2, 0, 5), 0, 0));
        CPanel1.add(jScrollPane1, new GridBagConstraints(0, 1, 3, 1, 1.0, 1.0
                ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 5, 0, 5), 0, 0));
        CPanel1.add(imagen, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 0), 0, 0));
        jScrollPane1.getViewport().add(explicL, null);

        return JDialog1;
  }
  public static mensajes getMensajes() { return new mensajes(); }
  /**
   * Muestra una Dialogo moda con un calendario. Devuelve NULL si no se elige
   * ninguna fecha o la fecha elegida.
   * Hace uso de la clase JCalendar de
   * @param fechaInicio Fecha inicio del calendario
   * @return Date con fecha (o null si no se escogio ninguna)
   */
  public static Date getFechaCalendario(Date fechaInicio)
  {
      final JDialog ifr=new JDialog();
      ifr.setModal(true);
      final JCalendar dch=new JCalendar(new Locale("ES"), true);
      ifr.setTitle("Calendario");

      ifr.setSize(new Dimension(250,250));
      ifr.setLocation(Formatear.getCentroLocation(ifr.getSize()));
      ifr.setLayout(new BorderLayout());

        dch.getDayChooser().setAlwaysFireDayProperty(true);
        if (fechaInicio!=null)
            dch.setDate(fechaInicio);
        ifr.getContentPane().add(dch);
        dch.getDayChooser().addPropertyChangeListener(new PropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                if (evt.getPropertyName().equals("day"))
                {
                    ifr.setVisible(false);
                    fecha=dch.getCalendar().getTime();
                }
            }
        });
        ifr.addWindowListener(new WindowAdapter()
        {
            @Override
             public void windowClosing(WindowEvent e)
             {
                ifr.setVisible(false);
                fecha=null;
             }
        });
       ifr.setVisible(true);
       return fecha;
  }
}
