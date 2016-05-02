package gnu.chu.anjelica.almacen;
/**
 *
 * <p>Titulo: Comvalm </p>
 * <p>Descripcion: Consulta Mvtos de Almacen Valorados</p>
 * <p>Copyright: Copyright (c) 2005-2016
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
 * @author chuchiP
 * @version 1.1
 * @parameters verprecio= 0 (No)/ 1 (Si)
 */
import gnu.chu.controles.StatusBar;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.ventana;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import javax.swing.JPopupMenu;
import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.compras.MantAlbCom;
import gnu.chu.anjelica.compras.MantAlbComCarne;
import gnu.chu.anjelica.compras.MantAlbComPlanta;
import gnu.chu.anjelica.despiece.MantDesp;
import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.anjelica.ventas.pdalbara;
import gnu.chu.camposdb.empPanel;
import gnu.chu.interfaces.ejecutable;
import gnu.chu.utilidades.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;


public class Comvalm extends ventana
{
  boolean P_VERPRECIO=false;
  JPopupMenu JpopupMenu = new JPopupMenu("Filtro");
  private MvtosAlma mvtosAlm = new MvtosAlma();
  private boolean cancelarConsulta=false;
  String fecMov=null;
  int almCodi;
  //boolean acepNeg=false; // Aceptar Negativos (por defecto false)
  final int JT_TIPO=3;
  final int JT_DOCUM=9;
  final int JT_FECDOC=10;
  final int JT_CLIPRV=11;
  final int JT_LOTE=12;
  final int JT_ALMACEN=14;
  
  public Comvalm(EntornoUsuario eu, Principal p)
  {
      this(eu,p,null);
  }
  
