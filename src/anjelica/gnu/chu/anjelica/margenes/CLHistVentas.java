/**
 *
 * <p>Título: CLHisVentas</p>
 * <p>Descripción: Consulta/Listado Historico ventas</p>
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
 * <p>Empresa: MISL</p>
 * @author Chuchi P
 * @version 1.0
 */
package gnu.chu.anjelica.margenes;

import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.ventas.Covezore;
import gnu.chu.controles.StatusBar;
import gnu.chu.interfaces.ejecutable;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.miThread;
import gnu.chu.utilidades.ventana;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class CLHistVentas extends ventana {
 
  private  Date fecIniMes,fecFinMes,fecIniMesAnt,fecFinMesAnt; 
  String s;
  DatosTabla dtAdd;
  boolean actual=false;

  public CLHistVentas(EntornoUsuario eu, Principal p)
  {
    this(eu,p,null);
  }
  public CLHistVentas(EntornoUsuario eu, Principal p,Hashtable ht)
  {
    EU = eu;
    vl = p.panel1;
    jf = p;
    eje = true;

    setTitulo("Cons./Listado Historico Ventas");

    try
    {
      if (ht != null)
      {
        if (ht.get("actualizar") != null)
          actual = Boolean.valueOf(ht.get("actualizar").toString());

       }
       if (jf.gestor.apuntar(this))
        jbInit();
       else
        setErrorInit(true);
    }
    catch (Exception e)
    {
      logger.fatal("Error al iniciar clase",e);
      setErrorInit(true);
    }
  }

  public CLHistVentas(gnu.chu.anjelica.menu p, EntornoUsuario eu)
  {

    EU = eu;
    vl = p.getLayeredPane();
    setTitulo("Cons./Listado Historico Ventas");
    eje = false;
    actual=true;
    try
    {
      jbInit();
    }
    catch (Exception e)
    {
      setErrorInit(true);
    }
  }

  private void jbInit() throws Exception
  {
      iniciarFrame();

      this.setVersion("2014-10-06"+(actual?"(Actualizar)":""));
      statusBar = new StatusBar(this);
      this.getContentPane().add(statusBar, BorderLayout.SOUTH);
      conecta();

      initComponents();
      this.setSize(new Dimension(720,480));
      
      Pcabe.setDefButton(Baceptar.getBotonAccion());
  }
  @Override
  public void iniciarVentana() throws Exception
  {
    dtAdd=new DatosTabla(ctUp);
    anoE.setValorInt(EU.ejercicio);
    activarEventos();
  }
  private void activarEventos()
  {
    Baceptar.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            consultar();
        }
    });
    jtMes.tableView.getSelectionModel().addListSelectionListener(new
        ListSelectionListener()
    {
            @Override
      public void valueChanged(ListSelectionEvent e)
      {

        if (e.getValueIsAdjusting()) // && e.getFirstIndex() == e.getLastIndex())
          return;
        if (! jtMes.isEnabled() || jtMes.isVacio())
            return;
        cargaSemanas(jtMes.getValorInt(0));
        actualAcum(jtMes.getSelectedRow());
      }
    });
    
    jtMes.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e)
        {
            if (e.getClickCount()<2)
                return;
            if (jf==null)
                return;

            ejecutable prog;
            if ((prog=jf.gestor.getProceso(Covezore.getNombreClase()))==null)
                  return;
            Covezore cm=(Covezore) prog;
            if (cm.inTransation())
            {
                  msgBox("Consulta Ventas Zonas ocupado. No se puede realizar la busqueda");
                  return;
            }
            cm.setFechaInicio(fecIniMes);
            cm.setFechaInicioComp(fecIniMesAnt);
            cm.setFechaFinal(fecFinMes);
            cm.setFechaFinalComp(fecFinMesAnt);
            cm.setTodosProd(opTodosProd.isSelected());
            cm.lanzaConsulta();

            jf.gestor.ir(cm);
        }
     });
     jtSem.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e)
        {
            if (e.getClickCount()<2)
                return;
            if (jf==null)
                return;

            ejecutable prog;
            if ((prog=jf.gestor.getProceso(Covezore.getNombreClase()))==null)
                  return;
            Covezore cm=(Covezore) prog;
            if (cm.inTransation())
            {
                  msgBox("Consulta Ventas Zonas ocupado. No se puede realizar la busqueda");
                  return;
            }
            try {
                cm.setFechaInicio(jtSem.getValDate(jtSem.getSelectedRowDisab(),0));
                cm.setFechaFinal(jtSem.getValDate(jtSem.getSelectedRowDisab(),1));
                cm.setFechaInicioComp(jtSem.getValDate(jtSem.getSelectedRowDisab(),4));
                cm.setFechaFinalComp(jtSem.getValDate(jtSem.getSelectedRowDisab(),5));
            } catch (Exception k)
            {
                
            }
            cm.lanzaConsulta();

            jf.gestor.ir(cm);
        }
     });
  }

  void cargaSemanas(int mes)
  {
    try
    {
        jtSem.removeAllDatos();
        Date fecIniAnt,fecFinAnt,fecFinCal,fecFinCalAnt,fecIni; 
        fecIniAnt=null;
        fecFinAnt=null;
        fecFinCalAnt=null;
         
        s="SELECT * FROM calendario where cal_ano="+(anoE.getValorInt()-1)+
            " and cal_mes= "+mes;
        if ( dtCon1.select(s))
        {
            fecIniAnt=dtCon1.getDate("cal_fecini");
            fecIniMesAnt=dtCon1.getDate("cal_fecini");
            fecFinMesAnt=dtCon1.getDate("cal_fecfin");
            if (mes==1 && Formatear.comparaFechas(fecIniAnt,Formatear.getDate("01-01-"+Formatear.format(anoE.getValorInt()-1,"9999"), "dd-MM-yyyy"))==0)
            {
                GregorianCalendar gcal=new GregorianCalendar();
                for (int n=1;n<=8;n++)
                {
                    fecFinAnt=Formatear.sumaDiasDate(fecIniAnt,n);
                    gcal.setTime(fecFinAnt);
                    if (gcal.get(GregorianCalendar.DAY_OF_WEEK)==GregorianCalendar.SUNDAY  && n>1)
                       break;
                }
            }
            else
                fecFinAnt=Formatear.sumaDiasDate(fecIniAnt,7);
            fecFinCalAnt= dtCon1.getDate("cal_fecfin");
        }
        s="SELECT * FROM calendario where cal_ano="+anoE.getValorInt()+
            " and cal_mes= "+mes;
        if (! dtCon1.select(s))
            return;
        fecIni=dtCon1.getDate("cal_fecini");
        fecIniMes=dtCon1.getDate("cal_fecini");
        fecFinMes=dtCon1.getDate("cal_fecfin");
        Date fecFin=null;
        if (mes==1 && Formatear.comparaFechas(fecIni,Formatear.getDate("01-01-"+Formatear.format(anoE.getValorInt(),"9999"), "dd-MM-yyyy"))==0)
        {
           GregorianCalendar gcal=new GregorianCalendar();
           for (int n=1;n<=8;n++)
           {
               fecFin=Formatear.sumaDiasDate(fecIni,n);
               gcal.setTime(fecFin);
               if (gcal.get(GregorianCalendar.DAY_OF_WEEK)==GregorianCalendar.SUNDAY && n>1)
                  break;
           }
        }
        else
            fecFin=Formatear.sumaDiasDate(fecIni,7);
         
        fecFinCal  =dtCon1.getDate("cal_fecfin");
        while (Formatear.comparaFechas(fecIni,fecFinCal )<=0)
        {
            ArrayList v=new ArrayList();
            if (Formatear.comparaFechas(fecFin,fecFinCal )>0 && mes==12)
                fecFin=fecFinCal;
            addSemana(v,fecIni,fecFin,true);

            if (fecIniAnt!=null && Formatear.comparaFechas(fecIniAnt,fecFinCalAnt)<=0)
            {
              addSemana(v,fecIniAnt,fecFinAnt,false);
              fecIniAnt=Formatear.sumaDiasDate(fecFinAnt,1);
              fecFinAnt=Formatear.sumaDiasDate(fecFinAnt,7);
               if (Formatear.comparaFechas(fecFinAnt, fecFinCalAnt)>0)
                fecFinAnt=fecFinCalAnt;
            }
            else
            {
                v.add("");v.add("");v.add("");v.add("");
            }
            jtSem.addLinea(v);
            fecIni=Formatear.sumaDiasDate(fecFin,1);
            fecFin=Formatear.sumaDiasDate(fecFin,7);
            if (Formatear.comparaFechas(fecFin, fecFinCal)>0)
                fecFin=fecFinCal;
        }
        if (fecIniAnt!= null && Formatear.comparaFechas(fecIniAnt, fecFinCalAnt)<0)
        {
            ArrayList v=new ArrayList();
            if (Formatear.comparaFechas(fecIni, fecFinCal)<0)
            {
                addSemana(v,fecIni,fecFinCal,true);
            }
            else
            {
                v.add("");v.add("");v.add("");v.add("");
            }
           
            if (Formatear.comparaFechas(fecIniAnt, fecFinCalAnt)<0)
            {
              addSemana(v,fecIniAnt,fecFinCalAnt,false);
            }
            else
            {
               v.add("");v.add("");v.add("");v.add("");
            }
            jtSem.addLinea(v);
        } 
//        {
//            ArrayList v=new ArrayList();
//            addSemana(v,fecIni,fecFinCal,true);
//            if (fecIniAnt!=null)
//            {
//              addSemana(v,fecIniAnt,fecFinCalAnt,false);
//            }
//            else
//            {
//                v.add("");v.add("");v.add("");v.add("");
//            }
//            jtSem.addLinea(v);
//        }
        jtSem.requestFocusInicio();
    } catch (SQLException | ParseException k)
    {
        Error("Error al cargar semanas",k);
    }
  }
  void addSemana(ArrayList v,Date fecIni,Date fecFin,boolean swActual) throws SQLException
  {
        if (Formatear.comparaFechas(fecIni, fecFin)>0)
        {
             v.add("");v.add("");v.add("");v.add("");
             return;
        }
        boolean swTodo=opTodosProd.isSelected();
        s="select sum("+(swTodo?"hve_kilven":"hve_kiveav")+") as hve_kilven,"+
            " sum("+(swTodo?"hve_impven":"hve_imveav")+") as hve_impven "
                + " from histventas where hve_fecini >= to_date(' "+Formatear.getFecha(fecIni, "dd-MM-yyyy") +"','dd-MM-yyyy') "
                + " and hve_fecfin <= to_date(' "+Formatear.getFecha(fecFin, "dd-MM-yyyy") +"','dd-MM-yyyy') ";
         dtStat.select(s);

         v.add(Formatear.getFecha(fecIni, "dd-MM-yyyy"));
         v.add(Formatear.getFecha(fecFin, "dd-MM-yyyy"));
         v.add(dtStat.getDouble("hve_kilven",true));
         v.add(dtStat.getDouble("hve_impven",true));
         if (!swActual)
            return;
         if (swTodo)
         {
            if (actual || dtStat.getDouble("hve_kilven",true)==0 
                    || Formatear.comparaFechas(Formatear.getDateAct(),fecIni )<45 )
            {
                actualDatos(fecIni,   fecFin);
            }
         }
         else
         {
            if (actual || dtStat.getDouble("hve_kilven",true)==0 
                    || Formatear.comparaFechas(Formatear.getDateAct(),fecIni )<45 )
            {
                actualDatosPV(fecIni,   fecFin);
            }
         }
  }
  /**
   * Actualiza Acumulados solo de productos  vendibles.
   * @param fecIni
   * @param fecFin
   * @throws SQLException 
   */
  void actualDatosPV(Date fecIni,Date fecFin) throws SQLException
  {
     s= "select sum(avl_canti) as avc_kilos," +
               " sum(avl_canti* avl_prbase) as avc_basimp " +
             " from v_albventa as c, v_articulo as a " +
             " where c.avc_serie >='A' and c.avc_serie <='C' "+
             " and c.pro_codi = a.pro_codi "+
             " and a.pro_tiplot = 'V' "+
             " and c.avc_fecalb >= TO_DATE('" + Formatear.getFecha(fecIni, "dd-MM-yyyy") +  "','dd-MM-yyyy') " +
             " and c.avc_fecalb <= TO_DATE('" + Formatear.getFecha(fecFin, "dd-MM-yyyy") +   "','dd-MM-yyyy') " ;
        actualDatos1(s,fecIni,fecFin,false);
             
  }
  void actualDatos(Date fecIni,Date fecFin) throws SQLException
  {
    s="select sum(avc_kilos) as avc_kilos," +
               " sum(avc_basimp) as avc_basimp  " +
             " from v_albavec  c  " +
             " where c.avc_serie >='A' and c.avc_serie <='C' "+
             " and c.avc_fecalb >= TO_DATE('" + Formatear.getFecha(fecIni, "dd-MM-yyyy") +  "','dd-MM-yyyy') " +
             " and c.avc_fecalb <= TO_DATE('" + Formatear.getFecha(fecFin, "dd-MM-yyyy") +   "','dd-MM-yyyy') " ;
    actualDatos1(s,fecIni,fecFin,true);
  }
  /**
   * Actualiza datos en la tabla histventas
   * @param s
   * @param fecIni
   * @param fecFin
   * @param swTodosProd
   * @throws SQLException 
   */
  void actualDatos1(String s,Date fecIni,Date fecFin,boolean swTodosProd) throws SQLException
  {
    dtStat.select(s);
    s="select * from histventas WHERE hve_fecini = TO_DATE('" + Formatear.getFecha(fecIni, "dd-MM-yyyy") +  "','dd-MM-yyyy') "+
      " and hve_fecfin <= TO_DATE('" + Formatear.getFecha(fecFin, "dd-MM-yyyy") +   "','dd-MM-yyyy') ";
    if (dtAdd.select(s,true))
        dtAdd.edit();
    else
    {
        dtAdd.addNew("histventas");
        dtAdd.setDato("hve_fecini",fecIni);
        dtAdd.setDato("hve_fecfin",fecFin);
    }
    if (swTodosProd)
    {
        dtAdd.setDato("hve_kilven",dtStat.getDouble("avc_kilos",true));
        dtAdd.setDato("hve_impven",dtStat.getDouble("avc_basimp",true));
    }
    else
    {
        dtAdd.setDato("hve_kiveav",dtStat.getDouble("avc_kilos",true));
        dtAdd.setDato("hve_imveav",dtStat.getDouble("avc_basimp",true)); 
    }
    dtAdd.update();
    dtAdd.commit();
  }
  void consultar()
  {
    s="SELECT * FROM calendario where cal_ano="+ anoE.getValorInt()+
            (mesE.getValorInt()>0?" and cal_mes= "+mesE.getValorInt():"")+
            " order by cal_mes";
    try
    {
        if (!dtCon1.select(s))
        {
            msgBox("No encontrado calendario para este Año/Mes");
            return;
        }

        new miThread("")
        {
            @Override
            public void run()
            {
                consulta1();
            }
        };
      } catch (SQLException ex)
      {
        Error ("Error al buscar calendarios",ex);
      }
    }

    private void consulta1()
    {
        try{
        msgEspere("Buscando datos del mes");
        jtMes.setEnabled(false);
        double kilVen,impGanan;
        ArrayList <ArrayList> datos=new ArrayList();
        do
        {
            ArrayList v=new ArrayList();
            v.add(dtCon1.getInt("cal_mes"));
            addMes(v,dtCon1.getSQLDate("cal_fecini"),dtCon1.getSQLDate("cal_fecfin"));
            kilVen=dtStat.getDouble("hve_kilven",true);
            impGanan=dtStat.getDouble("hve_impgan",true);
            s="SELECT * FROM calendario where cal_ano="+ (anoE.getValorInt()-1)+
                " and cal_mes= "+dtCon1.getInt("cal_mes");
            if (dtStat.select(s))
            {
                addMes(v,dtStat.getSQLDate("cal_fecini"),dtStat.getSQLDate("cal_fecfin"));
                v.add(kilVen-dtStat.getDouble("hve_kilven",true));
                v.add(impGanan-dtStat.getDouble("hve_impgan",true));
            }
            else
            {
                v.add(""); v.add(""); v.add(""); v.add("");v.add("");v.add("");
            }
            datos.add(v);
        } while (dtCon1.next());
        jtMes.setDatos(datos);
        resetMsgEspere();
        jtMes.setEnabled(true);
        jtMes.requestFocusInicio();
        cargaSemanas(jtMes.getValorInt(0));
        actualAcum(jtMes.getRowCount());
        mensajeErr("Carga de datos realizada");
        } catch (Exception k)
        {
            Error("Error al buscar datos mensuales",k);
        }
  }
  void actualAcum(int row)
  {
      double kilos=0,kilosAnt=0,importe=0,importeAnt=0,ganancia=0,gananciaAnt=0;
      
      for (int n=0;n<=row;n++)
      {
          kilos+=jtMes.getValorDec(n,1);
          importe+=jtMes.getValorDec(n,2);
          kilosAnt+=jtMes.getValorDec(n,5);
          importeAnt+=jtMes.getValorDec(n,6);
          ganancia+=jtMes.getValorDec(n,3);
          gananciaAnt+=jtMes.getValorDec(n,7);
      }
      kilosE.setValorDec(kilos);
      importeE.setValorDec(importe);
      gananciaE.setValorDec(ganancia);
      kilosAntE.setValorDec(kilosAnt);
      impAntE.setValorDec(importeAnt);
      gananciaAntE.setValorDec(gananciaAnt);
      kilosDifE.setValorDec(kilos-kilosAnt);
      importeDifE.setValorDec(importe-importeAnt);
      gananciaDifE.setValorDec(ganancia-gananciaAnt);
  }
  
  void addMes(ArrayList v, String fecini, String fecfin) throws SQLException
  {
   boolean swTodo=opTodosProd.isSelected();
   s="select sum("+(swTodo?"hve_kilven":"hve_kiveav")+") as hve_kilven,"+
       " sum("+(swTodo?"hve_impven":"hve_imveav")+") as hve_impven,"
                + " sum(hve_impgan) as hve_impgan "
                + " from histventas where hve_fecini >= "+fecini+
                " and hve_fecfin <= "+fecfin;
        dtStat.select(s);

        v.add(dtStat.getDouble("hve_kilven",true));
        v.add(dtStat.getDouble("hve_impven",true));
        v.add(dtStat.getDouble("hve_impgan",true));
        v.add(dtStat.getDouble("hve_impgan",true)==0?0:
              dtStat.getDouble("hve_impgan")/dtStat.getDouble("hve_kilven",true) );
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

        Pprinc = new gnu.chu.controles.CPanel();
        Pcabe = new gnu.chu.controles.CPanel();
        anoL = new gnu.chu.controles.CLabel();
        anoE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        mesL = new gnu.chu.controles.CLabel();
        mesE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#9");
        Baceptar = new gnu.chu.controles.CButtonMenu();
        opTodosProd = new gnu.chu.controles.CCheckBox();
        jtMes = new gnu.chu.controles.Cgrid(11);
        jtSem = new gnu.chu.controles.Cgrid(8);
        Pacum = new gnu.chu.controles.CPanel();
        kilosL = new gnu.chu.controles.CLabel();
        kilosE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----,--9");
        kilosAntE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----,--9");
        importeE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---,---,--9");
        kilosL2 = new gnu.chu.controles.CLabel();
        kilosL4 = new gnu.chu.controles.CLabel();
        kilosL5 = new gnu.chu.controles.CLabel();
        impAntE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---,---,--9");
        kilosL6 = new gnu.chu.controles.CLabel();
        kilosDifE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----,--9");
        kilosL7 = new gnu.chu.controles.CLabel();
        importeDifE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----,--9");
        kilosL8 = new gnu.chu.controles.CLabel();
        gananciaE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----,--9");
        kilosL9 = new gnu.chu.controles.CLabel();
        gananciaAntE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----,--9");
        kilosL10 = new gnu.chu.controles.CLabel();
        gananciaDifE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----,--9");

        Pprinc.setLayout(new java.awt.GridBagLayout());

        Pcabe.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Pcabe.setMaximumSize(new java.awt.Dimension(305, 26));
        Pcabe.setMinimumSize(new java.awt.Dimension(305, 26));
        Pcabe.setPreferredSize(new java.awt.Dimension(305, 26));
        Pcabe.setLayout(null);

        anoL.setText("Año");
        Pcabe.add(anoL);
        anoL.setBounds(0, 2, 32, 18);
        Pcabe.add(anoE);
        anoE.setBounds(30, 2, 39, 18);

        mesL.setText("Mes");
        Pcabe.add(mesL);
        mesL.setBounds(80, 2, 32, 18);
        Pcabe.add(mesE);
        mesE.setBounds(110, 2, 23, 18);

        Baceptar.setText("Aceptar");
        Pcabe.add(Baceptar);
        Baceptar.setBounds(230, 2, 120, 23);

        opTodosProd.setSelected(true);
        opTodosProd.setText("Todos Prod.");
        opTodosProd.setToolTipText("Incluir Todos productos,no solo los vendibles.");
        Pcabe.add(opTodosProd);
        opTodosProd.setBounds(140, 2, 85, 18);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 55;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        Pprinc.add(Pcabe, gridBagConstraints);

        jtMes.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        {ArrayList v=new ArrayList();
            v.add("Mes"); // 0
            v.add("Kilos"); // 1
            v.add("Importe");  // 2
            v.add("Gananc"); // 3
            v.add("Gan.Kg"); // 4
            v.add("Kil.Ant."); // 5
            v.add("Imp.Ant");  // 6
            v.add("Gan.Ant"); // 7
            v.add("Gan.Kg.A"); // 8
            v.add("Dif.Kil"); //9
            v.add("Dif.Gan");//10
            jtMes.setCabecera(v);
            jtMes.setAlinearColumna(new int[]{2,2,2,2,2,2,2,2,2,2,2});
            jtMes.setAnchoColumna(new int[]{40,80,80,80,50,80,80,80,50,80,80});
            jtMes.setFormatoColumna(1,"---,--9.99");
            jtMes.setFormatoColumna(2,"----,--9.99");
            jtMes.setFormatoColumna(3,"---,--9.99");
            jtMes.setFormatoColumna(4,"--9.999");
            jtMes.setFormatoColumna(5,"---,--9.99");
            jtMes.setFormatoColumna(6,"----,--9.99");
            jtMes.setFormatoColumna(7,"---,--9.99");
            jtMes.setFormatoColumna(8,"--9.999");
            jtMes.setFormatoColumna(9,"----,--9.99");
            jtMes.setFormatoColumna(10,"---,--9.99");
            jtMes.setAjustarGrid(true);
        }

        javax.swing.GroupLayout jtMesLayout = new javax.swing.GroupLayout(jtMes);
        jtMes.setLayout(jtMesLayout);
        jtMesLayout.setHorizontalGroup(
            jtMesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 616, Short.MAX_VALUE)
        );
        jtMesLayout.setVerticalGroup(
            jtMesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 449;
        gridBagConstraints.ipady = 169;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        Pprinc.add(jtMes, gridBagConstraints);

        jtSem.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        {ArrayList v=new ArrayList();
            v.add("De"); // 0
            v.add("A"); // 1
            v.add("Kilos"); // 2
            v.add("Importe"); // 3
            v.add("Del"); // 4
            v.add("Al"); // 5
            v.add("Kil.Ant"); // 6
            v.add("Imp.Ant"); //7
            jtSem.setCabecera(v);
            jtSem.setAlinearColumna(new int[]{1,1,2,2,1,1,2,2});
            jtSem.setAnchoColumna(new int[]{80,80,90,90,80,80,90,90});
            jtSem.setFormatoColumna(0,"dd-MM-yyyy");
            jtSem.setFormatoColumna(1,"dd-MM-yyyy");
            jtSem.setFormatoColumna(2,"---,--9.99");
            jtSem.setFormatoColumna(3,"---,--9.99");
            jtSem.setFormatoColumna(4,"dd-MM-yyyy");
            jtSem.setFormatoColumna(5,"dd-MM-yyyy");
            jtSem.setFormatoColumna(6,"---,--9.99");
            jtSem.setFormatoColumna(7,"---,--9.99");
            jtSem.setAjustarGrid(true);
        }

        javax.swing.GroupLayout jtSemLayout = new javax.swing.GroupLayout(jtSem);
        jtSem.setLayout(jtSemLayout);
        jtSemLayout.setHorizontalGroup(
            jtSemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 616, Short.MAX_VALUE)
        );
        jtSemLayout.setVerticalGroup(
            jtSemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 140, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 449;
        gridBagConstraints.ipady = 89;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        Pprinc.add(jtSem, gridBagConstraints);

        Pacum.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Pacum.setMaximumSize(new java.awt.Dimension(610, 40));
        Pacum.setMinimumSize(new java.awt.Dimension(610, 40));
        Pacum.setName(""); // NOI18N
        Pacum.setPreferredSize(new java.awt.Dimension(610, 40));
        Pacum.setLayout(null);

        kilosL.setBackground(new java.awt.Color(255, 200, 0));
        kilosL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        kilosL.setText("Kil.Ant");
        kilosL.setOpaque(true);
        Pacum.add(kilosL);
        kilosL.setBounds(210, 0, 60, 15);

        kilosE.setEditable(false);
        Pacum.add(kilosE);
        kilosE.setBounds(10, 20, 60, 17);

        kilosAntE.setEditable(false);
        Pacum.add(kilosAntE);
        kilosAntE.setBounds(210, 20, 60, 17);

        importeE.setEditable(false);
        Pacum.add(importeE);
        importeE.setBounds(75, 20, 65, 17);

        kilosL2.setBackground(new java.awt.Color(255, 200, 0));
        kilosL2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        kilosL2.setText("Importe");
        kilosL2.setOpaque(true);
        Pacum.add(kilosL2);
        kilosL2.setBounds(75, 0, 65, 15);

        kilosL4.setBackground(new java.awt.Color(255, 200, 0));
        kilosL4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        kilosL4.setText("Kilos");
        kilosL4.setOpaque(true);
        Pacum.add(kilosL4);
        kilosL4.setBounds(10, 0, 60, 15);

        kilosL5.setBackground(new java.awt.Color(255, 200, 0));
        kilosL5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        kilosL5.setText("Imp.Ant");
        kilosL5.setOpaque(true);
        Pacum.add(kilosL5);
        kilosL5.setBounds(272, 0, 65, 15);

        impAntE.setEditable(false);
        Pacum.add(impAntE);
        impAntE.setBounds(272, 20, 65, 17);

        kilosL6.setBackground(new java.awt.Color(255, 200, 0));
        kilosL6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        kilosL6.setText("Dif. Kilos");
        kilosL6.setOpaque(true);
        Pacum.add(kilosL6);
        kilosL6.setBounds(410, 0, 60, 15);

        kilosDifE.setEditable(false);
        Pacum.add(kilosDifE);
        kilosDifE.setBounds(410, 20, 60, 17);

        kilosL7.setBackground(new java.awt.Color(255, 200, 0));
        kilosL7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        kilosL7.setText("Dif. Imp.");
        kilosL7.setOpaque(true);
        Pacum.add(kilosL7);
        kilosL7.setBounds(473, 0, 65, 15);

        importeDifE.setEditable(false);
        Pacum.add(importeDifE);
        importeDifE.setBounds(473, 20, 65, 17);

        kilosL8.setBackground(new java.awt.Color(255, 200, 0));
        kilosL8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        kilosL8.setText("Ganancia");
        kilosL8.setOpaque(true);
        Pacum.add(kilosL8);
        kilosL8.setBounds(142, 0, 60, 15);

        gananciaE.setEditable(false);
        Pacum.add(gananciaE);
        gananciaE.setBounds(142, 20, 60, 17);

        kilosL9.setBackground(new java.awt.Color(255, 200, 0));
        kilosL9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        kilosL9.setText("Gana Ant");
        kilosL9.setOpaque(true);
        Pacum.add(kilosL9);
        kilosL9.setBounds(340, 0, 60, 15);

        gananciaAntE.setEditable(false);
        Pacum.add(gananciaAntE);
        gananciaAntE.setBounds(340, 20, 60, 17);

        kilosL10.setBackground(new java.awt.Color(255, 200, 0));
        kilosL10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        kilosL10.setText("Dif.Gana");
        kilosL10.setOpaque(true);
        Pacum.add(kilosL10);
        kilosL10.setBounds(540, 0, 60, 15);

        gananciaDifE.setEditable(false);
        Pacum.add(gananciaDifE);
        gananciaDifE.setBounds(540, 20, 60, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        Pprinc.add(Pacum, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButtonMenu Baceptar;
    private gnu.chu.controles.CPanel Pacum;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CTextField anoE;
    private gnu.chu.controles.CLabel anoL;
    private gnu.chu.controles.CTextField gananciaAntE;
    private gnu.chu.controles.CTextField gananciaDifE;
    private gnu.chu.controles.CTextField gananciaE;
    private gnu.chu.controles.CTextField impAntE;
    private gnu.chu.controles.CTextField importeDifE;
    private gnu.chu.controles.CTextField importeE;
    private gnu.chu.controles.Cgrid jtMes;
    private gnu.chu.controles.Cgrid jtSem;
    private gnu.chu.controles.CTextField kilosAntE;
    private gnu.chu.controles.CTextField kilosDifE;
    private gnu.chu.controles.CTextField kilosE;
    private gnu.chu.controles.CLabel kilosL;
    private gnu.chu.controles.CLabel kilosL10;
    private gnu.chu.controles.CLabel kilosL2;
    private gnu.chu.controles.CLabel kilosL4;
    private gnu.chu.controles.CLabel kilosL5;
    private gnu.chu.controles.CLabel kilosL6;
    private gnu.chu.controles.CLabel kilosL7;
    private gnu.chu.controles.CLabel kilosL8;
    private gnu.chu.controles.CLabel kilosL9;
    private gnu.chu.controles.CTextField mesE;
    private gnu.chu.controles.CLabel mesL;
    private gnu.chu.controles.CCheckBox opTodosProd;
    // End of variables declaration//GEN-END:variables

}
