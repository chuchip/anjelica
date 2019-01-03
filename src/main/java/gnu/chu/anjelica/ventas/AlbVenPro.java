/**
 *
 * <p>Título: AlbVenPro</p>
 * <p>Descripción: Muestra un desglose de  los productos vendidos para una familia </p>
 * <p>LLamado por las clases  Covezore </p>
 * <p>Copyright: Copyright (c) 2005-2010
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
 * @author chuchiP
 * @version 1.0
 *
 */
package gnu.chu.anjelica.ventas;

//import com.jgoodies.looks.plastic.PlasticInternalFrameUI;
import gnu.chu.controles.StatusBar;
import gnu.chu.utilidades.ventana;
import java.awt.BorderLayout;
import java.util.Vector;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicInternalFrameUI;

public class AlbVenPro extends ventana {
   
    Covezore padre;
    String fecIni, fecFin;

    public AlbVenPro() {
        try {
            iniciarFrame();
        } catch (Exception k)
        {
            Error("Error al iniciar AlbVenPro ",k);
        }
        this.setVersion("2011-01-26");
        statusBar = new StatusBar(this);
        initComponents();
        setResizable(false);
        setMaximizable(false);
        setIconifiable(false);
        this.setSize(400,440);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        setVisibleCabeceraVentana(false);
//        BasicInternalFrameUI miUi = (BasicInternalFrameUI)this.getUI();
//        miUi.setNorthPane(null); // Para quitar el titulo a la ventana
    }
    public void iniciar(Covezore papa)
    {
        dtCon1=papa.dtCon1;
        dtStat=papa.dtStat;
        padre=papa;
        EU=padre.EU;
        activarEventos();
    }
   
