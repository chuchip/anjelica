package gnu.chu.anjelica.despiece;

/**
 *
 * <p>Titulo: utildesp </p>
 * <p>Descripcion: Serie de Funciones con Utilidades para mant. de Despiece
 * Localiza datos de trazabilidad de  un individuo en particular.
 * </p>
 * <p>Copyright: Copyright (c) 2005-2017
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
 * @author chuchiP
 * @version 1.1
 */

import gnu.chu.anjelica.almacen.StkPartid;
import gnu.chu.anjelica.almacen.ActualStkPart;
import gnu.chu.anjelica.almacen.DatIndivBase;
import gnu.chu.anjelica.compras.MantAlbComCarne;
import gnu.chu.anjelica.listados.etiqueta;
import gnu.chu.anjelica.pad.MantPaises;
import gnu.chu.anjelica.pad.pdempresa;
import gnu.chu.sql.DatosTabla;
import gnu.chu.utilidades.CodigoBarras;
import gnu.chu.utilidades.EntornoUsuario;
import gnu.chu.utilidades.Formatear;
import java.util.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Vector;
import org.apache.log4j.Logger;

public class utildesp
{
  boolean swForzaTraza=false;
  DatIndivBase datInd=new DatIndivBase();
  private String tipoProduc="N";
  private String idioma=null;
  private int proCodiCompra;
  private int tidCodi=0;
  private int proLoteCompra;
  private String proSerieCompra;
  private int proIndiCompra;
  private int ejeLotCompra;
  private String datmat=null;
  private String diremp=null;
  private String logo=null;
  private final boolean swGenCrotal=false; // Generar crotal aleatoriamente.
  private final Vector <String> indiv  =new Vector();
  private final Vector <String> crotales  =new Vector();
  private int repiteIndiv=0;
  private boolean buscaIndDesp=false;
  private Date fecCadDesp;

  private boolean  swBuscaCompra=false;
  private java.util.Date fecCadPrvE,fecCompraE,fecDespE,fecProdE;
  
 // private int deoNumdes=0;
  public boolean swDesp; // Indica si el individuo salio de un despiece
  private String msgAviso;
  private Date deoFecval;
  public String fecrepL;
  public String fecrecepE;
  public String paisNacimientoNombre,paisEngordeNombre,despiezadoE,ntrazaE,sacrificadoE;
  private String conservarE;
  public String despiezadoD;
  public String accSerie;
  public int deoAno,deoCodi,accNume,accAno;
  public double accPrcom;
  int paiEmp;
  String empRGSA;
  public java.util.Date fecSacrE=null;
  public int proCodiDes;
  boolean dtRes;
  public String feccadE=null;
  public Date feccadD=null;
  boolean cambio=true;
  public int prvCodi=0;
  private String acp_painac,acp_engpai,acp_paisac;
  private String stp_matad,stp_saldes;
  private String mat_nrgsa,sde_nrgsa,paisSacrificioNombre;
// Variables Cabecera de Despiece
  int ejeNume, empCodi, numDesp;
  int almDest, almOrig;
  DatosTabla dtAdd;
  int grdNume=0;
  EntornoUsuario EU;
//  ActualStkPart actStkPart;
  StkPartid stkPart;
  etiqueta etiq;

  Logger loger = Logger.getLogger(utildesp.class.getName());
  public int prvCompra;
  private boolean swSaltaDespInt=false;
  private boolean swAlbInt=false;
  private int  ejeLotY,proLoteY,  empLotY ;
  private boolean swPaisCorto=false; // Especifica si pone el nombre del pais o las siglas del pais.
  
  public utildesp()
  {

  }
  /**
   * Especifica si poner el nombre del pais o las iniciales del pais en el matadero y sala de despiece
   * por defecto pone el nombre del pais. 
   * @param swNombrePais Si se manda true, pondra las iniciales, false pondra el nombre
   */
  public void setInicialesPais(boolean swNombrePais)
  {
      swPaisCorto=swNombrePais;
  }
  /**
   * @deprecated
   * Especifica si se deben saltar los albaranes de compra internos
   * @param saltaDespInt 
   */
  public void setSaltaDespInt(boolean saltaDespInt)
  {
    swSaltaDespInt = saltaDespInt;
  }
/**
 * @deprecated 
 * Devuelve si se deben saltar los albaranes de compra internos
 * @return 
 */
  public boolean getSaltaDespInt()
  {
    return swSaltaDespInt;
  }

  public boolean isCompraInterna()
  {
    return swAlbInt;
  }

  public int getEjeLoteY()
  {
    return ejeLotY;
  }

  public int getProLoteY()
  {
    return proLoteY;
  }

  public int getEmpLoteY()
  {
    return empLotY;
  }


/**
 * 
 * @param serie
 * @param proCodi
 * @param empLot
 * @param ejeLot
 * @param proLote
 * @param proIndi
 * @param dtCon1
 * @param dtStat
 * @param EU
 * @return boolean true si encuentra los datos del individuo
 * @throws SQLException 
 */
  public boolean busDatInd(String serie, int proCodi,int empLot,
                           int ejeLot, int proLote,
                           int proIndi,
                           DatosTabla dtCon1, DatosTabla dtStat,
                           EntornoUsuario EU) throws SQLException
  {
    return busDatInd(serie,proCodi,empLot,ejeLot,proLote,proIndi,0,dtCon1,dtStat,EU);
  }
  /**
   * Busca datos de compra de un lote.
   * @param serie Serie del individuo
   * @param proCodi Produco
   * @param empLot
   * @param ejeLot
   * @param proLote
     * @param nInd
   * @param dt1
   * @param dt2
   * @param EU
   * @return  true si encuentra los datos del individuo
   * @throws java.sql.SQLException
   */
  public boolean buscaDatCompra(String serie, int proCodi,int empLot,int ejeLot,
          int proLote,int nInd,DatosTabla dt1,DatosTabla dt2,EntornoUsuario EU) throws SQLException
  {
      setBuscaCompra(true);
      return buscaDatosIndiv(serie, proCodi, empLot, ejeLot, proLote, nInd, 0,dt1, dt2, EU);
  }
  /**
   * Busca datos de trazabilidad de un individio en particular
   * @param serie String
   * @param proCodi int
   * @param empLot int
   * @param ejeLot int
   * @param proLote int
   * @param proIndi int si es 0 no buscar individuo.
   * @param almCodi int
   * @param dtCon1 DatosTabla
   * @param dtStat DatosTabla
   * @param EU EntornoUsuario
   * @throws SQLException
   *
   * @return boolean  true si encuentra los datos del individuo
   */
    
    public boolean busDatInd(String serie, int proCodi, int empLot,
            int ejeLot, int proLote,
            int proIndi,
            int almCodi, DatosTabla dtCon1, DatosTabla dtStat,
            EntornoUsuario EU) throws SQLException {
        setBuscaCompra(false);
        return buscaDatosIndiv(serie, proCodi, empLot, ejeLot, proLote, proIndi, almCodi, dtCon1, dtStat, EU);
    }
    
