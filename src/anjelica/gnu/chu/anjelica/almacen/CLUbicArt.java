
package gnu.chu.anjelica.almacen;
/**
 *
 * <p>Título: CLUbicArt</p>
 * <p>Descripción: Consulta y Listado de Ubicaciones de Articulos.
 * Muestra las ubicaciones de los diferentes articulos en los almacenes externos.
 * Permite seleccionar productos y listar la hoja.
 *  </p>
 * <p>Copyright: Copyright (c) 2005-2015
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los términos de la Licencia Pública General de GNU segun es publicada por
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
 * <p>Empresa: miSL</p>
 * @author chuchi P
 * @version 2.0 (Antes era traspalma)
 */
import gnu.chu.Menu.Principal;
import gnu.chu.controles.StatusBar;
import gnu.chu.print.util;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.miThread;
import gnu.chu.utilidades.ventana;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;


public class CLUbicArt extends ventana implements  JRDataSource
{    
  int nLinGrid=0,JT_nRows;   
  ListStock ifList=new ListStock(this)
  {
    @Override
    public void matado()
    {
        if (ifList.getFechaHist()!=null)
            alm_codiE.setValorInt(ifList.getAlmacen());
        consulta();
    }
  };
  private final static int JT_ARTIC=0;
  private final static int JT_NOMBR=1;
  private final static int JT_EJERC=2;
  private final static int JT_SERIE=3;
  private final static int JT_LOTE=4;
  private final static int JT_INDI=5;
  private final static int JT_PESO=6;
  private final static int JT_UNID=7;
  private final static int JT_NUMPAL=8;
  private final static int JT_NUMCAJ=9;
  private final static int JT_INSER=10;
  
  public CLUbicArt(EntornoUsuario eu, Principal p)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    setTitulo("Cons./List. Ubicacion Articulos");

