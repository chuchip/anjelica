package gnu.chu.isql;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import gnu.chu.controles.*;
import javax.swing.table.*;
import java.util.*;
import gnu.chu.utilidades.*;
import java.awt.event.*;
public class elecBD extends JInternalFrame
{

  private boolean DEBUG = true;
 String[] data = {"one", "two", "three", "four"};
  DefaultTableModel myModel;
  JList bdE = new JList(data);
  JLabel jLabel1 = new JLabel();
  JPanel jPanel1 = new JPanel();
  JLabel jLabel2 = new JLabel();
  JTextField bdnombE = new JTextField();
  JPanel jPanel2 = new JPanel();
  JLabel jLabel3 = new JLabel();
  JTextField bdclaseE = new JTextField();
  JLabel jLabel4 = new JLabel();
  JTextField bdurlE = new JTextField();
  JLabel jLabel5 = new JLabel();
  JTextField bdusuaE = new JTextField();
  JCheckBox bdincusuE = new JCheckBox();
  JTable jt;
  JScrollPane jScrollPane1;

  JLabel jLabel6 = new JLabel();
  CButton BinsLinea = new CButton();
  CButton BdelLinea = new CButton();
  CButton Bacepta = new CButton();
  CButton Bcancela = new CButton();
  public elecBD() throws Exception {
    jbInit();
  }

