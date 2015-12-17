package gnu.chu.anjelica.riesgos;

import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import gnu.chu.sql.*;
import java.sql.*;
import gnu.chu.Menu.*;
import java.awt.*;
import java.util.*;
import javax.swing.BorderFactory;
import gnu.chu.interfaces.*;
import java.awt.event.*;
import java.text.*;
import java.util.Date;
/**

 * <p>Título: cacobrea</p>
 * <p>Descripcion: Apunta los cobros realizados en la ruta. Realizando los apuntes
 * necesarios en riesgos. <br>
*  Este programa se complementa con pdcobruta. Primero se utilizara ese y despues,
*  cuando los representantes vuelvan con los cobros realizados se utilizara este para
*  realizar los apuntes</p>
* <p>Copyright: Copyright (c) 2005-2015</p>
*  <p>Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
*  los términos de la Licencia Pública General de GNU según es publicada por
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
* <p>Empresa: MISL</p>
* @author Chuchi P
* @version 1.0
* @version 1.1 (01-02-2009) Se corrige error que cuando se anulaba un cobro
 *  daba un error pues comprobaba la fecha de cobro en un campo equivocado
*/
public class cacobrea extends ventanaPad implements PAD
{
  String s;
  CPanel Pprinc = new CPanel();
  CPanel Pcabe = new CPanel();
  CLabel cLabel1 = new CLabel();
  CTextField cor_fechaE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel cLabel2 = new CLabel();
  CTextField usu_nombE = new CTextField(Types.CHAR,"X",15);
  CLabel cLabel3 = new CLabel();
  CLinkBox rut_codiE = new CLinkBox();
  CLabel cLabel4 = new CLabel();
  CTextField cor_ordenE = new CTextField(Types.DECIMAL,"#9");
  CCheckBox cor_totcobE = new CCheckBox("S","N");

  CButton BirGrid = new CButton();

  CGridEditable jt = new CGridEditable(14)
  {
    @Override
    public void cambiaColumna(int col,int colNueva, int row)
    {
      if (col==9)
        ponValDefecto(row);
      if (col==10)
      {
        if (!cor_tipcobE.getText().equals(""))
        {
          cor_totcobE.setSelected(fvc_impcobE.getValorDec() >=
                                  jt.getValorDec(row,8));
          jt.setValor(fvc_impcobE.getValorDec() >=
              jt.getValorDec(row,8),row,12);
        }
      }
    }
    @Override
    public void afterCambiaLinea()
    {
      calcSumFras();
    }
    @Override
    public int cambiaLinea(int row, int col)
    {
      int val;
      if ((val=checkLinea(row))>0)
        return val;
      calcSumFras();
//      if (! cor_tipcobE.getText().equals(""))
//        ponValDefecto(row);
      return -1;
    }
  };
//  CButton Baceptar = new CButton();
//  CButton Bcancelar = new CButton();
  CTextField fvc_anoE = new CTextField(Types.DECIMAL,"###9");
  CTextField fvc_serieE=new CTextField(Types.CHAR,"X",1);
  CTextField emp_codiE = new CTextField(Types.DECIMAL,"#9");
  CTextField fvc_numeE = new CTextField(Types.DECIMAL,"####9");
  CTextField cli_codiE = new CTextField();
  CTextField cli_nombE = new CTextField();
  CTextField fvc_fecfraE = new CTextField();
  CTextField fvc_sumtotE = new CTextField();
  CTextField fvc_imppenE = new CTextField();
  CPanel Pacum = new CPanel();
  CLabel cLabel5 = new CLabel();
  CTextField numFrasE = new CTextField(Types.DECIMAL,"##9");
  CLabel cLabel6 = new CLabel();
  CTextField impFrasE = new CTextField(Types.DECIMAL,"--,---,--9.99");
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CTextField cor_tipcobE = new CTextField(Types.CHAR,"?",1);
  CTextField fvc_impcobE = new CTextField(Types.DECIMAL, "----,--9.99");
  CTextField cor_fecvtoE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CTextField cor_comentE = new CTextField(Types.CHAR, "X", 30);
  CLabel cLabel7 = new CLabel();
  CTextField cor_feccobE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel cLabel8 = new CLabel();
  CComboBox cor_intcobE = new CComboBox();
  CLabel cLabel9 = new CLabel();
  CTextField numTalonE = new CTextField(Types.DECIMAL,"##9");
  CTextField impTalonE = new CTextField(Types.DECIMAL,"--,---,--9.99");
  CLabel cLabel10 = new CLabel();
  CTextField numEfectE = new CTextField(Types.DECIMAL,"##9");
  CTextField impEfectE = new CTextField(Types.DECIMAL,"--,---,--9.99");
  CTextField impCobraE = new CTextField(Types.DECIMAL,"--,---,--9.99");
  CTextField numCobraE = new CTextField(Types.DECIMAL,"##9");
  CLabel cLabel11 = new CLabel();

