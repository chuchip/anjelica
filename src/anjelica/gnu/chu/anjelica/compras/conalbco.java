package gnu.chu.anjelica.compras;


import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import java.awt.*;
import javax.swing.BorderFactory;
import java.sql.*;
import gnu.chu.camposdb.*;
import java.awt.event.*;
import java.util.*;
import gnu.chu.Menu.*;
import gnu.chu.anjelica.pad.MantPaises;
import javax.swing.event.*;
import gnu.chu.sql.*; 
import net.sf.jasperreports.engine.*;
  
/** 
* Consulta/Listado Albaranes de Compras.
 *
 * Permite consultar las compras realizadas de un producto
 * dentro de unas fechas.
 *
 * Tambien permite delimitar por proveedor y por albaran.
 *  * <p>Copyright: Copyright (c) 2005-2016
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los términos de la Licencia Publica General de GNU según es publicada por
 *  la Free Software Foundation, bien de la versión 2 de dicha Licencia
 *  o bien (según su elección) de cualquier versión posterior.
 *  Este programa se distribuye con la esperanza de que sea útil,
 *  pero SIN NINGUNA GARANTÍA, incluso sin la garantía MERCANTIL implícita
 *  o sin garantizar la CONVENIENCIA PARA UN PROPÓSITO PARTICULAR.
 *  Véase la Licencia Pública General de GNU para más detalles.
 *  Debería haber recibido una copia de la Licencia Pública General junto con este programa.
 *  Si no ha sido así, escriba a la Free Software Foundation, Inc.,
 *  en 675 Mass Ave, Cambridge, MA 02139, EEUU.
 * @author chuchiP
 * @version 1.0
 */

public class conalbco extends ventana implements  JRDataSource
{
  DatosTabla dtCab;
  boolean inDatTraza=false;
  boolean regDesg; // Tiene Registros de Desglose (para el report)
  int cambio;
  boolean swIniRep = true;
  String prvNomb;
  ResultSet rs;
  boolean verInd=false;
  
  String s;
  int nThread=0;
  boolean brkBusq=false;
  boolean inDatoIndiv=false;
  CPanel Pprinc = new CPanel();
  CPanel Pdatcon = new CPanel();
  CLabel cLabel1 = new CLabel();
  CTextField feciniE = new CTextField(Types.DATE,"dd-MM-yy");
  CTextField fecfinE = new CTextField(Types.DATE,"dd-MM-yy");
  CLabel cLabel2 = new CLabel();
  CLabel cLabel3 = new CLabel();
  proPanel pro_codiE = new proPanel();
  CButton Bacepta = new CButton(Iconos.getImageIcon("check"));
  Cgrid jt = new Cgrid(12);
  DatosTabla dtInd;

  CLabel cLabel4 = new CLabel();
  prvPanel prv_codiE = new prvPanel();
  CLabel cLabel5 = new CLabel();
  CTextField acc_numeE = new CTextField(Types.DECIMAL,"#####9");
  CCheckBox agrupaE = new CCheckBox();
  CLabel cLabel6 = new CLabel();
  Cgrid jtInd = new Cgrid(6);
  CLabel cLabel7 = new CLabel();
  CTextField nIndE = new CTextField(Types.DECIMAL,"##9");
  CLabel kilosIndL = new CLabel();
  CTextField kilosIndE = new CTextField(Types.DECIMAL,"---,--#.99");
  CCheckBox opValora = new CCheckBox();
  CCheckBox opVerInd = new CCheckBox();
  CLabel cLabel9 = new CLabel();
  CTextField acp_painacE = new CTextField(Types.CHAR,"XX",2);
  CLabel cLabel10 = new CLabel();
  CTextField acp_paiengE = new CTextField(Types.CHAR,"XX",2);
  CPanel PTraza = new CPanel();
  CLabel cLabel11 = new CLabel();
  CTextField acp_nucrotE = new CTextField();
  CLabel cLabel12 = new CLabel();
  CTextField mat_codiE = new CTextField(Types.DECIMAL,"####9");
  CLabel cLabel13 = new CLabel();
  CTextField sde_codiE = new CTextField(Types.DECIMAL,"####9");
  CTextField acp_paisacE = new CTextField(Types.CHAR,"XX",2);
  CLabel cLabel14 = new CLabel();
  CLabel cLabel15 = new CLabel();
  CTextField acp_feccadE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel cLabel16 = new CLabel();
  CTextField acp_fecsacE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel mat_nombE = new CLabel();
  CLabel sde_nombE = new CLabel();
  CLabel pai_nacnomE = new CLabel();
  CLabel pai_engnomE = new CLabel();
  CLabel pai_sacnomE = new CLabel();
  CCheckBox opVerTraz = new CCheckBox();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CCheckBox opCanti = new CCheckBox();
  CLabel cLabel17 = new CLabel();
  prvPanel prv_codiE1 = new prvPanel();
  CCheckBox opDesPorte = new CCheckBox();
  proPanel pro_codiE1 = new proPanel();
  CLabel cLabel18 = new CLabel();
  CTextField acc_numeE1 = new CTextField(Types.DECIMAL,"#####9");
  CLabel cLabel19 = new CLabel();
  CButton Bimpri=new CButton(Iconos.getImageIcon("print"));
  CCheckBox opDifFecha = new CCheckBox();
  CComboBox opFact = new CComboBox();
  CCheckBox opIntern = new CCheckBox();
  CLabel cLabel8 = new CLabel();
  empPanel emp_codiE = new empPanel();
  CLabel cLabel20 = new CLabel();
  sbePanel sbe_codiE = new sbePanel();
  private boolean ARG_VERPRECIO=true;

