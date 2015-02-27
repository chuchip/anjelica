package gnu.chu.anjelica.despiece;

/**
 *
 * <p>Título: despventas </p>
 * <p>Descripción: Ventana de Despiece  para Albaranes de Ventas</p>
 * <p> Comprueba la variable de entorno 
 *   autollenardesp para ver  si debe hacer autollenado de
 *   los productos para un tipo de despiece (por defecto, NO)
 * </p>
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
 * @author chuchiP
 * @version 1.0
 */

import gnu.chu.anjelica.almacen.StkPartid;
import gnu.chu.anjelica.almacen.ActualStkPart;
import gnu.chu.anjelica.almacen.pdalmace;
import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.anjelica.sql.Desorilin;
import gnu.chu.anjelica.sql.DesorilinId;
import gnu.chu.anjelica.sql.Desporig;
import gnu.chu.anjelica.sql.DesporigId;
import gnu.chu.comm.BotonBascula;
import gnu.chu.controles.CGridEditable;
import gnu.chu.controles.Cgrid;
import gnu.chu.controles.StatusBar;
import gnu.chu.sql.DatosTabla;
import gnu.chu.sql.conexion;
import gnu.chu.utilidades.CodigoBarras;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.mensajes;
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
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.SwingUtilities;


public class DespVenta extends ventana {
    private char grdBlock;
    Desporig desorca;
    Desorilin desorli;
    final static String TABLA_BLOCK="desporig";
    private int proCodi=0;
    private boolean AUTOLLENARDESP=false;
    public static final String SERIE="V";
    boolean swEdicion=false;
    public final static int JT_ORDEN=5,JT_NUMIND=4,JT_KILOS=2,JT_UNID=3,JTLIN_NUMCAJ=8;
    
    boolean nuevoDespiece=false;
    int proCodAnt;
    double defKilAnt; // Kilos anteriores
    int defUnidAnt; // Unidades anteriores
    ventana papa;
    BotonBascula botonBascula;
    int ejeNume,empCodi,deoCodi;
    utildesp utdesp;
    DatosTabla dtAdd;
    int almCodi,prvCodi;
    ActualStkPart stkPart;


    public DespVenta() {
        initComponents();
        statusBar=new StatusBar(this);
        this.add(statusBar,BorderLayout.SOUTH);
       
        setResizable(true);
        setMaximizable(false);
        setIconifiable(false);
        this.setSize(500,440);
        this.setTitle("Carga Despiece desde Ventas");
        setVersion("20120201");
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
        pro_codiE.setCamposLote(deo_ejelotE,deo_serlotE, pro_loteE,  pro_numindE, deo_kilosE);
        pro_codiE.setAyudaLotes(true);
        pro_codsalE.iniciar(dtStat, this, getLayeredPane(), EU);
        pro_codsalE.setEntrada(true);
        pro_kilsalE.setLeePesoBascula(botonBascula);
        stkPart=new ActualStkPart(dtAdd,EU.em_cod);
        tid_codiE.iniciar(dtStat, padre, padre.vl, EU);
        pro_codsalE.setProNomb(pro_nombE);
        ArrayList vc=new ArrayList();
        vc.add(pro_codsalE.getFieldProCodi());
        vc.add(pro_nombE);
        vc.add(pro_kilsalE);
        vc.add(pro_unidE);
        vc.add(pro_indsalE);
        vc.add(def_ordenE);
        jt.setCampos(vc);
        jt.removeAllDatos();
        jt.setButton(KeyEvent.VK_F2,BF2);
        Pcabe.setButton(KeyEvent.VK_F2,BF2);
        jt.setButton(KeyEvent.VK_F4,Baceptar);
        AUTOLLENARDESP= EU.getValorParam("autollenardesp", AUTOLLENARDESP);
        cargaPSC.setSelected(AUTOLLENARDESP);
        cargaPSC.setFocusable(false);
        activarEventos();
      //  pro_codsalE.setCamposLote(pro_ejlsalE,pro_sersalE, pro_lotsalE,  pro_indsalE, pro_kilsalE);
    }
    public void setProCodi(int proCodi)
    {
        this.proCodi=proCodi;
        pro_codiE.setValorInt(proCodi);
    }
    public void mostrar()
    {
          nuevoDespiece=false;
          
          deoCodi=0;
          Baceptar.setEnabled(false);
          jt.setEnabled(false);
          pro_codiE.resetTexto();
          pro_loteE.resetTexto();
          deo_ejelotE.resetTexto();
          deo_kilosE.resetTexto();
          deo_serlotE.resetTexto();
          tid_codiE.resetTexto();
          pro_numindE.resetTexto();
          
       
          Ppie.resetTexto();
          jt.removeAllDatos();
          deo_ejelotE.setValorInt(EU.ejercicio);
          deo_serlotE.setText("A");
          pro_codiE.resetCambio();
          pro_codiE.setValorInt(proCodi);
          //pro_codiE.resetCambio();
          activar(true);
          setVisible(true);
          statusBar.setEnabled(true);
          toFront();
          setEnabled(true);
          SwingUtilities.invokeLater(new Thread()
          {
            @Override
            public void run()
            {
              if (pro_codiE.isNull())
                pro_codiE.requestFocus();
              else
                pro_loteE.requestFocus();
              Pcabe.setEnabled(true);
              jt.setEnabled(false);
            }
          });
    }
    
