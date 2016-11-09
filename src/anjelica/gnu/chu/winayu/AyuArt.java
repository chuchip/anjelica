package gnu.chu.winayu;
/**
 * <p>Titulo:   AyuArt </p>
 * <p>Descripción: Ventana de ayuda para buscar productos por nombre/familia/estado.
 * <p>Copyright: (c) 2005-2016
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
*  los términos de la Licencia Pública General de GNU según es publicada por
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
 * 
 */


import gnu.chu.controles.StatusBar;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.ventana;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLayeredPane;


public class AyuArt extends  ventana {
   final int JT_CODART=1;
   final int JT_NOMPRO=2;
   private int rseCodcli=0;
   private String s;
   private boolean chose=false;
   private int proCodi;
   private String proNomb,proCodArt;
    
    public AyuArt() {
        initComponents();
    }
    
  public AyuArt(EntornoUsuario e, JLayeredPane fr, DatosTabla dt)
  {
    setTitulo("Ayuda Articulos");
    
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
    
    this.setIconifiable(false);
  //  this.setResizable(false);
    this.setMaximizable(false);
    if (dtCon1 == null)
      conecta();
    
    initComponents();
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);
    this.setSize(new Dimension(480, 426));
  }
    @Override
  public void iniciarVentana() throws Exception
  {
    pArticVenta.iniciar(dtCon1);
    pArticVenta.setQuery(true);
    activarEventos();
    Pcondic.setDefButton(Bbuscar);
    reset();
  }
  public void reset()
  {
      chose=false;
      Pcondic.resetTexto();
      pArticVenta.resetTexto();
      pro_nombE.requestFocusLater();
  }
   public boolean getChose() {
        return chose;
    }

    public void setChose(boolean chose) {
        this.chose = chose;
    }
    public int getProCodi()    {return proCodi; }
    public String getProRefer()    {return proCodArt; }
    public String getProNomb() {return proNomb;}
    void activarEventos()
    {
        jt.tableView.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent m) {
                if (m.getClickCount() > 1 && jt.isVacio() == false)
                {
                    chose = true;
                    proCodi = jt.getValorInt(0);
                    proCodArt=jt.getValString(JT_CODART);
                    proNomb = jt.getValString(JT_NOMPRO);
                    matar();
                }
            }
        });

        jt.tableView.addKeyListener(new KeyAdapter()
        {

            @Override
            public void keyPressed(KeyEvent e) {
                if ((e.getKeyCode() == KeyEvent.VK_INSERT || e.getKeyCode() == KeyEvent.VK_ENTER) && jt.isVacio() == false)
                {
                    chose = true;
                    proCodi = jt.getValorInt(0);
                    matar();
                }
            }
        });
        Bbuscar.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e) {
                Bbuscar_actionPerformed();
            }
        });
  }
 public void setSeccionCliente(int rseCodcli)
  {
    this.rseCodcli=rseCodcli;
  }

  public int getSeccionCliente()
  {
      return rseCodcli;
  }
  void Bbuscar_actionPerformed()
  {
        try {
            s = "select pro_codi,pro_codart,pro_nomb FROM v_articulo as a left join prodventa as pv " +
                 " on a.pro_codart =pv.pve_codi where 1=1 "+                 
                 (pro_activE.getValor().equals("*")?"":
                  (" and pro_activ "+(pro_activE.getValor().equals("S")?" != 0":"= 0")))+                 
                  (pro_nombE.isNull()?"":" and UPPER(pro_nomb) like '%" + pro_nombE.getText() +"%'")+
                  (pro_codartE.isNull()?"":" and UPPER(pro_codart) like '%" + pro_codartE.getText() +"%'");
            ArrayList v= new ArrayList();
            v.add(pArticVenta.getCongeladoQuery());
            v.add(pArticVenta.getCuracionQuery());
            v.add(pArticVenta.getCorteQuery());
            s+=pArticVenta.getCondicionesQuery(false);
            s = creaWhere(s, v,false);
//            debug(s);
            if (rseCodcli!=0)
            {
                 if (dtCon1.select("select * from relsubempr where rse_codcli="+rseCodcli))
                 {
                    s+=" and sbe_codi IN (SELECT rse_codart FROM relsubempr  where rse_codcli="+rseCodcli+")";
                 }
            }
            s+=" order by pro_nomb";
            if (!dtCon1.select(s))
            {
                msgBox("No encontrados productos con estas condiciones");
                pro_nombE.requestFocus();
                return;
            }
            
            jt.setDatos(dtCon1,true);
        } catch (SQLException ex) {
            Logger.getLogger(AyuArt.class.getName()).log(Level.SEVERE, null, ex);
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
        jt = new gnu.chu.controles.Cgrid(3);
        Pcondic = new gnu.chu.controles.CPanel();
        pro_nombL = new gnu.chu.controles.CLabel();
        pro_nombE = new gnu.chu.controles.CTextField();
        pro_activE = new gnu.chu.controles.CComboBox();
        Bbuscar = new gnu.chu.controles.CButton(Iconos.getImageIcon("check"));
        pArticVenta = new gnu.chu.utilidades.PanelArticVenta();
        pro_codartL = new gnu.chu.controles.CLabel();
        pro_codartE = new gnu.chu.controles.CTextField();

        Pprinc.setLayout(new java.awt.GridBagLayout());

        ArrayList cabecera=new ArrayList();
        cabecera.add("Codigo"); // 0 -- Codigo
        cabecera.add("Refer"); // 1-- Codigo Alfanumerico prod.
        cabecera.add("Nombre"); //2 -- Nombre

        jt.setCabecera(cabecera);

        jt.setAnchoColumna(new int[]{60,120,300});

        jt.alinearColumna(new int[]{2,0,0});
        jt.setNumRegCargar(500);
        jt.setAjustarGrid(true);
        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jt.setPreferredSize(new java.awt.Dimension(100, 100));

        javax.swing.GroupLayout jtLayout = new javax.swing.GroupLayout(jt);
        jt.setLayout(jtLayout);
        jtLayout.setHorizontalGroup(
            jtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 490, Short.MAX_VALUE)
        );
        jtLayout.setVerticalGroup(
            jtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 211, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.weighty = 2.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 1, 0, 0);
        Pprinc.add(jt, gridBagConstraints);

        Pcondic.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        Pcondic.setMaximumSize(new java.awt.Dimension(475, 129));
        Pcondic.setMinimumSize(new java.awt.Dimension(475, 129));
        Pcondic.setPreferredSize(new java.awt.Dimension(475, 129));
        Pcondic.setLayout(null);

        pro_nombL.setText("Referencia");
        Pcondic.add(pro_nombL);
        pro_nombL.setBounds(300, 2, 70, 17);

        pro_nombE.setMayusc(true);
        Pcondic.add(pro_nombE);
        pro_nombE.setBounds(50, 2, 240, 17);

        pro_activE.addItem("Activo", "S");
        pro_activE.addItem("Todos","*");
        pro_activE.addItem("Inactivo", "N");
        Pcondic.add(pro_activE);
        pro_activE.setBounds(380, 80, 80, 17);

        Bbuscar.setText("Buscar");
        Pcondic.add(Bbuscar);
        Bbuscar.setBounds(380, 100, 85, 24);

        pArticVenta.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Pcondic.add(pArticVenta);
        pArticVenta.setBounds(0, 20, 375, 105);

        pro_codartL.setText("Nombre");
        Pcondic.add(pro_codartL);
        pro_codartL.setBounds(2, 2, 50, 17);

        pro_codartE.setMayusc(true);
        Pcondic.add(pro_codartE);
        pro_codartE.setBounds(370, 2, 90, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 1, 2);
        Pprinc.add(Pcondic, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Bbuscar;
    private gnu.chu.controles.CPanel Pcondic;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.Cgrid jt;
    private gnu.chu.utilidades.PanelArticVenta pArticVenta;
    private gnu.chu.controles.CComboBox pro_activE;
    private gnu.chu.controles.CTextField pro_codartE;
    private gnu.chu.controles.CLabel pro_codartL;
    private gnu.chu.controles.CTextField pro_nombE;
    private gnu.chu.controles.CLabel pro_nombL;
    // End of variables declaration//GEN-END:variables
}
