package gnu.chu.anjelica.compras;

import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import java.sql.*;
import gnu.chu.Menu.*;
import java.awt.*;
import java.util.*;
import javax.swing.BorderFactory;
import gnu.chu.camposdb.*;
import gnu.chu.interfaces.*;
import java.awt.event.*;
import java.text.ParseException;


/**
 *
 * <p>Titulo:   pdclaslom </p>
 * <p>Descripci�n: Mantenimienteos CLASIFICACION de Lomos
 * </p>
 * <p>Copyright: Copyright (c) 2005-2017
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los t�rminos de la Licencia P�blica General de GNU seg�n es publicada por
 *  la Free Software Foundation, bien de la versi�n 2 de dicha Licencia
 *  o bien (seg�n su elecci�n) de cualquier versi�n posterior.
 *  Este programa se distribuye con la esperanza de que sea �til,
 *  pero SIN NINGUNA GARANT�A, incluso sin la garant�a MERCANTIL impl�cita
 *  o sin garantizar la CONVENIENCIA PARA UN PROP�SITO PARTICULAR.
 *  V�ase la Licencia P�blica General de GNU para m�s detalles.
 *  Deber�a haber recibido una copia de la Licencia P�blica General junto con este programa.
 *  Si no ha sido as�, escriba a la Free Software Foundation, Inc.,
 *  en 675 Mass Ave, Cambridge, MA 02139, EEUU.
 * </p>

 * <p>Empresa: MISL</p>
 * @author chuchiP
 * @version 1.0
 */
public class pdclaslom extends ventanaPad implements PAD
{
  prvPanel prv_codiE= new prvPanel();
  CLabel prv_codiL=new CLabel("Proveedor");
  int cllCodi,prvCodi;
  CPanel Pprinc = new CPanel();
  CPanel Pcabe = new CPanel();
  CGridEditable jt = new CGridEditable(3)
  {
    @Override
    public void cambiaColumna(int col, int colNueva,int row)
    {
      try
      {
        if (col ==0)
        {
          String nombArt=pro_codiE.getNombArt(pro_codiE.getText());
          if (nombArt==null)
            jt.setValor("**PRODUCTO NO ENCONTRADO**", row, 1);
          else
            jt.setValor(nombArt, row, 1);
        }
      }
      catch (Exception k)
      {
        Error("Error al buscar Nombre Articulo", k);
      }
    }

    @Override
    public void afterCambiaLinea()
    {
//      tar_feciniG.setText(tar_feciniE.getText());
//      tar_fecfinG.setText(tar_fecfinE.getText());
    }

    @Override
    public int cambiaLinea(int row, int col)
    {
      return cambiaLineaJT();
    }
  };

  String s;
//  CButton Baceptar = new CButton();
//  CButton Bcancelar = new CButton();
  proPanel pro_codiE = new proPanel();
  CTextField pro_nombE = new CTextField();
  CTextField cll_kilosE= new CTextField(Types.DECIMAL,"###9.99");
  CButton Bocul = new CButton();
  CLabel cll_codiL = new CLabel();
  CTextField cll_codiE = new CTextField(Types.DECIMAL,"##9");
  boolean modoConsulta=false;
  GridBagLayout gridBagLayout1 = new GridBagLayout();


  public pdclaslom (EntornoUsuario eu, Principal p)
  {
    this(eu,p,null);
  }