  public conalbco(EntornoUsuario eu, Principal p)
  {
    this(eu,p,null);
  }
  public conalbco(EntornoUsuario eu, Principal p,Hashtable ht)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    setTitulo("Cons./List. Albaranes Compras");

    
    try
    {
      if (ht!=null)
      {
        if (ht.get("verPrecio") != null)
          ARG_VERPRECIO = Boolean.valueOf(ht.get("verPrecio").toString()).
              booleanValue();
       }
      if (jf.gestor.apuntar(this))
        jbInit();
      else
        setErrorInit(true);
    }
    catch (Exception e)
    {
        ErrorInit(e);
      setErrorInit(true);
    }
  }

  public conalbco(gnu.chu.anjelica.menu p, EntornoUsuario eu)
  {

    EU = eu;
    vl = p.getLayeredPane();
    setTitulo("Consulta Albaranes Compras");
    eje = false;

    try
    {

      jbInit();
    }
    catch (Exception e)
    {
      ErrorInit(e);     
    }
  }

  private void jbInit() throws Exception
  {
    iniciarFrame();
    this.setSize(new Dimension(760, 570));
    this.setVersion("2016-12-28");

    conecta();
    dtInd = new DatosTabla(ct);
    statusBar = new StatusBar(this);
    Bimpri.setToolTipText("Imprimir Resultados de Consulta");
    Bimpri.setPreferredSize(new Dimension(24, 24));
    Bimpri.setMinimumSize(new Dimension(24, 24));
    Bimpri.setMaximumSize(new Dimension(24, 24));

    opFact.setBounds(new Rectangle(481, 40, 73, 16));
    opIntern.setToolTipText("Incluir compras Internas ?");
    opIntern.setMargin(new Insets(0, 0, 0, 0));
    opIntern.setText("Internas");
    opIntern.setBounds(new Rectangle(4, 61, 64, 16));
    cLabel8.setText("Empresa");
    cLabel8.setBounds(new Rectangle(512, 38, 56, 16));
    emp_codiE.setBounds(new Rectangle(569, 38, 52, 16));
    opDesPorte.setMargin(new Insets(0, 0, 0, 0));
    cLabel20.setOpaque(false);
    cLabel20.setPreferredSize(new Dimension(48, 15));
    cLabel20.setText("SubEmpr");
    cLabel20.setBounds(new Rectangle(628, 38, 59, 16));
    opValora.setMaximumSize(new Dimension(75, 19));
    opValora.setMargin(new Insets(0, 0, 0, 0));
    opCanti.setMargin(new Insets(0, 0, 0, 0));
    opVerTraz.setMargin(new Insets(0, 0, 0, 0));
    opVerInd.setMargin(new Insets(0, 0, 0, 0));
    acp_painacE.setMayusculas(true);
    acp_paisacE.setMayusculas(true);
    acp_paiengE.setMayusculas(true);
    sbe_codiE.setBounds(new Rectangle(686, 38, 47, 16));
    statusBar.add(Bimpri, new GridBagConstraints(9, 0, 1, 2, 0.0, 0.0
                                                 , GridBagConstraints.EAST,
                                                 GridBagConstraints.VERTICAL,
                                                 new Insets(0, 5, 0, 0), 0, 0));
    statusBar.add(opDifFecha, new GridBagConstraints(8, 0, 1, 2, 0.0, 0.0
        , GridBagConstraints.EAST,
        GridBagConstraints.VERTICAL,
        new Insets(0, 5, 0, 0), 0, 0));

    Pprinc.setLayout(gridBagLayout1);
    Pdatcon.setBorder(BorderFactory.createLoweredBevelBorder());

    pai_sacnomE.setBounds(new Rectangle(577, 20, 151, 16));
    pai_sacnomE.setOpaque(true);
    pai_sacnomE.setBackground(Color.yellow);
    opVerTraz.setSelected(true);
    opVerTraz.setText("Ver Trazabilidad");
    opVerTraz.setBounds(new Rectangle(236, 61, 106, 16));
    PTraza.setMaximumSize(new Dimension(736, 58));
    PTraza.setMinimumSize(new Dimension(736, 58));
    PTraza.setPreferredSize(new Dimension(736, 58));
    PTraza.setBorder(BorderFactory.createLoweredBevelBorder());
    PTraza.setLayout(null);

    Pdatcon.setMaximumSize(new Dimension(739, 80));
    agrupaE.setToolTipText("Agrupar productos con el mismo precio");
    agrupaE.setMargin(new Insets(0, 0, 0, 0));
    agrupaE.setBounds(new Rectangle(68, 61, 67, 16));
    opValora.setToolTipText("Ver solo Lineas  sin valorar");
    opValora.setBounds(new Rectangle(535, 61, 96, 16));
    opCanti.setToolTipText("Ver Entradas con cantidad a cero");
    opCanti.setText("Cant. Dif. 0");
    opCanti.setBounds(new Rectangle(341, 61, 81, 16));
    cLabel17.setText("A Proveed.");
    cLabel17.setBounds(new Rectangle(366, 20, 73, 16));
    prv_codiE1.setAncTexto(50);
    prv_codiE1.setBounds(new Rectangle(438, 20, 296, 18));
    opDesPorte.setToolTipText("Descontar Portes del Precio de Compra");
    opDesPorte.setText("Descontar Portes");
    opDesPorte.setBounds(new Rectangle(421, 61, 119, 16));
    pro_codiE1.setVisibleBotonAlta(false);
    pro_codiE1.setBounds(new Rectangle(438, 2, 296, 18));
    pro_codiE1.setAncTexto(60);
    cLabel18.setText("A Producto");
    cLabel18.setBounds(new Rectangle(366, 3, 68, 16));
    cLabel19.setText("A Albaran");
    cLabel19.setBounds(new Rectangle(368, 39, 61, 16));
    Bacepta.setBounds(new Rectangle(633, 58, 105, 19));
    acc_numeE1.setBounds(new Rectangle(425, 39, 51, 16));
    acc_numeE.setBounds(new Rectangle(315, 39, 51, 16));
    cLabel5.setBounds(new Rectangle(251, 39, 61, 16));
    fecfinE.setBounds(new Rectangle(174, 39, 63, 16));
    cLabel2.setBounds(new Rectangle(128, 39, 50, 16));
    feciniE.setBounds(new Rectangle(53, 39, 63, 16));
    cLabel1.setBounds(new Rectangle(3, 39, 53, 16));
    prv_codiE.setBounds(new Rectangle(77, 20, 287, 18));
    cLabel4.setBounds(new Rectangle(3, 20, 73, 16));
    pro_codiE.setBounds(new Rectangle(77, 2, 287, 17));
    cLabel3.setBounds(new Rectangle(3, 2, 68, 16));
    acp_fecsacE.setBounds(new Rectangle(327, 39, 87, 16));
    cLabel16.setBounds(new Rectangle(239, 39, 87, 16));
    acp_feccadE.setBounds(new Rectangle(93, 39, 87, 16));
    cLabel15.setBounds(new Rectangle(5, 39, 87, 16));
    acp_paiengE.setBounds(new Rectangle(290, 22, 39, 16));
    acp_paisacE.setBounds(new Rectangle(535, 20, 39, 16));
    cLabel14.setBounds(new Rectangle(477, 20, 60, 16));
    pai_engnomE.setBounds(new Rectangle(332, 20, 143, 16));
    cLabel10.setBounds(new Rectangle(240, 20, 54, 16));
    pai_nacnomE.setBounds(new Rectangle(95, 20, 143, 16));
    acp_painacE.setBounds(new Rectangle(59, 20, 33, 16));
    cLabel9.setBounds(new Rectangle(6, 20, 55, 16));
    sde_nombE.setBounds(new Rectangle(577, 39, 151, 16));
    sde_codiE.setBounds(new Rectangle(535, 39, 39, 16));
    cLabel13.setBounds(new Rectangle(477, 39, 60, 16));
    mat_nombE.setBounds(new Rectangle(577, 2, 151, 16));
    mat_codiE.setBounds(new Rectangle(535, 2, 39, 16));
    cLabel12.setBounds(new Rectangle(477, 2, 54, 16));
    acp_nucrotE.setBounds(new Rectangle(60, 2, 338, 16));
    cLabel11.setBounds(new Rectangle(4, 2, 60, 16));
    opVerInd.setBounds(new Rectangle(136, 61, 98, 16));
    kilosIndE.setBounds(new Rectangle(475, 0, 65, 16));
    kilosIndL.setBounds(new Rectangle(435, 0, 35, 16));
    nIndE.setBounds(new Rectangle(378, 0, 42, 16));
    cLabel7.setBounds(new Rectangle(325, 0, 53, 16));

    opDifFecha.setToolTipText("Dif. Productos por Fechas");
    opDifFecha.setMargin(new Insets(0, 0, 0, 0));
    opDifFecha.setMaximumSize(new Dimension(80,24));
    opDifFecha.setMinimumSize(new Dimension(80,24));
    opDifFecha.setPreferredSize(new Dimension(80,24));

    opDifFecha.setText("Dif.Fecha");
//    opDifFecha.setBounds(new Rectangle(477, 39, 80, 17));
    opDifFecha.setSelected(true);
    PTraza.add(cLabel11, null);
    PTraza.add(acp_nucrotE, null);
    PTraza.add(acp_paiengE, null);
    PTraza.add(acp_paisacE, null);
    PTraza.add(pai_sacnomE, null);
    PTraza.add(cLabel9, null);
    PTraza.add(acp_painacE, null);
    PTraza.add(pai_nacnomE, null);
    PTraza.add(cLabel10, null);
    PTraza.add(pai_engnomE, null);
    PTraza.add(cLabel14, null);
    PTraza.add(acp_fecsacE, null);
    PTraza.add(cLabel15, null);
    PTraza.add(acp_feccadE, null);
    PTraza.add(cLabel16, null);
    PTraza.add(sde_nombE, null);
    PTraza.add(cLabel13, null);
    PTraza.add(sde_codiE, null);
    PTraza.add(mat_codiE, null);
    PTraza.add(mat_nombE, null);
    PTraza.add(cLabel12, null);
    Pdatcon.setMinimumSize(new Dimension(739, 80));
    Pdatcon.setPreferredSize(new Dimension(739, 80));
    Pdatcon.setDefButton(Bacepta);
    Pdatcon.setButton(KeyEvent.VK_F4,Bacepta);

    Pdatcon.setLayout(null);

    cLabel1.setText("De Fecha");

    cLabel2.setText("A Fecha");
    cLabel3.setText("De Producto");
    pro_codiE.setAncTexto(60);
    pro_codiE.setVisibleBotonAlta(false);
    Bacepta.setMargin(new Insets(0, 0, 0, 0));
    Bacepta.setText("Acepta(F4)");
    ArrayList v= new ArrayList();
    v.add("Fecha");  // 0
    v.add("Refer"); // 1
    v.add("Nombre Prod"); // 2
    v.add("Cantidad"); // 3
    v.add("Unidad"); // 4
    v.add("Precio"); // 5
    v.add("Prov."); // 6
    v.add("Nombre Prov"); // 7
    v.add("Albar"); // 8
    v.add("NL"); // 9
    v.add("Eje"); // 10
    v.add("Emp"); // 11

    jt.setCabecera(v);
    jt.setAnchoColumna(new int[]{79,52,166,64,64,62,48,145,60,40,40,40});
    jt.setAlinearColumna(new int[]{1,0,0,2,2,2,0,0,2,2,2,2});
    jt.setConfigurar("gnu.chu.anjelica.compras.conalbco",EU,dtStat);
    jt.setFormatoColumna(0,"dd-MM-yyyy");
    jt.setFormatoColumna(3,"---,--#.99");
    jt.setFormatoColumna(5,"---,--#.999");
    jt.setMaximumSize(new Dimension(736, 285));
    jt.setMinimumSize(new Dimension(736, 285));
    jt.setPreferredSize(new Dimension(736, 285));
    jt.setAjustarGrid(true);
    ArrayList v1= new ArrayList();
    v1.add("Refer"); // 0
    v1.add("Descripcion");
    v1.add("N. Ind"); //   2
    v1.add("Peso"); // 3
    v1.add("Fec.Cad."); // 4
    v1.add("NL."); // 5
    jtInd.setCabecera(v1);
    jtInd.setAnchoColumna(new int[]{52,166,30,50,80,30});
    jtInd.setAlinearColumna(new int[]{0,0,2,2,1,2});
    jtInd.setFormatoColumna(3,"---,--#.99");
    jtInd.setAjustarGrid(true);

    cLabel4.setText("De Proveed.");
    prv_codiE.setAncTexto(50);
    cLabel5.setText("De Albaran");
    agrupaE.setSelected(true);
    agrupaE.setText("Agrupar");
    jtInd.setMaximumSize(new Dimension(731, 166));
    jtInd.setMinimumSize(new Dimension(731, 166));
    jtInd.setPreferredSize(new Dimension(731, 166));
    cLabel7.setMaximumSize(new Dimension(54, 16));
    cLabel7.setMinimumSize(new Dimension(54, 16));
    cLabel7.setPreferredSize(new Dimension(54, 16));
    cLabel7.setText("N. Lineas");
    nIndE.setMaximumSize(new Dimension(48, 16));
    nIndE.setMinimumSize(new Dimension(48, 16));
    nIndE.setOpaque(true);
    nIndE.setPreferredSize(new Dimension(48, 16));
    nIndE.setText("");
    nIndE.setEnabled(false);
    kilosIndL.setMaximumSize(new Dimension(35, 15));
    kilosIndL.setMinimumSize(new Dimension(35, 15));
    kilosIndL.setPreferredSize(new Dimension(35, 15));
    kilosIndL.setText("Kilos");
    kilosIndE.setMaximumSize(new Dimension(89, 16));
    kilosIndE.setMinimumSize(new Dimension(89, 16));
    kilosIndE.setPreferredSize(new Dimension(89, 16));
    kilosIndE.setMaxLong(0);
    kilosIndE.setText("");
    kilosIndE.setEnabled(false);
    kilosIndE.setEjecSonido(false);
    opValora.setText("Sin Valorar");
    opVerInd.setMaximumSize(new Dimension(104, 22));
    opVerInd.setMinimumSize(new Dimension(104, 22));
    opVerInd.setPreferredSize(new Dimension(104, 22));
    opVerInd.setSelected(true);
    opVerInd.setText("Ver Individuos");
    Pprinc.setMaximumSize(new Dimension(2147483647, 2147483647));
    cLabel9.setText("Pais Nac.");
    cLabel10.setText("Pais Eng.");
    cLabel11.setText("N. Crotal");
    acp_nucrotE.setText("");
    cLabel12.setText("Matadero");
    cLabel13.setText("Sala Desp.");
    cLabel14.setText("Pais Sacr.");
    cLabel15.setText("Fec.Caducidad");
    cLabel16.setRequestFocusEnabled(true);
    cLabel16.setText("Fec.Sacrificio");
    mat_nombE.setBackground(Color.yellow);
    mat_nombE.setOpaque(true);
    mat_nombE.setText("");
    sde_nombE.setText("");
    sde_nombE.setOpaque(true);
    sde_nombE.setBackground(Color.yellow);
    acp_feccadE.setText("");
    pai_nacnomE.setText("");
    pai_nacnomE.setOpaque(true);
    pai_nacnomE.setBackground(Color.yellow);
    pai_engnomE.setText("");
    pai_engnomE.setOpaque(true);
    pai_engnomE.setBackground(Color.yellow);
    this.getContentPane().add(statusBar,BorderLayout.SOUTH);
//    Ptotal.add(nIndE, null);
    jtInd.panelBuscar.add(nIndE);

//    Ptotal.add(cLabel7, null);
    jtInd.panelBuscar.add(cLabel7);
    jtInd.panelBuscar.add(kilosIndE);
    jtInd.panelBuscar.add(kilosIndL);
//    Ptotal.add(kilosIndE, null);
//    Ptotal.add(kilosIndL, null);
    this.getContentPane().add(Pprinc,  BorderLayout.CENTER);
    Pprinc.add(jt,    new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 0, 0, 0), 0, 0));
    Pprinc.add(jtInd,   new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    Pprinc.add(PTraza,    new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 1, 0, 0), 0, 0));
    Pprinc.add(Pdatcon,   new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    Pdatcon.add(pro_codiE, null);
    Pdatcon.add(cLabel3, null);
    Pdatcon.add(pro_codiE1, null);
    Pdatcon.add(prv_codiE, null);
    Pdatcon.add(cLabel4, null);
    Pdatcon.add(prv_codiE1, null);
    Pdatcon.add(cLabel17, null);
    Pdatcon.add(cLabel18, null);
    Pdatcon.add(cLabel1, null);
    Pdatcon.add(feciniE, null);

    Pdatcon.add(fecfinE, null);
    Pdatcon.add(cLabel2, null);
    Pdatcon.add(acc_numeE1, null);
    Pdatcon.add(cLabel5, null);
    Pdatcon.add(acc_numeE, null);
    Pdatcon.add(cLabel19, null);
    Pdatcon.add(opIntern, null);
//    Pdatcon.add(opFact, null);
    Pdatcon.add(agrupaE, null);
    Pdatcon.add(opVerInd, null);
    Pdatcon.add(opVerTraz, null);
    Pdatcon.add(opCanti, null);
    Pdatcon.add(opDesPorte, null);
    Pdatcon.add(opValora, null);
    Pdatcon.add(Bacepta, null);
    Pdatcon.add(sbe_codiE, null);
    Pdatcon.add(cLabel8, null);
    Pdatcon.add(emp_codiE, null);
    Pdatcon.add(cLabel20, null);
    pro_codiE.iniciar(dtStat, this, vl, EU);
    prv_codiE.iniciar(dtStat,this,vl,EU);
    PTraza.setEnabled(false);
    prv_codiE1.iniciar(dtStat, this, vl, EU);
    pro_codiE1.iniciar(dtStat, this, vl, EU);
   }

    @Override
   public void iniciarVentana() throws Exception
   {
     dtCab=new DatosTabla(ct);
     opFact.addItem("Todos","T");
     opFact.addItem("Fact","F");
     opFact.addItem("S/Fact","S");
     emp_codiE.iniciar(dtStat,this,vl,EU);
     sbe_codiE.iniciar(dtStat,this,vl,EU);
     sbe_codiE.setFieldEmpCodi(emp_codiE.getTextField());
     sbe_codiE.setAceptaNulo(EU.getSbeCodi()==0);
     GregorianCalendar gc = new GregorianCalendar();
     gc.setTime(new java.util.Date(System.currentTimeMillis()));
     gc.add(GregorianCalendar.MONTH, -1);
     feciniE.setDate(gc.getTime());
     fecfinE.setText(Formatear.getFechaAct("dd-MM-yy"));

     activarEventos();
     jtInd.ponPanel();
     javax.swing.SwingUtilities.invokeLater(new Thread()
     {
       public void run()
       {
         pro_codiE.requestFocus();

       }
     });
   }

  void activarEventos()
  {
    Bimpri.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Bimpri_actionPerformed();
      }
    });
    opVerTraz.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        PTraza.setVisible(opVerTraz.isSelected());
        if (opVerTraz.isSelected())
          verDatTraza();
      }
    });
    Bacepta.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Bacepta_actionPerformed(e);
      }
    });
    opVerInd.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
       jtInd.setVisible(opVerInd.isSelected());
       if (jt.isVacio())
         return;
       if (!opVerInd.isSelected())
         jtInd.removeAllDatos();
       else
         verDatInd();
      }
    });
    agrupaE.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Bacepta.doClick();
      }
    });
