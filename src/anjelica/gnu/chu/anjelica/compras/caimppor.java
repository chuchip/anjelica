package gnu.chu.anjelica.compras;


import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import java.sql.*;
import gnu.chu.Menu.*;
import java.awt.*;
import java.util.*;
import javax.swing.BorderFactory;
import java.awt.event.ActionEvent;
import java.awt.event.*;
import javax.swing.event.*;
import gnu.chu.camposdb.proPanel ;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.*;

  /**
   *
   * <p>Titulo: caimppor</p>
   * <p>Descripción: Calculo y Listado  Importe de Portes de Albaranes de Compras
   * <p>Copyright: Copyright (c) 2005-2014</p>
   * <p>  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
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
   * </p>
   * <p>Empresa: MISL</p>
   * @author chuchiP
   * @version 1.0
   */

public class caimppor extends ventana implements  JRDataSource
{
  int nRow;
  ResultSet rs;
  proPanel pro_codiE = new proPanel();
  CPanel Pprinc = new CPanel();
  CPanel Pcrite = new CPanel();
  CTextField feciniE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel1 = new CLabel();
  CButton Baceptar = new CButton();
  CLabel cLabel2 = new CLabel();
  CTextField fecfinE = new CTextField(Types.DATE, "dd-MM-yyyy");
  Cgrid jt = new Cgrid(11);
  CPanel Presul = new CPanel();
  CLabel cLabel3 = new CLabel();
  CTextField numAlbE = new CTextField(Types.DECIMAL,"###9");
  CTextField numUniE = new CTextField(Types.DECIMAL,"----,--9");
  CLabel cLabel4 = new CLabel();
  CLabel cLabel5 = new CLabel();
  CTextField kilosE = new CTextField(Types.DECIMAL,"---,--9.99");
  CTextField importeE = new CTextField(Types.DECIMAL,"----,--9.99");
  CLabel cLabel6 = new CLabel();
  CLabel cLabel7 = new CLabel();
  CTextField kilFactE = new CTextField(Types.DECIMAL,"----,--9.99");
  CLabel cLabel8 = new CLabel();
  CTextField impKilE = new CTextField(Types.DECIMAL,"---9.999");
  CLabel cLabel9 = new CLabel();
  CTextField emp_codiE = new CTextField(Types.DECIMAL,"#9");
  CLabel cLabel10 = new CLabel();
  CTextField acc_anoE = new CTextField(Types.DECIMAL,"###9");
  Cgrid jtLin = new Cgrid(6);
  CLabel cLabel11 = new CLabel();
  CTextField impPorteE = new CTextField(Types.DECIMAL,"----,--9.99");
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CButton Bimprim = new CButton("Imprimir",Iconos.getImageIcon("print"));
  CCheckBox opSelec = new CCheckBox();

  public caimppor(EntornoUsuario eu, Principal p)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    setTitulo("Calc./List Importe de Portes");

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

