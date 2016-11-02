package gnu.chu.anjelica.almacen;

import gnu.chu.anjelica.pad.MantTarifa;
import gnu.chu.anjelica.ventas.pdalbara;
import gnu.chu.controles.*;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import gnu.chu.sql.*;
import java.awt.event.*;
import  gnu.chu.utilidades.*;
import gnu.chu.eventos.CambioListener;
import gnu.chu.eventos.CambioEvent;
import java.util.*;
/**
 *
 * <p>Titulo: pstockAct </p>
 * <p>Descripción: Panel Stock Actual. Muestra el stock actual y previsible de los productos
 * en un almacen o en todos. Permite ver el total de kilos por producto y desglosandolo por
 * proveedor y fecha caducidad </p>
* <p>Copyright: Copyright (c) 2005-2016
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los terminos de la Licencia Pública General de GNU seg�n es publicada por
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
 *
 */

public class pstockAct extends CPanel
{
  String feclim_clientes; // Fecha limite Albaran para buscar prod. de un cliente
  private int diaslim_clientes=60; // Fecha limite Albaran para buscar prod. de un cliente
  public final static char VER_CONSTOCK='S';
  public final static char VER_ULTVENTAS='U';
  public final static char VER_TODOS='T';
  String fefise;
  boolean incPedid=false; // Incluir Pedidos de Ventas y Compras en Consulta.
  int cliCodi=-1; // Ver Productos  vendidos a Clientes
  char opVerProd=VER_CONSTOCK;
  ArrayList<CButton> vt=new ArrayList();
  CButton btVolver=new CButton("Volver a Productos");
  AbstractButton Bprimero = null;
  double stock,unidad;
  int fam_codi;
  int emp_codi=1;
  ButtonGroup btFami = new ButtonGroup();
  BorderLayout borderLayout1 = new BorderLayout();
  CPanel Psuperior = new CPanel();
  CPanel Pprod = new CPanel();
  CPanel PDesProd = new CPanel();
  CPanel Pcondi = new CPanel();
  BorderLayout layout1 = new BorderLayout();
  CPanel Pfami = new CPanel();
  CLabel cLabel2 = new CLabel();
  CTextField pdc_fecpedE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CScrollPane SFami = new CScrollPane();
  CLabel cLabel3 = new CLabel();
  CComboBox agr_codiE = new CComboBox();
  DatosTabla dtCon1;
  DatosTabla dtStat;
  String s;
  ventana padre;
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CScrollPane Sprod = new CScrollPane();
  CLabel cLabel7 = new CLabel();
  CLinkBox tar_codiE = new CLinkBox();
  CLabel cLabel1 = new CLabel();
  CComboBox alm_codiE = new CComboBox();
  private int xFam = 0;
  private int yFam = 0;
  private boolean verPrecios=false;
  
  public pstockAct(DatosTabla dtCon1, DatosTabla dtStat, ventana papa,int emp_codi)
  {
    padre = papa;
    this.dtCon1 = dtCon1;
    this.dtStat = dtStat;
    try
    {
      jbInit();
    }
    catch (Exception e)
    {
      SystemOut.print(e);
    }
  }

