package gnu.chu.winayu;

import gnu.chu.camposdb.empPanel;
import gnu.chu.camposdb.proPanel;
import gnu.chu.controles.CLabel;
import gnu.chu.controles.CPanel;
import gnu.chu.controles.Cgrid;
import gnu.chu.controles.StatusBar;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.ventana;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;
import javax.swing.event.InternalFrameEvent;
/**
 *
 * <p>Título: ayuLote</p>
 * <p>Descripcion: Pantalla de Ayuda de Lotes disponibles
 * Saca los lotes disponibles sobre los productos mandados en cargaGrid
 * </p>
 * <p>Copyright: Copyright (c) 2005-2012
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
 * <p>Empresa: MISL</p>
 * @author chuchiP
 * @version 1.0
 */

public class ayuLote extends ventana
{
  String empAccesos;
  boolean verBloqueados=false;
  int proCodi;
  public int rowAct;
  CPanel Pprinc = new CPanel();
  CPanel Pprod = new CPanel();
  CLabel cLabel1 = new CLabel();
  proPanel pro_codiE = new proPanel();
  public Cgrid jt = new Cgrid(8);
  public static int JT_EMP=0;
  public static int JT_EJE=1;
  public static int JT_SER=2;
  public static int JT_LOTE=3;
  public static int JT_IND=4;
  public static int JT_PESO=5;
  public static int JT_NUMUNI=6;
  public static int JT_BLOCK=7;
  public boolean consulta=false;
  GridBagLayout gridBagLayout1 = new GridBagLayout();

  public ayuLote(EntornoUsuario e, JLayeredPane fr, DatosTabla dt,int proCodi)
  {
    setTitulo("Ayuda Lotes Disponibles");
    this.proCodi=proCodi;
    EU = e;
    eje = false;
    vl=fr;
    if (dt != null)
      dtCon1 = dt;
    try
    {
      jbInit();
    }
    catch (Exception k)
    {
      fatalError("constructor: ", k);
      setErrorInit(true);
    }
  }

  private void jbInit() throws Exception
  {
    statusBar = new StatusBar(this);
    iniciarFrame();
    this.setSize(new Dimension(406, 316));
    this.setIconifiable(false);

    if (dtCon1 == null)
      conecta();
    Pprinc.setLayout(gridBagLayout1);
    Pprod.setBorder(BorderFactory.createRaisedBevelBorder());
    Pprod.setMaximumSize(new Dimension(395, 27));
    Pprod.setMinimumSize(new Dimension(395, 27));
    Pprod.setPreferredSize(new Dimension(395, 27));
    Pprod.setLayout(null);
    cLabel1.setText("Producto");
    cLabel1.setBounds(new Rectangle(3, 2, 53, 18));
    pro_codiE.setBounds(new Rectangle(58, 4, 333, 17));
    pro_codiE.iniciar(dtCon1,this,vl,EU);
    pro_codiE.setEnabled(false);

    jt.setMaximumSize(new Dimension(393, 269));
    jt.setMinimumSize(new Dimension(393, 269));
    jt.setPreferredSize(new Dimension(393, 269));
    ArrayList v= new ArrayList();
    v.add("Emp"); // 0
    v.add("Ejer"); // 1
    v.add("Ser"); // 2
    v.add("Lote"); // 3
    v.add("Ind"); // 4
    v.add("Peso"); // 5
    v.add("N.Unid"); // 6
    v.add("Blo"); // 7
    jt.setCabecera(v);
    jt.setAnchoColumna(new int[]{40,60,30,60,60,90,60,30});
    jt.setAlinearColumna(new int[]{2,2,1,2,2,2,2,1});
    jt.setFormatoColumna(5,"---,--9.99");
    jt.setFormatoColumna(6,"--9");
    jt.setFormatoColumna(7,"B-");
    jt.setConfigurar("gnu.chu.winayu.ayuLote",EU,dtCon1);
    jt.setAjustarGrid(true);
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
    Pprinc.add(Pprod,   new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(3, 0, 0, 1), 0, 0));
    Pprod.add(pro_codiE, null);
    Pprod.add(cLabel1, null);
    Pprinc.add(jt,  new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 2, 1), 0, 0));
  }
    @Override
  public void iniciarVentana() throws Exception
  {
    empAccesos= empPanel.getStringAccesos(dtCon1,EU.usuario);
    activarEventos();
    jt.cuadrarGrid();
  }
  public void setVerBloqueados(boolean verBloqueados)
  {
      this.verBloqueados=verBloqueados;
  }
  void activarEventos()
  {

    jt.tableView.addMouseListener(new MouseAdapter()
    {
       @Override
      public void mouseClicked(MouseEvent m)
      {
        if (m.getClickCount() > 1 && jt.isVacio() == false)
        {
          Baceptar_actionPerformed();
        }
      }

    });

    jt.tableView.addKeyListener(new KeyAdapter()
    {
            @Override
      public void keyPressed(KeyEvent e)
      {
        if ((e.getKeyCode() == KeyEvent.VK_INSERT || e.getKeyCode() == KeyEvent.VK_ENTER) && jt.isVacio() == false)
          Baceptar_actionPerformed();
      }

    });
  }

  void Baceptar_actionPerformed()
  {
    if (jt.tableView.getSelectedRow() < 0)
    {
      mensaje("SELECCIONE UNA COLUMNA EN EL GRID");
      jt.requestFocus();
      return;
    }

    // Editar Columna Activa.
    rowAct=jt.getSelectedRow();
    consulta = true;
    matar(false);
  }
  public void cargaGrid(String prCod)
  {
    cargaGrid(prCod,0);
  }
  public void cargaGrid(String prCod,int almCodi)
  {
    if (prCod!=null)
      proCodi=Integer.parseInt(prCod.trim());
      consulta=false;
    try
    {
      pro_codiE.setText(""+proCodi);
      
      String s="SELECT emp_codi,eje_nume,pro_serie,pro_nupar,pro_numind,stp_kilact,stp_unact,stk_block "+
          " FROM v_stkpart WHERE pro_codi = "+proCodi+
          " AND emp_codi = "+EU.em_cod+
          (almCodi!=0?" and alm_codi = "+almCodi:"")+
          (verBloqueados?"":" and stk_block= 0")+
          (pro_codiE.hasControlExist()?" and stp_kilact > 0 and stp_unact > 0 ":"")+
          " and pro_nupar > 0 "+
//          " and emp_codi in ("+empAccesos+")"+
          " ORDER BY emp_codi, eje_nume desc,pro_serie,pro_nupar desc,pro_numind";

      dtCon1.select(s);
      jt.setDatos(dtCon1);
      javax.swing.SwingUtilities.invokeLater(new Thread()
      {
                @Override
        public void run()
        {
          jt.requestFocusInicio();
        }
      });
      jt.requestFocusInicio();
    } catch (Exception k)
    {
      Error("Error al Cargar Grid",k);
    }
  }
    @Override
  public void matar(boolean cerrarConexion)
  {
    try {
      setVisible(false);
    }
    catch (Exception k) {}
    processEvent(new InternalFrameEvent(this,
                                        InternalFrameEvent.
                                    INTERNAL_FRAME_CLOSING));
  }
}
