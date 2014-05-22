package gnu.chu.anjelica.almacen;

/**
 *
 * <p>Título: paregalm</p>
 * <p>Descripcion: Panel para mantenimientos  Regularizaciones en almacen  </p>
 *  <p>Copyright: Copyright (c) 2005-2014
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
 * <p>Empresa: MISL</p>
 * @author chuchiP
 * @version 2.0 - Fecha 01/03/2007
 */

import gnu.chu.anjelica.compras.pdalbco2;
import gnu.chu.anjelica.despiece.utildesp;
import gnu.chu.camposdb.cliPanel;
import gnu.chu.camposdb.proPanel;
import gnu.chu.camposdb.prvPanel;
import gnu.chu.controles.*;
import gnu.chu.eventos.CambioEvent;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.mensajes;
import gnu.chu.utilidades.ventana;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import javax.swing.BorderFactory;
import javax.swing.SpinnerNumberModel;


public class paregalm extends CPanel {
    int rgsNume=0;
    public static int ESTPEND = 1; // Vert. Proveedor Pend.
    public static int ESTACEP = 2; // Vert. Proveedor
    public static int ESTRECH = 3; // Rechazado
    public static int PENDREC = 4; // PEND.Reclamar.
//  public static int TIRVERPRVPEN=14; // Vertedero Proveedor Pendiente
//  public static int TIRVERPRVACE=13; // Vertedero Proveedor Aceptado
//  public static int TIRVERPRVREC=1; // Vertedero Proveedor Rechazado
    public static int TIRRECPRV = 2;
    utildesp utDesp;
    EntornoUsuario EU;
    DatosTabla dtStat, dtAdd, dtCon1;
    javax.swing.JLayeredPane vl;
    ventana papa;
    String afeStk;
    String s;
    actStkPart stkPart;
    CLabel cLabel8 = new CLabel();
    CTextField deo_serlotE = new CTextField(Types.CHAR, "X");
    CLabel cLabel1 = new CLabel();
    prvPanel prv_codiE = new prvPanel();
    CTextField rgs_comentE = new CTextField();
    CLinkBox tir_codiE = new CLinkBox();
    CLabel cLabel3 = new CLabel();
    CTextField deo_ejelotE = new CTextField(Types.DECIMAL, "###9");
    CLabel cLabel10 = new CLabel();
    CTextField stp_unactE = new CTextField(Types.DECIMAL, "---9");
    proPanel pro_codiE = new proPanel() {

        @Override
        protected void despuesLlenaCampos() {
            stp_unactE.setValorDec(1);
            Baceptar_doClick();
        }

        @Override
        public void afterFocusLost(boolean error) {
            if (error) {
                return;
            }
            if (sbe_codiE.isNull()) {
                sbe_codiE.setValorInt(this.getSubEmpresa());
            }
        }
    };
    CTextField rgs_prebasE = new CTextField(Types.DECIMAL, "---,--9.999");
    CLabel cLabel16 = new CLabel();
    CTextField cci_fecconE = new CTextField(Types.DATE, "dd-MM-yyyy");
    CSpinner cci_horconE = new CSpinner(new SpinnerNumberModel(0,0,23,1));
    CSpinner cci_minconE = new CSpinner(new SpinnerNumberModel(0,0,59,1));
    CLinkBox alm_codiE = new CLinkBox();
    CLabel cLabel9 = new CLabel();
    CLabel cLabel4 = new CLabel();
    CLabel cLabel2 = new CLabel();
    CLabel cLabel7 = new CLabel();
    CLabel cLabel11 = new CLabel();
    CTextField deo_kilosE = new CTextField(Types.DECIMAL, "---,--9.99");
    CLabel cLabel17 = new CLabel();
    CLabel cLabel6 = new CLabel();
    CTextField pro_loteE = new CTextField(Types.DECIMAL, "####9");
    CTextField pro_numindE = new CTextField(Types.DECIMAL, "###9");
    cliPanel cli_codiE = new cliPanel();
    CLabel cli_codiL = new CLabel();
    CTextField deo_emplotE = new CTextField(Types.DECIMAL, "#9");
    CLabel cLabel5 = new CLabel();
    CLabel cLabel12 = new CLabel();
    CTextField rgs_fecresE = new CTextField(Types.DATE, "dd-MM-yy");
    CLabel cLabel13 = new CLabel();
    cliPanel rgs_clidevE = new cliPanel();
    CLabel cli_codiL1 = new CLabel();
    CCheckBox rgs_traspE = new CCheckBox();
    CLabel cLabel14 = new CLabel();
    CLinkBox sbe_codiE = new CLinkBox();
    CLabel cLabel15 = new CLabel();
    CComboBox rgs_recprvE = new CComboBox();
    CLabel cLabel18 = new CLabel();
    CPanel PdatRecl = new CPanel();
    CTextField acc_anoE = new CTextField(Types.DECIMAL, "###9");
    CLabel cLabel19 = new CLabel();
    CLabel cLabel20 = new CLabel();
    CTextField acc_serieE = new CTextField(Types.CHAR, "X");
    CTextField acc_numeE = new CTextField(Types.DECIMAL, "####9");
    CLabel cLabel21 = new CLabel();

    public paregalm() throws Exception {
        jbInit();
    }

    public paregalm(EntornoUsuario eu, DatosTabla dtStat, DatosTabla dtAdd,
            javax.swing.JLayeredPane vl, ventana padre) throws Exception {
        this.EU = eu;
        this.dtStat = dtStat;
        this.dtAdd = dtAdd;
        this.vl = vl;
        papa = padre;
        jbInit();
    }

