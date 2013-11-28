package gnu.chu.anjelica.inventario;


import gnu.chu.controles.*;
import gnu.chu.Menu.*;
import gnu.chu.utilidades.*;
import java.util.*;
import java.sql.Types;
import gnu.chu.sql.*;
import java.awt.*;
import java.awt.event.*;
import gnu.chu.camposdb.*;
import javax.swing.BorderFactory;
import gnu.chu.anjelica.almacen.actStkPart;
/**
 *
 * <p>Título: traspalma</p>
 * <p>Descripción: Programa para traspasar productos de un almacen a otro.
 *  </p>
 * <p>Copyright: Copyright (c) 2005-2012
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
 * <p>Empresa: miSL</p>
 * @author chuchi P
 * @version 1.0
 */

public class traspalma  extends ventana
{
  private int cliCodi;
  actStkPart stkPart;
  String s;
  CPanel cPanel1 = new CPanel();
  CLabel cLabel1 = new CLabel();
  CLabel cLabel2 = new CLabel();
  CComboBox alm_codioE = new CComboBox();
  CComboBox alm_codifE = new CComboBox();
  proPanel pro_codiE = new proPanel();
  CLabel cLabel3 = new CLabel();
  CLabel cLabel4 = new CLabel();
  CTextField eje_numeE = new CTextField(Types.DECIMAL, "###9");
  CLabel cLabel5 = new CLabel();
  CTextField emp_codiE = new CTextField(Types.DECIMAL, "#9");
  CLabel cLabel6 = new CLabel();
  CLabel cLabel7 = new CLabel();
  CTextField pro_numlotE = new CTextField(Types.DECIMAL, "####9");
  CLabel cLabel8 = new CLabel();
  CTextField pro_indiiE = new CTextField(Types.DECIMAL, "##9");
  CLabel cLabel9 = new CLabel();
  CTextField pro_indiiE1 = new CTextField(Types.DECIMAL, "##9");
  CButton Bacecab = new CButton(Iconos.getImageIcon("check"));
  CPanel Pdatos = new CPanel();
  Cgrid jt = new Cgrid(4);
  CButton Baceptar = new CButton(Iconos.getImageIcon("check"));
  CButton Bcancela = new CButton(Iconos.getImageIcon("cancel"));
  CTextField pro_serieE = new CTextField(Types.CHAR, "X");
  CLabel cLabel10 = new CLabel();
  CTextField avc_fecalbE = new CTextField(Types.DATE, "dd-MM-yyyy");
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CPanel cPanel2 = new CPanel();
  CLabel cLabel11 = new CLabel();
  CTextField numIndE = new CTextField(Types.DECIMAL, "##9") ;
  CLabel Kilos = new CLabel();
  CTextField kilosE = new CTextField(Types.DECIMAL, "---,--9.99");

  public traspalma(EntornoUsuario eu, Principal p)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    setTitulo("Traspasar Productos entre Almacenes");

