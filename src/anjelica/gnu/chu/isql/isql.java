package gnu.chu.isql;

import javax.swing.*;
import java.util.*;
import java.sql.*;
import java.awt.*;
import gnu.chu.sql.*;
import gnu.chu.controles.*;
import gnu.chu.sql.*;
import gnu.chu.Menu.*;
import gnu.chu.utilidades.*;
import java.awt.event.*;

public class isql extends ventana
{
  conexion ctCom;
  static final int CARGA=0;
  static final int DESCARGA=1;
  JFileChooser ficeleE;
  CPanel Pprinc = new CPanel();
  CLabel cLabel1 = new CLabel();
  CButton Bload = new CButton("Load",Iconos.getImageIcon("load"));
  JScrollPane jScrollPane1 = new JScrollPane();
  JTextArea msgE = new JTextArea();
  utilSql utSq = new utilSql()
  {
    protected void incMsg()
    {
      msgE.append("Insertadas .. " + utSq.nLin+"\n");
    }
  };
  CTextField nombficE = new CTextField();
  CButton Bbusfic = new CButton(Iconos.getImageIcon("folder"));
  CLabel cLabel2 = new CLabel();
  CButton Bunload = new CButton("Unload",Iconos.getImageIcon("unload"));
  CButton Bcommit = new CButton("Commit",Iconos.getImageIcon("run"));
  CButton Brollback = new CButton("Rollback",Iconos.getImageIcon("cancel"));
  JScrollPane jScrollPane2 = new JScrollPane();
  JTextArea tablaE = new JTextArea();
  CCheckBox opBorrar = new CCheckBox();

  public isql (EntornoUsuario eu, Principal p)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    setTitulo("ISQL (Catalogo: "+EU.catalog+")");

