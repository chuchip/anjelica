package gnu.chu.anjelica.pad;

import gnu.chu.anjelica.menu;
import gnu.chu.controles.*;
import gnu.chu.interfaces.*;
import gnu.chu.utilidades.*;
import java.awt.*;
import java.awt.Rectangle;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.text.*;
import javax.swing.*;
import gnu.chu.Menu.*;
import gnu.chu.anjelica.listados.etiqueta;
import gnu.chu.camposdb.*;
import gnu.chu.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.border.*;
import javax.swing.event.*;

/**
 *
 * <p>Título: pdclien</p>
 * <p>Descripción: Mantenimiento de la Tabla de Clientes. Los cambios los guarda en
 * la tabla cliencamb </p>
* <p>Copyright: Copyright (c) 2005-2016
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
 * @author chuchiP
 * <p>Empresa: MISL</p>
 * @version 1.2 (Incluido campo de etiquetas de cliente)
 */
public class pdclien extends ventanaPad implements PAD
{
    public static final char EST_CONTACT='C';
    public static final char EST_NOCONT='N';
    public static final char EST_AUSENTE='A';
    public static final char EST_LLAMAR='L';
    public static final int LISTAR_ALB_VALORADOS=1;
    public static final int LISTAR_ALB_SINVALORAR=2;
    public static final int LISTAR_ALB_SINDEFINIR=0;
    String clc_comen;
    boolean chgCliente = true;
    String s;
    CTabbedPane Tpanel = new CTabbedPane();
    CLabel cli_comentL = new CLabel();
    CLabel cli_zonrepL = new CLabel();
    CTextField cli_poblE = new CTextField(Types.CHAR, "X", 30);
    CTextField cli_telconE = new CTextField(Types.CHAR, "X", 15);
    CTextField cli_codpoE = new CTextField(Types.CHAR, "X",8);
    CLabel cLabel1 = new CLabel();
    CLabel cLabel35 = new CLabel();
    CLabel cLabel3 = new CLabel();
    CComboBox cli_activE = new CComboBox();
    CLabel cLabel10 = new CLabel();
    CTextField cli_direcE = new CTextField(Types.CHAR, "X", 40);
    CTextField cli_direeE = new CTextField(Types.CHAR, "X", 40);
    CLabel cli_zoncreL = new CLabel();
    CLabel cli_direeL = new CLabel();
    CTextField cli_pobleE = new CTextField(Types.CHAR, "X", 30);
    CLabel cLabel4 = new CLabel();
    CLinkBox cli_zoncreE = new CLinkBox();
    CTextField cli_faxE = new CTextField(Types.CHAR, "X", 15);
    CLabel cLabel2 = new CLabel();
    CTextField cli_nombE = new CTextField(Types.CHAR, "X", 40);
    CLabel cLabel7 = new CLabel();
    CTextField cli_nomcoE = new CTextField(Types.CHAR, "X", 50);
    CPanel PdatGen = new CPanel();
    CLinkBox cli_zonrepE = new CLinkBox();
    CLabel cLabel6 = new CLabel();
    CTextField cli_perconE = new CTextField(Types.CHAR, "x", 30);
    CLabel cli_pobleL = new CLabel();
    CLabel cLabel33 = new CLabel();
    CTextField cli_telefE = new CTextField(Types.CHAR, "X", 25);
    CTextField cli_codiE = new CTextField(Types.DECIMAL, "####9");
    CLabel cLabel5 = new CLabel();
    CLabel cLabel11 = new CLabel();
    CLabel cli_nifL = new CLabel();
    CTextField cli_nifE = new CTextField(Types.CHAR, "X", 15);
    CPanel Pprinc = new CPanel();
    CPanel PdatEnv = new CPanel();
    CLabel cli_codfaL = new CLabel();
    cliPanel cli_codfaE = new cliPanel();
    CLabel cli_nomenL = new CLabel();
    CTextField cli_nomenE = new CTextField(Types.CHAR, "X", 50);
    CTextField cli_codpoeE = new CTextField(Types.CHAR, "X",8);
    CLabel cli_codpoeL = new CLabel();
    CPanel Pboton = new CPanel();
    CLabel cli_tipfacL = new CLabel();
    CComboBox cli_tipfacE = new CComboBox();
    CLabel fpa_codiL = new CLabel();
    fpaPanel fpa_codiE = new fpaPanel();
    CLabel cLabel16 = new CLabel();
    CTextField cli_dipa1E = new CTextField(Types.DECIMAL, "#9");
    CLabel cLabel17 = new CLabel();
    CTextField cli_dipa2E = new CTextField(Types.DECIMAL, "#9");
    CPanel PdatFra = new CPanel();
    CLabel ban_codiL = new CLabel();
    banPanel ban_codiE = new banPanel();
    CLabel cLabel12 = new CLabel();
    CTextField cli_baoficE = new CTextField(Types.DECIMAL, "9999");
    CLabel cLabel13 = new CLabel();
    CTextField cli_badicoE = new CTextField(Types.DECIMAL, "99");
    CLabel cLabel14 = new CLabel();
    CTextField cli_bacuenE = new CTextField(Types.DECIMAL, "9999999999");
    CLabel cli_dtoppL = new CLabel();
    CLabel cLabel18 = new CLabel();
    CComboBox cli_giroE = new CComboBox();
    CTextField cli_dtoppE = new CTextField(Types.DECIMAL, "#9.99");
    CLabel cli_comisL = new CLabel();
    CTextField cli_comisE = new CTextField(Types.DECIMAL, "#9.99");
    CLabel cli_dtootrL = new CLabel();
    CTextField cli_dtootrE = new CTextField(Types.DECIMAL, "#9.99");
    CLabel tar_codiL = new CLabel();
    CLinkBox tar_codiE = new CLinkBox();
    CLabel cli_albavlL = new CLabel();
    CComboBox cli_albvalE = new CComboBox();
    CLabel cli_recequL = new CLabel();
    CComboBox cli_recequE = new CComboBox();
    CComboBox cli_agralbE = new CComboBox();
    JScrollPane cli_comenS = new JScrollPane();
    JTextArea cli_comenT = new JTextArea();
    CLabel cli_riesgL = new CLabel();
    CTextField cli_riesgE = new CTextField(Types.DECIMAL, "--,---,--9.99");
    CComboBox cli_exeivaE = new CComboBox();
    CLabel cli_porivaL = new CLabel();
    CTextField cli_porivaE = new CTextField(Types.DECIMAL, "#9");
    CLabel cli_pdtocoL = new CLabel();
    CTextField cli_pdtocoE = new CTextField(Types.DECIMAL, "#9.99");
    CLabel cli_prapelL = new CLabel();
    CTextField cli_prapelE = new CTextField(Types.DECIMAL, "#9.99");
    CLabel cli_exeivaL = new CLabel();
    CLabel cli_agralbL = new CLabel();
    boolean modConsulta = true;
    CLabel cLabel15 = new CLabel();
    CLinkBox pai_codiE = new CLinkBox();
    CLabel ban_codiL1 = new CLabel();
    banPanel cli_baremeE = new banPanel();
    CLabel cli_vaccomL = new CLabel();
    CTextField cli_vaccomE = new CTextField(Types.DATE, "dd-MM-yyyy");
    CLabel cli_vacfinL = new CLabel();
    CTextField cli_vacfinE = new CTextField(Types.DATE, "dd-MM-yyyy");
    CPanel PdatCon = new CPanel();
    CTextField cue_codiE = new CTextField(Types.CHAR, "X", 12);
    CLabel cLabel21 = new CLabel();
    CLabel cLabel20 = new CLabel();
    CTextField cli_libivaE = new CTextField(Types.CHAR, "X", 2);
    CTextField cli_carteE = new CTextField(Types.CHAR, "X", 2);
    CLabel cLabel22 = new CLabel();
    CTextField cli_tipdocE = new CTextField(Types.CHAR, "X", 2);
    CLabel cLabel23 = new CLabel();
    CLabel cLabel24 = new CLabel();
    CTextField cli_diarioE = new CTextField(Types.CHAR, "X", 2);
    CTextField cli_sefacbE = new CTextField(Types.CHAR, "X", 1);
    CLabel cLabel25 = new CLabel();
    CComboBox cli_tipivaE = new CComboBox();
    CLabel cli_tipivaL1 = new CLabel();
    CLabel cLabel26 = new CLabel();
    CTextField cli_sitfacE = new CTextField(Types.CHAR, "X", 2);
    CLabel cLabel27 = new CLabel();
    CTextField cli_orgofiE = new CTextField(Types.DECIMAL, "###9");
    CLabel cLabel28 = new CLabel();
    CTextField cli_coimivE = new CTextField(Types.CHAR, "X", 1);
    CLabel cLabel29 = new CLabel();
    CTextField nRegSelE = new CTextField(Types.DECIMAL, "###,##9");
    CTextField nRegE = new CTextField(Types.DECIMAL, "###,##9");
    CLabel cLabel210 = new CLabel();
    CButton Bbusmax = new CButton("Max", Iconos.getImageIcon("pon"));
    CLabel cLabel30 = new CLabel();
    CLabel cli_fecaltL = new CLabel();
    CTextField cli_fecaltE = new CTextField(Types.DATE, "dd-MM-yyyy");
    CLabel cLabel32 = new CLabel();
    CTextField cli_feulmoE = new CTextField(Types.DATE, "dd-MM-yyyy");
    CLabel cLabel36 = new CLabel();
    empPanel emp_codiE = new empPanel();
    CTextField cli_telefeE = new CTextField(Types.CHAR, "X", 15);
    CTextField cli_faxeE = new CTextField(Types.CHAR, "X", 15);
    CLabel cLabel37 = new CLabel();
    CLabel cLabel38 = new CLabel();
    CLabel cli_codpoeL1 = new CLabel();
    CTextField cli_plzentE = new CTextField(Types.DECIMAL, "##9");
    CLabel div_codiL = new CLabel();
    CLinkBox div_codiE = new CLinkBox();
    CPanel Pcabe = new CPanel();
    CPanel Pdiscrim = new CPanel();
    TitledBorder titledBorder2;
    CLinkBox cli_disc1E = new CLinkBox();
    CLabel cli_disc1L = new CLabel();
    CLabel cli_disc2L = new CLabel();
    CLinkBox cli_disc2E = new CLinkBox();
    CLabel cli_disc3L = new CLabel();
    CLinkBox cli_disc3E = new CLinkBox();
    CLabel cli_disc4L = new CLabel();
    CLinkBox cli_disc4E = new CLinkBox();
    CPanel Phistor = new CPanel();
    Cgrid jt = new Cgrid(4);
    int clicodiAnt, empCodiAnt;
    CLabel cLabel8 = new CLabel();
    CComboBox cli_generE = new CComboBox();
    sbePanel sbe_codiE = new sbePanel();
    CLabel cLabel111 = new CLabel();
    CLabel cLabel9 = new CLabel();
    CComboBox cli_internE = new CComboBox();
    CLabel emp_nombL;
    CLabel sbe_nombL;
    CLabel eti_codiL = new CLabel();
    CLabel cli_horenvL = new CLabel("Horario");
    CTextField cli_horenvE = new CTextField(Types.CHAR,"X",50);
    CLabel cli_comenvL = new CLabel("Comentario");
    CTextField cli_comenvE = new CTextField(Types.CHAR,"X",80);

    CLinkBox eti_codiE = new CLinkBox();
    CLabel cli_precfiL = new CLabel();
    CComboBox cli_precfiE = new CComboBox();
    private CLabel zon_codiL = new CLabel();
    private CLinkBox zon_codiE = new CLinkBox();
    private CLabel rep_codiL = new CLabel();
     private CLinkBox rut_codiE = new CLinkBox();
    private CLabel rut_codiL = new CLabel("Ruta");
    private CLinkBox rep_codiE = new CLinkBox();
    private CLabel cli_feulveL = new CLabel();
    private CTextField cli_feulveE = new CTextField(Types.DATE, "dd-MM-yyyy");
    private CLabel cli_feulcoL = new CLabel();
    private CTextField cli_feulcoE = new CTextField(Types.DATE, "dd-MM-yyyy");
    private CLabel cli_estconL = new CLabel();
    private CComboBox cli_estconE = new CComboBox();
    private CLabel cli_email1L = new CLabel();
    private CTextField cli_email1E = new CTextField(Types.CHAR, "X", 60);
    private CLabel cli_email2L = new CLabel();
    private CTextField cli_email2E = new CTextField(Types.CHAR, "X", 60);
  
  /**
   * Constructor
   * @param eu EntornoUsuario Entorno Usuario
   * @param p Principal Clase del Menu
   */
  public pdclien(EntornoUsuario eu, Principal p)
  {
    this(eu,p,null);
  }
  public pdclien(EntornoUsuario eu, Principal p,Hashtable ht)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    setTitulo("Mantenimiento de Clientes");

