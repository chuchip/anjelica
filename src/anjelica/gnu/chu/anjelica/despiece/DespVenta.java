package gnu.chu.anjelica.despiece;

/**
 *
 * <p>Título: despventas </p>
 * <p>Descripción: Ventana de Despiece  para Albaranes de Ventas</p>
 * <p> Comprueba la variable de entorno 
 *   autollenardesp para ver  si debe hacer autollenado de
 *   los productos para un tipo de despiece (por defecto, NO)
 * </p>
 * <p>Copyright: Copyright (c) 2005-2018
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
 * @version 1.0
 */

import gnu.chu.anjelica.almacen.StkPartid;
import gnu.chu.anjelica.almacen.ActualStkPart;
import gnu.chu.anjelica.almacen.pdalmace;
import gnu.chu.anjelica.listados.etiqueta;
import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.anjelica.sql.Desorilin;
import gnu.chu.anjelica.sql.DesorilinId;
import gnu.chu.anjelica.sql.Desporig;
import gnu.chu.anjelica.sql.DesporigId;
import gnu.chu.anjelica.sql.IndivStock;
import gnu.chu.comm.BotonBascula;
import gnu.chu.controles.CGridEditable;
import gnu.chu.controles.Cgrid;
import gnu.chu.controles.StatusBar;
import gnu.chu.eventos.GridAdapter;
import gnu.chu.eventos.GridEvent;
import gnu.chu.sql.DatosTabla;
import gnu.chu.sql.conexion;
import gnu.chu.utilidades.CodigoBarras;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.mensajes;
import gnu.chu.utilidades.miThread;
import gnu.chu.utilidades.ventana;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.*;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;


public class DespVenta extends ventana {
    Date deoFecpro;
    Date deoFecSacr;
    Date fecCadAnt;
    int etiquetaInterior;    
    etiqueta etiq;
    int tidCodiAnt=0;
    private boolean tidAsprfi=false;
    int primeraLinea;
    Desporig desorca;
    Desorilin desorli;
    final static String TABLA_BLOCK="desporig";
//    private int proCodi=0;
    private boolean AUTOLLENARDESP=false;
    public static final String SERIE="V";
    boolean swEdicion=false;
    public final static int JT_PROCOD=0,JT_PRONOMB=1,JT_KILOS=2,JT_FECCAD=3,JT_UNID=4,JT_NUMIND=5, JT_ORDEN=6,JTLIN_NUMCAJ=8;
    
    boolean nuevoDespiece=false;
    int proCodAnt;
    double defKilAnt; // Kilos anteriores
    int defUnidAnt; // Unidades anteriores
    ventana papa;
    BotonBascula botonBascula;
    int ejeNume,empCodi,deoCodi;
    utildesp utdesp;
    int almCodi,prvCodi,cliCodi;
    ActualStkPart stkPart;


    public DespVenta() {
        initComponents();
       
        statusBar=new StatusBar(this);
        this.add(statusBar,BorderLayout.SOUTH);
       
        setResizable(true);
        setMaximizable(false);
        setIconifiable(false);
        this.setSize(570,440);
        this.setTitle("Carga Despiece desde Ventas");
        setVersion("20180114");
    }
    /**
     * Establece el cliente para el que se genera el despiece
     * @param cliente 
     */
    public void setCliente(int cliente)
    {
        cliCodi=cliente;
    }
    /**
     * Establece el almacén donde trabajar.
     * @param almacen
     */
    public void setAlmacen(int almacen)
    {
        almCodi=almacen;
    }
    public boolean isNuevoDespiece()
    {
        return nuevoDespiece;
    }
    public void iniciar(ventana padre) throws Exception
    {
        this.EU=padre.EU;
        papa=padre;
        dtStat=padre.dtStat;
        dtCon1=padre.dtCon1;
        dtAdd=new DatosTabla(new conexion(EU));
        etiquetaInterior=etiqueta.getCodigoEtiqInterior(EU);
        utdesp =new utildesp();
        ejeNume=EU.ejercicio;
        empCodi=EU.em_cod;
        almCodi=pdalmace.getAlmacenPrincipal();
        botonBascula = new BotonBascula(EU,this);
        botonBascula.setPreferredSize(new Dimension(50,24));
        botonBascula.setMinimumSize(new Dimension(50,24));
        botonBascula.setMaximumSize(new Dimension(50,24));
        statusBar.add(botonBascula, new GridBagConstraints(9, 0, 1, 2, 0.0, 0.0
                                                   , GridBagConstraints.EAST,
                                                   GridBagConstraints.VERTICAL,
                                                   new Insets(0, 5, 0, 0), 0, 0));
        pro_codiE.iniciar(dtStat, this, getLayeredPane(), EU);
//        pro_codiE.setCamposLote(deo_ejelotE,deo_serlotE, pro_loteE,  pro_numindE, deo_kilosE);
//        pro_codiE.setAyudaLotes(true);
        pro_codiE.setEnabled(false);
        pro_codsalE.iniciar(dtStat, this, getLayeredPane(), EU);
        pro_codsalE.setEntrada(true);
        pro_kilsalE.setLeePesoBascula(botonBascula);
        stkPart=new ActualStkPart(dtAdd,EU.em_cod);
        tid_codiE.iniciar(dtStat, padre, padre.vl, EU);
        tid_codiE.setValorInt(MantTipDesp.AUTO_DESPIECE);
        tid_codiE.getComboBox().setFocusable(false);
        tid_codiE.setVerSoloActivo(true);
//        pro_codsalE.setProNomb(pro_nombE);
        ArrayList vc=new ArrayList();
        vc.add(pro_codsalE.getFieldProCodi());
        vc.add(pro_nombE);
        vc.add(pro_kilsalE);
        vc.add(def_feccadE);
        vc.add(pro_unidE);
        vc.add(pro_indsalE);
        vc.add(def_ordenE);
        jt.setCampos(vc);
        jt.setFormatoCampos();
        jt.removeAllDatos();
        jt.setButton(KeyEvent.VK_F2,BF2);
        jt.setButton(KeyEvent.VK_F5, BcopLin);
        Pcabe.setButton(KeyEvent.VK_F2,BF2);
        jt.setButton(KeyEvent.VK_F4,Baceptar);
        AUTOLLENARDESP= EU.getValorParam("autollenardesp", AUTOLLENARDESP);
        cargaPSC.setSelected(AUTOLLENARDESP);
        cargaPSC.setFocusable(false);
        activarEventos();
      //  pro_codsalE.setCamposLote(pro_ejlsalE,pro_sersalE, pro_lotsalE,  pro_indsalE, pro_kilsalE);
    }
    public void setProCodi(int proCodi) throws SQLException
    {        
        pro_codiE.setValorInt(proCodi);
        
        tid_codiE.clearArticulos();
        tid_codiE.addArticulo(proCodi);
        
        tid_codiE.releer();
    }
    public void setLote(int ejeNume,String serieLote,int lote, int indiv) throws SQLException
    {
        deo_ejelotE.setValorInt(ejeNume);
        deo_serlotE.setText(serieLote);
        pro_loteE.setValorInt(lote);
        pro_numindE.setValorInt(indiv);
        buscaPeso();
    }
    public void mostrar() throws SQLException
    {
          nuevoDespiece=false;
          
          deoCodi=0;
          Baceptar.setEnabled(true);
          tid_codiE.setEnabled(true);

          jt.setEnabled(false);
//          pro_codiE.resetTexto();
//          pro_loteE.resetTexto();
//          deo_ejelotE.resetTexto();
//          deo_kilosE.resetTexto();
//          deo_serlotE.resetTexto();
//          tid_codiE.resetTexto();
//          pro_numindE.resetTexto();
//          
       
          Ppie.resetTexto();
          jt.removeAllDatos();
//          deo_ejelotE.setValorInt(EU.ejercicio);
//          deo_serlotE.setText("A");
//          pro_codiE.resetCambio();
//          pro_codiE.setValorInt(proCodi);
          //pro_codiE.resetCambio();
          activar(true);
          setVisible(true);
          statusBar.setEnabled(true);
          toFront();
//          setEnabled(true);
          buscaPeso();
          if (tidCodiAnt!=0 && tidAsprfi)
          {
             if (tid_codiE.controla())
             {
                 irGrid();
                 return;
             }
          }
          new miThread("")
          {
              @Override
              public void run()
              {
                  try
                  {
                      sleep(500);
                  } catch (InterruptedException ex)
                  {}
                 tid_codiE.requestFocusLater();
              }
          };
          tid_codiE.requestFocusLater();

    }
    
