package gnu.chu.anjelica.tesoreria;

/**
 * <p>Titulo: pdpagreal </p>
 * <p>Descripción: Consulta/Listado de  Pagos realizados</p>
 * <p>Copyright: Copyright (c) 2005-2010
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

import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import java.sql.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import gnu.chu.camposdb.*;
import gnu.chu.Menu.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.*;


public  class clpagreal extends ventana
{
  String s;
  CButton  Bimpri   = new CButton(Iconos.getImageIcon("print"));
  CPanel Pprinc = new CPanel();
  CLabel cLabel1 = new CLabel();
  CTextField lbp_fepainE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CTextField emp_codiE = new CTextField(Types.DECIMAL,"#9");
  CComboBox lbp_tippagE = new CComboBox();
  CLabel cLabel2 = new CLabel();
  CButton Bcondpag = new CButton();
  CPanel Pcondef = new CPanel();
  CLabel cLabel14 = new CLabel();
  CLabel cLabel3 = new CLabel();
  CLinkBox bat_codiniE = new CLinkBox();
  CLabel cLabel4 = new CLabel();
  CLinkBox bat_codfinE = new CLinkBox();
  CLabel cLabel5 = new CLabel();
  CTextField lbp_fepafiE = new CTextField(Types.DATE, "dd-MM-yyyy");
  prvPanel prv_codiniE = new prvPanel();
  CComboBox lbv_origeE = new CComboBox();
  CLabel cLabel6 = new CLabel();
  CLabel cLabel7 = new CLabel();
  prvPanel prv_codfinE = new prvPanel();
  traPanel tra_codiniE = new traPanel();
  traPanel tra_codfinE = new traPanel();
  Cgrid jt;
  CButton Baceptar = new CButton("Aceptar",Iconos.getImageIcon("buscar"));
  CPanel Presumen = new CPanel();
  CLabel cLabel8 = new CLabel();
  CTextField lbv_imppagE = new CTextField(Types.DECIMAL, "----,--9.99");
  CLabel cLabel9 = new CLabel();
  CTextField numpagosE = new CTextField(Types.DECIMAL,"###9");
  GridBagLayout gridBagLayout1 = new GridBagLayout();


  public clpagreal(EntornoUsuario eu, Principal p)
 {
   this(eu, p, new Hashtable());
 }

 public clpagreal(EntornoUsuario eu, Principal p, Hashtable ht)
 {
   EU = eu;
   vl = p.panel1;
   jf = p;
   eje = true;

   try
   {
//     if (ht != null)
//     {
//       if (ht.get("modPrecio") != null)
//         modPrecio = Boolean.valueOf(ht.get("modPrecio").toString()).
//             booleanValue();
//     }
     setTitulo("Cons/Listado Pagos Realizados");

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

 public clpagreal(gnu.chu.anjelica.menu p, EntornoUsuario eu)
 {
   EU = eu;
   vl = p.getLayeredPane();
   setTitulo("Cons/Listado Pagos Realizados");
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
   this.setSize(new Dimension(760, 505));
   this.setVersion("2005-17-13");

   Pprinc.setLayout(gridBagLayout1);
   statusBar = new StatusBar(this);
   this.getContentPane().add(Pprinc, BorderLayout.CENTER);
   this.getContentPane().add(statusBar, BorderLayout.SOUTH);
   statusBar.add(Bimpri, new GridBagConstraints(9, 0, 1, 2, 0.0, 0.0
                                                , GridBagConstraints.EAST,
                                                GridBagConstraints.VERTICAL,
                                                new Insets(0, 5, 0, 0), 0, 0));
   Bimpri.setPreferredSize(new Dimension(24, 24));
   Bimpri.setMaximumSize(new Dimension(24, 24));
   Bimpri.setMinimumSize(new Dimension(24, 24));
   Bimpri.setToolTipText("Imprimir Pagos Realizados");

   conecta();
   jt = confGrid();

   Pcondef.setMaximumSize(new Dimension(668, 87));
   Pcondef.setMinimumSize(new Dimension(668, 87));
   Pcondef.setOpaque(true);
   Pcondef.setPreferredSize(new Dimension(668, 87));
   jt.setMaximumSize(new Dimension(686, 236));
   jt.setMinimumSize(new Dimension(686, 236));
   jt.setPreferredSize(new Dimension(686, 236));
   Presumen.setMaximumSize(new Dimension(336, 25));
   Presumen.setMinimumSize(new Dimension(336, 25));
   Presumen.setPreferredSize(new Dimension(336, 25));

   cLabel1.setBounds(new Rectangle(5, 4, 57, 17));
   cLabel1.setText("De Banco");
   lbp_fepainE.setBounds(new Rectangle(303, 22, 72, 17));
   emp_codiE.setBounds(new Rectangle(580, 4, 26, 17));
   lbp_tippagE.setBounds(new Rectangle(68, 23, 99, 17));
   cLabel2.setBounds(new Rectangle(231, 22, 70, 17));
   cLabel2.setText("De Fec.Pag");
   Bcondpag.setBounds(new Rectangle(548, 6, 1, 1));
   Pcondef.setBorder(BorderFactory.createRaisedBevelBorder());
   Pcondef.setLayout(null);
   cLabel14.setText("Empresa");
   cLabel14.setBounds(new Rectangle(527, 4, 50, 17));
   cLabel3.setBounds(new Rectangle(9, 23, 61, 17));
   cLabel3.setText("Tipo Pago");
   bat_codiniE.setBounds(new Rectangle(68, 4, 184, 17));
   bat_codiniE.setAncTexto(30);
   cLabel4.setText("A Banco");
   cLabel4.setBounds(new Rectangle(267, 4, 50, 17));
   bat_codfinE.setAncTexto(30);
   bat_codfinE.setBounds(new Rectangle(320, 4, 184, 17));
   cLabel5.setText("A Fec.Pag");
   cLabel5.setBounds(new Rectangle(390, 22, 70, 17));
   lbp_fepafiE.setBounds(new Rectangle(462, 22, 72, 17));
   prv_codiniE.setBounds(new Rectangle(148, 43, 389, 18));
   tra_codiniE.setBounds(new Rectangle(148, 43, 389, 18));

   lbv_origeE.setBounds(new Rectangle(6, 43, 111, 18));
   cLabel6.setText("De");
   cLabel6.setBounds(new Rectangle(126, 43, 19, 17));
   cLabel7.setBounds(new Rectangle(127, 63, 19, 17));
   cLabel7.setText("A");
   prv_codfinE.setBounds(new Rectangle(148, 63, 389, 18));
   tra_codfinE.setBounds(new Rectangle(148, 63, 389, 18));
   Baceptar.setBounds(new Rectangle(545, 53, 117, 27));
   Baceptar.setMargin(new Insets(0,0,0,0));
   Presumen.setBorder(BorderFactory.createLoweredBevelBorder());
   Presumen.setLayout(null);
   cLabel8.setBounds(new Rectangle(154, 4, 94, 17));
   cLabel8.setText("Importe Pagado");
   cLabel8.setOpaque(true);
   lbv_imppagE.setBounds(new Rectangle(255, 4, 72, 17));
   lbv_imppagE.setEnabled(false);
   lbv_imppagE.setOpaque(true);
   cLabel9.setBounds(new Rectangle(6, 4, 90, 17));
   cLabel9.setText("Numero Pagos");
   numpagosE.setBounds(new Rectangle(100, 4, 47, 17));
   numpagosE.setEnabled(false);
   Pcondef.add(Bcondpag, null);
   Pprinc.add(Pcondef,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
   Pcondef.add(cLabel14, null);
   Pcondef.add(cLabel1, null);
   Pcondef.add(bat_codfinE, null);
   Pcondef.add(bat_codiniE, null);
   Pcondef.add(cLabel4, null);
   Pcondef.add(emp_codiE, null);
   Pcondef.add(lbp_tippagE, null);
   Pcondef.add(cLabel3, null);
   Pcondef.add(lbv_origeE, null);
   Pcondef.add(prv_codiniE, null);
   Pcondef.add(tra_codiniE, null);
   Pcondef.add(cLabel6, null);
   Pcondef.add(prv_codfinE, null);
   Pcondef.add(tra_codfinE, null);

   Pcondef.add(cLabel7, null);
   Pcondef.add(lbp_fepafiE, null);
   Pcondef.add(cLabel2, null);
   Pcondef.add(lbp_fepainE, null);
   Pcondef.add(cLabel5, null);
    Pcondef.add(Baceptar, null);
   Pprinc.add(jt,   new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
   Pprinc.add(Presumen,   new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
   Presumen.add(lbv_imppagE, null);
   Presumen.add(numpagosE, null);
   Presumen.add(cLabel9, null);
   Presumen.add(cLabel8, null);
   Presumen.setEnabled(false);
 }

 public void iniciarVentana() throws Exception
 {
   Pcondef.setDefButton(Baceptar);
   bat_codiniE.setFormato(Types.DECIMAL, "##9");
   bat_codfinE.setFormato(Types.DECIMAL, "##9");
   bat_codiniE.addDatos("-1", "***TODOS***");
   bat_codfinE.addDatos("-1", "***TODOS***");
   s = "SELECT bat_codi,bat_nomb FROM bancteso order by bat_nomb";
   dtStat.select(s);
   bat_codiniE.addDatos(dtStat, false);
   dtStat.first();
   bat_codfinE.addDatos(dtStat, false);

   lbp_tippagE.addItem("Extranjero", "E");
   lbp_tippagE.addItem("Pagare", "P");
   lbp_tippagE.addItem("Cheque", "C");
   lbp_tippagE.addItem("Recibo", "R");
   lbp_tippagE.addItem("Transferencia", "T");

   lbv_origeE.addItem("---------", "-");
   lbv_origeE.addItem("Proveedor", "C");
   lbv_origeE.addItem("Transport", "T");

   prv_codiniE.iniciar(dtStat, this, vl, EU);
   prv_codfinE.iniciar(dtStat, this, vl, EU);
   tra_codiniE.iniciar(dtStat, this, vl, EU);
   tra_codfinE.iniciar(dtStat, this, vl, EU);

   emp_codiE.setValorInt(EU.em_cod);
   lbp_fepafiE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
   lbp_fepainE.setText(Formatear.sumaDias(Formatear.getFechaAct("dd-MM-yyyy"),"dd-MM-yyyy",-7));
   activarEventos();
 }

  private Cgrid confGrid()
  {
    Cgrid jt = new Cgrid(12);

    Vector v = new Vector();
    v.addElement("Banco"); // 0
    v.addElement("Fec.Pago"); // 1
    v.addElement("T.Pago"); // 2 Tipo Pago
    v.addElement("Orig"); // 3 Origen: Proveedor o Transp
    v.addElement("Num.Vto."); // 4
    v.addElement("Proveedor/Transp."); // 5
    v.addElement("Imp.Pag"); // 6
    v.addElement("Banco/Talon"); // 7
    v.addElement("S/Fra."); // 8
    v.addElement("Imp.Vto."); // 9
    v.addElement("Fec.Vto"); // 10
    v.addElement("N.P"); // 11 Numero de Pago
    jt.setCabecera(v);
    jt.setAnchoColumna(new int[]{100,60,60,27,83,170,70,130,90,63,62,50});
    jt.setAlinearColumna(new int[]{0, 1, 0, 1,  0,  0, 2,  0, 0, 2, 1, 2});
    jt.setFormatoColumna(6, "---,--9.99");
    jt.setFormatoColumna(9, "---,--9.99");
    jt.setOrdenar(false);
    return jt;
  }

  private void activarEventos()
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
    Bimpri.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Bimpri_actionPerformed();
      }
    });

  }

  boolean checkCond()
  {
    if (emp_codiE.getValorInt() == 0)
    {
      mensajeErr("Introduzca una empresa");
      emp_codiE.requestFocus();
      return false;
    }
    if (lbp_fepainE.getError())
    {
      mensajeErr("Fecha Pago Inicial NO es valida");
      return false;
    }
    if (lbp_fepafiE.getError())
    {
      mensajeErr("Fecha Pago Final NO es valida");
      return false;
    }
    return true;
  }
  boolean buscaReg() throws SQLException
  {
    s = "SELECT c.*, d.*,l.lbv_nombre,l.lbv_pagado,l.lbv_impvto,"+
         " l.lbv_fecvto,bt.bat_nomb FROM libpagcab as c "+
         " left join bancteso as bt on  bt.bat_codi = c.bat_codi "+
         " , libpagdet as d  "+
         " left join librovto as l " +
         " on l.emp_codi = d.emp_codi "+
          " and l.lbv_orige = d.lbv_orige "+
          " and l.eje_nume = d.eje_nume "+
          " and l.lbv_numfra = d.lbv_numfra "+
          " and l.lbv_nume = d.lbv_nume "+
         " WHERE c.emp_codi = d.emp_codi " +
         " and c.lbp_nume = d.lbp_nume " +
         " and c.lbp_numlin = d.lbp_numlin "+
         " and c.emp_codi = "+emp_codiE.getValorInt();
     if (bat_codiniE.getValorInt()>0)
       s+=" AND c.bat_codi >= "+bat_codiniE.getValorInt();
     if (bat_codfinE.getValorInt()>0)
       s+=" AND c.bat_codi <= "+bat_codfinE.getValorInt();
     if (! lbp_fepainE.isNull() )
       s+=" AND lbp_fecpag >= to_date('"+lbp_fepainE.getText()+"','dd-MM-yyyy')";
     if (! lbp_fepafiE.isNull() )
       s+=" AND lbp_fecpag <= to_date('"+lbp_fepafiE.getText()+"','dd-MM-yyyy')";

     if (!lbv_origeE.getValor().equals("-"))
     {
       s+=" and d.lbv_orige = '"+lbv_origeE.getValor()+"'";
       if (lbv_origeE.getValor().equals("C"))
       {
         if (prv_codiniE.getValorInt()>0)
           s+=" and l.lbv_copvtr >= "+prv_codiniE.getValorInt();
         if (prv_codfinE.getValorInt()>0)
           s+=" and l.lbv_copvtr <= "+prv_codfinE.getValorInt();
       }
       else
       {
         if (tra_codiniE.getValorInt()>0)
           s+=" and l.lbv_copvtr >= "+tra_codiniE.getValorInt();
         if (tra_codfinE.getValorInt()>0)
           s+=" and l.lbv_copvtr <= "+tra_codfinE.getValorInt();
       }
     }
     s+=" order by c.lbp_nume,c.lbp_fecpag,c.bat_codi,c.lbp_numlin ";
     if (!dtCon1.select(s))
     {
       mensajeErr("NO encontrados Pagos con esas condiciones");
       return false;
     }
     return true;
  }
  void Baceptar_actionPerformed()
  {
    if (!checkCond())
      return;
     new clpagrealTH(this, 1);
  }
  void consPagosReal()
  {
    try {
      this.setEnabled(false);
      mensaje("A esperar .... estoy buscando datos");
      jt.removeAllDatos();
      if (! buscaReg())
      {
        this.setEnabled(true);
        mensaje("");
        return;
      }

      int lbpNume=dtCon1.getInt("lbp_nume");
      int numPagos=0;
      double impPagos=0;
      int numPagosT=0;
      double impPagosT=0;
      boolean swRoto=false;
      do
      {
        if (lbpNume!=dtCon1.getInt("lbp_nume"))
        {
          lbpNume=dtCon1.getInt("lbp_nume");
          swRoto=true;
          verAcumulado(numPagos,impPagos);
          numPagos=0;
          impPagos=0;

        }
        numPagos++;
        impPagos+=dtCon1.getDouble("lip_import");
        numPagosT++;
        impPagosT+=dtCon1.getDouble("lip_import");
        Vector v = new Vector();
        v.add(dtCon1.getString("bat_codi") + " " + dtCon1.getString("bat_nomb"));
        v.add(dtCon1.getFecha("lbp_fecpag", "dd-MM-yy"));
        v.add(lbp_tippagE.getText(dtCon1.getString("lbp_tippag")));
        v.add(dtCon1.getString("lbv_orige"));
        v.add(dtCon1.getString("eje_nume") + "-" + dtCon1.getString("lbv_numfra") +
              dtCon1.getString("lbv_nume"));
        v.add(dtCon1.getString("lbv_nombre"));
        v.add(dtCon1.getString("lip_import"));
        if (dtCon1.getInt("ban_codi", true) == 0)
          v.add(dtCon1.getString("lbp_numtal"));
        else
          v.add(Formatear.format(dtCon1.getInt("ban_codi"), "9999") +
                Formatear.format(dtCon1.getInt("lbp_baofic"), "9999") +
                Formatear.format(dtCon1.getInt("lbp_badico"), "99") +
                Formatear.format(dtCon1.getInt("lbp_bacuen"), "9999999999"));
        v.add(dtCon1.getString("lbp_facprv"));
        v.add(dtCon1.getString("lbv_impvto"));
        v.add(dtCon1.getFecha("lbv_fecvto", "dd-MM-yy"));
        v.add(dtCon1.getString("lbp_nume") + "/" + dtCon1.getString("lbp_numlin"));
        jt.addLinea(v);
      } while (dtCon1.next());
      if (swRoto)
        verAcumulado(numPagos,impPagos);
      jt.requestFocusInicio();
      numpagosE.setValorInt(numPagosT);
      lbv_imppagE.setValorDec(impPagosT);
      this.setEnabled(true);
      mensaje("");
      mensajeErr("Consulta ... realizada");
    } catch (Exception k)
    {
      Error("Error al buscar pagos realizados",k);
    }
  }
  void verAcumulado(int numPagos,double impPagos)
  {
    Vector v = new Vector();
    v.add("TOTAL PAGO ...");
    v.add("");
    v.add("");
    v.add("");
    v.add("" + numPagos);
    v.add("");
    v.add("" + impPagos);
    v.add("");
    v.add("");
    v.add("");
    v.add("");
    v.add("");
    jt.addLinea(v);
  }
  void setOrigen(String origen)
  {
    lbv_origeE.setValor(origen);
    prv_codiniE.setVisible(origen.equals("C"));
    tra_codiniE.setVisible(!origen.equals("C"));
    prv_codfinE.setVisible(origen.equals("C"));
    tra_codfinE.setVisible(!origen.equals("C"));
  }

  public void afterConecta() throws SQLException, java.text.ParseException
  {

  }

  void Bimpri_actionPerformed()
  {
    if (!checkCond())
      return;
    new clpagrealTH(this, 2);
  }

  void imprPagosReal()
 {
     try {
       this.setEnabled(false);
       mensaje("Generando listado ... Espere, por favor");

       if (!buscaReg())
       {
         mensaje("");
         mensajeErr("NO encontrados registros con estas condiciones");
         this.setEnabled(true);
         lbp_fepainE.requestFocus();
         return;
       }

       java.util.HashMap mp = new java.util.HashMap();
       mp.put("feinpa",lbp_fepainE.getText());
       mp.put("fefipa",lbp_fepafiE.getText());
       mp.put("lbv_origeP",lbv_origeE.getValor().equals("-")?null:lbv_origeE.getText());
       mp.put("lbv_copvinP",lbv_origeE.getValor().equals("-")?null:
              lbv_origeE.getValor().equals("C")?prv_codiniE.getText():tra_codiniE.getText());
       mp.put("lbv_copvfiP",lbv_origeE.getValor().equals("-")?null:
              lbv_origeE.getValor().equals("C")?prv_codfinE.getText():tra_codiniE.getText());

       mp.put("lbv_nombinP",lbv_origeE.getValor().equals("-")?null:
              lbv_origeE.getValor().equals("C")?prv_codiniE.getTextNomb():tra_codiniE.getTextNomb());
       mp.put("lbv_nombfiP",lbv_origeE.getValor().equals("-")?null:
              lbv_origeE.getValor().equals("C")?prv_codfinE.getTextNomb():tra_codfinE.getTextNomb());
       mp.put("bat_codiniP",bat_codiniE.getTextCombo());
       mp.put("bat_codfinP",bat_codfinE.getTextCombo());
       mp.put("lbp_tippagP",lbp_tippagE.getText());
       mp.put("empCodiP",emp_codiE.getText());
       JasperReport jr;
       jr = gnu.chu.print.util.getJasperReport(EU,"pagreali");
       ResultSet rs;
       rs=dtCon1.getStatement().executeQuery(dtCon1.parseaSql(s));
       JasperPrint jp = JasperFillManager.fillReport(jr, mp,new JRResultSetDataSource(rs));
       gnu.chu.print.util.printJasper(jp, EU);
       this.setEnabled(true);
       mensajeErr("Listado generado");
       mensaje("");
       lbp_fepainE.requestFocus();
     }
     catch (Exception k)
     {
       Error("Error al imprimir Libro vtos", k);
     }
   }
}

 class   clpagrealTH extends Thread
  {
    clpagreal padre ;
    int opcion;
    public clpagrealTH(clpagreal papa,int opcion)
    {
      padre=papa;
      this.opcion=opcion;
      this.start();
    }
    public void start()
    {
      if (opcion==1)
        padre.consPagosReal();
      if (opcion==2)
       padre.imprPagosReal();
    }
  }

