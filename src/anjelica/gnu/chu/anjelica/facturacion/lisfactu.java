package gnu.chu.anjelica.facturacion;

import gnu.chu.Menu.*;
import gnu.chu.anjelica.listados.Listados;
import gnu.chu.anjelica.menu;
import gnu.chu.anjelica.pad.MantPaises;
import gnu.chu.anjelica.pad.MantRepres;
import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.anjelica.pad.pdempresa;
import gnu.chu.anjelica.riesgos.*;
import gnu.chu.anjelica.ventas.*;
import gnu.chu.camposdb.*;
import gnu.chu.controles.*;
import gnu.chu.hylafax.SendFax;
import gnu.chu.interfaces.ejecutable;
import gnu.chu.mail.IFMail;
import gnu.chu.mail.MailHtml;
import gnu.chu.print.*;
import gnu.chu.sql.*;
import gnu.chu.utilidades.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import net.sf.jasperreports.engine.*;
/**
 *
 * <p>Titulo: lisfactu</p> 
 * <p>Descripción: Listado Facturas de Ventas</p>
 * <p>Copyright: Copyright (c) 2005-2016
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los terminos de la Licencia Pública General de GNU según es publicada por
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
 * @author chuchiP
 * @version 1.2 2009-03-01 Incluido Comentarios en cabecera y pie de factura (Modo texto)
 */
public class lisfactu extends ventana  implements JRDataSource
{
 
  IFMail ifMail=null;
  String subject;
  String emailCC;
  String asunto;
  int numLiComCab,numLiComPie,numLinFra;
  private boolean IMPFRATEXTO=false; // Permite imprimir facturas en modo texto? (tabla parametros)
  private boolean fraPreImp=false; // Usar listado fra. grafico Preimpresa
  private boolean simular=false; // Simula el listado (para opcion de enviar fax)
  private int numFrasTratar=0; // Numero de fras a tratar
  private final static int MAX_AVISO=150; // A partir de ahi avisa de q se imprimiran demasiadas fras.
  
  SendFax sendFax=null;
  int cliCodi,fvcAno,empCodi,fvcNume;
  String fvcSerie;
  boolean swFirst;
  String empObsfra;
//  ResultSet rs;
  int nFraImp;
  String msgError="";
  actCabAlbFra datCab;
  int nVtos;
  double impVto;
  double impVtoAc;
  String banNomb=null;
  proPanel pro_codiE=new proPanel();
  int nAlbImp;
  JasperPrint jp;
  int nLinALb;
  FileOutputStream fOut;
  Hashtable htCab;
  boolean swLineas=false;
  String s;
  CPanel Princ = new CPanel();
  CLabel cLabel6 = new CLabel();
  CLinkBox empIniE = new CLinkBox();
  CLabel cLabel3 = new CLabel();
  CLinkBox empFinE = new CLinkBox();
  CTextField fvc_numeE = new CTextField(Types.DECIMAL,"#####9");
  CLabel cLabel7 = new CLabel();
  CTextField eje_numeE = new CTextField(Types.DECIMAL,"###9");
  CTextField fvc_numeE1 = new CTextField(Types.DECIMAL,"#####9");
  CLabel cLabel8 = new CLabel();
  CTextField eje_numeE1 = new CTextField(Types.DECIMAL,"###9");
  CLabel cLabel1 = new CLabel();
  cliPanel cli_codiE = new cliPanel();
  CLabel rep_codiL = new CLabel("Representante");
  CLinkBox rep_codiE = new CLinkBox();
  CTextField feciniE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel9 = new CLabel();
  CLabel cLabel10 = new CLabel();
  CTextField fecfinE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CButtonMenu Baceptar = new CButtonMenu(Iconos.getImageIcon("print"));
  DatosTabla dt;
  DatosTabla dtFra;

  boolean valora=true;
  CPanel Pmargen = new CPanel();
  CLabel cLabel4 = new CLabel();
  CTextField margSupE = new CTextField(Types.DECIMAL,"##9");
  CLabel cLabel5 = new CLabel();
  CTextField margInfE = new CTextField(Types.DECIMAL,"##9");
  CLabel cLabel11 = new CLabel();
  CTextField nLiCabE = new CTextField(Types.DECIMAL,"##9");
  CLabel cLabel12 = new CLabel();
  CTextField margSupLE = new CTextField(Types.DECIMAL,"##9");
  CLabel cLabel13 = new CLabel();
  CTextField margIzqE = new CTextField(Types.DECIMAL,"##9");
  CButton BConsul = new CButton("Consultar",Iconos.getImageIcon("buscar"));
  Cgrid jt = new Cgrid(6);
  CPanel Pcond = new CPanel();
  CLabel cLabel14 = new CLabel();
  CTextField countFrasE = new CTextField(Types.DECIMAL,"###9");
  CLabel cLabel15 = new CLabel();
  CLabel cLabel21 = new CLabel();
  CComboBox cli_giroE = new CComboBox();
  CLinkBox cli_zoncreE = new CLinkBox();
  CPanel PcondCli = new CPanel();
  CLinkBox cli_zonrepE = new CLinkBox();
  CLabel cLabel16 = new CLabel();
  CLabel cLabel17 = new CLabel();
  CComboBox cli_activE = new CComboBox();
  TitledBorder titledBorder2;
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CCheckBox opAgrupa = new CCheckBox();
  private CTextField fvc_serieE = new CTextField(Types.CHAR,"X");
  private CTextField fvc_serie1E = new CTextField(Types.CHAR,"X");

    /**
     *
     * Usado para cuando se quiere solo imprimir.
     * @param eu EntornoUsuario
     * @param dtCon1 DatosTabla
     * @param dtStat DatosTabla
     * @param dt DatosTabla
     * @throws Exception
     */
  public lisfactu(EntornoUsuario eu,DatosTabla dtCon1,DatosTabla dtStat,DatosTabla dt)  throws Exception
  {
    EU=eu;
    this.dtCon1=dtCon1;
    this.dtStat=dtStat;
    this.dt=dt;
    this.ct=dt.getConexion();

    iniciarVentana();
  }

  public lisfactu(EntornoUsuario eu, Principal p) {
   EU=eu;
   vl=p.panel1;
   jf=p;
   eje=true;

   setTitulo("Listado Facturas Ventas");
    setAcronimo("lifrve");
   try  {
     if(jf.gestor.apuntar(this))
         jbInit();
     else
       setErrorInit(true);
   }
   catch (Exception e) {
     ErrorInit(e);
     setErrorInit(true);
   }
 }

