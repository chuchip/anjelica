package gnu.chu.anjelica.ventas;

import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import java.awt.*;
import javax.swing.BorderFactory;
import java.sql.*;
import java.awt.event.*;
import java.util.*;
import gnu.chu.Menu.*;
import gnu.chu.anjelica.listados.Listados;
import gnu.chu.interfaces.ejecutable;
import javax.swing.event.*;
import net.sf.jasperreports.engine.*;

/**
 *
 * <p>Titulo: clAlbSinCosto</p>
 * <p>Descripción: Cons/Listado Albaranes sin costo </p>
 * <p>Copyright: Copyright (c) 2005-2015
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
 * @author chuchi P
 * @version 1.0
 */
public class clAlbSinCosto extends ventana
{
  private final int  JT_EMPALB=1;
  private final int  JT_EJEALB=2;
  private final int  JT_SERALB=3;
  private final int  JT_NUMALB=4;
  CPanel Pprinc = new CPanel();
  CPanel Pentra = new CPanel();
  CTextField feciniE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel cLabel1 = new CLabel();
  CLabel cLabel2 = new CLabel();
  CTextField fecfinE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CButton Bconsulta = new CButton("Consulta F4",Iconos.getImageIcon("check"));
  Cgrid jt = new Cgrid(11);
  String s;
  Cgrid jtLin = new Cgrid(5);
  CButton Bprint = new CButton(Iconos.getImageIcon("print"));
  CLabel cLabel3 = new CLabel();
  CComboBox tipListE = new CComboBox();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CLabel cLabel8 = new CLabel();
  CTextField empiniE = new CTextField(Types.DECIMAL,"#9");
  CLabel cLabel9 = new CLabel();
  CTextField empfinE = new CTextField(Types.DECIMAL,"#9");
  CCheckBox opIncImp0 = new CCheckBox();

  public clAlbSinCosto(EntornoUsuario eu, Principal p) {
     EU=eu;
     vl=p.panel1;
     jf=p;
     eje=true;

     setTitulo("Cons./Listado Albaranes sin Precio ");

     try  {
       if(jf.gestor.apuntar(this))
           jbInit();
       else
         setErrorInit(true);
     }
     catch (Exception e) {
       ErrorInit(e);
     }
   }

   public clAlbSinCosto(gnu.chu.anjelica.menu p,EntornoUsuario eu) {

     EU=eu;
     vl=p.getLayeredPane();
     setTitulo("Cons./Listado Albaranes sin Precio");
     eje=false;

     try  {
       jbInit();
     }
     catch (Exception e) {
      ErrorInit(e);
     }
   }

