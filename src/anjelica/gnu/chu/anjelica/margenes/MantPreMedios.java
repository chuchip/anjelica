package gnu.chu.anjelica.margenes;
/**
 *
 * <p>Titulo: MantPreMedios </p>
 * <p>Descripción: Mantenimiento Precios Medios</p>
 * <p>Copyright: Copyright (c) 2005-2018
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los terminos de la Licencia Pública General de GNU según es publicada por
 *  la Free Software Foundation, bien de la versión 2 de dicha Licencia
 *  o bien (según su elección) de cualquier versión posterior.
 *  Este programa se distribuye con la esperanza de que sea útil,
 *  pero SIN NINGUNA GARANTIA, incluso sin la garantía MERCANTIL implícita
 *  o sin garantizar la CONVENIENCIA PARA UN PROPOSITO PARTICULAR.
 *  Véase la Licencia Pública General de GNU para más detalles.
 *  Debería haber recibidof una copia de la Licencia Pública General junto con este programa.
 *  Si no ha sido así, escriba a la Free Software Foundation, Inc.,
 *  en 675 Mass Ave, Cambridge, MA 02139, EEUU.
 * </p>
 * @author chuchiP
 *
 */ 
import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.despiece.pdprvades;
import gnu.chu.anjelica.pad.MantArticulos;
import gnu.chu.controles.StatusBar;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.ventana;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

public class MantPreMedios extends ventana
{   
  double importeVenta;
  double importeDesp;
  double kilosEntra;
   private int TIDE_AUTOCLASI=108; 
   private int TIDE_108A109=401;
   private final String TIPODESP_BOLA="275 and 280 ";
   Hashtable<Integer,Double> htBolas = new Hashtable();
   Hashtable<Integer,Integer[]> htLomos = new Hashtable();
   Hashtable<Integer,Boolean> htLomCom = new Hashtable();
   String codBolas;
   double kilDespT,impDespT,impCalcT;
   
//   final double PRECIO_DESP=2.8;
   public MantPreMedios(EntornoUsuario eu, Principal p)
  {
    this(eu,p,null);
  }

