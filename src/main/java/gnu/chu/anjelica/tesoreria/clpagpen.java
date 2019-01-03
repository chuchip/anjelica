package gnu.chu.anjelica.tesoreria;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import gnu.chu.Menu.*;
import gnu.chu.camposdb.*;
import java.sql.*;

/**
* <p>Titulo:   clpagpen </p>
 * <p>Descripción: Cons./Listado de Pagos Pendientes a Proveedores.
 * <p>Copyright: Copyright (c) 2005-2009
*  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
*  los términos de la Licencia Pública General de GNU según es publicada por
*  la Free Software Foundation, bien de la versión 2 de dicha Licencia
*  o bien (según su elección) de cualquier versión posterior.
*  Este programa se distribuye con la esperanza de que sea útil,
*  pero SIN NINGUNA GARANTÍA, incluso sin la garantía MERCANTIL implícita
*  o sin garantizar la CONVENIENCIA PARA UN PROPÓSITO PARTICULAR.
*  Véase la Licencia Pública General de GNU para más detalles.
*  Debería haber recibido una copia de la Licencia Pública General junto con este programa.
*  Si no ha sido así, escriba a la Free Software Foundation, Inc.,
*  en 675 Mass Ave, Cambridge, MA 02139, EEUU.
* </p>
* <p>Empresa: MISL</p>
* @author chuchiP
* @version 1.0
*/

public class clpagpen extends ventana
{
  String s;
  CPanel Pprinc = new CPanel();
  CPanel Pcabe = new CPanel();
  CTextField feciniE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel cLabel1 = new CLabel();
  CLabel cLabel2 = new CLabel();
  CTextField fecfinE = new CTextField(Types.DATE,"dd-MM-yyyy");
  prvPanel prv_codiE = new prvPanel();
  CLabel cLabel4 = new CLabel();
  CLinkBox fpa_codiE = new CLinkBox();
  CButton Baceptar = new CButton("F4 Aceptar",Iconos.getImageIcon("check"));
  CLabel cLabel5 = new CLabel();
  CTextField fecvtoE = new CTextField(Types.DATE,"dd-MM-yyyy");
  Cgrid jt = new Cgrid(10);
  CComboBox lbv_origeE = new CComboBox();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CLinkBox tra_codiE = new CLinkBox();
  GridBagLayout gridBagLayout2 = new GridBagLayout();


