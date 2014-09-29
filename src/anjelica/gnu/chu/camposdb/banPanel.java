package gnu.chu.camposdb;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import gnu.chu.controles.*;
import gnu.chu.sql.*;
import gnu.chu.winayu.*;
import gnu.chu.utilidades.*;
import javax.swing.event.*;
/**
 *
 * <p>Título: banPanel</p>
 * <p>Descripción: Panel para introducir el codigo de un banco. <br>
 *  Utiliza la tabla <b>v_banco</B> para comprobar la validez de ese codigo.</p>
 * <p>Copyright: Copyright (c) 2004-2014</p>
 * <p>Empresa: </p>
 * @author gnu.chup
 * @version 1.0
 */
public class banPanel extends CPanel
{
  int banCodi=0;
  boolean aceptaNulo = true;
  boolean swIniciar = false;
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CTextField ban_codiE = new CTextField(Types.DECIMAL, "9999");
  CTextField ban_nombL = new CTextField();
  CButton Bcons = new CButton(Iconos.getImageIcon("find"))
  {
    public boolean isFocusTraversable()
    {
      return false;
    }
  };
  int ancTexto = 40;
  EntornoUsuario eu1;
  boolean ponBcons = true;
  String msgError = "";
  DatosTabla dt;
  EntornoUsuario eu;
  JLayeredPane vl;
  CInternalFrame intfr;


  public banPanel()
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
    ban_codiE.setPreferredSize(new Dimension(ancTexto, 16));
    ban_codiE.setMinimumSize(new Dimension(ancTexto, 16));
    ban_codiE.setMaximumSize(new Dimension(ancTexto, 16));

