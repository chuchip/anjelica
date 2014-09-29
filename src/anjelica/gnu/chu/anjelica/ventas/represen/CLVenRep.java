package gnu.chu.anjelica.ventas.represen;

import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.pad.MantRepres;
import gnu.chu.anjelica.pad.MantTarifa;
import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.anjelica.ventas.MantPrAlb;
import gnu.chu.anjelica.ventas.pdalbara;
import gnu.chu.controles.StatusBar;
import gnu.chu.eventos.CambioEvent;
import gnu.chu.eventos.CambioListener;
import gnu.chu.sql.DatosTabla;
import gnu.chu.sql.conexion;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.ventana;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

/*
 *<p>Titulo: CLVenRep </p>
 * <p>Descripción: Consulta Listado Ventas a Representantes</p>
 * Este programa saca los margenes sobre el precio de tarifa entre unas fechas
 * y para una zona/Representante dada.
 * Tambien permite sacar una relacion de los albaranes, que no tienen precio de tarifa
 * puestos, dando la opción de actualizarlos.
 * Created on 03-dic-2009, 22:41:09
 *
 * <p>Copyright: Copyright (c) 2005-2010
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
 */
public class CLVenRep extends ventana {
    String condAlb;
    String ARG_ZONAREP = "";
    boolean ARG_MODIF=false;
    DatosTabla dtAdd;
    String s;

    public CLVenRep(EntornoUsuario eu, Principal p) {
        this(eu, p, null);
    }

    public CLVenRep(EntornoUsuario eu, Principal p, Hashtable ht) {
        EU = eu;
        vl = p.panel1;
        jf = p;
        eje = true;

        try {
            if (ht != null) {
                if (ht.get("zonaRep") != null) 
                    ARG_ZONAREP = ht.get("zonaRep").toString();
               if (ht.get("modif") != null)
                    ARG_MODIF = new Boolean(ht.get("modif").toString()).booleanValue();
            }
            setTitulo("Consulta Ventas Representantes");
            if (jf.gestor.apuntar(this)) {
                jbInit();
            } else {
                setErrorInit(true);
            }
        } catch (Exception e) {
           ErrorInit(e);
        }
    }

    public CLVenRep(gnu.chu.anjelica.menu p, EntornoUsuario eu, Hashtable ht) {
        EU = eu;
        vl = p.getLayeredPane();
        eje = false;

        try {
            if (ht != null) {
                if (ht.get("zonaRep") != null) {
                    ARG_ZONAREP = ht.get("zonaRep").toString();
                }
                if (ht.get("modif") != null)
                    ARG_MODIF = new Boolean(ht.get("modif").toString()).booleanValue();
            }
            setTitulo("Consuta Ventas Representantes");

            jbInit();
        } catch (Exception e) {
           ErrorInit(e);
        }
    }

    private void jbInit() throws Exception {
        statusBar = new StatusBar(this);

        iniciarFrame();

        this.setVersion("2010-06-12" + ARG_ZONAREP);

        initComponents();
        this.setSize(new Dimension(730, 535));
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);