    void pro_codiE_despuesLlenaCampos()
    {

    }
    void activarEventos()
    {
        BImprimir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                if (e.getActionCommand().startsWith("Inter"))
                     imprEtiq(jt.getSelectedRow(),true); 
                else
                {
                    if (jt.getValorInt(JT_NUMIND)>0)
                        imprEtiq(jt.getSelectedRow(),false);                      

                }
            }
        });
        BF2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                  irGrid();
            }
        });
        BcopLin.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jt.isEnabled() == false || jt.isVacio() || jt.getSelectedRow() < 1)
                    return;
                jt.setValor(jt.getValString(jt.getSelectedRow() - 1, 0), 0);
                pro_codsalE.setText(jt.getValString(jt.getSelectedRow() - 1, 0));
                jt.setValor(jt.getValString(jt.getSelectedRow() - 1, 1), 1);
                jt.requestFocusLater(jt.getSelectedRow(), 2);
            }
        });
        jt.addGridListener(new GridAdapter()
        {
            @Override
            public void cambioColumna(GridEvent event)   {
                if (event.getColumna()==0 && event.getLinea()==event.getLineaNueva())
                    try {
                        pro_nombE.setText(pro_codsalE.getNombArt());
                } catch (SQLException ex) {
                    Error("Error al buscar nombre de producto",ex);
                }
            }
        });
        Bir.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
              BF2.doClick();
            }}
        );

//        tid_codiE.addFocusListener(new FocusAdapter() {
//            @Override
//             public void focusGained(FocusEvent e) {
//                 try {
//                     buscaPeso();
//                     if (!pro_codiE.hasCambio())
//                         return;
//                     pro_codiE.resetCambio();
//                     tid_codiE.addArticulo(pro_codiE.getValorInt());
//                     tid_codiE.releer();
//                } catch (SQLException k)
//                {
//                    Error("Error en focus gained de tid_codi",k);
//                }
//             }
//        });
        Baceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                  guardaDespiece();
                 
            }
        });
    }
  
    @Override
    public boolean Error(String msg, Throwable k)
    {
        return papa.Error(msg, k);
    }
    /**
     * Rutina llamada al salir del despiece, dandole al boton aceptar.
     */
    void guardaDespiece()
    {      
        jt.salirGrid();
        if (cambiaLineajtLin(jt.getSelectedRow())>=0)
            return;
        actAcumulados();
        if (deoCodi==0)
        {
            msgBox("Introduzca alguna linea en despiece");
            return;
        }
        try {
           
            int nRow=jt.getRowCount();
            primeraLinea=-1;
            for (int n=0;n<nRow;n++)
            {
                if (jt.getValorInt(n,0)!=0 && jt.getValorDec(n,JT_KILOS)!=0)
                {
                    if (primeraLinea<0)
                        primeraLinea=n;
                }
            }
            if (primeraLinea<0)
            {
                mensajeErr("Introduzca alguna linea de despiece Valida");
                return;
            }
            if (difkilE.getValorDec()<0)
            {
                mensajeErr("No se pueden crear kilos de la nada. Corrija pesos");
                return;
            }
            String s;

            
            if (difkilE.getValorDec() > 0 )               
            { // Tengo Kg. de Diferencia 
                boolean swMer=tid_codiE.isAutoMer();
                if (swMer || tid_codiE.getValorInt()==MantTipDesp.AUTO_DESPIECE )
                {
                  if (swMer)
                  {
                    jt.resetCambio();
                    jt.setEnabled(false);
                    jt.requestFocusFinal();
                    ArrayList v=new ArrayList();
                    v.add(60); // Mer Venta
                    v.add(pro_codiE.getTextNomb());
                    v.add(difkilE.getValorDec()); // Kg
                    v.add(deo_feccadE.getDate());
                    v.add(1); // Unid                  
                    v.add("0"); // N Ind.
                    v.add("0"); // N. Orden                    
                    jt.addLinea(v);                  
                    jt.setEnabled(true);
                    jt.requestFocusFinal();
                    jt.ponValores(jt.getSelectedRow());
                    pro_kilsalE.setCambio(true);
                    cambiaLineajtLin(jt.getSelectedRow()); 
                  }
                  else
                  {
                      // Creo una linea automatica para la diferencia
                    jt.resetCambio();
                    jt.setEnabled(false);
                    jt.requestFocusFinal();
                    ArrayList v=new ArrayList();
                    v.add(pro_codiE.getValorInt());
                    v.add(pro_codiE.getTextNomb());
                    v.add(difkilE.getValorDec()); // Kg
                    v.add(deo_feccadE.getDate());
                    v.add(1); // Unid                  
                    v.add("0"); // N Ind.
                    v.add("0"); // N. Orden
                    jt.addLinea(v);                  
                    jt.setEnabled(true);
                    jt.requestFocusFinal();
                    jt.ponValores(jt.getSelectedRow());
                    pro_kilsalE.setCambio(true);
                    cambiaLineajtLin(jt.getSelectedRow());
                  }
                }
                else
                {
                    msgBox("Faltan kilos de salida");
                    return;
                }
            }
            else
            { // Compruebo integridad de despiece, ya q se marcara como cerrado.
                s=checkArtSalidaDesp(dtCon1,tid_codiE.getValorInt(),jt, pro_codiE.getValorInt(),1);
                if (s!=null)
                {
                    mensajeErr(s);
                    jt.requestFocusInicio();
                    return;
                }
            }

            actualCabDesp();
//            MantDesp.actFechaStock(dtAdd,def_fecproE.getDate(),def_fecsacE.getDate(),ejeNume,deoCodi,EU.em_cod); 
            resetBloqueo(dtAdd,TABLA_BLOCK, ejeNume+"|"+empCodi+
                         "|"+deoCodi,false);
            dtAdd.commit();
            tidCodiAnt=tid_codiE.getValorInt();
            tidAsprfi=tid_codiE.getAsignarProdSalida();
            nuevoDespiece=true;
        } catch (SQLException | ParseException k)
        {
           Error("Error al guardar despiece ",k);
        }
        matar();
    }
    public void resetTipoDespiece()
    {
       tidCodiAnt=0;
       tidAsprfi=false;  
       tid_codiE.resetTexto();
       tid_codiE.resetCambio();
    }
    public IndivStock getIndiviuoDespiece()
    {        
        IndivStock idSt= new IndivStock(almCodi,jt.getValorInt(primeraLinea,JT_PROCOD),
            deo_ejelotE.getValorInt(),pro_loteE.getValorInt(),deo_serlotE.getText(),
            jt.getValorInt(primeraLinea,JT_NUMIND),jt.getValorInt(primeraLinea,JT_UNID),
            jt.getValorDec(primeraLinea,JT_KILOS));
        idSt.setProNomb(jt.getValString(primeraLinea,JT_PRONOMB));
        return idSt;
    }
    /**
 * Llamada tanto en cambio de linea en el grid, como al guardar el registro (ej_addnew y ej_edit)
 * @param proCodi
 * @param ejeLot
 * @param serLot
 * @param numLot
 * @param numInd
 * @param kilos
 * @param dtAdd
 * @throws Exception 
 */