    ban_nombL.setPreferredSize(new Dimension(200, 17));
    ban_nombL.setEnabled(false);
    ban_nombL.setBackground(Color.orange);
    ban_nombL.setForeground(Color.white);
    ban_nombL.setOpaque(true);
    Bcons.setPreferredSize(new Dimension(17, 17));
    Bcons.setMinimumSize(new Dimension(17, 17));
    Bcons.setMaximumSize(new Dimension(17, 17));
    this.setLayout(gridBagLayout1);
    this.add(ban_codiE, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.WEST,
                                               GridBagConstraints.VERTICAL,
                                               new Insets(0, 0, 0, 0), 0, 0));
    this.add(ban_nombL, new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(0, 2, 0, 0), 0, 0));
    this.add(Bcons, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
                                           , GridBagConstraints.CENTER,
                                           GridBagConstraints.VERTICAL,
                                           new Insets(0, 2, 0, 0), 0, 0));
  }

  public void requestFocus()
  {
    ban_codiE.requestFocus();
  }

  public void resetTexto()
  {
    super.resetTexto();
    ban_nombL.setText("");
  }
  public void resetCambio()
  {
    ban_codiE.resetCambio();
  }
  public void ban_codiE_focusLost()
  {
    if (banCodi!=ban_codiE.getValorInt())
    {
      banCodi=ban_codiE.getValorInt();
      try
      {
        controla(false);
      }
      catch (Exception k)
      {}
    }
  }

  public boolean isNull()
  {
    if (ban_codiE.getValorDec() == 0 || ban_codiE.getText().trim().equals(""))
      return true;
    else
      return false;
  }
 public void setText(String repres)
 {
   setText(repres,false);
 }
  /**
   * Pone el Texto del Campo de Entrada
   * @param banCodi -  Banco
   * @param controla a true obliga a llamar a la funcion controla aunque el campo este enabled.
   * Actualizara el Campo del Nombre del representante.
   */
  public void setText(String banCodi,boolean controla)
  {
    ban_codiE.setText(banCodi);
    try {
      if (!ban_codiE.isEnabled() || controla)
        controla(false);
    } catch (Exception k)
    {
      SystemOut.print(k);
    }
  }

  public String getStrQuery()
  {
    return ban_codiE.getStrQuery();
  }

  public void setQuery(boolean query)
  {
    ban_codiE.setQuery(query);
  }

  public boolean getQuery()
  {
    return ban_codiE.getQuery();
  }

  /*
   * Devuelve el Codigo De Forma de Pago
   */
  public String getText()
  {
    return ban_codiE.getText();
  }


  public String getTextNomb()
  {
    return ban_nombL.getText();
  }

  /**
   * Iniciar Control
   * @param datTabla .. Utilizado para Iniciar los campos con Mascara y para realizar
   *                  la select de buscar el nombre de Producto.
   * @param intFrame Internal Frame donde se hechara el control
   * @param layPan .. Mandado como parametro al llamar al AyuRep o pdrepresentatnte,
   *        al pulsar botones.
   * @param entUsu .. Mandado a los programas de ayuda o alta y utilizado para poder
   *                  realizar sus propias select (para buscar el nombre del
   *                  representante.
   * @throws SQLException en caso error DB
   * @throws ParseException en caso error DB
   */
  public void iniciar(DatosTabla datTabla, CInternalFrame intFrame,
                      JLayeredPane layPan, EntornoUsuario entUsu) throws
      SQLException,java.text.ParseException
  {
    intfr = intFrame;
    dt = datTabla;
    vl = layPan;
    eu = entUsu;
    ponOrejas();
    setText("");
    controla(false);
    setAceptaNulo(false);
    swIniciar = true;
  }

  public void setbanNomb(CTextField ban_nombE)
  {
    this.remove(ban_nombL);
    this.remove(Bcons);
    this.remove(ban_codiE);
    this.add(ban_codiE, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
                                              , GridBagConstraints.WEST,
                                              GridBagConstraints.BOTH,
                                              new Insets(0, 0, 0, 0), 0, 0));

    ban_nombL=ban_nombE;
  }
  public void setColumnaAlias(String alias)
  {
    ban_codiE.setColumnaAlias(alias);
  }

  /**
   * Establece el ancho (en pantalla) del codigo (Forma de Pago)
   * @param anc Anchura en Pixeles
   */
  public void setAncTexto(int anc)
  {
    ancTexto = anc;
    ban_codiE.setPreferredSize(new Dimension(ancTexto, 16));
    ban_codiE.setMinimumSize(new Dimension(ancTexto, 16));
    ban_codiE.setMaximumSize(new Dimension(ancTexto, 16));
    validate();
    repaint();
  }

  public int getAncTexto()
  {
    return ancTexto;
  }

  void ponOrejas()
  {
    banCodi=ban_codiE.getValorInt();
    ban_codiE.addFocusListener(new FocusAdapter()
    {
      public void focusLost(FocusEvent e)
      {
        ban_codiE_focusLost();
      }
    });
    ban_codiE.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent e)
      {
        procesaTecla(e);
      }
    });

    Bcons.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        consban();
      }
    });

  }

  public void setVisibleBotonCons(boolean visible)
  {
    if (visible == ponBcons)
      return;
    ponBcons = visible;
    Bcons.setVisible(visible);
  }

  public boolean getVisibleBotonCons()
  {
    return ponBcons;
  }



  void procesaTecla(KeyEvent e)
  {
    if (ponBcons)
    {
      if (e.getKeyCode() == KeyEvent.VK_F3)
      {
        consban();
        return;
      }
    }

  }
  public void addKeyListener(KeyListener kl)
  {
    ban_codiE.addKeyListener(kl);
  }
  public void addFocusListener(FocusListener focLis)
  {
    if (ban_codiE != null)
      ban_codiE.addFocusListener(focLis);
  }
  public void setName(String name)
  {
    if (ban_codiE != null)
      ban_codiE.setName(name);
  }
  public String getName()
  {
    if (ban_codiE == null)
      return null;
    return ban_codiE.getName();
  }

  /**
   * Consulta Formas de Pago
   * Llama a la Ayuda de Formas de Pago
   */
  public void consban()
  {
    final ayuBan ayban;

    try
    {
      ayban = new ayuBan(eu, vl);
      ayban.addInternalFrameListener(new InternalFrameAdapter()
      {
        public void internalFrameClosing(InternalFrameEvent e)
        {
          ej_consban(ayban);
        }
      });

      vl.add(ayban);
      ayban.setLocation(25, 25);
      ayban.setVisible(true);
      if (intfr != null)
      {
        intfr.setEnabled(false);
        intfr.setFoco(ayban);
      }
      ayban.iniciarVentana();

    }
    catch (Exception j)
    {
      if (intfr != null)
        intfr.setEnabled(true);
    }
  }

  void ej_consban(ayuBan ayban)
  {
    if (ayban.consulta)
    {
      ban_codiE.setText(ayban.ban_codiT);
      ban_nombL.setText(ayban.ban_nombT);
    }
    ban_codiE.requestFocus();
    ayban.dispose();

    if (intfr != null)
    {
      intfr.setEnabled(true);
      intfr.toFront();
      try
      {
        intfr.setSelected(true);
      }
      catch (Exception k)
      {}
      intfr.setFoco(null);
      this.requestFocus();
    }
  }

  public void setAutoNext(boolean s)
  {
    ban_codiE.setAutoNext(s);
  }

  public boolean getAutoNext()
  {
    return ban_codiE.getAutoNext();
  }

  /**
   * Controla si la Forma de pago es Valida.
   * @return true si el Campo es Valido.
   *         false si No lo es.
   * <p> En el Caso de que el campo no sea valido se hace un requestFocus
   *  al pro_codi
   * @throws ParseException en error DB
   * @throws SQLException en error DB
   * @see getMsgError
   */
  public boolean controlar() throws SQLException,java.text.ParseException
  {
    return controla(true);
  }
  public int getValorInt()
  {
    try
    {
      return Integer.parseInt(ban_codiE.getText().trim());
    }
    catch (Exception k)
    {
      return 0;
    }
  }

  public boolean hasCambio()
  {
    return ban_codiE.hasCambio();
  }

  /**
   * Controla si el Codigo de Producto es Valido.
   *
   * @return true si el Campo es Valido.
   *         false si No lo es.
   * @throw Exception si hay un error de base de datos al buscar el
   * nombre del Representante.
   * @param reqFocus si debe hacer el requestFocus al pro_codi en caso de Error
   * @throws SQLException en error DB
   * @throws ParseException en error DB
   * @see getMsgError
   */

  public boolean controla(boolean reqFocus) throws SQLException,java.text.ParseException
  {
    msgError = "";
    if (isNull())
    {
      ban_nombL.setText("");
      if (getAceptaNulo())
        return true;
      else
      {
        msgError = "Banco no puede estar en blanco";
        if (reqFocus)
          ban_codiE.requestFocus();
        return false;
      }
    }

    if (dt == null)
      return false;
    try {
      Integer.parseInt(ban_codiE.getText().trim());
    } catch (Exception k)
    {
      msgError="Banco NO valida";
      if (reqFocus)
        ban_codiE.requestFocus();
      return false;
    }
    String s=getNombban(ban_codiE.getValorInt());

    if (s==null)
    {
      ban_nombL.setText("**Banco NO Encontrado");
      if (reqFocus)
        ban_codiE.requestFocus();
      msgError = "** Banco NO Encontrado**";

      return false;
    }
    ban_nombL.setText(s);
//    sefCodi = dt.getString("sef_codi");
    afterControla();
    return true;
  }

  /**
   * Machacar para hacer algo despues de llamar a controlar.
   */
  protected void afterControla()
  {

  }

  public String getNombban(int codban, DatosTabla dt) throws SQLException,
      java.text.ParseException
  {
    String s = "SELECT * FROM v_banco WHERE ban_codi= " + codban;
    dt.select(s);
    if (dt.getNOREG())
      return null;
    return dt.getString("ban_nomb");
  }

  public String getNombban(int codban) throws SQLException,java.text.ParseException
  {
    return getNombban(codban,dt);
  }


  public String getMsgError()
  {
    return msgError;
  }

  public boolean getAceptaNulo()
  {
    return aceptaNulo;
  }

  public void setAceptaNulo(boolean acepNulo)
  {
    aceptaNulo = acepNulo;
  }

}
