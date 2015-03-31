package gnu.chu.anjelica.riesgos;

import gnu.chu.Menu.*;
import gnu.chu.camposdb.*;
import gnu.chu.controles.*;
import gnu.chu.interfaces.*;
import gnu.chu.sql.*;
import gnu.chu.utilidades.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.Date;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
/**
* <p>Titulo:   pdcobros </p>
 * <p>Descripción: Mantenimientos Cobros DE FACTURAS Y ALBARANES DE VENTAS
 * <p>Copyright: Copyright (c) 2005-2015
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
* <p>Empresa: MISL</p>
* @author chuchiP
* @version 1.0
*/
public class pdcobros extends ventanaPad implements PAD
{
  int nCobBorrar;
    String condWhere="";
  IFCondCobros wCobMul=null;
  boolean swAdd=false;
  DatosTabla dtAlb;
  String s;
  boolean swVerFra=false;
  boolean swVerCob=false;
  CPanel Pprinc = new CPanel();
  CPanel Pcabe = new CPanel();
  CPanel PcondMult = new CPanel();
  CLabel cLabel1 = new CLabel();
  cliPanel cli_codiE = new cliPanel()
  {
    public void afterFocusLost(boolean noError)
    {

      try {
        if (noError)
          cli_poblE.setText(this.getLikeCliente().getString("cli_pobl"));
        verDatos(cli_codiE.getValorInt(), false);
        if (! jtFra.isVacio())
            Baceptar.setEnabled(true);
      } catch (Exception k)
      {
        Error("Error al ver datos Facturas de Clientes",k);
      }
    }
  };
  CComboBox clifacE = new CComboBox();
  CTextField avc_anoE = new CTextField(Types.DECIMAL,"###9");
  CPanel Palbar = new CPanel();
  CTextField avc_serieE = new CTextField(Types.CHAR,"X");
  CTextField avc_numeE = new CTextField(Types.DECIMAL,"#####9");
  CTextField fvc_serieE = new CTextField(Types.CHAR,"X");
  CTextField fvc_numeE = new CTextField(Types.DECIMAL,"#####9");
  CPanel Pfactu = new CPanel();
  CTextField fvc_anoE = new CTextField(Types.DECIMAL,"###9");
  CLabel avc_fecalbL = new CLabel();
  CTextField avc_fecalbE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel cli_poblE = new CLabel();
  Cgrid jtFra = new Cgrid(13);
  CGridEditable jtCobr = new CGridEditable(6)
  {
    public int cambiaLinea(int row, int col)
    {
      return checkLinea(row);
    }

        @Override
    public void afterCambiaLinea()
    {
      try
      {
        actDatCobr(jtCobr.getSelectedRow());
      }
      catch (Exception k)
      {
        Error("Error al actualizar datos cobros", k);
      }
    }
        @Override
   public void afterCambiaLineaDis(int nRow)
   {
     if (!swVerCob)
       return;
     try
     {
       actDatCobr(nRow);
     }
     catch (Exception k)
     {
       Error("Error al actualizar datos cobros", k);
     }
   }

  };
  CCheckBox opVerCobrado = new CCheckBox("S","N");
  CButton Birgrid = new CButton(Iconos.getImageIcon("reload"));
//  CButton Baceptar = new CButton("Aceptar",Iconos.getImageIcon("check"));
//  CButton Bcancelar = new CButton("Cancelar",Iconos.getImageIcon("cancel"));
  CLabel cLabel3 = new CLabel();
  CPanel Ppie = new CPanel();
  CTextField cob_feccobE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel cLabel5 = new CLabel();
  CLinkBox tpc_codiE = new CLinkBox();
  CLabel cLabel6 = new CLabel();
  CTextField cob_fecvtoE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel cob_obserL = new CLabel();
  CLabel cob_imporL = new CLabel();
  CTextField cob_imporE = new CTextField(Types.DECIMAL,"----,--9.99");
  CTextField cob_obserE = new CTextField(Types.CHAR,"X",30);
  CCheckBox avc_cobradE = new CCheckBox("-1","0");
  CLabel cLabel2 = new CLabel();
  CTextField avc_imppenE = new CTextField(Types.DECIMAL,"----,--9.99");
  CTextField avc_impalbE = new CTextField(Types.DECIMAL,"----,--9.99");
  CLabel cLabel8 = new CLabel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CLabel cLabel7 = new CLabel();
  CTextField emp_codiE = new CTextField(Types.DECIMAL,"#9");
  // Campos para el grid jtCobr
  CLinkBox tpc_codiG = new CLinkBox();
  CTextField cob_feccobG = new CTextField(Types.DATE, "dd-MM-yyyy");
  CTextField cob_fecvtoG = new CTextField(Types.DATE, "dd-MM-yyyy");
  CTextField cob_imporG = new CTextField(Types.DECIMAL, "----,--9.99");
  CTextField cob_obserG = new CTextField(Types.CHAR, "X", 30);
  CLabel cLabel4 = new CLabel();
  CTextField usu_nombE = new CTextField(Types.CHAR,"X",20);
  CTextField usu_nombG = new CTextField(Types.CHAR,"X",20);
  CTextField emp_codiG = new CTextField(Types.DECIMAL,"#9");
  CLabel cLabel9 = new CLabel();
  CTextField nAlPeE = new CTextField(Types.DECIMAL,"##9");
  CLabel iAlbPeL = new CLabel();
  CTextField iAlbPeE = new CTextField(Types.DECIMAL,"---,--9.99");
  CTextField nFraPeE = new CTextField(Types.DECIMAL,"##9");
  CTextField iFraPeE = new CTextField(Types.DECIMAL,"---,--9.99");

  CLabel cLabel10 = new CLabel();
  CTextField iTotPeE = new CTextField(Types.DECIMAL,"---,--9.99");
  CLabel cLabel11 = new CLabel();
  CTextField nTotPeE = new CTextField(Types.DECIMAL,"##9");
  CLabel iAlbPeL1 = new CLabel();
  CLabel iAlbPeL3 = new CLabel();
  CTextField iFraE = new CTextField(Types.DECIMAL,"---,--9.99");
  CTextField iAlbE = new CTextField(Types.DECIMAL,"---,--9.99");
  CTextField iTotE = new CTextField(Types.DECIMAL,"---,--9.99");
  CButton Bmulti = new CButton(Iconos.getImageIcon("fill"));
  CLabel cLabel12 = new CLabel();
  CLabel cLabel13 = new CLabel();
  CTextField serieFinE = new CTextField(Types.CHAR,"X",1);
  CTextField serieIniE = new CTextField(Types.CHAR,"X",1);
  CLinkBox empIniE = new CLinkBox();
  CTextField feciniE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel14 = new CLabel();
  CLabel cLabel15 = new CLabel();
  CTextField fecfinE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel16 = new CLabel();
  CLabel cLabel17 = new CLabel();
  CLinkBox empFinE = new CLinkBox();
  CLabel cLabel18 = new CLabel();
  cliPanel cliIniE = new cliPanel();
  CLabel cLabel19 = new CLabel();
  cliPanel cliFinE = new cliPanel();
  CTextField fvc_numeE1 = new CTextField(Types.DECIMAL,"#####9");
  CLabel cLabel20 = new CLabel();
  CTextField fvc_numeE2 = new CTextField(Types.DECIMAL,"#####9");
  CLabel cLabel21 = new CLabel();
  CButton Bconsulta = new CButton(Iconos.getImageIcon("pon"));
  CCheckBox opMulti = new CCheckBox();
  CButton Bvolver = new CButton(Iconos.getImageIcon("data-undo"));
  CLabel cLabel22 = new CLabel();
  CLinkBox fpaFinE = new CLinkBox();
  CLinkBox fpaIniE = new CLinkBox();
  CLabel cLabel23 = new CLabel();
  CLabel cLabel24 = new CLabel();
  CComboBox cli_giroE = new CComboBox();
  CButton Bborrar = new CButton(Iconos.getImageIcon("quita"));
  CLabel cLabel25 = new CLabel();
  CTextField feficoE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel cLabel26 = new CLabel();
  CTextField feincoE = new CTextField(Types.DATE,"dd-MM-yyyy");


