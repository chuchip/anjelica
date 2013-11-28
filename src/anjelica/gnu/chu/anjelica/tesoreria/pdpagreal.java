package gnu.chu.anjelica.tesoreria;

/**
 * <p>Titulo: pdpagreal </p>
 * <p>Descripción: Mantenimiento Pagos realizados</p>
 * <p>Copyright: Copyright (c) 2005-2009
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los terminos de la Licencia Pública General de GNU seg�n es publicada por
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

import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import gnu.chu.sql.*;
import java.sql.*;
import java.awt.*;
import java.util.*;
import gnu.chu.interfaces.*;
import java.awt.event.*;
import javax.swing.*;
import gnu.chu.camposdb.*;
import gnu.chu.Menu.*;
import gnu.chu.anjelica.pad.pdempresa;
import net.sf.jasperreports.engine.*;


public  class pdpagreal extends ventanaPad implements PAD
{
  private String condFecha;
  private String sqlGrupVto;
  CButton  Bimpri   = new CButton(Iconos.getImageIcon("print"));
  int empGrid=0;
  int prvCodi;
  int prvApunte;
  String prvNombre;
  double impPagadoDet=0;
  boolean swVerDet=true;
  int nLineasDet=0;
  String s;
  DatosTabla dtAux;
  boolean admin = false;
  CPanel Pprinc = new CPanel();
  prvPanel prv_codiE = new prvPanel();
  CComboBox lbv_origeE = new CComboBox();
  CComboBox lbv_pagadoE = new CComboBox();

  traPanel tra_codiE = new traPanel();
  CTextField lbv_imppagE = new CTextField(Types.DECIMAL, "----,--9.99");
  CLabel cLabel6 = new CLabel();
  CLabel cLabel8 = new CLabel();
  CTextArea lbv_comenE = new CTextArea();
  CScrollPane lbv_comenS = new CScrollPane();
//  CButton Baceptar = new CButton("Aceptar");
//  CButton Bcancelar = new CButton("Cancelar");
  CPanel Pfiltro = new CPanel();
  CPanel Pdatos = new CPanel();
  CLabel cLabel10 = new CLabel();
  CLabel cLabel11 = new CLabel();
  CTextField fevtiniE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CButton Bacepfil = new CButton("Aceptar",Iconos.getImageIcon("check"));
  CTextField fevtfinE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CPanel Pconpag = new CPanel();
  CLabel cLabel1 = new CLabel();
  CLinkBox bat_codiE = new CLinkBox();
  CLabel cLabel2 = new CLabel();
  CTextField lbp_fecpagE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel3 = new CLabel();
  CComboBox lbp_tippagE = new CComboBox();
  CButton Bcondpag = new CButton();
  CLabel cLabel4 = new CLabel();
  CTextField lbv_numpagE = new CTextField(Types.DECIMAL,"#9");
  CGridEditable jtPag;
  CGridEditable jtApu;
  CLabel cLabel5 = new CLabel();
  CPanel Pcondef = new CPanel();
  CTextField lbv_numfraE = new CTextField(Types.DECIMAL, "#####9");
  CLabel cLabel9 = new CLabel();
  CTextField eje_numeE = new CTextField(Types.DECIMAL, "###9");
  CLabel eje_numeL = new CLabel();
  CTextField fefrinE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel15 = new CLabel();
  CLabel cLabel16 = new CLabel();
  CTextField fefrfiE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel7 = new CLabel();
  CButton Bvecobu = new CButton(Iconos.getImageIcon("unload"));
  CLabel cLabel14 = new CLabel();
  CTextField emp_codiE = new CTextField(Types.DECIMAL,"#9");
  // Campos del Grid de Pagos
  CTextField lbv_origeG = new CTextField(Types.CHAR, "?");
  CTextField eje_numeG = new CTextField(Types.DECIMAL, "###9");
  CTextField lbv_numfraG = new CTextField(Types.DECIMAL, "#####9");
  CTextField lbv_numeG = new CTextField(Types.DECIMAL, "#9");
  CTextField lbv_nombreG = new CTextField(Types.CHAR, "X", 50);
  CTextField lbv_impvtoG = new CTextField(Types.DECIMAL, "----,--9.99");
  CTextField lbv_fecvtoG = new CTextField(Types.DATE, "dd-MM-yy");
  CTextField lbv_imppagG = new CTextField(Types.DECIMAL, "----,--9.99");
  CTextField ban_codiG = new CTextField(Types.DECIMAL, "9999");
  CTextField lbp_baoficG = new CTextField(Types.DECIMAL, "9999");
  CTextField lbp_badicoG = new CTextField(Types.DECIMAL, "99");
  CTextField lbp_bacuenG = new CTextField(Types.DECIMAL, "9999999999");
  CTextField lbp_numtalG = new CTextField(Types.CHAR, "X", 15);
  CTextField lbp_facprvG = new CTextField(Types.CHAR, "X", 50);
  CCheckBox lbp_totpagG = new CCheckBox();
  CTextField lbp_numlinG = new CTextField(Types.DECIMAL, "#####9");

  // Campos del Grid de Apuntes
  CTextField eje_numeP = new CTextField(Types.DECIMAL, "###9");
  CTextField lbv_numfraP = new CTextField(Types.DECIMAL, "#####9");
  CTextField lbv_numeP = new CTextField(Types.DECIMAL, "#9");
  CTextField lbv_imppagP = new CTextField(Types.DECIMAL, "----,--9.99");
  CCheckBox lbp_totpagP = new CCheckBox();
  CTextField lbv_fecvtoP = new CTextField(Types.DATE, "dd-MM-yy");
  CTextField lbv_impvtoP = new CTextField(Types.DECIMAL, "###9");
  CTextField lip_nulideP = new CTextField(Types.DECIMAL, "#9");

  CButton Bdetalle = new CButton();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CTextField emp_codiE1 = new CTextField(Types.DECIMAL,"#9");
  CLabel cLabel17 = new CLabel();
  CLabel cLabel12 = new CLabel();
  CTextField lbp_numeE = new CTextField(Types.DECIMAL,"#####9");
  CTextField numpagosE = new CTextField(Types.DECIMAL,"###9");
  CLabel cLabel13 = new CLabel();
  CComboBox fcc_conpagE = new CComboBox();

  public pdpagreal()
  {
    try
    {
      jbInit();
    } catch (Exception k){

    }
  }

  public pdpagreal(EntornoUsuario eu, Principal p)
  {
    this(eu, p, new Hashtable());
  }

  public pdpagreal(EntornoUsuario eu, Principal p, Hashtable ht)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    try
    {
      if (ht != null)
      {
//       if (ht.get("modPrecio") != null)
//         modPrecio = Boolean.valueOf(ht.get("modPrecio").toString()).
//             booleanValue();
      }
      setTitulo("Mant. Pagos Realizados");

      if (jf.gestor.apuntar(this))
        jbInit();
      else
        setErrorInit(true);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      setErrorInit(true);
    }
  }

  public pdpagreal(gnu.chu.anjelica.menu p, EntornoUsuario eu)
  {
    EU = eu;
    vl = p.getLayeredPane();
    setTitulo("Mant. Pagos Realizados");
    eje = false;

    try
    {
      jbInit();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      setErrorInit(true);
    }
  }

  private void jbInit() throws Exception
  {
    iniciarFrame();
    this.setSize(new Dimension(760, 505));
    this.setVersion("2006-12-19 " + (admin ? "-ADMINISTRADOR-" : ""));

    strSql = "SELECT emp_codi,lbp_nume FROM libpagcab WHERE emp_codi = " + EU.em_cod +
        " group by emp_codi,lbp_nume "+
        getOrderQuery();

    statusBar = new StatusBar(this);
    nav = new navegador(this, dtCons, false, navegador.NORMAL);
    conecta();

    cLabel13.setAlignmentX((float) 0.0);
    cLabel13.setText("Conf.");
    cLabel13.setBounds(new Rectangle(383, 45, 39, 17));
    fcc_conpagE.setAlignmentX((float) 0.0);
    fcc_conpagE.setBounds(new Rectangle(418, 45, 46, 17));
    statusBar.add(Bimpri, new GridBagConstraints(9, 0, 1, 2, 0.0, 0.0
                                                 , GridBagConstraints.EAST,
                                                 GridBagConstraints.VERTICAL,
                                                 new Insets(0, 5, 0, 0), 0, 0));
    Bimpri.setPreferredSize(new Dimension(24, 24));
    Bimpri.setMaximumSize(new Dimension(24, 24));
    Bimpri.setMinimumSize(new Dimension(24, 24));
    Bimpri.setToolTipText("Imprimir Pagos Realizados");

    jtPag=confGridPago();
    jtApu=confGridApunte();
    jtPag.setMaximumSize(new Dimension(730, 157));
    jtPag.setMinimumSize(new Dimension(10, 70));
    jtPag.setPreferredSize(new Dimension(10, 70));
    jtPag.setBounds(new Rectangle(4, 101, 731, 191));

    iniciar(this);

    lbv_imppagE.setOpaque(true);
    cLabel6.setOpaque(true);
    Bacepfil.setBounds(new Rectangle(466, 43, 102, 19));
    cLabel9.setBounds(new Rectangle(297, 25, 60, 17));
    lbv_numfraE.setBounds(new Rectangle(353, 25, 53, 17));
    eje_numeE.setBounds(new Rectangle(465, 25, 53, 17));
    fefrfiE.setBounds(new Rectangle(206, 44, 72, 17));
    cLabel16.setBounds(new Rectangle(146, 44, 62, 17));
    fefrinE.setBounds(new Rectangle(69, 44, 72, 17));
    cLabel15.setBounds(new Rectangle(6, 44, 62, 17));
    eje_numeL.setBounds(new Rectangle(414, 25, 53, 17));
    fevtfinE.setBounds(new Rectangle(206, 25, 72, 17));
    cLabel11.setBounds(new Rectangle(147, 25, 62, 17));
    fevtiniE.setBounds(new Rectangle(70, 25, 72, 17));
    cLabel10.setBounds(new Rectangle(3, 25, 66, 17));
    lbv_origeE.setBounds(new Rectangle(9, 5, 111, 18));
    prv_codiE.setBounds(new Rectangle(134, 5, 389, 18));
    tra_codiE.setBounds(new Rectangle(134, 5, 389, 18));
    lbp_fecpagE.setBounds(new Rectangle(474, 4, 72, 17));
    lbp_tippagE.setBounds(new Rectangle(252, 4, 99, 17));
    cLabel3.setBounds(new Rectangle(226, 4, 33, 17));
    bat_codiE.setBounds(new Rectangle(38, 4, 184, 17));
    cLabel1.setBounds(new Rectangle(2, 4, 42, 17));
    Bcondpag.setBounds(new Rectangle(548, 6, 1, 1));
    cLabel2.setBounds(new Rectangle(424, 4, 48, 17));
    cLabel14.setText("Emp.");
    cLabel14.setBounds(new Rectangle(359, 4, 36, 17));
    emp_codiE.setBounds(new Rectangle(396, 4, 26, 17));
    jtPag.setBounds(new Rectangle(4, 100, 731, 192));
    Pconpag.setMaximumSize(new Dimension(746, 96));
    Pconpag.setMinimumSize(new Dimension(746, 96));
    Pconpag.setPreferredSize(new Dimension(746, 96));
    Pfiltro.setBounds(new Rectangle(161, 29, 576, 65));
    Bvecobu.setBounds(new Rectangle(138, 28, 22, 23));
    cLabel7.setBounds(new Rectangle(3, 29, 133, 14));
    Pcondef.setBounds(new Rectangle(111, 3, 620, 24));
    cLabel5.setBounds(new Rectangle(3, 3, 106, 14));
    Pdatos.setMaximumSize(new Dimension(609, 53));
    Pdatos.setMinimumSize(new Dimension(609, 53));
    Pdatos.setPreferredSize(new Dimension(609, 50));
    Baceptar.setMaximumSize(new Dimension(110, 24));
    Baceptar.setMinimumSize(new Dimension(110, 24));
    Baceptar.setPreferredSize(new Dimension(110, 24));
    Bcancelar.setMaximumSize(new Dimension(110, 24));
    Bcancelar.setMinimumSize(new Dimension(110, 24));
    Bcancelar.setPreferredSize(new Dimension(110, 24));
    Bdetalle.setBounds(new Rectangle(21, 26, 81, 21));
    Bdetalle.setToolTipText("Ir grid Detalles");
    Bdetalle.setMargin(new Insets(0, 0, 0, 0));
    Bdetalle.setText("Detalle (F2)");
    jtApu.setMaximumSize(new Dimension(600, 100));
    jtApu.setMinimumSize(new Dimension(600, 100));
    jtApu.setPreferredSize(new Dimension(600, 100));
    emp_codiE1.setBounds(new Rectangle(353, 45, 26, 17));
    cLabel17.setBounds(new Rectangle(316, 45, 36, 17));
    cLabel17.setText("Emp.");
    cLabel12.setText("N.P");
    numpagosE.setEnabled(false);
    lbv_imppagE.setEnabled(false);
    cLabel12.setBounds(new Rectangle(548, 4, 24, 17));
    lbp_numeE.setBounds(new Rectangle(567, 4, 50, 17));
    numpagosE.setBounds(new Rectangle(544, 3, 47, 17));
    Pfiltro.add(prv_codiE, null);
    Pfiltro.add(tra_codiE, null);
    Pfiltro.add(lbv_origeE, null);
    Pfiltro.add(cLabel10, null);
    Pfiltro.add(fevtfinE, null);
    Pfiltro.add(cLabel11, null);
    Pfiltro.add(fevtiniE, null);
    Pfiltro.add(fefrfiE, null);
    Pfiltro.add(cLabel15, null);
    Pfiltro.add(fefrinE, null);
    Pfiltro.add(cLabel16, null);
    Pfiltro.add(eje_numeL, null);
    Pfiltro.add(eje_numeE, null);
    Pfiltro.add(cLabel9, null);
    Pfiltro.add(lbv_numfraE, null);
    Pfiltro.add(emp_codiE1, null);
    Pfiltro.add(cLabel17, null);
    Pfiltro.add(Bacepfil, null);
    Pfiltro.add(cLabel13, null);
    Pfiltro.add(fcc_conpagE, null);
    prv_codiE.setEnabledNombre(true);
    tra_codiE.setEnabledNombre(true);
    Pprinc.setLayout(gridBagLayout1);
    lbv_imppagE.setBounds(new Rectangle(516, 29, 72, 17));
    cLabel6.setText("Imp.Pagado");
    cLabel6.setBounds(new Rectangle(449, 29, 68, 17));
    cLabel8.setText("Comentario");
    cLabel8.setBounds(new Rectangle(30, 2, 79, 18));
    lbv_comenS.setBounds(new Rectangle(110, 2, 337, 45));
    Pfiltro.setBorder(BorderFactory.createRaisedBevelBorder());
    Pfiltro.setLayout(null);
    Pdatos.setBorder(BorderFactory.createEtchedBorder());
    Pdatos.setLayout(null);
    cLabel10.setText("De Fec.Vto");
    cLabel11.setText("A Fec.Vto");
    Pconpag.setBorder(BorderFactory.createRaisedBevelBorder());
    Pconpag.setLayout(null);
    cLabel1.setText("Banco");
    bat_codiE.setAncTexto(30);
    cLabel2.setText("Fec.Pag");
    cLabel3.setText("Tipo");
    cLabel4.setText("Numero Pagos");
    cLabel4.setBounds(new Rectangle(450, 2, 90, 17));
    lbp_numeE.setBounds(new Rectangle(578, 4, 37, 17));
    cLabel5.setBackground(Color.red);
    cLabel5.setForeground(Color.white);
    cLabel5.setOpaque(true);
    cLabel5.setHorizontalAlignment(SwingConstants.CENTER);
    cLabel5.setHorizontalTextPosition(SwingConstants.CENTER);
    cLabel5.setText("Condiciones Pago");
    Pcondef.setBorder(BorderFactory.createRaisedBevelBorder());
    Pcondef.setLayout(null);
    cLabel9.setText("Num. Fra.");
    eje_numeL.setText("Ejercicio");
    cLabel15.setText("De Fec.Fra.");
    cLabel16.setText("A Fec Fra.");
    cLabel7.setText("Condiciones Busqueda");
    cLabel7.setHorizontalTextPosition(SwingConstants.CENTER);
    cLabel7.setHorizontalAlignment(SwingConstants.CENTER);
    cLabel7.setOpaque(true);
    cLabel7.setForeground(Color.white);
    cLabel7.setBackground(Color.red);
    Bvecobu.setToolTipText("Ver Condiciones Busqueda");

    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
    Pdatos.add(lbv_comenS, null);
    Pdatos.add(lbv_imppagE, null);
    Pdatos.add(cLabel6, null);
    Pdatos.add(cLabel4, null);
    Pdatos.add(lbv_numpagE, null);
    Pdatos.add(cLabel8, null);
    Pdatos.add(Bdetalle, null);
    Pdatos.add(numpagosE, null);
    lbv_comenE.setEnabled(false);
    lbv_comenS.getViewport().add(lbv_comenE, null);

    Pprinc.add(jtApu,            new GridBagConstraints(0, 2, 2, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(4, 0, 0, 0), 0, 0));
    Pprinc.add(jtPag,   new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    jtApu.getScrollPane().setHorizontalScrollBarPolicy(CScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    Pprinc.add(Bcancelar,     new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 165), 0, 0));
    Pprinc.add(Baceptar,   new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 165, 0, 0), 0, 0));
    Pprinc.add(Pconpag,     new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    Pcondef.add(bat_codiE, null);
    Pcondef.add(cLabel2, null);
    Pcondef.add(cLabel1, null);
    Pcondef.add(cLabel3, null);
    Pcondef.add(lbp_tippagE, null);
    Pcondef.add(emp_codiE, null);
    Pcondef.add(cLabel14, null);
    Pcondef.add(lbp_fecpagE, null);
    Pcondef.add(Bcondpag, null);
    Pcondef.add(cLabel12, null);
    Pcondef.add(lbp_numeE, null);
    Pconpag.add(cLabel7, null);
    Pconpag.add(Bvecobu, null);
    Pconpag.add(Pfiltro, null);
    Pprinc.add(Pdatos,   new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    Pconpag.add(cLabel5, null);
    Pconpag.add(Pcondef, null);
    tra_codiE.setVisible(false);
  }

  public void iniciarVentana() throws Exception
  {
    dtAux = new DatosTabla(dtAdd.getConexion());
    jtApu.setButton(KeyEvent.VK_F2,Bdetalle);
    jtPag.setButton(KeyEvent.VK_F2,Bdetalle);
    Pconpag.setButton(KeyEvent.VK_F2,Bcondpag);
    Pfiltro.setDefButton(Bacepfil);
    verCondBusqueda(false);
    Pprinc.setDefButton(Baceptar);
    lbv_origeE.setColumnaAlias("lbv_origcale");
    tra_codiE.setColumnaAlias("lbv_copvtr");
    prv_codiE.setColumnaAlias("lbv_copvtr");
    tra_codiE.getCampoNombre().setColumnaAlias("lbv_nombre");
    prv_codiE.getCampoNombre().setColumnaAlias("lbv_nombre");
    lbv_comenE.setColumnaAlias("lbv_comen");
    activar(false);

    bat_codiE.setColumnaAlias("bat_codi");
    lbp_tippagE.setColumnaAlias("lbp_tippag");
    emp_codiE.setColumnaAlias("emp_codi");
    lbp_fecpagE.setColumnaAlias("lbp_fecpag");
    lbp_numeE.setColumnaAlias("lbp_nume");
    verDatos();
    activarEventos();
  }
  void activarEventos()
  {
    lbv_origeE.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        setOrigen(lbv_origeE.getValor());
      }
    });
    Bvecobu.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        verCondBusqueda(!Pfiltro.isVisible());
      }
    });
    Bcondpag.addFocusListener(new FocusAdapter()
    {
      public void focusGained(FocusEvent e)
      {
        irGridPago();
      }
    });
    Bcondpag.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        irGridPago();
      }
    });

    jtPag.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent e)
      {
        if (nav.pulsado!=navegador.ADDNEW  && nav.pulsado!=navegador.EDIT)
          return;
        if (! jtPag.isEnabled())
          irGridPago();
      }
    });
    jtApu.addMouseListener(new MouseAdapter()
   {
     public void mouseClicked(MouseEvent e)
     {
       if (nav.pulsado!=navegador.ADDNEW  && nav.pulsado!=navegador.EDIT)
         return;
       if (! jtApu.isEnabled() && jtPag.isEnabled())
         irGridApunte(true);
     }
   });

   Bacepfil.addActionListener(new ActionListener()
   {
     public void actionPerformed(ActionEvent e)
     {
       Bacepfil_actionPerformed();
     }
   });
   Bdetalle.addActionListener(new ActionListener()
   {
     public void actionPerformed(ActionEvent e)
     {
       if (jtPag.isEnabled())
         irGridApunte(true);
       else
         irGridPago();
     }
   });
   Bimpri.addActionListener(new ActionListener()
   {
     public void actionPerformed(ActionEvent e)
     {
       Bimpri_actionPerformed();
     }
   });
  }

  String getSqlCondVto()
  {
    String s = "SELECT * FROM librovto WHERE emp_codi= "+emp_codiE.getValorInt()+
        " and lbv_pagado = 'N'";
    if (!lbv_origeE.getValor().equals("-"))
      s += " AND lbv_orige = '" + lbv_origeE.getValor() + "'";

    if (lbv_origeE.getValor().equals("C") && !prv_codiE.isNull())
      s += " AND lbv_copvtr = " + prv_codiE.getValorInt();
    if (lbv_origeE.getValor().equals("T") && !tra_codiE.isNull())
      s += " AND lbv_copvtr = " + tra_codiE.getValorInt();
    condFecha="";
    if (!fevtiniE.isNull())
      condFecha += " and lbv_fecvto >= to_date('" + fevtiniE.getText() + "','dd-MM-yyyy')";
    if (!fevtfinE.isNull())
      condFecha += " and lbv_fecvto <= to_date('" + fevtfinE.getText() + "','dd-MM-yyyy')";
    if (!fefrinE.isNull())
      condFecha += " and lbv_fecfra >= to_date('" + fefrinE.getText() + "','dd-MM-yyyy')";
    if (!fefrfiE.isNull())
      condFecha += " and lbv_fecfra <= to_date('" + fefrinE.getText() + "','dd-MM-yyyy')";
    s+=condFecha;
    if (lbv_numfraE.getValorInt()!=0)
      s+=" and lbv_numfra= "+lbv_numfraE.getValorInt();
    if (eje_numeE.getValorInt()!=0)
      s+=" and eje_nume= "+eje_numeE.getValorInt();
    s+=" ORDER BY emp_codi,lbv_fecvto,eje_nume,lbv_numfra,lbv_nume";
    sqlGrupVto = "SELECT * FROM librovto WHERE emp_codi= " + emp_codiE.getValorInt() +
        " and lbv_pagado = 'N'"+condFecha;
    return s;
  }

  /**
   * Llena los grids con los datos de las condiciones de busqueda
   *
   */
  void Bacepfil_actionPerformed()
  {
    new miThread("")
    {
      public void run()
      {
        pdpagreal.this.setEnabled(false);
        buscaVtosFiltro();
        pdpagreal.this.setEnabled(true);
      }
    };
  }
  void buscaVtosFiltro()
  {

    s = getSqlCondVto();
//    debug("Cond. Filtro: "+s);
    int numLin = -1;
    empGrid = 0;
    mensaje("A esperar ... estoy buscando datos");
//    jtPag.removeAllDatos();
//    jtApu.removeAllDatos();
    boolean insCabec=false;
    int nReg=0;
    try
    {
      if (!dtCon1.select(s))
      {
        mensaje("");
        mensajeErr("No encontrados Nuevos Vtos. para estas condiciones");
        return;
      }
      do
      {
        numLin++;
        if (!fcc_conpagE.getValor().equals("-"))
        {
          s = "SELECT fcc_conpag FROM v_facaco WHERE emp_codi = " + emp_codiE.getValorInt() +
              " and eje_nume = " + dtCon1.getInt("eje_nume") +
              " and fcc_nume = " + dtCon1.getInt("lbv_numfra");
          if (dtStat.select(s))
          {
            if (!dtStat.getString("fcc_conpag").equals(fcc_conpagE.getValor()))
              continue;
          }
        }
        insCabec=false;
        // Busco si la factura esta dentro de un grupo
        if (checkGrupo(dtCon1.getString("lbv_orige"), dtCon1.getInt("eje_nume"),
                       dtCon1.getInt("lbv_numfra"), condFecha, dtBloq))
        {
          // Busco los vtos sobre la factura del grupo
          do
          {
            if (existeLinea(dtCon1.getString("lbv_orige"), dtBloq.getInt("eje_nume"),
                      dtBloq.getInt("lbv_numfra"), dtBloq.getInt("lbv_nume"), 0, 0))
              continue;
            if (! insCabec)
            {
              numLin = insertaCabPago(numLin);
              nReg++;
              insCabec=true;
            }
            insertaDetPago(lbp_numeE.getValorInt(), numLin, 0,
                           dtCon1.getString("lbv_orige"), dtCon1.getInt("eje_nume"),
                           dtBloq.getInt("lbv_numfra"), dtBloq.getInt("lbv_nume"),
                           dtBloq.getDouble("lbv_impvto") - dtBloq.getDouble("lbv_imppag"), true);
          }  while (dtBloq.next());
          if (existeLinea(dtCon1.getString("lbv_orige"), dtCon1.getInt("eje_nume"),
                  dtCon1.getInt("lbv_numfra"), dtCon1.getInt("lbv_nume"), 0, 0))
             continue;

          if (! insCabec)
          {
            numLin = insertaCabPago(numLin);
            nReg++;
          }
          insertaDetPago(lbp_numeE.getValorInt(), numLin, 0,
                        dtCon1.getString("lbv_orige"), dtCon1.getInt("eje_nume"),
                        dtCon1.getInt("lbv_numfra"), dtCon1.getInt("lbv_nume"),
                        dtCon1.getDouble("lbv_impvto") - dtCon1.getDouble("lbv_imppag"), true);
        }
        else
        {
          if (existeLinea(dtCon1.getString("lbv_orige"), dtCon1.getInt("eje_nume"),
                      dtCon1.getInt("lbv_numfra"), dtCon1.getInt("lbv_nume"), 0, 0))
                continue;
          if (dtCon1.getDouble("lbv_impvto") - dtCon1.getDouble("lbv_imppag")<0)
                 continue; // NO inserto los pagos en Negativo.
          numLin = insertaCabPago(numLin);
          nReg++;
          insertaDetPago(lbp_numeE.getValorInt(), numLin, 0,
                         dtCon1.getString("lbv_orige"), dtCon1.getInt("eje_nume"),
                         dtCon1.getInt("lbv_numfra"), dtCon1.getInt("lbv_nume"),
                         dtCon1.getDouble("lbv_impvto") - dtCon1.getDouble("lbv_imppag"), true);
        }
      }   while (dtCon1.next());
      ctUp.commit();
      this.setEnabled(true);
      empGrid = emp_codiE.getValorInt();
      jtApu.setEnabled(false);
      if (nReg==0)
      {
        mensaje("");
        mensajeErr("No encontrados Nuevos Vtos. para estas condiciones");
        return;
      }
        SwingUtilities.invokeLater(new Thread()
        {
          public void run()
          {
            try
            {
              boolean enab = jtPag.isEnabled();
              jtPag.setEnabled(false);
              jtPag.removeAllDatos();
              verDatos1();
              if (!enab)
                irGridPago();
              else
              {
                jtPag.setEnabled(true);
                jtPag.requestFocusInicio();
              }
              afterCambiaLineaPago();
            }
            catch (Exception k)
            {
              Error("Error al Buscar Vtos", k);
            }
          }
        });


      mensaje("");
      mensajeErr("Datos de pagos a realizar ... encontrados");
    } catch (Exception k)
    {
      Error("Error al Buscar Vtos", k);
    }
  }
  boolean checkGrupo(String origen, int ejeNume,int fccNume,String condWhere,DatosTabla dt) throws SQLException
  {
    s = "SELECT eje_nume2 as eje_nume,fcc_nume2 as lbv_numfra, " +
        " l.lbv_nume,l.lbv_fecvto,l.lbv_impvto,l.lbv_imppag " +
        " FROM grufaco as g, librovto as l " +
        " WHERE eje_nume1 = " + ejeNume +
        " and g.emp_codi1 = " + emp_codiE.getValorInt() +
        " and g.fcc_nume1 = " + fccNume +
        " and g.gfc_orige = '" + origen + "'" +
        " and g.eje_nume2= l.eje_nume " +
        " and g.fcc_nume2= l.lbv_numfra " +
        " and l.emp_codi = " + emp_codiE.getValorInt() +
        " "+condWhere+
        " UNION  ALL  " +
        " SELECT eje_nume1  as eje_nume,fcc_nume1 as lbv_numfra, " +
        " l.lbv_nume,l.lbv_fecvto,l.lbv_impvto,l.lbv_imppag  " +
        " FROM grufaco as g , librovto as l " +
        " WHERE eje_nume2 = " + ejeNume +
        " and g.emp_codi2 = " + emp_codiE.getValorInt() +
        " and g.fcc_nume2 = " + fccNume +
        " and g.gfc_orige = '" + origen + "'" +
        " and g.eje_nume1= l.eje_nume " +
        " and g.fcc_nume1= l.lbv_numfra " +
        " and l.emp_codi = " + emp_codiE.getValorInt() +
        " "+condWhere+
        " ORDER BY 1,2 ";
//    debug ("s: "+s);
    return dt.select(s);
  }
