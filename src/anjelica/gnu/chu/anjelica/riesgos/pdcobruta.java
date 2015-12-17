package gnu.chu.anjelica.riesgos;

import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import gnu.chu.sql.*;
import java.sql.*;
import gnu.chu.Menu.*;
import gnu.chu.anjelica.pad.pdconfig;
import java.awt.*;
import java.util.*;
import javax.swing.BorderFactory;
import gnu.chu.interfaces.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.text.*;
import net.sf.jasperreports.engine.*;
/**
 *
 * <p>Titulo: pdcobruta </p>
* <p>Descripcion: Carga de Cobros idos a realizar.
*  Utilizado para meter las facturas que un representante o
*  repartidor se lleva a una ruta. Permite sacar la hoja que se entregara al
*  repartidor/representante con la relacion de facturas entregadas</p>
 * <p>Copyright: Copyright (c) 2005-2009
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
 * <p>Empresa: miSL</p>
* @author Chuchi P
* @version 1.0
*/
public class pdcobruta extends ventanaPad implements PAD
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
  CButton BirGrid = new CButton();

  CGridEditable jt = new CGridEditable(9)
  {
    public void afterCambiaLinea()
    {
      calcSumFras();
    }

    public void cambiaColumna(int col,int colNueva, int row)
    {
      try
      {
        if (col != 3)
          return;
        if (fvc_anoE.getValorInt() > 0 && emp_codiE.getValorInt() > 0
            && fvc_numeE.getValorInt() > 0)
        {
          if (buscaFac(fvc_anoE.getValorInt(), emp_codiE.getValorInt(),
                       fvc_serieE.getText(), fvc_numeE.getValorInt()))
          {
            jt.setValor(dtStat.getString("cli_codi"), row, 4);
            jt.setValor(dtStat.getString("cli_nomb"), row, 5);
            jt.setValor(dtStat.getFecha("fvc_fecfra", "dd-MM-yyyy"), row, 6);
            jt.setValor(dtStat.getString("fvc_sumtot"), row, 7);
            jt.setValor("" +
                        (dtStat.getDouble("fvc_sumtot") - dtStat.getDouble("fvc_impcob")),
                        row, 8);
          }
        }
      }
      catch (Exception k)
      {
        Error("Error al Cambiar Columna", k);
      }
    }

        @Override
    public int cambiaLinea(int row, int col)
    {
      try
      {
        return checkLinea(row);
      }
      catch (Exception k)
      {
        Error("Error al Cambiar Columna", k);
      }
      return 0;

    }
  };
