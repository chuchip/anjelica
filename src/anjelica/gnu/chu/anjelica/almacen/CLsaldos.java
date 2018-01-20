/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gnu.chu.anjelica.almacen;

import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.listados.Listados;
import gnu.chu.anjelica.pad.MantFamPro;
import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.camposdb.proPanel;
import gnu.chu.controles.CButton;
import gnu.chu.controles.Cgrid;
import gnu.chu.controles.StatusBar;
import gnu.chu.controles.miCellRender;
import gnu.chu.interfaces.VirtualGrid;
import gnu.chu.interfaces.ejecutable;
import gnu.chu.print.util;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.mensajes;
import gnu.chu.utilidades.ventana;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.beans.PropertyVetoException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import javax.swing.JMenuItem;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

 
public class CLsaldos extends ventana implements JRDataSource
{
  String fecDomingo;
  proPanel pro_codmvE = new proPanel();
  ArrayList<DatIndiv>  listIndiv=new ArrayList();
  JMenuItem verIndiv = new JMenuItem("Ver Individuos", Iconos.getImageIcon("view_tree"));
  JMenuItem verMvtos = new JMenuItem("Ver Mvtos", Iconos.getImageIcon("view_tree"));
  private double kgVen,kgCom,kgReg=0;
  MvtosAlma mvtosAlm = new MvtosAlma();
  MvtosAlma mvtosDom = new MvtosAlma();
//  boolean valDesp;
  boolean cancelado=false;
  char sel='d';
  PreparedStatement ps;
  ResultSet rs;
  ifMvtosClase ifMvtos = new ifMvtosClase();
  IFStockPart ifStk=new IFStockPart(this);
  boolean imprList=false;
  int nLin;
  CButton Bimpr = new CButton(Iconos.getImageIcon("print"));
  String feulin;
  String s;
  private double kilos=0;
  int unid = 0;
  double precio=0;
  String camCodi;
  ResultSet dtProd; 
  Cgrid jtMv = new Cgrid(7);
  
  public CLsaldos(EntornoUsuario eu, Principal p) {
        EU = eu;
        vl = p.panel1;
        jf = p;
        eje = true;

        setTitulo("Cons./List Saldos");
        setAcronimo("clsaldo");
        try {
            if (jf.gestor.apuntar(this)) {       
                    jbInit();
            } else {
                setErrorInit(true);
            }
        } catch (Exception ex) {
            ErrorInit(ex);
            setErrorInit(true);
        }
    }

    public CLsaldos(gnu.chu.anjelica.menu p, EntornoUsuario eu) {

        EU = eu;
        vl = p.getLayeredPane();
        setTitulo("Cons./List Saldos");
        setAcronimo("clsaldo");
        eje = false;

        try {
            jbInit();
        } catch (Exception ex) {
            ErrorInit(ex);            
        }
        
    }