    public DatIndivBase getDatosIndividuo()
    {
        return datInd;
    }
    /**
     * Pone en stockPartidas los valores actuales de las variables
     * @param dtAdd
     * @return false si no existia el registro en stockpartidas
     * @throws SQLException 
     */
    public boolean actualTrazabilidad(DatosTabla dtAdd) throws SQLException
    {
        if (!ActualStkPart.checkIndiv(dtAdd, datInd.getProducto(), datInd.getEjercLot(),0,
            datInd.getSerie(), datInd.getLote(), datInd.getNumind(),datInd.getAlmCodi(),true))
            return false;
        dtAdd.edit();
        dtAdd.setDato("stp_feccad",getFechaCaducidad());
        dtAdd.setDato("stp_fecpro",getFechaProduccion());
        dtAdd.setDato("stp_nucrot",getNumCrot());
        dtAdd.setDato("stp_painac",acp_painac);
        dtAdd.setDato("stp_engpai",acp_engpai);
        dtAdd.setDato("stp_paisac",acp_paisac);
        dtAdd.setDato("stp_fecsac",getFecSacrif());        
        dtAdd.setDato("stp_matad",stp_matad);
        dtAdd.setDato("stp_saldes",stp_saldes);
        dtAdd.setDato("stp_traaut",0);
        dtAdd.setDato("stp_fefici","current_timestamp");
        dtAdd.update();
        return true;
    }
    
