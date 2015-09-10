/**
 *
 * <p>Titulo: MantTarifa </p>
 * <p>Descripción: Mantenimiento Tarifas de Ventas</p>
 * <p>Copyright: Copyright (c) 2005-2015
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
 * @author chuchiP
 *
 */ 

package gnu.chu.anjelica.pad;

import gnu.chu.Menu.Principal;
import gnu.chu.controles.StatusBar;
import gnu.chu.interfaces.PAD;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.navegador;
import gnu.chu.utilidades.ventanaPad;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.print.PrinterException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;


public class MantTarifa extends ventanaPad implements PAD
{
  String s;
  String fecini,tipo;
  boolean ARG_MODCONSULTA=false;
  public MantTarifa(EntornoUsuario eu, Principal p)
  {
    this(eu,p,null);
  }

  public MantTarifa(EntornoUsuario eu, Principal p, Hashtable ht)
  {
      EU = eu;
      vl = p.panel1;
      jf = p;
      eje = true;

      setTitulo("Mant. Tarifas de Productos");

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

    public MantTarifa(gnu.chu.anjelica.menu p, EntornoUsuario eu)
    {

      EU = eu;
      vl = p.getLayeredPane();
      setTitulo("Mant. Tarifas de Productos");
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
      this.setVersion("2015-09-08" + (ARG_MODCONSULTA ? " SOLO LECTURA" : ""));
      statusBar = new StatusBar(this);
      nav = new navegador(this,dtCons,false);
      iniciarFrame();
      strSql = "SELECT tar_fecini,tar_fecfin,tar_codi FROM tarifa"+
          " group by tar_fecini,tar_fecfin,tar_codi" +
          " order by tar_fecini,tar_codi";
      this.getContentPane().add(nav, BorderLayout.NORTH);
      this.getContentPane().add(statusBar, BorderLayout.SOUTH);
      this.setPad(this);
      navActivarAll();
      dtCons.setLanzaDBCambio(false);
      initComponents();
      iniciarBotones(Baceptar, Bcancelar);
      this.setSize(new Dimension(582,522));
      conecta();
      activar(false);
      if (ARG_MODCONSULTA)
      {
        nav.removeBoton(navegador.ADDNEW);
        nav.removeBoton(navegador.EDIT);
        nav.removeBoton(navegador.DELETE);
      }
      statusBar.add(Bimpri, new GridBagConstraints(9, 0, 1, 2, 0.0, 0.0
                            , GridBagConstraints.EAST,
                            GridBagConstraints.VERTICAL,
                            new Insets(0, 5, 0, 0), 0, 0));

    }
    @Override
    public void iniciarVentana() throws Exception
    {
      tar_feciniE.setColumnaAlias("tar_fecini");
      tar_fecfinE.setColumnaAlias("tar_fecfin");
      tar_codiE.setColumnaAlias("tar_codi");
      activarEventos();
      activar(false);
//      verDatos();
      nav.requestFocus();
      Pprinc.setDefButton(Baceptar);
//      Pprinc.setEscButton(Bcancelar);
    }

    void activarEventos()
    {
        Bimpri.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                Bimpri_actionPerformed();
            }
         });

      tar_feciniE.addFocusListener(new FocusAdapter()
      {
            @Override
        public void focusLost(FocusEvent e)
        {
          if (tar_feciniE.isQuery()|| tar_feciniE.getError())
            return;
          try {
            if (!tar_feciniE.isNull())
            {
              tar_nusemE.setValorDec(actFecha(tar_feciniE.getDate()));
              tar_fecfinE.setText( Formatear.sumaDias(tar_feciniE.getText(),"dd-MM-yyyy",6));
            }
          } catch (Exception k)
          {
            Error("Error al Comprobar Fecha",k);
          }
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

    int actFecha(java.util.Date fecha)
    {
      GregorianCalendar gc = new GregorianCalendar();
      gc.setTime(fecha);
      return gc.get(GregorianCalendar.WEEK_OF_YEAR);
    }
    void Bimpri_actionPerformed()
   {
     if (dtCons.getNOREG())
       return;
     try {
       if (dtCons.getNOREG())
       {
         mensajeErr("NO HAY NINGUN PRODUCTO SELECIONADO");
         return;
       }

       HashMap mp = new HashMap();
      
       mp.put("tar_feciniP", tar_feciniE.getDate());
       mp.put("tar_fecfinP", tar_fecfinE.getDate());
       mp.put("impRefer", opImpRef.isSelected());
       mp.put("tar_nombP", tar_codiE.getText()); 
       mp.put("logo", Iconos.getPathIcon() + "logotipo.jpg"); 
       JasperReport jr;
       jr = gnu.chu.print.util.getJasperReport(EU,"tarifa");
   
        s="select * from tarifa  "+
            " where tar_codi = "+tar_codiE.getValorInt()+
            " and tar_fecini = TO_DATE('"+tar_feciniE.getText()+"','dd-MM-yyyy') "+
            " order by tar_linea";
       ResultSet rs;

       rs=dtCon1.getStatement().executeQuery(dtCon1.parseaSql(s));

       JasperPrint jp = JasperFillManager.fillReport(jr, mp,
           new JRResultSetDataSource(rs));
       gnu.chu.print.util.printJasper(jp, EU);
     }
     catch (JRException | ParseException | SQLException | PrinterException k)
     {
       Error("Error al imprimir Tarifa", k);
     }
   }
    void irGrid()
    {
      if (nav.pulsado!=navegador.ADDNEW && nav.pulsado!=navegador.EDIT)
        return;
      
      if (tar_feciniE.isNull() || tar_fecfinE.isNull())
      {
        tar_feciniE.requestFocus();
        return;
      }
      try {
        if (nav.pulsado == navegador.ADDNEW)
        {
          s = "SELECT * FROM tarifa WHERE "+
              " tar_fecini = TO_DATE('"+tar_feciniE.getText()+"','dd-MM-yyyy') "+
              " and tar_codi = " + tar_codiE.getValor();
          if (dtCon1.select(s))
          {
            verDatLin(tar_feciniE.getText(), tar_codiE.getValor(),tar_fecfinE.getText(),0);
            jt.cargaTodo();
            fecini=tar_feciniE.getText();
            tipo=tar_codiE.getValor();
            nav.pulsado = navegador.EDIT;
            mensaje("Editando ... ");
          }
        }

        Pcabe.setEnabled(false);
        Baceptar.setEnabled(true);
        if (nav.pulsado==navegador.ADDNEW &&  !tar_fecopE.isNull())
        {// Copiar los datos de la anterior TARIFA
          s = "SELECT * FROM tarifa WHERE " +
              " tar_fecini = TO_DATE('" + tar_fecopE.getText() +
              "','dd-MM-yyyy') " +
              " and tar_codi = " + tar_copiaE.getValor();
          if (dtCon1.select(s))
          {
            verDatLin(tar_fecopE.getText(), tar_copiaE.getValor(),
                      tar_fecfinE.getText(),tar_incremE.getValorDec());
            jt.cargaTodo();
          }
        }
        jt.setEnabled(true);
        jt.requestFocusInicio();
        pro_codartE.resetCambio();
      } catch (Exception k)
      {
        Error("ERROR al ir al Grid",k);
      }
    }

    void guardaDatos(String fecha,String tipo)
    {
      try {

        borDatos(fecha,tipo);

        int nRow = jt.getRowCount();
        int grupo=0;
        int animal=0;
        dtAdd.addNew("tarifa");
        for (int n = 0; n < nRow; n++)
        {
          if ( jt.getValString(n,0).trim().equals(""))
          {
              if (jt.getValString(n,1).trim().equals(""))
                  continue;
              else
                  grupo++;
          }
          if ( jt.getValString(n,0).trim().equals("X") && !jt.getValString(n,1).trim().equals(""))
              animal++;
//          if (jt.getValorDec(n,2)==0)
//            continue;
          dtAdd.addNew();
          dtAdd.setDato("tar_fecini",tar_feciniE.getText(),"dd-MM-yyyy");
          dtAdd.setDato("tar_fecfin",tar_fecfinE.getText(),"dd-MM-yyyy");
          dtAdd.setDato("tar_linea",n);
          dtAdd.setDato("tar_codi",tar_codiE.getValor());
          dtAdd.setDato("pro_codart",jt.getValString(n,0));
          dtAdd.setDato("pro_nomb",jt.getValString(n,1));
          dtAdd.setDato("tar_preci",jt.getValorDec(n,2));
          dtAdd.setDato("tar_comen",jt.getValString(n,3));
          dtAdd.setDato("tar_grupo",grupo);
          dtAdd.setDato("tar_tipo",animal);
          dtAdd.update(stUp);
        }
        ctUp.commit();
        mensajeErr("Datos ... Guardados");
      } catch (Exception k)
      {
        Error("Error en La insercion de Referencias",k);
        return;
      }
    }

    void  borDatos(String fecha,String tipo) throws SQLException,java.text.ParseException
    {
      s = "DELETE FROM tarifa " +
          " WHERE tar_fecini = TO_DATE('" + fecha + "','dd-MM-yyyy') " +
          " AND tar_codi = " + tipo  ;
      stUp.executeUpdate(dtAdd.parseaSql(s));
    }
    void verDatos()
    {
      try
      {
        if (dtCons.getNOREG())
          return;
        tar_feciniE.setText(dtCons.getFecha("tar_fecini","dd-MM-yyyy"));
        tar_fecfinE.setText(dtCons.getFecha("tar_fecfin","dd-MM-yyyy"));

        tar_codiE.setValor(dtCons.getString("tar_codi"));
        verDatLin(tar_feciniE.getText(),tar_codiE.getValor(),tar_fecfinE.getText(),0);
      } catch (Exception k)
      {
        Error("Error al ver datos",k);
      }
    }

    void verDatLin(String fecha,String tipo,String fecfin,double increm) throws Exception
    {
      s = "SELECT pro_codart,pro_nomb,tar_preci +("+ increm +"),tar_comen " +
          " FROM tarifa " +
          " WHERE tar_fecini = TO_DATE('"+fecha+"','dd-MM-yyyy') "+
          " AND tar_codi = "+tipo+
          " order by tar_linea";
      if (jt.isEnabled())
        jt.setEnabled(false);
      if (! dtCon1.select(s))
      {
        mensajeErr("Registro BORRADO");
        tar_fecfinE.resetTexto();
        tar_nusemE.resetTexto();
        jt.removeAllDatos();
        return;
      }
      tar_fecfinE.setText(fecfin);
      tar_nusemE.setValorDec(actFecha(tar_feciniE.getDate()));

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
        tar_feciniE.requestFocus();

    }

    @Override
  public void ej_query1() {
    Baceptar.setEnabled(false);
    ArrayList v=new ArrayList();
    v.add(tar_feciniE.getStrQuery());
    v.add(tar_fecfinE.getStrQuery());
    v.add(tar_codiE.getStrQuery());
    Pcabe.setQuery(false);
    s="SELECT tar_fecini,tar_fecfin,tar_codi FROM tarifa";
    s=creaWhere(s,v);
    s+=" group by tar_fecini,tar_fecfin,tar_codi"+
        " order by tar_fecini,tar_codi";
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
//      verDatos();
      mensajeErr("Nuevos registros selecionados");
    }
    catch (Exception ex)
    {
      fatalError("Error al buscar Tarifas: ", ex);
    }

  }

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
    fecini=tar_feciniE.getText();
    tipo=tar_codiE.getValor();
    activar(true);
    tar_fecopE.setEnabled(false);
    jt.requestFocusInicioLater();
//    jt.requestFocusFinal();
//    jt.mueveSigLinea(0);
  }
  public void ej_edit1() {
      jt.procesaAllFoco();

    if (cambiaLineaJT()>=0)
    {
      jt.requestFocusSelected();
      return;
    }
    int row=checkRepetido();
    if (row>=0)
    {
        jt.requestFocus(row,0);
        return;
    }
    guardaDatos(fecini,tipo);
    activaTodo();
    verDatos();
    mensaje("");
  }
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
    tar_fecopE.setEnabled(true);
//    eje_numeE.setValorDec(EU.ejercicio);
    tar_feciniE.requestFocus();
    mensaje("Insertando ....");
  }

  public void ej_addnew1() {
    jt.procesaAllFoco();

    if (cambiaLineaJT()>=0)
    {
      jt.requestFocusSelected();
      return;
    }
    int row=checkRepetido();
    if (row>=0)
    {
        jt.requestFocus(row,0);
        return;
    }
    guardaDatos(tar_feciniE.getText(),tar_codiE.getValor());
    activaTodo();
    verDatos();
    mensaje("");
  }
  int checkRepetido()
  {
      int nRow=jt.getRowCount();
      for (int n=0;n<nRow-1;n++)
      {
          if (jt.getValString(n,0).equals("") || jt.getValString(n,0).equals("X"))
                  continue;
          for (int n1=n+1;n1<nRow;n1++)
          {
             
              if (jt.getValString(n,0).equals(jt.getValString(n1,0)))
              {
                  mensajeErr("Producto: "+jt.getValString(n,0)+" Ya existe en linea "+n);
                  return n1;
              }
          }
      }
      return -1;
  }
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
  public void ej_delete1() {
    try
    {
      borDatos(tar_feciniE.getText(), tar_codiE.getValor());
      ctUp.commit();
      rgSelect();
    } catch (Exception k)
    {
      Error("Error al borrar datos",k);
    }
    activaTodo();
   
    verDatos();
    mensaje("");
    mensajeErr("Datos .... Borrados");

  }
  public void canc_delete() {
    activaTodo();
    verDatos();
    mensaje("");
    mensajeErr("Borrado de Registro ... ANULADO");
  }

  int cambiaLineaJT()
  {
    if (pro_codartE.isNull() || pro_codartE.getText().equals("X"))
      return -1; // No hay producto o es de tipo... paso
    try {

      
      if (!pro_codartE.controla(false))
      {
        mensajeErr(pro_codartE.getMsgError());
        return 0;
      }
//      if (tar_preciE.getValorDec() == 0)
//      {
//        mensajeErr("Introduzca un precio de Tarifa");
//        return 2    ;
//      }
    } catch (Exception k)
    {
      Error("ERROR AL controlar Linea del Grid",k);
      return 0;
    }
    return -1;
  }
    @Override
  public void rgSelect() throws SQLException
  {
    super.rgSelect();
    if (! dtCons.getNOREG())
    {
      dtCons.last();
      nav.setEnabled(navegador.ULTIMO,false);
      nav.setEnabled(navegador.SIGUIENTE,false);
    }
    verDatos();
  }
  
  @Override
  public void afterConecta() throws SQLException, java.text.ParseException
  {
    s = "SELECT tar_codi,tar_nomb FROM tipotari WHERE tar_codori = 0" +
        " ORDER BY tar_codi ";
    if (dtStat.select(s))
    {
      do
      {
        tar_codiE.addItem(dtStat.getString("tar_nomb"),
                          dtStat.getString("tar_codi"));
        tar_copiaE.addItem(dtStat.getString("tar_nomb"),
                          dtStat.getString("tar_codi"));
      }
      while (dtStat.next());
    }
    pro_codartE.iniciar(dtStat, this, vl, EU);

    pro_codartE.setProNomb(null);
  }
  public static double getPrecTar(DatosTabla dt,int proCodi, int tarCodi,java.util.Date fecAlb) throws SQLException
 {
      return getPrecTar(dt,proCodi, tarCodi, Formatear.getFechaVer(fecAlb));
  }
  /**
   * Devuelve el precio de tarifa para un producto y tarifa dada, en una fecha.
   * @param dt DatosTabla para la conexion a la DB
   * @param proCodi int Codigo de Producto
   * @param tarCodi int Codigo de Tarifa
   * @param fecAlb String Fecha de Albaran (en formato dd-MM-yyyy)
   * @throws SQLException Error al acceder a la DB
   * @return double Precio de Tarifa. 0 Si no encuentra tarifa para las condiciones.
   */
 public static double getPrecTar(DatosTabla dt,int proCodi, int tarCodi,String fecAlb) throws SQLException
 {
   String s = " SELECT * FROM tipotari WHERE tar_codi = " + tarCodi;
   if (dt.select(s))
   {
     double tarIncPre = dt.getDouble("tar_incpre");

     s = " SELECT tar_preci,tar_fecini " +
         " FROM tarifa as t,v_articulo as ar where pro_codi = " + proCodi +
         " and ar.pro_codart=t.pro_codart "+
         " and tar_codi = " + (dt.getInt("tar_codori") == 0 ? dt.getInt("tar_codi") :
          dt.getInt("tar_codori")) +
         " AND tar_fecini <=  TO_DATE('" + fecAlb + "','dd-MM-yyyy')" +
         " AND tar_fecfin >=  TO_DATE('" + fecAlb + "','dd-MM-yyyy')" +
         " order by tar_fecini";
     if (dt.select(s))
       return dt.getDouble("tar_preci", true) + tarIncPre;
     else
       return 0;
   }
   return 0;
 }
 /**
  * Busca si una tarifa es de Costo.
  * @param dt DatosTabla a utilizar
  * @param tarCodi Codigo de Tarifa
  * @return true si es una tarifa de costo, false si no encuentra la tarifa o no es de costo.
  * @throws java.sql.SQLException
  */
 public static boolean isTarifaCosto(DatosTabla dt, int tarCodi) throws SQLException
 {
      String s = " SELECT tar_tipo FROM tipotari WHERE tar_codi = " + tarCodi;
      if (!dt.select(s))
          return false;
      return dt.getString("tar_tipo").equals("C");
 }
 /**
  * Devuelve el nombre de una tarifa
  * @param dt datosTabla para acceder a la DB
  * @param tarCodi codigo de tarifa
  * @return Nombre de tarifa. Null SI No existe
  * @throws SQLException Error al acceder a la DB
  */
 public static String getTarNomb(DatosTabla dt, int tarCodi) throws SQLException
 {
     String s= "SELECT tar_nomb FROM tipotari WHERE tar_codi = " +tarCodi;
     if (!dt.select(s))
         return null;
     return dt.getString("tar_nomb");
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

        pro_nombE = new gnu.chu.controles.CTextField();
        tar_comenG = new gnu.chu.controles.CTextField(Types.CHAR,"X",150);
        tar_preciE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9.99");
        pro_codartE = new gnu.chu.camposdb.proPanel();
        pro_codartE.setUsaCodigoArticulo(true);
        pro_codartE.setText("");
        Bimpri = new gnu.chu.controles.CButton(Iconos.getImageIcon("print"));
        Pprinc = new gnu.chu.controles.CPanel();
        Pcabe = new gnu.chu.controles.CPanel();
        cLabel5 = new gnu.chu.controles.CLabel();
        tar_feciniE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel6 = new gnu.chu.controles.CLabel();
        tar_fecfinE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel1 = new gnu.chu.controles.CLabel();
        tar_nusemE = new gnu.chu.controles.CTextField(Types.DECIMAL,"99");
        cLabel2 = new gnu.chu.controles.CLabel();
        tar_codiE = new gnu.chu.controles.CComboBox();
        cPanel1 = new gnu.chu.controles.CPanel();
        cLabel3 = new gnu.chu.controles.CLabel();
        tar_fecopE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel4 = new gnu.chu.controles.CLabel();
        tar_copiaE = new gnu.chu.controles.CComboBox();
        cLabel7 = new gnu.chu.controles.CLabel();
        tar_incremE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--9.99");
        Bocul = new gnu.chu.controles.CButton();
        jt = new gnu.chu.controles.CGridEditable(4) {
            public void cambiaColumna(int col,int colNueva, int row)
            {
                try
                {
                    if (col ==0)
                    {
                        if (! pro_codartE.hasCambio())
                        return;
                        String nombArt;
                        if (pro_codartE.getText().equals("X"))
                        {
                            nombArt="*TIPO*";
                        }
                        else
                        {
                            nombArt=pro_codartE.getNombArt(pro_codartE.getText());
                        }
                        if (nombArt==null)
                        jt.setValor("**PRODUCTO NO ENCONTRADO**", row, 1);
                        else
                        jt.setValor(nombArt, row, 1);
                        pro_codartE.resetCambio();
                    }
                }
                catch (Exception k)
                {
                    Error("Error al buscar Nombre Articulo", k);
                }
            }

            public void afterCambiaLinea()
            {
                pro_codartE.resetCambio();
                //      tar_feciniG.setText(tar_feciniE.getText());
                //      tar_fecfinG.setText(tar_fecfinE.getText());
            }

            public int cambiaLinea(int row, int col)
            {
                return cambiaLineaJT();
            }
        };
        ArrayList cabecera = new ArrayList();
        cabecera.add("Codigo"); // 2 -- Codigo
        cabecera.add("Nombre"); //3 -- Nombre
        cabecera.add("Precio"); // 4 -- Precio
        cabecera.add("Coment"); // 5 -- Comentario
        jt.setCabecera(cabecera);
        jt.setAnchoColumna(new int[]{86, 283, 60,150});
        jt.alinearColumna(new int[] {0, 0, 2,0});

        try {
            pro_codartE.setText("");
            ArrayList v = new ArrayList();
            v.add(pro_codartE.getTextField());
            v.add(pro_nombE);
            v.add(tar_preciE);
            v.add(tar_comenG);
            jt.setCampos(v);
        }catch (Exception k)
        {
            Error("Error al iniciar el grid",k);
        }
        Ppie = new gnu.chu.controles.CPanel();
        Baceptar = new gnu.chu.controles.CButton();
        Bcancelar = new gnu.chu.controles.CButton();
        opImpRef = new gnu.chu.controles.CCheckBox();

        pro_codartE.setAceptaNulo(false);

        Bimpri.setToolTipText("Imprimir Tarifa");
        Bimpri.setMaximumSize(new java.awt.Dimension(24, 24));
        Bimpri.setMinimumSize(new java.awt.Dimension(24, 24));
        Bimpri.setPreferredSize(new java.awt.Dimension(24, 24));

        Pprinc.setLayout(new java.awt.GridBagLayout());

        Pcabe.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        Pcabe.setMaximumSize(new java.awt.Dimension(530, 76));
        Pcabe.setMinimumSize(new java.awt.Dimension(530, 76));
        Pcabe.setPreferredSize(new java.awt.Dimension(530, 76));
        Pcabe.setLayout(null);

        cLabel5.setText("De Fecha");
        Pcabe.add(cLabel5);
        cLabel5.setBounds(10, 2, 49, 18);

        tar_feciniE.setMaximumSize(new java.awt.Dimension(10, 18));
        tar_feciniE.setMinimumSize(new java.awt.Dimension(10, 18));
        tar_feciniE.setPreferredSize(new java.awt.Dimension(10, 18));
        Pcabe.add(tar_feciniE);
        tar_feciniE.setBounds(65, 2, 76, 18);

        cLabel6.setText("A Fecha");
        Pcabe.add(cLabel6);
        cLabel6.setBounds(151, 2, 43, 18);

        tar_fecfinE.setMaximumSize(new java.awt.Dimension(10, 18));
        tar_fecfinE.setMinimumSize(new java.awt.Dimension(10, 18));
        tar_fecfinE.setPreferredSize(new java.awt.Dimension(10, 18));
        Pcabe.add(tar_fecfinE);
        tar_fecfinE.setBounds(198, 2, 75, 18);

        cLabel1.setText("Semana");
        Pcabe.add(cLabel1);
        cLabel1.setBounds(277, 2, 44, 18);

        tar_nusemE.setMinimumSize(new java.awt.Dimension(10, 18));
        tar_nusemE.setPreferredSize(new java.awt.Dimension(10, 18));
        Pcabe.add(tar_nusemE);
        tar_nusemE.setBounds(325, 2, 30, 18);

        cLabel2.setText("Tipo");
        Pcabe.add(cLabel2);
        cLabel2.setBounds(365, 2, 24, 18);

        tar_codiE.setMaximumSize(new java.awt.Dimension(160, 18));
        tar_codiE.setMinimumSize(new java.awt.Dimension(160, 18));
        tar_codiE.setPreferredSize(new java.awt.Dimension(160, 18));
        Pcabe.add(tar_codiE);
        tar_codiE.setBounds(399, 2, 122, 18);

        cPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Copiar de", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        cLabel3.setText("Fecha");
        cLabel3.setPreferredSize(new java.awt.Dimension(33, 18));

        tar_fecopE.setPreferredSize(new java.awt.Dimension(10, 18));

        cLabel4.setText("Tarifa");
        cLabel4.setPreferredSize(new java.awt.Dimension(33, 18));

        tar_copiaE.setMaximumSize(new java.awt.Dimension(160, 18));
        tar_copiaE.setMinimumSize(new java.awt.Dimension(160, 18));
        tar_copiaE.setPreferredSize(new java.awt.Dimension(160, 18));

        cLabel7.setText("Incremento");
        cLabel7.setPreferredSize(new java.awt.Dimension(64, 18));

        tar_incremE.setMinimumSize(new java.awt.Dimension(10, 18));
        tar_incremE.setPreferredSize(new java.awt.Dimension(10, 18));

        org.jdesktop.layout.GroupLayout cPanel1Layout = new org.jdesktop.layout.GroupLayout(cPanel1);
        cPanel1.setLayout(cPanel1Layout);
        cPanel1Layout.setHorizontalGroup(
            cPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(cPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(cLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(tar_fecopE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 75, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(cLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 41, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(tar_copiaE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 122, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(cLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 73, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(tar_incremE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 44, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(Bocul, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        cPanel1Layout.setVerticalGroup(
            cPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(cPanel1Layout.createSequentialGroup()
                .add(cPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(cLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(tar_fecopE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(tar_copiaE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(Bocul, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(tar_incremE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Pcabe.add(cPanel1);
        cPanel1.setBounds(12, 26, 487, 47);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        Pprinc.add(Pcabe, gridBagConstraints);

        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jt.setMaximumSize(new java.awt.Dimension(100, 100));
        jt.setMinimumSize(new java.awt.Dimension(100, 100));

        org.jdesktop.layout.GroupLayout jtLayout = new org.jdesktop.layout.GroupLayout(jt);
        jt.setLayout(jtLayout);
        jtLayout.setHorizontalGroup(
            jtLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 553, Short.MAX_VALUE)
        );
        jtLayout.setVerticalGroup(
            jtLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 272, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        Pprinc.add(jt, gridBagConstraints);

        Ppie.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        Ppie.setMaximumSize(new java.awt.Dimension(392, 30));
        Ppie.setMinimumSize(new java.awt.Dimension(392, 30));
        Ppie.setPreferredSize(new java.awt.Dimension(392, 30));
        Ppie.setLayout(null);

        Baceptar.setText("Aceptar");
        Ppie.add(Baceptar);
        Baceptar.setBounds(10, 2, 117, 25);

        Bcancelar.setText("Cancelar");
        Ppie.add(Bcancelar);
        Bcancelar.setBounds(140, 2, 117, 25);

        opImpRef.setText("Impr. Referenc.");
        Ppie.add(opImpRef);
        opImpRef.setBounds(273, 2, 110, 23);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.weightx = 1.0;
        Pprinc.add(Ppie, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CButton Bcancelar;
    private gnu.chu.controles.CButton Bimpri;
    private gnu.chu.controles.CButton Bocul;
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
    private gnu.chu.controles.CPanel cPanel1;
    private gnu.chu.controles.CGridEditable jt;
    private gnu.chu.controles.CCheckBox opImpRef;
    private gnu.chu.camposdb.proPanel pro_codartE;
    private gnu.chu.controles.CTextField pro_nombE;
    private gnu.chu.controles.CComboBox tar_codiE;
    private gnu.chu.controles.CTextField tar_comenG;
    private gnu.chu.controles.CComboBox tar_copiaE;
    private gnu.chu.controles.CTextField tar_fecfinE;
    private gnu.chu.controles.CTextField tar_feciniE;
    private gnu.chu.controles.CTextField tar_fecopE;
    private gnu.chu.controles.CTextField tar_incremE;
    private gnu.chu.controles.CTextField tar_nusemE;
    private gnu.chu.controles.CTextField tar_preciE;
    // End of variables declaration//GEN-END:variables

}
