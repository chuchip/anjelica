

/*
 *  * <p>Titulo: GenFactur.java </p>
 * <p>Descripción: Generacion de Facturas sobre alb. de ventas</p>
 * <p>Copyright: Copyright (c) 2005-2012
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
 * @version 1.0
 * @version 1.1 Incluida serie de factura. Realizado diseño en netbeans
 * Created on 24-ene-2009, 16:43:56
 */

package gnu.chu.anjelica.facturacion;

import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.DatosIVA;
import gnu.chu.anjelica.pad.MantArticulos;
import gnu.chu.anjelica.pad.MantTipoIVA;
import gnu.chu.anjelica.pad.pdclien;
import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.anjelica.pad.pdejerci;
import gnu.chu.anjelica.pad.pdnumeracion;
import gnu.chu.anjelica.riesgos.clFactCob;
import gnu.chu.controles.StatusBar;
import gnu.chu.eventos.CambioEvent;
import gnu.chu.eventos.CambioListener;
import gnu.chu.sql.DatosTabla;
import gnu.chu.sql.conexion;
import gnu.chu.sql.vlike;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Fecha;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.SystemOut;
import gnu.chu.utilidades.miThread;
import gnu.chu.utilidades.ventana;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

/**
 *
 * @author cpuente
 * <p>Titulo: GenFactur </p>
 * <p>Descripción: Generación de facturas de ventas sobre los albaranes</p>
 *  * <p>Copyright: Copyright (c) 2005-2012
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
 * @version 1.1 Incluida opcion de Serie de factura (01/20/2009)
 *
 */
public class GenFactur extends ventana {

  conexion ctCom;
  String fecAlb;
  int nFras;
  double impLin,impDtCom;
  boolean incIva;
  int fpaCodi,cliDipa1,cliDipa2;
  String fvcFecfra;
  int cliRecequ;
  int numDec=2;
  int numDecPrecio=4;
  int avcNume;
  int divCodi;
  int banCodi,banOfic,banDico;
  double banCuen;
  boolean serAuto;
  int fvcCobrad=-1; // NO cobrado
  double fvcImpcob=0; // Importe Cobrado
  int numFra[]=new int[4];
  int nLinFra,numFraB, tipIva,cliCodi,fvcNumfra;
  boolean opAgrCli;
  String avcClinom="",avcSerie="",fvcSerie;
  boolean esGiro=false,swAutNumFra;
  int recEqu;
  double dtoPP,dtoCom;
  vlike lkAlb=new vlike();
  String s;
  DatosTabla dtAlb;
  DatosTabla dtAdd;

  public GenFactur()
  {
    try  {
      jbInit();
    } catch (Exception k)
    {

    }
  }
  public GenFactur(EntornoUsuario eu, Principal p)
  {
    EU=eu;
    vl=p.panel1;
    jf=p;
    eje=true;

    setTitulo("Generacion Facturas Ventas");

    try  {
      if(jf.gestor.apuntar(this))
          jbInit();
      else
        setErrorInit(true);
    }
    catch (Exception e) {
      logger.fatal("Error al iniciar Generacion facturas",e);
      setErrorInit(true);
    }
  }

  public GenFactur(gnu.chu.anjelica.menu p,EntornoUsuario eu) {

    EU=eu;
    vl=p.getLayeredPane();
    setTitulo("Generacion Facturas Ventas");
    eje=false;

    try  {
      jbInit();
    }
    catch (Exception e) {
      logger.fatal("Error al iniciar Generacion facturas",e);
      setErrorInit(true);
    }
  }

  private void jbInit() throws Exception
  {
    iniciarFrame();
    this.setSize(new Dimension(534, 543));
    setVersion("2015-04-16");
   
    statusBar= new StatusBar(this);
    this.getContentPane().add(statusBar,BorderLayout.SOUTH);
    conecta();
    initComponents();
    this.setSize(new Dimension(564, 553));
    this.setResizable(false);

    jtAlb.setNumColumnas(6);
  }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Pprinc = new gnu.chu.controles.CPanel();
        jtAlb = new gnu.chu.controles.Cgrid();
        cLabel1 = new gnu.chu.controles.CLabel();
        feciniE = new gnu.chu.controles.CTextField(Types.DATE, "dd-MM-yyyy");
        cLabel2 = new gnu.chu.controles.CLabel();
        fecfinE = new gnu.chu.controles.CTextField(Types.DATE, "dd-MM-yyyy");
        cLabel3 = new gnu.chu.controles.CLabel();
        avc_numeE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#####9");
        cLabel4 = new gnu.chu.controles.CLabel();
        avc_numeE1 = new gnu.chu.controles.CTextField(Types.DECIMAL,"#####9");
        opFecFraAlb = new gnu.chu.controles.CCheckBox();
        cLabel5 = new gnu.chu.controles.CLabel();
        cli_codiE = new gnu.chu.camposdb.cliPanel();
        cLabel6 = new gnu.chu.controles.CLabel();
        cli_codiE1 = new gnu.chu.camposdb.cliPanel();
        cLabel7 = new gnu.chu.controles.CLabel();
        serieIniE = new gnu.chu.controles.CComboBox();
        cLabel8 = new gnu.chu.controles.CLabel();
        serieFinE = new gnu.chu.controles.CComboBox();
        cLabel9 = new gnu.chu.controles.CLabel();
        emp_codiE = new gnu.chu.controles.CLinkBox();
        cLabel10 = new gnu.chu.controles.CLabel();
        eje_numeE = new gnu.chu.controles.CTextField(Types.DECIMAL,"####9");
        cLabel11 = new gnu.chu.controles.CLabel();
        fvc_fecfraE = new gnu.chu.controles.CTextField(Types.DATE, "dd-MM-yyyy");
        cLabel12 = new gnu.chu.controles.CLabel();
        fvc_numeE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#####9");
        cLabel13 = new gnu.chu.controles.CLabel();
        tipMenIgual = new gnu.chu.controles.CComboBox();
        cli_tipfacE = new gnu.chu.controles.CComboBox();
        cPanel1 = new gnu.chu.controles.CPanel();
        cLabel14 = new gnu.chu.controles.CLabel();
        cli_zonrepE = new gnu.chu.controles.CLinkBox();
        cLabel15 = new gnu.chu.controles.CLabel();
        cli_zoncreE = new gnu.chu.controles.CLinkBox();
        cLabel16 = new gnu.chu.controles.CLabel();
        cli_activE = new gnu.chu.controles.CComboBox();
        cLabel17 = new gnu.chu.controles.CLabel();
        cli_giroE = new gnu.chu.controles.CComboBox();
        Bconsalb = new gnu.chu.controles.CButton(Iconos.getImageIcon("buscar"));
        cPanel2 = new gnu.chu.controles.CPanel();
        cLabel18 = new gnu.chu.controles.CLabel();
        numAlbE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        cLabel19 = new gnu.chu.controles.CLabel();
        impAlbE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---,---,--9.99");
        Bselect = new gnu.chu.controles.CButton(Iconos.getImageIcon("data-undo"));
        Baceptar = new gnu.chu.controles.CButton(Iconos.getImageIcon("check"));
        cLabel20 = new gnu.chu.controles.CLabel();
        fvc_serieE = new gnu.chu.controles.CComboBox();
        opAgrCliC = new gnu.chu.controles.CCheckBox();

