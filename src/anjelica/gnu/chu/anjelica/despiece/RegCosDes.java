package gnu.chu.anjelica.despiece;

import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.almacen.MvtosAlma;
import gnu.chu.controles.StatusBar;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
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
 * <p>Copyright: Copyright (c) 2010-2017</p>
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
    private boolean swSinCosto=false;
    private boolean swRegenerar;
    private String condWhereOrig,condWhereGrupo;
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

        this.setVersion("2018-04-08");
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
       
        llenaCombos();
        eje_numeE.setValor(EU.ejercicio);
        activarEventos();
    }
    private void activarEventos() {
       
        Baceptar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("Regenerar"))
                 buscaDatos();
                else
                  resetearDatos();
            }
        });
        jt.addMouseListener(new MouseAdapter()
        {
             @Override
             public void mouseClicked(MouseEvent e) {
                 if (e.getClickCount()>1 && !jt.isVacio())
                     MantDesp.irDespiece(eje_numeE.getValorInt(),jt.getValorInt(jt.getSelectedRowDisab(),0) , jf);
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
    private void llenaCombos()
    {
        try {
            String s="SELECT eje_nume FROM ejercicio where emp_codi = "+EU.em_cod+
                    " and eje_cerrad = 0 ";
            dtStat.select(s);
            eje_numeE.setDatos(dtStat);
        } catch (SQLException k )
        {
            Error ("Error al cargar ejercicio",k);
        }
    }

    private boolean validaCampos()
    {
        
        
        if (feciniE.isNull())
        {
            mensajeErr("Introduzca fecha inicio ");
            feciniE.requestFocus();
            return false;
        }
         if (fecfinE.isNull())
        {
            mensajeErr("Introduzca fecha final ");
            fecfinE.requestFocus();
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
    
        if (!checkDatosDesp()) 
            return;
        
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
                        + " WHERE   eje_nume = " + eje_numeE.getValorInt()
                        + " and def_preusu != 0 "
                        + " AND exists (select deo_codi from v_despori  as orig "
                        + " where orig.deo_codi = v_despfin.deo_codi "
                        + " and orig.eje_nume = v_despfin.eje_nume "
                        + " and orig.deo_numdes = 0 " 
                        + condWhereOrig + ")";
            int nRegAf = stUp.executeUpdate(s);
            setMensajePopEspere("Anulados despieces de Salida sin agrupar: "+nRegAf,false);
            s = "UPDATE v_despfin set def_prcost = def_preusu "
                        + " WHERE   eje_nume = " + eje_numeE.getValorInt()
                        + " and def_preusu != 0 "
                        + " AND exists (select deo_codi from v_despiece  as orig "
                        + " where orig.deo_codi = v_despfin.deo_codi "
                        + " and orig.eje_nume = v_despfin.eje_nume "
                        + " and orig.deo_numdes > 0 " 
                        + condWhereGrupo + ")";
            nRegAf += stUp.executeUpdate(s);
            setMensajePopEspere("Anulados despieces de Salida Agrupados: "+nRegAf,false);
            s = "UPDATE desorilin set deo_prcost = deo_preusu "
                        + " WHERE   eje_nume = " + eje_numeE.getValorInt()
                        + " and deo_preusu != 0 "+
                        "  and exists (select deo_codi from v_despori as orig "+
                        " where orig.deo_codi = desorilin.deo_codi "+
                        " and orig.eje_nume=desorilin.eje_nume "+
                        " and orig.deo_numdes = 0 " +
                         condWhereOrig+")";  
              nRegAf+=dtAdd.executeUpdate(s);
              setMensajePopEspere("Anulados despieces de Entrada sin agrupar: "+nRegAf,false);
              s = "UPDATE desorilin set deo_prcost = deo_preusu "
                        + " WHERE   eje_nume = " + eje_numeE.getValorInt()
                        + " and deo_preusu != 0 "+
                        "  and exists (select deo_codi from v_despiece as orig "+
                        " where orig.deo_codi = desorilin.deo_codi "+
                        " and orig.eje_nume=desorilin.eje_nume "+
                        " and orig.deo_numdes > 0 " +
                         condWhereGrupo+")";  
            nRegAf+=dtAdd.executeUpdate(s);
            dtAdd.commit();
            msgBox("Rescatados precio usuario en  " + nRegAf + " Registros ");
            
        } catch (SQLException k)
        {
            Error("Error al resetear Datos de costos",k);
        }
    }
    private boolean checkDatosDesp() throws SQLException
    {
        if (!validaCampos()) {
            return false;
        }
        condWhereOrig = " and deo_fecha between to_date('" + feciniE.getText() + "','dd-MM-yyyy')"
                + " and to_date('" + fecfinE.getText() + "','dd-MM-yyyy')"
                + (deo_codiE.getValorInt()==0?"":" and (orig.deo_codi = "+deo_codiE.getValorInt() +
                   " or orig.deo_numdes= "+deo_codiE.getValorInt()+")")
                + " and orig.eje_nume = " + eje_numeE.getValorInt();        
        condWhereGrupo= " and grd_fecha between to_date('" + feciniE.getText() + "','dd-MM-yyyy')"
                + " and to_date('" + fecfinE.getText() + "','dd-MM-yyyy')"
                + (deo_codiE.getValorInt()==0?"":" and (orig.deo_codi = "+deo_codiE.getValorInt() +
                   " or orig.deo_numdes= "+deo_codiE.getValorInt()+")")
                + " and orig.eje_nume = " + eje_numeE.getValorInt();   
        
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
                    if (!checkDatosDesp())
                    {
                        resetMsgEspere();
                        return;
                    }
                    setLabelMsgEspere("Buscando despieces ya valorados");
                    String s = "select * from v_despfin as fi,desporig as orig  "
                        + " WHERE  fi.def_preusu !=  0 "
                        + " AND fi.deo_codi =  orig.deo_codi "
                        + " and fi.eje_nume = orig.eje_nume "
                        + condWhereOrig;
                    swRegenerar = true;
                    if (dtStat.select(s))
                    {
                        int res = mensajes.mensajeYesNo("Ya se lanzo la regeneración de costos en este periodo. Lanzarlo de nuevo ?");
                        if (res != mensajes.YES)
                        {
                            msgBox("Abortada la regeneración de costos");
                            resetMsgEspere();
                            return;
                        }
                        swRegenerar = false;
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

    private void buscaDato1() {
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
            
            if (swRegenerar)
            {
             // Primera vez que se regeneran costos.
             // Paso todos los costos al campo de precio de usuario  y pongo el precio costo a 0
                s = "UPDATE v_despfin set def_preusu= def_prcost  "
                        + " WHERE   eje_nume = " + eje_numeE.getValorInt()
                        + " AND exists "
                        + " (select distinct(deo_codi) from v_despori as orig "
                        + "where deo_codi = v_despfin.deo_codi "
                        + " and orig.eje_nume = v_despfin.eje_nume "
                        + " and orig.deo_numdes = 0 "
                        + condWhereOrig + ")";
                int nRegAf = dtAdd.executeUpdate(s);
                s = "UPDATE v_despfin set def_preusu= def_prcost  "
                        + " WHERE   eje_nume = " + eje_numeE.getValorInt()
                        + " AND exists "
                        + " (select deo_codi from v_despiece as orig "
                        + " where deo_codi = v_despfin.deo_codi "
                        + " and orig.eje_nume = v_despfin.eje_nume "
                        + " and orig.deo_numdes > 0 "
                        + condWhereGrupo + ")";
                nRegAf += dtAdd.executeUpdate(s);
                textoE.setText(textoE.getText() + "AVISO: Puestas  " + nRegAf + " lineas de desp. salida con costo = 0");
                s = "UPDATE desorilin set deo_preusu= deo_prcost "
                        + " WHERE   eje_nume = " + eje_numeE.getValorInt()+
                         "  and exists (select orig.deo_codi from v_despori as orig "+
                        " where orig.deo_codi = desorilin.deo_codi "+
                        " and orig.eje_nume=desorilin.eje_nume "+
                        " and orig.deo_numdes = 0 "+
                         condWhereOrig+")";
                nRegAf = dtAdd.executeUpdate(s);
                 s = "UPDATE desorilin set deo_preusu= deo_prcost "
                        + " WHERE   eje_nume = " + eje_numeE.getValorInt()+
                         "  and exists (select orig.deo_codi from v_despiece as orig "+
                        " where orig.deo_codi = desorilin.deo_codi "+
                        " and orig.eje_nume=desorilin.eje_nume "+
                        " and orig.deo_numdes > 0 "+
                        condWhereGrupo+")";
                nRegAf += dtAdd.executeUpdate(s);
                textoE.setText(textoE.getText() + "\n AVISO: Puestas  " + nRegAf + " lineas de desp. Entradas con costo = 0");
            }
            else
            { // NO es la Primera vez que se regeneran costos
               s = "UPDATE v_despfin set def_preusu= def_prcost "
                        + " WHERE   eje_nume = " + eje_numeE.getValorInt()
                        + " and def_preusu = 0 "
                        + " AND exists (select deo_codi from v_despori  as orig "
                        + " where orig.deo_codi = v_despfin.deo_codi "
                        + " and orig.eje_nume = v_despfin.eje_nume "
                        + " and orig.deo_numdes = 0 "
                        + condWhereOrig + ")";
                int nRegAf = stUp.executeUpdate(s);
                s = "UPDATE v_despfin set def_preusu= def_prcost "
                        + " WHERE   eje_nume = " + eje_numeE.getValorInt()
                        + " and def_preusu = 0 "
                        + " AND exists (select deo_codi from v_despiece  as orig "
                        + " where orig.deo_codi = v_despfin.deo_codi "
                        + " and orig.eje_nume = v_despfin.eje_nume "
                        + " and orig.deo_numdes > 0 "
                        + condWhereGrupo + ")";
                nRegAf += stUp.executeUpdate(s);
                textoE.setText(textoE.getText() + "\nAVISO: Puesto precio usuario a precio costo a " + nRegAf + " lineas de desp. salida");
                s = "UPDATE desorilin set deo_preusu= deo_prcost "
                        + " WHERE   eje_nume = " + eje_numeE.getValorInt()
                        + " and deo_preusu = 0 "+
                        "  and exists (select deo_codi from v_despori as orig "+
                        " where orig.deo_codi = desorilin.deo_codi "+
                        " and orig.eje_nume=desorilin.eje_nume "+
                        " and orig.deo_numdes = 0 " +
                         condWhereOrig+")";                
                nRegAf = stUp.executeUpdate(s);
                s = "UPDATE desorilin set deo_preusu= deo_prcost "
                        + " WHERE   eje_nume = " + eje_numeE.getValorInt()
                        + " and deo_preusu = 0 "+
                        "  and exists (select deo_codi from v_despiece as orig "+
                        " where orig.deo_codi = desorilin.deo_codi "+
                        " and orig.eje_nume=desorilin.eje_nume "+
                        " and orig.deo_numdes > 0 " +
                         condWhereGrupo+")";
                nRegAf += stUp.executeUpdate(s);
                textoE.setText(textoE.getText() + "\nAVISO: Puesto precio usuario a precio costo a " + nRegAf + " lineas de desp. Entrada");
           }
         
           for (int n=1;n<6;n++)
           {
             if ( getPopEspere().isBCancelarEnabled())
                buscoDesp(n);
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
            //                + " and pro_codi = 10993 "
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
        double kilos = 0;
        double importe;
        double precio;
        double precioMedio = 0;
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
                kilos = rs.getDouble("kilos");
//                   importe=rs.getDouble("importe");
                precioMedio = rs.getDouble("kilos") == 0 ? 0 : rs.getDouble("importe") / rs.getDouble("kilos");
            }
            precio = dtCon1.getDouble("mvt_prec");
            if (dtCon1.getString("mvt_tipdoc").equals("D"))
            {
                dtAdd.executeUpdate("update desorilin set deo_prcost= " + precioMedio
                    + " where eje_nume=" + dtCon1.getInt("mvt_ejedoc")
                    + " and deo_codi=" + dtCon1.getInt("mvt_numdoc")
                    + " and pro_codi = " + dtCon1.getInt("pro_codi")
                    + " and deo_ejelot = " + dtCon1.getInt("pro_ejelot")
                    + " and deo_serlot = '" + dtCon1.getString("pro_serlot") + "'"
                    + " and pro_lote = " + dtCon1.getInt("pro_numlot")
                    + " and pro_numind = " + dtCon1.getInt("pro_indlot"));
                precio = precioMedio;
            }
//                if (dtCon1.getString("mvt_tipdoc").equals("R"))
//                {
//                    System.out.println("Reg. "+dtCon1.getDouble("mvt_canti"));  
//                }
            if (dtCon1.getString("mvt_tipo").equals("E"))
            {
                if (precioMedio > 0 && kilos > 0)
                    importe = kilos * precioMedio;
                else
                {
                    kilos = 0;
                    importe = 0;
                }
                if (kilos < -0.01)
                {
                    kilos = 0;
                    importe = 0;
                    continue;
                }
                kilos += dtCon1.getDouble("mvt_canti");
                if (precio != 0)
                {
                    importe += dtCon1.getDouble("mvt_canti")
                        * precio;
                    precioMedio = kilos == 0 ? 0 : importe / kilos;
                }
            } else
                kilos -= dtCon1.getDouble("mvt_canti");
        } while (dtCon1.next());

        s = "select deo_codi,sum(deo_kilos*deo_prcost) as costo "
            + " from v_despori as orig WHERE 1=1  " + condWhereOrig
            + " and deo_kilos > 0 "
            + " group by deo_codi "
            + " order by deo_codi ";
        dtCon1.select(s);
        String condWhere = " where def_kilos !=  0 "
            + " and def_preusu != 0 "
            + " and eje_nume = " + eje_numeE.getValorInt();
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
        s = "select * from desporig where eje_nume=" + eje_numeE.getValorInt()
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
            recalcEntr(dtCon1.getInt("deo_codi"), dtCon1.getDouble("costo"));//                   
        } while (dtCon1.next());
    }
    
    private  void addLineaComent(int deoCodi,String tipoAviso,String aviso)
    {
        ArrayList v=new ArrayList();
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
    private void recalcEntr(int deoCodi, double impTotAnt) throws SQLException
    {
        if (impTotAnt == 0)
        {
            addLineaComent(deoCodi,"ERR","sin valorar Cabecera. Se ignora");
            return;
        }
        double impTotCalc=impTotAnt;
//        if ( swSinCosto )
//         impTotCalc=impTotAnt;

        prstFin.setInt(1, deoCodi);

        rsFin = prstFin.executeQuery();
        if (! rsFin.next())
        {
            addLineaComent(deoCodi,"ERR","Sin valorar Lineas. Se ignora");
            return;
        }
        
        prstFin1.setInt(1, deoCodi);
        rsFin1 = prstFin1.executeQuery();
        rsFin1.next();
        double impLinAnt=rsFin1.getDouble("def_prcost"); // Importe TOTAL de Lineas antes de recalc.
        if ( Math.abs(impLinAnt-impTotAnt)>3)
        {
              prstDesp.setInt(1,deoCodi);
              rsDesp=prstDesp.executeQuery();
              rsDesp.next();
              if (rsDesp.getInt("deo_numdes")==0)
                addLineaComent(deoCodi,"AVI","Hay diferencias de Costo entre entrada y salida : "+
                            Formatear.format(Math.abs(impLinAnt-impTotAnt),"---,--9.99")
                            +" :Entrada: "+
                            Formatear.format(impTotAnt,"---,--9.99")+" :Salidas:  "+
                            Formatear.format(impLinAnt,"---,--9.99"));
        }
        prstFinCuantos.setInt(1, deoCodi);                
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
        prstUpd.setInt(2, deoCodi);
        for(Map.Entry<Integer, ArrayList<Double>> entry : htProd.entrySet())         
        {
            int proCodi = entry.getKey();
            ArrayList<Double> v= entry.getValue();
                  
            prstUpd.setInt(3, proCodi);
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
        cLabel3 = new gnu.chu.controles.CLabel();
        feciniE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel1 = new gnu.chu.controles.CLabel();
        fecfinE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel4 = new gnu.chu.controles.CLabel();
        eje_numeE = new gnu.chu.controles.CComboBox();
        cLabel5 = new gnu.chu.controles.CLabel();
        fecinvE = new gnu.chu.controles.CComboBox();
        cLabel6 = new gnu.chu.controles.CLabel();
        deo_codiE = new gnu.chu.controles.CTextField(Types.DECIMAL,"####9");
        Baceptar = new gnu.chu.controles.CButtonMenu(Iconos.getImageIcon("check"));
        cScrollPane1 = new gnu.chu.controles.CScrollPane();
        textoE = new gnu.chu.controles.CTextArea();
        jt = new gnu.chu.controles.Cgrid(3);

        Pprinc.setLayout(new java.awt.GridBagLayout());

        Pcondic.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        Pcondic.setMaximumSize(new java.awt.Dimension(422, 85));
        Pcondic.setMinimumSize(new java.awt.Dimension(422, 85));
        Pcondic.setPreferredSize(new java.awt.Dimension(422, 85));
        Pcondic.setRequestFocusEnabled(false);
        Pcondic.setLayout(null);

        cLabel3.setText("Ejercicio");
        Pcondic.add(cLabel3);
        cLabel3.setBounds(100, 1, 46, 18);
        Pcondic.add(feciniE);
        feciniE.setBounds(79, 24, 79, 17);

        cLabel1.setText("De Fecha");
        Pcondic.add(cLabel1);
        cLabel1.setBounds(12, 24, 49, 17);
        Pcondic.add(fecfinE);
        fecfinE.setBounds(212, 24, 79, 17);

        cLabel4.setText("A Fecha");
        Pcondic.add(cLabel4);
        cLabel4.setBounds(162, 24, 44, 17);
        Pcondic.add(eje_numeE);
        eje_numeE.setBounds(150, 1, 113, 18);

        cLabel5.setText("Fec.Invent. ");
        Pcondic.add(cLabel5);
        cLabel5.setBounds(12, 50, 62, 17);
        Pcondic.add(fecinvE);
        fecinvE.setBounds(78, 50, 128, 17);

        cLabel6.setText("Desp:");
        Pcondic.add(cLabel6);
        cLabel6.setBounds(294, 24, 31, 17);
        Pcondic.add(deo_codiE);
        deo_codiE.setBounds(331, 24, 51, 17);

        Baceptar.addMenu("Regenerar");
        Baceptar.addMenu("Anular Reg.");
        Baceptar.setText("Aceptar");
        Pcondic.add(Baceptar);
        Baceptar.setBounds(230, 50, 150, 26);

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
        v.add("Despiece");
        v.add("Tipo");
        v.add("Mensaje");
        jt.setCabecera(v);
        jt.setAnchoColumna(new int[]{50,40,250});
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
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CScrollPane cScrollPane1;
    private gnu.chu.controles.CTextField deo_codiE;
    private gnu.chu.controles.CComboBox eje_numeE;
    private gnu.chu.controles.CTextField fecfinE;
    private gnu.chu.controles.CTextField feciniE;
    private gnu.chu.controles.CComboBox fecinvE;
    private gnu.chu.controles.Cgrid jt;
    private gnu.chu.controles.CTextArea textoE;
    // End of variables declaration//GEN-END:variables

}
