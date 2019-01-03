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
 * Panel con el Codigo de la Sala de Despiece
 * @Author Chuchi P
 *
 * Version 1.0
*/
public class sdePanel extends CPanel
{
  private boolean swControl=true;
  String nRegSan;
  int matCodi=0;
  boolean aceptaNulo = true;
  boolean swIniciar = false;
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CTextField sde_codiE = new CTextField(Types.DECIMAL, "####9");
  CTextField sde_nombE = new CTextField(Types.CHAR,"X",50);
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


  public sdePanel()
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
    sde_codiE.setPreferredSize(new Dimension(ancTexto, 16));
    sde_codiE.setMinimumSize(new Dimension(ancTexto, 16));
    sde_codiE.setMaximumSize(new Dimension(ancTexto, 16));

    sde_nombE.setPreferredSize(new Dimension(200, 17));
    sde_nombE.setEnabled(false);
    sde_nombE.setBackground(Color.orange);
    sde_nombE.setForeground(Color.white);
    sde_nombE.setOpaque(true);
    Bcons.setPreferredSize(new Dimension(17, 17));
    Bcons.setMinimumSize(new Dimension(17, 17));
    Bcons.setMaximumSize(new Dimension(17, 17));
    this.setLayout(gridBagLayout1);
    this.add(sde_codiE, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.WEST,
                                               GridBagConstraints.VERTICAL,
                                               new Insets(0, 0, 0, 0), 0, 0));
    this.add(sde_nombE, new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0
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
    sde_codiE.requestFocus();
  }

  public void resetTexto()
  {
    super.resetTexto();
    sde_nombE.setText("");
  }
  public void resetCambio()
  {
    sde_codiE.resetCambio();
  }
  public void sde_codiE_focusLost()
  {
    if (matCodi!=sde_codiE.getValorInt())
    {
      matCodi=sde_codiE.getValorInt();
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
    if (sde_codiE.getValorDec() == 0 || sde_codiE.getText().trim().equals(""))
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
    sde_codiE.setText(matCodi);
    try {
      if (! swControl)
        return; // Pasa de buscar y poner el nombre del producto.
      if (!sde_codiE.isEnabled() || controla)
        controla(false);
    } catch (Exception k)
    {
      k.printStackTrace();
    }
  }

  public String getStrQuery()
  {
    return sde_codiE.getStrQuery();
  }

  public void setQuery(boolean query)
  {
    sde_codiE.setQuery(query);
  }

  public boolean getQuery()
  {
    return sde_codiE.getQuery();
  }

  /*
   * Devuelve el Codigo De Forma de Pago
   */
  public String getText()
  {
    return sde_codiE.getText();
  }


  public String getTextNomb()
  {
    return sde_nombE.getText();
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
    sde_codiE.setToolTipText("F3 Ayuda Salas Despiece");
    intfr = intFrame;
    dt = datTabla;
    vl = layPan;
    eu = entUsu;
//    sde_codiE.setMascaraDB(dt,eu.em_cod);
    ponOrejas();
    if (swControl)
    {
      setText("");
      controla(false);
    }
    setAceptaNulo(false);
    swIniciar = true;
  }

  public void setSdeNomb(CTextField sdeNombE)
  {
    this.remove(this.sde_nombE);
    this.remove(Bcons);
    this.remove(sde_codiE);
    this.add(sde_codiE, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
                                              , GridBagConstraints.WEST,
                                              GridBagConstraints.BOTH,
                                              new Insets(0, 0, 0, 0), 0, 0));
    this.sde_nombE=sdeNombE;
  }
  public void setColumnaAlias(String alias)
  {
    sde_codiE.setColumnaAlias(alias);
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
    sde_codiE.setPreferredSize(new Dimension(ancTexto, 16));
    sde_codiE.setMinimumSize(new Dimension(ancTexto, 16));
    sde_codiE.setMaximumSize(new Dimension(ancTexto, 16));
    validate();
    repaint();
  }

  public int getAncTexto()
  {
    return ancTexto;
  }

  void ponOrejas()
  {
    matCodi=sde_codiE.getValorInt();
    if (swControl)
    {
      sde_codiE.addFocusListener(new FocusAdapter()
      {
        public void focusLost(FocusEvent e)
        {
          sde_codiE_focusLost();
        }
      });
    }
    sde_codiE.addKeyListener(new KeyAdapter()
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
        consSde();
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
        consSde();
        return;
      }
    }

  }
  public void addKeyListener(KeyListener kl)
  {
    sde_codiE.addKeyListener(kl);
  }
  public void addFocusListener(FocusListener focLis)
  {
    if (sde_codiE != null)
      sde_codiE.addFocusListener(focLis);
  }
  public void setName(String name)
  {
    if (sde_codiE != null)
      sde_codiE.setName(name);
  }
  public String getName()
  {
    if (sde_codiE == null)
      return null;
    return sde_codiE.getName();
  }

  /**
   * Consulta Salas Desp.
   * Llama a la Ayuda de Salas Desp.
   */
  public void consSde()
  {
    final ayuSde aysde;

    try
    {
      aysde = new ayuSde(eu, vl);
      aysde.addInternalFrameListener(new InternalFrameAdapter()
      {
        public void internalFrameClosing(InternalFrameEvent e)
        {
          ej_consSde(aysde);
        }
      });

      vl.add(aysde);
      aysde.setLocation(25, 25);
      aysde.setVisible(true);
      if (intfr != null)
      {
        intfr.setEnabled(false);
        intfr.setFoco(aysde);
      }
      aysde.iniciarVentana();

    }
    catch (Exception j)
    {
      if (intfr != null)
        intfr.setEnabled(true);
    }
  }

  void ej_consSde(ayuSde aysde)
  {
    if (aysde.consulta)
    {
      sde_codiE.setText(aysde.sde_codiT);
      if (swControl)
        sde_nombE.setText(aysde.sde_nombT);
    }
    sde_codiE.requestFocus();
    aysde.dispose();

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
    sde_codiE.setAutoNext(s);
  }

  public boolean getAutoNext()
  {
    return sde_codiE.getAutoNext();
  }

  /**
   * Controla si el Sala Desp. es Valida.
   * @return true si el Campo es Valido.
   *         false si No lo es.
   * <p> En el Caso de que el campo no sea valido se hace un requestFocus
   *  al sde_codi
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
      return Integer.parseInt(sde_codiE.getText().trim());
    }
    catch (Exception k)
    {
      return 0;
    }
  }

  public boolean hasCambio()
  {
    return sde_codiE.hasCambio();
  }

  /**
   * Controla si el Sala Desp. es Valido.
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
      sde_nombE.setText("");
      if (getAceptaNulo())
        return true;
      else
      {
        msgError = "Sala Desp. no puede estar en blanco";
        if (reqFocus)
          sde_codiE.requestFocus();
        return false;
      }
    }

    if (dt == null)
      return false;
    try {
      Integer.parseInt(sde_codiE.getText().trim());
    } catch (Exception k)
    {
      msgError="Sala Desp. NO valido";
      if (reqFocus)
        sde_codiE.requestFocus();
      return false;
    }
    String s=getNombMat(sde_codiE.getValorInt());

    if (s==null)
    {
      sde_nombE.setText("**Sala Desp. NO Encontrado");
      if (reqFocus)
        sde_codiE.requestFocus();
      msgError = "** Sala Desp. NO Encontrado**";
      return false;
    }
    sde_nombE.setText(s);
    nRegSan=dt.getString("sde_nrgsa");
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
    String s = "SELECT * FROM v_saladesp WHERE sde_codi= " + codMat;
    if (! dt.select(s))
      return null;
    return dt.getString("sde_nomb");
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

     this.remove(sde_nombE);
     this.remove(Bcons);
     if (!swControl)
       return;
     this.remove(sde_codiE);
     this.add(sde_codiE, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
                                               , GridBagConstraints.WEST,
                                               GridBagConstraints.BOTH,
                                               new Insets(0, 0, 0, 0), 0, 0));

     sde_nombE=matNomb;
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
    return sde_codiE;
  }
}
