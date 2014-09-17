package gnu.chu.anjelica.almacen;

import com.jgoodies.looks.plastic.PlasticInternalFrameUI;
import gnu.chu.Menu.Principal;
import gnu.chu.camposdb.empPanel;
import gnu.chu.camposdb.proPanel;
import gnu.chu.camposdb.prvPanel;
import gnu.chu.controles.*;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.ventana;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;
import javax.swing.BorderFactory;
import javax.swing.JInternalFrame;
import javax.swing.border.TitledBorder;
//import javax.swing.plaf.basic.BasicInternalFrameUI;


/**
 *
 * <p>T�tulo: clstkdes</p>
 * <p>Descripción: Cons/List Stock por producto / Proveedor / Fecha Cad.</p>
 * <p>Copyright: Copyright (c) 2005-2014
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
 * <p>Empresa: miSL</p>
 * @author chuchi P
 * @version 1.0 - 20041227
 */
public class clstkdes extends ventana
{
  private String filtroEmpr;
  JInternalFrame ifPru=new JInternalFrame();
  proPanel pro_codiE=new proPanel();
  prvPanel prv_codiE=new prvPanel();
  String s;
  CPanel Pprinc = new CPanel();
  CPanel Pcons = new CPanel();
  CComboBox feulinE = new CComboBox();
  CLabel cLabel2 = new CLabel();
  CTextField fecfinE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel17 = new CLabel();
  proPanel proiniE = new proPanel();
  CLabel cLabel3 = new CLabel();
  CLabel cLabel19 = new CLabel();
  CLabel cLabel18 = new CLabel();
  CLinkBox pro_disc4E = new CLinkBox();
  CLinkBox pro_disc3E = new CLinkBox();
  TitledBorder titledBorder2;
  CButton Bconsulta = new CButton("Aceptar (F4)",Iconos.getImageIcon("check"));
  CLabel cLabel1 = new CLabel();
  prvPanel prviniE = new prvPanel();
  CLabel cLabel4 = new CLabel();
  CTextField fecainE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel5 = new CLabel();
  CTextField fecafinE = new CTextField(Types.DATE, "dd-MM-yyyy");
  Cgrid jt = new Cgrid(7);
  proPanel profinE = new proPanel();
  CLabel cLabel6 = new CLabel();
  CLabel cLabel7 = new CLabel();
  prvPanel prvfinE = new prvPanel();
  String proDisc3,proDisc4,feulst,fecStockStr;
  HashMap ht=new HashMap();
  HashMap htPrv=new HashMap();
  HashMap htGrp=new HashMap();
  int posFin;
  String tipMov;
  int almCodi=1;
  GridBagLayout gridBagLayout1 = new GridBagLayout();


  public clstkdes(EntornoUsuario eu, Principal p)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    setTitulo("Cons/List Stock Desglosado");

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

