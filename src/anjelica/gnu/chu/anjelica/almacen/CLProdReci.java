
package gnu.chu.anjelica.almacen;
/**
 *
 * <p>Título: CLProdreci </p>
 * <p>Descripción: Consulta/Listado Productos Reciclables  </p>
 * <p>Este programa permite consultar bien agrupados por productos, bien mostrando
 * todos las salidas de productos reciclables. Busca en tablas v_despfin,albvenres y en v_regstock</p>
 * <p>@see MantFamPro.getFamiliaRecicla(EU) </p>
 * <p>Copyright: Copyright (c) 2005-2012
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
* <p>Empresa: MISL</p>
* @author chuchiP
* @version 1.0
*/
import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.menu;
import gnu.chu.anjelica.pad.MantFamPro;
import gnu.chu.anjelica.pad.pdempresa;
import gnu.chu.camposdb.empPanel;
import gnu.chu.controles.StatusBar;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.miThread;
import gnu.chu.utilidades.ventana;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SpinnerNumberModel;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class CLProdReci extends ventana
{
    String filtroEmpr;
    String s;
    public CLProdReci() {
        initComponents();
    }
    public CLProdReci(EntornoUsuario eu, Principal p) {
        EU = eu;
        vl = p.panel1;
        jf = p;
        eje = true;

        setTitulo("Consulta/List. Productos Reciclados");

        try
        { 
            if (jf.gestor.apuntar(this))
                jbInit();
            else
                setErrorInit(true);
        } catch (Exception e)
        {
            Logger.getLogger(CLProdReci.class.getName()).log(Level.SEVERE, null, e);
            setErrorInit(true);
        }
    }

    public CLProdReci(menu p, EntornoUsuario eu) {

        EU = eu;
        vl = p.getLayeredPane();
        setTitulo("Consulta/List. Productos Reciclados");
        eje = false;


        try
        {
            jbInit();
        } catch (Exception e)
        {
            Logger.getLogger(CLProdReci.class.getName()).log(Level.SEVERE, null, e);
            setErrorInit(true);
        }
  }
  private void jbInit() throws Exception
  {
      iniciarFrame();

      this.setVersion("2012-03-08");
      statusBar = new StatusBar(this);
      this.getContentPane().add(statusBar, BorderLayout.SOUTH);
      conecta();

      initComponents();
      this.setSize(new Dimension(600,500));
      Pcondi.setDefButton(Baceptar.getBotonAccion());
  }
  @Override
  public void iniciarVentana() throws Exception
  {
      filtroEmpr=empPanel.getStringAccesos(dtStat, EU.usuario,true);
      pro_codiE.iniciar(dtStat, this, vl, EU);
      fecfinE.setDate(Formatear.getDateAct());
      activarEventos();
  }
  private void activarEventos()
  {
      Baceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Baceptar_actionPerformed(e.getActionCommand());
            }
        });
  }
  
  private void Baceptar_actionPerformed(String accion)
  {
     if (feciniE.isNull())
     {
         mensajeErr("Introduzca fecha Inicio");
         feciniE.requestFocus();
         return;
     }
      if (fecfinE.isNull())
     {
         mensajeErr("Introduzca fecha Final");
         fecfinE.requestFocus();
         return;
     }
      if (accion.startsWith("Listar"))
      {
          new miThread("")
          {
                @Override
              public void run()
              {
                  listar();
              }
          };
          return;
      }
      try {
        jt.removeAllDatos();
         s=getStrSql(accion.startsWith("Agr"));
         if (! dtCon1.select(s))
         {
             msgBox("No encontrados registros para estas condiciones");
             feciniE.requestFocus();
             return;
         }
         ArrayList<ArrayList> datos=new ArrayList();
         int proCod=dtCon1.getInt("pro_codi");
         double kilos=0;
         double kilTot=0;
         int nReg=0;
         do
         {
             ArrayList dat= new ArrayList();
             if (proCod!=dtCon1.getInt("pro_codi"))
             {
                 if (nReg>1)
                 {
                    ArrayList dt= new ArrayList();
                    dt.add("");
                    dt.add("");
                    dt.add(" TOTAL ....");
                    dt.add(kilos);
                    dt.add("");
                    datos.add(dt);
                 }
                 kilos=0;
                 proCod=dtCon1.getInt("pro_codi");
             }
             kilos+=dtCon1.getDouble("kilos");
             kilTot+=dtCon1.getDouble("kilos");
             nReg++;
             dat.add(dtCon1.getString("tipo"));
             dat.add(dtCon1.getInt("pro_codi"));
             dat.add(pro_codiE.getNombArt(dtCon1.getInt("pro_codi")));
             dat.add(dtCon1.getDouble("kilos"));
             if (accion.startsWith("Agr"))
                 dat.add("");
             else
                dat.add(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(dtCon1.getTimeStamp("fecha")));
             datos.add(dat);
         } while (dtCon1.next());
         if (nReg>1)
         { 
            ArrayList dat= new ArrayList();
            dat.add("");
            dat.add("");
            dat.add(" TOTAL ....");
            dat.add(kilos);
            dat.add("");
            datos.add(dat);
         }
         kilTotE.setValorDec(kilTot);
         jt.addLineas(datos);
      } catch (Exception k)
      {
          Error("Error al buscar productos recicables en albaranes venta",k);
      }
  }
  String getStrSql(boolean agrupa) throws Exception
  {
      String condEmpr=  (filtroEmpr==null?"":" and a.emp_codi in ("+filtroEmpr+")");
      String s1=" between  {ts '"+
            feciniE.getFecha("yyyy-MM-dd")+" "+Formatear.format(horiniE.getValue().toString(),"99")+":"+
            Formatear.format(mininiE.getValue().toString(),"99")+"'} "+
            " and {ts '"+
            fecfinE.getFecha("yyyy-MM-dd")+" "+Formatear.format(horfinE.getValue().toString(),"99")+":"+
            Formatear.format(minfinE.getValue().toString(),"99")+"'} "+
            " and a.pro_codi = pr.pro_codi "+
            " and pr.fam_codi= "+MantFamPro.getFamiliaRecicla(EU)+
            (pro_codiE.isNull()?"":" and a.pro_codi="+pro_codiE.getValorInt());
        if (agrupa)
        {
          s="SELECT 'A' as tipo,a.pro_codi,sum(avr_canti) as kilos,pro_nomb from albvenres as a,"
              + " v_articulo as pr  where avr_fecalt "+s1+
            " group by a.pro_codi,pro_nomb"+
              " union all "+
              " select 'D' as tipo,a.pro_codi,sum(def_kilos) as kilos,pro_nomb from v_despfin as a,"
              + " v_articulo as pr  where def_tiempo "+s1+
              " group by a.pro_codi,pro_nomb "+
              " union all "+
              " select '-' as tipo, a.pro_codi,sum(rgs_canti) as kilos,pro_nomb from v_regstock as a, "
              + "v_articulo as pr  where rgs_fecha "+s1+
               condEmpr+
              " and tir_afestk  = '-'  "+
              " group by a.pro_codi,pro_nomb "+
                " union all "+
              " select '+' as tipo,a.pro_codi,sum(rgs_canti)*-1 as kilos,pro_nomb from v_regstock as a,"
              + " v_articulo as pr  where rgs_fecha "+s1+
              condEmpr+
              " and tir_afestk = '+' "+
              " group by a.pro_codi,pro_nomb "+
              " order by 2  ";
        }
        else
          s="select 'A' as tipo,a.pro_codi, avr_canti as kilos,avr_fecalt as fecha "+
              " from albvenres  as a,v_articulo as pr where avr_fecalt  " +s1+
               " union all "+
              " select 'D' as tipo,a.pro_codi,def_kilos as kilos,def_tiempo as fecha "+
              " from v_despfin as a, v_articulo as pr  where def_tiempo "+s1+
              " union all "+
              " select '-' as tipo, a.pro_codi,rgs_canti as kilos,rgs_fecha as fecha from v_regstock as a, "
              + "v_articulo as pr  where rgs_fecha "+s1+
              condEmpr+
              " and  tir_afestk = '-' "+
                " union all "+
              " select '+' as tipo,a.pro_codi,rgs_canti*-1 as kilos,rgs_fecha as fecha from v_regstock as a,"
              + " v_articulo as pr  where rgs_fecha "+s1+
              condEmpr+
              " and  tir_afestk = '+'  "+
            " order by 2,4";
        return s;
  }
  void listar()
  {
      try {
          msgEspere("Generando listado.. espere, por favor");
       s=getStrSql(true);
      dtCon1.setStrSelect(s);
      ResultSet rs=ct.createStatement().executeQuery(dtCon1.getStrSelect());
      JasperReport jr;
      jr = gnu.chu.print.util.getJasperReport(EU, "prodreci");
      java.util.HashMap mp = new java.util.HashMap();
      mp.put("fecini", feciniE.getFecha("dd-MM-yyyy")+" "+Formatear.format(horiniE.getValue().toString(),"99")+":"+
            Formatear.format(mininiE.getValue().toString(),"99"));
      mp.put("fecfin", fecfinE.getFecha("dd-MM-yyyy")+" "+Formatear.format(horfinE.getValue().toString(),"99")+":"+
            Formatear.format(minfinE.getValue().toString(),"99"));
  
      JasperPrint jp = JasperFillManager.fillReport(jr, mp, new JRResultSetDataSource(rs));
      
      if (!gnu.chu.print.util.printJasper(jp, EU)) {
            mensaje("");
            resetMsgEspere();
            return;
      }
      } catch (Exception k)
      {
          Error("Error al generar Listado",k);
      }
      mensaje("");
      resetMsgEspere();
      mensajeErr("Listado ... Generado");
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

        proPanel1 = new gnu.chu.camposdb.proPanel();
        PPrinc = new gnu.chu.controles.CPanel();
        Pcondi = new gnu.chu.controles.CPanel();
        cLabel1 = new gnu.chu.controles.CLabel();
        pro_codiE = new gnu.chu.camposdb.proPanel();
        PhoraIni = new gnu.chu.controles.CPanel();
        cLabel3 = new gnu.chu.controles.CLabel();
        feciniE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel4 = new gnu.chu.controles.CLabel();
        horiniE = new gnu.chu.controles.CSpinner(new SpinnerNumberModel(0,0,23,1));
        mininiE = new gnu.chu.controles.CSpinner(new SpinnerNumberModel(0,0,59,1));
        PhoraFin = new gnu.chu.controles.CPanel();
        cLabel5 = new gnu.chu.controles.CLabel();
        fecfinE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel6 = new gnu.chu.controles.CLabel();
        horfinE = new gnu.chu.controles.CSpinner(new SpinnerNumberModel(23,0,23,1));
        minfinE = new gnu.chu.controles.CSpinner(new SpinnerNumberModel(0,0,59,1));
        Baceptar = new gnu.chu.controles.CButtonMenu();
        jt = new gnu.chu.controles.Cgrid(5);
        Ppie = new gnu.chu.controles.CPanel();
        cLabel2 = new gnu.chu.controles.CLabel();
        kilTotE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---,--9.99");

        Pcondi.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Pcondi.setMaximumSize(new java.awt.Dimension(546, 89));
        Pcondi.setMinimumSize(new java.awt.Dimension(546, 89));
        Pcondi.setLayout(null);

        cLabel1.setText("Producto ");
        Pcondi.add(cLabel1);
        cLabel1.setBounds(1, 55, 60, 17);
        Pcondi.add(pro_codiE);
        pro_codiE.setBounds(60, 55, 371, 17);

        PhoraIni.setBorder(javax.swing.BorderFactory.createTitledBorder("Inicio"));
        PhoraIni.setLayout(null);

        cLabel3.setText("Fecha");
        PhoraIni.add(cLabel3);
        cLabel3.setBounds(6, 18, 38, 18);
        PhoraIni.add(feciniE);
        feciniE.setBounds(48, 18, 70, 18);

        cLabel4.setText("Hora");
        PhoraIni.add(cLabel4);
        cLabel4.setBounds(130, 18, 38, 18);
        PhoraIni.add(horiniE);
        horiniE.setBounds(170, 18, 41, 18);
        PhoraIni.add(mininiE);
        mininiE.setBounds(220, 18, 41, 18);

        Pcondi.add(PhoraIni);
        PhoraIni.setBounds(3, 3, 266, 50);

        PhoraFin.setBorder(javax.swing.BorderFactory.createTitledBorder("Fin"));
        PhoraFin.setLayout(null);

        cLabel5.setText("Fecha");
        PhoraFin.add(cLabel5);
        cLabel5.setBounds(6, 18, 38, 18);
        PhoraFin.add(fecfinE);
        fecfinE.setBounds(48, 18, 70, 18);

        cLabel6.setText("Hora");
        PhoraFin.add(cLabel6);
        cLabel6.setBounds(130, 18, 38, 18);
        PhoraFin.add(horfinE);
        horfinE.setBounds(170, 18, 41, 18);
        PhoraFin.add(minfinE);
        minfinE.setBounds(210, 18, 41, 18);

        Pcondi.add(PhoraFin);
        PhoraFin.setBounds(275, 3, 262, 50);

        Baceptar.setText("Aceptar");
        Baceptar.addMenu("Agrupado");
        Baceptar.addMenu("Detallado");
        Baceptar.addMenu("Listar");
        Pcondi.add(Baceptar);
        Baceptar.setBounds(440, 55, 100, 30);

        ArrayList v=new ArrayList();
        v.add("Tipo");
        v.add("Producto");
        v.add("Nombre");
        v.add("Kilos");
        v.add("Fecha");
        jt.setCabecera(v);
        jt.setAnchoColumna(new int[]{30,50,250,60,100});
        jt.setAlinearColumna(new int[]{1,2,0,2,1});
        jt.setAjustarGrid(true);
        jt.setFormatoColumna(3, "--,--9.99");
        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jt.setMaximumSize(new java.awt.Dimension(539, 125));
        jt.setMinimumSize(new java.awt.Dimension(539, 125));

        javax.swing.GroupLayout jtLayout = new javax.swing.GroupLayout(jt);
        jt.setLayout(jtLayout);
        jtLayout.setHorizontalGroup(
            jtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jtLayout.setVerticalGroup(
            jtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 257, Short.MAX_VALUE)
        );

        Ppie.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        Ppie.setMaximumSize(new java.awt.Dimension(186, 25));
        Ppie.setMinimumSize(new java.awt.Dimension(186, 25));
        Ppie.setPreferredSize(new java.awt.Dimension(186, 25));
        Ppie.setLayout(null);

        cLabel2.setText("Kilos Totales");
        Ppie.add(cLabel2);
        cLabel2.setBounds(10, 6, 88, 15);

        kilTotE.setEditable(false);
        Ppie.add(kilTotE);
        kilTotE.setBounds(100, 6, 78, 17);

        javax.swing.GroupLayout PPrincLayout = new javax.swing.GroupLayout(PPrinc);
        PPrinc.setLayout(PPrincLayout);
        PPrincLayout.setHorizontalGroup(
            PPrincLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(PPrincLayout.createSequentialGroup()
                .addGroup(PPrincLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PPrincLayout.createSequentialGroup()
                        .addGap(157, 157, 157)
                        .addComponent(Ppie, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PPrincLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Pcondi, javax.swing.GroupLayout.PREFERRED_SIZE, 556, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        PPrincLayout.setVerticalGroup(
            PPrincLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PPrincLayout.createSequentialGroup()
                .addComponent(Pcondi, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Ppie, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getContentPane().add(PPrinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButtonMenu Baceptar;
    private gnu.chu.controles.CPanel PPrinc;
    private gnu.chu.controles.CPanel Pcondi;
    private gnu.chu.controles.CPanel PhoraFin;
    private gnu.chu.controles.CPanel PhoraIni;
    private gnu.chu.controles.CPanel Ppie;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CTextField fecfinE;
    private gnu.chu.controles.CTextField feciniE;
    private gnu.chu.controles.CSpinner horfinE;
    private gnu.chu.controles.CSpinner horiniE;
    private gnu.chu.controles.Cgrid jt;
    private gnu.chu.controles.CTextField kilTotE;
    private gnu.chu.controles.CSpinner minfinE;
    private gnu.chu.controles.CSpinner mininiE;
    private gnu.chu.camposdb.proPanel proPanel1;
    private gnu.chu.camposdb.proPanel pro_codiE;
    // End of variables declaration//GEN-END:variables
}