    public boolean isForzadaTrazab()
    {
        return swForzaTraza;
    }
 /**
   * Busca datos de trazabilidad de un individio en particular
   * @param serie String
   * @param proCodi int
   * @param empLot int
   * @param ejeLot int
   * @param proLote int
   * @param proIndi int si es 0 no buscar individuo.
   * @param almCodi int
   * @param dtCon1 DatosTabla
   * @param dtStat DatosTabla
   * @param EU EntornoUsuario
   * @throws SQLException
   * @throws ParseException
   * @return boolean true si encuentra los datos del individuo
   */
    private boolean buscaDatosIndiv(String serie, int proCodi, int empLot,
            int ejeLot, int proLote,
            int proIndi,
            int almCodi, DatosTabla dtCon1, DatosTabla dtStat,
            EntornoUsuario EU) throws SQLException {
        prvCodi = 0;
    
    datInd.setProducto(proCodi);
    datInd.setEjercLot(ejeLot);
    datInd.setSerie(serie);    
    datInd.setLote(proLote);
    datInd.setNumind(proIndi);
    datInd.setAlmCodi(almCodi);
    
//    debug("Buscando Datos Indiv. Prod: "+proCodi+" Lote: "+serie+"/"+proLote+"-"+proIndi,EU);
    int proCodiOrig=proCodi;
    String s;
    fecProdE=null;
    fecDespE=null;
    msgAviso = null;
    boolean busca = true;
    int nVuelta = 0;
    swDesp = false;
    swAlbInt=false;
    ntrazaE = null;
    sacrificadoE = "";
    despiezadoE = null;
    paisNacimientoNombre = null;
    paisEngordeNombre = null;
    fecSacrE = null;
    
    feccadE=null;
    fecCadDesp=null;
    deoCodi=0;
    boolean deoDesnue=false;
    deoAno=0;
    accSerie="";
    accAno=0;
    accNume=0;
    accPrcom=-1;
    prvCompra=0;
    fecCompraE=null;
    fecCadPrvE=null;
    ejeLotY=0;
    proLoteY = 0;
    empLotY = 0;
    acp_painac=null;
    acp_engpai=null;
    acp_paisac=null;
    fecSacrE=null;
    stp_matad="";
    stp_saldes="";
    boolean swTidCodi;
    swForzaTraza=false;
    if (ActualStkPart.checkIndiv(dtStat, datInd.getProducto(), datInd.getEjercLot(),0,
            datInd.getSerie(), datInd.getLote(), datInd.getNumind(),datInd.getAlmCodi(),false))
    {
        feccadE=dtStat.getFecha("stp_feccad","dd-MM-yyyy");
        feccadD=dtStat.getDate("stp_feccad");
        if (feccadE.equals(""))
            feccadE=null;
        fecProdE=dtStat.getDate("stp_fecpro");
        ntrazaE=dtStat.getString("stp_nucrot",false);
        acp_painac=dtStat.getString("stp_painac",false);
        acp_engpai=dtStat.getString("stp_engpai",false);
        acp_paisac=dtStat.getString("stp_paisac",false);
        fecSacrE=dtStat.getDate("stp_fecsac");
        stp_matad=dtStat.getString("stp_matad",true);
        stp_saldes=dtStat.getString("stp_saldes",true);        
        swForzaTraza=dtStat.getInt("stp_traaut")==0;
    }
    if (!pdempresa.checkEmpresa(dtCon1, empLot))
    {
      msgAviso = "No encontrados datos de Empresa: " + empLot;
      return false;
    }
   paiEmp = dtCon1.getInt("pai_codi",true);
   empRGSA = dtCon1.getString("emp_nurgsa");
   
   while (busca)
   {
      nVuelta++;
      if (nVuelta > 20)
      {
        msgAviso = "Mas de " + nVuelta + " depieces consecutivos " + empLot +
            "-" + dtCon1.getInt("eje_nume") +
            "-" + dtCon1.getInt("deo_codi") + " Busqueda cancelada";
        sacrificadoE="-----";
        paisNacimientoNombre="----";
        return false;
      }
      // Primero busco en Compras el Individuo mandado.
      s = "select *, acl_prcom  as prcom  "+
         " from  v_compras " +
          " WHERE acc_ano = " + ejeLot +
          (empLot==0?"": " and emp_codi = " + empLot )+          
          " and acc_serie = '" + serie + "'" +
          " and acc_nume = " + proLote +
          (proIndi==0?(proCodi!=0?"":" and pro_codi = " + proCodi )          
          :" and acp_numind = " + proIndi )+
          (proCodi!=0?"":" and pro_codi = " + proCodi )+
          (almCodi!=0?" and alm_codi = "+almCodi:"");

      dtRes=dtStat.select(s);
      if (!dtRes )
      {
        // No encontrado individuo  en compras, lo busco en salidas de despieces
        s = "SELECT * FROM v_despsal "+
            " WHERE pro_codi = " + proCodi +
            " and def_ejelot = " + ejeLot +
            " and def_serlot = '" + serie + "'" +
            " and pro_lote = " + proLote +
            " and "+(proIndi==0?" pro_codi= "+proCodi
            :" pro_numind = " + proIndi)+
            (almCodi!=0?" and deo_almdes = "+almCodi:"")+
            " order by def_tiempo desc";
        if (!dtCon1.select(s))
        {
          msgAviso = "No encontrado origen de Individuo en Compras o Despieces";
          return false;
        }
        if (accPrcom==-1)
             accPrcom=dtCon1.getDouble("def_prcost",true);
        swTidCodi=false;
        if (tidCodi==0 || tidCodi==dtCon1.getInt("tid_codi"))
            swTidCodi=true;
        if (!deoDesnue && !swForzaTraza)
            deoDesnue=dtCon1.getString("deo_desnue").equals("S");
//        }
//        else
//        {
//            if (accPrcom==-1)
//                System.out.println("update desporig set tid_codi=9997 where eje_nume="+dtCon1.getInt("eje_nume")+
//                     " and deo_codi= "+dtCon1.getInt("deo_codi")+" ant tid_codi= "+dtCon1.getInt("tid_codi")+";");
//        }
        // Busco el producto de origen en despiece entrada.
        s = "SELECT d.* FROM v_despori as d "
                    + " WHERE d.eje_nume = " + dtCon1.getInt("eje_nume")
                    + " and d.deo_codi = " + dtCon1.getInt("deo_codi")
                    + " order by deo_codi,del_numlin";
        if (!dtStat.select(s))
        {
                msgAviso = "NO encontrado cabecera de Despiece: "
                        + (EU != null ? EU.em_cod + "-" : "") + dtCon1.getInt("eje_nume")
                        + "-" + dtCon1.getInt("deo_codi") + " Para Individ: "
                        + empLot + "|" + ejeLot + "-" + proCodi + ":" + serie + proLote + "-" + proIndi;
                return false;
        }
         if (buscaIndDesp)
         {
             String ind;
             do
             {
               ind=getIndividuo(dtStat.getInt("pro_codi"),
                        dtStat.getInt("deo_ejelot"),
                        dtStat.getInt("deo_emplot"),
                        dtStat.getInt("pro_lote"),
                        dtStat.getInt("pro_numind"),
                        dtStat.getString("deo_serlot"));
//               System.out.println("ind desp: "+ind);
               indiv.add(ind);
               if (! checkRepetic(indiv,ind) )
                   break;
//                 System.out.println("pues esta repetido el individuo: "+ind);
             } while (dtStat.next());
//             if (dtStat.isLast())
//                 msgAviso = "Individuo repetido mas de " + repiteIndiv + "veces \n";
         }
         
        if (prvCodi==0 && swTidCodi)
        {
          prvCodi = dtStat.getInt("prv_codi", true);
          deoCodi=dtStat.getInt("deo_codi");
          deoAno=dtStat.getInt("eje_nume");
          fecDespE=dtStat.getDate("deo_fecha");
          if (fecProdE==null)
            fecProdE=dtStat.getDate("deo_fecpro");
        }

        serie = dtStat.getString("deo_serlot");
        proCodi = dtStat.getInt("pro_codi");
        proCodiDes=proCodi;
        empLot = dtStat.getInt("deo_emplot");
        ejeLot = dtStat.getInt("deo_ejelot");
        proLote = dtStat.getInt("pro_lote");
        proIndi = dtStat.getInt("pro_numind");
//        debug("Encontrado en Desp: "+dtCon1.getInt("deo_codi")+" Viene de Prod: "+proCodi+" Lote: "+serie+"/"+proLote+"-"+proIndi,EU);
        if ( swTidCodi)
        {
            if (!swDesp)
            {
                fecrepL = "Fec.Prod";
                fecrecepE = dtStat.getFecha("deo_fecha", "dd-MM-yyyy");
            }
            if (feccadE==null && dtCon1.getObject("def_feccad") != null)
            {
                feccadE = dtCon1.getFecha("def_feccad", "dd-MM-yyyy");
                feccadD=dtCon1.getDate("def_feccad");
            }
            if (fecCadDesp==null && dtCon1.getObject("def_feccad") != null)
                fecCadDesp=dtCon1.getDate("def_feccad");
            swDesp = true;
        }
      }
      else
      { // Encontrado en Compras.
          accNume=dtStat.getInt("acc_nume");
          accAno=dtStat.getInt("acc_ano");
          accSerie=dtStat.getString("acc_serie");
          proCodiCompra=proCodi;
          proLoteCompra=proLote;
          proSerieCompra=serie;
          proIndiCompra=proIndi;
          ejeLotCompra=ejeLot;
//        debug("Encontrado en Compras. Lote: "+proLote+"  Prod: "+proCodi+" Lote: "+serie+"/"+proLote+"-"+proIndi,EU);
          if (swSaltaDespInt && dtStat.getString("acc_serie").equals(EntornoUsuario.SERIEY))
          { // Albaran Interno. Saltarlo.
            s="SELECT * FROM v_albcopave WHERE emp_codi = "+empLot+
                " and acc_ano = "+ejeLot+
                " and acc_serie = '"+serie+"'"+
                " and acc_nume = "+proLote+
                " and acl_nulin = "+dtStat.getInt("acl_nulin")+
                " and acp_numlin = "+dtStat.getInt("acp_numlin");
            if (dtCon1.select(s))
            { // Pongo datos albaran venta.
              ejeLotY=ejeLot;
              proLoteY=proLote;
              empLotY=empLot;
              proCodi=dtCon1.getInt("pro_codi");
              ejeLot=dtCon1.getInt("avp_ejelot");
              serie=dtCon1.getString("avp_serlot");
              proLote=dtCon1.getInt("avp_numpar");
              proIndi=dtCon1.getInt("avp_numind");
              swAlbInt=true;
              continue;
            }
          }
          if (prvCodi==0)
          {
            prvCodi = dtStat.getInt("prv_codi", true);
            accSerie=dtStat.getString("acc_serie");
            accNume=dtStat.getInt("acc_nume");
            accAno=dtStat.getInt("acc_ano");
          }
          if (prvCompra==0)
          {
            prvCompra=dtStat.getInt("prv_codi", true);
            fecCompraE=dtStat.getDate("acc_fecrec");
          }
          if (accPrcom==-1)
            accPrcom=dtStat.getDouble("prcom",true);

          if (feccadE==null)
          {
            feccadE=dtStat.getFecha("acp_feccad","dd-MM-yyyy");
            feccadD=dtStat.getDate("acp_feccad");
          }
          fecCadPrvE=dtStat.getDate("acp_feccad");
          if (fecSacrE==null)
            fecSacrE=dtStat.getDate("acp_fecsac");
          if (fecProdE==null)
            fecProdE=dtStat.getDate("acp_fecpro");

          if (!swDesp)
          {
            fecrepL = "Fec.Recep:";
            fecrecepE = dtStat.getFecha("acc_fecrec", "dd-MM-yyyy");
          }
          if (swBuscaCompra)
              return true;
          if (acp_painac==null)
            acp_painac=dtStat.getString("acp_painac");
          paisNacimientoNombre=MantPaises.getNombrePais(acp_painac, dtCon1);         
          if (paisNacimientoNombre==null)
          {
            msgAviso = "No encontrado PAIS NACIMIENTO: " +  acp_painac;
            paisNacimientoNombre = "";
          }
          if (acp_engpai==null)
            acp_engpai=dtStat.getString("acp_engpai");
          paisEngordeNombre=MantPaises.getNombrePais(acp_engpai, dtCon1);         
          if (paisEngordeNombre==null)
          {
            msgAviso = "No encontrado PAIS CEBADO: " +  acp_engpai;
            paisEngordeNombre = "";
          }
          if (stp_matad.isEmpty())
            stp_matad = dtStat.getString("acp_matad");
          sacrificadoE=stp_matad;
          mat_nrgsa=sacrificadoE;
          sacrificadoE=getRegistroSanitario(false,dtCon1,sacrificadoE,dtStat.getString("acp_paisac"));           

          if (acp_paisac==null)
            acp_paisac = dtStat.getString("acp_paisac");
          paisSacrificioNombre=MantPaises.getNombrePais(acp_paisac, dtCon1);      
          
          if (paisSacrificioNombre==null)
          {
            msgAviso = "No encontrado PAIS Sacrificio: " +  acp_paisac;    
            paisSacrificioNombre="";
          }
          if (stp_saldes.isEmpty()) 
            stp_saldes=dtStat.getString("acp_saldes");
          if (deoDesnue)
            stp_saldes= pdempresa.getNumeroRegistroSanitario(dtCon1,EU.em_cod);
          despiezadoE = stp_saldes;
          sde_nrgsa=despiezadoE;
          
       if (ntrazaE==null)
        ntrazaE = dtStat.getString("acp_nucrot");
       if (repiteIndiv>0 && ntrazaE.length()>0)
       {
            crotales.add(ntrazaE);
//           System.out.println("crotal: "+ntrazaE);
            if (checkRepetic(crotales,ntrazaE))
            {
//                    System.out.println("pues esta repetido el crotal: "+ntrazaE);
               if (swGenCrotal)
               {
                   ntrazaE=MantAlbComCarne.getRandomCrotal(ntrazaE,EU);
                   crotales.add(ntrazaE);
                   if (checkRepetic(crotales,ntrazaE))
                   {
                       msgAviso = (msgAviso==null?"":msgAviso+"\n")+
                       "Numero crotal: " +ntrazaE+ " No se pudo generar otro aleatorio. ";
                   }
               }
               else
                     msgAviso = (msgAviso==null?"":msgAviso+"\n")+
                       "Numero crotal: " +ntrazaE+ " Repetido más de: "+repiteIndiv+" Veces";
            }

       } 
        actualConservar(proCodiOrig, dtCon1);
        busca = false;
      }
    }
    if (swDesp)
      fecrepL = "Fec.Desp";
    else
      fecrepL = "Fecha Recepc.";
    despiezadoD=despiezadoE;
    return true;
  }
  public   String getRegistroSanitario(DatosTabla dt,String numRegSanitario,int paiCodi) throws SQLException
  {          
      return getRegistroSanitario(swPaisCorto,dt, numRegSanitario, paiCodi);
  }
  public  static String getRegistroSanitario(boolean swPaisCorto,DatosTabla dt,String numRegSanitario,int paiCodi) throws SQLException
  {          
        String s = "select pai_nomb,pai_nomcor from v_paises where pai_codi = " +paiCodi;
        if (dt.select(s))
          numRegSanitario = (swPaisCorto?  dt.getString("pai_nomcor") :dt.getString("pai_nomb") )
              + "-" + numRegSanitario;
        return numRegSanitario;
  }
 
