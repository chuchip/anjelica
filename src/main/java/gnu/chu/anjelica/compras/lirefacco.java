package gnu.chu.anjelica.compras;


import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import java.sql.*;
import gnu.chu.Menu.*;
import gnu.chu.anjelica.listados.Listados;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import net.sf.jasperreports.engine.*;
import gnu.chu.sql.*;
import gnu.chu.camposdb.*;
import gnu.chu.print.util;
import javax.swing.BorderFactory;

/**
 *
 * <p>Título: lirefacco</p>
 * <p>Descripción: Listado Relacion de Facturas de Compras
 *  Tambien tiene una opcion (NO VISIBLE) para actualizar los Totales
 *  de las facturas y/o los kilos e importe por linea segun lo que haya en los albaranes
 * relacionados<br>
 *  Para habilitar esta opcion comentar las siguientes lineas<br>
 *    Pacum.add(kilosTotL,null);
 *    Pacum.add(kilosTotE,null);
 *  Y descomentar las siguientes:
 *    Pacum.add(BactFras, null);
 *    Pacum.add(opImpTot, null);
 * <p>Copyright: Copyright (c) 2005-2016
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los terminos de la Licencia Pública General de GNU según es publicada por
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
 * <p>Empresa: miCasa</p>
 * @author chuchi P.
 * @version 1.0
 */
public class lirefacco  extends ventana
{
  String  s;
  CPanel Pprinc = new CPanel();
  CTextField feciniE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel1 = new CLabel();
  CTextField acc_anoIniE = new CTextField(Types.DECIMAL, "###9");
  empPanel empIniE = new empPanel();
  CButton Blistar = new CButton("Listar", Iconos.getImageIcon("print"));
  CLabel cLabel2 = new CLabel();
  CTextField fecfinE = new CTextField(Types.DATE, "dd-MM-yyyy");  
  CButton BactFras = new CButton();
  CCheckBox opImpTot = new CCheckBox();
  CLabel cLabel3 = new CLabel();
  CTextField fcc_numeIniE = new CTextField(Types.DECIMAL, "#####9");
  CTextField acc_anoFinE = new CTextField(Types.DECIMAL, "###9");
  CTextField fcc_numeFinE = new CTextField(Types.DECIMAL, "#####9");
  CLabel cLabel4 = new CLabel();
  
  CLabel prv_codiL = new CLabel();
  CLabel kilosTotL = new CLabel("Kilos Lin.");
  CTextField kilosTotE = new CTextField(Types.DECIMAL,"----,--9.99");
  prvPanel prv_codiE = new prvPanel();
  CCheckBox opIncDto = new CCheckBox("Inc. Dtos.");
  CPanel Pcriterios = new CPanel();
  Cgrid jt = new Cgrid(13);
  CButton Bcons = new CButton("Cons.", Iconos.getImageIcon("check"));
  CPanel Pacum = new CPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CLabel cLabel6 = new CLabel();
  CTextField fcc_basim1E = new CTextField(Types.DECIMAL,"---,---,--9.99");
  CTextField fcc_imptotE = new CTextField(Types.DECIMAL,"---,---,--9.99");
  CTextField fcc_impiv1E = new CTextField(Types.DECIMAL,"---,---,--9.99");
  CLabel fcc_kilfraL=new CLabel("Kilos Fra");
  CTextField fcc_kilfraE = new CTextField(Types.DECIMAL,"----,--9.99");
  CLabel cLabel10 = new CLabel();
  CLabel fcc_imptotL = new CLabel();
  CLabel cLabel12 = new CLabel();
  CTextField fcc_imirp1E = new CTextField(Types.DECIMAL,"---,---,--9.99");
  private CLabel ordenarL = new CLabel();
  private CComboBox ordenE = new CComboBox();
  private CLabel empIniL = new CLabel();

  public lirefacco(EntornoUsuario eu, Principal p)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    setTitulo("Listado Relacion Facturas Compras");

