package gnu.chu.anjelica.ventas;

import gnu.chu.anjelica.almacen.StkPartid;
import gnu.chu.utilidades.*;
import gnu.chu.controles.*;
import gnu.chu.sql.*;
import java.awt.*;

import javax.swing.BorderFactory;
import gnu.chu.camposdb.*;
import java.sql.*;
import gnu.chu.anjelica.listados.*;
import java.util.*;
import gnu.chu.anjelica.despiece.utildesp;
import java.awt.event.*;
import gnu.chu.anjelica.almacen.ActualStkPart;
import gnu.chu.anjelica.despiece.MantDesp;
import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.comm.BotonBascula;
/**
 *
 * <p>Título: despventas </p>
 * <p>Descripción: Ventana de Despiece  para Albaranes de Ventas</p>
 * <p>Copyright: Copyright (c) 2005-2011
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
 * @deprecated NO FUNCIONA!!
 * @author chuchiP
 * @version 2.0
 * @version 2.1  NO permito introducir movimientos con lote igual a 0.
 *  Incluyo opcion de Listar Albaranes en Hoja en Blanco.
 */
public class despVentas extends ventana
{
  BotonBascula botonBascula;
  int empCodi,ejeNume;
  boolean cancelado=false;
  utildesp utdesp;
  int tidCodi=0;
//  int defOrden=0;
  etiqueta etiq;
  int TIPOETIQ;
  DatosTabla dtAdd;
  CLabel cLabel1 = new CLabel();
  CPanel Pprinc = new CPanel();
  CPanel Pcabe = new CPanel();
  proPanel pro_codiE = new proPanel();
  CTextField deo_ejelotE = new CTextField(Types.DECIMAL,"###9");
  CTextField pro_loteE = new CTextField(Types.DECIMAL,"####9");
  CLabel cLabel6 = new CLabel();
  CLabel cLabel8 = new CLabel();
  CTextField deo_serlotE = new CTextField(Types.CHAR,"X");
  CTextField pro_numindE = new CTextField(Types.DECIMAL,"###9");
  CLabel cLabel7 = new CLabel();
  CLabel cLabel9 = new CLabel();
  CTextField emp_codiE = new CTextField(Types.DECIMAL,"#9");
  CLabel cLabel5 = new CLabel();
  CLabel Almacen = new CLabel();
  CLinkBox alm_codiE = new CLinkBox();
  int proCodAnt=0;
  double defKilAnt=0;
  CTextField def_kilosE = new CTextField(Types.DECIMAL, "--,---9.99");
  CTextField def_numpieE = new CTextField(Types.DECIMAL, "##9");
  proPanel pro_codlE = new proPanel();
  CTextField pro_nombE = new CTextField();
  CTextField pro_nocabE = new CTextField();
//  CTextField deo_nulogeE = new CTextField(Types.DECIMAL, "####9");
  CLabel cLabel11 = new CLabel();
//  CTextField deo_selogeE = new CTextField(Types.CHAR, "X");
//  CTextField deo_ejlogeE = new CTextField(Types.DECIMAL, "###9");
//  CTextField def_numindE= new CTextField(Types.DECIMAL,"###9");
//  CTextField def_ordenE = new CTextField(Types.DECIMAL, "###9");
  int prvCodi;

