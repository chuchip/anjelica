package gnu.chu.hylafax;


import gnu.chu.utilidades.*;
import gnu.chu.controles.*;
import java.awt.*;
import gnu.chu.Menu.Principal;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;
import gnu.chu.anjelica.pad.*;
import javax.swing.*;
import java.io.*;
import gnu.hylafax.*;

/**
 * Envia Faxes a Clientes
 *
 * Tambien permite delimitar por proveedor y por albaran.
 * <p>Copyright: Copyright (c) 2005-2009
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los terminos de la Licencia Pública General de GNU seg�n es publicada por
 *  la Free Software Foundation, bien de la versión 2 de dicha Licencia
 *  o bien (según su elección) de cualquier versión posterior.
 *  Este programa se distribuye con la esperanza de que sea útil,ed
 *  pero SIN NINGUNA GARANTIA, incluso sin la garantía MERCANTIL implícita
 *  o sin garantizar la CONVENIENCIA PARA UN PROPOSITO PARTICULAR.
 *  Véase la Licencia Pública General de GNU para más detalles.
 *  Debería haber recibido una copia de la Licencia Pública General junto con este programa.
 *  Si no ha sido así, escriba a la Free Software Foundation, Inc.,
 *  en 675 Mass Ave, Cambridge, MA 02139, EEUU.
 * </p>
 * @author chuchiP
 *
 */

public class envfaxcli extends ventana
{
  JFileChooser ficeleE;
  CPanel Pprinc = new CPanel();
  condBusqCl Pcabe = new condBusqCl();
  CButton Bconsultar = new CButton("Aceptar",Iconos.getImageIcon("check"));
  CGridEditable jt = new CGridEditable(3);
  CPanel Pfichero = new CPanel();
  JLabel jLabel1 = new JLabel();
  CTextField nombficE = new CTextField();
  CButton Bbusfic = new CButton(Iconos.getImageIcon("folder"));
  CTextField cli_codiE=new CTextField(Types.DECIMAL,"####9");
  CTextField cli_nombE=new CTextField(Types.CHAR,"X",40);
  CTextField cli_faxE=new CTextField(Types.CHAR,"X",15);
  CCheckBox opMantDatos = new CCheckBox();
  CButton Bsend = new CButton("Enviar Fax",Iconos.getImageIcon("quickprint"));
  String servFax;
  HylaFAXClient hfClient;
  FileInputStream inStr;
  CTextField avc_desficE=new CTextField(Types.CHAR,"X",50);

