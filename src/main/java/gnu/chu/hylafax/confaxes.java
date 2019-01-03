package gnu.chu.hylafax;

import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import gnu.chu.Menu.*;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.*;
import java.awt.event.*;

import gnu.hylafax.*;
import gnu.chu.camposdb.*;
import gnu.inet.ftp.ServerResponseException;
import java.io.IOException;
/**
 *
 * <p>Título:  confaxes</p>
 * <p>Descripcion: Consulta de documentos enviados por Fax
 *   Tambien permite anular el envio de los faxes en curso</p>
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
 * <p>Empresa: miSL</p>
 * @author chuchi P
 * @version 1.0
 */
public class confaxes extends ventana
{
  HylaFAXClient clientHylaFax;
  int p1,p2;
  CPanel Pprinc = new CPanel();
  CPanel Pcabe = new CPanel();
  Cgrid jt = new Cgrid(15);
  CTextField feciniE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel1 = new CLabel();
  CLabel cLabel2 = new CLabel();
  CTextField fecfinE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CButton Baceptar = new CButton("Aceptar F4",Iconos.getImageIcon("check"));
  CLabel cLabel3 = new CLabel();
  cliPanel cli_codiE = new cliPanel();
  CLabel cLabel4 = new CLabel();
  CComboBox avf_estadE = new CComboBox();
  CButton BanuFax = new CButton("Anular Fax");
  boolean admin=false;
  CLabel cLabel5 = new CLabel();
  CComboBox usu_nombE = new CComboBox();
 
  CPanel Presum = new CPanel();
  CLabel cLabel6 = new CLabel();
  CTextField nFaxE = new CTextField(Types.DECIMAL,"##,##9");
  CLabel cLabel7 = new CLabel();
  CTextField correcE = new CTextField(Types.DECIMAL,"##,##9");
  CTextField errorE = new CTextField(Types.DECIMAL,"##,##9");
  CLabel cLabel8 = new CLabel();
  CTextField otrosE = new CTextField(Types.DECIMAL,"##,##9");
  CLabel cLabel9 = new CLabel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();

  public confaxes(EntornoUsuario eu, Principal p)
  {
    this(eu,p,null);
  }
  public confaxes(EntornoUsuario eu, Principal p,Hashtable ht)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    setTitulo("Consulta Faxes Enviados");

