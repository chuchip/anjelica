package gnu.chu.anjelica.despiece;

import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.almacen.MvtosAlma;
import gnu.chu.anjelica.almacen.pdalmace;
import gnu.chu.controles.StatusBar;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * <p>Titulo: RegCosDes</p>
 * <p>Descripción: Regeneración Costos de Despieces<br>
 * Regenera los costos en los despieces (costos de productos de salida).
 * Pone en el campo def_preusu de la tabla v_despfin el valor de def_prcost
 * y recalcula el valor de def_prcost.</p>
 * <p>Copyright: Copyright (c) 2010-2018</p>
 * <p>
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
* <p>Empresa: MISL</p>
* @author chuchiP
* @version 1.0
*/

public class RegCosDes extends ventana {
    boolean debug=false;
    private boolean swSinCosto=false;
    private boolean swRegenerar;
    private String condWhereOrig;
//    DatosTabla dtAdd;
    final static int MAXPERM=1;
    PreparedStatement prstFin,prstFin1,prstFinCuantos,prstUpd1,prstUpd,prstDesp;
    ResultSet rsFin,rsFin1,rsDesp;
    MvtosAlma mvtosAlm;
    private final int NUMDEC_COSTO=4;
    PreparedStatement psInv;
   public RegCosDes(EntornoUsuario eu, Principal p) {
        EU = eu;
        vl = p.panel1;
        jf = p;
        eje = true;

        setTitulo("Regenerar Costos Despieces");

        try {
            if (jf.gestor.apuntar(this)) {
                jbInit();
            } else {
                setErrorInit(true);
            }
        } catch (Exception e) {
            Logger.getLogger(RegCosDes.class.getName()).log(Level.SEVERE, null, e);
            setErrorInit(true);
        }
    }

    public RegCosDes(gnu.chu.anjelica.menu p, EntornoUsuario eu) {

        EU = eu;
        vl = p.getLayeredPane();
        setTitulo("Regenerar Costos Despieces");
        eje = false;

        try {
            jbInit();
        } catch (Exception e) {
           Logger.getLogger(ValDespi.class.getName()).log(Level.SEVERE, null, e);
           setErrorInit(true);
        }
    }

    private void jbInit() throws Exception {

        iniciarFrame();

        this.setVersion("2018-04-14");
        statusBar = new StatusBar(this);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        conecta();

        initComponents();

        this.setSize(new Dimension(455,484));
        this.setMaximizable(true);
        this.setResizable(true);
        this.setIconifiable(true);
    }