  JLabel jLabel2 = new JLabel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  public envfaxcli(EntornoUsuario eu, Principal p)
 {
   EU = eu;
   vl = p.panel1;
   jf = p;
   eje = true;

   setTitulo("Enviar Faxes a Clientes");

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

 public envfaxcli(gnu.chu.anjelica.menu p, EntornoUsuario eu)
 {

   EU = eu;
   vl = p.getLayeredPane();
   setTitulo("Enviar Faxes a Clientes");

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
   this.setSize(new Dimension(600, 530));
   this.setVersion("2006-05-06");
   conecta();
   Pprinc.setLayout(gridBagLayout1);
   statusBar = new StatusBar(this);
   Bconsultar.setText("Insertar F4");
   opMantDatos.setHorizontalAlignment(SwingConstants.CENTER);
   opMantDatos.setHorizontalTextPosition(SwingConstants.LEFT);
    opMantDatos.setText("Mantener datos");
   opMantDatos.setBounds(new Rectangle(429, 164, 134, 20));
   Bsend.setBounds(new Rectangle(396, 8, 178, 27));

    Pcabe.setMaximumSize(new Dimension(583, 227));
    Pcabe.setMinimumSize(new Dimension(583, 227));
    Pcabe.setPreferredSize(new Dimension(583, 227));
    avc_desficE.setBounds(new Rectangle(59, 23, 330, 17));
    jLabel2.setBounds(new Rectangle(6, 23, 52, 17));
    jLabel2.setToolTipText("Descripci�n");
    jLabel2.setText("Descrip.");
    Pfichero.setMaximumSize(new Dimension(581, 46));
    Pfichero.setMinimumSize(new Dimension(581, 46));
    Pfichero.setPreferredSize(new Dimension(581, 46));
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);
   Pcabe.setBorder(BorderFactory.createRaisedBevelBorder());
   Bconsultar.setBounds(new Rectangle(431, 187, 137, 30));

   Pfichero.setBorder(BorderFactory.createRaisedBevelBorder());
   Pfichero.setLayout(null);
   jLabel1.setText("Fichero");
   jLabel1.setBounds(new Rectangle(5, 4, 52, 17));
   nombficE.setBounds(new Rectangle(58, 4, 297, 17));
   Bbusfic.setBounds(new Rectangle(359, 0, 22, 23));
   Bbusfic.setMargin(new Insets(0, 0, 0, 0));

   this.getContentPane().add(Pprinc, BorderLayout.CENTER);
   Pprinc.add(Pcabe,     new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
   Pcabe.add(Bconsultar, null);
   Pcabe.add(opMantDatos, null);
   Pprinc.add(jt,   new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
   Pprinc.add(Pfichero,   new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    Pfichero.add(nombficE, null);
    Pfichero.add(jLabel1, null);
    Pfichero.add(avc_desficE, null);
    Pfichero.add(jLabel2, null);
    Pfichero.add(Bbusfic, null);
    Pfichero.add(Bsend, null);
   confGrid();

 }
 void confGrid()  throws Exception
  {
    Vector v=new Vector();
    v.addElement("Cliente");
    v.addElement("Nombre");
    v.addElement("N� Fax");
    jt.setCabecera(v);
    jt.setAlinearColumna(new int[]{0,0,0});
    jt.setAnchoColumna(new int[]{50,180,90});
    jt.setMaximumSize(new Dimension(582, 211));
    jt.setMinimumSize(new Dimension(582, 211));
    jt.setPreferredSize(new Dimension(582, 211));
    jt.setAjustarColumnas(true);
    cli_codiE.setEnabled(false);
    cli_nombE.setEnabled(false);
    Vector v1=new Vector();
    v1.add(cli_codiE);
    v1.add(cli_nombE);
    v1.add(cli_faxE);
    jt.setCampos(v1);
    jt.setCanInsertLinea(false);
  }
 public void iniciarVentana() throws Exception
 {
   Pcabe.iniciar(dtStat, this);
   activarEventos();
   Pcabe.resetTexto();
   opMantDatos.setSelected(true);
   Pcabe.setDefButton(Bconsultar);
   servFax=SendFax.getServFax();


 }

 void activarEventos()
 {
   Bbusfic.addActionListener(new java.awt.event.ActionListener()
   {
     public void actionPerformed(ActionEvent e)
     {
       Bbusfic_actionPerformed();
     }
   });
   Bconsultar.addActionListener(new ActionListener()
   {
     public void actionPerformed(ActionEvent e)
     {
         Bconsultar_actionPerformed();
     }
   });
   Bsend.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
             Bsend_actionPerformed();
           }
         });
 }
 void Bsend_actionPerformed()
 {
   if (nombficE.isNull())
   {
     mensajeErr("Introduzca el fichero a mandar");
     nombficE.requestFocus();
     return;
   }
   if (jt.isVacio())
   {
     mensajeErr("Introduzca al menos un cliente al que mandar el fax");
     jt.requestFocusInicio();
     return;
   }
   jt.procesaAllFoco();
   try {

     int nRow = jt.getRowCount();

     for (int n = 0; n < nRow; n++)
     {
       if (jt.getValString(n,2).trim().equals(""))
         continue;
       enviaFax(n);
     }
     mensajeErr("Faxes... en cola de envio");
   } catch (Exception k)
   {
     Error("Error al Mandar faxes",k);
   }
 }
 void enviaFax(int row) throws Exception
 {

  String killtime = "000259";
  int maxdials = 12;
  int maxtries = 3; // -t

  inStr = new FileInputStream(nombficE.getText());
  hfClient = new HylaFAXClient();
  hfClient.open(servFax);
//  hfClient.setDebug(EU.debug);

  if (hfClient.user(EU.usuario))
    hfClient.pass(EU.password);
  hfClient.noop(); // for the heck of it
  hfClient.tzone(HylaFAXClientProtocol.TZONE_LOCAL);
  Job job = hfClient.createJob(); // start a new job
  String remote_filename = hfClient.putTemporary(inStr);
  // set job properties
  job.setFromUser(EU.usuario);
  job.setKilltime(killtime);
  job.setMaximumDials(maxdials);
  job.setMaximumTries(maxtries);
  job.setDialstring(jt.getValString(row,2));
  job.setNotifyType(HylaFAXClientProtocol.NOTIFY_NONE);

  job.addDocument(remote_filename);
  hfClient.submit(job); // submit the job to the scheduler
  dtCon1.addNew("albvefax");
  dtCon1.setDato("cli_codi",jt.getValorInt(row,0));
  dtCon1.setDato("avc_ano",0);
  dtCon1.setDato("emp_codi",0);
  dtCon1.setDato("avc_serie","");
  dtCon1.setDato("avc_nume",0);
  dtCon1.setDato("usu_nomb",EU.usuario);
  dtCon1.setDato("avf_numfax",jt.getValString(row,2));
  dtCon1.setDato("avf_jobid",job.getId());
  dtCon1.setDato("avf_estad","-");
  dtCon1.setDato("avf_fecha",Formatear.getFechaAct("dd-MM-yyyy"),"dd-MM-yyyy");
  dtCon1.setDato("avf_hora",Formatear.getFechaAct("hh.mm"));
  dtCon1.setDato("avf_contro","S");
  dtCon1.setDato("avf_nomfic",Formatear.strCorta(new File(nombficE.getText()).getName(),50));
  dtCon1.setDato("avf_desfic",avc_desficE.getText());
  inStr.close();
  hfClient.quit();
  dtCon1.update();
  dtCon1.commit();
 }

