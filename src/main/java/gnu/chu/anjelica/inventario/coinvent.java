package gnu.chu.anjelica.inventario;

import gnu.chu.utilidades.*;
import gnu.chu.Menu.*;
import gnu.chu.anjelica.almacen.pdalmace;
import gnu.chu.controles.*;
import java.awt.*;
import gnu.chu.camposdb.*;
import java.sql.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
/**
 *
 * <p>Título: coinvent</p>
 * <p>Descripción: Consulta Inventarios Fisicos realizados, tanto traspasados como no.
 *  </p>
 * <p>Copyright: Copyright (c) 2006-2011
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
 * @author chuchi P
 * @version 1.0
 */
public class coinvent extends ventana
{
  boolean swChgCam=false;
  boolean swChgAlm=false;


  String camCodi;
  int almCodi;
  int proCodi;
  int unidades,unidCam,unidAlm=0;
  double peso,pesoCam,pesoAlm=0;

  CPanel Pprinc = new CPanel();
  CLinkBox cam_codiE = new CLinkBox();
  CPanel Pcabe = new CPanel();
  CLabel cLabel14 = new CLabel();
  empPanel emp_codiE = new empPanel();
  PfechaInv cci_fecconE = new PfechaInv();
  CLinkBox alm_codiE = new CLinkBox();
  CLabel cLabel9 = new CLabel();
  CButton Birlin = new CButton();
  CLabel cLabel5 = new CLabel();
  CLabel cLabel12 = new CLabel();
  CLabel cLabel3 = new CLabel();
  proPanel pro_codiniE = new proPanel()
  {
        @Override
    public void afterFocusLost(boolean error)
    {
      if (error)
      {
        if (pro_codfinE.getValorInt() == 0)
        {
          pro_codfinE.setValorInt(pro_codiniE.getValorInt());
          try
          {
            pro_codfinE.controla(false);
          }
          catch (Exception k)
          {}
        }
      }
    }
  };
  CLabel cLabel4 = new CLabel();
  proPanel pro_codfinE = new proPanel();
  CCheckBox opVerDetalle = new CCheckBox();
  Cgrid jt = new Cgrid(11);
  CButton Baceptar = new CButton("Aceptar",Iconos.getImageIcon("check"));
  String s;
  GridBagLayout gridBagLayout1 = new GridBagLayout();