  public clstkdes(gnu.chu.anjelica.menu p, EntornoUsuario eu)
  {

    EU = eu;
    vl = p.getPanelPrinc();
    setTitulo("Cons/List Stock Desglosado");
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
    this.setSize(new Dimension(632, 502));
    this.setVersion("2013-02-18");
    titledBorder2 = new TitledBorder("");


    conecta();
    statusBar = new StatusBar(this);
    Pprinc.setLayout(gridBagLayout1);
    Pcons.setBorder(BorderFactory.createRaisedBevelBorder());
    Pcons.setMaximumSize(new Dimension(616, 111));
    Pcons.setMinimumSize(new Dimension(616, 111));
    Pcons.setPreferredSize(new Dimension(616, 111));
    Pcons.setLayout(null);
    cLabel2.setText("En Fecha");
    cLabel2.setBounds(new Rectangle(188, 42, 57, 18));
    cLabel2.setOpaque(true);
    cLabel2.setRequestFocusEnabled(true);
    fecfinE.setOpaque(true);
    fecfinE.setBounds(new Rectangle(251, 42, 81, 18));
    cLabel17.setText("Fec.Ult.Inv");
    cLabel17.setBounds(new Rectangle(5, 42, 58, 18));
    cLabel3.setText("De Prod.");
    cLabel3.setBounds(new Rectangle(5, 4, 54, 18));
    cLabel19.setText("Mayor/Calle");
    cLabel19.setBounds(new Rectangle(343, 62, 72, 18));
    cLabel18.setText("Camara");
    cLabel18.setBounds(new Rectangle(344, 42, 52, 18));
    titledBorder2.setTitle("Clas. Prod.");

    cLabel1.setText("De Prov.");
    cLabel1.setBounds(new Rectangle(5, 22, 51, 18));
    prviniE.setText("");
    prviniE.setBounds(new Rectangle(57, 22, 240, 18));
    cLabel4.setText("De Fec.Cad. ");
    cLabel4.setBounds(new Rectangle(4, 62, 72, 18));
    fecainE.setOpaque(true);
    fecainE.setText("De Fec. Cad. ");
    fecainE.setBounds(new Rectangle(75, 62, 81, 18));
    cLabel5.setText("A Fec.Cad. ");
    cLabel5.setBounds(new Rectangle(190, 62, 64, 18));
    fecafinE.setOpaque(true);
    fecafinE.setBounds(new Rectangle(251, 62, 81, 18));
    pro_disc3E.setAncTexto(30);
    pro_disc3E.setBounds(new Rectangle(415, 42, 192, 18));
    cLabel6.setToolTipText("");
    cLabel6.setText("A Prod.");
    cLabel6.setBounds(new Rectangle(308, 4, 45, 18));
    cLabel7.setText("A Prov.");
    cLabel7.setBounds(new Rectangle(308, 22, 51, 18));

    Bconsulta.setBounds(new Rectangle(229, 83, 153, 19));
    Bconsulta.setMargin(new Insets(0, 0, 0, 0));
    ArrayList v=new ArrayList();
    jt.setMaximumSize(new Dimension(598, 273));
    jt.setMinimumSize(new Dimension(598, 273));
    jt.setPreferredSize(new Dimension(598, 273));
    pro_disc4E.setAncTexto(25);
    pro_disc4E.setBounds(new Rectangle(417, 62, 190, 18));
    feulinE.setBounds(new Rectangle(75, 42, 107, 18));
    prvfinE.setBounds(new Rectangle(355, 22, 254, 18));
    profinE.setBounds(new Rectangle(355, 4, 254, 18));
    proiniE.setBounds(new Rectangle(57, 4, 240, 18));
    v.add("Prod."); // 0
    v.add("Descripcion Prod."); // 1
    v.add("Proveedor"); // 2
    v.add("Nombre Prv"); // 3
    v.add("Fec.Cad"); // 4
    v.add("Kilos"); // 5
    v.add("Unidades"); // 6
    jt.setCabecera(v);
    jt.setAnchoColumna(new int[]{70,150,60,150,90,80,80});
    jt.setAlinearColumna(new int[]{2,0,2,0,1,2,2});
    jt.setAjustarGrid(true);
    jt.setFormatoColumna(5,"----,--9.9");
    jt.setFormatoColumna(6,"---,--9");
    this.getContentPane().add(statusBar,BorderLayout.SOUTH);
    this.getContentPane().add(Pprinc,  BorderLayout.CENTER);
    Pcons.add(proiniE, null);
    Pcons.add(cLabel3, null);
    Pcons.add(cLabel6, null);
    Pcons.add(profinE, null);
    Pcons.add(cLabel7, null);
    Pcons.add(prviniE, null);
    Pcons.add(cLabel1, null);
    Pcons.add(cLabel4, null);
    Pcons.add(fecainE, null);
    Pprinc.add(jt,   new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    Pprinc.add(Pcons,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 6), 0, 0));
    Pcons.add(cLabel5, null);
    Pcons.add(fecafinE, null);
    Pcons.add(prvfinE, null);
    Pcons.add(cLabel17, null);
    Pcons.add(feulinE, null);
    Pcons.add(cLabel2, null);
    Pcons.add(fecfinE, null);
    Pcons.add(Bconsulta, null);
    Pcons.add(pro_disc3E, null);
    Pcons.add(cLabel18, null);
    Pcons.add(cLabel19, null);
    Pcons.add(pro_disc4E, null);
  }
    @Override
  public void iniciarVentana() throws Exception
  {
    filtroEmpr=empPanel.getStringAccesos(dtStat, EU.usuario,true);
    ifPru.setSize(new Dimension(100,100));
    vl.add(ifPru);
    ifPru.putClientProperty(
             PlasticInternalFrameUI.IS_PALETTE,
             Boolean.TRUE);
//    ((BasicInternalFrameUI)ifPru.getUI()).setNorthPane(null);

    Pcons.setButton(KeyEvent.VK_F4,Bconsulta);
    String feulin;
    s =
        "select distinct(rgs_fecha) as cci_feccon from v_regstock as r,v_motregu  as m " +
        " where r.tir_codi = m.tir_codi " +
        (filtroEmpr==null?"":" and r.emp_codi in ("+filtroEmpr+")")+
        " and M.tir_afestk='=' " +
        " order by cci_feccon desc ";

    if (dtStat.select(s))
    {
      feulin = dtStat.getFecha("cci_feccon", "dd-MM-yyyy");
      do
      {
        feulinE.addItem(dtStat.getFecha("cci_feccon", "dd-MM-yyyy"),
                        dtStat.getFecha("cci_feccon", "dd-MM-yyyy"));
      }
      while (dtStat.next());
    }
    else
    {
      feulin = "01-01-" + EU.ejercicio; // Buscamos desde el principio del a�o.
      feulinE.addItem(feulin);
    }

    feulinE.setText(feulin);
    prviniE.iniciar(dtStat,this,vl,EU);
    prvfinE.iniciar(dtStat,this,vl,EU);

    proiniE.iniciar(dtStat,this,vl,EU);
    profinE.iniciar(dtStat,this,vl,EU);

    DatosTabla dt = dtStat;


    gnu.chu.anjelica.pad.pdconfig.llenaDiscr(dt,pro_disc3E,"AC",EU.em_cod);
    pro_disc3E.addDatos("**", "TODOS");

    gnu.chu.anjelica.pad.pdconfig.llenaDiscr(dt,pro_disc4E,"A4",EU.em_cod);
    pro_disc4E.addDatos("**", "TODOS");

//    for (int n=0;n < ifPru.getComponentCount();n++)
//      System.out.println("Componente: "+n+" -> "+ifPru.getComponent(n));
    activarEventos();
  }

  void activarEventos()
  {
    Bconsulta.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Bconsulta_actionPerformed();
      }
    });
