/* 
 *<p>Titulo: MantPrAlb </p>
 * <p>Descripción: Mantenimiento Precios Albaranes de Ventas</p>
 *
 * Created on 03-dic-2009, 22:41:09
 *
 * <p>Copyright: Copyright (c) 2005-2012
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
package gnu.chu.anjelica.ventas;


import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.almacen.MvtosAlma;
import gnu.chu.anjelica.almacen.actStkPart;
import gnu.chu.anjelica.pad.MantTarifa;
import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.anjelica.pad.pdprove;
import gnu.chu.camposdb.proPanel;
import gnu.chu.controles.StatusBar;
import gnu.chu.sql.DatosTabla;
import gnu.chu.sql.conexion;
import gnu.chu.utilidades.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author cpuente
 */
public class MantPrAlb extends ventana {
//    boolean inActualCosto=false;
//    private boolean inEventoSelecion=false;
    String feulin;
    actCabAlbFra datCab;
    private String s;
    MvtosAlma mvtosAlm=new MvtosAlma();
    String feulinv,fecAct;
    boolean jtLinCambio = false;
    private boolean ARG_MODCONSULTA=false;
 
    DatosTabla dtAdd,dtAlb,dtCli;
    DatosTabla dtCos1,dtCos2;

   public MantPrAlb(EntornoUsuario eu, Principal p)
 {
   this(eu, p, null);
 }

 public MantPrAlb(EntornoUsuario eu, Principal p,Hashtable ht)
 {
   EU = eu;
   vl = p.panel1;
   jf = p;
   eje = true;

   try
   {
     if (ht != null)
     {
       if (ht.get("modConsulta") != null)
         ARG_MODCONSULTA = Boolean.valueOf(ht.get("modConsulta").toString()).
             booleanValue();
     }
     setTitulo("Mantenimiento Precios de Albaranes");
     if (jf.gestor.apuntar(this))
       jbInit();
     else
       setErrorInit(true);
   }
   catch (Exception e)
   {
     e.printStackTrace();
     setErrorInit(true);
   }
 }

    public MantPrAlb(gnu.chu.anjelica.menu p, EntornoUsuario eu, Hashtable ht) {
        EU = eu;
        vl = p.getLayeredPane();
        eje = false;

        try {
            if (ht != null) {
                if (ht.get("modConsulta") != null) {
                    ARG_MODCONSULTA = Boolean.valueOf(ht.get("modConsulta").toString()).
                            booleanValue();
                }
            }
            setTitulo("Mantenimiento Precios de Albaranes");


            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
            setErrorInit(true);
        } 
    }
/**
 *  Devuelve las lineas de albaran que cumplen unas condiciones dadas
 * @param condWhere String con "  emp_codi =  ?
          and avc_ano = ?
           and avc_nume = ?
           and avc_serie = ?"
 *
 * @param avlCanti Cantidad
 * @param proCodi  Producto
 * @param avlPrven Precio Venta
 * @param avlPrepvp Precio Venta Publico
 * @param avlComent Comentario (puede ser null, para q no lo tenga en cuenta)
 * @param avlProfer Precio Oferta (de Pedido)
 * @param dtStat
 * @return lineas de albaran, separadas por comas, para poner en una sentencia sql IN ()
 * @throws SQLException
 */
    public static String getNumLinAlb(String condWhere,double avlCanti,int proCodi,double avlPrven,
         double avlPrepvp,String avlComent,double avlProfer,DatosTabla dtStat) throws SQLException
    {

         String s = "SELECT avl_numlin FROM v_albavel WHERE " +
             condWhere +
             " and avl_canti " + (avlCanti > 0 ? " > 0" : " < 0") +
             " AND pro_codi = " + proCodi +
             " and avl_prven = " + avlPrven +
             " and avl_prepvp = " +avlPrepvp +
             " and avl_coment  " + (avlComent == null ? " IS NULL" : "='" + avlComent + "'") +
             " and avl_profer = " + avlProfer;
        String avlNumlin="";
        if (dtStat.select(s))
        {
            avlNumlin=""+dtStat.getInt("avl_numlin");
            while (dtStat.next())
            {
                avlNumlin+=","+dtStat.getInt("avl_numlin");
            }
        }
        return avlNumlin;
    }

    private void jbInit() throws Exception {
        statusBar = new StatusBar(this);
     
        iniciarFrame();

        this.setVersion("2012-03-13" + (ARG_MODCONSULTA ? "SOLO LECTURA" : ""));
        
       
        initComponents();
        this.setSize(new Dimension(730, 535));
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);

        conecta();
    //    activar(false);

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

        pvl_precioE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----.99");
        pro_ganancE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----.99");
        avl_prulveE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----.99");
        avl_feulveE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yy");
        pro_prulcoE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----.99");
        pro_prcostE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----.99");
        tar_preciE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----.99");
        avl_prtecnE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----.99");
        avl_prvenE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----.99");
        avl_prvenE.setToolTipText("Pulse F3 para poner el ult. Precio Compra");
        avl_unidE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--,---");
        avl_cantiE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--,---.99");
        pro_nombE = new gnu.chu.controles.CTextField(Types.CHAR,"X",30);
        IFselalb = new javax.swing.JInternalFrame();
        cLabel1 = new gnu.chu.controles.CLabel();
        cLabel2 = new gnu.chu.controles.CLabel();
        Baceptar = new gnu.chu.controles.CButton(Iconos.getImageIcon("check"));
        Bcancelar = new gnu.chu.controles.CButton(Iconos.getImageIcon("cancel"));
        pro_codiE = proPanel.getTextFieldCodi();
        alm_codiE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        avl_numlinE = new gnu.chu.controles.CTextField(Types.CHAR,"X",500);
        avl_comentE = new gnu.chu.controles.CTextField(Types.CHAR,"X",50);
        Pprinc = new gnu.chu.controles.CPanel();
        Pcondic = new gnu.chu.controles.CPanel();
        cLabel3 = new gnu.chu.controles.CLabel();
        emp_codiE = new gnu.chu.camposdb.empPanel();
        cLabel4 = new gnu.chu.controles.CLabel();
        cLabel5 = new gnu.chu.controles.CLabel();
        fecIniE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel7 = new gnu.chu.controles.CLabel();
        cli_codiE = new gnu.chu.camposdb.cliPanel();
        cLabel6 = new gnu.chu.controles.CLabel();
        cLabel8 = new gnu.chu.controles.CLabel();
        fecFinE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        avc_revpreE = new gnu.chu.controles.CComboBox();
        Bbuscar = new gnu.chu.controles.CButton();
        cLabel12 = new gnu.chu.controles.CLabel();
        diferPrecE = new gnu.chu.controles.CTextField(Types.DECIMAL,"9.9");
        avc_numeE = new gnu.chu.camposdb.AvcPanel();
        cLabel16 = new gnu.chu.controles.CLabel();
        rep_codiE = new gnu.chu.controles.CLinkBox();
        Bir = new gnu.chu.controles.CButton();
        Bbaja = new gnu.chu.controles.CButton();
        PTabPane1 = new gnu.chu.controles.CTabbedPane();
        PSelec = new gnu.chu.controles.CPanel();
        jtSelAlb = new gnu.chu.controles.Cgrid(6);
        Vector v=new Vector();
        v.addElement("Albaran"); // 0
        v.addElement("Fec.Alb"); // 1
        v.addElement("Cliente"); // 2
        v.addElement("Nombre Cliente"); // 3
        v.addElement("Importe"); // 4
        v.addElement("Pedido"); // 5
        jtSelAlb.setCabecera(v);
        jtSelAlb.setAnchoColumna(new int[]{130,90, 60,150,80,100});
        jtSelAlb.setFormatoColumna(4,"----,--9.99");
        jtSelAlb.setAlinearColumna(new int[]{0,1,2,0,2,0});
        PLinea = new gnu.chu.controles.CPanel();
        jtLin = new gnu.chu.controles.CGridEditable(16){
            public int cambiaLinea(int row, int col)
            {
                return jtLinCambiaLinea(row,col);
            }
            public void afterCambiaLinea()
            {
                buscaDesglose(jtLin.getSelectedRow());
            }
        };
        jtDes = new gnu.chu.controles.Cgrid(5);
        PLinea1 = new gnu.chu.controles.CPanel();
        cLabel9 = new gnu.chu.controles.CLabel();
        avc_numacE = new gnu.chu.camposdb.AvcPanel();
        cLabel10 = new gnu.chu.controles.CLabel();
        avc_fecalbE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel11 = new gnu.chu.controles.CLabel();
        cli_codacE = new gnu.chu.camposdb.cliPanel();
        cLabel13 = new gnu.chu.controles.CLabel();
        avc_revpracE = new gnu.chu.controles.CComboBox();
        opAgrpLin = new gnu.chu.controles.CCheckBox();
        cLabel14 = new gnu.chu.controles.CLabel();
        fecCostoE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel15 = new gnu.chu.controles.CLabel();
        tar_nombE = new gnu.chu.controles.CTextField();

