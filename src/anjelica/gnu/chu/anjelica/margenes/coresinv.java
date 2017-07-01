package gnu.chu.anjelica.margenes;

import gnu.chu.Menu.Principal;
import gnu.chu.camposdb.proPanel;
import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Types;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

/**
* <p>Titulo: coresinv</p>
* <p>Descripción: Consulta Resultados por Inventario<br>
* <p>Copyright: Copyright (c) 2005-2017
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
*  * @author chuchi P.
* @version 1.0
*/
public class coresinv extends ventana
{
  String s;
  double kgComP,impComP,kgInvIniP,impInvIniP,kgInvFinP,impInvFinP,ganaP,kgMvtos,impMvtos;
  double kgVen;
  double impVen;
  double kgVenT=0,impVenT=0,kgComT=0,impComT=0,kgInvIniT=0;
  double impInvIniT=0,kgInvFinT=0,impInvFinT=0,ganaT=0,kgMvtosT=0,impMvtosT=0;
  private int DIVCODI=1;
  CPanel Pprinc = new CPanel();
  proPanel pro_codiE = new proPanel();
  CLabel cLabel1 = new CLabel();
  CPanel Presul = new CPanel();
  CLabel impVentasL = new CLabel();
  CComboBox feciniE = new CComboBox();
  CLabel cLabel3 = new CLabel();
  CLabel cLabel4 = new CLabel();
  CComboBox fecfinE = new CComboBox();
  CTextField impVentasE = new CTextField(Types.DECIMAL,"---,---,--9.99");
  CLabel cLabel5 = new CLabel();
  CTextField impComprasE = new CTextField(Types.DECIMAL,"---,---,--9.99");
  CLabel impInviniL = new CLabel();
  CTextField impInviniE = new CTextField(Types.DECIMAL,"---,---,--9.99");
  CLabel impInvfinL = new CLabel();
  CTextField impInvfinE = new CTextField(Types.DECIMAL,"---,---,--9.99");
  CLabel gananL = new CLabel("Ganancia");
  CTextField gananE = new CTextField(Types.DECIMAL,"-,---,--9.99");
  CLabel porcGanL= new CLabel("€/Kg");
  CTextField porcGanE = new CTextField(Types.DECIMAL,"--9.999");
  CLabel mvtosL = new CLabel("Mvtos");
  CTextField kgMvtosE = new CTextField(Types.DECIMAL,"--,---,--9.9");
  CTextField impMvtosE = new CTextField(Types.DECIMAL,"---,---,--9.99");
  CButton Baceptar = new CButton(Iconos.getImageIcon("check"));
  CTextField kgVentasE = new CTextField(Types.DECIMAL,"--,---,--9.9");
  CLabel cLabel11 = new CLabel();
  CTextField kgComprasE = new CTextField(Types.DECIMAL,"--,---,--9.9");
  CLabel cLabel12 = new CLabel();
  CTextField kgInviniE = new CTextField(Types.DECIMAL,"--,---,--9.9");
  CTextField kgInvfinE = new CTextField(Types.DECIMAL,"--,---,--9.9");
  CLabel cLabel13 = new CLabel();
  CLabel cLabel14 = new CLabel();
  CPanel Pentdat = new CPanel();
  Cgrid jt = new Cgrid(13);
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CCheckBox opCong = new CCheckBox();
  CCheckBox opDivi = new CCheckBox();
  
  CCheckBox opIncTodo = new CCheckBox("Inc.Todo");

  public coresinv(EntornoUsuario eu, Principal p)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    setTitulo("Cons. Resultado por Inventarios");

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