  private void jbInit() throws Exception
  {
    this.setSize(new Dimension(720, 600));
    Psuperior.setBorder(BorderFactory.createRaisedBevelBorder());
    Psuperior.setMaximumSize(new Dimension(2147483647, 54));
    Psuperior.setMinimumSize(new Dimension(14, 64));
    Psuperior.setPreferredSize(new Dimension(14, 64));

    Psuperior.setLayout(layout1);
    this.setLayout(borderLayout1);
    Pprod.setBorder(BorderFactory.createEtchedBorder());
    Pprod.setLayout(gridBagLayout1);
    PDesProd.setBorder(BorderFactory.createLineBorder(Color.BLUE));
    PDesProd.setLayout(new GridBagLayout());

    Pcondi.setBorder(BorderFactory.createLoweredBevelBorder());
    Pcondi.setMaximumSize(new Dimension(32767, 20));
    Pcondi.setMinimumSize(new Dimension(1, 20));
    Pcondi.setPreferredSize(new Dimension(1, 20));
    Pcondi.setLayout(null);
    cLabel2.setText("Fec. Stock");
    cLabel2.setBounds(new Rectangle(3, 2, 60, 16));
    cLabel3.setText("Grupo Prod.");
    cLabel3.setBounds(new Rectangle(310, 2, 71, 16));
    SFami.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    SFami.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    SFami.setMaximumSize(new Dimension(32767, 24));
    SFami.setMinimumSize(new Dimension(23, 24));
    SFami.setPreferredSize(new Dimension(19, 24));
    cLabel7.setRequestFocusEnabled(true);
    cLabel7.setText("Tarifa");
    cLabel7.setBounds(new Rectangle(144, 2, 35, 16));
    tar_codiE.setAncTexto(25);
    tar_codiE.setBounds(new Rectangle(179, 2, 131, 16));
    cLabel1.setText("Almacen");
    cLabel1.setBounds(new Rectangle(513, 2, 53, 17));

    agr_codiE.setBounds(new Rectangle(381, 3, 126, 16));
    alm_codiE.setBounds(new Rectangle(569, 2, 146, 17));
    pdc_fecpedE.setBounds(new Rectangle(63, 2, 79, 16));
    this.add(Psuperior, BorderLayout.NORTH);
    this.add(Sprod, BorderLayout.CENTER);
    Sprod.getViewport().add(Pprod, null);
//    Sprod.getViewport().add(PDesProd, null);

    Psuperior.add(Pcondi, BorderLayout.NORTH);
    Pcondi.add(cLabel2, null);
    Pcondi.add(pdc_fecpedE, null);
    Pcondi.add(agr_codiE, null);
    Pcondi.add(tar_codiE, null);
    Pcondi.add(cLabel7, null);
    Pcondi.add(cLabel3, null);
    Pcondi.add(cLabel1, null);
    Pcondi.add(alm_codiE, null);
    Psuperior.add(SFami, BorderLayout.CENTER);
    SFami.getViewport().add(Pfami, null);
    pdalmace.llenaCombo(alm_codiE, dtCon1,'*');
//    s = "SELECT alm_codi,alm_nomb FROM v_almacen ORDER BY alm_nomb";
//    dtCon1.select(s);
    alm_codiE.addItem("**TODOS**","0");
    alm_codiE.addItem(dtCon1,false);
    alm_codiE.setValor(0);
    s = "SELECT agr_codi,agp_nomb FROM v_agupro "+
              " ORDER BY agp_nomb";
    agr_codiE.addItem("TODOS", "0");
    dtCon1.select(s);
    agr_codiE.addItem(dtCon1,false);
    Pfami.setLayout(new GridBagLayout());
    tar_codiE.setFormato(true);
    pdc_fecpedE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
    pdc_fecpedE.resetCambio();
    tar_codiE.setFormato(Types.DECIMAL, "9", 1);

    s = "SELECT tar_codi,tar_nomb FROM tipotari " +
        " ORDER BY tar_codi ";
    dtStat.select(s);
    tar_codiE.addDatos(dtStat);
    tar_codiE.setValorInt(1);
    setDiasVer_Cliente(diaslim_clientes);
    activarEventos();
  }
  
