package gnu.chu.anjelica.pad;

import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import gnu.chu.sql.*;
import java.sql.*;
import java.awt.*;
import java.util.*;
import gnu.chu.interfaces.*;
import java.text.*;
import gnu.chu.camposdb.*;
import gnu.chu.Menu.*;
import javax.swing.border.*;

 /**
 *
 * <p>Título: pdconfig </p>
 * <p>Descripción: Mantenimiento de configuraciones por empresa</p>
 * <p>Copyright: Copyright (c) 2005-2014
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los términos de la Licencia Pública General de GNU segun es publicada por
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
 * @author chuchiP
 * @version 1.0
 */
public class pdconfig    extends ventanaPad     implements PAD
{
  public final static int TIPOEMP_SALADESP=1; // Sala despiece
  public final static int TIPOEMP_PLANTACION=2; // Plantacion (Invernadero)
  CTextField[][] camposDisc=new CTextField[3][4];
  String s;
  boolean modConsulta = true; 
  CPanel Pprinc = new CPanel();
  prvPanel emp_prvdesE = new prvPanel();
  CLinkBox emp_codiE = new CLinkBox();
  CLabel cLabel1 = new CLabel();
//  CButton Baceptar = new CButton("Aceptar");
//  CButton Bcancelar = new CButton("Cancelar");
  CPanel Pbasic = new CPanel();
  CLabel cLabel2 = new CLabel();
  CLinkBox cfg_almcomE = new CLinkBox();
  CLinkBox cfg_almvenE = new CLinkBox();
  CLabel cLabel3 = new CLabel();
  CLabel cLabel4 = new CLabel();
  CTextField cfg_numdecE = new CTextField(Types.DECIMAL, "#9");
  CCheckBox cfg_desvenE = new CCheckBox();
  tidCodi2 cfg_tideveE = new tidCodi2();
  CLabel cLabel5 = new CLabel();
  CLabel prv_nombL = new CLabel();
  CTabbedPane Tpanel1 = new CTabbedPane();
  CPanel Pdiscr = new CPanel();
  CPanel Pdisart = new CPanel();
  CPanel Pdiscli = new CPanel();
  CPanel Pdisprv = new CPanel();
  CCheckBox cfg_caejauE = new CCheckBox();
  CCheckBox cfg_lialgrC = new CCheckBox("S","N");
  CPanel PmodoGraf = new CPanel();
  TitledBorder titledBorder5=new TitledBorder("") ;
  CCheckBox cfg_lifrgrC = new CCheckBox("S","N");
  private CLabel cli_codiL = new CLabel();
  private cliPanel cli_codiE = new cliPanel();
  private CLabel cfg_tipempL = new CLabel("Tipo Empresa");
  private CComboBox cfg_tipempE = new CComboBox();
  private CCheckBox cfg_palvenE = new CCheckBox();
  public pdconfig(EntornoUsuario eu, Principal p)
  {
    this(eu, p, null);
  }

