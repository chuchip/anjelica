package gnu.chu.anjelica.despiece;

import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import java.sql.*;
import gnu.chu.Menu.*;
import gnu.chu.anjelica.almacen.MvtosAlma;
import gnu.chu.anjelica.pad.MantCalendar;
import gnu.chu.interfaces.ejecutable;
import gnu.chu.sql.DatosTabla;
import java.awt.*;
import java.util.*;
import javax.swing.BorderFactory;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import net.sf.jasperreports.engine.*;
/**
 * <p>Titulo: cldespsv</p>
 * <p>Descripción: Consulta/ LISTADO Despieces Sin Valorar o con descuadres</p>
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
 * <p>Empresa: MISL</p>
 * @author Chuchi P
 * @version 1.0
 */
public class cldespsv extends ventana
{
  private JMenuItem menuItem=new JMenuItem("Regenerar");
  private JMenuItem MIgrasa=new JMenuItem("Grasa");
  String s;
  DatosTabla dtSta2;
  DatosTabla dtSta3;
  CLabel mesL = new CLabel("Mes");
  CComboBox mesE = new CComboBox();
  private char swElec;
  final private char DESCUADRE='D';
  final private char VALORAR='S';
  final private char GRUPO_DESC='G';
  final private char LISTAR='L';
  CPanel Pprinc = new CPanel();
  CTextField feciniE = new CTextField(Types.DATE, "dd-MM-yyyy");
  CLabel cLabel1 = new CLabel();
  CButtonMenu Baceptar = new CButtonMenu();
  CLabel cLabel2 = new CLabel();
  CPanel PintrDatos = new CPanel();
  CTextField fecfinE = new CTextField(Types.DATE, "dd-MM-yyyy");
  Cgrid jt = new Cgrid(6);
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  private boolean P_ADMIN=false;
  
  public cldespsv(EntornoUsuario eu, Principal p)
  {
       this(eu,p,null);
  }
  public cldespsv(EntornoUsuario eu, Principal p,Hashtable ht)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    setTitulo("Cons/List Incidencias Despieces");

