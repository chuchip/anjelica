package gnu.chu.anjelica.despiece;

/**
 *
 * <p>Título: MantTipDesp </p>
 * <p>Descripcion: Mantenimiento Tipos de Despiece</p>
 * <p>Empresa: miSL</p>
*  <p>Copyright: Copyright (c) 2005-2011
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
import gnu.chu.Menu.Principal;
import gnu.chu.controles.CLinkBox;
import gnu.chu.controles.StatusBar;
import gnu.chu.interfaces.PAD;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.navegador;
import gnu.chu.utilidades.ventanaPad;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.*;
import java.sql.SQLException;
import java.sql.Types;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;

public class MantTipDesp  extends ventanaPad implements PAD
{
  public final static int AUTO_DESPIECE=9999;
  public final static int LIBRE_DESPIECE=9998;
  public final static int CONGELADO_DESPIECE=9997;
  String s;
  boolean ARG_MODCONSULTA=false;
  final int JTSAL_CODPRO=0;
  final int JTSAL_NOMBR=1;
  final int JTSAL_UNID=2;
  final int JTSAL_COSTO=3;
  final int JTSAL_GRUPO=4;
    /** Creates new form MantTipDesp */
   public MantTipDesp(EntornoUsuario eu, Principal p)
  {
    this(eu,p,null);
  }

  public MantTipDesp(EntornoUsuario eu, Principal p, Hashtable ht)
  {
      EU = eu;
      vl = p.panel1;
      jf = p;
      eje = true;

      setTitulo("Mantenimiento Tipos de Despiece");

      try
      {
        if (ht != null)
        {
          if (ht.get("modoConsulta") != null)
            ARG_MODCONSULTA = Boolean.valueOf(ht.get("modoConsulta").toString()).booleanValue();
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

    public MantTipDesp(gnu.chu.anjelica.menu p, EntornoUsuario eu)
    {

      EU = eu;
      vl = p.getLayeredPane();
      setTitulo("Mantenimiento Tipos de Despiece");
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
      this.setVersion("2012-01-04" + (ARG_MODCONSULTA ? " SOLO LECTURA" : ""));
      statusBar = new StatusBar(this);
      nav = new navegador(this,dtCons,false);
      iniciarFrame();
      strSql = "SELECT * FROM tipodesp where tid_activ!=0 "+
          " order by tid_codi";
      this.getContentPane().add(nav, BorderLayout.NORTH);
      this.getContentPane().add(statusBar, BorderLayout.SOUTH);
      this.setPad(this);
      navActivarAll();
      dtCons.setLanzaDBCambio(false);
      conecta();
      initComponents();
      iniciarBotones(Baceptar, Bcancelar);
     
     
      activar(false);
      if (ARG_MODCONSULTA)
      {
        nav.removeBoton(navegador.ADDNEW);
        nav.removeBoton(navegador.EDIT);
        nav.removeBoton(navegador.DELETE);
      }
      this.setSize(new Dimension(682, 522));
    }
      @Override
    public void iniciarVentana() throws Exception
    {
      tid_codiE.setColumnaAlias("tid_codi");
      tid_nombE.setColumnaAlias("tid_nomb");
      tid_activE.setColumnaAlias("tid_activ");
      tid_fecaltE.setColumnaAlias("tid_fecalt");
      tid_feulmoE.setColumnaAlias("tid_feulmo");
      usu_nombE.setColumnaAlias("usu_nomb");
      activarEventos();
      activar(false);
      verDatos(dtCons);
      nav.requestFocus();
      Pprinc.setButton(KeyEvent.VK_F2, Bocul);
      jtEnt.setButton(KeyEvent.VK_F2, Bocul1);
      jtSal.setButton(KeyEvent.VK_F2, Bocul2);
      Pprinc.setDefButton(Baceptar);
//      Pprinc.setEscButton(Bcancelar);
    }

    void activarEventos()
    {
      Bocul.addFocusListener(new FocusAdapter()
      {
            @Override
        public void focusGained(FocusEvent e)
        {
          irGrid();
        }
      });
      
       Bocul1.addActionListener(new ActionListener()
       {
         @Override
        public void actionPerformed(ActionEvent e){
          if (! jtEnt.isEnabled())
          {
              Bocul2.doClick();
              return;
          }
          if (cambiaLineaJT()>=0)
            jtEnt.requestFocusLater();
          else
          {
            jtEnt.setEnabled(false);
            jtSal.setEnabled(true);
            jtSal.requestFocusInicioLater();
          }
         }
       });
       Bocul2.addActionListener(new ActionListener()
       {
         @Override
        public void actionPerformed(ActionEvent e){
            if (! jtSal.isEnabled())
            {
               Bocul1.doClick();
              return;
            }
            if (cambiaLineaSal()>=0)
                jtSal.requestFocusLater();
            else
            {
                jtSal.setEnabled(false);
                jtEnt.setEnabled(true);
                jtEnt.requestFocusInicioLater();
            }
         }
       });
       Bcopiar.addActionListener(new ActionListener()
       {
         @Override
        public void actionPerformed(ActionEvent e){
            if (! jtSal.isEnabled() || tid_codcopE.getValorInt()==0)
              return;
            
            if (cambiaLineaSal()>=0)
            {
                jtSal.requestFocusLater();
                return;
            }
            try  {
                verDatSal(tid_codcopE.getValorInt());
            } catch (SQLException k)
            {
                Error("Error al copiar Lineas de Salida",k);
            }
             
         }
       });
               
    }

    int actFecha(java.util.Date fecha)
    {
      GregorianCalendar gc = new GregorianCalendar();
      gc.setTime(fecha);
      return gc.get(GregorianCalendar.WEEK_OF_YEAR);
    }
    void irGrid()
    {
      if (nav.pulsado!=navegador.ADDNEW && nav.pulsado!=navegador.EDIT)
        return;
      if (tid_codiE.isNull() || tid_nombE.isNull())
      {
        tid_codiE.requestFocus();
        return;
      }
      if (tid_codiE.getValorInt()> 9990)
      {
          mensajeErr("Codigos superiores a 9990 estan reservados para uso interno");
          tid_codiE.requestFocus();
          return ;
      }
      try {
        if (nav.pulsado == navegador.ADDNEW)
        {
          s = "SELECT * FROM tipodesp WHERE "+
              " tid_codi = "+tid_codiE.getValorInt();
          if (dtCon1.select(s))
          {
            verDatEnt(tid_codiE.getValorInt());
            verDatSal(tid_codiE.getValorInt());
            jtEnt.cargaTodo();
            nav.pulsado = navegador.EDIT;
            mensaje("Editando ... ");
          }
        }
        Pcabe.setEnabled(false);
        Baceptar.setEnabled(true);
        jtEnt.setEnabled(true);
        jtSal.setEnabled(true);
        jtSal.requestFocusInicio();
        jtEnt.requestFocusInicio();
      } catch (Exception k)
      {
        Error("ERROR al ir al Grid",k);
      }
    }

    void guardaDatos(int tidCodi)
    {
      try {
        if (tidCodi>=0)
          borDatos(tidCodi);
        s="SELECT * FROM tipodesp " +
          " WHERE tid_codi = " + tidCodi;
        if (dtAdd.select(s,true))
        {
            dtAdd.edit();
            dtAdd.setDato("tid_feulmo",Formatear.getDateAct());
            dtAdd.setDato("usu_nomb",EU.usuario);
        }
        else
        {
            dtAdd.addNew("tipodesp");
            dtAdd.setDato("tid_codi",tid_codiE.getValorInt());
            dtAdd.setDato("tid_fecalt",Formatear.getDateAct());
        }
        dtAdd.setDato("tid_nomb",tid_nombE.getText());
        dtAdd.setDato("tid_activ",tid_activE.getValor());
        dtAdd.setDato("tid_agrup",tid_agrupE.isSelected()?-1:0);
        dtAdd.setDato("tid_usoequ",tid_usoequE.isSelected()?-1:0);
        dtAdd.update(stUp);
        int nRow = jtEnt.getRowCount();
        dtAdd.addNew("tipdesent");
        int nl=0;
        for (int n = 0; n < nRow; n++)
        {
          if ( jtEnt.getValorInt(n,0)==0)
            continue;
          dtAdd.addNew();
          dtAdd.setDato("tid_codi",tid_codiE.getValorInt());
          dtAdd.setDato("pro_codi",jtEnt.getValorInt(n,0));
          dtAdd.setDato("tde_nuli",nl);
          dtAdd.update(stUp);
          nl++;
        }
        dtAdd.addNew("tipdessal");
        nl = 0;
        nRow = jtSal.getRowCount();
        for (int n = 0; n < nRow; n++)
        {
          if (jtSal.getValorInt(n, 0) == 0)
            continue;
          dtAdd.addNew();
          dtAdd.setDato("tid_codi", tid_codiE.getValorInt());
          dtAdd.setDato("pro_codi", jtSal.getValorInt(n, JTSAL_CODPRO));
          dtAdd.setDato("tds_unid", jtSal.getValorInt(n, JTSAL_UNID));
          dtAdd.setDato("tds_costo", jtSal.getValorInt(n, JTSAL_COSTO));
          dtAdd.setDato("tds_grupo", jtSal.getValorInt(n, JTSAL_GRUPO));
          dtAdd.update(stUp);
          nl++;
        }
        ctUp.commit();
        mensajeErr("Datos ... Guardados");
      } catch (Exception k)
      {
        Error("Error en La insercion de Referencias",k);
        return;
      }
    }
    /**
     * Borrar datos
     * @param ejerc Ejercicio
     * @param nusem Numero Semana
     * @throws SQLException Error base de Datos
     */
    void  borDatos(int tipo) throws SQLException,java.text.ParseException
    {
      s = "DELETE FROM tipdesent " +
          " WHERE tid_codi = " + tipo;
      stUp.executeUpdate(dtAdd.parseaSql(s));
      s = "DELETE FROM tipdessal " +
          " WHERE tid_codi = " + tipo;
      stUp.executeUpdate(dtAdd.parseaSql(s));
    }
    void verDatos(DatosTabla dt)
    {
      try
      {
        jtEnt.removeAllDatos();
        jtSal.removeAllDatos();
        if (dt.getNOREG())
          return;
        s="SELECT * FROM tipodesp  WHERE tid_codi = "+dt.getString("tid_codi");
        if (!dtStat.select(s))
       {
         mensajeErr("NO encontrado Despiece .. Probablemente se ha borrado");
         return;
        }
        tid_codiE.setText(dtStat.getString("tid_codi"));
        tid_nombE.setText(dtStat.getString("tid_nomb"));
        tid_activE.setValor(dtStat.getString("tid_activ"));
        tid_fecaltE.setDate(dtStat.getDate("tid_fecalt"));
        tid_feulmoE.setDate(dtStat.getDate("tid_feulmo"));
        usu_nombE.setText(dtStat.getString("usu_nomb"));
        tid_usoequE.setSelected(dtStat.getInt("tid_usoequ")!=0);
        tid_agrupE.setSelected(dtStat.getInt("tid_agrup")!=0);
        verDatEnt(tid_codiE.getValorInt());
        verDatSal(tid_codiE.getValorInt());

      } catch (Exception k)
      {
        Error("Error al ver datos",k);
        return;
      }
    }

    void verDatEnt(int tidCodi) throws Exception
    {
      s = "SELECT d.pro_codi,p.pro_nomb " +
          " FROM tipdesent d,v_articulo p" +
          " WHERE tid_codi = "+tidCodi+
          " AND d.pro_codi = p.pro_codi "+
          " order by tde_nuli";
      jtEnt.setEnabled(false);
      if (! dtCon1.select(s))
      {
        mensajeErr("Sin Codigos de Entrada");
        return;
      }
      do
      {
        Vector v=new Vector();
        v.addElement(dtCon1.getString("pro_codi"));
        v.addElement(dtCon1.getString("pro_nomb"));
        jtEnt.addLinea(v);
      } while (dtCon1.next());
      jtEnt.requestFocusInicio();
    }

    void verDatSal(int tidCodi) throws SQLException
    {
      s = "SELECT d.pro_codi,tds_unid,tds_grupo,tds_costo,p.pro_nomb " +
          " FROM tipdessal d,v_articulo p" +
          " WHERE tid_codi = " + tidCodi +
          " AND d.pro_codi = p.pro_codi " +
          " order by tds_grupo,d.pro_codi";
      boolean jtEnab=jtSal.isEnabled();
      jtSal.setEnabled(false);
      if (!dtCon1.select(s))
      {
        mensajeErr("Sin Codigos de Salida");
        return;
      }
      llenaDatSal(dtCon1);
      jtSal.setEnabled(jtEnab);
      jtSal.requestFocusInicio();
    }
    void llenaDatSal(DatosTabla dt) throws SQLException
    {
      do
      {
        Vector v = new Vector();
        v.addElement(dt.getString("pro_codi"));
        v.addElement(dt.getString("pro_nomb"));
        v.addElement(dt.getString("tds_unid"));
        v.addElement(dt.getString("tds_costo"));
        v.addElement(dt.getString("tds_grupo"));
        jtSal.addLinea(v);
      }
      while (dtCon1.next());
    
    }

    public void activar(boolean act)
    {
      activar(navegador.TODOS,act);
    }
    void activar(int modo,boolean act)
    {
      if (modo==navegador.TODOS || modo==navegador.QUERY || modo==navegador.ADDNEW)
      {
        jtEnt.setEnabled(act);
        jtSal.setEnabled(act);
      }
      tid_usoequE.setEnabled(act);
      tid_agrupE.setEnabled(act);
      tid_codiE.setEnabled(act);
      Baceptar.setEnabled(act);
      Bcancelar.setEnabled(act);
      Pcabe.setEnabled(act);
      if (modo!=navegador.QUERY )
      {
          tid_codcopE.setEnabled(act);
          Bcopiar.setEnabled(act);
      }
      tid_fecaltE.setEnabled(false);
      tid_feulmoE.setEnabled(false);
      usu_nombE.setEnabled(false);
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
    @Override
  public void PADQuery() {
    
    tid_fecaltE.setEnabled(true);
    tid_feulmoE.setEnabled(true);
    usu_nombE.setEnabled(true);
    Pcabe.setQuery(true);
    activar(navegador.QUERY, true);
    tid_usoequE.setEnabled(false);
    tid_agrupE.setEnabled(false);
    Pcabe.resetTexto();
    jtEnt.removeAllDatos();
    jtSal.removeAllDatos();
    tid_activE.resetTexto();
    tid_codiE.requestFocus();
  }

  public void ej_query1() {
    Component c;
    if ((c=Pcabe.getErrorConf())!=null)
    {
        msgBox("Parametros de consulta no validos");
        c.requestFocus();
        return;
    }
    Baceptar.setEnabled(false);
    Vector v=new Vector();
    v.addElement(tid_codiE.getStrQuery());
    v.addElement(tid_nombE.getStrQuery());
    v.addElement(tid_activE.getStrQuery());
    v.addElement(tid_fecaltE.getStrQuery());
    v.addElement(tid_feulmoE.getStrQuery());
    v.addElement(usu_nombE.getStrQuery());
    Pcabe.setQuery(false);
    s="SELECT * FROM tipodesp ";
    s=creaWhere(s,v);
    jtEnt.salirGrid();
    jtSal.salirGrid();
   
    if (jtEnt.getValorInt(0,0)!=0)
        s+=(s.indexOf("WHERE")==-1?" WHERE ":" AND ")+
                "  tid_codi in (select tid_codi from tipdesent where pro_codi =  "+jtEnt.getValorInt(0,0)+")";

    if (jtSal.getValorInt(0,0)!=0)
        s+=(s.indexOf("WHERE")==-1?" WHERE ":" AND ")+
                " tid_codi in (select tid_codi from tipdessal where pro_codi =  "+jtSal.getValorInt(0,0)+")";

    s+=" order by tid_codi";
//    debug(s);
    try
    {
      if (!dtCons.select(s))
      {
        mensaje("");
        mensajeErr("No encontrados Registros para estos criterios");
        rgSelect();
        verDatos(dtCons);
        activaTodo();
        return;
      }
      mensaje("");
      strSql = s;
      activaTodo();
      rgSelect();
      verDatos(dtCons);
      mensajeErr("Nuevos regisgtros selecionados");
    }
    catch (Exception ex)
    {
      fatalError("Error al buscar Tipos de Despieces: ", ex);
    }

  }

  public void canc_query() {
    Pcabe.setQuery(false);

    mensaje("");
    mensajeErr("Consulta ... CANCELADA");
    activaTodo();
    verDatos(dtCons);
  }

    @Override
  public void PADEdit() {
    mensaje("Editando ....");
    jtEnt.cargaTodo();

    try {
      s="SELECT * FROM tipodesp  WHERE tid_codi = "+tid_codiE.getValorInt();
      if (!dtStat.select(s))
      {
        mensajeErr("NO encontrado Despieze .. Probablemente se ha borrado");
        activaTodo();
        return;
      }
      verDatos(dtStat);
    } catch (Exception k)
    {
      Error("Error al Buscar tipo Despieze",k);
    }
    activar(true);
    tid_codiE.setEnabled(false);
    jtSal.setEnabled(false);
    jtEnt.requestFocusInicio();
  }
  public void ej_edit1() {
     if (jtEnt.isEnabled())
     {
        jtEnt.salirGrid();
        if (cambiaLineaJT()>=0)
        {
          jtEnt.requestFocusSelected();
          return;
        }
     }
    if (jtSal.isEnabled())
    {
        jtSal.salirGrid();
        if (cambiaLineaSal() >= 0)
        {
          jtSal.requestFocusSelected();
          return;
        }
    }
    if (! checkNumLin())
      return;
    guardaDatos(tid_codiE.getValorInt());
    activaTodo();
//    verDatos(dtCons);
    mensaje("");
  }
  public void canc_edit() {
    activaTodo();
    mensajeErr("Modificacion de Datos ... Cancelada");
    verDatos(dtCons);
    mensaje("");
  }
    @Override
  public void PADAddNew() {
    Pcabe.resetTexto();
    jtEnt.removeAllDatos();
    jtSal.removeAllDatos();
    activar(navegador.ADDNEW, true);
    Baceptar.setEnabled(false);
//    eje_numeE.setValorDec(EU.ejercicio);
    
    tid_usoequE.setSelected(true);
    tid_agrupE.setSelected(false);
    tid_codiE.requestFocus();
    mensaje("Insertando ....");
  }

  public void ej_addnew1() {
    jtEnt.salirFoco();

    if (cambiaLineaJT()>=0)
    {
      jtEnt.requestFocusSelected();
      return;
    }
    jtSal.salirFoco();
    if (cambiaLineaSal() >= 0)
    {
      jtSal.requestFocusSelected();
      return;
    }
    if (! checkNumLin())
      return;
    guardaDatos(-1);
    activaTodo();
//    verDatos();
    mensaje("");
  }
  public void canc_addnew() {
    activaTodo();
    mensajeErr("Insercion de Datos ... Cancelada");
    verDatos(dtCons);
    mensaje("");
  }
    @Override
  public void PADDelete() {
    Baceptar.setEnabled(true);
    Bcancelar.setEnabled(true);
    Bcancelar.requestFocus();
    mensaje("Borrando ....");
  }
  public void ej_delete1() {
    try
    {
      s = "DELETE FROM tipodesp " +
          " WHERE tid_codi = " + tid_codiE.getValorInt();
      stUp.executeUpdate(dtAdd.parseaSql(s));
      borDatos(tid_codiE.getValorInt());
      ctUp.commit();
    } catch (Exception k)
    {
      Error("Error al borrar datos",k);
    }
    activaTodo();
    verDatos(dtCons);
    mensaje("");
    mensajeErr("Datos .... Borrados");
  }
  public void canc_delete() {
    activaTodo();
    verDatos(dtCons);
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
        return 2;
      }
   
    } catch (Exception k)
    {
      Error("ERROR AL controlar Linea del Grid",k);
      return 0;
    }
    return -1;
  }
  int cambiaLineaSal()
  {
    if (pro_codsE.getValorInt() == 0)
      return -1; // No hay producto ... paso
    try
    {
      if (!pro_codsE.controla(false))
      {
        mensajeErr(pro_codsE.getMsgError());
        return 0;
      }


    } catch (Exception k)
    {
      Error("ERROR AL controlar Salida del Grid",k);
      return 0;
    }

    return -1;
  }
  boolean checkNumLin()
  {
    int nRow = jtEnt.getRowCount();
    int nLin = 0;
    for (int n = 0; n < nRow; n++)
    {
      if (jtEnt.getValorInt(n, 0) != 0)
        nLin++;
    }
    if (nLin == 0)
    {
      mensajeErr("Introduzca Algun producto de Entrada");
      jtEnt.requestFocusInicio();
      return false;
    }
    nRow = jtSal.getRowCount();
    nLin = 0;
    for (int n = 0; n < nRow; n++)
    {
      if (jtSal.getValorInt(n, JTSAL_CODPRO) != 0)
        nLin++;
    }
    if (nLin == 0)
    {
      mensajeErr("Introduzca Algun producto de Salida");
      jtSal.requestFocusInicio();
      return false;
    }
    return true;
  }
  /**
   * LLena un Linkbox con todos los tipos de despieces activos disponibles
   * @param dt
   * @param lk
   * @throws SQLException 
   */
  public static void llenaTipDesp(DatosTabla dt, CLinkBox lk) throws SQLException
  {
     llenaTipDesp(dt,lk,0);
  }
  /**
   * LLena un Linkbox con todos los tipos de despieces  disponibles
   * @param dt
   * @param lk
   * @param tid_activ -1 TODO,0 Tipos despiece Activos,  OTROS solo si tid_activ es mayor
   * @throws SQLException 
   */
  public static void llenaTipDesp(DatosTabla dt, CLinkBox lk,int tid_activ) throws SQLException
  {
      String s = "SELECT tid_codi,tid_nomb FROM tipodesp "+
              (tid_activ==-1?"":
              " WHERE tid_activ>"+tid_activ)+" order by tid_nomb";
      dt.select(s);
      lk.addDatos(dt);
  }
  /**
   * Comprueba si un articulo esta en los productos de salida de un tipo de despiece
   * @param dt DatosTabla
   * @param proCodi Articulo
   * @param tidCodi Tipo de despiece
   * @return true si existe
   * @throws SQLException
   */
   public static boolean checkArticuloSalida(DatosTabla dt, int proCodi,int tidCodi) throws SQLException
  {
      if (tidCodi==LIBRE_DESPIECE)
          return true;
      String s = "SELECT * FROM tipdessal where pro_codi="+proCodi+
              " and tid_codi = "+tidCodi;
      if ( dt.select(s))
          return true;
      if (!isArticEquiv(tidCodi, dt))
           return false;
      s = "SELECT * FROM tipdessal where pro_codi in "+
              " (SELECT pro_codfin FROM artiequiv where pro_codini = "+proCodi+
               " UNION SELECT pro_codini FROM artiequiv where pro_codfin = "+proCodi+" )"+
              " and tid_codi = "+tidCodi;
       return dt.select(s);
  }
   /**
   * Comprueba si un articulo esta en los productos de entrada un tipo de despiece
   * @param dt DatosTabla
   * @param proCodi Articulo
   * @param tidCodi Tipo de despiece
   * @return true si existe
   * @throws SQLException
   */
  public static boolean checkArticuloEntrada(DatosTabla dt, int proCodi,int tidCodi) throws SQLException
  {
      if (tidCodi==LIBRE_DESPIECE || tidCodi==AUTO_DESPIECE)
          return true;
      String s = "SELECT * FROM tipdesent where pro_codi="+proCodi+
              " and tid_codi = "+tidCodi;
      if (dt.select(s))
          return true;
       if (!isArticEquiv(tidCodi, dt))
           return false;
       s = "SELECT * FROM tipdesent where pro_codi in (SELECT pro_codfin FROM artiequiv where pro_codini = "+proCodi+
               " UNION SELECT pro_codini FROM artiequiv where pro_codfin = "+proCodi+" )"+
              " and tid_codi = "+tidCodi;
       return dt.select(s);
  }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pro_codiE = new gnu.chu.camposdb.proPanel();
        pro_nombE = new gnu.chu.controles.CTextField();
        pro_codsE = new gnu.chu.camposdb.proPanel();
        pro_nombsE = new gnu.chu.controles.CTextField();
        tds_unidE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#9");
        tds_grupoE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#9");
        tds_costoE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9.99");
        Pprinc = new gnu.chu.controles.CPanel();
        Pcabe = new gnu.chu.controles.CPanel();
        cLabel1 = new gnu.chu.controles.CLabel();
        tid_codiE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        cLabel2 = new gnu.chu.controles.CLabel();
        tid_nombE = new gnu.chu.controles.CTextField(Types.CHAR, "X", 50);
        cLabel3 = new gnu.chu.controles.CLabel();
        tid_activE = new gnu.chu.controles.CComboBox();
        cLabel4 = new gnu.chu.controles.CLabel();
        tid_fecaltE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yy");
        cLabel5 = new gnu.chu.controles.CLabel();
        tid_feulmoE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yy");
        cLabel6 = new gnu.chu.controles.CLabel();
        usu_nombE = new gnu.chu.controles.CTextField(Types.CHAR, "X", 50);
        Bocul = new gnu.chu.controles.CButton();
        tid_usoequE = new gnu.chu.controles.CCheckBox();
        tid_agrupE = new gnu.chu.controles.CCheckBox();
        jtEnt = new gnu.chu.controles.CGridEditable(2){
            public void cambiaColumna(int col, int colNueva,int row)
            {
                try
                {
                    if (col == 0)
                    {
                        String nombArt = pro_codiE.getNombArt(pro_codiE.getText());
                        if (nombArt == null)
                        jtEnt.setValor("**PRODUCTO NO ENCONTRADO**", row, 1);
                        else
                        jtEnt.setValor(nombArt, row, 1);
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
        }
        ;
        jtSal = new gnu.chu.controles.CGridEditable(5){
            @Override
            public void cambiaColumna(int col,int colNueva, int row)
            {
                try
                {
                    if (col == 0)
                    {
                        String nombArt = pro_codsE.getNombArt(pro_codsE.getText());
                        if (nombArt == null)
                        jtSal.setValor("**PRODUCTO NO ENCONTRADO**", row, 1);
                        else
                        jtSal.setValor(nombArt, row, 1);
                    }
                }
                catch (Exception k)
                {
                    Error("Error al buscar Nombre Articulo", k);
                }
            }
            @Override
            public int cambiaLinea(int row, int col)
            {
                return cambiaLineaSal();
            }
        };
        ;
        Ppie = new gnu.chu.controles.CPanel();
        Baceptar = new gnu.chu.controles.CButton();
        Bcancelar = new gnu.chu.controles.CButton();
        Bocul1 = new gnu.chu.controles.CButton();
        Bocul2 = new gnu.chu.controles.CButton();
        cLabel7 = new gnu.chu.controles.CLabel();
        tid_codcopE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        Bcopiar = new gnu.chu.controles.CButton();

        pro_nombE.setEnabled(false);

        pro_nombsE.setEnabled(false);

        Pprinc.setLayout(new java.awt.GridBagLayout());

        Pcabe.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        Pcabe.setMaximumSize(new java.awt.Dimension(640, 50));
        Pcabe.setMinimumSize(new java.awt.Dimension(640, 50));
        Pcabe.setPreferredSize(new java.awt.Dimension(640, 50));
        Pcabe.setLayout(null);

        cLabel1.setText("Tipo Despiece ");
        Pcabe.add(cLabel1);
        cLabel1.setBounds(2, 2, 81, 17);
        Pcabe.add(tid_codiE);
        tid_codiE.setBounds(93, 2, 39, 17);

        cLabel2.setText("Descripción");
        Pcabe.add(cLabel2);
        cLabel2.setBounds(140, 2, 71, 17);
        Pcabe.add(tid_nombE);
        tid_nombE.setBounds(220, 2, 300, 17);

        cLabel3.setText("Activo");
        Pcabe.add(cLabel3);
        cLabel3.setBounds(2, 23, 42, 17);

        tid_activE.addItem("Si", "2");
        tid_activE.addItem("No", "0");
        tid_activE.addItem("Tactil","1");
        Pcabe.add(tid_activE);
        tid_activE.setBounds(48, 23, 52, 17);

        cLabel4.setText("Alta ");
        Pcabe.add(cLabel4);
        cLabel4.setBounds(250, 23, 30, 17);

        tid_fecaltE.setEnabled(false);
        Pcabe.add(tid_fecaltE);
        tid_fecaltE.setBounds(280, 23, 60, 17);

        cLabel5.setText("Ult. Mod");
        Pcabe.add(cLabel5);
        cLabel5.setBounds(350, 23, 60, 17);

        tid_feulmoE.setEnabled(false);
        Pcabe.add(tid_feulmoE);
        tid_feulmoE.setBounds(410, 23, 60, 17);

        cLabel6.setText("Usuario");
        Pcabe.add(cLabel6);
        cLabel6.setBounds(480, 23, 50, 17);

        usu_nombE.setEnabled(false);
        Pcabe.add(usu_nombE);
        usu_nombE.setBounds(530, 23, 100, 17);

        Bocul.setToolTipText("Permitir codigos equivalentes");
        Pcabe.add(Bocul);
        Bocul.setBounds(100, 30, 2, 2);

        tid_usoequE.setSelected(true);
        tid_usoequE.setText("Equivalentes");
        Pcabe.add(tid_usoequE);
        tid_usoequE.setBounds(530, 2, 100, 17);

        tid_agrupE.setSelected(true);
        tid_agrupE.setText("Agrupar");
        Pcabe.add(tid_agrupE);
        tid_agrupE.setBounds(120, 23, 110, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 0);
        Pprinc.add(Pcabe, gridBagConstraints);

        try {
            pro_codiE.iniciar(dtStat, this, vl, EU);
            pro_codiE.setProNomb(null);
            pro_codiE.setAceptaInactivo(false);
            Vector cabecera = new Vector();
            cabecera.addElement("Codigo"); // 0 -- Codigo Producto
            cabecera.addElement("Nombre"); //1  -- Nombre Producto
            jtEnt.setCabecera(cabecera);
            jtEnt.setAnchoColumna(new int[]{46, 283});
            jtEnt.alinearColumna(new int[] {2, 0});
            Vector v = new Vector();
            v.addElement(pro_codiE.pro_codiE);
            v.addElement(pro_nombE);
            jtEnt.setCampos(v);
            jtEnt.setAjustarGrid(true);
            jtEnt.setAjusAncCol(true);
        } catch (Exception k ){}
        jtEnt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtEnt.setMaximumSize(new java.awt.Dimension(620, 110));
        jtEnt.setMinimumSize(new java.awt.Dimension(620, 110));

        javax.swing.GroupLayout jtEntLayout = new javax.swing.GroupLayout(jtEnt);
        jtEnt.setLayout(jtEntLayout);
        jtEntLayout.setHorizontalGroup(
            jtEntLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 672, Short.MAX_VALUE)
        );
        jtEntLayout.setVerticalGroup(
            jtEntLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 136, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        Pprinc.add(jtEnt, gridBagConstraints);

        try {
            pro_codsE.iniciar(dtStat, this, vl, EU);
            pro_codsE.setProNomb(null);
            pro_codsE.setAceptaInactivo(false);
            Vector linea = new Vector();
            linea.addElement("Codigo"); // 0 -- Codigo Producto
            linea.addElement("Nombre"); //1  -- Nombre Producto
            linea.addElement("N. Unid"); // 2  -- Unidades x unidad entrada
            linea.addElement("Costo"); // 3 Costo Estimado
            linea.addElement("Grupo"); // 4  -- Grupo
            jtSal.setCabecera(linea);
            jtSal.setPonValoresInFocus(false);
            jtSal.setAnchoColumna(new int[] {46, 283,60,60,40});
            jtSal.alinearColumna(new int[] {2, 0,2,2,2});
            jtSal.setFormatoColumna(3, "##9.99");
            tds_unidE.setValorDec(1);
            tds_grupoE.setValorDec(1);
            Vector vecL=new Vector();
            vecL.addElement(pro_codsE.pro_codiE);//0
            vecL.addElement(pro_nombsE); // 1
            vecL.addElement(tds_unidE); // 2
            vecL.addElement(tds_costoE); // 3
            vecL.addElement(tds_grupoE); //4
            jtSal.setCampos(vecL);
            jtSal.setAjustarGrid(true);
            jtSal.setAjusAncCol(true);
        } catch (Exception k){}
        jtSal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtSal.setMaximumSize(new java.awt.Dimension(620, 220));
        jtSal.setMinimumSize(new java.awt.Dimension(620, 220));

        javax.swing.GroupLayout jtSalLayout = new javax.swing.GroupLayout(jtSal);
        jtSal.setLayout(jtSalLayout);
        jtSalLayout.setHorizontalGroup(
            jtSalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 672, Short.MAX_VALUE)
        );
        jtSalLayout.setVerticalGroup(
            jtSalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 218, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        Pprinc.add(jtSal, gridBagConstraints);

        Ppie.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        Ppie.setMaximumSize(new java.awt.Dimension(320, 28));
        Ppie.setMinimumSize(new java.awt.Dimension(320, 28));
        Ppie.setPreferredSize(new java.awt.Dimension(320, 28));
        Ppie.setLayout(null);

        Baceptar.setText("Aceptar");
        Ppie.add(Baceptar);
        Baceptar.setBounds(3, 1, 110, 27);

        Bcancelar.setText("Cancelar");
        Ppie.add(Bcancelar);
        Bcancelar.setBounds(210, 0, 110, 27);
        Ppie.add(Bocul1);
        Bocul1.setBounds(100, 30, 2, 2);
        Ppie.add(Bocul2);
        Bocul2.setBounds(100, 30, 2, 2);

        cLabel7.setText("Tipo Despiece ");
        Ppie.add(cLabel7);
        cLabel7.setBounds(340, 2, 81, 17);
        Ppie.add(tid_codcopE);
        tid_codcopE.setBounds(430, 2, 39, 17);

        Bcopiar.setText("Copiar Sal.");
        Ppie.add(Bcopiar);
        Bcopiar.setBounds(480, 0, 80, 19);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 30);
        Pprinc.add(Ppie, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CButton Bcancelar;
    private gnu.chu.controles.CButton Bcopiar;
    private gnu.chu.controles.CButton Bocul;
    private gnu.chu.controles.CButton Bocul1;
    private gnu.chu.controles.CButton Bocul2;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Ppie;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CGridEditable jtEnt;
    private gnu.chu.controles.CGridEditable jtSal;
    private gnu.chu.camposdb.proPanel pro_codiE;
    private gnu.chu.camposdb.proPanel pro_codsE;
    private gnu.chu.controles.CTextField pro_nombE;
    private gnu.chu.controles.CTextField pro_nombsE;
    private gnu.chu.controles.CTextField tds_costoE;
    private gnu.chu.controles.CTextField tds_grupoE;
    private gnu.chu.controles.CTextField tds_unidE;
    private gnu.chu.controles.CComboBox tid_activE;
    private gnu.chu.controles.CCheckBox tid_agrupE;
    private gnu.chu.controles.CTextField tid_codcopE;
    private gnu.chu.controles.CTextField tid_codiE;
    private gnu.chu.controles.CTextField tid_fecaltE;
    private gnu.chu.controles.CTextField tid_feulmoE;
    private gnu.chu.controles.CTextField tid_nombE;
    private gnu.chu.controles.CCheckBox tid_usoequE;
    private gnu.chu.controles.CTextField usu_nombE;
    // End of variables declaration//GEN-END:variables

    /**
     * Comprueba si un producto es equivalente a otro.
     * Comprueba la tabla artiequiv
     * @param proCodini
     * @param proCodfin
     * @param dt
     * @return
     * @throws SQLException
     */
    public static boolean esEquivalente(int proCodini, int proCodfin,DatosTabla dt) throws SQLException
    {
        if (proCodini==proCodfin)
            return true;
        String s= "SELECT * FROM artiequiv where pro_codini = "+proCodini+" and pro_codfin = "+proCodfin;
        if (dt.select(s))
            return true;
        s="SELECT * FROM artiequiv where pro_codini = "+proCodfin+" and pro_codfin = "+proCodini;
        if (dt.select(s))
            return true;
        return false;
    }
     /**
     * Comprueba si un producto Fresco es equivalente a otro Congelado
     * Comprueba la tabla artiequiv y arteqcon
     * @param proCodFre Producto Fresco
     * @param proCodfin Producto Congelado
     * @param dt
     * @return true si son equivalentes.
     * @throws SQLException
     */
    public static boolean esEquivalenteCongelado(int proCodFre, int proCodCon,DatosTabla dt) throws SQLException
    {
        String s= "SELECT * FROM artequcon where pro_codini in  "+
              " (SELECT pro_codfin FROM artiequiv where pro_codini = "+proCodFre+
               " UNION SELECT pro_codini FROM artiequiv where pro_codfin = "+proCodFre+" UNION SELECT '"+ proCodFre+"')"+
                " and pro_codfin  in  "+
              " (SELECT pro_codfin FROM artiequiv where pro_codini = "+proCodCon+
               " UNION SELECT pro_codini FROM artiequiv where pro_codfin = "+proCodCon+" UNION SELECT '"+ proCodCon+"')";
        return dt.select(s);
    }
    /**
     * Comprueba si un tipo de despiece es tipo agrupacion
     * @param tidCodi 
     * @param dt
     * @return true si es tipo agrupacion. False si no lo es o no se encuentra el tipo desp.
     * @throws SQLException 
     */
    public static boolean isDespieceAgrup(int tidCodi, DatosTabla dt) throws SQLException
    {
        if (tidCodi==AUTO_DESPIECE)
            return true;
        String s= "SELECT * FROM tipodesp WHERE "+
              " tid_codi = "+tidCodi;
        if (! dt.select(s))
            return false;
        return dt.getInt("tid_agrup")!=0;
    }
    /**
     * Indica si un tipo de despiece permite usar productos equivalentes
     * @param tidCodi 
     * @param dt
     * @return true si es tipo agrupacion. False si no lo es o no se encuentra el tipo desp.
     * @throws SQLException 
     */
    public static boolean isArticEquiv(int tidCodi, DatosTabla dt) throws SQLException
    {
        String s= "SELECT tid_usoequ  FROM tipodesp WHERE "+
              " tid_codi = "+tidCodi;
        if (! dt.select(s))
            return false;
        return dt.getInt("tid_usoequ ")!=0;
    }
   
}
