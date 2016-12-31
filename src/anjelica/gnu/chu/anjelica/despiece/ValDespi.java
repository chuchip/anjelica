package gnu.chu.anjelica.despiece;
/**
 *
 * <p>Titulo: ValDespi</p>
 * <p>Descripción: Clase para Valorar y agrupar despieces</p>
 * <p>Copyright: Copyright (c) 2005-2016
 *
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los términos de la Licencia Publica General de GNU según es publicada por
 *  la Free Software Foundation, bien de la versión 2 de dicha Licencia
 *  o bien (según su elección) de cualquier versión posterior.
 *  Este programa se distribuye con la esperanza de que sea útil,
 *  pero SIN NINGUNA GARANTÍA, incluso sin la garantía MERCANTIL implícita
 *  o sin garantizar la CONVENIENCIA PARA UN PROPÓSITO PARTICULAR.
 *  Véase la Licencia Pública General de GNU para más detalles.
 *  Debería haber recibido una copia de la Licencia Pública General junto con este programa.
 *  Si no ha sido así, escriba a la Free Software Foundation, Inc.,
 *  en 675 Mass Ave, Cambridge, MA 02139, EEUU.
 * @version 1.0
 *
 */
import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.almacen.ActualStkPart;
import gnu.chu.anjelica.pad.pdconfig;
import gnu.chu.controles.CTextField;
import gnu.chu.controles.StatusBar;
import gnu.chu.interfaces.ejecutable;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;



public class ValDespi extends ventana {
   boolean swValGrupo=false;
   String msgError;
   double impDocum=0;
   double kgDocum=0;
   PreparedStatement psMvt;
   PreparedStatement psInv;
   private ArrayList<Integer> htAvi=new ArrayList();
   private java.util.Date fecIniSem,fecFinSem;
   private String rangoSemana;
   String deoSerlot;
   int proNumind;
   int deoEjelot;
   DatTrazFrame datTrazFrame;
   Albven aVen;
   boolean swValDesp;
   Date fecInv;
   int rowEdit=0;
   String msgAviso;
//   MvtosAlma mvtosAlm;
   final int NUMDEC_COSTO=4; // Numero de decimales en costo
   private double cantiAcu=0, precAcu=0;
   private boolean ARG_ADMIN=false;
   final static int JTDES_EJER=0;
   final static int JTDES_TIPO=1;
   final static int JTDES_NUMDES=2;
   final static int JTDES_FECDES=3;
   final static int JTDES_KILOS=5;
   final static int JTDES_VAL=6;
   final static int JTDES_PROC=7;
   final static int JTDES_SEL=8;
   final static int JTDES_CER=9;
//   final static int JTDES_LOTE=8;
   final static int JTCAB_NDES=0; // Numero de despiece
   final static int JTCAB_PROCODI=1; // Articulo
    final static int JTCAB_KILOS=3; 
   final static int JTCAB_COSTO=4;
   final static int JTCAB_UNID=5;
   final static int JTCAB_COSUSU=6;
   final static int JTCAB_NL=7;
   final static int JTCAB_LOTE=8;
   
   final static int JTLIN_PROCODI=0;
   final static int JTLIN_UNID=2;
   final static int JTLIN_KILOS=3;
   final static int JTLIN_COSTO=4;
   final static int JTLIN_COSFIN=5;
   final static int JTLIN_COSPOR=6;
   final static int JTLIN_COSBLO=7;
   String s;
   private boolean cancelaBuscaDesp=false;
   boolean jtLinCambio;
   DatosTabla dtAdd;
   DatosTabla dtAux;
   DatosTabla dtDesp;
   private String feulin; 
   
   public ValDespi(EntornoUsuario eu, Principal p)
   {
   this(eu, p, null);
   }

   public ValDespi(EntornoUsuario eu, Principal p,Hashtable ht)
   {
       EU = eu;
       vl = p.panel1;
       jf = p;
       eje = true;

       try
       {
          ponParametros(ht);
          setTitulo("Valoracion/Agrupacion despieces");
         if (jf.gestor.apuntar(this))
            jbInit();
       } catch (Exception ex)
       {
            Logger.getLogger(ValDespi.class.getName()).log(Level.SEVERE, null, ex);
            setErrorInit(true);
       }
   }

   public ValDespi(gnu.chu.anjelica.menu p, EntornoUsuario eu, Hashtable ht) {
        EU = eu;
        vl = p.getLayeredPane();
        eje = false;

        try {
            ponParametros(ht);
           
            setTitulo("Valoracion/Agrupacion despieces");


            jbInit();
        } catch (Exception e) {
            Logger.getLogger(ValDespi.class.getName()).log(Level.SEVERE, null, e);
            setErrorInit(true);
        } 
   }
   private void ponParametros(Hashtable<String,String> ht)
   {
        if (ht != null) {
            if (ht.get("modAdmin") != null) {
                ARG_ADMIN = Boolean.valueOf(ht.get("modAdmin"));
            }
        }
   }
   private void jbInit() throws Exception {
        statusBar = new StatusBar(this);    
        iniciarFrame();
        this.setVersion("2016-12-16" + (ARG_ADMIN ? "(ADMINISTRADOR)" : ""));
       
        initComponents();
        this.setSize(new Dimension(730, 535));
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        
        conecta();
   }
   public static String getNombreClase()
   {
    return "gnu.chu.anjelica.despiece.ValDespi";
   }
   public boolean inTransation()
   {
      return Baceptar.isEnabled();
   }
   
   @Override
   public void iniciarVentana() throws Exception
   {
     tid_codfinE.iniciar(dtStat, this, vl, EU);
     tid_codiniE.iniciar(dtStat, this, vl, EU);
     feulin=ActualStkPart.getFechaUltInv(0,0,null,dtStat);
     if (feulin == null)
        feulin = "01-01-" + EU.ejercicio; // Buscamos desde el principio del año.
     
     eje_numeE .setValorInt(EU.ejercicio);
     dtAdd=new DatosTabla(ctUp);
     dtAux=new DatosTabla(ct);
     dtDesp=new DatosTabla(ct);
     pro_codiE.iniciar(dtAux, this, vl, EU);
     enableEdicion(false);
     grd_numeE.setEnabled(false);
//     jtDesp.setOrdenar(false);
     jtDesp.setButton(KeyEvent.VK_F2, Bir);
     jtDesp.setButton(KeyEvent.VK_F5, BValorar.getBotonAccion());
     jtCab.setButton(KeyEvent.VK_F2, Bir);
     jtLin.setButton(KeyEvent.VK_F2, Bir);
     jtCab.setButton(KeyEvent.VK_F5, BValorar.getBotonAccion());
     jtLin.setButton(KeyEvent.VK_F5, BValorar.getBotonAccion());
     Pcond.setDefButton(Bbuscar.getBotonAccion());
     
     jtCab.setDefButton(Baceptar);
     jtLin.setDefButton(Baceptar);
     fecinfE.setDate(Formatear.sumaDiasDate(Formatear.getDateAct(),-15));
     preparaStatements();
     activarEventos();
   }
   
   private void activarEventos() {
       agrupaC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               grd_numeE.setEnabled(agrupaC.getValor().equals("A"));
            }
       });
       grd_numeE.addFocusListener(new FocusAdapter() {
            @Override
             public void focusLost(FocusEvent e) {
                 if (grd_numeE.getValorInt()==0)
                    try {
                    grd_numeE.setValorInt(utildesp.buscaMaxGrp(dtCon1,eje_numeE.getValorInt(),
                        EU.em_cod, 0));
                } catch (SQLException ex) {
                    Error("Error al poner siguiente numero de grupo",ex);
                } 
             }  
       });
       
       Bir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               if (agrupaC.getValor().equals("A"))
                   return;
               if (!jtDesp.getValBoolean(JTDES_CER))
                            return;
               if (jtDesp.isEnabled())
               {
                   editar(jtDesp.getSelectedRow());
                   return;
               }
               if (jtLin.isEnabled())
                   irJtCab();
               else
                   irJtLin();
            }
        });
       Baceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               guardarValoracion();
            }
        });
        Bcancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               enableEdicion(false);
               jtLinCambio=false;
               try{
                 cargaDespiece(jtDesp.getSelectedRow(), agruCabC.isSelected() );
                } catch (SQLException k)
                {
                    Error("Error al cargar el despiece",k);
                    return;
                }
                jtLinCambio=true;
               jtDesp.setEnabled(true);
               jtDesp.requestFocusSelectedLater();
               mensajeErr("Valoración de despiece CANCELADA");
               mensaje("");
            }
        });
       Bbuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {  
                if (e.getActionCommand().startsWith("Albaran"))
                    buscaAlb();
                else
                    buscarDespieces();
            }
        });
       jtDesp.addListSelectionListener(new ListSelectionListener()
       {
             @Override
             public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() || !jtDesp.isEnabled())
                    return;   
           
                try {
                    jtLinCambio=false;
                    cargaDespiece(jtDesp.getSelectedRow(), agruCabC.isSelected() );
                    jtLinCambio=true;
                } catch (SQLException k)
                {
                    Error("Error al cambiar de despiece",k);
                }
            }
       });
       jtDesp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
              
                if (jtDesp.getSelectedColumn()==JTDES_NUMDES && e.getClickCount()>1)
                {
                      ejecutable prog;
                      if ((prog = jf.gestor.getProceso(MantDesp.getNombreClase())) == null)
                          return;
                      MantDesp cm = (MantDesp) prog;
                      if (cm.inTransation())
                      {
                          msgBox("Mantenimiento Despieces ocupado. No se puede realizar la busqueda");
                          return;
                      }

                      cm.PADQuery();
                      cm.setEjeNume(jtDesp.getValorInt(JTDES_EJER));
                      cm.setDeoCodi(jtDesp.getValString(JTDES_NUMDES));
                      cm.ej_query();
                      jf.gestor.ir(cm);
                      return;
                }
                if (!jtDesp.getValBoolean(JTDES_CER))
                    return;
                if (e.getClickCount()<2)
                {
                    if (jtDesp.getSelectedColumn()==JTDES_SEL)
                    {
                        if ( !jtDesp.getValString(JTDES_TIPO).equals("GO")) 
                        {
                            numRegSelE.setValorInt(numRegSelE.getValorInt()+1*(jtDesp.getValBoolean(JTDES_SEL)?-1:1));
                            jtDesp.setValor(!jtDesp.getValBoolean(JTDES_SEL),JTDES_SEL);
                        }
                    }
                    if (jtDesp.getSelectedColumn()==JTDES_PROC)
                    {
                        if (jtDesp.getValString(JTDES_TIPO).equals("S"))
                        {
                            mensajeErr("Despiece es del grupo activo. Imposible Procesar");
                            return;
                        }
                        boolean proc=!jtDesp.getValBoolean(JTDES_PROC);
                        jtDesp.setValor(proc,JTDES_PROC);
                            s = "UPDATE desporig set deo_incval = '"+(proc?"S":"N")+"'"
                                +  " WHERE eje_nume = " + eje_numeE.getValorInt() 
                                + " AND "+(jtDesp.getValString(JTDES_TIPO).startsWith("G")? "deo_numdes":"deo_codi")+" = "
                                +jtDesp.getValorInt(JTDES_NUMDES );
                        try {
                            int executeUpdate = dtAdd.executeUpdate(s);
                            dtAdd.commit();
                            mensajeErr("Cambiado el estado de procesado");
                            
                        } catch (SQLException ex) {
                            Error("Error al poner Despiece como Procesado ",ex);
                        }
                    }
                    return;
                }
                if (agrupaC.getValor().equals("A"))
                   return;
                editar(jtDesp.getSelectedRowDisab());
            }
       });
       popEspere_BCancelaraddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               cancelaBuscaDesp();
            }
        });
       agruCabC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                 jtLinCambio=false;
                 cargaDespiece(jtDesp.getSelectedRow(), agruCabC.isSelected() );
                 jtLinCambio=true;
                }   catch (SQLException k)
                {
                    Error("Error al cambiar modo de ver despiece",k);
                }
            }
        });
       jtCab.addListSelectionListener(new ListSelectionListener()
       {
             public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() || !jtLinCambio || agruCabC.isSelected()) {
                    return;
                }   
                try {
                    jtLinCambio=false;
                    verDatLin(jtDesp.getValorInt(0),agruCabC.isSelected()? jtDesp.getValorInt(0): jtCab.getValorInt(jtCab.getSelectedRowDisab(),0),
                            agruCabC.isSelected() );
                    jtLinCambio=true;
                } catch (SQLException k)
                {
                    Error("Error al cambiar de despiece",k);
                }
            }
       });
       jtLin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                irJtLin(); 
            }
       });
       jtCab.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
              if (jtDesp.isEnabled() && e.getClickCount()>1)
              {
                mostrarDatosTraz();
                return;
              }
              irJtCab();
            }
       });
       BAgrupar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               if (e.getActionCommand().startsWith("Des"))
                  desagrupar();
               else
                   agrupar();
            }
        });
       BValorar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               if (e.getActionCommand().startsWith("Anular"))
               {
                   if (!Baceptar.isEnabled())
                    anularValoracion();
                   else
                     mensajeErr("No se puede Anular Valoracion si se esta editando");
               }
               else
                   BValorar_ActionPerformed();
            }
        });
       Binvsel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int n=0;n<jtDesp.getRowCount();n++)
                {
                    if (!jtDesp.getValBoolean(n,JTDES_CER))
                            continue;
                    if (e.getActionCommand().startsWith("Inv"))
                    {
                        numRegSelE.setValorInt(numRegSelE.getValorInt()+1*(jtDesp.getValBoolean(n,JTDES_SEL)?-1:1));
                        jtDesp.setValor(!jtDesp.getValBoolean(n,JTDES_SEL),n,JTDES_SEL);
                    }
                    if (e.getActionCommand().startsWith("Sel"))
                    {
                        numRegSelE.setValorInt(numRegSelE.getValorInt()+ (jtDesp.getValBoolean(n,JTDES_SEL)?1:0));
                        jtDesp.setValor(true,n,JTDES_SEL);
                    }
                     if (e.getActionCommand().startsWith("Des"))
                    {
                        numRegSelE.setValorInt(numRegSelE.getValorInt()- (jtDesp.getValBoolean(n,JTDES_SEL)?1:0));
                        jtDesp.setValor(false,n,JTDES_SEL);
                    }
                }
            }
        });
   }
    void mostrarDatosTraz()
  {
      try {
          if (agruCabC.isSelected())
              return;
          s="select * from desorilin where eje_nume="+eje_numeE.getValorInt()+
              " and deo_codi = "+jtCab.getValorInt(jtCab.getSelectedRowDisab(),JTCAB_NDES)+
              " and del_numlin = "+jtCab.getValorInt(jtCab.getSelectedRowDisab(),JTCAB_NL);
          if (! dtStat.select(s))
          {
              msgBox("No encontrada linea de despiece");
              return;
          }
          deoSerlot=dtStat.getString("deo_serlot");
          proNumind=dtStat.getInt("pro_numind");
          deoEjelot=dtStat.getInt("deo_ejelot");
          if (datTrazFrame==null)
          {
              datTrazFrame=new DatTrazFrame(EU,vl,this)
              {
                    @Override
                    public void matar()
                    {
                       salirDatTraza();
                    }
              };
              datTrazFrame.iniciar(dtStat, dtCon1,this,vl,EU);
              vl.add(datTrazFrame);
              datTrazFrame.setLocation(this.getLocation().x, this.getLocation().y + 30);
          }
          new miThread("")
          {
                @Override
                  public void run()
                  {
                     mostrarDatosTraz1();
                  }
          };
      }
      catch (SQLException k)
      {
          Error("Error a mostrar Datos de Trazabilidad",k);
      }
  }
  void mostrarDatosTraz1()
  {
      try {
          msgEspere("Buscando datos trazabilidad");
          popEspere_BCancelarSetEnabled(false);
          datTrazFrame.setDatos(jtCab.getValorInt(jtCab.getSelectedRowDisab(),JTCAB_PROCODI),
                      deoSerlot,
                      deoEjelot,
                      jtCab.getValorInt(jtCab.getSelectedRowDisab(),JTCAB_LOTE),
                      proNumind);
           datTrazFrame.actualizar();
           resetMsgEspere();
           this.setFoco(datTrazFrame);
           this.setEnabled(false);
           datTrazFrame.mostrar();
           this.setFoco(null);
      } catch (SQLException k)
      {
          Error("Error a mostrar Datos de Trazabilidad",k);
      }
  }
  private void salirDatTraza()
  {
    datTrazFrame.setVisible(false);
    this.toFront();
    this.setEnabled(true);
    try
    {
      this.setSelected(true);
    }
    catch (Exception k)
    {}
    
  }
   void agrupar()
   {
       if ( grd_numeE.getValorInt()<=98 )
       {
         msgBox("Grupo deber ser superior a 98");
         return; // Los Grupos empiezan por el 100 (por compatibilidad)
       }
  
       try {
//           s = "select max(deo_fecha) as fecha_max, min(deo_fecha) as fecha_min from desporig"
//                   + " where eje_nume=" + eje_numeE.getValorInt()
//                   + " and deo_numdes = " + grd_numeE.getValorInt();
//           dtStat.select(s);
//           if (dtStat.getDate("fecha_max") != null) {
//               if (Formatear.comparaFechas(deo_fechaE.getDate(), dtStat.getDate("fecha_min")) > 7) {
//                   msgBox("Más de 7 dias diferencia sobre la fecha minima de despiece. Imposible AGRUPAR");
//                   return;
//               }
//               if (Formatear.comparaFechas(dtStat.getDate("fecha_max"), deo_fechaE.getDate()) > 7) {
//                   msgBox("Mas de 7 dias diferencia sobre la fecha Maxima. Imposible AGRUPAR");
//                   return;
//               }
//           }
           int nRow=jtDesp.getRowCount();
           for (int n=0;n<nRow;n++)
           {
             if (jtDesp.getValBoolean(n,JTDES_SEL))
                ponerGrupo(eje_numeE.getValorInt(), 
                        jtDesp.getValString(n,JTDES_TIPO).startsWith("G"),
                        jtDesp.getValorInt(n,JTDES_NUMDES), grd_numeE.getValorInt());
           }
           dtAdd.commit();
       } catch (Exception k) {
           Error("Error al Actualizar Numero Grupo ", k);
           return;
       }
       mensajeErr("Despieces metidos en  Grupo: "+grd_numeE.getValorInt());
   
   }
