package gnu.chu.anjelica.compras;

import gnu.chu.Menu.Principal;
import gnu.chu.controles.StatusBar;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.mensajes;
import gnu.chu.utilidades.miThread;
import gnu.chu.utilidades.ventana;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.SwingUtilities;

/**
 *
 * <p>Titulo: Clrelalbco</p>
 * <p>Descripción: Consulta/Listado Relacion Albaranes de Compras</p>
 * <p>Copyright: Copyright (c) 2005-2015</p>
 * <p> El importe de la fras es solo la parte enlazada al albaran. No el total de la fra.</p>
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
 * @version 1.0
 * Created on 27-mar-2010, 9:40:54
 */
public class Clrelalbco extends ventana {
    double impAlb=0,kgAlb=0;
    double impFra=0,kgFra=0;
    ArrayList<ArrayList> vGrid=new ArrayList();
    final private String CONSULTAR="Consultar";
    final private String LISTAR="Listar";
    String s;
    
    public Clrelalbco(EntornoUsuario eu, Principal p) {
        EU = eu;
        vl = p.panel1;
        jf = p;
        eje = true;

        setTitulo("Cons./List Relacion Albaranes de Compras");

        try {
            if (jf.gestor.apuntar(this)) {
                jbInit();
            } else {
                setErrorInit(true);
            }
        } catch (Exception e) {
          ErrorInit(e);
        }
    }

    public Clrelalbco(gnu.chu.anjelica.menu p, EntornoUsuario eu) {

        EU = eu;
        vl = p.getLayeredPane();
        setTitulo("Cons./List Relacion Albaranes de Compras");
        eje = false;

        try {
            jbInit();
        } catch (Exception e) {
           ErrorInit(e);
        }
    }

    private void jbInit() throws Exception {

        iniciarFrame();

        this.setVersion("2014-12-02");
        statusBar = new StatusBar(this);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        conecta();

        initComponents();
        this.setSize(new Dimension(730, 530));
    }

