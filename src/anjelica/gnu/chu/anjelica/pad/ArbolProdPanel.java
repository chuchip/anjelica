package gnu.chu.anjelica.pad;

/**
 *
 * <p>Título: ArbolProdPanel </p>
 * <p>Descripción: Panel que muestra los grupos,familias y productos, en  un arbol
 *  </p>
 *  <p>Copyright: Copyright (c) 2005-2011
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
 * @author ChuchiP
 * @version 1.0
 */
import gnu.chu.controles.CPanel;
import gnu.chu.controles.CTabbedPane;
import gnu.chu.sql.DatosTabla;
import gnu.chu.sql.conexion;
import gnu.chu.utilidades.DatosProd;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.SystemOut;
import gnu.chu.utilidades.mensajes;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.swing.Icon;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;


public class ArbolProdPanel extends CPanel {
    String s;
    DefaultMutableTreeNode top;
    ArrayList  vtree=new ArrayList<JTree>();
    DatosTabla dt;
    conexion ct;
    Hashtable htArt=null;
    DatosProd valores;
    private class NodoInfo {
        public String grupo;
        public char tipo;
        
        DatosProd dtPro=null;

        public NodoInfo(String grupo, char tipo) {
            this.grupo = grupo;
            this.tipo=tipo;
        }
        public String getGrupo()
        {
            return grupo;
        }
        public void setGrupo(String grupo)
        {
            this.grupo=grupo;
        }
        public void setValores(DatosProd dtPro)
        {
            this.dtPro=dtPro;
        }

        @Override
        public String toString() {
            return grupo+
                    (getKilos()==null?"":" - "+Formatear.format(getKilos(),"----,--9.9").trim()+" Kg")+
                    (getGananciaKilo()==null || getGananciaKilo()==0 ?"":" ("+Formatear.format(getGananciaKilo(),"--9.9").trim()+" €/Kg)");
        }

        public char getTipo()
        {
            return tipo;
        }

        public Double getKilos()
        {
            if (dtPro==null)
                return null;
            return dtPro.getKilos();
        }
       
        public Double getImporte()
        {
            if (dtPro==null)
                return null;
            return dtPro.getImporte();
        }
        public Integer getUnidades()
        {
             if (dtPro==null)
                return null;
            return dtPro.getUnidades();
        }
        public Double getImpGanan()
        {
            if (dtPro==null)
                return null;
            return dtPro.getImpGanan();
        }
        public Double getGananciaKilo()
        {
           if (dtPro==null)
                return null;
           return dtPro.getGananciaKilo();
        }
       
    }
class MyRenderer extends DefaultTreeCellRenderer {
    Icon grupoIcon=Iconos.getImageIcon("view_tree");
    Icon familiaIcon=Iconos.getImageIcon("unload");
    Icon productoIcon=Iconos.getImageIcon("anjelica");

    public MyRenderer() {

    }

    @Override
    public Component getTreeCellRendererComponent(
                        JTree tree,
                        Object value,
                        boolean sel,
                        boolean expanded,
                        boolean leaf,
                        int row,
                        boolean hasFocus) {

        super.getTreeCellRendererComponent(
                        tree, value, sel,
                        expanded, leaf, row,
                        hasFocus);
       char tipo=getTipo(value);
       DefaultMutableTreeNode node =
                (DefaultMutableTreeNode)value;
//        System.out.println("tipo: "+tipo);
       if (tipo=='G')
       {
            setIcon(grupoIcon);
            return this;
        }
        if (tipo=='F')
        {
                setIcon(familiaIcon);
                return this;
        }
       if (tipo=='A')
       {
              setIcon(productoIcon);
              return this;
       }
       setToolTipText(null);
        return this;
    }

