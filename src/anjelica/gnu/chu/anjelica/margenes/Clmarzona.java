package gnu.chu.anjelica.margenes;

import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.almacen.Comvalm;
import gnu.chu.anjelica.almacen.MvtosAlma;
import gnu.chu.anjelica.almacen.pdmotregu;
import gnu.chu.anjelica.pad.MantRepres;
import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.controles.Cgrid;
import gnu.chu.controles.StatusBar;
import gnu.chu.interfaces.ejecutable;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.DatosProd;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * <p>Título: Clmarzona</p>
 * <p>Descripción: Consulta/Listado Margenes por zonas</p>
 * <p>Created on 03-abr-2009, 18:14:38</p>
 *  <p>Copyright: Copyright (c) 2005-2017
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
 * <p>Empresa: MISL</p>
 * @author Chuchi P
 * @version 2.0
 */
public class Clmarzona extends ventana {
  private int fprCodi=0;
  private double kgFam=0,impFam=0,ganaFam=0;
  private double iCoFa=0,kCoFa=0;
  private double kgTot=0,impTot=0,ganaTot=0;
  private double iCoTo=0,kCoTo=0;
 
  private boolean swCreateTableTemp=true;
  private MvtosAlma mvtosAlm = new MvtosAlma();
  boolean cancelaConsulta=false;
  String fecinv;
  String proCod;
  double kgCom,impCom;
  DatosTabla dtVen,dtAdd;
  int tirCodInv=0;
//  String tirCodVert="";
  gnu.chu.anjelica.menu menu;
  String s;
  Hashtable<Integer,DatosProd> htArt=new Hashtable<Integer,DatosProd>();

  public Clmarzona(EntornoUsuario eu, Principal p)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    setTitulo("Cons./Listado Margenes por Zonas ");

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

