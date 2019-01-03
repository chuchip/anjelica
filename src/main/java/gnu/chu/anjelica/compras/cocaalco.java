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
import javax.swing.event.*;
import gnu.chu.sql.*;
import javax.swing.border.*;
import javax.swing.SwingConstants;

/**
* Consulta Cabeceras Albaranes de Compras.
 *
 * Permite consultar las cabeceras de compras que cumplan diversos
 * criterios.
 * Tambien permite delimitar por proveedor y por albaran.
 * <p>Copyright: Copyright (c) 2005-2011
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
 * @author chuchiP
 * @version 1.0
 */

public class cocaalco extends ventana
{
  private  double LIMINF=-0.9;
  private  double LIMSUP=0.9;
  PreparedStatement  psTotal;
  double impTot,impFra,impPen,kilTot,kilFra,kilPen;
  double importe,kilos;
  CPanel Pprinc = new CPanel();
  CPanel Pcond = new CPanel();
  CTextField feciniE = new CTextField(Types.DATE,"dd-MM-yy");
  prvPanel prv_codiE1 = new prvPanel();
  CLabel cLabel4 = new CLabel();
  CLabel cLabel1 = new CLabel();
  prvPanel prv_codiE = new prvPanel();
  CLabel cLabel2 = new CLabel();
  CLabel cLabel17 = new CLabel();
  CTextField fecfinE = new CTextField(Types.DATE,"dd-MM-yy");
  CTextField acc_numeE = new CTextField(Types.DECIMAL,"#####9");
  CLabel cLabel19 = new CLabel();
  CTextField acc_numeE1 = new CTextField(Types.DECIMAL,"#####9");
  CLabel cLabel5 = new CLabel();
  CComboBox opFactC = new CComboBox();
  CButton Bacepta = new CButton(Iconos.getImageIcon("check"));
  Cgrid jtCab = new Cgrid(12);
  Cgrid jtLin = new Cgrid(8);
  CPanel PAcum = new CPanel();
  CLabel cLabel3 = new CLabel();
  CTextField kilTotE = new CTextField(Types.DECIMAL,"--,---,--9.9");
  CTextField impTotE = new CTextField(Types.DECIMAL,"---,---,--9.9");
  CLabel cLabel6 = new CLabel();
  TitledBorder titledBorder1;
  CLabel cLabel13 = new CLabel();
  CTextField impFactE = new CTextField(Types.DECIMAL,"---,---,--9.9");
  CLabel cLabel14 = new CLabel();
  CTextField kilFactE = new CTextField(Types.DECIMAL,"--,---,--9.9");
  CTextField impPendE = new CTextField(Types.DECIMAL,"---,---,--9.9");
  CTextField kilPendE = new CTextField(Types.DECIMAL,"--,---,--9.9");
  CLabel cLabel15 = new CLabel();
  CCheckBox opIgnTF = new CCheckBox();
  empPanel emp_codiE = new empPanel();
  CLabel emp_codiL = new CLabel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CLabel cLabel7 = new CLabel();
  CTextField kilRestE = new CTextField(Types.DECIMAL,"#9.9");
  sbePanel sbe_codiE = new sbePanel();
  CLabel sbe_codiL = new CLabel();
  CCheckBox opInterno = new CCheckBox();

