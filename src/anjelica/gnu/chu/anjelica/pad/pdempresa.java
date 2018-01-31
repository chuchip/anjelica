 package gnu.chu.anjelica.pad;

import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import java.sql.*;
import gnu.chu.Menu.*;
import gnu.chu.camposdb.empPanel;
import java.awt.*;
import java.util.*;
import gnu.chu.interfaces.*;
import gnu.chu.utilidades.ventanaPad;
import java.awt.event.KeyEvent;
import gnu.chu.sql.DatosTabla;
import gnu.chu.sql.vlike;
import java.net.UnknownHostException;

/**
 *
 * <p>Título: pdempresa </p>
 * <p>Descripción: Mantenimiento de Empresas</p>
 * <p>Empresa: miCasa</p>
 * <p>Copyright: Copyright (c) 2005-2018
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los términos de la Licencia Pública General de GNU segun es publicada por
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
 * @version 1.0
 */
public class pdempresa extends ventanaPad   implements PAD
{
  String s;
  CPanel Pprinc = new CPanel();
  boolean modConsulta=true;
//  CButton Baceptar = new CButton();
//  CButton Bcancelar = new CButton();
  CLabel emp_dirwebL=new CLabel("Dirección Web");
  CTextField emp_dirwebE = new CTextField(Types.CHAR,"X",100);
  CLabel emp_loclccL=new CLabel("N. Digitos Contabilidad");
  CTextField emp_loclccE = new CTextField(Types.DECIMAL,"#9");

  CTextField emp_codiE = new CTextField(Types.DECIMAL,"#9");
  CTextField emp_poblE = new CTextField(Types.CHAR,"X",30);
  CTextField emp_nombE = new CTextField(Types.CHAR,"X",40);
  CTextField emp_direE = new CTextField(Types.CHAR,"X",40);
  CTextField emp_codpoE = new CTextField(Types.DECIMAL,"####9");
  CTextField emp_telefE = new CTextField(Types.CHAR,"X",15);
  CTextField emp_faxE = new CTextField(Types.CHAR,"X",15);
  CTextField emp_nifE = new CTextField(Types.CHAR,"X",15);
  CTextField emp_nurgsaE = new CTextField(Types.CHAR,"X",15);
  CTextField emp_nomsocE = new CTextField(Types.CHAR,"X",30);
  CLinkBox pai_codiE = new CLinkBox();
  CTextField emp_orgconE = new CTextField(Types.CHAR,"X",15);
  CTextField emp_cercalE = new CTextField(Types.CHAR,"X",15);
  CTextField emp_labcalE = new CTextField(Types.CHAR,"X",10);
  CTextArea emp_obsfraE = new CTextArea();
  CTextArea emp_obsalbE = new CTextArea();
  CTextField emp_vetnomE = new CTextField(Types.CHAR,"X",50);
  CTextField emp_vetnumE = new CTextField(Types.CHAR,"X",15);
  CTextField emp_numexpE = new CTextField(Types.CHAR,"X",15);
  CTextField emp_codcomE = new CTextField(Types.DECIMAL,"#9");
  CTextField emp_codpviE = new CTextField(Types.DECIMAL,"#9");
  CComboBox emp_divimpE = new CComboBox();
  CComboBox emp_divexpE = new CComboBox();
  CTextField emp_dessprE = new CTextField(Types.CHAR,"X",50);
  CTextField emp_codediE = new CTextField(Types.CHAR,"X",15);
  CTextField emp_regmerE = new CTextField(Types.CHAR,"X",70);

  CLabel cLabel14 = new CLabel();
  CLabel cLabel1 = new CLabel();
  CLabel cLabel2 = new CLabel();
  CLabel cLabel3 = new CLabel();
  CLabel cLabel7 = new CLabel();
  CLabel cLabel5 = new CLabel();
  CLabel cli_nifL = new CLabel();
  CLabel cLabel4 = new CLabel();
  CLabel cLabel6 = new CLabel();
  CLabel cLabel8 = new CLabel();
  CLabel cLabel9 = new CLabel();
  CLabel cLabel10 = new CLabel();
  CLabel cLabel11 = new CLabel();
  CLabel cLabel12 = new CLabel();
  CLabel cLabel15 = new CLabel();
  CLabel cLabel16 = new CLabel();
  CLabel cLabel17 = new CLabel();
  CLabel cLabel18 = new CLabel();
  CLabel cLabel19 = new CLabel();
  CLabel cLabel110 = new CLabel();
  CLabel cLabel20 = new CLabel();
  CLabel cLabel21 = new CLabel();
  CLabel cLabel111 = new CLabel();
  CLabel cLabel112 = new CLabel();
  CLabel cLabel113 = new CLabel();
  CScrollPane jScrollPane1 = new CScrollPane();
  CScrollPane jScrollPane2 = new CScrollPane();