//  CButton Baceptar = new CButton();
//  CButton Bcancelar = new CButton();
   CTextField fvc_serieE = new CTextField(Types.CHAR,"X",1);
  CTextField fvc_anoE = new CTextField(Types.DECIMAL,"###9");
  CTextField emp_codiE = new CTextField(Types.DECIMAL,"#9");
  CTextField fvc_numeE = new CTextField(Types.DECIMAL,"####9");
  CTextField cli_codiE = new CTextField();
  CTextField cli_nombE = new CTextField();
  CTextField fvc_fecfraE = new CTextField();
  CTextField fvc_sumtotE = new CTextField();
  CTextField fvc_impcobE = new CTextField();
  CTextField fvc_imppenE = new CTextField();
  CPanel Pacum = new CPanel();
  CLabel cLabel5 = new CLabel();
  CTextField numFrasE = new CTextField(Types.DECIMAL,"##9");
  CLabel cLabel6 = new CLabel();
  CTextField impFrasE = new CTextField(Types.DECIMAL,"--,---,--9.99");
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CButton Bimprim = new CButton("Imprimir",Iconos.getImageIcon("print"));

  public pdcobruta(EntornoUsuario eu, Principal p)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    setTitulo("Mant. Cobros para Ruta ");

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

 public pdcobruta(gnu.chu.anjelica.menu p, EntornoUsuario eu)
 {
   EU = eu;
   vl = p.getLayeredPane();
   setTitulo("Mant. Cobros para  Ruta ");
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
   this.setSize(new Dimension(589,564));
   this.setVersion("2014-10-28");
   Pprinc.setLayout(gridBagLayout1);
   statusBar = new StatusBar(this);
   nav = new navegador(this, dtCons, false, navegador.NORMAL);
   strSql = "select cor_fecha,usu_nomb,zon_codi,cor_orden " +
       "  from factruta " +
       " group by  cor_fecha,usu_nomb,zon_codi,cor_orden " +
       " order by  cor_fecha,usu_nomb,zon_codi,cor_orden";

   conecta();
   iniciar(this);

    Pcabe.setBorder(BorderFactory.createRaisedBevelBorder());
    Pcabe.setMaximumSize(new Dimension(381, 48));
    Pcabe.setMinimumSize(new Dimension(381, 48));
    Pcabe.setPreferredSize(new Dimension(381, 48));
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
    cLabel4.setBounds(new Rectangle(276, 26, 41, 18));
    ArrayList v= new ArrayList();
    v.add("Año"); // 0
    v.add("Emp"); // 1
    v.add("S"); // 2  serie
    v.add("Factura");  // 3
    v.add("Cliente"); // 4
    v.add("Nombre"); // 5
    v.add("Fec.Fra");// 6
    v.add("Imp.Fra"); // 7
    v.add("Imp.Pend"); // 8
    cor_ordenE.setEnabled(false);
    cor_ordenE.setBounds(new Rectangle(324, 26, 33, 18));
    jt.setCabecera(v);
    cli_codiE.setEnabled(false);
    cli_nombE.setEnabled(false);
    fvc_fecfraE.setEnabled(false);
    fvc_sumtotE.setEnabled(false);
    fvc_impcobE.setEnabled(false);
    fvc_imppenE.setEnabled(false);
    fvc_serieE.setMayusc(true);
    fvc_serieE.setAutoNext(true);
    fvc_serieE.setText("1");
    emp_codiE.setValorDec(EU.em_cod);
    fvc_anoE.setValorDec(EU.ejercicio);
    ArrayList v1=new ArrayList();
    v1.add(fvc_anoE);
    v1.add(emp_codiE);
    v1.add(fvc_serieE);
    v1.add(fvc_numeE);
    v1.add(cli_codiE);
    v1.add(cli_nombE);
    v1.add(fvc_fecfraE);
    v1.add(fvc_sumtotE);
    v1.add(fvc_imppenE);
    jt.setCampos(v1);
    jt.setColNueva(2);
    jt.setMaximumSize(new Dimension(406, 311));
    jt.setMinimumSize(new Dimension(406, 311));
    jt.setPreferredSize(new Dimension(406, 311));
    jt.setAnchoColumna(new int[]{50,40,25,60,40,140,80,70,70});
    jt.setAlinearColumna(new int[]{2,2,1,2,2,0,1,2,2});
    jt.setFormatoColumna(7,"----,--9.99");
    jt.setFormatoColumna(8,"----,--9.99");

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
    Pacum.setMaximumSize(new Dimension(412, 29));
    Pacum.setMinimumSize(new Dimension(412, 29));
    Pacum.setPreferredSize(new Dimension(412, 29));
    Pacum.setLayout(null);
    cLabel5.setText("N.Fras");
    cLabel5.setBounds(new Rectangle(4, 4, 46, 20));
    numFrasE.setBounds(new Rectangle(56, 4, 55, 20));
    cLabel6.setToolTipText("");
    cLabel6.setText("Imp. Fras");
    cLabel6.setBounds(new Rectangle(124, 4, 54, 20));
    impFrasE.setBounds(new Rectangle(185, 4, 118, 20));
    Bimprim.setBounds(new Rectangle(306, 6, 93, 18));
    Bimprim.setMargin(new Insets(0, 0, 0, 0));
    BirGrid.setBounds(new Rectangle(368, 28, 4, 4));
    usu_nombE.setBounds(new Rectangle(216, 4, 145, 18));
    cor_fechaE.setBounds(new Rectangle(56, 4, 84, 18));
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);

    Pprinc.add(Pcabe,      new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 0, 0));
    Pcabe.add(rut_codiE, null);
    Pcabe.add(cLabel3, null);
    Pcabe.add(usu_nombE, null);
    Pcabe.add(cor_fechaE, null);
    Pcabe.add(cLabel1, null);
    Pcabe.add(cLabel2, null);
    Pcabe.add(BirGrid, null);
    Pcabe.add(cor_ordenE, null);
    Pcabe.add(cLabel4, null);
    Pprinc.add(jt,    new GridBagConstraints(0, 1, 3, 1, 1.0, 1.0
             ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 0, 0, 0), 0, 0));
    Pprinc.add(Baceptar,    new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 55, 0, 0), 0, 0));
    Pprinc.add(Bcancelar,    new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 55), 0, 0));
    Pprinc.add(Pacum,    new GridBagConstraints(0, 3, 3, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));
    Pacum.add(numFrasE, null);
    Pacum.add(cLabel5, null);
    Pacum.add(cLabel6, null);
    Pacum.add(impFrasE, null);
    Pacum.add(Bimprim, null);
    impFrasE.setEnabled(false);
    numFrasE.setEnabled(false);
 }
 public void iniciarVentana() throws Exception
 {
   Pcabe.setDefButton(Baceptar);
   Pcabe.setButton(KeyEvent.VK_F4,Baceptar);

   jt.setDefButton(Baceptar);
   jt.setButton(KeyEvent.VK_F4,Baceptar);
   cor_fechaE.setColumnaAlias("cor_fecha");
   usu_nombE.setColumnaAlias("usu_nomb");
   rut_codiE.setColumnaAlias("zon_codi");
   rut_codiE.setFormato(Types.CHAR, "XX");
   rut_codiE.texto.setMayusc(true);
   gnu.chu.anjelica.pad.pdconfig.llenaDiscr(dtCon1,rut_codiE,pdconfig.D_RUTAS,EU.em_cod);

   activarEventos();
   verDatos(dtCons);
 }
 void activarEventos()
 {
   BirGrid.addFocusListener(new FocusAdapter() {
     public void focusGained(FocusEvent e) {
       BirGrid_focusGained();
     }
   });
   Bimprim.addActionListener(new ActionListener() {
     public void actionPerformed(ActionEvent e) {
       Bimprim_actionPerFormed();
     }
   });
 }

 void Bimprim_actionPerFormed()
 {
   try {
     if (dtCons.getNOREG())
     {
       mensajeErr("NO HAY REGISTROS ACTIVOS");
       return;
     }
     
     s="select f.*,c.cli_codi,c.cli_nomb,fr.fvc_fecfra from factruta f,clientes c,v_facvec fr "+
         " WHERE cor_fecha = TO_DATE('"+cor_fechaE.getText()+"','dd-MM-yyyy') "+
         " and usu_nomb  = '"+usu_nombE.getText()+"'"+
         " and f.zon_codi = '"+rut_codiE.getText()+"'"+
         " and cor_orden = "+cor_ordenE.getValorInt()+
         " and c.cli_codi = fr.cli_codi "+
         " and fr.fvc_ano = f.fvc_ano "+
         " and fr.emp_codi = f.emp_codi "+
         " and fr.fvc_nume = f.fvc_nume "+
         " and fr.fvc_serie =f.fvc_serie "+
         " order by c.cli_codi,f.fvc_ano,f.emp_codi,fvc_serie,f.fvc_nume";
     dtCon1.setStrSelect(s);
     ResultSet rs=ct.createStatement().executeQuery(dtCon1.getStrSelect());

     JasperReport jr= gnu.chu.print.util.getJasperReport(EU, "cacobrea");
     java.util.HashMap mp = new java.util.HashMap();
     mp.put("cor_fecha", cor_fechaE.getText());
     mp.put("usu_nomb", usu_nombE.getText());
     mp.put("zon_codi", rut_codiE.getText()+"-"+rut_codiE.getTextCombo());
     mp.put("cor_orden", cor_ordenE.getText());

     JasperPrint jp = JasperFillManager.fillReport(jr, mp, new JRResultSetDataSource(rs));

//     JasperViewer.viewReport(jp,false);
    gnu.chu.print.util.printJasper(jp, EU);

   } catch (SQLException | JRException | PrinterException k)
   {
     Error("Error al generar Listado",k);
   }

 }

 void BirGrid_focusGained()
 {
   if (nav.pulsado == navegador.ADDNEW)
   {
     if (! checkCab())
       return;
     try {
       cor_ordenE.setValorDec(getNumOrden());
     } catch (Exception k)
     {
       Error("Error al Ir a Grid",k);
       return;
     }
     jt.setEnabled(true);
     jt.requestFocusInicio();
   }

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
  public void PADUltimo() {
    verDatos(dtCons);
  }
  @Override
  public void PADQuery() {
    activar(true);
    Pcabe.setQuery(true);
    Pcabe.resetTexto();
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
      ArrayList v = new ArrayList();
      v.add(cor_fechaE.getStrQuery());
      v.add(usu_nombE.getStrQuery());
      v.add(rut_codiE.getStrQuery());
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
    activar(true);
    jt.requestFocusInicio();
    mensaje("Modificacion de Facturas entregadas");
  }

  @Override
  public void ej_edit1()
  {
    if (!checkCab())
      return;
    try
    {
      jt.salirGrid();
      if (checkLinea(0) >= 0)
      {
        jt.requestFocusSelected();
        return;
      }
      borFactur();
      insLinFac();
      ctUp.commit();
    }
    catch (Exception k)
    {
      Error("Error al Insertar datos", k);
    }
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
    cor_ordenE.setValorDec(getNumOrden());
    for (int n = 0; n < nRow; n++)
    {
      if (jt.getValorInt(n, 3) == 0)
        continue;
      dtAdd.addNew("factruta");
      dtAdd.setDato("cor_fecha", cor_fechaE.getText(), "dd-MM-yyyy");
      dtAdd.setDato("usu_nomb", usu_nombE.getText());
      dtAdd.setDato("zon_codi", rut_codiE.getText());
      dtAdd.setDato("cor_orden", cor_ordenE.getValorInt());
      dtAdd.setDato("fvc_ano", jt.getValorInt(n, 0));
      dtAdd.setDato("emp_codi", jt.getValorInt(n, 1));
      dtAdd.setDato("fvc_serie", jt.getValString(n, 2));
      dtAdd.setDato("fvc_nume", jt.getValorInt(n, 3));
      dtAdd.setDato("fvc_sumtot", jt.getValorDec(n, 7));
      dtAdd.setDato("fvc_imppen", jt.getValorDec(n, 8));
      dtAdd.setDato("fvc_impcob", 0);
      dtAdd.setDato("cor_intcob","N");
      dtAdd.setDato("cor_totcob","N");

      dtAdd.update(stUp);
    }

  }
  @Override
  public void canc_edit()
  {
    activaTodo();
    mensaje("");
    mensajeErr("Modificacion de Facturas ... Cancelada");
    verDatos(dtCons);
  }
  @Override
  public void PADAddNew()
  {
    activar(true);
    jt.setEnabled(false);

    jt.removeAllDatos();
    Pcabe.resetTexto();
    cor_fechaE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
    cor_fechaE.requestFocus();
    mensaje("Alta de Facturas entregadas");
  }
  @Override
  public void ej_addnew1() {
    if (! checkCab())
      return;
    try
    {
      jt.salirGrid();
      if (checkLinea(0)>=0)
      {
        jt.requestFocusSelected();
        return;
      }
      insLinFac();
      ctUp.commit();
      activaTodo();
      mensaje("");
      if (dtCons.getNOREG())
      {
        rgSelect();
        verDatos(dtCons);
      }

    } catch (Exception k)
    {
      Error("Error al Insertar datos",k);
    }
    mensajeErr("Datos ... Insertados");
  }
  /**
   * Comprobar la cabecera de la factura.
   * @return false si hay error.
   *         true si todo va bien.
   */
  boolean checkCab()
  {
    if (cor_fechaE.getError() || cor_fechaE.isNull())
    {
      mensajeErr("Introduzca una fecha Valida");
      cor_fechaE.requestFocus();
      return false;
    }
    if (usu_nombE.getText().trim().equals(""))
    {
      mensajeErr("Introduzca un nombre de Usuario");
      usu_nombE.requestFocus();
      return false;
    }
    if (rut_codiE.getText().trim().equals(""))
    {
      mensajeErr("Introduzca una Ruta");
      rut_codiE.requestFocus();
      return false;
    }
    return true;
  }
  /**
   * Devuelve el Proximo numero de orden para una cabecera
   * @return numero orden
   * @throws Exception en caso error BD
   */
  int getNumOrden() throws Exception
  {
    s="SELECT MAX(cor_orden) as cor_orden FROM factruta "+
        " WHERE cor_fecha = TO_DATE('"+cor_fechaE.getText()+"','dd-MM-yyyy') "+
        " and usu_nomb  = '"+usu_nombE.getText()+"'"+
        " and zon_codi = '"+rut_codiE.getText()+"'";
    dtCon1.select(s);
    return dtCon1.getInt("cor_orden",true)+1;
  }
  @Override
  public void canc_addnew() {

    activaTodo();
    mensaje("");
    mensajeErr("Alta de Facturas ... Cancelada");
    verDatos(dtCons);
  }
  @Override
  public void PADDelete() {
    if (dtCons.getNOREG())
    {
      activaTodo();
      mensajeErr("No hay registros para borrar");
      return;
    }
    Baceptar.setEnabled(true);
    Bcancelar.setEnabled(true);
    mensaje("Borrando  Facturas entregadas ...");
    Bcancelar.requestFocus();
  }
  @Override
  public void ej_delete1() {
    try
    {
      borFactur();
      ctUp.commit();
      if (dtCons.next())
        verDatos(dtCons);
      else
      {
        if (dtCons.previous())
          verDatos(dtCons);
        else
        {
          Pcabe.resetTexto();
          jt.removeAllDatos();
        }
      }
    }
    catch (SQLException | ParseException k)
    {
      Error("Error al Insertar datos", k);
    }
    activaTodo();
    mensaje("");
    mensajeErr("Datos ... BORRADOS");
  }
  @Override
  public void canc_delete() {
    activaTodo();
    mensaje("");
    mensajeErr("Borrado de Facturas ... Cancelado");
    verDatos(dtCons);
  }

  @Override
  public void activar(boolean b)
  {
    cor_fechaE.setEnabled(b);
    usu_nombE.setEnabled(b);
    rut_codiE.setEnabled(b);
    BirGrid.setEnabled(b);
    jt.setEnabled(b);
    Baceptar.setEnabled(b);
    Bcancelar.setEnabled(b);
    Bimprim.setEnabled(!b);
  }

  boolean buscaFac(int ejeNume,int empCodi,String serie,int numFra) throws Exception
  {
    s="SELECT f.*,cli_nomb FROM v_facvec as f,clientes as cl "+
       "  WHERE f.emp_codi = "+empCodi+
        " and f.fvc_ano = "+ejeNume+
        " and f.fvc_nume = "+numFra+
        " and f.fvc_serie = '"+serie+"'"+
        " and f.cli_codi = cl.cli_codi ";
    return dtStat.select(s);
  }
  int checkLinea(int row) throws Exception
  {
    if (fvc_numeE.getValorInt()==0)
      return -1;
    if (!buscaFac(fvc_anoE.getValorInt(), emp_codiE.getValorInt(),
                  fvc_serieE.getText(), fvc_numeE.getValorInt()))
    {
      mensajeErr("Factura NO ENCONTRADA");
      return 0;
    }
    if (dtStat.getDouble("fvc_cobrad")==-1)
    {
      mensajeErr("Factura YA esta cobrada");
      return 0;
    }
    return -1;
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
      s="select f.*,c.cli_codi,c.cli_nomb,fr.fvc_fecfra from factruta f,clientes c,v_facvec fr "+
          " WHERE cor_fecha = TO_DATE('"+dt.getFecha("cor_fecha","dd-MM-yyyy")+"','dd-MM-yyyy') "+
          " and usu_nomb  = '"+dt.getString("usu_nomb")+"'"+
          " and f.zon_codi = '"+dt.getString("zon_codi")+"'"+
          " and cor_orden = "+dt.getInt("cor_orden")+
          " and c.cli_codi = fr.cli_codi "+
          " and fr.fvc_ano = f.fvc_ano "+
          " and fr.fvc_serie = f.fvc_serie "+
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
      int nFras=0;
      double impFras=0;
      do
      {
        ArrayList v=new ArrayList();
        v.add(dtCon1.getString("fvc_ano"));
        v.add(dtCon1.getString("emp_codi"));
        v.add(dtCon1.getString("fvc_serie"));
        v.add(dtCon1.getString("fvc_nume"));
        v.add(dtCon1.getString("cli_codi"));
        v.add(dtCon1.getString("cli_nomb"));
        v.add(dtCon1.getFecha("fvc_fecfra","dd-MM-yyyy"));
        v.add(dtCon1.getString("fvc_sumtot"));
        v.add(dtCon1.getString("fvc_imppen"));
        jt.addLinea(v);
        nFras++;
        impFras+=dtCon1.getDouble("fvc_imppen");
      } while (dtCon1.next());
      numFrasE.setValorDec(nFras);
      impFrasE.setValorDec(impFras);
    } catch (Exception k)
    {
      Error("Error al ver datos",k);
    }
  }
  void calcSumFras()
  {
    int nRow=jt.getRowCount();
    int nFras=0;
    double impFras = 0;

    for (int n=0;n<nRow;n++)
    {
      if (jt.getValorInt(n, 2) == 0)
        continue;
      nFras++;
      impFras+=jt.getValorDec(n,7);
    }
    numFrasE.setValorDec(nFras);
    impFrasE.setValorDec(impFras);

  }
}
