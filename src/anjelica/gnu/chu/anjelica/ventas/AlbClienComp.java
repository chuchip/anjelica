package gnu.chu.anjelica.ventas;

/**
 *
 * <p>Título: AlbClienComp </p>
 * <p>Descripción: Muestra un desglose de  los Albaranes Vendidos en una zona/Repr. en un periodo
 * comparandolo con otro periodo</p>
 * <p>LLamado por las clases Covezore</p>
 * <p>Copyright: Copyright (c) 2005-2012
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
import com.jgoodies.looks.plastic.PlasticInternalFrameUI;
import gnu.chu.anjelica.pad.MantArticulos;
import gnu.chu.anjelica.pad.MantFamPro;
import gnu.chu.anjelica.pad.pdgruppro;
import gnu.chu.controles.Cgrid;
import gnu.chu.controles.StatusBar;
import gnu.chu.utilidades.ventana;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AlbClienComp   extends ventana {
    Covezore padre;
    conVenProd padrePro;
    String fecIni, fecFin;
    String repCodiC,zonCodiC;
    int sbeCodiC;

    public AlbClienComp() {
        try {
            iniciarFrame();
        } catch (Exception k)
        {
            Error("Error al iniciar AlbClienComp ",k);
        }
        this.setVersion("2012-05-24");
        statusBar = new StatusBar(this);
        initComponents();
        setResizable(true);
        setMaximizable(false);
        setIconifiable(false);
        this.setSize(600,440);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        this.putClientProperty(
             PlasticInternalFrameUI.IS_PALETTE,
             Boolean.TRUE);
//        BasicInternalFrameUI miUi = (BasicInternalFrameUI)this.getUI();
//        miUi.setNorthPane(null); // Para quitar el titulo a la ventana
    }
    public void iniciar(Covezore papa)
    {
        dtCon1=papa.dtCon1;
        dtStat=papa.dtStat;
        padre=papa;
        activarEventos();
    }
    private void activarEventos()
    {
        grupoE.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
               cargaDatosArt();
            }
        });
    }
  @Override
  public void matar()
  {
    setVisible(false);
    ventana papa;
    if (padre==null)
        papa=padrePro;
    else
        papa=padre;
    papa.setEnabled(true);
    papa.setFoco(null);
    try {
        papa.setSelected(true);
    } catch (Exception k){}
  }
 
   private void setCabeceraGrid(Cgrid jt,int anoIni,int anoFin)
   {
        ArrayList v=new ArrayList();
        v.add("Codigo"); // 0
        v.add("Nombre"); // 1
        v.add("Kil."+(anoIni==0?"Ini":""+anoIni)); // 2
        v.add("Kil."+(anoFin==0?"Fin":""+anoFin)); // 3
        v.add("Dif.Kil"); //4
        v.add("Imp."+(anoIni==0?"Ini":""+anoIni)); // 5
        v.add("Imp."+(anoFin==0?"Fin":""+anoFin)); // 6
        v.add("Dif.Imp"); //7
        jt.setCabecera(v);
        jt.setAnchoColumna(new int[]{50,180,72,72,72,72,72,74});
        jt.setAlinearColumna(new int[]{0,0,2,2,2,2,2,2});
        jt.setAlinearColumna(new int[]{0,0,2,2,2,2,2,2});
        jt.setFormatoColumna(2,"---,--9");
        jt.setFormatoColumna(3,"----,--9");
        jt.setFormatoColumna(4,"----,--9");
        jt.setFormatoColumna(5,"--,---,--9");
        jt.setFormatoColumna(6,"--,---,--9");
        jt.setFormatoColumna(7,"--,---,--9");
        jt.setAjustarGrid(true);
    }
  private String getSqlCli(String repCodi, String zonCodi,int sbeCodi,String fecIni,String fecFin)
  {
     return "select  c.cli_codi,cl.cli_nomb,"+
        " sum(avc_kilos) as avl_canti,sum(avc_basimp)  as  importe "+
        " from v_albavec c,clientes cl  "+
        " where c.cli_codi = cl.cli_codi " +
        " and c.avc_fecalb >= TO_DATE('" + fecIni + "','dd-MM-yyyy') " +
        " and c.avc_fecalb <= TO_DATE('" + fecFin + "','dd-MM-yyyy') " +
        (zonCodi.equals("__")?"":" and cl.zon_codi LIKE '" + zonCodi + "'") +
        (repCodi.equals("__")?"":" and cl.rep_codi LIKE '" + repCodi + "'") +
        (sbeCodi==0?"":" and c.sbe_codi = "+sbeCodi)+
        (padre.getEmpresa()==0?"":" and c.emp_codi = "+padre.getEmpresa())+
        (padre.getSubEmpresa()==0?"":" and c.sbe_codi = "+padre.getSubEmpresa())+
        (padre.EU.isRootAV()?"":" and c.div_codi > 0 ")+
        padre.getCondDiscri("cl")+
        " group by c.cli_codi,cl.cli_nomb" +
        " ORDER BY c.cli_codi";
  }
  
  private String getSqlArt(String repCodi, String zonCodi,int sbeCodi,String fecIni,String fecFin)
  {
      char grupo=grupoE.getValor().charAt(0);
      String s1="",s2="";
      String condWhere=", sum(l.avl_canti) as kilos, " +
             " sum(l.avl_canti*l.avl_prbase)  as importe" +
             " from v_albventa as l,clientes AS cl, v_articulo as a "
           + (grupo=='F'?", v_famipro as f ":"")+
             (grupo=='G'?", v_agupro as g,grufampro as gf ":"") +
            " where avc_fecalb >= TO_DATE('" + fecIni+
           "','dd-MM-yyyy') " +
           " and avc_fecalb <= TO_DATE('" + fecFin +
           "','dd-MM-yyyy') " +
           " and a.pro_codi = l.pro_codi "+
           (grupo=='F'?" and a.fam_codi = f.fpr_codi ":"")+
           (grupo=='G'?" and g.agr_codi = gf.agr_codi "+
             " and a.fam_codi = gf.fpr_codi ":"")+
           " and avc_serie >= 'A' AND avc_serie <='C' " +
           " and cl.cli_codi = l.cli_codi " +
           " and l.avl_canti != 0 " +
           (padre.EU.isRootAV()?"":" and l.div_codi > 0 ")+
           (zonCodi.equals("__")?"": " and cl.rep_codi LIKE '" + repCodi + "'" ) +
           (zonCodi.equals("__")?"": " and cl.zon_codi LIKE '" + zonCodi + "'" ) +
           (sbeCodi==0?"":" and l.sbe_codi = "+sbeCodi);
              
         switch (grupoE.getValor().charAt(0) )
         {
             case 'A':
                 s1=" l.pro_codi  ";
                 s2=" group by l.pro_codi order by l.pro_codi";
                 break;
             case 'F':
                 s1=" f.fpr_codi ";
                 s2=" group by f.fpr_codi order by f.fpr_codi ";
                 break;
             case 'G':
                 s1=" g.agr_codi  ";
                 s2=" group by g.agr_codi order by g.agr_codi ";
         }
       return  "select  "+s1 +
             condWhere+
             s2;
       
  }
  void cargaDatos(String repCodi, String zonCodi,int sbeCodi) throws Exception
  {
    repCodiC=repCodi;
    zonCodiC=zonCodi;
    sbeCodiC=sbeCodi;
    cargaDatosCli(repCodi,zonCodi,sbeCodi);
    cargaDatosArt(repCodi,zonCodi,sbeCodi);
    statusBar.setEnabled(true);
    jtCli.requestFocusInicio();

    jtCli.setEnabled(true);
  }
  void cargaDatosArt()
  {
        try
        {
            cargaDatosArt(repCodiC, zonCodiC,sbeCodiC);
        } catch (Exception ex)
        {
            Logger.getLogger(AlbClienComp.class.getName()).log(Level.SEVERE, null, ex);
        }
  }
  void cargaDatosArt(String repCodi, String zonCodi,int sbeCodi) throws Exception
  {
    setCabeceraGrid(jtArt,padre.getCabeceraInic(),padre.getCabeceraFin());
    jtArt.cuadrarGrid();
    jtArt.removeAllDatos();
    String s;
    s=getSqlArt(repCodi,zonCodi,sbeCodi,padre.getFechaInic(),padre.getFechaFinal());
    char grupo=grupoE.getValor().charAt(0);
    datComp dt;
//  System.out.println("albVenZon: "+s);
    HashMap<Integer,datComp> ht=new HashMap();
    int proNume=0;
    String proNomb="";
    if (dtCon1.select(s))
    {
      do
      {
         switch (grupo )
         {
             case 'A':
                 proNume=dtCon1.getInt("pro_codi");
                 proNomb=MantArticulos.getNombProd(proNume,dtStat);
                 break;
             case 'F':
                 proNume=dtCon1.getInt("fpr_codi");
                 proNomb=MantFamPro.getNombreFam(proNume,dtStat);
                 break;
             case 'G':
                 proNume=dtCon1.getInt("agr_codi");
                 proNomb=pdgruppro.getNombreGrupo(proNume, dtStat);
         }
        ht.put(proNume,
                new datComp(proNume,proNomb,
                dtCon1.getDouble("kilos"),
                dtCon1.getDouble("importe")));
      } while (dtCon1.next());
    }
    
    s=getSqlArt(repCodi,zonCodi,sbeCodi,padre.getFechaInicComp(),padre.getFechaFinComp());
    if (dtCon1.select(s))
    {
      do
      {
         switch (grupo )
         {
             case 'A':
                 proNume=dtCon1.getInt("pro_codi");
                 proNomb=MantArticulos.getNombProd(proNume,dtStat);
                 break;
             case 'F':
                 proNume=dtCon1.getInt("fpr_codi");
                 proNomb=MantFamPro.getNombreFam(proNume,dtStat);
                 break;
             case 'G':
                 proNume=dtCon1.getInt("agr_codi");
                 proNomb=pdgruppro.getNombreGrupo(proNume, dtStat);
         }
        if ((dt=ht.get(proNume))==null )
              dt=new datComp(proNume,proNomb,0,0);
        dt.kilosFin=dtCon1.getDouble("kilos");
        dt.impFin=dtCon1.getDouble("importe");
        ht.put(proNume,dt);
      } while (dtCon1.next());
    }
    /**
     * Lo ordeno segun el codigo de la familia
     */
    Set ref = ht.keySet();
    Iterator<Integer> it=ref.iterator();
    ArrayList<datComp> dtc=new ArrayList();
   
    while (it.hasNext())
    {
        dtc.add(ht.get(it.next()));
    }
    int nRow=dtc.size()-1;
    datComp dt1;
    datComp dt2;