    try
    {
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

  public lirefacco(gnu.chu.anjelica.menu p, EntornoUsuario eu)
  {

    EU = eu;
    vl = p.getLayeredPane();
    setTitulo("Listado Relacion Facturas Compras");
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
    jt.setMaximumSize(new Dimension(607, 249));
    jt.setMinimumSize(new Dimension(607, 249));
    jt.setPreferredSize(new Dimension(607, 249));
    iniciarFrame();
    this.setSize(new Dimension(629, 432));
    this.setVersion("2016-06-08");

    Pprinc.setLayout(gridBagLayout1);

    statusBar = new StatusBar(this);
    kilosTotL.setBounds(new Rectangle(420, 24, 50, 17));
    kilosTotE.setEditable(false);
    kilosTotE.setBounds(new Rectangle(485, 24, 90, 17));
    fcc_kilfraL.setBounds(new Rectangle(420, 4, 60, 18));
    fcc_kilfraE.setBounds(new Rectangle(485, 4, 90, 18));

    opImpTot.setSelected(true);
    opImpTot.setText("Solo Totales");
    opImpTot.setBounds(new Rectangle(473, 30, 131, 15));
    cLabel3.setText("De Fra.");
    cLabel3.setBounds(new Rectangle(295, 4, 45, 17));
    fcc_numeIniE.setText("");
    fcc_numeIniE.setBounds(new Rectangle(387, 4, 57, 17));
    acc_anoFinE.setBounds(new Rectangle(495, 4, 43, 17));
    fcc_numeFinE.setBounds(new Rectangle(545, 4, 57, 17));
   
    cLabel4.setBounds(new Rectangle(453, 4, 45, 17));
    cLabel4.setText("De Fra.");
    
    prv_codiL.setText("Proveedor");
    prv_codiL.setBounds(new Rectangle(2, 42, 63, 16));
    prv_codiE.setAncTexto(50);
    prv_codiE.setBounds(new Rectangle(65, 42, 315, 18));
    
    Pcriterios.setBorder(BorderFactory.createRaisedBevelBorder());
    Pcriterios.setMaximumSize(new Dimension(615, 70));
    Pcriterios.setMinimumSize(new Dimension(615, 70));
    Pcriterios.setPreferredSize(new Dimension(615, 70));
  
    Pcriterios.setLayout(null);

    jt.setBounds(new Rectangle(4, 86, 607, 249));
    Bcons.setBounds(new Rectangle(397, 44, 98, 23));
    Pacum.setBorder(BorderFactory.createLoweredBevelBorder());
    Pacum.setMaximumSize(new Dimension(606, 51));
    Pacum.setMinimumSize(new Dimension(606, 51));
    Pacum.setPreferredSize(new Dimension(606, 51));
    Pacum.setLayout(null);
    cLabel6.setLabelFor(Pprinc);
    cLabel6.setText("Base Imponible");
    cLabel6.setBounds(new Rectangle(2, 3, 87, 19));

    fcc_basim1E.setBounds(new Rectangle(89, 3, 100, 17));
    fcc_imptotE.setBounds(new Rectangle(309, 24, 88, 18));
    fcc_impiv1E.setBounds(new Rectangle(309, 4, 88, 18));
    cLabel10.setBounds(new Rectangle(221, 4, 87, 18));
    cLabel10.setText("Importe Iva");
    fcc_imptotL.setBounds(new Rectangle(221, 24, 87, 18));
    fcc_imptotL.setText("Total Facturas");
    cLabel12.setText("Importe IRPF");
    cLabel12.setBounds(new Rectangle(2, 22, 87, 18));
    fcc_imirp1E.setBounds(new Rectangle(89, 22, 100, 18));
    this.getContentPane().add(statusBar,BorderLayout.SOUTH);
    conecta();
    fcc_basim1E.setEditable(false);
    fcc_imirp1E.setEditable(false);
    fcc_impiv1E.setEditable(false);
    fcc_imptotE.setEditable(false);
    fcc_kilfraE.setEditable(false);

    feciniE.setBounds(new Rectangle(57, 23, 76, 17));
    cLabel1.setText("De Fecha");
    cLabel1.setBounds(new Rectangle(2, 23, 55, 17));
    acc_anoIniE.setBounds(new Rectangle(337, 4, 43, 17));
    
    Blistar.setBounds(new Rectangle(498, 44, 106, 22));
    Blistar.setMargin(new Insets(0, 0, 0, 0));
    cLabel2.setText("A");
    cLabel2.setBounds(new Rectangle(145, 23, 19, 17));
   
    fecfinE.setBounds(new Rectangle(158, 23, 77, 17));
    opIncDto.setSelected(true);
    opIncDto.setBounds(new Rectangle(270, 23, 150, 17));
    BactFras.setBounds(new Rectangle(481, 5, 116, 23));
    BactFras.setText("Act Imp. Fras");
//    opImpTot.setEnabled(false);
//    BactFras.setEnabled(false);
    Pprinc.add(Pcriterios,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 1, 0, 2), 0, 0));
    empIniE.setBounds(new Rectangle(224, 5, 45, 15));
    empIniL.setText("Emp.");
    empIniL.setBounds(new Rectangle(194, 5, 30, 15));
    ordenarL.setText("Ordenar");
    ordenarL.setBounds(new Rectangle(2, 5, 50, 17));
    ordenE.setBounds(new Rectangle(60, 5, 125, 15));
    Pcriterios.add(feciniE, null);
    Pcriterios.add(empIniL, null);
    Pcriterios.add(empIniE, null);
  
    
    Pcriterios.add(fecfinE, null);
//    Pcriterios.add(opIncDto, null);
    Pcriterios.add(cLabel1, null);
    Pcriterios.add(cLabel2, null);
    Pcriterios.add(prv_codiE, null);
    Pcriterios.add(prv_codiL, null);
 
   
    Pcriterios.add(Blistar, null);
    Pcriterios.add(fcc_numeFinE, null);
    Pcriterios.add(cLabel4, null);
    Pcriterios.add(acc_anoFinE, null);
    Pcriterios.add(fcc_numeIniE, null);
    Pcriterios.add(cLabel3, null);
    Pcriterios.add(acc_anoIniE, null);
    Pcriterios.add(ordenarL, null);
    Pcriterios.add(ordenE, null);
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
    Pprinc.add(jt,   new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    Pcriterios.add(Bcons, null);
    Pprinc.add(Pacum,   new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
  //  Pacum.add(BactFras, null);
  //  Pacum.add(opImpTot, null);
    Pacum.add(kilosTotL,null);
    Pacum.add(kilosTotE,null);
    
    Pacum.add(fcc_impiv1E, null);
    Pacum.add(fcc_kilfraL, null);
    Pacum.add(fcc_kilfraE, null);
    Pacum.add(fcc_basim1E, null);
    Pacum.add(fcc_imptotE, null);
    Pacum.add(fcc_imptotL, null);
    Pacum.add(fcc_imirp1E, null);
    Pacum.add(cLabel12, null);
    Pacum.add(cLabel10, null);
    Pacum.add(cLabel6, null);

    prv_codiE.iniciar(dtStat, this, vl, EU);
    confGrid();
  }

    @Override
  public void iniciarVentana() throws Exception
  {
    Pcriterios.setDefButton(Bcons);
    ordenE.addItem("Por Factura","N");
    ordenE.addItem("Por Fecha","F");
    ordenE.addItem("Por Prv","P");

    empIniE.iniciar(dtStat, this, vl, EU);
    empIniE.setAceptaNulo(true);
   
//    acc_anoIniE.setValorDec(EU.ejercicio);
//    acc_anoFinE.setValorDec(EU.ejercicio);
    feciniE.setText(Formatear.sumaDias(Formatear.getDateAct(), -7));
    fecfinE.setText(Fecha.getFechaSys("dd-MM-yyyy"));
    activarEventos();
    feciniE.requestFocus();
  }
  void confGrid()
  {
    ArrayList v=new ArrayList();
    v.add("Factura"); // 0
    v.add("Fecha"); // 1
    v.add("Prv"); // 2
    v.add("Nombre Prv"); // 3
    v.add("Imp.Lin."); // 4
    v.add("B.Impon"); // 5
    v.add("IVA"); // 6
    v.add("Imp.Iva"); // 7
    v.add("IRPF"); // 8
    v.add("Imp.IRPF"); // 9
    v.add("Total"); // 10
    v.add("Kg.Lineas"); // 11
    v.add("Kil.Fra."); // 11
    jt.setCabecera(v);
    jt.setAnchoColumna(new int[]{70,55,55,160,60,70,40,70,40,60,70,70,70});
    jt.setAlinearColumna(new int[]{2,1,2,0,2,2,2,2,2,2,2,2,2});
    jt.setFormatoColumna(4,"----,--9.99");
    jt.setFormatoColumna(5,"----,--9.99");
    jt.setFormatoColumna(6,"99.9");
    jt.setFormatoColumna(7,"----,--9.99");
    jt.setFormatoColumna(8,"99.9");
    jt.setFormatoColumna(9,"----,--9.99");
    jt.setFormatoColumna(10,"--,---,--9.99");
    jt.setFormatoColumna(11,"---,--9.99");
    jt.setFormatoColumna(12,"---,--9.99");
  }
  void activarEventos()
  {
    BactFras.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        BactFras_actionPerformed();
      }
    });
    Blistar.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        Blistar_actionPerformed();
      }
    });
    Bcons.addActionListener(new ActionListener()
   {
     @Override
     public void actionPerformed(ActionEvent e)
     {
       Bcons_actionPerformed();
     }
   });

  }
  /**
   * Consultar Fras.
   */
  void Bcons_actionPerformed()
  {
    if (!checkEntrada())
      return;
    new miThread("")
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
    mensaje("A esperar.. estoy buscando las facturas");
    s = getSelect();
//    debug("Consulta: "+s);
    try  {
      jt.removeAllDatos();
      if (! dtCon1.select(s))
      {
        mensajeErr("NO encontradas Fras. para estos parametros");
        mensaje("");
        this.setEnabled(true);
        return;
      }
      jt.panelG.setVisible(false);
      double bImpT=0;
      double bImpIRFPT=0;
      double bImpIVAT=0;
      double sumTot=0;
      double kgTot=0;
      double kilFraT=0;
      PreparedStatement ps=dtStat.getPreparedStatement("select sum(fcl_canti) "+
              " as fcl_canti from v_falico where emp_codi = ? and eje_nume=? and fcc_nume=?");
      ResultSet rs;
      do
      {
        ps.setInt(1, dtCon1.getInt("emp_codi"));
        ps.setInt(2, dtCon1.getInt("eje_nume"));
        ps.setInt(3, dtCon1.getInt("fcc_nume"));
        rs=ps.executeQuery();
        rs.next();
        
        ArrayList v=new ArrayList();
        v.add(""+dtCon1.getInt("emp_codi")+"/"+dtCon1.getInt("eje_nume")+"-"+dtCon1.getInt("fcc_nume"));
        v.add(dtCon1.getFecha("fcc_fecfra","dd-MM-yy"));
        v.add(dtCon1.getString("prv_codi"));
        v.add(dtCon1.getString("prv_nomb"));
        v.add(dtCon1.getString("fcc_sumlin"));
        v.add(dtCon1.getString("fcc_basim1"));
        v.add(dtCon1.getDouble("fcc_piva1"));
        v.add(dtCon1.getString("fcc_impiv1"));
        v.add(dtCon1.getString("fcc_pirpf1"));
        v.add(dtCon1.getString("fcc_imirp1"));
        v.add(dtCon1.getString("fcc_sumtot"));
        
        if (rs.getObject("fcl_canti")==null)
            v.add("");
        else
        {
           v.add(rs.getDouble("fcl_canti"));
           kgTot+=rs.getDouble("fcl_canti");
        }
        v.add(dtCon1.getDouble("fcc_kilfra"));
        bImpT+=dtCon1.getDouble("fcc_basim1");
        bImpIRFPT+=dtCon1.getDouble("fcc_imirp1");
        bImpIVAT+=dtCon1.getDouble("fcc_impiv1");
        sumTot+=dtCon1.getDouble("fcc_sumtot");
        kilFraT+=dtCon1.getDouble("fcc_kilfra");
        jt.addLinea(v);
      } while (dtCon1.next());
      fcc_basim1E.setValorDec(bImpT);
      fcc_imirp1E.setValorDec(bImpIRFPT);
      fcc_impiv1E.setValorDec(bImpIVAT);
      fcc_imptotE.setValorDec(sumTot);
      fcc_kilfraE.setValorDec(kilFraT);
      kilosTotE.setValorDec(kgTot);
      jt.panelG.setVisible(true);
      jt.requestFocusInicio();
    } catch (Exception k)
    {
      Error("Error al buscar Facturas",k);
      return;
    }

    mensajeErr("Consulta REALIZADA");
    mensaje("");
    this.setEnabled(true);

  }
  String getSelect()
  {
    return " SELECT f.eje_nume,f.emp_codi,fcc_nume,fcc_fecfra,f.prv_codi,pv.prv_nomb,prv_nif,"+
       " fcc_sumlin,fcc_basim1,fcc_piva1 as fcc_piva1,fcc_impiv1,"+
       " fcc_pirpf1  as fcc_pirpf1,fcc_imirp1,fcc_sumtot,fcc_kilfra "+
       " FROM v_facaco f left join v_proveedo as pv on "+
       " pv.PRV_CODI = f.prv_codi "+
       " where 1=1 " +
       (empIniE.getValorInt()==0?"":" AND F.emp_codi = " + empIniE.getValorInt()) +
       (acc_anoIniE.isNull()?"":" and eje_nume >= " + acc_anoIniE.getValorInt()) +
       (acc_anoFinE.isNull()?"":" and eje_nume <= " + acc_anoFinE.getValorInt()) +
       (fcc_numeIniE.isNull()?"":" and fcc_nume >= "+fcc_numeIniE.getValorInt())+
       (fcc_numeFinE.isNull()?"":" and fcc_nume <= "+fcc_numeFinE.getValorInt())+
       (prv_codiE.isNull()?"":" and f.prv_codi = "+prv_codiE.getValorInt())+
       " and fcc_fecfra >= to_date('" + feciniE.getText() +"','dd-MM-yyyy')" +
       " and fcc_fecfra <= to_date('" + fecfinE.getText() +"','dd-MM-yyyy')" +
       " ORDER BY "+
       ((ordenE.getValor().equals("N")?" fcc_nume":
           (ordenE.getValor().equals("F")?" fcc_fecfra":"prv_codi,fcc_nume")));
  }
  void Blistar_actionPerformed()
  {
    if (!checkEntrada())
      return;
    new miThread("")
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
      mensaje("Voy a ver si listo las facturas, esperate un momento ;-) ");
      s = getSelect();
      debug("Select: " + s);

      java.util.HashMap mp = Listados.getHashMapDefault();
      JasperReport jr;
      mp.put("feciniE", feciniE.getText());
      mp.put("fecfinE", fecfinE.getText());
      mp.put("empiniE", empIniE.getValorInt());
      jr =  Listados.getJasperReport(EU, "relfacco");
     
      ResultSet rs = dtCon1.getStatement().executeQuery(dtCon1.parseaSql(s));
      JasperPrint jp = JasperFillManager.fillReport(jr, mp,
          new JRResultSetDataSource(rs));
      if (gnu.chu.print.util.printJasper(jp, EU))
        mensajeErr("Listado ... Generado");
      mensaje("");
      this.setEnabled(true);
    }
    catch (Exception k)
    {
      Error("Error al imprimir albaran", k);
    }
  }

  boolean checkEntrada()
  {
      try
      {
          if (!empIniE.controla())
          {
              mensajeErr(empIniE.getMsgError());
              return false;
          }
      } catch (Exception k)
      {
          Error("Error al controlar entrada",k);
          return false;
      }
    if (feciniE.isNull() || feciniE.getError())
    {
      mensajeErr("Fecha Inicio NO valida");
      feciniE.requestFocus();
      return false;
    }
    if (fecfinE.isNull() || fecfinE.getError())
    {
      mensajeErr("Fecha Final NO valida");
      fecfinE.requestFocus();
      return false;
    }
    return true;
  }
  void BactFras_actionPerformed()
  {
    if (!checkEntrada())
      return;

    new miThread("")
    {
      public void run()
      {
        actDatFra();
      }
    };
  }
  void actDatFra()
  {
    mensaje("Espere, por favor ... ACTUALIZANDO FACTURAS");
    this.setEnabled(false);
    actDatosFra();
    this.setEnabled(true);
    mensaje("");
    mensajeErr("Facturas ... ACTUALIZADAS");
  }
  void actDatosFra()
  {
    // Actualizar Importes de cabecera de Facturas.
    double impLi0, impIva, impRec;
    double impLin = 0;
    double impLiT = 0;

    try
    {
      DatosTabla dtAdd = new DatosTabla(ctUp);
      DatosTabla dtCons = new DatosTabla(ct);
      int empCodi, accAno, accNume;
      String accSerie;
      if (! opImpTot.isSelected())
      {
        // Busco Albaranes que entra en facturas del periodo
        s = "SELECT distinct l.emp_codi,l.acc_ano,l.acc_serie,l.acc_nume " +
            " FROM v_facaco as c, v_falico as l WHERE  c.emp_codi = l.emp_codi " +
            " AND c.eje_nume = l.eje_nume " +
            " and c.fcc_nume = l.fcc_nume " +
            " and l.acc_nume != 0 " +
            " and c.fcc_fecfra >= to_date('" + feciniE.getText() + "','dd-MM-yyyy')" +
            " and c.fcc_fecfra <= to_date('" + fecfinE.getText() + "','dd-MM-yyyy')";
        if (!dtCons.select(s))
          return;
        impLin = 0;
        impLiT = 0;
        do
        {
          mensajeRapido("Actualizando Imp. Alb: " + dtCons.getInt("acc_nume", true));
          empCodi = dtCons.getInt("emp_codi");
          accAno = dtCons.getInt("acc_ano");
          accSerie = dtCons.getString("acc_serie");
          accNume = dtCons.getInt("acc_nume", true);
          // Busco Lineas de Albaran con un Albaran dado
          s = "SELECT * FROM v_falico WHERE emp_codi = " + empCodi +
              " AND acc_ano =" + accAno +
              " and acc_serie = '" + accSerie + "'" +
              " and acc_nume = " + accNume;
          if (!dtAdd.select(s, true))
            continue;
          do
          {
            // Busco la Linea de la factura en Albaranes
            s = "SELECT l.acl_prcom,l.acl_canfac,l.acl_canti,c.acc_impokg " +
                " FROM v_albacol as l,v_albacoc as c" +
                " WHERE l.acc_ano =" + accAno +
                " and l.emp_codi = " + empCodi +
                " and l.acc_serie = '" + accSerie + "'" +
                " and l.acc_nume = " + accNume +
                " and l.acc_ano = c.acc_ano " +
                " and l.emp_codi = c.emp_codi " +
                " and l.acc_serie = c.acc_serie " +
                " and l.acc_nume = c.acc_nume " +
                " and acl_nulin = " + dtAdd.getInt("acl_nulin");
            if (!dtCon1.select(s))
            {
              aviso("pdalbaco: (actDatFra)\n" + s);
              continue; // Esto no se deberia dar ... SI DE DA ENVIO UN CORREO DE AVISO
            }
            dtAdd.edit("emp_codi = " + empCodi +
                       " AND eje_nume =" + dtAdd.getInt("eje_nume") +
                       " and fcc_nume = " + dtAdd.getInt("fcc_nume") +
                       " and fcl_numlin = " + dtAdd.getInt("fcl_numlin"));
            dtAdd.setDato("fcl_prcom",
                          dtCon1.getDouble("acl_prcom") -
                          dtCon1.getDouble("acc_impokg", true));
            dtAdd.setDato("fcl_canti", dtCon1.getDouble("acl_canti"));
            dtAdd.update(stUp);
          }
          while (dtAdd.next());
        }
        while (dtCons.next());
      }

        s = "SELECT *  FROM v_facaco WHERE " +
            "  emp_codi = " + empIniE.getValorInt() +
            " and eje_nume = " + acc_anoIniE.getValorInt() +
            " and fcc_fecfra >= to_date('" + feciniE.getText() + "','dd-MM-yyyy')" +
            " and fcc_fecfra <= to_date('" + fecfinE.getText() + "','dd-MM-yyyy')";
        if (!dtCons.select(s))
          return;
        do
        {
          mensajeRapido("Actualizando Totales Fra: " + dtCons.getInt("fcc_nume", true));
          impLiT = 0;
          s = "SELECT  * FROM v_falico " +
              "  WHERE emp_codi = " + dtCons.getInt("emp_codi") +
              " and eje_nume = " + dtCons.getInt("eje_nume") +
              " and fcc_nume = " + dtCons.getInt("fcc_nume");
          if (dtCon1.select(s))
          {
            do
            {
              impLin = Formatear.Redondea(dtCon1.getDouble("fcl_canti") *
                                          dtCon1.getDouble("fcl_prcom"), 3);
              if (dtCon1.getString("fcl_tipdes").equals("%") && dtCon1.getDouble("fcl_dto", true) > 0)
                impLin -= impLin * dtCon1.getDouble("fcl_dto", true) / 100;
              else
                impLin -= dtCon1.getDouble("fcl_dto", true);
              impLiT += Formatear.Redondea(impLin, 3);
            }       while (dtCon1.next());
          }
          impLiT = Formatear.redondea(impLiT, 3);
          impLi0 = impLiT;
          if (dtCons.getDouble("fcc_dtopp") != 0)
            impLiT -= Formatear.redondea(impLi0 * dtCons.getDouble("fcc_dtopp") / 100, 3);
          if (dtCons.getDouble("fcc_dtocom") != 0)
            impLiT -= Formatear.redondea(impLi0 * dtCons.getDouble("fcc_dtocom") / 100, 3);
          impIva = Formatear.redondea(impLiT * dtCons.getDouble("fcc_piva1", true) / 100, 3);
          impRec = Formatear.redondea(impLiT * dtCons.getDouble("fcc_prec1", true) / 100, 3);
          s = "UPDATE  v_facaco SET fcc_sumlin =" + impLi0 + "," +
              " fcc_basim1 =" + impLiT + "," +
              " fcc_impiv1 =" + impIva + "," +
              " fcc_impre1 =" + impRec + "," +
              " fcc_sumtot =" + (impLiT + impIva + impRec) +
              " where emp_codi = " + dtCons.getInt("emp_codi") +
              " AND eje_nume =" + dtCons.getInt("eje_nume") +
              " and fcc_nume = " + dtCons.getInt("fcc_nume");
          int nFraAct=stUp.executeUpdate(dtCon1.getStrSelect(s));
        }   while (dtCons.next());
      ctUp.commit();
    }
    catch (Exception k)
    {
      Error("Error al Act. Facturas", k);
    }
  }

}
