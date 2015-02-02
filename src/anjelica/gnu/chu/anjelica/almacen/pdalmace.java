package gnu.chu.anjelica.almacen;

import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import java.awt.*;
import javax.swing.BorderFactory;
import java.util.*;
import gnu.chu.Menu.*;
import gnu.chu.interfaces.PAD;
import java.sql.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import gnu.chu.camposdb.*;
import gnu.chu.sql.DatosTabla;
import java.awt.event.*;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * <p>Título: pdalmace</p>
 * <p>Descripcion: Mant. Almacenes de Ventas</p>
 * <p>Copyright: Copyright (c) 2005-2010
*  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
*  los términos de la Licencia Publica General de GNU según es publicada por
*  la Free Software Foundation, bien de la versión 2 de dicha Licencia
*  o bien (según su elección) de cualquier versión posterior.
*  Este programa se distribuye con la esperanza de que sea útil,
*  pero SIN NINGUNA GARANTÍA, incluso sin la garantía MERCANTIL implícita
*  o sin garantizar la CONVENIENCIA PARA UN PROPÓSITO PARTICULAR.
*  Véase la Licencia Pública General de GNU para más detalles.
*  Debería haber recibido una copia de la Licencia Pública General junto con este programa.
*  Si no ha sido así, escriba a la Free Software Foundation, Inc.,
*  en 675 Mass Ave, Cambridge, MA 02139, EEUU.
 * <p>Empresa: MISL</p>
 * @author chuchiP
 * @version 1.0
 */

public class pdalmace extends ventanaPad implements PAD
{
  String s;
  CPanel Pprinc = new CPanel();
  Cgrid jt = new Cgrid(10);
  CPanel Pdatos = new CPanel();
  CLabel cLabel1 = new CLabel();
  CTextField alm_codiE = new CTextField(Types.DECIMAL,"##9");
  CTextField alm_nombE = new CTextField(Types.CHAR,"X",50);
  CLabel alm_feulinL = new CLabel("Ult.Inventario");
  CTextField alm_feulinE= new CTextField(Types.DATE,"dd-MM-yyyy");
//  CButton Baceptar = new CButton("Aceptar",Iconos.getImageIcon("check"));
//  CButton Bcancelar = new CButton("Cancelar",Iconos.getImageIcon("cancel"));
  CTextField alm_poblE = new CTextField(Types.CHAR,"X",30);
  CLabel cLabel4 = new CLabel();
  CLabel cLabel33 = new CLabel();
  CTextField alm_direcE = new CTextField(Types.CHAR,"X",40);
  CTextField alm_codposE = new CTextField(Types.DECIMAL,"#99999");
  CLabel cLabel5 = new CLabel();
  CTextField alm_telefE = new CTextField(Types.CHAR,"X",15);
  CLabel cLabel7 = new CLabel();
  CTextField alm_faxE = new CTextField(Types.CHAR,"X",15);
  CLabel cLabel6 = new CLabel();
  CTextField alm_respoE = new CTextField(Types.CHAR,"X",30);
  CLabel cLabel8 = new CLabel();
  CLabel cLabel2 = new CLabel();
  empPanel emp_codiE = new empPanel();
  CLabel emp_nombL;
  CLabel cLabel3 = new CLabel();
  CLabel sbe_nombL;
  sbePanel sbe_codiE = new sbePanel();
  CPanel Pempre = new CPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();

  public pdalmace(EntornoUsuario eu, Principal p) {
     EU=eu;
     vl=p.panel1;
     jf=p;
     eje=true;

     setTitulo("Mantenimiento de Almacenes ");
     try  {
       if(jf.gestor.apuntar(this))
           jbInit();
       else
         setErrorInit(true);
     }
     catch (Exception e) {
       ErrorInit(e);
     }
   }

   public pdalmace(gnu.chu.anjelica.menu p,EntornoUsuario eu) {

     EU=eu;
     vl=p.getLayeredPane();
     setTitulo("Mantenimiento de Almacenes");
     try  {
       jbInit();
     }
     catch (Exception e) {
       ErrorInit(e);
     }
   }

