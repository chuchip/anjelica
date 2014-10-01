package gnu.chu.anjelica.almacen;

import gnu.chu.utilidades.*;
import gnu.chu.Menu.*;
import gnu.chu.anjelica.compras.MantAlbCom;
import gnu.chu.anjelica.compras.MantAlbComCarne;
import gnu.chu.anjelica.compras.MantAlbComPlanta;
import gnu.chu.anjelica.despiece.MantDesp;
import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.controles.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.tree.TreePath;
import javax.swing.plaf.metal.MetalIconFactory;
import javax.swing.tree.DefaultTreeCellRenderer;
import gnu.chu.camposdb.*;
import gnu.chu.interfaces.ejecutable;
import java.sql.*;
import java.awt.event.*; 
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * <p>Título: coarbtraz</p>
 * <p>Descripción: Consulta del arbol de trazabilidad
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
 * <p>Empresa: MISL</p>
 *
 */
public class coarbtraz extends ventana
{
  
  TreeSet liDes=new TreeSet();
  TreeSet liCom=new TreeSet();
//  String serie;
//  int proCodi,  empLot,ejeLot,  proLote, proIndi,almCodi,empCodi;
  DefaultTreeModel treeModel;
  CPanel Pprinc = new CPanel();
  CPanel Pcondic = new CPanel();
  CPanel Parbol = new CPanel();
  JTree arbol;
  BorderLayout borderLayout2 = new BorderLayout();
  Cgrid jtCom = new Cgrid(1);
  CTextField pro_nuparE = new CTextField(Types.DECIMAL,"####9");
  CTextField pro_serieE = new CTextField(Types.CHAR,"X",1);
  proPanel pro_codiE = new proPanel();
  CTextField pro_numindE = new CTextField(Types.DECIMAL,"###9");
  CLabel cLabel4 = new CLabel();
  CLabel cLabel1 = new CLabel();
  CLabel cLabel2 = new CLabel();
  CTextField eje_numeE = new CTextField(Types.DECIMAL,"###9");
  CLabel cLabel5 = new CLabel();
  CLabel cLabel3 = new CLabel();
  CButton Baceptar = new CButton("Aceptar (F4)",Iconos.getImageIcon("check"));
  ToolTipTreeNode top;
//  CLabel cLabel6 = new CLabel();
//  CTextField emp_codiE = new CTextField(Types.DECIMAL,"#9");
  Cgrid jtDes = new Cgrid(1);
  GridBagLayout gridBagLayout1 = new GridBagLayout();

   public coarbtraz(EntornoUsuario eu, Principal p)
   {
     EU = eu;
     vl = p.panel1;
     jf = p;
     eje = true;

     try
     {

       setTitulo("Consulta Arbol de Trazabilidad");
       if (jf.gestor.apuntar(this))
         jbInit();
       else
         setErrorInit(true);
     }
     catch (Exception e)
     {
      ErrorInit(e);
     }
   }

   public coarbtraz(gnu.chu.anjelica.menu p, EntornoUsuario eu)
   {
     EU = eu;
     vl = p.getLayeredPane();
     eje = false;

     try
     {
       setTitulo("Consulta Arbol de Trazabilidad");

       jbInit();
     }
     catch (Exception e)
     {
        ErrorInit(e);
     }
   }

