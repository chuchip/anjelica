package gnu.chu.anjelica.ventas.represen;

import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.listados.Listados;
import gnu.chu.anjelica.pad.MantRepres;
import gnu.chu.anjelica.pad.MantTarifa;
import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.anjelica.pad.pdtipotar;
import gnu.chu.anjelica.ventas.MantPrAlb;
import gnu.chu.anjelica.ventas.pdalbara;
import gnu.chu.controles.StatusBar;
import gnu.chu.eventos.CambioEvent;
import gnu.chu.eventos.CambioListener;
import gnu.chu.eventos.GridAdapter;
import gnu.chu.eventos.GridEvent;
import gnu.chu.interfaces.ejecutable;
import gnu.chu.sql.DatosTabla;
import gnu.chu.sql.conexion;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.Iconos;
import gnu.chu.utilidades.mensajes;
import gnu.chu.utilidades.ventana;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

/*
 *<p>Titulo: CLVenRep </p>
 * <p>Descripción: Consulta Listado Ventas a Representantes</p>
 * Este programa saca los margenes sobre el precio de tarifa entre unas fechas
 * y para una zona/Representante dada.
 * Tambien permite sacar una relacion de los albaranes, que no tienen precio de tarifa
 * puestos, dando la opción de actualizarlos.
 * Created on 03-dic-2009, 22:41:09
 *
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
 */
public class CLVenRep extends ventana {
    private boolean conComentarios=false;
    private boolean conIncPrecio=false;
    
    final  int JTCAB_EJER=0;
    final  int JTCAB_SERIE=1;
    final  int JTCAB_NUMALB=2;
    final  int JTCAB_FECALB=3;
    final  int JTCAB_CLICOD=4;
    final  int JTCAB_CLINOMB=5;
    final  int JTCAB_TARIFA=10;
    final  int JTLIN_PROCODI=0;
    final  int  JTLIN_PRECIO=4;    
    final  int  JTLIN_PRMIN=5;    
    
    final int JTLIN_GANA=6;
    final  int JTLIN_PRTAR=8;
    boolean swGridFoco=false;
    String condAlb;
    int idAlbaran;
    String ARG_ZONAREP = "";
    boolean ARG_MODIF=false;
    DatosTabla dtAdd;
  

    public CLVenRep(EntornoUsuario eu, Principal p) {
        this(eu, p, null);
    }

    public CLVenRep(EntornoUsuario eu, Principal p, Hashtable<String,String> ht) {
        EU = eu;
        vl = p.panel1;
        jf = p;
        eje = true;

        try {
            if (ht != null) {
                if (ht.get("zonaRep") != null) 
                    ARG_ZONAREP = ht.get("zonaRep");
               if (ht.get("modif") != null)
                    ARG_MODIF = Boolean.parseBoolean(ht.get("modif"));
            }
            setTitulo("Consulta Ventas Representantes");
            setAcronimo("covere");
            if (jf.gestor.apuntar(this)) {
                jbInit();
            } else {
                setErrorInit(true);
            }
        } catch (Exception e) {
           ErrorInit(e);
        }
    }

    public CLVenRep(gnu.chu.anjelica.menu p, EntornoUsuario eu, Hashtable<String,String> ht) {
        EU = eu;
        vl = p.getLayeredPane();
        eje = false;

        try {
            if (ht != null) {
                if (ht.get("zonaRep") != null) {
                    ARG_ZONAREP = ht.get("zonaRep");
                }
                if (ht.get("modif") != null)
                    ARG_MODIF = Boolean.parseBoolean(ht.get("modif"));
            }
            setTitulo("Consuta Ventas Representantes");

            jbInit();
        } catch (Exception e) {
           ErrorInit(e);
        }
    }

    private void jbInit() throws Exception {
        statusBar = new StatusBar(this);

        iniciarFrame();

        this.setVersion("2017-10-29" + ARG_ZONAREP);

        initComponents();
        this.setSize(new Dimension(730, 535));
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);