void guardaLinOrig(int proCodi,  int ejeLot, String serLot, int numLot,
                  int numInd, double kilos,double precio, DatosTabla dtAdd) throws SQLException
{

  desorli=new gnu.chu.anjelica.sql.Desorilin();
  desorli.setId(new DesorilinId(ejeNume,deoCodi,0));
  desorli.getId().setDelNumlin(desorli.getNextLinea(dtAdd));
  desorli.setDeoEjelot(ejeLot);
  desorli.setDeoKilos(kilos);
  desorli.setDeoPrcost(precio);
  //despOriLin.setDeoPrusu(null);
  desorli.setDeoSerlot(serLot.charAt(0));
  desorli.setProCodi(proCodi);
  desorli.setProLote(numLot);
  desorli.setProNumind(numInd);
  desorli.save(dtAdd);

}
 /**
 * Actualiza cabecera de despiece
 * @throws SQLException 
 */
 void actualCabDesp() throws SQLException, ParseException
 {  
      
   if (desorca==null)
   {
       desorca=new Desporig();
       desorca.setId(new DesporigId(ejeNume,deoCodi));
   }
   desorca.setTidCodi(tid_codiE.getValorInt());
   desorca.setDeoFecsac(def_fecsacE.getDate());
   desorca.setDeoFeccad(deo_feccadE.getDate());
   desorca.setDeoFecpro(def_fecproE.getDate());
   desorca.setPrvCodi(prvCodi);
   desorca.setDeoDesnue('N');
   desorca.setDeoEjloge(deo_ejelotE.getValorInt());
   desorca.setDeoSeloge(deo_serlotE.getText());
   desorca.setDeoNuloge(pro_loteE.getValorInt());
   desorca.setDeoLotnue((short) 0);
   desorca.setDeoCerra((short) -1);
   desorca.setDeoBlock( "N");
   desorca.update(dtAdd); 
   resetBloqueo(dtAdd,TABLA_BLOCK,
                   ejeNume+
                   "|" + deoCodi,false);
 }
 
    /**
     * 
     * @param dt DatosTabla 
     * @param tidCodi Tipo de despiece
     * @param jt Grid con los productos de salida
     * @param proCodi Producto Origen
     * @param unidEntrada Unidades de entrada
     * @return String con el mensaje de error. Null si no hay error
     * @throws SQLException 
     */
    static String checkArtSalidaDesp(DatosTabla dt,int tidCodi,Cgrid jt,int proCodi,int unidEntrada) throws SQLException
    {
       int nRow=jt.getRowCount();
       int nEle=0;
       if (tidCodi==MantTipDesp.AUTO_DESPIECE || tidCodi==MantTipDesp.LIBRE_DESPIECE 
               ||  tidCodi==MantTipDesp.CONGELADO_DESPIECE)
          return null;//checkAutoDespiece(dt, jt, proCodi, unidEntrada);

       HashMap<String,Integer> htGru  = new HashMap();
       String s="SELECT distinct(tds_grupo) as tds_grupo FROM tipdessal  WHERE tid_codi = "+tidCodi;
       if (dt.select(s))
       {
         do
         {
           htGru.put(dt.getString("tds_grupo",true),0);
         } while (dt.next());
       }
             
       for (int n=0;n<nRow;n++)
       {
         if (jt.getValorDec(n,JT_KILOS)==0  || jt.getValorInt(n,0)==0)
             continue;
         s="SELECT pro_tiplot FROM v_articulo  WHERE  pro_codi = "+jt.getValorInt(n,0);
         if (! dt.select(s))
             return  "Producto: " + jt.getValorInt(n, 0) +
                      " NO Encontrado en Maestro de Articulos";
         if (! dt.getString("pro_tiplot").equals("V"))
             continue; // Es un articulo no vendible. No lo tratamos.
         if (!checkArticuloTipoDesp(tidCodi,proCodi,jt.getValorInt(n,0),dt))
              return "Producto: " + jt.getValorInt(n, 0) +
                      " NO Encontrado en este Tipo de Despiece";
         
         if (htGru.get(dt.getString("tds_grupo"))==null)
           htGru.put(dt.getString("tds_grupo"),jt.getValorInt(n,JT_UNID)*jt.getValorInt(n,JTLIN_NUMCAJ));
         else
         {
           if (jt.getColumnCount()==6)
            nEle= htGru.get(dt.getString("tds_grupo")) + jt.getValorInt(n,JT_UNID);
           else    
            nEle= htGru.get(dt.getString("tds_grupo")) + (jt.getValorInt(n,JT_UNID) * jt.getValorInt(n,JTLIN_NUMCAJ));
           htGru.put(dt.getString("tds_grupo"),nEle);
         }
       }
       Iterator<String> en=htGru.keySet().iterator();
       String grupo;
       int tdsUnid;
       while (en.hasNext())
       {
         grupo =  en.next();
         s="SELECT tds_unid,pro_codi FROM tipdessal  WHERE tid_codi = "+tidCodi+
             " and tds_grupo = '" + grupo+"'";
         dt.select(s);
         nEle=htGru.get(grupo);
         tdsUnid=dt.getInt("tds_unid")*unidEntrada;
         if (nEle != tdsUnid && tdsUnid>0 )
         {
           return "Grupo: " + grupo +" ( Prod: "+dt.getInt("pro_codi")+" )"+
                      " con "+nEle+" Elementos. Deberia Tener "+tdsUnid;
         }
       }
       return null;
    }
    public  static  boolean checkArticuloTipoDesp(int tidCodi,int proCodOrig,int proCodi, DatosTabla dt) throws SQLException
    {
         if (tidCodi==MantTipDesp.LIBRE_DESPIECE)
             return true; 
         String s="SELECT pro_tiplot FROM v_articulo  WHERE  pro_codi = "+proCodi;
         if (! dt.select(s))
             return  false;
         if (! dt.getString("pro_tiplot").equals("V"))
             return true; // Es un articulo no vendible. No lo tratamos.
        if (tidCodi==MantTipDesp.AUTO_DESPIECE )
        {
            if (!MantTipDesp.esEquivalente(proCodOrig,proCodi,dt))
                return false;
            return true;
        }
        s="SELECT tds_grupo FROM tipdessal  WHERE tid_codi = "+tidCodi+
             " and pro_codi = "+proCodi;
         if (! dt.select(s))
         {
             // Quizas es un producto equivalente.
           s="SELECT tds_grupo FROM tipdessal  WHERE tid_codi = "+tidCodi+
              " and (pro_codi in (select pro_codini from artiequiv where pro_codfin = "+
               proCodi+") "+
               " or pro_codi in (select pro_codfin from artiequiv where pro_codini = "+
               proCodi+"))";
           if (!dt.select(s))
              return false;
         }
         return true;
    }
    @Override
    public void matar()
    {
        if (! nuevoDespiece)
        {
            if (deoCodi!=0)
            {
                int ret=mensajes.mensajeYesNo("Despiece sin guardar. Continuar en él ?");
                if (ret!=mensajes.NO)
                    return;                
                cancelarDespiece();
            }
        }
        despuesMatar();
    }

    void cancelaCabecera() throws SQLException
    {
         String s = "select * from v_despori WHERE eje_nume = " + ejeNume +
             " and deo_codi = " + deoCodi;
           if (!dtAdd.select(s))
               throw new SQLException("No encontrada cabecera despiece"+deoCodi);
           
           dtAdd.executeUpdate("delete from desorilin WHERE eje_nume = " + ejeNume +
             " and deo_codi = " + deoCodi);
           dtAdd.executeUpdate("delete from desporig WHERE eje_nume = " + ejeNume +
             " and deo_codi = " + deoCodi);
         
           deoCodi=0;
    }
    void cancelarDespiece()
    {
        try
        {
        
          String s = "DELETE FROM v_despfin " +
           " WHERE eje_nume = " + + ejeNume +
           " and emp_codi = " +empCodi +
           " and deo_codi = " + deoCodi ;
          dtAdd.executeUpdate(s);
          cancelaCabecera();       
          dtAdd.commit();
           resetBloqueo(dtAdd,TABLA_BLOCK,
                   ejeNume+
                   "|" + deoCodi,true);
        } catch (Exception k)
        {
            Error("Error al cancelar despiece",k);
        }

    }
    /**
     * Para que puedan machacarme la funcion y hacer algo despues de matar
     */
    public void despuesMatar()
    {

    }
    
   
    /**
     * Busca el peso disponible en stock de un individiuo
     * @return
     * @throws Exception
     */
  boolean buscaPeso() throws SQLException
  {
    if (! Pcabe.hasCambio())
        return true;
    swEdicion=false;
    StkPartid canStk =utildesp.buscaPeso(dtCon1,deo_ejelotE.getValorInt(),empCodi,
                       deo_serlotE.getText(),pro_loteE.getValorInt(),
                        pro_numindE.getValorInt(),pro_codiE.getValorInt(),almCodi);
   
    if (! canStk.hasError())
    {
      deo_kilosE.setValorDec(canStk.getKilos());
      return true;
    }
    if (canStk.isLockIndiv() )
    {
      mensajeErr(canStk.getMensaje());
      deo_kilosE.setValorDec(canStk.getKilos());
      swEdicion=true;
      return false;
    }
    mensajeErr(canStk.getMensaje());
    return false;
  }
    /**
     * Comprueba que la cabecera es correcta.
     * @return
     */
    boolean checkCabecera() throws Exception
    {
//       if (!pro_codiE.controla(true))
//       {
//           mensajeErr(pro_codiE.getMsgError());
//           return false;
//       }
//       if (! buscaPeso())
//           return false;
//       if (! tid_codiE.controla())
//       {
//           mensajeErr("Tipo Despiece NO valido ");
//           return false;
//       }
       if (tid_codiE.getValorInt()==0)
       {
           mensajeErr("Introduca un tipo de despiece");
           tid_codiE.requestFocus();
           return false;
       }
       if (!tid_codiE.controla(true))           
       {
           mensajeErr(tid_codiE.getMsgError());
           return false;
       }
       if ((tid_codiE.getValorInt()!=MantTipDesp.AUTO_DESPIECE  
           && tid_codiE.getValorInt()!=MantTipDesp.CONGELADO_DESPIECE)
               && ! MantTipDesp.checkArticuloEntrada(dtStat, pro_codiE.getValorInt(), tid_codiE.getValorInt()))
       {
           mensajeErr("Producto NO valido para este tipo de despiece");
           pro_codiE.requestFocus();
           return false;
       }
       return true;
    }

    void irGrid()
    {
        try {
//            if (!jt.isEnabled())
//            {
               if (!checkCabecera())
                return;
               genDatEtiq();
               if (!tid_codiE.isEnabled())
               { // Vuelta a entrar
                   if (deoCodi!=0 && Pcabe.hasCambio())
                   { // Han cambiado el producto entrada. A actualizar
                      cancelaCabecera();
                      guardaCabDesp();
                      dtAdd.commit();
                   }
                   Pcabe.resetCambio();
               }
               else
               { // Primera vez que se entra
                    llenaGrid();
               }
               tid_codiE.setEnabled(false);
                if (tid_codiE.getAsignarProdSalida() )
               {
                   int proCodi=MantTipDesp.getArticuloSalida(dtStat, pro_codiE.getValorInt(), tid_codiE.getValorInt());
                   if (proCodi>0)
                   {
                       jt.setValor(proCodi,0,JT_PROCOD);
                       jt.setValor(pro_codsalE.getNombArt(proCodi),0,JT_PRONOMB);
                   }
               }
//               activar(false);
               jt.setEnabled(true);
              
               jt.requestFocusInicioLater();
//            }
//            else
//            { // Ir a cabecera
//                activar(true);
//                tid_codiE.setEnabled(false);
//                jt.setEnabled(false);
//                pro_codiE.requestFocus();
//            }
        } catch (Exception k)
        {
            Error("Error en irGrid",k);
        }
    }
    /**
     * Devuelve la sentencia SQL a ejecutar para mostrar los productos disponibles
     * como de salida para un tipo de despiece y un producto de origen dados.
     * @param tidCodi Tipo de Despiece
     * @param pro_codi
     * @return  string con la sentencia SQL
     * @parm pro_codi Producto de Origen.
     */
    public static String getSqlProdSal(int tidCodi,int pro_codi) 
    {
        String s;
         switch (tidCodi)
        {
            case MantTipDesp.CONGELADO_DESPIECE:
              s="SELECT p.pro_codi,p.pro_nomb FROM artequcon as d,v_articulo as p "+
                " where p.pro_codi = d.pro_codfin and "+
                " d.pro_codini = "+pro_codi;
              break;
            case MantTipDesp.AUTO_DESPIECE:
               s= "SELECT p.pro_codi,p.pro_nomb  FROM artiequiv as d,v_articulo as p "+
                  " where pro_codini = "+pro_codi+
                  " AND p.pro_codi = d.pro_codfin "+
                  " UNION "+
                  "SELECT p.pro_codi,p.pro_nomb  FROM artiequiv as d,v_articulo as p "+
                  " where  pro_codfin = "+pro_codi+
                  " AND p.pro_codi = d.pro_codini ";
               break;
            default:
              s="select d.pro_codi,p.pro_nomb from tipdessal d,v_articulo p "+
                " WHERE d.tid_codi = "+tidCodi+
                " and d.pro_codi = p.pro_codi "+
                " and p.pro_activ < 0 "+ // Solo productos activos.
                " order by d.pro_codi";             
        }    
        return s;
    }
    void llenaGrid() throws SQLException,ParseException
    {

        if (tid_codiE.getValorInt()==MantTipDesp.AUTO_DESPIECE)
        {
            ArrayList v=new ArrayList();
            v.add(pro_codiE.getValorInt());
            v.add(pro_codiE.getTextNomb());
            v.add("0"); // Kg
             v.add(deo_feccadE.getDate());
            v.add("1"); // Unid
            v.add("0"); // N Ind.
            v.add("0"); // N. Orden           
            jt.addLinea(v);
            return;
        }
        if (tid_codiE.getValorInt()==MantTipDesp.LIBRE_DESPIECE ||  tid_codiE.getAsignarProdSalida() || 
                ! cargaPSC.isSelected())
          return;
       
        String s= getSqlProdSal(tid_codiE.getValorInt(), pro_codiE.getValorInt());
             
        if (!dtCon1.select(s))
        {
            mensajeErr("No encontrados articulos para este tipo de despiece");
            return;
        }
        
        do
        {
            ArrayList v=new ArrayList();
            v.add(dtCon1.getString("pro_codi"));
            v.add(dtCon1.getString("pro_nomb"));
            v.add("0"); // Kg
            v.add(deo_feccadE.getDate());
            v.add("1"); // Unid
            v.add("0"); // N Ind.
            v.add("0"); // N. Orden            
            jt.addLinea(v);
        } while (dtCon1.next());   
        
    }
    void activar(boolean b)
    {
//        statusBar.setEnabled(!b);
//        if (deoCodi!=0)
//            Baceptar.setEnabled(!b);
//        pro_codiE.setEnabled(b);
//        deo_ejelotE.setEnabled(b);
//        deo_serlotE.setEnabled(b);
//        tid_codiE.setEnabled(b);
//        pro_numindE.setEnabled(b);
//        pro_loteE.setEnabled(b);
    }
  
    /**
     * Función llamada cuando se cambia de linea en el grid
     * @param linea numero de linea
     * @return >=0 campo a volver con error.
     */
   int cambiaLineajtLin(int linea)
   {
       mensajeErr("", false);
       try
       {
         String proNomb;
         proNomb=pro_codsalE.getNombArt();
         jt.setValor(proNomb,linea,JT_PRONOMB);
         if (!pro_codsalE.hasCambio() && !pro_kilsalE.hasCambio() && !def_feccadE.hasCambio())
             return -1; // No hubo cambios en la linea.
      
         if (pro_kilsalE.getValorDec()==0 ||  pro_codsalE.isNull())
         {
           if (jt.getValorInt(linea,JT_NUMIND)==0)
            return -1; // Si NO tengo Kilos o no me han metido el codigo de prod. paso de todo
           borraLinDesp(jt.getValorInt(linea,JT_ORDEN));
           return -1;
         }

         if (!pro_codsalE.controla(false,false))
         {
           mensajeErr("Codigo de Producto NO valido");
           return 0;
         }
         if (! pro_codsalE.isActivo())
         {
             mensajeErr("Producto NO esta ACTIVO");
             return 0;
         }
         if (pro_unidE.getValorInt()==0)
         {
             mensajeErr("Introduzca UNIDADES");
             return JT_UNID;
         }
         if (pro_codsalE.isVendible())
         {
             if (tid_codiE.getValorInt()==MantTipDesp.AUTO_DESPIECE && ! MantTipDesp.esEquivalente(pro_codsalE.getValorInt(),pro_codiE.getValorInt(),dtStat))
             {
                     mensajeErr("Para tipo despiece 9999 solo permitidos el mismo producto o NO vendibles");
                     return 0;
             }

             if (tid_codiE.getValorInt()!=MantTipDesp.AUTO_DESPIECE  && ! MantTipDesp.checkArticuloSalida(dtAdd, pro_codsalE.getValorInt(),tid_codiE.getValorInt()))
             {
                 mensajeErr("Articulo no valido para este tipo despiece");
                 return 0;
             }
         }
         
         if (def_feccadE.isNull())
         {
             msgBox("Introduzca fecha Caducidad");
             def_feccadE.setText(utdesp.feccadE);
             return JT_FECCAD;
         }
         if (pro_codsalE.getDiasCaducidad() > 0)
         {
               if (Formatear.comparaFechas(def_feccadE.getDate(), Formatear.getDateAct()) <= 0)
               {
                   msgBox("Fecha Caducidad  debe ser superior a la actual");
                   def_feccadE.setText(utdesp.feccadE);
                   return JT_FECCAD;
               }
               if (Formatear.comparaFechas(def_feccadE.getDate(), Formatear.getDateAct()) <= 10)
               {
                   int ret = mensajes.mensajeYesNo("Fecha Caducidad  deberia ser superior en diez dias a la actual. Continuar?");
                   if (ret != mensajes.YES)
                       return JT_FECCAD;
               }              
         }
         if (checkExcluyentes("SELECT * FROM artiexcl where pro_codini ="+pro_codsalE.getValorInt()+
                 " and pro_codfin in (",linea))
             return 0;
         if (checkExcluyentes("SELECT * FROM artiexcl where pro_codfin ="+pro_codsalE.getValorInt()+
                 " and pro_codini in (",linea))
             return 0;
          pro_codsalE.resetCambio();
          pro_kilsalE.resetCambio();
          def_feccadE.resetCambio();
          mensajeErr("");
          guardaLinea(linea);
          mensajeErr("Linea " + linea + "... Guardada");

          if ( pro_codsalE.isVendible() && pro_codsalE.getEtiCodi()>=0 && jt.isEnabled()) // && pro_codlE.getConStkInd())
             imprEtiq(linea,false);
       }
       catch (SQLException | ParseException ex)
       {
         Error("Error al Cambiar Linea de Grid", ex);
       }
       return -1;
    }
    public String getCodBarras()
    {
        return new CodigoBarras("R",""+ejeNume,SERIE,deoCodi,jt.getValorInt(0),
                jt.getValorInt(JT_NUMIND),jt.getValorDec(JT_KILOS)).getCodBarra() ;                
    }
   /**
    * Comprueba si un articulo es excluyente contra los del grid.
    * @param s sentencia SQL inicial
    * @param linea Linea actual del producto a comparar
    * @return true en caso de q tenga excluyentes
    * @throws SQLException
    */
    boolean checkExcluyentes(String s, int linea) throws SQLException
    {
         int nRow=jt.getRowCount();
         boolean swInc=false;
         for (int n=0;n<nRow;n++)
         {
             if (n==linea)
                 continue; // Me excluyo a mi mismo.
             if (jt.getValorInt(n,0)==0 || jt.getValorDec(n,JT_KILOS)==0)
                 continue;  // Salto lineas vacias
             s+=jt.getValorInt(n,0)+",";
             swInc=true;
         }
         if (swInc)
         {
             s=s.substring(0,s.length()-1)+")";
             if (dtCon1.select(s))
             {
                 mensajeErr("Este articulo choca con otro articulo excluyente");
                 return true;
             }
         }
         return false;
    }
    void actAcumulados()
    {
       int nRow=jt.getRowCount();
       double totKilos=0;
       int totUnid=0;
       for (int n=0;n<nRow;n++)
       {
           if (jt.getValorInt(n,0)==0 || jt.getValorDec(n,JT_KILOS)==0)
               continue;
           totKilos+=jt.getValorDec(n,JT_KILOS);
           totUnid+=jt.getValorInt(n,JT_UNID);
       }
       kilsalE.setValorDec(totKilos);
       unisalE.setValorDec(totUnid);
       difkilE.setValorDec(deo_kilosE.getValorDec()-totKilos);
    }
    
    boolean  genDatEtiq() throws Exception
    {
       int prvDespiece=pdconfig.getPrvDespiece(empCodi,dtStat);
       if (!utdesp.busDatInd(deo_serlotE.getText(), pro_codiE.getValorInt(),
                             empCodi,
                             deo_ejelotE.getValorInt(),
                             pro_loteE.getValorInt(),
                             pro_numindE.getValorInt(), // N. Ind.
                             almCodi,
                             dtCon1, dtStat, EU))
       {
         mensajeErr(utdesp.getMsgAviso());
         prvCodi=prvDespiece;
         return true;
       }
       prvCodi=utdesp.getPrvCodi();
//       if (tid_codiE.getValorInt()!=MantTipDesp.AUTO_DESPIECE)
//         utdesp.setDespNuestro(Formatear.getFechaAct("dd-MM-yyyy"), dtStat);
       deo_feccadE.setText(utdesp.feccadE);
       def_feccadE.setText(utdesp.feccadE);
       def_fecproE.setDate(utdesp.getFechaProduccion());
       def_fecsacE.setDate(utdesp.fecSacrE);
       return true;
 }
 /**
  * Guarda Linea de despiece de salida
  * @param linea
  */
 void guardaLinea(int linea)
 {
   try
   {
     int nInd = jt.getValorInt(linea,JT_NUMIND);
     int defOrden=jt.getValorInt(linea,JT_ORDEN);
 
     if (deoCodi== 0)
     {
       guardaCabDesp();
       setBloqueo(dtAdd,TABLA_BLOCK, ejeNume+"|"+empCodi+
                         "|"+deoCodi,false);
     }

     if (nInd==0)
     { // Existe linea. Anulo stock Anterior.
        nInd = utildesp.getMaxNumInd( dtAdd,pro_codsalE.getValorInt(), deo_ejelotE.getValorInt(), empCodi,
               deo_serlotE.getText(),  pro_loteE.getValorInt());
     }
     defOrden=guardaLinDesp(deo_ejelotE.getValorInt(), empCodi,
                deo_serlotE.getText(), pro_loteE.getValorInt(),nInd,
                pro_codsalE.getValorInt(),
                pro_kilsalE.getValorDec(), pro_unidE.getValorInt(),1,
                deo_feccadE.getText(),defOrden);         
     if (Formatear.comparaFechas(def_feccadE.getDate(),deo_feccadE.getDate())!=0)
     {
         ponFechas(pro_codsalE.getDiasCaducidad(),def_feccadE.getDate());
         String s="update stockpart set stp_fecpro='"+Formatear.getFechaDB(deoFecpro)+"'"+
               (deoFecSacr==null ?"":", stp_fecsac = '"+Formatear.getFechaDB(deoFecSacr)+"'")+
               " where pro_nupar="+ pro_loteE.getValorInt()+
                " and pro_serie='"+deo_serlotE.getText()+"' "+
                " and eje_nume="+deo_ejelotE.getValorInt()+
                " and pro_codi = "+pro_codsalE.getValorInt()+
                " and pro_numind="+nInd;
         dtAdd.executeUpdate(s);
     }
     jt.setValor(""+nInd,linea,JT_NUMIND);
     jt.setValor(""+defOrden,linea,JT_ORDEN);
     
     dtAdd.commit();
     
   }
   catch (Exception ex)
   {
     Error("Error al Guardar Datos Despiece", ex);
   }
 }
 int guardaLinDesp(int ejeLot,int empLot,String serLot,int numLot,int nInd,
                    int proCodi,double kilos,int numPiezas,int uniCaj,String feccad,int defOrden) throws Exception
 {
   utdesp.iniciar(dtAdd,ejeNume,empCodi,
                  almCodi,almCodi,EU);
   utdesp.setGrupoDesp(0);
   utdesp.setTipoProduccion(false);
   return utdesp.guardaLinDesp(ejeLot,empLot,serLot,numLot,nInd,deoCodi,proCodi,
                                 kilos,numPiezas,feccad,defOrden,uniCaj,
                                 0,
                                 -1);
 }
 


   void  guardaCabDesp() throws SQLException, ParseException
   {
       if (deoCodi==0)
      {
        guardaCabOrig();
        deoCodi=desorca.getId().getDeoCodi();
      }
      guardaLinOrig(pro_codiE.getValorInt(),
                              deo_ejelotE.getValorInt(), deo_serlotE.getText(),
                              pro_loteE.getValorInt(),
                              pro_numindE.getValorInt(), deo_kilosE.getValorDec(),0,
                              dtAdd);
      Baceptar.setEnabled(true);

    }
   void guardaCabOrig() throws SQLException, ParseException
   {
     desorca=new Desporig();
     deoCodi=utildesp.incNumDesp(dtAdd,EU.em_cod,ejeNume);
     desorca.setId(new DesporigId(ejeNume,deoCodi));
     desorca.setCliente(cliCodi);
     desorca.setDeoAlmdes(almCodi);
     desorca.setDeoAlmori(almCodi);
     desorca.setDeoDesnue(tid_codiE.getValorInt()==9999?'S':'N');
     desorca.setDeoFecha(Formatear.getDateAct());
     desorca.setDeoIncval("N");
     desorca.save(dtAdd, ejeNume,EU);
     setBloqueo(dtAdd,TABLA_BLOCK,
                   ejeNume+
                   "|" + deoCodi,false);
    }
 

 /**
  * Imprime la etiqueta
  * @param linea Linea de despiece
  * Tipo etiqueta:
  */
 void imprEtiq(int linea,boolean etiqInterna)
 {
   int proCodeti;
   try {


     String nombArt=pro_codsalE.getNombArt(pro_codsalE.getText());
     if (etiqInterna)
         proCodeti=etiquetaInterior;
     else
     {
        if (pro_codsalE.getLikeProd().isNull("pro_codeti"))
          proCodeti = 0;
        else
          proCodeti = pro_codsalE.getLikeProd().getInt("pro_codeti");
     }
     ponFechas(pro_codsalE.getDiasCaducidad(),def_feccadE.getDate());
//     debug("Nombre Articulo: "+nombArt);
     utdesp.iniciar(dtAdd,ejeNume,empCodi,almCodi,
                    almCodi,EU);
     utdesp.setLogotipo(null);
     utdesp.setDirEmpresa(null);
//      etiq.iniciar(tipetiqE.getValorInt()!=etiqueta.ETIQINT?codBarras.getCodBarra():codBarras.getLote(false),
//            codBarras.getLote(tipetiqE.getValorInt()!=etiqueta.ETIQINT),
//            pro_codiE.getText(),pro_codiE.getTextNomb(),utDesp.getPaisNacimiento(),utDesp.getPaisEngorde(),
//            utDesp.getSalaDespiece(),
//            utDesp.getNumCrot(),deo_kilosE.getValorDec(),
//            utDesp.getConservar(), utDesp.getMatadero(),
//            utDesp.getFechaProduccion(),utDesp.getFechaProduccion(), utDesp.getFecCaduc(),
//            utDesp.getFecSacrif());
     utdesp.imprEtiq(proCodeti,dtCon1,jt.getValorInt(linea,0), nombArt,
                    "D",
                    pro_loteE.getValorInt(),
                    deo_ejelotE.getText(), deo_serlotE.getText(),
                    jt.getValorInt(linea,JT_NUMIND),
                    pro_kilsalE.getValorDec(),
                    Formatear.getDateAct(),
                    deoFecpro,
                    def_feccadE.getDate(),
                    deoFecSacr,
                    def_feccadE.getDate(),0);
      mensajeErr("Etiqueta ... Listada");
   }
   catch (Throwable ex)
   {
     Error("Error al Guardar Datos Despiece", ex);
   }
 }
  /**
     * Establece las fechas de produccion y sacrificio, segun la fecha caducidad y dias 
     * @param diasCad
     * @param fecCaduc
     * @throws ParseException 
     */
    void ponFechas(int diasCad,Date fecCaduc) throws ParseException
    {
        if (Formatear.comparaFechas(fecCaduc, utdesp.getFechaCaducidad())==0)
        {
            deoFecpro=utdesp.getFechaProduccion();
            deoFecSacr=utdesp.getFecSacrif();
        }
        else
        {
            deoFecpro= Formatear.sumaDiasDate(fecCaduc,diasCad*-1);
            if (def_fecsacE.isNull())
                deoFecSacr=null;
            else
                deoFecSacr=Formatear.sumaDiasDate(fecCaduc,(diasCad+2)*-1);
        }
   }