    @Override
    public void iniciarVentana() throws Exception {
        prvIniE.iniciar(dtStat, this, vl, EU);
        prvFinE.iniciar(dtStat, this, vl, EU);
        Pentra.setDefButton(Baceptar.getBotonAccion());
        emp_codiE.iniciar(dtStat, this, vl, EU);
        emp_codiE.setAceptaNulo(true);
        fecfinE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
        feciniE.setText(Formatear.sumaDias(new Date(System.currentTimeMillis()), -30));
        activarEventos();
    }
    private void activarEventos()
    {
        Baceptar.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
                 if (e.getActionCommand().equals("ACTUAL"))
                 {
                    actual();
                    return;
                 }
                if (! e.getActionCommand().equals(LISTAR))
                {
                    consultar();
                    return;
                }
                msgBox("Opcion NO DISPONIBLE");
            }
        });
    }
    private void actual() {

        s = "select l.* from v_facaco as c, v_falico as l "
                + "  WHERE  c.emp_codi = l.emp_codi "
                + " AND c.eje_nume = l.eje_nume "
                + " and c.fcc_nume = l.fcc_nume "
                + " and c.fcc_fecfra >= to_date('" + feciniE.getText() + "','dd-MM-yyyy')"
                + " and c.fcc_fecfra <= to_date('" + fecfinE.getText() + "','dd-MM-yyyy')"
                + (emp_codiE.getValorInt()==0?"":" and c.emp_codi = " + emp_codiE.getValorInt())
                + " and acc_ano =  0 "
                + " order by c.fcc_nume";
        try {
            DatosTabla dtUpd=new DatosTabla(ctUp);
            if (!dtCon1.select(s)) {
                msgBox("No encontradas fras.");
                return;
            }
            String msgErr="";
            do {
              s="select * from  v_falico  "
               + "  WHERE  emp_codi =  "+dtCon1.getInt("emp_codi")
                + " AND eje_nume =  "+dtCon1.getInt("eje_nume")
                + " and fcc_nume = "+dtCon1.getInt("fcc_nume")
                + " and acc_ano != 0";
              double importe=Math.abs(dtCon1.getDouble("fcl_canti")*dtCon1.getDouble("fcl_prcom"));
              if (! dtStat.select(s))
              { // Supongo que es una linea de abono. busco en agrupaciones el cargo.
                s="select * from grufaco, v_falico where eje_nume1="+dtCon1.getInt("eje_nume")+
                        " and emp_codi1 =  "+dtCon1.getInt("emp_codi")+
                        " and fcc_nume1 = "+dtCon1.getInt("fcc_nume")+
                        " and emp_codi = emp_codi2 "+
                        " and eje_nume = eje_nume2 "+
                        " and fcc_nume= fcc_nume2"+
                        " and abs(fcl_canti*fcl_prcom) between "+(importe - 1) +
                        " and "+ (importe+1) ;
                if (!dtStat.select(s))
                {
                    msgErr+="No encontrado albaran para fra: "+dtCon1.getInt("fcc_nume")+
                            " Importe: "+importe+"\n";
                    continue;
                }
                s="update  v_falico set  acc_ano=  "+dtStat.getInt("acc_ano")
                    + ", acc_nume = "+dtStat.getInt("acc_nume")
                    + ", acc_serie = '"+dtStat.getString("acc_serie")+"' "
                    + "  WHERE  emp_codi =  "+dtStat.getInt("emp_codi2")
                     + " AND eje_nume =  "+dtStat.getInt("eje_nume2")
                 + " and fcc_nume = "+dtStat.getInt("fcc_nume2")
                 + " fcl_canti = abs("+dtStat.getDouble("fcl_canti")+")";

              }
              s="update  v_falico set acc_ano=  "+dtStat.getInt("acc_ano")
                    + ", acc_nume = "+dtStat.getInt("acc_nume")
                    + ", acc_serie = '"+dtStat.getString("acc_serie")+"'"
                    +"  WHERE  emp_codi =  "+dtCon1.getInt("emp_codi")
                    + " AND eje_nume =  "+dtCon1.getInt("eje_nume")
                    + " and fcc_nume = "+dtCon1.getInt("fcc_nume")
                    + " and fcl_numlin = "+dtCon1.getInt("fcl_numlin");
              dtUpd.executeUpdate(s);

            } while (dtCon1.next());
             dtUpd.commit();
             mensajes.mensajeExplica("Problemas con Fras",msgErr);
        } catch (SQLException k) {
            Error("Error al actualizar", k);
        }

    }
    private void consultar() {
        if (!checkEntrada()) {
            return;
        }
        jt.removeAllDatos();
        vGrid.clear();
        new miThread("")
        {
            @Override
            public void run()
            {
                msgEspere("Buscando datos");
                consulta1();
                resetMsgEspere();
                mensajeErr("Consulta Realizada");
            }
        };
    }

    void consulta1()
    {
        try {
            boolean ordenPrv=ordenE.getValor().equals("P");
            s = "SELECT c.*,pv.prv_nomb FROM v_albacoc as c left join v_proveedo as pv on c.prv_codi = pv.prv_codi "
                    + " WHERE c.emp_codi = " + EU.em_cod
                    + " AND c.acc_fecrec >= to_date('" + feciniE.getText() + "','dd-MM-yyyy')"
                    + " AND c.acc_fecrec <= to_date('" + fecfinE.getText() + "','dd-MM-yyyy')"
                    + (prvIniE.isNull()?"": " and c.prv_codi >= "+prvIniE.getText())
                    + (prvFinE.isNull()?"": " and c.prv_codi <= "+prvFinE.getText())+
                    " order by "+
                    (ordenPrv?"c.prv_codi, ":"")+"  c.acc_fecrec,c.acc_nume";
            if (!dtCon1.select(s)) {
                msgBox("No encontrados Albaranes con esos criterios");
                return;
            }
            //dtCon1.getDouble("acl_canti",true) * (dtCon1.getDouble("acl_prcom",true) - (incPortes?0:acc_impokgE.getValorDec()));
            impAlb=0;kgAlb=0;
            impFra=0;kgFra=0;

            String fechaAlb="";
            double cantiP=0,imporP=0;
            int prvAntiguo=dtCon1.getInt("prv_codi");
            String prvNombAnt=dtCon1.getString("prv_nomb");
            int nAlbPrv=0;
            do
            {
                if (! dtCon1.getFecha("acc_fecrec").equals("fechaAlb"))
                {
                 fechaAlb=dtCon1.getFecha("acc_fecrec");
                 actualizaMsg("Tratando albaran de fecha: "+dtCon1.getFecha("acc_fecrec"),false);
                }
                if (ordenPrv && prvAntiguo!=dtCon1.getInt("prv_codi") && ordenPrv)
                {
                    if (nAlbPrv>1)
                    {
                        impTotPrv(prvAntiguo,prvNombAnt,cantiP,imporP);
                    }
                    cantiP=0;
                    imporP=0;
                    prvAntiguo=dtCon1.getInt("prv_codi");
                    prvNombAnt=dtCon1.getString("prv_nomb");
                    nAlbPrv=0;
                 }
                ArrayList v=new ArrayList();
                v.add(Formatear.format(dtCon1.getInt("emp_codi"),"#9")+"-"+
                        Formatear.format(dtCon1.getInt("acc_ano"),"###9")+
                        dtCon1.getString("acc_serie")+
                        Formatear.format(dtCon1.getInt("acc_nume"),"99999"));
                v.add(dtCon1.getFecha("acc_fecrec"));
                v.add(dtCon1.getString("prv_codi"));
                v.add(dtCon1.getString("prv_nomb"));
                s="select sum(acl_canti) as acl_canti, sum((acl_prcom" +
                       (opIncPortes.isSelected()?"":"-"+dtCon1.getDouble("acc_impokg"))+
                       ") * acl_canti) as acl_prcom from v_albacol"+
                       " WHERE acc_ano= "+dtCon1.getInt("acc_ano")+
                       (emp_codiE.getValorInt()==0?"":" and emp_codi = "+emp_codiE.getValorInt())+
                       " and acc_serie = '"+dtCon1.getString("acc_serie")+"'"+
                       " and acc_nume = "+dtCon1.getInt("acc_nume");
                dtStat.select(s);
                v.add(""+dtStat.getDouble("acl_prcom",true));
                v.add(""+dtStat.getDouble("acl_canti",true));
                if (ordenPrv)
                {
                    cantiP+=dtStat.getDouble("acl_canti",true);
                    imporP+=dtStat.getDouble("acl_prcom",true);
                    nAlbPrv++;
                }
                impAlb+=dtStat.getDouble("acl_prcom",true);
                kgAlb+=dtStat.getDouble("acl_canti",true);
                s = "select sum(fcl_canti) as fcl_canti,sum(fcl_canti*fcl_prcom -((fcl_prcom*fcl_dto)/100)) as fcl_prcom" +
                        " from v_facaco as c, v_falico as l "
                        + "  WHERE  c.emp_codi = l.emp_codi "
                        + " AND c.eje_nume = l.eje_nume "
                        + " and c.fcc_nume = l.fcc_nume "
                        + (opMosDet.isSelected() ?   " and c.fcc_fecfra >= to_date('" + feciniE.getText() + "','dd-MM-yyyy')"
                        + " and c.fcc_fecfra <= to_date('" + fecfinE.getText() + "','dd-MM-yyyy')":"")
                        + " and acl_nulin > 0 "
                        + (emp_codiE.getValorInt()==0?"":" and c.emp_codi = " + emp_codiE.getValorInt())
                        + " AND acc_nume  =  " + dtCon1.getInt("acc_nume")
                        + " and acc_ano =  " + dtCon1.getInt("acc_ano")
                        + " and acc_serie = '" + dtCon1.getString("acc_serie") + "'";
                dtStat.select(s);
                v.add(""+dtStat.getDouble("fcl_prcom",true));
                v.add(""+dtStat.getDouble("fcl_canti",true));
              
                impFra+=dtStat.getDouble("fcl_prcom",true);
                kgFra+=dtStat.getDouble("fcl_canti",true);
                if (opMosDet.isSelected() || !ordenPrv)
                    vGrid.add(v);
            } while (dtCon1.next());
            if (ordenPrv && nAlbPrv>1)
                impTotPrv(prvAntiguo,prvNombAnt,cantiP, imporP);
            SwingUtilities.invokeLater(new Thread()
            {
                @Override
                public void run()
                {
                  jt.setDatos(vGrid);
                  kgAlbE.setValorDec(kgAlb);
                  impAlbE.setValorDec(impAlb);
                  KgFraE.setValorDec(kgFra);
                  impFraE.setValorDec(impFra);
                  impPenE.setValorDec(impAlb-impFra);
                  numAlbE.setValorInt(jt.getRowCount());
                }
            });
            
        } catch (SQLException k) {
            Error("Error al consultar datos", k);
        }
    }
    private void impTotPrv(int prvCodi,String prvNomb,double cantiP,double imporP)
    {
        ArrayList v1=new ArrayList();
        v1.add("");
        v1.add("");
        v1.add(prvCodi);
        v1.add(prvNomb);
        v1.add(imporP);
        v1.add(cantiP);
        v1.add("");
        v1.add("");
        vGrid.add(v1);
    }
    private boolean checkEntrada()
    {
        if (! emp_codiE.controla())
        {
            mensajeErr(emp_codiE.getMsgError());
            return false;
        }
        if (feciniE.isNull() || feciniE.getError())
        {
            mensajeErr("Fecha Inicio no puede estar vacia");
            feciniE.requestFocus();
            return false;
        }
         if (fecfinE.isNull() || fecfinE.getError())
        {
            mensajeErr("Fecha Final no puede estar vacia");
            fecfinE.requestFocus();
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
        Pentra = new gnu.chu.controles.CPanel();
        cLabel9 = new gnu.chu.controles.CLabel();
        emp_codiE = new gnu.chu.camposdb.empPanel();
        cLabel2 = new gnu.chu.controles.CLabel();
        feciniE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel3 = new gnu.chu.controles.CLabel();
        fecfinE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        prvIniE = new gnu.chu.camposdb.prvPanel();
        cLabel10 = new gnu.chu.controles.CLabel();
        cLabel11 = new gnu.chu.controles.CLabel();
        prvFinE = new gnu.chu.camposdb.prvPanel();
        Baceptar = new gnu.chu.controles.CButtonMenu(Iconos.getImageIcon("check"));
        opMosDet = new gnu.chu.controles.CCheckBox();
        cLabel12 = new gnu.chu.controles.CLabel();
        ordenE = new gnu.chu.controles.CComboBox();
        opRecFra = new gnu.chu.controles.CCheckBox();
        opIncPortes = new gnu.chu.controles.CCheckBox();
        jt = new gnu.chu.controles.Cgrid(8);
        Ppie = new gnu.chu.controles.CPanel();
        cLabel1 = new gnu.chu.controles.CLabel();
        impAlbE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--,---,--9.99");
        cLabel4 = new gnu.chu.controles.CLabel();
        impFraE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--,---,--9.99");
        cLabel5 = new gnu.chu.controles.CLabel();
        kgAlbE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----,--9.99");
        cLabel6 = new gnu.chu.controles.CLabel();
        KgFraE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----,--9.99");
        cLabel7 = new gnu.chu.controles.CLabel();
        impPenE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--,---,--9.99");
        cLabel8 = new gnu.chu.controles.CLabel();
        numAlbE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");

        Pprinc.setLayout(new java.awt.GridBagLayout());

        Pentra.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        Pentra.setMaximumSize(new java.awt.Dimension(570, 110));
        Pentra.setMinimumSize(new java.awt.Dimension(570, 110));
        Pentra.setPreferredSize(new java.awt.Dimension(570, 110));
        Pentra.setLayout(null);

        cLabel9.setText("Empresa");
        Pentra.add(cLabel9);
        cLabel9.setBounds(2, 2, 49, 15);

        emp_codiE.setMaximumSize(new java.awt.Dimension(2147483647, 18));
        emp_codiE.setMinimumSize(new java.awt.Dimension(28, 18));
        emp_codiE.setPreferredSize(new java.awt.Dimension(28, 18));
        Pentra.add(emp_codiE);
        emp_codiE.setBounds(55, 2, 40, 18);

        cLabel2.setText("De Fecha");
        Pentra.add(cLabel2);
        cLabel2.setBounds(5, 70, 55, 17);

        feciniE.setMinimumSize(new java.awt.Dimension(10, 18));
        feciniE.setPreferredSize(new java.awt.Dimension(10, 18));
        Pentra.add(feciniE);
        feciniE.setBounds(70, 70, 70, 17);

        cLabel3.setText("A Fecha");
        Pentra.add(cLabel3);
        cLabel3.setBounds(150, 70, 44, 17);
        Pentra.add(fecfinE);
        fecfinE.setBounds(200, 70, 70, 17);

        prvIniE.setMaximumSize(new java.awt.Dimension(2147483647, 18));
        prvIniE.setMinimumSize(new java.awt.Dimension(91, 18));
        prvIniE.setPreferredSize(new java.awt.Dimension(281, 18));
        Pentra.add(prvIniE);
        prvIniE.setBounds(84, 26, 374, 18);

        cLabel10.setText("De Proveedor");
        Pentra.add(cLabel10);
        cLabel10.setBounds(5, 26, 75, 18);

        cLabel11.setText("A Proveedor");
        Pentra.add(cLabel11);
        cLabel11.setBounds(5, 48, 69, 18);

        prvFinE.setMaximumSize(new java.awt.Dimension(2147483647, 18));
        prvFinE.setMinimumSize(new java.awt.Dimension(91, 18));
        prvFinE.setPreferredSize(new java.awt.Dimension(281, 18));
        Pentra.add(prvFinE);
        prvFinE.setBounds(84, 48, 374, 18);

        Baceptar.addMenu(CONSULTAR);
        Baceptar.addMenu(LISTAR);
        Baceptar.addMenu("ACTUAL");
        Baceptar.setText("Aceptar");
        Pentra.add(Baceptar);
        Baceptar.setBounds(430, 70, 133, 32);

        opMosDet.setSelected(true);
        opMosDet.setText("Mostrar Detalle");
        opMosDet.setToolTipText("Considerar importe no fact. si la fra no se recibio en el periodo introducido");
        Pentra.add(opMosDet);
        opMosDet.setBounds(280, 70, 140, 16);

        cLabel12.setText("Ordenar");
        Pentra.add(cLabel12);
        cLabel12.setBounds(130, 0, 50, 17);

        ordenE.addItem("Proveedor","P");
        ordenE.addItem("Fecha","F");
        Pentra.add(ordenE);
        ordenE.setBounds(180, 2, 140, 17);

        opRecFra.setText("Limitar Fras al periodo");
        opRecFra.setToolTipText("Considerar importe no fact. si la fra no se recibio en el periodo introducido");
        Pentra.add(opRecFra);
        opRecFra.setBounds(350, 2, 210, 16);

        opIncPortes.setSelected(true);
        opIncPortes.setText("Inc. Portes");
        opIncPortes.setToolTipText("Incluir Portes");
        Pentra.add(opIncPortes);
        opIncPortes.setBounds(460, 30, 100, 16);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        Pprinc.add(Pentra, gridBagConstraints);

        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        // Code of sub-components - not shown here

        // Layout setup code - not shown here
        ArrayList v=new ArrayList();
        v.add("Albaran"); // 0
        v.add("Fec.Alb");  // 1
        v.add("Proveed"); // 2
        v.add("Nombre Prv."); // 3
        v.add("Importe"); // 4
        v.add("Kilos"); // 5
        v.add("Imp.Fra"); // 6
        v.add("Kil.Fra"); // 7
        jt.setCabecera(v);
        jt.setAnchoColumna(new int[]{100,80,50,200,70,60,70,60});
        jt.setAlinearColumna(new int[]{2,1,2,0,2,2,2,2});
        jt.setAjustaPanel(true);
        jt.setFormatoColumna(4, "----,--9.99");
        jt.setFormatoColumna(5, "---,--9.9");
        jt.setFormatoColumna(6, "----,--9.99");
        jt.setFormatoColumna(7, "---,--9.9");
        Pprinc.add(jt);
        jt.setBounds(9, 120, 600, 210);
        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jt.setMinimumSize(new java.awt.Dimension(300, 300));

        org.jdesktop.layout.GroupLayout jtLayout = new org.jdesktop.layout.GroupLayout(jt);
        jt.setLayout(jtLayout);
        jtLayout.setHorizontalGroup(
            jtLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 613, Short.MAX_VALUE)
        );
        jtLayout.setVerticalGroup(
            jtLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 331, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 2);
        Pprinc.add(jt, gridBagConstraints);

        Ppie.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        cLabel1.setText("Importe Albaranes");
        cLabel1.setPreferredSize(new java.awt.Dimension(105, 18));

        impAlbE.setEnabled(false);
        impAlbE.setPreferredSize(new java.awt.Dimension(73, 18));

        cLabel4.setText("Importe Facturado");
        cLabel4.setPreferredSize(new java.awt.Dimension(105, 18));

        impFraE.setEnabled(false);
        impFraE.setPreferredSize(new java.awt.Dimension(73, 18));

        cLabel5.setText("Kilos Albaranes");
        cLabel5.setPreferredSize(new java.awt.Dimension(105, 18));

        kgAlbE.setEnabled(false);
        kgAlbE.setPreferredSize(new java.awt.Dimension(73, 18));

        cLabel6.setText("Kilos Facturados");
        cLabel6.setPreferredSize(new java.awt.Dimension(105, 18));

        KgFraE.setEnabled(false);
        KgFraE.setPreferredSize(new java.awt.Dimension(73, 18));

        cLabel7.setText("Importe Pend");
        cLabel7.setPreferredSize(new java.awt.Dimension(105, 18));

        impPenE.setEnabled(false);
        impPenE.setPreferredSize(new java.awt.Dimension(73, 18));

        cLabel8.setText("Num.Albaranes");
        cLabel8.setPreferredSize(new java.awt.Dimension(105, 18));

        numAlbE.setEnabled(false);
        numAlbE.setPreferredSize(new java.awt.Dimension(73, 18));

        org.jdesktop.layout.GroupLayout PpieLayout = new org.jdesktop.layout.GroupLayout(Ppie);
        Ppie.setLayout(PpieLayout);
        PpieLayout.setHorizontalGroup(
            PpieLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(PpieLayout.createSequentialGroup()
                .add(PpieLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(PpieLayout.createSequentialGroup()
                        .add(cLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(impAlbE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 85, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(PpieLayout.createSequentialGroup()
                        .add(cLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(impFraE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .add(PpieLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(PpieLayout.createSequentialGroup()
                        .add(11, 11, 11)
                        .add(cLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(kgAlbE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(PpieLayout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(3, 3, 3)
                        .add(KgFraE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(PpieLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(PpieLayout.createSequentialGroup()
                        .add(cLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 88, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(impPenE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 85, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(PpieLayout.createSequentialGroup()
                        .add(cLabel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(numAlbE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 42, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        PpieLayout.setVerticalGroup(
            PpieLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(PpieLayout.createSequentialGroup()
                .add(PpieLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(PpieLayout.createSequentialGroup()
                        .add(PpieLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(cLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(PpieLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                .add(impAlbE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(cLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(kgAlbE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(PpieLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(PpieLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                .add(impFraE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(cLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(KgFraE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(cLabel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(numAlbE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(cLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(PpieLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(cLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(impPenE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        Pprinc.add(Ppie, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButtonMenu Baceptar;
    private gnu.chu.controles.CTextField KgFraE;
    private gnu.chu.controles.CPanel Pentra;
    private gnu.chu.controles.CPanel Ppie;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel10;
    private gnu.chu.controles.CLabel cLabel11;
    private gnu.chu.controles.CLabel cLabel12;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.controles.CLabel cLabel9;
    private gnu.chu.camposdb.empPanel emp_codiE;
    private gnu.chu.controles.CTextField fecfinE;
    private gnu.chu.controles.CTextField feciniE;
    private gnu.chu.controles.CTextField impAlbE;
    private gnu.chu.controles.CTextField impFraE;
    private gnu.chu.controles.CTextField impPenE;
    private gnu.chu.controles.Cgrid jt;
    private gnu.chu.controles.CTextField kgAlbE;
    private gnu.chu.controles.CTextField numAlbE;
    private gnu.chu.controles.CCheckBox opIncPortes;
    private gnu.chu.controles.CCheckBox opMosDet;
    private gnu.chu.controles.CCheckBox opRecFra;
    private gnu.chu.controles.CComboBox ordenE;
    private gnu.chu.camposdb.prvPanel prvFinE;
    private gnu.chu.camposdb.prvPanel prvIniE;
    // End of variables declaration//GEN-END:variables

}
