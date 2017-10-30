package gnu.chu.anjelica.compras;

import gnu.chu.Menu.*;
import gnu.chu.anjelica.almacen.pdalmace;
import gnu.chu.anjelica.listados.Listados;
import gnu.chu.anjelica.pad.pdtransp;
import gnu.chu.camposdb.*;
import gnu.chu.controles.*;
import gnu.chu.interfaces.ejecutable;
import gnu.chu.utilidades.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.BorderFactory;
import net.sf.jasperreports.engine.*;
/**
*  Consulta Productos de Pedidos de compras
 * @parametros verPrecio Ver precios
 * 
 * Permite consultar las compras realizadas de un producto
 * dentro de unas fechas.
 *
 * Tambien permite delimitar por proveedor y por albaran.
 *  <p>Copyright: Copyright (c) 2005-2017
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
  boolean  swBreakAcum;
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
  Cgrid jt = new Cgrid(10);
  CCheckBox opDesgl = new CCheckBox();
  CButton Bimpri=new CButton(Iconos.getImageIcon("print"));
  CCheckBox opPedPend = new CCheckBox();
  CCheckBox opVerPrecios = new CCheckBox();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CTextField feenfiE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel11 = new CLabel();
  CPanel Ppie = new CPanel(null);
  CLabel unidadL = new CLabel("Unidades");
  CLabel kilosL = new CLabel("Kilos");
  CTextField unidadE = new CTextField(Types.DECIMAL,"#,##9");
  CTextField kilosE = new CTextField(Types.DECIMAL,"###,##9.99");
  CLabel tra_codiL = new CLabel("Transport.");
  CLinkBox tra_codiE = new CLinkBox();
  
  public clprpeco(EntornoUsuario eu, Principal p)
 {
   this(eu,p,null);
 }

 public clprpeco(EntornoUsuario eu, Principal p, Hashtable<String,String> ht)
 {
   EU = eu;
   vl = p.panel1;
   jf = p;
   eje = true;

   try
   {
     ponParametros(ht);
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

 public clprpeco(gnu.chu.anjelica.menu p, EntornoUsuario eu, Hashtable<String,String> ht)
 {
   EU = eu;
   vl = p.getLayeredPane();
   eje = false;

   try
   {
     ponParametros(ht);
     setTitulo("Consulta Pedidos Compras");

     jbInit();
   }
   catch (Exception e)
   {
     ErrorInit(e);
   }
 }
 
 private void ponParametros(Hashtable<String,String> ht)
 {
     if (ht != null)
     {
       if (ht.get("verPrecio") != null)
         verPrecio = Boolean.valueOf(ht.get("verPrecio"));

     }
 }
 
 private void jbInit() throws Exception
 {
   iniciarFrame();
   this.setSize(new Dimension(751, 510));
   this.setVersion(" (2017-10-29)" + (verPrecio ? "- Ver Precios" : ""));
   statusBar = new StatusBar(this);

   opPedPend.setToolTipText("Ver SOLO Pedidos Pedidos Pendientes");
    opPedPend.setVerifyInputWhenFocusTarget(true);
    opPedPend.setSelected(true);
    opPedPend.setText("Pend.");
    opPedPend.setBounds(new Rectangle(365, 40, 60, 17));
    opVerPrecios.setText("Precios");
    if (verPrecio)
       opVerPrecios.setSelected(true);
    else
        opVerPrecios.setEnabled(false);
    opVerPrecios.setBounds(new Rectangle(437, 40, 60, 17));
    cLabel8.setToolTipText("");
    
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
   tra_codiL.setBounds(new Rectangle(3, 60, 69, 18));
   tra_codiE.setBounds(new Rectangle(74, 60, 350, 18));

   tra_codiE.setAncTexto(45);
   tra_codiE.setFormato(Types.DECIMAL,"###9");
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
   PtipoCons.setMaximumSize(new Dimension(728, 80));
   PtipoCons.setMinimumSize(new Dimension(728, 80));
   PtipoCons.setPreferredSize(new Dimension(728, 80));
   PtipoCons.setBorder(BorderFactory.createRaisedBevelBorder());
   PtipoCons.setLayout(null);
   Ppie.setMaximumSize(new Dimension(400, 25));
   Ppie.setMinimumSize(new Dimension(400, 25));
   Ppie.setPreferredSize(new Dimension(280, 25));
   Ppie.setBorder(BorderFactory.createLoweredBevelBorder());
   unidadL.setBounds(new Rectangle(12, 2, 68, 17));
   unidadE.setBounds(new Rectangle(82, 2, 48, 17));
   kilosL.setBounds(new Rectangle(160, 2, 38, 17));
   kilosE.setBounds(new Rectangle(200, 2, 68, 17));
   proiniE.setBounds(new Rectangle(49, 21, 224, 17));
   unidadE.setEditable(false);
   kilosE.setEditable(false);
   cLabel3.setText("Almacen");
   cLabel3.setBounds(new Rectangle(231, 3, 53, 17));
   cLabel10.setText("Empr.");
   cLabel10.setBounds(new Rectangle(538, 20, 39, 17));
   profinE.setBounds(new Rectangle(311, 20, 224, 17));
   Baceptar.setBounds(new Rectangle(608, 55, 113, 22));
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
            ,GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    Pprinc.add(jt,   new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    Pprinc.add(Ppie,   new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    PtipoCons.add(tra_codiL, null);
    PtipoCons.add(tra_codiE, null);
    PtipoCons.add(opDesgl, null);
    PtipoCons.add(opPedPend, null);
    PtipoCons.add(opVerPrecios, null);
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
    Ppie.add(unidadL,null);
    Ppie.add(unidadE,null);
    Ppie.add(kilosL,null);
    Ppie.add(kilosE,null);
 }

  @Override
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
    tla_codiE.addDatos(dtStat);
   tla_codiE.addDatos("99", "Definido Usuario");
   
   tla_codiE.setText("99");
   alm_codiE.setFormato(true);
   alm_codiE.setFormato(Types.DECIMAL, "#9", 2);
   pdalmace.llenaLinkBox(alm_codiE, dtCon1,'*');
   pdtransp.llenaPrvCompra(dtStat, tra_codiE);
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

   feeninE.setText(Formatear.sumaDias(Formatear.getDateAct(),-3));
   feenfiE.setText(Formatear.sumaDias(Formatear.getDateAct(),3));
   emp_codiE.setValorInt(EU.em_cod);
   activarEventos();
 }

 void activarEventos()
 {
    jt.addMouseListener(new MouseAdapter()
    {
            @Override
      public void mouseClicked(MouseEvent e)
      {
        try
        {
          if (jt.isVacio() || e.getClickCount()<2 || jt.getValorInt(3)==0)
              return;
          irMantPedidos();
        }
        catch (Exception k)
        {
          Error("Error al Ir a Lineas de despiece", k);
        }
      }
    });  
   Baceptar.addActionListener(new ActionListener()
   {
     @Override
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
void irMantPedidos()
  {
      msgEspere("Llamando a  Programa Mant. Pedidos");
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
                  if (jf==null)
                  {
                        resetMsgEspere();
                      return;
                  } 
                  if ((prog = jf.gestor.getProceso(pdpedco.getNombreClase())) == null)
                  {
                      resetMsgEspere();
                      msgBox("Usuario sin Mantenimiento Pedidos Compras");
                      return;
                  }
                      pdpedco cm = (pdpedco) prog;
                      if (cm.inTransation())
                      {
                          msgBox("Mantenimiento Pedidos Compras ocupado. No se puede realizar la consulta");
                          resetMsgEspere();
                          return;
                      }
                      
                      cm.PADQuery();
                      cm.setPedido(jt.getValorInt(3));                      
                      cm.ej_query1();
                      jf.gestor.ir(cm);
                  
                      resetMsgEspere();
              }
          });
          
        }
     };
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
       (tra_codiE.isNull()?"":" and tra_codi = "+tra_codiE.getValorInt())+
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
     @Override
     public void run()
     {
       msgEspere("Realizando Consulta");
       consultar();
       resetMsgEspere();
       jt.requestFocusInicioLater();
     }
   };
 }

 void consultar()
 {   
   mensaje("A esperar ... estoy generando el listado");

   int tlaCodi=tla_codiE.getValorInt();
   s=getStrSql(tlaCodi);
//   Formatear.verAncGrid(jt);
//   debug(s);
   jt.removeAllDatos();
   try {
     if (! dtCon1.select(s))
     {       
       mensajeErr("No encontrados pedidos con estas condiciones");
       mensaje("");
       return;
     }
     String proNomb=dtCon1.getString("nombart");
     int proCodi=dtCon1.getInt("pro_codi");
     String proGrupo=tlaCodi==99?proNomb:dtCon1.getString("pro_desc");
     int nLiPr=-1;
     double impAcum=0;
     double numCaj=0;
     double cantCaj=0;
     int nCajas;
     double kg;   
     double importe;
     String tipo;
     swBreakAcum=false;
     double unidT=0,kilosT=0;
     boolean swPonArtic=false,swPonGrupo=false;
     if (tlaCodi == 99)
         swPonArtic=true;
     else
         swPonGrupo=true;
     do
     {
         ArrayList v = new ArrayList();
         if (tlaCodi == 99)
         {             
             proNomb =  dtCon1.getString("nombart");
             if (proCodi != dtCon1.getInt("pro_codi") )
             {
                 if (nLiPr>1 || !opDesgl.isSelected() )
                     ponAcumul(numCaj, impAcum, cantCaj,proCodi,proGrupo);
                 swBreakAcum=false;
                 proCodi=dtCon1.getInt("pro_codi");
                 proGrupo = proNomb;
                 numCaj = 0;
                 cantCaj = 0;
                 impAcum = 0;
                 nLiPr = 0;
                 swPonArtic=true;
                
             } 
         } 
         else
         { // No es tipo listado 99           
             if (!proGrupo.equals(dtCon1.getString("pro_desc"))  )
             {
                 if (nLiPr > 1 || !opDesgl.isSelected())
                     ponAcumul(numCaj, cantCaj, impAcum, proCodi,proGrupo);
                 proCodi=dtCon1.getInt("pro_codi");
                 proGrupo = dtCon1.getString("pro_desc");
                 numCaj = 0;
                 cantCaj = 0;
                 impAcum = 0;
                 nLiPr = 0;
                 swPonGrupo=true;
             }                          
         }
         
         if (nLiPr<0 )
             nLiPr=0;
         switch (dtCon1.getString("pcc_estad"))
         {
             case "P":
                 nCajas=dtCon1.getInt("pcl_nucape");           
                 kg=dtCon1.getDouble("pcl_cantpe");        
                 importe=dtCon1.getDouble("pcl_precpe");
                 tipo="Pend.Conf";
                 break;
             case "C":
                 nCajas=dtCon1.getInt("pcl_nucaco");           
                 kg=dtCon1.getDouble("pcl_cantco");        
                 importe=dtCon1.getDouble("pcl_precco");
                 tipo="Confir";
                 break;
             default:
                 nCajas=dtCon1.getInt("pcl_nucafa");           
                 kg=dtCon1.getDouble("pcl_cantfa"); 
                 importe=dtCon1.getDouble("pcl_precfa");
                 tipo="Prefact";               
         }
         unidT+=nCajas;
         kilosT+=kg;
         numCaj +=nCajas;
         cantCaj += kg;
         if (importe==0)
         {
            if (nCajas>0 || kg>0)
                swBreakAcum=true;
         }
         else
         {
            importe+= dtCon1.getString("pcc_portes").equals("D") ? dtCon1.getDouble("pcc_imppor") : 0;         
            impAcum += kg * importe;
         }
         if ((nCajas>0 || kg>0) && opDesgl.isSelected())
         {
            nLiPr++;
            if (swPonArtic || swPonGrupo)
            {
                v.add(swPonArtic?proCodi:"");
                v.add(proGrupo);
                swPonArtic=false;
                swPonGrupo=false;
            }
            else
            {
                v.add("");
                v.add("");
            }
            v.add(dtCon1.getString("prv_nomco")); // 1
            v.add(dtCon1.getInt("pcc_nume")); // 3
            v.add(dtCon1.getFecha("pcc_fecrec", "dd-MM-yyyy")); // 2
            v.add(nCajas); // 3
            v.add(kg);          
            v.add(opVerPrecios.isSelected()?importe:0);
            v.add(tipo);                     
            v.add(dtCon1.getInt("tra_codi",true));
            jt.addLinea(v);
         }
     } while (dtCon1.next());
     if (nLiPr>1 || !opDesgl.isSelected() )
        ponAcumul(numCaj, impAcum, cantCaj,proCodi,proGrupo);
     unidadE.setValorDec(unidT);
     kilosE.setValorDec(kilosT);
     mensajeErr("COnsulta ... Realizada");
     mensaje("");
   } catch (SQLException k)
   {
     Error("Error al buscar pedidos Compras",k);
   }
 }

 private void ponAcumul(double numCaj, double impEntr,double cantCaj,int proCodi,String proNomb)
 {
   ArrayList v = new ArrayList();
   
   if (opDesgl.isSelected())
   {
     v.add("");
     v.add("Total Producto ...");
   }
   else
   {
     v.add(proCodi);
     v.add(proNomb);
   }
   v.add(""); // Proveedor
   v.add(""); //Nº Ped.
   v.add(""); // Fec. Cad
   v.add( numCaj);
   v.add( cantCaj);
   if (swBreakAcum)
       v.add("");
   else
       v.add( opVerPrecios.isSelected()? cantCaj==0?0:impEntr/cantCaj:0);
   v.add("");
   v.add("");
   swBreakAcum=false;
   jt.addLinea(v);
 }
 private void confGrid() throws Exception
 {
     ArrayList v = new ArrayList();
     jt.setMaximumSize(new Dimension(705, 359));
     jt.setMinimumSize(new Dimension(705, 359));
     jt.setPreferredSize(new Dimension(705, 359));
     v.add("Artic"); // 0
     v.add("Descripción"); // 1
     v.add("Prov"); // 2
     v.add("NºPed"); // 3
     v.add("Fec.Ent"); // 4
     v.add("Unid"); // 5
     v.add("Kilos"); // 6
     v.add("Precio"); // 7
     v.add("Estad"); // 8
     v.add("Trans.");
     jt.setCabecera(v);

     jt.setAnchoColumna(new int[]
     {
         45,130, 130, 40, 70, 35, 55, 45, 50,40
     });
     jt.setAlinearColumna(new int[]
     {
         2,0, 0,2, 1, 2, 2, 2, 0,2
     });
     jt.setAjustarGrid(true);
     jt.setFormatoColumna(5, "----9");
     jt.setFormatoColumna(6, "----,--9.9");
     jt.setFormatoColumna(7, "----9.99");
   
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
       mp.put("opDesgl",opDesgl.isSelected());
       mp.put("fecEntr",feeninE.getDate());
       mp.put("tla_nomb",tla_codiE.getTextCombo());
       mp.put("pro_codi",proiniE.getValorInt());
       mp.put("pro_nomb",proiniE.getTextNomb());
       mp.put("pro_codi1",profinE.getValorInt());
       mp.put("pro_nomb1",profinE.getTextNomb());
       mp.put("alm_codi",alm_codiE.getValorInt()+" -> "+alm_codiE.getTextCombo());
       mp.put("cam_codi",cam_codiE.isNull()?"":cam_codiE.getText()+" -> "+cam_codiE.getTextCombo());
       jr = Listados.getJasperReport(EU, "pedcompr");
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
  @Override
   public boolean next() throws JRException
    {
      try {
        return rs.next();
      } catch (SQLException k)
      {
        throw new JRException(k);
      }
    }

  @Override
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
        if (campo.equals("pcc_fecrec"))
          return rs.getDate("pcc_fecrec");
        if (campo.equals("prv_nomco"))
          return rs.getString("prv_nomco");
        if (campo.equals("pcl_numcaj"))
        {
            switch (rs.getString("pcc_estad"))
            {
                case "P":
                    return rs.getInt("pcl_nucape");
                case "C":
                    return rs.getInt("pcl_nucaco");
                default:
                    return rs.getInt("pcl_nucafa");
            }
        }
        if (campo.equals("pcl_canti"))
        {
            switch (rs.getString("pcc_estad"))
            {
                case "P":
                    return rs.getDouble("pcl_cantpe");
                case "C":
                    return rs.getDouble("pcl_cantco");
                default:
                    return rs.getDouble("pcl_cantfa");
            }
        }
        if (campo.equals("pcl_precio"))
        {
            if (!opVerPrecios.isSelected())
                return new Double(0);
            switch (rs.getString("pcc_estad"))
            {
                case "P":
                    return rs.getDouble("pcl_precpe") + (rs.getString("pcc_portes").equals("D") ? rs.getDouble("pcc_imppor") : 0);
                case "C":
                    return rs.getDouble("pcl_precco") + (rs.getString("pcc_portes").equals("D") ? rs.getDouble("pcc_imppor") : 0);
                default:
                    return rs.getDouble("pcl_precfa") + (rs.getString("pcc_portes").equals("D") ? rs.getDouble("pcc_imppor") : 0);
//            return new Double(rs.getDouble("pcl_precfa"));
            }
        }
        if (campo.equals("pcc_estad"))
        {
            switch (rs.getString("pcc_estad"))
            {
                case "P":
                    return "PEND.";
                case "C":
                    return "CONF";
                default:
                    return "PREFACT.";
            }
        }
        throw new JRException("Campo: " + campo + " NO encontrado");
      }
      catch (SQLException k)
      {
        throw new JRException(k);
      }

    }
}
