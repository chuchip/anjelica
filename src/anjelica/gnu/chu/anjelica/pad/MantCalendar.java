package gnu.chu.anjelica.pad;
/**
 *
 * <p>Titulo: MantCalendar </p>
 * <p>Descripción: Mantenimiento Calendario </p>
 * <p>Parametros:  modConsulta=true si queremos que solo se lanze el programa en modo consulta.
 * Por defecto es en modo ediccion. 
 * </p>
 * <p>Copyright: Copyright (c) 2005-2015
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los términos de la Licencia Pública General de GNU según es publicada por
 *  la Free Software Foundation, bien de la versión 2 de dicha Licencia
 *  o bien (según su elección) de cualquier versión posterior.
 *  Este programa se distribuye con la esperanza de que sea útil,
 *  pero SIN
 * NINGUNA GARANTIA, incluso sin la garantía MERCANTIL implícita
 *  o sin garantizar la CONVENIENCIA PARA UN PROPOSITO PARTICULAR.
 *  Véase la Licencia Pública General de GNU para más detalles.
 *  Debería haber recibido una copia de la Licencia Pública General junto con este programa.
 *  Si no ha sido así, escriba a la Free Software Foundation, Inc.,
 *  en 675 Mass Ave, Cambridge, MA 02139, EEUU.
 * </p>
 * @author chuchiP

 *
 */ 
import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.menu;
import gnu.chu.controles.StatusBar;
import gnu.chu.interfaces.PAD;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.mensajes;
import gnu.chu.utilidades.navegador;
import gnu.chu.utilidades.ventanaPad;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Hashtable;

import java.util.logging.Level;
import java.util.logging.Logger;


public class MantCalendar extends ventanaPad     implements PAD  {
    
    boolean ARG_MODCONSULTA=false;
    String s;