  public void setVerPrecios(boolean swVerPrecios)
  {
    verPrecios=swVerPrecios;
  }
  public boolean getVerPrecios()
  {
    return verPrecios;
  }
  void activarEventos()
  {
    agr_codiE.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        try
        {
          verFamilias();
        }
        catch (Exception k)
        {
          padre.Error("Error al Visualizar Familias del grupo: " + agr_codiE.getValorInt(), k);
        }
      }
    });

    tar_codiE.addCambioListener(new CambioListener()
    {
      public void cambio(CambioEvent event)
      {
        verProducto0(fam_codi);
      }
    });
    pdc_fecpedE.addFocusListener(new FocusAdapter(){
       public void focusLost(FocusEvent e) {
         if (!pdc_fecpedE.hasCambio())
           return;
         pdc_fecpedE.resetCambio();
         verProducto0(fam_codi);
       }
    });
    alm_codiE.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        verProducto0(fam_codi);
      }
    });
  }
  public void setFechaPedido(String fecped)
  {
    pdc_fecpedE.setText(fecped);
    pdc_fecpedE.resetCambio();
  }
  public String getFechaPedido()
  {
    return pdc_fecpedE.getText();
  }
  public void setPedidos(boolean incPedid)
  {
    this.incPedid=incPedid;
  }

  public boolean getPedidos()
  {
    return this.incPedid;
  }

  public void setCliente(int cliCodi)
  {
    this.cliCodi=cliCodi;
  }
  public int getCliente()
  {
    return this.cliCodi;
  }
  public void setVerProductos(String verProd)
  {
    if (verProd==null || verProd.length()==0)
      return;
    opVerProd=verProd.charAt(0);
  }

  public String getVerProductos()
  {
    return ""+opVerProd;
  }

  protected boolean isEdit()
  {
    return false;
  }

  public void verFamilias() throws SQLException
  {
    // Limpiar componentes del panel de Familias y ActionListeners de los JButton
    limpiaPanel(Pfami, btFami);
    xFam = 0;
    yFam = 0;
    s = "SELECT fpr_codi,fpr_nomb FROM v_famipro  "+
        (agr_codiE.getValorInt() == 0 ? "" : " where agr_codi = " + agr_codiE.getValorInt()) +
        " order by fpr_nomb";
    int famCodiOri=0;
    Bprimero=insFamilia(famCodiOri,"*TODOS*");
    if (opVerProd!=VER_ULTVENTAS)
        Bprimero=null;
    if (dtCon1.select(s))
    {
      do
      {
        if (Bprimero==null)
        {
          Bprimero=insFamilia(dtCon1.getInt("fpr_codi"),dtCon1.getString("fpr_nomb"));
          famCodiOri=dtCon1.getInt("fpr_codi");
        }
        else
          insFamilia(dtCon1.getInt("fpr_codi"),dtCon1.getString("fpr_nomb"));
      }   while (dtCon1.next());
    }
    
    if (Bprimero != null)
    {
      cambioFamilia(famCodiOri, Bprimero);
//      Bprimero.doClick();
    }
  }

  CToggleButton insFamilia(int fprCodi,String fprNomb) throws SQLException
  {
    CToggleButton Bfami = new CToggleButton(fprNomb);
//        if (Bprimero == null)
//          Bprimero = Bfami;
     Bfami.setToolTipText(fprNomb);
     Bfami.setMargin(new Insets(0, 0, 0, 0));
     Bfami.setPreferredSize(new Dimension(180, 16));
     Bfami.setMinimumSize(new Dimension(180, 16));
     Bfami.setMaximumSize(new Dimension(180, 16));
//        Bfami.setBorderPainted(false);
     Bfami.addActionListener(new actionFamilia(this, fprCodi,Bfami));
     btFami.add(Bfami);
     Pfami.add(Bfami, new GridBagConstraints(xFam, yFam, 1, 1, 1.0, 1.0
                                             , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                                             new Insets(2, 0, 0, 2), 0, 0));
     xFam++;
     if (xFam > 3) // 4 Botones por linea
     {
       xFam = 0;
       yFam++;
     }
     return Bfami;
  }
  public void setTarifa(int tarCodi)
  {
    tar_codiE.setValorInt(tarCodi);
  }

  public int getTarifa()
  {
    return tar_codiE.getValorInt();
  }
  private void verProducto0(int famCodi)
  {
    try
    {
      verProductos(fam_codi);
    }
    catch (Exception k)
    {
      padre.Error("Error al Visualizar Productos de Familia: " + fam_codi, k);
    }
  }

  void verProductos(final int famCodi) throws Exception
  {
//    javax.swing.SwingUtilities.invokeLater(new Thread()
//    {
//    new miThread("")
//    {
//      public void run()
//      {
          verProductos0(famCodi,0);
     
//
//      }
//    };

  }
  public void verProducto(int proCodi) throws Exception
  {
    verProductos0(0,proCodi);
  } 
  
  public void verProductos0(int famCodi,int proCodi) throws Exception
  {
    fam_codi= famCodi;
    limpiaPanel(Pprod, null);

    int almCodi=alm_codiE.getValorInt();
    if (proCodi==0)
    {
      s = "SELECT pro_codi,pro_nomb,pro_nomcor,pro_stock,pro_stkuni FROM v_articulo  " +
          " WHERE 1=1" +
          " and pro_tiplot = 'V' "+
          (fam_codi != 0 ? " and  fam_codi = " + famCodi : "") +
          (opVerProd == VER_CONSTOCK ? " and pro_stkuni > 0 " : "") + // Solo muestro los registros con unidades
          (opVerProd == VER_ULTVENTAS ? " and pro_codi in (select distinct(pro_codi) from v_albavel l, v_albavec AS C" +
           " WHERE c.avc_ano = l.avc_ano " +
           " and c.emp_codi = l.emp_codi " +
           " and c.avc_serie = l.avc_serie " +
           " and c.avc_nume = l.avc_nume" +
           " and c.cli_codi = " + cliCodi +
           " and c.avc_fecalb >= TO_DATE('" + feclim_clientes + "','dd-MM-yyyy'))" : "") +
          " order by pro_nomcor";
    }
    else
      s = "SELECT pro_codi,pro_nomb,pro_nomcor,pro_stock,pro_stkuni FROM v_articulo  " +
          " where pro_codi = "+proCodi;
//    System.out.println(s);
    fefise = "";
    if (pdc_fecpedE.getError())
    {
        padre.msgBox("Fecha Pedido NO es valida");
        return;
    }
    GregorianCalendar gc = new GregorianCalendar();
    java.util.Date fec1=Formatear.getDate(pdc_fecpedE.getText(), "dd-MM-yyyy");
    if (fec1==null)
    {
        padre.msgBox("Fecha Pedido NO es valida");
        return;
    }
    gc.setTime(fec1);
    fefise = Formatear.sumaDias(pdc_fecpedE.getText(), "dd-MM-yyyy", 7 - gc.get(GregorianCalendar.DAY_OF_WEEK));

    padre.mensajeRapido("Buscando productos de Familia: "+famCodi);
    double precio;
    vt.clear();
    if (dtCon1.select(s))
    {
      do
      {
        padre.mensajeRapido("Buscando producto: "+dtCon1.getString("pro_nomcor")+ " de Familia: "+famCodi);
        if (almCodi!=0)
        {
          s = "SELECT SUM(stp_kilact) as pro_stock,sum(stp_unact) as pro_stkuni " +
              " FROM actstkpart where pro_codi = " + dtCon1.getString("pro_codi") +             
              " and stp_kilact > 0 " +
              " and alm_codi = " + almCodi;
          dtStat.select(s);
          stock=dtStat.getDouble("pro_stock", true);
          unidad=dtStat.getDouble("pro_stkuni", true);
          if (unidad<=0)
            continue;
        }
        else
        {
          stock = dtCon1.getDouble("pro_stock", true);
          unidad = dtCon1.getDouble("pro_stkuni", true);
        }
        precio=MantTarifa.getPrecTar(dtStat, dtCon1.getInt("pro_codi"),cliCodi,
            tar_codiE.getValorInt(), pdc_fecpedE.getText());
        double ultPrecio=verPrecios? pdalbara.getUltimoPrecio(dtStat,dtCon1.getInt("pro_codi"),cliCodi):0;
        if (incPedid)
          getAcumPedid(dtCon1.getInt("pro_codi"), pdc_fecpedE.getText(),almCodi);
        CButton BProd = new CButton("<html>"+Formatear.format(dtCon1.getString("pro_codi"), "####9")+
                                   " <b>"+Formatear.ajusIzq(dtCon1.getString("pro_nomcor"), 15) +"</b><br>"+
                                     Formatear.format(unidad, "----9") +"U "+
                                     Formatear.format(stock, "--,--9") +"K "+
                                     Formatear.format(precio,"##9.99")+"/"+
                                     Formatear.format(ultPrecio,"##9.99")+"\u20AC"+
                                      "</html>");
        BProd.setFont(new java.awt.Font("Courier New", 0, 11));
        BProd.setToolTipText(dtCon1.getString("pro_nomb"));
        BProd.setMargin(new Insets(0, 0, 0, 0));
        BProd.setPreferredSize(new Dimension(170, 28));
        BProd.setMinimumSize(new Dimension(170, 28));
        BProd.setMaximumSize(new Dimension(170, 28));
//        Bfami.setBorderPainted(false);
        BProd.addMouseListener(new actionProducto(this, dtCon1.getInt("pro_codi"),precio,BProd));
        vt.add(BProd);
//        Pprod.add(BProd, new GridBagConstraints(x, y, 1, 1, 1.0, 1.0
//                                                , GridBagConstraints.CENTER, GridBagConstraints.NONE,
//                                                new Insets(2, 0, 0, 2), 0, 0));
//        BProd.setEnabledParent(false);
//        BProd.setEnabled(false);

      }  while (dtCon1.next());
    }
//    javax.swing.SwingUtilities.invokeLater(new Thread()
//    {
//      @Override
//      public void run()
//      {
        int x = 0;
        int y = 0;
        for (CButton vt1 : vt)
        {
              Pprod.add(vt1, new GridBagConstraints(x, y, 1, 1, 1.0, 1.0
                  , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                  new Insets(2, 0, 0, 2), 0, 0));
              x++;
              if (x > 3) // 3 Botones por linea
              {
                  x = 0;
                  y++;
              }
        }
        if (Sprod.getViewport().getComponent(0)!=Pprod)
        {
          Sprod.getViewport().remove(PDesProd);
          Sprod.getViewport().add(Pprod, null);
        }
        
//        padre.mensajeErr("Productos encontrados ",false);
//        padre.setEnabled(true);

  }
  /**
   * Calcula el Acumulado de Pedidos pendientes tanto de compras como
   * de ventas NO cerrados.
   * Tambien busca albaranes de compra y ventas sin cerrar
   *
   * @param proCodi int Producto
   * @param fecped String fecha pedido
   * @param almCodi Almacen - 0 es TODOS
   * @throws Exception Error de DB
   */
  void getAcumPedid(int proCodi,String fecped,int almCodi) throws Exception
  {
    // Sumo pedidos de compra que no esten cerrados.
    s = "SELECT sum(pcl_nucape) as pcl_nucape, sum(pcl_cantpe) as pcl_cantpe, " +
        " sum(pcl_nucaco) as pcl_nucaco, sum(pcl_cantco) as pcl_cantco, " +
        " sum(pcl_nucafa) as pcl_nucafa, sum(pcl_cantfa) as pcl_cantfa, " +
        " pcc_estad,prv_codi,pcl_feccad " +
        " FROM v_pedico " +
        " where EMP_CODI = " + emp_codi +
        " and pro_codi = "+proCodi+
        " AND pcc_estrec = 'P' "+
        (almCodi == 0?"":" and alm_codi = "+almCodi)+
        " and pcc_fecrec <=  TO_DATE('" + fecped + "','dd-MM-yyyy')" +       
        " group by pcc_estad,prv_codi,pcl_feccad";
      char estad;
      if (dtStat.select(s))
      {
        do
        {
          if (dtStat.getString("pcc_estad").length()<1)
            continue;
          estad=dtStat.getString("pcc_estad").charAt(0);
          switch (estad)
          {
            case 'P':
              stock += dtStat.getDouble("pcl_cantpe",true);
              unidad += dtStat.getDouble("pcl_nucape",true);
              break;
            case 'C':
              stock += dtStat.getDouble("pcl_cantco",true);
              unidad += dtStat.getDouble("pcl_nucaco",true);
              break;
            case 'F':
              stock += dtStat.getDouble("pcl_cantfa",true);
              unidad += dtStat.getDouble("pcl_nucafa",true);
          }
        } while (dtStat.next());
      }
      // Descuento Albaranes de Compra que NO esten cerrados
      s="select sum(acl_canti) as cantidad,sum(acl_numcaj) as unidades "+
          "  from v_albacoc as c, v_albacol as l "+
          " WHERE c.emp_codi = l.emp_codi "+
          " and c.acc_ano = l.acc_ano "+
          " and c.acc_serie = l.acc_serie "+
          " and c.acc_nume = l.acc_nume "+
          " and l.pro_codi = "+proCodi+
          " and c.emp_codi = "+emp_codi+
           (almCodi == 0?"":" and l.alm_codi = "+almCodi)+
           " AND C.ACC_CERRA = 0"+
           " AND l.ACC_CERRA = 0"+ // Abierto y con Pedido
           " and  exists ( select emp_codi from pedicoc as p " +
           " where p.emp_codi = c.emp_codi " +
           " and p.acc_ano = c.acc_ano " +
           " and p.acc_serie = c.acc_serie " +
           " and p.acc_nume = c.acc_nume) ";

      dtStat.select(s);
      stock-=dtStat.getDouble("cantidad",true);
      unidad -= dtStat.getDouble("unidades",true); // Resto la cantidad


      // Resto de Stock los pedidos de Venta SIN Albaran
      s = "SELECT sum(pvl_unid) as  pvl_unid, sum(pvl_kilos) as pvl_kilos" +
          "  FROM v_pedven " +
          " where EMP_CODI = " + emp_codi +
          " and pvc_confir = 'S' "+
          " and (avc_ano = 0 or pvc_cerra = 0) "+ // Pedidos NO cerrados
          (almCodi == 0?"":" and alm_codi = "+almCodi)+
          " and pro_codi = " + proCodi +
          " AND pvc_fecent <= TO_DATE('" + fefise + "','dd-MM-yyyy')";
      dtStat.select(s);
      unidad -= dtStat.getDouble("pvl_unid",true);
      stock -=dtStat.getDouble("pvl_kilos",true); 
      // Sumo a Stock los Albaranes de Venta SIN cerrar Y CON pedido
      s = "select sum(avl_canti) as cantidad,sum(avl_unid) as unidades " +
          "  from v_albavec as c, v_albavel as l " +
          " WHERE c.emp_codi = l.emp_codi " +
          " and c.avc_ano = l.avc_ano " +
          " and c.avc_serie = l.avc_serie " +
          " and c.avc_nume = l.avc_nume " +
          " and l.pro_codi = " + proCodi +
          " and c.emp_codi = " + emp_codi +
          (almCodi == 0 ? "" : " and c.avc_almori = " + almCodi) +
          " AND C.AvC_CERRA = 0" + // Abierto
          " AND l.AvC_CERRA = 0" + // Abierto
          " and  exists ( select emp_codi from pedvenc as p " +
          " where p.emp_codi = c.emp_codi " +
          " and p.avc_ano = c.avc_ano " +
          " and p.avc_serie = c.avc_serie " +
          " and p.avc_nume = c.avc_nume) ";

     dtStat.select(s);
     stock += dtStat.getDouble("cantidad", true);
     unidad += dtStat.getDouble("unidades", true); // Resto la cantidad
  }
  public int getAlmacen()
  {
    return alm_codiE.getValorInt();
  }
  void limpiaPanel(CPanel p, ButtonGroup bt)
  {
    Component c[] = p.getComponents();
    for (Component c1 : c)
    {
        p.remove(c1);
        if (bt != null && c1 instanceof AbstractButton)
            bt.remove((AbstractButton) c1);
        if (c1 instanceof AbstractButton)
        {
            ActionListener[] al = ((AbstractButton) c1).getActionListeners();
            for (ActionListener al1 : al)
            {
                ((AbstractButton) c1).removeActionListener(al1);
            }
        }
    }
  }

  void cambioFamilia(int famCodi,AbstractButton bt)
  {
    Bprimero=bt;
//    System.out.println("Pulsado boton familia: " + famCodi);
    try
    {
      verProductos(famCodi);
    }
    catch (Exception k)
    {
      padre.Error("Error al ver Productos ", k);
    }
  }