/*  void verDatos(DatosTabla dt) throws SQLException
  {
    Vector v = new Vector();
    v.add(dt.getString("lbv_orige"));
    v.add(dt.getString("eje_nume"));
    v.add(dt.getString("lbv_numfra"));
    v.add(dt.getString("lbv_nume"));
    v.add(dt.getString("lbv_nombre"));
    v.add(dt.getString("lbv_impvto"));
    v.add(dt.getFecha("lbv_fecvto", "dd-MM-yy"));
    v.add("" + (dt.getDouble("lbv_impvto") - dtCon1.getDouble("lbv_imppag")));
    v.add("0");
    v.add("0");
    v.add("0");
    v.add("0");
    v.add("");
    v.add("");
    v.add(new Boolean(true));
    v.add("");

  }
*/
  /**
   *
   * Comprueba si un vto. ya existe en las tablas
   * Esto se hace para que no se pueda meter varias veces un pago
   * en un mismo numero de pago.
   *
   * @param lbvOrige String
   * @param ejeNume int
   * @param lbvNumfra int
   * @param lbvNume int
   * @param lbpNumlin int
   * @param lipNulide int
   * @throws SQLException
   * @return boolean
   */
  boolean existeLinea(String lbvOrige, int ejeNume, int lbvNumfra, int lbvNume,
                      int lbpNumlin,int lipNulide ) throws SQLException
  {
    s = "SELECT * FROM libpagdet WHERE emp_codi = " + emp_codiE.getValorInt() +
        " AND lbp_nume = " + lbp_numeE.getValorInt() +
        " and lbv_orige = '" + lbvOrige + "'" +
        " and eje_nume = " + ejeNume +
        " AND lbv_numfra = " + lbvNumfra +
        " and lbv_nume = " + lbvNume+
        (lbpNumlin!=0?" and lbp_numlin != "+lbpNumlin:"")+
        (lipNulide!=0?" and lip_nulide != "+lipNulide:"");
    return dtStat.select(s);

  }
  void irGridApunte(boolean check)
  {
    if (check)
    {
      jtPag.procesaAllFoco();
      if (lbv_origeG.isNull(true))
      {
        SwingUtilities.invokeLater(new Thread()
        {
          public void run()
          {
            jtPag.requestFocus();
            jtPag.requestFocus(jtPag.getSelectedRow(), 0);
          }
        });
        return;
      }
      if (lbv_numfraG.isEnabled() && lbv_numfraG.getValorInt()>0 && jtApu.isVacio())
      { // Paso la linea al grid de Detalles
        prvApunte=prvCodi;
        jtApu.removeAllDatos();
        Vector v=new Vector();
        v.addElement(eje_numeG.getText());
        v.addElement(lbv_numfraG.getText());
        v.addElement(lbv_numeG.getText());
        v.addElement(lbv_imppagG.getText());
        v.addElement(new Boolean(lbp_totpagP.isSelected()));
        v.addElement(jtPag.getValString(5));
        v.addElement(jtPag.getValString(6));
        v.addElement("0");
        jtApu.addLinea(v);
      }
      else
        jtApu.ponValores(0);
    }
    jtPag.setEnabled(false);
    jtApu.setEnabled(true);
    Baceptar.setEnabled(false);
    jtApu.requestFocusInicio();
    jtApu.ponValores(0);
  }

  /**
   *
   * sumando todas las lineas de jtApu (Apuntes) y reflejandolo en jtPag.
   *
   * @return int Devuelve la primera linea del grid con datos validos.
   */
  int actualizaAcuPago()
  {
    int nRow=jtApu.getRowCount();
    double impPago=0;
    int numApu=0;
    int firstLinea=-1;
    for (int n=0;n<nRow;n++)
    {
      if (jtApu.getValorInt(n,1)==0)
        continue; // Sin N�mero de FRA.
      if (firstLinea==-1)
        firstLinea=n;
      impPago+=jtApu.getValorDec(n,3);
      numApu++;
    }
    jtPag.setValor(""+impPago,7);
    jtPag.setValor(""+numApu,3);
    return firstLinea;
  }
  /**
   * Controla si se puede  cambiar la Linea del grid de Apuntes
   * @param row int
   * @return int
   */
  int cambiaLineaApu(int row)
  {
    if (!jtApu.hasCambio())
      return -1;
    try
    {

      if (lbv_numfraP.getValorInt() == 0)
      {
        if (jtApu.getValorInt(7) != 0)
        {
          borrarApunte(lbp_numeE.getValorInt(), jtPag.getValorInt(15), jtApu.getValorInt(7));
          jtApu.setValor("0", 7);
          dtAdd.commit();
        }
        return -1;
      }
      if (eje_numeP.hasCambio() || lbv_numfraP.hasCambio() || lbv_numeP.hasCambio() )
      {
        if (existeLinea(lbv_origeG.getText(), eje_numeP.getValorInt(),
                        lbv_numfraP.getValorInt(), lbv_numeP.getValorInt(),
                        jtPag.getValorInt(15), jtApu.getValorInt(7)))
        {
          mensajeErr("Ya existe un apunte sobre este vencimiento en esta carga");
          return 2;
        }
          if (!buscaDatosVto(row, 2,
                             lbv_origeG.getText(),
                             emp_codiE.getValorInt(),
                             eje_numeP.getValorInt(),
                             lbv_numfraP.getValorInt(),
                             lbv_numeP.getValorInt()))
          {
            mensajeErr("Registro de VTO ... No valido");
            return 2;
          }
        if (dtStat.getString("lbv_pagado").equals("S"))
        {
          mensajeErr("Registro de Vencimiento esta marcado como YA pagado");
          return 2;
        }
        if (prvApunte != 0 && prvCodi != prvApunte)
        {
          mensajeErr("Proveedor de este PAGO es diferente al inicial");
          return 0;
        }
      }
      if (jtPag.getValorInt(15) == 0) // Si no tiene numero de Linea...
      {
        jtPag.setValor("" + insertaCabPago(0), 15);
        prvApunte=prvCodi;
      }
      if (jtApu.getValorInt(7) != 0)
        borrarApunte(lbp_numeE.getValorInt(), jtPag.getValorInt(15), jtApu.getValorInt(7));

      jtApu.setValor("" + insertaDetPago(lbp_numeE.getValorInt(), jtPag.getValorInt(15),
                                    jtApu.getValorInt(7),
                                    lbv_origeG.getText(), eje_numeP.getValorInt(),
                                    lbv_numfraP.getValorInt(),
                                    lbv_numeP.getValorInt(),
                                    lbv_imppagP.getValorDec(), lbp_totpagP.isSelected()), 7);
      ctUp.commit();
      mensajeErr("Linea de Apunte ... Guardada", false);
    }
    catch (Exception k)
    {
      Error("Error al Cambiar linea apunte", k);
    }
    return -1;
  }

  void actCamposEnabled(boolean enab)
  {
    lbv_origeG.setEditable(enab);
    eje_numeG.setEditable(enab);
    lbv_numfraG.setEditable(enab);
    lbv_numeG.setEditable(enab);
    lbv_imppagG.setEditable(enab);
    lbp_totpagG.setEnabled(enab);
  }

  boolean checkCabe() throws SQLException
  {
    if (!pdempresa.checkEmpresa(dtStat, emp_codiE.getValorInt()))
    {
      mensajeErr("Empresa NO VALIDA");
      emp_codiE.requestFocus();
      return false;
    }
    if (lbp_fecpagE.isNull() || lbp_fecpagE.getError())
    {
      mensajeErr("Fecha de Pago NO es valida");
      lbp_fecpagE.requestFocus();
      return false;
    }
    return true;
  }
  void irGridPago()
  {
    try
    {
      if (jtApu.isEnabled())
      {
        jtApu.procesaAllFoco();
        if (cambiaLineaApu(jtApu.getSelectedRow()) != -1)
          return;
        int nFirstLinea=actualizaAcuPago();
        actCamposEnabled(jtPag.getValorInt(3) <= 1);
        if (jtPag.getValorInt(3)==1)
        {
          jtPag.setValor(jtApu.getValString(nFirstLinea,0),1);
          jtPag.setValor(jtApu.getValString(nFirstLinea,1),2);
          jtPag.setValor(jtApu.getValString(nFirstLinea,2),3);
          jtPag.setValor(jtApu.getValString(nFirstLinea,3),5);
          jtPag.ponValores(jtPag.getSelectedRow());
          jtPag.setValor(new Boolean(jtApu.getValBoolean(nFirstLinea,4)),14);
          buscaDatosVto(jtPag.getSelectedRow(), 1,
                           lbv_origeG.getText(),
                           emp_codiE.getValorInt(),
                           eje_numeG.getValorInt(),
                           lbv_numfraG.getValorInt(),
                           lbv_numeG.getValorInt());
        }
        if (nFirstLinea==-1)
        {
          jtPag.setValor("0",1);
          jtPag.setValor("0",2);
          jtPag.setValor("0",3);
          jtPag.setValor("0",5);
        }
        jtApu.setEnabled(false);
        jtPag.setEnabled(true);
//        if (nav.pulsado==navegador.ADDNEW)
          Baceptar.setEnabled(true);
        jtPag.requestFocus(jtPag.getSelectedRow(),0);
        jtPag.resetCambio();
        return;
      }
      else
      {
        if (!checkCabe())
          return;

//      if (! jtPag.isVacio() && emp_codiE.getValorInt()!=empGrid)
//      {
//        mensajeErr("Datos del grid son de Empresa: "+empGrid);
//        emp_codiE.requestFocus();
//        return;
//      }
        char tippag = lbp_tippagE.getValor().charAt(0);
        ban_codiG.setEnabled(tippag == 'T');
        lbp_baoficG.setEnabled(tippag == 'T');
        lbp_badicoG.setEnabled(tippag == 'T');
        lbp_bacuenG.setEnabled(tippag == 'T');
        lbp_numtalG.setEnabled(tippag == 'C' || tippag == 'P');
        Bdetalle.setEnabled(true);
      }
      jtPag.setEnabled(true);
      Pcondef.setEnabled(false);
      jtPag.requestFocusInicio();
    }
    catch (Exception k)
    {
      Error("Error al Ir al grid de Pagos", k);
    }
  }

  void verCondBusqueda(boolean visible)
  {
    Pfiltro.setVisible(visible);
    if (visible)
    {
      Pconpag.setMaximumSize(new Dimension(746, 96));
      Pconpag.setMinimumSize(new Dimension(746,96));
      Pconpag.setPreferredSize(new Dimension(746, 96));
    }
    else
    {
      Pconpag.setMaximumSize(new Dimension(746, 58));
      Pconpag.setMinimumSize(new Dimension(746, 58));
      Pconpag.setPreferredSize(new Dimension(746, 58 ));
    }
  }
  public void afterConecta() throws SQLException, java.text.ParseException
  {
    bat_codiE.setFormato(Types.DECIMAL, "##9");
     s = "SELECT bat_codi,bat_nomb FROM bancteso order by bat_nomb";
     dtStat.select(s);
     bat_codiE.addDatos(dtStat);

     lbp_tippagE.addItem("Extranjero","E");
     lbp_tippagE.addItem("Pagare","P");
     lbp_tippagE.addItem("Cheque","C");
     lbp_tippagE.addItem("Recibo","R");
     lbp_tippagE.addItem("Transferencia","T");

     lbv_origeE.addItem("---------", "-");
     lbv_origeE.addItem("Proveedor", "C");
     lbv_origeE.addItem("Transport", "T");

     lbv_pagadoE.addItem("No", "N");
     lbv_pagadoE.addItem("Si", "S");

     fcc_conpagE.addItem("--","-");
     fcc_conpagE.addItem("Si","-");
     fcc_conpagE.addItem("No","N");

     prv_codiE.setAceptaNulo(false);
     tra_codiE.setAceptaNulo(false);

     s = "SELECT emp_codi,emp_nomb FROM v_empresa ORDER BY emp_codi";
     dtStat.select(s);


     prv_codiE.iniciar(dtStat, this, vl, EU);
     tra_codiE.iniciar(dtStat, this, vl, EU);


  }
  void setOrigen(String origen)
  {
    lbv_origeE.setValor(origen);
    prv_codiE.setVisible(origen.equals("C"));
    tra_codiE.setVisible(!origen.equals("C"));
  }
  void verDatos()
  {
    try
    {
      jtPag.removeAllDatos();
      jtApu.removeAllDatos();
      if (dtCons.getNOREG())
        return;
      emp_codiE.setValorInt(dtCons.getInt("emp_codi"));
      lbp_numeE.setValorDec(dtCons.getInt("lbp_nume"));

      verDatos1();
    }
    catch (Exception k)
    {
      Error("Error al Mostrar Datos", k);
    }

  }
  void verDatos1() throws SQLException
  {
    if (! selectRegPant(dtAux,false))
    {
      mensajeErr("Datos del Pago  .. NO ENCONTRADO");
      Pdatos.resetTexto();
      emp_codiE.setValorInt(dtCons.getInt("emp_codi"));
      lbp_numeE.setValorDec(dtCons.getInt("lbp_nume"));
      return;
    }
//      setOrigen(dtCons.getString("lbv_orige"));
    lbp_fecpagE.setText(dtAux.getFecha("lbp_fecpag","dd-MM-yyyy"));
    bat_codiE.setValorInt(dtAux.getInt("bat_codi"));
    lbp_tippagE.setValor(dtAux.getString("lbp_tippag"));
    swVerDet=false;
    do
    {
      if (getDatosPago(dtAux.getInt("lbp_nume"),dtAux.getInt("lbp_numlin"),null,false)==null)
        continue;

      Vector v=new Vector();
      v.addElement(dtCon1.getString("lbv_orige"));
      v.addElement(dtCon1.getString("eje_nume"));
      if (nLineasDet>1)
        v.addElement("0");
      else
        v.addElement(dtCon1.getString("lbv_numfra"));
      v.addElement(dtCon1.getString("lbv_nume"));
      v.addElement(prvNombre);
      if (nLineasDet > 1)
      {
        v.addElement("0");
        v.addElement("");
      }
      else
      {
        v.addElement(dtCon1.getString("lbv_impvto"));
        v.addElement(dtCon1.getFecha("lbv_fecvto", "dd-MM-yy"));
      }
      v.addElement(""+impPagadoDet);
      v.addElement(dtAux.getString("ban_codi"));
      v.addElement(dtAux.getString("lbp_baofic"));
      v.addElement(dtAux.getString("lbp_badico"));
      v.addElement(dtAux.getString("lbp_bacuen"));
      v.addElement(dtAux.getString("lbp_numtal"));
      v.addElement(dtAux.getString("lbp_facprv"));
      v.addElement(new Boolean(dtCon1.getString("lbv_pagado").equals("S")));
      v.addElement(dtAux.getString("lbp_numlin"));
      jtPag.addLinea(v);
    } while (dtAux.next());
    swVerDet=true;
    jtPag.requestFocusInicio();
    verDatosPago(lbp_numeE.getValorInt(), jtPag.getValorInt(0, 15),
                     jtPag.getValString(0,0));
    actAcumPagos();
  }

  private boolean selectRegPant(DatosTabla dt,boolean block) throws SQLException
  {
    s = "select * from libpagcab WHERE emp_codi = "+emp_codiE.getValorInt()+
        " and lbp_nume = "+lbp_numeE.getValorInt()+
        " order by lbp_numlin ";

    return dt.select(s,block);
  }

  public void PADPrimero()  {verDatos();}

  public void PADAnterior()  {verDatos();}

  public void PADSiguiente()  {verDatos();}

  public void PADUltimo()  {verDatos();}

  public void PADQuery()
  {
    Pprinc.setQuery(true);
    Pprinc.resetTexto();
    mensaje("Establezca FILTRO de Consulta");
    lbp_numeE.setEnabled(true);
    activar(true);
    Bvecobu.setEnabled(false);
    jtPag.setEnabled(false);
    jtApu.setEnabled(false);
    Pfiltro.setEnabled(false);
    bat_codiE.requestFocus();
  }


  public void ej_query1()
  {
    Component c = Pfiltro.getErrorConf();
    if (c != null)
    {
      mensajeErr("FILTRO DE CONSULTA NO VALIDO");
      c.requestFocus();
      return;
    }
    try
    {

      Vector v = new Vector();
      v.add(bat_codiE.getStrQuery());
      v.add(lbp_tippagE.getStrQuery());
      v.add(emp_codiE.getStrQuery());
      v.add(lbp_fecpagE.getStrQuery());
      v.add(lbp_numeE.getStrQuery());
      Pdatos.getVectorQuery(v);
      s = "SELECT * FROM libpagcab ";
      s = creaWhere(s, v, true);
      s += getOrderQuery();
//      debug("s: "+s);
      this.setEnabled(false);
      mensaje("Espere, por favor ... buscando datos");
      Pprinc.setQuery(false);
      if (!dtCon1.select(s))
      {
        msgBox("No encontrados datos a visualizar con el filtro introducido");
        mensaje("");
        rgSelect();
        verDatos();
        activaTodo();
        nav.pulsado=navegador.NINGUNO;
        this.setEnabled(true);
        return;
      }
      strSql = s;
      activaTodo();

      this.setEnabled(true);
      rgSelect();
      verDatos();
      mensaje("");
      nav.pulsado=navegador.NINGUNO;
      mensajeErr("FILTRO CONSULTA ... ESTABLECIDO");
    }
    catch (Exception k)
    {
      Error("Error al buscar datos", k);
    }
    nav.pulsado = navegador.NINGUNO;
  }

  public void canc_query()
  {
    Pprinc.setQuery(false);
    activaTodo();
    verDatos();
    mensaje("");
    nav.pulsado=navegador.NINGUNO;
    mensajeErr("Introducion FILTRO CONSULTA ... CANCELADO");
  }

  public void PADEdit()
  {
    try
    {
      if (!setBloqueo(dtAdd, "libpagcab",
                      emp_codiE.getValorInt() +
                     "|" + lbp_numeE.getValorInt()))
      {
        activaTodo();
        msgBox(msgBloqueo);
        nav.pulsado = navegador.NINGUNO;
        return;
      }
    }
    catch (Exception k)
    {
      Error("Error al bloquear Registro", k);
    }

    activar(true);
    mensaje("Modificando registro Actual ");
    Bcancelar.setEnabled(false);
    Pcondef.setEnabled(false);
    jtApu.setEnabled(false);
    irGridPago();
  }

  public void ej_edit1()
  {
    try
    {

      resetBloqueo(dtAdd, "libpagcab",
                      emp_codiE.getValorInt() +
                     "|" + lbp_numeE.getValorInt(),false);

      ctUp.commit();
      mensajeErr("REGISTRO ... MODIFICADO");
      mensaje("");
      activaTodo();
      nav.pulsado=navegador.NINGUNO;
    }
    catch (Exception k)
    {
      Error("Error al EDITAR registro", k);
    }
  }

  public void canc_edit()
  {
    try {
      resetBloqueo(dtAdd, "libpagcab",
                      emp_codiE.getValorInt() +
                     "|" + lbp_numeE.getValorInt());
    } catch (Exception k)
    {
      Error("Error al quitar Bloqueo",k);
    }
    activaTodo();
    verDatos();
    mensaje("");
    nav.pulsado=navegador.NINGUNO;
    mensajeErr("Edicion de registro ... CANCELADO");
  }

  public void PADAddNew()
  {
//    Formatear.verAncGrid(jtPag);
    jtApu.removeAllDatos();
    jtPag.removeAllDatos();
    activar(true);
    jtApu.setEnabled(false);
    jtPag.setEnabled(false);
    Bdetalle.setEnabled(false);
    mensaje("Insertando NUEVOS registros");
    Pprinc.resetTexto();
    verCondBusqueda(true);
    emp_codiE.setValorInt(EU.em_cod);
    empGrid=0;
    prvApunte=0;
    prvNombre="";
    lbp_numeE.setEnabled(false);
    lbp_fecpagE.setText(Fecha.getFechaSys("dd-MM-yyyy"));
    fevtfinE.setText(Fecha.getFechaSys("dd-MM-yyyy"));
    actCamposEnabled(true);
    lbv_origeE.requestFocus();
    prv_codiE.isEnabledNombre();
  }

  public boolean checkEdit()
  {
    return checkAddNew();
  }
  public boolean checkAddNew()
  {
   try
   {
    if (!checkCabe())
        return false;
      jtPag.procesaAllFoco();
      int nCol=cambiaLineaPago(jtPag.getSelectedRow(),true);
      if (nCol >= 0)
      {
        jtPag.requestFocus(jtPag.getSelectedRow(),nCol);
        return false;
      }
      actAcumPagos();
      if (numpagosE.getValorInt()==0)
      {
        mensajeErr("Introduzca un pago al menos");
        jtPag.requestFocusInicio();
        return false;
      }
      // Chequeo que no haya bancos no validos, o pagos sin numero de Talon
      int nRows=jtPag.getRowCount();
      char checkBanc=lbp_tippagE.getValor().charAt(0);
      for (int n=0;n<nRows;n++)
      {
        if (jtPag.getValorInt(n,3)==0)
          continue;
        nCol=checkBanco(checkBanc,jtPag.getValorInt(n,8),jtPag.getValorInt(n,9),
                        jtPag.getValorDec(n,11),jtPag.getValString(n,12));
        if (nCol>=0)
        {
          jtPag.requestFocusLater(n,nCol);
          return false;
        }
        if (jtPag.getValorDec(n,7) <= 0)
        {
          mensajeErr("Importe a Pagar NO puede ser inferior o igual a cero");
          jtPag.requestFocusLater(n,0);
          return false;
        }
      }
      // Busco en los pagos (Ya insertados) si hay alguna factura con conforme de pago a NO.
      String msgAviso="";
      s = "SELECT c.*,p.prv_nomb FROM v_facaco as c,libpagdet as d, v_proveedo as p"+
          " where d.emp_codi = " + emp_codiE.getValorInt() +
          " AND d.lbp_nume = " + lbp_numeE.getValorInt()+
          " and d.lbv_orige = 'C' "+
          " AND D.lbv_numfra = c.fcc_nume "+
          " and d.eje_nume = c.eje_nume "+
          " and c.emp_codi = "+emp_codiE.getValorInt()+
          " and c.fcc_conpag = 'N' "+
          " and c.prv_codi = p.prv_codi ";
//      debug("s"+s);
      if (dtCon1.select(s))
      {
        do
        {
          msgAviso+=" Factura: "+dtCon1.getInt("eje_nume")+"/"+dtCon1.getInt("fcc_nume")+" DE Proveedor: "+
              dtCon1.getString("prv_nomb")+" NO Conforme en Pago\n";
        } while (dtCon1.next());
      }
      if (! msgAviso.equals(""))
      {
        if (mensajes.mensajeYesNo(msgAviso + "\n Continuar ?", this) != mensajes.YES)
          return false;
      }
      msgAviso="";
      s = "SELECT l.eje_nume, l.fcc_nume FROM v_falico as l, libpagdet as d, v_regstock as r " +
          " where d.emp_codi = " + emp_codiE.getValorInt() +
          " AND d.lbp_nume = " + lbp_numeE.getValorInt() +
          " and d.lbv_orige = 'C' " +
          " and l.emp_codi = d.emp_codi "+
          " and l.eje_nume = d.eje_nume "+
          " and l.fcc_nume = d.lbv_numfra "+
          " and l.emp_codi =  r.emp_codi "+ // Union de Alb. Con mvto. Regularizacion
          " AND l.acc_ano = r.eje_nume " +
          " and l.acc_nume = r.pro_nupar "+
          " and l.acc_serie = r.pro_serie "+
          " and r.rgs_recprv = "+gnu.chu.anjelica.almacen.paregalm.ESTPEND+
          " group by l.eje_nume,l.fcc_nume ";

//      debug("s"+s);
      if (dtCon1.select(s))
      {
        do
        {
          msgAviso += " Factura: " + dtCon1.getInt("eje_nume") + "/" + dtCon1.getInt("fcc_nume")
              +" tiene albaranes con Vertederos Pendientes\n";
        }
        while (dtCon1.next());
      }
      if (!msgAviso.equals(""))
      {
        if (mensajes.mensajeYesNo(msgAviso + "\n Continuar ?", this) != mensajes.YES)
          return false;
      }

      return true;
    } catch (Exception k)
    {
      Error("ERROR EN checkAddNew",k);
    }
    return false;
  }

  public void ej_addnew1()
  {
    try
    {
      resetBloqueo(dtAdd, "libpagcab",
                    emp_codiE.getValorInt() +
                   "|" + lbp_numeE.getValorInt(),false);
      ctUp.commit();
      mensajeErr("REGISTRO ... INSERTADO");
      mensaje("");
      if (dtCons.getNOREG())
      {
        rgSelect();
        verDatos();
      }
      nav.pulsado=navegador.NINGUNO;
      activaTodo();
    }
    catch (Exception k)
    {
      Error("Error al INSERTAR registro", k);
    }
  }

