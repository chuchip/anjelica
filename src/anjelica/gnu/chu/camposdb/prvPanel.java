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
 * <p>Titulo: prvPanel</p>
 * <p>Descripción: Panel para introducir el codigo del proveedor</p>
 * <p>Copyright: Copyright (c) 2005-2011
 *
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los términos de la Licencia Publica General de GNU según es publicada por
 *  la Free Software Foundation, bien de la versión 2 de dicha Licencia
 *  o bien (según su elección) de cualquier versión posterior.
 *  Este programa se distribuye con la esperanza de que sea útil,
 *  pero SIN NINGUNA GARANTÍA, incluso sin la garantía MERCANTIL implícita
 *  o sin garantizar la CONVENIENCIA PARA UN PROPÓSITO PARTICULAR.
 *  Véase la Licencia Pública General de GNU para más detalles.
 *  Debería haber recibido una copia de la Licencia Pública General junto con este programa.
 *  Si no ha sido así, escriba a la Free Software Foundation, Inc.,
 *  en 675 Mass Ave, Cambridge, MA 02139, EEUU.
 * @version 1.0
 * 
 */
public class prvPanel extends CPanel
{
  private boolean error=false;
  private int prvIntern;
  boolean swControl=true;
  int prvCodi=0;
  boolean aceptaNulo = true;
  boolean swIniciar = false;
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  public CTextField prv_codiE = new CTextField(Types.DECIMAL, "#####9");
  CTextField prv_nombL = new CTextField();
  CButton Bcons = new CButton(Iconos.getImageIcon("find"));
  CButton Binserta = new CButton(Iconos.getImageIcon("nuevo"));

  int ancTexto = 60;
  EntornoUsuario eu1;
  boolean ponBcons = true;
  boolean ponBins = false;
  String msgError = "";
  DatosTabla dt;
  EntornoUsuario eu;
  JLayeredPane vl;
  CInternalFrame intfr;
  int fpaCodi=0;

  public prvPanel()
  {
    try
    {
      jbInit();
    }
    catch (Exception e)
    {
    }
  }

  private void jbInit() throws Exception
  {
    prv_codiE.setPreferredSize(new Dimension(ancTexto, 16));
    prv_codiE.setMinimumSize(new Dimension(ancTexto, 16));
    prv_codiE.setMaximumSize(new Dimension(ancTexto, 16));
    Bcons.setFocusable(false);

    prv_nombL.setPreferredSize(new Dimension(200, 17));
    prv_nombL.setEnabled(false);
    prv_nombL.setBackground(Color.orange);
    prv_nombL.setForeground(Color.white);
    prv_nombL.setOpaque(true);
    Bcons.setPreferredSize(new Dimension(17, 17));
    Bcons.setMinimumSize(new Dimension(17, 17));
    Bcons.setMaximumSize(new Dimension(17, 17));
    Binserta.setPreferredSize(new Dimension(17, 17));
    Binserta.setMinimumSize(new Dimension(17, 17));
    Binserta.setMaximumSize(new Dimension(17, 17));
    this.setLayout(gridBagLayout1);
    this.add(prv_codiE, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.WEST,
                                               GridBagConstraints.VERTICAL,
                                               new Insets(0, 0, 0, 0), 0, 0));
    this.add(prv_nombL, new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0
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
    prv_codiE.requestFocus();
  }

  public void resetTexto()
  {
    super.resetTexto();
    prvCodi=-1;
    prv_codiE.resetTexto();
    prv_nombL.resetTexto();
  }
  public void resetCambio()
  {
    prv_codiE.resetCambio();
  }
  public void prv_codiE_focusLost()
  {
    error=false;
    if (prvCodi!=prv_codiE.getValorInt())
    {
      if (! swControl)
        return;
      prvCodi=prv_codiE.getValorInt();
      try
      {
        error=controla(false,true);
        afterCambioPrv();
      }
      catch (Exception k)
      {}
    }
  }
  /**
   * Funcion a machacar si se quiere hacer algo despues de cambiar el proveedor
   * si ha habido un focuslost
   */
  public void afterCambioPrv()
  {
      
  }
  public boolean getError()
  {
      return error;
  }
  public void setError(boolean error)
  {
      this.error=error;
  }
  public boolean isNull()
  {
      return prv_codiE.getValorDec() == 0 || prv_codiE.getText().trim().equals("");
  }
    @Override
 public void setText(String repres)
 {
   setText(repres,false);
 }
 public int getValorInt()
 {
   return prv_codiE.getValorInt();
 }

