package gnu.chu.anjelica.listados;

import gnu.chu.utilidades.*;
import gnu.chu.controles.*;
import gnu.chu.Menu.Principal;
import java.awt.event.*;
import java.sql.*;
import java.awt.*;
import gnu.chu.camposdb.*;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import gnu.chu.anjelica.despiece.utildesp;
/**
 * <p>Titulo: repetiqu </p>
 * <p>Descripcion: Permite repetir etiquetas de un producto, cambiando los datos a
 * mostrar. Permite elegir diferentes tipos de etiqueta.
 * La opcion de generar el inventario esta obsoleta y deshabilitada.
 * </p>
 * <p>Copyright: Copyright (c) 2005-2017</p>
 *
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los términos de la Licencia Publica General de GNU según es publicada por
 *  la Free Software Foundation, bien de la versión 2 de dicha Licencia
 *  o bien (según su elección) de cualquier versión posterior.
 *  Este programa se distribuye con la esperanza de que sea útil,
 *  pero SIN NINGUNA GARANTÍA, incluso sin la garantía MERCANTIL implícita
 *  o sin garantizar la CONVENIENCIA PARA UN PROPÓSITO PARTICULAR.
 *  Véase la Licencia Pública General de GNU para más detalles.
 *  Debería haber recibido una copia de la Licencia Pública General junto con este programa.
 *  Si no ha sido así, escriba a la Free Software Foundation, Inc.,
 *  en 675 Mass Ave, Cambridge, MA 02139, EEUU.
 * @author chuchiP
 * @version 1.1
 */

public class repetiqu extends ventana
{
  utildesp utDesp=new utildesp();
  etiqueta etiq;
  String s;
  String famProd;
  java.util.Date grd_fecpro;
  CPanel Pprinc = new CPanel();
  proPanel pro_codiE = new proPanel();
  CLabel cLabel1 = new CLabel();
  CTextField deo_ejelotE = new CTextField(Types.DECIMAL, "###9");
  CTextField pro_loteE = new CTextField(Types.DECIMAL, "####9");
  CLabel cLabel6 = new CLabel();
  CLabel cLabel8 = new CLabel();
  CTextField deo_serlotE = new CTextField(Types.CHAR, "X");
  CTextField pro_numindE = new CTextField(Types.DECIMAL, "##9");
  CLabel numEtiqL = new CLabel("N.Etiq:");
  CTextField numEtiqE = new CTextField(Types.DECIMAL, "##9");
  CLabel cLabel7 = new CLabel();
  CLabel cLabel9 = new CLabel();
  CTextField deo_emplotE = new CTextField(Types.DECIMAL, "#9");
  CLabel cLabel5 = new CLabel();
  CTextField deo_kilosE = new CTextField(Types.DECIMAL, "---,--9.99");
  CLabel cLabel2 = new CLabel();
  CPanel cPanel1 = new CPanel();
  CLabel cLabel3 = new CLabel();
  CTextField nacidoE = new CTextField();
  CLabel cebadoL = new CLabel();
  CTextField cebadoE = new CTextField();
  CLabel cLabel4 = new CLabel();
  CTextField sacrificadoE = new CTextField();
  CLabel cLabel10 = new CLabel();
  CTextField despiezadoE = new CTextField();
  CLabel cLabel11 = new CLabel();
  CTextField ntrazaE = new CTextField();
  CLabel textoL = new CLabel();
  CTextField conservarE = new CTextField();
  CButton Baceptar = new CButton("Imprimir",Iconos.getImageIcon("print"));
  CButton Bindi = new CButton();
  CButton Bcancelar = new CButton("Cancelar",Iconos.getImageIcon("cancel"));
  CLabel fecrepL = new CLabel();
  CTextField fecrecepE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel cLabel12 = new CLabel();
  CComboBox tipetiqE = new CComboBox();
  CPanel Peleg = new CPanel();
  CButton Brepeti = new CButton();
  CButton Bgenera = new CButton();
  CPanel Pentra = new CPanel();
  CLabel cLabel14 = new CLabel();
  CLabel modoL = new CLabel();
  char swModo='R';
  CCheckBox opError = new CCheckBox();
  CLabel cLabel13 = new CLabel();
  CTextField feccadE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel cLabel15 = new CLabel();
  CTextField fecsacrE = new CTextField(Types.DATE,"dd-MM-yyyy");