  public cacobrea(EntornoUsuario eu, Principal p)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    setTitulo("Carga Cobros Realizados");

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

 public cacobrea(gnu.chu.anjelica.menu p, EntornoUsuario eu)
 {
   EU = eu;
   vl = p.getLayeredPane();
   setTitulo("Carga Cobros  Realizados");
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
   this.setSize(new Dimension(589, 564));
   this.setVersion("2015-12-16");
   Pprinc.setLayout(gridBagLayout1);
   statusBar = new StatusBar(this);
   nav = new navegador(this, dtCons, false, navegador.NORMAL);
   nav.removeBoton(navegador.DELETE);
   nav.removeBoton(navegador.ADDNEW);
   strSql = "select cor_fecha,usu_nomb,zon_codi,cor_orden " +
       "  from factruta " +
       " WHERE cor_intcob = 'N' "+
       " group by  cor_fecha,usu_nomb,zon_codi,cor_orden " +
       " order by  cor_fecha,usu_nomb,zon_codi,cor_orden";

   conecta();
   iniciar(this);

    Pcabe.setBorder(BorderFactory.createRaisedBevelBorder());
    Pcabe.setMaximumSize(new Dimension(511, 48));
    Pcabe.setMinimumSize(new Dimension(511, 48));
    Pcabe.setPreferredSize(new Dimension(511, 48));
    Pcabe.setLayout(null);
    cLabel1.setText("Fecha");
    cLabel1.setBounds(new Rectangle(10, 4, 39, 18));
    cLabel2.setText("Operario");
    cLabel2.setBounds(new Rectangle(152, 4, 57, 18));
    cLabel3.setText("Ruta");
    cLabel3.setBounds(new Rectangle(16, 26, 33, 18));
    rut_codiE.setAncTexto(30);
    rut_codiE.setBounds(new Rectangle(54, 26, 191, 18));
    cLabel4.setText("Orden");
    cLabel4.setBounds(new Rectangle(252, 26, 41, 18));
    ArrayList v= new ArrayList();

    v.add("Año"); // 0
    v.add("Emp"); // 1
    v.add("S"); //2
    v.add("Factura");  // 3
    v.add("Cliente"); // 4
    v.add("Nombre"); // 5
    v.add("Fec.Fra");// 6
    v.add("Imp.Fra"); // 7
    v.add("Imp.Pend"); // 8
    v.add("T.C"); // 9
    v.add("Imp.Cob."); // 10
    v.add("Fec.Vto"); // 11
    v.add("Cob"); // 12
    v.add("Coment."); // 13
    cor_ordenE.setEnabled(false);
    cor_ordenE.setBounds(new Rectangle(294, 26, 33, 18));
    jt.setCabecera(v);

    fvc_anoE.setEnabled(false);
    emp_codiE.setEnabled(false);
    fvc_serieE.setEnabled(false);
    fvc_numeE.setEnabled(false);
    cli_codiE.setEnabled(false);
    cli_nombE.setEnabled(false);
    fvc_fecfraE.setEnabled(false);
    fvc_sumtotE.setEnabled(false);
    fvc_imppenE.setEnabled(false);
    cor_tipcobE.setMayusc(true);

    cor_tipcobE.setAdmiteCar(1);
    cor_tipcobE.setStrCarEsp("TE");

    emp_codiE.setValorDec(EU.em_cod);
    fvc_anoE.setValorDec(EU.ejercicio);
    fvc_serieE.setMayusc(true);
    fvc_serieE.setAutoNext(true);
    ArrayList v1=new ArrayList();
    v1.add(fvc_anoE); // 0
    v1.add(emp_codiE); // 1
    v1.add(fvc_serieE); // 2
    v1.add(fvc_numeE); // 3
    v1.add(cli_codiE); // 4
    v1.add(cli_nombE); // 5
    v1.add(fvc_fecfraE); // 6
    v1.add(fvc_sumtotE); // 7
    v1.add(fvc_imppenE); // 8
    v1.add(cor_tipcobE); // 9
    v1.add(fvc_impcobE); // 10
    v1.add(cor_fecvtoE); // 11
    v1.add(cor_totcobE); // 12
    v1.add(cor_comentE); // 13

    jt.setMaximumSize(new Dimension(406, 311));
    jt.setMinimumSize(new Dimension(406, 311));
    jt.setPreferredSize(new Dimension(406, 311));
    jt.setAnchoColumna(new int[]{40,20,15,45,40,140,80,60,60,25,60,80,30,100});
    jt.setAlinearColumna(new int[]{2,2,1,2,2,0,1,2,2,1,2,1,1,0});
    jt.setFormatoColumna(12,"BSN");
    jt.setCampos(v1);
//    jt.ajustar(true);
    jt.setFormatoColumna(7,"----,--9.99");
    jt.setFormatoColumna(8,"----,--9.99");
    jt.setFormatoColumna(10,"----,--9.99");
    jt.setCanDeleteLinea(false);
    jt.setCanInsertLinea(false);
    Baceptar.setMaximumSize(new Dimension(116, 26));
    Baceptar.setMinimumSize(new Dimension(116, 26));
    Baceptar.setPreferredSize(new Dimension(116, 26));
    Baceptar.setMargin(new Insets(0, 0, 0, 0));
    Baceptar.setText("Aceptar F4");
    Bcancelar.setMaximumSize(new Dimension(116, 26));
    Bcancelar.setMinimumSize(new Dimension(116, 26));
    Bcancelar.setPreferredSize(new Dimension(116, 26));
    Bcancelar.setMargin(new Insets(0, 0, 0, 0));
//    Bcancelar.setText("Cancelar");
    Pacum.setBorder(BorderFactory.createLoweredBevelBorder());

    Pacum.setMaximumSize(new Dimension(490, 45));
    Pacum.setMinimumSize(new Dimension(490, 45));
    Pacum.setPreferredSize(new Dimension(490, 45));
    Pacum.setLayout(null);
    cLabel5.setText("N.Fras");
    cLabel5.setBounds(new Rectangle(152, 4, 46, 17));
    numFrasE.setBounds(new Rectangle(204, 4, 33, 17));
    cLabel6.setToolTipText("");
    cLabel6.setText("Imp. Fras");
    cLabel6.setBounds(new Rectangle(272, 4, 54, 17));
    impFrasE.setBounds(new Rectangle(333, 4, 75, 17));
    BirGrid.setBounds(new Rectangle(334, 32, 14, 10));
    usu_nombE.setBounds(new Rectangle(216, 4, 145, 18));
    cor_fechaE.setBounds(new Rectangle(56, 4, 84, 18));
    cLabel7.setText("Fec.Cobro");
    cLabel7.setBounds(new Rectangle(363, 4, 56, 16));
    cor_feccobE.setBounds(new Rectangle(419, 4, 84, 18));
    cLabel8.setText("Ver");
    cLabel8.setBounds(new Rectangle(372, 26, 27, 14));
    cor_intcobE.setText("cComboBox1");
    cor_intcobE.setBounds(new Rectangle(400, 24, 101, 18));
    cLabel9.setText("Talones");
    cLabel9.setBounds(new Rectangle(4, 25, 47, 18));
    numTalonE.setEnabled(false);
    numTalonE.setBounds(new Rectangle(55, 25, 33, 17));
    impTalonE.setEnabled(false);
    impTalonE.setBounds(new Rectangle(91, 25, 75, 17));
    cLabel10.setText("Efectivo");
    cLabel10.setBounds(new Rectangle(175, 25, 47, 18));
    numEfectE.setBounds(new Rectangle(219, 25, 33, 17));
    numEfectE.setEnabled(false);
    impEfectE.setBounds(new Rectangle(255, 25, 75, 17));
    impEfectE.setEnabled(false);
    impCobraE.setEnabled(false);
    impCobraE.setBounds(new Rectangle(405, 25, 75, 17));
    numCobraE.setEnabled(false);
    numCobraE.setBounds(new Rectangle(369, 25, 33, 17));
    cLabel11.setBounds(new Rectangle(335, 25, 35, 18));
    cLabel11.setText("Total");
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);