/**
  * Pone en un grupo a un despiece en particular
  * @param ejeNume Ejercicio
  * @param isGrupo Si estamos tratando un grupo o un despiece
  * @param deoCodi Número de despiece (o grupo)
  * @param grpNume Grupo en el que meterlo.
  * @throws Exception
  */
 void ponerGrupo(int ejeNume,boolean isGrupo,int deoCodi,int grpNume) throws Exception
 { 
   s = "UPDATE desporig set deo_numdes = " + grpNume +
       " WHERE  eje_nume = " + ejeNume +
       " AND "+(isGrupo?"deo_numdes":"deo_codi") +" = "+ deoCodi;
   dtAdd.executeUpdate(s);
 
   s = "UPDATE  v_despfin SET  def_numdes = " + grpNume +
       " WHERE eje_nume = " + ejeNume +
       " AND "+(isGrupo?"def_numdes":"deo_codi") +" = "+ deoCodi;
    dtAdd.executeUpdate(s);

   s = "SELECT * FROM grupdesp WHERE eje_nume = " + ejeNume +
       " AND grd_nume = " + grpNume;
   if (!dtAdd.select(s, true))
   { // Crea el grupo de despiece, si no existe.
     s="SELECT deo_fecha FROM desporig where  eje_nume = " + ejeNume +
       " AND deo_codi = " + deoCodi;
     dtStat.select(s);
     java.util.Date deoFecha=dtStat.getDate("deo_fecha");
     dtAdd.addNew("grupdesp");
     dtAdd.setDato("emp_codi", EU.em_cod);
     dtAdd.setDato("eje_nume", ejeNume);
     dtAdd.setDato("grd_nume", grpNume);
     dtAdd.setDato("grd_serie","V");
     dtAdd.setDato("grd_kilo", 0);
     dtAdd.setDato("grd_unid", 1);
     dtAdd.setDato("grd_prmeco", 0);
     dtAdd.setDato("grd_block","N");
     dtAdd.setDato("prv_codi", pdconfig.getPrvDespiece(EU.em_cod,dtStat));
     dtAdd.setDato("grd_incval", "N");
     dtAdd.setDato("grd_valor","N");
     dtAdd.setDato("grd_fecha",deoFecha);
     dtAdd.update(stUp);
   }
//   else
//   {
//     dtAdd.edit(dtAdd.getCondWhere());
////     dtAdd.setDato("grd_incval", grd_incvalE.getSelecion());
//     dtAdd.update(stUp);
//   }
 }
    void desagrupar() 
    {
        try {
            int nRow = jtDesp.getRowCount();
            for (int n = 0; n < nRow; n++) {
                if (jtDesp.getValBoolean(n,JTDES_SEL))
                    ponerGrupo(eje_numeE.getValorInt(),
                        jtDesp.getValString(n, JTDES_TIPO).startsWith("G"),
                        jtDesp.getValorInt(n, JTDES_NUMDES), 0);
            }
            dtAdd.commit();
        } catch (Exception k) {
            Error("Error al Actualizar Numero Grupo ", k);
            return;
        }

        mensajeErr("Despieces Sacados de sus grupos ");
   }
   void anularValoracion()
   {
       if (jtDesp.isVacio() || numRegSelE.getValorInt()==0)
           return;
       new miThread("")
       {
            @Override
           public void run()
           {
               anularValoracion1();
           }
       };
   }
   /**
    * Anula Valoracion despiece 
    * @param dt Datostabal
    * @param ejeNume Ejercicio
    * @param tipoDesp Tipo Despiece: 'D' Despiece Normal. 'G' Grupo
    * @param deoCodi Numero despiece o Grupo
    */
   public static void  anulaValoracionDesp(DatosTabla dt,int ejeNume,String tipoDesp,int deoCodi) throws SQLException
   {
       
       String s = "UPDATE v_despfin SET def_prcost = 0 "+
                " WHERE eje_nume = " + ejeNume +
                " AND " +
                (tipoDesp.startsWith("G")? "def_numdes":"deo_codi")+
                " = "+ deoCodi;
            dt.executeUpdate(s);

            s = "UPDATE desorilin set deo_prcost = 0 "+
              " WHERE  eje_nume = " + ejeNume +
              " AND deo_codi " +
             (tipoDesp.startsWith("G")? "  in (select deo_codi from desporig where "
                    + " eje_nume=desorilin.eje_nume and deo_numdes= "+deoCodi+")"
                    :" = "+ deoCodi);
            dt.executeUpdate(s);

            s="UPDATE  desporig SET deo_valor='N' where eje_nume = " + ejeNume +
                 " AND " +
                 (tipoDesp.startsWith("G")? "deo_numdes":"deo_codi")+
                  " = "+ deoCodi;
            dt.executeUpdate(s);
   }
   void anularValoracion1()
   {
       try {
       
        msgEspere("Anulando valoracion de despieces");
        popEspere_BCancelarSetEnabled(false);
        for (int n=0;n<jtDesp.getRowCount();n++)
        {
            if (! jtDesp.getValBoolean(n,JTDES_SEL))
                continue;
            actualizaMsg("Anulando valoracion de despiece: "+jtDesp.getValorInt(n,JTDES_NUMDES),false);
            anulaValoracionDesp(dtAdd,eje_numeE.getValorInt(),jtDesp.getValString(n,JTDES_TIPO),jtDesp.getValorInt(n,JTDES_NUMDES));
            jtDesp.setValor(false,n,JTDES_VAL);
        }
        dtAdd.commit();
        SwingUtilities.invokeAndWait(new Thread(){
                @Override
            public void run()
            {
                 resetMsgEspere();
                 mensajeErr("Quitada Valoración a despieces selecionados");
            }
        });
      } catch (SQLException k)
      {
          Error("Error al anular valoración",k);
      }
      catch (Exception k)
      {
          Error("Error al anular valoración",k);
      }
   }
   void irJtLin()
   {
        if (jtLin.isEnabled() || jtDesp.isEnabled())
            return;
        jtCab.salirGrid();
        jtCab.setEnabled(false);
        sumTotalesCab();
        jtLin.setEnabled(true);
        jtLin.requestFocusInicioLater();
   }
   
   void irJtCab()
   {
        if (jtCab.isEnabled() || jtDesp.isEnabled())
            return;
        jtLin.salirGrid();
        jtLin.setEnabled(false);
        recalcCostoLin();
        jtCab.setEnabled(true);
        jtCab.requestFocusInicioLater(); 
   }
   /**
    * Edita el despiece de la linea mandado como parametro
    * @param row 
    */
    void editar(int row) {
        if (jtDesp.isVacio() || ! jtDesp.isEnabled())
            return;
        rowEdit = row;
//        if (jtDesp.getValString(rowEdit,JTDES_TIPO).equals("D"))
//        { // Si el despiece esta metido en un grupo No permito valorarlo
//            try {
//                s="select * from v_despori where eje_nume= "+jtDesp.getValorInt(rowEdit,JTDES_EJER)+
//                    " and deo_codi = "+jtDesp.getValorInt(rowEdit,JTDES_NUMDES);
//                dtStat.select(s);
//                if (dtStat.getInt("deo_numdes")!=0)
//                {
//                    msgBox("Este despiece pertenece al grupo: "+dtStat.getInt("deo_numdes")+" Imposible valorar fuera del grupo");
//                    jtDesp.requestFocusLater(rowEdit,jtDesp.getSelectedColumn());
//                    return;
//                }
//            } catch (SQLException k)
//            {
//                Error("Error al comprobar grupo de Despiece",k);
//            }
//        }
        jtDesp.setEnabled(false);
        jtCab.initTransation();
        jtLin.initTransation();
        new miThread("") {
            @Override
            public void run() {
                edita1();
            }
        }; 
    }
   
   void guardarValoracion() 
   {
       int nRow = jtLin.getRowCount();

       sumTotalesCab();
       recalcCostoLin();
       if (jtLin.isEnabled()) {
           jtLin.salirGrid();
       } else {
           jtCab.salirGrid();
       }

       for (int n = 0; n < nRow; n++) {
           if (jtLin.getValorDec(n, JTLIN_UNID) < 0) {
               msgBox("En linea: " + (n + 1) + " Unidades esta en negativo");
               jtLin.requestFocusInicio();
               return;
           }
           if (jtLin.getValorDec(n, JTLIN_KILOS) < 0) {
               msgBox("En linea: " + (n + 1) + " Kilos esta en negativo");
               jtLin.requestFocusInicio();
               return;
           }

           if (jtLin.getValorDec(n, JTLIN_COSFIN) < 0) {
               msgBox("En linea: " + (n + 1) + " Importe esta en negativo");
               jtLin.requestFocusInicio();
               return;
           }
       }

       int nLiAf = 0;
       try {
           for (int n = 0; n < nRow; n++) {
               s = "UPDATE v_despfin SET def_prcost = " + jtLin.getValorDec(n, JTLIN_COSFIN)
                       + " WHERE eje_nume = " + eje_numeE.getValorInt()
                       + " AND "
                       + (jtDesp.getValString(rowEdit, JTDES_TIPO).startsWith("G") ? "def_numdes" : "deo_codi")
                       + " = " + jtDesp.getValorInt(rowEdit, JTDES_NUMDES)
                       + " AND pro_codi = " + jtLin.getValorInt(n, 0);
//        debug("update: "+s);
              nLiAf= dtAdd.executeUpdate(s);
           }

           nRow = jtCab.getRowCount();
           for (int n = 0; n < nRow; n++) {
               s = "UPDATE desorilin set deo_prcost = " + jtCab.getValorDec(n, JTCAB_COSTO)
                       + " WHERE  eje_nume = " + eje_numeE.getValorInt()
                       + " and deo_codi " + (jtDesp.getValString(JTDES_TIPO).startsWith("G") ? " in (select deo_codi from desporig where "
                       + " eje_nume=desorilin.eje_nume and deo_numdes= " + jtDesp.getValorInt(JTDES_NUMDES) + ")"
                       : " = " + jtDesp.getValorInt(JTDES_NUMDES))
                       + " and pro_codi = " + jtCab.getValorInt(n, JTCAB_PROCODI);
               dtAdd.executeUpdate(s);
           }

           s = "select * from desporig where eje_nume = " + eje_numeE.getValorInt()
                   + " AND "
                   + (jtDesp.getValString(rowEdit, JTDES_TIPO).startsWith("G") ? "deo_numdes" : "deo_codi")
                   + " = " + jtDesp.getValorInt(rowEdit, JTDES_NUMDES);
           dtAdd.select(s, true);
           dtAdd.edit();
           dtAdd.setDato("deo_valor", "S");
           dtAdd.setDato("deo_fecval", "current_timestamp");
           dtAdd.setDato("deo_usuval", EU.usuario);
           nLiAf = dtAdd.update();

           if (nLiAf == 0) {
               msgBox("No modificada ninguna cabecera de origen");
           }

           dtAdd.commit();
       } catch (Exception ex) {
           Error("Error al Actualizar Datos", ex);
           return;
       }
  
       enableEdicion(false);
       jtDesp.setEnabled(true);
       jtDesp.setValor(true, JTDES_VAL);
       jtDesp.requestFocusSelectedLater();
       mensajeErr("Valoración de despiece Realizada");
       mensaje("");  
   }
  void buscaAlb()
  {
      if (agrupaC.getValor().equals("V"))
      {
          mensajeErr("Opcion solo disponible en modo AGRUPAR");
          agrupaC.requestFocus();
          return;
      }
      if (grd_numeE.getValorInt()<=100)
      {
          mensajeErr("Grupo de despiece no es valido");
          grd_numeE.requestFocus();
          return;
      }
      try
      {
        if (aVen==null)
        {
          aVen = new Albven()
          {
            @Override
            public void matar()
            {
               salBuscaAlb();
            }
          };
          aVen.setLocation(25, 25);

          vl.add(aVen,new Integer(0));
          aVen.iniciar(dtCon1,dtStat, EU);
        }
        aVen.setVisible(true);
        this.setEnabled(false);
        this.setFoco(aVen);
        aVen.resetTexto();
        aVen.setGrupo(grd_numeE.getValorInt());
        aVen.setEjercicio(eje_numeE.getValorInt());
        aVen.setEmpCodi(EU.em_cod);
        aVen.getAvc_numeE().requestFocus();
      } catch (Exception j)
      {
        Error("Error al lanzar agrupacion por albaranes",j);
      }

  }
  void  salBuscaAlb()
  {
    setFoco(null);
    this.setEnabled(true);
    aVen.setVisible(false);
    eje_numeE.requestFocus();
    try  {
        this.setSelected(true);
    } catch (Exception k) {}
    if (!aVen.isAceptado())
        return;
    if (aVen.getNumDespieces().isEmpty())
        return;
    try {
       
    int nDesp;
    for (int n=0;n<aVen.getNumDespieces().size();n++)
    {
      nDesp=(Integer) aVen.getNumDespieces().get(n);
      ponerGrupo(eje_numeE.getValorInt(),false,
               nDesp,grd_numeE.getValorInt());
    }
    } catch (Exception k)
    {
        Error("Error al poner grupos sobre el albaran selecionado",k);
    }

    
  }
   void BValorar_ActionPerformed()
   {
       try {
           if (jtDesp.isVacio()) 
               return;
           
           if (Baceptar.isEnabled()) 
           {
               fecInv = Formatear.getDate(feulin, "dd-MM-yyyy");
               Date fecDesp = jtDesp.getValDate(rowEdit, JTDES_FECDES);
               if (Formatear.comparaFechas(fecInv, fecDesp) > 0) {
                   fecInv = ActualStkPart.getDateUltInv(jtDesp.getValDate(rowEdit, JTDES_FECDES), dtStat);
               }
               if (jtCab.isEnabled()) 
               {
                   precAcu=pdprvades.getPrecioOrigen(dtStat, jtCab.getValorInt(JTCAB_PROCODI), fecIniSem);
                   if (precAcu<=0)
                    precAcu= getPrecioMedioDesp(jtCab.getValorInt(JTCAB_PROCODI),
                       jtDesp.getValString(JTDES_TIPO).equals("G"),
                       jtDesp.getValorInt(JTDES_EJER),
                       jtDesp.getValorInt(JTDES_NUMDES), "D",true);
               }
               else
               {
                   precAcu=pdprvades.getPrecioFinal(dtStat, jtCab.getValorInt(JTCAB_PROCODI), fecIniSem);
                   if (precAcu<=0)
                       precAcu= getPrecioMedioDesp(jtLin.getValorInt(JTLIN_PROCODI),
                        jtDesp.getValString(JTDES_TIPO).equals("G"),
                        jtDesp.getValorInt(JTDES_EJER),
                        jtDesp.getValorInt(JTDES_NUMDES), "d",false);
                }
                   
                  
//                   res = buscaStock(fecDesp, fecInv,
//                           jtCab.getValorInt(JTCAB_PROCODI), jtDesp.getValorInt(JTDES_NUMDES));
//               } else {
//                   res = buscaStock(fecDesp, fecInv,
//                           jtLin.getValorInt(JTLIN_PROCODI), jtDesp.getValorInt(JTDES_NUMDES));
//               }
               
               
               if (precAcu!=0) {
//                   if (mvtosAlm.getEntradaSinValor()) {
//                      msgExplica("Error al Valorar","Despieces:\n "+mvtosAlm.getMsgDesp()+"\n"+
//                              "Compras:\n "+mvtosAlm.getMsgCompra()+"\nProducto tiene entradas anteriores sin valorar."
//                              + " Cancelada valoracion" );
//                      return;
//                   }
                   if (jtCab.isEnabled()) {
                       jtCab.setValor(precAcu, JTCAB_COSTO);
                       deo_prcogrE.setValorDec(precAcu);
                   } else {
                       jtLin.setValor(precAcu, JTLIN_COSTO);
                       def_prcostE.setValorDec(precAcu);
                   }
               }
               return;
           }
           if (numRegSelE.getValorInt()!=0)
           {
              new miThread("")
              {
                    @Override
                  public void run()
                  {
                        swValGrupo=true;
                        jtDesp.tableView.setVisible(false);
                        valorarDespSel();
                        jtDesp.tableView.setVisible(true);
                         swValGrupo=false;
                  }
              };
           } 
       } catch (ParseException | SQLException k) {
           Error("Error al valorar producto de despiece", k);
       }
   }
   void calcFechaInv(Date fecDesp) throws Exception
   {
        fecInv=Formatear.getDate(feulin,"dd-MM-yyyy");
        
        if (Formatear.comparaFechas(fecInv, fecDesp)>0)
          fecInv= ActualStkPart.getDateUltInv(jtDesp.getValDate(rowEdit,JTDES_FECDES),dtStat);
   }
   
   void calcFechaCostos(Date fecDesp) throws Exception
   {
     GregorianCalendar gc=new GregorianCalendar();
     
     gc.setTime(fecDesp);
     int diaSemana=gc.get(GregorianCalendar.DAY_OF_WEEK); 
     fecIniSem=Formatear.sumaDiasDate(fecDesp, (diaSemana-2)*-1); // Empieza la semana el Lunes
     fecFinSem=Formatear.sumaDiasDate(fecDesp, 8-diaSemana); // Termina el sabado.
     rangoSemana = "BETWEEN {d '"+Formatear.getFechaDB(fecIniSem)+"'} and {d '"+
         Formatear.getFechaDB(fecFinSem)+"'}";   
   }
   /**
    * Valora dspieces selecionados.
    */
   void valorarDespSel() 
   {
        try {
            cancelaBuscaDesp=false;
            HashMap<Integer,Double>  htPro=new HashMap();
            HashMap<Integer,Double>  htFin=new HashMap();
            ArrayList<String> msgs=new ArrayList();
            int nRegVal = 0;
            double impEnt,impCostLin;
            boolean swErr,isGrupo;
            
            msgAviso = "";
            msgEspere("Espere, por favor. Valorando despieces");
            popEspere_BCancelarSetEnabled(true);
            for (int n = 0; n < jtDesp.getRowCount(); n++) 
            {
                if (cancelaBuscaDesp)
                {
                    msgBox("Valoración de despieces .. CANCELADA");
                    break;
                }
                if (!jtDesp.getValBoolean(n, JTDES_SEL)) 
                    continue;
                
                msgEspere("Espere, por favor. Valorando despiece: " + jtDesp.getValorInt(n, JTDES_NUMDES));
                isGrupo=jtDesp.getValString(n, JTDES_TIPO).startsWith("G");
                s= "SELECT distinct(pro_codi) FROM v_despori o" +
                    " WHERE o.eje_nume = " + jtDesp.getValorInt(n,JTDES_EJER) +
                    " AND "+ (isGrupo?"o.deo_numdes = ":"o.deo_codi = ") + jtDesp.getValorInt(n,JTDES_NUMDES);
              
                if (!dtDesp.select(s))
                {
                    msgAviso += "\nDespiece: " + jtDesp.getValorInt(n, JTDES_NUMDES)+" SIN LINEAS ORIGEN";
                    continue;
                }
              
                
                impEnt=0;
                swErr=false;
                htPro.clear();
                htFin.clear();
                do
                {
                    precAcu=getPrecioMedioDesp(dtDesp.getInt("pro_codi"), isGrupo,jtDesp.getValorInt(n,JTDES_EJER) ,
                        jtDesp.getValorInt(n,JTDES_NUMDES), "D",true);
                    if (msgError!=null)
                    {                        
                        if (msgs.indexOf(msgError)<0)
                            msgs.add(msgError);
                    }
                    htPro.put(dtDesp.getInt("pro_codi"),Formatear.redondea( precAcu,NUMDEC_COSTO));
                    impEnt+=impDocum;
                    if (precAcu==0)
                    {
                          msgAviso += "\n Valor de Costo NO encontrado en origen para Producto: "
                                + dtDesp.getInt("pro_codi");
                    }
                } while (dtDesp.next());
                if (swErr)
                    continue;
                s= "SELECT distinct(pro_codi) FROM v_despsal " +
                    " WHERE eje_nume = " + jtDesp.getValorInt(n,JTDES_EJER) +
                    " AND "+ (isGrupo?" deo_numdes = ":" deo_codi = ") + jtDesp.getValorInt(n,JTDES_NUMDES);
              
                if (!dtDesp.select(s))
                {
                    msgAviso += "\nDespiece: " + jtDesp.getValorInt(n, JTDES_NUMDES)+" SIN LINEAS FINALES";
                    continue;
                }
                impCostLin=0;
                do
                {
                    pro_codiE.getNombArt(dtDesp.getInt("pro_codi"));
                    if (! pro_codiE.isVendible())
                    {
                        htFin.put(dtDesp.getInt("pro_codi"),(double)0);
                        continue;
                    }
                    precAcu=getPrecioMedioDesp(dtDesp.getInt("pro_codi"), isGrupo,jtDesp.getValorInt(n,JTDES_EJER) ,
                        jtDesp.getValorInt(n,JTDES_NUMDES), "d",false);
                    if (msgError!=null)
                    {                        
                        if (msgs.indexOf(msgError)<0)
                            msgs.add(msgError);
                    }
                    impCostLin+=impDocum;
                    htFin.put(dtDesp.getInt("pro_codi"),Formatear.redondea( precAcu,NUMDEC_COSTO));
                    if (precAcu==0)
                    {
                        swErr=true; 
                        msgAviso += "\n Valor de Costo NO encontrado en final para Producto: "
                                + dtDesp.getInt("pro_codi");
                    }
                } while (dtDesp.next());
                if (swErr)
                    continue;               
                s=getStrSqlOri(eje_numeE.getValorInt(), isGrupo, 
                        jtDesp.getValorInt(n,JTDES_NUMDES),true);
                dtDesp.select(s);
                double porc;
                do
                {
                  precAcu=htPro.get(dtDesp.getInt("pro_codi"));
                  s = "UPDATE desorilin set deo_prcost = " + precAcu
                       + " WHERE  eje_nume = " + eje_numeE.getValorInt()
                       + " and deo_codi " + (isGrupo ? " in (select deo_codi from desporig where "
                       + " eje_nume="+ eje_numeE.getValorInt()+" and deo_numdes= " + jtDesp.getValorInt(n,JTDES_NUMDES) + ")"
                       : " = " + jtDesp.getValorInt(n,JTDES_NUMDES))
                       + " and pro_codi = " + dtDesp.getInt("pro_codi");
                  dtAdd.executeUpdate(s);    
                } while (dtDesp.next());
                s=getStrSqlFin(eje_numeE.getValorInt(),true,  
                        jtDesp.getValorInt(n,JTDES_NUMDES),isGrupo);
                if (!dtDesp.select(s))
                    msgBox("Articulo "+ dtDesp.getInt("pro_codi",true)+" no encontrados en despiece: "+
                        jtDesp.getValorInt(n,JTDES_NUMDES) );
                else
                {
                    do
                    {
                        if (htFin.get(dtDesp.getInt("pro_codi",true))== null)
                        {
                            msgBox("No encontrado precio de articulo "+dtDesp.getInt("pro_codi",true)+
                                 " de despiece: "+jtDesp.getValorInt(n,JTDES_NUMDES));
                            continue;
                        }   
                        precAcu = htFin.get(dtDesp.getInt("pro_codi",true));
                        if (precAcu > 0)
                        {
                            porc = dtDesp.getDouble("def_kilos") * precAcu / impCostLin;
                            precAcu = impEnt * porc;
                            precAcu = precAcu / dtDesp.getDouble("def_kilos");
                            precAcu = Formatear.redondea(precAcu, NUMDEC_COSTO);
                        }
                        s = "UPDATE v_despfin SET def_prcost = " + precAcu
                            + " WHERE eje_nume = " + eje_numeE.getValorInt()
                            + " AND "
                            + (isGrupo ? "def_numdes" : "deo_codi")
                            + " = " + jtDesp.getValorInt(n, JTDES_NUMDES)
                            + " AND pro_codi = " + dtDesp.getInt("pro_codi");
//        debug("update: "+s);
                        dtAdd.executeUpdate(s);
                    } while (dtDesp.next());
                }
                 s = "select * from desporig where eje_nume = " + eje_numeE.getValorInt()
                   + " AND "
                   + (isGrupo ? "deo_numdes" : "deo_codi")
                   + " = " + jtDesp.getValorInt(n, JTDES_NUMDES);
                 dtAdd.select(s, true);
                 dtAdd.edit();
                 dtAdd.setDato("deo_valor", "S");
                 dtAdd.setDato("deo_fecval", "current_timestamp");
                 dtAdd.setDato("deo_usuval", EU.usuario);
                 dtAdd.update();
                 dtAdd.commit();
                 jtDesp.setValor(true,n,JTDES_VAL);
                 jtDesp.setValor(false, n, JTDES_SEL);
                 numRegSelE.setValorInt(numRegSelE.getValorInt() - 1);
                 nRegVal++;
            }   
            if (msgs.size()>0)
            {
                s="";
                for (int n=0;n<msgs.size();n++)
                    s+=msgs.get(n)+"\n";
                mensajes.mensajeExplica("Avisos", s);
            }
            mensajeErr("Valorados " + nRegVal + " Despieces");
            SwingUtilities.invokeLater(new Thread()
            {
                @Override
                public void run()
                {
                    if (!msgAviso.equals(""))
                       mensajes.mensajeExplica("Errores al valorar despieces", msgAviso);
                    try {
                        cargaDespiece(jtDesp.getSelectedRow(), agruCabC.isSelected() );
                    } catch (SQLException k) 
                    {
                        Error("Error al cargar despieces",k);
                    }
                    resetMsgEspere();
                }
            });
           
        } catch (Exception k) {
            Error("Error en valoracion masiva de despieces", k);
        }
    }
   
