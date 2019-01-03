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
 *  Panel con el Codigo de la Forma de Pago
 * @Author Chuchi P
 *
 * Version 1.0 - 1/8/03
 */
public class fpaPanel extends CPanel
{
  int fpaCodi=0;
  String fpaEsgir;
  boolean aceptaNulo = true;
  boolean swIniciar = false;
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CTextField fpa_codiE = new CTextField(Types.DECIMAL, "###9");
  CTextField fpa_nombL = new CTextField();
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


  public fpaPanel()
  {
    try
    {
      jbInit();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception
  {
    fpa_codiE.setPreferredSize(new Dimension(ancTexto, 16));
    fpa_codiE.setMinimumSize(new Dimension(ancTexto, 16));
    fpa_codiE.setMaximumSize(new Dimension(ancTexto, 16));

    fpa_nombL.setPreferredSize(new Dimension(200, 17));
    fpa_nombL.setEnabled(false);
    fpa_nombL.setBackground(Color.orange);
    fpa_nombL.setForeground(Color.white);
    fpa_nombL.setOpaque(true);
    Bcons.setPreferredSize(new Dimension(17, 17));
    Bcons.setMinimumSize(new Dimension(17, 17));
    Bcons.setMaximumSize(new Dimension(17, 17));
    this.setLayout(gridBagLayout1);
    this.add(fpa_codiE, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.WEST,
                                               GridBagConstraints.VERTICAL,
                                               new Insets(0, 0, 0, 0), 0, 0));
    this.add(fpa_nombL, new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(0, 2, 0, 0), 0, 0));
    this.add(Bcons, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
                                           , GridBagConstraints.CENTER,
                                           GridBagConstraints.VERTICAL,
                                           new Insets(0, 2, 0, 0), 0, 0));
//    this.add(Binserta, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
//            ,GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(0, 2, 0, 0), 0, 0));
  }

  public void requestFocus()
  {
    fpa_codiE.requestFocus();
  }

  public void resetTexto()
  {
    super.resetTexto();
    fpa_nombL.setText("");
  }
  public void resetCambio()
  {
    fpa_codiE.resetCambio();
  }
  public void fpa_codiE_focusLost()
  {
    if (fpaCodi!=fpa_codiE.getValorInt())
    {
      fpaCodi=fpa_codiE.getValorInt();
      try
      {
        controla(false);
      }
      catch (Exception k)
      {
        k.printStackTrace();
      }
    }
  }

  public boolean isNull()
  {
    if (fpa_codiE.getValorDec() == 0 || fpa_codiE.getText().trim().equals(""))
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
   * @param fpaCodi -  Forma de Pago
   * @param controla a true obliga a llamar a la funcion controla aunque el campo este enabled.
   * Actualizara el Campo del Nombre del representante.
   */
  public void setText(String fpaCodi,boolean controla)
  {
    fpa_codiE.setText(fpaCodi);
    try {
      if (!fpa_codiE.isEnabled() || controla)
        controla(false);
    } catch (Exception k)
    {
      k.printStackTrace();
    }
  }

  public String getStrQuery()
  {
    return fpa_codiE.getStrQuery();
  }

  public void setQuery(boolean query)
  {
    fpa_codiE.setQuery(query);
  }

  public boolean getQuery()
  {
    return fpa_codiE.getQuery();
  }

  /*
   * Devuelve el Codigo De Forma de Pago
   */
  public String getText()
  {
    return fpa_codiE.getText();
  }


  public String getTextNomb()
  {
    return fpa_nombL.getText();
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
//    fpa_codiE.setMascaraDB(dt,eu.em_cod);
    ponOrejas();
    setText("");
    controla(false);
    setAceptaNulo(false);
    swIniciar = true;
  }

  public void setFpaNomb(CTextField fpa_nombE)
  {
    this.remove(fpa_nombL);
    this.remove(Bcons);
    this.remove(fpa_codiE);
    this.add(fpa_codiE, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
                                              , GridBagConstraints.WEST,
                                              GridBagConstraints.BOTH,
                                              new Insets(0, 0, 0, 0), 0, 0));

    fpa_nombL=fpa_nombE;
  }
  public void setColumnaAlias(String alias)
  {
    fpa_codiE.setColumnaAlias(alias);
  }

  /**
   * Establece el ancho (en pantalla) del codigo (Forma de Pago)
   * @param anc Anchura en Pixeles
   */
  public void setAncTexto(int anc)
  {
    ancTexto = anc;
    fpa_codiE.setPreferredSize(new Dimension(ancTexto, 16));
    fpa_codiE.setMinimumSize(new Dimension(ancTexto, 16));
    fpa_codiE.setMaximumSize(new Dimension(ancTexto, 16));
    validate();
    repaint();
  }

  public int getAncTexto()
  {
    return ancTexto;
  }

  void ponOrejas()
  {
    fpaCodi=fpa_codiE.getValorInt();
    fpa_codiE.addFocusListener(new FocusAdapter()
    {
      public void focusLost(FocusEvent e)
      {
        fpa_codiE_focusLost();
      }
    });
    fpa_codiE.addKeyListener(new KeyAdapter()
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
        consFpa();
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
        consFpa();
        return;
      }
    }

  }
  public void addKeyListener(KeyListener kl)
  {
    fpa_codiE.addKeyListener(kl);
  }
  public void addFocusListener(FocusListener focLis)
  {
    if (fpa_codiE != null)
      fpa_codiE.addFocusListener(focLis);
  }
  public void setName(String name)
  {
    if (fpa_codiE != null)
      fpa_codiE.setName(name);
  }
  public String getName()
  {
    if (fpa_codiE == null)
      return null;
    return fpa_codiE.getName();
  }

  /**
   * Consulta Formas de Pago
   * Llama a la Ayuda de Formas de Pago
   */
  public void consFpa()
  {
    final ayuFpa ayfpa;

    try
    {
      ayfpa = new ayuFpa(eu, vl);
      ayfpa.addInternalFrameListener(new InternalFrameAdapter()
      {
        public void internalFrameClosing(InternalFrameEvent e)
        {
          ej_consFpa(ayfpa);
        }
      });

      vl.add(ayfpa);
      ayfpa.setLocation(25, 25);
      ayfpa.setVisible(true);
      if (intfr != null)
      {
        intfr.setEnabled(false);
        intfr.setFoco(ayfpa);
      }
      ayfpa.iniciarVentana();

    }
    catch (Exception j)
    {
      if (intfr != null)
        intfr.setEnabled(true);
    }
  }

  void ej_consFpa(ayuFpa ayfpa)
  {
    if (ayfpa.consulta)
    {
      fpa_codiE.setText(ayfpa.fpa_codiT);
      fpa_nombL.setText(ayfpa.fpa_nombT);
    }
    fpa_codiE.requestFocus();
    ayfpa.dispose();

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
    fpa_codiE.setAutoNext(s);
  }

  public boolean getAutoNext()
  {
    return fpa_codiE.getAutoNext();
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
      return Integer.parseInt(fpa_codiE.getText().trim());
    }
    catch (Exception k)
    {
      return 0;
    }
  }

  public boolean hasCambio()
  {
    return fpa_codiE.hasCambio();
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
      fpa_nombL.setText("");
      if (getAceptaNulo())
        return true;
      else
      {
        msgError = "Forma Pago no puede estar en blanco";
        if (reqFocus)
          fpa_codiE.requestFocus();
        return false;
      }
    }

    if (dt == null)
      return false;
    try {
      Integer.parseInt(fpa_codiE.getText().trim());
    } catch (Exception k)
    {
      msgError="Forma de Pago NO valida";
      if (reqFocus)
        fpa_codiE.requestFocus();
      return false;
    }
    String s=getNombFpa(fpa_codiE.getValorInt());

    if (s==null)
    {
      fpa_nombL.setText("**Forma de Pago NO Encontrado");
      if (reqFocus)
        fpa_codiE.requestFocus();
      msgError = "** Forma de Pago NO Encontrado**";
      return false;
    }
    fpa_nombL.setText(s);
    fpaEsgir = dt.getInt("fpa_esgir")==0?"N":"S";
    afterControla();
    return true;
  }
  public String getEsGiro()
  {
    return fpaEsgir;
  }
  /**
   * Machacar para hacer algo despues de llamar a controlar.
   */
  protected void afterControla()
  {

  }

  public String getNombFpa(int codFpa, DatosTabla dt) throws SQLException,
      java.text.ParseException
  {
    String s = "SELECT * FROM v_forpago WHERE fpa_codi= " + codFpa;
    if (! dt.select(s))
      return null;
    return dt.getString("fpa_nomb");
  }

  public String getNombFpa(int codFpa) throws SQLException,java.text.ParseException
  {
    return getNombFpa(codFpa,dt);
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