 public lisfactu(menu p,EntornoUsuario eu) {

   EU=eu;
   vl=p.getLayeredPane();
   setTitulo("Listado Facturas Ventas");
   eje=false;

   try  {
     jbInit();
   }
   catch (Exception e) {
     ErrorInit(e);
     setErrorInit(true);
   }
 }

 private void jbInit() throws Exception
 {
   iniciarFrame();
   this.setSize(new Dimension(600, 539));
   setVersion("2016-12-02");

   titledBorder2 = new TitledBorder("");
   Princ.setLayout(gridBagLayout1);
   conecta();
   dt = new DatosTabla(ct);
   IMPFRATEXTO=EU.getValorParam("impFraTexto",IMPFRATEXTO);
   statusBar = new StatusBar(this);
   Pmargen.setBorder(BorderFactory.createLoweredBevelBorder());
   Pmargen.setBounds(new Rectangle(8, 110, 351, 48));
   Pmargen.setLayout(null);
   cLabel4.setText("Marg.Sup.");
   cLabel4.setBounds(new Rectangle(3, 4, 64, 16));
   margSupE.setBounds(new Rectangle(61, 4, 39, 18));
   cLabel5.setBounds(new Rectangle(138, 26, 54, 16));
   cLabel5.setText("Marg.Inf");
   margInfE.setBounds(new Rectangle(195, 26, 39, 18));
   cLabel11.setBounds(new Rectangle(3, 24, 80, 16));
   cLabel11.setText("N. Lineas Cab.");
   nLiCabE.setBounds(new Rectangle(90, 24, 39, 18));

   cLabel12.setBounds(new Rectangle(135, 4, 67, 16));
   cLabel12.setText("Marg.Sup. L.");
   margSupLE.setBounds(new Rectangle(212, 4, 39, 18));
   cLabel13.setText("Marg.Izq");
   cLabel13.setBounds(new Rectangle(238, 28, 54, 16));
   margIzqE.setBounds(new Rectangle(295, 28, 39, 18));
   BConsul.setBounds(new Rectangle(2, 116, 114, 24));
   BConsul.setMargin(new Insets(0, 0, 0, 0));
   Pcond.setBorder(BorderFactory.createRaisedBevelBorder());
   Pcond.setMaximumSize(new Dimension(582, 168));
   Pcond.setMinimumSize(new Dimension(582, 168));
   Pcond.setPreferredSize(new Dimension(582, 168));
   Pcond.setLayout(null);
   feciniE.setBounds(new Rectangle(175, 3, 81, 17));
   cLabel7.setBounds(new Rectangle(51, 21, 62, 18));
   fecfinE.setBounds(new Rectangle(311, 3, 81, 17));
   cLabel9.setBounds(new Rectangle(123, 3, 53, 15));
   cLabel10.setBounds(new Rectangle(261, 3, 47, 15));
   eje_numeE1.setBounds(new Rectangle(340, 21, 41, 17));
   eje_numeE.setBounds(new Rectangle(142, 21, 41, 17));
   fvc_numeE.setBounds(new Rectangle(185, 21, 55, 17));
   cLabel8.setBounds(new Rectangle(245, 21, 62, 17));
   fvc_numeE1.setBounds(new Rectangle(383, 21, 55, 17));
   empFinE.setBounds(new Rectangle(324, 41, 156, 17));
   cLabel3.setBounds(new Rectangle(11, 41, 70, 19));
   empIniE.setBounds(new Rectangle(86, 41, 160, 17));
   cLabel6.setBounds(new Rectangle(249, 41, 70, 19));
   cli_codiE.setBounds(new Rectangle(86, 60, 396, 17));
   rep_codiE.setBounds(new Rectangle(106, 78, 376, 17));
   cLabel1.setBounds(new Rectangle(24, 60, 64, 19));
   rep_codiL.setBounds(new Rectangle(22, 78, 84, 19));
   Baceptar.setBounds(new Rectangle(119, 116, 116, 24));
   jt.setMaximumSize(new Dimension(497, 246));
   jt.setMinimumSize(new Dimension(497, 246));
   jt.setPreferredSize(new Dimension(497, 246));
   cLabel14.setText("No Fras");
   cLabel14.setBounds(new Rectangle(147, 144, 48, 17));
   countFrasE.setEnabled(false);
   countFrasE.setBounds(new Rectangle(192, 144, 45, 17));
   cLabel15.setBounds(new Rectangle(222, 17, 38, 18));
   cLabel15.setText("Activo");
   cLabel21.setText("Ag/Zona");
   cLabel21.setBounds(new Rectangle(5, 17, 60, 16));
   cli_giroE.setBounds(new Rectangle(261, 38, 65, 19));
   cli_zoncreE.setBounds(new Rectangle(70, 38, 148, 18));
   cli_zoncreE.setAncTexto(30);
   PcondCli.setLayout(null);
   PcondCli.setBorder(titledBorder2);
   PcondCli.setBounds(new Rectangle(243, 96, 334, 67));
   cli_zonrepE.setAncTexto(30);
   cli_zonrepE.setBounds(new Rectangle(70, 17, 148, 18));
   cLabel16.setText("Zona/Cdto");
   cLabel16.setBounds(new Rectangle(7, 38, 65, 18));
   cLabel17.setText("Giro");
   cLabel17.setBounds(new Rectangle(222, 38, 38, 18));
   cli_activE.setBounds(new Rectangle(261, 17, 65, 19));
   titledBorder2.setTitle("Discriminadores Clientes");
   opAgrupa.setToolTipText("Lista las Lineas agrupadas");
    
    opAgrupa.setSelected(true);
    opAgrupa.setText("Agrupa Lineas");
    opAgrupa.setBounds(new Rectangle(448, 2, 114, 18));
    fvc_serieE.setMayusc(true);
     fvc_serie1E.setMayusc(true);
    fvc_serieE.setBounds(new Rectangle(121, 21, 20, 17));
    fvc_serie1E.setBounds(new Rectangle(314, 21, 21, 17));
    this.getContentPane().add(BorderLayout.SOUTH, statusBar);
   this.getContentPane().add(Princ, BorderLayout.CENTER);
   rep_codiE.setAncTexto(30);
   rep_codiE.setPreferredSize(new java.awt.Dimension(152, 18));
   cLabel6.setText("A Empresa");
   empIniE.setAncTexto(30);
   cLabel3.setText("De Empresa");
   empFinE.setAncTexto(30);
   cLabel7.setText("De Factura");
   cLabel8.setText("A Factura");
   cLabel1.setText("Cliente");
   cLabel9.setText("De Fecha");
   cLabel10.setOpaque(true);
   cLabel10.setText("A Fecha");
   Baceptar.addMenu(PadFactur.IMPGRAFICO);
   Baceptar.addMenu(PadFactur.IMPGRAFPRE);
   Baceptar.addMenu(PadFactur.SENDMAIL);
   if (IMPFRATEXTO)
     Baceptar.addMenu(PadFactur.IMPPLANO);
   Baceptar.setToolTipText("Consultar e Imprimir");
   Baceptar.setText("Imprimir");
   ArrayList v = new ArrayList();
   v.add("Cliente"); // 0
   v.add("Nombre"); // 1
   v.add("Fec.Fra"); // 2
   v.add("N.Fra"); // 3
   v.add("Imp.Fra"); // 4
   v.add("Imp.Pend"); // 5
   jt.setCabecera(v);
   jt.setAnchoColumna(new int[]
                      {60, 150, 80, 90, 80, 80});
   jt.setAlinearColumna(new int[]
                        {2, 0, 1, 2, 2, 2});
   jt.setFormatoColumna(4, "---,--9.99");
   jt.setFormatoColumna(5, "---,--9.99");
   jt.setAjustarGrid(true);
//    cPanel1.add(Pmargen, null);
   Pmargen.add(cLabel4, null);
   Pmargen.add(margSupE, null);
   Pmargen.add(cLabel11, null);
   Pmargen.add(nLiCabE, null);
   Pmargen.add(cLabel12, null);
   Pmargen.add(margSupLE, null);
   Pmargen.add(margInfE, null);
   Pmargen.add(cLabel5, null);
   Pmargen.add(margIzqE, null);
   Pmargen.add(cLabel13, null);
   Princ.add(jt, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
                                        , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0),
                                        93, 9));
   Princ.add(Pcond, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                                           , GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 0, 0, 0),
                                           0, 0));
   PcondCli.add(cli_zonrepE, null);
   PcondCli.add(cLabel21, null);
   PcondCli.add(cLabel15, null);
   PcondCli.add(cli_giroE, null);
   PcondCli.add(cli_activE, null);
   PcondCli.add(cli_zoncreE, null);
   PcondCli.add(cLabel16, null);
   PcondCli.add(cLabel17, null);
        Pcond.add(fvc_serie1E, null);
        Pcond.add(fvc_serieE, null);
        Pcond.add(countFrasE, null);
   Pcond.add(cLabel14, null);
   Pcond.add(fecfinE, null);
   Pcond.add(feciniE, null);
   Pcond.add(cLabel7, null);
   Pcond.add(cLabel9, null);
   Pcond.add(cLabel10, null);
   Pcond.add(eje_numeE1, null);
   Pcond.add(eje_numeE, null);
   Pcond.add(fvc_numeE, null);
   Pcond.add(cLabel8, null);
   Pcond.add(fvc_numeE1, null);
   Pcond.add(empFinE, null);
   Pcond.add(cLabel3, null);
   Pcond.add(empIniE, null);
   Pcond.add(cLabel6, null);
   Pcond.add(cli_codiE, null);
   Pcond.add(cLabel1, null);
   Pcond.add(rep_codiL, null);
   Pcond.add(rep_codiE, null);
   Pcond.add(PcondCli, null);
   Pcond.add(Baceptar, null);
   Pcond.add(BConsul, null);
    Pcond.add(opAgrupa, null);
 }

    @Override
 public void iniciarVentana() throws Exception
 {
   if (!dt.select("SELECT emp_obsfra FROM v_empresa WHERE emp_codi=" + EU.em_cod))
     throw new SQLException("Empresa: " + EU.em_cod + " NO ENCONTRADA");
   empObsfra = dt.getString("emp_obsfra");
   MantRepres.llenaLinkBox(rep_codiE, dt);
   rep_codiE.setMayusculas(true);
   margInfE.setValorDec(5.0);
   nLiCabE.setValorDec(18.0);
   margSupLE.setValorDec(2);
   margSupE.setValorDec(0.0);
   margIzqE.setValorDec(63.0);
   dtFra=new DatosTabla(ct);
   cli_codiE.iniciar(dtStat, this, vl, EU);
   
   datCab = new actCabAlbFra(dtStat,EU.em_cod);

   fvc_serie1E.setText("1");
   fvc_serie1E.setText("1");
   empIniE.setFormato(true);
   empIniE.setFormato(Types.DECIMAL, "#9", 2);
   empFinE.setFormato(true);
   empFinE.setFormato(Types.DECIMAL, "#9", 2);

   s = "SELECT emp_codi,emp_nomb FROM v_empresa ORDER BY emp_codi";
   dtCon1.select(s);
   empIniE.addDatos(dtCon1);
   dtCon1.first();
   empIniE.setText(dtCon1.getString("emp_codi"));
   empFinE.addDatos(dtCon1);
   dtCon1.last();
   empFinE.setText(dtCon1.getString("emp_codi"));

   eje_numeE.setValorDec(EU.ejercicio);
   eje_numeE1.setValorDec(EU.ejercicio);
   cli_zonrepE.setFormato(Types.CHAR, "XX", 2);
   cli_zonrepE.texto.setMayusc(true);

   pdconfig.llenaDiscr(dt,cli_zonrepE,"CR",EU.em_cod);
   cli_zonrepE.addDatos("**", "TODOS");

   cli_zoncreE.setFormato(Types.CHAR, "XX", 2);
   cli_zoncreE.texto.setMayusc(true);
   pdconfig.llenaDiscr(dt,cli_zoncreE,"CC",EU.em_cod);

   cli_zoncreE.addDatos("**", "TODOS");

   cli_activE.addItem("Todos", "%");
   cli_activE.addItem("Si", "S");
   cli_activE.addItem("No", "N");
//    cli_activE.setModificable(true);

   cli_giroE.addItem("Todos", "%");
   cli_giroE.addItem("Si", "S");
   cli_giroE.addItem("No", "N");
   fvc_serieE.setText("1");
   fvc_serie1E.setText("1");
   activarEventos();
   eje_numeE.setValorDec(EU.ejercicio);
   eje_numeE1.setValorDec(EU.ejercicio);
 }
 void activarEventos()
 {
   Baceptar.addActionListener(new ActionListener() {
     @Override
     public void actionPerformed(ActionEvent e) {

         Baceptar_actionPerformed(e.getActionCommand());
     }
   });
   BConsul.addActionListener(new ActionListener()
   {
     @Override
     public void actionPerformed(ActionEvent e)
     {
       BConsul_actionPerformed();
     }
   });
 }
 void BConsul_actionPerformed()
 {
  
        try {
            if (!checkCond()) {
                return;
            }
            new miThread("a")
            {

                public void run() {
                    mensaje("Buscando facturas...");
                    lisfactu.this.setEnabled(false);
                    jt.panelG.setVisible(false);
                    verFrasListar();
                    lisfactu.this.setEnabled(true);
                    jt.panelG.setVisible(true);
                    mensaje("");
                }
            };
        } catch (Exception k) {
            Error("Error al Consultar Las Facturas", k);
        }
    }

 void verFrasListar()
 {
     try {

     s=getStrSql();
     jt.removeAllDatos();
//     debug(s);
     countFrasE.resetTexto();
     if (! dtCon1.select(s))
     {
       mensajeErr("NO ENCONTRADAS FACTURAS CON ESTOS CRITERIOS");
       return;
     }
     int nFras=0;
     Vector v=new Vector();
     do
     {
       v.removeAllElements();
       v.addElement(dtCon1.getString("cli_codi"));
       v.addElement(dtCon1.getString("cli_nomb"));
       v.addElement(dtCon1.getFecha("fvc_fecfra","dd-MM-yyyy"));
       v.addElement(dtCon1.getString("fvc_empcod")+"/"+
            dtCon1.getString("fvc_ano")+"-"+dtCon1.getString("fvc_serie")+" "+dtCon1.getString("fvc_nume"));
       v.addElement(dtCon1.getString("fvc_sumtot"));
       v.addElement(""+(dtCon1.getDouble("fvc_sumtot")-dtCon1.getDouble("fvc_impcob")));
       jt.addLinea(v);
       if (nFras>999)
       {
         msgBox("HAY Mas de 999 FACTURAS ... MOSTRADAS LAS 999 PRIMERAS");
         break;
       }
       nFras++;
     } while (dtCon1.next());
     jt.requestFocusInicio();
     countFrasE.setValorDec(nFras);
     mensajeErr(nFras+" Fras mostradas ");
   } catch (Exception k)
   {
     Error("Error al Consultar Las Facturas",k);
   }
 }
 String getStrSqlCount()
 {
     return "SELECT count(*) as cuantos"+
   " FROM v_facvec AS f,clientes as cl,v_forpago as fp WHERE "+
      getCondWhere();
 }
 String getStrSql()
 {
   return "SELECT f.emp_codi as fvc_empcod,f.*,cl.*,fp.fpa_nomb,fp.fpa_dia1,fp.fpa_dia2,fp.fpa_dia3 "+
   " FROM v_facvec AS f,clientes as cl,v_forpago as fp WHERE "+
      getCondWhere()+
    " ORDER BY f.fvc_ano,f.emp_codi,f.fvc_nume";
 }

 String getCondWhere()
 {
     return  " f.emp_codi >=" +empIniE.getValorInt()+
    (empFinE.getValorInt()>0?" and f.emp_codi <=" +empFinE.getValorInt():"")+
    (feciniE.isNull()?"":" AND f.fvc_fecfra >= TO_DATE('"+feciniE.getText()+"','dd-MM-yyyy') ")+
    (fecfinE.isNull()?"":" AND f.fvc_fecfra <= TO_DATE('"+fecfinE.getText()+"','dd-MM-yyyy') ")+
    (fvc_numeE.getValorInt()>0?" and f.fvc_nume >= "+fvc_numeE.getValorInt():"")+
    (fvc_numeE1.getValorInt()>0?" and f.fvc_nume <= "+fvc_numeE1.getValorInt():"")+
    (eje_numeE.getValorInt()>0?" and f.fvc_ano >= "+eje_numeE.getValorInt():"")+
    (eje_numeE1.getValorInt()>0?" and f.fvc_ano <= "+eje_numeE1.getValorInt():"")+
    (cli_codiE.getValorInt()>0?" and f.cli_codi = "+cli_codiE.getValorInt():"")+
    (rep_codiE.isNull()?"":" and rep_codi = '"+rep_codiE.getText()+"'")+
    (! cli_zonrepE.isNull(true) && !cli_zonrepE.getText().equals("**")?" and cl.cli_zonrep  LIKE '%"+Formatear.reemplazar(cli_zonrepE.getText(),"*","%")+"%'":"")+
    (! cli_zoncreE.isNull(true) && !cli_zoncreE.getText().equals("**")?" and cl.cli_zoncre  LIKE '%"+Formatear.reemplazar(cli_zoncreE.getText(),"*","%")+"%'":"")+
    (!cli_activE.getValor().equals("%")?" and cl.cli_activ = '"+cli_activE.getValor()+"'":"")+
    (!cli_giroE.getValor().equals("%")?" and cl.cli_giro = '"+cli_giroE.getValor()+"'":"")+
    (fvc_serieE.isNull()?"": " and fvc_serie  >= '"+fvc_serieE.getText()+"'")+
     (fvc_serie1E.isNull()?"": " and fvc_serie  <= '"+fvc_serie1E.getText()+"'")+
         
    " and f.cli_codi = cl.cli_codi "+
    " and fp.fpa_codi = cl.fpa_codi ";
 }
 boolean checkCond() throws SQLException
 {
   if (empIniE.getValorInt() == 0)
   {
     mensajeErr("Introduzca la empresa Inicial");
     empIniE.requestFocus();
     return false;
   }
   if (! empIniE.controla(true))
   {
     mensajeErr("La empresa inicial debe ser una empresa existente");
     return false;
   }
//   if (fvc_serieE.isNull())
//   {
//       mensajeErr("Introduzca serie inicial de la factura");
//       fvc_serieE.requestFocus();
//       return false;
//   }
//   if (fvc_serie1E.isNull())
//   {
//       mensajeErr("Introduzca serie final de la factura");
//       fvc_serie1E.requestFocus();
//       return false;
//   }
   s=getStrSqlCount();
   dtStat.select(s);
   numFrasTratar=dtStat.getInt("cuantos");
   if (numFrasTratar==0)
   {
       msgBox("No existen facturas con estas condiciones");
       feciniE.requestFocus();
       return false;
   }
   if (numFrasTratar>MAX_AVISO)
   {
       int res=mensajes.mensajeYesNo("Se van a imprimir "+dtStat.getInt("cuantos")+" Facturas. Desea cancelar el listado ?");
       if (res!=mensajes.NO)
       {
            mensajeErr("Demasiadas facturas. Cancelado por decision del usuario");
            feciniE.requestFocus();
            return false;
       }
   }
   return true;
 }

 void Baceptar_actionPerformed(final String accion)
 {
 
   try  {
       if (!checkCond()) {
           return;
       }
       new miThread("c")
       {
                @Override
         public void run()
         {
             msgEspere("Espere ... LISTANDO FACTURAS");
             listaFraThread(accion);
             resetMsgEspere();
         }
       };
   }   catch (SQLException k)
   {
     Error("Error al Listar Facturas",k);
   }
 }

 void listaFraThread(String accion)
 {
     try {
       int res;
       s = getStrSql();
       String cfgLifrgr=null;
          if (accion.contains(PadFactur.IMPPLANO))
          cfgLifrgr="N";
       if (accion.contains(PadFactur.IMPGRAFICO))
       {
           setFraPreImpr(false);
          cfgLifrgr="S";
       }
       if (accion.contains(PadFactur.IMPGRAFPRE))
       {
          setFraPreImpr(true);
          cfgLifrgr="S";
       }
       if (accion.contains(PadFactur.SENDMAIL))
       {
           enviarMail();
           return;
       }
     res = impFactura(s, empIniE.getValorInt(),cfgLifrgr);
     if (res==0)
       mensajeErr(msgError);
    else
      mensajeErr("Listadas  "+nFraImp+" Facturas");
   } catch (Throwable k)
   {
     Error("Error al Listar Facturas",k);
   }
 }
 void enviarMail()
 {
    if (! opAgrupa.isSelected())
    {
        if (mensajes.mensajePreguntar("Enviar por email factura con las lineas sin agrupar")!=mensajes.YES)
            return;
    }

    try {
        if (ifMail == null) {
            ifMail = new IFMail();
            ifMail.iniciar(this);
            ifMail.getCliField().setPeso(1);
            ifMail.setVisible(false);
            ifMail.setIconifiable(false);
            ifMail.setLocation(this.getLocation().x + 30, this.getLocation().x + 30);
            ifMail.setLisfactu(this);            
            vl.add(ifMail, 1);
        }
        ifMail.setVisible(true);
        ifMail.setSelected(true); 
        ifMail.setAsunto("Relación de Facturas ");
        ifMail.setText("Estimado cliente,\n\nAdjunto le enviamos las facturas de la fecha: "+
            feciniE.getText()+" a "+fecfinE.getText()+
               "\n\nAtentamente\n\n"+pdempresa.getNombreEmpresa(dtStat, EU.em_cod) );           
        ifMail.setCliCodi(cli_codiE.getText());

        ifMail.setDatosDoc("F", getStrSql(), true);     
    } catch (Exception k)
    {
        Error("Error al enviar el Correo",k);
    }
 }
 
 public int getNumFraImpr()
 {
   return nFraImp;
 }
 public String getMsgError()
 {
   return msgError;
 }

 public int impFacturaText(String s) throws Throwable
 {
   simular=false;
   nFraImp = 0;
   double impLin;
   String alb, alb1;
   String proNomb;

   if (!dtFra.select(s))
   {
     msgError = "NO ENCONTRADAS FACTURAS CON ESTOS CRITERIOS";
     return 0;
   }
   File f = util.getFile("albfra", EU);
   fOut = new FileOutputStream(f, false);

   //rs = dtFra.getResultSet();
   do
   {
     impVtoAc = 0;

     if (!getDatosFra(dtFra.getInt("fvc_ano"),
          dtFra.getInt("fvc_empcod"), dtFra.getString("fvc_serie"),
                      dtFra.getInt("fvc_nume"), dtFra.getDouble("fvc_porreq")!=0))
       continue;

     imprCabe(dtFra);
     nLinALb = 0;
     //Busco Comentarios de Cabecera.
     String condWhereCom="and  eje_nume = "+dtFra.getInt("fvc_ano")+
       " and emp_codi = "+dtFra.getInt("fvc_empcod")+
        " and fvc_serie = '"+dtFra.getString("fvc_serie")+"'"+
       " and fvc_nume = "+dtFra.getInt("fvc_nume");
     s="select fco_coment from facvecom where fco_tipo='C'"+ condWhereCom+
            " order by fco_numlin";
     if (dtStat.select(s))
         impLinCom(dtStat);
     s = getSqlLin();
     alb = "";
//       debug("Lineas: "+s);
     if (!dtStat.select(s))
       continue;
     do
     {
       alb1 = dtStat.getString("avc_serie") + dtStat.getString("avc_ano") +
           dtStat.getString("avc_nume");

       if (!alb.equals(alb1))
       {
         alb = alb1;
         print(Formatear.space(12) +
               dtStat.getFecha("avc_fecalb", "dd-MM-yyyy") +
               " Albarán: " +
               Formatear.format(dtStat.getString("avc_ano"), "9999") + "/" +
               dtFra.getInt("fvc_empcod") + dtStat.getString("avc_serie") + "/" +
               Formatear.format(dtStat.getString("avc_nume"), "999999"));
         nLinALb++;
       }

       impLin = Formatear.redondea(Formatear.redondea(dtStat.getDouble("fvl_canti", true), 2) *
                                   Formatear.redondea(dtStat.getDouble("fvl_prven", true), 3), 2);

       proNomb = dtStat.getString("fvl_nompro");
//         pro_codiE.getNombArtCli(dtStat.getInt("pro_codi"),
//                                           dtCon1.getInt("cli_codi"),
//                                           dtCon1.getInt("emp_codi"), dt);
       print(Formatear.space(2) +
             Formatear.format(dtStat.getInt("pro_codi", true), "#99999") +
             Formatear.space(4) +
             Formatear.ajusIzq(proNomb, 35) +
             Formatear.space(1) +
             Formatear.format(Formatear.redondea(dtStat.getDouble("fvl_canti", true), 2), "---,--9.99") +
             Formatear.space(2) +
             (valora ? Formatear.format(Formatear.redondea(dtStat.getDouble("fvl_prven", true), 3), "---9.99") :
              "       ") +
             Formatear.space(2) +
             (valora ? Formatear.format(impLin, "---,--9.99") :
              "          "));
       nLinALb++;
       if (nLinALb >= nLiCabE.getValorInt() - 1)
       {
         println(margInfE.getValorInt() + 14 - (nLinALb - (nLiCabE.getValorInt() - 1)));
         imprCabe(dtFra);
         nLinALb = 0;
       }
     }  while (dtStat.next());

     if (! empObsfra.equals(""))
     {
         int nLiSum=1;
         int posIni=0;
         do
         {
           if ((posIni=empObsfra.indexOf('\n',posIni+1))>=0)
             nLiSum++;
           else
             break;
         } while (true);
         print(Formatear.space(2) +empObsfra);
         nLinALb+=nLiSum;
     }
     s="select fco_coment from facvecom where fco_tipo='P'"+ condWhereCom+
            " order by fco_numlin";
     if (dtStat.select(s))
         impLinCom(dtStat);
     println(nLiCabE.getValorInt() - nLinALb);
     println(3);
     nVtos = clFactCob.calDiasVto(dtFra, dtFra.getInt("cli_dipa1"),
                                  dtFra.getInt("cli_dipa2"), 0,
                                  dtFra.getFecha("fvc_fecfra", "dd-MM-yyyy"));
     impVto = (dtFra.getDouble("fvc_sumtot") - dtFra.getDouble("fvc_impcob")) / nVtos;
     if (valora)
     {
       // Imp.Bruto
       print(Formatear.space(margIzqE.getValorInt()) +
             Formatear.space(3 + 4) +
             Formatear.format(getValHash("fvc_impbru"), "---,--9.99"));

       // Vto 0 y Dto.
       printVto(0);
       if (getValHash("fvc_dtopp") > 0)
         print(Formatear.format(getValHash("fvc_dtopp"), "#9.99") +
               Formatear.space(2) +
               Formatear.format(getValHash("fvc_impdpp"), "---,--9.99"));
       else
         print("");

         // Vto 1 y Base Imponible
       printVto(1);
       print(Formatear.space(3 + 4), false);
       print(Formatear.format(getValHash("fvc_basimp"), "---,--9.99"));

       printVto(2);
       if (getValHash("fvc_tipree") > 0)
         print(Formatear.format(getValHash("fvc_tipree"), "#9.99") +
               Formatear.space(2) +
               Formatear.format(getValHash("fvc_impree"), "---,--9.99"));
       else
         print("");

         // Imp. de Iva
       print(Formatear.space(margIzqE.getValorInt()), false);
       if (getValHash("fvc_tipiva") > 0)
         print(Formatear.format(getValHash("fvc_tipiva"), "#9.99") +
               Formatear.space(2) +
               Formatear.format(getValHash("fvc_impiva"), "---,--9.99"));
       else
         print("");

       print(Formatear.space(3) + dtFra.getString("fpa_nomb"));
       println(3);
       banNomb=buscaBanco( dtFra.getInt("ban_codi", true));

       print(Formatear.space(2) + Formatear.ajusIzq(banNomb, 30) +
             Formatear.space(margIzqE.getValorInt() + 7 - 32) +
             Formatear.format(dtFra.getDouble("fvc_sumtot"), "---,--9.99"));
     }
     else
       println(1);
     println(margInfE.getValorInt());
     nFraImp++;
     actualizaMsg("Imprimiendo fra: "+nFraImp+" de "+numFrasTratar,false);
   }
   while (dtFra.next());
   fOut.close();
   Process pr = Runtime.getRuntime().exec(EU.pathCom + EU.comPrint + " " +
                                          f.getAbsolutePath() + " " +
                                          EU.puertoAlb);
   new threaEspFra(pr);
   pr.waitFor();
   if (pr.exitValue() != 0)
   {
     msgError = "Error al Imprimir (Error: " + pr.exitValue() + ")";
     return 0;
   }
   dt.commit();
   return nFraImp;
 }
    private void impLinCom(DatosTabla dt) throws SQLException, IOException {
        do {
            print(Formatear.space(2) +
                 dt.getString("fco_coment"));
            if (nLinALb >= nLiCabE.getValorInt() - 1) {
                println(margInfE.getValorInt() + 14 - (nLinALb - (nLiCabE.getValorInt() - 1)));
                imprCabe(dtFra);
                nLinALb = 0;
            }
            nLinALb++;
        } while (dt.next());

    }
