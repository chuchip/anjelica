package gnu.chu.anjelica.ventas;


/**
 *
 * <p>Titulo: MantPedRuta </p>
* <p>Descripcion: Mantenimiento Pedidos de ruta
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
import gnu.chu.anjelica.listados.Listados;
import gnu.chu.anjelica.pad.MantArticulos;
import gnu.chu.anjelica.pad.pdclien;
import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.anjelica.pad.pdprove;
import gnu.chu.controles.StatusBar;
import gnu.chu.controles.miCellRender;
import gnu.chu.eventos.GridAdapter;
import gnu.chu.eventos.GridEvent;
import gnu.chu.interfaces.PAD;
import gnu.chu.interfaces.ejecutable;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.miThread;
import gnu.chu.utilidades.navegador;
import gnu.chu.utilidades.ventanaPad;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.net.UnknownHostException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class ManPedRuta extends ventanaPad implements PAD
{
    private final int JTLIN_CANTI=6;
    private final int JTLIN_PROCOD=1;
    private final int JTLIN_PRONOMB=2;
    private final int JTLIN_TIPLIN=0;
    private final int JTLIN_COMENT=9;
    private final int JTLIN_PRV=3;
    private final int JTLIN_FECCAD=5;
    private double kilosColgado,kilosEncajado;
    boolean swOrdenado=false;
    JasperReport jr=null;
    private boolean ARG_MODSALA=false;
    boolean swCarga=false; // Indica si se esta cargando el grid de pedidos.
    final int JT_EJEPED=0;
    final int JT_NUMPED=1;
    final int JT_FECENT=2;
    final int JT_KILCAJ=3;
    final int JT_KILCOL=4;
    final int JT_KILTOT=5;
    final int JT_CLICOD=6;
    final int JT_CLINOMB=7;
    final int JT_CODRUT=8;
    final int JT_CLIDIR=9;
    final int JT_CLICP=10;
    final int JT_CLIPOBL=11;
    final int JT_COMREP=12;
    
    public ManPedRuta(EntornoUsuario eu, Principal p) {
        this(eu, p, null);
    }

    public ManPedRuta(EntornoUsuario eu, Principal p, HashMap<String,String> ht) {
        EU = eu;
        vl = p.panel1;
        jf = p;
        eje = true;

        try
        {
            ponParametros(ht);
            setTitulo("Mantenimiento Pedidos de rutas");
            setAcronimo("maperu");
            if (jf.gestor.apuntar(this))
                jbInit();
            else
                setErrorInit(true);
        } catch (Exception e)
        {
            ErrorInit(e);
        }
    }

    public ManPedRuta(gnu.chu.anjelica.menu p, EntornoUsuario eu, HashMap ht) {
        EU = eu;
        vl = p.getLayeredPane();
        eje = false;

        try {
          ponParametros(ht);
          setAcronimo("maperu");
          setTitulo("Mantenimiento Pedidos de rutas");
          jbInit();
        } catch (Exception e) {
            ErrorInit(e);
        } 
    } 
    private void ponParametros(HashMap<String,String> ht)
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
        this.setVersion("2017-10-19 "+(ARG_MODSALA?" Modo Sala ":""));
        
        strSql = "SELECT * FROM pedrutacab "+
            (ARG_MODSALA?" where usu_nomb ='"+EU.usuario+"'":"")+
            " order by prc_fecsal";

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
     * Comprueba datos Pedido
     * @param row linea grid
     * @return  -1 Si no se comnprobara albaran. -2 Si esta todo bien. >=0 ERROR
     */
    int getDatosPedido(int row)
    {
         try
        {            
            if (! pedidoHasCambio() )
                return -1;         
            
            if (pvc_numeE.getValorInt()==0)
            {
                limpiaLinea(row);
                return -1; // No tiene Albaran. Lo ignoro.
            }
            if ( pdpeve.getIdPedido(dtCon1,EU.em_cod,eje_numeE.getValorInt(),pvc_numeE.getValorInt())<=0)
            {
                mensajeErr("Pedido NO encontrado");
                limpiaLinea(row);
                return JT_NUMPED;
            }
                     
            resetCambioPedido();          
           
            jt.setValor(dtCon1.getInt("cli_codi"),row,JT_CLICOD);
            pdclien.getNombreCliente(dtStat,dtCon1.getInt("cli_codi"));
            String cliNomb=dtCon1.getString("pvc_clinom").equals("")?
                dtStat.getString("cli_nomen"):dtCon1.getString("pvc_clinom");
            jt.setValor(dtCon1.getString("pvc_fecent"),row,JT_FECENT);
            jt.setValor(cliNomb,row,JT_CLINOMB);
            jt.setValor(dtStat.getString("cli_diree"),row,JT_CLIDIR);
            jt.setValor(dtStat.getString("cli_codpoe"),row,JT_CLICP);
            jt.setValor(dtStat.getString("cli_poble"),row,JT_CLIPOBL);
            calculaKilosPedido(dtCon1,eje_numeE.getValorInt(),pvc_numeE.getValorInt());
            plr_kilcajE.setValorDec(kilosEncajado);
            plr_kilcolE.setValorDec(kilosColgado);
            jt.setValor(kilosEncajado,row,JT_KILCAJ);
            jt.setValor(kilosColgado,row,JT_KILCOL);
            jt.setValor(kilosEncajado+kilosColgado,row,JT_KILTOT);                        
            jt.setValor(dtStat.getString("cli_comenv"),row,JT_COMREP);
            jt.setValor(dtStat.getString("cli_codrut"),row,JT_CODRUT);
            cli_nombE.setText(cliNomb);
            cli_direeE.setText(dtStat.getString("cli_diree"));            
            cli_codpoeE.setText(dtStat.getString("cli_codpoe"));
            cli_pobleE.setText(dtStat.getString("cli_poble"));
            alr_comrepE.setText(dtStat.getString("cli_comenv"));
            verDatPed(eje_numeE.getValorInt(),pvc_numeE.getValorInt());
            return -2;
        } catch (SQLException ex)
        {
            Error("Error al comprobar albaran para ruta", ex);
        }
        return -1;
    }
  
    void calculaKilosPedido(DatosTabla dt,int ejeNume,int pvcNume) throws SQLException
    {
         String s="SELECT pro_encaja,sum(pvl_kilos) as pvl_kilos FROM v_pedven as p,v_articulo as a  "+
           " WHERE p.emp_codi =  "+EU.em_cod+         
           " and a.pro_codi = p.pro_codi"+
           " AND p.eje_nume = "+ejeNume+           
           " and p.pvc_nume = "+pvcNume+
           " group by pro_encaja";
         kilosColgado=0;
         kilosEncajado=0;
         if (dt.select(s))
         {
             do
             {
              if (dt.getInt("pro_encaja")==0)              
                kilosColgado+=dt.getDouble("pvl_kilos"); // Genero colgado
              else
                kilosEncajado+=dt.getDouble("pvl_kilos");
             } while (dt.next());
         }
    }
    void resetCambioPedido()
    {
          pvc_numeE.resetCambio();
          eje_numeE.resetCambio();      
    }
    boolean pedidoHasCambio()
    {
        return pvc_numeE.hasCambio() || eje_numeE.hasCambio() ;
    }
    int cambiaLinJT(int row)
    {       
        try
        {
            int nl=getDatosPedido(row);
            
            if (nl>=0)
                return JT_NUMPED; 
            
            String s="select * from v_pedruta where emp_codi ="+EU.em_cod
                + " and  eje_nume = " + eje_numeE.getValorInt()
                + " and pvc_nume = " + pvc_numeE.getValorInt()
                + " and pru_id != " + pru_idE.getValorInt();
            if (dtStat.select(s))
            {
                msgBox("Pedido: "+ pvc_numeE.getValorInt()+" ya se sirvio en ruta: " + dtStat.getInt("pru_id")
                    + " de fecha: " + dtStat.getFecha("prc_fecsal", "dd-MM-yyyy") + "\n Volver a cargar en ruta ?");
            }         
            if (checkLineaRepe(eje_numeE.getValorInt(),pvc_numeE.getValorInt(),row ))
                return 0;           
            plr_kilcajE.resetCambio();
            plr_kilcolE.resetCambio();

            alr_comrepE.resetCambio();
           
            actAcumul();
          
        } catch (SQLException ex)
        {
            Error("Error al comprobar linea", ex);
        }
        return -1;
    }
    private boolean  checkLineaRepe(int pvcAno, int pvcNume, int row)
    {
        if (pvcAno==0 || pvcNume==0 )
            return false;
        int nRows = jt.getRowCount();
        for (int n = 0; n < nRows; n++)
        {
            if (n == row)
                continue;
            if (jt.getValorInt(n, JT_EJEPED) == pvcAno
                && jt.getValorInt(n, JT_NUMPED) == pvcNume      )
            {
                if (row>=0)
                    msgBox("Pedido ya se metio en la linea: " + n + " de esta carga");
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
        jt.setValor(0, row, JT_KILCAJ);
        jt.setValor(0, row, JT_KILCOL);
        jt.setValor(0, row, JT_KILTOT);
    }
    
    void verDatos()
    {
        if (dtCons.getNOREG())
            return;
        try {
            pru_idE.setValorInt(dtCons.getInt("pru_id"));
            if (! dtCon1.select("select * from pedrutacab where pru_id ="+dtCons.getInt("pru_id")))
            {
                Pcabe.resetTexto();
                msgBox("No encontrado parte ruta con ID: "+dtCons.getInt("pru_id"));
                return;
            }
            rut_codiE.setText(dtCon1.getString("rut_codi"));
            tra_codiE.setValor(dtCon1.getString("usu_nomb"));           
            prc_fecsalE.setDate(dtCon1.getDate("prc_fecsal"));
            prc_fecsalH.setText(dtCon1.getFecha("prc_fecsal","HH"));
            prc_fecsalM.setText(dtCon1.getFecha("prc_fecsal","mm"));
            //alr_impgasE.setValorInt(dtCon1.getInt("alr_impgas",true));
            prc_comentE.setText(dtCon1.getString("prc_coment"));
            PPie.resetTexto();
            swCarga=true;
            jt.removeAllDatos();            
            if ( dtCon1.select("select l.*,cl.cli_nomen as cli_nomb,cl.cli_pobl,cl.cli_codrut from v_pedruta as l "
                 + " left join v_cliente as cl "+
                " on l.cli_codi = cl.cli_codi where pru_id ="+dtCons.getInt("pru_id")+                
                 " order by plr_orden"))
            {

                 do
                 {
                     ArrayList a = new ArrayList();
                     a.add(dtCon1.getInt("eje_nume"));                     
                     a.add(dtCon1.getString("pvc_nume"));
                     a.add(dtCon1.getDate("pvc_fecent"));
                     a.add(dtCon1.getDouble("plr_kilcaj"));
                     a.add(dtCon1.getDouble("plr_kilcol"));
                     a.add(dtCon1.getDouble("plr_kilcol")+dtCon1.getDouble("plr_kilcaj"));
                     a.add(dtCon1.getString("cli_codi"));                     
                     a.add(dtCon1.getObject("cli_nomen")==null?dtCon1.getString("cli_nomb"):
                         dtCon1.getString("cli_nomen"));
                     a.add(dtCon1.getString("cli_codrut"));
                     a.add(dtCon1.getString("cli_diree", true));
                     a.add(dtCon1.getString("cli_codpoe", true));
                     a.add(dtCon1.getObject("cli_poble")==null?dtCon1.getString("cli_pobl"):
                         dtCon1.getString("cli_poble"));                                                                                 
                     a.add(dtCon1.getString("alr_comrep"));
//                     a.add((dtCon1.getInt("alr_repet") != 0));
                     jt.addLinea(a);
                 } while (dtCon1.next());
                 jt.requestFocusInicio();
                 swCarga=false;
                 verDatPed(jt.getValorInt(0,JT_EJEPED),jt.getValorInt(0,JT_NUMPED));

             }
//             else
//                 msgBox("No encontradas albaranes para parte ruta con ID: "+dtCons.getInt("alr_nume"));

             
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
        double kgCaja=0;
        double kgColgado=0;
        
        int nPed=0;
        for (int n=0;n<nRow;n++)
        {
            if (jt.getValorInt(n,JT_NUMPED)==0)
                continue;
            nPed++;
            kgCaja+=jt.getValorDec(n,JT_KILCAJ);
            kgColgado+=jt.getValorDec(n,JT_KILCOL);            
        }
        kgCajaE.setValorDec(kgCaja);
        kgColgadoE.setValorDec(kgColgado);
        kgTotalE.setValorDec(kgCaja+kgColgado);
        numPedidosE.setValorInt(nPed);
        
    }
    @Override
  public void PADAddNew()
  {    
    
    mensaje("Insertar Nuevo Registro");
    swOrdenado=false;
    Pcabe.resetTexto();
    prc_comentE.resetTexto();
    tra_codiE.setValor(EU.usuario);    
    prc_fecsalE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
    prc_fecsalH.setText("6");
    prc_fecsalM.setText("0");
  
    jt.removeAllDatos();
    activar(true);
    PPie.resetTexto();
    pru_idE.setEnabled(false);
    jt.requestFocusInicio();
    jt.ponValores(0,false,false);
    jt.setEnabled(false);
    jtClien.setOrdenadoGrid(false);
    prc_fecsalE.requestFocus();
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
    prc_comentE.resetTexto();
    prc_fecsalE.requestFocus();
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
         if (!setBloqueo(dtAdd, "pedrutacab", pru_idE.getText()))
         {
             msgBox(msgBloqueo);
             nav.pulsado = navegador.NINGUNO;
             activaTodo();
             return;
         }
         String s = "SELECT * FROM pedrutacab WHERE pru_id = " + pru_idE.getValorInt();
         if (!dtAdd.select(s, true))
         {
             mensajeErr("Registro ha sido borrado");
             resetBloqueo(dtAdd, "pedrutacab", pru_idE.getText(), true);
             activaTodo();
             mensaje("");
             return;
         }
         llenaGridClientes();
     } catch (SQLException | UnknownHostException k)
     {
       Error("Error al bloquear el registro", k);
       return;
     }
     pru_idE.setEnabled(false);
     jtClien.setOrdenadoGrid(false);
     resetCambioPedido();
     plr_kilcolE.resetCambio();
     plr_kilcajE.resetCambio();
     prc_fecsalE.resetCambio();
     alr_comrepE.resetCambio();
     if (ARG_MODSALA)
     {
         jt.setEnabled(false);  
         rut_codiE.setEnabled(false);
         prc_fecsalE.requestFocus();
     }
     jt.setEnabled(true);
     jt.requestFocusInicioLater();
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
         if (!setBloqueo(dtAdd, "pedrutacab", pru_idE.getText()))
         {
             msgBox(msgBloqueo);
             nav.pulsado = navegador.NINGUNO;
             activaTodo();
             return;
         }
         String s = "SELECT * FROM pedrutacab WHERE pru_id = " + pru_idE.getValorInt();
         if (!dtAdd.select(s, true))
         {
             mensajeErr("Registro ha sido borrado");
             resetBloqueo(dtAdd, "pedrutacab", pru_idE.getText(), true);
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
         
     
         if (! ARG_MODSALA)
         {
             if (!swOrdenar.isSelected())
             {
                jt.salirGrid();
                if (cambiaLinJT(jt.getSelectedRow()) >= 0)
                    return;
             }
             if (getNumeroPedidos()==0)
                return ;        
         }
         dtAdd.edit();
         guardaCab(pru_idE.getValorInt());
         // borro lineas e inserto las nuevas
         
         if (! ARG_MODSALA)
         {
             String s = "delete from pedrutalin where pru_id=" + pru_idE.getValorInt();
             dtAdd.executeUpdate(s);
             int orden = 1;
             int  nl = jt.getRowCount();
             for (int n = 0; n < nl; n++)
             {
                 if (jt.getValorInt(n, JT_NUMPED) == 0)
                     continue;
                 guardaLineas(pru_idE.getValorInt(), orden, n);
                 orden++;
             }  
             guardaOrden();
         }
         dtAdd.commit();
         mensajeErr("Pedidos de ruta.. Modificados");
         resetBloqueo(dtAdd, "pedrutacab", pru_idE.getText(), false);
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
       resetBloqueo(dtAdd, "pedrutacab",pru_idE.getText(), true);
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
            resetBloqueo(dtAdd, "pedrutacab", pru_idE.getText(), true);
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

        eje_numeE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        pvc_numeE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#####9");
        cli_codiE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#####9");
        cli_nombE = new gnu.chu.controles.CTextField(Types.CHAR,"X",40);
        plr_kilcajE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##,##9");
        alr_comrepE = new gnu.chu.controles.CTextField(Types.CHAR,"X",80);
        cli_pobleE = new gnu.chu.controles.CTextField(Types.CHAR,"X",50);
        cli_direeE = new gnu.chu.controles.CTextField(Types.CHAR,"X",100);
        cli_codpoeE = new gnu.chu.controles.CTextField(Types.CHAR,"X",8);
        cli_codrutE = new gnu.chu.controles.CTextField(Types.CHAR,"X",2);
        plr_kilcolE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##,##9");
        plr_kiltotE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##,##9");
        pvc_fecentE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yy");
        Pprinc = new gnu.chu.controles.CPanel();
        Pcabe = new gnu.chu.controles.CPanel();
        cLabel6 = new gnu.chu.controles.CLabel();
        prc_fecsalE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        prc_fecsalH = new gnu.chu.controles.CTextField(Types.DECIMAL,"99");
        prc_fecsalM = new gnu.chu.controles.CTextField(Types.DECIMAL,"99");
        cLabel1 = new gnu.chu.controles.CLabel();
        cLabel7 = new gnu.chu.controles.CLabel();
        cLabel2 = new gnu.chu.controles.CLabel();
        cLabel3 = new gnu.chu.controles.CLabel();
        rut_codiE = new gnu.chu.controles.CLinkBox();
        cLabel9 = new gnu.chu.controles.CLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        prc_comentE = new gnu.chu.controles.CTextArea();
        pru_idE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###,##9");
        BInsAuto = new gnu.chu.controles.CButton(Iconos.getImageIcon("fill"));
        BirGrid = new gnu.chu.controles.CButton();
        tra_codiE = new gnu.chu.controles.CComboBox();
        Ptab1 = new gnu.chu.controles.CTabbedPane();
        PPedidos = new gnu.chu.controles.CPanel();
        jt = new gnu.chu.controles.CGridEditable(13){
            @Override
            public int cambiaLinea(int row, int col)
            {
                return cambiaLinJT(row);
            }
        };
        jtLinPed = new gnu.chu.controles.Cgrid(4);
        PDatPed = new gnu.chu.controles.CPanel();
        cLabel21 = new gnu.chu.controles.CLabel();
        cLabel13 = new gnu.chu.controles.CLabel();
        cLabel22 = new gnu.chu.controles.CLabel();
        kgCajaE1 = new gnu.chu.controles.CTextField(Types.DECIMAL,"###,##9.9");
        kgColgadoE1 = new gnu.chu.controles.CTextField(Types.DECIMAL,"###,##9.9");
        kgTotalE1 = new gnu.chu.controles.CTextField(Types.DECIMAL,"###,##9.9");
        jtClien = new gnu.chu.controles.Cgrid(4,false);
        PPie = new gnu.chu.controles.CPanel();
        cLabel12 = new gnu.chu.controles.CLabel();
        numPedidosE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        kgCajaE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###,##9.9");
        Baceptar = new gnu.chu.controles.CButton();
        Bcancelar = new gnu.chu.controles.CButton();
        cLabel18 = new gnu.chu.controles.CLabel();
        cLabel19 = new gnu.chu.controles.CLabel();
        BImpri = new gnu.chu.controles.CButton(Iconos.getImageIcon("print"));
        kgColgadoE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###,##9.9");
        kgTotalE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###,##9.9");
        cLabel20 = new gnu.chu.controles.CLabel();
        swOrdenar = new gnu.chu.controles.CCheckBox();
        Bordenar = new gnu.chu.controles.CButton(Iconos.getImageIcon("order"));

        eje_numeE.setValorDec(EU.ejercicio);

        cli_codiE.setEnabled(false);

        cli_codrutE.setText("A");
        cli_codrutE.setEnabled(false);

        plr_kiltotE.setEnabled(false);

        pvc_fecentE.setEnabled(false);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Pprinc.setLayout(new java.awt.GridBagLayout());

        Pcabe.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Pcabe.setMaximumSize(new java.awt.Dimension(560, 90));
        Pcabe.setMinimumSize(new java.awt.Dimension(560, 90));
        Pcabe.setPreferredSize(new java.awt.Dimension(560, 90));
        Pcabe.setLayout(null);

        cLabel6.setText("Salida Ruta");
        cLabel6.setPreferredSize(new java.awt.Dimension(52, 18));
        Pcabe.add(cLabel6);
        cLabel6.setBounds(2, 23, 70, 17);

        prc_fecsalE.setPreferredSize(new java.awt.Dimension(10, 18));
        Pcabe.add(prc_fecsalE);
        prc_fecsalE.setBounds(80, 23, 70, 17);

        prc_fecsalH.setText("0");
        Pcabe.add(prc_fecsalH);
        prc_fecsalH.setBounds(160, 23, 20, 17);

        prc_fecsalM.setText("0");
        Pcabe.add(prc_fecsalM);
        prc_fecsalM.setBounds(190, 23, 20, 17);

        cLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cLabel1.setText(":");
        Pcabe.add(cLabel1);
        cLabel1.setBounds(180, 23, 10, 17);

        cLabel7.setText("Operario");
        cLabel7.setPreferredSize(new java.awt.Dimension(52, 18));
        Pcabe.add(cLabel7);
        cLabel7.setBounds(150, 0, 60, 17);

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
        rut_codiE.setBounds(270, 23, 260, 17);
        rut_codiE.getAccessibleContext().setAccessibleName("");

        cLabel9.setText("Identificador");
        Pcabe.add(cLabel9);
        cLabel9.setBounds(0, 0, 80, 17);

        prc_comentE.setColumns(20);
        prc_comentE.setRows(5);
        jScrollPane2.setViewportView(prc_comentE);

        Pcabe.add(jScrollPane2);
        jScrollPane2.setBounds(10, 45, 540, 40);
        Pcabe.add(pru_idE);
        pru_idE.setBounds(80, 0, 60, 17);

        BInsAuto.setToolTipText("Insertar Alb. Pend. de Ruta");
        Pcabe.add(BInsAuto);
        BInsAuto.setBounds(510, 0, 30, 24);
        Pcabe.add(BirGrid);
        BirGrid.setBounds(540, 120, 2, 2);
        Pcabe.add(tra_codiE);
        tra_codiE.setBounds(210, 0, 210, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        Pprinc.add(Pcabe, gridBagConstraints);

        PPedidos.setLayout(new java.awt.GridBagLayout());

        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jt.setMaximumSize(new java.awt.Dimension(559, 151));
        jt.setMinimumSize(new java.awt.Dimension(559, 151));
        jt.setName(""); // NOI18N
        jt.setPreferredSize(new java.awt.Dimension(559, 151));
        ArrayList v=new ArrayList();
        v.add("Ejer."); // 0
        v.add("N.Ped."); // 1
        v.add("Fec.Ent"); // 2
        v.add("Kg.Caja"); // 3
        v.add("Kg.Colg."); // 3
        v.add("Kg.Tot."); // 4
        v.add("Cliente"); // 5
        v.add("Nombre Cliente"); // 6
        v.add("C.Rut"); // 7
        v.add("Direccion"); // 8
        v.add("C.P"); // 9
        v.add("Poblacion"); // 10
        v.add("Coment."); // 11
        jt.setCabecera(v);
        jt.setColNueva(JT_NUMPED);
        jt.setAnchoColumna(new int[]{40,50,60,50,50,50,50,200,30,120,40,90,100});
        jt.setAlinearColumna(new int[]{2,2,1,2,2,2,2,0,0,0,0,0,0});

        ArrayList vc=new ArrayList();
        vc.add(eje_numeE);
        vc.add(pvc_numeE);
        vc.add(pvc_fecentE);
        vc.add(plr_kilcajE);
        vc.add(plr_kilcolE);
        vc.add(plr_kiltotE);
        vc.add(cli_codiE);
        vc.add(cli_nombE);
        vc.add(cli_codrutE);
        vc.add(cli_direeE);
        vc.add(cli_codpoeE);
        vc.add(cli_pobleE);
        vc.add(alr_comrepE);

        try {
            jt.setCampos(vc);
        } catch (Exception k)
        {
            Error("Error al poner campos en grid ",k);
            return;
        }

        jt.setFormatoCampos();
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.weighty = 2.0;
        PPedidos.add(jt, gridBagConstraints);

        try {confJtLin();} catch (Exception k){Error("Error al configurar grid Lineas",k);}
        jtLinPed.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtLinPed.setMaximumSize(new java.awt.Dimension(200, 81));
        jtLinPed.setMinimumSize(new java.awt.Dimension(200, 81));
        jtLinPed.setPreferredSize(new java.awt.Dimension(200, 81));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        PPedidos.add(jtLinPed, gridBagConstraints);

        PDatPed.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        PDatPed.setMaximumSize(new java.awt.Dimension(180, 65));
        PDatPed.setMinimumSize(new java.awt.Dimension(180, 65));
        PDatPed.setName(""); // NOI18N
        PDatPed.setPreferredSize(new java.awt.Dimension(180, 65));
        PDatPed.setLayout(null);

        cLabel21.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cLabel21.setText("Kg. Caja");
        PDatPed.add(cLabel21);
        cLabel21.setBounds(10, 2, 80, 17);

        cLabel13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cLabel13.setText("Kg. Colgado");
        PDatPed.add(cLabel13);
        cLabel13.setBounds(10, 18, 75, 20);

        cLabel22.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cLabel22.setText("Kg. Total");
        PDatPed.add(cLabel22);
        cLabel22.setBounds(10, 36, 60, 17);

        kgCajaE1.setEditable(false);
        PDatPed.add(kgCajaE1);
        kgCajaE1.setBounds(90, 0, 50, 17);

        kgColgadoE1.setEditable(false);
        PDatPed.add(kgColgadoE1);
        kgColgadoE1.setBounds(90, 18, 50, 17);

        kgTotalE1.setEditable(false);
        PDatPed.add(kgTotalE1);
        kgTotalE1.setBounds(90, 36, 50, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        PPedidos.add(PDatPed, gridBagConstraints);

        Ptab1.addTab("Pedidos", PPedidos);

        ArrayList acl=new ArrayList();
        acl.add("Cliente");
        acl.add("Nombre");
        acl.add("Direccion");
        acl.add("Poblacion");
        jtClien.setCabecera(acl);

        jtClien.setAlinearColumna(new int[]{2,0,0,0});
        jtClien.setAnchoColumna(new int[]{35,120,100,90});
        jtClien.setAjustarGrid(true);
        Ptab1.addTab("Clientes", jtClien);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Pprinc.add(Ptab1, gridBagConstraints);

        PPie.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        PPie.setMaximumSize(new java.awt.Dimension(549, 42));
        PPie.setMinimumSize(new java.awt.Dimension(549, 42));
        PPie.setName(""); // NOI18N
        PPie.setPreferredSize(new java.awt.Dimension(549, 42));
        PPie.setLayout(null);

        cLabel12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cLabel12.setText("Kg. Colgado");
        PPie.add(cLabel12);
        cLabel12.setBounds(110, 20, 75, 17);

        numPedidosE.setEditable(false);
        PPie.add(numPedidosE);
        numPedidosE.setBounds(70, 2, 30, 17);

        kgCajaE.setEditable(false);
        PPie.add(kgCajaE);
        kgCajaE.setBounds(185, 2, 50, 17);
        PPie.add(Baceptar);
        Baceptar.setBounds(370, 2, 90, 22);
        PPie.add(Bcancelar);
        Bcancelar.setBounds(460, 2, 90, 22);

        cLabel18.setText("Nº  Pedidos");
        PPie.add(cLabel18);
        cLabel18.setBounds(2, 2, 70, 17);

        cLabel19.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cLabel19.setText("Kg. Total");
        PPie.add(cLabel19);
        cLabel19.setBounds(250, 2, 60, 17);

        BImpri.setText("Imprimir");
        PPie.add(BImpri);
        BImpri.setBounds(250, 23, 80, 19);

        kgColgadoE.setEditable(false);
        PPie.add(kgColgadoE);
        kgColgadoE.setBounds(185, 20, 50, 17);

        kgTotalE.setEditable(false);
        PPie.add(kgTotalE);
        kgTotalE.setBounds(310, 2, 50, 17);

        cLabel20.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cLabel20.setText("Kg. Caja");
        PPie.add(cLabel20);
        cLabel20.setBounds(110, 2, 80, 17);

        swOrdenar.setText("Modo Ordenar");
        swOrdenar.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        PPie.add(swOrdenar);
        swOrdenar.setBounds(2, 20, 100, 17);

        Bordenar.setText("Ordenar");
        PPie.add(Bordenar);
        Bordenar.setBounds(340, 23, 80, 19);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        Pprinc.add(PPie, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton BImpri;
    private gnu.chu.controles.CButton BInsAuto;
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CButton Bcancelar;
    private gnu.chu.controles.CButton BirGrid;
    private gnu.chu.controles.CButton Bordenar;
    private gnu.chu.controles.CPanel PDatPed;
    private gnu.chu.controles.CPanel PPedidos;
    private gnu.chu.controles.CPanel PPie;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CTabbedPane Ptab1;
    private gnu.chu.controles.CTextField alr_comrepE;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel12;
    private gnu.chu.controles.CLabel cLabel13;
    private gnu.chu.controles.CLabel cLabel18;
    private gnu.chu.controles.CLabel cLabel19;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel20;
    private gnu.chu.controles.CLabel cLabel21;
    private gnu.chu.controles.CLabel cLabel22;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel9;
    private gnu.chu.controles.CTextField cli_codiE;
    private gnu.chu.controles.CTextField cli_codpoeE;
    private gnu.chu.controles.CTextField cli_codrutE;
    private gnu.chu.controles.CTextField cli_direeE;
    private gnu.chu.controles.CTextField cli_nombE;
    private gnu.chu.controles.CTextField cli_pobleE;
    private gnu.chu.controles.CTextField eje_numeE;
    private javax.swing.JScrollPane jScrollPane2;
    private gnu.chu.controles.CGridEditable jt;
    private gnu.chu.controles.Cgrid jtClien;
    private gnu.chu.controles.Cgrid jtLinPed;
    private gnu.chu.controles.CTextField kgCajaE;
    private gnu.chu.controles.CTextField kgCajaE1;
    private gnu.chu.controles.CTextField kgColgadoE;
    private gnu.chu.controles.CTextField kgColgadoE1;
    private gnu.chu.controles.CTextField kgTotalE;
    private gnu.chu.controles.CTextField kgTotalE1;
    private gnu.chu.controles.CTextField numPedidosE;
    private gnu.chu.controles.CTextField plr_kilcajE;
    private gnu.chu.controles.CTextField plr_kilcolE;
    private gnu.chu.controles.CTextField plr_kiltotE;
    private gnu.chu.controles.CTextArea prc_comentE;
    private gnu.chu.controles.CTextField prc_fecsalE;
    private gnu.chu.controles.CTextField prc_fecsalH;
    private gnu.chu.controles.CTextField prc_fecsalM;
    private gnu.chu.controles.CTextField pru_idE;
    private gnu.chu.controles.CTextField pvc_fecentE;
    private gnu.chu.controles.CTextField pvc_numeE;
    private gnu.chu.controles.CLinkBox rut_codiE;
    private gnu.chu.controles.CCheckBox swOrdenar;
    private gnu.chu.controles.CComboBox tra_codiE;
    // End of variables declaration//GEN-END:variables
   private void confJtLin() throws Exception
   {
     ArrayList v = new ArrayList();     
     v.add("Prod."); // 0
     v.add("Desc. Prod."); // 2 
     v.add("Cant"); // 2
     v.add("Comentario"); // 3 Comentario
     jtLinPed.setCabecera(v);   
     jtLinPed.setPuntoDeScroll(50);
     jtLinPed.setAnchoColumna(new int[]
                        {60, 160, 70, 200});
     jtLinPed.setAlinearColumna(new int[]
                          {2,0, 2, 0});        
     jtLinPed.setAjustarGrid(true);
     jtLinPed.setBuscarVisible(false);
    }
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
     
        if (ARG_MODSALA)
            tra_codiE.setEnabled(false);
        pru_idE.setColumnaAlias("alr_nume");
     
        rut_codiE.setColumnaAlias("rut_codi");
        tra_codiE.setColumnaAlias("usu_nomb");
        prc_fecsalE.setColumnaAlias("alr_fecsal");
        Pcabe.setDefButton(Baceptar);
        jt.setDefButton(Baceptar);
        
        activarEventos();
        if (! ARG_MODSALA && ! dtCons.getNOREG() )
                dtCons.last();
        
        verDatos();
    }
    
    void activarEventos()
    {
        BImpri.addActionListener(new ActionListener()
         {
              @Override
              public void actionPerformed(ActionEvent e)
              {
                  Bimpri_actionPerformed();
              }
        });
        Bordenar.addActionListener(new ActionListener()
         {
              @Override
              public void actionPerformed(ActionEvent e)
              {
                  ordenaPedidos();
              }
        });
        jt.addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (swCarga ||  e.getValueIsAdjusting() || jt.isVacio() && !nav.isEdicion()) // && e.getFirstIndex() == e.getLastIndex())
                    return;
                verDatPed(jt.getValorInt(jt.getSelectedRowDisab(), JT_EJEPED), jt.getValorInt(jt.getSelectedRowDisab(),JT_NUMPED));
            }
        });
        swOrdenar.addActionListener(new ActionListener()
         {
              @Override
              public void actionPerformed(ActionEvent e)
              {
                  if (swOrdenar.isSelected())
                  {
                    jt.salirGrid();
                    if (cambiaLinJT(jt.getSelectedRow())>=0)
                    {
                        swOrdenar.setSelected(false);
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
                  jt.setDragEnabled(swOrdenar.isSelected());      
                  jtClien.setDragEnabled(swOrdenar.isSelected());
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
                  jt.requestFocusInicioLater();
                  return;
              }
              if (e.getClickCount()<2 || jt.isEnabled() || jt.isVacio()) 
                  return;
              verDocumento();
            }
         });
        
         jt.addGridListener(new GridAdapter()
         {
              @Override
              public void cambioColumna(GridEvent event)   {
                  if (jt.isEnabled() && event.getColumna()==JT_NUMPED)
                      getDatosPedido(event.getLinea());
              }
         });
      
         BInsAuto.addActionListener(new ActionListener()
         {
              @Override
              public void actionPerformed(ActionEvent e)
              {
                  insertarAuto();
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
                    if (! checkIrGrid())
                        return;
                    jt.requestFocusInicioLater();
                  }
                  else
                    jt.requestFocusInicioLater(); 
              }
         });
             
    }
   void verDatPed(int ejeNume,int pvcNume)
   {
     try
     {
       String s="SELECT pro_encaja,p.*,cl.cli_pobl FROM v_pedven as p,v_cliente as cl,v_articulo as a  "+
           " WHERE p.emp_codi =  "+EU.em_cod+
           " and p.cli_codi = cl.cli_codi "+ 
           " AND p.eje_nume = "+ejeNume+
           " and p.pvc_nume = "+pvcNume+
           " and p.pro_codi = a.pro_codi "+
           " order by p.pvl_numlin ";
       jtLinPed.removeAllDatos();
      // Ppie.resetTexto();
     
       if (! dtCon1.select(s))
       {
         mensajeErr("NO ENCONTRADOS DATOS PARA ESTE PEDIDO");
         return;
       }
    kilosColgado=0;
    kilosEncajado=0;
       do
       {
         ArrayList v=new ArrayList();
         v.add(dtCon1.getString("pro_codi"));
         v.add(MantArticulos.getNombProd(dtCon1.getInt("pro_codi"), dtStat));
         v.add(dtCon1.getString("pvl_canti")+" "+dtCon1.getString("pvl_tipo"));
         v.add(dtCon1.getString("pvl_comen"));
         jtLinPed.addLinea(v);
         if (dtCon1.getInt("pro_encaja")==0)              
                kilosColgado+=dtCon1.getDouble("pvl_kilos"); // Genero colgado
              else
                kilosEncajado+=dtCon1.getDouble("pvl_kilos");
       } while (dtCon1.next());
       kgCajaE1.setValorDec(kilosEncajado);
       kgColgadoE1.setValorDec(kilosColgado);
       kgTotalE1.setValorDec(kilosColgado+kilosEncajado);
//       actAcumJT();
     } catch (Exception k)
     {
       Error("Error al Ver datos de pedido",k);
     }
   }
    
 
    /**
     * 
     */
    void insertarAuto()
    {
        try
        {
            if (!rut_codiE.controla(true))
            {
                msgBox("Introduzca ruta");
                return;
            }
            String s="select * from pedvenc as p, v_cliente as cl where "
                + "cl.cli_codi=p.cli_codi and p.rut_codi='"+rut_codiE.getText()+"' and pvc_confir='S'"+
//                " and cl.cli_codi = 48166 "+
                " and pvc_fecent between '"+
                Formatear.getFechaDB(Formatear.sumaDias(prc_fecsalE.getDate() ,-7))+"' and '"+
                    Formatear.getFechaDB(prc_fecsalE.getDate())+"'"+
                " order by cli_ordrut";
            PreparedStatement psPed=dtStat.getPreparedStatement("select * from pedrutalin where pru_id!="+pru_idE.getValorInt()+
                " and pvc_id=?");
             PreparedStatement psAlb=dtStat.getPreparedStatement("select pvc_nume,r.alr_nume,a.avc_impres from pedvenc as p,v_albavec as a left join albrutalin as r "+
                  " on a.avc_id=r.avc_id where   a.avc_nume=p.avc_nume and a.avc_ano=p.avc_ano "+
                  " and a.avc_serie=p.avc_serie and a.emp_codi=p.emp_codi "+
                  " and p.pvc_id=?");
            ResultSet rsPed;
            swCarga=true;
            if (dtCon1.select(s))
            {
                jt.setEnabled(false);
                do 
                {
                    psPed.setInt(1,dtCon1.getInt("pvc_id"));
                    rsPed=psPed.executeQuery();
                    if (rsPed.next())
                        continue; // Ya esta en otra carga
                    psAlb.setInt(1,dtCon1.getInt("pvc_id"));                  
                    rsPed=psAlb.executeQuery();
                    if (rsPed.next())
                    {
                        if (rsPed.getInt("alr_nume")>0 )
                            continue; // Ya esta en otra carga de Albaranes
                        if ((rsPed.getInt("avc_impres") & 1) == 1 )
                            continue; // Impreso
                    }
//                    if ( pdpeve.getIdPedido(dtCon1,EU.em_cod,eje_numeE.getValorInt(),pvc_numeE.getValorInt())==0)
    
//            {
//                mensajeErr("Pedido NO encontrado");
//               
//                return JT_NUMPED;
//            }
                    calculaKilosPedido(dtStat,dtCon1.getInt("eje_nume"),dtCon1.getInt("pvc_nume"));
                    ArrayList a=new ArrayList();
                    a.add(dtCon1.getInt("eje_nume"));
                    a.add(dtCon1.getInt("pvc_nume"));
                    a.add(dtCon1.getDate("pvc_fecent"));
                    a.add(kilosEncajado);
                    a.add(kilosColgado);
                    a.add(kilosEncajado+kilosColgado);
                    a.add(dtCon1.getInt("cli_codi"));                                       
                    String cliNomb=dtCon1.getString("pvc_clinom").equals("")?
                        dtCon1.getString("cli_nomen"):dtCon1.getString("pvc_clinom");
                    a.add(cliNomb);
                    a.add(dtCon1.getString("cli_codrut"));
                   
                    a.add(dtCon1.getString("cli_diree"));                    
                    a.add(dtCon1.getString("cli_codpoe"));
                    a.add(dtCon1.getString("cli_poble"));
                    a.add(dtCon1.getString("cli_comenv"));                   
                    jt.addLinea(a);
                } while (dtCon1.next());
                jt.setEnabled(true);
                jt.requestFocusFinalLater();
                swCarga=false;
            }
        } catch (ParseException | SQLException ex)
        {
            Error("Error al cargar Pedidos", ex);
        }
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
                        if ((prog = jf.gestor.getProceso(pdpeve.getNombreClase())) == null)
                            return;
                        pdpeve cm = (pdpeve) prog;
                        if (cm.inTransation())
                        {
                            msgBox("Mantenimiento Pedidos de Ventas ocupado. No se puede realizar la busqueda");
                            return;
                        }
                        cm.PADQuery();
                        cm.setEjercicioPedido(jt.getValorInt(jt.getSelectedRowDisab(), JT_EJEPED));                        
                        cm.setNumeroPedido(jt.getValorInt(jt.getSelectedRowDisab(), JT_NUMPED));

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
      v.add(pru_idE.getStrQuery());
      v.add(rut_codiE.getStrQuery());
      v.add(tra_codiE.getStrQuery());    
      v.add(prc_fecsalE.getStrQuery());
      String s = "select * from pedrutacab ";

      s = creaWhere(s, v,  true);
      s += " ORDER BY prc_fecsal ";

      mensaje("Espere, por favor ... buscando datos");
      Pcabe.setQuery(false);      
      
//      debug("s: "+s);
      if (!dtCon1.select(s))
      {
        msgBox("No encontradas Pedidos para Rutas con estos criterios");
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

  int getNumeroPedidos()
  {
      int nl = jt.getRowCount();
      int orden = 0;
      for (int n = 0; n < nl; n++)
      {
          if (jt.getValorInt(n, JT_NUMPED) == 0)
              continue;
          orden++;
      }

      if (orden == 0)
          msgBox("Introduzca algun Pedido en la ruta");
      return orden;
  }

    @Override
    public void ej_addnew1()
    {        
        try
        {
            if (! checkCabecera())
                return;
            if (!swOrdenar.isSelected())
            {
                jt.salirGrid();
                if (cambiaLinJT(jt.getSelectedRow())>=0)
                    return;
            }
            if (getNumeroPedidos()==0)
                return ;
        
        
            dtAdd.addNew("pedrutacab", false);
            int id = guardaCab(0);
            
            int orden=1;
            int nl = jt.getRowCount();
            for (int n=0;n<nl;n++)
            {
                if (jt.getValorInt(n,JT_NUMPED)==0)
                    continue;
                guardaLineas(id,orden,n);
                orden++;
            }   
            guardaOrden();
            dtAdd.commit();
            mensajeErr("Pedidos de ruta.. guardados");
            activaTodo();
            strSql="SELECT * FROM pedrutacab where pru_id = "+id;
            rgSelect();
            verDatos();
            nav.pulsado=navegador.NINGUNO;
        } catch (SQLException | ParseException ex )
        {
            Error("Error al guardar cabecera de ruta", ex);
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
        if (id>0)
            dtAdd.setDato("pru_id",id);
        dtAdd.setDato("rut_codi",rut_codiE.getText());
        dtAdd.setDato("usu_nomb",tra_codiE.getValor());
        dtAdd.setDato("prc_fecsal","{ts '"+prc_fecsalE.getFecha("yyyy-MM-dd")+" "+
                prc_fecsalH.getText()+":"+prc_fecsalM.getText()+"'}");          
        dtAdd.setDato("prc_coment",prc_comentE.getText());
        dtAdd.update();
        if (id>0)
            return id;
        dtAdd.select("SELECT lastval()");
        return dtAdd.getInt(1);
    }
    /**
     * Guarda orden de tabla clientes.
     * @throws SQLException 
     */
    void guardaOrden() throws SQLException
    {
        if (!jtClien.getOrdenadoGrid())
            return;
        int nRow=jtClien.getRowCount();
        PreparedStatement psCli=dtAdd.getPreparedStatement("update clientes set cli_ordrut=? where cli_codi=?");
        for (int n=0;n<nRow;n++)
        {
            psCli.setInt(1, n);
            psCli.setInt(2, jtClien.getValorInt(n,0));
            psCli.executeUpdate();
        }           
    }
    /**
     * 
     * @param id
     * @param orden
     * @param nlGrid
     * @throws SQLException 
     */
    void guardaLineas(int id,int orden,int nlGrid) throws SQLException
    {
        dtAdd.addNew("pedrutalin");
        dtAdd.setDato("pru_id",id);
        dtAdd.setDato("plr_orden",orden);
        dtAdd.setDato("pvc_id",pdpeve.getIdPedido(dtStat,EU.em_cod,
            jt.getValorInt(nlGrid,JT_EJEPED),jt.getValorInt(nlGrid,JT_NUMPED)   ));
        dtAdd.setDato("plr_kilcaj",jt.getValorDec(nlGrid,JT_KILCAJ));
        dtAdd.setDato("plr_kilcol",jt.getValorDec(nlGrid,JT_KILCOL));
        
        dtAdd.setDato("cli_nomen",jt.getValString(nlGrid,JT_CLINOMB));
        dtAdd.setDato("cli_diree",jt.getValString(nlGrid,JT_CLIDIR));
        dtAdd.setDato("cli_codpoe",jt.getValString(nlGrid,JT_CLICP));
        dtAdd.setDato("cli_poble",jt.getValString(nlGrid,JT_CLIPOBL));
        dtAdd.setDato("alr_comrep",jt.getValString(nlGrid,JT_COMREP));
//        dtAdd.setDato("alr_repet",jt.getValBoolean(nlGrid,JT_REPET)?-1:0);
       
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
        if (prc_fecsalE.isNull())
        {
            mensajeErr("Introduzca fecha de Ruta");
            prc_fecsalE.requestFocus();
            return false;
        }
        long nDias;
            if (prc_fecsalE.hasCambio())
            {
            nDias = Formatear.comparaFechas(prc_fecsalE.getDate(), Formatear.getDateAct());

            if (nDias < 0 )
            {
                mensajeErr("Fecha de Ruta no puede ser inferior a la actual");
                prc_fecsalE.requestFocus();
                return false;
            }
            if (nDias > 5)
            {
                mensajeErr("Fecha de Ruta no puede ser superior en mas de 5 dias a la actual.");
                prc_fecsalE.requestFocus();
                return false;
            }
        }
        if (prc_fecsalE.isNull() && prc_fecsalH.getValorInt()>0)
        {
            mensajeErr("Si introduce Hora salida. Introduzca el dia");
            prc_fecsalE.requestFocus();
            return false;
        }
        
        if (!rut_codiE.controla())
        {
            mensajeErr("Introduzca ruta");
            return false;
        }
        if (prc_comentE.getText().length()>250)
        {
            msgBox("Comentario no debe ser superior a 250 caracteres. Ha metido: "+prc_comentE.getText().length());
            prc_comentE.requestFocus();
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
        try{
        
            if (prc_fecsalE.isNull() || prc_fecsalE.getError())
            {
                mensajeErr("Fecha Salida no valida");
                prc_fecsalE.requestFocus();
                return false;
            }
            
            if (rut_codiE.getText().equals(""))
            {
                mensajeErr("Introduzca ruta");
                rut_codiE.requestFocus();
                return false;
            }
            llenaGridClientes();
            
            if (nav.pulsado!=navegador.ADDNEW) 
                return true;
            String s="select * from pedrutacab where rut_codi = '"+rut_codiE.getText()+"'"+
                " and usu_nomb = '"+tra_codiE.getValor()+"'"+
                " and prc_fecsal = {ts '"+prc_fecsalE.getFecha("yyyy-MM-dd")+" "+
                prc_fecsalH.getText()+":"+prc_fecsalM.getText()+"'}";
            if (dtStat.select(s))
            {
                msgBox("Ruta ya existe");
                rut_codiE.requestFocus();
                return false;
            }
        } catch (ParseException | SQLException k)
        {
            Error("Error al buscar Salidas duplicadas",k);
        }
        return true;
    }
    void ordenaPedidos() 
    {
        try
        {
            if (getNumeroPedidos()==0)
            {
                jt.requestFocusLater();
                return;
            }
             if (swOrdenar.isSelected())
             {
                jt.salirGrid();
                if (cambiaLinJT(jt.getSelectedRow())>=0)
                {                   
                    jt.requestFocusLater();
                    return;
                }
             }
             swCarga=true;
            String tabla="pedruta"+System.currentTimeMillis();
            String s="create temp table "+tabla+
                "( eje_nume int, "+
                " pvc_nume int,"+
                " pvc_fecent date, "+
                " cli_codi int, "+
                " plr_kilcaj float , "+
                " plr_kilcol float , "+
                " cli_nomen varchar(50) , "+
                " cli_diree varchar(100),"+
                " cli_poble varchar(50) , "+
                " cli_codpoe varchar(8), "+
                " alr_comrep varchar(80))";
            dtAdd.executeUpdate(s);
            int  nl = jt.getRowCount();
             for (int nlGrid = 0; nlGrid < nl; nlGrid++)
             {
                 if (jt.getValorInt(nlGrid, JT_NUMPED) == 0)
                     continue;
                  dtAdd.addNew(tabla);                                  
                  dtAdd.setDato("eje_nume",   jt.getValorInt(nlGrid,JT_EJEPED));
                  dtAdd.setDato("pvc_nume",  jt.getValorInt(nlGrid,JT_NUMPED)   );
                  dtAdd.setDato("pvc_fecent",  jt.getValDate(nlGrid,JT_FECENT)   );
                  
                  dtAdd.setDato("plr_kilcaj",jt.getValorDec(nlGrid,JT_KILCAJ));
                  dtAdd.setDato("plr_kilcol",jt.getValorDec(nlGrid,JT_KILCOL));        
                  dtAdd.setDato("cli_codi",  jt.getValorInt(nlGrid,JT_CLICOD)   );
                  dtAdd.setDato("cli_nomen",jt.getValString(nlGrid,JT_CLINOMB));
                  dtAdd.setDato("cli_diree",jt.getValString(nlGrid,JT_CLIDIR));
                  dtAdd.setDato("cli_codpoe",jt.getValString(nlGrid,JT_CLICP));
                  dtAdd.setDato("cli_poble",jt.getValString(nlGrid,JT_CLIPOBL));
                  dtAdd.setDato("alr_comrep",jt.getValString(nlGrid,JT_COMREP));
                  dtAdd.update();                
             }  
             jt.setEnabled(false);
             jt.removeAllDatos();
             s="select tt.*,cli_codrut from "+tabla+" as tt, v_cliente as cl where cl.cli_codi=tt.cli_codi order by cli_ordrut";
             dtAdd.select(s);
             do
             {
                  ArrayList a = new ArrayList();
                    a.add(dtAdd.getInt("eje_nume"));                     
                    a.add(dtAdd.getString("pvc_nume"));
                    a.add(dtAdd.getDate("pvc_fecent"));
                    a.add(dtAdd.getDouble("plr_kilcaj"));
                    a.add(dtAdd.getDouble("plr_kilcol"));
                    a.add(dtAdd.getDouble("plr_kilcol")+dtAdd.getDouble("plr_kilcaj"));
                    a.add(dtAdd.getString("cli_codi"));                     
                    a.add(dtAdd.getString("cli_nomen"));
                    a.add(dtAdd.getString("cli_codrut"));
                    a.add(dtAdd.getString("cli_diree", true));
                    a.add(dtAdd.getString("cli_codpoe", true));
                    a.add(dtAdd.getString("cli_poble"));                                                                                 
                    a.add(dtAdd.getString("alr_comrep"));
                    jt.addLinea(a);
             } while (dtAdd.next());            
             dtAdd.executeUpdate("drop table "+tabla);
             jt.setEnabled(!swOrdenar.isSelected());
              swCarga=false;
             jt.requestFocusInicio();
             msgBox("Pedidos ordenados");
        } catch (SQLException | ParseException ex)
        {
            Error("Error al ordenar grid", ex);
        }
    }
    void llenaGridClientes() throws SQLException {
        String s = "select cli_codi,cli_nomen,cli_diree,cli_poble from v_cliente "
            + " where rut_codi ='" + rut_codiE.getText() + "'"
            + " and cli_activ= 'S' "
            + "order by cli_ordrut";
        jtClien.removeAllDatos();
        if (dtCon1.select(s))
        {
            do
            {
                ArrayList a = new ArrayList();
                a.add(dtCon1.getInt("cli_codi"));
                a.add(dtCon1.getString("cli_nomen"));
                a.add(dtCon1.getString("cli_diree"));
                a.add(dtCon1.getString("cli_poble"));
                jtClien.addLinea(a);
            } while (dtCon1.next());
        }
        jtClien.requestFocusInicio();
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
            String s = "delete from pedrutalin where pru_id=" + pru_idE.getValorInt();
            dtBloq.executeUpdate(s);
            resetBloqueo(dtAdd, "pedrutacab", pru_idE.getText(), false);
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
         jt.setDragEnabled(false);      
        Pcabe.setEnabled(b);
        if (opcion!=navegador.QUERY && opcion!=navegador.DELETE)
        {
            swOrdenar.setEnabled(b);
            Bordenar.setEnabled(b);
            prc_fecsalH.setEnabled(b);          
            prc_fecsalM.setEnabled(b);
            BInsAuto.setEnabled(b);
        }
        pru_idE.setEnabled(b);
               

        rut_codiE.setEnabled(b);        
      
        
        prc_comentE.setEnabled(b);
        Baceptar.setEnabled(b);
        Bcancelar.setEnabled(b);
    }
   
    
    void Bimpri_actionPerformed()
    {
   
     try {
       if (dtCons.getNOREG())
         return;
       java.util.HashMap mp = Listados.getHashMapDefault();
       mp.put("pru_id",dtCons.getInt("pru_id"));
       JasperReport jr = Listados.getJasperReport(EU,"PedRuta");

       JasperPrint jp = JasperFillManager.fillReport(jr, mp, ct.getConnection());
       gnu.chu.print.util.printJasper(jp, EU);
       
        msgBox("Relacion Pedidos de Ruta ... IMPRESO ");
     }
     catch (JRException | SQLException | PrinterException k)
     {
       Error("Error al imprimir Pedido Venta", k);
     }
   }
}