  public  static String getRegistroSanitario(boolean swPaisCorto,DatosTabla dt,String numRegSanitario,String paiInic) throws SQLException
  {          
        String s = "select pai_nomb,pai_nomcor from v_paises where pai_inic = '" +paiInic+"'";
        if (dt.select(s))
          numRegSanitario = (swPaisCorto?  dt.getString("pai_nomcor") :dt.getString("pai_nomb") )
              + "-" + numRegSanitario;
        return numRegSanitario;
  }
  boolean checkRepetic(Vector v, String cadena) {
        int idx = 0;
        int n;
        for (n = 0; n <= repiteIndiv; n++) {
            idx = v.indexOf(cadena, idx + 1);
            if (idx < 0) {
                break;
            }
        }
        return n > repiteIndiv;
    }

    private String getIndividuo(int proCodi, int ejeNume, int empCodi, int lote, int numInd,String serie)
    {
       return  proCodi+"|"+ejeNume+"|"+
        empCodi+
        lote+
        numInd+
        serie;
    }


  /**
    * Pone los datos como si fuera un desp. Nuestro
    *
    * @throws Exception Si hay error  base datos
    */
   public void resetDespNuestro() throws Exception
   {
     despiezadoE = despiezadoD;
   }

  /**
   * Pone los datos como si fuera un desp. Nuestro
   * @param fecDesp Fecha Despiece (Formato: dd-MM-yyyy)
   * @param dt Datos Tabla para buscar nombre pais desp.
   * @throws Exception Si hay error  base datos
   */
  public void setDespNuestro(String fecDesp, DatosTabla dt) throws Exception
  {
    fecrepL = "Fec.Desp";
    fecrecepE = fecDesp;
    String s = "select pai_inic,pai_nomcor,pai_nomb from v_paises where pai_codi = " + paiEmp;
    if (!dt.select(s))
    {
      msgAviso = "No encontrado PAIS de Despiece: " + paiEmp;
      despiezadoE = "";
      return;
    }
    despiezadoE = (swPaisCorto?  dt.getString("pai_nomcor") :dt.getString("pai_nomb") ) + "-" + empRGSA;
  }

  public int busCliVenta(String serie, int proCodi, int empLot,
                          int ejeLot, int proLote,
                          int proIndi,
                          DatosTabla dtStat,
                          EntornoUsuario EU) throws SQLException,java.text.ParseException
 {
   String s;
   s="SELECT c.cli_codi FROM v_albvenpar as p,v_albavec as c WHERE p.pro_codi = "+proCodi+
       " and p.avp_ejelot = "+ejeLot+
       " and p.avp_emplot = "+empLot+
       " and p.avp_serlot = '"+serie+"'"+
       " and p.avp_numpar = "+proLote+
       " and p.avp_numind = "+proIndi+
       (EU != null ?" and p.emp_codi =  "+EU.em_cod:"")+
       " and c.emp_codi = p.emp_codi "+
       " and c.avc_ano = p.avc_ano "+
       " and c.avc_nume = p.avc_nume "+
       " and c.avc_serie = p.avc_serie ";
   if (! dtStat.select(s))
     return 0;
   return dtStat.getInt("cli_codi");
 }
  