//  void cambioProducto(int proCodi)
//  {
//    System.out.println("Pulsado boton Producto: " + proCodi);
//
//  }

  void verDesglProd(final CButton bt, final int proCodi, final double precio)
  {
//    new miThread("")
//    {
//            @Override
//      public void run()
//      {
//        verDesglProd0(bt, proCodi, precio);
//      }
//    };
      verDesglProd0(bt, proCodi, precio);
  }
  /**
   * Busca Desglose de un producto.
   * @param bt
   * @param proCodi
   * @param precio 
   */
  void verDesglProd0 (final CButton bt,int proCodi,double precio)
  {
//    Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
//    padre.setEnabled(false);
//    padre.mensajeRapido("Buscando Desglose producto: "+proCodi);
//    int x = 0, y = 1;
    int almCodi=alm_codiE.getValorInt();
    String fecped=pdc_fecpedE.getText();
    try
    {
      s = "SELECT 1 as tipsel,sum(stp_unact) as unidades,SUM(stp_kilact) as cantidad,prv_codi, "+
          " stp_feccad as feccad " +
          " FROM v_stkpart where pro_codi = " + proCodi +
          " and emp_codi = " + emp_codi +
          " and stp_kilact != 0 " +
          " and eje_nume > 0 " +
          (almCodi == 0 ? "" : " and alm_codi = " + almCodi) +
          " group by prv_codi,stp_feccad" ;
     if (incPedid)
       s+=" UNION ALL " +
 // Pedidos Compras Pendientes de confirmar
          " SELECT 2 as tipsel,sum(pcl_nucape) as unidades, sum(pcl_cantpe) as cantidad, " +
          " prv_codi,pcl_feccad as feccad " +
          " FROM v_pedico " +
          " where EMP_CODI = " + emp_codi +
          " and pro_codi = " + proCodi +
          (almCodi == 0 ? "" : " and alm_codi = " + almCodi) +
          " and pcc_estad = 'P' " +
          " AND pcc_estrec = 'P' "+ // Pendientes
          " and pcc_fecrec <=  TO_DATE('" + fecped + "','dd-MM-yyyy')" +
          " group by prv_codi,pcl_feccad " +
          " UNION ALL " +
// Pedidos Compras Confirmados
         " SELECT 3 as tipsel, sum(pcl_nucaco) as unidades, sum(pcl_cantco) as cantidad, " +
         " prv_codi,pcl_feccad as feccad " +
         " FROM v_pedico " +
         " where EMP_CODI = " + emp_codi +
         " and pro_codi = " + proCodi +
         (almCodi == 0 ? "" : " and alm_codi = " + almCodi) +
         " AND pcc_estrec = 'P' "+ // Pendientes
         " and pcc_estad = 'C' " + // Confirmados.
         " and pcc_fecrec <=  TO_DATE('" + fecped + "','dd-MM-yyyy')" +
         " group by prv_codi,pcl_feccad " +
         " UNION ALL " +
 // Pedidos Compras Pre-Factura
         " SELECT 4 as tipsel,  sum(pcl_nucafa) as unidades, sum(pcl_cantfa) as cantidad, " +
         " prv_codi,pcl_feccad as feccad " +
         " FROM v_pedico  " +
         " where EMP_CODI = " + emp_codi +
         " and pro_codi = " + proCodi +
         (almCodi == 0 ? "" : " and alm_codi = " + almCodi) +
         " and pcc_estrec = 'P' "+ // Ignorar pedidos cancelados
         " and pcc_estad = 'F' " +
         " and pcc_fecrec <=  TO_DATE('" + fecped + "','dd-MM-yyyy')" +
         " group by prv_codi,pcl_feccad " +
         " UNION ALL "+
// Pedidos Ventas Pendientes
         "SELECT 5 as tipsel, sum(pvl_unid)*-1 as  unidades, sum(pvl_kilos)*-1 as cantidad, " +
         " prv_codi,pvl_feccad as feccad " +
         "  FROM v_pedven as c" +
         " where C.EMP_CODI = " + emp_codi +
         " and (avc_ano = 0 or pvc_cerra = 0) "+ // Sin Albaran o Albaran sin CERRAR
         " and pvc_confir = 'S' "+
         (almCodi == 0 ? "" : " and c.alm_codi = " + almCodi) +
         " and c.pro_codi = " + proCodi +
         " AND pvc_fecent <= TO_DATE('" + fecped + "','dd-MM-yyyy')" +
         " group by c.prv_codi,pvl_feccad "+
         " UNION ALL " +
 // Albaranes Compra SIN Cerrar Y CON PEDIDOS
        " select 6 as tipsel, sum(acp_canind)*-1 as unidades,sum(acp_canti)*-1 as cantidad,  "+
        " c.prv_codi , c.acp_feccad as feccad "+
        " from v_compras as c "+
        " WHERE c.emp_codi = "+emp_codi+ 
        (almCodi == 0 ? "" : " and l.alm_codi = " + almCodi) +
        " and c.pro_codi = "+proCodi+
        " AND c.ACC_CERRA = 0 "+        
        " and exists ( select   emp_codi "+
        " from pedicoc as p " +
        " where p.emp_codi = c.emp_codi "+
        " and p.acc_ano = c.acc_ano "+
        " and p.acc_serie = c.acc_serie "+
        " and p.acc_nume = c.acc_nume) "+
        " group by prv_codi,acp_feccad "+
         " UNION ALL " +
// Albaranes Ventas sin CERRAR Y con pedidos
         " select 7 as tipsel, sum(avp_numuni) as unidades,sum(avp_canti) as cantidad, " +
         " s.prv_codi, s.stp_feccad as feccad " +
         " from v_albventa_detalle as c,v_stkpart as s " +
         " WHERE c.avc_cerra = 0 " + // Abierto y con pedido
         (almCodi == 0 ? "" : " and s.alm_codi = " + almCodi) +
         " and c.pro_codi = " + proCodi +
         " and c.emp_codi = " + emp_codi +
         " and  exists ( select emp_codi from pedvenc as p " +
         " where p.emp_codi = c.emp_codi " +
         " and p.avc_ano = c.avc_ano " +
         " and p.avc_serie = c.avc_serie " +
         " and p.avc_nume = c.avc_nume) " +
         " group by s.prv_codi, s.stp_feccad ";
      s+=" order by 4 asc,3 ";
//      padre.debug("verDesglProd: "+s);

      if (dtCon1.select(s))
      { 
        int prvCodi=dtCon1.getInt("prv_codi",true);
        String feccad=dtCon1.getFecha("feccad","dd-MM-yy");

        limpiaPanel(PDesProd, null);
        
        vt.clear();
        int unid=0;
        double canti=0;
        do
        {         
          if (prvCodi!=dtCon1.getInt("prv_codi",true) || !feccad.equals(dtCon1.getFecha("feccad","dd-MM-yy")))
          {
//            if (unid>=1 && canti>=1) 
//            {
                CButton BProd = getBotonDesgl(proCodi,precio, prvCodi, feccad, unid, canti);
                vt.add(BProd);
//            }
            unid = 0;
            canti = 0; 
            prvCodi = dtCon1.getInt("prv_codi",true);
            feccad = dtCon1.getFecha("feccad", "dd-MM-yy");
          }
          canti+=dtCon1.getDouble("cantidad",true);
          unid+=dtCon1.getInt("unidades",true);
        }  while (dtCon1.next());
        CButton BProd = getBotonDesgl(proCodi,precio, prvCodi, feccad, unid, canti);
        vt.add(BProd);
//        javax.swing.SwingUtilities.invokeLater(new Thread()
//        {
//          public void run()
//          {
            CLabel prodL = new CLabel(bt.getText(), CLabel.CENTER);
            prodL.setOpaque(true);
            prodL.setBackground(Color.blue);
            prodL.setForeground(Color.white);
            prodL.setPreferredSize(new Dimension(370, 26));
            prodL.setMinimumSize(new Dimension(370, 26));
            prodL.setMaximumSize(new Dimension(370, 26));

            PDesProd.add(prodL, new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0
                , GridBagConstraints.CENTER,
                GridBagConstraints.NONE,
                new Insets(2, 0, 0, 2), 0, 0));
            btVolver.setPreferredSize(new Dimension(370, 20));
            btVolver.setMinimumSize(new Dimension(370, 20));
            btVolver.setMaximumSize(new Dimension(370, 20));

            PDesProd.add(btVolver, new GridBagConstraints(2, 0, 2, 1, 1.0, 1.0
                , GridBagConstraints.CENTER,
                GridBagConstraints.NONE,
                new Insets(2, 0, 0, 2), 0, 0));

            btVolver.addActionListener(new ActionListener()
            {
              public void actionPerformed(ActionEvent e)
              {
                Sprod.getViewport().add(Pprod, null);
                Sprod.getViewport().remove(PDesProd);
                Pprod.setEnabled(true);
//                if (Bprimero != null)
//                  Bprimero.doClick();
              }
            });

            int x = 0;
            int y = 1;
              for (CButton vt1 : vt)
              {
                  PDesProd.add(vt1, new GridBagConstraints(x, y, 1, 1, 1.0, 1.0
                      , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                      new Insets(2, 0, 0, 2), 0, 0));
                  x++;
                  if (x > 3) // 4 Botones por linea
                  {
                      x = 0;
                      y++;
                  }
              }
            Sprod.getViewport().remove(Pprod);
            Sprod.getViewport().add(PDesProd, null);

//            Pprod.setVisible(false);
//            PDesProd.setVisible(true);
           
//            padre.mensajeErr("Productos Desglosado ",false);
//            padre.setEnabled(true);
//          }
//        });
      }
      else
      {
        padre.msgBox("No encontrado desglose de Producto: " + proCodi);

//        padre.setEnabled(true);
      }
    }
    catch (Exception k)
    {
      padre.Error("Error al ver desglose de productos: " + proCodi, k);
    }
  }
  CButton getBotonDesgl(int proCodi,double precio,int prvCodi,String feccad,int unid,double canti) throws Exception
  {
    String prvNomb;
    s = "SELECT * FROM v_proveedo where prv_codi = " + prvCodi;
    if (dtStat.select(s))
      prvNomb = Formatear.ajusIzq(dtStat.getString("prv_nomb"), 18);
    else
      prvNomb = "<font color=\"#cc0000\"> PRV: " + prvCodi +
          " NO ENCONTRADO</font>";

    CButton BProd = new CButton("<html><b> " + prvNomb + "</b><center>" +
                                " <em>" +feccad  + "</em>" +
                                Formatear.format(unid, "----9") +" U " +
                                Formatear.format(canti, "--,--9.9")+" K</center></html>");

    BProd.setFont(new java.awt.Font("Courier New", 0, 11));
    BProd.setToolTipText(prvNomb + " Fec.Cad: " + feccad);
    BProd.setMargin(new Insets(0, 0, 0, 0));
    BProd.setPreferredSize(new Dimension(190, 30));
    BProd.setMinimumSize(new Dimension(190, 30));
    BProd.setMaximumSize(new Dimension(190, 30));
    BProd.addActionListener(new actionDesglProd(this, proCodi, precio, prvCodi,
                                                feccad));
    return BProd;
  }

  /**
   * Funcion a machacar cuando se quiera insertar la linea.
   *
   * @param proCodi int Producto
   * @param precio precio dee Producto
   * @param prvCodi int Proveedor
   * @param feccad String Fecha Caducidad
   */
  protected void insertarDesgProd(int proCodi,double precio,int prvCodi,String feccad)
  {


  }
  public void setDiasVer_Cliente(int diasVer) throws java.text.ParseException
  {
    diaslim_clientes=diasVer;
    feclim_clientes=Formatear.sumaDias(Formatear.getDateAct(),diaslim_clientes*-1);
  }
  public int getDiasVer_Cliente()
{
  return diaslim_clientes;
}

  public void setEmpresa(int empCodi)
  {
    emp_codi=empCodi;
  }
  public int getEmpresa()
  {
    return emp_codi;
  }
}