 private void jbInit() throws Exception
 {
   iniciarFrame();
   this.setVersion("2015-08-19");
   this.setSize(new Dimension(623, 442));
   conecta();
   statusBar = new StatusBar(this);
    Pprinc.setLayout(gridBagLayout1);
    Pentra.setBorder(BorderFactory.createRaisedBevelBorder());
    Pentra.setMaximumSize(new Dimension(424, 53));
    Pentra.setMinimumSize(new Dimension(424, 53));
    Pentra.setPreferredSize(new Dimension(424, 53));
    Pentra.setLayout(null);
    feciniE.setBounds(new Rectangle(65, 5, 79, 17));
    cLabel1.setText("De Fecha");
    cLabel1.setBounds(new Rectangle(9, 5, 51, 18));
    cLabel2.setText("A Fecha");
    cLabel2.setBounds(new Rectangle(149, 5, 47, 17));
    fecfinE.setBounds(new Rectangle(195, 5, 79, 17));
    Bconsulta.setBounds(new Rectangle(213, 24, 110, 21));
    Bconsulta.setMargin(new Insets(0, 0, 0, 0));

    jt.setMaximumSize(new Dimension(444, 206));
    jt.setMinimumSize(new Dimension(444, 206));
    jt.setPreferredSize(new Dimension(444, 206));
    ArrayList v = new ArrayList();
    v.add("Fec.Alb"); // 0
    v.add("Emp"); // 1
    v.add("Año"); // 2
    v.add("Serie"); // 3
    v.add("Num."); // 4
    v.add("Cliente"); // 5
    v.add("Nombre"); // 6
    v.add("Importe"); // 7
    v.add("Conf."); //8
    v.add("T.Fact."); // 9
    v.add("Giro"); //10

    jt.setCabecera(v);
    jt.setAnchoColumna(new int[]{75,30,50,25,50,60,120,80,30,40,30});
    jt.setAlinearColumna(new int[]{1,2,2,1,2,2,0,2,1,1,1});
    jt.setFormatoColumna(7,"---,--9.99");
    jt.setFormatoColumna(8,"B-");
    jt.setFormatoColumna(10,"BSN");
    jt.setAjustarGrid(true);

    jtLin.setMaximumSize(new Dimension(442, 119));
    jtLin.setMinimumSize(new Dimension(442, 119));
    jtLin.setPreferredSize(new Dimension(442, 119));
    ArrayList v1=new ArrayList();
    Bprint.setBounds(new Rectangle(326, 24, 87, 21));
    Bprint.setMargin(new Insets(0, 0, 0, 0));
    Bprint.setText("Imprimir");
    cLabel3.setText("Tipo");
    cLabel3.setBounds(new Rectangle(277, 5, 28, 17));
    tipListE.setBounds(new Rectangle(309, 5, 102, 17));
    Pprinc.setFont(new java.awt.Font("Dialog", 0, 12));
    cLabel8.setText("De Empresa");
    cLabel8.setBounds(new Rectangle(7, 24, 69, 17));
    empiniE.setBounds(new Rectangle(76, 24, 27, 17));
    cLabel9.setBounds(new Rectangle(108, 24, 69, 17));
    cLabel9.setText("A Empresa");
    empfinE.setBounds(new Rectangle(177, 24, 27, 17));
    opIncImp0.setToolTipText("Incluir Albaranes con Importe a cero");
    opIncImp0.setSelected(true);
    opIncImp0.setText("Inc. Imp. = 0");
    opIncImp0.setBounds(new Rectangle(413, 5, 104, 17));
    v1.add("Prod"); // 0
    v1.add("Nombre"); // 1
    v1.add("Kilos"); // 2
    v1.add("Unid"); // 3
    v1.add("Precio"); // 4
    jtLin.setCabecera(v1);
    jtLin.setAnchoColumna(new int[]{50,150,50,40,50});
    jtLin.setAlinearColumna(new int[]{2,0,2,2,2});
    jtLin.setFormatoColumna(2,"---,--9.99");
    jtLin.setFormatoColumna(3,"---9");
    jtLin.setFormatoColumna(4,"----9.99");

    jtLin.setAjustarGrid(true);

    jtLin.setBuscarVisible(false);
    this.getContentPane().add(statusBar,  BorderLayout.SOUTH);
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
    Pentra.setDefButton(Bconsulta);
    Pentra.add(cLabel1, null);
    Pentra.add(feciniE, null);
    Pprinc.add(jtLin,   new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 3, 0), 0, 0));
    Pprinc.add(jt,   new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    Pprinc.add(Pentra,     new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 95, 0));
    Pentra.add(fecfinE, null);
    Pentra.add(cLabel2, null);
    Pentra.add(Bprint, null);
    Pentra.add(Bconsulta, null);
    Pentra.add(cLabel8, null);
    Pentra.add(empiniE, null);
    Pentra.add(empfinE, null);
    Pentra.add(cLabel9, null);
    Pentra.add(tipListE, null);
    Pentra.add(cLabel3, null);
    Pentra.add(opIncImp0, null);
  }
    @Override
  public void iniciarVentana() throws Exception
  {

    tipListE.addItem("Precios 0","P");
    tipListE.addItem("Sin Factur","F");
    tipListE.addItem("NO Valorado","V");
    Pentra.setDefButton(Bconsulta);
    empiniE.setValorInt(EU.em_cod);
    empfinE.setValorInt(EU.em_cod);
    activarEventos();
    feciniE.requestFocus();
  }
  public void activarEventos()
  {
    Bconsulta.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Bconsulta_actionPerformed();
      }
    });
    jt.tableView.getSelectionModel().addListSelectionListener(new ListSelectionListener()
    {
      @Override
      public void valueChanged(ListSelectionEvent e)
      {
        if (! jt.isEnabled())
          return;
        if (e.getValueIsAdjusting()) // && e.getFirstIndex() == e.getLastIndex())
          return;
        verDatLin();
      }
    });
    creaPopEspere();
    jt.addMouseListener(new MouseAdapter()
    {
         @Override
         public void mouseClicked(MouseEvent e) {
             if (e.getClickCount()<2)
                 return;
             if (jt.isVacio() || jf==null)
                 return;
             msgEspere("Ejecutando consulta en Mant. Albaranes");
             new miThread("")
             {
                 @Override
                 public void run()
                 {
                   javax.swing.SwingUtilities.invokeLater(new Thread()
                   {
                       @Override
                       public void run()
                       {                                                   
                            ejecutable prog;
                            if ((prog=jf.gestor.getProceso(pdalbara.getNombreClase()))==null)
                                return;
                            pdalbara cm=(pdalbara) prog;
                            if (cm.inTransation())
                            {
                                msgBox("Mantenimiento Albaranes de Ventas ocupado. No se puede realizar la busqueda");
                                return;
                            }
                            cm.PADQuery();
                            cm.setSerieAlbaran(jt.getValString(JT_SERALB));
                            cm.setNumeroAlbaran(jt.getValorInt(JT_NUMALB));
                            cm.setEjercAlbaran(jt.getValorInt(JT_EJEALB));
                            cm.setEmpresaAlbaran(jt.getValorInt(JT_EMPALB));
                            cm.ej_query();
                            jf.gestor.ir(cm);
                            resetMsgEspere();                                                         
                       }
                   });
                   
                 }
             };
              
              

         }
    });
    Bprint.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
       Bprint_actionPerformed();
      }
    });
  }

  void verDatLin()
  {
    try
    {
      s="select l.pro_codi,a.pro_nomb,l.avl_canti,l.avl_unid,l.avl_prven " +
       " from v_albavel as l,v_articulo as a "+
       "  where l.emp_codi = "+jt.getValorInt(1)+
       " and l.avc_ano = "+jt.getValorInt(2)+
       " and l.avc_serie = '"+jt.getValString(3)+ "'"+
       " and l.avc_nume = "+jt.getValorInt(4)+
       " and l.avl_prven = 0 "+
       " and l.avl_canti != 0 "+
       " and l.pro_codi = a.pro_codi "+
       " and l.pro_codi >= 100 "+
       " order by avl_numlin ";
     dtStat.select(s);
     jtLin.setEnabled(false);
     jtLin.setDatos(dtStat);
     jtLin.setEnabled(true);
//     jtLin.requestFocusInicio();
    } catch (Exception k)
    {
      Error("Error al Ver Lineas de Albaran",k);
    }
  }
  String getCondWhere()
  {
    return "c.avc_fecalb >= TO_DATE('" + feciniE.getText() + "','dd-MM-yyyy') " +
          " AND c.avc_fecalb <= TO_DATE('" + fecfinE.getText() +   "','dd-MM-yyyy') " +
          " and c.emp_codi>= "+empiniE.getValorInt()+
          " and c.emp_codi<= "+empfinE.getValorInt()+
          " and c.avc_serie!='X'"+
          (opIncImp0.isSelected()?"":" and c.avc_impalb != 0 ")+
          (EU.isRootAV()?"":" and c.div_codi > 0 ");
  }
  void Bconsulta_actionPerformed()
  {
    if (feciniE.getError() || feciniE.isNull())
    {
      mensajeErr("Introduzca Fecha Inicial");
      feciniE.requestFocus();
      return;
    }
    if (fecfinE.getError() || fecfinE.isNull())
    {
      mensajeErr("Introduzca Fecha Final");
      fecfinE.requestFocus();
      return;
    }

    if (tipListE.getValor().equals("P")) // Precios 0
      s = "SELECT c.avc_fecalb,c.emp_codi,c.avc_ano,c.avc_serie,c.avc_nume,c.cli_codi,cl.cli_nomb, " +
        " c.avc_impalb,c.avc_confo,cl.cli_tipfac,cl.cli_giro "+
        "  FROM v_albavec as c left join clientes as cl on  c.cli_codi = cl.cli_codi " +
          "  WHERE "+getCondWhere()+
          " and c.avc_valora !=  "+pdalbara.AVC_VALORADO +// No incluir los puestos como valorados
          " and exists (select * from v_albavel as l,v_articulo as ar " +
          "  where c.emp_codi = l.emp_codi " +
          " and c.avc_ano = l.avc_ano " +
          " and c.avc_serie = l.avc_serie " +
          " and c.avc_nume = l.avc_nume " +
          " and l.avl_canti != 0 " +
          " and l.avl_prven = 0 " +
          " and l.pro_codi = ar.pro_codi "+
          " and ar.pro_tiplot = 'V') "+
          " ORDER BY c.avc_fecalb,c.cli_codi";
   else if (tipListE.getValor().equals("V")) // NO Valorados
     s = "SELECT c.avc_fecalb,c.emp_codi,c.avc_ano,c.avc_serie,c.avc_nume,c.cli_codi,cl.cli_nomb, " +
         " c.avc_impalb,c.avc_confo,cl.cli_tipfac,cl.cli_giro "+
         "  FROM v_albavec as c left join clientes as cl on  c.cli_codi = cl.cli_codi " +
        "  WHERE "+getCondWhere()+
        " and avc_valora !=  "+pdalbara.AVC_VALORADO +
         " ORDER BY c.avc_fecalb,c.cli_codi";
   else // Sin facturar
     s = "SELECT c.avc_fecalb,c.emp_codi,c.avc_ano,c.avc_serie,c.avc_nume,c.cli_codi,cl.cli_nomb, " +
         " c.avc_impalb,c.avc_confo,cl.cli_tipfac,cl.cli_giro "+
         "  FROM v_albavec as c left join clientes as cl on  c.cli_codi = cl.cli_codi " +
         "  WHERE "+getCondWhere()+
         " and (fvc_ano = 0 or fvc_ano is null) "+
         " and avc_impalb != 0 "+
         " ORDER BY c.avc_fecalb,c.cli_codi";

   try {
     dtCon1.select(s);
     jt.setEnabled(false);
     jt.setDatos(dtCon1);
     jt.setEnabled(true);
     if (jt.isVacio())
     {
       mensajeErr("NO Encontrados albaranes con estos criterios");
       return;
     }
     jt.requestFocusInicio();
     verDatLin();
   } catch (Exception k)
   {
     Error("Error al buscar Datos",k);
   }
  }
  void Bprint_actionPerformed()
  {
    new miThread("")
    {
      public void run()
      {
        imprimir();
      }
    };
  }

  void imprimir()
  {
    this.setEnabled(false);
    try
    {
      mensaje("Esperate, tio ... estoy Generando el Listado");
      if (tipListE.getValor().equals("P"))
        s = "SELECT c.avc_fecalb,c.emp_codi,c.avc_ano,c.avc_serie," +
            "  c.avc_nume,c.cli_codi,cl.cli_nomb,l.pro_codi," +
            " a.pro_nomb,l.avl_canti " +
            " FROM v_albavec as c "+
            "  left join clientes as cl on  c.cli_codi = cl.cli_codi " +
            " ,v_albavel as l, " +
            " v_articulo as a where " +
            " a.pro_codi = l.pro_codi " +
            " and c.avc_valora !=  "+ pdalbara.AVC_VALORADO +// No incluir los puestos como valorados
            " and c.cli_codi = cl.cli_codi " +
            " and c.emp_codi = l.emp_codi " +
            " and c.avc_ano = l.avc_ano " +
            " and c.avc_serie = l.avc_serie " +
            " and c.avc_nume = l.avc_nume " +
            " and l.avl_canti != 0 " +
            " and l.avl_prven = 0 " +
            " and A.pro_tiplot = 'V' " +
            " and "+getCondWhere()+
            " ORDER BY c.avc_fecalb,c.cli_codi";
      else if (tipListE.getValor().equals("V")) // NO Valorados
        s =
            "SELECT c.avc_fecalb,c.emp_codi,c.avc_ano,c.avc_serie,c.avc_nume,c.cli_codi,cl.cli_nomb " +
            "  FROM v_albavec as c  left join clientes as cl on  c.cli_codi = cl.cli_codi " +
            "  WHERE "+getCondWhere()+
            " and avc_valora !=  " +pdalbara.AVC_VALORADO +
            " ORDER BY c.avc_fecalb,c.cli_codi";
      else
        s =
            "SELECT c.avc_fecalb,c.emp_codi,c.avc_ano,c.avc_serie,c.avc_nume,c.cli_codi,cl.cli_nomb, " +
            " c.avc_impalb,c.avc_confo,cl.cli_tipfac,cl.cli_giro "+
            "  FROM v_albavec as c left join clientes as cl on  c.cli_codi = cl.cli_codi " +
            "  WHERE "+getCondWhere()+
            " and avc_impalb != 0 "+
            " and (fvc_ano = 0 or fvc_ano is null) " +
            " ORDER BY c.avc_fecalb,c.cli_codi";
     debug(s);

      dtCon1.setStrSelect(s);
      ResultSet rs = ct.createStatement().executeQuery(dtCon1.getStrSelect());
      JasperReport jr;
      if (tipListE.isValor("P"))
        jr =Listados.getJasperReport(EU, "clAlbSinCosto");
      else if (tipListE.isValor("V"))
        jr = Listados.getJasperReport(EU, "albSinValor");
      else
         jr = Listados.getJasperReport(EU,"albSinFra");

      java.util.HashMap mp = Listados.getHashMapDefault();
      mp.put("fechas", "DEL : " + feciniE.getText() + " AL " + fecfinE.getText());
      JasperPrint jp = JasperFillManager.fillReport(jr, mp, new JRResultSetDataSource(rs));
      gnu.chu.print.util.printJasper(jp, EU);
      mensaje("");
      mensajeErr("Listado ... Generado");
      this.setEnabled(true);
    }
    catch (Exception k)
    {
      Error("Error al Generar Listado", k);
    }
  }

}