  public void setIdioma(String idioma)
  {
      this.idioma=idioma;
  }
  public String getConservar()
  {
      return conservarE;
  }
  public void setConservar(String litConservar)
  {
         conservarE=litConservar;
  }
  public void actualConservar(int codProd, DatosTabla dtCon1) throws SQLException
  {
    conservarE=gnu.chu.anjelica.pad.MantArticulos.getStrConservar(codProd,dtCon1,idioma);
  }
  /**
   * Devuelve fecha congelado
   * @param codProd
   * @param fecProd Fecha Produccion
   * @param dt
   * @return string "" si el producto no es congelado
   *
   * @throws SQLException
   */
  public static String getFechaCongelado(int codProd, java.util.Date fecProd, DatosTabla dt) throws SQLException
  {   
     java.util.Date fecCong=getDateCongelado(codProd,fecProd,dt);
     if (fecCong==null)
         return "";
     return "Cad.Cong:"+ Formatear.getFecha(fecCong, "dd-MM-yy");
  }
  public static java.util.Date getDateCongelado(int codProd, java.util.Date fecProd, DatosTabla dt) throws SQLException
  {
       if (fecProd==null)
         return null;
     Boolean b=gnu.chu.anjelica.pad.MantArticulos.isCongelado(codProd,dt);
     if (b==null)
         return null;
     if (!b)
         return null;
     return Formatear.sumaMesDate(fecProd, dt.getInt("pro_cadcong"));
  }
  /**
   * Busca Maximo Numero de Grupo a Asignar
   * @param dt DatosTabla
   * @param ejerc int Ejercicio del Grupo
   * @param empr int Empresa del Grupo
   * @param grupoInf int Limite Inferior. Si es >0 se buscara el maximo numero de grupo que sea inferior al dado
   * Si el numero resultante es <=100 se pondra 101 como maximo grupo a asignar.<br>
   *  Esto es asi por mantener la compatiblidad con antiguas versiones.
   * @throws SQLException
   * 
   * @return int
   */
  public static int buscaMaxGrp(DatosTabla dt,int ejerc,int empr,int grupoInf) throws SQLException
  {
      String s = "SELECT MAX(GRD_NUME) as GRD_NUME FROM grupdesp "+
         " WHERE eje_nume = " + ejerc+
          " AND  emp_codi = " + empr+
          (grupoInf>0?" and GRD_NUME < "+grupoInf:"");
      dt.select(s);
      int grdNume=dt.getInt("grd_nume",true);
      if (grdNume<100)
        grdNume=100;
      return grdNume+1;
  }


   public static StkPartid buscaPeso(DatosTabla dt,int ejeLot,int empLot,
                                 String serLot,int numLot,int numind,int proCodi,
                                 int almCodi) throws SQLException
   {
        return buscaPeso(dt,ejeLot,empLot,serLot,numLot,numind,proCodi,almCodi,false);
   }
  /**
   * Busca el Peso de un individuo segun la tabla v_stkpart
   * @param dt DatosTabla a utlizar
   * @param ejeLot Ejerc. Lote
   * @param empLot Empresa Lote
   * @param serLot Serie del Lote
   * @param numLot Numero del Lote
   * @param numind Individuo del Lote
   * @param proCodi Producto
   * @param almCodi Almacen si es cero, busca en todos.
   * @param incBloq Incluir Individuos bloqueados
   * @return mensaje de Error. NULL si todo ha ido bien
   * @throws SQLException En caso de error en base datos
   */
  public static StkPartid buscaPeso(DatosTabla dt,int ejeLot,int empLot,
                                 String serLot,int numLot,int numind,int proCodi,
                                 int almCodi,boolean incBloq) throws SQLException
  {
     StkPartid stkPart=new StkPartid(StkPartid.ARTIC_NOT_FOUND);
     String s = "SELECT pro_coinst,pro_coexis,pro_stock,pro_stkuni "+
          " FROM V_ARTICULO WHERE pro_codi = " +proCodi;
      if (!dt.select(s))
        return  stkPart;
      stkPart.setControlInd(dt.getInt("pro_coinst") != 0);
      stkPart.setControlExist(dt.getString("pro_coexis").equals("S"));
      stkPart.setKilos(Formatear.redondea(dt.getDouble("pro_stock"),2));      
      stkPart.setUnidades(dt.getInt("pro_stkuni"));
      s = "SELECT * FROM V_STKPART WHERE " +
        " EJE_NUME= " + ejeLot +
        " AND EMP_CODI= " + empLot +
        " AND PRO_SERIE='" + serLot + "'" +
        " AND pro_nupar= " + numLot +
        " and pro_numind= " + numind +
        " and pro_codi= " + proCodi+
        (almCodi!=0?" and alm_codi = "+almCodi:"")+
          " order by stp_unact desc"; // Para que me muestre los que tenga unidades primero
    if (!dt.select(s))
    {
       s="select * from v_config where emp_codi="+empLot+
           " and cfg_tipemp=2"; // Plantacion.
       if (dt.select(s))
       {
           s = "SELECT * FROM V_STKPART WHERE " +
            " EJE_NUME= " + ejeLot +
            " AND EMP_CODI= " + empLot +
            " AND PRO_SERIE='" + serLot + "'" +
            " AND pro_nupar= " + numLot +
            " and pro_numind= " + numind+          
            (almCodi!=0?" and alm_codi = "+almCodi:"");
           if (!dt.select(s))
           {
                stkPart.setEstado(StkPartid.INDIV_NOT_FOUND);           
                return stkPart;
            }
       }
       else
       {
            stkPart.setEstado(StkPartid.INDIV_NOT_FOUND);           
            return stkPart;

       }
    }
    stkPart.setEstado(StkPartid.INDIV_OK);     
    stkPart.setLockIndiv(dt.getInt("stk_block")!=0);   
    stkPart.setKilos(Formatear.redondea(dt.getDouble("stp_kilact"),2));
    stkPart.setUnidades(dt.getInt("stp_unact"));
    stkPart.setFechaCad(dt.getDate("stp_feccad"));
    return stkPart;
  }
  public void setStockPartidas(StkPartid stkPart)
  {
      this.stkPart=stkPart;
  }
  public StkPartid getStockPartidas()
  {
     return this.stkPart;
  }
  void debug(String msg,EntornoUsuario EU)
  {
    if (EU==null)
      return;
    if (EU.debug)
      System.out.println("utildesp: "+msg);
  }
  /**
   * Devuelve el proximo numero de individuo a asignar a un producto.
   * @param dt Conexion con la BD
   * @param proCodi int Producto (NO se utiliza)   *
   * @param ejeLot int Ejer. Lote
   * @param empLot int Emp. Lote
   * @param serLot String Serie de Lote
   * @param numLot int Numero de Lote
   * @throws SQLException Error BD
   * @return int Proximo numero de Ind. para una ejerc./emp-serie/lote.
   * NO puede haber un mismo numero individuo para diferentes productos (por comodidad del usuario)
   *
   */
  public static int getMaxNumInd(DatosTabla dt,int proCodi,int ejeLot, int empLot, String serLot, int numLot) throws
      SQLException
  {
    String s = "SELECT MAX(pro_numind) as numind FROM v_stkpart "+
        " WHERE eje_nume = " + ejeLot +
        " and emp_codi = " + empLot +
        " and pro_serie = '" + serLot + "'" +
        " and pro_nupar = " + numLot;
//   debug("getMaxNumInd: "+s);
    dt.select(s);

    int nInd = dt.getInt("numind", true);
    nInd++;
    return nInd;
  }
//  public void setActStkPart(ActualStkPart actStkPart)
//  {
//    this.actStkPart=actStkPart;
//  }
//
//  public ActualStkPart getActStkPart()
//  {
//    return this.actStkPart;
//  }

