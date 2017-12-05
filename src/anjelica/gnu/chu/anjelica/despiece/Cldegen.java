/**
 *
 * <p>Titulo: cldegen</p>
 * <p>Descripción: Consulta/Listado Despieces Generados</p>
 * <p>Copyright: Copyright (c) 2005-2017
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
 * Created on 27-mar-2010, 9:40:54
 */

package gnu.chu.anjelica.despiece;

import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import java.sql.*;
import gnu.chu.Menu.*;
import gnu.chu.anjelica.compras.pdtaripor;
import gnu.chu.anjelica.pad.MantTarifa;
import gnu.chu.anjelica.pad.pdtipotar;
import gnu.chu.interfaces.ejecutable;
import gnu.chu.sql.DatosTabla;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.sf.jasperreports.engine.*;

public class Cldegen extends ventana
{
    boolean swIgnEvento=false;
    int TARIFA_MAYOR;
    final private int JT_PROCOD=0;
    final private int JT_PRTARIFA=6;
    final private int JT_KGVENTA=7;
    final private int JT_PRVENTA=8;
    
    utildesp utdesp;
    DatosTabla dtDesp,dtAux;
    String condWhere;
    public Cldegen(EntornoUsuario eu, Principal p) {
        EU = eu;
        vl = p.panel1;
        jf = p;
        eje = true;

        setTitulo("Cons./List Despieces Generados");
         setAcronimo("cldege");
        try {
            if (jf.gestor.apuntar(this)) {       
                    jbInit();
            } else {
                setErrorInit(true);
            }
        } catch (Exception ex) {
            ErrorInit(ex);
            setErrorInit(true);
        }
    }

    public Cldegen(gnu.chu.anjelica.menu p, EntornoUsuario eu) {

        EU = eu;
        vl = p.getLayeredPane();
        setTitulo("Cons./List Despieces Generados ");
        eje = false;

        try {
            jbInit();
        } catch (Exception ex) {
            ErrorInit(ex);            
        }
    }

    private void jbInit() throws Exception {

        iniciarFrame(); 
       
        this.setVersion("2017-11-29");
        statusBar = new StatusBar(this);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        conecta();

        initComponents();
        this.setSize(new Dimension(640, 530));
    }

    @Override
    public void iniciarVentana() throws Exception {
       
        Pentra.setDefButton(Baceptar);
       
        fecfinE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
        feciniE.setText(Formatear.sumaDias(new Date(System.currentTimeMillis()), -7));
        TARIFA_MAYOR=pdtipotar.getTarifaBase(dtAdd, EU.em_cod);
//        emp_codiE.setValorInt(EU.em_cod);
        llenaCombos();
        utdesp =new utildesp();
        dtDesp=new DatosTabla(ct);
        dtAux=new DatosTabla(ct);
        dtCon1.select("SELECT sbe_codi,sbe_nomb FROM subempresa "+
            " where sbe_tipo ='A' order by sbe_codi");        
        sbe_codiE.addDatos(dtCon1);
        tid_codiE.iniciar(dtAux, this, vl, EU);
        activarEventos();
        feciniE.requestFocus();
    }
    private void llenaCombos() throws SQLException
    {
        String s="select fpr_codi,fpr_nomb from v_famipro "
                +" order by fpr_nomb";
        dtStat.select(s);
        fam_codiE.addDatos(dtStat);
        fam_codiE.addDatos("0","**TODAS**");
   
        fam_codiE.setValorInt(0);
        fam_codiE.setCeroIsNull(true);
        dtStat.first();
        fam_codentE.addDatos(dtStat);
        fam_codentE.addDatos("0","**TODAS**");
  
        fam_codentE.setValorInt(0);
        fam_codentE.setCeroIsNull(true);
        s="select agr_codi,agp_nomb from v_agupro ORDER BY agp_nomb";
        dtStat.select(s);
        grp_codiE.addDatos(dtStat);
        grp_codiE.addDatos("0","**TODAS**");
        grp_codiE.setValorInt(0);
        grp_codiE.setCeroIsNull(true);
        s = "SELECT alm_codi,alm_nomb FROM v_almacen "+
              " ORDER BY alm_nomb";
        dtStat.select(s);
        alm_codiE.addDatos(dtStat);
        alm_codiE.addDatos("0","**TODOS**");
        alm_codiE.setValorInt(0);
        alm_codiE.setCeroIsNull(true);
    }
    
