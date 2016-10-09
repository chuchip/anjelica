package gnu.chu.anjelica.pad;

import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import java.sql.*;
import gnu.chu.Menu.*;
import java.awt.*;
import java.util.*;
import gnu.chu.interfaces.*;
import java.awt.event.*;
import gnu.chu.sql.DatosTabla;

/**
 *
 * <p>Titulo:   pdtipotar </p>
 * <p>Descripción: Mantenimiento de Tipos de Tarifas </p>
 * <p>Empresa: miCasa</p>
 *  <p>Copyright: Copyright (c) 2005-2016
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
 * @author ChuchiP
 * @version 1.0
 */
public class pdtipotar extends ventanaPad implements PAD
{
  boolean modoConsulta=false;
  CPanel Pprinc = new CPanel();
  CLabel cLabel1 = new CLabel();
  CTextField tar_codiE = new CTextField(Types.DECIMAL,"###9");
  CTextField tar_nombE = new CTextField();
  CLabel cLabel2 = new CLabel();
  CComboBox tar_tipoE = new CComboBox();
  CLabel cLabel3 = new CLabel();
  CTextField tar_codoriE = new CTextField(Types.DECIMAL,"###9");
  CTextField tar_nomoriL = new CTextField();
  CLabel cLabel4 = new CLabel();
  CTextField tar_incpreE = new CTextField(Types.DECIMAL,"#9.99");

  public pdtipotar(EntornoUsuario eu, Principal p)
  {
    this(eu,p,null);
  }

  public pdtipotar(EntornoUsuario eu, Principal p, Hashtable ht)
  {
      EU = eu;
      vl = p.panel1;
      jf = p;
      eje = true;

      setTitulo("Mant. Tipos Tarifas");

      try
      {
        if (ht != null)
        {
          if (ht.get("modoConsulta") != null)
            modoConsulta = Boolean.valueOf(ht.get("modoConsulta").toString()).booleanValue();
        }

        if (jf.gestor.apuntar(this))
          jbInit();
        else
          setErrorInit(true);
      }
      catch (Exception e)
      {
        logger.fatal("Error en inicio programa pdtipotar",e);
        setErrorInit(true);
      }
    }

    public pdtipotar(gnu.chu.anjelica.menu p, EntornoUsuario eu)
    {

      EU = eu;
      vl = p.getLayeredPane();
      setTitulo("Mant. Tipos Tarifas");
      eje = false;

      try
      {
        jbInit();
      }
      catch (Exception e)
      {
        logger.fatal("Error en inicio programa pdtipotar",e);
        setErrorInit(true);
      }
    }

    private void jbInit() throws Exception
    {
      iniciar(502, 522);
      this.setSize(new Dimension(507, 237));
      this.setVersion("2016-05-07");
      strSql = "SELECT * FROM tipotari" +
          " order by tar_codi";

      statusBar = new StatusBar(this);
      nav = new navegador(this, dtCons, false, navegador.NORMAL);
      conecta();
      iniciar(this);
      Pprinc.setLayout(null);

      tar_tipoE.addItem("Automat.","S");
      tar_tipoE.addItem("No Autom.","N");
      cLabel2.setText("Tipo");
      cLabel2.setBounds(new Rectangle(9, 28, 66, 17));

      cLabel3.setText("Tarifa Padre");
      cLabel3.setBounds(new Rectangle(9, 52, 74, 17));
      tar_nomoriL.setBackground(Color.orange);
      tar_nomoriL.setForeground(Color.white);
      tar_nomoriL.setEnabled(false);
    tar_nomoriL.setBounds(new Rectangle(131, 52, 358, 17));
      cLabel4.setText("Incrementar ");
    cLabel4.setBounds(new Rectangle(9, 74, 71, 17));
      Baceptar.setBounds(new Rectangle(74, 101, 110, 29));
    Baceptar.setText("Aceptar");
      Bcancelar.setBounds(new Rectangle(257, 101, 110, 29));
    Bcancelar.setText("Cancelar");
      tar_incpreE.setBounds(new Rectangle(81, 74, 43, 17));
    tar_codoriE.setBounds(new Rectangle(81, 52, 45, 17));
    tar_tipoE.setBounds(new Rectangle(81, 28, 125, 20));
    tar_nombE.setBounds(new Rectangle(131, 7, 358, 17));
    tar_codiE.setBounds(new Rectangle(81, 7, 45, 17));
    cLabel1.setBounds(new Rectangle(9, 7, 68, 17));
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
      cLabel1.setText("Tipo Tarifa");

      Pprinc.add(cLabel1, null);
      Pprinc.add(tar_codiE, null);
      Pprinc.add(tar_nombE, null);
      Pprinc.add(tar_tipoE, null);
      Pprinc.add(cLabel2, null);
      Pprinc.add(cLabel3, null);
      Pprinc.add(tar_codoriE, null);
      Pprinc.add(tar_nomoriL, null);
      Pprinc.add(cLabel4, null);
      Pprinc.add(tar_incpreE, null);
      Pprinc.add(Bcancelar, null);
      Pprinc.add(Baceptar, null);

    }