private String buscaBanco(int banCodi) throws SQLException
{
  String s = "SELECT * FROM v_banco WHERE ban_codi = " + banCodi;
  if (dtStat.select(s))
    return dtStat.getString("ban_nomb");
  else
    return null;
}

 int setFraListada(int ejeNume, int empCodi,String fvcSerie, int fvcNume, int valor) throws SQLException
 {
   s = "UPDATE v_facvec set fvc_impres= "+valor+" WHERE " +
       "  fvc_ano = " + ejeNume +
       " and emp_codi = " + empCodi +
       " and fvc_serie = '"+fvcSerie+"'"+
       " and fvc_nume = " + fvcNume;
   return dt.executeUpdate(s);
 }
 boolean getDatosFra(int ejeNume, int empCodi,String fvcSerie, int fvcNume,boolean recEquiv) throws Exception
 {
   if ( simular)
    setFraListada(ejeNume,empCodi,fvcSerie,fvcNume,-1);
   boolean incIva;
   incIva = dtFra.getInt("cli_exeiva") == 0 && empCodi <= 90;
   if (!datCab.actDatosFra(ejeNume, empCodi, fvcSerie,fvcNume, incIva,
                           dtFra.getDouble("fvc_dtopp"),
                           dtFra.getDouble("fvc_dtocom"),
                           recEquiv ,null))
     return false;
   numLiComCab=0;
   numLiComPie=0;
   numLinFra=0;
   htCab = datCab.getHashTable();
   impVtoAc = 0;
   nVtos = clFactCob.calDiasVto(dtFra, dtFra.getInt("cli_dipa1"),
                                dtFra.getInt("cli_dipa2"), 0,
                                dtFra.getFecha("fvc_fecfra", "dd-MM-yyyy"));
   banNomb = buscaBanco(dtFra.getInt("ban_codi", true));
   impVto = (dtFra.getDouble("fvc_sumtot") - dtFra.getDouble("fvc_impcob")) / nVtos;
   s="SELECT count(*) as cuantos FROM v_facvel "+
    " where fvc_nume= "+fvcNume+
    " and fvc_serie= '"+fvcSerie+"'"+
    " and emp_codi = "+empCodi+
    " and eje_nume = "+ejeNume;
   dtStat.select(s);
   numLinFra=dtStat.getInt("cuantos",true);
   
   s="SELECT count(*) as cuantos,fco_tipo FROM facvecom "+
    " where fvc_nume= "+fvcNume+
    " and fvc_serie= '"+fvcSerie+"'"+
    " and emp_codi = "+empCodi+
    " and eje_nume = "+ejeNume+
    " group by fco_tipo";
   if (dtStat.select(s))
   {
       do
       {
           if (dtStat.getString("fco_tipo").equals("C"))
            numLiComCab = dtStat.getInt("cuantos");
           if (dtStat.getString("fco_tipo").equals("P"))
            numLiComPie = dtStat.getInt("cuantos");
       } while (dtStat.next());
   }
   return true;
 }

 private void imprCabe(DatosTabla dtCon1) throws SQLException, IOException
 {
    print(util.INIT_PRINT);
    println(margSupE.getValorInt());
    print(Formatear.space(43) + Formatear.ajusIzq(dtCon1.getString("cli_nomb"),37));
    print(Formatear.space(43) + Formatear.ajusIzq(dtCon1.getString("cli_nomco"),37));
    print(Formatear.space(43) + Formatear.ajusIzq(dtCon1.getString("cli_direc"),37));
    print(Formatear.space(43) + Formatear.ajusIzq(dtCon1.getString("cli_codpo") + "-" +
          dtCon1.getString("cli_pobl"),37));
    print(Formatear.space(43) + Formatear.ajusIzq("N.I.F. "+dtCon1.getString("cli_nif"),37));

    println(3);
    print(Formatear.space(2) +
          dtCon1.getString("fvc_serie") +
          Formatear.format(dtCon1.getInt("fvc_nume"), "999999") +
          Formatear.space(2) +
          dtCon1.getFecha("fvc_fecfra", "dd-MM-yy") +
          Formatear.space(3) +
          Formatear.format(dtCon1.getInt("cli_codi"), "999999"));
    println(margSupLE.getValorInt());
  }
  void println(int nLin) throws IOException
   {
     util.println(nLin,fOut);
   }
   void print(String str) throws IOException
   {
     util.print(str,fOut);
   }
   void print(String str,boolean saltoLinea) throws IOException
   {
     util.print(str,saltoLinea,fOut);
   }
   void printVto(int nVto) throws Throwable
   {
     if (nVtos > nVto)
       print(Formatear.space(1)+clFactCob.diasVto[nVto] + // 10 Car.
             Formatear.space(3) +
             (nVto+1==nVtos?Formatear.format( (dtCon1.getDouble("fvc_sumtot") -dtCon1.getDouble("fvc_impcob")) - impVtoAc, "---,--9.99"):
              Formatear.format(impVto, "---,--9.99")), false);
     else
       print(Formatear.space(24), false);
     print(Formatear.space(margIzqE.getValorInt()-24),false);
     impVtoAc+=impVto;
   }

   String getSqlLin() throws SQLException, ParseException
   {
     return "SELECT avc_fecalb,avc_ano,avc_serie,avc_nume, l.pro_codi,l.fvl_nompro, " +
         " sum(l.fvl_canti) as fvl_canti,  fvl_prven,fvl_dto FROM V_facvel as l " +
         " WHERE l.eje_nume = " + dtCon1.getInt("fvc_ano") +
         " and l.emp_codi = " + dtCon1.getInt("fvc_empcod") +
         " and l.fvc_serie = '"+dtCon1.getString("fvc_serie")+"'"+
         " and l.fvc_nume = " + dtCon1.getInt("fvc_nume") +
         " group by avc_fecalb, avc_serie,avc_ano,avc_nume, pro_codi,l.fvl_nompro,fvl_prven,fvl_dto " +
         " ORDER BY 1,2,3,4,5";
   }

   double getValHash(String name)
   {
     return util.getValHash(name, htCab);
   }


   /**
    * Imprime la facturas que el string SQL encuentre.
    * Dependiendo de la configuracion de la tabla listados lanzara un fichero
    * jasper o en texto plano.
    * Si es null el valor de la tabla listados usara el texto plano.
    *
    * @param sql String
    * @throws Throwable
    * @return int Numero de facturas impresas.
    */

   /**
    * Imprime la facturas que el string SQL encuentre.
    * Dependiendo de la configuracion de la tabla listados y/o de la variable tipoImpr
    * lanzara un fichero* jasper o en texto plano.
    * Si es null el valor de la tabla listados usara el texto plano.
    *
    * @param sql Setencia SQL con las facturas a imprimir (sobre la tabla v_facvec)
    * @param empCodi Empresa
    * @param lifrgr Null Depende de Conf. Global. "N" - Plano "S" - Jasper
    * @return Numero de Facturas Impresas
    * @throws java.lang.Throwable Error en el listado
    */
   public int impFactura( String sql,int empCodi,String lifrgr) throws Throwable
   {

     if (lifrgr==null)
        lifrgr=pdconfig.getCfgLifrgr(empCodi,dtStat);
     simular=false;
     if (! lifrgr.equals("S"))
       return impFacturaText(sql);
    else
       return impFacturaJasper(sql,true);
   }
   public void setAgrupa(boolean agrup)
   {
     opAgrupa.setSelected(agrup);
   }
   public boolean getAgrupa()
   {
     return opAgrupa.isSelected();
   }
    public void sendFraFax(String sql, String numFax, int cliCodi,boolean opCopiaPapel) throws Exception
    {
        setFraPreImpr(false);
        if (impFacturaJasper(sql,opCopiaPapel) == 0) {
            return;
        }
        File fichPDF = util.getFile("fraven", EU, "pdf",true);
        FileOutputStream outStr = new FileOutputStream(fichPDF);
        JasperExportManager.exportReportToPdfStream(jp, outStr);
        outStr.close();

        if (sendFax == null) {
            sendFax = new SendFax(EU, dtCon1); // Inicio clase SendFax
        }
        sendFax.setCliente(cliCodi);
        sendFax.sendFax("F", empCodi, fvcAno, fvcSerie, fvcNume, numFax, fichPDF.getAbsolutePath());
    }
      /**
    * Especifica el subject para cuando se envia un correo
    * @param subject 
    */
   public void setSubject(String subject)
   {
       this.subject=subject;
   }
   public void setEmailCC(String emailCC)
   {
       this.emailCC=emailCC;
   }
    public void setAsunto(String asunto)
   {
       this.asunto=asunto;
   }
    public void sendFraEmail(String sql, String from, int cliCodi) throws Exception
    {
        setFraPreImpr(false);
        if (impFacturaJasper(sql,false) == 0) {
            return;
        }
        File fichPDF = util.getFile("fraven", EU, "pdf",true);
        FileOutputStream outStr = new FileOutputStream(fichPDF);
        JasperExportManager.exportReportToPdfStream(jp, outStr);
        outStr.close();
        MailHtml correo=new MailHtml(EU.usu_nomb,EU.email);
        correo.setEmailCC(emailCC);
        correo.enviarFichero(from,asunto,subject,
                    fichPDF,"factura.pdf");       
       
    }
    /**
     * Indica si la fra se sacara en papel preimpreso (true) o blanco (false)
     * 
     * @param fraPreImp
     */
   public void setFraPreImpr(boolean fraPreImp)
   {
       this.fraPreImp=fraPreImp;
   }
   public boolean getFraPreImpr()
   {
       return fraPreImp;
   }
   public int impFacturaJasper(String sql,boolean copiaPapel) throws Exception
   {        
     if (!dtFra.select(sql))
     {
       msgError="No encontradas Facturas con estos criterios";
       mensajes.mensajeAviso(msgError);
       return 0;
     }

      cliCodi=dtFra.getInt("cli_codi");
      fvcAno=dtFra.getInt("fvc_ano");
      empCodi=dtFra.getInt("emp_codi");
      fvcSerie=dtFra.getString("fvc_serie");
      fvcNume=dtFra.getInt("fvc_nume");
      
     Listados lis=  Listados.getListado(EU.em_cod, (getFraPreImpr()?Listados.CABPI_FRV: Listados.CAB_FRV), dtStat);
     JasperReport jr = Listados.getJasperReport(EU,lis.getNombFich() );
     HashMap mp = Listados.getHashMapDefault();
     mp.put(JRParameter.REPORT_CONNECTION, ct);
 //     mp.put("valora", new Boolean(valora));
     mp.put("emp_obsfra", empObsfra);
     mp.put("logotipo", lis.getPathLogo());
     mp.put("obser",null);
     mp.put("agrupa",getAgrupa()?new Integer(1):new Integer(0));
    
     vlike lk=  pdempresa.getDatosEmpresa(dtStat, EU.em_cod);
     mp.put("empNif",MantPaises.getInicialesPais(lk.getInt("pai_codi"),dtStat)+
         lk.getString("emp_nif"));
   
     mp.put("SUBREPORT_FILE_NAME",
            gnu.chu.print.util.getPathReport(EU, Listados.getNombListado(EU.em_cod,Listados.LIN_FRV, dtStat)));
     mp.put("SB_NAME_POB",
            gnu.chu.print.util.getPathReport(EU, Listados.getNombListado(EU.em_cod, Listados.PCO_FRV, dtStat)));
     swFirst = true;
     nFraImp = 1;
//     rs = dtFra.getResultSet();
     if (!getDatosFra(dtFra.getInt("fvc_ano"), dtFra.getInt("fvc_empcod"),
                       dtFra.getString("fvc_serie"),dtFra.getInt("fvc_nume"),
                       dtFra.getDouble("fvc_porreq")!=0))
     {
         msgError="No entrado datos de fra:  "+dtFra.getInt("fvc_ano")+"-"+
              dtFra.getInt("fvc_empcod")+" "+
              dtFra.getString("fvc_serie")+"/"+dtFra.getInt("fvc_nume");
        return 0;
     }
//     rs.first();
     jp = JasperFillManager.fillReport(jr, mp, this);
     if (copiaPapel)
       gnu.chu.print.util.printJasper(jp, EU);
//     dt.commit();
     return nFraImp;
   }

  @Override
   public boolean next() throws JRException
   {
     try
     {
       if (swFirst)
       {
         swFirst = false;
         return true;
       }
       boolean ret;
       while (true)
       {
         ret = dtFra.next();
         if (!ret)
           return false;
         if (getDatosFra(dtFra.getInt("fvc_ano"), dtFra.getInt("fvc_empcod"),
                         dtFra.getString("fvc_serie"), dtFra.getInt("fvc_nume"),
                         dtFra.getDouble("fvc_porreq")!=0))
         {
             setMensajePopEspere("Imprimiendo fra "+nFraImp+ " de "+numFrasTratar,false);
           nFraImp++;
           return true;
         }
       }
     }
     catch (Exception k)
     {
       throw new JRException(k);
     }
   }

  @Override
   public Object getFieldValue(JRField jRField) throws JRException
   {
     try
     {
       String campo = jRField.getName();
       if (campo.equals("cli_nomb") ||
           campo.equals("cli_nomco") ||
           campo.equals("cli_direc") ||
           campo.equals("cli_pobl") ||
           campo.equals("cli_nif") ||
           campo.equals("fvc_serie") )
         return dtFra.getString(campo);
       if (campo.equals("emp_codi") ||
           campo.equals("fvc_nume") ||
           campo.equals("cli_codi") ||
           campo.equals("cli_recequ") ||
           campo.equals("fvc_ano"))
         return dtFra.getInt(campo);
       if (campo.equals("cli_codpo"))
           return dtFra.getString("cli_codpo");
       if (campo.equals("fvc_dtocom") ||
           campo.equals("fvc_dtootr") || campo.equals("fvc_sumtot"))
         return dtFra.getDouble(campo);
       if (campo.equals("fvc_fecfra"))
         return dtFra.getDate(campo);

       if ( campo.equals("fvc_impbru") ||      
           campo.equals("fvc_basimp") ||
           campo.equals("fvc_impiva") ||
           campo.equals("fvc_tipiva") ||
           campo.equals("fvc_tipree") ||
           campo.equals("fvc_impree") ||
           campo.equals("fvc_impfra"))
         return (Double) htCab.get(jRField.getName());
       
       if (campo.equals("fvc_dtos"))
           return (double)htCab.get("fvc_dtopp")+(double)htCab.get("fvc_dtoco");
       if (campo.equals("fvc_impdto"))
           return (double)htCab.get("fvc_impdpp")+(double)htCab.get("fvc_impdco");
       if (campo.equals("diaVto1") ||
           campo.equals("diaVto2") ||
           campo.equals("diaVto3"))
         return getDiaVto(campo);
       if (campo.equals("impVto1") ||
           campo.equals("impVto2") ||
           campo.equals("impVto3"))
         return getImpVto(campo);
       if (campo.equals("banNomb"))
         return banNomb;
       if (campo.equals("fpa_nomb"))
         return dtFra.getString("fpa_nomb");
       if (campo.equals("numLinComCab"))
           return numLiComCab;
       if (campo.equals("numLinComPie"))
           return numLiComPie;
       if (campo.equals("numLinFra"))
           return numLinFra;
       throw new Exception("Campo " + jRField.getName() + " NO encontrado");

     }
     catch (Exception k)
     {
       throw new JRException(k);
     }
   }
   java.util.Date getDiaVto(String campo) throws ParseException
   {
     return Formatear.getDate(clFactCob.diasVto[Integer.parseInt(campo.substring(campo.length()-1))-1],"dd-MM-yyyy");
   }

   Double getImpVto(String campo) throws SQLException
   {
     int nVto=Integer.parseInt(campo.substring(campo.length()-1))-1;
     if (nVtos > nVto)
     {
       if (nVto +1 == nVtos)
         return (dtFra.getDouble("fvc_sumtot") - dtFra.getDouble("fvc_impcob")) - impVtoAc;
       else
       {
         impVtoAc+=impVto;
         return impVto;
       }
     }
     else
      return null;
   }
 }
 class threaEspFra extends Thread
 {
   Process pr;

   public threaEspFra(Process p)
   {
     pr = p;
     this.start();
   }

    @Override
   public void run()
   {
     try
     {
       threaEspFra.sleep(2000);
       pr.destroy();
     }
     catch (Exception k)
     {

     }
   }

 }
