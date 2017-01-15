/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gnu.chu.anjelica.ventas;

import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.pad.MantRepres;
import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.camposdb.empPanel;
import gnu.chu.controles.StatusBar;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.ventana;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cpuente
 */
public class CLVentasMes extends ventana
{
    String REPRARG=null;
    gnu.chu.anjelica.menu menu;
    
public CLVentasMes(EntornoUsuario eu, Principal p)
  {
    this(eu, p, null);
  }

  public CLVentasMes(EntornoUsuario eu, Principal p, Hashtable ht)
  {

   EU=eu;
   vl=p.panel1;
   jf=p;
   eje=true;

   setTitulo("Consulta Ventas por Mes");

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
     setErrorInit(true);
   }
 }

 public CLVentasMes(gnu.chu.anjelica.menu p,EntornoUsuario eu) 
 {
     this.menu=p;
   EU=eu;
   vl=p.getLayeredPane();
   setTitulo("Consulta Ventas por Mes");
   eje=false;

   try  {
     jbInit();
   }
   catch (Exception e) {     
     setErrorInit(true);
   }
 }



private void jbInit() throws Exception
{
   iniciarFrame();

   this.setVersion("20176-01-13");
   statusBar = new StatusBar(this);

   initComponents();
   this.setSize(620,542);
   this.getContentPane().add(statusBar, BorderLayout.SOUTH);

   conecta();
}
  @Override