//    jt.tableView.
    jt.tableView.getSelectionModel().addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent e)
      {
        if (e.getValueIsAdjusting() || jt.isVacio())
          return;
        verDatInd();
    }
    });
    jtInd.tableView.getSelectionModel().addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent e)
      {
        if (e.getValueIsAdjusting()) // && e.getFirstIndex() == e.getLastIndex())
          return;
        verDatTraza();
    }
    });

  }

  boolean checkCond() throws SQLException
  {
    if (!emp_codiE.controla(true))
    {
      mensajeErr("Empresa NO VALIDA");
      return false;
    }
    if (feciniE.isNull() || feciniE.getError())
    {
      mensaje("Fecha Inicio ... NO valida");
      feciniE.requestFocus();
      return false;
    }
    if (fecfinE.isNull() || fecfinE.getError())
    {
      mensajeErr("Fecha Final ... NO valida");
      fecfinE.requestFocus();
      return false;
    }
   
    if (!prv_codiE.isNull())
    {
      if (prv_codiE1.isNull())
        prv_codiE1.setText(pro_codiE.getText());
    }
    if (!prv_codiE1.isNull())
    {
      if (prv_codiE.isNull())
        prv_codiE.setText(prv_codiE1.getText());
    }

    if (!acc_numeE.isNull())
    {
      if (acc_numeE1.isNull())
        acc_numeE1.setText(acc_numeE.getText());
    }
    if (!acc_numeE1.isNull())
    {
      if (acc_numeE.isNull())
        acc_numeE.setText(acc_numeE1.getText());
    }
    return true;
  }
  void Bacepta_actionPerformed(ActionEvent e)
  {
    try {
      if (!checkCond())
        return;
    } catch (Exception k)
    {
      Error("Error al controlar campos",k);
      return;
    }
    new miThread("Consulta")
    {
            @Override
      public void run()
      {
        consultar();
      }
    };
  }
  void consultar()
  {
    this.setEnabled(false);

    mensaje("Espere, por favor.. buscando Albaranes");
    try
    {
      while (inDatTraza || inDatoIndiv )
      {
          Thread.sleep(100);
      }
    } catch (Exception k)
    {
    }
//    Formatear.verAncGrid(jt);
    if (agrupaE.isSelected())
    {
      s = "select acc_fecrec,l.pro_codi,p.pro_nomb," +
          " sum(acl_canti)  as ACL_CANTI,sum(acl_numcaj) as ACL_NUMCAJ, " +
          " acl_prcom "+(opDesPorte.isSelected()?"- acc_impokg":"")+" as acl_prcom,"+
          " c.prv_codi,pv.prv_nomb,l.acc_nume, 0 as acl_nulin,l.acc_ano,l.emp_codi  ";
    }
    else
    {
      s = "select acc_fecrec,l.pro_codi,p.pro_nomb," +
          " acl_canti  ,acl_numcaj, " +
          " acl_prcom "+(opDesPorte.isSelected()?"- acc_impokg":"")+" as acl_prcom,"+
          " c.prv_codi,pv.prv_nomb,l.acc_nume, acl_nulin,l.acc_ano,l.emp_codi  ";
    }

    s += " from v_albacoc c,v_albacol l,v_articulo p,v_proveedo pv  " +
        " where c.acc_ano = l.acc_ano " +
        " and c.emp_codi = " + emp_codiE.getValorInt() +
        (sbe_codiE.getValorInt()==0?"":" and c.sbe_codi = "+sbe_codiE.getValorInt())+
        "and c.emp_codi = l.emp_codi " +
        "and c.acc_serie = l.acc_serie " +
        " and c.acc_nume = l.acc_nume " +
        " and p.pro_codi = l.pro_codi" +
        " and c.prv_codi = pv.prv_codi " +
        (opIntern.isSelected() ? "" : " and c.acc_serie!='Y'") +
        "and c.acc_fecrec >= TO_DATE('" + feciniE.getText() + "','dd-MM-yy')" +
        " AND c.acc_fecrec <= TO_DATE('" + fecfinE.getText() + "','dd-MM-yy') " +
        (opCanti.isSelected() ? " and l.acl_canti <> 0 " : "");    
      s +=  pro_codiE.isNull()?"":" and l.pro_codi "+
          (pro_codiE1.isNull()?"":">")+
          "= "+pro_codiE.getValorInt() ;
      s +=  pro_codiE1.isNull()?"":" and l.pro_codi <="+ pro_codiE1.getValorInt() ;

    if (!prv_codiE.isNull())
      s += " AND c.prv_codi >= " + prv_codiE.getText() + "AND c.prv_codi <= " + prv_codiE1.getText();
    if (acc_numeE.getValorInt() != 0)
      s += " and c.acc_nume >= " + acc_numeE.getValorInt() + " and c.acc_nume <= " + acc_numeE1.getValorInt();
    if (opValora.isSelected())
      s += " and l.acl_prcom = 0 ";
     /**
      * Deshabilitado...
      *
     if (! opFact.getValor().equals("T"))
     {
       s += " and c.fcc_ano " + (opFact.getValor().equals("F") ? "=" : "!=") + " 0 ";
     }
*/
    if (agrupaE.isSelected())
    {
      s += " GROUP BY  acc_fecrec,l.pro_codi,p.pro_nomb, "+
          " acl_prcom,"+
          (opDesPorte.isSelected()?" acc_impokg,":"")+
          "c.prv_codi,pv.prv_nomb,l.acc_nume,l.acc_ano,l.emp_codi "+
          " ORDER BY c.acc_fecrec desc,l.pro_codi,l.acc_nume,c.prv_codi";
    }
    else
    {
      s += " ORDER BY c.acc_fecrec desc,l.pro_codi,l.acc_nume,acl_nulin,c.prv_codi";
    }
//    debug(s);
    try {
      jt.setEnabled(false);
      jt.removeAllDatos();
      if (!dtCab.select(s))
      {
        mensajeErr("No Encontrados  albaranes de compra");
        this.setEnabled(true);
        mensaje("");
        feciniE.requestFocus();
        return;
      }
      jt.setDatos(dtCab);
      jt.requestFocusInicio();
      mensajeErr("Consulta realizada ...");
      javax.swing.SwingUtilities.invokeLater(new Thread()
      {
                @Override
        public void run()
        {
          consulta1();
        }
      });


    } catch (Exception k)
    {
      Error("Error al buscar albaranes",k);
    }
  }
  /**
   * Muestra los ver los datos de los individuos de la linea selecionada en jt
   */
  void consulta1()
  {
    this.setEnabled(true);
    jt.setEnabled(true);
    if (!opVerInd.isSelected())
      return;
    inDatoIndiv = true;
    verDatInd0();
    inDatoIndiv = false;
    verDatTraza();

  }
  void verDatInd()
  {
//    verDatInd0();
    if (! opVerInd.isSelected())
      return;
    new threadcoalbco(this);

  }
  void verDatInd0()
  {
//    this.setEnabled(false);
//    jt.setEnabled(false);

    mensaje("Espere, por favor .. BUSCANDO DATOS INDIVIDUOS",false);
    String s;
    try {
      jtInd.removeAllDatos();
      nIndE.setValorDec(0);
      kilosIndE.setValorDec(0);
      if (jt.getValString(0)==null)
        return;
//      if (agrupaE.isSelected())
//        return;
      if (!agrupaE.isSelected())
        s="SELECT i.pro_codi, a.pro_nomb,acp_numind,acp_canti,acp_feccad as fecinv,i.acl_nulin "+
            " from v_albcompar as i,v_articulo  as a " +
            " WHERE i.acc_serie = 'A'" +
            " and a.pro_codi = i.pro_codi "+
            " AND i.acc_ano = "+jt.getValorInt(10)+
            " AND i.emp_codi = "+jt.getValorInt(11)+
            " AND ACC_NUME = "+jt.getValorInt(8)+
            " and acl_nulin = "+jt.getValorInt(9);
      else
        s="SELECT i.pro_codi, a.pro_nomb,acp_numind,acp_canti,acp_feccad as fecinv,l.acl_nulin "+
            " from v_albcompar as i,v_albacol as l,v_albacoc as c,v_articulo as a " +
            " WHERE i.acc_serie = 'A'" +
            " and a.pro_codi = i.pro_codi "+
            " and i.acc_serie = l.acc_serie "+
            " and i.acc_ano = l.acc_ano "+
            " and i.emp_codi = l.emp_codi "+
            " and i.acc_nume = l.acc_nume "+
            " and i.acl_nulin = l.acl_nulin "+
            " and i.acc_ano = c.acc_ano " +
            " and i.emp_codi = c.emp_codi " +
            " and i.acc_nume = c.acc_nume " +
            " and i.acc_serie = l.acc_serie "+
            " and l.emp_codi = "+jt.getValorInt(11)+
            " and l.acc_nume = "+jt.getValorInt(8)+
            " and l.pro_Codi = "+jt.getValorInt(1)+
            " AND l.acl_prcom = " + jt.getValorDec(5) +
            " and c.prv_codi = "+jt.getValorInt(6)+
            " and c.acc_fecrec = TO_DATE('"+jt.getValString(0)+"','dd-MM-yyyy') ";
      s+=" ORDER BY acp_numind";
      if (! dtInd.select(s))
      {
        mensaje("");
        return;
      }
      int nInd=0;
      double kg=0;
      jtInd.tableView.setVisible(false);
      do
      {
        if (brkBusq)
          return;
        Vector v=new Vector();
        v.addElement(dtInd.getString("pro_Codi"));
        v.addElement(dtInd.getString("pro_nomb"));
        v.addElement(dtInd.getString("acp_numind"));
        v.addElement(dtInd.getString("acp_canti"));
        v.addElement(dtInd.getFecha("fecinv","dd-MM-yyyy"));
        v.addElement(dtInd.getString("acl_nulin"));
        jtInd.addLinea(v);
        nInd++;
        kg+=dtInd.getDouble("acp_canti",true);
      } while (dtInd.next());
     nIndE.setValorDec(nInd);
     kilosIndE.setValorDec(kg);
     jtInd.tableView.setVisible(true);
     jtInd.requestFocusInicio();
    } catch (Exception k)
    {
      Error("Error al Ver Individuos",k);
    }
//    this.setEnabled(true);
    mensaje("");
    mensajeErr("DATOS INDIVIDUOS ... Mostrados",false);
  }

  void verDatTraza()
  {
    if (inDatoIndiv)
      return;
    PTraza.resetTexto();
    mat_nombE.setText("");
    sde_nombE.setText("");
    pai_sacnomE.setText("");
    pai_nacnomE.setText("");
    pai_engnomE.setText("");

    if (!opVerTraz.isSelected() || !opVerInd.isSelected() || jtInd.isVacio())
      return;
    inDatTraza = true;
    try
    {
      s = "SELECT * from v_albcompar  " +
          " WHERE acc_serie = 'A'" +
          " AND acc_ano = " + jt.getValorInt(10) +
          " AND emp_codi = " + jt.getValorInt(11) +
          " AND ACC_NUME = " + jt.getValorInt(8) +
          " and acl_nulin = " + jtInd.getValorInt(5) +
          " and acp_numind = " + jtInd.getValorInt(2) +
          " and pro_codi = " + jtInd.getValorInt(0);
      if (!dtCon1.select(s))
      {
        inDatTraza = false;
        return;
      }
      acp_nucrotE.setText(dtCon1.getString("acp_nucrot"));
      mat_codiE.setText(dtCon1.getString("mat_codi"));
      sde_codiE.setText(dtCon1.getString("sde_codi"));
      acp_painacE.setText(dtCon1.getString("acp_painac"));
      acp_paiengE.setText(dtCon1.getString("acp_engpai"));
      acp_paisacE.setText(dtCon1.getString("acp_paisac"));
      acp_feccadE.setText(dtCon1.getFecha("acp_feccad", "dd-MM-yyyy"));
      acp_fecsacE.setText(dtCon1.getFecha("acp_fecsac", "dd-MM-yyyy"));
      if (mat_codiE.getValorInt()!=0)
      {
        s = "SELECT mat_nrgsa,pai_codi FROM v_matadero m WHERE m.mat_codi = " + mat_codiE.getValorInt();
        if (dtStat.select(s))
        {
          mat_nombE.setText(dtStat.getString("mat_nrgsa"));
          s = MantPaises.getNombrePais( dtStat.getInt("pai_codi"),dtStat);
          if (s!=null)
            mat_nombE.setText(s + "-" + mat_nombE.getText());
        }
        else
          mat_nombE.setText(mat_codiE.getValorInt() + " NO ENCONTRADO");
      }
      if (sde_codiE.getValorInt()!=0)
      {
        s = "SELECT sde_nrgsa,pai_codi FROM v_saladesp m " +
            " WHERE m.sde_codi = " + sde_codiE.getValorInt();
        if (dtStat.select(s))
        {
          sde_nombE.setText(dtStat.getString("sde_nrgsa"));
          
         s = MantPaises.getNombrePais( dtStat.getInt("pai_codi"),dtStat);
          if (s!=null)
            sde_nombE.setText(s + "-" + sde_nombE.getText());
        }
        else
          sde_nombE.setText(sde_codiE.getText() + " NO ENCONTRADO");
      }
      if (!acp_painacE.isNull())
      {
        s = MantPaises.getNombrePais( acp_painacE.getText(),dtStat);
        if (s!=null)
          pai_nacnomE.setText(s);
        else
          pai_nacnomE.setText("**NO ENCONTRADO**");
      }
      if (!acp_paiengE.isNull())
      {
         s = MantPaises.getNombrePais( acp_paiengE.getText(),dtStat);
        if (s!=null)  
          pai_engnomE.setText(s);
        else
          pai_engnomE.setText("**NO ENCONTRADO**");
      }
      if (!acp_paisacE.isNull())
      {
        s = MantPaises.getNombrePais( acp_paisacE.getText(),dtStat);
        if (s!=null)  
          pai_sacnomE.setText(s);
        else
          pai_sacnomE.setText("**NO ENCONTRADO**");
      }

    }
    catch (Exception k)
    {
      Error("Error al Ver Datos Trazabilidad", k);
    }
    inDatTraza = false;
  }

  void Bimpri_actionPerformed()
  {
    try
    {
      if (!checkCond())
        return;
    }
    catch (Exception k)
    {
      Error("Error al controlar campos", k);
      return;
    }

    new miThread("Consulta")
    {
            @Override
      public void run()
      {
        listar();
      }
    };
  }

  void listar()
  {
    try
    {
      this.setEnabled(false);
//      jt.setEnabled(false);
//      jtInd.setEnabled(false);
      mensaje("Espere, por favor ... GENERANDO LISTADO");
      try
      {
        while (inDatTraza || inDatoIndiv)
        {
            Thread.sleep(100);
        }
      }
      catch (Exception k)
      {
      }

      java.util.HashMap mp = new java.util.HashMap();
      JasperReport jr;
      mp.put("acc_fecrec", feciniE.getDate());
      mp.put("acc_fecrec1", fecfinE.getDate());
      mp.put("albini", acc_numeE.getText());
      mp.put("albfin", acc_numeE1.getText());
      mp.put("prv_codi", prv_codiE.getValorInt());
      mp.put("prv_codi1", prv_codiE1.getValorInt());
      mp.put("prv_nomb", prv_codiE.getTextNomb());
      mp.put("prv_nomb1", prv_codiE1.getTextNomb());
      mp.put("pro_codi", pro_codiE.getValorInt());
      mp.put("pro_codi1",pro_codiE1.getValorInt());
      mp.put("pro_nomb", pro_codiE.getTextNomb());
      mp.put("pro_nomb1", pro_codiE1.getTextNomb());

      mp.put("incPortes",!opDesPorte.isSelected());
      mp.put("difFecha", opDifFecha.isSelected());
      String nombRep = "albcompr";
      if (opDifFecha.isSelected())
        nombRep = "albcompr1";
      jr = gnu.chu.print.util.getJasperReport(EU,  nombRep );

      if (agrupaE.isSelected())
      {
        s = "select acc_fecrec,acc_impokg,l.pro_codi," +
            " sum(acl_canti)  as ACL_CANTI,sum(acl_numcaj) as ACL_NUMCAJ, " +
            " acl_prcom " + (opDesPorte.isSelected() ? "- acc_impokg" : "") +
            " as acl_prcom," +
            " c.prv_codi,pv.prv_nomb,l.acc_nume, 0 as acl_nulin,l.acc_ano,l.emp_codi  ";
      }
      else
      {
        s = "select acc_fecrec,acc_impokg,l.pro_codi," +
            " acl_canti  ,acl_numcaj, " +
            " acl_prcom " + (opDesPorte.isSelected() ? "- acc_impokg" : "") +
            " as acl_prcom," +
            " c.prv_codi,pv.prv_nomb,l.acc_nume, acl_nulin,l.acc_ano,l.emp_codi,l.pro_nomart  ";
      }

      s += " from v_albacoc c,v_albacol l,v_proveedo pv  " +
          "  where c.emp_codi = " + EU.em_cod +
          " and c.emp_codi = "+emp_codiE.getValorInt()+
          " and l.emp_codi = " + EU.em_cod +
          " and pv.prv_codi = c.prv_codi " +
          (sbe_codiE.getValorInt()==0?"":" and c.sbe_codi = "+sbe_codiE.getValorInt())+
          (opCanti.isSelected() ? " and l.acl_canti <> 0 " : "") +
          " and c.acc_ano = l.acc_ano " +
          " and c.acc_nume = l.acc_nume " +
          " and c.acc_serie = l.acc_serie " +
          (opIntern.isSelected() ? "" : " and c.acc_serie!='Y'") +
          "and c.acc_fecrec >= TO_DATE('" + feciniE.getText() + "','dd-MM-yy')" +
          " AND c.acc_fecrec <= TO_DATE('" + fecfinE.getText() + "','dd-MM-yy') " +
          (prv_codiE.isNull() ? "" :
           " AND c.prv_codi >= " + prv_codiE.getText() + "AND c.prv_codi <= " +
           prv_codiE1.getText());
      if (!pro_codiE.isNull())
        s += " and L.pro_codi >= " + pro_codiE.getText() +
            " and L.pro_codi <= " + pro_codiE1.getText();
      if (acc_numeE.getValorInt() != 0)
        s += " and l.acc_nume >= " + acc_numeE.getValorInt() +
            " and l.acc_nume <= " + acc_numeE1.getValorInt();
      if (opValora.isSelected())
        s += " and l.acl_prcom = 0 ";
        /**
         * Deshabilitado
         *
                 if (! opFact.getValor().equals("T"))
                s+=" and c.fcc_ano "+ (opFact.getValor().equals("F")?"=":"!=")+" 0 ";
         */
      if (agrupaE.isSelected())
      {
        s += " GROUP BY  acc_fecrec,acc_impokg,l.pro_codi, " +
            " acl_prcom," +
            (opDesPorte.isSelected() ? " acc_impokg," : "") +
            "c.prv_codi,pv.prv_nomb,l.acc_nume,l.acc_ano,l.emp_codi ";
        s += " ORDER BY l.pro_codi, c.acc_fecrec desc,l.acc_nume,c.prv_codi";
//            if (opDifFecha.isSelected())
//              s+=" ORDER BY l.pro_codi, c.acc_fecrec desc,l.acc_nume,c.prv_codi";
//            else
//              s+=" ORDER BY c.acc_fecrec desc,l.pro_codi, l.acc_nume,c.prv_codi";
      }
      else
      {
        s += " ORDER BY c.acc_fecrec desc,l.pro_codi,l.acc_nume,acl_nulin,c.prv_codi";
      }

      regDesg = false;
//      debug(s);
      Statement st = ct.createStatement();
      rs = st.executeQuery(dtCon1.parseaSql(s));
      swIniRep = true;
      cambio = 0;
      new JRResultSetDataSource(rs);
      JasperPrint jp = JasperFillManager.fillReport(jr, mp, this);
      gnu.chu.print.util.printJasper(jp, EU);
      this.setEnabled(true);
      mensajeErr("Listado ... GENERADO");
      mensaje("");
    }
    catch (Exception k)
    {
      Error("Error al imprimir albaran", k);
    }
  }

  public boolean next() throws JRException
  {
    try
    {
      return rs.next();

    }
    catch (Exception k)
    {
      throw new JRException(k);
    }

  }

  public Object getFieldValue(JRField f) throws JRException
  {
      String campo=f.getName().toLowerCase();
    try
    {
      if (campo.equals("acc_fecrec"))
        return rs.getDate("acc_fecrec");
      if (campo.equals("acc_serie"))
        return "";
      if (campo.equals("prv_codi") || campo.equals("acc_ano") || campo.equals("acc_nume"))
        return new Integer(rs.getInt(campo));
      if (campo.equals("acc_impokg"))
      {
        if (ARG_VERPRECIO)
          return new Double(rs.getDouble("acc_impokg"));
        else
          return new Double(0);
      }
      if (campo.equals("prv_nomb"))
        return rs.getString(campo);
      // Datos de Lineas
      if (campo.equals("pro_nomb"))
      {
        if ( agrupaE.isSelected() ||  rs.getString("pro_nomart").trim().equals(""))
        {
          s="SELECT pro_nomb FROM v_articulo WHERE pro_codi = "+rs.getInt("pro_codi");
          if (dtStat.select(s))
            return dtStat.getString("pro_nomb");
          else
            return "**ARTICULO NO ENCONTRADO**";
        }
        else
          return rs.getString("pro_nomart");
      }

      if (campo.equals("pro_codi"))
        return new Integer(rs.getInt("pro_codi"));
      if ( campo.equals("acl_canti"))
       return new Double(rs.getDouble(campo));
      if (campo.equals("acl_prcom"))
      {
        if (ARG_VERPRECIO)
          return new Double(rs.getDouble("acl_prcom"));
        else
          return new Double(0);
      }
      if (campo.equals("acl_impor"))
      {
        return new Double(rs.getDouble("acl_canti")*
                          rs.getDouble("acl_prcom"));
      }
     if (campo.equals("acl_numcaj"))
       return new Integer(rs.getInt(campo));

/*      if (campo.equals("acp_numind"))
        return new Integer(dtAdd.getInt(campo));
      if (campo.equals("acp_canti"))
        return new Double(dtAdd.getDouble(campo));
      if (campo.equals("acp_nucrot"))
        return dtAdd.getString(campo);
      if (campo.equals("mat_nomb"))
      {
        s = "SELECT mat_nrgsa FROM v_matadero " +
            " WHERE mat_codi = " + dtAdd.getInt("mat_codi");
        if (! dtStat.select(s))
          return "*****";
        else
          return dtStat.getString("mat_nrgsa");
      }
      if (campo.equals("sde_nomb"))
      {
        s = "SELECT sde_nrgsa FROM v_saladesp " +
            " WHERE sde_codi = " + dtAdd.getInt("sde_codi");
        if (!dtStat.select(s))
          return "*****";
        else
          return dtStat.getString("sde_nrgsa");
      }
      if (campo.equals("pai_nacid"))
        return getPais(dtAdd.getInt("acp_painac"));
      if (campo.equals("pai_engor"))
        return getPais(dtAdd.getInt("acp_engpai"));
      if (campo.equals("pai_sacrif"))
        return getPais(dtAdd.getInt("acp_paisac"));
      if (campo.equals("cambio"))
        return new Integer(cambio);
*/
      }
    catch (Exception k)
    {
      throw new JRException(k);
    }
    throw new JRException("Campo: "+campo+" NO VALIDO");

  }
}

class threadcoalbco extends Thread
{
  conalbco coalco;
  public threadcoalbco(conalbco coalco)
  {
    this.coalco=coalco;
    start();
  }

  public void run()
  {
    coalco.nThread++;
    if (!coalco.jt.isEnabled())
      return;
    int nThreadLoc = coalco.nThread;
    if (coalco.inDatoIndiv)
      coalco.brkBusq = true;
    while (coalco.inDatoIndiv || coalco.inDatTraza)
    {
      if (nThreadLoc != coalco.nThread)
        return; // Ha entrado otro thread
      try
      {
        this.sleep(100);
      }
      catch (Exception k)
      {
      }
    }
    coalco.brkBusq = false;
    coalco.inDatoIndiv = true;
    coalco.verDatInd0();
    coalco.inDatoIndiv = false;
    coalco.verDatTraza();
  }
}
