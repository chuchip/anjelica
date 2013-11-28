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
import java.io.*;

public class impExp extends ventana
{
  JFileChooser direleE;
  JFileChooser ficeleE;
  Vector tablas=new Vector();
  CPanel Pprinc = new CPanel();
  JScrollPane jScrollPane1 = new JScrollPane();
  CTextField nombficE = new CTextField();
  CLabel cLabel2 = new CLabel();
  CButton Bbusfic = new CButton(Iconos.getImageIcon("folder"));
  CButton Bimportar = new CButton();
  CButton Bexportar = new CButton();
  CLabel cLabel1 = new CLabel();
  JTextArea tablasE = new JTextArea();
  utilSql utSq = new utilSql()
  {
    protected void incMsg()
    {
      mensajeRapido("Tratadas: "+utSq.nLin+ " de Tabla: "+utSq.tabla);
    }

  };
  CTextField nombficE1 = new CTextField();
  CLabel Directorio = new CLabel();
  CButton Bbusdir = new CButton(Iconos.getImageIcon("folder"));
  CButton Bcarga = new CButton();
  CCheckBox opBorrar = new CCheckBox();

  public impExp(EntornoUsuario eu, Principal p)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    setTitulo("Importar y Exportar Base de datos " + EU.catalog + ")");

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

