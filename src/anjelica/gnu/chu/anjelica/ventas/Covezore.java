package gnu.chu.anjelica.ventas;

/**
 * <p>Titulo: Consulta de Ventas por Representantes y Zonas</p>
 * <p>Descripcion: Permite Consultar las Ventas por Representantes y/o Zonas y desglosar en Clientes.
 * Tambien muestra un resumen por Familias de Productos.</p>
 * <p>Puede recibir como parametros:
 * repr Representante
 * sbeCodi Seccion Empresa</p>
* <p>Copyright: Copyright (c) 2005-2015
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
 * <p>Empresa: micasa</p>
 * @author chuchiP
 * @version 1.0
 */


import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.almacen.ActualStkPart;
import gnu.chu.anjelica.almacen.MvtosAlma;
import gnu.chu.anjelica.pad.MantRepres;
import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.camposdb.empPanel;
import gnu.chu.controles.StatusBar;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.DatosProd;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import gnu.chu.utilidades.miThread;
import gnu.chu.utilidades.ventana;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class Covezore extends ventana {
    DatosTabla dtMarg;
    private boolean isBuscandoMarg=false;
    double impGanaP=0;
    HashMap<String, Double> htGana=new HashMap();
    HashMap<String, Double> htCliGana=new HashMap();
    private final int JT_SBECOD=10;
    private final int JT_IMPGAN=7;
    private final int JT_PORGAN=8;
    private final int JT_KILVEN=6;
    boolean cancelarConsulta=false;
    private MvtosAlma mvtosAlm = new MvtosAlma();
    private boolean swDivCodi;
    private boolean inTransation=false;
    int cabIni=0;
    int cabFin=0;
    final static int JTCOM_SECCION=9;
    String s;
    ArrayList<ArrayList> datos;
    boolean swBuscaFam=false;
    int nAlbZ,nCliZ,nClAcZ;
    String REPRARG;
    String SBECODIARG;
    boolean VERMARGEN=false;
    AlbVenZR alVeZo=null;
    AlbClienComp alVenComp=null;
    AlbVenPro alVePr=null;
    double kgVen = 0;
    double impVen = 0;
    double prVen = 0;
    int nAlbL = 0, nAlbS=0,nCliL = 0,nCliS=0, nAlbT = 0, nCliT = 0,
            nClAcL = 0, nClAcS = 0,nClAcT = 0;

    double kgVenL = 0, impVenL = 0,kgVenS = 0, impVenS = 0;
    double kgVenT = 0, impVenT = 0;
    Hashtable<Integer,DatosProd> htArt=new Hashtable<Integer,DatosProd>();

  public Covezore(EntornoUsuario eu, Principal p)
  {
    this(eu, p, null);
  }

  public Covezore(EntornoUsuario eu, Principal p, Hashtable<String,String> ht)
  {

   EU=eu;
   vl=p.panel1;
   jf=p;
   eje=true;

   setTitulo("Consulta Ventas por Repr/Zonas");

   try
   {
      ponParametros(ht);

     if(jf.gestor.apuntar(this))
         jbInit();
      else
        setErrorInit(true);
   }
   catch (Exception e) {
     Logger.getLogger(Covezore.class.getName()).log(Level.SEVERE, null,e);
     setErrorInit(true);
   }
 }

 public Covezore(gnu.chu.anjelica.menu p,EntornoUsuario eu,Hashtable<String,String> ht) {

   EU=eu;
   vl=p.getLayeredPane();
   setTitulo("Consulta Ventas por Repr/Zonas");
   eje=false;

   try  {
     ponParametros(ht);
     jbInit();
   }
   catch (Exception e) {
     Logger.getLogger(Covezore.class.getName()).log(Level.SEVERE, null,e);
     setErrorInit(true);
   }
 }
private void ponParametros(Hashtable<String,String> ht)
{
     if (ht != null)
     {
       if (ht.get("repr") != null)
         REPRARG = ht.get("repr");
       if (ht.get("sbeCodi") != null)
         SBECODIARG = ht.get("sbeCodi");
       if (ht.get("verMargen") != null)
         VERMARGEN = Boolean.parseBoolean( ht.get("verMargen"));
     }
}
private void jbInit() throws Exception
{
   iniciarFrame();

   this.setVersion("2016-02-18");
   statusBar = new StatusBar(this);
 
   initComponents();
   this.setSize(620,542);
   this.getContentPane().add(statusBar, BorderLayout.SOUTH);

   conecta();
}

@Override
public void iniciarVentana() throws Exception
{
    dtMarg=new DatosTabla(ct);
    Pbuscando.setVisible(false);
    opDivisa.setEnabled(EU.isRootAV());
    opDivisa.setSelected(EU.isRootAV());
    opDivisa.setToolTipText(EU.isRootAV()?"Selecionar para incluir TODAS las divisas":"");
    Pcabe.setDefButton(Baceptar.getBotonAccion());
    PcabCom.setDefButton(BaceptarCom);
    bdiscr.iniciar(dtStat, this, vl, EU);
    jt.tableView.setToolTipText("Doble click encima linea para detalles venta");
    sbe_codiE.setAceptaNulo(true);
    emp_codiE.iniciar(dtStat, this, vl, EU);
    emp_codiE.setAceptaNulo(empPanel.hasAccesoTotal(dtStat, EU.usuario));
    sbe_codiE.iniciar(dtStat, this, vl, EU);
    sbe_codiE.setAceptaNulo(true);
    sbe_codiE.setFieldEmpCodi(emp_codiE.getTextField());
    
    mvtosAlm.setResetCostoStkNeg(true);
    mvtosAlm.setFechasDocumento(true);
    MantRepres.llenaLinkBox(rep_codiE, dtCon1);
    fecIniE.iniciar(dtStat,this,vl,EU);
    fecFinE.iniciar(dtStat, this, vl, EU);
//    fecIniE.setDesplazaX(150);
//    fecFinE.setDesplazaX(150);
    fecIniComE.iniciar(dtStat, this, vl, EU);
    fecFinComE.iniciar(dtStat, this, vl, EU);
//    fecIniComE.setDesplazaY(110);
//    fecFinComE.setDesplazaY(110);
//    fecIniComE.setDesplazaX(185);
//    fecFinComE.setDesplazaX(185);
//    pdconfig.llenaDiscr(dtStat, rep_codiE, "Cr",EU.em_cod);
     pdconfig.llenaDiscr(dtStat, zon_codiE, pdconfig.D_ZONA ,EU.em_cod);
     activarEventos();
//     cli_codiE.setText("");
     this.setEnabled(true);
     fecIniE.requestFocus();

     fecFinE.setText(Formatear.getFechaAct("dd-MM-yyyy"));
     GregorianCalendar gc = new GregorianCalendar();
     gc.setTime(fecFinE.getDate());

     for (int n=0;n<7;n++)
     {
        if (gc.get(GregorianCalendar.DAY_OF_WEEK)==GregorianCalendar.MONDAY)
            break;
        gc.setTime(Formatear.sumaDiasDate(gc.getTime(),-1));
     }
     fecIniE.setDate(gc.getTime());
     emp_codiE.setValorInt(0);
     sbe_codiE.setValorInt(0);
     arbolProdPanel.iniciar(dtCon1);
     AnoComE.setValorInt(EU.ejercicio-1);
//     REPRARG="MA";
     if (REPRARG!=null)
     {
         rep_codiE.setText(REPRARG);
         rep_codiE.setEnabled(false);
     }
     if (SBECODIARG!=null)
     {
         sbe_codiE.setText(SBECODIARG);
         sbe_codiE.setEnabled(false);
     }
   }

   void activarEventos()
   {
     BaceptarCom.addActionListener(new java.awt.event.ActionListener()
     {
            @Override
       public void actionPerformed(ActionEvent e)
       {
         BaceptarCom_actionPerformed();
       }
     });
     Baceptar.addActionListener(new java.awt.event.ActionListener()
     {
            @Override
       public void actionPerformed(ActionEvent e)
       {
         Baceptar_actionPerformed(e);
       }
     });
     fecIniComE.addFocusListener(new java.awt.event.FocusAdapter()
     {
            @Override
       public void focusLost(FocusEvent e)
       {
            try{
                ponerFechasComp();
            } catch (Exception ex)
            {
                Error("Error al poner fechas de comparacion", ex);
            }
         
       }
     });
     jtGru.addListSelectionListener(new  ListSelectionListener()
     {
            @Override
      public void valueChanged(ListSelectionEvent e)
      {
          if (e.getValueIsAdjusting()) // && e.getFirstIndex() == e.getLastIndex())
            return;
          try {
            llenaFamilias();
          }catch (Exception k)
          {
              Error("Error al llenar familias",k);
          }
      }
     });
//     Bcz.addActionListener(new ActionListener()
//     {
//       public void actionPerformed(ActionEvent e)
//       {
//         if (jt.isVacio())
//           return;
//         busVenZona();
//       }
//     });
     fecIniE.addFocusListener(new java.awt.event.FocusAdapter()
     {
            @Override
       public void focusLost(FocusEvent e)
       {
         feciniE_focusLost(e);
       }
     });

     jt.addMouseListener(new MouseAdapter()
     {
            @Override
       public void mouseClicked(MouseEvent e) {
         if (jt.isVacio())
           return;
         if (e.getClickCount()<2)
           return;
         busVenZona();
       }
     });
     jtCom.addMouseListener(new MouseAdapter()
     {
            @Override
       public void mouseClicked(MouseEvent e) {
         if (jtCom.isVacio())
           return;
         if (e.getClickCount()<2)
           return;
         busVenComp();
       }
     });
     jtFam.addMouseListener(new MouseAdapter()
     {
            @Override
       public void mouseClicked(MouseEvent e) {
         if (jtFam.isVacio())
           return;
         if (e.getClickCount()<2)
           return;
         busVenArticulos();
       }
     });
   }
   
   void buscaVentasCom()
   {
      buscaVentas(false);
      try {
        
        s=getStrSql(fecIniComE.getText(),fecFinComE.getText());
        datos=new ArrayList();
        if (dtCon1.select(s))
        {
            int sbeCodi=dtCon1.getInt("sbe_codi");
            String repr = dtCon1.getString("rep_codi");
            nAlbL = nCliL = nAlbT = nCliT =  nClAcL = nClAcT = 0;
            kgVenL =  impVenL = 0;
            kgVenT =  impVenT = 0;
            do {
                 if (!repr.equals(dtCon1.getString("rep_codi"))) {
                     ponDatosRepr(repr,sbeCodi);
                     repr = dtCon1.getString("rep_codi");
                 }
                 if (sbeCodi!=dtCon1.getInt("sbe_codi"))
                 {
                     ponDatosSbe(sbeCodi);
                     sbeCodi=dtCon1.getInt("sbe_codi");
                 }

                 s = "SELECT COUNT(*) as cuantos FROM clientes WHERE "
                         + " zon_codi='" + dtCon1.getString("zon_codi") + "'"
                         + " and rep_codi = '" + dtCon1.getString("rep_codi") + "'"
                         + " and cli_activ ='S'";
                 dtStat.select(s);
                 nClAcZ = dtStat.getInt("cuantos");

                 ArrayList v = new ArrayList();
                 v.add(dtCon1.getString("rep_codi"));
                 v.add(dtCon1.getString("zon_codi"));
                 v.add(MantRepres.getNombRepr(repr,dtStat)
                         + " - " + pdconfig.getTextDiscrim(dtStat, "Cz", EU.em_cod, dtCon1.getString("zon_codi")));
                 v.add(dtCon1.getInt("numalb"));
                 v.add(dtCon1.getInt("cliCodiD"));
                 v.add(dtCon1.getDouble("avc_basimp"));
                 v.add(dtCon1.getDouble("avc_kilos"));
                 v.add(nClAcZ);
                 v.add(""+sbeCodi);
                 datos.add(v);
                 nAlbL += dtCon1.getInt("numalb");
                 nCliL += dtCon1.getInt("cliCodiD");
                 impVenL += dtCon1.getDouble("avc_basimp");
                 kgVenL += dtCon1.getDouble("avc_kilos");
                 nClAcL += nClAcZ;
             } while (dtCon1.next());

             ponDatosRepr(repr,sbeCodi);
             ponDatosSbe(sbeCodi);
       }
      
       int nlOri=0;
       int nlFin=0;
       
       ArrayList<ArrayList> datCom=new ArrayList();
       int maxLin=datos.size()>jt.getRowCount()?datos.size():jt.getRowCount();
       for (int n=0;n<maxLin;n++)
       {
           ArrayList v=new ArrayList();
           if (nlOri<jt.getRowCount())
           { // Le quedan linas al origen
               if (nlFin<datos.size())
               { // Todavia le quedan lineas al Final
                   if (jt.getValString(nlOri,0).equals(datos.get(nlFin).get(0).toString()) &&
                               jt.getValString(nlOri,1).equals(datos.get(nlFin).get(1).toString()) &&
                               jt.getValString(nlOri,8).equals(datos.get(nlFin).get(8).toString()))
                   {
                       datCom.add(addDatos(datos,nlOri++,nlFin++));
                       continue;
                   }
                   else
                   {
                       int nlEnc=-1;
                       for (int n1=nlFin;n1<datos.size();n1++)
                       {
                             if (jt.getValString(nlOri,0).equals(datos.get(n1).get(0).toString()) &&
                                   jt.getValString(nlOri,1).equals(datos.get(n1).get(1).toString()) &&
                                   jt.getValString(nlOri,8).equals(datos.get(n1).get(8).toString()))
                             {
                                nlEnc=n1;
                                break;
                             }
                       }
                       if (nlEnc>=0)
                       {
                         for (;nlFin<nlEnc;nlFin++)
                         {
                               datCom.add(addDatos1(datos,-1,nlFin));
                         }
                         continue;
                       }
                   }
               }
               if (nlOri<jt.getRowViewCount())
               { // Todavia le quedan lineas al Origen
                 datCom.add(addDatos(datos,nlOri++,-1));
               }
           }
           else
           { // Solo trato las lineas del final.
              if (nlFin<datos.size())
                 datCom.add(addDatos1(datos,-1,nlFin++));
           }
          // datCom.add(addDatos1(datos,-1,nlFin));
       }
       // Añade linea Final.
      
       numAlbOriE.setValorInt(numAlbE.getValorInt());
       numCliOriE.setValorInt(numCliE.getValorInt());
       impAlbOriE.setValorDec( impAlbE.getValorDec());
       kilAlbOriE.setValorDec(kilAlbE.getValorDec());

       numAlbFinE.setValorInt(nAlbT);
       numCliFinE.setValorInt(nCliT);
       impAlbFinE.setValorDec( impVenT);
       kilAlbFinE.setValorDec(kgVenT);

       numAlbDifE.setValorInt(numAlbE.getValorInt()-nAlbT);
       numCliDifE.setValorInt(numCliE.getValorInt()- nCliT);
       impAlbDifE.setValorDec(impAlbE.getValorDec()- impVenT);
       kilAlbDifE.setValorDec(kilAlbE.getValorDec()- kgVenT);
       jtCom.removeAllDatos();
       jtCom.setDatos(datCom);
       ArrayList v1=new ArrayList();
       v1.add(".");v1.add(".");v1.add("TOTAL GENERAL");
       v1.add(impAlbE.getValorDec());
       v1.add(impVenT);
       v1.add(impAlbE.getValorDec()- impVenT);
       v1.add(kilAlbE.getValorDec());
       v1.add(kgVenT);
       v1.add(kilAlbE.getValorDec()- kgVenT);
       v1.add(0);
       
       jtCom.addLinea(v1);
     }
     catch (Exception k)
     {
       Error("Error al Calcular Datos", k);
     }
   }

   boolean buscaDatos(ArrayList<ArrayList> datCom,int nlOri,int nlFin,int maxLin)
   {
       for (int n=nlFin;n<maxLin;n++)
       {
         if (jt.getValString(nlOri,0).equals(datCom.get(n).get(0)) &&
                           jt.getValString(nlOri,1).equals(datCom.get(n).get(1)) &&
                           jt.getValString(nlOri,8).equals(datCom.get(n).get(8)))
           return true;
       }
       return false;
   }
   /**
    * Añade datos del periodo incicial.
    * @param datCom
    * @param nlOri
    * @param nlFin
    * @return 
    */
   ArrayList addDatos(ArrayList<ArrayList> datCom,int nlOri,int nlFin)
   {
       double kilFin=0;
       double impFin=0;
       ArrayList v=new ArrayList();
       v.add(jt.getValString(nlOri,0)); //0
       v.add(jt.getValString(nlOri,1)); //1
       v.add(jt.getValString(nlOri,2)); // 2
       v.add(jt.getValString(nlOri,5)); // 3
       if (nlFin>=0)
       {
            kilFin=Double.parseDouble(datCom.get(nlFin).get(6).toString());
            impFin=Double.parseDouble(datCom.get(nlFin).get(5).toString());
       }
       v.add(impFin); // 4
       v.add(jt.getValorDec(nlOri,5)-impFin); // 5
       v.add(jt.getValString(nlOri,6)); // 6
       v.add(kilFin); // 7
       v.add(jt.getValorDec(nlOri,6)-kilFin); // 8
       v.add(jt.getValString(nlOri,8)); // 9
       return v;
   }
   /**
    *  Añade datos del periodo Inicial
    * @param datCom
    * @param nlOri
    * @param nlFin
    * @return Añad
    */
   ArrayList addDatos1(ArrayList<ArrayList> datCom,int nlOri,int nlFin)
   {
       double kilFin=0;
       double impFin=0;
       ArrayList v=new ArrayList();
       v.add(datCom.get(nlFin).get(0).toString());
       v.add(datCom.get(nlFin).get(1).toString());
       v.add(datCom.get(nlFin).get(2).toString());
       if (nlOri>=0)
       {
            kilFin=jt.getValorDec(nlOri,5);
            impFin=jt.getValorDec(nlOri,6);
       }
       v.add(impFin);
       v.add(datCom.get(nlFin).get(5).toString());
       v.add(impFin-Double.parseDouble(datCom.get(nlFin).get(5).toString()));
       v.add(kilFin);
       v.add(Double.parseDouble(datCom.get(nlFin).get(6).toString()));
       v.add(kilFin-Double.parseDouble(datCom.get(nlFin).get(6).toString()));
       v.add(datCom.get(nlFin).get(8).toString());
       return v;
   }
   void ponerFechasComp() throws SQLException,ParseException
   {
     if (fecIniComE.isNull() && ! AnoComE.isNull())
     {
                fecIniComE.setDate(calculaFechaComp(fecIniE.getDate(), AnoComE.getValorInt()));
                fecFinComE.setDate(Formatear.sumaDiasDate(fecIniComE.getDate(),
                            (int) Formatear.comparaFechas(fecFinE.getDate(),fecIniE.getDate())));
     }
   }
   void setCabeceraComparativo(int anoIni,int anoFin)
   {
        ArrayList vc = new ArrayList();
        vc.add("Repr"); //0
        vc.add("Zona"); //1
        vc.add("Descripcion"); // 2
        vc.add("Imp."+(anoIni==0?"Ini":""+anoIni)); // 3
        vc.add("Imp:"+(anoFin==0?"Fin":""+anoFin)); // 4
        vc.add("Dif.Imp");  // 5
        vc.add("Kil."+(anoIni==0?"Ini":""   +anoIni)); // 6
        vc.add("Kil:"+(anoFin==0?"Fin":""+anoFin)); // 7
        vc.add("Dif.Kil");  // 8
        vc.add("Sec"); // 9
        jtCom.setCabecera(vc);
        if (anoIni!=0)
        {
            labIniL.setText(anoIni==0?"Inicio":""+anoIni);
            labFinL.setText(anoFin==0?"Final":""+anoFin);
            labIni1L.setText(anoIni==0?"Inicio":""+anoIni);
            labFin1L.setText(anoFin==0?"Final":""+anoFin);
        }
        jtCom.setAnchoColumna(new int[]{25,25,180,51,52,52,52,52,52,20});
    }
   public void setTodosProd(boolean swTodosProd)
   {
       opIncComent.setSelected(swTodosProd);
   }
   public void setFechaInicio(Date fecha)
   {
       fecIniE.setDate(fecha);
   }
   public void setFechaFinal(Date fecha)
   {
       fecFinE.setDate(fecha);
   }
   public void setFechaInicioComp(Date fecha)
   {
       fecIniComE.setDate(fecha);
   }
   public void setFechaFinalComp(Date fecha)
   {
       fecFinComE.setDate(fecha);
   }
   public boolean inTransation()
   {
        return inTransation;
   }
   public static String getNombreClase()
   {
     return "gnu.chu.anjelica.ventas.Covezore";
   }
   public void lanzaConsulta()
   {
      TPanel1.setSelectedIndex(1);
      BaceptarCom_actionPerformed();
   } 
  
   void BaceptarCom_actionPerformed()
    {
       if (! checkCondic())
           return;
        try
        {
            ponerFechasComp();
        } catch (Exception ex)
        {
            Error("Error al poner fechas de comparacion", ex);
            return;
        }
       if( ! checkCondicCom())
           return;
        try
        {
            cabIni=Integer.parseInt(Formatear.getFecha(fecIniE.getDate(), "yyyy"));
            cabFin=Integer.parseInt(Formatear.getFecha(fecIniComE.getDate(), "yyyy"));
            if (cabIni==cabFin)
            {
                cabIni=0;
                cabFin=0;
            }
            setCabeceraComparativo(cabIni,cabFin);
            jtCom.cuadrarGrid();
        } catch (ParseException ex)
        {
            Logger.getLogger(Covezore.class.getName()).log(Level.SEVERE, null, ex);
        }
       new miThread("")
       {
            @Override
           public void run()
            {
              inTransation=true;
              msgEspere("Buscando datos...");
              swBuscaFam=false;
              buscaVentasCom();
              swBuscaFam=true;
              resetMsgEspere();
              jtCom.setEnabled(true);
              mensaje("");
              mensajeErr("Consulta realizada");
              inTransation=false;
           }
       };
   }
   boolean checkCondic()
   {
       if (fecIniE.isNull() || fecIniE.getError())
       {
           mensajeErr("Fecha inicial no valida");
           fecIniE.requestFocus();
           return false;
        }
        if (fecFinE.isNull() || fecFinE.getError())
       {
           mensajeErr("Fecha Final no valida");
           fecIniE.requestFocus();
           return false;
        }
       return true;
   }
   boolean checkCondicCom()
   {
       if (fecIniComE.isNull() || fecIniComE.getError())
       {
           mensajeErr("Fecha inicial no valida");
           fecIniComE.requestFocus();
           return false;
        }
        if (fecFinComE.getError())
        {
           mensajeErr("Fecha Final no valida");
           fecFinComE.requestFocus();
           return false;
        }
       if (fecFinComE.isNull())
       {
            try
            {
                fecFinComE.setDate(Formatear.sumaDiasDate(fecIniComE.getDate(),
                        (int) Formatear.comparaFechas(fecFinE.getDate(),fecIniE.getDate())));
            } catch (ParseException ex)
            {
                Error("Error al poner fecha Final de Comparativo",ex);
                return false;
            }
       }
       return true;
   }
    void Baceptar_actionPerformed(ActionEvent e)
   {
       if (! checkCondic())
            return;
       cancelarConsulta=true;
       
       while (isBuscandoMarg)
       {
           try
           {
               Thread.sleep(50);
           } catch (InterruptedException ex)
           {
               Error("Error al esperar margenes",ex);
           }
       }
       
       new miThread("")
       {
            @Override
           public void run()
            {
              inTransation=true;
              msgEspere("Buscando datos...");
              swBuscaFam=false;
              buscaVentas(true);
              swBuscaFam=true;
              resetMsgEspere();
              mensaje("");
              mensajeErr("Consulta realizada");
              inTransation=false;
              if (!VERMARGEN)
                  return;
              new miThread("")
              {
                  @Override
                   public void run()
                   {
                     Pbuscando.setVisible(true);
                     isBuscandoMarg=true;
                     buscaMargenes();
                     isBuscandoMarg=false;
                     Pbuscando.setVisible(false);
                   }
              };
           }
       };
   }
   void busVenZona()
  {
   try
   {
     if (jt.getValString(0).equals(".") || jt.getValString(1).equals("."))
         return;
     if (alVeZo == null)
     {
       alVeZo = new AlbVenZR();
       alVeZo.iniciar(this);
       vl.add(alVeZo);
     }
     alVeZo.setLocation(this.getLocation().x+100, this.getLocation().y+80);
     alVeZo.setSelected(true);
     this.setEnabled(false);
     this.setFoco(alVeZo);
     alVeZo.cargaDatos( jt.getValString(0),jt.getValString(1),jt.getValorInt(JT_SBECOD),
         isBuscandoMarg?null:htCliGana);

     alVeZo.setVisible(true);
   }
   catch (Exception ex)
   {
     fatalError("Error al Cargar datos de Zonas",ex);
   }
  }

  void busVenComp()
  {
   try
   {
//     if (jtCom.getValString(0).equals(".") || jtCom.getValString(1).equals("."))
//         return;
     if (alVenComp == null)
     {
       alVenComp = new AlbClienComp();
       alVenComp.iniciar(this);
       vl.add(alVenComp);
     }
     alVenComp.setLocation(this.getLocation().x+100, this.getLocation().y+80);
     alVenComp.setSelected(true);
     this.setEnabled(false);
     this.setFoco(alVenComp);
     alVenComp.cargaDatos( jtCom.getValString(0),jtCom.getValString(1),jtCom.getValorInt(JTCOM_SECCION));
    
     alVenComp.setVisible(true);
   }
   catch (Exception ex)
   {
     fatalError("Error al Cargar datos de Zonas",ex);
   }
  }
  /**
   * Sobre la fecha inicial mandada y teniendo en cuenta el calendario
   * de empresa, busca en el año puesto, la misma fecha
   * @param fecini
   * @param ano
   * @return Fecha en el año mandado. Null si no encuentra registros
   * @throws ParseException
   */
  Date calculaFechaComp(Date fecini,int ano) throws ParseException,SQLException
  {
    if (fecini==null)
        return null;
    s="SELECT * from calendario where cal_fecini <= to_date('"+
            Formatear.fechaDB(fecini)+"','yyyyMMdd')  "+
            " and cal_fecfin >= to_date('"+Formatear.fechaDB(fecini)+"','yyyyMMdd') ";
    if (! dtStat.select(s))
        return null;
    long despl=Formatear.comparaFechas(fecini, dtStat.getDate("cal_fecini"));
    s="SELECT * from calendario where cal_ano="+ano+
            " and cal_mes = "+dtStat.getInt("cal_mes");
    if (! dtStat.select(s))
        return null;
    return Formatear.sumaDiasDate(dtStat.getDate("cal_fecini") ,(int) despl);
//    GregorianCalendar gc=new GregorianCalendar();
//    gc.setTime(fecini);
//    int diaIni=gc.get(GregorianCalendar.DAY_OF_WEEK);
//    int semIni=gc.get(GregorianCalendar.WEEK_OF_YEAR);
//    int mesIni=gc.get(GregorianCalendar.MONTH);
//    String fecha=Formatear.getFecha(fecini, "dd-MM");
//    fecha+="-"+Formatear.format(ano, "9999");
//    fecini=Formatear.getDate(fecha,"dd-MM-yyyy");
//    gc.setTime(fecini);
//    int diaFin=gc.get(GregorianCalendar.DAY_OF_WEEK);
//    int semFin=gc.get(GregorianCalendar.WEEK_OF_MONTH);
//    int mesFin=gc.get(GregorianCalendar.MONTH);
//
//    gc.set(GregorianCalendar.WEEK_OF_YEAR,semIni );
//    gc.set(GregorianCalendar.DAY_OF_WEEK,diaIni);
//    fecini=gc.getTime();
//    return fecini;
  }
  void busVenArticulos()
  {
   try
   {
     if (jtFam.isVacio())
         return;
     if (alVePr == null)
     {
       alVePr = new AlbVenPro();
       alVePr.iniciar(this);
       vl.add(alVePr);
     }
     alVePr.setLocation(this.getLocation().x+100, this.getLocation().y+80);
     alVePr.setSelected(true);
     this.setEnabled(false);
     this.setFoco(alVePr);
     alVePr.cargaDatosPro( jtFam.getValorInt(0));
    
     alVePr.setVisible(true);
   }
   catch (Exception ex)
   {
     fatalError("Error al Cargar datos de productos",ex);
   }
  }
  public String getCondDiscri(String tabla)
  {
      return bdiscr.getCondWhere(tabla);
  }
    @Override
  public void matar(boolean cerrarConexion)
  {
    if (muerto)
      return;
    if (alVeZo != null)
    {
      alVeZo.setVisible(false);
      alVeZo.matar(false);
    }
    try
    {
        while (isBuscandoMarg)
        {
            Thread.sleep(50);
        }
        dtMarg.close();
    } catch (SQLException ex)
    {
        Logger.getLogger(Covezore.class.getName()).log(Level.SEVERE, null, ex);
    }   catch (InterruptedException ex)
        {
            Logger.getLogger(Covezore.class.getName()).log(Level.SEVERE, null, ex);
        }
    super.matar(cerrarConexion);
  }

   void feciniE_focusLost(FocusEvent e)
   {
     try
     {
       if (e.isTemporary())
         return;
       if (fecIniE.getText().equals("") || ! fecFinE.isNull())
         return;
       GregorianCalendar gc = new GregorianCalendar();
       gc.setTime(fecIniE.getDate());
       gc.add(GregorianCalendar.DAY_OF_MONTH, 6);
       fecFinE.setDate(gc.getTime());
     }
     catch (Exception k)
     {
       Logger.getLogger(Covezore.class.getName()).log(Level.SEVERE, null, k);
     }
   }
    void ponDatosRepr(String repr,int sbeCodi) throws SQLException {
        if (jt.getValorInt(jt.getRowCount() - 1, 3) != nAlbL) {
            ArrayList v = new ArrayList();
            v.add(repr);
            v.add(".");
            v.add(" Total " + MantRepres.getNombRepr(repr,dtStat));
            v.add(nAlbL);
            v.add(nCliL);
            v.add(impVenL);
            v.add(kgVenL);
            v.add("");
            v.add("");
            v.add(nClAcL);
            v.add(sbeCodi);
            datos.add(v);
        }
        nAlbS += nAlbL;
        nCliS += nCliL;
        impVenS += impVenL;
        kgVenS += kgVenL;
        nClAcS += nClAcL;
        nClAcL = nCliL = nAlbL = 0;
        kgVenL = impVenL = 0;
    }
   void ponDatosSbe(int sbeCodi) throws SQLException {

        ArrayList v = new ArrayList();
        v.add(".");
        v.add(".");
        v.add("  TOTAL DELEGACION: " + sbe_codiE.getNombSubEmpresa(dtStat,sbeCodi,0));
        v.add(nAlbS);
        v.add(nCliS);
        v.add(impVenS);
        v.add(kgVenS);
        v.add("");
        v.add("");
        v.add(nClAcS);
        v.add(sbeCodi);
        datos.add(v);
        nAlbT += nAlbS;
        nCliT += nCliS;
        impVenT += impVenS;
        kgVenT += kgVenS;
        nClAcT += nClAcS;
        nClAcS = nCliS = nAlbS = 0;
        kgVenS = impVenS = 0;
    }
    String getStrSql(String fecini,String fecfin)
    {
        swDivCodi=opDivisa.isSelected();
        if (opIncComent.isSelected())
          return "select sum(avc_kilos) as avc_kilos," +
               " sum(avc_basimp) as avc_basimp, count(*) as numalb,count (distinct c.cli_codi) as cliCodiD," +
               " cl.zon_codi,cl.rep_codi,c.sbe_codi " +
             " from v_albavec  c, clientes cl  " +
             " where c.avc_fecalb >= TO_DATE('" + fecini +  "','dd-MM-yyyy') " +
             " and c.avc_fecalb <= TO_DATE('" + fecfin +   "','dd-MM-yyyy') " +
             " and c.avc_serie >= 'A' AND c.avc_serie <='C' " +
             (swDivCodi?"":" and c.div_codi > 0 ")+
             (emp_codiE.getValorInt()==0?"":" and c.emp_codi = "+emp_codiE.getValorInt())+
             (sbe_codiE.getValorInt()==0?"":" and c.sbe_codi = "+sbe_codiE.getValorInt())+
             (zon_codiE.isNull()?"":" and cl.zon_codi = '"+zon_codiE.getText()+"'")+
             (rep_codiE.isNull()?"":" and cl.rep_codi = '"+rep_codiE.getText()+"'")+
             bdiscr.getCondWhere("cl")+
//             " and avc_kilos != 0 "+
             " and cl.cli_codi = c.cli_codi "+
             " group by c.sbe_codi,cl.rep_codi,cl.zon_codi "+
             " order by c.sbe_codi,cl.rep_codi,cl.zon_codi";
        return "select sum(avl_canti) as avc_kilos," +
               " sum(avl_canti* avl_prbase) as avc_basimp, count (distinct c.emp_codi + avc_ano +avc_nume || avc_serie) as numalb,"
             + "count (distinct c.cli_codi) as cliCodiD," +
               " cl.zon_codi,cl.rep_codi,c.sbe_codi " +
             " from v_albventa  c, clientes cl, v_articulo as ar " +
             " where c.avc_fecalb >= TO_DATE('" + fecini +  "','dd-MM-yyyy') " +
             " and c.avc_fecalb <= TO_DATE('" + fecfin +   "','dd-MM-yyyy') " +
             " and c.avc_serie >= 'A' AND c.avc_serie <='C' " +
             (swDivCodi?"":" and c.div_codi > 0 ")+
             (emp_codiE.getValorInt()==0?"":" and c.emp_codi = "+emp_codiE.getValorInt())+
             (sbe_codiE.getValorInt()==0?"":" and c.sbe_codi = "+sbe_codiE.getValorInt())+
             (zon_codiE.isNull()?"":" and cl.zon_codi = '"+zon_codiE.getText()+"'")+
             (rep_codiE.isNull()?"":" and cl.rep_codi = '"+rep_codiE.getText()+"'")+
            " and ar.pro_codi = c.pro_codi "+
            " and ar.pro_tiplot = 'V' "+
             bdiscr.getCondWhere("cl")+
             " and cl.cli_codi = c.cli_codi "+
             " group by c.sbe_codi,cl.rep_codi,cl.zon_codi "+
             " order by c.sbe_codi,cl.rep_codi,cl.zon_codi";
        
   }
    /**
     * Funcion que busca Margenes sobre los datos introducidos. Realiza la busqueda en background
     */
    void buscaMargenes()
    {       
        try {   
           
            cancelarConsulta=false;
            String fecIni=fecIniE.getText();
            String fecFin=fecFinE.getText();
            String fecInv=ActualStkPart.getFechaUltInv(0,0,fecIniE.getDate(),dtStat);
            Date fecInvD=Formatear.getDate(fecInv, "dd-MM-yyyy");
            String sql = "SELECT pro_codi,mvt_tipdoc as tipdoc, mvt_tipo as tipmov,  "
                + " mvt_time as fecmov,"
                + " mvt_canti as canti,mvt_prec as precio,"
                + " mvt_cliprv as cliCodi,mvt_empcod,mvt_ejedoc,mvt_serdoc,"
                + " mvt_numdoc, "
                + " mvt_fecdoc "
                + " from mvtosalm where "
                + "  mvt_canti <> 0 "
                + " and NOT (mvt_serdoc='X' and mvt_tipdoc ='V') " // ignorar traspaso entre almacenes 
                + " AND mvt_time::date > TO_DATE('" + fecInv + "','dd-MM-yyyy') "
                + " and mvt_time::date <= TO_DATE('" + fecFin + "','dd-MM-yyyy') "
                + " ORDER BY pro_codi,fecmov,tipmov";
            PreparedStatement psAlb=  dtStat.getPreparedStatement("select c.zon_codi,c.rep_codi,a.sbe_codi "
                + " from v_albavec as a, v_cliente  as c where c.cli_codi = a.cli_codi and a.emp_codi=  ? "
                + " and avc_ano=? "
                + " and avc_serie=? and avc_nume = ?");       
            PreparedStatement psCli=  dtStat.getPreparedStatement("select c.zon_codi,c.rep_codi,c.sbe_codi "
                + " from v v_cliente  as c where c.cli_codi = ?");

            ResultSet rsCli;
           if (!dtMarg.select(sql))
           {
               mensajeErr("No encontrados datos de margenes");             
               return;
           }
           sql=" select sum(rgs_kilos) as kilos, sum(rgs_kilos*rgs_prregu) as importe "
                + " FROM v_inventar  WHERE "
                + " rgs_fecha::date = TO_DATE('" + fecInv + "','dd-MM-yyyy') "
                + " and pro_codi = ?";
           PreparedStatement psInv= dtStat.getPreparedStatement(sql);
           ResultSet rsInv;
           impGanaP=0;
           double kilos=0,importe=0,impGana;
           double precioCosto=0;
        //   Double impGanaD;
           
           htGana.clear();
           htCliGana.clear();
           int proCodi=0;
           do
           {
               if (cancelarConsulta)
                 return;
               if (proCodi!=dtMarg.getInt("pro_codi") )
               { // Busco Inventario inicial
//                   System.out.println("Producto: "+proCodi+"  Ganancia: "+impGanaP);
                   impGanaP=0;
                   proCodi=dtMarg.getInt("pro_codi");
                   psInv.setInt(1, proCodi);
                   rsInv=psInv.executeQuery();   
                   rsInv.next();
                   kilos=rsInv.getDouble("kilos");
                   importe=rsInv.getDouble("importe");
                   precioCosto= kilos<=0?0:importe/kilos;
               }
               
             
               if (dtMarg.getString("tipmov").equals("E"))
               { // Entrada
                  kilos+=dtMarg.getDouble("canti",true);
                  importe+= dtMarg.getDouble("canti",true)* dtMarg.getDouble("precio",true);
                  precioCosto= kilos<=0?0:importe/kilos;
               }
               else              
               { // Salida
                   if (Formatear.comparaFechas(dtMarg.getDate("mvt_fecdoc"),fecInvD)>0)
                   {                       
                        if  (dtMarg.getString("tipdoc").equals("R") )
                        {
                           psCli.setInt(1,dtMarg.getInt("cliCodi"));
                           rsCli=psAlb.executeQuery();   
                           if (rsCli.next())
                               sumaGanan(rsCli,precioCosto);
                        }
                        if (dtMarg.getString("tipdoc").equals("V") )
                        {
                             psAlb.setInt(1,dtMarg.getInt("mvt_empcod"));
                             psAlb.setInt(2,dtMarg.getInt("mvt_ejedoc"));
                             psAlb.setString(3,dtMarg.getString("mvt_serdoc"));
                             psAlb.setInt(4,dtMarg.getInt("mvt_numdoc"));
                             rsCli=psAlb.executeQuery();   
                             if (rsCli.next())
                                 sumaGanan(rsCli,precioCosto);
                        }
                   }
                   kilos-=dtMarg.getDouble("canti",true);
                   importe-= dtMarg.getDouble("canti",true)* precioCosto;
               }
               
           } while (dtMarg.next());
            SwingUtilities.invokeLater(new Thread()
            {
                @Override
                public void run()
                {
                  muestraMargenes();      
                }
            });
          
        } catch (ParseException | SQLException k)
        {
            Error("Error al buscar margenes de ventas",k);
        }
        
    }
    void muestraMargenes() {
        Iterator<String> it = htGana.keySet().iterator();
        int nl = jt.getRowCount();
        String[] valorC;
        String valor;
        while (it.hasNext())
        {
            if (cancelarConsulta)
                return;
            valor = it.next();
//               System.out.println("valor : "+valor+" Ganancia: "+htGana.get(valor));
            valorC = valor.split("-", 3);
            for (int n = 0; n < nl; n++)
            {
                if (jt.getValString(n, 0).equals(valorC[0])
                    && jt.getValString(n, 1).equals(valorC[1])
                    && jt.getValString(n, JT_SBECOD).equals(valorC[2]))
                {
                    jt.setValor(htGana.get(valor), n, JT_IMPGAN);
                    break;
                }
            }
        }
        double totRepr = 0;
        double totSec = 0;
        double totGen = 0;
        for (int n = 0; n < nl; n++)
        {
            if (cancelarConsulta)
                return;
            jt.setValor(jt.getValorDec(n, JT_IMPGAN) / jt.getValorDec(n, JT_KILVEN),
                n, JT_PORGAN);
            if (jt.getValString(n, 0).equals("."))
            {
                jt.setValor(totSec, n, JT_IMPGAN);
                jt.setValor(totSec / jt.getValorDec(n, JT_KILVEN),
                    n, JT_PORGAN);

                totSec = 0;
                continue;
            }
            if (jt.getValString(n, 1).equals("."))
            {
                jt.setValor(totRepr, n, JT_IMPGAN);
                jt.setValor(totRepr / jt.getValorDec(n, JT_KILVEN),
                    n, JT_PORGAN);
                totRepr = 0;
                continue;
            }
            totRepr += jt.getValorDec(n, JT_IMPGAN);
            totSec += jt.getValorDec(n, JT_IMPGAN);
            totGen += jt.getValorDec(n, JT_IMPGAN);
        }
        impGananE.setValorDec(totGen);
        porGananE.setValorDec(totGen / kilAlbE.getValorDec());
    }
    void sumaGanan(ResultSet rsCli,double precioCosto) throws SQLException
    {
        Double impGanaD;
        double impGana;
        if (! rep_codiE.isNull() && !rep_codiE.getText().equals(rsCli.getString("rep_codi")))
            return;
        if (! zon_codiE.isNull() && !zon_codiE.getText().equals(rsCli.getString("zon_codi")))
            return;
         if (sbe_codiE.getValorInt()!=0 && sbe_codiE.getValorInt()!=rsCli.getInt("sbe_codi"))
            return;
        
        guardaGana(rsCli.getString("rep_codi") + "-" + rsCli.getString("zon_codi")
            + "-" + rsCli.getString("sbe_codi"),precioCosto,htGana);

        guardaGana(rsCli.getString("sbe_codi")+"-"+dtMarg.getInt("cliCodi"),precioCosto,htCliGana);
    
        
        impGanaP+=dtMarg.getDouble("canti", true)
            * (dtMarg.getDouble("precio", true) - precioCosto);
        
    }
    private void guardaGana(String valor,double precioCosto,HashMap<String, Double> ht) throws SQLException
    {
        Double impGanaD;
        double impGana;
        
        if ((impGanaD = ht.get(valor)) == null)
            impGana = 0;
        else
            impGana = impGanaD;
        impGana += dtMarg.getDouble("canti", true)
            * (dtMarg.getDouble("precio", true) - precioCosto);
        ht.put(valor, impGana);

    }
   void buscaVentas(boolean debug)
   {
     try
     {
         if (debug)
            mensaje("Espere, por favor ... buscando Datos");
         s=getStrSql(fecIniE.getText(),fecFinE.getText());
      
   //    debug(s);
         jt.removeAllDatos();
         jtGru.removeAllDatos();
         htArt.clear();
   //    rs=st.executeQuery(s);
         if (!dtCon1.select(s))
         {
           if (debug)
           {
            msgBox("No encontradas Ventas con estos criterios");
            mensaje("");
           }
           return;
         }

        kgVen = 0;
        impVen = 0;
        prVen = 0;
        nAlbL = nCliL = nAlbT = nCliT =  nClAcL = nClAcT = 0;
        kgVenL =  impVenL = 0;
        kgVenT =  impVenT = 0;
        int sbeCodi=dtCon1.getInt("sbe_codi");
        String repr = dtCon1.getString("rep_codi");
        datos=new ArrayList();
        do {

             if (!repr.equals(dtCon1.getString("rep_codi"))) {
                 ponDatosRepr(repr,sbeCodi);
                 repr = dtCon1.getString("rep_codi");
             }
             if (sbeCodi!=dtCon1.getInt("sbe_codi"))
             {
                 if (kgVenL!=0 || impVenL==0)
                 {
                     ponDatosRepr(repr,sbeCodi);
                     repr = dtCon1.getString("rep_codi");
                 }   
                 ponDatosSbe(sbeCodi);
                 sbeCodi=dtCon1.getInt("sbe_codi");
             }
             
             s = "SELECT COUNT(*) as cuantos FROM clientes WHERE "
                     + " zon_codi='" + dtCon1.getString("zon_codi") + "'"
                     + " and rep_codi = '" + dtCon1.getString("rep_codi") + "'"
                     + " and cli_activ ='S'";
             dtStat.select(s);
             nClAcZ = dtStat.getInt("cuantos");


             ArrayList v = new ArrayList();
             v.add(dtCon1.getString("rep_codi"));
             v.add(dtCon1.getString("zon_codi"));
             v.add(MantRepres.getNombRepr(repr,dtStat)
                     + " - " + pdconfig.getTextDiscrim(dtStat, "Cz", EU.em_cod, dtCon1.getString("zon_codi")));
             v.add(dtCon1.getInt("numalb"));
             v.add(dtCon1.getInt("cliCodiD"));
             v.add(dtCon1.getDouble("avc_basimp"));
             v.add(dtCon1.getDouble("avc_kilos"));
             v.add("");
             v.add("");
             v.add(nClAcZ);
             v.add(""+sbeCodi);
             datos.add(v);
             nAlbL += dtCon1.getInt("numalb");
             nCliL += dtCon1.getInt("cliCodiD");
             impVenL += dtCon1.getDouble("avc_basimp");
             kgVenL += dtCon1.getDouble("avc_kilos");
             nClAcL += nClAcZ;
         } while (dtCon1.next());
       ponDatosRepr(repr,sbeCodi);
       ponDatosSbe(sbeCodi);
       jt.setDatos(datos);
       numAlbE.setValorInt(nAlbT);
       numCliE.setValorInt(nCliT);
       nClActE.setValorInt(nClAcT);
       impAlbE.setValorDec(impVenT);
       kilAlbE.setValorDec(kgVenT);

       // Guardo datos por producto.
       s = "select pro_codi,sum(avl_canti) as canti,"
               + " sum(avl_unid) as unidades, "
               + "sum(avl_canti*avl_prbase) as importe" +
           " from v_albavec  c,v_albavel l,clientes cl " +
           " where c.avc_fecalb >='" + fecIniE.getFechaDB() + "'" +
           " and c.avc_fecalb <='" + fecFinE.getFechaDB() + "'" +
           (emp_codiE.getValorInt()==0?"":" and c.emp_codi = "+emp_codiE.getValorInt())+
           (sbe_codiE.getValorInt()==0?"":" and c.sbe_codi = "+sbe_codiE.getValorInt())+
           (zon_codiE.isNull()?"":" and cl.zon_codi = '"+zon_codiE.getText()+"'")+
           (rep_codiE.isNull()?"":" and cl.rep_codi = '"+rep_codiE.getText()+"'")+
            bdiscr.getCondWhere("cl")+
           " and l.emp_codi = c.emp_codi" +
           " and c.avc_ano = l.avc_ano  " +
           " and l.avc_serie = c.avc_serie " +
           " and c.avc_nume = l.avc_nume " +
           " and cl.cli_codi = c.cli_codi " +
            " group by pro_codi " ;
       if (dtCon1.select(s))
       {
           do
           {
               DatosProd dtPro=new DatosProd(dtCon1.getInt("pro_codi"));
               dtPro.setKilos(dtCon1.getDouble("canti"));
               dtPro.setImporte(dtCon1.getDouble("importe"));
               dtPro.setUnidades(dtCon1.getInt("unidades"));
               htArt.put(dtCon1.getInt("pro_codi"), dtPro);
           } while (dtCon1.next());
       }
       arbolProdPanel.setValoresArticulos(htArt);
       jt.setEnabled(true);
       jt.requestFocusInicio();
//       swBuscaFam=false;
       s = "select g.agr_codi, g.agp_nomb,sum(avl_canti) as canti" +
           " from v_famipro f, grufampro as gf, v_agupro as g, v_articulo p,v_albavec  c,v_albavel l,clientes cl " +
           " where c.avc_fecalb >='" + fecIniE.getFechaDB() + "'" +
           " and c.avc_fecalb <='" + fecFinE.getFechaDB() + "'" +
           (emp_codiE.getValorInt()==0?"":" and c.emp_codi = "+emp_codiE.getValorInt())+
           (sbe_codiE.getValorInt()==0?"":" and c.sbe_codi = "+sbe_codiE.getValorInt())+
           (zon_codiE.isNull()?"":" and cl.zon_codi = '"+zon_codiE.getText()+"'")+
           (rep_codiE.isNull()?"":" and cl.rep_codi = '"+rep_codiE.getText()+"'")+
            bdiscr.getCondWhere("cl")+
           " and l.emp_codi = c.emp_codi" +
           " and c.avc_ano = l.avc_ano  " +
           " and l.avc_serie = c.avc_serie " +
           " and c.avc_nume = l.avc_nume " +
           " and cl.cli_codi = c.cli_codi " +
           " and f.fpr_codi = p.fam_codi " +
           " and gf.fpr_codi = f.fpr_codi "+
           " and g.agr_codi = gf.agr_codi "+
           " and p.pro_codi = l.pro_codi ";
       s += " group by g.agr_codi,g.agp_nomb " +
           " order by g.agr_codi";
 //    debug("s: "+s);
       dtCon1.select(s);
       if (!dtCon1.select(s))
         return;

       do
       {
         ArrayList v = new ArrayList();
         v.add(dtCon1.getString("agr_codi"));
         v.add(dtCon1.getString("agp_nomb"));
         v.add(dtCon1.getString("canti"));

         jtGru.addLinea(v);
       }  while (dtCon1.next());
       if (debug)
       {
        mensajeErr("Busqueda ... realizada");
        mensaje("Doble Click o F2 en Resultados Zona para Desglosar Clientes");
       }
       llenaFamilias();
       arbolProdPanel.verDatosArbol();
//       swBuscaFam=true;
     }
     catch (Exception k)
     {
       Error("Error al Calcular Datos", k);
     }
   }
   void llenaFamilias() throws Exception
   {
    if (! swBuscaFam)
        return;
    jtFam.removeAllDatos();
    if (jtGru.isVacio())
        return;
       s = "select f.fpr_codi, f.fpr_nomb,sum(avl_canti) as canti" +
           " from v_famipro f, grufampro as gf, v_articulo p,v_albavec  c,v_albavel l,clientes cl " +
           " where c.avc_fecalb >='" + fecIniE.getFechaDB() + "'" +
           " and c.avc_fecalb <='" + fecFinE.getFechaDB() + "'" +
           (emp_codiE.getValorInt()==0?"":" and c.emp_codi = "+emp_codiE.getValorInt())+
           (sbe_codiE.getValorInt()==0?"":" and c.sbe_codi = "+sbe_codiE.getValorInt())+
           (zon_codiE.isNull()?"":" and cl.zon_codi = '"+zon_codiE.getText()+"'")+
           (rep_codiE.isNull()?"":" and cl.rep_codi = '"+rep_codiE.getText()+"'")+
            bdiscr.getCondWhere("cl")+
           " and l.emp_codi = c.emp_codi" +
           " and c.avc_ano = l.avc_ano  " +
           " and l.avc_serie = c.avc_serie " +
           " and c.avc_nume = l.avc_nume " +
           " and cl.cli_codi = c.cli_codi " +
           " and f.fpr_codi = p.fam_codi " +
           " and gf.agr_codi = "+jtGru.getValorInt(0)+
           " and gf.fpr_codi = f.fpr_codi "+
           " and p.pro_codi = l.pro_codi ";
       s += " group by f.fpr_codi,f.fpr_nomb " +
           " order by f.fpr_codi";
 //    debug("s: "+s);
       dtCon1.select(s);
       if (!dtCon1.select(s))
         return;

       do
       {
         ArrayList v = new ArrayList();
         v.add(dtCon1.getString("fpr_codi"));
         v.add(dtCon1.getString("fpr_nomb"));
         v.add(dtCon1.getString("canti"));
         jtFam.addLinea(v);
       } while (dtCon1.next());
       jtFam.requestFocusInicio();
   }
   public Date getDateInicial() throws ParseException
   {
       return fecIniE.getDate();
   }
   public Date getDateFinal() throws ParseException
   {
       return fecFinE.getDate();
   }

   public String getFechaInic()
   {
       return fecIniE.getText();
   }
   public String getFechaFinal()
   {
       return fecFinE.getText();
   }
   public String getFechaInicComp()
   {
        return fecIniComE.getText();
   }
   public String getFechaFinComp()
   {
        return fecFinComE.getText();
   }
   public int getEmpresa()
   {
       return emp_codiE.getValorInt();
   }
   public int getSubEmpresa()
   {
       return sbe_codiE.getValorInt();
   }
   public String getZona()
   {
       return zon_codiE.getText();
   }
   public String getRepresentante()
   {
       return rep_codiE.getText();
   }
   public int getCabeceraInic()
   {
       return cabIni;
   }
   public int getCabeceraFin()
   {
       return cabFin;
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

        cTabbedPane1 = new gnu.chu.controles.CTabbedPane();
        Pprinc = new gnu.chu.controles.CPanel();
        TPanel1 = new gnu.chu.controles.CTabbedPane();
        PPrinc = new gnu.chu.controles.CPanel();
        jt = new gnu.chu.controles.Cgrid(11);
        jt.setOrdenar(false);
        ArrayList v = new ArrayList();
        v.add("Repr"); //0
        v.add("Zona"); //1
        v.add("Descripcion"); // 2
        v.add("N.Alb."); // 3
        v.add("N.Cli."); // 4
        v.add("Importe"); // 5
        v.add("Kilos"); // 6
        v.add("Imp.Gan"); // 7
        v.add("Gan.kg"); // 8
        v.add("Cl.Act"); // 9
        v.add("SE"); // 10
        jt.setCabecera(v);
        jt.setAnchoColumna(new int[]{43,43,208,45,45,78,78,60,50,35,30});
        jt.setAlinearColumna(new int[]{0,0,0,2,2,2,2,2,2,2,2});
        jt.setFormatoColumna(3, "###9");
        jt.setFormatoColumna(4, "###9");
        jt.setFormatoColumna(5, "--,---,--9.99");
        jt.setFormatoColumna(6, "--,---,--9.99");
        jt.setFormatoColumna(7, "----,--9");
        jt.setFormatoColumna(8, "--9.999");
        jt.setFormatoColumna(9, "###9");
        Ppie = new gnu.chu.controles.CPanel();
        cLabel1 = new gnu.chu.controles.CLabel();
        numAlbE = new gnu.chu.controles.CTextField(Types.DECIMAL,"####9");
        cLabel2 = new gnu.chu.controles.CLabel();
        numCliE = new gnu.chu.controles.CTextField(Types.DECIMAL,"####9");
        cLabel7 = new gnu.chu.controles.CLabel();
        impAlbE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--,---,--9.99");
        cLabel8 = new gnu.chu.controles.CLabel();
        nClActE = new gnu.chu.controles.CTextField(Types.DECIMAL,"####9");
        cLabel9 = new gnu.chu.controles.CLabel();
        kilAlbE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--,---,--9.99");
        cLabel15 = new gnu.chu.controles.CLabel();
        impGananE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--,---,--9.99");
        porGananE = new gnu.chu.controles.CTextField(Types.DECIMAL,"-9.999");
        Pbuscando = new gnu.chu.controles.CPanel();
        cLabel14 = new gnu.chu.controles.CLabel();
        cLabel13 = new gnu.chu.controles.CLabel();
        PCompar = new gnu.chu.controles.CPanel();
        PcabCom = new gnu.chu.controles.CPanel();
        cLabel10 = new gnu.chu.controles.CLabel();
        cLabel11 = new gnu.chu.controles.CLabel();
        cLabel12 = new gnu.chu.controles.CLabel();
        AnoComE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        BaceptarCom = new gnu.chu.controles.CButton();
        fecIniComE = new gnu.chu.camposdb.fechaCal();
        fecFinComE = new gnu.chu.camposdb.fechaCal();
        jtCom = new gnu.chu.controles.Cgrid(10);
        setCabeceraComparativo(0,0);
        jtCom.setOrdenar(false);

        jtCom.setAlinearColumna(new int[]{0,0,0,2,2,2,2,2,2,2});
        jtCom.setFormatoColumna(3, "###9");
        jtCom.setFormatoColumna(4, "###9");
        jtCom.setFormatoColumna(3, "--,---,--9.99");
        jtCom.setFormatoColumna(4, "--,---,--9.99");
        jtCom.setFormatoColumna(5, "--,---,--9.99");
        jtCom.setFormatoColumna(6, "--,---,--9.99");
        jtCom.setFormatoColumna(7, "--,---,--9.99");
        jtCom.setFormatoColumna(8, "--,---,--9.99");
        Ppie2 = new gnu.chu.controles.CPanel();
        cLabel20 = new gnu.chu.controles.CLabel();
        numAlbFinE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----9");
        cLabel21 = new gnu.chu.controles.CLabel();
        numCliOriE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----9");
        cLabel22 = new gnu.chu.controles.CLabel();
        impAlbFinE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--,---,--9.99");
        cLabel24 = new gnu.chu.controles.CLabel();
        kilAlbFinE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--,---,--9.99");
        numAlbOriE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----9");
        numAlbDifE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----9");
        cLabel18 = new gnu.chu.controles.CLabel();
        labIniL = new gnu.chu.controles.CLabel();
        labFinL = new gnu.chu.controles.CLabel();
        numCliFinE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----9");
        numCliDifE = new gnu.chu.controles.CTextField(Types.DECIMAL,"----9");
        labIni1L = new gnu.chu.controles.CLabel();
        labFin1L = new gnu.chu.controles.CLabel();
        cLabel28 = new gnu.chu.controles.CLabel();
        kilAlbOriE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--,---,--9.99");
        impAlbDifE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--,---,--9.99");
        kilAlbDifE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--,---,--9.99");
        impAlbOriE = new gnu.chu.controles.CTextField(Types.DECIMAL,"--,---,--9.99");
        PArbol = new gnu.chu.controles.CPanel();
        arbolProdPanel = new gnu.chu.anjelica.pad.ArbolProdPanel();
        PGrupos = new gnu.chu.controles.CPanel();
        jtGru = new gnu.chu.controles.Cgrid(3);
        ArrayList vg=new ArrayList();
        vg.add("Grupo");
        vg.add("Descr.Grupo");
        vg.add("Kilos");
        jtGru.setCabecera(vg);
        jtGru.setAnchoColumna(new int[]{30,100,50});

        jtGru.setAjustarGrid(true);
        jtGru.setAlinearColumna(new int[]{2,0,2});
        jtGru.setFormatoColumna(2,"----,--9.99");
        jtFam = new gnu.chu.controles.Cgrid(3);
        Pcabe = new gnu.chu.controles.CPanel();
        cLabel5 = new gnu.chu.controles.CLabel();
        cLabel6 = new gnu.chu.controles.CLabel();
        cLabel16 = new gnu.chu.controles.CLabel();
        rep_codiE = new gnu.chu.controles.CLinkBox();
        cLabel3 = new gnu.chu.controles.CLabel();
        emp_codiE = new gnu.chu.camposdb.empPanel();
        cLabel4 = new gnu.chu.controles.CLabel();
        sbe_codiE = new gnu.chu.camposdb.sbePanel();
        Baceptar = new gnu.chu.controles.CButtonMenu();
        cLabel17 = new gnu.chu.controles.CLabel();
        zon_codiE = new gnu.chu.controles.CLinkBox();
        bdiscr = new gnu.chu.camposdb.DiscButton();
        fecIniE = new gnu.chu.camposdb.fechaCal();
        fecFinE = new gnu.chu.camposdb.fechaCal();
        opIncComent = new gnu.chu.controles.CCheckBox();
        opDivisa = new gnu.chu.controles.CCheckBox();

        Pprinc.setLayout(new java.awt.GridBagLayout());

        PPrinc.setLayout(new java.awt.GridBagLayout());

        jt.setAjustarGrid(true);
        jt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jt.setPreferredSize(new java.awt.Dimension(200, 100));

        org.jdesktop.layout.GroupLayout jtLayout = new org.jdesktop.layout.GroupLayout(jt);
        jt.setLayout(jtLayout);
        jtLayout.setHorizontalGroup(
            jtLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 1082, Short.MAX_VALUE)
        );
        jtLayout.setVerticalGroup(
            jtLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 293, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.weighty = 2.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        PPrinc.add(jt, gridBagConstraints);

        Ppie.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        Ppie.setEnabled(false);
        Ppie.setMaximumSize(new java.awt.Dimension(500, 42));
        Ppie.setMinimumSize(new java.awt.Dimension(500, 42));
        Ppie.setPreferredSize(new java.awt.Dimension(500, 42));
        Ppie.setLayout(null);

        cLabel1.setText("Num.Albaranes ");
        cLabel1.setMaximumSize(new java.awt.Dimension(87, 18));
        cLabel1.setMinimumSize(new java.awt.Dimension(87, 18));
        cLabel1.setPreferredSize(new java.awt.Dimension(87, 18));
        Ppie.add(cLabel1);
        cLabel1.setBounds(10, 0, 87, 18);

        numAlbE.setMinimumSize(new java.awt.Dimension(2, 18));
        numAlbE.setPreferredSize(new java.awt.Dimension(2, 18));
        Ppie.add(numAlbE);
        numAlbE.setBounds(100, 0, 59, 18);

        cLabel2.setText("Num. Clientes ");
        cLabel2.setMaximumSize(new java.awt.Dimension(87, 18));
        cLabel2.setMinimumSize(new java.awt.Dimension(87, 18));
        cLabel2.setPreferredSize(new java.awt.Dimension(87, 18));
        Ppie.add(cLabel2);
        cLabel2.setBounds(160, 0, 81, 18);

        numCliE.setMinimumSize(new java.awt.Dimension(2, 18));
        numCliE.setPreferredSize(new java.awt.Dimension(2, 18));
        Ppie.add(numCliE);
        numCliE.setBounds(250, 0, 59, 18);

        cLabel7.setText("Importe");
        cLabel7.setMaximumSize(new java.awt.Dimension(87, 18));
        cLabel7.setMinimumSize(new java.awt.Dimension(87, 18));
        cLabel7.setPreferredSize(new java.awt.Dimension(87, 18));
        Ppie.add(cLabel7);
        cLabel7.setBounds(10, 20, 51, 18);

        impAlbE.setMinimumSize(new java.awt.Dimension(2, 18));
        impAlbE.setPreferredSize(new java.awt.Dimension(2, 18));
        Ppie.add(impAlbE);
        impAlbE.setBounds(70, 20, 109, 18);

        cLabel8.setText("N. Clientes Activos ");
        cLabel8.setMaximumSize(new java.awt.Dimension(87, 18));
        cLabel8.setMinimumSize(new java.awt.Dimension(87, 18));
        cLabel8.setPreferredSize(new java.awt.Dimension(87, 18));
        Ppie.add(cLabel8);
        cLabel8.setBounds(320, 0, 110, 18);

        nClActE.setMinimumSize(new java.awt.Dimension(2, 18));
        nClActE.setPreferredSize(new java.awt.Dimension(2, 18));
        Ppie.add(nClActE);
        nClActE.setBounds(430, 0, 59, 18);

        cLabel9.setText("Ganancia");
        cLabel9.setMaximumSize(new java.awt.Dimension(87, 18));
        cLabel9.setMinimumSize(new java.awt.Dimension(87, 18));
        cLabel9.setPreferredSize(new java.awt.Dimension(87, 18));
        Ppie.add(cLabel9);
        cLabel9.setBounds(190, 20, 60, 18);

        kilAlbE.setMinimumSize(new java.awt.Dimension(2, 18));
        kilAlbE.setPreferredSize(new java.awt.Dimension(2, 18));
        Ppie.add(kilAlbE);
        kilAlbE.setBounds(399, 20, 90, 18);

        cLabel15.setText("Kilos");
        cLabel15.setMaximumSize(new java.awt.Dimension(87, 18));
        cLabel15.setMinimumSize(new java.awt.Dimension(87, 18));
        cLabel15.setPreferredSize(new java.awt.Dimension(87, 18));
        Ppie.add(cLabel15);
        cLabel15.setBounds(360, 20, 37, 18);

        impGananE.setMinimumSize(new java.awt.Dimension(2, 18));
        impGananE.setPreferredSize(new java.awt.Dimension(2, 18));
        Ppie.add(impGananE);
        impGananE.setBounds(250, 20, 70, 18);

        porGananE.setMinimumSize(new java.awt.Dimension(2, 18));
        porGananE.setPreferredSize(new java.awt.Dimension(2, 18));
        Ppie.add(porGananE);
        porGananE.setBounds(320, 20, 40, 18);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        PPrinc.add(Ppie, gridBagConstraints);

        Pbuscando.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Pbuscando.setMaximumSize(new java.awt.Dimension(120, 55));
        Pbuscando.setMinimumSize(new java.awt.Dimension(120, 55));
        Pbuscando.setPreferredSize(new java.awt.Dimension(120, 55));
        Pbuscando.setLayout(null);

        cLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gnu/chu/icons/ocupado.gif"))); // NOI18N
        Pbuscando.add(cLabel14);
        cLabel14.setBounds(60, 0, 60, 55);

        cLabel13.setForeground(java.awt.Color.blue);
        cLabel13.setText("<html>Buscando Margenes</html>");
        cLabel13.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        Pbuscando.add(cLabel13);
        cLabel13.setBounds(0, 0, 70, 50);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        PPrinc.add(Pbuscando, gridBagConstraints);

        TPanel1.addTab("Zonas", PPrinc);

        PCompar.setLayout(new java.awt.GridBagLayout());

        PcabCom.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        PcabCom.setMinimumSize(new java.awt.Dimension(540, 24));
        PcabCom.setPreferredSize(new java.awt.Dimension(540, 24));
        PcabCom.setLayout(null);

        cLabel10.setText("Año");
        cLabel10.setPreferredSize(new java.awt.Dimension(52, 18));
        PcabCom.add(cLabel10);
        cLabel10.setBounds(10, 2, 30, 18);

        cLabel11.setText("A Fecha");
        cLabel11.setMaximumSize(new java.awt.Dimension(43, 18));
        cLabel11.setMinimumSize(new java.awt.Dimension(43, 18));
        cLabel11.setPreferredSize(new java.awt.Dimension(44, 18));
        PcabCom.add(cLabel11);
        cLabel11.setBounds(250, 2, 44, 18);

        cLabel12.setText("De Fecha");
        cLabel12.setPreferredSize(new java.awt.Dimension(52, 18));
        PcabCom.add(cLabel12);
        cLabel12.setBounds(100, 2, 52, 18);
        PcabCom.add(AnoComE);
        AnoComE.setBounds(40, 2, 40, 18);

        BaceptarCom.setText("Aceptar (F4)");
        PcabCom.add(BaceptarCom);
        BaceptarCom.setBounds(400, 0, 100, 22);
        PcabCom.add(fecIniComE);
        fecIniComE.setBounds(150, 0, 90, 20);

        fecFinComE.setTipoFecha(2);
        PcabCom.add(fecFinComE);
        fecFinComE.setBounds(300, 0, 90, 18);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        PCompar.add(PcabCom, gridBagConstraints);

        jtCom.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtCom.setAjustarGrid(true);
        jtCom.setMinimumSize(new java.awt.Dimension(200, 100));

        org.jdesktop.layout.GroupLayout jtComLayout = new org.jdesktop.layout.GroupLayout(jtCom);
        jtCom.setLayout(jtComLayout);
        jtComLayout.setHorizontalGroup(
            jtComLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 1078, Short.MAX_VALUE)
        );
        jtComLayout.setVerticalGroup(
            jtComLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 250, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 2.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        PCompar.add(jtCom, gridBagConstraints);

        Ppie2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        Ppie2.setEnabled(false);
        Ppie2.setMinimumSize(new java.awt.Dimension(634, 66));
        Ppie2.setPreferredSize(new java.awt.Dimension(634, 66));
        Ppie2.setLayout(null);

        cLabel20.setText("Albaranes ");
        cLabel20.setMaximumSize(new java.awt.Dimension(87, 18));
        cLabel20.setMinimumSize(new java.awt.Dimension(87, 18));
        cLabel20.setPreferredSize(new java.awt.Dimension(87, 18));
        Ppie2.add(cLabel20);
        cLabel20.setBounds(0, 20, 70, 18);

        numAlbFinE.setEditable(false);
        numAlbFinE.setMinimumSize(new java.awt.Dimension(2, 18));
        numAlbFinE.setPreferredSize(new java.awt.Dimension(2, 18));
        Ppie2.add(numAlbFinE);
        numAlbFinE.setBounds(135, 19, 59, 18);

        cLabel21.setText("Clientes ");
        cLabel21.setMaximumSize(new java.awt.Dimension(87, 18));
        cLabel21.setMinimumSize(new java.awt.Dimension(87, 18));
        cLabel21.setPreferredSize(new java.awt.Dimension(87, 18));
        Ppie2.add(cLabel21);
        cLabel21.setBounds(0, 40, 60, 18);

        numCliOriE.setEditable(false);
        numCliOriE.setMinimumSize(new java.awt.Dimension(2, 18));
        numCliOriE.setPreferredSize(new java.awt.Dimension(2, 18));
        Ppie2.add(numCliOriE);
        numCliOriE.setBounds(70, 40, 59, 18);

        cLabel22.setText("Importe");
        cLabel22.setMaximumSize(new java.awt.Dimension(87, 18));
        cLabel22.setMinimumSize(new java.awt.Dimension(87, 18));
        cLabel22.setPreferredSize(new java.awt.Dimension(87, 18));
        Ppie2.add(cLabel22);
        cLabel22.setBounds(270, 40, 50, 18);

        impAlbFinE.setEditable(false);
        impAlbFinE.setMinimumSize(new java.awt.Dimension(2, 18));
        impAlbFinE.setPreferredSize(new java.awt.Dimension(2, 18));
        Ppie2.add(impAlbFinE);
        impAlbFinE.setBounds(425, 40, 100, 18);

        cLabel24.setText("Kilos");
        cLabel24.setMaximumSize(new java.awt.Dimension(87, 18));
        cLabel24.setMinimumSize(new java.awt.Dimension(87, 18));
        cLabel24.setPreferredSize(new java.awt.Dimension(87, 18));
        Ppie2.add(cLabel24);
        cLabel24.setBounds(290, 20, 30, 18);

        kilAlbFinE.setEditable(false);
        kilAlbFinE.setMinimumSize(new java.awt.Dimension(2, 18));
        kilAlbFinE.setPreferredSize(new java.awt.Dimension(2, 18));
        Ppie2.add(kilAlbFinE);
        kilAlbFinE.setBounds(425, 20, 100, 18);

        numAlbOriE.setEditable(false);
        numAlbOriE.setMinimumSize(new java.awt.Dimension(2, 18));
        numAlbOriE.setPreferredSize(new java.awt.Dimension(2, 18));
        Ppie2.add(numAlbOriE);
        numAlbOriE.setBounds(70, 19, 59, 18);

        numAlbDifE.setEditable(false);
        numAlbDifE.setMinimumSize(new java.awt.Dimension(2, 18));
        numAlbDifE.setPreferredSize(new java.awt.Dimension(2, 18));
        Ppie2.add(numAlbDifE);
        numAlbDifE.setBounds(200, 19, 59, 18);

        cLabel18.setBackground(new java.awt.Color(51, 255, 255));
        cLabel18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cLabel18.setText("Diferencia");
        cLabel18.setOpaque(true);
        Ppie2.add(cLabel18);
        cLabel18.setBounds(200, 0, 60, 15);

        labIniL.setBackground(new java.awt.Color(51, 255, 255));
        labIniL.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        labIniL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labIniL.setText("Inicial");
        labIniL.setOpaque(true);
        Ppie2.add(labIniL);
        labIniL.setBounds(68, 0, 60, 17);

        labFinL.setBackground(new java.awt.Color(51, 255, 255));
        labFinL.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        labFinL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labFinL.setText("Final");
        labFinL.setOpaque(true);
        Ppie2.add(labFinL);
        labFinL.setBounds(135, 0, 60, 15);

        numCliFinE.setEditable(false);
        numCliFinE.setMinimumSize(new java.awt.Dimension(2, 18));
        numCliFinE.setPreferredSize(new java.awt.Dimension(2, 18));
        Ppie2.add(numCliFinE);
        numCliFinE.setBounds(135, 40, 59, 18);

        numCliDifE.setEditable(false);
        numCliDifE.setMinimumSize(new java.awt.Dimension(2, 18));
        numCliDifE.setPreferredSize(new java.awt.Dimension(2, 18));
        Ppie2.add(numCliDifE);
        numCliDifE.setBounds(200, 40, 59, 18);

        labIni1L.setBackground(new java.awt.Color(51, 255, 255));
        labIni1L.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        labIni1L.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labIni1L.setText("Inicial");
        labIni1L.setOpaque(true);
        Ppie2.add(labIni1L);
        labIni1L.setBounds(320, 0, 100, 17);

        labFin1L.setBackground(new java.awt.Color(51, 255, 255));
        labFin1L.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        labFin1L.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labFin1L.setText("Final");
        labFin1L.setOpaque(true);
        Ppie2.add(labFin1L);
        labFin1L.setBounds(425, 0, 100, 15);

        cLabel28.setBackground(new java.awt.Color(51, 255, 255));
        cLabel28.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cLabel28.setText("Diferencia");
        cLabel28.setOpaque(true);
        Ppie2.add(cLabel28);
        cLabel28.setBounds(530, 0, 100, 15);

        kilAlbOriE.setEditable(false);
        kilAlbOriE.setMinimumSize(new java.awt.Dimension(2, 18));
        kilAlbOriE.setPreferredSize(new java.awt.Dimension(2, 18));
        Ppie2.add(kilAlbOriE);
        kilAlbOriE.setBounds(320, 20, 100, 18);

        impAlbDifE.setEditable(false);
        impAlbDifE.setMinimumSize(new java.awt.Dimension(2, 18));
        impAlbDifE.setPreferredSize(new java.awt.Dimension(2, 18));
        Ppie2.add(impAlbDifE);
        impAlbDifE.setBounds(530, 40, 100, 18);

        kilAlbDifE.setEditable(false);
        kilAlbDifE.setMinimumSize(new java.awt.Dimension(2, 18));
        kilAlbDifE.setPreferredSize(new java.awt.Dimension(2, 18));
        Ppie2.add(kilAlbDifE);
        kilAlbDifE.setBounds(530, 20, 100, 18);

        impAlbOriE.setEditable(false);
        impAlbOriE.setMinimumSize(new java.awt.Dimension(2, 18));
        impAlbOriE.setPreferredSize(new java.awt.Dimension(2, 18));
        Ppie2.add(impAlbOriE);
        impAlbOriE.setBounds(320, 40, 100, 18);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        PCompar.add(Ppie2, gridBagConstraints);

        TPanel1.addTab("Comparativo", PCompar);

        PArbol.setLayout(new java.awt.BorderLayout());
        PArbol.add(arbolProdPanel, java.awt.BorderLayout.CENTER);

        TPanel1.addTab("Arbol Familias", PArbol);

        PGrupos.setLayout(new java.awt.GridBagLayout());

        jtGru.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtGru.setMaximumSize(new java.awt.Dimension(200, 70));
        jtGru.setMinimumSize(new java.awt.Dimension(200, 70));

        org.jdesktop.layout.GroupLayout jtGruLayout = new org.jdesktop.layout.GroupLayout(jtGru);
        jtGru.setLayout(jtGruLayout);
        jtGruLayout.setHorizontalGroup(
            jtGruLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 1082, Short.MAX_VALUE)
        );
        jtGruLayout.setVerticalGroup(
            jtGruLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 173, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        PGrupos.add(jtGru, gridBagConstraints);

        jtFam.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        ArrayList v2=new ArrayList();
        v2.add("Familia");
        v2.add("Descr.Fam");
        v2.add("Kilos");
        jtFam.setCabecera(v2);
        jtFam.setAnchoColumna(new int[]{30,100,50});

        jtFam.setAjustarGrid(true);
        jtFam.setAlinearColumna(new int[]{2,0,2});
        jtFam.setFormatoColumna(2,"----,--9.99");
        jtFam.setBuscarVisible(false);
        jtFam.setMaximumSize(new java.awt.Dimension(100, 70));
        jtFam.setMinimumSize(new java.awt.Dimension(100, 70));

        org.jdesktop.layout.GroupLayout jtFamLayout = new org.jdesktop.layout.GroupLayout(jtFam);
        jtFam.setLayout(jtFamLayout);
        jtFamLayout.setHorizontalGroup(
            jtFamLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 1082, Short.MAX_VALUE)
        );
        jtFamLayout.setVerticalGroup(
            jtFamLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 173, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        PGrupos.add(jtFam, gridBagConstraints);

        TPanel1.addTab("Grupos", PGrupos);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Pprinc.add(TPanel1, gridBagConstraints);

        Pcabe.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Pcabe.setMaximumSize(new java.awt.Dimension(680, 50));
        Pcabe.setMinimumSize(new java.awt.Dimension(680, 50));
        Pcabe.setPreferredSize(new java.awt.Dimension(680, 50));
        Pcabe.setRequestFocusEnabled(false);
        Pcabe.setLayout(null);

        cLabel5.setText("De Fecha");
        cLabel5.setPreferredSize(new java.awt.Dimension(52, 18));
        Pcabe.add(cLabel5);
        cLabel5.setBounds(210, 20, 52, 18);

        cLabel6.setText("A Fecha");
        cLabel6.setMaximumSize(new java.awt.Dimension(43, 18));
        cLabel6.setMinimumSize(new java.awt.Dimension(43, 18));
        cLabel6.setPreferredSize(new java.awt.Dimension(44, 18));
        Pcabe.add(cLabel6);
        cLabel6.setBounds(350, 20, 44, 18);

        cLabel16.setText("Repres.");
        cLabel16.setPreferredSize(new java.awt.Dimension(60, 18));
        Pcabe.add(cLabel16);
        cLabel16.setBounds(10, 0, 60, 18);

        rep_codiE.setAncTexto(30);
        rep_codiE.setPreferredSize(new java.awt.Dimension(92, 18));
        Pcabe.add(rep_codiE);
        rep_codiE.setBounds(60, 0, 220, 18);

        cLabel3.setText("Empresa");
        Pcabe.add(cLabel3);
        cLabel3.setBounds(10, 20, 49, 18);

        emp_codiE.setPreferredSize(new java.awt.Dimension(39, 18));
        Pcabe.add(emp_codiE);
        emp_codiE.setBounds(60, 20, 40, 20);

        cLabel4.setText("Delegación");
        Pcabe.add(cLabel4);
        cLabel4.setBounds(100, 20, 70, 18);
        Pcabe.add(sbe_codiE);
        sbe_codiE.setBounds(170, 20, 37, 20);

        Baceptar.setText("Aceptar (F4)");
        Pcabe.add(Baceptar);
        Baceptar.setBounds(500, 20, 110, 26);

        cLabel17.setText("Zona");
        cLabel17.setPreferredSize(new java.awt.Dimension(60, 18));
        Pcabe.add(cLabel17);
        cLabel17.setBounds(290, 0, 60, 18);

        zon_codiE.setAncTexto(30);
        zon_codiE.setPreferredSize(new java.awt.Dimension(92, 18));
        Pcabe.add(zon_codiE);
        zon_codiE.setBounds(320, 0, 220, 18);

        bdiscr.setPreferredSize(new java.awt.Dimension(18, 18));
        Pcabe.add(bdiscr);
        bdiscr.setBounds(540, 0, 30, 18);
        Pcabe.add(fecIniE);
        fecIniE.setBounds(260, 20, 90, 20);

        fecFinE.setTipoFecha(2);
        Pcabe.add(fecFinE);
        fecFinE.setBounds(400, 20, 90, 20);

        opIncComent.setSelected(true);
        opIncComent.setText("Todos Prod.");
        opIncComent.setToolTipText("Incluir Todos productos,no solo los vendibles.");
        opIncComent.setPreferredSize(new java.awt.Dimension(83, 18));
        Pcabe.add(opIncComent);
        opIncComent.setBounds(580, 0, 90, 18);

        opDivisa.setText("Div");
        Pcabe.add(opDivisa);
        opDivisa.setBounds(613, 20, 60, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        Pprinc.add(Pcabe, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CTextField AnoComE;
    private gnu.chu.controles.CButtonMenu Baceptar;
    private gnu.chu.controles.CButton BaceptarCom;
    private gnu.chu.controles.CPanel PArbol;
    private gnu.chu.controles.CPanel PCompar;
    private gnu.chu.controles.CPanel PGrupos;
    private gnu.chu.controles.CPanel PPrinc;
    private gnu.chu.controles.CPanel Pbuscando;
    private gnu.chu.controles.CPanel PcabCom;
    private gnu.chu.controles.CPanel Pcabe;
    private gnu.chu.controles.CPanel Ppie;
    private gnu.chu.controles.CPanel Ppie2;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CTabbedPane TPanel1;
    private gnu.chu.anjelica.pad.ArbolProdPanel arbolProdPanel;
    private gnu.chu.camposdb.DiscButton bdiscr;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel10;
    private gnu.chu.controles.CLabel cLabel11;
    private gnu.chu.controles.CLabel cLabel12;
    private gnu.chu.controles.CLabel cLabel13;
    private gnu.chu.controles.CLabel cLabel14;
    private gnu.chu.controles.CLabel cLabel15;
    private gnu.chu.controles.CLabel cLabel16;
    private gnu.chu.controles.CLabel cLabel17;
    private gnu.chu.controles.CLabel cLabel18;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel20;
    private gnu.chu.controles.CLabel cLabel21;
    private gnu.chu.controles.CLabel cLabel22;
    private gnu.chu.controles.CLabel cLabel24;
    private gnu.chu.controles.CLabel cLabel28;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.controles.CLabel cLabel9;
    private gnu.chu.controles.CTabbedPane cTabbedPane1;
    private gnu.chu.camposdb.empPanel emp_codiE;
    private gnu.chu.camposdb.fechaCal fecFinComE;
    private gnu.chu.camposdb.fechaCal fecFinE;
    private gnu.chu.camposdb.fechaCal fecIniComE;
    private gnu.chu.camposdb.fechaCal fecIniE;
    private gnu.chu.controles.CTextField impAlbDifE;
    private gnu.chu.controles.CTextField impAlbE;
    private gnu.chu.controles.CTextField impAlbFinE;
    private gnu.chu.controles.CTextField impAlbOriE;
    private gnu.chu.controles.CTextField impGananE;
    private gnu.chu.controles.Cgrid jt;
    private gnu.chu.controles.Cgrid jtCom;
    private gnu.chu.controles.Cgrid jtFam;
    private gnu.chu.controles.Cgrid jtGru;
    private gnu.chu.controles.CTextField kilAlbDifE;
    private gnu.chu.controles.CTextField kilAlbE;
    private gnu.chu.controles.CTextField kilAlbFinE;
    private gnu.chu.controles.CTextField kilAlbOriE;
    private gnu.chu.controles.CLabel labFin1L;
    private gnu.chu.controles.CLabel labFinL;
    private gnu.chu.controles.CLabel labIni1L;
    private gnu.chu.controles.CLabel labIniL;
    private gnu.chu.controles.CTextField nClActE;
    private gnu.chu.controles.CTextField numAlbDifE;
    private gnu.chu.controles.CTextField numAlbE;
    private gnu.chu.controles.CTextField numAlbFinE;
    private gnu.chu.controles.CTextField numAlbOriE;
    private gnu.chu.controles.CTextField numCliDifE;
    private gnu.chu.controles.CTextField numCliE;
    private gnu.chu.controles.CTextField numCliFinE;
    private gnu.chu.controles.CTextField numCliOriE;
    private gnu.chu.controles.CCheckBox opDivisa;
    private gnu.chu.controles.CCheckBox opIncComent;
    private gnu.chu.controles.CTextField porGananE;
    private gnu.chu.controles.CLinkBox rep_codiE;
    private gnu.chu.camposdb.sbePanel sbe_codiE;
    private gnu.chu.controles.CLinkBox zon_codiE;
    // End of variables declaration//GEN-END:variables

}

//class conVenZonasTh extends Thread
//{
//  conVenZonas pr;
//
//  public conVenZonasTh(conVenZonas padre)
//  {
//    pr=padre;
//    this.start();
//  }
//  public void run()
//  {
//    this.setPriority(8);
//    pr.buscaVentas();
//    pr.setEnabled(true);
//  }
//}
