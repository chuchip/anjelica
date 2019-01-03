package gnu.chu.utilidades;

import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;
import java.text.*;

import gnu.chu.controles.*;
import gnu.chu.interfaces.*;

public class ClipEditable
    extends MouseAdapter implements ClipboardOwner
{
  CEditable editable;
  Component c;
  JTextComponent tc = null;
  JPopupMenu popMenu = new JPopupMenu();
  JMenuItem copy = new JMenuItem("Copiar", Iconos.getImageIcon("copia"));
  JMenuItem paste = new JMenuItem("Pegar", Iconos.getImageIcon("pon"));
  JMenuItem selec = new JMenuItem("Sel.Todo", Iconos.getImageIcon("fill"));
  public ClipEditable(CEditable controlEditable)
  {
    this.editable = controlEditable;
    if (editable instanceof Component)
    {
      this.c = (Component) editable;
      if (editable instanceof JTextComponent)
      {
        this.tc = (JTextComponent) editable;
        if (! tc.isEditable() || !tc.isEnabled() )
                 paste.setEnabled(false);
      }
      
    }
    else
      this.c = new CTextField();

    popMenu.add(copy);
    popMenu.add(paste);
    popMenu.add(selec);
    copy.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        String s = editable.getText();
        if (tc != null)
          s = getTextTC();
        clipboard(formatCopy(s),c);
      }
    });

    selec.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {

        if (tc != null)
        {
          tc.setSelectionStart(0);
          tc.setSelectionEnd(tc.getText().length());
        }

      }
    });
    paste.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        try
        {
            
       
          String strPaste=getClipboardContents();

          strPaste = formatPaste(strPaste);
          if (tc != null)
            setTextTC(strPaste);
          else
            editable.setText(strPaste);
        }
        catch (Throwable j)
        {
          SystemOut.print(j);
        }
      }
    });

  }
  public JMenuItem getJMenuItemPaste()
  {
      return paste;
  }
  public String getClipboardContents() {
     String result = "";
     Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
     //odd: the Object param of getContents is not currently used
     Transferable contents = clipboard.getContents(null);
     boolean hasTransferableText = (contents != null) &&
                                   contents.isDataFlavorSupported(DataFlavor.stringFlavor);
     if ( hasTransferableText ) {
       try {
         result = (String)contents.getTransferData(DataFlavor.stringFlavor);
       }
       catch (UnsupportedFlavorException ex){
         //highly unlikely since we are using a standard DataFlavor
         System.out.println(ex);
       }
       catch (IOException ex) {
         System.out.println(ex);
       }
     }
     return result;
   }

    @Override
  public void mouseClicked(MouseEvent e)
  {
    
    if (e.getButton()!=MouseEvent.BUTTON3)
      return;
    if (tc!=null)
        paste.setEnabled(tc.isEnabled() && tc.isEditable());
    else
        paste.setEnabled(c.isEnabled());

    popMenu.show(c, e.getX(), e.getY());
  }

  public JPopupMenu getPopMenu()
  {
    return popMenu;
  }
  /**
   * Retorna el Texto Seleccionado de un JTextComponent
   *
   * @return el Texto Selecionado en JTextComponent
   *
   */
  private String getTextTC()
  {
    String s = tc.getText();
    if (tc.getSelectionStart() != tc.getSelectionEnd())
      s = tc.getSelectedText();
    return s;
  }

  /**
   * Anade el texto Pegado donde este el cursor sobreescribe lo seleccionado de un JTextComponent
   * @param paste Texto a pegar.
   */
  private void setTextTC(String paste)
  {
    String s = "";
    int caretPos = 0;

    if (tc.getSelectionStart() != tc.getSelectionEnd())
    {
      // Tiene una Seleccion Sobreescribe esta
      caretPos = tc.getSelectionStart() + paste.length();
      s = tc.getText().substring(0, tc.getSelectionStart()) + paste +
          tc.getText().substring(tc.getSelectionEnd());
    }
    else
    {
      // Pega apartir de la posicion del cursor
      caretPos = tc.getCaretPosition() + paste.length();
      s = tc.getText().substring(0, tc.getCaretPosition()) + paste +
          tc.getText().substring(tc.getCaretPosition());
    }
    tc.requestFocus();
    tc.setText(s);
    try
    {
      tc.setCaretPosition(caretPos);
    }
    catch (Throwable k1)
    {}
  }

  public static void clipboard(String texto)
  {
    clipboard(texto,new JLabel());
  }

  public static void clipboard(String texto,Component componente)
  {

    StringSelection t = new StringSelection(texto);
    componente.getToolkit().getSystemClipboard().setContents(t, t);
  }

  public static String formatCopy(String s)
  {
    try
    {
      Formatear f;
      double d = Formatear.StrToDouble(s);
      String ss = "";
      for (int i = 0; i < s.length(); i++)
      {
        if (s.charAt(i) != Formatear.GROUPSEPARATOR)
          ss += s.charAt(i);
      }
      s = ss;
      // Cambia los puntos decimales
      DecimalFormatSymbols dfs = new DecimalFormatSymbols(new Locale("es", "es", ""));
      s = s.replace(Formatear.DECIMALSEPARATOR, dfs.getDecimalSeparator());
    }
    catch (Throwable j)
    {}
    return s;
  }

  public static String formatPaste(String s)
  {
    try
    {
      double d = Formatear.StrToDouble(s);

      // Cambia los puntos decimales
      DecimalFormatSymbols dfs = new DecimalFormatSymbols(new Locale("es", "es", ""));
      s = s.replace(dfs.getDecimalSeparator(), Formatear.DECIMALSEPARATOR);

      if (s.charAt(s.length() - 1) == ( (char) 0))
        s = s.substring(0, s.length() - 1);
    }
    catch (Throwable j)
    {}
    return s;
  }
  @Override
  public void lostOwnership(Clipboard clipboard, Transferable contents)
  {

  }
}