  public pdclaslom(EntornoUsuario eu, Principal p, Hashtable<String,String> ht)
  {
      EU = eu;
      vl = p.panel1;
      jf = p;
      eje = true;

      setTitulo("Mant. Grupos Clasificacion Productos");

      try
      {
        if (ht != null)
        {
          if (ht.get("modoConsulta") != null)
            modoConsulta = Boolean.valueOf(ht.get("modoConsulta"));
        }

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

    public pdclaslom (gnu.chu.anjelica.menu p, EntornoUsuario eu)
    {

      EU = eu;
      vl = p.getLayeredPane();
     setTitulo("Mant. Grupos Clasificacion Productos");
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
      this.setSize(new Dimension(502, 522));
      this.setVersion("2017-01-20");

      strSql = "SELECT prv_codi,cll_codi from claslomos "+
          " group by prv_codi,cll_codi " +
          " order by prv_codi,cll_codi ";

      statusBar = new StatusBar(this);
      conecta();
      nav = new navegador(this, dtCons, false, navegador.NORMAL);
      if (modoConsulta)
      {
        nav.removeBoton(navegador.ADDNEW);
        nav.removeBoton(navegador.EDIT);
        nav.removeBoton(navegador.DELETE);
      }
      Pprinc.setLayout(gridBagLayout1);
      Pcabe.setBorder(BorderFactory.createRaisedBevelBorder());
      Pcabe.setMaximumSize(new Dimension(485, 42));
      Pcabe.setMinimumSize(new Dimension(485, 42));
      Pcabe.setPreferredSize(new Dimension(485, 42));
      Pcabe.setLayout(null);

      ArrayList cabecera = new ArrayList();
      cabecera.add("Producto"); // 0 -- Codigo
      cabecera.add("Nombre"); //1 -- Nombre
      cabecera.add("Kilos Min"); // 2 -- Kilos
      jt.setCabecera(cabecera);
      jt.setAnchoColumna(new int[]{66, 283, 80});
      jt.setAlinearColumna(new int[] {2, 0, 2});
      pro_codiE.iniciar(dtStat, this, vl, EU);
      pro_codiE.setProNomb(null);

      pro_nombE.setEnabled(false);
      ArrayList v = new ArrayList();
      v.add(pro_codiE.getFieldProCodi());
      v.add(pro_nombE);
      v.add(cll_kilosE);
      jt.setCampos(v);
      jt.setMaximumSize(new Dimension(491, 443));
      jt.setMinimumSize(new Dimension(491, 443));
      jt.setPreferredSize(new Dimension(491, 443));
      jt.setAjustarGrid(true);
      jt.setAjusAncCol(true);
      jt.setConfigurar("gnu.chu.anjelica.compras.pdclaslom", EU, dtStat);
      jt.setNumRegCargar(100);
      iniciar(this);
      Baceptar.setMaximumSize(new Dimension(100, 26));
      Baceptar.setMinimumSize(new Dimension(100, 26));
      Baceptar.setPreferredSize(new Dimension(100, 26));
      Bcancelar.setMaximumSize(new Dimension(100, 26));
      Bcancelar.setMinimumSize(new Dimension(100, 26));
      Bcancelar.setPreferredSize(new Dimension(100, 26));
      Pprinc.setInputVerifier(null);
      cll_codiL.setText("Grupo Clasificacion");
      prv_codiL.setBounds(new Rectangle(5, 2, 80, 16));
      prv_codiE.setBounds(new Rectangle(90, 2, 302, 17));
      cll_codiL.setBounds(new Rectangle(117, 20, 112, 17));
      cll_codiE.setBounds(new Rectangle(238, 20, 42, 17));
      Bocul.setBounds(new Rectangle(378, 27, 10, 10));
    
      this.getContentPane().add(nav, BorderLayout.NORTH);
      this.getContentPane().add(statusBar, BorderLayout.SOUTH);
      Pprinc.add(Bcancelar, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
                                                   , GridBagConstraints.EAST,
                                                   GridBagConstraints.NONE,
                                                   new Insets(0, 0, 0, 50), 0, 0));
      Pprinc.add(Baceptar, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.WEST,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 50, 0, 0), 0, 0));
      Pprinc.add(Pcabe,   new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 0, 0), 0, 0));
      this.getContentPane().add(Pprinc, BorderLayout.CENTER);
      Pcabe.add(Bocul, null);
    Pcabe.add(cll_codiE, null);
    Pcabe.add(cll_codiL, null);
     Pcabe.add(prv_codiL, null);
    Pcabe.add(prv_codiE, null);
    Pprinc.add(jt,    new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0
            ,GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    }

  @Override
    public void iniciarVentana() throws Exception
    {
      prv_codiE.iniciar(dtStat, this, vl, EU);
      prv_codiE.setColumnaAlias("prv_codi");
      cll_codiE.setColumnaAlias("cll_codi");
      activarEventos();
      activar(false);
      verDatos();
      nav.requestFocus();
      Pprinc.setDefButton(Baceptar);
    }

    void activarEventos()
    {
      Bocul.addFocusListener(new FocusAdapter()
      {
        public void focusGained(FocusEvent e)
        {
          irGrid();
        }
      });
    }

    void irGrid()
    {
      if (nav.pulsado!=navegador.ADDNEW && nav.pulsado!=navegador.EDIT)
        return;
      if (cll_codiE.isNull() )
      {
        mensaje("Introduzca Codigo de Clasificacion");
        cll_codiE.requestFocus();
        return;
      }
      try {
        if (nav.pulsado == navegador.ADDNEW)
        {
          s = "SELECT * FROM claslomos WHERE "+
              " prv_codi = "+prv_codiE.getValorInt()+" and "+
              " cll_codi = "+cll_codiE.getValorInt();
          if (dtCon1.select(s))
          {
            verDatLin(cll_codiE.getValorInt(),prv_codiE.getValorInt());
            jt.cargaTodo();
            cllCodi=cll_codiE.getValorInt();
            prvCodi=prv_codiE.getValorInt();
            nav.pulsado = navegador.EDIT;
            mensaje("Editando ... ");
          }
        }

        Pcabe.setEnabled(false);
        Baceptar.setEnabled(true);

        jt.setEnabled(true);
        jt.requestFocusInicio();
      } catch (Exception k)
      {
        Error("ERROR al ir al Grid",k);
      }
    }

    void guardaDatos(int prvCodi,int cllCodi) throws SQLException
    {
     
        int nRow = jt.getRowCount();
        dtAdd.addNew("claslomos");
        for (int n = 0; n < nRow; n++)
        {
          if ( jt.getValorInt(n,0)==0)
            continue;
          dtAdd.addNew();
          dtAdd.setDato("prv_codi",prv_codiE.getValorInt());
          dtAdd.setDato("cll_codi",cll_codiE.getValorInt());
          dtAdd.setDato("pro_codi",jt.getValorInt(n,0));
          dtAdd.setDato("cll_kilos",jt.getValorDec(n,2));
          dtAdd.update(stUp);
        }
        ctUp.commit();
       
     
    }
    /**
     * Borrar datos
     * @param cllCodi Codigo de Clas.
     * @throws SQLException Error base de Datos
     * @throws ParseException Error al ejecutar select
     */
    void  borDatos(int prvCodi,int cllCodi) throws SQLException
    {
      s = "DELETE FROM claslomos " +
          " WHERE prv_codi = "+prvCodi +
          " and cll_codi ="+ cllCodi ;
      stUp.executeUpdate(dtAdd.parseaSql(s));
    }
    void verDatos()
    {
      try
      {
        if (dtCons.getNOREG())
          return;
        prv_codiE.setValorInt(dtCons.getInt("prv_codi"));
        cll_codiE.setValorDec(dtCons.getInt("cll_codi"));
        verDatLin(cll_codiE.getValorInt(),prv_codiE.getValorInt());
      } catch (Exception k)
      {
        Error("Error al ver datos",k);
      }
    }

    void verDatLin(int cllCodi,int prvCodi) throws Exception
    {
      s = "SELECT l.pro_codi,p.pro_nomb,l.cll_kilos " +
          " FROM claslomos l,v_articulo p" +
          " WHERE l.cll_codi = "+cllCodi+
          " and prv_codi = "+prvCodi+
          " and l.pro_codi = p.pro_codi "+
          " order by l.cll_kilos";
      if (jt.isEnabled())
        jt.setEnabled(false);
      if (! dtCon1.select(s))
      {
        mensajeErr("Registro BORRADO");
        cll_codiE.resetTexto();
        jt.removeAllDatos();
        return;
      }
      jt.setDatos(dtCon1);
      jt.requestFocusInicio();
    }
  @Override
    public void activar(boolean act)
    {
      activar(navegador.TODOS,act);
    }
    void activar(int modo,boolean act)
    {
      if (modo==navegador.TODOS)
        jt.setEnabled(act);
      Baceptar.setEnabled(act);
      Bcancelar.setEnabled(act);
      Pcabe.setEnabled(act);
    }

  @Override
    public void PADPrimero()
    {
      verDatos();
    }
 @Override
    public void PADAnterior()
    {
      verDatos();
    }
 @Override
    public void PADSiguiente()
    {
      verDatos();
    }
 @Override
    public void PADUltimo()
    {
      verDatos();
    }
     @Override
  public void PADQuery() {
    activar(navegador.QUERY, true);
    Pcabe.setQuery(true);
    Pcabe.resetTexto();
//    eje_numeE.setText(""+EU.ejercicio);
    prv_codiE.requestFocus();

  }
 @Override
  public void ej_query1() {
    Baceptar.setEnabled(false);
    ArrayList v=new ArrayList();
    v.add(prv_codiE.getStrQuery());
    v.add(cll_codiE.getStrQuery());
    Pcabe.setQuery(false);
    s="SELECT cll_codi,prv_codi FROM claslomos ";
    s=creaWhere(s,v);
    s+=" group by prv_codi,cll_codi"+
        " order by prv_codi,cll_codi";
//    debug(s);
    try
    {
      if (!dtCons.select(s))
      {
        mensaje("");
        mensajeErr("No encontrados Registros para estos criterios");
        rgSelect();
        activaTodo();
        return;
      }
      mensaje("");
      strSql = s;
      activaTodo();
      rgSelect();
      verDatos();
      mensajeErr("Nuevos regisgtros selecionados");
    }
    catch (Exception ex)
    {
      fatalError("Error al buscar Tarifas: ", ex);
    }

  }
