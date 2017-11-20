package gnu.chu.anjelica.despiece;

import gnu.chu.Menu.*;
import gnu.chu.camposdb.*;
import gnu.chu.controles.*;
import gnu.chu.interfaces.*;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import javax.swing.BorderFactory;

/**
 *
 * <p>Título:   pdprvades
 *
 * <p>Descripción: Mantenimiento Productos Valorados para Despiece </p>
 * <p> Marca Precios FIJOS de compra y ventas para los productos
 * en una semana determinada. Utilizado en programa valoracion despieces y Precios Medios</p>
 * <p>Copyright: Copyright (c) 2005-2017
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
 * <p>Empresa: MISL</p>
 * @author chuchiP
 * @version 1.0
 */

public class pdprvades extends ventanaPad implements PAD
{
  int ejeNume, numSem;
  CPanel Pprinc = new CPanel();
  CPanel Pcabe = new CPanel();
  CGridEditable jt = new CGridEditable(6)
  {
    @Override
    public void cambiaColumna(int col,int colNueva, int row)
    {
      try
      {
        if (col == 0)
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
    public int cambiaLinea(int row, int col)
    {
      return cambiaLineaJT(row,col);
    }
  };

  String s;
  CTextField dpv_feciniE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel dpv_feciniL = new CLabel();
//  CButton Baceptar = new CButton();
//  CButton Bcancelar = new CButton();
  proPanel pro_codiE = new proPanel();
  CTextField pro_nombE = new CTextField();
  CTextField dpv_preciE= new CTextField(Types.DECIMAL,"###9.99");
  CTextField dpv_preoriE= new CTextField(Types.DECIMAL,"###9.99");
  CTextField dpv_prevalE= new CTextField(Types.DECIMAL,"###9.99");
  CTextField dpv_pretarE= new CTextField(Types.DECIMAL,"###9.99");
  CLabel cLabel2 = new CLabel();
  CTextField dpv_nusemE = new CTextField(Types.DECIMAL,"99");
  CLabel cLabel3 = new CLabel();
  CTextField eje_numeE = new CTextField(Types.DECIMAL,"9999");
  CButton Bocul = new CButton();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CButton BCopia = new CButton(Iconos.getImageIcon("copia"));
  
    public pdprvades(EntornoUsuario eu, Principal p)
    {
      EU = eu;
      vl = p.panel1;
      jf = p;
      eje = true;

      setTitulo("Mant. Productos Valorados para Despiece ");

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

    public pdprvades(gnu.chu.anjelica.menu p, EntornoUsuario eu)
    {

      EU = eu;
      vl = p.getLayeredPane();
      setTitulo("Mant. Productos Valorados para Despiece");
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
      this.setSize(new Dimension(482,493));
      this.setVersion("2017-10-29");

      strSql = "SELECT dpv_nusem,eje_nume FROM desproval " +
          " WHERE eje_nume = " + EU.ejercicio +
          " group by dpv_nusem,eje_nume" +
          " order by eje_nume,dpv_nusem";

      statusBar = new StatusBar(this);
      conecta();
      nav = new navegador(this, dtCons, false, navegador.NORMAL);
      Pprinc.setLayout(gridBagLayout1);
      Pcabe.setBorder(BorderFactory.createRaisedBevelBorder());
      Pcabe.setMaximumSize(new Dimension(351, 27));
      Pcabe.setMinimumSize(new Dimension(351, 27));
      Pcabe.setPreferredSize(new Dimension(351, 27));
      Pcabe.setLayout(null);

      ArrayList cabecera = new ArrayList();
      cabecera.add("Codigo"); // 0 -- Codigo
      cabecera.add("Nombre"); //1 -- Nombre
      cabecera.add("Pr.Fin"); // 2 -- Precio Final
      cabecera.add("Pr.Ori"); // 3 -- Precio Orig.
      cabecera.add("Pr.Val"); // 4 -- Precio Val.
      cabecera.add("Pr.Tar"); // 5 -- Precio Tarifa
      jt.setCabecera(cabecera);
      jt.setAnchoColumna(new int[]{46, 283, 60,60,60,60});
      jt.setAlinearColumna(new int[]   {2, 0, 2,2,2,2});
      pro_codiE.iniciar(dtStat, this, vl, EU);
      pro_codiE.setProNomb(null);

      pro_nombE.setEnabled(false);
      ArrayList v = new ArrayList();
      v.add(pro_codiE.getFieldProCodi());
      v.add(pro_nombE);
      v.add(dpv_preciE);
      v.add(dpv_preoriE);
      v.add(dpv_prevalE);
      v.add(dpv_pretarE);
      jt.setCampos(v);
      jt.setMaximumSize(new Dimension(467, 400));
      jt.setMinimumSize(new Dimension(467, 400));
      jt.setPreferredSize(new Dimension(467, 400));

      jt.setConfigurar("gnu.chu.anjelica.depiece.pdprvades", EU, dtStat);

      iniciar(this);
      Baceptar.setMaximumSize(new Dimension(100, 26));
      Baceptar.setMinimumSize(new Dimension(100, 26));
      Baceptar.setPreferredSize(new Dimension(100, 26));
      Bcancelar.setMaximumSize(new Dimension(100, 26));
      Bcancelar.setMinimumSize(new Dimension(100, 26));
      Bcancelar.setPreferredSize(new Dimension(100, 26));
      
      dpv_feciniE.setEnabled(false);
      dpv_feciniL.setText("Fecha");
      dpv_feciniL.setBounds(new Rectangle(200, 5, 35, 15));
      dpv_feciniE.setBounds(new Rectangle(235, 5, 75, 15));
      BCopia.setBounds(new Rectangle(315, 5, 18, 18));
      cLabel2.setText("Semana");
      cLabel2.setBounds(new Rectangle(104, 5, 45, 15));
      dpv_nusemE.setBounds(new Rectangle(150, 5, 24, 15));
      cLabel3.setText("Ejercicio");
      cLabel3.setBounds(new Rectangle(7, 5, 52, 15));
      eje_numeE.setBounds(new Rectangle(56, 5, 41, 15));
      Bocul.setBounds(new Rectangle(350, 7, 1, 1));
      Pprinc.setInputVerifier(null);
      this.getContentPane().add(nav, BorderLayout.NORTH);
      this.getContentPane().add(statusBar, BorderLayout.SOUTH);
      Pcabe.add(dpv_feciniE, null);
      Pcabe.add(dpv_feciniL, null);
      Pcabe.add(cLabel3, null);
      Pcabe.add(eje_numeE, null);
      Pcabe.add(cLabel2, null);
      Pcabe.add(dpv_nusemE, null);
      Pcabe.add(Bocul, null);
      Pcabe.add(BCopia, null);
      
      Pprinc.add(Bcancelar,     new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 70), 0, 0));
      Pprinc.add(Baceptar,   new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 70, 0, 0), 0, 0));
      Pprinc.add(jt,   new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 0, 2), 0, 0));
      Pprinc.add(Pcabe,   new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 0, 0));
      this.getContentPane().add(Pprinc, BorderLayout.CENTER);
    }

  @Override
    public void iniciarVentana() throws Exception
    {
      dpv_nusemE.setColumnaAlias("dpv_nusem");
      eje_numeE.setColumnaAlias("eje_nume");
      activarEventos();
      activar(false);
      jt.setNumRegCargar(0);
      if (!dtCons.getNOREG())
        dtCons.last();
      verDatos();
      nav.requestFocus();
      Pprinc.setDefButton(Baceptar);
      Pprinc.setEscButton(Bcancelar);
    }

    void activarEventos()
    {
      BCopia.addActionListener(new ActionListener()
      {
          @Override
          public void actionPerformed(ActionEvent e) {
              copiaTarifa();
          }
      });
      jt.tableView.addMouseListener(new MouseAdapter()
      {
        @Override
        public void mouseClicked(MouseEvent e)
        {
          if (jt.isVacio() || jt.isEnabled() == false)
            return;
          if (jt.getSelectedColumn() != 2)
            return;
          jt.setValor(jt.getValBoolean(2) ? "N" : "S");
        }
      });
//      dpv_nusemE.addKeyListener(new KeyAdapter()
//      { 
//        @Override
//        public void keyPressed(KeyEvent e)
//        {
//             if ( e.getKeyCode()==  KeyEvent.VK_F1)
//             {
//                  java.util.Date fecha = mensajes.getFechaCalendario(Formatear.getDateAct());
//                  GregorianCalendar gc = new GregorianCalendar();
//                  gc.setTime(fecha);
//                  dpv_nusemE.setValorInt(gc.get(GregorianCalendar.WEEK_OF_YEAR));
//                  eje_numeE.setValorInt(gc.get(GregorianCalendar.YEAR));
//                  actFecha();
//             }
//        }
//      });
     
      dpv_nusemE.addFocusListener(new FocusAdapter()
      {
        @Override
        public void focusLost(FocusEvent e)
        {
          actFecha();
        }
      });
      Bocul.addFocusListener(new FocusAdapter()
      {
        @Override
        public void focusGained(FocusEvent e)
        {
          irGrid();
        }
      });
    }
    
    void copiaTarifa()
    {
      try
      {
          int  nusem=dpv_nusemE.getValorInt()==1?52:dpv_nusemE.getValorInt()-1;
          int  ejNume=eje_numeE.getValorInt()- (nusem==52?1:0);
          s = "SELECT d.pro_codi,a.pro_nomb,d.dpv_preci,dpv_preori,dpv_preval,dpv_pretar " +
            " FROM desproval as d,v_articulo as a " +
            " WHERE dpv_nusem = " + nusem +
            " and eje_nume = " + ejNume +
            " and a.pro_codi = d.pro_codi order by pro_codi";
          dtCon1.select(s);
          jt.setDatos(dtCon1);
          jt.requestFocusInicio();
          irGrid();
      } catch (SQLException ex)
      {
         Error("Error al copiar tarifa",ex);
      }
    }
    void actFecha()
    {
      if (dpv_nusemE.getValorInt() == 0 || eje_numeE.getValorInt() == 0)
        return;
      try
      {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(Formatear.getDate("01-01-"+eje_numeE.getValorInt(),"dd-MM-yyyy"));
        gc.set(GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.MONDAY);
        gc.set(GregorianCalendar.WEEK_OF_YEAR, dpv_nusemE.getValorInt());            
        dpv_feciniE.setText(Formatear.getFechaVer(gc.getTime()));
      }
      catch (Exception k)
      {
        Error("Error al calcular fecha",k);
      }
    }
    void irGrid()
    {
      if (nav.pulsado!=navegador.ADDNEW && nav.pulsado!=navegador.EDIT)
        return;
      if (eje_numeE.getValorInt() == 0 || dpv_nusemE.getValorInt() == 0)
      {
        eje_numeE.requestFocus();
        return;
      }
      try {
        if (nav.pulsado == navegador.ADDNEW)
        {
          s = "SELECT * FROM desproval WHERE eje_nume = " + eje_numeE.getValorInt() +
              " and dpv_nusem = " + dpv_nusemE.getValorInt();
          if (dtCon1.select(s))
          {
            verDatLin(eje_numeE.getValorInt(), dpv_nusemE.getValorInt());
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

    void guardaDatos(int ejerc,int nusem)
    {
      String s;
      try {

        borDatos(ejerc,nusem);

        int nRow = jt.getRowCount();
        dtAdd.addNew("desproval");
        for (int n = 0; n < nRow; n++)
        {
          if ( jt.getValorInt(n,0)==0 || (jt.getValorDec(n,2)==0 && jt.getValorDec(n,2)==3))
            continue;
          dtAdd.addNew();
       //   dtAdd.setDato("emp_codi",EU.em_cod);
          dtAdd.setDato("eje_nume",eje_numeE.getValorInt());
          dtAdd.setDato("dpv_nusem",dpv_nusemE.getValorInt());
          dtAdd.setDato("pro_codi",jt.getValorInt(n,0));
          dtAdd.setDato("dpv_preci",jt.getValorDec(n,2));
          dtAdd.setDato("dpv_preori",jt.getValorDec(n,3));
          dtAdd.setDato("dpv_preval",jt.getValorDec(n,4));
          dtAdd.setDato("dpv_pretar",jt.getValorDec(n,5));
          dtAdd.update(stUp);
        }
        ctUp.commit();
        mensajeErr("Datos ... Guardados");
      } catch (Exception k)
      {
        Error("Error en La insercion de Referencias",k);
      }
    }
    /**
     * Borrar datos
     * @param ejerc Ejercicio
     * @param nusem Numero Semana
     * @throws SQLException Error base de Datos
     */
    void  borDatos(int ejerc,int nusem) throws SQLException
    {
      s = "delete from desproval WHERE eje_nume = " + ejerc +
          " and dpv_nusem  = " + nusem;
      stUp.executeUpdate(s);
    }
    void verDatos()
    {
      try
      {
        if (dtCons.getNOREG())
          return;
        dpv_nusemE.setValorDec(dtCons.getInt("dpv_nusem"));
        eje_numeE.setValorDec(dtCons.getInt("eje_nume"));
        actFecha();
        verDatLin(eje_numeE.getValorInt(), dpv_nusemE.getValorInt());
      } catch (Exception k)
      {
        Error("Error al ver datos",k);
      }
    }

    void verDatLin(int ejerc,int nusem) throws Exception
    {
      s = "SELECT d.pro_codi,a.pro_nomb,d.dpv_preci,dpv_preori,dpv_preval,dpv_pretar " +
          " FROM desproval as d,v_articulo as a " +
          " WHERE dpv_nusem = " + nusem +
          " and eje_nume = " + ejerc +
          " and a.pro_codi = d.pro_codi order by pro_codi";
      dtCon1.select(s);
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
  public void PADPrimero() { verDatos();
  }
  @Override
  public void PADAnterior() { verDatos();
  }
  @Override
  public void PADSiguiente() {
    verDatos();
  }
  @Override
  public void PADUltimo() { verDatos();
  }
  @Override
  public void PADQuery() {
    activar(navegador.QUERY, true);
    Pcabe.setQuery(true);
    Pcabe.resetTexto();
    eje_numeE.setText(""+EU.ejercicio);
    dpv_nusemE.requestFocus();

  }
@Override
  public void ej_query1() {
    ArrayList v=new ArrayList();
    v.add(dpv_nusemE.getStrQuery());
    v.add(eje_numeE.getStrQuery());
    s="SELECT dpv_nusem,eje_nume FROM desproval ";
    s=creaWhere(s,v,true);
    s+=" group by dpv_nusem,eje_nume"+
        " order by eje_nume,dpv_nusem";
//    debug(s);
    try
    {
      if (!dtCons.select(s))
      {
        mensaje("");
        mensajeErr("No encontrados Registros para estos criterios");
        rgSelect();
        dtCons.last();
        activaTodo();
        return;
      }
      mensaje("");
      strSql = s;
      activaTodo();
      rgSelect();
      dtCons.last();
      verDatos();
      mensajeErr("Nuevos regisgtros selecionados");
    }
    catch (Exception ex)
    {
      fatalError("Error al buscar Inventarios: ", ex);
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
    ejeNume=eje_numeE.getValorInt();
    numSem=dpv_nusemE.getValorInt();
    activar(true);
    dpv_nusemE.requestFocus();
  }
  @Override
  public void ej_edit1() {
    jt.actualizarGrid();
    if (cambiaLineaJT(jt.getSelectedRow(),0)>=0)
    {
      jt.requestFocusSelected();
      return;
    }
    guardaDatos(ejeNume,numSem);
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
    eje_numeE.setValorDec(EU.ejercicio);
    dpv_nusemE.requestFocus();
    mensaje("Insertando ....");

  }
  
  @Override
  public void ej_addnew1() {
    jt.actualizarGrid();

    if (cambiaLineaJT(jt.getSelectedRow(),0)>=0)
    {
      jt.requestFocusSelected();
      return;
    }
    guardaDatos(eje_numeE.getValorInt(),dpv_nusemE.getValorInt());
    activaTodo();
    verDatos();
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
      borDatos(eje_numeE.getValorInt(), dpv_nusemE.getValorInt());
      ctUp.commit();
    } catch (Exception k)
    {
      Error("Error al borrar datos",k);
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

  int cambiaLineaJT(int row, int col)
  {
    if (pro_codiE.getValorInt()==0)
      return -1; // No hay producto ... paso
    try {
      if (!pro_codiE.controla(false,false))
      {
        jt.setValor("ARTICULO NO VALIDO",row,1);
        pro_nombE.setText("ARTICULO NO VALIDO");
        mensajeErr(pro_codiE.getMsgError());
        return 0;
      }
      jt.setValor(pro_codiE.getNombArtUltimo(), row,1);
      if (dpv_preciE.getValorDec() == 0 && dpv_preoriE.getValorDec() ==0  && dpv_prevalE.getValorDec() ==0 
           && dpv_pretarE.getValorDec() ==0 )
      {
        mensajeErr("Introduzca un precio de Tarifa");
        return 1;
      }
    } catch (Exception k)
    {
      Error("ERROR AL controlar Linea del Grid",k);
      return 0;
    }
    return -1;
  }
  /**
   * Devuelve fecha inicio de costos para una fecha. 
   * Teniendo en cuenta que el jueves empieza la semana.
   * @param fecha
   * @return 
   */
  public static Date getFechaCosto(Date fecha)
  { 
     GregorianCalendar gc=new GregorianCalendar();
     gc.setTime(fecha);
     gc.set(GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.MONDAY);
     return gc.getTime();
//        dpv_feciniE.setText(Formatear.getFechaVer(gc.getTime()));
//     int diaSemana=gc.get(GregorianCalendar.DAY_OF_WEEK); 
//     if (diaSemana>GregorianCalendar.WEDNESDAY)
//        return Formatear.sumaDiasDate(fecha, 9-diaSemana);    // lunes de Siguiente semana
//     else
//        return Formatear.sumaDiasDate(fecha, (diaSemana-2)*-1); // Lunes de semana en curso
  }
  /**
   * Devuelve la semana y el año para una fecha dada en Valoracion Despieces
   * @param fecha
   * @return  Dimension. Width sera el año. heigh sera la semana
   */
  public static Dimension getAnoySemana(Date fecha)
  {
      GregorianCalendar gc=new GregorianCalendar();
      gc.setFirstDayOfWeek(1);
      gc.setTime(fecha);
      int semana = gc.get(GregorianCalendar.WEEK_OF_YEAR);
      int dia=gc.get(GregorianCalendar.DAY_OF_WEEK); // Dia de la semana
  
      int ano= gc.get(GregorianCalendar.YEAR);
//      if (semana>1)
//          semana--;
      return new Dimension(ano,semana);
  }
   public static double getPrecioOrigen(DatosTabla dt, int proCodi,Date fecha)  throws SQLException
   {
      Dimension d=getAnoySemana(fecha); 
    
      return getPrecioOrigen(dt,proCodi,d.width, d.height);
   }
  /**
   * Devuelve el precio fijo de despiece  para un producto en la fecha indicada.
   * -1 Si no existe precio .
   * @param dt
   * @param proCodi
   * @param fecha
   * @return Precio fijo en el campo 1. 1 Si es forzado, 0 si no es forzado
   * @throws SQLException 
   */
  public static double[] getPrecioFinal(DatosTabla dt, int proCodi,Date fecha)  throws SQLException
  {
      Dimension d=getAnoySemana(fecha); 
    
      return getPrecioFinal(dt,proCodi,d.width, d.height);     
  }
  /**
   * Devuelve el precio final para un producto en una semana
   * @param dt
   * @param proCodi
   * @param ejeNume
   * @param semCodi
   * @return
   * @throws SQLException 
   */
  public static double[] getPrecioFinal(DatosTabla dt, int proCodi,int ejeNume,int semCodi) throws SQLException
  {
      String sql="SELECT dpv_preci,dpv_pretar FROM desproval " +
          " WHERE eje_nume = " + ejeNume +
            " and dpv_nusem = "+semCodi+
            " and pro_codi = "+proCodi;
      if (! dt.select(sql))
          return new double[]{-1,0};
      if (dt.getDouble("dpv_preci")>0)
          return  new double[]{dt.getDouble("dpv_preci"),1};
       return new double[]{dt.getDouble("dpv_pretar"),0};  
  }
  
  public static double getPrecioOrigen(DatosTabla dt, int proCodi,int ejeNume,int semCodi) throws SQLException
  {
      String sql="SELECT dpv_preori FROM desproval " +
          " WHERE eje_nume = " + ejeNume +
            " and dpv_nusem = "+semCodi+
            " and pro_codi = "+proCodi;
      if (! dt.select(sql))
          return -1;
      return dt.getDouble("dpv_preori");
  }
   public static double getPrecioValorar(DatosTabla dt, int proCodi,int ejeNume,int semCodi) throws SQLException
  {
      String sql="SELECT dpv_preval FROM desproval " +
          " WHERE eje_nume = " + ejeNume +
            " and dpv_nusem = "+semCodi+
            " and pro_codi = "+proCodi;
      if (! dt.select(sql))
          return -1;
      return dt.getDouble("dpv_preval");
  }
   public static double getPrecioValorar(DatosTabla dt, int proCodi,Date fecha)  throws SQLException
   {
      GregorianCalendar gc=new GregorianCalendar();    
      gc.setTime(fecha);
      int semana = gc.get(GregorianCalendar.WEEK_OF_YEAR);          
      int ano= gc.get(GregorianCalendar.YEAR);
      return getPrecioValorar(dt,proCodi,ano, semana);
   }
}