public void iniciarVentana() throws Exception
{
    
    Pcondi.setDefButton(Baceptar);

    jt.tableView.setToolTipText("Doble click encima linea para detalles venta");
   
    sbe_codiE.iniciar(dtStat, this, vl, EU);
    sbe_codiE.setAceptaNulo(true);
    avc_anoE.setValorInt(EU.ejercicio);

    MantRepres.llenaLinkBox(rep_codiE, dtCon1);
    pdconfig.llenaDiscr(dtStat, rut_codiE, pdconfig.D_RUTAS, EU.em_cod);
    pdconfig.llenaDiscr(dtStat, zon_codiE, pdconfig.D_ZONA,EU.em_cod);
//    fecIniE.setDesplazaX(150);
//    fecFinE.setDesplazaX(150);
    rut_codiE.getComboBox().setPreferredSize(new Dimension(150,18));
    rep_codiE.getComboBox().setPreferredSize(new Dimension(150,18));
//    pdconfig.llenaDiscr(dtStat, rep_codiE, "Cr",EU.em_cod);
     activarEventos();
//     cli_codiE.setText("");
     this.setEnabled(true);
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
                Baceptar_actionPerformed();
            }
        });
    }
    void Baceptar_actionPerformed()
    {
        Double[] kilos=new Double[24];
        
        String s="select distinct(cl.cli_codi) as cli_codi from v_albavec as c,v_cliente as cl where avc_ano>="+(avc_anoE.getValorInt()-1)+
            " and c.cli_codi = cl.cli_codi "+
            (rep_codiE.isNull()?"":" and rep_codi = '"+rep_codiE.getText()+"'")+
            (zon_codiE.isNull()?"":" and zon_codi = '"+zon_codiE.getText()+"'")+
            (rut_codiE.isNull()?"":" and rut_codi = '"+rut_codiE.getText()+"'")+           
//            " and cl.cli_codi=27210"+
            " order by cli_codi";
  
        try  {
            jt.removeAllDatos();
            if (! dtCon1.select(s))
            {
                msgBox("No encontradas ventas para este periodo");
                return;
            }
            s="select sum(avc_kilos) as kilos from v_albavec where cli_codi= ?"+
                    " and avc_ano = ?"+
                    " and avc_fecalb between ? and ? ";
            PreparedStatement psKilos=dtStat.getPreparedStatement(s);
            ResultSet rsKilos;
            s="select * from v_cliente where cli_codi= ?";
            PreparedStatement psCli=dtStat.getPreparedStatement(s);
            ResultSet rsCli;
            do
            {
                int nAno=0;
                for (int ano=avc_anoE.getValorInt()-1;ano<=avc_anoE.getValorInt();ano++)
                {
                    s="select * from calendario where cal_ano ="+ano+" order by cal_mes";
                    if (!dtAdd.select(s))
                    {
                           msgBox("No encontrado calendario para año: "+ano);
                           return; 
                    }
                    int mes=0;
                    do
                    {
                         psKilos.setInt(1, dtCon1.getInt("cli_codi"));
                         psKilos.setInt(2, ano);
                         psKilos.setDate(3, dtAdd.getDate("cal_fecini"));
                         psKilos.setDate(4, dtAdd.getDate("cal_fecfin"));
                         rsKilos=psKilos.executeQuery();
                         rsKilos.next();
                         kilos[(mes*2)+nAno]=rsKilos.getDouble("kilos");
                         mes++;
                    } while (dtAdd.next());
                    nAno++;                    
                }
                ArrayList v=new ArrayList();
                v.add(dtCon1.getInt("cli_codi"));
                psCli.setInt(1,dtCon1.getInt("cli_codi"));
                rsCli=psCli.executeQuery();
                rsCli.next();
                v.add(rsCli.getString("cli_nomb"));
                v.add(rsCli.getString("cli_pobl"));                
                for (int n=0;n<24;n++)
                {
                    v.add(Formatear.format(kilos[n],"----,--9.9"));
                }
                jt.addLinea(v);
            } while(dtCon1.next());
        } catch (SQLException  k)
        {
            Error("Error al buscar ventas",k);
        }
        
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
        Pcondi = new gnu.chu.controles.CPanel();
        cLabel1 = new gnu.chu.controles.CLabel();
        avc_anoE = new gnu.chu.controles.CTextField(Types.DECIMAL,"9999");
        Baceptar = new gnu.chu.controles.CButton(Iconos.getImageIcon("check"));
        cLabel16 = new gnu.chu.controles.CLabel();
        rep_codiE = new gnu.chu.controles.CLinkBox();
        cLabel18 = new gnu.chu.controles.CLabel();
        rut_codiE = new gnu.chu.controles.CLinkBox();
        cLabel4 = new gnu.chu.controles.CLabel();
        sbe_codiE = new gnu.chu.camposdb.sbePanel();
        cLabel19 = new gnu.chu.controles.CLabel();
        zon_codiE = new gnu.chu.controles.CLinkBox();
        jt = new gnu.chu.controles.Cgrid(27);

        Pprinc.setLayout(new java.awt.GridBagLayout());

        Pcondi.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        Pcondi.setLayout(null);

        cLabel1.setText("Año");
        Pcondi.add(cLabel1);
        cLabel1.setBounds(10, 10, 22, 15);
        Pcondi.add(avc_anoE);
        avc_anoE.setBounds(40, 10, 40, 17);

        Baceptar.setText("Aceptar");
        Pcondi.add(Baceptar);
        Baceptar.setBounds(470, 20, 90, 30);

        cLabel16.setText("Repres.");
        cLabel16.setPreferredSize(new java.awt.Dimension(60, 18));
        Pcondi.add(cLabel16);
        cLabel16.setBounds(10, 30, 50, 18);

        rep_codiE.setAncTexto(30);
        rep_codiE.setFormato(Types.CHAR,"X",2);
        rep_codiE.setMayusculas(true);
        rep_codiE.setPreferredSize(new java.awt.Dimension(152, 18));
        Pcondi.add(rep_codiE);
        rep_codiE.setBounds(60, 30, 190, 18);

        cLabel18.setText("Ruta");
        cLabel18.setPreferredSize(new java.awt.Dimension(60, 18));
        Pcondi.add(cLabel18);
        cLabel18.setBounds(260, 30, 30, 18);

        rut_codiE.setAncTexto(30);
        rut_codiE.setMayusculas(true);
        rut_codiE.setFormato(Types.CHAR,"X",2);
        rut_codiE.setPreferredSize(new java.awt.Dimension(152, 18));
        Pcondi.add(rut_codiE);
        rut_codiE.setBounds(290, 30, 170, 18);

        cLabel4.setText("Subempresa");
        Pcondi.add(cLabel4);
        cLabel4.setBounds(100, 10, 90, 18);
        Pcondi.add(sbe_codiE);
        sbe_codiE.setBounds(190, 10, 37, 20);

        cLabel19.setText("Zona");
        cLabel19.setPreferredSize(new java.awt.Dimension(60, 18));
        Pcondi.add(cLabel19);
        cLabel19.setBounds(260, 10, 30, 18);

        zon_codiE.setAncTexto(30);
        zon_codiE.setPreferredSize(new java.awt.Dimension(152, 18));
        Pcondi.add(zon_codiE);
        zon_codiE.setBounds(290, 10, 170, 18);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 579;
        gridBagConstraints.ipady = 59;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        Pprinc.add(Pcondi, gridBagConstraints);

        ArrayList v=new ArrayList();
        v.add("Cliente");
        v.add("Nombre");
        v.add("Poblacion");
        v.add("1/Ant");
        v.add("1/Act");
        v.add("2/Ant");
        v.add("2/Act");
        v.add("3/Ant");
        v.add("3/Act");
        v.add("4/Ant");
        v.add("4/Act");
        v.add("5/Ant");
        v.add("5/Act");
        v.add("6/Ant");
        v.add("6/Act");
        v.add("7/Ant");
        v.add("7/Act");
        v.add("8/Ant");
        v.add("8/Act");
        v.add("9/Ant");
        v.add("9/Act");
        v.add("10/Ant");
        v.add("10/Act");
        v.add("11/Ant");
        v.add("11/Act");
        v.add("12/Ant");
        v.add("12/Act");
        jt.setCabecera(v);
        jt.setAnchoColumna(new int[]{40,150,100,40,40,40,40,40,40,
            40,40,40,40,40,40,
            40,40,40,40,40,40,
            40,40,40,40,40,40
        });
        jt.setAlinearColumna(new int[]{2,0,0,2,2,2,2,2,2,
            2,2,2,2,2,2,
            2,2,2,2,2,2,
            2,2,2,2,2,2,
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 579;
        gridBagConstraints.ipady = 169;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Pprinc.add(jt, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CPanel Pcondi;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CTextField avc_anoE;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel16;
    private gnu.chu.controles.CLabel cLabel18;
    private gnu.chu.controles.CLabel cLabel19;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.Cgrid jt;
    private gnu.chu.controles.CLinkBox rep_codiE;
    private gnu.chu.controles.CLinkBox rut_codiE;
    private gnu.chu.camposdb.sbePanel sbe_codiE;
    private gnu.chu.controles.CLinkBox zon_codiE;
    // End of variables declaration//GEN-END:variables
}
