package gnu.chu.anjelica.margenes;

import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.almacen.ActualStkPart;
import gnu.chu.anjelica.almacen.MvtosAlma;
import gnu.chu.controles.CTextField;
import gnu.chu.controles.StatusBar;
import gnu.chu.eventos.GridAdapter;
import gnu.chu.eventos.GridEvent;
import gnu.chu.interfaces.PAD;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.PADThread;
import gnu.chu.utilidades.navegador;
import gnu.chu.utilidades.ventanaPad;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Hashtable;
/**
 *
 * <p>Titulo: CalcTarifa</p>
 * <p>Descripción: Programa para calcular tarifas</p>
 * <p>Copyright: Copyright (c) 2005-2016
 *
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
 * @version 1.0
 *
 */

public class CalcTarifa extends ventanaPad implements PAD
{
    double kilStock,impStock;
    boolean swCarga=false;
    String s;
    String feulin;
    MvtosAlma mvtosAlm = new MvtosAlma();
    DatosTabla dtProd;
    double kilPendRec,  ImpPendRec;
    double kilProd,kilProd1;
    double kgPed,impPed;
    double impProd,impProd1;
    private final int JT_KGEXI=2;
    private final int JT_COSSTK=3;
    private final int JT_COSEXI=8;
    private final int JT_COSTO=9;
    private final int JT_KGCOM=4;
    private final int JT_COSCOM=5;
    private final int JT_KGPRO=6;
    private final int JT_COSPRO=7;

    //private final int JT_COSASI=10;
    
    public CalcTarifa(EntornoUsuario eu, Principal p)
  {
    this(eu,p,null);
  }