    public void iniciarVentana()
    {
      Pprinc.setButton(KeyEvent.VK_F4, Baceptar);

      tar_codiE.setColumnaAlias("tar_codi");
      tar_nombE.setColumnaAlias("tar_nomb");
      tar_tipoE.setColumnaAlias("tar_tipo");
      tar_codoriE.setColumnaAlias("tar_codori");
      tar_incpreE.setColumnaAlias("tar_incpre");
      activarEventos();
      activaTodo();
      verDatos();
    }
    void activarEventos()
    {
      tar_codoriE.addFocusListener(new FocusAdapter()
      {
            @Override
        public void focusLost(FocusEvent e)
        {
          try
          {
            if (nav.pulsado==navegador.QUERY)
              return;
            if (tar_codoriE.getValorInt() == 0)
            {
              tar_nomoriL.setText("**SIN TARIFA PADRE**");
            }
            else
            {
              getNombTarifa(tar_codoriE.getValorInt(), true);
            }
          } catch (SQLException k)
          {
            Error("Error al Buscar Tarifa Padre",k);
          }
        }
      });
    }
    public void verDatos()
    {
      try
      {
        if (dtCons.getNOREG())
          return;
        tar_codiE.setValorInt(dtCons.getInt("tar_codi"));
        String s = "SELECT * FROM tipotari WHERE tar_codi = " + tar_codiE.getValorInt();
        if (!dtCon1.select(s))
        {
          mensajeErr("Tipo de Tarifa NO ENCONTRADA ... SEGURAMENTE SE BORRO");
          Pprinc.resetTexto();
          tar_codiE.setValorInt(dtCons.getInt("tar_codi"));
          return;
        }

        tar_nombE.setText(dtCon1.getString("tar_nomb"));

        tar_tipoE.setValor(dtCon1.getString("tar_tipo"));
        tar_codoriE.setValorInt(dtCon1.getInt("tar_codori"));
        if (tar_codoriE.getValorInt() == 0)
          tar_nomoriL.setText("**SIN TARIFA PADRE**");
        else
          getNombTarifa(tar_codoriE.getValorInt(), true);
        tar_incpreE.setValorDec(dtCon1.getDouble("tar_incpre"));
      }
      catch (Exception k)
      {
        Error("Error al ver Datos", k);
        return;
      }

    }
    @Override
    public void PADPrimero(){verDatos();  }

    @Override
     public void PADAnterior(){verDatos();     }

    @Override
     public void PADSiguiente(){verDatos();}
    @Override
     public void PADUltimo(){verDatos();}

    @Override
     public void PADQuery()
     {
       activar(true);
       Pprinc.setQuery(true);
       Pprinc.resetTexto();
       tar_codiE.requestFocus(); // Poner el foco al primer campo
     }



