package gnu.chu.anjelica.pad;

import java.sql.*;
import gnu.chu.Menu.*;
import java.awt.*;
import java.util.*;
import gnu.chu.interfaces.*;
import gnu.chu.controles.*;
import gnu.chu.camposdb.*;
import javax.swing.BorderFactory;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.*;
import java.awt.event.*;
/**
 *
 * <p>Título: pdgruart </p>
 * <p>Descripción: Mantenimiento Grupos de  Articulos</p>
 * <p>Empresa: miSL</p>
 * <p>Copyright: Copyright (c) 2005-2014
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los terminos de la Licencia Pública General de GNU según es publicada por
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
 * @author ChuchiP
 * @version 1.0
 */
public class pdgruart  extends ventanaPad  implements PAD
{
  int focus_grid=-1;
  boolean swVerLin=false;
  String s;
  CTextField tla_ordenE = new CTextField(Types.DECIMAL, "#9");
  CTextField pro_descE = new CTextField(Types.CHAR, "X", 25);
  proPanel pro_codiE = new proPanel();
  CTextField pro_nombE = new CTextField();
  CPanel Pprinc = new CPanel();
  CPanel Pcabe = new CPanel();
  CTextField tla_nuliprE = new CTextField(Types.DECIMAL, "#9");
  CLabel cLabel6 = new CLabel();
  CTextField tla_diagfeE = new CTextField(Types.DECIMAL, "#9");
  CLabel cLabel1 = new CLabel();
  CLabel cLabel7 = new CLabel();
  CComboBox tla_vekgcaE = new CComboBox();
  CTextField tla_codiE = new CTextField(Types.DECIMAL, "#9");
  CLabel cLabel5 = new CLabel();
  CTextField tla_nombE = new CTextField(Types.CHAR, "X", 60);
  CGridEditable jtCab = new CGridEditable(2)
  {
    public void afterCambiaLineaDis(int nRow)
    {
      if (nav.pulsado==navegador.EDIT || nav.pulsado==navegador.ADDNEW)
        return;
      try {
        verDesgDescr(tla_codiE.getValorInt(), jtCab.getValorInt(nRow, 1));
      } catch (SQLException k)
      {
        Error("Error al ver desglose de descripcion",k);
      }
    }

    public void afterCambiaLinea()
    {
      try
      {
        verDesgDescr(tla_codiE.getValorInt(), jtCab.getValorInt(1));
      }
      catch (SQLException k)
      {
        Error("Error al ver desglose de descripcion", k);
      }
    }

    public int cambiaLinea(int row, int col)
    {
      try {
        return cambiaLineaJtCab(row);
      } catch (Exception k)
      {
        Error("Error al cambiar de linea (jtCab) ",k);
      }
      return -1;
    }

    public boolean deleteLinea(int row, int col)
    {
      try {
        borraLin();
        ctUp.commit();
      } catch (Exception k)
      {
        Error("Error al borrar linea",k);
      }
      return true;
    }

  };
  CGridEditable jtLin = new CGridEditable(2)
  {
    public void cambiaColumna(int col, int colNueva, int row)
    {
      try
      {
//        if (col == 0)
//          jtLin.setValor(pro_codiE.getNombArtCli(pro_codiE.getValorInt(), row, 1));
      }
      catch (Exception k)
      {
        Error("Error al buscar Nombre Articulo", k);
      }
    }

    public int cambiaLinea(int row, int col)
    {
      return cambiaLineaJtLin(row);
    }

  };
//  CButton Baceptar = new CButton("Aceptar");
//  CButton Bcancelar = new CButton("Cancelar");
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CButton Birgrid = new CButton();

