/**
 *
 * <p>Titulo: MantTariCliente </p>
 * <p>Descripción: Mantenimiento Tarifas de Ventas para clientes</p>
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
 * @author chuchiP
 *
 */ 

package gnu.chu.anjelica.pad;

import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.listados.Listados;
import gnu.chu.controles.StatusBar;
import gnu.chu.interfaces.PAD;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.mensajes;
import gnu.chu.utilidades.navegador;
import gnu.chu.utilidades.ventanaPad;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.print.PrinterException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;


public class MantTariCliente extends ventanaPad implements PAD, JRDataSource
{
  double tarIncpre;
  String localeEmpresa;
  boolean swLocaleEmpresa;
  String s;
  String fecini,tipo;
  boolean ARG_MODCONSULTA=false;
  boolean swInicio=false;
  public MantTariCliente(EntornoUsuario eu, Principal p)
  {
    this(eu,p,null);
  }

  public MantTariCliente(EntornoUsuario eu, Principal p, Hashtable ht)
  {
      EU = eu;
      vl = p.panel1;
      jf = p;
      eje = true;

      setTitulo("Mant. Tarifas de Clientes");

      try
      {
        if (ht != null)
        {
          if (ht.get("modoConsulta") != null)
            ARG_MODCONSULTA = Boolean.parseBoolean(ht.get("modoConsulta").toString());
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

    public MantTariCliente(gnu.chu.anjelica.menu p, EntornoUsuario eu)
    {

      EU = eu;
      vl = p.getLayeredPane();
        setTitulo("Mant. Tarifas de Clientes");
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
      this.setVersion("2017-10-01" + (ARG_MODCONSULTA ? " SOLO LECTURA" : ""));
      statusBar = new StatusBar(this);
      nav = new navegador(this,dtCons,false);
      iniciarFrame();
      strSql = "SELECT tar_fecini,tar_fecfin,cli_codi FROM taricli"+
          " group by tar_fecini,tar_fecfin,cli_codi" +
          " order by tar_fecini,cli_codi";
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
//      statusBar.add(Bimpri, new GridBagConstraints(9, 0, 1, 2, 0.0, 0.0
//                            , GridBagConstraints.EAST,
//                            GridBagConstraints.VERTICAL,
//                            new Insets(0, 5, 0, 0), 0, 0));

    }
    @Override
    public void iniciarVentana() throws Exception
    {
      pro_codiE.iniciar(dtStat, this, vl, EU);
      pro_codiE.setUsaCodigoVenta(true);
      pro_codiE.setColumnaAlias("pro_codart");
      tar_feciniE.setColumnaAlias("tar_fecini");
      tar_fecfinE.setColumnaAlias("tar_fecfin");
      cli_codiE.setColumnaAlias("cli_codi");
      localeEmpresa=MantPaises.getLocalePais(pdempresa.getPais(dtStat, EU.em_cod),dtStat);
      activarEventos();
      activar(false);
//      verDatos();p
      nav.requestFocus();
      Pprinc.setDefButton(Baceptar);
//      Pprinc.setEscButton(Bcancelar);
    }

    void activarEventos()
    {
         BTexto.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                BTexto_actionPerformed();
            }
         });
        Bimpri.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Bimpri_actionPerformed();
            }
         });