    void pro_codiE_despuesLlenaCampos()
    {

    }
    void activarEventos()
    {
        BF2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                  irGrid();

            }
        });
        Bir.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
              BF2.doClick();
            }}
        );

        tid_codiE.addFocusListener(new FocusAdapter() {
            @Override
             public void focusGained(FocusEvent e) {
                 try {
                     buscaPeso();
                     if (!pro_codiE.hasCambio())
                         return;
                     pro_codiE.resetCambio();
                     tid_codiE.addArticulo(pro_codiE.getValorInt());
                     tid_codiE.releer();
                } catch (SQLException k)
                {
                    Error("Error en focus gained de tid_codi",k);
                }
             }
        });
        Baceptar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                  guardaDespiece();
                 
            }
        });
    }
//    /**
//     * Llena tipos de despiece disponibles para un producto de entrada
//     * @param proCodi
//     */
//    void llenaTiposDespieces(int proCodi) throws SQLException
//    {
//            tid_codiE.removeAllItems();
//            String s="SELECT t.tid_codi,t.tid_nomb FROM tipodesp as t,tipdesent as e "+
//                    " where tid_activ=2  "+ // activo y no de tactil
//                    " and t.tid_codi = e.tid_codi "+
//                    " and e.pro_codi = "+proCodi+
//                    " order by t.tid_codi";
//            dtStat.select(s);
//            tid_codiE.addDatos(dtStat);
//            tid_codiE.addDatos("9999","REENVASADO");
//    }
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
        if (deoCodi==0)
        {
            msgBox("Introduzca alguna linea en despiece");
            return;
        }
        jt.salirGrid();
        if (cambiaLineajtLin(jt.getSelectedRow())>=0)
            return;
        try {
            int nRowActivas=0;
            int nRow=jt.getRowCount();
            for (int n=0;n<nRow;n++)
            {
                if (jt.getValorInt(n,0)!=0 && jt.getValorDec(n,JT_KILOS)!=0)
                    nRowActivas++;
            }
            if (nRowActivas==0)
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

            grdBlock='N';
            if (difkilE.getValorDec() > deo_kilosE.getValorDec() * MantDesp.LIMDIF)
            { // Supera el 2% de los kilos de entrada. NO CIERRO el despiece
                grdBlock='S'; // Lo dejo como pendiente.
                stkPart.ponerStock(pro_codiE.getValorInt(),deo_ejelotE.getValorInt(),
                      empCodi,
                      deo_serlotE.getText(), pro_loteE.getValorInt(),
                      pro_numindE.getValorInt(), almCodi,difkilE.getValorDec(),
                      1);
                stkPart.setBloqueo(pro_codiE.getValorInt(),deo_ejelotE.getValorInt(),
                      empCodi,
                      deo_serlotE.getText(), pro_loteE.getValorInt(),
                      pro_numindE.getValorInt(),almCodi,true);
                   utdesp.iniciar(dtAdd,ejeNume,empCodi,almCodi,
                     almCodi,EU);
                 utdesp.setLogotipo(null);
                 utdesp.setDirEmpresa(null);
                 int proCodeti;
                 if (pro_codiE.getLikeProd().isNull("pro_codeti"))
                    proCodeti = 0;
                 else
                    proCodeti = pro_codiE.getLikeProd().getInt("pro_codeti");
                 utdesp.imprEtiq(proCodeti,dtCon1,pro_codiE.getValorInt(), pro_nombE.getText(),
                    "D",
                    pro_loteE.getValorInt(),
                    ""+deo_ejelotE.getValorInt(), deo_serlotE.getText(),
                    pro_numindE.getValorInt(),
                    difkilE.getValorDec(),
                    Formatear.getFecha( utdesp.getFecDesp(),"dd-MM-yyyy"),
                    utdesp.getFechaProduccion(),
                    Formatear.getFecha(utdesp.getFecCaduc(),"dd-MM-yyyy"),
                    utdesp.getFecSacrif(),//jtLin.getValDate(linea,5,def_feccadE.getFormato()),
                    utdesp.getFecCaduc());
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

        // Actualiza la cabecera.
       
//            s="SELECT * FROM desporig  WHERE eje_nume = " + ejeNume +
//                   " and deo_codi = " + deoCodi;
//            if (! dtAdd.select(s,true))
//                throw new SQLException("No encontrado cabecera despiece: "+deoCodi);
//            dtAdd.edit();
//            dtAdd.setDato("pro_codi",pro_codiE.getValorInt());
//            dtAdd.setDato("tid_codi", tid_codiE.getValorInt());
//            dtAdd.setDato("deo_ejelot", deo_ejelotE.getValorInt());
//            dtAdd.setDato("deo_serlot", deo_serlotE.getText());
//            dtAdd.setDato("pro_lote", pro_loteE.getValorInt());
//            dtAdd.setDato("pro_numind", pro_numindE.getValorDec());
//            dtAdd.update();
            actualCabDesp();
            resetBloqueo(dtAdd,TABLA_BLOCK, ejeNume+"|"+empCodi+
                         "|"+deoCodi,false);
            dtAdd.commit();
            nuevoDespiece=true;
        } catch (Throwable k)
        {
           Error("Error al guardar despiece ",k);
        }
        matar();
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
  try
   {
     stkPart.restar(ejeLot, serLot, numLot, numInd, proCodi,
                    almCodi, kilos, 1);
   }
   catch (java.sql.SQLWarning k)
   {
     enviaMailError("Usuario: "+EU.usuario+"\nNO SE Pudo restar stock en despVenta: "+
                             " deo_codi: " + deoCodi + "\n" + k.getMessage());
   }
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
   desorca.setDeoFeccad(def_feccadE.getDate());
   desorca.setDeoFecpro(Formatear.getDateAct());
   desorca.setPrvCodi(prvCodi);
   desorca.setDeoDesnue('N');
   desorca.setDeoEjloge(deo_ejelotE.getValorInt());
   desorca.setDeoSeloge(deo_serlotE.getText());
   desorca.setDeoNuloge(pro_loteE.getValorInt());
   desorca.setDeoLotnue((short) 0);
   desorca.setDeoCerra((short) (grdBlock=='S'?-1:0));
   desorca.setDeoBlock( (grdBlock=='S'?"S":"N"));
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

       Hashtable<Integer,Integer> htGru  = new Hashtable();
       String s="SELECT distinct(tds_grupo) as tds_grupo FROM tipdessal  WHERE tid_codi = "+tidCodi;
       if (dt.select(s))
       {
         do
         {
           htGru.put(dt.getInt("tds_grupo"),0);
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
         
         if (htGru.get(dt.getInt("tds_grupo"))==null)
           htGru.put(dt.getInt("tds_grupo"),jt.getValorInt(n,JT_UNID)*jt.getValorInt(n,JTLIN_NUMCAJ));
         else
         {
           if (jt.getColumnCount()==6)
            nEle= htGru.get(dt.getInt("tds_grupo")) + jt.getValorInt(n,JT_UNID);
           else    
            nEle= htGru.get(dt.getInt("tds_grupo")) + (jt.getValorInt(n,JT_UNID) * jt.getValorInt(n,JTLIN_NUMCAJ));
           htGru.put(dt.getInt("tds_grupo"),nEle);
         }
       }
       Enumeration<Integer> en=htGru.keys();
       int grupo;
       int tdsUnid;
       while (en.hasMoreElements())
       {
         grupo =  en.nextElement();
         s="SELECT tds_unid,pro_codi FROM tipdessal  WHERE tid_codi = "+tidCodi+
             " and tds_grupo = " + grupo;
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
           stkPart.anuStkPart(dtAdd.getInt("pro_codi"), dtAdd.getInt("deo_ejelot"), dtAdd.getInt("deo_emplot"),
                      dtAdd.getString("deo_serlot"), dtAdd.getInt("pro_lote"),
                      dtAdd.getInt("pro_numind"), almCodi,dtAdd.getDouble("deo_kilos")*-1,
                      -1);
           dtAdd.executeUpdate("delete from desporig WHERE eje_nume = " + ejeNume +
             " and deo_codi = " + deoCodi);
           dtAdd.executeUpdate("delete from desorilin WHERE eje_nume = " + ejeNume +
             " and deo_codi = " + deoCodi);
           deoCodi=0;
    }
    void cancelarDespiece()
    {
        try
        {
           cancelaCabecera();
         
           int nRow=jt.getRowCount();
           for (int n=0;n<nRow;n++)
               borraLinDesp(jt.getValorInt(n,JT_ORDEN));
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
       if (!pro_codiE.controla(true))
       {
           mensajeErr(pro_codiE.getMsgError());
           return false;
       }
       if (! buscaPeso())
           return false;
       if (! tid_codiE.controla())
       {
           mensajeErr("Tipo Despiece NO valido ");
           return false;
       }
       if (tid_codiE.getValorInt()==0)
       {
           mensajeErr("Introduca un tipo de despiece");
           tid_codiE.requestFocus();
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
            if (!jt.isEnabled())
            {
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
               activar(false);
               jt.setEnabled(true);
               jt.requestFocusInicio();
            }
            else
            { // Ir a cabecera
                activar(true);
                tid_codiE.setEnabled(false);
                jt.setEnabled(false);
                pro_codiE.requestFocus();
            }
        } catch (Exception k)
        {
            Error("Error en irGrid",k);
        }
    }
    /**
     * Devuelve la sentencia SQL a ejecutar para mostrar los productos disponibles
     * como de salida para un tipo de despiece y un producto de origen dados.
     * @param tidCodi Tipo de Despiece
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
    void llenaGrid() throws SQLException
    {

        if (tid_codiE.getValorInt()==MantTipDesp.LIBRE_DESPIECE ||  
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
            v.add("0"); // Unid
            v.add("1"); // Kg
            v.add("0"); // N Ind.
            v.add("0"); // N. Orden
            jt.addLinea(v);
        } while (dtCon1.next());   
    }
    void activar(boolean b)
    {
        statusBar.setEnabled(!b);
        if (deoCodi!=0)
            Baceptar.setEnabled(!b);
        pro_codiE.setEnabled(b);
        deo_ejelotE.setEnabled(b);
        deo_serlotE.setEnabled(b);
        tid_codiE.setEnabled(b);
        pro_numindE.setEnabled(b);
        pro_loteE.setEnabled(b);
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
         String s;
         s=pro_codsalE.getNombArt();
         jt.setValor(s,linea,1);
         if (pro_kilsalE.getValorDec()==0 ||  pro_codsalE.isNull())
           return -1; // Si NO tengo Kilos o no me han metido el codigo de prod. paso de todo

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
         if (checkExcluyentes("SELECT * FROM artiexcl where pro_codini ="+pro_codsalE.getValorInt()+
                 " and pro_codfin in (",linea))
             return 0;
         if (checkExcluyentes("SELECT * FROM artiexcl where pro_codfin ="+pro_codsalE.getValorInt()+
                 " and pro_codini in (",linea))
             return 0;
         if (pro_codsalE.hasCambio() || pro_kilsalE.hasCambio() )
         {
            mensajeErr("");
            guardaLinea(linea);
            mensajeErr("Linea " + linea + "... Guardada");

           if ( pro_codsalE.isVendible() && pro_codsalE.getEtiCodi()>=0 && jt.isEnabled()) // && pro_codlE.getConStkInd())
             imprEtiq(linea);
         }
       }
       catch (SQLException ex)
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
         if (prvCodi==0)
            prvCodi=prvDespiece;
    //     return false;
         return true;
       }
       if (prvCodi==0)
            prvCodi=prvDespiece;
       if (tid_codiE.getValorInt()!=MantTipDesp.AUTO_DESPIECE)
        utdesp.setDespNuestro(Formatear.getFechaAct("dd-MM-yyyy"), dtStat);
       def_feccadE.setText(utdesp.feccadE);
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

     if (nInd!=0)
     { // Existe linea. Anulo stock Anterior.
        stkPart.anuStkPart(proCodAnt,ejeNume,empCodi,SERIE,deoCodi,
                    nInd,almCodi,defKilAnt,defUnidAnt);
     }
     else
     {
        nInd = utildesp.getMaxNumInd( dtAdd,pro_codsalE.getValorInt(), ejeNume, empCodi,
               SERIE, deoCodi);
     }
     defOrden=guardaLinDesp(ejeNume, empCodi,
                SERIE, deoCodi,nInd,
                pro_codsalE.getValorInt(),
                pro_kilsalE.getValorDec(), pro_unidE.getValorInt(),1,
                def_feccadE.getText(),defOrden);
     stkPart.sumar(ejeNume, SERIE,deoCodi,nInd,pro_codsalE.getValorInt(),
              almCodi,
              pro_kilsalE.getValorDec(),pro_unidE.getValorInt(),
              null,ActualStkPart.CREAR_SI,
              prvCodi,def_feccadE.getDate());
    
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
 void imprEtiq(int linea)
 {
   int proCodeti;
   try {


     String nombArt=pro_codsalE.getNombArt(pro_codsalE.getText());
     if (pro_codsalE.getLikeProd().isNull("pro_codeti"))
       proCodeti = 0;
     else
       proCodeti = pro_codsalE.getLikeProd().getInt("pro_codeti");

//     debug("Nombre Articulo: "+nombArt);
     utdesp.iniciar(dtAdd,ejeNume,empCodi,almCodi,
                    almCodi,EU);
     utdesp.setLogotipo(null);
     utdesp.setDirEmpresa(null);
     utdesp.imprEtiq(proCodeti,dtCon1,jt.getValorInt(linea,0), nombArt,
                    "D",
                    deoCodi,
                    ""+ejeNume, SERIE,
                    jt.getValorInt(linea,JT_NUMIND),
                    pro_kilsalE.getValorDec(),
                    Formatear.getFechaAct("dd-MM-yyyy"),
                    Formatear.getDateAct(),
                    def_feccadE.getText(),
                    utdesp.getFecSacrif(),//jtLin.getValDate(linea,5,def_feccadE.getFormato()),
                    def_feccadE.getDate());
      mensajeErr("Etiqueta ... Listada");
   }
   catch (Throwable ex)
   {
     Error("Error al Guardar Datos Despiece", ex);
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
       String s = "SELECT * FROM v_despfin " +
           " WHERE eje_nume = " + ejeNume +
           " and emp_codi = " +empCodi +
           " and deo_codi = " + deoCodi +
           " and def_orden = " + defOrden;
       if (!dtAdd.select(s, true))
         return false;

       if (stkPart.anuStkPart(dtAdd.getInt("pro_codi"), dtAdd.getInt("def_ejelot"), dtAdd.getInt("def_emplot"),
                      dtAdd.getString("def_serlot"), dtAdd.getInt("pro_lote"),
                      dtAdd.getInt("pro_numind"), almCodi,dtAdd.getDouble("def_kilos"),
                      dtAdd.getInt("def_numpie"))==0)
       {
         msgBox("No encontrado apunte en Stock-Partidas");
         enviaMailError("No encontrado despiece en stock-partidas en DespVentas"+s);
         return false;
       }
       s = "DELETE FROM v_despfin " +
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
        jt = new gnu.chu.controles.CGridEditable(6){
            @Override
            public void afterCambiaLinea()
            {
                proCodAnt=pro_codsalE.getValorInt();
                defKilAnt=pro_kilsalE.getValorDec();
                defUnidAnt=pro_unidE.getValorInt();
                pro_codsalE.resetCambio();
                pro_kilsalE.resetCambio();
                pro_unidE.resetCambio();
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
                jt.setValor(""+proCodAnt,0); // Rest. El valor Antiguo
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
        v.add("Unid"); // 3
        v.add("Ind."); // 4
        v.add("Ord"); // 5
        jt.setCabecera(v);
        jt.setAnchoColumna(new int[]{80,180,60,40,50,30});
        jt.setAlinearColumna(new int[]{2,0,2,2,2,2});
        Ppie = new gnu.chu.controles.CPanel();
        cLabel6 = new gnu.chu.controles.CLabel();
        kilsalE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9.99");
        Baceptar = new gnu.chu.controles.CButton(Iconos.getImageIcon("check"));
        cLabel7 = new gnu.chu.controles.CLabel();
        def_feccadE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        BF2 = new gnu.chu.controles.CButton();
        cLabel8 = new gnu.chu.controles.CLabel();
        difkilE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---9.99");
        cLabel9 = new gnu.chu.controles.CLabel();
        unisalE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9.99");

        pro_nombE.setEnabled(false);

        pro_indsalE.setEnabled(false);

        def_ordenE.setText("0");
        def_ordenE.setEnabled(false);

        pro_unidE.setText("1");

        Pprinc.setLayout(new java.awt.GridBagLayout());

        Pcabe.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Pcabe.setMaximumSize(new java.awt.Dimension(448, 79));
        Pcabe.setMinimumSize(new java.awt.Dimension(448, 79));
        Pcabe.setPreferredSize(new java.awt.Dimension(448, 79));
        Pcabe.setLayout(null);

        pro_codiE.setToolTipText("F3 Cons. Lotes Disponibles");
        Pcabe.add(pro_codiE);
        pro_codiE.setBounds(58, 13, 374, 17);

        cLabel1.setText("Articulo");
        Pcabe.add(cLabel1);
        cLabel1.setBounds(5, 15, 43, 15);

        cLabel2.setText("Lote");
        Pcabe.add(cLabel2);
        cLabel2.setBounds(5, 35, 40, 17);

        deo_serlotE.setText("A");
        deo_serlotE.setMayusc(true);
        Pcabe.add(deo_serlotE);
        deo_serlotE.setBounds(90, 35, 18, 17);
        Pcabe.add(pro_loteE);
        pro_loteE.setBounds(110, 35, 41, 17);

        cLabel3.setText("Individuo");
        Pcabe.add(cLabel3);
        cLabel3.setBounds(155, 35, 53, 17);
        Pcabe.add(pro_numindE);
        pro_numindE.setBounds(210, 35, 42, 17);

        cLabel4.setText("Kilos");
        Pcabe.add(cLabel4);
        cLabel4.setBounds(260, 35, 36, 17);

        deo_kilosE.setEnabled(false);
        Pcabe.add(deo_kilosE);
        deo_kilosE.setBounds(300, 35, 52, 17);

        cLabel5.setText("Tipo Despiece");
        Pcabe.add(cLabel5);
        cLabel5.setBounds(5, 55, 87, 17);
        Pcabe.add(deo_ejelotE);
        deo_ejelotE.setBounds(40, 35, 41, 17);

        Bir.setText("cButton1");
        Pcabe.add(Bir);
        Bir.setBounds(440, 70, 2, 2);

        tid_codiE.setAncTexto(40);
        Pcabe.add(tid_codiE);
        tid_codiE.setBounds(90, 55, 340, 17);

        cargaPSC.setText("carga PS");
        cargaPSC.setToolTipText("Carga Productos de Salida del tipo despiece");
        Pcabe.add(cargaPSC);
        cargaPSC.setBounds(350, 35, 80, 17);

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
            .addGap(0, 167, Short.MAX_VALUE)
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
        Ppie.setMaximumSize(new java.awt.Dimension(500, 36));
        Ppie.setMinimumSize(new java.awt.Dimension(500, 36));
        Ppie.setPreferredSize(new java.awt.Dimension(500, 36));
        Ppie.setLayout(null);

        cLabel6.setText("Kilos");
        Ppie.add(cLabel6);
        cLabel6.setBounds(2, 2, 30, 17);

        kilsalE.setEnabled(false);
        Ppie.add(kilsalE);
        kilsalE.setBounds(35, 2, 50, 17);

        Baceptar.setText("Aceptar");
        Ppie.add(Baceptar);
        Baceptar.setBounds(394, 2, 99, 32);

        cLabel7.setText("Fec.Cad");
        Ppie.add(cLabel7);
        cLabel7.setBounds(270, 2, 60, 17);
        Ppie.add(def_feccadE);
        def_feccadE.setBounds(320, 2, 70, 17);

        BF2.setText("F2");
        BF2.setToolTipText("Ir de cabecera a Lineas y viceversa");
        Ppie.add(BF2);
        BF2.setBounds(20, 20, 30, 17);

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
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CButton Bir;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Ppie;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CLabel cLabel1;
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
    private gnu.chu.controles.CTextField def_ordenE;
    private gnu.chu.controles.CTextField deo_ejelotE;
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
    private gnu.chu.controles.CTextField pro_numindE;
    private gnu.chu.controles.CTextField pro_unidE;
    private gnu.chu.camposdb.tidCodi2 tid_codiE;
    private gnu.chu.controles.CTextField unisalE;
    // End of variables declaration//GEN-END:variables

}
