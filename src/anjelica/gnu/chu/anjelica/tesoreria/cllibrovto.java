package gnu.chu.anjelica.tesoreria;

/**
 * <p>Titulo: cllibrovto </p>
 * <p>Descripci�n: Consulta/Listado Libro de Vencimientos.
 * <p>Copyright: Copyright (c) 2005
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

import gnu.chu.utilidades.*;
import gnu.chu.controles.*;
import gnu.chu.Menu.*;
import java.awt.*;
import javax.swing.BorderFactory;
import gnu.chu.camposdb.*;
import java.sql.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.event.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.*;

public class cllibrovto extends ventana
{
  String s;
  CButton  Bimpri   = new CButton(Iconos.getImageIcon("print"));
  CPanel Pprinc = new CPanel();
  CPanel Pcabe = new CPanel();
  Cgrid jt = new Cgrid(13);
  CPanel Pacum = new CPanel();
  CLabel cLabel1 = new CLabel();
  CTextField fevtiniE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel2 = new CLabel();
  CTextField fevtfinE = new CTextField(Types.DATE, "dd-MM-yyyy");
  prvPanel prv_codiE = new prvPanel();
  traPanel tra_codiE = new traPanel();
  CComboBox lbv_origeE = new CComboBox();
  CLabel cLabel8 = new CLabel();
  CTextArea lbv_comenE = new CTextArea();
  CScrollPane lbv_comenS = new CScrollPane();
  CLabel cLabel3 = new CLabel();
  CTextField numfrasE = new CTextField(Types.DECIMAL,"###9");
  CLabel cLabel4 = new CLabel();
  CTextField impfrasE = new CTextField(Types.DECIMAL,"---,---,--9.99");
  CButton Baceptar = new CButton("Aceptar",Iconos.getImageIcon("check"));
  CComboBox lbv_pagadoE = new CComboBox();
  CLabel cLabel5 = new CLabel();
  CLabel cLabel6 = new CLabel();
  CTextField emp_codiniE = new CTextField(Types.DECIMAL,"#9");
  CLabel cLabel7 = new CLabel();
  CTextField emp_codfinE = new CTextField(Types.DECIMAL,"#9");
  CTextField imfrvtE = new CTextField(Types.DECIMAL,"---,---,--9.99");
  CLabel cLabel9 = new CLabel();
  CTextField nufrvtE = new CTextField(Types.DECIMAL,"###9");
  CLabel cLabel10 = new CLabel();
  CLabel cLabel11 = new CLabel();
  CTextField lbv_fecvtoE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel12 = new CLabel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();

 public cllibrovto(EntornoUsuario eu, Principal p)
 {
   EU = eu;
   vl = p.panel1;
   jf = p;
   eje = true;

   try
   {
     setTitulo("Cons./Listado Libro Vencimientos");

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


 public cllibrovto(gnu.chu.anjelica.menu p, EntornoUsuario eu)
 {
   EU = eu;
   vl = p.getLayeredPane();
   eje = false;

   try
   {
     setTitulo("Cons./Listado Libro Vencimientos");

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
   this.setSize(new Dimension(578, 497));
   this.setVersion("20050918");

   statusBar = new StatusBar(this);
   this.getContentPane().add(statusBar, BorderLayout.SOUTH);
   conecta();

   this.getContentPane().add(Pprinc, BorderLayout.CENTER);
   statusBar.add(Bimpri, new GridBagConstraints(9, 0, 1, 2, 0.0, 0.0
                                               , GridBagConstraints.EAST,
                                               GridBagConstraints.VERTICAL,
                                               new Insets(0, 5, 0, 0), 0, 0));

   Pcabe.setPreferredSize(new Dimension(519, 69));
   Pcabe.setMaximumSize(new Dimension(519, 69));
   Pcabe.setMinimumSize(new Dimension(519, 69));

   Pacum.setMaximumSize(new Dimension(511, 95));
   Pacum.setMinimumSize(new Dimension(511, 95));
   Pacum.setPreferredSize(new Dimension(511, 95));
   cLabel3.setBounds(new Rectangle(205, 73, 62, 17));
   cLabel12.setBounds(new Rectangle(123, 73, 72, 15));
   numfrasE.setBounds(new Rectangle(266, 73, 48, 17));
   impfrasE.setBounds(new Rectangle(407, 73, 96, 17));
   cLabel4.setBounds(new Rectangle(331, 73, 73, 18));
   lbv_fecvtoE.setBounds(new Rectangle(123, 53, 72, 17));
   cLabel11.setBounds(new Rectangle(59, 53, 66, 17));
   cLabel9.setBounds(new Rectangle(331, 53, 73, 18));
   nufrvtE.setBounds(new Rectangle(266, 53, 48, 17));
   cLabel10.setBounds(new Rectangle(205, 53, 58, 17));
   imfrvtE.setBounds(new Rectangle(407, 53, 96, 17));
   lbv_comenS.setBounds(new Rectangle(70, 6, 434, 45));
   cLabel8.setBounds(new Rectangle(2, 4, 79, 18));
   Baceptar.setBounds(new Rectangle(395, 43, 116, 24));
   lbv_pagadoE.setBounds(new Rectangle(395, 24, 115, 17));
   cLabel5.setBounds(new Rectangle(345, 24, 48, 17));
   emp_codfinE.setBounds(new Rectangle(243, 44, 37, 17));
   cLabel7.setBounds(new Rectangle(165, 44, 73, 17));
   emp_codiniE.setBounds(new Rectangle(81, 44, 37, 17));
   cLabel6.setBounds(new Rectangle(3, 44, 75, 17));
   fevtfinE.setBounds(new Rectangle(244, 25, 72, 17));
   cLabel2.setBounds(new Rectangle(161, 25, 84, 17));
   fevtiniE.setBounds(new Rectangle(80, 25, 72, 17));
   cLabel1.setBounds(new Rectangle(3, 25, 84, 17));
   tra_codiE.setBounds(new Rectangle(129, 4, 387, 18));
   prv_codiE.setBounds(new Rectangle(129, 4, 387, 18));
   lbv_origeE.setBounds(new Rectangle(4, 4, 111, 18));

   cLabel9.setText("Importe Fras");
   cLabel10.setText("Num.Fras");
   cLabel11.setText("Fecha Vto");
   cLabel12.setBackground(Color.red);
   cLabel12.setForeground(Color.white);
   cLabel12.setOpaque(true);
   cLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
   cLabel12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
   cLabel12.setText("TOTAL");

   Bimpri.setPreferredSize(new Dimension(24, 24));
   Bimpri.setMaximumSize(new Dimension(24, 24));
   Bimpri.setMinimumSize(new Dimension(24, 24));
   Bimpri.setToolTipText("Imprimir Libro Vtos");


   cLabel5.setText("Pagado");
   cLabel6.setText("De Empresa");
   cLabel7.setText("A Empresa");

   Pprinc.setLayout(gridBagLayout1);
   Pcabe.setBorder(BorderFactory.createRaisedBevelBorder());
   Pcabe.setLayout(null);
   Pacum.setBorder(BorderFactory.createLoweredBevelBorder());
   Pacum.setLayout(null);
   cLabel1.setText("De Fecha Vto");
   cLabel2.setText("A Fecha Vto");
   cLabel8.setText("Comentario");
   cLabel3.setText("Num.Fras");
   cLabel4.setText("Importe Fras");

   confGrid();
   Pprinc.add(Pcabe, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                                            new Insets(0, 0, 0, 0), 0, 0));
   Pprinc.add(jt, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
                                         , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0),
                                         0, 0));
   Pprinc.add(Pacum, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                                            new Insets(0, 0, 0, 0), 0, 0));
   Pacum.add(cLabel8, null);
   Pcabe.add(prv_codiE, null);
   Pcabe.add(tra_codiE, null);
   Pcabe.add(lbv_origeE, null);
   Pcabe.add(cLabel1, null);
   Pcabe.add(fevtfinE, null);
   Pcabe.add(cLabel2, null);
   Pcabe.add(fevtiniE, null);

   Pcabe.add(emp_codiniE, null);
   Pcabe.add(cLabel6, null);
   Pcabe.add(emp_codfinE, null);
   Pcabe.add(cLabel7, null);
   Pcabe.add(lbv_pagadoE, null);
   Pcabe.add(cLabel5, null);
   Pcabe.add(Baceptar, null);
   Pacum.add(lbv_comenS, null);
   Pacum.add(impfrasE, null);
   Pacum.add(imfrvtE, null);
   Pacum.add(cLabel9, null);
   Pacum.add(cLabel4, null);
   Pacum.add(numfrasE, null);
   Pacum.add(cLabel3, null);
   Pacum.add(nufrvtE, null);
   Pacum.add(cLabel10, null);
   Pacum.add(cLabel12, null);
   Pacum.add(lbv_fecvtoE, null);
   Pacum.add(cLabel11, null);
   lbv_comenS.getViewport().add(lbv_comenE, null);
 }
 public void iniciarVentana() throws Exception
 {
   Pcabe.setDefButton(Baceptar);
   Pacum.setEnabled(false);
   lbv_pagadoE.addItem("Pendiente","N");
   lbv_pagadoE.addItem("Pagado","S");
   lbv_pagadoE.addItem("Todo","T");
   lbv_origeE.addItem("-----","-");
   lbv_origeE.addItem("Proveedor","C");
   lbv_origeE.addItem("Transportista","T");
   fevtiniE.setText("01-01-"+Formatear.getFechaAct("yyyy"));
   fevtfinE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
   tra_codiE.setVisible(false);
   prv_codiE.iniciar(dtStat, this, vl, EU);
   tra_codiE.iniciar(dtStat, this, vl, EU);
   activarEventos();
 }
 void activarEventos()
 {

   lbv_origeE.addActionListener(new ActionListener()
   {
     public void actionPerformed(ActionEvent e)
     {
       setOrigen(lbv_origeE.getValor());
     }
   });

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
       if (e.getValueIsAdjusting() || !jt.isEnabled())
         return;
       try
       {
         verDatosLinea();
       }
       catch (Exception k)
       {
         Error("Error al ver Datos de linea", k);
       }
     }
   });
   Bimpri.addActionListener(new ActionListener()
   {
     public void actionPerformed(ActionEvent e)
     {
       Bimpri_actionPerformed();
     }
   });

 }

 void confGrid()
 {
   Vector v = new Vector();
   jt.setMaximumSize(new Dimension(513, 222));
   jt.setMinimumSize(new Dimension(513, 222));
   jt.setPreferredSize(new Dimension(513, 222));
   v.add("Or"); // 0
   v.add("Codigo"); // 1
   v.add("Nombre"); // 2
   v.add("Emp"); // 3
   v.add("Ejer"); // 4
   v.add("Fact."); // 5
   v.add("N�Vto"); // 6
   v.add("Fec.Fra"); // 7
   v.add("Fec.Vto"); // 8
   v.add("Importe"); // 9
   v.add("Pag"); // 10
   v.add("Imp.Pag."); // 11
   v.add("Comentario"); // 12
   jt.setCabecera(v);
   jt.setAnchoColumna(new int[]
                      {20, 52, 150, 30, 35, 43, 36, 62, 62, 80, 30, 80, 100});
   jt.setAlinearColumna(new int[]
                        {1, 2, 0, 2, 2, 2, 2, 1, 1, 2, 1, 2, 0});
   jt.setFormatoColumna(9, "----,--9.99");
   jt.setFormatoColumna(10, "BSN");
   jt.setFormatoColumna(11, "----,--9.99");
 }

 void Baceptar_actionPerformed()
 {
   if (! checkCabecera())
     return;
//   Formatear.verAncGrid(jt);
   new cllibrovtoTH(this,1);

 }

private boolean checkCabecera()
{
  if (fevtiniE.isNull() || fevtiniE.getError())
  {
    mensajeErr("Fecha Vencimiento Inicio NO es valida");
    fevtiniE.requestFocus();
    return false;
  }
  if (fevtfinE.isNull() || fevtfinE.getError())
  {
    mensajeErr("Fecha Vencimiento Final NO es valida");
    fevtfinE.requestFocus();
    return false;
  }
  return true;
}

 void consultaLibroVtos()
 {
   this.setEnabled(false);
   mensaje("Buscando datos solicitados .... ");
   String condWhere=getCondWhere(true);
    s="SELECT  * FROM librovto "+ condWhere+" ORDER by lbv_fecvto,lbv_orige,lbv_copvtr";
//    debug("s: "+s);
    try {
      Pacum.resetTexto();
      Double i=new Double("1");
      new Double(i.doubleValue());
      new java.util.Date(System.currentTimeMillis());
      jt.removeAllDatos();
      if (! dtCon1.select(s))
      {
        mensaje("");
        mensajeErr("NO encontrados registros con estas condiciones");
        this.setEnabled(true);
        fevtiniE.requestFocus();
        return;
      }
      jt.setEnabled(false);
      jt.setDatos(dtCon1);
      jt.requestFocus();
      s="SELECT SUM(lbv_impvto-lbv_imppag) as importe,count(*) as cuantos from librovto "+condWhere;
      dtStat.select(s);
      impfrasE.setValorDec(dtStat.getDouble("importe",true));
      numfrasE.setValorInt(dtStat.getInt("cuantos"));

      verDatosLinea();
      jt.setEnabled(true);
    } catch (Exception k)
    {
      Error("Error al buscar datos en Libro de Vto",k);
    }
    this.setEnabled(true);
    mensaje("");
    mensajeErr("Busqueda de datos ... FINALIZADA");
 }

private String getCondWhere(boolean incFecVto)
{
  if (incFecVto)
    s=" WHERE lbv_fecvto >= TO_DATE('"+fevtiniE.getText()+"','dd-MM-yyyy') "+
      " and lbv_fecvto <= TO_DATE('"+fevtfinE.getText()+"','dd-MM-yyyy') ";
  else
    s=" WHERE 1=1";
  if (! lbv_pagadoE.getValor().equals("T"))
    s+= " and lbv_pagado = '"+lbv_pagadoE.getValor()+"'";
  if (! lbv_origeE.getValor().equals("-"))
    s += " and lbv_orige = '" + lbv_origeE.getValor() + "'";
   if (lbv_origeE.getValor().equals("C"))
     s+=prv_codiE.getValorInt()==0?"":" and lbv_copvtr = "+prv_codiE.getValorInt();
   if (lbv_origeE.getValor().equals("T"))
     s+=tra_codiE.getValorInt()==0?"":" and lbv_copvtr = "+tra_codiE.getValorInt();
   s+=emp_codfinE.getValorInt()>0?" and emp_codi <= "+emp_codfinE.getValorInt():"";
   s+=emp_codiniE.getValorInt()>0?" and emp_codi >= "+emp_codiniE.getValorInt():"";
   return s;
}
 void verDatosLinea() throws Exception
 {
   s = "select lbv_comen from librovto WHERE lbv_orige = '" +jt.getValString(0) + "'" +
      " and emp_codi = " + jt.getValorInt(3) +
      " and eje_nume = " + jt.getValorInt(4) +
      " and lbv_numfra =  " + jt.getValorInt(5) +
      " and lbv_nume = " + jt.getValorInt(6);
   if (! dtStat.select(s))
   {
     lbv_comenE.resetTexto();
     return;
   }
   lbv_comenE.setText(dtStat.getString("lbv_comen"));
   if (! lbv_fecvtoE.getText().equals(jt.getValString(8)))
   {
     lbv_fecvtoE.setText(jt.getValString(8));
     s="SELECT SUM(lbv_impvto-lbv_imppag) as importe,count(*) as cuantos from librovto "+getCondWhere(false)+
         " and lbv_fecvto = TO_DATE('"+lbv_fecvtoE.getText()+"','dd-MM-yyyy')";
     dtStat.select(s);
     imfrvtE.setValorDec(dtStat.getDouble("importe",true));
     nufrvtE.setValorInt(dtStat.getInt("cuantos"));
   }
 }

 void Bimpri_actionPerformed()
 {
   if (!checkCabecera())
     return;
   new cllibrovtoTH(this, 2);
 }

 void imprLibroVtos()
{
    try {
      this.setEnabled(false);
      mensaje("Generando listado ... Espere, por favor");
      s = "SELECT  * FROM librovto " + getCondWhere(true) + " ORDER by lbv_fecvto,lbv_orige,lbv_copvtr";
      if (!dtCon1.select(s))
      {
        mensaje("");
        mensajeErr("NO encontrados registros con estas condiciones");
        this.setEnabled(true);
        fevtiniE.requestFocus();
        return;
      }

      java.util.HashMap mp = new java.util.HashMap();
      mp.put("feinvt",fevtiniE.getText());
      mp.put("fefivt",fevtfinE.getText());
      mp.put("lbv_origeP",lbv_origeE.getValor().equals("-")?null:lbv_origeE.getText());
      mp.put("lbv_copvtrP",lbv_origeE.getValor().equals("-")?null:
             lbv_origeE.getValor().equals("C")?prv_codiE.getText():tra_codiE.getText());
      mp.put("lbv_nombreP",lbv_origeE.getValor().equals("-")?null:
             lbv_origeE.getValor().equals("C")?prv_codiE.getTextNomb():tra_codiE.getTextNomb());
      mp.put("empiniP",emp_codiniE.getText());
      mp.put("empfinP",emp_codfinE.getText());
      mp.put("pagado",lbv_pagadoE.getText());
      JasperReport jr;
      jr = gnu.chu.print.util.getJasperReport(EU,"librovto");
      ResultSet rs;
      rs=dtCon1.getStatement().executeQuery(dtCon1.parseaSql(s));
      JasperPrint jp = JasperFillManager.fillReport(jr, mp,new JRResultSetDataSource(rs));
      gnu.chu.print.util.printJasper(jp, EU);
      this.setEnabled(true);
      mensajeErr("Listado generado");
      mensaje("");
      fevtiniE.requestFocus();
    }
    catch (Exception k)
    {
      Error("Error al imprimir Libro vtos", k);
    }
  }
  void setOrigen(String origen)
   {
     prv_codiE.setVisible(origen.equals("C"));
     tra_codiE.setVisible(!origen.equals("C"));
   }

}
 class   cllibrovtoTH extends Thread
 {
   cllibrovto padre ;
   int opcion;
   public cllibrovtoTH(cllibrovto papa,int opcion)
   {
     padre=papa;
     this.opcion=opcion;
     this.start();
   }
   public void start()
   {
     if (opcion==1)
       padre.consultaLibroVtos();
     if (opcion==2)
      padre.imprLibroVtos();
   }
 }


