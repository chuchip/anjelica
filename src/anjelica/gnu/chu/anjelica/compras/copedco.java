package gnu.chu.anjelica.compras;

import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import java.sql.*;
import gnu.chu.Menu.*;
import gnu.chu.anjelica.listados.Listados;
import java.awt.*;
import java.util.*;
import javax.swing.BorderFactory;
import gnu.chu.camposdb.*;
import javax.swing.event.*;
import java.awt.event.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.*;
/**
* Consulta y Listado Pedidos de compras 
 *
 * Permite consultar las compras realizadas de un producto
 * dentro de unas fechas.
 * Tambien permite delimitar por proveedor y por albaran.
 * <p>Copyright: Copyright (c) 2005-2016
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los términos de la Licencia Pública General de GNU segun es publicada por
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
 * @version 1.1 Incluida opcion de Listar los Pedidos de Compras
 */

public class copedco extends ventana
{
  String s;
  CButton Bimpri=new CButton(Iconos.getImageIcon("print"));

  CComboBox div_codiE = new CComboBox();
  boolean verPrecio;
  CPanel Pprinc = new CPanel();
  CPanel Pcond = new CPanel();
  Cgrid jtCab = new Cgrid(9);
  Cgrid jtLin = new Cgrid(13);
  CLabel cLabel4 = new CLabel();
  prvPanel prv_codiE = new prvPanel();
  CComboBox pcc_estadE = new CComboBox();
  CLabel cLabel7 = new CLabel();
  CTextField pcc_fecpedE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel cLabel3 = new CLabel();
  CButton Baceptar = new CButton(Iconos.getImageIcon("check"));
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  boolean modCons=false;
  int pcoNume=0,ejeNume=0;
  CLabel cLabel1 = new CLabel();
  CComboBox pcc_estrecE = new CComboBox();
  CLabel cLabel2 = new CLabel();
  empPanel emp_codiE = new empPanel();
  CLabel cLabel5 = new CLabel();
  CTextField pcc_fecrecE = new CTextField(Types.DATE,"dd-MM-yyyy");
  sbePanel sbe_codiE = new sbePanel();
  CLabel cLabel6 = new CLabel();

  public copedco(EntornoUsuario eu, Principal p)
 {
   this(eu,p,null);
 }

