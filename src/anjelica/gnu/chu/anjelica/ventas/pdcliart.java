package gnu.chu.anjelica.ventas;

import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import gnu.chu.sql.*;
import java.sql.*;
import gnu.chu.Menu.*;
import java.awt.*;
import java.util.*;
import gnu.chu.camposdb.*;
import gnu.chu.interfaces.*;
import gnu.chu.anjelica.listados.etiqueta;
import javax.swing.*;

/**
 *
 * <p>Título: pdcliart </p>
 * <p>Descripción: Mantenimiento de Tabla Clientes/Articulos</p>
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
 * @author chuchiP
 * @version 1.0
 *
 */
public class pdcliart extends ventanaPad  implements PAD
{
  String s;
  CPanel Pprinc = new CPanel();
//  CButton Baceptar = new CButton();
//  CButton Bcancelar = new CButton();
  CLabel cLabel1 = new CLabel();
  cliPanel cli_codiE = new cliPanel();
  CLabel cLabel2 = new CLabel();
  proPanel pro_codiE = new proPanel();
  CLabel cLabel3 = new CLabel();
  CTextField pro_nombE = new CTextField();
  CLabel cLabel4 = new CLabel();
  CTextField cla_obspedE = new CTextField();
  CLabel cLabel5 = new CLabel();
  CComboBox eti_codiE = new CComboBox();
  CLabel cLabel6 = new CLabel();
  CTextField cla_diaconE = new CTextField(Types.DECIMAL,"##9");
  CLabel cLabel7 = new CLabel();
  CTextField cla_codbarE = new CTextField();
  CLabel cLabel8 = new CLabel();
  JScrollPane jScrollPane1 = new JScrollPane();
  JTextArea cla_ingredE = new JTextArea();
  CLabel cLabel9 = new CLabel();
  CTextField cla_deset1E = new CTextField();
  CLabel cLabel10 = new CLabel();
  CTextField cla_deset2E = new CTextField();
  CLabel cLabel11 = new CLabel();
  CTextField cla_deset3E = new CTextField();
  CLabel cLabel12 = new CLabel();
  CTextField cla_deset4E = new CTextField();
  CLabel cLabel13 = new CLabel();
  CTextField cla_deset5E = new CTextField();