        Pprinc.setLayout(null);

        jtAlb.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Pprinc.add(jtAlb);
        jtAlb.setBounds(10, 277, 532, 173);

        cLabel1.setText("De fecha Alb.");
        Pprinc.add(cLabel1);
        cLabel1.setBounds(22, 1, 71, 15);

        feciniE.setText("DD-MM-yy");
        Pprinc.add(feciniE);
        feciniE.setBounds(100, 0, 86, 17);

        cLabel2.setText("A fecha Albaran");
        Pprinc.add(cLabel2);
        cLabel2.setBounds(307, 1, 86, 15);

        fecfinE.setText("DD-MM-yy");
        Pprinc.add(fecfinE);
        fecfinE.setBounds(403, 0, 86, 17);

        cLabel3.setText("De Albarán");
        Pprinc.add(cLabel3);
        cLabel3.setBounds(22, 23, 59, 17);

        avc_numeE.setText("0");
        Pprinc.add(avc_numeE);
        avc_numeE.setBounds(100, 23, 55, 17);

        cLabel4.setText("A Albaran");
        Pprinc.add(cLabel4);
        cLabel4.setBounds(177, 23, 53, 17);

        avc_numeE1.setText("0");
        Pprinc.add(avc_numeE1);
        avc_numeE1.setBounds(234, 23, 55, 17);

        opFecFraAlb.setText("Fec.Fra igual a la de Albaran");
        Pprinc.add(opFecFraAlb);
        opFecFraAlb.setBounds(306, 23, 200, 17);

        cLabel5.setText("De Cliente");
        Pprinc.add(cLabel5);
        cLabel5.setBounds(20, 45, 71, 18);
        Pprinc.add(cli_codiE);
        cli_codiE.setBounds(100, 45, 409, 18);

        cLabel6.setText("A Cliente");
        Pprinc.add(cLabel6);
        cLabel6.setBounds(22, 70, 74, 18);
        Pprinc.add(cli_codiE1);
        cli_codiE1.setBounds(100, 70, 409, 18);

        cLabel7.setText("De serie Alb.");
        Pprinc.add(cLabel7);
        cLabel7.setBounds(22, 100, 70, 17);
        Pprinc.add(serieIniE);
        serieIniE.setBounds(102, 100, 47, 17);

        cLabel8.setText("A serie Alb.");
        Pprinc.add(cLabel8);
        cLabel8.setBounds(153, 100, 64, 17);
        Pprinc.add(serieFinE);
        serieFinE.setBounds(219, 100, 46, 17);

        cLabel9.setText("Empresa");
        Pprinc.add(cLabel9);
        cLabel9.setBounds(22, 125, 74, 18);

        emp_codiE.setAncTexto(30);
        Pprinc.add(emp_codiE);
        emp_codiE.setBounds(100, 125, 270, 18);

        cLabel10.setText("Ejercicio");
        Pprinc.add(cLabel10);
        cLabel10.setBounds(400, 125, 46, 18);

        eje_numeE.setText("0");
        Pprinc.add(eje_numeE);
        eje_numeE.setBounds(450, 125, 55, 18);

        cLabel11.setText("Fecha Facturas");
        Pprinc.add(cLabel11);
        cLabel11.setBounds(22, 154, 82, 17);

        fvc_fecfraE.setText("DD-MM-yy");
        Pprinc.add(fvc_fecfraE);
        fvc_fecfraE.setBounds(108, 154, 87, 17);

        cLabel12.setText("Numero Factura");
        Pprinc.add(cLabel12);
        cLabel12.setBounds(352, 154, 92, 17);

        fvc_numeE.setText("0");
        Pprinc.add(fvc_numeE);
        fvc_numeE.setBounds(454, 154, 55, 17);

        cLabel13.setText("Tipo Facturacion");
        Pprinc.add(cLabel13);
        cLabel13.setBounds(10, 179, 90, 17);
        Pprinc.add(tipMenIgual);
        tipMenIgual.setBounds(104, 179, 176, 17);
        Pprinc.add(cli_tipfacE);
        cli_tipfacE.setBounds(286, 179, 140, 17);

        cPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Discriminadores Clientes"));
        cPanel1.setLayout(null);

        cLabel14.setText("Agente/Zona");
        cPanel1.add(cLabel14);
        cLabel14.setBounds(6, 15, 80, 17);

        cli_zonrepE.setAncTexto(30);
        cPanel1.add(cli_zonrepE);
        cli_zonrepE.setBounds(100, 20, 145, 17);

        cLabel15.setText("Zona/Cdto");
        cPanel1.add(cLabel15);
        cLabel15.setBounds(20, 35, 70, 17);

        cli_zoncreE.setAncTexto(30);
        cPanel1.add(cli_zoncreE);
        cli_zoncreE.setBounds(100, 40, 145, 17);

        cLabel16.setText("Activo");
        cPanel1.add(cLabel16);
        cLabel16.setBounds(250, 20, 34, 17);
        cPanel1.add(cli_activE);
        cli_activE.setBounds(290, 20, 88, 17);

        cLabel17.setText("Giros");
        cPanel1.add(cLabel17);
        cLabel17.setBounds(250, 40, 30, 17);
        cPanel1.add(cli_giroE);
        cli_giroE.setBounds(290, 40, 86, 17);

        Pprinc.add(cPanel1);
        cPanel1.setBounds(10, 202, 390, 60);

        Bconsalb.setText("Buscar");
        Pprinc.add(Bconsalb);
        Bconsalb.setBounds(420, 240, 125, 19);