// /**
//  *
//  * @param dt DatosTabla conexion para realiar la select.
//  * @throws Exception Error BD
//  * @return int Proximo Numero De despiece
//  */
//  int incNumDesp(DatosTabla dt) throws Exception
//  {
//    int numDesp=utdesp.incNumDesp(dt,empCodi,ejeNume);
//    return numDesp;
//  }
/**
 * Borra Linea despiece de salida
 * @param defOrden
 * @return true si todo va bien.
 * @throws Exception
 */
 boolean borraLinDesp(int defOrden)
 {
   try {
       if (defOrden==0)
           return true;
        
       String s = "DELETE FROM v_despfin " +
           " WHERE eje_nume = " + + ejeNume +
           " and emp_codi = " +empCodi +
           " and deo_codi = " + deoCodi +
           " and def_orden = " + defOrden;

       boolean res= dtAdd.executeUpdate(s)==1;
       dtAdd.commit();
       return res;
   } catch (SQLException k)
   {
         Error("Error al borrar linea despiece",k);
         return false;
   }
 }
 public CGridEditable getGrid()
 {
    return jt;
 }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pro_codsalE = new gnu.chu.camposdb.proPanel();
        pro_nombE = new gnu.chu.controles.CTextField(Types.CHAR,"X",40);
        pro_indsalE = new gnu.chu.controles.CTextField(Types.DECIMAL,"####9");
        pro_kilsalE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9.99");
        def_ordenE = new gnu.chu.controles.CTextField(Types.DECIMAL,"####9");
        pro_unidE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9");
        pro_nombE1 = new gnu.chu.controles.CTextField(Types.CHAR,"X",40);
        def_feccadE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        Pprinc = new gnu.chu.controles.CPanel();
        Pcabe = new gnu.chu.controles.CPanel();
        pro_codiE = new gnu.chu.camposdb.proPanel(){
            protected void despuesLlenaCampos()
            {
                pro_codiE_despuesLlenaCampos();
            }
        }
        ;
        cLabel1 = new gnu.chu.controles.CLabel();
        cLabel2 = new gnu.chu.controles.CLabel();
        deo_serlotE = new gnu.chu.controles.CTextField(Types.CHAR,"X",1);
        pro_loteE = new gnu.chu.controles.CTextField(Types.DECIMAL,"####9");
        cLabel3 = new gnu.chu.controles.CLabel();
        pro_numindE = new gnu.chu.controles.CTextField(Types.DECIMAL,"####9");
        cLabel4 = new gnu.chu.controles.CLabel();
        deo_kilosE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9.99");
        cLabel5 = new gnu.chu.controles.CLabel();
        deo_ejelotE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        Bir = new gnu.chu.controles.CButton();
        tid_codiE = new gnu.chu.camposdb.tidCodi2();
        cargaPSC = new gnu.chu.controles.CCheckBox();
        jt = new gnu.chu.controles.CGridEditable(7){
            @Override
            public void afterCambiaLinea()
            {
                proCodAnt=pro_codsalE.getValorInt();
                defKilAnt=pro_kilsalE.getValorDec();
                defUnidAnt=pro_unidE.getValorInt();
                try {
                    fecCadAnt=deo_feccadE.getDate();
                }   catch (ParseException k){}
                pro_codsalE.resetCambio();
                pro_kilsalE.resetCambio();
                pro_unidE.resetCambio();
                deo_feccadE.resetCambio();
                actAcumulados();
            }
            @Override
            public int cambiaLinea(int row, int col)
            {
                //      System.out.println("cambiaLinea: "+pro_codlE.getText()+
                    //                         " - "+def_kilosE.getValorDec());
                return cambiaLineajtLin(row);
            }
            @Override
            public boolean deleteLinea(int row, int col)
            {
                jt.setValor(""+proCodAnt,JT_PROCOD); // Rest. El valor Antiguo
                jt.setValor(""+defKilAnt,JT_KILOS); // Rest. Kilos ANTIGUOS
                jt.setValor(""+defUnidAnt,JT_UNID);
                return borraLinDesp(jt.getValorInt(row,JT_ORDEN));
            }
        }
        ;
        ArrayList v=new ArrayList();
        v.add("Producto"); //0
        v.add("Nombre"); // 1
        v.add("Kilos"); // 2
        v.add("Fec.Cad."); //3
        v.add("Unid"); // 4
        v.add("Ind."); // 5
        v.add("Ord"); // 6
        jt.setCabecera(v);
        jt.setAnchoColumna(new int[]{80,180,60,60,40,50,30});
        jt.setAlinearColumna(new int[]{2,0,2,1,2,2,2});
        Ppie = new gnu.chu.controles.CPanel();
        cLabel6 = new gnu.chu.controles.CLabel();
        kilsalE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9.99");
        Baceptar = new gnu.chu.controles.CButton(Iconos.getImageIcon("check"));
        cLabel7 = new gnu.chu.controles.CLabel();
        deo_feccadE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        BF2 = new gnu.chu.controles.CButton();
        cLabel8 = new gnu.chu.controles.CLabel();
        difkilE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---9.99");
        cLabel9 = new gnu.chu.controles.CLabel();
        unisalE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9.99");
        cLabel10 = new gnu.chu.controles.CLabel();
        def_fecproE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel11 = new gnu.chu.controles.CLabel();
        def_fecsacE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        BcopLin = new gnu.chu.controles.CButton("F5",Iconos.getImageIcon("fill"));
        BImprimir = new gnu.chu.controles.CButtonMenu(Iconos.getImageIcon("print"));

        pro_nombE.setEnabled(false);

        pro_indsalE.setEnabled(false);

        def_ordenE.setText("0");
        def_ordenE.setEnabled(false);

        pro_unidE.setText("1");

        pro_nombE1.setEnabled(false);

        Pprinc.setLayout(new java.awt.GridBagLayout());

        Pcabe.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Pcabe.setMaximumSize(new java.awt.Dimension(448, 62));
        Pcabe.setMinimumSize(new java.awt.Dimension(448, 62));
        Pcabe.setPreferredSize(new java.awt.Dimension(448, 62));
        Pcabe.setLayout(null);

        pro_codiE.setToolTipText("F3 Cons. Lotes Disponibles");
        Pcabe.add(pro_codiE);
        pro_codiE.setBounds(60, 1, 374, 17);

        cLabel1.setText("Articulo");
        Pcabe.add(cLabel1);
        cLabel1.setBounds(10, 1, 43, 15);

        cLabel2.setText("Lote");
        Pcabe.add(cLabel2);
        cLabel2.setBounds(10, 20, 40, 17);

        deo_serlotE.setText("A");
        deo_serlotE.setEnabled(false);
        deo_serlotE.setMayusc(true);
        Pcabe.add(deo_serlotE);
        deo_serlotE.setBounds(90, 20, 18, 17);

        pro_loteE.setEnabled(false);
        Pcabe.add(pro_loteE);
        pro_loteE.setBounds(110, 20, 41, 17);

        cLabel3.setText("Individuo");
        Pcabe.add(cLabel3);
        cLabel3.setBounds(160, 20, 53, 17);

        pro_numindE.setEnabled(false);
        Pcabe.add(pro_numindE);
        pro_numindE.setBounds(210, 20, 42, 17);

        cLabel4.setText("Kilos");
        Pcabe.add(cLabel4);
        cLabel4.setBounds(260, 20, 36, 17);

        deo_kilosE.setEnabled(false);
        Pcabe.add(deo_kilosE);
        deo_kilosE.setBounds(300, 20, 50, 17);

        cLabel5.setText("Tipo Despiece");
        Pcabe.add(cLabel5);
        cLabel5.setBounds(10, 40, 87, 17);

        deo_ejelotE.setEnabled(false);
        Pcabe.add(deo_ejelotE);
        deo_ejelotE.setBounds(40, 20, 41, 17);

        Bir.setText("cButton1");
        Pcabe.add(Bir);
        Bir.setBounds(440, 70, 2, 2);
        Pcabe.add(tid_codiE);
        tid_codiE.setBounds(90, 40, 340, 17);

        cargaPSC.setSelected(true);
        cargaPSC.setText("carga PS");
        cargaPSC.setToolTipText("Carga Productos de Salida del tipo despiece");
        cargaPSC.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cargaPSC.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        Pcabe.add(cargaPSC);
        cargaPSC.setBounds(350, 20, 80, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        Pprinc.add(Pcabe, gridBagConstraints);

        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jt.setMaximumSize(new java.awt.Dimension(100, 100));
        jt.setPreferredSize(new java.awt.Dimension(100, 100));

        javax.swing.GroupLayout jtLayout = new javax.swing.GroupLayout(jt);
        jt.setLayout(jtLayout);
        jtLayout.setHorizontalGroup(
            jtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 584, Short.MAX_VALUE)
        );
        jtLayout.setVerticalGroup(
            jtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 179, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.weighty = 2.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        Pprinc.add(jt, gridBagConstraints);

        Ppie.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        Ppie.setMaximumSize(new java.awt.Dimension(500, 40));
        Ppie.setMinimumSize(new java.awt.Dimension(550, 40));
        Ppie.setName(""); // NOI18N
        Ppie.setPreferredSize(new java.awt.Dimension(550, 40));
        Ppie.setLayout(null);

        cLabel6.setText("Kilos");
        Ppie.add(cLabel6);
        cLabel6.setBounds(2, 2, 30, 17);

        kilsalE.setEnabled(false);
        Ppie.add(kilsalE);
        kilsalE.setBounds(35, 2, 50, 17);

        Baceptar.setText("Aceptar (F4)");
        Ppie.add(Baceptar);
        Baceptar.setBounds(450, 2, 100, 22);

        cLabel7.setText("Fec.Cad.");
        Ppie.add(cLabel7);
        cLabel7.setBounds(270, 2, 50, 17);

        deo_feccadE.setEditable(false);
        Ppie.add(deo_feccadE);
        deo_feccadE.setBounds(320, 2, 70, 17);

        BF2.setText("F2");
        BF2.setToolTipText("Ir de cabecera a Lineas y viceversa");
        Ppie.add(BF2);
        BF2.setBounds(10, 20, 30, 20);

        cLabel8.setText("Diferenc");
        Ppie.add(cLabel8);
        cLabel8.setBounds(160, 2, 50, 17);

        difkilE.setEnabled(false);
        Ppie.add(difkilE);
        difkilE.setBounds(220, 2, 40, 17);

        cLabel9.setText("Unid.");
        Ppie.add(cLabel9);
        cLabel9.setBounds(90, 2, 30, 17);

        unisalE.setEnabled(false);
        Ppie.add(unisalE);
        unisalE.setBounds(120, 2, 30, 17);

        cLabel10.setText("Fec. Prod.");
        Ppie.add(cLabel10);
        cLabel10.setBounds(100, 20, 60, 17);

        def_fecproE.setEditable(false);
        Ppie.add(def_fecproE);
        def_fecproE.setBounds(160, 20, 70, 17);

        cLabel11.setText("Fec. Sacrificio");
        Ppie.add(cLabel11);
        cLabel11.setBounds(240, 20, 80, 17);

        def_fecsacE.setEditable(false);
        Ppie.add(def_fecsacE);
        def_fecsacE.setBounds(320, 20, 70, 17);

        BcopLin.setToolTipText("Copiar Linea Anterior");
        Ppie.add(BcopLin);
        BcopLin.setBounds(40, 20, 53, 17);

        BImprimir.addMenu("Normal","N");
        BImprimir.addMenu("Interna","I");
        BImprimir.setToolTipText("");
        Ppie.add(BImprimir);
        BImprimir.setBounds(400, 2, 45, 22);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        Pprinc.add(Ppie, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton BF2;
    private gnu.chu.controles.CButtonMenu BImprimir;
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CButton BcopLin;
    private gnu.chu.controles.CButton Bir;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Ppie;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel10;
    private gnu.chu.controles.CLabel cLabel11;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.controles.CLabel cLabel9;
    private gnu.chu.controles.CCheckBox cargaPSC;
    private gnu.chu.controles.CTextField def_feccadE;
    private gnu.chu.controles.CTextField def_fecproE;
    private gnu.chu.controles.CTextField def_fecsacE;
    private gnu.chu.controles.CTextField def_ordenE;
    private gnu.chu.controles.CTextField deo_ejelotE;
    private gnu.chu.controles.CTextField deo_feccadE;
    private gnu.chu.controles.CTextField deo_kilosE;
    private gnu.chu.controles.CTextField deo_serlotE;
    private gnu.chu.controles.CTextField difkilE;
    private gnu.chu.controles.CGridEditable jt;
    private gnu.chu.controles.CTextField kilsalE;
    private gnu.chu.camposdb.proPanel pro_codiE;
    private gnu.chu.camposdb.proPanel pro_codsalE;
    private gnu.chu.controles.CTextField pro_indsalE;
    private gnu.chu.controles.CTextField pro_kilsalE;
    private gnu.chu.controles.CTextField pro_loteE;
    private gnu.chu.controles.CTextField pro_nombE;
    private gnu.chu.controles.CTextField pro_nombE1;
    private gnu.chu.controles.CTextField pro_numindE;
    private gnu.chu.controles.CTextField pro_unidE;
    private gnu.chu.camposdb.tidCodi2 tid_codiE;
    private gnu.chu.controles.CTextField unisalE;
    // End of variables declaration//GEN-END:variables

}