  public repetiqu(EntornoUsuario eu, Principal p) {
     EU=eu;
     vl=p.panel1;
     jf=p;
     eje=true;

     setTitulo("Repetir/Generar Etiquetas");

     try  {
       if(jf.gestor.apuntar(this))
           jbInit();
       else
         setErrorInit(true);
     }
     catch (Exception e) {
        ErrorInit(e);
     }
   }

  public repetiqu(gnu.chu.anjelica.menu p,EntornoUsuario eu) {

      EU=eu;
      vl=p.getLayeredPane();
      setTitulo("Repetir/Generar Etiquetas");
      eje=false;

      try  {
        jbInit();
      }
      catch (Exception e) {
              ErrorInit(e);
      }
    }
   public static String getNombreClase()
   {        
     return "gnu.chu.anjelica.listados.repetiqu";
   }
   public void setEjercicio(int ejerc)
   {
       deo_ejelotE.setValorInt(ejerc);
   }
   public void setSerie(String serie)
   {
       deo_serlotE.setText(serie);
   }
   public void setPartida(int partida)
   {
       pro_loteE.setValorInt(partida);
   }
   public void  setIndividuo(int indiv)
   {
       pro_numindE.setValorInt(indiv);       
   }
   public void setProducto(int producto)
   {
       pro_codiE.setValorInt(producto);
   }
    private void jbInit() throws Exception
    {
      iniciarFrame();
      this.setSize(new Dimension(519,339));
      this.setVersion("2017-01-22");
      Pprinc.setLayout(null);
      statusBar = new StatusBar(this);
      Bindi.setBounds(new Rectangle(471, 25, 1, 1));
      Bcancelar.setBounds(new Rectangle(184, 171, 100, 35));
      Bcancelar.setMargin(new Insets(0, 0, 0, 0));

      fecrepL.setText("Fecha Recep:");
      fecrepL.setBounds(new Rectangle(4, 81, 82, 16));
      fecrecepE.setBounds(new Rectangle(86, 81, 79, 16));
      cLabel12.setText("Tipo Etiqueta");
      cLabel12.setBounds(new Rectangle(285, 191, 73, 16));
      tipetiqE.setBounds(new Rectangle(364, 191, 113, 16));
      Peleg.setBorder(BorderFactory.createRaisedBevelBorder());
      Peleg.setBounds(new Rectangle(180, 2, 279, 27));
      Peleg.setLayout(null);
      Brepeti.setBounds(new Rectangle(9, 3, 92, 20));
      Brepeti.setText("Repetir");
      Bgenera.setBounds(new Rectangle(178, 3, 92, 20));
      Bgenera.setText("Generar");
      Bgenera.setEnabled(false);
      Pentra.setBorder(BorderFactory.createLoweredBevelBorder());
      Pentra.setBounds(new Rectangle(4, 31, 495, 214));
      Pentra.setLayout(null);
    cLabel14.setText("Modo");
    cLabel14.setBounds(new Rectangle(12, 6, 31, 15));
    modoL.setBackground(Color.red);
    modoL.setForeground(Color.white);
    modoL.setOpaque(true);
    modoL.setHorizontalAlignment(SwingConstants.CENTER);
    modoL.setHorizontalTextPosition(SwingConstants.CENTER);
    modoL.setBounds(new Rectangle(44, 6, 110, 16));
    opError.setSelected(true);
    opError.setText("Imprimir s/compra");
    opError.setToolTipText("Imprimir aunque no tenga datos de compra");
    opError.setBounds(new Rectangle(285, 170, 128, 16));
    numEtiqL.setBounds(new Rectangle(415, 170, 40, 16));
    numEtiqE.setBounds(new Rectangle(455, 170, 30, 16));
    numEtiqE.setText("1");
    cLabel13.setText("Fecha Cad.");
    cLabel13.setBounds(new Rectangle(317, 81, 64, 16));
    feccadE.setTipoCampo(Types.DATE);

    feccadE.setBounds(new Rectangle(380, 81, 79, 16));
    cLabel15.setBounds(new Rectangle(169, 81, 66, 16));
    cLabel15.setText("Fecha Sacr.");
    fecsacrE.setBounds(new Rectangle(233, 81, 79, 16));
    fecsacrE.setText("");
    fecsacrE.setTipoCampo(91);
    fecsacrE.setStrQuery("");
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);
      conecta();
      pro_codiE.setBounds(new Rectangle(60, 3, 409, 18));
      cLabel1.setText("Producto");
      cLabel1.setBounds(new Rectangle(2, 3, 53, 18));
      deo_ejelotE.setBounds(new Rectangle(52, 23, 33, 15));
      pro_loteE.setBounds(new Rectangle(263, 23, 36, 15));
      pro_loteE.setAutoNext(true);
      cLabel6.setBounds(new Rectangle(164, 23, 33, 15));
      cLabel6.setText("Serie");
      cLabel8.setToolTipText("");
      cLabel8.setBounds(new Rectangle(304, 23, 26, 15));
      cLabel8.setText("Ind.");
      deo_serlotE.setBounds(new Rectangle(198, 23, 17, 15));
      deo_serlotE.setText("A");
      deo_serlotE.setMayusc(true);
      deo_serlotE.setAutoNext(true);
      pro_numindE.setBounds(new Rectangle(332, 23, 37, 16));
      pro_numindE.setAutoNext(true);
      cLabel7.setBounds(new Rectangle(217, 23, 41, 15));
      cLabel7.setText("N. Lote");
      cLabel9.setBounds(new Rectangle(4, 23, 47, 15));
      cLabel9.setText("Ejercicio");
      deo_emplotE.setBounds(new Rectangle(138, 23, 21, 15));
      deo_emplotE.setAutoNext(true);
      deo_emplotE.setValorInt(EU.em_cod);
      deo_emplotE.setEnabled(false);
      cLabel5.setBounds(new Rectangle(87, 23, 51, 15));
      cLabel5.setText("Empresa");
      deo_kilosE.setBounds(new Rectangle(408, 23, 62, 14));
      cLabel2.setText("Kilos");
      cLabel2.setBounds(new Rectangle(378, 23, 29, 16));
      cPanel1.setBorder(BorderFactory.createLoweredBevelBorder());
      cPanel1.setMaximumSize(new Dimension(32767, 32767));
      cPanel1.setBounds(new Rectangle(20, 44, 465, 121));
      cPanel1.setLayout(null);
      cLabel3.setText("Nacido en");
      cLabel3.setBounds(new Rectangle(4, 6, 59, 16));
      nacidoE.setBounds(new Rectangle(86, 6, 147, 16));
      cebadoL.setText("Cebado en");
      cebadoL.setBounds(new Rectangle(236, 6, 63, 16));
      cebadoE.setBounds(new Rectangle(302, 6, 147, 16));
      cLabel4.setText("Sacrificado en");
      cLabel4.setBounds(new Rectangle(4, 26, 83, 16));
      sacrificadoE.setAlignmentY( (float) 0.5);
      sacrificadoE.setBounds(new Rectangle(86, 26, 303, 16));
      cLabel10.setText("Despiezado en");
      cLabel10.setBounds(new Rectangle(4, 44, 84, 20));
      despiezadoE.setBounds(new Rectangle(86, 46, 303, 16));
      cLabel11.setText("N. Trazabilidad");
      cLabel11.setBounds(new Rectangle(2, 64, 85, 18));
      ntrazaE.setSelectedTextColor(Color.black);
      ntrazaE.setBounds(new Rectangle(86, 64, 303, 16));
      textoL.setText("Conservar");
      textoL.setBounds(new Rectangle(2, 99, 82, 16));
      conservarE.setBounds(new Rectangle(86, 99, 303, 16));
      Baceptar.setBounds(new Rectangle(31, 171, 125, 35));
      this.getContentPane().add(Pprinc, BorderLayout.CENTER);
      cPanel1.add(cLabel3, null);
      cPanel1.add(cLabel4, null);
      cPanel1.add(nacidoE, null);
      cPanel1.add(sacrificadoE, null);
      cPanel1.add(cebadoE, null);
      cPanel1.add(cebadoL, null);
      cPanel1.add(cLabel10, null);
      cPanel1.add(despiezadoE, null);
    cPanel1.add(ntrazaE, null);
    cPanel1.add(cLabel11, null);
    cPanel1.add(fecrecepE, null);
    cPanel1.add(fecrepL, null);
    cPanel1.add(conservarE, null);
    cPanel1.add(textoL, null);
    cPanel1.add(cLabel15, null);
    cPanel1.add(cLabel13, null);
    cPanel1.add(feccadE, null);
    cPanel1.add(fecsacrE, null);
    Pentra.add(opError, null);
    Pentra.add(numEtiqL, null);
    Pentra.add(numEtiqE, null);
    