    try
    {
      if (jf.gestor.apuntar(this))
        jbInit();
      else
        setErrorInit(true);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      setErrorInit(true);
    }
  }

  public isql(gnu.chu.anjelica.menu p, EntornoUsuario eu)
  {

    EU = eu;
    vl = p.getLayeredPane();
    setTitulo("ISQL (Catalogo: "+EU.catalog+")");
    eje = false;

    try
    {
      jbInit();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      setErrorInit(true);
    }
  }

  private void jbInit() throws Exception
  {
    iniciarFrame();
    this.setSize(new Dimension( 449, 322));
    statusBar = new StatusBar(this);
    conecta();
    cLabel1.setText("Sentencia/Tabla");
    cLabel1.setBounds(new Rectangle(5, 3, 109, 14));
    Pprinc.setLayout(null);
    Bload.setBounds(new Rectangle(335, 141, 99, 28));
    jScrollPane1.setBounds(new Rectangle(5, 140, 324, 135));
    nombficE.setText("/home/cpuente/cliente.txt");
    nombficE.setBounds(new Rectangle(45, 114, 361, 18));
    Bbusfic.setBounds(new Rectangle(407, 113, 22, 23));
    Bbusfic.setMargin(new Insets(0, 0, 0, 0));
    cLabel2.setText("Fichero");
    cLabel2.setBounds(new Rectangle(5, 116, 49, 17));
    Bunload.setBounds(new Rectangle(335, 171, 99, 28));
    Bunload.setMargin(new Insets(0, 0, 0, 0));
    Bcommit.setBounds(new Rectangle(342, 218, 90, 23));
    Bcommit.setMargin(new Insets(0, 0, 0, 0));
    Brollback.setBounds(new Rectangle(341, 250, 90, 23));
    Brollback.setMargin(new Insets(0, 0, 0, 0));
    jScrollPane2.setBounds(new Rectangle(5, 20, 424, 84));
    opBorrar.setText("Borrar Datos Antiguos");
    opBorrar.setBounds(new Rectangle(115, 0, 160, 16));
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);

    Pprinc.add(jScrollPane1, null);
    Pprinc.add(Bcommit, null);
    Pprinc.add(Brollback, null);
    Pprinc.add(Bload, null);
    Pprinc.add(Bunload, null);
    Pprinc.add(nombficE, null);
    Pprinc.add(cLabel2, null);
    Pprinc.add(Bbusfic, null);
    Pprinc.add(jScrollPane2, null);
    Pprinc.add(cLabel1, null);
    Pprinc.add(opBorrar, null);
    jScrollPane2.getViewport().add(tablaE, null);
    jScrollPane1.getViewport().add(msgE, null);
 }

 public void iniciarVentana() throws Exception
 {
   ctCom = new conexion(EU.usuario, EU.password,
                    EU.driverDB,
                    EU.addressDB);
   ctCom.setAutoCommit(false);
   ctCom.setCatalog(EU.catalog);

   stUp=ctCom.createStatement();

   utSq.nMsg=200;
   activarEventos();
   tablaE.requestFocus();
 }
 void activarEventos()
 {
   Bload.addActionListener(new ActionListener() {
     public void actionPerformed(ActionEvent e) {
       Bload_actionPerformed();
     }
   });
   Bunload.addActionListener(new ActionListener() {
     public void actionPerformed(ActionEvent e) {
       Bunload_actionPerformed();
     }
   });
   Bcommit.addActionListener(new ActionListener() {
     public void actionPerformed(ActionEvent e) {
       try
       {
         ctCom.commit();
         mensajeErr("Commit Realizado");
       } catch (SQLException k)
       {
         Error("Error al realizar commit",k);
       }
     }
   });
   Brollback.addActionListener(new ActionListener() {
     public void actionPerformed(ActionEvent e) {
       try
       {
         ctCom.rollback();
         mensajeErr("Realizado ... RollBack");
       } catch (SQLException k)
       {
         Error("Error al realizar Rollback",k);
       }
     }
   });

   Bbusfic.addActionListener(new java.awt.event.ActionListener()
   {
     public void actionPerformed(ActionEvent e)
     {
       Bbusfic_actionPerformed();
     }
   });

 }
 void  Bload_actionPerformed()
 {
   new ThreadSql(this,CARGA);
 }
 public void carga()
 {

  try
  {
    String sql;
    msgE.setText("");
    if (opBorrar.isSelected())
    {
      if (mensajes.mensajePreguntar("Borrar TODOS los Registros")!=mensajes.YES)
      {
        mensajeErr("Load ... ABORTADO");
        return;
      }
      sql = "DELETE FROM " + tablaE.getText();
      int n=stUp.executeUpdate(sql);
      msgE.setText("Borrados " + n + " Registros\n");
    }

    utSq.load(nombficE.getText(),tablaE.getText(), dtCon1,stUp,"dd-MM-yyyy");
    if (utSq.excep!=null)
      throw utSq.excep;
    msgBox("LOAD. Insertadas " + utSq.nLin + " Lineas");
  }
  catch (Throwable ex)
  {
    Error("Error al Cargar fichero. Nlinea: "+utSq.nLin ,ex);
  }


 }
 void Bbusfic_actionPerformed() {
   try
   {
     configurarFile();
     int returnVal = ficeleE.showOpenDialog(this);
     if(returnVal == JFileChooser.APPROVE_OPTION)
     {
        nombficE.setText(ficeleE.getSelectedFile().getAbsolutePath());
     }

   }
   catch (Exception k)
   {
     fatalError("error al elegir el fichero", k);
   }

 }

 void Bunload_actionPerformed()
 {
   new ThreadSql(this,DESCARGA);
 }

 void descarga()
 {
   try
   {
     msgE.setText("");
     String sql=tablaE.getText().toUpperCase();
     if (sql.indexOf("SELECT")==-1)
       sql="select * from " + tablaE.getText();

     utSq.unload(nombficE.getText(), dtCon1,sql);
     if (utSq.excep != null)
       throw utSq.excep;
     msgBox("UNLOAD. Guardadas " + utSq.nLin + " Lineas");

   }
   catch (Throwable ex)
   {
     Error("Error al Cargar fichero. Linea: "+utSq.nLin, ex);
   }
 }
 public void matar(boolean cerrarConexion)
 {
   super.matar(cerrarConexion);
   try
   {
     if (cerrarConexion)
     {
       ctCom.rollback();
       if (ctCom != null)
         ctCom.close();
     }
   }
   catch (Exception k)
   {
     k.printStackTrace();
   }

 }
 void configurarFile() throws Exception
 {
     if (ficeleE != null)
       return;
     ficeleE = new JFileChooser();
     ficeleE.setName("Abrir Fichero");
     ficeleE.setCurrentDirectory(new java.io.File("d:/"));
 }
}
class ThreadSql extends Thread {
  isql sql;
  int ejec;
      public ThreadSql(isql sql,int ejecuta) {
        this.sql=sql;
        this.ejec=ejecuta;
             start();
      }
      public void run() {
        if (ejec==isql.CARGA)
          sql.carga();
        if (ejec==isql.DESCARGA)
          sql.descarga();
      }
}
