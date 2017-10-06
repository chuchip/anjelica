package gnu.chu.anjelica.listados;

import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.almacen.StkPartid;
import gnu.chu.anjelica.almacen.pdalmace;
import gnu.chu.anjelica.despiece.utildesp;
import gnu.chu.controles.StatusBar;
import gnu.chu.utilidades.CodigoBarras;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.ventana;
import gnu.chu.winayu.ayuLote;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import javax.swing.SwingUtilities;

/**
 * <p>Titulo: RepEtiqueta </p>
 * <p>Descripcion: Permite repetir etiquetas de un producto, cambiando los datos a
 * mostrar. Permite elegir diferentes tipos de etiqueta.
 *
 * </p>
 * <p>Copyright: Copyright (c) 2005-20176</p>
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
public class RepEtiqueta extends ventana
{
    utildesp utDesp=new utildesp();
    etiqueta etiq;
    String famProd;    
    private ayuLote ayuLot = null;
     
    public RepEtiqueta(EntornoUsuario eu, Principal p) {
        EU = eu;
        vl = p.panel1;
        jf = p;
        eje = true;

        setTitulo("Repetir Etiquetas");

        try {
            if (jf.gestor.apuntar(this)) {
                jbInit();
            } else {
                setErrorInit(true);
            }
        } catch (Exception e) {
          ErrorInit(e);
        }
    }

    public RepEtiqueta(gnu.chu.anjelica.menu p, EntornoUsuario eu) {

        EU = eu;
        vl = p.getLayeredPane();
        setTitulo("Repetir Etiquetas");
        eje = false;

        try {
            jbInit();
        } catch (Exception e) {
           ErrorInit(e);
        }
    }

    private void jbInit() throws Exception {

        iniciarFrame();

        this.setVersion("2017-07-06");
        statusBar = new StatusBar(this);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        conecta();

        initComponents();
        this.setSize(new Dimension(730, 530));
    }

    @Override
    public void iniciarVentana() throws Exception {
        pro_codiE.iniciar(dtStat, this, vl, EU);
        pro_codiE.setCamposLote(pro_ejercE, pro_serieE, pro_loteE, pro_numindE, deo_kilosE);
        pdalmace.llenaLinkBox(alm_codiE, dtStat, '*');
        alm_codiE.setValorInt(1);
        pro_ejercE.setText("" + EU.ejercicio);
        pro_serieE.setText("A");
        etiq = new etiqueta(EU);
        tipetiqE.setDatos(etiqueta.getReports(dtStat, EU.em_cod, 0));
        trazPanel.iniciar(dtStat,dtCon1,this,vl,EU);
        trazPanel.setEditable(true);
        Pprinc.setDefButton(Baceptar.getBotonAccion());
        resetCambio();
        activarEventos();
        pro_codiE.requestFocusLater();
    }
     public static String getNombreClase()
   {        
     return "gnu.chu.anjelica.listados.RepEtiqueta";
   }
   public void setEjercicio(int ejerc)
   {
       pro_ejercE.setValorInt(ejerc);
   }
   public void setSerie(String serie)
   {
       pro_serieE.setText(serie);
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
     public void consulta0()
    {      
        buscaPeso();
        Bindi_actionPerformed();
    }
    private void activarEventos()
    {
        pro_ejercE.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F3)
                    ayudaLote();
            }
        });
        pro_serieE.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F3)
                    ayudaLote();
            }
        });
        pro_numindE.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F3)
                    ayudaLote();
            }
        });
        pro_loteE.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F3)
                    ayudaLote();
            }
        });
        Baceptar.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                listar(e.getActionCommand().startsWith("Listar"));
            }
        });
        pro_numindE.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e) {
                if (!cambioInd())
                    return;
                buscaPeso();
            }
        });
        Bindi.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusGained(FocusEvent e) {

                if (e.getOppositeComponent() == deo_kilosE)
                    Bindi.doClick();
                else
                    deo_kilosE.requestFocus();
            }
        });
        Bindi.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                Bindi_actionPerformed();
            }
        });
        Bcancelar.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetCambio();
//                activar(true);
                pro_codiE.requestFocus();
                mensajeErr("Listado .. Cancelado");
            }
        });
    }
    boolean cambioInd()
    {
     return deo_kilosE.hasCambio() || pro_numindE.hasCambio() || pro_ejercE.hasCambio() 
            || pro_loteE.hasCambio() || pro_codiE.hasCambio() || alm_codiE.hasCambio();
    }
    /**
   * Consulta Lotes Disponibles de Productos.
   */
  public void ayudaLote()
  {
    try
    {
      if (ayuLot == null)
      {
        ayuLot = new ayuLote(EU, vl, dtCon1, pro_codiE.getValorInt())
        {
          @Override
            public void matar(boolean cerrarConexion)
           {
            ayuLot.setVisible(false);
            ej_consLote();
           }
        };
        this.getLayeredPane().add(ayuLot,1);
//        vl.add(ayuLot);
        ayuLot.setIconifiable(false);
        ayuLot.setLocation(25, 25);
        ayuLot.iniciarVentana();
      }
      ayuLot.jt.removeAllDatos();
      ayuLot.setVisible(true);
      ayuLot.muerto = false;
      ayuLot.statusBar.setEnabled(true);
      ayuLot.statusBar.Bsalir.setEnabled(true);


      this.setEnabled(false);
      this.setFoco(ayuLot);
      ayuLot.cargaGrid(pro_codiE.getText(),alm_codiE.getValorInt());
      SwingUtilities.invokeLater(new Thread()
      {
        @Override
        public void run()
        {
          ayuLot.jt.requestFocusInicio();
        }
      });

    }
    catch (Exception j)
    {
      this.setEnabled(true);
    }
  }

  void ej_consLote()
  {
    if (ayuLot.consulta)
    {
      pro_ejercE.setValorDec(ayuLot.jt.getValorInt(ayuLot.rowAct, ayuLote.JT_EJE));
      pro_serieE.setText(ayuLot.jt.getValString(ayuLot.rowAct, ayuLote.JT_SER));
      pro_loteE.setValorDec(ayuLot.jt.getValorInt(ayuLot.rowAct, ayuLote.JT_LOTE));
      pro_numindE.setValorDec(ayuLot.jt.getValorInt(ayuLot.rowAct, ayuLote.JT_IND));
      deo_kilosE.setText(ayuLot.jt.getValString(ayuLot.rowAct, ayuLote.JT_PESO ));
// 
    }
    ayuLot.setVisible(false);
    this.setEnabled(true);
    this.toFront();
    try
    {
      this.setSelected(true);
    }
    catch (PropertyVetoException k)
    {}
    this.setFoco(null);
    deo_kilosE.requestFocusLater();
    
   }
  boolean checkIndividuo() throws SQLException
  {
      if (!pro_codiE.controlar())
      {
        mensajeErr(pro_codiE.getMsgError());
        return false;
      }
       famProd = pro_codiE.getFamilia();
      if (pro_ejercE.getValorInt() == 0)
      {
        mensajeErr("Introduzca Ejercicio de Lote");
        pro_ejercE.requestFocus();
        return false;
      }
//      if (deo_emplotE.getValorInt() == 0)
//      {
//        mensajeErr("Introduzca Empresa de Lote");
//        deo_emplotE.requestFocus();
//        return;
//      }
      if (pro_serieE.getText().trim().equals(""))
      {
        mensajeErr("Introduzca Serie de Lote");
        pro_serieE.requestFocus();
        return false;
      }
      if (pro_loteE.getValorInt() == 0)
      {
        mensajeErr("Introduzca N.de Lote");
        pro_loteE.requestFocus();
       return false;
      }
    
     
      if (deo_kilosE.getValorDec() == 0)
      {
        mensajeErr("Introduzca peso de Producto");
        deo_kilosE.requestFocus();
        return false;
      }
      return true;
  }
    void Bindi_actionPerformed()
    {
      try {
      if (muerto)
          return;
      if (!cambioInd())
          return;
      if (!checkIndividuo())
          return;
     
      trazPanel.setDatos(pro_codiE.getValorInt(),pro_ejercE.getValorInt(),pro_serieE.getText(),
           pro_loteE.getValorInt(),pro_numindE.getValorInt());
      trazPanel.actualizar();
      trazPanel.resetCambio();

      } catch (Exception k)
      {
        Error ("Error al controlar valores",k);
      }
     
     
    }
    void activar(boolean b)
    {
    
//        
//      pro_codiE.setEnabled(b);
//     
////        fecrepL.setText("Fecha Recep:");
////        deo_emplotE.setEnabled(b);
//        pro_ejercE.setEnabled(b);
//        pro_serieE.setEnabled(b);
//        pro_loteE.setEnabled(b);
//        pro_numindE.setEnabled(b);
//
//        Bindi.setEnabled(b);
//     
//      conservarE.setEnabled(!b);
     
    }

    boolean buscaPeso()
  {
      try
      {
          return buscaPeso(pro_ejercE.getValorInt(),
              pro_serieE.getText(),
              pro_loteE.getValorInt(),
              pro_numindE.getValorInt(),
              pro_codiE.getValorInt(),
              alm_codiE.getValorInt(), true);
      } catch (SQLException | ParseException k)
      {
          Error("Error al buscar Peso", k);
          return false;
      }
   
  }
  boolean buscaPeso(int ano,String serie,int partida,int indiv,int codProd,int almCodi,boolean ponPeso) throws SQLException,ParseException
  {
    try
    {
          if (utDesp==null)
            utDesp = new utildesp();
        StkPartid stkPart = utildesp.buscaPeso(dtCon1, ano,
                                       EU.em_cod,
                                       serie,
                                       partida,
                                       indiv,
                                       codProd,
                                       0);
        if (stkPart.hasError())
        {
            mensajeErr(stkPart.getMensaje());
            return false;
        }
        if (ponPeso)
            deo_kilosE.setValorDec(stkPart.getKilos());
        return true;
    }
    catch (SQLException k)
    {
      Error("Error al buscar Peso", k);
      return false;
    }
  }
    void resetCambio() {
        pro_numindE.resetCambio();
        deo_kilosE.resetCambio();
        pro_ejercE.resetCambio();
        alm_codiE.resetCambio();
        pro_codiE.resetCambio();
        pro_loteE.resetCambio();
        pro_codiE.resetCambio();
    }
  /**
   * Listar etiqueta y guardar los datos de trazabilidad si es necesario
   * @param guardar 
   */
    void listar(boolean guardar)
    {
      try
      {
        if (!checkIndividuo())
            return;
        mensajeErr("Espere, por favor ... Imprimiendo");
        etiq.setTipoEtiq(dtStat, EU.em_cod,
                          tipetiqE.getValorInt());
        
        CodigoBarras codBarras=new CodigoBarras("R",pro_ejercE.getText(),
                pro_serieE.getText(),
                pro_loteE.getValorInt(),pro_codiE.getValorInt(),
                pro_numindE.getValorInt(),
                deo_kilosE.getValorDec());
 
        etiq.setNumCopias(1);

        utDesp= trazPanel.getUtilDespiece(true);
         if ( utDesp.getFechaProduccion() != null && Formatear.comparaFechas(utDesp.getFecCaduc() , utDesp.getFechaProduccion() )<10)
        {
            msgBox("Fecha caducidad debe ser superior en 10 dias a fecha produccion");
            return;
        }
        if (utDesp.getFecSacrif()!=null && Formatear.comparaFechas(utDesp.getFechaProduccion(),utDesp.getFecSacrif() )<0)
        {
            msgBox("Fecha Sacrificio debe ser inferior a fecha produccion");
            return;
        }
        if (guardar && trazPanel.hasCambio())
            utDesp.actualTrazabilidad(dtAdd);
        etiq.iniciar(tipetiqE.getValorInt()!=etiqueta.ETIQINT?codBarras.getCodBarra():codBarras.getLote(false),
            codBarras.getLote(tipetiqE.getValorInt()!=etiqueta.ETIQINT),
            pro_codiE.getText(),pro_codiE.getTextNomb(),utDesp.getPaisNacimiento(),utDesp.getPaisEngorde(),
            utDesp.getSalaDespiece(),
            utDesp.getNumCrot(),deo_kilosE.getValorDec(),
            utDesp.getConservar(), utDesp.getMatadero(),
            utDesp.getFechaProduccion(),utDesp.getFechaProduccion(), utDesp.getFecCaduc(),
            utDesp.getFecSacrif());
        
        etiq.listarDefec();
              
        pro_codiE.requestFocus();
        dtAdd.commit();
        resetCambio();
        mensajeErr("Etiqueta Listada !");
      }
      catch (Throwable k)
      {
        Error("Error al Imprimir Etiqueta", k);
      }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Pprinc = new gnu.chu.controles.CPanel();
        cLabel9 = new gnu.chu.controles.CLabel();
        pro_codiE = new gnu.chu.camposdb.proPanel()
        {
            @Override
            protected void despuesLlenaCampos()
            {
                Bindi_actionPerformed();
                Baceptar.requestFocus();
            }
        }
        ;
        cLabel6 = new gnu.chu.controles.CLabel();
        pro_ejercE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        cLabel7 = new gnu.chu.controles.CLabel();
        pro_serieE = new gnu.chu.controles.CTextField(Types.CHAR,"X",1);
        cLabel8 = new gnu.chu.controles.CLabel();
        pro_loteE = new gnu.chu.controles.CTextField(Types.DECIMAL,"####9");
        cLabel3 = new gnu.chu.controles.CLabel();
        pro_numindE = new gnu.chu.controles.CTextField(Types.DECIMAL,"####9");
        cLabel4 = new gnu.chu.controles.CLabel();
        deo_kilosE = new gnu.chu.controles.CTextField(Types.DECIMAL, "--,--9.99");
        trazPanel = new gnu.chu.camposdb.DatTrazPanel();
        Baceptar = new gnu.chu.controles.CButtonMenu(Iconos.getImageIcon("print"));
        cLabel10 = new gnu.chu.controles.CLabel();
        numEtiqE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        cLabel11 = new gnu.chu.controles.CLabel();
        tipetiqE = new gnu.chu.controles.CComboBox();
        Bindi = new gnu.chu.controles.CButton();
        Bcancelar = new gnu.chu.controles.CButton(Iconos.getImageIcon("cancel"));
        cLabel12 = new gnu.chu.controles.CLabel();
        alm_codiE = new gnu.chu.controles.CLinkBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Pprinc.setLayout(null);

        cLabel9.setText("Producto");
        Pprinc.add(cLabel9);
        cLabel9.setBounds(10, 20, 60, 17);
        Pprinc.add(pro_codiE);
        pro_codiE.setBounds(70, 20, 340, 17);

        cLabel6.setText("Tipo List.");
        Pprinc.add(cLabel6);
        cLabel6.setBounds(150, 230, 60, 17);
        Pprinc.add(pro_ejercE);
        pro_ejercE.setBounds(40, 40, 40, 17);

        cLabel7.setText("Serie");
        Pprinc.add(cLabel7);
        cLabel7.setBounds(90, 40, 40, 17);

        pro_serieE.setAutoNext(true);
        pro_serieE.setMayusc(true);
        Pprinc.add(pro_serieE);
        pro_serieE.setBounds(130, 40, 20, 17);

        cLabel8.setText("Lote");
        Pprinc.add(cLabel8);
        cLabel8.setBounds(160, 40, 30, 17);
        Pprinc.add(pro_loteE);
        pro_loteE.setBounds(190, 40, 40, 17);

        cLabel3.setText("Kilos");
        Pprinc.add(cLabel3);
        cLabel3.setBounds(320, 40, 30, 17);
        Pprinc.add(pro_numindE);
        pro_numindE.setBounds(270, 40, 40, 17);

        cLabel4.setText("Ind");
        Pprinc.add(cLabel4);
        cLabel4.setBounds(240, 40, 25, 17);
        Pprinc.add(deo_kilosE);
        deo_kilosE.setBounds(360, 40, 50, 17);

        trazPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Pprinc.add(trazPanel);
        trazPanel.setBounds(10, 70, 510, 150);

        Baceptar.setText("Listar");
        Baceptar.addMenu("Listar y Guardar", "G");
        Baceptar.addMenu("Solo Listar", "L");
        Pprinc.add(Baceptar);
        Baceptar.setBounds(350, 230, 110, 26);

        cLabel10.setText("Almacen ");
        Pprinc.add(cLabel10);
        cLabel10.setBounds(10, 2, 60, 17);

        numEtiqE.setText("1");
        Pprinc.add(numEtiqE);
        numEtiqE.setBounds(110, 230, 30, 17);

        cLabel11.setText("Num. Etiquetas ");
        Pprinc.add(cLabel11);
        cLabel11.setBounds(20, 230, 90, 17);
        Pprinc.add(tipetiqE);
        tipetiqE.setBounds(210, 230, 130, 17);
        Pprinc.add(Bindi);
        Bindi.setBounds(420, 40, 20, 20);
        Pprinc.add(Bcancelar);
        Bcancelar.setBounds(470, 230, 45, 20);

        cLabel12.setText("Ejerc");
        Pprinc.add(cLabel12);
        cLabel12.setBounds(10, 40, 30, 17);

        alm_codiE.setAncTexto(30);
        alm_codiE.setFormato(true);
        alm_codiE.setFormato(Types.DECIMAL, "#9", 2);
        alm_codiE.combo.setPreferredSize(new Dimension(200,17));
        Pprinc.add(alm_codiE);
        alm_codiE.setBounds(70, 2, 240, 17);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButtonMenu Baceptar;
    private gnu.chu.controles.CButton Bcancelar;
    private gnu.chu.controles.CButton Bindi;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CLinkBox alm_codiE;
    private gnu.chu.controles.CLabel cLabel10;
    private gnu.chu.controles.CLabel cLabel11;
    private gnu.chu.controles.CLabel cLabel12;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.controles.CLabel cLabel9;
    private gnu.chu.controles.CTextField deo_kilosE;
    private gnu.chu.controles.CTextField numEtiqE;
    private gnu.chu.camposdb.proPanel pro_codiE;
    private gnu.chu.controles.CTextField pro_ejercE;
    private gnu.chu.controles.CTextField pro_loteE;
    private gnu.chu.controles.CTextField pro_numindE;
    private gnu.chu.controles.CTextField pro_serieE;
    private gnu.chu.controles.CComboBox tipetiqE;
    private gnu.chu.camposdb.DatTrazPanel trazPanel;
    // End of variables declaration//GEN-END:variables
}