    private void jbInit() throws Exception {
        this.setLayout(null);
        rgs_comentE.setBounds(new Rectangle(59, 133, 454, 16));

        prv_codiE.setBounds(new Rectangle(63, 79, 340, 16));
        prv_codiE.setAutoNext(false);
        prv_codiE.setAncTexto(40);
        prv_codiE.setVisible(false);
        cLabel1.setBounds(new Rectangle(225, 61, 35, 16));
        cLabel1.setText("Unid.");
        deo_serlotE.setAutoNext(true);
        deo_serlotE.setBounds(new Rectangle(248, 41, 17, 16));
        deo_serlotE.setMayusc(true);
        deo_serlotE.setText("A");
        deo_serlotE.setText("A");
        cLabel8.setBounds(new Rectangle(415, 41, 56, 16));
        cLabel8.setText("Individuo");
        cLabel8.setToolTipText("");
        tir_codiE.setAncTexto(30);
        tir_codiE.setBounds(new Rectangle(237, 2, 282, 17));
        cLabel3.setText("Tipo Mvto.");
        cLabel3.setBounds(new Rectangle(175, 2, 57, 16));
        deo_ejelotE.setBounds(new Rectangle(83, 41, 33, 16));
        deo_ejelotE.setAutoNext(true);
        cLabel10.setText("Precio");
        cLabel10.setBounds(new Rectangle(407, 61, 38, 16));
        stp_unactE.setBounds(new Rectangle(261, 61, 40, 16));
        pro_codiE.setAncTexto(50);
        pro_codiE.setBounds(new Rectangle(83, 21, 430, 18));
        rgs_prebasE.setBounds(new Rectangle(445, 61, 68, 16));
        cLabel16.setText("Coment.");
        cLabel16.setBounds(new Rectangle(1, 133, 56, 16));
        cci_fecconE.setBounds(new Rectangle(45, 2, 60, 16));
        cci_horconE.setBounds(new Rectangle(107,2,35,18));
        cci_minconE.setBounds(new Rectangle(140,2,35,18));
        
        alm_codiE.setAncTexto(30);
        alm_codiE.setBounds(new Rectangle(59, 61, 161, 16));
        cLabel9.setText("Ejercicio");
        cLabel9.setBounds(new Rectangle(32, 41, 47, 15));
        cLabel4.setText("Producto");
        cLabel4.setBounds(new Rectangle(26, 19, 53, 17));
        cLabel2.setText("Fecha");
        cLabel2.setBounds(new Rectangle(7, 2, 52, 16));
        cLabel7.setText("Num. Lote");
        cLabel7.setBounds(new Rectangle(290, 41, 61, 16));
        cLabel11.setText("Almacen");
        cLabel11.setBounds(new Rectangle(4, 61, 50, 16));
        deo_kilosE.setBounds(new Rectangle(335, 61, 67, 16));
        cLabel17.setText("Kilos");
        cLabel17.setBounds(new Rectangle(307, 61, 33, 16));
        cLabel6.setText("Serie");
        cLabel6.setBounds(new Rectangle(218, 41, 33, 16));
        pro_loteE.setAutoNext(true);
        pro_loteE.setBounds(new Rectangle(352, 41, 46, 16));
        pro_numindE.setAutoNext(true);
        pro_numindE.setBounds(new Rectangle(476, 41, 37, 16));
        cli_codiE.setBounds(new Rectangle(59, 79, 340, 16));
        cli_codiL.setText("Cliente");
        cli_codiL.setBounds(new Rectangle(3, 79, 55, 16));
        deo_emplotE.setEnabled(false);
        deo_emplotE.setBounds(new Rectangle(179, 41, 21, 16));
        deo_emplotE.setAutoNext(true);
        cLabel5.setText("Empresa");
        cLabel5.setBounds(new Rectangle(129, 41, 53, 16));
        cLabel12.setBounds(new Rectangle(137, 27, 128, 25));

        rgs_fecresE.setBounds(new Rectangle(457, 79, 56, 16));


        cLabel13.setBounds(new Rectangle(408, 79, 50, 16));
        cLabel13.setToolTipText("Fecha Respuesta");
        cLabel13.setText("Fec.Res");
        rgs_clidevE.setBounds(new Rectangle(81, 97, 432, 16));
        cli_codiL1.setBounds(new Rectangle(5, 97, 74, 16));
        cli_codiL1.setText("Cliente Dev.");
        rgs_traspE.setText("Traspasado");
        rgs_traspE.setBounds(new Rectangle(406, 115, 107, 16));
        cLabel14.setToolTipText("SubEmpresa");
        cLabel14.setText("SubEmp.");
        cLabel14.setBounds(new Rectangle(6, 115, 53, 18));
        sbe_codiE.setAncTexto(25);

        sbe_codiE.setBounds(new Rectangle(59, 115, 325, 16));
        cLabel15.setToolTipText("Reclamado a Proveedor");
        cLabel15.setText("Reclamar Prv.");
        cLabel15.setBounds(new Rectangle(3, 151, 88, 16));
        rgs_recprvE.addItem("No", "0");
        rgs_recprvE.addItem("Pend", "1");
        rgs_recprvE.addItem("Acep", "2");
        rgs_recprvE.addItem("Rech", "3");
        rgs_recprvE.addItem("Recl.Pend", "4");
        rgs_recprvE.setBounds(new Rectangle(97, 152, 74, 16));
        cLabel18.setBackground(Color.red);
        cLabel18.setForeground(Color.white);
        cLabel18.setOpaque(true);
        cLabel18.setText("Alb. Compra");
        cLabel18.setBounds(new Rectangle(2, 1, 75, 16));
        PdatRecl.setBorder(BorderFactory.createLineBorder(Color.black));
        PdatRecl.setBounds(new Rectangle(174, 149, 341, 19));
        PdatRecl.setLayout(null);
        acc_anoE.setAutoNext(true);
        acc_anoE.setBounds(new Rectangle(133, 1, 33, 16));
        cLabel19.setBounds(new Rectangle(82, 1, 47, 15));
        cLabel19.setText("Ejercicio");
        cLabel20.setBounds(new Rectangle(169, 1, 33, 16));
        cLabel20.setText("Serie");
        acc_serieE.setText("A");
        acc_serieE.setMayusc(true);
        acc_serieE.setBounds(new Rectangle(199, 1, 17, 16));
        acc_serieE.setAutoNext(true);
        acc_numeE.setBounds(new Rectangle(285, 2, 46, 16));
        acc_numeE.setAutoNext(true);
        cLabel21.setBounds(new Rectangle(223, 2, 61, 16));
        cLabel21.setText("Num. Lote");
        this.add(cci_fecconE, null);
        this.add(cci_horconE,null);
        this.add(cci_minconE,null);
        this.add(pro_codiE, null);
        this.add(cLabel2, null);
        this.add(cLabel3, null);
        this.add(tir_codiE, null);
        this.add(cLabel4, null);
        this.add(deo_serlotE, null);
        this.add(cLabel6, null);
        this.add(deo_emplotE, null);
        this.add(cLabel5, null);
        this.add(deo_ejelotE, null);
        this.add(cLabel9, null);
        this.add(pro_numindE, null);
        this.add(cLabel8, null);
        this.add(pro_loteE, null);
        this.add(cLabel7, null);
        this.add(rgs_prebasE, null);
        this.add(cLabel10, null);
        this.add(deo_kilosE, null);
        this.add(cLabel11, null);
        this.add(alm_codiE, null);
        this.add(cLabel17, null);
        this.add(stp_unactE, null);
        this.add(cLabel1, null);
        this.add(cli_codiL, null);
        this.add(prv_codiE, null);
        this.add(cli_codiE, null);
        this.add(cLabel13, null);
        this.add(rgs_fecresE, null);
        this.add(cLabel16, null);
        this.add(cLabel14, null);
        this.add(rgs_traspE, null);
        this.add(cli_codiL1, null);
        this.add(rgs_clidevE, null);
        this.add(sbe_codiE, null);
        this.add(rgs_comentE, null);
        this.add(cLabel15, null);
        this.add(PdatRecl, null);
        PdatRecl.add(cLabel18, null);
        PdatRecl.add(acc_anoE, null);
        PdatRecl.add(cLabel19, null);
        PdatRecl.add(acc_serieE, null);
        PdatRecl.add(cLabel20, null);
        PdatRecl.add(acc_numeE, null);
        PdatRecl.add(cLabel21, null);
        this.add(rgs_recprvE, null);
    }

