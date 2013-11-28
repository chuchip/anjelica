package gnu.chu.anjelica.almacen;

/**
 *
 * <p>Titulo: lisaldos </p>
 * <p>Descripción: Listado de Saldos </p>
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
 * <p>Empresa: miSL</p>
 * @author chuchi P
 * @version 1.0 - 20041227
 *
 *  @version 1.1 (20050807) Now only presents products that are 'Vendibles'
 * (look at field 'pro_tiplot' in table 'v_articulo'. But now presents articles that the cost is 0
*
 *
 */
import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.menu;
import gnu.chu.anjelica.pad.MantFamPro;
import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.camposdb.proPanel;
import gnu.chu.controles.*;
import gnu.chu.interfaces.VirtualGrid;
import gnu.chu.print.util;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.ventana;
import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import net.sf.jasperreports.engine.*;


public class lisaldos   extends ventana  implements JRDataSource
{
  private double kgVen,kgCom,kgReg=0;
  private CCheckBox pro_cosincE=new CCheckBox("Inc.Costo");
  MvtosAlma mvtosAlm = new MvtosAlma();
  boolean valDesp;
  boolean cancelado=false;
  char sel='d';
  PreparedStatement ps;
  ResultSet rs;
  ifMvtosClase ifMvtos = new ifMvtosClase();
  
  boolean imprList=false;
  int nLin;
  CButton Bimpr = new CButton(Iconos.getImageIcon("print"));
  String feulin;
  String s;
  private double kilos=0;
  int unid = 0;
  double precio=0;
  CPanel Pprinc = new CPanel();
  CPanel Pdatcon = new CPanel();
    CLinkBox cam_codiE = new CLinkBox();
  CComboBox pro_artconE = new CComboBox();
  CLabel cLabel9 = new CLabel();
  CButton Baceptar = new CButton("Aceptar",Iconos.getImageIcon("check"));
  String camCodi;
  ResultSet dtProd;
  CLabel cLabel2 = new CLabel();
  CLinkBox alm_inicE = new CLinkBox();
    //  int almOri,almFin;
  CLabel cLabel4 = new CLabel();
  CComboBox feulinE = new CComboBox();
  Cgrid jt = new Cgrid(7);
  CLabel cLabel5 = new CLabel();
  proPanel pro_codiE = new proPanel();
  
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  Cgrid jtMv = new Cgrid(4);
  proPanel pro_codmvE = new proPanel();
  CTextField fecsalE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel cLabel6 = new CLabel();
    CCheckBox opValDesp = new CCheckBox();
    private CPanel Ppie = new CPanel();
    private CLabel kilosL = new CLabel();
    private CTextField kilosE = new CTextField(Types.DECIMAL,"--,---,--9.99");
    private CTextField unidE = new CTextField(Types.DECIMAL,"----,--9");
    private CLabel unidL = new CLabel();
    private CLabel importeL = new CLabel();
    private CTextField importeE = new CTextField(Types.DECIMAL,"----,---,--9.99");
    private CLabel pro_congeL = new CLabel();
    private CLabel ordenL = new CLabel();
    private CComboBox ordenE = new CComboBox();