  public pdconfig(EntornoUsuario eu, Principal p,Hashtable ht)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    try
    {
      if (ht != null)
      {
        if (ht.get("modConsulta") != null)
          modConsulta = Boolean.valueOf(ht.get("modConsulta").toString()).
              booleanValue();
      }
      setTitulo("Mantenimiento Configuraciones");
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

  public pdconfig(gnu.chu.anjelica.menu p, EntornoUsuario eu,Hashtable ht)
  {
    EU = eu;
    vl = p.getLayeredPane();
    eje = false;

    try
    {
      if (ht != null)
      {
        if (ht.get("modConsulta") != null)
          modConsulta = Boolean.valueOf(ht.get("modConsulta").toString()).
              booleanValue();
      }
      setTitulo("Mantenimiento Configuraciones");
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
   this.setSize(new Dimension(567, 417));
   this.setVersion("2014-12-10 " + (modConsulta ? "-SOLO LECTURA-" : ""));

   strSql = "SELECT * FROM configuracion WHERE emp_codi = " + EU.em_cod +
       getOrderQuery();


   statusBar = new StatusBar(this);
   nav = new navegador(this, dtCons, false,modConsulta?navegador.CURYCON:navegador.NORMAL);
   conecta();
   iniciar(this);


    Tpanel1.setBounds(new Rectangle(3, 22, 535, 255));

    Pdiscr.setLayout(null);
    Pdisart.setBounds(new Rectangle(31, 3, 456, 62));
    Pdisart.setLayout(null);

    Pdiscli.setLayout(null);
    Pdiscli.setBounds(new Rectangle(31, 66, 456, 62));
    Pdisprv.setBounds(new Rectangle(31, 129, 456, 62));
    Pdisprv.setLayout(null);
    cfg_caejauE.setToolTipText("Cambio Automatico de Ejercicio");
    cfg_caejauE.setSelected(true);
    cfg_caejauE.setText("Cambio Aut. Ejercicio");
    cfg_caejauE.setBounds(new Rectangle(5, 140, 156, 18));
    cfg_lialgrC.setText("Albaranes Venta");
    cfg_lialgrC.setBounds(new Rectangle(21, 18, 138, 19));
    PmodoGraf.setBorder(titledBorder5);
    PmodoGraf.setText("Impresion modo grafico");
    PmodoGraf.setBounds(new Rectangle(160, 140, 174, 62));
    PmodoGraf.setLayout(null);
  
    titledBorder5.setTitleFont(new Font("Dialog", 1, 11));
    titledBorder5.setTitle("Impresion modo grafico");
    cfg_lifrgrC.setBounds(new Rectangle(21, 37, 127, 19));
    cfg_lifrgrC.setText("Facturas Venta");
    
    
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);

   Pprinc.setLayout(null);
   // Cambios
   emp_prvdesE.setAceptaNulo(false);
   cli_codiE.setAceptaNulo(false);
   cli_codiL.setText("Cliente");
   cli_codiL.setBounds(new Rectangle(7, 110, 90, 20));
   cli_codiE.setBounds(new Rectangle(105, 110, 390, 20));

   emp_prvdesE.setBounds(new Rectangle(105, 88, 390, 20));
   prv_nombL.setBounds(new Rectangle(7, 88, 90, 20));
   cfg_tipempE.addItem("Sala Despiece","1");
   cfg_tipempE.addItem("Plantación","2");

   cfg_tipempL.setBounds(7,205,80,16);
   cfg_tipempE.setBounds(100,205,150,16);
   cfg_palvenE.setText("Palets Venta");
   cfg_palvenE.setBounds(new Rectangle(310, 205, 107, 19));
   cfg_tideveE.setBounds(new Rectangle(108, 66, 389, 18));
   cLabel5.setBounds(new Rectangle(7, 66, 105, 18));
   cfg_desvenE.setBounds(new Rectangle(281, 46, 216, 18));
   cfg_numdecE.setBounds(new Rectangle(108, 46, 35, 18));
   cLabel4.setBounds(new Rectangle(5, 46, 106, 18));
   cfg_almvenE.setBounds(new Rectangle(108, 26, 314, 18));
   cLabel3.setBounds(new Rectangle(6, 26, 106, 18));
   cfg_almcomE.setBounds(new Rectangle(108, 5, 314, 18));
   cLabel2.setBounds(new Rectangle(5, 5, 106, 18));

   cLabel1.setText("Empresa");
   cLabel1.setBounds(new Rectangle(59, 3, 55, 17));
   Baceptar.setBounds(new Rectangle(111, 279, 126, 28));
   Bcancelar.setBounds(new Rectangle(289, 279, 126, 28));
   emp_codiE.setBounds(new Rectangle(112, 2, 358, 18));

   cLabel2.setText("Almacen Compras");
   cfg_almcomE.setAncTexto(30);
   cfg_almcomE.setFormato(Types.DECIMAL, "#9");
   cfg_almvenE.setFormato(Types.DECIMAL, "#9");
   cfg_almvenE.setAncTexto(30);
   cLabel3.setText("Almacen Ventas");
   cLabel4.setText("Numero Decimales");

   cfg_desvenE.setText("Desp. Automaticos en Ventas");

   cLabel5.setText("Tipo Desp. Ventas");
   prv_nombL.setText("Proveedor Desp.");
   emp_codiE.setAncTexto(30);
   Pbasic.setLayout(null);
   Pbasic.add(cli_codiE, null);
   Pbasic.add(cli_codiL, null);
   Pbasic.add(cfg_almcomE, null);
   Pbasic.add(cLabel2, null);
   Pbasic.add(cLabel3, null);
   Pbasic.add(prv_nombL, null);
   Pbasic.add(cfg_almvenE, null);
    Pbasic.add(emp_prvdesE, null);
    Pbasic.add(cfg_desvenE, null);
    Pbasic.add(cLabel4, null);
    Pbasic.add(cfg_numdecE, null);
    Pbasic.add(cfg_tideveE, null);
    Pbasic.add(cLabel5, null);
    Pbasic.add(cfg_tipempL,null);
    Pbasic.add(cfg_tipempE,null);
    Pbasic.add(cfg_palvenE,null);
    Pbasic.add(cfg_caejauE, null);
    Pbasic.add(PmodoGraf, null);
    PmodoGraf.add(cfg_lialgrC, null);
    PmodoGraf.add(cfg_lifrgrC, null);
    Tpanel1.add(Pbasic, "Basicos");
    Tpanel1.add(Pdiscr, "Discriminadores");
    Pprinc.add(cLabel1, null);
    Pprinc.add(emp_codiE, null);

    Pdiscr.add(Pdiscli, null);
    Pdiscr.add(Pdisprv, null);
    Pprinc.add(Bcancelar, null);
    Pprinc.add(Baceptar, null);
    Pprinc.add(Tpanel1, null);
    Pdiscr.add(Pdisart, null);
    ponCamposDis(0,Pdisart,"pr");
    ponCamposDis(1,Pdiscli,"cl");
    ponCamposDis(2,Pdisprv,"pv");
 }

 public void iniciarVentana() throws Exception
 {
   emp_codiE.setCeroIsNull(true);
   emp_codiE.setAceptaNulo(false);
   Pprinc.setDefButton(Baceptar);

   emp_codiE.setColumnaAlias("emp_codi");

   activar(false);
   verDatos();
   activarEventos();
 }

 void ponCamposDis(int pos, CPanel panel,String tipo) throws Exception
 {
   CLabel cfg_dispr1L = new CLabel();
   CTextField cfg_dispr1E = new CTextField(Types.CHAR, "X", 15);
   CTextField cfg_dispr2E = new CTextField(Types.CHAR, "X", 15);
   CLabel cfg_dispr2L = new CLabel();
   CTextField cfg_dispr3E = new CTextField(Types.CHAR, "X", 15);
   CLabel cfg_dispr3L = new CLabel();
   CTextField cfg_dispr4E = new CTextField(Types.CHAR, "X", 15);
   CLabel cfg_dispr4L = new CLabel();

   cfg_dispr1L.setText("Discriminador 1");
   cfg_dispr1L.setBounds(new Rectangle(5, 16, 98, 17));
   cfg_dispr1E.setBounds(new Rectangle(101, 16, 118, 17));
   cfg_dispr2E.setBounds(new Rectangle(330, 17, 118, 17));
   cfg_dispr2L.setBounds(new Rectangle(234, 17, 98, 17));
   cfg_dispr2L.setText("Discriminador 2");
   cfg_dispr3E.setBounds(new Rectangle(101, 36, 118, 17));
   cfg_dispr3L.setBounds(new Rectangle(5, 36, 98, 17));
   cfg_dispr3L.setText("Discriminador 3");
   cfg_dispr4E.setBounds(new Rectangle(330, 36, 118, 17));
   cfg_dispr4L.setBounds(new Rectangle(234, 36, 98, 17));
   cfg_dispr4L.setText("Discriminador 4");
   panel.add(cfg_dispr1L, null);
   panel.add(cfg_dispr1E, null);
   panel.add(cfg_dispr2E, null);
   panel.add(cfg_dispr2L, null);
   panel.add(cfg_dispr3E, null);
   panel.add(cfg_dispr3L, null);
   panel.add(cfg_dispr4E, null);
   panel.add(cfg_dispr4L, null);
   camposDisc[pos][0] = cfg_dispr1E;
   camposDisc[pos][1] = cfg_dispr2E;
   camposDisc[pos][2] = cfg_dispr3E;
   camposDisc[pos][3] = cfg_dispr4E;

 }

 void activarEventos(){
 }
    @Override
 public void afterConecta() throws SQLException, java.text.ParseException
 {
  s = "SELECT emp_codi,emp_nomb FROM v_empresa ORDER BY emp_codi";
   dtCon1.select(s);
   emp_codiE.addDatos(dtCon1);
   emp_codiE.setFormato(Types.DECIMAL, "#9");

   emp_prvdesE.setAceptaNulo(false);
   emp_prvdesE.iniciar(dtStat, this, vl, EU);
   cli_codiE.setAceptaNulo(false);
   cli_codiE.iniciar(dtStat, this, vl, EU);
   s = "SELECT alm_codi,alm_nomb FROM v_almacen ORDER BY alm_nomb";
   dtCon1.select(s);
   cfg_almcomE.addDatos(dtCon1);
   dtCon1.first();
   cfg_almvenE.addDatos(dtCon1);
   cfg_tideveE.iniciar(dtStat,this,vl,EU);
 }
 void verDatos()
 {
   try
   {
     if (dtCons.getNOREG())
       return;

     emp_codiE.setValorInt(dtCons.getInt("emp_codi"));

     if (! selectRegPant(dtCon1,false))
     {
       mensajeErr("Datos de Configuracion  .. NO ENCONTRADOS");
       Pbasic.resetTexto();
       return;
     }
     cfg_almcomE.setValorInt(dtCon1.getInt("cfg_almcom"));
     cfg_almvenE.setValorInt(dtCon1.getInt("cfg_almven"));
     cfg_numdecE.setValorInt(dtCon1.getInt("cfg_numdec"));
     cfg_desvenE.setSelected(dtCon1.getInt("cfg_desven")!=0);
     
     cfg_tideveE.setValorInt(dtCon1.getInt("cfg_tideve"));
     emp_prvdesE.setValorInt(dtCon1.getInt("emp_prvdes"));
     cli_codiE.setValorInt(dtCon1.getInt("cli_codi"));
     cfg_caejauE.setSelected(dtCon1.getString("cfg_caejau").equals("S"));
     cfg_lialgrC.setSelected(dtCon1.getString("cfg_lialgr").equals("S"));
     cfg_lifrgrC.setSelecion(dtCon1.getString("cfg_lifrgr"));
     cfg_tipempE.setValor(dtCon1.getString("cfg_tipemp"));
     cfg_palvenE.setSelected(dtCon1.getInt("cfg_palven")!=0);
     verDatos(0,"pr");
     verDatos(1,"cl");
     verDatos(2,"pv");
   }
   catch (SQLException k)
   {
     Error("Error al Mostrar Datos", k);
   }

 }
 private void verDatos(int p, String tabla) throws SQLException
 {
   for (int n = 0; n < 4; n++)
     camposDisc[p][n].setText(dtCon1.getString("cfg_dis" + tabla + (n+1)));
 }
 private boolean selectRegPant(DatosTabla dt,boolean block) throws SQLException
 {
   s = "select * from configuracion WHERE emp_codi = " + emp_codiE.getValorInt() ;

   return dt.select(s,block);

 }

 public void PADPrimero()  {verDatos();}

 public void PADAnterior()  {verDatos();}

 public void PADSiguiente()  {verDatos();}

 public void PADUltimo()  {verDatos();}

 public void PADQuery()
 {
   Pprinc.setQuery(true);
   Pprinc.resetTexto();

   mensaje("Establezca FILTRO de Consulta");
   activar(true);
   Pbasic.setEnabled(false);
   Pdiscr.setEnabled(false);
   emp_codiE.requestFocus();
 }


 public void ej_query1()
 {
   Component c = Pbasic.getErrorConf();
   if (c != null)
   {
     mensajeErr("FILTRO DE CONSULTA NO VALIDO");
     c.requestFocus();
     return;
   }
   try
   {

     Vector v = new Vector();
     v.add(emp_codiE.getStrQuery());
//     v.add(cfg_almcomE.getStrQuery());
//     v.add(cfg_almvenE.getStrQuery());
//     v.add(emp_prvdesE.getStrQuery());
     s = "SELECT * FROM configuracion ";
     s = creaWhere(s, v, true);
     s += getOrderQuery();
//      debug("s: "+s);
     this.setEnabled(false);
     mensaje("Espere, por favor ... buscando datos");
     Pprinc.setQuery(false);
     if (!dtCon1.select(s))
     {
       msgBox("No encontrados datos a visualizar con el filtro introducido");
       mensaje("");
       rgSelect();
       verDatos();
       activaTodo();
       this.setEnabled(true);
       return;
     }
     strSql = s;
     activaTodo();

     this.setEnabled(true);
     rgSelect();
     verDatos();
     mensaje("");
     mensajeErr("FILTRO CONSULTA ... ESTABLECIDO");
   }
   catch (Exception k)
   {
     Error("Error al buscar datos", k);
   }
   nav.pulsado = navegador.NINGUNO;
 }

 public void canc_query()
 {
   Pprinc.setQuery(false);
   activaTodo();
   verDatos();
   mensaje("");
   mensajeErr("Introducion FILTRO CONSULTA ... CANCELADO");
 }

 public void PADEdit()
 {
   try
   {
     if (!setBloqueo(dtAdd, "configuracion",
                   emp_codiE.getText() ))
     {
       activaTodo();
       msgBox(msgBloqueo);
       nav.pulsado = navegador.NINGUNO;
       return;
     }

   }
   catch (Exception k)
   {
     Error("Error al bloquear Registro", k);
   }

   activar(true);
   emp_codiE.setEnabled(false);
   Tpanel1.setSelectedIndex(0);
   mensaje("Modificando registro Actual ");
   cfg_almcomE.requestFocus();
 }

 public void ej_edit1()
 {
   try
   {
     selectRegPant(dtAdd, true);
     dtAdd.edit();
     actDatos(dtAdd);
     dtAdd.update(stUp);
     resetBloqueo(dtAdd, "configuracion",
                   emp_codiE.getText(),false);

     ctUp.commit();
     mensajeErr("REGISTRO ... MODIFICADO");
     mensaje("");
     activaTodo();
   }
   catch (Exception k)
   {
     Error("Error al EDITAR registro", k);
   }

 }

 public void canc_edit()
 {
   try {
     resetBloqueo(dtAdd, "configuracion",
                   emp_codiE.getText());
   } catch (Exception k)
   {
     Error("Error al quitar Bloqueo",k);
   }
   activaTodo();
   verDatos();
   mensaje("");
   mensajeErr("Edicion de registro ... CANCELADO");
 }

  @Override
 public void PADAddNew()
 {
   activar(true);
   mensaje("Insertando NUEVO registro");
   Pprinc.resetTexto();

   emp_codiE.setValorInt(EU.em_cod);
   Tpanel1.setSelectedIndex(0);
   cfg_caejauE.setSelected(true );
   emp_codiE.requestFocus();
 }
  @Override
 public boolean checkEdit()
 {
   return checkAddNew();
 }
    @Override
 public boolean checkAddNew()
 {
   try {
     Tpanel1.setSelectedIndex(0);
     if (emp_codiE.getError())
     {
       mensajeErr("Empresa NO es Valida");
       emp_codiE.requestFocus();
       return false;
     }
     if (cfg_almcomE.getError())
     {
       mensajeErr("Almacen de Compras NO valido");
       cfg_almcomE.requestFocus();
       return false;
     }
     if (cfg_almvenE.getError())
     {
       mensajeErr("Almacen de Ventas NO valido");
       cfg_almvenE.requestFocus();
       return false;
     }

     if (!emp_prvdesE.controlar())
     {
       mensajeErr(emp_prvdesE.getMsgError());
       return false;
     }
     if (!cli_codiE.controlar())
     {
       mensajeErr(cli_codiE.getMsgError());
       return false;
     }
     for (int n=0;n<3;n++)
     {
       for (int n1=0;n1<4;n1++)
       {
         if (camposDisc[n][n1].isNull())
         {
           mensajeErr("Campo de Discriminador NO puede estar vacio");
           Tpanel1.setSelectedIndex(1);
           camposDisc[n][n1].requestFocusLater();
           return false;
         }
       }
     }
     return true;
   } catch (Exception k)
   {
     Error("ERROR EN checkAddNew",k);
   }
   return false;
 }
 public void ej_addnew1()
 {
   try
   {
     if (selectRegPant(dtCon1,false))
     {
       mensajeErr("YA existe un registro CON esos datos de cabecera");
       emp_codiE.requestFocus();
       return;
     }
     dtAdd.addNew("configuracion");
     dtAdd.setDato("emp_codi", emp_codiE.getValorInt());

     actDatos(dtAdd);
     dtAdd.update(stUp);
     ctUp.commit();
     mensajeErr("REGISTRO ... INSERTADO");
     mensaje("");
     if (dtCons.getNOREG())
     {
       rgSelect();
       verDatos();
     }
     activaTodo();
   }
   catch (Exception k)
   {
     Error("Error al INSERTAR registro", k);
   }
 }

 private void actDatos(DatosTabla dt) throws SQLException,ParseException
 {
   dt.setDato("cfg_almcom", cfg_almcomE.getValorInt());
   dt.setDato("cfg_almven", cfg_almvenE.getValorInt());
   dt.setDato("cfg_numdec", cfg_numdecE.getValorInt());
   dt.setDato("cfg_desven", cfg_desvenE.isSelected()?-1:0);
   dt.setDato("cfg_palven", cfg_palvenE.isSelected()?-1:0);
   dt.setDato("cfg_tideve", cfg_tideveE.getValorInt());
   dt.setDato("emp_prvdes",emp_prvdesE.getValorInt());
   dt.setDato("cli_codi",cli_codiE.getValorInt());
   dt.setDato("cfg_caejau",cfg_caejauE.isSelected()?"S":"N");   
   dt.setDato("cfg_lialgr",cfg_lialgrC.getSelecion());
   dt.setDato("cfg_lifrgr",cfg_lifrgrC.getSelecion());
   dt.setDato("cfg_tipemp",cfg_tipempE.getValor());
   actDatos(dt,0,"pr");
   actDatos(dt,1,"cl");
   actDatos(dt,2,"pv");
 }
 void actDatos(DatosTabla dt,int p,String tabla) throws SQLException
 {
   for (int n = 0; n < 4; n++)
      dt.setDato("cfg_dis"+tabla+(n+1),camposDisc[p][n].getText());
 }
  @Override
 public void canc_addnew()
 {
   activaTodo();
   verDatos();
   mensaje("");
   mensajeErr("Insercion NUEVO registro ... CANCELADO");
 }

 public void PADDelete()
 {
   try
   {
       Error("Error al darle al delete",new Exception("prueba"));
     if (emp_codiE.getValorInt()==EU.em_cod)
     {
       mensajeErr("NO se puede borrar configurar de empresa actual");
       activaTodo();
       return;
     }
     if (!setBloqueo(dtAdd, "configuracion",
                   emp_codiE.getText()))
     {
       activaTodo();
       msgBox(msgBloqueo);
       nav.pulsado = navegador.NINGUNO;
       return;
     }
   }
   catch (Exception k)
   {
     Error("Error al bloquear Registro", k);
   }
   Baceptar.setEnabled(true);
   Bcancelar.setEnabled(true);
   mensaje("BORRANDO registro Actual ");
   Bcancelar.requestFocus();
 }


 public void ej_delete1()
 {
 try
 {
   selectRegPant(dtAdd, true);
   dtAdd.delete();
   resetBloqueo(dtAdd, "configuracion",
                   emp_codiE.getText(),false);
   ctUp.commit();
   mensaje("");
   rgSelect();
   verDatos();
   activaTodo();
   mensajeErr("REGISTRO ... BORRADO");
 }
 catch (Exception k)
 {
   Error("Error al BORRAR registro", k);
 }

 }

 public void canc_delete()
 {
   try
   {
     resetBloqueo(dtAdd, "configuracion", emp_codiE.getText());
   }
   catch (Exception k)
   {
     Error("Error al quitar Bloqueo", k);
   }
   activaTodo();
   mensaje("");
   mensajeErr("BORRADO de registro ... CANCELADO");
 }


 public void activar(boolean b)
 {
   emp_codiE.setEnabled(b);
   Pbasic.setEnabled(b);
   Pdiscr.setEnabled(b);
   Baceptar.setEnabled(b);
   Bcancelar.setEnabled(b);
 }
 String getOrderQuery()
 {
   return " ORDER BY emp_codi ";
 }

 public static String getNombreDiscr(int empCodi,String tipo,DatosTabla dt) throws SQLException
 {
   tipo=tipo.toUpperCase();
   if (tipo.length()!=2)
     return "DISCRIMINADOR: "+tipo+" NO ES DE DOS CARACTERES";
   if (tipo.startsWith("C"))
     tipo="cl"+ tipo.charAt(1);
   if (tipo.startsWith("A"))
     tipo="pr"+ tipo.charAt(1);
   if (tipo.startsWith("P"))
     tipo="pv"+ tipo.charAt(1);
   String s="SELECT cfg_dis"+tipo +" FROM configuracion WHERE emp_codi = "+empCodi;
   if (! dt.select(s))
     return "NO ENCONTRADA CONFIGURACION PARA ESTA EMPRESA";
   return dt.getString("cfg_dis"+tipo);
 }

 public static void llenaDiscr(DatosTabla dt, CLinkBox lkBox, String discr, int empCodi) throws
     SQLException
 {
   llenaDiscr(dt,lkBox,discr,empCodi,null);
 }
 /**
  * Devuelve el nombre para un codigo de discriminador
  * @param dt DatosTabla
  * @param discr Tipo de Discriminador
  * @param empCodi Empresa
  * @param disCodi COdigo de Discriminador
  * @return  Nombre del Discrim. Null si no lo encuentra
  * @throws SQLException
  */
 public static String getTextDiscrim(DatosTabla dt, String discr,int empCodi,String disCodi) throws SQLException
 {
     String s = "select dis_nomb from v_discrim " +
       " where emp_codi = " + empCodi +
       " and dis_codi =  '"+disCodi+"'"+
       " and dis_tipo='"+discr+"'";
   if (! dt.select(s))
       return null;
   return dt.getString("dis_nomb");
 }

 /**
  * LLena un linkbox con los Discriminadores marcados
  * @param dt DatosTabla
  * @param lkBox CLinkBox
  * @param discr String
  * @throws SQLException
  */
 public static void llenaDiscr(DatosTabla dt,CLinkBox lkBox, String discr,int empCodi,String limitador) throws SQLException
 {
   String s = "select dis_codi,dis_nomb from v_discrim " +
       " where emp_codi = " + empCodi +
        (limitador==null?"":" and dis_codi like '"+limitador+"'")+
       " and dis_tipo='"+discr+"' order by dis_codi";
   dt.select(s);
   lkBox.addDatos(dt);
 }
 public static boolean getConfiguracion(int empCodi,DatosTabla dt, vlike lk) throws SQLException
 {
   String s = "select * from configuracion where emp_codi = " + empCodi;
   return dt.selectInto(s, lk);
 }
 public static boolean getConfiguracion(int empCodi,DatosTabla dt) throws SQLException
 {
   String s = "select * from configuracion where emp_codi = " + empCodi;
   return dt.select(s);
 }

    
 /**
  * Devuelve el codigo del proveedor para despieces
  * @param empCodi Empresa
  * @param dt DatosTabla
  * @return
  * @throws SQLException
  */
 public static int getPrvDespiece(int empCodi,DatosTabla dt) throws SQLException
 {
   if (! getConfiguracion(empCodi,dt))
     throw new SQLException("Configuración para empresa: " + empCodi + " NO encontrada");
   return dt.getInt("emp_prvdes");
 }
 public static String getCfgLialgr(int empCodi,DatosTabla dt) throws SQLException
 {
   if (! getConfiguracion(empCodi,dt))
     throw new SQLException("Configuración para empresa: " + empCodi + " NO encontrada");
   return dt.getString("cfg_lialgr");
 }
 public static String getCfgLifrgr(int empCodi,DatosTabla dt) throws SQLException
 {
   if (! getConfiguracion(empCodi,dt))
     throw new SQLException("Configuración para empresa: " + empCodi + " NO encontrada");
   return dt.getString("cfg_lifrgr");
 }
 /**
  * Devuelve el Almacen de ventas por defecto
  * @param empCodi
  * @param dt
  * @return Codigo Almacen de ventas. Exception si no hay registro para la empresa
  * @throws java.sql.SQLException
  */
public static int getAlmVentas(int empCodi,DatosTabla dt) throws SQLException
 {
   if (! getConfiguracion(empCodi,dt))
     throw new SQLException("Configuración para empresa: " + empCodi + " NO encontrada");
   return dt.getInt("cfg_almven");
 }
 public static int getTipoEmpresa(int empCodi,DatosTabla dt) throws SQLException
 {
   if (! getConfiguracion(empCodi,dt))
     throw new SQLException("Configuración para empresa: " + empCodi + " NO encontrada");
   return dt.getInt("cfg_tipemp");
 }
 public static boolean getUsaPalets(int empCodi,DatosTabla dt) throws SQLException
 {
   if (! getConfiguracion(empCodi,dt))
     throw new SQLException("Configuración para empresa: " + empCodi + " NO encontrada");
   return dt.getInt("cfg_palven")!=0;
 }
}
