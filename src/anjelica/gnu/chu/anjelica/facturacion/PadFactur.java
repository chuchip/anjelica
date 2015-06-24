/**
 *
 * <p>Titulo: PadFactur </p>
 * <p>Descripción: Mantenimiento Facturas de Ventas</p>
 * <p>Copyright: Copyright (c) 2005-2015
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
 * @author chuchiP
 * @version 1.3 Puesta opcion de enviar por fax.
 *          1.2 Incluida Lineas de comentarios de cabecera y Pie.
 *          Pasado diseño a formato NetBeans
 *          1.1 Incluida serie de factura
 */

package gnu.chu.anjelica.facturacion;

import gnu.chu.Menu.*;
import gnu.chu.anjelica.DatosIVA;
import gnu.chu.anjelica.pad.MantTipoIVA;
import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.anjelica.pad.pdejerci;
import gnu.chu.anjelica.pad.pdempresa;
import gnu.chu.anjelica.pad.pdnumeracion;
import gnu.chu.anjelica.riesgos.clFactCob;
import gnu.chu.anjelica.ventas.actCabAlbFra;
import gnu.chu.camposdb.*;
import gnu.chu.controles.*;
import gnu.chu.eventos.GridAdapter;
import gnu.chu.eventos.GridEvent;
import gnu.chu.hylafax.IFFax;
import gnu.chu.interfaces.*;
import gnu.chu.mail.IFMail;
import gnu.chu.sql.*;
import gnu.chu.utilidades.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.*;
import java.util.*;

public class PadFactur extends ventanaPad   implements PAD {
  double impLinDtCom;
  boolean proIndtco;
  private boolean IMPFRATEXTO=false;
  int numDec=2;
  int numDecPrecio=4;  
  String FORMDECPRECIO=".9999";
  public final static String IMPPLANO="Texto Plano";
  public final static String IMPGRAFICO="Gráfico";
  public final static String IMPGRAFPRE="Gráf.Preim";
 
  public final static String SENDFAX="Fax";
  public final static String SENDMAIL="E-Mail";
  lisfactu lifact=null;
  actCabAlbFra datCab;
  String s;
  boolean MOD_CONS=true;
  int tiiIva=0;
  DatosTabla dtAux;
  CButtonMenu Bprint=new CButtonMenu(Iconos.getImageIcon("print"));
  IFFax ifFax=null;
  IFMail ifMail=null;
  proPanel pro_codiE=new proPanel();
  CTextField rec_numeE = new CTextField(Types.DECIMAL,"###9");
  CTextField rec_importE = new CTextField(Types.DECIMAL,"--,---,--9.99");
  CTextField rec_fecvtoE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CTextField ban_codiE = new CTextField(Types.DECIMAL,"9999");
  CTextField rec_baoficE = new CTextField(Types.DECIMAL,"9999");
  CTextField rec_badicoE = new CTextField(Types.DECIMAL,"99");
  CTextField rec_bacuenE = new CTextField(Types.DECIMAL,"9999999999");
  CTextField fvc_comencE=new CTextField(Types.CHAR,"X",76);
  CTextField fvc_comenpE=new CTextField(Types.CHAR,"X",76);

   
 public PadFactur(EntornoUsuario eu, Principal p,Hashtable ht)
 {
   EU = eu;
   vl = p.panel1;
   jf = p;
   eje = true;

   try
   {
     if (ht != null)
     {
         MOD_CONS = Boolean.valueOf(ht.get("modCons").toString()).
             booleanValue();
     }
     setTitulo("Mant. Facturas Ventas");

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

 public PadFactur(gnu.chu.anjelica.menu p, EntornoUsuario eu)
 {
   this(p,eu,null);
 }
 public PadFactur(gnu.chu.anjelica.menu p, EntornoUsuario eu,Hashtable ht)
 {
   EU = eu;
   vl = p.getLayeredPane();
   eje = false;

   try
   {
       if (ht != null)
       {
           MOD_CONS = Boolean.valueOf(ht.get("modCons").toString()).
               booleanValue();
       }
       setTitulo("Mant. Facturas Ventas");

     jf=null;
     jbInit();
   }
   catch (Exception e)
   {
    ErrorInit(e);
   }
 }

    private void jbInit() throws Exception 
    {
        statusBar = new StatusBar(this);
        nav = new navegador(this, dtCons, false, MOD_CONS ? navegador.CURYCON : navegador.NORMAL);
       
        iniciarFrame();

        this.setVersion("2015-06-19" + (MOD_CONS ? "SOLO LECTURA" : ""));
        strSql = getStrSql();
        IMPFRATEXTO=EU.getValorParam("impFraTexto",IMPFRATEXTO);
        this.getContentPane().add(nav, BorderLayout.NORTH);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        this.setPad(this);
        navActivarAll();
        dtCons.setLanzaDBCambio(false);
        Bprint.setToolTipText("Listar la Factura Activa");
     
        Bprint.setPreferredSize(new Dimension(40, 24));
        Bprint.setMinimumSize(new Dimension(40, 24));
        Bprint.setMinimumSize(new Dimension(40, 24));
        Bprint.addMenu(IMPGRAFICO);
        Bprint.addMenu(IMPGRAFPRE);
        if (IMPFRATEXTO)
            Bprint.addMenu(IMPPLANO);
        Bprint.addMenu(SENDFAX);
        Bprint.addMenu(SENDMAIL);
        
        
        statusBar.add(Bprint, new GridBagConstraints(9, 0, 1, 2, 0.0, 0.0
                                        , GridBagConstraints.EAST,
                                        GridBagConstraints.VERTICAL,
                                        new Insets(0, 5, 0, 0), 0, 0));

        initComponents();
        this.setSize(new Dimension(680, 537));
        confGrids();
        iniciarBotones(Baceptar, Bcancelar);

        conecta();
        activar(false);
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

        cTextField1 = new gnu.chu.controles.CTextField();
        Pprinc = new gnu.chu.controles.CPanel();
        Pcabe = new gnu.chu.controles.CPanel();
        cLabel1 = new gnu.chu.controles.CLabel();
        emp_codiE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##");
        cLabel2 = new gnu.chu.controles.CLabel();
        fvc_anoE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        cLabel3 = new gnu.chu.controles.CLabel();
        fvc_serieE = new gnu.chu.controles.CComboBox();
        cLabel4 = new gnu.chu.controles.CLabel();
        fvc_numeE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#####9");
        cLabel5 = new gnu.chu.controles.CLabel();
        fvc_fecfraE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        fvc_impresE = new gnu.chu.controles.CLabel();
        cLabel6 = new gnu.chu.controles.CLabel();
        cLabel7 = new gnu.chu.controles.CLabel();
        cli_pobleE = new gnu.chu.controles.CLabel();
        fvc_modifE = new gnu.chu.controles.CComboBox();
        cli_codiE = new gnu.chu.camposdb.cliAvcPanel() {
            public void afterFocusLost(boolean error)
            {
                afterFocusLostCli(error);
            }
        }
        ;
        Ptab = new gnu.chu.controles.CTabbedPane();
        jt = new gnu.chu.controles.Cgrid(6);
        Pcobros = new gnu.chu.controles.CPanel();
        cLabel19 = new gnu.chu.controles.CLabel();
        jtCobr = new gnu.chu.controles.Cgrid(8);
        cLabel20 = new gnu.chu.controles.CLabel();
        jtGiros = new gnu.chu.controles.CGridEditable(7);
        BrecCobr = new gnu.chu.controles.CButton("Recalc",Iconos.getImageIcon("calc"));
        Pcomen = new gnu.chu.controles.CPanel();
        cabecL = new gnu.chu.controles.CLabel();
        jtComCa = new gnu.chu.controles.CGridEditable(1);
        pieL = new gnu.chu.controles.CLabel();
        jtComPie = new gnu.chu.controles.CGridEditable(1);
        Ppie = new gnu.chu.controles.CPanel();
        cLabel8 = new gnu.chu.controles.CLabel();
        fvc_sumlinE = new gnu.chu.controles.CTextField(Types.DECIMAL,"-,---,--9.99");
        cLabel9 = new gnu.chu.controles.CLabel();
        fvc_dtoppE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#9.99");
        fvc_impdppE = new gnu.chu.controles.CTextField(Types.DECIMAL,"-,---,--9.99");
        cLabel10 = new gnu.chu.controles.CLabel();
        fvc_dtocomE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#9.99");
        fvc_impdcoE = new gnu.chu.controles.CTextField(Types.DECIMAL,"-,---,--9.99");
        cLabel11 = new gnu.chu.controles.CLabel();
        fvc_basimpE = new gnu.chu.controles.CTextField(Types.DECIMAL,"-,---,--9.99");
        cLabel12 = new gnu.chu.controles.CLabel();
        fvc_porivaE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#9.99");
        fvc_impivaE = new gnu.chu.controles.CTextField(Types.DECIMAL,"-,---,--9.99");
        cLabel13 = new gnu.chu.controles.CLabel();
        fvc_porreqE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#9.99");
        fvc_imprecE = new gnu.chu.controles.CTextField(Types.DECIMAL,"-,---,--9.99");
        cLabel14 = new gnu.chu.controles.CLabel();
        fvc_sumtotE = new gnu.chu.controles.CTextField(Types.DECIMAL,"-,---,--9.99");
        cLabel15 = new gnu.chu.controles.CLabel();
        fvc_cobradE = new gnu.chu.controles.CComboBox();
        cLabel16 = new gnu.chu.controles.CLabel();
        fvc_traspE = new gnu.chu.controles.CComboBox();
        cLabel17 = new gnu.chu.controles.CLabel();
        fvc_impcobE = new gnu.chu.controles.CTextField(Types.DECIMAL,"-,---,--9.99");
        cLabel18 = new gnu.chu.controles.CLabel();
        fpa_codiE = new gnu.chu.camposdb.fpaPanel();
        Brecalc = new gnu.chu.controles.CButton("Recalc",Iconos.getImageIcon("calc"));
        opAgrlin = new gnu.chu.controles.CCheckBox();
        Baceptar = new gnu.chu.controles.CButton();
        Bcancelar = new gnu.chu.controles.CButton();

        cTextField1.setText("cTextField1");

        Pprinc.setLayout(new java.awt.GridBagLayout());

        Pcabe.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        cLabel1.setText("Empresa");

        cLabel2.setText("Año");

        cLabel3.setText("Serie");

        cLabel4.setText("Num.Fra");

        cLabel5.setText("Fecha Fra.");

        cLabel6.setText("Cliente");

        cLabel7.setText("Poblacion");

        cli_pobleE.setBackground(java.awt.Color.orange);
        cli_pobleE.setOpaque(true);

        fvc_modifE.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "" }));