    try
    {
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

  public CLUbicArt(gnu.chu.anjelica.menu p, EntornoUsuario eu)
  {
    EU = eu;
    vl = p.getLayeredPane();
    setTitulo("Cons./List. Ubicacion Articulos");
    eje = false;

    try
    {
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

      this.setVersion("2015-06-11");
      statusBar = new StatusBar(this);
      this.getContentPane().add(statusBar, BorderLayout.SOUTH);
      conecta();

      initComponents();
      this.setSize(new Dimension(750,500));
     
   
  }
    @Override
    public void iniciarVentana() throws Exception 
    {       
        jt.setOrdenar(false);
        pdalmace.llenaLinkBox(alm_codiE,dtStat,'*');        
        alm_codiE.setAceptaNulo(false);
        alm_codiE.setTextInicio();
        pro_codiE.iniciar(dtStat, this, vl, EU);
        Pcabe.setDefButton(BBuscar.getBotonAccion());
        ifList.setVisible(false);
        ifList.setClosable(false);
        this.getLayeredPane().add(ifList);
        ifList.setLocation(5,5);
        activarEventos();
    }
    void activarEventos()
    {
      BBuscar.addActionListener(new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)            
        {          
            if (e.getActionCommand().contains("Historico"))             
               verHistorico();
            else
               consulta();
        }
      });
      
      Binv.addActionListener(new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {     
           if (jt.isVacio())
             return;
           invertirSelecion();
        }
      });
      BPrint.addActionListener(new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {     
           if (jt.isVacio())
             return;
           imprimir();
        }
      });  
      jt.addMouseListener(new MouseAdapter()
      {
         @Override
         public void mouseClicked(MouseEvent e) {
           if (jt.isVacio())
               return;
           if (jt.getSelectedColumn()!=JT_INSER)
               return;
           jt.setValor(!jt.getValBoolean(JT_INSER),JT_INSER);
           actAcumulados();
         }  
      });
    }
    
    private void actAcumulados()
    {
        int nLin=jt.getRowCount();
        ArrayList<Integer> palets=new ArrayList();
        ArrayList<Integer> cajas=new ArrayList();
        int numPalet;
       
        int nInd=0;
        double kilos=0;
        for (int n=0;n<nLin;n++)
        {
            if (!jt.getValBoolean(n, JT_INSER))
                continue;
            nInd+=jt.getValorInt(n,JT_UNID);
            kilos+=jt.getValorDec(n,JT_PESO);
            numPalet=jt.getValorInt(n,JT_NUMPAL);
            if (!palets.contains(numPalet))
            {
                palets.add(numPalet);
            }
            numPalet=(numPalet*-1)+jt.getValorInt(n,JT_NUMCAJ);
             if (!cajas.contains(numPalet))
            {
                cajas.add(numPalet);               
            }
        }
        numIndivE.setValorInt(nInd);
        kilosE.setValorDec(kilos);
        numPaletsE.setValorInt(palets.size());
        numCajasE.setValorInt(cajas.size());
        
    }
    private void invertirSelecion()
    {
        int nLin=jt.getRowCount();
        for (int n=0;n<nLin;n++)
        {
            jt.setValor(!jt.getValBoolean(n,JT_INSER),n,JT_INSER);
        }
        actAcumulados();
    }
    public void matar(boolean cerrarConexion)
    {
        if (muerto)
          return;
        if (ifList!=null)
        {
          ifList.setVisible(false);
          ifList.dispose();
        } 

        super.matar(cerrarConexion);
    }
    void verHistorico()
    {
        
      try
      {
          ifList.setVisible(true);
          ifList.iniciar(dtCon1,alm_codiE.getValorInt());
      } catch (SQLException ex)
      {
          Error("Error al ver datos historico de listados", ex);
      }
    }
    void consulta()
    {
        mensaje("");
        if (!alm_codiE.controla())
        {
            mensajeErr("Almacen no es valido");
            return;
        }
        new miThread("")
        {
             @Override
            public void run()
            {
                  msgEspere("Cargando datos...");
                  consulta1();
                  resetMsgEspere();
                  jt.setEnabled(true);
                  actAcumulados();
            }
        };
    }
   void consulta1()
    {
        try {
            jt.setEnabled(false);
            jt.removeAllDatos();
            String s="select a.*,p.pro_nomb from v_stkpart as a left join v_articulo as p on a.pro_codi =p.pro_codi where alm_codi="+alm_codiE.getText()+
                " and stp_kilact not between -0.2 and 0.2 "+
                (pro_codiE.isNull()?"":" and a.pro_codi = "+pro_codiE.getValorInt())+
                " order by a.pro_codi,stp_numpal,stp_numcaj ";
            if (!dtCon1.select(s))
            {
                msgBox("NO encontrado stock para las condiciones dadas");
                return;
            }
            jt.tableView.setVisible(false);
            boolean verHist=ifList.getFechaHist()!=null;
            PreparedStatement ps=null;
            boolean valorInc;
            if (verHist)
            {
                  ps=dtStat.getPreparedStatement("select * from hislisstk where hls_fecstk={ts '"+Formatear.getFecha(ifList.getFechaHist(),"yyyy-MM-dd HH:mm:ss")+"'}"+
                    " and alm_codi=  "+alm_codiE.getValorInt()+
                    " and pro_codi=? and pro_ejelot=? and pro_serlot=? and pro_numlot=? and pro_indlot=? ");  
            }
            do
            {
                ArrayList v=new ArrayList();
                v.add(dtCon1.getInt("pro_codi")); // 0
                v.add(dtCon1.getString("pro_nomb")); // 1
                v.add(dtCon1.getInt("eje_nume"));
                v.add(dtCon1.getString("pro_serie")); // 3
                v.add(dtCon1.getInt("pro_nupar")); // 4
                v.add(dtCon1.getInt("pro_numind")); // 5
                v.add(dtCon1.getDouble("stp_kilact")); // 6
                v.add(dtCon1.getInt("stp_unact")); // 7
                v.add(dtCon1.getInt("stp_numpal",true)); // 8
                v.add(dtCon1.getInt("stp_numcaj",true)); // 8     
                valorInc=false;
                if (verHist)
                {
                    ps.setInt(1,dtCon1.getInt("pro_codi"));
                    ps.setInt(2, dtCon1.getInt("eje_nume"));
                    ps.setString(3, dtCon1.getString("pro_serie"));
                    ps.setInt(4, dtCon1.getInt("pro_nupar"));
                    ps.setInt(5, dtCon1.getInt("pro_numind"));                  
                    ResultSet rs=ps.executeQuery();
                    if (rs.next())
                        valorInc=true;
                }
                v.add(valorInc); // 8 
                jt.addLinea(v);
            } while (dtCon1.next());
            if (verHist)
                actAcumulados();
            ifList.resetFechaHist();
            jt.tableView.setVisible(true);
            jt.requestFocusInicioLater();
            jt.setEnabled(true);
            mensaje("Consulta realizada...");
        } catch (SQLException k)
        {
            Error("Error al buscar stock-partida",k);
        }
    }
    void imprimir()
    {
      try
      {
          nLinGrid=-1;
          JT_nRows=jt.getRowCount();
          java.sql.Timestamp ahora=new java.sql.Timestamp(System.currentTimeMillis());
          ahora.setTime(System.currentTimeMillis());
          JasperReport jr = util.getJasperReport(EU,"stockPartidas");
          java.util.HashMap mp = new java.util.HashMap();
          mp.put("alm_codi",alm_codiE.getText());
          mp.put("alm_nomb",alm_codiE.getTextCombo());
          mp.put("fechaAct",ahora);
          JasperPrint jp = JasperFillManager.fillReport(jr, mp,this);
          if (EU.getSimulaPrint()) 
              return;
          gnu.chu.print.util.printJasper(jp, EU);
          guardaListado(ahora,alm_codiE.getValorInt());
      } catch (JRException | PrinterException ex)
      {
         Error("Error al imprimir listado",ex);
      }
    }
    
    private void guardaListado(java.sql.Timestamp ahora,int almacen)
    {
        try
        {
            for (int n=0;n<JT_nRows;n++)
            {
                if (jt.getValBoolean(n,JT_INSER))
                {
                    dtCon1.addNew("hislisstk");
                    dtCon1.setDato("hls_fecstk",ahora);
                    dtCon1.setDato("alm_codi",almacen);
                    dtCon1.setDato("pro_codi",jt.getValorInt(n,JT_ARTIC));
                    dtCon1.setDato("pro_ejelot",jt.getValorInt(n,JT_EJERC));
                    dtCon1.setDato("pro_serlot",jt.getValString(n,JT_SERIE));
                    dtCon1.setDato("pro_numlot",jt.getValorInt(n,JT_LOTE));
                    dtCon1.setDato("pro_indlot",jt.getValorInt(n,JT_INDI));
                    dtCon1.update();
                }
            }
            dtCon1.commit();            
            
        } catch (SQLException k)
        {
            Error("Error al guardar datos del listado",k);
        }
    }
    @Override
    public boolean next() throws JRException
    {
        nLinGrid++;
        while (!jt.getValBoolean(nLinGrid,JT_INSER) && nLinGrid < JT_nRows )
        {
            nLinGrid++;            
        }
        return nLinGrid < JT_nRows;
    }
  @Override
    public Object getFieldValue(JRField jrf) throws JRException
    {
        String campo = jrf.getName().toLowerCase(); 
        String campos[]=campo.split("_");
        int i=Integer.parseInt(campos[1]);
        String tipo=jrf.getValueClassName();
    
       
//            System.out.println("Linea: "+nLinGrid+"Campo:"+i+" Valor: "+jt.getValString(nLinGrid,i)+" Tipo: "+tipo);
       
        if (tipo.contains("Double"))
           return Double.parseDouble(jt.getValString(nLinGrid,i));        
        if (tipo.contains("Integer"))
           return jt.getValorInt(nLinGrid,i);
        return jt.getValString(nLinGrid,i);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        Pprinc = new gnu.chu.controles.CPanel();
        Pcabe = new gnu.chu.controles.CPanel();
        cLabel1 = new gnu.chu.controles.CLabel();
        pro_codiE = new gnu.chu.camposdb.proPanel();
        cLabel2 = new gnu.chu.controles.CLabel();
        alm_codiE = new gnu.chu.controles.CLinkBox();
        BBuscar = new gnu.chu.controles.CButtonMenu();
        jt = new gnu.chu.controles.Cgrid(11);
        Ppie = new gnu.chu.controles.CPanel();
        cLabel3 = new gnu.chu.controles.CLabel();
        numIndivE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        cLabel4 = new gnu.chu.controles.CLabel();
        kilosE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##,##9.9");
        cLabel5 = new gnu.chu.controles.CLabel();
        numCajasE = new gnu.chu.controles.CTextField(Types.DECIMAL,"####");
        cLabel6 = new gnu.chu.controles.CLabel();
        numPaletsE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9");
        Binv = new gnu.chu.controles.CButton(Iconos.getImageIcon("reload"));
        BPrint = new gnu.chu.controles.CButton(Iconos.getImageIcon("print"));

        Pprinc.setLayout(new java.awt.GridBagLayout());

        Pcabe.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Pcabe.setMaximumSize(new java.awt.Dimension(439, 52));
        Pcabe.setMinimumSize(new java.awt.Dimension(439, 52));
        Pcabe.setName(""); // NOI18N
        Pcabe.setPreferredSize(new java.awt.Dimension(439, 52));
        Pcabe.setLayout(null);

        cLabel1.setText("Articulo ");
        Pcabe.add(cLabel1);
        cLabel1.setBounds(10, 2, 60, 15);
        Pcabe.add(pro_codiE);
        pro_codiE.setBounds(70, 2, 340, 17);

        cLabel2.setText("Almacen");
        Pcabe.add(cLabel2);
        cLabel2.setBounds(10, 22, 60, 15);

        alm_codiE.setAncTexto(30);
        Pcabe.add(alm_codiE);
        alm_codiE.setBounds(70, 22, 240, 18);

        BBuscar.addMenu("Buscar","B");
        BBuscar.addMenu("Historico","H");
        BBuscar.setText("Buscar");
        Pcabe.add(BBuscar);
        BBuscar.setBounds(320, 20, 90, 24);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        Pprinc.add(Pcabe, gridBagConstraints);

        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jt.setMaximumSize(new java.awt.Dimension(539, 239));
        jt.setMinimumSize(new java.awt.Dimension(539, 239));
        jt.setPreferredSize(new java.awt.Dimension(539, 239));
        ArrayList v=new ArrayList();
        v.add("Artic"); // 0
        v.add("Nombre"); // 1
        v.add("Ejerc"); // 2
        v.add("Serie"); // 3
        v.add("Lote"); // 4
        v.add("Indiv"); // 5
        v.add("Peso"); // 6
        v.add("Unid."); // 7
        v.add("Palet"); // 8
        v.add("Caja"); // 9
        v.add("Inc"); // 9
        jt.setCabecera(v);
        jt.setAnchoColumna(new int[]    {50,120,40,30,50,30, 50,30, 35,35,30});
        jt.setAlinearColumna(new int[] {2,0,2,1,2,2, 2,2,2, 2,1});
        jt.setFormatoColumna(JT_INSER, "BSN");
        jt.setAjustarGrid(true);
        jt.resetRenderer(JT_INSER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Pprinc.add(jt, gridBagConstraints);

        Ppie.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        Ppie.setMaximumSize(new java.awt.Dimension(539, 22));
        Ppie.setMinimumSize(new java.awt.Dimension(539, 22));
        Ppie.setPreferredSize(new java.awt.Dimension(539, 22));
        Ppie.setLayout(null);

        cLabel3.setText("Num. Indiv");
        Ppie.add(cLabel3);
        cLabel3.setBounds(10, 4, 65, 17);

        numIndivE.setEnabled(false);
        Ppie.add(numIndivE);
        numIndivE.setBounds(80, 4, 40, 17);

        cLabel4.setText("Kilos");
        Ppie.add(cLabel4);
        cLabel4.setBounds(130, 4, 40, 17);

        kilosE.setEnabled(false);
        Ppie.add(kilosE);
        kilosE.setBounds(170, 4, 50, 17);

        cLabel5.setText("Cajas");
        Ppie.add(cLabel5);
        cLabel5.setBounds(240, 4, 40, 17);

        numCajasE.setEnabled(false);
        Ppie.add(numCajasE);
        numCajasE.setBounds(280, 4, 40, 17);

        cLabel6.setText("Palets");
        Ppie.add(cLabel6);
        cLabel6.setBounds(330, 4, 40, 17);

        numPaletsE.setEnabled(false);
        Ppie.add(numPaletsE);
        numPaletsE.setBounds(370, 4, 30, 17);

        Binv.setToolTipText("Invertir Selección");
        Ppie.add(Binv);
        Binv.setBounds(500, 4, 30, 19);

        BPrint.setText("Impr.");
        Ppie.add(BPrint);
        BPrint.setBounds(410, 4, 80, 20);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        Pprinc.add(Ppie, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButtonMenu BBuscar;
    private gnu.chu.controles.CButton BPrint;
    private gnu.chu.controles.CButton Binv;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Ppie;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CLinkBox alm_codiE;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.Cgrid jt;
    private gnu.chu.controles.CTextField kilosE;
    private gnu.chu.controles.CTextField numCajasE;
    private gnu.chu.controles.CTextField numIndivE;
    private gnu.chu.controles.CTextField numPaletsE;
    private gnu.chu.camposdb.proPanel pro_codiE;
    // End of variables declaration//GEN-END:variables
}
