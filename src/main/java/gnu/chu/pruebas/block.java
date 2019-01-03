package gnu.chu.pruebas;

import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import gnu.chu.sql.*;
import java.awt.*;
import gnu.chu.Menu.*;
import javax.swing.*;
import java.awt.event.*;

public class block extends ventana
{
  CPanel cPanel1 = new CPanel();
  CButton Bblock = new CButton();
  CLabel cLabel1 = new CLabel();
  JTextArea selectE = new JTextArea();
  JScrollPane jScrollPane1 = new JScrollPane();
  public block(EntornoUsuario eu, Principal p) {
    EU=eu;
    vl=p.panel1;
    jf=p;
    eje=true;

    setTitulo("Bloqueo registro");

    try  {
      if(jf.gestor.apuntar(this))
          jbInit();
      else
        setErrorInit(true);
    }
    catch (Exception e) {
      e.printStackTrace();
      setErrorInit(true);
    }
  }

 public block(gnu.chu.anjelica.menu p,EntornoUsuario eu) {

     EU=eu;
     vl=p.getLayeredPane();
     setTitulo("Bloqueo registro");
     eje=false;

     try  {
       jbInit();
     }
     catch (Exception e) {
       e.printStackTrace();
       setErrorInit(true);
     }
   }

   private void jbInit() throws Exception
   {
     iniciar(483, 205);

    cPanel1.setLayout(null);
     statusBar=  new StatusBar(this);
    Bblock.setBounds(new Rectangle(18, 50, 112, 32));
    Bblock.setToolTipText("");
    Bblock.setSelectedIcon(null);
    Bblock.setText("Bloquea");
    Bblock.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Bblock_actionPerformed(e);
      }
    });
    cLabel1.setText("Select");
    cLabel1.setBounds(new Rectangle(126, 5, 47, 19));
    selectE.setText("select * from usuarios where usu_nomb='cpuente' with updlock ");
    jScrollPane1.setBounds(new Rectangle(173, 6, 289, 114));
    this.getContentPane().add(cPanel1, BorderLayout.CENTER);
    this.getContentPane().add(cPanel1, BorderLayout.CENTER);
    cPanel1.add(jScrollPane1, null);
    cPanel1.add(cLabel1, null);
    cPanel1.add(Bblock, null);
    jScrollPane1.getViewport().add(selectE, null);
    conecta();
   }

  void Bblock_actionPerformed(ActionEvent e) {

  }
}