class actionFamilia implements ActionListener
{
  int famCodi;
  pstockAct padre;
  AbstractButton boton;
  public actionFamilia(pstockAct papa,int famCodi,AbstractButton bt)
  {
    boton=bt;
    this.famCodi=famCodi;
    padre=papa;
  }
  public void actionPerformed(ActionEvent e)
  {
    padre.cambioFamilia(famCodi,boton);
  }
}
class actionProducto extends MouseAdapter
{
  int proCodi;
  double precio;
  pstockAct padre;
  CButton boton;

  public actionProducto(pstockAct papa,int proCodi,double precio,CButton bt)
  {
    this.proCodi=proCodi;
    this.precio=precio;
    boton=bt;
    padre=papa;
  }

    @Override
  public void mouseClicked(MouseEvent e)
  {
    if ((e.getModifiers() & MouseEvent.CTRL_MASK) != 0 || e.getButton() != MouseEvent.BUTTON1) //  && padre.isEdit())
      padre.verDesglProd(boton, proCodi, precio);
    else
    {
      if (padre.isEdit())
        padre.insertarDesgProd(proCodi, precio, 0, "");
      else
        padre.verDesglProd(boton, proCodi, precio);
    }
  }

/*  public void actionPerformed(ActionEvent e)
  {
    if ( (e.getModifiers() & e.CTRL_MASK) != 0 || ! padre.isEdit())
      padre.verDesglProd(boton,proCodi,precio);
    else
      padre.insertarDesgProd(proCodi, precio,0,"");

  }*/
}
class actionDesglProd implements ActionListener
{
  String  feccad;
  int prvCodi,proCodi;
  double precio;
  pstockAct padre;

  public actionDesglProd(pstockAct papa,int proCodi,double precio,int prvCodi,String feccad)
  {
    this.feccad=feccad;
    this.prvCodi=prvCodi;
    this.proCodi=proCodi;
    this.precio=precio;
    padre=papa;
  }
  @Override
  public void actionPerformed(ActionEvent e)
  {
    if (padre.isEdit())
      padre.insertarDesgProd(proCodi,precio,prvCodi,feccad);
  }
}