  public MantPreMedios(EntornoUsuario eu, Principal p, Hashtable ht)
  {
      EU = eu;
      vl = p.panel1;
      jf = p;
      eje = true;

      setTitulo("Mant. Precios Medios");

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

    public MantPreMedios(gnu.chu.anjelica.menu p, EntornoUsuario eu)
    {

      EU = eu;
      vl = p.getLayeredPane();
       setTitulo("Mant. Precios Medios");
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
      this.setVersion("2018-05-03" );
      statusBar = new StatusBar(this);
      
      iniciarFrame();
      this.getContentPane().add(statusBar, BorderLayout.SOUTH);
      initComponents();
      
      this.setSize(new Dimension(582,522));
      conecta();
      

    }
     @Override
    public void iniciarVentana() throws Exception
    {
       
        TIDE_AUTOCLASI= EU.getValorParam("tipdespclasi", TIDE_AUTOCLASI);
        TIDE_108A109= EU.getValorParam("tipdesp108A109", TIDE_108A109);
        htBolas.put(40201, 2.55);
        htBolas.put(40202, 2.65);
        htBolas.put(40203, 2.75);
        htBolas.put(40205, 2.55);
        htBolas.put(40225, 2.55);
        htBolas.put(10201, 2.55);
        htBolas.put(10202, 2.65);
        htBolas.put(10203, 2.75);
        htBolas.put(10205, 2.55);
        htBolas.put(10225, 2.55);
        htLomos.put(10994,new Integer[]{10994,10995});
        htLomos.put(10904,new Integer[]{10904,10905});
//        htLomos.put(10994,new Integer[]{10994,10995});
        htLomos.put(10903,new Integer[]{10903});
        htLomos.put(10993,new Integer[]{10993});
        htLomos.put(10902,new Integer[]{10902});
        htLomos.put(10992,new Integer[]{10992});
        htLomos.put(10901,new Integer[]{10901});
        htLomos.put(10991,new Integer[]{10991});
        htLomos.put(10906,new Integer[]{10906});
        htLomos.put(10931,new Integer[]{10931});
        htLomos.put(10932,new Integer[]{10932});
        htLomos.put(10933,new Integer[]{10933});
        
        htLomCom.put(10904,true);
        htLomCom.put(10994,false);
        htLomCom.put(10903,true);
        htLomCom.put(10993,false);
        htLomCom.put(10902,true);
        htLomCom.put(10992,false);
        htLomCom.put(10901,true);
        htLomCom.put(10991,false);
        htLomCom.put(10906,true);
        htLomCom.put(10931,true);
        htLomCom.put(10932,true);
        htLomCom.put(10933,true);
        eje_numeE.setValorDec(EU.ejercicio);
        activarEventos();
    }
    void activarEventos()
    {
         cta_semanaE.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e) {
                if (cta_semanaE.hasCambio())
                {
                    cta_semanaE.resetCambio();
                    try {
                        ponFechas();
                    } catch ( ParseException  ex)
                    {
                        Error("Error al poner fechas",ex);
                    }
                }
            }
        });
       BirGrid.addActionListener(new ActionListener()
       {
            @Override
             public void actionPerformed(ActionEvent e) {
              if (!checkDatos())
                    return;

              calculaDatos();  
            }
       });
        Brefrescar.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                try
                {
                    if (!checkDatos())
                        return;
                    calculaBolas();
                    calculaBolasAnt();
                    mensajeErr("Calculo de bolas ... refrescado");
                } catch (SQLException | ParseException k)
                {
                    Error("Error al llenar grid bolas", k);
                }
            }
        });
    }
    boolean checkDatos()
    {
        if (tar_feciniE.isNull())
        {
            msgBox("Introduzca fecha Inicial tarifa");
            tar_feciniE.requestFocus();
            return false;
        }
        if (fecStockE.isNull())
        {
            msgBox("Introduzca fecha Stock");
            fecStockE.requestFocus();
            return false;
        }
        if (fecIniComE.isNull())
        {
            msgBox("Introduzca fecha Inicio Compra");
            fecIniComE.requestFocus();
            return false;
        }
        if (fecFinComE.isNull())
        {
            msgBox("Introduzca fecha Final Compra");
            fecFinComE.requestFocus();
            return false;
        }
        if (precioBolaAlemE.getValorDec()==0)
        {
            msgBox("Introduzca Precio Bola Alemana");
            precioBolaAlemE.requestFocus();
            return false;
        }
        if (precioBolaPoloniaE.getValorDec()==0)
        {
            msgBox("Introduzca Precio Bola Polaca");
            precioBolaPoloniaE.requestFocus();
            return false;
        }
        if (precioBolaOtrasE.getValorDec()==0)
        {
            msgBox("Introduzca Precio del resto de bolas");
            precioBolaOtrasE.requestFocus();
            return false;
        }
        return true;
    }
    void calculaDatos()
    {
        try
        {           
           calculaBolas();
           calculaBolasAnt();
           calculaLomos();
        } catch (SQLException | ParseException k)
        {
            Error("Error al Calcular Datos",k);
        }
    }
    void calculaLomos() throws SQLException, ParseException
    { 
        String s;
        jtLomos.removeAllDatos();

        List<Integer> stLomos = Collections.list(htLomos.keys());

        Collections.sort(stLomos);
        Iterator<Integer> it = stLomos.iterator();

        Integer lomo;
        while (it.hasNext())
        {
            lomo = it.next();
            Integer[] codigos = htLomos.get(lomo);
            int codigo1 = codigos[0];
            int codigo2 = codigos[0];
            if (codigos.length > 1)
                codigo2 = codigos[1];
            boolean incCompra = false;// htLomCom.get(lomo);
            boolean incPistolas=opInc1099.isSelected();
            s = "select sum(lci_peso) as kilos from v_coninvent  as i where lci_peso>0 and cci_feccon='" + fecStockE.getFechaDB() + "'"
                + " and (pro_codi=" + codigo1
                + " or pro_codi= " + codigo2 + ")"
                + (incCompra ? " and prp_part not in (select acc_nume from v_albacoc "
                    + "where acc_fecrec>='" + fecIniComE.getFechaDB() + "')" : "")+
                 (incPistolas ? " and not exists (select * from v_despsal as d where deo_fecha >= '"+fecIniComE.getFechaDB()+"' "+
                    " and d.pro_codi = i.pro_codi  "+
                    " and i.prp_ano = d.def_ejelot"+
                    " and i.prp_seri= d.def_serlot "+
                    " and i.prp_part = d.pro_lote "+
                    " and i.prp_indi = d.pro_numind)":""); 
            dtCon1.select(s);
            double costoInv = pdprvades.getPrecioOrigen(dtStat, lomo, Formatear.sumaDiasDate(tar_feciniE.getDate(), -7));
            ArrayList v = new ArrayList();
            v.add(lomo);
            v.add(MantArticulos.getNombProd(lomo, dtStat));
            v.add(dtCon1.getDouble("kilos", true));
            v.add(costoInv);
            v.add(dtCon1.getDouble("kilos", true) * costoInv);
            s = "select sum(acl_canti) as kilos,sum(acl_canti*acl_prcom) as importe from v_albacom"
                + " where  acc_fecrec between '" + fecIniComE.getFechaDB()
                + "' and '" + fecFinComE.getFechaDB() + "'"
                + " and (pro_codi=" + codigo1
                + " or pro_codi= " + codigo2 + ")";

            dtCon1.select(s);
            double kgEntra = dtCon1.getDouble("kilos", true);
            double kilCompra = dtCon1.getDouble("kilos", true);
            double impCompra = dtCon1.getDouble("importe", true);
            s = "select sum(deo_kilos) as kilos, sum(deo_kilos*c.acl_prcom) as importe "
                + "from v_despori as d,v_compras as c where  tid_codi= " + TIDE_AUTOCLASI
                + " and deo_fecha>='" + tar_feciniE.getFechaDB() + "'"
                + " and deo_fecha<='" + fecFinComE.getFechaDB() + "'"
                + " and c.acc_fecrec>='" + tar_feciniE.getFechaDB() + "'"
                + " and d.pro_codi=c.pro_codi "
                + " and d.pro_lote=c.acc_nume "
                + " and d.deo_ejelot=c.acc_ano "
                + " and deo_serlot=c.acc_serie "
                + " and pro_numind = c.acp_numind "
                + " and (c.pro_codi=" + codigo1
                + " or c.pro_codi= " + codigo2 + ")";
            dtCon1.select(s);
            double kgB = dtCon1.getDouble("kilos", true);
            kilCompra -= dtCon1.getDouble("kilos", true);
            impCompra -= dtCon1.getDouble("importe", true);
            
            if (codigo1 > 10990 && incPistolas)
            {
                // Sumo entradas de lomos pistolas
                s = "select pro_codi,deo_codi "
                    + "from v_despsal as d where deo_fecha>='" + fecIniComE.getFechaDB() + "'"
                    + " and deo_fecha<='" + fecStockE.getFechaDB() + "'"
                    + " and (pro_codi=" + codigo1
                    + " or pro_codi= " + codigo2 + ") "
                    + " and def_prcost <= 0";
                if (dtCon1.select(s))
                {
                    msgBox("DESPIECES DE TIPO 108 A 109 SIN VALORAR EN PRODUCTO: " + dtCon1.getInt("pro_codi")
                        + " DESPIECE: " + dtCon1.getInt("deo_codi"));
                    return;
                }
                s = "select sum(def_kilos) as kilos, sum(def_kilos*def_prcost) as importe "
                    + "from v_despsal as d where deo_fecha>='" + fecIniComE.getFechaDB() + "'"
                    + " and deo_fecha<='" + fecStockE.getFechaDB() + "'"
                    + " and (pro_codi=" + codigo1
                    + " or pro_codi= " + codigo2 + ")";
                dtCon1.select(s);
                kgEntra += dtCon1.getDouble("kilos", true);
                kilCompra += dtCon1.getDouble("kilos", true);
                impCompra += dtCon1.getDouble("importe", true);
            }
             // Sumo Entradas  de  Lomos de pistolas
            s = "select pro_codi,deo_codi "
                + "from v_despsal as d where  tid_codi= " + TIDE_108A109
                + " and deo_fecha>='" + fecIniComE.getFechaDB() + "'"
                + " and deo_fecha<='" + fecFinComE.getFechaDB() + "'"
                + " and (pro_codi=" + codigo1
                + " or pro_codi= " + codigo2 + ") "
                + " and def_prcost <= 0";
            if (dtCon1.select(s))
            {
                msgBox("DESPIECES DE TIPO 108 A 109 SIN VALORAR EN PRODUCTO: " + dtCon1.getInt("pro_codi")
                    + " DESPIECE: " + dtCon1.getInt("deo_codi"));
                return;
            }
            s = "select sum(def_kilos) as kilos, sum(def_kilos*def_prcost) as importe "
                + "from v_despsal as d where  tid_codi= " + TIDE_108A109
                + " and deo_fecha>='" + fecIniComE.getFechaDB() + "'"
                + " and deo_fecha<='" + fecFinComE.getFechaDB() + "'"
                + " and (pro_codi=" + codigo1
                + " or pro_codi= " + codigo2 + ")";
            dtCon1.select(s);
            double kg108 = dtCon1.getDouble("kilos", true);
            kilCompra += dtCon1.getDouble("kilos", true);
            impCompra += dtCon1.getDouble("importe", true);
            
            // Añado los pedidos cde compra
            s = "select * from v_pedico as c where "
                + "  pcc_fecrec between '" + fecIniComE.getFechaDB() + "' and '" + fecFinComE.getFechaDB() + "'"
                + " and pcc_estrec = 'P'"
                +// Solo pendientes.
                " and (c.pro_codi=" + codigo1
                + " or c.pro_codi= " + codigo2 + ")";
            double kgPed = 0;
            if (dtCon1.select(s))
            {
                do
                {
                    switch (dtCon1.getString("pcc_estad"))
                    {
                        case "P":
                            kgPed += dtCon1.getDouble("pcl_cantpe");
                            kilCompra += dtCon1.getDouble("pcl_cantpe");
                            impCompra += dtCon1.getDouble("pcl_cantpe") * dtCon1.getDouble("pcl_precpe");
                            break;
                        case "C":
                            kgPed += dtCon1.getDouble("pcl_cantco");
                            kilCompra += dtCon1.getDouble("pcl_cantco");
                            impCompra += dtCon1.getDouble("pcl_cantco") * dtCon1.getDouble("pcl_precco");
                            break;
                        default:
                            kgPed += dtCon1.getDouble("pcl_cantfa");
                            kilCompra += dtCon1.getDouble("pcl_cantfa");
                            impCompra += dtCon1.getDouble("pcl_cantfa") * dtCon1.getDouble("pcl_precfa");
                    }
                } while (dtCon1.next());
            }
            v.add(kilCompra);
            v.add(kilCompra == 0 ? 0 : impCompra / kilCompra);
            v.add(impCompra);
            v.add(kgEntra);
            v.add(kgPed);
            v.add(kgB);
            v.add(kg108);

            jtLomos.addLinea(v);
        }
    }
    
    void calculaBolas() throws SQLException, ParseException {
        jtBolas.removeAllDatos();
        Iterator<Integer> itBolas = htBolas.keySet().iterator();
        Integer bola;
        codBolas = "";

        while (itBolas.hasNext())
        {
            bola = itBolas.next();
            codBolas += bola + ",";
        }
        codBolas = codBolas.substring(0, codBolas.length() - 1);
        String s= "select d.pro_codi,st.stp_painac, 0 as kilventa,0 as kildesp, sum(def_kilos) as kilentra"
            + " from v_despfin as d, stockpart as st "
            + "where  d.pro_codi in (" + codBolas + ") "
            + " and d.def_tiempo between '" + fecIniDesE.getFechaDB() + "' and '"+fecFinDesE.getFechaDB()+"'"
            + " and d.pro_codi = st.pro_codi "
            + " and d.def_serlot = st.pro_serie "
            + " and d.pro_lote = st.pro_nupar "
            + " and d.def_ejelot = st.eje_nume "
            + " and d.pro_numind = st.pro_numind "            
            +" group  by d.pro_codi, stp_painac"
        + " union all "
            + "select a.pro_codi,st.stp_painac, sum(avp_canti) as kilventa,0 as kildesp,0 as kilentra"
            + " from v_albventa_detalle as a, "
            + "v_despfin as d,stockpart as st "
            + "where avl_fecalt between '" + fecIniVentaE.getFechaDB()  + "' and '"+fecFinVentaE.getFechaDB()+"'"
            +" and a.pro_codi in (" + codBolas + ") "
            + " and a.pro_codi=d.pro_codi and "
            + "a.avp_ejelot=d.def_ejelot and a.avp_serlot=d.def_serlot and a.avp_numpar=d.pro_lote"
            + " and a.avp_numind = d.pro_numind "
            + " and d.def_tiempo between '" + fecIniDesE.getFechaDB() + "' and '"+fecFinDesE.getFechaDB()+"'"
            + " and d.pro_codi = st.pro_codi "
            + " and d.def_serlot = st.pro_serie "
            + " and d.pro_lote = st.pro_nupar "
            + " and d.def_ejelot = st.eje_nume "
            + " and d.pro_numind = st.pro_numind "            
            +" group  by a.pro_codi, stp_painac"
         + " union all "
            + "select d.pro_codi,st.stp_painac, 0 as kilventa,sum(def_kilos) as kildesp,0 as  kilentra from  "
            + "v_despori as d,stockpart as st, "
            + " v_despfin as f "
            + " where  d.pro_codi in (" + codBolas + ") "
            + " and f.def_tiempo between '" + fecIniVentaE.getFechaDB() + "' and '"+fecFinVentaE.getFechaDB()+"'"
            + " and d.pro_codi = f.pro_codi "
            + " and d.deo_serlot = f.def_serlot "
            + " and d.pro_lote = f.pro_lote "
            + " and d.deo_ejelot = f.def_ejelot "
            + " and d.pro_numind = f.pro_numind "            
            + " and d.deo_tiempo between '" + fecIniDesE.getFechaDB() + "' and '"+fecFinDesE.getFechaDB()+"'"
            + " and d.pro_codi = st.pro_codi "
            + " and d.deo_serlot = st.pro_serie "
            + " and d.pro_lote = st.pro_nupar "
            + " and d.deo_ejelot = st.eje_nume "
            + " and d.pro_numind = st.pro_numind "            
            +" group  by d.pro_codi, stp_painac"+
            " order by 1,2";
        importeVenta = 0;
        importeDesp = 0;
        kilosEntra = 0;

        if (dtCon1.select(s))
        {

            int proCodi=dtCon1.getInt("pro_codi");
            String pais=dtCon1.getString("stp_painac",true);
            double kilVenta=0,kilVentaA=0;
            double kilDesp=0,kilDespA=0;
            double kilEntra=0,kilEntraA=0;            
            do
            {
                if (proCodi!=dtCon1.getInt("pro_codi") || ! pais.equals(dtCon1.getString("stp_painac",true)))
                {                 
                    addLineaBola(proCodi,pais,kilVenta,kilEntra,kilDesp);
                    if (proCodi!=dtCon1.getInt("pro_codi"))
                    {
                        if (kilVenta!=kilVentaA || kilDesp!=kilDespA || kilEntra!=kilEntraA)
                              addLineaBola(proCodi,"**",kilVentaA,kilEntraA,kilDespA);
                        kilVentaA=0;
                        kilDespA=0;
                        kilEntraA=0;
                        proCodi=dtCon1.getInt("pro_codi");  
                    }
                    kilVenta=0;
                    kilDesp=0;
                    kilEntra=0;                                    
                    pais=dtCon1.getString("stp_painac",true);
                }
                kilVenta+=dtCon1.getDouble("kilventa");
                kilEntra+=dtCon1.getDouble("kilentra");
                kilDesp+=dtCon1.getDouble("kildesp");
                kilVentaA+=dtCon1.getDouble("kilventa");
                kilEntraA+=dtCon1.getDouble("kilentra");
                kilDespA+=dtCon1.getDouble("kildesp");          
            } while (dtCon1.next());
            addLineaBola(proCodi,pais,kilVenta,kilEntra,kilDesp);
            if (kilVenta!=kilVentaA || kilDesp!=kilDespA || kilEntra!=kilEntraA)
                addLineaBola(proCodi,"**",kilVentaA,kilEntraA,kilDespA);
        }
    }
    void calculaBolasAnt()  throws SQLException, ParseException 
    {
        jtBolAnt.removeAllDatos();
        Date fecIni=Formatear.sumaDiasDate(fecIniDesE.getDate(),-7);
        Date fecFin=Formatear.sumaDiasDate(fecFinDesE.getDate(),-7);
        String s= "select st.stp_painac,  sum(deo_kilos) as deo_kilos,sum(deo_kilos*deo_prcost) as importe "
            + " from v_despori as d, stockpart as st "
            + "where  d.pro_codi in (" + codBolas + ") "
            + "  and tid_codi between "+TIPODESP_BOLA
            + " and d.deo_tiempo between '" + Formatear.getFechaDB(fecIni) + "' and '"+Formatear.getFechaDB(fecFin) +"'"
            + " and d.pro_codi = st.pro_codi "
            + " and d.deo_serlot = st.pro_serie "
            + " and d.pro_lote = st.pro_nupar "
            + " and d.deo_ejelot = st.eje_nume "
            + " and d.pro_numind = st.pro_numind "            
            +" group  by stp_painac"+
            " order by st.stp_painac";
        if (dtCon1.select(s))
        {
            kilDespT=0;;
            impDespT=0;
            impCalcT=0;
            String paiNac=dtCon1.getString("stp_painac");
            double kilDesp=0;
            double impDesp=0;
            do
            {
                if (!paiNac.equals(dtCon1.getString("stp_painac")))
                {
                    addLineaBolaDesp(paiNac,kilDesp,impDesp );
                    paiNac=dtCon1.getString("stp_painac");
                    kilDesp=0;
                    impDesp=0;
                }
                kilDesp+=dtCon1.getDouble("deo_kilos");
                impDesp+=dtCon1.getDouble("importe");
            } while (dtCon1.next());
            addLineaBolaDesp(paiNac,kilDesp,impDesp );
             ArrayList v = new ArrayList();
         v.add("**");
         v.add(kilDespT);
         v.add(impDespT/kilDespT);       
         v.add(impCalcT-impDespT);         
         jtBolAnt.addLinea(v);
        }
        
    }
    void addLineaBolaDesp(String pais,double kilDesp,double impDesp) throws SQLException
    {
         if (pais==null)
             pais="";
         ArrayList v = new ArrayList();
         v.add(pais);
         v.add(kilDesp);
         v.add(impDesp/kilDesp);
         double impCalc = kilDesp  * (pais.equals("DE")?precioBolaAlemE.getValorDec():
             pais.equals("PL")?precioBolaPoloniaE.getValorDec():precioBolaOtrasE.getValorDec());
         v.add(impCalc-impDesp);
         kilDespT+=kilDesp;
         impDespT+=impDesp;
         impCalcT+=impCalc;
         jtBolAnt.addLinea(v);
    }
    void addLineaBola(int proCodi,String pais,double kilVenta,double kilEntra,double kilDesp) throws SQLException
    {
         if (pais==null)
             pais="";
         ArrayList v = new ArrayList();
         v.add(pais.equals("**")?"":proCodi);
         v.add(pais.equals("**")?"TOTAL ARTICULO":MantArticulos.getNombProd(proCodi, dtStat));              
         v.add(kilDesp);
         v.add(kilVenta);
         v.add(kilEntra);         
         double kilInventario=kilEntra-kilDesp-kilVenta;
         
          v.add(kilInventario);   // Kilos invetario       
         double precio = htBolas.get(proCodi);
         v.add(precio);
         double impVenta=kilVenta * precio;
         double impInventario=kilInventario*precio;
         double impDesp = kilDesp  * (pais.equals("DE")?precioBolaAlemE.getValorDec():
             pais.equals("PL")?precioBolaPoloniaE.getValorDec():precioBolaOtrasE.getValorDec());
         if (!pais.equals("**"))
         {
              importeVenta +=  impVenta;
              importeDesp+=impDesp;
              kilosEntra+=kilEntra;
              v.add((impVenta + impDesp + impInventario) / kilEntra);         
         }
         else
         {
            v.add((importeVenta + importeDesp + impInventario) / kilosEntra);         
            importeVenta=0;
            importeDesp=0;
            kilosEntra=0;
         }
          v.add(pais);
         jtBolas.addLinea(v);
    }
     void ponFechas() throws ParseException
    {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(Formatear.getDate("01-01-"+eje_numeE.getValorInt(),"dd-MM-yyyy"));
//        gc.set(GregorianCalendar.YEAR, eje_numeE.getValorInt());
        gc.set(GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.SUNDAY);
        gc.set(GregorianCalendar.WEEK_OF_YEAR, cta_semanaE.getValorInt());     
        tar_feciniE.setDate(new java.util.Date(gc.getTimeInMillis()));
        gc.set(GregorianCalendar.WEEK_OF_YEAR, cta_semanaE.getValorInt());
//        gc.set(GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.SUNDAY);
        
        fecStockE.setDate(Formatear.sumaDiasDate(tar_feciniE.getDate(),-1)); // Sabado anterior
        fecIniComE.setDate(Formatear.sumaDiasDate(tar_feciniE.getDate(),-3)); // Jueves anterior
        fecFinComE.setDate(Formatear.sumaDiasDate(tar_feciniE.getDate(),6)); // Sabado siguiente.
        fecIniDesE.setDate(tar_feciniE.getDate());
        fecIniVentaE.setDate(tar_feciniE.getDate());
        fecFinDesE.setDate(fecFinComE.getDate());
        fecFinVentaE.setDate(fecFinComE.getDate());
//        fecIniComE.setText(Formatear.sumaDias(tar_feciniE.getText(),"dd-MM-yyyy",-4) ); // Jueves anteerior
//        fecFinComE.setText(Formatear.sumaDias(tar_feciniE.getText(),"dd-MM-yyyy",3) );
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        Pprinc = new gnu.chu.controles.CPanel();
        PCondi = new gnu.chu.controles.CPanel();
        cLabel2 = new gnu.chu.controles.CLabel();
        cta_semanaE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#9");
        cLabel5 = new gnu.chu.controles.CLabel();
        tar_feciniE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        BirGrid = new gnu.chu.controles.CButton();
        cLabel3 = new gnu.chu.controles.CLabel();
        eje_numeE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        opInc1099 = new gnu.chu.controles.CCheckBox();
        jtLomos = new gnu.chu.controles.Cgrid(12);
        jtBolas = new gnu.chu.controles.Cgrid(9);
        jtBolAnt = new gnu.chu.controles.Cgrid(4);
        PPrecioBola = new gnu.chu.controles.CPanel();
        cLabel11 = new gnu.chu.controles.CLabel();
        precioBolaPoloniaE = new gnu.chu.controles.CTextField(Types.DECIMAL,"9.999");
        cLabel13 = new gnu.chu.controles.CLabel();
        cLabel14 = new gnu.chu.controles.CLabel();
        precioBolaAlemE = new gnu.chu.controles.CTextField(Types.DECIMAL,"9.999");
        cLabel15 = new gnu.chu.controles.CLabel();
        precioBolaOtrasE = new gnu.chu.controles.CTextField(Types.DECIMAL,"9.999");
        Brefrescar = new gnu.chu.controles.CButton();
        cLabel10 = new gnu.chu.controles.CLabel();
        fecFinDesE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        fecIniDesE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel18 = new gnu.chu.controles.CLabel();
        cLabel19 = new gnu.chu.controles.CLabel();
        fecIniVentaE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        fecFinVentaE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel21 = new gnu.chu.controles.CLabel();
        cLabel22 = new gnu.chu.controles.CLabel();
        cLabel23 = new gnu.chu.controles.CLabel();
        PPie = new gnu.chu.controles.CPanel();
        cLabel7 = new gnu.chu.controles.CLabel();
        fecStockE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel9 = new gnu.chu.controles.CLabel();
        fecIniComE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        fecFinComE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel12 = new gnu.chu.controles.CLabel();

        Pprinc.setLayout(new java.awt.GridBagLayout());

        PCondi.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        PCondi.setMaximumSize(new java.awt.Dimension(470, 25));
        PCondi.setMinimumSize(new java.awt.Dimension(470, 25));
        PCondi.setPreferredSize(new java.awt.Dimension(470, 25));
        PCondi.setLayout(null);

        cLabel2.setText("Ejercicio");
        PCondi.add(cLabel2);
        cLabel2.setBounds(3, 3, 50, 17);
        PCondi.add(cta_semanaE);
        cta_semanaE.setBounds(150, 3, 20, 17);

        cLabel5.setText("Fecha");
        PCondi.add(cLabel5);
        cLabel5.setBounds(180, 3, 50, 17);

        tar_feciniE.setMaximumSize(new java.awt.Dimension(10, 18));
        tar_feciniE.setMinimumSize(new java.awt.Dimension(10, 18));
        tar_feciniE.setPreferredSize(new java.awt.Dimension(10, 18));
        PCondi.add(tar_feciniE);
        tar_feciniE.setBounds(220, 2, 70, 17);

        BirGrid.setText("Aceptar");
        BirGrid.setPreferredSize(new java.awt.Dimension(2, 2));
        PCondi.add(BirGrid);
        BirGrid.setBounds(370, 2, 90, 20);

        cLabel3.setText("Semana");
        PCondi.add(cLabel3);
        cLabel3.setBounds(100, 3, 50, 17);
        PCondi.add(eje_numeE);
        eje_numeE.setBounds(60, 3, 35, 17);

        opInc1099.setSelected(true);
        opInc1099.setText("Inc. 1099?");
        opInc1099.setToolTipText("Procesar lomos pistolas generados antes de fecha Stock.");
        PCondi.add(opInc1099);
        opInc1099.setBounds(290, 2, 83, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        Pprinc.add(PCondi, gridBagConstraints);

        jtLomos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtLomos.setMaximumSize(new java.awt.Dimension(40, 100));
        jtLomos.setMinimumSize(new java.awt.Dimension(40, 100));

        ArrayList v1=new ArrayList();
        v1.add("Producto"); //0
        v1.add("Nombre"); // 1
        v1.add("Kil.Inv."); // 2
        v1.add("Prec.Ant"); // 3
        v1.add("Imp.Ant"); // 4
        v1.add("Kg.Compra"); // 5
        v1.add("Prec.Compra"); // 6
        v1.add("Imp.Compra"); // 7
        v1.add("Kg.Entra"); // 8
        v1.add("Kg.Pedido"); // 9
        v1.add("Kg. B"); // 10
        v1.add("Kg.108"); // 11

        jtLomos.setCabecera(v1);
        jtLomos.setAnchoColumna(new int[]{50,200,70,60,70,70,60,70,50,50,50,50});
        jtLomos.setAlinearColumna(new int[]{0,0,2,2,2,2,2,2,2,2,2,2});
        jtLomos.setFormatoColumna(2, "##,##9.99");
        jtLomos.setFormatoColumna(3, "--,--9.999");
        jtLomos.setFormatoColumna(4, "-,---,--9.9");
        jtLomos.setFormatoColumna(5, "##,##9.99");
        jtLomos.setFormatoColumna(6, "--,--9.999");
        jtLomos.setFormatoColumna(7, "-,---,--9.9");
        jtLomos.setFormatoColumna(8, "--,--9.9");
        jtLomos.setFormatoColumna(9, "--,--9.9");
        jtLomos.setFormatoColumna(10, "--,--9.9");
        jtLomos.setFormatoColumna(11, "--,--9.9");
        jtLomos.setAjustarGrid(true);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        Pprinc.add(jtLomos, gridBagConstraints);

        jtBolas.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtBolas.setMaximumSize(new java.awt.Dimension(240, 100));
        jtBolas.setMinimumSize(new java.awt.Dimension(240, 100));

        ArrayList v=new ArrayList();
        v.add("Producto");//0
        v.add("Nombre"); // 1
        v.add("Kg.Desp"); // 2
        v.add("Kg.Venta"); // 3
        v.add("Kg.Entra"); // 4
        v.add("Kg.Inv"); // 5
        v.add("Pr.Venta"); // 6
        v.add("Pr.Medio"); // 7
        v.add("Origen"); // 8
        jtBolas.setCabecera(v);
        jtBolas.setAnchoColumna(new int[]{60,200,65,65,65,50,50,50,30});
        jtBolas.setAlinearColumna(new int[]{0,0,2,2,2,2,2,2,0});
        jtBolas.setFormatoColumna(2, "--,--9.99");
        jtBolas.setFormatoColumna(3, "--,--9.99");
        jtBolas.setFormatoColumna(4, "--,--9.99");
        jtBolas.setFormatoColumna(5, "--,--9.99");
        jtBolas.setFormatoColumna(6, "#9.99");
        jtBolas.setFormatoColumna(7, "#9.99");
        jtBolas.setAjustarGrid(true);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 2.0;
        Pprinc.add(jtBolas, gridBagConstraints);

        ArrayList vba=new ArrayList();
        vba.add("Pais");
        vba.add("Kilos");
        vba.add("Precio");
        vba.add("Perdida");
        jtBolAnt.setCabecera(vba);
        jtBolAnt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtBolAnt.setMaximumSize(new java.awt.Dimension(240, 40));
        jtBolAnt.setMinimumSize(new java.awt.Dimension(240, 40));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Pprinc.add(jtBolAnt, gridBagConstraints);

        PPrecioBola.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        PPrecioBola.setMaximumSize(new java.awt.Dimension(115, 100));
        PPrecioBola.setMinimumSize(new java.awt.Dimension(115, 100));
        PPrecioBola.setPreferredSize(new java.awt.Dimension(115, 100));
        PPrecioBola.setLayout(null);

        cLabel11.setText("Polaca");
        PPrecioBola.add(cLabel11);
        cLabel11.setBounds(0, 50, 60, 17);

        precioBolaPoloniaE.setText("2.8");
        PPrecioBola.add(precioBolaPoloniaE);
        precioBolaPoloniaE.setBounds(70, 50, 35, 20);

        cLabel13.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        cLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cLabel13.setText("Precio Despieces");
        PPrecioBola.add(cLabel13);
        cLabel13.setBounds(2, 5, 105, 20);

        cLabel14.setText("Alemana");
        PPrecioBola.add(cLabel14);
        cLabel14.setBounds(0, 30, 60, 17);

        precioBolaAlemE.setValorDec(2.95);
        PPrecioBola.add(precioBolaAlemE);
        precioBolaAlemE.setBounds(70, 30, 35, 20);

        cLabel15.setText("Otras");
        PPrecioBola.add(cLabel15);
        cLabel15.setBounds(0, 70, 60, 17);

        precioBolaOtrasE.setText("2.9");
        PPrecioBola.add(precioBolaOtrasE);
        precioBolaOtrasE.setBounds(70, 70, 35, 20);

        Brefrescar.setText("Refrescar");
        PPrecioBola.add(Brefrescar);
        Brefrescar.setBounds(20, 220, 80, 19);

        cLabel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cLabel10.setText("Entradas");
        PPrecioBola.add(cLabel10);
        cLabel10.setBounds(10, 90, 80, 17);

        fecFinDesE.setMaximumSize(new java.awt.Dimension(10, 18));
        fecFinDesE.setMinimumSize(new java.awt.Dimension(10, 18));
        fecFinDesE.setPreferredSize(new java.awt.Dimension(10, 18));
        PPrecioBola.add(fecFinDesE);
        fecFinDesE.setBounds(40, 130, 70, 17);

        fecIniDesE.setMaximumSize(new java.awt.Dimension(10, 18));
        fecIniDesE.setMinimumSize(new java.awt.Dimension(10, 18));
        fecIniDesE.setPreferredSize(new java.awt.Dimension(10, 18));
        PPrecioBola.add(fecIniDesE);
        fecIniDesE.setBounds(40, 110, 70, 17);

        cLabel18.setText("Inicio");
        PPrecioBola.add(cLabel18);
        cLabel18.setBounds(2, 110, 40, 17);

        cLabel19.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cLabel19.setText("Salidas");
        PPrecioBola.add(cLabel19);
        cLabel19.setBounds(10, 150, 80, 17);

        fecIniVentaE.setMaximumSize(new java.awt.Dimension(10, 18));
        fecIniVentaE.setMinimumSize(new java.awt.Dimension(10, 18));
        fecIniVentaE.setPreferredSize(new java.awt.Dimension(10, 18));
        PPrecioBola.add(fecIniVentaE);
        fecIniVentaE.setBounds(40, 170, 70, 17);

        fecFinVentaE.setMaximumSize(new java.awt.Dimension(10, 18));
        fecFinVentaE.setMinimumSize(new java.awt.Dimension(10, 18));
        fecFinVentaE.setPreferredSize(new java.awt.Dimension(10, 18));
        PPrecioBola.add(fecFinVentaE);
        fecFinVentaE.setBounds(40, 190, 70, 17);

        cLabel21.setText("Fin");
        PPrecioBola.add(cLabel21);
        cLabel21.setBounds(2, 125, 40, 17);

        cLabel22.setText("Inicio");
        PPrecioBola.add(cLabel22);
        cLabel22.setBounds(2, 170, 40, 17);

        cLabel23.setText("Fin");
        PPrecioBola.add(cLabel23);
        cLabel23.setBounds(2, 190, 40, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        Pprinc.add(PPrecioBola, gridBagConstraints);

        PPie.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        PPie.setMaximumSize(new java.awt.Dimension(560, 22));
        PPie.setMinimumSize(new java.awt.Dimension(560, 22));
        PPie.setPreferredSize(new java.awt.Dimension(500, 22));
        PPie.setLayout(null);

        cLabel7.setText("Fec.Stock");
        PPie.add(cLabel7);
        cLabel7.setBounds(20, 2, 60, 17);

        fecStockE.setMaximumSize(new java.awt.Dimension(10, 18));
        fecStockE.setMinimumSize(new java.awt.Dimension(10, 18));
        fecStockE.setPreferredSize(new java.awt.Dimension(10, 18));
        PPie.add(fecStockE);
        fecStockE.setBounds(80, 2, 70, 17);

        cLabel9.setText("Inic. Compra");
        PPie.add(cLabel9);
        cLabel9.setBounds(160, 2, 80, 17);

        fecIniComE.setMaximumSize(new java.awt.Dimension(10, 18));
        fecIniComE.setMinimumSize(new java.awt.Dimension(10, 18));
        fecIniComE.setPreferredSize(new java.awt.Dimension(10, 18));
        PPie.add(fecIniComE);
        fecIniComE.setBounds(240, 2, 70, 17);

        fecFinComE.setMaximumSize(new java.awt.Dimension(10, 18));
        fecFinComE.setMinimumSize(new java.awt.Dimension(10, 18));
        fecFinComE.setPreferredSize(new java.awt.Dimension(10, 18));
        PPie.add(fecFinComE);
        fecFinComE.setBounds(400, 2, 70, 17);

        cLabel12.setText("Fin. Compra");
        PPie.add(cLabel12);
        cLabel12.setBounds(320, 2, 70, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        Pprinc.add(PPie, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton BirGrid;
    private gnu.chu.controles.CButton Brefrescar;
    private gnu.chu.controles.CPanel PCondi;
    private gnu.chu.controles.CPanel PPie;
    private gnu.chu.controles.CPanel PPrecioBola;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CLabel cLabel10;
    private gnu.chu.controles.CLabel cLabel11;
    private gnu.chu.controles.CLabel cLabel12;
    private gnu.chu.controles.CLabel cLabel13;
    private gnu.chu.controles.CLabel cLabel14;
    private gnu.chu.controles.CLabel cLabel15;
    private gnu.chu.controles.CLabel cLabel18;
    private gnu.chu.controles.CLabel cLabel19;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel21;
    private gnu.chu.controles.CLabel cLabel22;
    private gnu.chu.controles.CLabel cLabel23;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel9;
    private gnu.chu.controles.CTextField cta_semanaE;
    private gnu.chu.controles.CTextField eje_numeE;
    private gnu.chu.controles.CTextField fecFinComE;
    private gnu.chu.controles.CTextField fecFinDesE;
    private gnu.chu.controles.CTextField fecFinVentaE;
    private gnu.chu.controles.CTextField fecIniComE;
    private gnu.chu.controles.CTextField fecIniDesE;
    private gnu.chu.controles.CTextField fecIniVentaE;
    private gnu.chu.controles.CTextField fecStockE;
    private gnu.chu.controles.Cgrid jtBolAnt;
    private gnu.chu.controles.Cgrid jtBolas;
    private gnu.chu.controles.Cgrid jtLomos;
    private gnu.chu.controles.CCheckBox opInc1099;
    private gnu.chu.controles.CTextField precioBolaAlemE;
    private gnu.chu.controles.CTextField precioBolaOtrasE;
    private gnu.chu.controles.CTextField precioBolaPoloniaE;
    private gnu.chu.controles.CTextField tar_feciniE;
    // End of variables declaration//GEN-END:variables
}
