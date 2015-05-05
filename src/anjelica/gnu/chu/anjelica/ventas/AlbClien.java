package gnu.chu.anjelica.ventas;


import com.jgoodies.looks.plastic.PlasticInternalFrameUI;
import gnu.chu.controles.StatusBar;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.ventana;
import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class AlbClien extends ventana {
    ventana padre;
    final int JTCAB_AVCANO=1;
    final int JTCAB_EMPCODI=0;
    final int JTCAB_AVCNUME=3;
    final int JTCAB_AVCSERIE=2;

    private boolean verPrecios=true;
    /** Creates new form AlbClien */
    public AlbClien() {
        try {
            iniciarFrame();
        } catch (Exception k)
        {
            Error("Error al iniciar AlbVenZR ",k);
        }
        this.setVersion("2012-05-01");
        statusBar = new StatusBar(this);
        initComponents();
        setResizable(false);
        setMaximizable(false);
        setIconifiable(false);
        this.setSize(400,440);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        this.putClientProperty(
             PlasticInternalFrameUI.IS_PALETTE,
             Boolean.TRUE);
//        BasicInternalFrameUI miUi = (BasicInternalFrameUI)this.getUI();
//        miUi.setNorthPane(null); // Para quitar el titulo a la ventana
    }
    public void iniciarFrame()
    {
        this.setTitle(getTitulo());
        this.setResizable(true);
        this.setMaximizable(true);
        this.setIconifiable(true);
    }
    public void iniciar(ventana papa)
    {
        dtCon1=papa.dtCon1;
        dtStat=papa.dtStat;
        padre=papa;
        EU=papa.EU;
        activarEventos();
    }
    public void setVerPrecios(boolean swVerPrecios)
    {
        verPrecios=swVerPrecios;
    }
    public boolean getVerPrecios()
    {
        return verPrecios;
    }
    public void iniciar(conVenProd papa)
    {
        dtCon1=papa.dtCon1;
        dtStat=papa.dtStat;
        padre=papa;
        EU=padre.EU;
        activarEventos();
    }
    void activarEventos()
    {
        jtCab.tableView.getSelectionModel().addListSelectionListener(new
            ListSelectionListener()
        {
                @Override
          public void valueChanged(ListSelectionEvent e)
          {
            if (jtCab.isVacio() || jtCab.isEnabled()==false)
              return;
            try  {
                verAlbaran(jtCab.getSelectedRow());
            } catch (Exception k)
            {
                Error("Error al ver datos albaranes de clientes",k);
            }
          }
        });

  }
    @Override
  public void matar()
  {
    setVisible(false);
   
    padre.setEnabled(true);
    padre.setFoco(null);
    try {
        padre.setSelected(true);
    } catch (Exception k){}
  }

  public void cargaDatos(String fecini,String fecfin,int cliCodi) throws Exception
  {
    String s="SELECT * FROM v_albavec as c WHERE cli_codi = "+cliCodi+
          " and avc_fecalb >= TO_DATE('"+fecini+"','dd-MM-yyyy') "+
          " and avc_fecalb <= TO_DATE('"+fecfin+"','dd-MM-yyyy') "+
           (EU.isRootAV()?"":" and c.div_codi > 0 ")+
          " order by avc_fecalb ";
      jtCab.setEnabled(false);
      jtCab.removeAllDatos();
      if (!dtStat.select(s))
        return;
      do
      {
        ArrayList v= new ArrayList();
        v.add(dtStat.getString("emp_codi"));
        v.add(dtStat.getString("avc_ano"));
        v.add(dtStat.getString("avc_serie"));
        v.add(dtStat.getString("avc_nume"));
        v.add(dtStat.getFecha("avc_fecalb","dd-MM-yyyy"));
        v.add(dtStat.getString("avc_kilos"));
        v.add(dtStat.getString("avc_basimp"));
        jtCab.addLinea(v);
      } while (dtStat.next());
      
      jtCab.requestFocusInicio();
      jtCab.setEnabled(true);
      statusBar.setEnabled(true);
      verAlbaran(0);
  }
  
  private void verAlbaran(int numLinea)
  {
      try
      {
          jtLin.removeAllDatos();
          String s=pdalbara.getSqlLinList(jtCab.getValorInt(numLinea,JTCAB_AVCANO),
                  jtCab.getValorInt(numLinea,JTCAB_EMPCODI),
                  jtCab.getValString(numLinea,JTCAB_AVCSERIE),
                  jtCab.getValorInt(numLinea,JTCAB_AVCNUME),
                  true);
           String proNomb;
           if (dtCon1.select(s))
           {
             do
             {
                 ArrayList v = new ArrayList();
                 proNomb = dtCon1.getString("avl_pronom");
                 if (proNomb == null)
                    proNomb = dtCon1.getString("pro_nomb");
                 v.add(dtCon1.getString("pro_codi"));
                 v.add(proNomb);
                 v.add(""+Formatear.redondea(dtCon1.getDouble("avl_canti",true), 2));
                 v.add(dtCon1.getString("avl_unid"));
                 if (!verPrecios)
                    v.add("0");
                 else
                    v.add(""+Formatear.redondea(dtCon1.getDouble("avl_prven",true),3));
                 jtLin.addLinea(v);
             }  while (dtCon1.next());
             jtLin.requestFocus(0,0);
          }
      } catch (Exception k)
      {
          Error("Error al ver lineas de albaran",k);
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
        jtCab = new gnu.chu.controles.Cgrid(7);
        jtLin = new gnu.chu.controles.Cgrid(5);
        { ArrayList v = new ArrayList();
            v.add("Prod."); // 0
            v.add("Descripcion"); // 1
            v.add("Kilos"); // 2
            v.add("Unid"); // 3
            v.add("Precio"); // 4
            jtLin.setCabecera(v);
            jtLin.setAnchoColumna(new int[]
                {50, 200, 70, 40, 60});
            jtLin.setAlinearColumna(new int[]
                { 2, 0, 2, 2, 2});
        }

        jtLin.setFormatoColumna(2, "---,--9.99");
        jtLin.setFormatoColumna(3, "---9");
        jtLin.setFormatoColumna(4, "----9.99");
        jtLin.setAjustarGrid(true);

        Pprinc.setLayout(new java.awt.GridBagLayout());

        ArrayList v1=new ArrayList();
        v1.add("Emp"); // 0
        v1.add("Año"); // 1
        v1.add("Serie"); // 2
        v1.add("Albaran"); // 3
        v1.add("Fec.Alb"); // 4
        v1.add("Kg. Alb"); // 5
        v1.add("Imp.Alb"); // 6
        jtCab.setCabecera(v1);
        jtCab.setAnchoColumna(new int[]{40,40,40,50,70,80,80});
        jtCab.setAlinearColumna(new int[]{2, 2, 1,2, 1, 2, 2});
        jtCab.setFormatoColumna(4, "---,--9.99");
        jtCab.setFormatoColumna(5, "---,--9.99");
        jtCab.setAjustarGrid(true);
        jtCab.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtCab.setMaximumSize(new java.awt.Dimension(100, 100));
        jtCab.setPreferredSize(new java.awt.Dimension(100, 100));

        javax.swing.GroupLayout jtCabLayout = new javax.swing.GroupLayout(jtCab);
        jtCab.setLayout(jtCabLayout);
        jtCabLayout.setHorizontalGroup(
            jtCabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 392, Short.MAX_VALUE)
        );
        jtCabLayout.setVerticalGroup(
            jtCabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 149, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 2.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 0);
        Pprinc.add(jtCab, gridBagConstraints);

        jtLin.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtLin.setMaximumSize(new java.awt.Dimension(100, 100));
        jtLin.setPreferredSize(new java.awt.Dimension(100, 100));

        javax.swing.GroupLayout jtLinLayout = new javax.swing.GroupLayout(jtLin);
        jtLin.setLayout(jtLinLayout);
        jtLinLayout.setHorizontalGroup(
            jtLinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 393, Short.MAX_VALUE)
        );
        jtLinLayout.setVerticalGroup(
            jtLinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 123, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Pprinc.add(jtLin, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.Cgrid jtCab;
    private gnu.chu.controles.Cgrid jtLin;
    // End of variables declaration//GEN-END:variables

}
