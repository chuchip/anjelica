package gnu.chu.controles;

import javax.swing.*;
import java.awt.*;
import gnu.chu.interfaces.*;
import gnu.chu.utilidades.ClipEditable;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CTextArea extends JTextArea implements CEditable,CQuery
{
  private boolean activado = true;
  private boolean activadoParent = true;
  private String valorAnt;
  private String conectaColumna="";
  private boolean query=false;
  private ClipEditable menuContestual;

  public CTextArea() {
       super();
       iniciar();
   }

   /**
    * Constructs a new TextArea with the specified text displayed.
    * A default model is created and rows/columns are set to 0.
    *
    * @param text the text to be displayed, or null
    */
   public CTextArea(String text) {
       super( text);
       iniciar();
   }

   /**
    * Constructs a new empty TextArea with the specified number of
    * rows and columns.  A default model is created, and the initial
    * string is null.
    *
    * @param rows the number of rows >= 0
    * @param columns the number of columns >= 0
    * @exception IllegalArgumentException if the rows or columns
    *  arguments are negative.
    */
   public CTextArea(int rows, int columns) {
       super( rows, columns);
       iniciar();
   }

   /**
    * Constructs a new TextArea with the specified text and number
    * of rows and columns.  A default model is created.
    *
    * @param text the text to be displayed, or null
    * @param rows the number of rows >= 0
    * @param columns the number of columns >= 0
    * @exception IllegalArgumentException if the rows or columns
    *  arguments are negative.
    */
   public CTextArea(String text, int rows, int columns) {
       super( text, rows, columns);
       iniciar();
   }

   /**
    * Constructs a new JTextArea with the given document model, and defaults
    * for all of the other arguments (null, 0, 0).
    *
    * @param doc  the model to use
    */
   public CTextArea(javax.swing.text.Document doc) {
       super(doc);
       iniciar();
   }

   /**
    * Constructs a new JTextArea with the specified number of rows
    * and columns, and the given model.  All of the constructors
    * feed through this constructor.
    *
    * @param doc the model to use, or create a default one if null
    * @param text the text to be displayed, null if none
    * @param rows the number of rows >= 0
    * @param columns the number of columns >= 0
    * @exception IllegalArgumentException if the rows or columns
    *  arguments are negative.
    */
   public CTextArea(javax.swing.text.Document doc, String text, int rows, int columns) {
       super(doc,text,rows,columns);
       iniciar();

   }
   public void resetTexto()
   {
     this.setText("");
   }

   public java.awt.Component getErrorConf()
   {
     return null;
   }

   public void setEnabled(boolean enab)
   {
     activado = enab;
     if (activado)
     {
       if (activadoParent)
         super.setEnabled(true);
       return;
     }
     super.setEnabled(false);
   }

   /**
    * @todo Para hacer cuando sea necesario
    * @param enab boolean
    */
   public void setEnabledParent(boolean enab)
   {
     activadoParent = enab;
     if (!enab)
     {
       if (super.isEnabled())
       {
         super.setEnabled(false);
       }
     }
     else
     {
       if (!super.isEnabled() && activado)
         super.setEnabled(true);
     }
   }
   public void setEditableParent(boolean edit)
   {

   }
   public void resetCambio()
   {
     valorAnt=this.getText();
   }

   public boolean hasCambio()
   {
     return ! valorAnt.equals(this.getText());
   }

   private void iniciar()
   {
     menuContestual = new ClipEditable(this);
     this.addMouseListener(menuContestual);
     this.setDisabledTextColor((Color) UIManager.get("ComboBox.disabledForeground"));
     this.addKeyListener(new KeyAdapter()
     {
            @Override
       public void keyPressed(KeyEvent e)
       {
         AbstractButton defaultButton;
         if (!e.isAltDown())
         {
           switch (e.getKeyCode())
           {
             case KeyEvent.VK_ENTER:
               if (e.isControlDown())
               {
                 defaultButton = getDefaultButton();
                 if (defaultButton != null)
                 {
                   defaultButton.requestFocus();
                   defaultButton.doClick();
                 }
               }
               break;
             case KeyEvent.VK_ESCAPE:
             case KeyEvent.VK_F2:
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
                 defaultButton.requestFocus();
                 defaultButton.doClick();
               }
               break;
           }
         }
       }
     });
   }

   public AbstractButton getDefaultButton()
   {
     return getButton(KeyEvent.VK_ENTER);
   }

   public AbstractButton getButton(int tecla)
   {
     return estatic.getButton(tecla,this);
   }

   public void setColumnaAlias(String col)
   {
     conectaColumna=col;
   }
   public String getColumnaAlias()
   {
     return conectaColumna;
   }
   public String getStrQuery()
   {
     if (getText().trim().equals(""))
       return "";
     return conectaColumna+" like '%"+getText()+"%'";
   }
   public void setQuery(boolean query)
   {
     this.query=query;
   }
   public boolean getQuery()
   {
     return query;
   }
}