    @Override
     public void ej_query1()
     {
       Component c;

       if ( (c = Pprinc.getErrorConf()) != null)
       {
         c.requestFocus();
         mensaje("Error en Criterios de busqueda");
         return;
       }

       ArrayList v = new ArrayList();
       v.add(tar_codiE.getStrQuery());
       v.add(tar_nombE.getStrQuery());
       v.add(tar_tipoE.getStrQuery());
       v.add(tar_codoriE.getStrQuery());
       v.add(tar_incpreE.getStrQuery());

       String s = "SELECT * FROM tipotari ";
       s = creaWhere(s, v, true);
       s += " ORDER BY tar_codi ";
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
         fatalError("Error al buscar Tipos de Tarifa: ", ex);
       }
     }

    @Override
     public void canc_query()
     {
       Pprinc.setQuery(false);
       mensaje("");
       mensajeErr("Consulta ... CANCELADA");
       activaTodo();
       verDatos();
     }

    @Override
     public void PADEdit()
     {
       activar(true);
       tar_codiE.setEnabled(false);
       try
       {
         if (!setBloqueo(dtAdd, "tipotari", ""+tar_codiE.getValorInt()))
         {
           msgBox(msgBloqueo);
           nav.pulsado = navegador.NINGUNO;
           activaTodo();
           dtAdd.getConexion().rollback();
           return;
         }

         if (!dtAdd.select("select * from tipotari where tar_codi= " +
                           tar_codiE.getValorInt() , true))
         {
           mensajeErr("Registro ha sido borrado");
           resetBloqueo(dtAdd,  "tipotari", ""+tar_codiE.getValorInt());
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
       tar_nombE.requestFocus();
     }
    @Override
     public void ej_edit1()
     {
       try
       {
         if (!checkValores())
           return;
         dtAdd.edit();
         actValores(dtAdd);
         dtAdd.update(stUp);
         resetBloqueo(dtAdd,  "tipotari",""+tar_codiE.getValorInt(), false);
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


    @Override
     public void canc_edit()
     {
       mensaje("");
       try
       {
         resetBloqueo(dtAdd, "tipotari", "" + tar_codiE.getValorInt(), true);
       }
       catch (Exception ex)
       {
         Error("Error al Quitar Bloqueo", ex);
         return;
       }

       mensajeErr("Modificación de Datos Cancelada");
       activaTodo();
       verDatos();
     }

    @Override
     public void PADAddNew(){
       Pprinc.resetTexto();
       try  {
         tar_codiE.setValorInt(getMaxTarifa() + 1);
       } catch (SQLException k)
       {
         Error("Error al recoger N�mero Máximo de Tarifa",k);
         return;
       }
       activar(true);
       tar_codiE.requestFocus();
       mensaje("Insertando Tarifa ...");
     }
     int getMaxTarifa() throws SQLException
     {
       String s="select max(tar_codi)  as tar_codi from tipotari";
       dtStat.select(s);
       return dtStat.getInt("tar_codi",true);
     }

    @Override
     public void ej_addnew1()
     {
       try
       {
         // Comprobamos que no existe el código
         String s = "SELECT * FROM tipotari WHERE tar_codi = " + tar_codiE.getValorInt();
         if (dtCon1.select(s))
         {
           mensajeErr("Tipo de Tarifa YA EXISTE");
           tar_codiE.requestFocus();
           return;
         }
         if (!checkValores())
           return;
         // A�adimos el Registro
         dtAdd.addNew("tipotari");
         actValores(dtAdd);
         dtAdd.update(stUp);
         ctUp.commit();
       }
       catch (Exception ex)
       {
         Error("Error al Insertar datos", ex);
         return;
       }

       mensaje("");
       mensajeErr("Tipo Tarifa ... Insertada");
       activaTodo();
     }
     boolean checkValores() throws SQLException
     {
       if (tar_nombE.isNull())
       {
         mensajeErr("Introduzca Descripcion de la Tarifa");
         tar_nombE.requestFocus();
         return false;
       }
       if (tar_codoriE.getValorInt()==0)
       {
            if (tar_incpreE.getValorDec()!=0)
            {
               mensajeErr("Si no tiene tarifa padre, no puede tener importe a Incrementar");
               tar_incpreE.requestFocus();
               return false; 
            }
            return true;  // No depende de otra tarifa.
       }
       if (getNombTarifa(tar_codoriE.getValorInt(),false)==null)
       {
         mensajeErr("Código Tarifa Original NO existe");
         tar_codoriE.requestFocus();
         return false;
       }
//       if (! dtStat.getString("tar_tipo").equals(tar_tipoE.getValor()))
//       {
//           mensajeErr("Tipo de tarifa debe ser igual al del padre");
//           tar_tipoE.requestFocus();
//           return false;
//       }
       if (tar_incpreE.getValorDec()==0)
       {
         mensajeErr("Introduzca el importe a Incrementar");
         tar_incpreE.requestFocus();
         return false;
       }
      
       return true;
     }
     /**
      * Devuelve el nobmbre de la tarifa
      * @param tarCodi Codigo de la tarifa
      * @param print Si debe imprimir en el campo tar_nomoriL el nombre del tipo de tarifa
      * @return Nombre del tipo de tarifa
      * @throws java.sql.SQLException
      */
     String getNombTarifa(int tarCodi, boolean print) throws SQLException
     {
       String s = "SELECT * FROM tipotari WHERE tar_codi = " + tarCodi;
       if (!dtStat.select(s))
       {
         if (print)
           tar_nomoriL.setText("**TARIFA PADRE NO ENCONTRADA**");
         return null;
       }
       else
       {
         if (print)
           tar_nomoriL.setText(dtStat.getString("tar_nomb"));
         return dtStat.getString("tar_nomb");
       }
     }
     void actValores(DatosTabla dt) throws Exception
     {
       dt.setDato("tar_codi", tar_codiE.getValorInt());
       dt.setDato("tar_nomb", tar_nombE.getText());
       dt.setDato("tar_tipo", tar_tipoE.getValor());
       dt.setDato("tar_codori",tar_codoriE.getValorInt());
       dt.setDato("tar_incpre",tar_incpreE.getValorDec());
     }


  @Override
     public void canc_addnew()
     {
       mensaje("");
       activaTodo();
       mensajeErr("Insercion de Datos Cancelada");
       verDatos();
     }

     public void PADDelete()
     {

       Baceptar.setEnabled(true);
       Bcancelar.setEnabled(true);
       try
       {
         if (!setBloqueo(dtAdd, "tipotari", "" + tar_codiE.getValorInt()))
         {
           msgBox(msgBloqueo);
           nav.pulsado = navegador.NINGUNO;
           activaTodo();
           dtAdd.getConexion().rollback();
           return;
         }

         if (!dtAdd.select("select * from tipotari where tar_codi= " +
                           tar_codiE.getValorInt(), true))
         {
           mensajeErr("Registro ha sido borrado");
           resetBloqueo(dtAdd, "tipotari", "" + tar_codiE.getValorInt());
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
       Bcancelar.requestFocus();

     }

     public void ej_delete1()
     {

       try
       {
         dtAdd.delete(stUp);
         resetBloqueo(dtAdd, "tipotari", "" + tar_codiE.getValorInt(), false);
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

     public void canc_delete()
     {
       mensaje("");
       try
       {
         resetBloqueo(dtAdd, "tipotari", "" + tar_codiE.getValorInt(), true);
       }
       catch (Exception ex)
       {
         Error("Error al Quitar Bloqueo", ex);
         return;
       }

       mensajeErr("Borrado de Datos Cancelada");
       activaTodo();
       verDatos();
     }

  @Override
     public void activar(boolean b)
     {
         Baceptar.setEnabled(b);
         Bcancelar.setEnabled(b);
         tar_codiE.setEnabled(b);
         tar_nombE.setEnabled(b);
         tar_tipoE.setEnabled(b);
         tar_codoriE.setEnabled(b);
         tar_incpreE.setEnabled(b);
     }
     public static void llenaLinkBox(CLinkBox lkbox, DatosTabla dt ) throws SQLException
     {
        String s="select tar_codi,tar_nomb from tipotari order by tar_nomb";
        dt.select(s);
        lkbox.addDatos(dt);
     }
     /**
      * Carga datos tabla con Codigo Tarifa y nombre de tarifa
      * @param dt
      * @param tarPadre cuyo padre sea el mandado. -1 Significa TODOS
      * @return DatosTabla con los datos
      * @throws SQLException 
      */
     public static DatosTabla  getTiposTarifa(DatosTabla dt, int tarPadre ) throws SQLException
     {
        String s="select tar_codi,tar_nomb from tipotari where 1=1"+
            (tarPadre>=0? " and tar_codori="+tarPadre:"")+
          "   order by tar_nomb";
        dt.select(s);
        return dt;
     }
     public static boolean  getPonerPrecios(DatosTabla dt, int tarCodi ) throws SQLException
     {
         String s="select tar_tipo from tipotari where "+
           "  tar_codori="+tarCodi;
         if (!dt.select(s))
             return false;
         return dt.getString("tar_tipo").equals("S");
     }
     
}