        conecta();
    }

    @Override
    public void iniciarVentana() throws Exception {
        if (! ARG_MODIF)
            avl_prtariE.setEnabled(false);
        bdisc.iniciar(dtStat, this, vl, EU);
                
        sbe_codiE.iniciar(dtStat, this, vl, EU);
        fecIniE.setText(Formatear.sumaDias(Formatear.getDateAct(), -15));
        fecFinE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
       
        emp_codiE.iniciar(dtStat, this, vl, EU);
        emp_codiE.setAceptaNulo(false);
        sbe_codiE.setValorInt(0);
        rep_codiE.setFormato(Types.CHAR, "XX",2);
        
        if (! ARG_ZONAREP.equals(""))
        {
            rep_codiE.addDatos(ARG_ZONAREP, MantRepres.getNombRepr(ARG_ZONAREP,dtCon1));
            rep_codiE.setText(ARG_ZONAREP);
        }
        else
              MantRepres.llenaLinkBox(rep_codiE, dtCon1);
        rut_codiE.setAncTexto(30);
        rut_codiE.setFormato(Types.CHAR, "XX");       
        zon_codiE.setAncTexto(30);
        zon_codiE.setFormato(Types.CHAR, "XX");        
        pdconfig.llenaDiscr(dtStat, rut_codiE, pdconfig.D_RUTAS, EU.em_cod);
        pdconfig.llenaDiscr(dtStat, zon_codiE, pdconfig.D_ZONA, EU.em_cod);
        dtAdd = new DatosTabla(new conexion(EU));
        Pcondic.setDefButton(Baceptar.getBotonAccion());
        jtCab.setButton(KeyEvent.VK_F2, BirGrid);
        jtLin.setButton(KeyEvent.VK_F2, BirGrid);
        jtLin.setButton(KeyEvent.VK_F5, BFill);
        jtLin.setButton(KeyEvent.VK_F9, Btarifa);
        Presumen.setButton(KeyEvent.VK_F2, BirGrid);
        activarEventos();
    }

    private void activarEventos() {
        Btarifa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               if (jtLin.isVacio())
                   return;
               try
               {
                   double prTari = MantTarifa.getPrecTar(dtStat, jtLin.getValorInt(JTLIN_PROCODI),
                       jtCab.getValorInt(JTCAB_CLICOD),
                     jtCab.getValorInt(JTCAB_TARIFA), Formatear.getFecha(jtCab.getValDate(JTCAB_FECALB),"dd-MM-yyyy"));
                   String s = "UPDATE  v_albavel set tar_preci = " + Formatear.redondea(prTari, 2)
                        + " where pro_codi = " + jtLin.getValorInt(JTLIN_PROCODI)+ " and "+
                       condAlb
                        + " and avl_numlin in (" + jtLin.getValString(7) + ")";
                   dtAdd.executeUpdate(s);
                   dtAdd.commit();
                   jtLin.setValor(prTari,JTLIN_PRTAR);
                   jtLin.requestFocusSelectedLater();
               } catch (ParseException | SQLException k)
               {
                   Error("Error al actualizar tarifa",k);
               }
            }
        });
        BirGrid.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (swGridFoco)
                {
                    jtCab.requestFocusSelectedLater();                    
                }
                else
                {
                    jtLin.requestFocusLater(0,5);                    
                }
                swGridFoco=!swGridFoco;
            }
        });
        BFill.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try
                {
                    if (jtLin.isVacio() || jtLin.getValorDec(JTLIN_PRTAR)==0) 
                        return;                    
                    double prMin,comRep;
                    
                    if (jtLin.getValorDec(JTLIN_PRTAR)>jtLin.getValorDec(JTLIN_PRECIO) )
                    { // Precio tarifa superior o igual a precio venta
                        msgBox("Precio por debajo tarifa");
                        jtLin.requestFocusSelectedLater();
                        return;
                    }
                    else
                    {
                        comRep= pdtipotar.getComisionTarifaInf(dtStat,jtCab.getValorInt(JTCAB_TARIFA));
                        if (comRep==0 )
                        {
                            msgBox("NO ENCONTARADO TIPO TARIFA");
                            jtLin.requestFocusSelectedLater();
                            return;
                        }
                        prMin=jtLin.getValorDec(JTLIN_PRECIO)-jtLin.getValorDec(JTLIN_PRTAR)+comRep; 
                        prMin=jtLin.getValorDec(JTLIN_PRECIO)-prMin;
                    }                                      
                    jtLin.setValor(prMin, JTLIN_PRMIN); 
                    avl_proferE.setValorDec(prMin);
                    jtLin.setValor(jtLin.getValorDec(JTLIN_PRECIO)-
                        prMin,JTLIN_GANA );
                    jtLin.requestFocusSelectedLater();
                } catch (SQLException ex)
                {
                    Error("Error al buscar precio tarifa",ex);
                }
            }
        });
        Baceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              conComentarios=false;
              conIncPrecio=false;
              String indice=Baceptar.getValor(e.getActionCommand()); 
              switch (indice)
              {
                  case "I":                
                    imprimir();
                    break;
                  case "T":
                      consultaSinTarifa();
                      break;
                  case "A":
                      actualTarifa(false);
                      break;
                  case "A!":
                      actualTarifa(true);
                      break;
                  case "P":
                      actualPrecioMin(false);
                      break;
                   case "P!":
                      actualPrecioMin(true);
                      break;
                   case "!":
                       conComentarios=true;
                       consultar(false);
                       break;                
                  default:                   
                       consultar(indice.equals("S"));
               }
            }
        });
        jtCab.addMouseListener(new MouseAdapter()
        {
              @Override
              public void mouseClicked(MouseEvent e) {
                  if (e.getClickCount()<2 || jtCab.isVacio())
                      return;
                  swGridFoco=false;
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

                  cm.setSerieAlbaran(jtCab.getValString(1));
                  cm.setEmpresaAlbaran(emp_codiE.getValorInt());
                  cm.setNumeroAlbaran(jtCab.getValorInt(2));
                  cm.setEjercAlbaran(jtCab.getValorInt(0));

                  cm.ej_query();

                  jf.gestor.ir(cm);

              }

});
        jtCab.tableView.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() || !jtCab.isEnabled()) {
                    return;
                }
                swGridFoco=false;
                guardaCambios(jtLin.getSelectedRow());
                verLineas();
                jtCab.requestFocusSelectedLater();
            }
        });
        
        jtLin.addGridListener(new GridAdapter()
        {
             @Override
            public void cambioColumna(GridEvent event)   { 
                int row=event.getLinea();
                if (event.getColumna()!=JTLIN_PRMIN)
                    return;
                jtLin.setValor(jtLin.getValorDec(row,JTLIN_PRECIO)-
                   avl_proferE.getValorDec(),row,JTLIN_GANA );
            }

            @Override
            public void cambiaLinea(GridEvent event){
                swGridFoco=true;
                int ret=jtLinCambiaLinea(event.getLinea(), event.getColumna());
                event.setColError(ret);
                 try
                {
                    if (ret==-1)
                        verComentario();
                } catch (SQLException k)
                {
                    Error("Error al ver Comentario sobre comisiones representante", k);
                }
            }
            @Override
            public void afterCambiaLinea(GridEvent event)
            {                
                if (jtLin.isVacio() || ARG_MODIF)
                    return;
                try
                {
                    verComentario();
                } catch (SQLException k)
                {
                    Error("Error al ver Comentario sobre comisiones representante", k);
                }
            }
        });
        emp_codiE.addCambioListener(new CambioListener() {

            @Override
            public void cambio(CambioEvent event) {
                try {
                    pdconfig.llenaDiscr(dtCon1, rep_codiE, "CR", emp_codiE.getValorInt());
                } catch (SQLException k) {
                    Error("Error al cargar discriminadores", k);
                }
            }
        });
    }
    

    String getCondWhere() throws ParseException {
        return " and a.avc_fecalb between TO_DATE('" + fecIniE.getFecha("dd-MM-yyyy")
                + "','dd-MM-yyyy') and TO_DATE('" + fecFinE.getFecha("dd-MM-yyyy")
                + "','dd-MM-yyyy') "
                + " and cl.cli_codi = a.cli_codi "
                + "and "+(opReprCli.isSelected()?"cl.rep_codi = ":"a.avc_repres = ") +"'"+ rep_codiE.getText() + "'"
                + (sbe_codiE.getValorInt()==0?"": " and cl.sbe_codi="+sbe_codiE.getValorInt())
                + (zon_codiE.isNull()?"": " and cl.zon_codi='"+zon_codiE.getText()+"'")
                + (rut_codiE.isNull()?"": " and cl.cli_ruta='"+rut_codiE.getText()+"'")
                + (opIncCobr.isSelected() ? " and avc_cobrad != 0" : "");
    }
     /**
      * Actualiza las tarifas
      * @param Forzar actualizar tarifa aunque ya tenga precios.
      * @throws SQLException
      */
    void actualTarifa(boolean swForzar) 
    {
        try {
             limpiarGrid();
            if (!checkCond()) 
                return;
            
            String s = "select  cl.cli_codi,cl.tar_codi,a.avc_fecalb,a.pro_codi "
                + " from v_albventa as a,clientes as cl "
                + " WHERE  a.emp_codi = " + emp_codiE.getValorInt()
                + getCondWhere()             
                +(swForzar?"": " and a.tar_preci = 0")                
                + " group by cl.cli_codi,tar_codi,avc_fecalb, pro_codi";
        if (!dtCon1.select(s)) {
            msgBox("No encontrados albaranes para estos criterios");
            return; // No hay albaranes sin valorar
        }
        int nLinAct = 0;
        double prTari;
        do {
            prTari = MantTarifa.getPrecTar(dtStat, dtCon1.getInt("pro_codi"),dtCon1.getInt("cli_codi"),
                    dtCon1.getInt("tar_codi"), dtCon1.getFecha("avc_fecalb", "dd-MM-yyyy"));
            if (prTari != 0 || swForzar) 
            {
                s = "UPDATE  v_albavel set tar_preci = " + Formatear.redondea(prTari, 2)
                        + " where   emp_codi = " + emp_codiE.getValorInt()
                        + " and pro_codi = " + dtCon1.getInt("pro_codi")
                        + (swForzar?"":" and tar_preci = 0")+
                        " and exists (select * from v_albavec as a,clientes as cl  where " +
                        " avc_fecalb = to_date('" + dtCon1.getFecha("avc_fecalb") + "','dd-MM-yyyy')"
                        + " and  a.emp_codi = " + emp_codiE.getValorInt()
                        + " and a.avc_ano = v_albavel.avc_ano  "
                        + " and a.avc_serie = v_albavel.avc_serie "
                        + " and a.avc_nume = v_albavel.avc_nume"
                        + " and  cl.cli_codi = a.cli_codi"
                        +" and tar_codi = '" + dtCon1.getInt("tar_codi") + "')";
                dtAdd.executeUpdate(s);
                nLinAct++;
            }
        } while (dtCon1.next());
          if (nLinAct > 0)
          {
             dtAdd.commit();
             msgBox("Actualizadas tarifas en " + nLinAct + " Elementos");
          }
          else
              msgBox("No encontradas nuevas lineas para introducir tarifas");
        } catch (SQLException k)
        {
            Error("Error al Actualizar tarifas",k);            
        } catch (ParseException ex)
        {
             Error("Error al Actualizar tarifas.",ex);  
        }
       
    }
    void limpiarGrid()
    {
        guardaCambios(jtLin.getSelectedRow());
        jtLin.setEnabled(false);
        jtCab.removeAllDatos();
        jtLin.removeAllDatos();
    }
    void actualPrecioMin(boolean swForzar) 
    {
        try {
            limpiarGrid();
            
            if (!checkCond()) 
                return;
            if (swForzar)
            {
                int ret=mensajes.mensajePreguntar("Seguro que desea sobreescribir TODOS los precios minimos?");
                if (ret!=mensajes.YES)
                    return;
            }
            PreparedStatement ps=dtAdd.getPreparedStatement("update v_albavel set avl_profer= ? where"+
                " emp_codi = "+emp_codiE.getValorInt()+
                " and avc_ano = ? "+
                " and avc_serie = ?"+
                " and avc_nume = ?"+                
                " and avl_numlin = ?");
            
            String s="select a.*,tar_comrep,tar_corein from v_albventa as a,v_cliente as cl, tipotari as ta "           
                + " WHERE   "
                + " cl.tar_codi = ta.tar_codi "
                + " and tar_preci>0 "
                +(swForzar?"": " and avl_profer < 0" )               
                + " and  a.emp_codi = " + emp_codiE.getValorInt()+            
                getCondWhere();                       
            if (!dtCon1.select(s,true))
            {
                msgBox("NO existen albaranes  sin precios minimos y precio de tarifas");
                return;
            }
            double nLinAct=0;
            double comRep,prMin;
            do
            {
                     if (dtCon1.getDouble("tar_preci")>dtCon1.getDouble("avl_prven") )
                    { // Precio tarifa superior o igual a precio venta
                        comRep=dtCon1.getDouble("tar_corein");
                        if (comRep==0)
                            continue; 
                        prMin=dtCon1.getDouble("avl_prven")-comRep;
                    }
                    else
                    {
                        comRep= dtCon1.getDouble("tar_corein");
                        if (comRep==0 )
                            continue; 
                        prMin=dtCon1.getDouble("avl_prven")-
                            ((dtCon1.getDouble("avl_prven")-dtCon1.getDouble("tar_preci"))+comRep);
                    }                                      
                    ps.setDouble(1, prMin);
                    ps.setInt(2, dtCon1.getInt("avc_ano"));
                    ps.setString(3, dtCon1.getString("avc_serie"));
                    ps.setInt(4, dtCon1.getInt("avc_nume"));
                    ps.setInt(5, dtCon1.getInt("avl_numlin"));
                    ps.executeUpdate();
                    nLinAct++;
            } while (dtCon1.next());

                dtAdd.commit();
                msgBox("Actualizados Precios minimos en " + nLinAct + " lineas");
        } catch (SQLException k)
        {
            Error("Error al Actualizar Precios Minimos",k);            
        } catch (ParseException ex)
        {
             Error("Error al Actualizar Precios Minimos.",ex);  
        }       
    }
    
    String getStrSql(boolean sinPrecMini,String condWhere)
    {
        String s = "SELECT a.avc_ano, a.avc_serie, a.avc_nume,a.avc_fecalb,a.cli_codi, "
                    + " cl.cli_nomb,avc_impalb,avc_impcob,cl.tar_codi,avc_kilos  "
                    + "  FROM v_albavec as a,clientes as cl "
                    + " WHERE 1=1 "+
                    bdisc.getCondWhere("cl")+
                    condWhere;
        if (conComentarios)
                s += " and exists (select *  FROM comision_represent as cr "
                        + " WHERE cr.avc_id = a.avc_id )";
        
        if (!gananOpcC.getValor().equals("")) {
                s += " and exists (select *  FROM v_albavel as l "
                        + " WHERE l.emp_codi = " + emp_codiE.getValorInt()
                        + " and a.avc_ano = l.avc_ano  "
                        + " and a.avc_serie = l.avc_serie "
                        + " and a.avc_nume = l.avc_nume "
                        + (gananOpcC.getValor().equals(">")?"  and (avl_prven-avl_profer) > "+gananMinE.getValorDec():"")
                        +(gananOpcC.getValor().equals("<")?"  and (avl_prven-avl_profer) < "+gananMinE.getValorDec():"")                        
                        + (gananOpcC.getValor().equals("E")?"  and (avl_prven-avl_profer)  between  "+
                            gananMinE.getValorDec() +" and "+gananMaxE.getValorDec():"")
                        + " and l.avl_canti <> 0 "
                        + " )";
        }
        if (sinPrecMini) {
                s += " and exists (select *  FROM v_albavel as l "
                        + " WHERE l.emp_codi = " + emp_codiE.getValorInt()
                        + " and a.avc_ano = l.avc_ano  "
                        + " and a.avc_serie = l.avc_serie "
                        + " and a.avc_nume = l.avc_nume "
                        + " and l.avl_profer <= 0"
                        + " and l.avl_canti <> 0 "
                        + " )";
        }
        s += " ORDER BY  avc_fecalb,avc_ano,avc_serie,avc_nume ";
        return s;
    }
    
    private void consultaSinTarifa() 
    {   
        try
        {
            if (!checkCond()) 
                return;
            guardaCambios(jtLin.getSelectedRow());
            String s = "select a.pro_codi,a.pro_nomb,sum(avl_canti) as avl_canti,"
                + "sum(avl_unid) as avl_unid,sum(avl_prven*avl_canti) as importe from v_albventa as a,clientes as cl "
                + " WHERE  a.emp_codi = " + emp_codiE.getValorInt()
                + getCondWhere()                   
                + " and a.tar_preci = 0"               
                + " group by  pro_codi,pro_nomb"
                + " order by pro_codi ";   
            jtCab.setEnabled(false);
            jtCab.removeAllDatos();
            jtLin.setEnabled(false);
            jtLin.removeAllDatos();
            if (!dtCon1.select(s))
            {
                msgBox("No hay articulos sin tarifa para estas condiciones");
                return;
            }
            do
            {
                ArrayList v = new ArrayList();
                v.add(dtCon1.getString("pro_codi"));
                v.add(dtCon1.getString("pro_nomb"));
                v.add(dtCon1.getString("avl_unid"));
                v.add(dtCon1.getString("avl_canti"));
                v.add(dtCon1.getDouble("importe")/dtCon1.getDouble("avl_canti"));               
                v.add("");
                v.add("");
                v.add("");
                v.add("");
                jtLin.addLinea(v); 
            } while (dtCon1.next());
            jtLin.requestFocusInicio();
        } catch (Exception ex)
        {
            Error("Error al buscar articulos sin tarifa",ex);
        }
    }
    /**
     * Consulta los albaranes de ese representante. Permitiendo modificar el precio oferta (Precio Minimo Venta)
     * 
     */
    void consultar(boolean sinPrecMinimo) {
        guardaCambios(jtLin.getSelectedRow());
        PreparedStatement ps;
        ResultSet rs;
        try {          
            jtCab.setEnabled(false);
            jtCab.removeAllDatos();
            String condWhere = getCondWhere();
            String s=getStrSql(sinPrecMinimo,condWhere);
          
            if (!dtCon1.select(s)) {
                msgBox("No encontrados albaranes con esos criterios");
                fecIniE.requestFocus();
                return;
            }
            
            s = "  SELECT sum(avl_canti*(avl_prven-avl_profer)) as gananc   "
                 + "  FROM v_albavel  "
                 + " WHERE emp_codi = " + emp_codiE.getValorInt()
                 + " and avc_ano = ?"
                 + " and avc_serie = ? "
                 + " and avc_nume = ?"
                 + " and avl_profer > 0 and avl_prbase > 0";
            ps = dtStat.getPreparedStatement(s);
            double TimpAlb = 0, TkilAlb = 0,  TimpGan = 0, ganAlb;
            int TnumAlb = 0;
            do {
                ArrayList v = new ArrayList();
                v.add(dtCon1.getString("avc_ano"));
                v.add(dtCon1.getString("avc_serie"));
                v.add(dtCon1.getString("avc_nume"));
                v.add(dtCon1.getFecha("avc_fecalb", "dd-MM-yy"));
                v.add(dtCon1.getString("cli_codi"));
                v.add(dtCon1.getString("cli_nomb"));
                v.add(dtCon1.getString("avc_impalb"));
                v.add(dtCon1.getString("avc_impcob"));
                
                ps.setInt(1, dtCon1.getInt("avc_ano"));
                ps.setString(2, dtCon1.getString("avc_serie"));
                ps.setInt(3, dtCon1.getInt("avc_nume"));                
               
                TnumAlb++;
                TimpAlb += dtCon1.getDouble("avc_impalb");

                ganAlb = 0;
               
                rs = ps.executeQuery();
                rs.next();
                if (rs.getObject("gananc") != null) {
                    ganAlb = rs.getDouble("gananc");                   
                }
               
                TkilAlb += dtCon1.getDouble("avc_kilos");
                TimpGan += ganAlb;
                v.add(dtCon1.getDouble("avc_kilos"));
                v.add( ganAlb); // Imp.Ganancia
                v.add(dtCon1.getString("tar_codi"));
                rs.next();
                jtCab.addLinea(v);
            } while (dtCon1.next());
            impAlbE.setValorDec(TimpAlb);
            impGanE.setValorDec(TimpGan);
            kilAlbE.setValorDec(TkilAlb);
            numAlbE.setValorInt(TnumAlb);

//        jtCab.requestFocusInicio();
            jtCab.setEnabled(true);
            jtCab.requestFocusInicio();
            verLineas();
        } catch (Exception k) {
            Error("Error al comprobar condiciones al buscar Albaranes", k);
        }
    }
    public void guardaCambios(int row)
    {
       jtLinCambiaLinea(row,0);
    }
    public int jtLinCambiaLinea(int row, int col) {
        try
        {
            guardaComentario(idAlbaran, jtLin.getValString(row, 7), cor_comentE.getText().trim(), cor_comresE.getText().trim());
            if (!avl_proferE.isEnabled() || jtLin.isVacio() || jtLin.getValString(row, 5).equals("") || jtLin.getValString(row, 7).trim().equals(""))
                return -1;
            double avlPrven = avl_proferE.getValorDec();
            
            String s = "UPDATE  V_albavel set avl_profer = " + avlPrven
                + " where " + condAlb
                + " and avl_numlin in (" + jtLin.getValString(row, 7) + ")";

            int nregAfe = dtAdd.executeUpdate(s);
            if (nregAfe == 0)
                msgBox("No se registro el precio. Revise por favor");
            dtAdd.commit();
        } catch (SQLException k)
        {
            Error("Error al actualizar linea", k);
        }
        mensajeErr("Precio Linea Actualizada", false);
        return -1;
    }
    private void getCondAlb(int row)
    {
        try
        {
            condAlb= "  emp_codi = " + emp_codiE.getValorInt()
                + " and avc_ano = " + jtCab.getValorInt(row,0)
                + " and avc_nume = " + jtCab.getValorInt(row,2)
                + " and avc_serie = '" + jtCab.getValString(row,1) + "'";
            idAlbaran=pdalbara.getIdAlbaran(dtStat, jtCab.getValorInt(row,0), emp_codiE.getValorInt(),
                jtCab.getValString(row,1),
                jtCab.getValorInt(row,2));
        } catch (SQLException ex)
        {
            Error("Error al localizar id albaran",ex);
        }
    }
    
    void guardaComentario(int avcId, String lineas,String coment,String comResp)  throws SQLException
    {
        String s="select * from comision_represent where avc_id = "+avcId+
            " and cor_linea='"+lineas+"'";
        if (! dtAdd.select(s,true))
        {
            if (coment.equals(""))
                return; 
            dtAdd.addNew("comision_represent");
            dtAdd.setDato("avc_id",avcId);
            dtAdd.setDato("cor_linea",lineas);            
        }
        else
        {
            if (coment.equals(""))
            {
                dtAdd.delete();
                dtAdd.commit();
                return; 
            }
            dtAdd.edit();
        }
        dtAdd.setDato("cor_coment",coment);
        dtAdd.setDato("cor_comres",comResp);
        dtAdd.update();
        dtAdd.commit();
    }
    
    void verComentario() throws SQLException
    {
        verComentario(idAlbaran, jtLin.getValString(7));
    }
    void verComentario(int avcId, String lineas) throws SQLException
    {
       String s="select * from comision_represent where avc_id = "+avcId+
            " and cor_linea='"+lineas+"'";
        if (! dtCon1.select(s))
        {
           cor_comentE.resetTexto();
           cor_comresE.resetTexto();
        }
        else
        {
            cor_comentE.setText(dtCon1.getString("cor_coment"));
            cor_comresE.setText(dtCon1.getString("cor_comres",true));
        }
       
    }
    void verPedido() throws SQLException
    {
       jtPed.removeAllDatos();
       String s="select p.*,a.pro_nomb,cl.cli_codi,"
           + " cl.tar_codi from v_cliente as cl,v_pedven p left join v_articulo as a on a.pro_codi=p.pro_codi where avc_ano="+ jtCab.getValorInt(0)+
           " and avc_serie='"+jtCab.getValString(1)+"' and avc_nume = "+ jtCab.getValorInt(2)+
           " and cl.cli_codi = p.cli_codi ";
       if (! dtCon1.select(s))
           return;
       do
       {
           ArrayList v=new ArrayList();
            v.add(dtCon1.getString("pro_codi"));
            v.add(dtCon1.getString("pro_nomb"));
            v.add(Formatear.redondea(dtCon1.getDouble("pvl_canti"),2)+" "+dtCon1.getString("pvl_tipo"));
//            v.add(dtCon1.getString("pvl_kilos"));
            v.add(dtCon1.getString("pvl_precio"));
            v.add(MantTarifa.getPrecTar(dtStat, dtCon1.getInt("pro_codi"),dtCon1.getInt("cli_codi"),
                    dtCon1.getInt("tar_codi"), dtCon1.getFecha("pvc_fecent", "dd-MM-yyyy"))); 
            v.add(dtCon1.getString("pvl_comen")); 
            jtPed.addLinea(v);
       } while (dtCon1.next());
    }
    
    void verLineas() {
        try {
            verPedido();
            String s = pdalbara.getSqlLinAgr( jtCab.getValorInt(0), emp_codiE.getValorInt(),
                    jtCab.getValString(1), jtCab.getValorInt(2), true);
            if (ARG_MODIF)
                jtLin.setEnabled(false);
            jtLin.removeAllDatos();
            if (!dtCon1.select(s)) {
                mensajeErr("No encontradas lineas de albaran");
                return;
            }
            getCondAlb(jtCab.getSelectedRow());
            String linAlb;
            PreparedStatement psCom=dtCon1.getPreparedStatement("select * from comision_represent where avc_id = ? and cor_linea=?");
            ResultSet rsCom;
            do
            {
                linAlb = MantPrAlb.getNumLinAlb(condAlb,
                     dtCon1.getDouble("avl_canti"),
                     dtCon1.getInt("pro_codi"),dtCon1.getDouble("avl_prven"),
                     dtCon1.getDouble("avl_prepvp"),dtCon1.getString("avl_coment",false),
                     dtCon1.getDouble("avl_profer"),dtStat);
                ArrayList v = new ArrayList();
                v.add(dtCon1.getString("pro_codi"));
                v.add(dtCon1.getString("pro_nomb"));
                v.add(dtCon1.getString("avl_unid"));
                v.add(dtCon1.getString("avl_canti"));
                v.add(dtCon1.getString("avl_prven"));               
                v.add(dtCon1.getDouble("avl_profer"));                
                v.add(dtCon1.getDouble("avl_profer")>0?
                    dtCon1.getDouble("avl_prven") - dtCon1.getDouble("avl_profer"):0);
                v.add(linAlb);
                v.add(dtCon1.getDouble("tar_preci", true));
                psCom.setInt(1,idAlbaran);
                psCom.setString(2,linAlb);
                rsCom=psCom.executeQuery();                
                v.add(rsCom.next());
                jtLin.addLinea(v);
            } while (dtCon1.next());
            if (ARG_MODIF)
                jtLin.setEnabled(true);
            jtLin.requestFocusInicio();
            verComentario();
        } catch (Exception k) {
            Error("Error al ver Lineas de albaran", k);
        }
    }
    @Override
    public void matar()
    {
        guardaCambios(jtLin.getSelectedRow());
        super.matar();
    }
    /**
     * Comprueba las condiciones introducidas
     * @return
     * @throws ParseException 
     */
    boolean checkCond() throws ParseException  {
        if (!emp_codiE.controla()) {
            mensajeErr(emp_codiE.getMsgError());
            return false;
        }
        if (fecIniE.getError()) {
            return false;
        }
        if (fecFinE.getError()) {
            return false;
        }
        if (fecIniE.isNull()) {
            mensajeErr("Introduzca Fecha Inicial");
            fecIniE.requestFocus();
            return false;
        }
        if (fecFinE.isNull()) {
            mensajeErr("Introduzca Fecha Final");
            fecFinE.requestFocus();
            return false;
        }
        if (Formatear.comparaFechas(fecIniE.getDate(), fecFinE.getDate()) > 0) {
            mensajeErr("Fecha final no puede ser inferios a Inicial");
            fecIniE.requestFocus();
            return false;
        }
        if (rep_codiE.isNull()) {
            mensajeErr("Introduzca un Representante");
            rep_codiE.requestFocus();
            return false;
        }
        if (! rep_codiE.controla(true))
        {
            mensajeErr("Representante NO VALIDO");
            return false;
        }
        return true;
    }
    
    private void imprimir() {
        try {
            if (! checkCond())
                return;
            mensaje("Espere, generando Listado");
            String s=getStrSql(false,getCondWhere());
            dtCon1.setStrSelect(s);
            ResultSet rs=ct.createStatement().executeQuery(dtCon1.getStrSelect());
            JasperReport jr = Listados.getJasperReport(EU,  "realbvrep");
            java.util.HashMap mp =Listados.getHashMapDefault();
            mp.put(JRParameter.REPORT_CONNECTION, ct);
            mp.put("fecIni",fecIniE.getDate());
            mp.put("fecFin",fecFinE.getDate());
            mp.put("fecIniD",fecIniE.getFecha("yyyy-MM-dd"));
            mp.put("fecFinD",fecFinE.getFecha("yyyy-MM-dd"));
            mp.put("empCodiE",new Integer(emp_codiE.getText()));
            mp.put("repCodiE",rep_codiE.getText());
            mp.put("repNombE",rep_codiE.getTextCombo());
            
            mp.put("SUBREPORT_FILE_NAME", EU.pathReport + "/lialbrep.jasper");

            JasperPrint jp = JasperFillManager.fillReport(jr, mp, new JRResultSetDataSource(rs));
            mensaje("");
            if (!gnu.chu.print.util.printJasper(jp, EU))
                 return;
            msgBox("Listado Generado !!");

        } catch (ParseException | SQLException | JRException | NumberFormatException | PrinterException k) {
            Error("Error al generar listado ", k);
        }
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

        pro_codiE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#####9");
        pro_nombE = new gnu.chu.controles.CTextField();
        avl_unidE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---9");
        avl_kilosE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---9.99");
        avl_precioE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--9.99");
        avl_prtariE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--9.99");
        avl_gananE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--9.99");
        avl_numlinE = new gnu.chu.controles.CTextField();
        avl_proferE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--9.99");
        avl_comentE = new gnu.chu.controles.CCheckBox();
        Pprinc = new gnu.chu.controles.CPanel();
        Pcondic = new gnu.chu.controles.CPanel();
        cLabel5 = new gnu.chu.controles.CLabel();
        fecIniE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel6 = new gnu.chu.controles.CLabel();
        fecFinE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cLabel3 = new gnu.chu.controles.CLabel();
        emp_codiE = new gnu.chu.camposdb.empPanel();
        cLabel1 = new gnu.chu.controles.CLabel();
        rep_codiE = new gnu.chu.controles.CLinkBox();
        opIncCobr = new gnu.chu.controles.CCheckBox();
        Baceptar = new gnu.chu.controles.CButtonMenu();
        bdisc = new gnu.chu.camposdb.DiscButton();
        rut_codiE = new gnu.chu.controles.CLinkBox();
        cLabel10 = new gnu.chu.controles.CLabel();
        zon_codiE = new gnu.chu.controles.CLinkBox();
        cLabel11 = new gnu.chu.controles.CLabel();
        gananMinE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9.99");
        cLabel12 = new gnu.chu.controles.CLabel();
        gananMaxE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9.99");
        cLabel13 = new gnu.chu.controles.CLabel();
        sbe_codiE = new gnu.chu.camposdb.sbePanel();
        gananOpcC = new gnu.chu.controles.CComboBox();
        cLabel14 = new gnu.chu.controles.CLabel();
        opReprCli = new gnu.chu.controles.CCheckBox();
        jtCab = new gnu.chu.controles.Cgrid(11);
        ArrayList v=new ArrayList();
        v.add("Ejer"); // 0
        v.add("Ser"); // 1
        v.add("Num.Alb"); // 2
        v.add("Fec.Alb"); // 3
        v.add("Cliente"); // 4
        v.add("Nomb.Cliente"); // 5
        v.add("Imp.Alb"); // 6
        v.add("Imp.Cob"); // 7
        v.add("Kilos"); // 8
        v.add("Imp.Ganan"); // 9
        v.add("Tar"); // 10
        jtCab.setCabecera(v);
        jtCab.setAnchoColumna(new int[]{
            40,30,50,80, 60,180,80,80,70,80,30});
    jtCab.setFormatoColumna(3,"dd-MM-yy");
    jtCab.setFormatoColumna(6,"----,--9.99");
    jtCab.setFormatoColumna(7,"----,--9.99");
    jtCab.setFormatoColumna(8,"--,--9.99");
    jtCab.setFormatoColumna(9,"----,--9.99");
    jtCab.setAlinearColumna(new int[]{2,1,2,1,2,0,2,2,2,2,2});
    jtCab.setAjustarGrid(true);
    jtLin = new gnu.chu.controles.CGridEditable(10){

    }
    ;
    ArrayList v1=new ArrayList();
    v1.add("Prod"); //0
    v1.add("Nombre"); // 1
    v1.add("Unid"); // 2
    v1.add("Kilos"); // 3
    v1.add("Prec"); // 4
    v1.add("Pr.Min."); // 5
    v1.add("Gananc"); // 6
    v1.add("NL"); // 7
    v1.add("Pr.Tarifa"); // 8
    v1.add("Com"); // 9
    jtLin.setCabecera(v1);
    jtLin.setAnchoColumna(new int[]{60,180,50,70,60,60,60,20,60,30});
    jtLin.setAlinearColumna(new int[]{2,0,2,2,2,2,2,0,2,1});
    try{
        ArrayList vc = new ArrayList();
        vc.add(pro_codiE);
        vc.add(pro_nombE);
        vc.add(avl_unidE);
        vc.add(avl_kilosE);
        vc.add(avl_precioE);
        vc.add(avl_proferE);
        vc.add(avl_gananE);
        vc.add(avl_numlinE);
        vc.add(avl_prtariE);
        vc.add(avl_comentE);
        jtLin.setCampos(vc);
    } catch (Exception k ){Error("Error al iniciar grid",k);}
    jtLin.setFormatoColumna(2,"---9");
    jtLin.setFormatoColumna(3,"---9.99");
    jtLin.setFormatoColumna(4,"--9.99");
    jtLin.setFormatoColumna(5,"--9.99");
    jtLin.setFormatoColumna(6,"--9.99");
    jtLin.setFormatoColumna(8,"--9.99");
    jtLin.setFormatoColumna(9,"BSN");
    jtLin.setCanDeleteLinea(false);
    jtLin.setCanInsertLinea(false);
    jtLin.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

    jtLin.setPreferredSize(new java.awt.Dimension(80, 80));

    // Code of sub-components - not shown here

    // Layout setup code - not shown here

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
    Pprinc.add(jtLin, gridBagConstraints);
    Presumen = new gnu.chu.controles.CPanel();
    cLabel2 = new gnu.chu.controles.CLabel();
    numAlbE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
    cLabel4 = new gnu.chu.controles.CLabel();
    impAlbE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----,--9.9");
    cLabel7 = new gnu.chu.controles.CLabel();
    kilAlbE = new gnu.chu.controles.CTextField(Types.DECIMAL,"-,---,--9.9");
    cLabel8 = new gnu.chu.controles.CLabel();
    impGanE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---,--9.9");
    cor_comentE = new gnu.chu.controles.CTextField(Types.CHAR,"X",120);
    BirGrid = new gnu.chu.controles.CButton(Iconos.getImageIcon("reload"));
    BFill = new gnu.chu.controles.CButton(Iconos.getImageIcon("fill"));
    Btarifa = new gnu.chu.controles.CButton(Iconos.getImageIcon("calc"));
    cor_comresE = new gnu.chu.controles.CTextField(Types.CHAR,"X",120);
    cLabel9 = new gnu.chu.controles.CLabel();
    cLabel15 = new gnu.chu.controles.CLabel();
    jtPed = new gnu.chu.controles.CGridEditable(6)
    ;
    ArrayList v2=new ArrayList();
    v2.add("Prod"); //0
    v2.add("Nombre"); // 1
    v2.add("Ped."); // 2
    v2.add("Prec"); // 3
    v2.add("Pr.Tari"); // 4
    v2.add("Coment."); // 5

    jtPed.setCabecera(v2);
    jtPed.setAnchoColumna(new int[]{60,180,50,60,60,250});
    jtPed.setAlinearColumna(new int[]{2,0,2,2,2,0});

    jtPed.setFormatoColumna(3, "--,--9.99");
    jtPed.setFormatoColumna(4, "##9.99");
    jtLin.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

    jtLin.setPreferredSize(new java.awt.Dimension(80, 80));

    // Code of sub-components - not shown here

    // Layout setup code - not shown here

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
    Pprinc.add(jtLin, gridBagConstraints);

    pro_codiE.setEnabled(false);

    pro_nombE.setEnabled(false);

    avl_unidE.setEnabled(false);

    avl_kilosE.setEnabled(false);

    avl_precioE.setEnabled(false);

    avl_prtariE.setEnabled(false);

    avl_gananE.setEnabled(false);

    avl_numlinE.setEnabled(false);

    avl_comentE.setText("cCheckBox1");
    avl_comentE.setEnabled(false);

    Pprinc.setLayout(new java.awt.GridBagLayout());

    Pcondic.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
    Pcondic.setMaximumSize(new java.awt.Dimension(620, 70));
    Pcondic.setMinimumSize(new java.awt.Dimension(620, 70));
    Pcondic.setPreferredSize(new java.awt.Dimension(620, 70));
    Pcondic.setLayout(null);

    cLabel5.setText("De Fecha");
    Pcondic.add(cLabel5);
    cLabel5.setBounds(100, 2, 49, 17);
    Pcondic.add(fecIniE);
    fecIniE.setBounds(150, 2, 76, 17);

    cLabel6.setText("A ");
    Pcondic.add(cLabel6);
    cLabel6.setBounds(230, 2, 20, 17);
    Pcondic.add(fecFinE);
    fecFinE.setBounds(240, 2, 75, 17);

    cLabel3.setText("Emp.");
    Pcondic.add(cLabel3);
    cLabel3.setBounds(1, 2, 30, 17);
    Pcondic.add(emp_codiE);
    emp_codiE.setBounds(40, 2, 50, 17);

    cLabel1.setText("Ruta");
    Pcondic.add(cLabel1);
    cLabel1.setBounds(320, 2, 30, 17);

    rep_codiE.setAncTexto(30);
    rep_codiE.setAncTexto(30);
    rep_codiE.setMayusculas(true);
    Pcondic.add(rep_codiE);
    rep_codiE.setBounds(50, 25, 190, 17);
    rep_codiE.getAccessibleContext().setAccessibleDescription("");

    opIncCobr.setText("Cobrados");
    Pcondic.add(opIncCobr);
    opIncCobr.setBounds(510, 22, 80, 17);

    Baceptar.setText("Elegir");
    Baceptar.addMenu("Consultar","C");
    Baceptar.addMenu("Cons. Incid","!");
    Baceptar.addMenu("Cons.Sin Precio","S");

    if (ARG_MODIF)
    {
        Baceptar.addMenu("Act. Precio Min","P");
        Baceptar.addMenu("Forzar Act. Precio Min","P!");
        Baceptar.addMenu("Act. Tarifa","A");
        Baceptar.addMenu("Forzar Act. Tarifa","A!");
    }
    Baceptar.addMenu("Sin Tarifa","T");

    Baceptar.addMenu("Imprimir","I");
    Pcondic.add(Baceptar);
    Baceptar.setBounds(520, 40, 100, 26);
    Pcondic.add(bdisc);
    bdisc.setBounds(600, 20, 21, 20);

    rut_codiE.setAncTexto(30);
    rut_codiE.setMayusculas(true);
    Pcondic.add(rut_codiE);
    rut_codiE.setBounds(350, 2, 250, 17);

    cLabel10.setText("Zona");
    Pcondic.add(cLabel10);
    cLabel10.setBounds(240, 25, 30, 17);
    cLabel10.getAccessibleContext().setAccessibleDescription("");

    zon_codiE.setAncTexto(30);
    zon_codiE.setMayusculas(true);
    Pcondic.add(zon_codiE);
    zon_codiE.setBounds(270, 25, 240, 17);
    zon_codiE.getAccessibleContext().setAccessibleDescription("");

    cLabel11.setText("Seccion");
    Pcondic.add(cLabel11);
    cLabel11.setBounds(270, 45, 50, 17);
    Pcondic.add(gananMinE);
    gananMinE.setBounds(150, 45, 40, 17);

    cLabel12.setText("Y");
    Pcondic.add(cLabel12);
    cLabel12.setBounds(200, 45, 20, 17);
    Pcondic.add(gananMaxE);
    gananMaxE.setBounds(220, 45, 40, 17);

    cLabel13.setText("Repres");
    Pcondic.add(cLabel13);
    cLabel13.setBounds(1, 25, 50, 15);
    Pcondic.add(sbe_codiE);
    sbe_codiE.setBounds(320, 45, 40, 19);

    gananOpcC.addItem(""," ");
    gananOpcC.addItem("Mayor",">");
    gananOpcC.addItem("Menor","<");
    gananOpcC.addItem("Entre","E");
    Pcondic.add(gananOpcC);
    gananOpcC.setBounds(50, 45, 90, 17);

    cLabel14.setText("Ganacia ");
    Pcondic.add(cLabel14);
    cLabel14.setBounds(0, 45, 50, 17);

    opReprCli.setText("Forzar Repr.");
    opReprCli.setToolTipText("Coger Repr. de Cliente");
    Pcondic.add(opReprCli);
    opReprCli.setBounds(360, 45, 90, 17);

    Pprinc.add(Pcondic, new java.awt.GridBagConstraints());

    jtCab.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jtCab.setMaximumSize(new java.awt.Dimension(220, 130));
    jtCab.setMinimumSize(new java.awt.Dimension(220, 130));

    org.jdesktop.layout.GroupLayout jtCabLayout = new org.jdesktop.layout.GroupLayout(jtCab);
    jtCab.setLayout(jtCabLayout);
    jtCabLayout.setHorizontalGroup(
        jtCabLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(0, 626, Short.MAX_VALUE)
    );
    jtCabLayout.setVerticalGroup(
        jtCabLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(0, 128, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 3.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
    Pprinc.add(jtCab, gridBagConstraints);

    jtLin.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jtLin.setMaximumSize(new java.awt.Dimension(180, 100));
    jtLin.setMinimumSize(new java.awt.Dimension(180, 100));

    org.jdesktop.layout.GroupLayout jtLinLayout = new org.jdesktop.layout.GroupLayout(jtLin);
    jtLin.setLayout(jtLinLayout);
    jtLinLayout.setHorizontalGroup(
        jtLinLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(0, 626, Short.MAX_VALUE)
    );
    jtLinLayout.setVerticalGroup(
        jtLinLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(0, 98, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 2.0;
    Pprinc.add(jtLin, gridBagConstraints);

    Presumen.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    Presumen.setMaximumSize(new java.awt.Dimension(628, 60));
    Presumen.setMinimumSize(new java.awt.Dimension(628, 60));
    Presumen.setOpaque(false);
    Presumen.setPreferredSize(new java.awt.Dimension(628, 60));
    Presumen.setLayout(null);

    cLabel2.setText("Num. Albaranes");
    cLabel2.setPreferredSize(new java.awt.Dimension(57, 17));
    Presumen.add(cLabel2);
    cLabel2.setBounds(12, 2, 95, 17);

    numAlbE.setEnabled(false);
    Presumen.add(numAlbE);
    numAlbE.setBounds(105, 2, 47, 17);

    cLabel4.setText("Respuesta");
    cLabel4.setPreferredSize(new java.awt.Dimension(57, 17));
    Presumen.add(cLabel4);
    cLabel4.setBounds(1, 40, 70, 17);

    impAlbE.setEnabled(false);
    Presumen.add(impAlbE);
    impAlbE.setBounds(210, 2, 70, 17);

    cLabel7.setText("Kilos");
    cLabel7.setPreferredSize(new java.awt.Dimension(57, 17));
    Presumen.add(cLabel7);
    cLabel7.setBounds(290, 2, 32, 17);

    kilAlbE.setEnabled(false);
    Presumen.add(kilAlbE);
    kilAlbE.setBounds(320, 2, 70, 17);

    cLabel8.setText("Ganancia");
    cLabel8.setPreferredSize(new java.awt.Dimension(57, 17));
    Presumen.add(cLabel8);
    cLabel8.setBounds(400, 2, 57, 17);

    impGanE.setEnabled(false);
    Presumen.add(impGanE);
    impGanE.setBounds(460, 2, 60, 17);
    Presumen.add(cor_comentE);
    cor_comentE.setBounds(70, 20, 550, 17);

    BirGrid.setToolTipText("F2 para moverse entre Tablas");
    Presumen.add(BirGrid);
    BirGrid.setBounds(600, 0, 20, 20);

    BFill.setToolTipText("F5 Para pasar precio Tarifa");
    Presumen.add(BFill);
    BFill.setBounds(560, 0, 30, 20);

    Btarifa.setToolTipText("F9 Buscar Precio Tarifa");
    Presumen.add(Btarifa);
    Btarifa.setBounds(530, 0, 20, 20);
    Presumen.add(cor_comresE);
    cor_comresE.setBounds(70, 39, 550, 17);

    cLabel9.setText("Importe");
    cLabel9.setPreferredSize(new java.awt.Dimension(57, 17));
    Presumen.add(cLabel9);
    cLabel9.setBounds(162, 2, 57, 17);

    cLabel15.setText("Incidencia");
    cLabel15.setPreferredSize(new java.awt.Dimension(57, 17));
    Presumen.add(cLabel15);
    cLabel15.setBounds(1, 20, 70, 17);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
    Pprinc.add(Presumen, gridBagConstraints);

    jtPed.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jtPed.setMaximumSize(new java.awt.Dimension(180, 100));
    jtPed.setMinimumSize(new java.awt.Dimension(180, 100));

    org.jdesktop.layout.GroupLayout jtPedLayout = new org.jdesktop.layout.GroupLayout(jtPed);
    jtPed.setLayout(jtPedLayout);
    jtPedLayout.setHorizontalGroup(
        jtPedLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(0, 626, Short.MAX_VALUE)
    );
    jtPedLayout.setVerticalGroup(
        jtPedLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(0, 98, Short.MAX_VALUE)
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 2.0;
    Pprinc.add(jtPed, gridBagConstraints);

    getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

    pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton BFill;
    private gnu.chu.controles.CButtonMenu Baceptar;
    private gnu.chu.controles.CButton BirGrid;
    private gnu.chu.controles.CButton Btarifa;
    private gnu.chu.controles.CPanel Pcondic;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CPanel Presumen;
    private gnu.chu.controles.CCheckBox avl_comentE;
    private gnu.chu.controles.CTextField avl_gananE;
    private gnu.chu.controles.CTextField avl_kilosE;
    private gnu.chu.controles.CTextField avl_numlinE;
    private gnu.chu.controles.CTextField avl_precioE;
    private gnu.chu.controles.CTextField avl_proferE;
    private gnu.chu.controles.CTextField avl_prtariE;
    private gnu.chu.controles.CTextField avl_unidE;
    private gnu.chu.camposdb.DiscButton bdisc;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel10;
    private gnu.chu.controles.CLabel cLabel11;
    private gnu.chu.controles.CLabel cLabel12;
    private gnu.chu.controles.CLabel cLabel13;
    private gnu.chu.controles.CLabel cLabel14;
    private gnu.chu.controles.CLabel cLabel15;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.controles.CLabel cLabel9;
    private gnu.chu.controles.CTextField cor_comentE;
    private gnu.chu.controles.CTextField cor_comresE;
    private gnu.chu.camposdb.empPanel emp_codiE;
    private gnu.chu.controles.CTextField fecFinE;
    private gnu.chu.controles.CTextField fecIniE;
    private gnu.chu.controles.CTextField gananMaxE;
    private gnu.chu.controles.CTextField gananMinE;
    private gnu.chu.controles.CComboBox gananOpcC;
    private gnu.chu.controles.CTextField impAlbE;
    private gnu.chu.controles.CTextField impGanE;
    private gnu.chu.controles.Cgrid jtCab;
    private gnu.chu.controles.CGridEditable jtLin;
    private gnu.chu.controles.Cgrid jtPed;
    private gnu.chu.controles.CTextField kilAlbE;
    private gnu.chu.controles.CTextField numAlbE;
    private gnu.chu.controles.CCheckBox opIncCobr;
    private gnu.chu.controles.CCheckBox opReprCli;
    private gnu.chu.controles.CTextField pro_codiE;
    private gnu.chu.controles.CTextField pro_nombE;
    private gnu.chu.controles.CLinkBox rep_codiE;
    private gnu.chu.controles.CLinkBox rut_codiE;
    private gnu.chu.camposdb.sbePanel sbe_codiE;
    private gnu.chu.controles.CLinkBox zon_codiE;
    // End of variables declaration//GEN-END:variables
}