    private void activarEventos() {
        Baceptar.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscaDatos();
            }
        });
        
        jt.addListSelectionListener(new ListSelectionListener ()
        {
             @Override
             public void valueChanged(ListSelectionEvent e)
             {
                 if (swIgnEvento)
                     return;
                 if (e.getValueIsAdjusting()) // && e.getFirstIndex() == e.getLastIndex())
                     return;
                 if (!jt.isEnabled())
                     return;
                 swIgnEvento=true;
                 verDatosDesg();                
//                 jt.requestFocusSelectedLater();
                 swIgnEvento=false;
             }
        });
        Bprint.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                genListado();
            }
        });
        
       jtDes.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
              if (e.getClickCount()<2)
                  return;
              if (jtDes.isVacio() || jf==null )
                  return;
              ejecutable prog;
              if ((prog=jf.gestor.getProceso("gnu.chu.anjelica.despiece.MantDesp"))==null)
                  return;
              gnu.chu.anjelica.despiece.MantDesp cm=(gnu.chu.anjelica.despiece.MantDesp) prog;
              cm.PADQuery();
              cm.setDeoCodi(jtDes.getValString(0));
              cm.ej_query();
              jf.gestor.ir(cm);
           }
      });
    }

    void buscaDatos() {
        String s;
        try {
            if (feciniE.isNull()) {
                mensajeErr("Introduzca Fecha Inicio");
                feciniE.requestFocus();
                return;
            }

            if (fecfinE.isNull()) {
                mensajeErr("Introduzca Fecha Final");
                fecfinE.requestFocus();
                return;
            }
            new miThread("")
            {
                @Override
               public void run() {
                   msgEspere("Espere, por favor, buscando despieces");
                  
                   jt.setEnabled(false);
                   boolean ret=buscaDatos0();
                   
                   resetMsgEspere();
                   if (!ret)
                    return;
                   
                   SwingUtilities.invokeLater(new Thread(){
                        @Override
                        public void run()
                        {
                            jt.setDatos(dtCon1);
                            try {
                                buscaTarifa();
                                buscaVenta();
                            } catch (SQLException | ParseException k)
                            {
                                Error("Error al buscar tarifa",k);
                                return;
                            }
                            jt.requestFocusInicio();
                            jt.setEnabled(true);
                            int rowCount=jt.getRowCount();
                            double kilosT=0,imporT=0;
                            int unidT=0;
                            for (int n=0;n<rowCount;n++)
                            {
                                kilosT+=jt.getValorDec(n,2);
                                unidT+=jt.getValorInt(n,3);
                                imporT+=jt.getValorDec(n,4);
                            }
                            unidTotE.setValorDec(unidT);
                            kilTotE.setValorDec(kilosT);
                            impTotE.setValorDec(imporT);
                            verDatosDesg();
                            mensajeErr("Consulta Realizada");
                        }
                   });
               }  
            };
        
        } catch (Exception k) {
            Error("Error al Buscar datos", k);
        }
    }
    private boolean buscaDatos0() {
        try {
            String s = getStrSelect();
            jt.panelG.setVisible(false);
            jt.removeAllDatos();
            if (!dtCon1.select(s)) {
                msgBox("No encontrados despieces con estos criterios");
                jt.panelG.setVisible(true);
                return false;
            }
           
        } catch (SQLException k ) {
            Error("Error al Buscar datos", k);
            return false;
        }
        return true;
    }
    /**
     * Busca Precios tarifa
     * @throws SQLException 
     */
    private void buscaTarifa() throws SQLException,ParseException
    {
        int nRow=jt.getRowCount();
        
        for (int n=0;n<nRow;n++)
        {
            jt.setValor(
            MantTarifa.getPrecTar(dtStat,jt.getValorInt(n,0) ,0,TARIFA_MAYOR, 
                Formatear.sumaDias(feciniE.getDate(),7)),
                n,JT_PRTARIFA);
        }
    }
    private void buscaVenta() throws SQLException,ParseException
    {
        int nRow=jt.getRowCount();
        String s="SELECT sum(avl_canti) as canti,sum(avl_canti*avl_prbase) as importe FROM v_albventa where pro_codi=?"+
            " and avc_fecalb between to_date('"+
            (opVentasSem.isSelected()?feciniE.getFecha("dd-MM-yyyy"): Formatear.sumaDias(feciniE.getDate(),7))+"','dd-MM-yyyy')"+ 
            " and to_date('"+(opVentasSem.isSelected()?fecfinE.getFecha("dd-MM-yyyy"):  Formatear.sumaDias(fecfinE.getDate(),7))+"','dd-MM-yyyy')";
        PreparedStatement ps1=dtStat.getPreparedStatement(s);
        ResultSet rs;
        double importe;
        for (int n=0;n<nRow;n++)
        {
            ps1.setInt(1, jt.getValorInt(n,JT_PROCOD));
            rs=ps1.executeQuery();
            rs.next();
            importe=rs.getDouble("canti")==0?0: rs.getDouble("importe")/  rs.getDouble("canti");
            jt.setValor( rs.getDouble("canti"),n,JT_KGVENTA);
            jt.setValor( importe,n,JT_PRVENTA);
        }
    }
    private String getStrSelect() {
        fam_codentE.isNull();
        condWhere= " l.eje_nume= C.EJE_NUME "
             + " and deo_fecha >= TO_DATE('" + feciniE.getText() + "','dd-MM-yyyy') "
             + " and deo_fecha <= TO_DATE('" + fecfinE.getText() + "','dd-MM-yyyy') "
             + (opSoloValor.isSelected()?" and deo_valor = 'S'":"")
             + (opVer.getValor().equals("S") ?  " and (l.def_tippro='S' or c.deo_incval='S') ":"")
            + (opVer.getValor().equals("N") ?  " and (l.def_tippro='N' AND c.deo_incval='N') ":"")
             + (tid_codiE.isNull()?"":" and c.tid_codi = "+tid_codiE.getValorInt())
             + " and c.deo_codi = l.deo_codi "
             + " and l.def_kilos > 0 "
             + (alm_codiE.isNull()?"": " and c.deo_almdes  = "+alm_codiE.getValorInt() )
             + " and c.eje_nume = c.eje_nume "             
             + (tipoProenE.getValor().equals("T") && fam_codentE.isNull() ?"":
                 " and exists (select * from  v_articulo as ar,v_despori as po" +
                 " where ar.pro_codi= po.pro_codi"
                + (tipoProenE.getValor().equals("T")?"":" and ar.pro_artcon  = "+tipoProenE.getValor())
                 + " and ar.pro_tiplot = 'V' " // Producto es vendible.
                 + (fam_codentE.isNull()?"": " and ar.fam_codi = "+fam_codentE.getValorInt() )               
                 + " and po.eje_nume = c.eje_nume "
                 + " and po.deo_codi= c.deo_codi) ")
                + (opIncReg.isSelected()  ?"":
                 " and c.tid_codi != "+MantTipDesp.AUTO_DESPIECE);
        String s;
        
        s = "select l.pro_codi,a.pro_nomb,sum(def_kilos) as kilos,"
             + " sum (def_numpie) as def_numpie, "
             + " sum(def_kilos* (def_prcost"+(opIncCosto.isSelected()?"+ pro_cosinc":"")+")"
             + " ) as costo,sum(def_kilos* (def_prcost"+(opIncCosto.isSelected()?"+ pro_cosinc":"")+")"
            + " )/sum(def_kilos) as imp,0 as precTarifa,0 as kgVenta,0 as prVenta,a.fam_codi "
             + " from v_despfin l ,desporig c,v_articulo a "
             + (grp_codiE.isNull() ?"":", v_famipro as fp ")
             + " where "+condWhere
             + " and a.pro_codi = l.pro_codi "
             + " and a.pro_tiplot = 'V' " // Producto es vendible.
             + (sbe_codiE.isNull()?"": " and a.sbe_codi = "+sbe_codiE.getValorInt() )
             + (grp_codiE.isNull() ?"":" and fp.fpr_codi = a.fam_codi ")
             + (fam_codiE.isNull()?"": " and a.fam_codi = "+fam_codiE.getValorInt() )
             + (grp_codiE.isNull()?"": " and fp.agr_codi = "+grp_codiE.getValorInt() )
             + (tipoProdE.getValor().equals("T")?"": " and pro_artcon  = "+tipoProdE.getValor() )
             + " group by a.fam_codi,l.pro_Codi,a.pro_nomb "
             + " order by a.fam_codi,l.pro_codi";
        return s;
    }
    /**
     * Muestra los datos de desglose.
     */
    void verDatosDesg()
    {
        jtDes.removeAllDatos();
        String prvNomb;
        String s = "select c.deo_codi,l.pro_codi,l.def_ejelot,l.def_serlot,l.pro_lote,"+
                " sum(def_numpie) as def_numpie,sum(def_kilos) as def_kilos "
             + " from v_despfin l,desporig c  "
             + " where "+condWhere+
             " and l.pro_codi = "+jt.getValorInt(0)+
             " group by c.deo_codi,l.pro_codi,l.def_ejelot,l.def_serlot,l.pro_lote "+
             " order by l.def_ejelot,l.def_serlot,l.pro_lote";
        try {
            Boolean swCong;
            int mesCadCong=23;
            if (dtDesp.select(s))
            do {
             swCong=gnu.chu.anjelica.pad.MantArticulos.isCongelado(jt.getValorInt(0), dtStat);
             if (swCong != null)
                  mesCadCong=dtStat.getInt("pro_cadcong");
//             utdesp.setNumDesp(dtDesp.getInt("deo_numdes"));
             if (!utdesp.busDatInd(dtDesp.getString("def_serlot"), dtDesp.getInt("pro_codi"),
                        EU.em_cod, dtDesp.getInt("def_ejelot"), dtDesp.getInt("pro_lote"),
                         0,dtAux,dtStat, EU))
                prvNomb=null;
             else
                prvNomb=gnu.chu.anjelica.pad.pdprove.getNombPrv(utdesp.getPrvCompra(),dtStat);
             ArrayList v=new ArrayList ();
             v.add(dtDesp.getInt("deo_codi"));
             v.add(prvNomb);
             v.add(""+dtDesp.getInt("pro_lote"));
             v.add(""+dtDesp.getInt("def_numpie"));
             v.add(dtDesp.getString("def_kilos "));
             v.add(utdesp.getFecCompra());
             v.add(utdesp.getFecCadPrv());
             v.add(utdesp.getFecDesp());
             if (! swCong)
                v.add(utdesp.getFecCaduc());
             else
             {
                java.util.Date fecCong=Formatear.sumaMesDate(utdesp.getFechaProduccion(),mesCadCong);
                v.add(Formatear.getFecha(fecCong,"dd-MM-yyyy"));
             }
             jtDes.addLinea(v);
            } while (dtDesp.next());
//            jtDes.requestFocusInicio();
        } catch (SQLException k)
        {
            Error("Error al ver datos de desglose",k);
        }
    }
    void genListado() {
        try {
            mensaje("Espere ... generando listado");
            String s = getStrSelect();
            dtCon1.setStrSelect(s);
            ResultSet rs = ct.createStatement().executeQuery(dtCon1.getStrSelect());

            JasperReport jr = null;
            jr = gnu.chu.print.util.getJasperReport(EU,  "lidesgen");
            java.util.HashMap mp = new java.util.HashMap();
            mp.put("fecini", feciniE.getText());
            mp.put("fecfin", fecfinE.getText());
            JasperPrint jp = JasperFillManager.fillReport(jr, mp, new JRResultSetDataSource(rs));
            if (gnu.chu.print.util.printJasper(jp, EU)) {
                mensajeErr("Listado ... Generado");
            }
            mensaje("");
        } catch (Exception k) {
            Error("Error al Generar Listado", k);
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

        Pprinc = new gnu.chu.controles.CPanel();
        Pentra = new gnu.chu.controles.CPanel();
        CLabel1 = new gnu.chu.controles.CLabel();
        feciniE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        CLabel2 = new gnu.chu.controles.CLabel();
        fecfinE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        CLabel3 = new gnu.chu.controles.CLabel();
        fam_codiE = new gnu.chu.controles.CLinkBox();
        CLabel4 = new gnu.chu.controles.CLabel();
        tipoProdE = new gnu.chu.controles.CComboBox();
        CLabel8 = new gnu.chu.controles.CLabel();
        opVer = new gnu.chu.controles.CComboBox();
        Baceptar = new gnu.chu.controles.CButton(Iconos.getImageIcon("check"));
        CLabel10 = new gnu.chu.controles.CLabel();
        grp_codiE = new gnu.chu.controles.CLinkBox();
        CLabel11 = new gnu.chu.controles.CLabel();
        alm_codiE = new gnu.chu.controles.CLinkBox();
        CLabel12 = new gnu.chu.controles.CLabel();
        tipoProenE = new gnu.chu.controles.CComboBox();
        CLabel13 = new gnu.chu.controles.CLabel();
        tid_codiE = new gnu.chu.camposdb.tidCodi2();
        CLabel9 = new gnu.chu.controles.CLabel();
        fam_codiE1 = new gnu.chu.controles.CLinkBox();
        CLabel14 = new gnu.chu.controles.CLabel();
        fam_codentE = new gnu.chu.controles.CLinkBox();
        opSoloValor = new gnu.chu.controles.CCheckBox();
        opIncReg = new gnu.chu.controles.CCheckBox();
        opIncCosto = new gnu.chu.controles.CCheckBox();
        opVentasSem = new gnu.chu.controles.CCheckBox();
        CLabel15 = new gnu.chu.controles.CLabel();
        sbe_codiE = new gnu.chu.controles.CLinkBox();
        jtDes = new gnu.chu.controles.Cgrid(9);
        Pfinal = new gnu.chu.controles.CPanel();
        CLabel5 = new gnu.chu.controles.CLabel();
        unidTotE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---,--9");
        CLabel6 = new gnu.chu.controles.CLabel();
        kilTotE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----,--9.9");
        CLabel7 = new gnu.chu.controles.CLabel();
        impTotE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--,---,--9.9");
        opDesgl = new gnu.chu.controles.CCheckBox();
        Bprint = new gnu.chu.controles.CButton(Iconos.getImageIcon("print"));
        jt = new gnu.chu.controles.Cgrid(10);

        Pprinc.setLayout(new java.awt.GridBagLayout());

        Pentra.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Pentra.setMaximumSize(new java.awt.Dimension(559, 125));
        Pentra.setMinimumSize(new java.awt.Dimension(559, 125));
        Pentra.setPreferredSize(new java.awt.Dimension(559, 125));
        Pentra.setQuery(true);
        Pentra.setLayout(null);

        CLabel1.setText("De Fecha");
        Pentra.add(CLabel1);
        CLabel1.setBounds(10, 1, 55, 15);

        feciniE.setMinimumSize(new java.awt.Dimension(2, 18));
        feciniE.setPreferredSize(new java.awt.Dimension(2, 18));
        Pentra.add(feciniE);
        feciniE.setBounds(60, 1, 79, 19);

        CLabel2.setText("A Fecha");
        CLabel2.setPreferredSize(new java.awt.Dimension(43, 18));
        Pentra.add(CLabel2);
        CLabel2.setBounds(160, 1, 44, 18);
        Pentra.add(fecfinE);
        fecfinE.setBounds(210, 1, 79, 19);

        CLabel3.setText("Familia");
        Pentra.add(CLabel3);
        CLabel3.setBounds(238, 42, 38, 18);

        fam_codiE.setAncTexto(30);
        fam_codiE.setFormato(Types.DECIMAL,"##9");
        Pentra.add(fam_codiE);
        fam_codiE.setBounds(283, 42, 270, 18);

        CLabel4.setText("Tipo Prod. Generado");
        Pentra.add(CLabel4);
        CLabel4.setBounds(3, 22, 136, 18);

        tipoProdE.setMinimumSize(new java.awt.Dimension(123, 18));
        tipoProdE.addItem("Todos", "T");
        tipoProdE.addItem("Congelado", "-1");
        tipoProdE.addItem("No Congelado", "0");
        Pentra.add(tipoProdE);
        tipoProdE.setBounds(129, 22, 140, 18);

        CLabel8.setText("Ver");
        CLabel8.setPreferredSize(new java.awt.Dimension(20, 18));
        Pentra.add(CLabel8);
        CLabel8.setBounds(300, 0, 20, 19);

        opVer.setPreferredSize(new java.awt.Dimension(28, 18));
        opVer.addItem("Todos","T");
        opVer.addItem("Produccion","S");
        opVer.addItem("Tactil","T");
        opVer.addItem("Despieces","N");
        Pentra.add(opVer);
        opVer.setBounds(340, 0, 110, 19);

        Baceptar.setText("Aceptar");
        Pentra.add(Baceptar);
        Baceptar.setBounds(450, 90, 100, 27);

        CLabel10.setText("Grupo");
        Pentra.add(CLabel10);
        CLabel10.setBounds(3, 42, 34, 18);

        grp_codiE.setAncTexto(30);
        grp_codiE.setFormato(Types.DECIMAL,"##9");
        Pentra.add(grp_codiE);
        grp_codiE.setBounds(40, 42, 190, 18);

        CLabel11.setText("Almacen");
        Pentra.add(CLabel11);
        CLabel11.setBounds(273, 22, 48, 18);

        alm_codiE.setAncTexto(30);
        alm_codiE.setFormato(Types.DECIMAL,"##9");
        Pentra.add(alm_codiE);
        alm_codiE.setBounds(325, 22, 230, 18);

        CLabel12.setText("Tipo Prod. Entrada");
        Pentra.add(CLabel12);
        CLabel12.setBounds(3, 62, 110, 18);

        tipoProenE.addItem("Todos", "T");
        tipoProenE.addItem("Congelado", "-1");
        tipoProenE.addItem("No Congelado", "0");
        Pentra.add(tipoProenE);
        tipoProenE.setBounds(110, 62, 120, 18);

        CLabel13.setText("Sección");
        Pentra.add(CLabel13);
        CLabel13.setBounds(2, 100, 70, 18);

        tid_codiE.setAncTexto(40);
        tid_codiE.getComboBox().setPreferredSize(new Dimension(400,18));
        Pentra.add(tid_codiE);
        tid_codiE.setBounds(70, 82, 240, 18);

        CLabel9.setText("Familia");
        Pentra.add(CLabel9);
        CLabel9.setBounds(238, 42, 38, 18);

        fam_codiE1.setAncTexto(30);
        fam_codiE.setFormato(Types.DECIMAL,"##9");
        Pentra.add(fam_codiE1);
        fam_codiE1.setBounds(283, 42, 255, 18);

        CLabel14.setText("Familia Entrada");
        Pentra.add(CLabel14);
        CLabel14.setBounds(230, 62, 90, 18);

        fam_codentE.setAncTexto(30);
        fam_codentE.setFormato(Types.DECIMAL,"##9");
        Pentra.add(fam_codentE);
        fam_codentE.setBounds(315, 62, 240, 18);

        opSoloValor.setSelected(true);
        opSoloValor.setText("Solo Valorados");
        opSoloValor.setToolTipText("Incluir productos que se generan a si mismos");
        opSoloValor.setPreferredSize(new java.awt.Dimension(83, 18));
        opSoloValor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opSoloValorActionPerformed(evt);
            }
        });
        Pentra.add(opSoloValor);
        opSoloValor.setBounds(230, 100, 100, 18);
        opSoloValor.getAccessibleContext().setAccessibleName("Inc.Costo Prod.");

        opIncReg.setText("Inc. reenv.");
        opIncReg.setToolTipText("Incluir productos que se generan a si mismos");
        opIncReg.setPreferredSize(new java.awt.Dimension(83, 18));
        Pentra.add(opIncReg);
        opIncReg.setBounds(463, 0, 90, 18);

        opIncCosto.setSelected(true);
        opIncCosto.setText("Inc. Costo Producc");
        opIncCosto.setToolTipText("Incluir Costo de Produccion");
        opIncCosto.setActionCommand("");
        opIncCosto.setPreferredSize(new java.awt.Dimension(83, 18));
        Pentra.add(opIncCosto);
        opIncCosto.setBounds(330, 82, 120, 18);

        opVentasSem.setText("Ventas Semana");
        opVentasSem.setToolTipText("Ventas del periodo desp.");
        opVentasSem.setPreferredSize(new java.awt.Dimension(83, 18));
        opVentasSem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opVentasSemActionPerformed(evt);
            }
        });
        Pentra.add(opVentasSem);
        opVentasSem.setBounds(330, 100, 120, 18);

        CLabel15.setText("Tipo  Desp");
        Pentra.add(CLabel15);
        CLabel15.setBounds(3, 82, 70, 18);

        sbe_codiE.setAncTexto(25);
        sbe_codiE.setFormato(Types.DECIMAL,"#9");
        Pentra.add(sbe_codiE);
        sbe_codiE.setBounds(50, 100, 178, 18);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        Pprinc.add(Pentra, gridBagConstraints);

        jtDes.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtDes.setMaximumSize(new java.awt.Dimension(600, 100));
        jtDes.setMinimumSize(new java.awt.Dimension(600, 100));
        {Vector v = new Vector();
            v.add("Desp"); //  0 Proveedor
            v.add("Proveed"); //  0 Proveedor
            v.add("Lote"); // 1
            v.add("Unid."); // 2
            v.add("Kilos"); // 3
            v.add("Fec.Com"); // 4
            v.add("Fec.Cad"); // 5
            v.add("Fec.Desp"); // 6
            v.add("Fec.Cad."); // 7
            jtDes.setCabecera(v);
        }
        jtDes.setAlinearColumna(new int[]{2,0,0,2,2,1,1,1,1});
        jtDes.setAnchoColumna(new int[]{50,130,70,40,60,70,70,70,70});
        jtDes.setFormatoColumna(0,"####9");
        jtDes.setFormatoColumna(3,"---,--9");
        jtDes.setFormatoColumna(4,"----,--9.99");
        jtDes.setFormatoColumna(5, "dd-MM-yy");
        jtDes.setFormatoColumna(6, "dd-MM-yy");
        jtDes.setFormatoColumna(7, "dd-MM-yy");
        jtDes.setFormatoColumna(8, "dd-MM-yy");
        jtDes.setAjustarGrid(true);

        org.jdesktop.layout.GroupLayout jtDesLayout = new org.jdesktop.layout.GroupLayout(jtDes);
        jtDes.setLayout(jtDesLayout);
        jtDesLayout.setHorizontalGroup(
            jtDesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 713, Short.MAX_VALUE)
        );
        jtDesLayout.setVerticalGroup(
            jtDesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 148, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Pprinc.add(jtDes, gridBagConstraints);

        Pfinal.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        Pfinal.setMaximumSize(new java.awt.Dimension(580, 25));
        Pfinal.setMinimumSize(new java.awt.Dimension(580, 25));
        Pfinal.setPreferredSize(new java.awt.Dimension(580, 25));

        CLabel5.setText("Unidades");

        unidTotE.setEditable(false);

        CLabel6.setText("Kilos");

        kilTotE.setEditable(false);

        CLabel7.setText("Importe");

        impTotE.setEditable(false);

        opDesgl.setText("Desglosado");

        Bprint.setText("Impr");

        org.jdesktop.layout.GroupLayout PfinalLayout = new org.jdesktop.layout.GroupLayout(Pfinal);
        Pfinal.setLayout(PfinalLayout);
        PfinalLayout.setHorizontalGroup(
            PfinalLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(PfinalLayout.createSequentialGroup()
                .add(CLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(4, 4, 4)
                .add(unidTotE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 52, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(CLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(2, 2, 2)
                .add(kilTotE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(2, 2, 2)
                .add(CLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(2, 2, 2)
                .add(impTotE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(opDesgl, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(Bprint, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 107, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );
        PfinalLayout.setVerticalGroup(
            PfinalLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(PfinalLayout.createSequentialGroup()
                .add(PfinalLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, Bprint, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, PfinalLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, opDesgl, 0, 0, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, unidTotE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, kilTotE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, impTotE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, PfinalLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(CLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(CLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(CLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        Pprinc.add(Pfinal, gridBagConstraints);

        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jt.setMaximumSize(new java.awt.Dimension(600, 200));
        jt.setMinimumSize(new java.awt.Dimension(600, 200));

        org.jdesktop.layout.GroupLayout jtLayout = new org.jdesktop.layout.GroupLayout(jt);
        jt.setLayout(jtLayout);
        jtLayout.setHorizontalGroup(
            jtLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 713, Short.MAX_VALUE)
        );
        jtLayout.setVerticalGroup(
            jtLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 148, Short.MAX_VALUE)
        );

        Vector v=new Vector();

        v.add("Producto"); // 0
        v.add("Descripcion"); // 1
        v.add("Kilos"); // 2
        v.add("Unid"); // 3
        v.add("Importe"); // 4
        v.add("Costo"); // 5
        v.add("Tarifa"); // 6
        v.add("Kg.Venta"); // 7
        v.add("Pr.Venta"); // 8
        v.add("Fam"); // 9
        jt.setCabecera(v);
        jt.setAnchoColumna(new int[]{50,200,60,40,70,60,40,45,45,40});
        jt.setAlinearColumna(new int[]{0,0,2,2,2,2,2,2,2,2});
        jt.setFormatoColumna(2,"---,--9.99");
        jt.setFormatoColumna(3,"--,--9");
        jt.setFormatoColumna(4,"----,--9.99");
        jt.setFormatoColumna(5,"--9.9999");
        jt.setFormatoColumna(JT_PRTARIFA,"#9.99");
        jt.setFormatoColumna(JT_KGVENTA,"--,--9.9");
        jt.setFormatoColumna(JT_PRVENTA,"--9.99");
        jt.setAjustarGrid(true);

        jt.setNumRegCargar(0);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        Pprinc.add(jt, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void opSoloValorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opSoloValorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_opSoloValorActionPerformed

    private void opVentasSemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opVentasSemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_opVentasSemActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CButton Bprint;
    private gnu.chu.controles.CLabel CLabel1;
    private gnu.chu.controles.CLabel CLabel10;
    private gnu.chu.controles.CLabel CLabel11;
    private gnu.chu.controles.CLabel CLabel12;
    private gnu.chu.controles.CLabel CLabel13;
    private gnu.chu.controles.CLabel CLabel14;
    private gnu.chu.controles.CLabel CLabel15;
    private gnu.chu.controles.CLabel CLabel2;
    private gnu.chu.controles.CLabel CLabel3;
    private gnu.chu.controles.CLabel CLabel4;
    private gnu.chu.controles.CLabel CLabel5;
    private gnu.chu.controles.CLabel CLabel6;
    private gnu.chu.controles.CLabel CLabel7;
    private gnu.chu.controles.CLabel CLabel8;
    private gnu.chu.controles.CLabel CLabel9;
    private gnu.chu.controles.CPanel Pentra;
    private gnu.chu.controles.CPanel Pfinal;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CLinkBox alm_codiE;
    private gnu.chu.controles.CLinkBox fam_codentE;
    private gnu.chu.controles.CLinkBox fam_codiE;
    private gnu.chu.controles.CLinkBox fam_codiE1;
    private gnu.chu.controles.CTextField fecfinE;
    private gnu.chu.controles.CTextField feciniE;
    private gnu.chu.controles.CLinkBox grp_codiE;
    private gnu.chu.controles.CTextField impTotE;
    private gnu.chu.controles.Cgrid jt;
    private gnu.chu.controles.Cgrid jtDes;
    private gnu.chu.controles.CTextField kilTotE;
    private gnu.chu.controles.CCheckBox opDesgl;
    private gnu.chu.controles.CCheckBox opIncCosto;
    private gnu.chu.controles.CCheckBox opIncReg;
    private gnu.chu.controles.CCheckBox opSoloValor;
    private gnu.chu.controles.CCheckBox opVentasSem;
    private gnu.chu.controles.CComboBox opVer;
    private gnu.chu.controles.CLinkBox sbe_codiE;
    private gnu.chu.camposdb.tidCodi2 tid_codiE;
    private gnu.chu.controles.CComboBox tipoProdE;
    private gnu.chu.controles.CComboBox tipoProenE;
    private gnu.chu.controles.CTextField unidTotE;
    // End of variables declaration//GEN-END:variables

}
