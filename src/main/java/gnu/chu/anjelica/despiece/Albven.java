/**
 *
 * <p>Titulo: Albven</p>
 * <p>Descripción: InternalFrame usada para agrupar despieces.
 * Es llamada desde valdespi2</p>
 * <p>Copyright: Copyright (c) 2005-2010
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
 *
 */

package gnu.chu.anjelica.despiece;

import gnu.chu.anjelica.pad.MantArticulos;
import gnu.chu.anjelica.ventas.pdalbara;
import gnu.chu.camposdb.AvcPanel;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.ventana;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class Albven extends ventana {
    int ejeNume=0;
    private ArrayList <Integer> numDesp =new ArrayList();
    //private boolean selLinea=false;
    private int antRow;
    private boolean aceptado=false;
    private int empCodi;
    private int avcNume,avcAno;

    public int getAvcAno() {
        return avcAno;
    }

    public int getAvcNume() {
        return avcNume;
    }

    public String getAvcSerie() {
        return avcSerie;
    }
    private String avcSerie;
    public void setEmpCodi(int emCod)
    {
        empCodi=emCod;
    }
    public int getEmpCodi()
    {
        return empCodi;
    }
    public AvcPanel getAvc_numeE() {
        return avc_numeE;
    }

    public boolean isAceptado() {
        return aceptado;
    }

    public void setAceptado(boolean aceptado) {
        this.aceptado = aceptado;
    }
    public void setEjercicio(int ejeNume)
    {
        this.ejeNume=ejeNume;
    }
    public int getGrupo() {
        return grd_codiE.getValorInt();
    }

    public void setGrupo(int grdCodi) {
        grd_codiE.setValorInt(grdCodi);
    }
    public ArrayList getNumDespieces()
    {
        return numDesp;
    }
    /** Creates new form Albven */
    public Albven() {
        initComponents();
        setResizable(false);
        setMaximizable(false);
        setIconifiable(false);
        this.setSize(600,540);
    }
    public void resetTexto()
    {
        setAceptado(false);
//        System.out.println("Altura: "+
//         this.getSize().height+ " Anc: "+this.getSize().width);
        jtLin.setEnabled(false);
        pCabe.resetTexto();
        jtDesg.setEnabled(false);
        numDesp.clear();
        jtLin.removeAllDatos();
        jtDesg.removeAllDatos();
        baceptar.setEnabled(false);
    }
    public void iniciar(DatosTabla dt,DatosTabla dtStat,EntornoUsuario eu) throws SQLException
    {
      pCabe.setDefButton(bbuscar);
      EU=eu;
      this.dtCon1=dt;
      this.dtStat=dtStat;
      avc_numeE.iniciar(eu);
      cli_codiE.iniciar(dtStat,this, vl, EU);
      pro_codiE.iniciar(dtStat, this, vl, EU);
      prv_codiE.iniciar(dtStat, this, vl, EU);
      activarEventos();
    }

    void activarEventos()
    {
      baceptar.addActionListener(new ActionListener()
      {
           public void actionPerformed(ActionEvent e) {
               setAceptado(true);
               matar();
            }
      });
      bbuscar.addActionListener(new ActionListener()
      {
           public void actionPerformed(ActionEvent e) {
                buscar();
            }
      });
      bcancelar.addActionListener(new ActionListener()
      {
           public void actionPerformed(ActionEvent e) {
               setAceptado(false);
               matar();
            }
      });
      jtLin.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) // && e.getFirstIndex() == e.getLastIndex())
                    return;
                if (!jtLin.isEnabled() || jtLin.isVacio())
                    return;
                verDatosDesg();
            }
      });
      jtDesg.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) // && e.getFirstIndex() == e.getLastIndex())
                    return;
                if (!jtDesg.isEnabled() || jtDesg.isVacio())
                    return;
                try {
                    verDatosIndiv();
                } catch (SQLException k)
                {
                    Error("Error al buscar dato de individuo",k);
                }
            }
      });
      jtLin.addMouseListener(new MouseAdapter() {
            @Override
        public void mouseClicked(MouseEvent e) {
          if (jtLin.isVacio() || ! jtLin.isEnabled() || jtLin.getSelectedColumn()!=0)
            return;
          
          if (actDespSel(antRow,true,!jtLin.getValBoolean(0)))
               jtLin.setValor((!jtLin.getValBoolean(0)),0);

         }
      });
       jtDesg.addMouseListener(new MouseAdapter() {
            @Override
        public void mouseClicked(MouseEvent e) {
          if (jtDesg.isVacio() || ! jtDesg.isEnabled() || jtDesg.getSelectedColumn()!=0 || jtDesg.getValorInt(6)==0)
            return;
          jtDesg.setValor((!jtDesg.getValBoolean(0)),0);
          actDespSel(jtDesg.getSelectedRow(),false,jtDesg.getValBoolean(0));
         }
      });
    }

    private boolean  actDespSel(int row, boolean allLinea,boolean incluir)
    {
      if(allLinea)
      {
        boolean valido=false;
        for (int n=0;n<jtDesg.getRowCount();n++)
        {
            if (actDespSel(n,false,incluir))
                valido=true;
            //jtDesg.setValor(new Boolean(!jtDesg.getValBoolean(n,0)),n,0);
        }
        return valido;
      }
      int nDesp=jtDesg.getValorInt(row,6);
      if (nDesp==0)
          return false;
      jtDesg.setValor(incluir,row,0);
      
      if (incluir)
      {
          if(numDesp.indexOf(nDesp)>=0)
              return true;
          numDesp.add(nDesp);
      }
      else
      {
         boolean bor=numDesp.remove((Integer) nDesp);
         if (!bor)
         {
             msgBox("Error al borrar despiece");
             return false;
         }
      }
      return true;
    }
    
    private void buscar()
    {
        try {
            avcAno= avc_numeE.geValorIntAno();
            avcNume=avc_numeE.geValorIntNume();
            avcSerie=avc_numeE.getTextSerie();
            jtLin.setEnabled(false);
            jtLin.removeAllDatos();
            jtDesg.removeAllDatos();
            cli_codiE.resetTexto();
            avc_fecalbE.resetTexto();
            if (!avc_numeE.selCabAlb(dtCon1, empCodi, false, false)) {
                msgBox("Albaran NO encontrado");
                avc_numeE.requestFocus();
                baceptar.setEnabled(false);
                return;
            }
            avc_fecalbE.setText(dtCon1.getFecha("avc_fecalb"));
            cli_codiE.setValorInt(dtCon1.getInt("cli_codi"));
            String s=pdalbara.getSqlLinAgr( avc_numeE.geValorIntAno() , empCodi,
                    avc_numeE.getTextSerie(),
                    avc_numeE.geValorIntNume(),false);
            if (! dtCon1.select(s))
            {
                msgBox("No encontradas lineas para este albaran");
                return;
            }
            do
            {
                Vector v=new Vector();
                v.add(false);
                v.add(dtCon1.getInt("pro_codi"));
                v.add(MantArticulos.getNombProd(dtCon1.getInt("pro_codi"),dtStat));
                v.add(dtCon1.getString("avl_unid"));
                v.add(dtCon1.getString("avl_canti"));
                jtLin.addLinea(v);
            } while (dtCon1.next());
            jtLin.requestFocusInicio();
            verDatosDesg();
            jtLin.setEnabled(true);
            baceptar.setEnabled(true);
        } catch (SQLException k)
        {
            Error("Error al buscar albaran",k);
        }
    }
    void verDatosDesg()
    {
       jtDesg.setEnabled(false);
       jtDesg.removeAllDatos();
       String s=pdalbara.getStrSqlDesg(empCodi, avc_numeE.geValorIntAno() ,
               avc_numeE.getTextSerie(),avc_numeE.geValorIntNume(),-1,jtLin.getValorInt(1),
               0,(String) null,0,0,false);
  
       try  {
        if (! dtCon1.select(s))
            {
                msgBox("No encontradas lineas desglose para este albaran");
                return;
            }
            int nDes;
            do
            {
                Vector v=new Vector();
                nDes=getDespiece(dtCon1.getInt("pro_codi"),
                        dtCon1.getInt("avp_ejelot"),dtCon1.getString("avp_serlot"),
                        dtCon1.getInt("avp_numpar"),dtCon1.getInt("avp_numind"));

                v.add((numDesp.indexOf(nDes)>=0));
                v.add(dtCon1.getInt("avp_ejelot"));
                v.add(dtCon1.getString("avp_serlot"));
                v.add(dtCon1.getString("avp_numpar"));
                v.add(dtCon1.getString("avp_numind"));
                v.add(dtCon1.getString("avp_canti"));
                v.add(nDes);
                jtDesg.addLinea(v);
            } while (dtCon1.next());
            jtDesg.requestFocusInicio();
            antRow=jtLin.getSelectedRow();
            jtDesg.setEnabled(true);
            verDatosIndiv();
        } catch (Exception k)
        {
            Error("Error al buscar albaran",k);
            return;
        }
    }
    void verDatosIndiv() throws SQLException
    {
        String s="SELECT * FROM v_despori as d  "+
            " WHERE d.eje_nume = "+ejeNume+
            " and d.deo_codi = "+jtDesg.getValorInt(6);
        
        if (!dtCon1.select(s))
        {
           pro_codiE.resetTexto();
           prv_codiE.resetTexto();
           feccadE.resetTexto();
           return;
        }
        pro_codiE.setValorInt(dtCon1.getInt("pro_codi"));
        prv_codiE.setValorInt(dtCon1.getInt("prv_codi"));
        deo_kilosE.setValorDec(dtCon1.getDouble("deo_kilos"));
        feccadE.setText(dtCon1.getFecha("deo_feccad","dd-MM-yyyy"));
    }
     /**
      * Devuelve el numero de despiece donde se genero un individuo. Solo busca en el ejercicio activo.
      * @param proCodi Producto
      * @param ejeLot Ejercicio.
      * @param serLot Serie
      * @param proLote Lote
      * @param proIndi Individuo
      * @return Numero de Despiece. 0 Si no lo encuentra.
      * @throws SQLException
      */
    int getDespiece(int proCodi,int ejeLot,String serLot,int proLote,int proIndi) throws SQLException
    {
        String s="SELECT df.* FROM v_despfin as df "+
            " WHERE df.pro_codi = " + proCodi +
            (EU==null?"":" AND df.emp_codi = " + EU.em_cod )+
            " and eje_nume = "+ejeNume+
            " and df.def_ejelot = " + ejeLot +
            " and df.def_serlot = '" + serLot + "'" +
            " and df.pro_lote = " + proLote +
            " and df.pro_numind = " + proIndi;
        if (! dtStat.select(s))
            return 0;
        return dtStat.getInt("deo_codi");
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

        pCabe = new gnu.chu.controles.CPanel();
        cLabel1 = new gnu.chu.controles.CLabel();
        avc_numeE = new gnu.chu.camposdb.AvcPanel();
        cLabel2 = new gnu.chu.controles.CLabel();
        avc_fecalbE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel3 = new gnu.chu.controles.CLabel();
        cli_codiE = new gnu.chu.camposdb.cliPanel();
        cLabel4 = new gnu.chu.controles.CLabel();
        grd_codiE = new gnu.chu.controles.CTextField(Types.DECIMAL,"####9");
        jtLin = new gnu.chu.controles.Cgrid(5);
        jtDesg = new gnu.chu.controles.Cgrid(7);
        pboton = new gnu.chu.controles.CPanel();
        bbuscar = new gnu.chu.controles.CButton("Buscar (F4)",Iconos.getImageIcon("buscar"));
        baceptar = new gnu.chu.controles.CButton("Aceptar",Iconos.getImageIcon("check"));
        bcancelar = new gnu.chu.controles.CButton("Cancelar",Iconos.getImageIcon("cancel"));
        cLabel5 = new gnu.chu.controles.CLabel();
        pro_codiE = new gnu.chu.camposdb.proPanel();
        cLabel6 = new gnu.chu.controles.CLabel();
        prv_codiE = new gnu.chu.camposdb.prvPanel();
        cLabel7 = new gnu.chu.controles.CLabel();
        feccadE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel8 = new gnu.chu.controles.CLabel();
        deo_kilosE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9.99");

        getContentPane().setLayout(new java.awt.GridBagLayout());

        pCabe.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pCabe.setMaximumSize(new java.awt.Dimension(464, 55));
        pCabe.setMinimumSize(new java.awt.Dimension(464, 55));
        pCabe.setPreferredSize(new java.awt.Dimension(464, 55));
        pCabe.setLayout(null);

        cLabel1.setText("Albaran");
        cLabel1.setPreferredSize(new java.awt.Dimension(42, 18));
        pCabe.add(cLabel1);
        cLabel1.setBounds(102, 3, 48, 18);
        pCabe.add(avc_numeE);
        avc_numeE.setBounds(154, 3, 118, 18);

        cLabel2.setText("Fecha Albaran");
        cLabel2.setPreferredSize(new java.awt.Dimension(42, 18));
        pCabe.add(cLabel2);
        cLabel2.setBounds(282, 3, 86, 18);

        avc_fecalbE.setEnabled(false);
        avc_fecalbE.setMinimumSize(new java.awt.Dimension(2, 18));
        avc_fecalbE.setPreferredSize(new java.awt.Dimension(2, 18));
        pCabe.add(avc_fecalbE);
        avc_fecalbE.setBounds(370, 3, 80, 19);

        cLabel3.setText("Cliente");
        cLabel3.setPreferredSize(new java.awt.Dimension(39, 18));
        pCabe.add(cLabel3);
        cLabel3.setBounds(3, 28, 39, 18);

        cli_codiE.setEnabled(false);
        pCabe.add(cli_codiE);
        cli_codiE.setBounds(46, 28, 405, 18);

        cLabel4.setText("Grupo");
        pCabe.add(cLabel4);
        cLabel4.setBounds(3, 5, 34, 15);

        grd_codiE.setEditable(false);
        pCabe.add(grd_codiE);
        grd_codiE.setBounds(41, 4, 51, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 10);
        getContentPane().add(pCabe, gridBagConstraints);

        jtLin.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtLin.setPreferredSize(new java.awt.Dimension(460, 100));
        Vector v=new Vector();
        v.add("Inc"); //0
        v.add("Prod"); // 1
        v.add("Nombre"); // 2
        v.add("Unid"); // 3
        v.add("Kgs"); // 4
        jtLin.setCabecera(v);
        jtLin.setAnchoColumna(new int[]{40,60,200,50,80});
        jtLin.setAlinearColumna(new int[]{1,2,0,2,2});
        jtLin.setFormatoColumna(0,"BS");
        jtLin.setFormatoColumna(3,"---9");
        jtLin.setFormatoColumna(4,"--,--9.99");

        org.jdesktop.layout.GroupLayout jtLinLayout = new org.jdesktop.layout.GroupLayout(jtLin);
        jtLin.setLayout(jtLinLayout);
        jtLinLayout.setHorizontalGroup(
            jtLinLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 645, Short.MAX_VALUE)
        );
        jtLinLayout.setVerticalGroup(
            jtLinLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 129, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        getContentPane().add(jtLin, gridBagConstraints);

        jtDesg.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtDesg.setPreferredSize(new java.awt.Dimension(460, 100));
        Vector v1=new Vector();
        v1.add("Inc"); //0
        v1.add("Ejer"); // 1
        v1.add("Serie"); // 2
        v1.add("Lote"); // 3
        v1.add("Ind");  // 4
        v1.add("Kilos"); // 5
        v1.add("Desp"); // 6
        jtDesg.setCabecera(v1);
        jtDesg.setAnchoColumna(new int[]{30,50,40,70,60,80,100});
        jtDesg.setAlinearColumna(new int[]{1,2,1,2,2,2,2});
        jtDesg.setFormatoColumna(0, "BS");
        jtDesg.setFormatoColumna(5, "--,--9.99");

        org.jdesktop.layout.GroupLayout jtDesgLayout = new org.jdesktop.layout.GroupLayout(jtDesg);
        jtDesg.setLayout(jtDesgLayout);
        jtDesgLayout.setHorizontalGroup(
            jtDesgLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 645, Short.MAX_VALUE)
        );
        jtDesgLayout.setVerticalGroup(
            jtDesgLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 129, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        getContentPane().add(jtDesg, gridBagConstraints);

        pboton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        pboton.setMaximumSize(new java.awt.Dimension(555, 80));
        pboton.setMinimumSize(new java.awt.Dimension(555, 80));
        pboton.setPreferredSize(new java.awt.Dimension(555, 80));
        pboton.setLayout(null);
        pboton.add(bbuscar);
        bbuscar.setBounds(13, 49, 100, 30);

        baceptar.setEnabled(false);
        pboton.add(baceptar);
        baceptar.setBounds(334, 49, 100, 30);
        pboton.add(bcancelar);
        bcancelar.setBounds(452, 49, 100, 30);

        cLabel5.setText("Producto Orig");
        cLabel5.setPreferredSize(new java.awt.Dimension(76, 17));
        pboton.add(cLabel5);
        cLabel5.setBounds(13, 3, 76, 17);

        pro_codiE.setEnabled(false);
        pboton.add(pro_codiE);
        pro_codiE.setBounds(93, 3, 337, 17);

        cLabel6.setText("Proveedor");
        cLabel6.setPreferredSize(new java.awt.Dimension(76, 17));
        pboton.add(cLabel6);
        cLabel6.setBounds(13, 28, 66, 17);

        prv_codiE.setEnabled(false);
        pboton.add(prv_codiE);
        prv_codiE.setBounds(93, 28, 281, 17);

        cLabel7.setText("Fec.Cad.");
        cLabel7.setPreferredSize(new java.awt.Dimension(46, 17));
        pboton.add(cLabel7);
        cLabel7.setBounds(384, 28, 46, 17);

        feccadE.setEnabled(false);
        feccadE.setMinimumSize(new java.awt.Dimension(2, 18));
        feccadE.setPreferredSize(new java.awt.Dimension(60, 17));
        pboton.add(feccadE);
        feccadE.setBounds(436, 28, 80, 19);

        cLabel8.setText("Kilos");
        cLabel8.setPreferredSize(new java.awt.Dimension(76, 17));
        pboton.add(cLabel8);
        cLabel8.setBounds(436, 4, 35, 17);

        deo_kilosE.setEnabled(false);
        deo_kilosE.setMinimumSize(new java.awt.Dimension(2, 18));
        deo_kilosE.setPreferredSize(new java.awt.Dimension(60, 17));
        pboton.add(deo_kilosE);
        deo_kilosE.setBounds(475, 3, 60, 19);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        getContentPane().add(pboton, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CTextField avc_fecalbE;
    private gnu.chu.camposdb.AvcPanel avc_numeE;
    private gnu.chu.controles.CButton baceptar;
    private gnu.chu.controles.CButton bbuscar;
    private gnu.chu.controles.CButton bcancelar;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.camposdb.cliPanel cli_codiE;
    private gnu.chu.controles.CTextField deo_kilosE;
    private gnu.chu.controles.CTextField feccadE;
    private gnu.chu.controles.CTextField grd_codiE;
    private gnu.chu.controles.Cgrid jtDesg;
    private gnu.chu.controles.Cgrid jtLin;
    private gnu.chu.controles.CPanel pCabe;
    private gnu.chu.controles.CPanel pboton;
    private gnu.chu.camposdb.proPanel pro_codiE;
    private gnu.chu.camposdb.prvPanel prv_codiE;
    // End of variables declaration//GEN-END:variables

}