    Pentra.add(Baceptar, null);
    Pentra.add(cLabel12, null);
    Pentra.add(Bcancelar, null);
    Pentra.add(tipetiqE, null);
    Pentra.add(pro_codiE, null);
    Pentra.add(cLabel1, null);
    Pentra.add(Bindi, null);
    Pprinc.add(modoL, null);
    Pprinc.add(cLabel14, null);
    Pprinc.add(Peleg, null);
    Peleg.add(Bgenera, null);
    Peleg.add(Brepeti, null);

      Pprinc.add(Pentra, null);
      Pentra.add(pro_numindE, null);
      Pentra.add(deo_ejelotE, null);
      Pentra.add(cLabel9, null);
      Pentra.add(cLabel5, null);
      Pentra.add(deo_emplotE, null);
      Pentra.add(cLabel6, null);
      Pentra.add(deo_serlotE, null);
      Pentra.add(cLabel7, null);
      Pentra.add(pro_loteE, null);
      Pentra.add(cLabel8, null);
      Pentra.add(cLabel2, null);
      Pentra.add(deo_kilosE, null);
      Pentra.add(cPanel1, null);
      Pprinc.setEscButton(Bcancelar);

      Pprinc.setDefButton(Baceptar);
      pro_codiE.iniciar(dtStat,this,vl,EU);
      pro_codiE.setCamposLote(deo_ejelotE,deo_serlotE,pro_loteE,pro_numindE,deo_kilosE);

