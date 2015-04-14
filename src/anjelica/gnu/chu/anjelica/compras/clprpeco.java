package gnu.chu.anjelica.compras;

import gnu.chu.Menu.*;
import gnu.chu.anjelica.almacen.pdalmace;
import gnu.chu.camposdb.*;
import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.BorderFactory;
import net.sf.jasperreports.engine.*;
/**
*  Consulta Productos de Pedidos de compras
 *
 * Permite consultar las compras realizadas de un producto
 * dentro de unas fechas.
 *
 * Tambien permite delimitar por proveedor y por albaran.
 *  <p>Copyright: Copyright (c) 2005-2010
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
 * @version 1.0
 */

public class clprpeco extends ventana implements  JRDataSource
{
  String s;
  ResultSet rs;
  boolean verPrecio=false;
  CPanel Pprinc = new CPanel();
  CLinkBox cam_codiE = new CLinkBox();
  CTextField feeninE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel8 = new CLabel();
  CLabel cLabel18 = new CLabel();
  CLabel cLabel1 = new CLabel();
  CLabel cLabel4 = new CLabel();
  CPanel PtipoCons = new CPanel();
  CLabel cLabel2 = new CLabel();
  proPanel proiniE = new proPanel();
  CLabel cLabel3 = new CLabel();
  CLabel cLabel10 = new CLabel();
  proPanel profinE = new proPanel();
  CButton Baceptar = new CButton("F4 Aceptar", Iconos.getImageIcon("check"));
  CLinkBox alm_codiE = new CLinkBox();
  CLabel cLabel9 = new CLabel();
  CLinkBox tla_codiE = new CLinkBox();
  CLinkBox emp_codiE = new CLinkBox();
  Cgrid jt = new Cgrid(7);
  CCheckBox opDesgl = new CCheckBox();
  CButton Bimpri=new CButton(Iconos.getImageIcon("print"));
  CCheckBox opPedPend = new CCheckBox();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CTextField feenfiE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel11 = new CLabel();

  public clprpeco(EntornoUsuario eu, Principal p)
 {
   this(eu,p,null);
 }

