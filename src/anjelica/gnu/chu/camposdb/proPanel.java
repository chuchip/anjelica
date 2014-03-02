package gnu.chu.camposdb;

import gnu.chu.anjelica.pad.MantArticulos;
import gnu.chu.controles.CButton;
import gnu.chu.controles.CInternalFrame;
import gnu.chu.controles.CPanel;
import gnu.chu.controles.CTextField;
import gnu.chu.sql.DatosTabla;
import gnu.chu.sql.vlike;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.mensajes;
import gnu.chu.winayu.AyuArt;
import gnu.chu.winayu.ayuLote;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashSet;
import javax.swing.JLayeredPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

/**
 *  Panel con el Código de Producto y Nombre de Producto.
 * @Author Chuchi P
 * <p>Copyright: Copyright (c) 2005-2013
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
 * <p>Empresa: MISL</p>
 * @author chuchiP
 * @version 2.0 Implementada ayuda lotes.
 */
public class proPanel extends CPanel
{
  private int almCodi=0; // Almacen donde buscar indiv. disponibles
  private boolean verIndivBloqueados=true;
  private CTextField campoLote;
  private ayuLote ayuLot = null;
  private boolean ayudaLotes=false; // Implementa llamada a ventana ayuda lotes
  private int etiCodi=0;
  private int rseCodcli=0;
  private boolean aceptaInactivo=true; // Acepta productos Inactivos
  private boolean isEntrada=false;
  private int activo;
  int sbeCodi=0;
  AyuArt aypro;
  boolean prodEqu=false;
  int proCodiAnt=0;
//  boolean refer=false;
  int proCoinst;
  private char tipLote='-',proConExist='-';
  private boolean controlIndiv=true;
  private boolean swControl=true;
  private CTextField prp_anoE=null;
  private CTextField emp_codiE;
  private CTextField prp_serieE;
  private CTextField prp_partE;
  private CTextField prp_indiE;
  private CTextField prp_pesoE;
  boolean aceptaNulo = true;
  boolean inHistorico = false;
  boolean swIniciar = false;
  String famCodi = "";
  String sefCodi = "";
  String camCodi="";
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  public CTextField pro_codiE = new CTextField(Types.DECIMAL, "####9");
  public CTextField pro_nombL = new CTextField();
  public vlike lkPrd=new vlike();
  CButton Bcons = new CButton(Iconos.getImageIcon("find"))
  {
        @Override
    public boolean isFocusTraversable()
    {
      return false;
    }
  };
  CButton Binserta = new CButton(Iconos.getImageIcon("nuevo"))
  {
        @Override
    public boolean isFocusTraversable()
    {
      return false;
    }
  };
  int ancTexto = 50;
  EntornoUsuario eu1;
  boolean ponBcons = true;
  boolean ponBins = false;
  String msgError = "";
  DatosTabla dt;
  EntornoUsuario eu;
  JLayeredPane vl;
  CInternalFrame intfr;