  public coresinv(gnu.chu.anjelica.menu p, EntornoUsuario eu)
  {
    EU = eu;
    vl = p.getLayeredPane();
    setTitulo("Cons. Resultado por Inventarios");
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
    this.setSize(new Dimension(515, 538));
    this.setVersion("2016-05-03");
    statusBar = new StatusBar(this);
    conecta();
    Pentdat.setBorder(BorderFactory.createLoweredBevelBorder());
    Pentdat.setMaximumSize(new Dimension(500, 53));
    Pentdat.setMinimumSize(new Dimension(500, 53));
    Pentdat.setPreferredSize(new Dimension(500, 53));
    Pentdat.setLayout(null);
    jt.setMaximumSize(new Dimension(471, 288));
    jt.setMinimumSize(new Dimension(471, 288));
    jt.setPreferredSize(new Dimension(471, 288));
    Baceptar.setMaximumSize(new Dimension(81, 25));
    Baceptar.setMargin(new Insets(0, 0, 0, 0));
    Presul.setMaximumSize(new Dimension(451, 85));
    Presul.setMinimumSize(new Dimension(471, 85));
    Presul.setPreferredSize(new Dimension(471, 85));
    opCong.setSelected(true);
    opCong.setText("Inc. Congelado");
    opCong.setBounds(new Rectangle(348, 4, 106, 16));
    if (EU.isRootAV())
    {
        opDivi.setText("Div");
        opDivi.setBounds(new Rectangle(456, 4, 140, 16));
        opDivi.setSelected(true);
    }
    else
        opDivi.setSelected(false);
    impVentasE.setEditable(false);
    impComprasE.setEditable(false);
    impInviniE.setEditable(false);
    impInvfinE.setEditable(false);
    kgVentasE.setEditable(false);
    kgComprasE.setEditable(false);
    kgInviniE.setEditable(false);
    kgMvtosE.setEditable(false);
    kgInvfinE.setEditable(false);
    pro_codiE.setBounds(new Rectangle(60, 25, 271, 20));
    opIncTodo.setToolTipText("Incluir productos NO vendibles");
    opIncTodo.setBounds(new Rectangle(335, 25, 85, 18));
    opIncTodo.setSelected(true);
    Presul.add(impVentasL, null);
    Presul.add(impVentasE, null);
    Presul.add(cLabel12, null);
    Presul.add(kgVentasE, null);
    Presul.add(cLabel11, null);
    Presul.add(kgComprasE, null);
    Presul.add(cLabel5, null);
    Presul.add(impComprasE, null);
    Presul.add(impInviniE, null);
    Presul.add(impInviniL, null);
    Presul.add(kgInviniE, null);
    Presul.add(impInvfinE, null);
    Presul.add(kgInvfinE, null);
    Presul.add(cLabel13, null);
    Presul.add(cLabel14, null);
    Presul.add(mvtosL, null);
    Presul.add(impMvtosE, null);
    Presul.add(kgMvtosE, null);   
    Presul.add(gananE, null);
    Presul.add(gananL, null);
    Presul.add(porcGanE, null);
    Presul.add(porcGanL, null);

    Presul.add(impInvfinL, null);
    Pentdat.add(pro_codiE, null);
    Pentdat.add(cLabel1, null);
    Pentdat.add(Baceptar, null);
    Pentdat.add(cLabel3, null);
    Pentdat.add(feciniE, null);
    Pentdat.add(fecfinE, null);
    Pentdat.add(cLabel4, null);
    Pentdat.add(opCong, null);
    if (EU.isRootAV())
        Pentdat.add(opDivi, null);
    Pentdat.add(opIncTodo, null);
    Pprinc.setLayout(gridBagLayout1);
   
    
    ArrayList v=new ArrayList();
    v.add("Ref."); // 0
    v.add("Nombre"); // 1
    v.add("Imp.Ventas"); //2
    v.add("Kg.Ventas"); // 3
    v.add("Imp.Compras"); // 4
    v.add("Kg.Compras"); // 5
    v.add("Imp.InvIni"); // 6
    v.add("Kg.InvIni"); // 7
    v.add("Imp.InvFin"); // 8
    v.add("Kg.InvFin"); // 9
    v.add("Imp.Mvtos"); // 10
    v.add("Kg.Mvtos"); // 11

    v.add("Ganancia"); // 12
    jt.setCabecera(v);
    jt.setAnchoColumna(new int[]{40,130,80,70,80,70,80,70,80,70,80,70,60});
    jt.setAlinearColumna(new int[]{0,0,2,2,2,2,2,2,2,2,2,2,2});
//    jt.setAjustar(true);
    cLabel1.setText("Producto");
    cLabel1.setBounds(new Rectangle(6, 25, 51, 20));
    Presul.setBorder(BorderFactory.createRaisedBevelBorder());
    Presul.setLayout(null);
    
    feciniE.setBounds(new Rectangle(58, 4, 109, 17));
    cLabel3.setBounds(new Rectangle(4, 4, 51, 18));
    cLabel3.setText("De Fecha");
    cLabel4.setBounds(new Rectangle(187, 4, 47, 17));
    cLabel4.setText("A Fecha");
    fecfinE.setBounds(new Rectangle(233, 4, 109, 17));
    
    impVentasL.setText("Ventas");
    impVentasL.setBounds(new Rectangle(11, 23, 42, 17));
    impVentasE.setBounds(new Rectangle(64, 22, 96, 18));
    cLabel5.setText("Compras");
    cLabel5.setBounds(new Rectangle(238, 22, 51, 18));
    impComprasE.setBounds(new Rectangle(290, 22, 96, 18));
    impComprasE.setText("");
    impInviniL.setText("Inv. Inicial");
    impInviniL.setBounds(new Rectangle(5, 42, 59, 18));
    impInviniE.setBounds(new Rectangle(65, 42, 96, 18));
    impInvfinL.setBounds(new Rectangle(239, 42, 50, 18));
    impInvfinL.setText("Inv. Final");
    
    impInvfinE.setBounds(new Rectangle(290, 42, 96, 18));
    porcGanE.setEditable(false);
    gananE.setEditable(false);
    impMvtosE.setEditable(false);
    
    mvtosL.setBounds(new Rectangle(5, 63, 59, 18));    
    impMvtosE.setBounds(new Rectangle(65, 63, 96, 18));
    kgMvtosE.setBounds(new Rectangle(162, 63, 71, 18));    
    gananL.setBounds(new Rectangle(239, 63, 50, 18));    
    gananE.setBounds(new Rectangle(290, 63, 96, 18));
    porcGanE.setBounds(new Rectangle(390, 63, 50, 18));
    porcGanL.setBounds(new Rectangle(442, 63, 30, 18));
    Baceptar.setBounds(new Rectangle(420, 23, 85, 23));
    Baceptar.setText("Aceptar");

    kgVentasE.setBounds(new Rectangle(161, 22, 74, 18));
    cLabel11.setBounds(new Rectangle(162, 2, 57, 18));
    cLabel11.setBorder(BorderFactory.createLineBorder(Color.black));
    cLabel11.setHorizontalAlignment(SwingConstants.CENTER);
    cLabel11.setText("Kg");
    kgComprasE.setBounds(new Rectangle(390, 22, 71, 18));
    kgComprasE.setText("");
    cLabel12.setBounds(new Rectangle(68, 2, 89, 18));
    cLabel12.setBorder(BorderFactory.createLineBorder(Color.black));
    cLabel12.setHorizontalAlignment(SwingConstants.CENTER);
    cLabel12.setHorizontalTextPosition(SwingConstants.CENTER);
    cLabel12.setText("Euros");
    kgInviniE.setBounds(new Rectangle(162, 42, 71, 18));    
    kgInvfinE.setBounds(new Rectangle(390, 42, 71, 18));
    cLabel13.setText("Kg");
    cLabel13.setHorizontalAlignment(SwingConstants.CENTER);
    cLabel13.setBorder(BorderFactory.createLineBorder(Color.black));
    cLabel13.setBounds(new Rectangle(388, 2, 57, 18));
    cLabel14.setText("Euros");
    cLabel14.setHorizontalTextPosition(SwingConstants.CENTER);
    cLabel14.setHorizontalAlignment(SwingConstants.CENTER);
    cLabel14.setBorder(BorderFactory.createLineBorder(Color.black));
    cLabel14.setBounds(new Rectangle(294, 2, 89, 18));
    this.getContentPane().add(statusBar,BorderLayout.SOUTH);
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
    Pprinc.add(jt,   new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    Pprinc.add(Pentdat,      new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 0, 0, 1), 12, 0));
    Pprinc.add(Presul,    new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
          ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

  }