      this.setEnabled(false);
    }
    void activaCPanel1(boolean b)
    {
      nacidoE.setEnabled(b);
      sacrificadoE.setEnabled(b);
      cebadoE.setEnabled(b);
      despiezadoE.setEnabled(b);
      ntrazaE.setEnabled(b);
      fecrecepE.setEnabled(b);
      feccadE.setEnabled(b);
      fecsacrE.setEnabled(b);
    }
    @Override
    public void iniciarVentana() throws Exception
    {
      Baceptar.setEnabled(false);
      conservarE.setEnabled(false);

      etiq=new etiqueta(EU);
      tipetiqE.setDatos(etiqueta.getReports(dtStat, EU.em_cod,0));

      
      deo_ejelotE.setText("" + EU.ejercicio);
      modoL.setText("Repetir");
      swModo = 'R';

      activarEventos();

      activar(true);
      this.setEnabled(true);
      // need to invoke because of new focus architecture
      javax.swing.SwingUtilities.invokeLater(new Runnable()
      {
        public void run()
        {
          try {
            Thread.currentThread().sleep(100);
          } catch (Exception k){}
          pro_codiE.requestFocus();
        }
      });

      pro_codiE.requestFocus();
    }

    void activar(boolean b)
    {
      if (b)
      {
        pro_numindE.resetCambio();
        pro_loteE.resetCambio();
        pro_codiE.hasCambio();
      }
      pro_codiE.setEnabled(b);
      if (swModo=='R')
      {
//        fecrepL.setText("Fecha Recep:");
//        deo_emplotE.setEnabled(b);
        deo_ejelotE.setEnabled(b);
        deo_serlotE.setEnabled(b);
        pro_loteE.setEnabled(b);
        pro_numindE.setEnabled(b);
        Baceptar.setEnabled(!b);
        Bindi.setEnabled(b);
      }
      conservarE.setEnabled(!b);
      activaCPanel1(false);
    }

    void activarEventos()
    {
      Baceptar.addActionListener(new ActionListener() {
            @Override
        public void actionPerformed(ActionEvent e) {
          listar();
        }
      });
      Brepeti.addActionListener(new ActionListener() {
            @Override
        public void actionPerformed(ActionEvent e) {
          swModo='R';
          modoL.setText("REPETIR");
          activar(true);
          pro_codiE.requestFocus();
          tipetiqE.setEnabled(true);
          mensajeErr("Activado Modo ... Repetir");
        }
      });
      Bgenera.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          Bgenera_actionPerformed();
        }
      });
      pro_numindE.addFocusListener(new FocusAdapter() {

        @Override
        public void focusLost(FocusEvent e) {
          try
          {
            if (swModo=='G')
              return;
            if (pro_numindE.hasCambio() || pro_loteE.hasCambio() || pro_codiE.hasCambio())
              pro_numindE.resetCambio();
            pro_loteE.resetCambio();
            pro_codiE.resetCambio();
            if (buscaPeso())
              deo_kilosE.setValorDec(dtCon1.getDouble("stp_kilact"));
          }
          catch (SQLException ex)
          {
            Error("Error al Buscar peso",ex);
          }
        }
      });
      Bindi.addFocusListener(new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
          if (swModo=='G')
            return;

          if (e.getOppositeComponent()==deo_kilosE)
            Bindi.doClick();
          else
            deo_kilosE.requestFocus();
        }
      });
      Bindi.addActionListener(new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            Bindi_actionPerformed();
        }
      });
      Bcancelar.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          activar(true);
          pro_codiE.requestFocus();
          mensajeErr("Listado .. Cancelado");
        }
      });
    }
    void Bgenera_actionPerformed()
    {
      swModo = 'G';
      modoL.setText("GENERAR");
      try {
        s = "SELECT MAX(pro_lote) as lote FROM genetiq WHERE eje_nume= " + EU.ejercicio +
            " AND emp_Codi = " + EU.em_cod;
        dtCon1.select(s);
        int numLote=dtCon1.getInt("lote",true);
        if (numLote==0)
          numLote=0;
        numLote++;
        pro_loteE.setValorDec(numLote);

      } catch (SQLException k)
      {
        Error("Error al buscar N.de Lote",k);
        return;
      }
      pro_numindE.setText("1");
      deo_emplotE.setValorDec(EU.em_cod);
      deo_ejelotE.setValorDec(EU.ejercicio);
      deo_serlotE.setText("G");

      deo_emplotE.setEnabled(false);
      deo_ejelotE.setEnabled(false);
      deo_serlotE.setEnabled(false);
      pro_loteE.setEnabled(false);
      pro_numindE.setEnabled(false);
      Bindi.setEnabled(false);
      Baceptar.setEnabled(true);
      fecrepL.setText("Fecha Etiq");
      fecrecepE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
      activar(true);
      pro_codiE.requestFocus();
      tipetiqE.setEnabled(false);

      mensajeErr("Activado Modo ... Generar");
    }
    public void consulta0()
    {
        try
        {
            if (buscaPeso())
            {
                deo_kilosE.setValorDec(dtCon1.getDouble("stp_kilact"));
            }
            verDatos();
        } catch (SQLException k)
        {
            Error("Error al buscar datos", k);
        }
    }
    void Bindi_actionPerformed()
    {
      try {
      if (muerto)
          return;
      if (!pro_codiE.controlar())
      {
        mensajeErr(pro_codiE.getMsgError());
        return;
      }
      famProd = pro_codiE.getFamilia();
      if (deo_ejelotE.getValorInt() == 0)
      {
        mensajeErr("Introduzca Ejercicio de Lote");
        deo_ejelotE.requestFocus();
        return;
      }
//      if (deo_emplotE.getValorInt() == 0)
//      {
//        mensajeErr("Introduzca Empresa de Lote");
//        deo_emplotE.requestFocus();
//        return;
//      }
      if (deo_serlotE.getText().trim().equals(""))
      {
        mensajeErr("Introduzca Serie de Lote");
        deo_serlotE.requestFocus();
        return;
      }
      if (pro_loteE.getValorInt() == 0)
      {
        mensajeErr("Introduzca N.de Lote");
        pro_loteE.requestFocus();
        return;
      }
      if (!buscaPeso() && opError.isSelected()==false)
      {
        pro_codiE.requestFocus();
        return;
      }
      else
      {
        if (deo_kilosE.getValorDec() == 0 || pro_numindE.hasCambio()
            || pro_loteE.hasCambio() || pro_codiE.hasCambio())
          deo_kilosE.setValorDec(dtCon1.getDouble("stp_kilact"));
        pro_numindE.resetCambio();
        pro_loteE.resetCambio();
        pro_codiE.resetCambio();
      }
      if (deo_kilosE.getValorDec() == 0)
      {
        mensajeErr("Introduzca peso de Producto");
        deo_kilosE.requestFocus();
        return;
      }
      } catch (Exception k)
      {
        Error ("Error al controlar valores",k);
        return;
      }
      new miThread("")
      {
        @Override
        public void run()
        {
          verDatos();
        }
      };
    }

    void verDatos()
    {
      try
      {
        cPanel1.resetTexto();
        this.setEnabled(false);
        if (! verDatos0())
        {
           if (! opError.isSelected())
           {
             this.setEnabled(true);
             pro_codiE.requestFocus();
             return;
           }
           else
           {
             this.setEnabled(true);
             activar(false);
             nacidoE.setEnabled(true);
             activaCPanel1(true);
             return;
           }
        }
        this.setEnabled(true);
        activar(false);
        activaCPanel1(true);
      }
      catch (Exception ex)
      {
        Error("Error al buscar datos",ex);
      }


    }

    boolean verDatos0() throws Exception
    {
      if (!utDesp.busDatInd(deo_serlotE.getText(), pro_codiE.getValorInt(),
                             EU.em_cod,
                            deo_ejelotE.getValorInt(), pro_loteE.getValorInt(),
                            pro_numindE.getValorInt(),
                            dtCon1, dtStat, EU))
      {
        msgBox(utDesp.getMsgAviso());
        return false;
      }
      nacidoE.setText(utDesp.paisNacimientoNombre);
      cebadoE.setText(utDesp.paisEngordeNombre);
      sacrificadoE.setText(utDesp.sacrificadoE);
      despiezadoE.setText(utDesp.despiezadoE);
      ntrazaE.setText(utDesp.ntrazaE);
      if (utDesp.swDesp)
      {
        fecrepL.setText("Fecha Prod:");
        fecrecepE.setDate(utDesp.getFechaProduccion());
      }
      else
      {
          fecrepL.setText("Fecha Recep:");
          fecrecepE.setText(utDesp.fecrecepE);
      }
      conservarE.setText(utDesp.getConservar());
      feccadE.setText(utDesp.feccadE);
      grd_fecpro=utDesp.getFechaProduccion();
      fecsacrE.setDate(utDesp.getFecSacrif());
      return true;
    }



    void listar()
    {
      try
      {
        if (swModo=='G')
        {
          if (!pro_codiE.controlar())
          {
            mensajeErr(pro_codiE.getMsgError());
            return;
          }
          if (deo_kilosE.getValorDec() == 0)
          {
            mensajeErr("Introduzca peso de Producto");
            deo_kilosE.requestFocus();
            return;
          }
        }
        // Fin del Bucle ya tenemos todos los datos.
        mensajeErr("Espere, por favor ... Imprimiendo");
        etiq.setTipoEtiq(dtStat, EU.em_cod,
                          tipetiqE.getValorInt());
        
        CodigoBarras codBarras=new CodigoBarras("R",deo_ejelotE.getText(),
                deo_serlotE.getText(),
                pro_loteE.getValorInt(),pro_codiE.getValorInt(),
                pro_numindE.getValorInt(),
                deo_kilosE.getValorDec());
        java.util.Date fecCong=null;
        etiq.setNumCopias(1);
//        if (tipetiqE.getText().startsWith("INTER"))
//        {
//            fecCong=utildesp.getDateCongelado(pro_codiE.getValorInt(), fecrecepE.getDate(), dtStat);
//            etiq.setNumCopias(numEtiqE.getValorInt());
//        }
//        else
//            etiq.setFechaCongelado(utp.getFechaProduccion());
        
        etiq.iniciar(tipetiqE.getValorInt()!=etiqueta.ETIQINT?codBarras.getCodBarra():codBarras.getLote(false),
            codBarras.getLote(tipetiqE.getValorInt()!=etiqueta.ETIQINT),
            pro_codiE.getText(),pro_codiE.getTextNomb(),nacidoE.getText(),cebadoE.getText(),despiezadoE.getText(),
            ntrazaE.getText(),deo_kilosE.getValorDec(),
            conservarE.getText(),sacrificadoE.getText(),
            fecrecepE.getDate(),fecrecepE.getDate(), feccadE.getDate(),
            fecsacrE.getDate());
        
        etiq.listarDefec();
        if (swModo=='G')
        {
          insEtiq();
          pro_numindE.setValorDec(pro_numindE.getValorInt()+1);
        }
        activar(true);
        pro_codiE.requestFocus();
        mensajeErr("Listado ... Generado");
      }
      catch (Throwable k)
      {
        Error("Error al Imprimir Etiqueta", k);
      }
    }
