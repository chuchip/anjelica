package gnu.chu.anjelica.almacen;

/**
 *
 * <p>Título: pdregalm</p>
 * <p>Descripcion: Mantenimientos de Regularizaciones en almacén (Actualiza la tabla v_regstock) </p>
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
 * <p>Empresa: MISL</p>
 * @author chuchiP
 * @version 1.0
 */
import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import java.awt.*;
import javax.swing.BorderFactory;
import java.sql.*;
import gnu.chu.camposdb.*;
import java.awt.event.*;
import java.util.*;
import gnu.chu.Menu.*;
import gnu.chu.interfaces.PAD;
import gnu.chu.interfaces.ejecutable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JMenuItem;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class pdregalm extends ventanaPad implements PAD
{
  final int JT_PROCOD=0;
  final int JT_PRONOMB=1;
  final int JT_FECHA=2;
  final int JT_EJERC=3;
  final int JT_SERIE=4;
  final int JT_LOTE=5;
  final int JT_INDIV=6;
  final int JT_TIPREG=7;
  final int JT_UNID=8;
  final int JT_CANTI=9;
  final int JT_COSTO=10;
  final int JT_ID=11;
  JMenuItem MirMvto = new JMenuItem("Ver Mvtos");
  paregalm pRegAlm; // Panel Regularizaciones Almacen
  int ROWSGRID=20;
  String QfecIni,QfecFin,QproCodi,QtirCodi;
  String afeStk;
  CPanel Pprinc = new CPanel();
  CButton BImportar = new CButton("Importar",Iconos.getImageIcon("choose"));
//  CButton Bcancelar = new CButton("Cancelar",Iconos.getImageIcon("cancel"));

  String s;
  Cgrid jt = new Cgrid(12);
  CPanel Pcond = new CPanel();
  CLabel cLabel12 = new CLabel();
  CTextField feciniE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CTextField fecfinE = new CTextField(Types.DATE,"dd-MM-yyyy");
  CLabel cLabel13 = new CLabel();
  CLinkBox tir_codiE1 = new CLinkBox();
  CLabel cLabel14 = new CLabel();
  CTextField rgs_numeE = new CTextField(Types.DECIMAL,"##,###,##9");
  CLabel rgs_numeL = new CLabel("Identif.");
  proPanel pro_codiE1 = new proPanel()
  {
    /**
     * Por si alguien quiere saber si se han llenado los campos.
     */
        @Override
    protected void despuesLlenaCampos()
    {
      pRegAlm.stp_unactE.setValorDec(1);
      Baceptar.doClick();
    }
  };
  CLabel cLabel15 = new CLabel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CCheckBox opVerInv = new CCheckBox();
  CLabel cLabel1 = new CLabel();
  CTextField linPantE = new CTextField(Types.DECIMAL,"##9");
  boolean P_ADMIN=false;
  
  public pdregalm(EntornoUsuario eu, Principal p)
  {
      this(eu,p,null);
  }
    
  public pdregalm(EntornoUsuario eu, Principal p,Hashtable<String,String> ht) {
     EU=eu;
     vl=p.panel1;
     jf=p;
     eje=true;
     setAcronimo("mareal");
     setTitulo("Mant. Regularizaciones de Almacen");

     try  {
        if (ht!=null)
        {
            if (ht.get("admin") != null)
             P_ADMIN = Boolean.parseBoolean(ht.get("admin"));   
        }
       if(jf.gestor.apuntar(this))
           jbInit();
       else
         setErrorInit(true);
     }
     catch (Exception e) {
       setErrorInit(true);
     }
   }
    public pdregalm(gnu.chu.anjelica.menu p,EntornoUsuario eu) {
        this(p,eu,null);
    }

   public pdregalm(gnu.chu.anjelica.menu p,EntornoUsuario eu,Hashtable<String,String> ht) {

     EU=eu;
     vl=p.getLayeredPane();
     setTitulo("Mant. Regularizaciones de Almacen");
     eje=false;

     try  {
         if (ht!=null)
         {
          if (ht.get("admin") != null)
             P_ADMIN = Boolean.parseBoolean(ht.get("admin"));
         }
       jbInit();
     }
     catch (Exception e) {
       ErrorInit(e);
     }
   }

   private void jbInit() throws Exception
   {
     iniciarFrame();
     this.setSize(new Dimension(583, 562));
     this.setVersion("2017-11-12 "+(P_ADMIN?"  ADMIN":""));
     Pprinc.setDefButton(Baceptar);
     Pprinc.setDefButtonDisable(false);
     Pprinc.setLayout(null);
     fecfinE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
     feciniE.setText(Formatear.sumaDias(fecfinE.getText(), "dd-MM-yyyy", -30));

     strSql = "select * from regalmacen WHERE rgs_fecha >= to_date('" + feciniE.getText() +
         "','dd-MM-yyyy')" +
         " and rgs_fecha <= to_date('" + fecfinE.getText() + "','dd-MM-yyyy')" +
         " AND tir_codi NOT IN (SELECT tir_codi FROM v_motregu WHERE tir_afestk = '=') " +
         " ORDER BY rgs_fecha, pro_codi,eje_nume,emp_codi,pro_serie,pro_nupar,pro_numind";
     statusBar = new StatusBar(this);
     nav = new navegador(this, false, navegador.NORMAL);
     conecta();
     pRegAlm = new paregalm(EU, dtStat, dtAdd, vl, this);
     pRegAlm.setAdmin(P_ADMIN);
     iniciar(this);

    Pcond.setBorder(BorderFactory.createLoweredBevelBorder());
    Pcond.setMaximumSize(new Dimension(498, 64));
    Pcond.setMinimumSize(new Dimension(498, 64));
    Pcond.setPreferredSize(new Dimension(498, 64));
    Pcond.setLayout(null);
    cLabel12.setText("De Fecha");
    cLabel12.setBounds(new Rectangle(4, 5, 60, 15));
    feciniE.setBounds(new Rectangle(60, 5, 86, 16));
    fecfinE.setBounds(new Rectangle(251, 5, 86, 16));
    cLabel13.setBounds(new Rectangle(195, 5, 60, 15));
    cLabel13.setText("A Fecha");
    tir_codiE1.setBounds(new Rectangle(61, 22, 249, 17));
    tir_codiE1.setAncTexto(30);
    cLabel14.setBounds(new Rectangle(2, 22, 57, 16));
    cLabel14.setText("Tipo Mvto.");
    pro_codiE1.setAncTexto(50);
    pro_codiE1.setBounds(new Rectangle(60, 41, 300, 18));
    rgs_numeL.setBounds(new Rectangle(363, 41, 50, 18));
    rgs_numeE.setBounds(new Rectangle(415, 41, 80, 18));
    cLabel15.setText("Producto");
    cLabel15.setBounds(new Rectangle(2, 42, 53, 17));
    Baceptar.setMaximumSize(new Dimension(103, 24));
    Baceptar.setMinimumSize(new Dimension(103, 24));
    Baceptar.setPreferredSize(new Dimension(103, 24));
    BImportar.setMaximumSize(new Dimension(103, 24));
    BImportar.setMinimumSize(new Dimension(103, 24));
    BImportar.setPreferredSize(new Dimension(103, 24));
    Bcancelar.setMaximumSize(new Dimension(103, 24));
    Bcancelar.setMinimumSize(new Dimension(103, 24));
    Bcancelar.setPreferredSize(new Dimension(103, 24));
//    dtAdd=new DatosTabla(ct);

    Baceptar.setMargin(new Insets(0, 0, 0, 0));
//    Bkil.setBounds(new Rectangle(380, 36, 14, 10));

    ArrayList v=new ArrayList();
    v.add("Prod."); // 0
    v.add("Nombre Prod"); // 1
    v.add("Fecha"); // 2
    v.add("Ejer"); // 3
//    v.add("Emp"); // 4
    v.add("Serie"); // 5
    v.add("Lote"); // 6
    v.add("Ind");  // 7
    v.add("Tipo Reg."); // 8
    v.add("Unid."); // 9
    v.add("Kilos"); // 10
    v.add("Precio"); // 11
    v.add("rowid"); // 12
    jt.setCabecera(v);
    jt.setAnchoColumna(new int[]{40,150,90,40,30,50,40,90,60,60,60,1});
    jt.setAlinearColumna(new int[]{0,0,1,2,1,2,2,0,2,2,2,2});
    jt.setFormatoColumna(8, "---9");
    jt.setFormatoColumna(9, "---,--9.99");
    jt.setFormatoColumna(10, "---,--9.999");
    jt.setFormatoColumna(11, "#######");
    jt.setAjustarGrid(true);
    jt.getPopMenu().add(MirMvto);
    pRegAlm.setPreferredSize(new Dimension(519, 172));
    pRegAlm.setMinimumSize(new Dimension(519, 172));
    pRegAlm.setBorder(BorderFactory.createRaisedBevelBorder());
    pRegAlm.setMaximumSize(new Dimension(519, 172));

    jt.setMaximumSize(new Dimension(526, 230));
    jt.setMinimumSize(new Dimension(526, 230));
    jt.setPreferredSize(new Dimension(526, 230));
//    cli_codiE.setVisible(false);
    Bcancelar.setMargin(new Insets(0, 0, 0, 0));
    opVerInv.setText("Ver Inventarios");
    opVerInv.setBounds(new Rectangle(366, 22, 125, 17));

    cLabel1.setVerifyInputWhenFocusTarget(true);
    cLabel1.setText("Lineas por Pant.");
    cLabel1.setBounds(new Rectangle(344, 5, 93, 16));
    linPantE.setToolTipText("Define el Numero de Lineas por pantalla a mostrar");

    linPantE.setBounds(new Rectangle(442, 5, 40, 16));
    this.getContentPane().add(Pprinc,  BorderLayout.CENTER);
    Pprinc.setLayout(gridBagLayout1);
    Pprinc.add(Pcond,    new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    Pprinc.add(jt,       new GridBagConstraints(0, 1, 3, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    Pprinc.add(pRegAlm,        new GridBagConstraints(0, 2, 3, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 3, 0), 0, 0));
    Pprinc.add(Baceptar,     new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 50, 0, 0), 0, 0));
    Pprinc.add(Bcancelar,     new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 50), 0, 0));
    Pprinc.add(BImportar,     new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 50, 0, 0), 0, 0));

    Pcond.add(cLabel12, null);
    Pcond.add(feciniE, null);
    Pcond.add(cLabel15, null);
    Pcond.add(tir_codiE1, null);
    Pcond.add(cLabel14, null);
    Pcond.add(pro_codiE1, null);
    Pcond.add(rgs_numeE, null);
    Pcond.add(rgs_numeL, null);
    Pcond.add(opVerInv, null);
    Pcond.add(fecfinE, null);
    Pcond.add(cLabel13, null);
    Pcond.add(cLabel1, null);
    Pcond.add(linPantE, null);

    pro_codiE1.iniciar(dtStat, this, vl, EU);
  //  pro_codiE1.setAceptaInactivo(false);
 }
   @Override
 public void afterConecta() throws SQLException
 {
   tir_codiE1.setFormato(Types.DECIMAL, "##9", 3);
   tir_codiE1.setAnchoComboDesp(450);
//     tir_codiE1.setFormato(Types.DECIMAL, "##9", 3);

   s = "SELECT * FROM v_motregu ORDER BY tir_nomb";
   if (dtCon1.select(s))
   {
     do
     {
       tir_codiE1.addDatos(dtCon1.getString("tir_codi"),
                           dtCon1.getString("tir_nomb") + "  (" + dtCon1.getString("tir_afestk") +
                           ")", true);
     }
     while (dtCon1.next());
   }
 }
    @Override
 public void iniciarVentana() throws Exception
 {
   linPantE.setValorInt(ROWSGRID);
   linPantE.resetCambio();
   linPantE.setDependePadre(false);
   pRegAlm.iniciar(dtCon1);
  

   pRegAlm.setDefButton(Baceptar);
   Pcond.setDefButton(Baceptar);
   swThread=true; // Tratar Ej_addnew1 y compa�ia en modo THREAD
   verDatos();
   activarEventos();
 }
 void activarEventos()
 {
    MirMvto.addActionListener(new java.awt.event.ActionListener()
      {
          @Override
          public void actionPerformed(java.awt.event.ActionEvent evt) {
              mostrarMvtos(jt.getSelectedRowDisab());
          }
      });
      BImportar.addActionListener(new java.awt.event.ActionListener()
      {
          @Override
          public void actionPerformed(java.awt.event.ActionEvent evt) {
             importarRegula();
          }
      });
   linPantE.addFocusListener(new FocusAdapter()
   {
            @Override
     public void focusLost(FocusEvent e)
     {
       if (!linPantE.hasCambio())
         return;
       linPantE.resetCambio();
       ROWSGRID=linPantE.getValorInt();
       try {
         rgSelect();
         verDatos();
       } catch (Exception k)
       {
         Error("Error al volver a cargar datos en grid",k);
       }
     }
   });
   jt.addMouseListener(new MouseAdapter()
   {
            @Override
     public void mouseClicked(MouseEvent e)
     {
       if (e.getClickCount() >= 2)
       {
         if (nav.pulsado == navegador.NINGUNO && !jt.isVacio())
           PADEdit();
       }
     }

   });
   jt.tableView.getSelectionModel().addListSelectionListener(new
        ListSelectionListener()
    {
      @Override
      public void valueChanged(ListSelectionEvent e)
      {

        if (e.getValueIsAdjusting() || !jt.isEnabled())
          return;
        try {
          pRegAlm.verDatReg(jt.getValorInt(JT_ID));
        } catch (Exception k)
        {
          Error("Error al Ver Datos Regularizacion",k);
        }
      }
    });
 }


 public void PADPrimero(){
   try {
     dtCons.first();
     verDatos();
   } catch (Exception k)
   {
     Error("Error al ir al primer Registro",k);
   }
 }
 void mostrarMvtos(int linea) {
    if (jf==null)
        return;
    ejecutable prog;
    if ((prog = jf.gestor.getProceso(Comvalm.getNombreClase())) == null)
        return;
    gnu.chu.anjelica.almacen.Comvalm cm = (gnu.chu.anjelica.almacen.Comvalm) prog;//   
    cm.setProCodi(jt.getValorInt(linea,JT_PROCOD));
    cm.setLote(jt.getValorInt(linea, JT_LOTE));
    cm.setIndividuo(jt.getValorInt(linea, JT_INDIV));
    cm.setSerie(jt.getValString(linea, JT_SERIE));
    cm.setEjercicio(jt.getValorInt(linea, JT_EJERC));
    cm.ejecutaConsulta();
    jf.gestor.ir(cm);
  }
  @Override
 public void PADAnterior(){
   try
   {
     if (ROWSGRID==0)
       return;
     if (!dtCons.previous(ROWSGRID + jt.getRowCount()))
     {
       mensajeErr("Ya estaba en el primer Registro");
       return;
     }
     verDatos();
   }
   catch (Exception k)
   {
     Error("Error al ir al anterior Registro", k);
   }

 }

 public void PADSiguiente(){
   try
   {
     if (ROWSGRID==0)
       return;
     verDatos();
   }
   catch (Exception k)
   {
     Error("Error al ir al Siguiente Registro", k);
   }
 }

 public void PADUltimo(){
   try
 {
   if (ROWSGRID==0)
     return;
   dtCons.last();
   if (dtCons.previous(ROWSGRID))
     verDatos();
 }
 catch (Exception k)
 {
   Error("Error al ir al Ultimo Registro", k);
 }

 }

    @Override
 public void PADQuery()
 {
   try {
     mensaje("Introduzca Criterios de Busqueda");
     Pcond.resetTexto();
     activar(true,navegador.QUERY);
     fecfinE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
     feciniE.setText(Formatear.sumaDias(fecfinE.getText(), "dd-MM-yyyy", -30));
     tir_codiE1.setValorInt(0);
     feciniE.requestFocus();
   } catch (Exception k)
   {
     Error("Error en padquery",k);
   }
 }

    @Override