 public clprpeco(EntornoUsuario eu, Principal p, Hashtable ht)
 {
   EU = eu;
   vl = p.panel1;
   jf = p;
   eje = true;

   try
   {
     if (ht != null)
     {

       if (ht.get("verPrecio") != null)
         verPrecio = Boolean.valueOf(ht.get("verPrecio").toString()).
             booleanValue();

     }
     setTitulo("Cons/List. Productos Pedidos Compras");
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

 public clprpeco(gnu.chu.anjelica.menu p, EntornoUsuario eu)
 {
   this(p, eu, null);
 }

 public clprpeco(gnu.chu.anjelica.menu p, EntornoUsuario eu, Hashtable ht)
 {
   EU = eu;
   vl = p.getLayeredPane();
   eje = false;

   try
   {
     if (ht != null)
     {
       if (ht.get("verPrecio") != null)
         verPrecio = Boolean.valueOf(ht.get("verPrecio").toString()).
             booleanValue();

     }
     setTitulo("Consulta Pedidos Compras");

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
   this.setSize(new Dimension(751, 510));
   this.setVersion(" (2010-06-04)" + (verPrecio ? "- Ver Precios" : ""));
   statusBar = new StatusBar(this);

   opPedPend.setToolTipText("Ver SOLO Pedidos Pedidos Pendientes");
    opPedPend.setVerifyInputWhenFocusTarget(true);
    opPedPend.setSelected(true);
    opPedPend.setText("Ped.Pendientes");
    opPedPend.setBounds(new Rectangle(373, 40, 118, 17));
    cLabel8.setToolTipText("");
    PtipoCons.setOpaque(true);
    cLabel9.setToolTipText("");
    feenfiE.setBounds(new Rectangle(279, 40, 73, 17));
    feenfiE.setFormato("dd-MM-yyyy");
    cLabel11.setToolTipText("");
    cLabel11.setBounds(new Rectangle(190, 40, 88, 17));
    cLabel11.setText("A Fec.Entrega");
    cLabel11.setRequestFocusEnabled(true);
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
   this.getContentPane().add(statusBar, BorderLayout.SOUTH);
   conecta();

   Bimpri.setToolTipText("Imprimir Resultados de Consulta");
   Bimpri.setPreferredSize(new Dimension(24, 24));
   Bimpri.setMinimumSize(new Dimension(24, 24));
   Bimpri.setMaximumSize(new Dimension(24, 24));
   statusBar.add(Bimpri, new GridBagConstraints(9, 0, 1, 2, 0.0, 0.0
                                                , GridBagConstraints.EAST,
                                                GridBagConstraints.VERTICAL,
                                                new Insets(0, 5, 0, 0), 0, 0));
   cLabel9.setRequestFocusEnabled(true);
   opDesgl.setToolTipText("Desglosar Lineas de pedidos");
   opDesgl.setSelected(true);
   opDesgl.setText("Desglosar ");
   opDesgl.setBounds(new Rectangle(500, 40, 92, 17));
   cam_codiE.setToolTipText("Desglosar Lineas de pedidos");

   Pprinc.setLayout(gridBagLayout1);

   confGrid();
   cam_codiE.setAncTexto(25);
   cam_codiE.setBounds(new Rectangle(513, 3, 208, 17));
   feeninE.setFormato("dd-MM-yyyy");
   feeninE.setBounds(new Rectangle(93, 40, 73, 17));
   cLabel8.setBounds(new Rectangle(273, 20, 44, 17));
   cLabel8.setText("A Prod");
   cLabel18.setRequestFocusEnabled(true);
   cLabel18.setText("Cámara");
   cLabel18.setBounds(new Rectangle(465, 1, 48, 17));
   cLabel1.setBounds(new Rectangle(3, 3, 75, 17));
   cLabel1.setText("Tipo Listado");
   cLabel4.setBounds(new Rectangle(2, 21, 53, 17));
   cLabel4.setText("De Prod");
   PtipoCons.setMaximumSize(new Dimension(728, 65));
   PtipoCons.setMinimumSize(new Dimension(728, 65));
   PtipoCons.setPreferredSize(new Dimension(728, 65));
   PtipoCons.setBorder(BorderFactory.createRaisedBevelBorder());
   PtipoCons.setLayout(null);
   proiniE.setBounds(new Rectangle(49, 21, 224, 17));
   cLabel3.setText("Almacen");
   cLabel3.setBounds(new Rectangle(231, 3, 53, 17));
   cLabel10.setText("Empr.");
   cLabel10.setBounds(new Rectangle(538, 20, 39, 17));
   profinE.setBounds(new Rectangle(311, 20, 224, 17));
   Baceptar.setBounds(new Rectangle(608, 38, 113, 22));
   Baceptar.setMargin(new Insets(0, 0, 0, 0));
   alm_codiE.setAncTexto(30);
   alm_codiE.setBounds(new Rectangle(282, 3, 164, 17));
   cLabel9.setText("De Fec.Entrega");
   cLabel9.setBounds(new Rectangle(4, 40, 88, 17));
   tla_codiE.setBounds(new Rectangle(75, 3, 156, 17));
   tla_codiE.setAncTexto(20);
   emp_codiE.setBounds(new Rectangle(573, 20, 151, 17));
   emp_codiE.setAncTexto(30);
   PtipoCons.add(cLabel2, null);
   PtipoCons.add(tla_codiE, null);
   PtipoCons.add(cLabel1, null);
    PtipoCons.add(alm_codiE, null);
    PtipoCons.add(cLabel3, null);
    PtipoCons.add(cLabel4, null);
    PtipoCons.add(feeninE, null);
    PtipoCons.add(cLabel9, null);
    Pprinc.add(PtipoCons,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    Pprinc.add(jt,   new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    PtipoCons.add(opDesgl, null);
    PtipoCons.add(opPedPend, null);
    PtipoCons.add(proiniE, null);
    PtipoCons.add(cLabel8, null);
    PtipoCons.add(profinE, null);
    PtipoCons.add(emp_codiE, null);
    PtipoCons.add(cLabel10, null);
    PtipoCons.add(cam_codiE, null);
    PtipoCons.add(cLabel18, null);
    PtipoCons.add(feenfiE, null);
    PtipoCons.add(cLabel11, null);
    PtipoCons.add(Baceptar, null);
 }

 public void iniciarVentana() throws Exception
 {
   s = "SELECT emp_codi,emp_nomb FROM v_empresa ORDER BY emp_nomb";
   dtStat.select(s);
   emp_codiE.addDatos(dtStat);
   emp_codiE.setFormato(true);
   emp_codiE.setFormato(Types.DECIMAL, "#9", 2);
   emp_codiE.setAceptaNulo(false);

   tla_codiE.setFormato(Types.DECIMAL, "#9");
   Pprinc.setButton(KeyEvent.VK_F4, Baceptar);
   int tlaCodi = 0;
   s = "SELECT tla_codi,tla_nomb FROM tilialca order by tla_codi";
   if (dtStat.select(s))
     tlaCodi = dtStat.getInt("tla_codi");
   tla_codiE.addDatos(dtStat);
   tla_codiE.addDatos("99", "Definido Usuario");
   tla_codiE.setValorInt(tlaCodi);
   alm_codiE.setFormato(true);
   alm_codiE.setFormato(Types.DECIMAL, "#9", 2);
   pdalmace.llenaLinkBox(alm_codiE, dtCon1,'*');
//   s = "SELECT alm_codi,alm_nomb FROM v_almacen " +
//       " ORDER BY alm_codi";
//   dtStat.select(s);
//   alm_codiE.addDatos(dtStat);
   alm_codiE.setText("0");

   cam_codiE.setFormato(Types.CHAR, "XX");
   cam_codiE.texto.setMayusc(true);

   gnu.chu.anjelica.pad.pdconfig.llenaDiscr(dtStat, cam_codiE, "AC", EU.em_cod);


   proiniE.iniciar(dtStat, this, vl, EU);
   profinE.iniciar(dtStat, this, vl, EU);

   feeninE.setText(Formatear.sumaDias(Formatear.getDateAct(),-10));
   feenfiE.setText(Formatear.sumaDias(Formatear.getDateAct(),30));
   emp_codiE.setValorInt(EU.em_cod);
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
   Bimpri.addActionListener(new ActionListener()
  {
    public void actionPerformed(ActionEvent e)
    {
      Bimpri_actionPerformed();
    }
  });

 }

 String getStrSql(int tlaCodi)
 {
   String proDisc3 = Formatear.reemplazar(cam_codiE.getText(), "*", "%").trim();
   if (proDisc3.equals("%%") || proDisc3.equals("%") || proDisc3.equals(""))
     proDisc3 = null;

   s = "SELECT c.*,l.*,ar.pro_nomb as nombart," +
       (tla_codiE.getValorInt() == 99 ? "" : " gp.pro_desc, ") +
       " pv.prv_nomco FROM  pedicoc as c,pedicol as l,v_proveedo as pv,v_articulo as ar " +
       (tla_codiE.getValorInt() == 99 ? "" :
        " , tilialgr as gp, tilialpr as pr ") +
       " WHERE c.emp_codi =  l.emp_codi " +
       " and c.eje_nume = l.eje_nume " +
       " and c.pcc_nume = l.pcc_nume " +
       " and c.prv_codi = pv.prv_codi " +
       " and l.pro_codi = ar.pro_codi " +
       (tlaCodi != 99 || proDisc3 == null ? "" : " and ar.cam_codi like '%" + proDisc3 + "%'") +
       (feeninE.isNull() ? "" :
        " and c.pcc_fecrec >= to_date('" + feeninE.getText() + "','dd-MM-yyyy')") +
       (feenfiE.isNull() ? "" :
        " and c.pcc_fecrec <= to_date('" + feenfiE.getText() + "','dd-MM-yyyy')") +
       (emp_codiE.getValorInt() == 0 ? "" : " and c.emp_codi = " + emp_codiE.getValorInt()) +
       (alm_codiE.getValorInt() == 0 ? "" : " and c.alm_codi = " + alm_codiE.getValorInt()) +
       (proiniE.isNull() || tlaCodi != 99 ? "" : " and l.pro_codi >= " + proiniE.getText()) +
       (profinE.isNull() || tlaCodi != 99 ? "" : " and l.pro_codi <= " + profinE.getText()) +
       (opPedPend.isSelected()?" and pcc_estrec = 'P'":"")+
       (tlaCodi == 99 ? "" : " and l.pro_codi = pr.pro_codi " +
        " and pr.tla_codi = " + tla_codiE.getValorInt() +
        " and pr.tla_orden = gp.tla_orden " +
        " and gp.tla_codi = " + tla_codiE.getValorInt()) +
       "  ORDER BY " +
       (tla_codiE.getValorInt() == 99 ? " l.pro_codi " : " gp.tla_orden ") +
       ", l.pcl_feccad,c.prv_codi ";
   return s;
 }

 void Baceptar_actionPerformed()
 {
   if (feeninE.getError() || feenfiE.getError())
     return;
   new miThread("")
   {
     public void run()
     {
       consultar();
     }
   };
 }

 void consultar()
 {
   this.setEnabled(false);
   mensaje("A esperar ... estoy generando el listado");

   int tlaCodi=tla_codiE.getValorInt();
   s=getStrSql(tlaCodi);
//   Formatear.verAncGrid(jt);
//   debug(s);
   jt.removeAllDatos();
   try {
     if (! dtCon1.select(s))
     {
       this.setEnabled(true);
       mensajeErr("No encontrados pedidos con estas condiciones");
       mensaje("");
       return;
     }
     String proNomb=dtCon1.getInt("pro_codi") + " -> " + dtCon1.getString("nombart");
     String proGrupo=tlaCodi==99?proNomb:dtCon1.getString("pro_desc");
     int nLiPr=0;
     double numCaj=0;
     double cantCaj=0;
     do
     {
       Vector v=new Vector();
       if (tlaCodi==99)
       {
            proNomb=dtCon1.getInt("pro_codi") + " -> " + dtCon1.getString("nombart");
            if (! proGrupo.equals(proNomb))
            {
              if (nLiPr>1)
                ponAcumul(numCaj,cantCaj,proGrupo);
              proGrupo=proNomb;
              numCaj=0;
              cantCaj=0;
              nLiPr=0;
            }
            nLiPr++;
            if (nLiPr==1)
              v.add(proNomb);
            else
              v.add("");
      }
       else
       {
         if (!proGrupo.equals(dtCon1.getString("pro_desc")))
         {
           if (nLiPr>1)
             ponAcumul(numCaj, cantCaj,proGrupo);
           proGrupo = dtCon1.getString("pro_desc");
           numCaj = 0;
           cantCaj = 0;
           nLiPr=0;
         }
         nLiPr++;
         if (nLiPr==1)
           v.add(dtCon1.getString("pro_desc"));
         else
           v.add("");
       }
       new Integer(1).intValue();
       v.add(dtCon1.getString("prv_nomco")); // 1
      v.add(dtCon1.getFecha("pcl_feccad","dd-MM-yyyy")); // 2
      if (dtCon1.getString("pcc_estad").equals("P"))
      {
        v.add(dtCon1.getString("pcl_nucape")); // 3
        v.add(dtCon1.getString("pcl_cantpe")); // 4
        if (!verPrecio)
          v.add("");
        else
          v.add(""+(dtCon1.getDouble("pcl_precpe")+(dtCon1.getString("pcc_portes").equals("D")?dtCon1.getDouble("pcc_imppor"):0)) ); // 5
        numCaj+=dtCon1.getDouble("pcl_nucape");
        cantCaj+=dtCon1.getDouble("pcl_cantpe");
        v.add("Pend.Conf");
      } else if (dtCon1.getString("pcc_estad").equals("C"))
      {
        v.add(dtCon1.getString("pcl_nucaco")); // 3
        v.add(dtCon1.getString("pcl_cantco")); // 4
        if (!verPrecio)
            v.add("");
          else
        v.add(""+(dtCon1.getDouble("pcl_precco")+(dtCon1.getString("pcc_portes").equals("D")?dtCon1.getDouble("pcc_imppor"):0)) ); // 5
        numCaj+=dtCon1.getDouble("pcl_nucaco");
        cantCaj+=dtCon1.getDouble("pcl_cantco");
        v.add("Confir");
      } else
      {
        v.add(dtCon1.getString("pcl_nucafa")); // 3
        v.add(dtCon1.getString("pcl_cantfa")); // 4
        if (!verPrecio)
          v.add("");
        else
          v.add("" +(dtCon1.getDouble("pcl_precfa") +
                 (dtCon1.getString("pcc_portes").equals("D") ? dtCon1.getDouble("pcc_imppor") : 0))); // 5
        numCaj+=dtCon1.getDouble("pcl_nucafa");
        cantCaj+=dtCon1.getDouble("pcl_cantfa");
        v.add("Prefact");
      }
      if (opDesgl.isSelected())
        jt.addLinea(v);
     } while (dtCon1.next());
     ponAcumul(numCaj, cantCaj,proGrupo);
     this.setEnabled(true);
     mensajeErr("COnsulta ... Realizada");
     mensaje("");
   } catch (SQLException k)
   {
     Error("Error al buscar pedidos Compras",k);
   }
 }

 private void ponAcumul(double numCaj, double cantCaj,String proNomb)
 {
   Vector v = new Vector();
   if (opDesgl.isSelected())
     v.add("Total Producto ...");
   else
     v.add(proNomb);
   v.add(""); // Proveedor
   v.add(""); // Fec. Cad
   v.add("" + numCaj);
   v.add("" + cantCaj);
   v.add("");
   v.add("");
   jt.addLinea(v);
 }
 private void confGrid() throws Exception
 {
   Vector v=new Vector();
   jt.setMaximumSize(new Dimension(705, 359));
    jt.setMinimumSize(new Dimension(705, 359));
    jt.setPreferredSize(new Dimension(705, 359));
    v.add("Producto"); // 0
   v.add("Prov"); // 1
   v.add("Fec.Cad"); // 2
   v.add("Unid"); // 3
   v.add("Kilos"); // 4
   v.add("Precio"); // 5
   v.add("Estad"); // 6
   jt.setCabecera(v);

   jt.setAnchoColumna(new int[]{130,130,80,45,55,45,50});
   jt.setAlinearColumna(new int[]{0,0,1,2,2,2,1});
   jt.setAjustarGrid(true);
   jt.setFormatoColumna(3,"----9");
   jt.setFormatoColumna(4,"----,--9.9");
   jt.setFormatoColumna(5,"----9.99");
   jt.setOrdenar(false);
 }
 void Bimpri_actionPerformed()
   {
     if (feeninE.getError() || feenfiE.getError())
       return;
     new miThread("")
     {
       public void run()
       {
         listar();
       }
     };
   }

   void listar()
   {
     this.setEnabled(false);
     mensaje("A esperar ... estoy generando el listado");
     try {
       java.util.HashMap mp = new java.util.HashMap();
       JasperReport jr;
       mp.put("opDesgl",new Boolean(opDesgl.isSelected()));
       mp.put("fecEntr",feeninE.getDate());
       mp.put("tla_nomb",tla_codiE.getTextCombo());
       mp.put("pro_codi",new Integer(proiniE.getValorInt()));
       mp.put("pro_nomb",proiniE.getTextNomb());
       mp.put("pro_codi1",new Integer(profinE.getValorInt()));
       mp.put("pro_nomb1",profinE.getTextNomb());
       mp.put("alm_codi",alm_codiE.getValorInt()+" -> "+alm_codiE.getTextCombo());
       mp.put("cam_codi",cam_codiE.isNull()?"":cam_codiE.getText()+" -> "+cam_codiE.getTextCombo());
       jr = gnu.chu.print.util.getJasperReport(EU, "pedcompr");
       s=dtCon1.parseaSql(getStrSql(tla_codiE.getValorInt()));
//      debug(s);
       Statement st= ct.createStatement();
       rs=st.executeQuery(s);

       JasperPrint jp = JasperFillManager.fillReport(jr, mp,this);
       gnu.chu.print.util.printJasper(jp, EU);
     
       mensaje("");
       mensajeErr("Listado ... generado");
       this.setEnabled(true);
       st.close();
     }
     catch (Exception k)
     {
       Error("Error al imprimir consulta", k);
     }
   }
   public boolean next() throws JRException
    {
      try {
        return rs.next();
      } catch (SQLException k)
      {
        throw new JRException(k);
      }
    }

    public Object getFieldValue(JRField f) throws JRException
    {
      try
      {
        String campo = f.getName().toLowerCase();
        if (campo.equals("pro_nomb"))
        {
          if (tla_codiE.getValorInt() == 99)
            return rs.getInt("pro_codi") + " " + rs.getString("nombart");
          else
            return rs.getString("pro_desc");
        }
        if (campo.equals("pcl_feccad"))
          return rs.getDate("pcl_feccad");
        if (campo.equals("prv_nomco"))
          return rs.getString("prv_nomco");
        if (campo.equals("pcl_numcaj"))
        {
          if (rs.getString("pcc_estad").equals("P"))
            return new Integer(rs.getInt("pcl_nucape"));
          else if (rs.getString("pcc_estad").equals("C"))
            return new Integer(rs.getInt("pcl_nucaco"));
          else
            return new Integer(rs.getInt("pcl_nucafa"));
        }
        if (campo.equals("pcl_canti"))
        {
          if (rs.getString("pcc_estad").equals("P"))
            return new Double(rs.getDouble("pcl_cantpe"));
          else if (rs.getString("pcc_estad").equals("C"))
            return new Double(rs.getDouble("pcl_cantco"));
          else
            return new Double(rs.getDouble("pcl_cantfa"));
        }
        if (campo.equals("pcl_precio"))
        {
          if (!verPrecio)
            return new Double(0);
          if (rs.getString("pcc_estad").equals("P"))
            return new Double(rs.getDouble("pcl_precpe")+(rs.getString("pcc_portes").equals("D")?rs.getDouble("pcc_imppor"):0));
          else if (rs.getString("pcc_estad").equals("C"))
            return new Double(rs.getDouble("pcl_precco")+(rs.getString("pcc_portes").equals("D")?rs.getDouble("pcc_imppor"):0));
          else
            return new Double(rs.getDouble("pcl_precfa")+(rs.getString("pcc_portes").equals("D")?rs.getDouble("pcc_imppor"):0));
//            return new Double(rs.getDouble("pcl_precfa"));
        }
        if (campo.equals("pcc_estad"))
        {
          if (rs.getString("pcc_estad").equals("P"))
            return "PEND.";
          else if (rs.getString("pcc_estad").equals("C"))
            return "CONF";
          else
            return "PREFACT.";
        }
        throw new JRException("Campo: " + campo + " NO encontrado");
      }
      catch (SQLException k)
      {
        throw new JRException(k);
      }

    }
}
