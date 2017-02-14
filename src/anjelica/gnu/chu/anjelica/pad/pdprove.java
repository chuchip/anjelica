package gnu.chu.anjelica.pad;

import gnu.chu.controles.*;
import gnu.chu.interfaces.*;
import gnu.chu.utilidades.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.text.*;
import javax.swing.*;
import gnu.chu.Menu.*;
import gnu.chu.camposdb.*;
import gnu.chu.sql.*;
import javax.swing.border.*;

/**
 *
 * <p>Título: pdprove</p>
 * <p>Descripción: Mantenimiento de la Tabla de Proveedores.</p>
 * <p>Copy zright: Copyright (c) 2005-2016
 *   Este programa es software libre. Puede redistribuirlo y/o modficarlo bajo
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
 * @author Chuchi P
 * @version 1.0
 */
public class pdprove extends ventanaPad implements PAD
{
// CButton Bcancelar = new CButton("Cancelar",Iconos.getImageIcon("cancel"));
//    CButton Baceptar = new CButton("Aceptar F4",Iconos.getImageIcon("check"));
  String s;
  CTabbedPane Tpanel=new CTabbedPane();
  CLabel cLabel34 = new CLabel();
  CTextField prv_poblE = new CTextField(Types.CHAR,"X",30);
  CTextField prv_telconE = new CTextField(Types.CHAR,"X",15);
  CTextField prv_codpoE = new CTextField(Types.DECIMAL,"#99999");
  CLabel cLabel1 = new CLabel();
  CLabel cLabel35 = new CLabel();
  CLabel cLabel3 = new CLabel();
  CComboBox prv_activE = new CComboBox();
  CLabel cLabel10 = new CLabel();
  CTextField prv_direcE = new CTextField(Types.CHAR,"X",40);
  CTextField prv_direreE = new CTextField(Types.CHAR,"X",40);
  CLabel cli_direeL = new CLabel();

  CTextField prv_poblreE = new CTextField(Types.CHAR,"X",30);
  CLabel cLabel4 = new CLabel();
  CLinkBox prv_disc3E = new CLinkBox();
  CTextField prv_faxE = new CTextField(Types.CHAR,"X",15);
  CLabel cLabel2 = new CLabel();
  CTextField prv_nombE = new CTextField(Types.CHAR,"X",50);
  CLabel cLabel7 = new CLabel();
  CTextField prv_nomcoE = new CTextField(Types.CHAR,"X",50);
  CPanel PdatGen = new CPanel();
  CLinkBox prv_disc2E = new CLinkBox();
  CLabel cLabel6 = new CLabel();
  CTextField prv_perconE = new CTextField(Types.CHAR,"x",30);
  CLabel cli_pobleL = new CLabel();
  CLabel cLabel33 = new CLabel();
  CTextField prv_telefE = new CTextField(Types.CHAR,"X",15);
  CTextField prv_codiE = new CTextField(Types.DECIMAL,"#####9");
  CLabel cLabel5 = new CLabel();
  CLabel cLabel11 = new CLabel();
  CLabel cli_nifL = new CLabel();
  CTextField prv_nifE = new CTextField(Types.CHAR,"X",15);
  CPanel Pprinc = new CPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  CPanel PdatEnv = new CPanel();
  CLabel cli_codfaL = new CLabel();
  prvPanel prv_codfacE = new prvPanel();
  CLabel cli_nomenL = new CLabel();
  CTextField prv_nombreE = new CTextField(Types.CHAR,"X",50);
  CTextField prv_coporeE = new CTextField(Types.DECIMAL,"#99999");
  CLabel cli_codpoeL = new CLabel();
  CPanel Pboton = new CPanel();
  CLabel cli_tipfacL = new CLabel();
  CComboBox prv_tipfacE = new CComboBox();
  CLabel fpa_codiL = new CLabel();
  fpaPanel fpa_codiE = new fpaPanel();
  CLabel cLabel16 = new CLabel();
  CTextField prv_dipa1E = new CTextField(Types.DECIMAL,"#9");
  CLabel cLabel17 = new CLabel();
  CTextField prv_dipa2E = new CTextField(Types.DECIMAL,"#9");
  CPanel PdatFra = new CPanel();
  CLabel cli_dtoppL = new CLabel();
  CTextField prv_dtoppE = new CTextField(Types.DECIMAL,"#9,99");
  CLabel tar_codiL = new CLabel();
  CLinkBox tar_codiE = new CLinkBox();
  CLabel prv_recequL = new CLabel();
  CComboBox prv_recequE = new CComboBox();
  JScrollPane jScrollPane1 = new JScrollPane();
  JTextArea prv_observT = new JTextArea();
  CComboBox prv_exeivaE = new CComboBox();
  CLabel cli_porivaL = new CLabel();
  CTextField prv_porivaE = new CTextField(Types.DECIMAL,"#9");
  CLabel cli_pdtocoL = new CLabel();
  CTextField prv_pdtocoE = new CTextField(Types.DECIMAL,"#9,99");
  CLabel cli_prapelL = new CLabel();
  CTextField prv_dtorapE = new CTextField(Types.DECIMAL,"#9,99");
  CLabel prv_exeivaL = new CLabel();
  boolean modConsulta=true;
  CLabel cLabel15 = new CLabel();
  CLinkBox pai_codiE = new CLinkBox();
  CLabel cLabel19 = new CLabel();
  CTextField prv_inivacE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel cLabel110 = new CLabel();
  CTextField prv_finvacE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CPanel PdatCon = new CPanel();
  CTextField cue_codiE = new CTextField(Types.CHAR,"X",12);
  CLabel cLabel21 = new CLabel();
  CLabel cLabel20 = new CLabel();
  CTextField prv_libivaE = new CTextField(Types.CHAR,"X",2);
  CTextField prv_carterE = new CTextField(Types.CHAR,"X",2);
  CLabel cLabel22 = new CLabel();
  CTextField prv_tipdocE = new CTextField(Types.CHAR,"X",2);
  CLabel cLabel23 = new CLabel();
  CLabel cLabel24 = new CLabel();
  CTextField prv_diarioE = new CTextField(Types.CHAR,"X",2);
  CTextField prv_sefactE = new CTextField(Types.CHAR,"X",1);
  CLabel cLabel25 = new CLabel();
  CComboBox prv_tipivaE = new CComboBox();
  CLabel cli_tipivaL1 = new CLabel();
  CLabel cLabel26 = new CLabel();
  CTextField prv_sitfacE = new CTextField(Types.CHAR,"X",2);
  CLabel cLabel27 = new CLabel();
  CTextField prv_orgofiE = new CTextField(Types.DECIMAL,"###9");
  CLabel cLabel28 = new CLabel();
  CTextField prv_coimivE = new CTextField(Types.CHAR,"X",1);
  CLabel cLabel29 = new CLabel();
  CTextField nRegSelE = new CTextField(Types.DECIMAL,"###,##9");
  CTextField nRegE = new CTextField(Types.DECIMAL,"###,##9");
  CLabel cLabel210 = new CLabel();
  CLabel cLabel30 = new CLabel();
  CLabel cLabel31 = new CLabel();
  CTextField prv_fecaltE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel cLabel32 = new CLabel();
  CTextField prv_feulmoE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel cLabel36 = new CLabel();
  empPanel emp_codiE = new empPanel();
  CTextField prv_telereE = new CTextField(Types.CHAR,"X",15);
  CTextField prv_faxreE = new CTextField(Types.CHAR,"X",15);
  CLabel cLabel37 = new CLabel();
  CLabel cLabel38 = new CLabel();
  CLabel cli_codpoeL1 = new CLabel();
  CTextField prv_plzentE = new CTextField(Types.DECIMAL,"##9");
  CComboBox prv_irpfE = new CComboBox();
  CLabel prv_irpfL = new CLabel();
  CLabel prv_irpfL1 = new CLabel();
  CComboBox prv_rpfsbiE = new CComboBox();
  CLinkBox prv_disc4E = new CLinkBox();
  CLabel cLabel39 = new CLabel();
  CComboBox prv_sumdesE = new CComboBox();
  CLabel cLabel310 = new CLabel();
  CComboBox prv_aploreE = new CComboBox();
  CLabel cLabel40 = new CLabel();
  CTextField prv_nurgsaE = new CTextField(Types.CHAR,"X",12);
  CLabel cLabel12 = new CLabel();
  CTextField prv_dtocomE = new CTextField(Types.DECIMAL,"#9.99");
  CLinkBox div_codiE = new CLinkBox();
  CLabel cLabel41 = new CLabel();
  CTextField prv_nexploE = new CTextField(Types.CHAR,"X",15);
  CPanel Pdatmat = new CPanel();
  CLabel cLabel13 = new CLabel();
  CGridEditable jtSde = new CGridEditable(2)
  {
    public int cambiaLinea(int row, int col)
    {
      return checkSde()?-1:0;
    }

    public void cambiaColumna(int col, int colNueva,int row)
    {
      String nombArt;
      try
      {
        if (col == 0)
        {
          nombArt = sde_codiE.getNombMat(sde_codiE.getValorInt());
          jtSde.setValor(nombArt, row, 1);
          sde_codiE.resetCambio();
        }
      }
      catch (Exception k)
      {
        Error("Error al buscar Nombre Matadero", k);
      }
    }
  };
  CLabel cLabel14 = new CLabel();
  CGridEditable jtMat = new CGridEditable(2)
  {
    public int cambiaLinea(int row, int col)
    {
      return checkMat() ? -1 : 0;
    }

    public void cambiaColumna(int col, int colNueva,int row)
    {
      String nombArt;
      try
      {
        if (col == 0)
        {
//          if (mat_codiE.hasCambio() || jtMat.isChangeRow())
 //         {
            nombArt = mat_codiE.getNombMat(mat_codiE.getValorInt());
            jtMat.setValor(nombArt, row, 1);
            mat_codiE.resetCambio();

//          }
        }
      }
      catch (Exception k)
      {
        Error("Error al buscar Nombre Matadero", k);
      }
    }
  };
  matPanel mat_codiE = new matPanel();
  CTextField mat_nombE = new CTextField(Types.CHAR,"X",50);
  sdePanel sde_codiE = new sdePanel();
  CTextField sde_nombE = new CTextField(Types.CHAR,"X",50);
  CPanel Pcabe = new CPanel();
  CPanel Pdiscrim = new CPanel();
  TitledBorder titledBorder2;
  CLabel prv_disc4L = new CLabel();
  CLabel prv_disc2L = new CLabel();
  CLabel prv_disc3L = new CLabel();
  CComboBox prv_internE = new CComboBox();
  CLabel prv_internL = new CLabel();
  CLabel emp_nombL;