  public pdcobros(EntornoUsuario eu, Principal p)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    setTitulo("Mantenimiento Cobros a Clientes");

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
      setErrorInit(true);
    }
  }

 public pdcobros(gnu.chu.anjelica.menu p, EntornoUsuario eu)
 {
   EU = eu;
   vl = p.getLayeredPane();
   setTitulo("Mantenimiento Cobros a Clientes");
   eje = false;

   try
   {
     jbInit();
   }
   catch (Exception e)
   {
     ErrorInit(e);
     setErrorInit(true);
   }
 }

 private void jbInit() throws Exception
 {
   iniciarFrame();
   this.setSize(new Dimension(602, 533));
   this.setVersion("2013-02-06");
   statusBar = new StatusBar(this);
   nav = new navegador(this, dtCons, false, navegador.NORMAL);
//   nav.removeBoton(navegador.DELETE);
   strSql = "SELECT distinct cli_codi FROM v_albavec WHERE fvc_ano = 0 " +
       " AND emp_codi = "+EU.em_cod+
       " AND  fvc_nume = 0" +
       (EU.isRootAV()?"":" AND div_codi > 0 ")+
       " AND avc_cobrad = 0 " + // NO cobrado.
       " and avc_impalb <> 0"+
       " UNION "+
       "SELECT distinct cli_codi FROM v_facvec WHERE "+
       " emp_codi = "+EU.em_cod+
       " and fvc_cobrad = 0 " + // No cobrado
       " and fvc_sumtot <> 0"+
       " ORDER BY 1";
//   System.out.println("s: "+strSql);
   conecta();
   iniciar(this);

    cli_poblE.setBackground(Color.orange);
    cli_poblE.setForeground(Color.black);
    cli_poblE.setOpaque(true);
    cli_poblE.setBounds(new Rectangle(368, 2, 213, 18));
    opVerCobrado.setText("Ver Cobrado");
    opVerCobrado.setEnabled(false);
    opVerCobrado.setBounds(new Rectangle(97, 31, 102, 16));
    cLabel3.setText("Fec. Cobro");
    cLabel3.setBounds(new Rectangle(2, 42, 64, 18));
    cLabel5.setText("Tipo Cobro");
    cLabel5.setBounds(new Rectangle(150, 42, 69, 18));
    tpc_codiE.setAncTexto(30);
    tpc_codiE.setBounds(new Rectangle(220, 42, 227, 19));
    cLabel6.setText("Fecha Vto");
    cLabel6.setBounds(new Rectangle(448, 42, 59, 18));
    cob_obserL.setText("Observaciones");
    cob_obserL.setBounds(new Rectangle(3, 82, 87, 18));
    cob_imporL.setText("Imp. Cobrado");
    cob_imporL.setBounds(new Rectangle(2, 63, 77, 18));
    avc_cobradE.setMargin(new Insets(0, 0, 0, 0));
    avc_cobradE.setText("Tot. Cobrado");
    avc_cobradE.setBounds(new Rectangle(171, 63, 102, 18));
    cLabel2.setText("Importe Pend.");
    cLabel2.setBounds(new Rectangle(276, 63, 81, 18));
    cLabel8.setText("Imp. Docum.");
    cLabel8.setBounds(new Rectangle(418, 22, 76, 18));
    Birgrid.setBounds(new Rectangle(261, 2, 52, 20));
    Birgrid.setToolTipText("Moverse entre Paneles");

    cLabel1.setText("Cliente");
    cLabel1.setBounds(new Rectangle(6, 2, 46, 18));
    avc_fecalbL.setText("Fec.Docum.");
    avc_fecalbL.setBounds(new Rectangle(267, 23, 71, 18));

    ArrayList v=new ArrayList();
    cLabel4.setText("Usuario");
    cLabel4.setBounds(new Rectangle(289, 85, 51, 16));
    Pcabe.setMaximumSize(new Dimension(586, 104));
    Pcabe.setMinimumSize(new Dimension(586, 104));
    Pcabe.setPreferredSize(new Dimension(586, 104));
    PcondMult.setMaximumSize(new Dimension(586, 104));
    PcondMult.setMinimumSize(new Dimension(586, 104));
    PcondMult.setPreferredSize(new Dimension(586, 104));

    jtFra.setMaximumSize(new Dimension(583, 171));
    jtFra.setMinimumSize(new Dimension(583, 171));
    jtFra.setPreferredSize(new Dimension(583, 171));
    jtCobr.setMaximumSize(new Dimension(567, 126));
    jtCobr.setMinimumSize(new Dimension(567, 126));
    jtCobr.setPreferredSize(new Dimension(567, 126));
    Ppie.setMaximumSize(new Dimension(502, 70));
    Ppie.setMinimumSize(new Dimension(502, 70));
    Ppie.setPreferredSize(new Dimension(502, 70));
  
    cLabel7.setText("Emp");
    cLabel7.setBounds(new Rectangle(2, 21, 33, 18));
    usu_nombE.setBounds(new Rectangle(343, 84, 94, 17));
    cob_obserE.setBounds(new Rectangle(89, 82, 193, 18));
    avc_imppenE.setBounds(new Rectangle(358, 63, 85, 18));
    cob_imporE.setBounds(new Rectangle(77, 62, 85, 18));
    cob_fecvtoE.setBounds(new Rectangle(504, 42, 75, 18));
    cob_feccobE.setBounds(new Rectangle(67, 42, 75, 18));
    avc_fecalbE.setBounds(new Rectangle(337, 23, 75, 18));
    avc_impalbE.setBounds(new Rectangle(495, 22, 85, 18));
    Palbar.setBounds(new Rectangle(158, 21, 107, 19));
    Pfactu.setBounds(new Rectangle(158, 21, 107, 19));
    clifacE.setBounds(new Rectangle(70, 21, 87, 18));
    emp_codiE.setBounds(new Rectangle(34, 21, 32, 18));
    cli_codiE.setBounds(new Rectangle(54, 2, 311, 18));
    avc_numeE.setBounds(new Rectangle(59, 1, 44, 18));
    avc_serieE.setBounds(new Rectangle(39, 1, 18, 18));
    avc_serieE.setMayusc(true);
    fvc_serieE.setBounds(new Rectangle(39, 1, 18, 18));
    fvc_serieE.setMayusc(true);
    avc_anoE.setBounds(new Rectangle(2, 1, 36, 18));
    fvc_numeE.setBounds(new Rectangle(59, 1, 44, 18));
    fvc_anoE.setBounds(new Rectangle(2, 1, 36, 18));
    cLabel9.setToolTipText("");
    cLabel9.setText("Albaranes");
    cLabel9.setBounds(new Rectangle(265, 18, 58, 16));
    nAlPeE.setEnabled(false);
    nAlPeE.setBounds(new Rectangle(328, 18, 34, 16));
    iAlbPeL.setBackground(Color.blue);
    iAlbPeL.setForeground(Color.white);
    iAlbPeL.setAlignmentY((float) 0.5);
    iAlbPeL.setOpaque(true);
    iAlbPeL.setToolTipText("");
    iAlbPeL.setHorizontalAlignment(SwingConstants.CENTER);
    iAlbPeL.setText("N.Doc");
    iAlbPeL.setBounds(new Rectangle(322, 2, 40, 13));
    iAlbPeE.setEnabled(false);
    iAlbPeE.setBounds(new Rectangle(366, 18, 76, 16));
    iFraPeE.setEnabled(false);
    iFraPeE.setBounds(new Rectangle(366, 34, 76, 16));
    nFraPeE.setEnabled(false);
    nFraPeE.setBounds(new Rectangle(328, 34, 34, 16));
    cLabel10.setText("Facturas");
    cLabel10.setBounds(new Rectangle(265, 34, 58, 16));
    iTotPeE.setEnabled(false);
    iTotPeE.setBounds(new Rectangle(366, 50, 76, 16));
    cLabel11.setText("Total");
    cLabel11.setBounds(new Rectangle(266, 50, 58, 16));
    nTotPeE.setEnabled(false);
    nTotPeE.setBounds(new Rectangle(328, 50, 34, 16));
    iAlbPeL1.setText("Imp.Pend.");
    iAlbPeL1.setBounds(new Rectangle(366, 2, 76, 15));
    iAlbPeL1.setHorizontalAlignment(SwingConstants.CENTER);
    iAlbPeL1.setToolTipText("");
    iAlbPeL1.setOpaque(true);
    iAlbPeL1.setAlignmentY((float) 0.5);
    iAlbPeL1.setForeground(Color.white);
    iAlbPeL1.setBackground(Color.blue);
    iAlbPeL3.setText("Imp.Docum.");
    iAlbPeL3.setBounds(new Rectangle(446, 2, 76, 15));
    iAlbPeL3.setHorizontalAlignment(SwingConstants.CENTER);
    iAlbPeL3.setToolTipText("");
    iAlbPeL3.setOpaque(true);
    iAlbPeL3.setAlignmentY((float) 0.5);
    iAlbPeL3.setForeground(Color.white);
    iAlbPeL3.setBackground(Color.blue);
    iFraE.setEnabled(false);
    iFraE.setBounds(new Rectangle(446, 34, 76, 16));
    iAlbE.setEnabled(false);
    iAlbE.setBounds(new Rectangle(446, 18, 76, 16));
    iTotE.setEnabled(false);
    iTotE.setBounds(new Rectangle(446, 50, 76, 16));
    Bmulti.setBounds(new Rectangle(452, 82, 126, 18));
    Bmulti.setMargin(new Insets(0, 0, 0, 0));
    Bmulti.setText("F9 Multiple");
    Bmulti.setToolTipText("Carga de Cobros Multiple");
    cLabel12.setBounds(new Rectangle(143, 3, 47, 17));
    cLabel12.setText("A Fecha");
    cLabel12.setOpaque(true);
    cLabel13.setBounds(new Rectangle(292, 21, 70, 19));
    cLabel13.setText("A Empresa");
    serieFinE.setMayusc(true);
    serieFinE.setBounds(new Rectangle(423, 3, 19, 16));
    serieIniE.setMayusc(true);
    serieIniE.setBounds(new Rectangle(337, 3, 19, 16));
    empIniE.setBounds(new Rectangle(88, 21, 201, 19));
    empIniE.setAncTexto(30);
    feciniE.setBounds(new Rectangle(57, 3, 81, 17));
    cLabel14.setBounds(new Rectangle(285, 3, 47, 16));
    cLabel14.setText("De Serie");
    cLabel15.setBounds(new Rectangle(5, 3, 53, 17));
    cLabel15.setText("De Fecha");
    fecfinE.setBounds(new Rectangle(193, 3, 81, 17));
    cLabel16.setBounds(new Rectangle(373, 3, 41, 16));
    cLabel16.setText("A Serie");
    cLabel17.setBounds(new Rectangle(4, 21, 70, 19));
    cLabel17.setText("De Empresa");
    empFinE.setBounds(new Rectangle(362, 21, 218, 19));
    empFinE.setAncTexto(30);
    cLabel18.setBounds(new Rectangle(10, 43, 59, 19));
    cLabel18.setText("De Cliente");
    cliIniE.setBounds(new Rectangle(69, 43, 226, 19));
    cliIniE.setCeroIsNull(true);
    cLabel19.setBounds(new Rectangle(298, 42, 56, 19));
    cLabel19.setText("A Cliente");
    cliFinE.setBounds(new Rectangle(351, 43, 225, 19));
    cliFinE.setCeroIsNull(true);
    fvc_numeE1.setBounds(new Rectangle(195, 85, 55, 16));
    cLabel20.setBounds(new Rectangle(130, 85, 60, 16));
    cLabel20.setText("A Factura");
    fvc_numeE2.setBounds(new Rectangle(69, 85, 55, 16));
    cLabel21.setBounds(new Rectangle(2, 85, 62, 16));
    cLabel21.setText("De Factura");
    Bconsulta.setBounds(new Rectangle(253, 81, 137, 21));
    Bconsulta .setText("F4 Añadir");
    opMulti.setText("Cobro Mutliple");
    opMulti.setBounds(new Rectangle(458, 63, 121, 19));
    Bvolver.setBounds(new Rectangle(392, 81, 137, 21));
    Bvolver.setToolTipText("Volver a tipo de Cobro");
    Bvolver.setText("F9 Volver");
    cLabel22.setText("A For Pago");
    cLabel22.setBounds(new Rectangle(298, 63, 65, 19));
    fpaFinE.setCeroIsNull(true);
    fpaFinE.setAncTexto(30);
    fpaFinE.setBounds(new Rectangle(364, 63, 216, 19));
    fpaIniE.setBounds(new Rectangle(74, 63, 221, 19));
    fpaIniE.setAncTexto(30);
    fpaIniE.setCeroIsNull(true);
    cLabel23.setText("De For.Pago");
    cLabel23.setBounds(new Rectangle(2, 63, 70, 19));
    cLabel24.setText("Giros");
    cLabel24.setBounds(new Rectangle(468, 3, 34, 16));

    cli_giroE.setBounds(new Rectangle(508, 1, 70, 17));
    Bborrar.setBounds(new Rectangle(533, 81, 47, 21));
    Bborrar.setToolTipText("Borrar Cobros");
    Bborrar.setText("");
    cLabel25.setText("De Fecha");
    cLabel25.setBounds(new Rectangle(4, 50, 54, 16));
    cLabel26.setText("A");
    cLabel26.setBounds(new Rectangle(159, 50, 19, 16));
    feficoE.setText("dd-mm-yyyy");
    feficoE.setBounds(new Rectangle(181, 51, 79, 16));
    feincoE.setBounds(new Rectangle(62, 50, 79, 16));
    Bcancelar.setBounds(new Rectangle(136, 4, 120, 23));
    Baceptar.setBounds(new Rectangle(13, 4, 120, 23));
    v.add("F/A"); // 0
    v.add("Emp"); // 1
    v.add("Ejer"); // 2
    v.add("Ser."); // 3
    v.add("Numero"); // 4
    v.add("Fec.Doc."); // 5
    v.add("Imp.Doc"); // 6
    v.add("Imp.Cobr."); // 7
    v.add("Imp.Pend."); // 8
    v.add("Pend.");  // 9 Pendiente
    v.add("Cliente"); // 10
    v.add("Nombre Cliente"); // 11
    v.add("Fec.Vto"); // 12
    jtFra.setCabecera(v);
    jtFra.setAnchoColumna(new int[]{25,30,40,30,60,90,90,90,90,30,60,100,90});
    jtFra.setAlinearColumna(new int[]{1,2,2,1,2,1,2,2,2,1,2,0,1});
    jtFra.setFormatoColumna(6,"----,--9.99");
    jtFra.setFormatoColumna(7,"----,--9.99");
    jtFra.setFormatoColumna(8,"----,--9.99");
    jtFra.setFormatoColumna(9,"B0-");
    jtFra.resetRenderer(9);
    jtFra.setAjustarGrid(true);
    jtFra.setAjustarColumnas(false);

    usu_nombG.setEnabled(false);
    tpc_codiG.setAceptaNulo(false);
    tpc_codiE.setAceptaNulo(false);
    Vector vc = new Vector();
    vc.add(tpc_codiG); // 0
    vc.add(cob_feccobG); // 1
    vc.add(cob_fecvtoG); // 2
    vc.add(cob_imporG); // 3
    vc.add(cob_obserG); // 4
    vc.add(usu_nombG); // 5

    ArrayList vv=new ArrayList();
    vv.add("Tip.Cob."); // 0 Tipo Cobro
    vv.add("Fec.Cobro"); // 1 Fecha de Cobro
    vv.add("Fec.Vto"); // 2 Fecha Vto.
    vv.add("Importe");  // 3
    vv.add("Observaciones"); // 4
    vv.add("Usuario"); // 5
    jtCobr.setCabecera(vv);


    jtCobr.setAnchoColumna(new int[]{150,90,90,80,100,80});
    jtCobr.setAlinearColumna(new int[]{0,1,1,2,0,0});
    jtCobr.setFormatoColumna(3,"----,--9.99");
    jtCobr.setFormatoColumna(1,"dd-MM-yyyy");
    jtCobr.setFormatoColumna(2,"dd-MM-yyyy");
    jtCobr.setAjustarGrid(true);
    jtCobr.setAjustarColumnas(false);
    jtCobr.setCampos(vc);


    avc_impalbE.setEnabled(false);
    avc_imppenE.setEnabled(false);

    Pprinc.setLayout(gridBagLayout1);
    Pcabe.setBorder(BorderFactory.createRaisedBevelBorder());
    PcondMult.setBorder(BorderFactory.createRaisedBevelBorder());
    Pcabe.setLayout(null);
    PcondMult.setLayout(null);
    Palbar.setBorder(BorderFactory.createLineBorder(Color.black));
    Pfactu.setBorder(BorderFactory.createLineBorder(Color.black));
    Palbar.setLayout(null);
    Pfactu.setLayout(null);
    Ppie.setBorder(BorderFactory.createLoweredBevelBorder());
    Ppie.setLayout(null);

    this.getContentPane().add(Pprinc, BorderLayout.CENTER);

    Ppie.add(nTotPeE, null);
    Ppie.add(nFraPeE, null);
    Ppie.add(nAlPeE, null);
    Ppie.add(iAlbPeL, null);
    Ppie.add(iAlbPeE, null);
    Ppie.add(iAlbPeL1, null);
    Ppie.add(iFraPeE, null);
    Ppie.add(iTotPeE, null);
    Ppie.add(cLabel10, null);
    Ppie.add(cLabel9, null);
    Ppie.add(cLabel11, null);
    Ppie.add(iTotE, null);
    Ppie.add(iAlbPeL3, null);
    Ppie.add(iAlbE, null);
    Ppie.add(iFraE, null);
    Ppie.add(Baceptar, null);
    Ppie.add(Bcancelar, null);
    Ppie.add(Birgrid, null);
    Ppie.add(cLabel25, null);
    Ppie.add(feincoE, null);
    Ppie.add(feficoE, null);
    Ppie.add(cLabel26, null);
    Ppie.add(opVerCobrado, null);
    PcondMult.add(fecfinE, null);
    PcondMult.add(cLabel15, null);
    PcondMult.add(feciniE, null);
    PcondMult.add(cLabel12, null);
    PcondMult.add(empIniE, null);
    PcondMult.add(cLabel17, null);
    PcondMult.add(cLabel13, null);
    PcondMult.add(empFinE, null);
    PcondMult.add(cLabel18, null);
    PcondMult.add(cliIniE, null);
    PcondMult.add(cLabel19, null);
    PcondMult.add(cliFinE, null);
    PcondMult.add(fpaFinE, null);
    PcondMult.add(cLabel21, null);
    PcondMult.add(fvc_numeE2, null);
    PcondMult.add(fpaIniE, null);
    PcondMult.add(cLabel22, null);
    PcondMult.add(cLabel23, null);
    PcondMult.add(cLabel14, null);
    PcondMult.add(serieIniE, null);
    PcondMult.add(serieFinE, null);
    PcondMult.add(cLabel16, null);
    PcondMult.add(cLabel24, null);
    PcondMult.add(cli_giroE, null);
    PcondMult.add(fvc_numeE1, null);
    PcondMult.add(cLabel20, null);
    PcondMult.add(Bconsulta, null);
    PcondMult.add(Bvolver, null);
    PcondMult.add(Bborrar, null);


    Pcabe.add(avc_impalbE, null);
    Pcabe.add(cli_poblE, null);
    Pcabe.add(cob_fecvtoE, null);
    Pcabe.add(cLabel3, null);
    Pcabe.add(cob_feccobE, null);
    Pcabe.add(cLabel5, null);
    Pcabe.add(tpc_codiE, null);
    Pcabe.add(cLabel6, null);
    Pcabe.add(cob_imporL, null);
    Pcabe.add(cli_codiE, null);
    Pcabe.add(cLabel1, null);
    Pcabe.add(Bmulti, null);
    Pcabe.add(cLabel7, null);
    Pcabe.add(emp_codiE, null);
    Pcabe.add(clifacE, null);
    Pcabe.add(cLabel8, null);
    Pcabe.add(avc_fecalbE, null);
    Pcabe.add(avc_fecalbL, null);
    Pcabe.add(cLabel4, null);
    Pcabe.add(usu_nombE, null);
    Pcabe.add(cob_obserE, null);
    Pcabe.add(cob_obserL, null);

    Pfactu.add(fvc_numeE, null);
    Pfactu.add(fvc_serieE,null);
    Pfactu.add(fvc_anoE, null);
    Pcabe.add(cob_imporE, null);
    Pcabe.add(avc_cobradE, null);
    Pcabe.add(avc_imppenE, null);
    Pcabe.add(cLabel2, null);
    Pcabe.add(opMulti, null);
    Palbar.add(avc_numeE, null);
    Palbar.add(avc_anoE, null);
    Palbar.add(avc_serieE, null);
    Pcabe.add(Palbar, null);
    Pcabe.add(Pfactu, null);

    Pprinc.add(Pcabe,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    Pprinc.add(PcondMult,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    Pprinc.add(jtFra,   new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 0, 0, 0), 0, 0));
    Pprinc.add(jtCobr,   new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 0, 0, 0), 0, 0));
    Pprinc.add(Ppie,    new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
  PcondMult.setVisible(false);
  empIniE.setCeroIsNull(true);
  empFinE.setCeroIsNull(true);
 }

 public void afterConecta() throws SQLException, java.text.ParseException
 {
   dtAlb=new DatosTabla(ct);
   cli_codiE.iniciar(dtStat,this,vl,EU);
   cliIniE.iniciar(dtStat,this,vl,EU);
   cliFinE.iniciar(dtStat,this,vl,EU);
   cliIniE.setCeroIsNull(true);
   cliFinE.setCeroIsNull(true);
   s="SELECT tpc_codi,tpc_nomb FROM v_cobtipo ORDER BY tpc_codi";
   dtStat.select(s);
   tpc_codiE.addDatos(dtStat);
   dtStat.first();
   tpc_codiG.addDatos(dtStat);
   clifacE.addItem("Factura","F");
   clifacE.addItem("Albaran","A");
   Baceptar.setText("F4 Aceptar");

   empIniE.setFormato(true);
   empIniE.setFormato(Types.DECIMAL, "#9", 2);
   empFinE.setFormato(true);
   empFinE.setFormato(Types.DECIMAL, "#9", 2);

   s = "SELECT emp_codi,emp_nomb FROM v_empresa ORDER BY emp_codi";
   dtCon1.select(s);
   empIniE.addDatos(dtCon1);
   dtCon1.first();
   empIniE.setText(dtCon1.getString("emp_codi"));
   empFinE.addDatos(dtCon1);
   dtCon1.last();
   empFinE.setText(dtCon1.getString("emp_codi"));

   fpaFinE.setFormato(true);
   fpaFinE.setFormato(Types.DECIMAL, "#9", 2);
   s = "SELECT fpa_codi,fpa_nomb FROM v_forpago ORDER BY fpa_codi";
   dtCon1.select(s);
   fpaFinE.addDatos(dtCon1);

   fpaIniE.setFormato(true);
   fpaIniE.setFormato(Types.DECIMAL, "#9", 2);
   s = "SELECT fpa_codi,fpa_nomb FROM v_forpago ORDER BY fpa_codi";
   dtCon1.select(s);
   fpaIniE.addDatos(dtCon1);

   cli_giroE.addItem("**","X");
   cli_giroE.addItem("No","N");
   cli_giroE.addItem("Si","S");
 }

 public void iniciarVentana() throws Exception
 {
   feincoE.setText(Formatear.sumaDias(Formatear.getDateAct(),-180));
   feficoE.setText(Formatear.getFechaAct("dd-MM-yyyy"));

   PcondMult.setButton(KeyEvent.VK_F4,Bconsulta);

   jtFra.setButton(KeyEvent.VK_F2,Birgrid);
   jtCobr.setButton(KeyEvent.VK_F2,Birgrid);
   Pcabe.setButton(KeyEvent.VK_F2,Birgrid);
   Pcabe.setButton(KeyEvent.VK_F9,Bmulti);

   PcondMult.setButton(KeyEvent.VK_F9,Bvolver);
   jtFra.setButton(KeyEvent.VK_F4,Baceptar);
   jtCobr.setButton(KeyEvent.VK_F4,Baceptar);
   Pcabe.setButton(KeyEvent.VK_F4,Baceptar);

   emp_codiE.setColumnaAlias("emp_codi");
   cli_codiE.setColumnaAlias("cli_codi");
   fvc_anoE.setColumnaAlias("cob_anofac");
   fvc_numeE.setColumnaAlias("fac_nume");
   avc_anoE.setColumnaAlias("cob_ano");
   avc_serieE.setColumnaAlias("cob_serie");
   avc_numeE.setColumnaAlias("alb_nume");
   fvc_serieE.setColumnaAlias("fvc_serie");
   usu_nombE.setColumnaAlias("usu_nomb");
   cob_feccobE.setColumnaAlias("cob_feccob");
   tpc_codiE.setColumnaAlias("tpc_codi");
   cob_fecvtoE.setColumnaAlias("cob_fecvto");
   cob_imporE.setColumnaAlias("cob_impor");
   cob_obserE.setColumnaAlias("cob_obser");

   activar(false);
   verDatos(dtCons);
   activarEventos();
 }

 void activarEventos()
 {
   Bborrar.addActionListener(new ActionListener()
   {
            @Override
     public void actionPerformed(ActionEvent e)
     {
       jtFra.removeAllDatos();
       iAlbPeE.resetTexto();
       iAlbE.resetTexto();
       nAlPeE.resetTexto();

       iFraPeE.resetTexto();
       iFraE.resetTexto();
       nFraPeE.resetTexto();

       iTotPeE.resetTexto();
       iTotE.resetTexto();
       nTotPeE.resetTexto();

       feciniE.requestFocus();
     }
   });
   opMulti.addActionListener(new ActionListener()
   {
            @Override
     public void actionPerformed(ActionEvent e)
     {
       opMulti_actionPerformed();
     }
   });

   Bmulti.addActionListener(new ActionListener()
   {
            @Override
     public void actionPerformed(ActionEvent e)
     {
       Bmulti_actionPerformed();
     }
   });
   Bconsulta.addActionListener(new ActionListener()
   {
            @Override
     public void actionPerformed(ActionEvent e)
     {
        Bconsulta_actionPerformed();
     }
   });

   Bvolver.addActionListener(new ActionListener()
   {
            @Override
     public void actionPerformed(ActionEvent e)
     {
        Bvolver_actionPerformed();
     }
   });

   Birgrid.addActionListener(new ActionListener()
   {
            @Override
     public void actionPerformed(ActionEvent e)
     {
       irGrid();
     }
   });

   jtCobr.addMouseListener(new MouseAdapter()
   {
            @Override
     public void mouseClicked(MouseEvent e)
     {
       if (! jtCobr.isEnabled() && nav.pulsado==navegador.EDIT)
         irGrid();
     }
   });

   jtFra.addMouseListener(new MouseAdapter()
   {
            @Override
     public void mouseClicked(MouseEvent e)
     {
       if (! jtFra.isEnabled() && nav.pulsado==navegador.EDIT)
         irGrid();
       if (! jtFra.isEnabled() || jtFra.isVacio() || jtFra.getSelectedColumn()!=9)
       {
         if (opMulti.isSelected() && !jtFra.isVacio()  && jtFra.getSelectedColumnDisab()==9)
         {
           jtFra.setValor((!jtFra.getValBoolean(jtFra.getSelectedRowDisab(), 9)),jtFra.getSelectedRowDisab(), 9);
           actSumGrid();
         }
         return;
       }
       try
       {
         swVerFra=false;
         if (! ponEstFra(! jtFra.getValBoolean(9)))
         {
           mensajes.mensajeAviso("Documento NO ENCONTRADO");
           ctUp.rollback();
           return;
         }
         jtFra.setValor((!jtFra.getValBoolean(9)),9);
/*         if (! jtCobr.isVacio())
         {
           int nRow = jtCobr.getRowCount();
           for (int n = 0; n < nRow; n++)
             jtCobr.setValor(new Boolean(!jtCobr.getValBoolean(n, 5)), n, 5);
         }
 */
         ctUp.commit();
         swVerFra=true;
       }
       catch (Exception k)
       {
         Error("Error al cambair estado de la Factura",k);
       }

     }

   });
   jtFra.tableView.getSelectionModel().addListSelectionListener(new
       ListSelectionListener()
   {
            @Override
     public void valueChanged(ListSelectionEvent e)
     {
       if (e.getValueIsAdjusting()) // && e.getFirstIndex() == e.getLastIndex())
         return;
       if (!swVerFra)
         return;
       try
       {
         actDatFra(jtFra.getSelectedRowDisab());
       }
       catch (Exception k)
       {
         Error("Error al actualizar datos Factura", k);
       }

     }
   });
   cob_imporE.addFocusListener(new FocusAdapter()
   {
            @Override
     public void focusLost(FocusEvent e)
     {
       if (cob_imporE.hasCambio())
       {
         cob_imporE.resetCambio();
         avc_cobradE.setSelected(cob_imporE.getValorDec() >=
                                 avc_imppenE.getValorDec());
       }
     }
   });
   clifacE.addActionListener(new ActionListener()
   {
            @Override
     public void actionPerformed(ActionEvent e)
     {
       Palbar.setVisible(clifacE.getValor().equals("A"));
       Pfactu.setVisible(!clifacE.getValor().equals("A"));
     }
   });
   fvc_numeE.addFocusListener(new FocusAdapter()
   {
            @Override
     public void focusLost(FocusEvent e)
     {
       actDatFra();
     }
   });
   avc_numeE.addFocusListener(new FocusAdapter()
   {
            @Override
     public void focusLost(FocusEvent e)
     {
       actDatAlb();
     }
   });
   /*
   jtCobr.tableView.getSelectionModel().addListSelectionListener(new
       ListSelectionListener()
   {
     public void valueChanged(ListSelectionEvent e)
     {
       if (e.getValueIsAdjusting()) // && e.getFirstIndex() == e.getLastIndex())
         return;
       if (!swVerCob)
         return;
       try
       {
         actDatCobr(jtCobr.getSelectedRowDisab());
       }
       catch (Exception k)
       {
         Error("Error al actualizar datos cobros", k);
       }

     }
   });
*/
   opVerCobrado.addActionListener(new ActionListener()
   {
            @Override
     public void actionPerformed(ActionEvent e)
     {
       verDatos(dtCons);
     }
   });
 }
 void actDatCobr(int row) throws SQLException, ParseException
 {
   if (nav.pulsado==navegador.ADDNEW)
   {
     if (! opMulti.isSelected())
     {
       cob_imporE.setValorDec(jtFra.getValorDec(jtFra.getSelectedRowDisab(), 8));
       avc_cobradE.setSelected(true);
       cob_imporE.requestFocus();
     }
     return;
   }

   if (jtCobr.isVacio())
     return;
  cob_feccobE.setText(jtCobr.getValString(row,1));
  tpc_codiE.setText(tpc_codiE.getTexto(jtCobr.getValString(row,0)));
  cob_fecvtoE.setText(jtCobr.getValString(row,2));
  cob_imporE.setValorDec(jtCobr.getValorDec(row,3));
  cob_obserE.setText(jtCobr.getValString(row,4));
  usu_nombE.setText(jtCobr.getValString(row,7));
 }

 /**
  * Actualiza los datos de cabecera de la factura activa en el grid
  * @param row int No Row del grid
  * @throws SQLException Error db
  * @throws ParseException Error DB
  */
 void actDatFra(int row) throws SQLException, ParseException
 {
   if (jtFra.isVacio())
     return;
  clifacE.setValor(jtFra.getValString(row,0));
  if (clifacE.getValor().equals("A"))
  {
    Pfactu.setVisible(false);
    Palbar.setVisible(true);
    avc_anoE.setValorDec(jtFra.getValInt(row,2));
    avc_serieE.setText(jtFra.getValString(row,3));
    avc_numeE.setValorDec(jtFra.getValInt(row,4));
  }
  else
  {
    Pfactu.setVisible(true);
    Palbar.setVisible(false);
    fvc_anoE.setValorDec(jtFra.getValInt(row,2));
    fvc_serieE.setText(jtFra.getValString(row,3));
    fvc_numeE.setValorDec(jtFra.getValInt(row,4));
  }
  emp_codiE.setText(jtFra.getValString(row,1));
  avc_fecalbE.setText(jtFra.getValString(row,5));
  avc_impalbE.setValorDec(jtFra.getValorDec(row,6));
  avc_cobradE.setSelected(jtFra.getValBoolean(row,9));
  avc_imppenE.setValorDec(jtFra.getValorDec(row,8));

  verDatCob(row);

 }

 void verDatCob(int row) throws SQLException, ParseException
 {
   swVerCob=false;
   jtCobr.removeAllDatos();
   if (nav.pulsado!=navegador.ADDNEW)
   {
     cob_feccobE.resetTexto();
     tpc_codiE.resetTexto();
     cob_fecvtoE.resetTexto();
     cob_imporE.resetTexto();
     cob_obserE.resetTexto();
   }

   s ="select  tc.tpc_codi,cob_anofac, fac_nume,c.cob_feccob,c.cob_impor,"+
       " tc.tpc_nomb,cob_fecvto,cob_obser,c.usu_nomb  " +
       " from v_cobros c,v_cobtipo tc " +
       " WHERE cob_anofac = " + jtFra.getValorInt(row, 2) +
       " and c.emp_codi = " + jtFra.getValorInt(row,1)+
       " and c.fac_nume = " + jtFra.getValorInt(row, 4) +
       " and c.alb_nume = 0 " +
       " and tc.tpc_codi = c.tpc_codi ";
   if (clifacE.getValor().equals("A"))
   {
     s += " UNION ALL " +
         "select tc.tpc_codi,cob_ano, alb_nume,c.cob_feccob,c.cob_impor,"+
         " tc.tpc_nomb,cob_fecvto,cob_obser,c.usu_nomb  " +
         " from v_cobros c,v_cobtipo tc " +
         " WHERE ( "+
   //cob_anofac = 0 OR "+
         " cob_anofac = " + jtFra.getValorInt(row, 2) +")"+
         " and c.emp_codi = " + jtFra.getValorInt(row,1)+
         " and c.alb_nume = " + jtFra.getValorInt(row, 4) +
         " and c.cob_serie = '" + jtFra.getValString(row, 3) + "'" +
         " and tc.tpc_codi = c.tpc_codi " +
         " order by 1 desc,2 desc";
   }
   if(dtCon1.select(s))
   {
     do
     {
       ArrayList v = new ArrayList();
       v.add(dtCon1.getInt("tpc_codi")+" - "+ dtCon1.getString("tpc_nomb"));
       v.add(dtCon1.getFecha("cob_feccob", "dd-MM-yyyy"));
       v.add(dtCon1.getFecha("cob_fecvto", "dd-MM-yyyy"));
       v.add(dtCon1.getString("cob_impor"));
       v.add(dtCon1.getString("cob_obser"));
       v.add(dtCon1.getString("usu_nomb"));
       jtCobr.addLinea(v);
     }  while (dtCon1.next());

     jtCobr.requestFocusInicio();
   }
   actDatCobr(0);
   swVerCob=true;
}

 public void PADPrimero() {verDatos(dtCons);}

 public void PADAnterior() {verDatos(dtCons);}

 public void PADSiguiente() {verDatos(dtCons);}

 public void PADUltimo() {verDatos(dtCons);}

 public void PADQuery()
 {
   swVerCob=false;
   swVerFra=false;
   mensaje("Establezca criterios de Filtro");
   activar(true,navegador.QUERY);
   Pcabe.setQuery(true);
   clifacE.setQuery(false);
   Pcabe.resetTexto();
 }

    @Override
 public void ej_query1()
 {
   boolean swAlb = false;
   boolean swFra = false;
   String condWhere1="";
   if (clifacE.getValor().equals("F"))
   {
     if (! (fvc_anoE.getStrQuery() + fvc_numeE.getStrQuery()).equals(""))
       swFra = true;
   }
   s = "";
   String s1 = "";
   ArrayList v = new ArrayList();
   if (!swFra)
   {
     s = "SELECT distinct cli_codi FROM v_albavec as a WHERE fvc_ano = 0 " +
         " AND  fvc_nume = 0" +
         (EU.isRootAV() ? "" : " AND a.div_codi > 0 ") +
         (opVerCobrado.isSelected() ? "" : " AND avc_cobrad = 0 ") +
         " and avc_impalb <> 0";

     v.add(cli_codiE.getStrQuery());
     v.add(emp_codiE.getStrQuery());
     s1 = creaWhere(s, v, false);
     condWhere1 = creaWhere("", v, false);
     s = "";
     v.clear();
     if (clifacE.getValor().equals("A"))
     {
       v.add(avc_anoE.getStrQuery());
       v.add(avc_serieE.getStrQuery());
       v.add(avc_numeE.getStrQuery());
       swAlb = true;
     }
     v.add(usu_nombE.getStrQuery());
     v.add(cob_feccobE.getStrQuery());
     v.add(tpc_codiE.getStrQuery());
     v.add(cob_fecvtoE.getStrQuery());
     v.add(cob_imporE.getStrQuery());
     v.add  (cob_obserE.getStrQuery());
     condWhere = creaWhere("", v, false);
     if (!condWhere.equals(""))
     {
       s1 += " and exists (select * from v_cobros as c where c.emp_codi = a.emp_codi" +
           " and c.alb_nume = a.avc_nume " +
           " and c.cob_serie = a.avc_serie " +
           " and c.cob_anofac = a.avc_ano " + condWhere + ")";
     }
   }
   // Busco datos para Fra.
   if (!swAlb)
   {
     if (!s1.equals(""))
       s1 += " UNION ";
     s1 += "SELECT distinct cli_codi FROM v_facvec as a WHERE " +
         " fvc_sumtot <> 0" +
         (!opVerCobrado.isSelected() ? " AND fvc_cobrad = 0 " : "");

     // Pongo condicion de cod. cliente
     v.clear();
     v.add(cli_codiE.getStrQuery());
     v.add(emp_codiE.getStrQuery());
     s1 = creaWhere(s1, v, false);
     condWhere1 = creaWhere("", v, false);
     ArrayList v1 = new ArrayList();
     if (clifacE.getValor().equals("F"))
     {
       v1.add(fvc_serieE.getStrQuery());
       v1.add(fvc_anoE.getStrQuery());
       v1.add(fvc_numeE.getStrQuery());
     }
     v1.add(usu_nombE.getStrQuery());
     v1.add(cob_feccobE.getStrQuery());
     v1.add(tpc_codiE.getStrQuery());
     v1.add(cob_fecvtoE.getStrQuery());
     v1.add(cob_imporE.getStrQuery());
     v1.add(cob_obserE.getStrQuery());
     condWhere = creaWhere("", v1, false);
     if (!condWhere.equals(""))
     {
       s1 += " and exists (select * from v_cobros as c  " +
           " where c.emp_codi = a.emp_codi " +
           " and c.fac_nume = a.fvc_nume " +
           " and c.cob_anofac = a.fvc_ano " + condWhere+ ")";
     }
   }
//   condWhere+=condWhere1;
   s1 += " ORDER BY 1";
    debug ("s1: "+s1+" CondWhere: "+condWhere);
   Pcabe.setQuery(false);
   try
   {
     if (!dtCons.select(s1))
     {
       mensaje("");
       mensajeErr("No encontrados Registros para estos criterios");
       rgSelect();
       activaTodo();
       verDatos(dtCons);
       return;
     }
     mensaje("");
     strSql = s1;
     activaTodo();
     rgSelect();
     verDatos(dtCons);
   }
   catch (Exception ex)
   {
     fatalError("Error al buscar Inventarios: ", ex);
   }

   mensajeErr("Filtro de Consulta ... ESTABLECIDO");
   swVerCob = true;
   swVerFra = true;
 }

 public void canc_query()
 {
   mensajeErr("Establecer Filtro de Consulta ... CANCELADO");
   mensaje("");
   Pcabe.setQuery(false);
   verDatos(dtCons);
   activaTodo();
   swVerCob=true;
   swVerFra=true;
   return;
 }

 public void PADEdit()
 {
   try {
   if (!setBloqueo(dtAdd, "v_cobros",cli_codiE.getText()))
   {
     msgBox(msgBloqueo);
     activaTodo();
     return;
   }
   } catch (Exception k)
   {
     Error("Error al bloquear registro",k);
     return;
   }
   mensaje("Editando Registros");
   Baceptar.setEnabled(true);
   Bcancelar.setEnabled(true);
   Birgrid.setEnabled(true);
   jtFra.setSelectRowDis();
   jtFra.setSelectColumnDis(0);
   jtFra.setEnabled(true);
   opVerCobrado.setEnabled(false);
   jtFra.requestFocusSelected();
 }


 public void ej_edit1()
 {
   try
   {
     if (! jtFra.isEnabled())
     {
       if (checkLinea(jtCobr.getSelectedRow())>=0)
         return;
       int nRow=jtFra.getSelectedRow();
       guardaDatCob(nRow);
     }
     resetBloqueo(dtAdd, "v_cobros", cli_codiE.getText(), true);
   } catch (Exception k)
   {
     Error("Error al Realizar Modif. Bloqueo",k);
   }

   nav.pulsado=navegador.NINGUNO;
   activaTodo();
   mensajeErr("Cobros ... ACTUALIZADOS");
   mensaje("");
 }

 public void canc_edit()
 {
   activaTodo();
   mensajeErr("Edicion Cancelada");
   try
   {
     ctUp.rollback();
     resetBloqueo(dtAdd,"v_cobros", cli_codiE.getText(),true);
/*     if (jf != null)
       {
         jf.ht.clear();
         jf.ht.put("%a", acc_numeE.getText());
         jf.guardaMens("C4", jf.ht);
       }
 */
   } catch (Exception k)
   {
     Error("Error al Cancelar Edicion",k);
   }
   mensaje("");
   verDatos(dtCons);
   nav.pulsado=navegador.NINGUNO;
 }

 public void PADAddNew()
 {
   if (!swAdd)
     jtFra.removeAllDatos();
   nav.pulsado=navegador.ADDNEW;
   swAdd=false;
   activar(true,navegador.ADDNEW);
   jtFra.setEnabled(false);
   jtCobr.setEnabled(false);
   Pcabe.resetTexto();
   usu_nombE.setEnabled(false);
   avc_cobradE.setEnabled(true);
   mensaje("Insertando COBROS  ....");
   emp_codiE.setValorDec(EU.em_cod);
   avc_anoE.setValorDec(EU.ejercicio);
   fvc_anoE.setValorDec(EU.ejercicio);
   fvc_serieE.setText("1");
   cob_feccobE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
   cob_fecvtoE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
   usu_nombE.setText(EU.usuario);
   cob_imporE.resetCambio();
   cli_codiE.requestFocus();
   tpc_codiE.setText("1");

   iAlbPeE.resetTexto();
   iAlbE.resetTexto();
   nAlPeE.resetTexto();

   iFraPeE.resetTexto();
   iFraE.resetTexto();
   nFraPeE.resetTexto();

   iTotPeE.resetTexto();
   iTotE.resetTexto();
   nTotPeE.resetTexto();
   Baceptar.setEnabled(false);
 }

 public boolean checkAddNew()
 {
   try
   {
     if (opMulti.isSelected())
     {
       int nRow=jtFra.getRowCount();
       for (int n=0;n<nRow;n++)
       {
         if (!jtFra.getValBoolean(n, 9))
           return true;
       }
       mensajeErr("Eliga algun documento como Cobrado");
       return false;
     }
     if (!cli_codiE.controlar())
     {
       mensajeErr(cli_codiE.getMsgError());
       return false;
     }
     if (clifacE.getValor().equals("A"))
     {
       if (!actDatAlb())
       {
         avc_numeE.requestFocus();
         return false;
       }
     }
     else
     {
       if (!actDatFra())
       {
         fvc_numeE.requestFocus();
         return false;
       }
     }
     if (cob_feccobE.getError() || cob_feccobE.isNull())
     {
       cob_feccobE.requestFocus();
       mensajeErr("Fecha de Cobro NO es Valida");
       return false;
     }
     long nDias=Formatear.comparaFechas(cob_feccobE.getDate(),Formatear.getDateAct());
     if (nDias>0)
     {
       if (mensajes.mensajeYesNo("Fecha Cobro superior a la Actual. Continuar ?")!=mensajes.YES)
       {
         cob_feccobE.requestFocus();
         return false;
       }
     }
     if (nDias<-6)
    {
      if (mensajes.mensajeYesNo("Fecha Cobro Inferior a la Actual en 7 dias. Continuar ?")!=mensajes.YES)
      {
        cob_feccobE.requestFocus();
        return false;
      }
    }
    if (! tpc_codiE.controla(true))
    {
      mensajeErr("Tipo de COBRO ... NO valido");
      return false;
    }
     if (cob_fecvtoE.getError() || cob_fecvtoE.isNull())
     {
       cob_fecvtoE.setText(cob_feccobE.getText());
       cob_fecvtoE.requestFocus();
       mensajeErr("Fecha de Vencimiento NO es Valida");
       return false;
     }
     nDias = Formatear.comparaFechas(cob_fecvtoE.getDate(),
                                     Formatear.getDateAct());
     if (nDias > 30)
     {
       if (mensajes.mensajeYesNo(
           "Fecha Vto. superior a la Actual en mas de 30 dias. Continuar ?") !=
           mensajes.YES)
       {
         cob_feccobE.requestFocus();
         return false;
       }
     }
     if (nDias < -6)
     {
       if (mensajes.mensajeYesNo(
           "Fecha Vto. Inferior a la Actual en 7 dias. Continuar ?") !=
           mensajes.YES)
       {
         cob_feccobE.requestFocus();
         return false;
       }
     }

     if (cob_imporE.getValorDec()==0)
     {
       if (mensajes.mensajeYesNo("Importe cobrado es  0. Continuar ?")!=mensajes.YES)
       {
         cob_imporE.requestFocus();
         return false;
       }
     }
   } catch (Exception k)
   {
     Error("Error al Comprobar ALTA",k);
   }
   return true;
 }
 void insCobro(String albFra, int empCodi,int docAno,String docSerie,int docNume,
               double importe,int avcCobr,String fecvto) throws SQLException,java.text.ParseException
 {
   dtAdd.addNew("v_cobros");
   dtAdd.setDato("emp_codi", empCodi);
   dtAdd.setDato("cob_ano", EU.ejercicio);

   if (albFra.equals("A"))
   {
     dtAdd.setDato("cob_anofac", docAno);
     dtAdd.setDato("fac_nume", 0);
     dtAdd.setDato("cob_serie", docSerie);
     dtAdd.setDato("alb_nume", docNume);
   }
   else
   {
     dtAdd.setDato("cob_anofac", docAno);
     dtAdd.setDato("fac_nume", docNume);
     dtAdd.setDato("cob_serie", "Z");
     dtAdd.setDato("fvc_serie",docSerie);
     dtAdd.setDato("alb_nume", 0);
   }
   dtAdd.setDato("tpc_codi", tpc_codiE.getText());
   dtAdd.setDato("usu_nomb", EU.usuario);
   dtAdd.setDato("cob_feccob", cob_feccobE.getText(), "dd-MM-yyyy");
//   dtAdd.setDato("cob_horcob", cob_feccobE.getText(), "dd-MM-yyyy");
   dtAdd.setDato("cob_horcob", (Date) null);

   dtAdd.setDato("cob_obser", cob_obserE.getText());
   dtAdd.setDato("rem_ejerc",0);
   dtAdd.setDato("rem_codi",0);
   dtAdd.setDato("cob_trasp", 0);
   dtAdd.setDato("cob_fecvto", fecvto,"dd-MM-yyyy");
   dtAdd.setDato("cob_impor", importe);
   dtAdd.update(stUp);
   int nRow=0;
   if (albFra.equals("A"))
   { // Actualizar Albaran
     s = "UPDATE v_albavec set avc_impcob = avc_impcob +" +importe + "," +
         "avc_cobrad = " + avcCobr+
         " WHERE emp_codi = " + empCodi +
         " and avc_ano = " + docAno+
         " and avc_serie = '" + docSerie + "'" +
         " and avc_nume = " + docNume;
   }
   else
   {
     s = "UPDATE v_facvec set fvc_impcob =  fvc_impcob + " +importe + "," +
         " fvc_cobrad = " + avcCobr +
         " WHERE emp_codi = " + empCodi +
         " and fvc_ano = " + docAno +
         " and fvc_serie = '"+docSerie+"'"+
         " and fvc_nume = " + docNume;
   }
   nRow = stUp.executeUpdate(dtAdd.getStrSelect(s));
   if (nRow != 1)
     throw new SQLException("N.Documentos Modificados diferente de 1 ("+nRow + ")\n" + s);
 }

 public void ej_addnew1()
 {
//   Formatear.verAncGrid(jtFra);
   try
   {
     if (! opMulti.isSelected())
     {
         
       insCobro(clifacE.getValor(),emp_codiE.getValorInt(),
              clifacE.getValor().equals("A")?avc_anoE.getValorInt():fvc_anoE.getValorInt(),
              clifacE.getValor().equals("A")? avc_serieE.getText():fvc_serieE.getText(),
              clifacE.getValor().equals("A")? avc_numeE.getValorInt():fvc_numeE.getValorInt(),
              cob_imporE.getValorDec(),
              avc_cobradE.isSelected() ? -1 : 0,cob_fecvtoE.getText()  );
     }
     else
     {
       int nRow=jtFra.getRowCount();
       if (nRow>5)
       {
           if (mensajes.mensajePreguntar("Se insertaran "+nRow+" Cobros. ¿ Seguro que desea continuar ?", this)!=mensajes.YES)
               return;
       }
       
       for (int n=0;n<nRow;n++)
       {
         if (jtFra.getValBoolean(n, 9) || jtFra.getValorInt(n,4)==0)
           continue;
         insCobro(jtFra.getValString(n,0),jtFra.getValorInt(n,1),
                jtFra.getValorInt(n,2),
                jtFra.getValString(n,3),
                jtFra.getValorInt(n,4),
                jtFra.getValorDec(n,8),
                -1,
                cob_fecvtoE.isNull()?jtFra.getValString(n,12):cob_fecvtoE.getText());
       }
     }
     ctUp.commit();
     verDatos(cli_codiE.getValorInt(),false);
   } catch (Exception k)
   {
     Error("Error al Insertar cobro",k);
   }
   mensajeErr("Cobros .. INSERTADO");
   nav.pulsado=navegador.ADDNEW;
   Pcabe.setVisible(true);
   PcondMult.setVisible(false);
   swAdd=true;
   PADAddNew();
 }

 public void canc_addnew()
 {
   activaTodo();
   mensajeErr("Insercion ... Cancelada");
   mensaje("");
   Pcabe.setVisible(true);
   PcondMult.setVisible(false);
   nav.pulsado=navegador.NINGUNO;
   verDatos(dtCons);
 }

 public void PADDelete()
 {
     try {
       if (condWhere.equals("")) {
           msgBox("Es obligatorio que haya alguna condicion de borrado");
            activaTodo();
           return;
        }
        s = "select count(*) as cuantos from v_cobros " +
             " WHERE 1=1 " + condWhere;
        dtCon1.select(s);
        nCobBorrar=dtCon1.getInt("cuantos");
        if (nCobBorrar==0)
        {
            msgBox("No hay selecionado ningun cobro para borrar");
            activaTodo();
            return;
        }

      
     } catch (SQLException k)
     {
         Error("Error al buscar cobros a borrar",k);
         return;
     }
   mensaje("Borrando Registros");
   Baceptar.setEnabled(true);
   Bcancelar.setEnabled(true);
 }


 public void ej_delete1()
 {
   if (nCobBorrar>5)
   {
     int res=mensajes.mensajePreguntar("Se borraran "+nCobBorrar+ " Apuntes de Cobros. Seguro que desea continuar ?");
     if (res!=mensajes.YES)
     {
        activaTodo();
        return;
     }
   }
  
   mensaje("Borrando Cobros...");
   try {
        if (jf != null) {
         jf.ht.clear();
         jf.ht.put("%c", nCobBorrar+" Cobros ");
         jf.guardaMens("R1", jf.ht);
   }
      borrarCobros();
      ctUp.commit();
   } catch (Exception k)
   {
     Error("Error al borrar cobros",k);
     return;
   }
     mensaje("");
     mensajeErr(nCobBorrar+" Cobros Borrados ");
     nav.pulsado=navegador.NINGUNO;
     activaTodo();
 }

    public void canc_delete()
    {
        nav.pulsado = navegador.NINGUNO;
        activaTodo();
        mensajeErr("Borrado Cancelado");

        mensaje("");
        verDatos(dtCons);
    }


 public void activar(boolean b)
 {
   activar(b,navegador.TODOS);
 }
 public void activar(boolean b,int modo)
 {
   cli_codiE.setEnabled(b);
   clifacE.setEnabled(b);
   Pfactu.setEnabled(b);
   Palbar.setEnabled(b);
   emp_codiE.setEnabled(b);
   cob_imporE.setEnabled(b);
   if (modo==navegador.CHOSE)
   {
     avc_cobradE.setEnabled(b);
     return;
   }
   cob_obserE.setEnabled(b);
   cob_feccobE.setEnabled(b);
   tpc_codiE.setEnabled(b);
   cob_fecvtoE.setEnabled(b);
   Baceptar.setEnabled(b);
   Bcancelar.setEnabled(b);
   usu_nombE.setEnabled(b);
   if (modo==navegador.QUERY)
     return;
   opVerCobrado.setEnabled(!b);
   Bmulti.setEnabled(b);
   opMulti.setEnabled(b);
   if (modo==navegador.ADDNEW)
     return;

   avc_impalbE.setEnabled(b);
   avc_imppenE.setEnabled(b);
   avc_fecalbE.setEnabled(b);
   avc_cobradE.setEnabled(b);
   Birgrid.setEnabled(b);
   jtFra.setEnabled(b);
   jtCobr.setEnabled(b);

 }

 void verDatos(DatosTabla dt)
 {
   verDatos(dt,opVerCobrado.isSelected());
 }

 void verDatos(DatosTabla dt,boolean opVerAlbCob)
 {
    try {
      if (dt.getNOREG())
        return;
      verDatos(dt.getInt("cli_codi"), opVerAlbCob);
   } catch (Exception k)
   {
     Error("Error al ver Datos",k);
   }
 }
 void verDatos(int cliCodi,boolean opVerAlbCob) throws SQLException, java.text.ParseException
 {
     cli_codiE.setText(""+cliCodi);
     cli_poblE.setText(cli_codiE.getLikeCliente().getString("cli_pobl"));
     swVerFra=false;
     swVerCob=false;
     jtCobr.removeAllDatos();
     jtFra.removeAllDatos();
     if (opVerAlbCob)
       verAlbCobr(cliCodi);
     else
       verAlbPend(cliCodi);
     swVerFra=true;
     swVerCob=true;
 }

 /**
  * Ver Albaranes cobrados de un cliente
  * @param cliCodi int
  * @throws SQLException
  * @throws ParseException
  */
 void verAlbCobr(int cliCodi) throws SQLException, java.text.ParseException
 {
  verAlbFra(cliCodi,true);
 }
 /**
  * Ver albaranes sin cobrar de un cliente
  * @param cliCodi Cliente
  * @throws SQLException
  * @throws ParseException
  */
 void verAlbPend(int cliCodi) throws SQLException, java.text.ParseException
 {
   verAlbFra(cliCodi,false);
 }
 void verAlbFra(int cliCodi,boolean swCobr) throws SQLException, java.text.ParseException
 {
   // Busco los Albaranes y Fras
   s = "SELECT 'A' as albfra,count(*) as cuantos, sum(avc_impalb) as impDoc, "+
       " sum(avc_impalb-avc_impcob) as impPend "+
       " FROM v_albavec WHERE cli_codi = " + cliCodi +
        " AND  (fvc_nume = 0 or fvc_nume is null) " +
        (EU.isRootAV()?"":" AND div_codi > 0 ")+
        (!swCobr?" AND avc_cobrad = 0 ":"")+
        (!swCobr || feincoE.isNull()?"":" and avc_fecalb >= to_date('"+feincoE.getText()+"','dd-MM-yyyy')" )+
        (!swCobr || feficoE.isNull()?"":" and avc_fecalb <= to_date('"+feficoE.getText()+"','dd-MM-yyyy')" )+
        " and avc_impalb <> 0"+
        " UNION ALL"+
        " SELECT 'F' as albfra, COUNT(*) as cuantos,SUM(fvc_sumtot) as impDoc, "+
        " SUM(fvc_sumtot-fvc_impcob) as impPend "+
       " FROM v_facvec WHERE "+
        " cli_codi = " + cliCodi +
        (!swCobr?" AND fvc_cobrad = 0 ":"")+
        (!swCobr || feincoE.isNull()?"":" and fvc_fecfra >= to_date('"+feincoE.getText()+"','dd-MM-yyyy')" )+
        (!swCobr || feficoE.isNull()?"":" and fvc_fecfra <= to_date('"+feficoE.getText()+"','dd-MM-yyyy')" )+
        " and fvc_sumtot <> 0" ;

    double iAlb=0,iAlbPen=0,iFra=0,iFraPen=0;
    int nAlb=0,nFra=0;
    if (dtCon1.select(s))
    {
      do
      {
        if (dtCon1.getString("albfra").equals("A"))
        {
          nAlb+=dtCon1.getInt("cuantos",true);
          iAlb+=dtCon1.getDouble("impDoc",true);
          iAlbPen+=dtCon1.getDouble("impPend",true);
        }
        else
        {
          nFra+=dtCon1.getInt("cuantos",true);
          iFra+=dtCon1.getDouble("impDoc",true);
          iFraPen+=dtCon1.getDouble("impPend",true);
        }
      } while (dtCon1.next());
    }
    iAlbPeE.setValorDec(iAlbPen);
    iAlbE.setValorDec(iAlb);
    nAlPeE.setValorDec(nAlb);

    iFraPeE.setValorDec(iFraPen);
    iFraE.setValorDec(iFra);
    nFraPeE.setValorDec(nFra);

    iTotPeE.setValorDec(iAlbPen+iFraPen);
    iTotE.setValorDec(iAlb+iFra);
    nTotPeE.setValorDec(nAlb+nFra);


   s = "SELECT 'A' as albfra, f.emp_codi, avc_ano as ano,avc_serie as serie,avc_nume as nume,"+
       " avc_fecalb as fecha,avc_impalb as impdoc,avc_impcob as impcob, "+
       "avc_impalb-avc_impcob as imppen, "+
       "avc_cobrad AS tocob,cl.cli_codi,cl.cli_nomb,'' as fecvto"+
      " FROM v_albavec as f,clientes cl WHERE f.cli_codi = " + cliCodi +
       " AND  fvc_nume = 0" +
       " and cl.cli_codi = f.cli_codi "+
       (EU.isRootAV()?"":" AND f.div_codi > 0 ")+
       (!swCobr?" AND avc_cobrad = 0 ":"")+
       (!swCobr || feincoE.isNull()?"":" and avc_fecalb >= to_date('"+feincoE.getText()+"','dd-MM-yyyy')" )+
       (!swCobr || feficoE.isNull()?"":" and avc_fecalb <= to_date('"+feficoE.getText()+"','dd-MM-yyyy')" )+
       " and avc_impalb <> 0" +
       " UNION ALL"+
       " SELECT 'F' as albfra, f.emp_codi, fvc_ano as ano,f.fvc_serie as serie,fvc_nume as nume,"+
       " fvc_fecfra as fecha,fvc_sumtot as impdoc,fvc_impcob as impcob, "+
       "fvc_sumtot-fvc_impcob as imppen, "+
        "fvc_cobrad AS tocob,cl.cli_codi,cl.cli_nomb,'' as fecvto "+
      " FROM v_facvec as f,clientes cl WHERE "+
       " cl.cli_codi = f.cli_codi "+
       " and f.cli_codi = " + cliCodi +
       (!swCobr?" AND fvc_cobrad = 0 ":"")+
       (!swCobr || feincoE.isNull()?"":" and fvc_fecfra >= to_date('"+feincoE.getText()+"','dd-MM-yyyy')" )+
       (!swCobr || feficoE.isNull()?"":" and fvc_fecfra <= to_date('"+feficoE.getText()+"','dd-MM-yyyy')" )+
       " and fvc_sumtot <> 0";
      s+=" ORDER BY 6 DESC,1 DESC,2 DESC,3 DESC,4 DESC"; // Fec. Alb,Emp,Ano,Serie,Numero
 //  debug(s);
   dtAlb.select(s);
   jtFra.setDatos(dtAlb);
   jtFra.setEnabled(true);
   if (nav.pulsado == navegador.ADDNEW)
   {
     jtFra.requestFocus(jtFra.getRowCount()-1,0);
     jtFra.setSelectRowDis(jtFra.getRowCount()-1);
     actDatFra(jtFra.getRowCount()-1);
   }
   else
   {
     jtFra.requestFocusInicio();
     actDatFra(0);
   }
 }
 private boolean ponEstFra(boolean estad) throws Exception
 {
    if (jtFra.getValString(0).equals("A"))
    { // Actualizar Albaran
      s="UPDATE v_albavec set avc_cobrad = "+(estad?0:-1)+
          " WHERE emp_codi = " + jtFra.getValInt(1)+
          " AND  fvc_nume = 0" +
          " and avc_ano = "+jtFra.getValInt(2)+
          " and avc_serie = '"+jtFra.getValString(3)+"'"+
          " and avc_nume = "+jtFra.getValInt(4);
    }
    else
    {
      s="UPDATE v_facvec set fvc_cobrad = "+(estad?0:-1)+
          " WHERE emp_codi = "+ jtFra.getValInt(1)+
          " and fvc_ano = "+jtFra.getValInt(2)+
          " and fvc_nume = "+jtFra.getValInt(4)+
          " and fvc_serie = '"+jtFra.getValString(3)+"'"+
          " and fvc_sumtot <> 0";
    }
    int nRow=stUp.executeUpdate(s);
    if (nRow==0)
      return false;
    return true;
 }

 void irGrid()
 {
   try {
     int nRow;
     if (jtFra.isEnabled())
     {
       jtCobr.setSelectColumnDis();
       jtCobr.setSelectRowDis();
       swVerCob = false;
       swVerFra = false;
       jtFra.setEnabled(!jtFra.isEnabled());
       jtCobr.setEnabled(!jtCobr.isEnabled());
       jtCobr.requestFocusSelected();
       swVerCob = true;
//     swVerFra=true;
       return;
     }
     if (!jtFra.isEnabled())
     {
       int col;
       if ( (col = checkLinea(jtCobr.getSelectedRow())) >= 0)
       {
         jtCobr.requestFocus();
         jtCobr.requestFocus(jtCobr.getSelectedRow(), col);
         return;
       }
       swVerCob = false;
       swVerFra = false;
       nRow = jtFra.getSelectedRow();
       guardaDatCob(nRow);
       jtCobr.setEnabled(!jtCobr.isEnabled());
       jtFra.setSelectColumnDis();
       jtFra.setSelectRowDis();
       jtFra.setEnabled(!jtFra.isEnabled());
       swVerCob = true;
       swVerFra = true;
       jtFra.requestFocusSelected();
       verDatCob(jtFra.getSelectedRow());
     }
   } catch (Exception k)
   {
     Error("error al cambiar de grid",k);
   }
 }

    private void borrarCobros() throws SQLException
    {

//        double impCob;
//        condWhere = "cob_feccob='20091809'";
      
        s = "select * from v_cobros " +
             " WHERE 1=1 " + condWhere;
        if (!dtCon1.select(s)) {
            msgBox("No hay cobros con esas condiciones");
            return;
        }
        do {
//            impCob=dtStat.getDouble("cob_impor");
            if (dtCon1.getString("cob_serie").equals("Z")) {
                s = "delete from v_cobros " + // Facturas
                     " WHERE cob_anofac = " + dtCon1.getInt("cob_anofac") +
                     " and emp_codi = " + dtCon1.getInt("emp_codi") +
                     " and fac_nume = " + dtCon1.getInt("fac_nume") +
                     " and alb_nume = 0 " +             condWhere;
            } else {
                s = "DELETE from v_cobros " + // Albaranes
                     " WHERE COB_ANOFAC= " + dtCon1.getInt("cob_anofac") +
                     " and emp_codi = " + dtCon1.getInt("emp_codi") +
                     " and alb_nume = " + dtCon1.getInt("alb_nume") +
                     " and cob_serie = '" + dtCon1.getString("cob_serie") + "'" +
                     condWhere;
            }
            dtAdd.executeUpdate(s);
            if (! dtCon1.getString("cob_serie").equals("Z")) {
                s = "select sum(cob_impor) as cob_impor from v_cobros " + // Albaranes
                     " WHERE COB_ANOFAC= " + dtCon1.getInt("cob_anofac") +
                     " and emp_codi = " + dtCon1.getInt("emp_codi") +
                     " and alb_nume = " + dtCon1.getInt("alb_nume") +
                     " and cob_serie = '" + dtCon1.getString("cob_serie") + "'";
                dtStat.select(s);
                s = "UPDATE v_albavec set avc_impcob = " + dtStat.getDouble("cob_impor",true) +
                     ", avc_cobrad = 0 "+
                     " WHERE emp_codi = " + dtCon1.getInt("emp_codi") +
                     " AND  fvc_nume = 0" +
                     " and avc_ano = " + dtCon1.getInt("cob_anofac") +
                     " and avc_serie = '" + dtCon1.getString("cob_serie") + "'" +
                     " and avc_nume = " + dtCon1.getInt("alb_nume");
                dtAdd.executeUpdate(s);
            } else {
                s = "select  sum(cob_impor) as cob_impor from v_cobros " + // Facturas
                     " WHERE cob_anofac = " + dtCon1.getInt("cob_anofac") +
                     " and emp_codi = " + dtCon1.getInt("emp_codi") +
                     " and fac_nume = " + dtCon1.getInt("fac_nume") +
                     " and fvc_serie = '" + dtCon1.getString("fvc_serie") + "'" +
                     " and alb_nume = 0 ";
                dtStat.select(s);

                s = "UPDATE v_facvec set fvc_impcob = " + dtStat.getDouble("cob_impor",true) +
                     ", fvc_cobrad = 0"+
                     " WHERE emp_codi = " + dtCon1.getInt("emp_codi") +
                     " and fvc_ano = " + dtCon1.getInt("cob_anofac") +
                     " and fvc_nume = " + dtCon1.getInt("fac_nume") +
                     " and fvc_serie = '" + dtCon1.getInt("fvc_serie") + "'" +
                     " and fvc_sumtot <> 0";
                dtAdd.executeUpdate(s);
            }

        } while (dtCon1.next());
        
    }
 /**
  * Guarda Datos de Cobros de el Documento Activo
  * @param row int
  */
 private void  guardaDatCob(int row)
 {
   if (jtFra.getValString(row,0).equals("F"))
     s ="delete from v_cobros "+
        " WHERE cob_anofac = " + jtFra.getValorInt(row, 2) +
        " and emp_codi = " +jtFra.getValorInt(row,1)+
        " and fac_nume = " + jtFra.getValorInt(row, 4) +
        " and alb_nume = 0 ";
    else
     s = "DELETE from v_cobros "+
          " WHERE  "+
//          " cob_anofac =  0 OR +
          " COB_ANOFAC= " + jtFra.getValorInt(row, 2) +
          " and emp_codi = " + jtFra.getValorInt(row,1)+
          " and alb_nume = " + jtFra.getValorInt(row, 4) +
          " and cob_serie = '" + jtFra.getValString(row, 3) + "'";
    try
    {
      int nRow;
      nRow=stUp.executeUpdate(dtBloq.getStrSelect(s));
      nRow=jtCobr.getRowCount();
      double impCobr = 0;
      for (int n = 0; n < nRow; n++)
      {
        if (jtCobr.getValorDec(n, 3) == 0)
          continue; // Sin importe
        dtAdd.addNew("v_cobros");
        dtAdd.setDato("emp_codi", jtFra.getValInt(row, 1));
        dtAdd.setDato("cob_ano", EU.ejercicio);
        if (jtFra.getValString(0).equals("A"))
        {
          dtAdd.setDato("cob_anofac", jtFra.getValInt(row,2));
          dtAdd.setDato("fac_nume", 0);
          dtAdd.setDato("cob_serie", jtFra.getValString(row,3));
          dtAdd.setDato("alb_nume", jtFra.getValString(row,4));
        }
        else
        {
          dtAdd.setDato("cob_anofac", jtFra.getValInt(row,2));
          dtAdd.setDato("fac_nume", jtFra.getValInt(row,4));
          dtAdd.setDato("cob_serie",jtFra.getValString(row,3));
          dtAdd.setDato("alb_nume",0 );
        }
        dtAdd.setDato("tpc_codi",tpc_codiG.getTexto(jtCobr.getValString(n,0)));
        dtAdd.setDato("usu_nomb", EU.usuario);
        dtAdd.setDato("cob_feccob", jtCobr.getValString(n,1), "dd-MM-yyyy");
//        dtAdd.setDato("cob_horcob",jtCobr.getValString(n,1), "dd-MM-yyyy");
        dtAdd.setDato("cob_horcob",(Date) null);
        dtAdd.setDato("cob_obser", jtCobr.getValString(n, 4));
        dtAdd.setDato("rem_ejerc",0);
        dtAdd.setDato("rem_codi", 0);
        dtAdd.setDato("cob_trasp", 0);
        dtAdd.setDato("cob_fecvto", jtCobr.getValString(n, 2), "dd-MM-yyyy");
        dtAdd.setDato("cob_impor", jtCobr.getValorDec(n, 3));
        impCobr+=jtCobr.getValorDec(n, 3);
        dtAdd.update(stUp);
      }
      if (jtFra.getValString(row,0).equals("A"))
      { // Actualizar Albaran
        s = "UPDATE v_albavec set avc_impcob = " + impCobr+
            " WHERE emp_codi = " + jtFra.getValorInt(row,1) +
            " AND  fvc_nume = 0" +
            " and avc_ano = " + jtFra.getValorInt(row,2) +
            " and avc_serie = '" + jtFra.getValString(row,3) + "'" +
            " and avc_nume = " + jtFra.getValorInt(row,4);
      }
      else
      {
        s = "UPDATE v_facvec set fvc_impcob = " + impCobr +
            " WHERE emp_codi = " + jtFra.getValorInt(row,1) +
            " and fvc_ano = " + jtFra.getValorInt(row,2) +
            " and fvc_nume = " + jtFra.getValorInt(row,4) +
            " and fvc_serie = '" + jtFra.getValString(row,3) + "'" +
            " and fvc_sumtot <> 0";
      }
      stUp.executeUpdate(dtBloq.getStrSelect(s));
      jtFra.setValor(""+impCobr,row,7);
      jtFra.setValor(""+(jtFra.getValorDec(row,6)-impCobr),row,8);
      jtFra.setValor(new Boolean(impCobr<jtFra.getValorDec(row,6)),row,9);
      ponEstFra(jtFra.getValBoolean(9));
      ctUp.commit();
      mensajeErr("Datos de Cobros ... Actualizados");
    }
    catch (Exception k)
    {
      Error("Error al Actualizar datos de Cobros", k);
    }

 }

 int checkLinea(int row)
 {
   if (cob_imporG.getValorDec() == 0)
     return -1; // Importe cobrado es 0 PASAMOS

   if (!tpc_codiG.controla(false))
   {
     mensajeErr("Tipo de Cobro NO VALIDO");
     return 0;
   }
   if (cob_feccobG.getError() || cob_feccobG.isNull())
   {
     mensajeErr("Fecha de Cobro ... NO VALIDA");
     return 1;
   }
   if (cob_fecvtoG.getError() || cob_fecvtoG.isNull())
   {
     cob_fecvtoG.setText(cob_feccobG.getText());
     mensajeErr("Fecha de Vencimiento ... NO VALIDA");
     return 2;
   }

   /*     if (Formatear.comparaFechas(cob_feccobG.getDate(), Formatear.getDateAct()) < 0)
        {
    mensajes.mensajeAviso("Fecha de Cobro NO DEBERIA ser inferior a la Actual");
          return -1;
        }
    if (Formatear.comparaFechas(cob_fecvtoG.getDate(), Formatear.getDateAct()) < 0)
        {
    mensajes.mensajeAviso("Fecha de Vto. NO DEBERIA ser inferior a la Actual");
          return -1;
        }

       }  catch (java.text.ParseException k)
       {
         k.printStackTrace();
       }
    */
   return -1;
 }

 boolean actDatFra()
 {
   try
   {
     s="SELECT * FROM v_facvec WHERE "+
         " emp_codi = " + emp_codiE.getValorInt()+
         " and cli_codi = " + cli_codiE.getValorInt()+
         " and fvc_serie = '"+fvc_serieE.getText()+"'"+
         " and fvc_ano = "+fvc_anoE.getValorInt()+
        " and fvc_nume = "+fvc_numeE.getValorInt();
    if (!dtCon1.select(s))
    {
      mensajeErr("Factura ... NO encontrada para este cliente");
      return false;
    }
    if (dtCon1.getInt("fvc_cobrad")!=0)
    {
      mensajeErr("Factura .... TOTALMENTE Cobrada");
      return false;
    }
    avc_fecalbE.setText(dtCon1.getFecha("fvc_fecfra","dd-MM-yyyy"));
    avc_impalbE.setValorDec(dtCon1.getDouble("fvc_sumtot"));
    avc_imppenE.setValorDec(dtCon1.getDouble("fvc_sumtot")-cob_imporE.getValorDec());
   } catch (Exception k)
   {
     Error("Error al Act. Datos Factura",k);
   }
   return true;
 }

 boolean actDatAlb()
 {
   try
   {
     s = "SELECT * FROM v_albavec WHERE " +
         " emp_codi = " + emp_codiE.getValorInt() +
         " and cli_codi = " + cli_codiE.getValorInt() +
         " and avc_ano = " + avc_anoE.getValorInt() +
         " and avc_serie = '" + avc_serieE.getText() + "'" +
         " and avc_nume = " + avc_numeE.getValorInt();
     if (!dtCon1.select(s))
     {
       mensajeErr("Albaran ... NO encontrado para este cliente");
       return false;
     }
     if (dtCon1.getInt("fvc_nume") != 0)
     {
       mensajeErr("Albaran ya esta Facturado ... (" + dtCon1.getInt("fvc_nume") +
                  ")");
       return false;
     }
     if (dtCon1.getInt("avc_cobrad") != 0)
     {
       mensajeErr("Albaran .... TOTALMENTE Cobrado");
       return false;
     }
     avc_fecalbE.setText(dtCon1.getFecha("avc_fecalb", "dd-MM-yyyy"));
     avc_impalbE.setValorDec(dtCon1.getDouble("avc_impalb"));
     avc_imppenE.setValorDec(dtCon1.getDouble("avc_impalb") -
                             cob_imporE.getValorDec());
   }
   catch (Exception k)
   {
     Error("Error al Act. Datos Albaran", k);
   }
   return true;
 }
 void salirWCobMul(boolean aceptado)
 {
   wCobMul.setVisible(false);
   this.setEnabled(true);
   this.toFront();
   try
   {
     this.setSelected(true);
   }
   catch (Exception k) {}
 }
 void Bmulti_actionPerformed()
 {
//     try {
//   if (wCobMul == null)
//   {
//     wCobMul = new IFCondCobros();
//     vl.add(wCobMul,new Integer(1));
//     wCobMul.setLocation(50, 40);
//     wCobMul.iniciar(dtCon1,this,EU);
//     wCobMul.addInternalFrameListener(new InternalFrameAdapter()
//     {
//         public void internalFrameClosing(InternalFrameEvent e)
//         {
//           salirWCobMul(false);
//         }
//     });
//   }
//   } catch (Exception k)
//   {
//         Error("Erorr al Iniciar ventana de cobros multiples",k);
//         return;
//   }
//   wCobMul.setVisible(true);
   Pcabe.setVisible(false);
   PcondMult.setVisible(true);
   opMulti.setSelected(true);
   activar(false,navegador.CHOSE);
   feciniE.requestFocus();
 }
 void Bvolver_actionPerformed()
 {
   Bconsulta_actionPerformed();
   Pcabe.setVisible(true);
   PcondMult.setVisible(false);
 }
 void Bconsulta_actionPerformed()
 {
   String cc = "";
   if (cliIniE.getValorInt()>cliFinE.getValorInt())
   {
     mensajeErr("Cliente Final NO puede ser inferior a Inicial");
     cliFinE.setValorInt(cliIniE.getValorInt());
     cliFinE.requestFocus();
     return;
   }
   if (fpaIniE.getValorInt() > fpaFinE.getValorInt())
   {
     mensajeErr("Forma Pago Final NO puede ser inferior a Inicial");
     fpaFinE.setValorInt(fpaIniE.getValorInt());
     fpaFinE.requestFocus();
     return;
   }
   if (fvc_numeE2.getValorInt() > fvc_numeE1.getValorInt())
   {
     mensajeErr("Factura Final NO puede ser inferior a Inicial");
     fvc_numeE2.setValorInt(fvc_numeE1.getValorInt());
     fvc_numeE2.requestFocus();
     return;
   }

   if (!empIniE.isNull())
     cc += " and f.emp_codi >= " + empIniE.getValorInt();
   if (!empFinE.isNull())
     cc += " and f.emp_codi <= " + empFinE.getValorInt();

   if (!cliIniE.isNull())
     cc += " and cl.cli_codi >= " + cliIniE.getValorInt();
   if (!cliFinE.isNull())
     cc += " and cl.cli_codi <= " + cliIniE.getValorInt();
   if (!fpaIniE.isNull())
     cc += " and cl.fpa_codi >= " + fpaIniE.getValorInt();
   if (!fpaFinE.isNull())
     cc += " and cl.fpa_codi <= " + fpaFinE.getValorInt();
   if (!cli_giroE.getValor().equals("X"))
     cc += " and cl.cli_giro = '" + cli_giroE.getValor() + "'";

   s = "SELECT 'A' as albfra, f.emp_codi, avc_ano as ano,avc_serie as serie,avc_nume as nume," +
       " avc_fecalb as fecha,avc_impalb as impdoc,avc_impcob as impcob, " +
       "avc_impalb-avc_impcob as imppen, " +
       "avc_cobrad AS tocob,cl.cli_codi,cl.cli_nomb," +
       " cl.cli_dipa1,cl.cli_dipa2,fp.fpa_dia1,fp.fpa_dia2,fp.fpa_dia3" +
       " FROM v_albavec f,clientes as cl,v_forpago as fp WHERE  fvc_nume = 0" +
       " and f.cli_codi = cl.cli_codi " +
       (EU.isRootAV()?"":" AND f.div_codi > 0 ")+
       " and fp.fpa_codi = cl.fpa_codi "+
       " AND avc_cobrad = 0 "+
       " AND avc_cobrad = 0 " +
       " and avc_impalb <> 0";
   if (!feciniE.isNull())
     s += " and f.avc_fecalb >= TO_DATE('" + feciniE.getText() +
         "','dd-MM-yyyy')";
   if (!fecfinE.isNull())
     s += " and f.avc_fecalb <= TO_DATE('" + fecfinE.getText() +
         "','dd-MM-yyyy')";
   if (!serieIniE.isNull(true))
     s += " and f.avc_serie >= '" + serieIniE.getText() + "'";
   if (!serieFinE.isNull(true))
     s += " and f.avc_serie <= '" + serieFinE.getText() + "'";
   s+=cc;

   s += " UNION ALL" +
       " SELECT 'F' as albfra, f.emp_codi, fvc_ano as ano,fvc_serie as serie,fvc_nume as nume," +
       " fvc_fecfra as fecha,fvc_sumtot as impdoc,fvc_impcob as impcob, " +
       "fvc_sumtot-fvc_impcob as imppen, " +
       "fvc_cobrad AS tocob,cl.cli_codi,cl.cli_nomb,"+
       " cl.cli_dipa1,cl.cli_dipa2,fp.fpa_dia1,fp.fpa_dia2,fp.fpa_dia3" +
       " FROM v_facvec as f,clientes cl,v_forpago as fp WHERE " +
       " fvc_sumtot <> 0" +
       " and fp.fpa_codi = cl.fpa_codi "+
       " AND fvc_cobrad = 0 "+
       " and f.cli_codi = cl.cli_codi ";
   if (!feciniE.isNull())
     s += " and f.fvc_fecfra >= TO_DATE('" + feciniE.getText() +
         "','dd-MM-yyyy')";
   if (!fecfinE.isNull())
     s += " and f.fvc_fecfra <= TO_DATE('" + fecfinE.getText() +
         "','dd-MM-yyyy')";
   if (fvc_numeE2.getValorInt()>0)
     s += " and f.fvc_nume >= " + fvc_numeE2.getValorInt();
   if (fvc_numeE1.getValorInt()>0)
     s += " and f.fvc_nume <= " + fvc_numeE1.getValorInt();
   s+=cc;
//   debug(s);
   try {
     if (!dtCon1.select(s))
     {
       mensajeErr("No encontrados Documentos para estos criterios");
       return;
     }

     swVerFra=false;
     do
     {

       ArrayList v=new ArrayList();
       if (!exisLinea(dtCon1))
       {
         v.add(dtCon1.getString("albfra"));
         v.add(dtCon1.getString("emp_codi"));
         v.add(dtCon1.getString("ano"));
         v.add(dtCon1.getString("serie"));
         v.add(dtCon1.getString("nume"));
         v.add(dtCon1.getFecha("fecha","dd-MM-yyyy"));
         v.add(dtCon1.getString("impdoc"));
         v.add(dtCon1.getString("impcob"));
         v.add(dtCon1.getString("imppen"));
         v.add("N"); // Pend. a NO
         v.add(dtCon1.getString("cli_codi"));
         v.add(dtCon1.getString("cli_nomb"));
         clFactCob.calDiasVto(dtCon1.getInt("fpa_dia1"),
                              dtCon1.getInt("fpa_dia2"),
                              dtCon1.getInt("fpa_dia3"),
                              dtCon1.getInt("cli_dipa1"),
                              dtCon1.getInt("cli_dipa2"), 0,
                              dtCon1.getFecha("fecha", "dd-MM-yyyy"));
         v.add(clFactCob.diasVto[0]);
         jtFra.addLinea(v);
       }
     } while (dtCon1.next());
     swVerFra = true;
     verDatCob(jtFra.getSelectedRow());
     actSumGrid();
   } catch (Throwable k)
   {
     Error("Error al cargar datos Maxivos",k);
   }
 }
 void actSumGrid()
 {
   double iAlb=0,iAlbPen=0,iFra=0,iFraPen=0;
   int nAlb=0,nFra=0;
   int nRow=jtFra.getRowCount();
   for (int n=0;n<nRow;n++)
   {
     if (jtFra.getValBoolean(n, 9))
           continue;
     if (jtFra.getValString(n,0).equals("A"))
     {
       nAlb++;
       iAlb += jtFra.getValorDec(n,6);
       iAlbPen +=  jtFra.getValorDec(n,8);
     }
     else
     {
       nFra++;
       iFra +=  jtFra.getValorDec(n,6);
       iFraPen += jtFra.getValorDec(n,8);
     }
   }
   iAlbPeE.setValorDec(iAlbPen);
   iAlbE.setValorDec(iAlb);
   nAlPeE.setValorDec(nAlb);

   iFraPeE.setValorDec(iFraPen);
   iFraE.setValorDec(iFra);
   nFraPeE.setValorDec(nFra);

   iTotPeE.setValorDec(iAlbPen + iFraPen);
   iTotE.setValorDec(iAlb + iFra);
   nTotPeE.setValorDec(nAlb + nFra);

 }
 boolean exisLinea(DatosTabla dt) throws SQLException
 {
   int nRow=jtFra.getRowCount();
   for (int n=0;n<nRow;n++)
   {
     if (dtCon1.getString("albfra").equals(jtFra.getValString(n,0)) &&
         dtCon1.getInt("emp_codi")==jtFra.getValInt(n,1) &&
         dtCon1.getInt("ano")==jtFra.getValInt(n,2) &&
         dtCon1.getString("serie").equals(jtFra.getValString(n,3)) &&
         dtCon1.getInt("nume")==jtFra.getValInt(n,4))
       return true;
   }
   return false;
 }
 void opMulti_actionPerformed()
 {
   boolean b;
   if (opMulti.isSelected())
   {
     activar(false,navegador.CHOSE);
     Bmulti_actionPerformed();
   }
   else
   {
     activar(true,navegador.CHOSE);
   }
 }

}
