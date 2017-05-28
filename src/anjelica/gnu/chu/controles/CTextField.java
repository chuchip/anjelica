
package gnu.chu.controles;

import gnu.chu.comm.BotonBascula;
import gnu.chu.interfaces.CEditable;
import gnu.chu.interfaces.CQuery;
import gnu.chu.utilidades.ClipEditable;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.SystemOut;
import gnu.chu.utilidades.mensajes;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
/**
 *
 * <p>T�tulo: CTextField </p>
 * <p>Descripción: Campo genérico de Entrada.<br>
 *  Soporta formateo de campos decimales, fechas y Strings</p>
 *  <p>Copyright: Copyright (c) 2005-2011
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
 * @version 2.1
 * @version 2.2 . Incluido enlace a JCalendarButton
 */

public class CTextField extends JTextField implements  CQuery,CEditable,TableCellRenderer
{
  Color colBackGround=null;
  private boolean incluirComodines=true;
  boolean aceptaComodines=true;
  boolean iniciado=false;
  private int rowGrid=0;
  private boolean editable=true;
  private boolean editadoParent=true;
  private boolean dependePadre=true;
  private boolean ajusActual=true;
  public final static int CHAR_LIMIT=1;
  public final static int CHAR_LETRA=2;
  public final static int CHAR_NUMERO=4;
  public final static int CHAR_TODO=8;
  int tieneCambio=-1;
  private BotonBascula bascula;
  private String copia="";  // Copia del TextField, puesto por la rutina resetCambio;
  int pulsado=0;
  CGridEditable gridEdit=null;
  private double copNum=0;
  final static char GROUPSEPARATOR=','; // Grupo separador para intr. formators (inglish)
  final static char DECIMALSEPARATOR='.'; // Grupo separador para intr. formators (inglish)
  final static char MINUSSIGN='-'; // Grupo separador para intr. formators (inglish)
  boolean ceroIsNull=true; // Cero es equivalente a NULL (isNull devuelve true)
  private boolean SalirConError = true; // Indica se se puede abandonar el Campo con un Error.
  private double LimInf=0d; // Limite Inferior.
  private double LimSup=0d; // Limite Superior.
  boolean swSimpleQuery=true; // Indica si el Query es modo simple (no hace falta
                              //   introducir % o *
  private boolean Capital=false;  // Dice si debe poner la primera letra en Mayusculas.
  private boolean AceptaNulo=true; // Si es false no deja salir si el campo esta vacio.
  private String conectaColumna=null; // Columna.
  private String strQuery="";
  private boolean sw_salir=false;
  private boolean CampoSel=true;
  private boolean sw_mt=false; // Indica si se ha modificado el TEXTO por setText.
  private boolean inSetText=false; // Indica si se esta en setTexo
  private GregorianCalendar gcalend =new GregorianCalendar();
  private String Texto1=""; // Variable temporal.
  private int SigloSig=70;
  private boolean Error=false;
  private int nc_ent=0;
  private int nc_dec=0;
  private boolean AutoNext=false;
  private boolean SonidoAutoNext=false;
  private String StrCarEsp=""; //Caracteres Especiales a Admitir
  private int AdmiteCar=15; // Dice que tipo caracteres admite en un CHAR.
  private String MsgError=null;
  private boolean EjecSonido=false; // Indica si se debe producir un sonido en el error
  private char May_min=' '; // Convierte las letras: 'M' -> Mayusculas, 'm' -> Minuscula, ' ' -> Nada
  //private boolean ev_keyb=false; // Indica si se esta procesando un evento de Keyboard
  private String noAceptaChar="'";
//  boolean NOT_FOCUS=true;
  private boolean control=true; // Indica si debe de realizar el control de las teclas.
  private boolean query=false; // Indica si estamos en Modo Query
  boolean ev_focus = false; // Indica si esta procesando un evento tipo FOCUSEVENT
  boolean noWrite=false;
  boolean trFocus=false;
  int Ltexto; // Longitud de Texto Actual.
  char tecla; // Tecla Pulsada.
  int posicion; // Posicion dentro del INPUT.
  private boolean activado=true;
  private boolean activadoParent=true;
  DocumentCTextField dctf;
  String Texto;
  private AbstractButton defaultButton;
//  private AbstractButton escapeButton;
//  private AbstractButton alternateButton;
  private char[] dia = new char[2];
  private char[] mes= new char[2];
  private char[] ano= new char[4]; // Dia, mes y ano de fecha.
  private char[] fecha = new char[10]; // Fecha Completa.
 // int TipoCampo=-1;
  boolean errorFoco=false;
  boolean swLostFocus=false;
  Component opFocus;
  private DecimalFormat form = new DecimalFormat();
  int tipoCampo=-1;
  String Formato=null;
  int MaxLong=0;
  int nc_DecMasc;
  int nc_EntMasc;
  boolean aceptamenos=true;
  boolean aceptapunto=true;
  char SepFecha='-';
  int nCarCampo=0;
  private ClipEditable menuContestual;

  public CTextField()
  {
    this("",Types.CHAR,"",0);
  }

  public CTextField(int td)
  {
    this("",td,"",0);
  }
  public CTextField(int td,String f)
  {
    this("",td,f,0);
  }

  public CTextField(String s)
  {
    this(s,Types.CHAR,"",0);
  }

  public CTextField(int td,String f,int ml)
  {
    this("",td,f,ml);
  }

 /*
  * Constructor Final.
  * @param: Numero de Columnas.
  * @param: Caracteristicas.
*/
  public CTextField(String s,int td,String f,int ml)
  {

    super(s);

    setDocument(new DocumentCTextField(this));
    form.applyPattern("##0.#####");
    form.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ENGLISH));
    enableEvents(AWTEvent.FOCUS_EVENT_MASK); // Activa la llamada al FOCUS
    enableEvents(AWTEvent.KEY_EVENT_MASK);
    setTipoCampo(td);
    if (f.length()>0)
      setFormato(f);
    editadoParent=true;
    setMaxLong(ml);
    iniciar();
    // Anade el Menu de Copiar y Pegar
    menuContestual = new ClipEditable(this);
    this.addMouseListener(menuContestual);
    // Configura un Font por defecto
    this.setFont(new Font("Dialog", 0, 11));
    this.setMargin(new Insets(0,0,0,0));

    this.setDisabledTextColor((Color) UIManager.get("ComboBox.disabledForeground"));
    
    this.addFocusListener(new FocusAdapter() {
      public void focusGained(FocusEvent e) {
        if (e.isTemporary())
          return;
       procesaFocusGained(e);
      }
    });

  }

  private Color unselectedForeground=null;
  private Color unselectedBackground=null;
  protected static javax.swing.border.Border noFocusBorder = new  javax.swing.border.EmptyBorder(1, 1, 1, 1);

  public Component getTableCellRendererComponent(JTable table, Object value,
                                                 boolean isSelected,
                                                 boolean hasFocus,
                                                 int row, int column)
  {
    if (isSelected)
    {
      super.setForeground(table.getSelectionForeground());
      super.setBackground(table.getSelectionBackground());
    }
    else
    {
      super.setForeground( (unselectedForeground != null) ?
                          unselectedForeground
                          : table.getForeground());
      super.setBackground( (unselectedBackground != null) ?
                          unselectedBackground
                          : table.getBackground());
    }

    setFont(table.getFont());

    if (hasFocus)
    {
      setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
      if (table.isCellEditable(row, column))
      {
        super.setForeground(UIManager.getColor("Table.focusCellForeground"));
        super.setBackground(UIManager.getColor("Table.focusCellBackground"));
      }
    }
    else
    {
      setBorder(noFocusBorder);
    }
//    System.out.println("row: "+row+" Col "+column+" Valor: "+value.toString());

    setText(value.toString());

    return this;
  }

  public void setError(boolean b)
  {
    errorFoco=b;
  }
  /**
   * El valor del campo no es valido segun su formato.
   *
   * @return true en caso de error.
   */
  public boolean getError()
  {
    return errorFoco;
  }

  private void iniciar()
  {
    iniciado=true;
    addMouseListener(new ClipEditable(this));
    this.setInputVerifier(new CTextFieldVerifier());

    // Anade Listener para las teclas pulsadas.
    this.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyPressed(KeyEvent e)
      {
        if (e.isControlDown() && e.getKeyChar()=='1')
        {
            pulsadaF1();
            return;
        }
        if (! e.isAltDown())
        {
          switch (e.getKeyCode())
          {
            case KeyEvent.VK_ENTER:
              if (e.isControlDown())
              {
                defaultButton = getDefaultButton();
                if (defaultButton != null)
                {
                  if (! doClick(e.getKeyCode()))
                    return;
                  if (! defaultButton.isEnabled())
                      return;
                  if (! puedoSalir())
                  {
                      mensajes.mensajeAviso("Valor de Campo, no valido\n "+getMsgError());
                      return;
                  }
                  defaultButton.requestFocus();
                  defaultButton.doClick();
                }
              }
              break;
            case KeyEvent.VK_F1:
              pulsadaF1();
              break;
            case KeyEvent.VK_ESCAPE:
            case KeyEvent.VK_F2:
            case KeyEvent.VK_F3:
            case KeyEvent.VK_F4:
            case KeyEvent.VK_F5:
            case KeyEvent.VK_F6:
            case KeyEvent.VK_F7:
            case KeyEvent.VK_F8:
            case KeyEvent.VK_F9:
            case KeyEvent.VK_F10:
              defaultButton = getButton(e.getKeyCode());
              if (defaultButton != null)
              {
                if (! doClick(e.getKeyCode()))
                  return;
                if (! defaultButton.isEnabled())
                      return;
                if (! puedoSalir())
                {
                      mensajes.mensajeAviso("Valor de Campo, no valido\n "+getMsgError());
                      return;
                }
                defaultButton.requestFocus();
               SwingUtilities.invokeLater(new Thread()
               {
                            @Override
                    public void run()
                    {
                        defaultButton.doClick();
                    }
                });
              }
              break;
          }
        }
      }


    });