   private void jbInit() throws Exception
   {

     iniciarFrame();
     this.setSize(new Dimension(576, 515));
     this.setVersion("2012-01-18");
     statusBar = new StatusBar(this);
     conecta();

     jtCom.setMaximumSize(new Dimension(144, 160));
    jtCom.setMinimumSize(new Dimension(144, 160));
    jtCom.setPreferredSize(new Dimension(144, 160));
    jtCom.setBuscarVisible(false);
    pro_serieE.setAutoNext(true);
    pro_serieE.setMayusc(true);
    pro_serieE.setBounds(new Rectangle(128, 25, 27, 17));
    cLabel4.setText("Partida");
    cLabel4.setBounds(new Rectangle(170, 25, 49, 17));
    cLabel1.setRequestFocusEnabled(true);
    cLabel1.setText("Producto");
    cLabel1.setBounds(new Rectangle(123, 4, 54, 18));
    cLabel2.setText("Ejerc.");
    cLabel2.setBounds(new Rectangle(5, 25, 37, 17));
    cLabel5.setText("Individuo");
    cLabel5.setBounds(new Rectangle(281, 25, 60, 17));
    cLabel3.setText("Serie");
    cLabel3.setBounds(new Rectangle(94, 25, 37, 17));
//    cLabel6.setText("Empresa");
//    cLabel6.setBounds(new Rectangle(6, 4, 57, 17));
    Baceptar.setBounds(new Rectangle(400, 24, 139, 24));
    Baceptar.setMargin(new Insets(0, 0, 0, 0));
    pro_numindE.setBounds(new Rectangle(335, 25, 37, 17));
    pro_nuparE.setBounds(new Rectangle(215, 25, 56, 17));
    eje_numeE.setBounds(new Rectangle(44, 25, 43, 17));
    pro_codiE.setBounds(new Rectangle(179, 4, 357, 18));
//    emp_codiE.setBounds(new Rectangle(57, 3, 28, 17));

    Pcondic.setMaximumSize(new Dimension(548, 55));
    Pcondic.setMinimumSize(new Dimension(548, 55));
    Pcondic.setPreferredSize(new Dimension(548, 55));
    Pprinc.setInputVerifier(null);
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);
     Pprinc.setLayout(gridBagLayout1);
     Pcondic.setBorder(BorderFactory.createRaisedBevelBorder());
     Pcondic.setLayout(null);
     Parbol.setBorder(BorderFactory.createLoweredBevelBorder());
     Parbol.setLayout(borderLayout2);
     this.getContentPane().add(Pprinc, BorderLayout.CENTER);


    top =   new ToolTipTreeNode("","tooltip",Iconos.getImageIcon("view_tree"));
    treeModel = new DefaultTreeModel(top);
//    treeModel.addTreeModelListener(new MyTreeModelListener());
     arbol = new JTree(treeModel)
     {
            @Override
         public String getToolTipText(MouseEvent evt)
         {
           if (getRowForLocation(evt.getX(), evt.getY()) == -1)
             return null;
           TreePath curPath = getPathForLocation(evt.getX(), evt.getY());
           return ( (ToolTipTreeNode) curPath.getLastPathComponent()).getToolTipText();
         }
      };
      top.setUserObject("* SIN PRODUCTO *");
      arbol.setToolTipText("prueba");
      arbol.setEditable(true);
      arbol.getSelectionModel().setSelectionMode
               (TreeSelectionModel.SINGLE_TREE_SELECTION);
       arbol.setShowsRootHandles(true);

//      arbol.setExpandsSelectedPaths(true);
//      arbol.putClientProperty("JTree.icons", makeIcons());
     arbol.setCellRenderer(new IconNodeRenderer());
     JScrollPane treeView = new JScrollPane(arbol);
     treeView.setMaximumSize(new Dimension(388, 335));
     treeView.setMinimumSize(new Dimension(388, 335));
     treeView.setPreferredSize(new Dimension(388, 335));

     Pprinc.add(treeView,   new GridBagConstraints(0, 1, 1, 2, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
     Pprinc.add(jtCom,     new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(4, 0, 0, 0), 0, 0));
    Pprinc.add(Pcondic,    new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 2, 0, 0), 0, 0));
    Pcondic.add(Baceptar, null);
    Pcondic.add(pro_codiE, null);
