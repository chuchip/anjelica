package gnu.chu.winayu;
/**
 * <p>Titulo:   AyuArt </p>
 * <p>Descripción: Ventana de ayuda para buscar productos por nombre/familia/estado.
 * <p>Copyright: (c) 2005-2015
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
import java.sql.Types;
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
    this.setSize(new Dimension(466, 426));
  }
    @Override
  public void iniciarVentana() throws Exception
  {
    s="select fpr_codi,fpr_nomb from v_famipro order by fpr_nomb";
    dtCon1.select(s);
    fam_codiE.addDatos(dtCon1);
    s="select agr_codi,agp_nomb from v_agupro order by agp_nomb";
    dtCon1.select(s);
    agr_codiE.addDatos(dtCon1);
    activarEventos();
    Pcondic.setDefButton(Bbuscar);
    reset();
  }
  public void reset()
  {
      chose=false;
      Pcondic.resetTexto();
      fam_codiE.setText("");
      agr_codiE.setText("");
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
            s = "select pro_codi,pro_codart,pro_nomb,fam_codi,fpr_nomb FROM v_articulo a,v_famipro f,v_agupro as gr " +
                 " where a.fam_codi=f.fpr_codi "+
                 " and f.agr_codi = gr.agr_codi "+
                 (pro_activE.getValor().equals("*")?"":
                  (" and pro_activ "+(pro_activE.getValor().equals("S")?" != 0":"= 0")))+
                  (pro_congE.getValor().equals("*")?"":
                  (" and pro_artcon "+(pro_congE.getValor().equals("F")?" = 0":"!= 0")))+
                  (pro_nombE.isNull()?"":" and UPPER(pro_nomb) like '%" + pro_nombE.getText() +"%'")+
                  (fam_codiE.isNull()?"":" and f.fpr_codi = "+fam_codiE.getValorInt())+
                  (pro_codartE.isNull(true)?"":" and pro_codart like '%"+pro_codartE.getText()+"%'")+
                  (agr_codiE.isNull()?"":" and gr.agr_codi = "+agr_codiE.getValorInt());
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
        jt = new gnu.chu.controles.Cgrid(5);
        Pcondic = new gnu.chu.controles.CPanel();
        pro_nombL = new gnu.chu.controles.CLabel();
        pro_nombE = new gnu.chu.controles.CTextField();
        cLabel1 = new gnu.chu.controles.CLabel();
        agr_codiE = new gnu.chu.controles.CLinkBox();
        cLabel2 = new gnu.chu.controles.CLabel();
        fam_codiE = new gnu.chu.controles.CLinkBox();
        pro_activE = new gnu.chu.controles.CComboBox();
        Bbuscar = new gnu.chu.controles.CButton(Iconos.getImageIcon("check"));
        pro_congE = new gnu.chu.controles.CComboBox();
        cLabel3 = new gnu.chu.controles.CLabel();
        pro_codartE = new gnu.chu.controles.CTextField(Types.CHAR,"X",12);

        Pprinc.setLayout(new java.awt.GridBagLayout());

        ArrayList cabecera=new ArrayList();
        cabecera.add("Codigo"); // 0 -- Codigo
        cabecera.add("Refer"); // 1-- Codigo Alfanumerico prod.
        cabecera.add("Nombre"); //2 -- Nombre
        cabecera.add("Familia"); // 3 -- Familia
        cabecera.add("Descrip"); // 4-- Desc. Familia

        jt.setCabecera(cabecera);

        jt.setAnchoColumna(new int[]{46,100,250,20,150});

        jt.alinearColumna(new int[]{2,0,0,2,0});
        jt.setNumRegCargar(500);
        jt.setAjustarGrid(true);
        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jtLayout = new javax.swing.GroupLayout(jt);
        jt.setLayout(jtLayout);
        jtLayout.setHorizontalGroup(
            jtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 420, Short.MAX_VALUE)
        );
        jtLayout.setVerticalGroup(
            jtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 271, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 1, 0, 0);
        Pprinc.add(jt, gridBagConstraints);

        Pcondic.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        Pcondic.setLayout(null);

        pro_nombL.setText("Nombre");
        Pcondic.add(pro_nombL);
        pro_nombL.setBounds(2, 2, 50, 15);

        pro_nombE.setMayusc(true);
        Pcondic.add(pro_nombE);
        pro_nombE.setBounds(50, 2, 250, 17);

        cLabel1.setText("Refer.");
        Pcondic.add(cLabel1);
        cLabel1.setBounds(172, 42, 42, 15);

        agr_codiE.setAncTexto(30);
        agr_codiE.combo.setPreferredSize(new java.awt.Dimension(352, 17));
        agr_codiE.setFormato(Types.DECIMAL,"##9");
        Pcondic.add(agr_codiE);
        agr_codiE.setBounds(50, 42, 120, 17);

        cLabel2.setText("Familia ");
        Pcondic.add(cLabel2);
        cLabel2.setBounds(2, 22, 42, 15);

        fam_codiE.setAncTexto(30);
        fam_codiE.combo.setPreferredSize(new java.awt.Dimension(352, 17));
        fam_codiE.setFormato(Types.DECIMAL,"##9");
        Pcondic.add(fam_codiE);
        fam_codiE.setBounds(50, 22, 250, 17);

        pro_activE.addItem("Activo", "S");
        pro_activE.addItem("Todos","*");
        pro_activE.addItem("Inactivo", "N");
        Pcondic.add(pro_activE);
        pro_activE.setBounds(305, 2, 100, 17);

        Bbuscar.setText("Buscar");
        Pcondic.add(Bbuscar);
        Bbuscar.setBounds(320, 40, 85, 24);

        pro_congE.addItem("Todos","*");
        pro_congE.addItem("Fresco", "F");
        pro_congE.addItem("Congelado", "C");
        Pcondic.add(pro_congE);
        pro_congE.setBounds(305, 22, 100, 17);

        cLabel3.setText("Grupo");
        Pcondic.add(cLabel3);
        cLabel3.setBounds(2, 42, 42, 15);

        pro_codartE.setMayusc(true);
        Pcondic.add(pro_codartE);
        pro_codartE.setBounds(220, 42, 90, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 415;
        gridBagConstraints.ipady = 69;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 1, 2);
        Pprinc.add(Pcondic, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Bbuscar;
    private gnu.chu.controles.CPanel Pcondic;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CLinkBox agr_codiE;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLinkBox fam_codiE;
    private gnu.chu.controles.Cgrid jt;
    private gnu.chu.controles.CComboBox pro_activE;
    private gnu.chu.controles.CTextField pro_codartE;
    private gnu.chu.controles.CComboBox pro_congE;
    private gnu.chu.controles.CTextField pro_nombE;
    private gnu.chu.controles.CLabel pro_nombL;
    // End of variables declaration//GEN-END:variables
}