 public copedco(EntornoUsuario eu, Principal p, Hashtable ht)
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
         verPrecio = Boolean.valueOf(ht.get("verPrecio").toString()).
             booleanValue();

     }
     setTitulo("Consulta/Listado Pedidos Compras");
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

 public copedco(gnu.chu.anjelica.menu p, EntornoUsuario eu)
 {
   this(p, eu, null);
 }

 public copedco(gnu.chu.anjelica.menu p, EntornoUsuario eu, Hashtable ht)
 {
   EU = eu;
   vl = p.getLayeredPane();
   eje = false;

   try
   {
     if (ht != null)
     {
       if (ht.get("verPrecio") != null)
         verPrecio = Boolean.parseBoolean(ht.get("verPrecio").toString());

     }
     setTitulo("Consulta/Listado Pedidos Compras");

     jbInit();
   }
   catch (Exception e)
   {
    ErrorInit(e);
   }
 }
 public copedco(EntornoUsuario eu,javax.swing.JLayeredPane lp,boolean verPrec) throws Exception
 {
   EU = eu;
   vl=lp;
   EU=eu;
   verPrecio=verPrec;
   modCons=true;
   setTitulo("Consulta/Listado Pedidos Compras ");
   jbInit();
 }
 private void jbInit() throws Exception
 {
    iniciarFrame();
    this.setSize(new Dimension(666, 522));
    this.setVersion(" (2008-02-01)" + (verPrecio ? "- Ver Precios" : ""));
    Pprinc.setLayout(gridBagLayout1);
    Pcond.setBorder(BorderFactory.createRaisedBevelBorder());
    Pcond.setMaximumSize(new Dimension(500, 49));
    Pcond.setMinimumSize(new Dimension(500, 49));
    Pcond.setPreferredSize(new Dimension(500, 49));
    Pcond.setLayout(null);
    statusBar = new StatusBar(this);
    conecta();

    Bimpri.setToolTipText("Imprimir Resultados de Consulta");
    Bimpri.setPreferredSize(new Dimension(24, 24));
    Bimpri.setMinimumSize(new Dimension(24, 24));
    Bimpri.setMaximumSize(new Dimension(24, 24));
    pcc_fecpedE.setToolTipText("Fecha de pedido Superior A");
    cLabel2.setRequestFocusEnabled(true);
    cLabel2.setToolTipText("");
    cLabel2.setText("Emp.");
    cLabel2.setBounds(new Rectangle(5, 2, 34, 17));
    emp_codiE.setBounds(new Rectangle(39, 2, 44, 17));
    cLabel3.setRequestFocusEnabled(true);
    cLabel5.setRequestFocusEnabled(true);
    cLabel5.setToolTipText("Fecha de Pedido superior a.");
    cLabel5.setBounds(new Rectangle(191, 3, 72, 17));
    cLabel5.setText("Fec.Ped.Sup.");
    pcc_fecrecE.setBounds(new Rectangle(411, 2, 74, 17));
    pcc_fecrecE.setToolTipText("Fecha Entrega Inferior A");
    sbe_codiE.setBounds(new Rectangle(141, 2, 44, 17));
    cLabel6.setBounds(new Rectangle(87, 2, 54, 17));
    cLabel6.setText("SubEmp");
    cLabel6.setToolTipText("");
    cLabel6.setRequestFocusEnabled(true);
    statusBar.add(Bimpri, new GridBagConstraints(9, 0, 1, 2, 0.0, 0.0
                                                 , GridBagConstraints.EAST,
                                                 GridBagConstraints.VERTICAL,
                                                 new Insets(0, 5, 0, 0), 0, 0));

    cLabel4.setText("Proveedor");
    cLabel4.setBounds(new Rectangle(2, 23, 59, 17));
    prv_codiE.setAncTexto(40);
    prv_codiE.setBounds(new Rectangle(59, 23, 326, 17));
    cLabel7.setDoubleBuffered(false);
    cLabel7.setText("Est. Ped");
    cLabel7.setBounds(new Rectangle(389, 23, 57, 17));
    cLabel3.setText("Fec.Ent.Inf.");
    cLabel3.setBounds(new Rectangle(344, 2, 65, 17));
    Baceptar.setBounds(new Rectangle(545, 20, 107, 23));
    Baceptar.setMargin(new Insets(0, 0, 0, 0));
    Baceptar.setText("Aceptar F4");
    jtCab.setMaximumSize(new Dimension(465, 143));
    jtCab.setMinimumSize(new Dimension(465, 143));
    jtCab.setPreferredSize(new Dimension(465, 143));
    jtLin.setMaximumSize(new Dimension(468, 184));
    jtLin.setMinimumSize(new Dimension(468, 184));
    jtLin.setPreferredSize(new Dimension(468, 184));
    Pprinc.setInputVerifier(null);
    pcc_estadE.setBounds(new Rectangle(447, 23, 91, 17));
    pcc_fecpedE.setBounds(new Rectangle(264, 2, 74, 17));
    cLabel1.setText("Est. Recep");
    cLabel1.setBounds(new Rectangle(488, 2, 63, 17));
    pcc_estrecE.setBounds(new Rectangle(551, 2, 99, 17));
    this.getContentPane().add(Pprinc,  BorderLayout.CENTER);
    this.getContentPane().add(statusBar,  BorderLayout.SOUTH);

    Vector v=new Vector();
    v.add("Prov"); // 0
    v.add("Nombre Prov"); // 1
    v.add("Eje"); // 2
    v.add("Pedido"); // 4
    v.add("Fec.Ped"); // 5
    v.add("Fec.Rec."); // 6
    v.add("Estado"); // 7
    v.add("Albaran"); // 8
    v.add("Comentario"); // 9
    jtCab.setCabecera(v);
    jtCab.setAlinearColumna(new int[]{2,0,2,2,1,1,1,2,0});
    jtCab.setAnchoColumna(new int[]{40,120,60,60,80,80,40,60,150});
    Vector vl = new Vector();
    vl.add("Prod"); // 0
    vl.add("Nombre Prod"); // 1
    vl.add("Un.Ped."); // 2
    vl.add("KG Ped."); // 3
    vl.add("Prec.Ped."); // 4
    vl.add("Un.Conf."); // 5
    vl.add("KG Conf."); // 6
    vl.add("Prec.Conf."); // 7
    vl.add("Unid.Fra."); // 8
    vl.add("KG Fra."); // 9
    vl.add("Prec.Fra."); // 10
    vl.add("Mon."); // 11
    vl.add("Coment"); // 12
    jtLin.setCabecera(vl);
    jtLin.setAnchoColumna(new int[]
                          {60, 200, 46, 60, 55, 60, 60, 70, 60, 57, 55, 80, 150});
    jtLin.setAlinearColumna(new int[]
                            {2, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0});

    Pprinc.add(Pcond,     new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 9, 0));
    Pcond.add(cLabel4, null);
    Pcond.add(Baceptar, null);
    Pcond.add(pcc_estadE, null);
    Pcond.add(cLabel7, null);
    Pcond.add(prv_codiE, null);
    Pcond.add(pcc_estrecE, null);
    Pcond.add(cLabel1, null);
    Pcond.add(cLabel2, null);
    Pcond.add(pcc_fecrecE, null);
    Pcond.add(cLabel3, null);
    Pcond.add(pcc_fecpedE, null);
    Pcond.add(emp_codiE, null);
    Pcond.add(cLabel6, null);
    Pcond.add(cLabel5, null);
    Pcond.add(sbe_codiE, null);
    Pprinc.add(jtCab,   new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    Pprinc.add(jtLin,   new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
 }
 public void iniciarVentana() throws Exception
 {
   emp_codiE.iniciar(dtStat,this,vl,EU);
   sbe_codiE.iniciar(dtStat,this,vl,EU);
   sbe_codiE.setFieldEmpCodi(emp_codiE.getTextField());
   sbe_codiE.setAceptaNulo(EU.getSbeCodi()==0);
   Pcond.setButton(KeyEvent.VK_F4,Baceptar);
   prv_codiE.iniciar(dtStat,this,vl,EU);
   pcc_estadE.addItem("-----","-");
   pcc_estadE.addItem("Pedido","N");
   pcc_estadE.addItem("Confirmado", "C");
   pcc_estadE.addItem("PreFacturado", "F");

   pcc_estrecE.addItem("-----", "-");
   pcc_estrecE.addItem("Pendiente", "P");
   pcc_estrecE.addItem("Cancelado", "C");
   pcc_estrecE.addItem("Recibido", "R");
   pcc_estrecE.setValor("P");
   prv_codiE.setAceptaNulo(true);
   s = "SELECT div_codi,div_codedi FROM v_divisa order by div_codedi";
   if (!dtStat.select(s))
     throw new SQLException("NO HAY NINGUNA DIVISA DEFINIDA");
   div_codiE.addItem(dtStat);
   emp_codiE.setValorInt(EU.em_cod);
   activarEventos();
 }
 void activarEventos()
 {
   jtCab.tableView.getSelectionModel().addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent e)
      {
        if (jtCab.isVacio() || !jtCab.isEnabled())
          return;
        if (e.getValueIsAdjusting())
          return;
        cambiaPedido0();
      }
    });
    jtCab.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent e)
      {
        if (e.getClickCount()<2 || jtCab.isVacio() && !modCons)
          return;
        elegir();
      }
    });
    jtCab.tableView.addKeyListener(new KeyAdapter()
    {
      public void keyPressed(KeyEvent e)
      {
        if (jtCab.isVacio() && !modCons)
          return;

        if (e.getKeyCode() == e.VK_INSERT)
          elegir();
      }
    });

    Baceptar.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Baceptar_actionPerformed();
      }
    });
    Bimpri.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Bimpri_actionPerformed();
      }
    });

  }


  void Baceptar_actionPerformed()
  {
    try
    {
      if (! emp_codiE.controla())
      {
        mensajeErr("Empresa NO VALIDA");
        return;
      }

      if (!sbe_codiE.controla())
      {
        mensajeErr("SubEmpresa NO VALIDA");
        return;
      }

      if (!prv_codiE.controla(true))
      {
        mensajeErr(prv_codiE.getMsgError());
        return;
      }
      if (pcc_fecpedE.getError())
      {
        mensajeErr("Fecha PEDIDO NO valida");
        return;
      }
      s = "SELECT c.*,p.prv_nomb FROM pedicoc as c,v_proveedo as p " +
          " WHERE c.emp_codi = " + emp_codiE.getValorInt() +
          (sbe_codiE.getValorInt()==0?"": " and c.sbe_codi = "+sbe_codiE.getValorInt())+
          " and p.prv_codi = c.prv_codi ";
      if (!prv_codiE.isNull())
        s += " AND c.prv_codi = " + prv_codiE.getValorInt();

      if (!pcc_fecpedE.isNull())
        s += " AND c.pcc_fecped >= TO_DATE('" + pcc_fecpedE.getText() + "','dd-MM-yyyy')";

      if (!pcc_fecrecE.isNull())
        s += " AND c.pcc_fecrec <= TO_DATE('" + pcc_fecrecE.getText() + "','dd-MM-yyyy')";
      if (!pcc_estadE.getValor().equals("-"))
        s += " AND c.pcc_estad = '" + pcc_estadE.getValor() + "'";

      if (!pcc_estrecE.getValor().equals("-"))
        s += " AND c.pcc_estrec = '" + pcc_estrecE.getValor() + "'";

      s += " ORDER BY c.eje_nume,c.pcc_nume desc";


//      debug("S: "+s);
      jtCab.setEnabled(false);
      jtCab.removeAllDatos();
      jtLin.removeAllDatos();
      if (! dtCon1.select(s))
      {
        mensajeErr("NO encontrados Pedidos para estas condiciones");
        prv_codiE.requestFocus();
        jtCab.setEnabled(true);
        return;
      }
      do
      {
        Vector v=new Vector();
        v.addElement(dtCon1.getString("prv_codi"));
        v.addElement(dtCon1.getString("prv_nomb"));
        v.addElement(dtCon1.getString("eje_nume"));
        v.addElement(dtCon1.getString("pcc_nume"));
        v.addElement(dtCon1.getFecha("pcc_fecped","dd-MM-yyyy"));
        v.addElement(dtCon1.getFecha("pcc_fecrec","dd-MM-yyyy"));
        v.addElement(pcc_estadE.getText(dtCon1.getString("pcc_estad")));
        v.addElement(dtCon1.getString("acc_nume"));
        v.addElement(dtCon1.getString("pcc_comen"));
        jtCab.addLinea(v);
      } while (dtCon1.next());
      jtCab.setEnabled(true);
      jtCab.requestFocusInicio();
      cambiaPedido();
//      jtCab.requestFocusInicio();
    } catch (Exception k)
    {
      Error("Error al Buscar Datos de Pedidos",k);
    }
  }
  void cambiaPedido0()
  {
    try {
      cambiaPedido();
    } catch (Exception k)
    {
      Error("Error Al buscar datos de lina de pedido",k);
    }
  }
 void cambiaPedido() throws Exception
 {
   jtLin.removeAllDatos();
   s="SELECT * FROM pedicol WHERE emp_codi = "+EU.em_cod+
       " and eje_nume = "+jtCab.getValorInt(2)+
       " and pcc_nume = "+jtCab.getValorInt(3)+
       " order by pcl_numli";
   if (! dtCon1.select(s))
   {
     mensajeErr("NO encontradas LINEAS de Pedido");
     return;
   }

   do
   {
     Vector v=new Vector();
     v.addElement(dtCon1.getString("pro_codi"));
     v.addElement(dtCon1.getString("pro_nomb"));
     v.addElement(dtCon1.getString("pcl_nucape"));
     v.addElement(dtCon1.getString("pcl_cantpe"));
     if (verPrecio)
       v.addElement(dtCon1.getString("pcl_precpe"));
      else
        v.addElement("");
     v.addElement(dtCon1.getString("pcl_nucaco"));
     v.addElement(dtCon1.getString("pcl_cantco"));
     if (verPrecio)
       v.addElement(dtCon1.getString("pcl_precco"));
     else
       v.addElement("");

     v.addElement(dtCon1.getString("pcl_nucafa"));
     v.addElement(dtCon1.getString("pcl_cantfa"));
     if (verPrecio)
       v.addElement(dtCon1.getString("pcl_precfa"));
     else
       v.addElement("");
     if (verPrecio)
       v.addElement(div_codiE.getText(dtCon1.getString("div_codi")));
     else
       v.addElement("");
     v.addElement(dtCon1.getString("pcl_comen"));
     jtLin.addLinea(v);
   } while (dtCon1.next());
//   jtLin.requestFocusInicio();
 }
 public int getNumPed()
 {
   return pcoNume;
 }
 public int getEjePed()
 {
   return ejeNume;
 }
 public void resetPed()
 {
   ejeNume=0;
   pcoNume=0;
 }
 void elegir()
 {
   ejeNume=jtCab.getValorInt(2);
   pcoNume=jtCab.getValorInt(3);
   prv_codiE.setText(jtCab.getValString(0));
   matar();
 }
 void Bimpri_actionPerformed()
  {
    new miThread("")
    {
      public void run()
      {
        listar();
      }
    };
  }

  void listar()
  {
    this.setEnabled(false);
    mensaje("Espere ... Generando listado");
    try
    {
       s = "SELECT c.*,l.*,p.prv_nomb FROM pedicoc as c,pedicol as l,v_proveedo as p " +
        " WHERE c.emp_codi = " + emp_codiE.getValorInt()+
        (sbe_codiE.getValorInt()==0?"": " and c.sbe_codi = "+sbe_codiE.getValorInt())+
        " and p.prv_codi = c.prv_codi "+
        " and c.emp_codi = l.emp_codi "+
        " and c.eje_nume = l.eje_nume "+
        " and c.pcc_nume = l.pcc_nume ";
    if (!prv_codiE.isNull())
      s += " AND c.prv_codi = " + prv_codiE.getValorInt();

    if (!pcc_fecpedE.isNull())
      s += " AND c.pcc_fecped >= TO_DATE('" + pcc_fecpedE.getText() + "','dd-MM-yyyy')";

    if (!pcc_fecrecE.isNull())
      s += " AND c.pcc_fecrec <= TO_DATE('" + pcc_fecrecE.getText() + "','dd-MM-yyyy')";

    if (!pcc_estadE.getValor().equals("-"))
      s += " AND c.pcc_estad = '" + pcc_estadE.getValor() + "'";

    if (!pcc_estrecE.getValor().equals("-"))
      s += " AND c.pcc_estrec = '" + pcc_estrecE.getValor() + "'";

    s += " ORDER BY c.eje_nume,c.pcc_nume desc,l.pcl_numli";

      if (!dtCon1.select(s))
      {
        mensajeErr("No encontrados registros para estos datos");
        mensaje("");
        this.setEnabled(true);
        return;
      }

      java.util.HashMap mp = Listados.getHashMapDefault();
      mp.put("pccFecped",pcc_fecpedE.getDate());
      mp.put("pccFecrec",pcc_fecrecE.getDate());
      mp.put("pccEstad",pcc_estadE.getText());
      mp.put("pccEstrec",pcc_estrecE.getText());
      mp.put("prvCodi",prv_codiE.getText());
      mp.put("prvNomb",prv_codiE.getTextNomb());
      mp.put("verPrecios",new Boolean(verPrecio));
      JasperReport jr;
      jr = Listados.getJasperReport(EU, "relpedcom");

      ResultSet rs;

      rs = dtCon1.getStatement().executeQuery(dtCon1.getStrSelect());

      JasperPrint jp = JasperFillManager.fillReport(jr, mp, new JRResultSetDataSource(rs));
      gnu.chu.print.util.printJasper(jp, EU);
      this.setEnabled(true);
      mensajeErr("Relacion Pedidos de Compras ... IMPRESO ");
      mensaje("");
      pcc_fecpedE.requestFocus();
    }
    catch (Exception k)
    {
      Error("Error al imprimir Pedido Venta", k);
    }

  }
}