public void ej_query()
{
  try
  {
    if (feciniE.isNull() || feciniE.getError())
    {
      mensajeErr("Fecha Inicio NO VALIDA");
      feciniE.requestFocus();
      return;
    }
    if (fecfinE.isNull() || fecfinE.getError())
    {
      mensajeErr("Fecha FINAL NO VALIDA");
      fecfinE.requestFocus();
      return;
    }
    if (rgs_numeE.getValorInt()!=0)
        strSql = "select * from regalmacen WHERE rgs_nume = " + rgs_numeE.getValorInt();
    else
        strSql = "select * from regalmacen WHERE "        
        + " rgs_fecha >= to_date('" + feciniE.getText() + "','dd-MM-yyyy')" +
        " and rgs_fecha <= to_date('" + fecfinE.getText() + "','dd-MM-yyyy')" +        
        (pro_codiE1.getValorInt() != 0 ? " AND pro_codi = " + pro_codiE1.getValorInt() : "") +
        (tir_codiE1.getValorInt() != 0 ? " AND tir_codi = " + tir_codiE1.getValorInt() : "") +
        (opVerInv.isSelected()?"": " AND tir_codi NOT IN (SELECT tir_codi FROM v_motregu WHERE tir_afestk = '=') ");
     strSql+=" ORDER BY rgs_fecha, pro_codi,eje_nume,emp_codi,pro_serie,pro_nupar,pro_numind";
//    debug("strSql: "+strSql);
    if (!dtCons.select(strSql))
      mensajeErr("NO ENCONTRADOS REGISTROS CON ESTOS CRITERIOS");
    else
      mensajeErr("NUEVOS CRITERIOS DE BUSQUEDA  ... INTRODUCIDOS");
    jt.setEnabled(false);
    jt.removeAllDatos();
    verDatos();
    activaTodo();
    mensaje("");
    nav.setPulsado(navegador.NINGUNO);
  } catch (Exception k)
  {
    Error("Error al introducir criterios de busqueda",k);
  }
}
  @Override
 public void ej_query1(){
}
  @Override
 public void canc_query(){
  feciniE.setText(QfecIni);
  fecfinE.setText(QfecFin);
  pro_codiE1.setText(QproCodi);
  tir_codiE1.setText(QtirCodi);
  mensaje("");
  mensajeErr("Introducion Criterios de Busqueda ... CANCELADA");
  activaTodo();
  nav.setPulsado(navegador.NINGUNO);
 }

    @Override
 public void PADEdit()
 {
   nav.pulsado=navegador.EDIT;
   if (jt.isVacio())
   {
     activaTodo();
     mensajeErr("NO hay registros SELECIONADOS");
     return;
   }
   try
   {
     s = "select * from regalmacen WHERE rgs_nume = " + jt.getValorInt(JT_ID);
     if (!dtAdd.select(s, true))
     {
       mensaje("");
       mensajeErr("Regularizacion NO encontrada ... Probablemente se borro");
       activaTodo();
       return;
     }
   }
   catch (Exception k)
   {
     Error("Error al bloquear registro de Stock", k);
   }

   jt.setEnabled(false);
   activar(true,navegador.ADDNEW);
   pRegAlm.PADEdit();
   feciniE.requestFocus();
   mensaje("Editando ... registro");
 }

    @Override
 public void ej_edit1(){
   try
   {
     pRegAlm.borrarRegistro(dtAdd.getDouble("rgs_kilos"),dtAdd.getInt("rgs_canti"),dtAdd.getInt("tir_codi"));
     pRegAlm.setRegNume(jt.getValorInt(JT_ID));
     pRegAlm.insRegistro();
     ctUp.commit();
     mensaje("");
     this.setEnabled(true);
     activaTodo();
     dtCons.previous(jt.getRowCount()-1);
     verDatos();
     nav.setPulsado(navegador.NINGUNO);
     mensajeErr("Registro .. MODIFICADO");
   } catch (Exception k)
   {
     Error("Error al Modificar Datos",k);
   }
 }

 public void canc_edit()
 {
   try
   {
     ctUp.rollback();
   } catch (Exception k){}
   mensajeErr("Modificacion de Registro ... Cancelado");
   mensaje("");
   nav.pulsado = navegador.NINGUNO;
   activaTodo();
   jt.setEnabled(true);
 }
 
    @Override
 public void PADAddNew(){
   activar(true,navegador.ADDNEW);
   jt.setEnabled(false);
   BImportar.setEnabled(true);
   pRegAlm.addNew();
   mensaje("Insertando NUEVO Registro");
 }
 void importarRegula()
 {
      try
      {
          if (pRegAlm.cci_fecconE.isNull() )
          {
              msgBox("Introduzca Fecha Movimiento");
              pRegAlm.cci_fecconE.requestFocus();
              return;
          }
          if (!pRegAlm.tir_codiE.controla(true))
          {
              msgBox("Tipo Regularización No valido");
              pRegAlm.tir_codiE.requestFocus();
              return;
          }
          s="select * from v_coninvent where cci_feccon='"+pRegAlm.cci_fecconE.getFechaDB()+"'";
          if (!dtCon1.select(s))
          {
              msgBox("No encontraro Control Inventario en esta fecha");
              return;
          }
          int nReg=0;
          do
          {
              paregalm.insRegularizacion(dtAdd, pRegAlm.cci_fecconE.getDate(),
                  dtCon1.getInt("pro_codi"),dtCon1.getInt("prp_empcod"),dtCon1.getInt("prp_ano") ,
                  dtCon1.getString("prp_seri"), dtCon1.getInt("prp_part"),  dtCon1.getInt("prp_indi"),
                   dtCon1.getInt("lci_numind"),dtCon1.getDouble("lci_peso"),dtCon1.getInt("alm_codi"),
                  pRegAlm.tir_codiE.getValorInt(),0,"Importado Inventario:"+pRegAlm.cci_fecconE.getFecha("dd-MM-yy"),
                  0,null,0,0,1,0,0,"",0,EU.usuario,0,0);
              nReg++;
          } while (dtCon1.next());
          dtAdd.commit();
           msgBox("Importado Control inventario. Numero Registros: "+nReg);
           nav.setPulsado(navegador.NINGUNO);
           this.setEnabled(true);
           activaTodo();
           jt.setEnabled(true);
      } catch (ParseException | SQLException ex)
      {
          Error("Error al Importar Inventario",ex);
      }
 }

    @Override
 public void ej_addnew1()
 {
   try
   {
     this.setEnabled(false);
     mensaje("Espere, Creando Mvto. de Regularizacion ...", false);
     pRegAlm.setRegNume(0);
     pRegAlm.insRegistro();
     ctUp.commit();
     mensaje("");
     mensajeErr("Regularizacion ... Generada");
     if (dtCons.getNOREG())
     {
       rgSelect();
       verDatos();
     }
   }
   catch (Exception k)
   {
     Error("Error al Insertar Registro", k);
     return;
   }
   nav.setPulsado(navegador.NINGUNO);
   this.setEnabled(true);
   activaTodo();
   jt.setEnabled(true);
 }

 public void canc_addnew(){
   mensajeErr("Insercion NUEVO Registro ... Cancelado");
   mensaje("");
   nav.pulsado=navegador.NINGUNO;
   activaTodo();
   jt.setEnabled(true);
 }

    @Override
 public void PADDelete(){
   if (jt.isVacio())
   {
     activaTodo();
     mensajeErr("NO hay registros SELECIONADOS");
     return;
   }
   try
   {
     s = "select * from regalmacen WHERE rgs_nume = " + jt.getValorInt(JT_ID);
     if (!dtAdd.select(s, true))
     {
       mensaje("");
       mensajeErr("Regularizacion NO encontrada ... Probablemente se borro");
       activaTodo();
       return;
     }
   }
   catch (SQLException k)
   {
     Error("Error al bloquear registro de Stock", k);
     return;
   }
   jt.setEnabled(false);
   Baceptar.setEnabled(true);
   Bcancelar.setEnabled(true);
   Bcancelar.requestFocus();
   mensaje("BORRANDO ... registro");

 }

 public void ej_delete1()
 {
   try
   {
     pRegAlm.borrarRegistro(dtAdd.getDouble("rgs_kilos"),dtAdd.getInt("rgs_canti"),dtAdd.getInt("tir_codi"));
     ctUp.commit();
     this.setEnabled(true);
     activaTodo();
     dtCons.previous(jt.getRowCount()-1);
     verDatos();
     mensaje("");
     mensajeErr("Regularizacion ... BORRADA");
   }
   catch (SQLException k)
   {
     Error("Error al Modificar Datos", k);
   }
   nav.setPulsado(navegador.NINGUNO);
 }


    @Override
 public void canc_delete()
 {
   try
   {
     ctUp.rollback();
   }
   catch (Exception k)  {}

   mensajeErr("BORRADO de Registro ... Cancelado");
   mensaje("");
   nav.pulsado = navegador.NINGUNO;
   activaTodo();
   jt.setEnabled(true);
 }

 public void activar(boolean b,int modo)
 {
   Baceptar.setEnabled(b);
   Bcancelar.setEnabled(b);
   BImportar.setEnabled(false);
   linPantE.setEnabled(!b);
   if (modo!=navegador.ADDNEW)
   {
     opVerInv.setEnabled(b);
     feciniE.setEnabled(b);
     fecfinE.setEnabled(b);
     tir_codiE1.setEnabled(b);
     pro_codiE1.setEnabled(b);
     rgs_numeE.setEnabled(b);
   }
   if (nav.modo == navegador.QUERY)
     return;
   pRegAlm.setActivo(b);
 }
    @Override
 public void activar(boolean b){
   activar(b,navegador.TODOS);
 }

 void verDatos()
 {
   try
   {
     if (dtCons.getNOREG() || dtCons.isLast())
       return;
     jt.setEnabled(false);
     jt.removeAllDatos();
     int n;
     for (n = 0; (n < ROWSGRID || ROWSGRID == 0); n++)
     {
       verDato(dtCons.getInt("rgs_nume"));
       if (!dtCons.next())
         break;
     }
     jt.requestFocusInicio();
     jt.setEnabled(true);
     pRegAlm.verDatReg(jt.getValorInt(JT_ID));
   }
   catch (Exception k)
   {
     Error("Error al ver datos", k);
   }

 }
 void verDato(int rgsNume) throws Exception
 {
   s="SELECT r.*,pro_nomb FROM regalmacen as r,v_articulo as p "+
       " WHERE rgs_nume = "+dtCons.getInt("rgs_nume")+
       " and r.pro_codi = p.pro_codi";
   if (!dtCon1.select(s))
     return;
   ArrayList v=new ArrayList();

   v.add(dtCon1.getString("pro_codi")); // 0
   v.add(dtCon1.getString("pro_nomb")); // 1
   v.add(new SimpleDateFormat("dd-MM-yy HH:mm").format(dtCon1.getTimeStamp("rgs_fecha"))); // 2
   v.add(dtCon1.getString("eje_nume")); // 3
//   v.add(dtCon1.getString("emp_codi")); // 4
   v.add(dtCon1.getString("pro_serie")); // 5
   v.add(dtCon1.getString("pro_nupar")); // 6
   v.add(dtCon1.getString("pro_numind")); // 7
   v.add(pRegAlm.tir_codiE.getValor(dtCon1.getString("tir_codi"))); // 8
   v.add(dtCon1.getString("rgs_canti")); // 9
   v.add(dtCon1.getString("rgs_kilos")); // 10
   v.add(dtCon1.getString("rgs_prregu")); // 11
   v.add(""+rgsNume);    // 12
   jt.addLinea(v);
 }

 @Override
 public boolean checkEdit()
 {
    return pRegAlm.checkCampos();
 }
 
    @Override
 public boolean checkAddNew()
 {   
    return pRegAlm.checkCampos();
 }
  public static String getNombreClase()
  {
   return "gnu.chu.anjelica.almacen.pdregalm";
  }
  public void setFecha(java.util.Date fecha)
  {
      pRegAlm.cci_fecconE.setDate(fecha);
  }
  public boolean inTransation()
  {
      return (nav.getPulsado()==navegador.ADDNEW || nav.getPulsado()==navegador.EDIT || nav.getPulsado()==navegador.DELETE);
  }
  public void setProCodi(int proCodi) 
  {
      pRegAlm.pro_codiE.setValorInt(proCodi,true);    
  }
  public void setLote(int lote)
  {
      pRegAlm.pro_loteE.setValorInt(lote);
  }
  public void setIndividuo(int indiv)
  {
      pRegAlm.pro_numindE.setValorInt(indiv);
  }
  public void setSerie(String serie)
  {
      pRegAlm.deo_serlotE.setText(serie);
  }
  public void setEjercicio(int ejerc)
  {
      pRegAlm.deo_ejelotE.setValorInt(ejerc);
  }
  public void setAlmacen(int almac)
  {
      pRegAlm.alm_codiE.setValorInt(almac);
  }       
  public void setNumeroRegula(int numReg)
  {
      rgs_numeE.setValorInt(numReg);
  }
}