//    Pcondic.add(cLabel6, null);
//    Pcondic.add(emp_codiE, null);
    Pcondic.add(pro_nuparE, null);
    Pcondic.add(cLabel2, null);
    Pcondic.add(eje_numeE, null);
    Pcondic.add(cLabel3, null);
    Pcondic.add(pro_serieE, null);
    Pcondic.add(cLabel4, null);
    Pcondic.add(cLabel5, null);
    Pcondic.add(pro_numindE, null);
    Pcondic.add(cLabel1, null);
    Pprinc.add(jtDes,    new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(4, 0, 0, 0), 0, 0));
    confGrid();
   }

    @Override
   public void iniciarVentana() throws Exception
   {
       
     Pcondic.setDefButton(Baceptar);
     Pcondic.setButton(KeyEvent.VK_F4, Baceptar);
//     emp_codiE.setValorInt(1);
     eje_numeE.setValorInt(EU.ejercicio);
     pro_serieE.setText("A");
     pro_codiE.iniciar(dtStat, this, vl, EU);
     activarEventos();
   }

   private void activarEventos()
   {
     Baceptar.addActionListener(new ActionListener()
     {
            @Override
       public void actionPerformed(ActionEvent e)
       {
         Baceptar_actionPerformed();
       }
     });
     jtCom.addMouseListener(new MouseAdapter() {
            @Override
          public void mouseClicked(MouseEvent e) {
                try
                {
                    if (e.getClickCount()<2 || jtCom.isVacio())
                        return;
                    irCompra(jtCom.getValString(0));
                } catch (SQLException ex)
                {
                    Error("Error al ir a compra",ex);
                }
          }
     });
      jtDes.addMouseListener(new MouseAdapter() {
            @Override
          public void mouseClicked(MouseEvent e) {
              if (e.getClickCount()<2 || jtDes.isVacio())
                  return;
              irDespiece(jtDes.getValString(0));
          }
     });
   }
   
   void irDespiece(String coment)
   {
    ejecutable prog;
    if ((prog=jf.gestor.getProceso(MantDesp.getNombreClase()))==null)
      return;
    MantDesp cm=(MantDesp) prog;
    if (cm.inTransation())
    {
        msgBox("Mantenimiento de Despieces esta en transacion. Imposible ejecutar consulta");
        return;
    }
    cm.PADQuery();
    cm.setEjeNume(Integer.parseInt(coment.substring(0,coment.indexOf("-"))));
    cm.setDeoCodi(coment.substring(coment.indexOf("-")+1));
    cm.ej_query();
    jf.gestor.ir(cm);
   }
   void irCompra(String coment) throws SQLException
   {
       ejecutable prog;
       if (pdconfig.getTipoEmpresa(EU.em_cod, dtStat)==pdconfig.TIPOEMP_PLANTACION)      
        {
           if ((prog = jf.gestor.getProceso(MantAlbComPlanta.getNombreClase())) == null)
              return;
        }
        else
        {
           if ((prog = jf.gestor.getProceso(MantAlbComCarne.getNombreClase())) == null)
              return;
        }

       MantAlbCom cm = (MantAlbCom) prog;
    
       if (cm.inTransation())
       {
            msgBox("Mantenimiento Albaran de Compras esta en transacion. Imposible ejecutar consulta");
            return;
       }
       cm.PADQuery();
       int n=coment.indexOf(":")+1;
       cm.setEjeNume(Integer.parseInt(coment.substring(0,coment.indexOf("-"))));
       cm.setAccSerie(coment.substring(coment.indexOf("-")+1,n-1));
       cm.setAccCodi(coment.substring(n));
       cm.ej_query();
       jf.gestor.ir(cm);
   }
   void  confGrid()
   {
     Vector v=new Vector();
     jtDes.setMaximumSize(new Dimension(144, 160));
    jtDes.setMinimumSize(new Dimension(144, 160));
    jtDes.setPreferredSize(new Dimension(144, 160));
    v.add("Alb.Compra");
     jtCom.setCabecera(v);
     jtCom.setAjustarGrid(true);

     Vector v1 = new Vector();
     v1.add("Despiece");
     jtDes.setCabecera(v1);
     jtDes.setAjustarGrid(true);
   }
   void Baceptar_actionPerformed()
   {
     try {

     if (! pro_codiE.controla(true))
     {
       mensajeErr(pro_codiE.getMsgError());
       return;
     }
//     if (emp_codiE.getValorInt()==0)
//     {
//       mensajeErr("Introduzca Empresa");
//       return;
//     }
     if (eje_numeE.getValorInt() == 0)
     {
       mensajeErr("Introduzca Ejercicio");
       return;
     }
     if (pro_nuparE.getValorInt() == 0)
     {
       mensajeErr("Introduzca Numero de Partido");
       return;
     }
     if (pro_numindE.getValorInt() == 0)
     {
       mensajeErr("Introduzca Numero de Individuo");
       return;
     }
     if (pro_serieE.isNull())
     {
       mensajeErr("Intoduzca Serie");
       return;
     }


     top.removeAllChildren();
     treeModel.reload();
     liCom.clear();
     liDes.clear();
     arbol.setExpandsSelectedPaths(true);
     int proCodi = pro_codiE.getValorInt();
//     int empLote = emp_codiE.getValorInt();
     int ejeLote = eje_numeE.getValorInt();
     int proLote = pro_nuparE.getValorInt();
     int indLote = pro_numindE.getValorInt();
     String serLote = pro_serieE.getText();

     int almCodi = 0;



      top.setToolTipText("Producto: " + proCodi + " Lote: " + ejeLote + ":" +
                         serLote + "-" + proLote +
                         "-" + indLote);
      top.setUserObject(proCodi + " Lote: " +  ejeLote + ":" +
                         serLote + "-" + proLote +
                         "-" + indLote);
      createNodoDesp(top, proCodi, ejeLote, proLote, indLote, serLote, almCodi);

      for (int n=0;n<= arbol.getRowCount();n++)
      {
        arbol.expandRow(n);
//        arbol.remove(n);
      }
      jtCom.removeAllDatos();
      Iterator it = liCom.iterator();
      Object obj;
      while (it.hasNext())
      {
        obj=it.next();
        ArrayList v=new ArrayList();
        v.add(obj.toString());
        jtCom.addLinea(v);

      }
      jtCom.requestFocusInicio();

      jtDes.removeAllDatos();
      it = liDes.iterator();
      while (it.hasNext())
      {
        obj = it.next();
        ArrayList v = new ArrayList();
        v.add(obj.toString());
        jtDes.addLinea(v);
      }
      jtDes.requestFocusInicio();

      mensajeErr("Arbol.. cargado");
    } catch (Exception k)
    {
      Error("Error en busqueda Trazabilidad",k);
    }
   }
   private void createNodoDesp(ToolTipTreeNode top,
                               int proCodi,int ejeLot,
                               int proLote,int proIndi,String serie,int almCodi)  throws Exception
   {
     java.sql.Statement st=ct.createStatement();
     java.sql.ResultSet rs;
     java.sql.Statement st1=ct.createStatement();
     java.sql.ResultSet rs1=null;
     String s= "SELECT de.eje_nume,de.deo_codi FROM v_despfin as df,v_despori as de "+
            " WHERE df.pro_codi = " + proCodi +
//            (empCodi==0?"":" AND df.emp_codi = " + empCodi )+
//            " AND df.def_emplot = " + empLot +
            " and df.def_ejelot = " + ejeLot +
            " and df.def_serlot = '" + serie + "'" +
            " and df.pro_lote = " + proLote +
            " and df.pro_numind = " + proIndi+
            (almCodi!=0?" and de.deo_almdes = "+almCodi:"")+
            " and df.eje_nume = de.eje_nume "+
//            " and df.emp_codi = de.emp_codi "+
            " and df.deo_codi = de.deo_codi "+
            " group by  de.eje_nume,de.deo_codi";
    rs=st.executeQuery(s);
    if (rs.next())
    {
       do
      {
        ToolTipTreeNode despiece = new ToolTipTreeNode(new despieceNodo(
            rs.getInt("eje_nume"),rs.getInt("deo_codi")),"Despiece",MetalIconFactory.getInternalFrameDefaultMenuIcon());
        top.add(despiece);
        liDes.add(rs.getInt("eje_nume")+"-"+rs.getInt("deo_codi"));
        s="SELECT * FROM v_despori WHERE eje_nume = "+rs.getInt("eje_nume")+
            " and deo_codi = "+rs.getInt("deo_codi")+
            " order by pro_codi,deo_ejelot,deo_serlot,pro_lote,pro_numind";
        rs1=st1.executeQuery(s);
        while (rs1.next())
        {
          ToolTipTreeNode producto = new ToolTipTreeNode(new productoNodo(rs1.getInt("pro_codi"),
              rs1.getInt("deo_ejelot"),rs1.getString("deo_serlot"),
              rs1.getInt("pro_lote"),rs1.getInt("pro_numind")),"producto",MetalIconFactory.getHorizontalSliderThumbIcon());
          despiece.add(producto);
          createNodoDesp(producto,rs1.getInt("pro_codi"),
                         rs1.getInt("deo_ejelot"),rs1.getInt("pro_lote"),
                         rs1.getInt("pro_numind"),rs1.getString("deo_serlot"),rs1.getInt("deo_almori"));
        }
      } while (rs.next());
    }
    else
    { // Busco la Compra...
        s="SELECT * FROM v_albcompar as p,v_albacoc as c "+
        "  where p.acc_ano = "+ejeLot+
//        " and p.emp_codi = "+empCodi+
        " and p.acc_nume = "+proLote+
        " and p.acc_serie = '"+serie+"'"+
        " and p.acp_numind = "+proIndi+
        " and p.acc_ano = c.acc_ano "+
//       " and p.emp_codi = c.emp_codi "+
       " and p.acc_nume = c.acc_nume "+
       " and p.acc_serie = c.acc_serie ";
       rs=st.executeQuery(s);

       if (rs.next())
       {
         ToolTipTreeNode compra = new ToolTipTreeNode(new CompraNodo(
            "Alb Compra: "+ejeLot+"-"+serie+":"+proLote+
            " Fec.Rec: "+   Formatear.getFecha(rs.getDate("acc_fecrec"),"dd-MM-yyyy")),
            "  Individuo: "+proIndi+ " Fec.Cad: "+Formatear.getFecha(rs.getDate("acp_feccad"),"dd-MM-yyyy"),
                  Iconos.getImageIcon("lanzador"));
         liCom.add(ejeLot+"-"+serie+":"+proLote);
//  debug("  Individuo: "+proIndi);
          top.add(compra);
//          compra.seti
       }
    }
    rs.close();
    st.close();
    if (rs1!=null)
      rs.close();
    st1.close();
   }

}


