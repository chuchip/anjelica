package gnu.chu.anjelica.pad;

/**
 *
 * <p>Título: MantFamPro </p>
 * <p>Descripcion: Mantenimiento Familias  de Articulos</p>
 * <p>Empresa: miSL</p>
*  <p>Copyright: Copyright (c) 2005- 2012
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
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
 * @author ChuchiP
 * @version 1.0
 */
import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.menu;
import gnu.chu.controles.CLinkBox;
import gnu.chu.controles.StatusBar;
import gnu.chu.interfaces.PAD;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.navegador;
import gnu.chu.utilidades.ventanaPad;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MantFamPro   extends ventanaPad  implements PAD  {
    private boolean swCarga=true;
    private int famAntigua=0;
    private int gruAntiguo=0;
    boolean modConsulta=false;
    String s;
   
 public MantFamPro(EntornoUsuario eu, Principal p)
  {
    this(eu,p,null);
  }
  public MantFamPro(EntornoUsuario eu, Principal p,Hashtable ht)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    try
    {
      if (ht != null)
      {
        if (ht.get("modConsulta") != null)
          modConsulta = Boolean.valueOf(ht.get("modConsulta").toString()).
              booleanValue();
      }
      setTitulo("Mantenimiento Familias de Articulos");

      if (jf.gestor.apuntar(this))
        jbInit();
      else
        setErrorInit(true);
    }
    catch (Exception e)
    {
      Logger.getLogger(MantFamPro.class.getName()).log(Level.SEVERE, null, e);
      setErrorInit(true);
    }
  }

  public MantFamPro(menu p, EntornoUsuario eu,Hashtable ht)
  {
    EU = eu;
    vl = p.getLayeredPane();
    eje = false;

    try
    {
      if (ht != null)
      {
        if (ht.get("modConsulta") != null)
          modConsulta = Boolean.valueOf(ht.get("modConsulta").toString()).
              booleanValue();
      }
      setTitulo("Mantenimiento Familias de Articulos");
      jbInit();
    }
    catch (Exception e)
    {
      Logger.getLogger(MantFamPro.class.getName()).log(Level.SEVERE, null, e);
      setErrorInit(true);
    }
  }

  private void jbInit() throws Exception {
        statusBar = new StatusBar(this);
        nav = new navegador(this, dtCons, false, modConsulta ? navegador.NOBOTON : navegador.BTN_EDITAR);
        iniciarFrame();
//        this.setResizable(false);

        this.setVersion("2012-01-20" + (modConsulta ? "SOLO LECTURA" : ""));
        strSql = "SELECT * FROM v_famipro "+
                " ORDER BY fpr_codi";

        this.getContentPane().add(nav, BorderLayout.NORTH);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        this.setPad(this);
        dtCons.setLanzaDBCambio(false);
        initComponents();
        iniciarBotones(Baceptar, Bcancelar);

        conecta();
        navActivarAll();
        this.setSize(663,524);
        activar(false);
    }
    @Override
    public void iniciarVentana() throws Exception
   {
        nav.setPulsado(navegador.NINGUNO);
        jt.setDefButton(Baceptar);
        jtGru.setDefButton(Baceptar);
        jt.setButton(KeyEvent.VK_F2, BirGrid);
        jtGru.setButton(KeyEvent.VK_F2, BirGrid);
        arbolProdPanel.iniciar(dtCon1);
        activar(false);
        activarEventos();
        verDatos();
    }
    void activarEventos()
    {
        BirGrid.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if (nav.pulsado==navegador.NINGUNO)
                    return;
                if (!salirGrid())
                    return;

                jtGru.setEnabled(jt.isEnabled());
                jt.setEnabled(!jt.isEnabled());
                if (jt.isEnabled())
                    jt.requestFocusSelected();
                else
                    jtGru.requestFocusInicio();
            }
        });
       
    }
    void verDatos()
    {
        swCarga=true;
        jt.removeAllDatos();
        jtGru.removeAllDatos();

        try {
            s="SELECT * FROM v_famipro order by fpr_codi";
            if (! dtCon1.select(s))
            {
                msgBox("NO hay ninguna FAMILIA");
                return;
            }
            do {
                ArrayList v = new ArrayList();
                v.add(dtCon1.getString("fpr_codi"));
                v.add(dtCon1.getString("fpr_nomb"));
                v.add(dtCon1.getString("fpr_ctacom"));
                v.add(dtCon1.getString("fpr_ctaven"));
                jt.addLinea(v);
            } while (dtCon1.next());
            jt.requestFocusInicio();
            famAntigua=jt.getValorInt(0,0);
            swCarga=false;
            verDatosGrupo();
            arbolProdPanel.verDatosArbol(); // Fresco
        }catch (SQLException k)
        {
            Error("Error al presentar datos",k);
        }
       
    }
    
    private void verDatosGrupo()
    {
        if (swCarga)
            return;
        if (nav.pulsado!=navegador.NINGUNO)
            jtGru.setEnabled(false);
        jtGru.removeAllDatos();
        s = "select gf.agr_codi,g.agp_nomb from grufampro as gf, v_agupro as g "
                + " where gf.fpr_codi =  "+jt.getValorInt(jt.getSelectedRowDisab(),0)
                + " and gf.agr_codi =  g.agr_codi "
                + " order by gf.agr_codi ";
        try {
            if (!dtCon1.select(s)) {
                mensajeErr("NO hay ningun GRUPO para esta FAMILIA");
                return;
            }
            do {
                ArrayList v = new ArrayList();
                v.add(dtCon1.getString("agr_codi"));
                v.add(dtCon1.getString("agp_nomb"));
                jtGru.addLinea(v);
            } while (dtCon1.next());
            if (nav.pulsado!=navegador.NINGUNO)
                jtGru.setEnabled(!jt.isEnabled());
            if (!jtGru.isEnabled())
             jtGru.requestFocusInicio();
            gruAntiguo=jtGru.getValorInt(jtGru.getSelectedRow(),0);
        } catch (SQLException k) {
            Error("Error al presentar datos", k);
            return;
        }
    }
   public static void llenaLinkBox(CLinkBox famCodi,DatosTabla dt) throws SQLException
   {
    String s = "SELECT fpr_codi,fpr_nomb FROM v_famipro "+
              " ORDER BY fpr_nomb";
    dt.select(s);
    famCodi.addDatos(dt);
    famCodi.setFormato(Types.DECIMAL, "#9", 2);
   }
   /**
    * Devuelve el nombre de la familia mandada como parametro
    * @param famCodi Código de familia
    * @param dt DatosTabla
    * @return Nombre de Familia. Null si no existe ninguna familia con el codigo mandado.
    * @throws SQLException 
    */
   public static String getNombreFam(int famCodi, DatosTabla dt) throws SQLException
   {
      String s="SELECT fpr_nomb FROM v_famipro "+
               " where fpr_codi = "+famCodi;
      if (! dt.select(s))
          return null;
      return dt.getString("fpr_nomb");
   }
    public void activar(boolean activ)
    {
        jt.setEnabled(activ);
        jtGru.setEnabled(activ);
        Baceptar.setEnabled(activ);
        BirGrid.setEnabled(activ);
    }
    int cambiaLineaJTGru(int row, int col)
    {
        if (agr_codiE.getValorInt()==0)
        {
            if (gruAntiguo==0)
                return -1;
            mensajeErr("Introduzca un Grupo");
            return 0;
        }
        try {
            if (!buscaGrupo(dtStat, agr_codiE.getValorInt())) {
                mensajeErr("Grupo NO existe");
                return 0;
            }
            agr_codiE.setText(dtStat.getString("agp_nomb"));
            jtGru.setValor(dtStat.getString("agp_nomb"),row,1);
            
            int nRow = jtGru.getRowCount();
            for (int n = 0; n < nRow; n++) {
                if (n == row) {
                    continue;
                }
                if (jtGru.getValorInt(n, 0) == agr_codiE.getValorInt()) {
                    mensajeErr("Este GRUPO ya esta introducida en linea " + (n + 1));
                    return 0;
                }
            }
            if (gruAntiguo!=agr_codiE.getValorInt())
            { // Bprro familia anterior
                if (buscaFamGrupo(dtAdd, gruAntiguo, fpr_codiE.getValorInt(), true))
                 dtAdd.delete();
            }
            guardaLineaGrupo();
        } catch (Exception k)
        {
            Error("Error al cambiar Linea de familia",k);
            return -1;
        }
        return -1;
    }

    int cambiaLineaJT(int row, int col)
    {
        if (fpr_codiE.getValorInt()==0)
        {
            if (famAntigua==0)
                return -1;
            mensajeErr("Introduzca una Familia");
            return 0;
        }
        if (fpr_nombE.isNull())
        {
            mensajeErr("Introduzca descripcion de Familia");
            return 1;
        }
        int nRow=jt.getRowCount();
        for (int n=0;n<nRow;n++)
        {
            if (n==row)
                continue;
            if (jt.getValorInt(n,0)==fpr_codiE.getValorInt())
            {
                mensajeErr("Esta FAMILIA ya esta introducida en linea "+(n+1));
                return 0;
            }
        }
        try {
            if (famAntigua!=fpr_codiE.getValorInt())
            { // Bprro familia anterior
                if (buscaFamilia(dtAdd, famAntigua, true))
                 dtAdd.delete();
            }
            guardaLinea();
        } catch (Exception k)
        {
            Error("Error al cambiar Linea de familia",k);
            return -1;
        }
        return -1;
    }
    /**
     * Guarda linea de la familia
     * @throws SQLException
     */
    void guardaLinea() throws SQLException
    {
       if (buscaFamilia(dtAdd, fpr_codiE.getValorInt(),true))
       {
           dtAdd.edit();
       }
       else
       {
        dtAdd.addNew("v_famipro");
        dtAdd.setDato("emp_codi", EU.em_cod);
        dtAdd.setDato("fpr_codi", fpr_codiE.getValorInt());
       }
       dtAdd.setDato("fpr_nomb", fpr_nombE.getText());
       dtAdd.setDato("agr_codi", jtGru.isVacio()?1: jtGru.getValorInt(0,0));
       dtAdd.setDato("fpr_ctacom", fpr_ctacomE.getText());
       dtAdd.setDato("fpr_ctaven", fpr_ctavenE.getText());
       dtAdd.update();
       dtAdd.commit();
    }
     void guardaLineaGrupo() throws SQLException
    {
       if (buscaFamGrupo(dtAdd, agr_codiE.getValorInt(),fpr_codiE.getValorInt(), true))
       {
           dtAdd.edit();
       }
       else
       {
        dtAdd.addNew("grufampro");
        dtAdd.setDato("fpr_codi", fpr_codiE.getValorInt());
       }
       dtAdd.setDato("agr_codi", agr_codiE.getValorInt());
       dtAdd.update();
       dtAdd.commit();
    }
    boolean buscaFamilia(DatosTabla dt, int famCodi,boolean forUpdate) throws SQLException {
        s = "SELECT * FROM v_famipro where fpr_codi =" + famCodi;
        return dt.select(s,forUpdate);
    }
    boolean buscaGrupo(DatosTabla dt, int grpCodi) throws SQLException {
        s="SELECT agr_codi,agp_nomb FROM v_agupro where agr_codi= "+grpCodi;
        return dt.select(s);
    }
    boolean buscaFamGrupo(DatosTabla dt, int grpCodi,int famCodi, boolean forUpdate) throws SQLException {
        s="SELECT agr_codi,fpr_codi FROM grufampro where agr_codi= "+grpCodi+
                " and fpr_codi = "+famCodi;
        return dt.select(s,forUpdate);
    }
    void afterCambiaLineaJT()
    {
        famAntigua=jt.getValorInt(jt.getSelectedRow(),0);
        verDatosGrupo();
        jt.requestFocusSelected();
    }
    void afterCambiaLineaJTGru()
    {
       gruAntiguo=jtGru.getValorInt(jtGru.getSelectedRow(),0);
    }
    void afterInsertaLineaJT(boolean insLinea)
    {
        famAntigua=0;
    }
    void afterInsertaLineaJTGru(boolean insLinea)
    {
        gruAntiguo=0;
    }
    void  afterCambiaLineaDisJT(int nRow)
    {
        if (nav.getPulsado()!=navegador.EDIT)
            verDatosGrupo();
    }
   
    boolean  deleteLineaJT(int row,int col)
    {
        if (famAntigua == 0) {
            return true;
        }
        try {
            if (buscaFamilia(dtAdd, famAntigua, true)) {
                dtAdd.delete();
                dtAdd.commit();
            } else {
                msgBox("No encontrada FAMILIA. Imposible BORRAR linea");
                return false;
            }
        } catch (Exception k) {
            Error("Error al BORRAR Linea de familia", k);
        }
        return true;
    }
     boolean  deleteLineaJTGru(int row,int col)
    {
        if (gruAntiguo == 0) {
            return true;
        }
        try {
            if (buscaFamGrupo(dtAdd, gruAntiguo,fpr_codiE.getValorInt(), true)) {
                dtAdd.delete();
                dtAdd.commit();
            } else {
                msgBox("No encontrado GRUPO en esta FAMILIA. Imposible BORRAR linea");
                return false;
            }
        } catch (Exception k) {
            Error("Error al BORRAR Linea de grupo en familia", k);
        }
        return true;
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

        fpr_codiE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9");
        fpr_nombE = new gnu.chu.controles.CTextField(Types.CHAR,"X",30);
        fpr_ctacomE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###########9");
        fpr_ctavenE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###########9");
        agr_codiE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9");
        agr_nombE = new gnu.chu.controles.CTextField(Types.CHAR,"X",30);
        PTabPrinc = new gnu.chu.controles.CTabbedPane();
        Pprinc = new gnu.chu.controles.CPanel();
        jt = new gnu.chu.controles.CGridEditable(4){
            public int cambiaLinea(int row, int col)
            {
                return cambiaLineaJT(row,col);
            }
            public void afterCambiaLinea()
            {
                afterCambiaLineaJT();
            }
            public void afterCambiaLineaDis(int nRow)
            {
                afterCambiaLineaDisJT(nRow);
            }
            public boolean deleteLinea(int row, int col) {
                return deleteLineaJT(row,col);
            }
            public boolean afterInsertaLinea(boolean insLinea)
            {
                afterInsertaLineaJT(insLinea);
                return true;
            }
        };
        {
            Vector v= new Vector();
            v.add("Familia");
            v.add("Nombre");
            v.add("Cuenta Compras");
            v.add("Cuenta Ventas");
            jt.setCabecera(v);
        }

        try {
            {
                Vector v=new Vector();
                v.add(fpr_codiE);
                v.add(fpr_nombE);
                v.add(fpr_ctacomE);
                v.add(fpr_ctavenE);
                jt.setCampos(v);
            }
        } catch (Exception k){}
        jt.setAnchoColumna(new int[]{40,180,70,70});
        jt.setAlinearColumna(new int[]{2,0,0,0});
        jt.setAjustarGrid(true);
        jtGru = new gnu.chu.controles.CGridEditable(2)
        {
            public int cambiaLinea(int row, int col)
            {
                return cambiaLineaJTGru(row,col);
            }
            public void afterCambiaLinea()
            {
                afterCambiaLineaJTGru();
            }
            public boolean deleteLinea(int row, int col) {
                return deleteLineaJTGru(row,col);
            }
            public boolean afterInsertaLinea(boolean insLinea)
            {
                afterInsertaLineaJTGru(insLinea);
                return true;
            }
        }
        ;
        Vector v=new Vector();
        v.add("Grupo");
        v.add("Nombre");
        jtGru.setCabecera(v);
        try {
            {
                Vector v1=new Vector();
                v1.add(agr_codiE);
                v1.add(agr_nombE);
                jtGru.setCampos(v1);
            }
        } catch (Exception k){}
        jtGru.setAlinearColumna(new int[]{2,0});
        jtGru.setAjustarGrid(true);
        jtGru.setAnchoColumna(new int[]{40,200});
        Ppie = new gnu.chu.controles.CPanel();
        Baceptar = new gnu.chu.controles.CButton();
        BirGrid = new gnu.chu.controles.CButton();
        Pvisual = new gnu.chu.controles.CPanel();
        arbolProdPanel = new gnu.chu.anjelica.pad.ArbolProdPanel();

        fpr_codiE.setCeroIsNull(true);

        agr_nombE.setEnabled(false);

        Pprinc.setLayout(new java.awt.GridBagLayout());

        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jt.setMaximumSize(new java.awt.Dimension(100, 250));
        jt.setMinimumSize(new java.awt.Dimension(100, 250));

        javax.swing.GroupLayout jtLayout = new javax.swing.GroupLayout(jt);
        jt.setLayout(jtLayout);
        jtLayout.setHorizontalGroup(
            jtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 538, Short.MAX_VALUE)
        );
        jtLayout.setVerticalGroup(
            jtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 281, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.weighty = 2.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        Pprinc.add(jt, gridBagConstraints);

        jtGru.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtGru.setMaximumSize(new java.awt.Dimension(100, 175));
        jtGru.setMinimumSize(new java.awt.Dimension(100, 175));
        jtGru.setProcInsLinea(true);

        javax.swing.GroupLayout jtGruLayout = new javax.swing.GroupLayout(jtGru);
        jtGru.setLayout(jtGruLayout);
        jtGruLayout.setHorizontalGroup(
            jtGruLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 538, Short.MAX_VALUE)
        );
        jtGruLayout.setVerticalGroup(
            jtGruLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 140, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        Pprinc.add(jtGru, gridBagConstraints);

        Ppie.setMaximumSize(new java.awt.Dimension(205, 22));
        Ppie.setMinimumSize(new java.awt.Dimension(205, 22));
        Ppie.setPreferredSize(new java.awt.Dimension(205, 22));
        Ppie.setLayout(new java.awt.GridBagLayout());

        Baceptar.setText("Aceptar");
        Baceptar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Baceptar.setMaximumSize(new java.awt.Dimension(63, 19));
        Baceptar.setMinimumSize(new java.awt.Dimension(63, 19));
        Baceptar.setPreferredSize(new java.awt.Dimension(63, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 40;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 3, 0);
        Ppie.add(Baceptar, gridBagConstraints);

        BirGrid.setIcon( Iconos.getImageIcon("run"));
        BirGrid.setText("F2");
        BirGrid.setToolTipText("F2 para moverse entre los dos grids");
        BirGrid.setMaximumSize(new java.awt.Dimension(24, 20));
        BirGrid.setMinimumSize(new java.awt.Dimension(24, 20));
        BirGrid.setPreferredSize(new java.awt.Dimension(24, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 25);
        Ppie.add(BirGrid, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        Pprinc.add(Ppie, gridBagConstraints);

        PTabPrinc.addTab("Familias", Pprinc);

        Pvisual.setLayout(new java.awt.BorderLayout());
        Pvisual.add(arbolProdPanel, java.awt.BorderLayout.CENTER);

        PTabPrinc.addTab("Visualizacion", Pvisual);

        getContentPane().add(PTabPrinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CButton BirGrid;
    private gnu.chu.controles.CTabbedPane PTabPrinc;
    private gnu.chu.controles.CPanel Ppie;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CPanel Pvisual;
    private gnu.chu.controles.CTextField agr_codiE;
    private gnu.chu.controles.CTextField agr_nombE;
    private gnu.chu.anjelica.pad.ArbolProdPanel arbolProdPanel;
    private gnu.chu.controles.CTextField fpr_codiE;
    private gnu.chu.controles.CTextField fpr_ctacomE;
    private gnu.chu.controles.CTextField fpr_ctavenE;
    private gnu.chu.controles.CTextField fpr_nombE;
    private gnu.chu.controles.CGridEditable jt;
    private gnu.chu.controles.CGridEditable jtGru;
    // End of variables declaration//GEN-END:variables
    @Override
    public void PADEdit() {
     activar(true);
     jtGru.setEnabled(false);

     mensajeErr("");
     mensaje("Editando Registro  ...");

     jt.requestFocusInicio();
    }
    public void PADPrimero() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void PADAnterior() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void PADSiguiente() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void PADUltimo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void ej_query1() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void canc_query() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    boolean salirGrid()
    {
         if (jt.isEnabled()) {
            jt.salirFoco();
            if (cambiaLineaJT(jt.getSelectedRow(), 0) >= 0) {
                jt.requestFocusSelected();
                return false;
            }
        } else {
            jtGru.salirFoco();
            if (cambiaLineaJTGru(jtGru.getSelectedRow(), 0) >= 0) {
                jtGru.requestFocusSelected();
                return false;
            }
        }
        return true;
    }
    public void ej_edit1() {
        if (!salirGrid())
            return;
        nav.setPulsado(navegador.NINGUNO);
        activaTodo();
        verDatos();
        mensajeErr("Edicion REALIZADA");
    }

    public void canc_edit() {
       nav.setPulsado(navegador.NINGUNO);
    }

    public void ej_addnew1() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void canc_addnew() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void ej_delete1() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void canc_delete() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    /**
     * Devuelve la familia de Reciclaje. Definida en la tabla 'parametros'
     * @param EU EntornoUsuario
     * @return 
     */
    public static int getFamiliaRecicla(EntornoUsuario EU)
    {
        return EU.getValorParam("famProdReci",99);
    }
}