    public lisaldos(EntornoUsuario eu, Principal p)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    setTitulo("Listado de Saldos Almacen");

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
      setErrorInit(true);
    }
  }

  public lisaldos(menu p, EntornoUsuario eu)
  {

    EU = eu;
    vl = p.getLayeredPane();
    setTitulo("Listado de Saldos Almacen");
    eje = false;

    try
    {
      jbInit();
    }
    catch (Exception e)
    {
      ErrorInit(e);
      setErrorInit(true);
    }
  }

  private void jbInit() throws Exception
  {
    iniciarFrame();
    this.setSize(new Dimension(592, 516));
    this.setVersion("2012-03-15");
    ifMvtos.setSize(new Dimension(475, 325));
    
    ifMvtos.setVisible(false);
    ifMvtos.setClosable(true);
    ifMvtos.setPadre(this);
    fecsalE.setBounds(new Rectangle(55, 22, 79, 17));
    cLabel6.setBounds(new Rectangle(5, 22, 50, 17));
    cLabel6.setText("En Fecha");
    opValDesp.setFocusable(false);
    opValDesp.setBounds(new Rectangle(160, 22, 135, 17));
    opValDesp.setText("Valorar Desp.");
    opValDesp.setMargin(new Insets(0, 0, 0, 0));
    opValDesp.setToolTipText("Considerar despieces de Salida como entradas en negativo");
    cLabel9.setText("Camara");
    cLabel9.setBounds(new Rectangle(284, 22, 47, 18));
    cam_codiE.setBounds(new Rectangle(332, 22, 191, 18));
    Pdatcon.setMaximumSize(new Dimension(540, 91));

    vl.add(ifMvtos,new Integer(1));
    Pprinc.setLayout(gridBagLayout1);

    statusBar= new StatusBar(this);
    Bimpr.setPreferredSize(new Dimension(24,24));
    Bimpr.setMinimumSize(new Dimension(24,24));
    Bimpr.setMaximumSize(new Dimension(24,24));

   
    pro_codmvE.setEnabled(false);
    statusBar.add(Bimpr, new GridBagConstraints(9, 0, 1, 2, 0.0, 0.0
                                           , GridBagConstraints.EAST,
                                           GridBagConstraints.VERTICAL,
                                           new Insets(0, 5, 0, 0), 0, 0));

    conecta();
    cLabel2.setText("Almacen");
    cLabel2.setBounds(new Rectangle(5, 45, 60, 17));
    alm_inicE.setAncTexto(30);
    alm_inicE.setBounds(new Rectangle(60, 45, 186, 17));
    Baceptar.setMargin(new Insets(0, 0, 0, 0));
    Baceptar.setText("Aceptar F4");
    cLabel4.setText("Ult. Inventario");
    cLabel4.setBounds(new Rectangle(3, 64, 78, 18));
    feulinE.setBounds(new Rectangle(80, 65, 105, 17));

    confGrid(new ArrayList());

    ArrayList v1=new ArrayList();
    v1.add("Tipo");
    v1.add("Cantidad");
    v1.add("Unidades");
    v1.add("Precio");
    jtMv.setCabecera(v1);
    jtMv.setAjustarGrid(true);
    jtMv.setAnchoColumna(new int[]{50,80,70,70});
    jtMv.setAlinearColumna(new int[]{1,2,2,2});
    jtMv.setFormatoColumna(1,"---,--9.99");
    jtMv.setFormatoColumna(2,"---,--9");
    jtMv.setFormatoColumna(3,"---,--9.99");

 
    cLabel5.setText("Producto");
    cLabel5.setBounds(new Rectangle(4, 3, 59, 17));
    pro_codiE.setBounds(new Rectangle(63, 2, 452, 17));
    Pprinc.setInputVerifier(null);
    Pdatcon.setMinimumSize(new Dimension(540, 91));
    Pdatcon.setPreferredSize(new Dimension(540, 91));
    Ppie.setMinimumSize(new Dimension(400,25));
    Ppie.setPreferredSize(new Dimension(400,25));
    Ppie.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
    Ppie.setLayout(null);
    kilosL.setText("Kilos");
    kilosL.setBounds(new Rectangle(5, 3, 35, 17));
    kilosE.setBounds(new Rectangle(40, 3, 75, 17));
    kilosE.setEditable(false);
    unidE.setBounds(new Rectangle(190, 3, 50, 17));
    unidE.setEditable(false);
    unidL.setBounds(new Rectangle(135, 3, 60, 17));
    unidL.setText("Unidades");
    importeL.setText("Importe");
    importeL.setBounds(new Rectangle(260, 3, 55, 17));
    importeE.setBounds(new Rectangle(310, 3, 85, 17));
    importeE.setEditable(false);
    pro_congeL.setText("Incluir");
    pro_congeL.setBounds(new Rectangle(280, 45, 50, 18));
    ordenL.setText("Orden");
    ordenE.addItem("Producto", "P");
    ordenE.addItem("Familia", "F");

    ordenL.setBounds(new Rectangle(200, 65, 50, 18));
    ordenE.setBounds(new Rectangle(245, 65, 135, 18));
    this.getContentPane().add(statusBar,BorderLayout.SOUTH);

    Pdatcon.setBorder(BorderFactory.createRaisedBevelBorder());
    Pdatcon.setLayout(null);
    
    cam_codiE.setAncTexto(25);
    pro_artconE.setBounds(new Rectangle(332, 45,100, 18));
    pro_artconE.addItem("TODOS","0");
    pro_artconE.addItem("Congelado","1");
    pro_artconE.addItem("NO Congel.","2");
    pro_cosincE.setToolTipText("Incluir Costo Añadido");
    pro_cosincE.setBounds(new Rectangle(435, 45, 90, 18));
    Baceptar.setBounds(new Rectangle(410, 65, 115, 24));
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
    Pdatcon.add(ordenE, null);
    Pdatcon.add(ordenL, null);
    Pdatcon.add(pro_congeL, null);
    Pdatcon.add(cLabel2, null);
    Pdatcon.add(cLabel5, null);
    Pdatcon.add(pro_codiE, null);
    Pdatcon.add(fecsalE, null);
    Pdatcon.add(cLabel6, null);
    Pprinc.add(jt, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 1, 0, 0), 0, -40));
    Pprinc.add(Pdatcon,     new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    Ppie.add(importeE, null);
    Ppie.add(importeL, null);
    Ppie.add(unidL, null);
    Ppie.add(unidE, null);
    Ppie.add(kilosE, null);
    Ppie.add(kilosL, null);
    Pprinc.add(Ppie,
               new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                                       GridBagConstraints.NONE,
                                      new Insets(0, 0, 0, 0), 0, 0));
    
    StatusBar stBar = new StatusBar(ifMvtos);
    ifMvtos.getContentPane().add(jtMv, BorderLayout.CENTER);
    ifMvtos.getContentPane().add(pro_codmvE, BorderLayout.NORTH);
    ifMvtos.getContentPane().add(stBar,BorderLayout.SOUTH);
    Pdatcon.add(cLabel4, null);
    Pdatcon.add(feulinE, null);
    Pdatcon.add(cLabel9, null);
    Pdatcon.add(cam_codiE, null);
    Pdatcon.add(pro_artconE, null);
    Pdatcon.add(pro_cosincE,null );
    Pdatcon.add(alm_inicE, null);
    Pdatcon.add(opValDesp, null);
    Pdatcon.add(Baceptar, null);   
  }

  private void confGrid(ArrayList v)
  {
    v.add("Prod"); // 0
    v.add("Nombre"); // 1
    v.add("Unid"); // 2
    v.add("Kilos"); // 3
    v.add("Precio"); // 4
    v.add("Importe"); // 5
    v.add("Fam"); // 6
    jt.setCabecera(v);
    jt.setAnchoColumna(new int[]{50,150,40,60,60,70,30});
    jt.setAlinearColumna(new int[]{2,0,2,2,2,2,2});
    jt.setMaximumSize(new Dimension(603, 223));
    jt.setMinimumSize(new Dimension(603, 223));
    jt.setFormatoColumna(2,"---9");
    jt.setFormatoColumna(3,"---,--9.99");
    jt.setFormatoColumna(4,"---9.99");
    jt.setFormatoColumna(5,"---,--9.99");
    jt.setAjustarGrid(true);
    cglisaldos vg=new cglisaldos();
    for (int n=0;n<jt.getColumnCount();n++)
    {
        miCellRender mc= jt.getRenderer(n);
        if (mc==null)
            continue;
        mc.setVirtualGrid(vg);
        mc.setErrBackColor(Color.CYAN);
        mc.setErrForeColor(Color.BLACK);
    }
  }

    @Override
  public void iniciarVentana() throws Exception
  {
    Pdatcon.setDefButton(Baceptar);
    cam_codiE.setFormato(Types.CHAR, "XX", 2);
    cam_codiE.texto.setMayusc(true);
    pro_codiE.iniciar(dtStat,this,vl,EU);
    pro_codmvE.iniciar(dtStat,this,vl,EU);
//    activar(true);

    s="select distinct(rgs_fecha) as cci_feccon from v_regstock as r,v_motregu  as m "+
         " where r.emp_codi = "+EU.em_cod+
         " and r.tir_codi = m.tir_codi "+
         " and M.tir_afestk='=' "+
         " order by cci_feccon desc ";

     if (dtStat.select(s))
     {
       feulin = dtStat.getFecha("cci_feccon", "dd-MM-yyyy");
       do
       {
         feulinE.addItem(dtStat.getFecha("cci_feccon","dd-MM-yyyy"),dtStat.getFecha("cci_feccon","dd-MM-yyyy"));
       } while (dtStat.next());
     }
     else
     {
       feulin = "01-01-" + EU.ejercicio; // Buscamos desde el principio del a�o.
       feulinE.addItem(feulin);
     }
     feulinE.setText(feulin);
     pdconfig.llenaDiscr(dtStat,cam_codiE,"AC",EU.em_cod);

    alm_inicE.setFormato(true);
    alm_inicE.setFormato(Types.DECIMAL,"#9",2);
    
    
    
    s="SELECT alm_codi,alm_nomb FROM V_ALMACen ORDER BY alm_codi ";
    dtStat.select(s);
    alm_inicE.addDatos(dtStat);
  
    activarEventos();
    fecsalE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
    fecsalE.requestFocus();
  }
  void activarEventos()
  {
    Baceptar.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
       Baceptar_actionPerformed(threadlisaldos.CONSULTA);
      }
    });
    Bimpr.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Baceptar_actionPerformed(threadlisaldos.LISTA);
      }
    });
    popEspere_BCancelaraddActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        msgEspere("Espere, por favor ... Cancelando Consulta");
        popEspere_BCancelarSetEnabled(false);
        cancelado=true;
      }
    });
    fecsalE.addFocusListener(new FocusAdapter()
    {
            @Override
     public void focusLost(FocusEvent e) {
         try {
          if (fecsalE.isNull() || fecsalE.getError())
              return;
         int nLin=feulinE.getItemCount()-1;
         for (int n=nLin;n>=0;n--)
         {
            if (((String) feulinE.getItemAt(n)).equals(fecsalE.getText()))
            {
                feulinE.setValor((String) feulinE.getItemAt(n));
                return;
            }
            if (Formatear.restaDias((String) feulinE.getItemAt(n), fecsalE.getText())>0)
            {
                feulinE.setValor((String) feulinE.getItemAt(n+1));
                return;
            }
         }
         feulinE.setValor((String) feulinE.getItemAt(0));
         } catch(Exception k )
         {
             Error("Error al buscar fecha",k);
         }
     }
    });