class despieceNodo
 {
   int ejeNume;
   int deoCodi;

   public despieceNodo(int ejeNume,int deoCodi)
   {
     this.ejeNume=ejeNume;
     this.deoCodi=deoCodi;
   }

    @Override
   public String toString()
   {
     return "Despiece: " + ejeNume + "/" + deoCodi;
   }
 }
 class productoNodo
 {
   int proCodi,ejeLote,proLote,indLote;
   String serLote;

   public productoNodo(int proCodi,int ejeLote,String serLote,int proLote,int indLote)
   {
     this.proCodi=proCodi;
     this.ejeLote=ejeLote;
     
     this.serLote=serLote;
     this.proLote=proLote;
     this.indLote=indLote;
   }

    @Override
   public String toString()
   {
     return "Producto: " + proCodi + " Indiv. "+ejeLote+serLote+"/"+
         proLote+"-"+indLote;
   }

 }
 class CompraNodo
 {
   String coment;

   public CompraNodo(String coment)
   {
     this.coment=coment;
   }

    @Override
   public String toString()
   {
     return "Compra. "+coment;
   }

 }
 class ToolTipTreeNode extends javax.swing.tree.DefaultMutableTreeNode {
   private String toolTipText;
   private Icon icono;
   String iconName;
   public ToolTipTreeNode(String str, String toolTipText) {
    super(str);
    this.toolTipText = toolTipText;
  }

  public ToolTipTreeNode(Object userObject,String toolTipText,Icon icono) {
    super(userObject);
    this.toolTipText = toolTipText;
    this.icono=icono;
   }

   public String getToolTipText() {
     return toolTipText;
   }
   public void setToolTipText(String toolTipText) {
    this.toolTipText = toolTipText;
 }

   public void setIcon(Icon icon) {
    this.icono = icon;
  }

  public Icon getIcon() {
    return icono;
  }

  public String getIconName() {
    if (iconName != null) {
      return iconName;
    } else {
      String str = userObject.toString();
      int index = str.lastIndexOf(".");
      if (index != -1) {
        return str.substring(++index);
      } else {
        return null;
      }
    }
  }

  public void setIconName(String name) {
    iconName = name;
  }

 }


 class IconNodeRenderer extends DefaultTreeCellRenderer {

   public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                 boolean sel, boolean expanded, boolean leaf,
                                                 int row, boolean hasFocus)
   {

     super.getTreeCellRendererComponent(tree, value,
                                        sel, expanded, leaf, row, hasFocus);

     if (value instanceof ToolTipTreeNode)
     {

       Icon icon = ( (ToolTipTreeNode) value).getIcon();

       if (icon != null)
       {
         setIcon(icon);
       }
     }
     return this;
   }
 }