  public Clmarzona(gnu.chu.anjelica.menu p, EntornoUsuario eu)
  {
    this.menu=p;
    EU = eu;
    vl = p.getLayeredPane();
    setTitulo("Cons./Listado Margenes por Zonas");
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

      this.setVersion("2017-07-04");
      statusBar = new StatusBar(this);
      this.getContentPane().add(statusBar, BorderLayout.SOUTH);
      conecta();

      initComponents();
      this.setSize(new Dimension(697,480));
      dtVen = new DatosTabla(ct);
      dtAdd = new DatosTabla(ct);
      Pcabe.setDefButton(Baceptar);
  }
  @Override
  public void iniciarVentana() throws Exception
  {
     jt.setNombre("Producto");
    jtGru.setNombre("Grupos");
    emp_codiE.iniciar(dtStat, this, vl, EU);
    sbe_codiE.iniciar(dtStat, this, vl, EU);
    
    cli_codiE.iniciar(dtStat, this, vl, EU);
    feciniE.iniciar(dtStat,this,vl,EU);
    fecfinE.iniciar(dtStat,this,vl,EU);
    rut_codiE.setAncTexto(30);
    rut_codiE.setFormato(Types.CHAR, "XX");       
    rut_codiE.setMayusculas(true);
    zon_codiE.setAncTexto(30);
    pdconfig.llenaDiscr(dtStat, rut_codiE, pdconfig.D_RUTAS, EU.em_cod);
    sbe_codiE.setFieldEmpCodi(emp_codiE.getTextField());
    emp_codiE.setValorInt(0);
    sbe_codiE.setValorInt(0);
    sbe_codiE.setAceptaNulo(true);
    emp_codiE.setAceptaNulo(true);
    pro_codiE.iniciar(dtStat, this, vl, EU);
    s="select distinct(rgs_fecha) as cci_feccon from v_regstock as r "+
        " where r.emp_codi = "+EU.em_cod+
        " and tir_afestk='=' "+
        " order by cci_feccon desc ";

    if (dtStat.select(s))
    {
      fecinv = dtStat.getFecha("cci_feccon", "dd-MM-yyyy");
      do
      {
        feulinE.addItem(dtStat.getFecha("cci_feccon","dd-MM-yyyy"),dtStat.getFecha("cci_feccon","dd-MM-yyyy"));
      } while (dtStat.next());
    }
    else
    {
      fecinv = "01-01-" + EU.ejercicio; // Buscamos desde el principio del a�o.
      feulinE.addItem(fecinv);
    }


    feulinE.setText(fecinv);

    zon_codiE.setFormato(Types.CHAR, "XX", 2);
    zon_codiE.texto.setMayusc(true);
    rep_codiE.setFormato(Types.CHAR, "XX", 2);
    rep_codiE.texto.setMayusc(true);

    gnu.chu.anjelica.pad.pdconfig.llenaDiscr(dtCon1, zon_codiE, "Cz", EU.em_cod);
    MantRepres.llenaLinkBox(rep_codiE, dtCon1);
   // gnu.chu.anjelica.pad.pdconfig.llenaDiscr(dtCon1, rep_codiE, "Cr", EU.em_cod);
    

    tirCodInv = pdmotregu.getTipoMotRegu(dtStat, "=");
//    tirCodVert=pdmotregu.getTiposRegul(dtStat,"V?");
    mvtosAlm.setResetCostoStkNeg(true);
    mvtosAlm.setFechasDocumento(true);
    arbolProdPanel.iniciar(dtCon1);
    activarEventos();
    feciniE.requestFocus();
  }
  void activarEventos()
  {
      popEspere_BCancelaraddActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        popEspere_BCancelarSetEnabled(false);
        setMensajePopEspere("A esperar.. estoy cancelando la consulta",false);
        cancelaConsulta=true;
      }
    });

    Baceptar.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Baceptar_actionPerformed();
      }
    });
    feciniE.addFocusListener(new FocusAdapter()
    {
            @Override
     public void focusLost(FocusEvent e) {
         try {
          if (feciniE.isNull() || feciniE.getError())
              return;
         int nLin=feulinE.getItemCount()-1;
         for (int n=nLin;n>=0;n--)
         {
            if (((String) feulinE.getItemAt(n)).equals(feciniE.getText()))
            {
                feulinE.setValor((String) feulinE.getItemAt(n));
                return;
            }
            if (Formatear.restaDias((String) feulinE.getItemAt(n), feciniE.getText())>0)
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
    jt.addMouseListener(new MouseAdapter()
     {
            @Override
       public void mouseClicked(MouseEvent e) {
         if (jt.isVacio())
           return;
         if (e.getClickCount()<2)
           return;
         llamaProgMvtos();
       }
     });
  }
  private void llamaProgMvtos()
  {
        try
        {    
            ejecutable prog;
            gnu.chu.anjelica.almacen.Comvalm cm;
            if (jf!=null)
            {                
                if ((prog=jf.gestor.getProceso(Comvalm.getNombreClase()))==null)
                    return;
                cm= (gnu.chu.anjelica.almacen.Comvalm) prog;
            }
            else
            {
                 cm= new gnu.chu.anjelica.almacen.Comvalm(menu,EU);
                 menu.lanzaEjecutable(cm);
            }
            cm.setFechaInicial(feciniE.getDate());
            cm.setFechaFinal(fecfinE.getDate());
            cm.setFechaInventario(feulinE.getDate());
            cm.setProCodi(jt.getValorInt(0));
            cm.ejecutaConsulta();
            if (jf!=null)
                jf.gestor.ir(cm);
        } catch (ParseException ex)
        {
            Error("Error al llamar programa consulta de Consulta mvtros", ex);
        }
  }
  public void setCliente(int cliCodi)
  {
      cli_codiE.setValorInt(cliCodi);
  }
  
  public void setFechaInicial(java.util.Date fecInicio)
  {
      feciniE.setDate(fecInicio);
  }
  
  public void setFechaFinal(java.util.Date fecFinal)
  {
      fecfinE.setDate(fecFinal);
  }
  public void setIncluirVert(boolean incTodasReg)
  {
      opIncVertE.setSelected(incTodasReg);
  }
  public void ejecutaConsulta()
  {
      Baceptar_actionPerformed();
  }
  /**
   * 
   * @return Nombre clase
   */
  public static String getNombreClase()
  {
      return "gnu.chu.anjelica.margenes.Clmarzona";
  }
  void  Baceptar_actionPerformed()
  {
//      System.out.println("Ancho: "+this.getSize().width+" Alto: "+this.getSize().height);
    if (feciniE.getError() || feciniE.isNull())
    {
      mensajeErr("Fecha Inicial NO valida");
      feciniE.requestFocus();
      return;
    }

    if (fecfinE.getError() || fecfinE.isNull() )
    {
      mensajeErr("Fecha Final NO valida");
      fecfinE.requestFocus();
      return;
    }
    try {
    if (!emp_codiE.controla())
    {
        mensajeErr(emp_codiE.getMsgError());
        return;
    }
    if (emp_codiE.getValorInt()!=0 && !sbe_codiE.controla())
    {
        mensajeErr("SubEmpresa NO valida");
        return;
    }
    } catch (SQLException k)
    {
        Error("Error al controlar condiciones de consulta",k);
        return;
    }
    cancelaConsulta=false;
    mensaje("");
    new miThread("")
    {
            @Override
      public void run()
      {
        msgEspere("Buscando Datos ...");
        popEspere_BCancelarSetEnabled(true);
        this.setPriority(Thread.MAX_PRIORITY-1);
        jt.panelG.setVisible(false);
        llenaGrid();
        jt.panelG.setVisible(true);
        resetMsgEspere();
      }
    };
  }
  private void resetAcumFam()
  {
    kgFam=0;impFam=0;ganaFam=0;
    iCoFa=0;kCoFa=0;
  }
  void llenaGrid()
  {
//    double prVen=0;
    int proCodi;
   
    String fecini, fecfin;

    String zonCodi=(zon_codiE.getText().contains("*") ||
         zon_codiE.getText().trim().equals("")) ?null:zon_codiE.getText();
    String repCodi=(rep_codiE.getText().contains("*") ||
         rep_codiE.getText().trim().equals("")) ?null:rep_codiE.getText();
    jt.removeAllDatos();
    jtGru.removeAllDatos();
    proCod=pro_codiE.isNull()?null:pro_codiE.getText();
    htArt.clear();
    try {
      fecini = feciniE.getText();
      fecfin = fecfinE.getText();
      kgTot=0;impTot=0;ganaTot=0;
      iCoTo=0;kCoTo=0;
      resetAcumFam();
      s = "select f.fpr_codi,p.pro_codi,P.PRO_NOMB," +
                " sum(avl_canti) as avl_canti ,sum(avl_prbase*avl_canti) as avl_prbase "
                + " from v_famipro f, v_articulo p,v_albavec  c,v_albavel l,clientes cl "
                + " where c.avc_fecalb >= TO_DATE('" + fecini + "','dd-MM-yyyy') "
                + " and c.avc_fecalb <=  TO_DATE('" + fecfin + "','dd-MM-yyyy') "
                + " and c.avc_serie >= 'A' AND c.avc_serie <='C' "
                + " and l.emp_codi = c.emp_codi  and c.avc_ano = l.avc_ano  "
                + " and l.avc_serie = c.avc_serie "
                + " and c.avc_nume = l.avc_nume "
                + " and cl.cli_codi = c.cli_codi "
                + " and f.fpr_codi = p.fam_codi "
                + " and p.pro_codi = l.pro_codi "
                + (rut_codiE.getText().equals("")?"": " and cl.rut_codi = '"+rut_codiE.getText()+"'")
                + (zonCodi == null ? "" : " AND cl.zon_codi = '" + zonCodi + "'")
                + (repCodi == null ? "" : " and "+
                (opIgnRepAlb.isSelected()?" rep_codi ": " avc_repres ") + "= '"+repCodi + "'")
                + (cli_codiE.isNull() ? "" : " AND cl.cli_codi = '" + cli_codiE.getValorInt() + "'")
                + (emp_codiE.getValorInt()==0?"": " and c.emp_codi = "+emp_codiE.getValorInt())
                + (sbe_codiE.getValorInt()==0?"": " and cl.sbe_codi = "+sbe_codiE.getValorInt())
                + " and p.pro_tiplot='V' "
                + (proCod != null ? "and l.pro_codi = " + proCod : "")
                + " group by f.fpr_codi,p.pro_codi,P.PRO_NOMB,pro_tiplot ";
        s+= " UNION"
            + " SELECT f.fpr_codi,p.pro_codi,P.PRO_NOMB, 0 as avl_canti, "
            + "  0  as avl_prbase "
            + " from v_famipro f, v_articulo p,v_regstock as r  "
            + " where r.rgs_fecha >= TO_DATE('" + fecini + "','dd-MM-yyyy') "
            + " and r.rgs_fecha <=  TO_DATE('" + fecfin + "','dd-MM-yyyy') "
            + " and tir_codi != " + tirCodInv
//            + " and tir_codi not in ("+tirCodVert+")"
            + " and p.pro_tiplot='V' "
            + (proCod != null ? "and r.pro_codi = " + proCod : "")          
            + " and p.pro_codi = r.pro_codi "
            + " and f.fpr_codi = p.fam_codi ";
      s+=" order by 1,2 ";
//    debug ("s: "+s);
    if (!dtVen.select(s))
    {
      mensajeErr("No encontradas ventas para estos Criterios");
      this.setEnabled(true);
      feciniE.requestFocus();
      return;
    }
    if (zonCodi != null)
      zonCodi = zonCodi.replace('*', '.');

    proCodi=dtVen.getInt("pro_codi");
    String proNomb=dtVen.getString("pro_nomb");
 //   String proTipLot=dtVen.getString("pro_tiplot");
    if (swCreateTableTemp)
    {
        s="CREATE  TEMP  TABLE clmarzona (fpr_codi int, "+
                " mar_kilven float, "+
                " mar_impven float,"+
                " mar_gananc float,"+
                " mar_kilcom float, "+
                " mar_impcom float )";
        dtAdd.executeUpdate(s);
        swCreateTableTemp=false;
    }
    dtAdd.executeUpdate("DELETE FROM clmarzona");
    dtAdd.commit();
    fprCodi=dtVen.getInt("fpr_codi");
    double kgPro=0,impPro=0;
    double impGana;
    
    fecinv=feulinE.getText();
    mvtosAlm.setIgnoraRegular(! opIncVertE.isSelected());
    mvtosAlm.setEmpresa(emp_codiE.getValorInt());
    mvtosAlm.setSbeCodi(sbe_codiE.getValorInt());
    mvtosAlm.setCliente(cli_codiE.getValorInt());
    mvtosAlm.setRutCodi(rut_codiE.isNull()?(String) null:rut_codiE.getText());
    mvtosAlm.iniciarMvtos(feulinE.getDate(), feciniE.getDate(),fecfinE.getDate(),dtCon1);
//    mvtosAlm.setIgnoraRepAlb(opIgnRepAlb.isSelected());
    mvtosAlm.resetMensajes();
    do
    {
          if (cancelaConsulta) {
            mensaje("Consulta Cancelada !!");
            this.setEnabled(true);
            feciniE.requestFocus();
            return;
        }
        if (proCodi!=dtVen.getInt("pro_codi"))
        {
           impGana = getImpGana(proCodi, fecini, fecfin, zonCodi,repCodi);
         
           if (impGana!=0 || impPro!=0)
            verDatos(""+proCodi, proNomb, kgPro, impPro, impGana,kgCom,impCom,jt);
           proNomb=dtVen.getString("pro_nomb");
           proCodi=dtVen.getInt("pro_codi");
           //proTipLot=dtVen.getString("pro_tiplot");
           kgFam+=kgPro;
           impFam+=impPro;
           iCoFa+=impCom;
           kCoFa+=kgCom;
           ganaFam+=impGana;
           kgPro=0;
           impPro=0;
           impCom=0;
           kgCom=0;
        }
        if (fprCodi != dtVen.getInt("fpr_codi"))
        { // Roto la familia del Producto. Muestro la agrupacion con los totales.
          guardaFam();
          fprCodi=dtVen.getInt("fpr_codi");
        }
        setMensajePopEspere("Procesando producto: "+proCodi,false);
        
        kgPro+=dtVen.getDouble("avl_canti");
        impPro+=dtVen.getDouble("avl_prbase",true); 
    } while (dtVen.next());
    impGana = getImpGana(proCodi, fecini, fecfin, zonCodi,repCodi);
    verDatos(""+proCodi, proNomb, kgPro, impPro, impGana,kgCom,impCom,jt);

    kgFam+=kgPro;
    impFam+=impPro;
    iCoFa+=impCom;
    kCoFa+=kgCom;
    ganaFam+=impGana;
    guardaFam();

    if (opIncComent.isSelected())
    { // Incluir productos no de venta.
        s="select l.pro_codi,l.pro_nomb,sum(l.avl_canti) as canti,"+
            " sum(l.avl_canti* l.avl_prbase) as importe  from "+
            " v_albavec as c, v_albavel as l, v_articulo as p,clientes cl "+
            " where c.emp_codi= l.emp_codi "+
            " and c.avc_ano=l.avc_ano "+
            " and c.avc_serie=l.avc_serie "+
            " and c.avc_nume=l.avc_nume "+
            " and cl.cli_codi = c.cli_codi "+
            " and c.avc_fecalb  >= TO_DATE('" + fecini + "','dd-MM-yyyy') "+
            " and c.avc_fecalb <=  TO_DATE('" + fecfin + "','dd-MM-yyyy') "+
            (zonCodi == null ? "" : " AND cl.zon_codi = '" + zonCodi + "'") +
            (repCodi == null ? "" : " and "+
             (opIgnRepAlb.isSelected()?" rep_codi ": " avc_repres ") + "= '"+repCodi + "'")+
             (emp_codiE.getValorInt()==0?"": " and c.emp_codi = "+emp_codiE.getValorInt()) +
            (sbe_codiE.getValorInt()==0?"": " and c.sbe_codi = "+ sbe_codiE.getValorInt()) +
            (proCod != null ? "and l.pro_codi = " + proCod : "") +
            " and p.pro_codi = l.pro_codi "+
            " and p.pro_tiplot!='V' "+
            " group by l.pro_codi,l.pro_nomb";
        if (dtCon1.select(s))
        {
         do
         {
            verDatos(dtCon1.getString("pro_codi"),dtCon1.getString("pro_nomb"),
                    dtCon1.getDouble("canti"), dtCon1.getDouble("importe"),
                     dtCon1.getDouble("importe"), 0, 0,jt);
            kgTot+= dtCon1.getDouble("canti");
            impTot+=dtCon1.getDouble("importe");
            ganaTot+=dtCon1.getDouble("importe");
         } while (dtCon1.next());
        }
    }
    kgsVenE.setValorDec(kgTot);
    impVentaE.setValorDec(impTot);
    pmVenE.setValorDec(impTot==0?0:impTot/kgTot);

    impGanaE.setValorDec(ganaTot);
    ekGanaE.setValorDec(kgTot==0?0:ganaTot/kgTot);
    pmComE.setValorDec(kCoTo==0?0:iCoTo/kCoTo);
    verDatos("TOTAL","**TOTAL GENERAL**",kgTot,impTot,ganaTot,kCoTo,iCoTo,jt);
    s="SELECT g.agr_codi,agp_nomb,sum(mar_kilven) as mar_kilven,"
            + " sum(mar_impven) as mar_impven, "
            + " sum(mar_gananc) as mar_gananc, "
            + " sum(mar_kilcom) as mar_kilcom, "
            + " sum(mar_impcom) as mar_impcom "
            + " from v_agupro as g,grufampro as gf,"
            + " clmarzona as m where m.fpr_codi = gf.fpr_codi "
            + " and g.agr_codi = gf.agr_codi "
            + " group by g.agr_codi,g.agp_nomb "
            + " order by g.agr_codi ";
    if (dtCon1.select(s))
    {
        do
        {
             verDatos(dtCon1.getString("agr_codi"),dtCon1.getString("agp_nomb"),
                        dtCon1.getDouble("mar_kilven"), dtCon1.getDouble("mar_impven"),
                         dtCon1.getDouble("mar_gananc"),
                         dtCon1.getDouble("mar_kilcom"),dtCon1.getDouble("mar_impcom"),jtGru);
        } while (dtCon1.next());
    }
    incindenE.setText(mvtosAlm.getMsgLog());
     
    stockE.setText( mvtosAlm.getMsgStock());
    arbolProdPanel.setValoresArticulos(htArt);
    arbolProdPanel.verDatosArbol();
    } catch (Exception k)
    {
      Error("Error al buscar Albaranes de Venta",k);
      return;
    }
    mensaje("");
    mensajeErr("Consulta ... Realizada");
    this.setEnabled(true);
  }
  /**
   * Guarda datos de la familia y actualiza acumulados
   * @throws SQLException
   */
  void guardaFam() throws SQLException
  {
      dtAdd.addNew("clmarzona");
      dtAdd.setDato("fpr_codi",fprCodi);
      dtAdd.setDato("mar_kilven",kgFam);
      dtAdd.setDato("mar_impven",impFam);
      dtAdd.setDato("mar_gananc",ganaFam);
      dtAdd.setDato("mar_kilcom",kCoTo);
      dtAdd.setDato("mar_impcom",iCoTo);
      dtAdd.update();
      iCoTo+=iCoFa;
      kCoTo+=kCoFa;
      kgTot+=kgFam;
      impTot+=impFam;
      ganaTot+=ganaFam;
      resetAcumFam();
  }
  void verDatos(String proCodi,String proNomb,double kgPro,double impPro,double impGana,double kCom,
          double iCom,Cgrid jt)
  {

    ArrayList v=new ArrayList();
    v.add(""+proCodi);
    v.add(proNomb);
    v.add(Formatear.format(kgPro,"----,--9.99")); // Kilos Venta
    v.add(Formatear.format(impPro,"----,--9.99")); //  Imp. Venta
    v.add(Formatear.format(kgPro==0?0:impPro/kgPro,"---9.99")); // Pr. Medio Venta.
    v.add(Formatear.format((iCom==0||kCom==0?0:iCom/kCom),"---9.99")); // 5
    v.add(Formatear.format(kgPro==0?0:impGana/kgPro,"--,--9.99")); // 6
    v.add(Formatear.format(impGana,"---,--9.99")); // 7
    jt.addLinea(v);
    try {
        if (jt.getNombre().startsWith("P"))
        {
            DatosProd dtPro=new DatosProd(Integer.parseInt(proCodi));
            dtPro.setKilos(kgPro);
            dtPro.setImporte(impPro);
            dtPro.setUnidades(0);
            dtPro.setImpGanan(impGana);
            htArt.put(Integer.parseInt(proCodi), dtPro);
        }
    } catch (NumberFormatException k) {}
  
  }

  double getImpGana(int proCodi, String fecini,String fecfin,String zona,String repr) throws Exception
  {    
    mvtosAlm.calculaMvtos( proCodi,dtCon1,dtStat, zona, repr);
    kgCom = mvtosAlm.getKilosEntrada();
    impCom= mvtosAlm.getImporteEntrada();
    return mvtosAlm.getImpGana();
  }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        Pprinc = new gnu.chu.controles.CPanel();
        Pcabe = new gnu.chu.controles.CPanel();
        cLabel5 = new gnu.chu.controles.CLabel();
        cLabel6 = new gnu.chu.controles.CLabel();
        cLabel16 = new gnu.chu.controles.CLabel();
        zon_codiE = new gnu.chu.controles.CLinkBox();
        cLabel1 = new gnu.chu.controles.CLabel();
        pro_codiE = new gnu.chu.camposdb.proPanel();
        cLabel2 = new gnu.chu.controles.CLabel();
        feulinE = new gnu.chu.controles.CComboBox();
        cLabel3 = new gnu.chu.controles.CLabel();
        emp_codiE = new gnu.chu.camposdb.empPanel();
        cLabel4 = new gnu.chu.controles.CLabel();
        sbe_codiE = new gnu.chu.camposdb.sbePanel();
        Baceptar = new gnu.chu.controles.CButton();
        cLabel17 = new gnu.chu.controles.CLabel();
        rep_codiE = new gnu.chu.controles.CLinkBox();
        opIncVertE = new gnu.chu.controles.CCheckBox();
        opIncComent = new gnu.chu.controles.CCheckBox();
        feciniE = new gnu.chu.camposdb.fechaCal();
        fecfinE = new gnu.chu.camposdb.fechaCal();
        cLabel19 = new gnu.chu.controles.CLabel();
        cli_codiE = new gnu.chu.camposdb.cliPanel();
        cLabel20 = new gnu.chu.controles.CLabel();
        rut_codiE = new gnu.chu.controles.CLinkBox();
        opIgnRepAlb = new gnu.chu.controles.CCheckBox();
        PPie = new gnu.chu.controles.CPanel();
        cLabel7 = new gnu.chu.controles.CLabel();
        cLabel8 = new gnu.chu.controles.CLabel();
        kgsVenE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----,--9.99");
        cLabel9 = new gnu.chu.controles.CLabel();
        impVentaE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--,---,--9.99");
        cLabel10 = new gnu.chu.controles.CLabel();
        pmVenE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---9.99");
        cLabel11 = new gnu.chu.controles.CLabel();
        pmComE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---9.99");
        cLabel12 = new gnu.chu.controles.CLabel();
        impGanaE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--,---,--9.99");
        cLabel13 = new gnu.chu.controles.CLabel();
        ekGanaE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---9.99");
        cTabbedPane1 = new gnu.chu.controles.CTabbedPane();
        jt = new gnu.chu.controles.Cgrid(8);
        cPanel1 = new gnu.chu.controles.CPanel();
        cScrollPane1 = new gnu.chu.controles.CScrollPane();
        incindenE = new gnu.chu.controles.CTextArea();
        cLabel14 = new gnu.chu.controles.CLabel();
        cLabel15 = new gnu.chu.controles.CLabel();
        cScrollPane2 = new gnu.chu.controles.CScrollPane();
        stockE = new gnu.chu.controles.CTextArea();
        Pfami = new gnu.chu.controles.CPanel();
        jtGru = new gnu.chu.controles.Cgrid(8);
        {
            Vector v=new Vector();
            v.addElement("Grupo"); // 0
            v.addElement("Descripcion"); // 1
            v.addElement("Kil.Vent"); // 2
            v.addElement("Imp.Vent"); // 3
            v.addElement("Pr.Me.Ve");  // 4
            v.addElement("Pr.Me.Co"); // 5
            v.addElement("Gana/Kg"); // 6
            v.addElement("Ganancia"); // 7
            jtGru.setCabecera(v);
            jtGru.setAjustarGrid(true);
            jtGru.setAlinearColumna(new int[]{0,0,2,2,2,2,2,2});
            jtGru.setAnchoColumna(new int[]{40,100,40,45,40,40,40,45});
        }
        cPanel2 = new gnu.chu.controles.CPanel();
        arbolProdPanel = new gnu.chu.anjelica.pad.ArbolProdPanel();

        Pprinc.setLayout(new java.awt.GridBagLayout());

        Pcabe.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        Pcabe.setMaximumSize(new java.awt.Dimension(650, 108));
        Pcabe.setMinimumSize(new java.awt.Dimension(650, 108));
        Pcabe.setPreferredSize(new java.awt.Dimension(650, 108));
        Pcabe.setLayout(null);

        cLabel5.setText("De Fecha");
        cLabel5.setPreferredSize(new java.awt.Dimension(52, 18));
        Pcabe.add(cLabel5);
        cLabel5.setBounds(240, 2, 52, 17);

        cLabel6.setText("A Fecha");
        cLabel6.setPreferredSize(new java.awt.Dimension(44, 18));
        Pcabe.add(cLabel6);
        cLabel6.setBounds(390, 2, 44, 17);

        cLabel16.setText("Ruta");
        cLabel16.setPreferredSize(new java.awt.Dimension(60, 18));
        Pcabe.add(cLabel16);
        cLabel16.setBounds(80, 87, 40, 17);

        zon_codiE.setAncTexto(30);
        zon_codiE.setPreferredSize(new java.awt.Dimension(92, 18));
        Pcabe.add(zon_codiE);
        zon_codiE.setBounds(50, 67, 210, 18);

        cLabel1.setText("Cliente");
        cLabel1.setPreferredSize(new java.awt.Dimension(50, 18));
        Pcabe.add(cLabel1);
        cLabel1.setBounds(10, 47, 50, 18);

        pro_codiE.setPreferredSize(new java.awt.Dimension(271, 18));
        Pcabe.add(pro_codiE);
        pro_codiE.setBounds(66, 25, 330, 18);

        cLabel2.setText("Fecha Ult. Inv.");
        cLabel2.setPreferredSize(new java.awt.Dimension(74, 18));
        Pcabe.add(cLabel2);
        cLabel2.setBounds(430, 25, 90, 18);

        feulinE.setPreferredSize(new java.awt.Dimension(28, 18));
        Pcabe.add(feulinE);
        feulinE.setBounds(520, 25, 120, 18);

        cLabel3.setText("Empresa");
        cLabel3.setPreferredSize(new java.awt.Dimension(49, 18));
        Pcabe.add(cLabel3);
        cLabel3.setBounds(12, 2, 49, 17);

        emp_codiE.setPreferredSize(new java.awt.Dimension(29, 18));
        Pcabe.add(emp_codiE);
        emp_codiE.setBounds(65, 2, 41, 17);

        cLabel4.setText("Delegacion");
        cLabel4.setPreferredSize(new java.awt.Dimension(70, 18));
        Pcabe.add(cLabel4);
        cLabel4.setBounds(124, 2, 70, 17);

        sbe_codiE.setToolTipText("Tratar solo Ventas de Subseccion introducida");
        Pcabe.add(sbe_codiE);
        sbe_codiE.setBounds(198, 2, 38, 17);

        Baceptar.setText("Aceptar");
        Pcabe.add(Baceptar);
        Baceptar.setBounds(540, 70, 100, 29);

        cLabel17.setText("Repres");
        cLabel17.setPreferredSize(new java.awt.Dimension(60, 18));
        Pcabe.add(cLabel17);
        cLabel17.setBounds(270, 67, 40, 18);

        rep_codiE.setAncTexto(30);
        rep_codiE.setPreferredSize(new java.awt.Dimension(92, 18));
        Pcabe.add(rep_codiE);
        rep_codiE.setBounds(310, 67, 210, 18);

        opIncVertE.setSelected(true);
        opIncVertE.setText("Incl. todas Reg.");
        opIncVertE.setToolTipText("Incluir Regularizaciones Genericas");
        Pcabe.add(opIncVertE);
        opIncVertE.setBounds(430, 87, 110, 18);
        opIncVertE.getAccessibleContext().setAccessibleDescription("Incluir Regularizaciones");

        opIncComent.setText("Inc.  Comentario");
        opIncComent.setToolTipText("Incluir productos tipo comentario");
        Pcabe.add(opIncComent);
        opIncComent.setBounds(532, 2, 110, 17);
        Pcabe.add(feciniE);
        feciniE.setBounds(292, 2, 90, 17);
        Pcabe.add(fecfinE);
        fecfinE.setBounds(440, 2, 92, 17);

        cLabel19.setText("Producto");
        cLabel19.setPreferredSize(new java.awt.Dimension(50, 18));
        Pcabe.add(cLabel19);
        cLabel19.setBounds(12, 25, 50, 18);
        Pcabe.add(cli_codiE);
        cli_codiE.setBounds(69, 47, 430, 18);

        cLabel20.setText("Zona");
        cLabel20.setPreferredSize(new java.awt.Dimension(60, 18));
        Pcabe.add(cLabel20);
        cLabel20.setBounds(10, 67, 40, 18);

        rut_codiE.setAncTexto(30);
        rut_codiE.setPreferredSize(new java.awt.Dimension(92, 18));
        Pcabe.add(rut_codiE);
        rut_codiE.setBounds(120, 87, 270, 17);

        opIgnRepAlb.setSelected(true);
        opIgnRepAlb.setText("Ignorar Rep. Alb");
        opIgnRepAlb.setToolTipText("Ignorar representante de Albaran");
        Pcabe.add(opIgnRepAlb);
        opIgnRepAlb.setBounds(520, 47, 110, 18);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        Pprinc.add(Pcabe, gridBagConstraints);

        PPie.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        PPie.setMaximumSize(new java.awt.Dimension(500, 50));
        PPie.setMinimumSize(new java.awt.Dimension(500, 50));
        PPie.setPreferredSize(new java.awt.Dimension(500, 50));

        cLabel7.setBackground(java.awt.Color.blue);
        cLabel7.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        cLabel7.setForeground(java.awt.Color.white);
        cLabel7.setText("Venta");
        cLabel7.setOpaque(true);

        cLabel8.setText("Kilos");
        cLabel8.setPreferredSize(new java.awt.Dimension(27, 18));

        kgsVenE.setEditable(false);
        kgsVenE.setPreferredSize(new java.awt.Dimension(2, 18));

        cLabel9.setText("Importe");
        cLabel9.setPreferredSize(new java.awt.Dimension(27, 18));

        impVentaE.setEditable(false);
        impVentaE.setPreferredSize(new java.awt.Dimension(2, 18));

        cLabel10.setText("Pr.Medio");
        cLabel10.setPreferredSize(new java.awt.Dimension(27, 18));

        pmVenE.setEditable(false);
        pmVenE.setPreferredSize(new java.awt.Dimension(2, 18));

        cLabel11.setText("Pr.Medio Compra ");
        cLabel11.setPreferredSize(new java.awt.Dimension(27, 18));

        pmComE.setEditable(false);
        pmComE.setPreferredSize(new java.awt.Dimension(2, 18));

        cLabel12.setText("Imp.Gana");
        cLabel12.setPreferredSize(new java.awt.Dimension(27, 18));

        impGanaE.setEditable(false);
        impGanaE.setPreferredSize(new java.awt.Dimension(2, 18));

        cLabel13.setText("Ganacia Kg");
        cLabel13.setPreferredSize(new java.awt.Dimension(27, 18));

        ekGanaE.setEditable(false);
        ekGanaE.setPreferredSize(new java.awt.Dimension(2, 18));

        org.jdesktop.layout.GroupLayout PPieLayout = new org.jdesktop.layout.GroupLayout(PPie);
        PPie.setLayout(PPieLayout);
        PPieLayout.setHorizontalGroup(
            PPieLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(PPieLayout.createSequentialGroup()
                .add(10, 10, 10)
                .add(PPieLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(PPieLayout.createSequentialGroup()
                        .add(cLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(10, 10, 10)
                        .add(cLabel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 36, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(4, 4, 4)
                        .add(kgsVenE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 82, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(4, 4, 4)
                        .add(cLabel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 58, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(4, 4, 4)
                        .add(impVentaE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 102, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(10, 10, 10)
                        .add(cLabel10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 58, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(pmVenE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 56, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(PPieLayout.createSequentialGroup()
                        .add(cLabel11, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 104, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(4, 4, 4)
                        .add(pmComE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 56, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(10, 10, 10)
                        .add(cLabel12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 58, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(4, 4, 4)
                        .add(impGanaE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 102, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(10, 10, 10)
                        .add(cLabel13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 64, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(10, 10, 10)
                        .add(ekGanaE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 56, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
        );
        PPieLayout.setVerticalGroup(
            PPieLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(PPieLayout.createSequentialGroup()
                .add(PPieLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(cLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cLabel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cLabel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cLabel10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(PPieLayout.createSequentialGroup()
                        .add(1, 1, 1)
                        .add(PPieLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(kgsVenE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(impVentaE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(pmVenE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .add(8, 8, 8)
                .add(PPieLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(cLabel11, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cLabel12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cLabel13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(PPieLayout.createSequentialGroup()
                        .add(1, 1, 1)
                        .add(PPieLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(pmComE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(impGanaE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(ekGanaE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        Pprinc.add(PPie, gridBagConstraints);

        cTabbedPane1.setMaximumSize(new java.awt.Dimension(100, 100));
        cTabbedPane1.setMinimumSize(new java.awt.Dimension(100, 100));

        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jt.setPreferredSize(new java.awt.Dimension(100, 100));
        Vector v=new Vector();
        v.addElement("Producto"); // 0
        v.addElement("Descripcion"); // 1
        v.addElement("Kil.Vent"); // 2
        v.addElement("Imp.Vent"); // 3
        v.addElement("Pr.Me.Ve");  // 4
        v.addElement("Pr.Me.Co"); // 5
        v.addElement("Gana/Kg"); // 6
        v.addElement("Ganancia"); // 7
        jt.setCabecera(v);
        jt.setAjustarGrid(true);
        jt.setAlinearColumna(new int[]{0,0,2,2,2,2,2,2});
        jt.setAnchoColumna(new int[]{40,100,40,45,40,40,40,45});

        org.jdesktop.layout.GroupLayout jtLayout = new org.jdesktop.layout.GroupLayout(jt);
        jt.setLayout(jtLayout);
        jtLayout.setHorizontalGroup(
            jtLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 681, Short.MAX_VALUE)
        );
        jtLayout.setVerticalGroup(
            jtLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 230, Short.MAX_VALUE)
        );

        cTabbedPane1.addTab("Datos", jt);

        incindenE.setColumns(20);
        incindenE.setRows(5);
        cScrollPane1.setViewportView(incindenE);

        cLabel14.setText("Incidencias");

        cLabel15.setText("Problemas Stock");

        stockE.setColumns(20);
        stockE.setRows(5);
        cScrollPane2.setViewportView(stockE);

        org.jdesktop.layout.GroupLayout cPanel1Layout = new org.jdesktop.layout.GroupLayout(cPanel1);
        cPanel1.setLayout(cPanel1Layout);
        cPanel1Layout.setHorizontalGroup(
            cPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, cScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 683, Short.MAX_VALUE)
            .add(cScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 683, Short.MAX_VALUE)
            .add(cPanel1Layout.createSequentialGroup()
                .add(cPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(cLabel14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cLabel15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        cPanel1Layout.setVerticalGroup(
            cPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(cPanel1Layout.createSequentialGroup()
                .add(cLabel14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(2, 2, 2)
                .add(cScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 119, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(2, 2, 2)
                .add(cLabel15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(2, 2, 2)
                .add(cScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 97, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cTabbedPane1.addTab("Comentarios", cPanel1);

        Pfami.setLayout(new java.awt.BorderLayout());

        jtGru.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        org.jdesktop.layout.GroupLayout jtGruLayout = new org.jdesktop.layout.GroupLayout(jtGru);
        jtGru.setLayout(jtGruLayout);
        jtGruLayout.setHorizontalGroup(
            jtGruLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 681, Short.MAX_VALUE)
        );
        jtGruLayout.setVerticalGroup(
            jtGruLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 230, Short.MAX_VALUE)
        );

        Pfami.add(jtGru, java.awt.BorderLayout.CENTER);

        cTabbedPane1.addTab("Grupos", Pfami);

        cPanel2.setLayout(new java.awt.BorderLayout());
        cPanel2.add(arbolProdPanel, java.awt.BorderLayout.CENTER);

        cTabbedPane1.addTab("Arbol Familias", cPanel2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 2.0;
        Pprinc.add(cTabbedPane1, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CPanel PPie;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Pfami;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.anjelica.pad.ArbolProdPanel arbolProdPanel;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel10;
    private gnu.chu.controles.CLabel cLabel11;
    private gnu.chu.controles.CLabel cLabel12;
    private gnu.chu.controles.CLabel cLabel13;
    private gnu.chu.controles.CLabel cLabel14;
    private gnu.chu.controles.CLabel cLabel15;
    private gnu.chu.controles.CLabel cLabel16;
    private gnu.chu.controles.CLabel cLabel17;
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
    private gnu.chu.controles.CScrollPane cScrollPane1;
    private gnu.chu.controles.CScrollPane cScrollPane2;
    private gnu.chu.controles.CTabbedPane cTabbedPane1;
    private gnu.chu.camposdb.cliPanel cli_codiE;
    private gnu.chu.controles.CTextField ekGanaE;
    private gnu.chu.camposdb.empPanel emp_codiE;
    private gnu.chu.camposdb.fechaCal fecfinE;
    private gnu.chu.camposdb.fechaCal feciniE;
    private gnu.chu.controles.CComboBox feulinE;
    private gnu.chu.controles.CTextField impGanaE;
    private gnu.chu.controles.CTextField impVentaE;
    private gnu.chu.controles.CTextArea incindenE;
    private gnu.chu.controles.Cgrid jt;
    private gnu.chu.controles.Cgrid jtGru;
    private gnu.chu.controles.CTextField kgsVenE;
    private gnu.chu.controles.CCheckBox opIgnRepAlb;
    private gnu.chu.controles.CCheckBox opIncComent;
    private gnu.chu.controles.CCheckBox opIncVertE;
    private gnu.chu.controles.CTextField pmComE;
    private gnu.chu.controles.CTextField pmVenE;
    private gnu.chu.camposdb.proPanel pro_codiE;
    private gnu.chu.controles.CLinkBox rep_codiE;
    private gnu.chu.controles.CLinkBox rut_codiE;
    private gnu.chu.camposdb.sbePanel sbe_codiE;
    private gnu.chu.controles.CTextArea stockE;
    private gnu.chu.controles.CLinkBox zon_codiE;
    // End of variables declaration//GEN-END:variables

}