        org.jdesktop.layout.GroupLayout PcabeLayout = new org.jdesktop.layout.GroupLayout(Pcabe);
        Pcabe.setLayout(PcabeLayout);
        PcabeLayout.setHorizontalGroup(
            PcabeLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(PcabeLayout.createSequentialGroup()
                .add(PcabeLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(PcabeLayout.createSequentialGroup()
                        .add(cLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(emp_codiE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(cLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(fvc_anoE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 42, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(fvc_serieE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 43, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(fvc_numeE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 46, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(6, 6, 6)
                        .add(fvc_fecfraE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(fvc_impresE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(PcabeLayout.createSequentialGroup()
                        .add(cLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(PcabeLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(PcabeLayout.createSequentialGroup()
                                .add(cLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(cli_pobleE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 302, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(fvc_modifE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .add(cli_codiE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PcabeLayout.setVerticalGroup(
            PcabeLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(PcabeLayout.createSequentialGroup()
                .add(2, 2, 2)
                .add(PcabeLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(cLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(emp_codiE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(fvc_serieE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(fvc_numeE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(fvc_fecfraE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(fvc_anoE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(2, 2, 2)
                .add(PcabeLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(cLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cli_codiE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(2, 2, 2)
                .add(PcabeLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(PcabeLayout.createSequentialGroup()
                        .add(fvc_modifE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .add(2, 2, 2))
                    .add(PcabeLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(cLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(cli_pobleE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
            .add(fvc_impresE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );

        PcabeLayout.linkSize(new java.awt.Component[] {fvc_anoE, fvc_serieE}, org.jdesktop.layout.GroupLayout.VERTICAL);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 2, 2);
        Pprinc.add(Pcabe, gridBagConstraints);

        Ptab.addTab("Lineas Fra", jt);

        cLabel19.setBackground(java.awt.Color.orange);
        cLabel19.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        cLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cLabel19.setText("Cobros Realizados");
        cLabel19.setOpaque(true);

        jtCobr.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtCobr.setBuscarVisible(false);

        cLabel20.setBackground(java.awt.Color.orange);
        cLabel20.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        cLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cLabel20.setText("Recibos Pendientes");
        cLabel20.setOpaque(true);

        jtGiros.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        org.jdesktop.layout.GroupLayout PcobrosLayout = new org.jdesktop.layout.GroupLayout(Pcobros);
        Pcobros.setLayout(PcobrosLayout);
        PcobrosLayout.setHorizontalGroup(
            PcobrosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jtCobr, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 710, Short.MAX_VALUE)
            .add(jtGiros, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 710, Short.MAX_VALUE)
            .add(PcobrosLayout.createSequentialGroup()
                .add(PcobrosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(PcobrosLayout.createSequentialGroup()
                        .add(27, 27, 27)
                        .add(cLabel19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 121, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(PcobrosLayout.createSequentialGroup()
                        .add(25, 25, 25)
                        .add(cLabel20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 136, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(153, 153, 153)
                        .add(BrecCobr, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 158, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PcobrosLayout.setVerticalGroup(
            PcobrosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(PcobrosLayout.createSequentialGroup()
                .add(cLabel19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(22, 22, 22)
                .add(jtCobr, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(PcobrosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(BrecCobr, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cLabel20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jtGiros, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        PcobrosLayout.linkSize(new java.awt.Component[] {BrecCobr, cLabel20}, org.jdesktop.layout.GroupLayout.VERTICAL);

        Ptab.addTab("Cobros", Pcobros);

        Pcomen.setLayout(new java.awt.GridBagLayout());

        cabecL.setBackground(java.awt.Color.orange);
        cabecL.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        cabecL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cabecL.setText("Cabecera");
        cabecL.setMaximumSize(new java.awt.Dimension(110, 18));
        cabecL.setMinimumSize(new java.awt.Dimension(110, 18));
        cabecL.setOpaque(true);
        cabecL.setPreferredSize(new java.awt.Dimension(110, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 49;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        Pcomen.add(cabecL, gridBagConstraints);

        jtComCa.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Pcomen.add(jtComCa, gridBagConstraints);

        pieL.setBackground(java.awt.Color.orange);
        pieL.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        pieL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pieL.setText("Pie ");
        pieL.setMaximumSize(new java.awt.Dimension(110, 18));
        pieL.setMinimumSize(new java.awt.Dimension(110, 18));
        pieL.setOpaque(true);
        pieL.setPreferredSize(new java.awt.Dimension(110, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        Pcomen.add(pieL, gridBagConstraints);

        jtComPie.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Pcomen.add(jtComPie, gridBagConstraints);

        Ptab.addTab("Comentarios", Pcomen);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        Pprinc.add(Ptab, gridBagConstraints);

        Ppie.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Ppie.setMinimumSize(new java.awt.Dimension(640, 114));
        Ppie.setPreferredSize(new java.awt.Dimension(640, 114));
        Ppie.setLayout(null);

        cLabel8.setText("Imp. Lineas");
        Ppie.add(cLabel8);
        cLabel8.setBounds(2, 3, 72, 15);

        fvc_sumlinE.setEnabled(false);
        Ppie.add(fvc_sumlinE);
        fvc_sumlinE.setBounds(78, 2, 75, 17);

        cLabel9.setText("Dto. PP");
        Ppie.add(cLabel9);
        cLabel9.setBounds(175, 3, 38, 15);

        fvc_dtoppE.setEnabled(false);
        Ppie.add(fvc_dtoppE);
        fvc_dtoppE.setBounds(217, 2, 37, 17);

        fvc_impdppE.setBackground(java.awt.Color.orange);
        fvc_impdppE.setEnabled(false);
        Ppie.add(fvc_impdppE);
        fvc_impdppE.setBounds(257, 2, 65, 17);

        cLabel10.setText("Dto. Comercial");
        Ppie.add(cLabel10);
        cLabel10.setBounds(335, 3, 80, 15);

        fvc_dtocomE.setEnabled(false);
        Ppie.add(fvc_dtocomE);
        fvc_dtocomE.setBounds(419, 2, 37, 17);

        fvc_impdcoE.setBackground(java.awt.Color.orange);
        fvc_impdcoE.setEnabled(false);
        Ppie.add(fvc_impdcoE);
        fvc_impdcoE.setBounds(459, 2, 65, 17);

        cLabel11.setText("B.Imponible");
        Ppie.add(cLabel11);
        cLabel11.setBounds(2, 22, 72, 15);

        fvc_basimpE.setEnabled(false);
        Ppie.add(fvc_basimpE);
        fvc_basimpE.setBounds(78, 21, 75, 17);

        cLabel12.setText("IVA");
        Ppie.add(cLabel12);
        cLabel12.setBounds(175, 22, 38, 15);

        fvc_porivaE.setEnabled(false);
        Ppie.add(fvc_porivaE);
        fvc_porivaE.setBounds(217, 21, 37, 17);

        fvc_impivaE.setEnabled(false);
        Ppie.add(fvc_impivaE);
        fvc_impivaE.setBounds(257, 21, 65, 17);

        cLabel13.setText("Recargo Equiv.");
        Ppie.add(cLabel13);
        cLabel13.setBounds(335, 22, 80, 15);

        fvc_porreqE.setEnabled(false);
        Ppie.add(fvc_porreqE);
        fvc_porreqE.setBounds(419, 21, 37, 17);

        fvc_imprecE.setEnabled(false);
        Ppie.add(fvc_imprecE);
        fvc_imprecE.setBounds(459, 21, 65, 17);

        cLabel14.setText("Imp. Factura");
        Ppie.add(cLabel14);
        cLabel14.setBounds(2, 44, 67, 15);
        Ppie.add(fvc_sumtotE);
        fvc_sumtotE.setBounds(78, 42, 75, 17);

        cLabel15.setText("Tot.Cobr");
        Ppie.add(cLabel15);
        cLabel15.setBounds(2, 63, 67, 17);

        fvc_cobradE.setEnabled(false);
        Ppie.add(fvc_cobradE);
        fvc_cobradE.setBounds(78, 63, 75, 17);

        cLabel16.setText("Contabil.");
        Ppie.add(cLabel16);
        cLabel16.setBounds(175, 63, 56, 17);

        fvc_traspE.setEnabled(false);
        Ppie.add(fvc_traspE);
        fvc_traspE.setBounds(235, 63, 46, 17);

        cLabel17.setText("Imp.Cobrado");
        Ppie.add(cLabel17);
        cLabel17.setBounds(175, 45, 71, 15);

        fvc_impcobE.setEnabled(false);
        Ppie.add(fvc_impcobE);
        fvc_impcobE.setBounds(254, 44, 75, 17);

        cLabel18.setText("F.Pago");
        Ppie.add(cLabel18);
        cLabel18.setBounds(285, 63, 39, 17);
        Ppie.add(fpa_codiE);
        fpa_codiE.setBounds(335, 63, 261, 17);

        Brecalc.setText("Recalc");
        Ppie.add(Brecalc);
        Brecalc.setBounds(454, 40, 77, 22);

        opAgrlin.setSelected(true);
        opAgrlin.setText("Agr. Lineas");
        opAgrlin.setMaximumSize(new java.awt.Dimension(90, 18));
        opAgrlin.setMinimumSize(new java.awt.Dimension(90, 18));
        Ppie.add(opAgrlin);
        opAgrlin.setBounds(335, 42, 113, 18);

        Baceptar.setText("Aceptar");
        Ppie.add(Baceptar);
        Baceptar.setBounds(175, 81, 105, 28);

        Bcancelar.setText("Cancelar");
        Ppie.add(Bcancelar);
        Bcancelar.setBounds(300, 81, 105, 28);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 10, 2);
        Pprinc.add(Ppie, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
 
 void confGrids() throws Exception
 {
      ArrayList vv = new ArrayList();
    vv.add("Producto"); // 0
    vv.add("Nombre Producto"); // 1
    vv.add("Cantidad"); // 2
    vv.add("Precio"); // 3
    vv.add("Importe"); // 4
    vv.add("NL"); // 5
    jt.setCabecera(vv);
    jt.setAnchoColumna(new int[]  {52, 273, 72, 72, 87, 30});
    jt.setAjustarGrid(true);
    jt.setFormatoColumna(2, "---,--9.99");
    jt.setFormatoColumna(3, "---,--9"+FORMDECPRECIO); // Precio
    jt.setFormatoColumna(4, "--,---,--9.99"); // Importe
    jt.setAlinearColumna(new int[]{0, 0, 2,  2, 2, 2});
    
     ArrayList v=new ArrayList();
    v.add("Tip.Cob."); // 0 Tipo Cobro
    v.add("Fec.Cobro"); // 1 Fecha de Cobro
    v.add("Fec.Vto"); // 2 Fecha Vto.
    v.add("N.Rem"); // 3 Numero de Remesa
    v.add("Fec.Rem"); // 4 Fecha de Remesa
    v.add("Importe"); // 5
    v.add("Observaciones"); // 6
    v.add("Usuario"); // 7
    jtCobr.setCabecera(v);


    jtCobr.setAnchoColumna(new int[]{150,90,90,45,90,80,100,80});
    jtCobr.setAlinearColumna(new int[]  {0, 1, 1, 2,1,2, 0, 0});

    jtCobr.setFormatoColumna(5, "----,--9.99");

//    jtCobr.setAjustarGrid(true);

    ArrayList vg = new ArrayList();
    vg.add("N.Giro"); // 0
    vg.add("Importe"); // 1
    vg.add("Fec.Vto."); // 2
    vg.add("Banco"); // 3
    vg.add("Sucur."); // 4
    vg.add("DC"); // 5
    vg.add("Cuenta"); // 6
    jtGiros.setCabecera(vg);
    jtGiros.setAnchoColumna(new int[]{43, 82, 90, 42, 42, 30, 100});
    jtGiros.setAlinearColumna(new int[]{2, 2, 1, 2, 2, 2, 2,});
    jtGiros.setFormatoColumna(1, "--,---,--9.99");
    jtGiros.setFormatoColumna(3, "9999");
    jtGiros.setFormatoColumna(4, "9999");
    jtGiros.setFormatoColumna(5, "99");
  //  jtGiros.setBounds(new Rectangle(6, 132, 592, 139));
    rec_numeE.setEnabled(false);
    rec_fecvtoE.setText("");
    ArrayList vx=new ArrayList();
    vx.add(rec_numeE);
    vx.add(rec_importE);
    vx.add(rec_fecvtoE);
    vx.add(ban_codiE);
    vx.add(rec_baoficE);
    vx.add(rec_badicoE);
    vx.add(rec_bacuenE);
    jtGiros.setCampos(vx);
    jtGiros.setEnabled(true);
    jtGiros.addGridListener(new GridAdapter()
    {
            @Override
          public void cambiaLinea(GridEvent event){
            event.setColError(checkLinGir(event.getLinea()));
          }
            @Override
          public void deleteLinea(GridEvent event)
          {
              borraLineaGir(event.getLinea());
          }
    });
    confGridCom();
}
 private void confGridCom() throws Exception
 {
     ArrayList v=new ArrayList();
     v.add("Comentario");
     jtComCa.setCabecera(v);
     jtComCa.setAnchoColumna(new int[]{80});
     jtComCa.setAlinearColumna(new int[]{0});
     jtComCa.setBounds(new Rectangle(6, 132, 592, 139));
     jtComCa.setAjustarGrid(true);
     
     ArrayList vc=new ArrayList();
     vc.add(fvc_comencE);
     jtComCa.setCampos(vc);
     ArrayList vl=new ArrayList();
     vl.add(fvc_comenpE);

     jtComPie.setCabecera(v);
     jtComPie.setCampos(vl);
     jtComPie.setAnchoColumna(new int[]{80});
     jtComPie.setAlinearColumna(new int[]{0});
     jtComPie.setAjustarGrid(true);
 }
  @Override
 public void iniciarVentana() throws Exception
 {
 
   Pcabe.setButton(KeyEvent.VK_F4, Baceptar);
   Ppie.setButton(KeyEvent.VK_F4, Baceptar);
   datCab = new actCabAlbFra(dtCon1, dtAdd,EU.em_cod,numDecPrecio);
  
   cli_codiE.setColumnaAlias("cli_codi");
   fvc_anoE.setColumnaAlias("fvc_ano");
   emp_codiE.setColumnaAlias("emp_codi");
   fvc_fecfraE.setColumnaAlias("fvc_fecfra");
   fvc_serieE.setColumnaAlias("fvc_serie");
   fvc_numeE.setColumnaAlias("fvc_nume");
   fpa_codiE.setColumnaAlias("fpa_codi");
   fvc_impcobE.setColumnaAlias("fvc_impcob");
   fvc_basimpE.setColumnaAlias("fvc_basimp");
   fvc_sumtotE.setColumnaAlias("fvc_sumtot");
   fvc_sumlinE.setColumnaAlias("fvc_sumlin");
   fvc_impivaE.setColumnaAlias("fvc_impiva");
   fvc_imprecE.setColumnaAlias("fvc_imprec");
   fvc_porreqE.setColumnaAlias("fvc_porreq");
   fvc_porivaE.setColumnaAlias("fvc_poriva");
   fvc_cobradE.setColumnaAlias("fvc_cobrad");
   fvc_traspE.setColumnaAlias("fvc_trasp");
   fvc_dtoppE.setColumnaAlias("fvc_dtopp");
   fvc_dtocomE.setColumnaAlias("fvc_dtocom");
   activarEventos();
   activar(false);
 }
 void activarEventos()
 {
   BrecCobr.addActionListener(new ActionListener()
   {
     @Override
     public void actionPerformed(ActionEvent e)
     {
        BrecalcCobros_actionPerformed();
     }
   });
   opAgrlin.addActionListener(new ActionListener()
   {
       @Override
     public void actionPerformed(ActionEvent e)
     {
       verDatos(dtCons);
     }
   });
   Brecalc.addActionListener(new ActionListener()
   {
     public void actionPerformed(ActionEvent e)
     {
       Brecalc_actionPerformed();
     }
   });

   Bprint.addActionListener(new ActionListener()
   {
     @Override
     public void actionPerformed(ActionEvent e)
     {
       String name=e.getActionCommand();
//         System.out.println("e: "+name);
       if ( name.contains(IMPGRAFICO) ||
           name.contains("Button") || name.contains(IMPPLANO) || name.contains(IMPGRAFPRE) )
       {
         Bprint_actionPerformed(name);
         return;
       }
       if (name.contains(SENDFAX))
           enviarFax();
        if (name.contains(SENDMAIL))
           enviarMail();
       else
         msgBox("Acion: "+name+" SIN implementar");
     }
   });
 }
 
 void enviarFax()
 {
    try {
         if (lifact==null)
          lifact=new lisfactu(EU,dtCon1,dtStat,dtBloq);
        lifact.setAgrupa(opAgrlin.isSelected());
        if (ifFax == null) {
            ifFax = new IFFax();
            ifFax.iniciar(this);
            ifFax.getCliField().setPeso(1);
            ifFax.setVisible(false);
            ifFax.setIconifiable(false);
            ifFax.setLocation(this.getLocation().x + 30, this.getLocation().x + 30);
            ifFax.setLisfactu(lifact);
            vl.add(ifFax, new Integer(1));
        }
        ifFax.setVisible(true);
        ifFax.setSelected(true);
        
        String numfax=cli_codiE.getLikeCliente().getString("cli_fax",true);
        ifFax.setCliCodi(cli_codiE.getText());
        ifFax.setDatosDoc("F", getSqlListFra(), true);
        ifFax.setNumFax(numfax);
    } catch (Exception k)
    {
        Error("Error al enviar el fax",k);
    }
 }
 void enviarMail()
 {
    try {
         if (! opAgrlin.isSelected())
         {
             if (mensajes.mensajePreguntar("Enviar por email factura con las lineas sin agrupar")!=mensajes.YES)
                 return;
         }
         if (lifact==null)
          lifact=new lisfactu(EU,dtCon1,dtStat,dtBloq);
        lifact.setAgrupa(opAgrlin.isSelected());
        if (ifMail == null) {
            ifMail = new IFMail();
            ifMail.iniciar(this);
            ifMail.getCliField().setPeso(1);
            ifMail.setVisible(false);
            ifMail.setIconifiable(false);
            ifMail.setLocation(this.getLocation().x + 30, this.getLocation().x + 30);
            ifMail.setLisfactu(lifact);
            vl.add(ifMail, 1);
        }
        ifMail.setVisible(true);
        ifMail.setSelected(true); 
        ifMail.setAsunto("Factura "+fvc_anoE.getValorInt()+"-"+fvc_serieE.getText()+"-"+ fvc_numeE.getValorInt()+"  de fecha: "+fvc_fecfraE.getText());
        ifMail.setText("Estimado cliente,\n\nAdjunto le enviamos la factura "+fvc_anoE.getValorInt()+"-"+
                fvc_serieE.getText()+"-"+ fvc_numeE.getValorInt()+"  de fecha: "+fvc_fecfraE.getText()+
               "\n\nAtentamente\n\n"+pdempresa.getNombreEmpresa(dtStat, emp_codiE.getValorInt()) );           
        ifMail.setCliCodi(cli_codiE.getText());

        ifMail.setDatosDoc("F", getSqlListFra(), true);     
    } catch (Exception k)
    {
        Error("Error al enviar el Correo",k);
    }
 }
 String getStrSql()
 {
   return "SELECT emp_codi,fvc_ano,fvc_serie,fvc_nume " +
       " FROM v_facvec WHERE emp_codi = " + EU.em_cod +
       " ORDER BY fvc_ano,fvc_serie desc,fvc_nume ";
 }
    @Override
 public void rgSelect() throws SQLException
 {
   super.rgSelect();
   if (!dtCons.getNOREG())
   {
     dtCons.last();
     nav.setEnabled(navegador.ULTIMO, false);
     nav.setEnabled(navegador.SIGUIENTE, false);
   }
   verDatos(dtCons);
 }
 void verDatos(DatosTabla dt)
 {
   try {
     if (dt.getNOREG())
     {
       jtGiros.setEnabled(false);
       return;
     }
     fvc_serieE.setValor(dt.getString("fvc_serie"));
     fvc_numeE.setValorDec(dt.getInt("fvc_nume"));
     fvc_anoE.setValorDec(dt.getInt("fvc_ano"));
     emp_codiE.setValorDec(dt.getInt("emp_codi"));
     jt.removeAllDatos();

     s = "select  * from v_facvec where " +
         " emp_codi = "+dt.getInt("emp_codi")+
         " and fvc_ano = "+dt.getInt("fvc_ano")+
         " and fvc_serie = '"+fvc_serieE.getValor()+"'"+
         " and fvc_nume = "+dt.getInt("fvc_nume");
     if (!dtCon1.select(s))
     {
       mensajeErr("Datos de Factura  .... NO encontrados");
       Pcabe.resetTexto();
       fvc_serieE.setValor(dt.getString("fvc_serie"));
       fvc_numeE.setValorDec(dt.getInt("fvc_nume"));
       fvc_anoE.setValorDec(dt.getInt("fvc_ano"));
       emp_codiE.setValorDec(dt.getInt("emp_codi"));
       Ppie.resetTexto();
       jtGiros.removeAllDatos();
       jtCobr.removeAllDatos();
       return;
     }
     cli_codiE.setValorInt(dtCon1.getInt("cli_codi"),dtCon1.getString("fvc_clinom",false));
//     cli_codiE.setText(dtCon1.getString("cli_codi"));

     fvc_fecfraE.setText(dtCon1.getFecha("fvc_fecfra","dd-MM-yyyy"));

     afterFocusLostCli(true);

     fvc_sumlinE.setValorDec(dtCon1.getDouble("fvc_sumlin"));
     fvc_dtoppE.setValorDec(dtCon1.getDouble("fvc_dtopp"));
     fvc_dtocomE.setValorDec(dtCon1.getDouble("fvc_dtocom"));
     fvc_cobradE.setValor(dtCon1.getString("fvc_cobrad"));
     fvc_traspE.setValor(dtCon1.getString("fvc_trasp"));
     fvc_impivaE.setValorDec(dtCon1.getDouble("fvc_impiva"));
     fvc_imprecE.setValorDec(dtCon1.getDouble("fvc_imprec"));
     fvc_porivaE.setValorDec(dtCon1.getDouble("fvc_poriva"));
     fvc_porreqE.setValorDec(dtCon1.getDouble("fvc_porreq"));
     fvc_modifE.setValor(dtCon1.getString("fvc_modif"));
     fpa_codiE.setText(dtCon1.getString("fpa_codi"));

     if (fvc_dtoppE.getValorDec()!=0)
       fvc_impdppE.setValorDec((fvc_dtoppE.getValorDec()/100)*fvc_sumlinE.getValorDec()) ;

   
     fvc_sumtotE.setValorDec(dtCon1.getDouble("fvc_sumtot"));
     fvc_impcobE.setValorDec(dtCon1.getDouble("fvc_impcob"));
     fvc_basimpE.setValorDec(dtCon1.getDouble("fvc_basimp"));
     if (dtCon1.getInt("fvc_impres")!=0)
     {
       fvc_impresE.setIcon(Iconos.getImageIcon("printer"));
       fvc_impresE.setToolTipText("Factura Impresa");
     }
     else
     {
       fvc_impresE.setIcon(null);
       fvc_impresE.setToolTipText("");
     }
     // Busco las cabeceras de albaran que tiene la factura.
     s="SELECT avc_nume,avc_ano,avc_serie,emp_codi,avc_fecalb FROM v_facvel "+
         " WHERE eje_nume = "+dt.getInt("fvc_ano")+
         " and emp_codi = "+dt.getInt("emp_codi")+
         " and fvc_nume = "+dt.getInt("fvc_nume")+
         " AND fvc_serie = '"+dt.getString("fvc_serie")+"'"+
         " group by  avc_nume,avc_ano,avc_serie,emp_codi,avc_fecalb"+
         " order by avc_fecalb,avc_ano,avc_serie,avc_nume";
     if (!dtCon1.select(s))
     {
       msgBox("No encontrados Lineas de FRA");
       return;
     }
     tiiIva=0;
     impLinDtCom=0;
     do
     {
       ArrayList v=new ArrayList();
       v.add("");
       v.add(" ALB: "+dtCon1.getString("avc_serie")+"-"+Formatear.format(dtCon1.getInt("avc_nume"),"999999")+
                    " DE FECHA: "+dtCon1.getFecha("avc_fecalb","dd-MM-yyyy"));
       v.add("");
       v.add("");
       v.add("");
       v.add("");
       jt.addLinea(v);
       s=getSqlLinFra(dtCon1);
       if (dtAux.select(s))
         verLinAlb(dtAux);
     } while (dtCon1.next());
    if (fvc_dtocomE.getValorDec()!=0)
       fvc_impdcoE.setValorDec((fvc_dtocomE.getValorDec()/100)*impLinDtCom) ;

     jtGiros.setDefaultValor(0,"0");
     jtGiros.setDefaultValor(3,cli_codiE.getLikeCliente().getString("ban_codi"));
     jtGiros.setDefaultValor(4,cli_codiE.getLikeCliente().getString("cli_baofic"));
     jtGiros.setDefaultValor(5,cli_codiE.getLikeCliente().getString("cli_badico"));
     jtGiros.setDefaultValor(6,Formatear.format(cli_codiE.getLikeCliente().getDouble("cli_bacuen"),"9999999999"));
     verDatCob();
     verDatGiros();
     verDatComen();
   }
   catch (Exception k)
   {
     Error("Error al Ver Datos de Factura", k);
   }
 }
 void verDatCob() throws SQLException, ParseException
 {
   jtCobr.removeAllDatos();
   s ="select  tc.tpc_codi,cob_anofac, fac_nume,c.cob_feccob,c.cob_impor,"+
       " c.rem_ejerc,c.rem_codi,tc.tpc_nomb,cob_fecvto,cob_obser,c.usu_nomb  " +
       " from v_cobros c,v_cobtipo tc " +
       " WHERE cob_anofac = " + fvc_anoE.getValorInt() +
       " and c.emp_codi = " + emp_codiE.getValorInt()+
       " and c.fvc_serie = '"+fvc_serieE.getValor()+"'"+
       " and c.fac_nume = " + fvc_numeE.getValorInt() +
       " and tc.tpc_codi = c.tpc_codi " +
       " order by cob_feccob desc";
   if(dtCon1.select(s))
   {
     do
     {
       ArrayList v = new ArrayList();
       v.add(dtCon1.getInt("tpc_codi")+" - "+ dtCon1.getString("tpc_nomb"));
       v.add(dtCon1.getFecha("cob_feccob", "dd-MM-yyyy"));
       v.add(dtCon1.getFecha("cob_fecvto", "dd-MM-yyyy"));
       v.add(dtCon1.getString("rem_codi"));
       if (dtCon1.getInt("rem_codi")>0)
       {
         s="SELECT * FROM remesas WHERE eje_nume= "+dtCon1.getInt("rem_ejerc")+
            " and rem_codi = "+dtCon1.getInt("rem_codi");
         if (dtStat.select(s))
           v.add(dtStat.getFecha("rem_fecha","dd-MM-yyyy"));
          else
            v.add("*ERROR*");
       }
       else
         v.add("");
       v.add(dtCon1.getString("cob_impor"));
       v.add(dtCon1.getString("cob_obser"));
       v.add(dtCon1.getString("usu_nomb"));
       jtCobr.addLinea(v);
     }  while (dtCon1.next());

       jtCobr.requestFocusInicio();
   }
}
 void verDatGiros() throws SQLException, ParseException
 {
   jtGiros.setEnabled(false);
   jtGiros.removeAllDatos();
   s="SELECT * FROM v_recibo  where eje_nume = "+fvc_anoE.getValorInt()+
       " and emp_codi = "+emp_codiE.getValorInt()+
        " and fvc_serie = '"+fvc_serieE.getValor()+"'"+
       " and fvc_nume = "+fvc_numeE.getValorInt()+
       " and rem_codi = 0 "+
       " order by rec_nume";
   if (dtCon1.select(s))
   {
     do
     {
       ArrayList v = new ArrayList();
       v.add(dtCon1.getString("rec_nume"));
       v.add(dtCon1.getString("rec_import"));
       v.add(dtCon1.getFecha("rec_fecvto", "dd-MM-yyyy"));
       v.add(dtCon1.getString("ban_codi"));
       v.add(dtCon1.getString("rec_baofic",true));
       v.add(dtCon1.getString("rec_badico",true));
       v.add(Formatear.format(dtCon1.getDouble("rec_bacuen",true),"9999999999"));
       jtGiros.addLinea(v);
     } while (dtCon1.next());
   }
   jtGiros.setEnabled(true);
   jtGiros.requestFocusInicio();
 }
 private void verLinAlb(DatosTabla dt) throws SQLException, ParseException, IllegalArgumentException
  {
    String proNomb;
    double impL;
    do
    {
      ArrayList v=new ArrayList();
      tiiIva=getTipoIva(dt.getInt("pro_codi"));
      impL=Formatear.redondea(Formatear.redondea(dt.getDouble("avl_canti",true), 2) *
                                Formatear.redondea(dt.getDouble("avl_prven",true),numDecPrecio),2);
      v.add(dt.getString("pro_Codi"));
      proNomb = dt.getString("fvl_nompro");
      v.add(proNomb);
      v.add(dt.getString("avl_canti"));
      v.add(dt.getString("avl_prven"));
      v.add(impL);
      v.add(dt.getString("avl_numlin"));
      impLinDtCom+=proIndtco?impL:0;
      jt.addLinea(v);
    } while (dt.next());
  }
 void verDatComen() throws SQLException, ParseException
 {

     String condicionWhere="and  eje_nume = "+fvc_anoE.getValorInt()+
       " and emp_codi = "+emp_codiE.getValorInt()+
        " and fvc_serie = '"+fvc_serieE.getValor()+"'"+
       " and fvc_nume = "+fvc_numeE.getValorInt()+
       " order by fco_numlin";
     verLinComen("C",condicionWhere,jtComCa);
     verLinComen("P",condicionWhere,jtComPie);
 }

 private void verLinComen(String tipo, String condicWhere,CGridEditable jt) throws SQLException
 {
     jt.removeAllDatos();
     s="select fco_coment from facvecom where fco_tipo='"+tipo+"'"+ condicWhere;
     if (! dtCon1.select(s))
         return;

     do
     {
         ArrayList v=new ArrayList();
         v.add(dtCon1.getString("fco_coment"));
         jt.addLinea(v);
     } while (dtCon1.next());
 }
 private int getTipoIva(int proCodi) throws SQLException
 {
   if (pro_codiE.getNombArt(""+proCodi)==null)
     return 0;
   proIndtco=pro_codiE.getLikeProd().getInt("pro_indtco")!=0;
   return pro_codiE.getLikeProd().getInt("pro_tipiva");   

 }
 private String getSqlLinFra(DatosTabla dt) throws SQLException
 {
   if (opAgrlin.isSelected())
     return "SELECT l.pro_codi,l.fvl_nompro,fvl_prven as avl_prven,"+
         " sum(fvl_canti) AS avl_canti, " +
         " 0 AS avl_unid, 0 AS avl_numlin" +
         "  FROM   v_facvel l where " +
         " l.avc_ano = " + dt.getInt("avc_ano") +
         " and l.emp_Codi = " + dt.getInt("emp_codi") +
         " and  l.avc_Serie = '" + dt.getString("avc_Serie") + "'" +
         " and l.avc_nume = " + dt.getInt("avc_nume") +
         " and l.fvl_canti >= 0 " +
         " group by l.pro_codi,l.fvl_nompro,fvl_prven " +
         " union all " +
         "SELECT l.pro_codi,l.fvl_nompro,fvl_prven as avl_prven,sum(fvl_canti) AS avl_canti, " +
         " 0 AS avl_unid,0 AS avl_numlin " +
         "  FROM   v_facvel l  where " +
         " l.avc_ano = " + dt.getInt("avc_ano") +
         " and l.emp_Codi = " + dt.getInt("emp_codi") +
         " and  l.avc_Serie = '" + dt.getString("avc_serie") + "'" +
         " and l.avc_nume = " + dt.getInt("avc_nume") +
         " and l.fvl_canti < 0 " +
         " group by l.pro_codi,l.fvl_nompro,fvl_prven " +
         " order by 1 ";
   else
     return
         "select  l.pro_codi,l.fvl_nompro,fvl_prven as avl_prven,fvl_canti AS avl_canti, " +
         " 0 AS avl_unid, fvl_numlin as avl_numlin from v_facvel as l  where " +
         " l.avc_ano = " + dt.getInt("avc_ano") +
         " and l.emp_Codi = " + dt.getInt("emp_codi") +
         " and  l.avc_Serie = '" + dt.getString("avc_Serie") + "'" +
         " and l.avc_nume = " + dt.getInt("avc_nume") +
         " order by l.fvl_numlin ";
 }

 public void PADPrimero(){verDatos(dtCons);}

  public void PADAnterior(){verDatos(dtCons);}

  public void PADSiguiente(){verDatos(dtCons);}

  public void PADUltimo(){verDatos(dtCons);}

  @Override
  public void PADQuery(){
    Pcabe.setQuery(true);
    Ppie.setQuery(true);
    activar(true);
    jt.setEnabled(false);
    Pcabe.resetTexto();
    boolean agrLin=opAgrlin.isSelected();
    Ppie.resetTexto();
    opAgrlin.setSelected(agrLin);
    fvc_anoE.setValorDec(EU.ejercicio);
    emp_codiE.setValorDec(EU.em_cod);
    fvc_numeE.requestFocus();
    mensaje("Introduzca los filtros de busqueda ...");
  }

  public void ej_query1(){
    Component c;
    if ((c=Pcabe.getErrorConf())!=null)
    {
      mensajeErr("Error en CAMPO");
      c.requestFocus();
      return;
    }
    if ((c=Ppie.getErrorConf())!=null)
    {
      mensajeErr("Error en CAMPO");
      c.requestFocus();
      return;
    }
    this.setEnabled(false);
    mensaje("Estableciendo filtro ....");
    ArrayList v=new ArrayList();
    v.add(cli_codiE.getStrQuery());
    v.add(fvc_anoE.getStrQuery());
    v.add(emp_codiE.getStrQuery());
    v.add(fvc_fecfraE.getStrQuery());
    v.add(fvc_serieE.getStrQuery());
    v.add(fvc_numeE.getStrQuery());

    v.add(fvc_impcobE.getStrQuery());
    v.add(fvc_basimpE.getStrQuery());
    v.add(fvc_sumtotE.getStrQuery());
    v.add(fvc_sumlinE.getStrQuery());
    v.add(fvc_impivaE.getStrQuery());
    v.add(fvc_imprecE.getStrQuery());
    v.add(fvc_cobradE.getStrQuery());
    v.add(fvc_traspE.getStrQuery());
    v.add(fvc_dtoppE.getStrQuery());
    v.add(fvc_dtocomE.getStrQuery());
    v.add(fvc_porreqE.getStrQuery());
    v.add(fvc_porivaE.getStrQuery());
    v.add(fpa_codiE.getStrQuery());

    Pcabe.setQuery(false);
    Ppie.setQuery(false);
    try  {
      s = "SELECT emp_codi,fvc_ano,fvc_serie,fvc_nume " +
          " FROM v_facvec ";
      s = creaWhere(s, v, true);
      s += " ORDER BY emp_codi,fvc_ano,fvc_serie desc,fvc_nume ";
      if (!dtCon1.select(s))
      {
        msgBox("No encontradas Facturas con estos criterios");
        mensaje("");
        verDatos(dtCons);
        activaTodo();
        this.setEnabled(true);
        return;
      }
      nav.pulsado = navegador.NINGUNO;
      activaTodo();

      strSql = s;
      rgSelect();
      this.setEnabled(true);

      mensaje("");
      mensajeErr("Nuevas FILTRO ... Establecido");
    }
    catch (Exception k)
    {
      Error("Error al buscar datos", k);
    }

  }

  public void canc_query(){
    nav.pulsado=navegador.NINGUNO;
    mensaje("");
    mensajeErr("Introducion Filtro de Busqueda ... CANCELADO");
    Pcabe.setQuery(false);
    Ppie.setQuery(false);
    activaTodo();
    verDatos(dtCons);
  }

    @Override
  public void PADEdit()
  {
    try
    {
      String msgErr;
      if ((msgErr=checkModif())!=null)
      {
        nav.pulsado = navegador.NINGUNO;
        activaTodo();
        msgBox(msgErr);
        return;
      }
      activar(true, navegador.EDIT);
      opAgrlin.setEnabled(false);
      Brecalc.setEnabled(false);
      BrecCobr.setEnabled(false);
      Bprint.setEnabled(false);

      mensaje("Modificando la Factura ....");
      fvc_sumtotE.resetCambio();
      fvc_sumtotE.setEnabled(true);
      fvc_modifE.setValor("M");
      fvc_fecfraE.requestFocus();
      cli_codiE.getCliNomb().setEnabled(cli_codiE.getLikeCliente().getInt("cli_gener") != 0);
    }
    catch (Exception k)
    {
      Error("Error al Modificar Factura", k);
    }

  }

  @Override
  public boolean checkEdit()
  {
    try {
      if (fvc_fecfraE.isNull() || fvc_fecfraE.getError())
      {
        mensajeErr("Fecha de Factura NO es valida");
        fvc_fecfraE.requestFocus();
        return false;
      }
       if (fpa_codiE.getValorInt()==0)
       {
         mensajeErr("Forma de PAGO No valida");
         fpa_codiE.requestFocus();
         return false;
       }

      if (!fpa_codiE.controla(true))
      {
        mensajeErr(fpa_codiE.getMsgError());
        return false;
      }
    } catch (SQLException | ParseException k)
    {
      Error("Error al comprobar valores de factura",k);
      return false;
    }
    return true;
  }

  @Override
  public void ej_edit1()
  {
    this.setEnabled(false);
    try
    {
      // Actualizo la cabecera de la factura
      dtAdd.edit();
      if (cli_codiE.getLikeCliente().getInt("cli_gener") == 0)
        dtAdd.setDato("fvc_clinom", (String) null);
      else
        dtAdd.setDato("fvc_clinom", cli_codiE.getTextNomb());

      dtAdd.setDato("fvc_fecfra", fvc_fecfraE.getText(), "dd-MM-yyyy");
      dtAdd.setDato("fvc_dtopp", fvc_dtoppE.getValorDec());
      dtAdd.setDato("fvc_dtocom", fvc_dtocomE.getValorDec());
      dtAdd.setDato("fpa_codi", fpa_codiE.getValorInt());
      dtAdd.setDato("fvc_modif", fvc_modifE.getValor());
      double impDtoPP = 0;
      double dtos = fvc_dtoppE.getValorDec() + fvc_dtocomE.getValorDec();
      
      if (dtos != 0)
        impDtoPP = Formatear.redondea(impLinDtCom * dtos / 100, numDec);
      
      double impBim = Formatear.redondea(fvc_sumlinE.getValorDec() - impDtoPP, numDec);
      double impIva;
      double impReq=0;
      DatosIVA tipIva= MantTipoIVA.getDatosIva(dtStat, tiiIva);
     
      if (tipIva==null)
        throw new SQLException("Tipo IVA: "+tiiIva+" NO ENCONTRADO");
      dtAdd.setDato("fvc_poriva", tipIva.getPorcIVA() );
      impIva = Formatear.redondea(impBim * tipIva.getPorcIVA()/ 100, numDec);
      if (cli_codiE.getLikeCliente().getInt("cli_recequ") != 0)
      {
        dtAdd.setDato("fvc_porreq", tipIva.getPorcREQ());
        impReq = Formatear.redondea(impBim * tipIva.getPorcREQ() / 100, numDec);
      }
      else
        dtAdd.setDato("fvc_porreq", 0);

      dtAdd.setDato("fvc_basimp", impBim);
      dtAdd.setDato("fvc_imprec", impReq);
      dtAdd.setDato("fvc_impiva", impIva);
      if (fvc_sumtotE.hasCambio())
      {
          if (Math.abs(fvc_sumtotE.getValorDec() - (impBim + impReq + impIva)) > 1) 
          {
              msgBox("Importe de la factura NO debe diferir en mas de uno. "
                  + "Valor calculado: BI: "+impBim+ " IVA: "+  impIva+ "RE: "+impReq+" Total: "+
                      (impBim + impReq + impIva));
              this.setEnabled(true);
              return;
          }
      }       
      dtAdd.setDato("fvc_sumtot", fvc_sumtotE.hasCambio()?fvc_sumtotE.getValorDec(): impBim + impReq + impIva);
      dtAdd.update(stUp);
// Actualizar Lineas de Comentarios
       // jtComCa.salirFoco();
       jtComCa.salirGrid();
     jtComPie.salirGrid();
      String condicionWhere=" emp_codi = " + emp_codiE.getValorInt() +
          " and fvc_serie = '"+fvc_serieE.getValor()+"'"+
          " AND eje_nume = " + fvc_anoE.getValorInt() +
          " AND fvc_nume = " + fvc_numeE.getValorInt();
      s="DELETE FROM facvecom WHERE "+condicionWhere;
      dtAdd.executeUpdate(s,stUp);
      int nLin=jtComCa.getRowCount();
      for (int n=0;n<nLin;n++)
      {
          if (jtComCa.getValString(n,0).trim().equals(""))
              continue;
          actLinComen(n,"C",jtComCa.getValString(n,0));
      }
      nLin=jtComPie.getRowCount();
      for (int n=0;n<nLin;n++)
      {
          if (jtComPie.getValString(n,0).trim().equals(""))
              continue;
          actLinComen(n,"P",jtComPie.getValString(n,0));
      }
     resetBloqueo(dtAdd, "v_facvec",
                     fvc_anoE.getValorInt() + "|" + emp_codiE.getValorInt() +
                     "|" + fvc_serieE.getValor()+ fvc_numeE.getValorInt(), false);
   
      // Actualizo el importe de los recibos que pudiera tener.
      s = "DELETE FROM v_recibo WHERE "+condicionWhere;
      dtAdd.executeUpdate(s, stUp);

      generaGiros(fpa_codiE.getValorInt());
      if (jf != null)
      {
        jf.ht.clear();
        jf.ht.put("%f",
                  emp_codiE.getValorInt() + "-" + fvc_anoE.getValorInt() + "-" +
                  fvc_serieE.getValor()+fvc_numeE.getText());
        jf.guardaMens("F2", jf.ht);
      }

      ctUp.commit();
      mensajeErr("Factura ... MODIFICADA");
      mensaje("");
      activaTodo();
      verDatos(dtCons);
      this.setEnabled(true);
    }
    catch (Exception k)
    {
      Error("Error al Modificar FACTURA", k);
    }
    nav.pulsado = navegador.NINGUNO;

  }
  private void actLinComen(int nLin, String tipo, String comentario) throws SQLException
  {
        dtAdd.addNew("facvecom");
        dtAdd.setDato("emp_codi", emp_codiE.getValorInt());
        dtAdd.setDato("eje_nume", fvc_anoE.getValorInt());
        dtAdd.setDato("fvc_serie", fvc_serieE.getValor());
        dtAdd.setDato("fvc_nume", fvc_numeE.getValorInt());
        dtAdd.setDato("fco_numlin", nLin);
        dtAdd.setDato("fco_tipo", tipo);
        dtAdd.setDato("fco_coment", comentario);
        dtAdd.update(stUp);
  }
  public void canc_edit()
  {
    activaTodo();
    verDatos(dtCons);
    try
    {
      resetBloqueo(dtAdd, "v_facvec",
                   fvc_anoE.getValorInt() + "|" + emp_codiE.getValorInt() +
                   "|" + fvc_serieE.getValor()+fvc_numeE.getValorInt(), true);
    }
    catch (Exception k)
    {
      mensajes.mensajeAviso("Error al Quitar el bloqueo\n" + k.getMessage());
    }

    mensajeErr("Modificacion Factura ... Cancelado");
    mensaje("");
    nav.pulsado = navegador.NINGUNO;

  }

  public void PADAddNew(){
      nav.pulsado = navegador.NINGUNO;
      msgBox("Funcion NO implementada");
      activaTodo();
  }
  public void PADAddNew1(){}

  public void ej_addnew(){}

  public void ej_addnew1(){}

  public void canc_addnew(){}

  private String checkModif() throws Exception
  {
    String resCheck = checkModif(fvc_traspE.getValor().equals("-1"),
                                 emp_codiE.getValorInt(), fvc_anoE.getValorInt(),
                                 fvc_serieE.getValor(),fvc_numeE.getValorInt(), dtStat,null);
    if (resCheck != null)
      return resCheck;
    if (!setBloqueo(dtBloq, "v_facvec",
                    fvc_anoE.getValorInt() + "|" + emp_codiE.getValorInt() +
                    "|" + fvc_serieE.getValor()+fvc_numeE.getValorInt()))
      return msgBloqueo;
    String msgErr= selectFraUpdate( fvc_anoE.getValorInt(),emp_codiE.getValorInt(),
                                 fvc_serieE.getValor(),fvc_numeE.getValorInt(),dtAdd);
    if (msgErr!=null)
      resetBloqueo(dtBloq, "v_facvec",
                    fvc_anoE.getValorInt() + "|" + emp_codiE.getValorInt() +
                    "|" + fvc_serieE.getValor()+fvc_numeE.getValorInt());
    return msgErr;
  }
  public static String checkModif(boolean traspCont,int empCodi,int fvcAno,String fvcSerie,int fvcNume,
                                  DatosTabla dt,DatosTabla dt1) throws SQLException
  {
    if (traspCont)
      return "Factura ya esta traspasada a contabilidad .. IMPOSIBLE Modificar o Borrar";

    String s = "SELECT * FROM v_recibo WHERE emp_codi = " + empCodi +
        " AND eje_nume = " + fvcAno +
        " and fvc_serie ='"+fvcSerie+"'"+
        " AND fvc_nume = " +fvcNume+
        " AND rem_codi != 0";
    if (dt.select(s))
      return "FACTURA TIENE REMESAS YA ASIGNADAS (Remesa: "+dt.getInt(103)+", Fec.Vto: "+
              dt.getFecha("rec_fecvto","dd-MM-yyyy")+
              ") ... IMPOSIBLE Modificar o Borrar";

    if (pdejerci.isCerrado(dt, fvcAno, empCodi))
      return "Factura  es de un ejercicio YA cerrado ... IMPOSIBLE MODIFICAR";
    return selectFraUpdate(fvcAno,empCodi,fvcSerie,fvcNume,dt1);
  }

  public static String selectFraUpdate(int fvcAno, int empCodi,String fvcSerie, int fvcNume, DatosTabla dt) throws
      SQLException
  {
    if (dt==null)
     return null;


    String s = "SELECT * FROM v_facvec WHERE fvc_ano =" + fvcAno +
        " and emp_codi = " + empCodi +
        " and fvc_serie ='"+fvcSerie+"'"+
        " and fvc_nume = " + fvcNume;
    if (!dt.select(s, true))
      return "FACTURA NO encontrada .. PROBABLEMENTE se ha borrado";
    return null;
  }
  public void PADDelete()
  {
    try
    {
      String msgErr;
      if ( (msgErr = checkModif()) != null)
      {
        nav.pulsado = navegador.NINGUNO;
        activaTodo();
        msgBox(msgErr);
        return;
      }
    }
    catch (Exception k)
    {
      Error("Error al Borrar Factura", k);
    }

    Baceptar.setEnabled(true);
    Bcancelar.setEnabled(true);
    jt.setEnabled(false);
    opAgrlin.setEnabled(false);
    Brecalc.setEnabled(false);
    BrecCobr.setEnabled(false);
    Bprint.setEnabled(false);
    mensaje("Borrando la Factura ....");
    Bcancelar.requestFocus();
  }

  public void ej_delete1()
  {
    this.setEnabled(false);
    try
    {
      deleteFra(emp_codiE.getValorInt(),
                fvc_anoE.getValorInt(),
                fvc_serieE.getValor(),
                fvc_numeE.getValorInt(),dtAdd);
      resetBloqueo(dtAdd, "v_facvec",
                      fvc_anoE.getValorInt() + "|" + emp_codiE.getValorInt() +
                      "|" +   fvc_serieE.getValor()+fvc_numeE.getValorInt(),false);
      if (jf != null)
      {
        jf.ht.clear();
        jf.ht.put("%f",
                  emp_codiE.getValorInt() + "-" + fvc_anoE.getValorInt() + "-" +
                   fvc_serieE.getValor()+fvc_numeE.getText());
        jf.guardaMens("F1", jf.ht);
      }

      ctUp.commit();
      mensajeErr("Factura ... BORRADA");
      mensaje("");
      activaTodo();
      if (dtCons.next())
        verDatos(dtCons);
      else
      {
        if (dtCons.previous())
          verDatos(dtCons);
        else
        {
          Pcabe.resetTexto();
          jt.removeAllDatos();
        }
      }
      this.setEnabled(true);
    }
    catch (Exception k)
    {
      Error("Error al Borrar FACTURA",k);
    }
    nav.pulsado = navegador.NINGUNO;
  }
  public static void deleteFra(int empCodi,int fvcAno, String fvcSerie,int fvcNume,DatosTabla dt) throws SQLException
{      // Borro la cabecera de la factura
       dt.delete();
      // Quito la referencia en la cabecera de Albaran
      String albSerie="";
      String s;
      int  albNume=0;
      s = "SELECT * FROM v_albavec "+
          " WHERE emp_codi = " + empCodi +
          " AND fvc_ano = " + fvcAno +
          " and fvc_serie ='"+fvcSerie+"'"+
          " AND fvc_nume = " + fvcNume;
      if (dt.select(s))
      {
        albSerie=dt.getString("avc_serie");
        albNume=dt.getInt("avc_nume");
      }
      s = "UPDATE v_albavec SET fvc_ano = 0, fvc_nume = 0 "+
          " WHERE emp_codi = " + empCodi +
          " and fvc_serie ='"+fvcSerie+"'"+
          " AND fvc_ano = " + fvcAno +
          " AND fvc_nume = " + fvcNume;
      dt.executeUpdate(s);
      // Borro las lineas de La factura
      s = "DELETE FROM v_facvel "+
          " WHERE emp_codi = " + empCodi +
          " and fvc_serie ='"+fvcSerie+"'"+
          " AND eje_nume = " + fvcAno +
          "  AND fvc_nume = " + fvcNume;
      dt.executeUpdate(s);

      // Anulo los cobros que haya sobre esta factura y se los pongo al primer albaran
      //  que tenga.
      s="UPDATE v_cobros set cob_serie = '"+albSerie+"', fac_nume = 0 "+
            ", alb_nume = "+albNume+
            ", cob_albar = null "+
            " WHERE emp_codi = "+empCodi+
            " AND cob_anofac = "+fvcAno+
            " and fvc_serie ='"+fvcSerie+"'"+
            " AND fac_nume = "+ fvcNume;
      dt.executeUpdate(s);
      // Borro Los recibos que pudiera tener.
      s="DELETE FROM v_recibo WHERE emp_codi = "+empCodi+
          " AND eje_nume = "+fvcAno+
          " and fvc_serie ='"+fvcSerie+"'"+
          " AND fvc_nume = "+fvcNume;
      dt.executeUpdate(s);
  }
  public void canc_delete(){
    activaTodo();
    verDatos(dtCons);
    try
    {
      resetBloqueo(dtAdd, "v_facvec",
                   fvc_anoE.getValorInt() + "|" + emp_codiE.getValorInt() +
                   "|" +  fvc_serieE.getValor()+ fvc_numeE.getValorInt(),true);
    }
    catch (Exception k)
    {
      mensajes.mensajeAviso("Error al Quitar el bloqueo\n" + k.getMessage());
    }

    mensajeErr("Borrado Factura ... Cancelado");
    mensaje("");
    nav.pulsado = navegador.NINGUNO;
  }

  @Override
  public void activar(boolean b)
  {
    activar(b, navegador.TODOS);
  }
  public void activar(boolean b,int modo)
  {
    fvc_fecfraE.setEnabled(b);
    fvc_dtoppE.setEnabled(b);
    fvc_dtocomE.setEnabled(b);
    fvc_sumtotE.setEnabled(b);
    fvc_modifE.setEnabled(b);
    fpa_codiE.setEnabled(b);
    Baceptar.setEnabled(b);
    Bcancelar.setEnabled(b);
    jtGiros.setEnabled(!b);
    jtComCa.setEnabled(b);
    jtComPie.setEnabled(b);
    if (modo==navegador.EDIT)
      return;
    fvc_porreqE.setEnabled(b);
    fvc_porivaE.setEnabled(b);
    cli_codiE.setEnabled(b);
    fvc_anoE.setEnabled(b);
    emp_codiE.setEnabled(b);
    fvc_serieE.setEnabled(b);
    fvc_numeE.setEnabled(b);
    fvc_impcobE.setEnabled(b);
    fvc_basimpE.setEnabled(b);
//    fvc_sumtotE.setEnabled(b);
    fvc_sumlinE.setEnabled(b);
    fvc_impivaE.setEnabled(b);
    fvc_imprecE.setEnabled(b);
    fvc_cobradE.setEnabled(b);
    fvc_traspE.setEnabled(b);
    jt.setEnabled(b);
    opAgrlin.setEnabled(!b);
    Brecalc.setEnabled(!b);
    BrecCobr.setEnabled(!b);
    Bprint.setEnabled(!b);

    cli_codiE.getCliNomb().setEnabled(false);
  }
  void afterFocusLostCli(boolean ok)
  {
    if (!ok)
      return;
    try
    {
      cli_pobleE.setText(cli_codiE.getLikeCliente().getString("cli_poble"));
    }
    catch (Exception k)
    {
      Error("Error al Buscar datos de Cliente", k);
    }
  }
  @Override
  public void afterConecta() throws SQLException, ParseException
  {
    pdnumeracion.llenaSeriesFraVen(fvc_serieE);
    fvc_traspE.addItem("Si", "-1");
    fvc_traspE.addItem("No", "0");
    dtAux= new DatosTabla(dtCon1.getConexion());
   
    pro_codiE.iniciar(dtStat,this,vl,EU);
    fvc_cobradE.addItem("Si","-1");
    fvc_cobradE.addItem("No","0");
    fpa_codiE.iniciar(dtStat,this,vl,EU);
    cli_codiE.iniciar(dtStat,this,vl,EU);
    fvc_modifE.addItem("Modificado","M");
    fvc_modifE.addItem("Automatico","A");
    numDecPrecio=pdconfig.getNumDecimales(EU.em_cod, dtStat);
    FORMDECPRECIO=".";
    for (int n=0;n<numDecPrecio;n++)
       FORMDECPRECIO+="9";
  }
  /**
   * Recalcular Importe Cobrado.
   */
  void BrecalcCobros_actionPerformed()
  {

    if (fvc_traspE.getValor().equals("-1"))
    {
      mensajeErr("Factura ya esta traspasada a contabilidad .. IMPOSIBLE RECALCULAR");
      activaTodo();
      return;
    }
    try {
      s = "select sum(cob_impor) as cob_impor from  v_cobros where " +
          " alb_nume = 0 " +
          " and emp_codi = " + emp_codiE.getValorInt() +
          " and cob_serie = 'A'" +
          " and fvc_serie = '"+fvc_serieE.getValor()+"'"+
          " AND cob_anofac = " + fvc_anoE.getValorInt() +
          " AND fac_nume = " + fvc_numeE.getValorInt();
      dtStat.select(s);
      s = "SELECT * FROM v_facvec WHERE fvc_ano = " + fvc_anoE.getValorInt() +
         " and emp_codi = " + emp_codiE.getValorInt() +
         " and fvc_serie = '"+fvc_serieE.getValor()+"'"+
         " and fvc_nume = " + fvc_numeE.getValorInt();
     if (!dtAdd.select(s, true))
       return;
     dtAdd.edit(dtAdd.getCondWhere());
     dtAdd.setDato("fvc_impcob",dtStat.getDouble("cob_impor",true));
     dtAdd.update(stUp);
     dtAdd.commit();
     verDatos(dtCons);
     mensajeErr("Importes de Cobros realizados ... ACTUALIZADOS");
   }
   catch (Exception k)
   {
     Error("Error al Actualizar Importe cobrado", k);
   }


  }
  void Brecalc_actionPerformed()
  {
    if (fvc_traspE.getValor().equals("-1"))
    {
      mensajeErr("Factura ya esta traspasada a contabilidad .. IMPOSIBLE RECALCULAR");
      activaTodo();
      return;
    }

    try {
    if (! datCab.actDatosFra(fvc_anoE.getValorInt() ,emp_codiE.getValorInt() ,
         fvc_serieE.getValor(), fvc_numeE.getValorInt()))
      return;
    if ( datCab.getCambioIva())
        throw new Exception("FACTURA ERRONEA ... TIENE TIPOS DE IVA DIFERENTES");

    s = "SELECT * FROM v_facvec WHERE fvc_ano = " + fvc_anoE.getValorInt() +
        " and emp_codi = " + emp_codiE.getValorInt() +
        " and fvc_serie ='"+fvc_serieE.getValor()+"'"+
        " and fvc_nume = " + fvc_numeE.getValorInt();
    if (!dtAdd.select(s, true))
      return;
    dtAdd.edit(dtAdd.getCondWhere());
    dtAdd.setDato("fvc_trasp",0); // Se marca COMO No traspasado
    dtAdd.setDato("fvc_basimp",datCab.getValDouble("fvc_basimp"));
    dtAdd.setDato("fvc_imprec", datCab.getValDouble("fvc_impree"));
    dtAdd.setDato("fvc_impiva", datCab.getValDouble("fvc_impiva"));
    dtAdd.setDato("fvc_sumlin", datCab.getValDouble("fvc_impbru"));
    dtAdd.setDato("fvc_sumtot", datCab.getValDouble("fvc_sumtot"));
    dtAdd.setDato("fvc_poriva", datCab.getValDouble("fvc_tipiva"));
    dtAdd.setDato("fvc_porreq", datCab.getValDouble("fvc_tipree"));

    dtAdd.update(stUp);
    verDatos(dtCons);
    dtAdd.commit();
    mensajeErr("Importes de Factura ... ACTUALIZADOS");
    } catch (Exception k)
    {
      Error("Error al Actualizar Cabecera",k);
    }
  }
  String getSqlListFra()
  {
       return "SELECT f.emp_codi as fvc_empcod,f.*,c.*,fp.fpa_nomb,fp.fpa_dia1,fp.fpa_dia2,fp.fpa_dia3 "+
          " FROM v_facvec AS f,clientes as c,v_forpago as fp WHERE "+
          "  f.fvc_ano = " + fvc_anoE.getValorInt() +
          " and f.emp_codi = " + emp_codiE.getValorInt() +
          " and f.fvc_nume = " + fvc_numeE.getValorInt() +
          " and f.fvc_serie ='"+fvc_serieE.getValor()+"'"+
          " and f.cli_codi = c.cli_codi "+
          " and fp.fpa_codi = c.fpa_codi ";
  }
  void Bprint_actionPerformed(String accion)
  {
    if (dtCons.getNOREG())
      return;
    try  {
      if (lifact==null)
        lifact=new lisfactu(EU,dtCon1,dtStat,dtBloq);
      s=getSqlListFra();
     
      lifact.setAgrupa(opAgrlin.isSelected());
      String cfgLifrgr=null;
      if (accion.contains(IMPPLANO))
          cfgLifrgr="N";
       if (accion.contains(IMPGRAFICO))
       {
           lifact.setFraPreImpr(false);
          cfgLifrgr="S";
       }
       if (accion.contains(IMPGRAFPRE))
       {
          lifact.setFraPreImpr(true);
          cfgLifrgr="S";
       }
//      boolean res=lifact.listarFactText(s);
      int res=lifact.impFactura(s,emp_codiE.getValorInt(),cfgLifrgr);
      if (res>0)
        mensajeErr("FACTURA ... Listada");

    } catch (Throwable k)
    {
      Error("Error al Listar Factura",k);
    }
  }
  @Override
  public void matar(boolean cerrarConexion)
 {
   if (muerto)
     return;
   if (lifact != null)
   {
     if (! lifact.muerto)
       lifact.matar(false);
   }
    if (ifFax!=null)
    {
      ifFax.setVisible(false);
      ifFax.dispose();
    }
   if (ifMail!=null)
   {
      ifMail.setVisible(false);
      ifMail.dispose();
   }
   super.matar(cerrarConexion);
 }
 int checkLinGir(int row)
 {
   int recNume;
   try {
     if (rec_importE.getValorDec() == 0)
     {
       borraLineaGir(row);
       return -1;
     }

     if (rec_fecvtoE.isNull() || rec_fecvtoE.getError())
     {
       mensajeErr("Fecha de VTO. no es valida");
       return 2;
     }
     if (! checkCuenta())
      return 3; // � Deja pasar aunque el digito de control este mal?.
     if (jtGiros.getValInt(row,0) == 0)
     {
       s="SELECT MAX(REC_NUME) AS rec_nume FROM v_recibo  where eje_nume = "+fvc_anoE.getValorInt()+
           " and emp_codi = "+emp_codiE.getValorInt()+
           " and fvc_serie ='"+fvc_serieE.getValor()+"'"+
           " and fvc_nume = "+fvc_numeE.getValorInt();
       dtStat.select(s);
       recNume=dtStat.getInt("rec_nume",true)+1;
       dtAdd.addNew("v_recibo");
       dtAdd.setDato("eje_nume", fvc_anoE.getValorInt());
       dtAdd.setDato("emp_codi", emp_codiE.getValorInt());
        dtAdd.setDato("fvc_serie",fvc_serieE.getValor());
       dtAdd.setDato("fvc_nume", fvc_numeE.getValorInt());
       dtAdd.setDato("rec_nume", recNume);
       dtAdd.setDato("rem_ejerc", 0); // Ejercicio de Remesa
       dtAdd.setDato("rem_codi", 0); // Codigo de Remesa
       dtAdd.setDato("bat_codi", 0); // Banco de Remesa
       dtAdd.setDato("rec_emitid", 0); // Emitido (0=NO, -1 SI)
       dtAdd.setDato("rec_remant", 0);
       dtAdd.setDato("rec_recagr", 0);
       dtAdd.setDato("rec_impor2", 0);
       actRecibo();
       jtGiros.setValor(""+recNume,row,0);
     }
     else
     {
       s = "SELECT * FROM v_recibo  where eje_nume = " + fvc_anoE.getValorInt() +
           " and emp_codi = " + emp_codiE.getValorInt() +
           " and fvc_serie ='"+fvc_serieE.getValor()+"'"+
           " and fvc_nume = " + fvc_numeE.getValorInt() +
           " and rec_nume = " + jtGiros.getValorInt(row, 0);
      if (!dtAdd.select(s,true))
      {
//        debug("RECIBO NO ENCONTRADO: "+s);
        mensajeErr("Recibo no encontrado");
        return 1;
      }
      dtAdd.edit();
      actRecibo();
     }
     ctUp.commit();
     mensajeErr("Linea .. Actualizada");
   } catch (Exception k)
   {
     Error("Error al Modificar Linea",k);
   }
   return -1;
 }

 void actRecibo() throws SQLException,ParseException
 {
   dtAdd.setDato("rec_fecvto", rec_fecvtoE.getText(), "dd-MM-yyyy");
   dtAdd.setDato("rec_import", rec_importE.getValorDec());
   dtAdd.setDato("ban_codi", ban_codiE.getValorInt());
   dtAdd.setDato("rec_baofic", rec_baoficE.getValorInt());
   dtAdd.setDato("rec_badico", rec_badicoE.getValorInt());
   dtAdd.setDato("rec_bacuen", rec_bacuenE.getText());
   dtAdd.update(stUp);
 }
 void borraLineaGir(int row)
 {
    if (jtGiros.getValorInt(row,0)==0)
    {
//      debug("Recibo : "+jtGiros.getValInt(row,0)+" NO borrado");
      return;
    }
//   debug("Recibo: "+jtGiros.getValInt(row,0)+" borrado");
   try {
     s = "DELETE FROM v_recibo  where eje_nume = " + fvc_anoE.getValorInt() +
         " and emp_codi = " + emp_codiE.getValorInt() +
         " and fvc_serie ='"+fvc_serieE.getValor()+"'"+
         " and fvc_nume = " + fvc_numeE.getValorInt() +
         " and rec_nume = " + jtGiros.getValorInt(row, 0);
     stUp.executeUpdate(s);
     ctUp.commit();
     mensajeErr("RECIBO .. BORRADO");
   } catch (Exception k)
   {
     Error("Recibo .. BORRADO",k);
   }
 }
 boolean checkCuenta()
 {
   String cuenta=Formatear.format(ban_codiE.getValorInt(),"9999")+
       Formatear.format(rec_baoficE.getValorInt(),"9999");
   if (Formatear.getNumControl(Integer.parseInt(cuenta)) !=
       Integer.parseInt(Formatear.format(rec_badicoE.getValorInt(), "99").
                        substring(0, 1)))
   {
     mensajeErr("Digito control NO VALIDO para banco y sucursal");
     return false;
   }
   if (Formatear.getNumControl(rec_bacuenE.getValorDec()) !=
       Integer.parseInt(Formatear.format(rec_badicoE.getValorInt(), "99").
                        substring(1, 2)))
   {
     mensajeErr("Digito control NO VALIDO para Num. Cuenta");
     return false;
   }

   return true;
 }

 void generaGiros(int fpaCodi) throws Exception
 {
   double impFra=fvc_sumtotE.getValorDec();
   s="SELECT * from v_forpago WHERE fpa_codi = "+fpaCodi;
   if (! dtStat.select(s))
     throw new Exception("Forma de PAGO: " + fpaCodi + " NO ENCONTRADA");
   if (dtStat.getInt("fpa_esgir")==0)
     return; // Forma de PAGO no es giro
   int nVtos = clFactCob.calDiasVto(dtStat,  cli_codiE.getLikeCliente().getDatoInt("cli_dipa1"),
                                     cli_codiE.getLikeCliente().getDatoInt("cli_dipa2"), 0, fvc_fecfraE.getText());
   double impGiros=Formatear.redondea(fvc_sumtotE.getValorDec() /nVtos,numDec);
   for (int n = 0; n < nVtos; n++)
   {
     dtAdd.addNew("v_recibo");
     dtAdd.setDato("eje_nume",  fvc_anoE.getValorInt());
     dtAdd.setDato("emp_codi",  emp_codiE.getValorInt());
     dtAdd.setDato("fvc_serie", fvc_serieE.getValor());
     dtAdd.setDato("fvc_nume", fvc_numeE.getValorInt());
     dtAdd.setDato("rec_nume", n + 1);
     dtAdd.setDato("rec_fecvto", clFactCob.diasVto[n], "dd-MM-yyyy");
     dtAdd.setDato("rem_ejerc", 0);  // Ejercicio de Remesa
     dtAdd.setDato("rem_codi", 0);  // Codigo de Remesa
     dtAdd.setDato("bat_codi", 0);  // Banco de Remesa
     dtAdd.setDato("rec_emitid", 0); // Emitido (0=NO, -1 SI)
     dtAdd.setDato("rec_remant", 0);
     dtAdd.setDato("rec_recagr", 0);
     dtAdd.setDato("rec_impor2", 0);
     dtAdd.setDato("rec_import", (n+1==nVtos)?fvc_sumtotE.getValorDec():impGiros);
     dtAdd.setDato("ban_codi", cli_codiE.getLikeCliente().getDatoInt("ban_codi"));
     dtAdd.setDato("rec_baofic",cli_codiE.getLikeCliente().getDatoInt("cli_baofic"));
     dtAdd.setDato("rec_badico",cli_codiE.getLikeCliente().getDatoInt("cli_badico"));
     dtAdd.setDato("rec_bacuen", cli_codiE.getLikeCliente().getDouble("cli_bacuen") );
     dtAdd.update(stUp);
     impFra-=impGiros;
   }
 }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CButton Bcancelar;
    private gnu.chu.controles.CButton BrecCobr;
    private gnu.chu.controles.CButton Brecalc;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Pcobros;
    private gnu.chu.controles.CPanel Pcomen;
    private gnu.chu.controles.CPanel Ppie;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CTabbedPane Ptab;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel10;
    private gnu.chu.controles.CLabel cLabel11;
    private gnu.chu.controles.CLabel cLabel12;
    private gnu.chu.controles.CLabel cLabel13;
    private gnu.chu.controles.CLabel cLabel14;
    private gnu.chu.controles.CLabel cLabel15;
    private gnu.chu.controles.CLabel cLabel16;
    private gnu.chu.controles.CLabel cLabel17;
    private gnu.chu.controles.CLabel cLabel18;
    private gnu.chu.controles.CLabel cLabel19;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel20;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.controles.CLabel cLabel9;
    private gnu.chu.controles.CTextField cTextField1;
    private gnu.chu.controles.CLabel cabecL;
    private gnu.chu.camposdb.cliAvcPanel cli_codiE;
    private gnu.chu.controles.CLabel cli_pobleE;
    private gnu.chu.controles.CTextField emp_codiE;
    private gnu.chu.camposdb.fpaPanel fpa_codiE;
    private gnu.chu.controles.CTextField fvc_anoE;
    private gnu.chu.controles.CTextField fvc_basimpE;
    private gnu.chu.controles.CComboBox fvc_cobradE;
    private gnu.chu.controles.CTextField fvc_dtocomE;
    private gnu.chu.controles.CTextField fvc_dtoppE;
    private gnu.chu.controles.CTextField fvc_fecfraE;
    private gnu.chu.controles.CTextField fvc_impcobE;
    private gnu.chu.controles.CTextField fvc_impdcoE;
    private gnu.chu.controles.CTextField fvc_impdppE;
    private gnu.chu.controles.CTextField fvc_impivaE;
    private gnu.chu.controles.CTextField fvc_imprecE;
    private gnu.chu.controles.CLabel fvc_impresE;
    private gnu.chu.controles.CComboBox fvc_modifE;
    private gnu.chu.controles.CTextField fvc_numeE;
    private gnu.chu.controles.CTextField fvc_porivaE;
    private gnu.chu.controles.CTextField fvc_porreqE;
    private gnu.chu.controles.CComboBox fvc_serieE;
    private gnu.chu.controles.CTextField fvc_sumlinE;
    private gnu.chu.controles.CTextField fvc_sumtotE;
    private gnu.chu.controles.CComboBox fvc_traspE;
    private gnu.chu.controles.Cgrid jt;
    private gnu.chu.controles.Cgrid jtCobr;
    private gnu.chu.controles.CGridEditable jtComCa;
    private gnu.chu.controles.CGridEditable jtComPie;
    private gnu.chu.controles.CGridEditable jtGiros;
    private gnu.chu.controles.CCheckBox opAgrlin;
    private gnu.chu.controles.CLabel pieL;
    // End of variables declaration//GEN-END:variables

}