    try
    {
      if (ht != null)
      {
        if (ht.get("modConsulta") != null)
          modConsulta = Boolean.valueOf(ht.get("modConsulta").toString());
      }

      if (jf.gestor.apuntar(this))
        jbInit();
      else
        setErrorInit(true);
    }
    catch (Exception e)
    {
      Logger.getLogger(pdclien.class.getName()).log(Level.SEVERE, null, e);
      setErrorInit(true);
    }
  }

  public pdclien(menu p, EntornoUsuario eu)
  {
    this(p,eu,null);
  }
  public pdclien(menu p, EntornoUsuario eu,Hashtable ht)
  {
    EU = eu;
    vl = p.getLayeredPane();
    setTitulo("Mantenimiento de Clientes");
    eje = false;

    try
    {
      if (ht != null)
      {
        if (ht.get("modConsulta") != null)
          modConsulta = Boolean.parseBoolean(ht.get("modConsulta").toString());
      }

      jbInit();
    }
    catch (Exception e)
    {
      Logger.getLogger(pdclien.class.getName()).log(Level.SEVERE, null, e);
      setErrorInit(true);
    }
  }
   public static String getNombreClase()
  {
   return "gnu.chu.anjelica.pad.pdclien";
  }
  public boolean inTransation()
  {
      return (nav.getPulsado()==navegador.ADDNEW || nav.getPulsado()==navegador.EDIT || nav.getPulsado()==navegador.DELETE);
  }
  public void setCliente(int cliCodi)
  {
      cli_codiE.setValorInt(cliCodi);
  }
  private void jbInit() throws Exception
  {
      titledBorder2 = new TitledBorder("");
      iniciarFrame();
      this.setSize(new Dimension(687, 496));
      this.setVersion("2016-11-09");
      strSql = "SELECT * FROM clientes where emp_codi = " + EU.em_cod
              + "ORDER BY cli_codi ";

      statusBar = new StatusBar(this);
      conecta();
      nav = new navegador(this, dtCons, false, modConsulta ? navegador.CURYCON : navegador.NORMAL);

      iniciar(this);
      cLabel11.setText("Nombre Fiscal");
      cLabel11.setBounds(new Rectangle(1, 2, 90, 16));
      cli_nifL.setText("NIF");
      cli_nifL.setBounds(new Rectangle(502, 5, 32, 17));
      Pprinc.setLayout(new BorderLayout());
      PdatGen.setLayout(null);
      PdatGen.setBorder(null);
      PdatEnv.setMaximumSize(new Dimension(32767, 32767));
      PdatEnv.setText("cPanel1");
      PdatEnv.setLayout(null);
      cli_codfaL.setText("Cliente a Facturar");
      cli_codfaL.setBounds(new Rectangle(1, 5, 101, 17));
      cli_nomenL.setText("Nombre Envio");
      cli_nomenL.setBounds(new Rectangle(5, 10, 83, 16));
      cli_codpoeL.setText("Cod Postal");
      cli_codpoeL.setBounds(new Rectangle(388, 31, 61, 17));
      Pboton.setMinimumSize(new Dimension(60, 30));
      Pboton.setPreferredSize(new Dimension(60, 30));
      Pboton.setLayout(null);
      Bcancelar.setBounds(new Rectangle(377, 1, 93, 22));
      Baceptar.setBounds(new Rectangle(274, 1, 98, 22));
      cli_tipfacL.setText("Tipo Facturacion");
      cli_tipfacL.setBounds(new Rectangle(2, 26, 92, 19));
      fpa_codiL.setText("Forma Pago");
      fpa_codiL.setBounds(new Rectangle(317, 51, 69, 17));
      cLabel16.setText("Dia Pago 1");
      cLabel16.setBounds(new Rectangle(2, 51, 59, 17));
      cLabel17.setText("Dia Pago 2");
      cLabel17.setBounds(new Rectangle(122, 51, 59, 17));
      ban_codiL.setText("Banco");
      ban_codiL.setBounds(new Rectangle(4, 72, 42, 17));
      cLabel12.setText("Oficina");
      cLabel12.setBounds(new Rectangle(309, 72, 49, 17));
      cli_baoficE.setBounds(new Rectangle(351, 72, 41, 17));
      cLabel13.setToolTipText("");
      cLabel13.setText("Dig. Control");
      cLabel13.setBounds(new Rectangle(395, 72, 73, 17));
      cli_badicoE.setBounds(new Rectangle(467, 72, 26, 17));
      cLabel14.setText("Num.Cuenta");
      cLabel14.setBounds(new Rectangle(505, 72, 75, 17));
      cli_bacuenE.setBounds(new Rectangle(583, 72, 85, 17));
      cli_dtoppL.setText("Dto. PP");
      cli_dtoppL.setBounds(new Rectangle(4, 95, 51, 18));
      cLabel18.setText("Giro");
      cLabel18.setBounds(new Rectangle(197, 122, 32, 16));
      cli_comisL.setToolTipText("");
      cli_comisL.setText("% Comision");
      cli_comisL.setBounds(new Rectangle(230, 95, 71, 18));
      cli_dtootrL.setText("Otros Dtos.");
      cli_dtootrL.setBounds(new Rectangle(104, 95, 67, 18));
      tar_codiL.setText("Tarifa");
      tar_codiL.setBounds(new Rectangle(1, 80, 39, 18));
      tar_codiE.setAceptaNulo(true);
      tar_codiE.setAncTexto(30);
      tar_codiE.setBounds(new Rectangle(64, 80, 212, 18));

      cli_albavlL.setText("Listar Albaran Valorado");
      cli_albavlL.setBounds(new Rectangle(312, 122, 132, 18));

      cli_recequL.setText("Rec. Equivalencia");
      cli_recequL.setBounds(new Rectangle(361, 156, 100, 18));
      cli_agralbE.setText("Agrupar Albaranes");
      cli_agralbE.setBounds(new Rectangle(104, 122, 68, 19));
      cli_riesgL.setText("Riesgo");
      cli_riesgL.setBounds(new Rectangle(539, 122, 43, 20));
      cli_exeivaE.setText("Exento de IVA");
      cli_exeivaE.setBounds(new Rectangle(599, 156, 68, 18));

      eti_codiL.setText("Etiquetas");
      eti_codiE.setAncTexto(40);
      eti_codiL.setBounds(new Rectangle(0, 150, 60, 18));
      eti_codiE.setBounds(new Rectangle(86, 150, 300, 18));
      cli_horenvL.setBounds(new Rectangle(0, 170, 60, 18));
      cli_horenvE.setBounds(new Rectangle(86, 170, 350, 18));
      cli_comenvL.setBounds(new Rectangle(0, 190, 70, 18));
      cli_comenvE.setBounds(new Rectangle(86, 190, 500, 18));

      cli_pdtocoL.setText("% Dto. Comercial");
      cli_pdtocoL.setBounds(new Rectangle(529, 95, 96, 18));
      cli_prapelL.setText("% Rappel");
      cli_prapelL.setBounds(new Rectangle(385, 95, 56, 18));
      cli_exeivaL.setText("Exento IVA");
      cli_exeivaL.setBounds(new Rectangle(526, 156, 67, 18));
      cli_agralbL.setText("Agrup. Albaranes");
      cli_agralbL.setBounds(new Rectangle(0, 122, 100, 20));
      cli_comenS.setBounds(new Rectangle(230, 60, 400, 70));
      cli_comentL.setBounds(new Rectangle(225, 40, 69, 14));
      cli_zoncreE.setBounds(new Rectangle(115, 171, 275, 18));
      cli_zoncreL.setBounds(new Rectangle(0, 171, 112, 18));
      cli_zonrepE.setBounds(new Rectangle(115, 150, 276, 19));

      cli_zonrepL.setBounds(new Rectangle(0, 150, 112, 18));
      cLabel35.setBounds(new Rectangle(1, 63, 65, 17));
      cli_perconE.setBounds(new Rectangle(261, 61, 223, 17));
      cLabel6.setBounds(new Rectangle(201, 61, 53, 17));
      cli_telconE.setBounds(new Rectangle(65, 61, 129, 17));
      cli_activE.setBounds(new Rectangle(615, 61, 53, 17));
      cLabel10.setBounds(new Rectangle(564, 61, 42, 17));
      cli_codpoE.setMayusc(true);
      cli_codpoE.setBounds(new Rectangle(65, 42, 120, 16));
      cLabel33.setBounds(new Rectangle(1, 42, 61, 16));
      cLabel5.setBounds(new Rectangle(262, 42, 32, 16));
      cli_telefE.setBounds(new Rectangle(294, 42, 189, 16));
      cLabel7.setBounds(new Rectangle(509, 42, 29, 16));
      
      cli_faxE.setBounds(new Rectangle(541, 42, 129, 16));
      cLabel3.setBounds(new Rectangle(1, 23, 59, 16));
      cli_poblE.setBounds(new Rectangle(65, 22, 227, 17));
      cli_poblE.setMayusc(true);
      cLabel4.setBounds(new Rectangle(296, 20, 59, 19));
      cli_direcE.setBounds(new Rectangle(355, 20, 316, 17));
      cli_direcE.setMayusc(true);
      cli_nomcoE.setBounds(new Rectangle(100, 1, 417, 17));
      cli_nomcoE.setMayusc(true);
      cLabel2.setBounds(new Rectangle(209, 3, 51, 16));
      cli_codiE.setBounds(new Rectangle(87, 3, 53, 16));
      cLabel1.setBounds(new Rectangle(2, 3, 83, 16));
      cli_nombE.setBounds(new Rectangle(256, 3, 417, 16));
      cli_nombE.setMayusc(true);
      cli_riesgE.setBounds(new Rectangle(584, 122, 83, 17));
      cli_albvalE.setBounds(new Rectangle(448, 122, 71, 18));
      cli_giroE.setBounds(new Rectangle(229, 122, 53, 18));
      cli_recequE.setBounds(new Rectangle(467, 156, 53, 18));
      cli_porivaE.setBounds(new Rectangle(204, 124, 38, 17));
      cli_pdtocoE.setBounds(new Rectangle(630, 95, 38, 18));
      cli_prapelE.setBounds(new Rectangle(441, 95, 38, 18));
      cli_comisE.setBounds(new Rectangle(306, 95, 38, 18));
      cli_dtootrE.setBounds(new Rectangle(174, 95, 38, 18));
      cli_dtoppE.setBounds(new Rectangle(52, 95, 38, 18));
      ban_codiE.setBounds(new Rectangle(47, 72, 257, 18));
      fpa_codiE.setBounds(new Rectangle(389, 51, 279, 17));
      cli_dipa2E.setBounds(new Rectangle(188, 51, 25, 17));
      cli_dipa1E.setBounds(new Rectangle(68, 51, 25, 17));
      cli_tipfacE.setBounds(new Rectangle(98, 26, 128, 20));
      cli_nifE.setBounds(new Rectangle(539, 5, 129, 17));
      cli_codfaE.setBounds(new Rectangle(99, 5, 351, 17));
      cli_direeL.setBounds(new Rectangle(4, 52, 55, 17));
      cli_direeE.setBounds(new Rectangle(86, 52, 311, 17));
      cli_pobleL.setBounds(new Rectangle(8, 31, 59, 17));
      cli_pobleE.setBounds(new Rectangle(86, 31, 227, 17));
      cli_codpoeE.setMayusc(true);
      cli_codpoeE.setBounds(new Rectangle(453, 31, 120, 17));
      cli_nomenE.setBounds(new Rectangle(86, 10, 417, 17));
      cLabel15.setText("Pais");
      cLabel15.setBounds(new Rectangle(0, 305, 35, 18));
      pai_codiE.setRequestFocusEnabled(true);
      pai_codiE.setAceptaNulo(true);
      pai_codiE.setAncTexto(45);
      pai_codiE.setBounds(new Rectangle(40, 305, 274, 18));
      ban_codiL1.setBounds(new Rectangle(0, 156, 93, 18));
      ban_codiL1.setText("Banco Remesas");
      cli_baremeE.setBounds(new Rectangle(98, 156, 257, 18));

      cli_vaccomL.setText("Fec. Ini Vacaciones");
      cli_vaccomL.setBounds(new Rectangle(375, 5, 111, 18));
      cli_vaccomE.setBounds(new Rectangle(550, 5, 80, 18));
      cli_vacfinL.setBounds(new Rectangle(375, 25, 111, 18));
      cli_vacfinL.setText("Fec. Ini Vacaciones");
      cli_vacfinE.setBounds(new Rectangle(550, 25, 80, 18));
      PdatCon.setLayout(null);
      cue_codiE.setBounds(new Rectangle(102, 7, 95, 19));
      cLabel21.setBounds(new Rectangle(2, 7, 99, 19));
      cLabel21.setText("Cuenta Contable");
      cLabel20.setText("Libro IVA");
      cLabel20.setBounds(new Rectangle(2, 28, 90, 19));
      cli_libivaE.setBounds(new Rectangle(102, 28, 35, 19));
      cli_carteE.setBounds(new Rectangle(101, 221, 35, 19));
      cLabel22.setBounds(new Rectangle(3, 221, 71, 19));
      cLabel22.setText("Cartera");
      cli_tipdocE.setBounds(new Rectangle(102, 48, 35, 19));
      cLabel23.setText("Tipo Documento");
      cLabel23.setBounds(new Rectangle(2, 48, 94, 19));
      cLabel24.setBounds(new Rectangle(2, 68, 53, 19));
      cLabel24.setText("Diario");
      cli_diarioE.setBounds(new Rectangle(102, 68, 35, 19));
      cli_diarioE.setText("");
      cli_sefacbE.setText("");
      cli_sefacbE.setBounds(new Rectangle(102, 89, 35, 19));
      cLabel25.setText("Serie Fra. en Ctb");
      cLabel25.setBounds(new Rectangle(2, 89, 97, 19));
      cli_tipivaE.setBounds(new Rectangle(102, 111, 74, 19));
      cli_tipivaL1.setBounds(new Rectangle(2, 111, 58, 19));
      cli_tipivaL1.setText("Tipo IVA");
      cli_porivaE.setBounds(new Rectangle(102, 132, 38, 19));
      cli_porivaL.setBounds(new Rectangle(2, 132, 55, 19));
      cli_porivaL.setText("% de IVA");
      cLabel26.setText("Situacion Fras.");
      cLabel26.setBounds(new Rectangle(2, 153, 91, 19));
      cli_sitfacE.setBounds(new Rectangle(102, 153, 35, 19));
      cli_sitfacE.setText("");
      cLabel27.setText("Organismo Oficial");
      cLabel27.setBounds(new Rectangle(2, 174, 105, 19));
      cli_orgofiE.setBounds(new Rectangle(102, 174, 37, 19));
      cLabel28.setText("Codigo Impuesto");
      cLabel28.setBounds(new Rectangle(2, 196, 98, 19));
      cli_coimivE.setBounds(new Rectangle(102, 196, 35, 19));
      cli_coimivE.setText("");
      cLabel29.setText("Num. Reg. Selecionados");
      cLabel29.setBounds(new Rectangle(3, 6, 135, 18));
      nRegSelE.setEnabled(false);
      nRegSelE.setBounds(new Rectangle(135, 6, 56, 18));
      nRegE.setBounds(new Rectangle(549, 6, 56, 18));
      nRegE.setEnabled(false);
      cLabel210.setBounds(new Rectangle(487, 6, 66, 18));
      cLabel210.setText("Num. Reg.");
      Bbusmax.setBounds(new Rectangle(142, 3, 61, 20));
      Bbusmax.setToolTipText("Buscar Cliente Mayor en este rango");
      Bbusmax.setMargin(new Insets(0, 0, 0, 0));
      Bbusmax.setFocusable(false);
      cli_fecaltL.setText("Fecha Alta");
      cli_fecaltL.setBounds(new Rectangle(412, 256, 63, 18));
      cli_fecaltE.setBounds(new Rectangle(587, 256, 80, 18));
      cLabel32.setBounds(new Rectangle(411, 278, 83, 18));
      cLabel32.setText("Fecha Ult. Mod.");
      cli_feulmoE.setBounds(new Rectangle(587, 278, 80, 18));
      cLabel36.setRequestFocusEnabled(true);
      cLabel36.setText("Empresa");
      cLabel36.setBounds(new Rectangle(280, 80, 54, 18));

      emp_codiE.setBounds(new Rectangle(330, 80, 42, 18));
      cli_telefeE.setBounds(new Rectangle(86, 76, 129, 17));
      cli_faxeE.setBounds(new Rectangle(374, 76, 129, 17));
      cLabel37.setText("Fax");
      cLabel37.setBounds(new Rectangle(342, 76, 29, 18));
      cLabel38.setText("Telef. Cto.");
      cLabel38.setBounds(new Rectangle(1, 76, 65, 17));
      cli_codpoeL1.setBounds(new Rectangle(2, 100, 82, 17));
      cli_codpoeL1.setText("Plazo Entrega");
      cli_plzentE.setBounds(new Rectangle(88, 100, 50, 17));
      div_codiL.setBounds(new Rectangle(392, 171, 49, 18));
      div_codiL.setText("Divisa");
      div_codiL.setRequestFocusEnabled(true);
      div_codiE.setBounds(new Rectangle(443, 171, 224, 18));
      div_codiE.setAncTexto(30);
      Pcabe.setBorder(BorderFactory.createRaisedBevelBorder());
      Pcabe.setMaximumSize(new Dimension(24, 24));
      Pcabe.setMinimumSize(new Dimension(24, 24));
      Pcabe.setPreferredSize(new Dimension(24, 24));
      Pcabe.setLayout(null);
      Pdiscrim.setBorder(titledBorder2);

      Pdiscrim.setBounds(new Rectangle(230, 140, 407, 106));
      Pdiscrim.setLayout(null);

      titledBorder2.setTitle("Discriminadores");
      cli_disc1E.setAncTexto(30);
      cli_disc1E.setBounds(new Rectangle(124, 20, 276, 19));
      cli_disc1L.setText("Discriminador 1.");
      cli_disc1L.setBounds(new Rectangle(9, 20, 112, 19));
      cli_disc2L.setText("Discriminador 2");
      cli_disc2L.setBounds(new Rectangle(9, 40, 112, 19));
      cli_disc2E.setAncTexto(30);
      cli_disc2E.setBounds(new Rectangle(124, 40, 276, 19));
      cli_disc3L.setText("Discriminador 3");
      cli_disc3L.setBounds(new Rectangle(10, 61, 112, 19));
      cli_disc3E.setAncTexto(30);
      cli_disc3E.setBounds(new Rectangle(124, 61, 276, 19));
      cli_disc4L.setText("Discriminador 4");
      cli_disc4L.setBounds(new Rectangle(10, 81, 112, 19));

      cli_disc4E.setAncTexto(30);
      cli_disc4E.setBounds(new Rectangle(124, 81, 276, 19));

      Phistor.setLayout(null);
      jt.setBounds(new Rectangle(8, 10, 662, 310));
      cLabel8.setRequestFocusEnabled(true);
      cLabel8.setToolTipText("Cliente Generico");
      cLabel8.setText("Generico");
      cLabel8.setBounds(new Rectangle(555, 1, 55, 17));

      cli_generE.setBounds(new Rectangle(613, 1, 55, 17));
      sbe_codiE.setBounds(new Rectangle(445, 151, 43, 18));

      cLabel111.setBounds(new Rectangle(392, 151, 53, 18));
      cLabel111.setText("SubEmp.");
      cLabel111.setToolTipText("SubEmpresa");
      cLabel9.setText("Cliente Interno");
      cLabel9.setBounds(new Rectangle(408, 302, 106, 19));
      cli_internE.setBounds(new Rectangle(614, 300, 53, 17));




      cli_precfiL.setText("Revisar Precios");
      cli_precfiL.setBounds(new Rectangle(259, 100, 105, 15));
      cli_precfiE.setBounds(new Rectangle(374, 100, 70, 16));

      zon_codiL.setText("Zona");
      zon_codiL.setBounds(new Rectangle(0, 100, 40, 20));
      zon_codiE.setBounds(new Rectangle(65, 100, 325, 20));
      zon_codiE.setAncTexto(30);
      rut_codiE.setAncTexto(30);
      rep_codiL.setText("Represent");
      rep_codiL.setBounds(new Rectangle(0, 125, 65, 20));
      rep_codiE.setBounds(new Rectangle(65, 125, 325, 20));
      rep_codiE.setAncTexto(30);
      cli_feulveL.setText("Fec. Ult. Venta");
      cli_feulveL.setBounds(new Rectangle(415, 205, 90, 20));
      cli_feulveE.setBounds(new Rectangle(585, 205, 80, 20));
      cli_feulveE.setEnabled(false);
      cli_feulcoL.setText("Fec.Ult.Contacto");
      cli_feulcoL.setBounds(new Rectangle(445, 100, 110, 20));
      cli_feulcoE.setBounds(new Rectangle(585, 100, 80, 20));
      cli_feulcoE.setEnabled(false);
      cli_estconL.setText("Estado Contacto");
      cli_estconL.setBounds(new Rectangle(445, 125, 110, 20));
      cli_estconE.setBounds(new Rectangle(565, 125, 100, 20));
      cli_estconE.setEnabled(false);
      cli_email1L.setText("Email Contacto");
      cli_email1L.setBounds(new Rectangle(1, 195, 110, 18));
      cli_email1E.setBounds(new Rectangle(115, 195, 275, 18));
      cli_email2L.setText("Email Administ.");
      cli_email2L.setBounds(new Rectangle(1, 220, 110, 18));
      cli_email2E.setBounds(new Rectangle(115, 220, 275, 18));
      rut_codiL.setBounds(new Rectangle(1, 240, 110, 18));
      rut_codiE.setBounds(new Rectangle(115, 240, 275, 18));
      llenaEstCont(cli_estconE);
      this.getContentPane().add(Pprinc, BorderLayout.CENTER);
      PdatFra.setLayout(null);

      Baceptar.setMargin(new Insets(0, 0, 0, 0));
      cLabel7.setText("Fax");
      cLabel2.setText("Nombre");
      cli_zoncreE.setAncTexto(30);
      cLabel4.setText("Direccion");
      Bcancelar.setMargin(new Insets(0, 0, 0, 0));
      cli_direeL.setText("Dir.Envio");
      cli_zoncreL.setText("Zona/Credito");
      cLabel10.setText("Activo");
      cLabel3.setText("Poblacion");
      cLabel35.setText("Telef. Cto.");
      cLabel1.setText("Codigo Cliente");
      cli_zonrepL.setText("Zona / Represent.");
      cli_comentL.setText("Comentario");

      Pcabe.setButton(KeyEvent.VK_F4, Baceptar);
      PdatGen.setButton(KeyEvent.VK_F4, Baceptar);
      PdatFra.setButton(KeyEvent.VK_F4, Baceptar);
      PdatEnv.setButton(KeyEvent.VK_F4, Baceptar);

      cli_zonrepE.setAncTexto(30);
      cLabel6.setText(" Contacto");
      cli_pobleL.setText("Pobl. Envio");
      cLabel33.setText("Cod Postal");
      cLabel5.setText("Telef.");

      PdatEnv.add(cli_nomenE, null);
      PdatEnv.add(cli_nomenL, null);
      PdatEnv.add(cli_pobleL, null);
      PdatEnv.add(cli_pobleE, null);
      PdatEnv.add(cli_direeL, null);
      PdatEnv.add(cli_codpoeE, null);
      PdatEnv.add(cli_codpoeL, null);
      PdatEnv.add(cli_direeE, null);
      PdatEnv.add(cli_telefeE, null);
      PdatEnv.add(cLabel38, null);
      PdatEnv.add(cLabel37, null);
      PdatEnv.add(cli_faxeE, null);
      PdatEnv.add(eti_codiL, null);
      PdatEnv.add(eti_codiE, null);
      PdatEnv.add(cli_horenvL, null);
      PdatEnv.add(cli_horenvE, null);
      PdatEnv.add(cli_comenvL, null);
      PdatEnv.add(cli_comenvE, null);

      Tpanel.add(PdatGen, "General");
      Tpanel.add(PdatFra, "Facturacion");
      Tpanel.add(PdatEnv, "Envio");
      Tpanel.add(PdatCon, "Varios");

      Pboton.add(Bcancelar, null);
      Pboton.add(Baceptar, null);
      Pboton.add(cLabel29, null);
      Pboton.add(nRegSelE, null);
      Pboton.add(cLabel210, null);
      Pboton.add(nRegE, null);
      Pprinc.add(Pcabe, BorderLayout.NORTH);
      Pcabe.add(cli_codiE, null);
      Pcabe.add(cLabel1, null);
      Pcabe.add(Bbusmax, null);
      Pcabe.add(cli_nombE, null);
      Pcabe.add(cLabel2, null);
      Pprinc.add(Tpanel, BorderLayout.CENTER);
      Pprinc.add(Pboton, BorderLayout.SOUTH);

      PdatFra.add(cli_codfaE, null);
      PdatFra.add(cli_codfaL, null);
      PdatFra.add(cli_tipfacE, null);
      PdatFra.add(cli_tipfacL, null);
      PdatFra.add(cLabel16, null);
      PdatFra.add(cli_dipa1E, null);
      PdatFra.add(cLabel17, null);
      PdatFra.add(cli_dipa2E, null);
      PdatFra.add(fpa_codiE, null);
      PdatFra.add(cli_nifE, null);
      PdatFra.add(cli_nifL, null);
      PdatFra.add(fpa_codiL, null);
      PdatFra.add(cli_bacuenE, null);
      PdatFra.add(ban_codiE, null);
      PdatFra.add(cLabel12, null);
      PdatFra.add(cli_baoficE, null);
      PdatFra.add(cLabel13, null);
      PdatFra.add(cli_badicoE, null);
      PdatFra.add(cLabel14, null);
      PdatFra.add(ban_codiL, null);
      PdatFra.add(cli_comisE, null);
      PdatFra.add(cli_dtoppL, null);
      PdatFra.add(cli_dtoppE, null);
      PdatFra.add(cli_dtootrL, null);
      PdatFra.add(cli_dtootrE, null);
      PdatFra.add(cli_comisL, null);
      PdatFra.add(cli_pdtocoE, null);
      PdatFra.add(cli_prapelE, null);
      PdatFra.add(cli_prapelL, null);
      PdatFra.add(cli_pdtocoL, null);
      PdatFra.add(cli_agralbE, null);
      PdatFra.add(cli_giroE, null);
      PdatFra.add(cli_riesgE, null);
      PdatFra.add(cli_agralbL, null);
      PdatFra.add(cli_albvalE, null);
      PdatFra.add(cli_albavlL, null);
      PdatFra.add(cLabel18, null);
      PdatFra.add(cli_riesgL, null);
      PdatFra.add(cli_baremeE, null);
      PdatFra.add(ban_codiL1, null);
      PdatFra.add(cli_recequE, null);
      PdatFra.add(cli_recequL, null);
      PdatFra.add(cli_exeivaE, null);
      PdatFra.add(cli_exeivaL, null);


      PdatCon.add(cLabel21, null);
      PdatCon.add(cue_codiE, null);
      PdatCon.add(cli_libivaE, null);
      PdatCon.add(cLabel20, null);
      PdatCon.add(cli_tipdocE, null);
      PdatCon.add(cLabel23, null);
      PdatCon.add(cli_diarioE, null);
      PdatCon.add(cLabel24, null);
      PdatCon.add(cli_sefacbE, null);
      PdatCon.add(cLabel25, null);
      PdatCon.add(cli_porivaE, null);
      PdatCon.add(cli_porivaL, null);
      PdatCon.add(cli_sitfacE, null);
      PdatCon.add(cLabel26, null);
      PdatCon.add(cli_orgofiE, null);
      PdatCon.add(cLabel27, null);
      PdatCon.add(cli_coimivE, null);
      PdatCon.add(cLabel28, null);
      PdatCon.add(cLabel22, null);
      PdatCon.add(cli_carteE, null);
      PdatCon.add(cli_tipivaE, null);
      PdatCon.add(cli_tipivaL1, null);
      PdatCon.add(cli_vaccomL, null);
        PdatCon.add(Pdiscrim, null);
        Tpanel.add(Phistor, "Hist. Cambios");
        PdatEnv.add(cli_plzentE, null);
        PdatEnv.add(cli_codpoeL1, null);
        PdatEnv.add(cli_precfiL, null);
        PdatEnv.add(cli_precfiE, null);
        Pdiscrim.add(cli_disc1E, null);
        Pdiscrim.add(cli_disc1L, null);
        Pdiscrim.add(cli_disc2E, null);
        Pdiscrim.add(cli_disc2L, null);
        Pdiscrim.add(cli_disc3E, null);
        Pdiscrim.add(cli_disc3L, null);
        Pdiscrim.add(cli_disc4E, null);
        Pdiscrim.add(cli_disc4L, null);
        sbe_nombL.setBounds(new Rectangle(491, 151, 174, 18));
        PdatGen.add(cli_email2E, null);
        PdatGen.add(cli_email2L, null);
        PdatGen.add(rut_codiL, null);
        PdatGen.add(rut_codiE, null);
        
        PdatGen.add(cli_email1E, null);
        PdatGen.add(cli_email1L, null);
        PdatGen.add(cli_estconE, null);
        PdatGen.add(cli_estconL, null);
        PdatGen.add(cli_feulcoE, null);
        PdatGen.add(cli_feulcoL, null);
        PdatGen.add(cli_feulveE, null);
        PdatGen.add(cli_feulveL, null);
        PdatGen.add(rep_codiE, null);
        PdatGen.add(rep_codiL, null);
        PdatGen.add(zon_codiE, null);
        PdatGen.add(zon_codiL, null);
        PdatGen.add(sbe_nombL, null);
        PdatGen.add(sbe_codiE, null);
        PdatGen.add(cLabel111, null);
        PdatGen.add(emp_codiE, null);
        PdatGen.add(cLabel36, null);
        PdatGen.add(emp_nombL, null);
        PdatGen.add(cli_nomcoE, null);
        PdatGen.add(cLabel9, null);
        PdatGen.add(cli_internE, null);
        PdatGen.add(pai_codiE, null);
        PdatGen.add(cLabel15, null);
        PdatGen.add(tar_codiE, null);
        PdatGen.add(cli_generE, null);
        PdatGen.add(cLabel8, null);
        PdatGen.add(cli_fecaltE, null);
        PdatGen.add(cli_fecaltL, null);
        PdatGen.add(cLabel32, null);
        PdatGen.add(cli_feulmoE, null);
        PdatGen.add(cli_direcE, null);
        PdatGen.add(cli_poblE, null);
        PdatGen.add(cLabel4, null);
        PdatGen.add(cli_faxE, null);
        PdatGen.add(cLabel5, null);
        PdatGen.add(cli_telefE, null);
        PdatGen.add(cLabel7, null);
        PdatGen.add(cli_codpoE, null);
        PdatGen.add(cli_telconE, null);
        PdatGen.add(cli_perconE, null);
        PdatGen.add(cLabel6, null);
        PdatGen.add(cLabel10, null);
        PdatGen.add(cli_activE, null);
        PdatGen.add(cLabel11, null);
        PdatGen.add(cLabel3, null);
        PdatGen.add(cLabel33, null);
        PdatGen.add(cLabel35, null);
        PdatGen.add(tar_codiL, null);
        PdatGen.add(cli_zonrepE, null);
        PdatGen.add(cli_zonrepL, null);
        PdatGen.add(cli_zoncreL, null);
        PdatGen.add(cli_zoncreE, null);
        PdatGen.add(div_codiL, null);
        PdatGen.add(div_codiE, null);
        PdatCon.add(cli_vacfinL, null);
      PdatCon.add(cli_vaccomE, null);
      PdatCon.add(cli_vacfinE, null);
      PdatCon.add(cli_comenS, null);
        PdatCon.add(cli_comentL, null);
        cli_comenS.getViewport().add(cli_comenT, null);
      emp_nombL.setBounds(new Rectangle(382, 81, 285, 19));
      Phistor.add(jt, null);

      Baceptar.setText("Aceptar F4");
      confGrid();
  }

    @Override
  public void afterConecta() throws SQLException
  {
    emp_codiE.iniciar(dtStat, this, vl, EU);
    sbe_codiE.iniciar(dtStat, this, vl, EU);
    sbe_codiE.setFieldEmpCodi(emp_codiE.getTextField());
    emp_nombL =emp_codiE.creaLabelEmp();
    emp_codiE.setLabelEmp(emp_nombL);
    sbe_nombL=sbe_codiE.creaLabelSbe();
    sbe_codiE.setLabelSbe(sbe_nombL);
  }
  public void iniciarVentana() throws Exception
  {
    cli_precfiE.addItem("No","0");
    cli_precfiE.addItem("Si","1");

    eti_codiE.addDatos(etiqueta.getReports(dtStat,EU.em_cod,1));
    eti_codiE.setFormato(Types.DECIMAL,"###9",4);

    s = "SELECT div_codi,div_nomb FROM v_divisa order by div_nomb";
    dtCon1.select(s);
    div_codiE.addDatos(dtCon1);
    div_codiE.setFormato(Types.DECIMAL, "##9");
    div_codiE.setAceptaNulo(false);

    cli_codfaE.iniciar(dtStat,this,vl,EU);
    fpa_codiE.iniciar(dtStat,this,vl,EU);
    ban_codiE.iniciar(dtStat,this,vl,EU);
    cli_baremeE.iniciar(dtStat,this,vl,EU);
    cli_disc1L.setText(pdconfig.getNombreDiscr(EU.em_cod,"C1",dtStat));
    cli_disc2L.setText(pdconfig.getNombreDiscr(EU.em_cod,"C2",dtStat));
    cli_disc3L.setText(pdconfig.getNombreDiscr(EU.em_cod,"C3",dtStat));
    cli_disc4L.setText(pdconfig.getNombreDiscr(EU.em_cod,"C4",dtStat));

    zon_codiE.setFormato(Types.CHAR,"XX");
    rep_codiE.setFormato(Types.CHAR,"XX");
    rut_codiE.setFormato(Types.CHAR,"XX");
    cli_zoncreE.setFormato(Types.CHAR,"XX");
    cli_zonrepE.setFormato(Types.CHAR,"XX");
    cli_disc1E.setFormato(Types.CHAR,"XX");
    cli_disc2E.setFormato(Types.CHAR,"XX");
    cli_disc3E.setFormato(Types.CHAR,"XX");
    cli_disc4E.setFormato(Types.CHAR,"XX");
    llenaDiscr(cli_zonrepE,"CR");
    llenaDiscr(cli_zoncreE,"CC");
    
    llenaDiscr(zon_codiE,pdconfig.D_ZONA);
    llenaDiscr(rut_codiE,pdconfig.D_RUTAS);
    MantRepres.llenaLinkBox(rep_codiE,dtCon1);
//    llenaDiscr(rep_codiE,"Cr");

    llenaDiscr(cli_disc1E,"C1");
    llenaDiscr(cli_disc2E,"C2");
    llenaDiscr(cli_disc3E,"C3");
    llenaDiscr(cli_disc4E,"C4");

    cli_activE.addItem("Si","S");
    cli_activE.addItem("No","N");


    cli_internE.addItem("No","0");
    cli_internE.addItem("Si","1");

    cli_generE.addItem("No","0");
    cli_generE.addItem("Si","1");
    s="SELECT tar_codi,tar_nomb FROM tipotari ORDER BY tar_codi";
    dtStat.select(s);
    tar_codiE.addDatos(dtStat);
    tar_codiE.setFormato(true);
    tar_codiE.setFormato(Types.DECIMAL,"#9",2);

    s="SELECT pai_codi,pai_nomb FROM v_paises ORDER BY pai_nomb";
    dtStat.select(s);
    pai_codiE.setFormato(Types.DECIMAL,"###9",2);
    pai_codiE.addDatos(dtStat);
    llenaTipoFact(cli_tipfacE);

    MantTipoIVA.llenaComboIVA(cli_tipivaE, dtStat);
    

    cli_giroE.addItem("Si","S");
    cli_giroE.addItem("No","N");

    cli_albvalE.addItem("Ambos",""+LISTAR_ALB_SINDEFINIR);
    cli_albvalE.addItem("Si",""+LISTAR_ALB_VALORADOS);
    cli_albvalE.addItem("No",""+LISTAR_ALB_SINVALORAR);

    cli_agralbE.addItem("Si","-1");
    cli_agralbE.addItem("No","0");

    cli_exeivaE.addItem("No","0");
    cli_exeivaE.addItem("Si","-1");


    cli_recequE.addItem("No","0");
    cli_recequE.addItem("Si","-1");
    emp_codiE.setColumnaAlias("emp_codi");
    cli_codiE.setColumnaAlias("cli_codi");
    cli_nombE.setColumnaAlias("cli_nomb");
    cli_nomcoE.setColumnaAlias("cli_nomco");
    cli_direcE.setColumnaAlias("cli_direc");
    cli_poblE.setColumnaAlias("cli_pobl");
    cli_codpoE.setColumnaAlias("cli_codpo");
    cli_telefE.setColumnaAlias("cli_telef");
    cli_faxE.setColumnaAlias("cli_fax");
    cli_nifE.setColumnaAlias("cli_nif");
    cli_perconE.setColumnaAlias("cli_percon");
    cli_telconE.setColumnaAlias("cli_telcon");
    cli_nomenE.setColumnaAlias("cli_nomen");
    cli_direeE.setColumnaAlias("cli_diree");
    cli_pobleE.setColumnaAlias("cli_poble");
    cli_codpoeE.setColumnaAlias("cli_codpoe");
    cli_telefeE.setColumnaAlias("cli_telefe");
    cli_faxeE.setColumnaAlias("cli_faxe");
    cli_plzentE.setColumnaAlias("cli_plzent");
    cli_codfaE.setColumnaAlias("cli_codfa");
    cli_tipfacE.setColumnaAlias("cli_tipfac");
    fpa_codiE.setColumnaAlias("fpa_codi");
    cli_dipa1E.setColumnaAlias("cli_dipa1");
    cli_dipa2E.setColumnaAlias("cli_dipa2");
    ban_codiE.setColumnaAlias("ban_codi");
    cli_baoficE.setColumnaAlias("cli_baofic");
    cli_badicoE.setColumnaAlias("cli_badico");
    cli_bacuenE.setColumnaAlias("cli_bacuen");
    cli_baremeE.setColumnaAlias("cli_bareme");
    cli_vaccomE.setColumnaAlias("cli_vaccom");
    cli_vacfinE.setColumnaAlias("cli_vacfin");
    cli_zonrepE.setColumnaAlias("cli_zonrep");
    cli_zoncreE.setColumnaAlias("cli_zoncre");
    zon_codiE.setColumnaAlias("zon_codi");
    rut_codiE.setColumnaAlias("rut_codi");
    rep_codiE.setColumnaAlias("rep_codi");
    cli_disc1E.setColumnaAlias("cli_disc1");
    cli_disc2E.setColumnaAlias("cli_disc2");
    cli_disc3E.setColumnaAlias("cli_disc3");
    cli_disc4E.setColumnaAlias("cli_disc4");

    cli_activE.setColumnaAlias("cli_activ");
    cli_generE.setColumnaAlias("cli_gener");
    sbe_codiE.setColumnaAlias("sbe_codi");
    cli_giroE.setColumnaAlias("cli_giro");
    cli_libivaE.setColumnaAlias("cli_libiva");
    cli_carteE.setColumnaAlias("cli_carte");
    cli_diarioE.setColumnaAlias("cli_diario");
    cli_sefacbE.setColumnaAlias("cli_sefacb");
    cli_dtoppE.setColumnaAlias("cli_dtopp");
    cli_comisE.setColumnaAlias("cli_comis");
    cli_dtootrE.setColumnaAlias("cli_dtootr");
    tar_codiE.setColumnaAlias("tar_codi");
    cli_albvalE.setColumnaAlias("cli_albval");
    cli_recequE.setColumnaAlias("cli_recequ");
    cli_agralbE.setColumnaAlias("cli_agralb");
    cli_riesgE.setColumnaAlias("cli_riesg");
    pai_codiE.setColumnaAlias("pai_codi");
    cue_codiE.setColumnaAlias("cue_codi");
    cli_exeivaE.setColumnaAlias("cli_exeiva");
    cli_tipivaE.setColumnaAlias("cli_tipiva");
    cli_porivaE.setColumnaAlias("cli_poriva");
    cli_tipdocE.setColumnaAlias("cli_tipdoc");
    cli_sitfacE.setColumnaAlias("cli_sitfac");
    cli_orgofiE.setColumnaAlias("cli_orgofi");
    cli_coimivE.setColumnaAlias("cli_coimiv");
    cli_pdtocoE.setColumnaAlias("cli_pdtoco");
    cli_prapelE.setColumnaAlias("cli_prapel");
    cli_fecaltE.setColumnaAlias("cli_fecalt");
    cli_feulmoE.setColumnaAlias("cli_feulmo");
    div_codiE.setColumnaAlias("div_codi");
    cli_internE.setColumnaAlias("cli_intern");
    cli_precfiE.setColumnaAlias("cli_precfi");
    eti_codiE.setColumnaAlias("eti_codi");
    cli_horenvE.setColumnaAlias("cli_horenv");
    cli_comenvE.setColumnaAlias("cli_comenv");
    cli_email1E.setColumnaAlias("cli_email1");
    cli_email2E.setColumnaAlias("cli_email2");
//    cli_nurgsaE.setColumnaAlias("cli_nurgsa");
//    jt.getPopMenu().add(copiaCliente,0);
    activaTodo();
    verDatos();
    activarEventos();
  }
  public static void llenaTipoFact(CComboBox tipFacE)
  {
    llenaTipoFact(tipFacE,true);
  }
  public static void llenaTipoFact(CComboBox tipFacE,boolean incNoFact)
  {
    tipFacE.addItem("Semanal", "S");
    tipFacE.addItem("Quincenal", "Q");
    tipFacE.addItem("Mensual", "M");
    tipFacE.addItem("No Facturar", "O");
  }
  void llenaDiscr(CLinkBox lkBox,String discr) throws SQLException
  {
    llenaDiscr(dtStat,lkBox,discr,EU.em_cod);
  }
  public static void llenaDiscr(DatosTabla dt,CLinkBox lkBox, String discr,int empCodi) throws SQLException
  {
    pdconfig.llenaDiscr(dt,lkBox,discr,empCodi);
  }
  void activarEventos()
  {
//    copiaCliente.addActionListener(new ActionListener()
//    {
//      public void actionPerformed(ActionEvent e)
//      {
//        copiaCliente_actionPerformed();
//      }
//    });
    cli_codiE.addFocusListener(new FocusAdapter()
    {
            @Override
      public void focusLost(FocusEvent e)
      {
        if (cli_codfaE.getValorInt() == 0 && nav.pulsado!=navegador.QUERY)
          cli_codfaE.setValorInt(cli_codiE.getValorInt());
        if (nav.pulsado == navegador.ADDNEW)
          verDatOtrCli();
      }
    });
    Tpanel.addChangeListener(new ChangeListener()
    {
      public void stateChanged(ChangeEvent e)
      {
        if (Tpanel.getSelectedIndex() == 4 && ! nav.isEdicion())
          llenaGridCambios();
      }
    });
    jt.tableView.getSelectionModel().addListSelectionListener(new
        ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent e)
      {
        if (e.getValueIsAdjusting() || nav.isEdicion())
          return;
        if ( ! jt.isEnabled() || jt.isVacio() )
          return;
        verDatos(jt.getSelectedRow());
        chgCliente=false;
      }
    });

    Bbusmax.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Bbusmax_actionPerformed();
      }
    });
    cli_tipivaE.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        if (!cli_tipivaE.isEnabled() || nav.pulsado==navegador.QUERY)
          return;
        try {
          int porcIva=MantTipoIVA.getPorcIva(dtStat, cli_tipivaE.getValorInt());
         
          if (porcIva<0)
            return;
          cli_porivaE.setValorDec(porcIva);
        } catch (Exception k)
        {
          Error("Error al buscar tipo de IVA",k);
        }
      }
    });
  }