 public Comvalm(EntornoUsuario eu, Principal p,Hashtable<String,String> ht)
  {
   
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    setTitulo("Cons. Mvtos Almacen ");
    ponParametros(ht);
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
    }
  }
  public Comvalm(gnu.chu.anjelica.menu p, EntornoUsuario eu)
  {
      this(p,eu,null);
  }
  public Comvalm(gnu.chu.anjelica.menu p, EntornoUsuario eu,Hashtable<String,String> ht)
  {

    EU = eu;
    vl = p.getLayeredPane();
    setTitulo("Cons. Mvtos Almacen ");
    ponParametros(ht);
    eje = false;

    try
    {
      jbInit();
    }
    catch (Exception e)
    {
        ErrorInit(e);
    }
  }
   public static String getNombreClase()
   {
     return "gnu.chu.anjelica.almacen.Comvalm";
   }
  private void ponParametros(Hashtable<String,String> ht)
  {
      if (ht==null)
          return;
       if (ht.get("verprecio") != null)
       {
           try {
             P_VERPRECIO = Integer.parseInt(ht.get("verprecio"))!=0;
           } catch ( NumberFormatException  x)
           {
               
           }
       }
      
  }
  private void jbInit() throws Exception
  {
      iniciarFrame();

      this.setVersion("2016-05-02");
      statusBar = new StatusBar(this);
      this.getContentPane().add(statusBar, BorderLayout.SOUTH);
      conecta();

      initComponents();
      this.setSize(new Dimension(730,480));
      JpopupMenu.add(opTodos);
      JpopupMenu.add(opNinguno);
      JpopupMenu.add(opVentas);
      JpopupMenu.add(opSalidaDep);
      JpopupMenu.add(opCompras);
      JpopupMenu.add(opDespEnt);
      JpopupMenu.add(opDespSal);
      JpopupMenu.add(opRegul);
      JpopupMenu.add(opInven);
      JpopupMenu.add(opInventDep);
      JpopupMenu.add(opTrasp);
      Pentra.setDefButton(Bacepta);
  }
  
  @Override
  public void iniciarVentana() throws Exception
  {
    String s;
    jt.setConfigurar("Comvalm", EU, dtStat);
    mvtosAlm.setAccesoEmp(empPanel.getStringAccesos(dtStat, EU.usuario,true));
    jt.setConfigurar("gnu.chu.anjelica.almacen.Comvalm",EU,dtStat);
    alm_codiE.setFormato(true);
    alm_codiE.setFormato(Types.DECIMAL, "#9", 2);
    pdalmace.llenaLinkBox(alm_codiE, dtStat,'*');
//    s = "SELECT alm_codi,alm_nomb FROM v_almacen " +
//        " ORDER BY alm_codi";
//    dtStat.select(s);
//    alm_codiE.addDatos(dtStat);
    alm_codiE.setText("0");
    pro_codiE.iniciar(dtStat, this, vl, EU);
    Pentra.setButton(KeyEvent.VK_F4,Bacepta);
    String feulin=MvtosAlma.llenaComboFecInv(dtStat,EU.em_cod,EU.ejercicio,feulinE,0);
    feciniE.setText(feulin);
    fecfinE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
   
    
    pro_numindE.setText("",false);
    activarEventos();
    this.setEnabled(true);
    PtotalE.setEnabled(false);
     pro_codiE.requestFocus();    
  }
  public void setProCodi(int proCodi)
  {
      pro_codiE.setValorInt(proCodi,true);
  }
  public void setLote(int lote)
  {
      pro_loteE.setValorInt(lote);
  }
  public void setIndividuo(int indiv)
  {
      pro_numindE.setValorInt(indiv);
  }
  public void setSerie(String serie)
  {
      pro_serieE.setText(serie);
  }
   public void setEjercicio(int ejerc)
  {
      pro_ejercE.setValorInt(ejerc);
  }
  public void setFechaInicial(Date fecini)
  {
      feciniE.setDate(fecini);
  }
  public void setFechaFinal(Date fecfin)
  {
      fecfinE.setDate(fecfin);
  }
  public void setFechaInventario(Date fechaInv) throws ParseException
  {
      feulinE.setDate(fechaInv);
  }
  public void ejecutaConsulta()
  {
      Bacepta.doClick();
  }

   void activarEventos()
  {
     MVerDoc.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
       verDocumento();           
      }
    });
     MVerStock.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        irStockPartidas(jt.getValString(JT_LOTE),pro_codiE.getValorInt(),jt.getValorInt(JT_ALMACEN));
      }
    });
     Breset.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        pro_loteE.resetTexto();
        pro_ejercE.resetTexto();
        pro_serieE.resetTexto();
        pro_numindE.resetTexto();
      }
    });
     Bfiltro.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JpopupMenu.show(Bfiltro,0,24);
      }
    });
    opTodos.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (opTodos.isSelected())
        {
            activaTodoFiltro(true);    
            opNinguno.setSelected(false);
        }
        JpopupMenu.show(Bfiltro,0,24);
      }
    });
    opNinguno.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (opNinguno.isSelected())
        {
            activaTodoFiltro(false);    
            opTodos.setSelected(false);
        }
        JpopupMenu.show(Bfiltro,0,24);
      }
    });
    ActionListener al1=new ActionListener() 
    {
      @Override
      public void actionPerformed(ActionEvent e) {
          opTodos.setSelected(false);
          opNinguno.setSelected(false);
          JpopupMenu.show(Bfiltro,0,24);
      }
    };
    opTrasp.addActionListener(al1);
    opCompras.addActionListener(al1);
    opSalidaDep.addActionListener(al1);
    opInventDep.addActionListener(al1);
    opVentas.addActionListener(al1);
    opDespEnt.addActionListener(al1);
    opDespSal.addActionListener(al1);
    opRegul.addActionListener(al1);
    opInven.addActionListener(al1);
        
    Bacepta.addActionListener(new ActionListener()
    {
            @Override
      public void actionPerformed(ActionEvent e)
      {
        buscaMvtos();
      }
    });
      popEspere_BCancelaraddActionListener(new ActionListener()
       {
        @Override
        public void actionPerformed(ActionEvent e)
        {
           mvtosAlm.setCancelarConsulta(true);
           mensaje("A esperar.. estoy cancelando la consulta");
        }
       });
     feciniE.addFocusListener(new FocusAdapter() {
          @Override
         public void focusLost(FocusEvent e) {
             if (feciniE.isNull())
                feciniE.setText(feulinE.getText());
         }
     });
     jt.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
              if (e.getClickCount()<2)
                  return;
              verDocumento();
  }
      });
  }
  void verDocumento()
  {
    if (jf==null)
        return;
     msgEspere("Ejecutando consulta para visualizar Documento");
     new miThread("")
     {
        @Override
        public void run()
        {
          javax.swing.SwingUtilities.invokeLater(new Thread()
          {
              @Override
              public void run()
              { 
                  ejecutable prog;
                  String doc[] = jt.getValString(JT_DOCUM).split("-");
                  if (jt.getValString(JT_TIPO).toUpperCase().startsWith("D"))
                  {
                      if ((prog = jf.gestor.getProceso(MantDesp.getNombreClase())) == null)
                          return;
                      MantDesp cm = (MantDesp) prog;
                      if (cm.inTransation())
                      {
                          msgBox("Mantenimiento Despieces ocupado. No se puede realizar la busqueda");
                          return;
                      }

                      cm.PADQuery();
                      cm.setEjeNume(doc[0]);
                      cm.setDeoCodi(doc[2]);
                      cm.ej_query();
                      jf.gestor.ir(cm);
                  }
                  if (jt.getValString(JT_TIPO).startsWith("V"))
                  {
                      if ((prog = jf.gestor.getProceso(pdalbara.getNombreClase())) == null)
                          return;
                      pdalbara cm = (pdalbara) prog;
                      if (cm.inTransation())
                      {
                          msgBox("Mantenimiento Albaranes de Ventas ocupado. No se puede realizar la busqueda");
                          return;
                      }
                      cm.PADQuery();
                      cm.setEjercAlbaran(Integer.parseInt(doc[0]));
                      cm.setSerieAlbaran(doc[1]);
                      cm.setNumeroAlbaran(doc[2]);

                      cm.ej_query();
                      jf.gestor.ir(cm);
                  }

                  if (jt.getValString(JT_TIPO).startsWith("C"))
                  {
                      try
                      {
                          if (pdconfig.getTipoEmpresa(EU.em_cod, dtStat) == pdconfig.TIPOEMP_PLANTACION)
                          {
                              if ((prog = jf.gestor.getProceso(MantAlbComPlanta.getNombreClase())) == null)
                                  return;
                          } else
                          {
                              if ((prog = jf.gestor.getProceso(MantAlbComCarne.getNombreClase())) == null)
                                  return;
                          }

                          MantAlbCom cm = (MantAlbCom) prog;
                          if (cm.inTransation())
                          {
                              msgBox("Mantenimiento Albaranes de Compras ocupado. No se puede realizar la busqueda");
                              return;
                          }
                          cm.PADQuery();
                          cm.setEjeNume(Integer.parseInt(doc[0]));
                          cm.setAccCodi(doc[2]);
                          cm.ej_query();
                          jf.gestor.ir(cm);
                      } catch (SQLException ex)
                      {
                          Error("Error al ir al programa", ex);
                      }
                  }
                  if (jt.getValString(JT_TIPO).startsWith("R"))
                  {
                      if ((prog = jf.gestor.getProceso(pdregalm.getNombreClase())) == null)
                          return;
                      pdregalm cm = (pdregalm) prog;
                      if (cm.inTransation())
                      {
                          msgBox("Mantenimiento Regularizaciones Almacen ocupado. No se puede realizar la busqueda");
                          return;
                      }
                      cm.PADQuery();
                      cm.setNumeroRegula(Integer.parseInt(doc[2]));

                      cm.ej_query();
                      jf.gestor.ir(cm);

                  }
                  resetMsgEspere();
              }
          });
          
        }
     };

  
   
   
  }
  void  activaTodoFiltro(boolean sel)
  {
      opCompras.setSelected(sel);
      opVentas.setSelected(sel);
      opDespEnt.setSelected(sel);
      opDespSal.setSelected(sel);
      opRegul.setSelected(sel);     
      opInven.setSelected(sel);   
      opTrasp.setSelected(sel);   
//      opSalidaDep.setSelected(!sel);
//      opInventDep.setSelected(!sel);
  }
  
  void irStockPartidas(final String lote,final int proCodi,final int almCodi)
  {
      msgEspere("Ejecutando consulta en Stock Partidas");
        new miThread("")
     {
                @Override
                public void run()
                {
                  javax.swing.SwingUtilities.invokeLater(new Thread()
                  {
                      @Override
                      public void run()
                      {                   
                        ejecutable prog;
                        if ((prog=jf.gestor.getProceso(costkpar.getNombreClase()))==null)
                          return;
                        costkpar cm=(costkpar) prog;
                        String lot[]=lote.split("-");
                        cm.setEjercicio(Integer.parseInt(lot[0]));
                        cm.setSerie(lot[1]);
                        cm.setPartida(Integer.parseInt(lot[2]));
                        cm.setIndividuo(Integer.parseInt(lot[3]));
                        cm.setProducto(proCodi);
                        cm.setAlmacen(almCodi);
                        cm.setVerSoloConStock(false);
                        cm.consulta0();
                        jf.gestor.ir(cm);
                        resetMsgEspere();
                      }
                  });
                }
      };
  }
  
  void buscaMvtos()
  {
    if (fecfinE.getError() || fecfinE.isNull())
    {
      mensajeErr("Fecha Final NO VALIDA");
      return;
    }
    if (feciniE.getError() || feciniE.isNull())
    {
      mensajeErr("Fecha Inicial NO Valida");
      return;
    }
    if (pro_codiE.isNull())
    {
      mensajeErr("Introduzca Codigo de Producto");
      pro_codiE.requestFocus();
      return;
    }
    new miThread("")
    {
        @Override
        public void run()
        {
          try
          {
            jt.panelG.setVisible(false);
            msgEspere("Buscando datos ... ");
           
            buscaStock1(feulinE.getText(), feciniE.getText(), fecfinE.getText(),
                       pro_codiE.getValorInt());

            resetMsgEspere();

            jt.panelG.setVisible(true);
            if (cancelarConsulta)
            {
               mensajeErr("Consulta CANCELADA!!");
               jt.removeAllDatos();
            }
            else
               mensajeErr("OKEI -- MAKEI");
            mensaje("");
            
          }
          catch (Exception ex)
          {
            Error("Error al buscar Stock", ex);
          }
        }
    };

  }

  void buscaStock1(String feulin,String fecIni, String fecFin, int proCodi) throws Exception
  {
     cancelarConsulta=false;
     mvtosAlm.setCancelarConsulta(false);
     fecMov=null;
     if (alm_codiE.getText().trim().equals(""))
       almCodi=0;
     else
       almCodi=alm_codiE.getValorInt();
    

    mvtosAlm.setPadre(this);
    mvtosAlm.setIncUltFechaInv(true);
    mvtosAlm.setIncIniFechaInv(true);
    mvtosAlm.setLote(pro_loteE.getValorInt());
    mvtosAlm.setIndividuo(pro_numindE.getValorInt());
    mvtosAlm.setSerieLote(pro_serieE.getText());
    mvtosAlm.setEjerLote(pro_ejercE.getValorInt());
    mvtosAlm.setVerPrecios(P_VERPRECIO);
    mvtosAlm.setDesglIndiv(pro_numindE.getValorInt()>0);
    mvtosAlm.setAlmacen(almCodi);
  
    mvtosAlm.setIncluyeSerieX(opTrasp.isSelected());
    mvtosAlm.setIgnDespSinValor(true);
    mvtosAlm.setVerRegul(opRegul.isSelected());
    mvtosAlm.setVerVenta(opVentas.isSelected());
    mvtosAlm.setVerCompra(opCompras.isSelected());
    mvtosAlm.setUseMvtos(opMvtos.isSelected());
    mvtosAlm.setIncSalidaDep(opSalidaDep.isSelected());
    mvtosAlm.setVerSalidaDep(opSalidaDep.isSelected());
    mvtosAlm.setIncInventDep(opInventDep.isSelected());
    mvtosAlm.setVerInventDep(opInventDep.isSelected());
    mvtosAlm.setFechasDocumento(! tipfecC.getValor().equals("M"));
    mvtosAlm.setCostoFijo(-1);
    
    mvtosAlm.setVerDesEnt(opDespEnt.isSelected());
    mvtosAlm.setVerDespSal(opDespSal.isSelected());
    mvtosAlm.setResetCostoStkNeg(opStkNeg.isSelected());
    mvtosAlm.setIncrementarCosto(pro_cosincE.getValorDec());
    if (! mvtosAlm.calculaMvtos(feulin,fecIni,fecFin,proCodi,null,null,pro_loteE.getValorInt(),dtCon1,dtStat,jt))
    {
        msgBox("No encontrados movimientos para estas condiciones");
        jt.removeAllDatos();
        PtotalE.resetTexto();
        return;
    }

    kgVentaE.setValorDec(mvtosAlm.getKilosVenta());
    impVentaE.setValorDec(!P_VERPRECIO?0:mvtosAlm.getImporteVenta());
    pmVentaE.setValorDec(!P_VERPRECIO?0:
        mvtosAlm.getImporteVenta()==0?0:mvtosAlm.getImporteVenta()/mvtosAlm.getKilosVenta());

    kgCompraE.setValorDec(mvtosAlm.getKilosEntrada());
    impCompra1.setValorDec(!P_VERPRECIO?0:mvtosAlm.getImporteEntrada());
    pmCompraE.setValorDec(!P_VERPRECIO?0:
        mvtosAlm.getKilosEntrada()==0?0:mvtosAlm.getImporteEntrada()/mvtosAlm.getKilosEntrada());

    impGanaE.setValorDec(!P_VERPRECIO?0:mvtosAlm.getImpGana());
    ganKilE.setValorDec(!P_VERPRECIO?0:
        mvtosAlm.getKilosVenta()==0?0:mvtosAlm.getImpGana()/mvtosAlm.getKilosVenta());
//    ganKilPorE.setValorDec((pmCompraE.getValorDec()==0  || ganKilE.getValorDec() == 0)
//            ? 0:ganKilE.getValorDec()/ pmCompraE.getValorDec()*100 );
    kgDesEntE.setValorDec(mvtosAlm.getKilosEntDesp());
    kgDesSalE.setValorDec(mvtosAlm.getKilosSalDesp());

  }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        opNinguno = new javax.swing.JCheckBoxMenuItem();
        opVentas = new javax.swing.JCheckBoxMenuItem();
        opDespSal = new javax.swing.JCheckBoxMenuItem();
        opCompras = new javax.swing.JCheckBoxMenuItem();
        opDespEnt = new javax.swing.JCheckBoxMenuItem();
        opRegul = new javax.swing.JCheckBoxMenuItem();
        opTodos = new javax.swing.JCheckBoxMenuItem();
        opInven = new javax.swing.JCheckBoxMenuItem();
        opTrasp = new javax.swing.JCheckBoxMenuItem();
        MVerStock = new javax.swing.JMenuItem();
        MVerDoc = new javax.swing.JMenuItem();
        opSalidaDep = new javax.swing.JCheckBoxMenuItem();
        opInventDep = new javax.swing.JCheckBoxMenuItem();
        Pprinc = new gnu.chu.controles.CPanel();
        Pentra = new gnu.chu.controles.CPanel();
        cLabel1 = new gnu.chu.controles.CLabel();
        feulinE = new gnu.chu.controles.CComboBox();
        cLabel2 = new gnu.chu.controles.CLabel();
        feciniE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel3 = new gnu.chu.controles.CLabel();
        fecfinE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel4 = new gnu.chu.controles.CLabel();
        pro_codiE = new gnu.chu.camposdb.proPanel()
        {
            @Override
            public void afterFocusLost(boolean error)
            {
                if (!error)
                return;
                try {
                    pro_cosincE.setValorDec(getLikeProd().getDouble("pro_cosinc"));

                } catch (Exception k )
                {
                    Error("Error al buscar Costo a incrementar al producto",k);
                }
            }}
            ;
            cLabel5 = new gnu.chu.controles.CLabel();
            pro_loteE = new gnu.chu.controles.CTextField(Types.DECIMAL,"####9");
            pro_numindE = new gnu.chu.controles.CTextField(Types.DECIMAL,"####9");
            pro_serieE = new gnu.chu.controles.CTextField(Types.CHAR,"X",1);
            cLabel6 = new gnu.chu.controles.CLabel();
            cLabel7 = new gnu.chu.controles.CLabel();
            cLabel8 = new gnu.chu.controles.CLabel();
            cLabel9 = new gnu.chu.controles.CLabel();
            alm_codiE = new gnu.chu.controles.CLinkBox();
            cLabel10 = new gnu.chu.controles.CLabel();
            pro_ejercE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
            pro_cosincE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9.99");
            tipfecC = new gnu.chu.controles.CComboBox();
            opStkNeg = new gnu.chu.controles.CCheckBox();
            opCostFij = new gnu.chu.controles.CCheckBox();
            Bacepta = new gnu.chu.controles.CButton(Iconos.getImageIcon("check"));
            Bfiltro = new gnu.chu.controles.CButton(Iconos.getImageIcon("filter"));
            opMvtos = new gnu.chu.controles.CCheckBox();
            Breset = new gnu.chu.controles.CButton(Iconos.getImageIcon("quita"));
            jt = new gnu.chu.controles.Cgrid(15);
            PtotalE = new gnu.chu.controles.CPanel();
            Pventas = new gnu.chu.controles.CPanel();
            cLabel11 = new gnu.chu.controles.CLabel();
            kgVentaE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---,--9.99");
            cLabel12 = new gnu.chu.controles.CLabel();
            impVentaE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--,---,--9.99");
            cLabel13 = new gnu.chu.controles.CLabel();
            pmVentaE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---9.99");
            PEntra = new gnu.chu.controles.CPanel();
            cLabel14 = new gnu.chu.controles.CLabel();
            kgCompraE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---,--9.99");
            cLabel15 = new gnu.chu.controles.CLabel();
            impCompra1 = new gnu.chu.controles.CTextField(Types.DECIMAL,"--,---,--9.99");
            cLabel16 = new gnu.chu.controles.CLabel();
            pmCompraE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---9.99");
            PEntra1 = new gnu.chu.controles.CPanel();
            cLabel17 = new gnu.chu.controles.CLabel();
            impGanaE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----,--9.99");
            cLabel18 = new gnu.chu.controles.CLabel();
            ganKilE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---9.999");
            pmCompraE1 = new gnu.chu.controles.CTextField(Types.DECIMAL,"---9.99");
            PEntra2 = new gnu.chu.controles.CPanel();
            cLabel19 = new gnu.chu.controles.CLabel();
            kgDesEntE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---,--9.99");
            cLabel20 = new gnu.chu.controles.CLabel();
            kgDesSalE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---,--9.99");

            opNinguno.setMnemonic('N');
            opNinguno.setText("Ninguno");

            opVentas.setMnemonic('V');
            opVentas.setSelected(true);
            opVentas.setText("Ventas");

            opDespSal.setMnemonic('S');
            opDespSal.setSelected(true);
            opDespSal.setText("Salida Desp.");

            opCompras.setMnemonic('C');
            opCompras.setSelected(true);
            opCompras.setText("Compras");

            opDespEnt.setMnemonic('E');
            opDespEnt.setSelected(true);
            opDespEnt.setText("Entr. Desp.");

            opRegul.setMnemonic('R');
            opRegul.setSelected(true);
            opRegul.setText("Regularizaciones");

            opTodos.setSelected(true);
            opTodos.setText("Todo");

            opInven.setMnemonic('I');
            opInven.setSelected(true);
            opInven.setText("Inventario");
            opInven.setToolTipText("Inventario");

            opTrasp.setMnemonic('I');
            opTrasp.setSelected(true);
            opTrasp.setText("Traspasos");
            opTrasp.setToolTipText("Traspaso entre almacenes");

            MVerStock.setText("Ver Stock");

            MVerDoc.setText("Ver Documento");

            opSalidaDep.setMnemonic('V');
            opSalidaDep.setText("Sal.Deposito");

            opInventDep.setMnemonic('V');
            opInventDep.setText("Inv.Deposito");

            Pprinc.setLayout(new java.awt.GridBagLayout());

            Pentra.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
            Pentra.setMaximumSize(new java.awt.Dimension(729, 75));
            Pentra.setMinimumSize(new java.awt.Dimension(729, 75));
            Pentra.setPreferredSize(new java.awt.Dimension(729, 75));
            Pentra.setLayout(null);

            cLabel1.setText("Almacen");
            Pentra.add(cLabel1);
            cLabel1.setBounds(2, 45, 60, 17);
            Pentra.add(feulinE);
            feulinE.setBounds(70, 2, 110, 17);

            cLabel2.setText("Fec. Ult. Inv ");
            Pentra.add(cLabel2);
            cLabel2.setBounds(2, 2, 70, 17);
            Pentra.add(feciniE);
            feciniE.setBounds(335, 2, 70, 17);

            cLabel3.setText("Ind");
            Pentra.add(cLabel3);
            cLabel3.setBounds(650, 25, 25, 17);
            Pentra.add(fecfinE);
            fecfinE.setBounds(460, 2, 70, 17);

            cLabel4.setText("De Fecha");
            Pentra.add(cLabel4);
            cLabel4.setBounds(190, 2, 60, 17);
            Pentra.add(pro_codiE);
            pro_codiE.setBounds(70, 25, 300, 17);

            cLabel5.setText("Incr Costos");
            Pentra.add(cLabel5);
            cLabel5.setBounds(600, 2, 70, 17);
            Pentra.add(pro_loteE);
            pro_loteE.setBounds(600, 25, 40, 17);
            Pentra.add(pro_numindE);
            pro_numindE.setBounds(670, 25, 50, 17);

            pro_serieE.setAutoNext(true);
            pro_serieE.setMayusc(true);
            Pentra.add(pro_serieE);
            pro_serieE.setBounds(540, 25, 20, 17);

            cLabel6.setText("Ejerc");
            Pentra.add(cLabel6);
            cLabel6.setBounds(420, 25, 30, 17);

            cLabel7.setText("Serie");
            Pentra.add(cLabel7);
            cLabel7.setBounds(500, 25, 40, 17);

            cLabel8.setText("Lote");
            Pentra.add(cLabel8);
            cLabel8.setBounds(570, 25, 30, 17);

            cLabel9.setText("Producto");
            Pentra.add(cLabel9);
            cLabel9.setBounds(2, 25, 60, 17);

            alm_codiE.setAncTexto(30);
            Pentra.add(alm_codiE);
            alm_codiE.setBounds(70, 45, 250, 17);

            cLabel10.setText("A Fecha");
            Pentra.add(cLabel10);
            cLabel10.setBounds(410, 2, 50, 17);
            Pentra.add(pro_ejercE);
            pro_ejercE.setBounds(450, 25, 40, 17);

            pro_cosincE.setToolTipText("Incrementar costo");
            Pentra.add(pro_cosincE);
            pro_cosincE.setBounds(670, 2, 50, 17);

            tipfecC.addItem("Movimiento","M");
            tipfecC.addItem("Documento","D");
            Pentra.add(tipfecC);
            tipfecC.setBounds(245, 2, 90, 17);

            opStkNeg.setSelected(true);
            opStkNeg.setText("Reset Costo");
            opStkNeg.setToolTipText("Resetar Costo si Stock < 0");
            Pentra.add(opStkNeg);
            opStkNeg.setBounds(430, 45, 90, 17);

            opCostFij.setText("Costos Fijos");
            opCostFij.setToolTipText("Usar Costos Fijos Desp.");
            Pentra.add(opCostFij);
            opCostFij.setBounds(340, 45, 90, 22);

            Bacepta.setText("Aceptar");
            Bacepta.setToolTipText("");
            Pentra.add(Bacepta);
            Bacepta.setBounds(610, 45, 100, 24);

            Bfiltro.setText("Filtrar");
            Pentra.add(Bfiltro);
            Bfiltro.setBounds(520, 45, 70, 24);

            opMvtos.setSelected(true);
            opMvtos.setText("Mvtos");
            opMvtos.setToolTipText("Usar mvtos de almacen / Documentos ");
            Pentra.add(opMvtos);
            opMvtos.setBounds(530, 2, 70, 17);

            Breset.setToolTipText("Resetear Inidividuo");
            Pentra.add(Breset);
            Breset.setBounds(380, 25, 20, 20);

            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
            gridBagConstraints.weightx = 1.0;
            Pprinc.add(Pentra, gridBagConstraints);

            jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
            jt.setMaximumSize(new java.awt.Dimension(102, 102));
            jt.setMinimumSize(new java.awt.Dimension(102, 102));

            ArrayList v = new ArrayList();
            v.add("Fecha"); // 0
            v.add("Entr."); // 1
            v.add("Salida"); // 2
            v.add("Tipo"); // 3
            v.add("Precio"); // 4
            v.add("Unid"); // 5
            v.add("Kilos"); // 6
            v.add("Costo"); // 7
            v.add("Ganan"); // 8
            v.add("Documento");  //9
            v.add("Fec.Doc");  //10
            v.add("Cli/Prv/Otros"); // 11
            v.add("Ej/Serie/Lote/Ind"); // 12
            v.add("Gan.Kg");  // 13
            v.add("Alm");  // 14
            jt.setCabecera(v);
            jt.setToolTipText("Doble click para ir a Mvto");
            jt.setAnchoColumna(new int[]
                {110, 60, 60, 30, 55,50, 70, 55, 65,90,70,110,80,45,30});
            jt.setAlinearColumna(new int[]
                {1, 2, 2, 1, 2, 2,2, 2, 2,0,1,0,0,2,2});
            jt.setFormatoColumna(0,"dd-MM-yy HH:mm");
            jt.setFormatoColumna(1,"---9.99");
            jt.setFormatoColumna(2,"---9.99");
            jt.setFormatoColumna(4,"---9.99");
            jt.setFormatoColumna(5,"----9");
            jt.setFormatoColumna(6,"--,--9.9");
            jt.setFormatoColumna(7,"--9.99");
            jt.setFormatoColumna(8,"--,--9.99");
            jt.setFormatoColumna(13,"---9.99");
            jt.setFormatoColumna(JT_FECDOC,"dd-MM-yy");
            jt.setMinimumSize(new Dimension(422, 282));
            jt.setPreferredSize(new Dimension(422, 282));
            jt.setAjustarGrid(true);
            jt.setAjustarColumnas(true);
            jt.getPopMenu().add(MVerDoc);
            jt.getPopMenu().add(MVerStock);

            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 1.0;
            Pprinc.add(jt, gridBagConstraints);

            PtotalE.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
            PtotalE.setMaximumSize(new java.awt.Dimension(725, 82));
            PtotalE.setMinimumSize(new java.awt.Dimension(725, 82));
            PtotalE.setName(""); // NOI18N
            PtotalE.setPreferredSize(new java.awt.Dimension(725, 82));
            PtotalE.setLayout(null);

            Pventas.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Ventas"));
            Pventas.setLayout(null);

            cLabel11.setText("Kgs");
            Pventas.add(cLabel11);
            cLabel11.setBounds(10, 15, 30, 17);

            kgVentaE.setEditable(false);
            Pventas.add(kgVentaE);
            kgVentaE.setBounds(40, 15, 70, 17);

            cLabel12.setText("Importe");
            Pventas.add(cLabel12);
            cLabel12.setBounds(130, 15, 50, 17);

            impVentaE.setEditable(false);
            Pventas.add(impVentaE);
            impVentaE.setBounds(180, 15, 80, 17);

            cLabel13.setText("Precio medio");
            Pventas.add(cLabel13);
            cLabel13.setBounds(270, 15, 80, 17);

            pmVentaE.setEditable(false);
            Pventas.add(pmVentaE);
            pmVentaE.setBounds(350, 15, 50, 17);

            PtotalE.add(Pventas);
            Pventas.setBounds(5, 3, 420, 40);

            PEntra.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Entradas"));
            PEntra.setLayout(null);

            cLabel14.setText("Kgs");
            PEntra.add(cLabel14);
            cLabel14.setBounds(10, 15, 30, 17);

            kgCompraE.setEditable(false);
            PEntra.add(kgCompraE);
            kgCompraE.setBounds(40, 15, 70, 17);

            cLabel15.setText("Importe");
            PEntra.add(cLabel15);
            cLabel15.setBounds(130, 15, 50, 17);

            impCompra1.setEditable(false);
            PEntra.add(impCompra1);
            impCompra1.setBounds(180, 15, 80, 17);

            cLabel16.setText("Precio medio");
            PEntra.add(cLabel16);
            cLabel16.setBounds(270, 15, 80, 17);

            pmCompraE.setEditable(false);
            PEntra.add(pmCompraE);
            pmCompraE.setBounds(350, 15, 50, 17);

            PtotalE.add(PEntra);
            PEntra.setBounds(5, 40, 420, 40);

            PEntra1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Margenes"));
            PEntra1.setMaximumSize(new java.awt.Dimension(280, 40));
            PEntra1.setMinimumSize(new java.awt.Dimension(280, 40));
            PEntra1.setPreferredSize(new java.awt.Dimension(280, 40));
            PEntra1.setLayout(null);

            cLabel17.setText("Importe");
            PEntra1.add(cLabel17);
            cLabel17.setBounds(10, 15, 50, 17);

            impGanaE.setEditable(false);
            PEntra1.add(impGanaE);
            impGanaE.setBounds(80, 15, 70, 17);

            cLabel18.setText("€ por Kg");
            PEntra1.add(cLabel18);
            cLabel18.setBounds(170, 15, 50, 17);

            ganKilE.setEditable(false);
            PEntra1.add(ganKilE);
            ganKilE.setBounds(225, 15, 50, 17);

            pmCompraE1.setEditable(false);
            PEntra1.add(pmCompraE1);
            pmCompraE1.setBounds(350, 15, 50, 17);

            PtotalE.add(PEntra1);
            PEntra1.setBounds(430, 3, 290, 40);

            PEntra2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Despieces"));
            PEntra2.setMaximumSize(new java.awt.Dimension(280, 40));
            PEntra2.setMinimumSize(new java.awt.Dimension(280, 40));
            PEntra2.setPreferredSize(new java.awt.Dimension(280, 40));
            PEntra2.setLayout(null);

            cLabel19.setText("Kgs Entrada");
            PEntra2.add(cLabel19);
            cLabel19.setBounds(10, 15, 70, 17);

            kgDesEntE.setEditable(false);
            PEntra2.add(kgDesEntE);
            kgDesEntE.setBounds(80, 15, 70, 17);

            cLabel20.setText("Kg. Salida");
            PEntra2.add(cLabel20);
            cLabel20.setBounds(155, 15, 60, 17);

            kgDesSalE.setEditable(false);
            PEntra2.add(kgDesSalE);
            kgDesSalE.setBounds(210, 15, 70, 17);

            PtotalE.add(PEntra2);
            PEntra2.setBounds(430, 40, 290, 40);

            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 2;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
            gridBagConstraints.weightx = 1.0;
            Pprinc.add(PtotalE, gridBagConstraints);

            getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

            pack();
        }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Bacepta;
    private gnu.chu.controles.CButton Bfiltro;
    private gnu.chu.controles.CButton Breset;
    private javax.swing.JMenuItem MVerDoc;
    private javax.swing.JMenuItem MVerStock;
    private gnu.chu.controles.CPanel PEntra;
    private gnu.chu.controles.CPanel PEntra1;
    private gnu.chu.controles.CPanel PEntra2;
    private gnu.chu.controles.CPanel Pentra;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CPanel PtotalE;
    private gnu.chu.controles.CPanel Pventas;
    private gnu.chu.controles.CLinkBox alm_codiE;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel10;
    private gnu.chu.controles.CLabel cLabel11;
    private gnu.chu.controles.CLabel cLabel12;
    private gnu.chu.controles.CLabel cLabel13;
    private gnu.chu.controles.CLabel cLabel14;
    private gnu.chu.controles.CLabel cLabel15;
    private gnu.chu.controles.CLabel cLabel16;
    private gnu.chu.controles.CLabel cLabel17;
    private gnu.chu.controles.CLabel cLabel18;
    private gnu.chu.controles.CLabel cLabel19;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel20;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.controles.CLabel cLabel9;
    private gnu.chu.controles.CTextField fecfinE;
    private gnu.chu.controles.CTextField feciniE;
    private gnu.chu.controles.CComboBox feulinE;
    private gnu.chu.controles.CTextField ganKilE;
    private gnu.chu.controles.CTextField impCompra1;
    private gnu.chu.controles.CTextField impGanaE;
    private gnu.chu.controles.CTextField impVentaE;
    private gnu.chu.controles.Cgrid jt;
    private gnu.chu.controles.CTextField kgCompraE;
    private gnu.chu.controles.CTextField kgDesEntE;
    private gnu.chu.controles.CTextField kgDesSalE;
    private gnu.chu.controles.CTextField kgVentaE;
    private javax.swing.JCheckBoxMenuItem opCompras;
    private gnu.chu.controles.CCheckBox opCostFij;
    private javax.swing.JCheckBoxMenuItem opDespEnt;
    private javax.swing.JCheckBoxMenuItem opDespSal;
    private javax.swing.JCheckBoxMenuItem opInven;
    private javax.swing.JCheckBoxMenuItem opInventDep;
    private gnu.chu.controles.CCheckBox opMvtos;
    private javax.swing.JCheckBoxMenuItem opNinguno;
    private javax.swing.JCheckBoxMenuItem opRegul;
    private javax.swing.JCheckBoxMenuItem opSalidaDep;
    private gnu.chu.controles.CCheckBox opStkNeg;
    private javax.swing.JCheckBoxMenuItem opTodos;
    private javax.swing.JCheckBoxMenuItem opTrasp;
    private javax.swing.JCheckBoxMenuItem opVentas;
    private gnu.chu.controles.CTextField pmCompraE;
    private gnu.chu.controles.CTextField pmCompraE1;
    private gnu.chu.controles.CTextField pmVentaE;
    private gnu.chu.camposdb.proPanel pro_codiE;
    private gnu.chu.controles.CTextField pro_cosincE;
    private gnu.chu.controles.CTextField pro_ejercE;
    private gnu.chu.controles.CTextField pro_loteE;
    private gnu.chu.controles.CTextField pro_numindE;
    private gnu.chu.controles.CTextField pro_serieE;
    private gnu.chu.controles.CComboBox tipfecC;
    // End of variables declaration//GEN-END:variables
}