  private void jbInit() throws Exception
  {
    this.setSize(new Dimension(464, 308));
    this.setResizable(true);
    this.setIconifiable(true);
    this.setClosable(true);
    this.setTitle("Elegir Base de Datos");

    Vector v = new Vector();
    myModel = new DefaultTableModel();
    Vector vc = new Vector();
    vc.addElement("Propiedad");
    vc.addElement("Valor");


    jt = new JTable(myModel);
    jScrollPane1 = new JScrollPane(jt);
    jLabel1.setBackground(Color.green);
    jLabel1.setForeground(Color.blue);
    jLabel1.setOpaque(true);
    jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel1.setText("Conexion");
    jPanel1.setLayout(new GridBagLayout());
    jLabel2.setText("Nombre");
    jLabel2.setBounds(new Rectangle(3, 7, 54, 20));
    bdnombE.setText("");
    bdnombE.setBounds(new Rectangle(59, 7, 198, 20));
    jPanel2.setBorder(BorderFactory.createLoweredBevelBorder());
    jPanel2.setDebugGraphicsOptions(0);
    jPanel2.setMinimumSize(new Dimension(350, 100));
    jPanel2.setOpaque(true);
    jPanel2.setPreferredSize(new Dimension(350, 100));
    jPanel2.setRequestFocusEnabled(true);
    jPanel2.setLayout(null);
    jLabel3.setToolTipText("");
    jLabel3.setVerifyInputWhenFocusTarget(true);
    jLabel3.setText("Clase");
    jLabel3.setBounds(new Rectangle(3, 33, 50, 19));
    bdclaseE.setRequestFocusEnabled(true);
    bdclaseE.setText("");
    bdclaseE.setBounds(new Rectangle(59, 29, 198, 21));
    jLabel4.setText("URL");
    jLabel4.setBounds(new Rectangle(3, 60, 24, 16));
    bdurlE.setText("");
    bdurlE.setBounds(new Rectangle(59, 58, 247, 20));
    jLabel5.setText("Usuario");
    jLabel5.setBounds(new Rectangle(3, 85, 50, 19));
    bdusuaE.setText("");
    bdusuaE.setBounds(new Rectangle(59, 80, 140, 21));
    bdincusuE.setToolTipText("");
    bdincusuE.setText("Incluir Usuario/Password en URL");
    bdincusuE.setBounds(new Rectangle(3, 104, 230, 24));

    jLabel6.setBackground(Color.blue);
    jLabel6.setForeground(Color.cyan);
    jLabel6.setOpaque(true);
    jLabel6.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel6.setHorizontalTextPosition(SwingConstants.CENTER);
    jLabel6.setText("Anadir Propiedades");
    jLabel6.setVerticalAlignment(SwingConstants.BOTTOM);
    jLabel6.setBounds(new Rectangle(49, 121, 141, 21));

    jScrollPane1.setPreferredSize(new Dimension(453, 403));
    jScrollPane1.setBounds(new Rectangle(3, 144, 226, 83));
    bdE.setMaximumSize(new Dimension(34, 72));
    BinsLinea.setBounds(new Rectangle(6, 229, 107, 19));
    BinsLinea.setMargin(new Insets(0, 0, 0, 0));
    BinsLinea.setText("Insertar");
    BinsLinea.addActionListener(new elecBD_BinsLinea_actionAdapter(this));
    BdelLinea.setBounds(new Rectangle(122, 228, 107, 22));
    BdelLinea.setMargin(new Insets(0, 0, 0, 0));
    BdelLinea.setSelected(false);
    BdelLinea.setText("Borrar");
    BdelLinea.addActionListener(new elecBD_BdelLinea_actionAdapter(this));
    Bacepta.setBounds(new Rectangle(230, 145, 88, 30));
    Bacepta.setToolTipText("Aceptar datos de Conexion");
    Bacepta.setVerifyInputWhenFocusTarget(true);
    Bacepta.setMargin(new Insets(0, 0, 0, 0));
    Bacepta.setMnemonic('A');
    Bacepta.setText("Aceptar");
    Bacepta.addActionListener(new elecBD_Bacepta_actionAdapter(this));
    Bcancela.setText("Cancelar");
    Bcancela.addActionListener(new elecBD_Bcancela_actionAdapter(this));
    Bcancela.setBounds(new Rectangle(229, 185, 88, 30));
    Bcancela.setToolTipText("Rechazar datos de Conexion");
    Bcancela.setVerifyInputWhenFocusTarget(true);
    Bcancela.setIcon(null);
    Bcancela.setMargin(new Insets(0, 0, 0, 0));
    Bcancela.setMnemonic('C');
    this.getContentPane().add(jPanel1, BorderLayout.CENTER);
    jPanel1.add(bdE,   new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 110, 164));
    jPanel1.add(jLabel1,     new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 0, 2, 0), 47, 1));
    jPanel1.add(jPanel2,     new GridBagConstraints(1, 0, 1, 2, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(4, 0, 5, 3), 0, 0));
    jPanel2.add(bdnombE, null);
    jPanel2.add(jLabel2, null);
    jPanel2.add(bdclaseE, null);
    jPanel2.add(jLabel4, null);
    jPanel2.add(jLabel3, null);
    jPanel2.add(bdurlE, null);
    jPanel2.add(jLabel5, null);
    jPanel2.add(bdusuaE, null);
    jPanel2.add(bdincusuE, null);
    jPanel2.add(jScrollPane1, null);
    jPanel2.add(jLabel6, null);
    jPanel2.add(BinsLinea, null);
    jPanel2.add(BdelLinea, null);
    jPanel2.add(Bacepta, null);
    jPanel2.add(Bcancela, null);
  }

  public void iniciar() throws Throwable {
    jt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    myModel.setColumnIdentifiers(new Object[]{"Valor","Dato"});
    myModel.addRow(new Object[]{"",""});

     bdnombE.requestFocus();
  }

  void BinsLinea_actionPerformed(ActionEvent e) {
    myModel.addRow(new Object[]{"",""});

    jt.setColumnSelectionInterval(1,1);
    jt.setRowSelectionInterval(1,1);
    jt.requestFocus();
  }

  void BdelLinea_actionPerformed(ActionEvent e) {

  }

  void Bacepta_actionPerformed(ActionEvent e) {

  }

  void Bcancela_actionPerformed(ActionEvent e) {

  }


}

class elecBD_BinsLinea_actionAdapter implements java.awt.event.ActionListener {
  elecBD adaptee;

  elecBD_BinsLinea_actionAdapter(elecBD adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.BinsLinea_actionPerformed(e);
  }
}

class elecBD_BdelLinea_actionAdapter implements java.awt.event.ActionListener {
  elecBD adaptee;

  elecBD_BdelLinea_actionAdapter(elecBD adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.BdelLinea_actionPerformed(e);
  }
}

class elecBD_Bacepta_actionAdapter implements java.awt.event.ActionListener {
  elecBD adaptee;

  elecBD_Bacepta_actionAdapter(elecBD adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.Bacepta_actionPerformed(e);
  }
}

class elecBD_Bcancela_actionAdapter implements java.awt.event.ActionListener {
  elecBD adaptee;

  elecBD_Bcancela_actionAdapter(elecBD adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.Bcancela_actionPerformed(e);
  }
}
