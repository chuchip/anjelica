package gnu.chu.pruebas;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import java.awt.event.*;
import java.awt.*;
import java.util.EventObject;
import gnu.chu.controles.*;
import java.sql.*;
import java.util.*;
import gnu.chu.utilidades.*;
//import de.kleopatra.support.debugon.*;

/** Implement "light edit mode" (left/right arrows navigate on a
 *	per cell basis even when isEditing().
 *	Trying to put the table's navigationalActions into the
 *	editorComponent's action map instead of keeping focus in table
 *
 *
 *	@author (C) 2001 Jeanette Winzenburg
 *	@version	15. November 2001
 *
 */
public class LightEdit {
   CTextField campo1 = new CTextField(Types.DECIMAL,"###9");
   CTextField campo2= new CTextField(Types.DECIMAL,"--9.99");
   CTextField campo3= new CTextField(Types.CHAR,"X",3);

	protected JFrame frame ;
	// table vars
	protected final int ROWS = 15;
	protected final int COLUMNS = 3;
	protected DefaultTableModel model;
	protected JTable table;

	public LightEdit() {
		frame = new JFrame("LightEdit");
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		frame.getContentPane().add(buildMainPanel());
		frame.getContentPane().add(buildButtonPanel(), BorderLayout.SOUTH);
		frame.pack();
//		frame.setSize(300, 200);
		frame.show();
	}

//------------------------inner class

	public class JLightEditTable extends JTable {
          // actions to be put into editor's actionMap
          protected Action rightAction;
          protected Action leftAction;
          protected boolean preMerlin;

          public JLightEditTable(int row, int col)
          {
            super(row, col);
            initJavaVersion();
            ponerEventos();
            TableSorter sorter = new TableSorter(new DefaultTableModel(row,col));

            setModel(sorter);
//            sorter.addMouseListenerToHeaderInTable(this);

          }

                void ponerEventos()
                {
                  // PONE QUE el ENTER actue igul que el TABULADOR
                  this.getColumnModel().addColumnModelListener(new TableColumnModelListener()
                      {
                        public void columnAdded(TableColumnModelEvent e){}

public void columnRemoved(TableColumnModelEvent e){}

/** Tells listeners that a column was repositioned. */
public void columnMoved(TableColumnModelEvent e) {}

/** Tells listeners that a column was moved due to a margin change. */
public void columnMarginChanged(ChangeEvent e) {}

/**
 * Tells listeners that the selection model of the
 * TableColumnModel changed.
 */
public void columnSelectionChanged(ListSelectionEvent e)
{
  if ( e.getValueIsAdjusting())// && e.getFirstIndex() == e.getLastIndex())
    return;
  System.out.println("Cambiada columna");

  cambiaLinea();

}
});
                 this.getSelectionModel().addListSelectionListener(new ListSelectionListener()
                    {
                       public void valueChanged(ListSelectionEvent e)
                       {
                         System.out.println("valueChanged ");
                         if ( e.getValueIsAdjusting())// && e.getFirstIndex() == e.getLastIndex())
                           return;
                         cambiaLinea();
                      }
                    });
                }
                void cambiaLinea()
                {
//                  System.out.println("en CambiaLinea");
                    table.editCellAt(table.getSelectedRow(),table.getSelectedColumn());
                }
		public boolean editCellAt(int row, int col, EventObject e) {
			if (super.editCellAt(row, col, e)) {
                          System.out.println("Editando celda: Row: "+row+" Col: "+col);
				hackFocusForEditorComponent();
				// need to invoke because of new focus architecture
				SwingUtilities.invokeLater(new Runnable() { public void run() {
//					replaceEditorNavigation();
				}});
				return true;
			}
			return false;
		}

	//---------------------------helper in fact this is where the music plays

		/** PRE: isEditing();
		 */
		protected void hackFocusForEditorComponent() {
			// dont care about surrender - always request focus on comp
//                        System.out.println("requestFocus al componente ");
			getEditorComponent().requestFocus();
			if (!preMerlin) {
				// **** merlin beta 3
				// hack around submitted bug (review ID 135159):
				// tabbing out of an editing combo focus the next comp
				// _outside_ the table
				if (getEditorComponent() instanceof JComboBox) {
					JComboBox box = (JComboBox) getEditorComponent();
					if (box.isEditable()) {
						((JComponent) box.getEditor().getEditorComponent()).setNextFocusableComponent(this);
					}
				}
			}
		}

		/** replaces the left/right arrow key bindings in editorComponent
		 *	with wrappers around table's navigational actions.
		 */
//		protected void replaceEditorNavigation() {
//                  System.out.println("replaceEditorNavigation");
//			JComponent focused = findFocused();
//			if (focused != null) {
//				replaceEditorNavigation(focused, "RIGHT", "right", getRightAction());
//				replaceEditorNavigation(focused, "LEFT", "left", getLeftAction());
//			}
//		}