//   double getValorProd(HashMap<Integer,Double>  htPro,Date fecDesp,Date fecInv, int proCodi) throws Exception
//   {
//        Double d = htPro.get(proCodi);
//        if (d == null)
//        {
//            boolean res = buscaStock(fecDesp, fecInv,
//                    proCodi,0);
//            if (res) {
//                if (mvtosAlm.getEntradaSinValor()) {
//                    msgAviso += "\nEncontradas entradas anteriores sin valorar de producto: " +proCodi;
//                    precAcu = 0;
//                }
//            } else {
//                msgAviso += "\n Valor de Costo NO encontrado para Producto: "
//                        + proCodi;
//                precAcu = 0;
//            }
//            htPro.put(proCodi, Formatear.redondea( precAcu,4));
//        }
//        else 
//            precAcu = d;
//        return precAcu;
//    }
   private void edita1()
   {
    try {
        if (! jtDesp.getValBoolean(rowEdit,JTDES_VAL))
             calcCostos();
        else
        {
            for (int n=0;n<jtLin.getRowCount();n++)
            {
                jtLin.setValor(jtLin.getValorDec(n,JTLIN_COSTO),n,JTLIN_COSFIN);
//                jtLin.setValor(true,n,JTLIN_COSBLO);
                if (jtLin.getValorDec(n,JTLIN_COSTO)==0)
                    jtLin.setValor(0,n,JTLIN_COSPOR);
                else
                    jtLin.setValor(Formatear.redondea(((jtLin.getValorDec(n,JTLIN_KILOS)*
                        jtLin.getValorDec(n,JTLIN_COSTO))/impFinE.getValorDec())*100,2), 
                        n,JTLIN_COSPOR);
            }
        }
        jtCab.commitTransation();
        jtLin.commitTransation();
     } catch (Exception k)
    {
      Error("Error al buscar Precio Medio ",k);
      return;
    }
    
    enableEdicion(true);
    jtLin.setEnabled(false);
    jtCab.requestFocusInicio();
    mensaje("Valorando despiece...");
  }
  void enableEdicion(boolean enab)
  {
    //BValorar.setEnabled(!enab);
    Pcond.setEnabled(!enab);
    Binvsel.setEnabled(!enab);
    jtDesp.setEnabled(!enab);
    jtCab.setEnabled(enab);
    jtLin.setEnabled(enab);  
    Baceptar.setEnabled(enab);
    Bcancelar.setEnabled(enab);
  }
  /**
   * Calcula Costos
   * @return
   * @throws Exception 
   */
  private String calcCostos() throws Exception
  {
        msgEspere("Calculando costos\n");
        popEspere_BCancelarSetEnabled(false);
       
        Date fecDesp=jtDesp.getValDate(rowEdit,JTDES_FECDES);
        calcFechaInv(fecDesp);
        calcFechaCostos(fecDesp);
        int nRow = jtCab.getRowCount();
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(fecDesp);
        
        setMensajePopEspere("Calculando costos cabecera",false);
        String entSinValor="";
        int proCodi;
        double costo;
        for (int n = 0; n < nRow; n++)
        {
            proCodi=jtCab.getValorInt(n, JTCAB_PROCODI);
            setMensajePopEspere("Calculando costos cabecera\n Producto: "+proCodi,false);
            pro_codiE.getNombArt( proCodi);
            if (! pro_codiE.isVendible())
            {
               jtCab.setValor(0, n, JTCAB_COSTO);
               continue;
            }
            
            if (pro_codiE.getMantenerCosto())
            {
                costo=-1;
                s="select deo_prcost,deo_tiempo from v_despori "+
                     " where pro_codi = "+proCodi
                        + " and deo_prcost > 0 "
                        + " and deo_fecha "+ rangoSemana+" order by deo_fecha desc";
                if (dtStat.select(s)) 
                    costo=dtStat.getDouble("deo_prcost");
                if (costo<0)
                    costo=pdprvades.getPrecioOrigen(dtStat, proCodi, fecIniSem);
                if (costo<0)
                {
                    s="\nNO Encontrados DESPIECES anteriores ni costo Fijo para producto: "+proCodi;
                    entSinValor+=s;
                    jtCab.setValor(0, n,JTCAB_COSTO);
                    continue;
                }
                jtCab.setValor(costo, n,JTCAB_COSTO);
                continue;
            }
            costo=pdprvades.getPrecioOrigen(dtStat, proCodi, fecIniSem);
            if (costo>0)
            {
                jtCab.setValor(costo, n,JTCAB_COSTO);
                continue;
            } 
            precAcu=getPrecioMedioDesp(proCodi,
                jtDesp.getValString(JTDES_TIPO).equals("G"),
                jtDesp.getValorInt(JTDES_EJER),                  
                jtDesp.getValorInt(JTDES_NUMDES),"D",false);                
            if (precAcu!=0)
                jtCab.setValor(precAcu, n, JTCAB_COSTO);
            else
            {
              s="\n Valor de Costo NO encontrado para Producto: " +
                     proCodi;
              entSinValor+=s;
//              actualizaMsg(s,true);
              jtCab.setValor(0, n, JTCAB_COSTO);
            }
        }
        sumTotalesCab();
        entSinValor+=calcCostoLin();
        if (! entSinValor.equals(""))
          mensajes.mensajeExplica("Incidencias al Valorar Productos: ", entSinValor);
        resetMsgEspere();
        return entSinValor;
  }
  
  void sumTotalesCab() 
  {
    int nRow = jtCab.getRowCount();
    int unid=0;
    double totCosto = 0;
    double kilos=0;
    for (int n = 0; n < nRow; n++)
    {
      unid+=jtCab.getValorDec(n,JTCAB_UNID);
      kilos+=jtCab.getValorDec(n,JTCAB_KILOS);
      totCosto+=jtCab.getValorDec(n,JTCAB_KILOS)*jtCab.getValorDec(n,JTCAB_COSTO);
    }
    uniCabE.setValorDec(unid);
    kilosCabE.setValorDec(kilos);
    impCabE.setValorDec(totCosto);
  }
  