/**
 * 
 * @throws Exception 
 */
    void insEtiq() throws Exception
    {
      dtCon1.addNew("genetiq");
      dtCon1.setDato("get_fecha",Formatear.getFechaAct("dd-MM-yyyy"),"dd-MM-yyyy");
      dtCon1.setDato("usu_codi",EU.usuario);
      dtCon1.setDato("pro_codi",pro_codiE.getValorInt());
      dtCon1.setDato("emp_codi",EU.em_cod);
      dtCon1.setDato("eje_nume",deo_ejelotE.getValorInt());
      dtCon1.setDato("pro_serie",deo_serlotE.getText());
      dtCon1.setDato("pro_lote",pro_loteE.getValorInt());
      dtCon1.setDato("pro_numind",pro_numindE.getValorInt());
      dtCon1.setDato("get_kilos",deo_kilosE.getValorDec());
      dtCon1.update(stUp);
      ctUp.commit();
    }
    boolean buscaPeso()
    {
      try
      {
        if (muerto)
            return false;
        s = "SELECT * FROM V_STKPART WHERE " +
            " EJE_NUME= " + deo_ejelotE.getValorInt() +
//            " AND EMP_CODI= " + deo_emplotE.getValorInt() +
            " AND PRO_SERIE='" + deo_serlotE.getText() + "'" +
            " AND pro_nupar= " + pro_loteE.getValorInt() +
            " and pro_numind= " + pro_numindE.getValorInt() +
            " and pro_codi= " + pro_codiE.getValorInt();
        if (!dtCon1.select(s))
        {
          // Compruebo si el producto tiene control stock por individuo
          s = "SELECT pro_coinst,pro_stock as stp_kilact FROM V_ARTICULO WHERE pro_codi = " +
              pro_codiE.getValorInt();
          if (!dtCon1.select(s))
          {
            mensajeErr("Articulo NO encontrado");
            return false;
          }
          if (dtCon1.getInt("pro_coinst") == 0)
          { // SIN CONTROL individual devolvemos el STOCK
            return true;
          }
          mensajeErr("NO encontrado Partida para estos valores");
          return false;
        }
        return true;
      }
      catch (Exception k)
      {
        Error("Error al buscar Peso", k);
        return false;
      }
    }


}