    @Override
  public void iniciarVentana() throws Exception
  {
    String s,feulin;
     if (EU.isRootAV())
        DIVCODI=0;
    s =
        "select distinct(rgs_fecha) as cci_feccon from v_regstock as r " +
        " where r.emp_codi = " + EU.em_cod +
        " and tir_afestk='=' " +
        " order by cci_feccon desc ";

    if (dtStat.select(s))
    {
      feulin = dtStat.getFecha("cci_feccon", "dd-MM-yyyy");
      do
      {
        feciniE.addItem(dtStat.getFecha("cci_feccon", "dd-MM-yyyy"),
                        dtStat.getFecha("cci_feccon", "dd-MM-yyyy"));
        fecfinE.addItem(dtStat.getFecha("cci_feccon", "dd-MM-yyyy"),
                        dtStat.getFecha("cci_feccon", "dd-MM-yyyy"));
      } while (dtStat.next());
    }
    else
    {
        msgBox("No encontrados Inventarios...\n EL PROGRAMA SE CERRARA");
        statusBar.Bsalir.doClick();
    }
    pro_codiE.iniciar(dtStat,this,vl,EU);
    activarEventos();
    Pentdat.setDefButton(Baceptar);
    feciniE.requestFocus();
    feciniE.setSelectedIndex(1);
  }