  /**
  * Constructor
  * @param eu EntornoUsuario Entorno Usuario
  * @param p Principal Clase del Menu
  */
 public pdprove(EntornoUsuario eu, Principal p)
 {
   this(eu,p,null);
 }
 public pdprove(EntornoUsuario eu, Principal p,Hashtable ht)
 {
   EU = eu;
   vl = p.panel1;
   jf = p;
   eje = true;

   setTitulo("Mantenimiento de Proveedores");

   try
   {
     if (ht != null)
     {
       if (ht.get("modConsulta") != null)
         modConsulta = Boolean.valueOf(ht.get("modConsulta").toString()).
             booleanValue();
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

 public pdprove(gnu.chu.anjelica.menu p, EntornoUsuario eu)
 {
   this(p,eu,null);
 }
 public pdprove(gnu.chu.anjelica.menu p, EntornoUsuario eu,Hashtable ht)
 {
   EU = eu;
   vl = p.getLayeredPane();
   setTitulo("Mantenimiento de Proveedores");
   eje = false;

   try
   {
     if (ht != null)
     {
       if (ht.get("modConsulta") != null)
         modConsulta = Boolean.valueOf(ht.get("modConsulta").toString()).
             booleanValue();
     }

     jbInit();
   }
   catch (Exception e)
   {
     ErrorInit(e);
   }
 }


  private void jbInit() throws Exception
  {
    titledBorder2 = new TitledBorder("");
    iniciarFrame();
    setSize(new Dimension(688,500));
    this.setVersion("2017-02-14");
    strSql = "SELECT * FROM v_proveedo where emp_codi = "+EU.em_cod+" ORDER BY prv_codi ";

    statusBar = new StatusBar(this);
    conecta();
    nav = new navegador(this, dtCons, false,modConsulta?navegador.CURYCON:navegador.NORMAL);
    Iniciar(this);
    Pcabe.setBorder(BorderFactory.createRaisedBevelBorder());
    Pcabe.setMaximumSize(new Dimension(24, 24));
    Pcabe.setMinimumSize(new Dimension(24, 24));
    Pcabe.setPreferredSize(new Dimension(24, 24));
    Pcabe.setLayout(null);
    Pdiscrim.setBorder(titledBorder2);
    Pdiscrim.setBounds(new Rectangle(0, 209, 419, 90));
    Pdiscrim.setLayout(null);
    titledBorder2.setTitle("Discriminadores");
    titledBorder2.setBorder(BorderFactory.createLineBorder(Color.black));
    prv_disc4L.setText("Discriminador 4");
    prv_disc4L.setBounds(new Rectangle(9, 61, 112, 19));
    prv_disc4L.setToolTipText("");
    prv_disc2L.setToolTipText("");
    prv_disc2L.setText("Discriminador 2");
    prv_disc2L.setBounds(new Rectangle(7, 20, 112, 19));
    prv_disc3L.setToolTipText("");
    prv_disc3L.setText("Discriminador 3");
    prv_disc3L.setBounds(new Rectangle(8, 40, 112, 19));
    prv_disc4E.setBounds(new Rectangle(139, 61, 275, 19));
    prv_disc3E.setBounds(new Rectangle(139, 40, 275, 19));
    prv_disc2E.setBounds(new Rectangle(139, 20, 275, 19));
    prv_internE.setBounds(new Rectangle(585, 7, 53, 17));
    prv_internL.setText("Interno");
    prv_internL.setBounds(new Rectangle(532, 7, 47, 19));
    emp_nombL = emp_codiE.creaLabelEmp();
    emp_nombL.setBounds(new Rectangle(511, 226, 160, 19));
    this.getContentPane().add(Pprinc,  BorderLayout.CENTER);

    cLabel11.setText("Nombre Fiscal");
    cLabel11.setBounds(new Rectangle(4, 8, 90, 16));
    cli_nifL.setText("NIF");
    cli_nifL.setBounds(new Rectangle(502, 5, 32, 17));
    Pprinc.setLayout(new BorderLayout());
    PdatGen.setLayout(null);
    PdatGen.setBorder(null);
    PdatEnv.setMaximumSize(new Dimension(32767, 32767));
    PdatEnv.setLayout(null);
    cli_codfaL.setText("Prov. a Facturar");
    cli_codfaL.setBounds(new Rectangle(1, 5, 101, 17));
    cli_nomenL.setText("Nombre Recog.");
    cli_nomenL.setBounds(new Rectangle(1, 10, 87, 16));
    cli_codpoeL.setText("Cod Postal");
    cli_codpoeL.setBounds(new Rectangle(388, 31, 61, 17));
    Pboton.setMinimumSize(new Dimension(60, 30));
    Pboton.setPreferredSize(new Dimension(60, 30));
    Pboton.setLayout(null);
    Bcancelar.setBounds(new Rectangle(377, 1, 93, 22));
    Baceptar.setBounds(new Rectangle(267, 1, 105, 22));
    cli_tipfacL.setText("Tipo Facturacion");
    cli_tipfacL.setBounds(new Rectangle(2, 26, 92, 19));
    fpa_codiL.setText("Forma Pago");
    fpa_codiL.setBounds(new Rectangle(317, 51, 69, 17));
    cLabel16.setText("Dia Pago 1");
    cLabel16.setBounds(new Rectangle(2, 51, 59, 17));
    cLabel17.setText("Dia Pago 2");
    cLabel17.setBounds(new Rectangle(122, 51, 59, 17));
    cli_dtoppL.setText("Dto. PP");
    cli_dtoppL.setBounds(new Rectangle(4, 95, 51, 18));
    tar_codiL.setText("Tarifa");
    tar_codiL.setBounds(new Rectangle(-1, 102, 39, 18));
    tar_codiE.setAceptaNulo(true);
    tar_codiE.setAncTexto(30);
    tar_codiE.setBounds(new Rectangle(63, 104, 212, 18));


    prv_recequL.setText("Rec. Equivalencia");
    prv_recequL.setBounds(new Rectangle(361, 156, 100, 18));
    prv_exeivaE.setToolTipText("");
    prv_exeivaE.setText("Exento de IVA");
    prv_exeivaE.setBounds(new Rectangle(599, 156, 68, 18));

    cli_pdtocoL.setText("% Dto. Comercial");
    cli_pdtocoL.setBounds(new Rectangle(529, 95, 96, 18));
    cli_prapelL.setText("% Rappel");
    cli_prapelL.setBounds(new Rectangle(385, 95, 56, 18));
    prv_exeivaL.setText("Exento IVA");
    prv_exeivaL.setBounds(new Rectangle(526, 156, 67, 18));
    jScrollPane1.setBounds(new Rectangle(75, 130, 597, 42));
    cLabel34.setBounds(new Rectangle(5, 129, 69, 14));
    cLabel35.setBounds(new Rectangle(1, 85, 65, 17));
    prv_perconE.setBounds(new Rectangle(259, 83, 223, 17));
    cLabel6.setBounds(new Rectangle(199, 83, 53, 17));
    prv_telconE.setBounds(new Rectangle(63, 83, 129, 17));
    prv_activE.setBounds(new Rectangle(613, 82, 53, 17));
    cLabel10.setBounds(new Rectangle(562, 82, 42, 17));
    prv_codpoE.setBounds(new Rectangle(63, 61, 50, 17));
    cLabel33.setBounds(new Rectangle(3, 61, 61, 17));
    prv_telefE.setBounds(new Rectangle(352, 61, 129, 17));
    cLabel7.setBounds(new Rectangle(507, 61, 29, 17));
    cLabel5.setBounds(new Rectangle(320, 61, 32, 17));
    prv_faxE.setBounds(new Rectangle(539, 61, 129, 17));
    cLabel3.setBounds(new Rectangle(380, 40, 59, 17));
    prv_poblE.setBounds(new Rectangle(440, 40, 227, 17));
    cLabel4.setBounds(new Rectangle(2, 40, 59, 17));
    prv_direcE.setBounds(new Rectangle(61, 40, 316, 17));
    prv_nomcoE.setBounds(new Rectangle(95, 7, 417, 17));
    cLabel2.setBounds(new Rectangle(157, 4, 99, 16));
    prv_codiE.setBounds(new Rectangle(69, 4, 53, 16));
    cLabel1.setBounds(new Rectangle(5, 4, 67, 16));
    prv_nombE.setBounds(new Rectangle(252, 2, 417, 17));
    prv_recequE.setBounds(new Rectangle(467, 156, 53, 18));
    prv_porivaE.setBounds(new Rectangle(204, 124, 38, 17));
    prv_dtocomE.setBounds(new Rectangle(630, 95, 38, 18));
    prv_dtorapE.setBounds(new Rectangle(441, 95, 38, 18));
    prv_dtoppE.setBounds(new Rectangle(52, 95, 38, 18));
    fpa_codiE.setBounds(new Rectangle(389, 51, 279, 17));
    prv_dipa2E.setBounds(new Rectangle(188, 51, 25, 17));
    prv_dipa1E.setBounds(new Rectangle(68, 51, 25, 17));
    prv_tipfacE.setBounds(new Rectangle(98, 26, 128, 20));
    prv_nifE.setBounds(new Rectangle(539, 5, 129, 17));
    prv_codfacE.setBounds(new Rectangle(99, 5, 351, 17));
    cli_direeL.setBounds(new Rectangle(1, 52, 86, 17));
    prv_direreE.setBounds(new Rectangle(86, 52, 311, 17));
    cli_pobleL.setBounds(new Rectangle(1, 31, 86, 17));
    prv_poblreE.setBounds(new Rectangle(86, 31, 227, 17));
    prv_coporeE.setBounds(new Rectangle(453, 31, 50, 17));
    prv_nombreE.setBounds(new Rectangle(86, 10, 417, 17));
    cLabel15.setText("Pais");
    cLabel15.setBounds(new Rectangle(7, 184, 65, 20));
    pai_codiE.setAceptaNulo(true);
    pai_codiE.setAncTexto(45);
    pai_codiE.setBounds(new Rectangle(78, 187, 274, 18));
    cLabel19.setText("Fec. Ini Vacaciones");
    cLabel19.setBounds(new Rectangle(280, 104, 111, 18));
    prv_inivacE.setBounds(new Rectangle(387, 104, 80, 18));
    cLabel110.setBounds(new Rectangle(480, 104, 111, 18));
    cLabel110.setText("Fec. Ini Vacaciones");
    prv_finvacE.setBounds(new Rectangle(590, 104, 80, 18));
    PdatCon.setLayout(null);
    cue_codiE.setBounds(new Rectangle(102, 7, 95, 19));
    cLabel21.setBounds(new Rectangle(2, 7, 99, 19));
    cLabel21.setText("Cuenta Contable");
    cLabel20.setText("Libro IVA");
    cLabel20.setBounds(new Rectangle(2, 28, 90, 19));
    prv_libivaE.setAlignmentY((float) 0.5);
    prv_libivaE.setBounds(new Rectangle(102, 28, 35, 19));
    prv_carterE.setBounds(new Rectangle(101, 221, 35, 19));
    cLabel22.setBounds(new Rectangle(3, 221, 71, 19));
    cLabel22.setText("Cartera");
    prv_tipdocE.setBounds(new Rectangle(102, 48, 35, 19));
    cLabel23.setText("Tipo Documento");
    cLabel23.setBounds(new Rectangle(2, 48, 94, 19));
    cLabel24.setBounds(new Rectangle(2, 68, 53, 19));
    cLabel24.setText("Diario");
    prv_diarioE.setBounds(new Rectangle(102, 68, 35, 19));
    prv_sefactE.setBounds(new Rectangle(102, 89, 35, 19));
    cLabel25.setText("Serie Fra. en Ctb");
    cLabel25.setBounds(new Rectangle(2, 89, 97, 19));
    prv_tipivaE.setBounds(new Rectangle(102, 111, 74, 19));
    cli_tipivaL1.setBounds(new Rectangle(2, 111, 58, 19));
    cli_tipivaL1.setText("Tipo IVA");
    prv_porivaE.setBounds(new Rectangle(102, 132, 38, 19));
    cli_porivaL.setBounds(new Rectangle(2, 132, 55, 19));
    cli_porivaL.setText("% de IVA");
    cLabel26.setText("Situacion Fras.");
    cLabel26.setBounds(new Rectangle(2, 153, 91, 19));
    prv_sitfacE.setBounds(new Rectangle(102, 153, 35, 19));
    cLabel27.setText("Organismo Oficial");
    cLabel27.setBounds(new Rectangle(2, 174, 105, 19));
    prv_orgofiE.setBounds(new Rectangle(102, 174, 37, 19));
    cLabel28.setText("Codigo Impuesto");
    cLabel28.setBounds(new Rectangle(2, 196, 98, 19));
    prv_coimivE.setBounds(new Rectangle(102, 196, 35, 19));
    cLabel29.setText("Num. Reg. Selecionados");
    cLabel29.setBounds(new Rectangle(3, 6, 135, 18));
    nRegSelE.setEnabled(false);
    nRegSelE.setBounds(new Rectangle(135, 6, 56, 18));
    nRegE.setBounds(new Rectangle(549, 6, 56, 18));
    nRegE.setEnabled(false);
    cLabel210.setBounds(new Rectangle(487, 6, 66, 18));
    cLabel210.setText("Num. Reg.");
    cLabel31.setText("Fecha Alta");
    cLabel31.setBounds(new Rectangle(360, 187, 63, 18));
    prv_fecaltE.setBounds(new Rectangle(421, 187, 80, 18));
    cLabel32.setBounds(new Rectangle(508, 187, 83, 18));
    cLabel32.setText("Fecha Ult. Mod.");
    prv_feulmoE.setBounds(new Rectangle(589, 187, 80, 18));
    cLabel36.setRequestFocusEnabled(true);
    cLabel36.setText("Empr.");
    cLabel36.setBounds(new Rectangle(421, 226, 39, 18));

    emp_codiE.setBounds(new Rectangle(461, 226, 47, 19));
    prv_telereE.setBounds(new Rectangle(86, 76, 129, 17));
    prv_faxreE.setBounds(new Rectangle(374, 76, 129, 17));
    cLabel37.setText("Fax");
    cLabel37.setBounds(new Rectangle(342, 76, 29, 18));
    cLabel38.setText("Telef. Contacto");
    cLabel38.setBounds(new Rectangle(1, 76, 92, 17));
    cli_codpoeL1.setBounds(new Rectangle(2, 100, 82, 17));
    cli_codpoeL1.setText("Plazo Entrega");
    prv_plzentE.setBounds(new Rectangle(86, 100, 50, 17));
    prv_irpfE.setBounds(new Rectangle(74, 156, 68, 18));
    prv_irpfE.setText("Exento de IVA");
    prv_irpfL.setBounds(new Rectangle(1, 156, 67, 18));
    prv_irpfL.setText("Exento IRPF");
    prv_irpfL1.setText("IRPF sobre BI");
    prv_irpfL1.setBounds(new Rectangle(158, 156, 79, 18));
    prv_rpfsbiE.setText("Exento de IVA");
    prv_rpfsbiE.setBounds(new Rectangle(233, 156, 68, 18));
    prv_disc4E.setAncTexto(30);
    cLabel39.setText("Suministra Desglosado");
    cLabel39.setBounds(new Rectangle(311, 99, 133, 17));
    prv_sumdesE.setText("cComboBox1");
    prv_sumdesE.setBounds(new Rectangle(446, 99, 55, 17));
    cLabel310.setBounds(new Rectangle(-1, 126, 81, 17));
    cLabel310.setText("Aplicar Oreo");
    prv_aploreE.setBounds(new Rectangle(86, 124, 55, 17));
    prv_aploreE.setText("cComboBox1");
    cLabel40.setText("N� Reg. Sanitario");
    cLabel40.setBounds(new Rectangle(421, 277, 98, 18));
    prv_nurgsaE.setBounds(new Rectangle(566, 277, 104, 18));
    cLabel12.setText("Divisa");
    cLabel12.setBounds(new Rectangle(1, 73, 43, 17));
    div_codiE.setCeroIsNull(true);
    div_codiE.setAncTexto(30);
    div_codiE.setBounds(new Rectangle(51, 73, 344, 17));
    cLabel41.setBounds(new Rectangle(421, 254, 87, 18));
    cLabel41.setText("N� Explotacion");
    prv_nexploE.setBounds(new Rectangle(530, 254, 140, 18));
    Pdatmat.setText("cPanel1");
    cLabel13.setBackground(Color.red);
    cLabel13.setForeground(Color.white);
    cLabel13.setOpaque(true);
    cLabel13.setHorizontalAlignment(SwingConstants.CENTER);
    cLabel13.setHorizontalTextPosition(SwingConstants.CENTER);
    cLabel13.setText("Salas Despiece");
    cLabel13.setBounds(new Rectangle(102, 2, 105, 17));
    cLabel14.setText("Mataderos");
    cLabel14.setBounds(new Rectangle(102, 172, 105, 17));
    cLabel14.setHorizontalTextPosition(SwingConstants.CENTER);
    cLabel14.setHorizontalAlignment(SwingConstants.CENTER);
    cLabel14.setOpaque(true);
    cLabel14.setForeground(Color.white);
    cLabel14.setBackground(Color.red);
    Vector v=new Vector();
    v.addElement("Codigo");
    v.addElement("Nombre");
    jtMat.setCabecera(v);
    jtMat.setAnchoColumna(new int[]{60,250});
    jtMat.setAlinearColumna(new int[]{2,0});

    jtMat.setFormatoColumna(0,"####9");
    jtMat.setAjustarGrid(true);

    Vector v1=new Vector();
    v1.addElement("Codigo");
    v1.addElement("Nombre");
    jtSde.setCabecera(v1);
    jtSde.setAnchoColumna(new int[]{60,250});
    jtSde.setAlinearColumna(new int[]{2,0});

    jtSde.setFormatoColumna(0,"####9");
    jtSde.setAjustarGrid(true);
    mat_nombE.setEnabled(false);
    Vector vcMat=new Vector();
    vcMat.addElement(mat_codiE.getTextField());
    vcMat.addElement(mat_nombE);
    jtMat.setCampos(vcMat);

    sde_nombE.setEnabled(false);
    Vector vcSal=new Vector();
    vcSal.addElement(sde_codiE.getTextField());
    vcSal.addElement(sde_nombE);
    jtSde.setCampos(vcSal);
    jtSde.setBounds(new Rectangle(100, 19, 360, 146));

    PdatFra.setLayout(null);
    jtMat.setBounds(new Rectangle(102, 188, 360, 146));
    PdatGen.setDefButtonDisable(false);
    PdatGen.setDefButton(Baceptar);
    Baceptar.setMargin(new Insets(0, 0, 0, 0));
    cLabel7.setText("Fax");
    cLabel2.setText("Nombre Comerc.");
    prv_disc3E.setAncTexto(30);
    cLabel4.setText("Direccion");
    Bcancelar.setMargin(new Insets(0, 0, 0, 0));
    cli_direeL.setText("Dir.Recogida");
    cLabel10.setText("Activo");
    cLabel3.setText("Poblacion");
    cLabel35.setText("Telef. Cto.");
    cLabel1.setText("Proveedor");
    cLabel34.setText("Comentario");

    Pcabe.setButton(KeyEvent.VK_F4, Baceptar);
    PdatGen.setButton(KeyEvent.VK_F4, Baceptar);
    PdatFra.setButton(KeyEvent.VK_F4, Baceptar);
    PdatEnv.setButton(KeyEvent.VK_F4, Baceptar);
    jtSde.setButton(KeyEvent.VK_F4, Baceptar);
    jtMat.setButton(KeyEvent.VK_F4, Baceptar);


    prv_disc2E.setAncTexto(30);
    cLabel6.setText(" Contacto");
    cli_pobleL.setText("Pobl. Recogida");
    cLabel33.setText("Cod Postal");
    cLabel5.setText("Telef.");

    PdatEnv.add(prv_nombreE, null);
    PdatEnv.add(cli_nomenL, null);
    PdatEnv.add(cli_pobleL, null);
    PdatEnv.add(prv_poblreE, null);
    PdatEnv.add(cli_direeL, null);
    PdatEnv.add(prv_coporeE, null);
    PdatEnv.add(cli_codpoeL, null);
    PdatEnv.add(prv_direreE, null);
    PdatEnv.add(prv_telereE, null);
    PdatEnv.add(cLabel38, null);
    PdatEnv.add(cLabel37, null);
    PdatEnv.add(prv_faxreE, null);

    Tpanel.add(PdatGen, "Generales");
    Tpanel.add(PdatFra, "Facturacion");
    Tpanel.add(PdatEnv,  "Recogida");
    Tpanel.add(PdatCon, "Contable");

    Pboton.add(Bcancelar, null);
    Pboton.add(Baceptar, null);
    Pboton.add(cLabel29, null);
    Pboton.add(nRegSelE, null);
    Pboton.add(cLabel210, null);
    Pboton.add(nRegE, null);
    Pprinc.add(Pcabe, BorderLayout.NORTH);
    Pcabe.add(cLabel1, null);
    Pcabe.add(cLabel2, null);
    Pcabe.add(prv_nombE, null);
    Pcabe.add(prv_codiE, null);
    Pprinc.add(Tpanel,  BorderLayout.CENTER);
    Pprinc.add(Pboton, BorderLayout.SOUTH);

    PdatGen.add(prv_faxE, null);
    PdatGen.add(cLabel5, null);
    PdatGen.add(prv_telefE, null);
    PdatGen.add(cLabel7, null);
    PdatGen.add(cLabel33, null);
    PdatGen.add(prv_codpoE, null);
    PdatGen.add(cLabel35, null);
    PdatGen.add(prv_telconE, null);
    PdatGen.add(prv_perconE, null);
    PdatGen.add(cLabel6, null);
    PdatGen.add(prv_activE, null);
    PdatGen.add(cLabel10, null);
    PdatFra.add(prv_codfacE, null);
    PdatFra.add(cli_codfaL, null);
    PdatFra.add(prv_tipfacE, null);
    PdatFra.add(cli_tipfacL, null);
    PdatFra.add(cLabel16, null);
    PdatFra.add(prv_dipa1E, null);
    PdatFra.add(cLabel17, null);
    PdatFra.add(prv_dipa2E, null);
    PdatFra.add(fpa_codiE, null);
    PdatFra.add(prv_nifE, null);
    PdatFra.add(cli_nifL, null);
    PdatFra.add(fpa_codiL, null);
    PdatFra.add(cli_dtoppL, null);
    PdatFra.add(prv_dtoppE, null);
    PdatFra.add(prv_dtocomE, null);
    PdatFra.add(prv_dtorapE, null);
    PdatFra.add(cli_prapelL, null);
    PdatFra.add(cli_pdtocoL, null);
    PdatFra.add(prv_recequE, null);
    PdatFra.add(prv_recequL, null);
    PdatFra.add(prv_exeivaE, null);
    PdatFra.add(prv_exeivaL, null);
    PdatGen.add(prv_poblE, null);
    PdatGen.add(cLabel3, null);
    PdatGen.add(prv_direcE, null);
    PdatGen.add(cLabel4, null);
    PdatCon.add(cLabel21, null);
    PdatCon.add(cue_codiE, null);
    PdatCon.add(prv_libivaE, null);
    PdatCon.add(cLabel20, null);
    PdatCon.add(prv_tipdocE, null);
    PdatCon.add(cLabel23, null);
    PdatCon.add(prv_diarioE, null);
    PdatCon.add(cLabel24, null);
    PdatCon.add(prv_sefactE, null);
    PdatCon.add(cLabel25, null);
    PdatCon.add(prv_porivaE, null);
    PdatCon.add(cli_porivaL, null);
    PdatCon.add(prv_sitfacE, null);
    PdatCon.add(cLabel26, null);
    PdatCon.add(prv_orgofiE, null);
    PdatCon.add(cLabel27, null);
    PdatCon.add(prv_coimivE, null);
    PdatCon.add(cLabel28, null);
    PdatCon.add(cLabel22, null);
    PdatCon.add(prv_carterE, null);
    PdatCon.add(prv_tipivaE, null);
    PdatCon.add(cli_tipivaL1, null);
    Tpanel.add(Pdatmat,   "Matad/S.Desp.");
    PdatEnv.add(prv_plzentE, null);
    PdatEnv.add(cli_codpoeL1, null);
    PdatFra.add(prv_irpfE, null);
    PdatFra.add(prv_irpfL, null);
    PdatFra.add(prv_irpfL1, null);
    PdatFra.add(prv_rpfsbiE, null);
    PdatEnv.add(prv_sumdesE, null);
    PdatEnv.add(cLabel39, null);
    PdatEnv.add(cLabel310, null);
    PdatEnv.add(prv_aploreE, null);
    PdatGen.add(prv_nomcoE, null);
    PdatGen.add(cLabel11, null);
    PdatFra.add(cLabel12, null);
    PdatFra.add(div_codiE, null);
    Pdatmat.setLayout(null);
    Pdatmat.add(jtSde, null);
    Pdatmat.add(cLabel13, null);
    Pdatmat.add(jtMat, null);
    Pdatmat.add(cLabel14, null);
    Pdiscrim.add(prv_disc2E, null);
    Pdiscrim.add(prv_disc4L, null);
    Pdiscrim.add(prv_disc3L, null);
    Pdiscrim.add(prv_disc2L, null);
    Pdiscrim.add(prv_disc3E, null);
    Pdiscrim.add(prv_disc4E, null);
    PdatGen.add(emp_nombL, null);
    PdatGen.add(prv_internE, null);
    PdatGen.add(prv_internL, null);
    PdatGen.add(prv_nurgsaE, null);
    PdatGen.add(cLabel40, null);
    PdatGen.add(prv_nexploE, null);
    PdatGen.add(cLabel41, null);
    PdatGen.add(emp_codiE, null);
    PdatGen.add(cLabel36, null);
    PdatGen.add(prv_finvacE, null);
    PdatGen.add(tar_codiE, null);
    PdatGen.add(cLabel34, null);
    PdatGen.add(jScrollPane1, null);
    PdatGen.add(tar_codiL, null);
    PdatGen.add(cLabel15, null);
    PdatGen.add(pai_codiE, null);
    PdatGen.add(cLabel19, null);
    PdatGen.add(cLabel110, null);
    PdatGen.add(prv_inivacE, null);
    PdatGen.add(cLabel31, null);
    PdatGen.add(prv_fecaltE, null);
    PdatGen.add(prv_feulmoE, null);
    PdatGen.add(cLabel32, null);
    PdatGen.add(Pdiscrim, null);
    jScrollPane1.getViewport().add(prv_observT, null);
    Baceptar.setText("Aceptar F4");
  }

  @Override
  public void iniciarVentana() throws Exception
  {
    mat_codiE.setMatNomb(null);
    mat_codiE.iniciar(dtStat,this,vl,EU);
    sde_codiE.setMatNomb(null);
    sde_codiE.iniciar(dtStat,this,vl,EU);

    
    prv_internE.addItem("No","0");
    prv_internE.addItem("Si","1");

    prv_codfacE.iniciar(dtStat,this,vl,EU);
    fpa_codiE.iniciar(dtStat,this,vl,EU);

    gnu.chu.anjelica.pad.pdconfig.llenaDiscr(dtStat,prv_disc2E,"P2",EU.em_cod);
    gnu.chu.anjelica.pad.pdconfig.llenaDiscr(dtStat,prv_disc3E,"P3",EU.em_cod);
    gnu.chu.anjelica.pad.pdconfig.llenaDiscr(dtStat,prv_disc4E,"P4",EU.em_cod);
    prv_disc2L.setText(pdconfig.getNombreDiscr(EU.em_cod,"P2",dtStat));
    prv_disc3L.setText(pdconfig.getNombreDiscr(EU.em_cod,"P3",dtStat));
    prv_disc4L.setText(pdconfig.getNombreDiscr(EU.em_cod,"P4",dtStat));

    prv_activE.addItem("Si","S");
    prv_activE.addItem("No","N");

    s="SELECT tar_codi,tar_nomb FROM tipotari ORDER BY tar_codi";
    dtStat.select(s);
    tar_codiE.addDatos(dtStat);
    tar_codiE.setFormato(true);
    tar_codiE.setFormato(Types.DECIMAL,"#9",2);

    s="SELECT pai_codi,pai_nomb FROM v_paises ORDER BY pai_nomb";
    dtStat.select(s);
    pai_codiE.setFormato(Types.DECIMAL,"###9",2);
    pai_codiE.setFormato(true);
    pai_codiE.addDatos(dtStat);

    prv_tipfacE.addItem("Diario","D");
    prv_tipfacE.addItem("Semanal","S");
    prv_tipfacE.addItem("Quincenal","Q");
    prv_tipfacE.addItem("Mensual","M");
    MantTipoIVA.llenaComboIVA(prv_tipivaE, dtStat);
   

    emp_codiE.iniciar(dtStat,this,vl,EU);
    emp_codiE.setLabelEmp(emp_nombL);

    prv_exeivaE.addItem("No","0");
    prv_exeivaE.addItem("Si","-1");

    prv_irpfE.addItem("No","0");
    prv_irpfE.addItem("Si","-1");

    prv_recequE.addItem("No","0");
    prv_recequE.addItem("Si","-1");

    prv_sumdesE.addItem("No","0");
    prv_sumdesE.addItem("Si","-1");

    prv_rpfsbiE.addItem("No","0");
    prv_rpfsbiE.addItem("Si","-1");

    prv_aploreE.addItem("No","0");
    prv_aploreE.addItem("Si","-1");


    div_codiE.setFormato(true);
    div_codiE.setFormato(Types.DECIMAL,"#9",2);
    s="SELECT div_codi,div_codedi FROM v_divisa ORDER BY div_codedi";
    dtStat.select(s);
    div_codiE.addDatos(dtStat);

    emp_codiE.setColumnaAlias("emp_codi");
    prv_codiE.setColumnaAlias("prv_codi");
    prv_nombE.setColumnaAlias("prv_nomb");
    prv_nomcoE.setColumnaAlias("prv_nomco");
    prv_direcE.setColumnaAlias("prv_direc");
    prv_poblE.setColumnaAlias("prv_pobl");
    prv_codpoE.setColumnaAlias("prv_codpo");
    prv_telefE.setColumnaAlias("prv_telef");
    prv_faxE.setColumnaAlias("prv_fax");
    prv_nifE.setColumnaAlias("prv_nif");
    prv_perconE.setColumnaAlias("prv_percon");
    prv_telconE.setColumnaAlias("prv_telcon");
    prv_nombreE.setColumnaAlias("prv_nombre");
    prv_direreE.setColumnaAlias("prv_direre");
    prv_poblreE.setColumnaAlias("prv_poblre");
    prv_coporeE.setColumnaAlias("prv_copore");
    prv_telereE.setColumnaAlias("prv_telere");
    prv_faxreE.setColumnaAlias("prv_faxre");
    prv_plzentE.setColumnaAlias("prv_plzent");
    prv_codfacE.setColumnaAlias("prv_codfac");
    prv_tipfacE.setColumnaAlias("prv_tipfacE");
    fpa_codiE.setColumnaAlias("fpa_codi");
    prv_dipa1E.setColumnaAlias("prv_dipa1");
    prv_dipa2E.setColumnaAlias("prv_dipa2");
    prv_inivacE.setColumnaAlias("prv_inivac");
    prv_finvacE.setColumnaAlias("prv_finvac");
    prv_disc2E.setColumnaAlias("prv_disc2");
    prv_disc3E.setColumnaAlias("prv_disc3");
    prv_activE.setColumnaAlias("prv_activ");
    prv_libivaE.setColumnaAlias("prv_libiva");
    prv_carterE.setColumnaAlias("prv_carter");
    prv_diarioE.setColumnaAlias("prv_diario");
    prv_sefactE.setColumnaAlias("prv_sefact");
    prv_dtoppE.setColumnaAlias("prv_dtopp");
    tar_codiE.setColumnaAlias("tar_codi");
    prv_internE.setColumnaAlias("prv_intern");
    prv_recequE.setColumnaAlias("prv_recequ");
    pai_codiE.setColumnaAlias("pai_codi");
    cue_codiE.setColumnaAlias("cue_codi");
    prv_exeivaE.setColumnaAlias("prv_exeiva");
    prv_tipivaE.setColumnaAlias("prv_tipiva");
    prv_porivaE.setColumnaAlias("prv_poriva");
    prv_tipdocE.setColumnaAlias("prv_tipdoc");
    prv_sitfacE.setColumnaAlias("prv_sitfac");
    prv_orgofiE.setColumnaAlias("prv_orgofi");
    prv_coimivE.setColumnaAlias("prv_coimiv");
    prv_dtocomE.setColumnaAlias("prv_dtocom");
    prv_dtorapE.setColumnaAlias("prv_dtorap");
    prv_fecaltE.setColumnaAlias("prv_fecalt");
    prv_feulmoE.setColumnaAlias("prv_feulmo");

    activaTodo();
    verDatos();
    activarEventos();
  }
  void activarEventos()
  {
    prv_codiE.addFocusListener(new FocusAdapter()
    {
      public void focusLost(FocusEvent e)
      {
        try {
          if (prv_codfacE.getValorInt() == 0 && nav.getPulsado()!=navegador.QUERY)
          {
            prv_codfacE.setValorInt(prv_codiE.getValorInt());
            prv_codfacE.controla(false);
          }
        } catch (Exception k)
        {
          Error("Error al comprobar codigo de Proveedor",k);
          return;
        }
        if (nav.pulsado == navegador.ADDNEW)
          verDatOtrCli();
      }
    });
    prv_tipivaE.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        if (!prv_tipivaE.isEnabled() || nav.pulsado==navegador.QUERY)
          return;
        try {
          double porcIva=MantTipoIVA.getPorcIva(dtStat, prv_tipivaE.getValorInt());
     
          if (porcIva<0)
            return;
          prv_porivaE.setValorDec(porcIva);
        } catch (Exception k)
        {
          Error("Error al buscar tipo de IVA",k);
        }
      }
    });
  }
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
    @Override
  public void PADUltimo()
  {
    nRegE.setValorDec(nRegSelE.getValorInt());
    verDatos();
  }