    @Override
    public void iniciarVentana() throws Exception {
        dtAdd = new DatosTabla(dtCon1.getConexion());
        String feulin; // actStkPart.getFechaUltInv(EU.em_cod, 0, dtStat);
        String s = "select distinct(rgs_fecha) as cci_feccon from v_regstock as r "
                + " where r.emp_codi = " + EU.em_cod
                + " and tir_afestk='=' "
                + " order by cci_feccon desc ";

        if (dtStat.select(s)) {
            feulin = dtStat.getFecha("cci_feccon", "dd-MM-yyyy");
            do {
                fecinvE.addItem(dtStat.getFecha("cci_feccon", "dd-MM-yyyy"), dtStat.getFecha("cci_feccon", "dd-MM-yyyy"));
            } while (dtStat.next());
        } else {
            feulin = "01-01-" + EU.ejercicio; // Buscamos desde el principio del año.
            fecinvE.addItem(feulin);
        }
        s="select min(deo_fecha) as deo_fecha from v_despori  where deo_preusu=0 and deo_fecha>= to_Date('"+feulin+"','dd-MM-yyyy')";
        dtStat.select(s);
        if (dtStat.getDate("deo_fecha")!=null)
            feciniE.setDate(dtStat.getDate("deo_fecha"));
        fecinvE.setText(feulin);
        Pcondic.setDefButton(Baceptar.getBotonAccion());
       
  
        activarEventos();
    }
    private void activarEventos() {
       
        feciniE.addFocusListener(new FocusAdapter()
        {
            public void focusLost(FocusEvent e) {
                try
                {
                    if (feciniE.isNull() || feciniE.getError())
                        return;
                    int nLin = fecinvE.getItemCount() - 1;
                    for (int n = nLin; n >= 0; n--)
                    {
                        if (((String) fecinvE.getItemAt(n)).equals(feciniE.getText()))
                        {
                            fecinvE.setValor((String) fecinvE.getItemAt(n));
                            return;
                        }
                        if (Formatear.restaDias((String) fecinvE.getItemAt(n), feciniE.getText()) > 0)
                        {
                            fecinvE.setValor((String) fecinvE.getItemAt(n + 1));
                            return;
                        }
                    }
                    fecinvE.setValor((String) fecinvE.getItemAt(0));
                } catch (Exception k)
                {
                    Error("Error al buscar fecha", k);
                }
            }
        });
            
        Baceptar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try
                {
                    if (!checkDatosDesp())
                        return;
                    if (e.getActionCommand().equals("Regenerar"))
                        buscaDatos();
                    else
                        resetearDatos();
                } catch (SQLException | ParseException ex)
                {
                   Error("Error al comprobar datos para despieces",ex);
                }
            }
        });
        jt.addMouseListener(new MouseAdapter()
        {
             @Override
             public void mouseClicked(MouseEvent e) {
                 if (e.getClickCount()>1 && !jt.isVacio())
                     MantDesp.irDespiece(jt.getValorInt(jt.getSelectedRowDisab(),0),jt.getValorInt(jt.getSelectedRowDisab(),1) , jf);
             }
        });
        popEspere_BCancelaraddActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
              msgEspere("Cancelando actualización ");
              popEspere_BCancelarSetEnabled(false);
            }
        });
    }
    
  

    private boolean validaCampos() throws ParseException,SQLException
    {          
        if (feciniE.isNull() || feciniE.getError())
        {
            mensajeErr("Introduzca fecha inicio ");
            feciniE.requestFocus();
            return false;
        }
        if (Formatear.restaDias(feciniE.getText(), fecinvE.getValor())<0 )
        {
            mensajeErr("FECHA INVENTARIO debe ser Inferior a primera fecha ");
            feciniE.requestFocus();
            return false;
        }
        if (fecfinE.isNull() || fecfinE.getError())
        {
            mensajeErr("Introduzca fecha final ");
            fecfinE.requestFocus();
            return false;
        }
        if (Formatear.restaDias(fecfinE.getText(), feciniE.getText())<0 )
        {
            mensajeErr("FECHA FINAL NO PUEDE SER INFERIOR A INICIAL ");
            fecfinE.requestFocus();
            return false;
        }
        if (Formatear.comparaFechas(pdalmace.getFechaInventario( dtStat),feciniE.getDate() )>0)
        {
            msgBox("Fecha es infeior a  fecha Inventario Bloqueado. ( "+Formatear.getFecha( pdalmace.getFechaInventario( dtStat),"dd-MM-yy")
                + ").\n Imposible Regenerar costos");
            feciniE.requestFocus();
            return false;
        }
    
           return true;
    }
    /**
     * Anula la regeneracion de costos de despieces
     */
    private void resetearDatos()
    {
        try {
    
        
           String s="select * from v_despfin as fi,desporig as orig  "+
                    " WHERE  fi.def_preusu !=  0 " +
                     " AND fi.deo_codi =  orig.deo_codi " +
                     " and fi.eje_nume = orig.eje_nume " +
                   condWhereOrig;
            swRegenerar=true;
            if (! dtStat.select(s))
            {
                msgBox("No hay costos regenerados para estos criterios");
                return;
            }
        } catch (SQLException k)
        {
            Error("Error al resetear Datos de costos",k);
        }
        new miThread("") {

           @Override
           public void run() {

               reseteaDato1();
               resetMsgEspere();
           }
       };
    }

    void reseteaDato1() 
    {
        try 
        {
           msgEspere("Anulando Regeneracion de costos en despieces ...");
           setLabelMsgEspere("Anulando Costos");
           popEspere_BCancelarSetEnabled(false);
           String s = "UPDATE v_despfin set def_prcost = def_preusu,def_preusu=0 "
                        + " WHERE  def_preusu != 0 "
                        + " AND exists (select deo_codi from v_despori  as orig "
                        + " where orig.deo_codi = v_despfin.deo_codi "
                        + " and orig.eje_nume = v_despfin.eje_nume "
                        + condWhereOrig + ")";
            int nRegAf = stUp.executeUpdate(s);
            setMensajePopEspere("Anulados despieces de Salida: "+nRegAf,false);
         
            setMensajePopEspere("Anulados despieces de Salida Agrupados: "+nRegAf,false);
            s = "UPDATE desorilin set deo_prcost = deo_preusu "
                        + " WHERE  deo_preusu != 0 "+
                        "  and exists (select deo_codi from v_despori as orig "+
                        " where orig.deo_codi = desorilin.deo_codi "+
                        " and orig.eje_nume=desorilin.eje_nume "+                     
                         condWhereOrig+")"; 
              double nRegAfT=nRegAf;
              nRegAf=dtAdd.executeUpdate(s);
              nRegAfT+=nRegAf;
              setMensajePopEspere("Anulados despieces de Entrada "+nRegAf,false);
        
            dtAdd.commit();
            msgBox("Rescatados precio usuario en  " + nRegAfT + " Registros ");
            
        } catch (SQLException k)
        {
            Error("Error al resetear Datos de costos",k);
        }
    }
    private boolean checkDatosDesp() throws ParseException,SQLException
    {
        if (!validaCampos()) {
            return false;
        }
        condWhereOrig = " and deo_fecha between to_date('" + feciniE.getText() + "','dd-MM-yyyy')"
                + " and to_date('" + fecfinE.getText() + "','dd-MM-yyyy')";

         String s = "select  pro_codi,deo_codi, deo_fecha as deo_fecha,"
                    + " sum(deo_kilos) as kilos,sum(deo_kilos*deo_prcost) as costo "
                    + " from v_despori as orig WHERE 1=1  " + condWhereOrig
                    + " GROUP BY pro_codi,deo_codi,deo_fecha "
                    + " order by deo_codi ";
            if (!dtCon1.select(s)) {
                msgBox("No encontrados despieces con esos criterios");
                feciniE.requestFocus();
                return false;
            }
            jt.removeAllDatos();
            textoE.resetTexto();
            return true;
    }
    private void buscaDatos() {
       
      
         
           new miThread("")
        {

            @Override
            public void run() {
                try
                {
                    msgEspere("Comprobando validez despieces ...");
                    
                    setLabelMsgEspere("Buscando despieces ya valorados");
                    String s = "select * from v_despfin as fi,desporig as orig  "
                        + " WHERE  fi.def_preusu !=  0 "
                        + " AND fi.deo_codi =  orig.deo_codi "
                        + " and fi.eje_nume = orig.eje_nume "
                        + condWhereOrig;
                    swRegenerar = dtStat.select(s);
                    if (swRegenerar)
                    {
                        int res = mensajes.mensajeYesNo("Ya se lanzo la regeneración de costos en este periodo. Lanzarlo de nuevo ?");
                        if (res != mensajes.YES)
                        {
                            msgBox("Abortada la regeneración de costos");
                            resetMsgEspere();
                            return;
                        }
                    }
                } catch (SQLException k)
                {
                    Error("Error al calcular despieces", k);
                }
                jt.tableView.setVisible(false);
                buscaDato1();
                resetMsgEspere();
                jt.tableView.setVisible(true);
            }
        };
       
    }

    private void buscaDato1() 
    {
        try {
          
            String s;            
            setLabelMsgEspere("Regenerando Costos");
            popEspere_BCancelarSetEnabled(true);
            if (mvtosAlm == null) {
                mvtosAlm = new MvtosAlma();
                mvtosAlm.setResetCostoStkNeg(true); 
                mvtosAlm.setIncIniFechaInv(true);
            }
             
            mvtosAlm.setUsaDocumentos(false);            
            mvtosAlm.iniciarMvtos(fecinvE.getText(), dtAdd);                                  
           
            s = "UPDATE v_despfin set def_preusu= def_prcost "
                     + " WHERE def_preusu = 0 "
                     + " AND exists (select deo_codi from v_despori  as orig "
                     + " where orig.deo_codi = v_despfin.deo_codi "
                     + " and orig.eje_nume = v_despfin.eje_nume "                
                     + condWhereOrig + " )";
             int nRegAf = stUp.executeUpdate(s);

             textoE.setText(textoE.getText() + "\nAVISO: Puesto precio usuario a precio costo a " + nRegAf + " lineas de desp. salida");
             s = "UPDATE desorilin set deo_preusu= deo_prcost "
                     + " WHERE  deo_preusu = 0 "+
                     "  and exists (select deo_codi from v_despori as orig "+
                     " where orig.deo_codi = desorilin.deo_codi "+
                     " and orig.eje_nume=desorilin.eje_nume "+
                     condWhereOrig+" )";
             nRegAf = stUp.executeUpdate(s);             
             textoE.setText(textoE.getText() + "\nAVISO: Puesto precio usuario a precio costo a " + nRegAf + " lineas de desp. Entrada");
         
           for (int n=1;n<6;n++)
           {
             if ( getPopEspere().isBCancelarEnabled() )
                buscoDesp(n);
             if (debug)
                 break;
           }
//       
           ctUp.commit();
           if ( getPopEspere().isBCancelarEnabled())
            msgBox("Costos ... Recalculados");
        } catch (Exception k) {
            Error("Error al calcular despieces", k);
        }
    }
    /**
     * Busca despieces de la sentenecia en dtCon1
     * @param agrupados
     * @throws Exception 
     */
    void buscoDesp(int n) throws Exception {
        String s = "select * from mvtosalm "
            + " where mvt_time between to_date('" + feciniE.getText() + "','dd-MM-yyyy')"
            + " and to_date('" + fecfinE.getText() + "','dd-MM-yyyy')"
            + " and NOT (mvt_tipdoc='V' and mvt_serdoc='X') " // Excluir mvtos de traspasos entre almacenes
            +(debug? " and pro_codi = 10994 ":"")
            + " order by pro_codi,mvt_time,mvt_tipo";
        if (!dtCon1.select(s, true))
        {
            msgBox("No encontrado mvtos entre estas fechas");
            return;
        }
        s = "SELECT sum(rgs_kilos) as kilos, sum(rgs_kilos*rgs_prregu) as importe "
            + " from v_inventar  "
            + " where pro_codi =  ?"
            + " AND rgs_fecha::date = to_date('" + fecinvE.getValor() + "','dd-MM-yyyy')";
        psInv = dtCon1.getPreparedStatement(s);

        int proCodi = -1;
        double kgstock = 0;
        double importeStock;
        double precioMvto;
        double precioMedioStock = 0;
        do
        {
            if (proCodi != dtCon1.getInt("pro_codi"))
            {
                setMensajePopEspere("Regenerando costos. Vuelta "+n+ " de 5\n"
                    + "Recalculando Costos mvtos de articulo  " + proCodi, false);
                proCodi = dtCon1.getInt("pro_codi");
                psInv.setInt(1, proCodi);
                ResultSet rs = psInv.executeQuery();
                rs.next();
                kgstock = rs.getDouble("kilos");
//                   importe=rs.getDouble("importe");
                precioMedioStock = rs.getDouble("kilos") == 0 ? 0 : rs.getDouble("importe") / rs.getDouble("kilos");
            }
            precioMvto = dtCon1.getDouble("mvt_prec");
            if (dtCon1.getString("mvt_tipdoc").equals("D"))
            {
                dtAdd.executeUpdate("update desorilin set deo_prcost= " + precioMedioStock
                    + " where eje_nume=" + dtCon1.getInt("mvt_ejedoc")
                    + " and deo_codi=" + dtCon1.getInt("mvt_numdoc")
                    + " and pro_codi = " + dtCon1.getInt("pro_codi")
                    + " and deo_ejelot = " + dtCon1.getInt("pro_ejelot")
                    + " and deo_serlot = '" + dtCon1.getString("pro_serlot") + "'"
                    + " and pro_lote = " + dtCon1.getInt("pro_numlot")
                    + " and pro_numind = " + dtCon1.getInt("pro_indlot"));
                precioMvto = precioMedioStock;
            }
            
       
//                if (dtCon1.getString("mvt_tipdoc").equals("R"))
//                {
//                    System.out.println("Reg. "+dtCon1.getDouble("mvt_canti"));  
//                }
            if (dtCon1.getString("mvt_tipo").equals("E"))
            {
                boolean swIgn=false;
                importeStock=kgstock * precioMedioStock;
                double kgStockNuevo=  kgstock + dtCon1.getDouble("mvt_canti");
                if (importeStock + (dtCon1.getDouble("mvt_canti")  * precioMvto)<0.001 || kgStockNuevo< 0.01 )
                {// Estoy con stock en negativo. Ignoro acumulados de precios medios.
                    if (kgstock<0.01)
                         precioMedioStock=precioMvto;  // Si los kilos de stock anteriores son 0 pongo costo de mvto.
                    kgstock = kgStockNuevo;
                    swIgn=true;
                }
                if (importeStock  < 0.001 && !swIgn )
                {
                    kgstock = kgStockNuevo;
                    precioMedioStock=precioMvto;   
                    swIgn=true;
                }
                if (!swIgn)
                {
                    importeStock = kgstock * precioMedioStock;             
                    kgstock += dtCon1.getDouble("mvt_canti");
                    importeStock += dtCon1.getDouble("mvt_canti")
                        * precioMvto;
                    precioMedioStock = kgstock == 0 ? 0 : importeStock / kgstock;
                }
            } else
                kgstock -= dtCon1.getDouble("mvt_canti");
             if (debug)
             {
                System.out.println("fecha: "+Formatear.getFecha(dtCon1.getTimeStamp("mvt_time"),"dd-MM-yyyy HH:mm")+
                "Tipo: "+ dtCon1.getString("mvt_tipdoc")+
                    " kilos: "+dtCon1.getDouble("mvt_canti")+
                    " Precio: "+dtCon1.getDouble("mvt_prec")+
                    " Stock: "+kgstock+" Precio: "+precioMedioStock);
             }
        } while (dtCon1.next());
        if (debug)
        {
            dtAdd.commit();
            msgBox("Registro de log, generado");
            return;
        }
        s = "select eje_nume,deo_codi,sum(deo_kilos*deo_prcost) as costo "
            + " from v_despori as orig WHERE 1=1  " + condWhereOrig
            + " and deo_kilos > 0 "
            + " group by eje_nume,deo_codi "
            + " order by eje_nume,deo_codi ";
        if (!dtCon1.select(s))
        {
            msgBox("No encontrados despieces entre estas fechas");
            return;
        }
        String condWhere = " where def_kilos !=  0 "
            + " and def_preusu != 0 "
            + " and eje_nume = ?"  ;
        String condWher1 = condWhere
            + " AND deo_codi = ? ";
        s = "select pro_codi, def_blkcos, sum(def_kilos) as def_kilos, sum(def_kilos*def_preusu) as def_prcost"
            + " from v_despsal  "
            + condWher1
            + " group by pro_codi,def_blkcos ";
        prstFin = ct.prepareStatement(s);
        s = "select count(distinct pro_codi) as cuantos "
            + " from v_despsal  "
            + condWher1;
        prstFinCuantos = ct.prepareStatement(s);
        s = "select  sum(def_kilos*def_preusu) as def_prcost "
            + " from v_despsal  " + condWher1;
        prstFin1 = ct.prepareStatement(s); // Importe total de productos salida.
        condWher1 += " and pro_codi = ? ";

        s = "update v_despfin set def_prcost = ?"
            + condWher1;
        prstUpd1 = ctUp.prepareStatement(s);
        s = "select * from desporig where eje_nume= ? "
            + " AND deo_codi = ?  ";
        prstDesp = ct.prepareStatement(s);
        swSinCosto = false;
        double impTotAnt = 0;
        int deoCodi = dtCon1.getInt("deo_codi");
        int nRows = 1;
        s = "select count (distinct deo_codi) as cuantos from v_despori as orig  WHERE 1=1  " + condWhereOrig;
        dtStat.select(s);
        int totalRows = dtStat.getInt("cuantos");
        do
        {
            if (!getPopEspere().isBCancelarEnabled())
            {
                ctUp.rollback();
                msgBox("Cancelada actualizacion costos");
                return;
            }
            if ( nRows % 50 ==  0)
            {
              setMensajePopEspere("Regenerando costos. Vuelta "+n+ " de 5\n"+
                  "Recalculando despiece " + dtCon1.getInt("deo_codi") + "  ... " + nRows + " de " + totalRows, false);
            }
            nRows++;
            recalcEntr(dtCon1.getInt("eje_nume"), dtCon1.getInt("deo_codi"), dtCon1.getDouble("costo"));//                   
        } while (dtCon1.next());
    }
    
    private  void addLineaComent(int ejeNume,int deoCodi,String tipoAviso,String aviso)
    {
        ArrayList v=new ArrayList();
        v.add(ejeNume);
        v.add(deoCodi);
        v.add(tipoAviso);
        v.add(aviso);
        jt.addLinea(v);
    }
    /**
     * Recalcular costos de entrada (v_despfin)
     * @param deoCodi Numero de despieces o grupo Despiece
     * @param impTotAnt Importe Total anterior de entrada (Cabecera)
     * @throws SQLException
     */
    private void recalcEntr(int ejeNume,int deoCodi, double impTotAnt) throws SQLException
    {
        if (impTotAnt == 0)
        {
            addLineaComent(ejeNume,deoCodi,"ERR","sin valorar Cabecera. Se ignora");
            return;
        }
        double impTotCalc=impTotAnt;
//        if ( swSinCosto )
//         impTotCalc=impTotAnt;
        prstFin.setInt(1, ejeNume);
        prstFin.setInt(2, deoCodi);

        rsFin = prstFin.executeQuery();
        if (! rsFin.next())
        {
            addLineaComent(ejeNume,deoCodi,"ERR","Sin valorar Lineas. Se ignora");
            return;
        }
        prstFin1.setInt(1, ejeNume);
        prstFin1.setInt(2, deoCodi);
        rsFin1 = prstFin1.executeQuery();
        rsFin1.next();
        double impLinAnt=rsFin1.getDouble("def_prcost"); // Importe TOTAL de Lineas antes de recalc.
//        if ( Math.abs(impLinAnt-impTotAnt)>3)
//        {
//              prstDesp.setInt(1,deoCodi);
//              rsDesp=prstDesp.executeQuery();
//              rsDesp.next();
////              if (rsDesp.getInt("deo_numdes")==0)
////                addLineaComent(deoCodi,"AVI","Hay diferencias de Costo entre entrada y salida : "+
////                            Formatear.format(Math.abs(impLinAnt-impTotAnt),"---,--9.99")
////                            +" :Entrada: "+
////                            Formatear.format(impTotAnt,"---,--9.99")+" :Salidas:  "+
////                            Formatear.format(impLinAnt,"---,--9.99"));
//        }
        prstFinCuantos.setInt(1, ejeNume); 
        prstFinCuantos.setInt(2, deoCodi);                
        rsFin1 = prstFinCuantos.executeQuery();
        rsFin1.next();
        int nRow=rsFin1.getInt("cuantos"); // Numero de lineas 
        HashMap<Integer,ArrayList<Double>> htProd=new HashMap();
        double costo;
        boolean swAjus=false; // Indica si hay algun precio bloqueado
        final int COSTO=0;
        final int KILOS=1;
        final int COSPOR=2;
        final int COSBLO=3;
        final int COSFIN=4;
        // Calculo el % de Costo sobre el costo total original y lo pongo en cada producto
        if (nRow==1)
        {
            ArrayList v=new ArrayList();
            v.add(rsFin.getDouble("def_prcost")/rsFin.getDouble("def_kilos"));
            v.add(rsFin.getDouble("def_kilos") );
            v.add(100);  // Porcentaje de Kilos
            v.add(0);  // Bloqueado
            v.add(0); // Costo Final
            htProd.put(rsFin.getInt("pro_codi"),v);                
        }
        do 
        {
             costo =  rsFin.getDouble("def_prcost");
             if (impLinAnt == 0)
                costo = 0;
             else
                costo = costo / impLinAnt * 100; 
            if (costo <=-100)
                costo=-99.99;
            if (costo>100)
                costo=100;          
            ArrayList v=new ArrayList();
            v.add(rsFin.getDouble("def_prcost")/rsFin.getDouble("def_kilos"));
            v.add(rsFin.getDouble("def_kilos") );
            v.add(costo);          
            v.add(rsFin.getDouble("def_blkcos"));
            v.add(0); // Costo Final
            htProd.put(rsFin.getInt("pro_codi"),v);            
        } while (rsFin.next());
        double totLin=0;
        double totFin=0;
        double totFi1=0;
        boolean swTodoBloq=true;
         for(Map.Entry<Integer, ArrayList<Double>> entry : htProd.entrySet()) 
         {
             ArrayList<Double> v= entry.getValue();
             if (v.get(COSBLO)==0)
             {
                 swTodoBloq=false;
                 break;
             }
         }
        for(Map.Entry<Integer, ArrayList<Double>> entry : htProd.entrySet()) 
        {
            int proCodi = entry.getKey();
            ArrayList<Double> v= entry.getValue();
            costo=impTotCalc * v.get(COSPOR) / 100;
            costo=costo / v.get(KILOS);
             if (v.get(COSBLO)!=0 && !swTodoBloq)
             {
                  swAjus=true;
                  v.set(COSFIN, v.get(COSTO));
                  totFin+=v.get(COSTO)*v.get(KILOS);
                  continue;   
              }

              if (costo >= 0)
              {
                 totFi1+=costo*v.get(KILOS);
                 totFin+=costo*v.get(KILOS);
                 costo= Formatear.redondea(costo,NUMDEC_COSTO);
                 v.set(COSFIN,costo );
                 totLin+= costo*v.get(KILOS);
              }
              htProd.put(proCodi,v);
       }
    
        if (swAjus) // Hay algun precio bloqueado.
        {
              double porc1;
              double dif=impTotCalc-totFin;
              for(Map.Entry<Integer, ArrayList<Double>> entry : htProd.entrySet())         
              {
                  int proCodi = entry.getKey();
                  ArrayList<Double> v= entry.getValue();
                  if (v.get(COSBLO)==0)
                  {
                    totLin = v.get(KILOS) * v.get(COSFIN);
                    porc1 = totLin / totFi1;
                    v.set(COSFIN,(totLin + (porc1 * dif))/v.get(KILOS));
                    htProd.put(proCodi,v);
                  }
                }
        }
        prstUpd = prstUpd1;
        prstUpd.setInt(2, ejeNume);
        prstUpd.setInt(3, deoCodi);
        for(Map.Entry<Integer, ArrayList<Double>> entry : htProd.entrySet())         
        {
            int proCodi = entry.getKey();
            ArrayList<Double> v= entry.getValue();
                  
            prstUpd.setInt(4, proCodi);
            prstUpd.setDouble(1, (double) v.get(COSFIN) );
            prstUpd.executeUpdate();
        }  

       //ctUp.commit();
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modifynet this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        Pprinc = new javax.swing.JPanel();
        Pcondic = new gnu.chu.controles.CPanel();
        feciniE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel1 = new gnu.chu.controles.CLabel();
        fecfinE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel4 = new gnu.chu.controles.CLabel();
        cLabel5 = new gnu.chu.controles.CLabel();
        fecinvE = new gnu.chu.controles.CComboBox();
        Baceptar = new gnu.chu.controles.CButtonMenu(Iconos.getImageIcon("check"));
        cScrollPane1 = new gnu.chu.controles.CScrollPane();
        textoE = new gnu.chu.controles.CTextArea();
        jt = new gnu.chu.controles.Cgrid(4);

        Pprinc.setLayout(new java.awt.GridBagLayout());

        Pcondic.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        Pcondic.setMaximumSize(new java.awt.Dimension(422, 50));
        Pcondic.setMinimumSize(new java.awt.Dimension(422, 50));
        Pcondic.setPreferredSize(new java.awt.Dimension(422, 50));
        Pcondic.setRequestFocusEnabled(false);
        Pcondic.setLayout(null);
        Pcondic.add(feciniE);
        feciniE.setBounds(70, 2, 79, 17);

        cLabel1.setText("De Fecha");
        Pcondic.add(cLabel1);
        cLabel1.setBounds(10, 2, 49, 17);
        Pcondic.add(fecfinE);
        fecfinE.setBounds(210, 2, 79, 17);

        cLabel4.setText("A Fecha");
        Pcondic.add(cLabel4);
        cLabel4.setBounds(160, 2, 44, 17);

        cLabel5.setText("Fecha Inventario");
        Pcondic.add(cLabel5);
        cLabel5.setBounds(10, 22, 90, 17);
        Pcondic.add(fecinvE);
        fecinvE.setBounds(110, 22, 90, 17);

        Baceptar.addMenu("Regenerar");
        Baceptar.addMenu("Anular Reg.");
        Baceptar.setPreferredSize(new java.awt.Dimension(42, 24));
        Baceptar.setText("Aceptar");
        Pcondic.add(Baceptar);
        Baceptar.setBounds(260, 20, 150, 24);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        Pprinc.add(Pcondic, gridBagConstraints);

        cScrollPane1.setMaximumSize(new java.awt.Dimension(402, 77));
        cScrollPane1.setMinimumSize(new java.awt.Dimension(402, 77));
        cScrollPane1.setPreferredSize(new java.awt.Dimension(402, 77));
        cScrollPane1.setQuery(true);

        textoE.setEditable(false);
        textoE.setColumns(20);
        textoE.setRows(5);
        cScrollPane1.setViewportView(textoE);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        Pprinc.add(cScrollPane1, gridBagConstraints);

        ArrayList v=new ArrayList();
        v.add("Ejerc.");
        v.add("Nº Desp.");
        v.add("Tipo");
        v.add("Mensaje");
        jt.setCabecera(v);
        jt.setAnchoColumna(new int[]{40,50,40,250});
        jt.setAlinearColumna(new int[]{2,2,0,0});
        jt.setAjustarGrid(true);
        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Pprinc.add(jt, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButtonMenu Baceptar;
    private gnu.chu.controles.CPanel Pcondic;
    private javax.swing.JPanel Pprinc;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CScrollPane cScrollPane1;
    private gnu.chu.controles.CTextField fecfinE;
    private gnu.chu.controles.CTextField feciniE;
    private gnu.chu.controles.CComboBox fecinvE;
    private gnu.chu.controles.Cgrid jt;
    private gnu.chu.controles.CTextArea textoE;
    // End of variables declaration//GEN-END:variables

}
