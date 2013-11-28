package gnu.chu.pruebas;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import javax.swing.DefaultCellEditor;

import javax.swing.JScrollPane;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.*;
import java.awt.event.*;
import gnu.chu.controles.*;
import java.sql.*;

/**
 * This is exactly like TableDemo, except that it uses a
 * custom cell editor to validate integer input.
 */
public class TableEditDemo extends JFrame {
    private boolean DEBUG = true;

    public TableEditDemo() {
        super("TableEditDemo");

        MyTableModel myModel = new MyTableModel();
        JTable table = new JTable(myModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        //Set up real input validation for the integer column.
        setUpIntegerEditor(table);

        //Add the scroll pane to this window.
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    private void setUpIntegerEditor(JTable table) {
        //Set up the editor for the integer cells.
        final CTextField integerField = new CTextField(Types.DECIMAL,"-999");
        integerField.setHorizontalAlignment(WholeNumberField.RIGHT);

        DefaultCellEditor integerEditor =
            new DefaultCellEditor(integerField) {
                //Override DefaultCellEditor's getCellEditorValue method
                //to return an Integer, not a String:
                public Object getCellEditorValue() {
                    return new Integer(integerField.getValorInt());
                }
            };
        table.setDefaultEditor(Integer.class, integerEditor);
    }

    class MyTableModel extends AbstractTableModel {
        final String[] columnNames = {"First Name",
                                      "Last Name",
                                      "Sport",
                                      "# of Years",
                                      "Vegetarian"};
        final Object[][] data = {
            {"Mary", "Campione",
             "Snowboarding", new Integer(5), new Boolean(false)},
            {"Alison", "Huml",
             "Rowing", new Integer(3), new Boolean(true)},
            {"Kathy", "Walrath",
             "Chasing toddlers", new Integer(2), new Boolean(false)},
            {"Sharon", "Zakhour",
             "Speed reading", new Integer(20), new Boolean(true)},
            {"Angela", "Lih",
             "Teaching high school", new Integer(4), new Boolean(false)}
        };

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col < 2) {
                return false;
            } else {
                return true;
            }
        }

        public void setValueAt(Object value, int row, int col) {
            if (DEBUG) {
                System.out.println("Setting value at " + row + "," + col
                                   + " to " + value
                                   + " (an instance of "
                                   + value.getClass() + ")");
            }

            data[row][col] = value;
            fireTableCellUpdated(row, col);

            if (DEBUG) {
                System.out.println("New value of data:");
                printDebugData();
            }
        }

        private void printDebugData() {
            int numRows = getRowCount();
            int numCols = getColumnCount();

            for (int i=0; i < numRows; i++) {
                System.out.print("    row " + i + ":");
                for (int j=0; j < numCols; j++) {
                    System.out.print("  " + data[i][j]);
                }
                System.out.println();
            }
            System.out.println("--------------------------");
        }
    }

    public static void main(String[] args) {
        TableEditDemo frame = new TableEditDemo();
        frame.pack();
        frame.setVisible(true);
    }
}
