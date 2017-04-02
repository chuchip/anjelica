package gnu.chu.anjelica.ventas;
/**
 *
 * <p>Titulo: CLVentaMes</p>
 * <p>Descripción: Consulta y Listdo de ventas por meses</p>
 * <p>Copyright: Copyright (c) 2005-2017
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
 * <p>Empresa: miSL</p>
 * @author chuchiP
 * @version 1.0
 */
import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.pad.MantRepres;
import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.controles.StatusBar;
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
import java.util.ArrayList;
import java.util.Hashtable;

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

   this.setVersion("20176-01-16");
   statusBar = new StatusBar(this);

   initComponents();
   this.setSize(620,542);
   this.getContentPane().add(statusBar, BorderLayout.SOUTH);

   conecta();
}
  @Override
public void iniciarVentana() throws Exception
{
    
    Pcondi.setDefButton(Baceptar.getBotonAccion());

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
                Baceptar_actionPerformed(e.getActionCommand());
            }
        });
    }
    void Baceptar_actionPerformed(String accion)
    {
        Double[] kilos=new Double[26];
        Double[] total=new Double[26];
        
        String s="select cl.cli_codi,cl.cli_nomb,cl.cli_pobl from v_albavec as c,v_cliente as cl where avc_ano>="+(avc_anoE.getValorInt()-1)+
            " and c.cli_codi = cl.cli_codi "+
            (rep_codiE.isNull()?"":" and cl.rep_codi = '"+rep_codiE.getText()+"'")+
            (zon_codiE.isNull()?"":" and cl.zon_codi = '"+zon_codiE.getText()+"'")+
            (rut_codiE.isNull()?"":" and cl.rut_codi = '"+rut_codiE.getText()+"'")+   
            (sbe_codiE.getValorInt()==0 ?"":" and cl.sbe_codi = '"+sbe_codiE.getText()+"'")+   
//            " and cl.cli_codi=27210"+
            " group by cl.cli_pobl,cl.cli_nomb,cl.cli_codi"+
            " order by cl.cli_pobl,cl.cli_nomb";
  
        try  {
            jt.removeAllDatos();
            if (! dtCon1.select(s))
            {
                msgBox("No encontradas ventas para este periodo");
                return;
            }
            ArrayList cab=new ArrayList();
            cab.add("Cliente");
            cab.add("Nombre");
            cab.add("Poblacion");
            for (int n=0;n<26;n++)
            {
                total[n]=(double)0;
            }
            for (int n=0;n<12;n++)
            {
                cab.add(Formatear.format(n+1,"99")+"/"+(avc_anoE.getValorInt()-2001));
                cab.add(Formatear.format(n+1,"99")+"/"+(avc_anoE.getValorInt()-2000));
            }
            cab.add("Tot."+(avc_anoE.getValorInt()-2001));
            cab.add("Tot."+(avc_anoE.getValorInt()-2000));
            jt.setCabecera(cab);
            configurarGrid();
            switch (accion)
            {
                case "Comisiones":
                     s="select sum(avl_canti*(avl_prven-avl_profer))  as kilos from v_albventa where cli_codi= ?"+
                    (rep_codiE.isNull() || ! opReprAlb.isSelected() ?"":" and avc_repres = '"+rep_codiE.getText()+"'")+
                    " and avl_profer > 0 and avl_prbase > 0 "+
                    " and avc_ano = ?"+
                    " and avc_fecalb between ? and ? ";
                    break;
                case "Albaranes":
                  s="select count(*) as kilos from v_albavec where cli_codi= ?"+
                    (rep_codiE.isNull() || ! opReprAlb.isSelected() ?"":" and avc_repres = '"+rep_codiE.getText()+"'")+
                    " and avc_ano = ?"+
                    " and avc_fecalb between ? and ? ";
                   break;
                case "Clientes":
                  s="select count(distinct(cli_codi)) as kilos from v_albavec where cli_codi= ?"+
                    (rep_codiE.isNull() || ! opReprAlb.isSelected() ?"":" and avc_repres = '"+rep_codiE.getText()+"'")+
                    " and avc_ano = ?"+
                    " and avc_fecalb between ? and ? ";
                  break;
                default:
                  s="select sum(avc_kilos) as kilos from v_albavec where cli_codi= ?"+
                    (rep_codiE.isNull() || ! opReprAlb.isSelected()?"":" and avc_repres = '"+rep_codiE.getText()+"'")+
                    " and avc_ano = ?"+
                    " and avc_fecalb between ? and ? ";
            }
          
            PreparedStatement psKilos=dtStat.getPreparedStatement(s);
            ResultSet rsKilos;
           
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
                    kilos[24+nAno]=(double)0;
                    do
                    {
                         psKilos.setInt(1, dtCon1.getInt("cli_codi"));
                         psKilos.setInt(2, ano);
                         psKilos.setDate(3, dtAdd.getDate("cal_fecini"));
                         psKilos.setDate(4, dtAdd.getDate("cal_fecfin"));
                         rsKilos=psKilos.executeQuery();
                         rsKilos.next();
                         kilos[(mes*2)+nAno]=rsKilos.getDouble("kilos");
                         kilos[24+nAno]+=rsKilos.getDouble("kilos");
                         mes++;
                    } while (dtAdd.next());
                    nAno++;                    
                }
                ArrayList v=new ArrayList();
                v.add(dtCon1.getInt("cli_codi"));               
                v.add(dtCon1.getString("cli_nomb"));
                v.add(dtCon1.getString("cli_pobl"));                
                for (int n=0;n<26;n++)
                {
                    total[n]+=kilos[n];
                    v.add(Formatear.format(kilos[n],"----,--9.9"));                    
                }
                jt.addLinea(v);
            } while(dtCon1.next());
            ArrayList v=new ArrayList();
            v.add("");
            v.add("");
            v.add("Total");
            for (int n=0;n<26;n++)
            {
                 v.add(Formatear.format(total[n],"----,--9.9"));       
            }
            jt.addLinea(v);
        } catch (SQLException  k)
        {
            Error("Error al buscar ventas",k);
        }
        
    }
    void configurarGrid()
    {
        jt.setAnchoColumna(new int[]{40,150,100,
            55,55,55,55,55,55,
            55,55,55,55,55,55,
            55,55,55,55,55,55,
            55,55,55,55,55,55,
            55,55
        });
        jt.setAlinearColumna(new int[]{2,0,0,2,2,2,2,2,2,
            2,2,2,2,2,2,
            2,2,2,2,2,2,
            2,2,2,2,2,2,
            2,2
        });
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
        cLabel16 = new gnu.chu.controles.CLabel();
        rep_codiE = new gnu.chu.controles.CLinkBox();
        cLabel18 = new gnu.chu.controles.CLabel();
        rut_codiE = new gnu.chu.controles.CLinkBox();
        cLabel4 = new gnu.chu.controles.CLabel();
        sbe_codiE = new gnu.chu.camposdb.sbePanel();
        cLabel19 = new gnu.chu.controles.CLabel();
        zon_codiE = new gnu.chu.controles.CLinkBox();
        Baceptar = new gnu.chu.controles.CButtonMenu(Iconos.getImageIcon("check"));
        opReprAlb = new gnu.chu.controles.CCheckBox();
        jt = new gnu.chu.controles.Cgrid(29);

        Pprinc.setLayout(new java.awt.GridBagLayout());

        Pcondi.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        Pcondi.setLayout(null);

        cLabel1.setText("Año");
        Pcondi.add(cLabel1);
        cLabel1.setBounds(10, 5, 22, 17);
        Pcondi.add(avc_anoE);
        avc_anoE.setBounds(40, 5, 40, 17);

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
        cLabel4.setBounds(100, 5, 90, 17);
        Pcondi.add(sbe_codiE);
        sbe_codiE.setBounds(190, 5, 37, 17);

        cLabel19.setText("Zona");
        cLabel19.setPreferredSize(new java.awt.Dimension(60, 18));
        Pcondi.add(cLabel19);
        cLabel19.setBounds(260, 5, 30, 17);

        zon_codiE.setAncTexto(30);
        zon_codiE.setMayusculas(true);
        zon_codiE.setPreferredSize(new java.awt.Dimension(152, 18));
        Pcondi.add(zon_codiE);
        zon_codiE.setBounds(290, 5, 170, 17);

        Baceptar.addMenu("Kilos", "K");
        Baceptar.addMenu("Albaranes", "A");
        Baceptar.addMenu("Clientes", "C");
        Baceptar.addMenu("Comisiones", "M");
        Baceptar.setText("Consultar");
        Pcondi.add(Baceptar);
        Baceptar.setBounds(470, 30, 110, 26);

        opReprAlb.setText("Repr. Albaran");
        opReprAlb.setToolTipText("Forzar Representante de Albaran");
        opReprAlb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opReprAlbActionPerformed(evt);
            }
        });
        Pcondi.add(opReprAlb);
        opReprAlb.setBounds(470, 5, 100, 17);

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

        configurarGrid();
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

    private void opReprAlbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opReprAlbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_opReprAlbActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButtonMenu Baceptar;
    private gnu.chu.controles.CPanel Pcondi;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CTextField avc_anoE;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel16;
    private gnu.chu.controles.CLabel cLabel18;
    private gnu.chu.controles.CLabel cLabel19;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.Cgrid jt;
    private gnu.chu.controles.CCheckBox opReprAlb;
    private gnu.chu.controles.CLinkBox rep_codiE;
    private gnu.chu.controles.CLinkBox rut_codiE;
    private gnu.chu.camposdb.sbePanel sbe_codiE;
    private gnu.chu.controles.CLinkBox zon_codiE;
    // End of variables declaration//GEN-END:variables
}