 public void setValorInt(int valor)
 {
   setText(""+valor);
 }
 public void setTextNomb(String nombPrv)
 {
   prv_nombL.setText(nombPrv);
 }
 public String getTextNomb()
 {
   return prv_nombL.getText();
 }
 /**
  * Establece el texto del Proveedor
  * @param prvCodi String Codigo de Proveedor
  * @param controla boolean  llamar a Controlar ?
  */
 public void setText(String prvCodi,boolean controla)
  {
    prv_codiE.setText(prvCodi);
    try {
      if (! swControl)
        return; // Pasa de buscar y poner el nombre del producto.
      if (!prv_codiE.isEnabled() || !prv_codiE.isEditable() || controla)
        controla(false,true);
    } catch (Exception k)
    {
      k.printStackTrace();
    }
  }

  public String getStrQuery()
  {
    return prv_codiE.getStrQuery();
  }

  public void setQuery(boolean query)
  {
    prv_codiE.setQuery(query);
//    Bcons.setEnabled(!query);
//    Binserta.setEnabled(!query);
  }

  public boolean getQuery()
  {
    return prv_codiE.isQuery();
  }

  /*
   * Devuelve el Codigo Proveedor
   */
  public String getText()
  {
    return prv_codiE.getText();
  }

  public String getTextAlfa()
  {
    if (isNull())
      return null;
    String texto;
    texto = getText();
    if (texto.length() == 0)
      return null;
    return "" + texto.charAt(0);
  }

  public String getTextNume()
  {
    if (isNull())
      return null;
    String texto;
    texto = getText();
    if (texto.length() < 2)
      return null;
    return texto.substring(1);
  }


  /**
   * Iniciar Control
   * @param datTabla .. Utilizado para Iniciar los campos con Mascara y para realizar
   *                  la select de buscar el nombre de Producto.
   * @param intFrame Internal Frame
   * @param layPan .. Mandado como parametro al llamar al AyuRep o pdrepresentatnte,
   *        al pulsar botones.
   * @param entUsu .. Mandado a los programas de ayuda o alta y utilizado para poder
   *                  realizar sus propias select (para buscar el nombre del
   *                  representante.
   * @throws SQLException
   * @throws java.text.ParseException
   */
  public void iniciar(DatosTabla datTabla, CInternalFrame intFrame,
                      JLayeredPane layPan, EntornoUsuario entUsu) throws
      SQLException
  {
    prv_codiE.setToolTipText("F3 Ayuda Proveedores");
    intfr = intFrame;
    dt = datTabla;
    vl = layPan;
    eu = entUsu;
//    prv_codiE.setMascaraDB(dt,eu.em_cod);
    activarEventos();
    if (swControl)
    {
      setText("");
      controla(false,true);
    }
    setAceptaNulo(false);
    swIniciar = true;
  }