//  private void actDatos(DatosTabla dt) throws SQLException, ParseException
//  {
//
//    dt.setDato("lbv_pagado", lbv_pagadoE.getValor());
//    dt.setDato("lbv_imppag", lbv_imppagE.getValorDec());
//    dt.setDato("lbv_comen", Formatear.strCorta(lbv_comenE.getText(),255));
//  }

  public void canc_addnew()
  {
    try
    {
      if (lbp_numeE.getValorInt()!=0)
      {
        borrarRegPagos();
        resetBloqueo(dtAdd, "libpagcab",
                    emp_codiE.getValorInt() +
                   "|" + lbp_numeE.getValorInt(),false);
        ctUp.commit();
      }
    } catch (Exception k)
    {
      Error("Error al cancelar Apuntes de cobros",k);
    }
    activaTodo();
    verDatos();
    mensaje("");
    mensajeErr("Insercion NUEVO registro ... CANCELADO");
    nav.pulsado=navegador.NINGUNO;
  }

  void borrarRegPagos() throws SQLException
  {
    if (! selectRegPant(dtAux,true))
      return;
    do
    {
      borrarApunte(lbp_numeE.getValorInt(),dtAux.getInt("lbp_numlin"),-1);
    } while (dtAux.next());
    dtAux.delete(stUp);
  }
  public void PADDelete()
  {
    try
    {
      if (!setBloqueo(dtAdd, "libpagcab",
                       emp_codiE.getValorInt() +
                      "|" + lbp_numeE.getValorInt()))
      {
        activaTodo();
        msgBox(msgBloqueo);
        nav.pulsado = navegador.NINGUNO;
        return;
      }
    }
    catch (Exception k)
    {
      Error("Error al bloquear Registro", k);
    }
    Baceptar.setEnabled(true);
    Bcancelar.setEnabled(true);
    mensaje("BORRANDO registro Actual ");
    Bcancelar.requestFocus();
  }


  public void ej_delete1()
  {
  try
  {
    borrarRegPagos();
    resetBloqueo(dtAdd,  "libpagcab",
                       emp_codiE.getValorInt() +
                      "|" + lbp_numeE.getValorInt(),false);

    ctUp.commit();
    mensaje("");
    rgSelect();
    verDatos();
    activaTodo();
    mensajeErr("REGISTRO ... BORRADO");
    nav.pulsado=navegador.NINGUNO;
  }
  catch (Exception k)
  {
    Error("Error al BORRAR registro", k);
  }

  }

  public void canc_delete()
  {
    try
    {
      resetBloqueo(dtAdd,  "libpagcab",
                       emp_codiE.getValorInt() +
                      "|" + lbp_numeE.getValorInt());
    }
    catch (Exception k)
    {
      Error("Error al quitar Bloqueo", k);
    }
    activaTodo();
    mensaje("");
    nav.pulsado=navegador.NINGUNO;
    mensajeErr("BORRADO de registro ... CANCELADO");
  }



  public void activar(boolean b)
  {
    if (!b)
    {
      jtPag.setEnabled(false);
      jtApu.setEnabled(false);
    }
    Bvecobu.setEnabled(b);
    Pfiltro.setEnabled(b);
    prv_codiE.setEnabled(b);
    tra_codiE.setEnabled(b);
    jtPag.setEnabled(b);
    jtApu.setEnabled(b);
    Pcondef.setEnabled(b);
    Pdatos.setEnabled(b);
    Pconpag.setEnabled(b);
    Baceptar.setEnabled(b);
    Bcancelar.setEnabled(b);
    if (!b)
      verCondBusqueda(false);
  }
  String getOrderQuery()
  {
    return " ORDER BY emp_codi,lbp_nume ";
  }
  boolean buscaDatosVto(int row,int ponDatos,String lbvOrige,
                        int empCodi,int ejeNume,int numFra,int nVto) throws SQLException
  {
    String s;

    if (nVto==0)
    {
      s = "select * from librovto as l WHERE lbv_orige = '" + lbvOrige + "'" +
          " and emp_codi = " + empCodi +
          " and eje_nume = " + ejeNume +
          " and lbv_numfra = " + numFra +
          " and lbv_pagado = 'N' " +
          " ORDER BY lbv_fecvto ";
       if (dtStat.select(s))
       {
         nVto = dtStat.getInt("lbv_nume");
         if (ponDatos!=0)
         {
           if (ponDatos == 1)
           {
             jtPag.setValor("" + nVto, row, 3);
             lbv_numeG.setValorDec(nVto);
           }
           else
           {
             jtApu.setValor("" + nVto, row, 3);
             lbv_numeP.setValorDec(nVto);
           }
         }
       }
    }
    s = "select * from librovto as l WHERE lbv_orige = '" + lbvOrige + "'" +
       " and emp_codi = " + empCodi +
       " and eje_nume = " + ejeNume +
       " and lbv_numfra = " + numFra+
       " and lbv_nume= "+nVto;
   boolean res=dtStat.select(s);
   if (res)
     prvCodi=dtStat.getInt("lbv_copvtr");
   if (ponDatos==0)
     return res;
   if (! res)
   {
     if (ponDatos==1)
     {
       jtPag.setValor("**NO ENCONTRADO**", row, 4);
       jtPag.setValor("0", row, 5);
       jtPag.setValor("", row, 6);
     }
     else
     {
       jtApu.setValor("0", row, 5);
       jtApu.setValor("", row, 6);
     }
     lbv_comenE.setText("");
   }
   else
   {
     if (ponDatos == 1)
     {
       jtPag.setValor(dtStat.getString("lbv_impvto"), row, 5);
       jtPag.setValor(dtStat.getFecha("lbv_fecvto", "dd-MM-yy"), row, 6);
       if (lbv_imppagG.getValorDec() == 0)
       {
         lbv_imppagG.setValorDec(dtStat.getDouble("lbv_impvto") - dtStat.getDouble("lbv_imppag"));
         jtPag.setValor("" + (dtStat.getDouble("lbv_impvto") - dtStat.getDouble("lbv_imppag")),
                        row, 7);
       }
     }
     else
     {
       jtApu.setValor(dtStat.getString("lbv_impvto"), row, 5);
       jtApu.setValor(dtStat.getFecha("lbv_fecvto", "dd-MM-yy"), row, 6);
       if (lbv_imppagP.getValorDec() == 0)
       {
         lbv_imppagP.setValorDec(dtStat.getDouble("lbv_impvto") - dtStat.getDouble("lbv_imppag"));
         jtApu.setValor("" + (dtStat.getDouble("lbv_impvto") - dtStat.getDouble("lbv_imppag")),
                        row, 3);
       }
     }
     lbv_comenE.setText(dtStat.getString("lbv_comen"));
     jtPag.setValor(dtStat.getString("lbv_nombre"), row, 4);

   }
   return res;

  }
  /**
   * Devuelve el Numero de Lineas detalle en grid jtDet
   *
   * @return int Numero Lineas
   */