  public CalcTarifa(EntornoUsuario eu, Principal p, Hashtable ht)
  {
      EU = eu;
      vl = p.panel1;
      jf = p;
      eje = true;

      setTitulo("Mant. Caculos de Tarifas");

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

    public CalcTarifa(gnu.chu.anjelica.menu p, EntornoUsuario eu)
    {

      EU = eu;
      vl = p.getLayeredPane();
       setTitulo("Mant. Caculos de Tarifas");
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
      this.setVersion("2016-05-23" );
      statusBar = new StatusBar(this);
      nav = new navegador(this,dtCons,false);
      iniciarFrame();
      strSql ="SELECT eje_nume,cta_semana FROM calctarifa "+
             " group by eje_nume,cta_semana order by eje_nume,cta_semana";
      this.getContentPane().add(nav, BorderLayout.NORTH);
      this.getContentPane().add(statusBar, BorderLayout.SOUTH);
      this.setPad(this);
      navActivarAll();
      dtCons.setLanzaDBCambio(false);
      initComponents();
      iniciarBotones(Baceptar, Bcancelar);
      this.setSize(new Dimension(582,522));
      conecta();
      activar(false);

    }
     @Override
    public void iniciarVentana() throws Exception
    {
         dtProd=new DatosTabla(ct);
         mvtosAlm.setUsaDocumentos(false);
         mvtosAlm.setIncUltFechaInv(false);
         mvtosAlm.setIgnDespSinValor(true);
         mvtosAlm.setIgnComprasSinValor(true);
         mvtosAlm.setAlmacen(0);
         mvtosAlm.setEntornoUsuario(EU);
         mvtosAlm.setSoloInventario(false);
         mvtosAlm.setIncluyeSerieX(false);
         jt.setButton(KeyEvent.VK_F5, Bvalorar);
          jt.setButton(KeyEvent.VK_F3, Bcosto);
         eje_numeE.setColumnaAlias("eje_nume");
         cta_semanaE.setColumnaAlias("cta_semana");
         jtArt.setEnabled(false);
         verDatos();
         activarEventos();
    }
    
    void activarEventos()
    {
        Bvalorar.addActionListener(new ActionListener()
        {
             @Override
             public void actionPerformed(ActionEvent e)
             {
                 try
                 {
                     if (jt.isVacio())
                         return;
                     jt.salirGrid();
                     feulin=null;
                     calculaCosto(jt.getSelectedRow());
                     jt.requestFocusSelectedLater();
                     verArticulos(jt.getValString(jt.getSelectedRowDisab(),0));
                     //cta_cosasiE.setValorDec(Formatear.redondea(impPed/kilos,2));
                 } catch (SQLException | ParseException ex)
                 {
                     Error("Error al calcular costos",ex);
                 }
             }
        });
        Bcosto.addActionListener(new ActionListener()
        {
             @Override
             public void actionPerformed(ActionEvent e)
             {
                     if (jt.isVacio())
                         return;
                     jt.salirGrid();
                     double imporVal=(jt.getValorDec(JT_KGEXI)*
                         (jt.getValorDec(JT_COSEXI)==0?jt.getValorDec(JT_COSSTK):jt.getValorDec(JT_COSEXI)))+
                         (jt.getValorDec(JT_KGCOM)*jt.getValorDec(JT_COSCOM))+
                         (jt.getValorDec(JT_KGPRO)*jt.getValorDec(JT_COSPRO));
                     double kilosVal=jt.getValorDec(JT_KGEXI)+jt.getValorDec(JT_KGCOM)+jt.getValorDec(JT_KGPRO);
                     jt.setValor(kilosVal==0?0:Formatear.redondea(imporVal / kilosVal, 2), JT_COSTO);
                     cta_costoE.setValorDec(kilosVal==0?0:Formatear.redondea(imporVal / kilosVal, 2));
                    
                     jt.requestFocusSelectedLater();
                     //cta_cosasiE.setValorDec(Formatear.redondea(impPed/kilos,2));

             }
        });
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
        jt.addGridListener(new GridAdapter()
        {
            @Override
            public void afterCambiaLinea(GridEvent event)
            {
                verArticulos(jt.getValString(event.getLinea(),0));
            }
           @Override
          public void afterCambiaLineaDis(GridEvent event){ 
                verArticulos(jt.getValString(event.getLinea(),0));
          }
        });
        BirGrid.addFocusListener(new FocusAdapter()
        {
           @Override
           public void focusGained(FocusEvent e) {
               if (tar_feciniE.isNull() || tar_fecfinE.isNull() || nav.pulsado==navegador.QUERY)
                   return;
               if (tar_feciniE.getError() || tar_fecfinE.getError())
                   return;
               if (tar_fecfinE.hasCambio())
               {
                   tar_fecfinE.resetCambio();
                    try {
                        ponFechas();
                    } catch ( ParseException  ex)
                    {
                        Error("Error al poner fechas",ex);
                        return;
                    }

                    if (!llenaGrid())
                        return;
                    Baceptar.setEnabled(true);                     
                    Bvalorar.setEnabled(true);                    
               }
           }
         });
    }
    
    void ponFechas() throws ParseException
    {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(Formatear.getDateAct());
        gc.set(GregorianCalendar.YEAR, eje_numeE.getValorInt());
        gc.set(GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.MONDAY);
        gc.set(GregorianCalendar.WEEK_OF_YEAR, cta_semanaE.getValorInt()+1);     
        tar_feciniE.setDate(new java.util.Date(gc.getTimeInMillis()));
        gc.set(GregorianCalendar.WEEK_OF_YEAR, cta_semanaE.getValorInt()+2);
        gc.set(GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.SUNDAY);
        tar_fecfinE.setDate(new java.util.Date(gc.getTimeInMillis()));
        fecStockE.setText(Formatear.sumaDias(tar_feciniE.getText(),"dd-MM-yyyy",-4));
        fecIniProdE.setText(Formatear.sumaDias(tar_feciniE.getText(),"dd-MM-yyyy",-8));
        fecFinProdE.setText(Formatear.sumaDias(tar_feciniE.getText(),"dd-MM-yyyy",-4) );
        fecIniPedE.setText(Formatear.sumaDias(tar_feciniE.getText(),"dd-MM-yyyy",-4) );
        fecFinPedE.setText(Formatear.sumaDias(tar_feciniE.getText(),"dd-MM-yyyy",0) );
    }
    
    boolean llenaGrid()
    {
        try
        {
            if (dtCon1.select("select * from calctarifa where eje_nume= "+eje_numeE.getValorInt()+
                " and cta_semana="+cta_semanaE.getValorInt()))
            {
                msgBox("Ya existen calculos para este ejercicio y semana");
                return false;
            }
            swCarga=true;
            jt.setEnabled(false);
            jt.removeAllDatos();
            /** 
             * @todo Controlar semana cuando la semana es 1
             */
            s="SELECT pro_codart,pro_nomb,tar_linea,cta_costo FROM tarifa as t left join calctarifa on eje_nume="+eje_numeE.getValorInt()+
                " and cta_semana="+(cta_semanaE.getValorInt()-1)+
                " and pve_codi = pro_codart "
                + " where pro_codart!='' and pro_codart!='X' "
                + " AND tar_codi=6 and tar_fecini between '"+Formatear.getFecha(Formatear.sumaDiasDate(tar_feciniE.getDate(),-13),"yyyy-MM-dd")
                +"' and '"+Formatear.getFecha(Formatear.sumaDiasDate(tar_feciniE.getDate(),-7),"yyyy-MM-dd")+"'"
                +" order by  tar_linea";
            int nl=0;
            
            if (dtCon1.select(s))
            {
                do
                {                    
                    ArrayList vc=new ArrayList();
                    vc.add(dtCon1.getString("pro_codart")); // 0
                    vc.add(dtCon1.getString("pro_nomb")); // 1
                    vc.add(0); // 2
                    vc.add(0); // 3
                    vc.add(0); // 4
                    vc.add(0); // 5
                    vc.add(0); // 6
                    vc.add(0); // 7 
                    vc.add(0); // 8
                    vc.add(0);
                    vc.add(0);
                    jt.addLinea(vc);
                    nl++;
                } while (dtCon1.next());
            }
            feulin=null;
            for (int n=0;n<nl;n++)
            {
                calculaCosto(n);               
            }
            jt.setEnabled(true);
            jt.requestFocusInicio();
            swCarga=false;
            mensajeErr("Tarifa Calculada");
        } catch (ParseException | SQLException ex)
        {
           Error("Erro al cargar tarifa anterior",ex);
        }
        return true;
    }
    
    void calculaCosto(int nLinea) throws SQLException, ParseException
    {
        if (feulin == null)
        {
            feulin=ActualStkPart.getFechaUltInv(EU.em_cod,EU.ejercicio,tar_fecfinE.getDate(),dtStat);
            mvtosAlm.iniciarMvtos(feulin,fecStockE.getText(),dtStat);
        }
        double impStockVal,kilosVal;
        double kilosStock=0,imporStock=0;
        double kilosProd=0,imporProd=0;
        double kilosPendRec=0,imporPendRec=0;
        kilStock = 0;
        impStock = 0;
        impStockVal = 0;
        kilosVal=0;
        kilProd = 0;
        impProd = 0;
        kilPendRec = 0;
        ImpPendRec = 0;
        final int SBEPROD=14; // Seccion de Produccion
       
        s = "select t.pro_codi,pro_cosinc,sbe_codi from prodtarifa as t,v_articulo as ar "
            + " where pve_codi ='" + jt.getValString(nLinea, 0) + "'"
            + " and t.pro_codi= ar.pro_codi";
        if (dtProd.select(s))
        {
            do
            {  
                calculaProd(dtProd.getInt("pro_codi"),null,dtProd.getDouble("pro_cosinc"));
                kilosStock+=kilStock;
                imporStock+=impStock;
                kilosVal +=kilStock;
                impStockVal += jt.getValorDec(nLinea, JT_COSEXI)==0 || dtProd.getInt("sbe_codi")==SBEPROD ?impStock:
                        (mvtosAlm.getKilosStock() *  jt.getValorDec(nLinea, JT_COSEXI));
                kilosVal+=kilProd;
                impStockVal+=impProd+(dtProd.getDouble("pro_cosinc")*kilProd);
                kilosProd+=kilProd;
                imporProd+=impProd+(dtProd.getDouble("pro_cosinc")*kilProd);
                kilosPendRec+=kilPendRec;
                imporPendRec+=ImpPendRec;
                kilosVal+=kilPendRec;
                impStockVal+=ImpPendRec;
//                if (dtProd.getInt("sbe_codi") == 14) 
//                { // Producto de Producion propia                  
//                   kilosVal+=kilProd;
//                   impStockVal+= kilProd*mvtosAlm.getPrecioStock();
//                }
//                else
//                {                  
//                   kilosVal+=kilPendRec;
//                   impStockVal+= ImpPendRec;
//                }                               
            } while (dtProd.next());
            jt.setValor(kilosStock, nLinea, 2);            
            jt.setValor(kilosStock==0?0:Formatear.redondea(imporStock / kilosStock, 2), nLinea, 3);
            jt.setValor(kilosPendRec, nLinea, 4);
            jt.setValor(kilosPendRec==0?0:Formatear.redondea(imporPendRec / kilosPendRec, 2), nLinea, 5);
            jt.setValor(kilosProd, nLinea, 6);
            jt.setValor(kilosProd==0?0:Formatear.redondea(imporProd / kilosProd, 2), nLinea, 7);            
            jt.setValor(kilosVal==0?0:Formatear.redondea(impStockVal / kilosVal, 2),nLinea, JT_COSTO);
            if (jt.isEnabled())
            {
                cta_prprodE.setValorDec(kilosProd==0?0:Formatear.redondea(imporProd / kilosProd, 2));
                cta_kgprodE.setValorDec(kilosProd);
                cta_prcompE.setValorDec(kilosPendRec==0?0:Formatear.redondea(imporPendRec / kilosPendRec, 2));
                cta_kgcompE.setValorDec(kilosPendRec);
                cta_prmexE.setValorDec(kilStock==0?0:Formatear.redondea(impStock / kilStock, 2));
                cta_kgexisE.setValorDec(kilStock);
                cta_costoE.setValorDec(kilosVal==0?0:Formatear.redondea(impStockVal / kilosVal, 2));
            }
        }
    }
    /**
     * Caclula costos para un producto
     * @param proCodi
     * @param v ArrayList a llenar
     * @throws SQLException
     * @throws ParseException 
     */
    void calculaProd(int proCodi,ArrayList v,double costoInc) throws SQLException,ParseException
    {
        kilStock=0;
        impStock=0;
        if (mvtosAlm.calculaMvtos(proCodi, dtCon1, dtStat, null, null))
        {
            kilStock = mvtosAlm.getKilosStock();
            impStock = mvtosAlm.getKilosStock()
                * (mvtosAlm.getPrecioStock() + costoInc);
        }
        getValorDespiece(dtProd.getInt("pro_codi"),
            dtCon1, fecIniProdE.getDate(), // Lunes Ant.
            fecFinProdE.getDate());
        impProd+=(costoInc*kilProd);
        getRecepPendiente(dtProd.getInt("pro_codi"),
            dtCon1, fecIniPedE.getDate(),// Sabado
            fecFinPedE.getDate()); // Jueves
        if (v==null)
            return;
        v.add(kilStock);              
        v.add(kilStock==0?0:Formatear.redondea(impStock/kilStock,2));       
        v.add(kilPendRec);
        v.add(kilPendRec==0?0:Formatear.redondea(ImpPendRec/kilPendRec,2));
         v.add(kilProd);
        v.add(kilProd==0?0:Formatear.redondea(impProd/kilProd,2));
    }
    boolean getValorDespiece(int proCodi,DatosTabla dt,java.util.Date fecini,java.util.Date fecfin) throws SQLException
    {
        kilProd=0;
        impProd=0;
        s="select sum(def_kilos) as kilos,sum(def_kilos*def_prcost) as importe from v_despsal where pro_codi = "+proCodi+
            ((proCodi >= 10994 && proCodi<=10995) || (proCodi>=40801 && proCodi<41000) ?"": " and deo_incval='S' ")
             + " and deo_fecha >= TO_DATE('" + Formatear.getFecha(fecini, "dd-MM-yyyy") + "','dd-MM-yyyy') "
             + " and deo_fecha <= TO_DATE('" + Formatear.getFecha(fecfin, "dd-MM-yyyy")+ "','dd-MM-yyyy') "+
             " and def_prcost > 0";
        dt.select(s);
        if (dt.getDouble("kilos",true)==0)
            return false;
        kilProd=dt.getDouble("kilos",true);
        impProd=dt.getDouble("importe",true);
        return true;
    }
    
    boolean getRecepPendiente(int proCodi, DatosTabla dt,java.util.Date fecini,java.util.Date fecfin) throws SQLException
    {
        kilPendRec=0;
        ImpPendRec=0;
        s="select * from v_pedico where pro_codi = "+proCodi+
             " and pcc_fecrec >= to_date('" + Formatear.getFecha(fecini, "dd-MM-yyyy") + "','dd-MM-yyyy')"+
             " and pcc_fecrec <= to_date('" + Formatear.getFecha(fecfin, "dd-MM-yyyy") + "','dd-MM-yyyy')" ;
            // " and pcc_estrec = 'P'";
        if (!dt.select(s))
            return false;
       
        do
        {          
            switch (dtCon1.getString("pcc_estad"))
            {
                case "P":
                    kgPed = dtCon1.getDouble("pcl_cantpe");
                    impPed = dtCon1.getDouble("pcl_precpe");
                    break;
                case "C":
                    kgPed = dtCon1.getDouble("pcl_cantco");
                    impPed = dtCon1.getDouble("pcl_precco");
                    break;
                default:
                    kgPed = dtCon1.getDouble("pcl_cantfa");
                    impPed = dtCon1.getDouble("pcl_precfa");
            }
            kilPendRec+=kgPed;
            ImpPendRec+=kgPed*impPed;            
        } while (dt.next());
        return true;
    }
    void getDatos(int proCodi) throws SQLException
    {
       
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

        pve_codiE = new gnu.chu.controles.CTextField(Types.CHAR,"X",15);
        pve_nombE = new gnu.chu.controles.CTextField(Types.CHAR,"X",50);
        cta_kgexisE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--,--9.9");
        cta_prmexE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---9.99");
        cta_kgcompE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##,##9");
        cta_prcompE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9.99");
        cta_kgprodE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##,##9");
        cta_prprodE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9.99");
        cta_costoE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9.99");
        cta_cosasiE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9.99");
        pro_codiE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#####9");
        cta_cosantE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9.99");
        pro_nombE = new gnu.chu.controles.CTextField();
        Pprinc = new gnu.chu.controles.CPanel();
        PCondi = new gnu.chu.controles.CPanel();
        cLabel2 = new gnu.chu.controles.CLabel();
        cta_semanaE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#9");
        cLabel5 = new gnu.chu.controles.CLabel();
        tar_feciniE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel6 = new gnu.chu.controles.CLabel();
        tar_fecfinE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        BirGrid = new gnu.chu.controles.CButton();
        cLabel3 = new gnu.chu.controles.CLabel();
        eje_numeE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        jt = new gnu.chu.controles.CGridEditable(11)
        {

        }
        ;
        PPie = new gnu.chu.controles.CPanel();
        Bcancelar = new gnu.chu.controles.CButton();
        Baceptar = new gnu.chu.controles.CButton();
        Bvalorar = new gnu.chu.controles.CButton();
        cLabel7 = new gnu.chu.controles.CLabel();
        fecStockE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel8 = new gnu.chu.controles.CLabel();
        fecIniProdE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel9 = new gnu.chu.controles.CLabel();
        fecFinProdE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel10 = new gnu.chu.controles.CLabel();
        fecIniPedE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel11 = new gnu.chu.controles.CLabel();
        fecFinPedE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        opDesglo = new gnu.chu.controles.CCheckBox();
        Bcosto = new gnu.chu.controles.CButton();
        jtArt = new gnu.chu.controles.CGridEditable(8);

        pve_nombE.setEnabled(false);

        pro_nombE.setEnabled(false);

        Pprinc.setLayout(new java.awt.GridBagLayout());

        PCondi.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        PCondi.setMaximumSize(new java.awt.Dimension(470, 28));
        PCondi.setMinimumSize(new java.awt.Dimension(470, 28));
        PCondi.setPreferredSize(new java.awt.Dimension(470, 28));
        PCondi.setLayout(null);

        cLabel2.setText("Ejercicio");
        PCondi.add(cLabel2);
        cLabel2.setBounds(3, 3, 50, 17);
        PCondi.add(cta_semanaE);
        cta_semanaE.setBounds(150, 3, 20, 17);

        cLabel5.setText("De Fecha");
        PCondi.add(cLabel5);
        cLabel5.setBounds(180, 3, 60, 17);

        tar_feciniE.setMaximumSize(new java.awt.Dimension(10, 18));
        tar_feciniE.setMinimumSize(new java.awt.Dimension(10, 18));
        tar_feciniE.setPreferredSize(new java.awt.Dimension(10, 18));
        PCondi.add(tar_feciniE);
        tar_feciniE.setBounds(240, 3, 70, 17);

        cLabel6.setText("A Fecha");
        PCondi.add(cLabel6);
        cLabel6.setBounds(320, 3, 43, 17);

        tar_fecfinE.setMaximumSize(new java.awt.Dimension(10, 18));
        tar_fecfinE.setMinimumSize(new java.awt.Dimension(10, 18));
        tar_fecfinE.setPreferredSize(new java.awt.Dimension(10, 18));
        PCondi.add(tar_fecfinE);
        tar_fecfinE.setBounds(370, 3, 70, 17);

        BirGrid.setText("cButton1");
        BirGrid.setPreferredSize(new java.awt.Dimension(2, 2));
        PCondi.add(BirGrid);
        BirGrid.setBounds(450, 10, 10, 10);

        cLabel3.setText("Semana");
        PCondi.add(cLabel3);
        cLabel3.setBounds(100, 3, 50, 17);
        PCondi.add(eje_numeE);
        eje_numeE.setBounds(60, 3, 35, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        Pprinc.add(PCondi, gridBagConstraints);

        ArrayList v=new ArrayList();
        v.add("Codigo"); // 0
        v.add("Nombre"); // 1
        v.add("Ex.KG"); // 2
        v.add("Ex.Pr"); // 3
        v.add("Com.KG"); // 4
        v.add("Com.Pr."); // 5
        v.add("Pro.KG"); // 6
        v.add("Pro.Pr"); // 7
        v.add("Cos.Ex"); // 8
        v.add("Costo"); // 9
        v.add("Asig."); // 10
        jt.setCabecera(v);
        ArrayList vc=new ArrayList();
        vc.add(pve_codiE);
        vc.add(pve_nombE);
        vc.add(cta_kgexisE);
        vc.add(cta_prmexE);
        vc.add(cta_kgcompE);
        vc.add(cta_prcompE);
        vc.add(cta_kgprodE);
        vc.add(cta_prprodE);
        vc.add(cta_cosantE);
        vc.add(cta_costoE);
        vc.add(cta_cosasiE);

        try
        {
            jt.setCampos(vc);
        } catch (Exception k){}

        jt.setAnchoColumna(new int[]{100,250,50,40,50,40,50,50,50,50,50});
        jt.setAlinearColumna(new int[]{0,0,2,2,2,2,2,2,2,2,2});
        jt.setFormatoCampos();
        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 469;
        gridBagConstraints.ipady = 199;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Pprinc.add(jt, gridBagConstraints);

        PPie.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        PPie.setMaximumSize(new java.awt.Dimension(559, 47));
        PPie.setMinimumSize(new java.awt.Dimension(559, 47));
        PPie.setPreferredSize(new java.awt.Dimension(559, 47));
        PPie.setLayout(null);

        Bcancelar.setText("Cancelar");
        PPie.add(Bcancelar);
        Bcancelar.setBounds(470, 20, 80, 19);

        Baceptar.setText("Aceptar");
        PPie.add(Baceptar);
        Baceptar.setBounds(370, 20, 90, 19);

        Bvalorar.setText("Valorar F5");
        PPie.add(Bvalorar);
        Bvalorar.setBounds(420, 0, 60, 19);

        cLabel7.setText("Fec.Stock");
        PPie.add(cLabel7);
        cLabel7.setBounds(0, 2, 60, 17);

        fecStockE.setMaximumSize(new java.awt.Dimension(10, 18));
        fecStockE.setMinimumSize(new java.awt.Dimension(10, 18));
        fecStockE.setPreferredSize(new java.awt.Dimension(10, 18));
        PPie.add(fecStockE);
        fecStockE.setBounds(60, 2, 70, 17);

        cLabel8.setText(" Fin Prod");
        PPie.add(cLabel8);
        cLabel8.setBounds(280, 3, 60, 17);

        fecIniProdE.setMaximumSize(new java.awt.Dimension(10, 18));
        fecIniProdE.setMinimumSize(new java.awt.Dimension(10, 18));
        fecIniProdE.setPreferredSize(new java.awt.Dimension(10, 18));
        PPie.add(fecIniProdE);
        fecIniProdE.setBounds(210, 2, 70, 17);

        cLabel9.setText("Inic. Ped");
        PPie.add(cLabel9);
        cLabel9.setBounds(10, 22, 85, 17);

        fecFinProdE.setMaximumSize(new java.awt.Dimension(10, 18));
        fecFinProdE.setMinimumSize(new java.awt.Dimension(10, 18));
        fecFinProdE.setPreferredSize(new java.awt.Dimension(10, 18));
        PPie.add(fecFinProdE);
        fecFinProdE.setBounds(340, 3, 70, 17);

        cLabel10.setText("Inic. Prod");
        PPie.add(cLabel10);
        cLabel10.setBounds(150, 0, 60, 17);

        fecIniPedE.setMaximumSize(new java.awt.Dimension(10, 18));
        fecIniPedE.setMinimumSize(new java.awt.Dimension(10, 18));
        fecIniPedE.setPreferredSize(new java.awt.Dimension(10, 18));
        PPie.add(fecIniPedE);
        fecIniPedE.setBounds(60, 20, 70, 17);

        cLabel11.setText("Fin. Ped");
        PPie.add(cLabel11);
        cLabel11.setBounds(150, 20, 60, 17);

        fecFinPedE.setMaximumSize(new java.awt.Dimension(10, 18));
        fecFinPedE.setMinimumSize(new java.awt.Dimension(10, 18));
        fecFinPedE.setPreferredSize(new java.awt.Dimension(10, 18));
        PPie.add(fecFinPedE);
        fecFinPedE.setBounds(210, 20, 70, 17);

        opDesglo.setSelected(true);
        opDesglo.setText("Desglosar");
        PPie.add(opDesglo);
        opDesglo.setBounds(290, 20, 73, 17);

        Bcosto.setText("Costo F3");
        PPie.add(Bcosto);
        Bcosto.setBounds(480, 0, 70, 19);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        Pprinc.add(PPie, gridBagConstraints);

        ArrayList va=new ArrayList();
        va.add("Articulo");
        va.add("Nombre");
        va.add("Ex.KG"); // 2
        va.add("Ex.Pr"); // 3
        va.add("Com.KG"); // 4
        va.add("Com.Pr."); // 5
        va.add("Pro.KG"); // 6
        va.add("Pro.Pr"); // 7
        jtArt.setCabecera(va);
        jtArt.setAnchoColumna(new int[]{120,300,60,40,60,40,60,40});
        jtArt.setAlinearColumna(new int[]{2,0,2,2,2,2,2,2});
        ArrayList va1=new ArrayList();
        va1.add(pro_codiE);
        va1.add(pro_nombE);
        try{
            CTextField t1=new CTextField(Types.DECIMAL,"--,--9.9");
            t1.setEnabled(false);
            CTextField t2=new CTextField(Types.DECIMAL,"##9.99");
            t2.setEnabled(false);
            CTextField t3=new CTextField(Types.DECIMAL,"--,--9.9");
            t3.setEnabled(false);
            CTextField t4=new CTextField(Types.DECIMAL,"##9.99");
            t4.setEnabled(false);
            CTextField t5=new CTextField(Types.DECIMAL,"--,--9.9");
            t5.setEnabled(false);
            CTextField t6=new CTextField(Types.DECIMAL,"##9.99");
            t6.setEnabled(false);
            va1.add(t1);
            va1.add(t2);
            va1.add(t3);
            va1.add(t4);
            va1.add(t5);
            va1.add(t6);
            jtArt.setCampos(va1);
        } catch (Exception ss)
        {
            Error("Error al configurar grid",ss);
        }
        jtArt.setFormatoCampos();
        jtArt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtArt.setMaximumSize(new java.awt.Dimension(80, 60));
        jtArt.setMinimumSize(new java.awt.Dimension(80, 60));
        jtArt.setProcInsLinea(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Pprinc.add(jtArt, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CButton Bcancelar;
    private gnu.chu.controles.CButton Bcosto;
    private gnu.chu.controles.CButton BirGrid;
    private gnu.chu.controles.CButton Bvalorar;
    private gnu.chu.controles.CPanel PCondi;
    private gnu.chu.controles.CPanel PPie;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CLabel cLabel10;
    private gnu.chu.controles.CLabel cLabel11;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.controles.CLabel cLabel9;
    private gnu.chu.controles.CTextField cta_cosantE;
    private gnu.chu.controles.CTextField cta_cosasiE;
    private gnu.chu.controles.CTextField cta_costoE;
    private gnu.chu.controles.CTextField cta_kgcompE;
    private gnu.chu.controles.CTextField cta_kgexisE;
    private gnu.chu.controles.CTextField cta_kgprodE;
    private gnu.chu.controles.CTextField cta_prcompE;
    private gnu.chu.controles.CTextField cta_prmexE;
    private gnu.chu.controles.CTextField cta_prprodE;
    private gnu.chu.controles.CTextField cta_semanaE;
    private gnu.chu.controles.CTextField eje_numeE;
    private gnu.chu.controles.CTextField fecFinPedE;
    private gnu.chu.controles.CTextField fecFinProdE;
    private gnu.chu.controles.CTextField fecIniPedE;
    private gnu.chu.controles.CTextField fecIniProdE;
    private gnu.chu.controles.CTextField fecStockE;
    private gnu.chu.controles.CGridEditable jt;
    private gnu.chu.controles.CGridEditable jtArt;
    private gnu.chu.controles.CCheckBox opDesglo;
    private gnu.chu.controles.CTextField pro_codiE;
    private gnu.chu.controles.CTextField pro_nombE;
    private gnu.chu.controles.CTextField pve_codiE;
    private gnu.chu.controles.CTextField pve_nombE;
    private gnu.chu.controles.CTextField tar_fecfinE;
    private gnu.chu.controles.CTextField tar_feciniE;
    // End of variables declaration//GEN-END:variables
   @Override
  public void PADAddNew() {
    feulin=null;
    PCondi.resetTexto();
    PCondi.resetCambio();
    jt.removeAllDatos();
    activar(navegador.ADDNEW, true);
    Baceptar.setEnabled(false);
    Bvalorar.setEnabled(false);
    cta_semanaE.setEnabled(true);
    eje_numeE.setValorDec(EU.ejercicio);
    eje_numeE.requestFocus();
    mensajeErr("");
    mensaje("Insertando ....");
  }
    void verArticulos(String articulo)
    {
        if (jt.isVacio() || swCarga)
            return;
        try
        {
            if (feulin == null)
            {
                feulin=ActualStkPart.getFechaUltInv(EU.em_cod,EU.ejercicio,tar_fecfinE.getDate(),dtStat);
                if (feulin==null)
                    return;
                mvtosAlm.iniciarMvtos(feulin,fecStockE.getText(),dtStat);                
            }

            String s1="select pt.pro_codi,a.pro_nomb,pro_cosinc from prodtarifa as pt, v_articulo as a where pt.pro_codi = a.pro_codi "+
                " and pve_codi = '"+articulo+"'";
            
            jtArt.removeAllDatos();
            if (! dtProd.select(s1))
                return;
           
            do
            {
                ArrayList v=new ArrayList();
                v.add(dtProd.getInt("pro_codi"));
                v.add(dtProd.getString("pro_nomb"));
                if (! opDesglo.isSelected())
                {
                    v.add("");
                    v.add("");
                    v.add("");
                    v.add("");
                    v.add("");
                    v.add("");
                }
                else
                {
                    calculaProd(dtProd.getInt("pro_codi"), v,dtProd.getDouble("pro_cosinc"));
                }
                jtArt.addLinea(v);
            } while (dtProd.next());
        } catch (Exception ex)
        {
                  Error("Error al ver articulos",ex);
        }
    }
    @Override
  public void ej_addnew1() {
        try
        {
            jt.salirGrid();
            
//    if (cambiaLineaJT()>=0)
//    {
//      jt.requestFocusSelected();
//      return;
//    }
//    int row=checkRepetido();
      
            guardaDatos(eje_numeE.getValorInt(), cta_semanaE.getValorInt());
            activaTodo();
            ctUp.commit();
            mensajeErr("Calculos de Tarifa Guardados");
            mensaje("");
        } catch (SQLException ex)
        {
            Error("Error al insertar registro", ex);
        }
  }
     @Override
  public void PADEdit() {
      feulin=null;
    mensaje("Editando ....");
    jt.setEnabled(true);
    Baceptar.setEnabled(true);
    Bcancelar.setEnabled(true);
    Bvalorar.setEnabled(true);
    jt.requestFocusInicioLater();
  }
   @Override
  public void PADDelete()
  {
    activar(navegador.DELETE, true);
    feulin=null;
    mensaje("Borrando ....");
    
    Baceptar.setEnabled(true);
    Bcancelar.setEnabled(true);
    Bcancelar.requestFocus();
  }
    @Override
   public void ej_edit1() {
        try
        {
            jt.salirGrid();
            
//    if (cambiaLineaJT()>=0)
//    {
//      jt.requestFocusSelected();
//      return;
//    }
//    int row=checkRepetido();
//    if (row>=0)
//    {
//        jt.requestFocus(row,0);
//        return;
//    }
        borrarTarifa();
        guardaDatos(eje_numeE.getValorInt(),cta_semanaE.getValorInt());
        dtAdd.commit();
        activaTodo();
        verDatos();
        mensaje("");
        mensajeErr("Calculos de Tarifa Modficados");
        } catch (SQLException ex)
        {
           Error("Error al editar tarifas",ex);
        }
  }
    @Override
  public void canc_edit() {
    activaTodo();
    mensajeErr("Modificacion de Datos ... Cancelada");
    verDatos();
    mensaje("");
  }
  void borrarTarifa() throws SQLException
  {
      dtAdd.executeUpdate("delete from calctarifa  where eje_nume="+eje_numeE.getValorInt()+
                " and cta_semana="+cta_semanaE.getValorInt());
  }
  void verDatos()
  {
        try
        {
            if (dtCons.getNOREG())
                return;
            eje_numeE.setValorInt(dtCons.getInt("eje_nume"));
            cta_semanaE.setValorInt(dtCons.getInt("cta_semana"));
            jt.removeAllDatos();
            ponFechas();
            s="select c.*,p.pve_nomb from calctarifa as c "
                + "left join prodventa as p  on p.pve_codi = c.pve_codi where eje_nume="+dtCons.getInt("eje_nume")+
                " and cta_semana="+dtCons.getInt("cta_semana")+
                " order by cta_linea";
            if (!dtCon1.select(s))
            {
                msgBox("No encontrados calculos de tarifa");
                return;
            }
            swCarga=true;
            do
            {
                ArrayList vc= new ArrayList();
                vc.add(dtCon1.getString("pve_codi"));
                vc.add(dtCon1.getString("pve_nomb",true));
                vc.add(dtCon1.getDouble("cta_kgexis"));
                vc.add(dtCon1.getDouble("cta_prmex"));
                vc.add(dtCon1.getDouble("cta_kgcomp"));
                vc.add(dtCon1.getDouble("cta_prcomp"));
                vc.add(dtCon1.getDouble("cta_kgprod"));
                vc.add(dtCon1.getDouble("cta_prprod"));
                vc.add(dtCon1.getDouble("cta_coscal"));
                vc.add(dtCon1.getDouble("cta_costo"));
                vc.add(dtCon1.getDouble("cta_cosasi"));
                jt.addLinea(vc);                
            } while (dtCon1.next());
            jt.requestFocusInicio();
            swCarga=false;
            verArticulos(jt.getValString(0,0));
        } catch (SQLException | ParseException ex)
        {
           Error("Error al ver datos",ex);
        }
  }
  void guardaDatos(int ejerc,int semana) throws SQLException
  {
      int nl=jt.getRowCount();
      for (int n=0;n<nl;n++)
      {
          dtAdd.addNew("calctarifa");
          dtAdd.setDato("eje_nume",ejerc);
          dtAdd.setDato("cta_semana",semana);
          dtAdd.setDato("cta_linea",n+1);
          dtAdd.setDato("pve_codi",jt.getValString(n,0));
          dtAdd.setDato("cta_kgexis",jt.getValorDec(n,2));
          dtAdd.setDato("cta_prmex",jt.getValorDec(n,3));
          dtAdd.setDato("cta_kgcomp",jt.getValorDec(n,4));
          dtAdd.setDato("cta_prcomp",jt.getValorDec(n,5));
          dtAdd.setDato("cta_kgprod",jt.getValorDec(n,6));
          dtAdd.setDato("cta_prprod",jt.getValorDec(n,7));
          dtAdd.setDato("cta_coscal",jt.getValorDec(n,8));
          dtAdd.setDato("cta_costo",jt.getValorDec(n,9));
          dtAdd.setDato("cta_cosasi",jt.getValorDec(n,10));
          dtAdd.update();
      }
  }
   @Override
  public void canc_addnew() {
    activaTodo();
    mensajeErr("Insercion de Datos ... Cancelada");
//    verDatos();
    mensaje("");
  }
    @Override
    public void PADPrimero() {
        verDatos();
    }

    @Override
    public void PADAnterior() {
        verDatos();
    }

    @Override
    public void PADSiguiente() {
        verDatos();
    }

    @Override
    public void PADUltimo() {
        verDatos();
    }
 @Override
    public void PADQuery() {
        activar(navegador.QUERY, true);
        PCondi.setQuery(true);
        PCondi.resetTexto();
    //    eje_numeE.setText(""+EU.ejercicio);
        eje_numeE.requestFocus();

    }
    @Override
    public void ej_query1() {
 Baceptar.setEnabled(false);
    ArrayList v=new ArrayList();
    v.add(eje_numeE.getStrQuery());
    v.add(cta_semanaE.getStrQuery());
    PCondi.setQuery(false);
    s="SELECT eje_nume,cta_semana FROM calctarifa ";
    s=creaWhere(s,v);
    s+=" group by eje_nume,cta_semana order by eje_nume,cta_semana";
//    debug(s);
    try
    {
      if (!dtCons.select(s))
      {
        mensaje("");
        mensajeErr("No encontrados Registros para estos criterios");
        rgSelect();
        activaTodo();
        return;
      }
      mensaje("");
      strSql = s;
      activaTodo();
      rgSelect();
//      verDatos();
      mensajeErr("Nuevos registros selecionados");
    }
    catch (Exception ex)
    {
      fatalError("Error al buscar Tarifas: ", ex);
    }
        
    }

    @Override
    public void canc_query() {
        PCondi.setQuery(false);

        mensaje("");
        mensajeErr("Consulta ... CANCELADA");
        activaTodo();
        verDatos();
    }

   
   
    @Override
    public void canc_delete() {
        activaTodo();
        mensajeErr("Borrado de Datos ... Cancelada");        
        mensaje("");
    }

    @Override
    public void activar(boolean act)
    {
      activar(navegador.TODOS,act);
    }
     void activar(int modo,boolean act)
    {
      if (modo==navegador.TODOS)
        jt.setEnabled(act);
      if (modo==navegador.TODOS)
      {
        fecStockE.setEnabled(act);
        fecIniPedE.setEnabled(act);
        fecIniProdE.setEnabled(act);
        fecFinPedE.setEnabled(act);
        fecFinProdE.setEnabled(act);
        Bvalorar.setEnabled(act);
        Bcosto.setEnabled(act);
      }
      Baceptar.setEnabled(act);        
      Bcancelar.setEnabled(act);
      PCondi.setEnabled(act);
    }

    @Override
    public void ej_delete1() {
       try
      {
          borrarTarifa();
          dtAdd.commit();
          activaTodo();
          verDatos();
          mensaje("");
          mensajeErr("Calculos de Tarifa Borrados");
      } catch (SQLException ex)
      {
          Error("Error al borrar tarifas", ex);
      }
    }
}