//    datComp dtTemp;
    boolean swCambios=true;
    while (swCambios)
    {
        swCambios=false;
        for (int n=0;n<nRow;n++)
        {
           dt1=dtc.get(n);
           dt2=dtc.get(n+1);
           if (dt1.cliCodi>dt2.cliCodi )
           { // Intercambio
               dtc.set(n, dt2);
               dtc.set(n+1,dt1);
               swCambios=true;
           }
        }
    }
    // Fin de Ordenacion
    nRow++;

   for (int n=0;n<nRow;n++)
   {
       ArrayList v=new ArrayList();
       dt=dtc.get(n);
       v.add(dt.cliCodi);
       v.add(dt.cliNomb);
       v.add(dt.kilosOri);
       v.add(dt.kilosFin);
       v.add(dt.kilosOri-dt.kilosFin);
       v.add(dt.impOri);
       v.add(dt.impFin);
       v.add(dt.impOri-dt.impFin);
       jtArt.addLinea(v);
   }
  }
  
  void cargaDatosCli(String repCodi, String zonCodi,int sbeCodi) throws Exception
  {
    setCabeceraGrid(jtCli,padre.getCabeceraInic(),padre.getCabeceraFin());
  
    jtCli.cuadrarGrid();
    jtCli.removeAllDatos();
    String s;
    s=getSqlCli(repCodi,zonCodi,sbeCodi,padre.getFechaInic(),padre.getFechaFinal());
    datComp dt;
//  System.out.println("albVenZon: "+s);
    HashMap<Integer,datComp> ht=new HashMap();
    if (dtCon1.select(s))
    {
      do
      {
        ht.put(dtCon1.getInt("cli_codi"),
                new datComp(dtCon1.getInt("cli_codi"),
            dtCon1.getString("cli_nomb"),
                dtCon1.getDouble("avl_canti"),
                dtCon1.getDouble("importe")));
      } while (dtCon1.next());
    }
    
    s=getSqlCli(repCodi,zonCodi,sbeCodi,padre.getFechaInicComp(),padre.getFechaFinComp());
    if (dtCon1.select(s))
    {
      do
      {
        if ((dt=ht.get(dtCon1.getInt("cli_codi")))==null )
              dt=new datComp(dtCon1.getInt("cli_codi"),
                  dtCon1.getString("cli_nomb"),0,0);
        dt.kilosFin=dtCon1.getDouble("avl_canti");
        dt.impFin=dtCon1.getDouble("importe");
        ht.put(dtCon1.getInt("cli_codi"),
                dt);
      } while (dtCon1.next());
    }
    /**
     * Lo ordeno segun diferencia de kilos
     */
    Set ref = ht.keySet();
    Iterator<Integer> it=ref.iterator();
    ArrayList<datComp> dtc=new ArrayList();
   
    while (it.hasNext())
    {
        dtc.add(ht.get(it.next()));
    }
    int nRow=dtc.size()-1;
    datComp dt1;
    datComp dt2;
//    datComp dtTemp;
    boolean swCambios=true;
    while (swCambios)
    {
        swCambios=false;
        for (int n=0;n<nRow;n++)
        {
           dt1=dtc.get(n);
           dt2=dtc.get(n+1);
           if (dt1.kilosOri-dt1.kilosFin>dt2.kilosOri-dt2.kilosFin )
           { // Intercambio
               dtc.set(n, dt2);
               dtc.set(n+1,dt1);
               swCambios=true;
           }
        }
    }
    // Fin de Ordenacion
    nRow++;

   for (int n=0;n<nRow;n++)
   {
       ArrayList v=new ArrayList();
       dt=dtc.get(n);
       v.add(dt.cliCodi);
       v.add(dt.cliNomb);
       v.add(dt.kilosOri);
       v.add(dt.kilosFin);
       v.add(dt.kilosFin-dt.kilosOri);
       v.add(dt.impOri);
       v.add(dt.impFin);
       v.add(dt.impFin-dt.impOri);
       jtCli.addLinea(v);
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

        cTabbedPane1 = new gnu.chu.controles.CTabbedPane();
        jtCli = new gnu.chu.controles.Cgrid(8);
        Partic = new gnu.chu.controles.CPanel();
        Pcabe = new gnu.chu.controles.CPanel();
        cLabel1 = new gnu.chu.controles.CLabel();
        grupoE = new gnu.chu.controles.CComboBox();
        jtArt = new gnu.chu.controles.Cgrid(8);

        jtCli.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtCli.setPreferredSize(new java.awt.Dimension(100, 100));

        javax.swing.GroupLayout jtCliLayout = new javax.swing.GroupLayout(jtCli);
        jtCli.setLayout(jtCliLayout);
        jtCliLayout.setHorizontalGroup(
            jtCliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 489, Short.MAX_VALUE)
        );
        jtCliLayout.setVerticalGroup(
            jtCliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 353, Short.MAX_VALUE)
        );

        cTabbedPane1.addTab("Clientes", jtCli);

        Partic.setLayout(new java.awt.GridBagLayout());

        Pcabe.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        Pcabe.setMaximumSize(new java.awt.Dimension(207, 25));
        Pcabe.setMinimumSize(new java.awt.Dimension(207, 25));
        Pcabe.setName(""); // NOI18N
        Pcabe.setPreferredSize(new java.awt.Dimension(207, 25));
        Pcabe.setLayout(null);

        cLabel1.setText("Agrupar por");
        Pcabe.add(cLabel1);
        cLabel1.setBounds(12, 2, 80, 17);

        grupoE.addItem("Grupo","G");
        grupoE.addItem("Prod", "A");
        grupoE.addItem("Fam.","F");
        Pcabe.add(grupoE);
        grupoE.setBounds(100, 2, 97, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        Partic.add(Pcabe, gridBagConstraints);

        jtArt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        setCabeceraGrid(jtArt,0,0);

        javax.swing.GroupLayout jtArtLayout = new javax.swing.GroupLayout(jtArt);
        jtArt.setLayout(jtArtLayout);
        jtArtLayout.setHorizontalGroup(
            jtArtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 487, Short.MAX_VALUE)
        );
        jtArtLayout.setVerticalGroup(
            jtArtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 317, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Partic.add(jtArt, gridBagConstraints);

        cTabbedPane1.addTab("Articulos", Partic);

        getContentPane().add(cTabbedPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CPanel Partic;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CTabbedPane cTabbedPane1;
    private gnu.chu.controles.CComboBox grupoE;
    private gnu.chu.controles.Cgrid jtArt;
    private gnu.chu.controles.Cgrid jtCli;
    // End of variables declaration//GEN-END:variables

}
class datComp
{
    double kilosOri=0,kilosFin=0;
    double impOri=0;double impFin=0;
    String cliNomb;
    int cliCodi;
    
    public datComp(int cli_codi,String cli_nomb,double kilos,double importe)
    {
        cliCodi=cli_codi;
        cliNomb=cli_nomb;
        kilosOri=kilos;
        impOri=importe;
    }
}
