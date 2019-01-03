package gnu.chu.anjelica.compras;

import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import java.sql.*;
import gnu.chu.Menu.*;
import java.awt.*;
import java.util.*;
import gnu.chu.interfaces.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * <p>Titulo:   Mant. Tarifas de Portes </p>
 * <p>Descripción: Mant. Tarifas de portes (VARIABLES). Para cada proveedor habra
 *  diferentes tarifas que tendran vigencias entre unas fechas dadas.<br>
 *  Es decir puede haber, simultanemamente, la tarifa 1 del prov. 1 valida del 1/1/05 al 1/2/05
 *  la tarifa 1 del prov 1 valida del 1/3/05 al 1/4/5 (Misma tarifa y prov, distintas fechas)
 *  la tarifa 1 del prov 2 valida del 1/1/05 al /1/5/05 (misma tarifa, distinto proveedor).
 *   etc.
 * <p>Copyright: Copyright (c) 2005-2014
 *   *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
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
 * </p>
 * @author chuchiP
 * @version 1.0
 */
public class pdtaripor extends ventanaPad implements PAD
{
  int tapCodi,traCodi;
  String fecini,fecfin;
  CPanel Pprinc = new CPanel();
  CPanel Pcabe = new CPanel();
  CGridEditable jt = new CGridEditable(3);


  String s;
//  CButton Baceptar = new CButton();
//  CButton Bcancelar = new CButton();
  CTextField  tap_kilosE = new CTextField(Types.DECIMAL,"###,##9.99");
  CTextField tap_imporE = new CTextField(Types.DECIMAL,"###,##9.99");
  CTextField tap_fijkilE= new CTextField(Types.CHAR,"?",1);
  CButton Bocul = new CButton();
  CLabel cLabel4 = new CLabel();
  CTextField tap_codiE = new CTextField(Types.DECIMAL,"##9");
  boolean modoConsulta=false;
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CLabel cLabel1 = new CLabel();
  CLinkBox tra_codiE = new CLinkBox();
  CTextField tap_feciniE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel cLabel3 = new CLabel();
  CLabel cLabel2 = new CLabel();
  CTextField tap_fecfinE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CTextField tap_nombE = new CTextField(Types.CHAR,"X",30);


  public pdtaripor (EntornoUsuario eu, Principal p)
  {
    this(eu,p,null);
  }