@Override
  public void canc_query() {
    Pcabe.setQuery(false);

    mensaje("");
    mensajeErr("Consulta ... CANCELADA");
    activaTodo();
    verDatos();
  }
@Override
  public void PADEdit() {
    mensaje("Editando ....");
//    ejeNume=eje_numeE.getValorInt();
    jt.cargaTodo();
    prvCodi=prv_codiE.getValorInt();
    cllCodi=cll_codiE.getValorInt();
    activar(true);

    jt.requestFocusFinal();
    jt.mueveSigLinea(0);
  }
  @Override
  public void ej_edit1() {
    jt.actualizarGrid();
    if (cambiaLineaJT()>=0)
    {
      jt.requestFocusSelected();
      return;
    }
    try {
        borDatos(prvCodi,cllCodi);
        guardaDatos(prv_codiE.getValorInt(),cll_codiE.getValorInt());
    } catch (SQLException k)
    {
        Error("Error al actualizar datos",k);
        return;
    }
    mensajeErr("Datos Clasificación actualizados");
    activaTodo();
    verDatos();
    mensaje("");
  }
  @Override
  public void canc_edit() {
    activaTodo();
    mensajeErr("Modificacion de Datos ... Cancelada");
    verDatos();
    mensaje("");
  }
  @Override
  public void PADAddNew() {
    Pcabe.resetTexto();
    jt.removeAllDatos();
    activar(navegador.QUERY, true);
    Baceptar.setEnabled(false);
//    eje_numeE.setValorDec(EU.ejercicio);
    prv_codiE.requestFocus();
    mensaje("Insertando ....");
  }
