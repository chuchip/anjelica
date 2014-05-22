package gnu.chu.anjelica.almacen;

import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import java.awt.*;
import javax.swing.BorderFactory;
import java.sql.*;
import gnu.chu.camposdb.*;
import java.awt.event.*;
import java.util.*;
import gnu.chu.Menu.*;
import gnu.chu.anjelica.inventario.CreaStkPart;
import gnu.chu.interfaces.ejecutable;

/**
 *
 * <p>Titulo: costkpar</p>
 * <p>Descripción: Consulta tabla de Stock Partidas </p>
 *
 * <p>Copyright: Copyright (c) 2005-2014
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
 * <p>Empresa: miSL</p>
 * @author chuchi P
 * @version 1.0
 */
public class costkpar extends ventana
{
  static final int JT_ARTIC=0;
  static final int JT_EJERC=2;
  static final int JT_SERIE=3;
  static final int JT_LOTE=4;
  static final int JT_INDI=5;
  static final int JT_ALMAC=8;
  private String filtroEmpr;
  String s;
  CPanel Pprinc = new CPanel();
  CPanel Pcabe = new CPanel();
  proPanel pro_codiE = new proPanel();
  CLabel cLabel1 = new CLabel();
  CLabel cLabel2 = new CLabel();
  CTextField eje_numeE = new CTextField(Types.DECIMAL,"###9");
  CLabel cLabel3 = new CLabel();
  CTextField pro_serieE = new CTextField(Types.CHAR,"X");
  CLabel cLabel4 = new CLabel();
  CTextField pro_nuparE = new CTextField(Types.DECIMAL,"####9");
  CLabel cLabel5 = new CLabel();
  CTextField pro_numindE = new CTextField(Types.DECIMAL,"###9");
  CLabel cLabel6 = new CLabel();
  CLinkBox alm_codiE = new CLinkBox();
  CLabel cLabel7 = new CLabel();
  CTextField stp_unactE = new CTextField(Types.DECIMAL,"----9");
  CTextField stp_uniniE = new CTextField(Types.DECIMAL,"----9");
  CLabel cLabel8 = new CLabel();
  CLabel cLabel9 = new CLabel();
  CTextField stp_kiliniE = new CTextField(Types.DECIMAL,"---,--9.99");
  CLabel cLabel10 = new CLabel();
  CTextField stp_feccreE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CTextField stp_kilactE = new CTextField(Types.DECIMAL,"---,--9.99");
  CLabel cLabel11 = new CLabel();
  CButton Baceptar = new CButton("Aceptar (F4)",Iconos.getImageIcon("check"));
  Cgrid jt = new Cgrid(15);
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CCheckBox opStock = new CCheckBox();

  public costkpar(EntornoUsuario eu, Principal p)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    setTitulo("Consulta Stock Partidas");

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

