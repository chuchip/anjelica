/**
 *
 * <p>Título: AlbVenZR </p>
 * <p>Descripción: Muestra un desglose de  los Albaranes Vendidos en una zona/Repr.</p>
 * <p>LLamado por las clases conVenProd y conVenZonas</p>
 * <p>Copyright: Copyright (c) 2005-2016
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

import com.jgoodies.looks.plastic.PlasticInternalFrameUI;
import gnu.chu.anjelica.margenes.Clmarzona;
import gnu.chu.controles.StatusBar;
import gnu.chu.interfaces.ejecutable;
import gnu.chu.utilidades.ventana;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class AlbVenZR extends ventana {
    private boolean swGanan=false;
    final  static int JT_KILOS=3;
    final  static int JT_IMPGAN=5;
    final  static int JT_KILGAN=6;
    Covezore padre;
    conVenProd padrePro;
    String fecIni, fecFin;
    String repCodiE=null;
    int sbeCodiE=0;
    
    public AlbVenZR() {
        try {
            iniciarFrame();
        } catch (Exception k)
        {
            Error("Error al iniciar AlbVenZR ",k);
        }
        this.setVersion("2017-01-08");
        statusBar = new StatusBar(this);
        initComponents();
        setResizable(false);
        setMaximizable(false);
        setIconifiable(false);
        this.setSize(450,440);
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
        jf=padre.jf;
        activarEventos();
    }
    public void iniciar(conVenProd papa)
    {
        dtCon1=papa.dtCon1;
        dtStat=papa.dtStat;
        padrePro=papa;
        EU=padrePro.EU;
        activarEventos();
    }
    void activarEventos()
    {
    jt.tableView.getSelectionModel().addListSelectionListener(new
        ListSelectionListener()
    {
            @Override
      public void valueChanged(ListSelectionEvent e)
      {
        if (jt.isVacio() || jt.isEnabled()==false)
          return;
        try  {
            if (padre==null)
                verAlbClien(fecIni,fecFin);
            else
                verAlbClien();
        } catch (Exception k)
        {
            Error("Error al ver datos albaranes de clientes",k);
        }
      }
    });
    jt.addMouseListener(new MouseAdapter()
    {
               @Override
          public void mouseClicked(MouseEvent e) {
            if (jt.isVacio() || !swGanan)
              return;
            if (e.getClickCount()<2)
              return;
            
            llamaProgGana();
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
  public void verAlbClien(String fecini,String fecfin) throws Exception
  {
    String s="SELECT * FROM v_albavec as c WHERE cli_codi = "+jt.getValorInt(0)+          
          " and avc_fecalb >= TO_DATE('"+fecini+"','dd-MM-yyyy') "+
          " and avc_fecalb <= TO_DATE('"+fecfin+"','dd-MM-yyyy') "+
           (EU.isRootAV()?"":" and c.div_codi > 0 ")+
          (repCodiE==null?"":" and avc_repres= '"+repCodiE+"'")+
          (sbeCodiE==0?"":" and sbe_codi= "+sbeCodiE)+
          " order by avc_fecalb ";
      jtAlb.removeAllDatos();
      if (!dtStat.select(s))
        return;
      do
      {
        ArrayList v= new ArrayList();
        v.add(dtStat.getString("emp_codi"));
        v.add(dtStat.getString("avc_serie"));
        v.add(dtStat.getString("avc_nume"));
        v.add(dtStat.getFecha("avc_fecalb","dd-MM-yyyy"));
        v.add(dtStat.getString("avc_kilos"));
        v.add(dtStat.getString("avc_basimp"));
        jtAlb.addLinea(v);
      } while (dtStat.next());
      jtAlb.requestFocusInicio();

  }
  void verAlbClien()
  {
    try
    {
      String s="SELECT * FROM v_albavec as c WHERE cli_codi = "+jt.getValorInt(0)+
          " and avc_fecalb >= TO_DATE('"+padre.getFechaInic()+"','dd-MM-yyyy') "+
          " and avc_fecalb <= TO_DATE('"+padre.getFechaFinal()+"','dd-MM-yyyy') "+
          (padre.getEmpresa()==0?"":" and c.emp_codi = "+padre.getEmpresa())+
          (padre.getSubEmpresa()==0?"":" and c.sbe_codi = "+padre.getSubEmpresa())+
           (padre.EU.isRootAV()?"":" and c.div_codi > 0 ")+
          (repCodiE==null?"":" and avc_repres= '"+repCodiE+"'")+
          (sbeCodiE==0?"":" and sbe_codi= "+sbeCodiE)+
          " order by avc_fecalb ";
      jtAlb.removeAllDatos();
      if (!dtStat.select(s))
        return;
      do
      {
        ArrayList v= new ArrayList();
        v.add(dtStat.getString("emp_codi"));
        v.add(dtStat.getString("avc_serie"));
        v.add(dtStat.getString("avc_nume"));
        v.add(dtStat.getFecha("avc_fecalb","dd-MM-yyyy"));
        v.add(dtStat.getString("avc_kilos"));
        v.add(dtStat.getString("avc_basimp"));
        jtAlb.addLinea(v);
      } while (dtStat.next());
      jtAlb.requestFocusInicio();
    } catch (Exception k)
    {
      Error("Error al buscar albaranes de Cliente",k);
    }
  }
  
  /**
   * Rutina utilizada por el programa Covezore
   * @param repCodi
   * @param zonCodi
   * @param sbeCodi
   * @param ht Datos de ganancia pasados.
   * @throws Exception 
   */
  void cargaDatos(String repCodi, String zonCodi,int sbeCodi,HashMap<String,Double> ht) throws Exception
  {
    repCodiE=repCodi;
    sbeCodiE=sbeCodi;
    swGanan=ht!=null;
    jt.setEnabled(false);
    String s;
    s="select  c.cli_codi,cl.cli_nomb,count(distinct c.avc_nume) as cuantos,"+
        " sum(avc_kilos) as avl_canti,sum(avc_basimp)  as  importe,0 as ganan,0 as kilgana "+
        " from v_albavec c,clientes cl  "+
        " where c.cli_codi = cl.cli_codi " +
        " and c.avc_fecalb >= TO_DATE('" + padre.getFechaInic() + "','dd-MM-yyyy') " +
        " and c.avc_fecalb <= TO_DATE('" + padre.getFechaFinal() + "','dd-MM-yyyy') " +
        " and cl.zon_codi = '" + zonCodi + "'" +
        " and avc_repres = '" + repCodi + "'" +
        " and c.sbe_codi = "+sbeCodi+
        (padre.getEmpresa()==0?"":" and c.emp_codi = "+padre.getEmpresa())+
        (padre.getSubEmpresa()==0?"":" and c.sbe_codi = "+padre.getSubEmpresa())+
        (padre.EU.isRootAV()?"":" and c.div_codi > 0 ")+
        padre.getCondDiscri("cl")+
        " group by c.cli_codi,cl.cli_nomb" +
        " ORDER BY c.cli_codi";
//  System.out.println("albVenZon: "+s);
   dtCon1.select(s);
   jt.setDatos(dtCon1);
   statusBar.setEnabled(true);
//   this.setEnabled(true);
//   PintrDatos.setEnabled(false);
   
    ponGanancia(ht,sbeCodi);
  
       
   jt.requestFocusInicio();
   verAlbClien();
   jt.setEnabled(true);
//   Formatear.verAncGrid(jt);
//   System.out.println("Ancho: "+this.getSize().getWidth());
  }
  void ponGanancia(HashMap<String,Double> ht, int sbeCodi)
  {
      if (ht==null)
          return;
      Iterator<String> it = ht.keySet().iterator();
        int nl = jt.getRowCount();
        String[] valorC;
        String valor;
        while (it.hasNext())
        {           
            valor = it.next();
//            System.out.println("valor : "+valor+" Ganancia: "+ht.get(valor));
            valorC = valor.split("-", 2);
            for (int n = 0; n < nl; n++)
            {
                if (sbeCodi ==  Integer.parseInt(valorC[0]) && jt.getValString(n, 0).equals(valorC[1]))
                {
                    double gananc=ht.get(valor);
                    jt.setValor(gananc, n, JT_IMPGAN);
                    jt.setValor(gananc/jt.getValorDec(n, JT_KILOS),n,JT_KILGAN);
                    break;
                }
            }
        }
  }
  
  private void llamaProgGana()
  {
        try
        {    
            ejecutable prog;
            gnu.chu.anjelica.margenes.Clmarzona cm;
            if (jf!=null)
            {                
                if ((prog=jf.gestor.getProceso(Clmarzona.getNombreClase()))==null)
                    return;
                cm= (gnu.chu.anjelica.margenes.Clmarzona) prog;
            }
            else
            {
                 cm= new gnu.chu.anjelica.margenes.Clmarzona(padre.menu,padre.EU);
                 padre.menu.lanzaEjecutable(cm);
            }
            cm.setFechaInicial(padre.getDateInicial());
            cm.setFechaFinal(padre.getDateFinal());
            cm.setCliente(jt.getValorInt(0));
            cm.setIncluirVert(false);
            cm.ejecutaConsulta();
            if (jf!=null)
                jf.gestor.ir(cm);
        } catch (ParseException ex)
        {
            padre.Error("Error al llamar programa consulta de Margenes por productos", ex);
        }
  }
 /**
   * LLamado por la clase   conVenProd
   * 
   * @param fecini String Fecha Inicio
   * @param fecfin String Fecha Final
   * @param proCodi int Codigo
   * @param zonCodi String Filtro de Zona
   * @param repCodi String Filtro de Repr.
   * @param serieIni String Serie Inicial
   * @param serieFin String Serie Final
   * @param cliCodi int Cod. Cliente
   * @param grupo Tipo Codigo. A Articulo, F Familia, G Grupo
   * @throws Exception
   */
  public void cargaDatosPro(String fecini,String fecfin,
                       int proCodi,String zonCodi,String repCodi,
                       String serieIni,String serieFin,int cliCodi,char grupo) throws Exception
  {
    repCodiE=repCodi;
    sbeCodiE=0;
    jt.setEnabled(false);
    this.fecIni=fecini;
    this.fecFin=fecfin;
//  feciniE.setText(fecini);
//  fecfinE.setText(fecfin);
//  pro_codiE.setText(""+proCodi);
//  zon_codiE.setText(zonCodi);
//  zon_nombE.setText(zonNomb);
  

  String s="select  c.cli_codi,cl.cli_nomb,count(distinct c.avc_nume) as cuantos,"+
      " sum(avl_canti) as avl_canti,sum(avl_canti*avl_prven)  as  importe,0 as impGana,0 as porcGana "+
      " from v_albventa as c,clientes cl,v_articulo as a"+
       (grupo=='F'?", v_famipro as f ":"")+
       (grupo=='G'?", v_agupro as g,grufampro as gf ":"") +
      " where c.cli_codi = cl.cli_codi " +
      " and c.avc_fecalb >= TO_DATE('" + fecini + "','dd-MM-yyyy') " +
      " and c.avc_fecalb <= TO_DATE('" + fecfin + "','dd-MM-yyyy') " +
      " and c.avc_serie >= '" + serieIni + "'" +
      "  AND c.avc_serie <= '" + serieFin + "'" +
      (repCodi==null?"":" and avc_repres like '"+repCodi+"'")+
      (cliCodi ==0?"":" and c.cli_codi = "+cliCodi)+
      (zonCodi==null?"":" AND cl.zon_codi like '" + zonCodi + "'") +
      " and a.pro_codi = c.pro_codi "+
      (grupo=='A'?" AND a.pro_codi = "+proCodi:"")+
      (grupo=='F'?" and a.fam_codi = f.fpr_codi and a.fam_codi= "+proCodi:"")+
      (grupo=='G'?" and g.agr_codi = gf.agr_codi "+
             " and a.fam_codi = gf.fpr_codi and g.agr_codi= "+proCodi:"")+     
      " group by c.cli_codi,cl.cli_nomb" +
      " ORDER BY c.cli_codi";
//  System.out.println("albVenZon: "+s);
  dtCon1.select(s);
  jt.setDatos(dtCon1);
  statusBar.setEnabled(true);
  this.setEnabled(true);
 
  jt.requestFocusInicio();

  verAlbClien(fecini, fecfin);
  jt.setEnabled(true);
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
        jt = new gnu.chu.controles.Cgrid(7);
        jtAlb = new gnu.chu.controles.Cgrid(6);

        Pprinc.setLayout(new java.awt.GridBagLayout());

        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jt.setPreferredSize(new java.awt.Dimension(100, 100));
        ArrayList v=new ArrayList();
        v.add("Cliente"); // 0
        v.add("Nombre"); // 1
        v.add("N.Alb"); // 2
        v.add("Kilos"); // 3
        v.add("Imp.Ventas"); // 4
        v.add("Imp.Gana"); // 5
        v.add("€/Gana"); // 6
        jt.setCabecera(v);
        jt.setAnchoColumna(new int[]{50,180,42,72,84,70,50});
        jt.setAlinearColumna(new int[]{0,0,2,2,2,2,2});
        jt.setFormatoColumna(3,"---,--9.99");
        jt.setFormatoColumna(4,"---,--9.99");
        jt.setFormatoColumna(5,"---,--9");
        jt.setFormatoColumna(6,"--9.999");
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
        ArrayList v1=new ArrayList();
        v1.add("Emp"); // 0
        v1.add("Serie"); // 1
        v1.add("Albaran"); // 2
        v1.add("Fec.Alb"); // 3
        v1.add("Kg. Alb"); // 4
        v1.add("Imp.Alb"); // 5
        jtAlb.setCabecera(v1);
        jtAlb.setAnchoColumna(new int[]{40,40,50,70,80,80});
        jtAlb.setAlinearColumna(new int[]{0, 0, 2, 1, 2, 2});
        jtAlb.setFormatoColumna(4, "---,--9.99");
        jtAlb.setFormatoColumna(5, "---,--9.99");
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
