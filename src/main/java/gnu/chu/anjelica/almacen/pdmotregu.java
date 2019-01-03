package gnu.chu.anjelica.almacen;

/**
 *
 * <p>Título: pdmotregu</p>
 * <p>Descripcion: Mant. TIPOS de Regularizacion</p>
 * <p>Copyright: Copyright (c) 2006-201
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
import gnu.chu.Menu.*;
import gnu.chu.controles.*;
import gnu.chu.interfaces.PAD;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.*;
import java.awt.*;
import java.sql.*;
import java.text.ParseException;
import java.util.*;
import javax.swing.BorderFactory;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class pdmotregu extends ventanaPad implements PAD
{
  public final static String VERT_CLIENTE="VC";
  public final static String VERT_PROVEE="VP";
  public final static String VERT_SALA="VS";   
  public final static String MERM_CLIENTE="MC";   
     
  String s;
  CPanel Pprinc = new CPanel();
  Cgrid jt = new Cgrid(4);
  CPanel Pdatos = new CPanel();
  CLabel cLabel1 = new CLabel();
  CTextField tir_codiE = new CTextField(Types.DECIMAL,"##9");
  CTextField tir_nombE = new CTextField(Types.CHAR,"X",50);
  CLabel cLabel2 = new CLabel();
  CComboBox tir_afestkE = new CComboBox();
  CLabel cLabel3 = new CLabel();
  CComboBox tir_tipoE = new CComboBox();
//  CButton Baceptar = new CButton("Aceptar",Iconos.getImageIcon("check"));
//  CButton Bcancelar = new CButton("Cancelar",Iconos.getImageIcon("cancel"));
  GridBagLayout gridBagLayout2 = new GridBagLayout();

  public pdmotregu(EntornoUsuario eu, Principal p) {
     EU=eu;
     vl=p.panel1;
     jf=p;
     eje=true;

     setTitulo("Mant. Motivos de Regularizacion");

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

   public pdmotregu(gnu.chu.anjelica.menu p,EntornoUsuario eu) {

     EU=eu;
     vl=p.getLayeredPane();
     setTitulo("Mant. Motivos de Regularizacion");
     try  {
       jbInit();
     }
     catch (Exception e) {
        ErrorInit(e);
        setErrorInit(true);
     }
   }

   private void jbInit() throws Exception
   {
     iniciarFrame();
     this.setSize(new Dimension(539, 522));
     this.setVersion("2015-06-05");
     strSql = "select * from motregu ORDER BY tir_codi";
     statusBar = new StatusBar(this);
     nav = new navegador(this, false, navegador.GRID);
     conecta();
     iniciar(this);
     ArrayList v = new ArrayList();
     cLabel1.setText("Codigo");
    cLabel1.setBounds(new Rectangle(7, 5, 47, 18));
    tir_codiE.setBounds(new Rectangle(57, 5, 42, 18));
    tir_nombE.setBounds(new Rectangle(106, 5, 405, 18));
    cLabel2.setText("Afecta Stock");
    cLabel2.setBounds(new Rectangle(8, 28, 79, 18));
    tir_afestkE.setBounds(new Rectangle(93, 28, 76, 18));
    cLabel3.setText("Tipo Regularizacion");
    cLabel3.setBounds(new Rectangle(260, 28, 118, 18));
    tir_tipoE.setBounds(new Rectangle(378, 28, 133, 18));
    jt.setMaximumSize(new Dimension(516, 323));
    jt.setMinimumSize(new Dimension(516, 323));
    jt.setPreferredSize(new Dimension(516, 323));
    Pdatos.setMaximumSize(new Dimension(515, 53));
    Pdatos.setMinimumSize(new Dimension(515, 53));
    Pdatos.setPreferredSize(new Dimension(515, 53));
    Baceptar.setMaximumSize(new Dimension(148, 30));
    Baceptar.setMinimumSize(new Dimension(148, 30));
    Baceptar.setPreferredSize(new Dimension(148, 30));
    Bcancelar.setMaximumSize(new Dimension(148, 30));
    Bcancelar.setMinimumSize(new Dimension(148, 30));
    Bcancelar.setPreferredSize(new Dimension(148, 30));
    v.add("Codigo");
    v.add("Descripcion");
    v.add("Afecta");
    v.add("Tipo");
    jt.setCabecera(v);
    jt.setAnchoColumna(new int[]   {40, 160, 40, 40});
    jt.setAlinearColumna(new int[] {2, 0, 1, 1});
    jt.setAjustarGrid(true);

    Pprinc.setLayout(gridBagLayout2);
    Pdatos.setBorder(BorderFactory.createRaisedBevelBorder());
    Pdatos.setLayout(null);
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
    Pprinc.add(jt,   new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0
           ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    Pprinc.add(Pdatos,   new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0
           ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    Pdatos.add(cLabel1, null);
    Pdatos.add(tir_codiE, null);
    Pprinc.add(Bcancelar,   new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
           ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 70), 0, 0));
    Pprinc.add(Baceptar,    new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
           ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 70, 0, 0), 0, 0));
    Pdatos.add(tir_nombE, null);
    Pdatos.add(tir_afestkE, null);
    Pdatos.add(cLabel2, null);
    Pdatos.add(tir_tipoE, null);
    Pdatos.add(cLabel3, null);

   }
   public void iniciarVentana() throws Exception
   {
     activar(false);
     tir_afestkE.addItem("Suma","+");
     tir_afestkE.addItem("Resta","-");
     tir_afestkE.addItem("Invent.","=");
     tir_afestkE.addItem("No Afecta.","*");
     tir_tipoE.addItem("----","");
     tir_tipoE.addItem("Vert. Cliente",VERT_CLIENTE);
     tir_tipoE.addItem("Vert. Proveed",VERT_PROVEE);
     tir_tipoE.addItem("Vert. Sala",VERT_SALA);
     tir_tipoE.addItem("Merma Cliente",MERM_CLIENTE);
     
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
   }

   void cambiaLinea()
   {
     tir_codiE.setText(jt.getValString(0));
     tir_nombE.setText(jt.getValString(1));
     tir_afestkE.setValor(jt.getValString(2));
     tir_tipoE.setValor(jt.getValString(3));
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
           Vector v= new Vector();
           v.addElement(dtCons.getString("tir_codi"));
           v.addElement(dtCons.getString("tir_nomb"));
           v.addElement(dtCons.getString("tir_afestk"));
           v.addElement(dtCons.getString("tir_tipo"));
           jt.addLinea(v);
         } while (dtCons.next());
         jt.requestFocusInicio();
         jt.setEnabled(true);
         cambiaLinea();
       }
     } catch (Exception k)
     {
       Error("Error al ver Tipos de Regularizacion",k);
     }
   }
 public void PADPrimero(){}
 public void PADAnterior(){}
 public void PADSiguiente(){}
 public void PADUltimo(){}

  public void PADQuery(){}
  public void ej_query1(){}
  public void canc_query(){}

  public void PADEdit(){
    try {
      if (tir_codiE.getValorInt() <= 20)
      {
        mensajeErr("Códigos de Regularizacion Inferiores a 20 NO se pueden modificar");
        activaTodo();
        return;
      }

      s = "SELECT tir_codi FROM v_regstock WHERE tir_codi = "+tir_codiE.getValorInt();
      if (dtStat.select(s))
      {
        mensajeErr("Tipo de Regularizacion EN uso ... Imposible MODIFICAR");
        activaTodo();
        return;
      }
      s = "SELECT * FROM motregu WHERE tir_codi = "+tir_codiE.getValorInt();
      if (! dtAdd.select(s,true))
      {
        mensajeErr("Tipo de Regularizacion NO ENCONTRADO ... PROBABLEMENTE SE BORRO");
        activaTodo();
        return;
      }
      mensaje("Modificando Tipo de Regularizacion ...");
      jt.setEnabled(false);
      activar(true);
      tir_codiE.setEnabled(false);
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

  @Override
  public void ej_edit1()
  {
    try
    {
      dtAdd.edit();
      actDatos();
      ctUp.commit();
      mensaje("");
      mensajeErr("Tipo De Regularizacion ...  MODIFICADO");
      activaTodo();
      verDatos();
    }
    catch (SQLException k)
    {
      Error("Error al Insertar Motivo de Regularizacion", k);
    }

  }
  @Override
  public void canc_edit(){
    try
    {
      ctUp.rollback();
      activaTodo();
      mensaje("");
      jt.setEnabled(true);
      mensajeErr("Modificacion Motivo de Regularizacion ... CANCELADO");
    }
    catch (Exception k)
    {
      Error("Error al Cancelar Edicion de Registro", k);
    }
  }

  @Override
  public void PADAddNew(){
    jt.setEnabled(false);
    activar(true);
    Pdatos.resetTexto();
    tir_codiE.setEnabled(true);
    tir_codiE.requestFocus();
    mensaje("Introduzca UN Nuevo tipo de Vertedero");
  }
  @Override
  public boolean checkAddNew() 
  {
    if (tir_codiE.getValorInt()==0)
    {
      mensajeErr("Introduzca Codigo de Regularizacion");
      tir_codiE.requestFocus();
      return false;
    }
    try {
        if (tir_codiE.getValorInt() <= 20 && ! EU.isAdminDB(dtStat))
        {
          mensajeErr("Codigo de Regularizacion Inferiores a 20 Estan reservados para uso interno");
          tir_codiE.requestFocus();
          return false;
        }
    } catch (SQLException k)
    {
        Error("Error al comprobar permisos de usuario",k);
        return false;
    }
    if (tir_nombE.isNull())
    {
      mensajeErr("Introduzca Descripcion Tipo de Regularizacion");
      tir_nombE.requestFocus();
      return false;
    }

    return true;
  }
  @Override
  public void ej_addnew1(){
    try {
      String s = "SELECT * FROM motregu WHERE tir_codi = " + tir_codiE.getValorInt();
      if (dtStat.select(s))
      {
        mensajeErr("Motivo de Regularizacion YA existe");
        return;
      }
      dtAdd.addNew("motregu");
      dtAdd.setDato("tir_codi",tir_codiE.getValorInt());
      actDatos();
      ctUp.commit();
      mensaje("");
      mensajeErr("Tipo De Regularizacion ...  CREADO");
      activaTodo();
      verDatos();
    } catch (Exception k)
    {
      Error("Error al Insertar Motivo de Regularizacion",k);
    }
  }
  void actDatos() throws SQLException
  {
    dtAdd.setDato("tir_nomb", tir_nombE.getText());
    dtAdd.setDato("tir_afestk", tir_afestkE.getValor());
    dtAdd.setDato("tir_tipo", tir_tipoE.getValor());
    dtAdd.update(stUp);
  }
  @Override
  public void canc_addnew(){
    mensaje("");
    jt.setEnabled(false);
    mensajeErr("Insercion Motivo de Regularizacion ... CANCELADO");
    activaTodo();
    verDatos();
  }

  public void PADDelete()
  {
    try
    {
      if (tir_codiE.getValorInt() <= 20)
      {
        mensajeErr("C�digos de Regularizacion Inferiores a 20 NO se pueden BORRAR (Uso Interno)");
        activaTodo();
        return;
      }

      s = "SELECT tir_codi FROM v_regstock WHERE tir_codi = " + tir_codiE.getValorInt();
      if (dtStat.select(s))
      {
        mensajeErr("Tipo de Regularizacion EN uso ... Imposible BORRAR");
        activaTodo();
        return;
      }
      s = "SELECT * FROM motregu WHERE tir_codi = " + tir_codiE.getValorInt();
      if (!dtAdd.select(s, true))
      {
        mensajeErr("Tipo de Regularizacion NO ENCONTRADO ... PROBABLEMENTE SE BORRO");
        activaTodo();
        return;
      }
      mensaje("Borrando Tipo de Movimiento ...");
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
      mensajeErr("Tipo De Regularizacion ...  BORRADO");
      activaTodo();
      verDatos();
    }
    catch (Exception k)
    {
      Error("Error al Insertar Motivo de Regularizacion", k);
    }
  }

  public void canc_delete(){
    try
   {
     ctUp.rollback();
     activaTodo();
     mensaje("");
     jt.setEnabled(true);
     mensajeErr("Modificacion Motivo de Regularizacion ... CANCELADO");
   }
   catch (Exception k)
   {
     Error("Error al Cancelar Edicion de Registro", k);
   }

  }


  @Override
  public void activar(boolean b){
    Baceptar.setEnabled(b);
    Bcancelar.setEnabled(b);
    Pdatos.setEnabled(b);
  }
  /**
   * Devuelve el codigo de un motivo de regularizacion segun como afecta al stock
   * @param dt DatosTabla
   * @param afestk String indicando como afecta a stock (normalmente '=')
   * @return codigo (tir_codi) de la tabla motregu. -1 si no lo encuentra
   * @throws SQLException
   */
  public static int getTipoMotRegu(DatosTabla dt,String afestk) throws SQLException
  {
    String s = "select  tir_codi from motregu where tir_afestk = '"+afestk+"'";
     if (!dt.select(s))
       return -1;
     return  dt.getInt("tir_codi");
  }
  /**
   * Devuelve la cadena con los tipos de regularizacion separados por comas 
   * que sean como el tipo regularizacion Mandado.
   * @param dt DatosTabla
   * @param tipRegul String Tipo Regularizacion. Admite Comodines.
   * @return Cadena con Tipos Regularizacion, separados por comas, del tipo mandado.
   * @throws SQLException
   */
  public static String getTiposRegul(DatosTabla dt,String tipRegul) throws SQLException
  {
    String tirCodi="";
    String s = "select  tir_codi from motregu where tir_tipo like '"+tipRegul+"'";
     if (!dt.select(s))
       return tirCodi;
     do
     {
         if (!tirCodi.isEmpty())
             tirCodi+=",";
         tirCodi+=dt.getInt("tir_codi");
     } while (dt.next());
     return  tirCodi;
  }
}
