package gnu.chu.anjelica.ventas;


/**
 *
 * <p>Titulo: MantAlbRuta </p>
* <p>Descripcion: Mantenimiento Albaranes de ruta
*  Utilizado para meter los albaranes entregados en una ruta por un
*  repartidor</p>
* <p>Parametros: modSala: true/false. Indica si es en modo sala, con lo cual 
* solo podra modificar partes para poner kms,vehiculo y comentarios.
* Por defecto modSala=false
* </p>
 * <p>Copyright: Copyright (c) 2005-2017
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los terminos de la Licencia Pública General de GNU según es publicada por
 *  la Free Software Foundation, bien de la versión 2 de dicha Licencia
 *  o bien (según su elección) de cualquier versión posterior.
 *  Este programa se distribuye con la esperanza de que sea útil,ed
 *  pero SIN NINGUNA GARANTIA, incluso sin la garantía MERCANTIL implícita
 *  o sin garantizar la CONVENIENCIA PARA UN PROPOSITO PARTICULAR.
 *  Véase la Licencia Pública General de GNU para más detalles.
 *  Debería haber recibido una copia de la Licencia Pública General junto con este programa.
 *  Si no ha sido así, escriba a la Free Software Foundation, Inc.,
 *  en 675 Mass Ave, Cambridge, MA 02139, EEUU.
 * </p>
 * <p>Empresa: miSL</p>
* @author Chuchi P
* @version 1.0
*/
import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.facturacion.PadFactur;
import gnu.chu.anjelica.listados.Listados;
import static gnu.chu.anjelica.listados.etiqueta.LOGOTIPO;
import gnu.chu.anjelica.pad.pdclien;
import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.controles.StatusBar;
import gnu.chu.eventos.GridAdapter;
import gnu.chu.eventos.GridEvent;
import gnu.chu.interfaces.PAD;
import gnu.chu.interfaces.ejecutable;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.mensajes;
import gnu.chu.utilidades.miThread;
import gnu.chu.utilidades.navegador;
import gnu.chu.utilidades.ventanaPad;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class ManAlbRuta extends ventanaPad implements PAD
{
    boolean swOrdenado=false;
    JasperReport jr=null;
    private boolean ARG_MODSALA=false;
    final int JT_NUMALB=3;
    final int JT_BULTOS=4;
    final int JT_PALETS=5;
    final int JT_CLICOD=6;
    final int JT_CLINOMB=7;
    final int JT_CODRUT=8;
    final int JT_CLIDIR=9;
    final int JT_CLICP=10;
    final int JT_CLIPOBL=11;
    final int JT_UNID=12;
    final int JT_KILOS=13;
    final int JT_HORREP=14;
    final int JT_COMREP=15;
    final int JT_REPET=16;
    final int JTFR_ANO=0;
    final int JTFR_EMPCOD=1;
    final int JTFR_SERIE=2;
    final int JTFR_NUME=3;
    final int JTFR_CLICOD=4;
    final int  JTFR_IMPPEN=8;
    
    public ManAlbRuta(EntornoUsuario eu, Principal p) {
        this(eu, p, null);
    }

    public ManAlbRuta(EntornoUsuario eu, Principal p, Hashtable ht) {
        EU = eu;
        vl = p.panel1;
        jf = p;
        eje = true;

        try
        {
            ponParametros(ht);
            setTitulo("Mantenimiento salidas de ruta");
            setAcronimo("masaru");
            if (jf.gestor.apuntar(this))
                jbInit();
            else
                setErrorInit(true);
        } catch (Exception e)
        {
            ErrorInit(e);
        }
    }

    public ManAlbRuta(gnu.chu.anjelica.menu p, EntornoUsuario eu, Hashtable ht) {
        EU = eu;
        vl = p.getLayeredPane();
        eje = false;

        try {
          ponParametros(ht);
            setAcronimo("masaru");
          setTitulo("Mantenimiento salidas de ruta");
          jbInit();
        } catch (Exception e) {
            ErrorInit(e);
        } 
    } 
    private void ponParametros(Hashtable<String,String> ht)
    {
        if (ht != null)
        {
            if (ht.get("modSala") != null)
                ARG_MODSALA = Boolean.parseBoolean(ht.get("modSala"));
        }
    }
    private void jbInit() throws Exception {
        
        statusBar = new StatusBar(this);
        nav = new navegador(this, dtCons, false, navegador.NORMAL);
        
        iniciarFrame();
        this.setVersion("2017-10-22 "+(ARG_MODSALA?" Modo Sala ":""));
        
        strSql = "SELECT * FROM albrutacab "+
            (ARG_MODSALA?" where usu_nomb ='"+EU.usuario+"'":"")+
            " order by alr_fecha,alr_nume";

        this.getContentPane().add(nav, BorderLayout.NORTH);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        this.setPad(this);
        conecta();
       
        initComponents();
        iniciarBotones(Baceptar, Bcancelar);
        if (ARG_MODSALA)
        {
            nav.removeBoton(navegador.ADDNEW);
            nav.removeBoton(navegador.DELETE);
        }
        navActivarAll();
     
        this.setSize(663,524);
        
        activar(false);  
    }
    /**
     * Devuelve numero de albaran
     * @param row linea grid
     * @return  -1 Si no se comnprobara albaran. -2 Si esta todo bien. >=0 ERROR
     */
    int getDatosAlb(int row)
    {
         try
        {            
            if (! albaranHasCambio() )
                return -1;         
            jt.setValor(false,row,JT_REPET);
            if (avc_numeE.getValorInt()==0)
            {
                limpiaLinea(row);
                return -1; // No tiene Albaran. Lo ignoro.
            }
            if (! pdalbara.getAlbaranCab(dtCon1,emp_codiE.getValorInt(),avc_anoE.getValorInt(),avc_serieE.getText(),avc_numeE.getValorInt()))
            {
                mensajeErr("Albaran NO encontrado");
                limpiaLinea(row);
                return JT_NUMALB;
            }
            
            if (dtCon1.getInt("div_codi")<0 && ! EU.isRootAV())
            {
                mensajeErr("Albaran NO encontrado.");
                limpiaLinea(row);
                return JT_NUMALB; 
            }
            resetCambioAlb();          
           
            jt.setValor(dtCon1.getInt("cli_codi"),row,JT_CLICOD);
            pdclien.getNombreCliente(dtStat,dtCon1.getInt("cli_codi"));
            String cliNomb=dtCon1.getString("avc_clinom").equals("")?
                dtStat.getString("cli_nomen"):dtCon1.getString("avc_clinom");
            jt.setValor(cliNomb,row,JT_CLINOMB);
            jt.setValor(dtStat.getString("cli_diree"),row,JT_CLIDIR);
            jt.setValor(dtStat.getString("cli_codpoe"),row,JT_CLICP);
            jt.setValor(dtStat.getString("cli_poble"),row,JT_CLIPOBL);

            jt.setValor(dtCon1.getDouble("avc_kilos"),row,JT_KILOS);
            jt.setValor(dtCon1.getInt("avc_unid"),row,JT_UNID);
            jt.setValor(dtStat.getString("cli_horenv"),row,JT_HORREP);
            jt.setValor(dtStat.getString("cli_comenv"),row,JT_COMREP);
            jt.setValor(dtStat.getString("cli_codrut"),row,JT_CODRUT);
            cli_nombE.setText(cliNomb);
            cli_direeE.setText(dtStat.getString("cli_diree"));
            
            cli_codpoeE.setText(dtStat.getString("cli_codpoe"));
            cli_pobleE.setText(dtStat.getString("cli_poble"));
            alr_horrepE.setText(dtStat.getString("cli_horenv"));
            alr_comrepE.setText(dtStat.getString("cli_comenv"));
            return -2;
        } catch (SQLException ex)
        {
            Error("Error al comprobar albaran para ruta", ex);
        }
        return -1;
    }
    void resetCambioAlb()
    {
          avc_numeE.resetCambio();
          avc_anoE.resetCambio();
          avc_serieE.resetCambio();
          emp_codiE.resetCambio();
    }
    boolean albaranHasCambio()
    {
        return avc_numeE.hasCambio() || avc_anoE.hasCambio() || avc_serieE.hasCambio() || emp_codiE.hasCambio() ;
    }
    int cambiaLinJT(int row)
    {
        try
        {
          
//            if (!alr_bultosE.hasCambio() && !alr_paletsE.hasCambio() && ! albaranHasCambio() )
//                return -1;
            int nl=getDatosAlb(row);
            
            if (nl>=0)
                return JT_NUMALB; 

            String s;
          
            
            if (! jt.getValBoolean(row,JT_REPET) )
            {
                s="select * from v_albruta where emp_codi ="+emp_codiE.getValorInt()
                    + " and  avc_ano = " + avc_anoE.getValorInt()
                    + " and avc_serie ='" + avc_serieE.getText() + "'"
                    + " and avc_nume = " + avc_numeE.getValorInt()
                    + " and alr_nume != " + alr_numeE.getValorInt();
                if (dtStat.select(s))
                {
                    int res = mensajes.mensajeYesNo("Albaran: "+ avc_numeE.getValorInt()+" ya se sirvio en ruta: " + dtStat.getInt("alr_nume")
                        + " de fecha: " + dtStat.getFecha("alr_fecha", "dd-MM-yyyy") + "\n Volver a cargar en ruta ?");
                    if (res != mensajes.YES)
                        return 0;
                    jt.setValor(true, row, JT_REPET);
                }
            }
         
            if (checkLineaRepe(emp_codiE.getValorInt(),avc_anoE.getValorInt(),avc_serieE.getText(),
                avc_numeE.getValorInt(),row ))
                return 0;           
            alr_bultosE.resetCambio();
            alr_paletsE.resetCambio();
            fvc_numeE.resetCambio();
            alr_comrepE.resetCambio();
            alr_horrepE.resetCambio();
           
            actAcumul();
        } catch (SQLException ex)
        {
            Error("Error al comprobar albaran para ruta", ex);
        }
        return -1;
    }
    private boolean  checkLineaRepe(int empCodi,int avcAno, String avcSerie,int avcNume, int row)
    {
        if (avcAno==0 || avcNume==0 ||avcSerie.trim().equals(""))
            return false;
        int nRows = jt.getRowCount();
        for (int n = 0; n < nRows; n++)
        {
            if (n == row)
                continue;
            if (jt.getValorInt(n, 0) == empCodi
                && jt.getValorInt(n, 1) == avcAno
                && jt.getValString(n, 2).equals(avcSerie)
                && jt.getValorInt(n, 3) == avcNume)
            {
                if (row>=0)
                    msgBox("Albaran ya se metio en la linea: " + n + " de esta carga");
                return true;
            }
        }
        return false;
}
    private void  limpiaLinea(int row)
    {
        jt.setValor(0, row, JT_CLICOD);
        jt.setValor("", row, JT_CLINOMB);
        jt.setValor("", row, JT_CODRUT);
        jt.setValor(0, row, JT_UNID);
        jt.setValor(0, row, JT_KILOS);
    }
    
    void verDatos()
    {
        if (dtCons.getNOREG())
            return;
        try {
            alr_numeE.setValorInt(dtCons.getInt("alr_nume"));
            if (! dtCon1.select("select * from albrutacab where alr_nume ="+dtCons.getInt("alr_nume")))
            {
                Pcabe.resetTexto();
                msgBox("No encontrado parte ruta con ID: "+dtCons.getInt("alr_nume"));
                return;
            }
            rut_codiE.setText(dtCon1.getString("rut_codi"));
            tra_codiE.setValor(dtCon1.getString("usu_nomb"));
            alr_fechaE.setDate(dtCon1.getDate("alr_fecha"));
            alr_fecsalE.setDate(dtCon1.getDate("alr_fecsal"));
            alr_fecsalH.setText(dtCon1.getFecha("alr_fecsal","HH"));
            alr_fecsalM.setText(dtCon1.getFecha("alr_fecsal","mm"));
            alr_fecregE.setDate(dtCon1.getDate("alr_fecreg"));
            alr_fecregH.setText(dtCon1.getFecha("alr_fecreg","HH"));
            alr_fecregM.setText(dtCon1.getFecha("alr_fecreg","mm"));
            veh_codiE.setText(dtCon1.getString("veh_codi"));
            alr_vekminE.setValorInt(dtCon1.getInt("alr_vekmin",true));
            alr_vekmfiE.setValorInt(dtCon1.getInt("alr_vekmfi",true));
            //alr_impgasE.setValorInt(dtCon1.getInt("alr_impgas",true));
            alr_comentE.setText(dtCon1.getString("alr_coment"));
            PPie.resetTexto();
        
            jt.removeAllDatos();
            jtFra.removeAllDatos();
            if ( dtCon1.select("select l.*,cl.cli_nomen as cli_nomb,cl.cli_pobl,cl.cli_codrut from v_albruta as l "
                 + " left join v_cliente as cl "+
                " on l.cli_codi = cl.cli_codi where alr_nume ="+dtCons.getInt("alr_nume")+                
                 " order by "+
                (swOrdenado?"l.emp_codi,l.avc_ano,l.avc_serie,l.avc_nume ":"alr_orden")))
            {

                 do
                 {
                     ArrayList a = new ArrayList();
                     a.add(dtCon1.getString("emp_codi"));
                     a.add(dtCon1.getString("avc_ano"));
                     a.add(dtCon1.getString("avc_serie"));
                     a.add(dtCon1.getString("avc_nume"));
                     a.add(dtCon1.getString("alr_bultos"));
                     a.add(dtCon1.getString("alr_palets"));
                     a.add(dtCon1.getString("cli_codi"));
                     
                     a.add(dtCon1.getObject("cli_nomen")==null?dtCon1.getString("cli_nomb"):
                         dtCon1.getString("cli_nomen"));
                     a.add(dtCon1.getString("cli_codrut"));
                     a.add(dtCon1.getString("cli_diree", true));
                     a.add(dtCon1.getString("cli_codpoe", true));
                     a.add(dtCon1.getObject("cli_nomen")==null?dtCon1.getString("cli_pobl"):
                         dtCon1.getString("cli_poble"));
                                       
                     
                     
                     if (dtCon1.getObject("alr_unid") == null)
                     {
                         a.add(dtCon1.getString("avc_unid"));
                         a.add(dtCon1.getString("avc_kilos"));
                     } else
                     {  
                         a.add(dtCon1.getString("alr_unid"));
                         a.add(dtCon1.getString("alr_kilos"));
                     }
                     a.add(dtCon1.getString("alr_horrep"));
                     a.add(dtCon1.getString("alr_comrep"));
                     a.add((dtCon1.getInt("alr_repet") != 0));
                     jt.addLinea(a);
                 } while (dtCon1.next());
                 jt.requestFocusInicio();
             }
//             else
//                 msgBox("No encontradas albaranes para parte ruta con ID: "+dtCons.getInt("alr_nume"));
             if ( dtCon1.select("select l.*,cl.cli_nomb from v_cobruta as l "
                 + " left join v_cliente as cl "+
                " on l.cli_codi = cl.cli_codi where alr_nume ="+dtCons.getInt("alr_nume")+
                 " order by "+(swOrdenado?"l.emp_codi,l.fvc_ano,l.fvc_serie,l.fvc_nume ":"cru_orden")  ))
            {

                 do
                 {
                     ArrayList a = new ArrayList();                     
                     a.add(dtCon1.getString("fvc_ano"));
                     a.add(dtCon1.getString("emp_codi"));
                     a.add(dtCon1.getString("fvc_serie"));
                     a.add(dtCon1.getString("fvc_nume"));
                     a.add(dtCon1.getString("cli_codi"));
                     a.add(dtCon1.getString("fvc_clinom", true).equals("") ? dtCon1.getString("cli_nomb") : dtCon1.getString("fvc_clinom", true));
                     a.add(dtCon1.getFecha("fvc_fecfra","dd-MM-yyyy"));                     
                     a.add(dtCon1.getString("fvc_sumtot"));                     
                     a.add(dtCon1.getString("fvc_imppen"));
                     jtFra.addLinea(a);
                 } while (dtCon1.next());
                 jtFra.requestFocusInicio();
             }
             
            actAcumul();
        } catch (SQLException ex)
        {
            Error("Error al comprobar albaran para ruta", ex);
        }
    }
    /**
     * Actualizar acumulado
     */
    void actAcumul()
    {
        int nRow=jt.getRowCount();
        double kilos=0;
        int unid=0;
        int nAlb=0;
        int bultos=0;
        int palets=0;
        for (int n=0;n<nRow;n++)
        {
            if (jt.getValorInt(n,JT_NUMALB)==0)
                continue;
            nAlb++;
            bultos+=jt.getValorInt(n,JT_BULTOS);
            palets+=jt.getValorInt(n,JT_PALETS);
            kilos+=jt.getValorDec(n,JT_KILOS);
            unid+=jt.getValorInt(n,JT_UNID);
        }
        kilosTotE.setValorDec(kilos);
       
        numAlbE.setValorInt(nAlb);
        bulTotE.setValorDec(bultos);
        palTotE.setValorDec(palets);
        nRow=jtFra.getRowCount();
        int nFra=0;
        double impCobros=0;
        for (int n=0;n<nRow;n++)
        {
            if (jtFra.getValorInt(n,JTFR_NUME)==0)
                continue;
            nFra++;            
            impCobros+=jtFra.getValorDec(n,JTFR_IMPPEN);            
        }
        numFrasE.setValorDec(nFra);
        impFrasE.setValorDec(impCobros);
    }
    @Override
  public void PADAddNew()
  {    
    alr_numeE.setEnabled(false);
    mensaje("Insertar Nuevo Registro");
    swOrdenado=false;
    Pcabe.resetTexto();
    alr_comentE.resetTexto();
    tra_codiE.setValor(EU.usuario);
    alr_fechaE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
    alr_fecsalE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
    
    veh_codiE.setText("");
    jt.removeAllDatos();
    jt.setDragEnabled(false); 
    ordenarC.setSelected(false);
    jtFra.removeAllDatos();
    activar(true);
    kilosTotE.setValorDec(0);
 
    numAlbE.setValorDec(0);
    Tpanel1.setSelectedIndex(0);
    jtFra.requestFocusInicio();
    jtFra.ponValores(0,false,false);
    jt.requestFocusInicio();
    jt.ponValores(0,false,false);
    jt.setEnabled(false);
    jtFra.setEnabled(false);
    alr_fechaE.requestFocus();
  }
  @Override
  public void PADQuery()
  {    
    mensaje("Introduzca Criterios de Busqueda");
    nav.pulsado = navegador.QUERY;
    activar(true);
    jt.setEnabled(false);
    Pcabe.setQuery(true);    
    Pcabe.resetTexto();
    alr_comentE.resetTexto();
    alr_fechaE.requestFocus();
  }
    @Override
   public void PADEdit()
   {
      if (!tra_codiE.getValor().equals(EU.usuario) && ARG_MODSALA)
      {
        msgBox("No tiene permisos para editar este registro");
         nav.pulsado = navegador.NINGUNO;
        activaTodo();
        return;
      }
      if (swOrdenado)
      {
          swOrdenado=false;
          verDatos();
      }
     activar(true);
     
     try
     {
         if (!setBloqueo(dtAdd, "albrutacab", alr_numeE.getText()))
         {
             msgBox(msgBloqueo);
             nav.pulsado = navegador.NINGUNO;
             activaTodo();
             return;
         }
         String s = "SELECT * FROM albrutacab WHERE alr_nume = " + alr_numeE.getValorInt();
         if (!dtAdd.select(s, true))
         {
             mensajeErr("Registro ha sido borrado");
             resetBloqueo(dtAdd, "albrutacab", alr_numeE.getText(), true);
             activaTodo();
             mensaje("");
             return;
         }

     } catch (SQLException | UnknownHostException k)
     {
       Error("Error al bloquear el registro", k);
       return;
     }
     jt.setDragEnabled(false); 
     ordenarC.setSelected(false);

     alr_numeE.setEnabled(false);
     alr_fechaE.resetCambio();
     resetCambioAlb();
     alr_bultosE.resetCambio();
     alr_paletsE.resetCambio();
     fvc_numeE.resetCambio();
     alr_comrepE.resetCambio();
     alr_horrepE.resetCambio();
     Tpanel1.setSelectedIndex(0);
     if (ARG_MODSALA)
     {
         jt.setEnabled(false);
         alr_fechaE.setEnabled(false);
         rut_codiE.setEnabled(false);
         alr_fecsalE.requestFocus();
     }
     else
     {        
        jtFra.requestFocusInicio();
        jt.requestFocusInicio();
     }
     mensaje("MODIFICANDO registro activo ....");
  }
    @Override
   public void PADDelete()
   {
     try
     {
         if (ARG_MODSALA)
         {
             if (tra_codiE.getValor().equals(EU.usuario))
             {
                 msgBox("No tiene permisos para editar este registro");
                 nav.pulsado = navegador.NINGUNO;
                 activaTodo();
                 return;
             }
         }
         if (!setBloqueo(dtAdd, "albrutacab", alr_numeE.getText()))
         {
             msgBox(msgBloqueo);
             nav.pulsado = navegador.NINGUNO;
             activaTodo();
             return;
         }
         String s = "SELECT * FROM albrutacab WHERE alr_nume = " + alr_numeE.getValorInt();
         if (!dtAdd.select(s, true))
         {
             mensajeErr("Registro ha sido borrado");
             resetBloqueo(dtAdd, "albrutacab", alr_numeE.getText(), true);
             activaTodo();
             mensaje("");
             return;
         }

     }
     catch (SQLException | UnknownHostException k)
     {
       Error("Error al bloquear el registro", k);
       return;
     }
     Baceptar.setEnabled(true);
     Bcancelar.setEnabled(true);
     Bcancelar.requestFocus();
     mensaje("BORRANDO Registro Activo ...");
   }
    @Override
   public void ej_edit1()
   {
     try
     {
         if (!checkCabecera())
             return;
         
         int nl = jt.getRowCount();
         int orden = 0;
         if (! ARG_MODSALA)
         {
             if (!ordenarC.isSelected())
             {
                jt.salirGrid();
                if (cambiaLinJT(jt.getSelectedRow()) >= 0)
                    return;
             }

             for (int n = 0; n < nl; n++)
             {
                 if (jt.getValorInt(n, JT_NUMALB) == 0)
                     continue;

                 orden++;
             }
             jtFra.salirGrid();
             if (checkLineaFra(jtFra.getSelectedRow()) >= 0)
             {
                 Tpanel1.setSelectedIndex(1);
                 return;
             }
             nl=jtFra.getRowCount();
             for (int n = 0; n < nl; n++)
             {
                 if (jtFra.getValorInt(n, JTFR_NUME) == 0)
                     continue;

                 orden++;
             }
             if (orden == 0)
             {
                 msgBox("Introduzca algun albaran O factura para la ruta");
                 return;
             }
             if (! checkFraDeAlbaran())
                return;
         }
         dtAdd.edit();
         guardaCab(alr_numeE.getValorInt());
         // borro lineas e inserto las nuevas
         
         if (! ARG_MODSALA)
         {
             String s = "delete from albrutalin where alr_nume=" + alr_numeE.getValorInt();
             dtAdd.executeUpdate(s);
             s="delete from cobrosruta where alr_nume=" + alr_numeE.getValorInt();
             dtAdd.executeUpdate(s);
             orden = 1;
             nl = jt.getRowCount();
             for (int n = 0; n < nl; n++)
             {
                 if (jt.getValorInt(n, JT_NUMALB) == 0)
                     continue;
                 guardaLineas(alr_numeE.getValorInt(), orden, n);
                 orden++;
             }
             insLinFac(alr_numeE.getValorInt());
         }
         dtAdd.commit();
         mensajeErr("Albaranes de ruta.. Modificados");
         resetBloqueo(dtAdd, "albrutacab", alr_numeE.getText(), false);
         ctUp.commit();
     }
     catch (ParseException | SQLException ex)
     {
       Error("Error al Modificar datos", ex);
       return;
     }
     mensaje("");
     mensajeErr("Datos ... Modificados");
     activaTodo();
     verDatos();
   }
    @Override
   public void canc_edit()
   {
     mensaje("");
     try
     {
       resetBloqueo(dtAdd, "albrutacab",alr_numeE.getText(), true);
     }
     catch (Exception ex)
     {
       Error("Error al Quitar Bloqueo", ex);
       return;
     }

     mensajeErr("Modificacion de Datos Cancelada");
     activaTodo();
     verDatos();
   }
    @Override
  public void canc_delete()
  {
        mensaje("");
        activaTodo();
        try
        {
            resetBloqueo(dtAdd, "albrutacab", alr_numeE.getText(), true);
        } catch (Exception k)
        {
            Error("Error al Anular bloqueo", k);
        }
        mensajeErr("Borrado de Datos Cancelada");
        verDatos();
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

        emp_codiE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#9");
        avc_anoE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        avc_serieE = new gnu.chu.controles.CTextField(Types.CHAR,"X");
        avc_numeE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#####9");
        cli_codiE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#####9");
        cli_nombE = new gnu.chu.controles.CTextField(Types.CHAR,"X",40);
        avc_unidE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        avc_kilosE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##,##9.99");
        alr_repetE = new gnu.chu.controles.CCheckBox();
        fvc_serieE = new gnu.chu.controles.CTextField(Types.CHAR,"X",1);
        fvc_numeE = new gnu.chu.controles.CTextField(Types.DECIMAL,"####9");
        fvc_anoE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        fvc_empcodE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#9");
        fvc_clicodE = new gnu.chu.controles.CTextField();
        fvc_clinomE = new gnu.chu.controles.CTextField();
        fvc_fecfraE = new gnu.chu.controles.CTextField();
        fvc_sumtotE = new gnu.chu.controles.CTextField();
        fvc_imppenE = new gnu.chu.controles.CTextField();
        alr_bultosE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        alr_paletsE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        alr_horrepE = new gnu.chu.controles.CTextField(Types.CHAR,"X",50);
        alr_comrepE = new gnu.chu.controles.CTextField(Types.CHAR,"X",80);
        cli_pobleE = new gnu.chu.controles.CTextField(Types.CHAR,"X",50);
        BimprEti = new javax.swing.JMenuItem();
        cli_direeE = new gnu.chu.controles.CTextField(Types.CHAR,"X",100);
        cli_codpoeE = new gnu.chu.controles.CTextField(Types.CHAR,"X",8);
        cli_codrutE = new gnu.chu.controles.CTextField(Types.CHAR,"X",2);
        Pprinc = new gnu.chu.controles.CPanel();
        Pcabe = new gnu.chu.controles.CPanel();
        cLabel5 = new gnu.chu.controles.CLabel();
        alr_fechaE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel6 = new gnu.chu.controles.CLabel();
        alr_fecsalE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        alr_fecsalH = new gnu.chu.controles.CTextField(Types.DECIMAL,"99");
        alr_fecsalM = new gnu.chu.controles.CTextField(Types.DECIMAL,"99");
        cLabel1 = new gnu.chu.controles.CLabel();
        cLabel7 = new gnu.chu.controles.CLabel();
        alr_fecregE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        alr_fecregH = new gnu.chu.controles.CTextField(Types.DECIMAL,"99");
        alr_fecregM = new gnu.chu.controles.CTextField(Types.DECIMAL,"99");
        cLabel2 = new gnu.chu.controles.CLabel();
        cLabel3 = new gnu.chu.controles.CLabel();
        rut_codiE = new gnu.chu.controles.CLinkBox();
        cLabel4 = new gnu.chu.controles.CLabel();
        cLabel8 = new gnu.chu.controles.CLabel();
        alr_vekminE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#,###,##9");
        cLabel9 = new gnu.chu.controles.CLabel();
        alr_vekmfiE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#,###,##9");
        jScrollPane2 = new javax.swing.JScrollPane();
        alr_comentE = new gnu.chu.controles.CTextArea();
        cLabel11 = new gnu.chu.controles.CLabel();
        veh_codiE = new gnu.chu.controles.CLinkBox();
        cLabel15 = new gnu.chu.controles.CLabel();
        alr_numeE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###,##9");
        BirGrid = new gnu.chu.controles.CButton();
        tra_codiE = new gnu.chu.controles.CComboBox();
        BInsAuto = new gnu.chu.controles.CButtonMenu(Iconos.getImageIcon("fill"));
        PPie = new gnu.chu.controles.CPanel();
        numAlbE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        kilosTotE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###,##9.9");
        Baceptar = new gnu.chu.controles.CButton();
        Bcancelar = new gnu.chu.controles.CButton();
        cLabel16 = new gnu.chu.controles.CLabel();
        cLabel17 = new gnu.chu.controles.CLabel();
        cLabel18 = new gnu.chu.controles.CLabel();
        numFrasE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        cLabel19 = new gnu.chu.controles.CLabel();
        impFrasE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--,---,--9.99");
        cLabel13 = new gnu.chu.controles.CLabel();
        bulTotE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        cLabel14 = new gnu.chu.controles.CLabel();
        palTotE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        Bimpri = new gnu.chu.controles.CButtonMenu(Iconos.getImageIcon("print"));
        Borden = new gnu.chu.controles.CButton(Iconos.getImageIcon("order"));
        ordenarC = new gnu.chu.controles.CCheckBox();
        Tpanel1 = new gnu.chu.controles.CTabbedPane();
        jt = new gnu.chu.controles.CGridEditable(17){
            @Override
            public int cambiaLinea(int row, int col)
            {
                return cambiaLinJT(row);
            }
        };
        jtFra = new gnu.chu.controles.CGridEditable(9)
        {
            public void afterCambiaLinea()
            {
                calcSumFras();
            }

            public void cambiaColumna(int col,int colNueva, int row)
            {
                try
                {
                    if (col != 3)
                    return;

                }
                catch (Exception k)
                {
                    Error("Error al Cambiar Columna", k);
                }
            }
            public int cambiaLinea(int row, int col)
            {
                try
                {
                    return checkLineaFra(row);
                }
                catch (SQLException k)
                {
                    Error("Error al Cambiar Columna", k);
                }
                return 0;

            }
        }
        ;

        emp_codiE.setText("1");

        avc_anoE.setValorDec(EU.ejercicio);

        avc_serieE.setMayusc(true);
        avc_serieE.setAutoNext(true);
        avc_serieE.setText("A");

        cli_codiE.setEnabled(false);

        avc_unidE.setEnabled(false);

        avc_kilosE.setEnabled(false);

        alr_repetE.setEnabled(false);

        fvc_serieE.setText("1");

        fvc_empcodE.setText("1");

        fvc_clicodE.setEnabled(false);

        fvc_clinomE.setEnabled(false);

        fvc_fecfraE.setEnabled(false);

        fvc_sumtotE.setEnabled(false);

        fvc_imppenE.setEnabled(false);

        alr_bultosE.setText("1");

        alr_paletsE.setText("1");

        BimprEti.setText("Etiqueta ");
        BimprEti.setToolTipText("Imprimir Etiqueta Envio");

        cli_codrutE.setText("A");
        cli_codrutE.setEnabled(false);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Pprinc.setLayout(new java.awt.GridBagLayout());

        Pcabe.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Pcabe.setMaximumSize(new java.awt.Dimension(560, 130));
        Pcabe.setMinimumSize(new java.awt.Dimension(560, 130));
        Pcabe.setPreferredSize(new java.awt.Dimension(560, 130));
        Pcabe.setLayout(null);

        cLabel5.setText("Fecha");
        cLabel5.setPreferredSize(new java.awt.Dimension(52, 18));
        Pcabe.add(cLabel5);
        cLabel5.setBounds(2, 2, 40, 17);

        alr_fechaE.setPreferredSize(new java.awt.Dimension(10, 18));
        Pcabe.add(alr_fechaE);
        alr_fechaE.setBounds(50, 2, 76, 17);
        alr_fechaE.getAccessibleContext().setAccessibleName("");

        cLabel6.setText("Salida Ruta");
        cLabel6.setPreferredSize(new java.awt.Dimension(52, 18));
        Pcabe.add(cLabel6);
        cLabel6.setBounds(2, 23, 70, 17);

        alr_fecsalE.setPreferredSize(new java.awt.Dimension(10, 18));
        Pcabe.add(alr_fecsalE);
        alr_fecsalE.setBounds(80, 23, 70, 17);

        alr_fecsalH.setText("0");
        Pcabe.add(alr_fecsalH);
        alr_fecsalH.setBounds(160, 23, 20, 17);

        alr_fecsalM.setText("0");
        Pcabe.add(alr_fecsalM);
        alr_fecsalM.setBounds(190, 23, 20, 17);

        cLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cLabel1.setText(":");
        Pcabe.add(cLabel1);
        cLabel1.setBounds(180, 23, 10, 17);

        cLabel7.setText("Operario");
        cLabel7.setPreferredSize(new java.awt.Dimension(52, 18));
        Pcabe.add(cLabel7);
        cLabel7.setBounds(230, 2, 60, 17);

        alr_fecregE.setPreferredSize(new java.awt.Dimension(10, 18));
        Pcabe.add(alr_fecregE);
        alr_fecregE.setBounds(80, 45, 76, 17);

        alr_fecregH.setText("0");
        Pcabe.add(alr_fecregH);
        alr_fecregH.setBounds(160, 45, 20, 17);

        alr_fecregM.setText("0");
        Pcabe.add(alr_fecregM);
        alr_fecregM.setBounds(190, 45, 20, 17);

        cLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cLabel2.setText(":");
        Pcabe.add(cLabel2);
        cLabel2.setBounds(180, 23, 10, 17);

        cLabel3.setText("Ruta");
        Pcabe.add(cLabel3);
        cLabel3.setBounds(230, 23, 40, 17);

        rut_codiE.setAncTexto(30);
        rut_codiE.setMayusculas(true);
        rut_codiE.setFormato(Types.CHAR, "XX");
        Pcabe.add(rut_codiE);
        rut_codiE.setBounds(290, 23, 210, 17);
        rut_codiE.getAccessibleContext().setAccessibleName("");

        cLabel4.setText("Vehiculo");
        Pcabe.add(cLabel4);
        cLabel4.setBounds(230, 45, 60, 17);

        cLabel8.setText("Km. Iniciales ");
        Pcabe.add(cLabel8);
        cLabel8.setBounds(10, 67, 80, 17);
        Pcabe.add(alr_vekminE);
        alr_vekminE.setBounds(100, 67, 70, 17);

        cLabel9.setText("Identificador");
        Pcabe.add(cLabel9);
        cLabel9.setBounds(410, 67, 80, 17);
        Pcabe.add(alr_vekmfiE);
        alr_vekmfiE.setBounds(270, 67, 70, 17);

        alr_comentE.setColumns(20);
        alr_comentE.setRows(5);
        jScrollPane2.setViewportView(alr_comentE);

        Pcabe.add(jScrollPane2);
        jScrollPane2.setBounds(10, 87, 540, 40);

        cLabel11.setText("Regreso Ruta");
        cLabel11.setPreferredSize(new java.awt.Dimension(52, 18));
        Pcabe.add(cLabel11);
        cLabel11.setBounds(2, 45, 80, 17);

        veh_codiE.setAncTexto(30);
        veh_codiE.setFormato(Types.DECIMAL, "##9");
        Pcabe.add(veh_codiE);
        veh_codiE.setBounds(290, 45, 210, 17);
        veh_codiE.getAccessibleContext().setAccessibleName("");

        cLabel15.setText("Km. Finales");
        Pcabe.add(cLabel15);
        cLabel15.setBounds(190, 67, 70, 17);
        Pcabe.add(alr_numeE);
        alr_numeE.setBounds(500, 67, 50, 17);
        Pcabe.add(BirGrid);
        BirGrid.setBounds(540, 120, 2, 2);
        Pcabe.add(tra_codiE);
        tra_codiE.setBounds(290, 2, 210, 17);

        BInsAuto.addMenu("Pedidos", "P");
        BInsAuto.addMenu("Albaranes", "A");
        Pcabe.add(BInsAuto);
        BInsAuto.setBounds(502, 20, 50, 26);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        Pprinc.add(Pcabe, gridBagConstraints);

        PPie.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        PPie.setMaximumSize(new java.awt.Dimension(549, 49));
        PPie.setMinimumSize(new java.awt.Dimension(549, 49));
        PPie.setName(""); // NOI18N
        PPie.setPreferredSize(new java.awt.Dimension(549, 49));
        PPie.setLayout(null);

        numAlbE.setEditable(false);
        PPie.add(numAlbE);
        numAlbE.setBounds(45, 2, 40, 17);

        kilosTotE.setEditable(false);
        PPie.add(kilosTotE);
        kilosTotE.setBounds(130, 2, 50, 17);
        PPie.add(Baceptar);
        Baceptar.setBounds(350, 2, 90, 28);
        PPie.add(Bcancelar);
        Bcancelar.setBounds(445, 2, 95, 28);

        cLabel16.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cLabel16.setText("Importe");
        PPie.add(cLabel16);
        cLabel16.setBounds(130, 22, 50, 17);

        cLabel17.setText("Nº Fras");
        PPie.add(cLabel17);
        cLabel17.setBounds(0, 22, 50, 17);

        cLabel18.setText("Nº Alb.");
        PPie.add(cLabel18);
        cLabel18.setBounds(0, 2, 40, 17);

        numFrasE.setEditable(false);
        PPie.add(numFrasE);
        numFrasE.setBounds(45, 20, 40, 17);

        cLabel19.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cLabel19.setText("Kilos");
        PPie.add(cLabel19);
        cLabel19.setBounds(90, 2, 40, 17);

        impFrasE.setEditable(false);
        PPie.add(impFrasE);
        impFrasE.setBounds(180, 20, 70, 17);

        cLabel13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cLabel13.setText("Bultos");
        PPie.add(cLabel13);
        cLabel13.setBounds(190, 2, 40, 17);

        bulTotE.setEditable(false);
        PPie.add(bulTotE);
        bulTotE.setBounds(230, 2, 30, 17);

        cLabel14.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cLabel14.setText("Palets");
        PPie.add(cLabel14);
        cLabel14.setBounds(260, 2, 40, 17);

        palTotE.setEditable(false);
        PPie.add(palTotE);
        palTotE.setBounds(300, 2, 30, 17);

        Bimpri.addMenu("Albaranes","A");
        Bimpri.addMenu("Facturas","F");
        Bimpri.addMenu("Det.Albar","D");
        PPie.add(Bimpri);
        Bimpri.setBounds(253, 25, 70, 24);

        Borden.setText("Orden");
        Borden.setToolTipText("Ordenar por Num. Albaran");
        PPie.add(Borden);
        Borden.setBounds(330, 30, 60, 18);

        ordenarC.setText("Modo Ordenar");
        ordenarC.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        PPie.add(ordenarC);
        ordenarC.setBounds(440, 30, 100, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        Pprinc.add(PPie, gridBagConstraints);

        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jt.setMaximumSize(new java.awt.Dimension(559, 149));
        jt.setMinimumSize(new java.awt.Dimension(559, 149));
        ArrayList v=new ArrayList();
        v.add("Em"); // 0
        v.add("Ejer."); // 1
        v.add("Se"); // 2
        v.add("Num."); // 3
        v.add("Btos"); // 4
        v.add("Plet"); // 5
        v.add("Cliente"); // 6
        v.add("Nombre Cliente"); // 7
        v.add("C.Rut"); // 8
        v.add("Direccion"); // 9
        v.add("C.P"); // 10
        v.add("Poblacion"); // 11
        v.add("Unid."); // 12
        v.add("Kilos"); // 13
        v.add("Horario"); // 14
        v.add("Coment."); // 15
        v.add("Dupl."); // 16
        jt.setCabecera(v);
        jt.setColNueva(3);
        jt.setAnchoColumna(new int[]{20,40,20,40,30,30,40,200,40,120,40,90,40,50,100,120,45});
        jt.setAlinearColumna(new int[]{2,2,1,2,2,2,2,0,0,0,0,0,2,2,0,0,1});

        ArrayList vc=new ArrayList();
        vc.add(emp_codiE);
        vc.add(avc_anoE);
        vc.add(avc_serieE);
        vc.add(avc_numeE);
        vc.add(alr_bultosE);
        vc.add(alr_paletsE);
        vc.add(cli_codiE);
        vc.add(cli_nombE);
        vc.add(cli_codrutE);
        vc.add(cli_direeE);
        vc.add(cli_codpoeE);
        vc.add(cli_pobleE);
        vc.add(avc_unidE);
        vc.add(avc_kilosE);
        vc.add(alr_horrepE);
        vc.add(alr_comrepE);
        vc.add(alr_repetE);
        try {
            jt.setCampos(vc);
        } catch (Exception k)
        {
            Error("Error al poner campos en grid ",k);
            return;
        }

        jt.setFormatoCampos();
        jt.setFormatoColumna(JT_REPET, "B-");
        jt.setToolTipHeader(0, "Empresa");
        jt.setToolTipHeader(1, "Ejercicio");
        jt.setToolTipHeader(2, "Serie");
        jt.setToolTipHeader(3, "Numero Albaran");
        jt.setToolTipHeader(JT_BULTOS, "Bultos");
        jt.setToolTipHeader(JT_BULTOS, "Bultos");
        jt.setToolTipHeader(JT_PALETS, "Palets");
        jt.getPopMenu().add(BimprEti);
        Tpanel1.addTab("Albaranes", jt);

        ArrayList vf= new ArrayList();
        vf.add("Año"); // 0
        vf.add("Emp"); // 1
        vf.add("S"); // 2  serie
        vf.add("Factura");  // 3
        vf.add("Cliente"); // 4
        vf.add("Nombre"); // 5
        vf.add("Fec.Fra");// 6
        vf.add("Imp.Fra"); // 7
        vf.add("Imp.Pend"); // 8
        jtFra.setCabecera(vf);
        fvc_clicodE.setEnabled(false);
        fvc_clinomE.setEnabled(false);
        fvc_fecfraE.setEnabled(false);
        fvc_sumtotE.setEnabled(false);
        fvc_imppenE.setEnabled(false);
        fvc_serieE.setMayusc(true);
        fvc_serieE.setAutoNext(true);
        fvc_clicodE.setText("");
        fvc_sumtotE.setText("");
        fvc_serieE.setText("1");
        fvc_empcodE.setValorDec(EU.em_cod);
        fvc_anoE.setValorDec(EU.ejercicio);
        try {
            ArrayList vf1=new ArrayList();
            vf1.add(fvc_anoE);
            vf1.add(fvc_empcodE);
            vf1.add(fvc_serieE);
            vf1.add(fvc_numeE);
            vf1.add(fvc_clicodE);
            vf1.add(fvc_clinomE);
            vf1.add(fvc_fecfraE);
            vf1.add(fvc_sumtotE);
            vf1.add(fvc_imppenE);
            jtFra.setCampos(vf1);
        } catch (Exception k)
        {
            Error("Error al configurar grid fras.",k);

            return;
        }

        jtFra.setColNueva(3);
        jtFra.setMaximumSize(new Dimension(406, 311));
        jtFra.setMinimumSize(new Dimension(406, 311));
        jtFra.setPreferredSize(new Dimension(406, 311));
        jtFra.setAnchoColumna(new int[]{50,40,25,60,40,140,80,70,70});
        jtFra.setAlinearColumna(new int[]{2,2,1,2,2,0,1,2,2});
        jtFra.setFormatoColumna(7,"----,--9.99");
        jtFra.setFormatoColumna(8,"----,--9.99");
        Tpanel1.addTab("Facturas", jtFra);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Pprinc.add(Tpanel1, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButtonMenu BInsAuto;
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CButton Bcancelar;
    private javax.swing.JMenuItem BimprEti;
    private gnu.chu.controles.CButtonMenu Bimpri;
    private gnu.chu.controles.CButton BirGrid;
    private gnu.chu.controles.CButton Borden;
    private gnu.chu.controles.CPanel PPie;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CTabbedPane Tpanel1;
    private gnu.chu.controles.CTextField alr_bultosE;
    private gnu.chu.controles.CTextArea alr_comentE;
    private gnu.chu.controles.CTextField alr_comrepE;
    private gnu.chu.controles.CTextField alr_fechaE;
    private gnu.chu.controles.CTextField alr_fecregE;
    private gnu.chu.controles.CTextField alr_fecregH;
    private gnu.chu.controles.CTextField alr_fecregM;
    private gnu.chu.controles.CTextField alr_fecsalE;
    private gnu.chu.controles.CTextField alr_fecsalH;
    private gnu.chu.controles.CTextField alr_fecsalM;
    private gnu.chu.controles.CTextField alr_horrepE;
    private gnu.chu.controles.CTextField alr_numeE;
    private gnu.chu.controles.CTextField alr_paletsE;
    private gnu.chu.controles.CCheckBox alr_repetE;
    private gnu.chu.controles.CTextField alr_vekmfiE;
    private gnu.chu.controles.CTextField alr_vekminE;
    private gnu.chu.controles.CTextField avc_anoE;
    private gnu.chu.controles.CTextField avc_kilosE;
    private gnu.chu.controles.CTextField avc_numeE;
    private gnu.chu.controles.CTextField avc_serieE;
    private gnu.chu.controles.CTextField avc_unidE;
    private gnu.chu.controles.CTextField bulTotE;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel11;
    private gnu.chu.controles.CLabel cLabel13;
    private gnu.chu.controles.CLabel cLabel14;
    private gnu.chu.controles.CLabel cLabel15;
    private gnu.chu.controles.CLabel cLabel16;
    private gnu.chu.controles.CLabel cLabel17;
    private gnu.chu.controles.CLabel cLabel18;
    private gnu.chu.controles.CLabel cLabel19;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.controles.CLabel cLabel9;
    private gnu.chu.controles.CTextField cli_codiE;
    private gnu.chu.controles.CTextField cli_codpoeE;
    private gnu.chu.controles.CTextField cli_codrutE;
    private gnu.chu.controles.CTextField cli_direeE;
    private gnu.chu.controles.CTextField cli_nombE;
    private gnu.chu.controles.CTextField cli_pobleE;
    private gnu.chu.controles.CTextField emp_codiE;
    private gnu.chu.controles.CTextField fvc_anoE;
    private gnu.chu.controles.CTextField fvc_clicodE;
    private gnu.chu.controles.CTextField fvc_clinomE;
    private gnu.chu.controles.CTextField fvc_empcodE;
    private gnu.chu.controles.CTextField fvc_fecfraE;
    private gnu.chu.controles.CTextField fvc_imppenE;
    private gnu.chu.controles.CTextField fvc_numeE;
    private gnu.chu.controles.CTextField fvc_serieE;
    private gnu.chu.controles.CTextField fvc_sumtotE;
    private gnu.chu.controles.CTextField impFrasE;
    private javax.swing.JScrollPane jScrollPane2;
    private gnu.chu.controles.CGridEditable jt;
    private gnu.chu.controles.CGridEditable jtFra;
    private gnu.chu.controles.CTextField kilosTotE;
    private gnu.chu.controles.CTextField numAlbE;
    private gnu.chu.controles.CTextField numFrasE;
    private gnu.chu.controles.CCheckBox ordenarC;
    private gnu.chu.controles.CTextField palTotE;
    private gnu.chu.controles.CLinkBox rut_codiE;
    private gnu.chu.controles.CComboBox tra_codiE;
    private gnu.chu.controles.CLinkBox veh_codiE;
    // End of variables declaration//GEN-END:variables
   @Override
    public void iniciarVentana() throws Exception {
        Pcabe.setAltButton(BirGrid);
        pdconfig.llenaDiscr(dtStat, rut_codiE, pdconfig.D_RUTAS ,EU.em_cod);
        String s="select tra_codi,tra_nomb from v_tranpvent "+
            (ARG_MODSALA?" where tra_codi ='"+EU.usuario+"'":"")+            
            " order by tra_codi";
        if (dtCon1.select(s))
            tra_codiE.addItem(dtCon1);
        tra_codiE.addItem("Externo","Externo");
        s="select veh_codi,veh_nomb from vehiculos order by veh_nomb";
        if (dtCon1.select(s))
            veh_codiE.addDatos(dtCon1);
        if (ARG_MODSALA)
            tra_codiE.setEnabled(false);
        alr_numeE.setColumnaAlias("alr_nume");
        alr_fechaE.setColumnaAlias("alr_fecha");
        rut_codiE.setColumnaAlias("rut_codi");
        tra_codiE.setColumnaAlias("usu_nomb");
        veh_codiE.setColumnaAlias("veh_codi");
        alr_fecsalE.setColumnaAlias("alr_fecsal");
        alr_fecregE.setColumnaAlias("alr_fecreg");
        alr_vekminE.setColumnaAlias("alr_vekmin");
        alr_vekmfiE.setColumnaAlias("alr_vekmfi");
        jt.setDragEnabled(false); 
        Pcabe.setDefButton(Baceptar);
        jt.setDefButton(Baceptar);
        jtFra.setDefButton(Baceptar);
        activarEventos();
        if (! ARG_MODSALA && ! dtCons.getNOREG() )
                dtCons.last();

        verDatos();
    }
    
    void activarEventos()
    {
        ordenarC.addActionListener(new ActionListener()
         {
              @Override
              public void actionPerformed(ActionEvent e)
              {
                  if (ordenarC.isSelected())
                  {
                    jt.salirGrid();
                    if (cambiaLinJT(jt.getSelectedRow())>=0)
                    {
                        ordenarC.setSelected(false);
                        jt.requestFocusLater();
                        return;
                    }
                    jt.setEnabled(false);              
                  }
                  else
                  {
                     jt.setEnabled(true); 
                     jt.requestFocusInicioLater();                   
                  }
                  jt.setDragEnabled(ordenarC.isSelected());      
                
              }
         });
         jt.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
              if (! jt.isEnabled() && nav.pulsado==navegador.ADDNEW)
              {
                  if (! checkIrGrid())
                        return;
                  jt.setEnabled(true);
                  jtFra.setEnabled(true);
                  jt.requestFocusInicioLater();
                  return;
              }
              if (e.getClickCount()<2 || jt.isEnabled() || jt.isVacio()) 
                  return;
              verDocumento();
            }
         });
         jtFra.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
              if (! jtFra.isEnabled() && nav.pulsado==navegador.ADDNEW)
              {
                  if (! checkIrGrid())
                        return;
                  jt.setEnabled(true);
                  jtFra.setEnabled(true);
                  jtFra.requestFocusInicioLater();
              }         
            }
         });
         jt.addGridListener(new GridAdapter()
         {
              @Override
              public void cambioColumna(GridEvent event)   {
                  if (jt.isEnabled() && event.getColumna()==JT_NUMALB)
                     getDatosAlb(event.getLinea());
              }
         });
         Borden.addActionListener(new ActionListener()
         {
              @Override
              public void actionPerformed(ActionEvent e)
              {
                 swOrdenado= !swOrdenado;
                 verDatos();
              }
         });
         BInsAuto.addActionListener(new ActionListener()
         {
              @Override
              public void actionPerformed(ActionEvent e)
              {
                  if (e.getActionCommand().startsWith("Alb"))                      
                    insertarAutoAlb();
                  else
                    insertarAutoPed();                  
                    
              }
         });
         BirGrid.addActionListener(new ActionListener()
         {
              @Override
              public void actionPerformed(ActionEvent e)
              {
                  if (! jt.isEnabled())
                  {
                    jt.setEnabled(true);
                    jtFra.setEnabled(true);
                    if (! checkIrGrid())
                        return;
                    jt.requestFocusInicioLater();
                  }
              }
         });
         BimprEti.addActionListener(new ActionListener()
         {
              @Override
              public void actionPerformed(ActionEvent e)
              {
                  if (jt.isVacio())
                      return;
                
                  String res=mensajes.mensajeGetTexto("Numero Etiquetas", "Imprimir etiqueta",ManAlbRuta.this, 
                      jt.getValString(jt.getSelectedRowDisab(),JT_BULTOS));
                  int numEti=0;
                  if (res==null)
                      return;
                  try {
                    numEti=Integer.parseInt(res.trim());
                  } catch (NumberFormatException ex){ 
                      msgBox("Introduzca un numero valido");
                      return; 
                  }
                  if (numEti<=0 || numEti>=99)
                  {
                      msgBox("Numero de etiquetas NO valido");
                      return;
                  }
                  imprEtiqueta(alr_numeE.getValorInt(),jt.getSelectedRowDisab()+1,numEti);
              }
         });
         Bimpri.addActionListener(new ActionListener()
         {
              @Override
              public void actionPerformed(ActionEvent e)
              {
                  String accion=Bimpri.getValor(e.getActionCommand());
                  if (accion.equals("F"))
                      imprimir_facturas();
                  if (accion.equals("A"))
                      imprimir_albaranes();
                  if (accion.equals("D"))
                      imprimir_albaranes_ext();
              }
         });
    }
    void imprEtiqueta(int alrNume,int alrOrden, int numCopias)
    {
        try
        {
            String s="select r.*,cl.cli_codrut from v_albruta as r,"
                + " v_albavec as a,v_cliente as cl where alr_nume="+alrNume+
                " and alr_orden="+alrOrden+
                " and r.avc_nume= a.avc_nume "+
                " and r.avc_ano=a.avc_ano "+
                " and r.avc_serie = a.avc_serie "+
                " and r.emp_codi = a.emp_codi "+
                " and a.cli_codi = cl.cli_codi";                
            if (!dtStat.select(s))
            {
                msgBox("No encontrado salida a ruta para este  Albaran");
                return;
            }
            
            if (jr==null)
                jr = Listados.getJasperReport(EU,"etiqDireccion");
            
            java.util.HashMap mp = new java.util.HashMap();
            
            mp.put("logotipo",Iconos.getPathIcon()+LOGOTIPO);
            mp.put("cli_codrut",dtStat.getString("cli_codrut"));
            mp.put("documento",dtStat.getString("emp_codi")+"-"+dtStat.getString("avc_ano")+
                dtStat.getString("avc_serie")+dtStat.getString("avc_nume"));
            mp.put("cli_nomen",dtStat.getString("cli_nomen"));
            mp.put("cli_nomen",dtStat.getString("cli_nomen"));
            mp.put("cli_diree",dtStat.getString("cli_diree"));
            mp.put("cli_poble",dtStat.getString("cli_poble"));
            mp.put("cli_codpoe",dtStat.getString("cli_codpoe"));
       
            ResourceBundle rsB=ResourceBundle.getBundle("gnu.chu.anjelica.locale.jasper",Locale.getDefault());
            mp.put(JRParameter.REPORT_LOCALE,Locale.getDefault());
            mp.put(JRParameter.REPORT_RESOURCE_BUNDLE,rsB);
            
            JasperPrint jp = JasperFillManager.fillReport(jr, mp, new JREmptyDataSource());
            if (EU.getSimulaPrint()) 
                return;
            gnu.chu.print.util.printJasper(jp, EU,numCopias);
        } catch (JRException  | SQLException  | PrinterException ex)
        {
            Error("Error al imprimir etiqueta",ex);
        }
    }
    boolean checkInsertAuto()
    {
        if (!jt.isVacio())
        {
            jt.salirGrid();
            if (cambiaLinJT(jt.getSelectedRow()) > 0)
                return false;
        }
        if (rut_codiE.isNull())
        {
            mensajeErr("Inserte primero la ruta");
            return false;
        }
        if (alr_fechaE.isNull())
        {
            mensajeErr("Inserte primero la fecha de registro");
            return false;
        }
        return true;
    }
    void insertarAutoPed()
    {
         try
        {
           String s="select c.cli_ruta,c.avc_fecalb,c.avc_id,c.emp_codi,c.avc_ano,c.avc_serie,c.avc_nume,"
                + "c.cli_codi,cl.cli_ordrut,c.avc_clinom,avc_kilos,avc_unid,cl.cli_nomen,cl.cli_diree,cl.cli_poble,"
                + "cl.cli_codpoe,cli_horenv,cli_comenv from v_albavec as c,"
                + "v_cliente as cl,v_pedruta as pr where c.cli_codi = cl.cli_codi "
               + " and pr.rut_codi = '"+rut_codiE.getText()+"'"
               + " and pr.prc_fecsal::date= '"+alr_fechaE.getFechaDB()+"'"
               + " and c.avc_nume = pr.avc_nume and c.avc_ano = pr.avc_ano and c.avc_serie = pr.avc_serie "               
               + " order by plr_orden";
           if (!dtCon1.select(s))
           {
               msgBox("No encontradas pedidos en ruta y fecha establecidos");
               return;
           }
           jt.setEnabled(false);
           do
           {
              if (checkLineaRepe(dtCon1.getInt("emp_codi"),dtCon1.getInt("avc_ano"),dtCon1.getString("avc_serie"),
                  dtCon1.getInt("avc_nume"),-1))
                    continue;
               addLineaAlb();
           } while (dtCon1.next());
           jt.setEnabled(!ordenarC.isSelected());
           jt.requestFocusFinalLater();
            msgBox("Albaranes de ruta cargados");
        } catch (ParseException | SQLException ex)
        {
            Error("Error al buscar albaranes para ruta",ex);
        }
    }
    void insertarAutoAlb()
    {
        try
        {
            if (!checkInsertAuto())
                return;
          
            String fecIni=Formatear.getFecha(Formatear.sumaDiasDate(alr_fechaE.getDate(),-7),"yyyy-MM-dd");
            String s="select c.cli_ruta,c.avc_fecalb,c.avc_id,c.emp_codi,c.avc_ano,c.avc_serie,c.avc_nume,"
                + "c.cli_codi,cl.cli_ordrut,c.avc_clinom,avc_kilos,avc_unid,cl.cli_nomen,cl.cli_diree,cl.cli_poble,"
                + "cl.cli_codpoe,cli_horenv,cli_comenv from v_albavec as c,"
                + "v_cliente as cl where c.cli_codi = cl.cli_codi and c.cli_ruta = '"+rut_codiE.getText()+"'"+
                " and c.avc_fecalb between '"+fecIni+"' and '"+alr_fechaE.getFechaDB()+"'"+
                " order by cli_ordrut,avc_fecalb";
            if (! dtCon1.select(s))
            {
                mensajeErr("NO hay ningun albaran para estos criterios");
                return;
            }
            jt.setEnabled(false);
            do
            {
                s="select * from albrutalin where avc_id = "+dtCon1.getInt("avc_id");
                if (dtStat.select(s))
                    continue;
               if (checkLineaRepe(dtCon1.getInt("emp_codi"),dtCon1.getInt("avc_ano"),dtCon1.getString("avc_serie"),
                  dtCon1.getInt("avc_nume"),-1))
                    continue;
                addLineaAlb();
              
            } while (dtCon1.next());
            jt.setEnabled(!ordenarC.isSelected());
            jt.requestFocusFinalLater();
            msgBox("Albaranes de ruta cargados");
        } catch (ParseException | SQLException ex)
        {
            Error("Error al buscar albaranes para ruta",ex);
        }
    }
    void addLineaAlb() throws SQLException
    {
        ArrayList a = new ArrayList();
        a.add(dtCon1.getString("emp_codi"));
        a.add(dtCon1.getString("avc_ano"));
        a.add(dtCon1.getString("avc_serie"));
        a.add(dtCon1.getString("avc_nume"));
        a.add("1");
        a.add("1"); // Palets
        a.add(dtCon1.getString("cli_codi"));
        a.add(dtCon1.getString("avc_clinom", true).equals("") ? dtCon1.getString("cli_nomen") : dtCon1.getString("avc_clinom", true));
        a.add(dtCon1.getString("cli_ruta"));
        a.add(dtCon1.getString("cli_diree"));
        a.add(dtCon1.getString("cli_codpoe"));
        a.add(dtCon1.getString("cli_poble"));
        a.add(dtCon1.getString("avc_unid"));
        a.add(dtCon1.getString("avc_kilos"));
        a.add(dtCon1.getString("cli_horenv"));
        a.add(dtCon1.getString("cli_comenv"));
        a.add(false);
        jt.addLinea(a);
    }
    private void verDocumento() {
        if (jf == null )
            return;
        msgEspere("Ejecutando consulta para visualizar Documento");
        new miThread("")
        {
            @Override
            public void run() {
                javax.swing.SwingUtilities.invokeLater(new Thread()
                {
                    @Override
                    public void run() {
                        ejecutable prog;
                        if ((prog = jf.gestor.getProceso(pdalbara.getNombreClase())) == null)
                            return;
                        pdalbara cm = (pdalbara) prog;
                        if (cm.inTransation())
                        {
                            msgBox("Mantenimiento Albaranes de Ventas ocupado. No se puede realizar la busqueda");
                            return;
                        }
                        cm.PADQuery();
                        cm.setEjercAlbaran(jt.getValorInt(jt.getSelectedRowDisab(), 1));
                        cm.setSerieAlbaran(jt.getValString(jt.getSelectedRowDisab(), 2));
                        cm.setNumeroAlbaran(jt.getValorInt(jt.getSelectedRowDisab(), JT_NUMALB));

                        cm.ej_query();
                        jf.gestor.ir(cm);
                        resetMsgEspere();
                    }
                });

            }
        };
    }
    
    @Override
    public void PADPrimero() {
           verDatos();
    }

    @Override
    public void PADAnterior() {
        verDatos();
    }

    @Override
    public void PADSiguiente() {
       verDatos();
    }

    @Override
    public void PADUltimo() {
        verDatos();
    }

    @Override
    public void ej_query1() {
      Component c = Pcabe.getErrorConf();
      if (c != null)
      {
        mensajeErr("Condiciones de Busqueda NO validas");
        c.requestFocus();
        return;
      }
      this.setEnabled(false);
      try
      {
      ArrayList v = new ArrayList();
      v.add(alr_numeE.getStrQuery());
      v.add(rut_codiE.getStrQuery());
      v.add(tra_codiE.getStrQuery());
      v.add(alr_fechaE.getStrQuery());
      v.add(alr_fecsalE.getStrQuery());
      v.add(alr_fecregE.getStrQuery());
      v.add(veh_codiE.getStrQuery());
      v.add(alr_vekminE.getStrQuery());
      v.add(alr_vekmfiE.getStrQuery());
      String s = "select * from albrutacab ";

      s = creaWhere(s, v,  true);
      s += " ORDER BY alr_fecha,alr_nume ";

      mensaje("Espere, por favor ... buscando datos");
      Pcabe.setQuery(false);      
      
//      debug("s: "+s);
      if (!dtCon1.select(s))
      {
        msgBox("No encontradas Albaranes de Rutas con estos criterios");
        rgSelect();
        verDatos();
        activaTodo();
        this.setEnabled(true);       
        return;
      }
      strSql = s;
      activaTodo();
      
      this.setEnabled(true);
      rgSelect();
      dtCons.last();
      nav.setEnabled(navegador.ULTIMO, false);
      nav.setEnabled(navegador.SIGUIENTE, false);
      verDatos();
      mensaje("");
      mensajeErr("Nuevas Condiciones ... Establecidas");
    }
    catch (Exception k)
    {
      Error("Error al buscar datos", k);
    }

    nav.pulsado = navegador.NINGUNO;
    }

    @Override
  public void canc_query()
  {
    Pcabe.setQuery(false);    
    activaTodo();
    verDatos();
    mensaje("");
    mensajeErr("Consulta ... Cancelada");
    nav.pulsado = navegador.NINGUNO;
  }

  

    @Override
    public void ej_addnew1()
    {        
        try
        {
            if (! checkCabecera())
                return;
            if (!ordenarC.isSelected())
            {
                jt.salirGrid();
                if (cambiaLinJT(jt.getSelectedRow())>=0)
                {
                    Tpanel1.setSelectedIndex(0);
                    return;
                }
            }
            int nl=jt.getRowCount();
            int orden=0;
            for (int n=0;n<nl;n++)
            {
                if (jt.getValorInt(n,JT_NUMALB)==0)
                    continue;
                
                orden++;
            }
           
            jtFra.salirGrid();
            if (checkLineaFra(jtFra.getSelectedRow())>=0)
            {
                Tpanel1.setSelectedIndex(1);
                return;
            }
            nl=jtFra.getRowCount();
            for (int n=0;n<nl;n++)
            {
                if (jtFra.getValorInt(n,JTFR_NUME)==0)
                    continue;
                
                orden++;
            }
            if (orden==0)
            {
                msgBox("Introduzca algun albaran O factura para la ruta");
                return;
            }
            if (! checkFraDeAlbaran())
                return;
            dtAdd.addNew("albrutacab", false);
            int id = guardaCab(0);
            
            orden=1;
            nl = jt.getRowCount();
            for (int n=0;n<nl;n++)
            {
                if (jt.getValorInt(n,JT_NUMALB)==0)
                    continue;
                guardaLineas(id,orden,n);
                orden++;
            }
            insLinFac(id);
            dtAdd.commit();
            mensajeErr("Albaranes de ruta.. guardados");
            activaTodo();
            strSql="SELECT * FROM albrutacab where alr_nume = "+id;
            rgSelect();
            verDatos();
            nav.pulsado=navegador.NINGUNO;
        } catch (SQLException | ParseException ex )
        {
            Error("Error al guardar cabecera de ruta", ex);
        }
    }
    /**
     * 
     * @return Devuelve Error. true si hay error. 
     * @throws SQLException 
     */
    private boolean checkFraDeAlbaran() throws SQLException
    {
        int nrows=jt.getRowCount();
        int nrowfr=jtFra.getRowCount();
        
        String s,msg="";
        for (int n=0;n<nrows;n++)
        {
            s="select pvc_incfra from pedvenc as p where avc_ano="+jt.getValorInt(n,1)+
                " and avc_serie = '"+jt.getValString(n,2)+"'"+
                " and avc_nume ="+jt.getValorInt(n,JT_NUMALB)+
                " and emp_codi = "+jt.getValorInt(n,0)+
                " and pvc_incfra = 'S'";
            if (dtStat.select(s))
            {
                int cliCodi=jt.getValorInt(n,JT_CLICOD);
                boolean swEnc=false;
                for (int nfr=0;nfr<nrows;nfr++)
                {
                    if (jtFra.getValorInt(nfr,JTFR_CLICOD)==cliCodi)
                    {
                        swEnc=true;
                        break;
                    }
                }
                if (!swEnc)
                    msg+="Cliente: "+jt.getValString(n,JT_CLINOMB)+" ... DEBERIA TENER FRAS\n";
            }
        }
        if (msg.equals(""))
            return true;
        int ret=mensajes.mensajeYesNo("Deberia haber Facturas", msg+"\n ¿Continuar?",this);
        return ret==mensajes.YES;
    }
    /**
     * Inserta las lineas de facturas mandadas a cobrar.
     * @param orden
     * @throws SQLException 
     */
    void insLinFac(int id) throws SQLException 
    {
        int nRow = jtFra.getRowCount();
        int orden = 1;
        for (int n = 0; n < nRow; n++)
        {
            if (jtFra.getValorInt(n, JTFR_NUME) == 0)
                continue;
            dtAdd.addNew("cobrosRuta", false);
            dtAdd.setDato("alr_nume", id);
            dtAdd.setDato("cru_orden", orden);
            dtAdd.setDato("avc_id", -1);
            dtAdd.setDato("fvc_id", PadFactur.getIdFactura(jtFra.getValorInt(n, JTFR_ANO),
                jtFra.getValorInt(n, JTFR_EMPCOD), jtFra.getValString(n, JTFR_SERIE),
                jtFra.getValorInt(n, JTFR_NUME), dtStat));
            dtAdd.setDato("cru_impdoc", jtFra.getValorDec(n, JTFR_IMPPEN));

            dtAdd.update(stUp);
            orden++;
        }

    }
    /**
     * Inserta o modifica cabecera de ruta
     * @param id si id=0 es addnew
     * @return
     * @throws SQLException
     * @throws ParseException 
     */
    int guardaCab(int id) throws SQLException,ParseException
    {
      
        dtAdd.setDato("rut_codi",rut_codiE.getText());
        dtAdd.setDato("usu_nomb",tra_codiE.getValor());
        dtAdd.setDato("alr_fecha",alr_fechaE.getDate());
        if (! alr_fecsalE.isNull())
            dtAdd.setDato("alr_fecsal","{ts '"+alr_fecsalE.getFecha("yyyy-MM-dd")+" "+
                alr_fecsalH.getText()+":"+alr_fecsalM.getText()+"'}");
        if (! alr_fecregE.isNull())
            dtAdd.setDato("alr_fecreg","{ts '"+alr_fecregE.getFecha("yyyy-MM-dd")+" "+
                alr_fecregH.getText()+":"+alr_fecregM.getText()+"'}");
        
        dtAdd.setDato("veh_codi",veh_codiE.getText());
        dtAdd.setDato("alr_vekmin",alr_vekminE.getValorDec());
        dtAdd.setDato("alr_vekmfi",alr_vekmfiE.getValorDec());
        dtAdd.setDato("alr_coment",alr_comentE.getText());
        dtAdd.update();
        if (id>0)
            return id;
        dtAdd.select("SELECT lastval()");
        return dtAdd.getInt(1);
    }
    void guardaLineas(int id,int orden,int nlGrid) throws SQLException
    {
        dtAdd.addNew("albrutalin");
        dtAdd.setDato("alr_nume",id);
        dtAdd.setDato("alr_orden",orden);
        dtAdd.setDato("avc_id",pdalbara.getIdAlbaran(dtStat,
            jt.getValorInt(nlGrid,1),jt.getValorInt(nlGrid,0),
            jt.getValString(nlGrid,2),jt.getValorInt(nlGrid,3)));
        dtAdd.setDato("alr_bultos",jt.getValorDec(nlGrid,JT_BULTOS));
        dtAdd.setDato("alr_palets",jt.getValorDec(nlGrid,JT_PALETS));
        dtAdd.setDato("alr_kilos",jt.getValorDec(nlGrid,JT_KILOS));
        dtAdd.setDato("alr_unid",jt.getValorDec(nlGrid,JT_UNID));
        dtAdd.setDato("alr_horrep",jt.getValString(nlGrid,JT_HORREP));
        dtAdd.setDato("alr_comrep",jt.getValString(nlGrid,JT_COMREP));
        dtAdd.setDato("cli_nomen",jt.getValString(nlGrid,JT_CLINOMB));
        dtAdd.setDato("cli_diree",jt.getValString(nlGrid,JT_CLIDIR));
        dtAdd.setDato("cli_codpoe",jt.getValString(nlGrid,JT_CLICP));
        dtAdd.setDato("cli_poble",jt.getValString(nlGrid,JT_CLIPOBL));
        dtAdd.setDato("alr_repet",jt.getValBoolean(nlGrid,JT_REPET)?-1:0);
       
        dtAdd.update();
    }
    boolean checkCabecera() throws ParseException
    {
        Component c;
        if ( (c=Pcabe.getErrorConf())!=null)
        {
            c.requestFocus();
            return false;
        }
        if (alr_fechaE.isNull())
        {
            mensajeErr("Introduzca fecha de Ruta");
            alr_fechaE.requestFocus();
            return false;
        }
        long nDias;
        if (alr_fechaE.hasCambio())
        {
            nDias = Formatear.comparaFechas(alr_fechaE.getDate(), Formatear.getDateAct());

            if (nDias < 0 )
            {
                mensajeErr("Fecha de Ruta no puede ser inferior a la actual");
                alr_fechaE.requestFocus();
                return false;
            }
            if (nDias > 3)
            {
                mensajeErr("Fecha de Ruta no puede ser superior en mas de 3 dias a la actual.");
                alr_fechaE.requestFocus();
                return false;
            }
        }
        if (alr_fecsalE.isNull() && alr_fecsalH.getValorInt()>0)
        {
            mensajeErr("Si introduce Hora salida. Introduzca el dia");
            alr_fecsalE.requestFocus();
            return false;
        }
         if (alr_fecregE.isNull() && alr_fecregH.getValorInt()>0)
        {
            mensajeErr("Si introduce Hora Regreso. Introduzca el dia");
            alr_fecregE.requestFocus();
            return false;
        }
        if (!rut_codiE.controla())
        {
            mensajeErr("Introduzca ruta");
            return false;
        }
        if (alr_comentE.getText().length()>250)
        {
            msgBox("Comentario no debe ser superior a 250 caracteres. Ha metido: "+alr_comentE.getText().length());
            alr_comentE.requestFocus();
            return false;
        }
//        if (usu_nombE.isNull())
//        {
//            mensajeErr("Usuario NO es valido");
//            usu_nombE.requestFocus();
//            return false;
//        }
       
        return true;
    }
    
    boolean  checkIrGrid()
    {
        if (nav.pulsado!=navegador.ADDNEW)
            return true;
        
        try{
            if (alr_fechaE.isNull() || alr_fechaE.getError())
            {
                mensajeErr("Fecha ruta no valida");
                alr_fechaE.requestFocus();
                return false;
            }
              if (alr_fecsalE.isNull() || alr_fecsalE.getError())
            {
                mensajeErr("Fecha Salida no valida");
                alr_fecsalE.requestFocus();
                return false;
            }
            if (rut_codiE.getText().equals(""))
            {
                mensajeErr("Introduzca ruta");
                rut_codiE.requestFocus();
                return false;
            }
            String s="select * from albrutacab where rut_codi = '"+rut_codiE.getText()+"'"+
                " and usu_nomb = '"+tra_codiE.getValor()+"'"+
                " and alr_fecha = '"+alr_fechaE.getFechaDB()+"'"+
                " and alr_fecsal = {ts '"+alr_fecsalE.getFecha("yyyy-MM-dd")+" "+
                alr_fecsalH.getText()+":"+alr_fecsalM.getText()+"'}";
            if (dtStat.select(s))
            {
                msgBox("Ruta ya existe");
                rut_codiE.requestFocus();
                return false;
            }
        } catch (Exception k)
        {
            Error("Error al buscar Salidas duplicadas",k);
        }
        return true;
    }
        
    @Override
    public void canc_addnew()
    {
    
      mensaje("");
      mensajeErr("Insercion ... CANCELADA");
      activaTodo();
      verDatos();
      
      nav.pulsado = navegador.NINGUNO;
    }
    
    @Override
    public void ej_delete1() {
        try
        {
            String s = "delete from albrutalin where alr_nume=" + alr_numeE.getValorInt();
            dtBloq.executeUpdate(s);
            s="delete from cobrosruta where alr_nume=" + alr_numeE.getValorInt();
            dtBloq.executeUpdate(s);
            dtAdd.delete(stUp);
            resetBloqueo(dtAdd, "albrutacab", alr_numeE.getText(), false);
            ctUp.commit();
            rgSelect();
        } catch (Exception ex)
        {
            Error("Error al borrar Registro", ex);
        }

        activaTodo();
        verDatos();
        mensaje("");
        mensajeErr("Registro ... Borrado");
    }
    @Override
    public void activar(boolean b) {
        activar(b,navegador.TODOS);
    }
    
    public void activar(boolean b,int opcion) {
        jt.setEnabled(b);
        BimprEti.setEnabled(!b);
        jtFra.setEnabled(b);
        Pcabe.setEnabled(b);
        Borden.setEnabled(!b);
        if (opcion!=navegador.QUERY && opcion!=navegador.DELETE)
        {
            ordenarC.setEnabled(b);
            alr_fecsalH.setEnabled(b);
            alr_fecregH.setEnabled(b);
            alr_fecsalM.setEnabled(b);
            alr_fecregM.setEnabled(b);
            BInsAuto.setEnabled(b);
        }
        alr_numeE.setEnabled(b);
               
        alr_fechaE.setEnabled(b);
        rut_codiE.setEnabled(b);        
      
        
        alr_comentE.setEnabled(b);
        Baceptar.setEnabled(b);
        Bcancelar.setEnabled(b);
    }
    void calcSumFras()
  {
    int nRow=jtFra.getRowCount();
    int nFras=0;
    double impFras = 0;

    for (int n=0;n<nRow;n++)
    {
      if (jtFra.getValorInt(n, 2) == 0)
        continue;
      nFras++;
      impFras+=jtFra.getValorDec(n,JTFR_IMPPEN);
    }
    numFrasE.setValorDec(nFras);
    impFrasE.setValorDec(impFras);
  }
    
  boolean buscaFac(int ejeNume,int empCodi,String serie,int numFra) throws SQLException
  {
    String s="SELECT f.*,cli_nomb FROM v_facvec as f,clientes as cl "+
       "  WHERE f.emp_codi = "+empCodi+
        " and f.fvc_ano = "+ejeNume+
        " and f.fvc_nume = "+numFra+
        " and f.fvc_serie = '"+serie+"'"+
        " and f.cli_codi = cl.cli_codi ";
    return dtStat.select(s);
  }
  /**
   * Comprueba si es valida una linea de la carga de facturas.
   * @param row
   * @return
   * @throws SQLException 
   */
  
  int checkLineaFra(int row) throws SQLException
  {
    if (! fvc_numeE.hasCambio())
        return -1;
    fvc_numeE.resetCambio();
    if (fvc_numeE.getValorInt() == 0)
        return -1;
    if (!buscaFac(fvc_anoE.getValorInt(), fvc_empcodE.getValorInt(),
                  fvc_serieE.getText(), fvc_numeE.getValorInt()))
    {
      mensajeErr("Factura NO ENCONTRADA");
      return 0;
    }
    if (dtStat.getDouble("fvc_cobrad")==-1)
    {
      msgBox("Factura YA esta cobrada");
//      return 0;
    }  
    jtFra.setValor(dtStat.getString("cli_codi"), row, 4);
    jtFra.setValor(dtStat.getString("cli_nomb"), row, 5);
    jtFra.setValor(dtStat.getFecha("fvc_fecfra", "dd-MM-yyyy"), row, 6);
    jtFra.setValor(dtStat.getString("fvc_sumtot"), row, 7);
    jtFra.setValor("" +
        (dtStat.getDouble("fvc_sumtot") - dtStat.getDouble("fvc_impcob")),
        row, 8);
    return -1;
  }
  void imprimir_facturas()
  {
      try
      {
          mensaje("Espere, por favor... Generando listado");
          if (dtCons.getNOREG())
          {
              mensajeErr("NO HAY REGISTROS ACTIVOS");
              return;
          }

          String s = "select f.*,c.cli_codi,c.cli_nomb,f.fvc_fecfra from v_cobruta f,clientes c "
              + " WHERE alr_nume = " + alr_numeE.getValorInt()
              + " and c.cli_codi = f.cli_codi "
              + " order by cru_orden";
          dtCon1.setStrSelect(s);
          ResultSet rs = ct.createStatement().executeQuery(dtCon1.getStrSelect());

          JasperReport jr = Listados.getJasperReport(EU, "cacobrea");
          java.util.HashMap mp = Listados.getHashMapDefault();
          mp.put("cor_fecha", alr_fechaE.getText());
          mp.put("usu_nomb", tra_codiE.getText());
          mp.put("zon_codi", rut_codiE.getText() + "-" + rut_codiE.getTextCombo());
          mp.put("cor_orden", alr_numeE.getText());

          JasperPrint jp = JasperFillManager.fillReport(jr, mp, new JRResultSetDataSource(rs));

//     JasperViewer.viewReport(jp,false);
          gnu.chu.print.util.printJasper(jp, EU);
          mensaje("");
          mensajeErr("Listado Generado");
      } catch (SQLException | JRException | PrinterException k)
      {
          Error("Error al generar Listado", k);
      }
  }
    void imprimir_albaranes() {
        try
        {
            mensaje("Espere, por favor... Generando listado");
            if (dtCons.getNOREG())
            {
                mensajeErr("NO HAY REGISTROS ACTIVOS");
                return;
            }

            String s = "select l.*,cl.cli_nomb,cl.cli_poble,cli_codrut from v_albruta as l "
                + " left join v_cliente as cl "
                + " on l.cli_codi = cl.cli_codi where alr_nume =" + dtCons.getInt("alr_nume")
                + " order by alr_orden";
            dtCon1.setStrSelect(s);
            ResultSet rs = ct.createStatement().executeQuery(dtCon1.getStrSelect());

            JasperReport jr = Listados.getJasperReport(EU, "relAlbRuta");
            java.util.HashMap mp = Listados.getHashMapDefault();
            mp.put("cor_fecha", alr_fechaE.getText());
            mp.put("usu_nomb", tra_codiE.getText());
            mp.put("zon_codi", rut_codiE.getText() + "-" + rut_codiE.getTextCombo());
            mp.put("cor_orden", alr_numeE.getText());

            JasperPrint jp = JasperFillManager.fillReport(jr, mp, new JRResultSetDataSource(rs));

//     JasperViewer.viewReport(jp,false);
            gnu.chu.print.util.printJasper(jp, EU);
            mensaje("");
            mensajeErr("Listado Generado");
        } catch (SQLException | JRException | PrinterException k)
        {
            Error("Error al generar Listado", k);
        }
    }

    void imprimir_albaranes_ext() {
        try
        {
            mensaje("Espere, por favor... Generando listado");
            if (dtCons.getNOREG())
            {
                mensajeErr("NO HAY REGISTROS ACTIVOS");
                return;
            }

            JasperReport jr = Listados.getJasperReport(EU, "relAlbRutaExt");
            java.util.HashMap mp = Listados.getHashMapDefault();
            mp.put("cor_fecha", alr_fechaE.getText());
            mp.put("usu_nomb", tra_codiE.getText());
            mp.put("zon_codi", rut_codiE.getText() + "-" + rut_codiE.getTextCombo());
            mp.put("cor_orden", alr_numeE.getValorInt());
            mp.put("emp_codi", EU.em_cod);

            JasperPrint jp = JasperFillManager.fillReport(jr, mp, ct);

            //     JasperViewer.viewReport(jp,false);
            gnu.chu.print.util.printJasper(jp, EU);
            mensaje("");
            mensajeErr("Listado Generado");
        } catch (JRException | PrinterException k)
        {
            Error("Error al generar Listado", k);
        }
    }
}