  public pdtaripor(EntornoUsuario eu, Principal p, Hashtable ht)
  {
      EU = eu;
      vl = p.panel1;
      jf = p;
      eje = true;
      setTitulo( "Mant. Tarifas Portes");
      try
      {
        if (ht != null)
        {
          if (ht.get("modoConsulta") != null)
            modoConsulta = Boolean.parseBoolean(ht.get("modoConsulta").toString());
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

    public pdtaripor (gnu.chu.anjelica.menu p, EntornoUsuario eu)
    {

      EU = eu;
      vl = p.getLayeredPane();
      setTitulo( "Mant. Tarifas Portes");
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
      iniciar(502, 522);
      this.setSize(new Dimension(502, 522));
      setVersion( "(20141113)" + (modoConsulta?"-Modo Consulta-":"")+")");
      strSql = "SELECT tra_codi,tap_codi,tap_fecini,tap_fecfin from taripor " +
          " group by tra_codi,tap_codi,tap_fecini,tap_fecfin " +
          " order by tra_codi,tap_codi,tap_fecini ";

      statusBar = new StatusBar(this);
      conecta();
      nav = new navegador(this, dtCons, false, modoConsulta ? navegador.CURYCON : navegador.NORMAL);
      Pprinc.setLayout(gridBagLayout1);
      Pcabe.setBorder(BorderFactory.createRaisedBevelBorder());
      Pcabe.setMaximumSize(new Dimension(485, 20));
      Pcabe.setMinimumSize(new Dimension(485, 20));
      Pcabe.setPreferredSize(new Dimension(485, 20));
      Pcabe.setLayout(null);

      ArrayList cabecera = new ArrayList();
      cabecera.add("Hasta Kilos"); // 0 -- Kilos (0 Sin limite)
      cabecera.add("Importe"); //1 -- Importe
      cabecera.add("Kilos/Fijo"); // 2 -- Kilos o Fijo
      jt.setCabecera(cabecera);
      jt.setAnchoColumna(new int[]      {90, 90, 80});
      jt.setAlinearColumna(new int[]      {2, 2, 1});
     
      tap_fijkilE.setAdmiteCar(CTextField.CHAR_LIMIT);
      tap_fijkilE.setStrCarEsp("KF");
      tap_fijkilE.setMayusc(true);
      tap_fijkilE.setHorizontalAlignment(CTextField.CENTER);
      tap_kilosE.setToolTipText("Introducir 0 para marcar sin Limite");

      ArrayList v = new ArrayList();
      v.add(tap_kilosE);
      v.add(tap_imporE);
      v.add(tap_fijkilE);
      jt.setCampos(v);
      jt.setMaximumSize(new Dimension(491, 443));
      jt.setMinimumSize(new Dimension(491, 443));
      jt.setPreferredSize(new Dimension(491, 443));
      jt.setAjustarGrid(true);
      jt.setAjusAncCol(true);
      jt.setFormatoCampos();
      jt.setConfigurar("gnu.chu.anjelica.compras.pdtaripor", EU, dtStat);
      jt.setNumRegCargar(100);
      iniciar(this);
      Baceptar.setMaximumSize(new Dimension(100, 26));
      Baceptar.setMinimumSize(new Dimension(100, 26));
      Baceptar.setPreferredSize(new Dimension(100, 26));
      Bcancelar.setMaximumSize(new Dimension(100, 26));
      Bcancelar.setMinimumSize(new Dimension(100, 26));
      Bcancelar.setPreferredSize(new Dimension(100, 26));
      Pprinc.setInputVerifier(null);
      cLabel4.setText("Tarifa");
      cLabel4.setBounds(new Rectangle(51, 21, 36, 17));
      cLabel1.setText("Transportistas");
      cLabel1.setBounds(new Rectangle(5, 2, 89, 17));
      tra_codiE.setAncTexto(40);
      tra_codiE.setBounds(new Rectangle(93, 2, 308, 17));
      cLabel3.setText("De Fecha");
      cLabel3.setBounds(new Rectangle(80, 42, 53, 17));
      cLabel2.setText("A Fecha");
      cLabel2.setBounds(new Rectangle(268, 42, 50, 17));
      tap_fecfinE.setBounds(new Rectangle(320, 42, 80, 17));
      tap_feciniE.setBounds(new Rectangle(136, 42, 80, 17));
      Bocul.setBounds(new Rectangle(411, 42, 1, 1));
      tap_codiE.setBounds(new Rectangle(95, 21, 39, 17));

      tap_nombE.setBounds(new Rectangle(136, 21, 265, 17));
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
      Pprinc.add(jt, new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(0, 2, 0, 0), 0, 0));
      Pprinc.add(Pcabe,   new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 0, 0), -75, 51));
      this.getContentPane().add(Pprinc, BorderLayout.CENTER);
      Pcabe.add(cLabel1, null);
      Pcabe.add(tra_codiE, null);
      Pcabe.add(cLabel3, null);
      Pcabe.add(tap_nombE, null);
      Pcabe.add(tap_codiE, null);
      Pcabe.add(cLabel4, null);
      Pcabe.add(Bocul, null);
      Pcabe.add(tap_fecfinE, null);
      Pcabe.add(cLabel2, null);
      Pcabe.add(tap_feciniE, null);
    }