  public pdempresa(EntornoUsuario eu, Principal p)
  {
    this(eu, p, null);
  }

  public pdempresa(EntornoUsuario eu, Principal p,Hashtable ht)
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
          modConsulta = Boolean.valueOf(ht.get("modConsulta").toString());
      }
      setTitulo("Mantenimiento Empresas" );
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

  public pdempresa(gnu.chu.anjelica.menu p, EntornoUsuario eu,Hashtable ht)
  {
    EU = eu;
    vl = p.getLayeredPane();
    eje = false;

    try
    {
      if (ht != null)
      {
        if (ht.get("modConsulta") != null)
          modConsulta = Boolean.valueOf(ht.get("modConsulta").toString());
      }
      setTitulo("Mantenimiento Empresas");
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
    this.setSize(new Dimension(643, 455));
    this.setVersion("2018-01-28"+ (modConsulta ? "SOLO LECTURA" : ""));
    strSql = "SELECT * FROM empresa  order by emp_codi ";


    statusBar = new StatusBar(this);
    conecta();
    nav = new navegador(this, dtCons, false, modConsulta? navegador.CURYCON:navegador.NORMAL);
    iniciar(this);
    Pprinc.setDefButton(Baceptar);
    Pprinc.setEscButton(Bcancelar);
    Pprinc.setLayout(null);
    cLabel14.setText("Empresa");
    cLabel14.setBounds(new Rectangle(3, 2, 56, 17));

    emp_direE.setText("");
    emp_direE.setBounds(new Rectangle(57, 42, 293, 17));
    cLabel1.setText("Direccion");
    cLabel1.setBounds(new Rectangle(3, 42, 57, 17));
    cLabel2.setText("Poblacion");
    cLabel2.setBounds(new Rectangle(353, 42, 60, 17));
    cLabel3.setText("Codigo Postal");
    cLabel3.setBounds(new Rectangle(1, 61, 80, 17));
    emp_nombE.setText("");
    emp_nombE.setBounds(new Rectangle(102, 2, 293, 17));
    emp_poblE.setText("");
    emp_poblE.setBounds(new Rectangle(408, 42, 220, 17));
    cLabel7.setText("Fax");
    cLabel7.setBounds(new Rectangle(467, 61, 29, 17));
    cLabel5.setOpaque(false);
    cLabel5.setText("Telefono");
    cLabel5.setBounds(new Rectangle(157, 61, 53, 17));
    cli_nifL.setText("NIF");
    cli_nifL.setBounds(new Rectangle(466, 2, 32, 17));
    cLabel4.setText("NRGSA");
    cLabel4.setBounds(new Rectangle(484, 239, 45, 17));
    emp_nurgsaE.setText("");
    emp_nurgsaE.setBounds(new Rectangle(531, 239, 101, 17));
    cLabel6.setText("Nombre Social");
    cLabel6.setBounds(new Rectangle(3, 21, 80, 17));
    emp_nomsocE.setText("");
    emp_nomsocE.setBounds(new Rectangle(102, 21, 220, 17));
    cLabel8.setText("Pais");
    cLabel8.setBounds(new Rectangle(355, 21, 31, 17));
    pai_codiE.setAncTexto(40);
    pai_codiE.setFormato(Types.DECIMAL,"###9");
    pai_codiE.setBounds(new Rectangle(390, 21, 238, 17));
    cLabel9.setText("Org. Control");
    cLabel9.setBounds(new Rectangle(421, 82, 78, 17));
    cLabel9.setOpaque(false);
    cLabel10.setOpaque(false);
    cLabel10.setText("Cert.Calidad");
    cLabel10.setBounds(new Rectangle(2, 82, 78, 17));
    cLabel11.setText("Etiqu. Calidad");
    cLabel11.setBounds(new Rectangle(229, 82, 78, 17));
    cLabel11.setOpaque(false);
    emp_labcalE.setToolTipText("Etiqueta de Calidad");
    emp_labcalE.setText("");
    emp_labcalE.setBounds(new Rectangle(304, 82, 85, 17));
    cLabel12.setText("Observ. Fra");
    cLabel12.setBounds(new Rectangle(1, 103, 73, 17));
    emp_obsfraE.setText("");
    emp_obsalbE.setText("");
    cLabel15.setText("Observ. Alb");
    cLabel15.setBounds(new Rectangle(1, 163, 73, 17));
    cLabel16.setText("Nomb.Veter.");
    cLabel16.setBounds(new Rectangle(0, 219, 72, 17));
    emp_vetnomE.setToolTipText("Nombre de Veterinario");
    emp_vetnomE.setText("");
    emp_vetnomE.setBounds(new Rectangle(69, 219, 358, 17));
    cLabel17.setText("Num. Veter.");
    cLabel17.setBounds(new Rectangle(431, 219, 70, 17));
    emp_vetnumE.setToolTipText("Numero de Veterinario");
    emp_vetnumE.setBounds(new Rectangle(503, 219, 129, 17));
    emp_orgconE.setToolTipText("Organismo de Control");
    emp_orgconE.setBounds(new Rectangle(499, 82, 129, 17));
    emp_cercalE.setToolTipText("Certificado de Calidad");
    emp_cercalE.setText("");
    emp_cercalE.setBounds(new Rectangle(76, 82, 129, 17));
    emp_numexpE.setToolTipText("Numero de Explotacion");
    emp_numexpE.setBounds(new Rectangle(78, 239, 129, 17));
    cLabel18.setText("Num.Explot.");
    cLabel18.setBounds(new Rectangle(6, 239, 70, 17));
    cLabel19.setText("Comunidad");
    cLabel19.setBounds(new Rectangle(217, 239, 71, 17));
    cLabel110.setText("Provincia");
    cLabel110.setBounds(new Rectangle(347, 239, 60, 17));
    cLabel20.setToolTipText("Divisa de Importacion");
    cLabel20.setText("Divisa Imp.");
    cLabel20.setBounds(new Rectangle(6, 259, 63, 17));
    emp_divimpE.setToolTipText("Divisa de Importacion");
    emp_divimpE.setBounds(new Rectangle(75, 259, 233, 17));

    cLabel21.setText("Divisa Exp.");
    cLabel21.setBounds(new Rectangle(318, 259, 63, 17));
    cLabel21.setToolTipText("Divisa de Exportacion");

    emp_divexpE.setToolTipText("Divisa de Exportacion");
    emp_divexpE.setBounds(new Rectangle(383, 259, 249, 17));
    emp_dessprE.setToolTipText("Destinos Subproductos");
    emp_dessprE.setText("");
    emp_dessprE.setBounds(new Rectangle(93, 278, 358, 17));
    cLabel111.setToolTipText("Destinos Subproductos");
    cLabel111.setText("Dest. Subprod.");
    cLabel111.setBounds(new Rectangle(5, 278, 84, 17));
    emp_codediE.setToolTipText("Codigo EDI");
    emp_codediE.setBounds(new Rectangle(488, 278, 144, 17));
    cLabel112.setText("EDI");
    cLabel112.setBounds(new Rectangle(459, 278, 30, 17));
    emp_regmerE.setToolTipText("Registro Mercantil");

    emp_regmerE.setBounds(new Rectangle(118, 297, 514, 17));
    cLabel113.setText("Registro Mercantil");
    cLabel113.setBounds(new Rectangle(4, 297, 111, 17)); 
    emp_dirwebL.setBounds(new Rectangle(4, 315, 120, 17)); 
    emp_dirwebE.setBounds(new Rectangle(125, 315, 250, 17)); 
    emp_loclccL.setBounds(new Rectangle(390, 315, 135, 17)); 
    emp_loclccE.setBounds(new Rectangle(528, 315, 30, 17)); 
    Bcancelar.setBounds(new Rectangle(349, 347, 106, 27));
    Baceptar.setBounds(new Rectangle(191, 347, 106, 27));
    emp_codpviE.setBounds(new Rectangle(409, 239, 30, 17));
    emp_codcomE.setBounds(new Rectangle(287, 239, 30, 17));
    emp_faxE.setBounds(new Rectangle(499, 61, 129, 17));
    emp_telefE.setBounds(new Rectangle(215, 61, 129, 17));
    emp_codpoE.setBounds(new Rectangle(80, 61, 49, 17));
    emp_nifE.setBounds(new Rectangle(499, 2, 129, 17));
    emp_codiE.setBounds(new Rectangle(68, 2, 30, 17));
    jScrollPane1.setBounds(new Rectangle(76, 104, 550, 58));
    jScrollPane2.setBounds(new Rectangle(76, 163, 550, 53));
    this.getContentPane().add(nav, BorderLayout.NORTH);
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);

    Pprinc.add(cLabel6, null);
    Pprinc.add(emp_nifE, null);
    Pprinc.add(emp_codiE, null);
    Pprinc.add(emp_nombE, null);
    Pprinc.add(cLabel14, null);
    Pprinc.add(cli_nifL, null);
    Pprinc.add(emp_nomsocE, null);
    Pprinc.add(emp_direE, null);
    Pprinc.add(cLabel1, null);
    Pprinc.add(emp_poblE, null);
    Pprinc.add(cLabel2, null);
    Pprinc.add(emp_faxE, null);
    Pprinc.add(cLabel3, null);
    Pprinc.add(emp_codpoE, null);
    Pprinc.add(cLabel5, null);
    Pprinc.add(emp_telefE, null);
    Pprinc.add(cLabel7, null);
    Pprinc.add(emp_orgconE, null);
    Pprinc.add(emp_cercalE, null);
    Pprinc.add(cLabel10, null);
    Pprinc.add(emp_labcalE, null);
    Pprinc.add(cLabel11, null);
    Pprinc.add(cLabel9, null);
    Pprinc.add(cLabel12, null);
    Pprinc.add(pai_codiE, null);
    Pprinc.add(cLabel8, null);
    Pprinc.add(emp_dirwebL,null);
    Pprinc.add(emp_dirwebE,null);
    Pprinc.add(emp_loclccL,null);
    Pprinc.add(emp_loclccE,null);

    Pprinc.add(jScrollPane1, null);
    jScrollPane1.getViewport().add(emp_obsfraE, null);
    Pprinc.add(Bcancelar, null);
    Pprinc.add(emp_vetnumE, null);
    Pprinc.add(cLabel16, null);
    Pprinc.add(emp_vetnomE, null);
    Pprinc.add(cLabel17, null);
    Pprinc.add(emp_nurgsaE, null);
    Pprinc.add(emp_numexpE, null);
    Pprinc.add(cLabel18, null);
    Pprinc.add(emp_codpviE, null);
    Pprinc.add(cLabel4, null);
    Pprinc.add(cLabel110, null);
    Pprinc.add(cLabel19, null);
    Pprinc.add(emp_codcomE, null);
    Pprinc.add(emp_divexpE, null);
    Pprinc.add(cLabel21, null);
    Pprinc.add(cLabel20, null);
    Pprinc.add(emp_divimpE, null);
    Pprinc.add(emp_codediE, null);
    Pprinc.add(cLabel111, null);
    Pprinc.add(emp_dessprE, null);
    Pprinc.add(cLabel112, null);
    Pprinc.add(cLabel113, null);
    Pprinc.add(emp_regmerE, null);
    Pprinc.add(jScrollPane2, null);
    Pprinc.add(Baceptar, null);
    Pprinc.add(cLabel15, null);
    jScrollPane2.getViewport().add(emp_obsalbE, null);
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);

  }

  @Override
  public void iniciarVentana() throws Exception
  {

    Pprinc.setButton(KeyEvent.VK_F4,Baceptar);

    emp_codiE.setColumnaAlias("emp_codi");
    emp_poblE.setColumnaAlias("emp_pobl");
    emp_nombE.setColumnaAlias("emp_nomb");
    emp_direE.setColumnaAlias("emp_dire");
    emp_codpoE.setColumnaAlias("emp_codpo");
    emp_telefE.setColumnaAlias("emp_telef");
    emp_faxE.setColumnaAlias("emp_fax");
    emp_nifE.setColumnaAlias("emp_nif");
    emp_nurgsaE.setColumnaAlias("emp_nurgsa");
    emp_nomsocE.setColumnaAlias("emp_nomsoc");
    pai_codiE.setColumnaAlias("pai_codi");
    emp_orgconE.setColumnaAlias("emp_orgcon");
    emp_cercalE.setColumnaAlias("emp_cercal");
    emp_labcalE.setColumnaAlias("emp_labcal");
    emp_obsfraE.setColumnaAlias("emp_obsfra");
    emp_obsalbE.setColumnaAlias("emp_obsalb");
    emp_vetnomE.setColumnaAlias("emp_vetnom");
    emp_vetnumE.setColumnaAlias("emp_vetnum");
    emp_numexpE.setColumnaAlias("emp_numexp");
    emp_codcomE.setColumnaAlias("emp_codcom");
    emp_codpviE.setColumnaAlias("emp_codpvi");
    emp_divimpE.setColumnaAlias("emp_divimp");
    emp_divexpE.setColumnaAlias("emp_divexp");
    emp_dessprE.setColumnaAlias("emp_desspr");
    emp_codediE.setColumnaAlias("emp_codedi");
    emp_regmerE.setColumnaAlias("emp_regmer");
    emp_dirwebE.setColumnaAlias("emp_dirweb");
    emp_loclccE.setColumnaAlias("emp_loclcc");
    activarEventos();
    activaTodo();
    verDatos();
  }
  void activarEventos()
  {
  }

  void verDatos()
  {
    try {
      if (dtCons.getNOREG())
        return;

      emp_codiE.setValorInt(dtCons.getInt("emp_codi"));
      s="SELECT * FROM empresa WHERE emp_codi = "+emp_codiE.getValorInt();
      if (! dtCon1.select(s))
      {
        Pprinc.resetTexto();
        mensajeErr("EMPRESA NO ENCONTRADA ... SEGURAMENTE SE BORRO");
        emp_codiE.setValorInt(dtCons.getInt("emp_codi"));
        return;
      }
       emp_poblE.setText(dtCon1.getString("emp_pobl"));
       emp_nombE.setText(dtCon1.getString("emp_nomb"));
       emp_direE.setText(dtCon1.getString("emp_dire"));
       emp_codpoE.setText(dtCon1.getString("emp_codpo"));
       emp_telefE.setText(dtCon1.getString("emp_telef"));
       emp_faxE.setText(dtCon1.getString("emp_fax"));
       emp_nifE.setText(dtCon1.getString("emp_nif"));
       emp_nurgsaE.setText(dtCon1.getString("emp_nurgsa"));
       emp_nomsocE.setText(dtCon1.getString("emp_nomsoc"));
       pai_codiE.setText(dtCon1.getString("pai_codi"));
       emp_orgconE.setText(dtCon1.getString("emp_orgcon"));
       emp_cercalE.setText(dtCon1.getString("emp_cercal"));
       emp_labcalE.setText(dtCon1.getString("emp_labcal"));
       emp_obsfraE.setText(dtCon1.getString("emp_obsfra"));
       emp_obsalbE.setText(dtCon1.getString("emp_obsalb"));
       emp_vetnomE.setText(dtCon1.getString("emp_vetnom"));
       emp_vetnumE.setText(dtCon1.getString("emp_vetnum"));
       emp_numexpE.setText(dtCon1.getString("emp_numexp"));
       emp_codcomE.setText(dtCon1.getString("emp_codcom"));
       emp_codpviE.setText(dtCon1.getString("emp_codpvi"));
       emp_divimpE.setValor(dtCon1.getString("emp_divimp"));
       emp_divexpE.setValor(dtCon1.getString("emp_divexp"));
       emp_dessprE.setText(dtCon1.getString("emp_desspr"));
       emp_codediE.setText(dtCon1.getString("emp_codedi"));
       emp_regmerE.setText(dtCon1.getString("emp_regmer"));
       emp_dirwebE.setText(dtCon1.getString("emp_dirweb"));
       emp_loclccE.setValorInt(dtCon1.getInt("emp_loclcc"));
    } catch (Exception k)
    {
      Error("Error al ver Datos",k);
    }
  }

  @Override
  public void activar(boolean enab)
  {
    emp_codiE.setEnabled(enab);
    emp_poblE.setEnabled(enab);
    emp_nombE.setEnabled(enab);
    emp_direE.setEnabled(enab);
    emp_codpoE.setEnabled(enab);
    emp_telefE.setEnabled(enab);
    emp_faxE.setEnabled(enab);
    emp_nifE.setEnabled(enab);
    emp_nurgsaE.setEnabled(enab);
    emp_nomsocE.setEnabled(enab);
    pai_codiE.setEnabled(enab);
    emp_orgconE.setEnabled(enab);
    emp_cercalE.setEnabled(enab);
    emp_labcalE.setEnabled(enab);
    emp_obsfraE.setEnabled(enab);
    emp_obsalbE.setEnabled(enab);
    emp_vetnomE.setEnabled(enab);
    emp_vetnumE.setEnabled(enab);
    emp_numexpE.setEnabled(enab);
    emp_codcomE.setEnabled(enab);
    emp_codpviE.setEnabled(enab);
    emp_divimpE.setEnabled(enab);
    emp_divexpE.setEnabled(enab);
    emp_dessprE.setEnabled(enab);
    emp_codediE.setEnabled(enab);
    emp_regmerE.setEnabled(enab);
    emp_dirwebE.setEnabled(enab);
    emp_loclccE.setEnabled(enab);
    Baceptar.setEnabled(enab);
    Bcancelar.setEnabled(enab);
  }

  @Override
  public void afterConecta() throws SQLException, java.text.ParseException
  {
    pai_codiE.setAceptaNulo(false);

    s = "SELECT pai_codi,pai_nomb FROM v_paises ORDER BY pai_nomb";
    dtStat.select(s);
    pai_codiE.setFormato(Types.DECIMAL, "###9", 2);
    pai_codiE.addDatos(dtStat);
    s = "SELECT div_codi,div_nomb FROM v_divisa ORDER BY div_nomb";
    dtCon1.select(s);
    emp_divimpE.setDatos(dtCon1);
    dtCon1.first();
    emp_divexpE.setDatos(dtCon1);


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
  public void PADQuery()
  {
    activar(true);
    Pprinc.setQuery(true);
    Pprinc.resetTexto();
    emp_codiE.requestFocus();
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
    v.add(emp_codiE.getStrQuery());
    v.add(emp_poblE.getStrQuery());
    v.add(emp_nombE.getStrQuery());
    v.add(emp_direE.getStrQuery());
    v.add(emp_codpoE.getStrQuery());
    v.add(emp_telefE.getStrQuery());
    v.add(emp_faxE.getStrQuery());
    v.add(emp_nifE.getStrQuery());
    v.add(emp_nurgsaE.getStrQuery());
    v.add(emp_nomsocE.getStrQuery());
    v.add(pai_codiE.getStrQuery());
    v.add(emp_orgconE.getStrQuery());
    v.add(emp_cercalE.getStrQuery());
    v.add(emp_labcalE.getStrQuery());
    v.add(emp_obsfraE.getStrQuery());
    v.add(emp_obsalbE.getStrQuery());
    v.add(emp_vetnomE.getStrQuery());
    v.add(emp_vetnumE.getStrQuery());
    v.add(emp_numexpE.getStrQuery());
    v.add(emp_codcomE.getStrQuery());
    v.add(emp_codpviE.getStrQuery());
    v.add(emp_divimpE.getStrQuery());
    v.add(emp_divexpE.getStrQuery());
    v.add(emp_dessprE.getStrQuery());
    v.add(emp_codediE.getStrQuery());
    v.add(emp_regmerE.getStrQuery());
    v.add(emp_dirwebE.getStrQuery());
    v.add(emp_loclccE.getStrQuery());


    v.add(emp_codiE.getStrQuery());

    s = "SELECT * FROM empresa ";
    s = creaWhere(s, v,true);
    s+=" ORDER BY emp_codi ";
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
      fatalError("Error al buscar Empresas: ", ex);
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
    if (! checkRegistro())
      return;
    activar(true);
    emp_nombE.setEnabled(false);
    emp_codiE.setEnabled(false);
    emp_direE.requestFocus();
  }

  @Override
  public void ej_edit1()
  {
    try
    {
      dtAdd.edit();
      actValores(dtAdd);
      dtAdd.update(stUp);
      resetBloqueo(dtAdd, "empresa",emp_codiE.getText(),false);
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
    try {
        resetBloqueo(dtAdd, "empresa",emp_codiE.getText(),true);
    } catch (Exception ex)
    {
      Error("Error al Quitar Bloqueo", ex);
      return;
    }
    mensajeErr("Modificacion de Datos Cancelada");
    activaTodo();
    verDatos();
  }

  @Override
  public boolean checkEdit()
  {
    return checkAddNew();
  }

  @Override
  public void PADAddNew()
  {
    Pprinc.resetTexto();
    activar(true);
    emp_codiE.requestFocus();
    mensaje("Insertando Empresa ...");
  }
  
  @Override
  public boolean checkAddNew()
  {
    try {
      if (emp_codiE.isNull())
      {
        mensajeErr("Empresa NO VALIDA");
        return false;
      }

      if (emp_nombE.isNull())
      {
        mensajeErr("Introduzca Nombre de Empresa");
        emp_nombE.requestFocus();
        return false;
      }
      if (emp_direE.isNull())
      {
        mensajeErr("Direccion de Empresa .. NO VALIDA");
        emp_direE.requestFocus();
        return false;
      }
      if (emp_poblE.isNull())
      {
        mensajeErr("Introduzca Poblacion de empresa");
        emp_poblE.requestFocus();
        return false;
      }
      if (emp_nifE.isNull())
      {
        mensajeErr("Introduzca NIF");
        emp_nifE.requestFocus();
        return false;
      }
      if (pai_codiE.getError())
      {
        mensajeErr("Pais NO VALIDO");
        pai_codiE.requestFocus();
        return false;
      }
      if (emp_obsalbE.getText().length()>255)
      {
        msgBox("Observaciones albaran se truncara a 255 caracteres");
        emp_obsalbE.setText(emp_obsalbE.getText().substring(0,255));
      }
      if (emp_obsfraE.getText().length() > 255)
      {
        msgBox("Observaciones Factura se truncara a 255 caracteres");
        emp_obsfraE.setText(emp_obsfraE.getText().substring(0, 255));
      }

    } catch (Exception k)
    {
      Error("Error al comprobar campos",k);
      return false;
    }
    return true;
  }
  
  @Override
  public void ej_addnew1()
  {
    try
    {
      s="SELECT * FROM empresa WHERE emp_codi = "+emp_codiE.getValorInt();
      if (dtCon1.select(s))
      {
        mensajeErr("Empresa YA EXISTE");
        return;
      }
      dtAdd.addNew("empresa");
      dtAdd.setDato("emp_codi",emp_codiE.getText());
      actValores(dtAdd);
      dtAdd.update(stUp);
      ctUp.commit();
    }
    catch (Exception ex)
    {
      Error("Error al Insertar datos",ex);
      return;
    }
    mensaje("");
    mensajeErr("Empresa ... Insertada");

    activaTodo();
  }

  void actValores(DatosTabla dt) throws Exception
  {
    dt.setDato("emp_pobl",emp_poblE.getText());
    dt.setDato("emp_nomb",emp_nombE.getText());
    dt.setDato("emp_dire",emp_direE.getText());
    dt.setDato("emp_codpo",emp_codpoE.getText());
    dt.setDato("emp_telef",emp_telefE.getText());
    dt.setDato("emp_fax",emp_faxE.getText());
    dt.setDato("emp_nif",emp_nifE.getText());
    dt.setDato("emp_nurgsa",emp_nurgsaE.getText());
    dt.setDato("emp_nomsoc",emp_nomsocE.getText());
    dt.setDato("pai_codi",pai_codiE.getText());
    dt.setDato("emp_orgcon",emp_orgconE.getText());
    dt.setDato("emp_cercal",emp_cercalE.getText());
    dt.setDato("emp_labcal",emp_labcalE.getText());
    dt.setDato("emp_obsfra",emp_obsfraE.getText());
    dt.setDato("emp_obsalb",emp_obsalbE.getText());
    dt.setDato("emp_vetnom",emp_vetnomE.getText());
    dt.setDato("emp_vetnum",emp_vetnumE.getText());
    dt.setDato("emp_numexp",emp_numexpE.getText());
    dt.setDato("emp_codcom",emp_codcomE.getText());
    dt.setDato("emp_codpvi",emp_codpviE.getText());
    dt.setDato("emp_divimp",emp_divimpE.getValor());
    dt.setDato("emp_divexp",emp_divexpE.getValor());
    dt.setDato("emp_desspr",emp_dessprE.getText());
    dt.setDato("emp_codedi",emp_codediE.getText());
    dt.setDato("emp_regmer",emp_regmerE.getText());
    dt.setDato("emp_dirweb",emp_dirwebE.getText());
    dt.setDato("emp_loclcc",emp_loclccE.getValorInt());
    
  }
  
  @Override
  public void canc_addnew()
  {
    mensaje("");
    activaTodo();
    mensajeErr("Insercion de Datos Cancelada");
    verDatos();
  }

  private boolean checkRegistro()
  {
    try
    {
      if (!setBloqueo(dtAdd, "empresa", emp_codiE.getText() ))
      {
        msgBox(msgBloqueo);
        nav.pulsado = navegador.NINGUNO;
        mensaje("");
        activaTodo();
        return false;
      }
      if (!dtAdd.select("select * from empresa where emp_codi = " + emp_codiE.getValorInt(), true))
      {
        mensajeErr("Registro ha sido borrado");
        resetBloqueo(dtAdd, "empresa",emp_codiE.getText(),true);
        activaTodo();
        nav.pulsado = navegador.NINGUNO;
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
  
  @Override  
  public void PADDelete()
  {
    mensaje("Borrar Registro ...");

    if (!checkRegistro())
      return;
    Baceptar.setEnabled(true);
    Bcancelar.setEnabled(true);
    Bcancelar.requestFocus();
  }

  @Override
  public void ej_delete1()
  {
    try
    {
      if (EU.em_cod==emp_codiE.getValorInt())
      {
        mensajeErr("NO puede borrar la Empresa Actual del usuario");
        return;
      }

      dtAdd.delete(stUp);
      resetBloqueo(dtAdd,"empresa", emp_codiE.getText(),false);
      ctUp.commit();
      rgSelect();
    }
    catch (Exception ex)
    {
      Error("Error al borrar Registro",ex);
    }
    activaTodo();
    verDatos();
    mensaje("");
    mensajeErr("Registro ... Borrado");
  }
  
  @Override
  public void canc_delete() {
    mensaje("");
    activaTodo();
    try {
      resetBloqueo(dtAdd,"empresa", emp_codiE.getText(), true);
    } catch (Exception k)
    {
      Error("Error al Anular bloqueo sobre tabla Empresa",k);
    }
    mensajeErr("Borrado  de Datos Cancelada");
    verDatos();
  }
  /**
   * Devuelve un vlike con los datos de la empresa
   * @param dt DatosTabla
   * @param empCodi Codigo empresa
   * @return vlike con los datos de la empresa. Null si la empresda no existe.
   * @throws SQLException 
   */
  public static vlike getDatosEmpresa(DatosTabla dt, int empCodi) throws SQLException
  {
      String s="select * from v_empresa WHERE emp_codi = "+empCodi;
      vlike lkEmp=new vlike();
      if (! dt.selectInto(s,lkEmp))
          return null;
      else
          return lkEmp;
  }
 
  public static int getLongCuentaCliente(DatosTabla dt, int empCodi) throws SQLException
  {
      String s="select emp_loclcc from v_empresa WHERE emp_codi = "+empCodi;     
      if (! dt.select(s))
          return -1;
      else
          return dt.getInt("emp_loclcc");
  }
  public static int getPais(DatosTabla dt, int empCodi) throws SQLException
  {
      String s="select pai_codi from v_empresa WHERE emp_codi = "+empCodi;     
      if (!dt.select(s))
          return 0;
      else
          return dt.getInt("pai_codi");
  }
  /**
   * Busca el nombre de una empresa
   * @param dt  DatosTabla con conexion a DB 
   * @param empCodi Codigo empresa
   * @return Nombre d la empresa. Null si no la encuentra
   * @throws SQLException 
   */
   public static String getNombreEmpresa(DatosTabla dt, int empCodi) throws SQLException
  {
      String s="select emp_nomb from empresa WHERE emp_codi = "+empCodi;      
      if (! dt.select(s))
          return null;
      else
          return dt.getString("emp_nomb");
  }
  /**
   * Indica si una empresa existe en la tabla empresas.
   * @param dt
   * @param empCodi
   * @return false si no existe
   * @throws SQLException 
   */
  public static boolean checkEmpresa(DatosTabla dt,int empCodi) throws SQLException
  {
    String s="select * from empresa WHERE emp_codi = "+empCodi;
    return dt.select(s);
  }
  public static String getNumeroRegistroSanitario(DatosTabla dt,int empCodi) throws SQLException
  {
    String s="select emp_nurgsa from empresa WHERE emp_codi = "+empCodi;
    if (! dt.select(s))
        return null;
    else
        return dt.getString("emp_nurgsa");
  } 

  /**
   * Comprueba si un usuario puede acceder a los datos de otra empresa.
   * Para ello comprueba si la empresa esta agrupada con la mandada y
   * si el usuario tiene acceso a esta empresa (tabla usuarios)
   * @param dt DatosTabla
   * @param empCodi int
   * @param  login String Login de Usuario
   * @throws SQLException
   * @return String null si tiene acceso, en otro caso el mensaje de error.
   */
  public static String isValidEmpresa(DatosTabla dt,int empCodi,String login) throws SQLException
  {
    int res=empPanel.isValida(dt,empCodi,login);
    if (res==-1)
        return "Empresa: "+empCodi+" NO existe";
    if (res==0)
        return "Empresa "+empCodi+" NO existe o sin acceso";
    return null;
  }
}