    protected char getTipo(Object value) {
        DefaultMutableTreeNode node =
                (DefaultMutableTreeNode)value;

        if (node.getUserObject() instanceof   NodoInfo)
        {
            NodoInfo nodeInfo =
                    (NodoInfo)(node.getUserObject());
            setToolTipText((nodeInfo.getKilos()==null?"":" Kilos: "+Formatear.format(nodeInfo.getKilos(),"----,--9.9"))+
                  (nodeInfo.getImporte()==null?"":" Imp: "+Formatear.format(nodeInfo.getImporte(),"----,--9")));
            return nodeInfo.getTipo();
        }
        else
              return ' ';

    }
}
    /** Creates new form ArbolProdPanel */
    public ArbolProdPanel() {
        initComponents();
    }

    public void iniciar(DatosTabla dt)
    {
      this.dt=dt;
      ct=dt.getConexion();
      Brefrescar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Component c[]=Pvisual.getComponents();
                for (int n=0;n<c.length;n++)
                    Pvisual.remove(c[n]);
                for (int n=0;n<vtree.size();n++)
                {
                    DefaultTreeModel model = (DefaultTreeModel) ((JTree) vtree.get(n)).getModel();
                    model.setRoot(null);
                }
                vtree.clear();
                try {
                    verDatosArbol();
                }  catch (SQLException k)
                {
                    SystemOut.print(k);
                }
            }
        });
    }
    public void verDatosArbol() throws SQLException
    {
            Component c[]=Pvisual.getComponents();
            for (int n=0;n<c.length;n++)
                Pvisual.remove(c[n]);
            for (int n=0;n<vtree.size();n++)
            {
                DefaultTreeModel model = (DefaultTreeModel) ((JTree) vtree.get(n)).getModel();
                model.setRoot(null);
            }
            vtree.clear();
           s="select * from subempresa where sbe_tipo='A' order by sbe_codi ";
           if (!dt.select(s))
           {
               mensajes.mensajeAviso("No encontradas definiciones de secciones para articulos");
               return;
           }

           CTabbedPane TPaneArbol=new CTabbedPane();
           Pvisual.add(TPaneArbol, BorderLayout.CENTER);
           do
           {
               CPanel panel=new CPanel();
               panel.setLayout(new BorderLayout());
               TPaneArbol.addTab(dt.getString("sbe_nomb"), panel);
               JScrollPane js=new  JScrollPane();
               panel.add(js,BorderLayout.CENTER);
               final JTree jtree=new JTree();
               ToolTipManager.sharedInstance().registerComponent(jtree);
               jtree.addTreeSelectionListener(new TreeSelectionListener() {
                public void valueChanged(TreeSelectionEvent e) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                       jtree.getLastSelectedPathComponent();
                    if (node==null)
                        return;
                    NodoInfo nodoInfo = (NodoInfo) node.getUserObject();
                    if (nodoInfo.getKilos()==null)
                    {
                        Ppie.resetTexto();
                        return;
                    }
                    kilosE.setValorDec(nodoInfo.getKilos());
                    importeE.setValorDec(nodoInfo.getImporte());
                    unidadesE.setValorDec(nodoInfo.getUnidades());
                    impGanaE.setValorDec(nodoInfo.getImpGanan());
                    ganKiloE.setValorDec(nodoInfo.getGananciaKilo());
                }
               });

               jtree.setCellRenderer(new MyRenderer());
               vtree.add(jtree);
               js.setViewportView(jtree);
               NodoInfo nodoInfo=new NodoInfo(dt.getString("sbe_nomb"),'R');
               top= new DefaultMutableTreeNode(nodoInfo);

               DefaultTreeModel model = (DefaultTreeModel)jtree.getModel();
               model.setRoot(top);
               DatosProd dtPro=verDatosArbol( 0,top," 1=1 ",dt.getInt("sbe_codi"));
               if (htArt!=null)
                nodoInfo.setValores(dtPro);
               int i=0;

               for (i = 0; i < jtree.getRowCount(); i++)
               {
//                   System.out.println("i: "+i+" RowCount: "+jtree.getRowCount()+
//                           " PC: "+jtree.getPathForRow(i).getPathCount()+
//                           " Componente: "+jtree.getPathForRow(i).getLastPathComponent());
                   if ( ((NodoInfo) (( DefaultMutableTreeNode) jtree.getPathForRow(i).getLastPathComponent()).getUserObject()).getTipo()!='F')
                     jtree.expandRow(i);
               }
           } while (dt.next());
      
    }

    int contarRamasArbol(int papa, String grupos,int sbeCodi) throws SQLException
    {
//        System.out.println("Entro a contarRamasArbol. Papa:  "+papa/*+" Grupo: "+
//                   grupos*/+ " Seccion: "+sbeCodi);
        Statement st=ct.crearEstamento();
        ResultSet rs ;
        int nHijos;
        s="SELECT v.*,agp_nomb FROM grufamvis as v,v_agupro as g "
                + " where  gfv_padre= "+papa
                +" and g.agr_codi= v.agr_codi "
                + " order by gfv_orden ";

        rs=st.executeQuery(s);
        nHijos=0;
        if (! rs.next())
        {
           s="SELECT f.fpr_codi,f.fpr_nomb FROM v_famipro as f,v_articulo as a where "+
                grupos+
                " and f.fpr_codi = a.fam_codi "+
                " and a.sbe_codi = "+sbeCodi+
                " and a.pro_activ!=0 "+
                " group by fpr_codi,fpr_nomb "+
                " order by f.fpr_codi";
            rs=st.executeQuery(s);
            if (rs.next())
                nHijos++;
            st.close();
            return nHijos;
        }
       

        String excepto="";
        do {
//            System.out.println("gfv_codi: "+rs.getInt("gfv_codi")/*+" Grupo: "+
//                   grupos+" and fpr_codi in (select fpr_codi from grufampro "+
//                           " where agr_codi="+rs.getInt("agr_codi")+")"*/);

            int nRamas=contarRamasArbol(rs.getInt("gfv_codi"),
                    grupos+" and fpr_codi in (select fpr_codi from grufampro "+
                           " where agr_codi="+rs.getInt("agr_codi")+")",
                    sbeCodi);
          
            if (nRamas>0)
            {
                excepto+=" and fpr_codi not in (select fpr_codi from grufampro where agr_codi="+
                        rs.getInt("agr_codi")+")";
                nHijos++;
            }
        } while (rs.next());
//         System.out.println("antess nHijos: "+nHijos);
        if (nHijos>1)
            return nHijos;
        s="SELECT f.fpr_codi,f.fpr_nomb FROM v_famipro as f,v_articulo as a where "+
                grupos+excepto+
                " and f.fpr_codi = a.fam_codi "+
                " and a.sbe_codi = "+sbeCodi+
                " and a.pro_activ!=0 "+
                " group by fpr_codi,fpr_nomb "+
                " order by f.fpr_codi";
        rs=st.executeQuery(s);
        if (rs.next())
           nHijos++;
        st.close();
//        System.out.println("nHijos: "+nHijos);
        return nHijos;
    }
    /**
     * Muestra los datos de los grupos de familias
     * @param papa
     * @param nodo
     * @param grupos
     * @param sbeCodi
     * @return DatosProd
     * @throws SQLException
     */
    private DatosProd verDatosArbol(int papa, DefaultMutableTreeNode nodo,String grupos,int sbeCodi) throws SQLException
    {
//         System.out.println("Entro a verDatosArbol. Papa:  "+papa+" Grupo: "+
//                   grupos+" Seccion: "+sbeCodi);
        Statement st=ct.crearEstamento();
        ResultSet rs ;
        int nHijos;
        boolean swJuntar=false;
        if (htArt!=null)
        {
            nHijos=contarRamasArbol(papa, grupos, sbeCodi);
//            System.out.println("---------------------\nN. Hijos: "+nHijos);
            swJuntar=nHijos==1;
        }

        s="SELECT v.*,agp_nomb FROM grufamvis as v,v_agupro as g "
                + " where  gfv_padre= "+papa
                +" and g.agr_codi= v.agr_codi "
                + " order by gfv_orden ";

        rs=st.executeQuery(s);
        if (! rs.next())
        {
            st.close();
            return verFamilias(grupos,nodo,sbeCodi);
        }
        nHijos=0;
        DatosProd dtGru=new DatosProd(0);
        String excepto="";
        DatosProd dtPro;
        NodoInfo nodoInfo;
        DefaultMutableTreeNode df;
        do {

            if (swJuntar)
            {
               df=nodo;
               nodoInfo=(NodoInfo) df.getUserObject();
              // nodoInfo.setGrupo(nodoInfo.getGrupo()+" - "+rs.getString("agp_nomb"));
               dtPro=verDatosArbol(rs.getInt("gfv_codi"),
                    df,
                    grupos+" and fpr_codi in (select fpr_codi from grufampro "+
                           " where agr_codi="+rs.getInt("agr_codi")+")",
                    sbeCodi);
            }
            else
            {
                nodoInfo=new NodoInfo(rs.getString("agp_nomb")
                    +(htArt==null?" ("+rs.getInt("agr_codi")+")":""),
                    'G');
                df=new DefaultMutableTreeNode(nodoInfo);
                dtPro=verDatosArbol(rs.getInt("gfv_codi"),
                    df,
                    grupos+" and fpr_codi in (select fpr_codi from grufampro "+
                           " where agr_codi="+rs.getInt("agr_codi")+")",
                    sbeCodi);
            }
            if (dtPro!=null)
            {
                if (htArt!=null)
                {
                    dtGru.add(dtPro);
                    nodoInfo.setValores(dtPro);
                }
                if (! swJuntar)
                    nodo.add(df);
                else
                    nodoInfo.setGrupo(nodoInfo.getGrupo()+", "+rs.getString("agp_nomb"));
                excepto+=" and fpr_codi not in (select fpr_codi from grufampro where agr_codi="+rs.getInt("agr_codi")+")";
                nHijos++;
            }
        } while (rs.next());
        dtPro=verFamilias(grupos+excepto,nodo,sbeCodi);
        if (dtPro!=null)
        {      
           nHijos++;
           dtGru.add(dtPro);
        }
        st.close();
        if (nHijos>0)
            return dtGru;
        return null;
    }
    private DatosProd verFamilias(String grupos, DefaultMutableTreeNode nodo,int sbeCodi) throws SQLException
    {
         s="SELECT f.fpr_codi,f.fpr_nomb FROM v_famipro as f,v_articulo as a where "+
                grupos+
                " and f.fpr_codi = a.fam_codi "+
                " and a.sbe_codi = "+sbeCodi+
                " and a.pro_activ!=0 "+
                " group by fpr_codi,fpr_nomb "+
                " order by f.fpr_codi";
         Statement st=ct.crearEstamento();
         ResultSet rs=st.executeQuery(s);
         if (! rs.next())
         {
                st.close();
                return null;
         }
         int nHijos=0;
         DatosProd dtFam=new DatosProd(0);
         do
         {
             NodoInfo nodoInfo=new NodoInfo(
                      rs.getString("fpr_nomb")+
                      (htArt==null?" ("+rs.getString("fpr_codi")+")":"")
                      ,'F');
             DefaultMutableTreeNode df=new DefaultMutableTreeNode(nodoInfo);
             DatosProd dtPro=verProductos(rs.getInt("fpr_codi"),df,sbeCodi);
             if (dtPro!=null)
             {
                if (htArt!=null)
                {
                    dtFam.add(dtPro);
                    nodoInfo.setValores(dtPro);
                }
                nodo.add(df);
                nHijos++;
             }
         } while (rs.next());
         st.close();
         if (nHijos>0)
             return dtFam;
         return null;
    }
    private DatosProd verProductos(int familia, DefaultMutableTreeNode nodo,int sbeCodi) throws SQLException
    {
         s="SELECT pro_nomcor,pro_codi,pro_nomb FROM v_articulo  "+
                " where fam_codi = "+familia+
                " and sbe_codi ="+sbeCodi+
                " and pro_activ!=0"+
                " order by pro_codi";
         Statement st=ct.crearEstamento();
         ResultSet rs=st.executeQuery(s);
         if (! rs.next())
         {
                st.close();
                return null;
         }
         int nHijos=0;
         DatosProd dtPro=new DatosProd(0);
         do
         {
             NodoInfo nodoInfo= new  NodoInfo(rs.getString("pro_codi") +
                     " "+rs.getString("pro_nomb"),'A');
             if (htArt!=null)
             {
                if ((valores=(DatosProd) htArt.get(rs.getInt("pro_codi")))!=null)
                {
                  dtPro.add(valores);
                  nodoInfo.setValores(valores);
                }
                else
                  continue;
             }
             DefaultMutableTreeNode df=new DefaultMutableTreeNode(nodoInfo);
             
             nodo.add(df);
             nHijos++;
         } while (rs.next());
         st.close();
         if (nHijos==0)
             return null;
         return dtPro;
    }
     public void setValoresArticulos(Hashtable ht)
     {
        htArt=ht;
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

        Pvisual = new gnu.chu.controles.CPanel();
        Ppie = new gnu.chu.controles.CPanel();
        Brefrescar = new gnu.chu.controles.CButton(Iconos.getImageIcon("ajustar"));
        cLabel1 = new gnu.chu.controles.CLabel();
        kilosE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---,--9.9");
        cLabel2 = new gnu.chu.controles.CLabel();
        importeE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---,--9.9");
        cLabel3 = new gnu.chu.controles.CLabel();
        unidadesE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---,--9");
        cLabel4 = new gnu.chu.controles.CLabel();
        impGanaE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---,--9.9");
        ganKiloE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--9.99");
        cLabel5 = new gnu.chu.controles.CLabel();

        setLayout(new java.awt.GridBagLayout());

        Pvisual.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        Pvisual.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(Pvisual, gridBagConstraints);

        Ppie.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Ppie.setMaximumSize(new java.awt.Dimension(544, 56));
        Ppie.setMinimumSize(new java.awt.Dimension(544, 56));
        Ppie.setLayout(null);

        Brefrescar.setText("Refrescar");
        Brefrescar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BrefrescarActionPerformed(evt);
            }
        });
        Ppie.add(Brefrescar);
        Brefrescar.setBounds(452, 2, 90, 32);

        cLabel1.setText("Kilos");
        Ppie.add(cLabel1);
        cLabel1.setBounds(2, 3, 27, 15);

        kilosE.setEnabled(false);
        Ppie.add(kilosE);
        kilosE.setBounds(47, 2, 70, 17);

        cLabel2.setText("Importe");
        Ppie.add(cLabel2);
        cLabel2.setBounds(135, 3, 44, 15);

        importeE.setEnabled(false);
        Ppie.add(importeE);
        importeE.setBounds(189, 2, 70, 17);

        cLabel3.setText("Unidades");
        Ppie.add(cLabel3);
        cLabel3.setBounds(2, 26, 51, 15);

        unidadesE.setEnabled(false);
        Ppie.add(unidadesE);
        unidadesE.setBounds(57, 25, 59, 17);

        cLabel4.setText("Ganancia ");
        Ppie.add(cLabel4);
        cLabel4.setBounds(134, 26, 52, 15);

        impGanaE.setEnabled(false);
        Ppie.add(impGanaE);
        impGanaE.setBounds(190, 25, 70, 17);

        ganKiloE.setEnabled(false);
        Ppie.add(ganKiloE);
        ganKiloE.setBounds(264, 25, 36, 17);

        cLabel5.setText("€/Kg");
        Ppie.add(cLabel5);
        cLabel5.setBounds(304, 26, 23, 15);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        add(Ppie, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void BrefrescarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BrefrescarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BrefrescarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Brefrescar;
    private gnu.chu.controles.CPanel Ppie;
    private gnu.chu.controles.CPanel Pvisual;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CTextField ganKiloE;
    private gnu.chu.controles.CTextField impGanaE;
    private gnu.chu.controles.CTextField importeE;
    private gnu.chu.controles.CTextField kilosE;
    private gnu.chu.controles.CTextField unidadesE;
    // End of variables declaration//GEN-END:variables

}
