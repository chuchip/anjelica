package gnu.chu.anjelica.pad;

import gnu.chu.utilidades.*;
import java.sql.*;
import gnu.chu.Menu.*;
import java.awt.*;
import java.util.*;
import gnu.chu.interfaces.*;
import gnu.chu.utilidades.ventanaPad;
import java.awt.event.KeyEvent;
import gnu.chu.controles.*;
import gnu.chu.camposdb.*;

import gnu.chu.sql.DatosTabla;

/**
 *
 * <p>Título: pdsaladesp </p>
 * <p>Descripción: Mantenimiento Tabla de Salas de Despiece</p>
 * <p>Copyright: Copyright (c) 2005
 *    Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
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
public class pdsaladesp extends ventanaPad   implements PAD
{
  String s;
  CPanel Pprinc = new CPanel();
  CLabel sde_codiL = new CLabel();
  CTextField sde_codiE = new CTextField(Types.DECIMAL,"####9");
  CTextField sde_nombE = new CTextField(Types.CHAR,"X",50);
//  CButton Baceptar = new CButton();
//  CButton Bcancelar = new CButton();

  boolean modConsulta=true;
  CTextField sde_poblE = new CTextField(Types.CHAR,"X",50);
  CTextField sde_codposE = new CTextField(Types.CHAR,"X",15);
  CLabel cLabel4 = new CLabel();
  CLabel cLabel33 = new CLabel();
  CTextField sde_telefE = new CTextField(Types.CHAR,"X",15);
  CTextField cli_faxE = new CTextField(Types.CHAR,"X",15);
  CLabel cLabel7 = new CLabel();
  CLabel cLabel5 = new CLabel();
  CLabel cLabel3 = new CLabel();
  CTextField sde_direcE = new CTextField(Types.CHAR,"X",50);
  CLabel cLabel6 = new CLabel();
  CTextField sde_proviE = new CTextField(Types.CHAR,"X",50);
  CTextField sde_faxE = new CTextField(Types.CHAR,"X",15);
  CLabel cLabel8 = new CLabel();
  CLabel sde_nifL = new CLabel();
  CTextField sde_nifE = new CTextField(Types.CHAR,"X",15);
  CLabel sde_nifL1 = new CLabel();
  CTextField sde_nuexplE = new CTextField(Types.CHAR,"X",15);
  CLinkBox pai_codiE = new CLinkBox();
  CLabel cLabel15 = new CLabel();
  CLabel sde_nrgsaL = new CLabel();
  CTextField sde_nrgsaE = new CTextField(Types.CHAR,"X",12);
  CLabel sde_orgconL = new CLabel();
  CTextField sde_orgconE = new CTextField(Types.CHAR,"X",15);
  CTextField sde_comenE = new CTextField(Types.CHAR,"X",50);
  CGridEditable jt = new CGridEditable(2)
  {
    public int cambiaLinea(int row, int col)
    {
      return checkPrv() ? -1 : 0;
    }

    public void cambiaColumna(int col,int colNueva, int row)
    {
      String nombArt;
      try
      {
        if (col == 0)
        {
          nombArt = prv_codiE.getNombPrv(prv_codiE.getText());
          jt.setValor(nombArt, row, 1);
          prv_codiE.resetCambio();
        }
      }
      catch (Exception k)
      {
        Error("Error al buscar Nombre Sala Despiece", k);
      }
    }
  };
  prvPanel prv_codiE = new prvPanel();
  CTextField prv_nombE = new CTextField();
  CLabel sde_orgconL1 = new CLabel();

  public pdsaladesp(EntornoUsuario eu, Principal p)
  {
    this(eu,p,null);
  }
  public pdsaladesp(EntornoUsuario eu, Principal p,Hashtable ht)
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
      setTitulo("Mant. Salas de Despiece ");;

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

  public pdsaladesp(gnu.chu.anjelica.menu p, EntornoUsuario eu,Hashtable ht)
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
      setTitulo("Mant. Salas de Despiece");
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
    this.setSize(new Dimension(515, 432));
    this.setVersion("2005-11-16"+(modConsulta?"SOLO LECTURA":""));
    strSql = "SELECT * FROM v_saladesp "+
        " ORDER BY sde_codi ";

    statusBar = new StatusBar(this);
    conecta();
    nav = new navegador(this, dtCons, false, modConsulta? navegador.CURYCON:navegador.NORMAL);
    Iniciar(this);


    Pprinc.setDefButton(Baceptar);
    Pprinc.setEscButton(Bcancelar);
    Pprinc.setLayout(null);
    sde_codiL.setFont(new java.awt.Font("Dialog", 1, 11));
    sde_codiL.setText("Sala Despiece");
    sde_codiL.setBounds(new Rectangle(1, 4, 88, 17));// NO utilizado Siempre SI

    Baceptar.setBounds(new Rectangle(380, 257, 120, 28));
    Bcancelar.setBounds(new Rectangle(380, 307, 120, 27));
    sde_nombE.setBounds(new Rectangle(144, 4, 348, 17));
    sde_codiE.setBounds(new Rectangle(91, 4, 46, 17));
    sde_poblE.setBounds(new Rectangle(71, 44, 361, 17));
    sde_codposE.setBounds(new Rectangle(71, 100, 129, 16));
    cLabel4.setBounds(new Rectangle(7, 26, 59, 17));
    cLabel4.setText("Direccion");
    cLabel33.setBounds(new Rectangle(9, 100, 61, 16));
    cLabel33.setText("Cod Postal");
    sde_telefE.setBounds(new Rectangle(71, 81, 129, 17));
    cli_faxE.setBounds(new Rectangle(629, 110, 129, 17));
    cLabel7.setBounds(new Rectangle(597, 110, 29, 18));
    cLabel7.setText("Fax");
    cLabel5.setBounds(new Rectangle(7, 81, 56, 17));
    cLabel5.setText("Telefono");
    cLabel3.setBounds(new Rectangle(7, 44, 59, 17));
    cLabel3.setText("Poblacion");
    sde_direcE.setBounds(new Rectangle(71, 26, 361, 17));
    cLabel6.setText("Provincia");
    cLabel6.setBounds(new Rectangle(7, 63, 59, 17));
    sde_proviE.setBounds(new Rectangle(71, 63, 361, 17));
    sde_faxE.setBounds(new Rectangle(358, 81, 129, 17));
    cLabel8.setText("Fax");
    cLabel8.setBounds(new Rectangle(333, 81, 34, 17));
    sde_nifL.setText("NIF");
    sde_nifL.setBounds(new Rectangle(320, 157, 28, 18));
    sde_nifE.setBounds(new Rectangle(358, 157, 129, 18));
    sde_nifL1.setBounds(new Rectangle(2, 136, 67, 17));
    sde_nifL1.setText("Num. Expl.");
    sde_nuexplE.setToolTipText("Numero de Explotaci�n");
    sde_nuexplE.setBounds(new Rectangle(78, 136, 129, 17));
    pai_codiE.setRequestFocusEnabled(true);
    pai_codiE.setAceptaNulo(true);
    pai_codiE.setAncTexto(45);
    pai_codiE.setBounds(new Rectangle(236, 100, 253, 18));
    cLabel15.setText("Pais");
    cLabel15.setBounds(new Rectangle(206, 100, 29, 18));
    sde_nrgsaL.setText("NRGSA");
    sde_nrgsaL.setBounds(new Rectangle(322, 136, 48, 17));
    sde_nrgsaE.setTipoCampo(1);
    sde_nrgsaE.setText("");
    sde_nrgsaE.setBounds(new Rectangle(382, 136, 105, 17));

    sde_orgconL.setText("Org. Control");
    sde_orgconL.setBounds(new Rectangle(2, 157, 78, 17));
    sde_orgconE.setBounds(new Rectangle(78, 157, 129, 17));
    sde_comenE.setBounds(new Rectangle(78, 177, 361, 17));
    jt.setBounds(new Rectangle(2, 199, 376, 163));
    Vector v= new Vector();
    v.addElement("Cod.");
    v.addElement("Nombre Proveedor");
    jt.setCabecera(v);
    jt.setAnchoColumna(new int[]{60,200});
    jt.setAlinearColumna(new int[]{2,0});
    jt.setAjustarGrid(true);
    prv_nombE.setEnabled(false);
    prv_codiE.setCampoNombre(null);
    Vector c=new Vector();
    c.addElement(prv_codiE.getTextField());
    c.addElement(prv_nombE);
    jt.setCampos(c);
    sde_orgconL1.setBounds(new Rectangle(0, 178, 78, 17));
    sde_orgconL1.setText("Comentario");
    Pprinc.add(sde_codiL, null);
    Pprinc.add(sde_nombE, null);
    Pprinc.add(cli_faxE, null);
    Pprinc.add(cLabel7, null);
    Pprinc.add(sde_direcE, null);
    Pprinc.add(cLabel4, null);
    Pprinc.add(sde_poblE, null);
    Pprinc.add(cLabel3, null);
    Pprinc.add(cLabel6, null);
    this.getContentPane().add(nav, BorderLayout.NORTH);
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);
    this.getContentPane().add(Pprinc,  BorderLayout.CENTER);
    Pprinc.add(sde_proviE, null);
    Pprinc.add(cLabel5, null);
    Pprinc.add(sde_telefE, null);
    Pprinc.add(cLabel8, null);
    Pprinc.add(sde_faxE, null);
    Pprinc.add(sde_nrgsaE, null);
    Pprinc.add(sde_nrgsaL, null);
    Pprinc.add(sde_orgconE, null);
    Pprinc.add(sde_nuexplE, null);
    Pprinc.add(sde_orgconL, null);
    Pprinc.add(sde_nifL1, null);
    Pprinc.add(sde_comenE, null);
    Pprinc.add(jt, null);
    Pprinc.add(Baceptar, null);
    Pprinc.add(Bcancelar, null);
    Pprinc.add(sde_nifE, null);
    Pprinc.add(sde_nifL, null);
    Pprinc.add(pai_codiE, null);
    Pprinc.add(cLabel33, null);
    Pprinc.add(sde_codposE, null);
    Pprinc.add(cLabel15, null);
    Pprinc.add(sde_orgconL1, null);
    Pprinc.add(sde_codiE, null);
  }

  public void iniciarVentana() throws Exception
  {
    Pprinc.setButton(KeyEvent.VK_F4,Baceptar);
    prv_codiE.iniciar(dtStat,this,vl,EU);
    s="SELECT pai_codi,pai_nomb FROM v_paises ORDER BY pai_nomb";
    dtStat.select(s);
    pai_codiE.setFormato(Types.DECIMAL,"###9",2);
    pai_codiE.setFormato(true);
    pai_codiE.addDatos(dtStat);

    sde_codiE.setColumnaAlias("sde_codi");
    sde_nombE.setColumnaAlias("sde_nomb");
    sde_direcE.setColumnaAlias("sde_direc");
    sde_poblE.setColumnaAlias("sde_pobl");
    sde_proviE.setColumnaAlias("sde_provi");
    sde_codposE.setColumnaAlias("sde_codpos");
    sde_telefE.setColumnaAlias("sde_telef");
    sde_faxE.setColumnaAlias("sde_fax");
    sde_nifE.setColumnaAlias("sde_nif");
    sde_nuexplE.setColumnaAlias("sde_nuexpl");
    sde_nrgsaE.setColumnaAlias("sde_nrgsa");
    sde_comenE.setColumnaAlias("sde_comen");
    pai_codiE.setColumnaAlias("pai_codi");
    sde_orgconE.setColumnaAlias("sde_orgcon");

    activarEventos();
    activaTodo();
    verDatos(dtCons);
  }
  void activarEventos()
  {
  }

  void verDatos(DatosTabla dt)
  {
    try {
      if (dt.getNOREG())
        return;

      sde_codiE.setText(dt.getString("sde_codi"));

      s="SELECT * FROM v_saladesp WHERE  sde_codi = "+dt.getInt("sde_codi");
      if (! dtCon1.select(s))
      {
        mensajeErr("Codigo NO ENCONTRADO ... SEGURAMENTE SE BORRO");
        Pprinc.resetTexto();
        sde_codiE.setText(dt.getString("sde_codi"));
        return;
      }
      sde_nombE.setText(dtCon1.getString("sde_nomb"));
      sde_direcE.setText(dtCon1.getString("sde_direc"));
      sde_poblE.setText(dtCon1.getString("sde_pobl"));
      sde_proviE.setText(dtCon1.getString("sde_provi"));
      sde_codposE.setText(dtCon1.getString("sde_codpos"));
      sde_telefE.setText(dtCon1.getString("sde_telef"));
      sde_faxE.setText(dtCon1.getString("sde_fax"));
      sde_nifE.setText(dtCon1.getString("sde_nif"));
      sde_nuexplE.setText(dtCon1.getString("sde_nuexpl"));
      sde_nrgsaE.setText(dtCon1.getString("sde_nrgsa"));
      sde_comenE.setText(dtCon1.getString("sde_comen"));
      pai_codiE.setText(dtCon1.getString("pai_codi"));
      sde_orgconE.setText(dtCon1.getString("sde_orgcon"));
      s="SELECT p.prv_codi,p.prv_nomb FROM v_prvsade m,v_proveedo p"+
          " WHERE sde_codi = "+dt.getString("sde_codi")+
          " and m.prv_codi =  p.prv_codi "+
          " order by p.prv_codi ";
      jt.removeAllDatos();
      if (dtStat.select(s))
      {
        do
        {
          Vector v=new Vector();
          v.addElement(dtStat.getString("prv_codi"));
          v.addElement(dtStat.getString("prv_nomb"));
          jt.addLinea(v);
        } while (dtStat.next());
        jt.requestFocus(0,0);
      }
    } catch (Exception k)
    {
      Error("Error al ver Datos",k);
      return;
    }
  }

  public void activar(boolean act)
  {
    sde_codiE.setEnabled(act);
    sde_nombE.setEnabled(act);
    sde_direcE.setEnabled(act);
    sde_poblE.setEnabled(act);
    sde_proviE.setEnabled(act);
    sde_codposE.setEnabled(act);
    sde_telefE.setEnabled(act);
    sde_faxE.setEnabled(act);
    sde_nifE.setEnabled(act);
    sde_nuexplE.setEnabled(act);
    sde_nrgsaE.setEnabled(act);
    sde_comenE.setEnabled(act);
    pai_codiE.setEnabled(act);
    sde_orgconE.setEnabled(act);
    jt.setEnabled(act);
    Baceptar.setEnabled(act);
    Bcancelar.setEnabled(act);
  }

  public void PADPrimero()
  {
    verDatos(dtCons);
  }

  public void PADAnterior()
  {
    verDatos(dtCons);
  }

  public void PADSiguiente()
  {
    verDatos(dtCons);
  }

  public void PADUltimo()
  {
    verDatos(dtCons);
  }

  public void PADQuery()
  {
    activar(true);
    jt.setEnabled(false);
    Pprinc.setQuery(true);
    Pprinc.resetTexto();
    sde_codiE.requestFocus();
  }

  public void ej_query1()
  {
    Component c;
    if ((c=Pprinc.getErrorConf())!=null)
    {
      c.requestFocus();
      mensaje("Error en Criterios de busqueda");
      return;
    }
    Vector v = new Vector();
    v.addElement(sde_codiE.getStrQuery());
    v.addElement(sde_nombE.getStrQuery());
    v.addElement(sde_direcE.getStrQuery());
    v.addElement(sde_poblE.getStrQuery());
    v.addElement(sde_proviE.getStrQuery());
    v.addElement(sde_codposE.getStrQuery());
    v.addElement(sde_telefE.getStrQuery());
    v.addElement(sde_faxE.getStrQuery());
    v.addElement(sde_nifE.getStrQuery());
    v.addElement(sde_nuexplE.getStrQuery());
    v.addElement(sde_nrgsaE.getStrQuery());
    v.addElement(sde_comenE.getStrQuery());
    v.addElement(pai_codiE.getStrQuery());
    v.addElement(sde_orgconE.getStrQuery());

    s = "SELECT * FROM v_saladesp ";
    s = creaWhere(s, v,true);
    s+=" ORDER BY sde_codi ";
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
        verDatos(dtCons);
        return;
      }
      mensaje("");
      strSql = s;
      activaTodo();
      rgSelect();
      verDatos(dtCons);
      mensajeErr("Nuevos registros selecionados");
    }
    catch (Exception ex)
    {
      fatalError("Error al buscar Sala Despieces: ", ex);
    }

  }

  public void canc_query()
  {
    Pprinc.setQuery(false);
    mensaje("");
    mensajeErr("Consulta ... CANCELADA");
    activaTodo();
    verDatos(dtCons);
  }

  public void PADEdit()
  {
    if (!bloqueaRegistro())
      return;
    activar(true);
    jt.requestFocusInicio();
    sde_codiE.setEnabled(false);
    sde_nombE.requestFocus();
  }

  public void ej_edit1()
  {
    jt.procesaAllFoco();
    try
    {
      dtAdd.edit(dtAdd.getCondWhere());
      actTabla(dtAdd);
      resetBloqueo(dtAdd, "v_saladesp", sde_codiE.getText(),false);
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
//    verDatos(dtCons);
  }

  public void canc_edit()
  {
    try
    {
      resetBloqueo(dtAdd, "v_saladesp", sde_codiE.getText(),false);
    }
    catch (Exception k)
    {
      Error("Error al Desbloquear el registro", k);
      return;
    }

    mensaje("");
    activaTodo();
    mensajeErr("Modificacion de Datos Cancelada");
    verDatos(dtCons);
  }

  public boolean checkEdit()
  {
    return checkAddNew();
  }


  public void PADAddNew()
  {
    Pprinc.resetTexto();
    jt.removeAllDatos();
    activar(true);
    pai_codiE.setText("");
    jt.requestFocusInicio();
    sde_codiE.requestFocus();
    mensaje("Introduzca datos del Sala Despiece a insertar ...");
  }

  public boolean checkAddNew()
  {
    try
    {
      if (sde_codiE.getValorInt() == 0)
      {
        s = "SELECT max(sde_codi) as sde_codi  from v_saladesp";
        dtStat.select(s);
        sde_codiE.setValorInt(dtStat.getInt("sde_codi", true) + 1);
        mensajeErr("Introduzca Codigo de Sala Desp. (PUESTO EL ULTIMO + 1)");
        sde_codiE.requestFocus();
        return false;
      }

      if (sde_nombE.isNull())
      {
        mensajeErr("Introduzca Nombre de Sala Despiece");
        sde_nombE.requestFocus();
        return false;
      }
      if (!pai_codiE.controla())
      {
        mensajeErr("Pais no es valido");
        return false;
      }
      if (sde_nrgsaE.isNull())
      {
        mensajeErr("Introduzca Num. Registro Sanitario");
        sde_nrgsaE.requestFocus();
        return false;
      }
      s = "SELECT sde_nrgsa,sde_nomb,sde_codi  from v_saladesp " +
          "  where sde_nrgsa = '" + sde_nrgsaE.getText() + "'";
      if (dtStat.select(s))
      {
        if (mensajes.mensajeYesNo("Este N� Registro Sanitario ya lo tiene asignado " +
                                  dtStat.getString("sde_codi") + "(" +
                                  dtStat.getString("sde_nomb") +
                                  ")\n � Continuar ?") != mensajes.YES)
        {
          mensajeErr("Num. Registro Duplicado");
          sde_nrgsaE.requestFocus();
          return false;
        }
      }

      if (!checkPrv())
        return false;
    }
    catch (Exception k)
    {
      Error("Error al Chequear campos", k);
      return false;
    }
    return true;
  }
  public void ej_addnew1()
  {
    try
    {
      jt.procesaAllFoco();
      s="SELECT * FROM v_saladesp WHERE sde_codi = "+sde_codiE.getValorInt();
      if (dtStat.select(s))
      {
        mensajeErr("Codigo de Sala Despiece ya existe");
        return;
      }
      mensaje("Insertando Sala Despiece ...",false);
      dtAdd.addNew("v_saladesp");
      dtAdd.setDato("sde_codi", sde_codiE.getValorInt());
      actTabla(dtAdd);
      ctUp.commit();
    }
    catch (Exception ex)
    {
      Error("Error al Insertar datos",ex);
      return;
    }
    mensaje("");
    mensajeErr("Datos ... Insertados");

    activaTodo();
  }

  void actTabla(DatosTabla dt) throws Exception
  {
    dt.setDato("sde_nomb", sde_nombE.getText());
    dt.setDato("sde_direc", sde_direcE.getText());
    dt.setDato("sde_pobl", sde_poblE.getText());
    dt.setDato("sde_provi", sde_proviE.getText());
    dt.setDato("sde_codpos", sde_codposE.getText());
    dt.setDato("sde_telef", sde_telefE.getText());
    dt.setDato("sde_fax", sde_faxE.getText());
    dt.setDato("sde_nif", sde_nifE.getText());
    dt.setDato("sde_nuexpl", sde_nuexplE.getText());
    dt.setDato("sde_nrgsa", sde_nrgsaE.getText());
    dt.setDato("sde_comen", sde_comenE.getText());
    dt.setDato("pai_codi", pai_codiE.getText());
    dt.setDato("sde_orgcon", sde_orgconE.getText());
    dtAdd.update(stUp);
    s="DELETE FROM  v_prvsade "+
        " WHERE sde_codi = "+sde_codiE.getValorInt();
    stUp.executeUpdate(s);
    int nRow=jt.getRowCount();
    nRow = jt.getRowCount();
    for (int n = 0; n < nRow; n++)
    {
      if (jt.getValorDec(n, 0) == 0)
        continue;
      s = "INSERT INTO v_prvsade VALUES (" + jt.getValInt(n, 0)  + "," +
          sde_codiE.getValorInt()+ ")";
      stUp.executeUpdate(s);
    }

  }
  public void canc_addnew()
  {
    mensaje("");
    activaTodo();
    mensajeErr("Insercion de Datos Cancelada");
    verDatos(dtCons);
  }

  public void PADDelete()
  {
    if (!bloqueaRegistro())
      return;
    Baceptar.setEnabled(true);
    Bcancelar.setEnabled(true);
    Bcancelar.requestFocus();
    mensaje("Borrar Registro ...");
  }

  boolean bloqueaRegistro()
  {
    try
    {
      if (!setBloqueo(dtAdd, "v_saladesp", sde_codiE.getText()))
      {
        msgBox(msgBloqueo);
        nav.pulsado = navegador.NINGUNO;
        activaTodo();
        return false;
      }

      if (!dtAdd.select("select * from v_saladesp where sde_codi = " +
                        sde_codiE.getText(), true))
      {
        mensajeErr("Registro ha sido borrado");
        resetBloqueo(dtAdd, "v_saladesp", sde_codiE.getText());
        activaTodo();
        mensaje("");
        return false;
      }
    }
    catch (Exception k)
    {
      Error("Error al bloquear el registro", k);
      return false;
    }
    return true;
  }
  public void ej_delete1()
  {
    try
    {
      dtAdd.delete(stUp);
      s = "DELETE FROM  v_prvsade " +
          " WHERE sde_codi = " + sde_codiE.getValorInt();
      stUp.executeUpdate(s);

      resetBloqueo(dtAdd, "v_saladesp",sde_codiE.getText());
      ctUp.commit();
      rgSelect();
    }
    catch (Exception ex)
    {
      Error("Error al borrar Registro",ex);
    }
    activaTodo();
    verDatos(dtCons);
    mensaje("");
    mensajeErr("Registro ... Borrado");
  }
  public void canc_delete()
  {
    try
    {
      resetBloqueo(dtAdd, "v_saladesp",sde_codiE.getText());
    }
    catch (Exception k)
    {
      Error("Error al Desbloquear el registro", k);
      return;
    }

    mensaje("");
    activaTodo();
    mensajeErr("Borrado  de Datos Cancelada");
    verDatos(dtCons);
  }

  boolean checkPrv()
  {
    try
    {
      if (prv_codiE.getValorInt() == 0)
        return true;
      s = "SELECT * FROM v_proveedo WHERE prv_codi = " + prv_codiE.getValorInt();
      if (!dtStat.select(s))
      {
        mensajeErr("Proveedor " + prv_codiE.getValorInt() + "... NO ENCONTRADA");
        return false;
      }
    }
    catch (Exception k)
    {
      Error("Error al buscar Proveedor", k);
      return false;
    }
    return true;
  }

}