//  void copiaCliente_actionPerformed()
//  {
//    if (! bloqueaCliente())
//      return;
//    swCopiaCl=true;
//    mensaje("Restaurar datos de Cliente");
//    clc_comen="Restaurados Datos de Fecha " +
//            jt.getValString(1) + " " + jt.getValString(2);
//    Baceptar.setEnabled(true);
//    Bcancelar.setEnabled(true);
//  }

  public void PADPrimero()
  {
    nRegE.setValorInt(1);
    verDatos();
  }
  public void PADAnterior()
  {
    nRegE.setValorInt(nRegE.getValorInt()-1);
    verDatos();
  }
  public void PADSiguiente()
  {
    if (! dtCons.isLast())
      nRegE.setValorInt(nRegE.getValorInt()+1);
    verDatos();
  }
  public void PADUltimo()
  {
    nRegE.setValorDec(nRegSelE.getValorInt());
    verDatos();
  }

  public void ej_query1()
  {
    Component c = PdatGen.getErrorConf();
    if (c != null)
    {
      mensajeErr("(datGen) Condiciones de Busqueda NO validas");
      Tpanel.setSelectedIndex(0);
      c.requestFocus();
      return;
    }
     c = Pcabe.getErrorConf();
    if (c != null)
    {
      mensajeErr("(datGen) Condiciones de Busqueda NO validas");
      Tpanel.setSelectedIndex(0);
      c.requestFocus();
      return;
    }

    c = PdatFra.getErrorConf();
    if (c != null)
    {
      mensajeErr("(datfra) Condiciones de Busqueda NO validas");
      Tpanel.setSelectedIndex(1);
      c.requestFocus();
      return;
    }

    c = PdatEnv.getErrorConf();
    if (c != null)
    {
      mensajeErr("(datEnv) Condiciones de Busqueda NO validas");
      Tpanel.setSelectedIndex(2);
      c.requestFocus();
      return;
    }


    c = PdatCon.getErrorConf();
      if (c != null)
      {
        mensajeErr("(datfra) Condiciones de Busqueda NO validas");
        Tpanel.setSelectedIndex(3);
        c.requestFocus();
        return;
      }

    ArrayList v = new ArrayList();
    v.add(emp_codiE.getStrQuery());
    v.add(cli_codiE.getStrQuery());
    v.add(cli_nombE.getStrQuery());
    v.add(cli_nomcoE.getStrQuery());
    v.add(cli_direcE.getStrQuery());
    v.add(cli_poblE.getStrQuery());
    v.add(cli_codpoE.getStrQuery());
    v.add(cli_telefE.getStrQuery());
    v.add(cli_faxE.getStrQuery());
    v.add(cli_nifE.getStrQuery());
    v.add(cli_perconE.getStrQuery());
    v.add(cli_telconE.getStrQuery());
    v.add(cli_nomenE.getStrQuery());
    v.add(cli_direeE.getStrQuery());
    v.add(cli_internE.getStrQuery());
    v.add(cli_precfiE.getStrQuery());
    v.add(eti_codiE.getStrQuery());
    v.add(cli_comenvE.getStrQuery());
    v.add(cli_horenvE.getStrQuery());
//    v.add(cli_nurgsaE.getStrQuery());
    v.add(cli_telefeE.getStrQuery());
    v.add(cli_faxeE.getStrQuery());
    v.add(cli_plzentE.getStrQuery());
    v.add(cli_pobleE.getStrQuery());
    v.add(cli_codpoeE.getStrQuery());
    v.add(cli_codfaE.getStrQuery());
    v.add(cli_tipfacE.getStrQuery());
    v.add(fpa_codiE.getStrQuery());
    v.add(cli_dipa1E.getStrQuery());
    v.add(cli_dipa2E.getStrQuery());
    v.add(ban_codiE.getStrQuery());
    v.add(cli_baoficE.getStrQuery());
    v.add(cli_badicoE.getStrQuery());
    v.add(cli_bacuenE.getStrQuery());
    v.add(cli_baremeE.getStrQuery());
    v.add(cli_vaccomE.getStrQuery());
    v.add(cli_vacfinE.getStrQuery());
    v.add(cli_zonrepE.getStrQuery());
    v.add(cli_zoncreE.getStrQuery());
    v.add(zon_codiE.getStrQuery());
    v.add(rut_codiE.getStrQuery());
    
    v.add(rep_codiE.getStrQuery());
    v.add(cli_disc1E.getStrQuery());
    v.add(cli_disc2E.getStrQuery());
    v.add(cli_disc3E.getStrQuery());
    v.add(cli_disc4E.getStrQuery());
    v.add(cli_activE.getStrQuery());
    v.add(cli_generE.getStrQuery());
    v.add(sbe_codiE.getStrQuery());
    v.add(cli_giroE.getStrQuery());
    v.add(cli_libivaE.getStrQuery());
    v.add(cli_carteE.getStrQuery());
    v.add(cli_diarioE.getStrQuery());
    v.add(cli_sefacbE.getStrQuery());
    v.add(cli_dtoppE.getStrQuery());
    v.add(cli_comisE.getStrQuery());
    v.add(cli_dtootrE.getStrQuery());
    v.add(tar_codiE.getStrQuery());
    v.add(cli_albvalE.getStrQuery());
    v.add(cli_recequE.getStrQuery());
    v.add(cli_agralbE.getStrQuery());
    v.add(cli_riesgE.getStrQuery());
    v.add(pai_codiE.getStrQuery());
    v.add(cue_codiE.getStrQuery());
    v.add(cli_exeivaE.getStrQuery());
    v.add(cli_tipivaE.getStrQuery());
    v.add(cli_porivaE.getStrQuery());
    v.add(cli_tipdocE.getStrQuery());
    v.add(cli_sitfacE.getStrQuery());
    v.add(cli_orgofiE.getStrQuery());
    v.add(cli_coimivE.getStrQuery());
    v.add(cli_pdtocoE.getStrQuery());
    v.add(cli_prapelE.getStrQuery());
    v.add(pai_codiE.getStrQuery());
    v.add(cli_fecaltE.getStrQuery());
    v.add(cli_feulmoE.getStrQuery());
    v.add(cli_email1E.getStrQuery());
    v.add(cli_email2E.getStrQuery());
    Pcabe.setQuery(false);
    PdatGen.setQuery(false);
    PdatEnv.setQuery(false);
    PdatFra.setQuery(false);
    PdatCon.setQuery(false);
    try
    {
      s = "SELECT * FROM clientes ";
      s = creaWhere(s, v, true);
      s += " ORDER BY cli_activ DESC,cli_codi";
      this.setEnabled(false);
      mensaje("Espere, por favor ... buscando datos");

      if (!dtCon1.select(s))
      {
        msgBox("No encontrados Clientes con estos criterios");
        mensaje("");
        verDatos();
        activaTodo();
        this.setEnabled(true);
        nav.pulsado=nav.NINGUNO;
        return;
      }
      strSql = s;
      rgSelect();
      this.setEnabled(true);

      mensaje("");
      mensajeErr("Nuevas Condiciones ... Establecidas");
    }
    catch (Exception k)
    {
      Error("Error al buscar datos", k);
    }

    activaTodo();
    verDatos();
    nav.pulsado=navegador.NINGUNO;
  }
  public void canc_query()
  {
    Pcabe.setQuery(false);
    PdatGen.setQuery(false);
    PdatEnv.setQuery(false);
    PdatFra.setQuery(false);
    PdatCon.setQuery(false);

    activaTodo();
    mensaje("");
    verDatos();
    mensajeErr("Consulta ... CANCELADA");
    nav.pulsado=navegador.NINGUNO;
  }

    @Override
  public boolean checkEdit()
  {
    try
    {
        if (!emp_codiE.controla())
        {
         mensajeErr("Empresa NO Valida");
         return false;
        }

    if (cli_nombE.isNull())
    {
      mensajeErr("Introduzca Nombre del Cliente");
      cli_nombE.requestFocus();
      return false;
    }
    if (cli_nomcoE.isNull())
    {
      mensajeErr("Introduzca Nombre Comercial del Cliente");
      cli_nomcoE.requestFocus();
      return false;
    }

    if (cli_poblE.isNull())
    {
      mensajeErr("Introduzca Poblacion del Cliente");
      cli_poblE.requestFocus();
      return false;
    }
  
    if (!checkCodPostal(cli_codpoE.getText()))
    {
        cli_codpoE.requestFocus();
        return false;
    }

        
    if (cli_direcE.isNull())
    {
      mensajeErr("Introduzca Direccion del Cliente");
      cli_direcE.requestFocus();
      return false;
    }

    if (! sbe_codiE.controla(true))
    {
      mensajeErr("Introduzca SubEmpresa de Cliente");
      return false;
    }
    if (! cli_zonrepE.controla(true))
    {
      mensajeErr("Zona/Representante NO valida");
//      cli_zonrepE.requestFocus();
      return false;
    }
    if (! cli_zoncreE.controla())
    {
      mensajeErr("Zona/Credito NO valido");
      cli_zoncreE.requestFocus();
      return false;
    }
    if (! zon_codiE.controla(true))
    {
      mensajeErr("Zona NO valida");
      return false;
    }
     if (! rut_codiE.controla(true))
    {
      mensajeErr("Ruta NO valida");
      return false;
    }
      if (! rep_codiE.controla(true))
    {
      mensajeErr("Representante NO valido");
      return false;
    }
    s=MantRepres.checkRepr(rep_codiE.getText(),
            zon_codiE.getText(),sbe_codiE.getValorInt(),tar_codiE.getValorInt(),
            dtStat);
    if (s!=null)
    {
       mensajeErr(s);
       rep_codiE.requestFocus();
       return false;
    }
    if (! pai_codiE.controla()){
        mensajeErr("Pais NO VALIDO");
        return false;
      }
      if (! div_codiE.controla())
      {
        mensajeErr("Divisa NO VALIDA");
        return false;
      }
      if (cli_codfaE.getValorInt() != cli_codiE.getValorInt())
      {
        if (!cli_codfaE.controlar())
        {
          mensajeErr(cli_codfaE.getMsgError());
          return false;
        }
      }
      if (cli_nifE.isNull())
      {
        mensajeErr("Introduzca NIF del Cliente");
        cli_nifE.requestFocus();
        return false;
      }

      if (!fpa_codiE.controlar())
      {
        mensajeErr("Introduzca Forma de Pago del Cliente");
        return false;
      }
      if (fpa_codiE.getEsGiro().equals("S"))
      {
        if (cli_giroE.getValor().equals("N"))
        {
          mensajeErr("Forma de Pago es de giro Pero se ha marcado como no girar al cliente");
          fpa_codiE.requestFocus();
          return false;
        }
      }
      if (fpa_codiE.getEsGiro().equals("N"))
      {
        if (cli_giroE.getValor().equals("S"))
        {
          mensajeErr("Forma de Pago NO es de giro Pero se ha marcado a girar al cliente");
          fpa_codiE.requestFocus();
          return false;
        }
      }

      if (tar_codiE.isNull())
      {
        mensajeErr("Introduzca Tarifa del Cliente");
        tar_codiE.requestFocus();
        return false;
      }
      ban_codiE.setAceptaNulo(!cli_giroE.getValor().equals("S"));
      if (!ban_codiE.controlar())
      {
        mensajeErr(ban_codiE.getMsgError());
        return false;
      }

      if (cli_giroE.getValor().equals("S"))
      {
        String cuenta=Formatear.format(ban_codiE.getValorInt(),"9999")+
             Formatear.format(cli_baoficE.getValorInt(),"9999");
         int numControl=Formatear.getNumControl(Double.parseDouble(cuenta)) ;
         if (numControl != Integer.parseInt(Formatear.format(cli_badicoE.getValorInt(), "99").
                              substring(0, 1)))
         {
           mensajeErr("Digito control NO VALIDO para banco y sucursal ("+numControl+")");
           return false;
         }
         numControl=Formatear.getNumControl(cli_bacuenE.getValorDec());
         if (numControl !=
             Integer.parseInt(Formatear.format(cli_badicoE.getValorInt(), "99").
                              substring(1, 2)))
         {
           mensajeErr("Digito control NO VALIDO para Num. Cuenta ("+numControl+")");
           return false;
         }
      }
      if (cli_comenT.getText().length()>250 )
      {
        mensajeErr("Campo comentario no puede ser superior a 250 caracteres");
        cli_comenT.requestFocus();
        return false;
      }
      if (cli_nomenE.isNull())
      {
          mensajeErr("Introduzca nombre de entrega");
          cli_nomenE.setText(cli_nombE.getText());
          cli_nomenE.requestFocus();
          return false;
      }
      if (cli_pobleE.isNull())
      {
          mensajeErr("Introduzca Población de entrega");
          cli_pobleE.setText(cli_poblE.getText());
          cli_pobleE.requestFocus();
          return false;
      }
      if (cli_direeE.isNull())
      {
          mensajeErr("Introduzca Direccion de entrega");
          cli_direeE.setText(cli_direcE.getText());
          cli_direeE.requestFocus();
          return false;
      } 
      if (cli_codpoeE.isNull())
      {
          mensajeErr("Introduzca Codigo Postal de entrega");
          cli_codpoeE.setText(cli_codpoE.getText());
          cli_codpoeE.requestFocus();
          return false;
      }
      if (!checkCodPostal(cli_codpoeE.getText()))
      {
        cli_codpoeE.requestFocus();
        return false;
      }
      if (!cli_telefeE.isNull() && cli_telefeE.isNull())
      {
          mensajeErr("Introduzca Telefono Entrega de entrega");
          cli_telefeE.setText(cli_telefeE.getText());
          cli_telefeE.requestFocus();
          return false;
      }
              
    } catch (SQLException | ParseException | NumberFormatException k)
    {
      Error("Error al Controlar Campos",k);
    }

    return true;
  }
  boolean checkCodPostal(String codPostal) throws SQLException
  {
      if (codPostal.trim().equals(""))
      {
          mensajeErr("Introduzca Codigo Postal del Cliente");
          return false;
      }
      if (pai_codiE.getValorInt() == MantPaises.PAI_ESPANA)
      {
          if (!Formatear.isNumeric(codPostal))
          {
              mensajeErr("Codigo postal para España debe ser numerico");
              return false;
          } else
          {
              if (codPostal.length() != 5)
              {
                  mensajeErr("Codigo postal para España debe ser de cinco caracteres");
                  return false;
              }
          }
          s="SELECT * from  prov_espana where cop_codi='"+codPostal.substring(0,2)+"'";
          if (!dtStat.select(s))
          {
              mensajeErr("Provincia NO valida para ese codigo postal");
              return false;
          }
      }
      return true;
  }
    @Override
  public void ej_edit1()
  {
    try
    {
      if (clicodiAnt != cli_codiE.getValorInt())
      {
        if (! checkCliente())
          return;
        int responder = mensajes.mensajeYesNo("Cambiar codigo de Cliente ?");
        if (responder != mensajes.YES)
        {
          cli_codiE.getValorInt();
          return;
        }
      }
      clc_comen = mensajes.mensajeExplica("Cambio en Cliente", "Explica los cambios realizados",
                                          clc_comen);
      if (clc_comen == null)
        return;
      clc_comen = Formatear.strCorta(clc_comen, 100);
    }
    catch (Exception k)
    {
      Error("Error al actualizar Datos\n" + s, k);
    }

    new miThread("")
    {
            @Override
      public void run()
      {
        actDatosCli();
      }
    };
  }

   void actDatosCli()
   {
     try
     {
       msgEspere("Actualizando datos de Clientes....");
       s="select * from clientes where cli_codi = "+clicodiAnt;
       dtCon1.select(s);
       dtBloq.addNew("cliencamb");
       dtCon1.copiaRegistro(dtBloq);
       dtBloq.setDato("usu_nomb",EU.usuario);
       dtBloq.setDato("clc_fecha",Formatear.getDateAct());
       dtBloq.setDato("clc_hora",Formatear.getFechaAct("HH.mm"));
       dtBloq.setDato("clc_comen",clc_comen);
       dtBloq.update();
//     
//       s = " INSERT INTO cliencamb values(" + dtAdd.getStrInsert() +
//           ", '" + EU.usuario + "'" +
//           ",TO_DATE('" + Formatear.getFechaAct("dd-MM-yyyy") + "','dd-MM-yyyy')" +
//           "," + Formatear.getFechaAct("HH.mm") +
//           ",'" + clc_comen + "')";
// //     debug(dtAdd.getStrSelect(s));
//       stUp.executeUpdate(dtAdd.getStrSelect(s));
       dtAdd.edit();
       if (clicodiAnt != cli_codiE.getValorInt())
       {
         s = "UPDATE v_albavec set cli_codi = " + cli_codiE.getValorInt() +
             " WHERE cli_codi = " + clicodiAnt;
         stUp.executeUpdate(s);
         s = "UPDATE v_albavec set cli_codfa = " + cli_codiE.getValorInt() +
             " WHERE cli_codfa = " + clicodiAnt;
         stUp.executeUpdate(s);

         s = "UPDATE v_facvec set cli_codi = " + cli_codiE.getValorInt() +
             " WHERE cli_codi = " + clicodiAnt;
         stUp.executeUpdate(s);
         s = "UPDATE v_cliart set cli_codi = " + cli_codiE.getValorInt() +
             " WHERE cli_codi = " + clicodiAnt;
         stUp.executeUpdate(s);
         s = "UPDATE albvefax set cli_codi = " + cli_codiE.getValorInt() +
             " WHERE cli_codi = " + clicodiAnt;
         stUp.executeUpdate(s);
         s = "UPDATE pedvenc set cli_codi = " + cli_codiE.getValorInt() +
             " WHERE cli_codi = " + clicodiAnt;
         stUp.executeUpdate(s);
         s = "UPDATE compedven set cli_codi = " + cli_codiE.getValorInt() +
             " WHERE cli_codi = " + clicodiAnt;
         stUp.executeUpdate(s);
         s = "UPDATE comprocli set cli_codi = " + cli_codiE.getValorInt() +
             " WHERE cli_codi = " + clicodiAnt;
         stUp.executeUpdate(s);
       }
       actDatos();
       resetBloqueo(dtAdd, "clientes", clicodiAnt + "-" + empCodiAnt);
       llenaGridCambios();
     }
     catch (Exception k)
     {
       Error("Error al actualizar Datos\n" + s, k);
     }
     activaTodo();
     nav.pulsado = navegador.NINGUNO;
     resetMsgEspere();
     mensajeErr("Cliente .... MODIFICADO");
   }

  private void actDatos() throws ParseException, SQLException
  {
    dtAdd.setDato("emp_codi",emp_codiE.getValorInt());
    dtAdd.setDato("cli_codi",cli_codiE.getValorInt());
    dtAdd.setDato("cli_nomb",cli_nombE.getText());
    dtAdd.setDato("cli_nomco",cli_nomcoE.getText());
    dtAdd.setDato("cli_direc",cli_direcE.getText());
    dtAdd.setDato("cli_pobl",cli_poblE.getText());
    dtAdd.setDato("cli_codpo",cli_codpoE.getText());
    dtAdd.setDato("cli_telef",cli_telefE.getText());
    dtAdd.setDato("cli_fax",cli_faxE.getText());
    dtAdd.setDato("cli_nif",cli_nifE.getText());
    dtAdd.setDato("cli_percon",cli_perconE.getText());
    dtAdd.setDato("cli_telcon",cli_telconE.getText());
    dtAdd.setDato("cli_nomen",cli_nomenE.getText());
    dtAdd.setDato("cli_diree",cli_direeE.getText());
    dtAdd.setDato("cli_poble",cli_pobleE.getText());
    dtAdd.setDato("cli_codpoe",cli_codpoeE.getText());
    dtAdd.setDato("cli_telefe",cli_telefeE.getText());
    dtAdd.setDato("cli_faxe",cli_faxeE.getText());
    dtAdd.setDato("cli_plzent",cli_plzentE.getValorInt());


    dtAdd.setDato("cli_codfa",cli_codfaE.getText());
    dtAdd.setDato("cli_tipfac",cli_tipfacE.getValor());
    dtAdd.setDato("fpa_codi",fpa_codiE.getValorInt());
    dtAdd.setDato("cli_dipa1",cli_dipa1E.getValorInt());
    dtAdd.setDato("cli_dipa2",cli_dipa2E.getValorInt());
    dtAdd.setDato("ban_codi",ban_codiE.getValorInt());
    dtAdd.setDato("cli_baofic",cli_baoficE.getValorInt());
    dtAdd.setDato("cli_badico",cli_badicoE.getValorInt());
    dtAdd.setDato("cli_bacuen",cli_bacuenE.getText());
    dtAdd.setDato("cli_bareme",cli_baremeE.getValorInt());
    dtAdd.setDato("cli_vaccom",cli_vaccomE.getDate());
    dtAdd.setDato("cli_vacfin",cli_vacfinE.getDate());
    dtAdd.setDato("cli_zonrep",cli_zonrepE.getText());
    dtAdd.setDato("cli_zoncre",cli_zoncreE.getText());
    dtAdd.setDato("zon_codi",zon_codiE.getText());
    dtAdd.setDato("rut_codi",rut_codiE.getText());
    dtAdd.setDato("rep_codi",rep_codiE.getText());
    dtAdd.setDato("cli_activ",cli_activE.getValor());
    dtAdd.setDato("cli_gener",cli_generE.getValor());
    dtAdd.setDato("sbe_codi",sbe_codiE.getValorInt());
    dtAdd.setDato("cli_giro",cli_giroE.getValor());
    dtAdd.setDato("cli_libiva",cli_libivaE.getText());
    dtAdd.setDato("cli_carte",cli_carteE.getText());
    dtAdd.setDato("cli_diario",cli_diarioE.getText());
    dtAdd.setDato("cli_sefacb",cli_sefacbE.getText());
    dtAdd.setDato("cli_dtopp",cli_dtoppE.getValorDec());
    dtAdd.setDato("cli_comis",cli_comisE.getValorDec());
    dtAdd.setDato("cli_dtootr",cli_dtootrE.getValorDec());
    dtAdd.setDato("tar_codi",tar_codiE.getValorInt());
    dtAdd.setDato("cli_albval",cli_albvalE.getValor());
    dtAdd.setDato("cli_recequ",cli_recequE.getValor());
    dtAdd.setDato("cli_agralb",cli_agralbE.getValor());
    dtAdd.setDato("cli_comen",cli_comenT.getText());
    dtAdd.setDato("cli_riesg",cli_riesgE.getValorDec());
    dtAdd.setDato("pai_codi",pai_codiE.getValorInt());
    dtAdd.setDato("cue_codi",cue_codiE.getText());
    dtAdd.setDato("cli_exeiva",cli_exeivaE.getValor());
    dtAdd.setDato("cli_tipiva",cli_tipivaE.getValor());
    dtAdd.setDato("cli_poriva",cli_porivaE.getText());
    dtAdd.setDato("cli_tipdoc",cli_tipdocE.getText());
    dtAdd.setDato("cli_sitfac",cli_sitfacE.getText());
    dtAdd.setDato("cli_orgofi",cli_orgofiE.getValorInt());
    dtAdd.setDato("cli_coimiv",cli_coimivE.getText());
    dtAdd.setDato("cli_pdtoco",cli_pdtocoE.getValorDec());
    dtAdd.setDato("cli_prapel",cli_prapelE.getValorDec());
    dtAdd.setDato("div_codi",div_codiE.getValorInt());
    dtAdd.setDato("cli_feulmo",Formatear.getFechaAct("dd-MM-yyyy"),"dd-MM-yyyy");
    dtAdd.setDato("cli_disc1",cli_disc1E.getText());
    dtAdd.setDato("cli_disc2",cli_disc2E.getText());
    dtAdd.setDato("cli_disc3",cli_disc3E.getText());
    dtAdd.setDato("cli_disc4",cli_disc4E.getText());
    dtAdd.setDato("cli_intern",cli_internE.getValor());
    dtAdd.setDato("cli_precfi",cli_precfiE.getValor());
    dtAdd.setDato("eti_codi",eti_codiE.getValorInt());
    dtAdd.setDato("cli_horenv",cli_horenvE.getText());
    dtAdd.setDato("cli_comenv",cli_comenvE.getText());
    dtAdd.setDato("cli_email1",cli_email1E.getText());
    dtAdd.setDato("cli_email2",cli_email2E.getText());
//    dtAdd.setDato("cli_nurgsa",cli_nurgsaE.getText());
    dtAdd.update(stUp);
    ctUp.commit();
  }

    @Override
  public void canc_edit()
  {
    try
    {
      resetBloqueo(dtAdd, "clientes", cli_codiE.getText()+"-"+emp_codiE.getValorInt());
    }
    catch (Exception k)
    {
      mensajes.mensajeAviso("Error al Quitar el bloqueo\n" + k.getMessage());
    }

    activaTodo();
    mensaje("");
    verDatos();
    mensajeErr("Edicion ... CANCELADA");
    nav.pulsado=navegador.NINGUNO;
  }

    @Override
  public boolean checkAddNew()
  {
    try
    {
      if (!checkCliente())
        return false;

      return checkEdit();
    }
    catch (Exception k)
    {
      Error("Error al chequear datos de Clientes", k);
    }
    return false;
  }
  boolean checkCliConf(int cliCodi, int empCodi) throws SQLException
  {
    s="SELECT cli_codi from configuracion WHERE cli_codi = "+cliCodi+
        " and emp_codi = "+empCodi;
    if (dtStat.select(s))
    {
        msgBox("Codigo de Cliente es INTERNO de Aplicacion. Imposible Modificar/Borrar/Insertar");
        return true;
    }
    return false;
  }

  boolean checkCliente() throws SQLException
  {
    if (cli_codiE.getValorInt() == 0)
    {
      mensajeErr("Introduzca CODIGO de Cliente");
      cli_codiE.requestFocus();
      return false;
    }
    s = "select * from clientes WHERE cli_codi = " + cli_codiE.getValorInt() +
        " and emp_codi =" + emp_codiE.getValorInt();
    if (dtStat.select(s))
    {
      mensajeErr("Codigo asignado a: " + dtStat.getString("cli_nomb"));
      cli_codiE.requestFocus();
      return false;
    }
   
    return true;
  }
    @Override
  public void ej_addnew1()
  {
    try
    {
      dtAdd.addNew("clientes");
      dtAdd.setDato("cli_codi",cli_codiE.getValorInt());
      dtAdd.setDato("emp_codi",emp_codiE.getValorInt());
      dtAdd.setDato("cli_fecalt",Formatear.getFechaAct("dd-MM-yyyy"),"dd-MM-yyyy");
      actDatos();
    }
    catch (SQLException | ParseException k)
    {
      Error("Error al actualizar Datos", k);
    }
    activaTodo();
    nav.pulsado = navegador.NINGUNO;
    mensaje("");
    mensajeErr("Cliente .... INSERTADO");
  }
    @Override
  public void canc_addnew()
  {
      activaTodo();
      mensaje("");
      verDatos();
      mensajeErr("INSERCION ... CANCELADA");
      nav.pulsado=navegador.NINGUNO;
  }

    @Override
  public void activar(boolean b)
  {
    Pcabe.setEnabled(b);
    PdatGen.setEnabled(b);
    PdatEnv.setEnabled(b);
    PdatFra.setEnabled(b);
    PdatCon.setEnabled(b);
    Bbusmax.setEnabled(b);
    Baceptar.setEnabled(b);
    Bcancelar.setEnabled(b);
    cli_comenT.setEnabled(b);
    cli_codiE.setEnabled(b);
//    emp_codiE.setEnabled(b);
  }
    @Override
  public void PADQuery(){
    nav.pulsado=navegador.QUERY;
    activar(true);
    Pcabe.setQuery(true);
    PdatGen.setQuery(true);
    PdatEnv.setQuery(true);
    PdatFra.setQuery(true);
    PdatCon.setQuery(true);

    Pcabe.resetTexto();
    PdatGen.resetTexto();
    PdatEnv.resetTexto();
    PdatFra.resetTexto();
    PdatCon.resetTexto();
    emp_codiE.setValorInt(EU.em_cod);
    if (EU.getSbeCodi()!=0)
      sbe_codiE.setValorInt(EU.getSbeCodi());
    cli_comenT.setEnabled(false);
    cli_nombE.requestFocus();
    cli_fecaltE.setEnabled(true);
    cli_feulmoE.setEnabled(true);
    cli_activE.setValor("S");
    Bbusmax.setEnabled(false);
    Tpanel.setSelectedIndex(0);
    mensaje("Introduzca Condiciones de Busqueda");
  }

    @Override
  public void PADAddNew()
  {
    activar(true);
    mensaje("Insertando ... NUEVO CLIENTE");
    Pcabe.resetTexto();
    PdatGen.resetTexto();
    PdatEnv.resetTexto();
    PdatFra.resetTexto();
    PdatCon.resetTexto();
    cli_fecaltE.setEnabled(false);
    cli_feulmoE.setEnabled(false);
    cli_comenT.setText("");
    cli_tipivaE.setValor("1"); // Iva del 7
    cli_recequE.setValor("0");
    pai_codiE.setValorInt(MantPaises.PAI_ESPANA);
    sbe_codiE.resetTexto();
//   emp_codiE.setEnabled(false);
    emp_codiE.setValorInt(EU.em_cod);
    cli_codiE.requestFocus();
    Tpanel.setSelectedIndex(0);
  }

  boolean bloqueaCliente()
  {
    try
    {
      if (!setBloqueo(dtAdd, "clientes", cli_codiE.getText() + "-" + emp_codiE.getValorInt()))
      {
        msgBox(msgBloqueo);
        nav.pulsado = navegador.NINGUNO;
        activaTodo();
        return false;
      }

      s = "SELECT * FROM clientes WHERE cli_codi =" + cli_codiE.getValorInt() +
          " AND emp_codi = " + emp_codiE.getValorInt();
      if (!dtAdd.select(s, true))
      {
        mensajeErr("Registro NO encontrado ... PROBABLEMENTE SE HA BORRADO");
        nav.pulsado = navegador.NINGUNO;
        activaTodo();
        return false;
      }
    }
    catch (Exception k)
    {
      Error("Error al bloquear Registro de Cliente", k);
      return false;
    }
    return true;
  }

    @Override
  public void PADEdit()
  {
    try {
      if (checkCliConf(cli_codiE.getValorInt(),emp_codiE.getValorInt()))
      {
        activaTodo();
        nav.pulsado=navegador.NINGUNO;
        return;
      }
    } catch (SQLException k)
    {
        Error("Error al Editar Cliente",k);
    }
    clc_comen="";
    if (jt.getSelectedRow()>0)
    {
      clc_comen = "Restaurados Datos de Fecha " +
          jt.getValString(1) + " " + jt.getValString(2);
      msgBox("En Registro de Historico de cambios ... Los datos que esta editando NO son los últimos");
    }
    else
      verDatos(); // Refresco Datos.
    nav.pulsado=navegador.EDIT;
    activar(true);
    cli_codiE.setEnabled(true);
    clicodiAnt=cli_codiE.getValorInt();
    empCodiAnt=emp_codiE.getValorInt();
//    emp_codiE.setEnabled(false);
    mensaje("Modificando Datos de Clientes");
    if (! bloqueaCliente())
      return;

    Bbusmax.setEnabled(false);
    cli_fecaltE.setEnabled(false);
    cli_feulmoE.setEnabled(false);
    Tpanel.setSelectedIndex(0);
    cli_nombE.requestFocus();
  }

  void verDatos(int lineaGrid)
  {
    verDatos(emp_codiE.getValorInt(),cli_codiE.getValorInt(),lineaGrid);
  }

  void verDatos()
  {
    verDatos(dtCons);


    if (Tpanel.getSelectedIndex() == 4)
    {
      chgCliente=true;
      llenaGridCambios();
    }
  }
  /**
  * Muestra los Datos en pantalla del cursor dtCons
  * @param dt DatosTabla
  */

  void verDatos(DatosTabla dt)
  {
    chgCliente=true;
    try
    {
      if (dt.getNOREG())
        return;
      verDatos(dt.getInt("emp_codi"), dt.getInt("cli_codi"), -1);
    } catch (Exception k)
    {
      Error("en VerDatos",k);
    }
  }
  /**
   * Muestra los datos de clientes sobre los parametros mandados
   * @param empCodi int
   * @param cliCodi int
   * @param lineaGrid int > 0 Ver los datos en historico clientes.
   */
  void verDatos(int empCodi,int cliCodi,int lineaGrid)
  {
    chgCliente=true;

    try
    {
      if (lineaGrid<=0)
        s = "SELECT * FROM clientes where cli_codi = " + cliCodi +
            " and emp_codi = " + empCodi;
      else
      {
        s = "SELECT * FROM cliencamb where cli_codi = " + cliCodi +
            " and emp_codi = " + empCodi+
            " and usu_nomb = '"+jt.getValString(lineaGrid,0)+"'"+
            " and clc_fecha = TO_DATE('"+jt.getValString(lineaGrid,1) +"','dd-MM-yyyy') "+
            " and clc_hora =  "+jt.getValString(lineaGrid,2);
//        debug(s);
      }
      if (!dtCon1.select(s))
      {
        msgBox("Cliente: " + cliCodi +
               " REGISTRO NO ENCONTRADO ... PROBABLEMENTE SE HA BORRADO");
        Pcabe.resetTexto();
        PdatGen.resetTexto();
        PdatFra.resetTexto();
        PdatEnv.resetTexto();
        PdatCon.resetTexto();
        cli_codiE.setValorInt(cliCodi);
        emp_codiE.setValorInt(empCodi);
        return;
      }
      emp_codiE.setText(dtCon1.getString("emp_codi"));
      cli_codiE.setText(dtCon1.getString("cli_codi"));
      cli_nombE.setText(dtCon1.getString("cli_nomb"));
      cli_nomcoE.setText(dtCon1.getString("cli_nomco"));
      cli_direcE.setText(dtCon1.getString("cli_direc"));
      cli_poblE.setText(dtCon1.getString("cli_pobl"));
      cli_codpoE.setText(dtCon1.getString("cli_codpo"));
      cli_telefE.setText(dtCon1.getString("cli_telef"));
      cli_faxE.setText(dtCon1.getString("cli_fax"));
      cli_nifE.setText(dtCon1.getString("cli_nif"));
      cli_perconE.setText(dtCon1.getString("cli_percon"));
      cli_telconE.setText(dtCon1.getString("cli_telcon"));
      cli_nomenE.setText(dtCon1.getString("cli_nomen"));
      cli_direeE.setText(dtCon1.getString("cli_diree"));
      cli_pobleE.setText(dtCon1.getString("cli_poble"));
      cli_codpoeE.setText(dtCon1.getString("cli_codpoe"));
      cli_telefeE.setText(dtCon1.getString("cli_telefe"));
      cli_faxeE.setText(dtCon1.getString("cli_faxe"));
      cli_plzentE.setText(dtCon1.getString("cli_plzent"));
      cli_codfaE.setText(dtCon1.getString("cli_codfa"));
      cli_tipfacE.setValor(dtCon1.getString("cli_tipfac"));
      fpa_codiE.setText(dtCon1.getString("fpa_codi"));
      cli_dipa1E.setText(dtCon1.getString("cli_dipa1"));
      cli_dipa2E.setText(dtCon1.getString("cli_dipa2"));
      ban_codiE.setText(dtCon1.getString("ban_codi"));
      cli_baoficE.setText(dtCon1.getString("cli_baofic"));
      cli_badicoE.setText(dtCon1.getString("cli_badico"));
      cli_bacuenE.setText(Formatear.format(dtCon1.getDouble("cli_bacuen",true), "##########"));
      cli_baremeE.setText(dtCon1.getString("cli_bareme"));
      cli_vaccomE.setDate(dtCon1.getDate("cli_vaccom"));
      cli_vacfinE.setDate(dtCon1.getDate("cli_vacfin"));
      cli_zonrepE.setText(dtCon1.getString("cli_zonrep"));
      cli_zoncreE.setText(dtCon1.getString("cli_zoncre"));
      zon_codiE.setText(dtCon1.getString("zon_codi"));
      rut_codiE.setText(dtCon1.getString("rut_codi"));
      rep_codiE.setText(dtCon1.getString("rep_codi"));

      cli_activE.setValor(dtCon1.getString("cli_activ"));
      cli_generE.setValor(dtCon1.getString("cli_gener"));
      sbe_codiE.setValorInt(dtCon1.getInt("sbe_codi",true));
      cli_giroE.setValor(dtCon1.getString("cli_giro"));
      cli_libivaE.setText(dtCon1.getString("cli_libiva"));
      cli_carteE.setText(dtCon1.getString("cli_carte"));
      cli_diarioE.setText(dtCon1.getString("cli_diario"));
      cli_sefacbE.setText(dtCon1.getString("cli_sefacb"));
      cli_dtoppE.setText(dtCon1.getString("cli_dtopp"));
      cli_comisE.setText(dtCon1.getString("cli_comis"));
      cli_dtootrE.setText(dtCon1.getString("cli_dtootr"));
      tar_codiE.setText(dtCon1.getString("tar_codi"));
      cli_albvalE.setValor(dtCon1.getString("cli_albval"));
      cli_recequE.setValor(dtCon1.getString("cli_recequ"));
      cli_agralbE.setValor(dtCon1.getString("cli_agralb"));
      cli_comenT.setText(dtCon1.getString("cli_comen"));
      cli_riesgE.setText(dtCon1.getString("cli_riesg"));
      pai_codiE.setText(dtCon1.getString("pai_codi"));
      cue_codiE.setText(dtCon1.getString("cue_codi"));

      cli_exeivaE.setValor(dtCon1.getString("cli_exeiva"));
      cli_tipivaE.setValor(dtCon1.getString("cli_tipiva"));
      cli_porivaE.setText(dtCon1.getString("cli_poriva"));
      cli_tipdocE.setText(dtCon1.getString("cli_tipdoc"));
      cli_sitfacE.setText(dtCon1.getString("cli_sitfac"));
      cli_orgofiE.setText(dtCon1.getString("cli_orgofi"));
      cli_coimivE.setText(dtCon1.getString("cli_coimiv"));
      cli_pdtocoE.setText(dtCon1.getString("cli_pdtoco"));
      cli_prapelE.setText(dtCon1.getString("cli_prapel"));
      cli_fecaltE.setText(dtCon1.getFecha("cli_fecalt", "dd-MM-yyyy"));
      cli_feulmoE.setText(dtCon1.getFecha("cli_feulmo", "dd-MM-yyyy"));
      div_codiE.setText(dtCon1.getString("div_codi"));
      cli_disc1E.setText(dtCon1.getString("cli_disc1"));
      cli_disc2E.setText(dtCon1.getString("cli_disc2"));
      cli_disc3E.setText(dtCon1.getString("cli_disc3"));
      cli_disc4E.setText(dtCon1.getString("cli_disc4"));
      cli_internE.setValor(dtCon1.getString("cli_intern"));
      cli_precfiE.setValor(dtCon1.getString("cli_precfi"));
      eti_codiE.setValorInt(dtCon1.getInt("eti_codi"));
      cli_horenvE.setText(dtCon1.getString("cli_horenv"));
      cli_comenvE.setText(dtCon1.getString("cli_comenv"));
      cli_feulveE.setDate(dtCon1.getDate("cli_feulve"));
      cli_feulcoE.setDate(dtCon1.getDate("cli_feulco"));
      cli_estconE.setValor(dtCon1.getString("cli_estcon"));
      cli_email1E.setText(dtCon1.getString("cli_email1"));
      cli_email2E.setText(dtCon1.getString("cli_email2"));
//      cli_nurgsaE.setText(dtCon1.getString("cli_nurgsa"));
    }
    catch (Exception k)
    {
      Error("Error al Ver datos", k);
    }
  }

    @Override
  public void PADDelete()
  {
    verDatos();
    Baceptar.setEnabled(true);
    Bcancelar.setEnabled(true);
    mensaje("Borrando Cliente ....");
    try
    {

      if (checkCliConf(cli_codiE.getValorInt(),emp_codiE.getValorInt()))
      {
        activaTodo();
        nav.pulsado=navegador.NINGUNO;
        return;
      }

      s="SELECT * FROM v_albavec WHERE cli_codi = "+cli_codiE.getValorInt()+
          " and emp_codi = "+emp_codiE.getValorInt();
      if (dtStat.select(s))
      {
       msgBox("Cliente TIENE Albaranes de Venta ... IMPOSIBLE BORRAR");
       nav.pulsado=navegador.NINGUNO;
       activaTodo();
       return;
      }
      if (! bloqueaCliente())
        return;
    } catch (Exception k)
    {
      Error("Error al bloquear Registro de Cliente",k);
    }
    Bcancelar.requestFocus();
  }

    @Override
  public void canc_delete()
  {
    try
    {
      resetBloqueo(dtAdd, "clientes", cli_codiE.getText()+"-"+emp_codiE.getValorInt());
    }
    catch (Exception k)
    {
      mensajes.mensajeAviso("Error al Quitar el bloqueo\n" + k.getMessage());
    }

    activaTodo();
    mensaje("");
    verDatos();
    mensajeErr("Edicion ... CANCELADA");
    nav.pulsado = navegador.NINGUNO;
  }

    @Override
  public void ej_delete1()
  {
    try
     {
       clc_comen="";
       clc_comen=mensajes.mensajeExplica("Borrado de Cliente","Explica el POR QUE",clc_comen);
       if (clc_comen==null)
         return;
       clc_comen=Formatear.strCorta(clc_comen,90);
       s="SELECT * FROM clientes WHERE cli_codi ="+cli_codiE.getValorInt()+
           " and emp_codi = "+emp_codiE.getValorInt();
       dtAdd.select(s,true);
       s=" INSERT INTO cliencamb values("+dtAdd.getStrInsert()+
            ", '"+EU.usu_nomb+"' "+
           ",TO_DATE('"+Formatear.getFechaAct("dd-MM-yyyy")+"','dd-MM-yyyy')"+
           ","+Formatear.getFechaAct("HH.mm")+
           ",'**ANULADO**"+clc_comen+"')";
       stUp.executeUpdate(dtAdd.getStrSelect(s));
       dtAdd.delete();
       resetBloqueo(dtAdd, "clientes", cli_codiE.getText()+"-"+emp_codiE.getValorInt());
       rgSelect();
     }
     catch (Exception k)
     {
       Error("Error al actualizar Datos",k);
     }
     activaTodo();
     nav.pulsado=navegador.NINGUNO;
     mensaje("");
     mensajeErr("Cliente "+cli_codiE.getValorInt()+"  .... BORRADO");
     verDatos();
    }
    @Override
    public void rgSelect() throws SQLException
    {
      super.rgSelect();
      if (dtCons.getNOREG())
        nRegSelE.setValorDec(0);
      else
      {
          s="SELECT count(*) as cuantos FROM clientes "+
          (dtCons.getCondWhere().equals("")?"":" WHERE "+dtCons.getCondWhere());
          dtStat.select(s);
          nRegSelE.setValorDec(dtStat.getInt("cuantos"));
      }
      nRegE.setValorDec(1);
    }
    void Bbusmax_actionPerformed()
    {
      s="SELECT MAX(cli_codi) as cli_codi FROM clientes WHERE emp_codi = "+emp_codiE.getValorInt()+
          (cli_codiE.getValorInt()>0?" and cli_codi < "+((cli_codiE.getValorInt()+1)*1000):"");
      try {
        dtStat.select(s);
        cli_codiE.setValorInt(dtStat.getInt("cli_codi",true)+1);
        cli_codiE.requestFocus();
      } catch (Exception k)
      {
        Error("Error al buscar Maximo N� de Cliente",k);
      }
    }

    void verDatOtrCli()
    {
      try
      {
        s="SELECT * FROM clientes WHERE cli_codi = "+cli_codiE.getValorInt()+
            " and emp_codi = "+emp_codiE.getValorInt();
        if (dtStat.select(s))
        {
          if (mensajes.mensajePreguntar("Cliente YA existe .. Ver sus datos",this)==mensajes.YES)
          {
            verDatos(dtStat);
            PADEdit();
          }
        }
      } catch (Exception k)
      {

      }
    }

    void confGrid() throws Exception
    {
      Vector v=new Vector();
      v.addElement("Usuario");
      v.addElement("Fecha");
      v.addElement("Hora");
      v.addElement("Comentario");
      jt.setCabecera(v);
      jt.setAnchoColumna(new int[]{70,90,80,400});
      jt.setAlinearColumna(new int[]{0,1,2,0});
      jt.setAjustarGrid(true);
    }
    void llenaGridCambios()
    {
//      System.out.println("en grid Cambios");
      if (! chgCliente)
        return;
      chgCliente=false;
      jt.setEnabled(false);
      jt.removeAllDatos();
      ArrayList v1 = new ArrayList();
      v1.add("");
      v1.add("Actual");
      v1.add("Actual");
      v1.add("Cliente Actual");
      jt.addLinea(v1);

      try {
        s = "SELECT * FROM cliencamb WHERE emp_codi = " +emp_codiE.getValorInt() +
            " AND cli_codi = " + cli_codiE.getValorInt()+
            " order by clc_fecha desc,clc_hora desc ";
        if (!dtCon1.select(s))
          return;
        do
        {
          ArrayList v=new ArrayList();
          v.add(dtCon1.getString("usu_nomb"));
          v.add(dtCon1.getFecha("clc_fecha","dd-MM-yyyy"));
          v.add(dtCon1.getString("clc_hora"));
          v.add(dtCon1.getString("clc_comen"));
          jt.addLinea(v);
        } while (dtCon1.next());
        jt.requestFocusInicio();
        jt.setEnabled(true);
      } catch (Exception k)
      {
        Error("Error al llenar Grid de Cambios de Clientes",k);
        return;
      }
    }
    
    public static void llenaEstCont(CComboBox cb)
    {
      cb.addItem( "Contactado","C");
      cb.addItem( "No Contac.","N");
      cb.addItem( "Ausente","A");
      cb.addItem( "Llamar","L");
    }
    public static void llenaEstCont(CTextField tf)
    {
      tf.setAdmiteCar(CTextField.CHAR_LIMIT);
      tf.setMaxLong(1);
      tf.setFormato("?");
      
      tf.setMayusc(true);
      tf.setStrCarEsp("CNAL");
      tf.setToolTipText("Contactado,No Contactado,Ausente, Llamar");
    }