    try
    {
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

  public traspalma(gnu.chu.anjelica.menu p, EntornoUsuario eu)
  {

    EU = eu;
    vl = p.getLayeredPane();
    setTitulo("Traspasar Productos entre Almacenes");
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
    this.setSize(new Dimension(492,530));
    this.setVersion("2012-02-16");
    statusBar = new StatusBar(this);
    conecta();
    ArrayList v = new ArrayList();
    cLabel10.setText("Fecha Traspaso");
    cLabel10.setBounds(new Rectangle(4, 95, 91, 15));
    avc_fecalbE.setBounds(new Rectangle(90, 93, 75, 19));
    Bacecab.setMargin(new Insets(0, 0, 0, 0));
    jt.setMaximumSize(new Dimension(1, 252));
    numIndE.setEnabled(false);
    kilosE.setEnabled(false);
    jt.setMinimumSize(new Dimension(1, 252));
    jt.setPreferredSize(new Dimension(1, 252));
    jt.setPuntoDeScroll(50);
    Pdatos.setMaximumSize(new Dimension(490, 121));
    Pdatos.setMinimumSize(new Dimension(490, 121));
    Pdatos.setPreferredSize(new Dimension(490, 121));
    Baceptar.setMaximumSize(new Dimension(95, 26));
    Baceptar.setMinimumSize(new Dimension(95, 26));
    Baceptar.setPreferredSize(new Dimension(95, 26));
    Bcancela.setMaximumSize(new Dimension(95, 26));
    Bcancela.setMinimumSize(new Dimension(95, 26));
    Bcancela.setPreferredSize(new Dimension(95, 26));
    cPanel2.setBorder(BorderFactory.createLineBorder(Color.black));
    cPanel2.setMaximumSize(new Dimension(60, 60));
    cPanel2.setMinimumSize(new Dimension(30, 30));
    cPanel2.setPreferredSize(new Dimension(30, 30));
    cPanel2.setQuery(false);
    cPanel2.setLayout(null);
    cLabel11.setText("N.Unidades");
    cLabel11.setBounds(new Rectangle(1, 4, 73, 17));
    numIndE.setText("999");
    numIndE.setBounds(new Rectangle(73, 4, 30, 17));
    Kilos.setText("Kilos");
    Kilos.setBounds(new Rectangle(116, 4, 39, 17));
    kilosE.setText("---,--#.99");
    kilosE.setBounds(new Rectangle(160, 4, 63, 17));
    v.add("Indiv"); // 0
    v.add("Peso"); // 1
    v.add("Unid."); // 2
    v.add("Inserta"); // 3
    jt.setCabecera(v);
    jt.setAnchoColumna(new int[]    {30, 50,40, 60});
    jt.setAlinearColumna(new int[] {2, 2,2, 1});
    jt.setFormatoColumna(3, "BSN");
    jt.resetRenderer(3);
    jt.setAjustarGrid(true);
    pro_serieE.setMayusc(true);
    pro_serieE.setAutoNext(true);
    pro_serieE.setMayusc(true);
    eje_numeE.setAutoNext(true);
    emp_codiE.setAutoNext(true);
    pro_numlotE.setAutoNext(true);
    pro_indiiE.setAutoNext(true);
    pro_indiiE1.setAutoNext(true);
    pro_serieE.setText("A");
    pro_serieE.setBounds(new Rectangle(264, 71, 17, 17));
    Pdatos.add(pro_codiE, null);
    Pdatos.add(cLabel1, null);
    Pdatos.add(alm_codioE, null);
    Pdatos.add(cLabel2, null);
    Pdatos.add(alm_codifE, null);
    Pdatos.add(cLabel3, null);
    Pdatos.add(cLabel4, null);
    Pdatos.add(eje_numeE, null);
    Pdatos.add(cLabel5, null);
    Pdatos.add(emp_codiE, null);
    Pdatos.add(cLabel6, null);
    Pdatos.add(cLabel7, null);
    Pdatos.add(pro_numlotE, null);
    Pdatos.add(pro_serieE, null);
    Pdatos.add(cLabel10, null);
    Pdatos.add(avc_fecalbE, null);
    Pdatos.add(Bacecab, null);
    Pdatos.add(pro_indiiE1, null);
    Pdatos.add(cLabel8, null);
    Pdatos.add(pro_indiiE, null);
    Pdatos.add(cLabel9, null);

    cPanel1.setLayout(gridBagLayout1);
    cLabel1.setToolTipText("");
    cLabel1.setText("Almacen Origen");
    cLabel1.setBounds(new Rectangle(26, 6, 93, 17));
    cLabel2.setText("Almacen Final");
    cLabel2.setBounds(new Rectangle(26, 28, 85, 17));
    alm_codioE.setBounds(new Rectangle(120, 6, 288, 17));
    alm_codifE.setBounds(new Rectangle(118, 28, 289, 17));
    pro_codiE.setAncTexto(50);
    pro_codiE.setBounds(new Rectangle(54, 49, 408, 18));
    cLabel3.setText("Producto");
    cLabel3.setBounds(new Rectangle(2, 48, 51, 19));
    cLabel4.setText("Ejercicio");
    cLabel4.setBounds(new Rectangle(46, 70, 51, 17));
    eje_numeE.setBounds(new Rectangle(102, 70, 33, 17));
    cLabel5.setText("Empresa");
    cLabel5.setBounds(new Rectangle(144, 70, 55, 17));
    emp_codiE.setBounds(new Rectangle(204, 70, 21, 17));
    cLabel6.setText("Serie");
    cLabel6.setBounds(new Rectangle(234, 70, 33, 17));
    cLabel7.setText("Numero Lote");
    cLabel7.setBounds(new Rectangle(325, 70, 73, 17));
    pro_numlotE.setBounds(new Rectangle(405, 70, 36, 17));
    cLabel8.setText("De individuo");
    cLabel8.setBounds(new Rectangle(168, 94, 71, 16));
    pro_indiiE.setBounds(new Rectangle(240, 94, 37, 17));
    cLabel9.setBounds(new Rectangle(282, 94, 67, 17));
    cLabel9.setText("A individuo");
    pro_indiiE1.setBounds(new Rectangle(350, 94, 37, 17));
    Bacecab.setBounds(new Rectangle(390, 90, 77, 22));
    Bacecab.setBorderPainted(true);
    Pdatos.setBorder(BorderFactory.createLoweredBevelBorder());
    Pdatos.setLayout(null);
    this.getContentPane().add(cPanel1, BorderLayout.CENTER);
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);

    cPanel1.add(Pdatos, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.NONE,
                                               new Insets(1, 2, 0, 1), 0, 0));
    cPanel1.add(jt, new GridBagConstraints(0, 1, 3, 1, 1.0, 1.0
                                           , GridBagConstraints.CENTER,
                                           GridBagConstraints.BOTH,
                                           new Insets(0, 2, 0, 1), 0, 0));