    Pprinc.add(Pcabe,      new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 0, 0));
    Pcabe.add(rut_codiE, null);
    Pcabe.add(cLabel3, null);
    Pcabe.add(usu_nombE, null);
    Pcabe.add(cor_fechaE, null);
    Pcabe.add(cLabel1, null);
    Pcabe.add(cLabel2, null);
    Pcabe.add(cLabel7, null);
    Pcabe.add(cor_feccobE, null);
    Pcabe.add(cor_ordenE, null);
    Pcabe.add(cLabel4, null);
    Pcabe.add(cor_intcobE, null);
    Pcabe.add(cLabel8, null);
    Pcabe.add(BirGrid, null);
    Pprinc.add(jt,    new GridBagConstraints(0, 1, 3, 1, 1.0, 1.0
             ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 0, 0, 0), 0, 0));
    Pprinc.add(Baceptar,    new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 55, 0, 0), 0, 0));
    Pprinc.add(Bcancelar,    new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 55), 0, 0));
    Pprinc.add(Pacum,    new GridBagConstraints(0, 3, 3, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));
    Pacum.add(cLabel9, null);
    Pacum.add(numTalonE, null);
    Pacum.add(cLabel10, null);
    Pacum.add(numEfectE, null);
    Pacum.add(impEfectE, null);
    Pacum.add(impTalonE, null);
    Pacum.add(cLabel11, null);
    Pacum.add(numCobraE, null);
    Pacum.add(impCobraE, null);
    Pacum.add(impFrasE, null);
    Pacum.add(cLabel5, null);
    Pacum.add(numFrasE, null);
    Pacum.add(cLabel6, null);
    impFrasE.setEnabled(false);
    numFrasE.setEnabled(false);
 }
 public void iniciarVentana() throws Exception
 {
   Pcabe.setDefButton(Baceptar);
   Pcabe.setButton(KeyEvent.VK_F4,Baceptar);
   cor_intcobE.addItem("Sin Comp.","S");
   cor_intcobE.addItem("Comprobado","C");
   cor_intcobE.setColumnaAlias("cor_intcob");

   jt.setDefButton(Baceptar);
   jt.setButton(KeyEvent.VK_F4,Baceptar);
   cor_fechaE.setColumnaAlias("cor_fecha");
   usu_nombE.setColumnaAlias("usu_nomb");
   rut_codiE.setColumnaAlias("zon_codi");
   rut_codiE.setFormato(Types.CHAR, "XX", 2);
   rut_codiE.texto.setMayusc(true);
   rut_codiE.setFormato(true);

   gnu.chu.anjelica.pad.pdconfig.llenaDiscr(dtCon1,rut_codiE,
       gnu.chu.anjelica.pad.pdconfig.D_RUTAS,EU.em_cod);

   activarEventos();
   verDatos(dtCons);
 }
 void activarEventos()
 {
   BirGrid.addFocusListener(new FocusAdapter() {
     @Override
     public void focusGained(FocusEvent e) {
       BirGrid_focusGained();
     }
   });
 }


 void BirGrid_focusGained()
 {
     jt.setEnabled(true);
     jt.requestFocusInicio();
 }
 @Override
  public void PADPrimero() {
    verDatos(dtCons);
  }
  @Override
  public void PADAnterior() {
    verDatos(dtCons);
  }
  @Override
  public void PADSiguiente() {
    verDatos(dtCons);
  }
  @Override
  public void PADUltimo() {
    verDatos(dtCons);
  }
  @Override
  public void PADQuery() {
    activar(true);
    Pcabe.setQuery(true);
    Pcabe.resetTexto();
    cor_feccobE.setEnabled(false);
    BirGrid.setEnabled(false);
    jt.setEnabled(false);
    mensaje("Introduzca Condiciones de Busqueda");
    cor_fechaE.requestFocus();
  }
  @Override
  public void ej_query1()
  {
    try
    {
      Component c = Pcabe.getErrorConf();
      if (c != null)
      {
        mensajeErr("Condiciones de Busqueda NO validas");
        c.requestFocus();
        return;
      }
      Vector v = new Vector();
      v.addElement(cor_fechaE.getStrQuery());
      v.addElement(usu_nombE.getStrQuery());
      v.addElement(rut_codiE.getStrQuery());
      v.addElement(cor_intcobE.getStrQuery());
      s = "select cor_fecha,usu_nomb,zon_codi,cor_orden " +
          "  from factruta ";
      s = creaWhere(s, v, true);
      s += " group by  cor_fecha,usu_nomb,zon_codi,cor_orden " +
          " order by  cor_fecha,usu_nomb,zon_codi,cor_orden";

      this.setEnabled(false);
      mensaje("Espere, por favor ... buscando datos");
      if (!dtCon1.select(s))
      {
        msgBox("No encontrados Cobros con estos criterios");
        rgSelect();
        activaTodo();
        verDatos(dtCons);
        this.setEnabled(true);
        return;
      }
      strSql = s;
      rgSelect();
      verDatos(dtCons);
      this.setEnabled(true);
      mensaje("");
      mensajeErr("Nuevas Condiciones ... Establecidas");
    }
    catch (Exception k)
    {
      Error("Error al buscar datos", k);
    }
    activaTodo();

  }
  @Override
  public void canc_query() {
    Pcabe.setQuery(false);
    activaTodo();
    verDatos(dtCons);
    mensaje("");
    mensajeErr("Consulta ... Cancelada");
  }
  @Override
  public void PADEdit() {
    if (dtCons.getNOREG())
    {
      activaTodo();
      mensajeErr("No hay registros para editar");
      return;
    }
    activar(true,navegador.EDIT);
    cor_feccobE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
    jt.requestFocusInicio();
    mensaje("Modificacion de Facturas entregadas");
  }

  public void ej_edit1()
  {
    if (!checkCab())
      return;
    if (checkLinea(jt.getSelectedRow())>=0)
    {
      jt.requestFocusSelected();
      return;
    }
    try
    {
      mensaje("Espere, por favor ... Insertando datos");

      jt.procesaAllFoco();
      this.setEnabled(false);
      insLinFac();
      ctUp.commit();
    }
    catch (Exception k)
    {
      Error("Error al Modificar datos", k);
    }
    this.setEnabled(true);
    activaTodo();
    mensaje("");
    mensajeErr("Datos ... Modificados");
  }

  private void borFactur() throws SQLException, ParseException
  {
    s="DELETE FROM factruta "+
        " WHERE cor_fecha = TO_DATE('"+dtCons.getFecha("cor_fecha","dd-MM-yyyy")+"','dd-MM-yyyy') "+
        " and usu_nomb  = '" + dtCons.getString("usu_nomb") + "'" +
        " and zon_codi = '" + dtCons.getString("zon_codi") + "'" +
        " and cor_orden = " + dtCons.getInt("cor_orden") ;
    dtAdd.setSql(s);
    stUp.executeUpdate(dtAdd.getStrSelect());
  }

  void insLinFac () throws Exception
  {
    int nRow = jt.getRowCount();
    boolean swCobrad;
    double impCobra=0;
//    boolean totCobra=true;

    for (int n = 0; n < nRow; n++)
    {
      s="SELECT * FROM factruta "+
          " WHERE cor_fecha = TO_DATE('"+dtCons.getFecha("cor_fecha","dd-MM-yyyy")+"','dd-MM-yyyy') "+
          " and usu_nomb  = '" + dtCons.getString("usu_nomb") + "'" +
          " and zon_codi = '" + dtCons.getString("zon_codi") + "'" +
          " and cor_orden = " + dtCons.getInt("cor_orden") +
          " and fvc_serie = '"+jt.getValString(n,2)+"'"+
          " and fvc_ano = "+jt.getValorInt(n,0)+
          " and emp_codi = "+jt.getValorInt(n,1)+
          " and fvc_nume = "+jt.getValorInt(n,3);
      if (! dtAdd.select(s,true))
      {
        msgBox("NO ENCONTRADA FACTURA en factruta: "+jt.getValInt(n,2));
        continue;
      }
      impCobra=dtAdd.getDouble("fvc_impcob");
      dtAdd.edit(dtAdd.getCondWhere());
      dtAdd.setDato("cor_tipcob", jt.getValString(n,9));
      dtAdd.setDato("fvc_impcob", jt.getValorDec(n,10));
//      if (! jt.getValString(n,10).trim().equals(""))
        dtAdd.setDato("cor_fecvto",jt.getValString(n,11), "dd-MM-yyyy");
      dtAdd.setDato("cor_totcob",jt.getValBoolean(n,12)?"S":"N");
      dtAdd.setDato("cor_coment", jt.getValString(n,13));
      dtAdd.setDato("cor_intcob","C");
      dtAdd.update(stUp);
      swCobrad=false;
      if (jt.getValorDec(n,10)!=0 && !jt.getValString(n,9).equals(""))
        swCobrad=true;
      // Actualizar cobros
      s="select * from v_cobros where cob_anofac= "+jt.getValorInt(n,0)+
           " and fvc_serie = '"+jt.getValorInt(n,2)+"'"+
          " and fac_nume = "+jt.getValorInt(n,3)+
          " and emp_codi = "+jt.getValorInt(n,1);
      dtAdd.select(s,true);
      if (swCobrad)
      {// Cobrado
        if (dtAdd.getNOREG())
          insCobro(n);           // No encontrado cobro
        else
          updCobro(n);
      }
      else
      {
        if (! dtAdd.getNOREG())
        {
          if (Formatear.restaDias(dtAdd.getFecha("cob_feccob","dd-MM-yyyy"),
                                  cor_feccobE.getText())==0)
            stUp.executeUpdate("DELETE FROM v_cobros where "+dtAdd.getCondWhere());
        }
      }
/*
      s="select sum(cob_impor) as cob_impor from v_cobros where cob_anofac= "+jt.getValInt(n,0)+
          " and fac_nume = " + jt.getValInt(n, 2) +
          " and emp_codi = " + jt.getValInt(n, 1);
      dtAdd.select(s);
*/
      // Actualizar v_facturas
      s = "SELECT * FROM v_facvec  " +
          "  WHERE emp_codi = " + jt.getValorInt(n, 1) +
          " and fvc_ano = " + jt.getValorInt(n, 0) +
          " and fvc_nume = " + jt.getValorInt(n, 2);
      if (!dtAdd.select(s,true))
        throw new SQLException("NO encontrada factura en fact. Ventas "+ jt.getValorInt(n, 2));
      dtAdd.edit(dtAdd.getCondWhere());
      dtAdd.setDato("fvc_impcob",dtAdd.getDouble("fvc_impcob") -
                    impCobra + jt.getValorDec(n,10));
      dtAdd.setDato("fvc_cobrad",jt.getValBoolean(n,11)?-1:0);
      dtAdd.update(stUp);
    }
  }

  void insCobro(int n) throws Exception
  {
    dtAdd.addNew("v_cobros");
    dtAdd.setDato("cob_ano",jt.getValInt(n,0));
    dtAdd.setDato("emp_codi",jt.getValInt(n,1));
    dtAdd.setDato("cob_serie","Z");
    dtAdd.setDato("fvc_serie",jt.getValString(n,2));
    dtAdd.setDato("alb_nume",0);
    dtAdd.setDato("cob_anofac",jt.getValInt(n,0));
    dtAdd.setDato("fac_nume",jt.getValInt(n,3));
    actCobro(n);
    dtAdd.update(stUp);
  }
  void updCobro(int n) throws Exception
  {
    dtAdd.edit(dtAdd.getCondWhere());
    actCobro(n);
    dtAdd.update(stUp);
  }
  void actCobro(int n) throws Exception
  {
    dtAdd.setDato("tpc_codi",jt.getValString(n,9).equals("E")?1:2);
    dtAdd.setDato("usu_nomb", EU.usuario);
    dtAdd.setDato("cob_feccob", cor_feccobE.getText(), "dd-MM-yyyy");
//    dtAdd.setDato("cob_horcob",cor_fechaE.getText(),"dd-MM-yyyy");
    dtAdd.setDato("cob_horcob",(Date) null);
    dtAdd.setDato("cob_obser", jt.getValString(n, 13));
    dtAdd.setDato("cob_trasp", 0);
    dtAdd.setDato("cob_fecvto", jt.getValString(n, 11), "dd-MM-yyyy");
    dtAdd.setDato("cob_impor", jt.getValorDec(n, 10));
  }
  public void canc_edit()
  {
    activaTodo();
    mensaje("");
    mensajeErr("Modificacion de Facturas ... Cancelada");
    verDatos(dtCons);
  }
  public void PADAddNew()
  {
  }
  public void ej_addnew1() {
  }
  /**
   * Comprobar la cabecera de la factura.
   * @return false si hay error.
   *         true si todo va bien.
   */
  boolean checkCab()
  {
    if (cor_feccobE.getError() || cor_feccobE.isNull())
    {
      mensajeErr("Introduzca una fecha Valida");
      cor_fechaE.requestFocus();
      return false;
    }
    return true;
  }
  @Override
  public void canc_addnew() {
  }
  @Override
  public void PADDelete() {
  }
  @Override
  public void ej_delete1() {
  }
  @Override
  public void canc_delete() {
  }