  public void setCampoNombre(CTextField prv_nombE)
  {
    if (prv_nombE == null)
      swControl = false;
    else
      swControl = true;
    this.remove(prv_nombL);
    this.remove(Bcons);
    if (!swControl)
      return;
    this.remove(prv_codiE);
    this.add(prv_codiE, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
                                               , GridBagConstraints.WEST,
                                               GridBagConstraints.BOTH,
                                               new Insets(0, 0, 0, 0), 0, 0));
    prv_nombL = prv_nombE;

  }
  /**
   * @deprecated usar setCampoNombre(CTextField prv_nomb)
   * @param prv_nombE CTextField
   */
  public void setPrvNomb(CTextField prv_nombE)
  {
    setCampoNombre(prv_nombE);
  }



  public CTextField getTextField()
  {
    return prv_codiE;
  }

  public void setColumnaAlias(String alias)
  {
    prv_codiE.setColumnaAlias(alias);
  }

  public void setControla(boolean swControla)
   {
     swControl=swControla;
   }
  public void setEditable(boolean editable)
  {
     prv_codiE.setEditable(editable);
  }
  public boolean isEditable()
  {
      return prv_codiE.isEditable();
  }

  /**
   * Por si alguien quiere saber si se han llenado los campos.
   */
  protected void llenaCampos1()
  {

  }
  /**
   * Establece el Ancho del codigo del proveedor
   * @param anc int
   */
  public void setAncTexto(int anc)
  {
    ancTexto = anc;
    prv_codiE.setPreferredSize(new Dimension(ancTexto, 16));
    prv_codiE.setMinimumSize(new Dimension(ancTexto, 16));
    prv_codiE.setMaximumSize(new Dimension(ancTexto, 16));
    validate();
    repaint();
  }

  public int getAncTexto()
  {
    return ancTexto;
  }

  private void activarEventos()
  {
    prvCodi=prv_codiE.getValorInt();
    if (swControl)
    {
      prv_codiE.addFocusListener(new FocusAdapter()
      {
        public void focusLost(FocusEvent e)
        {
          prv_codiE_focusLost();
        }
      });
    }
    prv_codiE.addKeyListener(new KeyAdapter()
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
        consPrv();
      }
    });
    Binserta.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