  public caimppor(gnu.chu.anjelica.menu p, EntornoUsuario eu)
  {

    EU = eu;
    vl = p.getLayeredPane();
    setTitulo("Calc./List Importe de Portes");
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
   this.setSize(new Dimension(720, 412));
   this.setVersion("2005-11-19");

   statusBar=new StatusBar(this);
   conecta();
    Pprinc.setLayout(gridBagLayout1);
    Pcrite.setBorder(BorderFactory.createLoweredBevelBorder());
    Pcrite.setMaximumSize(new Dimension(518, 27));
    Pcrite.setMinimumSize(new Dimension(518, 27));
    Pcrite.setPreferredSize(new Dimension(518, 27));
    Pcrite.setDefButton(Baceptar);
    Pcrite.setLayout(null);
    cLabel1.setText("De Fecha");
    cLabel1.setBounds(new Rectangle(174, 2, 55, 17));
    Baceptar.setText("Aceptar F4");
    Baceptar.setBounds(new Rectangle(408, 2, 106, 22));
    Baceptar.setIcon(Iconos.getImageIcon("check"));
    Baceptar.setMargin(new Insets(0, 0, 0, 0));
    cLabel2.setText("A");
    cLabel2.setBounds(new Rectangle(314, 3, 19, 17));
    Presul.setBorder(BorderFactory.createRaisedBevelBorder());
    Presul.setMaximumSize(new Dimension(495, 44));
    Presul.setMinimumSize(new Dimension(495, 44));
    Presul.setPreferredSize(new Dimension(495, 44));
    Presul.setLayout(null);
    Pcrite.setButton(KeyEvent.VK_F4,Baceptar);
    Vector v=new Vector();
    v.addElement("Prov"); // 0
    v.addElement("Nombre Prov"); // 1
    v.addElement("Fec. Alb."); // 2
    v.addElement("Unid."); // 3
    v.addElement("Kilos"); // 4
    v.addElement("Importe"); // 5
    v.addElement("Inc"); // 6
    v.addElement("I/K Porte"); // 7
    v.addElement("Imp Porte"); // 8
    v.addElement("Ser"); // 9
    v.addElement("Alb"); // 10

    jt.setCabecera(v);
    jt.setMaximumSize(new Dimension(565, 160));
    jt.setMinimumSize(new Dimension(565, 160));
    jt.setPreferredSize(new Dimension(565, 160));
    jt.setAnchoColumna(new int[]{40,150,80,60,80,90,40,60,80,30,40});
    jt.setAlinearColumna(new int[]{2,0,1,2,2,2,1,2,2,0,2});
    jt.setFormatoColumna(3,"--,--9");
    jt.setFormatoColumna(4,"---,--9.99");
    jt.setFormatoColumna(5,"--,---,--9.99");
    jt.setFormatoColumna(6,"BSN");
    jt.setFormatoColumna(7,"--9.999");
    jt.setFormatoColumna(8,"---,--9.99");
//    jt.setAjustarGrid(true);

    Vector v1=new Vector();
    v1.addElement("Prod"); // 0
    v1.addElement("Ref Prod"); //1
    v1.addElement("Unid"); // 2
    v1.addElement("Kilos"); // 3
    v1.addElement("Precio"); // 4
    v1.addElement("Importe"); // 5
    jtLin.setCabecera(v1);
    jtLin.setAnchoColumna(new int[]{50,200,60,80,70,90});
    jtLin.setAlinearColumna(new int[]{0,0,2,2,2,2});
    jtLin.setFormatoColumna(2,"---9");
    jtLin.setFormatoColumna(3,"----,--9.99");
    jtLin.setFormatoColumna(4,"---9.99");
    jtLin.setFormatoColumna(5,"----,--9.99");
    jtLin.setAjustarGrid(true);

    cLabel3.setText("Albaranes");
    cLabel3.setBounds(new Rectangle(4, 2, 60, 18));
    cLabel4.setText("Unidades");
    cLabel4.setBounds(new Rectangle(122, 2, 54, 18));
    numAlbE.setEnabled(false);
    numAlbE.setBounds(new Rectangle(68, 2, 39, 18));
    numUniE.setEnabled(false);
    numUniE.setBounds(new Rectangle(178, 2, 62, 17));
    kilosE.setEnabled(false);
    kilosE.setBounds(new Rectangle(279, 2, 71, 18));
    importeE.setEnabled(false);
    importeE.setBounds(new Rectangle(410, 2, 71, 18));
    cLabel5.setText("Kilos");
    cLabel5.setBounds(new Rectangle(249, 2, 32, 18));
    cLabel6.setText("Importe");
    cLabel6.setBounds(new Rectangle(362, 2, 49, 18));
    cLabel7.setText("Kilos Fact.");
    cLabel7.setBounds(new Rectangle(181, 22, 59, 18));
    cLabel8.setText("Importe Kilo");
    cLabel8.setBounds(new Rectangle(341, 22, 70, 18));
    cLabel9.setText("Empresa");
    cLabel9.setBounds(new Rectangle(4, 3, 52, 17));
    cLabel10.setText("Ejercicio");
    cLabel10.setBounds(new Rectangle(78, 3, 52, 17));

    acc_anoE.setBounds(new Rectangle(127, 3, 43, 17));
    jtLin.setMaximumSize(new Dimension(567, 85));
    jtLin.setMinimumSize(new Dimension(567, 85));
    jtLin.setPreferredSize(new Dimension(567, 85));
    jtLin.setBuscarVisible(false);
    cLabel11.setText("Imp.Portes");
    cLabel11.setBounds(new Rectangle(5, 22, 62, 17));
    impPorteE.setEnabled(false);
    impPorteE.setBounds(new Rectangle(67, 22, 71, 17));
    impKilE.setEnabled(false);
    impKilE.setBounds(new Rectangle(418, 22, 63, 17));
    fecfinE.setBounds(new Rectangle(327, 3, 77, 17));
    feciniE.setBounds(new Rectangle(226, 3, 76, 17));
    emp_codiE.setBounds(new Rectangle(53, 3, 23, 17));
    kilFactE.setBounds(new Rectangle(245, 22, 71, 17));
    Bimprim.setBounds(new Rectangle(488, 4, 115, 22));
    Bimprim.setMargin(new Insets(0, 0, 0, 0));
    opSelec.setSelected(true);
    opSelec.setText("Solo Alb. Selec.");
    opSelec.setSelecion("false");
    opSelec.setBounds(new Rectangle(491, 25, 121, 17));
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
    this.getContentPane().add(statusBar, borderLayout1.SOUTH);
    Pcrite.add(cLabel9, null);
    Pcrite.add(Baceptar, null);
    Pcrite.add(feciniE, null);
    Pcrite.add(cLabel2, null);
    Pcrite.add(fecfinE, null);
    Pcrite.add(emp_codiE, null);
    Pcrite.add(acc_anoE, null);
    Pcrite.add(cLabel10, null);
    Pcrite.add(cLabel1, null);
    Pprinc.add(Presul,    new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    Pprinc.add(jtLin,   new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    Pprinc.add(jt,   new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    Presul.add(cLabel3, null);
    Presul.add(numAlbE, null);
    Presul.add(cLabel4, null);
    Presul.add(numUniE, null);
    Presul.add(cLabel5, null);
    Presul.add(kilosE, null);
    Presul.add(cLabel6, null);
    Presul.add(importeE, null);
    Presul.add(impKilE, null);
    Presul.add(kilFactE, null);
    Presul.add(cLabel7, null);
    Presul.add(impPorteE, null);
    Presul.add(cLabel11, null);
    Presul.add(cLabel8, null);
    Presul.add(Bimprim, null);
    Presul.add(opSelec, null);
    Pprinc.add(Pcrite,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 0, 0, 0), 0, 0));
 }
 public void iniciarVentana() throws Exception
 {
   emp_codiE.setValorDec(EU.em_cod);
   acc_anoE.setValorDec(EU.ejercicio);
   feciniE.setText(Formatear.sumaDias(Formatear.getDateAct(),-7));
   fecfinE.setText(Fecha.getFechaSys("dd-MM-yyyy"));
   activarEventos();
   feciniE.requestFocus();
 }

 void activarEventos()
 {
   Baceptar.addActionListener(new ActionListener()
   {
     public void actionPerformed(ActionEvent e)
     {
       Baceptar_actionPerformed();
     }
   });
   jt.tableView.getSelectionModel().addListSelectionListener(new
       ListSelectionListener()
   {
     public void valueChanged(ListSelectionEvent e)
     {
       if (e.getValueIsAdjusting()) // && e.getFirstIndex() == e.getLastIndex())
         return;
       if (jt.isVacio())
         return;
       verDatLin();
     }
   });
   jt.tableView.addMouseListener(new MouseAdapter()
   {
     public void mouseClicked(MouseEvent e)
     {
       if (jt.isVacio() || jt.getSelectedColumn()!=6)
         return;
       jt.setValor(new Boolean(!jt.getValBoolean(6)));
       actAcum();
     }

   });
   kilFactE.addFocusListener(new FocusAdapter()
   {
     public void focusLost(FocusEvent e)
     {
       if (kilFactE.getValorDec()==0)
         kilFactE.setValorDec(kilosE.getValorDec());
       actImpPortes(kilFactE.getValorDec());
     }
   });
   Bimprim.addActionListener(new ActionListener()
   {
     public void actionPerformed(ActionEvent e)
     {
       imprimir();
     }
   });
 }
 void Baceptar_actionPerformed()
 {
   String s;
   s="SELECT c.*,p.prv_nomb FROM v_albacoc AS C,v_proveedo AS P "+
       " WHERE c.emp_codi = "+emp_codiE.getValorInt()+
       " and c.acc_ano = "+acc_anoE.getValorInt()+
       " and c.acc_fecrec >= TO_DATE('"+feciniE.getText()+"','dd-MM-yyyy')"+
       " and c.acc_fecrec <= TO_DATE('"+fecfinE.getText()+"','dd-MM-yyyy')"+
       " and P.prv_codi = c.prv_codi "+
       " ORDER BY c.acc_fecrec,c.prv_codi";
   try
   {
     jt.setEnabled(false);
     jt.removeAllDatos();
     if (!dtCon1.select(s))
     {
       mensajeErr("No encontrados ALBARANES DE COMPRA");
       jt.setEnabled(true);
       return;
     }
     Vector v=new Vector();
     do
     {
       s="SELECT SUM(acl_numcaj) as numuni, SUM(acl_canti) as kilos,"+
           " sum(acl_canti*(acl_prcom - "+dtCon1.getDouble("acc_impokg",true)+")) as importe "+
           " from v_albacol "+
           " WHERE emp_codi = "+emp_codiE.getValorInt()+
           " and acc_ano = "+acc_anoE.getValorInt()+
           " and acc_serie = '"+dtCon1.getString("acc_serie")+"'"+
           " and acc_nume = "+dtCon1.getInt("acc_nume");
       dtStat.select(s);
       v.removeAllElements();
       v.addElement(dtCon1.getString("prv_codi"));
       v.addElement(dtCon1.getString("prv_nomb"));
       v.addElement(dtCon1.getFecha("acc_fecrec","dd-MM-yyyy"));
       v.addElement(dtStat.getString("numuni"));
       v.addElement(dtStat.getString("kilos"));
       v.addElement(dtStat.getString("importe"));
       v.addElement(new Boolean(dtCon1.getDouble("acc_impokg",true)!=0));
       v.addElement(dtCon1.getString("acc_impokg"));
       v.addElement(""+dtCon1.getDouble("acc_impokg",true)*dtStat.getDouble("kilos"));
       v.addElement(dtCon1.getString("acc_serie"));
       v.addElement(dtCon1.getString("acc_nume"));
       jt.addLinea(v);
     } while (dtCon1.next());
     jt.requestFocusInicio();
     jt.setEnabled(true);
     verDatLin();
     actAcum();
   }
   catch (Exception k)
   {
     Error("Error al buscar datos de Compras", k);
   }
 }

 void verDatLin()
 {
   if (! jt.isEnabled())
     return;
   try {
     String s;
     s = "SELECT pro_codi,acl_prcom, sum(acl_canti) as acl_canti, " +
         " sum(acl_numcaj) as acl_numcaj " +
         " FROM v_albacol AS l " +
         " WHERE emp_codi = " + emp_codiE.getValorInt() +
         " and acc_ano = " + acc_anoE.getValorInt() +
         " and acc_serie = '" + jt.getValString(9) + "'" +
         " and acc_nume = " + jt.getValInt(10) +
         " GROUP BY pro_codi,acl_prcom";
     jtLin.removeAllDatos();
     if (!dtCon1.select(s))
       return;
     do
     {
       Vector v=new Vector();
       v.addElement(dtCon1.getString("pro_codi"));
       s=pro_codiE.getNombArt(dtCon1.getString("pro_codi"),EU.em_cod,0,dtStat);
       if (s==null)
        s="**PROD. NO ENCONTRADO";
       v.addElement(s);
       v.addElement(dtCon1.getString("acl_numcaj"));
       v.addElement(dtCon1.getString("acl_canti"));
       v.addElement(dtCon1.getString("acl_prcom"));
       v.addElement("" + (Formatear.Redondea (dtCon1.getDouble("acl_prcom")*dtCon1.getDouble("acl_canti"),2)));
       jtLin.addLinea(v);
     } while (dtCon1.next());
     jtLin.requestFocus(0,0);
   }
   catch (Exception k)
   {
     Error("Error al buscar datos de Compras", k);
   }
 }

 void actAcum()
 {
   String s;
   int nRow=jt.getRowCount();
   int unid=0,nAlb=0;
   double kilos=0,importe=0,impPort=0;
   for (int n=0;n<nRow;n++)
   {
     if (! jt.getValBoolean(n,6))
       continue;
     nAlb++;
     unid+=jt.getValorDec(n,3);
     kilos+=jt.getValorDec(n,4);
     importe+=jt.getValorDec(n,5);
     impPort+=jt.getValorDec(n,8);
   }
   if (kilFactE.getValorDec()==kilosE.getValorDec())
     kilFactE.setValorDec(0);

   numAlbE.setValorDec(nAlb);
   numUniE.setValorDec(unid);
   kilosE.setValorDec(kilos);
   importeE.setValorDec(importe);
   impPorteE.setValorDec(impPort);
   if (kilFactE.getValorDec()==0)
     kilFactE.setValorDec(kilos);
   actImpPortes(kilFactE.getValorDec());

 }

 void actImpPortes(double kilos)
 {
   String s;
   impKilE.setValorDec(0);
   if (true)
     return;
  /* try
   {
     s = "SELECT  * FROM taripor WHERE " +
         " tap_fecini <= to_date('" + feciniE.getText() + "','dd-MM-yyyy')" +
         " and tap_fecfin >= to_date('" + fecfinE.getText() +
         "','dd-MM-yyyy')" +
         " and tap_kilos >= " + kilos +
         " order by tap_kilos";
     if (!dtStat.select(s))
     {
       s = "SELECT  * FROM taripor WHERE " +
           " tap_fecini <= to_date('" + feciniE.getText() + "','dd-MM-yyyy')" +
           " and tap_fecfin >= to_date('" + fecfinE.getText() +
           "','dd-MM-yyyy')" +
           " and tap_kilos = 0";
       if (!dtStat.select(s))
         return;
     }
     if (dtStat.getString("tap_fijkil").equals("K"))
       impKilE.setValorDec(dtStat.getDouble("tap_impor"));
     else
       impKilE.setValorDec(dtStat.getDouble("tap_impor") / kilos);
   }
   catch (Exception k)
   {
     Error("Error al buscar datos de Compras", k);
   }
*/
 }
 void imprimir()
 {
   try {
     rs = ct.createStatement().executeQuery(dtCon1.getStrSelect());
     JasperReport jr;
     jr =  gnu.chu.print.util.getJasperReport(EU, "calimppor");
     java.util.HashMap mp = new java.util.HashMap();

     mp.put("feciniE",feciniE.getText());
     mp.put("fecfinE",fecfinE.getText());
     mp.put("impKilE",new Double(impKilE.getValorDec()));
     mp.put("kilfacE",new Double(kilFactE.getValorDec()));
     JasperPrint jp = JasperFillManager.fillReport(jr, mp, this);
     gnu.chu.print.util.printJasper(jp, EU);
     mensaje("");
     mensajeErr("Listado ... GENERADO");
     nRow=-1;
   } catch (Exception k)
   {
     Error("Error al Generar Listado",k);
   }
 }
 public boolean next() throws JRException
 {
   for (nRow++;nRow<jt.getRowCount();nRow++)
   {
     if (jt.getValBoolean(nRow,6) || !opSelec.isSelected())
       return true;
   }
   return false;
  }
  public Object getFieldValue(JRField jRField) throws JRException
  {
    try
    {
      String campo = jRField.getName();
      if (campo.equals("prv_codi"))
        return new Integer(jt.getValInt(nRow,0));
      if (campo.equals("prv_nomb"))
        return jt.getValString(nRow,1);
      if (campo.equals("acc_fecrec"))
        return Formatear.getDate(jt.getValString(nRow,2),"dd-MM-yyyy");
      if (campo.equals("acl_numcaj"))
        return new Integer(jt.getValInt(nRow,3));

      if (campo.equals("acl_numcaj"))
        return new Integer(jt.getValInt(nRow,3));
      if (campo.equals("acl_canti"))
        return new Double(jt.getValorDec(nRow,4));
      if (campo.equals("acc_impor"))
        return new Double(jt.getValorDec(nRow,5));
      if (campo.equals("acc_impokg"))
        return new Double(jt.getValorDec(nRow,7));
      if (campo.equals("acc_imppor"))
        return new Double(jt.getValorDec(nRow,8));
      if (campo.equals("acc_serie"))
        return jt.getValString(nRow,9);
      if (campo.equals("acc_nume"))
        return new Integer(jt.getValInt(nRow,10));
      throw new JRException("Campo: "+campo+" No encontrado");
    }
    catch (Exception k)
    {
      ErrorInit(k);
      throw new JRException(k);
    }
  }

}
