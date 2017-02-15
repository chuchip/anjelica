/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gnu.chu.anjelica.margenes;

import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.pad.MantArticulos;
import gnu.chu.controles.StatusBar;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.navegador;
import gnu.chu.utilidades.ventana;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author cpuente
 */
public class MantPreMedios extends ventana
{
   Hashtable<Integer,Double> htBolas = new Hashtable();
   Hashtable<Integer,Integer[]> htLomos = new Hashtable();
   Hashtable<Integer,Boolean> htLomCom = new Hashtable();
    
   final double PRECIO_DESP=2.8;
   public MantPreMedios(EntornoUsuario eu, Principal p)
  {
    this(eu,p,null);
  }

  public MantPreMedios(EntornoUsuario eu, Principal p, Hashtable ht)
  {
      EU = eu;
      vl = p.panel1;
      jf = p;
      eje = true;

      setTitulo("Mant. Precios Medios");

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

    public MantPreMedios(gnu.chu.anjelica.menu p, EntornoUsuario eu)
    {

      EU = eu;
      vl = p.getLayeredPane();
       setTitulo("Mant. Precios Medios");
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
      this.setVersion("2017-02-15" );
      statusBar = new StatusBar(this);
      
      iniciarFrame();
      this.getContentPane().add(statusBar, BorderLayout.SOUTH);
      initComponents();
      
      this.setSize(new Dimension(582,522));
      conecta();
      

    }
     @Override
    public void iniciarVentana() throws Exception
    {
        htBolas.put(40201, 2.5);
        htBolas.put(40202, 2.6);
        htBolas.put(40203, 2.7);
        htBolas.put(40205, 2.5);
        htBolas.put(40225, 2.5);
        
        htLomos.put(10904,new Integer[]{10904,10905});
        htLomos.put(10994,new Integer[]{10994,10995});
        htLomos.put(10903,new Integer[]{10903});
        htLomos.put(10993,new Integer[]{10993});
        htLomos.put(10902,new Integer[]{10902});
        htLomos.put(10992,new Integer[]{10992});
        htLomos.put(10901,new Integer[]{10901});
        htLomos.put(10991,new Integer[]{10991});
        htLomos.put(10906,new Integer[]{10906});
        htLomos.put(10931,new Integer[]{10931});
        htLomos.put(10932,new Integer[]{10932});
        htLomos.put(10933,new Integer[]{10933});
        
        htLomCom.put(10904,true);
        htLomCom.put(10994,false);
        htLomCom.put(10903,true);
        htLomCom.put(10993,false);
        htLomCom.put(10902,true);
        htLomCom.put(10992,false);
        htLomCom.put(10901,true);
        htLomCom.put(10991,false);
        htLomCom.put(10906,true);
        htLomCom.put(10931,true);
        htLomCom.put(10932,true);
        htLomCom.put(10933,true);
        eje_numeE.setValorDec(EU.ejercicio);
        activarEventos();
    }
    void activarEventos()
    {
         cta_semanaE.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e) {
                if (cta_semanaE.hasCambio())
                {
                    cta_semanaE.resetCambio();
                    try {
                        ponFechas();
                    } catch ( ParseException  ex)
                    {
                        Error("Error al poner fechas",ex);
                    }
                }
            }
        });
       BirGrid.addActionListener(new ActionListener()
       {
            @Override
             public void actionPerformed(ActionEvent e) {
              llenaBolas();  
            }
       });
    }
    void llenaBolas()
    {
        try
        {
            jt.removeAllDatos();
           String s="select a.pro_codi,sum(avp_canti) as kilos from v_albventa_detalle as a , clientes as c,v_despfin as d where a.cli_codi=c.cli_codi "+
            " and avc_fecalb>='"+tar_feciniE.getFechaDB()+"'"+ //  -- Lunes
            " and (a.pro_codi>=40201 and a.pro_codi<=40299) "+
            " and d.def_serlot='G' "+
            " and a.pro_codi=d.pro_codi and a.avp_ejelot=d.def_ejelot and a.avp_serlot=d.def_serlot and a.avp_numpar=d.pro_lote and a.avp_numind = d.pro_numind "+
            " and d.def_tiempo>='"+tar_feciniE.getFechaDB()+"' " + // Lunes
            " group  by a.pro_codi"+
            " order by pro_codi";
           String s1;
           if (!dtCon1.select(s))
           {
               msgBox("NO ENCONTRADOS DATOS");
               return;
           }
           do
           {
               ArrayList v=new ArrayList();
               v.add(dtCon1.getInt("pro_codi"));
               v.add(MantArticulos.getNombProd(dtCon1.getInt("pro_codi"), dtStat));
               s1="select sum(def_kilos) as kilos from v_despsal where pro_codi = "+dtCon1.getInt("pro_codi")+
                   " and deo_fecha between '"+tar_feciniE.getFechaDB()+"' and '"+fecFinComE.getFechaDB()+"'";
               dtStat.select(s1);
               v.add(dtStat.getDouble("kilos",true)); // Kilos producidos
               v.add(dtStat.getDouble("kilos",true)-dtCon1.getDouble("kilos",true)); // Kilos a despiezados
               v.add(dtCon1.getDouble("kilos",true));
               double precio=htBolas.get(dtCon1.getInt("pro_codi"));
               v.add(precio);
               double importeVenta=dtCon1.getDouble("kilos",true)*precio;
               double importeDesp=(dtStat.getDouble("kilos",true)-dtCon1.getDouble("kilos",true))*PRECIO_DESP;
               v.add((importeVenta+importeDesp)/dtStat.getDouble("kilos",true));
               jt.addLinea(v);
           } while (dtCon1.next());
           jt1.removeAllDatos();
           Set<Integer> stLomos= htLomos.keySet();
           Iterator<Integer> it =stLomos.iterator();
           Integer lomo;           
           while ((lomo=it.next())!=null)
           {
               Integer[] codigos=htLomos.get(lomo);
               int codigo1=codigos[0];
               int codigo2=codigos[0];
               if (codigos.length>1)
                  codigo2=codigos[1];
               boolean incCompra=htLomCom.get(lomo);
               s="select sum(lci_peso) as kilos from v_coninvent where lci_peso>0 and cci_feccon='"+fecStockE.getFechaDB()+"'"+
                   " and (pro_codi="+codigo1+
                   " or pro_codi= "+codigo2+")"+ 
                   (incCompra?" and prp_part not in (select acc_nume from v_albacoc where acc_fecrec>='"+fecIniComE.getFechaDB()+"')":"");
              dtCon1.select(s);
              ArrayList v=new ArrayList();
              v.add(lomo);
              v.add(MantArticulos.getNombProd(lomo, dtStat));
              v.add(dtCon1.getDouble("kilos",true));
              jt1.addLinea(v);
           }
           
        } catch (SQLException | ParseException k)
        {
            Error("Error al llenar grid bolas",k);
        }
    }
     void ponFechas() throws ParseException
    {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(Formatear.getDate("01-01-"+eje_numeE.getValorInt(),"dd-MM-yyyy"));
//        gc.set(GregorianCalendar.YEAR, eje_numeE.getValorInt());
        gc.set(GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.MONDAY);
        gc.set(GregorianCalendar.WEEK_OF_YEAR, cta_semanaE.getValorInt());     
        tar_feciniE.setDate(new java.util.Date(gc.getTimeInMillis()));
        gc.set(GregorianCalendar.WEEK_OF_YEAR, cta_semanaE.getValorInt()+1);
//        gc.set(GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.SUNDAY);
        
        fecStockE.setText(Formatear.sumaDias(tar_feciniE.getText(),"dd-MM-yyyy",-2)); // Sabado anterior
        
        fecIniComE.setText(Formatear.sumaDias(tar_feciniE.getText(),"dd-MM-yyyy",-4) ); // Jueves anteerior
        fecFinComE.setText(Formatear.sumaDias(tar_feciniE.getText(),"dd-MM-yyyy",3) );
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
        PCondi = new gnu.chu.controles.CPanel();
        cLabel2 = new gnu.chu.controles.CLabel();
        cta_semanaE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#9");
        cLabel5 = new gnu.chu.controles.CLabel();
        tar_feciniE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        BirGrid = new gnu.chu.controles.CButton();
        cLabel3 = new gnu.chu.controles.CLabel();
        eje_numeE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        jt1 = new gnu.chu.controles.Cgrid(3);
        jt = new gnu.chu.controles.Cgrid(7);
        PPie = new gnu.chu.controles.CPanel();
        cLabel7 = new gnu.chu.controles.CLabel();
        fecStockE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel9 = new gnu.chu.controles.CLabel();
        fecIniComE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel11 = new gnu.chu.controles.CLabel();
        fecFinComE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");

        Pprinc.setLayout(new java.awt.GridBagLayout());

        PCondi.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        PCondi.setMaximumSize(new java.awt.Dimension(470, 25));
        PCondi.setMinimumSize(new java.awt.Dimension(470, 25));
        PCondi.setPreferredSize(new java.awt.Dimension(470, 25));
        PCondi.setLayout(null);

        cLabel2.setText("Ejercicio");
        PCondi.add(cLabel2);
        cLabel2.setBounds(3, 3, 50, 17);
        PCondi.add(cta_semanaE);
        cta_semanaE.setBounds(150, 3, 20, 17);

        cLabel5.setText("Fecha");
        PCondi.add(cLabel5);
        cLabel5.setBounds(180, 3, 60, 17);

        tar_feciniE.setMaximumSize(new java.awt.Dimension(10, 18));
        tar_feciniE.setMinimumSize(new java.awt.Dimension(10, 18));
        tar_feciniE.setPreferredSize(new java.awt.Dimension(10, 18));
        PCondi.add(tar_feciniE);
        tar_feciniE.setBounds(240, 3, 70, 17);

        BirGrid.setText("Aceptar");
        BirGrid.setPreferredSize(new java.awt.Dimension(2, 2));
        PCondi.add(BirGrid);
        BirGrid.setBounds(370, 0, 90, 20);

        cLabel3.setText("Semana");
        PCondi.add(cLabel3);
        cLabel3.setBounds(100, 3, 50, 17);
        PCondi.add(eje_numeE);
        eje_numeE.setBounds(60, 3, 35, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        Pprinc.add(PCondi, gridBagConstraints);

        jt1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jt1.setPreferredSize(new java.awt.Dimension(40, 40));

        ArrayList v1=new ArrayList();
        v1.add("Producto");
        v1.add("Nombre");
        v1.add("Kilos"); // 0

        jt1.setCabecera(v1);
        jt1.setAnchoColumna(new int[]{50,200,70});
        jt1.setAlinearColumna(new int[]{0,0,2});
        jt1.setFormatoColumna(2, "##9.99");
        jt1.setAjustarGrid(true);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        Pprinc.add(jt1, gridBagConstraints);

        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jt.setPreferredSize(new java.awt.Dimension(40, 40));

        ArrayList v=new ArrayList();
        v.add("Producto");//0
        v.add("Nombre"); // 1
        v.add("Total"); // 2
        v.add("Kg.Desp"); // 3
        v.add("Kg.Venta"); // 4
        v.add("Pr.Venta"); // 5
        v.add("Pr.Medio"); // 6
        jt.setCabecera(v);
        jt.setAnchoColumna(new int[]{60,200,65,65,65,50,50});
        jt.setAlinearColumna(new int[]{0,0,2,2,2,2,2});
        jt.setFormatoColumna(2, "##,##9.99");
        jt.setFormatoColumna(3, "--,--9.99");
        jt.setFormatoColumna(4, "##,##9.99");
        jt.setFormatoColumna(5, "#9.99");
        jt.setFormatoColumna(6, "#9.99");
        jt.setAjustarGrid(true);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Pprinc.add(jt, gridBagConstraints);

        PPie.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        PPie.setMaximumSize(new java.awt.Dimension(480, 22));
        PPie.setMinimumSize(new java.awt.Dimension(480, 22));
        PPie.setPreferredSize(new java.awt.Dimension(480, 22));
        PPie.setLayout(null);

        cLabel7.setText("Fec.Stock");
        PPie.add(cLabel7);
        cLabel7.setBounds(0, 3, 60, 17);

        fecStockE.setMaximumSize(new java.awt.Dimension(10, 18));
        fecStockE.setMinimumSize(new java.awt.Dimension(10, 18));
        fecStockE.setPreferredSize(new java.awt.Dimension(10, 18));
        PPie.add(fecStockE);
        fecStockE.setBounds(60, 3, 70, 17);

        cLabel9.setText("Inic. Compra");
        PPie.add(cLabel9);
        cLabel9.setBounds(140, 3, 80, 17);

        fecIniComE.setMaximumSize(new java.awt.Dimension(10, 18));
        fecIniComE.setMinimumSize(new java.awt.Dimension(10, 18));
        fecIniComE.setPreferredSize(new java.awt.Dimension(10, 18));
        PPie.add(fecIniComE);
        fecIniComE.setBounds(220, 3, 70, 17);

        cLabel11.setText("Fin. Compra");
        PPie.add(cLabel11);
        cLabel11.setBounds(300, 3, 70, 17);

        fecFinComE.setMaximumSize(new java.awt.Dimension(10, 18));
        fecFinComE.setMinimumSize(new java.awt.Dimension(10, 18));
        fecFinComE.setPreferredSize(new java.awt.Dimension(10, 18));
        PPie.add(fecFinComE);
        fecFinComE.setBounds(380, 3, 70, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        Pprinc.add(PPie, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton BirGrid;
    private gnu.chu.controles.CPanel PCondi;
    private gnu.chu.controles.CPanel PPie;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CLabel cLabel11;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel9;
    private gnu.chu.controles.CTextField cta_semanaE;
    private gnu.chu.controles.CTextField eje_numeE;
    private gnu.chu.controles.CTextField fecFinComE;
    private gnu.chu.controles.CTextField fecIniComE;
    private gnu.chu.controles.CTextField fecStockE;
    private gnu.chu.controles.Cgrid jt;
    private gnu.chu.controles.Cgrid jt1;
    private gnu.chu.controles.CTextField tar_feciniE;
    // End of variables declaration//GEN-END:variables
}
