package gnu.chu.anjelica.inventario;

import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.almacen.MvtosAlma;
import gnu.chu.anjelica.almacen.actStkPart;
import gnu.chu.anjelica.almacen.paregalm;
import gnu.chu.anjelica.almacen.pdmotregu;
import gnu.chu.controles.StatusBar;
import gnu.chu.interfaces.PAD;
import gnu.chu.utilidades.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * <p>Titulo: TrasInven </p>
 * <p>Descripción: Traspasar Inventario. Inserta en tabla de regularizaciones  </p>
 * <p>Copyright: Copyright (c) 2005-2013
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
 * @version 1.0
 */
public class TrasInven extends ventanaPad implements PAD {
   final static String TIPOINVENTARIO="I";
   final static String TIPOSTOCKPARTI="P";
   final static String TIPOSINSTOCKPA="i";
   String s;
   boolean swInvCong;
   int tirCodi;
   actStkPart stkPart;
   paregalm pRegAlm;
    
 public TrasInven(EntornoUsuario eu, Principal p)
 {
   EU = eu;
   vl = p.panel1;
   jf = p;
   eje = true;

   try
   {

     setTitulo("Traspaso Inventarios");
     if (jf.gestor.apuntar(this))
       jbInit();
     else
       setErrorInit(true);
   }
   catch (Exception e)
   {
     Logger.getLogger(TrasInven.class.getName()).log(Level.SEVERE, null, e);
     setErrorInit(true);
   }
 }

    public TrasInven(gnu.chu.anjelica.menu p, EntornoUsuario eu) {
        EU = eu;
        vl = p.getLayeredPane();
        eje = false;

        try {
            setTitulo("Traspaso Inventarios");
           jbInit();
        } catch (Exception e) {
            Logger.getLogger(TrasInven.class.getName()).log(Level.SEVERE, null, e);
            setErrorInit(true);
        }
    }
    private void jbInit() throws Exception {
        statusBar = new StatusBar(this);
        nav = new navegador(this, dtCons, false,navegador.CURYCON);
        iniciarFrame();

        this.setVersion("2013-27-01" );
        strSql = "SELECT emp_codi, alm_codi, cci_feccon FROM coninvcab where emp_codi = "+EU.em_cod+
                " group by emp_codi, alm_codi,cci_feccon "+
            " order by cci_feccon";

        this.getContentPane().add(nav, BorderLayout.NORTH);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        this.setPad(this);
        navActivarAll();
        dtCons.setLanzaDBCambio(false);
        initComponents();
        iniciarBotones(Baceptar, Bcancelar);
        this.setSize(new Dimension(617,381));
        conecta();
       
        activar(false);
    }
    
    @Override
    public void iniciarVentana() throws Exception {

        Pcabe.setDefButton(Baceptar);
        emp_codiE.iniciar(dtStat, this, vl, EU);
        alm_codiE.setFormato(true);
        alm_codiE.setFormato(Types.DECIMAL, "#9", 2);
        s = "SELECT alm_codi,alm_nomb FROM v_almacen "
                + " ORDER BY alm_codi";
        dtStat.select(s);
        alm_codiE.addDatos(dtStat);
        MvtosAlma.llenaComboFecInv(dtStat, EU.em_cod, EU.ejercicio, fecinvConE,32);
        Pprinc.setButton(KeyEvent.VK_F4, Btraspasar);
        emp_codiE.setColumnaAlias("emp_codi");
        alm_codiE.setColumnaAlias("alm_codi");
        cci_fecconE.setColumnaAlias("cci_feccon");
        stkPart = new actStkPart(dtAdd, EU.em_cod);
        stkPart.setVentana(this);
        pRegAlm = new paregalm();
        pRegAlm.iniciar(EU, dtStat, dtAdd, vl, this, dtBloq);
        /**
         * Busco el tipo Mvto de Inventarios
         */
        tirCodi = pdmotregu.getTipoMotRegu(dtStat, "=");
        if (tirCodi == -1) {
            throw new Exception("No encontrado Motivo Regulacion tipo '='");
        }


        nav.btnUltimo.doClick();
        activarEventos();
        //verDatos();
    }