    cPanel1.add(Bcancela, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
                                                 , GridBagConstraints.EAST,
                                                 GridBagConstraints.NONE,
                                                 new Insets(0, 0, 5, 30), 0, 0));
    cPanel1.add(Baceptar, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
                                                 , GridBagConstraints.WEST,
                                                 GridBagConstraints.NONE,
                                                 new Insets(0, 30, 5, 0), 0, 1));
    cPanel1.add(cPanel2, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0
                                                , GridBagConstraints.CENTER,
                                                GridBagConstraints.HORIZONTAL,
                                                new Insets(0, 0, 0, 0), 0, 0));
    cPanel2.add(numIndE, null);
    cPanel2.add(cLabel11, null);
    cPanel2.add(Kilos, null);
    cPanel2.add(kilosE, null);
  }

  public void iniciarVentana() throws Exception
  {
    Pdatos.setDefButton(Bacecab);
    s = "SELECT alm_codi, alm_nomb from v_almacen order by alm_codi";
    dtCon1.select(s);
    alm_codifE.addItem(dtCon1);
    dtCon1.select(s); // Volvemos al principio
    alm_codioE.addItem(dtCon1);
    pro_codiE.iniciar(dtStat, this, vl, EU);
    stkPart=new actStkPart(dtCon1,EU.em_cod);
    eje_numeE.setText("" + EU.ejercicio);
    emp_codiE.setText("" + EU.em_cod);
    pro_serieE.setText("A");
    avc_fecalbE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
    s="SELECT cli_codi from v_config WHERE emp_codi = "+EU.em_cod;
    if (! dtStat.select(s))
    {
        Error("Codigo de Cliente INTERNO NO encontrado",new Exception());
        return;
    }
    cliCodi=dtStat.getInt("cli_codi");
    activarEventos();
    activar(false);

    pro_codiE.requestFocus();

  }

  void activar(boolean enab)
  {
    Baceptar.setEnabled(enab);
    Bcancela.setEnabled(enab);
    jt.setEnabled(enab);
  }

  void selecInd()
  {
    if (jt.isVacio())
      return;
    if (jt.getSelectedColumn() != 3)
      return;
    if (jt.getValBoolean(3))
    { // Esta activado se desactivara.
      numIndE.setValorDec(numIndE.getValorInt() - jt.getValorDec(1));
      kilosE.setValorDec(kilosE.getValorDec() - jt.getValorDec(1));
    }
    else
    {
      numIndE.setValorDec(numIndE.getValorInt() + jt.getValorDec(1));
      kilosE.setValorDec(kilosE.getValorDec() + jt.getValorDec(1));
    }
    jt.setValor(jt.getValBoolean(3) ? "N" : "S", 3);
  }

  void activarEventos()
  {
    jt.addMouseListener(new MouseAdapter()
    {
            @Override
      public void mouseClicked(MouseEvent e)
      {
        selecInd();
      }
    });
    jt.tableView.addKeyListener(new KeyAdapter()
    {

            @Override
      public void keyPressed(KeyEvent e)
      {
        if (e.getKeyCode() == KeyEvent.VK_INSERT)
        {
          selecInd();
          if (jt.getSelectedRow() < jt.getRowCount())
            jt.requestFocus(jt.getSelectedRow() + 1, 3);
        }
      }

    });

    Bacecab.addActionListener(new ActionListener()
    {
            @Override
      public void actionPerformed(ActionEvent e)
      {
        aceptaCab();
      }
    });
    Baceptar.addActionListener(new ActionListener()
    {
            @Override
      public void actionPerformed(ActionEvent e)
      {
        traspDatos();
      }
    });
    Bcancela.addActionListener(new ActionListener()
    {
            @Override
      public void actionPerformed(ActionEvent e)
      {
        mensajeErr("Traspaso ... Cancelado");
        Pdatos.setEnabled(true);
        activar(false);
        pro_codiE.requestFocus();
      }
    });

  }

  void aceptaCab()
  {

    try
    {
      if (!pro_codiE.controlar())
      {
        mensajeErr(pro_codiE.getMsgError());
        return;
      }
      if (pro_numlotE.getValorInt() == 0)
      {
        mensajeErr("Introduzca N. de Lote");
        pro_numlotE.requestFocus();
        return;
      }
      if (alm_codifE.getValor().equals(alm_codioE.getValor()))
      {
        mensajeErr("Los dos almacenes NO pueden ser iguales");
        alm_codioE.requestFocus();
        return;
      }
      if (avc_fecalbE.isNull())
      {
        mensajeErr("Introduzca Fecha de Traspaso");
        avc_fecalbE.requestFocus();
        return;
      }
      s = "SELECT * FROM V_STKPART WHERE " +
          " EJE_NUME= " + eje_numeE.getValorInt() +
          " AND EMP_CODI= " + emp_codiE.getValorInt() +
          " AND PRO_SERIE='" + pro_serieE.getText() + "'" +
          " AND pro_nupar= " + pro_numlotE.getValorInt() +
          " and pro_codi= " + pro_codiE.getValorInt() +
          " and stp_kilact > 0" +
          " and alm_codi = " + alm_codioE.getValor() +
          " and pro_numind >= " + pro_indiiE.getValorInt() +
          (pro_indiiE1.getValorInt() > 0 ?
           " and PRO_NUMIND <= " + pro_indiiE1.getValorInt() : "") +
          " order by pro_numind";
      if (!dtCon1.select(s))
      {
        msgBox("No encontrados datos en Stock-Partidas");
        pro_codiE.requestFocus();
        return;
      }
      Pdatos.setEnabled(false);
      activar(true);
      jt.removeAllDatos();
      int unid=0;
      double kilos = 0;
      do
      {
        ArrayList v = new ArrayList();
        v.add(dtCon1.getString("pro_numind"));
        v.add(Formatear.format(dtCon1.getString("stp_kilact"),"##,##9.99"));
        v.add(Formatear.format(dtCon1.getString("stp_unact"),"##9"));
        v.add("S");
        kilos += dtCon1.getDouble("stp_kilact");
        unid+=dtCon1.getDouble("stp_unact");
        jt.addLinea(v);
      }
      while (dtCon1.next());
      jt.requestFocusInicio();
      numIndE.setValorDec(unid);
      kilosE.setValorDec(kilos);
    }
    catch (Exception k)
    {
      Error("Error al cargar datos", k);
    }
  }

  void traspDatos()
  {
    int nl = jt.getRowCount();
    int unidT = 0;
    for (int n = 0; n < nl; n++)
    {
      if (!jt.getValBoolean(n, 3))
        continue;
      unidT++;
    }
    if (unidT == 0)
    {
      mensajeErr("NO ha selecionado ningun Individuo");
      return;
    }

    new miThread("")
    {
            @Override
      public void run()
      {
        activar(false);
        traspDato1();
      }
    };
  }

  void traspDato1()
  {
    try
    {
      DatosTabla dtCon1 = new DatosTabla(ct);
      dtCon1.setStatement(stUp);
      int numAlb;
      double kilosT = 0, kilos = 0;
      double unidT = 0, unid = 0;

      int nl = jt.getRowCount();
      for (int n = 0; n < nl; n++)
      {
        if (!jt.getValBoolean(n, 3))
          continue;
        unidT++;
      }

      Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
      mensaje("Espere, por favor ... traspasando Individuos");

      // Busco el numero de Albaran a asignar.
      s = "SELECT num_serieX FROM v_numerac WHERE emp_codi = " + EU.em_cod +
          " AND eje_nume = " + EU.ejercicio;
      if (!dtCon1.select(s))
        throw new Exception("s: " + s + "\nError al buscar numeracion serie X");
      numAlb = dtCon1.getInt("num_serieX");
      numAlb++;
      // Lo guardo .
      s = "UPDATE v_numerac set  num_serieX = " + numAlb +
          " WHERE emp_codi = " + EU.em_cod +
          " AND eje_nume = " + EU.ejercicio;

      stUp.executeUpdate(s);

      // Genero la cabecera del Albaran
      dtCon1.addNew("v_albavec");

      dtCon1.setDato("emp_codi", EU.em_cod);
      dtCon1.setDato("avc_ano", EU.ejercicio);
      dtCon1.setDato("cli_codi",cliCodi);
      dtCon1.setDato("avc_serie", "X");
      dtCon1.setDato("avc_nume", numAlb);
      dtCon1.setDato("avc_fecalb", avc_fecalbE.getText(), "dd-MM-yyyy");
      dtCon1.setDato("usu_nomb", EU.usuario);
      dtCon1.setDato("alm_codori", alm_codioE.getValor());
      dtCon1.setDato("alm_coddes", alm_codifE.getValor());
      dtCon1.setDato("avc_almori", alm_codioE.getValor());
      dtCon1.setDato("avc_cerra", -1); // Cerrado
      dtCon1.setDato("avc_tarimp", 0);
      dtCon1.setDato("div_codi",1);
      dtCon1.update(stUp);

      nl = jt.getRowCount();
      for (int n = 0; n < nl; n++)
      {
        if (!jt.getValBoolean(n, 3))
          continue;
        kilosT += jt.getValorDec(n, 1);
        unidT++;
        // Insertamos linea de albaran
        dtCon1.addNew("v_albavel");
        dtCon1.setDato("emp_codi", EU.em_cod);
        dtCon1.setDato("avc_ano", EU.ejercicio);
        dtCon1.setDato("avc_serie", "X");
        dtCon1.setDato("avc_nume", numAlb);
        dtCon1.setDato("avl_numlin", n);
        dtCon1.setDato("avl_unid", jt.getValorInt(n, 2));
        dtCon1.setDato("pro_codi", pro_codiE.getText());
        dtCon1.setDato("alm_codi", alm_codifE.getValor());
        dtCon1.setDato("avl_canti", jt.getValorDec(n, 1));
        dtCon1.setDato("avc_cerra",-1);
        dtCon1.setDato("avl_fecalt","current_timestamp");
        dtCon1.update(stUp);

        // Insertamos linea partida de albaran
        dtCon1.addNew("v_albvenpar");

        dtCon1.setDato("emp_codi", EU.em_cod);
        dtCon1.setDato("avc_ano", EU.ejercicio);
        dtCon1.setDato("avc_serie", "X");
        dtCon1.setDato("avc_nume", numAlb);
        dtCon1.setDato("avl_numlin", n);
        dtCon1.setDato("pro_codi", pro_codiE.getText());
        dtCon1.setDato("avp_tiplot", "P");
        dtCon1.setDato("avp_ejelot", eje_numeE.getValorInt());
        dtCon1.setDato("avp_emplot", emp_codiE.getValorInt());
        dtCon1.setDato("avp_serlot", pro_serieE.getText());
        dtCon1.setDato("avp_numpar", pro_numlotE.getValorInt());
        dtCon1.setDato("avp_numind", jt.getValorDec(n, 0));
        dtCon1.setDato("avp_numuni", 1);
        dtCon1.setDato("avp_canti", jt.getValorDec(n, 1));
        dtCon1.update(stUp);

        // Cambiamos el almacen para el inviduo.
        s = "UPDATE V_STKPART SET alm_codi = " +alm_codifE.getValorInt()+
            "  WHERE  EJE_NUME= " + eje_numeE.getValorInt() +
            " AND EMP_CODI= " + emp_codiE.getValorInt() +
            " AND PRO_SERIE='" + pro_serieE.getText() + "'" +
            " AND pro_nupar= " + pro_numlotE.getValorInt() +
            " and alm_codi = " + alm_codioE.getValor() +
            " and pro_codi= " + pro_codiE.getValorInt() +
            " and pro_numind = " + jt.getValorDec(n, 0);
        stUp.executeUpdate(s);
      // Actualizo Acumulados
       stkPart.actAcum(pro_codiE.getValorInt(),alm_codioE.getValorInt(),
                       jt.getValorDec(n,1)*-1,jt.getValorInt(n,2)*-1,avc_fecalbE.getText());
       stkPart.actAcum(pro_codiE.getValorInt(),alm_codifE.getValorInt(),
                jt.getValorDec(n,1),jt.getValorInt(n,2),avc_fecalbE.getText());


      }
/*
      // Actualizo Existencias en almacen-Deposito Original.
      s = "select * from v_almdep WHERE pro_codi = " + pro_codiE.getValorInt() +
          " AND alm_codi = " + alm_codioE.getValor();
      if (dtCon1.select(s, true))
      {
        kilos = dtCon1.getDouble("ald_kilos") - kilosT;
        unid = dtCon1.getDouble("ald_unid") - unidT;
        dtCon1.edit(dtCon1.getCondWhere());
      }
      else
      {
        kilos = kilosT * -1;
        unid = unidT * -1;
        dtCon1.addNew("v_almdep");
        dtCon1.setDato("pro_codi ", pro_codiE.getValorInt());
        dtCon1.setDato("alm_codi", alm_codioE.getValor());
      }
      dtCon1.setDato("ald_kilos", kilos);
      dtCon1.setDato("ald_unid", unid);
      //         debug("Ins. "+dtCon1.getStrInsert());
      dtCon1.update(stUp);

      // Actualizo Existencias en almacen-Deposito Final.
      s = "select * from v_almdep WHERE pro_codi = " + pro_codiE.getValorInt() +
          " AND alm_codi = " + alm_codifE.getValor();
      if (dtCon1.select(s, true))
      {
        kilos += kilosT + dtCon1.getDouble("ald_kilos");
        unid = unidT + dtCon1.getDouble("ald_unid");
        dtCon1.edit(dtCon1.getCondWhere());
      }
      else
      {
        kilos = kilosT;
        unid = unidT;
        dtCon1.addNew("v_almdep");
        dtCon1.setDato("pro_codi ", pro_codiE.getValorInt());
        dtCon1.setDato("alm_codi", alm_codifE.getValor());
      }
      dtCon1.setDato("ald_kilos", kilos);
      dtCon1.setDato("ald_unid", unid);
      dtCon1.update(stUp);
*/
      ctUp.commit();
      mensaje("");
      msgBox("Traspaso REALIZADO ... ALBARAN N. " + numAlb);
    }
    catch (Exception k)
    {
      try
      {
        ctUp.rollback();
      }
      catch (java.sql.SQLException k1)
      {}
      Error("Error al generar el traspaso", k);
      return;
    }
    Pdatos.setEnabled(true);
    activar(false);

    pro_codiE.requestFocus();
  }
}