  public proPanel()
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
    pro_codiE.setPreferredSize(new Dimension(ancTexto, 16));
    pro_codiE.setMinimumSize(new Dimension(ancTexto, 16));
    pro_codiE.setMaximumSize(new Dimension(ancTexto, 16));
    pro_nombL.setDisabledTextColor(Color.black);
    pro_nombL.setPreferredSize(new Dimension(200, 17));
    pro_nombL.setEnabled(false);
    pro_nombL.setBackground(Color.orange);
    pro_nombL.setForeground(Color.black);
    pro_nombL.setOpaque(true);
    Bcons.setPreferredSize(new Dimension(17, 17));
    Bcons.setMinimumSize(new Dimension(17, 17));
    Bcons.setMaximumSize(new Dimension(17, 17));
    Binserta.setPreferredSize(new Dimension(17, 17));
    Binserta.setMinimumSize(new Dimension(17, 17));
    Binserta.setMaximumSize(new Dimension(17, 17));
    this.setLayout(gridBagLayout1);
    this.add(pro_codiE, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.WEST,
                                               GridBagConstraints.VERTICAL,
                                               new Insets(0, 0, 0, 0), 0, 0));
    this.add(pro_nombL, new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0
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

    @Override
  public void requestFocus()
  {
    pro_codiE.requestFocus();
  }

    @Override
  public void resetTexto()
  {
    pro_codiE.resetTexto();
    pro_nombL.resetTexto();
  }
    @Override
  public void resetCambio()
  {
    pro_codiE.resetCambio();
    proCodiAnt=pro_codiE.getValorInt();
  }
    @Override
  public boolean hasCambio()
  {
    return pro_codiE.hasCambio();
  }

  public void pro_codiE_focusLost()
  {
    if (pro_codiE.getValorInt()!=proCodiAnt)
    {
      proCodiAnt=pro_codiE.getValorInt();
      try
      {
        afterFocusLost(controla(false,true));
      }
      catch (Exception k)
      {}
    }

  }
  public static CTextField getTextFieldCodi()
  {
      return  new CTextField(Types.DECIMAL, "####9"); 
  }

  /**
   * Obliga a que en el proximo focus_lost se ejecute la rutina controla()
   *
   */
  public void setCambio()
  {
    proCodiAnt=-1;
  }
  public void setProdEquiv(boolean prodEqu)
  {
    this.prodEqu=prodEqu;
    if (prodEqu)
    {
      pro_codiE.setTipoCampo(Types.CHAR);
      pro_codiE.setFormato("X");
      pro_codiE.setMaxLong(12);
      pro_codiE.setMayusc(true);
      pro_codiE.setPreferredSize(new Dimension(100, 16));
      validate();
      repaint();
    }
    else
    {
      pro_codiE.setTipoCampo(Types.DECIMAL);
      pro_codiE.setFormato("####9");
      pro_codiE.setPreferredSize(new Dimension(getAncTexto(),16));
      validate();
      repaint();
    }
  }
  public boolean getProdEquiv()
  {
    return prodEqu;
  }
  /**
   * Machacar si se quiere hacer algo despues de un FocusLost
   * @param correcto Indica si el valor del campo (controla devolvio true) es correcto.
   */
  public void afterFocusLost(boolean correcto)
  {

  }


  public boolean isNull()
  {
    if (pro_codiE.getText().trim().equals(""))
      return true;
    try {
      Integer.parseInt(pro_codiE.getText().trim());
    } catch (Exception k){return false;}
    if (pro_codiE.getValorDec() == 0)
      return true;
    return false;
  }

  public void setText(String proCodi)
  {
    setText(proCodi,false);
  }
  /**
   * Pone el Texto del Campo de Entrada
   * @param proCodi - Codigo de Producto
   * @param actNomb -- Actualizar Nombre aunque este disabled
   * <p>
   * Actualizara el Campo del Nombre del representante.
   */
  public void setText(String proCodi,boolean actNomb)
  {
    pro_codiE.setText(proCodi);
    try {
      if (! swControl)
        return; // Pasa de buscar y poner el nombre del producto.
      if (!pro_codiE.isEnabled() || actNomb)
        controla(false,true);
    } catch (Exception k)
    {
      k.printStackTrace();
    }
  }

  public int getValorInt()
  {
    return pro_codiE.getValorInt();
  }

  public void setValorInt(int valor)
  {
    setValorInt(valor,false);
  }
  public void setValorInt(int valor,boolean actNomb)
  {
    setText("" + valor,actNomb);
  }
  public void setControla(boolean swControla)
  {
    swControl=swControla;
  }
  public String getStrQuery()
  {
    return pro_codiE.getStrQuery();
  }

  public void setQuery(boolean query)
  {
    pro_codiE.setQuery(query);
//    Bcons.setEnabled(!query);
//    Binserta.setEnabled(!query);
  }

  public boolean getQuery()
  {
    return pro_codiE.getQuery();
  }

  /*
   * Devuelve el Codigo Producto
   * ej: 01002
   */
  public String getText()
  {
    return pro_codiE.getText();
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

  public String getTextNomb()
  {
    return pro_nombL.getText();
  }

  /**
   * Iniciar Control
   * @param datTabla DatosTabla .. Utilizado para Iniciar los campos con Mascara y para realizar
   *                  la select de buscar el nombre de Producto.
   * @param  intFrame CInternalFrame.. Mandado como parametro al llamar al AyuArt al pulsar botones.
   * @param layPan JLayeredPane .. Mandado como parametro al llamar al AyuArt al pulsar botones.
   * @param entUsu EntornoUsuario .. Mandado a los programas de ayuda o alta y utilizado para poder
   *                  realizar sus propias select (para buscar el nombre del
   *                  representante.
   * @throws SQLException
   * @throws java.text.ParseException
   *
   */
  public void iniciar(DatosTabla datTabla, CInternalFrame intFrame,
                      JLayeredPane layPan, EntornoUsuario entUsu) throws SQLException
  {
    intfr = intFrame;
    dt = datTabla;
    vl = layPan;
    eu = entUsu;
//    pro_codiE.setMascaraDB(dt,eu.em_cod);
    ponOrejas();
    if (swControl)
    {
      setText("");
      controla(false,true);
    }
    setAceptaNulo(false);
    swIniciar = true;
  }
  public boolean isIniciado()
  {
          return swIniciar;
  }
  /**
   *
   * @param pro_nombE CTextField poner a NULL si no se quiere actualizar el nombre
   *                  del producto.
   */
  public void setProNomb(CTextField pro_nombE)
  {
    if (pro_nombE==null)
      swControl = false;
    else
      swControl = true;

    this.remove(pro_nombL);
    this.remove(Bcons);
    if (!swControl)
      return;
    this.remove(pro_codiE);
    this.add(pro_codiE, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
                                              , GridBagConstraints.WEST,
                                              GridBagConstraints.BOTH,
                                              new Insets(0, 0, 0, 0), 0, 0));

    pro_nombL=pro_nombE;
  }
  public void setColumnaAlias(String alias)
  {
    pro_codiE.setColumnaAlias(alias);
  }
public void setCamposLote(CTextField prp_anoE,CTextField prp_seriE,
                            CTextField prp_partE,
                            CTextField prp_indiE,CTextField prp_pesoE)
{
  setCamposLote(prp_anoE,null,prp_seriE,prp_partE,prp_indiE,prp_pesoE);
}

  /**
   * Establece los Campos que definen el Lote.
   * Les pone un KeyListener para que al pulsar F3 salga la ventana de ayuda
   * con los lotes disponibles
   * @param prp_anoE CTextField
   * @param emp_codiE CTextField
   * @param prp_seriE CTextField
   * @param prp_partE CTextField
   * @param prp_indiE CTextField
   * @param prp_pesoE CTextField
   */
  public void setCamposLote(CTextField prpAnoE,CTextField empCodiE,CTextField prpSeriE,
                            CTextField prpPartE,
                            CTextField prpIndiE,CTextField prpPesoE)
  {
    this.prp_anoE=prpAnoE;
    this.emp_codiE=empCodiE;
    this.prp_serieE=prpSeriE;
    this.prp_partE=prpPartE;
    this.prp_indiE=prpIndiE;
    this.prp_pesoE=prpPesoE;
    pro_codiE.setIncluirComodines(false);
    pro_codiE.setAutoNext(false);
    pro_codiE.setTipoCampo(Types.CHAR);
    pro_codiE.setFormato("X");
    pro_codiE.setMaxLong(24);
    pro_codiE.setMayusc(true);
    // Quita que el ENTER actue igul que el TABULADOR
    HashSet hs = new HashSet();
    hs.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_TAB, 0));
    this.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, hs);



    pro_codiE.addKeyListener(new KeyAdapter()
    {
            @Override
      public void keyPressed(KeyEvent e)
      {
        if (!e.isAltDown())
        {
          switch (e.getKeyCode())
          {
            case KeyEvent.VK_ENTER:
              llenaCampos();
          }
        }
      }
    });
    prp_anoE.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyPressed(KeyEvent e)
      {
        campoLote=prp_anoE;
        procesaTeclaLote(e);
      }
    });
    prp_indiE.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyPressed(KeyEvent e)
      {
        campoLote=prp_indiE;
        procesaTeclaLote(e);
      }
    });
    prp_serieE.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyPressed(KeyEvent e)
      {
        campoLote=prp_serieE;
        procesaTeclaLote(e);
      }
    });
    prp_partE.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyPressed(KeyEvent e)
      {
        campoLote=prp_partE;
        procesaTeclaLote(e);
      }
    });
    if (emp_codiE!=null)
    {
        emp_codiE.addKeyListener(new KeyAdapter()
        {
          @Override
          public void keyPressed(KeyEvent e)
          {
            campoLote=emp_codiE;
            procesaTeclaLote(e);
          }
        });
    }
  }

  protected void antesLlenaCampos()
  {

  }
  public void llenaCampos()
  {
    if (pro_codiE.getText().length()!=23 && pro_codiE.getText().length()!=24)
    {
      if (pro_nombL.isEnabled())
      {
         if (pro_nombL.getGridEditable()!=null)
              return;
         pro_nombL.requestFocus();
      }
      else
      {
        if (prp_anoE.getGridEditable()!=null) return;
//              prp_anoE.getGridEditable().requestFocus(prp_anoE.getRowGrid());
        prp_anoE.requestFocus();
      }
      return;
    }
    antesLlenaCampos();
//    System.out.println("LLenar campos");
    String valor=pro_codiE.getText();
    try {
      prp_anoE.setValorDec(Integer.parseInt(valor.substring(0, 2))+2000);
    } catch (Exception k){}
    /*
     Año: 03
     Emp: 1 o 01
     Serie: A
     Partida: 1240 o 01240
     Producto: 10951
     Individuo: 001
     Peso: 025.33
     Ejemplo: 0301A124010951001025.33
              04-1-A-02381-10601-001-010.00
              041A0238110601001010.00
              051A0000100001001011.00
              051A0034510601225023.00
              051A0000100001004002.00
              061A0000100001012005.00
              061A0000610601031021.00
              071A0015110601054013.10
     */

    // Localizo donde esta la Serie.
    int posSerie=4;
    for (int n=2;n<6;n++)
    {
      if (!Character.isDigit(valor.charAt(n)))
        posSerie=n; // NO Es tipo  Digito. Hemos encontrado la serie
    }
    if (posSerie==4)
    {
      if (emp_codiE!=null)
        emp_codiE.setText(valor.substring(2,4)); // la empresa usa un solo digito (Antiguo sistema)
      prp_serieE.setText(valor.substring(4,5));
      prp_partE.setText(valor.substring(5,9));
    }
    else
    {
      if (emp_codiE!=null)
        emp_codiE.setText(valor.substring(2, 3)); // la empresa usa un solo digito.
      prp_serieE.setText(valor.substring(3, 4));
      prp_partE.setText(valor.substring(4, 9));
    }
    pro_codiE.setText(valor.substring(9,14));
    if (valor.length()==23)
    {
      prp_indiE.setText(valor.substring(14,17));
      prp_pesoE.setText(valor.substring(17,23));
    }
    else
    {
      prp_indiE.setText(valor.substring(14,18));
      prp_pesoE.setText(valor.substring(18,23));
    }
    proCodiAnt=0;
    pro_codiE_focusLost();
    despuesLlenaCampos();
  }

 
  /**
   * Por si alguien quiere saber si se han llenado los campos.
   * Es utilizada cuando se mete un código de barras en este control y se han puesto
   * los correspondientes campos de individuo,lote, etc.
   * Es llamada esta función después de haber rellenado los campos del código de barras
   * y haber puesto el código del producto.
   */
  protected void despuesLlenaCampos()
  {

  }

  /**
   * Establece el Ancho del campo del codigo del producto
   * @param anc int
   */
  public void setAncTexto(int anc)
  {
    ancTexto = anc;
    pro_codiE.setPreferredSize(new Dimension(ancTexto, 16));
    pro_codiE.setMinimumSize(new Dimension(ancTexto, 16));
    pro_codiE.setMaximumSize(new Dimension(ancTexto, 16));

    validate();
    repaint();
  }

  public int getAncTexto()
  {
    return ancTexto;
  }

  void ponOrejas()
  {
    if (swControl)
    {
      pro_codiE.resetCambio();
      pro_codiE.addFocusListener(new FocusAdapter()
      {
                @Override
        public void focusLost(FocusEvent e)
        {
          pro_codiE_focusLost();
        }
      });
    }
    pro_codiE.addKeyListener(new KeyAdapter()
    {
            @Override
      public void keyPressed(KeyEvent e)
      {
        procesaTecla(e);
      }
    });

    Bcons.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        consPro();
      }
    });
    Binserta.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        altaPro();
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
  public void addMouseListener(MouseListener l)
  {
    if (pro_codiE!=null)
      pro_codiE.addMouseListener(l);
  }
  public void requestFocusLater()
  {
     pro_codiE.requestFocusLater();
  }
  void procesaTecla(KeyEvent e)
  {
    if (ponBcons)
    {
      if (e.getKeyCode() == KeyEvent.VK_F3 && isEditable())
      {
        consPro();
        return;
      }
    }
    if (ponBins)
    {
      if (e.getKeyCode() == KeyEvent.VK_INSERT && isEditable())
      {
        altaPro();
        return;
      }
    }
  }
    @Override
  public boolean isEditable()
  {
      if (pro_codiE==null)
          return super.isEditable();
      return pro_codiE.isEditable();
  }
