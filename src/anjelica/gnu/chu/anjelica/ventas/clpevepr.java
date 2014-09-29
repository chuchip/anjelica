package gnu.chu.anjelica.ventas;

/**
 *
 * <p>Título: clpevepr </p>
 * <p>Descripción: Consulta/Listado Pedidos de Ventas Agrupados por Productos</p>
 * <p>Copyright: Copyright (c) 2005-2012
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
import java.awt.event.*;
import gnu.chu.sql.*;
import javax.swing.event.*;
import java.text.*;

public class  clpevepr extends ventana  implements  JRDataSource
{
  final static int JT_CANTI=2;
  int linProd = 0;
  int linClien=0;
  boolean swRotoProd;
  CButton Bimpri=new CButton(Iconos.getImageIcon("print"));
  int empCodiS,ejeNumeS,pvcNumeS,cliCodiS;
  ventana padre=null;
  String s;
  boolean verPrecio;
  proPanel pro_codiE= new proPanel();
  prvPanel prv_codiE = new prvPanel();
  String ZONA = null;
  CPanel Pprinc = new CPanel();
  CPanel Pcondi = new CPanel();
  Cgrid jtProd = new Cgrid(3);
  Cgrid jtCli = new Cgrid(3);
  CLabel cLabel2 = new CLabel();
  CTextField pvc_feciniE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel3 = new CLabel();
  CTextField pvc_fecfinE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CComboBox alm_iniE = new CComboBox();
  CLabel cLabel14 = new CLabel();
  CComboBox alm_finE = new CComboBox();
  CLabel cLabel15 = new CLabel();
  CButton Baceptar = new CButton("<html><center>Aceptar<em>(F4)</EM></center></html>",
                                 Iconos.getImageIcon("check"));
  CLabel cLabel6 = new CLabel();
  CComboBox pvc_confirE = new CComboBox();
  CLabel cLabel8 = new CLabel();
  CTextField emp_codiE = new CTextField(Types.DECIMAL,"#9");
  CLabel cLabel10 = new CLabel();
  CTextField feeninE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel4 = new CLabel();
  CLinkBox tla_codiE = new CLinkBox();
  CLabel cLabel11 = new CLabel();
  proPanel profinE = new proPanel();
  CLabel cLabel12 = new CLabel();
  proPanel proiniE = new proPanel();
  CLinkBox cam_codiE = new CLinkBox();
  CLabel cLabel18 = new CLabel();
  Cgrid jtPed = new Cgrid(10);
  CLabel cLabel5 = new CLabel();
  CTextField feenfiE = new CTextField(Types.DATE, "dd-MM-yyyy");
  String condWhere;
  GridBagLayout gridBagLayout1 = new GridBagLayout();

  public clpevepr(EntornoUsuario eu, Principal p)
  {
    this(eu, p, new Hashtable());
  }

  public clpevepr(EntornoUsuario eu, Principal p, Hashtable ht)
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
        if (ht.get("zona") != null)
          ZONA = ht.get("zona").toString();
      }
      setTitulo("Cons Pedidos Ventas por Productos");


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

  public clpevepr(gnu.chu.anjelica.menu p, EntornoUsuario eu, Hashtable ht)
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
          verPrecio = Boolean.valueOf(ht.get("verPrecio").toString()).
              booleanValue();
        if (ht.get("zona") != null)
          ZONA = ht.get("zona").toString();
      }
      setTitulo("Cons. Pedidos Ventas por Productos");

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
    this.setSize(new Dimension(675, 519));
    this.setVersion("(2006-01-26)"+ (verPrecio ? " (CON PRECIOS) " : ""));
    statusBar = new StatusBar(this);

    if (dtStat==null)
      conecta();

    Bimpri.setMargin(new Insets(0, 0, 0, 0));
    Bimpri.setPreferredSize(new Dimension(24, 24));
    Bimpri.setMaximumSize(new Dimension(24, 24));
    Bimpri.setMinimumSize(new Dimension(24, 24));

    Bimpri.setToolTipText("Imprimir Relacion Productos en Pedidos");

    cLabel8.setText("Empresa");
    cLabel8.setBounds(new Rectangle(565, 3, 52, 17));
//    pvc_impresE.setBounds(new Rectangle(488, 39, 63, 16));
    cLabel10.setText("De Fec.Pedido");
    cLabel10.setBounds(new Rectangle(3, 42, 79, 17));
    cLabel4.setText("Tipo Listado");
    cLabel4.setBounds(new Rectangle(3, 3, 75, 17));
    tla_codiE.setAncTexto(20);
    tla_codiE.setBounds(new Rectangle(75, 3, 156, 17));
    cLabel11.setText("A Prod.");
    cLabel11.setBounds(new Rectangle(325, 22, 50, 17));
    cLabel12.setText("De Prod.");
    cLabel12.setBounds(new Rectangle(3, 22, 54, 17));
    cam_codiE.setAncTexto(25);
    cam_codiE.setBounds(new Rectangle(333, 3, 181, 17));
    cLabel18.setRequestFocusEnabled(true);
    cLabel18.setText("Cámara");
    cLabel18.setBounds(new Rectangle(280, 3, 48, 17));
    jtPed.setMaximumSize(new Dimension(324, 131));
    jtPed.setMinimumSize(new Dimension(324, 131));
    jtPed.setPreferredSize(new Dimension(324, 131));
    jtPed.setText("cgrid1");
    jtPed.setBuscarVisible(false);
    cLabel5.setText("A Fec.Entrega");
    cLabel5.setBounds(new Rectangle(484, 42, 87, 17));
    statusBar.add(Bimpri, new GridBagConstraints(9, 0, 1, 2, 0.0, 0.0
                                           , GridBagConstraints.EAST,
                                           GridBagConstraints.VERTICAL,
                                           new Insets(0, 5, 0, 0), 0, 0));
    cLabel6.setText("Conf.");
    cLabel6.setBounds(new Rectangle(448, 63, 39, 17));
    Pcondi.setMaximumSize(new Dimension(655, 89));
    Pcondi.setMinimumSize(new Dimension(655, 89));
    Pcondi.setPreferredSize(new Dimension(655, 89));
    Baceptar.setBounds(new Rectangle(528, 63, 121, 22));
    alm_finE.setBounds(new Rectangle(289, 63, 156, 17));
    alm_iniE.setBounds(new Rectangle(72, 63, 156, 17));
    cLabel14.setBounds(new Rectangle(3, 63, 67, 17));
    cLabel15.setBounds(new Rectangle(229, 63, 63, 17));
    pvc_confirE.setBounds(new Rectangle(479, 63, 46, 17));
    feenfiE.setBounds(new Rectangle(571, 42, 75, 18));
    feeninE.setBounds(new Rectangle(404, 42, 75, 17));
    cLabel2.setBounds(new Rectangle(317, 42, 87, 17));
    pvc_fecfinE.setBounds(new Rectangle(235, 42, 75, 17));
    cLabel3.setBounds(new Rectangle(160, 42, 75, 17));
    pvc_feciniE.setBounds(new Rectangle(81, 42, 75, 17));
    profinE.setBounds(new Rectangle(378, 22, 267, 17));
    proiniE.setBounds(new Rectangle(54, 22, 267, 17));
    emp_codiE.setBounds(new Rectangle(616, 3, 27, 17));
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);

    Baceptar.setMargin(new Insets(0, 0, 0, 0));
    Pprinc.setLayout(gridBagLayout1);
    Pcondi.setBorder(BorderFactory.createRaisedBevelBorder());
    Pcondi.setLayout(null);

    cLabel2.setText("De Fec.Entrega");
    cLabel3.setRequestFocusEnabled(true);
    cLabel3.setText("A Fec.Pedido");
    cLabel14.setText("De Almacen");
    cLabel15.setText("A Almacen");
    confJtPro();
    confJtCli();
    confJtPed();
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
    Pcondi.add(tla_codiE, null);
    Pcondi.add(cLabel4, null);
    Pcondi.add(pvc_fecfinE, null);
    Pcondi.add(cLabel3, null);
    Pcondi.add(cLabel2, null);
    Pcondi.add(pvc_feciniE, null);
    Pcondi.add(cLabel10, null);
    Pcondi.add(cLabel11, null);
    Pcondi.add(profinE, null);
    Pcondi.add(cLabel12, null);
    Pcondi.add(proiniE, null);
    Pcondi.add(cLabel14, null);
    Pcondi.add(alm_iniE, null);
    Pcondi.add(cLabel15, null);
    Pcondi.add(alm_finE, null);
    Pcondi.add(Baceptar, null);
    Pcondi.add(emp_codiE, null);
    Pcondi.add(cLabel8, null);
    Pcondi.add(cLabel6, null);
    Pcondi.add(pvc_confirE, null);
    Pcondi.add(feeninE, null);
    Pcondi.add(feenfiE, null);
    Pcondi.add(cLabel5, null);
    Pcondi.add(cam_codiE, null);
    Pcondi.add(cLabel18, null);
    Pprinc.add(Pcondi,   new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 2), 0, 0));

    Pprinc.add(jtCli,     new GridBagConstraints(1, 1, 1, 1, 0.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
    Pprinc.add(jtProd,   new GridBagConstraints(0, 1, 1, 2, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    Pprinc.add(jtPed,     new GridBagConstraints(1, 2, 1, 1, 0.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
    Bimpri.setEnabledParent(true);
  }

  public void iniciarVentana() throws Exception
  {
    Pcondi.setDefButton(Baceptar);
    tla_codiE.setFormato(Types.DECIMAL, "#9");

    Pprinc.setButton(KeyEvent.VK_F4, Baceptar);
    int tlaCodi = 0;
    s = "SELECT tla_codi,tla_nomb FROM tilialca order by tla_codi";
    if (dtStat.select(s))
      tlaCodi = dtStat.getInt("tla_codi");
    tla_codiE.addDatos(dtStat);
    tla_codiE.addDatos("99", "Definido Usuario");
    tla_codiE.setValorInt(tlaCodi);

    cam_codiE.texto.setMayusc(true);
    cam_codiE.setFormato(Types.CHAR, "XX", 2);

    gnu.chu.anjelica.pad.pdconfig.llenaDiscr(dtStat,cam_codiE,"AC",EU.em_cod);

    proiniE.iniciar(dtStat, this, vl, EU);
    profinE.iniciar(dtStat, this, vl, EU);

    pvc_feciniE.setAceptaNulo(false);
    pvc_fecfinE.setAceptaNulo(false);

    s = "SELECT alm_codi,alm_nomb FROM v_almacen ORDER BY alm_codi";
    dtCon1.select(s);
    int almIni = dtCon1.getInt("alm_codi");
    alm_iniE.setValor(almIni);
    alm_iniE.addItem(dtCon1);
    dtCon1.select(s);
    alm_finE.addItem(dtCon1);
    dtCon1.last();
    alm_finE.setValor(dtCon1.getInt("alm_codi"));


    pvc_confirE.addItem("Si", "S");
    pvc_confirE.addItem("No", "N");
    pvc_confirE.addItem("**", "-");
    pvc_feciniE.setText(Formatear.sumaDias(Formatear.getDateAct(), -15));
    pvc_fecfinE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
    emp_codiE.setValorInt(EU.em_cod);
    activarEventos();
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

    Baceptar.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Baceptar_actionPerformed();
      }
    });
    jtProd.addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent e)
      {
        if (e.getValueIsAdjusting() || ! jtProd.isEnabled() || jtProd.isVacio() )
          return;
        try {
          verDatCli(jtProd.getValString(0));
        } catch (Exception k)
        {
          Error("Error al Ver desglose de clientes",k);
        }
      }
    });

    jtCli.addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent e)
      {
        if (e.getValueIsAdjusting() || ! jtCli.isEnabled() || jtCli.isVacio() )
          return;
        try {
          verDatPed(jtProd.getValString(0), jtCli.getValorInt(0));
         } catch (Exception k)
        {
          Error("Error al Ver desglose de Pedidos",k);
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
        Vector v=new Vector();
        v.addElement((tla_codiE.getValorInt()==99?"":"L")+dtCon1.getString("pro_codi")); // 0
        v.addElement(dtCon1.getString("pro_nomb")); // 1
        v.addElement(dtCon1.getString("pvl_canti")); // 2
        jtProd.addLinea(v);
      } while (dtCon1.next());
      jtProd.requestFocusInicio();
      jtProd.setEnabled(true);
      jtCli.setEnabled(true);
      jtPed.setEnabled(true);
      verDatCli(jtProd.getValString(0,0));
    }
    catch (Exception k)
    {
      Error("Error al buscar pedidos", k);
    }
  }
  void verDatCli(String producto) throws Exception
  {
    if (! jtProd.isEnabled() ||  ! jtCli.isEnabled() || producto == null)
      return;
    producto=producto.trim();
    if (producto.equals(""))
      return;
    int proCodi=0;
    boolean swGrupo=producto.startsWith("L");
    if (swGrupo)
      proCodi=Integer.parseInt(producto.substring(1));
    else
      proCodi=Integer.parseInt(producto);
    s="SELECT cl.cli_codi, cl.cli_nomb, sum(pvl_unid) as pvl_canti FROM v_pedven as p, clientes as cl "+
        (swGrupo?", tilialpr as t ":"")+
        condWhere+
        (swGrupo?" and tla_orden = "+proCodi+" and p.pro_codi = t.pro_codi ":
         " and p.pro_codi = "+proCodi)+
        " and cl.cli_codi = p.cli_codi "+
        " group by cl.cli_codi,cl.cli_nomb "+
        " order by cl.cli_codi ";
//    debug(s);
    jtCli.setEnabled(false);
    jtCli.removeAllDatos();
    if (! dtCon1.select(s))
      return;
    do
    {
      Vector v=new Vector();
      v.addElement(dtCon1.getString("cli_codi"));
      v.addElement(dtCon1.getString("cli_nomb"));
      v.addElement(dtCon1.getString("pvl_canti"));
      jtCli.addLinea(v);
    } while (dtCon1.next());
    jtCli.requestFocusInicio();
    jtCli.setEnabled(true);
    verDatPed(producto,jtCli.getValorInt(0,0));
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
    if (feeninE.getError())
    {
      mensajeErr("Fecha Entrega INICIAL no es valida");
      feeninE.requestFocus();
      return false;
    }
    if (feenfiE.getError())
    {
      mensajeErr("Fecha Entrega FINAL no es valida");
      feenfiE.requestFocus();
      return false;
    }

    if (emp_codiE.getValorInt()==0)
    {
      mensajeErr("Debe introducir una empresa");
      emp_codiE.requestFocus();
      return false;
    }
    if (! ejecSelect)
      return true; // No ejecutar select, solo comprobar campos.

    s = "SELECT "+
        (tla_codiE.getValorInt()==99?"ar.pro_codi , ar.pro_nomb ":
         "t.tla_orden as pro_codi , gr.pro_desc as pro_nomb ")+
        " ,sum(pvl_unid) as pvl_canti FROM v_pedven as  p " +
        (tla_codiE.getValorInt()==99?",v_articulo as ar ":", tilialpr as t,tilialgr as gr ");

    condWhere=" where  1 = 1 "+
        (pvc_feciniE.isNull()?"":" AND pvc_fecped >= to_date('" + pvc_feciniE.getText() + "','dd-MM-yyyy')") +
        (pvc_fecfinE.isNull()?"": " and pvc_fecped <= {ts '" + pvc_fecfinE.getFecha("yyyy-MM-dd") + " 23:59:59'}" )+
        (feeninE.isNull()?"":" AND pvc_fecent >= to_date('" + feeninE.getText() + "','dd-MM-yyyy')") +
        (feenfiE.isNull()?"": " and pvc_fecent <= to_date('" + feenfiE.getFecha("yyyy-MM-dd") + "','dd-MM-yyyy')")+
        " and p.alm_codi >= " + alm_iniE.getValor() +
        " and p.alm_codi <= " + alm_finE.getValor() +
        " AND p.emp_codi = " + emp_codiE.getValorInt()+
        (pvc_confirE.getValor().equals("-")?"":" and pvc_confir = '" + pvc_confirE.getValor() + "'");
    s=s+ condWhere+
       (tla_codiE.getValorInt()==99?"  and ar.pro_codi = p.pro_codi ":
       " and p.pro_codi = t.pro_codi ")+
        (tla_codiE.getValorInt()!=99 || proiniE.getValorInt()==0?"": " and p.pro_codi >= "+proiniE.getValorInt())+
        (tla_codiE.getValorInt()!=99 || profinE.getValorInt()==0?"": " and p.pro_codi > "+profinE.getValorInt())+
        (tla_codiE.getValorInt()==99?"":" and gr.tla_codi = t.tla_codi "+
        " and gr.tla_orden = t.tla_orden ")+
       " group by  "+
       (tla_codiE.getValorInt()==99?"ar.pro_codi , ar.pro_nomb ":
         "t.tla_orden , gr.pro_desc ")+
        (tla_codiE.getValorInt()==99?" order by ar.pro_codi ": " order by t.tla_orden ");
//    debug(s);
    jtProd.setEnabled(false);
    jtCli.setEnabled(false);
    jtProd.removeAllDatos();
    jtCli.removeAllDatos();
    jtPed.removeAllDatos();
    if (!dtCon1.select(s))
    {
      mensajeErr("NO hay PEDIDOS que cumplan estas condiciones");
      tla_codiE.requestFocus();
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

  private void confJtPro()
  {
    Vector v=new Vector();
    v.addElement("Codigo"); // 0
    v.addElement("Nombre Producto"); // 1
    v.addElement("Unid.");// 2
    jtProd.setCabecera(v);
    jtProd.setMaximumSize(new Dimension(333, 345));
    jtProd.setMinimumSize(new Dimension(333, 345));
    jtProd.setPreferredSize(new Dimension(333, 345));
    jtProd.setBuscarVisible(false);
    jtProd.setAnchoColumna(new int[]{50,160,70});
    jtProd.setAlinearColumna(new int[]{2,0,2});
    jtProd.setFormatoColumna(2,"----9");
  }
    private void confJtCli() throws Exception
   {
     Vector v = new Vector();
     v.addElement("Cliente"); // 0
     v.addElement("Nombre Cliente"); // 1
     v.addElement("Unid."); // 2
     jtCli.setCabecera(v);
     jtCli.setMaximumSize(new Dimension(322, 208));
    jtCli.setMinimumSize(new Dimension(322, 208));
    jtCli.setPreferredSize(new Dimension(322, 208));
    jtCli.setBuscarVisible(false);
     jtCli.setPuntoDeScroll(50);
     jtCli.setAnchoColumna(new int[]{60, 160, 70});
     jtCli.setAlinearColumna(new int[] {2, 0, 2});
     jtCli.setFormatoColumna(2,"----9");
   }
   private void confJtPed() throws Exception
   {
     Vector v = new Vector();
     v.addElement("Pedido"); // 0
     v.addElement("Fec.Entr."); // 1
     v.addElement("Unid."); // 2
     v.addElement("Precio"); // 3
     v.addElement("CP");  // 4 Confirmado Precio
     v.addElement("Proveed"); // 5 Nombre Proveedor
     v.addElement("Fec.Cad"); // 6
     v.addElement("Conf"); // 7 Pedido Confirmado
     v.addElement("Alm."); // 8
     v.addElement("Comentario"); // 9 Comentario
     jtPed.setCabecera(v);
     jtPed.setAnchoColumna(new int[]{70,70,40,50,30,100,70,30,30,100});
     jtPed.setAlinearColumna(new int[]{2,1,2,2,1,0,1,2,0,0});
     jtPed.setFormatoColumna(2,"----9");
     jtPed.setFormatoColumna(3,"---9.99");
     jtPed.setFormatoColumna(4,"BSN");
     jtPed.setFormatoColumna(7,"BSN");
   }
   void verDatPed(String producto,int cliCodi) throws Exception
   {
     if (! jtPed.isEnabled() || ! jtCli.isEnabled())
       return;
     int proCodi = 0;
     boolean swGrupo = producto.startsWith("L");
     if (swGrupo)
       proCodi = Integer.parseInt(producto.substring(1));
     else
       proCodi = Integer.parseInt(producto);

     s="SELECT p.*,pv.prv_nomb FROM v_pedven as p"+
       "  left join v_proveedo as pv on pv.prv_codi = p.prv_codi  "+
      (swGrupo?", tilialpr as t ":"")+
      condWhere+
      (swGrupo?" and tla_orden = "+proCodi+" and p.pro_codi = t.pro_codi ":
       " and p.pro_codi = "+proCodi)+
       " AND p.cli_codi = "+cliCodi+
       " order by p.emp_codi,p.eje_nume,p.pvc_nume ";
//   debug(s);
       jtPed.removeAllDatos();
       if (! dtCon1.select(s))
       {
         mensajeErr("NO ENCONTRADOS PEDIDOS PARA ESTE CLIENTE Y PRODUCTO");
         return;
       }

       do
       {
         Vector v=new Vector();
         v.addElement(dtCon1.getString("eje_nume")+"/"+dtCon1.getString("pvc_nume"));
         v.addElement(dtCon1.getFecha("pvc_fecent","dd-MM-yy"));
         v.addElement((dtCon1.getString("pvl_tipo").equals("K")?dtCon1.getString("pvl_kilos"):dtCon1.getString("pvl_unid"))+dtCon1.getString("pvl_tipo"));
         v.addElement(dtCon1.getString("pvl_precio"));
         v.addElement(dtCon1.getInt("pvl_precon") != 0);
         v.addElement(dtCon1.getString("prv_nomb"));
         v.addElement(dtCon1.getFecha("pvl_feccad","dd-MM-yy"));
         v.addElement(dtCon1.getString("pvc_confir").equals("S"));
         v.addElement(dtCon1.getString("alm_codi"));
         v.addElement(dtCon1.getString("pvl_comen"));
         jtPed.addLinea(v);
       } while (dtCon1.next());
       jtPed.requestFocusInicio();
   }


   void Bimpri_actionPerformed()
    {
      try {
        if (jtProd.isVacio())
        {
          mensajeErr("Realize primero una consulta");
          return;
        }
        linProd=-1;
        linClien=0;
        jtProd.requestFocusInicio();
        swRotoProd=true;
        java.util.HashMap mp = new java.util.HashMap();
        mp.put("fecini",pvc_feciniE.getDate());
        mp.put("fecfin",pvc_fecfinE.getDate());

        JasperReport jr;
        jr = gnu.chu.print.util.getJasperReport(EU, "relpevepr");

        JasperPrint jp = JasperFillManager.fillReport(jr, mp, this);
        gnu.chu.print.util.printJasper(jp, EU);

        mensajeErr("Relacion Pedido Ventas ... IMPRESO ");
      }
      catch (Exception k)
      {
        Error("Error al imprimir Pedido Venta", k);
      }
    }

    public boolean next() throws JRException
    {
      if (linProd<0)
      {
        linProd = 0;
        return true;
      }
      if (linClien+1<jtCli.getRowCount())
      {
        swRotoProd=false;
        linClien++;
        return true;
      }
      if (linProd+1 >= jtProd.getRowCount())
        return false;
      linProd++;
      jtProd.requestFocus(linProd,0);
      swRotoProd=true;
      linClien=0;
      return true;
    }

    public Object getFieldValue(JRField jRField) throws JRException
    {
      String campo = jRField.getName();
      if (campo.equals("pro_codi"))
        return swRotoProd?jtProd.getValString(linProd,0):null;
      if (campo.equals("pro_nomb"))
        return swRotoProd?jtProd.getValString(linProd,1):null;
      if (campo.equals("pvl_canti"))
        return swRotoProd?jtProd.getValString(linProd,JT_CANTI):null;
      if (campo.equals("cli_nomb"))
        return jtCli.getValString(linClien,1);
      if (campo.equals("unidcli"))
        return new Integer(jtCli.getValorInt(linClien,2));

      throw new JRException("Campo: "+campo+ " No definido");
    }


}