//    Bcancelar.addActionListener(new ActionListener()
//    {
//      public void actionPerformed(ActionEvent e)
//      {
//        mensaje("Espere, por favor ... Cancelando Consulta");
//        Bcancelar.setEnabled(false);
//      }
//    });
    jt.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
          if (jt.isVacio()  || e.getClickCount()<2)
            return;
          verMvtos();
         }
       });
  }
  /**
   * LLamada cuando se hace doble click en una linea.
   * Muestra un detalle de los movimientos realizados.
   */
  void verMvtos()
  {
    ifMvtos.setVisible(true);
    int proCodi = jt.getValorInt(0);
    String  fecinv=fecsalE.getText();
    pro_codmvE.setValorInt(jt.getValorInt(0));
    jtMv.removeAllDatos();
    ArrayList v=new ArrayList();
    try {
      String feulst, tipMov;

      getStrSql(proCodi, feulin, fecinv);
//   debug("verMvtos: "+s);
   rs= ps.executeQuery();
   if (! rs.next())
     return;
   dtCon1.setResultSet(rs);

   feulst = "";
   unid=0;
   do
   {
     v.clear();
     tipMov = dtCon1.getString("tipmov");
     if (tipMov.equals("="))
     {
       if (!feulst.equals(dtCon1.getFecha("fecmov")))
       {
         feulst = dtCon1.getFecha("fecmov");
         v.add("=");
         v.add(dtCon1.getString("canti"));
         v.add(dtCon1.getString("unid"));
         v.add(dtCon1.getString("precio"));
         jtMv.addLinea(v);
       }
       else
         tipMov = "+";
     }

     if (tipMov.equals("+"))
     { // Entrada.
       v.add(dtCon1.getString("sel"));
       v.add(dtCon1.getString("canti"));
       v.add(dtCon1.getString("unid"));
       v.add(dtCon1.getString("precio"));
       jtMv.addLinea(v);
     }


     if (tipMov.equals("-"))
     {
       v.add("-");
       v.add(dtCon1.getString("canti"));
       v.add(dtCon1.getString("unid"));
       v.add(dtCon1.getString("precio"));
       jtMv.addLinea(v);
     }
   } while (dtCon1.next());

 }
 catch (Exception k)
 {
   Error("Error al ver Mvtos desglosados", k);
 }

  }
  void Baceptar_actionPerformed(int opcion)
  {
    ps=null;
    ifMvtos.setVisible(false);
    if (fecsalE.isNull())
    {
      mensajeErr("Introduzca Fecha de Saldo");
      return;
    }
    camCodi=cam_codiE.getText().trim();
    if (camCodi.equals(""))
      camCodi=null;
    if (camCodi!=null)
    {
      camCodi = camCodi.replace('*', '%');
      if (camCodi.equals("*") || camCodi.equals("**"))
        camCodi = null;
    }

//    almOri=Integer.parseInt(alm_inicE.getText().trim());
//    almFin=Integer.parseInt(alm_finalE.getText().trim());
    
    feulin = feulinE.getValor();
   
    threadlisaldos th =new threadlisaldos(this,opcion);
    th.start();
  }

  boolean consultar()
  {
    msgEspere("Calculando Saldos...");
    popEspere_BCancelarSetEnabled(true);
    valDesp=opValDesp.isSelected();
    cancelado=false;
    mensaje("A esperar que estoy  buscando datitos ...");
//    activar(false);
    try
    {
      mvtosAlm.setIncUltFechaInv(fecsalE.getText().equals(feulinE.getText()));
      mvtosAlm.setValorarDesp(valDesp);
      mvtosAlm.setAlmacen(alm_inicE.getValorInt());
      mvtosAlm.setEntornoUsuario(EU);
      mvtosAlm.setSoloInventario(fecsalE.getText().equals(feulinE.getText()));

      if (!alm_inicE.isNull())
          mvtosAlm.setSerieX(true);
      mvtosAlm.iniciarMvtos(feulin,fecsalE.getText(),dtCon1);
//      mvtosAlm.setDesglIndiv(true);
      char orden=ordenE.getValor().charAt(0);
      jt.removeAllDatos();
      s = "SELECT a.*,f.fpr_nomb FROM V_ARTICULO as a left join v_famipro as f on f.fpr_codi = a.fam_codi where 1=1  " +
          (camCodi != null ? " and a.cam_codi= '" + camCodi + "'" : "") +
          (pro_codiE.isNull()?"":" and a.pro_codi = "+pro_codiE.getValorInt())+
          (pro_artconE.getValorInt()==0?"": " and a.pro_artcon "+(pro_artconE.getValorInt()==1?"!":"")+"=0")+
          " and a.pro_tiplot='V' "+
          " ORDER BY "+(orden=='F'?" fam_codi,":"")+
          " pro_codi ";
      dtProd = stUp.executeQuery(s);
      int unidT=0;
      double kilosT=0,importeT=0;
      double kgVenT=0,kgComT=0,kgRegT=0;
     
      ArrayList<ArrayList> datos=new ArrayList();
      int famCodi=0;
      while (next())
      {
        if (cancelado)
        {
          mensajeErr("Consulta Cancelada ...");
          
          mensaje("");
          resetMsgEspere();
//          activar(true);
          pro_codiE.requestFocus();
          return false;
        }
        if (famCodi!=dtProd.getInt("fam_codi") && orden=='F' )
        {
           ArrayList v1 = new ArrayList();  
           v1.add("");
           v1.add("Fam: "+dtProd.getString("fpr_nomb"));
           v1.add("" );
           v1.add("" );
           v1.add("" );
           v1.add("" );
           v1.add("");
           datos.add(v1);
           famCodi=dtProd.getInt("fam_codi");
        }
        ArrayList v = new ArrayList();
        v.add(dtProd.getString("pro_codi"));
        v.add(dtProd.getString("pro_nomb"));
        v.add("" + unid);
        v.add("" + kilos);
        v.add("" + precio);
        v.add("" + (kilos * precio));
        v.add(famCodi);
        datos.add(v);
        kilosT+=kilos;
        unidT+=unid;
        kgComT+=kgCom;;
        kgVenT+=kgVen;
        kgRegT+=+kgReg;
        importeT+=(kilos * precio);
      }
      jt.setDatos(datos);
      jt.requestFocusInicio();
      kilosE.setValorDec(kilosT);
      unidE.setValorDec(unidT);
      importeE.setValorDec(importeT);
//        System.out.println("Kilos venta: "+kgVenT+" Kg.Compra: "+kgComT+" Kg. Reg:"+kgRegT);
      resetMsgEspere();
      mensaje("Pulse Doble click en una linea para ver los movimientos");
      mensajeErr("Consulta .... Generada");
//      activar(true);
      Pdatcon.resetCambio();
      pro_codiE.requestFocus();
    }
    catch (Exception ex)
    {
      Error("Error al buscar Productos", ex);
    }
    return true;
  }

  void listar()
  {
    try
    {
      msgEspere("Generando Listado.. espere, por favor");
      popEspere_BCancelarSetEnabled(false);
      if (Pdatcon.hasCambio() || cancelado)
      {
        if (! consultar())
            return;
      }
      mensaje("Generando listado ...");
//      activar(false);
      
      nLin=-1;
      imprList=true;
      JasperReport jr = util.getJasperReport(EU, "lisaldos");

      HashMap mp = new HashMap();
      mp.put("fecini",feulin);
      mp.put("fecsal",fecsalE.getText());
      mp.put("ordenFam", ordenE.getValor().equals("F"));
      if (camCodi==null)
        mp.put("camara", "*TODAS*");
      else
        mp.put("camara",
               cam_codiE.getText() + " -> " + cam_codiE.getTextCombo());

      JasperPrint jp = JasperFillManager.fillReport(jr, mp, this);
      mensaje("");


      if (util.printJasper(jp, EU))
            mensajeErr("Listado .... Generado");
      
      mensaje("");
      
      resetMsgEspere();

      imprList=false;
    }
    catch (Exception ex)
    {
      Error("Error al buscar Productos",ex);
    }
  }

    @Override
  public boolean next() throws JRException
  {
    try {
      if (imprList)
      {
        nLin++;
        if (nLin>=jt.getRowCount())
            return false;
        if (jt.getValString(nLin,0).equals(""))
            nLin++;
        
        return true;
      }
      if (! dtProd.next())
        return false;
      actualizaMsg("Ejecutando Consulta de Saldos\nTratando producto "+dtProd.getInt("pro_codi"),false);

      precio=0;
      while (true)
      {
        
        if (! mvtosAlm.calculaMvtos(dtProd.getInt("pro_codi"), dtCon1, dtStat, null,null))
        {
          if (! dtProd.next())
            return false;
          continue;
        }
        kilos=mvtosAlm.getKilosStock();
        unid=mvtosAlm.getUnidStock();
        kgCom=mvtosAlm.getKilosCompra();
        kgVen=mvtosAlm.getKilosVenta();
        kgReg=mvtosAlm.getKilosRegul();
        precio = mvtosAlm.getPrecioStock()+
                (pro_cosincE.isSelected()?dtProd.getDouble("pro_cosinc"):0) ;
                //getPreMed(dtProd.getInt("pro_codi"), fecsalE.getText());
//        if (precio != 0 && (kilos >= 1 || kilos <= -1))
        if ( (kilos >= 1 || kilos <= -1))
          return true;
        if (! dtProd.next())
          return false;
      }

    } catch (Exception k)
    {
      Error("Error en Next: ",k);
      throw new JRException(k.getMessage());
    }
  }

    @Override
  public Object getFieldValue(JRField field) throws JRException
  {
    try
    {
      if (field.getName().equals("pro_codi"))
        return new Integer(jt.getValorInt(nLin,0));
       if (field.getName().equals("fam_codi"))
        return new Integer(jt.getValorInt(nLin,6));
       if (field.getName().equals("fpr_nomb"))
        return MantFamPro.getNombreFam(jt.getValorInt(nLin,6),dtStat);
      if (field.getName().equals("pro_nomb"))
        return jt.getValString(nLin,1);
      if (field.getName().equals("kilos"))
        return new Double(jt.getValorDec(nLin,3));
      if (field.getName().equals("unid"))
        return new Integer(jt.getValorInt(nLin,2));
      if (field.getName().equals("precio"))
        return new Double(jt.getValorDec(nLin,4));
      if (field.getName().equals("importe"))
        return new Double(jt.getValorDec(nLin,5));
      throw new JRException("Field: "+field.getName()+" NO valido");
    }
    catch (NumberFormatException n)
    { // Para cuando salen infinitos y cosas asi
      return new Double(0);
    }
    catch (Exception k)
    {
      Error("Error en getFieldValue: ("+field.getName()+")", k);
      throw new JRException(k.getMessage());
    }

  }
  

  String getStrSql(int proCodi, String fecini, String fecfin) throws SQLException
  {
    // Compras
    if (ps==null)
    {
      s = "SELECT 'CO' as sel,'+' as tipmov,c.acc_fecrec as fecmov," +
          " l.acl_canti as canti,l.acl_prcom as precio,acl_numcaj as unid,0 AS PRO_CODORI " +
          " FROM v_albacoc c,v_albacol l" +
          " where c.emp_codi = l.emp_codi " +
          " AND c.acc_serie = l.acc_serie " +
          " AND c.acc_nume = l.acc_nume " +
          " and c.acc_ano = l.acc_ano " +
          " and l.acl_canti <> 0 " +
          " AND l.pro_codi = ?" +
          (alm_inicE.getValorInt() == 0 ? "" : " and l.alm_codi = " + alm_inicE.getValorInt()) +
          " AND c.acc_fecrec >= TO_DATE('" + fecini + "','dd-MM-yyyy') " +
          " and c.acc_fecrec <= TO_DATE('" + fecfin + "','dd-MM-yyyy') ";
      s += " UNION all"+  // Albaranes de Venta
           " select 'VE' as sel,'-' as tipmov,c.avc_fecalb as fecmov," +
          " l.avl_canti as canti,0 as precio,avl_unid as unid,0 AS PRO_CODORI  " +
          "  from v_albavel l, v_albavec c" +
          " where c.emp_codi = l.emp_codi " +
          " AND c.avc_serie = l.avc_serie " +
          " AND c.avc_nume = l.avc_nume " +
          " and c.avc_ano = l.avc_ano " +
          " and l.avl_canti <> 0 " +
          (alm_inicE.getValorInt() == 0 ? "" : " and c.alm_codori = " + alm_inicE.getValorInt()) +
          " AND C.AVC_SERIE != 'X' " + // Quito los albaranes de trasp. ALMACEN.
          " AND l.pro_codi = ? " +
          " AND c.avc_fecalb >= TO_DATE('" + fecini + "','dd-MM-yyyy') " +
          " and c.avc_fecalb <= TO_DATE('" + fecfin + "','dd-MM-yyyy') ";
      s += " UNION all " + // Despieces (Salidas de Almacen)
          " select 'DS' as sel,'" +
          (opValDesp.isSelected() ? "+":"-")+"'  as tipmov ,"+
          "deo_fecha as fecmov," +
          " deo_kilos as canti,deo_prcost as precio,1 as unid,0 AS PRO_CODORI  " +
          " from  v_despori where " +
          " deo_kilos <> 0 " +
          (alm_inicE.getValorInt() == 0 ? "" : " and deo_almori = " + alm_inicE.getValorInt()) +
          " AND pro_codi = ? " +
          " AND deo_fecha >= TO_DATE('" + fecini + "','dd-MM-yyyy') " +
          " and deo_fecha <= TO_DATE('" + fecfin + "','dd-MM-yyyy') ";
      s += " UNION all " + // Despieces (Entradas en Almacen)
          " select 'DE' as sel, '+' as tipmov,c.deo_fecha as fecmov," +
          " l.def_kilos as canti,l.def_prcost as precio,def_numpie as unid,C.PRO_CODI AS PRO_CODORI  " +
          " from  v_despori c,v_despfin l where " +
          " C.EMP_codi = l.emp_codi " +
          " and c.eje_nume = l.eje_nume " +
          " and c.deo_codi = l.deo_codi " +
          " and l.def_kilos <> 0 " +
          (alm_inicE.getValorInt() == 0 ? "" : " and alm_codi = " + alm_inicE.getValorInt()) +
          " AND l.pro_codi = ? " +
          " AND c.deo_fecha >= TO_DATE('" + fecini + "','dd-MM-yyyy') " +
          " and c.deo_fecha <= TO_DATE('" + fecfin + "','dd-MM-yyyy') ";
      s += " UNION all " + // Regularizaciones.
          " select 'RE' as sel,tir_afestk as tipmov,r.rgs_fecha as fecmov," +
          " r.rgs_kilos as canti,r.rgs_prregu as precio,1 as unid,0 AS PRO_CODORI  " +
          " FROM v_regstock r, v_motregu m WHERE " +
          " m.tir_codi = r.tir_codi " +
          " and rgs_kilos <> 0" +
          " and rgs_trasp != 0 "+
          (alm_inicE.getValorInt() == 0 ? "" : " and alm_codi = " + alm_inicE.getValorInt()) +
          " AND r.pro_codi = ? " +
          " AND r.rgs_fecha >= TO_DATE('" + fecini + "','dd-MM-yyyy') " +
          " and r.rgs_fecha <= TO_DATE('" + fecfin + "','dd-MM-yyyy') ";
      s += " ORDER BY 3,2 desc"; // FECHA y tipo
      ps=dtCon1.getConexion().prepareStatement(dtCon1.getStrSelect(s));
    }
    ps.setInt(1,proCodi);
    ps.setInt(2,proCodi);
    ps.setInt(3,proCodi);
    ps.setInt(4,proCodi);
    ps.setInt(5,proCodi);
    return s;
  }