//  int getNumLineasDetalle()
//  {
//    return 0;
//  }
  /**
   * Inserta cabecera de PAGO
   * @param numLin int
   *
   * @throws SQLException
   * @throws java.net.UnknownHostException
   * @return int Devuelve el Numero de Linea insertado
   */
  int insertaCabPago(int numLin) throws SQLException,java.net.UnknownHostException
  {
    int numPago = lbp_numeE.getValorInt();

    if (numPago == 0)
    {
      numPago = getNextNumPago();
      numLin = 1;
      Pcondef.setEnabled(false);
      lbp_numeE.setValorDec(numPago);
      setBloqueo(dtAdd, "libpagcab",
                emp_codiE.getValorInt() +
               "|" + lbp_numeE.getValorInt());
     }
     if (numLin == 0)
       numLin = getNextNumLineaPago(numPago);
     insertaCabPago(numPago,numLin,0,0,0,0,"","");
     return numLin;
  }
  /**
   *
   * Inserta cabecera de Pago
   *  Numero de Linea insertada
   * @param numPago int
   * @param numLin int
   * @param banCodi int
   * @param baofic int
   * @param badico int
   * @param bacuen double
   * @param numtal String
   * @param facprv String
   * @throws SQLException
   */
  void insertaCabPago(int numPago,int numLin,int banCodi,
                     int baofic,int badico,double bacuen,String numtal,String facprv) throws SQLException
  {
    dtAdd.addNew("libpagcab");
    dtAdd.setDato("emp_codi", emp_codiE.getValorInt());
    dtAdd.setDato("lbp_nume", numPago);
    dtAdd.setDato("lbp_numlin", numLin);
    dtAdd.setDato("lbp_fecpag", lbp_fecpagE.getText(), "dd-MM-yyyy");
    dtAdd.setDato("bat_codi", bat_codiE.getValorInt());
    dtAdd.setDato("lbp_tippag", lbp_tippagE.getValor());
    dtAdd.setDato("ban_codi", banCodi);
    dtAdd.setDato("lbp_baofic", baofic);
    dtAdd.setDato("lbp_badico", badico);
    dtAdd.setDato("lbp_bacuen", bacuen);
    dtAdd.setDato("lbp_numtal", numtal);
    dtAdd.setDato("lbp_facprv", facprv);
    dtAdd.update();

  }
  int getNextNumLineaPago(int numPago) throws SQLException
  {
    s = "SELECT max(lbp_numlin) as lbp_numlin  FROM libpagcab WHERE emp_codi = " + emp_codiE.getValorInt() +
         " AND lbp_nume = " + numPago;
     dtStat.select(s);
     return  dtStat.getInt("lbp_numlin", true) + 1;

  }

  int getNextNumPago() throws SQLException
  {
    s = "SELECT max(lbp_nume) as lbp_nume FROM libpagcab WHERE emp_codi = " + emp_codiE.getValorInt();
    dtStat.select(s);
    return dtStat.getInt("lbp_nume", true) + 1;
  }

  int getNextNumLineaDet(int numPago, int numLinea) throws SQLException
  {
    s = "SELECT max(lip_nulide) as lip_nulide  FROM libpagdet " +
        " WHERE emp_codi = " + emp_codiE.getValorInt() +
        " AND lbp_nume = " + numPago +
        " and lbp_numlin = " + numLinea;
    dtStat.select(s);
    return dtStat.getInt("lip_nulide", true) + 1;

  }



  /**
   *  Inserta Pagos en tabla libpagcab y libpagdet.
   * @param origen String
   * @param ejeNume int
   * @param numFra int
   * @param numVto int
   * @param impPag double
   * @param totPag boolean
   * @param rowPago int
   * @param actVtos boolean
   * @throws SQLException
   */
  void insertaPago(String origen,int ejeNume,int numFra,
                   int numVto,double impPag,boolean totPag,int rowPago,boolean actVtos) throws SQLException
  {
    int numPago=lbp_numeE.getValorInt();
    int numLin=jtPag.getValorInt(rowPago,15);

    if (numPago==0)
    {
      numPago=getNextNumPago();
      numLin=0;
      Pcondef.setEnabled(false);
      lbp_numeE.setValorDec(numPago);
    }
    if (numLin!=0)
    {
      // Borro los apuntes de pago sobre este n�mero de pago .
      s = "DELETE FROM libpagcab WHERE emp_codi = " + emp_codiE.getValorInt() +
          " AND lbp_nume = " + numPago +
          " AND lbp_numlin = " + numLin;
      dtAdd.executeUpdate(s);
      if (actVtos)
      {
        s = "SELECT * FROM libpagdet WHERE emp_codi = " + emp_codiE.getValorInt() +
            " AND lbp_nume = " + numPago +
            " AND lbp_numlin = " + numLin;
        if (dtCon1.select(s))
        {
          do
          {
            actLibroVto(dtCon1.getDouble("lip_import") * -1, false, null, dtCon1.getInt("eje_nume"),
                        dtCon1.getInt("lbv_numfra"), dtCon1.getInt("lbv_nume"));
          }
          while (dtCon1.next());
        }

        // Anulo los apuntes de pago sobre las lineas de detalle
        s = "DELETE FROM libpagdet WHERE emp_codi = " + emp_codiE.getValorInt() +
            " AND lbp_nume = " + numPago +
            " AND lbp_numlin = " + numLin;
        dtAdd.executeUpdate(s);
      }
    }
    else
    {
      numLin = getNextNumLineaPago(numPago);
      jtPag.setValor("" + numLin, rowPago, 15);
    }


    insertaCabPago(numPago,numLin,ban_codiG.getValorInt(),lbp_baoficG.getValorInt(),
                   lbp_badicoG.getValorInt(),lbp_bacuenG.getValorDec(),
                   lbp_numtalG.getText(),lbp_facprvG.getText());
    if (actVtos)
      insertaDetPago(numPago,numLin,0,origen,ejeNume,numFra,numVto,impPag,totPag);
    dtAdd.commit();
  }
  /**
   * Borra Apunte de pago
   * @param numPago int Numero de Pago
   * @param numLiPag int Numero Linea de Pago
   * @param nuliDet int Numero Linea de Detalle. -1 PARA todos.
   * @throws SQLException error en base de datos.
   */
  void borrarApunte(int numPago,int numLiPag,int nuliDet) throws SQLException
  {
    s = "SELECT * FROM libpagdet WHERE emp_codi = " + emp_codiE.getValorInt() +
        " AND lbp_nume = " + numPago +
        " AND lbp_numlin = " + numLiPag +
        (nuliDet<0?"":" and lip_nulide  = " + nuliDet);
    if (!dtBloq.select(s,true))
      return;
    do
    {
      actLibroVto(dtBloq.getDouble("lip_import")*-1,false,null,dtBloq.getInt("eje_nume"),
                    dtBloq.getInt("lbv_numfra"),dtBloq.getInt("lbv_nume"));
    } while (dtBloq.next());
    dtBloq.delete();
  }


  /**
   * Inserta pago en la tabla 'libpagdet' (Detalle Pagos)
   * @param numPago int
   * @param numLiPag int
   * @param nuliDet int
   * @param origen String
   * @param ejeNume int
   * @param numFra int
   * @param numVto int
   * @param impPag double
   * @param totPag boolean
   * @throws SQLException
   * @return int
   */
  int insertaDetPago(int numPago,int numLiPag,int nuliDet,String origen,int ejeNume,int numFra,
                   int numVto,double impPag,boolean totPag) throws SQLException
  {
    if (nuliDet==0)
      nuliDet=getNextNumLineaDet(numPago,numLiPag);
    dtAdd.addNew("libpagdet");
    dtAdd.setDato("emp_codi", emp_codiE.getValorInt());
    dtAdd.setDato("lbp_nume", numPago);
    dtAdd.setDato("lbp_numlin", numLiPag);
    dtAdd.setDato("lip_nulide",nuliDet);
    dtAdd.setDato("lbv_orige", origen);
    dtAdd.setDato("eje_nume", ejeNume);
    dtAdd.setDato("lbv_numfra", numFra);
    dtAdd.setDato("lbv_nume", numVto);
    dtAdd.setDato("lip_import", impPag);
    dtAdd.setDato("lip_totpag", totPag?"S":"N");
    dtAdd.update();
    actLibroVto(impPag,totPag,lbv_comenE.getText(),ejeNume,numFra,numVto);
    return nuliDet;
  }
  /**
   * Actualiza el Libro de Vtos.
   *
   * @param impPag double
   * @param totPag boolean
   * @param coment String
   * @param ejeNume int
   * @param numFra int
   * @param numVto int
   * @throws SQLException
   * @return int
   */
  int actLibroVto(double impPag, boolean totPag, String coment, int ejeNume, int numFra, int numVto)
      throws SQLException
  {
    s = "UPDATE librovto SET lbv_imppag = lbv_imppag + " + impPag + ", " +
        "  lbv_pagado = '" + (totPag ? "S" : "N") + "'," +
        " lbv_comen = '" + coment + "'" +
        " WHERE emp_codi = " + emp_codiE.getValorInt() +
        " and eje_nume = " + ejeNume +
        " and lbv_numfra = " + numFra +
        " and lbv_nume = " + numVto;
    return dtAdd.executeUpdate(s);
  }
  /**
   * Actualiza los datos del banco o talon en los pagos si ha habido cambios en la
   * linea.
   *
   * @param row int N� Banco
   * @throws SQLException
   */
  void actDatosBanco(int row) throws SQLException
  {
    boolean swCambio=false;
    if (! ban_codiG.isEnabled())
    {
      if (lbp_numtalG.hasCambio())
        swCambio=true;
    }
    else
    {
        if (ban_codiG.hasCambio() || lbp_baoficG.hasCambio() ||
            lbp_badicoG.hasCambio() || lbp_bacuenG.hasCambio())
         swCambio=true;
    }
    if (lbp_facprvG.hasCambio())
      swCambio=true;
    if (!swCambio)
      return;
    jtPag.resetCambio();

    s="SELECT * FROM libpagcab WHERE emp_codi = "+emp_codiE.getValorInt()+
        " and lbp_nume = "+lbp_numeE.getValorInt()+
        " and lbp_numlin = "+ jtPag.getValorInt(row,15);
    if (! dtAdd.select(s))
      throw new SQLException("No encontrado registro cabecera de pago\n"+s);
    dtAdd.edit();
    if (!ban_codiG.isEnabled())
      dtAdd.setDato("lbp_numtal", lbp_numtalG.getText());
    else
    {
      dtAdd.setDato("ban_codi", ban_codiG.getValorInt());
      dtAdd.setDato("lbp_baofic", lbp_baoficG.getValorInt());
      dtAdd.setDato("lbp_badico", lbp_badicoG.getValorInt());
      dtAdd.setDato("lbp_bacuen", lbp_bacuenG.getValorDec());
    }
    dtAdd.setDato("lbp_facprv",lbp_facprvG.getText());
    dtAdd.update();
    dtAdd.commit();
  }

  int cambiaLineaPago(int row,boolean swGuarda)
  {
    if (jtPag.getValorInt(row,15)>0)
    {
      int nErr=checkBanco();
      if (nErr>=0)
        return nErr;
    }
    if (! jtPag.hasCambio())
      return -1;
//    if (lbv_numfraG.getValorDec()==0)
//      return -1;
    try
    {
      if (lbv_imppagG.getValorDec() <= 0)
      {
        mensajeErr("Importe a Pagar NO puede ser inferior o igual a cero");
        return 5;
      }

      if (swGuarda && lbv_imppagG.isEditable() )
      {
        if (lbv_origeG.hasCambio() || eje_numeG.hasCambio() ||
            lbv_numfraG.hasCambio()  || lbv_numeG.hasCambio())
        {
          if (existeLinea( lbv_origeG.getText(),
                             eje_numeG.getValorInt(),
                             lbv_numfraG.getValorInt(),
                             lbv_numeG.getValorInt(),jtPag.getValorInt(15), 0))
            {
              mensajeErr("Ya existe un apunte sobre este vto en esta carga");
              return 2;
            }
          if (lbv_imppagG.isEditable() && !buscaDatosVto(row, 1,
                             lbv_origeG.getText(),
                             emp_codiE.getValorInt(),
                             eje_numeG.getValorInt(),
                             lbv_numfraG.getValorInt(),
                             lbv_numeG.getValorInt()))
          {
            mensajeErr("Registro de VTO ... No valido");
            return 2;
          }
          if (dtStat.getString("lbv_pagado").equals("S"))
          {
            mensajeErr("Registro de Vencimiento esta marcado como YA pagado");
            return 2;
          }
        }
      }
      int nErr=checkBanco();
      if (nErr>=0)
        return nErr;
      if (!swGuarda)
        return -1;
      if (lbv_imppagG.isEnabled())
      {
        if (checkGrupo(lbv_origeG.getText(),
                       eje_numeG.getValorInt(),
                       lbv_numfraG.getValorInt(), "", dtAux))
        {
          int res = mensajes.mensajeYesNo("El Vto: "+(eje_numeG.getValorInt()+"/"+lbv_numfraG.getValorInt())+"  tiene otras Vto. agrupados ... Insertarlos", this);
          if (res == mensajes.YES)
          {
            insertaPago(lbv_origeG.getText(),
                      eje_numeG.getValorInt(),
                      lbv_numfraG.getValorInt(),
                      lbv_numeG.getValorInt(),
                      lbv_imppagG.getValorDec(),
                      lbp_totpagG.isSelected(), row,lbp_totpagG.isEnabled());
            insertaFactGrupo(dtAux);
            return -1;
          }
        }
        insertaPago(lbv_origeG.getText(),
                    eje_numeG.getValorInt(),
                    lbv_numfraG.getValorInt(),
                    lbv_numeG.getValorInt(),
                    lbv_imppagG.getValorDec(),
                    lbp_totpagG.isSelected(), row,lbp_totpagG.isEnabled());
        return -1;
      }
      else
        actDatosBanco(row);

    }
    catch (SQLException k)
    {
      Error("Error al Chequear Linea de Pago", k);
    }
    return -1;
  }

  private int checkBanco(char checkBanco,int banco,int baofic,double bacuen,String numtal)
  {
    if (checkBanco=='T')
    {
      if (banco == 0)
      {
        mensajeErr("Introduzca Banco del Proveedor/Transportista");
        return 8;
      }
      if (baofic == 0)
      {
        mensajeErr("Introduzca  sucursal del banco");
        return 9;
      }
      if (bacuen == 0)
      {
        mensajeErr("Introduzca Numero Cuenta de Proveedor/Transportista");
        return 11;
      }
    }
    if (checkBanco=='P'  || checkBanco=='C')
    {
      if (numtal.trim().equals(""))
      {
        mensajeErr("Introduzca Numero de Tal�n/Pagare");
        return 12;
      }
    }
    return -1;

  }
  private int checkBanco()
  {
    return checkBanco(lbp_tippagE.getValor().charAt(0),ban_codiG.getValorInt(),lbp_baoficG.getValorInt(),
                      lbp_bacuenG.getValorDec(),lbp_numtalG.getText());
  }

  /**
   * Configura el Grid de pago
   * @throws Exception
   * @return CGridEditable
   */
  CGridEditable confGridPago() throws Exception
  {
    CGridEditable jt = new CGridEditable(16)
    {
      protected void cambiaColumna(int col, int colNueva, int row)
      {
        try
        {
          if (col == 3 && lbv_origeG.isEditable())
          {
            buscaDatosVto(row, 1,
                          lbv_origeG.getText(),
                          emp_codiE.getValorInt(),
                          eje_numeG.getValorInt(),
                          lbv_numfraG.getValorInt(),
                          lbv_numeG.getValorInt());
//                          this.getValorInt(row,1),this.getValorInt(row,2),this.getValorInt(row,3));
          }

        } catch (SQLException k)
        {
          Error("Error al buscar datos de Vto",k);
        }
      }

      public int cambiaLinea(int row, int col)
      {
        int linErr= cambiaLineaPago(row,true);
        return linErr;
      }

      public void afterCambiaLinea()
      {
        try {
          afterCambiaLineaPago();
        } catch (SQLException k)
        {
          Error("Error al ver datos de Pago de la Linea",k);
        }
      }
      public void afterCambiaLineaDis(int nRow)
      {
        try
        {
          if (this.isVacio() || nav.pulsado==navegador.ADDNEW || nav.pulsado==navegador.EDIT || !swVerDet)
            return;
          verDatosPago(lbp_numeE.getValorInt(), jtPag.getValorInt(nRow, 15),
                       jtPag.getValString(0));
        } catch (SQLException k)
        {
          Error("Error al ver datos de Pago de la Linea",k);
        }

      }

      public boolean deleteLinea(int row, int col)
      {
        try
        {
          if (jtPag.getValorInt(15) != 0)
          {
            borrarApunte(lbp_numeE.getValorInt(), jtPag.getValorInt(15),-1);
            dtAdd.commit();
          }
        }
        catch (SQLException k)
        {
          Error("Error al borrar linea", k);
        }
        return true;
      }

    };
//    jt.setPonValoresEnabled(true);
    lbv_origeG.setMayusc(true);
    lbv_origeG.setCaracterAceptar("CT");
    lbv_origeG.setHorizontalAlignment(SwingConstants.CENTER);
    lbv_origeG.setToolTipText("C - Compra //  T - Transporte");
    lbv_origeG.setText("C");

    lbp_totpagG.setSelected(true);

    lbv_nombreG.setEnabled(false);
    lbv_impvtoG.setEnabled(false);
    lbv_fecvtoG.setEnabled(false);
    lbp_numlinG.setEnabled(false);
    eje_numeG.setValorInt(EU.ejercicio);

    Vector v=new Vector();
    v.addElement("Orig"); // 0 Origen: Proveedor o Transp
    v.addElement("Ejerc");  // 1
    v.addElement("Factura"); // 2
    v.addElement("N.V");   // 3 Numero Vto.
    v.addElement("Proveedor/Transp."); // 4
    v.addElement("Imp.Vto."); // 5
    v.addElement("Fec.Vto"); // 6
    v.addElement("Imp.Pag"); // 7
    v.addElement("Banco"); // 8
    v.addElement("Ofic."); // 9
    v.addElement("DC"); // 10
    v.addElement("Cuenta"); // 11
    v.addElement("Num.Talon"); // 12
    v.addElement("S/Fra.");// 13
    v.addElement("TP"); // 14 Totalmente Pagado
    v.addElement("NL"); // 15 Numero de Linea
    jt.setCabecera(v);
    jt.setAnchoColumna(new int[]{30,40,63,32,193,70,66,74,40,40,30,90,90,90,30,30});
    jt.setAlinearColumna(new int[]{1,2,2,2,0,2,1,2,2,2,2,2,2,0,1,2});
    jt.setFormatoColumna(5,"---,--9.99");
    jt.setFormatoColumna(7,"---,--9.99");
    jt.setFormatoColumna(8,"9999");
    jt.setFormatoColumna(9,"9999");
    jt.setFormatoColumna(10,"99");
    jt.setFormatoColumna(11,"9999999999");
    jt.setFormatoColumna(14,"BSN");

    Vector vc = new Vector();
    vc.add(lbv_origeG); // 0
    vc.add(eje_numeG); // 1
    vc.add(lbv_numfraG); // 2
    vc.add(lbv_numeG); // 3
    vc.add(lbv_nombreG); // 4
    vc.add(lbv_impvtoG); // 5
    vc.add(lbv_fecvtoG); // 6
    vc.add(lbv_imppagG); // 7
    vc.add(ban_codiG); // 8
    vc.add(lbp_baoficG); // 9
    vc.add(lbp_badicoG); // 10
    vc.add(lbp_bacuenG); // 11
    vc.add(lbp_numtalG); // 12
    vc.add(lbp_facprvG); // 13
    vc.add(lbp_totpagG); // 14
    vc.add(lbp_numlinG); // 15
    jt.setCampos(vc);
    return jt;
  }

  CGridEditable confGridApunte() throws Exception
  {
    CGridEditable jt=new CGridEditable(8)
    {
      public int cambiaLinea(int row, int col)
      {
        int linErr = cambiaLineaApu(row);
        return linErr;
      }

      protected void cambiaColumna(int col, int colNueva, int row)
      {
        try
        {
          if (col == 2)
          {
            buscaDatosVto(row, 2,
                          lbv_origeG.getText(),
                          emp_codiE.getValorInt(),
                          eje_numeP.getValorInt(),
                          lbv_numfraP.getValorInt(),
                          lbv_numeP.getValorInt());
          }
        }
        catch (SQLException k)
        {
          Error("Error al buscar datos de Vto", k);
        }
      }

      public void afterCambiaLinea()
      {
        actualizaAcuPago();
      }
      public boolean deleteLinea(int row, int col)
      {
        try
        {
          if (jtApu.getValorInt(7) != 0)
          {
            borrarApunte(lbp_numeE.getValorInt(), jtPag.getValorInt(15), jtApu.getValorInt(row,7));
            jtApu.setValor("0", 7);
            dtAdd.commit();
          }
        }
        catch (SQLException k)
        {
          Error("Error al borrar linea", k);
        }
        return true;
      }

    };
    Vector v=new Vector();
    v.addElement("Ejerc"); // 0
    v.addElement("Factura"); // 1
    v.addElement("N.V"); // 2
    v.addElement("Imp.Pag");  // 3
    v.addElement("TP");  // 4 Tot. pagado
    v.addElement("Imp.Vto."); // 5
    v.addElement("Fec.Vto."); // 6
    v.addElement("NL"); // 7
    jt.setCabecera(v);
    jt.setAlinearColumna(new int[]{2,2,2,2,1,2,1,2});
    jt.setAnchoColumna(new int[]{40,63,32,75,30,75,80,30});
    jt.setFormatoColumna(3,"---,--9.99");
    jt.setFormatoColumna(5,"---,--9.99");
    jt.setFormatoColumna(4,"BSN");
    eje_numeP.setValorInt(EU.ejercicio);
    lbv_fecvtoP.setEnabled(false);
    lbv_impvtoP.setEnabled(false);
    lip_nulideP.setEnabled(false);
    lbp_totpagP.setSelected(true);
    Vector vc=new Vector();

    vc.add(eje_numeP);
    vc.add(lbv_numfraP);
    vc.add(lbv_numeP);
    vc.add(lbv_imppagP);
    vc.add(lbp_totpagP);
    vc.add(lbv_impvtoP);
    vc.add(lbv_fecvtoP);
    vc.add(lip_nulideP);
    jt.setCampos(vc);
    return jt;
  }
  void afterCambiaLineaPago() throws SQLException
  {
    int nPagos = verDatosPago(lbp_numeE.getValorInt(), jtPag.getValorInt(15), jtPag.getValString(0));
    actAcumPagos();
    actCamposEnabled(nPagos < 2);
  }

  /**
   * Ver datos del grid de pagos (grid inferior)
   * @param numPago int
   * @param numLinea int
   * @param origen String
   * @throws SQLException
   * @return int Numero de Pagos para esa linea
   */
  int verDatosPago(int numPago,int numLinea,String origen) throws SQLException
  {
    jtApu.removeAllDatos();
    Vector v=getDatosPago(numPago,numLinea,origen,true);

    if (v==null)
      return 0;
    for (int n=0;n<v.size();n++)
      jtApu.addLinea((Vector) v.elementAt(n));
    return v.size();
  }


  /**
   * Ver Datos del Pago.
   * Muestra las lineas del detalle.
   * @param numPago int
   * @param numLinea int
   * @param origen String
   * @param verDetalle boolean
   * @throws SQLException
   * @return Vector
   */
  Vector getDatosPago(int numPago,int numLinea,String origen,boolean verDetalle) throws SQLException
  {
    prvApunte=0;
    impPagadoDet=0;
    nLineasDet=0;

    s = "SELECT  d.*,l.lbv_fecvto,l.lbv_impvto,l.lbv_copvtr,l.lbv_nombre,l.lbv_pagado FROM libpagdet as  d "+
      " left join librovto as l " +
      " on l.emp_codi = " + emp_codiE.getValorInt() +
      (origen==null?" and l.lbv_orige = d.lbv_orige ":
         " and l.lbv_orige = '"+origen+"'")+
      " and l.eje_nume = d.eje_nume "+
      " and l.lbv_numfra = d.lbv_numfra "+
      " and l.lbv_nume = d.lbv_nume "+
      " where d.emp_codi = " + emp_codiE.getValorInt() +
      " AND d.lbp_nume = " + numPago +
      " and d.lbp_numlin = " + numLinea+
      " order by d.lip_nulide";
    if (! dtCon1.select(s))
      return null;
    prvApunte=dtCon1.getInt("lbv_copvtr",true);
    prvNombre=dtCon1.getString("lbv_nombre");
    Vector dat=new Vector();
    do
    {
      if (verDetalle)
      {
        Vector v = new Vector();
        v.addElement(dtCon1.getString("eje_nume"));
        v.addElement(dtCon1.getString("lbv_numfra"));
        v.addElement(dtCon1.getString("lbv_nume"));
        v.addElement(dtCon1.getString("lip_import"));
        v.addElement(new Boolean(dtCon1.getString("lip_totpag").equals("S")));
        v.addElement(dtCon1.getString("lbv_impvto"));
        v.addElement(dtCon1.getFecha("lbv_fecvto", "dd-MM-yyyy"));
        v.add(dtCon1.getString("lip_nulide"));
        dat.addElement(v);
      }
      impPagadoDet+=dtCon1.getDouble("lip_import");
      nLineasDet++;
//      jtApu.addLinea(v);
    }  while (dtCon1.next());

    return dat;
  }

  void insertaFactGrupo(DatosTabla dt) throws SQLException
  {
//    jtApu.removeAllDatos();
    do
    {
      insertaDetPago(lbp_numeE.getValorInt(), jtPag.getValorInt(15), 0,
                    jtPag.getValString(0), dt.getInt("eje_nume"),
                    dt.getInt("lbv_numfra"), dt.getInt("lbv_nume"),
                    dt.getDouble("lbv_impvto") - dt.getDouble("lbv_imppag"), true);
    } while (dt.next());
//      Vector v = new Vector();
//      v.addElement(dt.getString("eje_nume"));
//      v.addElement(dt.getString("lbv_numfra"));
//      v.addElement(dt.getString("lbv_nume"));
//      v.addElement("" + (dt.getDouble("lbv_impvto") - dt.getDouble("lbv_imppag")));
//      v.addElement(new Boolean(true));
//      v.addElement(dt.getString("lbv_impvto"));
//      v.addElement(dt.getFecha("lbv_fecvto", "dd-MM-yyyy"));
//      v.add("0");
//      jtApu.addLinea(v);
//    }  while (dt.next());
    verDatosPago(lbp_numeE.getValorInt(),jtPag.getValorInt(15),jtPag.getValString(0));
//    irGridApunte(false);
  }
  void actAcumPagos()
  {
    int nRows=jtPag.getRowCount();
    int nPagos=0;
    double impPagos=0;
    for (int n=0;n<nRows;n++)
    {
      if (jtPag.getValorInt(n,2)==0 && jtPag.getValorInt(n,3)==0)
          continue;
      nPagos++;
      impPagos+=jtPag.getValorDec(n,7);
    }
    numpagosE.setValorInt(nPagos);
    lbv_imppagE.setValorDec(impPagos);
  }

  public void rgSelect() throws SQLException
  {
    super.rgSelect();
    if (!dtCons.getNOREG())
    {
      dtCons.last();
      nav.setEnabled(nav.ULTIMO, false);
      nav.setEnabled(nav.SIGUIENTE, false);
    }
  }
  void Bimpri_actionPerformed()
  {
    new miThread("")
    {
      public void run()
      {
        mensaje("Espere, por favor ... generando listado");
        pdpagreal.this.setEnabled(false);
        imprPagosReal();
        pdpagreal.this.setEnabled(true);
      }
    };
  }
  void imprPagosReal()
  {
      try {

        mensaje("Generando listado ... Espere, por favor");
        s = "SELECT c.*, d.*,l.lbv_nombre,l.lbv_pagado,l.lbv_impvto,"+
              " l.lbv_fecvto,bt.bat_nomb FROM libpagcab as c "+
              " left join bancteso as bt on  bt.bat_codi = c.bat_codi "+
              " , libpagdet as d  "+
              " left join librovto as l " +
              " on l.emp_codi = d.emp_codi "+
               " and l.lbv_orige = d.lbv_orige "+
               " and l.eje_nume = d.eje_nume "+
               " and l.lbv_numfra = d.lbv_numfra "+
               " and l.lbv_nume = d.lbv_nume "+
              " WHERE c.emp_codi = d.emp_codi " +
              " and c.lbp_nume = d.lbp_nume " +
              " and c.lbp_numlin = d.lbp_numlin "+
              " and c.emp_codi = "+emp_codiE.getValorInt()+
              " and c.lbp_nume = "+lbp_numeE.getValorInt();

        if (!dtCon1.select(s))
        {
          mensaje("");
          mensajeErr("NO encontrados registros con estas condiciones");
          return;
        }

        java.util.HashMap mp = new java.util.HashMap();
        mp.put("feinpa",lbp_fecpagE.getText());
        mp.put("fefipa",lbp_fecpagE.getText());
        mp.put("bat_codiniP",bat_codiE.getTextCombo());
        mp.put("bat_codfinP",bat_codiE.getTextCombo());
        mp.put("lbp_tippagP",lbp_tippagE.getText());
        mp.put("empCodiP",emp_codiE.getText());
        JasperReport jr;
        jr = gnu.chu.print.util.getJasperReport(EU,"pagreali");
        ResultSet rs;
        rs=dtCon1.getStatement().executeQuery(dtCon1.parseaSql(s));
        JasperPrint jp = JasperFillManager.fillReport(jr, mp,new JRResultSetDataSource(rs));
        gnu.chu.print.util.printJasper(jp, EU);
        mensajeErr("Listado generado");
        mensaje("");
      }
      catch (Exception k)
      {
        Error("Error al imprimir Libro vtos", k);
      }
    }

}