  public costkpar(gnu.chu.anjelica.menu p, EntornoUsuario eu)
  {

    EU = eu;
    vl = p.getLayeredPane();
    setTitulo("Consulta Stock Partidas");
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
    this.setSize(new Dimension(621,471));
    this.setVersion("2009-12-22");
    Pprinc.setLayout(gridBagLayout1);

    conecta();
    statusBar = new StatusBar(this);
    Pcabe.setBorder(BorderFactory.createRaisedBevelBorder());
    Pcabe.setMaximumSize(new Dimension(531, 105));
    Pcabe.setMinimumSize(new Dimension(531, 105));
    Pcabe.setPreferredSize(new Dimension(531, 105));
    Pcabe.setLayout(null);
    cLabel1.setRequestFocusEnabled(true);
    cLabel1.setText("Producto");
    cLabel1.setBounds(new Rectangle(8, 4, 54, 18));
    cLabel2.setText("Ejerc.");
    cLabel2.setBounds(new Rectangle(5, 23, 37, 17));

    cLabel3.setText("Serie");
    cLabel3.setBounds(new Rectangle(150, 23, 37, 17));
    cLabel4.setText("Num. Partida");
    cLabel4.setBounds(new Rectangle(236, 23, 75, 17));
    cLabel5.setText("Num. Individuo");
    cLabel5.setBounds(new Rectangle(401, 23, 83, 17));
    cLabel6.setText("Almacen");
    cLabel6.setBounds(new Rectangle(5, 43, 55, 17));
    alm_codiE.setAncTexto(30);
    alm_codiE.setBounds(new Rectangle(58, 43, 189, 17));
    alm_codiE.setCeroIsNull(true);
    cLabel7.setText("Un. Actual");
    cLabel7.setBounds(new Rectangle(257, 62, 58, 17));

    cLabel8.setText("Un. Iniciales");
    cLabel8.setBounds(new Rectangle(257, 43, 75, 17));
    cLabel9.setText("Cant. Incial");
    cLabel9.setBounds(new Rectangle(383, 43, 66, 17));
    cLabel10.setText("Fec.Creacion");
    cLabel10.setBounds(new Rectangle(3, 62, 86, 17));
    cLabel11.setText("Cant. Actual");
    cLabel11.setBounds(new Rectangle(383, 62, 67, 17));
    ArrayList v=new ArrayList();
    v.add("Prod."); // 0
    v.add("Nombre Prod."); //  1
    v.add("Ejer"); // 2
    v.add("Serie"); // 3
    v.add("Lote"); // 4
    v.add("N.Ind."); // 5
    v.add("Un.Act"); // 6
    v.add("Kg.Act"); // 7
    v.add("Alm"); // 8
    v.add("Prv."); // 9
    v.add("Fec.Cad."); // 10
    v.add("Fec.Creac."); // 11
    v.add("Un.Ini"); // 12
    v.add("Kg.Ini"); // 13
    v.add("Blo");
    jt.setCabecera(v);
    jt.setToolTipText("Doble click para editar apunte Stock");
    jt.setAnchoColumna(new int[]{50,100,40,30,50,40,40,60, 50,40,80,80,40,60,30});
    jt.setAlinearColumna(new int[]{2,0,2,1,2,2,2,2,2,2,1,1,2,2,1});
    jt.setFormatoColumna(6,"--,--9");
    jt.setFormatoColumna(7,"---,--9.99");
    jt.setFormatoColumna(12,"--,--9");
    jt.setFormatoColumna(13,"---,--9.99");
    jt.setFormatoColumna(14,"B-");
    jt.setMaximumSize(new Dimension(594, 179));
    jt.setMinimumSize(new Dimension(594, 179));
    jt.setPreferredSize(new Dimension(594, 179));
    jt.setAjustarGrid(true);
    jt.setAjustarColumnas(false);
    opStock.setSelected(true);
    opStock.setText("Con Stock");
    pro_serieE.setAutoNext(true);
    opStock.setBounds(new Rectangle(5, 86, 103, 15));
    Baceptar.setBounds(new Rectangle(208, 80, 147, 24));
    stp_kilactE.setBounds(new Rectangle(449, 62, 74, 17));
    stp_unactE.setBounds(new Rectangle(334, 62, 41, 17));
    stp_feccreE.setBounds(new Rectangle(83, 62, 79, 17));
    stp_kiliniE.setBounds(new Rectangle(449, 43, 74, 17));
    stp_uniniE.setBounds(new Rectangle(334, 43, 41, 17));
    pro_numindE.setBounds(new Rectangle(487, 23, 37, 17));
    pro_nuparE.setBounds(new Rectangle(310, 23, 56, 17));
    pro_serieE.setBounds(new Rectangle(184, 23, 27, 17));
    eje_numeE.setBounds(new Rectangle(45, 23, 49, 17));
    pro_codiE.setBounds(new Rectangle(64, 4, 458, 18));
    pro_serieE.setMayusc(true);
    this.getContentPane().add(Pprinc,  BorderLayout.CENTER);
    this.getContentPane().add(statusBar,  BorderLayout.SOUTH);

    Pprinc.add(Pcabe,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    Pcabe.add(pro_codiE, null);
    Pcabe.add(cLabel1, null);
    Pcabe.add(pro_numindE, null);
    Pcabe.add(cLabel5, null);
    Pcabe.add(pro_nuparE, null);
    Pcabe.add(cLabel4, null);
    Pcabe.add(pro_serieE, null);
    Pcabe.add(cLabel3, null);
    Pcabe.add(cLabel9, null);
    Pcabe.add(stp_kiliniE, null);
    Pcabe.add(stp_uniniE, null);
    Pcabe.add(cLabel8, null);
    Pcabe.add(stp_kilactE, null);
    Pcabe.add(cLabel11, null);
    Pcabe.add(stp_unactE, null);
    Pcabe.add(cLabel7, null);
    Pprinc.add(jt,   new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    Pcabe.add(cLabel2, null);
    Pcabe.add(eje_numeE, null);
    Pcabe.add(alm_codiE, null);
    Pcabe.add(cLabel6, null);
    Pcabe.add(stp_feccreE, null);
    Pcabe.add(cLabel10, null);
    Pcabe.add(Baceptar, null);
    Pcabe.add(opStock, null);
  }
    @Override
  public void iniciarVentana() throws Exception
  {
    filtroEmpr=empPanel.getStringAccesos(dtStat, EU.usuario,true);
    Pcabe.setDefButton(Baceptar);
    Pcabe.setButton(KeyEvent.VK_F4,Baceptar);

    pro_codiE.iniciar(dtStat,this,vl,EU);
    pro_codiE.setColumnaAlias("s.pro_codi");
    eje_numeE.setColumnaAlias("eje_nume");
    pro_nuparE.setColumnaAlias("pro_nupar");
    pro_numindE.setColumnaAlias("pro_numind");
    pro_serieE.setColumnaAlias("pro_serie");
    alm_codiE.setColumnaAlias("alm_codi");
    stp_kilactE.setColumnaAlias("stp_kilact");
    stp_kiliniE.setColumnaAlias("stp_kilini");
    stp_unactE.setColumnaAlias("stp_unact");
    stp_uniniE.setColumnaAlias("stp_unini");
    stp_feccreE.setColumnaAlias("stp_feccre");
    alm_codiE.setFormato(true);
    alm_codiE.setFormato(Types.DECIMAL,"#9",2);
    Pcabe.setQuery(true);
    Pcabe.resetTexto();
    eje_numeE.setValorDec(EU.ejercicio);
    pro_serieE.setText("A");

    String s = "SELECT alm_codi,alm_nomb FROM v_almacen ORDER BY alm_codi ";
    dtStat.select(s);
    alm_codiE.addDatos(dtStat);
    opStock.setSelected(true);
    jt.cuadrarGrid();
    activarEventos();
    pro_numindE.resetCambio();
  }
  void activarEventos()
  {
    Baceptar.addActionListener(new ActionListener()
    {
            @Override
      public void actionPerformed(ActionEvent e)
      {
        consultar();
      }
    });
    pro_numindE.addFocusListener(new FocusAdapter()
    {
            @Override
      public void focusLost(FocusEvent e)
      {
        if (!pro_numindE.hasCambio())
          return;
        pro_numindE.resetCambio();
        opStock.setSelected(false);
      }
    });
    jt.addMouseListener(new MouseAdapter()
    {
      @Override
      public void mouseClicked(MouseEvent e)
      {
          if (e.getClickCount()>1)
            editStkPart();
      }
    });
  }
  void editStkPart()
  {
    if (jf==null)
        return; // Para cuando se esta en modo debug
    ejecutable prog;
    if ((prog=jf.gestor.getProceso(CreaStkPart.getNombreClase()))==null)
      return;
    CreaStkPart cm=(CreaStkPart) prog;
    cm.setProducto(jt.getValorInt(JT_ARTIC));
    cm.setAlmacen(jt.getValorInt(JT_ALMAC));
    cm.setEjercicio(jt.getValorInt(JT_EJERC));
    cm.setSerie(jt.getValString(JT_SERIE));
    cm.setLote(jt.getValorInt(JT_LOTE));
    cm.setIndividuo(jt.getValorInt(JT_INDI));
    
    cm.verKilos();
    jf.gestor.ir(cm);
    cm.ej_focus();
  }
  void consultar()
  {
    pro_numindE.resetCambio();
    new miThread("")
    {
            @Override
      public void run()
      {
       consulta0();
      }
    };
  }
  private void consulta0()
  {
    this.setEnabled(false);
    jt.setEnabled(false);
    mensaje("Buscando datos... Espere, por favor");
    try  {
      ArrayList v = new ArrayList();
      v.add(pro_codiE.getStrQuery());
      v.add(eje_numeE.getStrQuery());
      v.add(pro_nuparE.getStrQuery());
      v.add(pro_numindE.getStrQuery());
      v.add(pro_serieE.getStrQuery());
      v.add(alm_codiE.getStrQuery());
      v.add(stp_kilactE.getStrQuery());
      v.add(stp_kiliniE.getStrQuery());
      v.add(stp_unactE.getStrQuery());
      v.add(stp_uniniE.getStrQuery());
      v.add(stp_feccreE.getStrQuery());

      s = "SELECT s.pro_codi,a.pro_nomb,s.eje_nume,s.pro_serie,s.pro_nupar,"+
          " s.pro_numind,s.stp_unact,s.stp_kilact, "+
          " s.alm_codi,s.prv_codi, s.stp_feccad,  "+
          " s.stp_feccre,s.stp_unini, s.stp_kilini,s.stk_block "+
          " FROM V_stkpart as s, v_articulo as a where a.pro_codi = s.pro_codi "+
           (filtroEmpr==null?"":" and s.emp_codi in ("+filtroEmpr+")")+
          (opStock.isSelected()?" and (s.stp_unact > 0 or s.stp_kilact > 0.5 or stp_kilact < -0.5) ":"");

      s=creaWhere(s, v, false);
      s+=" order by s.pro_codi,s.pro_nupar,s.pro_numind";

      jt.removeAllDatos();

      if (!dtCon1.select(s))
      {
        this.setEnabled(true);
        mensajeErr("NO encontrados Registros para estas condiciones");
        mensaje("");
        return;
      }
      mensaje("");
      jt.setDatos(dtCon1);
      mensajeErr("Consulta ... Realizada");
      this.setEnabled(true);
      jt.setEnabled(true);
    } catch (Exception k)
    {
      Error("Error al Buscar Datos",k);
    }
  }
}
