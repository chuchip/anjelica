package gnu.chu.anjelica.ventas;

/**
 *
 * <p>Título: clpedven </p>
 * <p>Descripción: Consulta/Listado Pedidos de Ventas</p>
 * <p>Copyright: Copyright (c) 2005-2015
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
 * </p>
 * @author chuchiP
 * @version 1.0
 *
 */
import net.sf.jasperreports.engine.*;
import gnu.chu.utilidades.*;
import gnu.chu.controles.*;
import gnu.chu.Menu.*;
import java.util.*;
import java.awt.*;
import javax.swing.BorderFactory;
import java.sql.*;
import gnu.chu.camposdb.*;
import gnu.chu.interfaces.ejecutable;
import java.awt.event.*;
import gnu.chu.sql.*;
import java.awt.print.PrinterException;
import javax.swing.event.*;
import java.text.*;

public class  clpedven extends ventana
{
  private final int JTCAB_SERALB=11;
  private final int JTCAB_NUMALB= 12;   
  private final int JTCAB_EJEALB=10;
  CButton Bimpri=new CButton("Imprimir",Iconos.getImageIcon("print"));
  int empCodiS,ejeNumeS,pvcNumeS,cliCodiS;
  ventana padre=null;
  String s;
  boolean verPrecio;
  proPanel pro_codiE= new proPanel();
  prvPanel prv_codiE = new prvPanel();
  String ZONA = null;
  CPanel Pprinc = new CPanel();
  CPanel Pcondi = new CPanel();
  Cgrid jtCabPed = new Cgrid(13);
  Cgrid jtLinPed = new Cgrid(10);
  CLabel cLabel1 = new CLabel();
  CComboBox verPedidosE = new CComboBox();
  CLabel cLabel2 = new CLabel();
  CTextField pvc_feciniE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel3 = new CLabel();
  CTextField pvc_fecfinE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel4 = new CLabel();
  cliPanel cli_codiE = new cliPanel();
  CComboBox alm_iniE = new CComboBox();
  CLabel cLabel14 = new CLabel();
  CComboBox alm_finE = new CComboBox();
  CLabel cLabel15 = new CLabel();
  CScrollPane jScrollPane1 = new CScrollPane();
  CTextArea pvc_comenE = new CTextArea();
  CPanel Ppie = new CPanel();
  CLabel cLabel16 = new CLabel();
  CTextField cantE = new CTextField(Types.DECIMAL, "---9");
  CTextField nlE = new CTextField(Types.DECIMAL, "#9");
  CLabel cLabel7 = new CLabel();
  CTextField pvc_fecpedE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel5 = new CLabel();
  CTextField usu_nombE = new CTextField(Types.CHAR, "X", 20);
  CTextField pvc_horpedE = new CTextField(Types.DECIMAL, "99.99");
  CLabel cLabel9 = new CLabel();
  CButton Baceptar = new CButton("<html><center>Aceptar<em>(F4)</EM></center></html>",
                                 Iconos.getImageIcon("check"));
  CLabel cLabel6 = new CLabel();
  CComboBox pvc_confirE = new CComboBox();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CLabel cLabel8 = new CLabel();
  CTextField emp_codiE = new CTextField(Types.DECIMAL,"#9");
  CLinkBox cli_zoncreE = new CLinkBox();
  CLinkBox cli_zonrepE = new CLinkBox();
  CLabel cLabel19 = new CLabel();
  CLabel cLabel20 = new CLabel();
  CComboBox opLista = new CComboBox();
  CComboBox opListado = new CComboBox();
  CCheckBox pvc_impresE = new CCheckBox("S","N");

  public clpedven(EntornoUsuario eu, Principal p)
  {
    this(eu, p, new Hashtable());
  }