//    @Override
//  public boolean isEnabled()
//  {
//      if (pro_codiE==null)
//          return super.isEnabled();
//      return pro_codiE.isEnabled();
//  }
//    @Override
//  public void setEnabled(boolean b)
//  {
//        if (pro_codiE==null)
//        {
//           super.setEnabled(b);
//           return;
//        }
//        super.setEnabled(b);
//        pro_codiE.setEnabled(b);
//  }
  void procesaTeclaLote(KeyEvent e)
  {
      if (! ayudaLotes)
          return;
       if (e.getKeyCode() != KeyEvent.VK_F3 ||  ! isEditable() || ! isEnabled())
                   return;
       
      if (pro_codiE.getValorInt()==0)
      {
          mensajes.mensajeAviso("Introduzca Codigo Producto para consultar lotes disponbiles");
          return;
      }
      ayudaLote();
  }

    @Override
  public void addKeyListener(KeyListener kl)
  {
    pro_codiE.addKeyListener(kl);
  }
    @Override
  public void addFocusListener(FocusListener focLis)
  {
    if (pro_codiE != null)
      pro_codiE.addFocusListener(focLis);
  }
  public void setName(String name)
  {
    if (pro_codiE != null)
      pro_codiE.setName(name);
  }
    @Override
  public String getName()
  {
    if (pro_codiE == null)
      return null;
    return pro_codiE.getName();
  }
  void altaPro()
  {
  }
  /**
   * Consulta Productos.
   * Llama a la Ayuda de Productos.
   */
  public void consPro()
  {
    try
    {
      if (aypro == null)
      {
        aypro = new AyuArt(eu, vl, dt)
        {
          @Override
          public void matar()
          {
            ej_consPro(aypro);
          }
        };
        vl.add(aypro);
        aypro.setLocation(25, 25);
        aypro.iniciarVentana();
      }

      aypro.setVisible(true);
      if (intfr!=null)
      {
        intfr.setEnabled(false);
        intfr.setFoco(aypro);
      }
      aypro.reset();
      aypro.setSeccionCliente(rseCodcli);
      
    }
    catch (Exception j)
    {
      if (intfr != null)
        intfr.setEnabled(true);
    }
  }

  void ej_consPro(AyuArt aypro)
  {
    if (aypro.getChose())
    {
      pro_codiE.setValorInt(aypro.getProCodi());
      if (swControl)
        pro_nombL.setText(aypro.getProNomb());
    }
    pro_codiE.requestFocus();
    aypro.setVisible(false);

    if (intfr != null)
    {
      intfr.setEnabled(true);
      intfr.toFront();
      try
      {
        intfr.setSelected(true);
      }
      catch (PropertyVetoException k)
      {}
      intfr.setFoco(null);
      this.requestFocus();
    }
  }

  public void setAutoNext(boolean s)
  {
    pro_codiE.setAutoNext(s);
  }

  public boolean getAutoNext()
  {
    return pro_codiE.getAutoNext();
  }

  /**
   * Controla si el Codigo de Producto es Valido.
   * @return true si el Campo es Valido.
   *         false si No lo es.
   * <p> En el Caso de que el campo no sea valido se hace un requestFocus
   *  al pro_codi
   * @see getMsgError
   */

  /**
   * Controla si el Codigo de Producto es Valido.
   * <p> En el Caso de que el campo no sea valido se hace un requestFocus
   *  al pro_codi </p>
   * @see getMsgError
   * @throws Exception
   * @deprecate usar controla
   * @return boolean true si el Campo es Valido.
   *         false si No lo es.
   */
  public boolean controlar() throws SQLException
  {
    return controla(true,true);
  }
   /**
   * Controla si el Codigo de Producto es Valido.
   * <p> En el Caso de que el campo no sea valido se hace un requestFocus
   *  al pro_codi </p>
   * @see getMsgError
   * @throws Exception
   * @return boolean true si el Campo es Valido.
   *         false si No lo es.
   */
  public boolean controla() throws Exception
  {
    return controla(true,true);
  }
  public boolean controla(boolean reqFocus) throws Exception
  {
    return controla(reqFocus,true);
  }
  /**
   * El codigo sera la referencia no el numero
   * @deprecated usar setProdEquiv(true);
   */
  public void setRefer()
  {
    setProdEquiv(true);
  }
  public JPopupMenu getPopMenu()
  {
    return pro_codiE.getMenuContestual().getPopMenu();
  }
  public boolean controla(boolean reqFocus,boolean ponTexto) throws SQLException
  {
      return controla(reqFocus,ponTexto,0);
  }
  /**
   * Controla si el Codigo de Producto es Valido.
   * @return true si el Campo es Valido.
   *         false si No lo es.
   * @throws SQLException si hay un error de base de datos al buscar el
   * nombre del Producto
   * @param reqFocus indica si debe hacer el requestFocus al pro_codi en caso de Error
   * @param ponTexto indica si el cambo pro_nomb debe ser actualizado
   * @param sbeCodi seccion(subempresa) de la venta. 0 = No controlar.
   * @see getMsgError
   */
  public boolean controla(boolean reqFocus,boolean ponTexto,int sbeCodi) throws SQLException
  {
    msgError = "";
    if (pro_codiE.isNull())
    {
      pro_nombL.setText("");
      if (getAceptaNulo())
        return true;
      else
      {
        msgError = "Producto no puede estar en blanco";
        return false;
      }
    }

    if (dt == null)
      return false;
    inHistorico = false;
    if (! prodEqu)
    {
      try
      {
        Integer.parseInt(pro_codiE.getText().trim());
      }
      catch (Exception k)
      {
        msgError = "Codigo de Producto NO valido";
        return false;
      }
    }
    String s=getNombArt(pro_codiE.getText());

    if (s==null)
    {
      if (ponTexto)
        pro_nombL.setText("**Producto NO Encontrado");
      if (reqFocus)
        pro_codiE.requestFocus();
      msgError = "** Producto NO Encontrado**";
      return false;
    }
    if (sbeCodi!=0 && lkPrd.getInt("sbe_codi")!=99)
    { //Se ha definido seccion de venta y producto NO es accesible por todas las ventas.
        s="select * from relsubempr where rse_codcli="+sbeCodi;
        if (dt.select(s))
        { // Existen registros para esta secion de venta.
            boolean swPerm=false;
            do
            {
                if (dt.getInt("rse_codart")==lkPrd.getInt("sbe_codi"))
                {
                    swPerm=true;
                    break;
                }
            } while (dt.next());
            if (! swPerm)
            {
                 msgError = "**ARTICULO NO VENDIBLE EN ESTA SECCION**";
                  if (ponTexto)
                 pro_nombL.setText(msgError);
                 if (reqFocus)
                    pro_codiE.requestFocus();
                return false;
            }
        }
    }
    if (ponTexto)
      pro_nombL.setText(s);
    if (aceptaInactivo)
        return true;
    if (! isActivo())
    {
        if (ponTexto)
            pro_nombL.setText("(I) "+pro_nombL.getText());
        msgError="Producto esta marcado como INACTIVO";
        return false;
    }
    return true;
    
  }
  /**
   * Devuelve el nombre dado a ese cliente para un producto.
   * @param codArt Codigo de Articulo
   * @param codCli Codigo de Cliente
   * @return nombre de articulo. Null si no existe
   * @throws SQLException 
  */
  public String getNombArtCli(int codArt,int codCli) throws SQLException
  {
    return getNombArtCli(codArt,codCli,eu.em_cod,dt);
  }
  /**
   * Devuelve el nombre dado a ese cliente para un producto
   * @param proCodi  Codido de Articulo
   * @param codCli COodigo cliente
   * @param empCodi Empresa (normalmente siempre 1)
   * @return nombre de articulo. Null si no existe
   * @throws SQLException 
   */
  public String getNombArtCli(int proCodi,int codCli,int empCodi) throws SQLException
  {
    return getNombArtCli(proCodi,codCli,empCodi,dt);
  }
  /**
   * Devuelve el nombre dado a ese cliente para un producto.
   * 
   * @param proCodi
   * @param codCli
   * @param empCodi
   * @param dt
   * @return
   * @throws SQLException
   * 
   */
  public String getNombArtCli(int proCodi,int codCli,int empCodi,DatosTabla dt) throws SQLException
  {
    String s;
    s = "SELECT * FROM v_articulo WHERE pro_codi= " + proCodi;
    dt.selectInto(s, lkPrd);
    if (dt.getNOREG())
      return null;
    s="SELECT * FROM v_cliart WHERE cli_codi = "+codCli+
        " and pro_codi = "+proCodi;
    if (dt.select(s))
      return dt.getString("pro_nomb");
    else
      return lkPrd.getString("pro_nomb");
  }
  public String getNombArt() throws SQLException
  {
    return getNombArt(pro_codiE.getText(),eu.em_cod);
  }
