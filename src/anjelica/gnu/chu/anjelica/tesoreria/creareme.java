package gnu.chu.anjelica.tesoreria;

import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import gnu.chu.Menu.*;
import gnu.chu.anjelica.facturacion.condBusFra;
import gnu.chu.interfaces.*;
import java.io.*;
import java.util.Date;
import net.sf.jasperreports.engine.*;

/**
 *
 * <p>Titulo: creareme</p>
 * <p>Descripción: Mantenimiento Remesas de Giros<br>
 * <p>Título: creareme</p>
 * <p>Descripción: Mantenimiento Remesas de Giros<br>
 *  Genera Ficheros segun la norma 58 y 19. <br>
 *  Tambien permite sacar un listado con la remesa generada<br>
 *  Para crear o modificar recibos se utiliza
 *  el mantenimiento de Facturas de Ventas<br>
 * Hay una opcion de Act. de Cuentas bancarias que esta oculta. Para mostrarla
 * Simplemente descomentar la linea: <br>
 *   PcondBus.add(Bactcuen, null);<br>
 * Esta opcion lo que hace es pasar los datos de cuenta bancarias del cliente a la
 * tabla de recibos a las facturas que esten el grid </p>
 * <p>Copyright: Copyright (c) 2005-2009
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
 *  los términos de la Licencia P�blica General de GNU según es publicada por
 *  la Free Software Foundation, bien de la versión 2 de dicha Licencia
 *  o bien (según su elección) de cualquier versión posterior.
 *  Este programa se distribuye con la esperanza de que sea útil,
 *  pero SIN NINGUNA GARANTÍA, incluso sin la garant�a MERCANTIL impl�cita
 *  o sin garantizar la CONVENIENCIA PARA UN PROPÓSITO PARTICULAR.
 *  V�ase la Licencia Pública General de GNU para m�s detalles.
 *  Debería haber recibido una copia de la Licencia P�blica General junto con este programa.
 *  Si no ha sido así, escriba a la Free Software Foundation, Inc.,
 *  en 675 Mass Ave, Cambridge, MA 02139, EEUU.
 * </p>
 * @author chuchi P.
 * @version 1.0
 */
public class creareme  extends ventanaPad implements PAD
{
  int tpcCodi=-1;
  int numDec=2;
  String fichero;
  JFileChooser ficeleE;
  boolean modConsulta=false;
  String diasVto[] = new String[4];
  String s;
  CPanel Pprinc = new CPanel();
  CPanel Pentra = new CPanel();
  CLabel cLabel2 = new CLabel();
  CTextField rem_fechaE = new CTextField(Types.DATE, "dd-MM-yyyy");

//  CButton Baceptar = new CButton("Aceptar");
//  CButton Bcancelar = new CButton("Cancelar");

  Cgrid jt = new Cgrid(11);
  CLabel cLabel5 = new CLabel();
  CTextField rem_codiE = new CTextField(Types.DECIMAL, "###9");
  CLabel cLabel6 = new CLabel();
  CLinkBox bat_codiE = new CLinkBox();
  CLabel cLabel7 = new CLabel();
  CTextField rem_comeE = new CTextField(Types.CHAR, "X", 100);
  condBusFra PcondBus = new condBusFra();
  CPanel Pboton = new CPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CLabel cLabel1 = new CLabel();
  CTextField rem_importE = new CTextField(Types.DECIMAL,"--,---,--9.99");
  CLabel cLabel3 = new CLabel();
  CTextField rem_numfraE = new CTextField(Types.DECIMAL,"###9");
  CButton Bbusfra = new CButton("Buscar Fras",Iconos.getImageIcon("buscar"));
  CButton BirGrid = new CButton();
  CButton BoculPanel = new CButton(Iconos.getImageIcon("insertar"));
  CButton Blistar = new CButton(Iconos.getImageIcon("print"));
  CButton BinsFra = new CButton("Buscar Fras",Iconos.getImageIcon("buscar"));
  CButton Binvert = new CButton(Iconos.getImageIcon("data-undo"));
  CLabel cLabel4 = new CLabel();
  CTextField rem_fecvtoE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CButton Bsopmag = new CButton(Iconos.getImageIcon("save"));
  CLabel cLabel8 = new CLabel();
  CTextField rem_fecpreE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel9 = new CLabel();
  CTextField rem_fecadeE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel10 = new CLabel();
  CComboBox rem_normaE = new CComboBox();
  CLabel cLabel11 = new CLabel();
  CTextField rem_direcE = new CTextField();
  CButton Bbusfic = new CButton(Iconos.getImageIcon("folder"));
  CCheckBox rem_fordosE = new CCheckBox();
  CCheckBox opIncCob = new CCheckBox();
  CButton Bactcuen = new CButton();
  CLabel cLabel12 = new CLabel();
  CTextField bat_nifsufE = new CTextField(Types.DECIMAL,"##9");
  CTextField eje_numeE = new CTextField(Types.DECIMAL, "###9");


  public creareme(EntornoUsuario eu, Principal p)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    setTitulo("Mantenimiento Remesas");

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