 public MantCalendar(EntornoUsuario eu, Principal p)
  {
    this(eu,p,null);
  }
  public MantCalendar(EntornoUsuario eu, Principal p,Hashtable ht)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    try
    {
      if (ht != null)
      {
        if (ht.get("modConsulta") != null)
          ARG_MODCONSULTA = Boolean.valueOf(ht.get("modConsulta").toString()).
              booleanValue();
      }
      setTitulo("Mantenimiento Calendario");

      if (jf.gestor.apuntar(this))
        jbInit();
      else
        setErrorInit(true);
    }
    catch (Exception e)
    {
      Logger.getLogger(MantCalendar.class.getName()).log(Level.SEVERE, null, e);
      setErrorInit(true);
    }
  }

  public MantCalendar(menu p, EntornoUsuario eu,Hashtable ht)
  {
    EU = eu;
    vl = p.getLayeredPane();
    eje = false;

    try
    {
      if (ht != null)
      {
        if (ht.get("modConsulta") != null)
          ARG_MODCONSULTA = Boolean.valueOf(ht.get("modConsulta").toString()).
              booleanValue();
      }
      setTitulo("Mantenimiento Calendario");
      jbInit();
    }
    catch (Exception e)
    {
      Logger.getLogger(MantCalendar.class.getName()).log(Level.SEVERE, null, e);
      setErrorInit(true);
    }
  }

  private void jbInit() throws Exception {
        statusBar = new StatusBar(this);
        nav = new navegador(this, dtCons, false);
        iniciarFrame();
//        this.setResizable(false);

        this.setVersion("2015-05-06" + (ARG_MODCONSULTA ? "SOLO LECTURA" : ""));
        strSql = "SELECT distinct(cal_ano) FROM calendario "+
                " ORDER BY cal_ano ";

        this.getContentPane().add(nav, BorderLayout.NORTH);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        this.setPad(this);
        dtCons.setLanzaDBCambio(false);
        initComponents();
        iniciarBotones(Baceptar, Bcancelar);

        conecta();
        navActivarAll();
        this.setSize(423,420);
        activar(false);
        if (ARG_MODCONSULTA)
        {
            nav.removeBoton(navegador.ADDNEW);
            nav.removeBoton(navegador.EDIT);
            nav.removeBoton(navegador.DELETE);
        }
    }
    @Override
    public void activar(boolean activ)
    {
       activar(navegador.TODOS,activ);
    }
    void activar(int modo,boolean act)
    {
      if (modo==navegador.TODOS)
        jt.setEnabled(act);
      Bautomat.setEnabled(!act || modo==navegador.ADDNEW);
      Baceptar.setEnabled(act);
      Bcancelar.setEnabled(act);
      Pcabe.setEnabled(act);
      cal_anoE.setEnabled(act);
    }
    @Override
    public void iniciarVentana() throws Exception
   {
        nav.setPulsado(navegador.NINGUNO);
        jt.setDefButton(Baceptar);
        Pcabe.setDefButton(Baceptar);
       
        cal_anoE.setColumnaAlias("cal_ano");
        
        activar(false);
        activarEventos();
        verDatos();
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
  public void PADQuery() {
    activar(navegador.QUERY, true);
    Pcabe.setQuery(true);
    Pcabe.resetTexto();
//    eje_numeE.setText(""+EU.ejercicio);
    cal_anoE.requestFocus();

  }

    @Override
  public void ej_query1() {
    Baceptar.setEnabled(false);
    ArrayList v=new ArrayList();
    v.add(cal_anoE.getStrQuery());
   
    Pcabe.setQuery(false);
    s="SELECT distinct(cal_ano)  FROM calendario ";
    s=creaWhere(s,v);
    s+=" order by cal_ano ";
//    debug(s);
    try
    {
      if (!dtCons.select(s))
      {
        mensaje("");
        mensajeErr("No encontrados Registros para estos criterios");
        rgSelect();
        verDatos();
        activaTodo();
        return;
      }
      mensaje("");
      strSql = s;
      activaTodo();
      rgSelect();
      verDatos();
      mensajeErr("Nuevos registros selecionados");
    }
    catch (Exception ex)
    {
      fatalError("Error al buscar Tarifas: ", ex);
    }

  }

    @Override
  public void canc_query() {
    Pcabe.setQuery(false);

    mensaje("");
    mensajeErr("Consulta ... CANCELADA");
    activaTodo();
    verDatos();
  }
    void verDatos()
    {

        jt.removeAllDatos();

        try {
            if (dtCons.getNOREG())
                return;
            cal_anoE.setValorInt(dtCons.getInt("cal_ano"));
            s="SELECT * FROM calendario "+
                    " where cal_ano = "+dtCons.getInt("cal_ano")+
                    " order by cal_mes";
            dtCon1.select(s);
            do {
                ArrayList v = new ArrayList();
                v.add(dtCon1.getString("cal_mes"));
                v.add(dtCon1.getDate("cal_fecini"));
                v.add(dtCon1.getDate("cal_fecfin"));
                jt.addLinea(v);
            } while (dtCon1.next());
            jt.requestFocusInicio();

        }catch (SQLException k)
        {
            Error("Error al presentar datos",k);
            return;
        }

    }
    void activarEventos()
    {
      Bautomat.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent ae)
          {
             if (!jt.isVacio())
             {
                if (mensajes.mensajePreguntar("Insertar calendario automatico, borrando datos existentes?")!=mensajes.YES)
                    return;
             }
             jt.setEnabled(false);
            jt.removeAllDatos();
            ArrayList datos=new ArrayList();
            for (int n=1;n<=12;n++)
            {
                try
                {
                    ArrayList ht=new ArrayList();
                    ht.add(n);
                    ht.add("01-"+Formatear.format(n, "99")+"-"+cal_anoE.getValorInt());
                    if (n==12)
                        ht.add("31-12-"+cal_anoE.getValorInt());
                    else
                        ht.add(Formatear.sumaDias("01"+Formatear.format(n+1, "99")+cal_anoE.getValorInt(),
                            "ddMMyyyy", -1));
                    datos.add(ht);
                } catch (ParseException ex)
                {
                    Logger.getLogger(MantCalendar.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            jt.setDatos(datos);
            jt.setEnabled(true);
            jt.requestFocusInicioLater();
          }
      });
      
      BirGrid.addFocusListener(new FocusAdapter()
      {
        @Override
        public void focusGained(FocusEvent e)
        {
            if (nav.pulsado!=navegador.ADDNEW)
                return;
            if (cal_anoE.getValorInt()==0)
            {
                mensajeErr("Año no valido");
                cal_anoE.requestFocusLater();
                return;
            }
            String s="SELECT * FROM calendario WHERE cal_ano="+cal_anoE.getValorInt();
            try
            {
                if (dtStat.select(s))
                {
                    mensajeErr("Calendario de año: " + cal_anoE.getValorInt() + " Ya existe");
                    cal_anoE.requestFocusLater();
                    return;
                }
            } catch (SQLException ex)
            {
                Error("Error al validar calendario",ex);
                return;
            }
            jt.setEnabled(true);
            cal_anoE.setEnabled(false);
            jt.requestFocusInicioLater();
        }
      });
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

        cal_mesE = new gnu.chu.controles.CTextField(Types.DECIMAL,"99");
        cal_feciniE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        cal_fecfinE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yyyy");
        Pprinc = new gnu.chu.controles.CPanel();
        Pcabe = new gnu.chu.controles.CPanel();
        cal_anoL = new gnu.chu.controles.CLabel();
        cal_anoE = new gnu.chu.controles.CTextField(Types.DECIMAL,"9999");
        BirGrid = new gnu.chu.controles.CButton();
        Bautomat = new gnu.chu.controles.CButton();
        jt = new gnu.chu.controles.CGridEditable(3){
            public int cambiaLinea(int row, int col)
            {
                return cambiaLineaJT();
            }
        }
        ;
        ArrayList v=new ArrayList();
        v.add("Mes");// 0
        v.add("Inicio");
        v.add("Final");
        jt.setCabecera(v);
        try {
            ArrayList vc=new ArrayList();
            vc.add(cal_mesE);
            vc.add(cal_feciniE);
            vc.add(cal_fecfinE);
            jt.setCampos(vc);
            jt.setAnchoColumna(new int[]{50,90,90});
            jt.setAlinearColumna(new int[]{2,1,1});
            jt.setFormatoCampos();
            jt.setAjustarGrid(true);

        } catch (Exception k)
        {
            Error("Error al iniciar grid",k);
            return;
        }
        Baceptar = new gnu.chu.controles.CButton();
        Bcancelar = new gnu.chu.controles.CButton();

        Pprinc.setLayout(new java.awt.GridBagLayout());

        Pcabe.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Pcabe.setMaximumSize(new java.awt.Dimension(180, 23));
        Pcabe.setMinimumSize(new java.awt.Dimension(180, 23));
        Pcabe.setPreferredSize(new java.awt.Dimension(180, 23));
        Pcabe.setLayout(null);

        cal_anoL.setText("Año");
        Pcabe.add(cal_anoL);
        cal_anoL.setBounds(20, 2, 30, 17);
        Pcabe.add(cal_anoE);
        cal_anoE.setBounds(50, 2, 40, 17);

        BirGrid.setText("cButton1");
        Pcabe.add(BirGrid);
        BirGrid.setBounds(95, 4, 2, 2);

        Bautomat.setText("Automatico");
        Bautomat.setToolTipText("Generar fechas automaticamente.");
        Pcabe.add(Bautomat);
        Bautomat.setBounds(107, 2, 70, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        Pprinc.add(Pcabe, gridBagConstraints);

        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jtLayout = new javax.swing.GroupLayout(jt);
        jt.setLayout(jtLayout);
        jtLayout.setHorizontalGroup(
            jtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 584, Short.MAX_VALUE)
        );
        jtLayout.setVerticalGroup(
            jtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 335, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.weighty = 2.0;
        Pprinc.add(jt, gridBagConstraints);

        Baceptar.setText("Aceptar");
        Baceptar.setMaximumSize(new java.awt.Dimension(110, 24));
        Baceptar.setMinimumSize(new java.awt.Dimension(110, 24));
        Baceptar.setPreferredSize(new java.awt.Dimension(110, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 0, 0);
        Pprinc.add(Baceptar, gridBagConstraints);

        Bcancelar.setText("Cancelar");
        Bcancelar.setMaximumSize(new java.awt.Dimension(110, 24));
        Bcancelar.setMinimumSize(new java.awt.Dimension(110, 24));
        Bcancelar.setPreferredSize(new java.awt.Dimension(110, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 20);
        Pprinc.add(Bcancelar, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CButton Bautomat;
    private gnu.chu.controles.CButton Bcancelar;
    private gnu.chu.controles.CButton BirGrid;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CTextField cal_anoE;
    private gnu.chu.controles.CLabel cal_anoL;
    private gnu.chu.controles.CTextField cal_fecfinE;
    private gnu.chu.controles.CTextField cal_feciniE;
    private gnu.chu.controles.CTextField cal_mesE;
    private gnu.chu.controles.CGridEditable jt;
    // End of variables declaration//GEN-END:variables

  
    @Override
    public void PADAddNew() {

    Pcabe.resetTexto();
    jt.removeAllDatos();
    Baceptar.setEnabled(false);

    cal_anoE.requestFocus();
    activar(navegador.ADDNEW,true);
    mensaje("Insertando ....");
  }
    @Override
  public void canc_addnew() {
    activaTodo();
    mensajeErr("Insercion de Datos ... Cancelada");
    verDatos();
    mensaje("");
  }
  int cambiaLineaJT()
  {
        try
        {
            if (cal_mesE.getValorInt() == 0)
                return -1; // No hay producto ... paso

          
            if (cal_mesE.getValorInt() > 12)
            {
                mensajeErr("Mes no puede ser superior a 12");
                return 0;
            }
            if (cal_feciniE.isNull() || cal_feciniE.getError())
            {
                mensajeErr("Fecha Inicial NO valida");
                return 1;
            }
            if (cal_fecfinE.isNull() || cal_fecfinE.getError())
            {
                mensajeErr("Fecha Final NO valida");
                return 2;
            }
            if (Formatear.getYear(cal_feciniE.getDate()) != cal_anoE.getValorInt() )
            {
                mensajeErr("Año de fecha debe coincidir");
                return 1;
            }
             if (Formatear.getYear(cal_fecfinE.getDate()) != cal_anoE.getValorInt() )
            {
                mensajeErr("Año de fecha debe coincidir");
                return 2;
            }
            if (Formatear.comparaFechas(cal_feciniE.getDate(), cal_fecfinE.getDate()) >= 0)
            {
                mensajeErr("Fecha inicial debe ser inferior a Final");
                return 1;
            }
        } catch (ParseException ex)
        {
            Logger.getLogger(MantCalendar.class.getName()).log(Level.SEVERE, null, ex);
        }
          return -1;
  }
    @Override
  public void ej_addnew1() {
    jt.salirGrid();
    
    if (cambiaLineaJT()>=0)
    {
      jt.requestFocusSelected();
      return;
    }

    guardaDatos(cal_anoE.getValorInt());
    activaTodo();
    verDatos();
    mensaje("");
  }
    void guardaDatos(int calAno)
    {
      try {

        borDatos(calAno);

        int nRow = jt.getRowCount();
        dtAdd.addNew("calendario");
        for (int n = 0; n < nRow; n++)
        {
          if ( jt.getValorInt(n,0)==0 )
            continue;
          dtAdd.addNew();
          dtAdd.setDato("cal_ano",calAno);
          dtAdd.setDato("cal_mes",jt.getValorInt(n,0));
          dtAdd.setDato("cal_fecini",jt.getValDate(n,1,"dd-MM-yyyy"));
          dtAdd.setDato("cal_fecfin",jt.getValDate(n,2,"dd-MM-yyyy"));

          dtAdd.update(stUp);
        }
        ctUp.commit();
        mensajeErr("Datos ... Guardados");
      } catch (Exception k)
      {
        Error("Error en La insercion de Fechas",k);
        return;
      }
    }

    void  borDatos(int calAno) throws SQLException,java.text.ParseException
    {
      s = "DELETE FROM calendario " +
          " WHERE cal_ano =  "+calAno;
      stUp.executeUpdate(dtAdd.parseaSql(s));
    }
    @Override
   public void PADDelete() {
    Baceptar.setEnabled(true);
    Bcancelar.setEnabled(true);
    Bcancelar.requestFocus();
    mensaje("Borrando ....");
  }
    @Override
  public void ej_delete1() {
    try
    {
      borDatos(cal_anoE.getValorInt());
      ctUp.commit();
      rgSelect();
    } catch (Exception k)
    {
      Error("Error al borrar datos",k);
    }
    activaTodo();

    verDatos();
    mensaje("");
    mensajeErr("Datos .... Borrados");

  }
    @Override
  public void canc_delete() {
    activaTodo();
    verDatos();
    mensaje("");
    mensajeErr("Borrado de Registro ... ANULADO");
  }
  @Override
  public void PADEdit() {
    mensaje("Editando ....");
    activar(true);

    Pcabe.setEnabled(false);

    jt.requestFocusInicio();

  }
    @Override
  public void ej_edit1() {
      jt.salirGrid();

    if (cambiaLineaJT()>=0)
    {
      jt.requestFocusSelected();
      return;
    }
  
    guardaDatos(cal_anoE.getValorInt());
    activaTodo();
    verDatos();
    mensaje("");
    mensajeErr("Datos Modificados...");
  }
    @Override
  public void canc_edit() {
    activaTodo();
    mensajeErr("Modificacion de Datos ... Cancelada");
    verDatos();
    mensaje("");
  }
  
  public static java.util.Date getFechaInicio(DatosTabla dt,int mes, int ano) throws SQLException
  {
      if (!dt.select("select * from calendario where cal_ano= "+ano+" and cal_mes="+mes))
          return null;
      return dt.getDate("cal_fecini");
  }
  public static java.util.Date getFechaFinal(DatosTabla dt,int mes, int ano) throws SQLException
  {
      if (!dt.select("select * from calendario where cal_ano= "+ano+" and cal_mes="+mes))
          return null;
      return dt.getDate("cal_fecfin");
  }
  
  public static  java.util.Date getFechaFinal(DatosTabla dt,java.util.Date fecha) throws SQLException
  {
      return getFechaFinal(dt,Formatear.getMonth(fecha),Formatear.getYear(fecha));      
  }
  public static  java.util.Date getFechaInicio(DatosTabla dt,java.util.Date fecha) throws SQLException
  {
      return getFechaInicio(dt,Formatear.getMonth(fecha),Formatear.getYear(fecha));      
  }
}