    private void jbInit() throws Exception {

        iniciarFrame(); 
       
        this.setVersion("2018-01-18");
        statusBar = new StatusBar(this);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        conecta();

        initComponents();
        confGrid(new ArrayList());
        this.setSize(new Dimension(640, 530));
        Bimpr.setPreferredSize(new Dimension(24,24));
        Bimpr.setMinimumSize(new Dimension(24,24));
        Bimpr.setMaximumSize(new Dimension(24,24));
        statusBar.add(Bimpr, new GridBagConstraints(9, 0, 1, 2, 0.0, 0.0
                                           , GridBagConstraints.EAST,
                                           GridBagConstraints.VERTICAL,
                                           new Insets(0, 5, 0, 0), 0, 0));
        this.getLayeredPane().add(ifMvtos);
        this.getLayeredPane().add(ifStk);
        ifMvtos.setLocation(5, 5);
        ifStk.setLocation(5,5);
        pro_codmvE.setEnabled(false);
        StatusBar stBar = new StatusBar(ifMvtos);
        ifMvtos.getContentPane().add(jtMv, BorderLayout.CENTER);
        ifMvtos.getContentPane().add(pro_codmvE, BorderLayout.NORTH);
        ifMvtos.getContentPane().add(stBar,BorderLayout.SOUTH);
         ifMvtos.setSize(new Dimension(475, 325));
    
        ifMvtos.setVisible(false);
        ifMvtos.setClosable(true);
        ifMvtos.setPadre(this);
        ifStk.setVisible(false);
        ifStk.setClosable(false);
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
        Pdatcon = new gnu.chu.controles.CPanel();
        cLabel1 = new gnu.chu.controles.CLabel();
        pro_codiE = new gnu.chu.camposdb.proPanel();
        cLabel2 = new gnu.chu.controles.CLabel();
        fecsalE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        opIgnDespSVal = new gnu.chu.controles.CCheckBox();
        cLabel3 = new gnu.chu.controles.CLabel();
        sbe_codiE = new gnu.chu.camposdb.sbePanel();
        cLabel4 = new gnu.chu.controles.CLabel();
        cam_codiE = new gnu.chu.controles.CLinkBox();
        opAjuCosto = new gnu.chu.controles.CCheckBox();
        cLabel5 = new gnu.chu.controles.CLabel();
        alm_inicE = new gnu.chu.controles.CLinkBox();
        cLabel6 = new gnu.chu.controles.CLabel();
        pro_artconE = new gnu.chu.controles.CComboBox();
        pro_cosincE = new gnu.chu.controles.CCheckBox();
        opIncSemana = new gnu.chu.controles.CCheckBox();
        cLabel7 = new gnu.chu.controles.CLabel();
        feulinE = new gnu.chu.controles.CComboBox();
        cLabel8 = new gnu.chu.controles.CLabel();
        ordenE = new gnu.chu.controles.CComboBox();
        Baceptar = new gnu.chu.controles.CButton(Iconos.getImageIcon("check"));
        jt = new gnu.chu.controles.Cgrid(7);
        Ppie = new gnu.chu.controles.CPanel();
        cLabel9 = new gnu.chu.controles.CLabel();
        kilosE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--,---,--9.99");
        cLabel10 = new gnu.chu.controles.CLabel();
        unidE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----,--9");
        cLabel11 = new gnu.chu.controles.CLabel();
        importeE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----,---,--9.99");

        Pprinc.setLayout(new java.awt.GridBagLayout());

        Pdatcon.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Pdatcon.setMaximumSize(new java.awt.Dimension(609, 79));
        Pdatcon.setMinimumSize(new java.awt.Dimension(609, 79));
        Pdatcon.setPreferredSize(new java.awt.Dimension(609, 79));
        Pdatcon.setLayout(null);

        cLabel1.setText("Incluir");
        Pdatcon.add(cLabel1);
        cLabel1.setBounds(290, 40, 50, 17);
        Pdatcon.add(pro_codiE);
        pro_codiE.setBounds(70, 2, 330, 17);

        cLabel2.setText("Producto");
        Pdatcon.add(cLabel2);
        cLabel2.setBounds(10, 2, 60, 17);
        Pdatcon.add(fecsalE);
        fecsalE.setBounds(75, 20, 65, 17);

        opIgnDespSVal.setSelected(true);
        opIgnDespSVal.setText("Ign. Desp. SV");
        opIgnDespSVal.setToolTipText("Ignorar despieces sin valorar");
        Pdatcon.add(opIgnDespSVal);
        opIgnDespSVal.setBounds(490, 0, 100, 17);

        cLabel3.setText("Orden");
        Pdatcon.add(cLabel3);
        cLabel3.setBounds(210, 60, 50, 17);
        Pdatcon.add(sbe_codiE);
        sbe_codiE.setBounds(280, 20, 40, 17);

        cLabel4.setText("Seccion");
        Pdatcon.add(cLabel4);
        cLabel4.setBounds(230, 20, 50, 17);

        cam_codiE.setAncTexto(30);
        Pdatcon.add(cam_codiE);
        cam_codiE.setBounds(380, 20, 210, 17);

        opAjuCosto.setText("Aj. Costos");
        opAjuCosto.setToolTipText("Ignorar despieces sin valorar");
        Pdatcon.add(opAjuCosto);
        opAjuCosto.setBounds(410, 0, 80, 17);

        cLabel5.setText("En Fecha");
        Pdatcon.add(cLabel5);
        cLabel5.setBounds(10, 20, 60, 17);

        alm_inicE.setAncTexto(30);
        Pdatcon.add(alm_inicE);
        alm_inicE.setBounds(70, 40, 210, 17);

        cLabel6.setText("Camara");
        Pdatcon.add(cLabel6);
        cLabel6.setBounds(330, 20, 50, 17);

        pro_artconE.addItem("TODOS","0");
        pro_artconE.addItem("Congelado","1");
        pro_artconE.addItem("NO Congel.","2");
        Pdatcon.add(pro_artconE);
        pro_artconE.setBounds(340, 40, 100, 17);

        pro_cosincE.setSelected(true);
        pro_cosincE.setText("Inc. Costo");
        pro_cosincE.setToolTipText("Ignorar despieces sin valorar");
        Pdatcon.add(pro_cosincE);
        pro_cosincE.setBounds(400, 60, 90, 17);

        opIncSemana.setSelected(true);
        opIncSemana.setText("Inc. Semana");
        opIncSemana.setToolTipText("Mostrar todos los productos que hayan tenido stock en la semana");
        Pdatcon.add(opIncSemana);
        opIncSemana.setBounds(140, 20, 88, 17);

        cLabel7.setText("Almacen");
        Pdatcon.add(cLabel7);
        cLabel7.setBounds(10, 40, 60, 17);
        Pdatcon.add(feulinE);
        feulinE.setBounds(100, 60, 100, 17);

        cLabel8.setText("Ult. Inventario");
        Pdatcon.add(cLabel8);
        cLabel8.setBounds(10, 60, 90, 17);

        ordenE.addItem("Producto", "P");
        ordenE.addItem("Familia", "F");
        Pdatcon.add(ordenE);
        ordenE.setBounds(260, 60, 130, 17);

        Baceptar.setText("Aceptar");
        Pdatcon.add(Baceptar);
        Baceptar.setBounds(490, 50, 100, 24);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        Pprinc.add(Pdatcon, gridBagConstraints);

        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jt.setMaximumSize(new java.awt.Dimension(100, 109));
        jt.setMinimumSize(new java.awt.Dimension(100, 109));
        jt.setPreferredSize(new java.awt.Dimension(100, 109));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 609;
        gridBagConstraints.ipady = 139;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Pprinc.add(jt, gridBagConstraints);

        Ppie.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        Ppie.setMaximumSize(new java.awt.Dimension(419, 21));
        Ppie.setMinimumSize(new java.awt.Dimension(419, 21));
        Ppie.setPreferredSize(new java.awt.Dimension(419, 21));
        Ppie.setLayout(null);

        cLabel9.setText("Kilos");
        Ppie.add(cLabel9);
        cLabel9.setBounds(12, 2, 40, 17);

        kilosE.setEditable(false);
        Ppie.add(kilosE);
        kilosE.setBounds(50, 2, 80, 17);

        cLabel10.setText("Unidades");
        Ppie.add(cLabel10);
        cLabel10.setBounds(140, 2, 60, 17);

        unidE.setEditable(false);
        Ppie.add(unidE);
        unidE.setBounds(200, 2, 60, 17);

        cLabel11.setText("Importe");
        Ppie.add(cLabel11);
        cLabel11.setBounds(270, 2, 60, 17);

        importeE.setEditable(false);
        Ppie.add(importeE);
        importeE.setBounds(330, 2, 80, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        Pprinc.add(Ppie, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CPanel Pdatcon;
    private gnu.chu.controles.CPanel Ppie;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CLinkBox alm_inicE;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel10;
    private gnu.chu.controles.CLabel cLabel11;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.controles.CLabel cLabel9;
    private gnu.chu.controles.CLinkBox cam_codiE;
    private gnu.chu.controles.CTextField fecsalE;
    private gnu.chu.controles.CComboBox feulinE;
    private gnu.chu.controles.CTextField importeE;
    private gnu.chu.controles.Cgrid jt;
    private gnu.chu.controles.CTextField kilosE;
    private gnu.chu.controles.CCheckBox opAjuCosto;
    private gnu.chu.controles.CCheckBox opIgnDespSVal;
    private gnu.chu.controles.CCheckBox opIncSemana;
    private gnu.chu.controles.CComboBox ordenE;
    private gnu.chu.controles.CComboBox pro_artconE;
    private gnu.chu.camposdb.proPanel pro_codiE;
    private gnu.chu.controles.CCheckBox pro_cosincE;
    private gnu.chu.camposdb.sbePanel sbe_codiE;
    private gnu.chu.controles.CTextField unidE;
    // End of variables declaration//GEN-END:variables

  private void confGrid(ArrayList v)
  {
    v.add("Prod"); // 0
    v.add("Nombre"); // 1
    v.add("Unid"); // 2
    v.add("Kilos"); // 3
    v.add("Precio"); // 4
    v.add("Importe"); // 5
    v.add("Fam"); // 6
    jt.setCabecera(v);
    jt.setAnchoColumna(new int[]{50,150,40,60,60,70,30});
    jt.setAlinearColumna(new int[]{2,0,2,2,2,2,2});
    jt.setMaximumSize(new Dimension(603, 223));
    jt.setMinimumSize(new Dimension(603, 223));
    jt.setFormatoColumna(2,"---9");
    jt.setFormatoColumna(3,"---,--9.99");
    jt.setFormatoColumna(4,"---9.99");
    jt.setFormatoColumna(5,"---,--9.99");
    jt.setAjustarGrid(true);
    jt.getPopMenu().add(verIndiv,1);
    jt.getPopMenu().add(verMvtos,2);
    jt.setToolTipText("Click en boton derecho para más opciones");
    cgclisaldos vg=new cgclisaldos();
    for (int n=0;n<jt.getColumnCount();n++)
    {
        miCellRender mc= jt.getRenderer(n);
        if (mc==null)
            continue;
        mc.setVirtualGrid(vg);
        mc.setErrBackColor(Color.CYAN);
        mc.setErrForeColor(Color.BLACK);
    }


    ArrayList v1=new ArrayList();
    v1.add("Fecha");
    v1.add("Tipo"); // 0 
    v1.add("Kg.Entr.");// 1
    v1.add("Un.Entr");// 2
    v1.add("Kg. Sal");// 3
    v1.add("Un Sal");// 4
    v1.add("Precio");// 5
    jtMv.setCabecera(v1);
    jtMv.setAjustarGrid(true);
    jtMv.setAnchoColumna(new int[]{90,50,80,70,80,70,70});
    jtMv.setAlinearColumna(new int[]{1,1,2,2,2,2,2});
    
    jtMv.setFormatoColumna(2,"---,--9.99");
    jtMv.setFormatoColumna(3,"---,--9");
    jtMv.setFormatoColumna(4,"---,--9.99");
    jtMv.setFormatoColumna(5,"---,--9");   
    jtMv.setFormatoColumna(6,"-,--9.99");
    jtMv.setAjustarGrid(true);
  }

    @Override
  public void iniciarVentana() throws Exception
  {
    Pdatcon.setDefButton(Baceptar);
    cam_codiE.setFormato(Types.CHAR, "XX", 2);
    cam_codiE.texto.setMayusc(true);
    pro_codiE.iniciar(dtStat,this,vl,EU);
    pro_codmvE.iniciar(dtStat,this,vl,EU);
    sbe_codiE.iniciar(dtStat, this, vl, EU);
    sbe_codiE.setTipo("A");
    sbe_codiE.setValorInt(0);
    sbe_codiE.setAceptaNulo(true);
//    activar(true);

    s="select distinct(rgs_fecha) as cci_feccon from v_inventar as r "+
         " where r.emp_codi = "+EU.em_cod+
         " order by cci_feccon desc ";

     if (dtStat.select(s))
     {
       feulin = dtStat.getFecha("cci_feccon", "dd-MM-yyyy");
       do
       {
         feulinE.addItem(dtStat.getFecha("cci_feccon","dd-MM-yyyy"),dtStat.getFecha("cci_feccon","dd-MM-yyyy"));
       } while (dtStat.next());
     }
     else
     {
       feulin = "01-01-" + EU.ejercicio; // Buscamos desde el principio del a�o.
       feulinE.addItem(feulin);
     }
     feulinE.setText(feulin);
     pdconfig.llenaDiscr(dtStat,cam_codiE,"AC",EU.em_cod);

    alm_inicE.setFormato(true);
    alm_inicE.setFormato(Types.DECIMAL,"#9",2);
    
    
    pdalmace.llenaLinkBox(alm_inicE, dtStat,'*');
//    s="SELECT alm_codi,alm_nomb FROM V_ALMACen ORDER BY alm_codi ";
//    dtStat.select(s);
//    alm_inicE.addDatos(dtStat);
  
    activarEventos();
    fecsalE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
    fecsalE.requestFocusLater();
  }
  void activarEventos()
  {
    jt.addMouseListener(new MouseAdapter()
      {
          @Override
          public void mouseClicked(MouseEvent e) {
              if (e.getClickCount() < 2 || jt.isVacio())
                  return;            
              mostrarMvtos();
          }
      });
    verIndiv.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (jt.isVacio()) 
             return;
        
          try
          {
              verIndividuos(jt.getValorInt(jt.getSelectedRowDisab(), 0));
          } catch (SQLException ex)
          {
              Error("Error al localizar individuos",ex);
          }
      }
    });
    Baceptar.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
       Baceptar_actionPerformed(threadCLsaldos.CONSULTA);
      }
    });
    Bimpr.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        Baceptar_actionPerformed(threadCLsaldos.LISTA);
      }
    });
    popEspere_BCancelaraddActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        msgEspere("Espere, por favor ... Cancelando Consulta");
        popEspere_BCancelarSetEnabled(false);
        cancelado=true;
      }
    });
    fecsalE.addFocusListener(new FocusAdapter()
    {
            @Override
        public void focusLost(FocusEvent e) {
         try {
          if (fecsalE.isNull() || fecsalE.getError())
              return;
         int nLin=feulinE.getItemCount()-1;
         for (int n=nLin;n>=0;n--)
         {
            if (((String) feulinE.getItemAt(n)).equals(fecsalE.getText()))
            {
                feulinE.setValor((String) feulinE.getItemAt(n));
                return;
            }
            if (Formatear.restaDias((String) feulinE.getItemAt(n), fecsalE.getText())>0)
            {
                feulinE.setValor((String) feulinE.getItemAt(n+1));
                return;
            }
         }
         feulinE.setValor((String) feulinE.getItemAt(0));
         } catch(Exception k )
         {
             Error("Error al buscar fecha",k);
         }
     }
    });
    verMvtos.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (jt.isVacio()) 
             return;
        verMvtos();
      }
    });
   
  }
  void mostrarMvtos() {
    ejecutable prog;
    if ((prog = jf.gestor.getProceso(Comvalm.getNombreClase())) == null)
        return;
    gnu.chu.anjelica.almacen.Comvalm cm = (gnu.chu.anjelica.almacen.Comvalm) prog;

    cm.setProCodi(jt.getValorInt(jt.getSelectedRowDisab(),0));
    cm.setLote(0);
    cm.setIndividuo(0);
    cm.setSerie("");
    cm.setEjercicio(0);
    cm.ejecutaConsulta();
    jf.gestor.ir(cm);
  }
  /**
   * Muestra los individuos desglosados en stock.
   * @param proCodi 
   */
  void verIndividuos(int proCodi) throws SQLException
  {
    listIndiv.clear();
    
    iniciarStatement(proCodi, feulinE.getValor(),fecsalE.getText());
    rs = ps.executeQuery();
    if (!rs.next())
        msgBox("No encontrados invidivuos");
    ifStk.setVisible(true);
    DatIndiv dtInd;
    int row;
    do
    {
        dtInd=new DatIndiv();
        dtInd.setAlmCodi(rs.getInt("alm_codi"));
        dtInd.setProducto(proCodi);
        dtInd.setEjercLot(rs.getInt("pro_ejelot"));
        dtInd.setSerie(rs.getString("pro_serlot"));
        dtInd.setLote(rs.getInt("pro_numlot"));
        dtInd.setNumind(rs.getInt("pro_indlot"));
       
        row=listIndiv.indexOf(dtInd);
        if (row>=0)
            dtInd=listIndiv.get(row);
        dtInd.setCanti((rs.getString("tipmov").equals("=")?0:dtInd.getCanti())+
            rs.getDouble("canti")* (rs.getString("tipmov").equals("S")?-1:1 ));
        dtInd.setNumuni((rs.getString("tipmov").equals("=")?0:dtInd.getNumuni())+
            rs.getInt("unid")* (rs.getString("tipmov").equals("S")?-1:1 ));
         if (row>=0)
             listIndiv.set(row,dtInd);
         else
            listIndiv.add(dtInd);
    } while (rs.next());
    ifStk.iniciar(proCodi,feulinE.getValor(),fecsalE.getText(), listIndiv);
//    for (DatIndiv dtInd1 : listIndiv)
//    {
//        if (dtInd1.getCanti()==0)
//            continue;
//        System.out.println("indiv: "+dtInd1.getProducto()+" "+
//            dtInd1.getEjercLot()+ dtInd1.getSerie()+dtInd1.getLote()+" "+dtInd1.getNumind()+ ":"+
//            dtInd1.getCanti()+" kg "+dtInd1.getNumuni());
//    }

  }
  /**
   * LLamada cuando se hace doble click en una linea.
   * Muestra un detalle de los movimientos realizados.
   */
  void verMvtos()
  {     
      ifMvtos.setVisible(true);
      int proCodi = jt.getValorInt(jt.getSelectedRowDisab(), 0);
      String fecinv = fecsalE.getText();
      pro_codmvE.setValorInt(proCodi);
      jtMv.removeAllDatos();
      ArrayList v = new ArrayList();
      try
      {
          iniciarStatement(proCodi, feulin, fecinv);
//   debug("verMvtos: "+s);
          rs = ps.executeQuery();
          if (!rs.next())
              return;
          dtCon1.setResultSet(rs);

          unid = 0;
          do
          {
              v.clear();
              v.add(dtCon1.getFecha("fecmov","dd-MM-yy HH:mm"));
              switch (dtCon1.getString("tipmov"))
              {
                  case "=":
                      v.add("=");
                      v.add(dtCon1.getString("canti"));
                      v.add(dtCon1.getString("unid"));
                      v.add(0);
                      v.add(0);                                        
                      break;
                  case "E":  
                    v.add(dtCon1.getString("sel"));
                    v.add(dtCon1.getString("canti"));
                    v.add(dtCon1.getString("unid"));
                    v.add(0);
                    v.add(0);
                    break;
                  default:              
                    v.add(dtCon1.getString("sel"));
                    v.add(0);
                    v.add(0);
                    v.add(dtCon1.getString("canti"));
                    v.add(dtCon1.getString("unid"));
              }
              v.add(dtCon1.getString("precio"));
              jtMv.addLinea(v);
          } while (dtCon1.next());

      } catch (SQLException k)
      {
          Error("Error al ver Mvtos desglosados", k);
      }
  }
  void Baceptar_actionPerformed(int opcion)
  {
    ps=null;
    ifMvtos.setVisible(false);
    ifStk.setVisible(false);
    if (fecsalE.isNull())
    {
      mensajeErr("Introduzca Fecha de Saldo");
      return;
    }
    if (fecsalE.getText().equals(feulinE.getText()) && pro_cosincE.isSelected())
    {
        int ret=mensajes.mensajeYesNo("Fecha Salida es la de Inventario. Incrementar costo seguro?");
        if (ret!=mensajes.YES)
        {
            pro_cosincE.setSelected(false);
            return;
        }
    }
    camCodi=cam_codiE.getText().trim();
    if (camCodi.equals(""))
      camCodi=null;
    if (camCodi!=null)
    {
      camCodi = camCodi.replace('*', '%');
      if (camCodi.equals("*") || camCodi.equals("**"))
        camCodi = null;
    }

//    almOri=Integer.parseInt(alm_inicE.getText().trim());
//    almFin=Integer.parseInt(alm_finalE.getText().trim());
    
    feulin = feulinE.getValor();
   
    threadCLsaldos th =new threadCLsaldos(this,opcion);
    th.start();
  }

  boolean consultar()
  {
    msgEspere("Calculando Saldos...");
    popEspere_BCancelarSetEnabled(true);
    
    cancelado=false;
    mensaje("A esperar que estoy  buscando datitos ...");
//    activar(false);
    try
    {
      if (opIncSemana.isSelected())
      {
           GregorianCalendar gc = new GregorianCalendar();
           gc.setTime(fecsalE.getDate());
           gc.set(GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.SUNDAY );
           if (Formatear.comparaFechas(gc.getTime(),fecsalE.getDate() )>0)
               fecDomingo=Formatear.sumaDias(gc.getTime(), -7);
           else
               fecDomingo=Formatear.getFecha(gc.getTime(),"dd-MM-yyyy");
           mvtosDom.setUsaDocumentos(false);
           mvtosDom.setIncUltFechaInv(false);
           mvtosDom.setIgnDespSinValor(opIgnDespSVal.isSelected());
           mvtosDom.setAlmacen(alm_inicE.getValorInt());
           mvtosDom.setEntornoUsuario(EU);
      //      mvtosAlm.setIncAjusteCostos(opAjuCosto.isSelected());
           mvtosDom.setSoloInventario(false);
           mvtosDom.setIncluyeSerieX(false);
           mvtosDom.iniciarMvtos(feulin,fecDomingo,dtCon1);
      }
      mvtosAlm.setUsaDocumentos(false);
      mvtosAlm.setIncUltFechaInv(fecsalE.getText().equals(feulinE.getText()));
      mvtosAlm.setIgnDespSinValor(opIgnDespSVal.isSelected());
      mvtosAlm.setAlmacen(alm_inicE.getValorInt());
      mvtosAlm.setEntornoUsuario(EU);
//      mvtosAlm.setIncAjusteCostos(opAjuCosto.isSelected());
      mvtosAlm.setSoloInventario(fecsalE.getText().equals(feulinE.getText()));
      
      if (!alm_inicE.isNull())
          mvtosAlm.setIncluyeSerieX(true);
      mvtosAlm.iniciarMvtos(feulin,fecsalE.getText(),dtCon1);
//      mvtosAlm.setDesglIndiv(true);
      char orden=ordenE.getValor().charAt(0);
      jt.removeAllDatos();
      s = "SELECT a.*,f.fpr_nomb FROM V_ARTICULO as a left join v_famipro as f on f.fpr_codi = a.fam_codi where 1=1  " +
          (camCodi != null ? " and a.cam_codi= '" + camCodi + "'" : "") +
          (pro_codiE.isNull()?"":" and a.pro_codi = "+pro_codiE.getValorInt())+
          (pro_artconE.getValorInt()==0?"": " and a.pro_artcon "+(pro_artconE.getValorInt()==1?"!":"")+"=0")+
          (sbe_codiE.getValorInt()==0?"": " and a.sbe_codi = "+sbe_codiE.getValorInt())+
          " and a.pro_tiplot='V' "+
          " ORDER BY "+(orden=='F'?" fam_codi,":"")+
          " pro_codi ";
      dtProd = stUp.executeQuery(s);
      int unidT=0;
      double kilosT=0,importeT=0;
      double kgVenT=0,kgComT=0,kgRegT=0;
     
      ArrayList<ArrayList> datos=new ArrayList();
      int famCodi=0;
      while (next())
      {
        if (cancelado)
        {
          mensajeErr("Consulta Cancelada ...");
          
          mensaje("");
          resetMsgEspere();
//          activar(true);
          pro_codiE.requestFocus();
          return false;
        }
        if (famCodi!=dtProd.getInt("fam_codi") && orden=='F' )
        {
           ArrayList v1 = new ArrayList();  
           v1.add("");
           v1.add("Fam: "+dtProd.getString("fpr_nomb"));
           v1.add("" );
           v1.add("" );
           v1.add("" );
           v1.add("" );
           v1.add("");
           datos.add(v1);
           famCodi=dtProd.getInt("fam_codi");
        }
        ArrayList v = new ArrayList();
        v.add(dtProd.getString("pro_codi"));
        v.add(dtProd.getString("pro_nomb"));
        v.add( unid);
        v.add(kilos);
        v.add( precio);
        v.add(kilos * precio);
        v.add(famCodi);
        datos.add(v);
        kilosT+=kilos;
        unidT+=unid;
        kgComT+=kgCom;
        kgVenT+=kgVen;
        kgRegT+=+kgReg;
        importeT+=(kilos * precio);
      }
      jt.setDatos(datos);
      jt.requestFocusInicio();
      kilosE.setValorDec(kilosT);
      unidE.setValorDec(unidT);
      importeE.setValorDec(importeT);
//        System.out.println("Kilos venta: "+kgVenT+" Kg.Compra: "+kgComT+" Kg. Reg:"+kgRegT);
      resetMsgEspere();
    //  mensaje("Pulse Doble click en una linea para ver los movimientos");
      mensajeErr("Consulta .... Generada");
//      activar(true);
      Pdatcon.resetCambio();
      pro_codiE.requestFocus();
    }
    catch (SQLException | ParseException | JRException ex)
    {
      Error("Error al buscar Productos", ex);
    }
    return true;
  }

  void listar()
  {
    try
    {
      msgEspere("Generando Listado.. espere, por favor");
      popEspere_BCancelarSetEnabled(false);
      if (Pdatcon.hasCambio() || cancelado)
      {
        if (! consultar())
            return;
      }
      mensaje("Generando listado ...");
//      activar(false);
      
      nLin=-1;
      imprList=true;
      JasperReport jr = Listados.getJasperReport(EU, "lisaldos");

      HashMap mp = Listados.getHashMapDefault();
      mp.put("fecini",feulin);
      mp.put("fecsal",fecsalE.getText());
      mp.put("ordenFam", ordenE.getValor().equals("F"));
      if (camCodi==null)
        mp.put("camara", "*TODAS*");
      else
        mp.put("camara",
               cam_codiE.getText() + " -> " + cam_codiE.getTextCombo());

      JasperPrint jp = JasperFillManager.fillReport(jr, mp, this);
      mensaje("");


      if (util.printJasper(jp, EU))
            mensajeErr("Listado .... Generado");
      
      mensaje("");
      
      resetMsgEspere();

      imprList=false;
    }
    catch (JRException | PrinterException ex)
    {
      Error("Error al buscar Productos",ex);
    }
  }

    @Override
  public boolean next() throws JRException
  {
    try {
      if (imprList)
      {
        nLin++;
        if (nLin>=jt.getRowCount())
            return false;
        if (jt.getValString(nLin,0).equals(""))
            nLin++;
        
        return true;
      }
      if (! dtProd.next())
        return false;
      setMensajePopEspere("Ejecutando Consulta de Saldos\nTratando producto "+dtProd.getInt("pro_codi"),false);

      precio=0;
      while (true)
      {
        
        if (! mvtosAlm.calculaMvtos(dtProd.getInt("pro_codi"), dtCon1, dtStat, null,null))
        {                
           if (! dtProd.next())
               return false;
           continue;
        }
        kilos=mvtosAlm.getKilosStock();
        unid=mvtosAlm.getUnidStock();
        kgCom=mvtosAlm.getKilosCompra();
        kgVen=mvtosAlm.getKilosVenta();
        kgReg=mvtosAlm.getKilosRegul();
        precio = mvtosAlm.getPrecioStock()+
                (pro_cosincE.isSelected()?dtProd.getDouble("pro_cosinc"):0) ;
                //getPreMed(dtProd.getInt("pro_codi"), fecsalE.getText());
//        if (precio != 0 && (kilos >= 1 || kilos <= -1))
        if ( (kilos >= 1 || kilos <= -1))
          return true;
        if (opIncSemana.isSelected())
        {
              if (! mvtosDom.calculaMvtos(dtProd.getInt("pro_codi"), dtCon1, dtStat, null,null))
              {
                 if (! dtProd.next())
                    return false;
                 continue;             
              }
              kilos = 0;
              unid = 0;
              kgCom = mvtosDom.getKilosCompra();
              kgVen = 0;
              kgReg = mvtosDom.getKilosRegul();
              precio = mvtosDom.getPrecioStock()
                  + (pro_cosincE.isSelected() ? dtProd.getDouble("pro_cosinc") : 0);
              if (mvtosDom.getKilosStock() >= 1)
                  return true;        
        }    
        if (! dtProd.next())
          return false;
      }

    } catch (Exception k)
    {
      Error("Error en Next: ",k);
      throw new JRException(k.getMessage());
    }
  }

    @Override
  public Object getFieldValue(JRField field) throws JRException
  {
    try
    {
      if (field.getName().equals("pro_codi"))
        return jt.getValorInt(nLin,0);
       if (field.getName().equals("fam_codi"))
        return jt.getValorInt(nLin,6);
       if (field.getName().equals("fpr_nomb"))
        return MantFamPro.getNombreFam(jt.getValorInt(nLin,6),dtStat);
      if (field.getName().equals("pro_nomb"))
        return jt.getValString(nLin,1);
      if (field.getName().equals("kilos"))
        return jt.getValorDec(nLin,3);
      if (field.getName().equals("unid"))
        return jt.getValorInt(nLin,2);
      if (field.getName().equals("precio"))
        return jt.getValorDec(nLin,4);
      if (field.getName().equals("importe"))
        return jt.getValorDec(nLin,5);
      throw new JRException("Field: "+field.getName()+" NO valido");
    }
    catch (NumberFormatException n)
    { // Para cuando salen infinitos y cosas asi
      return (double) 0;
    }
    catch (SQLException | JRException k)
    {
      Error("Error en getFieldValue: ("+field.getName()+")", k);
      throw new JRException(k.getMessage());
    }

  }
  

  private void iniciarStatement(int proCodi, String fecini, String fecfin) throws SQLException
  {
    
    if (ps==null)
    {
       String sql="SELECT  mvt_tipdoc as sel, mvt_tipo as tipmov,mvt_time as fecmov, alm_codi, pro_codi,"
           + "pro_ejelot,pro_serlot,pro_numlot,pro_indlot,"+
            " "+        
            " mvt_canti as canti,mvt_prec as precio "+
            ", mvt_unid as unid " +
            " from mvtosalm where "+
            " mvt_canti <> 0 "+
            " AND pro_codi = ?" +
              (alm_inicE.getValorInt() == 0 ? "" : " and alm_codi = " + alm_inicE.getValorInt()) +
             " AND mvt_time::date >= TO_DATE('" + fecini + "','dd-MM-yyyy') " +
          "   and  mvt_time::date <= TO_DATE('" + fecfin + "','dd-MM-yyyy') "+
          " UNION all " + // Inventarios
          " select 'RE' as sel,tir_afestk as tipmov,rgs_fecha as fecmov,alm_codi, pro_codi, eje_nume as pro_ejelot,"
           + "pro_serie as pro_serlot,pro_nupar as pro_numlot, pro_numind as pro_indlot,"+
          " r.rgs_kilos as canti,r.rgs_prregu as precio,1 as unid  " +
          " FROM v_regstock as r WHERE " +
          " tir_afestk = '='"+
          " and rgs_kilos <> 0" +
          " and rgs_trasp != 0 "+
          (alm_inicE.getValorInt() == 0 ? "" : " and alm_codi = " + alm_inicE.getValorInt()) +
          " AND r.pro_codi = ? " +
          " AND r.rgs_fecha::date >= TO_DATE('" + fecini + "','dd-MM-yyyy') " +
          " and r.rgs_fecha::date <= TO_DATE('" + fecfin + "','dd-MM-yyyy') ";
      sql += " ORDER BY 3,2 desc"; // FECHA y tipo
      ps=dtCon1.getConexion().prepareStatement(dtCon1.getStrSelect(sql));
    }
    ps.setInt(1,proCodi);
    ps.setInt(2,proCodi);   
  }

    @Override
  public void matar(boolean cerrarConexion)
 {
   if (muerto)
     return;
   if (ifMvtos!=null)
   {
     ifMvtos.setVisible(false);
     ifMvtos.dispose();
   } 
   ifStk.setVisible(false);
   ifStk.dispose();
   super.matar(cerrarConexion);
 }

}

class threadCLsaldos extends Thread
{
  final static int CONSULTA=1;
  final static int LISTA=2;
  CLsaldos lisal;
  int opcion; 
  
  public threadCLsaldos(CLsaldos lisal,int opcion)
  {
    this.lisal=lisal;
    this.opcion=opcion;
  }
    @Override
  public void run()
  {
   
    this.setPriority(Thread.MAX_PRIORITY-2);
    if (opcion==CONSULTA)
      lisal.consultar();
    if (opcion==LISTA)
      lisal.listar();
    lisal.mensaje("");
  }
}
class cgclisaldos implements VirtualGrid
{
 @Override
 public boolean getColorGrid(int row, int col, Object valor, boolean selecionado, String nombreGrid)
 {
     return  (col==1 && ((String) valor).startsWith("Fam:"));             
 }
}

class  ifMvtosClase extends ventana
{
      CLsaldos papa;
      public void setPadre(CLsaldos padre)
      {
          papa=padre;
          this.setTitle("Consulta Mvtos de Prod.");
      }
       @Override
        public void matar()
        {
          setVisible(false);
    
          papa.setEnabled(true);
          papa.setFoco(null);
          try {
              papa.setSelected(true);
          } catch (PropertyVetoException k){}
        }  
  }