//        altaPrv();
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

  public boolean getVisibleBotonAlta()
  {
    return ponBins;
  }

  public void setVisibleBotonAlta(boolean visible)
  {
    if (visible == ponBins)
      return;
    ponBins = visible;
    Binserta.setVisible(visible);

  }

  void procesaTecla(KeyEvent e)
  {
    if (ponBcons)
    {
      if (e.getKeyCode() == KeyEvent.VK_F3)
      {
        consPrv();
        return;
      }
    }
    if (ponBins)
    {
      if (e.getKeyCode() == KeyEvent.VK_INSERT)
      {
//        altaPrv();
        return;
      }
    }
  }

  /*
   * Pone un Orejas de Focus al control de entrada de Representante
   * que es un TextChar
   * @since 5/2/99
   */
  /*  public void addFocusListener(FocusListener f)
    {
      if (swIniciar)
        prv_codiE.addFocusOrejas(f);
    }
   */
  public void addKeyListener(KeyListener kl)
  {
    prv_codiE.addKeyListener(kl);
  }
  public void addFocusListener(FocusListener focLis)
  {
    if (prv_codiE != null)
      prv_codiE.addFocusListener(focLis);
  }
  public void setName(String name)
  {
    if (prv_codiE != null)
      prv_codiE.setName(name);
  }
  public String getName()
  {
    if (prv_codiE == null)
      return null;
    return prv_codiE.getName();
  }

  /**
   * Consulta Productos.
   * Llama a la Ayuda de Productos.
   */
  public void consPrv()
  {
    final ayuPrv ayprv;

    try
    {
      ayprv = new ayuPrv(eu, vl);
      ayprv.addInternalFrameListener(new InternalFrameAdapter()
      {
        public void internalFrameClosing(InternalFrameEvent e)
        {
          ej_consPrv(ayprv);
        }
      });

      vl.add(ayprv);
      ayprv.setLocation(25, 25);
      ayprv.setVisible(true);
      if (intfr != null)
      {
        intfr.setEnabled(false);
        intfr.setFoco(ayprv);
      }
      ayprv.iniciarVentana();

    }
    catch (Exception j)
    {
      if (intfr != null)
        intfr.setEnabled(true);
    }
  }

  void ej_consPrv(ayuPrv aypro)
  {
    if (aypro.consulta)
    {
      prv_codiE.setText(aypro.prv_codiT);
      if (swControl)
         prv_nombL.setText(aypro.prv_nombT);
    }
    prv_codiE.requestFocus();
    aypro.dispose();

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
    prv_codiE.setAutoNext(s);
  }

  public boolean getAutoNext()
  {
    return prv_codiE.getAutoNext();
  }

  /**
   * Controla si el codigo del proveedor es valido
   * @throws SQLException En caso de error en la DB
   * @throws ParseException En caso error en la DB
   * @return boolean false si hay un error
   */
  public boolean controlar() throws SQLException
  {
    return controla(true,true);
  }

  public boolean hasCambio()
  {
    return prv_codiE.hasCambio();
  }
  public boolean controla(boolean reqFocus) throws SQLException
  {
    return controla(reqFocus,true);
  }
  /**
   * Controla si el codigo del proveedor es Valido
   * @param reqFocus boolean Ejecutar requestFocus si hay un error
   * @param ponNombre boolean Poner el nombre del proveedor en prv_nomb
   * @throws SQLException error en DB
   * @throws ParseException error en DB
   * @return boolean false si hay un error
   */
  public boolean controla(boolean reqFocus,boolean ponNombre) throws SQLException
  {
    fpaCodi=0;
    msgError = "";
    if (isNull())
    {
      if (prv_nombL!=null)
        prv_nombL.setText("");
      if (getAceptaNulo())
        return true;
      else
      {
        msgError = "Proveedor no puede estar en blanco";
        if (reqFocus)
          prv_codiE.requestFocus();
        return false;
      }
    }

    if (dt == null)
      return false;
    try {
      Integer.parseInt(prv_codiE.getText().trim());
    } catch (Exception k)
    {
      msgError="Codigo de Proveedor NO valido";
      if (reqFocus)
        prv_codiE.requestFocus();
      return false;
    }
    String s=getNombPrv(prv_codiE.getText());

    if (s==null)
    {
      if (ponNombre)
        prv_nombL.setText("**Proveedor NO Encontrado");
      if (reqFocus)
        prv_codiE.requestFocus();
      msgError = "** Proveedor NO Encontrado**";
      return false;
    }
    if (ponNombre)
      prv_nombL.setText(s);
    afterControla();
    return true;
  }

  /**
   * Machacar para hacer algo despues de llamar a controlar.
   */
  protected void afterControla()
  {

  }

  public String getNombPrv(String codPrv, DatosTabla dt) throws SQLException
  {
    fpaCodi=0;
    prvIntern=0;
    if (codPrv.trim().equals(""))
      return null;
    String s = "SELECT prv_nomb,fpa_codi,prv_intern FROM v_proveedo WHERE prv_codi= " + codPrv;
    dt.select(s);
    if (dt.getNOREG())
      return null;
    fpaCodi=dt.getInt("fpa_codi",true);
    prvIntern=dt.getInt("prv_intern",true);
    return dt.getString("prv_nomb");
  }
  public boolean isInterno()
  {
    return prvIntern!=0;
  }
  /**
   * Devuelve codigo de forma de pag
   * @return int Forma de pago
   */
  public int getFpaCodi()
  {
    return fpaCodi;
  }
  public String getNombPrv(String codPrv) throws SQLException
  {
    return getNombPrv(codPrv,dt);
  }


  public String getMsgError()
  {
    return msgError;
  }

  public boolean getAceptaNulo()
  {
    return aceptaNulo;
  }
  public void setToolTipText(String toolTipText)
  {
    if (prv_codiE!=null)
      prv_codiE.setToolTipText(toolTipText);
  }

  public void setAceptaNulo(boolean acepNulo)
  {
    aceptaNulo = acepNulo;
  }
  
  public void setEnabledNombre(boolean enab)
  {
    prv_nombL.setEnabled(enab);
  }
  public boolean isEnabledNombre()
  {
    return prv_nombL.isEnabled();
  }
  public CTextField getCampoNombre()
  {
    return prv_nombL;
  }
}