    void activarEventos()
    {
    jt.tableView.getSelectionModel().addListSelectionListener(new
        ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent e)
      {
        if (jt.isVacio() || jt.isEnabled()==false)
          return;
        try  {
                verDatClien(jt.getValorInt(0));
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
  public void verDatClien(int proCodi) throws Exception
  {
   String s="select  cl.cli_codi,cl.cli_nomb,"+
        " sum(avl_canti) as avl_canti,sum(avl_canti*avl_prbase)  as  importe "+
        " from v_albavec c,v_albavel as l,clientes cl  "+
        " where c.cli_codi = cl.cli_codi " +
        " and c.avc_fecalb >= TO_DATE('" + padre.getFechaInic() + "','dd-MM-yyyy') " +
        " and c.avc_fecalb <= TO_DATE('" + padre.getFechaFinal() + "','dd-MM-yyyy') " +
        " and l.avc_ano = c.avc_ano " +
        " and l.emp_Codi = c.emp_codi" +
        " and  l.avc_Serie = c.avc_serie" +
        " and l.avc_nume = c.avc_nume " +
        (padre.getZona().equals("")?"":" and cl.zon_codi = '" + padre.getZona() + "'" )+
        (padre.getRepresentante().equals("")?"":" and cl.rep_codi = '" + padre.getRepresentante()+ "'") +
        (padre.getEmpresa()==0?"":" and c.emp_codi = "+padre.getEmpresa())+
        (padre.getSubEmpresa()==0?"":" and c.sbe_codi = "+padre.getSubEmpresa())+
        (padre.EU.isRootAV()?"":" and c.div_codi > 0 ")+
        padre.getCondDiscri("cl")+
        " and l.pro_codi = "+proCodi+
        " group by cl.cli_codi,cl.cli_nomb" +
        " ORDER BY cl.cli_nomb";
      jtAlb.removeAllDatos();
      if (!dtStat.select(s))
        return;
      do
      {
        Vector v= new Vector();
        v.addElement(dtStat.getString("cli_codi"));
        v.addElement(dtStat.getString("cli_nomb"));
        v.addElement(dtStat.getString("avl_canti"));
        v.addElement(dtStat.getString("importe"));
        jtAlb.addLinea(v);
      } while (dtStat.next());
      jtAlb.requestFocusInicio();

  }
 
  /**
   * Rutina utilizada por el programa conVenZonas
   * @param ct conexion Conexion base datos
   * @param fecini String Fecha Inicio
   * @param fecfin String Fecha Final
   * @param zonCodi String Zona
   * @param zonNomb String Nombre Zona
   * @param serieIni String Serie Inicial
   * @param serieFin String Serie Final
   * @param cliCodi int Codigo de Cliente
   * @throws Exception Si hay algun error
   */
 
  void cargaDatosPro(int famCodi) throws Exception
  {
    jt.setEnabled(false);
    String s;
    s="select  a.pro_codi,a.pro_nomb,"+
        " sum(avl_canti) as avl_canti,sum(avl_canti*avl_prbase)  as  importe "+
        " from v_albavec c,v_albavel as l,clientes cl,v_articulo as a  "+
        " where c.cli_codi = cl.cli_codi " +
        " and c.avc_fecalb >= TO_DATE('" + padre.getFechaInic() + "','dd-MM-yyyy') " +
        " and c.avc_fecalb <= TO_DATE('" + padre.getFechaFinal() + "','dd-MM-yyyy') " +
        " and l.avc_ano = c.avc_ano " +
        " and l.emp_Codi = c.emp_codi" +
        " and  l.avc_Serie = c.avc_serie" +
        " and l.avc_nume = c.avc_nume " +
        (padre.getZona().equals("")?"":" and cl.zon_codi = '" + padre.getZona() + "'" )+
        (padre.getRepresentante().equals("")?"":" and cl.rep_codi = '" + padre.getRepresentante()+ "'") +
        (padre.getEmpresa()==0?"":" and c.emp_codi = "+padre.getEmpresa())+
        (padre.getSubEmpresa()==0?"":" and c.sbe_codi = "+padre.getSubEmpresa())+
        (padre.EU.isRootAV()?"":" and c.div_codi > 0 ")+
        padre.getCondDiscri("cl")+
        " and l.pro_codi = a.pro_codi "+
        " and a.fam_codi = "+famCodi+
        " group by a.pro_codi,a.pro_nomb" +
        " ORDER BY a.pro_codi";
   System.out.println("albVenPro: "+s);
   dtCon1.select(s);
   jt.setDatos(dtCon1);
   statusBar.setEnabled(true);
   jt.requestFocusInicio();
   jt.setEnabled(true);
   verDatClien(jt.getValorInt(0));
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
        jt = new gnu.chu.controles.Cgrid(4);
        jtAlb = new gnu.chu.controles.Cgrid(4);

        Pprinc.setLayout(new java.awt.GridBagLayout());

        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jt.setPreferredSize(new java.awt.Dimension(100, 100));
        Vector v=new Vector();
        v.add("Producto"); // 0
        v.add("Nombre"); // 1
        v.add("Kilos"); // 2
        v.add("Importe"); // 3
        jt.setCabecera(v);
        jt.setAnchoColumna(new int[]{50,180,72,84});
        jt.setAlinearColumna(new int[]{0,0,2,2});
        jt.setFormatoColumna(2,"---,--9.99");
        jt.setFormatoColumna(3,"---,--9.99");
        jt.setAjustarGrid(true);

        org.jdesktop.layout.GroupLayout jtLayout = new org.jdesktop.layout.GroupLayout(jt);
        jt.setLayout(jtLayout);
        jtLayout.setHorizontalGroup(
            jtLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 428, Short.MAX_VALUE)
        );
        jtLayout.setVerticalGroup(
            jtLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 232, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.weighty = 2.0;
        Pprinc.add(jt, gridBagConstraints);

        jtAlb.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtAlb.setPreferredSize(new java.awt.Dimension(100, 70));
        Vector v1=new Vector();
        v1.addElement("Cliente"); // 0
        v1.addElement("Nombre"); // 1
        v1.addElement("Kilos"); // 2
        v1.addElement("Importe"); // 3

        jtAlb.setCabecera(v1);
        jtAlb.setAnchoColumna(new int[]{40,160,80,80});
        jtAlb.setAlinearColumna(new int[]{0, 0, 2, 2});
        jtAlb.setFormatoColumna(2, "---,--9.99");
        jtAlb.setFormatoColumna(3, "---,--9.99");
        jtAlb.setAjustarGrid(true);

        org.jdesktop.layout.GroupLayout jtAlbLayout = new org.jdesktop.layout.GroupLayout(jtAlb);
        jtAlb.setLayout(jtAlbLayout);
        jtAlbLayout.setHorizontalGroup(
            jtAlbLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 428, Short.MAX_VALUE)
        );
        jtAlbLayout.setVerticalGroup(
            jtAlbLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 135, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Pprinc.add(jtAlb, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.Cgrid jt;
    private gnu.chu.controles.Cgrid jtAlb;
    // End of variables declaration//GEN-END:variables

}
