package gnu.chu.anjelica.compras;


import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import java.sql.*;
import gnu.chu.Menu.*;
import java.awt.*;
import java.util.*;
import javax.swing.BorderFactory;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.*;


/**
 *
 * <p>Titulo: clintalco</p>
 * <p>Descripció: Cons/Listado Integridad Albaranes de Compras</p>
 * <p>Copyright: Copyright (c) 2004-2014</p>
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
 * <p>Empresa: MISL</p>
 * @author chuchiP
 * @version 1.1
 */

public class clintalco extends ventana implements JRDataSource {

    String s;
    boolean swConsInt = true; // Integridad
    ResultSet rs;
    CPanel Pprinc = new CPanel();
    CTextField feciniE = new CTextField(Types.DATE, "dd-MM-yyyy");
    CLabel cLabel1 = new CLabel();
    CButtonMenu Baceptar = new CButtonMenu(Iconos.getImageIcon("check"));
    CLabel cLabel2 = new CLabel();
    CPanel PintrDatos = new CPanel();
    CTextField fecfinE = new CTextField(Types.DATE, "dd-MM-yyyy");
    Cgrid jt = new Cgrid(10);
    GridBagLayout gridBagLayout1 = new GridBagLayout();

    public clintalco(EntornoUsuario eu, Principal p) {
        EU = eu;
        vl = p.panel1;
        jf = p;
        eje = true;

        setTitulo("Cons/List Integridad Albaranes Compras");

        try {
            if (jf.gestor.apuntar(this)) {
                jbInit();
            } else {
                setErrorInit(true);
            }
        } catch (Exception e) {
            ErrorInit(e);
            setErrorInit(true);
        }
    }

    public clintalco(gnu.chu.anjelica.menu p, EntornoUsuario eu) {

        EU = eu;
        vl = p.getLayeredPane();
        setTitulo("Cons/List Integridad Albaranes Compras");
        eje = false;

        try {
            jbInit();
        } catch (Exception e) {
            ErrorInit(e);
            setErrorInit(true);
        }
    }

    private void jbInit() throws Exception {
        iniciarFrame();
        this.setSize(new Dimension(700, 500));
        this.setVersion("2014-07-29");

        statusBar = new StatusBar(this);
        conecta();
        Pprinc.setLayout(gridBagLayout1);
        feciniE.setBounds(new Rectangle(73, 5, 76, 18));
        cLabel1.setText("De Fecha");
        cLabel1.setBounds(new Rectangle(14, 5, 55, 18));
        Baceptar.setBounds(new Rectangle(125, 26, 85, 24));

        Baceptar.setMargin(new Insets(0, 0, 0, 0));
        cLabel2.setText("A");
        cLabel2.setBounds(new Rectangle(193, 6, 19, 17));
        PintrDatos.setBorder(BorderFactory.createRaisedBevelBorder());
        PintrDatos.setMaximumSize(new Dimension(308, 55));
        PintrDatos.setMinimumSize(new Dimension(308, 55));
        PintrDatos.setPreferredSize(new Dimension(308, 55));
        PintrDatos.setDefButton(Baceptar.getBotonAccion());
        PintrDatos.setLayout(null);
        fecfinE.setBounds(new Rectangle(216, 5, 77, 18));
        ArrayList v = new ArrayList();
        Pprinc.setMaximumSize(new Dimension(32767, 32767));

        v.add("Albaran"); // 0
        v.add("Fec.Alb"); // 1
        v.add("Proveedor"); // 2
        v.add("Producto"); // 3
        v.add("Descripcion"); // 4
        v.add("Un.Linea"); // 5
        v.add("Kg.Linea"); // 6
        v.add("Un.Desgl."); // 7
        v.add("Kg.Desgl"); // 8
        v.add("Coment"); // 9
        jt.setCabecera(v);
        jt.setAnchoColumna(new int[]{60, 90, 120, 70, 120, 60, 70, 60, 70, 100});
        jt.setAlinearColumna(new int[]{0, 1, 0, 2, 0, 2, 2, 2, 2, 0});
        jt.setFormatoColumna(5, "---9");
        jt.setFormatoColumna(7, "---9");
        jt.setFormatoColumna(6, "---,---9.99");
        jt.setFormatoColumna(8, "---,---9.99");
        jt.setAjustarGrid(true);
        jt.setConfigurar("gnu.chu.anjelica.compras.clintalco", EU, dtStat);
        jt.setNumRegCargar(200);
        Baceptar.addMenu("Integridad");
        Baceptar.addMenu("Sin Precio");
        Baceptar.addMenu("Ind.Perdido");
        Baceptar.addMenu("Imprimir");
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        this.getContentPane().add(Pprinc, BorderLayout.CENTER);
        Pprinc.add(PintrDatos, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 0, 0));
        PintrDatos.add(feciniE, null);
        PintrDatos.add(cLabel1, null);
        PintrDatos.add(cLabel2, null);
        PintrDatos.add(fecfinE, null);
        PintrDatos.add(Baceptar, null);
        // PintrDatos.add(BListar, null);
        Pprinc.add(jt, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 3, 2, 1), -89, -137));
    }

    @Override
    public void iniciarVentana() throws Exception {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(new java.util.Date(System.currentTimeMillis()));
        gc.add(GregorianCalendar.MONTH, -1);
        feciniE.setDate(gc.getTime());
        fecfinE.setText(Formatear.getFechaAct("dd-MM-yyyy"));

        Pprinc.setDefButton(Baceptar.getBotonAccion());
        activarEventos();
        feciniE.requestFocus();
    }

    void activarEventos() {
        Baceptar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().startsWith("Ind"))
                {
                    buscaIndPerd();
                    return;
                }
                if (e.getActionCommand().startsWith("Impr")) {
                    BListar_actionPerformed();
                } else {
                    swConsInt = e.getActionCommand().startsWith("Integ") || e.getActionCommand().startsWith("CB");
                    buscaDatos();
                }
            }
        });