  public clpagpen(EntornoUsuario eu, Principal p)
   {
     EU = eu;
     vl = p.panel1;
     jf = p;
     eje = true;

     setTitulo("Cons./Listado Pagos Pend. Realizar");

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

   public clpagpen(gnu.chu.anjelica.menu p, EntornoUsuario eu)
   {

     EU = eu;
     vl = p.getLayeredPane();
     setTitulo("Cons./Listado Pagos Pend. Realizar");
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
     iniciar(733, 521);
     this.setSize(new Dimension(733, 413));
     this.setVersion("20090817");
     statusBar = new StatusBar(this);
     conecta();
     tra_codiE.setAncTexto(40);
    tra_codiE.setBounds(new Rectangle(0, 0, 102, 17));
     prv_codiE.setAncTexto(50);
    prv_codiE.setBounds(new Rectangle(92, 24, 282, 18));
     Pprinc.setLayout(gridBagLayout2);
    fpa_codiE.setBounds(new Rectangle(74, 44, 296, 18));
    cLabel4.setBounds(new Rectangle(3, 44, 70, 18));
    Baceptar.setBounds(new Rectangle(376, 37, 104, 25));
    lbv_origeE.setBounds(new Rectangle(4, 23, 84, 20));
    fecvtoE.setBounds(new Rectangle(401, 4, 79, 17));
    cLabel5.setBounds(new Rectangle(303, 4, 99, 17));
    fecfinE.setBounds(new Rectangle(220, 3, 79, 17));
    cLabel2.setBounds(new Rectangle(159, 3, 63, 17));
    feciniE.setBounds(new Rectangle(74, 3, 79, 17));
    cLabel1.setBounds(new Rectangle(3, 3, 69, 17));
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);
     jt.setMaximumSize(new Dimension(484, 271));
     jt.setMinimumSize(new Dimension(484, 271));
     jt.setPreferredSize(new Dimension(484, 271));

     Vector v=new Vector();
     v.addElement("T.F"); // 0 Tipo Fra.
     v.addElement("Prv"); // 1 Prooveedor o Transp.
     v.addElement("Nombre  Prv"); //  2 Prooveedor o Transp.
     v.addElement("Factura"); // 3 Empresa / Ejerc./ Fra.
     v.addElement("Fec.Fra"); // 4 Fec.Fra
     v.addElement("N.Rec"); // 5 Numero de Recibo.
     v.addElement("Fec.Vto."); // 6 Fecha Recibo.
     v.addElement("Imp.Rec"); // 7 Imp. Recibo
     v.addElement("T.P"); // 8 Totalmente Pagado.
     v.addElement("Imp.Pag"); // 9 Imp.Pagado
     jt.setCabecera(v);
     jt.setAnchoColumna(new int[]{50,29,149,83,72,37,76,76,26,76});
     jt.setAlinearColumna(new int[]{1,2,0,0,1,2,1,2,1,2});
     jt.setFormatoColumna(7,"---,--9.99");
     jt.setFormatoColumna(8,"BSN");
     jt.setFormatoColumna(9,"---,--9.99");
     jt.resetRenderer(8);
     jt.setAjustarGrid(true);
     Pcabe.setBorder(BorderFactory.createRaisedBevelBorder());
     Pcabe.setLayout(null);
     cLabel1.setText("De Fec.Fra.");
     cLabel2.setText("A Fec.Fra");
     cLabel4.setText("Forma Pago");
     fpa_codiE.setAncTexto(30);
     Baceptar.setMargin(new Insets(0, 0, 0, 0));
     tra_codiE.setVisible(false);
     cLabel5.setText("Fec.Vto Inferior a");
     this.getContentPane().add(Pprinc, BorderLayout.CENTER);
     Pprinc.add(Pcabe,      new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
     Pcabe.add(cLabel1, null);
     Pcabe.add(feciniE, null);
     Pcabe.add(prv_codiE, null);
     Pcabe.add(tra_codiE, null);
     Pcabe.add(cLabel4, null);
     Pcabe.add(fpa_codiE, null);
     Pcabe.add(Baceptar, null);
    Pcabe.add(fecvtoE, null);
    Pcabe.add(cLabel2, null);
    Pcabe.add(fecfinE, null);
    Pcabe.add(cLabel5, null);
    Pcabe.add(lbv_origeE, null);
    Pprinc.add(jt,  new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    Pcabe.setMinimumSize(new Dimension(484, 67));
    Pcabe.setMaximumSize(new Dimension(484, 67));
    Pcabe.setPreferredSize(new Dimension(484, 67));
   }

   public void iniciarVentana() throws Exception
   {
     fpa_codiE.setCeroIsNull(true);
      prv_codiE.setVisible(false);
     lbv_origeE.addItem("TODO","*");
     lbv_origeE.addItem("Proveed.","C");
     lbv_origeE.addItem("Transp.","T");
     s = "SELECT tra_codi,tra_nomb FROM v_transport ORDER BY tra_nomb";
     dtStat.select(s);
     tra_codiE.addDatos(dtStat);
     tra_codiE.setFormato(Types.DECIMAL, "#9");

     s = "SELECT fpa_codi,fpa_nomb FROM v_forpago order by fpa_codi";
     dtStat.select(s);
     fpa_codiE.setFormato(Types.DECIMAL,"##9",3);
     fpa_codiE.addDatos(dtStat);
     prv_codiE.iniciar(dtStat, this, vl, EU);
     fecvtoE.setDate(Formatear.getDateAct());
     activarEventos();
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
     lbv_origeE.addActionListener(new ActionListener()
     {
       public void actionPerformed(ActionEvent e)
       {
         if (lbv_origeE.getValor().equals("P") && ! prv_codiE.isVisible())
         {
           prv_codiE.setVisible(true);
           tra_codiE.setVisible(false);
         }
         if (lbv_origeE.getValor().equals("T") && ! tra_codiE.isVisible())
         {
           tra_codiE.setVisible(true);
           prv_codiE.setVisible(false);
         }
         if (lbv_origeE.getValor().equals("*"))
         {
           tra_codiE.setVisible(false);
           prv_codiE.setVisible(false);
         }
       }
     });
   }

   void Baceptar_actionPerformed()
   {
     int prvCodi, fpaCodi;
     String prvNomb;

     if (feciniE.getError())
     {
       mensajeErr("Fecha de Inicio de Fra... NO VALIDA");
       feciniE.requestFocus();
       return;
     }
     if (fecfinE.getError())
     {
       mensajeErr("Fecha de Final de Fra... NO VALIDA");
       fecfinE.requestFocus();
       return;
     }
     if (fecvtoE.getError())
     {
       mensajeErr("Fecha de Vto. de Recibo... NO VALIDA");
       fecvtoE.requestFocus();
       return;
     }

//     Formatear.verAncGrid(jt);
//     System.out.println("Ancho:"+this.getSize().getWidth());
     jt.removeAllDatos();
     String condWhere;
     s = "select l.* from librovto as l";
     condWhere=" WHERE l.emp_codi = "+EU.em_cod;

     if (! feciniE.isNull())
       condWhere+=" and l.lbv_fecvto >= to_date('"+fecvtoE.getFecha()+"','dd-MM-yyyy')";



     if (lbv_origeE.getValor().equals("C") && prv_codiE.getValorInt() != 0)
     {
       condWhere += " and f.emp_codi = l.emp_codi " +
           " and f.eje_nume = l.eje_nume " +
           " and f.fcc_nume = l.lbv_numfra " +
           " and f.PRV_CODI = " + prv_codiE.getValorInt();
       s += " , v_facaco as f";
     }
     if (lbv_origeE.getValor().equals("T") && prv_codiE.getValorInt() != 0)
     {
       condWhere += " and f.emp_codi = l.emp_codi " +
           " and f.eje_nume = l.eje_nume " +
           " and f.frt_nume = l.lbv_numfra " +
           " and f.PRV_CODI = " + prv_codiE.getValorInt();
       s += " , fratraca as f";
     }
     if (! feciniE.isNull())
       condWhere+=" and l.lbv_fecfra >= to_date('"+feciniE.getText()+"','dd-MM-yyyy')";
     if (! fecfinE.isNull())
       condWhere+=" and l.lbv_fecfra <= to_date('"+fecfinE.getText()+"','dd-MM-yyyy')";

     s=s+condWhere+" ORDER BY l.lbv_orige,l.emp_codi,l.eje_nume,l.lbv_numfra";
     try
     {
       if (!dtCon1.select(s))
       {
         mensajeErr("NO Encontrados Registros con estos datos");
         return;
       }
       do
       {
         prvCodi=0;
         fpaCodi=0;
         prvNomb="";
         Vector v=new Vector();
         if (dtCon1.getString("lbv_orige").equals("C"))
         {
           s = "SELECT f.fpa_codi,p.prv_codi,p.prv_nomb FROM v_facaco as f,v_proveedo as p" +
               "  WHERE f.prv_codi = p.prv_codi " +
               " and f.emp_codi = " + dtCon1.getInt("emp_codi") +
               " and f.eje_nume = " + dtCon1.getInt("eje_nume") +
               " and f.fcc_nume = " + dtCon1.getInt("lbv_numfra");
           if (dtStat.select(s))
           {
             prvCodi = dtStat.getInt("prv_codi");
             prvNomb = dtStat.getString("prv_nomb");
             fpaCodi=dtStat.getInt("fpa_codi");
           }
         }
         if (dtCon1.getString("lbv_orige").equals("T"))
         {
           s = "SELECT f.fpa_codi,p.tra_codi,p.tra_nomb FROM fratraca as f,v_transport as p" +
               "  WHERE f.prv_codi = p.prv_codi " +
               " and f.emp_codi = " + dtCon1.getInt("emp_codi") +
               " and f.eje_nume = " + dtCon1.getInt("eje_nume") +
               " and f.frt_nume = " + dtCon1.getInt("lbv_numfra");
           if (dtStat.select(s))
           {
             prvCodi = dtStat.getInt("tra_codi");
             prvNomb = dtStat.getString("tra_nomb");
             fpaCodi=dtStat.getInt("fpa_codi");
           }
         }
         if (! fpa_codiE.isNull() && fpa_codiE.getValorInt()!=fpaCodi)
           continue;
         v.addElement( lbv_origeE.getText(dtCon1.getString("lbv_orige"))); //0
         v.addElement(""+prvCodi); //1
         v.addElement(prvNomb); // 2
         v.addElement(dtCon1.getString("emp_codi")+"-"+dtCon1.getString("eje_nume")+"/"+
                      dtCon1.getString("lbv_numfra")); // 3
         v.addElement(dtCon1.getFecha("lbv_fecfra","dd-MM-yyyy")); // 4
         v.addElement(dtCon1.getString("lbv_nume")); // 5
         v.addElement(dtCon1.getFecha("lbv_fecvto","dd-MM-yyyy")); // 6
         v.addElement(dtCon1.getString("lbv_impvto")); // 7
         v.addElement(dtCon1.getString("lbv_pagado")); // 8
         v.addElement(dtCon1.getString("lbv_imppag")); // 9
         jt.addLinea(v);
       } while (dtCon1.next());
       jt.requestFocusInicio();
     } catch (Exception k)
     {
       Error("Error al Buscar Datos",k);
     }
   }
 }