  @Override
    public void iniciarVentana() throws Exception
    {
      jt.setButton(KeyEvent.VK_F4,Baceptar);
      s="SELECT tra_codi,tra_nomb FROM v_transport ORDER BY tra_nomb";
      dtStat.select(s);
      tra_codiE.addDatos(dtStat);
      tra_codiE.setFormato(Types.DECIMAL,"#9");
      tap_codiE.setColumnaAlias("tap_codi");
      tap_nombE.setColumnaAlias("tap_nomb");
      tra_codiE.setColumnaAlias("tra_codi");
      tap_feciniE.setColumnaAlias("tap_fecini");
      tap_fecfinE.setColumnaAlias("tap_fecfin");
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
        @Override
        public void focusGained(FocusEvent e)
        {
          irGrid();
        }
      });
      tap_codiE.addFocusListener(new FocusAdapter()
      {

        @Override
        public void focusLost(FocusEvent e)
        {
          if (!tap_codiE.isQuery())
          {
            ponNombTap(tra_codiE.getValorInt(),tap_codiE.getValorInt());
          }
        }
      });
    }
    void ponNombTap(int traCodi,int tapCodi)
    {
      try
      {
        s="SELECT tap_nomb FROM taripor where tra_codi = "+traCodi+
            " and tap_codi = "+tapCodi;
        if (dtStat.select(s))
          tap_nombE.setText(dtStat.getString("tap_nomb"));
      } catch (Exception k)
      {
        Error("Error al recoger Nombre de Tarifa",k);
      }
    }
    boolean checkCab() throws Exception
    {
      if (! tra_codiE.controla())
      {
        mensajeErr("Transportista NO VALIDO");
        return false;
      }
      if (tap_nombE.isNull())
      {
        mensajeErr("Introduzca Nombre de Tarifa");
        tap_codiE.requestFocus();
        return false;
      }
      if (tap_feciniE.getError() || tap_feciniE.isNull())
      {
        mensajeErr("Fecha de INICIO NO es Valido");
        tap_feciniE.requestFocus();
        return false;
      }
      if (tap_fecfinE.getError() || tap_fecfinE.isNull())
      {
        mensajeErr("Fecha de FINAL NO es Valido");
        tap_fecfinE.requestFocus();
        return false;
      }
      if (Formatear.comparaFechas(tap_fecfinE.getDate(),tap_feciniE.getDate())<0)
      {
         mensajeErr("Fecha Final DEBE ser superior a la Fecha Inicial");
         tap_feciniE.requestFocus();
         return false;
      }
      return true;
    }
    void irGrid()
    {
      if (nav.pulsado!=navegador.ADDNEW && nav.pulsado!=navegador.EDIT)
        return;

      try {
        if (! checkCab())
          return;
        if (nav.pulsado == navegador.ADDNEW)
        {
          s = "SELECT * FROM taripor  "+
              " where tap_codi = "+tap_codiE.getValorInt()+
              " and tra_codi = "+tra_codiE.getValorInt()+
              " and tap_fecini = TO_DATE('"+tap_feciniE.getFecha("dd-MM-yyyy")+"','dd-MM-yyyy')"+
              " and tap_fecfin = TO_DATE('"+tap_fecfinE.getFecha("dd-MM-yyyy")+"','dd-MM-yyyy')";
          if (dtCon1.select(s))
          {
            verDatLin(tap_codiE.getValorInt(),tra_codiE.getValorInt(),
                      tap_feciniE.getText(),tap_fecfinE.getText());
            jt.cargaTodo();
            tapCodi = tap_codiE.getValorInt();
            traCodi = tra_codiE.getValorInt();
            fecini = tap_feciniE.getText();
            fecfin = tap_fecfinE.getText();
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

    void guardaDatos(int tapCodi,int traCodi,String fecini,String fecfin) throws Exception
    {
      String s;

        borDatos(tapCodi,traCodi,fecini,fecfin);

        int nRow = jt.getRowCount();
        dtAdd.addNew("taripor");
        for (int n = 0; n < nRow; n++)
        {
          if ( jt.getValString(n,2).equals(""))
            continue;
          dtAdd.addNew();
          dtAdd.setDato("tra_codi",tra_codiE.getValorInt());
          dtAdd.setDato("tap_codi",tap_codiE.getValorInt());
          dtAdd.setDato("tap_nomb",tap_nombE.getText());
          dtAdd.setDato("tap_fecini",tap_feciniE.getText(),"dd-MM-yyyy");
          dtAdd.setDato("tap_fecfin",tap_fecfinE.getText(),"dd-MM-yyyy");
          dtAdd.setDato("tap_kilos",jt.getValorDec(n,0));
          dtAdd.setDato("tap_impor",jt.getValorDec(n,1));
          dtAdd.setDato("tap_fijkil", jt.getValString(n,2));
          dtAdd.update(stUp);
        }
        ctUp.commit();
        mensajeErr("Datos ... Guardados");
    }
    /**
     * Borrar datos
     * @param cllCodi Codigo de Clas.
     * @throws SQLException Error base de Datos
     * @throws ParseException Error al ejecutar select
     */
    void  borDatos(int tapCodi,int traCodi,String fecini,String fecfin) throws SQLException,java.text.ParseException
    {
      s = "DELETE FROM taripor " +
          " where tap_codi = "+tapCodi+
           " and tra_codi = "+traCodi+
           " and tap_fecini = TO_DATE('"+fecini+"','dd-MM-yyyy')"+
           " and tap_fecfin = TO_DATE('"+fecfin+"','dd-MM-yyyy')";

      stUp.executeUpdate(dtAdd.parseaSql(s));
    }

    void verDatos()
    {
      try
      {
        if (dtCons.getNOREG())
          return;

        tap_codiE.setValorInt(dtCons.getInt("tap_codi"));
        tra_codiE.setValorInt(dtCons.getInt("tra_codi"));
        tap_feciniE.setText(dtCons.getFecha("tap_fecini","dd-MM-yyyy"));
        tap_fecfinE.setText(dtCons.getFecha("tap_fecfin","dd-MM-yyyy"));

        verDatLin(tap_codiE.getValorInt(),tra_codiE.getValorInt(),tap_feciniE.getText(),tap_fecfinE.getText());
      } catch (Exception k)
      {
        Error("Error al ver datos",k);
        return;
      }
    }

    void verDatLin(int tapCodi,int traCodi,String fecini,String fecfin) throws Exception
    {
      s = "SELECT tap_nomb,tap_kilos, tap_impor,tap_fijkil FROM taripor " +
          " WHERE tra_codi = "+traCodi+
          " and tap_codi = "+tapCodi+
          " and tap_fecini = to_date('"+fecini+"','dd-MM-yyyy')"+
          " and tap_fecfin = to_date('"+fecfin+"','dd-MM-yyyy')"+
          " order by tap_kilos ";


      if (jt.isEnabled())
        jt.setEnabled(false);
      jt.removeAllDatos();
      if (! dtCon1.select(s))
      {
        mensajeErr("Registro BORRADO");
        tap_codiE.resetTexto();
        return;
      }
      tap_nombE.setText(dtCon1.getString("tap_nomb"));
      do
      {
        Vector v=new Vector();
        v.addElement(dtCon1.getString("tap_kilos"));
        v.addElement(dtCon1.getString("tap_impor"));
        v.addElement(dtCon1.getString("tap_fijkil"));//tap_fijkilE.getText(dtCon1.getString("tap_fijkil")));
        jt.addLinea(v);
      } while (dtCon1.next());

      jt.requestFocusInicio();
    }
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
      tap_codiE.setEnabled(act);
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
  public void PADQuery() {
    activar(navegador.QUERY, true);
    Pcabe.setQuery(true);
    Pcabe.resetTexto();
//    eje_numeE.setText(""+EU.ejercicio);
    tap_codiE.requestFocus();

  }

  public void ej_query1() {
    Baceptar.setEnabled(false);
    Vector v=new Vector();
    v.addElement(tap_codiE.getStrQuery());
    v.addElement(tap_nombE.getStrQuery());
    v.addElement(tra_codiE.getStrQuery());
    v.addElement(tap_feciniE.getStrQuery());
    v.addElement(tap_fecfinE.getStrQuery());
    Pcabe.setQuery(false);
    s = "SELECT tra_codi,tap_codi,tap_fecini,tap_fecfin from taripor ";

    s=creaWhere(s,v);
    s+=" group by tra_codi,tap_codi,tap_fecini,tap_fecfin " +
          " order by tra_codi,tap_codi,tap_fecini ";
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

  public void PADEdit() {
    mensaje("Editando ....");
//    ejeNume=eje_numeE.getValorInt();
    jt.cargaTodo();
    tapCodi=tap_codiE.getValorInt();
    traCodi=tra_codiE.getValorInt();
    fecini=tap_feciniE.getText();
    fecfin=tap_fecfinE.getText();
    activar(true);

    jt.requestFocusFinal();
    jt.mueveSigLinea(0);
  }
  public void ej_edit1() {
    jt.salirFoco();
    jt.procesaAllFoco();
    try {
      guardaDatos(tapCodi, traCodi, fecini, fecfin);
    } catch (Exception k)
    {
      Error("Error al Modificar Datos",k);
    }
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
  public void PADAddNew() {
    Pcabe.resetTexto();
    jt.removeAllDatos();
    activar(navegador.QUERY, true);
    Baceptar.setEnabled(false);
//    eje_numeE.setValorDec(EU.ejercicio);
    tap_codiE.requestFocus();
    mensaje("Insertando tarifa ....");
  }

  public void ej_addnew1() {
    jt.salirFoco();
    jt.procesaAllFoco();

    try {
      if (tap_codiE.getValorInt()==0)
      {
        s = "SELECT max(tap_codi) as tap_codi FROM taripor WHERE tra_codi = " + tra_codiE.getValorInt();
        dtStat.select(s);
        tra_codiE.setValorInt(dtStat.getInt("tap_codi", true) + 1);
      }
      guardaDatos(tap_codiE.getValorInt(), tra_codiE.getValorInt(), tap_feciniE.getText(), tap_fecfinE.getText());
    } catch (Exception k)
    {
      Error("Error al guardar Datos",k);
    }
    activaTodo();
    verDatos();

    mensaje("");
  }
  public void canc_addnew() {
    activaTodo();
    mensajeErr("Insercion de Datos ... Cancelada");
    verDatos();
    mensaje("");
  }
  public void PADDelete() {
    Baceptar.setEnabled(true);
    Bcancelar.setEnabled(true);
    Bcancelar.requestFocus();
    mensaje("Borrando ....");
  }
  public void ej_delete1() {
    try
    {
      borDatos(tap_codiE.getValorInt(),tra_codiE.getValorInt(),tap_feciniE.getText(),tap_fecfinE.getText());
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
  public void canc_delete() {
    activaTodo();
    verDatos();
    mensaje("");
    mensajeErr("Borrado de Registro ... ANULADO");
  }


}