  public pdgruart(EntornoUsuario eu, Principal p)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    try
    {
      setTitulo("Mantenimiento Agrupaciones de  Articulos");

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

  public pdgruart(gnu.chu.anjelica.menu p, EntornoUsuario eu)
  {
    EU = eu;
    vl = p.getLayeredPane();
    eje = false;

    try
    {
      setTitulo("Mantenimiento Agrupaciones de  Articulos");
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
    this.setSize(new Dimension(553, 440));
    this.setVersion("2006-01-21");
    strSql = "SELECT * FROM tilialca "+getOrderQuery();

    statusBar = new StatusBar(this);
    conecta();
    nav = new navegador(this, dtCons, false, navegador.NORMAL);
    iniciar(this);
    confGrids();
    Pprinc.setLayout(gridBagLayout1);
    Pcabe.setBorder(BorderFactory.createRaisedBevelBorder());
    Pcabe.setMaximumSize(new Dimension(537, 44));
    Pcabe.setMinimumSize(new Dimension(537, 44));
    Pcabe.setPreferredSize(new Dimension(537, 44));
    Pcabe.setLayout(null);
    tla_nombE.setMayusc(true);
    cLabel6.setRequestFocusEnabled(true);
    cLabel6.setToolTipText("N�mero Lineas a Imprimir por grupo");
    cLabel6.setText("No. Lineas Por Grupo");
    cLabel6.setBounds(new Rectangle(383, 20, 119, 17));
    tla_diagfeE.setToolTipText("Agrupar Fechas Caducidad en grupos de N dias");
    tla_diagfeE.setText(" 0");
    tla_diagfeE.setBounds(new Rectangle(315, 20, 26, 17));
    cLabel1.setText("Tipo Listado");
    cLabel1.setBounds(new Rectangle(4, 2, 70, 17));
    cLabel7.setToolTipText("Agrupar Fechas Caducidad en grupos de N dias");
    cLabel7.setText("Agrupar Fecha Caducidad");
    cLabel7.setBounds(new Rectangle(164, 20, 151, 17));
    cLabel5.setText("Ver");
    cLabel5.setBounds(new Rectangle(5, 20, 31, 17));
    tla_nuliprE.setToolTipText("Número Lineas a Imprimir por grupo");
    tla_nuliprE.setBounds(new Rectangle(503, 20, 27, 17));
    Baceptar.setMaximumSize(new Dimension(112, 28));
    Baceptar.setMinimumSize(new Dimension(112, 28));
    Baceptar.setPreferredSize(new Dimension(112, 28));
    Baceptar.setMargin(new Insets(0, 0, 0, 0));
    Bcancelar.setMaximumSize(new Dimension(112, 28));
    Bcancelar.setMinimumSize(new Dimension(112, 28));
    Bcancelar.setPreferredSize(new Dimension(112, 28));
    Bcancelar.setMargin(new Insets(0, 0, 0, 0));
    tla_vekgcaE.setBounds(new Rectangle(30, 20, 96, 17));
    tla_nombE.setBounds(new Rectangle(104, 2, 428, 17));
    tla_codiE.setBounds(new Rectangle(74, 2, 22, 17));
    Birgrid.setBounds(new Rectangle(529, 26, 2,2));
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
    Pprinc.add(Pcabe,   new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 1, 0, 3), 0, 0));
    Pcabe.add(cLabel1, null);
    Pcabe.add(tla_nombE, null);
    Pcabe.add(tla_codiE, null);
    Pcabe.add(tla_nuliprE, null);
    Pcabe.add(cLabel5, null);
    Pcabe.add(tla_vekgcaE, null);
    Pcabe.add(cLabel6, null);
    Pcabe.add(tla_diagfeE, null);
    Pcabe.add(cLabel7, null);
    Pcabe.add(Birgrid, null);
    Pprinc.add(jtCab,   new GridBagConstraints(0, 1, 2, 1, 2.0, 2.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 1, 0, 3), 0, 0));
    Pprinc.add(Bcancelar,      new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 0, 50), 0, 0));
    Pprinc.add(Baceptar,      new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 50, 0, 0), 0, 0));
    Pprinc.add(jtLin, new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0
            ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(6, 4, 3, 0), 0, 0));
  }

  public void iniciarVentana() throws Exception
  {
    tla_codiE.setColumnaAlias("tla_codi");
    tla_nombE.setColumnaAlias("tla_nomb");
    tla_nuliprE.setColumnaAlias("tla_nulipr");
    tla_diagfeE.setColumnaAlias("tla_diagfe");
    tla_vekgcaE.setColumnaAlias("tla_vekgca");


    Pcabe.setButton(KeyEvent.VK_F2,Birgrid);
    jtCab.setButton(KeyEvent.VK_F2,Birgrid);
    jtLin.setButton(KeyEvent.VK_F2,Birgrid);
    Pcabe.setDefButton(Baceptar);
    jtCab.setDefButton(Baceptar);
    jtLin.setDefButton(Baceptar);

    activar(false);
    verDatos();
    activarEventos();
  }

  void activarEventos()
  {
    Birgrid.addFocusListener(new FocusAdapter()
    {
      public void focusGained(FocusEvent e)
      {
        Birgrid_focusGained();
      }
    });
    jtCab.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent e)
      {
        try {
          Birgrid_focusGained();
        } catch (Exception k)
        {
          Error("Error al guardar lineas de Grid",k);
        }
      }
    });
    jtLin.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent e)
      {
        Birgrid_focusGained();
      }
    });
  }
  void Birgrid_focusGained()
  {
    if (nav.pulsado != navegador.EDIT && nav.pulsado != navegador.ADDNEW)
          return;
    try {
      if (focus_grid == 0)
      {
        irGridInicio();
        return;
      }
      if (focus_grid == 2)
      {
        irGridCab();
        return;
      }
      if (focus_grid == 1)
      {
        irGridLin();
        return;
      }
    } catch (Exception k)
    {
      Error("Error al Ir a Grid",k);
    }
  }

  void irGridLin()
  {
    jtCab.procesaAllFoco();
    if (jtCab.getValString(0).trim().equals(""))
    {
      mensaje("Introduzca una descripcion para poder introducir sus productos");
      return;
    }
    jtCab.setEnabled(false);
    jtLin.setEnabled(true);
    jtLin.requestFocusInicio();
    focus_grid = 2;
  }

  void irGridCab() throws Exception
  {

    int nCol = cambiaLineaJtLin(jtLin.getSelectedRow());
    if (nCol >= 0)
    {
      jtLin.requestFocusLater(jtLin.getSelectedRow(), nCol);
      return;
    }
    jtLin.setEnabled(false);
    guardaGridLin();
    jtCab.setEnabled(true);
    jtCab.requestFocusLater();
    focus_grid = 1;

  }

  void guardaGridLin() throws Exception
  {
    int col;
    if ( (col = cambiaLineaJtLin(jtLin.getSelectedRow())) >= 0)
    {
      jtLin.requestFocusLater(jtLin.getSelectedRow(), col);
      return;
    }
    s="delete from tilialpr WHERE tla_codi = "+tla_codiE.getValorInt()+
        " and tla_orden = "+jtCab.getValorInt(1);
    dtAdd.executeUpdate(s);
    col=jtLin.getRowCount();
    for (int n=0;n<col;n++)
    {
      if (jtLin.getValorDec(n,0)==0)
        continue;
      if (Pcabe.isEnabled())
      {
        if (!checkCabe())
        {
          jtLin.requestFocusLater();
          return;
        }
        guardaCabe();
        Pcabe.setEnabled(false);
      }
      dtAdd.addNew("tilialpr");
      dtAdd.setDato("tla_codi",tla_codiE.getValorInt());
      dtAdd.setDato("tla_orden",guardaDescGrupo());
      dtAdd.setDato("pro_codi",jtLin.getValorInt(n,0));
      dtAdd.update();
    }

    ctUp.commit();
  }

  int guardaDescGrupo() throws SQLException
  {
    int tlaOrden = jtCab.getValorInt(1);
    if (tlaOrden == 0)
    {
      s = "select max(tla_orden) as tla_orden from tilialgr " +
          " where tla_codi = " + tla_codiE.getValorInt();
      dtBloq.select(s, true);
      tlaOrden = dtBloq.getInt("tla_orden",true) + 1;
      jtCab.setValor("" + tlaOrden, 1);
      dtBloq.addNew("tilialgr");
      dtBloq.setDato("tla_codi", tla_codiE.getValorInt());
    }
    else
    {
      s = "select * from tilialgr " +
          " where tla_codi = " + tla_codiE.getValorInt() +
          " and tla_orden = " + tlaOrden;
      if (!dtBloq.select(s, true))
        throw new SQLException("No encontrada Descripcion Grupo " + s);
      dtBloq.edit();
    }
    dtBloq.setDato("tla_orden", tlaOrden);
    dtBloq.setDato("pro_desc", jtCab.getValString(0));
    dtBloq.update();

    return tlaOrden;
  }

  private void guardaCabe() throws Exception
  {
    if (!selectRegPant(dtAdd,true))
    {
      dtAdd.addNew();
      tla_codiE.setValorInt(getMaxCod());
      setBloqueo(dtBloq, "tilialca", tla_codiE.getText());
      dtAdd.setDato("tla_codi",tla_codiE.getValorInt());
    }
    else
      dtAdd.edit();
    dtAdd.setDato("tla_nomb",tla_nombE.getText());
    dtAdd.setDato("tla_nulipr",tla_nuliprE.getValorInt());
    dtAdd.setDato("tla_diagfe",tla_diagfeE.getValorInt());
    dtAdd.setDato("tla_vekgca",tla_vekgcaE.getValor());
    dtAdd.update();
  }
  int getMaxCod() throws SQLException
  {
    s="SELECT MAX(tla_codi) as tla_codi from tilialca ";
    dtStat.select(s);
    return dtStat.getInt("tla_codi",true)+1;
  }
  void irGridInicio()
  {
    if (!checkCabe())
      return;
    focus_grid=1;
    jtCab.requestFocusInicio();
  }
  boolean checkCabe()
  {
/*    if (tla_codiE.getValorInt()==0 || tla_codiE.getValorInt()==99 )
    {
      tla_codiE.requestFocus();
      mensajeErr("Codigo de Grupo NO VALIDO");
      return false;
    }
 */
    if (tla_nombE.isNull())
    {
      mensajeErr("Nombre de Grupo no puede estar en blanco");
      tla_nombE.requestFocus();
      return false;
    }
    return true;
  }
  public void afterConecta() throws SQLException, java.text.ParseException
  {
    tla_vekgcaE.addItem("Unid", "U");
    tla_vekgcaE.addItem("Kilos", "K");
  }

  private void confGrids() throws Exception
  {
    Vector v = new Vector();
    v.addElement("Descr. Grupo");
    v.addElement("Orden");
    jtCab.setCabecera(v);
    jtCab.setMaximumSize(new Dimension(535, 146));
    jtCab.setMinimumSize(new Dimension(535, 146));
    jtCab.setPreferredSize(new Dimension(535, 146));
    jtCab.setAlinearColumna(new int[] {0, 2});
    jtCab.setAnchoColumna(new int[] {150, 20});
    tla_ordenE.setEnabled(false);
    Vector vc = new Vector();
    jtLin.setDebugGraphicsOptions(0);
    jtLin.setMaximumSize(new Dimension(537, 104));
    jtLin.setMinimumSize(new Dimension(537, 104));
    jtLin.setPreferredSize(new Dimension(537, 104));
    jtLin.setPonValoresInFocus(true);
    vc.add(pro_descE);
    vc.add(tla_ordenE);
    jtCab.setCampos(vc);
    jtCab.setAjustarGrid(true);
    pro_codiE.iniciar(dtStat, this, vl, EU);
    pro_codiE.setAceptaNulo(true);
    pro_codiE.setCeroIsNull(true);
    pro_codiE.setProNomb(null);
    pro_nombE.setEnabled(false);
    Vector v1 = new Vector();
    v1.addElement("Producto");
    v1.addElement("Nombre de Producto");
    jtLin.setCabecera(v1);
    jtLin.setAnchoColumna(new int[] {50, 200});
    jtLin.setAlinearColumna(new int[]  {2, 0});
    Vector vc1 = new Vector();
    vc1.addElement(pro_codiE.pro_codiE);
    vc1.addElement(pro_nombE);
    jtLin.setCampos(vc1);
    jtLin.setAjustarGrid(true);
  }


  void verDatos()
  {
    try
    {
      if (dtCons.getNOREG())
        return;
      tla_codiE.setValorInt(dtCons.getInt("tla_codi"));

      jtCab.setEnabled(false);
      jtLin.setEnabled(false);
      if (!selectRegPant(dtCon1, false))
      {
        mensajeErr("Datos del Grupo  .. NO ENCONTRADO");
        Pcabe.resetTexto();
        tla_codiE.setValorInt(dtCons.getInt("tla_codi"));
        jtCab.removeAllDatos();
        jtLin.removeAllDatos();
        return;
      }
      tla_nombE.setText(dtCon1.getString("tla_nomb"));
      tla_nuliprE.setValorInt(dtCon1.getInt("tla_nulipr"));
      tla_diagfeE.setValorInt(dtCon1.getInt("tla_diagfe"));
      tla_vekgcaE.setValor(dtCon1.getString("tla_vekgca"));
      jtCab.removeAllDatos();
      jtLin.removeAllDatos();
      if (! selectDescr(dtCon1,tla_codiE.getValorInt()))
      {
        mensajeErr("NO encontrado Descripciones para este Grupo");
        return;
      }
      swVerLin=false;
      do
      {
        Vector v= new Vector();
        v.add(dtCon1.getString("pro_desc"));
        v.add(dtCon1.getString("tla_orden"));
        jtCab.addLinea(v);
      } while (dtCon1.next());
      jtCab.requestFocusInicio();
      swVerLin = true;
      verDesgDescr(tla_codiE.getValorInt(), jtCab.getValorInt(0, 1));
    }
    catch (Exception k)
    {
      Error("Error al Mostrar Datos", k);
    }
  }
  void verDesgDescr(int tlaCodi,int tlaOrden) throws SQLException
  {
    if (! swVerLin)
      return;
    boolean isEnabled=jtLin.isEnabled();
    jtLin.setEnabled(false);
    jtLin.removeAllDatos();
    if (!selectDesgGrup(dtCon1,tlaCodi,tlaOrden))
    {
      mensajeErr("No encontrados productos para esta descripcion",false);
      return;
    }
    do
    {
      Vector v= new Vector();
      v.addElement(dtCon1.getString("pro_codi"));
      v.addElement(pro_codiE.getNombArt(dtCon1.getString("pro_codi"),EU.em_cod));
      jtLin.addLinea(v);
    } while (dtCon1.next());
    jtLin.setEnabled(isEnabled);
    jtLin.requestFocusInicio();
  }

  private boolean selectDesgGrup(DatosTabla dt,int tlaCodi,int tlaOrden) throws SQLException
  {
    s = "select * from tilialpr WHERE tla_codi = " + tlaCodi+
        " and tla_orden = "+tlaOrden+
        " order by pro_codi";
    return dt.select(s);
  }

  private boolean selectDescr(DatosTabla dt,int tlaCodi) throws SQLException
  {
    s = "select * from tilialgr WHERE tla_codi = " + tlaCodi+
        " order by tla_orden";
    return dt.select(s);
  }
  private boolean selectRegPant(DatosTabla dt, boolean block) throws SQLException
  {
    s = "select * from tilialca WHERE tla_codi = " + tla_codiE.getValorInt();
    return dt.select(s,block);
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
    Pcabe.setQuery(true);
    Pprinc.resetTexto();
    mensaje("Establezca FILTRO de Consulta");
    activar(true);
    jtCab.setEnabled(false);
    jtLin.setEnabled(false);
    tla_codiE.requestFocus();
  }

  public void ej_query1()
  {
    Component c = Pcabe.getErrorConf();
    if (c != null)
    {
      mensajeErr("FILTRO DE CONSULTA NO VALIDO");
      c.requestFocus();
      return;
    }
       try
       {

      Vector v = new Vector();
      v.add(tla_codiE.getStrQuery());

      v.add(tla_nombE.getStrQuery());
      v.add(tla_diagfeE.getStrQuery());
      v.add(tla_vekgcaE.getStrQuery());
      v.add(tla_nuliprE.getStrQuery());
      s=Pcabe.getStrQuery();
      s = "SELECT * FROM tilialca ";
      s = creaWhere(s, v, true);
      s += getOrderQuery();
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
    Pcabe.setQuery(false);
    activaTodo();
    verDatos();
    mensaje("");
    mensajeErr("Introducion FILTRO CONSULTA ... CANCELADO");
  }

  public void PADEdit()
  {
    try
    {
      if (!setBloqueo(dtAdd, "tilialca", tla_codiE.getText()))
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
    mensaje("Modificando registro Actual ... ");
    jtLin.setEnabled(false);
    tla_codiE.setEnabled(false);
    Bcancelar.setEnabled(false);
    focus_grid=0;
    nav.pulsado=navegador.EDIT;
    tla_nombE.requestFocus();
  }

  public void ej_edit1()
  {
    try
    {
      guardaCabe();
      resetBloqueo(dtAdd, "tilialca", tla_codiE.getText(), false);
      ctUp.commit();
      mensajeErr("REGISTRO ... MODIFICADO");
      mensaje("");
      activaTodo();
      nav.pulsado = navegador.NINGUNO;
    }
    catch (Exception k)
    {
      Error("Error al EDITAR registro", k);
    }
  }
  public void canc_edit()
  {
  }

  public void PADAddNew()
  {
    try {
      s = "SELECT max(tla_codi) as tla_codi FROM tilialca ";
      dtStat.select(s);
      if (dtStat.getInt("tla_codi",true ) == 98)
      {
        mensaje("NO se pueden insertar m�s grupos ... ha alcanzado el registro 98");
        activaTodo();
        nav.pulsado=navegador.ADDNEW;
        return;
      }
    } catch (SQLException k)
    {
      Error("Error al Insertar registro",k);
    }
    swVerLin=false;
    jtCab.removeAllDatos();
    jtLin.removeAllDatos();
    activar(true);
    mensaje("Insertando NUEVO registro");
    Pprinc.resetTexto();
    tla_codiE.setEnabled(false);
    jtLin.setEnabled(false);
    focus_grid=0;
    swVerLin=true;
    tla_nombE.requestFocus();
  }
  public boolean checkEdit()
  {
    return checkAddNew();
  }
  public boolean checkAddNew()
  {
    try
    {
      int nCol;
      if (!checkCabe())
        return false;
      if (focus_grid == 2)
      {
        nCol = cambiaLineaJtLin(jtLin.getSelectedRow());
        if (nCol >= 0)
        {
          jtLin.requestFocusLater(jtLin.getSelectedRow(), nCol);
          return false;
        }
        guardaGridLin();

      }
      if (focus_grid == 1)
      {
        nCol = cambiaLineaJtCab(jtCab.getSelectedRow());
        if (nCol >= 0)
        {
          jtCab.requestFocusLater(jtCab.getSelectedRow(), nCol);
          return false;
        }
      }
      return true;
    }
    catch (Exception k)
    {
      Error("ERROR EN checkAddNew", k);
    }
    return false;
  }

  public void ej_addnew1()
  {
    try
    {
      guardaCabe();
      resetBloqueo(dtAdd, "tilialca", tla_codiE.getText(), false);
      ctUp.commit();
      mensajeErr("REGISTRO ... MODIFICADO");
      mensaje("");
      nav.pulsado = navegador.NINGUNO;
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



  public void canc_addnew()
  {

    try {
      if (tla_codiE.getValorInt() != 0)
      {
        resetBloqueo(dtAdd, "tilialca", tla_codiE.getText(), false);
        borrarRegistro();
        ctUp.commit();
      }
    }  catch (Exception k)
    {
      Error("Error al INSERTAR registro", k);
    }

    activaTodo();
    verDatos();
    mensaje("");
    nav.pulsado=navegador.NINGUNO;
    mensajeErr("Insercion NUEVO registro ... CANCELADO");
  }

  private void borrarRegistro() throws SQLException
  {
    s="delete from tilialca WHERE tla_codi = "+tla_codiE.getValorInt();
    dtAdd.executeUpdate(s);
    s="delete from tilialgr WHERE tla_codi = "+tla_codiE.getValorInt();
    dtAdd.executeUpdate(s);
    s="delete from tilialpr WHERE tla_codi = "+tla_codiE.getValorInt();
    dtAdd.executeUpdate(s);
  }
  public void PADDelete()
  {
    try
    {
      if (!setBloqueo(dtAdd, "tilialca", tla_codiE.getText()))
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
    jtLin.setEnabled(false);
    nav.pulsado=navegador.DELETE;
    tla_nombE.requestFocus();
    Baceptar.setEnabled(true);
    Bcancelar.setEnabled(true);
    mensaje("BORRANDO registro Actual ");
    Bcancelar.requestFocus();
  }

  public void ej_delete1()
  {

     try
     {
       borrarRegistro();
       resetBloqueo(dtAdd, "tilialca", tla_codiE.getText(), false);
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
      resetBloqueo(dtAdd, "tilialca", tla_codiE.getText(), true);
    }
    catch (Exception k)
    {
      Error("Error al quitar Bloqueo", k);
    }
    activaTodo();
    mensaje("");
    mensajeErr("BORRADO de registro ... CANCELADO");
    nav.pulsado = navegador.NINGUNO;
  }


  @Override
  public void activar(boolean b)
  {
    Birgrid.setEnabled(b);
    Pcabe.setEnabled(b);
    tla_codiE.setEnabled(b);
    tla_nombE.setEnabled(b);
    tla_nuliprE.setEnabled(b);
    tla_diagfeE.setEnabled(b);
    tla_vekgcaE.setEnabled(b);
    jtCab.setEnabled(b);
    jtLin.setEnabled(b);
    Baceptar.setEnabled(b);
    Bcancelar.setEnabled(b);
  }

  String getOrderQuery()
  {
    return " ORDER BY tla_codi ";
  }

  int cambiaLineaJtLin(int row)
  {
    try
    {
      if (!pro_codiE.controla(false, false))
      {
        mensajeErr("Codigo de Producto NO valido");
        return 0;
      }
//      pro_nombE.setText(pro_codiE.getNombArt(pro_codiE.getText()));
      jtLin.setValor(pro_codiE.getNombArt(pro_codiE.getText()),row,1);
    }
    catch (Exception k)
    {
      Error("Error al controlar la linea", k);
    }
    return -1;
  }
  void borraLin() throws SQLException
  {
    if (jtCab.getValorDec(1)==0)
      return;
    s="delete from tilialgr WHERE tla_codi = "+tla_codiE.getValorInt()+
        " and tla_orden = "+jtCab.getValorInt(1);
    dtAdd.executeUpdate(s);
    s="delete from tilialpr WHERE tla_codi = "+tla_codiE.getValorInt()+
        " and tla_orden = "+jtCab.getValorInt(1);
    dtAdd.executeUpdate(s);
  }

  int cambiaLineaJtCab(int nLin) throws Exception
  {
    if (jtCab.getValorInt(nLin,1)!=0 && pro_descE.isNull())
    {
      mensajeErr("Introduzca una descripcion del grupo");
      return 0;
    }
    if (! pro_descE.isNull())
    {
      if (Pcabe.isEnabled())
      {
        if (!checkCabe())
          return 0;
        guardaCabe();
        Pcabe.setEnabled(false);
      }
      guardaDescGrupo();
      ctUp.commit();
    }
    return -1;
  }
}
