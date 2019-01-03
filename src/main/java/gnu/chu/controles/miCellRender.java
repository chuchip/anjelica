package gnu.chu.controles;
/**
 * 
 * <p>Título: miCellRender</p>
 * <p>Descripción: Clase usada para poner color a las celdas de la tablas.
 * Se puede ver un uso en el programa gnu.chu.anjelica.almacen.CLinvcong
 * </p>
 * <p>Copyright: Copyright (c) 2005-2012
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los términos de la Licencia Pública General de GNU segun es publicada por
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
 * @author chuchi P.
 * 
 * 
 */
import gnu.chu.interfaces.VirtualGrid;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


public class miCellRender extends  DefaultTableCellRenderer
{
   Cgrid padre;
   Color oldForeground;
   Color oldBackground;
   String nombre="Grid";
   Color foreTrueColor=Color.red;
   Color backTrueColor;
   VirtualGrid virtualGrid;
//   Color colorSel=Color.white;
//   Color colorBac=new Color(-6710836);
   Font fuente=null;
   public miCellRender(Cgrid jt)
   {
       padre=jt;
   }
    @Override
   public Component getTableCellRendererComponent(JTable table,Object value,
             boolean isSelected, boolean hasFocus, int row,  int column){
    Component c = super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
    if (virtualGrid!=null)
    {
      if (virtualGrid.getColorGrid(row,column,value,isSelected,nombre)){
        if (foreTrueColor!=null)
          c.setForeground(foreTrueColor);
        else
          c.setForeground(dimeColorFore(table,isSelected));

        if (backTrueColor!=null)
         c.setBackground(backTrueColor);
        else
          c.setBackground(dimeColorBack(table,isSelected));
      }
      else
      {
          if (! isSelected)
          {
            c.setForeground(dimeColorFore(table, isSelected));
            c.setBackground(dimeColorBack(table, isSelected));
          }
      }
    }
    else
    {
      if (!isSelected)
      {
        c.setForeground(dimeColorFore(table,isSelected));
        c.setBackground(dimeColorBack(table,isSelected));
      }

    }


    if (c instanceof JLabel)
    {
      if (table.getValueAt(row, column)!=null)
      {
        if (table.getValueAt(row, column).toString().length() * 7 >
            table.getColumn(table.getColumnName(column)).getWidth())
          ( (JLabel) c).setToolTipText(table.getValueAt(row, column).toString());
        else
          ( (JLabel) c).setToolTipText(padre.getToolTipText());
      }
    }

    if (fuente!=null)
      c.setFont(fuente);
    else
      c.setFont(new Font("Dialog", 0, 11));
    return c;
   }

   Color dimeColorFore(JTable table,boolean isSelected){
      if (oldForeground!=null)
        return oldForeground;
      else{
        if (isSelected)
           return table.getSelectionForeground();
        else
         return table.getForeground();
     }
   }

   Color dimeColorBack(JTable table,boolean isSelected){
     if (isSelected)
       return table.getSelectionBackground();
     if (oldBackground!=null)
      return oldBackground;
     else
       return table.getBackground();
   }

   public void setOldForeground(Color c){
     oldForeground=c;
   }
   public void setOldBackground(Color c){
     oldBackground=c;
   }
   public void setNombre(String s){
    nombre=s;
   }
   public void setErrForeColor(Color trueColor){
      foreTrueColor=trueColor;
   }
   public void setErrBackColor(Color trueColor){
      backTrueColor=trueColor;
   }
   public void setVirtualGrid(VirtualGrid vg){
      virtualGrid=vg;
    }
   public void setFuente(Font f)
   {
//    System.out.println("Llamado a set Fuente: "+f);
    fuente=f;
   }

  }