    try
    {
      if (ht != null)
      {
        if (ht.get("admin") != null)
          admin = Boolean.valueOf(ht.get("admin").toString()).
              booleanValue();
      }
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

  public confaxes(gnu.chu.anjelica.menu p, EntornoUsuario eu)
  {

    EU = eu;
    vl = p.getLayeredPane();
    setTitulo("Consulta Faxes Enviados");
    eje = false;
    admin =true;
    try
    {
      jbInit();
    }
    catch (Exception e)
    {
      setErrorInit(true);
    }
  }
  /**
   * Actualiza los datos del fax en la tabla albvefax, conectandose al servidor del fax.
   * Solo actualiza los datos del Usuario en usu_nombE
   * @throws java.sql.SQLException
   * @throws gnu.inet.ftp.ServerResponseException
   * @throws java.io.IOException
   */
    private void actDatosFax() throws SQLException, ServerResponseException, IOException
    {
        if (SendFax.getServFax()==null)
            return;
        int job;
        clientHylaFax = new HylaFAXClient();
//       clientHylaFax.setDebug(false);	// enable debug messages
        clientHylaFax.open(SendFax.getServFax());
        if (clientHylaFax.user("anjelica")) {
            clientHylaFax.pass("anjelica");
        }
        clientHylaFax.tzone(HylaFAXClientProtocol.TZONE_GMT);
        clientHylaFax.jobfmt("%5j\t%a\t%o\t%.25s");
        Vector list = new Vector();
        list = clientHylaFax.getList("doneq");
        Enumeration lines = list.elements();
        String linea;
        String s;
        // Trato la cola de done (ya enviados)
        String usuario = "";
        String estado = "";
        String msgerr = "";
        while (lines.hasMoreElements()) {
            linea = lines.nextElement().toString();
            //         System.out.println("linea: "+linea);
            p2 = -1;
            try {
                job = Integer.parseInt(getValor(linea, "\t"));
            } catch (NumberFormatException k1) {
                continue;
            }
            estado = getValor(linea, "\t");
            usuario = getValor(linea, "\t");
            msgerr = getValor(linea, "\t");
//            if (!usuario.equals(usu_nombE.getValor())) {
//                continue;
//            }
//           Actualizo solo el estado de los faxes de este usuario.
            s = "SELECT * FROM albvefax WHERE avf_jobid= " + job+
                 " and usu_nomb='"+usu_nombE.getValor()+"'";
            if (!dtStat.select(s)) 
                continue;
            if (estado.equals(dtStat.getString("avf_estad"))) {
                continue; // No ha cambiado el estado
            }
            s = "UPDATE albvefax set avf_estad = '" +
                 estado + "',avf_msgerr='" + msgerr + "'" + " WHERE avf_jobid= " + job;
            stUp.executeUpdate(s);
        }
        // Trato la cola de pendientes. (sendq)
        clientHylaFax.jobfmt("%5j\t%o");
        list = clientHylaFax.getList("sendq");
//       list = c.getList("docq");
//       list = c.getList("recvq");
//       list = c.getList("sendq");
        lines = list.elements();
        while (lines.hasMoreElements()) {
            linea = lines.nextElement().toString();
            p2 = -1;
            try {
                job = Integer.parseInt(getValor(linea, "\t"));
            } catch (NumberFormatException k1) {
                continue;
            }
            usuario = getValor(linea, "\t");

//            if (!usuario.equals(usu_nombE.getValor())) {
//                continue;
//            }
            s = "SELECT * FROM albvefax WHERE avf_jobid= " + job+
                 " and usu_nomb='"+usu_nombE.getValor()+"'";
            if (!dtStat.select(s)) 
                continue;
            
            s = "UPDATE albvefax set avf_estad = 'S' " + " WHERE avf_jobid= " + job;
            stUp.executeUpdate(s);
        }
        clientHylaFax.quit();
        ctUp.commit();
    }

private void jbInit() throws Exception
{
  this.setSize(new Dimension(700, 401));
  iniciarFrame();
  this.setVersion("2009-07-01");


    statusBar = new StatusBar(this);
    conecta();
    Pprinc.setMaximumSize(new Dimension(32767, 32767));
    Pprinc.setOpaque(true);
    Pprinc.setInputVerifier(null);
    Pprinc.setLayout(gridBagLayout1);
    Pcabe.setBorder(BorderFactory.createRaisedBevelBorder());
    Pcabe.setMaximumSize(new Dimension(491, 56));
    Pcabe.setMinimumSize(new Dimension(491, 56));
    Pcabe.setPreferredSize(new Dimension(491, 56));
    Pcabe.setLayout(null);
    cLabel1.setText("De Fecha");
    cLabel1.setBounds(new Rectangle(8, 4, 55, 16));
    cLabel2.setText("A");
    cLabel2.setBounds(new Rectangle(155, 4, 19, 16));
    fecfinE.setHorizontalAlignment(SwingConstants.CENTER);
    fecfinE.setBounds(new Rectangle(178, 4, 77, 16));

    Vector v=new Vector();
    cLabel3.setText("Cliente");
    cLabel3.setBounds(new Rectangle(4, 26, 42, 17));
    cLabel4.setText("Ver");
    cLabel4.setBounds(new Rectangle(308, 4, 29, 16));
    jt.setMaximumSize(new Dimension(689, 284));
    jt.setMinimumSize(new Dimension(689, 284));
    jt.setPreferredSize(new Dimension(689, 284));
    Baceptar.setBounds(new Rectangle(356, 24, 128, 23));
    cli_codiE.setBounds(new Rectangle(45, 27, 299, 17));
    avf_estadE.setBounds(new Rectangle(340, 2, 143, 20));
    feciniE.setBounds(new Rectangle(67, 4, 77, 16));

    BanuFax.setBounds(new Rectangle(546, 26, 107, 22));
    BanuFax.setMaximumSize(new Dimension(116, 22));
    BanuFax.setMinimumSize(new Dimension(116, 22));
    BanuFax.setPreferredSize(new Dimension(116, 22));
    cLabel5.setText("Usuario");
    cLabel5.setBounds(new Rectangle(497, 3, 51, 16));
    usu_nombE.setBounds(new Rectangle(555, 4, 106, 18));
    Presum.setBorder(BorderFactory.createRaisedBevelBorder());
    Presum.setMaximumSize(new Dimension(472, 23));
    Presum.setMinimumSize(new Dimension(472, 23));
    Presum.setPreferredSize(new Dimension(472, 23));
    Presum.setLayout(null);
    cLabel6.setText("Nº Faxes");
    cLabel6.setBounds(new Rectangle(5, 2, 55, 17));
    nFaxE.setStrQuery("");
    nFaxE.setEnabled(false);
    nFaxE.setBounds(new Rectangle(64, 2, 53, 17));
    cLabel7.setText("Correctos");
    cLabel7.setBounds(new Rectangle(138, 2, 64, 17));
    correcE.setEnabled(false);
    correcE.setBounds(new Rectangle(203, 2, 53, 17));
    correcE.setStrQuery("");
    errorE.setStrQuery("");
    errorE.setEnabled(false);
    errorE.setBounds(new Rectangle(303, 3, 53, 17));
    cLabel8.setText("Error");
    cLabel8.setBounds(new Rectangle(269, 3, 36, 17));
    otrosE.setStrQuery("");
    otrosE.setEnabled(false);
    otrosE.setBounds(new Rectangle(407, 2, 53, 17));
    cLabel9.setText("Otros");
    cLabel9.setBounds(new Rectangle(363, 3, 42, 17));

    confGrid();
    if (!admin)
      usu_nombE.setEnabled(false);
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
    Pprinc.add(Pcabe,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 172, 0));
    Pcabe.add(feciniE, null);
    Pcabe.add(cLabel1, null);
    Pcabe.add(cLabel2, null);
    Pcabe.add(fecfinE, null);
    Pcabe.add(cLabel3, null);
    Pprinc.add(jt,  new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 1, 0, 0), 0, 0));
    Pprinc.add(Presum,   new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 1, 0), 0, 0));
    Presum.add(cLabel6, null);
    Presum.add(nFaxE, null);
    Presum.add(cLabel7, null);
    Presum.add(correcE, null);
    Pcabe.add(cli_codiE, null);
    Pcabe.add(Baceptar, null);
    Pcabe.add(avf_estadE, null);
    Pcabe.add(cLabel4, null);
    Pcabe.add(BanuFax, null);
    Pcabe.add(usu_nombE, null);
    Pcabe.add(cLabel5, null);
    Presum.add(cLabel8, null);
    Presum.add(errorE, null);
    Presum.add(cLabel9, null);
    Presum.add(otrosE, null);
    Pcabe.setButton(java.awt.event.KeyEvent.VK_F4,Baceptar);
  }

  private void confGrid()
  {
    Vector v = new Vector();
    v.add("Cliente"); // 0
    v.add("Nombre Cliente"); // 1
    v.add("Año"); // 2
    v.add("Emp"); // 3
    v.add("Ser"); // 4
    v.add("N.Doc"); // 5
    v.add("N.Fax"); // 6
    v.add("Id.Fax"); // 7
    v.add("Est."); // 8
    v.add("Fecha"); // 9
    v.add("Hora"); // 10
    v.add("Comentario"); // 11
    v.add("Fichero"); // 11
    v.add("Descr.Fich"); // 11
    v.add("Tipo"); // 12
    jt.setCabecera(v);
    jt.setAnchoColumna(new int[]
                       {48, 150, 39, 31, 19, 53, 98, 56, 43, 82, 42, 100, 80, 80,40});
    jt.setAlinearColumna(new int[]
                         {0, 0, 2, 2, 1, 2, 2, 2, 1, 1, 1, 0, 0, 0,1});
    jt.setAjustaPanel(true);
    jt.setFormatoColumna(10, "99.99");
  }

    @Override
  public void iniciarVentana() throws Exception
  {
    String s="SELECT usu_nomb FROM usuarios order by usu_nomb";
    dtStat.select(s);
    usu_nombE.addItem(dtStat);
    usu_nombE.setValor(EU.usuario);

    feciniE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
    fecfinE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
    cli_codiE.iniciar(dtStat,this,vl,EU);
    cli_codiE.setCeroIsNull(true);
    avf_estadE.addItem("Todos","T");
    avf_estadE.addItem("Enviando","S");
    avf_estadE.addItem("Error","F");
    avf_estadE.addItem("Cancelado","C");
    avf_estadE.addItem("OK","D");
    activarEventos();
  }


  void activarEventos()
  {
    Baceptar.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Baceptar_actionPerformed();
      }
    });
    BanuFax.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        BanuFax_actionPerformed();
      }
    });
  }
  /**
   * Anular Faxes
   */
  void BanuFax_actionPerformed()
  {
    if (jt.isVacio())
      return;
    if (! jt.getValString(8).equals("Enviando"))
      return;
    try
    {
      clientHylaFax=new HylaFAXClient();
//      clientHylaFax.setDebug(false); // enable debug messages

      clientHylaFax.open(SendFax.getServFax());

      if (clientHylaFax.user("anjelica"))
        clientHylaFax.pass("anjelica");
  
      clientHylaFax.tzone(HylaFAXClientProtocol.TZONE_GMT);
//      c.jkill(jt.getValInt(7));
      clientHylaFax.jsusp(jt.getValInt(7));
      clientHylaFax.delete(clientHylaFax.getJob(jt.getValInt(7)));
      clientHylaFax.quit();
//      c.finalize();
      String s="UPDATE albvefax set avf_estad = 'C',avf_msgerr='CANCELADO POR USUARIO'"+
          " WHERE avf_jobid= "+jt.getValInt(7);
      stUp.executeUpdate(s);
      ctUp.commit();
      mensajeErr("Envio de fax a: "+jt.getValString(1)+ " ... Cancelado");
    }
    catch (SQLException sqlk)
    {
      Error("Error al Cancelar Fax",sqlk);
    }
    catch (ServerResponseException x)
    {
      mensajes.mensajeAviso("El Servidor de Fax respondio: "+x.getMessage()+"\n EL FAX NO SE PUDO CANCELAR");
    }
    catch (IOException iok)
    {
       mensajes.mensajeAviso("Hubo un error al cancelar el Fax: "+iok.getMessage()+"\n EL FAX  NO SE PUDO CANCELAR");
    }
    Baceptar_actionPerformed();
  }
  void Baceptar_actionPerformed()
  {
//    Formatear.verAncGrid(jt);
    try
    {
      actDatosFax();

      jt.removeAllDatos();
      String s="select a.*,cli_nomb from albvefax as a,clientes c WHERE usu_nomb = '"+usu_nombE.getValor()+"'"+
          " and A.cli_codi = c.cli_codi "+
          (! feciniE.isNull()?" and avf_fecha >= to_date('"+feciniE.getText()+"','dd-MM-yyyy')":"")+
          (! fecfinE.isNull()?" and avf_fecha <= to_date('"+fecfinE.getText()+"','dd-MM-yyyy')":"")+
          (! cli_codiE.isNull()?" and a.cli_codi = "+cli_codiE.getValorInt():"")+
          (!avf_estadE.getValor().equals("T")?" and avf_estad = '"+avf_estadE.getValor()+"'":"")+
          " ORDER BY avf_fecha desc,avf_hora desc";
//      debug(s);
      nFaxE.setValorDec(0);
      errorE.setValorDec(0);
      correcE.setValorDec(0);
      otrosE.setValorDec(0);
      int nFax=0,error=0,correc=0,otros=0;
      if (!dtCon1.select(s))
      {
        mensajeErr("No encontrados Faxes para estas condiciones");
        return;
      }
      String estad;
      do
      {
        nFax++;
        estad=avf_estadE.getText(dtCon1.getString("avf_estad"));
        Vector v=new Vector();
        v.addElement(dtCon1.getString("cli_codi"));
        v.addElement(dtCon1.getString("cli_nomb"));
        v.addElement(dtCon1.getString("avc_ano"));
        v.addElement(dtCon1.getString("emp_codi"));
        v.addElement(dtCon1.getString("avc_serie"));
        v.addElement(dtCon1.getString("avc_nume"));
        v.addElement(dtCon1.getString("avf_numfax"));
        v.addElement(dtCon1.getString("avf_jobid"));
        v.addElement(estad==null?dtCon1.getString("avf_estad"):estad);
        if (dtCon1.getString("avf_estad").equals("F"))
          error++;
        else if (dtCon1.getString("avf_estad").equals("D"))
          correc++;
        else
          otros++;
        v.addElement(dtCon1.getFecha("avf_fecha","dd-MM-yyyy"));
        v.addElement(dtCon1.getString("avf_hora"));
        v.addElement(dtCon1.getString("avf_msgerr"));
        v.addElement(dtCon1.getString("avf_nomfic"));
        v.addElement(dtCon1.getString("avf_desfic"));
         v.addElement(dtCon1.getString("avf_tipdoc"));
        jt.addLinea(v);
      } while (dtCon1.next());
      nFaxE.setValorInt(nFax);
      correcE.setValorInt(correc);
      errorE.setValorInt(error);
      otrosE.setValorInt(otros);
      jt.requestFocusInicio();
      mensajeErr("Consulta .. realizada");
    }catch(Exception e){
      Error("Error al mostrar datos del Fax",e);
    }
  }

  String getValor(String linea,String separador)
  {
    p1 = p2 + 1;
    p2 = linea.indexOf(separador, p1);
    if (p2==-1)
    {
      p2=p1;
      return linea.substring(p1).trim();
    }
    return linea.substring(p1, p2).trim();
  }
}