 void Bconsultar_actionPerformed()
 {
   if (Pcabe.getErrorConf() != null)
     return;
   String sql = Pcabe.getStrSql();
   try
   {
     jt.setEnabled(false);
     if (!opMantDatos.isSelected())
       jt.removeAllDatos();
     debug(sql);
     if (!dtCon1.select(sql))
     {
       mensajeErr("NO encontrados CLIENTES para estos criterios");
       return;
     }
     Vector v = new Vector();
     do
     {
       v.removeAllElements();
       v.add(dtCon1.getString("cli_codi"));
       v.add(dtCon1.getString("cli_nomb"));
       v.add(dtCon1.getString("cli_fax"));
       jt.addLinea(v);
     }  while (dtCon1.next());
     jt.setEnabled(true);
     jt.requestFocusInicio();
   }
   catch (Exception k)
   {
     Error("Error al buscar clientes", k);
     return;
   }

 }
 void Bbusfic_actionPerformed()
 {
   try
   {
     configurarFile();
     int returnVal = ficeleE.showOpenDialog(this);
     if (returnVal == JFileChooser.APPROVE_OPTION)
     {
       nombficE.setText(ficeleE.getSelectedFile().getAbsolutePath());
     }
   }
   catch (Exception k)
   {
     fatalError("error al elegir el fichero", k);
   }

 }
 void configurarFile() throws Exception
  {
      if (ficeleE != null)
        return;
      ficeleE = new JFileChooser();
      ficeleE.setName("Abrir Fichero");

      ficeleE.setFileFilter(new filtroPDF());
      ficeleE.setCurrentDirectory(new java.io.File("c:/"));
  }
}

class filtroPDF    extends javax.swing.filechooser.FileFilter
{
  public boolean accept(File pathname)
  {
    if (pathname.isDirectory())
      return true;
    if (pathname.getAbsolutePath().endsWith(".pdf") || pathname.getAbsolutePath().endsWith(".ps"))
      return true;
    return false;
  }

  public  String getDescription()
  {
    return "PDF ó PS";
  }
}

