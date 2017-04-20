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
import java.net.UnknownHostException;

/**
 *
 * <p>Título: pdmatadero </p>
 * <p>Descripción: Mantenimiento Tabla de Mataderos</p>
 * <p> Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
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
 * <p>Empresa: miSL</p>
 * @version 1.0
 */
public class pdmatadero extends ventanaPad   implements PAD
{
  String s;
  CPanel Pprinc = new CPanel();
  CLabel mat_codiL = new CLabel();
  CTextField mat_codiE = new CTextField(Types.DECIMAL,"####9");
  CTextField mat_nombE = new CTextField(Types.CHAR,"X",50);
//  CButton Baceptar = new CButton();
//  CButton Bcancelar = new CButton();

  boolean modConsulta=true;
  CTextField mat_poblE = new CTextField(Types.CHAR,"X",50);
  CTextField mat_codposE = new CTextField(Types.CHAR,"X",15);
  CLabel cLabel4 = new CLabel();
  CLabel cLabel33 = new CLabel();
  CTextField mat_telefE = new CTextField(Types.CHAR,"X",15);
  CTextField cli_faxE = new CTextField(Types.CHAR,"X",15);
  CLabel cLabel7 = new CLabel();
  CLabel cLabel5 = new CLabel();
  CLabel cLabel3 = new CLabel();
  CTextField mat_direcE = new CTextField(Types.CHAR,"X",50);
  CLabel cLabel6 = new CLabel();
  CTextField mat_proviE = new CTextField(Types.CHAR,"X",50);
  CTextField mat_faxE = new CTextField(Types.CHAR,"X",15);
  CLabel cLabel8 = new CLabel();
  CLabel mat_nifL = new CLabel();
  CTextField mat_nifE = new CTextField(Types.CHAR,"X",15);
  CLabel mat_nifL1 = new CLabel();
  CTextField mat_nuexplE = new CTextField(Types.CHAR,"X",15);
  CLinkBox pai_codiE = new CLinkBox();
  CLabel cLabel15 = new CLabel();
  CLabel mat_nrgsaL = new CLabel();
  CTextField mat_nrgsaE = new CTextField(Types.CHAR,"X",12);
  CLabel mat_orgconL = new CLabel();
  CTextField mat_orgconE = new CTextField(Types.CHAR,"X",15);
  CTextField mat_comenE = new CTextField(Types.CHAR,"X",50);
  CGridEditable jt = new CGridEditable(2)
  {
    public int cambiaLinea(int row, int col)
    {
      return checkPrv() ? -1 : 0;
    }

    public void cambiaColumna(int col, int colNueva,int row)
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
        Error("Error al buscar Nombre Matadero", k);
      }
    }
  };
  prvPanel prv_codiE = new prvPanel();
  CTextField prv_nombE = new CTextField();
  CLabel mat_orgconL1 = new CLabel();

  public pdmatadero(EntornoUsuario eu, Principal p)
  {
    this(eu,p,null);
  }
  public pdmatadero(EntornoUsuario eu, Principal p,Hashtable ht)
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
      setTitulo("Mantenimiento Mataderos");

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

  public pdmatadero(gnu.chu.anjelica.menu p, EntornoUsuario eu,Hashtable ht)
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
      setTitulo("Mant. Mataderos");
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
    this.setVersion("2016-10-03"+(modConsulta?"SOLO LECTURA":""));
    strSql = "SELECT * FROM v_matadero "+
        " ORDER BY mat_codi ";

    statusBar = new StatusBar(this);
    conecta();
    nav = new navegador(this, dtCons, false, modConsulta? navegador.CURYCON:navegador.NORMAL);
    Iniciar(this);


    Pprinc.setDefButton(Baceptar);
    Pprinc.setEscButton(Bcancelar);
    Pprinc.setLayout(null);
    mat_codiL.setText("Matadero");
    mat_codiL.setBounds(new Rectangle(7, 4, 62, 17));// NO utilizado Siempre SI

    Baceptar.setBounds(new Rectangle(380, 257, 120, 28));
    Bcancelar.setBounds(new Rectangle(380, 307, 120, 27));
    mat_nombE.setBounds(new Rectangle(124, 4, 368, 17));
    mat_codiE.setBounds(new Rectangle(71, 4, 46, 17));
    mat_poblE.setBounds(new Rectangle(71, 44, 361, 17));
    mat_codposE.setBounds(new Rectangle(71, 100, 129, 16));
    cLabel4.setBounds(new Rectangle(7, 26, 59, 17));
    cLabel4.setText("Direccion");
    cLabel33.setBounds(new Rectangle(9, 100, 61, 16));
    cLabel33.setText("Cod Postal");
    mat_telefE.setBounds(new Rectangle(71, 81, 129, 17));
    cli_faxE.setBounds(new Rectangle(629, 110, 129, 17));
    cLabel7.setBounds(new Rectangle(597, 110, 29, 18));
    cLabel7.setText("Fax");
    cLabel5.setBounds(new Rectangle(7, 81, 56, 17));
    cLabel5.setText("Telefono");
    cLabel3.setBounds(new Rectangle(7, 44, 59, 17));
    cLabel3.setText("Poblacion");
    mat_direcE.setBounds(new Rectangle(71, 26, 361, 17));
    cLabel6.setText("Provincia");
    cLabel6.setBounds(new Rectangle(7, 63, 59, 17));
    mat_proviE.setBounds(new Rectangle(71, 63, 361, 17));
    mat_faxE.setBounds(new Rectangle(358, 81, 129, 17));
    cLabel8.setText("Fax");
    cLabel8.setBounds(new Rectangle(333, 81, 34, 17));
    mat_nifL.setText("NIF");
    mat_nifL.setBounds(new Rectangle(320, 157, 28, 18));
    mat_nifE.setBounds(new Rectangle(358, 157, 129, 18));
    mat_nifL1.setBounds(new Rectangle(2, 136, 67, 17));
    mat_nifL1.setText("Num. Expl.");
    mat_nuexplE.setToolTipText("Numero de Explotaci�n");
    mat_nuexplE.setBounds(new Rectangle(78, 136, 129, 17));
    pai_codiE.setRequestFocusEnabled(true);
    pai_codiE.setAceptaNulo(true);
    pai_codiE.setAncTexto(45);
    pai_codiE.setBounds(new Rectangle(236, 100, 253, 18));
    cLabel15.setText("Pais");
    cLabel15.setBounds(new Rectangle(206, 100, 29, 18));
    mat_nrgsaL.setText("NRGSA");
    mat_nrgsaL.setBounds(new Rectangle(322, 136, 48, 17));
    mat_nrgsaE.setTipoCampo(1);
    mat_nrgsaE.setText("");
    mat_nrgsaE.setBounds(new Rectangle(382, 136, 105, 17));

    mat_orgconL.setText("Org. Control");
    mat_orgconL.setBounds(new Rectangle(2, 157, 78, 17));
    mat_orgconE.setBounds(new Rectangle(78, 157, 129, 17));
    mat_comenE.setBounds(new Rectangle(78, 177, 361, 17));
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
    mat_orgconL1.setBounds(new Rectangle(0, 178, 78, 17));
    mat_orgconL1.setText("Comentario");
    Pprinc.add(mat_codiL, null);
    Pprinc.add(mat_codiE, null);
    Pprinc.add(mat_nombE, null);
    Pprinc.add(cli_faxE, null);
    Pprinc.add(cLabel7, null);
    Pprinc.add(mat_direcE, null);
    Pprinc.add(cLabel4, null);
    Pprinc.add(mat_poblE, null);
    Pprinc.add(cLabel3, null);
    Pprinc.add(cLabel6, null);
    this.getContentPane().add(nav, BorderLayout.NORTH);
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);
    this.getContentPane().add(Pprinc,  BorderLayout.CENTER);
    Pprinc.add(mat_proviE, null);
    Pprinc.add(cLabel5, null);
    Pprinc.add(mat_telefE, null);
    Pprinc.add(cLabel8, null);
    Pprinc.add(mat_faxE, null);
    Pprinc.add(mat_nrgsaE, null);
    Pprinc.add(mat_nrgsaL, null);
    Pprinc.add(mat_orgconE, null);
    Pprinc.add(mat_nuexplE, null);
    Pprinc.add(mat_orgconL, null);
    Pprinc.add(mat_nifL1, null);
    Pprinc.add(mat_comenE, null);
    Pprinc.add(jt, null);
    Pprinc.add(Baceptar, null);
    Pprinc.add(Bcancelar, null);
    Pprinc.add(mat_nifE, null);
    Pprinc.add(mat_nifL, null);
    Pprinc.add(pai_codiE, null);
    Pprinc.add(cLabel33, null);
    Pprinc.add(mat_codposE, null);
    Pprinc.add(cLabel15, null);
    Pprinc.add(mat_orgconL1, null);
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

    mat_codiE.setColumnaAlias("mat_codi");
    mat_nombE.setColumnaAlias("mat_nomb");
    mat_direcE.setColumnaAlias("mat_direc");
    mat_poblE.setColumnaAlias("mat_pobl");
    mat_proviE.setColumnaAlias("mat_provi");
    mat_codposE.setColumnaAlias("mat_codpos");
    mat_telefE.setColumnaAlias("mat_telef");
    mat_faxE.setColumnaAlias("mat_fax");
    mat_nifE.setColumnaAlias("mat_nif");
    mat_nuexplE.setColumnaAlias("mat_nuexpl");
    mat_nrgsaE.setColumnaAlias("mat_nrgsa");
    mat_comenE.setColumnaAlias("mat_comen");
    pai_codiE.setColumnaAlias("pai_codi");
    mat_orgconE.setColumnaAlias("mat_orgcon");

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

      mat_codiE.setText(dt.getString("mat_codi"));

      s="SELECT * FROM v_matadero WHERE  mat_codi = "+dt.getInt("mat_codi");
      if (! dtCon1.select(s))
      {
        mensajeErr("Codigo NO ENCONTRADO ... SEGURAMENTE SE BORRO");
        Pprinc.resetTexto();
        mat_codiE.setText(dt.getString("mat_codi"));
        return;
      }
      mat_nombE.setText(dtCon1.getString("mat_nomb"));
      mat_direcE.setText(dtCon1.getString("mat_direc"));
      mat_poblE.setText(dtCon1.getString("mat_pobl"));
      mat_proviE.setText(dtCon1.getString("mat_provi"));
      mat_codposE.setText(dtCon1.getString("mat_codpos"));
      mat_telefE.setText(dtCon1.getString("mat_telef"));
      mat_faxE.setText(dtCon1.getString("mat_fax"));
      mat_nifE.setText(dtCon1.getString("mat_nif"));
      mat_nuexplE.setText(dtCon1.getString("mat_nuexpl"));
      mat_nrgsaE.setText(dtCon1.getString("mat_nrgsa"));
      mat_comenE.setText(dtCon1.getString("mat_comen"));
      pai_codiE.setText(dtCon1.getString("pai_codi"));
      mat_orgconE.setText(dtCon1.getString("mat_orgcon"));
      s="SELECT p.prv_codi,p.prv_nomb FROM V_PRVMATA m,v_proveedo p"+
          " WHERE mat_codi = "+dt.getString("mat_codi")+
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
    mat_codiE.setEnabled(act);
    mat_nombE.setEnabled(act);
    mat_direcE.setEnabled(act);
    mat_poblE.setEnabled(act);
    mat_proviE.setEnabled(act);
    mat_codposE.setEnabled(act);
    mat_telefE.setEnabled(act);
    mat_faxE.setEnabled(act);
    mat_nifE.setEnabled(act);
    mat_nuexplE.setEnabled(act);
    mat_nrgsaE.setEnabled(act);
    mat_comenE.setEnabled(act);
    pai_codiE.setEnabled(act);
    mat_orgconE.setEnabled(act);
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
    mat_codiE.requestFocus();
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
    v.addElement(mat_codiE.getStrQuery());
    v.addElement(mat_nombE.getStrQuery());
    v.addElement(mat_direcE.getStrQuery());
    v.addElement(mat_poblE.getStrQuery());
    v.addElement(mat_proviE.getStrQuery());
    v.addElement(mat_codposE.getStrQuery());
    v.addElement(mat_telefE.getStrQuery());
    v.addElement(mat_faxE.getStrQuery());
    v.addElement(mat_nifE.getStrQuery());
    v.addElement(mat_nuexplE.getStrQuery());
    v.addElement(mat_nrgsaE.getStrQuery());
    v.addElement(mat_comenE.getStrQuery());
    v.addElement(pai_codiE.getStrQuery());
    v.addElement(mat_orgconE.getStrQuery());

    s = "SELECT * FROM v_matadero ";
    s = creaWhere(s, v,true);
    s+=" ORDER BY mat_codi ";
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
      fatalError("Error al buscar Mataderos: ", ex);
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
    mat_codiE.setEnabled(false);
    mat_nombE.requestFocus();
  }

  public void ej_edit1()
  {
    jt.procesaAllFoco();
    try
    {
      dtAdd.edit(dtAdd.getCondWhere());
      actTabla(dtAdd);
      resetBloqueo(dtAdd, "v_matadero", mat_codiE.getText(),false);
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
      resetBloqueo(dtAdd, "v_matadero", mat_codiE.getText(),false);
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
    jt.requestFocusInicio();
    pai_codiE.setText("");
    mat_codiE.requestFocus();
    mensaje("Introduzca datos del Matadero a insertar ...");
  }

  public boolean checkAddNew()
  {

    try
    {

      if (mat_codiE.getValorInt()==0 && nav.pulsado==navegador.ADDNEW)
      {
          int antNumero = 0;
          String s = "SELECT mat_codi FROM v_matadero order by mat_codi";
          if (dtStat.select(s))
          {
              do
              {
                  if (antNumero > 0 && dtStat.getInt("mat_codi") > antNumero + 1)
                      break;
                  antNumero = dtStat.getInt("mat_codi");
              } while (dtStat.next());
          }       
            mat_codiE.setValorInt(antNumero+ 1);
            mensajeErr("Introduzca Codigo de Matadero (PUESTO EL PRIMERO LIBRE)");
            mat_codiE.requestFocus();
            return false;
      }
      if (mat_nombE.isNull())
      {
        mensajeErr("Introduzca Nombre de Matadero");
        mat_nombE.requestFocus();
        return false;
      }
      if (!pai_codiE.controla())
      {
        mensajeErr("Pais no es valido");
        return false;
      }
      if (mat_nrgsaE.isNull())
      {
        mensajeErr("Introduzca Num. Registro Sanitario");
        mat_nrgsaE.requestFocus();
        return false;
      }
      s ="SELECT mat_nrgsa,mat_nomb,mat_codi  from v_matadero "+
          "  where mat_nrgsa = '" +  mat_nrgsaE.getText() + "'";
      if (dtStat.select(s))
      {
        if (mensajes.mensajeYesNo("N� Registro Sanitario LO tiene asignado " +
                                  dtStat.getString("mat_codi") + "(" +
                                  dtStat.getString("mat_nomb") +
                                  ")\n � Continuar ?") != mensajes.YES)
        {
          mensajeErr("Num. Registro Duplicado");
          mat_nrgsaE.requestFocus();
          return false;
        }
      }

      if (!checkPrv())
        return false;
      return true;
    }
    catch (Exception k)
    {
      Error("Error al Chequear campos", k);
      return false;
    }

  }
  public void ej_addnew1()
  {
    try
    {
      jt.procesaAllFoco();
      s="SELECT * FROM v_matadero WHERE mat_codi = "+mat_codiE.getValorInt();
      if (dtStat.select(s))
      {
        mensajeErr("Codigo de Matadero ya existe");
        return;
      }
      mensaje("Insertando Matadero ...",false);
      dtAdd.addNew("v_matadero");
      dtAdd.setDato("mat_codi", mat_codiE.getValorInt());
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
    dt.setDato("mat_nomb", mat_nombE.getText());
    dt.setDato("mat_direc", mat_direcE.getText());
    dt.setDato("mat_pobl", mat_poblE.getText());
    dt.setDato("mat_provi", mat_proviE.getText());
    dt.setDato("mat_codpos", mat_codposE.getText());
    dt.setDato("mat_telef", mat_telefE.getText());
    dt.setDato("mat_fax", mat_faxE.getText());
    dt.setDato("mat_nif", mat_nifE.getText());
    dt.setDato("mat_nuexpl", mat_nuexplE.getText());
    dt.setDato("mat_nrgsa", mat_nrgsaE.getText());
    dt.setDato("mat_comen", mat_comenE.getText());
    dt.setDato("pai_codi", pai_codiE.getText());
    dt.setDato("mat_orgcon", mat_orgconE.getText());
    dtAdd.update(stUp);
    s="DELETE FROM  V_PRVMATA "+
        " WHERE mat_codi = "+mat_codiE.getValorInt();
    stUp.executeUpdate(s);
    int nRow=jt.getRowCount();
    nRow = jt.getRowCount();
    for (int n = 0; n < nRow; n++)
    {
      if (jt.getValorDec(n, 0) == 0)
        continue;
      s = "INSERT INTO v_prvmata VALUES (" + jt.getValInt(n, 0)  + "," +
          mat_codiE.getValorInt()+ ")";
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
      if (!setBloqueo(dtAdd, "v_matadero", mat_codiE.getText()))
      {
        msgBox(msgBloqueo);
        nav.pulsado = navegador.NINGUNO;
        activaTodo();
        return false;
      }

      if (!dtAdd.select("select * from v_matadero where mat_codi = " +
                        mat_codiE.getText(), true))
      {
        mensajeErr("Registro ha sido borrado");
        resetBloqueo(dtAdd, "v_matadero", mat_codiE.getText());
        activaTodo();
        mensaje("");
        return false;
      }
    }
    catch (SQLException | UnknownHostException k)
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
      s = "DELETE FROM  V_PRVMATA " +
          " WHERE mat_codi = " + mat_codiE.getValorInt();
      stUp.executeUpdate(s);

      resetBloqueo(dtAdd, "v_matadero",mat_codiE.getText());
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
  @Override
  public void canc_delete()
  {
    try
    {
      resetBloqueo(dtAdd, "v_matadero",mat_codiE.getText());
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
  /**
   * Devuelve el pais del matadero
   * @param dt
   * @param matCodi
   * @return 0 si no encuentra el matadero o el pais esta a null
   * @throws SQLException 
   */
  public static int getPaisMatadero(DatosTabla dt,int matCodi) throws SQLException
  {
       String s="select pai_codi,mat_nrgsa from v_matadero where mat_codi ="+matCodi;
       if (!dt.select(s))
           return 0;
       return dt.getInt("pai_codi",true);
  }
  public static boolean getDatosMatadero(DatosTabla dt,int matCodi) throws SQLException
  {
       String s="select * from v_matadero where mat_codi ="+matCodi;
       return dt.select(s);
  }
  public static String getRegistroSanitario(DatosTabla dt,int matCodi,boolean swPaisCorto) throws SQLException
  {
      if (!getDatosMatadero(dt,matCodi))
          return "**Matadero "+matCodi+" NO Encontrado**";
      String numRegSanitario=dt.getString("mat_nrgsa");
      String s = "select pai_nomb,pai_nomcor from v_paises where pai_codi = " +dt.getInt("pai_codi");
      if (dt.select(s))
          numRegSanitario = (swPaisCorto?  dt.getString("pai_nomcor") :dt.getString("pai_nomb") )
              + "-" + numRegSanitario;
       return numRegSanitario;
  }
}
