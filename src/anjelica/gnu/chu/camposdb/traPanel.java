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
 * <p>Titulo: traPanel </p>
 * <p>Descripcion: Panel para introducir Codigo de Transportista
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
 * </p>
 * @author chuchiP
 * @version 2.0
 *
 */

public class traPanel extends CPanel
{
  boolean swControl=true;
  int prvCodi=0;
  boolean aceptaNulo = true;
  boolean swIniciar = false;
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  public CTextField tra_codiE = new CTextField(Types.DECIMAL, "###9");
  CTextField tra_nombL = new CTextField();
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


  public traPanel()
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
    tra_codiE.setPreferredSize(new Dimension(ancTexto, 16));
    tra_codiE.setMinimumSize(new Dimension(ancTexto, 16));
    tra_codiE.setMaximumSize(new Dimension(ancTexto, 16));

    Bcons.setFocusable(false);
    tra_nombL.setPreferredSize(new Dimension(200, 17));
    tra_nombL.setEnabled(false);
    tra_nombL.setBackground(Color.orange);
    tra_nombL.setForeground(Color.white);
    tra_nombL.setOpaque(true);
    Bcons.setPreferredSize(new Dimension(17, 17));
    Bcons.setMinimumSize(new Dimension(17, 17));
    Bcons.setMaximumSize(new Dimension(17, 17));
    Binserta.setPreferredSize(new Dimension(17, 17));
    Binserta.setMinimumSize(new Dimension(17, 17));
    Binserta.setMaximumSize(new Dimension(17, 17));
    this.setLayout(gridBagLayout1);
    this.add(tra_codiE, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.WEST,
                                               GridBagConstraints.VERTICAL,
                                               new Insets(0, 0, 0, 0), 0, 0));
    this.add(tra_nombL, new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0
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
    tra_codiE.requestFocus();
  }

  public void resetTexto()
  {
    super.resetTexto();
    tra_nombL.setText("");
  }
  public void resetCambio()
  {
    tra_codiE.resetCambio();
  }
  public void tra_codiE_focusLost()
  {
    if (prvCodi!=tra_codiE.getValorInt())
    {
      if (! swControl)
        return;
      prvCodi=tra_codiE.getValorInt();
      try
      {
        controla(false,true);
      }
      catch (Exception k)
      {}
    }
  }

  public boolean isNull()
  {
    if (tra_codiE.getValorDec() == 0 || tra_codiE.getText().trim().equals(""))
      return true;
    else
      return false;
  }
 public void setText(String repres)
 {
   setText(repres,false);
 }
 public int getValorInt()
 {
   return tra_codiE.getValorInt();
 }
 /**
  * Establece el valor de tra_codiE.
  * Observar que no busca el nombre del transportista, si se desea que
  * lo haga, se deberia llamar a controlar()
  *
  * @param valor int Valor a poner en el campo tra_codiE
  */

 public void setValorInt(int valor)
 {
   tra_codiE.setValorDec(valor);
 }
 public void setTextNomb(String nombPrv)
 {
   tra_nombL.setText(nombPrv);
 }
 public String getTextNomb()
 {
   return tra_nombL.getText();
 }

  /**
   * Pone el Texto del Campo de Entrada
   * @param Representante - que estara compuesto de la Zona y el Representante
   * <p>
   * @param controla a true obliga a llamar a la funcion controla aunque el campo este enabled.
   * Actualizara el Campo del Nombre del representante.
   */
  public void setText(String repres,boolean controla)
  {
    tra_codiE.setText(repres);
    try {
      if (! swControl)
        return; // Pasa de buscar y poner el nombre del producto.
      if (!tra_codiE.isEnabled() || controla)
        controla(false,true);
    } catch (Exception k)
    {
      k.printStackTrace();
    }
  }

  public String getStrQuery()
  {
    return tra_codiE.getStrQuery();
  }

  public void setQuery(boolean query)
  {
    tra_codiE.setQuery(query);
//    Bcons.setEnabled(!query);
//    Binserta.setEnabled(!query);
  }

  public boolean getQuery()
  {
    return tra_codiE.isQuery();
  }

  /*
   * Devuelve el Codigo Proveedor
   */
  public String getText()
  {
    return tra_codiE.getText();
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
   * @param layPan .. Mandado como parametro al llamar al AyuRep o pdrepresentatnte,
   *        al pulsar botones.
   * @param entUsu .. Mandado a los programas de ayuda o alta y utilizado para poder
   *                  realizar sus propias select (para buscar el nombre del
   *                  representante.
   */
  public void iniciar(DatosTabla datTabla, CInternalFrame intFrame,
                      JLayeredPane layPan, EntornoUsuario entUsu) throws
      SQLException,java.text.ParseException
  {
    tra_codiE.setToolTipText("F3 Ayuda Proveedores");
    intfr = intFrame;
    dt = datTabla;
    vl = layPan;
    eu = entUsu;
//    tra_codiE.setMascaraDB(dt,eu.em_cod);
    ponOrejas();
    if (swControl)
    {
      setText("");
      controla(false,true);
    }
    setAceptaNulo(false);
    swIniciar = true;
  }

  public void setCampoNombre(CTextField tra_nombE)
  {
    if (tra_nombE == null)
      swControl = false;
    else
      swControl = true;
    this.remove(tra_nombL);
    this.remove(Bcons);
    if (!swControl)
      return;
    this.remove(tra_codiE);
    this.add(tra_codiE, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
                                               , GridBagConstraints.WEST,
                                               GridBagConstraints.BOTH,
                                               new Insets(0, 0, 0, 0), 0, 0));
    tra_nombL = tra_nombE;

  }
  /**
   * @deprecated usar setCampoNombre(CTextField tran_nombE)
   * @param tra_nombE CTextField
   */
  public void setPrvNomb(CTextField tra_nombE)
  {
    setCampoNombre(tra_nombE);
  }




  public CTextField getTextField()
  {
    return tra_codiE;
  }

  public void setColumnaAlias(String alias)
  {
    tra_codiE.setColumnaAlias(alias);
  }

  public void setControla(boolean swControla)
   {
     swControl=swControla;
   }


  /**
   * Por si alguien quiere saber si se han llenado los campos.
   */
  protected void llenaCampos1()
  {

  }
  /**
   * Linka este control con los Campos Mandados en la Base de Datos.
   */
  /*  public void setConectaDB(DatosTabla datTabla, String campo)
    {
      tra_codiE.setConectaDB(datTabla,campo);
    }
   */
  public void setAncTexto(int anc)
  {
    ancTexto = anc;
    tra_codiE.setPreferredSize(new Dimension(ancTexto, 16));
    tra_codiE.setMinimumSize(new Dimension(ancTexto, 16));
    tra_codiE.setMaximumSize(new Dimension(ancTexto, 16));
    validate();
    repaint();
  }

  public int getAncTexto()
  {
    return ancTexto;
  }

  void ponOrejas()
  {
    prvCodi=tra_codiE.getValorInt();
    if (swControl)
    {
      tra_codiE.addFocusListener(new FocusAdapter()
      {
        public void focusLost(FocusEvent e)
        {
          tra_codiE_focusLost();
        }
      });
    }
    tra_codiE.addKeyListener(new KeyAdapter()
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
        consTra();
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
        consTra();
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
        tra_codiE.addFocusOrejas(f);
    }
   */
  public void addKeyListener(KeyListener kl)
  {
    tra_codiE.addKeyListener(kl);
  }
  public void addFocusListener(FocusListener focLis)
  {
    if (tra_codiE != null)
      tra_codiE.addFocusListener(focLis);
  }
  public void setName(String name)
  {
    if (tra_codiE != null)
      tra_codiE.setName(name);
  }
  public String getName()
  {
    if (tra_codiE == null)
      return null;
    return tra_codiE.getName();
  }

  /**
   * Consulta Productos.
   * Llama a la Ayuda de Transportistas
   */
  public void consTra()
  {
    final ayuTra ayTra;

    try
    {
      ayTra = new ayuTra(eu, vl);
      ayTra.addInternalFrameListener(new InternalFrameAdapter()
      {
        public void internalFrameClosing(InternalFrameEvent e)
        {
          ej_consTra(ayTra);
        }
      });

      vl.add(ayTra);
      ayTra.setLocation(25, 25);
      ayTra.setVisible(true);
      if (intfr != null)
      {
        intfr.setEnabled(false);
        intfr.setFoco(ayTra);
      }
      ayTra.iniciarVentana();

    }
    catch (Exception j)
    {
      if (intfr != null)
        intfr.setEnabled(true);
    }
  }

  void ej_consTra(ayuTra aytra)
  {
    if (aytra.consulta)
    {
      tra_codiE.setText(aytra.tra_codiT);
      if (swControl)
         tra_nombL.setText(aytra.tra_nombT);
    }
    tra_codiE.requestFocus();
    aytra.dispose();

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
    tra_codiE.setAutoNext(s);
  }

  public boolean getAutoNext()
  {
    return tra_codiE.getAutoNext();
  }

  /**
   * Controla si el Codigo de Transportista es Valido.
   * @return true si el Campo es Valido.
   *         false si No lo es.
   * <p> En el Caso de que el campo no sea valido se hace un requestFocus
   *  al tra_codi
   * @see getMsgError
   */
  public boolean controlar() throws SQLException
  {
    return controla(true,true);
  }

  public boolean hasCambio()
  {
    return tra_codiE.hasCambio();
  }

  /**
   * Controla si el Codigo de Transportista es Valido.
   * @return true si el Campo es Valido.
   *         false si No lo es.
   * @throw Exception si hay un error de base de datos al buscar el
   * nombre del Transportista.
   * @param indica si debe hacer el requestFocus al tra_codi en caso de Error
   * @see getMsgError
   */

  public boolean controla(boolean reqFocus,boolean ponNombre) throws SQLException
  {
    msgError = "";
    if (isNull())
    {
      if (tra_nombL!=null)
        tra_nombL.setText("");
      if (getAceptaNulo())
        return true;
      else
      {
        msgError = "Campo de Transportista es obligatorio";
        if (reqFocus)
          tra_codiE.requestFocus();
        return false;
      }
    }

    if (dt == null)
      return false;
    try {
      Integer.parseInt(tra_codiE.getText().trim());
    } catch (Exception k)
    {
      msgError="Codigo de Transportista NO valido";
      if (reqFocus)
        tra_codiE.requestFocus();
      return false;
    }
    String s=getNombTra(tra_codiE.getValorInt());

    if (s==null)
    {
      if (ponNombre)
        tra_nombL.setText("**Transportista NO Encontrado");
      if (reqFocus)
        tra_codiE.requestFocus();
      msgError = "** Transportista NO Encontrado**";

      return false;
    }
    if (ponNombre)
      tra_nombL.setText(s);
    afterControla();
    return true;
  }

  /**
   * Machacar para hacer algo despues de llamar a controlar.
   */
  protected void afterControla()
  {

  }

  public String getNombTra(int codTra, DatosTabla dt) throws SQLException
  {
    String s = "SELECT tra_nomb FROM v_transport WHERE tra_codi= " + codTra;
    dt.select(s);
    if (dt.getNOREG())
      return null;
    return dt.getString("tra_nomb");
  }

  public String getNombTra(int codTra) throws SQLException
  {
    return getNombTra(codTra,dt);
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
  public void setEnabledNombre(boolean enab)
  {
    tra_nombL.setEnabled(enab);
  }
  public boolean isEnabledNombre()
  {
    return tra_nombL.isEnabled();
  }
  public CTextField getCampoNombre()
  {
    return tra_nombL;
  }

}