  public void iniciar(DatosTabla dtAdd,int ejeNume,int empCodi,int almDest,int almOrig,EntornoUsuario EU)
  {
    this.dtAdd=dtAdd;
    this.ejeNume=ejeNume;
    this.empCodi=empCodi;
    this.almDest=almDest;
    this.almOrig=almOrig;
    this.numDesp=0;
    this.EU=EU;
//    if (actStkPart==null)
//      actStkPart=new ActualStkPart(dtAdd,EU.em_cod);
  }
  
  public void setGrupoDesp(int grdNume)
  {
    this.grdNume=grdNume;
  }
  public int getGrupoDesp()
  {
    return this.grdNume;
  }

  /**
   * Devuelve si esta activado buscar Nuevos individuos en despiece
   * @return true esta activado
   */
 public boolean isBuscaIndDesp() {
        return buscaIndDesp;
    }
 /**
   * Busca  nuevo Individuo en despiece.
   * Hace que busque en otros productos de entrada en despieces, cuando
   * el individuo ya ha sido elegido más de repiteIndiv
     * @param buscaIndDesp
  *  @see setRepiteIndiv
   * 
   */
    public void setBuscaIndDesp(boolean buscaIndDesp) {
        this.buscaIndDesp = buscaIndDesp;
    }
    
  public int guardaLinDesp(int ejeLot, int empLot, String serLot, int numLot, int nInd,
                           int proCodi, double kilos,int numPie, String feccad,int defOrden,
                           int uniCaj, double precCost,
                           int defCerra) throws Exception
  {
    return guardaLinDesp(ejeLot, empLot, serLot, numLot, nInd, numDesp,proCodi, kilos, numPie,feccad,defOrden,uniCaj, precCost,
                         defCerra);
  }
  /**
   * Establece si la linea de despiece es tipo produccion o no. (Defecto es 'N')
   * @param tipoProdu 
   */
  public void setTipoProduccion(String tipoProdu)
  {
      this.tipoProduc=tipoProdu;
  }
   public void setTipoProduccion(boolean tipoProdu)
  {
      this.tipoProduc=tipoProdu?"S":"N";
  }
/**
 * Guarda una linea de salida del despiece
 * @param ejeLot
 * @param empLot
 * @param serLot
 * @param numLot
 * @param nInd
 * @param numDesp
 * @param proCodi
 * @param kilos
 * @param numPie
 * @param feccad
 * @param defOrden
 * @param uniCaj
 * @param precCost
 * @param defCerra
 * @return Numero de orden insertado 
 * @throws SQLException
 */
  public int guardaLinDesp(int ejeLot,int empLot,String serLot,int numLot,int nInd,
                    int numDesp,int proCodi,double kilos,int numPie,
                    String feccad, int defOrden,int uniCaj,double precCost,
                    int defCerra) throws SQLException,ParseException
 {
   String s;
   if (defOrden==0)
   {
     s = "SELECT MAX(def_orden) as def_orden " +
         " from v_despfin WHERE  eje_nume = " +ejeNume +
         " and emp_codi = " + empCodi +
         (grdNume==0? " and deo_codi = " + numDesp:" and def_numdes = " + grdNume);
//         " and deo_codi = " + numDesp;
     dtAdd.select(s);
     defOrden = dtAdd.getInt("def_orden", true);
     defOrden++;
   }
   s = "SELECT * FROM v_despfin " +
       " WHERE eje_nume = " + ejeNume +
       " and emp_codi = " + empCodi +
       " and deo_codi = " + numDesp +
       " and def_orden = " + defOrden;

   if (! dtAdd.select(s, true))
   {
     dtAdd.addNew();
     dtAdd.setDato("emp_codi", empCodi);
     dtAdd.setDato("eje_nume", ejeNume);
     dtAdd.setDato("deo_codi", numDesp);
     dtAdd.setDato("def_orden", defOrden);
     dtAdd.setDato("def_ejelot", ejeLot);
     dtAdd.setDato("def_emplot", empLot);
     dtAdd.setDato("def_serlot", serLot);
     dtAdd.setDato("pro_lote",numLot);
     dtAdd.setDato("pro_numind",nInd);
     dtAdd.setDato("def_tippes","V");
     dtAdd.setDato("def_numdes",grdNume); // Grupo del N. Desp.
     dtAdd.setDato("alm_codi",almDest);
     dtAdd.setDato("usu_nomb",EU.usuario);
     dtAdd.setDato("def_unicaj",uniCaj);
     dtAdd.setDato("def_cerra",defCerra);
     dtAdd.setDato("def_prcost",precCost);
     dtAdd.setDato("def_tippro",tipoProduc);
     dtAdd.setDato("def_tiempo", "current_timestamp");
   }
   else
     dtAdd.edit(dtAdd.getCondWhere());

   java.util.Date fecha;
   try
   {
     fecha=Formatear.getDate(feccad,"dd-MM-yyyy");
     int ano=Integer.parseInt(Formatear.getFecha(fecha,"yyyy").trim());
     if (ano<2000)
       throw new Exception("Año: "+ano+" NO VALIDO");
   } catch (Exception k1)
   {
     loger.warn("Usuario: "+EU.usu_nomb+"\nError en fecha despices\nDespiece: "+
                                          " and deo_codi = " + numDesp +
                                          " and def_orden = " + defOrden+" con fecha cad: "+feccad+" NO VALIDO\n"+
                                          k1.getMessage(),k1);
     fecha=null;
   }
   dtAdd.setDato("pro_codi", proCodi);//pro_codlE.getValorInt());
   dtAdd.setDato("def_numpie",numPie);//def_numpieE.getValorInt());
   dtAdd.setDato("def_kilos", kilos); //def_kilosE.getValorDec());
   dtAdd.setDato("def_feccad",fecha);//def_kilosE.getValorDec());

   dtAdd.update(dtAdd.getStatement());
//   s="update stockpart set stp_feccad='"+Formatear.getFechaDB(feccad) +"' where pro_nupar="+ numLot+
//                " and pro_serie='"+serLot+"' "+
//                " and eje_nume="+ejeLot+
//                " and pro_codi = "+proCodi+
//                " and pro_numind="+nInd;
//   dtAdd.executeUpdate(s);
   return defOrden;
 }
  