/**
 * Calcula costos lineas finales
 * @return
 * @throws Exception 
 */
  String calcCostoLin() throws Exception
  {
      actualizaMsg("Calculando costos Linea",false);
      // Valoro costos de Productos.
      int nRow = jtLin.getRowCount();
//      double totCosto = 0;
      String entSinValor="";
      double costo;
      int proCodi;
      for (int n = 0; n < nRow; n++)
      {    
        proCodi=jtLin.getValorInt(n, JTLIN_PROCODI);
        actualizaMsg("Calculando costos Linea\nProducto: "+proCodi,false);
        pro_codiE.getNombArt( proCodi);
        if (! pro_codiE.isVendible())
        {
               jtLin.setValor(0, n, JTLIN_COSTO);
               continue;
        }
        costo=-1;
//        String strError="";
        if (pro_codiE.getMantenerCosto())
        { // Busco costo en la semana.
            s="select def_prcost from v_despsal where pro_codi = "+proCodi
                    + " and def_prcost > 0"
                    + " and deo_fecha "+ rangoSemana +" order by deo_fecha desc";
            if (dtStat.select(s)) 
               costo=dtStat.getDouble("def_prcost");
            if (costo<0)
               costo=pdprvades.getPrecioFinal(dtStat, proCodi, fecIniSem);
            if (costo<0)
            {
                s="\nNO Encontrados DESPIECES anteriores para producto: "+proCodi;
                entSinValor+=s;
                jtLin.setValor("0", n,JTLIN_COSTO);
                continue;
            }
            jtLin.setValor(costo, n,JTLIN_COSTO);
            continue;
        }
        costo=pdprvades.getPrecioFinal(dtStat, proCodi, fecIniSem);
        if (costo>0)
        {
            jtLin.setValor(costo, n,JTLIN_COSTO);
            continue;
        }
         precAcu=getPrecioMedioDesp(proCodi,
                    jtDesp.getValString(JTDES_TIPO).equals("G"),
                    jtDesp.getValorInt(JTDES_EJER),
                    jtDesp.getValorInt(JTDES_NUMDES),"d",false);               
        if (precAcu!=0)
        {        
            jtLin.setValor("" + precAcu, n,JTLIN_COSTO );
        }
        else
        {
          s="\n Valor de Costo NO encontrado para producto: "+jtLin.getValorInt(n, JTLIN_PROCODI);
          entSinValor+=s;
          jtLin.setValor(0,n,JTLIN_COSTO);
        }
      }
      recalcCostoLin();
      return entSinValor;
  }
  /**
   * Recalcular Costo de Lineas (Despiece de Salida)
   */
  void recalcCostoLin()
  {
    int nRow=jtLin.getRowCount();
    
    double totFin=0;
    
    double totCosto=0;
//    boolean swDesBloq=false;
    for (int n = 0; n < nRow; n++)
      totCosto += (jtLin.getValorDec(n, JTLIN_COSTO) * jtLin.getValorDec(n, JTLIN_KILOS));
    
    // Busco lo que importan sobre el total (el %)
    if (totCosto==0)
      totCosto=impCabE.getValorDec();
    double costo=0;
    if (nRow==1)
    {
      jtLin.setValor("100",0,JTLIN_COSPOR);
    }
    else
    {
      for (int n = 0; n < nRow; n++)
      {
        costo = jtLin.getValorDec(n, JTLIN_KILOS) * jtLin.getValorDec(n, JTLIN_COSTO);

        if (totCosto == 0)
          costo = 0;
        else
          costo = costo / totCosto * 100;
        if (costo <=-100)
            costo=-99.99;
        if (costo>100)
            costo=100;
        jtLin.setValor("" + costo, n, JTLIN_COSPOR);
      }
    }
//      jtLin.setValor(""+(jtLin.getValorDec(n,3)*jtLin.getValorDec(n,4)/totCosto*100),n,6);
    totCosto=impCabE.getValorDec();
    if (totCosto==0)
      return;
    
    double totLin=0;
    totFin=0;
    double totFi1=0;
    boolean swAjus=false; // Indica si hay algun precio bloqueado
    for (int n=0;n<nRow;n++)
    {
      costo=totCosto * jtLin.getValorDec(n, JTLIN_COSPOR ) / 100;
      costo=costo / jtLin.getValorDec(n,JTLIN_KILOS);
      if (jtLin.getValBoolean(n,JTLIN_COSBLO))
      {
        swAjus=true;
        jtLin.setValor(""+jtLin.getValorDec(n,JTLIN_COSTO), n, JTLIN_COSFIN);
        totFin+=jtLin.getValorDec(n,JTLIN_COSFIN)*jtLin.getValorDec(n,JTLIN_KILOS);
        continue;
      }

      if (costo >= 0)
      {
        totFi1+=costo*jtLin.getValorDec(n,JTLIN_KILOS);
        totFin+=costo*jtLin.getValorDec(n,JTLIN_KILOS);
        jtLin.setValor("" + Formatear.redondea(costo,NUMDEC_COSTO), n, JTLIN_COSFIN);
        totLin+= jtLin.getValorDec(n,JTLIN_COSFIN)*jtLin.getValorDec(n,JTLIN_KILOS);
      }
      else
        jtLin.setValor("0", n, JTLIN_COSFIN);
    }
    if (swAjus) // Hay algun precio bloqueado.
    {
      double porc1;
      double dif=totCosto-totFin;
      for (int n = 0; n < nRow; n++)
      {
        if (!jtLin.getValBoolean(n, JTLIN_COSBLO))
        {
          totLin = jtLin.getValorDec(n, JTLIN_KILOS) * jtLin.getValorDec(n, JTLIN_COSFIN);
          porc1 = totLin / totFi1;
          jtLin.setValor("" + (totLin + (porc1 * dif))/jtLin.getValorDec(n, JTLIN_KILOS), n, JTLIN_COSFIN);
        }
      }
      totLin=0;
      for (int n= 0; n < nRow; n++)
        totLin+=jtLin.getValorDec(n, JTLIN_KILOS) * jtLin.getValorDec(n, JTLIN_COSFIN);

    }
    impFinE.setValorDec(totLin);
  }
   /**
  * Busca Stock sobre un producto. Comprueba si el producto tiene control
  * individual, para buscar por numero de individuos o no.
  *
  * @param fecStock Fecha de Stock, fecha a la que queremos el valor
  *                 . SI es nulo es a fecha actual.
  * @param fecInic Fecha Inicio desde la que buscar Mvtos. Tecnicamente la fecha
  * del ultimo stock fisico. Si es NULO 1-1-(Año curso)
  * @param proCodi Codigo de Producto .. No puede ser nulo.
  * @param ejeNume Ejercicio de producto .. si es 0 se pondra el actual.
  * @param empCodi Empresa ... si es 0 se buscara en la activa.
  * @param serie   Serie .. si es null se buscara en todas
  * @param numLote si es 0 .. se buscara en todas.
  * @throws Exception en caso de cualquier error
  *
  * @return boolean true si encontro datos para tratar.
  */