   public pdcliart(EntornoUsuario eu, Principal p)
   {
     EU = eu;
     vl = p.panel1;
     jf = p;
     eje = true;
     try
     {
       setTitulo("Mant. Clientes/Articulos)");
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

   public pdcliart(gnu.chu.anjelica.menu p, EntornoUsuario eu)
   {
     EU = eu;
     vl = p.getLayeredPane();
     eje = false;
     jf = null;
     try
     {
       setTitulo("Mant. Clientes/Articulos");
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
     this.setSize(new Dimension(657, 444));
     statusBar = new StatusBar(this);
     this.setVersion("20100625");
     strSql="SELECT * FROM v_cliart ORDER BY cli_codi,pro_codi";
     conecta();
     nav = new navegador(this, dtCons, false, navegador.NORMAL);
     iniciar(this);

    cLabel10.setHorizontalAlignment(SwingConstants.RIGHT);
    cLabel10.setHorizontalTextPosition(SwingConstants.LEFT);
    cLabel10.setText("Descr. Campo 2");
    cLabel10.setBounds(new Rectangle(6, 229, 115, 17));
    cLabel11.setHorizontalAlignment(SwingConstants.RIGHT);
    cLabel11.setHorizontalTextPosition(SwingConstants.LEFT);
    cLabel11.setText("Descr. Campo 3");
    cLabel11.setBounds(new Rectangle(6, 249, 115, 17));
    cLabel12.setHorizontalAlignment(SwingConstants.RIGHT);
    cLabel12.setHorizontalTextPosition(SwingConstants.LEFT);
    cLabel12.setText("Descr. Campo 4");
    cLabel12.setBounds(new Rectangle(6, 268, 115, 17));
    cLabel13.setHorizontalAlignment(SwingConstants.RIGHT);
    cLabel13.setHorizontalTextPosition(SwingConstants.LEFT);
    cLabel13.setText("Descr. Campo 5");
    cLabel13.setBounds(new Rectangle(6, 288, 115, 17));
    Bcancelar.setBounds(new Rectangle(351, 313, 131, 29));
    Baceptar.setBounds(new Rectangle(109, 313, 131, 29));
    cla_deset5E.setBounds(new Rectangle(124, 288, 384, 17));
    cla_deset4E.setBounds(new Rectangle(124, 268, 384, 17));
    cla_deset3E.setBounds(new Rectangle(124, 249, 384, 17));
    cla_deset2E.setBounds(new Rectangle(124, 229, 384, 17));
    cla_deset1E.setBounds(new Rectangle(124, 209, 384, 17));
    cLabel9.setBounds(new Rectangle(6, 209, 115, 17));
    jScrollPane1.setBounds(new Rectangle(124, 140, 383, 65));
    cLabel8.setBounds(new Rectangle(38, 140, 83, 16));
    cLabel7.setBounds(new Rectangle(5, 118, 115, 19));
    cla_codbarE.setBounds(new Rectangle(124, 118, 384, 17));
    cla_diaconE.setBounds(new Rectangle(473, 99, 35, 17));
    cLabel6.setBounds(new Rectangle(361, 99, 106, 17));
    eti_codiE.setBounds(new Rectangle(124, 99, 181, 17));
    cLabel5.setBounds(new Rectangle(44, 97, 77, 19));
    cla_obspedE.setBounds(new Rectangle(124, 77, 384, 20));
    cLabel4.setBounds(new Rectangle(6, 77, 115, 19));
    pro_nombE.setBounds(new Rectangle(124, 55, 384, 20));
    cLabel3.setBounds(new Rectangle(6, 55, 115, 17));
    pro_codiE.setBounds(new Rectangle(94, 22, 391, 17));
    cLabel2.setBounds(new Rectangle(39, 22, 58, 17));
    cli_codiE.setBounds(new Rectangle(94, 1, 391, 17));
    cLabel1.setBounds(new Rectangle(38, 1, 55, 17));
    jScrollPane1.getViewport().add(cla_ingredE, null);

     cLabel1.setText("Cliente");
     cLabel2.setText("Producto");
     cLabel3.setHorizontalAlignment(SwingConstants.RIGHT);
    cLabel3.setHorizontalTextPosition(SwingConstants.LEFT);
    cLabel3.setText("Descr. para Alb/Fra");
     cLabel4.setHorizontalAlignment(SwingConstants.RIGHT);
    cLabel4.setHorizontalTextPosition(SwingConstants.LEFT);
    cLabel4.setText("Observ. s/pedido");
     cLabel5.setHorizontalAlignment(SwingConstants.RIGHT);
    cLabel5.setHorizontalTextPosition(SwingConstants.LEFT);
    cLabel5.setText("Etiqueta");
    Pprinc.setLayout(null);

     cLabel6.setText("Dias de Consumo");

    cLabel7.setText("Codigo Barras");
    cLabel7.setHorizontalTextPosition(SwingConstants.LEFT);
    cLabel7.setHorizontalAlignment(SwingConstants.RIGHT);
    cLabel8.setVerifyInputWhenFocusTarget(true);
    cLabel8.setText("Ingredientes");
    cLabel9.setText("Descr. Campo 1");
    cLabel9.setHorizontalTextPosition(SwingConstants.LEFT);
    cLabel9.setHorizontalAlignment(SwingConstants.RIGHT);
    this.getContentPane().add(Pprinc,  BorderLayout.CENTER);
    Pprinc.add(pro_nombE, null);
    Pprinc.add(cLabel3, null);
    Pprinc.add(cLabel4, null);
    Pprinc.add(cla_obspedE, null);
    Pprinc.add(eti_codiE, null);
    Pprinc.add(cLabel5, null);
    Pprinc.add(cla_diaconE, null);
    Pprinc.add(cLabel6, null);
    Pprinc.add(cli_codiE, null);
    Pprinc.add(cLabel1, null);
    Pprinc.add(cLabel2, null);
    Pprinc.add(pro_codiE, null);
    Pprinc.add(cLabel7, null);
    Pprinc.add(cla_codbarE, null);
    Pprinc.add(cLabel8, null);
    Pprinc.add(jScrollPane1, null);
    Pprinc.add(cla_deset1E, null);
    Pprinc.add(cLabel9, null);
    Pprinc.add(cla_deset2E, null);
    Pprinc.add(cLabel10, null);
    Pprinc.add(cla_deset3E, null);
    Pprinc.add(cLabel11, null);
    Pprinc.add(cla_deset4E, null);
    Pprinc.add(cLabel12, null);
    Pprinc.add(Bcancelar, null);
    Pprinc.add(cLabel13, null);
    Pprinc.add(cla_deset5E, null);
    Pprinc.add(Baceptar, null);
   }

    @Override
   public void iniciarVentana() throws Exception
   {
     Pprinc.setDefButton(Baceptar);
     eti_codiE.setDatos(etiqueta.getReports(dtStat, EU.em_cod,0));
     cli_codiE.iniciar(dtStat, this, vl, EU);
     pro_codiE.iniciar(dtStat, this, vl, EU);
     pro_codiE.setAceptaInactivo(false);
     cli_codiE.setColumnaAlias("cli_codi");
     pro_codiE.setColumnaAlias("pro_codi");
     pro_nombE.setColumnaAlias("pro_nomb");

     verDatos();
   }

    @Override
   public void PADDelete()
   {
     try
     {
       if (!setBloqueo(dtAdd, "v_cliart", cli_codiE.getText() + "-" + pro_codiE.getText()))
       {
         msgBox(msgBloqueo);
         nav.pulsado = navegador.NINGUNO;
         activaTodo();
         return;
       }
       s = "SELECT * FROM v_cliart WHERE cli_codi = " + cli_codiE.getValorInt() +
           " and pro_codi = " + pro_codiE.getValorInt();
       if (!dtAdd.select(s, true))
       {
         mensajeErr("Registro ha sido borrado");
         resetBloqueo(dtAdd, "v_cliart", cli_codiE.getText() + "-" + pro_codiE.getText());
         activaTodo();
         mensaje("");
         return;
       }

     }
     catch (Exception k)
     {
       Error("Error al bloquear el registro", k);
       return;
     }
     Baceptar.setEnabled(true);
     Bcancelar.setEnabled(true);
     Bcancelar.requestFocus();
     mensaje("BORRANDO Registro Activo ...");
   }

public void ej_delete1()
{

  try
  {
    dtAdd.delete(stUp);
    resetBloqueo(dtAdd, "v_cliart", cli_codiE.getText()+"-"+pro_codiE.getText(), false);
    ctUp.commit();
    rgSelect();
  }
  catch (Exception ex)
  {
    Error("Error al borrar Registro", ex);
  }

  activaTodo();
  verDatos();
  mensaje("");
  mensajeErr("Registro ... Borrado");
}

  @Override
  public void canc_delete()
{
  mensaje("");
  activaTodo();
  try
  {
    resetBloqueo(dtAdd, "v_cliart", cli_codiE.getText()+"-"+pro_codiE.getText(), true);
  }
  catch (Exception k)
  {
    Error("Error al Anular bloqueo", k);
  }
  mensajeErr("Borrado de Datos Cancelada");
  verDatos();
}
void verDatos()
 {
   try
   {
     if (dtCons.getNOREG())
       return;

     cli_codiE.setText(dtCons.getString("cli_codi"));
     pro_codiE.setText(dtCons.getString("pro_codi"));

     s = "SELECT * FROM v_cliart WHERE cli_codi = " + cli_codiE.getValorInt()+
         " and pro_codi = "+pro_codiE.getValorInt();
     if (!dtCon1.select(s))
     {
       mensajeErr("REGISTRO DE CLIENTE/ARTICULO NO ENCONTRADO ... SEGURAMENTE SE BORRO");
       Pprinc.resetTexto();
       cli_codiE.setText(dtCons.getString("cli_codi"));
       pro_codiE.setText(dtCons.getString("pro_codi"));
       return;
     }
     pro_nombE.setText(dtCon1.getString("pro_nomb"));
     cla_obspedE.setText(dtCon1.getString("cla_obsped"));
     cla_codbarE.setText(dtCon1.getString("cla_codbar"));
     eti_codiE.setText(dtCon1.getString("eti_codi"));
     cla_ingredE.setText(dtCon1.getString("cla_ingred"));
     cla_diaconE.setValorInt(dtCon1.getInt("cla_diacon"));
     cla_deset1E.setText(dtCon1.getString("cla_deset1"));
     cla_deset2E.setText(dtCon1.getString("cla_deset2"));
     cla_deset3E.setText(dtCon1.getString("cla_deset3"));
     cla_deset4E.setText(dtCon1.getString("cla_deset4"));
     cla_deset5E.setText(dtCon1.getString("cla_deset5"));
   }  catch (Exception k)
   {
     Error("Error al ver Datos", k);
     return;
   }
 }
 public void activar(boolean enab)
 {
   activar(enab,navegador.TODOS);
 }
 public void activar(boolean enab,int modo)
  {
    cli_codiE.setEnabled(enab);
    pro_codiE.setEnabled(enab);
    pro_nombE.setEnabled(enab);
    Baceptar.setEnabled(enab);
    Bcancelar.setEnabled(enab);
    if (modo==navegador.QUERY)
      return;
    cla_obspedE.setEnabled(enab);
    cla_codbarE.setEnabled(enab);
    eti_codiE.setEnabled(enab);
    cla_ingredE.setEnabled(enab);
    cla_diaconE.setEnabled(enab);
    cla_deset1E.setEnabled(enab);
    cla_deset2E.setEnabled(enab);
    cla_deset3E.setEnabled(enab);
    cla_deset4E.setEnabled(enab);
    cla_deset5E.setEnabled(enab);
  }

  public void PADPrimero()
  {
    verDatos();
  }

  public void PADAnterior()
  {
    verDatos();
  }

  public void PADSiguiente()
  {
    verDatos();
  }

  public void PADUltimo()
  {
    verDatos();
  }

  public void PADQuery()
  {
    activar(true,navegador.QUERY);
    Pprinc.setQuery(true);
    Pprinc.resetTexto();
    cli_codiE.requestFocus(); // Poner el foco al primer campo
  }

  public void ej_query1()
  {
    Component c;

    if ( (c = Pprinc.getErrorConf()) != null)
    {
      c.requestFocus();
      mensaje("Error en Criterios de busqueda");
      return;
    }

    Vector v = new Vector();
    v.addElement(cli_codiE.getStrQuery());
    v.addElement(pro_codiE.getStrQuery());
    v.addElement(pro_nombE.getStrQuery());

    s = "SELECT * FROM v_cliart ";
    s = creaWhere(s, v, true);
    s += " ORDER BY cli_codi,pro_codi ";
    Pprinc.setQuery(false);

//    debug(s);
    try
    {
      if (!dtCons.select(s))
      {
        mensaje("");
        mensajeErr("No encontrados Registros para estos criterios");
        rgSelect();
        activaTodo();
        verDatos();
        return;
      }
      mensaje("");
      strSql = s;
      activaTodo();
      rgSelect();
      verDatos();
      mensajeErr("Nuevos registros selecionados");
    }
    catch (Exception ex)
    {
      fatalError("Error al buscar REGISTROS: ", ex);
    }

  }

  public void canc_query()
  {
    Pprinc.setQuery(false);
    mensaje("");
    mensajeErr("Consulta ... CANCELADA");
    activaTodo();
    verDatos();
  }
  public void ej_edit1()
   {
     try
     {
       dtAdd.edit();
       actValores(dtAdd);
       dtAdd.update(stUp);
       resetBloqueo(dtAdd, "v_cliart", cli_codiE.getText()+"-"+pro_codiE.getText(), false);
       ctUp.commit();
     }
     catch (Throwable ex)
     {
       Error("Error al Modificar datos", ex);
       return;
     }
     mensaje("");
     mensajeErr("Datos ... Modificados");
     activaTodo();
     verDatos();
   }
   public void PADAddNew()
   {
     Pprinc.resetTexto();
     cla_ingredE.setText("");
     activar(true);
     cli_codiE.requestFocus();
     mensaje("Dando de alta un Nuevo registro ....");
   }
   public void PADEdit()
   {
     activar(true);
     cli_codiE.setEnabled(false);
     pro_codiE.setEnabled(false);
     try
     {
       if (!setBloqueo(dtAdd, "v_cliart", cli_codiE.getText()+"-"+pro_codiE.getText()))
        {
          msgBox(msgBloqueo);
          nav.pulsado = navegador.NINGUNO;
          activaTodo();
          return;
        }
        s = "SELECT * FROM v_cliart WHERE cli_codi = " + cli_codiE.getValorInt()+
           " and pro_codi = "+pro_codiE.getValorInt();
       if (! dtAdd.select(s,true))
       {
         mensajeErr("Registro ha sido borrado");
         resetBloqueo(dtAdd,"v_cliart", cli_codiE.getText()+"-"+pro_codiE.getText());
         activaTodo();
         mensaje("");
        return;
       }

     } catch (Exception k)
     {
       Error("Error al bloquear el registro", k);
       return;
     }
     pro_nombE.requestFocus();
     mensaje("MODIFICANDO registro activo ....");
  }

   public void canc_edit()
   {
     mensaje("");
     try
     {
       resetBloqueo(dtAdd, "v_cliart", cli_codiE.getText()+"-"+pro_codiE.getText(), true);
     }
     catch (Exception ex)
     {
       Error("Error al Quitar Bloqueo", ex);
       return;
     }

     mensajeErr("Modificacion de Datos Cancelada");
     activaTodo();
     verDatos();
   }
   public void ej_addnew1()
   {
     try
     {
       // Comprobamos que no existe el c�digo
       s = "SELECT * FROM v_cliart WHERE cli_codi = " + cli_codiE.getValorInt()+
           " and pro_codi = "+pro_codiE.getValorInt();

       if (dtCon1.select(s))
       {
         mensajeErr("CLIENTE YA TIENE ESE CODIGO DE PRODUCTO ASIGNADO");
         cli_codiE.requestFocus();
         return;
       }
       dtAdd.addNew("v_cliart");
       dtAdd.setDato("cli_codi", cli_codiE.getValorInt());
       dtAdd.setDato("pro_codi", pro_codiE.getText());
       actValores(dtAdd);
       dtAdd.update(stUp);
       ctUp.commit();
     } catch (Exception ex)
     {
       Error("Error al Insertar datos", ex);
       return;
     }
     mensaje("");
     mensajeErr("Registro ... Insertado");
     activaTodo();
   }
   public void canc_addnew()
   {
     mensaje("");
     activaTodo();
     mensajeErr("Insercion de Datos Cancelada");
     verDatos();
   }

   void actValores(DatosTabla dt) throws Exception
   {
     dt.setDato("pro_nomb", pro_nombE.getText());
     dt.setDato("cla_obsped", cla_obspedE.getText());
     dt.setDato("cla_codbar", cla_codbarE.getText());
     dt.setDato("eti_codi", eti_codiE.getValorInt());
     dt.setDato("eti_codi", eti_codiE.getValorInt());
     dt.setDato("cla_ingred", cla_ingredE.getText());
     dt.setDato("cla_diacon", cla_diaconE.getText());

     dt.setDato("cla_deset1", cla_deset1E.getText());
     dt.setDato("cla_deset2", cla_deset2E.getText());
     dt.setDato("cla_deset3", cla_deset4E.getText());
     dt.setDato("cla_deset4", cla_deset4E.getText());
     dt.setDato("cla_deset5", cla_deset5E.getText());
   }

   public boolean checkAddNew()
   {
     try
     {
       if (cli_codiE.isNull())
       {
         mensajeErr("Introduzca un codigo para el CLIENTE");
         cli_codiE.requestFocus();
         return false;
       }
       if (!cli_codiE.controlar())
       {
         mensajeErr("CODIGO DE CLIENTE .. NO VALIDO");
         cli_codiE.requestFocus();
         return false;
       }
//       if (pro_codiE.isNull())
//       {
//         mensajeErr("Introduzca un codigo para el Producto");
//         pro_codiE.requestFocus();
//         return false;
//       }
       if (! pro_codiE.controlar())
       {
         mensajeErr(pro_codiE.getMsgError());
         return false;
       }


     }
     catch (Exception k)
     {
       Error("Error al CHEQUEAR validez de Campos", k);
       return false;
     }
     return true;
   }
 }