//    BListar.addActionListener(new ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        BListar_actionPerformed();
//      }
//    });
    }

    void BListar_actionPerformed() {
        new miThread("") {

            @Override
            public void run() {
                imprimir();
            }
        };
    }

    void imprimir() {
        this.setEnabled(false);
        try {
            mensaje("Esperate, tio ... Estoy Generando el Listado");
            s = getSelect();
            dtCon1.setStrSelect(s);
            rs = ct.createStatement().executeQuery(dtCon1.getStrSelect());
            JasperReport jr;
            jr = gnu.chu.print.util.getJasperReport(EU, "clintalco");

            java.util.HashMap mp = new java.util.HashMap();
            mp.put("fechas", "DEL : " + feciniE.getText() + " AL " + fecfinE.getText());
            JasperPrint jp = JasperFillManager.fillReport(jr, mp, this);
            gnu.chu.print.util.printJasper(jp, EU);
            mensaje("");
            mensajeErr("Listado ... Generado");
            this.setEnabled(true);
        } catch (Exception k) {
            Error("Error al Generar el Listado", k);
            return;
        }
    }

    String getSelect() {
        return "SELECT c.emp_codi,c.acc_ano,c.acc_serie,c.acc_nume,c.acc_fecrec,"
                + "l.*,p.prv_nomb,a.pro_nomb,l.acl_prcom "
                + "  FROM v_albacoc as c,v_albacol as l,v_proveedo as p,v_articulo as a "
                + " where c.emp_codi = " + EU.em_cod
                + " and a.pro_codi = l.pro_codi "
                + " AND c.acc_fecrec >= TO_DATE('" + feciniE.getText() + "','dd-MM-yyyy')"
                + " AND c.acc_fecrec <= TO_DATE('" + fecfinE.getText() + "','dd-MM-yyyy')"
                + " and l.emp_codi = " + EU.em_cod
                + " and C.acc_ano = L.ACC_ANO "
                + " and C.acc_nume =  L.acc_nume "
                + " and c.acc_serie = l.acc_serie "
                + " and c.prv_codi = p.prv_codi "
                + " and pro_tiplot = 'V' " + // Solo trata productos vendibles.
                " ORDER BY c.acc_ano,c.acc_serie,c.acc_nume";
    }

    boolean checkCampos() {
        if (feciniE.isNull()) {
            mensajeErr("Introduzca Fecha Inicio");
            feciniE.requestFocus();
            return false;
        }

        if (fecfinE.isNull()) {
            mensajeErr("Introduzca Fecha Final");
            fecfinE.requestFocus();
            return false;
        }
        return true;
    }
    /**
     * Busca individuos que han salido y que no tienen trazabilidad.
     */
    void buscaIndPerd()
    {
         try {
            if (!checkCampos()) {
                return;
            }
            s = getSelect();
            jt.removeAllDatos();
            if (!dtCon1.select(s)) {
                mensajeErr("NO encontrados ALBARANES ENTRE ESTAS FECHAS");
                return;
            }
            mensaje("Espere, por favor ... Buscando datos");
            new miThread("") {

                @Override
                public void run() {
                    buscaIndPerd0();
                }
            };
        } catch (SQLException k) {
            Error("Error al Buscar datos", k);
        }
    }
    void buscaIndPerd0() 
    {
        try
        {            
            this.setEnabled(false);
            jt.panelG.setVisible(false);
            s="select m.*,a.pro_nomb,prv_nomb,acc_fecrec from mvtosalm as m, v_albacoc as c , v_articulo as a,v_proveedo as p " +
                " where mvt_tipo='S' and "+
                " m.mvt_empcod=c.emp_codi "+
                " and m.pro_ejelot = c.acc_ano " +
                " and m.pro_serlot = c.acc_serie " +
                " and m.pro_numlot = c.acc_nume " +
                " and m.pro_codi = a. pro_codi "+
                " and c.prv_codi = p.prv_codi "+
                " and c.acc_fecrec between TO_DATE('" + feciniE.getText() + "','dd-MM-yyyy')"+
                         " and  TO_DATE('" + fecfinE.getText() + "','dd-MM-yyyy')"+
                " and not exists (select * from mvtosalm as m1 where  "+
                " m1.mvt_tipo='E'  "+
                " and m.mvt_empcod = m1.mvt_empcod  "+
                " and m.pro_ejelot = m1.pro_ejelot "+
                " and m.pro_serlot = m1.pro_serlot "+
                " and m.pro_numlot = m1.pro_numlot "+
                " and m.pro_indlot= m1.pro_indlot "+
                " ) order by pro_numlot,pro_indlot ";
            if (! dtCon1.select(s))
            {
               msgBox("No encontrados salidas de individuos sin entradas");
               this.setEnabled(true);
               return;
            }
            jt.panelG.setVisible(false);
            do
            {
               ArrayList v = new ArrayList();
               v.add(dtCon1.getString("pro_numlot"));
               v.add(dtCon1.getFecha("acc_fecrec", "dd-MM-yyyy"));
               v.add(dtCon1.getString("prv_nomb"));
               v.add(dtCon1.getString("pro_codi"));
               v.add(dtCon1.getString("pro_nomb"));
               v.add(dtCon1.getString("mvt_unid", true)   );
               v.add(dtCon1.getString("mvt_canti", true));
               v.add("0");
               v.add("0");
               v.add("Tipo "+dtCon1.getString("mvt_tipo")+" Doc:"+
                   dtCon1.getString("mvt_empcod")+"-"+
                   dtCon1.getString("mvt_ejedoc")+
                   dtCon1.getString("mvt_serdoc")+
                   dtCon1.getString("mvt_numdoc"));
               jt.addLinea(v); 
            } while (dtCon1.next());
        } catch (SQLException ex)
        {
            Error("Error al buscar Salidas sin entradas",ex);
        }
        this.setEnabled(true);
        jt.panelG.setVisible(true);
        mensajeErr("Salidas sin entradas .. encontradas");
    }
    void buscaDatos() {
        try {
            if (!checkCampos()) {
                return;
            }
            s = getSelect();
            jt.removeAllDatos();
            if (!dtCon1.select(s)) {
                mensajeErr("NO encontrados ALBARANES ENTRE ESTAS FECHAS");
                return;
            }
            mensaje("Espere, por favor ... Buscando datos");
            new miThread("") {

                @Override
                public void run() {
                    llenaGrid();
                }
            };
        } catch (SQLException k) {
            Error("Error al Buscar datos", k);
        }

    }

    void llenaGrid() {
        try {
            this.setEnabled(false);
            jt.panelG.setVisible(false);
            do {
                if (swConsInt)
                {
                    s = "SELECT count(*) as cuantos, sum(acp_canti) as acp_canti,"
                            + " avg(pro_codi) as pro_codi from v_albcompar "
                            + " where emp_codi = " + dtCon1.getInt("emp_codi")
                            + " and acc_ano =" + dtCon1.getInt("acc_ano")
                            + " and acc_serie = '" + dtCon1.getString("acc_serie") + "'"
                            + " and acc_nume = " + dtCon1.getInt("acc_nume")
                            + " and acl_nulin = " + dtCon1.getInt("acl_nulin");
                    dtStat.select(s);

                    if (dtStat.getInt("cuantos", true) != dtCon1.getInt("acl_numcaj", true)
                            || !Formatear.esIgual(dtStat.getDouble("acp_canti", true),
                            dtCon1.getDouble("acl_canti", true), 0.1)
                            || (dtCon1.getInt("pro_codi", true) != dtStat.getInt("pro_codi", true)
                             && dtStat.getInt("cuantos")>0))
                    {
                        ArrayList v = new ArrayList();
                        v.add(dtCon1.getString("acc_nume"));
                        v.add(dtCon1.getFecha("acc_fecrec", "dd-MM-yyyy"));
                        v.add(dtCon1.getString("prv_nomb"));
                        v.add(dtCon1.getString("pro_codi"));
                        v.add(dtCon1.getString("pro_nomb"));
                        v.add(dtCon1.getString("acl_numcaj", true));
                        v.add(dtCon1.getString("acl_canti", true));
                        v.add(dtStat.getString("cuantos", true));
                        v.add(dtStat.getString("acp_canti", true));
                        if (dtCon1.getInt("pro_codi", true) != dtStat.getInt("pro_codi", true)) {
                            v.add(dtCon1.getInt("pro_codi", true)
                                    + " != " + dtStat.getInt("pro_codi", true));
                        } //           v.addElement(" Desg: "+dtStat.getInt("pro_codi",true));
                        else {
                            v.add("");
                        }
                        jt.addLinea(v);
                    }

                    } else {
                        if (dtCon1.getDouble("acl_prcom") == 0) {
                            Vector v = new Vector();
                            v.addElement(dtCon1.getString("acc_nume"));
                            v.addElement(dtCon1.getFecha("acc_fecrec", "dd-MM-yyyy"));
                            v.addElement(dtCon1.getString("prv_nomb"));
                            v.addElement(dtCon1.getString("pro_codi"));
                            v.addElement(dtCon1.getString("pro_nomb"));
                            v.addElement(dtCon1.getString("acl_numcaj", true));
                            v.addElement(dtCon1.getString("acl_canti", true));
                            v.addElement("");
                            v.addElement("");
                            v.addElement("");
                            jt.addLinea(v);
                        }
                    }
            } while (dtCon1.next());
            jt.panelG.setVisible(true);
            this.setEnabled(true);
            jt.setEnabled(true);
            jt.requestFocusInicio();
            mensaje("");
            mensajeErr("Consulta ... Realizada");
        } catch (Exception k) {
            Error("Error al Buscar datos", k);
        }
    }

    @Override
    public boolean next() throws JRException {
        try {
            while (true) {
                if (!rs.next()) {
                    return false;
                }
                if (swConsInt) {
                    s = "SELECT count(*) as unDesg, sum(acp_canti) as acp_canti,avg(pro_codi) as pro_codi from v_albcompar "
                            + " where emp_codi = " + rs.getInt("emp_codi")
                            + " and acc_ano =" + rs.getInt("acc_ano")
                            + " and acc_serie = '" + rs.getString("acc_serie") + "'"
                            + " and acc_nume = " + rs.getInt("acc_nume")
                            + " and acl_nulin = " + rs.getInt("acl_nulin");
                    dtStat.select(s);
                    if (dtStat.getInt("unDesg", true) != rs.getInt("acl_numcaj")
                            || !Formatear.esIgual(dtStat.getDouble("acp_canti", true), rs.getDouble("acl_canti"), 0.1)
                            || rs.getInt("pro_codi") != dtStat.getInt("pro_codi", true)) {
                        return true;
                    }
                } else {
                    if (rs.getDouble("acl_prcom") != 0) {
                        return true;
                    }
                }
            }
        } catch (Exception k) {
            throw new JRException(k);
        }
    }

    public Object getFieldValue(JRField f) throws JRException {
        String campo = f.getName().toLowerCase();
        try {
            if (campo.equals("acc_fecrec")) {
                return rs.getDate(campo);
            }
            if (campo.equals("acc_nume") || campo.equals("pro_codi") || campo.equals("acl_numcaj")) {
                return new Integer(rs.getInt(campo));
            }
            if (campo.equals("prv_nomb") || campo.equals("pro_nomb")) {
                return rs.getString(campo);
            }
            if (campo.equals("acl_canti")) {
                return new Double(rs.getDouble(campo));
            }
            if (!swConsInt) {
                return null;
            }
            if (campo.equals("undesg")) {
                return new Integer(dtStat.getInt(campo, true));
            }
            if (campo.equals("acp_canti")) {
                return new Double(dtStat.getDouble(campo, true));
            }
            if (campo.equals("coment")) {
                if (rs.getInt("pro_codi") == dtStat.getInt("pro_codi", true)) {
                    return "";
                } else {
                    return "DIF. PRODUCTOS";
                }
            }
        } catch (Exception k) {
            throw new JRException(k);
        }

        throw new JRException("Campo: " + campo + " NO VALIDO");
    }
}