@Override
  public void activar(boolean b)
  {
    activar(b,navegador.QUERY);
  }
  public void activar(boolean b,int modo)
  {
    BirGrid.setEnabled(b);
    cor_feccobE.setEnabled(b);
    Baceptar.setEnabled(b);
    Bcancelar.setEnabled(b);
    jt.setEnabled(b);

    if (modo==navegador.EDIT)
      return;
    cor_intcobE.setEnabled(b);
    cor_fechaE.setEnabled(b);
    usu_nombE.setEnabled(b);
    rut_codiE.setEnabled(b);

  }

  void verDatos(DatosTabla dt)
  {
    try
    {
      if (dtCons.getNOREG())
        return;
      cor_fechaE.setText(dt.getFecha("cor_fecha","dd-MM-yyyy"));
      usu_nombE.setText(dt.getString("usu_nomb"));
      rut_codiE.setText(dt.getString("zon_codi"));
      cor_ordenE.setValorDec(dt.getInt("cor_orden"));
      s="select f.*,c.cli_codi,c.cli_nomb,fr.fvc_fecfra "+
          " from factruta f,clientes c,v_facvec fr "+
          " WHERE cor_fecha = TO_DATE('"+dt.getFecha("cor_fecha","dd-MM-yyyy")+"','dd-MM-yyyy') "+
          " and usu_nomb  = '"+dt.getString("usu_nomb")+"'"+
          " and f.zon_codi = '"+dt.getString("zon_codi")+"'"+
          " and cor_orden = "+dt.getInt("cor_orden")+
          " and c.cli_codi = fr.cli_codi "+
          " and fr.fvc_ano = f.fvc_ano "+
          " and fr.emp_codi = f.emp_codi "+
          " and fr.fvc_nume = f.fvc_nume "+
          " order by c.cli_codi,f.fvc_ano,f.emp_codi,f.fvc_nume";
      jt.setEnabled(false);
      numFrasE.setValorDec(0);
      impFrasE.setValorDec(0);

      jt.removeAllDatos();
      if (! dtCon1.select(s))
      {
        mensajeErr("No encontrados DATOS de Cobros");
        return;
      }
      int nFras=0,nTal=0,nEfe=0;
      double impFras=0, impTal=0,impEfe=0;
      do
      {
        Vector v=new Vector();
        v.addElement(dtCon1.getString("fvc_ano"));
        v.addElement(dtCon1.getString("emp_codi"));
        v.addElement(dtCon1.getString("fvc_serie"));
        v.addElement(dtCon1.getString("fvc_nume"));
        v.addElement(dtCon1.getString("cli_codi"));
        v.addElement(dtCon1.getString("cli_nomb"));
        v.addElement(dtCon1.getFecha("fvc_fecfra","dd-MM-yyyy"));
        v.addElement(dtCon1.getString("fvc_sumtot"));
        v.addElement(dtCon1.getString("fvc_imppen"));
        v.addElement(dtCon1.getString("cor_tipcob"));
        v.addElement(dtCon1.getString("fvc_impcob"));
        v.addElement(dtCon1.getFecha("cor_fecvto","dd-MM-yyyy"));
        v.addElement(dtCon1.getString("cor_totcob"));
        v.addElement(dtCon1.getString("cor_coment"));
        jt.addLinea(v);
        nFras++;
        impFras+=dtCon1.getDouble("fvc_imppen");
        if (dtCon1.getString("cor_tipcob").equals("E"))
        {
          nEfe++;
          impEfe+=dtCon1.getDouble("fvc_impcob");
        }
        if (dtCon1.getString("cor_tipcob").equals("T"))
        {
          nTal++;
          impTal += dtCon1.getDouble("fvc_impcob");
        }
      } while (dtCon1.next());
      numFrasE.setValorDec(nFras);
      impFrasE.setValorDec(impFras);
      numTalonE.setValorDec(nTal);
      impTalonE.setValorDec(impTal);
      numEfectE.setValorDec(nEfe);
      impEfectE.setValorDec(impEfe);
      numCobraE.setValorDec(nEfe+nTal);
      impCobraE.setValorDec(impEfe+impTal);
    } catch (Exception k)
    {
      Error("Error al ver datos",k);
    }
  }
  void calcSumFras()
  {
    int nRow=jt.getRowCount();
    int nFras=0,nTal=0,nEfe=0;
    double impFras=0, impTal=0,impEfe=0;


    for (int n=0;n<nRow;n++)
    {
      if (jt.getValorInt(n, 3) == 0)
        continue;
      nFras++;
      impFras+=jt.getValorDec(n,8);
      if (jt.getValString(n,9).equals("E"))
      {
        nEfe++;
        impEfe+=jt.getValorDec(n,10);
      }
      if (jt.getValString(n, 9).equals("T"))
      {
        nTal++;
        impTal += jt.getValorDec(n, 10);
      }

    }
    numFrasE.setValorDec(nFras);
    impFrasE.setValorDec(impFras);
    numTalonE.setValorDec(nTal);
    impTalonE.setValorDec(impTal);
    numEfectE.setValorDec(nEfe);
    impEfectE.setValorDec(impEfe);
    numCobraE.setValorDec(nEfe+nTal);
    impCobraE.setValorDec(impEfe+impTal);

  }
  void ponValDefecto(int row)
  {
    if (!cor_tipcobE.getText().equals(""))
    {
      if (jt.getValorDec(row, 10) == 0)
      {
        jt.setValor("" + jt.getValorDec(row, 8), row, 10);
        fvc_impcobE.setValorDec(jt.getValorDec(row, 8));
      }
      if (jt.getValString(row, 11, true).equals("") || jt.getValString(row,10,true).equals("01-01-1900"))
      {
        jt.setValor(cor_feccobE.getText(), row, 11);
        cor_fecvtoE.setText(cor_feccobE.getText());
      }
      cor_totcobE.setSelected(fvc_impcobE.getValorDec() >=
                              jt.getValorDec(row, 8));
      jt.setValor((fvc_impcobE.getValorDec() >=
          jt.getValorDec(row, 8)), row, 12);
    }
    else
    {
      cor_totcobE.setSelected(false);
      jt.setValor(false, row, 12);
      jt.setValor("0", row, 10);
      fvc_impcobE.setValorDec(0);
      jt.setValor("", row, 11);
      cor_fecvtoE.setText("");
    }

  }

  int checkLinea(int row)
  {
    if (cor_fecvtoE.getError())
    {
      mensajeErr("Fecha Vto. NO VALIDA");
      return 10;
    }
    if (fvc_impcobE.getValorDec()==0)
      return -1;
    if (cor_fecvtoE.isNull())
    {
      cor_fecvtoE.setText(cor_feccobE.getText());
      mensajeErr("Introduzca la Fecha de Vto");
      return 10;
    }
    if (cor_tipcobE.getText().equals(""))
    {
      mensajeErr("Tipo de Cobro NO valido");
      return 8;
    }
    return -1;
  }
}