/**
   * Devuelve el nombre del articulo
   * @param codArt Codigo Articulo
   * @return Nombre de Articulo. Null si no lo encuentra.
   * @throws SQLException
   */
  public String getNombArt(String codArt) throws SQLException
  {
    return getNombArt(codArt,eu.em_cod);
  }
  /**
   * Devuelve el nombre del articulo
   * @param codArt Código Articulo
   * @return Nombre de Articulo. Null si no lo encuentra.
   * @throws SQLException
   */
  public String getNombArt(int codArt) throws SQLException
  {
    return getNombArt(""+codArt,eu.em_cod);
  }
  public String getNombArt(String codArt,int empCodi) throws SQLException
  {
    return getNombArt(codArt,empCodi,0);
  }
  public String getNombArt(String codArt,int empCodi,int prvCodi) throws SQLException
  {
    return getNombArt(codArt,empCodi,prvCodi,dt);
  }
  /**
   * Devuelve el nombre del articulo
   * @param codArt Código Articulo
   * @param empCodi Empresa de Articulo
   * @param prvCodi Proveedor (para buscar en pedidos de compras)
   * @param dt DatosTabla
   * @return Nombre de Articulo. Null si no lo encuentra.
   * @throws SQLException
   */
  public String getNombArt(String codArt,int empCodi,int prvCodi,DatosTabla dt) throws SQLException
  {
    if (dt.getConexion().isClosed())
        return null;
    tipLote='-';
    activo=-1;
    etiCodi=0;
    proConExist='-';
    controlIndiv=true;
    String s;
    if (codArt==null)
      return "";
    if (codArt.equals(""))
      return "";
    try {
      codArt = ""+Integer.parseInt(codArt.trim());
    } catch (NumberFormatException k){codArt="0";}
    if (prodEqu)
    {
      s = "SELECT * FROM refproeq WHERE pro_codi= '" + codArt +"'"+
          " and emp_codi = " + empCodi;
      if (! dt.select(s))
      {
        lkPrd.setColumnCount(0);
        return null;
      }
      codArt=dt.getString("pro_nume");
    }
    if (prvCodi==0)
      s = "SELECT * FROM v_articulo WHERE pro_codi= " + codArt;
    else
    {
      s="SELECT * FROM pedicol WHERE emp_codi = "+empCodi+
      " and prv_codi = "+prvCodi+
      " and pro_codi = "+codArt+
      " order by eje_nume desc,pcc_nume desc";
      if (! dt.select(s))
        s = "SELECT * FROM v_articulo WHERE pro_codi= " + codArt;
      else
      {

        s = "SELECT pro_nomb FROM pedicol WHERE emp_codi = " + empCodi +
            " and eje_nume =  "+dt.getInt("eje_nume")+
            " and pcc_nume = "+dt.getInt("pcc_nume")+
            " and pro_codi = "+codArt;
//            System.out.println("proPanel: "+s);
      }
    }
    dt.selectInto(s,lkPrd);
    if (dt.getNOREG())
      return null;
    
    if (prvCodi==0)
    {
      camCodi= dt.getString("cam_codi");
      sbeCodi=dt.getInt("sbe_codi");
      famCodi = dt.getString("fam_codi");
      proCoinst = dt.getInt("pro_coinst");
      activo=dt.getInt("pro_activ");
      tipLote = (dt.getString("pro_tiplot").length()>=1)? dt.getString("pro_tiplot").charAt(0):'-';
      etiCodi=dt.getInt("pro_codeti");
      proConExist =dt.getString("pro_coexis").length()>=1?dt.getString("pro_coexis").charAt(0):'-';
      controlIndiv=dt.getInt("pro_coinst")!=0;
    }
    return dt.getString("pro_nomb");
  }
  public boolean isCongelado() throws SQLException
  {
      return getLikeProd().getInt("pro_artcon")!=0;
  }
  public vlike getLikeProd()
  {
    return lkPrd;
  }
  public String getFamilia()
  {
    return famCodi;
  }
  public boolean getMantenerCosto() throws SQLException
  {
      return lkPrd.getInt("pro_mancos")!=0;
  }
  public int getDiasCad() throws SQLException
  {
      return lkPrd.getInt("pro_diacom");
  }
  public String getCamara()
  {
    return camCodi;
  }

  public int getSubEmpresa()
  {
    return sbeCodi;
  }
  /**
   * Devuelve La familia del producto
   * @return  int con la familia
   */
  public int getFamInt()
  {
    return Integer.parseInt(famCodi.trim());
  }

  public boolean esHistorico()
  {
    return inHistorico;
  }
  public void setCeroIsNull(boolean ceroIsNull)
  {
    pro_codiE.setCeroIsNull(ceroIsNull);
  }

  public String getMsgError()
  {
    return msgError;
  }

  public boolean getAceptaNulo()
  {
    return aceptaNulo;
  }

    @Override
  public void setEditable(boolean edit)
  {
    if (pro_codiE==null)
         return;
    pro_codiE.setEditable(edit);
  }
 
  /**
   * Indica si se acepta como valido dejar el campo vacio,
   * cuando es llamado a controlar.
   * Por defecto no se aceptan valores nulos.
   * 
   * @param acepNulo true Acepta Nulo
   */
  public void setAceptaNulo(boolean acepNulo)
  {
    aceptaNulo = acepNulo;
  }
  public CTextField getTextField()
  {
    return pro_codiE;
  }
  public boolean getConStkInd()
  {
    return proCoinst==-1;
  }
  /**
   * Controlar Stock?.
   * @return 'S' o 'N'
   */
  public char getControlExist()
  {
    return proConExist;
  }
  /**
   * Devuelve si este producto tiene control sobre individuos (En Stock-Partidas)
   * @return
   */
  public boolean hasControlIndiv()
  {
    return controlIndiv;
  }
  /**
   * Devuelve el tipo de Lote
   * Si es 'V' significa que el producto es Vendible. En caso contrario sera un
   * producto para comentarios o Desechable
   * @return char
   */
  public char getTipoLote()
  {
    return tipLote;
  }

  public void setSeccionCliente(int rseCodcli)
  {
    this.rseCodcli=rseCodcli;
  }
  
  public int getSeccionCliente()
  {
      return rseCodcli;
  }
   public void setAlmacen(int almCodi)
  {
    this.almCodi=almCodi;
  }
  
  public int getAlmacen()
  {
      return almCodi;
  }
  /**
   * Devuelve si un producto es Vendible (true) o comentario false
   * @return boolean
   */
  public boolean isVendible()
  {
    return getTipoLote()==MantArticulos.TIPO_VENDIBLE.charAt(0);
  }
  /**
   * Acepta como validos productos inactivos
   * True aceptara como validos los productos innactivos.
   * @param aceptInact  
   */
  public void setAceptaInactivo(boolean aceptInact)
  {
      this.aceptaInactivo=aceptInact;
  }
  public boolean getAceptaInactivo()
  {
      return aceptaInactivo;
  }
  public int getEtiCodi() {
        return etiCodi;
  }
  /**
   * Devuelve si el articulo actual es activo.
   * Tiene en cuenta la variable isEntrada para decidirlo.
   * @return true es activo
   */
  public boolean isActivo()
  {
    if (isEntrada)
        return activo<0; // Solo es activo si = -1
    else
        return activo!=0;// Es activo si es diferente de 0
  }
  /**
   * Indica que este campo sera usado para dar entrada. Si es true (por defecto es falso),
   * la funcion isActivo devolvera false si el valor de pro_activ>0
   * Usado para marcar un producto como inactivo pero que todavia tiene stock y
   * en salidas debe poder ser usado.
   */
  public void setEntrada(boolean isEntrada)
  {
      this.isEntrada=isEntrada;
  }
  public boolean getEntrada()
  {
      return this.isEntrada;
  }
    @Override
  protected void finalize() throws Throwable 
  {
    if (aypro!=null)
      aypro.dispose();
    aypro=null;
    if (ayuLot!=null)
        ayuLot.dispose();
    ayuLot=null;
    super.finalize();
  }
  /**
   * Establece si estara disponible la ayuda para buscar lotes disponibles
   * @see setCamposLote
   * @param ayudaLotes
   */
  public void setAyudaLotes(boolean ayudaLotes) // throws IllegalAccessException
  {
    this.ayudaLotes=ayudaLotes;
    if (ayudaLotes)
    {
        if (prp_anoE==null)
            throw new NullPointerException("Debe llamar primero a setCamposLote");
        prp_anoE.setToolTipText("F3 para consulta lotes disponibles");
        prp_serieE.setToolTipText("F3 para consulta lotes disponibles");
        prp_partE.setToolTipText("F3 para consulta lotes disponibles");
        prp_indiE.setToolTipText("F3 para consulta lotes disponibles");
        if (emp_codiE!=null)
            emp_codiE.setToolTipText("F3 para consulta lotes disponibles");
    }
  }
   /**
   * Consulta Lotes Disponibles de Productos.
   */
  private void ayudaLote()
  {
    try
    {
      if (ayuLot == null)
      {
        ayuLot = new ayuLote(eu, vl, dt, pro_codiE.getValorInt())
        {
          @Override
            public void matar(boolean cerrarConexion)
           {
            ayuLot.setVisible(false);
            ej_consLote();
           }
        };

        vl.add(ayuLot);
        ayuLot.setIconifiable(false);
        ayuLot.setLocation(25, 25);
        ayuLot.iniciarVentana();
      }
      ayuLot.jt.removeAllDatos();
      ayuLot.setVisible(true);
      ayuLot.muerto = false;
      ayuLot.statusBar.setEnabled(true);
      ayuLot.statusBar.Bsalir.setEnabled(true);
      ayuLot.setVerBloqueados(getVerIndivBloqueados());

      intfr.setEnabled(false);
      intfr.setFoco(ayuLot);
      ayuLot.cargaGrid(pro_codiE.getText(),almCodi);
            SwingUtilities.invokeLater(new Thread()
      {
        @Override
        public void run()
        {
          ayuLot.jt.requestFocusInicio();
        }
      });

    }
    catch (Exception j)
    {
      this.setEnabled(true);
    }
  }
  public boolean getVerIndivBloqueados()
  {
      return verIndivBloqueados;
  }
  public void  setVerIndivBloqueados(boolean verIndivBloqueados )
  {
      this.verIndivBloqueados=verIndivBloqueados;
  }
  void ej_consLote()
  {
    if (ayuLot.consulta)
    {
      if (emp_codiE!=null)
        emp_codiE.setValorDec(ayuLot.jt.getValorInt(0));
      prp_anoE.setValorDec(ayuLot.jt.getValorInt(ayuLot.rowAct, 1));
      prp_serieE.setText(ayuLot.jt.getValString(ayuLot.rowAct, 2));
      prp_partE.setValorDec(ayuLot.jt.getValorInt(ayuLot.rowAct, 3));
      prp_indiE.setValorDec(ayuLot.jt.getValorInt(ayuLot.rowAct, 4));
      prp_pesoE.setText(ayuLot.jt.getValString(ayuLot.rowAct, 5));     
    }
    ayuLot.setVisible(false);
    intfr.setEnabled(true);
    intfr.toFront();
    try
    {
      intfr.setSelected(true);
    }
    catch (Exception k)
    {}
    intfr.setFoco(null);
    campoLote.requestFocus();
    afterSalirLote(ayuLot);
  }
  /**
   * Función para machacar si se quiere controlar algo despues de salir del lote
   * 
   */
  public void afterSalirLote(ayuLote ayuLot)
  {

  }
}