        cPanel2.setLayout(null);

        cLabel18.setText("Num. Albaranes");
        cPanel2.add(cLabel18);
        cLabel18.setBounds(0, 1, 87, 15);

        numAlbE.setEditable(false);
        numAlbE.setText("0");
        cPanel2.add(numAlbE);
        numAlbE.setBounds(91, 0, 55, 17);

        cLabel19.setText("Importe");
        cPanel2.add(cLabel19);
        cLabel19.setBounds(156, 1, 44, 15);

        impAlbE.setEditable(false);
        impAlbE.setText("0");
        cPanel2.add(impAlbE);
        impAlbE.setBounds(204, 0, 65, 17);

        Bselect.setText("Inv. Selecion");
        cPanel2.add(Bselect);
        Bselect.setBounds(275, 0, 124, 19);

        Baceptar.setText("Gen Fras");
        cPanel2.add(Baceptar);
        Baceptar.setBounds(405, 0, 124, 19);

        Pprinc.add(cPanel2);
        cPanel2.setBounds(10, 460, 530, 25);

        cLabel20.setText("Serie Factura");
        Pprinc.add(cLabel20);
        cLabel20.setBounds(269, 100, 72, 17);

        fvc_serieE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fvc_serieEActionPerformed(evt);
            }
        });
        Pprinc.add(fvc_serieE);
        fvc_serieE.setBounds(345, 100, 164, 17);

        opAgrCliC.setText("Agr.por Cli.Fact.");
        opAgrCliC.setToolTipText("Agrupar por clientes de facturacion");
        Pprinc.add(opAgrCliC);
        opAgrCliC.setBounds(210, 154, 120, 17);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fvc_serieEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fvc_serieEActionPerformed
       cambiaEmpresa();

    }//GEN-LAST:event_fvc_serieEActionPerformed
    @Override
