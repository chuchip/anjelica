package gnu.chu.anjelica.compras;

/**
 *
 * <p>Titulo: MantAlbPrv</p>
 *
 * <p>Descripción: Mantenimiento Albaranes de Proveedores </p>
 * <p>Copyright: Copyright (c) 2005-2012
 *   Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
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
 * <p>En beta. No testeado en profundidad</p>
 * <p>Empresa: miSL</p>
 * @author chuchi P
 * @version 1.0
*
*/
import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.menu;
import gnu.chu.controles.StatusBar;
import gnu.chu.interfaces.PAD;
import gnu.chu.utilidades.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author jpuente
 */
public class MantAlbPrv extends ventanaPad  implements PAD  {
    boolean PARAM_MODCONSULTA=false;
    /** Creates new form MantAlXbPrv */
    public MantAlbPrv() {
        initComponents();
    }
 public MantAlbPrv(EntornoUsuario eu, Principal p)
  {
    this(eu,p,null);
  }
  public MantAlbPrv(EntornoUsuario eu, Principal p,Hashtable ht)
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
          PARAM_MODCONSULTA = Boolean.valueOf(ht.get("modConsulta").toString()).
              booleanValue();
      }
      setTitulo("Mantenimiento Albaranes Proveedores");

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

  public MantAlbPrv(menu p, EntornoUsuario eu,Hashtable ht)
  {
    EU = eu;
    vl = p.getLayeredPane();
    eje = false;

    try
    {
      if (ht != null)
      {
        if (ht.get("modConsulta") != null)
         PARAM_MODCONSULTA = Boolean.valueOf(ht.get("modConsulta").toString()).
              booleanValue();
      }
      setTitulo("Mantenimiento Albaranes Proveedores");
      jbInit();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      setErrorInit(true);
    }
  }

  private void jbInit() throws Exception {
        statusBar = new StatusBar(this);
        nav = new navegador(this, dtCons, false, PARAM_MODCONSULTA ? navegador.CURYCON : navegador.NORMAL);
        iniciarFrame();
//        this.setResizable(false);

        this.setVersion("2012-03-14" + (PARAM_MODCONSULTA ? "SOLO LECTURA" : ""));
        strSql = getStrSql();

        this.getContentPane().add(nav, BorderLayout.NORTH);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        this.setPad(this);
        dtCons.setLanzaDBCambio(false);
        initComponents();
        iniciarBotones(Baceptar, Bcancelar);

        conecta();
        navActivarAll();
        this.setSize(663,524);
        activar(false);

    }
  /**
   * Ver datos de albaran de proveedor en cursor DTCONS.
   * 
   */
    void verDatos()
    {
        if (dtCons.getNOREG())
            return;
        try {
            emp_codiE.setValorInt(dtCons.getInt("emp_codi"));
            apc_codiE.setValorInt(dtCons.getInt("apc_codi"));
            apc_albprvE.setText(dtCons.getString("apc_albprv"));
            apc_fechaE.setDate(dtCons.getDate("apc_fecha"));
            apc_transE.setValor(dtCons.getString("apc_trans"));
            prv_codiE.resetTexto();
            tra_codiE.resetTexto();
            if (apc_transE.getValor().equals("P"))
                prv_codiE.setValorInt(dtCons.getInt("prv_codi"));
            else
                tra_codiE.setValorInt(dtCons.getInt("prv_codi"));
            apc_comentE.setText(dtCons.getString("apc_coment"));
//            fcc_anoE.setValorInt(dtCons.getInt("fcc_ano",true));
//            fcc_numeE.setValorInt(dtCons.getInt("fcc_nume",true));
            apc_albtraE.setValorInt(dtCons.getInt("apc_albtra"));
            apc_estadE.setValor(dtCons.getString("apc_estad"));
            jtAlb.removeAllDatos();
            String s="select apl_numlin,pro_codi,pro_nomb,apl_unid,"+
                    " apl_canti,apl_precio from albprvli "+
                    " where apc_codi="+dtCons.getInt("apc_codi")+
                    " order by apl_numlin";
            if (dtCon1.select(s))
            {
                do
                {
                    ArrayList v=new ArrayList();
                    v.add(dtCon1.getInt("pro_codi"));
                    v.add(dtCon1.getString("pro_nomb"));
                    v.add(dtCon1.getInt("apl_unid"));
                    v.add(dtCon1.getDouble("apl_canti"));
                    v.add(dtCon1.getDouble("apl_precio"));
                    v.add(dtCon1.getDouble("apl_canti")*dtCon1.getDouble("apl_precio"));
                    jtAlb.addLinea(v);
                } while (dtCon1.next());
            }
            calcAcumLineas();
            jtNAlb.removeAllDatos();
            s="SELECT acc_ano,acc_serie, acc_nume,acc_fecrec FROM v_albacoc "+
                     " where emp_codi="+emp_codiE.getValorInt()+
                     " and apc_nume = "+apc_codiE.getValorInt();
             if (dtCon1.select(s))
            {
                do
                {
                    ArrayList v=new ArrayList();

                    v.add(dtCon1.getInt("acc_ano"));
                    v.add(dtCon1.getString("acc_serie"));
                    v.add(dtCon1.getInt("acc_nume"));
                    v.add(dtCon1.getDate("acc_fecrec"));
                    jtNAlb.addLinea(v);
                }  while (dtCon1.next());
            }
        } catch (SQLException k)
        {
            Error("Error al ver datos",k);
        }
    }
    /**
     * Calcular Acumulados de lineas albaran
     */
    void calcAcumLineas()
    {
        int nRow=jtAlb.getRowCount();
        double impLinea=0,kilos=0;
        int numInd=0;
        for (int n=0;n<nRow;n++)
        {
            if (jtAlb.getValorInt(n,0)==0)
                continue; // Sin producto.
            impLinea+= Formatear.Redondea(jtAlb.getValorDec(n,3)
                    *jtAlb.getValorDec(n,4),2);
            kilos+=jtAlb.getValorDec(n,3);
            numInd+=jtAlb.getValorDec(n,2);
        }
        impLinE.setValorDec(impLinea);
        numIndE.setValorDec(numInd);
        kilosE.setValorDec(kilos);
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

        pro_nombE = new gnu.chu.controles.CTextField(Types.CHAR,"X",40);
        apl_unidE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        apl_kilosE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---,--9.99");
        apl_precioE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--,--9.99");
        apl_numlinE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        apl_implinE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----,--9.99");
        apr_estrecE = new gnu.chu.controles.CComboBox();
        apr_kilosE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---,--9.99");
        apr_precioE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--,--9.99");
        acc_serieE = new gnu.chu.controles.CTextField(Types.CHAR,"X",1);
        acc_numeE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        apr_implinE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----,--9.99");
        acc_anoE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        acc_fecalbE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        apr_comentE = new gnu.chu.controles.CTextField(Types.CHAR,"X",40);
        pro_codiE = new gnu.chu.camposdb.proPanel();
        Pprinc = new gnu.chu.controles.CPanel();
        Pcabe = new gnu.chu.controles.CPanel();
        cLabel1 = new gnu.chu.controles.CLabel();
        emp_codiE = new gnu.chu.camposdb.empPanel();
        prv_codiE = new gnu.chu.camposdb.prvPanel();
        cLabel6 = new gnu.chu.controles.CLabel();
        apc_albprvE = new gnu.chu.controles.CTextField(Types.CHAR,"X",15);
        cLabel7 = new gnu.chu.controles.CLabel();
        apc_fechaE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel8 = new gnu.chu.controles.CLabel();
        apc_albtraE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        cLabel10 = new gnu.chu.controles.CLabel();
        cScrollPane1 = new gnu.chu.controles.CScrollPane();
        apc_comentE = new gnu.chu.controles.CTextArea();
        cPanel1 = new gnu.chu.controles.CPanel();
        jtNAlb = new gnu.chu.controles.CGridEditable(4){
            public int cambiaLinea(int row, int col)
            {
                return cambiaLinAlb(row);
            }
        };
        apc_transE = new gnu.chu.controles.CComboBox();
        tra_codiE = new gnu.chu.camposdb.traPanel();
        cLabel11 = new gnu.chu.controles.CLabel();
        apc_codiE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        BirGrid = new gnu.chu.controles.CButton(Iconos.getImageIcon("ajustar"));
        apc_estadE = new gnu.chu.controles.CComboBox();
        jtAlb = new gnu.chu.controles.CGridEditable(6){
            public void cambiaColumna(int col,int colNueva, int row)
            {
                try{
                    if (col==0 && pro_codiE.hasCambio())
                    {
                        String proNomb=pro_codiE.getNombArt();
                        jtAlb.setValor(proNomb,row,1);
                        pro_nombE.setText(proNomb);
                    }
                } catch (SQLException k){Error("Error al buscar nombre producto",k);
                }
            }
            public int cambiaLinea(int row, int col)
            {
                jtAlb.setValor(""+apl_kilosE.getValorDec()*apl_precioE.getValorDec(),row,5);
                return -1;
            }

            public void afterCambiaLinea()
            {
                pro_codiE.resetCambio();
                calcAcumLineas();
            }

        };
        Ppie = new gnu.chu.controles.CPanel();
        Baceptar = new gnu.chu.controles.CButton("Aceptar(F4)",Iconos.getImageIcon("check"));
        Bcancelar = new gnu.chu.controles.CButton("Cancelar",Iconos.getImageIcon("cancel"));
        cLabel2 = new gnu.chu.controles.CLabel();
        impLinE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----,--9.99");
        cLabel9 = new gnu.chu.controles.CLabel();
        numIndE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--,--9");
        cLabel3 = new gnu.chu.controles.CLabel();
        kilosE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--,--9.99");

        apl_numlinE.setEnabled(false);

        apl_implinE.setEnabled(false);

        apr_estrecE.addItem("Pendiente", "P");
        apr_estrecE.addItem("Aceptado", "A");
        apr_estrecE.addItem("Rechazado", "R");
        apr_estrecE.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        apr_implinE.setEnabled(false);

        acc_fecalbE.setEnabled(false);

        Pprinc.setLayout(new java.awt.GridBagLayout());

        Pcabe.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Pcabe.setPreferredSize(new java.awt.Dimension(1, 169));
        Pcabe.setLayout(null);

        cLabel1.setText("Empresa");
        Pcabe.add(cLabel1);
        cLabel1.setBounds(12, 2, 51, 15);
        Pcabe.add(emp_codiE);
        emp_codiE.setBounds(67, 2, 40, 15);
        Pcabe.add(prv_codiE);
        prv_codiE.setBounds(210, 22, 390, 17);

        cLabel6.setText("Alb. Transportista ");
        Pcabe.add(cLabel6);
        cLabel6.setBounds(10, 150, 102, 15);
        Pcabe.add(apc_albprvE);
        apc_albprvE.setBounds(326, 2, 130, 17);

        cLabel7.setText("Fec. Albaran");
        Pcabe.add(cLabel7);
        cLabel7.setBounds(460, 3, 67, 15);
        Pcabe.add(apc_fechaE);
        apc_fechaE.setBounds(531, 2, 71, 17);

        cLabel8.setText("Nº Registro");
        Pcabe.add(cLabel8);
        cLabel8.setBounds(111, 3, 61, 15);
        Pcabe.add(apc_albtraE);
        apc_albtraE.setBounds(120, 150, 57, 17);

        cLabel10.setText("Comentario");
        Pcabe.add(cLabel10);
        cLabel10.setBounds(10, 50, 80, 15);

        apc_comentE.setColumns(20);
        apc_comentE.setRows(5);
        cScrollPane1.setViewportView(apc_comentE);

        Pcabe.add(cScrollPane1);
        cScrollPane1.setBounds(6, 68, 260, 70);

        cPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cPanel1.setLayout(new java.awt.BorderLayout());

        try {
            ArrayList v=new ArrayList();
            v.add("Ejerc."); //0
            v.add("Serie"); // 1
            v.add("Numero"); //2
            v.add("Fecha Alb"); // 3
            jtNAlb.setCabecera(v);
            jtNAlb.setAlinearColumna(new int[]{2,1,2,1});
            jtNAlb.setAnchoColumna(new int[]{60,40,80,80});
            jtNAlb.setAjustarGrid(true);
            acc_anoE.setValorInt(EU.ejercicio);
            acc_serieE.setText("A");
            ArrayList vc=new ArrayList();
            vc.add(acc_anoE);
            vc.add(acc_serieE);
            vc.add(acc_numeE);
            vc.add(acc_fecalbE);
            jtNAlb.setCampos(vc);
            jtNAlb.setFormatoCampos();

        } catch (Exception k)
        {
        }

        javax.swing.GroupLayout jtNAlbLayout = new javax.swing.GroupLayout(jtNAlb);
        jtNAlb.setLayout(jtNAlbLayout);
        jtNAlbLayout.setHorizontalGroup(
            jtNAlbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 328, Short.MAX_VALUE)
        );
        jtNAlbLayout.setVerticalGroup(
            jtNAlbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 98, Short.MAX_VALUE)
        );

        cPanel1.add(jtNAlb, java.awt.BorderLayout.CENTER);

        Pcabe.add(cPanel1);
        cPanel1.setBounds(272, 68, 330, 100);

        apc_transE.addItem("Proveedor", "P");
        apc_transE.addItem("Transportista", "T");
        Pcabe.add(apc_transE);
        apc_transE.setBounds(100, 30, 100, 20);
        Pcabe.add(tra_codiE);
        tra_codiE.setBounds(210, 45, 390, 17);

        cLabel11.setText("Alb. Proveedor ");
        Pcabe.add(cLabel11);
        cLabel11.setBounds(237, 3, 85, 15);
        Pcabe.add(apc_codiE);
        apc_codiE.setBounds(176, 2, 57, 17);
        Pcabe.add(BirGrid);
        BirGrid.setBounds(210, 150, 60, 16);

        apc_estadE.addItem("Cerrado", "C");
        apc_estadE.addItem("Abierto", "A");
        Pcabe.add(apc_estadE);
        apc_estadE.setBounds(10, 30, 80, 20);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        Pprinc.add(Pcabe, gridBagConstraints);

        {
            pro_codiE.setProNomb(null);
            ArrayList v=new ArrayList();
            v.add("Producto"); // 0
            v.add("Nombre"); // 1
            v.add("Unid"); // 2
            v.add("Kilos"); // 3
            v.add("Precio"); // 4
            v.add("Importe");// 5
            jtAlb.setCabecera(v);
            jtAlb.setAnchoColumna(new int[]{80,180,50,70,60,75});
            jtAlb.setAjustarGrid(true);
            jtAlb.setAlinearColumna(new int[]{2,0,2,2,2,2});
            ArrayList vc=new ArrayList();
            vc.add(pro_codiE.getTextField()); // 0
            vc.add(pro_nombE); // 1
            vc.add(apl_unidE); // 2
            vc.add(apl_kilosE); // 3
            vc.add(apl_precioE); // 4
            vc.add(apl_implinE); // 5
            try {
                jtAlb.setCampos(vc);
                jtAlb.setFormatoCampos();
            } catch (Exception k) {}
        }
        jtAlb.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtAlb.setPreferredSize(new java.awt.Dimension(1, 139));

        javax.swing.GroupLayout jtAlbLayout = new javax.swing.GroupLayout(jtAlb);
        jtAlb.setLayout(jtAlbLayout);
        jtAlbLayout.setHorizontalGroup(
            jtAlbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 624, Short.MAX_VALUE)
        );
        jtAlbLayout.setVerticalGroup(
            jtAlbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 281, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 2.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 0);
        Pprinc.add(jtAlb, gridBagConstraints);

        Ppie.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        Ppie.setPreferredSize(new java.awt.Dimension(1, 39));
        Ppie.setLayout(null);
        Ppie.add(Baceptar);
        Baceptar.setBounds(390, 2, 95, 28);
        Ppie.add(Bcancelar);
        Bcancelar.setBounds(490, 2, 95, 28);

        cLabel2.setText("Importe Lineas ");
        Ppie.add(cLabel2);
        cLabel2.setBounds(10, 8, 90, 16);

        impLinE.setEnabled(false);
        Ppie.add(impLinE);
        impLinE.setBounds(100, 8, 70, 16);

        cLabel9.setText("Unidades");
        Ppie.add(cLabel9);
        cLabel9.setBounds(272, 8, 58, 16);

        numIndE.setEnabled(false);
        Ppie.add(numIndE);
        numIndE.setBounds(330, 8, 50, 16);

        cLabel3.setText("Kilos");
        Ppie.add(cLabel3);
        cLabel3.setBounds(178, 8, 30, 16);

        kilosE.setEnabled(false);
        Ppie.add(kilosE);
        kilosE.setBounds(210, 8, 60, 16);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 2, 0);
        Pprinc.add(Ppie, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CButton Bcancelar;
    private gnu.chu.controles.CButton BirGrid;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Ppie;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CTextField acc_anoE;
    private gnu.chu.controles.CTextField acc_fecalbE;
    private gnu.chu.controles.CTextField acc_numeE;
    private gnu.chu.controles.CTextField acc_serieE;
    private gnu.chu.controles.CTextField apc_albprvE;
    private gnu.chu.controles.CTextField apc_albtraE;
    private gnu.chu.controles.CTextField apc_codiE;
    private gnu.chu.controles.CTextArea apc_comentE;
    private gnu.chu.controles.CComboBox apc_estadE;
    private gnu.chu.controles.CTextField apc_fechaE;
    private gnu.chu.controles.CComboBox apc_transE;
    private gnu.chu.controles.CTextField apl_implinE;
    private gnu.chu.controles.CTextField apl_kilosE;
    private gnu.chu.controles.CTextField apl_numlinE;
    private gnu.chu.controles.CTextField apl_precioE;
    private gnu.chu.controles.CTextField apl_unidE;
    private gnu.chu.controles.CTextField apr_comentE;
    private gnu.chu.controles.CComboBox apr_estrecE;
    private gnu.chu.controles.CTextField apr_implinE;
    private gnu.chu.controles.CTextField apr_kilosE;
    private gnu.chu.controles.CTextField apr_precioE;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel10;
    private gnu.chu.controles.CLabel cLabel11;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.controles.CLabel cLabel9;
    private gnu.chu.controles.CPanel cPanel1;
    private gnu.chu.controles.CScrollPane cScrollPane1;
    private gnu.chu.camposdb.empPanel emp_codiE;
    private gnu.chu.controles.CTextField impLinE;
    private gnu.chu.controles.CGridEditable jtAlb;
    private gnu.chu.controles.CGridEditable jtNAlb;
    private gnu.chu.controles.CTextField kilosE;
    private gnu.chu.controles.CTextField numIndE;
    private gnu.chu.camposdb.proPanel pro_codiE;
    private gnu.chu.controles.CTextField pro_nombE;
    private gnu.chu.camposdb.prvPanel prv_codiE;
    private gnu.chu.camposdb.traPanel tra_codiE;
    // End of variables declaration//GEN-END:variables

    @Override
    public void iniciarVentana() throws Exception
    {
      prv_codiE.iniciar(dtStat, this, vl, EU);
      tra_codiE.iniciar(dtStat, this, vl, EU);
      emp_codiE.iniciar(dtStat, this, vl, EU);
      pro_codiE.iniciar(dtStat, this, vl, EU);

      prv_codiE.setColumnaAlias("prv_codi");
      tra_codiE.setColumnaAlias("prv_codi");
      apc_codiE.setColumnaAlias("apc_codi");
      apc_albprvE.setColumnaAlias("apc_albprv");
      apc_fechaE.setColumnaAlias("apc_fecha");
      apc_comentE.setColumnaAlias("apc_coment");
//      fcc_anoE.setColumnaAlias("fcc_ano");
//      fcc_numeE.setColumnaAlias("fcc_nume");
      emp_codiE.setColumnaAlias("emp_codi");
      apc_transE.setColumnaAlias("apc_trans");
      apc_estadE.setColumnaAlias("apc_estad");

      verDatos();
      activarEventos();
      Pcabe.setButton(KeyEvent.VK_F2, BirGrid);
      Pcabe.setDefButton( Baceptar);
      jtAlb.setDefButton( Baceptar);
      jtNAlb.setDefButton( Baceptar);
    }
    public void activarEventos()
    {
        apc_transE.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               if (!apc_transE.isEnabled())
                   return;
               actTransp();
            }
        });
        BirGrid.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                 jtAlb.requestFocusInicio();
                 pro_codiE.resetCambio();
            }
            public void focusLost(FocusEvent e) {

            }
        });
    }
    void actTransp()
    {
        tra_codiE.setEnabled(apc_transE.getValor().equals("T"));
        prv_codiE.setEnabled(apc_transE.getValor().equals("P"));
        apc_albtraE.setEnabled(apc_transE.getValor().equals("P"));
    }
    int cambiaLinAlb(int row)
    {
        try {
            if (acc_numeE.getValorInt()==0)
                return -1;
            String s="SELECT * FROM v_albacoc where emp_codi="+emp_codiE.getValorInt()+
                    " and acc_ano ="+acc_anoE.getValorInt()+
                    " and acc_nume="+acc_numeE.getValorInt()+
                    " and acc_serie='"+acc_serieE.getText()+"'";
            if (!dtStat.select(s))
            {
                mensajeErr("Albaran NO encontrado");
                return 2;
            }
            if (dtStat.getInt("apc_nume",true)==0 ||
                    (dtStat.getInt("apc_nume",true)==apc_codiE.getValorInt() && apc_codiE.getValorInt()!=0))
            {
                jtNAlb.setValor(dtStat.getDate("acc_fecrec"),row,3);
            }
            else
            {
                jtNAlb.setValor("",row,3);
                mensajeErr("Albaran ya tiene asignado otro albaran de proveedor");
                return 2;
            }
        } catch (SQLException k){
            Error("Error al comprobar validez de albaran",k);
        }
        return -1;
    }
     public void PADPrimero() {
        verDatos();
        nav.pulsado = navegador.NINGUNO;
    }
  public void PADAnterior()
  {
    verDatos();
    nav.pulsado=navegador.NINGUNO;
  }
  public void PADSiguiente()
  {
    verDatos();
    nav.pulsado=navegador.NINGUNO;
  }
  public void PADUltimo()
  {
    verDatos();
    nav.pulsado=navegador.NINGUNO;
  }
    @Override
  public void PADQuery()
  {
    activar(true);
//    fcc_anoE.setEnabled(true);
//    fcc_numeE.setEnabled(true);
    jtAlb.setEnabled(false);
    jtNAlb.setEnabled(false);
    BirGrid.setEnabled(false);
    mensaje("Buscar datos ...");
    Pcabe.setQuery(true);
    Pcabe.resetTexto();
    emp_codiE.setValorInt(EU.em_cod);
    apc_transE.setValor("P");
    apc_codiE.requestFocus();
  }

    @Override
  public void ej_query1() {
    Component c=Pcabe.getErrorConf();
    if (c!=null)
    {
      c.requestFocus();
      mensajeErr("Error en condiciones Busqueda");
      return;
    }
    ArrayList v=new ArrayList();
    v.add(emp_codiE.getStrQuery());
    v.add(apc_codiE.getStrQuery());
    v.add(apc_fechaE.getStrQuery());
    if (apc_transE.getValor().equals("P"))
        v.add(prv_codiE.getStrQuery());
    else
        v.add(tra_codiE.getStrQuery());
    v.add(apc_albprvE.getStrQuery());
    v.add(apc_comentE.getStrQuery());
//    v.addElement(fcc_anoE.getStrQuery());
//    v.addElement(fcc_numeE.getStrQuery());
    v.add(apc_transE.getStrQuery());
    try
    {
      Pcabe.setQuery(false);
      String s = "SELECT * FROM albprvca ";
      s = creaWhere(s, v, true);
      s += " ORDER BY apc_codi ";
//      this.setEnabled(false);
      mensaje("Espere, por favor ... buscando datos");
//      debug(s);
      if (!dtCon1.select(s))
      {
        msgBox("No encontrados Albaranes con estos criterios");
        rgSelect();
        activaTodo();
        nav.pulsado=navegador.NINGUNO;
        verDatos();
        this.setEnabled(true);
        return;
      }
      strSql = s;
      rgSelect();
      this.setEnabled(true);
      mensaje("");
      mensajeErr("Nuevas Condiciones ... Establecidas");
    }
    catch (Exception k)
    {
      Error("Error al buscar datos", k);
    }
    activaTodo();
    verDatos();
    nav.pulsado=navegador.NINGUNO;
    }

    public void canc_query() {
        Pcabe.setQuery(false);
        activaTodo();
        verDatos();
        mensaje("");
        mensajeErr("Consulta ... Cancelada");
        nav.pulsado=navegador.NINGUNO;
    }
    @Override
  public boolean checkEdit()
  {
    return checkAddNew();
  }
  public void ej_edit1()
  {
    try {
      mensaje("Modificando Compra .. Espere, por favor");
       if (jf != null)
      {
        jf.ht.clear();
        jf.ht.put("%a",emp_codiE.getValorInt()+"/"+ apc_codiE.getValorInt());
        jf.guardaMens("CA", jf.ht);
      }
      activaTodo();
      deleteCompra(false);
      updCabAlb();
      insLinAlb(apc_codiE.getValorInt());
      insRelAlb(apc_codiE.getValorInt());
      resetBloqueo(dtAdd, "albprvca", emp_codiE.getValorInt()+
                      "|"+apc_codiE.getValorInt());
      rgSelect();
      verDatos();
      
      mensajeErr("Modificacion ... REALIZADA");
      mensaje("");
      nav.pulsado=navegador.NINGUNO;
    } catch (Exception k)
    {
      Error("Error al borrar albaran de proveedor",k);
    }
   
  }

    public void canc_edit() {
        activaTodo();
        nav.pulsado=navegador.NINGUNO;
        verDatos();
        try {
          resetBloqueo(dtAdd, "albprvca", emp_codiE.getValorInt()+
                          "|"+apc_codiE.getValorInt());
        } catch (Exception k)
        {
          msgBox("Error al Quitar el bloqueo\n"+k.getMessage());
        }
        mensajeErr("Modificacion ... Cancelada");
        mensaje("");
    }
    void updCabAlb() throws SQLException,ParseException
    {
        String s="select * from  albprvca where emp_codi ="+emp_codiE.getValorInt()+
            " and apc_codi= "+apc_codiE.getValorInt();
        dtAdd.select(s,true);
        dtAdd.edit();
        setDatoCabAlb();
        dtAdd.update();
    }
            
    void deleteCompra(boolean borraCab) throws SQLException
    {
       String s;
       if (borraCab)
       {
        s="delete from  albprvca where emp_codi ="+emp_codiE.getValorInt()+
            " and apc_codi= "+apc_codiE.getValorInt();
        dtAdd.executeUpdate(s);
       }
       s="delete from albprvli where emp_codi ="+emp_codiE.getValorInt()+
            " and apc_codi= "+apc_codiE.getValorInt();
       dtAdd.executeUpdate(s);
       s="update v_albacoc set apc_nume=0  where emp_codi ="+emp_codiE.getValorInt()+
            " and apc_nume= "+apc_codiE.getValorInt();
       dtAdd.executeUpdate(s);
    }
    @Override
    public boolean checkAddNew()
    {
        String s;
        try  {
        if (! emp_codiE.controla())
        {
            mensajeErr(emp_codiE.getMsgError());
            return false;
        }
        if (apc_albprvE.isNull())
        {
            mensajeErr("Introduzca albaran de proveedor");
            apc_albprvE.requestFocus();
            return false;
        }
        if (apc_fechaE.isNull())
        {
            mensajeErr("Introduzca fecha de  albaran");
            apc_fechaE.requestFocus();
            return false;
        }
        if (apc_transE.getValor().equals("P"))
        { // albaran de proveedor
            if (! prv_codiE.controla(true))
            {
               mensajeErr("Proveedor no valido");
               return false;
            }
            if (! apc_albtraE.isNull())
            { // Compruebo si existe el albaran del transportista.
                s="SELECT * FROM albprvca WHERE emp_codi="+emp_codiE.getValorInt()+
                        " and apc_codi="+apc_albtraE.getValorInt()+
                        " and apc_trans ='T'";
                if (!dtStat.select(s))
                {
                    mensajeErr("Albaran de transportista NO existe");
                    apc_albtraE.requestFocus();
                    return false;
                }
            }
        }
        else
        {
            if (! tra_codiE.controlar())
            {
               mensajeErr("Transportista no valido");
               return false;
            }
        }
        jtNAlb.procesaAllFoco();
        // Compruebo la validez de los albaranes
        int nRow=jtNAlb.getRowCount();
        int nlVal=0;
        for (int n=0;n<nRow;n++)
        {
            if (jtNAlb.getValorInt(n,2)==0)
                continue;
           s="select * from v_albacoc where emp_codi="+emp_codiE.getValorInt()+
                    " and acc_ano ="+jtNAlb.getValorInt(n,0)+
                    " and acc_nume="+jtNAlb.getValorInt(n,2)+
                    " and acc_serie='"+jtNAlb.getValString(n,1)+"'";
           if (! dtStat.select(s))
           {
               mensajeErr("Albaran de entrada NO encontrado");
               jtNAlb.requestFocusLater(n, 2);
               return false;
           }
           if (dtStat.getInt("apc_nume",true)>0 &&  dtStat.getInt("apc_nume",true)!=apc_codiE.getValorInt() )
           {
                mensajeErr("Albaran de entrada ya tiene asignado el albaran del proveedor numero: "+dtStat.getInt("apc_nume",true));
               jtNAlb.requestFocusLater(n, 2);
               return false;
           }
           nlVal++;
        }
        if (nlVal==0)
        {
            mensajeErr("Introduzca al menos un albaran de entrada");
            jtNAlb.requestFocusInicio();
            return false;
        }
        // Compruebo la validez de las lineas de producto.
        jtAlb.salirGrid();
  
        nRow=jtAlb.getRowCount();
        nlVal=0;
        for (int n=0;n<nRow;n++)
        {
            if (jtAlb.getValorInt(n,0)==0)
                continue; // Sin producto.
            if (pro_codiE.getNombArt(jtAlb.getValString(n,0))==null)
            {
                mensajeErr("Codigo de ARTICULO no valido");
                jtAlb.requestFocusLater(n, 0);
                pro_codiE.resetCambio();
                return false;
            }
            if (jtAlb.getValString(n,1).trim().equals(""))
            {
                mensajeErr("Introduzca nombre de producto");
                jtAlb.requestFocusLater(n, 1);
                pro_codiE.resetCambio();
                return false;
            }
            if (jtAlb.getValorInt(n,2)==0)
            {
                mensajeErr("Introduzca Unidades del producto");
                jtAlb.requestFocusLater(n, 2);
                pro_codiE.resetCambio();
                return false;
            }
            if (jtAlb.getValorDec(n,3)==0)
            {
                mensajeErr("Introduzca Kilos del producto");
                jtAlb.requestFocusLater(n, 3);
                pro_codiE.resetCambio();
                return false;
            }
            if (jtAlb.getValorDec(n,4)==0)
            {
                mensajeErr("Introduzca Precio del producto");
                jtAlb.requestFocusLater(n, 4);
                pro_codiE.resetCambio();
                return false;
            }
            nlVal++;
        }
         if (nlVal==0)
        {
            mensajeErr("Introduzca al menos una linea de producto");
            jtNAlb.requestFocusInicio();
            return false;
        }
        } catch (SQLException k)
        {
            Error("Error al controlar valide de campos",k);
            return false;
        }
     return true;
    }
    public void ej_addnew1() {
        try
        {
           dtAdd.addNew("albprvca",false);
           int numAlb=insCabAlb();
           
           insLinAlb(numAlb);
           insRelAlb(numAlb);
           dtAdd.commit();
        } catch (Exception k)
        {
            Error("Error al dar de alta registro",k);
            return;
        }
        mensajeErr("Alta REALIZADA");
        mensaje("");
        activaTodo();
        nav.pulsado=navegador.NINGUNO;
        try  {
            rgSelect();
        } catch (SQLException k)
        {
            Error("Error al regenerar consulta",k);
            return;
        }
        verDatos();
    }