@Override
  public void ej_addnew1() {
    jt.actualizarGrid();

    if (cambiaLineaJT()>=0)
    {
      jt.requestFocusSelected();
      return;
    }
    try 
    {        
        guardaDatos(prv_codiE.getValorInt(), cll_codiE.getValorInt());
    }  catch (SQLException k)
    {
        Error("Error al modificar datos",k);
        return;
    }
    activaTodo();
    verDatos();
    mensajeErr("Datos ... Guardados");
    mensaje("");
  }
  @Override
  public void canc_addnew() {
    activaTodo();
    mensajeErr("Insercion de Datos ... Cancelada");
    verDatos();
    mensaje("");
  }
  @Override
  public void PADDelete() {
    Baceptar.setEnabled(true);
    Bcancelar.setEnabled(true);
    Bcancelar.requestFocus();
    mensaje("Borrando ....");
  }
  @Override
  public void ej_delete1() {
    try
    {
      borDatos(prv_codiE.getValorInt(),cll_codiE.getValorInt());
      ctUp.commit();
    } catch (SQLException k)
    {
      Error("Error al borrar datos",k);
      return;
    }
    activaTodo();
    verDatos();
    mensaje("");
    mensajeErr("Datos .... Borrados");
  }
  @Override
  public void canc_delete() {
    activaTodo();
    verDatos();
    mensaje("");
    mensajeErr("Borrado de Registro ... ANULADO");
  }

  int cambiaLineaJT()
  {
    if (pro_codiE.getValorInt()==0)
      return -1; // No hay producto ... paso
    try {

      if (!pro_codiE.controla(false))
      {
        mensajeErr(pro_codiE.getMsgError());
        return 0;
      }
    } catch (Exception k)
    {
      Error("ERROR AL controlar Linea del Grid",k);
      return 0;
    }
    return -1;
  }


}
