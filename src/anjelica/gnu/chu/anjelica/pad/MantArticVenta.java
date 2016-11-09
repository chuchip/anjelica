package gnu.chu.anjelica.pad;
/**
 *
 * <p>Título: MantArticVenta </p>
 * <p>Descripcion: Mantenimiento Articulos de Venta</p>
 * <p>Empresa: miSL</p>
*  <p>Copyright: Copyright (c) 2005-2016
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
 * @author ChuchiP
 * @version 1.0
 */ 
import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.menu;
import gnu.chu.controles.StatusBar;
import gnu.chu.interfaces.PAD;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.navegador;
import gnu.chu.utilidades.ventanaPad;
import java.awt.BorderLayout;
import java.awt.Component;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;


public class MantArticVenta extends ventanaPad implements PAD
{

  
  public MantArticVenta(EntornoUsuario eu, Principal p)
  {

    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    try
    {
     
      setTitulo("Mantenimiento de Articulos Venta");

      if (jf.gestor.apuntar(this))
        jbInit();
      else
        setErrorInit(true);
    }
    catch (Exception e)
    {
      ErrorInit(e);
      setErrorInit(true);
    }
  }

  public MantArticVenta(menu p, EntornoUsuario eu)
  {
    EU = eu;
    vl = p.getLayeredPane();
    eje = false;

    try
    {
     
      setTitulo("Mantenimiento de Articulos Venta");
      jbInit();
    }
    catch (Exception e)
    {
      ErrorInit(e);
      setErrorInit(true);
    }
  }
private void jbInit() throws Exception {
        statusBar = new StatusBar(this);
        nav = new navegador(this, dtCons, false,  navegador.NORMAL);
        iniciarFrame();
//        this.setResizable(false);

        this.setVersion("2016-11-05");
        strSql = "SELECT * FROM prodventa  "+
                " ORDER BY pve_codi";

        this.getContentPane().add(nav, BorderLayout.NORTH);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        this.setPad(this);
        dtCons.setLanzaDBCambio(false);
        conecta();
        initComponents();
        iniciarBotones(Baceptar, Bcancelar);
       
        navActivarAll();
        this.setSize(400,300);
        activar(false);
    }