        pro_ganancE.setEnabled(false);

        avl_prulveE.setEnabled(false);

        avl_feulveE.setEnabled(false);

        pro_prulcoE.setEnabled(false);

        pro_prcostE.setEnabled(false);

        tar_preciE.setEnabled(false);

        avl_unidE.setEnabled(false);

        avl_cantiE.setEnabled(false);

        pro_nombE.setEnabled(false);

        IFselalb.setVisible(true);

        cLabel1.setText("De Fecha Alb:");

        cLabel2.setText("A Fecha Alb:");

        Baceptar.setText("Aceptar");

        Bcancelar.setText("Cancelar");
        Bcancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BcancelarActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout IFselalbLayout = new org.jdesktop.layout.GroupLayout(IFselalb.getContentPane());
        IFselalb.getContentPane().setLayout(IFselalbLayout);
        IFselalbLayout.setHorizontalGroup(
            IFselalbLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, IFselalbLayout.createSequentialGroup()
                .add(IFselalbLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(cLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(54, 54, 54))
            .add(IFselalbLayout.createSequentialGroup()
                .addContainerGap()
                .add(Baceptar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 104, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(Bcancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 106, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        IFselalbLayout.linkSize(new java.awt.Component[] {Baceptar, Bcancelar}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        IFselalbLayout.setVerticalGroup(
            IFselalbLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(IFselalbLayout.createSequentialGroup()
                .add(cLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(IFselalbLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(Baceptar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(Bcancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        pro_codiE.setEnabled(false);

        alm_codiE.setEnabled(false);

        avl_numlinE.setEnabled(false);

        Pprinc.setLayout(new java.awt.GridBagLayout());

        Pcondic.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Pcondic.setMinimumSize(new java.awt.Dimension(634, 75));
        Pcondic.setPreferredSize(new java.awt.Dimension(634, 75));
        Pcondic.setLayout(null);

        cLabel3.setText("Empresa");
        Pcondic.add(cLabel3);
        cLabel3.setBounds(25, 2, 49, 18);

        emp_codiE.setPreferredSize(new java.awt.Dimension(39, 18));
        Pcondic.add(emp_codiE);
        emp_codiE.setBounds(80, 2, 40, 18);

        cLabel4.setText("Albaran");
        cLabel4.setMinimumSize(new java.awt.Dimension(43, 18));
        cLabel4.setPreferredSize(new java.awt.Dimension(10, 18));
        Pcondic.add(cLabel4);
        cLabel4.setBounds(149, 2, 47, 18);

        cLabel5.setText("De Fecha");
        cLabel5.setPreferredSize(new java.awt.Dimension(52, 18));
        Pcondic.add(cLabel5);
        cLabel5.setBounds(342, 2, 52, 18);

        fecIniE.setPreferredSize(new java.awt.Dimension(10, 18));
        Pcondic.add(fecIniE);
        fecIniE.setBounds(398, 2, 76, 18);

        cLabel7.setText("Cliente");
        cLabel7.setPreferredSize(new java.awt.Dimension(39, 18));
        Pcondic.add(cLabel7);
        cLabel7.setBounds(14, 22, 39, 18);
        Pcondic.add(cli_codiE);
        cli_codiE.setBounds(65, 22, 251, 18);

        cLabel6.setText("A Fecha");
        cLabel6.setPreferredSize(new java.awt.Dimension(44, 18));
        Pcondic.add(cLabel6);
        cLabel6.setBounds(486, 2, 44, 18);

        cLabel8.setText("Estado");
        Pcondic.add(cLabel8);
        cLabel8.setBounds(14, 43, 37, 18);

        fecFinE.setPreferredSize(new java.awt.Dimension(10, 18));
        Pcondic.add(fecFinE);
        fecFinE.setBounds(542, 2, 76, 18);
        Pcondic.add(avc_revpreE);
        avc_revpreE.setBounds(65, 42, 125, 18);

        Bbuscar.setText("Buscar (F4)");
        Pcondic.add(Bbuscar);
        Bbuscar.setBounds(501, 42, 117, 19);

        cLabel12.setText("Difer.");
        cLabel12.setPreferredSize(new java.awt.Dimension(31, 18));
        Pcondic.add(cLabel12);
        cLabel12.setBounds(202, 42, 31, 19);

        diferPrecE.setMinimumSize(new java.awt.Dimension(10, 18));
        diferPrecE.setPreferredSize(new java.awt.Dimension(10, 18));
        Pcondic.add(diferPrecE);
        diferPrecE.setBounds(234, 42, 38, 18);
        Pcondic.add(avc_numeE);
        avc_numeE.setBounds(208, 2, 108, 18);

        cLabel16.setText("Repres.");
        cLabel16.setPreferredSize(new java.awt.Dimension(60, 18));
        Pcondic.add(cLabel16);
        cLabel16.setBounds(334, 22, 60, 18);

        rep_codiE.setAncTexto(30);
        rep_codiE.setPreferredSize(new java.awt.Dimension(92, 18));
        Pcondic.add(rep_codiE);
        rep_codiE.setBounds(398, 22, 220, 18);
        Pcondic.add(Bir);
        Bir.setBounds(590, 60, 1, 1);
        Pcondic.add(Bbaja);
        Bbaja.setBounds(560, 60, 1, 1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        Pprinc.add(Pcondic, gridBagConstraints);

        PTabPane1.setMinimumSize(new java.awt.Dimension(700, 308));
        PTabPane1.setPreferredSize(new java.awt.Dimension(700, 308));

        PSelec.setLayout(new java.awt.BorderLayout());

        jtSelAlb.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtSelAlb.setMinimumSize(new java.awt.Dimension(730, 280));

        org.jdesktop.layout.GroupLayout jtSelAlbLayout = new org.jdesktop.layout.GroupLayout(jtSelAlb);
        jtSelAlb.setLayout(jtSelAlbLayout);
        jtSelAlbLayout.setHorizontalGroup(
            jtSelAlbLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 743, Short.MAX_VALUE)
        );
        jtSelAlbLayout.setVerticalGroup(
            jtSelAlbLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 314, Short.MAX_VALUE)
        );

        PSelec.add(jtSelAlb, java.awt.BorderLayout.CENTER);

        PTabPane1.addTab("Alb. Pend.", PSelec);

        PLinea.setLayout(new java.awt.GridBagLayout());

        jtLin.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtLin.setMinimumSize(new java.awt.Dimension(650, 359));
        jtLin.setPreferredSize(new java.awt.Dimension(650, 259));
        Vector vl= new Vector();
        vl.addElement("Prod.");
        vl.addElement("Nombre"); // 1
        vl.addElement("Und"); //2
        vl.addElement("Kilos"); // 3
        vl.addElement("Precio"); // 4
        vl.addElement("PrTec"); // 5
        vl.addElement("PrPed"); // 6
        vl.addElement("PMedio"); // 7
        vl.addElement("Gananc."); // 8
        vl.addElement("PCompra"); // 9
        vl.addElement("PrUV.");  // 10
        vl.addElement("FecUV"); // 11
        vl.addElement("Pr.Tar.");  // 12
        vl.addElement("Alm"); //13
        vl.addElement("Comen"); // 14
        vl.addElement("NL"); // 15
        jtLin.setCabecera(vl);
        jtLin.setAnchoColumna(new int[]{45,120,25,55,45,45,45,45,5,45,45,65,45,25,130,30});
        jtLin.setAlinearColumna(new int[]{2,0,2,2,2,2,2,2,2,2,2,1,2,2,0,2});

        Vector vc=new Vector();
        vc.addElement(pro_codiE);
        vc.addElement(pro_nombE);
        vc.addElement(avl_unidE);
        vc.addElement(avl_cantiE);
        vc.addElement(avl_prvenE);
        vc.addElement(avl_prtecnE);
        vc.addElement(pvl_precioE);
        vc.addElement(pro_prcostE);
        vc.addElement(pro_ganancE);
        vc.addElement(pro_prulcoE);
        vc.addElement(avl_prulveE);
        vc.addElement(avl_feulveE);
        vc.addElement(tar_preciE);
        vc.addElement(alm_codiE);
        vc.addElement(avl_comentE);
        vc.addElement(avl_numlinE);
        try {
            jtLin.setCampos(vc); } catch (Exception k) {msgBox("Error al inicializar grid"+k.getMessage());}
        jtLin.setFormatoCampos();

        org.jdesktop.layout.GroupLayout jtLinLayout = new org.jdesktop.layout.GroupLayout(jtLin);
        jtLin.setLayout(jtLinLayout);
        jtLinLayout.setHorizontalGroup(
            jtLinLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 743, Short.MAX_VALUE)
        );
        jtLinLayout.setVerticalGroup(
            jtLinLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 357, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 3.0;
        PLinea.add(jtLin, gridBagConstraints);

        jtDes.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtDes.setBuscarVisible(false);
        jtDes.setMinimumSize(new java.awt.Dimension(659, 100));
        Vector v2 = new Vector();
        v2.addElement("Proveed");
        v2.addElement("Fec.Cad");
        v2.addElement("Unid");
        v2.addElement("Kilos");
        v2.addElement("Prec.Compra");
        jtDes.setCabecera(v2);
        jtDes.setAlinearColumna(new int[]{0,1,2,2,2});
        jtDes.setAnchoColumna(new int[]{150,90,40,70,70});
        jtDes.setFormatoColumna(2,"---9");
        jtDes.setFormatoColumna(3,"--,--9.99");
        jtDes.setFormatoColumna(4,"--,--9.99");

        org.jdesktop.layout.GroupLayout jtDesLayout = new org.jdesktop.layout.GroupLayout(jtDes);
        jtDes.setLayout(jtDesLayout);
        jtDesLayout.setHorizontalGroup(
            jtDesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 743, Short.MAX_VALUE)
        );
        jtDesLayout.setVerticalGroup(
            jtDesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 98, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        PLinea.add(jtDes, gridBagConstraints);

        PTabPane1.addTab("Lineas Alb", PLinea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        Pprinc.add(PTabPane1, gridBagConstraints);

        PLinea1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        PLinea1.setMinimumSize(new java.awt.Dimension(750, 50));
        PLinea1.setPreferredSize(new java.awt.Dimension(750, 50));
        PLinea1.setLayout(null);

        cLabel9.setText("Albaran ");
        PLinea1.add(cLabel9);
        cLabel9.setBounds(2, 2, 45, 18);

        avc_numacE.setEnabled(false);
        PLinea1.add(avc_numacE);
        avc_numacE.setBounds(51, 2, 118, 18);

        cLabel10.setText("Fecha");
        PLinea1.add(cLabel10);
        cLabel10.setBounds(173, 2, 32, 18);

        avc_fecalbE.setEnabled(false);
        PLinea1.add(avc_fecalbE);
        avc_fecalbE.setBounds(206, 2, 73, 18);

        cLabel11.setText("Cliente");
        PLinea1.add(cLabel11);
        cLabel11.setBounds(282, 2, 39, 18);

        cli_codacE.setBotonConsultar(false);
        cli_codacE.setEnabled(false);
        PLinea1.add(cli_codacE);
        cli_codacE.setBounds(323, 2, 287, 18);

        cLabel13.setText("Estado");
        PLinea1.add(cLabel13);
        cLabel13.setBounds(2, 24, 37, 17);
        PLinea1.add(avc_revpracE);
        avc_revpracE.setBounds(43, 24, 178, 17);

        opAgrpLin.setSelected(true);
        opAgrpLin.setText("Agrupar Lineas");
        opAgrpLin.setMargin(new java.awt.Insets(0, 0, 0, 0));
        PLinea1.add(opAgrpLin);
        opAgrpLin.setBounds(612, 2, 110, 18);

        cLabel14.setText("Fecha Costo");
        PLinea1.add(cLabel14);
        cLabel14.setBounds(225, 24, 68, 17);
        PLinea1.add(fecCostoE);
        fecCostoE.setBounds(295, 24, 88, 17);

        cLabel15.setText("Tarifa");
        PLinea1.add(cLabel15);
        cLabel15.setBounds(387, 24, 31, 17);

        tar_nombE.setEnabled(false);
        PLinea1.add(tar_nombE);
        tar_nombE.setBounds(421, 24, 288, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        Pprinc.add(PLinea1, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BcancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BcancelarActionPerformed
        // TODO add your handling code here:
        IFselalb.setVisible(false);
    }//GEN-LAST:event_BcancelarActionPerformed
    @Override
    public void iniciarVentana() throws Exception {
        dtAdd= new DatosTabla(new conexion(EU));
        dtAlb=new DatosTabla(ct);
        dtCos1=new DatosTabla(ct);
        dtCos2=new DatosTabla(ct);
        dtCli=new DatosTabla(ct);
        jtLin.setCanDeleteLinea(false);
        jtLin.setCanInsertLinea(false);
        jtLin.setDefButton(Bbuscar);
        jtSelAlb.setButton(KeyEvent.VK_F2,Bir);
        jtLin.setButton(KeyEvent.VK_F2,Bbaja);
        avc_numeE.iniciar(EU);
        mvtosAlm.setEntornoUsuario(EU);
        fecIniE.setText(Formatear.sumaDias(Formatear.getDateAct(),-15));
        fecFinE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
        pdalbara.initAvcRevpreE(avc_revpreE, false);
        avc_revpreE.addItem("Difer.Pre","9");
        pdalbara.initAvcRevpreE(avc_revpracE, false);
        cli_codiE.iniciar(dtCli, this, vl, EU);
        cli_codacE.iniciar(dtCli, this, vl, EU);
        avc_numacE.iniciar(EU);
        emp_codiE.iniciar(dtCli, this, vl, EU);
        emp_codiE.setAceptaNulo(false);
        feulinv=actStkPart.getFechaUltInv(0,0,null,dtStat);
        fecAct=Formatear.getFechaAct("dd-MM-yyyy");
        
        datCab = new actCabAlbFra(dtCon1,dtAdd);
        Pcondic.setButton(KeyEvent.VK_F4, Bbuscar);
        feulin=actStkPart.getFechaUltInv(0,0,null,dtStat);
        if (feulin == null)
            feulin = "01-01-" + EU.ejercicio; // Buscamos desde el principio del año.
        rep_codiE.setFormato(Types.CHAR,"XX");
        rep_codiE.setCeroIsNull(true);
        pdconfig.llenaDiscr(dtCon1,rep_codiE,"Cz",EU.em_cod);
        activarEventos();
    }
    private int jtLinCambiaLinea(int row, int col)
    {
        if (avl_prtecnE.hasCambio() || avl_prvenE.hasCambio() || pvl_precioE.hasCambio() || avl_comentE.hasCambio())
             jtLinCambio=true;

        return -1;
    }
    private void activarEventos() {
        Bir.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
               irLineaAlb();
            }
        });
        Bbaja.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
               if (jtSelAlb.getSelectedRow()+1==jtSelAlb.getRowCount())
                   return;
                new miThread("")
                {
                    @Override
                    public void run()
                    {
                        setTrabajando(true);
                        guardaCambios();
                        int nuevaRow=jtSelAlb.getSelectedRow()+1;
                        jtSelAlb.setRowFocus(nuevaRow);
                        jtLinCambio=false;
                        cargaAlbaran(nuevaRow); 
                        setTrabajando(false);
                        jtLin.requestFocusInicioLater();
                    }
                };
            }
        });
        Bbuscar.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                Bbuscar_actionPerformed();
            }
        });
        jtSelAlb.tableView.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (getTrabajando())
                    return;
                if (e.getValueIsAdjusting() || !jtSelAlb.isEnabled()) {
                    return;
                }
//                inEventoSelecion=true;
                new miThread("")
                {
                    @Override
                    public void run()
                    {
                        setTrabajando(true);
                        guardaCambios();
                        jtLinCambio=false;
                        cargaAlbaran(jtSelAlb.getSelectedRow()); 
                        setTrabajando(false);
                        jtSelAlb.requestFocusSelectedLater();
                    }
                };
            }
        });
        jtSelAlb.addMouseListener(new MouseAdapter() {
            @Override
         public void mouseClicked(MouseEvent e) {
             if (e.getClickCount()>1)// && ! jtSelAlb.isVacio() && jtSelAlb.isEnabled())
                 irLineaAlb();
         }
        });
        avc_revpracE.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
               if (avc_revpracE.isEnabled())
                actualizarEstado();
            } 
        });
        avl_prvenE.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e) {
                if (jtLin.isEnabled() && e.getKeyCode()==KeyEvent.VK_F3)
                    avl_prvenE.setValorDec(jtLin.getValorDec(10));
            }
        } );
        opAgrpLin.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
               if (avc_revpracE.isEnabled())
               {
                guardaCambios(!opAgrpLin.isSelected());
                cargaAlbaran();
               }
            } 
        });
    }
    @Override
    public void matar()
    {
        try  {
           dtCos1=null;
           dtCos2=null;
        } catch (Exception k)
        {

        }
        guardaCambios();
        super.matar();
    }
    void irLineaAlb()
    {
      PTabPane1.setSelectedIndex(1);
      jtLin.requestFocusInicioLater();
      //cargaAlbaran();
    }
    void cargaAlbaran()
    {
        cargaAlbaran(jtSelAlb.getSelectedRow() );
    }
    void cargaAlbaran(int row)
    {
        mensajeErr("Buscando datos de Albaran",false);
        double prCosto;
        String albaran=jtSelAlb.getValString(row,0);
        int avcAno=Integer.parseInt(albaran.substring(0,4));
        String avcSerie=albaran.substring(4,5);
        int avcNume=Integer.parseInt(albaran.substring(5));
        cli_codacE.setValorInt(jtSelAlb.getValorInt(row,2));
        
        avc_numacE.setText(albaran);
        avc_fecalbE.setText(jtSelAlb.getValString(row,1));
        avc_revpracE.setEnabled(false);
        avc_revpracE.setValor(avc_revpreE.getValor());
        avc_revpracE.setEnabled(true);

        pvl_precioE.setEditable(avc_revpracE.getValorInt()==pdalbara.REVPRE_PCOSTO &&
             jtSelAlb.getValorInt(row,5)==0);
        String feulVen,prulVen,prulCom;

        HashMap<Integer,precVentas> htPrulv = new HashMap();
        HashMap<Integer,String> htCompra = new HashMap();
        jtLin.setEnabled(false);
        jtLin.removeAllDatos();
        String strSql;
        if (opAgrpLin.isSelected())
         strSql=pdalbara.getSqlLinAgr(avcAno,
                emp_codiE.getValorInt(),avcSerie,avcNume,true);
        else
            strSql=pdalbara.getSqlLinAlb(pdalbara.TABLACAB,avcAno,
                emp_codiE.getValorInt(),avcSerie,avcNume);
        try {
            if (!dtAlb.select(strSql)) {
                msgBox("ERROR INTERNO: LINEAS DE ALBARAN NO ENCONTRADO");
                mensajeErr("");
                return;
            }
            tar_nombE.setText(MantTarifa.getTarNomb(dtStat, cli_codacE.getTarifa()));
//            
            String fecCompra=avc_fecalbE.getText();
            try {
             fecCompra =fecCostoE.isNull()? Formatear.sumaDias(avc_fecalbE.getDate(),-7):
                 Formatear.sumaDias(fecCostoE.getDate(),-7);
            } catch (ParseException k){}
            int numLin=0;
            String  avlNumlin="";
            double precTar;
            precVentas prVenta;
            ArrayList<ArrayList> datos = new ArrayList();
            do
            {
                precTar=dtAlb.getDouble("tar_preci");
                if (opAgrpLin.isSelected())
                {
                    avlNumlin=getNumLinAlb(avc_numacE.getCondWhere(null, false),
                     dtAlb.getDouble("avl_canti"),
                     dtAlb.getInt("pro_codi"),dtAlb.getDouble("avl_prven"),
                     dtAlb.getDouble("avl_prepvp"),dtAlb.getString("avl_coment",false),
                     dtAlb.getDouble("avl_profer"),dtStat);
                }
                else
                    avlNumlin=dtAlb.getString("avl_numlin");
                if (avlNumlin.equals(""))
                {
                    msgBox("No encontrado indice para editar esta linea. Consulta cancelada");
                    enviaMailError("Error al buscar indice para editar linea en MantPrAlb Albaran: "+
                            avc_numeE.getStrQuery());
                    return ;
                }
                prVenta=htPrulv.get(dtAlb.getInt("pro_codi"));
                if (prVenta==null)
                {
                    s="SELECT avc_fecalb , avl_prven from v_albavel as l, v_albavec c WHERE" +
                        " pro_codi = "+dtAlb.getInt("pro_codi")+
                        " and c.emp_codi = "+emp_codiE.getValorInt()+
                        " and avc_fecalb >= current_date - 60  "+
                        " and c.avc_nume != "+avc_numacE.geValorIntNume()+
                        " and avl_prven !=0 "+
                        " and c.cli_codi = "+cli_codacE.getValorInt()+
                        " and l.emp_codi = c.emp_codi "+
                        " and l.avc_ano = c.avc_ano "+
                        " and l.avc_serie = c.avc_serie  "+
                        " and l.avc_nume = c.avc_nume "+
                        " order by avc_fecalb desc";
                    if (! dtStat.select(s))
                    {
                        feulVen="";
                        prulVen="";
                    }
                    else
                    {
                        feulVen=dtStat.getFecha("avc_fecalb","dd-MM-yy");
                        prulVen=dtStat.getString("avl_prven");
                        if (precTar==0)
                            precTar=MantTarifa.getPrecTar(dtStat,dtAlb.getInt("pro_codi"),
                                cli_codacE.getLikeCliente().getInt("tar_codi"), avc_fecalbE.getText());
                    }
                    htPrulv.put(dtAlb.getInt("pro_codi"), new precVentas(prulVen, feulVen, precTar));
                }
                else
                {
                    prulVen=prVenta.getPrecioVenta();
                    feulVen=prVenta.getFecVenta();
                    if (precTar==0)
                     precTar=prVenta.getPrecioTar();
                    
                }
                String prCompra=htCompra.get(dtAlb.getInt("pro_codi"));
                if (prCompra==null)
                { // Busco el precio de Compra
                    s = "SELECT sum(acl_prcom*acl_canti) as importe,sum(acl_canti) as cantidad "+
                         " FROM v_albacol as l, v_albacoc as c "
                         + " WHERE pro_codi = " + dtAlb.getInt("pro_codi")
                         + " and c.emp_codi = " + emp_codiE.getValorInt()
                         + " and c.acc_fecrec >= to_date('"+fecCompra+"','dd-MM-yyyy')"
                         + " and l.acc_ano = c.acc_ano "
                         + " and acl_prcom !=0 "
                         + " and l.emp_codi = c.emp_codi "
                         + " and l.acc_serie = c.acc_serie  "
                         + " and l.acc_nume = c.acc_nume ";
                    dtStat.select(s);
                    if (dtStat.getObject("importe")==null) {
                        prulCom = "";
                    } else {
                        prulCom = ""+ (dtStat.getDouble("importe",true)/
                                dtStat.getDouble("cantidad",true));
                    }
                    htCompra.put(dtAlb.getInt("pro_codi"), prulCom);
                }
                else
                {
                    prulCom=htCompra.get( dtAlb.getInt("pro_codi"));
                }
//                Double prec=htCosto.get(dtAlb.getInt("pro_codi"));
//                if (prec==null)
//                {
////                    if (! mvtosAlm.calculaMvtos(dtAlb.getInt("pro_codi"), dtAdd, dtStat, null,null))
////                        prCosto=0;
////                    else
////                        prCosto = mvtosAlm.getPrecioStock();
//                     prCosto=0;
//                    htCosto.put(dtAlb.getInt("pro_codi"), prCosto);
//                }
//                else
//                   prCosto=prec;
                numLin++;
                ArrayList v=new ArrayList();
                v.add(dtAlb.getString("pro_codi")); //0
                v.add(dtAlb.getString("avl_pronom"));// 1
                v.add(dtAlb.getString("avl_unid")); // 2
                v.add(dtAlb.getString("avl_canti")); // 3
                v.add(dtAlb.getString("avl_prven")); // 4
                v.add(dtAlb.getString("avl_prepvp")); //  // 5 Precio Tecnico
//                if (pvl_precioE.isEditable())
                    v.add(dtAlb.getString("avl_profer")); // 6
//                else
//                    v.addElement("0");// Precio Pedido
                v.add("0"); // Precio Costo. // 7
                v.add(""); // Ganancia.
                v.add(prulCom); // Precio Compra.
                v.add(prulVen); // Precio Ult. Venta.
                v.add(feulVen); // Fec. Ult.Venta
                v.add(""+precTar); // Precio Tarifa
                v.add(dtAlb.getString("alm_codi")); // almacen.
                v.add(dtAlb.getString("avl_coment"));
                v.add(avlNumlin);
                datos.add(v);
     
            } while (dtAlb.next());
             
            jtLin.setDatos(datos);
            if (! ARG_MODCONSULTA)
             jtLin.setEnabled(true);
            avl_prtecnE.resetCambio();
            avl_prvenE.resetCambio();
            pvl_precioE.resetCambio();
            avl_comentE.resetCambio();
            actualCostos();
            buscaDesglose(0);
            jtLin.requestFocusInicioLater();
//            SwingUtilities.invokeLater(new Thread()
//            {
//                @Override
//                public void run()
//                {
//                  try {
//                      while (inActualCosto)
//                      {
//                          Thread.sleep(50);
//                      } 
//                  }catch (Exception k1 ){}
//                  actualCostos();
//                  try {
//                      while (inEventoSelecion)
//                      {
//                              Thread.sleep(50);
//                      }
//                  }catch (Exception k1 ){}
//                
//                  buscaDesglose(0);
//                }
//            });
            
            mensajeErr("");
        } catch (SQLException k)
        {
            Error("Error al buscar Lineas de Albaran",k);
        }
    }
    /**
     * Actualiza los costos en las lineas del grid, 
     * anteriormente cargadas.
     */
    private void actualCostos()
    {
//        inActualCosto=true;
        double prCosto;
        HashMap<Integer,Double> htCosto = new HashMap();
        int nRow=jtLin.getRowCount();
        int nLin=jtSelAlb.getSelectedRow();
        try {
          mvtosAlm.iniciarMvtos(feulin,fecCostoE.isNull()?avc_fecalbE.getText():fecCostoE.getText(),dtCos1);
        for (int n=0;n<nRow;n++)
        {
            if (nLin!=jtSelAlb.getSelectedRow())
                break;
             Double prec=htCosto.get(jtLin.getValorInt(n,0));
             if (prec==null)
             {
                  if (! mvtosAlm.calculaMvtos(jtLin.getValorInt(n,0), dtCos1, dtCos2, null,null))
                        prCosto=0;
                  else
                        prCosto = mvtosAlm.getPrecioStock();
                  htCosto.put(jtLin.getValorInt(n,0), prCosto);
             }
             else
                   prCosto=prec;
             jtLin.setValor(""+prCosto,n,7);
        }
        } catch (Exception k)
        {
            enviaMailError("(MantPrAlb) Error al actualizar costos "+k.getMessage());
        }
//        inActualCosto=false;
    }
    private void  buscaDesglose(int row)
    {
//        inEventoSelecion=true;
//        if (jtLin.getValString(row,15).equals(""))
//            return;
        ArrayList<ArrayList> datos=new ArrayList();
        double aclPrcom;
        s="select s.prv_codi,stp_feccad,"+
              (opAgrpLin.isSelected()?" sum(avp_numuni) as avp_numuni,sum(avp_canti) as avp_canti":
                  " avp_numuni, avp_canti") +
             ", a.avp_ejelot, "+
              "avp_serlot, a.avp_numpar "+
             " from v_albavel as l,v_albvenpar as a  " +
              " LEFT OUTER JOIN  v_stkpart as s on "+
              "( a.avp_emplot = s.emp_codi  and a.avp_ejelot = s.eje_nume "+
              " and a.avp_numpar  = s.pro_nupar and a.avp_numind = s.pro_numind" +
              "  and a.avp_serlot = s.pro_serie " +
              "  and s.alm_codi =  "+jtLin.getValorDec(row,13)+")"+
              " where a.emp_codi = "+emp_codiE.getValorInt()+
              " and l.emp_codi = a.emp_codi "+
              " and l.avc_ano = a.avc_ano " +
              " and l.avc_serie = a.avc_serie " +
              " and l.avc_nume = a.avc_nume " +
              " and l.avl_numlin = a.avl_numlin " +
              ( (opAgrpLin.isSelected()?
              " and l.avl_numlin in ("+jtLin.getValString(row,15)+")":
               " and l.avl_numlin = "+jtLin.getValorDec(row,15)) )+
              avc_numacE.getCondWhere("l", true)+
              (opAgrpLin.isSelected()?
              " group by s.prv_codi,stp_feccad,a.avp_ejelot, avp_serlot, a.avp_numpar":
                  "");
        avl_comentE.setText(jtLin.getValString(14));
        try {
          jtDes.removeAllDatos();
          if (! dtCon1.select(s))
             return;
          do
          {
            s="SELECT avg(acl_prcom)  as acl_prcom FROM v_albacol where emp_codi ="+emp_codiE.getValorInt()+
                 " and acc_ano = "+dtCon1.getInt("avp_ejelot")+
                 " and acc_nume = "+dtCon1.getInt("avp_numpar")+
                 " and acc_serie = '"+dtCon1.getString("avp_serlot")+"'"+
                 " and pro_codi = "+jtLin.getValorInt(row,0);
           dtStat.select(s);
       
            aclPrcom=dtStat.getDouble("acl_prcom",true) ;
            ArrayList v=new ArrayList();
            v.add(pdprove.getNombPrv(dtCon1.getInt("prv_codi",true), dtStat));
            v.add(dtCon1.getFecha("stp_feccad","dd-MM-yyyy"));
            v.add(dtCon1.getString("avp_numuni"));
            v.add(dtCon1.getString("avp_canti"));
            v.add(""+aclPrcom);
            datos.add(v);
        } while (dtCon1.next());
        jtDes.setDatos(datos);
        jtDes.requestFocus(0,0);
        } catch (SQLException k)
        {
            Error ("Error al mostrar desglose de albaranes",k);
        }
//        inEventoSelecion=false;
    }
    void Bbuscar_actionPerformed() {
        guardaCambios();
        jtLinCambio=false;
        jtSelAlb.setEnabled(false);
        jtSelAlb.removeAllDatos();
        jtLin.setEnabled(false);
        jtLin.removeAllDatos();

        try {
            if (!emp_codiE.controla()) {
                mensajeErr(emp_codiE.getMsgError());
                return;
            }
            if (fecIniE.getError())
                return;
            if (fecFinE.getError())
                return;

            if (! fecIniE.isNull() && ! fecFinE.isNull() && Formatear.comparaFechas(fecIniE.getDate(),fecFinE.getDate())>0)
            {
                mensajeErr("Fecha final no puede ser inferios a Inicial");
                fecIniE.requestFocus();
                return;
            }
            if (avc_revpreE.getValorInt()==9 && diferPrecE.getValorDec()==0)
            {
                mensajeErr("Introduzca un importe de diferencia");
                diferPrecE.requestFocus();
                return;
            }
        } catch (Exception k) {
            Error("Error al comprobar condiciones al buscar Albaranes", k);
            return;
        }
        avl_prtecnE.setEditable(avc_revpreE.getValorInt() == pdalbara.REVPRE_PCOSTO );
        avl_prvenE.setEditable(avc_revpreE.getValorInt() == pdalbara.REVPRE_PVALOR
                || avc_revpreE.getValorInt() == pdalbara.REVPRE_REVISA ); pvl_precioE.setEditable(avc_revpreE.getValorInt() == pdalbara.REVPRE_PCOSTO );
        new miThread("")
        {

            @Override
            public void run() {
                setTrabajando(true);
                busca1();
                setTrabajando(false);
            }
        };
    }

    void busca1()
    {
        int pvcNume;
        mensaje("Espere, por favor, buscando Albaranes");
        jtLin.setColNueva(avc_revpreE.getValorInt()==pdalbara.REVPRE_PCOSTO?5:4);
        try {
          if (avc_revpreE.getValorInt()==9)
          {
              s = "SELECT v.*,cli_nomb FROM v_albavec as v, clientes as c "
                   + " WHERE v.emp_codi =" + emp_codiE.getValorInt()
                   + avc_numeE.getCondWhere("v", true)
                   + (fecIniE.isNull() ? "" : " and v.avc_fecalb >= to_date('" + fecIniE.getText() + "','dd-MM-yyyy')")
                   + (fecFinE.isNull() ? "" : " and v.avc_fecalb <= to_date('" + fecFinE.getText() + "','dd-MM-yyyy')")
                   + (cli_codiE.isNull() ? "" : " and v.cli_codi = " + cli_codiE.getValorInt())
                   + (rep_codiE.isNull()?"": " and c.rep_codi ='"+rep_codiE.getText()+"'")
                   + " and avc_revpre = " + pdalbara.REVPRE_REVISA
                   + (EU.isRootAV() ? "" : " AND v.div_codi > 0 ")
                   + " and (select count(*) from v_albavel as l where l.emp_codi = v.emp_codi  and l.avc_ano = v.avc_ano "
                   + " and l.avc_serie = v.avc_serie   and l.avc_nume = v.avc_nume "
                   + " and abs(avl_prven - avl_prepvp) > 0.0  ) > " + diferPrecE.getValorDec()
                   + " and v.cli_codi = c.cli_codi "
                   + " ORDER BY v.cli_codi,v.avc_fecalb";
          }
          else
          {
              s = "SELECT v.*,cli_nomb FROM  clientes as c, v_albavec as v "
                   + " WHERE v.emp_codi =" + emp_codiE.getValorInt()
                   + avc_numeE.getCondWhere("v", true)
                   + (fecIniE.isNull() ? "" :
                       " and v.avc_fecalb >= to_date('" + fecIniE.getText() + "','dd-MM-yyyy')")
                   + (fecFinE.isNull() ? "" :
                       " and v.avc_fecalb <= to_date('" + fecFinE.getText() + "','dd-MM-yyyy')")
                   + (cli_codiE.isNull() ? "" :
                       " and v.cli_codi = " + cli_codiE.getValorInt())
                   + (rep_codiE.isNull()?"": " and c.rep_codi ='"+rep_codiE.getText()+"'")
                   + " and avc_revpre = " + avc_revpreE.getValor()
                   + " and v.cli_codi = c.cli_codi "
                   + " and v.fvc_ano + v.fvc_nume = 0 " // No mostrar albaranes facturados
                   + (EU.isRootAV() ? "" : " AND v.div_codi > 0 ")
                   + " ORDER BY v.cli_codi,v.avc_fecalb";
          }
        if (!dtCon1.select(s))
        {
            msgBox("No encontrados albaranes para estas condiciones");
            return;
        }
        ArrayList<ArrayList> datos=new ArrayList();
        do
        {
             s = "SELECT pvc_nume FROM pedvenc WHERE avc_ano =" +dtCon1.getInt("avc_ano") +
              " and emp_codi = " + +dtCon1.getInt("emp_codi")  +
              " and avc_serie = '" + dtCon1.getString("avc_serie") + "'" +
              " and avc_nume = " + dtCon1.getInt("avc_nume");
            if (!dtStat.select(s))
                pvcNume=0;
            else
                pvcNume=dtStat.getInt("pvc_nume",true);
            ArrayList v=new ArrayList();
            v.add(Formatear.format(dtCon1.getInt("avc_ano"),"9999")+
                 dtCon1.getString("avc_serie")+Formatear.format(dtCon1.getInt("avc_nume"),"999999"));
            v.add(dtCon1.getFecha("avc_fecalb", "dd-MM-yyyy"));
            v.add(dtCon1.getString("cli_codi"));
            v.add(dtCon1.getString("cli_nomb"));
            v.add(dtCon1.getString("avc_impalb"));
            v.add(pvcNume);
            datos.add(v);
        } while (dtCon1.next());        
        jtSelAlb.setDatos(datos);
        jtSelAlb.requestFocusInicio();
        jtSelAlb.setEnabled(true);
        mensajeErr("Busqueda Albaranes Realizada...");
        mensaje("");
       
        cargaAlbaran();
        jtSelAlb.requestFocusInicio();
        jtSelAlb.requestFocusSelectedLater();
        } catch (SQLException k)
        {
            Error("Error al buscar albaranes",k);
        }
    }
    private void actualizarEstado()
    {
         try {
            avc_numacE.selCabAlb(dtAdd,emp_codiE.getValorInt(),true,true);

            dtAdd.edit(dtAdd.getCondWhere());
            dtAdd.setDato("avc_revpre", avc_revpracE.getValorInt());
            dtAdd.update();
            dtAdd.commit();
            mensajeErr("Estado de Albaran Actualizado...");
         }   catch (SQLException k)
       {
           Error ("Error al guardar Estado de albaran",k);
       }
    }
    private void guardaCambios()
    {
        guardaCambios(opAgrpLin.isSelected());
    }
    /**
     * Guarda los cambios realizados en el grid
     */
    private void guardaCambios(boolean agrupLin)
    {
        if (jtLin.isVacio())
            return;
        //jtLin.procesaAllFoco();
        // jtLin.salirFoco();

       if ( avl_prtecnE.hasCambio() || avl_prvenE.hasCambio() || pvl_precioE.hasCambio()
           || avl_comentE.hasCambio())
             jtLinCambio=true;
       avl_prtecnE.resetCambio();
       avl_prvenE.resetCambio();
       pvl_precioE.resetCambio();
       avl_comentE.resetCambio();
       if (! jtLinCambio)
            return;
       jtLinCambio=false;
       jtLin.salirGrid();
      
       int nRows;
      
       try {
        
         s="select * from v_albavec where  emp_codi = " + emp_codiE.getValorInt() +
            avc_numacE.getCondWhere(null, true);
         if (!dtStat.select(s))
         {
             msgBox("ERROR INTERNO: NO encontrada cabecera de Albaran");
             return;
         }
         double avcDto=dtStat.getDouble("avc_dtocom")+ dtStat.getDouble("avc_dtopp");
         int nLinPrecio=0,nLinPreTec=0;
         for (int row=0;row<jtLin.getRowCount();row++)
         {
           if (jtLin.getValString(row,15).trim().equals(""))
           {
               enviaMailError("Error de Aplicacion: (MantPrAlb). Lineas de albaran estan en blanco. "+
                       " Albaran: "+avc_numacE.getCondWhere(null, true));
               msgBox("Error de Aplicacion. La linea: "+row+ " No se guardara. Intentelo de nuevo o avise a Informatica");
               continue;
           }
            String condWhere = " WHERE emp_codi = " + emp_codiE.getValorInt() +
                avc_numacE.getCondWhere(null, true)+
             ( (agrupLin?" and avl_numlin in ("+jtLin.getValString(row,15)+")":
               " and avl_numlin = "+jtLin.getValorDec(row,15)) );
           
            s = "select avl_prven,avl_prepvp from  v_albavel " + condWhere;
            double avlPrven=Formatear.Redondea(jtLin.getValorDec(row,4),pdalbara.numDec);
            double avlPrbase = avlPrven - (avlPrven * (avcDto/100)) ;
            avlPrbase=Formatear.Redondea(avlPrbase,pdalbara.numDec);

             if (jtLin.getValorDec(row,5)!=0)
                   nLinPreTec++;
             if ( avlPrven!=0)
                   nLinPrecio++;

            s = "UPDATE  V_albavel set avl_prven = " +avlPrven +
             ", avl_prbase = "+avlPrbase+
             (pvl_precioE.isEditable()? ", avl_profer = "+jtLin.getValorDec(row, 6):"")+
             ", avl_prepvp = "+jtLin.getValorDec(row,5)+
             ", avl_coment = '"+jtLin.getValString(row,14)+"'"+
             condWhere;
//      debug("actPrecioAlb - s: "+s);
               nRows=dtAdd.executeUpdate(dtAdd.getStrSelect(s));
           if (nRows==0)
           {
            String msg="MantPrAlb: (guardaCambios)\n" +s+"\n "+" No Columnas Act.: "+nRows;
            msgBox(msg);
            aviso(msg);
           }

         }
         datCab.actDatosAlb(emp_codiE.getValorInt(),avc_numacE.geValorIntAno(),avc_numacE.getTextSerie(),
                  avc_numacE.geValorIntNume(),
                            cli_codacE.getLikeCliente().getInt("cli_exeiva") == 0 && emp_codiE.getValorInt() < 90,
                            avcDto,
                            cli_codacE.getLikeCliente().getInt("cli_recequ"),avc_fecalbE.getDate());
        avc_numacE.selCabAlb(dtAdd,emp_codiE.getValorInt(),true,true);

        dtAdd.edit(dtAdd.getCondWhere());
        dtAdd.setDato("avc_revpre", pdalbara.REVPRE_PCOSTO);
        if (nLinPrecio==jtLin.getRowCount())
            dtAdd.setDato("avc_revpre", pdalbara.REVPRE_REVISA);
        else if (nLinPreTec==jtLin.getRowCount())
            dtAdd.setDato("avc_revpre", pdalbara.REVPRE_PVALOR);
            
            
        dtAdd.setDato("avc_impalb", datCab.getValDouble("avc_impalb"));
        dtAdd.setDato("avc_basimp", datCab.getValDouble("avc_basimp"));
        dtAdd.setDato("avc_kilos", datCab.getValDouble("kilos"));
        dtAdd.update();
        dtAdd.commit();
        mensajeErr("Albaran Actualizado...");
       } catch (Exception k)
       {
           Error ("Error al guardar linea de albaran",k);
       }

      //  msgBox("Guardando Cambios de Albaran "+avc_numacE.getText());
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CButton Bbaja;
    private gnu.chu.controles.CButton Bbuscar;
    private gnu.chu.controles.CButton Bcancelar;
    private gnu.chu.controles.CButton Bir;
    private javax.swing.JInternalFrame IFselalb;
    private gnu.chu.controles.CPanel PLinea;
    private gnu.chu.controles.CPanel PLinea1;
    private gnu.chu.controles.CPanel PSelec;
    private gnu.chu.controles.CTabbedPane PTabPane1;
    private gnu.chu.controles.CPanel Pcondic;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CTextField alm_codiE;
    private gnu.chu.controles.CTextField avc_fecalbE;
    private gnu.chu.camposdb.AvcPanel avc_numacE;
    private gnu.chu.camposdb.AvcPanel avc_numeE;
    private gnu.chu.controles.CComboBox avc_revpracE;
    private gnu.chu.controles.CComboBox avc_revpreE;
    private gnu.chu.controles.CTextField avl_cantiE;
    private gnu.chu.controles.CTextField avl_comentE;
    private gnu.chu.controles.CTextField avl_feulveE;
    private gnu.chu.controles.CTextField avl_numlinE;
    private gnu.chu.controles.CTextField avl_prtecnE;
    private gnu.chu.controles.CTextField avl_prulveE;
    private gnu.chu.controles.CTextField avl_prvenE;
    private gnu.chu.controles.CTextField avl_unidE;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel10;
    private gnu.chu.controles.CLabel cLabel11;
    private gnu.chu.controles.CLabel cLabel12;
    private gnu.chu.controles.CLabel cLabel13;
    private gnu.chu.controles.CLabel cLabel14;
    private gnu.chu.controles.CLabel cLabel15;
    private gnu.chu.controles.CLabel cLabel16;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.controles.CLabel cLabel9;
    private gnu.chu.camposdb.cliPanel cli_codacE;
    private gnu.chu.camposdb.cliPanel cli_codiE;
    private gnu.chu.controles.CTextField diferPrecE;
    private gnu.chu.camposdb.empPanel emp_codiE;
    private gnu.chu.controles.CTextField fecCostoE;
    private gnu.chu.controles.CTextField fecFinE;
    private gnu.chu.controles.CTextField fecIniE;
    private gnu.chu.controles.Cgrid jtDes;
    private gnu.chu.controles.CGridEditable jtLin;
    private gnu.chu.controles.Cgrid jtSelAlb;
    private gnu.chu.controles.CCheckBox opAgrpLin;
    private gnu.chu.controles.CTextField pro_codiE;
    private gnu.chu.controles.CTextField pro_ganancE;
    private gnu.chu.controles.CTextField pro_nombE;
    private gnu.chu.controles.CTextField pro_prcostE;
    private gnu.chu.controles.CTextField pro_prulcoE;
    private gnu.chu.controles.CTextField pvl_precioE;
    private gnu.chu.controles.CLinkBox rep_codiE;
    private gnu.chu.controles.CTextField tar_nombE;
    private gnu.chu.controles.CTextField tar_preciE;
    // End of variables declaration//GEN-END:variables

}
class precVentas
{
    String precVenta;
    String fecVenta;
    double precTarifa;

    public precVentas(String prVentas, String fVenta, double prTarifa)
    {
        precVenta=prVentas;
        fecVenta=fVenta;
        precTarifa=prTarifa;
    }
    public String getPrecioVenta()
    {
        return precVenta;
    }
    public String getFecVenta()
    {
        return fecVenta;
    }
    public double getPrecioTar()
    {
        return precTarifa;

    }
}