		protected void replaceEditorNavigation(JComponent realEditor,
			String keyName, String tableActionName, Action tableAction)	{
//      System.out.println("ReplaceEditorNavigation1");
			KeyStroke navigationKey = KeyStroke.getKeyStroke(keyName);
			Object editorActionValue = (String) realEditor.getInputMap().get(navigationKey);
			// nothing registered for the key, so we have to put it in first
			if (editorActionValue == null) {
				editorActionValue = tableActionName;
				realEditor.getInputMap().put(navigationKey, editorActionValue);
			}
			realEditor.getActionMap().put(editorActionValue, tableAction);
		}

		protected Action getRightAction() {
			if (rightAction == null) {
				rightAction = createWrappedAction(this, getActionMap().get("selectNextColumnCell"));
			}
			return rightAction;
		}

		protected Action getLeftAction() {
			if (leftAction == null) {
				leftAction = createWrappedAction(this, getActionMap().get("selectPreviousColumnCell"));
			}
			return leftAction	;
		}

		protected Action createWrappedAction(final Object source, final Action action) {
			// have to replace the original actionevent with one pointing to
			// the table as source
			Action wrappedAction = new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					ActionEvent newEvent = new ActionEvent(source, e.getID(), e.getActionCommand(), e.getModifiers());
					action.actionPerformed(newEvent);
				}
			};
			return wrappedAction;
		}

		/** returns the component that has the focus in editorComponent.
		 *	PRE: isEditing()
		 */
		protected JComponent findFocused() {
			Component result = SwingUtilities.findFocusOwner(getEditorComponent());
			if (result instanceof JComponent) {
				return (JComponent) result;
			}
			return null;
		}

	//---------------------------init ui

		protected void initJavaVersion() {
			String version = System.getProperty("java.vm.version");
			preMerlin = (version == null) || !version.startsWith("1.4");
		}

	} // end inner class JLightEditTable

//---------------------------init ui

	protected JComponent buildMainPanel() {
		JPanel panel = new JPanel();
		initTable();
		JScrollPane scroll = new JScrollPane(table);
		panel.add(scroll);
		return panel;
	}

	protected void initTable() {
		table = createTable(ROWS, COLUMNS);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		initComboEditor(1, true);
//		initComboEditor(2, false);
	}

	protected void initComboEditor(int col, boolean editable) {
	// make a long list to force non lightweight popup
//		setColumnEditorAt(editor, col);
                ponEventoTextField(campo1);
                ponEventoTextField(campo2);
                ponEventoTextField(campo3);

                table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(campo1));
                table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(campo2));
                table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(campo3));

	}

        void ponEventoTextField(final CTextField tf)
        {
          tf.addKeyListener(new KeyAdapter()
          {
            public void keyPressed(KeyEvent e)
            {
              if (!e.isControlDown() && !e.isShiftDown())
              {
                if (e.getKeyCode()==KeyEvent.VK_F9)
                {
                  System.out.println("pulsado f2");
                    tf.setSelectionStart(0);
                    tf.setSelectionEnd(0);
                  return;
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                {
                  if (tf.getCaretPosition()!=tf.getTextSuper().length())
                    return;
                  System.out.println("A la derecha");
                  if (table.getSelectedColumn()+1 == table.getColumnCount())
                  {
                    table.setColumnSelectionInterval(0,0);
                    if (table.getSelectedRow()+1<table.getRowCount())
                      table.setRowSelectionInterval(table.getSelectedRow()+1,table.getSelectedRow()+1);
                  }
                  else
                    table.setColumnSelectionInterval(table.getSelectedColumn()+1,table.getSelectedColumn()+1);
                  return;
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT)
                {
                  if (tf.getCaretPosition()==0 || (tf.getSelectionStart()==0 && tf.getSelectionEnd()==tf.getTextSuper().length()))
                  {
                    if (table.getSelectedColumn()==0)
                    { // Subir de Columna
                      table.setColumnSelectionInterval(table.getColumnCount()-1,table.getColumnCount()-1);
                      if (table.getSelectedRow()>0)
                        table.setRowSelectionInterval(table.getSelectedRow()-1,table.getSelectedRow()-1);
                    }
                    else
                      table.setColumnSelectionInterval(table.getSelectedColumn()-1,table.getSelectedColumn()-1);
//                    tf.setCaretPosition(tf.getTextSuper().length()-1);
                    System.out.println("A la izquierda");
                  }
                }

              }
              tf.addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) {
                 System.out.println("Perdido Foco");
                }

                public void focusLost(FocusEvent e) {
                  System.out.println("Ganado Foco");
                }
              });
            }
          });
        }


	protected JComponent buildButtonPanel() {
		JPanel panel = new JPanel();
		JButton button = new JButton("do something");
		button.setMnemonic('d');
		frame.getRootPane().setDefaultButton(button);
		ActionListener action = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
			}
		};
		button.addActionListener(action);
		panel.add(button);
		return panel;
	}

//---------------------------factory methods

	protected JTable createTable(int r, int c) {
		JTable table= new JLightEditTable(r, c);
		return table;
	}

//---------------------------Main

	public static void main(String[] args) {
	//	LFSwitcher.windowsLF();
		new LightEdit();
	}
}