    @Override
    public void iniciarVentana() throws Exception
    {
        pve_codiE.setColumnaAlias("pve_codi");
        pve_nombE.setColumnaAlias("pve_nomb");
        pArticVenta.iniciar(dtCon1);
       
        Pprinc.setDefButton(Baceptar);
        
        verDatos(dtCons);
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
        pve_codiE = new gnu.chu.controles.CTextField(Types.CHAR,"X",15);
        cLabel9 = new gnu.chu.controles.CLabel();
        pve_nombE = new gnu.chu.controles.CTextField(Types.CHAR,"X",50);
        Bcancelar = new gnu.chu.controles.CButton();
        Baceptar = new gnu.chu.controles.CButton();
        cLabel11 = new gnu.chu.controles.CLabel();
        pArticVenta = new gnu.chu.utilidades.PanelArticVenta();

        Pprinc.setLayout(null);
        Pprinc.add(pve_codiE);
        pve_codiE.setBounds(100, 10, 110, 18);

        cLabel9.setText("Descripcion");
        Pprinc.add(cLabel9);
        cLabel9.setBounds(10, 154, 70, 17);
        Pprinc.add(pve_nombE);
        pve_nombE.setBounds(80, 154, 300, 17);

        Bcancelar.setText("Cancelar");
        Pprinc.add(Bcancelar);
        Bcancelar.setBounds(220, 180, 120, 30);

        Baceptar.setText("Aceptar");
        Pprinc.add(Baceptar);
        Baceptar.setBounds(60, 180, 120, 30);

        cLabel11.setText("Articulo Venta");
        Pprinc.add(cLabel11);
        cLabel11.setBounds(10, 10, 90, 17);

        pArticVenta.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        Pprinc.add(pArticVenta);
        pArticVenta.setBounds(10, 40, 390, 110);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CButton Bcancelar;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CLabel cLabel11;
    private gnu.chu.controles.CLabel cLabel9;
    private gnu.chu.utilidades.PanelArticVenta pArticVenta;
    private gnu.chu.controles.CTextField pve_codiE;
    private gnu.chu.controles.CTextField pve_nombE;
    // End of variables declaration//GEN-END:variables

    @Override
    public void PADPrimero() {
       verDatos(dtCons);
    }

    @Override
    public void PADAnterior() {
          verDatos(dtCons);
    }

    @Override
    public void PADSiguiente() {
          verDatos(dtCons);
    }
  @Override
    public void PADDelete()
    {
        try
        {
            String s="select * from v_articulo where pro_codart = '"+pve_codiE+"'";
            if (dtStat.select(s))
            {
                msgBox("Articulo Venta asignado en Maestro Productos ("+dtStat.getInt("pro_codi")+")");
                nav.pulsado = navegador.NINGUNO;
                activaTodo();
                return;
                
            }
            if (!setBloqueo(dtAdd, "prodventa", pve_codiE.getText()))
            {
                msgBox(msgBloqueo);
                nav.pulsado = navegador.NINGUNO;
                activaTodo();
                return;
            }

            if (!dtAdd.select("select * from prodventa where pve_codi= '" + pve_codiE.getText() + "'", true))
            {
                mensajeErr("Registro ha sido borrado");
                resetBloqueo(dtAdd, "prodventa", pve_codiE.getText());
                activaTodo();
                mensaje("");
            }

        } catch (SQLException | UnknownHostException k)
        {
            Error("Error al bloquear el registro", k);            
        }
    }
    @Override
    public void PADUltimo() {
          verDatos(dtCons);
    }
    void verDatos(DatosTabla dt)
    {
        try
        {
            if (dt.getNOREG())
                return;
            
            pve_codiE.setText(dt.getString("pve_codi"));
            
            String s = "SELECT * FROM prodventa WHERE  pve_codi = '" + dt.getString("pve_codi")+"'";
            if (!dtCon1.select(s))
            {
                mensajeErr("Codigo NO ENCONTRADO ... SEGURAMENTE SE BORRO");
                pve_nombE.resetTexto();                
                return;
            }
            pve_nombE.setText(dtCon1.getString("pve_nomb"));
            pArticVenta.setValorCongelado(dtCon1.getInt("pve_cong"));
            pArticVenta.setValorCorte(dtCon1.getInt("pve_corte"));
            pArticVenta.setValorCurado(dtCon1.getInt("pve_curac"));
        } catch (Exception k)
        {
            Error("Error al ver Datos", k);
        }
    }
      @Override
  public void PADAddNew()
  {
    Pprinc.resetTexto();
    activar(true);
    
    pve_codiE.requestFocus();
  }
    @Override
  public void PADEdit()
  {
    activar(true);
//    pve_codiE.setEnabled(false);
    try
    {
      if (!setBloqueo(dtAdd, "prodventa", pve_codiE.getText()))
       {
         msgBox(msgBloqueo);
         nav.pulsado = navegador.NINGUNO;
         activaTodo();
         return;
       }

      if (! dtAdd.select("select * from prodventa where pve_codi= '"+pve_codiE.getText()+"'",true))
      {
        mensajeErr("Registro ha sido borrado");
        resetBloqueo(dtAdd, "prodventa",  pve_codiE.getText());
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
    pve_nombE.requestFocus();
  }

  @Override
  public void PADQuery()
  {
    activar(true);    
    Pprinc.setQuery(true);    
    pArticVenta.setQuery(true);
   
    Pprinc.resetTexto();
    pArticVenta.resetTexto();
    pve_codiE.requestFocus();
  }
  
    @Override
    public void ej_query1() {
        Component c;
        if ((c = Pprinc.getErrorConf()) != null)
        {
            c.requestFocus();
            mensaje("Error en Criterios de busqueda");
            return;
        }
        ArrayList v = new ArrayList();
    v.add(pve_codiE.getStrQuery());
    v.add(pve_nombE.getStrQuery());
    v.add(pArticVenta.getCongeladoQuery());
    v.add(pArticVenta.getCuracionQuery());
    v.add(pArticVenta.getCorteQuery());
    String cond=pArticVenta.getCondicionesQuery(true);
   
    
    String s = "SELECT * FROM prodventa "+cond;
    s = creaWhere(s, v,cond.isEmpty());    
    Pprinc.setQuery(false);   
    
//    debug(s);
    try
    {
      if (!dtCons.select(s))
      {
        mensaje("");
        mensajeErr("No encontrados Registros para estos criterios");
        rgSelect();
        activaTodo();
        verDatos(dtCons);
        return;
      }
      mensaje("");
      strSql = s;
      nav.pulsado=navegador.NINGUNO;
      activaTodo();
      rgSelect();
      verDatos(dtCons);
      mensajeErr("Nuevos registros selecionados");
    }
    catch (Exception ex)
    {
      fatalError("Error al buscar productos ventas: ", ex);
    }
    }

    @Override
    public void canc_query() {
       mensaje("");
    mensajeErr("Consulta ... CANCELADA");
    nav.pulsado=navegador.NINGUNO;
    activaTodo();
    verDatos(dtCons);Pprinc.setQuery(false);
    
    
    }

    @Override
    public void ej_edit1() {
      try
    {
      dtAdd.edit(dtAdd.getCondWhere());
      
      actDatos();
      
      dtAdd.update(stUp);
     
      resetBloqueo(dtAdd, "prodventa",  pve_codiE.getText(),false);
      ctUp.commit();
      verDatos(dtCons);
    }
    catch (Throwable ex)
    {
      Error("Error al Modificar datos", ex);
      return;
    }
    mensaje("");
    mensajeErr("Datos ... Modificados");
    nav.pulsado=navegador.NINGUNO;
    activaTodo();
    }
    void actDatos() throws SQLException
    {
        dtAdd.setDato("pve_codi", pve_codiE.getText());
        dtAdd.setDato("pve_nomb", pve_nombE.getText());
        dtAdd.setDato("pve_cong", pArticVenta.getValorCongelado());
        dtAdd.setDato("pve_corte", pArticVenta.getValorCorte());
        dtAdd.setDato("pve_curac", pArticVenta.getValorCurado());
    }
    @Override
    public void canc_edit() {
        try
        {
            resetBloqueo(dtAdd, "prodventa", pve_codiE.getText());
        } catch (Exception k)
        {
            Error("Error al Desbloquear el registro", k);
            return;
        }

        mensaje("");
        nav.pulsado = navegador.NINGUNO;
        activaTodo();
        mensajeErr("Modificacion de Datos Cancelada");
        verDatos(dtCons);        
    }
    @Override
    public boolean checkEdit() {
        return checkAddNew();
    }

    @Override
    public boolean checkAddNew()
    {

        if (pve_codiE.isNull())
        {
            mensajeErr("Introduzca un codigo para el Producto");
            pve_codiE.requestFocus();
            return false;
        }
        if (pve_nombE.isNull())
        {
            mensajeErr("Introduzca Descripcion de Producto");
            pve_nombE.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void ej_addnew1() {
        try
        {
            String s = "SELECT * FROM prodventa WHERE "
                + " pve_codi = '" + pve_codiE.getText() + "'";
            if (dtStat.select(s))
            {
                mensajeErr("Codigo de Articulo ya existe");
                return;
            }
            mensaje("Insertando PRODUCTO ...", false);
            dtAdd.addNew("prodventa");            
            actDatos();
            dtAdd.update(stUp);
            ctUp.commit();
        } catch (Exception ex)
        {
            Error("Error al Insertar datos", ex);
            return;
        }
        mensaje("");
        mensajeErr("Datos ... Insertados");
        nav.pulsado = navegador.NINGUNO;
        activaTodo();

    }

    @Override
    public void canc_addnew() {
      mensaje("");
      nav.pulsado=navegador.NINGUNO;
      activaTodo();
      mensajeErr("Insercion de Datos Cancelada");
      verDatos(dtCons);    
    }

    @Override
    public void ej_delete1() {
        try
        {
            dtAdd.delete();
            resetBloqueo(dtAdd, "prodventa", pve_codiE.getText(), false);
            ctUp.commit();
            rgSelect();
            verDatos(dtCons);
        } catch (Throwable ex)
        {
            Error("Error al Modificar datos", ex);
            return;
        }
        mensaje("");
        mensajeErr("Articulo VENTA ... Borrado");
        nav.pulsado = navegador.NINGUNO;
        
        activaTodo();
    }

    @Override
    public void canc_delete() {
          mensaje("");
      nav.pulsado=navegador.NINGUNO;
      activaTodo();
      mensajeErr("Borrado  de Registro Cancelada");
      verDatos(dtCons);     
    }

    @Override
    public void activar(boolean b) {
        Pprinc.setEnabled(b);
    }
  
    public static String getNombreArticulo(DatosTabla dt,String pveCodi) throws SQLException
    {
        if (! dt.select("select * from prodventa where pve_codi ='"+pveCodi+"'"))
            return null;
        return dt.getString("pve_nomb");
    }
    
}