  public clpedven(EntornoUsuario eu, Principal p, Hashtable<String,String> ht)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    try
    {
      if (ht != null)
      {
        if (ht.get("verPrecio") != null)
          verPrecio = Boolean.parseBoolean(ht.get("verPrecio"));
        if (ht.get("zona") != null)
          ZONA = ht.get("zona");
      }
      setTitulo("Cons/List. Pedidos Ventas");


      if (jf.gestor.apuntar(this))
        jbInit();
      else
        setErrorInit(true);
    }
    catch (Exception e)
    {
      ErrorInit(e);
    }
  }

  public clpedven(gnu.chu.anjelica.menu p, EntornoUsuario eu, Hashtable<String, String> ht)
  {
    EU = eu;
    vl = p.getLayeredPane();
    eje = false;
    jf = null;
    try
    {
      if (ht != null)
      {
        if (ht.get("verPrecio") != null)
          verPrecio = Boolean.parseBoolean(ht.get("verPrecio"));
        if (ht.get("zona") != null)
          ZONA = ht.get("zona");
      }
      setTitulo("Cons/List. Pedidos Ventas") ;

      jbInit();
    }
    catch (Exception e)
    {
      ErrorInit(e);
    }
  }

  public clpedven(ventana papa) throws Exception
  {
    padre=papa;
    dtStat=padre.dtStat;
    dtCon1=padre.dtCon1;
    vl=padre.vl;
    jf=padre.jf;

    EU=padre.EU;
    setTitulo("Consulta Pedidos Ventas");

    jbInit();
  }
  private void jbInit() throws Exception
  {
    iniciarFrame();
    this.setSize(new Dimension(675, 519));
    this.setVersion( " (2015-01-20)"+ (verPrecio ? " (CON PRECIOS) " : ""));
    statusBar = new StatusBar(this);

    if (dtStat==null)
      conecta();

    Bimpri.setMargin(new Insets(0, 0, 0, 0));
//    Bimpri.setPreferredSize(new Dimension(24, 24));
//    Bimpri.setMaximumSize(new Dimension(24, 24));
//    Bimpri.setMinimumSize(new Dimension(24, 24));

    Bimpri.setToolTipText("Imprimir Pedido");

    cLabel8.setText("Empresa");
    cLabel8.setBounds(new Rectangle(495, 24, 56, 18));
    emp_codiE.setBounds(new Rectangle(553, 24, 44, 18));
    cli_zoncreE.setBounds(new Rectangle(315, 65, 148, 18));
    cli_zoncreE.setAncTexto(30);
    cli_zonrepE.setAncTexto(30);
    cli_zonrepE.setBounds(new Rectangle(76, 63, 148, 18));
    cLabel19.setText("Zona/Cdto");
    cLabel19.setBounds(new Rectangle(255, 65, 65, 18));
    cLabel20.setToolTipText("");
    cLabel20.setText("Agen/Zona");
    cLabel20.setBounds(new Rectangle(3, 63, 69, 16));
    opLista.setBounds(new Rectangle(550, 2, 105, 17));
    Bimpri.setBounds(new Rectangle(551, 23, 104, 27));
    opListado.setBounds(new Rectangle(518, 44, 79, 16));
    pvc_impresE.setToolTipText("Pedido Listado ?");
    pvc_impresE.setMargin(new Insets(0, 0, 0, 0));
    pvc_impresE.setText("Impr.");
    pvc_impresE.setBounds(new Rectangle(496, 39, 53, 15));
//    pvc_impresE.setBounds(new Rectangle(488, 39, 63, 16));
//    statusBar.add(Bimpri, new GridBagConstraints(9, 0, 1, 2, 0.0, 0.0
//                                           , GridBagConstraints.EAST,
//                                           GridBagConstraints.VERTICAL,
//                                           new Insets(0, 5, 0, 0), 0, 0));
    cLabel6.setText("Confirmado");
    cLabel6.setBounds(new Rectangle(468, 3, 79, 18));
    Pcondi.setMaximumSize(new Dimension(601, 87));
    Pcondi.setMinimumSize(new Dimension(601, 87));
    Pcondi.setPreferredSize(new Dimension(601, 87));
    alm_finE.setBounds(new Rectangle(327, 44, 187, 16));
    cLabel15.setBounds(new Rectangle(266, 44, 63, 16));
    alm_iniE.setBounds(new Rectangle(76, 44, 187, 16));
    cLabel14.setBounds(new Rectangle(3, 44, 71, 16));
    Baceptar.setBounds(new Rectangle(473, 62, 121, 22));
    cli_codiE.setBounds(new Rectangle(76, 24, 388, 18));
    cLabel4.setBounds(new Rectangle(5, 24, 63, 18));
    pvc_confirE.setBounds(new Rectangle(551, 3, 46, 18));
    pvc_fecfinE.setBounds(new Rectangle(390, 3, 75, 18));
    cLabel3.setBounds(new Rectangle(332, 3, 58, 18));
    pvc_feciniE.setBounds(new Rectangle(252, 3, 75, 18));
    cLabel2.setBounds(new Rectangle(195, 3, 58, 18));
    verPedidosE.setBounds(new Rectangle(76, 3, 118, 18));
    cLabel1.setBounds(new Rectangle(6, 3, 78, 18));
    Ppie.setMaximumSize(new Dimension(544, 59));
    Ppie.setMinimumSize(new Dimension(544, 59));
    Ppie.setPreferredSize(new Dimension(544, 59));
    usu_nombE.setBounds(new Rectangle(395, 39, 99, 16));
    cLabel9.setBounds(new Rectangle(322, 37, 69, 16));
    cLabel5.setBounds(new Rectangle(324, 22, 78, 16));
    pvc_horpedE.setBounds(new Rectangle(479, 21, 35, 16));
    pvc_fecpedE.setBounds(new Rectangle(395, 21, 81, 16));
    jScrollPane1.setBounds(new Rectangle(8, 3, 309, 51));
    cantE.setBounds(new Rectangle(495, 3, 43, 16));
    cLabel16.setBounds(new Rectangle(438, 3, 55, 16));
    nlE.setBounds(new Rectangle(395, 3, 28, 16));
    cLabel7.setBounds(new Rectangle(326, 3, 69, 16));
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);

    cLabel5.setText("Fecha Pedido");
    cLabel9.setText("Usuario");
    Baceptar.setMargin(new Insets(0, 0, 0, 0));
    Pprinc.setLayout(gridBagLayout1);
    Pcondi.setBorder(BorderFactory.createRaisedBevelBorder());
    Pcondi.setLayout(null);
    cLabel1.setText("Ver Pedidos");

    cLabel2.setText("De Fecha");
    cLabel3.setRequestFocusEnabled(true);
    cLabel3.setText("A Fecha");
    cLabel4.setText("Cliente");
    cLabel14.setText("De Almacen");
    cLabel15.setText("A Almacen");
    Ppie.setLayout(null);
    Ppie.setBorder(BorderFactory.createRaisedBevelBorder());
    cLabel16.setText("Cantidad");
    cantE.setToolTipText("Cantidad de piezas del pedido");
    nlE.setToolTipText("Numero Lineas del pedido");
    cLabel7.setText("Num.Lineas");
    confJTCab();
    confJtLin();
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
    jScrollPane1.getViewport().add(pvc_comenE, null);
    Pprinc.add(Ppie,     new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    Pcondi.add(cLabel1, null);
    Pcondi.add(verPedidosE, null);
    Pcondi.add(pvc_feciniE, null);
    Pcondi.add(pvc_fecfinE, null);
    Pcondi.add(cLabel3, null);
    Pcondi.add(cli_codiE, null);
    Pcondi.add(cLabel4, null);
    Pcondi.add(alm_iniE, null);
    Pcondi.add(cLabel14, null);
    Pcondi.add(cLabel6, null);
    Pcondi.add(pvc_confirE, null);
    Pcondi.add(emp_codiE, null);
    Pcondi.add(cLabel8, null);
    Pcondi.add(cli_zoncreE, null);
    Pcondi.add(cLabel19, null);
    Pcondi.add(cli_zonrepE, null);
    Pcondi.add(cLabel20, null);
    Pcondi.add(Baceptar, null);
    Pcondi.add(alm_finE, null);
    Pcondi.add(cLabel15, null);
    Pcondi.add(opListado, null);
    Pcondi.add(cLabel2, null);
    Pprinc.add(jtCabPed,   new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    Pprinc.add(jtLinPed,   new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    Pprinc.add(Pcondi,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

    Ppie.add(jScrollPane1, null);
    Ppie.add(cLabel7, null);
    Ppie.add(cLabel5, null);
    Ppie.add(cLabel9, null);
    Ppie.add(cantE, null);
    Ppie.add(cLabel16, null);
    Ppie.add(nlE, null);
    Ppie.add(pvc_fecpedE, null);
    Ppie.add(pvc_horpedE, null);
    Ppie.add(usu_nombE, null);
    Ppie.add(opLista, null);
    Ppie.add(Bimpri, null);
    Ppie.add(pvc_impresE, null);
    Ppie.setEnabled(false);
    opLista.setEnabledParent(true);
    Bimpri.setEnabledParent(true);
    if (padre!=null)
    {
      verPedidosE.setEnabled(false);
      emp_codiE.setEnabled(false);
      pvc_confirE.setEnabled(false);
    }
  }

  @Override
  public void iniciarVentana() throws Exception
  {
    opLista.addItem("Relacion","R");
    opLista.addItem("Impreso","I");

    opListado.addItem("Todos","T");
    opListado.addItem("Listado","S");
    opListado.addItem("NO List.","N");
    cli_zonrepE.setFormato(Types.CHAR, "XX", 2);
    cli_zonrepE.texto.setMayusc(true);
    gnu.chu.anjelica.pad.pdconfig.llenaDiscr(dtStat,cli_zonrepE,"CR",EU.em_cod);

    cli_zonrepE.addDatos("**", "TODOS");

    cli_zoncreE.setFormato(Types.CHAR, "XX", 2);
    cli_zoncreE.texto.setMayusc(true);
    gnu.chu.anjelica.pad.pdconfig.llenaDiscr(dtStat,cli_zoncreE,"CC",EU.em_cod);

    cli_zoncreE.addDatos("**", "TODOS");

    Pcondi.setDefButton(Baceptar);
    pvc_feciniE.setAceptaNulo(false);
    pvc_fecfinE.setAceptaNulo(false);
    cli_codiE.setCeroIsNull(true);
    s = "SELECT alm_codi,alm_nomb FROM v_almacen ORDER BY alm_codi";
    dtCon1.select(s);
    int almIni = dtCon1.getInt("alm_codi");
    alm_iniE.setValor(almIni);
    alm_iniE.addItem(dtCon1);
    dtCon1.select(s);
    alm_finE.addItem(dtCon1);
    dtCon1.last();
    alm_finE.setValor(dtCon1.getInt("alm_codi"));
    cli_codiE.iniciar(dtStat, this, vl, EU);
    cli_codiE.setZona(ZONA);
    verPedidosE.addItem("Pendientes", "P");
    verPedidosE.addItem("Todos", "T");
    verPedidosE.addItem("Servidos", "S");
    pvc_confirE.addItem("Si", "S");
    pvc_confirE.addItem("No", "N");
    pvc_confirE.addItem("**", "-");
    pvc_feciniE.setText(Formatear.sumaDias(Formatear.getDateAct(), -15));
    pvc_fecfinE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
   
    activarEventos();
  }
  void activarEventos()
  {
    Bimpri.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        Bimpri_actionPerformed();
      }
    });

    Baceptar.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        Baceptar_actionPerformed();
      }
    });
    jtCabPed.addListSelectionListener(new ListSelectionListener()
    {
      @Override
      public void valueChanged(ListSelectionEvent e)
      {
        if (e.getValueIsAdjusting() || ! jtCabPed.isEnabled() || jtCabPed.isVacio() ) // && e.getFirstIndex() == e.getLastIndex())
          return;
        verDatPed(jtCabPed.getValorInt(0),jtCabPed.getValorInt(1),jtCabPed.getValorInt(2));
//      System.out.println(" Row "+getValString(0,5)+ " - "+getValString(1,5));

      }
    });
  
    jtCabPed.addMouseListener(new MouseAdapter()
    {
      @Override
      public void mouseClicked(MouseEvent e)
      {
        if (e.getClickCount() < 2)
          return;
        if (jtCabPed.isVacio())
          return;
        if (padre!=null)
        {
          empCodiS=jtCabPed.getValorInt(0);
          ejeNumeS=jtCabPed.getValorInt(1);
          pvcNumeS=jtCabPed.getValorInt(2);
          cliCodiS=jtCabPed.getValorInt(3);
          matar();
        }
        else
        { 
            if (jtCabPed.getValorInt(JTCAB_EJEALB)==0)
                return;
             ejecutable prog;
             if ((prog=jf.gestor.getProceso(pdalbara.getNombreClase()))==null)
                    return;
            pdalbara cm=(pdalbara) prog;
            if (cm.inTransation())
            {
               msgBox("Mantenimiento Albaranes de Ventas ocupado. No se puede realizar la busqueda");
               return;
            }
            cm.PADQuery();
            
            cm.setSerieAlbaran(jtCabPed.getValString(JTCAB_SERALB));
            cm.setNumeroAlbaran(jtCabPed.getValString(JTCAB_NUMALB));
            cm.setEjercAlbaran(jtCabPed.getValorInt(JTCAB_EJEALB));

            cm.ej_query();
            jf.gestor.ir(cm);
        }
      }

    });    
  }
  void Baceptar_actionPerformed()
  {
    try
    {
      if (! iniciarCons(true))
        return;
      do
      {
        ArrayList v=new ArrayList();
        v.add(dtCon1.getString("emp_codi")); // 0
        v.add(dtCon1.getString("eje_nume")); // 1
        v.add(dtCon1.getString("pvc_nume")); // 2
        v.add(dtCon1.getString("cli_codi")); // 3
        v.add(dtCon1.getString("cli_nomb")); // 4
        v.add(dtCon1.getFecha("pvc_fecent","dd-MM-yyyy")); // 5
        v.add(dtCon1.getString("pvc_confir")); // 6
        v.add(dtCon1.getInt("pvc_cerra")!=0); // 7
        v.add(dtCon1.getString("pvc_nupecl")); // 8
        v.add(dtCon1.getString("alm_nomb")); // 9
        v.add(dtCon1.getString("avc_ano")); //10
        v.add(dtCon1.getString("avc_serie")); // 11
        v.add(dtCon1.getString("avc_nume")); //12
        jtCabPed.addLinea(v);
      } while (dtCon1.next());
      jtCabPed.requestFocusInicio();
      jtCabPed.setEnabled(true);
      verDatPed(jtCabPed.getValorInt(0),jtCabPed.getValorInt(1),jtCabPed.getValorInt(2));
    }
    catch (SQLException | ParseException k)
    {
      Error("Error al buscar pedidos", k);
    }
  }

  private boolean iniciarCons(boolean ejecSelect) throws SQLException, ParseException
  {
    if (pvc_feciniE.getError())
    {
      mensajeErr("Fecha INICIAL no es valida");
      pvc_feciniE.requestFocus();
      return false;
    }
    if (pvc_fecfinE.getError())
    {
      mensajeErr("Fecha FINAL no es valida");
      pvc_feciniE.requestFocus();
      return false;
    }
    if (! ejecSelect)
      return true;
    s = "SELECT c.*,cl.cli_nomb,al.alm_nomb FROM pedvenc as c,clientes as cl,v_almacen as al " +
        " WHERE pvc_fecent between to_date('" + pvc_feciniE.getText() + "','dd-MM-yyyy')" +
        " and  to_date('" + pvc_fecfinE.getText()  + "','dd-MM-yyyy')" +
        " and c.alm_codi >= " + alm_iniE.getValor() +
        " and c.alm_codi <= " + alm_finE.getValor() +
        " and c.pvc_confir = 'S' "+
        " and cl.cli_codi = c.cli_codi " +
        " and c.alm_codi = al.alm_codi "+
        (emp_codiE.getValorInt() == 0 ? "" : " AND c.emp_codi = " + emp_codiE.getValorInt());

    if (verPedidosE.getValor().equals("P"))
      s += " AND c.avc_ano = 0";
    if (verPedidosE.getValor().equals("S"))
      s += " AND c.avc_ano != 0";
    if (!pvc_confirE.getValor().equals("-"))
      s += " and pvc_confir = '" + pvc_confirE.getValor() + "'";
    if (!cli_codiE.isNull())
      s += " AND c.cli_codi = " + cli_codiE.getValorInt();
    if (!cli_zoncreE.isNull(true) && !cli_zoncreE.getText().equals("**") && !cli_zoncreE.getText().equals("*"))
      s += " and cl.cli_zoncre  LIKE '" + Formatear.reemplazar(cli_zoncreE.getText(), "*", "%") + "'";
    if (!cli_zonrepE.isNull(true) && !cli_zonrepE.getText().equals("**") && !cli_zonrepE.getText().equals("*"))
      s += " and cl.cli_zonrep  LIKE '" + Formatear.reemplazar(cli_zonrepE.getText(), "*", "%") + "'";
    if (! opListado.getValor().equals("T"))
      s+=" AND c.pvc_impres = '"+opListado.getValor()+"'";
    s += " order by c.pvc_fecent,c.cli_codi ";

    jtCabPed.setEnabled(false);
    jtCabPed.removeAllDatos();
//      debug("s: "+s);
    if (!dtCon1.select(s))
    {
      mensajeErr("NO hay PEDIDOS que cumplan estas condiciones");
      verPedidosE.requestFocus();
      return false;
    }
    return true;
  }

  String getAlmNomb(int almCodi, DatosTabla dt) throws SQLException, java.text.ParseException
  {
   s="SELECT alm_nomb FROM v_almacen WHERE alm_codi = "+almCodi;
   if (! dt.select(s))
     return "**ALMACEN: "+almCodi+" NO ENCONTRADO**";

   return dt.getString("alm_nomb");
  }

  private void confJTCab()
  {
    ArrayList v=new ArrayList();
    v.add("Em"); // 0
    v.add("Eje."); // 1
    v.add("Num.");// 2
    v.add("Cliente"); // 3
    v.add("Nombre Cliente"); // 4
    v.add("Fec.Entrega"); // 5
    v.add("Conf"); // 6
    v.add("Cerr");// 7
    v.add("Ped.Cliente"); // 8
    v.add("Almacen");// 9
    v.add("Ej.Alb");
    v.add("S.Alb");
    v.add("Num.Alb");
    jtCabPed.setCabecera(v);
    jtCabPed.setMaximumSize(new Dimension(548, 158));
    jtCabPed.setMinimumSize(new Dimension(548, 158));
    jtCabPed.setPreferredSize(new Dimension(548, 158));
    jtCabPed.setAnchoColumna(new int[]{26,40,49,55,150,76,30,40,80,100,40,40,60});
    jtCabPed.setAlinearColumna(new int[]{2,2,2,2,0,1,1,1,0,0,2,1,2});

    jtCabPed.setFormatoColumna(6,"BSN");
    jtCabPed.setFormatoColumna(7,"BSN");
  }
  private void confJtLin() throws Exception
   {
     ArrayList v = new ArrayList();
     v.add("Prod."); // 0
     v.add("Desc. Prod."); // 1
     v.add("Prv"); // 2
     v.add("Nombre Prv"); // 3
     v.add("Fec.Cad"); // 4
     v.add("Cant"); // 5
     v.add("Precio"); // 6
     v.add("Conf"); // 7 Confirmado Precio ?
     v.add("Comentario"); // 8 Comentario
     v.add("NL."); // 9
     jtLinPed.setCabecera(v);
     jtLinPed.setMaximumSize(new Dimension(548, 127));
     jtLinPed.setMinimumSize(new Dimension(548, 127));
     jtLinPed.setPreferredSize(new Dimension(548, 127));
     jtLinPed.setPuntoDeScroll(50);
     jtLinPed.setAnchoColumna(new int[]
                        {60, 160, 50, 150, 90, 70, 60, 50, 150, 30});
     jtLinPed.setAlinearColumna(new int[]
                          {2, 0, 2, 0, 1, 2, 2, 1, 0, 2});
     
     jtLinPed.setFormatoColumna(6, "---,--9.99");
     jtLinPed.setFormatoColumna(7, "BSN");
   }

   void verDatPed(int empCodi,int ejeNume,int pvcNume)
   {
     try
     {
       s="SELECT * FROM v_pedven "+
           " WHERE emp_codi =  "+empCodi+
           " AND eje_nume = "+ejeNume+
           " and pvc_nume = "+pvcNume+
           " order by pvl_numlin ";
       jtLinPed.removeAllDatos();
       Ppie.resetTexto();
       if (! dtCon1.select(s))
       {
         msgBox("NO ENCONTRADOS DATOS PARA ESTE PEDIDO");
         return;
       }
       usu_nombE.setText(dtCon1.getString("usu_nomb"));
       pvc_fecpedE.setText(dtCon1.getFecha("pvc_fecped"));
       pvc_horpedE.setText(dtCon1.getFecha("pvc_fecped","hh.mm"));
       pvc_comenE.setText(dtCon1.getString("pvc_comen"));
       pvc_impresE.setSelecion(dtCon1.getString("pvc_impres"));
       do
       {
         ArrayList v=new ArrayList();
         v.add(dtCon1.getString("pro_codi"));
         v.add(pro_codiE.getNombArtCli(dtCon1.getInt("pro_codi"),
                                              cli_codiE.getValorInt(),EU.em_cod,dtStat));
         v.add(dtCon1.getString("prv_codi"));
         v.add(prv_codiE.getNombPrv(dtCon1.getString("prv_codi"),dtStat));
         v.add(dtCon1.getFecha("pvl_feccad"));
         v.add(dtCon1.getString("pvl_canti")+" "+dtCon1.getString("pvl_tipo"));
         v.add(dtCon1.getString("pvl_precio"));
         v.add(dtCon1.getInt("pvl_precon") != 0);
         v.add(dtCon1.getString("pvl_comen"));
         v.add(dtCon1.getString("pvl_numlin"));
         jtLinPed.addLinea(v);
       } while (dtCon1.next());
       actAcumJT();
     } catch (Exception k)
     {
       Error("Error al Ver datos de pedido",k);
     }
   }

   void actAcumJT()
   {
     int nRows = jtLinPed.getRowCount(),nl = 0,nu=0;

     for (int n = 0; n < nRows; n++)
     {
       if (jtLinPed.getValorInt(n, 0) == 0)
         continue;
       nl++;
       nu += jtLinPed.getValorDec(n, 5);
     }
     nlE.setValorInt(nl);
     cantE.setValorDec(nu);
   }
   void Bimpri_actionPerformed()
    {

        if (opLista.getValor().equals("I"))
        {
          imprImpreso();
          return;
        }
      try {
        if (!iniciarCons(true))
          return;

        java.util.HashMap mp = new java.util.HashMap();
        mp.put("fecini",pvc_feciniE.getDate());
        mp.put("fecfin",pvc_fecfinE.getDate());
        mp.put("cli_zonrep",cli_zonrepE.getText());
        mp.put("cli_zoncre",cli_zoncreE.getText());
        JasperReport jr;
        jr =  gnu.chu.print.util.getJasperReport(EU, "relpedven");

        ResultSet rs;

        rs=dtCon1.getStatement().executeQuery(dtCon1.getStrSelect());

        JasperPrint jp = JasperFillManager.fillReport(jr, mp, new JRResultSetDataSource(rs));
        gnu.chu.print.util.printJasper(jp, EU);

         mensajeErr("Relacion Pedido Ventas ... IMPRESO ");
      }
      catch (SQLException | ParseException | JRException | PrinterException k)
      {
        Error("Error al imprimir Pedido Venta", k);
      }
    }

    void imprImpreso()
    {
      try
      {
        s = "select l.*,p.prv_nomb ,c.cli_codi,c.alm_codi,c.pvc_fecped, " +
            " c.pvc_fecent,c.usu_nomb,c.pvc_comen,al.alm_nomb, " +
            " a.pro_nomb, cl.cli_nomb,cl.cli_pobl " +
            " from pedvenl as l left join v_proveedo p on  p.prv_codi = l.prv_codi, " +
            " pedvenc as c,v_articulo as a,v_almacen as al,clientes as cl " +
            " where  c.emp_codi = l.emp_codi " +
            " and pvc_fecent  between to_date('" + pvc_feciniE.getText() + "','dd-MM-yyyy')" +
            " and to_date('" + pvc_fecfinE.getText() + "','dd-MM-yyyy')"  +
            " and c.eje_nume = l.eje_nume " +
            " and c.pvc_nume = l.pvc_nume " +
            " and l.pro_codi = a.pro_codi " +
            " and al.alm_codi = c.alm_codi " +
            " and c.cli_codi = cl.cli_codi " +
            " and c.pvc_confir = 'S' "+
            " and c.alm_codi >= " + alm_iniE.getValor() +
            " and c.alm_codi <= " + alm_finE.getValor() +
            " and cl.cli_codi = c.cli_codi " +
            " and c.alm_codi = al.alm_codi " +
            (emp_codiE.getValorInt() == 0 ? "" : " AND emp_codi = " + emp_codiE.getValorInt());

        if (verPedidosE.getValor().equals("P"))
          s += " AND avc_ano = 0";
        if (verPedidosE.getValor().equals("S"))
          s += " AND avc_ano != 0";
        if (!pvc_confirE.getValor().equals("-"))
          s += " and pvc_confir = '" + pvc_confirE.getValor() + "'";
        if (!cli_codiE.isNull())
          s += " AND c.cli_codi = " + cli_codiE.getValorInt();
        if (!cli_zoncreE.isNull(true) && !cli_zoncreE.getText().equals("**") && !cli_zoncreE.getText().equals("*"))
          s += " and cl.cli_zoncre  LIKE '" + Formatear.reemplazar(cli_zoncreE.getText(), "*", "%") + "'";
        if (!cli_zonrepE.isNull(true) && !cli_zonrepE.getText().equals("**") && !cli_zonrepE.getText().equals("*"))
          s += " and cl.cli_zonrep  LIKE '" + Formatear.reemplazar(cli_zonrepE.getText(), "*", "%") + "'";
        if (! opListado.getValor().equals("T"))
          s+=" AND c.pvc_impres = '"+opListado.getValor()+"'";
        s += " order by c.pvc_fecent,c.cli_codi ";
        if (! dtCon1.select(s)){
          mensajeErr("NO hay pedidos con estas condiciones");
          return;
        }
        ResultSet rs;
        java.util.HashMap mp = new java.util.HashMap();
        JasperReport jr;
        jr = gnu.chu.print.util.getJasperReport(EU, "pedventas");

        rs = dtCon1.getStatement().executeQuery(dtCon1.getStrSelect());

        JasperPrint jp = JasperFillManager.fillReport(jr, mp, new JRResultSetDataSource(rs));
        gnu.chu.print.util.printJasper(jp, EU);

        mensajeErr("Relacion Pedido Ventas ... IMPRESO ");
        dtCon1.select(s);
        do
        {
          s = "update PEDVENC SET pvc_impres = 'S' WHERE emp_codi = " + dtCon1.getInt("emp_codi")+
          " and eje_nume = " + dtCon1.getInt("eje_nume")+
          " and pvc_nume = " +dtCon1.getInt("pvc_nume");
          stUp.executeUpdate(s);
        } while (dtCon1.next());
        ctUp.commit();
        mensajeErr("Pedido Ventas ... IMPRESO ");
      }
      catch (SQLException | JRException | PrinterException k)
      {
        Error("Error al imprimir Pedido Venta", k);
      }
    }
}
