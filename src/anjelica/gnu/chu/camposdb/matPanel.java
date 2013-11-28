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
 * Panel con el Codigo del Matadero
 * @Author Chuchi P
 *
 * Version 1.0
*/
public class matPanel extends CPanel
{
  private boolean swControl=true;
  String nRegSan;
  int matCodi=0;
  boolean aceptaNulo = true;
  boolean swIniciar = false;
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CTextField mat_codiE = new CTextField(Types.DECIMAL, "####9");
  CTextField mat_nombE = new CTextField(Types.CHAR,"X",50);
//  CTextField mat_ngrsaE = new CTextField(Types.CHAR,"X",12);
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


  public matPanel()
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
    mat_codiE.setPreferredSize(new Dimension(ancTexto, 16));
    mat_codiE.setMinimumSize(new Dimension(ancTexto, 16));
    mat_codiE.setMaximumSize(new Dimension(ancTexto, 16));

    mat_nombE.setPreferredSize(new Dimension(200, 17));
    mat_nombE.setEnabled(false);
    mat_nombE.setBackground(Color.orange);
    mat_nombE.setForeground(Color.white);
    mat_nombE.setOpaque(true);
    Bcons.setPreferredSize(new Dimension(17, 17));
    Bcons.setMinimumSize(new Dimension(17, 17));
    Bcons.setMaximumSize(new Dimension(17, 17));
    this.setLayout(gridBagLayout1);
    this.add(mat_codiE, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.WEST,
                                               GridBagConstraints.VERTICAL,
                                               new Insets(0, 0, 0, 0), 0, 0));
    this.add(mat_nombE, new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0
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
    mat_codiE.requestFocus();
  }

  public void resetTexto()
  {
    super.resetTexto();
    mat_nombE.setText("");
  }
  public void resetCambio()
  {
    mat_codiE.resetCambio();
  }
  public void mat_codiE_focusLost()
  {
    if (matCodi!=mat_codiE.getValorInt())
    {
      matCodi=mat_codiE.getValorInt();
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
    if (mat_codiE.getValorDec() == 0 || mat_codiE.getText().trim().equals(""))
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
  public void setText(String matCodi,boolean controla)
  {
    mat_codiE.setText(matCodi);
    try {
      if (! swControl)
        return; // Pasa de buscar y poner el nombre del producto.
      if (!mat_codiE.isEnabled() || controla)
        controla(false);
    } catch (Exception k)
    {
      k.printStackTrace();
    }
  }

  public String getStrQuery()
  {
    return mat_codiE.getStrQuery();
  }

  public void setQuery(boolean query)
  {
    mat_codiE.setQuery(query);
  }

  public boolean getQuery()
  {
    return mat_codiE.getQuery();
  }

  /*
   * Devuelve el Codigo De Forma de Pago
   */
  public String getText()
  {
    return mat_codiE.getText();
  }


  public String getTextNomb()
  {
    return mat_nombE.getText();
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
    mat_codiE.setToolTipText("F3 Ayuda Mataderos");
    intfr = intFrame;
    dt = datTabla;
    vl = layPan;
    eu = entUsu;
//    mat_codiE.setMascaraDB(dt,eu.em_cod);
    ponOrejas();
    if (swControl)
    {
      setText("");
      controla(false);
    }
    setAceptaNulo(false);
    swIniciar = true;
  }

  public void setFpaNomb(CTextField fpa_nombE)
  {
    this.remove(mat_nombE);
    this.remove(Bcons);
    this.remove(mat_codiE);
    this.add(mat_codiE, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
                                              , GridBagConstraints.WEST,
                                              GridBagConstraints.BOTH,
                                              new Insets(0, 0, 0, 0), 0, 0));

    mat_nombE=fpa_nombE;
  }
  public void setColumnaAlias(String alias)
  {
    mat_codiE.setColumnaAlias(alias);
  }
  public void setControla(boolean swControla)
   {
     swControl=swControla;
   }

  /**
   * Establece el ancho (en pantalla) del codigo (Forma de Pago)
   * @param anc Anchura en Pixeles
   */
  public void setAncTexto(int anc)
  {
    ancTexto = anc;
    mat_codiE.setPreferredSize(new Dimension(ancTexto, 16));
    mat_codiE.setMinimumSize(new Dimension(ancTexto, 16));
    mat_codiE.setMaximumSize(new Dimension(ancTexto, 16));
    validate();
    repaint();
  }

  public int getAncTexto()
  {
    return ancTexto;
  }

  void ponOrejas()
  {
    matCodi=mat_codiE.getValorInt();
    if (swControl)
    {
      mat_codiE.addFocusListener(new FocusAdapter()
      {
        public void focusLost(FocusEvent e)
        {
          mat_codiE_focusLost();
        }
      });
    }
    mat_codiE.addKeyListener(new KeyAdapter()
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
        consMat();
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
        consMat();
        return;
      }
    }

  }
  public void addKeyListener(KeyListener kl)
  {
    mat_codiE.addKeyListener(kl);
  }
  public void addFocusListener(FocusListener focLis)
  {
    if (mat_codiE != null)
      mat_codiE.addFocusListener(focLis);
  }
  public void setName(String name)
  {
    if (mat_codiE != null)
      mat_codiE.setName(name);
  }
  public String getName()
  {
    if (mat_codiE == null)
      return null;
    return mat_codiE.getName();
  }

  /**
   * Consulta Matadero
   * Llama a la Ayuda de Mataderos
   */
  public void consMat()
  {
    final ayuMat aymat;

    try
    {
      aymat = new ayuMat(eu, vl);
      aymat.addInternalFrameListener(new InternalFrameAdapter()
      {
        public void internalFrameClosing(InternalFrameEvent e)
        {
          ej_consMat(aymat);
        }
      });

      vl.add(aymat);
      aymat.setLocation(25, 25);
      aymat.setVisible(true);
      if (intfr != null)
      {
        intfr.setEnabled(false);
        intfr.setFoco(aymat);
      }
      aymat.iniciarVentana();

    }
    catch (Exception j)
    {
      if (intfr != null)
        intfr.setEnabled(true);
    }
  }

  void ej_consMat(ayuMat aymat)
  {
    if (aymat.consulta)
    {
      mat_codiE.setText(aymat.mat_codiT);
      if (swControl)
        mat_nombE.setText(aymat.mat_nombT);
    }
    mat_codiE.requestFocus();
    aymat.dispose();

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
    mat_codiE.setAutoNext(s);
  }

  public boolean getAutoNext()
  {
    return mat_codiE.getAutoNext();
  }

  /**
   * Controla si el Matadero es Valida.
   * @return true si el Campo es Valido.
   *         false si No lo es.
   * <p> En el Caso de que el campo no sea valido se hace un requestFocus
   *  al mat_codi
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
      return Integer.parseInt(mat_codiE.getText().trim());
    }
    catch (Exception k)
    {
      return 0;
    }
  }

  public boolean hasCambio()
  {
    return mat_codiE.hasCambio();
  }

  /**
   * Controla si el Matadero es Valido.
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
      mat_nombE.setText("");
      if (getAceptaNulo())
        return true;
      else
      {
        msgError = "Matadero no puede estar en blanco";
        if (reqFocus)
          mat_codiE.requestFocus();
        return false;
      }
    }

    if (dt == null)
      return false;
    try {
      Integer.parseInt(mat_codiE.getText().trim());
    } catch (Exception k)
    {
      msgError="Matadero NO valido";
      if (reqFocus)
        mat_codiE.requestFocus();
      return false;
    }
    String s=getNombMat(mat_codiE.getValorInt());

    if (s==null)
    {
      mat_nombE.setText("**Matadero NO Encontrado");
      if (reqFocus)
        mat_codiE.requestFocus();
      msgError = "** Matadero NO Encontrado**";
      return false;
    }
    mat_nombE.setText(s);
    nRegSan=dt.getString("mat_nrgsa");
    afterControla();
    return true;
  }

  public String getNumRegSan()
  {
    return nRegSan;
  }
  /**
   * Machacar para hacer algo despues de llamar a controlar.
   */
  protected void afterControla()
  {

  }

  public String getNombMat(int codMat, DatosTabla dt) throws SQLException,
      java.text.ParseException
  {
    String s = "SELECT * FROM v_matadero WHERE mat_codi= " + codMat;
    if (! dt.select(s))
      return null;
    return dt.getString("mat_nomb");
  }

  public String getNombMat(int codMat) throws SQLException,java.text.ParseException
  {
    return getNombMat(codMat,dt);
  }


  public String getMsgError()
  {
    return msgError;
  }
  /**
    *
    * @param pro_nombE CTextField poner a NULL si no se quiere actualizar el nombre
    *                  del Matadero.
    */
   public void setMatNomb(CTextField matNomb)
   {
     if (matNomb==null)
       swControl = false;
     else
       swControl = true;

     this.remove(mat_nombE);
     this.remove(Bcons);
     if (!swControl)
       return;
     this.remove(mat_codiE);
     this.add(mat_codiE, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
                                               , GridBagConstraints.WEST,
                                               GridBagConstraints.BOTH,
                                               new Insets(0, 0, 0, 0), 0, 0));

     mat_nombE=matNomb;
   }

  public boolean getAceptaNulo()
  {
    return aceptaNulo;
  }

  public void setAceptaNulo(boolean acepNulo)
  {
    aceptaNulo = acepNulo;
  }
  public JTextField getTextField()
  {
    return mat_codiE;
  }
}