  CGridEditable jtLin = new CGridEditable(5)
  {
    public void afterCambiaLinea()
    {
      if (jtLin.getSelectedRow()==0)
      {
        def_numpieE.setEditable(false);
        pro_codlE.getTextField().setEditable(false);
      }
      else
      {
        def_numpieE.setEditable(true);
        pro_codlE.getTextField().setEditable(true);
      }
//      System.out.println("En AftercambiaLinea: "+pro_codlE.getText()+
//                         " - "+def_kilosE.getValorDec());
      proCodAnt=pro_codlE.getValorInt();
      defKilAnt=def_kilosE.getValorDec();
      pro_codlE.resetCambio();
      def_kilosE.resetCambio();
      if (def_feccadE.isNull())
        def_feccadE.setText(feccadE.getText());
      actKilos();
    }
    public int cambiaLinea(int row, int col)
    {
//      System.out.println("cambiaLinea: "+pro_codlE.getText()+
//                         " - "+def_kilosE.getValorDec());
      return cambiaLineajtLin(row);
    }
    public boolean deleteLinea(int row, int col)
    {
      if (row ==0 )
        return false; // La primera Linea NO se puede borrar
      jtLin.setValor(""+proCodAnt,0); // Rest. El valor Antiguo
      jtLin.setValor(""+defKilAnt,2); // Rest. Kilos ANTIGUOS
      return true;
//      return borraLineajtLIn(row);
    }
    public void cambiaColumna(int col,int colNueva,int row)
    {
      try {
        if (col == 0)
          jtLin.setValor(pro_codlE.getNombArt(pro_codlE.getText()), row, 1);
      } catch (Exception k)
      {
        Error("Error al buscar Nombre Articulo",k);
      }
    }
  };
  CLabel cLabel2 = new CLabel();
  CTextField deo_kilosE = new CTextField(Types.DECIMAL,"--,---9.99");
  CTextField kgFinE = new CTextField(Types.DECIMAL,"---,--9.99");
  CLabel Lineas1 = new CLabel();
  CCheckBox opImpEt = new CCheckBox();
  CTextField linFinE = new CTextField(Types.DECIMAL,"##9");
  CLabel cLabel112 = new CLabel();
  CTextField kgDifE = new CTextField(Types.DECIMAL,"---,--9.99");
  CLabel cLabel111 = new CLabel();
  CPanel Ptotal1 = new CPanel();
  CTextField deo_fechaE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel deo_fechaL = new CLabel();
  CLabel cLabel10 = new CLabel();
  CTextField fecprodE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel cLabel12 = new CLabel();
  CTextField feccadE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CTextField def_feccadE = new CTextField(Types.DATE,"dd-MM-yyyy");
  java.util.Date fecSacr;
  CButton Baceptar = new CButton("Aceptar", Iconos.getImageIcon("check"));
  CButton Bcancelar = new CButton("Cancelar", Iconos.getImageIcon("cancel"));
  String s;
  GridBagLayout gridBagLayout1 = new GridBagLayout();