//    Bdiscrim.addActionListener(new ActionListener()
//    {
//      public void actionPerformed(ActionEvent e)
//      {
//        if (!ifPru.isVisible())
//          ifPru.setLocation(clstkdes.this.getLocation().x+ Bdiscrim.getBounds().x+20,clstkdes.this.getLocation().y+Bdiscrim.getBounds().y+50);
//        ifPru.setVisible(true);
//        try {
//          ifPru.setSelected(true);
//        } catch (Exception k){}
//      }
//    });
    this.addPropertyChangeListener(new PropertyChangeListener()
    {
      public void propertyChange(PropertyChangeEvent e)
      {
//        System.out.println("this " + e.getPropertyName() + " pr.iD" + e.getNewValue());
        if (e.getPropertyName().equals("selected"))
        {
          if (! ( (Boolean) e.getNewValue()).booleanValue())
            return;
        }
        else
          return;
        if (ifPru.isVisible())
        {
          ifPru.setVisible(false);
          try
          {
            clstkdes.this.setSelected(true);
          }
          catch (Exception k)
          {}
        }
      }
    });
  


  }

  void Bconsulta_actionPerformed()
  {
    if (fecfinE.isNull() || fecfinE.getError())
    {
      mensajeErr("Fecha de Stock no es Valida");
      fecfinE.requestFocus();
      return;
    }
    if (fecainE.getError())
    {
      mensajeErr("Fecha Caducidad Inicial NO es Valida");
      fecainE.requestFocus();
      return;
    }
    if (fecafinE.getError())
   {
     mensajeErr("Fecha Caducidad Final NO es Valida");
     fecafinE.requestFocus();
     return;
   }

//    if (feulinE.isNull() || feulinE.getError())
//    {
//      mensajeErr("Fecha Ultimo Inventario no es Valida");
//      feulinE.requestFocus();
//      return;
//    }

    new clstkdesTh(this);
  }
  public void consulta()
  {
    String proDisc3 = Formatear.reemplazar(pro_disc3E.getText(), "*", "%").trim();
    if (proDisc3.equals("%%") || proDisc3.equals("%") || proDisc3.equals(""))
      proDisc3 = null;
    String proDisc4 = Formatear.reemplazar(pro_disc4E.getText(), "*", "%").trim();
    if (proDisc4.equals("%%") || proDisc4.equals("%") || proDisc4.equals(""))
      proDisc4 = null;

    feulst = feulinE.getText();
    fecStockStr = fecfinE.getText();
    jt.removeAllDatos();
    ht.clear();
    htPrv.clear();
    htGrp.clear();

    String llave;
    s=getStrSql();

//    debug("s: "+s);
    String fecCadErr;
    fecCadErr="01-01-"+EU.ejercicio;
    int prvCodi;
    double canti=0,cantiGr=0;
    double numuni=0,numuniGr=0;
    Object o;
    feulst="";

    try {
      if (!dtCon1.select(s))
      {
        mensajeErr("No encontrado Movimientos entre estas fechas");
        return;
      }
      int n=0;
      do
      {
        n++;
        if (n % 10 == 0)
          mensajeErr("Buscando Mvtos. Tratando Producto: "+dtCon1.getString("pro_codi"),false);
        tipMov=dtCon1.getString("tipmov");
        llave=dtCon1.getString("pro_codi")+"|"+
            dtCon1.getInt("emp_codi") + "|"+dtCon1.getInt("eje_nume") +"|"+
            dtCon1.getString("serie")+ "|"+ dtCon1.getInt("lote") + "|" +dtCon1.getInt("numind",true);

        if (tipMov.equals("="))
        {
          if (feulst.equals(dtCon1.getFecha("fecmov")))
            tipMov="+";
          else
          {
            feulst = dtCon1.getFecha("fecmov");
            ht.put(llave,
                   dtCon1.getString("canti", true) + "|" +
                   dtCon1.getString("canind", true));
            if (buscaDatPrv())
              htPrv.put(llave, dtCon1.getString("pro_codi") + "|" +
                        dtStat.getString("prv_codi") + "|" +
                        dtStat.getFecha("feccad", "dd-MM-yyyy"));
            else
              htPrv.put(llave, dtCon1.getString("pro_codi") + "|0|");
            continue;
          }
        }
        if (tipMov.equals("+"))
        {
          if (!dtCon1.getString("sel").equals("R"))
            htPrv.put(llave, dtCon1.getString("pro_codi")+"|"+dtCon1.getString("prv_codi")+"|"+dtCon1.getFecha("feccad","dd-MM-yyyy"));
          else
          {
            if (buscaDatPrv())
              htPrv.put(llave, dtCon1.getString("pro_codi") + "|" +
                   dtStat.getString("prv_codi") + "|" +
                   dtStat.getFecha("feccad", "dd-MM-yyyy"));
            else
               htPrv.put(llave, dtCon1.getString("pro_codi") + "|0|");
          }
          if ((o=ht.get(llave))==null)
            ht.put(llave, dtCon1.getString("canti",true)+"|"+dtCon1.getString("canind",true));
          else
          {
            canti = Double.parseDouble(getCampoLlave(o.toString(),0));
            numuni= Double.parseDouble(getCampoLlave(o.toString(),posFin));
            ht.put(llave,  (dtCon1.getDouble("canti")+canti)+"|"+(dtCon1.getInt("canind",true)+numuni));
          }
        }
        if (tipMov.equals("-"))
        {
          if ((o=ht.get(llave))==null)
          {
            canti = 0;
            numuni=0;
          }
          else
          {
            canti = Double.parseDouble(getCampoLlave(o.toString(),0));
            numuni= Double.parseDouble(getCampoLlave(o.toString(),posFin));
          }
          if (dtCon1.getString("sel").equals("V") && dtCon1.getString("seralb").equals("X"))
          { // Trasp. entre almacenes.
            if (dtCon1.getInt("almori")==almCodi)
            {
              canti = canti + dtCon1.getDouble("canti", true);
              numuni= numuni + dtCon1.getInt("canind", true);
            }
            else
            {
              canti = canti - dtCon1.getDouble("canti", true);
               numuni= numuni - dtCon1.getInt("canind", true);
            }
          }
          else
          {
            canti = canti - dtCon1.getDouble("canti", true);
            numuni = numuni - dtCon1.getInt("canind", true);
          }
           ht.put(llave, canti+"|"+numuni);
        }
      } while (dtCon1.next());

      Iterator it;
      it=ht.keySet().iterator();
      Object key;
      String proCodi="";
      Object valor;
      n=0;
      while (it.hasNext ()) {

        key=it.next();
        valor=ht.get(key);
        canti = Double.parseDouble(getCampoLlave(valor.toString(),0));
        numuni= Double.parseDouble(getCampoLlave(valor.toString(),posFin));

        proCodi=getProdLlave(key.toString());
        n++;
        if (n % 10 == 0)
          mensajeErr("Buscando Prov.. Tratando Producto: "+proCodi,false);

        // Busco Proveedor/Fec.Cad. en htPrv
        valor=htPrv.get(key);
        if (valor==null)
        {
          if (buscaDatPrv(key,proCodi))
            llave=proCodi+"|"+dtStat.getString("prv_codi")+"|"+dtStat.getFecha("feccad","dd-MM-yyyy");
          else
            llave= proCodi+"|0|";
        }
        else
          llave=valor.toString();

        if ( (o = htGrp.get(llave)) == null)
          htGrp.put(llave,""+canti+"|"+numuni);
        else
        {
          cantiGr = Double.parseDouble(getCampoLlave(o.toString(),0));
          numuniGr= Double.parseDouble(getCampoLlave(o.toString(),posFin));
          htGrp.put(llave, "" + (canti+cantiGr)+"|"+(numuni+numuniGr));
        }

//        System.out.println(key+" : "+htPrv.get(key));
      }

      java.util.List list = new ArrayList(htGrp.keySet());
      Collections.sort(list);

      it = list.iterator();


      while (it.hasNext ()) {
        key=it.next();
        llave=key.toString();
        valor=htGrp.get(key);
        canti=Double.parseDouble(getCampoLlave(valor.toString(),0));
        numuni=Double.parseDouble(getCampoLlave(valor.toString(),posFin));
        if (canti+numuni==0)
          continue;
        Vector v=new Vector();
        v.add(getProdLlave(llave)); // 0
        v.add(pro_codiE.getNombArtCli(Integer.parseInt(getProdLlave(llave)),0,EU.em_cod,dtStat)); // 1
        prvCodi=Integer.parseInt(getCampoLlave(llave,posFin));
        v.add(""+prvCodi); // 2
        v.add(prv_codiE.getNombPrv(""+prvCodi,dtStat)); // 3
        v.add(getCampoLlave(llave,posFin)); // 4
        key=htGrp.get(key);
        llave=key.toString();
        v.add(""+canti); // 5
        v.add(""+numuni); // 6
        jt.addLinea(v);
//        System.out.println(key+" : "+htGrp.get(key));
      }
      mensajeErr("Consulta ... REALIZADA");
    }
    catch (Exception k)
    {
      Error("Error al buscar Movimientos", k);
    }
  }

  String getStrSql()
  {
    String condArt = (proDisc3 == null ? "" :" and a.cam_codi like '%" + proDisc3 + "%'") +
        (proDisc4 == null ? "" :
         " and a.pro_disc4 like '%" + proDisc4 + "%'") +
        " and a.pro_codi > 99 "+
        (!proiniE.isNull() ? "  and a.PRo_codi >= " + proiniE.getValorInt() :"") +
        (!profinE.isNull() ? "  and a.PRo_codi <= " + profinE.getValorInt() :
         "");
    String sql;

    sql = "SELECT 'C' as sel,'+' as tipmov,c.acc_fecrec as fecmov," +
        " c.acc_serie as serie," +
        " c.acc_nume as  lote," +
        " i.acp_canti as canti,0 as precio,i.acp_numind as numind, " +
        " i.pro_codi,i.emp_codi,i.acc_ano as eje_nume,0 as almori,'' AS seralb, " +
        " c.prv_codi as prv_codi,i.acp_feccad as feccad,i.acp_canind as canind "+
        " FROM v_albacoc c,v_albcompar i,v_articulo a " +
        " where i.emp_codi = c.emp_codi " +
        " AND i.acc_serie = c.acc_serie " +
        " AND i.acc_nume = c.acc_nume " +
        " and i.acc_ano = c.acc_ano " +
        " and a.pro_codi = i.pro_codi " +
        " and i.acp_canti <> 0 " +
        condArt +
        " AND c.acc_fecrec >= TO_DATE('" + feulst + "','dd-MM-yyyy') " +
        " and c.acc_fecrec <= TO_DATE('" + fecStockStr + "','dd-MM-yyyy') ";
    sql += " UNION all"; // Albaranes de Venta
    sql += " select 'V' as sel,'-' as tipmov,c.avc_fecalb as fecmov," +
        "  i.avp_serlot as serie,i.avp_numpar as  lote," +
        " i.avp_canti as canti,0 as precio,i.avp_numind as numind, " +
        " i.pro_codi,i.avp_emplot,i.avp_ejelot as eje_nume,l.alm_codi as almori,c.avc_serie AS seralb, " +
        " '0' as prv_codi, null as feccad, avp_numuni as canind "+
        "  from v_albavel l, v_albavec c,v_albvenpar i,v_articulo a" +
        " where c.emp_codi = l.emp_codi " +
        " AND c.avc_serie = l.avc_serie " +
        " AND c.avc_nume = l.avc_nume " +
        " and c.avc_ano = l.avc_ano " +
        " and i.emp_codi = l.emp_codi " +
        " AND i.avc_serie = l.avc_serie " +
        " AND i.avc_nume = l.avc_nume " +
        " and i.avc_ano = l.avc_ano " +
        " and i.avl_numlin = l.avl_numlin " +
        " and a.pro_codi = i.pro_codi " +
        " and i.avp_canti <> 0 " +
        condArt + " AND c.avc_fecalb >= TO_DATE('" + feulst +"','dd-MM-yyyy') " +
        " and c.avc_fecalb <= TO_DATE('" + fecStockStr + "','dd-MM-yyyy') ";
    sql += " UNION all " + // Despieces (Salidas)
        " select 'D' as sel,'-' as tipmov,deo_fecha as fecmov," +
        "  deo_serlot as serie,pro_lote as  lote," +
        " deo_kilos as canti,0 as precio,pro_numind as numind, " +
        " L.pro_codi,deo_emplot,deo_ejelot as eje_nume,0 as almori,'' AS seralb, " +
         " '0' as prv_codi, null as feccad, 1 as canind "+
        " from  v_despori l,v_articulo a where " +
        "  a.pro_codi = l.pro_codi " +
        " and deo_kilos <> 0 " +
        condArt + " and deo_fecha >= TO_DATE('" + feulst + "','dd-MM-yyyy') " +
        " and deo_fecha <= TO_DATE('" + fecStockStr + "','dd-MM-yyyy') ";
    sql += " UNION all " + // Despieces (Entradas)
        " select 'd' as sel, '+' as tipmov,c.deo_fecha as fecmov," +
        "  l.def_serlot as serie,l.pro_lote as  lote," +
        " l.def_kilos as canti,l.def_prcost as precio,l.pro_numind as numind, " +
        " l.pro_codi,l.def_emplot,l.def_ejelot as eje_nume,0 as almori,'' AS seralb, " +
         " c.prv_codi as prv_codi, c.deo_feccad as feccad,1 as canind "+
        " from  v_despori c,v_despfin l,v_articulo a where " +
        " C.EMP_codi = l.emp_codi " +
        " and c.eje_nume = l.eje_nume " +
        " and c.deo_codi = l.deo_codi " +
        " and a.pro_codi = l.pro_codi " +
        " and l.def_kilos <> 0 " +
        condArt + " AND c.deo_fecha >= TO_DATE('" + feulst + "','dd-MM-yyyy') " +
        " and c.deo_fecha <= TO_DATE('" + fecStockStr + "','dd-MM-yyyy') ";
    sql += " UNION all " + // Regularizaciones.
        " select 'R' as sel,tir_afestk as tipmov,r.rgs_fecha as fecmov," +
        "  r.pro_serie as serie,r.pro_nupar as  lote," +
        " r.rgs_kilos as canti,r.rgs_prregu as precio,r.pro_numind as numind, " +
        " R.pro_codi,r.emp_codi, eje_nume,0 as almori,'' AS seralb, " +
         " 0 as prv_codi, null as feccad,rgs_canti as canind "+
        " FROM v_regstock r, v_motregu m,v_articulo a WHERE " +
        " m.tir_codi = r.tir_codi " +
        " and a.pro_codi =r.pro_codi " +
        " and r.rgs_trasp != 0 "+
        " and r.rgs_kilos <> 0" +
        (filtroEmpr==null?"":" and r.emp_codi in ("+filtroEmpr+")")+
        condArt + " AND r.rgs_fecha >= TO_DATE('" + feulst + "','dd-MM-yyyy') " +
        " and r.rgs_fecha <= TO_DATE('" + fecStockStr + "','dd-MM-yyyy') ";
    sql += " ORDER BY 3,2 desc"; // FECHA y tipo

    return sql;
  }
  private boolean  buscaDatPrv(Object llave,String proCodi)  throws SQLException,java.text.ParseException
  {
    String campo=llave.toString();
    return buscaDatPrv(getCampoLlave(campo,posFin),
                              getCampoLlave(campo,posFin),
                              proCodi,
                              getCampoLlave(campo,posFin), getCampoLlave(campo,posFin),
                              getCampoLlave(campo,posFin));
  }

  String getProdLlave(String llave)
  {
    return getCampoLlave(llave,0);
  }
  String getCampoLlave(String llave,int posIni)
  {
    posFin = llave.indexOf("|",posIni);
    if (posFin< 0)
      return llave.substring(posIni);
    else
      return llave.substring(posIni, posFin++ );
  }

  private boolean buscaDatPrv() throws SQLException,java.text.ParseException
  {
    return buscaDatPrv( dtCon1.getString("emp_codi"),dtCon1.getString("eje_nume"),dtCon1.getString("pro_codi"),
                        dtCon1.getString("serie"),dtCon1.getString("lote"),dtCon1.getString("numind"));
  }

  private boolean buscaDatPrv(String empCodi,String ejeNume,String proCodi,String serie,String lote,String numind) throws SQLException,java.text.ParseException
  {
    s = " select prv_codi as prv_codi, deo_feccad as feccad" +
        " from  v_despfin as l,desporig as c where " +
        "  l.def_emplot = " + empCodi +
        " and l.def_ejelot = " + ejeNume +
        " and l.pro_codi = " + proCodi +
        " and l.pro_lote = " + lote +
        " and l.def_serlot = '" + serie + "'" +
        " and l.pro_numind = " + numind +
        " and l.eje_nume = c.eje_nume "+
        " and l.deo_codi = c.deo_codi ";
    if (!dtStat.select(s))
    {
      s = " select prv_codi as prv_codi, l.acp_feccad as feccad" +
          " from  v_albcompar as l,v_albacoc as g where " +
          "  l.emp_codi = " + empCodi +
          " and l.acc_ano = " + ejeNume +
          " and l.pro_codi = " + proCodi +
          " and l.acc_nume = " + lote +
          " and l.acc_serie = '" + serie+ "'" +
          " and l.acp_numind = " + numind +
          " and l.acc_ano = g.acc_ano " +
          " and l.emp_codi = g.emp_codi " +
          " and l.acc_serie = g.acc_serie" +
          " and l.acc_nume = g.acc_nume";
      dtStat.select(s);
    }
    return !dtStat.getNOREG();
  }
  public void matar(boolean cerrarConexion)
  {
    if (!muerto)
    {
      ifPru.dispose();
    }
    super.matar(cerrarConexion);
  }
}
class clstkdesTh extends Thread
{
  clstkdes padre;
  public clstkdesTh(clstkdes papa)
  {
    padre=papa;
    this.start();
  }
  public void run()
  {
    padre.setEnabled(false);
    padre.jt.panelG.setVisible(false);
    padre.mensaje("Buscando datos ... A esperar!!");
    padre.consulta();
    padre.mensaje("");
    padre.jt.panelG.setVisible(true);
    padre.setEnabled(true);
  }
}