  public void copiaDespieceNuevo(DatosTabla dt, DatosTabla dtUpd,String coment,String usuario, 
          int ejeNume, int deoCodi) throws SQLException
  {
      String s="select max(his_rowid) as his_rowid from deorcahis ";
      dt.select(s);
      int rowid=dt.getInt("his_rowid",true);
      rowid++;
      s = "select * from desporig WHERE eje_nume = " + ejeNume +
          " and deo_codi = " + deoCodi;
      if (dt.select(s))
      {
         dtUpd.addNew("deorcahis");
         dt.copy(dtUpd,usuario, coment,rowid);    
      }
      s = "select * from desorilin WHERE eje_nume = " + ejeNume +
          " and deo_codi = " + deoCodi;
      if (dt.select(s))
      {
         dtUpd.addNew("deorlihis");
         dt.copy(dtUpd,usuario, coment,rowid);    
      }
      s = "select * from v_despfin WHERE eje_nume = " + ejeNume+
          " and deo_codi = " +deoCodi;
      if (dt.select(s))
      {
         dtUpd.addNew("desfinhis");
         dt.copy(dtUpd, usuario, coment,rowid);    
      }
  }
 /**
  * Devuelve el Numero de despiece.
  * @return 
  */
 public int getDeoCodi()
 {
     return deoCodi;
 }
 /**
  * Devuelve el ejercicio del Numero de despiece.
  * @return 
  */
 public int getEjeDesp()
 {
     return deoAno;
 }
 /**
  * Devuelve numero de serie compra producto origen
  * @return Numero Serie Albaran Compra
  */
 public String getAccSerie()
 {
     return accSerie;
 }
  /**
  * Devuelve numero de albaran compra producto origen
  * @return Numero Albaran Compra
  */
 public int getAccNume()
 {
     return accNume;
 }
  /**
  * Devuelve ejercicio del albaran compra producto origen
  * @return  ejercicio del albaran compra
  */
 public int getAccAno()
 {
     return accAno;
 }
 /**
  * Devuelve el precio de compra
  * @return Precio Compra.
  */
 public double getPrecioCompra()
 {
     return accPrcom;
 }
 /**
  * Establece que los despieces solo se deben de buscar del tipo mandado.
  * Eso influye solo para  costos, si el despiece no es de costo, ignorara el costo.
  * Por defecto se buscara el ultimo despiece de cualquier tipo
  * Usado para saber cuando se metio un producto a congelar.
  * @param tidCodi 
  */
 public void setTidCodi(int tidCodi)
 {
     this.tidCodi=tidCodi;
 }
 public int getTidCodi()
 {
    return this.tidCodi;
 }
// public void actOriDesp(DatosTabla dtAdd,Desporig desorig) throws SQLException
// {
//   String s = "select * from desporig WHERE eje_nume = " + desorig.getId().getEjeNume() +
//          " and deo_codi = " + desorig.getId().getDeoCodi();
//   if (! dtAdd.select(s))
//       throw new SQLException("Cabecera de despiece NO ENCONTRADA "+s);
//   dtAdd.edit("desporig"); 
//   dtAdd.update();
// }
 public void setFechaVal(Date fecval)
 {
     deoFecval=fecval;
 }
 public Date getFechaVal()
 {
     return deoFecval;
 }
 public int getPrvCodi()
 {
     return prvCodi;
 }
 public void setPrvCodi(int prvCodi)
 {
     this.prvCodi=prvCodi;
 }
// public void actDatCab(DatosTabla dt,String fecDesp,int ejloge,int emloge,String seloge,
//                       int nuloge,int deoCerra) throws SQLException
// {
//   dt.setDato("deo_fecha", fecDesp, "dd-MM-yyyy");
//   dt.setDato("deo_almori", almOrig);
//   dt.setDato("deo_almdes", almDest);
//   dt.setDato("deo_ejloge", ejloge);
//   if (emloge>0)
//    dt.setDato("deo_emloge", emloge);
//   dt.setDato("deo_seloge", seloge);
//   dt.setDato("deo_nuloge", nuloge);
//   dt.setDato("deo_cerra", deoCerra);
// }
 public int incNumDesp(DatosTabla dt)  throws SQLException
 {
   return incNumDesp(dt,empCodi,ejeNume);
 }
 /**
  * Devuelve el proximo numero de despiece
  * @param dt DatosTabla
  * @param empCodi int Empresa
  * @param ejeNume int Ejercicio
  * @throws SQLException
  * @return int Proximo Numero de despiece
  */
 public static int incNumDesp(DatosTabla dt,int empCodi,int ejeNume) throws SQLException
 {
   String s = "SELECT num_despi FROM v_numerac WHERE emp_codi = " + empCodi+
       " AND eje_nume = " + ejeNume;
    if (! dt.select(s,true))
      throw new SQLException("NO encontrado Registro para esta empresa y numero en tabla de numeraciones\n"+s);
    int numDespInt=dt.getInt("num_despi")+1;
    dt.edit(dt.getCondWhere());
    dt.setDato("num_despi",numDespInt);
    dt.update();
    return numDespInt;
 }
  public String getDatMatadero()
  {
      return this.datmat;
  }
  public void setDatMatadero(String datMatadero)
  {
      this.datmat=datMatadero;
  }
   public String getDirEmpresa()
  {
      return this.diremp;
  }
  public void setDirEmpresa(String dirEmpresa)
  {
      this.diremp=dirEmpresa;
  }
  public void setLogotipo(String logotipo)
  {
      logo=logotipo;
  }
  public String getLogotipo()
  {
      return logo;
  }
  
 /**
  * Imprime etiqueta
  * @param TIPOETIQ Tipo etiqueta. Segun tabla etiquetas
  * @param dtStat DatosTabla para buscar tipo etiqueta
  * @param proCodi Codigo Articulo
  * @param nombArt Nombre Articulo
  * @param indiceEti Indice Etiqueta (C, D...)
  * @param nuloge Numero Lote
  * @param ejloge Ejercicio de Lote
  * @param seloge Serie de Lote
  * @param numInd Individuo
  * @param kilos Kilos
  * @param fecDesp Fecha despiece (String)
  * @param fecProd Fecha de Producción o de congelación (Date)
  * @param fecCad Fecha Caducadad (String)
  * @param fecSacr Fecha Ssacrificio (Date)
  * @param cadProdDate Fecha Caducidad (Date)
  * @param grupoLote Grupo del Lote a imprimir 
  * @throws Exception
  */
 public void imprEtiq(int TIPOETIQ, DatosTabla dtStat,
                      int proCodi, String nombArt,  String indiceEti, int nuloge, String ejloge,
                      String seloge, int numInd,
                      double kilos, java.util.Date fecDesp, java.util.Date fecProd, java.util.Date fecCad,
                      java.util.Date fecSacr,java.util.Date cadProdDate,int grupoLote) throws  Throwable
    {
        int etiquetaInterior=etiqueta.getCodigoEtiqInterior(EU);
        actualConservar(proCodi, dtStat);
        if (etiq == null) {
            etiq = new etiqueta(EU);
        }
        etiq.setTipoEtiq(dtStat, EU.em_cod, TIPOETIQ);
//        if (TIPOETIQ==0)
//            logo=null;
//        else
            logo=etiq.getLogotipo();
           
        CodigoBarras codBarras = new CodigoBarras(indiceEti,ejloge,seloge,nuloge,proCodi,
             TIPOETIQ==etiquetaInterior?0:numInd,
            TIPOETIQ==etiquetaInterior?0:kilos,
            grupoLote);
      
            
        etiq.iniciar(TIPOETIQ !=etiquetaInterior?codBarras.getCodBarra():codBarras.getLote(false),
            codBarras.getLote(TIPOETIQ!=etiquetaInterior)  ,
                "" + proCodi, nombArt,
                paisNacimientoNombre, paisEngordeNombre, despiezadoE,
                ntrazaE,kilos,
                conservarE, sacrificadoE,null,
                fecProd,fecCad,fecSacr);
        etiq.setDatMatadero(datmat);
        etiq.setDirEmpresa(diremp);
        etiq.setLogotipo(logo);
        
        etiq.listarDefec();
    }