//      tar_feciniE.addFocusListener(new FocusAdapter()
//      {
//            @Override
//        public void focusLost(FocusEvent e)
//        {
//          if (tar_feciniE.isQuery()|| tar_feciniE.getError())
//            return;
//        }
//      });

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
    void BTexto_actionPerformed()
    {
         String s=mensajes.mensajeExplica("Copie y pegue", "Codigo:Precio","");
         if (s==null)
             return;
         int nLen=s.length();
         int modo=0; // Buscando codigo producto
         int inicProd,finProd;
         int codProd=0;
         double precio;
              
        char sep=new DecimalFormatSymbols(new Locale("es","","")).getDecimalSeparator();
        NumberFormat nf = NumberFormat.getInstance(new Locale("es","",""));
         for (int n=0;n<nLen;n++)
         {
            if (modo==0)
            {              
              if (Character.isDigit(s.charAt(n)) )
              {
                  inicProd=n;
                  finProd=0;
                  for (;n<nLen;n++)
                  {
//                      if (!Character.isAlphabetic(s.charAt(n)))
//                          break;
                      if (s.charAt(n)==':')
                      {
                          finProd=n;
                          break;
                      }
                  }
                  if (finProd>0)
                  {
                      try {
                        codProd=Integer.valueOf(s.substring(inicProd,finProd).trim());
                      } catch (NumberFormatException ex )
                      {
                         continue;
                      }
                      modo=1; // Buscando precio
                      continue;
                  }
              }
            }
            if (modo==1)
            { // Buscando precio
                 if (!Character.isDigit(s.charAt(n)) )
                 {
                     modo=0;
                     continue;
                 }
//                if (!Character.isAlphabetic(s.charAt(n)))
//                { 
//                    modo=0;
//                    continue;
//                }
            
                  inicProd=n;
                  finProd=0;
                  for (;n<nLen;n++)
                  {
//                      if (!Character.isAlphabetic(s.charAt(n)))
//                          break;
                     if (!Character.isDigit(s.charAt(n)) && s.charAt(n)!=sep )
                      {
                          finProd=n;
                          break;
                      }
                  }
                  if (finProd>0 && finProd>inicProd)
                  {                      
                      try                   
                      {
                          precio=nf.parse(s.substring(inicProd,finProd).trim()).doubleValue();
                      } catch (ParseException ex)
                      {
                          modo=0;
                          continue;
                      }
                      pro_codartE.setText(""+codProd);
                      pro_codartE.pro_codiE_focusLost();
                      ArrayList v=new ArrayList();
                      v.add(pro_codartE.getText());
                      v.add(pro_codartE.getTextNomb());
                      v.add(precio);
                      v.add("Importado");
                      v.add(0);
                      jt.addLinea(v);
                  }
                  modo=0; // Buscando codigo
            }
            
         }
         
         
    }
    void Bimpri_actionPerformed()
   {
     if (dtCons.getNOREG())
       return;
     try {
       if (dtCons.getNOREG())
       {
         msgBox("NO HAY NINGUN PRODUCTO SELECIONADO");
         return;
       }

       HashMap mp = Listados.getHashMapDefault();
       swLocaleEmpresa=loc_codiE.getValor().equals(localeEmpresa);
       swInicio=true;
       Listados lis=  Listados.getListado(EU.em_cod, Listados.TARIFA, dtStat);
       mp.put("tar_feciniP", tar_feciniE.getDate());
       mp.put("tar_fecfinP", tar_fecfinE.getDate());
       mp.put("impRefer", opImpRef.isSelected());
       mp.put("tar_nombP", cli_codiE.getTextNomb()); 
       mp.put("logo", lis.getPathLogo()); 
       JasperReport jr;
       jr = Listados.getJasperReport(EU,lis.getNombFich());
      
       int cliCodi=cli_codiE.getValorInt();
    
       s="select * from taricli  "+
            " where cli_codi = "+cliCodi+
            " and tar_fecini = TO_DATE('"+tar_feciniE.getText()+"','dd-MM-yyyy') "+
            " order by tar_linea";
       
       dtCon1.select(s);

       JasperPrint jp = JasperFillManager.fillReport(jr, mp, this);
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
      
      if (tar_feciniE.isNull())
      {
        mensajeErr("Introduzca fecha inicio");
        tar_feciniE.requestFocus();
        return;
      }
      try {
        if (! cli_codiE.controlar())
        {
            mensajeErr("Cliente NO valido");
            cli_codiE.requestFocus();
            return;
        }
      
        if (nav.pulsado == navegador.ADDNEW )
        {
          s = "SELECT * FROM taricli WHERE "+
              " tar_fecini = TO_DATE('"+tar_feciniE.getText()+"','dd-MM-yyyy') "+
              " and cli_codi = " + cli_codiE.getValorInt();
          if (dtCon1.select(s))
          {
            int ret=mensajes.mensajeYesNo("Le ha dado a Tarifa nueva, pero ya existe una tarifa con estos criterios. Desea editarla");
            if (ret!=mensajes.YES)
            {
                msgBox("Alta cancelada");
                mensaje("");
                activaTodo();
                nav.pulsado=navegador.NINGUNO;
                return;
            }
            verDatLin(tar_feciniE.getText(), cli_codiE.getText(),tar_fecfinE.getText(),0);
            jt.cargaTodo();
            fecini=tar_feciniE.getText();
            tipo=cli_codiE.getText();
            nav.pulsado = navegador.EDIT;
            mensaje("Editando ... ");
          }
          else
          {
               s = "SELECT * FROM taricli WHERE "+
                 " tar_fecini < TO_DATE('"+tar_feciniE.getText()+"','dd-MM-yyyy') "+
                 " and (tar_fecfin is null or tar_fecfin >=  TO_DATE('"+tar_feciniE.getText()+"','dd-MM-yyyy')) "+
                 " and cli_codi = " + cli_codiE.getValorInt();
               if (dtStat.select(s))
               {
                   mensajeErr("Tarifa entraria en conflicto con la del "+dtStat.getFecha("tar_fecini","dd-MM-yyyy"));
                   tar_feciniE.requestFocusLater();
                   return;
               }
          }
        }

        Pcabe.setEnabled(false);
        Baceptar.setEnabled(true);
     
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
        int tarButapa=tar_butapaE.getValorInt(); 
        borDatos(fecha,tipo);

        int nRow = jt.getRowCount();
        int grupo=0;
        int animal=0;
        dtAdd.addNew("taricli");
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
          dtAdd.setDato("tar_butapa",tarButapa);
          dtAdd.setDato("tar_linea",n);
          dtAdd.setDato("cli_codi",cli_codiE.getValorInt());
          dtAdd.setDato("pro_codart",jt.getValString(n,0));
          dtAdd.setDato("pro_nomb",jt.getValString(n,1));
          dtAdd.setDato("tar_preci",jt.getValorDec(n,2));
          dtAdd.setDato("tar_comen",jt.getValString(n,3));   
          dtAdd.setDato("tar_comrep",jt.getValorDec(n,4)); 
          dtAdd.update(stUp);
        }
        ctUp.commit();
        mensajeErr("Datos ... Guardados");
      } catch (SQLException | ParseException k)
      {
        Error("Error en La insercion de Referencias",k);
      }
    }

    void  borDatos(String fecha,String tipo) throws SQLException,java.text.ParseException
    {
      s = "DELETE FROM taricli " +
          " WHERE tar_fecini = TO_DATE('" + fecha + "','dd-MM-yyyy') " +
          " AND cli_codi = " + tipo  ;
      stUp.executeUpdate(dtAdd.parseaSql(s));
    }
    void verDatos()
    {
      try
      {
        if (dtCons.getNOREG())
          return;
        tar_feciniE.setText(dtCons.getFecha("tar_fecini","dd-MM-yyyy"));
        cli_codiE.setText(dtCons.getString("cli_codi"));
        
        s = "SELECT tar_fecfin,tar_butapa " +
          " FROM taricli " +
          " WHERE tar_fecini = TO_DATE('"+tar_feciniE.getText()+"','dd-MM-yyyy') "+
          " AND cli_codi = "+tipo;
        if ( dtCon1.select(s))
        {
            tar_butapaE.setValor(dtCon1.getString("tar_butapa"));
            tar_fecfinE.setDate(dtCon1.getDate("tar_fecfin"));
        }
        
        verDatLin(tar_feciniE.getText(),cli_codiE.getText(),tar_fecfinE.getText(),0);
      } catch (Exception k)
      {
        Error("Error al ver datos",k);
      }
    }

    void verDatLin(String fecha,String tipo,String fecfin,double increm) throws Exception
    {
      s = "SELECT pro_codart,pro_nomb,tar_preci,tar_comen,tar_comrep " +
          " FROM taricli " +
          " WHERE tar_fecini = TO_DATE('"+fecha+"','dd-MM-yyyy') "+
          " AND cli_codi = "+tipo+
          (pro_codiE.isNull()?"":" and pro_codart like '%"+pro_codiE.getText()+"%'")+
          " order by tar_linea";
      if (jt.isEnabled())
        jt.setEnabled(false);
      if (! dtCon1.select(s))
      {
        mensajeErr("Registro BORRADO");
        tar_fecfinE.resetTexto();

        jt.removeAllDatos();
        return;
      }
      tar_fecfinE.setText(fecfin);

      jt.setDatos(dtCon1);
      if (increm>0)
      { // Sumarle el incremento.
          int nl=jt.getRowCount();
          for (int n=0;n<nl;n++)
          {
              if (! jt.getValString(n,0).equals("") && !jt.getValString(n,0).equals("X") )
              {
                  jt.setValor(jt.getValorDec(n,2)==0?0:jt.getValorDec(n,2)+increm,n,2);
              }
          }
      }
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
      pro_codiE.setEnabled(false);
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
        pro_codiE.setEnabled(true);
        Pcabe.resetTexto();
        
        tar_feciniE.requestFocus();

    }

    @Override
  public void ej_query1()
  {
    Baceptar.setEnabled(false);
    ArrayList v=new ArrayList();
    v.add(tar_feciniE.getStrQuery());
    v.add(tar_fecfinE.getStrQuery());
    v.add(cli_codiE.getStrQuery());
    v.add(pro_codiE.getStrQuery());
    Pcabe.setQuery(false);
    s="SELECT tar_fecini,tar_fecfin,cli_codi FROM taricli";
    s=creaWhere(s,v);
    s+=" group by tar_fecini,tar_fecfin,cli_codi"+
        " order by tar_fecini,cli_codi";
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
      fatalError("Error al buscar Tarifas de clientes: ", ex);
    }
  }

  @Override
  public void canc_query() {
    Pcabe.setQuery(false);
    pro_codiE.resetTexto();
    mensaje("");
    mensajeErr("Consulta ... CANCELADA");
    activaTodo();
    verDatos();
  }

    @Override
  public void PADEdit() {
    
    mensaje("Editando ....");
//    ejeNume=eje_numeE.getValorInt();
    if (!pro_codiE.isNull())
    {
        pro_codiE.resetTexto();
        verDatos();
    }
    jt.cargaTodo();
    fecini=tar_feciniE.getText();
    tipo=cli_codiE.getText();
    activar(true);
   
    jt.requestFocusInicioLater();
  }
  @Override
  public void ej_edit1() {
      jt.salirGrid();

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
    tar_feciniE.requestFocus();
    mensaje("Insertando ....");
  }

  @Override
  public void ej_addnew1() {
    jt.salirGrid();

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
    guardaDatos(tar_feciniE.getText(),cli_codiE.getText());
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
      borDatos(tar_feciniE.getText(), cli_codiE.getText());
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
    cli_codiE.iniciar(dtStat,this,vl,EU);
    pro_codartE.iniciar(dtStat, this, vl, EU);   
    loc_codiE.setDatos(MantIdiomas.getDatos(dtAdd));
    loc_codiE.setValor(MantPaises.getLocalePais(pdempresa.getPais(dtStat, EU.em_cod), dtCon1));
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
       return dt.getDouble("tar_preci", true)==0?0:dt.getDouble("tar_preci", true) + tarIncPre;
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
        tar_comrepE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#9.99");
        Pprinc = new gnu.chu.controles.CPanel();
        Pcabe = new gnu.chu.controles.CPanel();
        cLabel5 = new gnu.chu.controles.CLabel();
        tar_feciniE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel6 = new gnu.chu.controles.CLabel();
        tar_fecfinE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel2 = new gnu.chu.controles.CLabel();
        Bocul = new gnu.chu.controles.CButton();
        cli_codiE = new gnu.chu.camposdb.cliPanel();
        BTexto = new gnu.chu.controles.CButton();
        cLabel7 = new gnu.chu.controles.CLabel();
        tar_butapaE = new gnu.chu.controles.CComboBox();
        jt = new gnu.chu.controles.CGridEditable(5) {
            public void cambiaColumna(int col,int colNueva, int row)
            {
                try
                {
                    if (col ==0)
                    {
                        if (! pro_codartE.hasCambio())
                        return;
                        pro_codartE.pro_codiE_focusLost();
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
        pro_codartE.setUsaCodigoVenta(true);
        pro_codartE.setText("");
        ArrayList cabecera = new ArrayList();
        cabecera.add("Codigo"); // 2 -- Codigo
        cabecera.add("Nombre"); //3 -- Nombre
        cabecera.add("Precio"); // 4 -- Precio
        cabecera.add("Coment"); // 5 -- Comentario
        cabecera.add("Comis."); // 4 -- Precio
        jt.setCabecera(cabecera);
        jt.setAnchoColumna(new int[]{86, 283, 60,150,60});
        jt.setAlinearColumna(new int[] {0, 0, 2,0,2});

        jt.setNumRegCargar(0);
        try {
            pro_codartE.setText("");
            ArrayList v = new ArrayList();
            v.add(pro_codartE.getTextField());
            v.add(pro_nombE);
            v.add(tar_preciE);
            v.add(tar_comenG);
            v.add(tar_comrepE);
            jt.setCampos(v);
        }catch (Exception k)
        {
            Error("Error al iniciar el grid",k);
        }
        Ppie = new gnu.chu.controles.CPanel();
        Baceptar = new gnu.chu.controles.CButton();
        Bcancelar = new gnu.chu.controles.CButton();
        Bimpresion = new gnu.chu.controles.CPanel();
        cLabel4 = new gnu.chu.controles.CLabel();
        Bimpri = new gnu.chu.controles.CButton(Iconos.getImageIcon("print"));
        opImpRef = new gnu.chu.controles.CCheckBox();
        loc_codiE = new gnu.chu.controles.CComboBox();
        cLabel3 = new gnu.chu.controles.CLabel();
        pro_codiE = new gnu.chu.camposdb.proPanel();

        pro_codartE.setAceptaNulo(false);
        pro_codartE.setUsaCodigoVenta(true);

        Pprinc.setLayout(new java.awt.GridBagLayout());

        Pcabe.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        Pcabe.setMaximumSize(new java.awt.Dimension(550, 50));
        Pcabe.setMinimumSize(new java.awt.Dimension(550, 50));
        Pcabe.setPreferredSize(new java.awt.Dimension(550, 50));
        Pcabe.setLayout(null);

        cLabel5.setText("De Fecha");
        Pcabe.add(cLabel5);
        cLabel5.setBounds(3, 3, 49, 18);

        tar_feciniE.setMaximumSize(new java.awt.Dimension(10, 18));
        tar_feciniE.setMinimumSize(new java.awt.Dimension(10, 18));
        tar_feciniE.setPreferredSize(new java.awt.Dimension(10, 18));
        Pcabe.add(tar_feciniE);
        tar_feciniE.setBounds(60, 3, 70, 18);

        cLabel6.setText("A Fecha");
        Pcabe.add(cLabel6);
        cLabel6.setBounds(10, 25, 43, 18);

        tar_fecfinE.setMaximumSize(new java.awt.Dimension(10, 18));
        tar_fecfinE.setMinimumSize(new java.awt.Dimension(10, 18));
        tar_fecfinE.setPreferredSize(new java.awt.Dimension(10, 18));
        Pcabe.add(tar_fecfinE);
        tar_fecfinE.setBounds(60, 25, 70, 18);

        cLabel2.setText("Buscar en Tarifas Generales");
        Pcabe.add(cLabel2);
        cLabel2.setBounds(140, 25, 180, 18);
        Pcabe.add(Bocul);
        Bocul.setBounds(545, 30, 2, 2);
        Pcabe.add(cli_codiE);
        cli_codiE.setBounds(190, 3, 350, 18);

        BTexto.setText("Importar");
        Pcabe.add(BTexto);
        BTexto.setBounds(480, 25, 60, 20);

        cLabel7.setText("Cliente");
        Pcabe.add(cLabel7);
        cLabel7.setBounds(140, 3, 50, 18);

        tar_butapaE.addItem("Si","1");
        tar_butapaE.addItem("No","0");
        Pcabe.add(tar_butapaE);
        tar_butapaE.setBounds(310, 25, 60, 17);

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
            .add(0, 269, Short.MAX_VALUE)
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
        Ppie.setMaximumSize(new java.awt.Dimension(392, 58));
        Ppie.setMinimumSize(new java.awt.Dimension(392, 58));
        Ppie.setPreferredSize(new java.awt.Dimension(392, 58));
        Ppie.setLayout(null);

        Baceptar.setText("Aceptar");
        Ppie.add(Baceptar);
        Baceptar.setBounds(10, 2, 90, 25);

        Bcancelar.setText("Cancelar");
        Ppie.add(Bcancelar);
        Bcancelar.setBounds(10, 30, 90, 25);

        Bimpresion.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        Bimpresion.setLayout(null);

        cLabel4.setText("Idioma");
        cLabel4.setPreferredSize(new java.awt.Dimension(33, 18));
        Bimpresion.add(cLabel4);
        cLabel4.setBounds(10, 22, 40, 17);

        Bimpri.setText("Imprimir");
        Bimpri.setToolTipText("Imprimir Tarifa");
        Bimpri.setMaximumSize(new java.awt.Dimension(24, 24));
        Bimpri.setMinimumSize(new java.awt.Dimension(24, 24));
        Bimpri.setPreferredSize(new java.awt.Dimension(24, 24));
        Bimpresion.add(Bimpri);
        Bimpri.setBounds(330, 22, 80, 24);

        opImpRef.setText("Impr. Ref.");
        Bimpresion.add(opImpRef);
        opImpRef.setBounds(240, 22, 80, 17);
        Bimpresion.add(loc_codiE);
        loc_codiE.setBounds(50, 22, 180, 18);

        cLabel3.setText("Articulo");
        Bimpresion.add(cLabel3);
        cLabel3.setBounds(10, 2, 50, 18);

        pro_codiE.setAncTexto(80);
        pro_codiE.setUsaCodigoVenta(true);
        Bimpresion.add(pro_codiE);
        pro_codiE.setBounds(60, 2, 280, 17);

        Ppie.add(Bimpresion);
        Bimpresion.setBounds(130, 2, 420, 50);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        Pprinc.add(Ppie, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton BTexto;
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CButton Bcancelar;
    private gnu.chu.controles.CPanel Bimpresion;
    private gnu.chu.controles.CButton Bimpri;
    private gnu.chu.controles.CButton Bocul;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Ppie;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.camposdb.cliPanel cli_codiE;
    private gnu.chu.controles.CGridEditable jt;
    private gnu.chu.controles.CComboBox loc_codiE;
    private gnu.chu.controles.CCheckBox opImpRef;
    private gnu.chu.camposdb.proPanel pro_codartE;
    private gnu.chu.camposdb.proPanel pro_codiE;
    private gnu.chu.controles.CTextField pro_nombE;
    private gnu.chu.controles.CComboBox tar_butapaE;
    private gnu.chu.controles.CTextField tar_comenG;
    private gnu.chu.controles.CTextField tar_comrepE;
    private gnu.chu.controles.CTextField tar_fecfinE;
    private gnu.chu.controles.CTextField tar_feciniE;
    private gnu.chu.controles.CTextField tar_preciE;
    // End of variables declaration//GEN-END:variables
   
    @Override
   public boolean next() throws JRException
   {
     try
     {
        if (swInicio)
        {
            swInicio=false;
            return !dtCon1.getNOREG();           
        }
        return dtCon1.next();
     }
     catch (Exception k)
     {
       throw new JRException(k);
     }
   }

  @Override
   public Object getFieldValue(JRField jRField) throws JRException
   {
     try
     {
       String campo = jRField.getName().toLowerCase();
       switch (campo)
       {
           case "pro_nomb":
             if (!swLocaleEmpresa && dtCon1.getString("pro_codart").length()>1)
                 return MantArticulos.getNombreProdLocale(dtCon1.getString("pro_codart"), loc_codiE.getValor(), dtStat);
             else
                return dtCon1.getString(campo);
            case "tar_codi":
               return dtCon1.getInt(campo);               
            case "tar_linea":
               return dtCon1.getInt(campo);               
           case "tar_fecini":
               return dtCon1.getDate(campo);
           case "tar_fecfin":
               return dtCon1.getDate(campo);
           case "pro_codart":  
               return dtCon1.getString(campo);
           case "tar_preci":
               return new BigDecimal(dtCon1.getDouble(campo)+tarIncpre);               
           case "tar_comen":
               return dtCon1.getString(campo);
           case "tar_grupo":
               return dtCon1.getInt(campo);
           case "tar_tipo":
                 return dtCon1.getInt(campo);
           default:
                 throw new Exception("Campo " + campo + " NO encontrado");
       }
     }
     catch (Exception k)
     {
       throw new JRException(k);
     }
   }
}