//  void activar(boolean b)
//  {
//    pro_codiE.setEnabled(b);
//    alm_inicE.setEnabled(b);
//    cam_codiE.setEnabled(b);
//    pro_artconE.setEnabled(b);
//    opValDesp.setEnabled(b);
//    ordenE.setEnabled(b);
//    fecsalE.setEnabled(b);
//    feulinE.setEnabled(b);
//    Baceptar.setEnabled(b);
//
//    statusBar.setEnabled(b);
//    Bimpr.setEnabled(b);
//  }
    @Override
  public void matar(boolean cerrarConexion)
 {
   if (muerto)
     return;
   if (ifMvtos!=null)
   {
     ifMvtos.setVisible(false);
     ifMvtos.dispose();
   } 
   super.matar(cerrarConexion);
 }

}

class threadlisaldos extends Thread
{
  final static int CONSULTA=1;
  final static int LISTA=2;
  lisaldos lisal;
  int opcion; 
  
  public threadlisaldos(lisaldos lisal,int opcion)
  {
    this.lisal=lisal;
    this.opcion=opcion;
  }
    @Override
  public void run()
  {
   
    this.setPriority(Thread.MAX_PRIORITY-2);
    if (opcion==CONSULTA)
      lisal.consultar();
    if (opcion==LISTA)
      lisal.listar();
 
  }
}
class cglisaldos implements VirtualGrid
{
 public boolean getColorGrid(int row, int col, Object valor, boolean selecionado, String nombreGrid)
 {
     if (col==1 && ((String) valor).startsWith("Fam:"))
         return true;
     return false;
 }
}

class  ifMvtosClase extends ventana
{
      lisaldos papa;
      public void setPadre(lisaldos padre)
      {
          papa=padre;
          this.setTitle("Consulta Mvtos de Prod.");
      }
       @Override
        public void matar()
        {
          setVisible(false);
    
          papa.setEnabled(true);
          papa.setFoco(null);
          try {
              papa.setSelected(true);
          } catch (Exception k){}
        }  
  }