//    /**
//     * Rutina a machacar si se quiere hacer algo m�s cuando se haya pulsado
//     * el boton Aceptar
//     */
//    protected void pulsadoAceptar()
//    {
//      if (!swCopiaCl)return;
//      swCopiaCl = true;
//      try
//      {
//        clc_comen = mensajes.mensajeExplica("Restauracion datos Cliente", "Explica los cambios realizados", clc_comen);
//        if (clc_comen == null)
//          return;
//        clc_comen = Formatear.strCorta(clc_comen, 100);
//
//        dtAdd.select(s, true);
//        s = " INSERT INTO cliencamb values(" + dtAdd.getStrInsert() +
//            ", '" + EU.usuario + "'" +
//            ",TO_DATE('" + Formatear.getFechaAct("dd-MM-yyyy") + "','dd-MM-yyyy')" +
//            "," + Formatear.getFechaAct("HH.mm") +
//            ",'" +clc_comen+ "')";
//  //     debug(dtAdd.getStrSelect(s));
//        stUp.executeUpdate(dtAdd.getStrSelect(s));
//        dtAdd.edit();
//        actDatos();
//        resetBloqueo(dtAdd, "clientes", cli_codiE.getText() + "-" + emp_codiE.getValorInt());
//        mensajeErr("Datos ... restaurados");
//        mensaje("");
//        llenaGridCambios();
//      }
//      catch (Exception k)
//      {
//        Error("Error al Copiar datos a cliente", k);
//      }
//      activaTodo();
//    }
//
//    /**
//     * Rutina a machacar si se quiere hacer algo m�s cuando se haya pulsado
//     * el boton Cancelar
//     */
//    protected void pulsadoCancelar()
//    {
//      if (!swCopiaCl)return;
//      try
//      {
//        resetBloqueo(dtAdd, "clientes", cli_codiE.getText() + "-" + emp_codiE.getValorInt());
//      }
//      catch (Exception k)
//      {
//        mensajes.mensajeAviso("Error al Quitar el bloqueo\n" + k.getMessage());
//      }
//
//
//      swCopiaCl=false;
//      mensaje("");
//      mensajeErr("Restauracion datos de cliente ... Cancelado");
//      activaTodo();
//      return;
//    }
 public static String getNombreCliente(DatosTabla dt, int cliCodi) throws SQLException
 {
    String s = "SELECT cli_nomb,cli_nomen,cli_diree,cli_codpoe,cli_poble,"
        + "cli_horenv,cli_comenv from clientes WHERE cli_codi = " + cliCodi;
    if (! dt.select(s))
      return null;
    return dt.getString("cli_nomb");
  }
  public static  String  ponerInactivos(DatosTabla dtSel,DatosTabla dtUpd, int diasVenta ,int diasModif) throws SQLException
  {
      String s = "SELECT * from clientes WHERE cli_activ = 'S' and cli_feulmo <= current_date - "+ diasModif +
          " and cli_feulve <= current_date - "+diasVenta+
          " and cli_intern=0 and cli_gener=0";
      if (! dtSel.select(s))
         return null;
      
      s="";
      do
      {         
        dtUpd.addNew("cliencamb");
        dtSel.copiaRegistro(dtUpd);
        dtUpd.setDato("usu_nomb","anjelica");
        dtUpd.setDato("clc_fecha",Formatear.getDateAct());
        dtUpd.setDato("clc_hora",Formatear.getFechaAct("HH.mm"));
        dtUpd.setDato("clc_comen","Dado de baja automaticamente");
        dtUpd.update();      
        dtUpd.executeUpdate("update clientes set cli_activ='N' where cli_codi="+dtSel.getInt("cli_codi") );
        s+=" Cliente: "+dtSel.getInt("cli_codi")+" "+ dtSel.getString("cli_nomb")+ " ("+dtSel.getString("cli_pobl")+")\n";
      } while (dtSel.next());
      dtUpd.commit();
      return s;
  }
}