  public coinvent(EntornoUsuario eu, Principal p)
 {
   EU = eu;
   vl = p.panel1;
   jf = p;
   eje = true;

   setTitulo("Consulta Inventarios Fisicos");

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

 public coinvent(gnu.chu.anjelica.menu p, EntornoUsuario eu)
 {

   EU = eu;
   vl = p.getLayeredPane();
   setTitulo("Consulta Inventarios Fisicos");

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
   Pprinc.setLayout(gridBagLayout1);
   iniciarFrame();
   this.setSize(new Dimension(648, 500));
   this.setVersion("2012-03-07");
   statusBar = new StatusBar(this);
   conecta();
   Pprinc.setInputVerifier(null);
   this.getContentPane().add(Pprinc, BorderLayout.CENTER);
   this.getContentPane().add(statusBar, BorderLayout.SOUTH);
   cam_codiE.setBounds(new Rectangle(351, 3, 229, 17));
   cam_codiE.setAncTexto(25);
   Pcabe.setBorder(BorderFactory.createLoweredBevelBorder());
   Pcabe.setMaximumSize(new Dimension(591, 85));
   Pcabe.setMinimumSize(new Dimension(591, 85));
   Pcabe.setOpaque(true);
   Pcabe.setPreferredSize(new Dimension(591, 85));
   Pcabe.setQuery(false);
   Pcabe.setLayout(null);

   cLabel14.setText("Empresa");
   cLabel14.setBounds(new Rectangle(4, 3, 52, 17));
   emp_codiE.setBounds(new Rectangle(63, 3, 36, 17));
   cci_fecconE.setBounds(new Rectangle(188, 3, 100, 17));
   alm_codiE.setAncTexto(40);
   alm_codiE.setBounds(new Rectangle(62, 22, 317, 16));
   cLabel9.setText("Camara");
   cLabel9.setBounds(new Rectangle(289, 3, 47, 17));
   Birlin.setBounds(new Rectangle(543, 24, 2, 2));
   cLabel5.setText("Fecha Control");
   cLabel5.setBounds(new Rectangle(108, 4, 82, 17));
   cLabel12.setText("Almacen");
   cLabel12.setBounds(new Rectangle(4, 22, 55, 16));
   cLabel3.setText("De Producto");
   cLabel3.setBounds(new Rectangle(3, 42, 72, 19));
   pro_codiniE.setAncTexto(50);
   pro_codiniE.setBounds(new Rectangle(79, 42, 392, 18));
   cLabel4.setBounds(new Rectangle(3, 61, 72, 19));
   cLabel4.setMaximumSize(new Dimension(60, 14));
   cLabel4.setText("A Producto");
   pro_codfinE.setBounds(new Rectangle(79, 61, 391, 18));
   pro_codfinE.setAncTexto(50);
   opVerDetalle.setToolTipText("Agrupar Individuos");
   opVerDetalle.setText("Agrupar Indiv.");
   opVerDetalle.setBounds(new Rectangle(455, 22, 133, 16));

   Baceptar.setBounds(new Rectangle(476, 47, 111, 31));

   Pprinc.add(Pcabe,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 0, 0));
   Pcabe.add(cam_codiE, null);
   Pcabe.add(cLabel9, null);
   Pcabe.add(cci_fecconE, null);
   Pcabe.add(cLabel14, null);
   Pcabe.add(emp_codiE, null);
   Pcabe.add(Birlin, null);
   Pcabe.add(alm_codiE, null);
   Pcabe.add(cLabel12, null);
   Pcabe.add(pro_codiniE, null);
   Pcabe.add(cLabel3, null);
   Pcabe.add(pro_codfinE, null);
   Pcabe.add(cLabel4, null);
   Pprinc.add(jt,   new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 0, 0), 0, 0));
   Pcabe.add(Baceptar, null);
    Pcabe.add(opVerDetalle, null);
    Pcabe.add(cLabel5, null);
   confGrid();
 }

    @Override
 public void iniciarVentana() throws Exception
 {
   Pcabe.setDefButton(Baceptar);
   alm_codiE.setFormato(true);
   alm_codiE.setFormato(Types.DECIMAL, "#9", 2);
//   s = "SELECT alm_codi,alm_nomb FROM v_almacen " +
//       " ORDER BY alm_codi";
//   dtStat.select(s);
   pdalmace.llenaLinkBox(alm_codiE, dtStat,'*');
//    alm_codiE.addDatos(dtStat);
   alm_codiE.resetTexto();
   emp_codiE.iniciar(dtStat, this, vl, EU);
   opVerDetalle.setSelected(true);
  

   s="SELECT MAX(cci_feccon) as cci_feccon FROM coninvcab ";
   dtStat.select(s);
   cci_fecconE.setText(dtStat.getFecha("cci_feccon","dd-MM-yyyy"));
   cam_codiE.setFormato(true);
   cam_codiE.texto.setMayusc(true);
   cam_codiE.setFormato(Types.CHAR, "XX", 2);
   gnu.chu.anjelica.pad.pdconfig.llenaDiscr(dtCon1, cam_codiE, "AC", EU.em_cod);
   pro_codiniE.iniciar(dtStat,this,vl,EU);
   pro_codfinE.iniciar(dtStat,this,vl,EU);
   cci_fecconE.iniciar(dtStat,this,vl,EU);
   activarEventos();
 }

 void activarEventos()
 {
   Baceptar.addActionListener(new ActionListener()
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
   if (! emp_codiE.controla())
   {
     mensajeErr(emp_codiE.getMsgError());
     return;
   }
   if (cci_fecconE.isNull())
   {
     mensajeErr("Introduzca Fecha de Control Inventario");
     cci_fecconE.requestFocus();
     return;
   }
   s="SELECT c.*,l.*,p.pro_nomb FROM coninvcab as c,coninvlin as l left join v_articulo as p on"+
       "  p.pro_codi = l.pro_codi "+
       " WHERE c.emp_codi = "+emp_codiE.getValorInt()+
       " and c.cci_feccon = TO_DATE('"+cci_fecconE.getFecha()+"','dd-MM-yyyy')"+
       " and c.cci_codi= l.cci_codi "+
       " and c.emp_codi = l.emp_codi "+
       " and l.lci_regaut = 0" + // Solo registros no automaticos
       " and l.lci_peso + l.lci_numind != 0 ";
   if (! cam_codiE.isNull())
     s+=" and c.cam_codi = '"+cam_codiE.getText()+"'";
    if (alm_codiE.isNull())
      s+=" and c.alm_codi = '"+alm_codiE.getText()+"'";
    if (pro_codfinE.getValorInt()==pro_codiniE.getValorInt() && pro_codiniE.getValorInt()!=0)
        s+="and l.pro_codi = "+pro_codiniE.getValorInt();
    else
    {
        if (pro_codiniE.getValorInt()!=0)
            s+=" and l.pro_codi >= "+pro_codiniE.getValorInt();
        if (pro_codfinE.getValorInt()!=0)
            s+=" and l.pro_codi <= "+pro_codfinE.getValorInt();
     }
    s+=" ORDER BY c.alm_codi,c.cam_codi,l.pro_codi,prp_part,prp_indi";
//    debug("s: "+s);
    try {
      jt.removeAllDatos();
      if (!dtCon1.select(s))
      {
        mensajeErr("No encontrados registros de Inventario");
        return;
      }
      camCodi = dtCon1.getString("cam_codi");
      almCodi = dtCon1.getInt("alm_codi");
      proCodi = dtCon1.getInt("pro_codi");
      peso=0;
      unidades=0;
      pesoCam=0;
      unidCam=0;
      pesoAlm=0;
      unidAlm=0;
      swChgCam=false;
      swChgAlm=false;

      do
      {
        if (almCodi!=dtCon1.getInt("alm_codi") || proCodi!=dtCon1.getInt("pro_codi") ||
            ! camCodi.equals(dtCon1.getString("cam_codi")))
            muestraAcumulado(false);
        peso+=dtCon1.getDouble("lci_peso");
        unidades+=dtCon1.getInt("lci_numind");
        pesoCam+=dtCon1.getDouble("lci_peso");
        unidCam+=dtCon1.getInt("lci_numind");
        pesoAlm+=dtCon1.getDouble("lci_peso");
        unidAlm+=dtCon1.getInt("lci_numind");
        Vector v = new Vector();
        v.addElement(dtCon1.getString("alm_codi")); //'0
        v.addElement(dtCon1.getString("pro_codi")); // 1
        v.addElement(dtCon1.getString("pro_nomb")); //2
        v.addElement(dtCon1.getString("prp_ano")); // 3
        v.addElement(dtCon1.getString("prp_empcod")); // 4
        v.addElement(dtCon1.getString("prp_seri")); // 5
        v.addElement(dtCon1.getString("prp_part")); // 6
        v.addElement(dtCon1.getString("prp_indi")); // 7
        v.addElement(dtCon1.getString("lci_peso")); // 8
        v.addElement(dtCon1.getString("lci_numind")); // 9
        v.addElement(dtCon1.getString("cam_codi")); //10
        if (!opVerDetalle.isSelected())
          jt.addLinea(v);
      } while (dtCon1.next());
      muestraAcumulado(true);
      jt.requestFocusInicio();
      mensajeErr("Carga de datos.. completada");
    } catch (Exception k)
    {
      Error("Error al buscar Registros de Inventario",k);
      return;
    }
 }

 void muestraAcumulado(boolean forzar) throws SQLException
 {
   if ( (! camCodi.equals(dtCon1.getString("cam_codi")) &&
         almCodi==dtCon1.getInt("alm_codi") && proCodi==dtCon1.getInt("pro_codi") )
       || ( ! camCodi.equals(dtCon1.getString("cam_codi")) && swChgCam)
       || (swChgCam && forzar))
  {
     muestraResumen("Camara: " + camCodi, 1,pesoCam,unidCam);
     swChgCam=true;
     camCodi=dtCon1.getString("cam_codi");
     pesoCam=0;
     unidCam=0;
   }
   if (( almCodi!=dtCon1.getInt("alm_codi") && proCodi==dtCon1.getInt("pro_codi"))
      ||  (almCodi!=dtCon1.getInt("alm_codi") && swChgAlm)
      || (swChgAlm && forzar))
   {
     muestraResumen("Almacen: " + almCodi, 2,pesoAlm,unidAlm);
     almCodi = dtCon1.getInt("alm_codi");
     swChgAlm=true;
     pesoAlm = 0;
     unidAlm = 0;
     pesoCam=0;
     unidCam=0;
   }
   if ( proCodi!=dtCon1.getInt("pro_codi")   || forzar)
    {
      muestraResumen("Producto: " + proCodi, 3,peso,unidades);
      proCodi = dtCon1.getInt("pro_codi");
      peso = 0;
      unidades = 0;
      pesoAlm = 0;
      unidAlm = 0;
      pesoCam = 0;
      unidCam = 0;
      swChgCam=false;
      swChgAlm=false;

    }

 }
 void muestraResumen(String literal,int tipo,double pesoAc, int unidAc)
 {
   Vector v=new Vector();
   v.addElement(tipo==2?""+almCodi:"");
   v.addElement("");
   v.addElement(literal);
   v.addElement(""); // 3
   v.addElement(""); // 4
   v.addElement(""); // 5
   v.addElement(""); // 6
   v.addElement(""); // 7
   v.addElement(""+pesoAc);
   v.addElement(""+unidAc);
   v.addElement(tipo==1?camCodi:"");
   jt.addLinea(v);
 }
 void confGrid() throws Exception
 {
   Vector v = new Vector();
   v.addElement("Almacén"); // 0
   v.addElement("Producto"); // 1
   v.addElement("Descripcion"); // 2
   v.addElement("Eje"); // 3
   v.addElement("Emp"); // 4
   v.addElement("Ser"); // 5
   v.addElement("Part"); // 6
   v.addElement("Ind."); // 7
   v.addElement("Cantidad"); // 8
   v.addElement("N.Pzas"); // 9
   v.addElement("Cámara"); // 10
   jt.setCabecera(v);
   jt.setAnchoColumna(new int[]
                      {30, 80, 200, 40, 30, 20, 40, 40, 60, 40,40});
   jt.setMaximumSize(new Dimension(598, 339));
    jt.setMinimumSize(new Dimension(598, 339));
    jt.setPreferredSize(new Dimension(598, 339));
    jt.setAlinearColumna(new int[]
                        {2, 2, 0, 2, 2, 0, 2, 2, 2, 2,0});
   jt.setFormatoColumna(8,"---,--9.99");
   jt.setFormatoColumna(9,"---9");
 }
}