/**
 * Inserta cabecera de albaran
 * @return Numero de albaran insertado
 * @throws SQLException
 * @throws ParseException
 */
    int insCabAlb() throws SQLException, ParseException
    {
        dtAdd.setDato("emp_codi",emp_codiE.getValorInt());
        setDatoCabAlb();
        
        dtAdd.update();
        String s="SELECT  currval(pg_get_serial_sequence('albprvca', 'apc_codi')) ";
        ResultSet rs=dtAdd.getStatement().executeQuery(s);
        rs.next();
        return rs.getInt(1);
    }
    
    void setDatoCabAlb()  throws SQLException, ParseException
    {
        dtAdd.setDato("apc_fecha",apc_fechaE.getDate());
        dtAdd.setDato("prv_codi",apc_transE.getValor().equals("P")?prv_codiE.getValorInt():tra_codiE.getValorInt());
        dtAdd.setDato("apc_albprv",apc_albprvE.getText());
        dtAdd.setDato("apc_coment",apc_comentE.getText());
        dtAdd.setDato("apc_trans",apc_transE.getValor());
        dtAdd.setDato("apc_albtra",apc_albtraE.getValorInt());
        dtAdd.setDato("apc_estad",apc_estadE.getValor());    
    }
    /**
     * Inserta Lineas de albaran
     * @param apcCodi Numero de albaran
     * @throws SQLException
     */
    void insLinAlb(int apcCodi) throws SQLException
    {
       for (int n=0;n<jtAlb.getRowCount();n++)
       {
           if (jtAlb.getValorInt(n,0)==0)
                continue; // Sin producto.
           dtAdd.addNew("albprvli");
           dtAdd.setDato("emp_codi",emp_codiE.getValorInt());
           dtAdd.setDato("apc_codi",apcCodi);
           dtAdd.setDato("apl_numlin",n+1);
           dtAdd.setDato("pro_codi",jtAlb.getValorInt(n,0));
           dtAdd.setDato("pro_nomb",jtAlb.getValString(n,1));
           dtAdd.setDato("apl_unid",jtAlb.getValorInt(n,2));
           dtAdd.setDato("apl_canti",jtAlb.getValorDec(n,3));
           dtAdd.setDato("apl_precio",jtAlb.getValorDec(n,4));
           dtAdd.update();
       }
      
    }
    /**
     * Inserta relacion con albaranes de ventas.
     * Actualiza tabla v_albacoc poniendo el campo apc_codi
     * @param apcCodi
     * @throws SQLException
     */
    void insRelAlb(int apcCodi) throws SQLException
    {
      for (int n=0;n<jtNAlb.getRowCount();n++)
      {
          if (jtNAlb.getValorInt(n,2)==0)
              continue;
          String s="UPDATE v_albacoc set apc_nume="+apcCodi+
                 " where emp_codi="+emp_codiE.getValorInt()+
                    " and acc_ano ="+jtNAlb.getValorInt(n,0)+
                    " and acc_nume="+jtNAlb.getValorInt(n,2)+
                    " and acc_serie='"+jtNAlb.getValString(n,1)+"'";
          dtAdd.executeUpdate(s);
      }
    }
    public void canc_addnew() {
       activaTodo();
       mensaje("");
       mensajeErr("Alta CANCELADA");
       nav.pulsado=navegador.NINGUNO;
       verDatos();
    }
    @Override
    public void PADEdit()
    {
        try
        {
            if (! puedeEdit())
                return;
            activar(true);
            apc_estadE.setEnabled(false);
            apc_codiE.setEnabled(false);
            emp_codiE.setEnabled(false);
            jtAlb.requestFocusInicioLater();
            jtNAlb.requestFocusInicioLater();
            apc_albprvE.requestFocusLater();
            mensaje("Editando albaran de proveedor");
        } catch (Exception k)
        {
          Error("Error al Modificar Albaran", k);
        }
    }
    
    /**
     * Comprueba si se puede entrar a editar o borrar un registro.
     * @return false si no se puede modificar
     * @throws Exception
     */
    boolean puedeEdit() throws Exception
    {
        if (apc_estadE.getValor().equals("C"))
         {
             msgBox("Albaran en estado CERRADO. Imposible modificar");
             activaTodo();
             return false;
         }
        
         if (! setBloqueo(dtAdd,"albprvca", emp_codiE.getValorInt()+
                          "|"+apc_codiE.getValorInt()))
          {
            msgBox(msgBloqueo);
            activaTodo();
            return false;
          }
         return true;
    }
    @Override
    public void PADDelete()
    {
        try
        {
            if (! puedeEdit())
                return;
            mensaje("Borrando Albaran de proveedor...");
            Ppie.setEnabled(true);
            Bcancelar.requestFocus();
             } catch (Exception k)
        {
          Error("Error al Borrar Albaran", k);
        }
    }
    public void ej_delete1() {
        try {
      mensaje("Borrando Alb. Proveedor .. Espere, por favor");
      if (jf != null)
      {
        jf.ht.clear();
        jf.ht.put("%a",emp_codiE.getValorInt()+"/"+ apc_codiE.getValorInt());
        jf.guardaMens("C9", jf.ht);
      }
      deleteCompra(true);
      resetBloqueo(dtAdd, "albprvca", emp_codiE.getValorInt()+
                      "|"+apc_codiE.getValorInt());
      activaTodo();
      strSql = getStrSql();
      rgSelect();
      mensajeErr("BORRADO .. Realizado");
      mensaje("");
      nav.pulsado = navegador.NINGUNO;
    } catch (Exception k)
    {
      Error("Error al Borrar albaran",k);
    }
 
    }

    public void canc_delete() {
        activaTodo();
        nav.pulsado=navegador.NINGUNO;
      
        try {
          resetBloqueo(dtAdd, "albprvca", emp_codiE.getValorInt()+
                          "|"+apc_codiE.getValorInt());
        } catch (Exception k)
        {
          msgBox("Error al Quitar el bloqueo\n"+k.getMessage());
        }
        mensajeErr("BORRADO ... Cancelado");
        mensaje("");
    }

    public void activar(boolean b) {
//        fcc_anoE.setEnabled(false);
//        fcc_numeE.setEnabled(false);
        Pcabe.setEnabled(b);
        jtAlb.setEnabled(b);
        jtNAlb.setEnabled(b);
        Ppie.setEnabled(b);
        BirGrid.setEnabled(b);
    }
    @Override
    public void PADAddNew1()
    {
        mensaje("Dando de alta un albaran de proveedor");
        apc_albtraE.setEnabled(false);
        Pcabe.resetTexto();
        jtAlb.removeAllDatos();
        jtNAlb.removeAllDatos();

        activar(true);
        emp_codiE.setValorInt(EU.em_cod);
        apc_codiE.setEnabled(false);
        apc_transE.setValor("P");
        actTransp();
        apc_estadE.setValor("A");
        apc_estadE.setEnabled(false);
        apc_fechaE.setDate(Formatear.getDateAct());
        apc_albprvE.requestFocus();
    }
    String getStrSql()
    {
        return "SELECT * FROM albprvca where emp_codi = "+EU.em_cod+
                " ORDER BY apc_codi";
    }
}