    try
    {
      if (ht!=null)
      {
        if (ht.get("admin") != null)
          P_ADMIN = Boolean.valueOf(ht.get("admin").toString()).
              booleanValue();
      }
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

  public cldespsv(gnu.chu.anjelica.menu p, EntornoUsuario eu,Hashtable ht)
  {

    EU = eu;
    vl = p.getLayeredPane();
    setTitulo("Cons/List Incidencias Despieces");
    eje = false;

    try
    {
      if (ht!=null)
      {
        if (ht.get("admin") != null)
          P_ADMIN = Boolean.valueOf(ht.get("admin").toString()).
              booleanValue();
      }
      jbInit();
    }
    catch (Exception e)
    {
      ErrorInit(e);
      setErrorInit(true);
    }
  }

  private void jbInit() throws Exception
  {
    iniciarFrame(); 
    this.setSize(442, 448);
    this.setVersion((P_ADMIN?"(MODO ADMINISTRADOR)":"")+"2013-10-21");

    statusBar=new StatusBar(this);
    conecta();
    for (int n=1;n<=12;n++)
        mesE.addItem(n);
    Pprinc.setLayout(gridBagLayout1);
    mesL.setBounds(new Rectangle(2,2,30,18));
    mesE.setBounds(new Rectangle(38,2,40,18));
    cLabel1.setBounds(new Rectangle(100, 5, 55, 18));
    feciniE.setBounds(new Rectangle(160, 5, 76, 18));
    cLabel1.setText("De Fecha");
    
    cLabel2.setBounds(new Rectangle(250, 6, 19, 17));
    fecfinE.setBounds(new Rectangle(275, 5, 77, 18));
    Baceptar.setBounds(new Rectangle(125, 26, 125, 24));
    Baceptar.setText("Aceptar");
    Baceptar.addMenu("Sin Valorar");
    Baceptar.addMenu("Descuadre");
//    Baceptar.addMenu("Grupos Desc.");
    Baceptar.addMenu("Listar Descuadres");
    if (P_ADMIN)
    {
     Baceptar.addMenu("Regenerar");
     jt.getPopMenu().add(menuItem);
     jt.getPopMenu().add(MIgrasa);
    }
    Baceptar.setIcon(Iconos.getImageIcon("check"));
    Baceptar.setMargin(new Insets(0, 0, 0, 0));
    cLabel2.setText("A");
    
    PintrDatos.setBorder(BorderFactory.createRaisedBevelBorder());
    PintrDatos.setMaximumSize(new Dimension(408, 55));
    PintrDatos.setMinimumSize(new Dimension(408, 55));
    PintrDatos.setPreferredSize(new Dimension(408, 55));
    PintrDatos.setDefButton(Baceptar.getBotonAccion());
    PintrDatos.setLayout(null);
    
    Pprinc.setMaximumSize(new Dimension(32767, 32767));
  
    confGrid();
    this.getContentPane().add(statusBar,BorderLayout.SOUTH);
    this.getContentPane().add(Pprinc,  BorderLayout.CENTER);
    Pprinc.add(PintrDatos,    new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 0, 0));
    PintrDatos.add(mesL, null);
    PintrDatos.add(mesE, null);
    PintrDatos.add(feciniE, null);
    PintrDatos.add(cLabel1, null);
    PintrDatos.add(cLabel2, null);
    PintrDatos.add(fecfinE, null);
    PintrDatos.add(Baceptar, null);
   
    Pprinc.add(jt,   new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 3, 2, 1), -89, -137));
  }

  private void confGrid() throws Exception
  {
    ArrayList v=new ArrayList();
    v.add("Producto"); // 0
    v.add("Descripcion"); // 1
    v.add("Cant.Or."); // 2
    v.add("Cant.F."); // 3
    v.add("N. Desp"); // 4
    v.add("Fecha"); // 5
    jt.setCabecera(v);
    jt.setAnchoColumna(new int[]{50,200,60,60,60,90});
    jt.setAlinearColumna(new int[]{0,0,2,2,2,1});
    jt.setFormatoColumna(2,"---,--9.99");
    jt.setFormatoColumna(3,"---,--9.99");
    jt.setFormatoColumna(5,"dd-MM-yyyy");
    jt.setAjustarGrid(true);
    jt.setConfigurar("gnu.chu.anjelica.despiece.cldespsv",EU,dtStat);
    jt.setNumRegCargar(200);
  }

    @Override
  public void iniciarVentana() throws Exception
  {
    Pprinc.setDefButton(Baceptar.getBotonAccion());
   
    mesE.setValor(Formatear.getMonth(Formatear.getDateAct()));
    fecfinE.setDate(MantCalendar.getFechaFinal(dtStat, Formatear.getDateAct()));
    feciniE.setDate(MantCalendar.getFechaInicio(dtStat, Formatear.getDateAct()));
    activarEventos();
    feciniE.requestFocus();
    jt.setEnabled(true);
  }

  void activarEventos()
  {
      mesE.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            
            try
            {
                fecfinE.setDate(MantCalendar.getFechaFinal(dtStat, mesE.getValorInt(),EU.ejercicio));
                feciniE.setDate(MantCalendar.getFechaInicio(dtStat,  mesE.getValorInt(),EU.ejercicio));
            } catch (SQLException ex)
            {
                Error("Error al buscar fechas de calendario",ex);
            }
      }
      });
               
      MIgrasa.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
           if (jt.isVacio())
               return;
           if (! jt.getValString(jt.getSelectedRowDisab(), 1).contains("Kilos"))
               return;
           try {
                insertaGrasa(jt.getSelectedRowDisab());
           } catch (SQLException k)
           {
               Error("Error al insertar grasa en  despiece: "+jt.getValorInt(jt.getSelectedRowDisab(),3),k);
           }
        }
      });
      menuItem.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
           if (jt.isVacio())
               return;
           if (! jt.getValString(jt.getSelectedRowDisab(), 1).contains("Kilos"))
               return;
           int nLin=0;
           try {
               dtSta2=new DatosTabla(ct);
               dtSta3=new DatosTabla(ct);
               nLin=regeneraLinea(jt.getSelectedRowDisab());
           } catch (SQLException k)
           {
               Error("Error al regenerar despiece: "+jt.getValorInt(jt.getSelectedRowDisab(),3),k);
               return;
           }
           mensajeErr("Regenerado Despiece. "+nLin+" Insertadas");
      }
    });
    Baceptar.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        swElec=e.getActionCommand().charAt(0);
        buscaDatos();
    
      }
    });
    jt.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            if (e.getClickCount()<2)
              return;
            if (jf==null)
              return;
            String coment=jt.getValString(9);
            ejecutable prog;
            if ((prog=jf.gestor.getProceso("gnu.chu.anjelica.despiece.MantDesp"))==null)
              return;
            gnu.chu.anjelica.despiece.MantDesp cm=(gnu.chu.anjelica.despiece.MantDesp) prog;
            cm.PADQuery();
            if (jt.getValString(0).equals("G"))
                cm.setGrupo(jt.getValorInt(4));
            else
                cm.setDeoCodi(jt.getValString(4));
            cm.ej_query();
            jf.gestor.ir(cm);        
          }
      });
  }
  
  void imprimir()
  {
    this.setEnabled(false);
    try {
      mensaje("A esperar, Estoy Generando el Listado");
      s=getSelect();
      dtCon1.setStrSelect(s);
      ResultSet rs = ct.createStatement().executeQuery(dtCon1.getStrSelect());
      JasperReport jr;
      jr = gnu.chu.print.util.getJasperReport(EU, "cldespsv");

      java.util.HashMap mp = new java.util.HashMap();
      mp.put("fechas", "DEL : " + feciniE.getText() + " AL " + fecfinE.getText());
      JasperPrint jp = JasperFillManager.fillReport(jr, mp,
          new JRResultSetDataSource(rs));

      gnu.chu.print.util.printJasper(jp, EU);


      mensaje("");
      mensajeErr("Listado ... Generado");
      this.setEnabled(true);

    }
    catch (Exception k)
    {
      Error("Error al Generar el Listado", k);
    }
  }
  
  String getSelect()
  {
    return "select l.pro_codi,a.pro_nomb,0, def_kilos, c.deo_codi,deo_fecha " +
        " from v_despfin l,desporig c,v_articulo a " +
        " where l.emp_Codi = " + EU.em_cod +
        " and l.eje_nume= C.EJE_NUME " +
        " and a.pro_codi = l.pro_codi " +
        " and deo_fecha >= TO_DATE('" + feciniE.getText() + "','dd-MM-yyyy') " +
        " and deo_fecha <= TO_DATE('" + fecfinE.getText() + "','dd-MM-yyyy') " +
        " and c.deo_codi = l.deo_codi " +
        " and l.def_kilos> 0 " +
        " and a.pro_tiplot = 'V' " +
        " and def_prcost = 0 " +
//        " UNION " +
//        " select l.pro_codi,a.pro_nomb,def_kilos, c.deo_codi,deo_fecha " +
//        " from v_despfin l,v_despori c,v_articulo a " +
//        " where c.emp_Codi = " + EU.em_cod +
////        " and c.eje_nume= " + EU.ejercicio +
//        " and l.emp_Codi = C.EMP_CODI " +
//        " and l.eje_nume= C.EJE_NUME " +
//        " and a.pro_codi = l.pro_codi " +
//        " and deo_fecha >= TO_DATE('" + feciniE.getText() + "','dd-MM-yyyy') " +
//        " and deo_fecha <= TO_DATE('" + fecfinE.getText() + "','dd-MM-yyyy') " +
//        " and c.deo_codi = l.deo_codi " +
//        " and l.def_kilos> 0 " +
//         " and a.pro_tiplot = 'V' " + // Excluir comentarios
//        " and (not exists (select * from grupdesp " +
//        " WHERE eje_nume = c.eje_nume" +
//        " AND  emp_codi = c.emp_codi " +
//        " AND grd_nume = c.deo_numdes " +
//        " and (grd_valor = 'S' OR grd_nume >= 5000 or grd_nume=99)) OR c.deo_numdes < 99) " +
        " order by 5,4";
  }
  void buscaDatos()
  {
      if (feciniE.isNull())
      {
        mensajeErr("Introduzca Fecha Inicio");
        feciniE.requestFocus();
        return;
      }

      if (fecfinE.isNull())
      {
        mensajeErr("Introduzca Fecha Final");
        fecfinE.requestFocus();
        return;
      }
      if (swElec=='R')
      {
          regenerar();
          return;
      }
      new miThread("") {
          @Override
          public void run() {
              switch (swElec)
              {
                  case DESCUADRE:
                      buscaDespDesc();
                      break;
                  case VALORAR:
                      buscaDespSinValorar();
                      break;
                  case GRUPO_DESC:
                      buscaDespDescGrupo();
                      break;
                  case LISTAR:
                      imprimir();
              }
          }
      };
      
    }
    private int restaurarDespiece(int deoCodi,String serLote, int numLote) throws SQLException
    {
        int nLinIns=0;       
        s="select * from v_stkpart as s where s.eje_nume=2011 and s.pro_serie='"
            +serLote+ "' "+
            " and s.pro_nupar= "+numLote+
            " and stp_feccre between to_date('"+feciniE.getText()+"','dd-MM-yyyy')  "+
                " and to_date('"+fecfinE.getText()+"','dd-MM-yyyy') "+
            " and not exists (select * from v_despfin as f where eje_nume=2011 "+
            " and pro_lote= s.pro_nupar and s.pro_numind= f.pro_numind "+
            " and def_serlot = pro_serie "+
            " and f.pro_codi=s.pro_codi)";
        if (! dtCon1.select(s))
            return 0;
        s="SELECT  max(def_orden) as def_orden FROM v_despfin "+
                " where eje_nume=2011 and deo_codi="+deoCodi;
        dtStat.select(s);
        int defOrden=dtStat.getInt("def_orden",true)+1;
        s="SELECT  * FROM desporig where eje_nume=2011 and deo_codi="+deoCodi;
        dtStat.select(s);
        do
        {
            if (MvtosAlma.hasMvtosEntrada(dtSta2, dtCon1.getInt("pro_codi"),
                   1, 2011, serLote, numLote,
                   dtCon1.getInt("pro_numind"), 0,null)!=0)
               continue;
            s="insert into v_despfin values (2011,1,"+
                    deoCodi+","+defOrden+","+
                    dtCon1.getInt("pro_codi")+","+
                    "2011,1,'"+serLote+ "',"+
                    numLote+","+dtCon1.getInt("pro_numind")+
                    ",1,'V',"+dtStat.getInt("deo_numdes")+",1,'cpuente',1,-1,0,"+
                    dtCon1.getDouble("stp_kilini")+
                    ",to_date('"+dtStat.getFecha("deo_feccad","dd-MM-yyyy")+"','dd-MM-yyyy'),0,"
                    + "to_date('"+dtStat.getFecha("deo_fecha","dd-MM-yyyy")+"','dd-MM-yyyy'))";
            System.out.println(s+";");
           nLinIns++; 
           stUp.executeUpdate(s);
            defOrden++;
        } while (dtCon1.next());
        ctUp.commit();
        return nLinIns;
    }   
    void insertaGrasa(int deoCodi,double kilos,int numDesp) throws SQLException
    {
     s="insert into v_despfin values (2011,1,"+
                    deoCodi+","+1+","+
                    50+","+
                    "2011,1,'T',1,0,1,'V',"+numDesp+",1,'cpuente',1,-1,0,"+
                    kilos+
                    ",to_date('01-01-2011','dd-MM-yyyy'),0,"
                    + "to_date('01-01-2011','dd-MM-yyyy'))";   
     stUp.executeUpdate(s);
     ctUp.commit();
    }
    void insertaGrasa(int nRow) throws SQLException
    {
      if (jt.getValorDec(nRow,2)-jt.getValorDec(nRow,3)<=0)
          return;
      if (! jt.getValString(nRow,1).contains("Kilos"))
             return;
            
      if (jt.getValString(nRow,0).startsWith("D"))
        {
            s="select * from v_despfin where eje_nume=2011 and deo_codi="+jt.getValorInt(nRow,4);
            dtStat.select(s);
            insertaGrasa(jt.getValorInt(nRow,4),jt.getValorDec(nRow,2)-jt.getValorDec(nRow,3),0); 
        }
        
        if (jt.getValString(nRow,0).startsWith("G"))
        {

            s="select * from desporig as d where eje_nume=2011 and deo_numdes= "+jt.getValorInt(nRow,4)+
                   " order by deo_codi";
            dtStat.select(s);
            insertaGrasa(dtStat.getInt("deo_codi"),jt.getValorDec(nRow,2)-jt.getValorDec(nRow,3),dtStat.getInt("deo_numdes")); 
        }   
        mensajeErr("Registro de grasa INSERTADO");
    }
    int regeneraLinea(int nRow) throws SQLException
    {
        if (! jt.getValString(nRow,0).contains("Kilos"))
               return 0;
               
        if (jt.getValString(nRow,0).startsWith("D"))
        {
            s="select * from v_despfin where eje_nume=2011 and deo_codi="+jt.getValorInt(nRow,4);
            dtStat.select(s);
            return restaurarDespiece(jt.getValorInt(nRow,4),dtStat.getString("def_serlot"),dtStat.getInt("pro_lote")); 
        }
        int numLin=0;
        if (jt.getValString(nRow,0).startsWith("G"))
        {
             int numLote;
             String serLot;

             s="select * from v_despfin where eje_nume=2011 and def_numdes="+jt.getValorInt(nRow,4);
             dtStat.select(s);

             numLote=dtStat.getInt("pro_lote");
             serLot=dtStat.getString("def_serlot");
            s="select * from desporig as d where eje_nume=2011 and deo_numdes= "+jt.getValorInt(nRow,4)+
                   " order by deo_codi";
            dtSta3.select(s);
            do
            {
               s="select * from v_despfin where eje_nume=2011 and deo_codi="+dtSta3.getInt("deo_codi");
               if (dtStat.select(s))
                   numLin+=restaurarDespiece(dtSta3.getInt("deo_codi"),dtStat.getString("def_serlot"),dtStat.getInt("pro_lote")); 
               else
                   numLin+=restaurarDespiece(dtSta3.getInt("deo_codi"),serLot,numLote); 
            } while (dtSta3.next());  
            return numLin;
        }
        return 0;
    }
    void regenerar()
    {
       try {
            dtSta2=new DatosTabla(ct);
            dtSta3=new DatosTabla(ct);
            int n;
            for (n=0;n<jt.getRowCount();n++)
                regeneraLinea(n);
                
            mensajeErr("Regenerados despieces");
        } catch (SQLException k)
        {
            Error("Error al regenerar despieces",k);
        }
    }
  /**
   * Buscar despieces descuadrados antiguos por grupo
   */
    void buscaDespDescGrupo()
    {
       jt.removeAllDatos();
       this.setEnabled(false);
       mensaje("Espere, buscando despieces descuadrados");
       try {
           s="select distinct eje_nume,deo_numdes " +
            " from v_despori  " +
            " where emp_Codi = " + EU.em_cod +
            " and deo_fecha >= TO_DATE('" + feciniE.getText() + "','dd-MM-yyyy') " +
            " and deo_fecha <= TO_DATE('" + fecfinE.getText() + "','dd-MM-yyyy') " +
            " order by eje_nume, deo_numdes";
           if (! dtCon1.select(s))
           {
              msgBox("No encontrados despieces entre estas fechas");
              this.setEnabled(true);
              mensaje("");
              return;
           }
           s="select sum(deo_kilos) as deo_kilos,sum(deo_kilos*deo_prcost) as costo from v_despori "+
               " where emp_codi = " + EU.em_cod +
               " and eje_nume = ? "+
               " and deo_numdes = ? ";
           PreparedStatement psOrig=dtStat.getPreparedStatement(s);
           ResultSet rsOrig;

            s="select sum(def_kilos) as def_kilos,sum(def_kilos*def_prcost) as costo from v_despfin "+
                    " where emp_codi = " + EU.em_cod +
                    " and eje_nume = ? "+
                    " and def_numdes = ? ";
           PreparedStatement ps=dtStat.getPreparedStatement(s);
           ResultSet rs;
           double kilLin,kilOrig;
           double impLin,impOrig;
           jt.panelG.setVisible(false);
           do {
               mensajeRapido("Tratando Grupo de Despiece: "+ dtCon1.getInt("deo_numdes"));
                psOrig.setInt(1, dtCon1.getInt("eje_nume"));
                psOrig.setInt(2, dtCon1.getInt("deo_numdes"));
                rsOrig=psOrig.executeQuery();
                rsOrig.next();
                if (rsOrig.getObject("deo_kilos")==null)
                {
                    kilOrig=0;
                    impOrig=0;
                }
                else
                {
                    kilOrig=rsOrig.getDouble("deo_kilos");
                    impOrig=rsOrig.getDouble("costo");
                }
                ps.setInt(1, dtCon1.getInt("eje_nume"));
                ps.setInt(2, dtCon1.getInt("deo_numdes"));
                rs=ps.executeQuery();
                rs.next();
                if (rs.getObject("def_kilos")==null)
                {
                    kilLin=0;
                    impLin=0;
                }
                else
                {
                    kilLin=rs.getDouble("def_kilos");
                    impLin=rs.getDouble("costo");
                }
                if (! Formatear.esIgual(kilOrig, kilLin,  (kilLin*0.02) ))
                {
                    ArrayList v=new ArrayList();
                    v.add("G");
                    v.add("Diferencia en Kilos"); // 0
                    v.add(kilOrig); // 1
                    v.add(kilLin); // 2
                    v.add(dtCon1.getInt("deo_numdes")); // 3
                    v.add(""); // 4
                    jt.addLinea(v);
                }
                if (! Formatear.esIgual(impOrig, impLin,  (impLin*0.02) ))
                {
                    ArrayList v=new ArrayList();
                    v.add("G");
                    v.add("Diferencia en Costo"); // 0
                    v.add(impOrig); // 1
                    v.add(impLin); // 2
                    v.add(dtCon1.getInt("deo_numdes")); // 3
                    v.add(""); // 4
                    jt.addLinea(v);
                }
           } while (dtCon1.next());
           jt.requestFocusInicio();
           jt.panelG.setVisible(true);
           jt.setEnabled(true);
           mensaje("");
           mensajeErr("Consulta despieces descuadrados ... terminada");
           this.setEnabled(true);
        } catch (SQLException k)
        {
              Error("Error al Generar el Listado", k);
        }

    }
    /**
     * Busca despieces descuadrados
     * Comprueba que los kilos y el importe  de entrada y salida coincidan.
     */
    void buscaDespDesc()
    {
       jt.panelG.setVisible(false);
       jt.removeAllDatos();
       this.setEnabled(false);
       mensaje("Espere, buscando despieces descuadrados");
       try 
       {
           ResultSet rsOrig;
           PreparedStatement psOrig;
           PreparedStatement ps;
           ResultSet rs;
           double kilLin,kilOrig;
           double impLin,impOrig;
           s="select eje_nume,deo_codi,deo_fecha " +
            " from desporig  " +
            " where deo_fecha >= TO_DATE('" + feciniE.getText() + "','dd-MM-yyyy') " +
            " and deo_fecha <= TO_DATE('" + fecfinE.getText() + "','dd-MM-yyyy') " +
            " and deo_numdes = 0"+
            " order by eje_nume, deo_codi";
          if ( dtCon1.select(s))
          {
           s="select sum(deo_kilos) as deo_kilos,sum(deo_kilos*deo_prcost) as costo from desorilin "+
               " where eje_nume = ? "+
               " and deo_codi = ? ";
           psOrig=dtStat.getPreparedStatement(s);

           s="select sum(def_kilos) as def_kilos,sum(def_kilos*def_prcost) as costo from v_despfin "+
                    " where eje_nume = ? "+
                    " and deo_codi = ? ";
           ps=dtStat.getPreparedStatement(s);
          
           do {
               mensajeRapido("Tratando Despiece: "+ dtCon1.getInt("deo_codi"));
                psOrig.setInt(1, dtCon1.getInt("eje_nume"));
                psOrig.setInt(2, dtCon1.getInt("deo_codi"));
                rsOrig=psOrig.executeQuery();
                rsOrig.next();
                if (rsOrig.getObject("deo_kilos")==null)
                {
                    kilOrig=0;
                    impOrig=0;
                }
                else
                {
                    kilOrig=rsOrig.getDouble("deo_kilos");
                    impOrig=rsOrig.getDouble("costo");
                }
                ps.setInt(1, dtCon1.getInt("eje_nume"));
                ps.setInt(2, dtCon1.getInt("deo_codi"));
                rs=ps.executeQuery();
                rs.next();
                if (rs.getObject("def_kilos")==null)
                {
                    kilLin=0;
                    impLin=0;
                }
                else
                {
                    kilLin=rs.getDouble("def_kilos");
                    impLin=rs.getDouble("costo");
                }
                if (! Formatear.esIgual(kilOrig, kilLin,  (kilLin*0.01) ))
                {
                    ArrayList v=new ArrayList();
                    v.add("D"); // 0
                    v.add("Diferencia en Kilos");
                    v.add(kilOrig); // 1
                    v.add(kilLin); // 2
                    v.add(dtCon1.getInt("deo_codi")); // 3
                    v.add(dtCon1.getDate("deo_fecha")); // 4
                    jt.addLinea(v);
                }
                if (! Formatear.esIgual(impOrig, impLin,  (impLin*0.02) ))
                {
                    ArrayList v=new ArrayList();
                    v.add("D");
                    v.add("Diferencia en Costo"); // 0
                    v.add(impOrig); // 1
                    v.add(impLin); // 2
                    v.add(dtCon1.getInt("deo_codi")); // 3
                    v.add(dtCon1.getDate("deo_fecha")); // 4
                    jt.addLinea(v);
                }
            } while (dtCon1.next());
           }
           mensaje("Espere, Buscando grupos descuadrados...");
           // Buscamos por grupos
           s="select eje_nume,deo_numdes " +
            " from desporig  " +
            " where deo_fecha >= TO_DATE('" + feciniE.getText() + "','dd-MM-yyyy') " +
            " and deo_fecha <= TO_DATE('" + fecfinE.getText() + "','dd-MM-yyyy') " +
            " and deo_numdes > 0"+
            " group by eje_nume, deo_numdes"+
            " order by eje_nume, deo_numdes";
          if (dtCon1.select(s))
          {
               s="select sum(deo_kilos) as kilos,sum(deo_kilos*deo_prcost) as costo from desorilin as l "+
                   " where eje_nume = ? "+
                   " and deo_codi in (select deo_codi from desporig as c "+
                   " where c.eje_nume=l.eje_nume and c.deo_codi=l.deo_codi  "+
                   " and eje_nume = ? "+
                   " and deo_numdes= ?)";
               psOrig=dtStat.getPreparedStatement(s);

               s="select sum(def_kilos) as kilos,sum(def_kilos*def_prcost) as costo from v_despfin "+
                        " where eje_nume = ? "+
                        " and def_numdes = ? ";
               ps=dtStat.getPreparedStatement(s);


               do {
                   mensajeRapido("Tratando Grupo Despiece: "+ dtCon1.getInt("deo_numdes"));
                   psOrig.setInt(1, dtCon1.getInt("eje_nume"));
                   psOrig.setInt(2, dtCon1.getInt("eje_nume"));
                   psOrig.setInt(3, dtCon1.getInt("deo_numdes"));
                    rsOrig=psOrig.executeQuery();
                    rsOrig.next();
                    if (rsOrig.getObject("kilos")==null)
                    {
                        kilOrig=0;
                        impOrig=0;
                    }
                    else
                    {
                        kilOrig=rsOrig.getDouble("kilos");
                        impOrig=rsOrig.getDouble("costo");
                    }
                    ps.setInt(1, dtCon1.getInt("eje_nume"));
                    ps.setInt(2, dtCon1.getInt("deo_numdes"));
                    rs=ps.executeQuery();
                    rs.next();
                    if (rs.getObject("kilos")==null)
                    {
                        kilLin=0;
                        impLin=0;
                    }
                    else
                    {
                        kilLin=rs.getDouble("kilos");
                        impLin=rs.getDouble("costo");
                    }
                    if (! Formatear.esIgual(kilOrig, kilLin,  (kilLin*0.02) ))
                    {
                        ArrayList v=new ArrayList();
                        v.add("G");
                        v.add("Diferencia en Kilos"); // 0
                        v.add(kilOrig); // 1
                        v.add(kilLin); // 2
                        v.add(dtCon1.getInt("deo_numdes")); // 3
                        v.add(""); // 4
                        jt.addLinea(v);
                    }
                    if (! Formatear.esIgual(impOrig, impLin,  (impLin*0.02) ))
                    {
                        ArrayList v=new ArrayList();
                        v.add("G");
                        v.add("Diferencia en Costo"); // 0
                        v.add(Formatear.format(impOrig,"---,--9.99")); // 1
                        v.add(impLin); // 2
                        v.add(dtCon1.getInt("deo_numdes")); // 3
                        v.add(""); // 4
                        jt.addLinea(v);
                    }
               } while (dtCon1.next());
           }
           // Busco despieces con fechas fuera de rango en entradas
           s="select 'Entr' as tipo ,eje_nume,deo_codi,deo_fecha,del_numlin as linea " +
                " from v_despori  " +
                " where deo_fecha between TO_DATE('" + feciniE.getText() + "','dd-MM-yyyy') " +
                " and TO_DATE('" + fecfinE.getText() + "','dd-MM-yyyy') " +
                " and deo_tiempo::date not between TO_DATE('" + feciniE.getText() + "','dd-MM-yyyy') " +
                " and TO_DATE('" + fecfinE.getText() + "','dd-MM-yyyy') " +
                " union all "+
                "select 'Salid' as tipo,eje_nume,deo_codi,deo_fecha,def_orden as linea " +
                " from v_despsal  " +
                " where deo_fecha between TO_DATE('" + feciniE.getText() + "','dd-MM-yyyy') " +
                " and TO_DATE('" + fecfinE.getText() + "','dd-MM-yyyy') " +
                " and def_tiempo::date not between TO_DATE('" + feciniE.getText() + "','dd-MM-yyyy') " +
                " and TO_DATE('" + fecfinE.getText() + "','dd-MM-yyyy') " +                
                " order by 1,2";
           if (dtCon1.select(s))
           {
                do {
                        ArrayList v=new ArrayList();
                        v.add("D"); // 0
                        v.add("Diferencia Fechas en  Desp "+dtCon1.getString("tipo"));
                        v.add(0); // 1
                        v.add(dtCon1.getInt("linea")); // 2
                        v.add(dtCon1.getInt("deo_codi")); // 3
                        v.add(dtCon1.getDate("deo_fecha")); // 4
                        jt.addLinea(v);

                } while (dtCon1.next());
           }
           jt.requestFocusInicio();
           jt.panelG.setVisible(true);
           mensaje("");
           if (jt.isVacio())
             msgBox("No encontrados despieces entre estas fechas");
         
           mensajeErr("Consulta despieces descuadrados ... terminada");
           this.setEnabled(true);
           jt.setEnabled(true);
       } catch (SQLException k)
       {
              Error("Error al Generar el Listado", k);
       }
    }
    /**
     * Busca despieces sin valorar
     */
    void buscaDespSinValorar()
    {
      try
      {
        mensaje("Espere .. buscando Despieces sin valorar");
        this.setEnabled(false);
        s = getSelect();
        jt.removeAllDatos();
        dtCon1.select(s);
        jt.setDatos(dtCon1);
        this.setEnabled(true);
        jt.setEnabled(true);
        mensaje("");
        if (dtCon1.getNOREG())
          mensajeErr("NO encontrados Despieces SIN valorar");
        else
          mensajeErr("Despieces Encontrados....");
      }
      catch (Exception k)
      {
        Error("Error al Buscar datos", k);
      }
    }
}