/*    KeyboardFocusManager.getCurrentKeyboardFocusManager().addVetoableChangeListener(new VetoableChangeListener()
    {
      public void vetoableChange(PropertyChangeEvent evt)
          throws PropertyVetoException
      {
        boolean debug=false;
        if (evt.getNewValue()!=null && evt.getNewValue()==CTextField.this)
          debug=true;
        if (evt.getOldValue()!=null && evt.getOldValue()==CTextField.this)
          debug=true;
        if (debug)
        {
          System.out.println("en VetoableChange "+evt.getPropertyName()+
                             "\n id: "+evt.getPropagationId()+
                             "\n newValue: "+evt.getNewValue()+
                             "\n oldValue: "+evt.getOldValue());
          System.out.println("---------------------------");
        }

        if (evt.getPropertyName().equals("focusOwner"))
        {
          if (evt.getNewValue()!=null && opFocus!=null)
          {
            System.out.println("....");
            if (evt.getNewValue()==opFocus && CTextField.this.getText().equals("NO"))
            {
              System.out.println("----- QUE NO TE VAYAS ----");
              throw new PropertyVetoException("No entras al text3 por que yo lo digo",evt);
            }
          }

        }

      }
    });
    this.addFocusListener(new FocusAdapter()
    {
      public void focusGained(FocusEvent e) {
        swLostFocus=false;
        System.out.println("he ganado el foco"+CTextField.this.getText());
      }

      public void focusLost(FocusEvent e) {
        opFocus=e.getOppositeComponent();
        System.out.println("He perdido el foco"+CTextField.this.getText()+
                           "\n opFocus"+opFocus);
        swLostFocus=true;
      }

    });
    */
  }
  void  pulsadaF1()
  {
    if (getTipoCampo()==Types.DATE)
    {
      Date fecha=null;
      try
      {
          fecha = mensajes.getFechaCalendario(getDate());
      } catch (ParseException ex)
      {
          Logger.getLogger(CTextField.class.getName()).log(Level.SEVERE, null, ex);
          return;
      }
      if (fecha==null)
          return;
      setDate(fecha);
    }
    else
    {
      if (isEditable())
          leePesoBasc(0);
    }
  }
  public boolean leePesoBasc()
  {
    return leePesoBasc(getValorDec());
  }

  public boolean leePesoBasc(double valorDec)
  {
    if (valorDec == 0)
    {
      if (bascula != null)
      {
        double valor = bascula.getPesoBascula();
//          System.out.println("LeePesoBasc: "+valor);
        if (valor != 0)
        {
//          Formatear.Redondea(valor, nc_DecMasc); // Formatea al numero de decimales
          setValorDec(valor);
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public void setQuery(boolean b)
  {
    query=b;
  }
  /**
   * @deprecated usar isQuery()
   * @return boolean esta el campo en Modo query
   */
  public boolean getQuery()
  {
    return isQuery();
  }
  /**
   * @return boolean esta el campo en Modo query
   */
  public boolean isQuery()
  {
    return query;
  }

  public String getStrQuery()
  {
    return strQuery;
  }
  public void setStrQuery(String strQuery)
  {
    this.strQuery=strQuery;
  }

  public void setAutoNext(boolean s)
   {
           AutoNext=s;
   }
   public boolean getAutoNext()
    {
            return AutoNext;
    }
  public int getTipoCampo()
  {
    return tipoCampo;
  }
  public void setTipoCampo(int i)
  {
    tipoCampo=i;
    // Poner formatos por defecto.
    switch (tipoCampo)
    {
      case Types.DECIMAL:
        if (Formato==null)
          setFormato("---"+GROUPSEPARATOR+"---"+DECIMALSEPARATOR+"--"); // Formato por defecto para decimal
        setText("0",false);
        break;
      case Types.CHAR:
        if (Formato==null)
          setFormato("X"); // Formato por defecto para String.
        break;
      case Types.DATE:
        if (Formato==null)
          setFormato("dd-MM-yyyy");  // Formato por defecto para DATE
        setText("  -  -    ",false);
        for(int n=0;n<10;fecha[n++]=' ');
        break;
      default:
        if (Formato==null)
          setFormato("???");
    }
  }

 

  public void setFormato(String f)
  {
    setFormato(f,true);
  }


  void setFormato(String f,boolean v)
  {
    int n;
    Formato= f;


    switch (tipoCampo)
    {
      case Types.DECIMAL:
        setHorizontalAlignment(RIGHT);
        n=Formato.indexOf(DECIMALSEPARATOR);
        aceptapunto = n != -1;

        n=Formato.indexOf(MINUSSIGN);
        aceptamenos = n != -1;

        nc_DecMasc=0;
        nc_EntMasc=0;
        boolean sw_dec=false;
        for (n=0;n<Formato.length();n++)
        {
          if (Formato.charAt(n)==DECIMALSEPARATOR)
          {
          sw_dec=true;
          continue;
        }
        if (Formato.charAt(n)==GROUPSEPARATOR)
          continue;

        if (sw_dec)
          nc_DecMasc++;
        else
          nc_EntMasc++;
        }
        if (aceptamenos)
        {
          setMaxLong(Formato.length()-1);
          nc_EntMasc--;
        }
        else
          setMaxLong(Formato.length());
        if (nc_DecMasc>0)
          form.applyPattern("##0"+DECIMALSEPARATOR+Formatear.llenar('#',nc_DecMasc));
        else
          form.applyPattern("##0");
        setValorDec(getValorDec());
        break;
      case Types.DATE:
        setHorizontalAlignment(CENTER);
        setMaxLong(Formato.length() + 1);
        for (n = 0; n < Formato.length(); n++)
        {
          if (Formato.charAt(n) != 'd' && Formato.charAt(n) != 'M' && Formato.charAt(n) != 'y')
          {
            SepFecha = Formato.charAt(n);
            break;
          }
        }


        break;
      case Types.CHAR:
        break;
      default:
        setMaxLong(Formato.length());
    }

  }

  public String getFormato()
  {
    return Formato;
  }

  public void setMaxLong(int n)
  {
    nCarCampo=n;
    if (tipoCampo==Types.CHAR)
      MaxLong=n;
  }

  public int getMaxLong()
  {
    return MaxLong;
  }

  public AbstractButton getDefaultButton()
  {
    return getButton(KeyEvent.VK_ENTER);
  }
  public AbstractButton getEscapeButton()
  {
    return getButton(KeyEvent.VK_ESCAPE);
  }
  public AbstractButton getAlternateButton()
  {
    return getButton(KeyEvent.VK_F2);
  }

    public AbstractButton getButton(int tecla)
    {
      Component yo = this;
      AbstractButton boton=null;
      if (gridEdit != null)
      {
        yo = gridEdit;
        boton=gridEdit.getButton(tecla);
        if (boton!=null)
          return boton;
      }
      return estatic.getButton(tecla,this);
    }


    @Override
  public void setText(String text)
  {
    setText(text,true);
  }

  public void setText(String Text,boolean format)
  {
    if (Text==null)
      Text="";

    int n;
    if (!query && control && format)
    {
      Error=false;
      switch (tipoCampo)
      {
        case Types.DECIMAL:
          // Quitarles las Comas y espacios.
//          System.out.println("setText: "+Text);
          String x="";
          for (n=0;n<Text.length();n++)
          {
            if (Text.charAt(n)!=Formatear.GROUPSEPARATOR && Text.charAt(n)!=' ')
              x=x+Text.charAt(n);
          }
          Text=x;

          // Quita los los Decimales si los hubiera
          if (aceptapunto == false)
          {
            int p;
            if ((p=Text.indexOf(Formatear.DECIMALSEPARATOR))>0)
              Text=Text.substring(0,p);
          }

          if (Text.length() > MaxLong && MaxLong > 0)
          {
            MsgError="Excedida la Longitud Maxima";
            Error=true;
            return;
          }

          if (!	sal_DEC1(Text))
          {
            Error=true;
            return ;
          }
          if (!checkValDec(Text))
          {
            Error=true;
            return ;
          }
          if (! this.isFocusOwner()) // No tiene focus. presentar el Texto como debe.
            Text=Formatear.FormatChar(Text,Formato,ceroIsNull);
          break;
        case Types.CHAR:
          if (Text.length() > MaxLong && MaxLong > 0)
            Text = Text.substring(0,MaxLong);
          String Textot="";
          for (n=0;n<Text.length();n++)
          {
            tecla=Text.charAt(n);
            if (! AceptaChar(n,tecla))
            {
              Error=true;
              return ;
            }
            tecla = controlCHAR(n,tecla);
            Textot=Textot+tecla;
          }
          Text=Textot;
          break;
        case Types.DATE:
          Text=Text.trim();
          // Compruebo no me haya mandado una cadena vacia
          if (Formatear.esNulo(Text,""+SepFecha))
                  Text="";

          if (Ltexto > MaxLong && MaxLong > 0 )
          {
            Error=true;
            return ;
          }
          if (Text.length()==0)
          {
            if (! AceptaNulo)
              Error=true;
            break;
          }
          setdate(Text);
          // Primero comprueba si la fecha es Nula.
          Text="";
          for(n=0;n<Formato.length();Text=Text+fecha[n++])

          if (! setdate1())
          {
            Error=true;
            return;
          }
          Text=Texto1;
      }
    }

    this.Texto=Text;

    if (!this.isFocusOwner())
      strQuery="";

    sw_mt=true;
    inSetText=true;

    if (!super.getText().equals(Text))
    {
//      if (gridEdit==null || this.isEnabled()==true )
        super.setText(Text);
//      if (! this.isFocusOwner())
//        setCaretPosition(Text.length());
//        setCaretPosition(0);
//      else
//        selectAll();
    }
    if (! this.isFocusOwner())
      setCaretPosition(Text.length());

    inSetText=false;

    if (gridEdit!=null)
    {
      if (! isEnabled() && gridEdit.isEnabled())
      {
        int nCol=gridEdit.getSelectedColumn();
        for (int n1=0;n1<gridEdit.campos.size();n1++)
        {
          if (gridEdit.campos.get(n1)==this)
            nCol=n1;
        }
        gridEdit.setValor(super.getText(), gridEdit.getRowEditada(),
                          nCol);
      }
    }
    if (!this.isFocusOwner() && query)
      salir1();
    return;
  }

  public int getValorInt()
  {
    return (int)((double) getValorDec());
  }
  public long getValorLong()
  {
    return (long) getValorDec();
  }

  public double getValorDec()
  {
      try {
          String txt = super.getText().trim();
          txt = cambiaSimbolRev(txt);
          return Double.parseDouble(txt);
      } catch (NumberFormatException n) {
          return 0d;
      }
  }

  public void setValorInt(int valor)
  {
    setValorDec(valor);
  }

  public void setValorDec(double v)
  {
    String txt;
    try
    {
      if (v==0)
        txt="0";
      else
      {
        txt = form.format(Formatear.Redondea(v, nc_DecMasc));
      }
     } catch (Exception k)
     {
        SystemOut.print(k);
       return;
     }
     setText(txt,true);
  }

  
  public void setBackground(Color colorBack)
  {
      colBackGround=colorBack;
      super.setBackground(colBackGround);
  }

    @Override
  public void setEnabled(boolean enab)
 {
   if (enab)
      super.setCursor(new Cursor(Cursor.TEXT_CURSOR));
   else if (!getCursor().equals(new Cursor(Cursor.WAIT_CURSOR)))
       super.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
   activado=enab;
   if (activado)
   {
     if (activadoParent)
     {
       super.setEnabled(true);
       if (gridEdit!=null)
           gridEdit.setCellEditable(true,getRowGrid());
       if (isFocusOwner())
          selectAll();
     }
     return;
   }
   super.setEnabled(false);
   if (gridEdit!=null)
        gridEdit.setCellEditable(true,getRowGrid());
 }

    @Override
 public void setCursor(Cursor c) {
        if (c.equals(new Cursor(Cursor.WAIT_CURSOR)))
           super.setCursor(c);
        else if (isEnabled())
             super.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        else
             super.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
 }

  @Override
  public void setEnabledParent(boolean enab)
  {
    if (! dependePadre)
      return;
    activadoParent=enab;
    if (! enab)
    {
      if (super.isEnabled())
      {
        super.setEnabled(false);
      }
    }
    else
    {
      if (! super.isEnabled() && activado) {
        super.setEnabled(true);
        if (isFocusOwner())
          selectAll();
      }
    }
  }
  
  public void setEditable(boolean edit)
  {
    editable = edit;
    if (editable)
    {
     
      if (editadoParent || ! iniciado)
      {
        super.setEditable(true);
        if (colBackGround!=null && gridEdit!=null && isEnabled())
            super.setBackground(colBackGround); 
      }     
      return;
    }
    if (gridEdit!=null && isEnabled())
        super.setBackground(Color.LIGHT_GRAY);
    super.setEditable(false);      
  }
    @Override
  public void setEditableParent(boolean edit)
  {
    if (!dependePadre)
      return;
    editadoParent = edit;
    if (!edit)
    {
      if (super.isEditable())
      {
         if (gridEdit!=null && isEnabled())
            super.setBackground(Color.LIGHT_GRAY);
        super.setEditable(false);
      }
    }
    else
    {
      if (!super.isEditable() && this.editable)
      {
        if (colBackGround!=null && gridEdit!=null && isEnabled())
            super.setBackground(colBackGround); 
        super.setEditable(true);
      }
    }
}

  public Component getErrorConf()
  {
    if (errorFoco)
      return this;
    else
      return null;
  }
  public void resetTexto()
  {
    setText("");
  }
  public String getTextSuper()
  {
    return super.getText();
  }
  public String getTexto()
  {
    if (getTipoCampo()==Types.DECIMAL && getTextSuper().trim().length()==0)
      setText("0");
    salir1();
    return Texto;
  }
  /**
   * @deprecated
   * @use setCambio(boolean hasCambio)
   * Rutina que fuerza a que la proxima llamada a hasCambio devuelva lo que
   * se mande aqui como parametro
   * @param hasCambio boolean valor a devolver por la proxima llamada a hasCambio
   */
  public void setHasCambio(boolean hasCambio)
  {
    tieneCambio=hasCambio?1:0;
  }
  
   public void setCambio(boolean hasCambio)
  {
    tieneCambio=hasCambio?1:0;
  }
   /**
    * Pone el valor antiguo de nuevo (lo coge de getCopia)
    */
  public void resetValor()
  {
      if (tipoCampo==Types.DECIMAL)
          setValorDec(getValorDecAnt());
      else
        setText(getCopia());
  }
  /**
* Rutina que comprueba si el campo ha cambiado desde la ultima ejecucinn de
* resetCambio.
* @return true -> Ha cambiado.
*         false-> NO ha cambiado.
* <p>
*
*
*/
public boolean hasCambio()
{
  if (tieneCambio==0)
  {
    tieneCambio=-1;
    return false;
  }
  if (tieneCambio==1)
  {
    tieneCambio=-1;
    return true;
  }
  if (tipoCampo==Types.DECIMAL)
  {
      return copNum != getValorDec();
  }

  return copia.trim().compareTo(super.getText().trim())!=0;
}
public String getTextAnt()
{
  return copia;
}
public double getValorDecAnt()
{
  return copNum;
}
/*  public void requestFocus() {
       super.requestFocus();
       selectAll();
}
/**
* Iguala la variable 'copia' a lo que haya actualmente en el VTextField
* <p>
* Si llamamos inmediatamente a la funcion hasCambio, esta devolveria true.
*
*/
    @Override
public void resetCambio()
{
  if (tipoCampo==Types.DECIMAL)
    copNum=getValorDec();
  copia=super.getText();
}
/**
* Retorna el valor Anterior
* @deprecated usar getCopia
*/
public String getValorOld() { return copia; };
/**
 * Retorna el valor Actual
 */
public String getValorAct() {
       return getText();
};
public String getCopia(){
  return copia;
}

/**
 * Devuelve el valor de la copia como un double.
 * Si hay un error al pasarlo a double, devuelve 0.
 * @return  Valor en formato 'double' del valor antiguo
 */
public double getCopiaDouble(){
   try {
    String num=copia.trim();
    num = cambiaSimbolRev(num);
     return Double.parseDouble(num);
   } catch (NumberFormatException n) {
          return 0d;
  }
}
/**
 * Devuelve el valor de la copia como un entero.
 * Si hay un error al pasarlo a entero, devuelve 0.
 * @return  Valor en formato 'entero' del valor antiguo
 */
 public int getCopiaInt(){
      return (int)((double) getCopiaDouble());

}
String procesaTecla()
{
  if (Character.isISOControl(tecla) || isFocusOwner() == false || ev_focus == true)
    return "" + tecla;

  if (!control || query)
  {
    if (tipoCampo == Types.CHAR)
    {
      char tec = controlCHAR(tecla);
      if (tec != tecla)
        return "" + tec;
    }
    return "" + tecla;
  }

  // Comprobar que no sea ninguno de los Caracteres a NO admitir.
  for (int n = 0; n < noAceptaChar.length(); n++)
  {
    if (tecla == noAceptaChar.charAt(n))
    {
      noWrite = true;
      sonido();
      return "";
    }
  }

//  ev_keyb = true;
  String a = pulsatecla();
//  ev_keyb = false;
  return a;
}
  private char controlCHAR(char tec)
  {
    switch (May_min)
    {
      case 'm':
        tec=Character.toLowerCase(tec);
        break;
      case 'M':
        tec=Character.toUpperCase(tec);
        break;
    }
    return tec;
  }
  /**
   * @todo nque cono hace esta funcion?
   *
   */
/*synchronized void processTextEvent()
{

    if (inSetText)
      return;
    if (NOT_FOCUS || !control || query )
           return;

    if ( sw_salir)
    {
      sw_salir=false;
           return;
    }
    if (! ev_keyb && ! ev_focus && !ev_text)
    {
          ev_text=true;
      compcambio();
      ev_text=false;
    }
}
*/
  public void procesaSalir()
  {
    puedoSalir();
  }
  private boolean salir1()
  {
    Texto=super.getText();
    Ltexto=Texto.length();

    if (Ltexto > MaxLong && MaxLong > 0 && ! query)
    {
      MsgError="Longitud Maxima ("+MaxLong+") Superada. Longitud: "+Ltexto;
      Error=true;
      return false;
    }

    switch (tipoCampo)
    {
      case Types.CHAR:
        return (sal_CHAR());
      case Types.DECIMAL:
        if (sal_DECIMAL())
        {
          if (!checkValDec(Texto))
            return false;
          return true;
        }
        else
          return false;
        case Types.DATE:
          return (sal_DATE());
        default:
          return true;
    }

  }

  private boolean checkValDec(String Text)
  {
    if (Text==null || Text.length() == 0)
      return true;

    Text=Text.trim();
    Text=cambiaSimbolRev(Text);
    if (query)
      return true;
    try {
       Double.parseDouble(Text);
    } catch (NumberFormatException k)
    {
        MsgError="checkValDec: Error al transformar Cadena a Numero";
        sonido();
        return false;
    }
    return true;
  }

  private String creaQueryDec(String text) throws ParseException
  {
    text=text.trim();
    String strQuer1="";
    int p=0;

    switch (text.charAt(0))
    {
      case '=':
       if (! aceptaComodines)
          throw new ParseException("No se aceptan comodines",0);
        if (text.length()<2)
          throw new ParseException("Introduzca Valor Numerico",0);
        strQuer1=" = ";
        p=1;
        break;
      case '>':
        if (! aceptaComodines)
          throw new ParseException("No se aceptan comodines",0);

        if (text.length()<2)
          throw new ParseException("Introduzca Valor Numerico",0);

        if (text.charAt(1)=='=')
        {
          if (text.length()<3)
            throw new ParseException("Introduzca Valor Numerico",0);
          strQuer1=" >"+ text.charAt(1)+" ";
          p=2;
        }
        else
        {
          strQuer1=" > ";
          p=1;
        }

        break;
      case '<':
        if (! aceptaComodines)
          throw new ParseException("No se aceptan comodines",0);

        if (text.length()<2)
          throw new ParseException("Introduzca Valor Numerico",0);

        if (text.charAt(1)=='=' || text.charAt(1) == '>')
        {
          if (text.length()<3)
            throw new ParseException("Introduzca Valor Numerico",0);
          strQuer1=" <"+ text.charAt(1)+" ";
          p=2;
        }
        else
        {
          strQuer1=" < ";
          p=1;
        }
        break;
    }

    if (sal_DEC1(text.substring(p).trim())==false)
      throw new ParseException("Numero: "+text.substring(p)+" no Valido",0);
    if (p==0)
      strQuer1= " = ";
   
    strQuer1= strQuer1+Texto1;
    return strQuer1;
  }
  private boolean sal_DEC1(String txt)
  {
    Texto1=txt.trim();
    int lTxt=Texto1.length();
    char letra;
    int n;

    // Comprueba que los caracteres del Decimal son validos.
    txt="";
    for (n=0;n<= lTxt-1;n++)
    {
      tecla = Texto1.charAt(n);
      posicion=n;
      if (! carEsValidDec(tecla,posicion,txt)) // Es Un caracter Valido?.
      {
        Error=true;
        return false; // Error grave
      }
      txt=txt + tecla;
    }

    Texto1="";
    // Quitar ',' el GROUPSEPARATOR del Locate en cuestion
    for (n=0;n< lTxt;n++)
    {
      letra=txt.charAt(n);
      if (letra == Formatear.GROUPSEPARATOR)
        continue;
      Texto1=Texto1+letra;
    }
    return true;
  }

  String getConectaColumna()
  {
    return conectaColumna;
  }
  /**
   * Establece si se deben poner % en los campos Automaticamente
   * cuando se esta en modo query.
   * Es decir si el usuario introduce 'pepe' el texto sera cambiado
   * a '%pepe%'
   * Por defecto es true.
   * @param simpleQuery boolean Establecer modo Simple Query.
   */
  public void setSimpleQuery(boolean simpleQuery)
  {
    swSimpleQuery=simpleQuery;
  }
  public boolean getSimpleQuery()
  {
    return swSimpleQuery;
  }
  private boolean sal_CHAR()
  {
    int n;
    char plet;

    if (Ltexto==0)
    {
      if (query || AceptaNulo)
        return true;
      else
      {
        // No aceptar salir del campo si es Nulo.
        MsgError="Campo NO admite NULOS";
        return false;
      }
    }

    if (query)
    {
      if (swSimpleQuery && getTipoCampo()==Types.CHAR && isIncluirComodines())
        Texto="%"+Texto+"%";
      // Estamos en Modo Query. Preparamos la condicion.
      // Buscamos caracteres | y & por entender que son OR y AND respectivamente.
      strQuery=getConectaColumna();
      int p=0;
      int pOr,pAnd;
      while (p>=0)
      {
        pOr=Texto.indexOf('|',p);
        pAnd=Texto.indexOf('&',p);

        if (pOr >= 0 || pAnd >= 0)
        {
          // Encontrada condicion OR o AND Tratamos Una Parte.
          if ( (pOr >= 0 && pAnd >= 0 && pAnd < pOr) || (pOr < 1) )
          {
            strQuery=strQuery+creaQueryChar(Texto.substring(p,pAnd))+" AND "+getConectaColumna();
            p=pAnd+1;
            continue;
          }
          else
          {
            // Encontrada condicion OR. Tratamos Una Parte.
            strQuery=strQuery+creaQueryChar(Texto.substring(p,pOr))+" OR "+getConectaColumna();
            p=pOr+1;
            continue;
          }
        }

        strQuery=strQuery+creaQueryChar(Texto.substring(p));
        p=-1;
      }
      //strQuery=getConectaColumna()+strQuery;
      return true;
    }

    if (Capital )
    {
      // Poner la primera letra en Mayusculas.
      plet=Texto.charAt(0);
      plet=Character.toUpperCase(plet);
      Texto=plet+Texto.substring(1);
      setText(Texto,false);
    }


    for (n=0;n<= Texto.length()-1;n++)
    {
      if (! AceptaChar(n,Texto.charAt(n)))
        return false;
    }

    return true;
  }
  /**********************************************************************
   * Crea query para tipo CHAR.
   * Recibe: Texto a tratar.
   * Devuelve: Texto Tratado.
   **********************************************************************/
  private String creaQueryChar(String text)
  {
    boolean swTrata=true;
    String strQuer1="";
    boolean equal=true;
    int p=0;

    switch (text.charAt(0))
    {
      case '=':
        if (text.equals("=="))
        {
          strQuer1=" IS NULL";
          swTrata=false;
          break;
        }
        if (text.length()<2)
        {
          strQuer1=" = '='";
          return strQuer1;
        }
        strQuer1=" = '";
        p=1;
        break;
      case '>':
        if (text.length()<2)
        {
          strQuer1=" = '>'";
          return strQuer1;
        }
        if (text.charAt(1)=='=' )
        {
          if (text.length()<3)
          {
            strQuer1=" = '>='";
            return strQuer1;
          }
          strQuer1=" >"+ text.charAt(1)+" ' ";
          p=2;
        }
        else
        {
          strQuer1=" > '";
          p=1;
        }
        break;
      case '<':
        if (text.equals("<>"))
        {
          strQuer1=" IS NOT NULL";
          swTrata=false;
          break;
        }
        if (text.length()<2)
        {
          strQuer1=" = '>'";
          return strQuer1;
        }

        if (text.charAt(1)=='='  || text.charAt(1) == '>')
        {
          strQuer1=" <"+ text.charAt(1)+" '";
          p=2;
        }
        else
        {
          strQuer1=" < '";
          p=1;
        }
        break;
      default:
        equal=false;
    }
    if (swTrata)
    {
      if (equal == false)
      {
        // Buscar Comodines - ? % *
        if (text.indexOf('*')>=0 || text.indexOf('?')>=0 )
          strQuer1=" LIKE '"+text+"'";
        else if (text.indexOf('%')>=0 )
          strQuer1=" like '"+text+"'";
        else
          strQuer1=" = '"+text+"'";
      }
      else
        strQuer1= strQuer1+text.substring(p)+"'";
    }
    return strQuer1;
  }

  /**********************************************************************
   * Comprobar la salida de un Campo tipo Date.
   * Recibe: Nada.
   * Devuelve: true si puede salir.
   *           False si ha habido algun error al  salir.
   **********************************************************************/
  private boolean sal_DATE()
  {
    int n;

    if (query)
    {
      if (Texto.compareTo("")==0)
        return true;
      // Modo query.
      // Buscamos caracteres | y & ententiendo que son OR y AND respectivamente.
      strQuery=getConectaColumna();
      int p=0;
      int pOr,pAnd;
      while (p>=0)
      {
        pOr=Texto.indexOf('|',p);
        pAnd=Texto.indexOf('&',p);

        if ((pOr > 0 || pAnd > 0) && (pOr != 0 && pAnd != 0))
        {

          if ( (pOr > 0 && pAnd > 0 && pAnd < pOr) || (pOr < 1) )
          {
            try {
              strQuery=strQuery+creaQueryDate(Texto.substring(p,pAnd))+" AND "+getConectaColumna();
              } catch (ParseException k)
              {
                MsgError = k.getMessage();
                Error=true;
                return false;
              }
              p=pAnd+1;
              continue;
          }
          else
          {
            // Encontrada condicion OR. Tratamos Una Parte.
            try {
              strQuery=strQuery+creaQueryDate(Texto.substring(p,pOr))+" OR "+getConectaColumna();
              } catch (ParseException k)
              {
                MsgError = k.getMessage();
                Error=true;
                return false;
              }
              p=pOr+1;
              continue;
          }
        }
        try {
          strQuery=strQuery+creaQueryDate(Texto.substring(p));
        }
        catch (ParseException k)
        {
          MsgError = k.getMessage();
          Error=true;
          return false;
        }
        p=-1;
        return true;
      }
    }


    setdate(Texto);
    // Primero comprueba si la fecha es Nula.
    Texto=super.getText();
    if (Texto.compareTo("")==0)
    {
      if (AceptaNulo)
        return true;
      else
      {
        MsgError="CAMPO (Tipo Fecha) NO ACEPTA NULOS";
        Error=true;
        return true;
      }
    }

    for(n=0;n<10;Texto+=fecha[n++]);
    boolean j=setdate1();
    if (! j)
      return false;
    setText(Texto1,false);
    return true;
  }

  /**********************************************************************
   * Crea query para tipo Date
   * Recibe: Texto a tratar.
   * Devuelve: Texto Tratado. Lanza una ParseException en caso de Error.
   **********************************************************************/
  private String creaQueryDate(String text) throws ParseException
  {
    boolean swTrata=true;
    text=text.trim();
    String strQuer1="";
    int p=0;

    switch (text.charAt(0))
    {
      case '=':
        if (text.equals("=="))
        {
          swTrata=false;
          strQuer1=" IS NULL";
          break;
        }
        strQuer1=" = '";
        p=1;
        break;
      case '>':
        if (text.charAt(1)=='=')
        {
          strQuer1=" >"+ text.charAt(1)+" ";
          p=2;
        }
        else
        {
          strQuer1=" > ";
          p=1;
        }

        break;
      case '<':
        if (text.equals("<>"))
        {
          swTrata=false;
          strQuer1=" IS NOT NULL";
          break;
        }
        if (text.charAt(1)=='=' || text.charAt(1) == '>')
        {
          strQuer1=" <"+ text.charAt(1)+" ";
          p=2;
        }
        else
        {
          strQuer1=" < ";
          p=1;
        }
        break;
    }

    String fec=text.substring(p).trim();
    if (swTrata)
    {
      if (fec.length() < Formato.length())
      { // n No me han introducido separadores ?
        text = "";
        int n1 = 0;
        for (int n = 0; n < Formato.length(); n++)
        {
          if (Formato.charAt(n) == SepFecha)
            text += SepFecha;
          else
          {
            if (n1 < fec.length())
              text += fec.charAt(n1++);
          }
        }
        fec = text;
//        System.out.println("Fecha Query: "+fec);
      }
      setdate(fec);

      if (setdate1() == false)
        throw new ParseException("Fecha: " + text.substring(p) + " no Valida", 0);

      if (p == 0)
        strQuer1 = " = ";
      strQuer1 = strQuer1 + "to_Date( '" + Texto1 + "', '" + Formato + "')";
    }
    return strQuer1;
  }

  /*************************************************************************
   * Salida del campo cuando es decimal -
   * Formatea la salida y comprueba si esta en unos rangos validos.
   *************************************************************************/
  private boolean sal_DEC2(String Text1)
  {
    Texto=Formatear.FormatChar(Text1,Formato);
    setText(Texto,false);

    // Comprueba si los valores son validos.
    if (LimInf == LimSup)
      return true; // No realizar Comprobacion.

    if (getValorDec() < LimInf || getValorDec() > LimSup)
    {
      MsgError="Valor fuera de Limites. VALIDO DE: "+LimInf+" A: "+LimSup;
      return false; // No es un Error grave.
    }

    return true; // Bien.
  }

  /*************************************************************************
   * Comprueba que puede salir de un Decimal
   ***************************************************************************/
  private boolean sal_DECIMAL()
  {
    Ltexto=Texto.trim().length();
    if (Ltexto==0)
    {
      if (AceptaNulo || query )
      {
        Error=false;
        return true;
      }
      else
      {
        if (!query)
        {
          Texto=Formatear.FormatChar("0", Formato);
          setText(Texto);
        }
        MsgError="Campo (Tipo decimal) NO Acepta Nulos";
        return true; // No es un error grave.
      }
    }

    if (!query)
    {
      if (! sal_DEC1(Texto))
        return false;
      return sal_DEC2(Texto1);
    }

    // Modo query. Preparar la strQuery.
    strQuery=getConectaColumna();
    int p=0;
    int pOr,pAnd;
    while (p>=0)
    {
      pOr=Texto.indexOf('|',p);
      pAnd=Texto.indexOf('&',p);

      if ((pOr > 0 || pAnd > 0) && (pOr != 0 && pAnd != 0))
      {
        // Encontrada condicion OR o AND Tratamos Una Parte.
        if ( (pOr > 0 && pAnd > 0 && pAnd < pOr) || (pOr < 1) )
        {
          try {
            strQuery=strQuery+creaQueryDec(Texto.substring(p,pAnd))+" AND "+getConectaColumna();
            } catch (ParseException k)
            {
              MsgError = k.getMessage();
              Error=true;
              return false;
            }
            p=pAnd+1;
            continue;
        }
        else
        {
          // Encontrada condicion OR. Tratamos Una Parte.
          try {
            strQuery=strQuery+creaQueryDec(Texto.substring(p,pOr))+" OR "+getConectaColumna();
            } catch (ParseException k)
            {
              MsgError = k.getMessage();
              Error=true;
              return false;
            }
            p=pOr+1;
            continue;
        }
      }
      try {
         String cadQuery=creaQueryDec(Texto.substring(p));
          try {
          if (ceroIsNull) {
              if (cadQuery.trim().equals("= 0")) {
                      strQuery="";
                      return true;
              }
          }
      } catch (Exception k) {
      }
        strQuery=strQuery+cadQuery;
      }
      catch (ParseException k)
      {
        MsgError = k.getMessage();
        Error=true;
        return false;
      }
      p=-1;
    }
    return true;
  }

 /*
  * Rutina principal para salir del campo.
*/
  boolean puedoSalir()
  {
    errorFoco=false;
    Error=false; // Sin errores.
    if (! control)
      return true;

    if (! super.getText().equals(""))
      posicion=getCaretPosition();
    else
      posicion=0;

    MsgError="";
    strQuery="";

    sw_salir=true;
    sw_mt = false;
/*    if (TipoCampo==Types.DECIMAL && query==false)
    {
      if (getValorDec() == 0)
      {
        if (EU != null)
        {
          double valor = EU.getPesoBascula();
          if (valor != 0)
            setValorDec(valor);
        }
      }
    }
 */
    boolean rt= salir1();

    sw_salir= ! sw_mt;

    if ( ! rt )
    {
      if (! Error)
      {
        // NO ES UN ERROR DE FORMATO
        Error=true;
        if (! SalirConError)
        {
          errorFoco=true;
          return false;
        }
        else
          sonido();
      }
      else
      {
        errorFoco=true;
        sonido();
        return false;
      }
    }
    if (super.getText().length()>=0)
      this.setCaretPosition(super.getText().length());
    return true;
  }

  public void setSalirConError(boolean v)
  {
    SalirConError=v;
  }
  public boolean getSalirConError()
  {
    return SalirConError;
  }
  public void setSonidoAutoNext(boolean v)
  {
    SonidoAutoNext=v;
  }
  public boolean getSonidoAutoNext()
  {
    return SonidoAutoNext;
  }

    @Override
  protected void processKeyEvent(KeyEvent ke)
  {
    if (ke.getID() == KeyEvent.KEY_PRESSED)
    {
      pulsado=ke.getKeyCode();
      switch (ke.getKeyCode())
      {
        case KeyEvent.VK_TAB:
          procesaTab(ke);
          super.processKeyEvent(ke);
          break;
        case KeyEvent.VK_ENTER:
          leePesoBasc();
          procesaEnter(ke);
          super.processKeyEvent(ke);
          break;
        default:
          super.processKeyEvent(ke);
      }
    }
    else
    {
      super.processKeyEvent(ke);
    }
  }

  protected void procesaTab(KeyEvent ke)
  {
    if (gridEdit!=null)
      gridEdit.procesaTab(ke);
    afterProcesaTab();
  }

  protected void procesaEnter(KeyEvent ke)
  {
    afterProcesaEnter();
    if (gridEdit != null)
      gridEdit.procesaEnter(ke);
  }
  /**
   * Rutina perezosa para poder hacer algo cuando  se le da al Enter
   */
  public void afterProcesaEnter()
  {
      
  }
   /**
   * Rutina perezosa para poder hacer algo cuando  se le da al Tabulador
   */
  public void afterProcesaTab()
  {
      
  }
  public void setGridEditable(CGridEditable gridEdit)
  {
    this.gridEdit=gridEdit;
  }

  public CGridEditable getGridEditable()
  {
    return gridEdit;
  }
  public int getRowGrid()
  {
      return rowGrid;
  }
   public void setRowGrid(int rowGrid)
  {
      this.rowGrid=rowGrid;
  }
  /**
   *  @todo a currartela chiquitin ...
   */
  void procesaFocusGained(FocusEvent e)
  {
    boolean p=false;
    if (! this.isEditable() || e.isTemporary())
      return;
    ev_focus=true;
//    Thread.dumpStack();
//    System.out.println("CTextField procesando Foco");
      if (super.getText()==null )
        Texto="";
      else
      { // Comprueba que el texto existente sea Valido
        String Text1=super.getText();
        if (Text1.trim().length()==0 && tipoCampo==Types.DECIMAL && AceptaNulo)
        {
          super.setText("");
          Texto="";
        }
        else
        {
          Texto="";
//          Text1=cambiaSimbol(Text1);
          setText(Text1);
          if (Texto.length()==0)
            super.setText("");
        }
      }

      if (tipoCampo==Types.DECIMAL && Texto.length()!=0 && control)
      {
        if (query)
          setText(Texto.trim(),false);
        else
        {
//              if (! AceptaNulo)
          setValorDec(getValorDec());
        }
      }
//      System.out.println("Ctextfield. length: "+super.getText().length()+" getCaretPosition: "+getCaretPosition());
      if (Texto.length()!=0 && getCaretPosition()>=super.getText().length())
        p=true;

      if (p && CampoSel)
        selectAll();


    ev_focus=false;
    return;
  }
/**
 * Cambia caracteres
 * @param grupo Grupo de caracteres a cambiar
 *   Cambiara el elemento n por el elemento n+1 por lo cual debe ser siempre
 *   par el numero de elmentos.
 * @str Cadena donde cambiar los caracteres
 *
 * @return cadena cambiada
 *
 */

  String cambiaCar(char[] grupo,String str) throws IllegalArgumentException
  {
    if (grupo.length <2 || grupo.length%2!=0)
      throw new IllegalArgumentException("Numero de Caracteres Erroneos (<2)");
    String s="";
    for (int n=0;n<str.length();n++)
    {
      char c=str.charAt(n);
      for (int n1=0;n1<grupo.length;n1+=2)
      {
        if (c==grupo[n1])
        {
          c=grupo[n1+1];
          break;
        }
      }
      if (c!='\0')
        s+=c;
    }
    return s;
  }


  String cambiaSimbolRev(String str)
  {

    return cambiaCar(new char[]{Formatear.GROUPSEPARATOR,'\0',
                     Formatear.DECIMALSEPARATOR,DECIMALSEPARATOR,Formatear.MINUSSIGN,MINUSSIGN},str);
  }

  String cambiaSimbol(String str)
  {
    return cambiaCar(new char[]{GROUPSEPARATOR,Formatear.GROUPSEPARATOR,
                 DECIMALSEPARATOR,Formatear.DECIMALSEPARATOR,MINUSSIGN,Formatear.MINUSSIGN},str);
  }

  private boolean formatchar()
  {
    tecla=controlCHAR(posicion,tecla);

    return AceptaChar(posicion,tecla);
  }
  public void setMayusculas(boolean swMayusc) {
     if (swMayusc)
      May_min = 'M';
    else {
      if (May_min == 'M')
        May_min = ' ';
    }
  }
     
  public void setMayusc(boolean v) {
      setMayusculas(v);
  }

  public void setMinusc(boolean v) {
    if (v)
      May_min = 'm';
    else {
      if (May_min == 'm')
        May_min = ' ';
    }
  }

  public boolean getMayusc() {
    return (May_min == 'M');
  }

  public boolean getMinusc() {
    return (May_min == 'm');
  }

  /*************************************************************************
   * Devuelve si un caracter x en una posicion Y es valido, segun el formato para
   * un campo TipoCHAR
   *******************************************************************************/
  private boolean AceptaChar(int pos,char letra)
  {
    char ft;

    if (pos < Formato.length())
      ft=Formato.charAt(pos);
    else
      ft='?';

    switch (ft)
    {
      case 'm': // Admite Letras - Pasarlas a Minusculas
      case 'M': // Admite Letras - Pasarlas a Mayusculas.
      case 'A': // Solo Admite Letras.(Incluye los ESPACIOS)
        if (Character.isLetter(letra)== false && Character.isWhitespace(letra)==false)
        {
          MsgError="Caracter: "+letra+" - Solo se Admiten LETRAS";
          return false;
        }
        break;
      case '%': // Admite Numeros  menos y punto decimal.
        if (Character.isDigit(letra)==false && tecla != '-' && tecla!=Formatear.DECIMALSEPARATOR)
        {
          MsgError="Caracter: "+letra+" - Solo se Admiten NUMEROS, '-' y '.' ";
          return false;
        }

      case '9': // Solo Admite Numeros.
        if (Character.isDigit(letra)==false )
        {
          MsgError="Caracter: "+letra+" - Solo se Admiten NUMEROS - ";
          return false;
        }
      case '?': // Admite lo que se diga genericamente.
        if (May_min=='M')
          letra=Character.toUpperCase(letra);
        if (May_min=='m')
          letra=Character.toLowerCase(letra);

        if ((AdmiteCar & CHAR_TODO) == 8 ) // Admite Todo.
          break;
        if ((AdmiteCar & CHAR_LIMIT) == 1 && isCarEspe(letra))  // Admite Caract. Especiales
          break;
        if ((AdmiteCar & CHAR_LETRA) == 2 && (Character.isLetter(letra) || Character.isWhitespace(letra)))
          break;  	// Admite Letras
        if ((AdmiteCar & CHAR_NUMERO) == 4 && Character.isDigit(letra))
          break;            	// Admite Digitos
        MsgError="Caracter: '"+letra+ "' NO Admitido";
        return false;
    }
    return true;
  }
  /**
   * Define los caracteres Especiales a admitir.
   * Usado solo cuando en el formato se pone un interrogante '?' y
   * admiteCar es CHAR_LIMIT
   *
   * @param carEsp String
   */
  public void setStrCarEsp(String carEsp)
  {
    StrCarEsp=carEsp;
  }
  public String getStrCarEsp()
  {
    return StrCarEsp;
  }
  /**
   * Especifica que caracteres se deben admitir cuando en el formato se pone
   * un "?"
   * @param carEsp String
   */
  public void setCaracterAceptar(String carEsp)
  {
    setAdmiteCar(CTextField.CHAR_LIMIT);
    setStrCarEsp(carEsp);
  }
  /**
   * Especifica que tipos de caracter debe admitir cuando el caracter
   * de formato es "?".<br>
   * Los valores aceptados son: CHAR_LIMIT, CHAR_LETRA,CHAR_NUMERO y CHAR_TODO
   *
   * @see setFormato(String formato)
   * @see setStrCarEsp(String car)
   * @param admiteCar int
   */
  public void setAdmiteCar(int admiteCar)
  {
    AdmiteCar=admiteCar;
  }
  public int getAdmiteCar()
  {
    return AdmiteCar;
  }
  public void setAceptaNulo(boolean aceptaNull)
  {
    this.AceptaNulo=aceptaNull;
  }
  public boolean getAceptaNulo()
  {
    return this.AceptaNulo;
  }

  private boolean isCarEspe(char tec)
  {
    for (int n=0;n<StrCarEsp.length();n++)
    {
      if (tec==StrCarEsp.charAt(n))
        return true;
    }
    return false;

  }

  private boolean carEsValidDec(char tecla,int posicion,String t)
  {

    if (Character.isDigit(tecla)) // Es digito  Aceptarlo en cualquier posicion
    {
      if (getSelectionStart() != getSelectionEnd())
        return true;
      // Comprobar que el Numero resultante seria valido.
      String text1="";
      int n=0;
      for (n=0;n<t.length();n++)
      {
        if (n==posicion)
          text1=text1+tecla;
        text1=text1+t.charAt(n);
      }
      if (n==posicion)
        text1=text1+tecla;
      return comp_lgdec(text1);
    }


    if (tecla == Formatear.DECIMALSEPARATOR && aceptapunto== false)
    {
      MsgError="No se Admiten Decimales en este Campo";
      return false;
    }

    if (tecla=='-' && aceptamenos==false)
    {
      MsgError="No se Admiten Numeros Negativos en este Campo";
      return false;
    }


    if (tecla == '-')
    {
      if (posicion != 0 && getSelectionStart()==getSelectionEnd())
      {
        MsgError="El signo de Negacinn debe ser el Primer Caracter";
        return false;
      }
      // Comprobar que sea el unico simbolo '-'
      int pg=t.indexOf(Formatear.MINUSSIGN);
      if (pg==-1)
        return true;
      if (getSelectionStart() == getSelectionEnd())
      {
        MsgError="Solo debe haber un signo de Negacion";
        return false;
      }
      if (pg>=getSelectionEnd() || pg < getSelectionStart())
      {
        MsgError="Solo debe haber un signo de Negacion";
        return false;
      }
      return true;
    }

    if (tecla == Formatear.DECIMALSEPARATOR )
    { // Comprobar que sea el unico punto
      int pg=Formatear.buscaletra(t,Formatear.decimalSeparator.charAt(0));
      if (pg==-1)
        return true;
      if (getSelectionStart() == getSelectionEnd()  )
      {
        MsgError="Solo debe haber un Punto Decimal";
        return false;
      }
      if (pg >= getSelectionEnd() || pg < getSelectionStart())
      {
        MsgError="Solo debe haber un Punto Decimal";
        return false;
      }
      return true;
    }

    if (tecla ==Formatear.GROUPSEPARATOR)
      return true;

    MsgError="carEsValidDec: Caracter: "+tecla+" NO VALIDO";
    return false;
  }
  private boolean comp_lgdec(String Text1)
  {
    calcNumDec(Text1);

    if (nc_dec > nc_DecMasc || nc_ent > nc_EntMasc)
      return false;
    return true;
  }

  private void calcNumDec(String Text1)
  {
    nc_ent=0;
    nc_dec=0;
    boolean sw_dec=false;
    for (int n=0;n<Text1.length();n++)
    {
      if (Text1.charAt(n)==Formatear.DECIMALSEPARATOR)
      {
      sw_dec=true;
      continue;
    }
    if (Text1.charAt(n)== Formatear.GROUPSEPARATOR || Text1.charAt(n)==' ' || Text1.charAt(n)==Formatear.MINUSSIGN)
      continue;

    if (sw_dec)
      nc_dec++;
    else
      nc_ent++;
    }
  }


  /**
   * @todo poner en orden este tema
   */
  private String pulsatecla()
  {
    if (Ltexto >= MaxLong && MaxLong > 0)
    {
      sonido();
      noWrite=true;
      return "";
    }


    switch (tipoCampo)
    {
      case Types.CHAR:
        if ( ! formatchar() )
        {
          sonido();
          noWrite=true;
          return "";
        }

        if (posicion + 1 == nCarCampo && AutoNext &&   this.getSelectionStart() == this.getSelectionEnd())
        { // Realizar AutoNext.
          if (SonidoAutoNext)
            sonido();
          trFocus = true;
          return "" + tecla;
        }
        break;
      case Types.DECIMAL:
        // Types Decimal.
        if (! carEsValidDec(tecla,posicion,super.getText()))
        {
          sonido();
          noWrite=true;
          return "";
        }

        if (AutoNext && this.getSelectionStart()==this.getSelectionEnd() )
        {
          int posStart=this.getSelectionStart();
          String s;
          String tx=super.getText();
          if (tx.length()>posStart)
          {
            return ""+tecla;
          }
          s=tx+tecla;
          calcNumDec(s);
          if (nc_dec == nc_DecMasc && aceptapunto)
          {
            if (SonidoAutoNext)
              sonido();
            trFocus=true;
          }
          if (nc_ent == nc_EntMasc && aceptapunto==false)
          {
            if (SonidoAutoNext)
              sonido();
            trFocus=true;
          }
        }
        return ""+tecla;
      case Types.DATE:
        // Types Fecha.
//	       	ev_tecla.consume(); // Consume la pulsacion. La simulamos nosotros.
        String s=formatdate();
        if (posicion+1 == nCarCampo && AutoNext && this.getSelectionStart()==this.getSelectionEnd() )
        {
          if (SonidoAutoNext)
            sonido();
          trFocus=true;
        }
        return s;
    }
    return ""+tecla;
  }
  private String formatdate()
  {
    return controlDATE();
     /*   	if ( ! controlDATE())
         {
                  sonido();
           noWrite=true;
           return "";
         }

//    Texto=getdate();
         return Texto;*/
  }

  /***********************************
   * Controla que la tecla pulsada, en la posicion actual es valida.
   * en un tipo DATE.
   * La introduce en la variable fecha[] en la 'posicion' actual.
   * Devuelve: true si es valida.
   *           false si no es valida.
   ************************************/
  private String controlDATE()
  {
    if (posicion>=Formato.length())
    {
      noWrite=true;
      return "";
    }
    if (super.getText().length()>=Formato.length())
    {
      noWrite=true;
      return "";
    }
     /*    String tx1=super.getText();
         int lg1=tx1.length();

         for (int n=0;n<10;n++)
         {
           if (n>=lg1)
             fecha[n]=' ';
           else
             fecha[n]=tx1.charAt(n);
         }
     */
    if (Character.isDigit(tecla))
    {
      if (Formato.charAt(posicion)==SepFecha)
      {
        return SepFecha+""+tecla;
//				posicion++;
      }
      return ""+tecla;
    }

    if ( tecla == SepFecha)
    {
      for (int n=posicion;n<Formato.length();n++)
      {
        if (Formato.charAt(n)==SepFecha)
        {
          posicion = n + 1;
          break;
        }
      }
      return ""+tecla;
    }
    noWrite=true;
    return "";
  }
  /**********************************
   * Pasa a un String la fecha, guardado en variable fecha[].
   * No formatea nada. Solo pone el separador que se ha establecido en
   * el formato en el lugar que debe.
   ***********************************/
  private String getdate()
  {
    String Text="";
    for (int n=Formato.length()-1;n>=0;n--)
    {
      if (Formato.charAt(n)==SepFecha)
      {
      Text=SepFecha+Text;
      continue;
    }
    Text=fecha[n]+Text;
    }
    return Text;
  }
  /**
   * Pasa la fecha de un string a la variable fecha[].
   * @param fec String  Fecha a Pasar
   */
  private void setdate(String fec)
  {
    int n;

    for(n=0;n<Formato.length();fecha[n++]=' ');

    for (n=0;n<Formato.length();n++)
    {
      if (n>= fec.length())
        return;
      if (Formato.charAt(n)==SepFecha)
      {
        fecha[n]=' ';
        continue;
      }
      fecha[n]=fec.charAt(n);
    }
  }


  /*
   * Pasa a las variables dia[],mes[] y ano[] los datos de fecha[].
   * Comprueba que la fecha resultantes sea valida, en todos los conceptos.
   * Devuelve: true si lo ha pasado correctamente.
   *           False si encuentra errores al pasarlos.
   * En la variable global Texto1 deja la fecha ya formateada.
   */
  private boolean setdate1()
  {
    String s;
    int n;
    int ndia=0;
    int nmes=0;
    int nano=0;

    for(n=0;n<2;dia[n++]=' ');
    for(n=0;n<2;mes[n++]=' ');
    for(n=0;n<4;ano[n++]=' ');


    for (n=0;n<Formato.length();n++)
    {
      switch (Formato.charAt(n))
      {
         case 'd':
          dia[ndia++]=fecha[n];
          break;
         case 'M':
          mes[nmes++]=fecha[n];
          break;
         case 'y':
          ano[nano++]=fecha[n];
      }
    }

    if (dia[0]==' ')
      dia[0]='0';
    if (dia[1]==' ')
    {
      dia[1]=dia[0];
      dia[0]='0';
    }
    if (mes[0]==' ')
      mes[0]='0';

    if (mes[1]==' ')
    {
      mes[1]=mes[0];
      mes[0]='0';
    }
    if (Character.isDigit(dia[0])== false || Character.isDigit(dia[1])==false)
    {
      MsgError="Los caracteres del DIA No son validos";
      Error=true;
      return false;
    }
    if (Character.isDigit(mes[0])== false || Character.isDigit(mes[1])==false)
    {
      MsgError="Los caracteres del MES No son validos";
      Error=true;
      return false;
    }

    String  ano1=""+ano[0]+ano[1]+ano[2]+ano[3];
    int ndigano=0;
    for (n=0;n<nano;n++)
    {
      if (Character.isDigit(ano[n]))
        ndigano++;
    }
    try
    {
      n = Integer.parseInt(ano1.trim());
    }
    catch (NumberFormatException k)
    {
      n = -1;
    }

    if (ndigano != 2 && ndigano != 4)
    {
      MsgError = "El numero de digitos del Año debe ser 2 ó 4";
      Error = true;
      return false;
    }

    if (ndigano <= 2 && nano == 4)
    {
      // Ajustar a 4 digitos el ano.
      if (n < SigloSig)
        n = 2000 + n;
      else
        n = 1900 + n;
        // Lo vuelve a pasar a la variable ano.
      s = "" + n;
      for (int n1 = 0; n1 < 4; n1++)
        ano[n1] = s.charAt(n1);
    }

    if (ajusActual)
    {
      if (n < 1900  && nano==4)
      {
        n = n + 2000;
        s = "" + n;
        for (int n1 = 0; n1 < 4; n1++)
          ano[n1] = s.charAt(n1);
      }
    }

    Texto1 = getDate1();

    int dia1 = getNumero("" + dia[0] + dia[1]);
    int mes1 = getNumero("" + mes[0] + mes[1]);
    int ano2;
    if (nano==2)
        ano2 = getNumero("" + ano[0] + ano[1]);
    else
        ano2 = getNumero("" + ano[0] + ano[1] + ano[2] + ano[3]);


    if (!esfechavalida(dia1, mes1, ano2))
    {
      Error = true;
      return false;
    }

    return true;
  }
  /**
   * Devuelve true si el valor del campo esta vacio o 0 si es numerico 
   * @return 
   */
  public boolean isNull()
  {
    return isNull(true);
  }
  public char getSepFecha()
  {
    return SepFecha;
  }
  public boolean isNull(boolean trim)
  {
    String txt = super.getText();
    if (tipoCampo == Types.DATE)
    {
      // Quitar Guiones.
      if (getError())
        return true;
      txt = txt.replace(SepFecha, ' ');
      txt = txt.trim();
    }
    if (tipoCampo == Types.DECIMAL && getValorDec() == 0 && ceroIsNull)
      return true;

    if (trim)
      txt = txt.trim();

    if (txt.equals(""))
      return true;
    else
      return false;
   }
  /**
   * Devuelve si en una query se aceptaran comodines. (*, ?... )
   * Por defecto es true
   * @return 
   */
   public boolean isAceptaComodines()
   {
     return aceptaComodines;
   }
   /**
    * Indica si en una query se acetaran comodines. (*, ?... 
    * Este valor por defecto es true.
    * @param aceptaComodines `true/false
    */
   public void setAceptaComodines(boolean aceptaComodines)
   {
     this.aceptaComodines=aceptaComodines;
   }
   /**
    * Devuelve si en una query si el campo es tipo char se incluiran comodines 
    * automaticamente. Es decir si a la cadena se le incluira el caracter % al principio y al final.
    * @return true si se incluiran comodines.
    */
   public boolean isIncluirComodines()
   {
     return incluirComodines;
   }
   /**
    * Indica si se deben incluir comodines al devolver una cadena de query 
    * en el caso de q el campo sea tipo CHAR.
    * Por defecto es true
    * @param incluirComodines 
    */
   public void setIncluirComodines(boolean incluirComodines)
   {
     this.incluirComodines=incluirComodines;
   }
   public boolean esfechavalida(int dia, int mes, int ano)
  {
    if (mes > 12 || mes < 1)
    {
      MsgError="El mes debe estar comprendido entre 1 y 12";
      return false;
    }
    if (dia > 31 || dia < 1)
    {
      MsgError="El dia debe estar comprendido entre 1 y 31";
      return false;
    }
    if (mes == 4 || mes == 6 || mes == 9 || mes == 11)
    {
      if (dia > 30)
      {
        MsgError="El dia para el mes: "+mes+ " No puede ser superior a 30";
        return false;
      }
    }

    if (mes == 2)
    {
      if (dia > 29)
      {
        MsgError="Febrero NO puede tener mns de 29 dias";
        return false;
      }
      if (!gcalend.isLeapYear(ano))
      {
        if (dia > 28)
        {
          MsgError="Febrero en Ano NO bisiesto no puede tener 29 dias";
          return false;
        }
      }

    }


    return true;

  }

  int getNumero(String num)
  {
    try
    {
      return Integer.parseInt(num);
      } catch (NumberFormatException k)  { }
      return -1;
  }
                  /*
  * Devuelve en un String la fecha
  * lo que haya en dia[],mes[] y ano[]  (tambien la guarda en la variable 'fecha[]')
              */

  private String getDate1()
  {

    String t;
    int ndia=0;
    int nmes=0;
    int nano=0;
    t="";
    for (int n=0;n<Formato.length();n++)
    {
      switch (Formato.charAt(n))
    {
      case 'd':
        t=t+dia[ndia];
        fecha[n]=dia[ndia++];
        break;
      case 'M':
        t=t+mes[nmes];
        fecha[n]=mes[nmes++];
        break;
      case 'y':
        t=t+ano[nano];
        fecha[n]=ano[nano++];
        break;
      default:
        t=t+SepFecha;
    }
    }
    return t;
  }


  private char controlCHAR(int pos,char tec)
  {
    tec=controlCHAR(tec);

    char ft;
    if (pos < Formato.length())
      ft=Formato.charAt(pos);
    else
      ft='?';

    if (ft=='M')
      tec=Character.toUpperCase(tec);
    if (ft=='m')
      tec=Character.toLowerCase(tec);
    return tec;
  }
  public void sonido()
  {
    if (! EjecSonido)
      return;
    Toolkit p = getToolkit();
//     for (int n=1;n<50;n++)
    p.beep();
  }
  public boolean getEjecSonido()
  {
    return EjecSonido;
  }
  public void setEjecSonido(boolean v)
  {
    EjecSonido=v;
  }
  /**
   *  Emula que se conecta a una columna, aunque no se Linka a la base
   * de datos.
   * Util para realizar Querys sin unirlo a la base de datos.
   */
   public void setColumnaAlias(String col)
   {
     conectaColumna=col;
   }
   public String getColumnaAlias()
   {
           return conectaColumna;
   }
   public java.util.Date getDate() throws java.text.ParseException
   {
     return getDate(false);
   }
   public String getSQLDate() throws java.text.ParseException
   {
     return Formatear.getSQLDate(getDate(false));
   }
   /**
    * Devuelve el Texto del TextField como un Date
    * @param trim boolean si el campo es nulo si trim es false devolvera null en caso contrario un
    * date con la fecha 01-01-1970
    * @throws ParseException Error al convertir a fecha
    * @return Date Fecha del TextField
    */
   public java.util.Date getDate(boolean trim) throws java.text.ParseException
   {
     if (getTipoCampo()!=Types.DATE)
        throw new ParseException("Tipo de Campo NO es Fecha",0);
      SimpleDateFormat sd = new SimpleDateFormat(Formato);
      if (getError())
          return null;
               
      String s;
      if (isNull())
      {
        if (! trim)
          return null;
        s="01-01-1970";
      }
      else
        s = super.getText();
      Date dt = sd.parse(s);
      return dt;
   }

   public void setDate(java.util.Date dt)
   {
     if (dt==null)
       resetTexto();
     else
       setText(Formatear.formatearFecha(dt,Formato));
   }
   /**
    * Devuelve la fecha en el formato marcado en setFormato
    * @return Devuelve "" si la fecha no es valida.
    */
   public String getFecha()
   {
     try
     {
       return getFecha(getFormato());
     }
     catch (java.text.ParseException k)
     {       
       return "";
     }
   }
   /**
    * Ajustar fecha a Periodo actual.
    * Si es mandado true cuando se introduzca una fecha inferior a 1900
    * sera ajustada a 1900 + la fecha introducida
    * @param ajusFec boolean Ajustar la Fecha
    */
   public void setAjusFechaAct(boolean ajusFec)
   {
     ajusActual=ajusFec;
   }
   public boolean getAjusFechaAct()
   {
     return ajusActual;
   }

   public String getMsgError()
   {
     return MsgError;
   }
    public String getFecha(String fr) throws java.text.ParseException
    {
      if (getTipoCampo()!=Types.DATE)
        throw new ParseException("Tipo de Campo NO es Fecha",0);
      if (isNull())
        return "";
      return Formatear.formatearFecha(getText(),getFormato(),fr);
    }
    /**
    * Devuelve el texto del VTextField en un String
    * con el formato de la base de datos. 'yyyyMMdd'
    * Null SI no hay fecha
    */
    public String getFechaDB() throws java.text.ParseException
    {
      Date dt = getDate();
      if (dt==null)
        return null;
      GregorianCalendar gcal = new GregorianCalendar();
      gcal.setTime(dt);
      return  Formatear.format(gcal.get(GregorianCalendar.YEAR),"0000")+
          (Formatear.format(gcal.get(GregorianCalendar.MONTH)+1,"00"))+
          Formatear.format(gcal.get(GregorianCalendar.DAY_OF_MONTH),"00");
    }

    protected boolean  doClick(int keyCode)
    {
      return true;
    }
    /**
     * Especifica si se considera que el valor CERO es nulo
     * Por defecto se considera que si.
     * @see getCeroIsNull()
     * @param ceroIsNull
     */
    public void setCeroIsNull(boolean ceroIsNull)
    {
      this.ceroIsNull=ceroIsNull;
    }
    /**
     * Devuelve si el valor cero se considerara como nulo.
     * Por defecto es que si.
     * @return valor cero = null (usado sobre todo para CLinkBox)
     */
    public boolean getCeroIsNull()
    {
      return ceroIsNull;
    }
    /**
     * Establece si debe intentar coger el peso de una bascula cuando el valor
     * es 0 y el tipo de campo es decimal.
     *
     * @param eu EntornoUsuario De donde coger el peso. Si es NULL no intentara coger el peso
     *
     */
    public void setLeePesoBascula(BotonBascula bascula)
    {
      if (bascula!=null)
          this.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
      this.bascula=bascula;
    }
    public ClipEditable getMenuContestual()
    {
      return menuContestual;
    }

    public void requestFocusLater()
    {
      javax.swing.SwingUtilities.invokeLater(new Thread()
      {
            @Override
        public void run()
        {
          CTextField.this.requestFocus();
        }
      });
    }

    public boolean getDependePadre()
    {
      return dependePadre;
    }
    /**
     * Indica si depende del padre (normalmente un CPanel) para ponerse enabled/disabled.
     * Normalmente cuando el padre es puesto a disabled el CTextField tambien es puesto y viceversa.
     * 
     * @param dependPadre 
     */
    public void setDependePadre(boolean dependPadre)
    {
      dependePadre=dependPadre;
    }

}
/**
 * Clase para verificar si el textfield esta bien
 * @author cpuente
 */
class CTextFieldVerifier extends InputVerifier
{


  public boolean verify(JComponent input)
  {
    CTextField c=(CTextField) input;
    if (c.puedoSalir())
      return true;
    else
      return false;
  }
}

class DocumentCTextField extends PlainDocument
{
  CTextField tf;

  public DocumentCTextField(CTextField txField)
  {
    tf=txField;
  }

    @Override
  public void insertString(int offs, String str, AttributeSet a)
      throws BadLocationException
  {
    if (!tf.isFocusOwner())
    {
      super.insertString(offs, str, a);
      return;
    }

    if (str == null)
    {
      super.insertString(offs, str, a);
      return;
    }
    char[] upper = str.toCharArray();
    String strR="";
    String strT="";
    tf.trFocus=false;
    tf.posicion=offs;
    tf.Ltexto=tf.getTextSuper().length();
    for (int i = 0; i < upper.length; i++)
    {
      tf.noWrite=false;
      tf.tecla=upper[i];
      strT=tf.procesaTecla();
      if (! tf.noWrite)
      {
        strR=strR+strT;
        tf.Ltexto+=strT.length();
      }
      if (tf.trFocus)
        break;
    }
    super.insertString(offs, strR, a);
    if (tf.trFocus)
      tf.transferFocus();
  }
}