  public creareme(gnu.chu.anjelica.menu p, EntornoUsuario eu)
  {
    EU = eu;
    vl = p.getLayeredPane();
    setTitulo("Mantenimiento Remesas");
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
    this.setSize(new Dimension(705, 532));
    this.setVersion("(2009-03-11)");
    statusBar = new StatusBar(this);
    nav = new navegador(this, dtCons, false, modConsulta? navegador.CURYCON:navegador.NORMAL);
    Vector v = new Vector();
    v.addElement("Año"); // 0
    v.addElement("Emp"); // 1
    v.addElement("N. Fra."); // 2
    v.addElement("N.Giro"); // 3
    v.addElement("Cliente"); // 4
    v.addElement("Nombre"); // 5
    v.addElement("Fec.Fra"); // 6
    v.addElement("Importe"); // 7
    v.addElement("Fec.Vto."); // 8
    v.addElement("Inc"); // 9
    v.addElement("Num.Cuenta"); // 10
    jt.setCabecera(v);
    jt.setFormatoColumna(9, "BSN");
    jt.setAnchoColumna(new int[]    {43, 32, 59, 42, 57, 157, 77, 89, 82, 30, 100});
    jt.setAlinearColumna(new int[] {2, 2, 2, 2, 2, 0, 1, 2, 1, 1, 0});
    jt.setFormatoColumna(7, "--,---,--9.99");

    strSql="SELECT * FROM remesas order by eje_nume,rem_codi";
    iniciar(this);
    conecta();

    rem_importE.setEnabled(false);
    rem_numfraE.setEnabled(false);
    PcondBus.setMaximumSize(new Dimension(682, 184));
    PcondBus.setMinimumSize(new Dimension(682, 184));
    PcondBus.setPreferredSize(new Dimension(682, 184));
    Pentra.setMinimumSize(new Dimension(510, 95));
    Pentra.setPreferredSize(new Dimension(510, 95));

    jt.setMaximumSize(new Dimension(682, 202));
    jt.setMinimumSize(new Dimension(682, 202));
    jt.setPreferredSize(new Dimension(682, 202));
    Pboton.setBorder(BorderFactory.createRaisedBevelBorder());
    Pboton.setMaximumSize(new Dimension(251, 36));
    Pboton.setMinimumSize(new Dimension(251, 36));
    Pboton.setPreferredSize(new Dimension(251, 36));
    Pboton.setLayout(null);
    Bcancelar.setBounds(new Rectangle(120, 3, 99, 26));
    Pprinc.setMaximumSize(new Dimension(2147483647, 2147483647));
    cLabel1.setText("Imp.Fras");
    cLabel1.setBounds(new Rectangle(249, 7, 51, 16));
    rem_importE.setBounds(new Rectangle(305, 7, 88, 16));
    cLabel3.setText("NºFras");
    cLabel3.setBounds(new Rectangle(399, 7, 41, 16));
    rem_numfraE.setBounds(new Rectangle(440, 7, 48, 16));
    Bbusfra.setBounds(new Rectangle(468, 149, 107, 22));
    Bbusfra.setToolTipText("Inserta las facturas, borrando las anteriores");
    Bbusfra.setMargin(new Insets(0, 0, 0, 0));
    Bbusfra.setText("Act. Fras");
    BirGrid.setBounds(new Rectangle(480, 73, 1, 1));

    BoculPanel.setPreferredSize(new Dimension(24, 24));
    BoculPanel.setMaximumSize(new Dimension(24, 24));
    BoculPanel.setMinimumSize(new Dimension(24, 24));
    BoculPanel.setToolTipText("Ocultar/Mostrar Panel de Condiciones");

    Blistar.setPreferredSize(new Dimension(24, 24));
    Blistar.setMaximumSize(new Dimension(24, 24));
    Blistar.setMinimumSize(new Dimension(24, 24));
    Blistar.setToolTipText("Listar remesa de pantalla");


    BinsFra.setText("F4 Ins. Fras");
    BinsFra.setMargin(new Insets(0, 0, 0, 0));
    BinsFra.setBounds(new Rectangle(355, 149, 107, 22));
    BinsFra.setToolTipText("Inserta las facturas sin borrar las anteriores");
    BinsFra.setVerifyInputWhenFocusTarget(true);
    Binvert.setBounds(new Rectangle(507, 5, 63, 20));
    Binvert.setToolTipText("Invertir Facturas Selecionadas");
    Binvert.setMargin(new Insets(0, 0, 0, 0));
    Binvert.setText("Inv");
    cLabel4.setBounds(new Rectangle(356, 123, 77, 17));
    cLabel4.setText("Fecha Vto < ");
    rem_fecvtoE.setBounds(new Rectangle(436, 123, 76, 17));
    Bsopmag.setBounds(new Rectangle(494, 63, 116, 23));
    Bsopmag.setMargin(new Insets(0, 0, 0, 0));
    Bsopmag.setText("Grabar disco");
    cLabel8.setText("Fecha Present.");
    cLabel8.setBounds(new Rectangle(3, 45, 89, 17));
    rem_fecpreE.setBounds(new Rectangle(86, 45, 76, 17));
    cLabel9.setBounds(new Rectangle(176, 45, 81, 17));
    cLabel9.setText("Fecha Adeudo");
    rem_fecadeE.setBounds(new Rectangle(257, 45, 76, 17));
    cLabel10.setText("Formato");
    cLabel10.setBounds(new Rectangle(345, 45, 53, 17));
    rem_normaE.setBounds(new Rectangle(393, 44, 96, 17));
    cLabel11.setText("Directorio");
    cLabel11.setBounds(new Rectangle(4, 63, 56, 17));
    rem_direcE.setBounds(new Rectangle(59, 63, 257, 17));
    Bbusfic.setBounds(new Rectangle(317, 63, 24, 22));
    Bbusfic.setToolTipText("Buscar Fichero");
    rem_fordosE.setToolTipText("Inserta una linea en Blanco despues de cada registro");
    rem_fordosE.setSelected(true);
    rem_fordosE.setText("Ins. Linea Blanco");
    rem_fordosE.setBounds(new Rectangle(492, 44, 118, 17));
    opIncCob.setToolTipText("Incluir giros cuya factura este marcada como cobrada");
    opIncCob.setVerifyInputWhenFocusTarget(true);
    opIncCob.setSelected(true);
    opIncCob.setText("Inc. Fras Cobradas");
    opIncCob.setBounds(new Rectangle(531, 123, 140, 17));
    Bactcuen.setBounds(new Rectangle(582, 148, 97, 24));
    Bactcuen.setMargin(new Insets(0, 0, 0, 0));
    Bactcuen.setText("Act.Cuentas");
    cLabel12.setText("Sufijo Titular");
    cLabel12.setBounds(new Rectangle(342, 66, 76, 16));
    bat_nifsufE.setBounds(new Rectangle(423, 65, 37, 17));
    eje_numeE.setBounds(new Rectangle(75, 4, 38, 17));
    statusBar.add(BoculPanel, new GridBagConstraints(9, 0, 1, 2, 0.0, 0.0
                                         , GridBagConstraints.EAST,
                                         GridBagConstraints.VERTICAL,
                                         new Insets(0, 5, 0, 0), 0, 0));

    statusBar.add(Blistar, new GridBagConstraints(8, 0, 1, 2, 0.0, 0.0
                  , GridBagConstraints.EAST,
                  GridBagConstraints.VERTICAL,
                   new Insets(0, 5, 0, 0), 0, 0));
    Pentra.add(cLabel5, null);
    Pentra.add(Bsopmag, null);
    Pentra.add(cLabel7, null);
    Pentra.add(rem_comeE, null);
    Pentra.add(cLabel10, null);
    Pentra.add(rem_fecadeE, null);
    Pentra.add(cLabel9, null);
    Pentra.add(cLabel8, null);
    Pentra.add(rem_fecpreE, null);

    Pprinc.setLayout(gridBagLayout1);
    Pentra.setBorder(BorderFactory.createRaisedBevelBorder());
    Pentra.setMaximumSize(new Dimension(510, 95));
    Pentra.setLayout(null);
    cLabel2.setText("Fecha Remesa");
    cLabel2.setBounds(new Rectangle(201, 2, 81, 17));
    rem_fechaE.setBounds(new Rectangle(284, 2, 76, 17));
    Baceptar.setBounds(new Rectangle(6, 3, 103, 26));
    Baceptar.setMnemonic('A');
    Baceptar.setText("Aceptar F4");



    cLabel5.setText("N� Remesa");
    cLabel5.setBounds(new Rectangle(4, 5, 64, 15));
    rem_codiE.setBounds(new Rectangle(114, 4, 48, 17));
    cLabel6.setText("Banco");
    cLabel6.setBounds(new Rectangle(369, 4, 42, 17));
    bat_codiE.setAncTexto(30);
    bat_codiE.setBounds(new Rectangle(408, 3, 193, 19));
    cLabel7.setText("Comentario");
    cLabel7.setBounds(new Rectangle(4, 23, 69, 18));
    rem_comeE.setBounds(new Rectangle(76, 22, 495, 18));
    Pboton.add(Baceptar, null);
    Pboton.add(rem_importE, null);
    Pboton.add(cLabel1, null);
    Pboton.add(cLabel3, null);
    Pboton.add(rem_numfraE, null);
    Pboton.add(Bcancelar, null);
    Pboton.add(Binvert, null);
    PcondBus.setBorder(BorderFactory.createLoweredBevelBorder());

    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
    Pprinc.add(Pboton,         new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 328, 0));
    Pprinc.add(jt,    new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));


    Pprinc.add(PcondBus,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    PcondBus.add(rem_fecvtoE, null);
    PcondBus.add(cLabel4, null);
    PcondBus.add(opIncCob, null);
    PcondBus.add(BinsFra, null);
    PcondBus.add(Bbusfra, null);
//    PcondBus.add(Bactcuen, null);
    Pprinc.add(Pentra,         new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 104, 0));
    Pentra.add(rem_direcE, null);
    Pentra.add(cLabel11, null);
    Pentra.add(BirGrid, null);
    Pentra.add(rem_normaE, null);
    Pentra.add(rem_fordosE, null);
    Pentra.add(Bbusfic, null);
    Pentra.add(cLabel12, null);
    Pentra.add(bat_nifsufE, null);
    Pentra.add(bat_codiE, null);
    Pentra.add(cLabel6, null);
    Pentra.add(rem_fechaE, null);
    Pentra.add(cLabel2, null);
    Pentra.add(rem_codiE, null);
    Pentra.add(eje_numeE, null);
  }

  public void iniciarVentana() throws Exception
  {
    PcondBus.setButton(KeyEvent.VK_F4,BinsFra);
    PcondBus.setVisible(false);
    Pentra.setDefButton(Baceptar);
    Pentra.setButton(KeyEvent.VK_F4,Baceptar);
    PcondBus.iniciar(dtStat,this,vl,EU);
    activarEventos();
    activar(false);
//    verDatos();
    eje_numeE.setColumnaAlias("eje_nume");
    rem_codiE.setColumnaAlias("rem_codi");
    bat_codiE.setColumnaAlias("bat_codi");
    rem_comeE.setColumnaAlias("rem_come");
    rem_fechaE.setColumnaAlias("rem_fecha");
    rem_importE.setColumnaAlias("rem_import");
    rem_numfraE.setColumnaAlias("rem_numfra");
    rem_fecpreE.setColumnaAlias("rem_fecpre");
    rem_fecadeE.setColumnaAlias("rem_fecade");
    rem_normaE.setColumnaAlias("rem_norma");
    bat_nifsufE.setColumnaAlias("bat_nifsuf");
  }

  void actSufBanco()  throws Exception
  {
    s = "SELECT  * FROM bancteso WHERE bat_codi = " + bat_codiE.getValorInt();
    if (!dtStat.select(s))
      return;
    bat_nifsufE.setValorInt(dtStat.getInt("bat_nifsuf", true));
  }
  void activarEventos()
  {
    bat_codiE.addFocusListener(new FocusAdapter()
    {
      public void focusLost(FocusEvent e)
      {
        try {
          if (bat_codiE.hasCambio() && !bat_codiE.getQuery())
          {
            actSufBanco();
            bat_codiE.resetCambio();
          }
        }catch (Exception k)
        {
          Error("Error al Buscar banco de tesoreria",k);
        }
      }

    });
    Bactcuen.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Bactcuen_actionPerformed();
      }
    });
    BirGrid.addFocusListener(new FocusAdapter()
    {
      public void focusGained(FocusEvent e)
      {
        jt.requestFocusInicio();
      }

    });

    Bbusfra.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Bbusfra_actionPerformed(true);
      }
    });
    BinsFra.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Bbusfra_actionPerformed(false);
      }
    });

    BoculPanel.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        if (jt.isEnabled())
          PcondBus.setVisible(!PcondBus.isVisible());
      }
    });
    jt.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent e)
      {
        if (!jt.isEnabled() || jt.getSelectedColumn()!=9 )
          return;
        jt.setValor(new Boolean(!jt.getValBoolean(9)));
        recalcTot();
      }
    });
    Binvert.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        if (!jt.isEnabled())
          return;

        int nRow = jt.getRowCount();
        for (int n = 0; n < nRow; n++)
        {
          jt.setValor(new Boolean(!jt.getValBoolean(n, 9)), n, 9);
        }
        recalcTot();
      }
    });
    Bsopmag.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Bsopmag_actionPerformed();
      }
    });
    Bbusfic.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Bbusfic_actionPerformed();
      }
    });
    Blistar.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Blistar_actionPerformed();
      }
    });

  }
  void Bbusfic_actionPerformed() {
  try
  {
    configurarFile();
    int returnVal = ficeleE.showOpenDialog(this);
    if(returnVal == JFileChooser.APPROVE_OPTION)
       rem_direcE.setText(ficeleE.getSelectedFile().getParent());

  }
  catch (Exception k)
  {
    fatalError("error al elegir el fichero", k);
  }

}
void configurarFile() throws Exception
{
    if (ficeleE != null)
      return;
    ficeleE = new JFileChooser();
    ficeleE.setName("Elegir Directorio");
    ficeleE.setCurrentDirectory(new java.io.File(EU.dirTmp));
}

  void recalcTot()
  {
    int nGiros=0;
    double impGiros=0;
    int nRow=jt.getRowCount();

    for (int n=0;n<nRow;n++)
    {
      if (jt.getValBoolean(n,9))
      {
        nGiros++;
        impGiros += jt.getValorDec(n, 7);
      }
    }
    rem_numfraE.setValorInt(nGiros);
    rem_importE.setValorDec(impGiros);
  }

  public void PADPrimero(){verDatos();}

  public void PADAnterior(){verDatos();}

  public void PADSiguiente(){verDatos();}

  public void PADUltimo(){verDatos();}

  public void PADQuery(){
    eje_numeE.setEnabled(true);
    rem_codiE.setEnabled(true);
    Pentra.setQuery(true);
    activar(true);
    jt.setEnabled(false);
    rem_direcE.setEnabled(false);
    rem_fordosE.setEnabled(false);
    eje_numeE.setValorInt(EU.ejercicio);
    Pentra.resetTexto();
    rem_codiE.requestFocus();
    mensaje("Introduzca Criterios de Busqueda");
  }

  public void ej_query1(){
    Component c=Pentra.getErrorConf();
    if (c!=null)
    {
      mensajeErr("Condiciones de busqueda NO son validas");
      c.requestFocus();
      return;
    }
    Vector v = new Vector();
    v.add(eje_numeE.getStrQuery());
    v.add(rem_codiE.getStrQuery());
    v.add(bat_codiE.getStrQuery());
    v.add(rem_comeE.getStrQuery());
    v.add(bat_codiE.getStrQuery());
    v.add(rem_fechaE.getStrQuery());
    v.add(rem_importE.getStrQuery());
    v.add(rem_numfraE.getStrQuery());
    v.add(bat_nifsufE.getStrQuery());
    Pentra.setQuery(false);
    try
   {
     s = "SELECT * FROM remesas ";
     s = creaWhere(s, v, true);
     s += " ORDER BY eje_nume,rem_codi";
//     debug("s: "+s);
       mensaje("Espere, por favor ... buscando datos");
     if (!dtCon1.select(s))
     {
       msgBox("No encontrados REMESAS con estos criterios");
       mensaje("");
       verDatos();
       activaTodo();
       return;
     }
     strSql = s;
     rgSelect();

     mensaje("");
     mensajeErr("Nuevas Condiciones ... Establecidas");
   }
   catch (Exception k)
   {
     Error("Error al buscar datos", k);
   }

   activaTodo();
//   verDatos();
   nav.pulsado=navegador.NINGUNO;
  }

  public void canc_query(){
    activaTodo();
    Pentra.setQuery(false);
    mensajeErr("Consulta .. CANCELADA");
    verDatos();
  }

  public void PADEdit(){
    if (!bloqueaRegistro())
      return;

    bat_codiE.resetCambio();
    activar(true);
    eje_numeE.setEnabled(false);
    rem_codiE.setEnabled(false);
    PcondBus.setVisible(true);
    mensaje("Editando remesa ...");
    PcondBus.requestFocus();
  }
  boolean bloqueaRegistro()
   {
     try
     {
       if (!setBloqueo(dtAdd, "remesas", eje_numeE.getText()+"-"+ rem_codiE.getText()))
       {
         msgBox(msgBloqueo);
         nav.pulsado = navegador.NINGUNO;
         activaTodo();
         return false;
       }

       if (!dtAdd.select("select * from remesas where eje_nume = "+eje_numeE.getValorInt()+
                         " and rem_codi = " +rem_codiE.getText(), true))
       {
         mensajeErr("Registro ha sido borrado");
         resetBloqueo(dtAdd, "remesas",eje_numeE.getText()+"-"+ rem_codiE.getText());
         activaTodo();
         mensaje("");
         return false;
       }
     }
     catch (Exception k)
     {
       Error("Error al bloquear el registro", k);
       return false;
     }
     return true;
   }


  public void ej_edit1()
  {
    try
    {
      dtAdd.edit();
      deleReme(); // Borrar datos de remesa.
      actTablas();
      resetBloqueo(dtAdd, "remesas",eje_numeE.getText()+"-"+ rem_codiE.getText(),false);
      ctUp.commit();
      verDatos();
    } catch (Exception k)
    {
      Error("Error al Insertar Remesas", k);
      return;
    }
    PcondBus.setVisible(false);
    activaTodo();
    nav.pulsado = navegador.NINGUNO;
    mensaje("");
    mensajeErr("Remesa .. MODIFICADA");
  }

  public void canc_edit(){
    try {
      resetBloqueo(dtAdd, "remesas",eje_numeE.getText()+"-"+ rem_codiE.getText());
    } catch (Exception k){}
    PcondBus.setVisible(false);
    mensajeErr("Modificacion dd remesa .. CANCELADA");
    mensaje("");
    activaTodo();
    verDatos();
    nav.pulsado = navegador.NINGUNO;
  }


  public void PADAddNew(){
    try
    {
      rem_codiE.setValorInt(getMaxNumRemesa());

      jt.removeAllDatos();
      activar(true);
      eje_numeE.setValorInt(EU.ejercicio);
      eje_numeE.setEnabled(false);
      rem_codiE.setEnabled(false);
      rem_fechaE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
      rem_fecpreE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
      rem_fecadeE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
      bat_codiE.resetTexto();
      rem_comeE.resetTexto();
      bat_codiE.setHasCambio(true);
      actSufBanco();
      PcondBus.setVisible(true);
    }
    catch (Exception k)
    {
      Error("Error al buscar Numero de remesa", k);
    }

    mensaje("Creando NUEVA remesa ...");
    PcondBus.requestFocus();
  }
  int getMaxNumRemesa()  throws Exception
  {
    s = "SELECT * FROM v_numerac where emp_codi = " + EU.em_cod +
        " AND eje_nume = " + EU.ejercicio;
    if (!dtStat.select(s))
      throw new Exception("No encontrado Guia de Numeraciones\n"+s);
    return dtStat.getInt("num_remesa") + 1;
  }
  public boolean checkEdit()
  {
    return checkAddNew();
  }
  public boolean checkAddNew()
  {
    if (! bat_codiE.controla())
    {
      mensajeErr("Banco ... NO VALIDO");
      return false;
    }
    if (rem_fechaE.isNull() || rem_fechaE.getError())
    {
      mensajeErr("Fecha de Remesa .. NO VALIDA");
      return false;
    }
    recalcTot();
    if (rem_numfraE.getValorInt()==0)
    {
      mensajeErr("Selecione alguna Factura");
      return false;
    }
    if (rem_fecpreE.getError())
    {
      mensajeErr("Fecha de Presentacion NO es valida");
      rem_fecpreE.requestFocus();
      return false;
    }
    if (rem_fecadeE.getError())
    {
      mensajeErr("Fecha de Adeudo NO es valida");
      rem_fecadeE.requestFocus();
      return false;
    }
    try {
      // Comprobar que los numeros de cuenta sean validos.
      int nRow = jt.getRowCount();
      String mensajeCuenta="";
      for (int n = 0; n < nRow; n++)
      {
        if (!jt.getValBoolean(n, 9))
          continue;
        if (jt.getValorDec(n,7)<0)
        {
          mensajeErr("Recibo de nFra:"+jt.getValorInt(n,2)+" de cliente: "+jt.getValorInt(n,4)+" CON importe NEGATIVO");
          jt.requestFocus(n,0);
          return false;
        }
        s = "SELECT * FROM v_recibo where  emp_codi = " + jt.getValorInt(n, 1) +
            " and eje_nume = " + jt.getValorInt(n, 0) +
            " and fvc_nume = " + jt.getValorInt(n, 2) +
            " and rec_nume = " + jt.getValorInt(n, 3);
        dtStat.select(s);
        String cuenta=Formatear.format(dtStat.getInt("ban_codi",true),"9999")+
            Formatear.format(dtStat.getInt("rec_baofic",true),"9999");
        if (Formatear.getNumControl(Integer.parseInt(cuenta)) !=
            Integer.parseInt(Formatear.format(dtStat.getInt("rec_badico",true), "99").
                             substring(0, 1)))
        {
          mensajeCuenta+="Digito control NO VALIDO para banco y sucursal. \nFra:"+jt.getValorInt(n,2)+" de cliente: "+jt.getValorInt(n,4)+"\n";
//          jt.requestFocus(n,0);
//          return false;
        }
        if (Formatear.getNumControl( dtStat.getDouble("rec_bacuen",true)) !=
            Integer.parseInt(Formatear.format(dtStat.getInt("rec_badico",true), "99").
                             substring(1, 2)))
        {
          mensajeCuenta+="Digito control NO VALIDO para Num. Cuenta \nFra:"+jt.getValorInt(n,2)+" de cliente: "+jt.getValorInt(n,4)+"\n";
//          jt.requestFocus(n,0);
//          return false;
        }
      }
      if (! mensajeCuenta.equals(""))
      {
        int resp;
       resp=mensajes.mensajePreguntar("Errores en Cuentas bancarias\n\n"+
                                mensajeCuenta,this);
        if (resp!=mensajes.YES)
            return false;
      }
    } catch (Exception k)
    {
      Error("Error al Chequear Numeros de cuenta",k);
    }

    return true;
  }

  public void ej_addnew1()
  {
    try {

      // Actualizo la NUMERACION
      s = "SELECT * FROM v_numerac where emp_codi = " + EU.em_cod +
          " AND eje_nume = " + EU.ejercicio;
      if (!dtAdd.select(s,true))
        throw new Exception("No encontrado Guia de Numeraciones\n" + s);
      rem_codiE.setValorInt(dtAdd.getInt("num_remesa") + 1);
      dtAdd.edit();
      dtAdd.setDato("num_remesa",rem_codiE.getValorInt());
      dtAdd.update(stUp);

      // Inserto el registro en tabla remesas
      dtAdd.addNew("remesas");
      dtAdd.setDato("eje_nume", eje_numeE.getValorInt());
      dtAdd.setDato("rem_codi", rem_codiE.getValorInt());
      actTablas();
      ctUp.commit();
      strSql="SELECT * FROM remesas where eje_nume = "+ eje_numeE.getValorInt()+
          " and rem_codi = "+rem_codiE.getValorDec();
      rgSelect();
    } catch (Exception k)
    {
      Error("Error al Insertar Remesas",k);
      return;
    }
    PcondBus.setVisible(false);
    activaTodo();
    nav.pulsado=navegador.NINGUNO;
    mensaje("");
    mensajeErr("Remesa .. INSERTADA");
  }
  void actTablas() throws Exception
  {
    double impCob;
    dtAdd.setDato("rem_fecha", rem_fechaE.getText(), "dd-MM-yyyy");
    dtAdd.setDato("bat_codi", bat_codiE.getValorInt());
    dtAdd.setDato("rem_come", rem_comeE.getText());
    dtAdd.setDato("rem_import", rem_importE.getValorDec());
    dtAdd.setDato("rem_numfra", rem_numfraE.getValorDec());
    dtAdd.setDato("rem_fecpre", rem_fecpreE.getText(), "dd-MM-yyyy");
    dtAdd.setDato("rem_fecade", rem_fecadeE.getText(), "dd-MM-yyyy");
    dtAdd.setDato("rem_direc", rem_direcE.getText() );
    dtAdd.setDato("rem_norma", rem_normaE.getValor());
    dtAdd.setDato("bat_nifsuf", bat_nifsufE.getValorInt());
    dtAdd.setDato("rem_fordos", rem_fordosE.isSelected()?"S":"N");
    dtAdd.update(stUp);
    int nRow = jt.getRowCount();
    for (int n = 0; n < nRow; n++)
    {
      if (!jt.getValBoolean(n, 9))
        continue;
      s = "SELECT * FROM v_recibo WHERE eje_nume = " + jt.getValorInt(n, 0) +
          " and emp_codi = " + jt.getValorInt(n, 1) +
          " and fvc_nume = " + jt.getValorInt(n, 2)+
          " and rec_nume = "+jt.getValorInt(n,3);
      if (!dtAdd.select(s,true))
        throw new Exception("No encontrado Recibo:\n " + s);
      dtAdd.edit();
      dtAdd.setDato("rem_ejerc",eje_numeE.getValorInt());
      dtAdd.setDato("rem_codi", rem_codiE.getValorInt());
      dtAdd.setDato("bat_codi", bat_codiE.getValorInt());
      dtAdd.update(stUp);
      // Inserto el cobros.
      if (tpcCodi!=1)
      {
        dtAdd.addNew("v_cobros");
        dtAdd.setDato("emp_codi", jt.getValorInt(n, 1));
        dtAdd.setDato("cob_ano", EU.ejercicio);
        dtAdd.setDato("cob_anofac", jt.getValorInt(n, 0));
        dtAdd.setDato("fac_nume", jt.getValorInt(n, 2));
        dtAdd.setDato("cob_serie", "A");
        dtAdd.setDato("alb_nume", 0);
        dtAdd.setDato("tpc_codi", tpcCodi);
        dtAdd.setDato("usu_nomb", EU.usuario);
        dtAdd.setDato("cob_feccob", rem_fecpreE.getText(), "dd-MM-yyyy");
        dtAdd.setDato("cob_horcob", (Date) null);
        dtAdd.setDato("cob_obser", "REMESA " + rem_codiE.getText());
        dtAdd.setDato("rem_ejerc",eje_numeE.getValorInt());
        dtAdd.setDato("rem_codi", rem_codiE.getValorInt());
        dtAdd.setDato("cob_trasp", 0);
        dtAdd.setDato("cob_fecvto", jt.getValString(n, 8), "dd-MM-yyyy");
        dtAdd.setDato("cob_impor", jt.getValorDec(n, 7));
        dtAdd.update(stUp);
        s = "SELECT * FROM v_facvec "+
             " WHERE emp_codi = " +jt.getValorInt(n, 1) +
             " and fvc_ano = " + jt.getValorInt(n, 0)  +
             " and fvc_nume = " +jt.getValorInt(n, 2);
         if (dtAdd.select(s,true))
         {
           dtAdd.edit();
           impCob=dtAdd.getDouble("fvc_impcob")+jt.getValorDec(n, 7);
           dtAdd.setDato("fvc_impcob",dtAdd.getDouble("fvc_impcob")+jt.getValorDec(n, 7));
           dtAdd.setDato("fvc_cobrad",impCob>=dtAdd.getDouble("fvc_sumtot")-2?-1:0);
           dtAdd.update(stUp);
         }
         else
           throw new Exception("Factura: "+jt.getValorInt(n, 2)+" NO ENCONTRADA");
      }
    }

  }
  public void canc_addnew(){
    PcondBus.setVisible(false);
    mensajeErr("Creacion de  Nueva remesa .. CANCELADA");
    mensaje("");
    activaTodo();
    nav.pulsado=navegador.NINGUNO;
    verDatos();
  }

  public void PADDelete(){
    if (!bloqueaRegistro())
      return;
    Bsopmag.setEnabled(false);
    Baceptar.setEnabled(true);
    Bcancelar.setEnabled(true);
    mensaje("Borrando  remesa ...");
    Bcancelar.requestFocus();
  }
  public void ej_delete1(){
    try
      {
        dtAdd.delete(stUp); // borra registro de remesa
        deleReme();
        resetBloqueo(dtAdd, "remesas", eje_numeE.getValorInt()+"-"+rem_codiE.getText(),false);
        ctUp.commit();
        rgSelect();
      } catch (Exception k)
      {
        Error("Error al Insertar Remesas", k);
        return;
      }
      activaTodo();
      nav.pulsado = navegador.NINGUNO;
      verDatos();
      mensaje("");
      mensajeErr("Remesa .. MODIFICADA");
  }
  /**
   * Borra los cobros asignados a la remesa activa
   *
   * @throws SQLException
   */
  void deleReme() throws SQLException
  {
    // Anulo LOS COBROS
    double impCob;
    if (tpcCodi != 1)
    {
      s = "SELECT * FROM v_recibo WHERE eje_nume = "+eje_numeE.getValorInt()+
         " and  rem_codi = " + rem_codiE.getValorInt();
      if (dtCon1.select(s))
      {
        do
        {
          s = "SELECT sum(cob_impor) as cob_impor FROM v_cobros " +
              "  WHERE emp_codi = " + dtCon1.getInt("emp_codi") +
              " and cob_anofac = " + dtCon1.getInt("eje_nume") +
              " and fac_nume = " + dtCon1.getInt("fvc_nume") +
              " and rem_ejerc = "+eje_numeE.getValorInt()+
              " AND rem_codi = " + rem_codiE.getValorInt();
          dtBloq.select(s);
          impCob = dtBloq.getDouble("cob_impor",true);
          if (impCob != 0)
          {
            s = "DELETE FROM v_cobros WHERE emp_codi = " +
                dtCon1.getInt("emp_codi") +
                " and cob_anofac = " + dtCon1.getInt("eje_nume") +
                " and fac_nume = " + dtCon1.getInt("fvc_nume") +
                " and rem_ejerc= "+eje_numeE.getValorInt()+
                " AND rem_codi = " + rem_codiE.getValorInt();
            stUp.executeUpdate(s);

              s = "UPDATE v_facvec set fvc_impcob =  fvc_impcob - " + impCob +
                ", fvc_cobrad = 0 " + // NO cobrado
                " WHERE emp_codi = " + dtCon1.getInt("emp_codi") +
                " and fvc_ano = " + dtCon1.getInt("eje_nume") +
                " and fvc_nume = " + dtCon1.getInt("fvc_nume");
            stUp.executeUpdate(s);
          }
        } while (dtCon1.next());
      }
    }

    s="UPDATE v_recibo set bat_codi =0, rem_ejerc = 0, rem_codi = 0 WHERE rem_codi = "+rem_codiE.getValorInt();
    stUp.executeUpdate(s);
  }
  public void canc_delete(){
    try
    {
      resetBloqueo(dtAdd, "remesas", eje_numeE.getValorInt()+"-"+rem_codiE.getText());
    }
    catch (Exception k)
    {}
    PcondBus.setVisible(false);
    mensajeErr("BORRADO de remesa .. CANCELADA");
    mensaje("");
    activaTodo();
    nav.pulsado = navegador.NINGUNO;
    verDatos();
  }


  public void activar(boolean b){
    eje_numeE.setEnabled(b);
    rem_codiE.setEnabled(b);
    bat_codiE.setEnabled(b);
    rem_comeE.setEnabled(b);
    rem_fechaE.setEnabled(b);
    BirGrid.setEnabled(b);
    rem_fecadeE.setEnabled(b);
    rem_fecpreE.setEnabled(b);
    rem_normaE.setEnabled(b);
    rem_direcE.setEnabled(b);
    rem_fordosE.setEnabled(b);
    Bsopmag.setEnabled(b);
    Bbusfic.setEnabled(b);
    jt.setEnabled(b);
    bat_nifsufE.setEnabled(b);
    Binvert.setEnabled(b);
    Baceptar.setEnabled(b);
    Bcancelar.setEnabled(b);
    Blistar.setEnabled(!b);
  }

  void verDatos()
  {
    try {
      if (dtCons.getNOREG())
        return;
      eje_numeE.setValorInt(dtCons.getInt("eje_nume"));
      rem_codiE.setValorInt(dtCons.getInt("rem_codi"));
      s="SELECT * FROM remesas WHERE eje_nume = "+eje_numeE.getValorInt()+
          " and rem_codi = "+rem_codiE.getValorInt();
      if (dtBloq.select(s))
      {
        bat_codiE.setText(dtBloq.getString("bat_codi"));
        rem_comeE.setText(dtBloq.getString("rem_come"));
        rem_fechaE.setText(dtBloq.getFecha("rem_fecha","dd-MM-yyyy"));
        rem_fecpreE.setText(dtBloq.getFecha("rem_fecpre","dd-MM-yyyy"));
        rem_fecadeE.setText(dtBloq.getFecha("rem_fecade","dd-MM-yyyy"));
        rem_normaE.setValor(dtBloq.getString("rem_norma"));
        rem_direcE.setText(dtBloq.getString("rem_direc"));
        rem_fordosE.setSelected(dtBloq.getString("rem_fordos").equals("S"));
        bat_nifsufE.setValorInt(dtBloq.getInt("bat_nifsuf",true));
      }
      else
      {
        bat_codiE.setText("");
        eje_numeE.resetTexto();
        rem_codiE.resetTexto();
        rem_fechaE.resetTexto();
        rem_fecpreE.resetTexto();
        rem_fecadeE.resetTexto();
        rem_normaE.resetTexto();
        rem_direcE.resetTexto();
        rem_fordosE.resetTexto();
        msgBox("Remesa NO ENCONTRADA");
      }
      s="SELECT c.*,re.rec_nume,re.rec_import,rec_fecvto,cl.cli_nomb,"+
          " re.ban_codi,re.rec_baofic,re.rec_badico,re.rec_bacuen, "+
          " cl.cli_nomco,fp.fpa_nomb "+
          " FROM v_facvec as c,clientes as cl,v_forpago as fp,v_recibo as re "+
          " WHERE  c.cli_codi = cl.cli_codi "+
          " and fp.fpa_codi = cl.fpa_codi "+
          " and re.rem_ejerc = "+eje_numeE.getValorInt()+
          " and re.rem_codi = "+rem_codiE.getValorInt()+
          " and re.emp_codi = c.emp_codi "+
          " and re.eje_nume = c.fvc_ano "+
          " and re.fvc_nume = c.fvc_nume "+
          " ORDER BY rec_fecvto,c.cli_codi,c.emp_codi, c.fvc_ano,c.fvc_nume,re.rec_nume";
      jt.removeAllDatos();
      if (!dtCon1.select(s))
      {
        mensajeErr("No encontrados giros para esta remesa");
        return;
      }
      int nGiros=0;
      double impGiros=0;
      do
      {
        Vector v = new Vector();
        v.addElement(dtCon1.getString("fvc_ano"));
        v.addElement(dtCon1.getString("emp_codi"));
        v.addElement(dtCon1.getString("fvc_nume"));
        v.addElement(dtCon1.getString("rec_nume"));
        v.addElement(dtCon1.getString("cli_codi"));
        v.addElement(dtCon1.getString("cli_nomb")+" ("+dtCon1.getString("cli_nomco")+")");
        v.addElement(dtCon1.getFecha("fvc_fecfra", "dd-MM-yyyy"));
        v.addElement(dtCon1.getString("rec_import"));
        v.addElement(dtCon1.getFecha("rec_fecvto", "dd-MM-yyyy"));
        v.addElement("S");
        v.addElement(Formatear.format(dtCon1.getInt("ban_codi",true),"9999")+"-"+
                     Formatear.format(dtCon1.getInt("rec_baofic",true),"9999")+"-"+
                     Formatear.format(dtCon1.getInt("rec_badico",true),"99")+"-"+
                     Formatear.format(dtCon1.getDouble("rec_bacuen",true),"9999999999"));
        jt.addLinea(v);
        nGiros++;
        impGiros += dtCon1.getDouble("rec_import");
      } while (dtCon1.next());
      rem_numfraE.setValorDec(nGiros);
      rem_importE.setValorDec(impGiros);
      jt.requestFocusInicio();
      if (nGiros!=dtBloq.getInt("rem_numfra"))
        mensajes.mensajeAviso("Numero de Facturas NO COINCIDE CON el guardado");
      if (rem_importE.getValorDec() != dtBloq.getDouble("rem_import"))
        mensajes.mensajeAviso("Importe  de Facturas: "+impGiros+"  NO COINCIDE CON el guardado: "+dtCons.getDouble("rem_import"));

    } catch (Exception k)
    {
      Error("Error al Ver Datos de Remesa",k);
    }
  }

  void Bbusfra_actionPerformed(boolean borDatos)
  {
    s="SELECT c.*,re.rec_nume,re.rec_import,rec_fecvto,cl.cli_nomb,fp.fpa_nomb, "+
        " re.ban_codi,re.rec_baofic,re.rec_badico,re.rec_bacuen "+
        " FROM v_facvec as c,clientes as cl,v_forpago as fp,v_recibo as re "+
        " WHERE  c.cli_codi = cl.cli_codi "+
        " and fp.fpa_codi = cl.fpa_codi "+
        " and (re.rem_codi = 0  "+
        (nav.pulsado == navegador.EDIT?
        " or ( re.eje_nume = "+eje_numeE.getValorInt()+
        " and  re.rem_codi = "+rem_codiE.getValorInt()+")":"")+")"+
        " and re.emp_codi = c.emp_codi "+
        " and re.eje_nume = c.fvc_ano "+
        " and re.fvc_nume = c.fvc_nume "+
        " and re.fvc_serie = c.fvc_serie "+
        PcondBus.getCondWhere()+
        (! opIncCob.isSelected()?" and c.fvc_cobrad = 0 ":"")+
        (!rem_fecvtoE.isNull()?" and rec_fecvto <= to_date('"+ rem_fecvtoE.getText()+"','dd-MM-yyyy')":"")+
        " ORDER BY c.emp_codi, c.fvc_ano,c.fvc_nume,re.rec_nume";
//    debug("s: "+s);
    if (borDatos)
      jt.removeAllDatos();
    rem_numfraE.setValorDec(0);
    rem_importE.setValorDec(0);

    try {
      if (!dtCon1.select(s))
      {
        mensajeErr("No encontrados Giros para estas condiciones");
        return;
      }
      int nRow=jt.getRowCount();
      int n;
      boolean swEnc=false;
      do
      {
        swEnc=false;
        for (n=0;n<nRow;n++)
        {
          if (jt.getValorInt(n,0) == dtCon1.getInt("fvc_ano") &&
              jt.getValorInt(n,1) == dtCon1.getInt("emp_codi")  &&
              jt.getValorInt(n,2) == dtCon1.getInt("fvc_nume")  &&
              jt.getValorInt(n,3) == dtCon1.getInt("rec_nume"))
          {
            swEnc = true; // Linea duplicada
            break;
          }
        }
        if (swEnc)
          continue;
        Vector v= new Vector();
        v.addElement(dtCon1.getString("fvc_ano"));
        v.addElement(dtCon1.getString("emp_codi"));
        v.addElement(dtCon1.getString("fvc_nume"));
        v.addElement(dtCon1.getString("rec_nume"));
        v.addElement(dtCon1.getString("cli_codi"));
        v.addElement(dtCon1.getString("cli_nomb"));
        v.addElement(dtCon1.getFecha("fvc_fecfra","dd-MM-yyyy"));
        v.addElement(dtCon1.getString("rec_import"));
        v.addElement(dtCon1.getFecha("rec_fecvto","dd-MM-yyyy"));
        v.addElement("S");
        v.addElement(Formatear.format(dtCon1.getInt("ban_codi",true),"9999")+"-"+
               Formatear.format(dtCon1.getInt("rec_baofic",true),"9999")+"-"+
               Formatear.format(dtCon1.getInt("rec_badico",true),"99")+"-"+
               Formatear.format(dtCon1.getDouble("rec_bacuen",true),"9999999999"));

        jt.addLinea(v);
      } while (dtCon1.next());
      recalcTot();
      jt.requestFocusInicio();
    } catch (Exception k)
    {
      Error("Error al buscar giros",k);
    }

  }
  void Bsopmag_actionPerformed()
  {
    try {
      if (rem_fecpreE.isNull() || rem_fecpreE.getError())
      {
        mensajeErr("Fecha de Presentacion NO es valida");
        rem_fecpreE.requestFocus();
        return;
      }
      if (rem_fecadeE.isNull() || rem_fecadeE.getError())
      {
        mensajeErr("Fecha de Adeudo NO es valida");
        rem_fecadeE.requestFocus();
        return;
      }
      if (rem_direcE.isNull())
      {
        mensajeErr("Introduzca Directorio donde guardar el fichero");
        rem_direcE.requestFocus();
        return;
      }
      if (!checkAddNew())
        return;
      if (nav.pulsado== navegador.ADDNEW)
      {
        ej_addnew1();
        if (nav.pulsado== navegador.ADDNEW)
          return;
      }
      if (nav.pulsado== navegador.EDIT)
      {
        ej_edit1();
        if (nav.pulsado== navegador.EDIT)
          return;
      }
      if (rem_normaE.getValor().equals("19_1"))
        creaNorma19_1();
      if (rem_normaE.getValor().equals("58_6"))
        creaNorma58_6();
    } catch (Exception k)
    {
      Error("Error al Generar fichero",k);
    }
  }
  void creaNorma19_1() throws Exception
  {
    String fecFich=rem_fecpreE.getFecha("ddMMyy");
    String fechAdeu=rem_fecadeE.getFecha("ddMMyy");
    String procedimiento="01";
    String finLinea="\r\n";
    if (rem_fordosE.isSelected())
      finLinea+="\r\n";
    int importe=0;
    int impTot;
    int nGiros;
    fichero=rem_direcE.getText()+"/remesa19_1.dat";
    s="SELECT  * FROM bancteso WHERE bat_codi = "+bat_codiE.getValorInt();
     if (! dtStat.select(s))
       throw new Exception("No encontrado banco de Tesoreria:\n"+s);

    String codOrdenante=Formatear.ajusIzq(dtStat.getString("bat_nif"),9)+
        Formatear.format(bat_nifsufE.getValorInt(),"999");
    FileWriter fr = new FileWriter(fichero, false);
    // Imprimo la Cabecera

    String sOut;
    sOut="5180"+codOrdenante+
        fecFich+Formatear.space(6)+
        Formatear.ajusIzq(dtStat.getString("bat_titcue"),40)+
        Formatear.space(20)+
        Formatear.format(dtStat.getString("bat_cucoba"),"9999")+
        Formatear.format(dtStat.getString("bat_cusuba"),"9999")+
        Formatear.space(12)+Formatear.space(40)+Formatear.space(14)+finLinea;
    fr.write(sOut);
    sOut="5380"+codOrdenante+
        fecFich+fechAdeu+Formatear.ajusIzq(dtStat.getString("bat_titcue"),40)+
        Formatear.format(dtStat.getString("bat_cucoba"),"9999")+
        Formatear.format(dtStat.getString("bat_cusuba"),"9999")+
        Formatear.format(dtStat.getString("bat_digico"),"99")+
        Formatear.format(dtStat.getDouble("bat_numcue"),"9999999999")+
        Formatear.space(8)+procedimiento+Formatear.space(10)+
        Formatear.space(40)+Formatear.space(14)+finLinea;
    fr.write(sOut);

    s="SELECT c.*,cl.cue_codi,cl.cli_codi,cl.cli_nomb,cl.cli_nomco,"+
        " re.rec_nume,re.rec_import,rec_fecvto,"+
        " re.ban_codi,re.rec_baofic,re.rec_badico,re.rec_bacuen "+
        " FROM v_facvec as c,clientes as cl,v_recibo as re "+
        " WHERE  c.cli_codi = cl.cli_codi "+
        " and re.rem_ejerc = "+eje_numeE.getValorInt()+
        " and re.rem_codi = "+rem_codiE.getValorInt()+
        " and re.emp_codi = c.emp_codi "+
        " and re.eje_nume = c.fvc_ano "+
        " and re.fvc_nume = c.fvc_nume "+
        " ORDER BY rec_fecvto,cl.cli_codi, c.emp_codi, c.fvc_ano,c.fvc_nume,re.rec_nume";
    dtCon1.select(s);
    impTot=0;
    nGiros=0;
    do
    {
      if (dtCon1.getInt("ban_codi",true) == 0 ||   dtCon1.getDouble("rec_bacuen",true) == 0)
      {
        mensajes.mensajeUrgente("Cuenta bancaria de Cliente " + dtCon1.getInt("cli_codi") +
                                " NO es correcto\n GENERACION DE FICHERO CANCELADA");
        aviso("Cuenta bancaria de Cliente " + dtCon1.getInt("cli_codi") +
                                " NO es correcto");
        fr.close();
        return;
      }
      importe=(int) Formatear.Redondea(dtCon1.getDouble("rec_import")*100,0);
      sOut="5680"+codOrdenante+
          Formatear.ajusIzq(dtCon1.getString("cue_codi"),12)+
          Formatear.ajusIzq(dtCon1.getString("cli_nomco"),40)+
          Formatear.format(dtCon1.getString("ban_codi"),"9999")+
          Formatear.format(dtCon1.getString("rec_baofic"),"9999")+
          Formatear.format(dtCon1.getString("rec_badico"),"99")+
          Formatear.format(dtCon1.getDouble("rec_bacuen"),"9999999999")+
          Formatear.format(importe,"9999999999")+
          Formatear.space(16)+
          Formatear.ajusIzq("N/Fra "+dtCon1.getString("fvc_nume")+
                           (dtCon1.getInt("rec_nume")>1?"-"+dtCon1.getInt("rec_nume"):"")
                            ,40)+
          Formatear.space(8)+finLinea;
      fr.write(sOut);
      impTot+=importe;
      nGiros++;
    } while (dtCon1.next());
    sOut="5880"+codOrdenante+
        Formatear.space(12)+
        Formatear.space(40)+
        Formatear.space(20)+
        Formatear.format(impTot,"9999999999")+
        Formatear.space(6)+
        Formatear.format(nGiros,"9999999999")+
        Formatear.format(nGiros+2,"9999999999")+
        Formatear.space(20)+
        Formatear.space(18)+finLinea;
    fr.write(sOut);
    sOut="5980"+codOrdenante+
        Formatear.space(12)+
        Formatear.space(40)+
        "0001"+ // Numero de Ordenantes
        Formatear.space(16)+
        Formatear.format(impTot,"9999999999")+
        Formatear.space(6)+
        Formatear.format(nGiros,"9999999999")+
        Formatear.format(nGiros+4,"9999999999")+
        Formatear.space(20)+
        Formatear.space(18)+finLinea;
    fr.write(sOut);
    fr.close();
    mensajeErr("Fichero "+fichero+" GENERADO");
    mensajes.mensajeAviso("Fichero "+fichero+" GENERADO");
  }
  /**
   * Crea fichero con la norma 58 (BBVA). Procedimiento 6
   * @throws Exception
   */
  void creaNorma58_6() throws Exception
  {
    String fecFich = rem_fecpreE.getFecha("ddMMyy");
    String fechAdeu = rem_fecadeE.getFecha("ddMMyy");
    String procedimiento = "06";
    String finLinea = "\r\n";
    if (rem_fordosE.isSelected())
      finLinea += "\r\n";
    int importe = 0;
    int impTot;
    int nGiros;
    fichero = rem_direcE.getText() + "/remesa58_6.dat";
    s = "SELECT  * FROM bancteso WHERE bat_codi = " + bat_codiE.getValorInt();
    if (!dtStat.select(s))
      throw new Exception("No encontrado banco de Tesoreria:\n" + s);

    String codOrdenante = Formatear.ajusIzq(dtStat.getString("bat_nif"), 9) +
        Formatear.format(bat_nifsufE.getValorInt(), "999");
    FileWriter fr = new FileWriter(fichero, false);
     // Imprimo la Cabecera

     String sOut;
     sOut="5170"+codOrdenante+
         fecFich+Formatear.space(6)+
         Formatear.ajusIzq(dtStat.getString("bat_titcue"),40)+
         Formatear.space(20)+
         Formatear.format(dtStat.getString("bat_cucoba"),"9999")+
         Formatear.format(dtStat.getString("bat_cusuba"),"9999")+
         Formatear.space(12)+Formatear.space(40)+Formatear.space(14)+finLinea;
     fr.write(sOut);
     sOut="5370"+codOrdenante+
         fecFich+Formatear.space(6)+
         Formatear.ajusIzq(dtStat.getString("bat_titcue"),40)+
         Formatear.format(dtStat.getString("bat_cucoba"),"9999")+
         Formatear.format(dtStat.getString("bat_cusuba"),"9999")+
         Formatear.format(dtStat.getString("bat_digico"),"99")+
         Formatear.format(dtStat.getDouble("bat_numcue"),"9999999999")+
         Formatear.space(8)+procedimiento+Formatear.space(10)+
         Formatear.space(40)+Formatear.space(14)+finLinea;
     fr.write(sOut);

     s="SELECT c.*,cl.cue_codi,cl.cli_codi,cl.cli_nomb,cl.cli_nomco,"+
         " re.rec_nume,re.rec_import,rec_fecvto,"+
         " re.ban_codi,re.rec_baofic,re.rec_badico,re.rec_bacuen "+
         " FROM v_facvec as c,clientes as cl,v_recibo as re "+
         " WHERE  c.cli_codi = cl.cli_codi "+
         " and re.rem_ejerc = "+eje_numeE.getValorInt()+
         " and re.rem_codi = "+rem_codiE.getValorInt()+
         " and re.emp_codi = c.emp_codi "+
         " and re.eje_nume = c.fvc_ano "+
         " and re.fvc_nume = c.fvc_nume "+
         " ORDER BY rec_fecvto,cl.cli_codi, c.emp_codi, c.fvc_ano,c.fvc_nume,re.rec_nume";
     dtCon1.select(s);
     impTot=0;
     nGiros=0;
     do
     {
       if (dtCon1.getInt("ban_codi",true) == 0 ||  dtCon1.getDouble("rec_bacuen",true) == 0)
      {
        mensajes.mensajeUrgente("Cuenta bancaria de Cliente " + dtCon1.getInt("cli_codi") +
                                " NO es correcto\n GENERACION DE FICHERO CANCELADA");
        aviso("Cuenta bancaria de Cliente " + dtCon1.getInt("cli_codi") +
                                " NO es correcto");
        fr.close();
        return;
      }
      importe=(int) Formatear.Redondea(dtCon1.getDouble("rec_import")*100,0);
      sOut="5670"+codOrdenante+
          Formatear.ajusIzq(dtCon1.getString("cue_codi"),12)+
          Formatear.ajusIzq(dtCon1.getString("cli_nomco"),40)+
          Formatear.format(dtCon1.getString("ban_codi"),"9999")+
          Formatear.format(dtCon1.getString("rec_baofic"),"9999")+
          Formatear.format(dtCon1.getString("rec_badico"),"99")+
          Formatear.format(dtCon1.getDouble("rec_bacuen"),"9999999999")+
          Formatear.format(importe,"9999999999")+
          Formatear.space(16)+
          Formatear.ajusIzq("N/Fra "+dtCon1.getString("fvc_nume")+
                           (dtCon1.getInt("rec_nume")>1?"-"+dtCon1.getInt("rec_nume"):"")
                            ,40)+
          dtCon1.getFecha("rec_fecvto","ddMMyy")+
          Formatear.space(2)+finLinea;
      fr.write(sOut);
      impTot+=importe;
      nGiros++;
     } while (dtCon1.next());
     sOut="5870"+codOrdenante+
         Formatear.space(12)+
         Formatear.space(40)+
         Formatear.space(20)+
         Formatear.format(impTot,"9999999999")+
         Formatear.space(6)+
         Formatear.format(nGiros,"9999999999")+
         Formatear.format(nGiros+2,"9999999999")+
         Formatear.space(20)+
         Formatear.space(18)+finLinea;
     fr.write(sOut);
     sOut="5970"+codOrdenante+
         Formatear.space(12)+
         Formatear.space(40)+
         "0001"+ // Numero de Ordenantes
         Formatear.space(16)+
         Formatear.format(impTot,"9999999999")+
         Formatear.space(6)+
         Formatear.format(nGiros,"9999999999")+
         Formatear.format(nGiros+4,"9999999999")+
         Formatear.space(20)+
         Formatear.space(18)+finLinea;
     fr.write(sOut);
     fr.close();
     mensajeErr("Fichero "+fichero+" GENERADO");
     mensajes.mensajeAviso("Fichero "+fichero+" GENERADO");
  }

  void Bactcuen_actionPerformed()
  {
    try
    {
      int nRow = jt.getRowCount();

      for (int n = 0; n < nRow; n++)
      {
        s = "SELECT * FROM clientes " +
            " WHERE  cli_codi = " + jt.getValorInt(n, 4);
        dtStat.select(s);
        s = "UPDATE v_recibo SET ban_codi = " + dtStat.getInt("ban_codi") + "," +
            " rec_baofic = " + dtStat.getInt("cli_baofic") + "," +
            " rec_badico = " + dtStat.getInt("cli_badico") + "," +
            " rec_bacuen = " + dtStat.getDouble("cli_bacuen") +
            " where  emp_codi = " + jt.getValorInt(n, 1) +
            " and eje_nume = " + jt.getValorInt(n, 0) +
            " and fvc_nume = " + jt.getValorInt(n, 2) +
            " and rec_nume = " + jt.getValorInt(n, 3);
        stUp.executeUpdate(s);
      }
      ctUp.commit();
    }
    catch (Exception k)
    {
      Error("Error al act. Numero de cuenta", k);
    }
  }

  void Blistar_actionPerformed()
  {
    try
    {
      if (dtCons.getNOREG())
        return;
      java.util.HashMap mp = new java.util.HashMap();
      JasperReport jr;
      mp.put("rem_fechaE", rem_fechaE.getText());
      mp.put("rem_fecpreE", rem_fecpreE.getText());
      mp.put("rem_fecadeE", rem_fecadeE.getText());
      mp.put("rem_normaE", rem_normaE.getText());
      mp.put("rem_codiE",  rem_codiE.getText());
      mp.put("eje_numeE", eje_numeE.getText());
      mp.put("bat_nombE", bat_codiE.getTextCombo());
      jr = gnu.chu.print.util.getJasperReport(EU,"recibosrem");
      s="SELECT re.*,c.cli_codi,c.fvc_fecfra,cl.cli_nomco FROM v_facvec as c,"+
          " clientes as cl,"+
          " v_recibo as re "+
          " WHERE  c.cli_codi = cl.cli_codi  "+
          " and re.eje_nume = c.fvc_ano "+
          " and re.emp_codi = c.emp_codi "+
          " and re.fvc_nume = c.fvc_nume  "+
          " and re.rem_codi= "+rem_codiE.getValorInt()+
          " and re.rem_ejerc = "+eje_numeE.getValorInt()+
          " ORDER BY re.rec_fecvto,c.cli_codi,c.emp_codi, c.fvc_ano,c.fvc_nume,re.rec_nume";
      debug(s);
      ResultSet rs=dtCon1.getStatement().executeQuery(s);
      JasperPrint jp = JasperFillManager.fillReport(jr, mp,new JRResultSetDataSource(rs));
      gnu.chu.print.util.printJasper(jp, EU);
    }
    catch (Exception k)
    {
      Error("Error al imprimir Relacion de Recibos", k);
    }

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
    verDatos();
  }

  public void afterConecta() throws SQLException, java.text.ParseException
  {
    s = "SELECT tpc_codi FROM v_cobtipo WHERE tpc_giro = 'S'";
    if (!dtStat.select(s))
      msgBox(
          "NO ENCONTRADO TIPO DE COBRO 'GIRO' .. NO SE ACTUALIZARAN LOS COBROS");
    else
      tpcCodi = dtStat.getInt("tpc_codi");
    bat_codiE.setFormato(Types.DECIMAL, "##9");
    s = "SELECT bat_codi,bat_nomb FROM bancteso order by bat_nomb";
    dtStat.select(s);
    bat_codiE.addDatos(dtStat);
    rem_normaE.addItem("Norma 19.1", "19_1");
    rem_normaE.addItem("Norma 58.6", "58_6");
  }

}