   private void jbInit() throws Exception
   { 
     iniciarFrame();
     this.setSize(new Dimension(539, 522));
     this.setVersion("2014-12-11 ");
     strSql = "select * from almacen where emp_codi = "+EU.em_cod + "ORDER BY alm_codi";
     statusBar = new StatusBar(this);
     nav = new navegador(this, false, navegador.GRID);
     conecta();
     iniciar(this);
     
     cLabel1.setText("Codigo");
    cLabel1.setBounds(new Rectangle(3, 24, 47, 18));
    alm_codiE.setBounds(new Rectangle(58, 24, 42, 18));
    alm_nombE.setBounds(new Rectangle(106, 24, 405, 18));
    jt.setMaximumSize(new Dimension(528, 349));
    jt.setMinimumSize(new Dimension(528, 349));
    jt.setPreferredSize(new Dimension(516, 283));
    Pdatos.setMaximumSize(new Dimension(518, 122));
    Pdatos.setMinimumSize(new Dimension(518, 122));
    Pdatos.setPreferredSize(new Dimension(518, 122));
    Baceptar.setMaximumSize(new Dimension(148, 30));
    Baceptar.setMinimumSize(new Dimension(148, 30));
    Baceptar.setPreferredSize(new Dimension(148, 30));
    Bcancelar.setMaximumSize(new Dimension(148, 30));
    Bcancelar.setMinimumSize(new Dimension(148, 30));
    Bcancelar.setPreferredSize(new Dimension(148, 30));
    alm_poblE.setBounds(new Rectangle(58, 61, 227, 17));
    cLabel4.setText("Direccion");
    cLabel4.setBounds(new Rectangle(3, 43, 59, 17));
    cLabel33.setText("Cod Postal");
    cLabel33.setBounds(new Rectangle(401, 43, 61, 17));
    alm_direcE.setBounds(new Rectangle(58, 43, 316, 17));
    alm_codposE.setBounds(new Rectangle(461, 43, 50, 17));
    cLabel5.setText("Poblacion");
    cLabel5.setBounds(new Rectangle(4, 61, 59, 17));
    alm_telefE.setBounds(new Rectangle(383, 61, 129, 17));
    cLabel7.setText("Fax");
    cLabel7.setBounds(new Rectangle(4, 79, 29, 17));
    alm_faxE.setBounds(new Rectangle(58, 79, 129, 17));
    alm_feulinL.setBounds(new Rectangle(4, 99, 90, 17));
    alm_feulinE.setBounds(new Rectangle(95, 99, 80, 17));
    cLabel6.setText("Telef.");
    cLabel6.setBounds(new Rectangle(348, 61, 32, 17));
    alm_respoE.setBounds(new Rectangle(285, 79, 227, 17));
    cLabel8.setBounds(new Rectangle(206, 79, 75, 17));
    cLabel8.setText("Responsable");
    cLabel2.setText("Empresa");
    cLabel2.setBounds(new Rectangle(4, 3, 54, 17));

    emp_codiE.setBounds(new Rectangle(56, 3, 47, 20));

    emp_nombL.setBounds(new Rectangle(107, 3, 330, 19));
    cLabel3.setBounds(new Rectangle(4, 3, 50, 17));
    cLabel3.setText("SubEmpr");
    sbe_nombL.setBounds(new Rectangle(119, 3, 390, 18));

    sbe_codiE.setBounds(new Rectangle(59, 3, 47, 18));


    Pempre.setBorder(BorderFactory.createRaisedBevelBorder());
    Pempre.setMaximumSize(new Dimension(453, 28));
    Pempre.setMinimumSize(new Dimension(453, 28));
    Pempre.setPreferredSize(new Dimension(453, 28));
    Pempre.setLayout(null);
    ArrayList v = new ArrayList();
    v.add("Codigo"); // 0
    v.add("Nombre");  // 1
    v.add("Direccion"); // 2
    v.add("Poblac"); // 3
    v.add("Cod.Postal");//4
    v.add("Telef.");// 5
    v.add("Fax"); //6
    v.add("Responsable"); //7
    v.add("SubEmpresa"); // 8
    v.add("Fec.Inv."); // 9
    jt.setCabecera(v);
    jt.setAnchoColumna(new int[]   {40, 160,160,120,60,60,60,80,40,80});
    jt.setAlinearColumna(new int[] {2, 0, 0, 0,0,0,0,0,2,1});
    jt.setFormatoColumna(9,alm_feulinE.getFormato());
//    jt.ajustar(true);

     Pprinc.setLayout(gridBagLayout1);
     Pdatos.setBorder(BorderFactory.createRaisedBevelBorder());
     Pdatos.setLayout(null);
     this.getContentPane().add(Pprinc, BorderLayout.CENTER);
     Pprinc.add(jt,    new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 1), 0, 0));
     Pprinc.add(Pdatos,    new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 2, 0, 2), 0, 0));
     Pprinc.add(Bcancelar,    new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 70), 0, 0));
    Pprinc.add(Baceptar,     new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 70, 0, 0), 0, 0));
    Pdatos.add(alm_codiE, null);
    Pdatos.add(alm_codposE, null);
    Pdatos.add(cLabel33, null);
    Pdatos.add(alm_nombE, null);
    Pdatos.add(cLabel1, null);
    Pdatos.add(alm_direcE, null);
    Pdatos.add(cLabel4, null);
    Pdatos.add(alm_telefE, null);
    Pdatos.add(cLabel5, null);
    Pdatos.add(alm_poblE, null);
    Pdatos.add(cLabel6, null);
    Pdatos.add(alm_respoE, null);
    Pdatos.add(cLabel7, null);
    Pdatos.add(alm_faxE, null);
    Pdatos.add(cLabel8, null);
    Pdatos.add(sbe_nombL, null);
    Pdatos.add(cLabel3, null);
    Pdatos.add(sbe_codiE, null);
    Pdatos.add(alm_feulinL, null);
    Pdatos.add(alm_feulinE, null);
    
    Pprinc.add(Pempre,    new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 37, 0, 39), 0, 0));
    Pempre.add(emp_nombL, null);
    Pempre.add(cLabel2, null);
    Pempre.add(emp_codiE, null);

   }

    @Override
   public void afterConecta() throws SQLException
   {
     emp_codiE.iniciar(dtStat, this, vl, EU);
     emp_nombL = emp_codiE.creaLabelEmp();
     emp_codiE.setLabelEmp(emp_nombL);
     sbe_codiE.iniciar(dtStat, this, vl, EU);
     sbe_nombL = sbe_codiE.creaLabelSbe();
     sbe_codiE.setLabelSbe(sbe_nombL);
     sbe_codiE.setFieldEmpCodi(emp_codiE.getTextField());
   }
    @Override
   public void iniciarVentana() throws Exception
   {
     emp_codiE.setValorInt(EU.em_cod);
     emp_codiE.getTextField().resetCambio();
     Pdatos.setDefButton(Baceptar);
     activar(false);
     verDatos();
     activarEventos();
   }
   void activarEventos()
   {
     jt.tableView.getSelectionModel().addListSelectionListener(new
        ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent e)
      {

        if (e.getValueIsAdjusting() || !jt.isEnabled())
          return;
        cambiaLinea();
      }
    });
    emp_codiE.addFocusListener(new FocusAdapter()
    {
            @Override
      public void focusLost(FocusEvent e)
      {
        if (emp_codiE.hasCambio())
          cambiaEmp();
      }
    });
   }
   void cambiaEmp()
   {
       if (! emp_codiE.controla())
       {
         mensajeErr("Empresa NO VALIDA");
         emp_codiE.setText(emp_codiE.getTextField().getTextAnt());
         return;
       }
       emp_codiE.getTextField().resetCambio();
       strSql = "select * from almacen where emp_codi = "+emp_codiE.getValorInt() + "ORDER BY alm_codi";

       verDatos();
//     } catch (SQLException k)
//     {
//
//     }
   }
   void cambiaLinea()
   {
      try
      {
          if (jt.isVacio())
          {
              Pdatos.resetTexto();
              return;
          }
          alm_codiE.setText(jt.getValString(0));
          alm_nombE.setText(jt.getValString(1));
          alm_direcE.setText(jt.getValString(2));
          alm_poblE.setText(jt.getValString(3));
          alm_codposE.setText(jt.getValString(4));
          alm_telefE.setText(jt.getValString(5));
          alm_faxE.setText(jt.getValString(6));
          alm_respoE.setText(jt.getValString(7));
          sbe_codiE.setValorInt(jt.getValorInt(8));
          alm_feulinE.setDate(jt.getValDate(9));
      } catch (ParseException ex)
      {
          Error("Error al poner datos de linea de grid",ex);
      }
   }

   public void verDatos()
   {
     try {
       jt.setEnabled(false);
       jt.removeAllDatos();
       if (dtCons.select(strSql))
       {
         do
         {
           ArrayList v= new ArrayList();
           v.add(dtCons.getString("alm_codi"));
           v.add(dtCons.getString("alm_nomb"));
           v.add(dtCons.getString("alm_direc"));
           v.add(dtCons.getString("alm_pobl"));
           v.add(dtCons.getString("alm_codpos"));
           v.add(dtCons.getString("alm_telef"));
           v.add(dtCons.getString("alm_fax"));
           v.add(dtCons.getString("alm_respo"));
           v.add(dtCons.getString("sbe_codi"));
           v.add(dtCons.getDate("alm_feulin"));
           jt.addLinea(v);
         } while (dtCons.next());
         jt.requestFocusInicio();
         jt.setEnabled(true);
       }
       navActivarAll();
       cambiaLinea();
     } catch (Exception k)
     {
       Error("Error al ver Almacenes",k);
     }
   }
  @Override
 public void PADPrimero(){}
  @Override
 public void PADAnterior(){}
  @Override
 public void PADSiguiente(){}
  @Override
 public void PADUltimo(){}

    @Override
  public void PADQuery(){}
  public void ej_query1(){}
  public void canc_query(){}

    @Override
  public void PADEdit(){
    try {
      s = "SELECT * FROM almacen WHERE emp_codi = "+emp_codiE.getValorInt()+" and alm_codi = "+alm_codiE.getValorInt();
      if (! dtAdd.select(s,true))
      {
        mensajeErr("ALMACEN NO ENCONTRADO ... PROBABLEMENTE SE BORRO");
        activaTodo();
        return;
      }
      mensaje("Modificando ALMACEN ...");
      jt.setEnabled(false);
      activar(true);
      alm_codiE.setEnabled(false);
    } catch (Exception k)
    {
      Error("Error al Editar Registro",k);
    }
  }
    @Override
  public boolean checkEdit()
  {
    return checkAddNew();
  }

  public void ej_edit1()
  {
    try
    {
      dtAdd.edit();
      actDatos();
      ctUp.commit();
      mensaje("");
      mensajeErr("ALMACEN ...  MODIFICADO");
      activaTodo();
      verDatos();
    }
    catch (Exception k)
    {
      Error("Error al MODIFICAR ALMACEN", k);
    }

  }
  public void canc_edit(){
    try
    {
      ctUp.rollback();
      activaTodo();
      mensaje("");
      jt.setEnabled(true);
      mensajeErr("Modificacion ALMACEN ... CANCELADO");
    }
    catch (Exception k)
    {
      Error("Error al Cancelar Edicion de ALMACEN", k);
    }
  }

    @Override
  public void PADAddNew(){
    jt.setEnabled(false);
    activar(true);
    Pdatos.resetTexto();
    alm_codiE.setEnabled(true);
    sbe_codiE.requestFocus();
    mensaje("INSERTANDO UN NUEVO ALMACEN ... ");
  }
    @Override
  public boolean checkAddNew()
  {
    try
    {
      if (alm_codiE.getValorInt() == 0)
      {
        mensajeErr("Introduzca Codigo de Almacen");
        alm_codiE.requestFocus();
        return false;
      }
      if (alm_nombE.isNull())
      {
        mensajeErr("Introduzca Nombre de Almacen");
        alm_nombE.requestFocus();
        return false;
      }
      if (! sbe_codiE.controla())
      {
        mensajeErr("SubEmpresa NO VALIDA");
        return false;
      }
    } catch (SQLException k)
    {
      Error("Error al Comprobar Campos",k);
      return false;
    }
    return true;
  }
  public void ej_addnew1(){
    try {
      String s = "SELECT * FROM almacen WHERE emp_codi = "+emp_codiE.getValorInt()+
          " and alm_codi = " + alm_codiE.getValorInt();
      if (dtStat.select(s))
      {
        mensajeErr("ALMACEN YA existe");
        return;
      }
      dtAdd.addNew("almacen");
      dtAdd.setDato("emp_codi",emp_codiE.getValorInt());
      dtAdd.setDato("alm_codi",alm_codiE.getValorInt());
      actDatos();
      ctUp.commit();
      mensaje("");
      mensajeErr("Almacen ...  CREADO");
      activaTodo();
      verDatos();
    } catch (Exception k)
    {
      Error("Error al Insertar Motivo de Regularizacion",k);
    }
  }
  void actDatos() throws SQLException,java.text.ParseException
  {
    dtAdd.setDato("alm_nomb", alm_nombE.getText());
    dtAdd.setDato("alm_direc", alm_direcE.getText());
    dtAdd.setDato("alm_pobl", alm_poblE.getText());
    dtAdd.setDato("alm_codpos", alm_codposE.getText());
    dtAdd.setDato("alm_telef", alm_telefE.getText());
    dtAdd.setDato("alm_fax", alm_faxE.getText());
    dtAdd.setDato("alm_respo", alm_respoE.getText());
    dtAdd.setDato("sbe_codi",sbe_codiE.getValorInt());
    dtAdd.setDato("alm_feulin",alm_feulinE.getDate());
    dtAdd.update(stUp);
  }
  @Override
  public void canc_addnew(){
    mensaje("");
    jt.setEnabled(false);
    mensajeErr("ALTA Almacen ... CANCELADA");
    activaTodo();
    verDatos();
  }

    @Override
  public void PADDelete()
  {
    try
    {
      s = "SELECT alm_codi FROM v_albavel WHERE emp_codi = "+emp_codiE.getValorInt()+" and alm_codi = " +
          alm_codiE.getValorInt();
      if (dtStat.select(s))
      {
        mensajeErr("ALMACEN EN uso (en ventas) ... Imposible BORRAR");
        activaTodo();
        return;
      }
      s = "SELECT alm_codi FROM v_albacol WHERE emp_codi = "+emp_codiE.getValorInt()+ " and alm_codi = " +
          alm_codiE.getValorInt();
      if (dtStat.select(s))
      {
        mensajeErr("ALMACEN EN uso (en Compras) ... Imposible BORRAR");
        activaTodo();
        return;
      }

      s = "SELECT * FROM almacen WHERE emp_codi = "+emp_codiE.getValorInt()+" and alm_codi = " + alm_codiE.getValorInt();
      if (!dtAdd.select(s, true))
      {
        mensajeErr("ALMACEN NO ENCONTRADO ... PROBABLEMENTE SE BORRO");
        activaTodo();
        return;
      }
      mensaje("Borrando ALMACEN ...");
      jt.setEnabled(false);
      Baceptar.setEnabled(true);
      Bcancelar.setEnabled(true);
      Bcancelar.requestFocus();
    }
    catch (Exception k)
    {
      Error("Error al Editar Registro", k);
    }

  }

  public void ej_delete1()
  {
    try
    {
      dtAdd.delete();
      ctUp.commit();
      mensaje("");
      mensajeErr("ALMACEN ...  BORRADO");
      activaTodo();
      verDatos();
    }
    catch (Exception k)
    {
      Error("Error al BORRAR ALMACEN", k);
    }
  }

  public void canc_delete(){
    try
   {
     ctUp.rollback();
     activaTodo();
     mensaje("");
     jt.setEnabled(true);
     mensajeErr("Modificacion ALMACEN ... CANCELADO");
   }
   catch (Exception k)
   {
     Error("Error al Cancelar Edicion de Registro", k);
   }

  }
  public void activar(boolean b){
    emp_codiE.setEnabled(!b);
    Baceptar.setEnabled(b);
    Bcancelar.setEnabled(b);
    Pdatos.setEnabled(b);
  }
  public static void llenaCombo(CComboBox almCodi, DatosTabla dt) throws SQLException
  {
    String s = "SELECT alm_codi,alm_nomb FROM almacen " +
              " ORDER BY alm_codi";
    dt.select(s);
    almCodi.addItem(dt);
  }
  /**
   * Carga un LinkBox con los datos necesarios para buscar un almacen
   * @param almCodi
   * @param dt
   * @throws SQLException 
   */
  public static void llenaLinkBox(CLinkBox almCodi, DatosTabla dt) throws SQLException
  {
    almCodi.setFormato(true);
    almCodi.setFormato(Types.DECIMAL, "#9", 2);

    String s = "SELECT alm_codi,alm_nomb FROM almacen " +
              " ORDER BY alm_codi";
    dt.select(s);
    almCodi.addDatos(dt);
  }
   public static int getAlmacenPrincipal()
   {
     int ALMACENPRNCIPAL=1;
     return ALMACENPRNCIPAL;
   }
   
    /**
     * Devuelve la fecha de ultimo inventario para una empresa
     * @param almCodi Empresa de la q buscar la fecha inventario. 0 para todas. 
     * @param dt Datostabla
     * @return Fecha Inventario. Puede ser null
     * @throws SQLException Si no encuentra la configuracion o hay un erro de DB 
     */
 public static java.sql.Date getFechaInventario(int almCodi, DatosTabla dt) throws SQLException
 {
     if (!dt.select("select alm_feulin from almacen"+
         " where alm_codi = " + almCodi))
         throw new SQLException("No encontrado Almacen"+almCodi);
     return dt.getDate("alm_feulin");
 }
}