  public cocaalco(EntornoUsuario eu, Principal p)
  {
  EU = eu;
  vl = p.panel1;
  jf = p;
  eje = true;

  setTitulo("Consulta Cabeceras Alb. Compras");

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

public cocaalco(gnu.chu.anjelica.menu p, EntornoUsuario eu)
{

  EU = eu;
  vl = p.getLayeredPane();
  setTitulo("Consulta Cabeceras Alb. Compras");
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
  titledBorder1 = new TitledBorder("");
  iniciarFrame();
  this.setSize(new Dimension(749, 592));
  this.setVersion("2011-08-19");

  conecta();
  statusBar = new StatusBar(this);

    Pprinc.setLayout(gridBagLayout1);
    Pcond.setBorder(BorderFactory.createRaisedBevelBorder());
    Pcond.setMaximumSize(new Dimension(733, 107));
    Pcond.setMinimumSize(new Dimension(733, 107));
    Pcond.setPreferredSize(new Dimension(733, 107));
    opFactC.setBounds(new Rectangle(3, 47, 112, 16));
    Pcond.setLayout(null);
    feciniE.setBounds(new Rectangle(55, 23, 67, 16));
    prv_codiE1.setAncTexto(50);
    prv_codiE1.setBounds(new Rectangle(435, 4, 296, 18));
    cLabel4.setBounds(new Rectangle(1, 4, 84, 16));
    cLabel4.setText("De Proveedor");
    cLabel1.setBounds(new Rectangle(1, 23, 53, 16));
    cLabel1.setText("De Fecha");
    prv_codiE.setBounds(new Rectangle(90, 4, 271, 18));
    prv_codiE.setAncTexto(50);
    cLabel2.setBounds(new Rectangle(125, 23, 50, 16));
    cLabel2.setText("A Fecha");
    cLabel17.setText("A Proveedor");
    cLabel17.setBounds(new Rectangle(363, 4, 73, 16));
    fecfinE.setBounds(new Rectangle(171, 23, 70, 16));
    acc_numeE.setBounds(new Rectangle(314, 23, 51, 16));
    cLabel19.setText("A Albaran");
    cLabel19.setBounds(new Rectangle(367, 23, 61, 16));
    acc_numeE1.setBounds(new Rectangle(424, 23, 51, 16));
    cLabel5.setBounds(new Rectangle(250, 23, 61, 16));
    cLabel5.setText("De Albaran");
    Bacepta.setBounds(new Rectangle(244, 70, 123, 33));
    Bacepta.setMargin(new Insets(0, 0, 0, 0));
    Bacepta.setText("Acepta(F4)");

    PAcum.setBorder(BorderFactory.createLoweredBevelBorder());
    PAcum.setMaximumSize(new Dimension(356, 60));
    PAcum.setMinimumSize(new Dimension(356, 60));
    PAcum.setPreferredSize(new Dimension(356, 60));
    PAcum.setBounds(new Rectangle(371, 44, 356, 60));

    PAcum.setLayout(null);
    cLabel3.setBackground(Color.orange);
    cLabel3.setFont(new java.awt.Font("Dialog", 1, 11));
    cLabel3.setOpaque(true);
    cLabel3.setToolTipText("");
    cLabel3.setHorizontalAlignment(SwingConstants.CENTER);
    cLabel3.setText("Kilos");
    cLabel3.setBounds(new Rectangle(6, 23, 54, 16));


    impTotE.setBounds(new Rectangle(62, 39, 91, 16));
    cLabel6.setBackground(Color.orange);
    cLabel6.setFont(new java.awt.Font("Dialog", 1, 11));
    cLabel6.setOpaque(true);
    cLabel6.setHorizontalAlignment(SwingConstants.CENTER);
    cLabel6.setText("Importe");
    cLabel6.setBounds(new Rectangle(6, 39, 54, 16));
    titledBorder1.setTitle("Total");
    cLabel13.setBackground(Color.red);
    cLabel13.setForeground(Color.white);
    cLabel13.setOpaque(true);
    cLabel13.setHorizontalAlignment(SwingConstants.CENTER);
    cLabel13.setHorizontalTextPosition(SwingConstants.CENTER);
    cLabel13.setText("Total");
    cLabel13.setBounds(new Rectangle(62, 3, 89, 16));
    impFactE.setText("cTextField1");
    impFactE.setBounds(new Rectangle(155, 39, 91, 16));
    cLabel14.setText("Facturado");
    cLabel14.setBounds(new Rectangle(155, 3, 89, 16));
    cLabel14.setHorizontalTextPosition(SwingConstants.CENTER);
    cLabel14.setHorizontalAlignment(SwingConstants.CENTER);
    cLabel14.setOpaque(true);
    cLabel14.setForeground(Color.white);
    cLabel14.setBackground(Color.red);
    impPendE.setText("cTextField1");
    impPendE.setBounds(new Rectangle(248, 38, 91, 16));
    cLabel15.setBackground(Color.red);
    cLabel15.setForeground(Color.white);
    cLabel15.setOpaque(true);
    cLabel15.setHorizontalAlignment(SwingConstants.CENTER);
    cLabel15.setHorizontalTextPosition(SwingConstants.CENTER);
    cLabel15.setText("Pendiente");
    cLabel15.setBounds(new Rectangle(248, 4, 89, 16));
    kilFactE.setBounds(new Rectangle(155, 22, 91, 16));
    kilTotE.setBounds(new Rectangle(62, 22, 91, 16));
    kilPendE.setBounds(new Rectangle(248, 21, 91, 16));
    jtLin.setMaximumSize(new Dimension(621, 156));
    jtLin.setMinimumSize(new Dimension(621, 156));
    jtLin.setPreferredSize(new Dimension(621, 156));
    opIgnTF.setToolTipText("Ign. Campo Totalmente Fact.");
    opIgnTF.setText("Ign. Tot. Fact.");
    opIgnTF.setBounds(new Rectangle(118, 45, 118, 17));
    emp_codiL.setText("Empresa");
    emp_codiL.setBounds(new Rectangle(487, 23, 57, 17));
    emp_codiE.setBounds(new Rectangle(546, 23, 48, 17));
    sbe_codiL.setBounds(new Rectangle(606, 23, 72, 17));
    sbe_codiL.setText("SubEmpresa");
    sbe_codiE.setBounds(new Rectangle(680, 23, 48, 17));
    cLabel7.setText("Ignorar si Kilos Restantes <");
    cLabel7.setBounds(new Rectangle(3, 66, 157, 17));
    kilRestE.setText("0.9");
    kilRestE.setBounds(new Rectangle(159, 64, 44, 20));

    opInterno.setToolTipText("Incluir Alb. Internos");
    opInterno.setText("Inc. Alb. Interno");
    opInterno.setBounds(new Rectangle(232, 45, 136, 17));
    PAcum.add(cLabel3, null);
    PAcum.add(cLabel6, null);
    PAcum.add(kilTotE, null);
    PAcum.add(cLabel13, null);
    PAcum.add(impTotE, null);
    PAcum.add(cLabel14, null);
    PAcum.add(kilFactE, null);
    PAcum.add(impFactE, null);
    PAcum.add(cLabel15, null);
    PAcum.add(kilPendE, null);
    PAcum.add(impPendE, null);
    Pprinc.add(jtCab,  new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    Pprinc.add(jtLin,   new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 1, 0), 0, 0));
    this.getContentPane().add(statusBar,BorderLayout.SOUTH);
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
   
    Pprinc.add(Pcond,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 6), 0, 0));
    Pcond.add(opFactC, null);
    Pcond.add(opIgnTF, null);
    Pcond.add(cLabel7, null);
    Pcond.add(kilRestE, null);
    Pcond.add(emp_codiL, null);
    Pcond.add(emp_codiE, null);
    Pcond.add(sbe_codiE, null);
    Pcond.add(sbe_codiL, null);
    Pcond.add(Bacepta, null);
    Pcond.add(opInterno, null);
    Pcond.add(prv_codiE1, null);
    Pcond.add(prv_codiE, null);
    Pcond.add(cLabel2, null);
    Pcond.add(cLabel17, null);
    Pcond.add(fecfinE, null);
    Pcond.add(cLabel19, null);
    Pcond.add(acc_numeE1, null);
    Pcond.add(cLabel5, null);
    Pcond.add(acc_numeE, null);
    Pcond.add(cLabel1, null);
    Pcond.add(feciniE, null);
    Pcond.add(cLabel4, null);
    Pcond.add(PAcum, null);
    prv_codiE1.iniciar(dtStat, this, vl, EU);
    prv_codiE.iniciar(dtStat, this, vl, EU);
    confGridCab();
    confGridLin();
}

    @Override
  public void iniciarVentana() throws Exception
  {

    emp_codiE.iniciar(dtStat,this,vl,EU);
    sbe_codiE.iniciar(dtStat,this,vl,EU);
    sbe_codiE.setFieldEmpCodi(emp_codiE.getTextField());
    sbe_codiE.setAceptaNulo(EU.getSbeCodi()==0);
    Pcond.setDefButton(Bacepta);
    PAcum.setEnabled(false);
    opFactC.addItem("Todos", "T");
    opFactC.addItem("Factur.", "F");
    opFactC.addItem("Sin Fact.", "S");
    GregorianCalendar gc = new GregorianCalendar();
    gc.setTime(new java.util.Date(System.currentTimeMillis()));
    gc.add(GregorianCalendar.MONTH, -1);
    feciniE.setDate(gc.getTime());
    fecfinE.setText(Formatear.getFechaAct("dd-MM-yy"));

    activarEventos();

  }
  void activarEventos()
  {
    Bacepta.addActionListener(new java.awt.event.ActionListener()
   {
     public void actionPerformed(ActionEvent e)
     {
       Bacepta_actionPerformed(e);
     }
   });
   jtCab.tableView.getSelectionModel().addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent e)
      {
        if (e.getValueIsAdjusting() || jtCab.isVacio() || ! jtCab.isEnabled())
          return;
        verLinAlbaran(jtCab.getSelectedRow());
    }
    });

  }

  void Bacepta_actionPerformed(ActionEvent e)
  {
    try {
      if (!emp_codiE.controla())
      {
        mensajeErr("Empresa NO valida");
        return;
      }
      if (!sbe_codiE.controla())
      {
        mensajeErr("SubEmpresa NO valida");
        return;
      }
      if (feciniE.isNull() || feciniE.getError())
      {
        mensaje("Fecha Inicio ... NO valida");
        feciniE.requestFocus();
        return;
      }
      if (fecfinE.isNull() || fecfinE.getError())
      {
        mensajeErr("Fecha Final ... NO valida");
        fecfinE.requestFocus();
        return;
      }
    } catch (Exception k)
    {
      Error("Error al comprobar validez campos",k);
      return;
    }
    LIMINF=kilRestE.getValorDec()*-1;
    LIMSUP=kilRestE.getValorDec();
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
    String s,s1;
//    sum(l.acl_canti)  as acl_canti, " +
//        " sum(acl_canti * (acl_prcom - c.acc_impokg)) as acc_imppen
    s = "select c.acc_serie,c.acc_nume, c.acc_ano,c.emp_codi, c.acc_fecrec, "+
        " c.prv_codi,pv.prv_nomb "+
        " from v_albacoc c left join v_proveedo as pv on pv.prv_codi = c.prv_codi  " +
        " where c.acc_fecrec >= TO_DATE('" + feciniE.getText() + "','dd-MM-yy')" +
        " AND c.acc_fecrec <= TO_DATE('" + fecfinE.getText() + "','dd-MM-yy') "+
        " and c.emp_codi = "+emp_codiE.getValorInt()+
        (opInterno.isSelected()?"":" and c.acc_serie != 'Y' ")+
        (sbe_codiE.getValorInt()==0?"":" and c.sbe_codi = "+sbe_codiE.getValorInt());
    if (!prv_codiE.isNull())
      s += " AND c.prv_codi >= " + prv_codiE.getText() + "AND c.prv_codi <= " + prv_codiE1.getText();
    if (acc_numeE.getValorInt() != 0)
      s += " and c.acc_nume >= " + acc_numeE.getValorInt() + " and c.acc_nume <= " +
          acc_numeE1.getValorInt();
    if (!opFactC.getValor().equals("T"))
    {
      // Select con Lineas FACTURADAS.
      s1 = "SELECT l.acc_nume FROM  v_albacol as l where " +
          " c.acc_ano = l.acc_ano " +
          " and c.emp_codi = l.emp_codi " +
          " and c.acc_serie = l.acc_serie " +
          " and c.acc_nume = l.acc_nume " +
          (opInterno.isSelected()?"":" and c.acc_serie != 'Y' ")+
          " and c.emp_codi = "+emp_codiE.getValorInt()+
          (sbe_codiE.getValorInt()==0?"":" and c.sbe_codi = "+sbe_codiE.getValorInt())+
          " and l.acl_canti - l.acl_canfac not BETWEEN  "+LIMINF+"  AND "+LIMSUP;
      if (opFactC.getValor().equals("F")) // Ver lo Facturado.
        s += " and ("+(opIgnTF.isSelected()?"":"c.acc_totfra != 0 OR ")+ "  NOT exists ( "+s1+"))";
      else // Ver lo No facturado
        s+=" and "+(opIgnTF.isSelected()?"":"c.acc_totfra = 0 and ")+"  EXISTS ("+s1+") ";
    }
    s += " ORDER BY c.acc_ano,c.acc_serie,c.acc_nume";
//    debug("select: "+s);
    try {
      jtCab.setEnabled(false);
      jtCab.removeAllDatos();
      jtLin.setEnabled(false);
      jtLin.removeAllDatos();
      if (!dtCon1.select(s))
      {
        mensajeErr("NO Encontrados Albaranes");
        mensaje("");
        this.setEnabled(true);
        return;
      }
      s1 = "SELECT sum(l.acl_canti)  as acl_canti," +
          " sum((acl_canti) * (acl_prcom - c.acc_impokg)) as acc_imppen, " +
          " sum(l.acl_canfac) as acl_canfac, "+
          " sum (l.acl_canfac* (acl_prcom  - c.acc_impokg)) as acc_impfra "+
          " FROM v_albacol as l,v_albacoc as c " +
          "  WHERE c.EMP_CODI =  " + emp_codiE.getValorInt() +
         (sbe_codiE.getValorInt()==0?"":" and c.sbe_codi = "+sbe_codiE.getValorInt())+
          " and l.acc_ano = ? " +
          " and l.acc_serie = ? " +
          " and l.acc_nume = ? " +
          " and c.acc_ano = l.acc_ano " +
          " and c.emp_codi = l.emp_codi " +
          " and c.acc_serie = l.acc_serie " +
          " and c.acc_nume = l.acc_nume ";
//      debug(s1);
      // Total
      psTotal = ct.prepareStatement(s1);
      s = s1 + " and ( " + (opIgnTF.isSelected() ? "" : "c.acc_totfra != 0 or ") +
          " l.acl_canti - l.acl_canfac  BETWEEN  " + LIMINF + "  AND " + LIMSUP + " )";
      PreparedStatement psFact = ct.prepareStatement(s);
      impTot = 0;
      impFra = 0;
      impPen = 0;
      kilTot = 0;
      kilFra = 0;
      kilPen = 0;
      double k, i;
      jtCab.panelG.setVisible(false);
      do
      {
        Vector v = new Vector();
        v.add(dtCon1.getString("acc_ano"));
        v.add(dtCon1.getString("acc_serie"));
        v.add(dtCon1.getString("acc_nume"));
        v.add(dtCon1.getFecha("acc_fecrec", "dd-MM-yy"));
        v.add(dtCon1.getString("prv_codi"));
        v.add(dtCon1.getString("prv_nomb"));
        insAcum(v, psTotal, dtCon1);
        k = kilos; // Kilos Totales (facturados y sin facturar)
        i = importe; // Importe Total
        kilTot += kilos;
        impTot += importe;
        insAcumFra(v, psFact, dtCon1);
        kilFra += kilos; // Kilos Facturados
        impFra += importe; // Importe Fra.
        kilos = k - kilos;
        importe = i - importe;
        v.add("" + kilos);
        v.add("" + importe);
        kilPen += kilos;
        impPen += importe;
        jtCab.addLinea(v);
      }
      while (dtCon1.next());
      javax.swing.SwingUtilities.invokeLater(new Thread()
      {
        public void run()
        {
          consulta1();
        }
      });
      } catch (SQLException k)
      {
        Error("Error al buscar condiciones Albaranes",k);
        return;
      }

    }
    void consulta1()
    {
      try
      {
     kilTotE.setValorDec(kilTot);
     impTotE.setValorDec(impTot);
     kilFactE.setValorDec(kilFra);
     impFactE.setValorDec(impFra);
     kilPendE.setValorDec(kilPen);
     impPendE.setValorDec(impPen);
     String s = "SELECT l.*,c.acc_impokg,pr.pro_nomb FROM v_albacoc as c,v_albacol as l left join v_articulo as pr " +
         " on pr.pro_codi = l.pro_codi " +
         "  WHERE c.EMP_CODI = " +emp_codiE.getValorInt()+
         (sbe_codiE.getValorInt()==0?"":" and c.sbe_codi = "+sbe_codiE.getValorInt())+
         " and l.acc_ano = ? " +
         " and l.acc_serie = ? " +
         " and l.acc_nume = ? " +
         " and c.acc_ano = l.acc_ano " +
         " and c.emp_codi = l.emp_codi " +
         " and c.acc_serie = l.acc_serie " +
         " and c.acc_nume = l.acc_nume "+
         (opFactC.getValor().equals("T") ? "" :
          " and l.acl_canti - l.acl_canfac " +
          (opFactC.getValor().equals("F") ? "":"NOT" ) +
          " BETWEEN  "+LIMINF+"  AND "+LIMSUP);
//     debug("Select lineas: " + s);
     psTotal = ct.prepareStatement(s);
     jtCab.setEnabled(true);
     jtCab.panelG.setVisible(true);
     jtCab.requestFocusInicio();
     verLinAlbaran(0);

      this.setEnabled(true);
      mensajeErr("Consulta Realizada...");
      mensaje("");
    } catch (SQLException k)
  {
    Error("Error al buscar condiciones Albaranes",k);
    return;
  }

  }
  /**
   * Inserta Acumulados
   * @param v Vector Donde poner los valores
   * @param ps PreparedStatement Donde suma los kilos e importes
   * @param dt DatosTabla De donde coger el numero de albaran
   * @throws SQLException
   */
  void insAcum(Vector v, PreparedStatement ps, DatosTabla dt) throws SQLException
  {
    ps.setInt(1, dt.getInt("acc_ano"));
    ps.setString(2, dt.getString("acc_serie"));
    ps.setInt(3, dt.getInt("acc_nume"));
    ResultSet rs = ps.executeQuery();
    rs.next();
    v.add(rs.getString("acl_canti"));
    v.add(rs.getString("acc_imppen"));
    kilos=rs.getDouble("acl_canti");
    importe=rs.getDouble("acc_imppen");
  }

  void insAcumFra(Vector v, PreparedStatement ps, DatosTabla dt) throws SQLException
  {
    ps.setInt(1, dt.getInt("acc_ano"));
    ps.setString(2, dt.getString("acc_serie"));
    ps.setInt(3, dt.getInt("acc_nume"));
    ResultSet rs = ps.executeQuery();
    rs.next();
    v.add(rs.getString("acl_canfac"));
    v.add(rs.getString("acc_impfra"));
    kilos = rs.getDouble("acl_canfac");
    importe = rs.getDouble("acc_impfra");
  }

  void confGridCab()  throws Exception
  {
    Vector v=new Vector();
    jtCab.setMaximumSize(new Dimension(739, 194));
    jtCab.setMinimumSize(new Dimension(739, 194));
    jtCab.setPreferredSize(new Dimension(739, 194));

    v.add("Ejer."); // 0
    v.add("S"); // 1
    v.add("Numero"); // 2
    v.add("Fec.Rec."); // 3
    v.add("Prv."); // 4
    v.add("Nombre Prv."); // 5
    v.add("Kilos T"); // 6
    v.add("Imp.T"); // 7
    v.add("Kilos F"); //8
    v.add("Imp.F"); // 9
    v.add("Kilos P"); // 10
    v.add("Imp. P"); // 11
    jtCab.setCabecera(v);
    jtCab.setAnchoColumna(new int[]{40,15,50,70,50,150,80,80,80,80,80,80});
    jtCab.setAlinearColumna(new int[]{2,0,2,1,2,0,2,2,2,2,2,2});
    jtCab.setFormatoColumna(6,"----,--9.9");
    jtCab.setFormatoColumna(7,"--,---,--9.9");
    jtCab.setFormatoColumna(8,"----,--9.9");
    jtCab.setFormatoColumna(9,"--,---,--9.9");
    jtCab.setFormatoColumna(10,"----,--9.9");
    jtCab.setFormatoColumna(11,"--,---,--9.9");
  }
  void confGridLin() throws Exception
  {
    Vector v=new Vector();
    v.add("Prod."); // 0
    v.add("Nombre Producto"); // 1
    v.add("Unid."); // 2
    v.add("Precio"); // 3
    v.add("Kilos"); // 4
    v.add("Importe"); // 5
    v.add("Kilos.Fra"); // 6
    v.add("Kilos.Pend"); // 7
    jtLin.setCabecera(v);
    jtLin.setAnchoColumna(new int[]{50,200,50,70,60,80,60,60});
    jtLin.setAlinearColumna(new int[]{2,0,2,2,2,2,2,2});
    jtLin.setFormatoColumna(2,"----9");
    jtLin.setFormatoColumna(3,"---9.99");
    jtLin.setFormatoColumna(4,"--,--9.9");
    jtLin.setFormatoColumna(5,"---,--9.9");
    jtLin.setFormatoColumna(6,"--,--9.9");
    jtLin.setFormatoColumna(7,"--,--9.9");
    jtLin.setAjustarGrid(true);
  }
  void verLinAlbaran(int row)
  {
    try {

        psTotal.setInt(1, jtCab.getValorInt(row,0));
        psTotal.setString(2, jtCab.getValString(row,1));
        psTotal.setInt(3,jtCab.getValorInt(row,2));
        ResultSet rs = psTotal.executeQuery();
        jtLin.removeAllDatos();
        while (rs.next())
        {
          Vector v=new Vector();
          v.addElement(rs.getString("pro_codi"));
          v.addElement(rs.getString("pro_nomb"));
          v.addElement(""+rs.getDouble("acl_numcaj"));
          v.addElement(""+(rs.getDouble("acl_prcom")-rs.getDouble("acc_impokg")));
          v.addElement(""+rs.getDouble("acl_canti"));
          v.addElement(""+(rs.getDouble("acl_canti")*(rs.getDouble("acl_prcom")-rs.getDouble("acc_impokg"))));
          v.addElement(""+rs.getDouble("acl_canfac"));
          v.addElement(""+(rs.getDouble("acl_canti")-rs.getDouble("acl_canfac")));
          jtLin.addLinea(v);
        }
        jtLin.requestFocusInicio();
    } catch (Exception k)
    {
      Error("Error al ver Lineas",k);
    }



  }
}
