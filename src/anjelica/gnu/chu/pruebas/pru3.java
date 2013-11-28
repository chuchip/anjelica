package gnu.chu.pruebas;

import gnu.chu.controles.*;
import gnu.chu.Menu.*;
import gnu.chu.utilidades.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*; 
import java.util.*; 

public class pru3 extends ventana
{
  int contador=1;
  int nLin;
  CPanel cPanel1 = new CPanel();
  CButton cButton1 = new CButton();
  FileOutputStream fOut;
  CGridEditable jt=new CGridEditable(4);
  CCheckBox cButton2 = new CCheckBox("A esperar");

  public pru3(EntornoUsuario eu, Principal p) {
     EU=eu;
     vl=p.panel1;
     jf=p;
     eje=true;

     setTitulo("Pruebas 3");

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

  public pru3(gnu.chu.anjelica.menu p,EntornoUsuario eu) {

      EU=eu;
      vl=p.getLayeredPane();
      setTitulo("Pruebas 3");
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
    cPanel1.setLayout(null);
    iniciar(371, 249);
    cButton1.setBounds(new Rectangle(115, 3, 110, 32));
    cButton1.setText("Listar");
    statusBar = new StatusBar(this);

    cButton1.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        cButton1_actionPerformed(e);
      }
    });

    int nCol=4;
    Vector v=new Vector();
    for (int n=0;n<nCol;n++)
      v.add("Col-"+n);

    jt.setCabecera(v);

    int[] i=new int[nCol];
    for (int n=0;n<nCol;n++)
      i[n]=(n*10)+100;
    jt.setAnchoColumna(i);
    Vector vc=new Vector();
    CButton boton= new CButton("a");
    boton.setEnabled(false);
    cButton2.setBounds(new Rectangle(254, 6, 96, 30));
    cButton1.setEnabledParent(false);
    cButton2.setText("cButton2");
    cButton2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cButton2_actionPerformed(e);
      }
    });
    vc.add(boton);
    vc.add(new CTextField());
    vc.add(new CTextField());
    vc.add(new CTextField());
    jt.setCampos(vc);
//    jt.setAlinearColumna(new int[]{0,1,2});
//    jt.setAjustarGrid(true);
    for (int n=0;n<5;n++)
    {
      Vector v1 = new Vector();
      for (int c=0;c<nCol;c++)
      {
        v1.add("L:"+n+"C:"+c);
      }
      jt.addLinea(v1);
    }

    jt.setBounds(new Rectangle(11, 47, 340, 147));
    this.getContentPane().add(cPanel1, BorderLayout.CENTER);
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);

    cPanel1.add(cButton1, null);
    cPanel1.add(jt, null);
    cPanel1.add(cButton2, null);
    jt.setEnabled(true);
    popEspere_BCancelaraddActionListener(new ActionListener() {
                                           public void actionPerformed(ActionEvent e) {
                                              resetMsgEspere();
                                           }
                                         });
    }
    public void iniciarVentana() throws Exception
    {
      jt.requestFocusInicio();
    }

  void cButton1_actionPerformed(ActionEvent e)
  {
    try{

      }
      catch (Exception k2)
      {
        Error("Failed to generate report ", k2);
      }

  }

  void cButton2_actionPerformed(ActionEvent e) {


    new miThread("")
    {
      public void run()
      {
        contador=1;
        msgEspere("A esperar");

      }
    };
  }
  public void actualizaMsg()
  {
   actualizaMsg("\n ya vamos por el "+contador);
   this.setEnabled(true);
  }
}

