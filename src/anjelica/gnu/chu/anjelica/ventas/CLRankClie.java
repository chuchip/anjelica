package gnu.chu.anjelica.ventas;

/**
 * <p>Titulo: Consulta Ranking de clientes</p>
 * <p>Descripcion: Consulta/Listado de Ranking de clientes</p>
* <p>Copyright: Copyright (c) 2005-2012
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
 * <p>Empresa: micasa</p>
 * @author chuchiP
 * @version 1.0
 */
import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.pad.MantRepres;
import gnu.chu.anjelica.pad.pdclien;
import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.camposdb.empPanel;
import gnu.chu.controles.StatusBar;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.mensajes;
import gnu.chu.utilidades.miThread;
import gnu.chu.utilidades.ventana;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CLRankClie extends ventana {
  String REPRARG;
  String s;
  ArrayList<ArrayList> datos;
  AlbClien alCli=null;
  private String condWhere;
  public CLRankClie(EntornoUsuario eu, Principal p)
  {
    this(eu, p, null);
  }

  public CLRankClie(EntornoUsuario eu, Principal p, Hashtable ht)
  {

   EU=eu;
   vl=p.panel1;
   jf=p;
   eje=true;

   setTitulo("Consulta Ranking Clientes");

   try
   {
     if (ht != null)
     {
       if (ht.get("repr") != null)
         REPRARG = ht.get("repr").toString();
     }

     if(jf.gestor.apuntar(this))
         jbInit();
      else
        setErrorInit(true);
   }
   catch (Exception e) {
     Logger.getLogger(CLRankClie.class.getName()).log(Level.SEVERE, null, e);
     setErrorInit(true);
   }
 }

 public CLRankClie(gnu.chu.anjelica.menu p,EntornoUsuario eu) {

   EU=eu;
   vl=p.getLayeredPane();
   setTitulo("Consulta Ranking Clientes");
   eje=false;

   try  {
     jbInit();
   }
   catch (Exception e) {
      Logger.getLogger(CLRankClie.class.getName()).log(Level.SEVERE, null, e);
     setErrorInit(true);
   }
 }



private void jbInit() throws Exception
{
   iniciarFrame();

   this.setVersion("2012-05-23");
   statusBar = new StatusBar(this);

   initComponents();
   this.setSize(620,542);
   this.getContentPane().add(statusBar, BorderLayout.SOUTH);

   conecta();
}
    @Override
public void iniciarVentana() throws Exception
{
    Pcabe.setDefButton(Baceptar.getBotonAccion());
    bdiscr.iniciar(dtStat, this, vl, EU);
    jt.tableView.setToolTipText("Doble click encima linea para detalles venta");
   
    emp_codiE.iniciar(dtStat, this, vl, EU);
    emp_codiE.setAceptaNulo(empPanel.hasAccesoTotal(dtStat, EU.usuario));
    sbe_codiE.iniciar(dtStat, this, vl, EU);
    sbe_codiE.setAceptaNulo(true);
    sbe_codiE.setFieldEmpCodi(emp_codiE.getTextField());

    MantRepres.llenaLinkBox(rep_codiE, dtCon1);
    fecIniE.iniciar(dtStat,this,vl,EU);
    fecFinE.iniciar(dtStat, this, vl, EU);
//    fecIniE.setDesplazaX(150);
//    fecFinE.setDesplazaX(150);
    zon_codiE.getComboBox().setPreferredSize(new Dimension(150,18));
    rep_codiE.getComboBox().setPreferredSize(new Dimension(150,18));
//    pdconfig.llenaDiscr(dtStat, rep_codiE, "Cr",EU.em_cod);
    pdconfig.llenaDiscr(dtStat, zon_codiE, "Cz",EU.em_cod);
     activarEventos();
//     cli_codiE.setText("");
     this.setEnabled(true);
     fecIniE.requestFocus();

     fecFinE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
     GregorianCalendar gc = new GregorianCalendar();
     gc.setTime(fecFinE.getDate());

     for (int n=0;n<7;n++)
     {
        if (gc.get(GregorianCalendar.DAY_OF_WEEK)==GregorianCalendar.MONDAY)
            break;
        gc.setTime(Formatear.sumaDiasDate(gc.getTime(),-1));
     }
     fecIniE.setDate(gc.getTime());
     emp_codiE.setValorInt(0);
     sbe_codiE.setValorInt(0);

//     REPRARG="MA";
     if (REPRARG!=null)
     {
         rep_codiE.setText(REPRARG);
         rep_codiE.setEnabled(false);
     }
    }
    private void activarEventos()
    {
        Baceptar.addActionListener(new java.awt.event.ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Baceptar_actionPerformed(e);
            }
        });
        fecIniE.addFocusListener(new java.awt.event.FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                feciniE_focusLost(e);
            }
        });
        jt.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                 if (jt.isVacio())
                    return;
                 if (e.getClickCount()<2)
                    return;
                busAlbCli();
            }
        });
    }

    void feciniE_focusLost(FocusEvent e)
    {
        try
        {
            if (e.isTemporary())
                return;
            if (fecIniE.getText().equals("") || !fecFinE.isNull())
                return;
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(fecIniE.getDate());
            gc.add(GregorianCalendar.DAY_OF_MONTH, 6);
            fecFinE.setDate(gc.getTime());
        } catch (ParseException ex)
        {
            Logger.getLogger(CLRankClie.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    void Baceptar_actionPerformed(ActionEvent e)
    {
       if (! checkCondic())
            return;
       new miThread("")
       {
            @Override
           public void run()
            {
              msgEspere("Buscando datos...");
              buscaVentas(true);
              resetMsgEspere();
              mensaje("");
              mensajeErr("Consulta realizada");
           }
       };
   }
   boolean checkCondic()
   {
       if (fecIniE.isNull() || fecIniE.getError())
       {
           mensajeErr("Fecha inicial no valida");
           fecIniE.requestFocus();
           return false;
        }
        if (fecFinE.isNull() || fecFinE.getError())
       {
           mensajeErr("Fecha Final no valida");
           fecIniE.requestFocus();
           return false;
        }
       return true;
   }
   void buscaVentas(boolean debug)
   {
     try
     {
       if (debug)
         mensaje("Espere, por favor ... buscando Datos");
        condWhere=getCondWhere(fecIniE.getText(),fecFinE.getText());
        s="select sum(avc_kilos) as avc_kilos," +
               " sum(avc_basimp) as avc_basimp,count(distinct(c.cli_codi)) as numcli " +
             " from v_albavec  c, clientes cl  where " +condWhere;
        dtCon1.select(s);
        double kilAlb=dtCon1.getDouble("avc_kilos",true);
        double impAlb=dtCon1.getDouble("avc_basimp",true);
        kilAlbE.setValorDec(kilAlb);
        impAlbE.setValorDec(impAlb);
        numCliE.setValorInt(dtCon1.getInt("numcli"));
        s=getStrSql(ordenE.getValor());
        if (!dtCon1.select(s))
        {
            mensajes.mensajeAviso("No encontradas ventas para estas fechas");
            return;
        }
        datos=new ArrayList();
        do
        {  
            ArrayList v=new ArrayList();
            v.add(dtCon1.getInt("cli_codi"));
            v.add(pdclien.getNombreCliente(dtStat,dtCon1.getInt("cli_codi")));
            v.add(dtCon1.getInt("numalb"));
            v.add(dtCon1.getDouble("avc_basimp"));
            v.add(dtCon1.getDouble("avc_kilos"));

            v.add(dtCon1.getDouble("avc_basimp")==0?0:Formatear.Redondea(dtCon1.getDouble("avc_basimp")/impAlb*100,2));
            v.add(dtCon1.getDouble("avc_kilos")==0?0:Formatear.Redondea(dtCon1.getDouble("avc_kilos")/kilAlb*100,2));
            datos.add(v);
        } while (dtCon1.next());
        jt.setDatos(datos);
        javax.swing.SwingUtilities.invokeLater(new Thread()
        {
            @Override
            public void run()
            {
              jt.requestFocus(0,0);
              jt.setEnabled(true);
              mensajeErr("Consulta realizada");
            }
        });     
     } catch (Exception k)
     {
         Error("Error al Calcular Datos", k);
     }
   }
   
   void busAlbCli()
   {
       try
       {
         if (jt.isVacio())
             return;
         if (alCli == null)
         {
           alCli = new AlbClien();
           alCli.iniciar(this);
           vl.add(alCli);
         }
         alCli.setLocation(this.getLocation().x+100, this.getLocation().y+80);
         alCli.setSelected(true);
         this.setEnabled(false);
         this.setFoco(alCli);
         alCli.cargaDatos(fecIniE.getText(),fecFinE.getText(),jt.getValorInt(0));
         alCli.setVisible(true);
       }
       catch (Exception ex)
       {
         fatalError("Error al Cargar datos de Clientes",ex);
       }
  }
   String getStrSql(String orden)
   {
        return "select sum(avc_kilos) as avc_kilos," +
               " sum(avc_basimp) as avc_basimp, count(*) as numalb,c.cli_codi " +
             " from v_albavec  c, clientes cl  " +
             " where "+
             condWhere+
             " group by c.cli_codi "+
             " order by "+(orden.equals("K")?"avc_kilos":"avc_basimp")+
             " desc";
   }
   String getCondWhere(String fecini,String fecfin)
   {
      return " c.avc_fecalb >= TO_DATE('" + fecini +  "','dd-MM-yyyy') " +
             " and c.avc_fecalb <= TO_DATE('" + fecfin +   "','dd-MM-yyyy') " +
             " and c.avc_serie >= 'A' AND c.avc_serie <='C' " +
             (EU.isRootAV()?"":" and c.div_codi > 0 ")+
             (emp_codiE.getValorInt()==0?"":" and c.emp_codi = "+emp_codiE.getValorInt())+
             (sbe_codiE.getValorInt()==0?"":" and c.sbe_codi = "+sbe_codiE.getValorInt())+
             (zon_codiE.isNull()?"":" and cl.zon_codi = '"+zon_codiE.getText()+"'")+
             (rep_codiE.isNull()?"":" and cl.rep_codi = '"+rep_codiE.getText()+"'")+
             bdiscr.getCondWhere("cl")+
             " and cl.cli_codi = c.cli_codi ";
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
        Pcabe = new gnu.chu.controles.CPanel();
        cLabel5 = new gnu.chu.controles.CLabel();
        cLabel6 = new gnu.chu.controles.CLabel();
        cLabel16 = new gnu.chu.controles.CLabel();
        rep_codiE = new gnu.chu.controles.CLinkBox();
        cLabel3 = new gnu.chu.controles.CLabel();
        emp_codiE = new gnu.chu.camposdb.empPanel();
        cLabel4 = new gnu.chu.controles.CLabel();
        sbe_codiE = new gnu.chu.camposdb.sbePanel();
        Baceptar = new gnu.chu.controles.CButtonMenu();
        cLabel17 = new gnu.chu.controles.CLabel();
        zon_codiE = new gnu.chu.controles.CLinkBox();
        bdiscr = new gnu.chu.camposdb.DiscButton();
        fecIniE = new gnu.chu.camposdb.fechaCal();
        fecFinE = new gnu.chu.camposdb.fechaCal();
        cLabel18 = new gnu.chu.controles.CLabel();
        ordenE = new gnu.chu.controles.CComboBox();
        jt = new gnu.chu.controles.Cgrid(7);
        Ppie = new gnu.chu.controles.CPanel();
        cLabel2 = new gnu.chu.controles.CLabel();
        numCliE = new gnu.chu.controles.CTextField(Types.DECIMAL,"####9");
        cLabel7 = new gnu.chu.controles.CLabel();
        impAlbE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--,---,--9.99");
        cLabel9 = new gnu.chu.controles.CLabel();
        kilAlbE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--,---,--9.99");

        Pprinc.setLayout(new java.awt.GridBagLayout());

        Pcabe.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Pcabe.setMaximumSize(new java.awt.Dimension(610, 51));
        Pcabe.setMinimumSize(new java.awt.Dimension(610, 51));
        Pcabe.setPreferredSize(new java.awt.Dimension(610, 51));
        Pcabe.setLayout(null);

        cLabel5.setText("De Fecha");
        cLabel5.setPreferredSize(new java.awt.Dimension(52, 18));
        Pcabe.add(cLabel5);
        cLabel5.setBounds(210, 20, 52, 18);

        cLabel6.setText("A Fecha");
        cLabel6.setMaximumSize(new java.awt.Dimension(43, 18));
        cLabel6.setMinimumSize(new java.awt.Dimension(43, 18));
        cLabel6.setPreferredSize(new java.awt.Dimension(44, 18));
        Pcabe.add(cLabel6);
        cLabel6.setBounds(350, 20, 44, 18);

        cLabel16.setText("Repres.");
        cLabel16.setPreferredSize(new java.awt.Dimension(60, 18));
        Pcabe.add(cLabel16);
        cLabel16.setBounds(10, 0, 60, 18);

        rep_codiE.setAncTexto(30);
        rep_codiE.setPreferredSize(new java.awt.Dimension(152, 18));
        Pcabe.add(rep_codiE);
        rep_codiE.setBounds(60, 0, 190, 18);

        cLabel3.setText("Empresa");
        Pcabe.add(cLabel3);
        cLabel3.setBounds(10, 20, 49, 18);

        emp_codiE.setPreferredSize(new java.awt.Dimension(39, 18));
        Pcabe.add(emp_codiE);
        emp_codiE.setBounds(60, 20, 40, 20);

        cLabel4.setText("Delegación");
        Pcabe.add(cLabel4);
        cLabel4.setBounds(100, 20, 70, 18);
        Pcabe.add(sbe_codiE);
        sbe_codiE.setBounds(170, 20, 37, 20);

        Baceptar.setText("Aceptar (F4)");
        Pcabe.add(Baceptar);
        Baceptar.setBounds(500, 20, 100, 26);

        cLabel17.setText("Orden");
        cLabel17.setPreferredSize(new java.awt.Dimension(60, 18));
        Pcabe.add(cLabel17);
        cLabel17.setBounds(450, 0, 40, 18);

        zon_codiE.setAncTexto(30);
        zon_codiE.setPreferredSize(new java.awt.Dimension(152, 18));
        Pcabe.add(zon_codiE);
        zon_codiE.setBounds(280, 0, 170, 18);

        bdiscr.setPreferredSize(new java.awt.Dimension(18, 18));
        Pcabe.add(bdiscr);
        bdiscr.setBounds(570, 0, 30, 18);
        Pcabe.add(fecIniE);
        fecIniE.setBounds(260, 20, 90, 20);

        fecFinE.setTipoFecha(2);
        Pcabe.add(fecFinE);
        fecFinE.setBounds(400, 20, 90, 20);

        cLabel18.setText("Zona");
        cLabel18.setPreferredSize(new java.awt.Dimension(60, 18));
        Pcabe.add(cLabel18);
        cLabel18.setBounds(250, 0, 40, 18);

        ordenE.addItem("Kilos", "K");
        ordenE.addItem("Importe","I");
        Pcabe.add(ordenE);
        ordenE.setBounds(490, 0, 70, 18);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        Pprinc.add(Pcabe, gridBagConstraints);

        ArrayList v = new ArrayList();
        v.add("Cliente"); // 0
        v.add("Nombre"); // 1
        v.add("N. Albaran"); // 2
        v.add("Importe"); // 3
        v.add("Kilos"); // 4
        v.add("% Imp");
        v.add("% Kg");
        jt.setCabecera(v);
        jt.setAnchoColumna(new int[]{43,208,50,78,78,40,40});
        jt.setAlinearColumna(new int[]{2,0,2,2,2,2,2});
        jt.setFormatoColumna(0, "####9");
        jt.setFormatoColumna(3, "--,---,--9.99");
        jt.setFormatoColumna(4, "--,---,--9.99");
        jt.setFormatoColumna(5, "--9.99");
        jt.setFormatoColumna(6, "--9.99");
        jt.setAjustarGrid(true);
        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jtLayout = new javax.swing.GroupLayout(jt);
        jt.setLayout(jtLayout);
        jtLayout.setHorizontalGroup(
            jtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 651, Short.MAX_VALUE)
        );
        jtLayout.setVerticalGroup(
            jtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 187, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        Pprinc.add(jt, gridBagConstraints);

        Ppie.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        Ppie.setEnabled(false);
        Ppie.setMaximumSize(new java.awt.Dimension(500, 30));
        Ppie.setMinimumSize(new java.awt.Dimension(500, 30));
        Ppie.setPreferredSize(new java.awt.Dimension(500, 30));
        Ppie.setLayout(null);

        cLabel2.setText("Num. Clientes ");
        cLabel2.setMaximumSize(new java.awt.Dimension(87, 18));
        cLabel2.setMinimumSize(new java.awt.Dimension(87, 18));
        cLabel2.setPreferredSize(new java.awt.Dimension(87, 18));
        Ppie.add(cLabel2);
        cLabel2.setBounds(10, 5, 81, 18);

        numCliE.setMinimumSize(new java.awt.Dimension(2, 18));
        numCliE.setPreferredSize(new java.awt.Dimension(2, 18));
        Ppie.add(numCliE);
        numCliE.setBounds(90, 5, 59, 18);

        cLabel7.setText("Importe");
        cLabel7.setMaximumSize(new java.awt.Dimension(87, 18));
        cLabel7.setMinimumSize(new java.awt.Dimension(87, 18));
        cLabel7.setPreferredSize(new java.awt.Dimension(87, 18));
        Ppie.add(cLabel7);
        cLabel7.setBounds(160, 5, 51, 18);

        impAlbE.setMinimumSize(new java.awt.Dimension(2, 18));
        impAlbE.setPreferredSize(new java.awt.Dimension(2, 18));
        Ppie.add(impAlbE);
        impAlbE.setBounds(220, 5, 109, 18);

        cLabel9.setText("Kilos");
        cLabel9.setMaximumSize(new java.awt.Dimension(87, 18));
        cLabel9.setMinimumSize(new java.awt.Dimension(87, 18));
        cLabel9.setPreferredSize(new java.awt.Dimension(87, 18));
        Ppie.add(cLabel9);
        cLabel9.setBounds(340, 5, 37, 18);

        kilAlbE.setMinimumSize(new java.awt.Dimension(2, 18));
        kilAlbE.setPreferredSize(new java.awt.Dimension(2, 18));
        Ppie.add(kilAlbE);
        kilAlbE.setBounds(380, 5, 109, 18);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        Pprinc.add(Ppie, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButtonMenu Baceptar;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Ppie;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.camposdb.DiscButton bdiscr;
    private gnu.chu.controles.CLabel cLabel16;
    private gnu.chu.controles.CLabel cLabel17;
    private gnu.chu.controles.CLabel cLabel18;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel9;
    private gnu.chu.camposdb.empPanel emp_codiE;
    private gnu.chu.camposdb.fechaCal fecFinE;
    private gnu.chu.camposdb.fechaCal fecIniE;
    private gnu.chu.controles.CTextField impAlbE;
    private gnu.chu.controles.Cgrid jt;
    private gnu.chu.controles.CTextField kilAlbE;
    private gnu.chu.controles.CTextField numCliE;
    private gnu.chu.controles.CComboBox ordenE;
    private gnu.chu.controles.CLinkBox rep_codiE;
    private gnu.chu.camposdb.sbePanel sbe_codiE;
    private gnu.chu.controles.CLinkBox zon_codiE;
    // End of variables declaration//GEN-END:variables

}