// boolean buscaStock(java.util.Date fecStock, java.util.Date fecInic, int proCodi,
//                     int numLote) throws Exception
// {
//     cantiAcu=0;
//     precAcu=0;
//     if (fecStock == null)
//        fecStock = new java.util.Date(System.currentTimeMillis());
//
//     String fecStockStr=Formatear.getFechaVer(fecStock);
//     String fecInicStr=Formatear.getFechaVer(fecInic);
//     if (mvtosAlm==null)
//     {
//        mvtosAlm=new MvtosAlma();        
//        mvtosAlm.setIncUltFechaInv(false);
//        mvtosAlm.setIgnDespSinValor(true);
//        mvtosAlm.setEntornoUsuario(EU);
//        mvtosAlm.setIncIniFechaInv(true);
//        mvtosAlm.setIgnComprasSinValor(true);
////        mvtosAlm.setIncluyeHora(true);
//     }
//     mvtosAlm.setLote(0);
//
//     mvtosAlm.resetAcumulados();
//     mvtosAlm.resetMensajes();
//     s=mvtosAlm.getSqlMvt(fecInicStr, fecStockStr, proCodi );
//    
//     boolean ret=mvtosAlm.calculaMvtos( dtAdd,null, s,null,null,null,proCodi);
//     if (mvtosAlm.getCompraSinValor() && !htAvi.contains(proCodi))
//     {
//         msgBox("Encontradas compras sin precio, para producto: "+proCodi+
//             mvtosAlm.getMsgCompra()+" Revise costos");
//         htAvi.add(proCodi);
//     }
//     
//     if (ret)
//     {
//        cantiAcu=mvtosAlm.getKilosStock();
//        precAcu=mvtosAlm.getPrecioStock();
//     }
//      return ret;
//   }
   String getStrSqlOri(int ejeNume,boolean isGrupo,int numdes,boolean agrupa)
   {
      if (agrupa)
      {
        return "SELECT 0 as deo_codi,"+
             " pro_codi,sum(deo_kilos) as kilos,sum(deo_kilos*deo_prcost) as costo," +
             " count(*) as nreg,max(deo_fecha) as deo_fecha,"
             + " sum(deo_kilos*deo_preusu) as costoUsu " +
             " FROM v_despori o" +
             " WHERE o.eje_nume = " + ejeNume +
             " AND "+ (isGrupo?"o.deo_numdes = " + numdes: "o.deo_codi = "+numdes) +
             " group by pro_codi ";
      }
      else
      {
         return "SELECT deo_codi,"+
            " pro_codi,deo_kilos as kilos,deo_kilos*deo_prcost as costo, "+
            " deo_kilos * deo_preusu as costoUsu, " +
             " 1 as nreg,deo_fecha,del_numlin,pro_lote" +
            " FROM v_despori o" +
            " WHERE o.eje_nume = " + ejeNume +
            " AND "+ (isGrupo?"o.deo_numdes = " + numdes: "o.deo_codi = "+numdes)+
            " order by deo_codi ";
      }
   }
   
   void cargaDespiece(int row,boolean agrupa) throws SQLException
   {
     int ejeNume=jtDesp.getValorInt(row,JTDES_EJER);
     int numdes=jtDesp.getValorInt(row,JTDES_NUMDES);
     boolean isGrupo=false;
     try {
        isGrupo=jtDesp.getValString(row,JTDES_TIPO).equals("G");
     } catch (NullPointerException k)
     {
        enviaMailError("Error al leer grupo en cargadespiece (valdespi) "+
            getCurrentStackTrace()+"\n"+
            jtDesp.getMsgError()); 
     }
     s=getStrSqlOri(ejeNume,isGrupo,numdes,agrupa);
  
     jtCab.removeAllDatos();
     if (! dtCon1.select(s))
     {
         msgBox("No encontrados datos de cabecera para este despiece");
         jtLin.removeAllDatos();
         return;
     }
//   debug(dtCon1.getStrSelect());
     double kilos=0;
     double costo=0;
     int nReg=0;
     ArrayList<ArrayList> lista=new ArrayList();
     do
     {
       ArrayList v = new ArrayList();
     
       v.add(""+dtCon1.getInt("deo_codi"));
       v.add(dtCon1.getString("pro_Codi")); // 0
       v.add(pro_codiE.getNombArt(dtCon1.getString("pro_codi")));
       v.add(dtCon1.getString("kilos")); // 2
       v.add("" +
                    (dtCon1.getDouble("costo") / dtCon1.getDouble("kilos"))); // 3
       v.add(dtCon1.getString("nreg"));
       v.add("" +(dtCon1.getDouble("costoUsu") / dtCon1.getDouble("kilos"))); // 3
       if (agrupa)
       {
           v.add("");
           v.add("");
       }
       else
       {
           v.add(dtCon1.getDouble("del_numlin"));
           v.add(dtCon1.getDouble("pro_lote"));
       }
       lista.add(v);
       kilos+=dtCon1.getDouble("kilos");
       costo+=dtCon1.getDouble("costo");
       nReg+=dtCon1.getInt("nreg");
     }  while (dtCon1.next());
     jtCab.setDatos(lista);
     impCabE.setValorDec(costo);
     kilosCabE.setValorDec(kilos);
     uniCabE.setValorDec(nReg);
     jtCab.requestFocus(0, 0);
    
     verDatLin(ejeNume,agrupa?numdes:jtCab.getValorInt(0,JTCAB_NDES),agrupa );
     jtLinCambio=true;
   }
   void verDatLin(int ejeNume,int numDesp,boolean agrupa) throws SQLException
   {
       verDatLin(ejeNume,numDesp,jtDesp.getSelectedRow(),agrupa);
   }
 void verDatLin(int ejeNume,int numDesp,int numLin,boolean agrupa) throws SQLException
 {
     boolean isGrupo=jtDesp.getValString(numLin,JTDES_TIPO).startsWith("G");
    verDatLi1(getStrSqlFin(ejeNume,agrupa,numDesp,isGrupo));
 }
 String getStrSqlFin(int ejeNume,boolean agrupa, int numDesp,boolean isGrupo)
 {
      return " select f.pro_codi as pro_codi,sum(f.def_numpie) as def_numpie, " +
         " sum(f.def_kilos) as def_kilos ,sum(f.def_prcost*f.def_kilos) as def_prcost, " +
         " avg(def_preusu) as def_preusu "+
         " from v_despfin as f "+
         " where  f.def_kilos <> 0 " +
         " and eje_nume = " + ejeNume +
         " AND "+(! agrupa? "f.deo_codi = "+numDesp:
             (isGrupo?" f.def_numdes = " + numDesp: "f.deo_codi = "+numDesp)) +
         " group by f.pro_codi " +
         " order by f.pro_codi ";
 }
 
 void preparaStatements() throws SQLException
 {     
    s= "SELECT mvt_time,mvt_tipo, mvt_canti,mvt_prec,mvt_tipdoc,mvt_empcod,mvt_ejedoc,mvt_serdoc,mvt_numdoc "+
             " from mvtosalm  "+
             " where pro_codi =  ?" +
             " AND mvt_time::date >= ?  "+           
            " and mvt_time <= ? "+
            " order by mvt_time";             
    psMvt=dtCon1.getPreparedStatement(s);
    s= "SELECT sum(rgs_kilos) as kilos, sum(rgs_kilos*rgs_prregu) as importe "+
             " from v_inventar  "+
             " where pro_codi =  ?" +
             " AND rgs_fecha::date = ?  ";
    psInv=dtCon1.getPreparedStatement(s);
 }
 
 private double getPrecioMedioDesp(int proCodi,boolean isGrupo, int ejerc,int deoCodi, String tipoDoc,boolean swOrig) throws SQLException
 {
   
   impDocum=0;
   kgDocum=0;
   if (isGrupo)
   {
    s="select deo_codi from desporig where eje_nume= "+ejerc+
        " and deo_numdes ="+deoCodi;
    if (!dtCon1.select(s))
        return 0;
    ArrayList<Integer> grupos=new ArrayList();
    int nEle=0;
    do
    {
        grupos.add(dtCon1.getInt("deo_codi"));
        nEle++;
    } while (dtCon1.next());
    for (int n=0;n<nEle;n++)
    {
       getPrecioMedioDesp(proCodi,ejerc,grupos.get(n), tipoDoc,swOrig);
    }  
    return kgDocum==0?0:impDocum/kgDocum;
   }
  
   return getPrecioMedioDesp(proCodi,ejerc,deoCodi, tipoDoc,swOrig);
 }
 
 /**
  * Busca el precio Medio  de Entrada
  * @param proCodi
  * @param ejerc
  * @param deoCodi
  * @param tipoDoc 'd' Entrada a Almacen (v_despfin). 'D' Salida Almacen (desorilin)
  * @return
  * @throws SQLException 
  */
 double getPrecioMedioDesp(int proCodi, int ejerc,int deoCodi, String tipoDoc,boolean swOrig) throws SQLException
 {
     if (tipoDoc.equals("D") )
     {
         s="select max(deo_tiempo) as maxTiempo, min (deo_tiempo) as minTiempo from desorilin where deo_codi="+deoCodi+
             " and eje_nume="+ejerc+
             " and pro_codi = "+proCodi;
     }
     else
     {
         s="select max(def_tiempo) as maxTiempo, min (def_tiempo) as minTiempo from v_despfin where deo_codi="+deoCodi+
             " and eje_nume="+ejerc+
             " and pro_codi = "+proCodi;         
     }
     dtStat.select(s);
     Date fecha=dtStat.getTimeStamp("minTiempo");
     if (fecha==null)
         return 0;
     fecInv = ActualStkPart.getDateUltInv(fecha, dtCon1);
     if (fecInv==null)
         return 0;
     return getPrecioMedioEntrada(proCodi, new java.sql.Date(fecInv.getTime()),
               dtStat.getTimeStamp("maxTiempo"),EU.em_cod,ejerc,"D",deoCodi,tipoDoc );
 }
 double getPrecioMedioEntrada(int proCodi,java.sql.Date fechaInv,java.sql.Timestamp timeSupMvt,int empCodi,
     int ejerc,String serie,int deoCodi, String tipoDoc) throws SQLException
 {
    msgError=null;
    psInv.setInt(1, proCodi);
    psInv.setDate(2, fechaInv);
    ResultSet rs=psInv.executeQuery();
    rs.next();
    double kilos=rs.getDouble("kilos");
    double precioMedio=rs.getDouble("kilos")==0?0:rs.getDouble("importe")/rs.getDouble("kilos");
    double importe;
   
    boolean isDocumActual;
    psMvt.setInt(1, proCodi);
    psMvt.setDate(2, fechaInv);
    psMvt.setTimestamp(3, timeSupMvt);
    rs=psMvt.executeQuery();
    if (rs.next())
    {
        do
        {
            isDocumActual=false;
          
            if (rs.getInt("mvt_empcod") == empCodi
                && rs.getInt("mvt_ejedoc") == ejerc
                && rs.getString("mvt_serdoc").equals(serie)
                && rs.getInt("mvt_numdoc") == deoCodi)
            { // Despiece a Valorar.
                isDocumActual = true;
                if (rs.getString("mvt_tipdoc").equals(tipoDoc))
                {
                    kgDocum += rs.getDouble("mvt_canti");
                    impDocum += rs.getDouble("mvt_canti") * precioMedio ;
                }
            }
          
            if (rs.getString("mvt_tipo").equals("E"))
            {
                importe=kilos*precioMedio;
                if (kilos<-0.01)
                {
                    if (msgError==null)
                        msgError="Producto : "+proCodi+" Stock negativo en entrada de fecha: "+
                            Formatear.getFecha(rs.getTimestamp("mvt_time"),"dd-MM-yyyy HH:mm");
                    kilos=0;
                }
                kilos+=rs.getDouble("mvt_canti");
                if (rs.getDouble("mvt_prec")!=0 && ! isDocumActual)
                {
                    importe+= rs.getDouble("mvt_canti")* 
                        rs.getDouble("mvt_prec") ;
                    precioMedio=kilos==0?0:importe/kilos;
                }
            }
            else
                kilos-=rs.getDouble("mvt_canti");
        } while (rs.next());
    }
    if (msgError!=null && !swValGrupo)
        msgBox(msgError);
    return kgDocum==0?0:impDocum/kgDocum;
 }
 void verDatLi1(String sql) throws SQLException
 {
     double kilos = 0, costo = 0;
     int nLin = 0;
     int codArt;

     jtLin.removeAllDatos();
     if (!dtAdd.select(sql)) {
         kilosFinE.setValorDec(0);
         impFinE.setValorDec(0);
         uniFinE.setText("0");
         return;
     }
     
     do {
         ArrayList v = new ArrayList();
         codArt = dtAdd.getInt("pro_codi", true);

         v.add("" + codArt);
         v.add(pro_codiE.getNombArt("" + codArt));
         v.add(dtAdd.getString("def_numpie"));
         v.add(dtAdd.getString("def_kilos"));
         v.add("" + (dtAdd.getDouble("def_prcost", true) / dtAdd.getDouble("def_kilos", true)));
         v.add("");
         v.add("");
         v.add(false);
         v.add(dtAdd.getString("def_preusu", true));
         jtLin.addLinea(v);
      
         kilos += dtAdd.getDouble("def_kilos", true);
         costo += dtAdd.getDouble("def_prcost", true);
         nLin++;
     } while (dtAdd.next());
     kilosFinE.setValorDec(kilos);
     impFinE.setValorDec(costo);
     uniFinE.setText("" + nLin);
   }
   private void cancelaBuscaDesp()
   {
       if (swValDesp)
        actualizaMsg("Cancelando Valoracion despieces... espere, por favor",false);   
       else
        actualizaMsg("Cancelando busqueda... espere, por favor",false);
       popEspere_BCancelarSetEnabled(false);
       cancelaBuscaDesp=true;
   }
   
   public void setDespiece(int numDesp)
   {
       grd_valorE.setValor("*");
       grupoC.setValor("D");
       try
       {
           fecinfE.setDate(Formatear.getDate("01-01-"+EU.ejercicio,"dd-MM-yyyy"));
       } catch (ParseException ex)
       {
           Logger.getLogger(ValDespi.class.getName()).log(Level.SEVERE, null, ex);
       }
       deo_codiE.setValorInt(numDesp);
   }
   public void buscarDespieces()
   {
       if (eje_numeE.isNull())
       {
           mensajeErr("Introduzca Ejercicio");
           eje_numeE.requestFocus();
           return;
       }
       if (fecinfE.isNull() || fecinfE.getError())
       {
           mensajeErr("Introduzca una fecha Inferior valida");
           fecinfE.requestFocus();
           return;
       }
        if (fecsupE.getError())
       {
           mensajeErr("Introduzca una fecha Superior valida");
           fecsupE.requestFocus();
           return;
       }
       if (agrupaC.getValor().equals("A") && grd_numeE.getValorInt()==0)
       {
           mensajeErr("Introduzca Numero de Grupo");
           grd_numeE.requestFocus();
           return;
       }
       new miThread("")
       {
            @Override
           public void run()
           {
               buscaDesp1();
           }
       };
   }
   
   private void buscaDesp1()
   {
       msgEspere("Buscando Despieces ...");
       cancelaBuscaDesp=false;
       popEspere_BCancelarSetEnabled(true);
       s="";
       Bir.setEnabled(agrupaC.getValor().equals("V"));
       BValorar.setEnabled(agrupaC.getValor().equals("V"));
       BAgrupar.setEnabled(!agrupaC.getValor().equals("V"));
       if (agrupaC.getValor().equals("V"))
        jtDesp.setButton(KeyEvent.VK_F5, BValorar.getBotonAccion());
       else
        jtDesp.setButton(KeyEvent.VK_F5, BAgrupar.getBotonAccion());  
       if (agrupaC.getValor().equals("A"))
       {
         s="select 1 as orden,eje_nume, deo_codi, -1 as deo_numdes,deo_fecha as fecha from  desporig as d "
            + " where  d.eje_nume = "+eje_numeE.getValorInt()
            + " and deo_numdes="+grd_numeE.getValorInt()
            + " UNION ALL "
            + "select 0 as orden,eje_nume, 0 as deo_codi,  grd_nume as deo_numdes,grd_fecha as fecha  from grupdesp as g "
            + " where g.eje_nume = "+eje_numeE.getValorInt()
            + " and g.grd_nume = "+grd_numeE.getValorInt()
            + " UNION ALL ";
       }
       String s1="select 2 as orden,eje_nume, deo_codi, 0 as deo_numdes,"
               + " deo_fecha as fecha  from desporig as d "
               + " where d.deo_fecha>=TO_DATE('"+fecinfE.getText()+"','dd-MM-yy')"
               +(fecsupE.isNull()?"":" and d.deo_fecha<=TO_DATE('"+fecsupE.getText()+"','dd-MM-yy')" )
               + " and d.eje_nume = "+eje_numeE.getValorInt()
               + (tid_codiniE.isNull()?"":" and d.tid_codi "+
               (tid_codfinE.isNull()?"":">")+
               "= "+tid_codiniE.getValorInt())
              + (tid_codfinE.isNull()?"":" and d.tid_codi<= "+tid_codfinE.getValorInt())
               + (deo_codiE.getValorInt()==0?"":" and d.eje_nume = "+eje_numeE.getValorInt()
               +" and "+(grupoC.getValor().equals("G")?" d.deo_numdes= ":" d.deo_codi = ")+deo_codiE.getValorInt())
               + (opCerradoC.getValor().equals("T")?"":" and deo_block = '"+opCerradoC.getValor()+"' ")
               + (grd_valorE.getValor().equals("*")?"":" and d.deo_valor = '"+grd_valorE.getValor()+"'");
       s+= s1+" and deo_numdes = 0 ";
       if (opVerGrupo.isSelected())
        s+= " UNION ALL "
               + "select 2 as orden,eje_nume, 0 as deo_codi,  grd_nume as deo_numdes,"
               + " grd_fecha as fecha from grupdesp as g "
               + " where exists( "+s1+" and deo_numdes>0 "
               + (agrupaC.getValor().equals("A")?" and deo_numdes != "+grd_numeE.getValorInt():"")
               + " and g.eje_nume=d.eje_nume and g.grd_nume=d.deo_numdes ) ";
       s+= " order by 1,5";
//       System.out.println(s);
       try {
         jtDesp.setEnabled(false);
         jtDesp.removeAllDatos();
         if (! dtCon1.select(s))
         {
           mensajeErr("No encontrados registros para estas condiciones");
           jtCab.removeAllDatos();
           jtLin.removeAllDatos();
           resetMsgEspere();
           return;
         }
   
         PreparedStatement ps1,ps2,ps3,ps4;
         ResultSet rs;
         ps1=ct.prepareStatement("select sum(deo_kilos) as deo_kilos from v_despori as d  "+
                 " where eje_nume= ? and deo_codi=? ");
         ps2=ct.prepareStatement("select sum(deo_kilos) as deo_kilos from v_despori as d  "+
                 " where eje_nume= ? and deo_numdes=? ");
         ps3=ct.prepareStatement("select d.*,tid_nomb from desporig as d left join tipodesp on tipodesp.tid_codi = d.tid_codi  "+
                 " where eje_nume= ? and deo_codi=? ");
         ps4=ct.prepareStatement("select d.*,tid_nomb from desporig as d left join tipodesp on tipodesp.tid_codi = d.tid_codi  "+
                 " where eje_nume= ? and deo_numdes= ? order by deo_codi");
         
         double deoKilos;
         int nRegSel=0;
         boolean sel;
         ArrayList<ArrayList> lista=new ArrayList();
         do
         {
             if (cancelaBuscaDesp)
             {
                 mensajeErr("Cancelada busqueda de despieces");
                 jtDesp.removeAllDatos();
                 jtDesp.panelG.setVisible(true);
                 resetMsgEspere();
                 return;
             }
             ArrayList v=new ArrayList();
             v.add(dtCon1.getInt("eje_nume")); // 0 Ejercicio despiece
             switch (dtCon1.getInt("orden"))
             {
                 case 2:
                    v.add(dtCon1.getInt("deo_codi")==0?"G":"D");
                     break;
                 case 1:
                    v.add("S");
                    break; 
                 case 0:
                    v.add("GO");
                    break; 
             }
             
             v.add(dtCon1.getInt("deo_codi")==0?dtCon1.getInt("deo_numdes"):dtCon1.getInt("deo_codi")); // 1 numero despiece
             if (dtCon1.getInt("deo_codi")!=0)
             {
//                  System.out.println("Usando ps1");
                 ps1.setInt(1, dtCon1.getInt("eje_nume"));
                 ps1.setInt(2, dtCon1.getInt("deo_codi"));
                 rs=ps1.executeQuery();
             }
             else
             {
//                  System.out.println("Usando ps2");
                 ps2.setInt(1, dtCon1.getInt("eje_nume"));
                 ps2.setInt(2, dtCon1.getInt("deo_numdes"));
                 rs=ps2.executeQuery();
             }
             rs.next();
             deoKilos=rs.getDouble("deo_kilos");
             if (dtCon1.getInt("deo_codi")!=0)
             {
//                 System.out.println("Usando ps3");
                 ps3.setInt(1, dtCon1.getInt("eje_nume"));
                 ps3.setInt(2, dtCon1.getInt("deo_codi"));
                 rs=ps3.executeQuery();
             }
             else
             {
//                 System.out.println("Usando ps4");
                 ps4.setInt(1, dtCon1.getInt("eje_nume"));
                 ps4.setInt(2, dtCon1.getInt("deo_numdes"));
                 rs=ps4.executeQuery();
             }
             rs.next();
             if (deoKilos==0)
                 continue;
             v.add(dtCon1.getDate("fecha"));
             v.add(rs.getInt("tid_codi")+(rs.getString("tid_nomb")==null?"":" - "+rs.getString("tid_nomb")));
             v.add(deoKilos);
             v.add(rs.getString("deo_valor"));
             v.add(rs.getString("deo_incval").equals("S"));
             
                
             if (dtCon1.getInt("orden")<=1)
                sel=dtCon1.getInt("orden")==1;
             else
                sel=agrupaC.getValor().equals("A")?false:rs.getString("deo_valor").equals("N");
             sel=rs.getString("deo_block").equals("N")?sel:false;
             if (sel)
                  nRegSel++;
             v.add(sel);
             v.add(rs.getString("deo_block").equals("N"));
             lista.add(v);
         } while (dtCon1.next());
         if (lista.isEmpty())
         {
           mensajeErr("No encontrados registros para estas condiciones");
           jtCab.removeAllDatos();
           jtLin.removeAllDatos();
           resetMsgEspere();
           return;  
         }
         jtDesp.setDatos(lista);
         numRegSelE.setValorDec(nRegSel);
         
         resetMsgEspere();
         jtDesp.requestFocusInicio();
         jtDesp.setEnabled(true);
         cargaDespiece(0,agruCabC.isSelected());
         mensajeErr("Busqueda de despieces, terminada");
       } catch (SQLException k )
       {
           Error("Error al buscar despieces",k);
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

        pro_codiE = new gnu.chu.camposdb.proPanel();
        deo_prcogrE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---,--9.9999");
        def_preusuE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---,--9.9999");
        def_prcostE = new gnu.chu.controles.CTextField(Types.DECIMAL,"---,--9.9999");
        def_prebloC = new gnu.chu.controles.CCheckBox();
        Pprinc = new gnu.chu.controles.CPanel();
        Pcond = new gnu.chu.controles.CPanel();
        cLabel1 = new gnu.chu.controles.CLabel();
        fecinfE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yy");
        cLabel2 = new gnu.chu.controles.CLabel();
        eje_numeE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###9");
        deo_codiE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#####9");
        cLabel4 = new gnu.chu.controles.CLabel();
        grd_valorE = new gnu.chu.controles.CComboBox();
        tid_codfinE = new gnu.chu.camposdb.tidCodi2();
        grupoC = new gnu.chu.controles.CComboBox();
        agrupaC = new gnu.chu.controles.CComboBox();
        cLabel3 = new gnu.chu.controles.CLabel();
        grd_numeE = new gnu.chu.controles.CTextField(Types.DECIMAL,"#####9");
        cLabel12 = new gnu.chu.controles.CLabel();
        Bbuscar = new gnu.chu.controles.CButtonMenu();
        opVerGrupo = new gnu.chu.controles.CCheckBox();
        fecsupE = new gnu.chu.controles.CTextField(Types.DATE,"dd-MM-yy");
        cLabel13 = new gnu.chu.controles.CLabel();
        opCerradoC = new gnu.chu.controles.CComboBox();
        tid_codiniE = new gnu.chu.camposdb.tidCodi2();
        cLabel14 = new gnu.chu.controles.CLabel();
        jtDesp = new gnu.chu.controles.Cgrid(10);
        jtCab = new gnu.chu.controles.CGridEditable(9)
        {
            @Override

            public void afterCambiaLinea()
            {
                try
                {
                    sumTotalesCab();
                }catch (Exception k)
                {
                    Error("Error al Calcular Costos de Cabecera",k);
                }
            }
        } ;
        jtLin = new gnu.chu.controles.CGridEditable(9){
            @Override
            public void afterCambiaLinea()
            {
                recalcCostoLin();
                jtLin.actualizarGrid();
            }
        };
        Ppie = new gnu.chu.controles.CPanel();
        Bcancelar = new gnu.chu.controles.CButton(Iconos.getImageIcon("cancel"));
        PtotOri = new gnu.chu.controles.CPanel();
        cLabel5 = new gnu.chu.controles.CLabel();
        kilosCabE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##,##9.99");
        cLabel6 = new gnu.chu.controles.CLabel();
        uniCabE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9");
        cLabel7 = new gnu.chu.controles.CLabel();
        impCabE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###,##9.99");
        agruCabC = new gnu.chu.controles.CCheckBox();
        PtotFin = new gnu.chu.controles.CPanel();
        cLabel8 = new gnu.chu.controles.CLabel();
        kilosFinE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##,##9.99");
        cLabel9 = new gnu.chu.controles.CLabel();
        uniFinE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9");
        cLabel10 = new gnu.chu.controles.CLabel();
        impFinE = new gnu.chu.controles.CTextField(Types.DECIMAL,"###,##9.99");
        cLabel11 = new gnu.chu.controles.CLabel();
        numRegSelE = new gnu.chu.controles.CTextField(Types.DECIMAL,"##9");
        BValorar = new gnu.chu.controles.CButtonMenu();
        Baceptar = new gnu.chu.controles.CButton(Iconos.getImageIcon("check"));
        Bir = new gnu.chu.controles.CButton(Iconos.getImageIcon("reload"));
        Binvsel = new gnu.chu.controles.CButtonMenu(Iconos.getImageIcon("filter"));
        BAgrupar = new gnu.chu.controles.CButtonMenu();

        def_prebloC.setText("cCheckBox1");

        setMaximumSize(new java.awt.Dimension(636, 319));
        setMinimumSize(new java.awt.Dimension(636, 319));

        Pprinc.setMinimumSize(new java.awt.Dimension(630, 292));
        Pprinc.setLayout(new java.awt.GridBagLayout());

        Pcond.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        Pcond.setMaximumSize(new java.awt.Dimension(590, 65));
        Pcond.setMinimumSize(new java.awt.Dimension(590, 65));
        Pcond.setPreferredSize(new java.awt.Dimension(590, 65));
        Pcond.setQuery(true);
        Pcond.setLayout(null);

        cLabel1.setText("Ejercicio");
        Pcond.add(cLabel1);
        cLabel1.setBounds(10, 2, 60, 17);
        Pcond.add(fecinfE);
        fecinfE.setBounds(70, 20, 60, 17);

        cLabel2.setText("Tipo");
        Pcond.add(cLabel2);
        cLabel2.setBounds(10, 40, 40, 15);
        Pcond.add(eje_numeE);
        eje_numeE.setBounds(70, 2, 32, 17);

        deo_codiE.setFocusTraversalPolicyProvider(true);
        Pcond.add(deo_codiE);
        deo_codiE.setBounds(360, 20, 51, 17);

        cLabel4.setText("Valorado");
        Pcond.add(cLabel4);
        cLabel4.setBounds(320, 2, 58, 17);

        grd_valorE.addItem("No", "N");
        grd_valorE.addItem("Si", "S");
        grd_valorE.addItem("**", "*");
        Pcond.add(grd_valorE);
        grd_valorE.setBounds(380, 2, 45, 17);

        tid_codfinE.setAncTexto(40);
        Pcond.add(tid_codfinE);
        tid_codfinE.setBounds(270, 40, 200, 17);

        grupoC.addItem("Despiece", "D");
        grupoC.addItem("Grupo","G");
        Pcond.add(grupoC);
        grupoC.setBounds(260, 20, 90, 17);

        agrupaC.addItem("Valorar","V");
        agrupaC.addItem("Agrupar", "A");
        Pcond.add(agrupaC);
        agrupaC.setBounds(110, 2, 100, 17);

        cLabel3.setText("A");
        Pcond.add(cLabel3);
        cLabel3.setBounds(250, 40, 20, 17);
        Pcond.add(grd_numeE);
        grd_numeE.setBounds(260, 2, 51, 17);

        cLabel12.setText("Grupo");
        Pcond.add(cLabel12);
        cLabel12.setBounds(220, 2, 40, 17);

        Bbuscar.setText("Buscar (F4)");
        Bbuscar.addMenu("Despieces");
        Bbuscar.addMenu("Albaran de Venta");
        Pcond.add(Bbuscar);
        Bbuscar.setBounds(470, 40, 120, 24);

        opVerGrupo.setSelected(true);
        opVerGrupo.setText("Ver Grupos");
        opVerGrupo.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        Pcond.add(opVerGrupo);
        opVerGrupo.setBounds(430, 2, 90, 17);
        Pcond.add(fecsupE);
        fecsupE.setBounds(170, 20, 60, 17);

        cLabel13.setText("De Fecha");
        Pcond.add(cLabel13);
        cLabel13.setBounds(10, 20, 60, 17);

        opCerradoC.addItem("Cerrado", "N");
        opCerradoC.addItem("Abierto", "S");
        opCerradoC.addItem("Todos", "T");
        Pcond.add(opCerradoC);
        opCerradoC.setBounds(420, 20, 100, 17);

        tid_codiniE.setAncTexto(40);
        Pcond.add(tid_codiniE);
        tid_codiniE.setBounds(40, 40, 200, 17);

        cLabel14.setText("A");
        Pcond.add(cLabel14);
        cLabel14.setBounds(140, 20, 20, 17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        Pprinc.add(Pcond, gridBagConstraints);

        {Vector v=new Vector();
            v.add("Ejer"); // 0 Ejercicio despiece
            v.add("N/G"); // 1 Tipo Despice (Normal/Grupo)
            v.add("N.Desp"); // 2 Numero despiece
            v.add("Fecha"); // 3 Fecha despiece
            v.add("Tipo Despiece"); // 4 Nombre Tipo Desp.
            v.add("Kilos"); // 5 Kilos Desp (entrada)
            v.add("Val"); // 6 Valorado
            v.add("Proc"); // 7 Procesado
            v.add("Sel"); // / 8 selecionado
            v.add("Cer"); // 9 Cerrado
            jtDesp.setCabecera(v);
        }
        jtDesp.setAlinearColumna(new int[]{2,1,2,1,0,2,1,1,1,1});
        jtDesp.setAnchoColumna(new int[]{50,40,70,80,200,70,40,40,30,20});
        jtDesp.setFormatoColumna(JTDES_FECDES, "dd-MM-yy");
        jtDesp.setFormatoColumna(JTDES_KILOS , "##,##9.9");
        jtDesp.setFormatoColumna(JTDES_VAL, "BSN");
        jtDesp.setFormatoColumna(JTDES_PROC, "BSN");
        jtDesp.setFormatoColumna(JTDES_SEL, "BSN");
        jtDesp.setFormatoColumna(JTDES_CER, "BSN");
        jtDesp.setAjustarGrid(true);
        jtDesp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtDesp.setMaximumSize(new java.awt.Dimension(100, 60));
        jtDesp.setMinimumSize(new java.awt.Dimension(100, 60));

        javax.swing.GroupLayout jtDespLayout = new javax.swing.GroupLayout(jtDesp);
        jtDesp.setLayout(jtDespLayout);
        jtDespLayout.setHorizontalGroup(
            jtDespLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 628, Short.MAX_VALUE)
        );
        jtDespLayout.setVerticalGroup(
            jtDespLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 58, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Pprinc.add(jtDesp, gridBagConstraints);

        jtCab.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtCab.setBuscarVisible(false);
        jtCab.setMaximumSize(new java.awt.Dimension(100, 40));
        jtCab.setMinimumSize(new java.awt.Dimension(100, 40));

        javax.swing.GroupLayout jtCabLayout = new javax.swing.GroupLayout(jtCab);
        jtCab.setLayout(jtCabLayout);
        jtCabLayout.setHorizontalGroup(
            jtCabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 628, Short.MAX_VALUE)
        );
        jtCabLayout.setVerticalGroup(
            jtCabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 44, Short.MAX_VALUE)
        );

        try {
            Vector v = new Vector();
            v.addElement("N.Des"); // 0
            v.addElement("Producto"); // 1
            v.addElement("Descripcion"); //2
            v.addElement("Kilos"); // 3
            v.addElement("Costo"); // 4
            v.addElement("Unid"); // 5
            v.addElement("Costo Usu"); // 6
            v.addElement("NL"); // 7
            v.addElement("Lote");
            jtCab.setCabecera(v);
            jtCab.setAnchoColumna(new int[]
                {38,47, 170, 56, 56,40,70,30,50});
            jtCab.setAlinearColumna(new int[]
                {2,2, 0, 2,2,2,2,2,2});
            jtCab.setFormatoColumna(3, "###,##9.99");
            jtCab.setFormatoColumna(4, "---,--9.9999");
            jtCab.setFormatoColumna(5, "###9");
            jtCab.setFormatoColumna(6, "---,--9.9999");
            jtCab.setFormatoColumna(7, "##9");
            jtCab.setFormatoColumna(8, "####9");

            jtCab.setAjustarGrid(true);
            Vector v1=new Vector();

            CTextField[] tfCab = new CTextField[10];
            for (int n=0;n<9;n++)
            {
                tfCab[n]=new CTextField();
                tfCab[n].setEnabled(false);
            }

            v1.addElement(tfCab[0]);
            v1.addElement(tfCab[1]);
            v1.addElement(tfCab[2]);
            v1.addElement(tfCab[3]);
            v1.addElement(deo_prcogrE);
            v1.addElement(tfCab[4]);
            v1.addElement(tfCab[5]);
            v1.addElement(tfCab[7]);
            v1.addElement(tfCab[8]);
            jtCab.setCampos(v1);
            jtCab.setCanInsertLinea(false);
            jtCab.setCanDeleteLinea(false);
        } catch (Exception k)
        {
            Error("Error al configurar el grid",k);
            return;
        }

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Pprinc.add(jtCab, gridBagConstraints);

        jtLin.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtLin.setBuscarVisible(false);
        jtLin.setMaximumSize(new java.awt.Dimension(100, 40));
        jtLin.setMinimumSize(new java.awt.Dimension(100, 40));
        try
        {
            Vector v = new Vector();
            v.addElement("Producto"); // 0
            v.addElement("Descripcion"); // 1
            v.addElement("Unid"); // 2
            v.addElement("Cantidad"); // 3
            v.addElement("Costo Cal"); // 4
            v.addElement("Costo Fin"); // 5
            v.addElement("% Costo"); // 6
            v.addElement("Bloq"); // 7
            v.addElement("Costo Usu"); // 8
            jtLin.setCabecera(v);
            jtLin.setAnchoColumna(new int[]
                {54, 172, 36, 56, 60, 60, 50,50,60});
            jtLin.setAlinearColumna(new int[]
                {2, 0, 2, 2, 2, 2, 2,1,2});
            jtLin.setFormatoColumna(3, "---,--9.99");
            jtLin.setFormatoColumna(4, "---9.9999");
            jtLin.setFormatoColumna(5, "---9.9999");
            jtLin.setFormatoColumna(6, "--99.99");
            jtLin.setFormatoColumna(7, "BSN");
            jtLin.setFormatoColumna(8, "---9.9999");
            jtLin.resetRenderer(7);
            jtLin.setAjustarGrid(true);
            CTextField[] tfLin = new CTextField[10];
            for (int n=0;n<8;n++)
            {
                tfLin[n]=new CTextField();
                tfLin[n].setEnabled(false);
            }
            def_preusuE.setEnabled(false);
            //    c7.setEnabled(false);
            Vector v1 = new Vector();
            v1.addElement(tfLin[0]);
            v1.addElement(tfLin[1]);
            v1.addElement(tfLin[2]);
            v1.addElement(tfLin[3]);
            v1.addElement(def_prcostE);
            v1.addElement(tfLin[5]);
            v1.addElement(tfLin[6]);
            v1.addElement(def_prebloC);
            v1.addElement(def_preusuE);
            jtLin.setCampos(v1);
            jtLin.setCanInsertLinea(false);
            jtLin.setCanDeleteLinea(false);
        } catch (Exception k) {Error("Error al iniciar grid final",k);
            return;
        }

        javax.swing.GroupLayout jtLinLayout = new javax.swing.GroupLayout(jtLin);
        jtLin.setLayout(jtLinLayout);
        jtLinLayout.setHorizontalGroup(
            jtLinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 628, Short.MAX_VALUE)
        );
        jtLinLayout.setVerticalGroup(
            jtLinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 44, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Pprinc.add(jtLin, gridBagConstraints);

        Ppie.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Ppie.setMaximumSize(new java.awt.Dimension(610, 85));
        Ppie.setMinimumSize(new java.awt.Dimension(610, 85));
        Ppie.setName(""); // NOI18N
        Ppie.setPreferredSize(new java.awt.Dimension(610, 85));
        Ppie.setLayout(null);

        Bcancelar.setText("Cancelar");
        Ppie.add(Bcancelar);
        Bcancelar.setBounds(510, 2, 90, 24);

        PtotOri.setBorder(javax.swing.BorderFactory.createTitledBorder("Origen"));
        PtotOri.setLayout(null);

        cLabel5.setText("Unid");
        PtotOri.add(cLabel5);
        cLabel5.setBounds(10, 14, 30, 17);

        kilosCabE.setEnabled(false);
        PtotOri.add(kilosCabE);
        kilosCabE.setBounds(110, 14, 57, 17);

        cLabel6.setText("Kilos");
        PtotOri.add(cLabel6);
        cLabel6.setBounds(80, 14, 30, 17);

        uniCabE.setEnabled(false);
        PtotOri.add(uniCabE);
        uniCabE.setBounds(50, 14, 20, 17);

        cLabel7.setText("Importe");
        PtotOri.add(cLabel7);
        cLabel7.setBounds(170, 14, 50, 17);

        impCabE.setEnabled(false);
        PtotOri.add(impCabE);
        impCabE.setBounds(220, 14, 60, 17);

        Ppie.add(PtotOri);
        PtotOri.setBounds(2, 2, 300, 40);

        agruCabC.setSelected(true);
        agruCabC.setText("Agrupar Prod");
        agruCabC.setMargin(new java.awt.Insets(2, 0, 2, 0));
        Ppie.add(agruCabC);
        agruCabC.setBounds(302, 10, 105, 17);

        PtotFin.setBorder(javax.swing.BorderFactory.createTitledBorder("Final"));
        PtotFin.setLayout(null);

        cLabel8.setText("Unid");
        PtotFin.add(cLabel8);
        cLabel8.setBounds(10, 14, 30, 17);

        kilosFinE.setEnabled(false);
        PtotFin.add(kilosFinE);
        kilosFinE.setBounds(110, 14, 57, 17);

        cLabel9.setText("Kilos");
        PtotFin.add(cLabel9);
        cLabel9.setBounds(80, 14, 30, 17);

        uniFinE.setEnabled(false);
        PtotFin.add(uniFinE);
        uniFinE.setBounds(50, 14, 20, 17);

        cLabel10.setText("Importe");
        PtotFin.add(cLabel10);
        cLabel10.setBounds(170, 14, 50, 17);

        impFinE.setEnabled(false);
        PtotFin.add(impFinE);
        impFinE.setBounds(220, 14, 60, 17);

        Ppie.add(PtotFin);
        PtotFin.setBounds(2, 40, 300, 40);

        cLabel11.setText("Reg. Selec");
        Ppie.add(cLabel11);
        cLabel11.setBounds(305, 30, 70, 17);

        numRegSelE.setEnabled(false);
        Ppie.add(numRegSelE);
        numRegSelE.setBounds(375, 30, 30, 17);

        BValorar.setText("Valorar (F5)");
        BValorar.addMenu("Valorar");
        BValorar.addMenu("Anular Valoracion");
        Ppie.add(BValorar);
        BValorar.setBounds(460, 55, 130, 24);

        Baceptar.setText("Aceptar");
        Baceptar.setPreferredSize(new java.awt.Dimension(47, 19));
        Ppie.add(Baceptar);
        Baceptar.setBounds(410, 2, 90, 24);

        Bir.setText("F2");
        Bir.setToolTipText("Ir a siguiente grid");
        Ppie.add(Bir);
        Bir.setBounds(537, 30, 60, 22);

        Binvsel.addMenu("Invertir Seleccion");
        Binvsel.addMenu("Seleccionar Todo");
        Binvsel.addMenu("Deselecionar Todo");
        Binvsel.setText("Filtrar");
        Ppie.add(Binvsel);
        Binvsel.setBounds(410, 30, 90, 22);

        BAgrupar.setText("Agrupar (F5)");
        BAgrupar.addMenu("Agrupar");
        BAgrupar.addMenu("Desagrupar");
        Ppie.add(BAgrupar);
        BAgrupar.setBounds(320, 55, 130, 24);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 5);
        Pprinc.add(Ppie, gridBagConstraints);

        getContentPane().add(Pprinc, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gnu.chu.controles.CButtonMenu BAgrupar;
    private gnu.chu.controles.CButtonMenu BValorar;
    private gnu.chu.controles.CButton Baceptar;
    private gnu.chu.controles.CButtonMenu Bbuscar;
    private gnu.chu.controles.CButton Bcancelar;
    private gnu.chu.controles.CButtonMenu Binvsel;
    private gnu.chu.controles.CButton Bir;
    private gnu.chu.controles.CPanel Pcond;
    private gnu.chu.controles.CPanel Ppie;
    private gnu.chu.controles.CPanel Pprinc;
    private gnu.chu.controles.CPanel PtotFin;
    private gnu.chu.controles.CPanel PtotOri;
    private gnu.chu.controles.CCheckBox agruCabC;
    private gnu.chu.controles.CComboBox agrupaC;
    private gnu.chu.controles.CLabel cLabel1;
    private gnu.chu.controles.CLabel cLabel10;
    private gnu.chu.controles.CLabel cLabel11;
    private gnu.chu.controles.CLabel cLabel12;
    private gnu.chu.controles.CLabel cLabel13;
    private gnu.chu.controles.CLabel cLabel14;
    private gnu.chu.controles.CLabel cLabel2;
    private gnu.chu.controles.CLabel cLabel3;
    private gnu.chu.controles.CLabel cLabel4;
    private gnu.chu.controles.CLabel cLabel5;
    private gnu.chu.controles.CLabel cLabel6;
    private gnu.chu.controles.CLabel cLabel7;
    private gnu.chu.controles.CLabel cLabel8;
    private gnu.chu.controles.CLabel cLabel9;
    private gnu.chu.controles.CTextField def_prcostE;
    private gnu.chu.controles.CCheckBox def_prebloC;
    private gnu.chu.controles.CTextField def_preusuE;
    private gnu.chu.controles.CTextField deo_codiE;
    private gnu.chu.controles.CTextField deo_prcogrE;
    private gnu.chu.controles.CTextField eje_numeE;
    private gnu.chu.controles.CTextField fecinfE;
    private gnu.chu.controles.CTextField fecsupE;
    private gnu.chu.controles.CTextField grd_numeE;
    private gnu.chu.controles.CComboBox grd_valorE;
    private gnu.chu.controles.CComboBox grupoC;
    private gnu.chu.controles.CTextField impCabE;
    private gnu.chu.controles.CTextField impFinE;
    private gnu.chu.controles.CGridEditable jtCab;
    private gnu.chu.controles.Cgrid jtDesp;
    private gnu.chu.controles.CGridEditable jtLin;
    private gnu.chu.controles.CTextField kilosCabE;
    private gnu.chu.controles.CTextField kilosFinE;
    private gnu.chu.controles.CTextField numRegSelE;
    private gnu.chu.controles.CComboBox opCerradoC;
    private gnu.chu.controles.CCheckBox opVerGrupo;
    private gnu.chu.camposdb.proPanel pro_codiE;
    private gnu.chu.camposdb.tidCodi2 tid_codfinE;
    private gnu.chu.camposdb.tidCodi2 tid_codiniE;
    private gnu.chu.controles.CTextField uniCabE;
    private gnu.chu.controles.CTextField uniFinE;
    // End of variables declaration//GEN-END:variables
}