    public String getAcpPainac() {
        return acp_painac;
    }
    public void setAcpPainac(String paisNacimiento) {
        acp_painac=paisNacimiento;
    }
    public void setPaisNacimiento(String paisNacimiento)
    {
        paisNacimientoNombre=paisNacimiento;
    }
    public String getPaisNacimiento()
    {
        return paisNacimientoNombre;
    }
    public void setPaisEngordeNombre(String paisEngorde)
    {
        paisEngordeNombre=paisEngorde;
    }
    public void setMatadero(String sacrificado)
    {
        sacrificadoE=sacrificado;
    }
    /**
     *  Devuelve el Nombre Pais del matadero + Nº Reg. Sanitario del Matadero
     * @return 
     */
    public String getMatadero()
    {
        return sacrificadoE;
    }
    /**
     * Establece el String con el nombre del Pais de la Sala Despiece + Nº Reg.Sanitario Sala Despiece
     * @param despiezado 
     */
    public void setSalaDespiece(String despiezado)
    {
        this.despiezadoE=despiezado;
    }
    /**
     * Devuelve el Nombre Pais de sala despiece + Nº Reg. Sanitario de sala despiece
     * @return String
     */
    public String getSalaDespiece()
    {
        return despiezadoE;
    }
    public String getMatCodi() {
        return stp_matad;
    }
    public void setMatCodi(String matadero) {
         stp_matad=matadero;
    }
    public String getSdeCodi() {
        return stp_saldes;
    }
   public void setSdeCodi(String salaDespiece) {
        stp_saldes=salaDespiece;
    }
    public String getPaisEngordeCodigo() {
        return acp_engpai;
    }
    public void setPaisEngordeCodigo(String paisEngorde) {
        acp_engpai=paisEngorde;
    }

    public String getPaisSacrificioCodigo() {
        return acp_paisac;
    }
    public void setPaisSacrificioCodigo(String paisSacrificio) {
        acp_paisac=paisSacrificio;
    }
    /**
     * Numero de crotal . A veces se le llama trazabilidad
     * @return 
     */
    public String getNumCrot() {
        return ntrazaE;
    }
    public void setNumCrot(String numCrotal) {
         ntrazaE=numCrotal;
    }

    public String getMatNrgsa() {
        return mat_nrgsa;
    }

    public String getSdeNrgsa() {
        return sde_nrgsa;
    }
    public String getPaisSacrificioNombre()
   {
     return paisSacrificioNombre;
   }
   public void setPaisSacrificioNombre(String paisSacrifNombre)
   {
     paisSacrificioNombre=paisSacrifNombre;
   }
   public java.util.Date getFecSacrif()
   {
     return Formatear.getDate2000(fecSacrE);
   }
       

   public void setFecSacrif(java.util.Date fechaSacrificio)
   {
       fecSacrE=fechaSacrificio;
   }
   public int getEjeLotCompra() {
        return ejeLotCompra;
   }

    public int getProCodiCompra() {
        return proCodiCompra;
    }

    public int getProIndiCompra() {
        return proIndiCompra;
    }

    public int getProLoteCompra() {
        return proLoteCompra;
    }

    public String getProSerieCompra() {
        return proSerieCompra;
    }
   /**
    * Devuelve la fecha de caducidad real (si es congelado, la de congelado)
    * @return Date 
    */
   public java.util.Date getFechaCaducidad()
   {
        try {
            return Formatear.getDate2000(
                Formatear.getDate(feccadE, "dd-MM-yyyy"));
        } catch (ParseException ex) {
            return null;
        }
   }
   public void setFechaCaducidad(java.util.Date fecCaduc)
   {
       feccadE=Formatear.getFecha(fecCaduc,"dd-MM-yyyy");
   }
   /**
    * Devuelve la fecha de caducidad del ultimo despiece 
    * Equivalente a fecha caducidad si hay despieces.
    * @return 
    */
   public Date getFechaCadDesp()
   {
       return fecCadDesp;
   }
   /**
    * Devuelve la fecha de Caducidad del Proveedor.
    * @return fecha caducidad segun  Proveedor
    */
    public java.util.Date getFecCadPrv() {
        return fecCadPrvE;
    }
     /**
    * Establece la fecha de Caducidad del Proveedor.
    * @param Fecha caducidad
    */
    public  void setFecCadPrv(Date fecCaduc) {
        fecCadPrvE=fecCaduc;
    }
     /**
    * Devuelve la fecha de Compra del Proveedor.
    * @return date con fecha Compra
    */
    public Date getFecCompra() {
        return fecCompraE;
    }
    public void setFecCompra(java.util.Date fechaCompra)
    {
        fecCompraE=fechaCompra;
    }
     public Date getFecDesp() {
        return fecDespE;
    }
    public void setFechaProduccion(Date fechaProd)
    {
        fecProdE=fechaProd;
    }
     /**
      * Devuelve la fecha produccion. 
      * Si es null devolvera la fecha Desp. Si esta tambien es nula devolvera la fecha Compra
      * @return 
      */
   public Date getFechaProduccion()
   {
       if (fecProdE!=null)
         return Formatear.getDate2000( fecProdE);
       if (fecDespE!=null)
         return Formatear.getDate2000(fecDespE);
       return Formatear.getDate2000(fecCompraE);
   }
   /**
    * Si es puesto a false en el caso de encontrar el individuo a buscar en despieces
    * NO lo seguira buscando en compras.
    * Por defecto es 'true'. La función buscaDatCompra() pone este valor a true de nuevo.
    * @param swCompra 
    */
   public void setBuscaCompra(boolean swCompra)
   {
       swBuscaCompra=swCompra;
   }
   public int getPrvCompra()
   {
       return prvCompra;
   }
   /**
   * Limpia el HashSet donde se guardan los individuos
   * @see setRepiteIndiv(int numInd)
   */
   public void resetIndividuos() {
         indiv.removeAllElements();
         crotales.removeAllElements();
   }
   /**
    * Permite repetir individuos  en origen de despiece
    * Por defecto es 0. Es decir no limita el numero de individuos a repetir
    * 
    * @see resetIndividuos
    */
   public void setRepiteIndiv(int numRepiteInd)
   {
       repiteIndiv=numRepiteInd;
   }
   public int getRepiteIndiv()
   {
       return repiteIndiv;
   }
    public String getMsgAviso() {
        return msgAviso;
    }
    public String getPaisEngorde()
    {
        return paisEngordeNombre;
    }
    public boolean hasCambio()
    {
        return cambio;
    }
    public void setCambio(boolean cambio)
    {
        this.cambio=cambio;
    }
}