  public impExp(gnu.chu.anjelica.menu p, EntornoUsuario eu)
  {

    EU = eu;
    vl = p.getLayeredPane();
    setTitulo("Importar y Exportar Base de datos " + EU.catalog + ")");
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
    this.setSize(new Dimension(562, 322));
    statusBar = new StatusBar(this);
    conecta();

    opBorrar.setText("Borrar Datos Antiguos");
    opBorrar.setBounds(new Rectangle(290, 198, 168, 16));
    this.add(statusBar,BorderLayout.SOUTH);
    nombficE1.setBounds(new Rectangle(66, 32, 346, 18));
    nombficE1.setText("");
    Directorio.setBounds(new Rectangle(5, 32, 62, 17));
    Directorio.setText("Directorio");
    Bbusdir.setMargin(new Insets(0, 0, 0, 0));
    Bbusdir.setBounds(new Rectangle(419, 33, 22, 23));
    Bcarga.setBounds(new Rectangle(127, 129, 121, 35));
    Bcarga.setText("Carga Tablas");
    this.getContentPane().add(Pprinc,  BorderLayout.CENTER);
    Pprinc.setLayout(null);
    nombficE.setText("");
    nombficE.setBounds(new Rectangle(52, 5, 361, 18));
    cLabel2.setText("Fichero");
    cLabel2.setBounds(new Rectangle(6, 5, 49, 17));
    Bbusfic.setBounds(new Rectangle(419, 5, 22, 23));
    Bbusfic.setMargin(new Insets(0, 0, 0, 0));
    Bimportar.setBounds(new Rectangle(298, 156, 144, 34));
    Bimportar.setText("Importar");
    Bexportar.setBounds(new Rectangle(295, 96, 144, 34));
    Bexportar.setText("Exportar");
    cLabel1.setBackground(Color.red);
    cLabel1.setForeground(Color.white);
    cLabel1.setOpaque(true);
    cLabel1.setHorizontalAlignment(SwingConstants.CENTER);
    cLabel1.setText("Tablas");
    cLabel1.setBounds(new Rectangle(5, 67, 120, 17));
    jScrollPane1.setBounds(new Rectangle(5, 86, 119, 151));
    Pprinc.add(cLabel2, null);
    Pprinc.add(nombficE, null);
    Pprinc.add(Bbusfic, null);
    Pprinc.add(cLabel1, null);
    Pprinc.add(jScrollPane1, null);
    Pprinc.add(Directorio, null);
    Pprinc.add(nombficE1, null);
    Pprinc.add(Bbusdir, null);
    Pprinc.add(Bexportar, null);
    Pprinc.add(Bimportar, null);
    Pprinc.add(Bcarga, null);
    Pprinc.add(opBorrar, null);
    jScrollPane1.getViewport().add(tablasE, null);
  }
  public void iniciarVentana() throws Exception
  {
    stUp= dtCon1.getConexion().createStatement();
    utSq.setAnadir(false);
    utSq.nMsg=200;
    Bcarga.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        try {
          cargaTablas(nombficE.getText());
        } catch (Exception k)
        {
          Error("Error al Cargar Tablas",k);
        }
      }
    });

    Bbusfic.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Bbusfic_actionPerformed();
      }
    });
    Bbusdir.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Bbusdir_actionPerformed();
      }
    });
    Bexportar.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        exportar();
      }
    });
    Bimportar.addActionListener(new ActionListener()
       {
         public void actionPerformed(ActionEvent e)
         {
           importar();
         }
       });

  }
  void cargaTablas(String fichero) throws Exception
  {

    FileReader fr = new FileReader(fichero);
    BufferedReader bfr = new BufferedReader(fr);
    String tablasS = "";
    int nLin = 0;
    String linea;

    tablasE.setText("");
    while ( (linea = bfr.readLine()) != null)
    {
      tablasS+=linea+"\n";
      nLin++;
    }
    tablasE.setText(tablasS);
    mensajeErr("Cargadas: "+nLin+" Tablas");
  }
  boolean cargaTablas()
  {
    String linea;
    tablas.removeAllElements();
    StringTokenizer st=new StringTokenizer(tablasE.getText(),""+'\n',false);
    while (st.hasMoreTokens())
    {
      linea=st.nextToken();
      linea=linea.trim();
      if (!linea.equals(""))
        tablas.add(linea);
    }
    if (tablas.size()==0)
    {
      mensajeErr("Introduzca alguna Tabla desde donde importar/Exportar");
      return false;
    }
    return true;
  }
  void exportar()
  {
    if (!cargaTablas())
      return;
    new miThread("")
    {
      public void run()
      {
        try
        {
          Thread.currentThread().setPriority(Thread.MAX_PRIORITY-1);

          int nTablas = tablas.size();
          String sql;
          String tabla="";
          for (int n = 0; n < nTablas; n++)
          {
            tabla = tablas.elementAt(n).toString();
            sql = "select * from " + tabla;

            utSq.unload(nombficE1.getText() + "/" + tabla + ".unl", dtCon1, sql,"dd-MM-yyyy");
            if (utSq.excep != null)
              throw utSq.excep;

            mensaje("UNLOAD. Guardadas " + utSq.nLin + " Lineas de Tabla: " + tabla,false);
          }
          mensajeErr("Guradados todos los datos");
          mensaje("");
        }
        catch (Throwable k)
        {
          Error("Error al Exportar Tablas", k);
        }
      }
    };


  }

  void importar()
  {
    if (!cargaTablas())
      return;
    new miThread("")
    {
      public void run()
      {
        try
        {
          Thread.currentThread().setPriority(Thread.MAX_PRIORITY-1);
          int nTablas = tablas.size();
          String sql;
          String tabla = "";
          for (int n = 0; n < nTablas; n++)
          {
            tabla = tablas.elementAt(n).toString();
            if (opBorrar.isSelected())
            {
              sql="delete from "+tabla;
              dtCon1.executeUpdate(sql,stUp);
            }
            utSq.load(nombficE1.getText() + "/" + tabla + ".unl",tabla, dtCon1,
                      stUp,"dd-MM-yyyy");
            if (utSq.excep != null)
              throw utSq.excep;
            dtCon1.commit();
            mensaje("UNLOAD. Cargadas " + utSq.nLin + " Lineas de Tabla: " + tabla,false);
          }
          mensajeErr("Cargados todos los datos");
          mensaje("");

        }
        catch (Throwable k)
        {
          Error("Error al Exportar Tablas", k);
        }
      }
    };
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
  void Bbusdir_actionPerformed() {
   try
   {
     configurarDirectorio();
     int returnVal = direleE.showOpenDialog(this);
     if(returnVal == JFileChooser.APPROVE_OPTION)
     {
        nombficE1.setText(direleE.getSelectedFile().getAbsolutePath());
     }

   }
   catch (Exception k)
   {
     fatalError("error al elegir el Directorio", k);
   }

 }

  void configurarFile() throws Exception
  {
      if (ficeleE != null)
        return;
      ficeleE = new JFileChooser();
      ficeleE.setName("Abrir Fichero");
      ficeleE.setCurrentDirectory(new java.io.File("c:/"));
  }
  void configurarDirectorio() throws Exception
  {
      if (direleE != null)
        return;
      direleE = new JFileChooser();
      direleE.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      direleE.setName("Directorio donde guardar/cargar");
      direleE.setCurrentDirectory(new java.io.File("c:/"));
  }

}