        conecta();
    }

    @Override
    public void iniciarVentana() throws Exception {
        if (! ARG_MODIF)
            avl_prtariE.setEnabled(false);
        bdisc.iniciar(dtStat, this, vl, EU);
        Vector vc = new Vector();
        vc.add(pro_codiE);
        vc.add(pro_nombE);
        vc.add(avl_unidE);
        vc.add(avl_kilosE);
        vc.add(avl_precioE);
        vc.add(avl_prtariE);
        vc.add(avl_gananE);
        vc.add(avl_numlinE);
        jtLin.setCampos(vc);
        fecIniE.setText(Formatear.sumaDias(Formatear.getDateAct(), -15));
        fecFinE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
        Baceptar.addMenu("Consultar");
        Baceptar.addMenu("Sin Tarifa");
        Baceptar.addMenu("Imprimir");
        emp_codiE.iniciar(dtStat, this, vl, EU);
        emp_codiE.setAceptaNulo(false);

        rep_codiE.setFormato(Types.CHAR, "XX",2);
        
        if (! ARG_ZONAREP.equals(""))
        {
            rep_codiE.addDatos(ARG_ZONAREP, MantRepres.getNombRepr(ARG_ZONAREP,dtCon1));
            rep_codiE.setText(ARG_ZONAREP);
        }
        else
              MantRepres.llenaLinkBox(rep_codiE, dtCon1);
        dtAdd = new DatosTabla(new conexion(EU));

        activarEventos();
    }

    private void activarEventos() {

        Baceptar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
              if (e.getActionCommand().startsWith("Impr"))
                  imprimir();
              else
                consultar(e.getActionCommand().startsWith("Sin"));
            }
        });
        jtCab.tableView.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() || !jtCab.isEnabled()) {
                    return;
                }
                guardaCambios();
                verLineas();
            }
        });
        emp_codiE.addCambioListener(new CambioListener() {

            public void cambio(CambioEvent event) {
                try {
                    pdconfig.llenaDiscr(dtCon1, rep_codiE, "CR", emp_codiE.getValorInt());
                } catch (SQLException k) {
                    Error("Error al cargar discriminadores", k);
                }
            }
        });
    }
    

    String getCondWhere() throws ParseException {
        return " and a.avc_fecalb between TO_DATE('" + fecIniE.getFecha("dd-MM-yyyy")
                + "','dd-MM-yyyy') and TO_DATE('" + fecFinE.getFecha("dd-MM-yyyy")
                + "','dd-MM-yyyy') "
                + " and cl.rep_codi = '" + rep_codiE.getText() + "'"
                + (opIncCobr.isSelected() ? " and avc_cobrad != 0" : "");
    }
     /**
      * Actualiza las tarifas
      * @throws SQLException
      */
    void actualTarifa() throws Exception
    {
        s = "select cl.tar_codi,a.avc_fecalb,l.pro_codi from v_albavec as a, v_albavel as l,clientes as cl "
                + " WHERE  l.emp_codi = " + emp_codiE.getValorInt()
                + getCondWhere()
                + " and  a.emp_codi = " + emp_codiE.getValorInt()
                + " and a.avc_ano = l.avc_ano  "
                + " and a.avc_serie = l.avc_serie "
                + " and a.avc_nume = l.avc_nume "
                + " and l.avl_profer = 0"
                + " and l.tar_preci = 0"
                + " and  cl.cli_codi = a.cli_codi"
                + " group by tar_codi,avc_fecalb, pro_codi";
        if (!dtCon1.select(s)) {
            return; // No hay albaranes sin valorar
        }
        int nLinAct = 0;
        double prTari;
        do {
            prTari = MantTarifa.getPrecTar(dtStat, dtCon1.getInt("pro_codi"),
                    dtCon1.getInt("tar_codi"), dtCon1.getFecha("avc_fecalb", "dd-MM-yyyy"));
            if (prTari != 0) {
                s = "UPDATE  v_albavel set tar_preci = " + Formatear.Redondea(prTari, 2)
                        + " where   emp_codi = " + emp_codiE.getValorInt()
                        + " and pro_codi = " + dtCon1.getInt("pro_codi")
                        + " and avl_profer = 0"
                        + " and tar_preci = 0"+
                        " and exists (select * from v_albavec as a,clientes as cl  where " +
                        " avc_fecalb = to_date('" + dtCon1.getFecha("avc_fecalb") + "','dd-MM-yyyy')"
                        + " and  a.emp_codi = " + emp_codiE.getValorInt()
                        + " and a.avc_ano = v_albavel.avc_ano  "
                        + " and a.avc_serie = v_albavel.avc_serie "
                        + " and a.avc_nume = v_albavel.avc_nume"
                        + " and  cl.cli_codi = a.cli_codi"
                        +" and tar_codi = '" + dtCon1.getInt("tar_codi") + "')";
                dtAdd.executeUpdate(s);
                nLinAct++;
            }
        } while (dtCon1.next());
        if (nLinAct > 0)
         {
            dtAdd.commit();
             msgBox("Actualizadas tarifas en " + nLinAct + " Elementos");
        }
           
        
    }
    String getStrSql(boolean sinTarifa,String condWhere)
    {
        s = "SELECT a.avc_ano, a.avc_serie, a.avc_nume,a.avc_fecalb,a.cli_codi, "
                    + " cl.cli_nomb,avc_impalb,avc_impcob,cl.tar_codi  "
                    + "  FROM v_albavec as a,clientes as cl "
                    + " WHERE cl.cli_codi = a.cli_codi "+
                    bdisc.getCondWhere("cl")+
                    condWhere;
            if (sinTarifa) {
                s += " and exists (select *  FROM v_albavel as l "
                        + " WHERE l.emp_codi = " + emp_codiE.getValorInt()
                        + " and a.avc_ano = l.avc_ano  "
                        + " and a.avc_serie = l.avc_serie "
                        + " and a.avc_nume = l.avc_nume "
                        + " and l.avl_profer = 0"
                        + " and l.tar_preci = 0)";
            }
            s += " ORDER BY  a.avc_ano, a.avc_serie, a.avc_nume ";
            return s;
    }
    void consultar(boolean sinTarifa) {
        guardaCambios();
        PreparedStatement ps;
        ResultSet rs;
        try {
            if (!checkCond()) 
                return;
            if (ARG_MODIF && sinTarifa)
              actualTarifa();

            jtCab.setEnabled(false);
            jtCab.removeAllDatos();
            String condWhere = getCondWhere();
            s=getStrSql(sinTarifa,condWhere);
          
            if (!dtCon1.select(s)) {
                msgBox("No encontrados albaranes con esos criterios");
                fecIniE.requestFocus();
                return;
            }
            s = " SELECT sum(avl_canti*(avl_prbase-tar_preci)) as gananc,sum(avl_canti) as avl_canti  "
                    + "  FROM v_albavel  "
                    + " WHERE emp_codi = " + emp_codiE.getValorInt()
                    + " and avc_ano = ?"
                    + " and avc_serie = ? "
                    + " and avc_nume = ?"
                    + " and avl_profer = 0"
                    + " union all "
                    + "  SELECT sum(avl_canti*(avl_prbase-avl_profer)) as gananc,sum(avl_canti) as avl_canti   "
                    + "  FROM v_albavel  "
                    + " WHERE emp_codi = " + emp_codiE.getValorInt()
                    + " and avc_ano = ?"
                    + " and avc_serie = ? "
                    + " and avc_nume = ?"
                    + " and avl_profer != 0";

            ps = dtStat.getPreparedStatement(s);
            double TimpAlb = 0, TkilAlb = 0, kilAlb, TimpGan = 0, ganAlb;
            int TnumAlb = 0;
            do {
                Vector v = new Vector();
                v.add(dtCon1.getString("avc_ano"));
                v.add(dtCon1.getString("avc_serie"));
                v.add(dtCon1.getString("avc_nume"));
                v.add(dtCon1.getFecha("avc_fecalb", "dd-MM-yy"));
                v.add(dtCon1.getString("cli_codi"));
                v.add(dtCon1.getString("cli_nomb"));
                v.add(dtCon1.getString("avc_impalb"));
                v.add(dtCon1.getString("avc_impcob"));
                
                ps.setInt(1, dtCon1.getInt("avc_ano"));
                ps.setString(2, dtCon1.getString("avc_serie"));
                ps.setInt(3, dtCon1.getInt("avc_nume"));
                ps.setInt(4, dtCon1.getInt("avc_ano"));
                ps.setString(5, dtCon1.getString("avc_serie"));
                ps.setInt(6, dtCon1.getInt("avc_nume"));
                TnumAlb++;
                TimpAlb += dtCon1.getDouble("avc_impalb");

                ganAlb = 0;
                kilAlb = 0;
                rs = ps.executeQuery();
                rs.next();
                if (rs.getObject("gananc") != null) {
                    ganAlb = rs.getDouble("gananc");
                    kilAlb += rs.getDouble("avl_canti");
                }
                rs.next();
                if (rs.getObject("gananc") != null) {
                    ganAlb += rs.getDouble("gananc");
                    kilAlb += rs.getDouble("avl_canti");
                }
                TkilAlb += kilAlb;
                TimpGan += ganAlb;
                v.add("" + kilAlb);
                v.add("" + ganAlb); // Imp.Ganancia
                v.add(dtCon1.getString("tar_codi"));
                rs.next();
                jtCab.addLinea(v);
            } while (dtCon1.next());
            impAlbE.setValorDec(TimpAlb);
            impGanE.setValorDec(TimpGan);
            kilAlbE.setValorDec(TkilAlb);
            numAlbE.setValorInt(TnumAlb);

//        jtCab.requestFocusInicio();
            jtCab.setEnabled(true);
            jtCab.requestFocusInicio();
            verLineas();
        } catch (Exception k) {
            Error("Error al comprobar condiciones al buscar Albaranes", k);
            return;
        }
    }
    public void guardaCambios()
    {
       jtLinCambiaLinea(jtLin.getSelectedRow(),0);
    }
    public int jtLinCambiaLinea(int row,int col)
    {
        if (! avl_prtariE.isEnabled() || jtLin.isVacio())
            return -1;
        double avlPrven=avl_prtariE.getValorDec();
       
        s = "UPDATE  V_albavel set avl_profer = " +avlPrven +
             " where " +condAlb +
             " and avl_numlin in ("+jtLin.getValString(row,7)+")";
        try {
//            System.out.println("s: "+s);
            dtAdd.executeUpdate(s);
            dtAdd.commit();
        } catch (SQLException k)
        {
            Error("Error al actualizar linea",k);
        }
        mensajeErr("Precio Linea Actualizada",false);
        return -1;
    }
    private void getCondAlb(int row)
    {
         condAlb= "  emp_codi = " + emp_codiE.getValorInt()
                    + " and avc_ano = " + jtCab.getValorInt(row,0)
                    + " and avc_nume = " + jtCab.getValorInt(row,2)
                    + " and avc_serie = '" + jtCab.getValString(row,1) + "'";
    }
    void verLineas() {
        try {
            s = pdalbara.getSqlLinAgr( jtCab.getValorInt(0), emp_codiE.getValorInt(),
                    jtCab.getValString(1), jtCab.getValorInt(2), true);
            if (ARG_MODIF)
                jtLin.setEnabled(false);
            jtLin.removeAllDatos();
            if (!dtCon1.select(s)) {
                mensajeErr("No encontradas lineas de albaran");
                return;
            }
            getCondAlb(jtCab.getSelectedRow());
            String linAlb;
            do
            {
                linAlb = MantPrAlb.getNumLinAlb(condAlb,
                     dtCon1.getDouble("avl_canti"),
                     dtCon1.getInt("pro_codi"),dtCon1.getDouble("avl_prven"),
                     dtCon1.getDouble("avl_prepvp"),dtCon1.getString("avl_coment",false),
                     dtCon1.getDouble("avl_profer"),dtStat);
                Vector v = new Vector();
                v.addElement(dtCon1.getString("pro_codi"));
                v.addElement(dtCon1.getString("pro_nomb"));
                v.addElement(dtCon1.getString("avl_unid"));
                v.addElement(dtCon1.getString("avl_canti"));
                v.addElement(dtCon1.getString("avl_prven"));
                double prTari = 0;
                if (dtCon1.getDouble("avl_profer", true) == 0) {
                    prTari = dtCon1.getDouble("tar_preci", true);
                }
                 else {
                    prTari = dtCon1.getDouble("avl_profer", true);
                }
                v.addElement("" + prTari);
                v.addElement("" + (dtCon1.getDouble("avl_prven") - prTari));
                v.addElement(linAlb);
                jtLin.addLinea(v);
            } while (dtCon1.next());
            if (ARG_MODIF)
                jtLin.setEnabled(true);
            jtLin.requestFocusInicio();
        } catch (Exception k) {
            Error("Error al ver Lineas de albaran", k);
            return;
        }
    }
    @Override
    public void matar()
    {

        guardaCambios();
        super.matar();
    }
    boolean checkCond() throws Exception {
        if (!emp_codiE.controla()) {
            mensajeErr(emp_codiE.getMsgError());
            return false;
        }
        if (fecIniE.getError()) {
            return false;
        }
        if (fecFinE.getError()) {
            return false;
        }
        if (fecIniE.isNull()) {
            mensajeErr("Introduzca Fecha Inicial");
            fecIniE.requestFocus();
            return false;
        }
        if (fecFinE.isNull()) {
            mensajeErr("Introduzca Fecha Final");
            fecFinE.requestFocus();
            return false;
        }
        if (Formatear.comparaFechas(fecIniE.getDate(), fecFinE.getDate()) > 0) {
            mensajeErr("Fecha final no puede ser inferios a Inicial");
            fecIniE.requestFocus();
            return false;
        }
        if (rep_codiE.isNull()) {
            mensajeErr("Introduzca un Representante");
            rep_codiE.requestFocus();
            return false;
        }
        if (! rep_codiE.controla(true))
        {
            mensajeErr("Representante NO VALIDO");
            return false;
        }
        return true;
    }
    
    private void imprimir() {
        try {
            if (! checkCond())
                return;
            mensaje("Espere, generando Listado");
            s=getStrSql(false,getCondWhere());
            dtCon1.setStrSelect(s);
            ResultSet rs=ct.createStatement().executeQuery(dtCon1.getStrSelect());
            JasperReport jr = gnu.chu.print.util.getJasperReport(EU,  "realbvrep");
            java.util.HashMap mp = new java.util.HashMap();
            mp.put(JRParameter.REPORT_CONNECTION, ct);
            mp.put("fecIni",fecIniE.getDate());
            mp.put("fecFin",fecFinE.getDate());
            mp.put("fecIniD",fecIniE.getFecha("yyyy-MM-dd"));
            mp.put("fecFinD",fecFinE.getFecha("yyyy-MM-dd"));
            mp.put("empCodiE",new Integer(emp_codiE.getText()));
            mp.put("repCodiE",rep_codiE.getText());
            mp.put("repNombE",rep_codiE.getTextCombo());
            
            mp.put("SUBREPORT_FILE_NAME", EU.pathReport + "/lialbrep.jasper");

            JasperPrint jp = JasperFillManager.fillReport(jr, mp, new JRResultSetDataSource(rs));
            mensaje("");
            if (!gnu.chu.print.util.printJasper(jp, EU))
                 return;
            mensajeErr("Listado Generado !!");

        } catch (Exception k) {
            Error("Error al generar listado ", k);
        }
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

        pro_codiE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#####9");
        pro_nombE = new gnu.chu.controles.CTextField();
        avl_unidE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---9");
        avl_kilosE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---9.99");
        avl_precioE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--9.99");
        avl_prtariE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--9.99");
        avl_gananE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--9.99");
        avl_numlinE = new gnu.chu.controles.CTextField();
        Pprinc = new gnu.chu.controles.CPanel();
        Pcondic = new gnu.chu.controles.CPanel();
        cLabel5 = new gnu.chu.controles.CLabel();
        fecIniE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel6 = new gnu.chu.controles.CLabel();
        fecFinE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel3 = new gnu.chu.controles.CLabel();
        emp_codiE = new gnu.chu.camposdb.empPanel();
        cLabel1 = new gnu.chu.controles.CLabel();
        rep_codiE = new gnu.chu.controles.CLinkBox();
        opIncCobr = new gnu.chu.controles.CCheckBox();
        Baceptar = new gnu.chu.controles.CButtonMenu();
        bdisc = new gnu.chu.camposdb.DiscButton();
        jtCab = new gnu.chu.controles.Cgrid(11);
        Vector v=new Vector();
        v.addElement("Ejer"); // 0
        v.addElement("Ser"); // 1
        v.addElement("Num.Alb"); // 2
        v.addElement("Fec.Alb"); // 3
        v.addElement("Cliente"); // 4
        v.addElement("Nomb.Cliente"); // 5
        v.addElement("Imp.Alb"); // 6
        v.addElement("Imp.Cob"); // 7
        v.addElement("Kilos"); // 8
        v.addElement("Imp.Ganan"); // 9
        v.addElement("Tar"); // 10
        jtCab.setCabecera(v);
        jtCab.setAnchoColumna(new int[]{
            40,30,50,80, 60,180,80,80,70,80,30});
    jtCab.setFormatoColumna(3,"dd-MM-yy");
    jtCab.setFormatoColumna(6,"----,--9.99");
    jtCab.setFormatoColumna(7,"----,--9.99");
    jtCab.setFormatoColumna(8,"--,--9.99");
    jtCab.setFormatoColumna(9,"----,--9.99");
    jtCab.setAlinearColumna(new int[]{2,1,2,1,2,0,2,2,2,2,2});
    jtCab.setAjustarGrid(true);
    jtLin = new gnu.chu.controles.CGridEditable(8){
        public int cambiaLinea(int row, int col)
        {
            return jtLinCambiaLinea(row,col);
        }
    }
    ;
    Vector v1=new Vector();
    v1.add("Prod"); //0
    v1.add("Nombre"); // 1
    v1.add("Unid"); // 2
    v1.add("Kilos"); // 3
    v1.add("Prec"); // 4
    v1.add("Tarifa"); // 5
    v1.add("Gananc"); // 6
    v1.add("NL"); // 7
    jtLin.setCabecera(v1);
    jtLin.setAnchoColumna(new int[]{60,180,50,70,60,60,60,20});
    jtLin.setAlinearColumna(new int[]{2,0,2,2,2,2,2,0});
    jtLin.setFormatoColumna(2,"---9");
    jtLin.setFormatoColumna(3,"---9.99");
    jtLin.setFormatoColumna(4,"--9.99");
    jtLin.setFormatoColumna(5,"--9.99");
    jtLin.setFormatoColumna(6,"--9.99");

    jtLin.setCanDeleteLinea(false);
    jtLin.setCanInsertLinea(false);
    jtLin.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

    jtLin.setPreferredSize(new java.awt.Dimension(80, 80));

    // Code of sub-components - not shown here

    // Layout setup code - not shown here

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
    Pprinc.add(jtLin, gridBagConstraints);
    Presumen = new gnu.chu.controles.CPanel();
    cLabel2 = new gnu.chu.controles.CLabel();
    numAlbE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
    cLabel4 = new gnu.chu.controles.CLabel();
    impAlbE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--,---,--9.9");
    cLabel7 = new gnu.chu.controles.CLabel();
    kilAlbE = new gnu.chu.controles.CTextField(Types.DECIMAL,"-,---,--9.9");
    cLabel8 = new gnu.chu.controles.CLabel();
    impGanE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--,---,--9.9");

    pro_codiE.setEnabled(false);

    pro_nombE.setEnabled(false);

    avl_unidE.setEnabled(false);

    avl_kilosE.setEnabled(false);

    avl_precioE.setEnabled(false);

    avl_gananE.setEnabled(false);

    avl_numlinE.setEnabled(false);

    Pprinc.setLayout(new java.awt.GridBagLayout());

    Pcondic.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
    Pcondic.setMaximumSize(new java.awt.Dimension(578, 60));
    Pcondic.setMinimumSize(new java.awt.Dimension(578, 60));
    Pcondic.setPreferredSize(new java.awt.Dimension(578, 60));

    cLabel5.setText("De Fecha");

    cLabel6.setText("A Fecha");

    cLabel3.setText("Empresa");

    cLabel1.setText("Repres");

    rep_codiE.setAncTexto(30);
    rep_codiE.setAncTexto(30);

    opIncCobr.setText("Solo cobrados");

    Baceptar.setText("Ejecutar");

    org.jdesktop.layout.GroupLayout PcondicLayout = new org.jdesktop.layout.GroupLayout(Pcondic);
    Pcondic.setLayout(PcondicLayout);
    PcondicLayout.setHorizontalGroup(
        PcondicLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(PcondicLayout.createSequentialGroup()
            .add(21, 21, 21)
            .add(PcondicLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(PcondicLayout.createSequentialGroup()
                    .add(cLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(rep_codiE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 237, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(3, 3, 3)
                    .add(opIncCobr, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(7, 7, 7)
                    .add(Baceptar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(PcondicLayout.createSequentialGroup()
                    .add(cLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(11, 11, 11)
                    .add(emp_codiE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(20, 20, 20)
                    .add(cLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(1, 1, 1)
                    .add(fecIniE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 76, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(26, 26, 26)
                    .add(cLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(4, 4, 4)
                    .add(fecFinE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 75, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(42, 42, 42)
                    .add(bdisc, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
            .add(76, 76, 76))
    );
    PcondicLayout.setVerticalGroup(
        PcondicLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(PcondicLayout.createSequentialGroup()
            .add(6, 6, 6)
            .add(PcondicLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(cLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(emp_codiE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(cLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(fecIniE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(cLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(fecFinE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(bdisc, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(PcondicLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(PcondicLayout.createSequentialGroup()
                    .add(3, 3, 3)
                    .add(cLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(PcondicLayout.createSequentialGroup()
                    .add(3, 3, 3)
                    .add(rep_codiE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(PcondicLayout.createSequentialGroup()
                    .add(3, 3, 3)
                    .add(opIncCobr, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(Baceptar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(12, 12, 12))
    );

    PcondicLayout.linkSize(new java.awt.Component[] {bdisc, cLabel3, cLabel5, cLabel6, emp_codiE, fecFinE, fecIniE}, org.jdesktop.layout.GroupLayout.VERTICAL);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.weightx = 1.0;
    Pprinc.add(Pcondic, gridBagConstraints);

    jtCab.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jtCab.setMaximumSize(new java.awt.Dimension(220, 130));
    jtCab.setMinimumSize(new java.awt.Dimension(220, 130));

    org.jdesktop.layout.GroupLayout jtCabLayout = new org.jdesktop.layout.GroupLayout(jtCab);
    jtCab.setLayout(jtCabLayout);
    jtCabLayout.setHorizontalGroup(
        jtCabLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(0, 628, Short.MAX_VALUE)
    );
    jtCabLayout.setVerticalGroup(
        jtCabLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(0, 152, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 3.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
    Pprinc.add(jtCab, gridBagConstraints);

    jtLin.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jtLin.setMaximumSize(new java.awt.Dimension(180, 100));
    jtLin.setMinimumSize(new java.awt.Dimension(180, 100));

    org.jdesktop.layout.GroupLayout jtLinLayout = new org.jdesktop.layout.GroupLayout(jtLin);
    jtLin.setLayout(jtLinLayout);
    jtLinLayout.setHorizontalGroup(
        jtLinLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(0, 628, Short.MAX_VALUE)
    );
    jtLinLayout.setVerticalGroup(
        jtLinLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(0, 101, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 2.0;
    Pprinc.add(jtLin, gridBagConstraints);

    Presumen.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    Presumen.setMaximumSize(new java.awt.Dimension(628, 30));
    Presumen.setMinimumSize(new java.awt.Dimension(628, 30));
    Presumen.setOpaque(false);
    Presumen.setPreferredSize(new java.awt.Dimension(628, 30));

    cLabel2.setText("Num. Albaranes");
    cLabel2.setPreferredSize(new java.awt.Dimension(57, 17));

    numAlbE.setEnabled(false);

    cLabel4.setText("Importe");
    cLabel4.setPreferredSize(new java.awt.Dimension(57, 17));

    impAlbE.setEnabled(false);

    cLabel7.setText("Kilos");
    cLabel7.setPreferredSize(new java.awt.Dimension(57, 17));

    kilAlbE.setEnabled(false);

    cLabel8.setText("Ganancia");
    cLabel8.setPreferredSize(new java.awt.Dimension(57, 17));

    impGanE.setEnabled(false);

    org.jdesktop.layout.GroupLayout PresumenLayout = new org.jdesktop.layout.GroupLayout(Presumen);
    Presumen.setLayout(PresumenLayout);
    PresumenLayout.setHorizontalGroup(
        PresumenLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(PresumenLayout.createSequentialGroup()
            .addContainerGap()
            .add(cLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 95, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(numAlbE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 47, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(cLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(impAlbE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 86, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(cLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 32, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(kilAlbE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 86, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(18, 18, 18)
            .add(cLabel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(impGanE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 86, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(28, Short.MAX_VALUE))
    );
    PresumenLayout.setVerticalGroup(
        PresumenLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(PresumenLayout.createSequentialGroup()
            .add(PresumenLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(cLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(numAlbE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(cLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(impAlbE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(cLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(kilAlbE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(cLabel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(impGanE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
    Pprinc.add(Presumen, gridBagConstraints);

    getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

    pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButtonMenu Baceptar;
    private gnu.chu.controles.CPanel Pcondic;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CPanel Presumen;
    private gnu.chu.controles.CTextField avl_gananE;
    private gnu.chu.controles.CTextField avl_kilosE;
    private gnu.chu.controles.CTextField avl_numlinE;
    private gnu.chu.controles.CTextField avl_precioE;
    private gnu.chu.controles.CTextField avl_prtariE;
    private gnu.chu.controles.CTextField avl_unidE;
    private gnu.chu.camposdb.DiscButton bdisc;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.camposdb.empPanel emp_codiE;
    private gnu.chu.controles.CTextField fecFinE;
    private gnu.chu.controles.CTextField fecIniE;
    private gnu.chu.controles.CTextField impAlbE;
    private gnu.chu.controles.CTextField impGanE;
    private gnu.chu.controles.Cgrid jtCab;
    private gnu.chu.controles.CGridEditable jtLin;
    private gnu.chu.controles.CTextField kilAlbE;
    private gnu.chu.controles.CTextField numAlbE;
    private gnu.chu.controles.CCheckBox opIncCobr;
    private gnu.chu.controles.CTextField pro_codiE;
    private gnu.chu.controles.CTextField pro_nombE;
    private gnu.chu.controles.CLinkBox rep_codiE;
    // End of variables declaration//GEN-END:variables
}