    public void iniciar(EntornoUsuario eu, DatosTabla dtStat, DatosTabla dtAdd,
            javax.swing.JLayeredPane vl, ventana padre, DatosTabla dtCon1) throws
            Exception {
        this.EU = eu;
        this.dtStat = dtStat;
        this.dtAdd = dtAdd;
        this.vl = vl;
        papa = padre;

        iniciar(dtCon1);
    }

    /**
     * Configura los controles
     * @param dtCon1 DatosTabla
     * @throws Exception
     */
    public void iniciar(DatosTabla dtCon1) throws Exception {
        this.dtCon1 = dtCon1;
        deo_ejelotE.setValorDec(EU.ejercicio);
        deo_emplotE.setValorDec(EU.em_cod);
        tir_codiE.setFormato(Types.DECIMAL, "##9", 3);
//     tir_codiE1.setFormato(Types.DECIMAL, "##9", 3);

        s = "SELECT * FROM v_motregu ORDER BY tir_codi";
        if (dtCon1.select(s)) {
            do {
                tir_codiE.addDatos(dtCon1.getString("tir_codi"),
                        dtCon1.getString("tir_nomb") + "  (" + dtCon1.getString("tir_afestk")
                        + ")", true);
            } while (dtCon1.next());
        } else {
            throw new Exception("NO HAY DEFINIDOS TIPOS DE REGULARIZACION. IMPOSIBLE EJECUTAR ESTE PROGRAMA");
        }

        pro_codiE.iniciar(dtStat, papa, vl, EU);
        pro_codiE.setCamposLote(deo_ejelotE, deo_emplotE, deo_serlotE, pro_loteE, pro_numindE,
                deo_kilosE);
        stkPart = new actStkPart(dtAdd, EU.em_cod);
        alm_codiE.setFormato(Types.DECIMAL, "#9", 2);

        s = "SELECT alm_codi,alm_nomb FROM v_almacen ORDER BY alm_codi";
        dtCon1.select(s);
        alm_codiE.addDatos(dtCon1);
        pro_codiE.iniciar(dtStat, papa, vl, EU);
        pro_codiE.setAceptaInactivo(false);
// pro_codiE1.iniciar(dtStat,papa,vl,EU);
        cli_codiE.setAceptaNulo(true);
        cli_codiE.iniciar(dtStat, papa, vl, EU);
        rgs_clidevE.setAceptaNulo(true);
        rgs_clidevE.iniciar(dtStat, papa, vl, EU);
        sbe_codiE.setFormato(Types.DECIMAL, "#9");
        sbe_codiE.setAceptaNulo(true);
        s = "SELECT sbe_codi, sbe_nomb FROM subempresa where emp_codi = "
                + EU.em_cod + " ORDER BY sbe_codi ";
        dtCon1.select(s);
        sbe_codiE.addDatos(dtCon1);
        prv_codiE.setAceptaNulo(true);
        prv_codiE.iniciar(dtStat, papa, vl, EU);
        utDesp = new utildesp();
// QfecIni=feciniE.getText();
// QfecFin=fecfinE.getText();
// QproCodi=pro_codiE1.getText();
// QtirCodi=tir_codiE1.getText();
        activarEventos();
    }

