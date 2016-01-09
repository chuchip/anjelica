package gnu.chu.anjelica.ventas;

import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.pad.pdclien;
import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.controles.StatusBar;
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
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Hashtable;

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
 * <p>Copyright: Copyright (c) 2005-2015
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
public class ManAlbRuta extends ventanaPad implements PAD
{
    private boolean ARG_MODSALA=false;
    final int JT_NUMALB=3;
    final int JT_BULTOS=4;
    
    final int JT_CLICOD=5;
    final int JT_CLINOMB=6;
    final int JT_UNID=7;
    final int JT_KILOS=8;
    final int JT_REPET=9;
 
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
            setTitulo("Mantenimiento Albaranes de una ruta");
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
          setTitulo("Mantenimiento Albaranes de una ruta");
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
                ARG_MODSALA = Boolean.parseBoolean(ht.get("modSala").toString());
        }
    }
    private void jbInit() throws Exception {
        
        statusBar = new StatusBar(this);
        nav = new navegador(this, dtCons, false, navegador.NORMAL);
        
        iniciarFrame();
        this.setVersion("2016-01-09 "+(ARG_MODSALA?" Modo Sala ":""));
        
        strSql = "SELECT * FROM albrutacab "+
            (ARG_MODSALA?" where usu_nomb ='"+EU.usuario+"'":"")+
            " order by alr_nume";

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
    
    int cambiaLinJT(int row)
    {
        try
        {
          
            if (!avc_numeE.hasCambio() && !alr_bultosE.hasCambio())
                return -1;
            if (avc_numeE.hasCambio())
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
                mensajeErr("Albaran NO encontrado");
                limpiaLinea(row);
                return JT_NUMALB;
            }
            String s;
            if (avc_numeE.hasCambio())
                jt.setValor(false,row,JT_REPET);
            
            if (! jt.getValBoolean(row,JT_REPET) )
            {
                s="select * from v_albruta where emp_codi ="+emp_codiE.getValorInt()
                    + " and  avc_ano = " + avc_anoE.getValorInt()
                    + " and avc_serie ='" + avc_serieE.getText() + "'"
                    + " and avc_nume = " + avc_numeE.getValorInt()
                    + " and alr_nume != " + alr_numeE.getValorInt();
                if (dtStat.select(s))
                {
                    int res = mensajes.mensajeYesNo("Albaran ya se sirvio en ruta: " + dtStat.getInt("alr_nume")
                        + " de fecha: " + dtStat.getFecha("alr_fecha", "dd-MM-yyyy") + "\n Volver a cargar en ruta ?");
                    if (res != mensajes.YES)
                        return 0;
                    jt.setValor(true, row, JT_REPET);
                }
            }
         
            if (checkLineaRepe(emp_codiE.getValorInt(),avc_anoE.getValorInt(),avc_serieE.getText(),
                avc_numeE.getValorInt(),row ))
                return 0;
            avc_numeE.resetCambio();
            alr_bultosE.resetCambio();

            jt.setValor(dtCon1.getInt("cli_codi"),row,JT_CLICOD);
            String cliNomb=dtCon1.getString("avc_clinom").equals("")?
                pdclien.getNombreCliente(dtStat,dtCon1.getInt("cli_codi")):dtCon1.getString("avc_clinom");
            jt.setValor(cliNomb,row,JT_CLINOMB);
            jt.setValor(dtCon1.getDouble("avc_kilos"),row,JT_KILOS);
            jt.setValor(dtCon1.getInt("avc_unid"),row,JT_UNID);
            actAcumul();
        } catch (SQLException ex)
        {
            Error("Error al comprobar albaran para ruta", ex);
        }
        return -1;
    }
    private boolean  checkLineaRepe(int empCodi,int avcAno, String avcSerie,int avcNume, int row)
    {
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
            usu_nombE.setText(dtCon1.getString("usu_nomb"));
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
            jt.removeAllDatos();
             if (! dtCon1.select("select l.*,cl.cli_nomb from v_albruta as l "
                 + " left join v_cliente as cl "+
                " on l.cli_codi = cl.cli_codi where alr_nume ="+dtCons.getInt("alr_nume")+
                 " order by alr_orden"))
            {
                msgBox("No encontradas albaranes para parte ruta con ID: "+dtCons.getInt("alr_nume"));
                return;
            }
             
            do
            {
                ArrayList a=new ArrayList();
                a.add(dtCon1.getString("emp_codi"));
                a.add(dtCon1.getString("avc_ano"));
                a.add(dtCon1.getString("avc_serie"));
                a.add(dtCon1.getString("avc_nume"));
                a.add(dtCon1.getString("alr_bultos"));
                a.add(dtCon1.getString("cli_codi"));
                a.add(dtCon1.getString("avc_clinom",true).equals("")?dtCon1.getString("cli_nomb"):dtCon1.getString("avc_clinom",true));
  
                if (dtCon1.getObject("alr_unid")==null)
                { 
                    a.add(dtCon1.getString("avc_unid"));
                    a.add(dtCon1.getString("avc_kilos"));
                }
                else
                {
                    a.add(dtCon1.getString("alr_unid"));
                    a.add(dtCon1.getString("alr_kilos"));
                }
                a.add((dtCon1.getInt("alr_repet") != 0));
                jt.addLinea(a);                
            } while (dtCon1.next());
            jt.requestFocusInicio();
            actAcumul();
        } catch (SQLException ex)
        {
            Error("Error al comprobar albaran para ruta", ex);
        }
    }
    void actAcumul()
    {
        int nRow=jt.getRowCount();
        double kilos=0;
        int unid=0;
        int nAlb=0;
        int bultos=0;
        for (int n=0;n<nRow;n++)
        {
            if (jt.getValorInt(n,JT_NUMALB)==0)
                continue;
            nAlb++;
            bultos+=jt.getValorInt(n,JT_BULTOS);
            kilos+=jt.getValorDec(n,JT_KILOS);
            unid+=jt.getValorInt(n,JT_UNID);
        }
        kilosTotE.setValorDec(kilos);
        uniTotE.setValorInt(unid);
        numAlbE.setValorInt(nAlb);
        bulTotE.setValorDec(bultos);
    }
    @Override
  public void PADAddNew()
  {
    
    alr_numeE.setEnabled(false);
    mensaje("Insertar Nuevo Registro");
    
    Pcabe.resetTexto();
    alr_comentE.resetTexto();
    usu_nombE.setText(EU.usuario);
    alr_fechaE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
    alr_fecsalE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
    usu_nombE.setText(EU.usuario);
    veh_codiE.setText("");
    jt.removeAllDatos();
    activar(true);
    kilosTotE.setValorDec(0);
    uniTotE.setValorDec(0);
    numAlbE.setValorDec(0);
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
      if (!usu_nombE.getText().equals(EU.usuario) && ARG_MODSALA)
      {
        msgBox("No tiene permisos para editar este registro");
         nav.pulsado = navegador.NINGUNO;
        activaTodo();
        return;
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
     alr_numeE.setEnabled(false);
     alr_fechaE.resetCambio();
     avc_numeE.resetCambio();
     alr_bultosE.resetCambio();
     if (ARG_MODSALA)
     {
         jt.setEnabled(false);
         alr_fechaE.setEnabled(false);
         rut_codiE.setEnabled(false);
         alr_fecsalE.requestFocus();
     }
     else
        jt.requestFocusInicio();
     mensaje("MODIFICANDO registro activo ....");
  }
    @Override
   public void PADDelete()
   {
     try
     {
         if (ARG_MODSALA)
         {
             if (usu_nombE.getText().equals(EU.usuario))
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
             jt.salirGrid();
             if (cambiaLinJT(jt.getSelectedRow()) >= 0)
                 return;
                          
             for (int n = 0; n < nl; n++)
             {
                 if (jt.getValorInt(n, JT_NUMALB) == 0)
                     continue;

                 orden++;
             }
             if (orden == 0)
             {
                 msgBox("Introduzca algun albaran para la ruta");
                 return;
             }
         }
         dtAdd.edit();
         guardaCab(alr_numeE.getValorInt());
         // borro lineas e inserto las nuevas
         
         if (! ARG_MODSALA)
         {
             String s = "delete from albrutalin where alr_nume=" + alr_numeE.getValorInt();
             dtAdd.executeUpdate(s);
             orden = 1;
             for (int n = 0; n < nl; n++)
             {
                 if (jt.getValorInt(n, JT_NUMALB) == 0)
                     continue;
                 guardaLineas(alr_numeE.getValorInt(), orden, n);
                 orden++;
             }
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
        usu_nombE = new gnu.chu.controles.CLinkBox();
        cLabel8 = new gnu.chu.controles.CLabel();
        alr_vekminE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#,###,##9");
        cLabel9 = new gnu.chu.controles.CLabel();
        alr_vekmfiE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#,###,##9");
        cLabel10 = new gnu.chu.controles.CLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        alr_comentE = new gnu.chu.controles.CTextArea();
        cLabel11 = new gnu.chu.controles.CLabel();
        veh_codiE = new gnu.chu.controles.CLinkBox();
        cLabel15 = new gnu.chu.controles.CLabel();
        alr_numeE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###,##9");
        BInsAuto = new gnu.chu.controles.CButton(Iconos.getImageIcon("fill"));
        BirGrid = new gnu.chu.controles.CButton();
        PPie = new gnu.chu.controles.CPanel();
        cLabel12 = new gnu.chu.controles.CLabel();
        numAlbE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        uniTotE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        kilosTotE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###,##9.99");
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
        Tpanel1 = new gnu.chu.controles.CTabbedPane();
        jt = new gnu.chu.controles.CGridEditable(10){
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
                    if (fvc_anoE.getValorInt() > 0 && emp_codiE.getValorInt() > 0
                        && fvc_numeE.getValorInt() > 0)
                    {
                        if (buscaFac(fvc_anoE.getValorInt(), emp_codiE.getValorInt(),
                            fvc_serieE.getText(), fvc_numeE.getValorInt()))
                    {
                        jt.setValor(dtStat.getString("cli_codi"), row, 4);
                        jt.setValor(dtStat.getString("cli_nomb"), row, 5);
                        jt.setValor(dtStat.getFecha("fvc_fecfra", "dd-MM-yyyy"), row, 6);
                        jt.setValor(dtStat.getString("fvc_sumtot"), row, 7);
                        jt.setValor("" +
                            (dtStat.getDouble("fvc_sumtot") - dtStat.getDouble("fvc_impcob")),
                            row, 8);
                    }
                }
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
                return checkLinea(row);
            }
            catch (Exception k)
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

    cli_nombE.setEnabled(false);

    avc_unidE.setEnabled(false);

    avc_kilosE.setEnabled(false);

    alr_repetE.setEnabled(false);

    fvc_clicodE.setText("cTextField1");

    fvc_clinomE.setText("cTextField1");

    fvc_fecfraE.setText("cTextField1");

    fvc_sumtotE.setText("cTextField1");

    fvc_imppenE.setText("cTextField1");

    alr_bultosE.setText("1");

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    Pprinc.setLayout(new java.awt.GridBagLayout());

    Pcabe.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
    Pcabe.setMaximumSize(new java.awt.Dimension(560, 140));
    Pcabe.setMinimumSize(new java.awt.Dimension(560, 140));
    Pcabe.setPreferredSize(new java.awt.Dimension(560, 140));
    Pcabe.setLayout(null);

    cLabel5.setText("Fecha");
    cLabel5.setPreferredSize(new java.awt.Dimension(52, 18));
    Pcabe.add(cLabel5);
    cLabel5.setBounds(2, 2, 40, 18);

    alr_fechaE.setPreferredSize(new java.awt.Dimension(10, 18));
    Pcabe.add(alr_fechaE);
    alr_fechaE.setBounds(50, 2, 76, 18);
    alr_fechaE.getAccessibleContext().setAccessibleName("");

    cLabel6.setText("Salida Ruta");
    cLabel6.setPreferredSize(new java.awt.Dimension(52, 18));
    Pcabe.add(cLabel6);
    cLabel6.setBounds(2, 23, 70, 17);

    alr_fecsalE.setPreferredSize(new java.awt.Dimension(10, 18));
    Pcabe.add(alr_fecsalE);
    alr_fecsalE.setBounds(80, 23, 70, 18);

    alr_fecsalH.setText("0");
    Pcabe.add(alr_fecsalH);
    alr_fecsalH.setBounds(160, 23, 20, 18);

    alr_fecsalM.setText("0");
    Pcabe.add(alr_fecsalM);
    alr_fecsalM.setBounds(190, 23, 20, 18);

    cLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    cLabel1.setText(":");
    Pcabe.add(cLabel1);
    cLabel1.setBounds(180, 23, 10, 17);

    cLabel7.setText("Operario");
    cLabel7.setPreferredSize(new java.awt.Dimension(52, 18));
    Pcabe.add(cLabel7);
    cLabel7.setBounds(230, 2, 60, 18);

    alr_fecregE.setPreferredSize(new java.awt.Dimension(10, 18));
    Pcabe.add(alr_fecregE);
    alr_fecregE.setBounds(80, 45, 76, 18);

    alr_fecregH.setText("0");
    Pcabe.add(alr_fecregH);
    alr_fecregH.setBounds(160, 45, 20, 18);

    alr_fecregM.setText("0");
    Pcabe.add(alr_fecregM);
    alr_fecregM.setBounds(190, 45, 20, 18);

    cLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    cLabel2.setText(":");
    Pcabe.add(cLabel2);
    cLabel2.setBounds(180, 23, 10, 17);

    cLabel3.setText("Ruta");
    Pcabe.add(cLabel3);
    cLabel3.setBounds(230, 23, 40, 14);

    rut_codiE.setAncTexto(30);
    rut_codiE.setFormato(Types.CHAR, "XX");
    Pcabe.add(rut_codiE);
    rut_codiE.setBounds(290, 23, 210, 18);
    rut_codiE.getAccessibleContext().setAccessibleName("");

    cLabel4.setText("Vehiculo");
    Pcabe.add(cLabel4);
    cLabel4.setBounds(230, 45, 60, 14);

    usu_nombE.setAncTexto(100);
    usu_nombE.setFormato(Types.CHAR,"X",15);
    Pcabe.add(usu_nombE);
    usu_nombE.setBounds(290, 2, 260, 18);
    usu_nombE.getAccessibleContext().setAccessibleName("");

    cLabel8.setText("Km. Iniciales ");
    Pcabe.add(cLabel8);
    cLabel8.setBounds(390, 70, 80, 14);
    Pcabe.add(alr_vekminE);
    alr_vekminE.setBounds(480, 70, 70, 18);

    cLabel9.setText("Identificador");
    Pcabe.add(cLabel9);
    cLabel9.setBounds(390, 110, 80, 14);
    Pcabe.add(alr_vekmfiE);
    alr_vekmfiE.setBounds(480, 90, 70, 18);

    cLabel10.setText("Comentarios ");
    Pcabe.add(cLabel10);
    cLabel10.setBounds(10, 70, 80, 14);

    alr_comentE.setColumns(20);
    alr_comentE.setRows(5);
    jScrollPane2.setViewportView(alr_comentE);

    Pcabe.add(jScrollPane2);
    jScrollPane2.setBounds(90, 70, 290, 60);

    cLabel11.setText("Regreso Ruta");
    cLabel11.setPreferredSize(new java.awt.Dimension(52, 18));
    Pcabe.add(cLabel11);
    cLabel11.setBounds(2, 45, 80, 18);

    veh_codiE.setAncTexto(30);
    veh_codiE.setFormato(Types.DECIMAL, "##9");
    Pcabe.add(veh_codiE);
    veh_codiE.setBounds(290, 45, 210, 18);
    veh_codiE.getAccessibleContext().setAccessibleName("");

    cLabel15.setText("Km. Finales");
    Pcabe.add(cLabel15);
    cLabel15.setBounds(390, 90, 70, 14);
    Pcabe.add(alr_numeE);
    alr_numeE.setBounds(480, 110, 50, 18);

    BInsAuto.setToolTipText("Insertar Alb. Pend. de Ruta");
    Pcabe.add(BInsAuto);
    BInsAuto.setBounds(510, 23, 30, 24);
    Pcabe.add(BirGrid);
    BirGrid.setBounds(540, 120, 2, 2);

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

    cLabel12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    cLabel12.setText("Unid.");
    PPie.add(cLabel12);
    cLabel12.setBounds(260, 2, 40, 17);

    numAlbE.setEditable(false);
    PPie.add(numAlbE);
    numAlbE.setBounds(80, 2, 40, 17);

    uniTotE.setEditable(false);
    PPie.add(uniTotE);
    uniTotE.setBounds(300, 2, 40, 17);

    kilosTotE.setEditable(false);
    PPie.add(kilosTotE);
    kilosTotE.setBounds(190, 2, 60, 17);
    PPie.add(Baceptar);
    Baceptar.setBounds(350, 2, 90, 30);
    PPie.add(Bcancelar);
    Bcancelar.setBounds(445, 2, 95, 30);

    cLabel16.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    cLabel16.setText("Imp. Fras.");
    PPie.add(cLabel16);
    cLabel16.setBounds(120, 20, 60, 17);

    cLabel17.setText("Nº Facturas");
    PPie.add(cLabel17);
    cLabel17.setBounds(3, 20, 70, 14);

    cLabel18.setText("Nº Albaranes ");
    PPie.add(cLabel18);
    cLabel18.setBounds(0, 2, 80, 14);

    numFrasE.setEditable(false);
    PPie.add(numFrasE);
    numFrasE.setBounds(80, 20, 40, 17);

    cLabel19.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    cLabel19.setText("Kilos");
    PPie.add(cLabel19);
    cLabel19.setBounds(150, 2, 40, 17);

    impFrasE.setEditable(false);
    PPie.add(impFrasE);
    impFrasE.setBounds(180, 20, 70, 17);

    cLabel13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    cLabel13.setText("Bultos");
    PPie.add(cLabel13);
    cLabel13.setBounds(260, 20, 40, 17);

    bulTotE.setEditable(false);
    PPie.add(bulTotE);
    bulTotE.setBounds(300, 20, 40, 17);

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
    v.add("Emp"); // 0
    v.add("Ejer."); // 1
    v.add("Serie"); // 2
    v.add("Numero"); // 3
    v.add("Bultos"); // 4
    v.add("Cliente"); // 5
    v.add("Nombre Cliente"); // 6
    v.add("Unid."); // 7
    v.add("Kilos"); // 8
    v.add("Dupl."); // 9
    jt.setCabecera(v);
    jt.setColNueva(3);
    jt.setAnchoColumna(new int[]{30,40,30,60,40,50,200,40,50,45});
    jt.setAlinearColumna(new int[]{2,2,1,2,2,2,0,2,2,1});
    ArrayList vc=new ArrayList();
    vc.add(emp_codiE);
    vc.add(avc_anoE);
    vc.add(avc_serieE);
    vc.add(avc_numeE);
    vc.add(alr_bultosE);
    vc.add(cli_codiE);
    vc.add(cli_nombE);
    vc.add(avc_unidE);
    vc.add(avc_kilosE);
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
    jtFra.setColNueva(2);
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
    private gnu.chu.controles.CButton BInsAuto;
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CButton Bcancelar;
    private gnu.chu.controles.CButton BirGrid;
    private gnu.chu.controles.CPanel PPie;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CTabbedPane Tpanel1;
    private gnu.chu.controles.CTextField alr_bultosE;
    private gnu.chu.controles.CTextArea alr_comentE;
    private gnu.chu.controles.CTextField alr_fechaE;
    private gnu.chu.controles.CTextField alr_fecregE;
    private gnu.chu.controles.CTextField alr_fecregH;
    private gnu.chu.controles.CTextField alr_fecregM;
    private gnu.chu.controles.CTextField alr_fecsalE;
    private gnu.chu.controles.CTextField alr_fecsalH;
    private gnu.chu.controles.CTextField alr_fecsalM;
    private gnu.chu.controles.CTextField alr_numeE;
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
    private gnu.chu.controles.CLabel cLabel10;
    private gnu.chu.controles.CLabel cLabel11;
    private gnu.chu.controles.CLabel cLabel12;
    private gnu.chu.controles.CLabel cLabel13;
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
    private gnu.chu.controles.CTextField cli_nombE;
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
    private gnu.chu.controles.CLinkBox rut_codiE;
    private gnu.chu.controles.CTextField uniTotE;
    private gnu.chu.controles.CLinkBox usu_nombE;
    private gnu.chu.controles.CLinkBox veh_codiE;
    // End of variables declaration//GEN-END:variables
   @Override
    public void iniciarVentana() throws Exception {
        Pcabe.setAltButton(BirGrid);
        pdconfig.llenaDiscr(dtStat, rut_codiE, pdconfig.D_RUTAS ,EU.em_cod);
        String s="select usu_nomb,usu_nomco from usuarios where emp_codi= "+EU.em_cod+
            (ARG_MODSALA?" and usu_nomb='"+EU.usuario+"'":"")+
            " and usu_activ = 'S' "+
            " order by usu_nomb";
        if (dtCon1.select(s))
            usu_nombE.addDatos(dtCon1);
        usu_nombE.addDatos("Externo","Externo");
        s="select veh_codi,veh_nomb from vehiculos order by veh_nomb";
        if (dtCon1.select(s))
            veh_codiE.addDatos(dtCon1);
        if (ARG_MODSALA)
            usu_nombE.setEnabled(false);
        alr_numeE.setColumnaAlias("alr_nume");
        alr_fechaE.setColumnaAlias("alr_fecha");
        rut_codiE.setColumnaAlias("rut_codi");
        usu_nombE.setColumnaAlias("usu_nomb");
        veh_codiE.setColumnaAlias("veh_codi");
        alr_fecsalE.setColumnaAlias("alr_fecsal");
        alr_fecregE.setColumnaAlias("alr_fecreg");
        alr_vekminE.setColumnaAlias("alr_vekmin");
        alr_vekmfiE.setColumnaAlias("alr_vekmfi");
        Pcabe.setDefButton(Baceptar);
        jt.setDefButton(Baceptar);
        activarEventos();
        if (ARG_MODSALA && ! dtCons.getNOREG() )
                dtCons.last();
        verDatos();
    }
    void activarEventos()
    {
         jt.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
              if (e.getClickCount()<2 || jt.isEnabled() || jt.isVacio()) 
                  return;
              verDocumento();
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
                  if (jt.isEnabled())
                    jt.requestFocusInicioLater();
              }
         });
    }
    
    void insertarAuto()
    {
        try
        {
            if (! jt.isVacio())
            {
                jt.salirGrid();
                if (cambiaLinJT(jt.getSelectedRow())>0)                
                    return;
            }
            if (rut_codiE.isNull())
            {
                mensajeErr("Inserte primero la ruta");
                return;
            }
            if (alr_fechaE.isNull())
            {
                mensajeErr("Inserte primero la fecha de registro");
                return;
            }
            String fecIni=Formatear.getFecha(Formatear.sumaDiasDate(alr_fechaE.getDate(),-7),"yyyy-MM-dd");
            String s="select c.avc_id,c.emp_codi,c.avc_ano,c.avc_serie,c.avc_nume,"
                + "c.cli_codi,c.avc_clinom,avc_kilos,avc_unid,cl.cli_nomb from v_albavec as c,v_cliente as cl where c.cli_codi = cl.cli_codi and cl.rut_codi = '"+rut_codiE.getText()+"'"+
                " and c.avc_fecalb between '"+fecIni+"' and '"+alr_fechaE.getFechaDB()+"'";
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
                ArrayList a=new ArrayList();
                a.add(dtCon1.getString("emp_codi"));
                a.add(dtCon1.getString("avc_ano"));
                a.add(dtCon1.getString("avc_serie"));
                a.add(dtCon1.getString("avc_nume"));
                a.add(dtCon1.getString("cli_codi"));
                a.add(dtCon1.getString("avc_clinom",true).equals("")?dtCon1.getString("cli_nomb"):dtCon1.getString("avc_clinom",true));
                
                a.add(dtCon1.getString("avc_unid"));
                a.add(dtCon1.getString("avc_kilos"));
                a.add(false);
                jt.addLinea(a);  
            } while (dtCon1.next());
            jt.setEnabled(true);
            jt.requestFocusFinalLater();
            msgBox("Albaranes de ruta cargados");
        } catch (ParseException | SQLException ex)
        {
            Error("Error al buscar albaranes para ruta",ex);
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
      v.add(usu_nombE.getStrQuery());
      v.add(alr_fechaE.getStrQuery());
      v.add(alr_fecsalE.getStrQuery());
      v.add(alr_fecregE.getStrQuery());
      v.add(veh_codiE.getStrQuery());
      v.add(alr_vekminE.getStrQuery());
      v.add(alr_vekmfiE.getStrQuery());
      String s = "select * from albrutacab ";

      s = creaWhere(s, v,  true);
      s += " ORDER BY alr_fecha ";

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
            jt.salirGrid();
            if (cambiaLinJT(jt.getSelectedRow())>=0)
                return;
            int nl=jt.getRowCount();
            int orden=0;
            for (int n=0;n<nl;n++)
            {
                if (jt.getValorInt(n,JT_NUMALB)==0)
                    continue;
                
                orden++;
            }
            if (orden==0)
            {
                msgBox("Introduzca algun albaran para la ruta");
                return;
            }
            dtAdd.addNew("albrutacab", false);
            int id = guardaCab(0);
            
             orden=1;
            for (int n=0;n<nl;n++)
            {
                if (jt.getValorInt(n,JT_NUMALB)==0)
                    continue;
                guardaLineas(id,orden,n);
                orden++;
            }
            dtAdd.commit();
            mensajeErr("Albaranes de ruta.. guardados");
            activaTodo();
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
      
        dtAdd.setDato("rut_codi",rut_codiE.getText());
        dtAdd.setDato("usu_nomb",usu_nombE.getText());
        dtAdd.setDato("alr_fecha",alr_fechaE.getDate());
        if (! alr_fecsalE.isNull())
            dtAdd.setDato("alr_fecsal","{ts '"+alr_fecsalE.getFecha("yyyy-MM-dd")+" "+
                alr_fecsalH.getText()+":"+alr_fecsalM.getText()+"'}");
        if (! alr_fecregE.isNull())
            dtAdd.setDato("alr_fecreg","{ts '"+alr_fecregE.getFecha("yyyy-MM-dd")+" "+
                alr_fecregH.getText()+":"+alr_fecregM.getText()+"'}");
        dtAdd.setDato("rut_codi",rut_codiE.getText());
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
        dtAdd.setDato("alr_kilos",jt.getValorDec(nlGrid,JT_KILOS));
        dtAdd.setDato("alr_unid",jt.getValorDec(nlGrid,JT_UNID));
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
        if (!rut_codiE.controla())
        {
            mensajeErr("Introduzca ruta");
            return false;
        }
        if (usu_nombE.isNull())
        {
            mensajeErr("Usuario NO es valido");
            usu_nombE.requestFocus();
            return false;
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
        Pcabe.setEnabled(b);
        
        if (opcion!=navegador.QUERY)
        {
            alr_fecsalH.setEnabled(b);
            alr_fecregH.setEnabled(b);
            alr_fecsalM.setEnabled(b);
            alr_fecregM.setEnabled(b);
            BInsAuto.setEnabled(b);
        }
        alr_numeE.setEnabled(b);
               
        alr_fechaE.setEnabled(b);
        rut_codiE.setEnabled(b);        
        usu_nombE.setColumnaAlias("usu_nomb");
        veh_codiE.setColumnaAlias("veh_codi");
        
        alr_comentE.setEnabled(b);
        Baceptar.setEnabled(b);
        Bcancelar.setEnabled(b);
    }
     void calcSumFras()
  {
    int nRow=jt.getRowCount();
    int nFras=0;
    double impFras = 0;

    for (int n=0;n<nRow;n++)
    {
      if (jt.getValorInt(n, 2) == 0)
        continue;
      nFras++;
      impFras+=jt.getValorDec(n,7);
    }
    numFrasE.setValorDec(nFras);
    impFrasE.setValorDec(impFras);

  }
  boolean buscaFac(int ejeNume,int empCodi,String serie,int numFra) throws Exception
  {
    String s="SELECT f.*,cli_nomb FROM v_facvec as f,clientes as cl "+
       "  WHERE f.emp_codi = "+empCodi+
        " and f.fvc_ano = "+ejeNume+
        " and f.fvc_nume = "+numFra+
        " and f.fvc_serie = '"+serie+"'"+
        " and f.cli_codi = cl.cli_codi ";
    return dtStat.select(s);
  }
  int checkLinea(int row) throws Exception
  {
    if (fvc_numeE.getValorInt()==0)
      return -1;
    if (!buscaFac(fvc_anoE.getValorInt(), emp_codiE.getValorInt(),
                  fvc_serieE.getText(), fvc_numeE.getValorInt()))
    {
      mensajeErr("Factura NO ENCONTRADA");
      return 0;
    }
    if (dtStat.getDouble("fvc_cobrad")==-1)
    {
      mensajeErr("Factura YA esta cobrada");
      return 0;
    }
    return -1;
  }
}