    void activarEventos() {
        Btraspasar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                traspasar();
            }
        });
    }
    void verDatos() {
//        System.out.println("ancho: " + this.getSize().width + " alto: " + this.getSize().height);
        try {
            alm_codiE.setValorInt(dtCons.getInt("alm_codi"));
            cci_fecconE.setText(dtCons.getFecha("cci_feccon"));
            s = "select * from v_regstock where "
                    + "  rgs_fecha = TO_DATE('" + cci_fecconE.getText() + "','dd-MM-yyyy') "
                    + " and tir_codi = " + tirCodi;

            opTrasp.setSelected(dtStat.select(s));
            String condWhere = " c.cci_feccon = to_date('" + cci_fecconE.getText() + "','dd-MM-yyyy')"
                    + " and c.alm_codi = " + alm_codiE.getValorInt()
                    + " and c.emp_codi = " + emp_codiE.getValorInt()
                    + " and c.emp_codi = l.emp_codi "
                    + " and c.cci_codi = l.cci_codi ";
            s = "select pr.pro_artcon from coninvlin as l,coninvcab as c, v_articulo as pr "
                    + " where pr.pro_codi = l.pro_codi "
                    + " and pr.pro_artcon != 0 " + // Buscar prod. Congelados
                    " and " + condWhere;
            opIncCong.setSelected(dtStat.select(s));
            jt.removeAllDatos();
            s = "select cam_codi,dis_nomb,sum(lci_numind) as lci_numind, sum(lci_peso) as lci_peso "
                    + " from  coninvlin as l,coninvcab as c "
                    + " left join v_discrim as a on c.cam_codi = a.dis_codi and c.emp_codi = a.emp_codi "
                    + " and a.dis_tipo = 'AC' "
                    + " where " + condWhere
                    + " group by cam_codi,dis_nomb "
                    + " order by cam_codi ";
            if (!dtCon1.select(s)) {
                msgBox("No encontradas lineas de inventarios");
                return;
            }
            double precio;
            do {
                s = " select sum(lci_peso*ipr_prec) as precio from "
                        + " coninvlin as l,coninvcab as c,invprec as ip "
                        + " where " + condWhere
                        + " and c.cam_codi = '" + dtCon1.getString("cam_codi") + "'"
                        + " and ip.cci_feccon = c.cci_feccon "
                        + " and ip.pro_codi = l. pro_codi ";
                dtStat.select(s);
                precio = dtStat.getDouble("precio", true);
                ArrayList v = new ArrayList();
                v.add(dtCon1.getString("dis_nomb"));
                v.add(dtCon1.getString("lci_numind"));
                v.add(dtCon1.getString("lci_peso"));
                v.add("" + precio);
                jt.addLinea(v);
            } while (dtCon1.next());
            jt.requestFocusInicio();
        } catch (SQLException k) {
            Error("Error al ver datos", k);
        }
    }
    void traspasar()
    {
        if (! checkTraspasar())
            return;
        swInvCong=false;
        if (pro_artconE.getValorInt()==2 && (!opIncCong.isSelected() || opIgnCongel.isSelected()) )
        {
            int res=mensajes.mensajeYesNo("Si traspasa ambos tipos productos (congelado y fresco),"+
                    " cuando no se ha inventariado el congelado, \nse generaran apuntes de inventario para el producto congelado."+
                    " Desea continuar ?");
            if (res!=mensajes.YES)
                return;
            swInvCong=true;
            try {
                if (Formatear.comparaFechas(cci_fecconE.getDate(),
                        Formatear.getDate(fecinvConE.getText(), "dd-MM-yyyy"))<= 0 ) {
                    mensajeErr("Fecha Inventario congelado debe ser inferior a la actual");
                    return;
                }
            } catch (ParseException ex) {
                Error("Error al comparar fechas",ex);
                return;
            }
        }
        mensaje("Insertando   .. Inventario");
        new  miThread("")
        {
            @Override
            public void run()
            {
                TrasInven.this.setEnabled(false);
                traspasar1();
                TrasInven.this.setEnabled(true);
                activaTodo();
            }
        };
    }
    /**
     * Rutina que hace el traspaso en si.
     */
    void traspasar1()
    {
        try {
            mensaje("Insertando apuntes de regularización...");
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY - 1);
            
            if (!tipoTraspE.getValor().equals(TIPOSTOCKPARTI))
            {// No es inventario tipo stock/partidas
                insertaRegul(); // Inserta Apuntes de Regularizacion sobre inventario
                if (swInvCong)
                {
                    stkPart.insInventCong(dtAdd,dtStat,
                            opInsAllAlmac.isSelected()?0:alm_codiE.getValorInt(),fecinvConE.getText(),
                       cci_fecconE.getText(),EU.usuario);
                }
            }
            mensaje("Actualizando Acumulados de Articulos y Stock-Partidas ...");

            int proArtcon = pro_artconE.getValorInt();

            //stkPart.setCamara(cam_codiE.getText());
            if (jf != null) {
                jf.ht.clear();
                jf.ht.put("%f", cci_fecconE.getText());
                jf.ht.put("%a", alm_codiE.getText());
                jf.guardaMens("I4", jf.ht);
            }
            // Ahora busco todos los movimientos y vuelvo a poner los acumulados.
            if (!tipoTraspE.getValor().equals(TIPOSINSTOCKPA)) {
                stkPart.regeneraStock(dtBloq, proArtcon,
                        opInsAllAlmac.isSelected()?0:alm_codiE.getValorInt(),
                        cci_fecconE.getText(),0,opResetear.isSelected());
            }
         
            ctUp.commit();

            mensaje("");
            msgBox("Inventario ... Insertado");
        } catch (Exception ex) {
            Error("Error al Insertar inventario ", ex);
        }

    }

    /**
     * Inserta en la tabla v_regstock las regularizaciones tipo inventario
     *
     * @throws SQLException
     */
    private void insertaRegul() throws Exception {
        mensaje("Insertando Regularizaciones .... A esperar tocan ;-)");
        // Busco los productos en Inventario agrupandolos por Individuo.
        s = "select c.alm_codi, l.pro_codi,prp_ano,l.prp_empcod,prp_seri,prp_part,prp_indi,a.pro_coinst, "
                + " sum(lci_peso) as lci_peso, "
                + " sum(lci_kgsord) as lci_kgsord, "
                + " sum (lci_numind) as lci_numind,i.ipr_prec "
                + "  from coninvlin as l,coninvcab as c,invprec as  i,v_articulo a  "
                + "  where c.cci_codi = l.cci_codi "
                + " and c.emp_codi = l.emp_codi "
                + " and c.emp_codi = " + emp_codiE.getValorInt()
                + " and lci_peso <> 0 "
                + (opIgnCongel.isSelected()? " and a.pro_artcon = 0":"")
               // + " and ipr_prec > 0"
                + " and l.lci_regaut =0 "  // No es apunte automatico
                + (opInsAllAlmac.isSelected()?"": " and c.alm_codi = " + alm_codiE.getValorInt())
                +" and c.cci_feccon = TO_DATE('" + cci_fecconE.getText() + "','dd-MM-yyyy') "
                + " AND i.cci_feccon = TO_DATE('" + cci_fecconE.getText() + "','dd-MM-yyyy') "
                + " and l.pro_codi = i.pro_codi "
                + " and l.pro_codi = a.pro_codi "
                + " group by c.alm_codi,i.ipr_prec,l.pro_codi,prp_ano, "
                + " l.prp_empcod,prp_seri,prp_part,prp_indi,pro_coinst ";

        dtCon1.select(s);
//  debug("(insertaRegul) s: "+dtCon1.getStrSelect());

        int nReg = 1;
        int rgsNume;
        s = "SELECT max(rgs_nume) as rgs_nume FROM v_regstock  ";
        dtStat.select(s);
        rgsNume = dtStat.getInt("rgs_nume", true) + 1;
        int almCodi;
        int sbeCodi = 1;
        double kilosDeposito=0;
        do {
            // Quito los individuos que esten en albaranes de deposito.
            s="SELECT * FROM V_albvenpar as  p, v_albavec as c "+
                    " WHERE c.avc_depos ='D' AND c.emp_codi=p.emp_codi "+
                    " and c.avc_ano=p.avc_ano and c.avc_serie=p.avc_serie "+
                    " and c.avc_nume = p.avc_nume "+
                    " and c.avc_fecalb <= to_date('"+cci_fecconE.getText()+"','dd-MM-yyyy')"+
                    " and p.pro_codi = "+dtCon1.getInt("pro_codi")+
                    " and p.avp_ejelot= "+dtCon1.getInt("prp_ano")+
                    " and p.avp_serlot= '"+ dtCon1.getString("prp_seri")+ "' "+
                    " and p.avp_numpar = "+dtCon1.getInt("prp_part")+
                    " and p.avp_numind = "+dtCon1.getInt("prp_indi"); // No ponemos la empresa,pues esta obsoleta
            if (dtStat.select(s))
            { // Esta puesto en un albaran de deposito. Se ignora
                dtAdd.addNew("invdepos");
                dtAdd.setDato("ind_fecha", cci_fecconE.getDate());
                dtAdd.setDato("pro_codi", dtCon1.getInt("pro_codi"));
                dtAdd.setDato("eje_nume", dtCon1.getInt("prp_ano"));
                dtAdd.setDato("emp_codi", dtCon1.getInt("prp_empcod"));
                dtAdd.setDato("pro_serie", dtCon1.getString("prp_seri"));
                dtAdd.setDato("pro_nupar", dtCon1.getInt("prp_part"));
                dtAdd.setDato("pro_numind",  dtCon1.getInt("prp_indi"));
                dtAdd.setDato("ind_numuni", dtCon1.getInt("lci_numind"));
                dtAdd.setDato("alm_codi", dtCon1.getInt("alm_codi"));
                dtAdd.setDato("ind_kilos", dtCon1.getDouble("lci_peso"));
                dtAdd.update();
                kilosDeposito+=dtCon1.getDouble("lci_peso");
                continue;
            }
            almCodi = dtCon1.getInt("alm_codi");
            pRegAlm.setRegNume(++rgsNume);
            try {
                pRegAlm.insRegistro(cci_fecconE.getFecha("dd-MM-yyyy"), dtCon1.getInt("pro_codi"),
                        dtCon1.getInt("prp_empcod"), dtCon1.getInt("prp_ano"), dtCon1.getString("prp_seri"),
                        dtCon1.getInt("prp_part"), dtCon1.getInt("prp_indi"), dtCon1.getInt("lci_numind"),
                        dtCon1.getDouble("lci_peso"), almCodi, tirCodi, 0, "", dtCon1.getDouble("ipr_prec"),
                        null, 0, sbeCodi, 1, 0, 0, "", 0,0);
            } catch (ParseException ex) {
                throw new SQLException("Error al parsear fecha \n"+ex.getMessage());
            }

            if (++nReg % 10 == 0) {
                mensaje("Insertados: " + nReg + " Registros en Regulacion Stock", false);
            }
        } while (dtCon1.next());
        if (jf != null) {
            jf.ht.clear();
            jf.ht.put("%f", cci_fecconE.getText());
            jf.ht.put("%a", alm_codiE.getText());
            jf.guardaMens("I3", jf.ht);
        }
        debug("Kilos ignorados: "+kilosDeposito);
    }
    public boolean checkTraspasar()
    {
        try {
//            if (tipoTraspE.getValor().equals("I") && opInsAllAlmac.isSelected())
//            {
//                mensajeErr("No se pueden elegir TODOS Los almacenes si el tipo traspaso es Inventario ");
//                return false;
//            }
            if (!tipoTraspE.getValor().equals(TIPOSTOCKPARTI)) { // No es traspaso solo STOCK-PARTIDAS

                s = "select  l.cci_codi,l.pro_codi,c.cam_codi from coninvlin l,coninvcab c "
                        + "  where c.cci_codi = l.cci_codi "
                        + " and c.emp_codi = l.emp_codi "
                        + " and c.emp_codi = " + emp_codiE.getValorInt()
                        + " and lci_peso <> 0 "
                        + " and c.cci_feccon = TO_DATE('" + cci_fecconE.getText() + "','dd-MM-yyyy') "
                        + " and not exists (SELECT * FROM invprec where pro_codi = l.pro_codi "
                        + " and cci_feccon = TO_DATE('" + cci_fecconE.getText() + "','dd-MM-yyyy')) ";
                if (dtCon1.select(s)) {
                    mensajeErr("Existen Productos SIN precio ("+dtCon1.getInt("pro_codi",true)+
                        " en camara "+dtCon1.getString("cam_codi",true)+")");
                    return false;
                }
                s =
                        "select  l.cci_codi,l.pro_codi,c.cam_codi from coninvlin l,coninvcab c "
                        + " where c.cci_codi = l.cci_codi "
                        + " and c.emp_codi = l.emp_codi "
                        + " and c.emp_codi = " + emp_codiE.getValorInt()
                        + " and lci_peso <> 0 "
                        + " and c.cci_feccon = TO_DATE('" + cci_fecconE.getText() + "','dd-MM-yyyy') "
                        + " and exists (SELECT * FROM invprec where pro_codi = l.pro_codi "
                        + " and cci_feccon = TO_DATE('" + cci_fecconE.getText() + "','dd-MM-yyyy')"
                        + " and ipr_prec = 0) ";
                if (dtCon1.select(s)) {
                    int ret=mensajes.mensajeYesNo("Existen productos sin VALORAR (" + dtCon1.getInt("pro_codi")
                            + " en CAMARA: " + dtCon1.getString("cam_codi") + "). ¿ Continuar ?");
                    if (ret!=mensajes.YES)
                        return false;
                }
            }


            /**
             * Busco el tipo Mvto de Inventarios ... otra vez
             */
            tirCodi = pdmotregu.getTipoMotRegu(dtStat, "=");
            if (tirCodi == -1) 
                throw new Exception("No encontrado Motivo Regulacion tipo '='");

            if (!tipoTraspE.getValor().equals(TIPOSTOCKPARTI))
            {
                /**
                 * Comprobar que no hay inventarios para esta fecha.
                 */
                s = "select * from v_regstock where "
                        + "  rgs_fecha = TO_DATE('" + cci_fecconE.getText() + "','dd-MM-yyyy') "
                        + " and tir_codi = " + tirCodi;
                if (dtCon1.select(s)) {
                    int res = mensajes.mensajeYesNo("YA EXISTEN REGISTROS DE"
                            + " INVENTARIOS EN ESTA FECHA.  CONTINUAR ?");
                    if (res != mensajes.YES) {
                        mensajeErr("Inventario NO insertado");
                        return false;
                    }
                    if (jf != null) {
                        jf.ht.clear();
                        jf.ht.put("%f", cci_fecconE.getText());
                        jf.guardaMens("I1", jf.ht);
                    }

                    res = mensajes.mensajeYesNo("Borrar los registros existentes ?");
                    if (res == mensajes.YES) {
                        s = "delete from v_regstock where "
                                + " rgs_fecha = TO_DATE('" + cci_fecconE.getText() + "','dd-MM-yyyy') "
                                + " and tir_codi = " + tirCodi
                                +(opInsAllAlmac.isSelected()?"":
                                    " and alm_codi = " + alm_codiE.getValorInt());
                        dtAdd.executeUpdate(s, stUp);
                        s = "delete from invdepos where "
                                + " ind_fecha = TO_DATE('" + cci_fecconE.getText() + "','dd-MM-yyyy') "
                                +(opInsAllAlmac.isSelected()?"":
                                    " and alm_codi = " + alm_codiE.getValorInt());
                        dtAdd.executeUpdate(s, stUp);
                        if (jf != null) {
                            jf.ht.clear();
                            jf.ht.put("%f", cci_fecconE.getText());
                            jf.guardaMens("I2", jf.ht);
                        }

                    }
                }
            }
        } catch (Exception ex) {
            Error("Error al Comprobar datos para insertar inventario ", ex);
            return false;
        }
        return true;
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
        emp_codiE = new gnu.chu.camposdb.empPanel();
        emp_codiL = new gnu.chu.controles.CLabel();
        cLabel1 = new gnu.chu.controles.CLabel();
        cci_fecconE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel2 = new gnu.chu.controles.CLabel();
        alm_codiE = new gnu.chu.controles.CLinkBox();
        opTrasp = new gnu.chu.controles.CCheckBox();
        opIncCong = new gnu.chu.controles.CCheckBox();
        Baceptar = new gnu.chu.controles.CButton(Iconos.getImageIcon("check"));
        Bcancelar = new gnu.chu.controles.CButton(Iconos.getImageIcon("cancel"));
        jt = new gnu.chu.controles.Cgrid(4);
        Ptraspa = new gnu.chu.controles.CPanel();
        Btraspasar = new gnu.chu.controles.CButton( Iconos.getImageIcon("save"));
        cLabel3 = new gnu.chu.controles.CLabel();
        tipoTraspE = new gnu.chu.controles.CComboBox();
        tipoTraspE.addItem("Inventario",TIPOINVENTARIO);
        tipoTraspE.addItem("Stock/Partidas",TIPOSTOCKPARTI);
        tipoTraspE.addItem("Inventario (Sin Stock/Part.)",TIPOSINSTOCKPA);
        cLabel4 = new gnu.chu.controles.CLabel();
        pro_artconE = new gnu.chu.controles.CComboBox();
        pro_artconE.addItem("Ambos","2");
        pro_artconE.addItem("Fresco","0");
        pro_artconE.addItem("Congelado","-1");
        opResetear = new gnu.chu.controles.CCheckBox();
        cLabel5 = new gnu.chu.controles.CLabel();
        fecinvConE = new gnu.chu.controles.CComboBox();
        opInsAllAlmac = new gnu.chu.controles.CCheckBox();
        opIgnCongel = new gnu.chu.controles.CCheckBox();

        Pprinc.setLayout(new java.awt.GridBagLayout());

        Pcabe.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        emp_codiL.setText("Empresa");

        cLabel1.setText("Fecha Control");

        cci_fecconE.setMaximumSize(new java.awt.Dimension(2147483647, 18));
        cci_fecconE.setMinimumSize(new java.awt.Dimension(10, 18));
        cci_fecconE.setPreferredSize(new java.awt.Dimension(10, 18));

        cLabel2.setText("Almacen");

        alm_codiE.setAncTexto(30);
        alm_codiE.setMaximumSize(new java.awt.Dimension(73, 18));
        alm_codiE.setMinimumSize(new java.awt.Dimension(73, 18));
        alm_codiE.setPreferredSize(new java.awt.Dimension(73, 18));

        opTrasp.setText("Traspasado");

        opIncCong.setText("Incluye congelado");

        Baceptar.setText("Aceptar");

        Bcancelar.setText("Cancelar");

        org.jdesktop.layout.GroupLayout PcabeLayout = new org.jdesktop.layout.GroupLayout(Pcabe);
        Pcabe.setLayout(PcabeLayout);
        PcabeLayout.setHorizontalGroup(
            PcabeLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(PcabeLayout.createSequentialGroup()
                .addContainerGap()
                .add(PcabeLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(PcabeLayout.createSequentialGroup()
                        .add(opTrasp, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(6, 6, 6)
                        .add(opIncCong, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(Baceptar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 129, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(Bcancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 130, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(PcabeLayout.createSequentialGroup()
                        .add(emp_codiL, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(3, 3, 3)
                        .add(emp_codiE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 49, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(cLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(6, 6, 6)
                        .add(cci_fecconE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 79, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(4, 4, 4)
                        .add(cLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(alm_codiE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 194, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        PcabeLayout.setVerticalGroup(
            PcabeLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, PcabeLayout.createSequentialGroup()
                .add(PcabeLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(emp_codiL, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cci_fecconE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(emp_codiE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(alm_codiE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(PcabeLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(PcabeLayout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(PcabeLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(opTrasp, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(opIncCong, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(PcabeLayout.createSequentialGroup()
                        .add(2, 2, 2)
                        .add(PcabeLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(Baceptar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 29, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(Bcancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))
        );

        PcabeLayout.linkSize(new java.awt.Component[] {alm_codiE, cLabel1, cLabel2, cci_fecconE, emp_codiL}, org.jdesktop.layout.GroupLayout.VERTICAL);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        Pprinc.add(Pcabe, gridBagConstraints);

        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jt.setMinimumSize(new java.awt.Dimension(100, 100));
        ArrayList v=new ArrayList();
        v.add("Camara"); //0
        v.add("Unid"); // 1
        v.add("Kilos"); // 2
        v.add("Importe"); // 3
        jt.setCabecera(v);
        jt.setAlinearColumna(new int[]{0,2,2,2});
        jt.setAnchoColumna(new int[]{100,50,80,100});
        jt.setFormatoColumna(1, "##,##9");
        jt.setFormatoColumna(2, "###,##9.9");
        jt.setFormatoColumna(3, "##,###,##9.9");
        jt.setAjustarGrid(true);
        Pprinc.add(jt);

        org.jdesktop.layout.GroupLayout jtLayout = new org.jdesktop.layout.GroupLayout(jt);
        jt.setLayout(jtLayout);
        jtLayout.setHorizontalGroup(
            jtLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 614, Short.MAX_VALUE)
        );
        jtLayout.setVerticalGroup(
            jtLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 179, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 0, 2);
        Pprinc.add(jt, gridBagConstraints);

        Ptraspa.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Ptraspa.setMinimumSize(new java.awt.Dimension(610, 81));
        Ptraspa.setPreferredSize(new java.awt.Dimension(610, 81));
        Ptraspa.setLayout(null);

        Btraspasar.setText("Traspasar");
        Btraspasar.setMargin(new Insets(0,0,0,0));
        Ptraspa.add(Btraspasar);
        Btraspasar.setBounds(492, 49, 106, 19);

        cLabel3.setText("Tipo traspaso");
        Ptraspa.add(cLabel3);
        cLabel3.setBounds(2, 2, 76, 18);

        tipoTraspE.setMaximumSize(new java.awt.Dimension(78, 18));
        tipoTraspE.setMinimumSize(new java.awt.Dimension(78, 18));
        tipoTraspE.setPreferredSize(new java.awt.Dimension(78, 18));
        Ptraspa.add(tipoTraspE);
        tipoTraspE.setBounds(81, 2, 140, 18);

        cLabel4.setText("Traspasar productos");
        Ptraspa.add(cLabel4);
        cLabel4.setBounds(338, 3, 116, 15);

        pro_artconE.setMaximumSize(new java.awt.Dimension(78, 18));
        pro_artconE.setMinimumSize(new java.awt.Dimension(78, 18));
        pro_artconE.setPreferredSize(new java.awt.Dimension(78, 18));
        Ptraspa.add(pro_artconE);
        pro_artconE.setBounds(458, 2, 140, 18);

        opResetear.setSelected(true);
        opResetear.setText("Poner a 0 prod. sin inventariar");
        opResetear.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        Ptraspa.add(opResetear);
        opResetear.setBounds(0, 20, 171, 23);

        cLabel5.setText("Fecha Inv. Congelado");
        Ptraspa.add(cLabel5);
        cLabel5.setBounds(2, 50, 116, 15);

        fecinvConE.setMaximumSize(new java.awt.Dimension(78, 18));
        fecinvConE.setMinimumSize(new java.awt.Dimension(78, 18));
        fecinvConE.setPreferredSize(new java.awt.Dimension(78, 18));
        Ptraspa.add(fecinvConE);
        fecinvConE.setBounds(121, 49, 167, 18);

        opInsAllAlmac.setSelected(true);
        opInsAllAlmac.setText("Insertar Todos Almacenes");
        opInsAllAlmac.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        Ptraspa.add(opInsAllAlmac);
        opInsAllAlmac.setBounds(447, 24, 151, 23);

        opIgnCongel.setText("Ignorar Inventario Congelado ");
        opIgnCongel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        Ptraspa.add(opIgnCongel);
        opIgnCongel.setBounds(200, 20, 200, 23);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        Pprinc.add(Ptraspa, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CButton Bcancelar;
    private gnu.chu.controles.CButton Btraspasar;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CPanel Ptraspa;
    private gnu.chu.controles.CLinkBox alm_codiE;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CTextField cci_fecconE;
    private gnu.chu.camposdb.empPanel emp_codiE;
    private gnu.chu.controles.CLabel emp_codiL;
    private gnu.chu.controles.CComboBox fecinvConE;
    private gnu.chu.controles.Cgrid jt;
    private gnu.chu.controles.CCheckBox opIgnCongel;
    private gnu.chu.controles.CCheckBox opIncCong;
    private gnu.chu.controles.CCheckBox opInsAllAlmac;
    private gnu.chu.controles.CCheckBox opResetear;
    private gnu.chu.controles.CCheckBox opTrasp;
    private gnu.chu.controles.CComboBox pro_artconE;
    private gnu.chu.controles.CComboBox tipoTraspE;
    // End of variables declaration//GEN-END:variables

    public void PADPrimero() {
      verDatos();
    }

    public void PADAnterior() {
         verDatos();
    }

    public void PADSiguiente() {
         verDatos();
    }

    public void PADUltimo() {
         verDatos();  
    }
     public void PADQuery()
  {
         activar(true,navegador.QUERY);
         Pcabe.setQuery(true);
         Pcabe.resetTexto();
         emp_codiE.setValorInt(EU.em_cod);
         mensaje("Introduzca criterios de Busqueda");
  }
    public void ej_query1() {

      Component c;
   if ( (c = Pcabe.getErrorConf()) != null)
   {
     c.requestFocus();
     mensaje("Error en Criterios de busqueda");
     return;
   }


   ArrayList v = new ArrayList();
    v.add(emp_codiE.getStrQuery());
    v.add(alm_codiE.getStrQuery());
    v.add(cci_fecconE.getStrQuery());


   s = "SELECT emp_codi, alm_codi, cci_feccon FROM coninvcab "; // where emp_codi = "+emp_codiE.getValorInt();

    s = creaWhere(s, v,true);
    s+=  " group by emp_codi, alm_codi,cci_feccon "+
            " order by cci_feccon";
    Pcabe.setQuery(false);

//    debug(s);
    try
    {
      if (!dtCons.select(s))
      {
        mensaje("");
        mensajeErr("No encontrados Registros para estos criterios");
        rgSelect();
        activaTodo();
        verDatos();
        return;
      }
      mensaje("");
      strSql = s;
      activaTodo();
      rgSelect();
      verDatos();
      mensajeErr("Nuevos registros selecionados");
    }
    catch (Exception ex)
    {
      fatalError("Error al buscar Inventarios ", ex);
    }
    }

    public void canc_query() {
         activaTodo();
         Pcabe.setQuery(true);
         mensajeErr("Cancelada Consulta");
         mensaje("");
         verDatos();
    }

    public void ej_edit1() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void canc_edit() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void ej_addnew1() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void canc_addnew() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void ej_delete1() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void canc_delete() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public void activar(boolean b) {
        activar(b, navegador.TODOS);
    }

    public void activar(boolean b, int modo) {
        emp_codiE.setEnabled(b);
        cci_fecconE.setEnabled(b);
        alm_codiE.setEnabled(b);
        Ptraspa.setEnabled(!b);
        Bcancelar.setEnabled(b);
        Baceptar.setEnabled(b);
        if (modo == navegador.TODOS) {
            opIncCong.setEnabled(b);
            opTrasp.setEnabled(b);
        }
        jt.setEnabled(b);
    }

}