  public despVentas() throws Exception
  {
    jbInit();
  }
  private void jbInit() throws Exception
  {
    iniciarFrame();
    this.setVersion("2011-03-30");
    this.setTitle("Despieces sobre Alb.Ventas");
    this.setSize(new Dimension(652, 421));

    statusBar = new StatusBar(this);
    cLabel1.setText("Producto");
    cLabel1.setBounds(new Rectangle(146, 5, 56, 16));
    Pprinc.setLayout(gridBagLayout1);
    Pcabe.setBorder(BorderFactory.createRaisedBevelBorder());
    Pcabe.setMaximumSize(new Dimension(638, 62));
    Pcabe.setMinimumSize(new Dimension(638, 62));
    Pcabe.setPreferredSize(new Dimension(638, 62));
    Pcabe.setLayout(null);
    deo_ejelotE.setAutoNext(true);
    deo_ejelotE.setBounds(new Rectangle(60, 42, 33, 16));
    pro_loteE.setAutoNext(true);
    pro_loteE.setBounds(new Rectangle(333, 42, 46, 16));
    cLabel6.setText("Serie");
    cLabel6.setBounds(new Rectangle(199, 42, 33, 16));
    cLabel8.setText("Individuo");
    cLabel8.setBounds(new Rectangle(396, 42, 56, 16));
    cLabel7.setText("Num. Lote");
    cLabel7.setBounds(new Rectangle(271, 42, 61, 16));
    cLabel9.setText("Ejercicio");
    cLabel9.setBounds(new Rectangle(5, 42, 47, 16));
    cLabel5.setText("Empresa");
    cLabel5.setBounds(new Rectangle(104, 42, 53, 16));
    Almacen.setText("Almacen");
    Almacen.setBounds(new Rectangle(3, 24, 57, 16));
    alm_codiE.setAncTexto(30);
    alm_codiE.setBounds(new Rectangle(58, 24, 242, 16));
    jtLin.setMaximumSize(new Dimension(638, 206));
    jtLin.setMinimumSize(new Dimension(638, 206));
    jtLin.setPreferredSize(new Dimension(638, 206));
    cLabel2.setText("Kilos");
    cLabel2.setBounds(new Rectangle(506, 42, 40, 16));
    kgFinE.setBounds(new Rectangle(39, 3, 65, 16));
    kgFinE.setEnabled(false);
    Lineas1.setBounds(new Rectangle(107, 3, 41, 16));
    Lineas1.setText("Lineas");
    opImpEt.setToolTipText("Impr.Etiqueta");
    opImpEt.setMargin(new Insets(0, 0, 0, 0));
    opImpEt.setSelected(true);
    opImpEt.setText("Impr.Etiqueta");
    opImpEt.setBounds(new Rectangle(402, 3, 119, 16));
    linFinE.setBounds(new Rectangle(147, 3, 52, 16));
    linFinE.setEnabled(false);
    cLabel112.setRequestFocusEnabled(true);
    cLabel112.setToolTipText("");
    cLabel112.setText("Dif. Kilos");
    cLabel112.setBounds(new Rectangle(200, 3, 53, 16));
    kgDifE.setEnabled(false);
    kgDifE.setBounds(new Rectangle(248, 3, 57, 16));
    cLabel111.setBounds(new Rectangle(5, 3, 29, 16));
    cLabel111.setText("Kilos");
    Ptotal1.setLayout(null);
    Ptotal1.setBorder(BorderFactory.createLoweredBevelBorder());
    Ptotal1.setMaximumSize(new Dimension(635, 49));
    Ptotal1.setMinimumSize(new Dimension(635, 49));
    Ptotal1.setOpaque(true);
    Ptotal1.setPreferredSize(new Dimension(635, 49));
    deo_fechaL.setText("Fecha Desp.");
    deo_fechaL.setBounds(new Rectangle(5, 5, 70, 16));
    cLabel12.setText("Fecha Prod");
    cLabel12.setBounds(new Rectangle(490, 24, 64, 16));
    cLabel10.setText("Fecha Cad.");
    cLabel10.setBounds(new Rectangle(306, 24, 62, 16));
    Baceptar.setBounds(new Rectangle(201, 21, 129, 21));
    Baceptar.setText("Aceptar F4");
    Bcancelar.setBounds(new Rectangle(335, 21, 129, 21));
    deo_kilosE.setBounds(new Rectangle(547, 42, 65, 16));
    pro_numindE.setBounds(new Rectangle(457, 42, 37, 16));
    deo_serlotE.setBounds(new Rectangle(229, 42, 17, 16));
    emp_codiE.setBounds(new Rectangle(154, 42, 21, 16));
    fecprodE.setBounds(new Rectangle(556, 24, 73, 16));
    feccadE.setBounds(new Rectangle(370, 24, 73, 16));
    deo_fechaE.setBounds(new Rectangle(71, 5, 73, 16));
    pro_codiE.setBounds(new Rectangle(201, 4, 429, 17));
    this.getContentPane().add(statusBar,BorderLayout.SOUTH);
    this.getContentPane().add(Pprinc, BorderLayout.CENTER);
    Pcabe.add(deo_fechaL, null);
    Pcabe.add(deo_fechaE, null);
    Pcabe.add(pro_codiE, null);
    Pcabe.add(cLabel1, null);
    Pcabe.add(Almacen, null);
    Pcabe.add(alm_codiE, null);
    Pcabe.add(cLabel10, null);
    Pcabe.add(feccadE, null);
    Pcabe.add(deo_ejelotE, null);
    Pcabe.add(emp_codiE, null);
    Pcabe.add(cLabel9, null);
    Pcabe.add(cLabel5, null);
    Pcabe.add(deo_kilosE, null);
    Pcabe.add(pro_numindE, null);
    Pcabe.add(cLabel6, null);
    Pcabe.add(deo_serlotE, null);
    Pcabe.add(cLabel7, null);
    Pcabe.add(pro_loteE, null);
    Pcabe.add(cLabel8, null);
    Pcabe.add(cLabel2, null);
    Pcabe.add(fecprodE, null);
    Pcabe.add(cLabel12, null);
    Pprinc.add(jtLin,  new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 4), 0, 0));
    Pprinc.add(Ptotal1,   new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

    Ptotal1.add(kgDifE, null);
    Ptotal1.add(cLabel111, null);
    Ptotal1.add(kgFinE, null);
    Ptotal1.add(Lineas1, null);
    Ptotal1.add(linFinE, null);
    Ptotal1.add(cLabel112, null);
    Ptotal1.add(opImpEt, null);
    Ptotal1.add(Bcancelar, null);
    Ptotal1.add(Baceptar, null);
    Pprinc.add(Pcabe,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jtLin.setDefButton(Baceptar);
    jtLin.setEscButton(Bcancelar);
  }
  public void iniciar(DatosTabla dtStat,DatosTabla dtCon1,DatosTabla dtAdd,EntornoUsuario entUsu) throws Exception
  {
    this.dtAdd=dtAdd;
    this.dtStat=dtStat;
    this.dtCon1=dtCon1;
    this.EU=entUsu;
    gnu.chu.Menu.LoginDB.iniciarLKEmpresa(EU,dtStat);


    pro_nombE.setEnabled(false);
    Pcabe.setEnabled(false);
    fecprodE.setEnabledParent(true);
    utdesp = new utildesp();
    pro_codlE.setProNomb(null);
    pro_codiE.iniciar(dtStat,this,vl,EU);
    pro_codlE.iniciar(dtStat,this,vl,EU);
    s = "SELECT alm_codi,alm_nomb FROM v_almacen ORDER BY alm_nomb";
    dtCon1.select(s);
    alm_codiE.addDatos(dtCon1);
    botonBascula.setPreferredSize(new Dimension(50,24));
    botonBascula.setMinimumSize(new Dimension(50,24));
    botonBascula.setMaximumSize(new Dimension(50,24));
    botonBascula = new BotonBascula(EU,this);
    statusBar.add(botonBascula, new GridBagConstraints(9, 0, 1, 2, 0.0, 0.0
                                               , GridBagConstraints.EAST,
                                               GridBagConstraints.VERTICAL,
                                               new Insets(0, 5, 0, 0), 0, 0));
    confGrid();
    activarEventos();
    statusBar.setEnabled(false);
  }
  public void setTidCodi(int tidCodi)
  {
    this.tidCodi=tidCodi;
  }
  private void activarEventos()
  {
    Baceptar.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Baceptar_actionPerformed();
      }
    });
    Bcancelar.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Bcancelar_actionPerformed();
      }
    });
  }
  void Baceptar_actionPerformed()
  {
    if (! guardaDespiece())
      return;
    cancelado=false;
    matar();
  }
  void Bcancelar_actionPerformed()
 {
   cancelado=true;
   matar();
 }
 public boolean isCancelado()
 {
   return cancelado;
 }
 public double getKilos()
 {
   return jtLin.getValorDec(0,2);
 }
 public int getNumInd()
 {
   return jtLin.getValorInt(0,3);
 }

 boolean guardaDespiece()
 {
   aviso("Guardando Despiece de Albaran");
   jtLin.procesaAllFoco();
   actKilos();
   if (cambiaLineajtLin(jtLin.getSelectedRow()) >= 0)
   {
     jtLin.requestFocusLater();
     return false;
   }
   if (kgDifE.getValorDec() > deo_kilosE.getValorDec() * MantDesp.LIMDIF ||
       kgDifE.getValorDec() < deo_kilosE.getValorDec() * MantDesp.LIMDIF * -1)
   {
     mensajeErr("Diferencia de Kilos SUPERA EL " + (MantDesp.LIMDIF * 100) +
                " POR CIENTO");
     jtLin.requestFocusLater();
     return false;
   }
   try
   {
    
    
     for (int n = 0; n < jtLin.getRowCount(); n++)
     {
       if (jtLin.getValorDec(n, 2) == 0 || jtLin.getValorDec(n, 3) == 0)
         continue;
       int nInd = utildesp.getMaxNumInd(dtAdd, jtLin.getValorInt(n, 0), deo_ejelotE.getValorInt(),
                                        emp_codiE.getValorInt(),
                                        deo_serlotE.getText(), pro_loteE.getValorInt());
       int nOrden = utdesp.guardaLinDesp(deo_ejelotE.getValorInt(), emp_codiE.getValorInt(),
                                         deo_serlotE.getText(),
                                         pro_loteE.getValorInt(), nInd, jtLin.getValorInt(n, 0),
                                         jtLin.getValorDec(n, 2), jtLin.getValorInt(n,3),
                                         jtLin.getValString(n,4), 0,0, 0,  0);
      
       jtLin.setValor(""+nInd,n,3);
       imprEtiq(n,TIPOETIQ);
     }
     dtAdd.getConexion().commit();
     mensajeErr("Despiece Guardado");
   }
   catch (Exception k)
   {
     Error("Error al guardar despiece", k);
     return false;
   }
   return true;
 }
 public String insertaDesp(int proCodi,int ejeLote,int empLote,String serLote,int numLote,int numIndiv,
                             int almCodi,java.util.Date fecAlbar,double kilos,int empCodi,
                             int ejeNume,int tipoEtiq) throws Exception
  {
    String msgError;
    fecprodE.setEnabledParent(true);
    feccadE.setEnabledParent(true);
    StkPartid canStk= utildesp.buscaPeso(dtCon1, ejeLote,empLote,serLote,numLote,numIndiv,proCodi,almCodi);
    if (canStk.hasError())
      return canStk.getMensaje();
    if (canStk.getKilos()  <= kilos - 0.1)
         return "Partida de Stock solo tiene "+canStk.getKilos()+"  kg. Disponibles";

    alm_codiE.setValorInt(almCodi);
    pro_codiE.setValorInt(proCodi);
    deo_ejelotE.setValorInt(ejeLote);
    emp_codiE.setValorInt(empLote);
    deo_serlotE.setText(serLote);
    pro_loteE.setValorDec(numLote);
    pro_numindE.setValorDec(numIndiv);
    deo_kilosE.setValorDec(dtCon1.getDouble("stp_kilact"));
    deo_fechaE.setDate(fecAlbar);
    fecprodE.setText(null);
    this.empCodi=empCodi;
    this.ejeNume=ejeNume;
    int prvDesp=pdconfig.getPrvDespiece(empCodi,dtStat);
    TIPOETIQ=tipoEtiq;

    if (utdesp.busDatInd(serLote, proCodi, empLote, ejeLote, numLote, numIndiv,
                         dtCon1, dtStat,
                         EU))
    {
      prvCodi = utdesp.prvCodi;
      feccadE.setText(utdesp.feccadE);
      fecSacr=utdesp.getFecSacrif();
    }
    else
      prvCodi = prvDesp;


    jtLin.setEnabled(false);
    jtLin.removeAllDatos();
    {
      Vector v = new Vector();
      v.add("" + proCodi);
      v.add(pro_codiE.getTextNomb());
      v.add("" + kilos);
      v.add("" + 1);
      v.add("" + feccadE.getText());
      jtLin.addLinea(v);

      jtLin.setValor(pro_codiE.getTextNomb(), 0, 1);
    }
    s = "select d.pro_codi,p.pro_nomb from tipdessal d,v_articulo p " +
        " WHERE d.tid_codi = " + tidCodi +
        " and d.pro_codi = p.pro_codi " +
        " order by d.pro_codi";
    if (dtCon1.select(s))
    {
      do
      {
        Vector v = new Vector();
        v.addElement(dtCon1.getString("pro_codi"));
        v.addElement(dtCon1.getString("pro_nomb"));
        v.addElement("0"); // kgs
        v.addElement("1"); //Unid.
        v.add("" + feccadE.getText());
        jtLin.addLinea(v);
      }
      while (dtCon1.next());
    }
    else
    {
      Vector v = new Vector();
      v.addElement("");
      v.addElement("");
      v.addElement("0"); // kgs
      v.addElement("1"); //Unid.
      v.add("" + feccadE.getText());
      jtLin.addLinea(v);
    }
    jtLin.setEnabled(true);
    jtLin.requestFocusLater(1,0);
    return null;
  }
  void actKilos()
  {
    int nLin=0;
    double kg=0;
    int nRow=jtLin.getRowCount();
    for (int n=0;n<nRow;n++)
    {
      if (jtLin.getValorDec(n,2)==0 || jtLin.getValorDec(n,3)==0)
        continue;
      kg+=jtLin.getValorDec(n,2);
      nLin++;
    }
    kgFinE.setValorDec(kg);
    linFinE.setValorDec(nLin);
    kgDifE.setValorDec(deo_kilosE.getValorDec()-kgFinE.getValorDec());
   }
   int cambiaLineajtLin(int linea)
    {
      try
      {
        if (def_kilosE.getValorDec()==0 || def_numpieE.getValorInt()==0)
          return -1; // Si NO tengo Kilos o unidades paso de todo
        if (!pro_codlE.controla(false))
        {
          mensajeErr("Codigo de Producto NO valido");
          return 0;
        }

       if (def_feccadE.getError() || def_feccadE.isNull())
        {
          mensajeErr("Fecha Caducidad NO valida");
          return 4;
        }
    }
      catch (Exception ex)
      {
        Error("Error al Cambiar Linea de Grid", ex);
      }
      return -1;
    }

    void imprEtiq(int linea, int tipoEtiq)
     {

       try {
         pro_codlE.getNombArt(jtLin.getValString(linea,0),emp_codiE.getValorInt());
         if (! pro_codlE.isVendible())
           return;
         utdesp.imprEtiq(tipoEtiq,dtStat,jtLin.getValorInt(linea,0),
                         pro_codlE.getTextNomb(),
                    "D",pro_loteE.getValorInt(),deo_ejelotE.getText(),
                   deo_serlotE.getText(),jtLin.getValorInt(linea,3),jtLin.getValorDec(linea,2),
                   deo_fechaE.getText(),fecprodE.isNull()?null:fecprodE.getDate(),
                   "Cad:"+ jtLin.getValDate(linea,4,"dd-MM-yy"),
                   fecSacr,jtLin.getValDate(linea,4,"dd-MM-yy"));

          mensajeErr("Etiqueta ... Listada");
       }
       catch (Throwable ex)
       {
         Error("Error al Imprimir Etiqueta", ex);
       }

      }

      void confGrid() throws Exception
      {
        Vector v = new Vector();

        v.addElement("Producto"); // 0
        v.addElement("Descripcion"); // 1
        v.addElement("Kgs"); // 2
        v.addElement("Unid"); // 3 (Unidades Pesadas).
        v.addElement("Fec.Cad"); // 4
//        v.addElement("N.Ind"); // 5
//        v.addElement("N.Lin"); // 6
        jtLin.setCabecera(v);
        jtLin.setAnchoColumna(new int[] {54, 220, 70, 50, 90});
        jtLin.setAlinearColumna(new int[]  {2, 0, 2, 2, 1});
        jtLin.setFormatoColumna(2, "--,---9.99");
        jtLin.setFormatoColumna(3, "##9");
//        jtLin.setFormatoColumna(5, "---9");
//        jtLin.setFormatoColumna(6, "##9");
//        def_numpieE.setEnabled(false);
        def_numpieE.setValorInt(1);
//        def_numpieE.setEnabled(false);
        Vector v1 = new Vector();
        def_kilosE.setLeePesoBascula(botonBascula);
        v1.addElement(pro_codlE.getTextField()); // 0
        v1.addElement(pro_nombE); // 1
        v1.addElement(def_kilosE); // 2
        v1.addElement(def_numpieE); // 3
        v1.addElement(def_feccadE); // 4
//        v1.addElement(def_numindE); // 5
//        v1.addElement(def_ordenE); // 6
        jtLin.setCampos(v1);
      }
}