  void activarEventos()
  {
    Baceptar.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (Baceptar.getText().equals("Cancelar"))
        {
          Baceptar.setEnabled(false);
          return;
        }
        buscaDatos0();
      }
    });
  }
  void buscaDatos0()
  {
//    if (feciniE.isNull())
//    {
//      mensajeErr("Introduzca Fecha Inicio");
//      feciniE.requestFocus();
//      return;
//    }
//    if (fecfinE.isNull())
//    {
//      mensajeErr("Introduzca Fecha Final");
//      fecfinE.requestFocus();
//      return;
//    }

    new miThread("")
    {
            @Override
      public void run()
      {
        buscaDatos();
        feciniE.requestFocus();
      }
    };

  }
  void buscaDatos()
  {
    try
    {
       if (EU.isRootAV())
        DIVCODI=opDivi.isSelected()?0:1;
       else
        DIVCODI=1;
      Presul.resetTexto();
      String condArticulo= (opIncTodo.isSelected()?"": " and a.pro_tiplot='V' ") +
          ( !opCong.isSelected() ? " and a.pro_artcon = 0 " : "") +
          (pro_codiE.isNull() ? "" : " and pro_codi = " + pro_codiE.getValorInt());
      s = "select 'VI' as tipo,a.pro_codi,"
          + " 0 as canti, sum(avl_canti*avl_prbase) as importe from v_albventa as v, v_articulo as a  " +
            " where avc_fecalb >= TO_DATE('" + feciniE.getText() +"','dd-MM-yyyy') " +
            " and avc_fecalb <= TO_DATE('" + fecfinE.getText() +"','dd-MM-yyyy') " +
            " and avc_serie >= 'A' AND avc_serie <='C' "+
            " and div_codi >= "+DIVCODI+
            " and a.pro_codi = v.pro_codi "+
            condArticulo+
            " group by a.pro_codi "+
           " UNION ALL "+
            "select 'VK' as tipo,a.pro_codi,"
            + " sum(avl_canti) as canti, 0 as importe from v_albventa as v, v_articulo as a  " +
            " where avc_fecalb >= TO_DATE('" + feciniE.getText() +"','dd-MM-yyyy') " +
            " and avc_fecalb <= TO_DATE('" + fecfinE.getText() +"','dd-MM-yyyy') " +
            " and avc_serie >= 'A' AND avc_serie <='C' "+
            " and div_codi >= "+DIVCODI+
            " and a.pro_tiplot ='V' "+ // En kg. Solo tratamos prod. vendibles.
            " and a.pro_codi = v.pro_codi "+
            condArticulo+
            " group by a.pro_codi "+
            " UNION ALL "+
            "SELECT  'C' as tipo,a.pro_codi,sum(acl_canti) as canti, " +
            " sum(acl_canti*acl_prcom) as importe from v_albacom as v,v_articulo as a " +
            " where acc_fecrec >= TO_DATE('" + feciniE.getText() +"','dd-MM-yyyy')" +
            " AND acc_fecrec <= TO_DATE('" + fecfinE.getText() +"','dd-MM-yyyy') " +
             " and a.pro_codi = v.pro_codi "+
            condArticulo+
            " group by a.pro_codi "+
            " UNION ALL "+
            "select 'I' as tipo,a.pro_codi,sum(rgs_kilos) as canti,  sum(rgs_kilos*rgs_prregu) as importe " +            
            " from v_inventar as r, v_articulo as a " +
            " where  rgs_fecha = TO_DATE('" + feciniE.getText() +"','dd-MM-yyyy') "+
            " and a.pro_codi = r.pro_codi "+
            condArticulo+
          " group by a.pro_codi "+
            " UNION ALL "+
            "select 'F' as tipo,a.pro_codi, sum(rgs_kilos) as canti,sum(rgs_kilos*rgs_prregu) as importe " +
            " from v_inventar as r, v_articulo as a " +
            " where  rgs_fecha = TO_DATE('" + fecfinE.getText() +"','dd-MM-yyyy') "+
            " and a.pro_codi = r.pro_codi "+
            condArticulo+
          " group by a.pro_codi "+
            " UNION ALL "+
          "select 'M' as tipo,a.pro_codi,sum(mvt_canti) as canti,"
          + "sum(mvt_canti* mvt_prenet) as importe from mvtosalm as v,v_articulo as a where mvt_tipdoc = 'V' "
          + "and  mvt_time::date >= TO_DATE('" + feciniE.getText() +"','dd-MM-yyyy') " +
            " and mvt_time::date <= TO_DATE('" + fecfinE.getText() +"','dd-MM-yyyy') " +
            " and mvt_serdoc >= 'A' and mvt_serdoc<='C' "+ // Solo albaranes venta normales       
            " and a.pro_codi = v.pro_codi "+
            condArticulo+
            " group by a.pro_codi "+
          " ORDER BY 2,1 ";

      if (! dtCon1.select(s))
      {
        mensajeErr("NO encontrados PRODUCTOS");
        return;
      }

      mensaje("ESPERATE MUYAYO ... BUSCANDO",false);
      activar(false);
      Baceptar.setText("Cancelar");
      Baceptar.setIcon(Iconos.getImageIcon("cancel"));
    
     
      kgVenT=0;impVenT=0;kgComT=0;impComT=0;kgInvIniT=0;
      impInvIniT=0;kgInvFinT=0;impInvFinT=0;ganaT=0;kgMvtosT=0;impMvtosT=0;
      
      jt.setEnabled(false);
      jt.panelG.setVisible(false);
      jt.removeAllDatos();
      
     
      int proCodi=dtCon1.getInt("pro_codi");
      do
      {
        if (proCodi!=dtCon1.getInt("pro_codi"))
        {
            addLineaGrid(proCodi,pro_codiE.getNombArt(proCodi));          
            proCodi=dtCon1.getInt("pro_codi");
        }
        mensajeErr("Tratando Producto ..."+dtCon1.getInt("pro_codi"),false);
        switch (dtCon1.getString("tipo"))
        {
            case "VI":
                 impVen = dtCon1.getDouble("importe",true);  
                 break;
            case "VK":
                 kgVen = dtCon1.getDouble("canti",true);      
                 break;
            case "C":
                kgComP = dtCon1.getDouble("canti",true);       
                impComP = dtCon1.getDouble("importe",true);  
                break;
            case "I":
                kgInvIniP=dtCon1.getDouble("canti", true);
                impInvIniP=dtCon1.getDouble("importe", true);               
                break;
            case "F":
               kgInvFinP = dtCon1.getDouble("canti", true);
               impInvFinP = dtCon1.getDouble("importe", true);               
               break;
            case "M":
               kgMvtos = dtCon1.getDouble("canti", true);
               impMvtos = dtCon1.getDouble("importe", true);
               break;

        }                                         
     } while (dtCon1.next() && Baceptar.isEnabled());
     addLineaGrid(proCodi,pro_codiE.getNombArt(proCodi));
     impVentasE.setValorDec(impVenT);
     kgVentasE.setValorDec(kgVenT);
     impComprasE.setValorDec(impComT);
     kgComprasE.setValorDec(kgComT);

     impInviniE.setValorDec(impInvIniT);
     kgInviniE.setValorDec(kgInvIniT);

     impInvfinE.setValorDec(impInvFinT);
     kgInvfinE.setValorDec(kgInvFinT);
     
     impMvtosE.setValorDec(impMvtosT);
     kgMvtosE.setValorDec(kgMvtosT);
     gananE.setValorDec((impMvtosE.getValorDec()-impComprasE.getValorDec())+
                        ( impInvfinE.getValorDec()- impInviniE.getValorDec()));
     porcGanE.setValorDec(gananE.getValorDec()/kgMvtosT);
     if (!Baceptar.isEnabled())
       mensajeErr("Consulta Cancelada...");
     else
       mensajeErr("YA'TA....");
     mensaje("");
     jt.panelG.setVisible(true);
     activar(true);
     Baceptar.setText("Aceptar");
     Baceptar.setIcon(Iconos.getImageIcon("check"));
     Baceptar.setEnabled(true);
    } catch (Exception k)
    {
      Error("Error al buscar datos", k);
    }
  }
    void addLineaGrid(int proCodi, String proNomb) 
    {
        impVenT += impVen;
        kgVenT += kgVen;
        impComT += impComP;
        kgComT += kgComP;
        impInvIniT += impInvIniP;
        kgInvIniT += kgInvIniP;
        impInvFinT += impInvFinP;
        kgInvFinT += kgInvFinP;
        kgMvtosT += kgMvtos;
        impMvtosT += impMvtos;
        ganaP = (impMvtos - impComP) + (impInvFinP - impInvIniP);
//        if (kgVen == 0 && kgMvtos == 0)
//        {
//            resetValores();
//            return;
//        }
        ArrayList v = new ArrayList();
        v.add(proCodi);
        v.add(proNomb);
        v.add(Formatear.format(impVen, "--,---,--9.9"));
        v.add(Formatear.format(kgVen, "----,--9.9"));
        v.add(Formatear.format(impComP, "--,---,--9.9"));
        v.add(Formatear.format(kgComP, "----,--9.9"));
        v.add(Formatear.format(impInvIniP, "--,---,--9.9"));
        v.add(Formatear.format(kgInvIniP, "----,--9.9"));
        v.add(Formatear.format(impInvFinP, "--,---,--9.9"));
        v.add(Formatear.format(kgInvFinP, "----,--9.9"));
        v.add(Formatear.format(impMvtos, "--,---,--9.9"));
        v.add(Formatear.format(kgMvtos, "----,--9.9"));

        v.add(Formatear.format(ganaP, "----,--9.9"));
        jt.addLinea(v);
        resetValores();

    }
    void resetValores()
    {
        kgComP=0;impComP=0;kgInvIniP=0;impInvIniP=0;impVen=0;kgVen=0;
        kgMvtos=0;impMvtos=0;
        kgInvFinP=0;impInvFinP=0;
    }
  void activar(boolean enab)
  {
    fecfinE.setEnabled(enab);
    feciniE.setEnabled(enab);
    opIncTodo.setEnabled(enab);
    opCong.setEnabled(enab);
    pro_codiE.setEnabled(enab);
    statusBar.setEnabled(enab);
  }
}