    void activarEventos() {
        stp_unactE.addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
//        if (nav.pulsado == navegador.ADDNEW)
                verKilos();
            }
        });
        tir_codiE.addCambioListener(new gnu.chu.eventos.CambioListener() {

            public void cambio(CambioEvent event) {
                try {
                    if (rgs_recprvE.getValorInt() != 0) {
                        verCliPrv("");
                        return;
                    }

                    verCliPrv();
                    if (!tir_codiE.hasCambio()) {
                        return;
                    }

                    if (tir_codiE.getValorDec() == TIRRECPRV) {
                        rgs_recprvE.setValor(ESTPEND);
                    }
                    tir_codiE.resetCambio();
//          rgs_clidevE.setEnabled(tir_codiE.getValorInt() == pdalbco2.ESTPEND ||
//              tir_codiE.getValorInt() == pdalbco2.ESTACEP ||
//              tir_codiE.getValorInt() == pdalbco2.ESTRECH);
                } catch (Exception k) {
                    papa.Error("Error al Comprobar tipo de Movimiento", k);
                }
            }
        });

        rgs_recprvE.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    if (rgs_recprvE.getValor().equals("0")) {
                        verCliPrv();
                        return;
                    }
                } catch (Exception k) {
                    papa.Error("Error al Comprobar tipo de Movimiento", k);
                }

                verCliPrv("");
                if (acc_anoE.getValorInt() == 0) {
                    acc_anoE.setValorInt(deo_ejelotE.getValorInt());
                    acc_serieE.setText(deo_serlotE.getText());
                    acc_numeE.setValorInt(pro_loteE.getValorInt());
                }
            }
        });

    }

    void actProvCli() throws Exception {
        if (prv_codiE.isVisible() && prv_codiE.getValorInt() == 0) {
            utDesp.busDatInd(deo_serlotE.getText(), pro_codiE.getValorInt(), 0, deo_emplotE.getValorInt(),
                    deo_ejelotE.getValorInt(),
                    pro_loteE.getValorInt(), pro_numindE.getValorInt(), dtCon1, dtStat, EU);
            if (utDesp.prvCompra == 0) {
                mensajeErr("Datos de Compra NO encontrados");
            } else {
                prv_codiE.setText("" + utDesp.prvCompra);
                prv_codiE.controla(false);
            }
        }
        if (cli_codiE.isVisible() && cli_codiE.getValorInt() == 0) {
            int cliCodi = utDesp.busCliVenta(deo_serlotE.getText(), pro_codiE.getValorInt(), deo_emplotE.getValorInt(),
                    deo_ejelotE.getValorInt(),
                    pro_loteE.getValorInt(), pro_numindE.getValorInt(), dtStat, null);
            if (cliCodi == 0) {
                mensajeErr("Datos de Venta NO encontrados");
            } else {
                cli_codiE.setText("" + cliCodi);
                cli_codiE.controlar();
            }

        }
    }

    void mensajeErr(String msgErr) {
        papa.mensajeErr(msgErr);
    }

    boolean verKilos() {
        try {
            if (pro_codiE.isNull() || deo_ejelotE.getValorInt() == 0 || pro_loteE.getValorInt() == 0)
                return false;
            
            actProvCli();

            if (!stkPart.verKilos(dtCon1, deo_emplotE.getValorInt(), deo_ejelotE.getValorInt(),
                    deo_serlotE.getText(), pro_loteE.getValorInt(), pro_numindE.getValorInt(),
                    pro_codiE.getValorInt(), alm_codiE.getValorInt())) {
                mensajeErr("Registro de Stock NO encontrado");
                deo_kilosE.setValorDec(0);
                stp_unactE.setValorDec(0);
                return false;
            }
            deo_kilosE.setValorDec(stkPart.getKilosStk());
            stp_unactE.setValorDec(stkPart.getUnidStk());

            return true;
        } catch (Exception k) {
            papa.Error("Error al buscar entrada", k);
        }
        return false;
    }

    public static String getStrTipRecl(int rgsRecprv) {
        return rgsRecprv == paregalm.ESTPEND ? "P"
                : rgsRecprv == paregalm.ESTACEP ? "A"
                : rgsRecprv == paregalm.PENDREC ? "E" : "R";
    }

    public static String getStrLongTipRecl(int rgsRecprv) {
        return rgsRecprv == paregalm.ESTPEND ? "Pend"
                : rgsRecprv == paregalm.ESTACEP ? "Acep"
                : rgsRecprv == paregalm.PENDREC ? "No Recl" : "Rechaz";
    }

    void verCliPrv() throws SQLException {
        String s = "SELECT * FROM V_motregu WHERE tir_codi = " + tir_codiE.getValorInt();
        if (!dtStat.select(s)) {
            return;
        }
        verCliPrv(dtStat.getString("tir_tipo", true));
    }

    void verCliPrv(String tirTipo) {
        if (rgs_recprvE.getValorInt() != 0) {
            cli_codiE.setVisible(false);
            prv_codiE.setVisible(true);
        } else {
            cli_codiE.setVisible(tirTipo.equals("VC") || tirTipo.equals("MC"));
            prv_codiE.setVisible(tirTipo.equals("VP"));
        }
        cli_codiL.setText("");
        if (cli_codiE.isVisible()) {
            cli_codiL.setText("Cliente");
        }
        if (prv_codiE.isVisible()) {
            cli_codiL.setText("Proveedor");
        }
    }

    public boolean getDatosReg(DatosTabla dt, int rgsNume) throws SQLException {
        String s = "SELECT r.*,m.tir_tipo FROM v_regstock as r,v_motregu as m "
                + " where rgs_nume = " + rgsNume
                + " and r.tir_codi = m.tir_codi ";
        return dt.select(s);
    }
    /**
     * Muestra en pantalla los datos del registro mandado
     * @param rgsNume
     * @throws Exception 
     */
    void verDatReg(int rgsNume) throws Exception {

        if (!getDatosReg(dtCon1, rgsNume)) {
            this.resetTexto();
            return;
        }
        cci_fecconE.setText(dtCon1.getFecha("rgs_fecha", "dd-MM-yyyy"));
        cci_horconE.setValue(new Integer(new SimpleDateFormat("HH").format(dtCon1.getTimeStamp("rgs_fecha"))));
        cci_minconE.setValue(new Integer(new SimpleDateFormat("mm").format(dtCon1.getTimeStamp("rgs_fecha"))));
        tir_codiE.setText(dtCon1.getString("tir_codi"));
        pro_codiE.setText(dtCon1.getString("pro_codi"));
        deo_ejelotE.setText(dtCon1.getString("eje_nume"));
        deo_emplotE.setText(dtCon1.getString("emp_codi"));
        deo_serlotE.setText(dtCon1.getString("pro_serie"));
        pro_loteE.setText(dtCon1.getString("pro_nupar"));
        pro_numindE.setText(dtCon1.getString("pro_numind"));
        alm_codiE.setText(dtCon1.getString("alm_codi"));
        stp_unactE.setText(dtCon1.getString("rgs_canti"));
        deo_kilosE.setText(dtCon1.getString("rgs_kilos"));
        rgs_prebasE.setText(dtCon1.getString("rgs_prebas"));
        verCliPrv(dtCon1.getString("tir_tipo", true));
        if (cli_codiE.isVisible()) {
            cli_codiE.setText(dtCon1.getString("rgs_cliprv"));
        }
        if (prv_codiE.isVisible()) {
            prv_codiE.setText(dtCon1.getString("rgs_cliprv"));
        }
        rgs_clidevE.setValorInt(dtCon1.getInt("rgs_clidev"));
        sbe_codiE.setText(dtCon1.getString("sbe_codi"));
        rgs_traspE.setSelected(dtCon1.getInt("rgs_trasp") != 0);
        rgs_comentE.setText(dtCon1.getString("rgs_coment"));
        rgs_fecresE.setText(dtCon1.getFecha("rgs_fecres", "dd-MM-yy"));
        rgs_recprvE.setValor(dtCon1.getInt("rgs_recprv"));
        acc_numeE.setValorDec(dtCon1.getInt("acc_nume", true));
        acc_anoE.setValorDec(dtCon1.getInt("acc_ano", true));
        acc_serieE.setText(dtCon1.getString("acc_serie"));
    }

    protected void Baceptar_doClick() {
    }

    public int getNumReg() {
        return rgsNume;
    }

    public static String actAfeStk(DatosTabla dt, int tirCodi) throws SQLException
    {
        String s = "SELECT * FROM v_motregu WHERE tir_codi =" + tirCodi;
        if (!dt.select(s)) {
            return null;
        }
        return dt.getString("tir_afestk");
        //afeStk = dtStat.getString("tir_afestk");
//    return true;
    }

    public int insRegistro() throws Exception {

        return insRegistro(cci_fecconE.getFecha("yyyy-MM-dd")+" "+
            Formatear.format(cci_horconE.getText(),"99")+":"+
            Formatear.format(cci_minconE.getText(),"99")
            , pro_codiE.getValorInt(), deo_emplotE.getValorInt(),
                deo_ejelotE.getValorInt(), deo_serlotE.getText(), pro_loteE.getValorInt(),
                pro_numindE.getValorInt(), stp_unactE.getValorInt(),
                deo_kilosE.getValorDec(), alm_codiE.getValorInt(), tir_codiE.getValorInt(),
                cli_codiE.isVisible() ? cli_codiE.getValorInt() : prv_codiE.getValorInt(),
                rgs_comentE.getText(), rgs_prebasE.getValorDec(), rgs_fecresE.getDate(),
                rgs_clidevE.getValorInt(), sbe_codiE.getValorInt(),
                rgs_traspE.isSelected() ? 1 : 0, rgs_recprvE.getValorInt(),
                acc_anoE.getValorInt(), acc_serieE.getText(), acc_numeE.getValorInt(),rgsNume);

    }

    /**
     * Inserta en la tabla v_regstock los valores mandados como parametros
     * @param fecmvto Formato yyyy-MM-dd HH:mm (formato para pasarlo como {ts ..} en jdbc)
     * @param proCodi
     * @param empCodi
     * @param ejeNume
     * @param serie
     * @param numPar
     * @param numInd
     * @param unAct
     * @param kilos
     * @param almCodi
     * @param tirCodi
     * @param rgsCliprv
     * @param coment
     * @param precbas
     * @param fecres
     * @param cliDev
     * @param sbeCodi
     * @param rgsTrasp
     * @param rgsRecPrv
     * @param accAno
     * @param accSerie
     * @param accNume
     * @param rgsNume
     * @return
     * @throws SQLException
     */
    public int insRegistro(String fecmvto, int proCodi, int empCodi, int ejeNume,
            String serie, int numPar, int numInd, int unAct, double kilos,
            int almCodi, int tirCodi, int rgsCliprv, String coment,
            double precbas, java.util.Date fecres, int cliDev, int sbeCodi, int rgsTrasp,
            int rgsRecPrv, int accAno, String accSerie, int accNume,int rgsNume) throws SQLException {
        double kilosMvt = kilos;
        int unActMvt = unAct;

        afeStk=actAfeStk(dtStat, tirCodi);
        if (!afeStk.equals("=") && rgsTrasp != 0) { // No es Inventario
            if (afeStk.equals("-")) {
                kilosMvt = kilos * -1;
                unActMvt = unAct * -1;
            }
            // Actualizo las tabla de Stock/Partidas
            stkPart.setEmpresa(empCodi);
            // suma en stock-partidas el movimiento
            if (!stkPart.sumar(ejeNume, serie,
                    numPar, numInd,
                    proCodi, almCodi,
                    kilosMvt, unActMvt,
                    fecmvto)) {
                return -1;
            }
        }
        return insRegularizacion(dtAdd, fecmvto, proCodi, empCodi, ejeNume,
                serie, numPar, numInd, unAct, kilos,
                almCodi, tirCodi, rgsCliprv, coment,
                precbas, fecres, cliDev, sbeCodi, rgsTrasp,
                rgsRecPrv, accAno, accSerie, accNume, EU.usuario,rgsNume);
    }
    public static int insRegStock(DatosTabla dtAdd, String fecmvto, int proCodi, int empCodi, int ejeNume,
            String serie, int numPar, int numInd, int unAct, double kilos,
            int almCodi, int tirCodi, String coment,
            double precbas,String usuNomb,int rgsNume) throws SQLException {
        return insRegularizacion(dtAdd, fecmvto, proCodi, empCodi, ejeNume, serie, numPar, numInd, unAct, kilos, almCodi,
                tirCodi, 0, coment, precbas, null, 0,1,1,0,0,"",0,usuNomb,rgsNume);
    }
    /**
     * Inserta mvto. Regularizacion
     * @param dtAdd
     * @param fecmvto Formato yyyy-MM-dd HH:mm (formato para pasarlo como {ts ..} en jdbc)
     * @param proCodi
     * @param empCodi
     * @param ejeNume
     * @param serie
     * @param numPar
     * @param numInd
     * @param unAct
     * @param kilos
     * @param almCodi
     * @param tirCodi
     * @param rgsCliprv
     * @param coment
     * @param precbas
     * @param fecres
     * @param cliDev
     * @param sbeCodi
     * @param rgsTrasp
     * @param rgsRecPrv
     * @param accAno
     * @param accSerie
     * @param accNume
     * @param usuNomb
     * @return
     * @throws SQLException 
     */
    public static int insRegularizacion(DatosTabla dtAdd, String fecmvto, int proCodi, int empCodi, int ejeNume,
            String serie, int numPar, int numInd, int unAct, double kilos,
            int almCodi, int tirCodi, int rgsCliprv, String coment,
            double precbas, java.util.Date fecres, int cliDev, int sbeCodi, int rgsTrasp,
            int rgsRecPrv, int accAno, String accSerie, int accNume, String usuNomb, int rgsNume) throws SQLException {
      
        if (rgsNume==0)
        {
            String s = "SELECT MAX(rgs_nume) as rgs_nume FROM v_regstock";
            dtAdd.select(s);
            rgsNume = dtAdd.getInt("rgs_nume", true) + 1;
        }
        
        dtAdd.addNew("v_regstock");
        dtAdd.setDato("pro_codi", proCodi);
        dtAdd.setDato("rgs_fecha", "{ts '"+fecmvto+"'}");
//        dtAdd.setDato("rgs_hora", fecmvto, "dd-MM-yyyy");
        dtAdd.setDato("rgs_nume", rgsNume);
        dtAdd.setDato("eje_nume", ejeNume);
        dtAdd.setDato("emp_codi", empCodi);
        dtAdd.setDato("pro_serie", serie);
        dtAdd.setDato("pro_nupar", numPar);
        dtAdd.setDato("pro_numind", numInd);
        dtAdd.setDato("tir_codi", tirCodi);
        dtAdd.setDato("rgs_canti", unAct);
        dtAdd.setDato("alm_codi", almCodi);
        dtAdd.setDato("rgs_recprv", rgsRecPrv); // Reclamado A proveedor
        dtAdd.setDato("sbe_codi", sbeCodi);
        dtAdd.setDato("rgs_partid ", 1);
        dtAdd.setDato("usu_nomb", usuNomb);
        dtAdd.setDato("rgs_prebas", precbas);
        dtAdd.setDato("rgs_prmeco", precbas);
        dtAdd.setDato("rgs_prulco", precbas); // Buscamos el precio Ul.Compra ?
        dtAdd.setDato("rgs_prregu", precbas);

        dtAdd.setDato("rgs_kilos", kilos);
        dtAdd.setDato("rgs_coment", coment);
        dtAdd.setDato("rgs_cliprv", rgsCliprv==0?cliDev:rgsCliprv);

        dtAdd.setDato("rgs_clidev", cliDev);
        dtAdd.setDato("rgs_kilant", 0);
        dtAdd.setDato("rgs_trasp", rgsTrasp);
        dtAdd.setDato("rgs_fecres", fecres);
        dtAdd.setDato("acc_ano", accAno);
        dtAdd.setDato("acc_serie", accSerie);
        dtAdd.setDato("acc_nume", accNume);
        dtAdd.update();
//    rgsNume = 0;
        return rgsNume;
    }

    public boolean checkCampos() {
        try {
            if (!pro_codiE.controla(true)) {
                mensajeErr(pro_codiE.getMsgError());
                return false;
            }
            if (!tir_codiE.controla()) {
                mensajeErr("Tipo de Movimiento NO VALIDO");
                return false;
            }
            if (!alm_codiE.controla()) {
                mensajeErr("Almacen NO VALIDO");
                return false;
            }
            if (cli_codiE.isVisible()) {
                if (!cli_codiE.controlar()) {
                    cli_codiE.requestFocus();
                    mensajeErr("Cliente NO valido");
                    return false;
                }
            }
            if (prv_codiE.isVisible()) {
                if (!prv_codiE.controlar()) {
                    prv_codiE.requestFocus();
                    mensajeErr("Proveedor NO valido");
                    return false;
                }
            }
            if (!rgs_clidevE.controlar()) {
                rgs_clidevE.requestFocus();
                mensajeErr("Cliente que provoco devolucion No valido");
                return false;
            }

            afeStk = actAfeStk(dtStat, tir_codiE.getValorInt());

            if (!afeStk.equals("=")) {
                if (stp_unactE.getValorInt() == 0) {
                    mensajeErr("Introduzca Unidades de Regularizacion");
                    stp_unactE.requestFocus();
                    return false;
                }
                if (deo_kilosE.getValorDec() == 0) {
                    mensajeErr("Introduzca Kilos de Regularizacion");
                    deo_kilosE.requestFocus();
                    return false;
                }
                if (rgs_traspE.isSelected() && pro_codiE.hasControlIndiv() )
                {
                    if (!stkPart.verKilos(dtStat, deo_emplotE.getValorInt(),
                            deo_ejelotE.getValorInt(), deo_serlotE.getText(),
                            pro_loteE.getValorInt(), pro_numindE.getValorInt(),
                            pro_codiE.getValorInt(), alm_codiE.getValorInt())) {
                        mensajeErr("Este individuo NO tiene Registro inicial en STOCK");
                        return false;
                    }
                    if (stkPart.getKilosStk()<=0 && afeStk.equals("-"))
                    {
                        int res=mensajes.mensajeYesNo("Este individuo No tiene suficiente stock (Stock Actual: "+stkPart.getKilosStk()+") Lo dejara en NEGATIVO. CONTINUAR?");
                        if (res!=mensajes.YES)
                            return false;
                        papa.enviaMailError("Creada Regularización sobre Individuo sin stock. Producto: "+
                                pro_codiE.getValorInt()+" Individuo:"+deo_ejelotE.getValorInt()+deo_serlotE.getText()+pro_numindE.getValorInt()+
                                " en Almacen: "+alm_codiE.getValorInt());
                    }
                }
            }
            if (!rgs_recprvE.getValor().equals("0")) {
                if (acc_anoE.getValorInt() == 0) {
                    mensajeErr("Introduzca Ejercicio de Albarán de Compra");
                    acc_anoE.requestFocus();
                    return false;
                }
                if (acc_serieE.isNull()) {
                    mensajeErr("Introduzca Serie Albarán de Compra");
                    acc_serieE.requestFocus();
                    return false;
                }
                if (acc_numeE.getValorInt() == 0) {
                    mensajeErr("Introduzca Número Albarán de Compra");
                    acc_numeE.requestFocus();
                    return false;
                }
                if (!pdalbco2.getCabeceraAlb(deo_emplotE.getValorInt(),
                        acc_anoE.getValorInt(), acc_serieE.getText(),
                        acc_numeE.getValorInt(), dtStat)) {
                    mensajeErr("Albaran NO existe");
                    acc_anoE.requestFocus();
                    return false;
                }
                if (prv_codiE.getValorInt() != dtStat.getInt("prv_codi")) {
                    mensajeErr("Este albaran NO pertenece a este Proveedor");
                    prv_codiE.setValorInt(dtStat.getInt("prv_codi"));
                    prv_codiE.requestFocus();
                    return false;
                }
            }
            return true;
        } catch (Exception k) {
            papa.Error("Error al Generar Movimiento", k);
            return false;
        }
    }

    /**
     * Borra registro de Regularizacion. En el cursor dtADD debe
     * estar selecionando el registro a borrar (de la tabla v_regstock)
     *
     * @param kilos double Kilos Originales
     * @param unAct int Unidades Originales
     * @param tirCodi int Tipo de Regularizacion
     * @throws SQLException En caso error DB
     */
    public void borrarRegistro(double kilos, int unAct, int tirCodi) throws SQLException {
        int rgsTrasp = dtAdd.getInt("rgs_trasp");
        dtAdd.delete(); // Borro el registro de v_regstock

        afeStk = actAfeStk(dtStat, tirCodi);

        if (afeStk.equals("=") || rgsTrasp == 0) {
            return;
        }

        if (afeStk.equals("-")) {
            kilos = kilos * -1;
            unAct = unAct * -1;
        }
        stkPart.setEmpresa(deo_emplotE.getValorInt());
        stkPart.restar(deo_ejelotE.getValorInt(), deo_serlotE.getText(),
                pro_loteE.getValorInt(), pro_numindE.getValorInt(),
                pro_codiE.getValorInt(), alm_codiE.getValorInt(),
                kilos, unAct);

    }

    public void setSbeCodi(int sbeCodi) {
        sbe_codiE.setValorInt(sbeCodi);
    }

    public void setRgsCliDev(int rgsClidev) {
        rgs_clidevE.setValorInt(rgsClidev);
    }

    /**
     * Borro el registro de v_regstock y resto movimientos
     * @param rgsNume int Numero de Registro
     * @throws SQLException Error en Base de datos
     * @return boolean true si se borro el registro.
     *         false si no se encontro.
     */
    public boolean borrarRegistro(int rgsNume) throws SQLException {
        double kilos;
        int unAct, nRowAf;
        s = "select * from v_regstock WHERE rgs_nume = " + rgsNume;
        if (!dtAdd.select(s, true)) {
            return false;
        }
        afeStk = actAfeStk(dtStat, dtAdd.getInt("tir_codi"));

        if (afeStk.equals("=") || dtAdd.getInt("rgs_trasp") == 0) { // Es de Inventario
            nRowAf = dtAdd.delete(); // SOLO Borro el Registro Regularizaci�n.
            return nRowAf > 0;
        }
        kilos = dtAdd.getDouble("rgs_kilos", true);
        unAct = dtAdd.getInt("rgs_canti", true);
        if (afeStk.equals("-")) {
            kilos = kilos * -1;
            unAct = unAct * -1;
        }

        stkPart.setEmpresa(deo_emplotE.getValorInt());
        stkPart.restar(dtAdd.getInt("eje_nume"), dtAdd.getString("pro_serie"),
                dtAdd.getInt("pro_nupar"), dtAdd.getInt("pro_numind"),
                dtAdd.getInt("pro_codi"), dtAdd.getInt("alm_codi"),
                kilos, unAct);
        s = "DELETE FROM v_regstock WHERE rgs_nume = " + rgsNume;
        nRowAf = dtAdd.executeUpdate(s); // Borro el registro de v_regstock
        return nRowAf > 0;
    }

    /**
     * Establece el Numero de Registro para v_regstock (rgs_nume)
     * @param rgsNumero int Numero Registro
     */
    public void setRegNume(int rgsNumero) {
        rgsNume = rgsNumero;
    }

    void setActivo(boolean b) {
        cci_fecconE.setEnabled(b);
        cci_horconE.setEnabled(b);
        cci_minconE.setEnabled(b);
        tir_codiE.setEnabled(b);
        pro_codiE.setEnabled(b);
        deo_ejelotE.setEnabled(b);
        deo_emplotE.setEnabled(b);
        deo_serlotE.setEnabled(b);
        pro_loteE.setEnabled(b);
        pro_numindE.setEnabled(b);
        alm_codiE.setEnabled(b);
        stp_unactE.setEnabled(b);
        deo_kilosE.setEnabled(b);
        rgs_prebasE.setEnabled(b);
        cli_codiE.setEnabled(b);
        rgs_comentE.setEnabled(b);
        prv_codiE.setEnabled(b);
        rgs_clidevE.setEnabled(b);
        sbe_codiE.setEnabled(b);
        rgs_traspE.setEnabled(b);
        rgs_recprvE.setEnabled(b);
        PdatRecl.setEnabled(b);
    }

    void addNew() {
        this.resetTexto();
        tir_codiE.resetCambio();
        deo_ejelotE.setValorInt(EU.ejercicio);
        deo_serlotE.setText("A");
        deo_emplotE.setValorDec(EU.em_cod);
        rgs_clidevE.setEnabled(true);
        rgs_traspE.setSelected(true);
        cci_fecconE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
        cci_minconE.setText("0");
        cci_horconE.setText("0");
        cci_fecconE.requestFocus();
    }

    void PADEdit() {
        tir_codiE.resetCambio();
//      rgs_clidevE.setEnabled(tir_codiE.getValorInt() == pdalbco2.ESTPEND ||
//              tir_codiE.getValorInt() == pdalbco2.ESTACEP ||
//              tir_codiE.getValorInt() == pdalbco2.ESTRECH);
    }

    public void setCampos(java.util.Date fecmvto, int proCodi, int empCodi, int ejeNume, String serie, int numPar, int numInd,
            int numUni, double canti,
            int almCodi, int tirCodi, int cliCodi, String coment,
            double precbas, java.util.Date fecres) throws Exception {
        setCampos(fecmvto, proCodi, empCodi, ejeNume, serie, numPar, numInd, numUni, canti,
                almCodi, tirCodi, cliCodi, coment, precbas, fecres,
                0, 0, 1, 0);
    }

    public void setCampos(java.util.Date fecmvto, int proCodi, int empCodi, int ejeNume, String serie, int numPar, int numInd, int numUni, double canti,
            int almCodi, int tirCodi, int cliCodi, String coment,
            double precbas, java.util.Date fecres, int cliDev, int sbeCodi, int rgsTrasp, int rgsRecPrv) throws Exception {
        setCampos(fecmvto, proCodi, empCodi, ejeNume, serie, numPar, numInd, numUni, canti,
                almCodi, tirCodi, cliCodi, coment, precbas, fecres,
                cliDev, sbeCodi, rgsTrasp, rgsRecPrv, 0, "", 0);

    }

    public void setCampos(java.util.Date fecmvto, int proCodi, int empCodi, int ejeNume, String serie, int numPar, int numInd, int numUni, double canti,
            int almCodi, int tirCodi, int cliCodi, String coment,
            double precbas, java.util.Date fecres, int cliDev, int sbeCodi, int rgsTrasp, int rgsRecPrv,
            int accAno, String accSerie, int accNume) throws Exception {
        pro_codiE.setValorInt(proCodi);
        pro_codiE.controla(false);
        deo_emplotE.setValorInt(empCodi);
        deo_ejelotE.setValorInt(ejeNume);
        deo_serlotE.setText(serie);
        pro_loteE.setValorInt(numPar);
        pro_numindE.setValorInt(numInd);
        stp_unactE.setValorInt(numUni);
        deo_kilosE.setValorDec(canti);
        alm_codiE.setText("" + almCodi);
        tir_codiE.setText("" + tirCodi);
        cci_fecconE.setDate(fecmvto);
        cci_horconE.setText(new SimpleDateFormat("HH").format(fecmvto));
        cci_minconE.setText(new SimpleDateFormat("mm").format(fecmvto));
        rgs_comentE.setText(coment);
        cli_codiE.setValorInt(cliCodi);
        prv_codiE.setValorInt(cliCodi);
        rgs_prebasE.setValorDec(precbas);
        rgs_fecresE.setDate(fecres);
        rgs_clidevE.setValorInt(cliDev);
        sbe_codiE.setValorInt(sbeCodi);
        rgs_traspE.setSelected(rgsTrasp != 0);
        rgs_recprvE.setValor(rgsRecPrv);
        acc_anoE.setValorInt(accAno);
        acc_serieE.setText(accSerie);
        acc_numeE.setValorInt(accNume);
    }
}