public void iniciarVentana() throws Exception
  {

    if (ctUp.getAutoCommit())
    { // No quiero que el cursor de Update Sea Autocommit
      ctCom = new conexion(EU.usuario, EU.password,
                           EU.driverDB,
                           EU.addressDB);
      ctCom.setAutoCommit(false);
      if (EU.catalog != null)
        ctCom.setCatalog(EU.catalog);

      stUp = ctCom.createStatement();
    }
    else
      ctCom=ctUp;

    dtAlb=new DatosTabla(ct);
    dtAdd=new DatosTabla(ct);

    ctUp=new conexion(EU.usuario, EU.password,
                        EU.driverDB,
                        EU.addressDB);
      if (EU.catalog != null)
        ctUp.setCatalog(EU.catalog);

    ctUp.setAutoCommit(false);
    pdclien.llenaTipoFact(cli_tipfacE,false);
   
    cli_codiE.iniciar(dtStat, this, vl, EU);
    cli_codiE1.iniciar(dtStat, this, vl, EU);
    fecfinE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
    tipMenIgual.addItem("Menor o Igual","M");
    tipMenIgual.addItem("Igual","I");
    ArrayList v=new ArrayList();
    v.add("Sel"); // 0
    v.add("Albaran"); // 1
    v.add("Fec.Alb.");  //2
    v.add("Cliente"); // 3
    v.add("Nomb.Cliente"); // 4
    v.add("Importe"); // 5
    jtAlb.setCabecera(v);
    jtAlb.setAnchoColumna(new int[]{30,99,65,46,169,80});
    jtAlb.setAlinearColumna(new int[]{1,0,1,0,0,2});
    jtAlb.setFormatoColumna(0,"BSN");
    s = "SELECT emp_codi,emp_nomb FROM v_empresa ORDER BY emp_codi";
    dtCon1.select(s);
    emp_codiE.addDatos(dtCon1);
    emp_codiE.setValorInt(EU.em_cod);
    eje_numeE.setValorDec(EU.ejercicio);
    fvc_fecfraE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
    eje_numeE.resetCambio();
    pdnumeracion.llenaSeriesAlbVen(true,serieIniE,false,false);
    pdnumeracion.llenaSeriesAlbVen(true,serieFinE,false,false);
    serieIniE.resetTexto();
    serieFinE.resetTexto();
    fvc_serieE.addItem("Automatico","*");
    pdnumeracion.llenaSeriesFraVen(fvc_serieE);
    cli_zonrepE.setFormato(Types.CHAR, "XX", 2);
    cli_zonrepE.texto.setMayusc(true);
    gnu.chu.anjelica.pad.pdconfig.llenaDiscr(dtCon1,cli_zonrepE,"CR",EU.em_cod);
    cli_zonrepE.addDatos("**", "TODOS");

    cli_zoncreE.setFormato(Types.CHAR, "XX", 2);
    cli_zoncreE.texto.setMayusc(true);
    gnu.chu.anjelica.pad.pdconfig.llenaDiscr(dtCon1,cli_zoncreE,"CC",EU.em_cod);

    cli_zoncreE.addDatos("**", "TODOS");

    cli_activE.addItem("Todos", "%");
    cli_activE.addItem("Si", "S");
    cli_activE.addItem("No", "N");
//    cli_activE.setModificable(true);

    cli_giroE.addItem("Todos", "%");
    cli_giroE.addItem("Si", "S");
    cli_giroE.addItem("No", "N");

    cambiaEmpresa();
    activarEventos();
    feciniE.setText("01-"+Fecha.getFechaSys("MM-yyyy"));
    activar(false);
    feciniE.requestFocus();
  }

  void activarEventos()
  {
    Bselect.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Bselect_actionPerformed();
      }
    });
    Bconsalb.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Bconsalb_actionPerformed();
      }
    });
    Baceptar.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Baceptar_actionPerformed();
      }
    });
    emp_codiE.addCambioListener(new CambioListener()
    {
      public void cambio(CambioEvent event)
      {
        cambiaEmpresa();
      }
    });
    eje_numeE.addFocusListener(new FocusAdapter()
    {
      public void focusLost(FocusEvent e)
      {
        if (e.isTemporary())
          return;
        if (eje_numeE.hasCambio())
        {
          eje_numeE.resetCambio();
          cambiaEmpresa();
        }
      }
    });
    jtAlb.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent e)
      {
        if (!jtAlb.isEnabled())
          return;
        if (jtAlb.getSelectedColumn()==0)
          jtAlb.setValor(new Boolean(! jtAlb.getValBoolean(0)),0);
        calcTotales();
      }
    });
  }

  boolean cambiaEmpresa()
  {
    try
    {
      s="SELECT * FROM v_numerac WHERE emp_codi = "+emp_codiE.getValorInt()+
          " and eje_nume = "+eje_numeE.getValorInt();
      if (! dtStat.select(s))
      {
        msgBox("No encontrado NUMERACION Para esta fecha y Ejercicio");
        return false;
      }
      numFra[0]=dtStat.getInt("num_factur")+1;
      numFra[1]=dtStat.getInt("num_factub")+1;
      numFra[2]=dtStat.getInt("num_factuc")+1;
      numFra[3]=dtStat.getInt("num_factud")+1;
      numFraB=numFra[getNumFra(fvc_serieE.getValor())];
      fvc_numeE.setValorDec(numFraB);
      return true;
    } catch (Exception k)
    {
      Error("Error en cambiaEmpresa: ",k);
      return false;
    }
  }
  void Baceptar_actionPerformed()
  {
    try  {
      if (!checkCond())
        return;
      if (numAlbE.getValorDec() == 0)
      {
        mensajeErr("Escoja al menos un albarán para facturar");
        return;
      }
      if (fvc_numeE.getValorInt() != numFraB && fvc_serieE.getValor().equals("*"))
      {
          mensajeErr("No puede especificar el numero de Fra. Si la serie de Fra. es Automatica");
          fvc_serieE.requestFocus();
          return;
      }
      if (fvc_numeE.getValorInt() > numFraB)
      {
        mensajeErr("No. Factura NO puede ser Superior al guia");
        fvc_numeE.setValorDec(numFra[0]);
        fvc_numeE.requestFocus();
        return;
      }
      numDecPrecio=pdconfig.getNumDecimales(emp_codiE.getValorInt(), dtStat);
    } catch (Exception k)
    {
      Error("Error al comprobar condiciones",k);
      return;
    }
    new miThread("")
    {
      public void run()
      {
        genFact();
        activar(false);
      }
    };
  }

  private boolean checkCond() throws SQLException
  {

    if (feciniE.getError() || feciniE.isNull())
    {
      mensajeErr("Introduzca Fecha Inicio");
      feciniE.requestFocus();
      return false;
    }
    if (fecfinE.getError() || fecfinE.isNull())
    {
      mensajeErr("Introduzca Fecha Final");
      fecfinE.requestFocus();
      return false;
    }
    if (fvc_fecfraE.getError() || fvc_fecfraE.isNull())
    {
      mensajeErr("Introduzca Fecha de Factura");
      fecfinE.requestFocus();
      return false;
    }
   s=pdejerci.checkFecha(dtStat, eje_numeE.getValorInt(), emp_codiE.getValorInt(),fvc_fecfraE.getText());
   if (s!=null)
    {
      msgBox(s);
      fvc_fecfraE.requestFocus();
      return false;
    }

    return true;
  }
  /**
   * Generar Facturas. rutina Final para generar las facturas sobre los albaranes
   * selecionados.
   */
  void genFact()
  {

    mensaje("Espere, por favor ... Generando FACTURAS");
    Thread.currentThread().setPriority(Thread.MAX_PRIORITY-1);
    this.setEnabled(false);
    
    try {

      s = "create  temp table nofact (avc_ano int ,emp_codi int ,avc_serie char(1),avc_nume int)";
      dtAlb.executeUpdate(s);
      int nRow=jtAlb.getRowCount();
      int np,npAnt,avcAno,empCodi,avcNume=0;
      
      avcSerie="";
      String numAlb;
      
      for (int n=0;n<nRow;n++)
      {
        if (! jtAlb.getValBoolean(n,0))
        {
          numAlb=jtAlb.getValString(n,1);
          np=numAlb.indexOf("-",0);
          empCodi=Integer.parseInt(numAlb.substring(0,np));
          npAnt=np+1;
          np=numAlb.indexOf("-",npAnt);
          avcAno=Integer.parseInt(numAlb.substring(npAnt,np));
          npAnt=np+1;
          np=numAlb.indexOf("-",npAnt);
          avcSerie = numAlb.substring(npAnt, np);
          npAnt = np + 1;
          avcNume = Integer.parseInt(numAlb.substring(npAnt));
          dtAlb.executeUpdate("insert into nofact values("+avcAno+","+empCodi+",'"+avcSerie+"',"+avcNume+")");
        }
      }
      dtAlb.commit();
      s=getStrSql(true);

      if (!dtAlb.select(s))
      {
//        debug("1. select :"+dtAlb.getStrSelect());
        msgBox("NO encontrados Albaranes para facturar");
        dtAlb.executeUpdate("drop table nofact");
        dtAlb.commit();
        this.setEnabled(true);
        return;
      }
      swAutNumFra=true; // Indica si seguir el Numerador Automatico.
      serAuto=fvc_serieE.getValor().equals("*");
      if (!serAuto && fvc_numeE.getValorInt()!= numFraB)
      {
          swAutNumFra=false; // Se pondran num. fras. a partir del introducido
          numFraB=fvc_numeE.getValorInt();
      }
      nFras=0;
      int tipIvaPro=0;
      resetValores();
     
      do
      {       
        mensajeRapido("Tratando Albaran: "+ dtAlb.getInt("avc_nume"));
        if (cliCodi!=dtAlb.getInt(opAgrCli?"cli_codfa":"cli_codi") ||  dtoPP!=dtAlb.getDouble("avc_dtopp")
            || dtoCom!=dtAlb.getDouble("avc_dtocom") ||
            (dtAlb.getInt("cli_agralb")==0 && dtAlb.getInt("avc_nume")!=avcNume)
            || (opFecFraAlb.isSelected() && dtAlb.getInt("avc_nume")!=avcNume)
            || (serAuto && ! dtAlb.getString("avc_serie").equals(avcSerie)) )
        {
          guardaFra();
        }

        s="SELECT * FROM v_albavel  "+
            " WHERE emp_codi = "+emp_codiE.getValorInt()+
            " and avc_ano = "+eje_numeE.getValorInt()+
            " and avc_serie = '"+dtAlb.getString("avc_serie")+"'"+
            " and avc_nume = "+dtAlb.getInt("avc_nume")+
            " ORDER BY avl_numlin ";
        if (dtCon1.select(s))
        {
          s=" SELECT pro_tipiva FROM v_articulo "+
              " WHERE  pro_codi = "+dtCon1.getInt("pro_codi");
          if (! dtStat.select(s))
            throw new Exception ("Error tratando Albaran: "+dtAlb.getInt("avc_nume")+ "\n NO ENCONTRADO ARTICULO "+dtCon1.getInt("pro_codi")+" EN TABLA MAESTRA");
          tipIvaPro=dtStat.getInt("pro_tipiva");
          if (tipIva!=-1 && tipIva!=tipIvaPro)
            guardaFra(); // Tipos de Iva distintos.
          tipIva=tipIvaPro;
          do
          {
//            debug("Linea de Factura: "+nLinFra+" Albaran: "+dtAlb.getInt("avc_nume"));
            stUp.executeUpdate("update v_albavel set fvl_numlin = "+nLinFra+
                               "  where emp_codi = "+emp_codiE.getValorInt()+
                               " and avc_ano = " + eje_numeE.getValorInt() +
                               " and avc_serie = '" + dtAlb.getString("avc_serie") + "'" +
                               " and avc_nume = " + dtAlb.getInt("avc_nume") +
                               " AND avl_numlin = "+dtCon1.getInt("avl_numlin"));

            dtAdd.addNew("v_facvel");
            dtAdd.setDato("eje_nume",dtCon1.getInt("avc_ano"));
            dtAdd.setDato("emp_codi",dtCon1.getInt("emp_codi"));
            dtAdd.setDato("fvc_serie",fvcSerie);
            dtAdd.setDato("fvc_nume",fvcNumfra);
            dtAdd.setDato("fvl_numlin",nLinFra);
            dtAdd.setDato("fvl_tipdes",dtCon1.getString("avl_tipdes"));
            dtAdd.setDato("pro_codi",dtCon1.getInt("pro_codi"));
            dtAdd.setDato("fvl_nompro",dtCon1.getString("pro_nomb"));
            dtAdd.setDato("avc_nume",dtCon1.getInt("avc_nume"));
            dtAdd.setDato("avc_ano",dtCon1.getInt("avc_ano"));
            dtAdd.setDato("avc_serie",dtCon1.getString("avc_serie"));
            dtAdd.setDato("avc_fecalb",dtAlb.getDate("avc_fecalb"));
            dtAdd.setDato("fvl_prve2",dtCon1.getDouble("precioventa2"));
            dtAdd.setDato("fvl_dto2",dtCon1.getDouble("descuento2"));
            dtAdd.setDato("fvl_tireta",dtCon1.getString("tiporecargotasa"));
            dtAdd.setDato("fvl_rectas",dtCon1.getDouble("recargotasa"));
            dtAdd.setDato("fvl_reta2",dtCon1.getDouble("recargotasa2"));
            dtAdd.setDato("fvl_canti",dtCon1.getDouble("avl_canti"));
            dtAdd.setDato("fvl_dto",dtCon1.getDouble("avl_dtolin"));
            dtAdd.setDato("fvl_prven",dtCon1.getDouble("avl_prven"));
            dtAdd.update(stUp);
            nLinFra++;
          } while (dtCon1.next());
          // Vuelvo a realizar la select de Lineas para calcular el importe agrupando.
          s = "SELECT pro_codi,sum(avl_canti) as avl_canti, " +
              " sum(avl_unid) as avl_unid, avl_prven,pro_nomb as pro_nomb FROM v_albavel  " +
              " WHERE emp_codi = " + emp_codiE.getValorInt() +
              " and avc_ano = " + eje_numeE.getValorInt() +
              " and avc_serie = '" + dtAlb.getString("avc_serie") + "'" +
              " and avc_nume = " + dtAlb.getInt("avc_nume") +
             " group by pro_codi,avl_prven,pro_nomb ";
          dtCon1.select(s);
      
          double impL;
          do
          {
            impL=Formatear.redondea(Formatear.redondea(dtCon1.getDouble("avl_canti",true), 2) *
                        Formatear.redondea(dtCon1.getDouble("avl_prven",true),numDecPrecio),2);
            impDtCom+=MantArticulos.getInclDtoCom(dtCon1.getInt("pro_codi"), dtStat)?
                impL:0;
            impLin+=impL;
          } while (dtCon1.next());

          s = "SELECT * FROM v_albavec WHERE emp_codi = " + emp_codiE.getValorInt() +
              " and avc_ano = " + eje_numeE.getValorInt() +
              " and avc_serie = '" + dtAlb.getString("avc_serie") + "'" +
              " and avc_nume = " + dtAlb.getInt("avc_nume");
          if (dtCon1.select(s,true))
          {
            if (dtCon1.getInt("avc_cobrad")==0)
              fvcCobrad=0; // Si cualquier ALB. esta como NO cobrado Totalmente
                           // la Fra. se marcara como NO cobrada Totalmente
            fvcImpcob+=dtCon1.getDouble("avc_impcob");
            dtCon1.edit();
            dtCon1.setDato("cli_codfa", dtCon1.getInt("cli_codfa"));
            dtCon1.setDato("fvc_ano", eje_numeE.getValorInt());
            dtCon1.setDato("fvc_serie", fvcSerie);
            dtCon1.setDato("fvc_nume", fvcNumfra);
            dtCon1.update(stUp);
          }
          else
            throw new SQLException("NO Encontrada cabecera Albaran",s);
          s="UPDATE v_cobros set cob_serie = 'A', fvc_serie ='"+fvcSerie+"'"+
                  ", fac_nume = "+fvcNumfra+
              ", alb_nume = 0 "+
              ", cob_albar = '"+dtAlb.getString("avc_serie")+
                Formatear.format(dtAlb.getInt("avc_nume"),"999999")+"'"+
              " WHERE emp_codi = "+emp_codiE.getValorInt()+
              " and cob_serie = '"+dtAlb.getString("avc_serie")+"'"+
              " AND cob_anofac = "+eje_numeE.getValorInt()+
              " AND alb_nume = "+ dtAlb.getInt("avc_nume");
          stUp.executeUpdate(dtAdd.getStrSelect(s));
        }
      } while (dtAlb.next());

      guardaFra();
      if ( swAutNumFra)
      {
            s = "UPDATE v_numerac SET  num_factur= " + (numFra[0]-1) +
                    ", num_factub="+(numFra[1]-1)+
                    " , num_factuc="+(numFra[2]-1)+
                    " , num_factud="+(numFra[3]-1)+
              " WHERE emp_codi = " + emp_codiE.getValorInt() +
              " and eje_nume = " + eje_numeE.getValorInt();
            stUp.executeUpdate(dtAdd.getStrSelect(s));
      }
      mensajeErr(nFras+" Facturas ... GENERADAS");
      mensaje("");
      dtAlb.executeUpdate("drop table nofact");
      dtAlb.commit();
      ctCom.commit();
      cambiaEmpresa();
      this.setEnabled(true);
    } catch (Exception k)
    {
      Error("Error en Facturacion",k);
    }
  }

  private String getStrSql()
  {
    return getStrSql(false);
  }
  private String getStrSql(boolean exclAlb)
  {
    String s1= "SELECT c.*,cl.cli_codfa,cl.cli_exeiva,cl.cli_agralb,cl.cli_recequ,"+
        " cl.cli_giro,cl.cli_dipa1,cl.cli_dipa2,cl.fpa_codi,cl.div_codi,cl.cli_gener, "+
        " ban_codi,cli_baofic,cli_badico,cli_bacuen,cl.cli_nomb,c.avc_impalb "+
        " FROM v_albavec as c,clientes cl "+
        " WHERE c.emp_codi = " +   emp_codiE.getValorInt() +
        " AND c.avc_ano = " + eje_numeE.getValorInt() +
        " and c.avc_fecalb >= to_date('" + feciniE.getText() +"','dd-MM-yyyy')" +
        " and c.avc_fecalb <= to_date('" + fecfinE.getText() +"','dd-MM-yyyy')" +
        (cli_codiE.getValorInt() == 0 ? "" : " and c.cli_codfa >= " + cli_codiE.getValorInt()) +
        (cli_codiE1.getValorInt() == 0 ? "" :" and c.cli_codfa <= " + cli_codiE1.getValorInt()) +
        (avc_numeE.getValorInt()==0?"": " and avc_nume >= "+avc_numeE.getValorInt())+
        (avc_numeE1.getValorInt()==0?"": " and avc_nume <= "+avc_numeE1.getValorInt())+
        " and c.avc_confo != 0 " + // Albaranes Conformes (A facturar en Mant. Albaranes)
        " and c.fvc_nume = 0 "+ // Sin facturar
        " and c."+(opAgrCliC.isSelected()?"cli_codfa":"cli_codi")+" = cl.cli_codi "+
        (cli_tipfacE.getValor().equals("S")? " AND cl.cli_tipfac = 'S'":"")+
        (cli_tipfacE.getValor().equals("Q")?
         tipMenIgual.getValor().equals("M")?" AND (cl.cli_tipfac = 'S' OR cl.cli_tipfac = 'Q')":
         " AND cl.cli_tipfac = 'Q'":"")+
        (cli_tipfacE.getValor().equals("M")?
        tipMenIgual.getValor().equals("M")?" ":
        " AND cl.cli_tipfac = 'M'":"")+
        " AND avc_serie >= '"+serieIniE.getValor()+"'"+
        " AND avc_serie <= '"+serieFinE.getValor()+"'"+
        (! cli_zonrepE.isNull(true) && !cli_zonrepE.getText().equals("**")?
         " and cl.cli_zonrep  LIKE '"+Formatear.reemplazar(cli_zonrepE.getText(),"*","%")+"'":"")+
        (! cli_zoncreE.isNull(true) && !cli_zoncreE.getText().equals("**")?
         " and cl.cli_zoncre  LIKE '"+Formatear.reemplazar(cli_zoncreE.getText(),"*","%")+"'":"")+
        (!cli_activE.getValor().equals("%")?
         " and cl.cli_activ = '"+cli_activE.getValor()+"'":"")+
        (!cli_giroE.getValor().equals("%")?
         " and cl.cli_giro = '"+cli_giroE.getValor()+"'":"")+
        " and cl.cli_tipfac != 'N' "+
        " and exists (select avc_ano  from v_albavel as l WHERE l.emp_codi = "+emp_codiE.getValorInt()+
        " and l.avc_ano = "+eje_numeE.getValorInt()+
        " and l.avc_serie = c.avc_serie "+
        " and l.avc_nume = c.avc_nume "+
        " and l.avl_canti != 0) "+
        (exclAlb?" and not exists (select avc_nume from nofact WHERE nofact.emp_codi = c.emp_codi "+
         " and nofact.avc_serie = c.avc_serie and nofact.avc_ano = "+eje_numeE.getValorInt()+
         " and nofact.avc_nume = c.avc_nume) ":"")+
         " ORDER BY "+
        (opFecFraAlb.isSelected()?
        (opAgrCliC.isSelected()?"cl.cli_codfa":"cl.cli_codi")+",c.avc_fecalb ":
        (opAgrCliC.isSelected()?"cl.cli_codfa":"cl.cli_codi")+" ,avc_dtopp,avc_dtocom,c.avc_serie,c.avc_nume");
 //     debug("select :"+s1);
      return s1;
  }

  void guardaFra() throws Exception
  {
    if (impLin==0)
    { // Si la suma de todos las lineas de los albaranes es 0 NO genero las facturas
      // Borro las lineas de Fra.
      debug("0. Numero Factura "+fvcNumfra+" Anulada");
      s="DELETE FROM v_facvel WHERE eje_nume = "+eje_numeE.getValorInt()+
          " and emp_codi = "+emp_codiE.getValorInt()+
          " and fvc_serie = '"+fvcSerie+"'"+
          " and fvc_nume = "+fvcNumfra;
      dtAdd.executeUpdate(s,stUp);
      debug("1. Numero Factura "+fvcNumfra+" Anulada");

      // Quito la relacion a la fra en los albaranes.
      s = "UPDATE v_albavec SET fvc_ano = 0, fvc_nume = 0 "+
         " WHERE emp_codi = " + emp_codiE.getValorInt() +
         " AND fvc_ano = " + eje_numeE.getValorInt() +
         " AND fvc_serie = '"+fvcSerie+"'"+
         " AND fvc_nume = " + fvcNumfra;
      dtAdd.executeUpdate(s,stUp);
//      debug("2. Numero Factura "+numFra+" Anulada");
      resetValores();
      return;
    }
    fvcFecfra=opFecFraAlb.isSelected()?fecAlb:fvc_fecfraE.getText();
    nFras++;
    dtAdd.addNew("v_facvec");
    dtAdd.setDato("fvc_ano", eje_numeE.getValorInt());
    dtAdd.setDato("emp_codi", emp_codiE.getValorInt());
    dtAdd.setDato("fvc_serie",fvcSerie );
    dtAdd.setDato("fvc_nume", fvcNumfra);
    dtAdd.setDato("cli_codi", cliCodi);
    dtAdd.setDato("fvc_clinom",  avcClinom);
    dtAdd.setDato("fvc_fecfra", fvcFecfra,"dd-MM-yyyy");
    dtAdd.setDato("fvc_sumlin", impLin);
    dtAdd.setDato("fpa_codi", fpaCodi);
    dtAdd.setDato("fvc_modif", "A");

    double impDtoPP = 0,impDtoCom=0;
    double impIva = 0, impReq = 0;
    //double dtos = dtoCom + dtoPP;

    if (dtoPP != 0)
      impDtoPP = Formatear.redondea(impLin * dtoPP / 100, numDec);
    if (dtoCom != 0)
      impDtoCom = Formatear.redondea(impDtCom* dtoCom / 100, numDec);
    double impBim = Formatear.redondea(impLin - impDtoPP - impDtoCom, numDec);
    DatosIVA datoIva=null;
    if (incIva)
    {
      datoIva= MantTipoIVA.getDatosIva(dtStat, tipIva,Formatear.getDate(fvcFecfra,"dd-MM-yyyy"));
      
      if (datoIva!=null)
      {
        impIva = Formatear.redondea(impBim * datoIva.getPorcIVA() /
                                    100, numDec);
        if (cliRecequ != 0)
          impReq = Formatear.redondea(impBim * datoIva.getPorcREQ() /
                               100, numDec);
      }
      else
        throw new SQLException(" Tipo de Iva " + tipIva + " NO ENCONTRADO");
    }
    double impFra = Formatear.redondea(impBim + impIva + impReq,numDec) ;

    dtAdd.setDato("fvc_trasp", 0); // Fact. Trasp. A contabilidad
    dtAdd.setDato("fvc_sumtot", impFra);
    dtAdd.setDato("fvc_impiva", impIva);
    dtAdd.setDato("fvc_imprec", impReq);
    dtAdd.setDato("fvc_cobrad", fvcCobrad);
    dtAdd.setDato("fvc_cobtra", 0); // Cobro traspasado
    dtAdd.setDato("fvc_impres", 0); // Factura Impresa
    dtAdd.setDato("div_codi", divCodi); // Divisa
    dtAdd.setDato("fvc_impcob", Formatear.redondea(fvcImpcob, numDec));
    dtAdd.setDato("fvc_basimp", impBim);
    dtAdd.setDato("fvc_dtopp", dtoPP);
    dtAdd.setDato("fvc_dtocom", dtoCom);
    dtAdd.setDato("fvc_dtootr", 0);
    if (incIva)
    {
      dtAdd.setDato("fvc_poriva", datoIva.getPorcIVA());
      dtAdd.setDato("fvc_porreq",
                    (recEqu != 0 ? datoIva.getPorcREQ() : 0));
    }
    else
    {
      dtAdd.setDato("fvc_poriva", 0);
      dtAdd.setDato("fvc_porreq",0);
    }
    dtAdd.update(stUp);
    if (esGiro)
    { // Crear Recibos en tabla v_recibo
      generaRecibos(fpaCodi,impFra);
    }
    if (swAutNumFra)
      numFra[getNumFra(fvcSerie)]++;
    else
      numFraB++;
    resetValores();
  }
  int getNumFra(String serie)
  {
      if (serie.equals("1") || serie.equals("*"))
        return 0;
      if (serie.equals("B"))
        return 1;
      if (serie.equals("C"))
        return 2;
      return 3;
  }
  private void generaRecibos(int fpaCodi,double impFra) throws Exception
  {
    s="SELECT * from v_forpago WHERE fpa_codi = "+fpaCodi;
   if (! dtStat.select(s))
     throw new Exception("Forma de PAGO: " + fpaCodi + " NO ENCONTRADA");
   int nVtos = clFactCob.calDiasVto(dtStat, cliDipa1,cliDipa2, 0, fvcFecfra);
   double impGiros=Formatear.Redondea(impFra/nVtos,numDec);
   for (int n = 0; n < nVtos; n++)
   {
     dtAdd.addNew("v_recibo");
     dtAdd.setDato("eje_nume",  eje_numeE.getValorInt());
     dtAdd.setDato("emp_codi",  emp_codiE.getValorInt());
     dtAdd.setDato("fvc_nume", fvcNumfra);
     dtAdd.setDato("fvc_serie", fvcSerie);
     dtAdd.setDato("rec_nume", n + 1);
     dtAdd.setDato("rec_fecvto", clFactCob.diasVto[n], "dd-MM-yyyy");
     dtAdd.setDato("rem_ejerc", 0);  // Ejercicio de Remesa
     dtAdd.setDato("rem_codi", 0);  // Codigo de Remesa
     dtAdd.setDato("bat_codi", 0);  // Banco de Remesa
     dtAdd.setDato("rec_emitid", 0); // Emitido (0=NO, -1 SI)
     dtAdd.setDato("rec_remant", 0);
     dtAdd.setDato("rec_recagr", 0);
     dtAdd.setDato("rec_impor2", 0);
     dtAdd.setDato("rec_import", (n+1==nVtos)?impFra:impGiros);
     dtAdd.setDato("ban_codi", banCodi);
     dtAdd.setDato("rec_baofic", banOfic);
     dtAdd.setDato("rec_badico", banDico);
     dtAdd.setDato("rec_bacuen", banCuen);
     dtAdd.update(stUp);
     impFra-=impGiros;
   }

  }
  void resetValores() throws Exception
  {
    opAgrCli=opAgrCliC.isSelected();
    tipIva=-1;
    impLin=0;
    impDtCom=0;
    cliCodi=dtAlb.getInt(opAgrCli?"cli_codfa":"cli_codi");
    avcClinom=dtAlb.getInt("cli_gener")==0?null:dtAlb.getString("avc_clinom");
    recEqu=dtAlb.getInt("cli_recequ");
    fecAlb=dtAlb.getFecha("avc_fecalb","dd-MM-yyyy");
    avcNume=dtAlb.getInt("avc_nume");
    dtoPP=dtAlb.getDouble("avc_dtopp");
    dtoCom=dtAlb.getDouble("avc_dtocom");
    divCodi=dtAlb.getInt("div_codi");
    esGiro=dtAlb.getString("cli_giro").equals("S");
    fpaCodi=dtAlb.getInt("fpa_codi");
    cliDipa1=dtAlb.getInt("cli_dipa1");
    cliDipa2=dtAlb.getInt("cli_dipa2");

    banCodi=dtAlb.getInt("ban_codi");
    banOfic=dtAlb.getInt("cli_baofic");
    banDico=dtAlb.getInt("cli_badico");
    banCuen=dtAlb.getDouble("cli_bacuen");
    avcSerie=dtAlb.getString("avc_serie");
    fvcSerie=serAuto?avcSerie:fvc_serieE.getValor();
    fvcSerie=fvcSerie.equals("A")?"1":fvcSerie;
    if (swAutNumFra)
      fvcNumfra=numFra[getNumFra(fvcSerie)];
    else
      fvcNumfra=numFraB;
    nLinFra=1;
    incIva=dtAlb.getInt("cli_exeiva")==0 && emp_codiE.getValorInt()<90;
    cliRecequ=dtAlb.getInt("cli_recequ");
    fvcCobrad=-1; // TODO cobrado. POR defecto = NO
    fvcImpcob=0; // Importe Cobrado
    lkAlb.setDatosTabla(dtAlb);
  }
  public void matar(boolean cerrarConexion)
  {
    super.matar(cerrarConexion);
    try
    {
      if (cerrarConexion)
      {
        if (ctCom.isConectado())
        {
          ctCom.rollback();
          if (ctCom != null)
            ctCom.close();
        }
      }
    }
    catch (Exception k)
    {
      SystemOut.print(k);
    }
  }

  void Bselect_actionPerformed()
  {
    int nRow = jtAlb.getRowCount();

    for (int n = 0; n < nRow; n++)
      jtAlb.setValor(new Boolean(!jtAlb.getValBoolean(n, 0)), n, 0);
    calcTotales();

  }
  void Bconsalb_actionPerformed()
  {
    try {
      if (!checkCond())
        return;
    } catch (Exception k)
    {
      Error("Error al comprobar condiciones de facturaci�n",k);
      return;
    }
    new miThread("")
    {
      public void run()
      {
        consAlb();
      }
    };
  }

  void consAlb()
  {
    mensaje("Espere, por favor .. BUSCANDO ALBARANES A CONSULTAR");
    this.setEnabled(false);
    s=getStrSql();
    try {
      jtAlb.removeAllDatos();
      activar(false);
      if (!dtAlb.select(s))
      {
        msgBox("NO encontrados Albaranes para facturar");
        mensaje("");
        this.setEnabled(true);
        return;
      }
      jtAlb.panelG.setVisible(false);

      do
      {
        ArrayList v=new ArrayList();
        v.add(true);
        v.add(dtAlb.getString("emp_codi")+"-"+
                     dtAlb.getInt("avc_ano")+"-"+
                     dtAlb.getString("avc_serie")+"-"+
                     dtAlb.getString("avc_nume"));
        v.add(dtAlb.getFecha("avc_fecalb","dd-MM-yy"));
        v.add(dtAlb.getString("cli_codi"));
        v.add(dtAlb.getString("cli_nomb"));
        v.add(Formatear.format(dtAlb.getString("avc_impalb"),"----,--9.99"));
        jtAlb.addLinea(v);

      } while (dtAlb.next());
      jtAlb.panelG.setVisible(true);
      jtAlb.requestFocusInicio();
      activar(true);
      calcTotales();
      this.setEnabled(true);
      mensajeErr("Consulta Albaranes ... REALIZADA");
      mensaje("");
    }catch (Exception k)
    {
      Error("Error al Consultar Albaranes",k);
    }
  }

  void calcTotales()
  {
    int nRow=jtAlb.getRowCount();
    int nAlb=0;
    double impAlb=0;
    for (int n=0;n<nRow;n++)
    {
      if (jtAlb.getValBoolean(n,0))
      {
        nAlb++;
        impAlb += jtAlb.getValorDec(n, 5);
      }
    }
    numAlbE.setValorInt(nAlb);
    impAlbE.setValorDec(impAlb);
  }
  void activar(boolean enab)
  {
    Baceptar.setEnabled(enab);
   Bselect.setEnabled(enab);
   jtAlb.setEnabled(enab);

  }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CButton Bconsalb;
    private gnu.chu.controles.CButton Bselect;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CTextField avc_numeE;
    private gnu.chu.controles.CTextField avc_numeE1;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel10;
    private gnu.chu.controles.CLabel cLabel11;
    private gnu.chu.controles.CLabel cLabel12;
    private gnu.chu.controles.CLabel cLabel13;
    private gnu.chu.controles.CLabel cLabel14;
    private gnu.chu.controles.CLabel cLabel15;
    private gnu.chu.controles.CLabel cLabel16;
    private gnu.chu.controles.CLabel cLabel17;
    private gnu.chu.controles.CLabel cLabel18;
    private gnu.chu.controles.CLabel cLabel19;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel20;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.controles.CLabel cLabel9;
    private gnu.chu.controles.CPanel cPanel1;
    private gnu.chu.controles.CPanel cPanel2;
    private gnu.chu.controles.CComboBox cli_activE;
    private gnu.chu.camposdb.cliPanel cli_codiE;
    private gnu.chu.camposdb.cliPanel cli_codiE1;
    private gnu.chu.controles.CComboBox cli_giroE;
    private gnu.chu.controles.CComboBox cli_tipfacE;
    private gnu.chu.controles.CLinkBox cli_zoncreE;
    private gnu.chu.controles.CLinkBox cli_zonrepE;
    private gnu.chu.controles.CTextField eje_numeE;
    private gnu.chu.controles.CLinkBox emp_codiE;
    private gnu.chu.controles.CTextField fecfinE;
    private gnu.chu.controles.CTextField feciniE;
    private gnu.chu.controles.CTextField fvc_fecfraE;
    private gnu.chu.controles.CTextField fvc_numeE;
    private gnu.chu.controles.CComboBox fvc_serieE;
    private gnu.chu.controles.CTextField impAlbE;
    private gnu.chu.controles.Cgrid jtAlb;
    private gnu.chu.controles.CTextField numAlbE;
    private gnu.chu.controles.CCheckBox opAgrCliC;
    private gnu.chu.controles.CCheckBox opFecFraAlb;
    private gnu.chu.controles.CComboBox serieFinE;
    private gnu.chu.controles.CComboBox serieIniE;
    private gnu.chu.controles.CComboBox tipMenIgual;
    // End of variables declaration//GEN-END:variables

}