    @Override
  public void ej_query1()
  {
    Component c = Pcabe.getErrorConf();
    if (c != null)
    {
      mensajeErr("(datGen) Condiciones de Busqueda NO validas");
      Tpanel.setSelectedIndex(0);
      c.requestFocus();
      return;
    }

    c = PdatGen.getErrorConf();
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
    v.add(prv_codiE.getStrQuery());
    v.add(prv_nombE.getStrQuery());
    v.add(prv_nomcoE.getStrQuery());
    v.add(prv_direcE.getStrQuery());
    v.add(prv_poblE.getStrQuery());
    v.add(prv_codpoE.getStrQuery());
    v.add(prv_telefE.getStrQuery());
    v.add(prv_faxE.getStrQuery());
    v.add(prv_nifE.getStrQuery());
    v.add(prv_perconE.getStrQuery());
    v.add(prv_telconE.getStrQuery());
    v.add(prv_nombreE.getStrQuery());
    v.add(prv_direreE.getStrQuery());

    v.add(prv_telereE.getStrQuery());
    v.add(prv_faxreE.getStrQuery());
    v.add(prv_plzentE.getStrQuery());
    v.add(prv_poblreE.getStrQuery());
    v.add(prv_coporeE.getStrQuery());
    v.add(prv_codfacE.getStrQuery());
    v.add(prv_tipfacE.getStrQuery());
    v.add(fpa_codiE.getStrQuery());
    v.add(prv_dipa1E.getStrQuery());
    v.add(prv_dipa2E.getStrQuery());
    v.add(prv_inivacE.getStrQuery());
    v.add(prv_finvacE.getStrQuery());
    v.add(prv_disc2E.getStrQuery());
    v.add(prv_disc3E.getStrQuery());
    v.add(prv_activE.getStrQuery());
    v.add(prv_libivaE.getStrQuery());
    v.add(prv_carterE.getStrQuery());
    v.add(prv_diarioE.getStrQuery());
    v.add(prv_sefactE.getStrQuery());
    v.add(prv_dtoppE.getStrQuery());
    v.add(tar_codiE.getStrQuery());

    v.add(prv_recequE.getStrQuery());
    v.add(pai_codiE.getStrQuery());
    v.add(cue_codiE.getStrQuery());
    v.add(prv_exeivaE.getStrQuery());
    v.add(prv_tipivaE.getStrQuery());
    v.add(prv_porivaE.getStrQuery());
    v.add(prv_tipdocE.getStrQuery());
    v.add(prv_sitfacE.getStrQuery());
    v.add(prv_orgofiE.getStrQuery());
    v.add(prv_coimivE.getStrQuery());
    v.add(prv_dtocomE.getStrQuery());
    v.add(prv_dtorapE.getStrQuery());
    v.add(pai_codiE.getStrQuery());
    v.add(prv_fecaltE.getStrQuery());
    v.add(prv_feulmoE.getStrQuery());
    v.add(prv_internE.getStrQuery());
    Pcabe.setQuery(false);
    PdatGen.setQuery(false);
    PdatEnv.setQuery(false);
    PdatFra.setQuery(false);
    PdatCon.setQuery(false);

    try
    {
      s = "SELECT * FROM v_proveedo ";
      s = creaWhere(s, v, true);
      s += " ORDER BY prv_activ DESC,prv_codi";
      this.setEnabled(false);
      mensaje("Espere, por favor ... buscando datos");
//      debug(s);
      if (!dtCon1.select(s))
      {
        msgBox("No encontrados Proveedores con estos criterios");
        mensaje("");
        verDatos();
        activaTodo();
        this.setEnabled(true);
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
    @Override
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

  public boolean checkEdit()
  {
    try
    {
      if (!emp_codiE.controla())
      {
        mensajeErr("Empresa NO Valida");
        return false;
      }

      if (prv_nombE.isNull())
      {
        mensajeErr("Introduzca Nombre del Proveedor");
        prv_nombE.requestFocus();
        return false;
      }
      if (prv_nomcoE.isNull())
      {
        mensajeErr("Introduzca Nombre Comercial del Proveedor");
        prv_nomcoE.requestFocus();
        return false;
      }

      if (prv_poblE.isNull())
      {
        mensajeErr("Introduzca Poblacion del Proveedor");
        prv_poblE.requestFocus();
        return false;
      }
      if (prv_codpoE.getValorInt() == 0)
      {
        mensajeErr("Introduzca Codigo Postal del Proveedor");
        prv_codpoE.requestFocus();
        return false;
      }
      if (prv_direcE.isNull())
      {
        mensajeErr("Introduzca Direccion del Proveedor");
        prv_direcE.requestFocus();
        return false;
      }
      if (!checkSde())
        return false;
      if (!checkMat())
        return false;

      if (!pai_codiE.controla())
      {
        mensajeErr("Pais NO VALIDO");
        pai_codiE.requestFocus();
        return false;
      }
      if (prv_codfacE.getValorInt() != prv_codiE.getValorInt())
      {
        if (!prv_codfacE.controlar())
        {
          mensajeErr(prv_codfacE.getMsgError());
          return false;
        }
      }
      if (prv_nifE.isNull())
      {
        mensajeErr("Introduzca NIF del Proveedor");
        prv_nifE.requestFocus();
        return false;
      }

      if (!fpa_codiE.controlar())
      {
        mensajeErr("Introduzca Forma de Pago del Proveedor");
        return false;
      }
      if (tar_codiE.isNull())
      {
        mensajeErr("Introduzca Tarifa del Proveedor");
        tar_codiE.requestFocus();
        return false;
      }

    }
    catch (Exception k)
    {
      Error("Error al Controlar Campos", k);
    }

    return true;
  }

  public void ej_edit1()
  {
   jtSde.procesaAllFoco();
   jtMat.procesaAllFoco();
   try
   {
     dtAdd.edit();
     actDatos();
     resetBloqueo(dtAdd, "v_proveedo", prv_codiE.getText()+"-"+emp_codiE.getValorInt());
   }
   catch (Exception k)
   {
     Error("Error al actualizar Datos\n"+s,k);
   }
   activaTodo();
   nav.pulsado=navegador.NINGUNO;
   mensaje("");
   mensajeErr("Proveedor .... MODIFICADO");
  }

  private void actDatos() throws ParseException, SQLException
  {
    dtAdd.setDato("prv_nomb",prv_nombE.getText());
    dtAdd.setDato("prv_nomco",prv_nomcoE.getText());
    dtAdd.setDato("prv_direc",prv_direcE.getText());
    dtAdd.setDato("prv_pobl",prv_poblE.getText());
    dtAdd.setDato("prv_codpo",prv_codpoE.getText());
    dtAdd.setDato("pai_codi",pai_codiE.getValorInt());
    dtAdd.setDato("prv_telef",prv_telefE.getText());
    dtAdd.setDato("prv_fax",prv_faxE.getText());
    dtAdd.setDato("prv_nif",prv_nifE.getText());
    dtAdd.setDato("prv_percon",prv_perconE.getText());
    dtAdd.setDato("prv_telcon",prv_telconE.getText());

    dtAdd.setDato("prv_nombre",prv_nombreE.getText());
    dtAdd.setDato("prv_direre",prv_direreE.getText());
    dtAdd.setDato("prv_poblre",prv_poblreE.getText());
    dtAdd.setDato("prv_copore",prv_coporeE.getValorInt());
    dtAdd.setDato("prv_telere",prv_telereE.getText());
    dtAdd.setDato("prv_faxre",prv_faxreE.getText());
    dtAdd.setDato("prv_plzent",prv_plzentE.getValorInt());
    dtAdd.setDato("tar_codi",tar_codiE.getValorInt());
    dtAdd.setDato("prv_codfac",prv_codfacE.getText());
    dtAdd.setDato("prv_tipfac",prv_tipfacE.getValor());
    dtAdd.setDato("fpa_codi",fpa_codiE.getValorInt());
    dtAdd.setDato("prv_dipa1",prv_dipa1E.getValorInt());
    dtAdd.setDato("prv_dipa2",prv_dipa2E.getValorInt());
    dtAdd.setDato("prv_inivac",prv_inivacE.getFecha(),"dd-MM-yyyy");
    dtAdd.setDato("prv_finvac",prv_finvacE.getFecha(),"dd-MM-yyyy");
    dtAdd.setDato("prv_recequ",prv_recequE.getValor());
    dtAdd.setDato("cue_codi",cue_codiE.getText());
    dtAdd.setDato("prv_exeiva",prv_exeivaE.getValor());
    dtAdd.setDato("prv_irpf",prv_irpfE.getValor());
    dtAdd.setDato("prv_rpfsbi",prv_rpfsbiE.getValor());
    dtAdd.setDato("prv_activ",prv_activE.getValor());
    dtAdd.setDato("prv_disc2",prv_disc2E.getText());
    dtAdd.setDato("prv_disc3",prv_disc3E.getText());
    dtAdd.setDato("prv_disc4",prv_disc4E.getText());
    dtAdd.setDato("prv_libiva",prv_libivaE.getText());
    dtAdd.setDato("prv_sumdes",prv_sumdesE.getValor());
    dtAdd.setDato("prv_observ",prv_observT.getText());
    dtAdd.setDato("prv_carter",prv_carterE.getText());
    dtAdd.setDato("prv_diario",prv_diarioE.getText());
    dtAdd.setDato("prv_sefact",prv_sefactE.getText());
    dtAdd.setDato("prv_tipiva",prv_tipivaE.getValor());
    dtAdd.setDato("prv_poriva",prv_porivaE.getText());
    dtAdd.setDato("prv_tipdoc",prv_tipdocE.getText());
    dtAdd.setDato("prv_sitfac",prv_sitfacE.getText());
    dtAdd.setDato("prv_orgofi",prv_orgofiE.getValorInt());
    dtAdd.setDato("prv_aplore",prv_aploreE.getValor());
    dtAdd.setDato("prv_nexplo",prv_nexploE.getText());
    dtAdd.setDato("prv_nurgsa",prv_nurgsaE.getText());
    dtAdd.setDato("div_codi",div_codiE.getText());
    dtAdd.setDato("prv_coimiv",prv_coimivE.getText());
    dtAdd.setDato("prv_dtocom",prv_dtocomE.getValorDec());
    dtAdd.setDato("prv_dtopp",prv_dtoppE.getValorDec());
    dtAdd.setDato("prv_dtorap",prv_dtorapE.getValorDec());
    dtAdd.setDato("prv_feulmo",Formatear.getFechaAct("dd-MM-yyyy"),"dd-MM-yyyy");
    dtAdd.setDato("prv_intern",prv_internE.getValorInt());
    dtAdd.update(stUp);
    s="delete from v_prvsade where prv_codi = "+prv_codiE.getValorInt();
    stUp.executeUpdate(s);
    s="delete from v_prvmata where prv_codi = "+prv_codiE.getValorInt();
    stUp.executeUpdate(s);
    int nRow=jtSde.getRowCount();
    for (int n=0;n<nRow;n++)
    {
      if (jtSde.getValorDec(n,0)==0)
        continue;
      insSalaDesp(prv_codiE.getValorInt(),jtSde.getValorInt(n,0),dtAdd);
    }
    nRow=jtMat.getRowCount();
    for (int n=0;n<nRow;n++)
    {
      if (jtMat.getValorDec(n,0)==0)
        continue;
      insMatadero(prv_codiE.getValorInt(),jtMat.getValorInt(n,0),dtAdd);
    }
    ctUp.commit();
  }
  public static void insSalaDesp(int prvCodi, int sdeCodi, DatosTabla dt) throws SQLException
  {
    String s="INSERT INTO v_prvsade VALUES ("+prvCodi+","+sdeCodi+")";
    dt.executeUpdate(s);
  }
  public static void insMatadero(int prvCodi, int matCodi, DatosTabla dt) throws SQLException {
    String s = "INSERT INTO v_prvmata VALUES (" + prvCodi+ "," + matCodi+ ")";
    dt.executeUpdate(s);
  }
  public void canc_edit()
  {
    try
    {
      resetBloqueo(dtAdd, "v_proveedo", prv_codiE.getText()+"-"+emp_codiE.getValorInt());
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

     if (prv_codiE.getValorInt()==0)
     {
       if (nav.pulsado==navegador.ADDNEW)
       {
           int antNumero=0;
           s="SELECT prv_codi FROM v_proveedo order by prv_codi";
           if (dtStat.select(s))
           {            
             do
             {
                if (antNumero>0 && dtStat.getInt("prv_codi")>antNumero+1)
                    break;
                antNumero=dtStat.getInt("prv_codi");
             } while (dtStat.next()); 
            }
           prv_codiE.setValorInt(antNumero+1);
       }
       mensajeErr("Introduzca CODIGO de PROVEEDOR");
       prv_codiE.requestFocus();
       return false;
     }
     s="select * from v_proveedo WHERE prv_codi = "+prv_codiE.getValorInt()+
         " and emp_codi ="+emp_codiE.getValorInt();
     if (dtStat.select(s))
     {
       mensajeErr("Codigo asignado a: "+dtStat.getString("prv_nomb"));
       prv_codiE.requestFocus();
       return false;
     }
     return checkEdit();
   } catch (Exception k)
   {
     Error("Error al chequear datos de Proveedor",k);
   }
   return false;
 }

  @Override
  public void ej_addnew1()
  {
    jtSde.salirGrid();
    jtMat.salirGrid();
    try
    {
      dtAdd.addNew("v_proveedo");
      dtAdd.setDato("prv_codi",prv_codiE.getValorInt());
      dtAdd.setDato("emp_codi",emp_codiE.getValorInt());
      dtAdd.setDato("prv_fecalt",Formatear.getFechaAct("dd-MM-yyyy"),"dd-MM-yyyy");
      actDatos();
    }
    catch (Exception k)
    {
      Error("Error al actualizar Datos", k);
    }
    activaTodo();
    nav.pulsado = navegador.NINGUNO;
    mensaje("");
    mensajeErr("Proveedor .... INSERTADO");
  }
  public void canc_addnew()
  {
      activaTodo();
      mensaje("");
      verDatos();
      mensajeErr("INSERCION ... CANCELADA");
      nav.pulsado=navegador.NINGUNO;
  }

  public void activar(boolean b)
  {
    PdatGen.setEnabled(b);
    PdatEnv.setEnabled(b);
    PdatFra.setEnabled(b);
    PdatCon.setEnabled(b);
    Pcabe.setEnabled(b);
    Baceptar.setEnabled(b);
    Bcancelar.setEnabled(b);
    prv_observT.setEnabled(b);
    prv_codiE.setEnabled(b);
    emp_codiE.setEnabled(b);
    jtSde.setEnabled(b);
    jtMat.setEnabled(b);

  }
  public void PADQuery(){
    activar(true);
    Pcabe.setQuery(true);
    PdatGen.setQuery(true);
    PdatEnv.setQuery(true);
    PdatFra.setQuery(true);
    PdatCon.setQuery(true);

    PdatGen.resetTexto();
    Pcabe.resetTexto();
    PdatEnv.resetTexto();
    PdatFra.resetTexto();
    PdatCon.resetTexto();
    emp_codiE.setValorInt(EU.em_cod);
    prv_observT.setEnabled(false);
    prv_nombE.requestFocus();
    prv_activE.setValor("S");
    mensaje("Introduzca Condiciones de Busqueda");
  }
  public void PADAddNew()
 {

   mensaje("Insertando ... NUEVO PROVEEDOR");
   jtMat.removeAllDatos();
   jtSde.removeAllDatos();
   activar(true);
   jtMat.requestFocusInicio();
   jtSde.requestFocusInicio();
   PdatGen.resetTexto();
   PdatEnv.resetTexto();
   PdatFra.resetTexto();
   PdatCon.resetTexto();
   Pcabe.resetTexto();
   prv_fecaltE.setEnabled(false);
   prv_feulmoE.setEnabled(false);
   prv_observT.setText("");
   prv_tipivaE.setValor("1"); // Iva del 7
   prv_recequE.setValor("0");
   pai_codiE.setText("11"); // Espa�a
//   emp_codiE.setEnabled(false);
   emp_codiE.setValorInt(EU.em_cod);
   prv_codiE.requestFocus();
 }

  public void PADEdit(){
    activar(true);
    prv_codiE.setEnabled(false);
//    emp_codiE.setEnabled(false);
    mensaje("Modificando Datos de Proveedores");
    try
    {
      if (!setBloqueo(dtAdd, "v_proveedo", prv_codiE.getText()+"-"+emp_codiE.getValorInt()))
      {
        msgBox(msgBloqueo);
        nav.pulsado = navegador.NINGUNO;
        activaTodo();
        return;
      }

      s="SELECT * FROM v_proveedo WHERE prv_codi ="+prv_codiE.getValorInt()+
          " AND emp_codi = "+emp_codiE.getValorInt();
      if (!dtAdd.select(s,true))
      {
        mensajeErr("Registro NO encontrado ... PROBABLEMENTE SE HA BORRADO");
        nav.pulsado=navegador.NINGUNO;
        activaTodo();
        return;
      }

    } catch (Exception k)
    {
      Error("Error al bloquear Registro de Proveedor",k);
      return;
    }
    prv_fecaltE.setEnabled(false);
    prv_feulmoE.setEnabled(false);
    prv_nombE.requestFocus();
  }
  void verDatos()
  {
    verDatos(dtCons);
  }

  /**
   * Muestra los Datos en pantalla del cursor dtCons
   * @param dt DatosTabla
   */
  void verDatos(DatosTabla dt)
  {
    if (dt.getNOREG())
      return;
    try {
      s="SELECT * FROM v_proveedo where prv_codi = "+dt.getInt("prv_codi")+
          " and emp_codi = "+dt.getInt("emp_codi");
      if (! dtCon1.select(s))
      {
        msgBox("Proveedor: "+dt.getInt("prv_codi")+" REGISTRO NO ENCONTRADO ... PROBABLEMENTE SE HA BORRADO");
        Pcabe.resetTexto();
        PdatGen.resetTexto();
        PdatFra.resetTexto();
        PdatEnv.resetTexto();
        PdatCon.resetTexto();
        prv_codiE.setText(dt.getString("prv_codi"));
        emp_codiE.setText(dt.getString("emp_codi"));
        return;
      }
    emp_codiE.setValorInt(dtCon1.getInt("emp_codi"));
    prv_codiE.setText(dtCon1.getString("prv_codi"));
    prv_nombE.setText(dtCon1.getString("prv_nomb"));
    prv_nomcoE.setText(dtCon1.getString("prv_nomco"));
    prv_direcE.setText(dtCon1.getString("prv_direc"));
    prv_poblE.setText(dtCon1.getString("prv_pobl"));
    prv_codpoE.setText(dtCon1.getString("prv_codpo"));
    pai_codiE.setText(dtCon1.getString("pai_codi"));
    prv_telefE.setText(dtCon1.getString("prv_telef"));
    prv_faxE.setText(dtCon1.getString("prv_fax"));
    prv_nifE.setText(dtCon1.getString("prv_nif"));
    prv_perconE.setText(dtCon1.getString("prv_percon"));
    prv_telconE.setText(dtCon1.getString("prv_telcon"));
    prv_nombreE.setText(dtCon1.getString("prv_nombre"));
    prv_direreE.setText(dtCon1.getString("prv_direre"));
    prv_poblreE.setText(dtCon1.getString("prv_poblre"));
    prv_coporeE.setText(dtCon1.getString("prv_copore"));
    prv_telereE.setText(dtCon1.getString("prv_telere"));
    prv_faxreE.setText(dtCon1.getString("prv_faxre"));
    prv_plzentE.setText(dtCon1.getString("prv_plzent"));
    tar_codiE.setText(dtCon1.getString("tar_codi"));
    prv_codfacE.setText(dtCon1.getString("prv_codfac"));
    prv_tipfacE.setValor(dtCon1.getString("prv_tipfac"));
    fpa_codiE.setText(dtCon1.getString("fpa_codi"));
    prv_dipa1E.setText(dtCon1.getString("prv_dipa1"));
    prv_dipa2E.setText(dtCon1.getString("prv_dipa2"));
    prv_inivacE.setText(dtCon1.getString("prv_inivac"));
    prv_finvacE.setText(dtCon1.getString("prv_finvac"));
    prv_recequE.setValor(dtCon1.getString("prv_recequ"));
    cue_codiE.setText(dtCon1.getString("cue_codi"));
    prv_exeivaE.setValor(dtCon1.getString("prv_exeiva"));
    prv_irpfE.setValor(dtCon1.getString("prv_irpf")); // Nuevo
    prv_rpfsbiE.setValor(dtCon1.getString("prv_rpfsbi")); // Nuevo
    prv_activE.setValor(dtCon1.getString("prv_activ")); // Nuevo
    prv_disc2E.setText(dtCon1.getString("prv_disc2"));
    prv_disc3E.setText(dtCon1.getString("prv_disc3"));
    prv_disc4E.setText(dtCon1.getString("prv_disc4"));
    prv_libivaE.setText(dtCon1.getString("prv_libiva"));
    prv_sumdesE.setValor(dtCon1.getString("prv_sumdes")); // Nuevo
    prv_observT.setText(dtCon1.getString("prv_observ"));
    prv_carterE.setText(dtCon1.getString("prv_carter"));
    prv_diarioE.setText(dtCon1.getString("prv_diario"));
    prv_sefactE.setText(dtCon1.getString("prv_sefact"));
    prv_tipivaE.setValor(dtCon1.getString("prv_tipiva"));
    prv_porivaE.setText(dtCon1.getString("prv_poriva"));
    prv_tipdocE.setText(dtCon1.getString("prv_tipdoc"));
    prv_sitfacE.setText(dtCon1.getString("prv_sitfac"));
    prv_orgofiE.setText(dtCon1.getString("prv_orgofi"));
    prv_aploreE.setValor(dtCon1.getString("prv_aplore")); // Nuevo
    prv_nexploE.setText(dtCon1.getString("prv_nexplo")); // Nuevo
    prv_nurgsaE.setText(dtCon1.getString("prv_nurgsa")); // Nuevo
    div_codiE.setText(dtCon1.getString("div_codi")); // Nuevo
    prv_coimivE.setText(dtCon1.getString("prv_coimiv"));
    prv_dtocomE.setText(dtCon1.getString("prv_dtocom"));
    prv_dtoppE.setText(dtCon1.getString("prv_dtopp"));
    prv_dtorapE.setText(dtCon1.getString("prv_dtorap"));
    prv_fecaltE.setText(dtCon1.getFecha("prv_fecalt","dd-MM-yyyy"));
    prv_feulmoE.setText(dtCon1.getFecha("prv_feulmo","dd-MM-yyyy"));
    prv_internE.setValor(dtCon1.getString("prv_intern"));
    jtSde.removeAllDatos();
    jtMat.removeAllDatos();
    s="select p.sde_codi,s.sde_nomb from v_prvsade as p,v_saladesp S"+
       " WHERE p.prv_codi = "+prv_codiE.getValorInt()+
       " and s.sde_codi = p.sde_codi "+
       " order by p.sde_codi ";
    if (dtStat.select(s))
    {
      do
      {
        Vector v = new Vector();
        v.addElement(dtStat.getString("sde_codi"));
        v.addElement(dtStat.getString("sde_nomb"));
        jtSde.addLinea(v);
      }
      while (dtStat.next());
    }
    jtSde.requestFocusInicio();
    s = "select p.mat_codi,m.mat_nomb from v_prvmata as p,v_matadero m" +
        " WHERE p.prv_codi = " + prv_codiE.getValorInt() +
        " and p.mat_codi = m.mat_codi " +
        " order by p.mat_codi ";
    if (dtStat.select(s))
    {
      do
      {
        Vector v = new Vector();
        v.addElement(dtStat.getString("mat_codi"));
        v.addElement(dtStat.getString("mat_nomb"));
        jtMat.addLinea(v);
      }
      while (dtStat.next());
    }
    jtMat.requestFocusInicio();


    } catch (Exception k)
    {
      Error("Error al Ver datos",k);
    }
  }

  public void PADDelete()
  {
    Baceptar.setEnabled(true);
    Bcancelar.setEnabled(true);
    mensaje("Borrando Proveedor ....");
    try
    {
      s="SELECT * FROM v_albacoc WHERE prv_codi = "+prv_codiE.getValorInt()+
          " and emp_codi = "+emp_codiE.getValorInt();
      if (dtStat.select(s))
      {
       msgBox("Proveedor TIENE Albaranes de Compra ... IMPOSIBLE BORRAR");
       nav.pulsado=navegador.NINGUNO;
       activaTodo();
       return;
      }
      if (!setBloqueo(dtAdd, "v_proveedo", prv_codiE.getText()+"-"+emp_codiE.getValorInt()))
      {
        msgBox(msgBloqueo);
        nav.pulsado=navegador.NINGUNO;
        activaTodo();
        return;
      }
      s="SELECT * FROM v_proveedo WHERE prv_codi ="+prv_codiE.getValorInt()+
           " and emp_codi = "+emp_codiE.getValorInt();
      if (! dtAdd.select(s,true))
      {
        msgBox("Proveedor NO ENCONTRADO .. PROBABLEMENTE SE HAYA BORRADO");
        nav.pulsado=navegador.NINGUNO;
        activaTodo();
        return;
      }
    } catch (Exception k)
    {
      Error("Error al bloquear Registro de Proveedor",k);
    }
    Bcancelar.requestFocus();
  }

  public void canc_delete()
  {
    try
    {
      resetBloqueo(dtAdd, "v_proveedo", prv_codiE.getText()+"-"+emp_codiE.getValorInt());
    }
    catch (Exception k)
    {
      mensajes.mensajeAviso("Error al Quitar el bloqueo\n" + k.getMessage());
    }

    activaTodo();
    mensaje("");
    verDatos();
    mensajeErr("Borrado ... CANCELADO");
    nav.pulsado = navegador.NINGUNO;
  }

  public void ej_delete1()
  {
    try
     {
       dtAdd.delete(stUp);
       s = "delete from v_prvsade where prv_codi = " + prv_codiE.getValorInt();
       stUp.executeUpdate(s);
       s = "delete from v_prvmata where prv_codi = " + prv_codiE.getValorInt();
       stUp.executeUpdate(s);
       resetBloqueo(dtAdd, "v_proveedo", prv_codiE.getText()+"-"+emp_codiE.getValorInt());
       rgSelect();
     }
     catch (Exception k)
     {
       Error("Error al actualizar Datos",k);
     }
     activaTodo();
     nav.pulsado=navegador.NINGUNO;
     mensaje("");
     mensajeErr("Proveedor "+prv_codiE.getValorInt()+"  .... BORRADO");
     verDatos();
    }
    public void rgSelect() throws SQLException
    {
      super.rgSelect();
      if (dtCons.getNOREG())
        nRegSelE.setValorDec(0);
      else
      {
          s="SELECT count(*) as cuantos FROM v_proveedo "+
          (dtCons.getCondWhere().equals("")?"":" WHERE "+dtCons.getCondWhere());
          dtStat.select(s);
          nRegSelE.setValorDec(dtStat.getInt("cuantos"));
      }
      nRegE.setValorDec(1);
    }

    void verDatOtrCli()
    {
      try
      {
        s="SELECT * FROM v_proveedo WHERE prv_codi = "+prv_codiE.getValorInt()+
            " and emp_codi = "+emp_codiE.getValorInt();
        if (dtStat.select(s))
        {
          if (mensajes.mensajePreguntar("Proveedor YA existe .. Ver sus datos",this)==mensajes.YES)
            verDatos(dtStat);
        }
      } catch (Exception k)
      {

      }
    }

    boolean checkSde()
    {
      try
      {
        if (sde_codiE.getValorInt()==0)
          return true;
        s="SELECT * FROM v_saladesp WHERE sde_codi = "+sde_codiE.getValorInt();
        if (!dtStat.select(s))
        {
          mensajeErr("Sala de Despiece "+sde_codiE.getValorInt()+"... NO ENCONTRADA");
          return false;
        }
      }catch (Exception k)
      {
        Error("Error al buscar Sala de Despiece",k);
        return false;
      }
      return true;
    }
   boolean checkMat()
   {
     try
     {
       if (mat_codiE.getValorInt()==0)
         return true;
       s="SELECT * FROM v_matadero WHERE mat_codi = "+mat_codiE.getValorInt();
       if (!dtStat.select(s))
       {
         mensajeErr("Matadero "+mat_codiE.getValorInt()+"... NO ENCONTRADA");
         return false;
       }
     }catch (Exception k)
     {
       Error("Error al buscar Matadero",k);
       return false;
     }
     return true;
   }
   /**
    * Devuelve el Nombre del Proveedor (El nombre corto)
    * @param codPrv Codido del Proveedor
    * @param dt DatosTabla sobre el q realizar la select
    * @return Nombre Proveedor. NULL si no lo encuentra.
    * @throws java.sql.SQLException
    */
   public static String getNombPrv(int codPrv, DatosTabla dt) throws SQLException
   {
     String s = "select prv_nomb from v_proveedo  where " +
         "  prv_codi = " + codPrv;

     if (!dt.select(s))
       return null;
     return dt.getString("prv_nomb");
   }
   
}
